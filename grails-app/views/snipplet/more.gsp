<g:each var="gist" in="${gists}" status="status">
	<div class="gist gist-2" id="gist-${gist.id}">
		<div class="language">${gist.language?.name ?: 'General'}</div>
		
		<div class="header">
			<div class="logo">
				<a href="#"><img src="${gist.user.avatarUrl}" title="${gist.user.username}" class="thumbnail" /></a>
			</div>
			<div class="info">
				<h3>${gist.description ?: 'Untitled'}</h3>
				<p>
					Submitted by ${gist.user.username}  
					<span title="Updated ${gist.dateUpdated.format('MMMM d, yyyy h:mm:ss aa')}, Created ${gist.dateCreated.format('MMMM d, yyyy h:mm:ss aa')}">
						${gist.dateUpdated.relativeTime}
					</span>
					(<a href="${gist.apiUrl}" style="color: white;" target="_new">API</a>)
				</p>
				<!-- <p>Gist: ${gist.pullUrl}</p> -->
			</div>
			<div class="clear"></div>
		</div>

		<div class="expanded">
			<div class="options">
				<ul class="gist-nav nav-${status == 2 ? 'pills' : 'tabs'}">
					<li class="active"><a href="#code-${gist.id}" data-toggle="tab">
						Code
					</a></li>
					<li><a href="#comments-${gist.id}" data-toggle="tab">
						<g:if test="${gist.numberOfComments > 0}">
							<span class="badge">${gist.numberOfComments}</span>
						</g:if>
						Comments
					</a></li>
					<li><a href="#history-${gist.id}" data-toggle="tab">
						<g:if test="${gist.history.size() > 1}">
							<span class="badge">${gist.history.size()}</span>
						</g:if>
						History
					</a></li>
					<li><a href="#forks-${gist.id}" data-toggle="tab">
						<g:if test="${gist.forks.size() > 0}">
							<span class="badge">${gist.forks.size()}</span>
						</g:if>
						Forks
					</a></li>
				</ul>
				<g:if test="${user}">
					<ul class="gist-subnav">
						<li><a href="#">Snip</a></li>
						<li><a href="#">Favorite</a></li>
						<li><a href="#">Share</a></li>
					</ul>
				</g:if>
				<div class="clear"></div>
			</div>
			<div class="gist-container">
				<div class="tab-content">
					<div class="tab-pane active" id="code-${gist.id}">
						<div class="code">
							<g:if test="${gist.files}">
								<g:set var="file" value="${gist.files.iterator().next()}" />
								<g:set var="language" value="${file.language?.abbreviation}" />
								<g:set var="history" value="${gist.history.iterator().next()}" />

								<div class="selector" data-file="${file.filename}" data-history="${history.id}">
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
																<g:link action="viewCode" data-file="${file.filename}" data-gist="${gist.id}" title="${file.filename}" class="select-file">${file.name}</g:link>
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
																<g:link action="viewCode" data-history="${history.versioning}" data-gist="${gist.id}" title="${history.versioning} on ${history.dateCommitted.format('MMM d, yyyy h:mm:ss aa')}" class="select-history">
																	${history.versioning.substring(0, 8)} <em>${history.dateCommitted.relativeTime} by ${history.user?.username ?: 'N/A'}</em>
																</g:link>
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
					</div>
					<div class="tab-pane" id="comments-${gist.id}">
						<div class="comments gist-overflow">
							<g:if test="${!gist.comments}">
								<p class="message">No comments available</p>
							</g:if>
							<g:each var="comment" in="${gist.comments}">
								<div class="comment">
									<div class="comment-info">
										<img src="${comment.user.avatarUrl}" title="${comment.user.username}" class="thumbnail" />
										<div class="date">${comment.dateCreated.format('MMM d').toUpperCase()}</div>
									</div>
									<div class="comment-content">
										<p class="comment-details">
											<span class="name">${comment.user.username}</span> <span class="date">on ${comment.dateCreated.format('MMMM d, yyyy h:mm:ss aa')}</span>
										</p>
										<p class="comment-text">${comment.text}</p>
									</div>
									<div class="clear"></div>
								</div>
							</g:each>
						</div>
						<g:if test="${session.user}">
							<div class="add-comment-form">
								<g:form action="addComment">
									<g:textField name="comment" />
									<g:submitToRemote update="comments-${gist.id}" value="Add" />
								</g:form>
							</div>
						</g:if>
					</div>
					<div class="tab-pane" id="history-${gist.id}">
						<div class="histories gist-overflow">
							<g:if test="${!gist.history}">
								<p class="message">No history available</p>
							</g:if>
							<g:each var="history" in="${gist.history}">
								<div class="history">
									<div class="history-info">
										<img src="${history.user.avatarUrl}" title="${history.user.username}" class="thumbnail" />
										<div class="date">${history.dateCommitted.format('MMM d').toUpperCase()}</div>
									</div>
									<div class="history-content">
										<p class="history-details">
											<span class="name">${history.user.username}</span> 
											<span class="date">on ${history.dateCommitted.format('MMMM d, yyyy h:mm:ss aa')}</span>
										</p>
										<p class="history-revision">
											<span class="revision" title="${history.versioning}">revision ${history.versioning.substring(0, 8)}</span>
											<span class="updates">
												(<g:if test="${history.additions}"><span class="additions">+${history.additions}</span> additions, </g:if>
												<g:if test="${history.deletions}"><span class="deletions">-${history.deletions}</span> deletions, </g:if>
													${history.totalUpdates} total updates)
											</span>
										</p>
										<p class="history-text"><a href="#">show revision</a></p>
									</div>
									<div class="clear"></div>
								</div>
							</g:each>
						</div>
					</div>
					<div class="tab-pane" id="forks-${gist.id}">
						<div class="forks gist-overflow">
							<g:if test="${!gist.forks}">
								<p class="message">No forks available</p>
							</g:if>
							<g:each var="fork" in="${gist.forks}">
								<div class="fork">
									<div class="fork-info">
										<img src="${fork.fork.user.avatarUrl}" title="${fork.fork.user.username}" class="thumbnail" />
										<div class="date">${fork.fork.dateCreated.format('MMM d').toUpperCase()}</div>
									</div>
									<div class="fork-content">
										<p class="fork-details">
											<span class="name">${fork.fork.user.username}</span> 
											<span class="date">on ${fork.fork.dateCreated.format('MMMM d, yyyy h:mm:ss aa')}</span>
										</p>
										<p class="fork-data">
											Last updated on ${fork.fork.dateUpdated.format('MMMM d, yyyy h:mm:ss aa')}
										</p>
										<p class="fork-text"><a href="#">view fork</a></p>
									</div>
									<div class="clear"></div>
								</div>
							</g:each>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</g:each>
