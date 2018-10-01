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
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<link rel="stylesheet" href="/resources/jqmobile/jquery.mobile-1.4.5.min.css" />
<!-- 	<link rel="stylesheet" href="/resources/css/jqm-demos.css" /> -->
	<link rel="stylesheet" href="/resources/css/default.css" />

	<script type="text/javascript" src="/resources/jquery/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="/resources/jqmobile/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript" src="/resources/js/main.js"></script>
</head>
<body>
<div data-role="page" id="pg1" data-dom-cache="false">
    <div data-role="header" data-position="fixed"  data-tap-toggle="false" data-theme="a">
    	<h1></h1>
    	<a href="#leftpanel1" data-icon="bars" data-iconpos="notext" data-shadow="false" data-iconshadow="false">Menu</a>
    </div>
    <div id="dvMain" role="main" class="ui-content" data-theme="a">
        <sitemesh:write property="page.main" />
    </div>
    <div data-role="footer" data-position="fixed" data-tap-toggle="false" data-theme="b">
         <h1>Footer</h1>
    </div>
    
    <!-- leftpanel3  -->
	<div data-role="panel" id="leftpanel1" data-position="left" data-display="reveal" data-theme="c">
		<ul data-role="listview" data-theme="d">
        	<li data-icon="delete"><a href="#" data-rel="close">Close</a></li>
        	<li data-role="list-divider">Menu</li>
        	<li ><a href="/book/1">대여현황</a></li>
        </ul>

        <div data-role="collapsible" data-inset="false" data-iconpos="right" data-theme="d" data-content-theme="b" <c:if test="${not empty selectedBook}">data-collapsed="false"</c:if>>
              <h3>도서목록</h3>
              <ul data-role="listview">
<!--                 <li><a href="/book/1" class="ui-btn ui-btn-icon-right ui-icon-carat-r ui-btn-active">1.</a></li> -->
				<c:forEach items="${bookList}" var="book">
					<li><a href="/book/<c:out value='${book.bookNum}' />">${book.bookNum}. ${book.bookName}</a></li>
				</c:forEach>
<!-- 				<li><a href="/book/1">1.</a></li> -->
<!--                 <li><a href="/book/2">2.</a></li> -->
<!--                 <li><a href="/book/3">3. </a></li> -->
<!--                 <li><a href="/book/4">4. 하나님의 영광</a></li> -->
<!--                 <li><a href="/book/5">5. 치유를 유지하는 법</a></li> -->
<!--                 <li><a href="/book/6">6. 위대한 세단어</a></li> -->
<!--                 <li><a href="/book/7">7. 형통의 계시</a></li> -->
<!--                 <li><a href="/book/8">8. </a></li> -->
<!--                 <li><a href="/book/9">9. 그 이름은 놀라우신 분</a></li> -->
<!--                 <li><a href="/book/10">10. 하나님의 영광</a></li> -->
<!--                 <li><a href="/book/11">11. 은혜안에서의 성장을 방해하는 다섯가지</a></li> -->
<!--                 <li><a href="/book/12">12. 셀모임에서 성령인도 받기</a></li> -->
<!--                 <li><a href="/book/13">13. 재정분야의 순종</a></li> -->
<!--                 <li><a href="/book/14">14. 네 주장을 변론하라</a></li> -->
<!--                 <li><a href="/book/15">15. 사랑가운데 걷는법</a></li> -->
<!--                 <li><a href="/book/16">16. 바울의 계시:화해의 복음</a></li> -->
<!--                 <li><a href="/book/17">17. 방언기도의 능력을 풀어 놓으라</a></li> -->
<!--                 <li><a href="/book/18">18. 그리스도 안에서</a></li> -->
<!--                 <li><a href="/book/19">19. 사랑은 결코 실패하지 않습니다</a></li> -->
<!--                 <li><a href="/book/20">20. 하나님을 탓하지 마십시오</a></li> -->
<!--                 <li><a href="/book/21">21. 잊어버리는 법을 배우기</a></li> -->
<!--                 <li><a href="/book/22">22. 믿음이란 무엇인가?</a></li> -->
<!--                 <li><a href="/book/23">23. 네 염려를 주께 맡겨라</a></li> -->
<!--                 <li><a href="/book/24">24. 나는 지옥에 갔다 왔습니다</a></li> -->
<!--                 <li><a href="/book/25">25. </a></li> -->
<!--                 <li><a href="/book/26">26. 당신은 당신이 말하는 것을 가질 수 있습니다</a></li> -->
<!--                 <li><a href="/book/27">27. 다가오는 회복</a></li> -->
<!--                 <li><a href="/book/28">28. 당신의 믿음을 풀어놓는 법</a></li> -->
<!--                 <li><a href="/book/29">29. 말 </a></li> -->
<!--                 <li><a href="/book/30">30. 안수 </a></li> -->
<!--                 <li><a href="/book/31">31. 절망적인 상황을 반전시키기 </a></li> -->
<!--                 <li><a href="/book/32">32. 성령을 받는 성경적인 방법</a></li> -->
<!--                 <li><a href="/book/33">33. 새로운 탄생 </a></li> -->
<!--                 <li><a href="/book/34">34. 진짜믿음</a></li> -->
              </ul>
        </div><!-- /collapsible -->
	</div><!-- /leftpanel3 -->
</div>
<!-- $(":mobile-pagecontainer").pagecontainer("change", "#page", { options }); -->

</body>
</html>

</html>