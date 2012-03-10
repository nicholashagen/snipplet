<html>
<head>
	<meta name="layout" content="main"/>

	<title>Snipplet</title>
</head>
<body>

	<table cellspacing="3" cellpadding="3" width="100%">
		<tr>
			<th>source</th>
			<th>message</th>
			<th>stacktrace</th>
		</tr>
		<g:each var="error" in="${errors}" status="status">
			<tr>
				<td style="vertical-align: top;"><a href="${error.source}" target="_new">${error.source}</a></td>
				<td style="vertical-align: top;">${error.message ?: 'none'}</td>
				<td style="overflow: auto;b">
					<pre style="height: 200px; background: none !important; border: 0;">${error.stacktrace}</pre>
				</td>
			</tr>
		</g:each>
	</table>
	
</body>
</html>
