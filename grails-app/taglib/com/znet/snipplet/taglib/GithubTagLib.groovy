package com.znet.snipplet.taglib

import com.znet.snipplet.GithubService

class GithubTagLib {

	static namespace = "git"
	static returnObjectForTags = ['createLoginLink']
	
	static String OAUTH_URL = "https://github.com/login/oauth/authorize"
	
	GithubService githubService;
	
	def createLoginLink = { attrs, body ->
		def loginLink = createLink(controller:'login', action:'github')
		return "${OAUTH_URL}?client_id=${githubService.clientId}"
	}
}
