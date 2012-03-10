<!doctype html>

<html class="main" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9" />
		
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<title><g:layoutTitle default="Snipplet"/></title>
		
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon" />
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}" />
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}" />

		<r:require modules="application" />		

		<g:layoutHead/>
        <r:layoutResources />
	</head>
	<body class="main">

		<!--[if lt IE 7 ]><div class="ie ie6"><![endif]-->
		<!--[if IE 7 ]><div class="ie ie7"><![endif]-->
		<!--[if IE 8 ]><div class="ie ie8"><![endif]-->
	
		<div class="navbar navbar-fixed-top">
	      	<div class="navbar-inner">
	        	<div class="container-fluid">
	        		<g:link controller="snipplet" action="list" class="brand">Snipplet</g:link>
	          		<ul class="nav main">
	          			<g:if test="${session.user}">
	            			<li class="active">
	            				<g:link controller="user" action="snipplets">My Snipplets</g:link>
	            			</li>
	            			<!-- <li><a href="#">My Friends</a></li> -->
	            			<li class="divider-vertical"></li>
	            		</g:if>
	            		<g:each var="language" in="${languages}">
	            			<li class="${params.language == language.abbreviation ? 'active' : ''}">
	            				<g:link controller="snipplet" action="list" params="[language:language.abbreviation]" title="${language.name}: over ${language.numberOfGists}+ gists">
	            					${language.name}
	            					<div class="selected"></div>
	            				</g:link>
	            			</li>
	            		</g:each>
	            	</ul>
	            	<ul class="nav extra">
	            		<li class="divider-vertical"></li>
	            		<li class="dropdown">
	            			<a href="#" class="dropdown-toggle" data-toggle="dropdown">
	            				More
	            				<b class="caret"></b>
	            			</a>
	            			<ul class="dropdown-menu">
	            			</ul>
	            		</li>
	            		<li class="divider-vertical"></li>
	          		</ul>        		
	          		<div class="pull-right">
	          			<div class="search">
		          			<form class="navbar-search pull-left">
							  	<input type="text" class="search-query" placeholder="Search">
							</form>
	          			</div>
	          			<div class="login-details">
			            	<ul class="nav login">
				            	<g:if test="${session.user}">
				            		<li class="dropdown">
					          			<a href="#" class="dropdown-toggle" data-toggle="dropdown">
						          			<g:if test="${session.user.githubAccount.avatarUrl}">
						          				<img src="${session.user.githubAccount.avatarUrl}" />
						          			</g:if>
						          			${session.user.name}
						          			<b class="caret"></b>
						          		</a>
						          		<ul class="dropdown-menu">
											<li><g:link controller="login" action="settings">Profile</g:link></li>
											<li><a target="_new" href="${session.user.githubAccount.htmlUrl}">Github Account</a></li>
											<li class="divider"></li>
											<li><g:link controller="login" action="logout">Logout</g:link></li>
										</ul>
									</li>
			          			</g:if>
			          			<g:else>
			          				<li>
			          					<a href="#" class="login">
			          						<img src="http://github.com/favicon.ico" />
			          						Login with Github
			          					</a>	
			          				</li>
			          			</g:else>
			          		</ul>
			          	</div>
		          	</div>
	        	</div>
	      	</div>
	    </div>
	
	 	<div class="container-fluid">
	      	<div class="content">
	        	<div class="row-fluid">
	          		<div class="span12">
	          			<div style="min-height: 500px;">
	            			<g:layoutBody />
	            		</div>
	            	</div>
	          	</div>
	        </div>
	
	    	<footer>
	        	<p>Copyright &copy; <a href="http://www.znetdevelopment.com">Z|NET Development, LLC</a> 2012</p>
	    	</footer>
	    </div>

		<g:if test="${!session.user}">
			<g:render template="/login/frame" />
		</g:if>
	
		<!--[if lt IE 9 ]></div><![endif]-->
	
		<r:layoutResources />

	</body>
</html>
