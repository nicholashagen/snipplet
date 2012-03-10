<!doctype html>

<html class="simple" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9" />
		
		<title><g:layoutTitle default="Snipplet"/></title>
		
		<link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon" />
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}" />
		<link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}" />

		<r:require modules="application" />		

		<g:layoutHead/>
        <r:layoutResources />
	</head>
	<body class="simple">

		<!--[if lt IE 7 ]><div class="ie ie6"><![endif]-->
		<!--[if IE 7 ]><div class="ie ie7"><![endif]-->
		<!--[if IE 8 ]><div class="ie ie8"><![endif]-->

		<g:layoutBody />
	
		<!--[if lt IE 9 ]></div><![endif]-->
	
		<r:layoutResources />

	</body>
</html>
