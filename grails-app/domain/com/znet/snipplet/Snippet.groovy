package com.znet.snipplet

class Snippet {

    static constraints = { 
		title nullable: true, maxSize: 512
		description nullable: true, maxSize: 512
		language nullable: true
		category nullable: true
		dateCreated nullable: true
		dateModified nullable: true
		dateLastViewed nullable: true
	}
	
	static hasMany = [ tags : SnippetTag ]
	
	static mapping = {
		autoTimestamp false
	}
	
	User user;
	Gist gist;
	String title;
	String description;
	Language language;
	Category category;
	Date dateCreated;
	Date dateModified;
	Date dateLastViewed;
}
