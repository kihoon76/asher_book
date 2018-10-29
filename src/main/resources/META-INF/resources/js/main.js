var Common = { 
	socket: null,
	popupClose: function() {},
	useCacheReserPop: true,
	getFullHeight: function() {
		var screen = $.mobile.getScreenHeight();
    	var header = $(".ui-header").hasClass("ui-header-fixed") ? $(".ui-header").outerHeight()  - 1 : $(".ui-header").outerHeight();
    	var footer = $(".ui-footer").hasClass("ui-footer-fixed") ? $(".ui-footer").outerHeight() - 1 : $(".ui-footer").outerHeight();

    	/* content div has padding of 1em = 16px (32px top+bottom). This step
    	   can be skipped by subtracting 32px from content var directly. */
    	var contentCurrent = $('.ui-content').outerHeight() - $('.ui-content').height();
    	var content = screen - header - footer - contentCurrent;
    	
    	return content - 32 - 32 - 32;
	},
	kakakoInit: function() {
		try{
			Kakao.init('a932718b73c047da4fd29bd762563cd4');
		}
		catch(e) {}
	},
	createLinkKakao: function(cfg) {
		// 카카오링크 버튼을 생성합니다. 처음 한번만 호출하면 됩니다.
		Kakao.Link.createDefaultButton({
			container: cfg.container,
		    objectType: 'feed',
		    content: {
		    	title: cfg.title,
		        imageUrl: cfg.imageUrl, 
		        description: cfg.description,
		        link: {
		          mobileWebUrl: cfg.mobileWebUrl || '',
		        }
		   },
		});
	},
	makeBookReser: function(json) {
		var $selRentedBook = $('#selRentedBook');
		if(json) {
			var len = json.length;
			
			if(len > 0) {
				var optionArr = [];
				
				optionArr.push('<option value="-1">== 대여예약하실 도서를 선택하세요 ==</option>');
				for(var i=0; i<len; i++) {
					optionArr.push('<option value="' + json[i].bookNum + '">' +  json[i].bookName + '</option>');
				}
				
				$selRentedBook.html(optionArr.join(''));
				
			}
			else {
				$selRentedBook.html('<option value="-1">== 대여중인 도서가 없습니다. ==</option>');
			}
		}
	},
	makeReserMembers: function(json) {
		var $selReservation = $('#selReservation');
		var $spBookReserCount = $('#spBookReserCount');
		
		if(json) {
			var len = json.length;
			
			if(len > 0) {
				var arr = [];
				for(var i=0; i<len; i++) {
					arr.push('<option value="' +  json[i].idx+ '">' + (i+1) + '.' + json[i].name + '(' + json[i].id + ')</option>')
				}
				
				$selReservation.html(arr.join(''));
				$spBookReserCount.text(len);
			}
			else {
				$selReservation.html('<option value="-1">예약하신분이 없습니다.</option>');
				$spBookReserCount.text('0');
			}
		}
	},
};

