<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="req" value="${pageContext.request}" />
<c:set var="url">${req.requestURL}</c:set>
<c:set var="uri" value="${req.requestURI}" />
<!DOCTYPE html>
<!--[if IE 8]><html class="ie8" lang="ko"><![endif]-->
<!--[if IE 9]><html class="ie9" lang="ko"><![endif]-->
<!--[if gt IE 9]><!--><html lang="ko"><!--<![endif]-->
<html>
<head>
	<title>Home</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimun-scale=1.0, user-scalable=0" />
	<!-- 안드로이드 홈화면추가시 상단 주소창 제거 -->
	<meta name="mobile-web-app-capable" content="yes">
	<!-- ios홈화면추가시 상단 주소창 제거 -->
	<meta name="apple-mobile-web-app-capable" content="yes">

	
	<link rel="stylesheet" href="/resources/jqmobile/jquery.mobile-1.4.5.min.css" />
	<link rel="stylesheet" href="/resources/swiper-4.4.1/css/swiper.min.css" />
<!-- 	<link rel="stylesheet" href="/resources/css/jqm-demos.css" /> -->
	<link rel="stylesheet" href="/resources/css/default.css" />
	<link rel="stylesheet" href="/resources/css/column.css" />
	<link rel="stylesheet" href="/resources/css/info.css" />
	<link rel="stylesheet" href="/resources/css/fileupload.css" />
	
	<script type="text/javascript" src="/resources/jquery/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="/resources/jqmobile/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="/resources/js/fastclick.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.scrollLock.js"></script>
	<script type="text/javascript" src="/resources/js/sockjs.min.js"></script>
	<script type="text/javascript" src="/resources/swiper-4.4.1/js/swiper.min.js"></script>
	<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
	<script type="text/javascript" src="/resources/js/main.js"></script>
</head>
<body onload="initFastButtons();">
<div data-role="popup" id="popupDialog" data-overlay-theme="b" data-theme="b" data-dismissible="false" style="max-width: 400px;" data-transition="none">
	<div id="popupHeader" data-role="header" data-theme="a"></div>
	<div role="main" class="ui-content">
		<div id="dvPopupContent"></div>
		<a href="#" data-role="button" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b" id="btnPopupOk" rel="external"></a>
		<a href="#" data-role="button" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b" id="btnPopupEtc" rel="external" style="display:none;"></a>
		<a href="#" data-rel="back" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-btn-b" id="btnPopupCancel"></a>
	</div>
</div>

<div id="bookReservePopup" data-role="popup" data-theme="a" class="ui-corner-all" style="display:none;  max-width: 400px;">
	<div  data-role="header" data-theme="a"></div>
	<div role="main" class="ui-content">
		<form>
    		<div style="padding:10px 20px;" class="ui-field-contain" >
    			<label for="selRentedBook">대여도서</label>
    			<select id="selRentedBook" data-mini="true"></select>
    			<button class="ui-btn ui-btn-b ui-mini" data-rel="back" style="margin-top: 10px;">예약하기</button>
    			<button class="ui-btn ui-btn-b ui-mini" data-rel="back" style="margin-top: 10px;">돌아가기</button>
    			<div style="margin:10px 0 5px 0;">예약하신 분</div>
    			<ol data-role="listview" data-inset="true">
<!-- 					<li>The Godfather</li> -->
<!-- 					<li>Inception</li> -->
<!-- 					<li>The Good, the Bad and the Ugly </li> -->
<!-- 					<li>Pulp Fiction</li> -->
<!-- 					<li>Schindler's List</li> -->
				</ol>
    		</div>
    	</form>
	</div>
</div>

