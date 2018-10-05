<%@ page import="net.asher.book.domain.Book"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<div id="dvRentalHistory" style="width: 100%">
	<%
		List<Book> list = (List<Book>)request.getAttribute("bookList");
		int listCnt = list.size();
		if(listCnt > 0) {
				
			for(int i=0; i<listCnt; i++) {
				if(i % 3 == 0) {
					if(i != 0) {
	%>
	</div>
	<%				
					}
	%>
	<div class="row">
	<%			
				}
	%>
	<div class="column">
		<div>
			<a href="#" class="IMG_POPUP" data-rel="popup" data-position-to="window" data-transition="pop" data-ajax="false">
				<img style="width: 100%; height: 130px;" src="/resources/img/book<%=list.get(i).getBookNum() %>.jpg" 
				data-name="<%=list.get(i).getBookName()%>"
				data-num="<%=list.get(i).getBookNum() %>">
			</a>
		</div>
		<div>
			<table>
				<tr>
					<td><%=list.get(i).getBookNum()%></td>
					<%
						if("N".equals(list.get(i).getRentalPossible())) {
					%>
					<td style="background: red; color: #fff;">불가</td>
					<%		
						}
						else {
					%>
					<td>가능</td>
					<%		
						}
					%>
				</tr>
				<tr>
					<%
						if(list.get(i).getRentalMan() != null) {
					%>
					<td colspan="2"><%=list.get(i).getRentalMan() %></td>
					<%		
						}
						else {
					%>
					<td colspan="2">&nbsp;</td>
					<%		
						}
					%>
				</tr>
			</table>
		</div>
	</div>
	<%
			}
			out.println("</div>");
		}
		else {
	%>
	<span>책목록이 존재하지 않습니다.</span>
	<%
		}
	%>
</div>
</content>