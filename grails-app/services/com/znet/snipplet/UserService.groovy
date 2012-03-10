package com.znet.snipplet

class UserService {

	DateService dateService
	LanguageService languageService
	
	Category lookupCategory(User user, def name) {
		return Category.findOrSaveWhere(user:user, name:name)
	}
	
	void saveSnippet(def params) {
		Snippet snippet = new Snippet();
		snippet.user = params.user;
		snippet.gist = Gist.get(params.gist);
		snippet.title = params.title;
		snippet.description = params.description;
		snippet.language = languageService.lookupLanguage(params.language)
		snippet.category = lookupCategory(params.user, params.category);
		snippet.dateCreated = dateService.now();
		snippet.dateModified = dateService.now();
		snippet.dateLastViewed = dateService.now();
		
		snippet.save()
	}
	
    def createUser(GithubUser account) {

		User user = new User()
		user.name = account.name
		user.username = account.username
		user.dateCreated = new Date();
		user.githubAccount = account;
		user.dateLastLoggedIn = new Date();
		user.save();
		
		return user;
    }
	
	def loginUser(User user) {
		user.dateLastLoggedIn = new Date();
		user.save();
	}
}
