<div class="comments gist-section-container">
	<g:if test="${!gist.comments}">
		<p class="message">No comments available</p>
	</g:if>
	<g:each var="comment" in="${gist.comments}">
		<div class="comment gist-section-entry">
			<div class="gist-section-info">
				<img src="${comment.user.avatarUrl}" title="${comment.user.username}" class="thumbnail" />
				<div class="date">${comment.dateCreated.format('MMM d').toUpperCase()}</div>
			</div>
			<div class="gist-section-content">
				<p class="details">
					<span class="name">${comment.user.username}</span> <span class="date">on ${comment.dateCreated.format('MMMM d, yyyy h:mm:ss aa')}</span>
				</p>
				<p class="text">${comment.text}</p>
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
