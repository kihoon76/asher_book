<%@ page import="net.asher.book.domain.Book"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedHashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="net.asher.book.domain.RentalHistory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<div id="dvRentalManage" style="width: 100%">
<%
	Object rmObj = request.getAttribute("rentalMap");
	if(rmObj != null) {
		Map<String, ArrayList<RentalHistory>> rm = (Map<String, ArrayList<RentalHistory>>)rmObj;
		int listCnt = 0;
		ArrayList<RentalHistory> item = null;
%>
<ul data-role="listview" data-split-icon="gear" data-inset="true">
<%	
		for(Map.Entry<String, ArrayList<RentalHistory>> entry : rm.entrySet()) {
			item = entry.getValue();
			listCnt = item.size();
%>
	<li id="rm<%=item.get(0).getRentalManIdx()%>Divider" data-role="list-divider"><%=item.get(0).getRentalMan()%><span id="rm<%=item.get(0).getRentalManIdx()%>Count" class="ui-li-count"><%=listCnt%></span></li>
<%
			for(int x=0; x<listCnt; x++) {
%>
	<li id="rmHis<%=item.get(x).getBookNum()%>">
		<a href="#" data-ajax="false">
			<h2><%=item.get(x).getBookNum() %>.<%=item.get(x).getBookName() %></h2>
			<%if("R".equals(item.get(x).getStatus())) { %>
			<p><strong>대여신청일 : <%=item.get(x).getRentalDate() %></strong></p>
			<%} else { %>
			<p><strong>대여일 : <%=item.get(x).getRentalDate() %></strong></p>
			<p><strong>반납예정일 : <%=item.get(x).getReturnDate() %></strong></p>
			<%} %>
			<p class="ui-li-aside"><strong><%if("R".equals(item.get(x).getStatus())) {%><span style="color: blue;">대여신청중</span><%} else { %> <span style="color: red;">대여중</span><%} %></strong></p>
		</a>
		<a href="#" class="RENTAL_SELECT" data-rel="popup" data-position-to="window" data-transition="pop" data-ajax="false"
		   data-rental-man-idx="<%=item.get(x).getRentalManIdx() %>"
		   data-rental-man="<%=item.get(x).getRentalMan() %>"
		   data-book-name="<%=item.get(x).getBookName() %>"
		   data-book-num="<%=item.get(x).getBookNum() %>"
		   data-status="<%=item.get(x).getStatus() %>"></a>
	</li>
<%				
			}
		}
	}
%>
</ul>
</div>

<!-- <ul data-role="listview" data-inset="true"> -->
<!--     <li data-role="list-divider">Friday, October 8, 2010 <span class="ui-li-count">2</span></li> -->
<!--     <li><a href="index.html"> -->
<!--     <h2>Stephen Weber</h2> -->
<!--     <p><strong>You've been invited to a meeting at Filament Group in Boston, MA</strong></p> -->
<!--     <p>Hey Stephen, if you're available at 10am tomorrow, we've got a meeting with the jQuery team.</p> -->
<!--         <p class="ui-li-aside"><strong>6:24</strong>PM</p> -->
<!--     </a></li> -->
<!--     <li><a href="index.html"> -->
<!--     <h2>jQuery Team</h2> -->
<!--     <p><strong>Boston Conference Planning</strong></p> -->
<!--     <p>In preparation for the upcoming conference in Boston, we need to start gathering a list of sponsors and speakers.</p> -->
<!--         <p class="ui-li-aside"><strong>9:18</strong>AM</p> -->
<!--     </a></li> -->
<!--     <li data-role="list-divider">Thursday, October 7, 2010 <span class="ui-li-count">1</span></li> -->
<!--     <li><a href="index.html"> -->
<!--     <h2>Avery Walker</h2> -->
<!--     <p><strong>Re: Dinner Tonight</strong></p> -->
<!--     <p>Sure, let's plan on meeting at Highland Kitchen at 8:00 tonight. Can't wait!</p> -->
<!--         <p class="ui-li-aside"><strong>4:48</strong>PM</p> -->
<!--     </a></li> -->
<!-- </ul> -->

</content>