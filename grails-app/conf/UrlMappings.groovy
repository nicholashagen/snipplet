class UrlMappings {

	static mappings = {
		"/snipplets/$language?"(controller:'snipplet', action:'list')
		
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
		
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
