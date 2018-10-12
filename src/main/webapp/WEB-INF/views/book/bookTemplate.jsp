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
</div>
</content>