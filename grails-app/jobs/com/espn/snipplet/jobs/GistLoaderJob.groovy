package com.espn.snipplet.jobs

import com.znet.snipplet.ApiService
import com.znet.snipplet.ErrorService
import com.znet.snipplet.Gist
import com.znet.snipplet.GistService;
import com.znet.snipplet.LanguageService;
import com.znet.snipplet.Status

class GistLoaderJob {
	
	def concurrent = false
	
	static triggers = { }
	
	ApiService apiService
	GistService gistService
	ErrorService errorService
	LanguageService languageService

    def execute() {
		def nextTimestamp = 0L;
		
		try { nextTimestamp = loadGists(); }
		catch (error) {
			nextTimestamp = 1000L * 60L * 5L;
			println "ERROR INVOKING LOADER: ${error}"
		}
		
		if (nextTimestamp > 0) {
			Date nextDate = new Date(System.currentTimeMillis() + nextTimestamp);
			println "SCHEDULING FOR ${nextTimestamp}: ${nextDate}"
			GistLoaderJob.schedule(nextDate);
		}
    }
	
	def loadGists() {
		def status = Status.get(1);
		def page = status.lastPage + 1;
		if (page >= GistService.GIST_PAGES) { return -1; }

		println "REDUCING GISTS..."
		def deletes = gistService.reduceGists();
		println "DONE REDUCING GISTS: ${deletes}"
		
		println "LOADING GISTS AT PAGE ${page}"
		def start = System.currentTimeMillis();
		def count = loadGists(page);
		def elapsed = (System.currentTimeMillis() - start);
		
		println "UPDATING LANGUAGES"
		languageService.countLanguages();
		
		Status.withNewSession { s ->
			status = Status.get(1);
			status.lastPage = page;
			status.apiCalls += count;
			status.elapsedTime += elapsed;
			status.lastUpdated = new Date();
			status.save(flush:true);
		}
		
		double avgCount = status.apiCalls / status.lastPage;
		double pagesPerHour = (ApiService.RATE_LIMIT * 0.8) / avgCount;
		double minutesPerPage = 60.0 / pagesPerHour;
		println "PAGE: COUNT = ${count}, PG/HR = ${pagesPerHour}, MIN/PG = ${minutesPerPage}"
		return Math.floor(minutesPerPage * 60L * 1000L) as long
	}
	
	def loadGists(int page) {
		apiService.startMonitoring();
		def json = apiService.invokeAll('/gists/public', page);
		json.each { entry ->
			try { 
				println "DISCOVER GIST: ${entry.url}"
				Gist.withNewSession { s ->
					gistService.discoverGist(entry);
				}
			}
			catch (Exception e) {
				println "Error processing gist (${entry.url}): ${e.message}"
				errorService.saveError('gist', entry.url, e);
			}
		}
		
		int count = apiService.endMonitoring();
		return count;
	}
}
