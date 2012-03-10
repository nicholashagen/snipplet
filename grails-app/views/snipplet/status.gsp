<html>
<head>
	<meta name="layout" content="main"/>

	<title>Snipplet</title>
</head>
<body>

	<table class="table table-striped">
		<thead>
			<tr>
				<th colspan="2">Status</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th width="250px">Gists</th>
				<td width="300px">${count}</td>
				<td>
					<g:remoteLink class="btn" action="reduce" update="reduce">Reduce</g:remoteLink>
					<span id="reduce" class="label label-success"></span>
				</td>
			</tr>
			<tr>
				<th width="250px">Last Page</th>
				<td colspan="2">${status.lastPage}</td>
			</tr>
			<tr>
				<th width="250px">Last Updated</th>
				<td colspan="2" title="${status.lastUpdated.format('MMM d, yyyy h:mm:ss')}">${status.lastUpdated.relativeTime}</td>
			</tr>
			<tr>
				<th width="250px">API Calls</th>
				<td colspan="2">
					<g:formatNumber number="${status.apiCalls}" format="#,###" /> 
					(average of <g:formatNumber number="${status.apiCalls * 1.0 / status.lastPage}" format="#,###.##" /> per page)
				</td>
			</tr>
			<tr>
				<th width="250px">Elapsed Time</th>
				<td colspan="2">
					<g:formatNumber number="${status.elapsedTime}" format="#,###" /> ms 
					(average of <g:formatNumber number="${status.elapsedTime * 1.0 / status.lastPage}" format="#,###.##" /> ms per page)
				</td>
			</tr>
		</tbody>
	</table>

	<table class="table table-striped">
		<thead>
			<tr>
				<th colspan="2">Job Scheduler</th>
			</tr>
		</thead>
		<tbody>
			<g:each var="job" in="${jobs}">
			<tr>
				<td width="250px">${job.name}</td>
				<td width="300px" title="Job status is ${job.status}">
					${job.status == 0 ? 'Not Running' : job.status == 1 ? 'Paused' : job.status == 4 ? 'Running' : job.status}
				</td>
				<td>
					<g:if test="${job.status != 1}">
						<g:remoteLink class="btn" action="invoke" params="[operation:'pause', job:job.name, group:job.group]" update="invoke-${job.name.replace('.', '-')}">Pause</g:remoteLink>
					</g:if>
					<g:if test="${job.status == 1}">
						<g:remoteLink class="btn" action="invoke" params="[operation:'resume', job:job.name, group:job.group]" update="invoke-${job.name.replace('.', '-')}">Resume</g:remoteLink>
					</g:if>
					<g:remoteLink class="btn" action="invoke" params="[operation:'invoke', job:job.name, group:job.group]" update="invoke-${job.name.replace('.', '-')}">Invoke</g:remoteLink>
					<span id="invoke-${job.name.replace('.', '-')}" class="label label-success"></span>
				</td>
			</tr>
			</g:each>
		</tbody>
	</table>
		
	<table class="table table-striped">
		<thead>
			<tr>
				<th colspan="2">Label</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th width="250px">Free Memory</th>
				<td colspan="2">
					<g:formatNumber number="${Runtime.getRuntime().freeMemory()}" format="#,###" /> bytes
				</td>
			</tr>
			<tr>
				<th width="250px">Total Memory</th>
				<td colspan="2">
					<g:formatNumber number="${Runtime.getRuntime().totalMemory()}" format="#,###" /> bytes
				</td>
			</tr>
			<tr>
				<th width="250px">Used Memory</th>
				<td colspan="2">
					<g:formatNumber number="${Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()}" format="#,###" /> bytes
				</td>
			</tr>
		</tbody>
	</table>
	
	<table class="table table-striped">
		<thead>
			<tr>
				<th>Database Console</th>
			</tr>
		</thead>
	</table>
	
	<div class="form-actions">
		<cf:dbconsoleLink class="btn">Database Console</cf:dbconsoleLink>
	</div>

</body>
</html>
