<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<div>
<!-- 	<img style="width: 100%;" src="/resources/img/Corinthians2_9.jpg" /> -->
	<div> <c:out value="${memberName}" />님의 대여현황</div>
	<table>
		<tr>
			<td>도서명</td>
			<td>대여일</td>
			<td>반납예정일</td>
		</tr>
		<c:choose>
		<c:when test="${not empty myRentalList}">
		<c:forEach var="list" items="${myRentalList}">
		<tr>
			<td>${list.bookNum}.${list.bookName}</td>
			<td>${list.rentalDate}</td>
			<td>${list.returnDate}</td>
		</tr>
		</c:forEach>
		</c:when>
		<c:otherwise>
		<tr>
			<td colspan="3" style="text-align: center;">대여중인 도서가 없습니다.</td>
		</tr>
		</c:otherwise>
		</c:choose>
	</table>
	<div style="padding-top: 5px;">나의 예약도서 </div>
	<table>
		<tr>
			<td>도서명</td>
			<td>예약순위(나/전체)</td>
			<td>취소</td>
		</tr>
		<c:choose>
		<c:when test="${not empty myReservationList}">
		<c:forEach var="reservation" items="${myReservationList}">
		<tr>
			<td>${reservation.bookNum}.${reservation.bookName}</td>
			<td><span style="color:red;">${reservation.orderNum}</span>/${reservation.total}</td>
			<td><a class="DEL-RESERVE" href="#" 
				   data-role="button"
				   data-icon="delete"
				   data-iconpos="notext"
				   data-book-num="${reservation.bookNum}">
				 </a>
			</td>
		</tr>
		</c:forEach>
		</c:when>
		<c:otherwise>
		<tr>
			<td colspan="3" style="text-align: center;">예약 도서가 없습니다.</td>
		</tr>
		</c:otherwise>
		</c:choose>
	</table>
	<div style="padding-top: 5px;"> 대여내역 </div>
	<table>
		<tr>
			<td>도서명</td>
			<td>대여일</td>
			<td>반납일</td>
			<td>연체</td>
		</tr>
		<c:choose>
		<c:when test="${not empty myRentalHistoryList}">
		<c:forEach var="history" items="${myRentalHistoryList}">
		<tr>
			<td>${history.bookNum}.${history.bookName}</td>
			<td>${history.rentalDate}</td>
			<td>${history.returnDate}</td>
			<td>${history.returnExpired}</td>
		</tr>
		</c:forEach>
		</c:when>
		<c:otherwise>
		<tr>
			<td colspan="4" style="text-align: center;">대여내역이 없습니다.</td>
		</tr>
		</c:otherwise>
		</c:choose>
	</table>
</div>
</content>