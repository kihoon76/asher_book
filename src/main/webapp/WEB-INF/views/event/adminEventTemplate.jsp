<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<content tag="main">
<div id="dvEventDetail" style="width: 100%">
	<div class="article">
		<p><img src="/event/${event.fileName}"></p>
        <h2>${event.title}</h2>
        <p>${event.content}</p>
        <p><small>작성자: ${event.writer}</small></p>
        <c:set var="fileSize" value="${event.fileSize}" />
        <fmt:parseNumber var="parsedFileSize" type="number" value="${fileSize}" />
        <c:if test="${parsedFileSize gt 2000000 }">
       	<p><small>카카오링크 이미지는 2MB이하만 전송됩니다.</small></p>
       	</c:if>
        <p>
        	<a href="#" class="ui-btn ui-btn-inline ui-mini">삭제</a>
        	<a id="btnEventKakaoLink" href="#" class="ui-btn ui-btn-inline ui-mini" data-ajax="false">
        		<img src="//developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_small.png"/>
        	</a>
       	</p>
    </div>
    <c:set var="singlequote" value="'"/>
    <c:set var="singlequoteR" value="\\'"/>
    <c:set var="backslash" value="\\"/>
    <c:set var="backslashR" value="\\\\"/>
    <c:set value="${fn:replace(event.content, backslash, backslashR)}" var="bsContent" />
    <c:set value="${fn:replace(bsContent, singlequote, singlequoteR)}" var="sqContent" />
    <script type="text/javascript">
	    Common.createLinkKakao({
	   		container: '#btnEventKakaoLink',
	   		title: '${event.title}',
	   		imageUrl: 'http://book.asherchurch.net:48080/event/${event.fileName}',
	   		description: '${sqContent}'
	    });
	    
	    //5000000
	</script>
</div>

</content>