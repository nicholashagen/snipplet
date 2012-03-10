package com.znet.snipplet

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*


class GithubService {

	def grailsApplication
	
	private String clientId
	private String clientSecret
	
	GithubUser lookupUser(def json) {

		// lookup user or create if not found
		GithubUser gitUser = GithubUser.get(json.id);
		println "LOOKUP GITHUB USER: ${json.id} => ${gitUser}"
		if (!gitUser) {
			gitUser = new GithubUser();
			gitUser.id = json.id;
			gitUser.name = json.name;
			gitUser.username = json.login;
			gitUser.avatarUrl = new URL(json.avatar_url);
			gitUser.gravatarId = json.gravatar_id;
			gitUser.apiUrl = new URL(json.url);
			// gitUser.htmlUrl = new URL(json.html_url);
			gitUser.save();
		}
		
		return gitUser;
	}
	
	private String getClientId() {
		if (!clientId) {
			clientId = grailsApplication.config.com?.znet?.snipplet?.github?.clientId
			if (!clientId) {
				throw new IllegalStateException('missing com.znet.snipplet.github.clientId property in grails-app/conf/GithubConfig.groovy')
			}
		}
	}
	
	private String getClientSecret() {
		if (!clientSecret) {
			clientSecret = grailsApplication.config.com?.znet?.snipplet?.github?.clientSecret
			if (!clientSecret) {
				throw new IllegalStateException('missing com.znet.snipplet.github.clientSecret property in grails-app/conf/GithubConfig.groovy')
			}
		}
	}

}
