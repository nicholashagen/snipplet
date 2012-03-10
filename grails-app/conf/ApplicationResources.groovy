modules = {
	
	application {
		defaultBundle 'application-ui'
		
		dependsOn 'jquery, jquery-ui, bootstrap'
		
		resource url:'/js/application.js'
		resource url:'/css/application.css'
	}
	
	snipplet {
		defaultBundle 'application-ui'
		
		dependsOn 'application'
		
		resource url:'/assets/prettify/prettify.js'
		resource url:'/assets/prettify/prettify.css'
		
		resource url:'/js/snipplet.js'
		resource url:'/css/snipplet.css'
	}

	user {
		defaultBundle 'application-ui'
		
		dependsOn 'application'
		
		resource url:'/css/user.css'
	}
	
	overrides {
		'jquery' {
			defaultBundle 'application-ui'
		}
		
		'jquery-ui' {
			defaultBundle 'application-ui'
		}

		'jquery-ui-theme' {
			defaultBundle 'application-ui'
		}
		
		'bootstrap' {
			defaultBundle 'application-ui'
		}
	}
}