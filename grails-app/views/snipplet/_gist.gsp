<div class="gist gist-2" id="gist-${gist.id}" data-gist="${gist.id}">
	<div class="header">
		<div class="language">${gist.language?.name ?: 'General'}</div>
		<div class="logo">
			<img src="${gist.user.avatarUrl}" title="${gist.user.username}" class="thumbnail" />
		</div>
		<div class="info">
			<h3>${gist.description ?: 'Untitled'}</h3>
			<p>
				Submitted by ${gist.user.username}  
				<span title="Updated ${gist.dateUpdated.format('MMMM d, yyyy h:mm:ss aa')}, Created ${gist.dateCreated.format('MMMM d, yyyy h:mm:ss aa')}">
					${gist.dateUpdated.relativeTime}
				</span>
			</p>
		</div>
		<div class="clear"></div>
	</div>

	<div class="expanded">
		<div class="options">
			<ul class="gist-nav nav-pills">
				<li class="active">
					<a href="#code-${gist.id}" data-toggle="tab">
						Code
					</a>
				</li>
				<li>
					<a href="#comments-${gist.id}" data-toggle="tab">
						<g:if test="${gist.numberOfComments > 0}">
							<span class="badge">${gist.numberOfComments}</span>
						</g:if>
						Comments
					</a>
				</li>
				<li>
					<a href="#history-${gist.id}" data-toggle="tab">
						<g:if test="${gist.history.size() > 1}">
							<span class="badge">${gist.history.size()}</span>
						</g:if>
						History
					</a>
				</li>
				<li>
					<a href="#forks-${gist.id}" data-toggle="tab">
						<g:if test="${gist.forks.size() > 0}">
							<span class="badge">${gist.forks.size()}</span>
						</g:if>
						Forks
					</a>
				</li>
			</ul>
			<g:if test="${user}">
				<ul class="gist-subnav">
					<li><a href="#" class="snip-it" rel="tooltip" title="Snip this Gist"><i class="icon-bookmark"></i></a></li>
					<!-- <li><a href="#" rel="tooltip" title="Fork tihs Gist"><i class="icon-fork"></i></a></li> -->
					<li><a href="#" rel="tooltip" title="Mark as Favorite"><i class="icon-star-empty"></i></a></li>
					<li><a href="#" rel="tooltip" title="Share tihs Gist"><i class="icon-share"></i></a></li>
				</ul>
			</g:if>
			<div class="clear"></div>
		</div>
		<div class="gist-container">
			<div class="tab-content">
				<div class="tab-pane active" id="code-${gist.id}">
					<g:render template="/snipplet/code" model="[gist:gist, user:user]" />
				</div>
				<div class="tab-pane" id="comments-${gist.id}">
					<g:render template="/snipplet/comments" model="[gist:gist, user:user]" />
				</div>
				<div class="tab-pane" id="history-${gist.id}">
					<g:render template="/snipplet/history" model="[gist:gist, user:user]" />
				</div>
				<div class="tab-pane" id="forks-${gist.id}">
					<g:render template="/snipplet/forks" model="[gist:gist, user:user]" />
				</div>
			</div>
		</div>
	</div>
</div>
