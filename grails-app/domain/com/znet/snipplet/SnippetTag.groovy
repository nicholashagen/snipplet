package com.znet.snipplet

class SnippetTag implements Comparable {

    static constraints = { }
	
	static belongsTo = [ snippet:Snippet ]
	
	String tag;
	
	int compareTo(obj) {
		tag <=> obj.tag;
	}
}
