package com.znet.snipplet

class GistComment implements Comparable {

	static belongsTo = [ gist : Gist ]
	
    static constraints = { 
		apiUrl nullable: true, maxSize: 512
		text nullable: true
		dateCreated nullable: true
	}
	
	static mapping = {
		text type: 'text'
		autoTimestamp false
	}
	
	URL apiUrl;
	String text;
	GithubUser user;
	Date dateCreated;
	
	int compareTo(Object other) {
		return other.dateCreated <=> dateCreated;
	}
}
