<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<div id="dvEventList" style="width: 100%">
<c:choose>
<c:when test="${not empty eventList}">
	<div>이벤트 리스트</div>
	<input type="search" id="eventSearch" value=""  />
	<ul data-role="listview" data-inset="true">
	<c:forEach var="event" items="${eventList}">
		<li>
			<a href="/admin/event/item/${event.suffix}">
        		<img style="width:100px; height:100px;" src="/event/${event.fileName}">
    			<h2>${event.title}</h2>
    			<p>작성자: ${event.writer}</p>
    			<p>${event.content}</p>
        	</a>
    	</li>
	</c:forEach>
	</ul>
</c:when>
<c:otherwise></c:otherwise>
</c:choose>
</div>
</content>