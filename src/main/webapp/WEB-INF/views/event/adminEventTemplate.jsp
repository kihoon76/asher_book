<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<div id="dvEventDetail" style="width: 100%">
	<div class="article">
		<p><img src="/event/${event.fileName}" alt="Fixed Gear bike"></p>
        <h2>${event.title}</h2>
        <p>${event.content}</p>
        <p><small>작성자: ${event.writer}</small></p>
        <p>
        	<a href="#" class="ui-btn ui-btn-inline ui-mini">삭제</a>
        	<a id="btnEventKakaoLink" href="#" class="ui-btn ui-btn-inline ui-mini" data-ajax="false">
        		<img src="//developers.kakao.com/assets/img/about/logos/kakaolink/kakaolink_btn_small.png"/>
        	</a>
       	</p>
    </div>
    <script type="text/javascript">
	    Common.createLinkKakao({
	   		container: '#btnEventKakaoLink',
	   		title: '${event.title}',
	   		imageUrl: 'http://book.asherchurch.net:48080/event/${event.fileName}',
	   		description: '${event.content}'
	    });
	</script>
</div>

</content>