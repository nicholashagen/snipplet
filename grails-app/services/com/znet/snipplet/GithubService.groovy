package com.znet.snipplet

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*


class GithubService {

	String clientId = null; // LOCAL_CLIENT_ID;
	String clientSecret = null; // LOCAL_CLIENT_SECRET;
	
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
}
