<div class="code">
	<g:if test="${gist.files}">
		<g:set var="file" value="${gist.files.iterator().next()}" />
		<g:set var="language" value="${file.language?.abbreviation}" />
		<g:set var="history" value="${gist.history.iterator().next()}" />

		<div class="selector" data-file="${file.filename}" data-history="${history.versioning}">
			<div class="selector-content">
				<div class="selector-files selector-section">
					<div class="label">File </div>
					<div class="btn-group">
						<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
							<span id="gist-file-${gist.id}" title="${file.filename}">${file.name}</span>
							<g:if test="${gist.files.size() > 1}">
								<span class="caret"></span>
							</g:if>
						</a>
						<g:if test="${gist.files.size() > 1}">
							<ul class="dropdown-menu">
								<g:each var="file" in="${gist.files}" status="idx">
									<li class="${idx == 0 ? 'active' : ''}">
										<a href="#" data-file="${file.filename}" data-gist="${gist.id}" title="${file.filename}" class="select-file">${file.name}</a>
									</li>
								</g:each>
							</ul>
						</g:if>
					</div>
				</div>
				<div class="selector-history selector-section">
					<div class="label">Revision </div>
					<div class="btn-group">
						<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
							<span id="gist-history-${gist.id}" title="${history.versioning} on ${history.dateCommitted.format('MMM d, yyyy h:mm:ss aa')}">${history.versioning.substring(0, 8)} <em>${history.dateCommitted.relativeTime} by ${history.user?.username ?: 'N/A'}</em></span>
							<g:if test="${gist.history.size() > 1}">
								<span class="caret"></span>
							</g:if>
						</a>
						<g:if test="${gist.history.size() > 1}">
							<ul class="dropdown-menu">
								<g:each var="history" in="${gist.history}" status="idx">
									<li class="${idx == 0 ? 'active' : ''}">
										<a href="#" onclick="return false;" data-history="${history.versioning}" data-gist="${gist.id}" title="${history.versioning} on ${history.dateCommitted.format('MMM d, yyyy h:mm:ss aa')}" class="select-history">
											${history.versioning.substring(0, 8)} <em>${history.dateCommitted.relativeTime} by ${history.user?.username ?: 'N/A'}</em>
										</a>
									</li>
								</g:each>
							</ul>
						</g:if>
					</div>
				</div>
				<div class="clear"></div>
			</div>
		</div>
		<pre id="gist-${gist.id}-code" class="prettyprint gist-overflow linenums">${file.content.encodeAsHTML()}</pre>
	</g:if>
</div>
