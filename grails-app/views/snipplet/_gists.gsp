<g:render template="/snipplet/snipit" />

<div class="clear-content-margin">
	<div id="loader-top" class="loading-container loading-container-top" style="display: block;">
		<div class="frame">
			<a class="close">&times;</a>
			<h3>Loading snipplets...</h3>
			<div class="loading-content">
				<div class="progress progress-info progress-striped active">
		  			<div class="bar" style="width: 100%;"></div>
				</div>
			</div>
		</div>
	</div>
</div>

<div class="gists row-fluid" data-more-uri="${createLink(action: 'more')}" data-code-uri="${createLink(controller:'snipplet', action : 'code')}">
	<div class="clear"></div>
</div>

<div class="gists-loader">
	<g:each var="gist" in="${gists}" status="status">
		<g:render template="/snipplet/gist" model="[gist:gist, user:user]" />
	</g:each>
</div>

<p class="refresh">
	<a href="#">Load more snippets...</a>
</p>

<div id="status" class="status-container" style="display: none;">
	<div class="overlay"></div>
	<div class="frame">
		<a class="close">&times;</a>
		<h3>Loading...</h3>
		<div class="status-content">
			<p class="error">
				<span class="label label-important">Error</span> 
				<span class="error-message">Unable to retrieve content.</span>
				Please close this dialog and try again.
			</p>
			<div class="progress progress-info progress-striped">
	  			<div class="bar" style="width: 100%;"></div>
			</div>
		</div>
	</div>
</div>

<div id="loader-bottom" class="loading-container loading-container-bottom" style="display: block;">
	<div class="frame">
		<a class="close">&times;</a>
		<h3>Loading more content...</h3>
		<div class="loading-content">
			<p class="error">
				<span class="label label-important">Error</span> 
				<span class="error-message">Unable to retrieve content.</span>
				Please close this dialog and try again.
			</p>
			<div class="progress progress-info progress-striped active">
	  			<div class="bar" style="width: 100%;"></div>
			</div>
		</div>
	</div>
</div>