$(document)
.off('pageinit')
.on('pageinit', function () {
	$('body>[data-role="panel"]').panel();
	$('#bookReservePopup').hide();
	
	Common.kakakoInit();
	function openPopup(type, afterFn) {
		if(type == 'alert') {
			$('#btnPopupOk').hide();
			$('#btnPopupEtc').hide();
			$('#btnPopupCancel').text('확인');
		}
		else if(type == 'rental_manage') {
			$('#btnPopupEtc').data('category', 'rental_manage').text('삭제')
			$('#btnPopupOk').data('category', 'rental_manage').text('승인');
			$('#btnPopupCancel').text('닫기');
			
			$('#btnPopupOk').show();
			$('#btnPopupEtc').show();
		}
		else if(type == 'rental_apply') {
			$('#btnPopupEtc').hide();
			$('#btnPopupOk').data('category', 'rental_apply').text('대여신청');
			$('#btnPopupCancel').text('닫기');
			$('#btnPopupOk').show();
		}
		else if(type == 'rental_manage_return') {
			$('#btnPopupEtc').data('category', 'rental_manage_return').text('연장')
			$('#btnPopupOk').data('category', 'rental_manage_return').text('반납');
			$('#btnPopupCancel').text('닫기');
			
			$('#btnPopupOk').show();
			$('#btnPopupEtc').show();
		}
		else if(type == 'apply_cancel') {
			$('#btnPopupEtc').hide();
			$('#btnPopupOk').show();
			$('#btnPopupOk').data('category', 'apply_cancel').text('신청취소');
			$('#btnPopupCancel').text('닫기');
		}
		else if(type == 'cancel_reservation') {
			$('#btnPopupEtc').hide();
			$('#btnPopupOk').data('category', 'cancel_reservation').text('예약취소');
			$('#btnPopupCancel').text('닫기');
			$('#btnPopupOk').show();
		}

		Common.popupClose = afterFn;
		$('#popupDialog').popup('open');
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
	
	function makeErrMsg(errMsg, fn) {
		makePopup('', errMsg);
		openPopup('alert', fn);
	}
	
	Common.ajax = function(cfg) {
		$.ajax(cfg.url, {
			method: cfg.method || 'POST',
			dataType: cfg.dataType || 'json',
			data: cfg.data || {},
			contentType: cfg.contentType || 'application/x-www-form-urlencoded; charset=UTF-8',
			headers: cfg.headers || {},
			beforeSend: function() {
				$.mobile.loading('show', {
				    theme: 'a'
				});
			},
			complete: function(jqXHR, textStatus) {
			    $.mobile.loading('hide');
			    
			    if(!cfg.isSystem) closePopup();
			    
				if(jqXHR.errCode) {
					var msg = '';
					var fn = null;
					
					switch(jqXHR.errCode) {
					case '100': 
						msg = '세션만료됨';
						fn = function() {
							window.location.href = '/signin'
						};
						break;
					case '600': 
						msg = '미반납된 도서가 있습니다<br/>[' + jqXHR.data[0].bookName + ']';
						break;
					case '601': 
						msg = '[' + jqXHR.data[0].rentalMan + ']님에게 대여중입니다.<br/>' + '대여일자 [' + jqXHR.data[0].rentalDate + ']';
						break;
					case '602': 
						msg = '[' + jqXHR.data[0].rentalMan + ']님이 대여요청중입니다.<br/>' + '신청일자 [' + jqXHR.data[0].rentalDate + ']';
						break;
					case '603': 
						msg = '대여신청 취소를 하실수 없습니다.';
						break;
					case '604': 
						msg = '사용자 등록에 실패했습니다.';
						break;
					case '605': 
						msg = '도서가 대여중이 아닙니다.';
						break;
					case '606': 
						msg = '이미 예약한 도서입니다 ';
						break;
					case '607': 
						msg = '내가 예약한 도서가 아닙니다.';
						break;
					default: 
						msg = '오류가 발생했습니다.';
						break;
					}
					
					//alert창 사용
					if(cfg.isSystem) {
						alert(msg);
						if(fn) fn();
					}
					else {
						setTimeout(function() {
							makeErrMsg(msg, fn);
						}, 500);
					}
				}
				else {
					//성공 이후 final로 실행할 함수
					if(jqXHR.runFinal) {
						setTimeout(jqXHR.runFinal, 500);
					}
				}
				
			},
			success: function(data, textStatus, jqXHR) {
				var json = $.parseJSON(jqXHR.responseText);
				if(json.success) {
					cfg.success(json, textStatus, jqXHR); 
				}
				else {
					jqXHR.errCode = json.errCode;
					jqXHR.data = json.datas;
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
		
		$.scrollLock(true);
		if('R' == possible) {
			if('Y' == $img.data('mine')) {
				msg = '[' + $img.data('name') + '] 대여신청을 취소하시겠습니까? ';
				makePopup('', msg, [{key: 'bookNum', value: $img.data('num')}]);
				openPopup('apply_cancel');
			}
			else {
				msg = '[' + $img.data('name') + ']는 ' + $img.data('rentalMan') + '님이 대여요청중 입니다.';
				makePopup('', msg);
				openPopup('alert');
			}
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
	
	$(document)
	.off('click', '#btnPopupOk')
	.on('click', '#btnPopupOk', function() {
		var $this = $(this);
		var cate = $(this).data('category');
		var $dvPopupContent = $('#dvPopupContent');
		
		switch(cate) {
		case 'rental_apply' :	//대여신청
			Common.ajax({
				url: '/user/apply/rental',
				method: 'POST',
				dataType: 'json',
				headers: {'CUSTOM': 'Y'},
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
				headers: {'CUSTOM': 'Y'},
				contentType: 'application/json',
				data: JSON.stringify({
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
				headers: {'CUSTOM': 'Y'},
				contentType: 'application/json',
				data: JSON.stringify({
					memberIdx: $dvPopupContent.data('rentalManIdx'),
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
		case 'apply_cancel' :  //신청취소
			Common.ajax({
				url: '/user/cancel/apply',
				method: 'POST',
				dataType: 'json',
				headers: {'CUSTOM': 'Y'},
				data: {bookNum: $dvPopupContent.data('bookNum')},
				success: function(data, textStatus, jqXHR) {
					jqXHR.runFinal = function() {
						makePopup('', '신청취소되었습니다.');
						openPopup('alert', function() {
							//window.location.reload();
						});
					}
				},
			});
			break;
		case 'cancel_reservation' : //예약취소
			Common.ajax({
				url: '/user/reserve/cancel',
				type: 'POST',
				headers: {'CUSTOM': 'Y'},
				data: {
					reserveBookNum: $dvPopupContent.data('reserveBookNum')
				},
				success: function(data, textStatus, jqXHR) {
					jqXHR.runFinal = function() {
						makePopup('', '예약취소되었습니다.');
						openPopup('alert', function() {
							$.mobile.loading('show', {
							    theme: 'a'
							});
							window.location.reload();
						});
					}
					
				},
			});
			break;
		}
		
	});
	
	$(document)
	.off('click', '#btnPopupEtc')
	.on('click', '#btnPopupEtc', function() {
		var $this = $(this);
		var cate = $(this).data('category');
		var $dvPopupContent = $('#dvPopupContent');
		
		console.log(cate)
		switch(cate) {
		case 'rental_manage' : //관리자 대여신청 취소
			Common.ajax({
				url: '/admin/cancel/apply',
				method: 'POST',
				dataType: 'json',
				headers: {'CUSTOM': 'Y'},
				data: {bookNum: $dvPopupContent.data('bookNum'), memberIdx: $dvPopupContent.data('rentalManIdx')},
				success: function(data, textStatus, jqXHR) {
					jqXHR.runFinal = function() {
						makePopup('', '신청삭제되었습니다.');
						openPopup('alert', function() {
							window.location.reload();
						});
					}
				},
			});
			break;
		case 'rental_manage_return' : //반납기간 연장
			Common.ajax({
				url: '/admin/extension/return',
				method: 'POST',
				dataType: 'json',
				headers: {'CUSTOM': 'Y'},
				data: {bookNum: $dvPopupContent.data('bookNum')},
				success: function(data, textStatus, jqXHR) {
					jqXHR.runFinal = function() {
						makePopup('', '7일 연장되었습니다.');
						openPopup('alert', function() {
							window.location.reload();
						});
					}
				},
			});
			break;
		}
	});
	
	
	//$popupDialog.open();
	$('[data-role=panel] a')
	.off('click')
	.on('click', function () {
		var $this = $(this);
		console.log($this)
		
		if($this.attr('href') != '#') {
			$('#leftpanel1').panel('close');
		}
		
	});
	
	//로그아웃
	$(document)
	.off('click','#footerLogout')
	.on('click', '#footerLogout', function() {
		Common.ajax({
			url: '/logout',
			method: 'POST',
			dataType: 'json',
			headers: {'CUSTOM': 'Y'},
			contentType: 'application/json',
			success: function(data, textStatus, jqXHR) {
				window.location.href = '/signin';
			},
		});
	});
	
	$(document)
	.off('click', '#btnRegUser')
	.on('click', '#btnRegUser', function() {
		var $userId = $('#userId'),
			$userName = $('#userName'),
			$userPhone = $('#userPhone'),
			$userEmail = $('#userEmail');
		
		var userId = $.trim($userId.val());
		if(userId == '') {
			$userId.focus();
			return;
		}
		
		var userName = $.trim($userName.val());
		if(userName == '') {
			$userName.focus();
			return;
		}
		
		var userPhone = $.trim($userPhone.val());
		
		if(userPhone == '') {
			$userPhone.focus();
			return;
		}
		
		if(!/^\d{3}-\d{3,4}-\d{4}$/gm.test(userPhone)) {
			$userPhone.val('');
			$userPhone.focus();
			return;
		}
		
		var userEmail = $.trim($userEmail.val());
		
		if(userEmail == '') {
			$userEmail.focus();
			return;
		}
		
		if(!/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i.test(userEmail)) {
			$userEmail.val('');
			$userEmail.focus();
			return;
		}
		
		
		Common.ajax({
			url: '/admin/reg/user',
			method: 'POST',
			dataType: 'json',
			headers: {'CUSTOM': 'Y'},
			contentType: 'application/json',
			data: JSON.stringify({
				userId: userId,
				userName: userName,
				userPhone: userPhone,
				userEmail: userEmail
			}),
			success: function(data, textStatus, jqXHR) {
				jqXHR.runFinal = function() {
					makePopup('', '사용자가 등록되었습니다.');
					openPopup('alert', function() {
						$userId.val('');
						$userName.val('');
						$userPhone.val('');
						$userEmail.val('');
					});
				}
			},
		});
	});
	
//	$(document)
//	.off('touchmove', 'html')
//	.on('touchmove', 'html', function (event) {
//	    //only run code if the user has two fingers touching
//	    if(event.originalEvent.touches.length === 2) {
//	    	event.preventDefault();
//	    	return false;
//	    }
//	});
	
	$(document)
	.off('click', '#chooseFile')
	.on('click', '#chooseFile', function(e) {
		e.preventDefault();
		//$('input[type=file]').trigger('click');
		$('#eventImage').trigger('click');
		
	});
	
	$(document)
	.off('change', '#eventImage')
	.on('change', '#eventImage', function(e) {
		var file = $('#eventImage')[0].files[0];            
		$('#preview').empty();
		displayAsImage3(file, 'preview');
		
		$info = $('#info');
		$info.empty();
		if (file && file.name) {
			$info.append('<li>파일명:<span>' + file.name + '</span></li>');
		}
		if (file && file.type) {
			$info.append('<li>파일타입:<span>' + file.type + ' </span></li>');
		}
		if (file && file.size) {
			$info.append('<li>파일크기:<span>' + file.size + ' bytes</span></li>');
		}
		if (file && file.lastModifiedDate) {
			$info.append('<li>마지막 수정일:<span>' + file.lastModifiedDate.toISOString().substring(0, 10) + ' </span></li>');
		}
		$info.listview('refresh');
	});
	
	
	
	$(document)
	.off('click', '#eventContent')
	.on('click', '#eventContent', function() {
		$(this).keyup();
	});
	
	function displayAsImage3(file, containerid) {
		if(typeof FileReader !== "undefined") {
			var container = document.getElementById(containerid),
				img = document.createElement("img"),
				reader;
			container.appendChild(img);
			reader = new FileReader();
			reader.onload = (function (theImg) {
				return function (evt) {
					theImg.src = evt.target.result;
				};
			}(img));
			reader.readAsDataURL(file);
		}
	}
	
	//이벤트 등록
	$(document)
	.off('click', '#btnRegEvent')
	.on('click', '#btnRegEvent', function() {
		var $eventTitle = $('#eventTitle'),
			$eventContent = $('#eventContent');
		
		if($.trim($eventTitle.val()) == '') {
			$eventTitle.focus();
			return;
		}
		
		if($.trim($eventContent.val()) == '') {
			$eventContent.focus();
			return;
		}
		
		var $eventImage = $('#eventImage');
		var $form = $('#eventRegForm');
		var fileObj = $eventImage[0].files[0];
		
		if(fileObj) {
			var formData = new FormData($form[0]);
			formData.append('eventTitle', $eventTitle.val());
			formData.append('eventContent', $eventContent.val());
			formData.append('eventImage', fileObj);
			
			console.log($eventTitle.val());
			$.ajax({
				url: '/upload/event/regWithPic',
				processData: false,
				contentType: false,
				headers: {'CUSTOM': 'Y'},
				beforeSend: function() {
					$.mobile.loading('show', {
					    theme: 'a'
					});
				},
				type: 'POST',
				data: formData,
				success: function(data, textStatus, jqXHR) {
					var json = $.parseJSON(jqXHR.responseText);
					if(json.success) {
						jqXHR.runFinal = function() {
							makePopup('', '이벤트가 등록되었습니다.');
							openPopup('alert', function() {
								window.location.reload();
							});
						}
					}
					else {
						jqXHR.errCode = json.errCode;
					}
				},
				complete: function(jqXHR, textStatus) {
				    $.mobile.loading('hide');
				    
				    if(jqXHR.errCode) {
						var msg = '';
						var fn = null;
						
						switch(jqXHR.errCode) {
						case '100': 
							msg = '세션만료됨';
							fn = function() {
								window.location.href = '/signin'
							};
							break;
						default: 
							msg = '오류가 발생했습니다.';
							break;
						}
						
						setTimeout(function() {
							makeErrMsg(msg, fn);
						}, 500);
					}
					else {
						//성공 이후 final로 실행할 함수
						if(jqXHR.runFinal) {
							setTimeout(jqXHR.runFinal, 500);
						}
					}
				}
				
			});
		}
		else {
			Common.ajax({
				url: '/upload/event/regWithNoPic',
				method: 'POST',
				dataType: 'json',
				headers: {'CUSTOM': 'Y'},
				contentType: 'application/json',
				data: JSON.stringify({
					eventTitle: $.trim($eventTitle.val()),
					eventContent: $.trim($eventContent.val())
				}),
				success: function(data, textStatus, jqXHR) {
					jqXHR.runFinal = function() {
						makePopup('', '이벤트가 등록되었습니다.');
						openPopup('alert', function() {
							window.location.reload();
						});
					}
				},
			});
		}
	});
	
	//이벤트 삭제
	$(document)
	.off('click', '#deleteEvent')
	.on('click', '#deleteEvent', function() {
		if(confirm('이벤트를 삭제하시겠습니까?')) {
			var suffix = $(this).data('suffix');
			
			Common.ajax({
				url: '/admin/event/delete',
				method: 'POST',
				dataType: 'json',
				headers: {'CUSTOM': 'Y'},
				data: {
					suffix: suffix
				},
				success: function(data, textStatus, jqXHR) {
					alert('이벤트가 삭제되었습니다');
					window.history.back();
				},
			});
		}
	});
	
	$(document)
	.off('click', '#footerPrevious')
	.on('click', '#footerPrevious', function() {
		window.history.back();
	});
	
	$(document)
	.off('keyup', '#eventSearch')
	.on('keyup', '#eventSearch', function(e) {
		if(e.keyCode == 13) {
			var v = $.trim($(this).val());
			
			if(v == '') {
				$(this).focus();
				return;
			}
			
			window.location.href = '/admin/event/list?search=' + v;
		}
	});
	
	$(document)
	.off('click dblclick', '#footerReservation')
	.on('click dblclick', '#footerReservation', function(e) {
		e.stopPropagation();
		e.preventDefault();
		var $this = $(this);
		
		if(!$this.data('disabled')) {
			$this.addClass('ui-disabled');
			$this.data('disabled', true);
			
			if(Common.useCacheReserPop) {
				$('#bookReservePopup').show();
				$('#bookReservePopup').popup('open', {transition: 'pop'});
				$.scrollLock(true);
			}
			else {
				Common.ajax({
					isSystem: true,
					url: 'reserve/booklist',
					method: 'GET',
					dataType: 'json',
					headers: {'CUSTOM': 'Y'},
					success: function(data, textStatus, jqXHR) {
						Common.makeBookReser(data.datas);
						Common.makeReserMembers([]);
						Common.useCacheReserPop = true;
						
						$('#bookReservePopup').show();
						$('#bookReservePopup').popup('open', {transition: 'pop'});
						$.scrollLock(true);
					},
				});
			}
		}
	});
	
	//link button active remove
	$(document)
	.on('tap', '#footerReservation,#btnRegEvent', function(e) {
		console.log('tap');
		$(this).removeClass('ui-btn-active');
	});
	
	$(document)
	.off('click', '#footerHome')
	.on('click', '#footerHome', function(e) {
		$.mobile.loading('show', {
		    theme: 'a'
		});
		window.location.href = '/main';
	});
	
	//책예약
	$(document)
	.off('click', '#btnBookReservation')
	.on('click', '#btnBookReservation', function() {
		var $selRentedBook = $('#selRentedBook');
		
		if($selRentedBook.val() == '-1') {
			alert('예약하실 도서를 선택하세요.');
			return;
		}
		
		Common.ajax({
			isSystem: true,
			url: '/book/reserve',
			type: 'POST',
			headers: {'CUSTOM': 'Y'},
			data: {
				reserveBookNum: $selRentedBook.val()
			},
			success: function(data, textStatus, jqXHR) {
				Common.makeReserMembers(data.datas);
				alert('예약되었습니다.');
				
			},
		});
	});
	
	
	$(document)
	.off('click', '.DEL-RESERVE')
	.on('click', '.DEL-RESERVE', function() {
		var $this = $(this);
		
		makePopup('', '대여예약을 취소하시겠습니까?', [{key: 'reserveBookNum', value: $this.data('bookNum')}]);
		openPopup('cancel_reservation');
		          		
		
		
	});
	
	
	//책예약 취소
//	$(document)
//	.off('click', '#btnCancelReservation')
//	.on('click', '#btnCancelReservation', function() {
//		var myMemberIdx = $('#dvRentalHistory').data('memberIdx');
//		var $selRentedBook = $('#selRentedBook');
//		
//		if($selRentedBook.val() == '-1') {
//			alert('예약취소하실 도서를 선택하세요');
//			return;
//		}
//		
//		var exists = $('#selReservation option[value="' + myMemberIdx + '"]').length !== 0;
//		
//		if(!exists) {
//			alert('내가 예약한 도서가 아닙니다.');
//			return;
//		}
//		
//		Common.ajax({
//			isSystem: true,
//			url: '/user/reserve/cancel',
//			type: 'POST',
//			headers: {'CUSTOM': 'Y'},
//			data: {
//				reserveBookNum: $selRentedBook.val()
//			},
//			success: function(data, textStatus, jqXHR) {
//				alert('예약취소 되었습니다.')
//				var ds = data.datas;
//				Common.makeReserMembers(ds);
//				
//			},
//		});
//		
//	});
	
	$(document)
	.off('change', '#selRentedBook')
	.on('change', '#selRentedBook', function() {
		var $this = $(this);
		
		if($this.val() == '-1') {
			Common.makeReserMembers([]);
		}
		else {
			Common.ajax({
				isSystem: true,
				url: '/book/reserve/members',
				type: 'POST',
				headers: {'CUSTOM': 'Y'},
				data: {
					reserveBookNum: $this.val()
				},
				success: function(data, textStatus, jqXHR) {
					
					var ds = data.datas;
					Common.makeReserMembers(ds);
					
				},
			});
			
		}
	});
	
	
});

$(document).on('pageshow', function (event, ui) {
    // Remove the previous page
	//$(ui.prevPage).remove();
	$('#popupDialog').popup({history: false});
	$('#bookReservePopup').popup({history: false});
	
    ///book/rental_history
    if($('#dvRentalHistory').get(0)) {
    	var myMemberIdx = $('#dvRentalHistory').data('memberIdx');
    	console.log(myMemberIdx);
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
    				if(data.memberIdx == myMemberIdx) {
    					$(selector).removeClass('apply').addClass('apply_mine');
    					$(selector + 'Image').data('mine', 'Y');
    				}
    				else {
    					$(selector).removeClass('apply_mine').addClass('apply');
    					$(selector + 'Image').data('mine', 'N');
    				}
    				
    				$(selector).text('신청');
    				$(selector + 'Man').text(data.memberName);
    				$(selector + 'Image').data('possible', 'R');
    				$(selector + 'Image').data('rentalMan', data.memberName);
    				
    			}
    			else if(data.type == 'A') {
    				$(selector).removeClass('apply apply_mine');
    				$(selector).addClass('norental');
    				$(selector).text('대여');
    				$(selector + 'Man').text(data.memberName);
    				$(selector + 'Image').data('possible', 'A');
    				$(selector + 'Image').data('rentalMan', data.memberName);
    			}
    			else if(data.type == 'T') { //반납
    				$(selector).removeClass('apply_mine apply norental');
    				$(selector).text('가능');
    				$(selector + 'Man').html('&nbsp;');
    				$(selector + 'Image').data('possible', 'Y');
    				$(selector + 'Image').data('rentalMan', null);
    			}
    			else if(data.type == 'D') {//대여신청 취소
    				$(selector).removeClass('apply_mine apply norental');
    				$(selector).text('가능');
    				$(selector + 'Man').html('&nbsp;');
    				$(selector + 'Image').data('possible', 'Y');
    				$(selector + 'Image').data('rentalMan', null);
    			}
    			
    			Common.useCacheReserPop = false;
    		}
    		catch(e) {
    			console.log(e)
    		}
			
		}
    }
    else if($('#dvSiteInfo').get(0)) {
    	
    	//$('#dvSiteInfo').height(Common.getFullHeight());
    	var swiper = new Swiper('.swiper-container', {
    		pagination: {
    			el: '.swiper-pagination',
    	    },
    	});
    	
    	$('#dvSiteInfo').height(Common.getFullHeight());
    }
});

$(document).on('pageremove', function (event, ui) {
	console.log('pageremove')
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
	if(e.target.id == 'popupDialog') {
		if(Common.popupClose) {
			Common.popupClose();
			Common.popupClose = null;
		}
	}
	else if(e.target.id == 'bookReservePopup') {
		$('#footerReservation').removeClass('ui-disabled');
		$('#footerReservation').data('disabled', false);
	}
	
	$.scrollLock(false);
	
	
    console.log(e.target.id + " -> " + e.type);
});



//$(document).on("panelbeforeopen", "div[data-role='panel']", function(e,ui) {
//    $.mobile.activeClickedLink = null;
//});