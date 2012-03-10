package com.znet.snipplet

class Language {

    static constraints = { 
		abbreviation nullable: true
		numberOfGists nullable: true
		dateUpdated nullable: true
	}
	
	static mapping = {
		autoTimestamp false
	}
	
	String name;
	String abbreviation;
	
	Integer numberOfGists;
	Date dateUpdated;
}
