package com.espn.snipplet.jobs

import com.znet.snipplet.ApiService
import com.znet.snipplet.ErrorService
import com.znet.snipplet.Gist
import com.znet.snipplet.GistService;
import com.znet.snipplet.LanguageService;


class GistUpdaterJob {
	
	def concurrent = false
	
    def timeout = 1000L * 60L * 10L // execute job once per 10min
	
	ApiService apiService
	GistService gistService
	ErrorService errorService
	LanguageService languageService

    def execute() {
		try {
			println "REDUCING GISTS..."
			def deletes = gistService.reduceGists();
			println "DONE REDUCING GISTS: ${deletes}"
			
			println "UPDATING GISTS..."
			loadGists();
			
			println "UPDATING LANGUAGES..."
			languageService.countLanguages();
		}
		catch (error) {
			println "ERROR UPDATING: ${error}"
		}
    }
	
	def loadGists() {
		
		def page = 0;
		def found = true;
		while (found) {
			page++;
			def json = apiService.invokeAll('/gists/public', page);
			json.each { entry ->
				try {
					println "DISCOVER GIST: ${entry.url}"
					Gist.withNewSession { s ->
						if (!gistService.discoverGist(entry)) {
							found = false;
						}
					}
				}
				catch (Exception e) {
					println "Error processing gist (${entry.url}): ${e.message}"
					errorService.saveError('gist', entry.url, e);
				}
			}
		}
	}
}
