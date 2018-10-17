$(document).ready(function() {
	$('#pw').on('keyup', function(e) {
		if(e.keyCode == 13) {
			$('#btnLogin').trigger('click');
		}
	});
	
	$('#btnLogin').on('click', function() {
		var $id = $('#id');
		var $pw = $('#pw');
		
		if($.trim($id.val()) == '') {
			$id.focus();
			return;
		}
		
		if($.trim($pw.val()) == '') {
			$pw.focus();
			return;
		}
		
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
	
	var userAgent = navigator.userAgent.toLowerCase(); // 접속 핸드폰 정보 
	   
	// 모바일 홈페이지 바로가기 링크 생성 
	if(userAgent.match('iphone')) { 
	    $('head').append('<link rel="apple-touch-icon" href="/resources/img/asher.png" />') 
	} 
	else if(userAgent.match('android')) { 
		$('head').append('<link rel="shortcut icon" href="/resources/img/asher.png" />') 
	} 
});