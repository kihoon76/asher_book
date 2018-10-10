var Common = { socket: null, popupClose: function() {} };

$(document).on('pageinit', function () {
	
	$('body>[data-role="panel"]').panel();
	
	function openPopup(type, afterFn) {
		if(type == 'alert') {
			$('#btnPopupOk').hide();
			$('#btnPopupEtc').hide();
			$('#btnPopupCancel').text('확인');
		}
		else if(type == 'rental_manage') {
			$('#btnPopupEtc').data('category', 'rental_manage').text('삭제')
			$('#btnPopupOk').data('category', 'rental_manage').text('승인');
			$('#btnPopupCancel').text('취소');
			
			$('#btnPopupOk').show();
			$('#btnPopupEtc').show();
		}
		else if(type == 'rental_apply') {
			$('#btnPopupEtc').hide();
			$('#btnPopupOk').data('category', 'rental_apply').text('대여신청');
			$('#btnPopupCancel').text('취소');
			$('#btnPopupOk').show();
		}
		else if(type == 'rental_manage_return') {
			$('#btnPopupEtc').data('category', 'rental_manage_return').text('연장')
			$('#btnPopupOk').data('category', 'rental_manage_return').text('반납');
			$('#btnPopupCancel').text('취소');
			
			$('#btnPopupOk').show();
			$('#btnPopupEtc').show();
		}

		Common.popupClose = afterFn;
		$('#popupDialog').popup({history: false}).popup('open');
	}
	
	function closePopup() {
		$('#popupDialog').popup('close');
	}
	
	function makePopup(header, content, datas) {
		var $dvPopupContent = $('#dvPopupContent');
		//$('#popupHeader').html(header || '<span style="padding: 10px 10px 10px 10px; font-size:1.5em;">알림</span>');
		$('#popupHeader').html(header || '');
		$dvPopupContent.html(content || '');
		if(datas) {
			for(var i=0, len=datas.length; i<len; i++) {
				$dvPopupContent.data(datas[i].key, datas[i].value);
			}
		}
	}
	
	function makeErrMsg(errMsg) {
		makePopup('', errMsg);
		openPopup('alert');
	}
	
	Common.ajax = function(cfg) {
		$.ajax(cfg.url, {
			method: cfg.method || 'POST',
			dataType: cfg.dataType || 'json',
			data: cfg.data || {},
			contentType: cfg.contentType || 'application/x-www-form-urlencoded; charset=UTF-8',
			beforeSend: function() {
				$.mobile.loading('show', {
				    theme: 'a'
				});
			},
			complete: function(jqXHR, textStatus) {
			    $.mobile.loading('hide');
				closePopup();
			    
				if(jqXHR.errCode) {
					var msg = '';
					switch(jqXHR.errCode) {
					case '100': 
						msg = '세션만료됨';
						break;
					case '600': 
						msg = '미반납된 도서가 있습니다<br/>[' + jqXHR.data[0].bookName + ']';
						break;
					case '601': 
						msg = '[' + jqXHR.data[0].memberName + ']님에게 대여중입니다.<br/>' + '대여일자 [' + jqXHR.data[0].rentalDate + ']';
						break;
					case '602': 
						msg = '[' + jqXHR.data[0].memberName + ']님이 대여신청중입니다.<br/>' + '신청일자 [' + jqXHR.data[0].rentalDate + ']';
						break;
					default: 
						msg = '오류발생';
						break;
					}
					
					setTimeout(function() {
						makeErrMsg(msg);
					}, 500);
				}
				else {
					//성공 이후 final로 실행할 함수
					if(jqXHR.runFinal) {
						setTimeout(jqXHR.runFinal, 500);
					}
				}
			},
			success: function(data, textStatus, jqXHR) {
				if(data.success) {
					cfg.success(data, textStatus, jqXHR); 
				}
				else {
					jqXHR.errCode = data.errCode;
					jqXHR.data = data.datas;
				}
			},
			error: function(jqXHR, textStatus, err) {
				
			}
		});
	}
	
	$(document)
	.off('click', 'a.IMG_POPUP')
	.on('click', 'a.IMG_POPUP', function() {
		var $this = $(this);
		var $img = $this.find('img');
		var possible = $img.data('possible');
		var msg = '';
		if('R' == possible) {
			msg = '[' + $img.data('name') + ']는 ' + $img.data('rentalMan') + '님이 대여요청중 입니다.';
			makePopup('', msg);
			openPopup('alert');
		}
		else if('A' == possible) {
			msg = '[' + $img.data('name') + ']는 ' + $img.data('rentalMan') + '님이 대여중 입니다.';
			makePopup('', msg);
			openPopup('alert');
		}
		else {
			makePopup('', + $img.data('num') + '.' + '[' + $img.data('name') + ']를 대여신청 하시겠습니까?', [{key: 'bookNum', value: $img.data('num')}]);
			openPopup('rental_apply');
		}
		
		
	});
	
	$(document)
	.off('click', 'a.RENTAL_SELECT')
	.on('click', 'a.RENTAL_SELECT', function() {
		var $this = $(this);
		var status = $this.data('status');
		var cate = '';
		
		var msg = '';
		if(status == 'R') {
			msg = $this.data('rentalMan') + '님의 [' + $this.data('bookName') + '] 대여승인/삭제 하시겠습니까?';
			cate = 'rental_manage';
		}
		else if(status == 'A') {  //도서반납
			msg = $this.data('rentalMan') + '님의 [' + $this.data('bookName') + ']를 반납/연장 하시겠습니까?';
			cate = 'rental_manage_return';
		}
		
		makePopup('', msg, [
		  {key: 'rentalIdx', value: $this.data('rentalIdx')},
		  {key: 'rentalManIdx', value: $this.data('rentalManIdx')},
		  {key: 'bookNum', value: $this.data('bookNum')},
		  {key: 'memberName', value: $this.data('rentalMan')}
		  
		]);
		openPopup(cate);
//		if('R' == possible) {
//			msg = '[' + $img.data('name') + ']는 ' + $img.data('rentalMan') + '님이 대여요청중 입니다.';
//			makePopup('', msg);
//			openPopup('alert');
//		}
//		else if('A' == possible) {
//			msg = '[' + $img.data('name') + ']는 ' + $img.data('rentalMan') + '님이 대여중 입니다.';
//			makePopup('', msg);
//			openPopup('alert');
//		}
//		else {
//			makePopup('', + $img.data('num') + '.' + '[' + $img.data('name') + ']를 대여신청 하시겠습니까?', [{key: 'bookNum', value: $img.data('num')}]);
//			openPopup();
//		}
		
		
	});
	
	$(document).on('click', '#btnPopupOk', function() {
		var $this = $(this);
		var cate = $(this).data('category');
		var $dvPopupContent = $('#dvPopupContent');
		
		switch(cate) {
		case 'rental_apply' :	//대여신청
			Common.ajax({
				url: '/user/apply/rental',
				method: 'POST',
				dataType: 'json',
				data: {bookNum: $dvPopupContent.data('bookNum')},
				success: function(data, textStatus, jqXHR) {
					jqXHR.runFinal = function() {
						makePopup('', '대여신청 되었습니다.');
						openPopup('alert', function() {
							//window.location.reload();
						});
					}
					
					
				},
			});
			break;
		case 'rental_manage': 	//대여신청 승인
			Common.ajax({
				url: '/admin/accept/rental/apply',
				method: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				data: JSON.stringify({
					rentalIdx: $dvPopupContent.data('rentalIdx'),
					rentalManIdx: $dvPopupContent.data('rentalManIdx'),
					bookNum: $dvPopupContent.data('bookNum'),
					memberName: $dvPopupContent.data('rentalMan')
				}),
				success: function(data, textStatus, jqXHR) {
					jqXHR.runFinal = function() {
						
						makePopup('', '대여되었습니다.');
						openPopup('alert', function() {
							window.location.reload();
						});
					}
					
					
				},
			});
			break;
		case 'rental_manage_return' :	//대여반납
			Common.ajax({
				url: '/admin/return/rental',
				method: 'POST',
				dataType: 'json',
				contentType: 'application/json',
				data: JSON.stringify({
					rentalIdx: $dvPopupContent.data('rentalIdx'),
					rentalManIdx: $dvPopupContent.data('rentalManIdx'),
					bookNum: $dvPopupContent.data('bookNum'),
					memberName: $dvPopupContent.data('rentalMan')
				}),
				success: function(data, textStatus, jqXHR) {
					jqXHR.runFinal = function() {
						
						makePopup('', '반납되었습니다.');
						openPopup('alert', function() {
							window.location.reload();
						});
					}
				},
			});
			break;
		}
	});
	
	$(document).on('click', '#btnPopupEtc', function() {
		var $this = $(this);
		var cate = $(this).data('category');
		var $dvPopupContent = $('#dvPopupContent');
		
		console.log(cate)
		switch(cate) {
		case 'rental_manage' :
			break;
		case 'rental_manage_return' :
			break;
		}
	});
	
	
	//$popupDialog.open();
	$('[data-role=panel] a').on('click', function () {
		var $this = $(this);
		console.log($this)
		
		if($this.attr('href') != '#') {
			$('#leftpanel1').panel('close');
		}
		
	});
	
	$('#footerLogout').on('click', function() {
		Common.ajax({
			url: '/logout',
			method: 'POST',
			dataType: 'json',
			contentType: 'application/json',
			success: function(data, textStatus, jqXHR) {
				window.location.href = '/signin';
			},
		});
	});
});

