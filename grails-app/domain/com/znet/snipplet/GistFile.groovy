package com.znet.snipplet

class GistFile implements Comparable {

	static belongsTo = [ 
		history : GistHistory
	]
	
    static constraints = {
		name nullable: true
		mimeType nullable: true
		language nullable: true
		fileSize nullable: true
		filename nullable: true, maxSize: 512
		rawUrl nullable: true, maxSize: 512
		content nullable: true
	}
	
	static mapping = {
		content type: 'text'
	}

	String name;
	String mimeType;
	Language language;
	Integer fileSize;
	String filename;
	URL rawUrl;
	String content;
	
	int compareTo(Object other) {
		return name <=> other.name
	}
}
