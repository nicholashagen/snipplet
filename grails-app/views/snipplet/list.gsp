<html>
<head>
	<meta name="layout" content="main" />

	<title>Snipplet</title>
	
	<r:require modules="snipplet" />
</head>
<body>

	<g:render template="/snipplet/gists" model="[ gists:gists, user:user ]" />

</body>
</html>
