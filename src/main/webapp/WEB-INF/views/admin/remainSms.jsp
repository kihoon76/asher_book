<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<content tag="main">
<div id="dvSmsRemain" style="width: 100%">
	<div>남은 문자건수</div>
	<table>
		<tr>
			<td>SMS</td>
			<td>LMS</td>
			<td>MMS</td>
		</tr>
		<c:choose>
		<c:when test="${info['result_code'] eq '1.0' or info['result_code'] eq '1'}">
		<tr>
			<td><c:out value="${fn:split(info['SMS_CNT'], '.')[0]}" /> 건</td>
			<td><c:out value="${fn:split(info['LMS_CNT'], '.')[0]}" /> 건</td>
			<td><c:out value="${fn:split(info['MMS_CNT'], '.')[0]}" /> 건</td>
		</tr>
		</c:when>
		<c:otherwise>
		<tr>
			<td colspan="3"><c:out value="${info['result_code']}" /></td>
		</tr>
		</c:otherwise>
		</c:choose>
	</table>
</div>
</content>