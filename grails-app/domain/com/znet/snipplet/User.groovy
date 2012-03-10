package com.znet.snipplet

class User {

    static constraints = { 
		name nullable: true
		username nullable: false
		dateCreated nullable: true
		dateLastLoggedIn nullable: true
		githubAccount nullable: true
	}
	
	static mapping = {
		autoTimestamp false
	}
	
	String name;
	String username;
	Date dateCreated;
	Date dateLastLoggedIn;
	
	GithubUser githubAccount;
	
	// TODO: need sessions domain object (can have multiple sessions w/ different last locations)
}
