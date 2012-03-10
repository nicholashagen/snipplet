<div class="histories gist-section-container">
	<g:if test="${!gist.history}">
		<p class="message">No history available</p>
	</g:if>
	<g:each var="history" in="${gist.history}">
		<div class="history gist-section-entry">
			<div class="gist-section-info">
				<img src="${history.user.avatarUrl}" title="${history.user.username}" class="thumbnail" />
				<div class="date">${history.dateCommitted.format('MMM d').toUpperCase()}</div>
			</div>
			<div class="gist-section-content">
				<p class="details">
					<span class="name">${history.user.username}</span> 
					<span class="date">on ${history.dateCommitted.format('MMMM d, yyyy h:mm:ss aa')}</span>
				</p>
				<p class="subdetails">
					<span class="revision" title="${history.versioning}">revision ${history.versioning.substring(0, 8)}</span>
					<span class="updates">
						(<g:if test="${history.additions}"><span class="additions">+${history.additions}</span> additions, </g:if>
						<g:if test="${history.deletions}"><span class="deletions">-${history.deletions}</span> deletions, </g:if>
							${history.totalUpdates} total updates)
					</span>
				</p>
				<!-- <p class="history-text"><a href="#">show revision</a></p> -->
			</div>
			<div class="clear"></div>
		</div>
	</g:each>
</div>