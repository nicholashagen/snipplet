(function($) {

	/**
	 * Setup progress pane
	 */
	var showProgress = function($container, text) {
		var $status = $('#status'),
		    offset = $container.offset(),
		    width = $container.outerWidth() + 1, height = $container.outerHeight();
		
		text = text || 'Loading...';
		
		if ($status.data('visible')) {
			$status.hide();
		}
		
		$status.css({ top: (offset.top + 1) + 'px', left: offset.left + 'px', height: height + 'px', width: width + 'px' });
		
		var $overlay = $status.children('div.overlay');
		$overlay.css({ height: (height - 1) + 'px', width: (width - 1) + 'px' });
		$overlay.hide();
		
		var $frame = $status.children('div.frame');
		$frame.css({ left: ((width - $frame.outerWidth()) / 2.0) + 'px' });
		
		$frame.find('h3').html(text);
		$frame.find('div.progress').addClass('active').show();
		$frame.hide();

		$status.data('visible', true);			
		$status.show();
		$overlay.fadeIn('fast', function() {
			$frame.slideDown('fast');
		});
	};

	var showProgressError = function(message) {
		var $status = $('#status'),
		    $frame = $status.children('div.frame');
	    
		$frame.find('a.close').fadeIn();
		$frame.find('div.progress').removeClass('active').fadeOut('fast', function() {
			var $error = $frame.find('p.error');
			if (!message) { message = 'Unable to retrieve contents. '; }
			$error.find('span.error-message').html(message);
			$error.fadeIn('fast');
			$frame.find('div.progress').hide();
		});
	};
	
	var hideProgress = function(callback) {
		var $status = $('#status');
		$status.data('visible', false);
		$status.children('div.frame').slideUp('fast', function() {
			$status.find('div.progress').removeClass('active');
			$status.children('div.overlay').fadeOut('fast', function() {
				$status.hide();
				if (callback) { callback(); }
			});
		});
	};

	$('#status').find('a.close').click(function() {
		hideProgress();
	});
	
	/**
	 * setup bottom loader
	 */
	$('#loader-bottom').find('a.close').click(function() {
		$(this).parent('.loading-container').fadeOut('fast', function() {
			$('p.refresh').fadeIn('fast');
		});
	});
	
	/**
	 * setup top loader
	 */
	$('#loader-top').find('a.close').click(function() {
		$(this).parent('.loading-container').fadeOut('fast');
	});

	/**
	 * setup code and history selector
	 */
	(function() {
		var selectFileHistory = function(gist, file, history, callback) {
	
			// get gist
			var $gist = $('#gist-' + gist);
			var uri = $('.gists').data('code-uri');
	
			// show dialog
			var $expanded = $gist.find('div.expanded');
			showProgress($expanded, 'Loading contents...');
	
			// get current states
			var selector = $('#code-' + gist + ' div.selector');
			file = file || selector.data('file');
			history = history || selector.data('history');
	
			// lookup uri
			$.ajax({
				url : uri, 
				data : {gist:gist, file:file, history:history}, 
				success : function(html) {
	
					// remove progress dialog
					hideProgress(function() {
						// pretty print results and update in code
						html = html.replace(/(\n|\r\n)/g, '<br />');
						var result = prettyPrintOne(html, null, true);
						$('#gist-' + gist + '-code').html(result);
		
						// update data attributes
						selector.data('file', file);
						selector.data('history', history);
		
						// invoke callback to update display
						callback();
					});
				},
				
				error : function(xhr, status, error) {
					// console.log('ERROR IN AJAX REQUEST: ', xhr, status, error);
					showProgressError("Unable to retrieve file contents.");
				}
			});
	
			// remove dialog
			$('#code-' + gist + ' div.btn-group.open').removeClass('open');
			
			// prevent normal link behavior
			event.preventDefault();
			return false;
		};
		
		$('.select-file').click(function(event) {
			var $this = $(this);
			var gist = $this.data('gist');
			var file = $this.data('file');
			return selectFileHistory(gist, file, null, function() {
				$this.parent('li').parent().children('li').removeClass('active');
				$this.parent('li').addClass('active');
				
				$('#gist-file-' + gist).html($this.html());
			});
		});
	
		$('.select-history').click(function(event) {
			var $this = $(this);
			var gist = $this.data('gist');
			var history = $this.data('history');
			return selectFileHistory(gist, null, history, function() {
				$this.parent('li').parent().children('li').removeClass('active');
				$this.parent('li').addClass('active');
				
				$('#gist-history-' + gist).html($this.html());
			});
		});
	})();
	
	/**
	 * gist initialization and column handling
	 */
	var initializeGists = function() {
		// pretty style the gist code
		prettyPrint();

		var idx = 0, heights = [0, 0, 0, 0, 0], padding = 20,
		    pageWidth = $('.gists').outerWidth(),
		    columns = Math.min(Math.floor(pageWidth / 700), 4),
		    width = (pageWidth / columns) - (padding * (columns - 1)),
			gists = [], $gists = [ ];
	
		$('.gists').data('columns', columns);
	
		var i = 0, j = 0, $gistdivs = $('.gists'), colspan = (12 / columns);
		for (i = columns - 1; i >= 0; i--) {
			var $gistdiv = $('<div class="gist-items gists-' + i + ' span' + colspan + '"></div>');
			$gistdivs.prepend($gistdiv);
			$gists.unshift($gistdiv);
			gists.unshift([]);
		}
	
		$('.gist').each(function() {
			idx++;
			var $this = $(this), height = $this.outerHeight();
	
			var column = -1;
			if (idx <= columns) { column = idx - 1; }
			else {
				var i = 0, max = -1, min = -1;
				for (i = 0; i < columns; i++) {
					if (max == -1 || heights[i] > heights[max]) { max = i; }
					if (min == -1 || heights[i] < heights[min]) { min = i; }
				}
	
				for (i = 0; i < columns; i++) {
					if (heights[i] + height < heights[max]) {
						column = i;
						break;
					}
				}
	
				if (column < 0) { column = min; }
			}
	
			var ypos = heights[column] + padding; 
			heights[column] += (height + padding);
			gists[column].push($this);
		});
	
		for (i = 0; i < gists.length; i++) {
			for (j = 0; j < gists[i].length; j++) {
				gists[i][j].appendTo($gists[i]);
			}
		}
	};
	
	/**
	 * setup infinite scroll and load more gists
	 */
	(function() {
		var loadedGists = { };
		var loadingOnScroll = false;

		var orderGists = function() {
		 	var i = 0, j = 0, idx = 0, heights = [0, 0, 0, 0, 0], padding = 20,
				gists = [ ], $gists = [ ], columns = $('.gists').data('columns');
	
			var $gistdivs = $('.gists'), colspan = (12 / columns);
			for (i = columns - 1; i >= 0; i--) {
				var $gistdiv = $('.gists-' + i);
				$gists.unshift($gistdiv);
				gists.unshift([]);
				heights[i] = $gistdiv.height();
			}
	
			$('.gists-loader .gist').each(function() {
				idx++;
				var i = 0, $this = $(this), height = $this.outerHeight(),
	                column = -1, max = -1, min = -1;
				
				for (i = 0; i < columns; i++) {
					if (max == -1 || heights[i] > heights[max]) { max = i; }
					if (min == -1 || heights[i] < heights[min]) { min = i; }
				}
	
				for (i = 0; i < columns; i++) {
					if (heights[i] + height < heights[max]) {
						column = i;
						break;
					}
				}
	
				if (column < 0) { column = min; }
	
				var ypos = heights[column] + padding; 
				heights[column] += (height + padding);
				gists[column].push($this);
			});
	
			for (i = 0; i < gists.length; i++) {
				for (j = 0; j < gists[i].length; j++) {
					gists[i][j].appendTo($gists[i]);
					
					// pretty print results and update in code
					var $prettyprint = gists[i][j].find('pre.prettyprint');
					var html = $prettyprint.html();
					html = html.replace(/(\n|\r\n)/g, '<br />');
					var result = prettyPrintOne(html, null, true);
					$prettyprint.html(result);
				}
			}
		};

		var loadGists = function() {
			var last = -1;
			$('.gists').children('div.gist-items').each(function() {
				var $last = $(this).children('div.gist').last();
				if ($last) {
					var clast = parseInt($last.attr('id').split('-')[1], 10);
					if (clast > last) { last = clast; }
				}
			});
			
			var uri = $('.gists').data('more-uri');
	
			if (loadedGists[last] != true) {
				$('p.refresh').hide();
				$('#loader-bottom').show();
				loadedGists[last] = true;
				loadingOnScroll = true;
	
				$.ajax({
					url:uri, 
					data:{ last:last, language:'${params.language}' }, 
					success:function(data) {
						if (data.trim().length > 0) {
							var pheight = $(document).height();
							$('.gists-loader').append($(data));
							orderGists();
							
							var cheight = $(document).height();
							$(document).data('infiniteScroll', 0.5*pheight + 0.5*cheight);
						}
						else {
							$(document).data('infiniteScroll', -1);
						}
	
						$('#loader-bottom').hide();
						loadingOnScroll = false;
					},
					
					error:function(xhr, status, error) {
						loadedGists[last] = false;
						var $loader = $('#loader-bottom');
						// console.log('ERROR IN AJAX REQUEST: ', xhr, status, error);
						$loader.find('a.close').fadeIn('fast');
						$loader.find('div.progress').fadeOut('fast', function() {
							$loader.find('p.error').fadeIn('fast');
							$loader.find('div.progress').hide();
						});
					}
				});
			}
		};
		
		$(window).scroll(function() {
			var scrollTop = $(window).scrollTop();
			var infiniteScroll = $(document).data('infiniteScroll');
			if (!loadingOnScroll && infiniteScroll != -1 && scrollTop >= infiniteScroll) {
				loadGists();
			}
		});

		$('p.refresh a').click(function() {
			loadGists(); 
			event.preventDefault();
			return false;
		});
	})();

	/**
	 * page load handling
	 */
	$(window).load(function() {

		// initialize gists
		initializeGists();
		
		// setup infinite scroll state
		var $document = $(document);
		$document.data('infiniteScroll', $document.height() * 0.50);
		
		// slide up progress and fade in gists
		$('#loader-top').slideUp('slow', function() {
			$('#loader-top').parent().hide();
			$('.gists').css('visibility', 'visible');
		});
	});

})(jQuery);
