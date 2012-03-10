package com.znet.snipplet


import grails.converters.JSON;
import groovy.json.JsonSlurper
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class LoginController {

	ApiService apiService;
	GistService gistService;
	UserService userService;
	ErrorService errorService;
	GithubService githubService;
	
	static int TIMEOUT = 1000 * 60 * 5;
	static String JUNK = "<!-- this frame uses comet style techniques with JSONP to load individual messages -->"
	
    def index() { 
		[ "clientId" : githubService.clientId ];
	}

	def logout() {
		session.invalidate()
		redirect(controller:'snipplet', action:'list');
	}
	
	def info() {
		String url = 'https://api.github.com/users/nicholashagen/gists';
		discoverGists(url);
		
		[ user : session.user ]
	}
	
	def status(int status) {
		def loginState = session.loginState
		def cstatus = loginState ? loginState.status : 0
		def count = loginState ? loginState.count : 0
		
		if (status == cstatus) {
			def ctx = startAsync()
			ctx.timeout = 5000
			ctx.start {
				synchronized (session.loginState) {
					try { session.loginState.wait(2500); }
					catch (Exception e) { /* ignore */ }
				}

				loginState = session.loginState
				cstatus = loginState ? loginState.status : 0
				count = loginState ? loginState.count : 0
				
				def json = [ status:cstatus, count:count ]
				render json as JSON
	
				ctx.complete();
			}
		}
		else {
			def json = [ status:cstatus, count:count ]
			render json as JSON
		}
	}

	def login() {
		if (!session.token || !session.account) {
			throw new IllegalStateException("ACK: NO SESSION TOKEN ON LOGIN");
		}
		
		User user = null;
		if (session.account?.login) {
			user = User.findByUsername(session.account.login);
			if (!user) {
				def account = githubService.lookupUser(session.account);
				if (account) {
					user = userService.createUser(account);
				}
			}

			// login user
			loginUser(user);
		}
		
		// setup synchronization state if gists found
		if (session.account.public_gists > 0) {

			// initialize state			
			session.loginState = new LoginState(count:session.account.public_gists);
		
			// process in background
			def ctx = startAsync()
			ctx.timeout = TIMEOUT
			ctx.start {
				
				// discover user gists
				String url = session.account.url + '/gists';
				discoverGists(url);

				// reset state and complete
				session.loginState = null;
				
				// redirect to profile page
				// redirect(controller:'user', action:'snipplets')
				render createLink(controller:'user', action:'snipplets')
				
				// complete request
				ctx.complete();
			}
		}
	}

	def github() {
		if (params.error) {
			throw new IllegalStateException("ACK: ${params}");
		}
		else if (params.code) {
			// TODO: change to use startAsync
			
			def http = new HTTPBuilder('https://github.com')
			
			def token = null, account = null; 
			http.request( POST, TEXT ) {
				uri.path = '/login/oauth/access_token'
				uri.query = [ client_id : githubService.clientId, client_secret : githubService.clientSecret, code : params.code ]
				headers.Accept = '*/*'

				response.success = { resp, reader ->
					def input = reader.text
					def pattern = ~/^.*access_token=([^&]+).*$/
					def matcher = pattern.matcher(input)
					
					if (matcher.matches()) {
						token = matcher.group(1)
					}
				}
				
				response.failure = { resp, reader ->
					println "Handled URL: ${uri}"
					println "My response handler got response: ${resp.statusLine}"
					println "Response length: ${resp.headers.'Content-Length'}"
					resp.headers.each { h ->
						println "    HEADER: ${h}"
					}
					System.out << reader
				}
			}

			// only invoke if valid user
			if (token) {
				http = new HTTPBuilder('https://api.github.com')
				
				http.request( GET, JSON ) {
					uri.path = '/user'
					uri.query = [ client_id:githubService.clientId, access_token:token ]
					headers.Accept = 'application/json'
				
					response.success = { resp, reader ->
						def json = new JsonSlurper().parseText(reader.text)
						account = json;
					}

					response.failure = { resp ->
						println "Handled URL: ${uri}"
						println "My response handler got response: ${resp.statusLine}"
						println "Response length: ${resp.headers.'Content-Length'}"
						resp.headers.each { h ->
							println "    HEADER: ${h}"
						}
						System.out << reader
					}
				}
			}

			// update session
			session.token = token;
			session.account = account;

			// lookup existing user and login automatically
			boolean existing = false;
			if (account?.login) {
				println "FIND USER: ${account.login}"
				User user = User.findByUsername(account.login);
				if (user) {
					existing = true;
					loginUser(user);
				}
			}

			def json = session.account as JSON
			[ account:"${json}", existing:existing ];
		}
		else {
			throw new IllegalStateException("ACK: ${params}");
		}
	}
	
	def loginUser(User user) {
		
		// update user login information and state
		userService.loginUser(user);
		
		// save to session
		session.github = user.githubAccount
		session.user = user
		
		// TODO: drop cookie
	}
	
	def discoverGists(String apiUrl) {
		apiService.startMonitoring();
		println "DISCOVER GISTS: ${apiUrl}"
		// TODO: do as user
		def json = apiService.invokeAll(apiUrl, 1);
		json.each { entry ->
			try {
				println "DISCOVER USER GIST: ${entry.url}"
				Gist.withNewSession { s ->
					gistService.discoverGist(entry);
				}
			}
			catch (Exception e) {
				// TODO: handl error
				println "Error processing gist (${entry.url}): ${e.message}"
				errorService.saveError('gist', entry.url, e);
			}
			
			synchronized (session.loginState) {
				session.loginState.status++;
				session.loginState.notifyAll();
			}
		}
		
		int count = apiService.endMonitoring();
		return count;
	}
}

class LoginState {
	int count;
	int status;
}
