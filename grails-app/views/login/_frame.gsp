<div class="modal modal-ext hide fade" id="login-frame" data-uri="${git.createLoginLink()}">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">
			<i class="icon-remove"></i>
		</a>
		<h3>Login with Github</h3>
	</div>
	<div class="modal-body">
		<iframe src="${createLink(controller:'login', action : 'index')}"></iframe>		
	</div>
</div>