$(document).on('pageshow', function (event, ui) {
    // Remove the previous page
    $(ui.prevPage).remove();
    
    ///book/rental_history
    if($('#dvRentalHistory').get(0)) {
    	Common.socket = new SockJS('/rental_book/list');
		
    	Common.socket.onopen = function(event) {
    		console.log(event)
    	}
    	
    	Common.socket.onmessage = function(event) {
    		try {
    			var data = $.parseJSON(event.data);
    			var selector = '#book' + data.bookNum + 'Rental';
    			if(data.type == 'R') {
    				//대여신청
    				$(selector).addClass('apply');
    				$(selector).text('신청');
    				$(selector + 'Man').text(data.memberName);
    				$(selector + 'Image').data('possible', 'R');
    				$(selector + 'Image').data('rentalMan', data.memberName);
    				
    			}
    			else if(data.type == 'A') {
    				$(selector).removeClass('apply');
    				$(selector).addClass('norental');
    				$(selector).text('대여');
    				$(selector + 'Man').text(data.memberName);
    				$(selector + 'Image').data('possible', 'A');
    				$(selector + 'Image').data('rentalMan', data.memberName);
    			}
    			else if(data.type == 'T') { //반납
    				$(selector).removeClass('norental');
    				$(selector).text('가능');
    				$(selector + 'Man').html('&nbsp;');
    				$(selector + 'Image').data('possible', 'Y');
    				$(selector + 'Image').data('rentalMan', null);
    			}
    		}
    		catch(e) {}
			
		}
    }
});

$(document).on('pageremove', function (event, ui) {
	console.log('yy')
    if(Common.socket != null) {
    	 Common.socket.close();
    }
});

$(window).unload(function() {
	if(Common.socket != null) {
   	 Common.socket.close();
   }
});


$(document).on('popupafterclose', "[data-role=popup]", function (e) {
	if(Common.popupClose) {
		Common.popupClose();
		Common.popupClose = null;
	}
	
    console.log(e.target.id + " -> " + e.type);
});

//$(document).on("panelbeforeopen", "div[data-role='panel']", function(e,ui) {
//    $.mobile.activeClickedLink = null;
//});