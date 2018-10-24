<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<content tag="main">
<div id="dvEventList" style="width: 100%">
	<div>이벤트 리스트  <input type="button" data-inline="true" value="전체보기" data-mini="true" onclick="window.location.href='/admin/event/list';"></div>
<c:choose>
<c:when test="${not empty eventList}">
	<input type="search" id="eventSearch" value="${search}"  />
	<ul data-role="listview" data-inset="true">
	<c:forEach var="event" items="${eventList}">
		<li>
			<a href="/admin/event/item/${event.suffix}">
        		<img style="width:100px; height:100px;" src="/event/${event.fileName}">
    			<h2>${event.title}</h2>
    			<p>작성자: ${event.writer}</p>
    			<c:set value="${fn:replace(event.content, '<br/>', ' ')}" var="exBR" />
    			<p>${exBR}</p>
        	</a>
    	</li>
	</c:forEach>
	</ul>
</c:when>
<c:otherwise>
<div style="">검색결과가 없습니다.</div>
</c:otherwise>
</c:choose>
</div>
</content>