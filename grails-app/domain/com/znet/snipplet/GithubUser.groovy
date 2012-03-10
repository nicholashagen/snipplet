package com.znet.snipplet

class GithubUser {

    static constraints = {
		name nullable: true, maxSize: 256
		apiUrl nullable: true, maxSize: 512
		htmlUrl nullable: true, maxSize: 512
		avatarUrl nullable: true, maxSize: 512
		gravatarId nullable: true, maxSize: 512
	}
	
	static mapping = {
		id generator: 'assigned'
    }
	
	URL apiUrl;
	URL htmlUrl;
	URL avatarUrl;
	String gravatarId;
	String username;
	String name;
}
