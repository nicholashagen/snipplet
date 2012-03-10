<html>
<head>
	<meta name="layout" content="main"/>

	<title>Snipplet</title>
</head>
<body>

	<div class="body">
		${gist.description}<br /><br />
		<br /><br /><hr /><br /><br />
		<g:each var="file" in="${files}">
			${file.name} - ${file.filename}<br />
			<br />
			<pre>
			${file.content}
			</pre>
		</g:each>
	</div>

</body>
</html>
