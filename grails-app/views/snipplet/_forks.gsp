<div class="forks gist-section-container">
	<g:if test="${!gist.forks}">
		<p class="message">No forks available</p>
	</g:if>
	<g:each var="fork" in="${gist.forks}">
		<div class="fork gist-section-entry">
			<div class="gist-section-info">
				<img src="${fork.fork.user.avatarUrl}" title="${fork.fork.user.username}" class="thumbnail" />
				<div class="date">${fork.fork.dateCreated.format('MMM d').toUpperCase()}</div>
			</div>
			<div class="gist-section-content">
				<p class="details">
					<span class="name">${fork.fork.user.username}</span> 
					<span class="date">on ${fork.fork.dateCreated.format('MMMM d, yyyy h:mm:ss aa')}</span>
				</p>
				<p class="subdetails">
					Last updated on ${fork.fork.dateUpdated.format('MMMM d, yyyy h:mm:ss aa')}
				</p>
				<!-- <p class="text"><a href="#">view fork</a></p> -->
			</div>
			<div class="clear"></div>
		</div>
	</g:each>
</div>