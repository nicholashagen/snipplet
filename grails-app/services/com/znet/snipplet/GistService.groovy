package com.znet.snipplet

import java.net.URL;
import java.util.Date;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

class GistService {

	def transactional = true
	
	ApiService apiService;
	DateService dateService;
	ErrorService errorService;
	GithubService githubService;
	LanguageService languageService;
	ExecutorService executorService;
	
	static int MAX_GISTS = 500
	static int GIST_PAGES = 10000; // TODO: dynamically calculate from Link header
	static int GISTS_PER_PAGE = 100;
	static String API_URL = "https://api.github.com/gists/public";
	
	synchronized int reduceGists() {
		def gcount = Gist.count();
		if (gcount <= MAX_GISTS) { return 0; }
		
		def gists = Gist.findAllByLocked(false, [sort:"dateUpdated", order:"desc", offset:MAX_GISTS, max:1]);
		if (gists) {
			int count = 0;
			def gistId = gists[0].id;
			
			count += GistComment.executeUpdate("delete GistComment c where c.gist.id in (select id from Gist where id <= ${gistId} and locked is false)");
			count += GistFile.executeUpdate("delete GistFile f where f.history.id in (select id from GistHistory where gist.id <= ${gistId} and gist.locked is false)");
			count += GistHistory.executeUpdate("delete GistHistory h where h.gist.id in (select id from Gist where id <= ${gistId} and locked is false)");
			count += GistComment.executeUpdate("delete GistFork f where f.gist.id in (select id from Gist where id <= ${gistId} and locked is false)");
			count += Gist.executeUpdate("delete Gist g where g.id <= ${gistId} and locked is false");

			return count;
		}
		
		return 0;
	}
	
	def getUserGists(String username) {
		GithubUser account = GithubUser.findByUsername(username);
		println "FIND GIT GISTS ${username}: ${account}"
		if (!account) { return null; }
		
		User user = User.findByUsername(username)
		println "FIND USER GISTS ${username}: ${user}"
		
		def gists = new HashSet(Gist.findAllByUser(account))
		println "GISTS: ${gists.size()}"

		def snippets = Snippet.findAllByUser(user)
		gists.addAll(snippets*.gist)
		println "GISTS: ${gists.size()}"
		
		return gists
	}
	
	def getLatestGists(String language = null, Date dateUpdated = null) {
		Language lang = null;
		if (language != null) {
			lang = Language.findByAbbreviation(language);
		}
		
		def options = [sort:"dateUpdated", order:"desc", offset:0, max:25]; 
		if (lang == null) {
			if (dateUpdated == null) {
				return Gist.list(options);
			}
			else {
				return Gist.findAllByDateUpdatedLessThan(dateUpdated, options);
			}
		}
		else {
			def criteria = Gist.createCriteria()
			def list = criteria.listDistinct {
				order "dateUpdated", "desc"
				maxResults(25)
				firstResult(0)
				
				if (dateUpdated) {
					lt('dateUpdated', dateUpdated)
				}
				
				history {
					files {
						eq('language', lang)
					}
				}
			}
			
			return list;
		}
	}
	
	def lookupGist(def url) {
		Gist gist = Gist.findByApiUrl(url);
		if (!gist) {
			def json = apiService.invoke(url);
			gist = (json.isNull() ? null : discoverGist(json));
		}
		
		return gist
	}
	
