<html>
<head>
	<meta name="layout" content="main"/>

	<title>Snipplets</title>
	
	<r:require modules="user" />
</head>
<body>

	<div class="row-fluid">
		<div class="lhn well span3">

			<div class="profile">
				<div class="thumbnail">
					<img src="${session.user.githubAccount.avatarUrl}" />
				</div>
				<div class="details">
					<h4 title="${session.user.username}">${session.user.name}</h4>
					<div class="btn-group">
						<a class="btn action" href="#">Follow</a>
						<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
							<span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li><a href="#">Profile</a></li>
							<li><a href="#">Unfollow</a></li>
						</ul>
						<br class="clear" />
					</div>
				</div>
			</div>
			
			<div class="divider"></div>
	
			<div class="search row-fluid">
				<form class="form-search">
					<div class="controls">
             			<div class="input-append">
             				<div style="margin-left: 3%; width: 85%;">
	               				<input size="64" type="text" placeholder="Search Snipplets" />
	               			</div>
	               			<div style="margin-right: 3%; width: 9%;">
		               			<span class="add-on">
		               				<i class="icon-search"></i>
		               			</span>
	               			</div>
	               			<br class="clear" />
             			</div>
	           		</div>
				</form>
			</div>
			
			<div class="divider"></div>
	
			<ul class="nav nav-profile nav-list">
				<li class="active"><a href="#">All Snipplets</a></li>
				<li><a href="#">Favorites</a></li>
				<li><a href="#">Uncategorized</a></li>
				<li><a href="#">Recently Viewed</a></li>
				
				<li class="nav-header">
					<a href="#" data-toggle="collapse" data-target="#user-nav-category">Categories</a>
					<ul id="user-nav-category" class="collapse in nav nav-list">
						<li><a href="#">Design Patterns</a></li>
						<li><a href="#">Factories</a></li>
					</ul>
				</li>
				
				<li class="nav-header">
					<a href="#" data-toggle="collapse" data-target="#user-nav-language">Languages</a>
					<ul id="user-nav-language" class="collapse in nav nav-list">
						<li><a href="#">Java</a></li>
						<li><a href="#">Javascript</a></li>
					</ul>
				</li>
				
				<li class="nav-header">
					<a href="#" data-toggle="collapse" data-target="#user-nav-tag">Tags</a>
					<ul id="user-nav-tag" class="collapse nav nav-list">
						<li><a href="#">Tag1</a></li>
						<li><a href="#">Tag2</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
	
	<div class="row-fluid">
		<div class="span3">&nbsp;</div>

		<div class="span9">

			<g:render template="/snipplet/gists" model="[ gists:gists, user:user ]" />

		</div>
	</div>
	
</body>
</html>
