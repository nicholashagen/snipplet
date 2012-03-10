package com.znet.snipplet

class Category implements Comparable {

    static constraints = { 
		
	}
	
	User user;
	String name;
	
	int compareTo(obj) {
		name <=> obj.name;
	}
}
