(function($) {

	/**
	 * Handle tooltips
	 */
	$('.gist').tooltip({
		selector: "a[rel=tooltip]"
	});
	    
	/**
	 * Handle logins
	 */
	$('a.login').click(function() {
		var $content = $('#login-frame');
		$content.modal();

		// TODO: reset display states in case previous cancel
		
		event.preventDefault();
		return false;
	});
	
	$('a#github-login').click(function() {
		// calculate center of screen
		var left = 0, top = 0, 
		    fheight = 475, fwidth = 960,
		    wheight = window.outerHeight, wwidth = window.outerWidth;
	    
		if (fheight > wheight || fwidth > wwidth) {
			left = ((screen.width - fwidth) / 2);
			top = ((screen.height - fheight) / 2);
		}
		else {
			left = window.screenLeft + ((wwidth - fwidth) / 2);
			top = window.screenTop + ((wheight - fheight) / 2);
		}
	
		// open github auth window
		var $content = $('#login-frame', parent.document),
	        options = 'width=' + fwidth + ',height=' + fheight + ',top=' + top + ',left=' + left;
		
		// show progress
		var github = window.open($content.data('uri'), 'github', options);
		$('.login-container').fadeOut('fast', function() {
			$('.login-status').fadeIn('fast', function() {
				// github = window.open($content.data('uri'), 'github', options)
			});
		});
		
		window.finishLogin = function(user, existing) {
			
			// close login window
			github.close();
			window.finishLogin = null;
			
			// if already existing, just login
			if (existing) {
				$content.modal("hide");
				parent.location.reload(true);
			} 

			// otherwise, verify finish
			else {
				var gists = user.public_gists;
				$('.login-result span.name').html(user.name).attr('title', user.login);
				$('.login-result img').attr('src', user.avatar_url);
				$('.login-result span.gists-count').html(gists + ' gist' + (gists != 1 ? 's' : ''));
				
				$('.login-result .login-actions input.finish').click(function() {
					$('.login-result .login-progress').css('visibility', 'visible');
					$('.login-result .login-actions input.finish').addClass('disabled').html('Synchronizing...');
					
					var loginUrl = $('.login-result .login-actions input.finish').data('login-uri'),
					    statusUrl = $('.login-result .login-actions input.finish').data('status-uri');
					
					console.log('LOGIN: ', loginUrl, statusUrl);
					var checkStatus = function(json) {
						if (json.count > 0) {
							var html = json.status + 1
							if (html > json.count) { html = json.count; }
							
							var pct = json.status / json.count * 100.0;
							$('.login-result span.gists-status').html(html + '');
							$('.login-result .login-progress .progress .bar').css('width', pct + '%');
						}
						
						$.get(statusUrl, {status:json.status}, function(json) {
							checkStatus(json);
						});
					};

					// TODO: handle errors
					
					checkStatus({ status:0, count:gists });

					//event.preventDefault();
					//return false;
				});
				
				$('.login-status').fadeOut('fast', function() {
					$('.login-result').fadeIn('fast');
				});
			}
		};

		event.preventDefault();
		return false;
	});
	
	/**
	 * setup initialization of navigation
	 */
	var initNavigation = function() {
		var $navbar = $('div.navbar');
		var $nav = $('ul.nav.main');
		var $dropdown = $('ul.nav.extra li.dropdown ul.dropdown-menu');
		$nav.children('li').each(function() {
			$dropdown.append($(this).clone(false).css('display', 'none'));
		});
	};

	/**
	 * handle resizing of navigation
	 */
	var resizeNavigation = function() {
		var $navbar = $('div.navbar'), $nav = $('ul.nav.main'),
		    $container = $navbar.find('.container-fluid'),
		    width = $container.width(),
		    lessWidth = $navbar.find('.brand').outerWidth(true) +
		                $navbar.find('.pull-right').outerWidth(true) +
		                $navbar.find('ul.nav.extra').outerWidth(true) +
		                ($nav.outerWidth(true) - $nav.width()),
	        usableWidth = width - lessWidth;
		
		$nav.css('width', usableWidth).show();
		$navli = $nav.children('li');
		$ddli = $('ul.nav.extra li.dropdown ul.dropdown-menu').children('li');
		$ddli.css('display', 'none');
		
		var i = 0;
		for (i = $navli.length - 1; i >= 0; i--) {
			var $li = $($navli[i]);
			var y = $li.position().top;
			if (y > 0) {
				$($ddli[i]).show();
			}
			else { break; }
		}
	};
	
	/**
	 * setup initial states for navigation
	 */
	initNavigation();
	resizeNavigation();
	$(window).resize(function() { resizeNavigation(); });

})(jQuery);
