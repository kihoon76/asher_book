var Common = { socket: null };

$(document).on('pageinit', function () {
	
	$('body>[data-role="panel"]').panel();
	
	function openPopup(type) {
		if(type == 'alert') {
			$('#btnPopupBookRental').hide();
			$('#btnPopupDelegate').text('확인');
		}
		else {
			$('#btnPopupBookRental').show();
			$('#btnPopupDelegate').text('취소');
		}

		$('#popupDialog').popup({history: false}).popup('open');
	}
	
	function closePopup() {
		$('#popupDialog').popup('close');
	}
	
	function makePopup(header, content, datas) {
		var $dvPopupContent = $('#dvPopupContent');
		$('#popupHeader').html(header || '');
		$dvPopupContent.html(content || '');
		if(datas) {
			for(var i=0, len=datas.length; i<len; i++) {
				$dvPopupContent.data(datas[i].key, datas[i].value);
			}
		}
	}
	
	function makeErrMsg(errMsg) {
		makePopup('알림', errMsg);
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
				    theme: "a"
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
					default: 
						msg = '오류발생';
						break;
					}
					
					setTimeout(function() {
						makeErrMsg(msg);
					}, 500);
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
		makePopup('<span style="padding: 10px 10px 10px 10px; font-size:1.5em;">대여</span>', + $img.data('num') + '.' + '[' + $img.data('name') + ']를 대여하시겠습니까?', [{key: 'bookNum', value: $img.data('num')}]);
		openPopup();
	});
	
	$(document).on('click', '#btnPopupBookRental', function() {
		
		Common.ajax({
			url: '/user/rental',
			method: 'POST',
			dataType: 'json',
			data: {bookNum: $('#dvPopupContent').data('bookNum')},
			success: function(data, textStatus, jqXHR) {
				console.log('pp')
			}
		});
		
		
	});
	//$popupDialog.open();
	$('[data-role=panel] a').on('click', function () {
		var $this = $(this);
		console.log($this)
		
		if($this.attr('href') != '#') {
			$('#leftpanel1').panel('close');
		}
		
	});
	
	
	
});

$(document).on('pageshow', function (event, ui) {
    // Remove the previous page
    $(ui.prevPage).remove();
    
    ///book/rental_history
    if($('#dvRentalHistory').get(0)) {
    	Common.socket = new SockJS('/rental_book/list');
		
    	Common.socket.onmessage = function(event) {
			
		}
    }
   
});

$(document).on('pageremove', function (event, ui) {
	console.log('yy')
    if(Common.socket != null) {
    	 Common.socket.close();
    }
});


//$(document).on('popupcreate popupinit popupafteropen popupafterclose", "[data-role=popup]", function (e) {
//    console.log(e.target.id + " -> " + e.type);
//});

//$(document).on("panelbeforeopen", "div[data-role='panel']", function(e,ui) {
//    $.mobile.activeClickedLink = null;
//});