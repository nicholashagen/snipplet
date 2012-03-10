package com.znet.snipplet

class GistHistory implements Comparable {

	static hasMany = [
		files : GistFile
	]

	static belongsTo = [ gist : Gist ]
	
	static constraints = {
		apiUrl nullable: true, maxSize: 512
		user nullable: true
		versioning nullable: true
		deletions nullable: true
		additions nullable:  true
		totalUpdates nullable: true
		dateCommitted nullable: true
	}
	
	URL apiUrl;
	String versioning;
	GithubUser user;
	Integer deletions;
	Integer additions;
	Integer totalUpdates;
	Date dateCommitted;
	SortedSet files;
	
	def getFilesAsMap() {
		return files.asMap('name')
	}
	
	def getFile(String name) {
		return files.find { it.name == name }
	}
	
	int compareTo(Object other) {
		return other.dateCommitted <=> dateCommitted;
	}
}
