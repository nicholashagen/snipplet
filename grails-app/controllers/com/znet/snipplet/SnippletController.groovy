package com.znet.snipplet

import org.grails.plugins.quartz.JobManagerService;

import com.espn.snipplet.jobs.GistLoaderJob;
import com.espn.snipplet.jobs.GistUpdaterJob;

class SnippletController extends DefaultController {

	GistService gistService
	JobManagerService jobManagerService
	
    def index() { 
		def gists = gistService.getLatestGists(params.start ?: 0, 20);
		[ 'user' : session.user, gists : gists ]
	}
	
	def status() {
		int count = Gist.count();
		Status status = Status.get(1);
		def jobs = jobManagerService.getAllJobs();
		// def runningJobs = jobManagerService.getRunningJobs();
		[ 'count' : count, 'status' : status, 'jobs' : jobs ]
	}

	def reduce() {
		def count = gistService.reduceGists();
		render "updated ${count} rows"
	}	
	
	def invoke(String operation, String job, String group) {
		if (operation == 'pause') {
			jobManagerService.pauseJob(group, job);
		}
		else if (operation == 'resume') {
			jobManagerService.resumeJob(group, job);
		}
		else if (operation == 'invoke') {
			def jobClass = this.getClass().getClassLoader().loadClass(job);
			jobClass.triggerNow();
		}
		
		render "success";
	}
	
	def list(String language) {
		println "LIST BY LANGUAGE: ${language}"
		def gists = gistService.getLatestGists(language);
		[ 'user' : session.user, gists : gists ]
	}
	
	def errors() {
		def errors = Error.list();
		[ 'errors' : errors ]
	}
	
	def more(String last, String language) {
		def gist = Gist.get(last);
		if (!gist) {
			return "no content available ${last}"
		}
		
		def gists = gistService.getLatestGists(language, gist.dateUpdated);
		[ 'user' : session.user, gists : gists ]
	}
	
	def load() {
		for (def page in 1..1000) {
			println "Loading Gists for Page ${page}"
			gistService.loadGists(page);
		}
			
		redirect(controller: 'snipplet', action:'index')
	}
	
	def refresh() {
		GistLoaderJob.triggerNow()
		redirect(controller: 'snipplet', action:'index')
	}
	
	def view() {
		println "VIEW: ${params.id}"
		def gistId = params.id
		
		def files = [] 
		Gist gist = Gist.get(gistId);
		println "RESULT: ${gist}"
		if (gist) {
			gistService.discoverGist(gist);
			files = GistFile.findAllByGist(gist);
		}
		else {
			redirect(controller: 'snipplet', action:'index')
		}
		
		[ gist: gist, files: files ]
	}
	
	def viewCode() {
		// TODO: lookup gist and then validate history/file on gist
		def gist = Gist.get(params.gist);
		if (!gist) {
			// render 404
			throw new IllegalStateException("invalid gist: ${params.gist}")
		}
		
		def history = gist.history?.find { it.versioning == params.history }
		if (!history) {
			// render 404
			throw new IllegalStateException("invalid history: ${params.gist} / ${params.history}")
		}
		
		def file = history.files?.find { it.name == params.file }
		if (!file) {
			// render 404
			throw new IllegalStateException("invalid file: ${params.gist} / ${params.history} / ${params.file}")
		}

		render file.content?.encodeAsHTML() ?: 'no content available';
	}
	
}
