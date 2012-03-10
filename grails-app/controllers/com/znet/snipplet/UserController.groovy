package com.znet.snipplet

import java.util.Date;

class UserController {

	UserService userService
	GistService gistService
	
	def snipplets() {
		if (session.user == null) {
			redirect(controller:'snipplet', action:'list');
			return;
		} 

		// TODO: lookup snippets
		def login = session.user.username;
		def gists = gistService.getUserGists(login);
		if (gists == null) { gists = []; }
		
		[ 'gists' : gists ]
	}
	
	def snip() {
		
		userService.saveSnippet(user:session.user, title:params.title, description:params.description,
			                    gist:params.gist, language:params.language, category:params.category)
		
		render text:'Yay!'
	}

}
