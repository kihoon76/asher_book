$(document).ready(function() {
	$('#btnLogin').on('click', function() {
		$.ajax('login', {
			method: 'POST',
			dataType: 'text',
			data: $('#loginFm').serialize(),
			beforeSend: function() {
				$.mobile.loading('show', {
				    theme: "a"
				});
			},
			complete: function() {
			    $.mobile.loading('hide');
			},
			success: function(data, textStatus, jqXHR) {
				var jo = $.parseJSON(data);
				if(jo.success) {
					window.location.href = 'main';
				}
				else {
					$(':mobile-pagecontainer').pagecontainer('change', '#dialog');
				}
			},
		});
	});
});