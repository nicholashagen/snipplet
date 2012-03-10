package com.znet.snipplet

class Status {

    static constraints = { }
	
	static mapping = {
		version false
		autoTimestamp false
		id generator: 'assigned'
	}
	
	int id;
	int lastPage;
	Date lastUpdated;
	long apiCalls;
	long elapsedTime;
}
