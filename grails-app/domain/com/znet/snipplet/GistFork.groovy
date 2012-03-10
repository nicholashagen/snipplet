package com.znet.snipplet

class GistFork implements Comparable {

	static belongsTo = [ gist : Gist ]
	
	static constraints = { 
		dateCreated nullable: true
	}
	
	static mapping = {
		autoTimestamp false
	}
	
	Gist fork;
	Date dateCreated;
	
	int compareTo(Object other) {
		return dateCreated <=> dateCreated;
	}
}