<span id="fastclick">
<div data-role="page" id="pg1" data-dom-cache="false">
    <div data-role="header" data-position="fixed"  data-tap-toggle="false" data-theme="a">
    	<h1></h1>
    	<a href="#leftpanel1" data-icon="bars" data-iconpos="notext" data-shadow="false" data-iconshadow="false">Menu</a>
    </div>
    <div id="dvMain" role="main" class="ui-content" data-theme="a">
        <sitemesh:write property="page.main" />
    </div>
    <!-- footer  -->
    <div data-role="footer" data-position="fixed" data-tap-toggle="false" data-theme="b">
         <div data-role="navbar">
         	<ul>
         		<li><a id="footerHome" href="/main" data-icon="home" <c:if test="${'home' eq footbar}">class="ui-btn-active"</c:if>>홈</a></li>
         		<li><a href="/info" data-icon="info" <c:if test="${'info' eq footbar}">class="ui-btn-active"</c:if>>사용안내</a></li>
         		<c:choose>
         		<c:when test="${'event' eq footbar}">
         		<li><a id="btnRegEvent" href="#" data-icon="action" data-ajax="false">이벤트등록</a></li>
         		</c:when>
         		<c:when test="${'Y' eq back}">
         		<li><a id="footerPrevious" href="#" data-icon="back" data-ajax="false">이전</a></li>
         		</c:when>
         		<c:when test="${'reservation' eq footbar}">
         		<li><a id="footerReservation" href="#" data-icon="star" data-ajax="false" data-role="none">예약하기</a></li>
         		</c:when>
         		</c:choose>
         		<li><a id="footerLogout" href="#" data-icon="user" data-ajax="false">로그아웃</a></li>
         	</ul>
         </div>
    </div>
    
    <!-- leftpanel3  -->
	<div data-role="panel" id="leftpanel1" data-position="left" data-display="reveal" data-theme="c">
		<ul data-role="listview" data-theme="d">
        	<li data-icon="delete"><a href="#" data-rel="close">Close</a></li>
        	<li data-role="list-divider">Menu</li>
        	<li><a href="/book/rental_history">대여현황</a></li>
        	<sec:authorize access="hasRole('ROLE_ADMIN')">
        	<li><a href="/book/rental_manage">대여관리</a></li>
        	</sec:authorize>
        </ul>

        <div data-role="collapsible" data-inset="false" data-iconpos="right" data-theme="d" data-content-theme="b" <c:if test="${not empty selectedBook}">data-collapsed="false"</c:if>>
              <h3>도서목록</h3>
              <ul data-role="listview">
<!--                 <li><a href="/book/1" class="ui-btn ui-btn-icon-right ui-icon-carat-r ui-btn-active">1.</a></li> -->
				<c:forEach items="${bookList}" var="book">
					<li><a href="/book/<c:out value='${book.bookNum}' />">${book.bookNum}. ${book.bookName}</a></li>
				</c:forEach>
              </ul>
        </div><!-- /collapsible -->
        
        <sec:authorize access="hasRole('ROLE_ADMIN')">
        <div data-role="collapsible" data-inset="false" data-iconpos="right" data-theme="d" data-content-theme="b">
              <h3>회원관리</h3>
              <ul data-role="listview">
              	<li><a href="/admin/reg/user_form" class="ui-btn ui-btn-icon-right ui-icon-carat-r">회원등록</a></li>
              	<li><a href="/admin/list/user" class="ui-btn ui-btn-icon-right ui-icon-carat-r">회원목록</a></li>
              </ul>
        </div><!-- /collapsible -->
        
        <div data-role="collapsible" data-inset="false" data-iconpos="right" data-theme="d" data-content-theme="b">
              <h3>sms관리</h3>
              <ul data-role="listview">
              	<li><a href="/admin/sms/remain" class="ui-btn ui-btn-icon-right ui-icon-carat-r">발송가능 건수</a></li>
              </ul>
        </div><!-- /collapsible -->
        
        <div data-role="collapsible" data-inset="false" data-iconpos="right" data-theme="d" data-content-theme="b">
              <h3>이벤트관리</h3>
              <ul data-role="listview">
              	<li><a href="/admin/event/reg_form" class="ui-btn ui-btn-icon-right ui-icon-carat-r">이벤트등록</a></li>
              	<li><a href="/admin/event/list" class="ui-btn ui-btn-icon-right ui-icon-carat-r">이벤트목록</a></li>
              </ul>
        </div><!-- /collapsible -->
        </sec:authorize>
        
	</div><!-- /leftpanel3 -->
	
	
</div>
</span> <!-- end fastclick -->
<!-- $(":mobile-pagecontainer").pagecontainer("change", "#page", { options }); -->
</body>
</html>
