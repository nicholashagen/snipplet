package com.znet.snipplet

class LanguageService {

	def dateService;
	
    Language getDefaultLanguage() {
		return Language.findByAbbreviation('general');
    }
	
	Language lookupLanguage(def type) {
		def typeLower = type.toLowerCase();
		def language = Language.findByAbbreviation(typeLower);
		if (!language) {
			language = new Language(name:type, abbreviation:typeLower)
			language.save()
		}
		
		return language;
	}
	
	def getLanguages() {
		return Language.list(sort:'numberOfGists', order:'desc');
	}
	
	void countLanguages() {
		def languages = Language.list()
		languages.each { language ->
			def criteria = Gist.createCriteria()
			def count = criteria.get {
			    projections {
			        countDistinct('id')
			    }
			    history {
			        files {
			            eq('language', language)
			        }
			    }
			}
			
			language.numberOfGists = count;
			language.dateUpdated = dateService.now();
			language.save();
		}
		/*
		def criteria = Gist.createCriteria()
		def results = criteria.list {
			projections {
				count 'id'
				groupProperty 'language'
			}
		}
		
		results.each { result ->
			def count = result[0];
			Language language = result[1];
			language.numberOfGists = count;
			language.abbreviation = language.abbreviation.toLowerCase();
			language.dateUpdated = dateService.now();
			language.save();
		}
		*/
	}
}
