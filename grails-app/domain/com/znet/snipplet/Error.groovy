package com.znet.snipplet

class Error {

	static constraints = {
		source nullable: true, maxSize: 64
		message nullable: true, maxSize: 512
		stacktrace nullable: true
	}
	
	static mapping = {
		stacktrace type: 'text'
	}
	
	String type;
	String source;
	String message;
	String stacktrace;
}
