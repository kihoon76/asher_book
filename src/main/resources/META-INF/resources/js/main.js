$(document).ready(function() {
	var screen = $.mobile.getScreenHeight(),
	    header = $('.ui-header').hasClass('ui-header-fixed') ? $('.ui-header').outerHeight() - 1 : $('.ui-header').outerHeight(),
	    footer = $('.ui-footer').hasClass('ui-footer-fixed') ? $('.ui-footer').outerHeight() - 1 : $('.ui-footer').outerHeight(),
	    contentCurrent = $('.ui-content').outerHeight() - $('.ui-content').height(),
	    content = screen - header - footer - contentCurrent;

	$('.ui-content').height(content);
	$('body>[data-role="panel"]').panel();
	

});

$(document).on('pageinit', function () {
	$('[data-role=panel] a').on('click', function () {
		var $this = $(this);
		console.log($this)
		
		if($this.attr('href') != '#') {
			$('#leftpanel1').panel('close');
		}
		
	});
	
});

//$(document).on("panelbeforeopen", "div[data-role='panel']", function(e,ui) {
//    $.mobile.activeClickedLink = null;
//});