package com.znet.snipplet

class Gist {

	static hasMany = [
		forks : GistFork,
		comments : GistComment,
		history : GistHistory
	]
	
    static constraints = { 
		name nullable: true
		description nullable: true, maxSize: 512
		apiUrl nullable: true, maxSize: 512
		htmlUrl nullable: true, maxSize: 512
		pushUrl nullable: true, maxSize: 512
		pullUrl nullable: true, maxSize: 512
		dateCreated nullable: true
		dateUpdated nullable: true
		dateDiscovered nullable: true
		numberOfComments nullable: true
		user nullable: true
		language nullable: true
	}
	
	static mapping = {
		id generator: 'assigned'
		autoTimestamp false
	}
	
	String id;
	String name;
	String description;
	boolean valid;
	boolean isPublic;
	boolean locked;
	URL apiUrl;
	URL htmlUrl;
	String pushUrl;
	String pullUrl;
	Date dateCreated;
	Date dateUpdated;
	Date dateDiscovered;
	Integer numberOfComments;
	GithubUser user;
	Language language;
	SortedSet history;
	SortedSet comments;
	SortedSet forks;
	
	def getFiles() {
		if (!history) { return null; }
		return history?.iterator()?.next()?.files;
	}
	
	boolean equals(def other) {
		return this.id == other.id
	}
	
	int hashCode() {
		return 17 * this.id.hashCode()
	}
}