	def discoverGist(def entry) {

		println "Processing Gist(${entry.id}) at ${entry.url}"
		
		// check if gist exists yet and stop
		Gist gist = Gist.get(entry.id);
		if (gist) {
			def dateUpdated = dateService.parseDate(entry.updated_at);
			if (dateUpdated <= gist.dateUpdated) {
				println "    Gist(${entry.id}) already processed"
				return null;
			}
		}

		// initialize if not initialized
		if (!gist) {
			gist = new Gist()
			gist.id = entry.id;
		}
		
		// lookup user
		GithubUser gitUser = githubService.lookupUser(entry.user);

		// calculate default language
		def language = null;
		def defaultLanguage = false;
		entry.files.each { key, value ->
			def found = null
			if (!value.language) {
				found = languageService.getDefaultLanguage();
			}
			else {
				found = languageService.lookupLanguage(value.language);
			}
			
			if (language == null) {
				language = found;
			}
			else if (!defaultLanguage && language != found) {
				defaultLanguage = true;
				language = languageService.getDefaultLanguage();
			}
		}

		// create gist
		gist.valid = true;
		gist.locked = false;
		gist.name = entry.description ?: null;
		gist.description = entry.description ?: null;
		gist.isPublic = entry['public'];
		gist.apiUrl = new URL(entry.url);
		gist.htmlUrl = new URL(entry.html_url);
		gist.pushUrl = entry.git_push_url;
		gist.pullUrl = entry.git_pull_url;
		gist.dateCreated = dateService.parseDate(entry.created_at);
		gist.dateUpdated = dateService.parseDate(entry.updated_at);
		gist.dateDiscovered = dateService.now();
		gist.numberOfComments = entry.comments ?: 0;
		gist.language = language;
		gist.user = gitUser;
		
		// load forks and history/files
		discoverForksAndHistory(gist);
		
		// load comments, if available
		if (gist.numberOfComments > 0) {
			discoverComments(gist);
		}

		// save gist and files
		gist.save(flush:true);

		// create user snippets
		User user = User.findByGithubAccount(gitUser);
		if (user) {
			Snippet snippet = Snippet.findByUserAndGist(user, gist);
			if (!snippet) {
				snippet = new Snippet();
				snippet.user = user;
				snippet.gist = gist;
				snippet.title = gist.description;
				snippet.description = gist.description;
				snippet.language = language;
				snippet.category = null;
				snippet.dateCreated = gist.dateCreated;
				snippet.dateModified = gist.dateUpdated;
				snippet.save(flush:true);
				
				// lock gist from removals
				gist.locked = true;
				gist.save(flush:true);
			}
		}
		
		// return gist
		return gist;
	}	
	
	def discoverForksAndHistory(Gist gist) {
		def json = apiService.invoke("/gists/${gist.id}");
		if (!json) {
			println "UNABLE TO GET DATA: ${gist.id}"
		}

		json?.forks.each {

			// create gist
			println "Looking up Gist Fork (${it.url}) for Gist (${gist.apiUrl})"
			Gist forked = lookupGist(it.url);
			
			// create fork
			GistFork fork = new GistFork();
			fork.gist = gist;
			fork.fork = forked;
			fork.dateCreated = dateService.parseDate(it.created_at);
			gist.addToForks(fork);
		}

		// process history
		json?.history.each {
			// create history
			GistHistory history = new GistHistory();
			history.gist = gist;
			history.apiUrl = new URL(it.url);
			history.versioning = it['version'] ?: 'N/A';
			history.user = it.user ? githubService.lookupUser(it.user) : gist.user
			history.deletions = it.change_status?.deletions ?: 0;
			history.additions = it.change_status?.additions ?: 0;
			history.totalUpdates = it.change_status?.total ?: 0;
			history.dateCommitted = dateService.parseDate(it.committed_at);
			
			discoverFiles(history);
			
			gist.addToHistory(history);
		}
	}
	
	def discoverFiles(GistHistory history) {
		def json = apiService.invoke(history.apiUrl);
		json.files?.each { key, value ->
			def found = null
			if (!value.language) {
				found = languageService.getDefaultLanguage();
			}
			else {
				found = languageService.lookupLanguage(value.language);
			}
			
			GistFile file = new GistFile();
			file.name = key;
			file.mimeType = value.type ?: null;
			file.language = found;
			file.fileSize = value.size ?: 0;
			file.filename = value.filename ?: null;
			file.content = value.content ?: '';
			file.rawUrl = new URL(value.raw_url);
			
			history.addToFiles(file);
		}
	}
	
	def discoverComments(Gist gist) {
		// process comments
		def json = apiService.invoke("/gists/${gist.id}/comments");
		json.each {
			def comment = new GistComment();
			comment.gist = gist;
			comment.apiUrl = new URL(it.url);
			comment.text = it.body ?: '';
			comment.user = githubService.lookupUser(it.user);
			comment.dateCreated = dateService.parseDate(it.created_at);
			gist.addToComments(comment);
		}
	}

}
