package com.znet.snipplet

abstract class DefaultController {

	def languageService
	
	def afterInterceptor = { model ->
		model.languages = getLanguages();
	}

	protected List getLanguages() {
		return languageService.getLanguages();
	}
}
