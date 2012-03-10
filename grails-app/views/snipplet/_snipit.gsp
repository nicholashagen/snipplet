<style type="text/css">
	#snip-it-dialog .icon-bookmark { margin: 4px 5px 0 0; }
	form.snip-it-form input.btn { width: 125px; }
	form.snip-it-form .control-group label { width: 90px; }
	form.snip-it-form .controls { margin-left: 100px; }
	form.snip-it-form .input-xxlarge { width: 370px; }
	form.snip-it-form input[type="text"] { padding-right: 30px; }
	form.snip-it-form textarea { padding-right: 30px; }
	form.snip-it-form .input-append input[type="text"] { padding-right: 0 !important; }
	form.snip-it-form .input-append div.dropdown { cursor: pointer; }
	.typeahead.dropdown-menu { width: 274px; max-width: 274px; border-radius: 5px 0 5px 5px; margin-top: -1px; }
</style>

<div id="snip-it-dialog" class="blahblah">
	<g:formRemote id="snip-it-form" name="snip-it-form" url="[controller: 'user', action:'snip']"
	              class="snip-it-form form-horizontal"
	              on1Loading="\$.snipplet.snipit.showProgress()"
	              on1Success="\$.snipplet.snipit.update()"
	              on1Failure="\$.snipplet.snipit.fail()">

		<div class="modal modal-ext hide fade">
			<div class="modal-header">
				<a href="#" class="close" data-dismiss="modal">×</a>
				<h3>
					<i class="icon-bookmark icon-white"></i>
					Snip It
				</h3>
			</div>
			<div class="modal-body">
				<fieldset>
		    		<div class="control-group">
		      			<label class="control-label" for="name">Name</label>
		      			<div class="controls">
		        			<input type="text" class="name input-xxlarge" id="snip-it-name" name="name" />
		      			</div>
		    		</div>
		    		<div class="control-group">
		      			<label class="control-label" for="category">Category</label>
		      			<div class="controls">
		      				<div class="input-append">
			        			<input type="text" class="category input-xxlarge" id="snip-it-category" name="category" data-provide="typeahead" data-source='[ "Design Patterns", "Web Design", "Command Line" ]'/>
			        			<div class="add-on">
			        				<div class="dropdown">
					        			<a class="dropdown-toggle">
									      	<b class="caret"></b>
									    </a>
									</div>
								</div>
							</div>
		      			</div>
		    		</div>
		    		<div id="snip-it-options" class="collapse">
			    		<div class="control-group">
			      			<label class="control-label" for="description">Description</label>
			      			<div class="controls">
			        			<textarea class="description input-xxlarge" id="snip-it-description" name="description"></textarea>
			      			</div>
			    		</div>
			    		<div class="control-group">
			      			<label class="control-label" for="language">Language</label>
			      			<div class="controls">
			        			<input type="text" class="language input-xxlarge" id="snip-it-language" name="language" />
			      			</div>
			    		</div>
			    		<div class="control-group">
			      			<label class="control-label" for="tags">Tags</label>
			      			<div class="controls">
			        			<input type="text" class="tags input-xxlarge" id="snip-it-tags" name="tags" />
			      			</div>
			    		</div>
			    	</div>
		  		</fieldset>
			</div>
			<div class="modal-footer">
				<div class="pull-left">
					<input type="hidden" name="gist" id="snip-it-gist" value="" />
					<input type="submit" class="submit btn btn-primary" value="Snip It" />
				</div>
				<div class="pull-right">
					<a href="#snip-it-options" class="more-options btn">
						<i class="icon-chevron-down"></i>
						More Options 
					</a>
				</div>
				<br class="clear" />
			</div>
		</div>
	
	</g:formRemote>
</div>

<script type="text/javascript">
	$(function() {
		$('#category').typeahead({ 
			source:['Web Design', 'Design Patterns', 'Command Line'],
			matcher:function(item) {
				if (this.query == '') { return true; }
				return ~item.toLowerCase().indexOf(this.query.toLowerCase());
			}
		});
	
		$('#category').keyup(function(e) {
			if (e.keyCode == 40) {
				var typeahead = $(this).data('typeahead');
				if (typeahead) {
					if (!typeahead.shown) {
						var $input = $(this); 
						var val = $input.val();
						if (!val) { $input.val(' '); }
						setTimeout(function() { typeahead.lookup(); $input.val(''); }, 250);
					}
				}
			}	
		});

		$('form.snip-form .input-append div.dropdown').click(function() {
			var $input = $(this).parents('.input-append').find('input');
			var typeahead = $input.data('typeahead');
			if (typeahead) {
				var val = $input.val();
				if (!val) { $input.val(' '); }
				$input.focus();
				setTimeout(function() { typeahead.lookup(); $input.val(''); }, 250);
			}
		});

		$('.more-options').click(function() {
			var $this = $(this);
			if ($this.hasClass('in')) {
				$(this).find("i").removeClass('icon-chevron-up').addClass('icon-chevron-down');
			}
			else {
				$(this).find("i").removeClass('icon-chevron-down').addClass('icon-chevron-up');
			}

			var target = $(this).attr('href');
			if (target) { $(target).collapse({ toggle: true }); }
		});

		$('#snip-it-dialog a.submit').click(function() {
			// darken form, show spinner
			// submit form ajax
			// on success, hide modal, update gist display
			// on failure, show error dropdown
		});
		
		$('a.snip-it').click(function() {
			var $this = $(this), 
			    $gist = $this.parents('.gist'),
			    $form = $('#snip-it-form'), 
			    $dialog = $('#snip-it-dialog');

			var title = $gist.find("div.info h3").text();
			var language = $gist.find(".language").text();
			$form.find('input[name="gist"]').val($gist.data('gist'));
			console.log('TEST: ', $form.find('input[name="gist"]'));
			
			$form.find('input.name').val(title);
			$form.find('input.category').val('');
			$form.find('textarea.description').val();
			$form.find('input.language').val(language);
			$form.find('input.tags').val();
			
			$('#snip-it-options').removeClass('in');

			$dialog.find('.modal').modal();

			event.preventDefault();
			return false;
		});
	});
</script>
