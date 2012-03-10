<!doctype html>

<html>
<head>
	<meta name="layout" content="simple" />
	
	<title>Snipplet :: Login</title>
</head>
<body>

	<div class="login-container">
		<div class="login-section">
			<h3>Existing Github Account</h3>
			
			<div class="login-info">
				<p>
					Signing in with your Github account is easy and will
					provide the following features:
				</p>
				
				<ul>
					<li>Automatically import and synchronize your Gists</li>
					<li>Easily fork, share, and save other Gists</li>
					<li>Follow other Github users</li>
					<li>Comment on and colloborate on shared Gists</li> 
				</ul>
				
				<p>
					To sign in, click the login button below to login to
					Github and grant Snipplet access.
				</p>
			</div>
			
			<div class="login-actions">
				<a id="github-login" href="#" class="btn btn-primary">Login</a>
			</div>
		</div>
	
		<div class="login-section">
			<h3>New Github User</h3>
			
			<div class="login-info">
				<p>
					Snipplet requires a Github account to store and synchronize Gists on your behalf.
					To get started with Github, click the button below to create a new Github account.
					Then, return here to login and link your account.
				</p>
				
				<ul>
					<li><a href="https://github.com">Introduction to Github</a></li>
					<li><a href="https://gist.github.com/">Introduction to Gists</a></li>
				</ul>
			</div>
			
			<div class="login-actions">
				<a href="#" class="btn btn-primary">Create Account</a>
			</div>
		</div>
	</div>
	
	<div class="login-status">
	
		<h3>Logging into Github and authorizing...</h3>
		<div class="progress progress-info progress-striped active">
			<div class="bar"></div>
		</div>
		
		<div class="login-notes">
			<p>
				<strong>Note:</strong> If this is your first time logging in, you will need to login to 
				Github with the newly opened window and authorize Snipplet before continuing.  Once you 
				authorize Snipplet, this window will automatically refresh and allow you to finish creating 
				an account.
			</p>
		</div>
	</div>
	
	<div class="login-result">
		<div class="thumbnail"><img src="" /></div>
		<h3>Welcome <span class="name"></span></h3>
		<p>
			This appears to be your first time creating an account. Would you like to finish creating
			this account and automatically synchronize your existing <span class="gists-count"></span>? 
		</p>
		<br class="clear" />
		
		<div class="login-progress">
			<div class="progress progress-info progress-striped active">
				<div class="bar" style="width: 0%;"></div>
			</div>
			<p>Synchronizing <span class="gists-status">1</span> of <span class="gists-count"></span>...</p>
		</div>

		<div class="login-actions">
			<g:formRemote name="login" url="[controller:'login', action:'login']" onSuccess="self.parent.location.href = data">
				<input type="submit" data-login-uri="${createLink(controller:'login', action:'login')}" data-status-uri="${createLink(controller:'login', action:'status')}" class="finish btn btn-primary pull-left" value="Finish" />
				<a href="#" class="btn pull-right" data-dismiss="modal">Cancel</a>
			</g:formRemote>
		</div>
	</div>

</body>
</html>