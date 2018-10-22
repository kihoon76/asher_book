<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<div id="dvBookInfo" style="width: 100%">
	<ul data-role="listview" data-inset="true">
		<li data-role="list-divider"><c:out value="${bookInfo.bookName}" /> <span class="ui-li-count">No.<c:out value="${bookNum}" /></span></li>
		<li>
			<img src="/resources/img/book<c:out value='${bookNum}' />.jpg">
			<p style="color: red;"><c:out value="${bookInfo.bookEngName }" /></p>
			<h2>목차</h2>
    		<c:out value="${bookInfo.bookContents}" escapeXml="false"/>
		</li>
	</ul>
	<div><a href="<c:out value='${bookInfo.bookLink}' />" target="_blank">상세정보</a></div>
	<div style="font-size:.8em;margin-top:5px;">위 도서를 읽으신 분</div>
	<table>
	<c:choose>
	<c:when test="${not empty readMembers}">
	<c:forEach items="${readMembers}" var="members">
	<tr>
		<td>${members.userName}</td>
		<td>${members.id}</td>
	</tr>
	</c:forEach>
	</c:when>
	<c:otherwise>
		<tr>
			<td colspan="2">읽으신 성도님이 없습니다.</td>
		</tr>
	</c:otherwise>	
	</c:choose>
	</table>
</div>
</content>