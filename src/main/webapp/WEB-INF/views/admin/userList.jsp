<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="net.asher.book.domain.Account"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<div id="dvUserList" style="width: 100%">
<%
	Object userObj = request.getAttribute("userList");
	if(userObj != null) {
		List<Account> userList = (ArrayList<Account>)userObj;
		int listCnt = userList.size();
%>
<ul data-role="listview" data-split-icon="gear" data-inset="true">
<%	
		for(int u=0; u<listCnt; u++) {
			System.out.println("111111---------");
%>
	<li id="userIdx<%=userList.get(u).getIdx()%>Divider" data-role="list-divider"><%=userList.get(u).getUserName()%><span class="ui-li-count"><%=userList.get(u).getIdx()%></span></li>
	<li>
		<a href="#" data-ajax="false">
			<h2><%=userList.get(u).getId() %></h2>
			<p><strong>전화번호 : <%=userList.get(u).getPhone() %></strong></p>
			<p><strong>이메일 : <%=userList.get(u).getEmail() %></strong></p>
			<p class="ui-li-aside"><strong><%if("Y".equals(userList.get(u).getIsAdmin())) {%><span style="color: red;">관리자</span><%} else { %> &nbsp;<%} %></strong></p>
		</a>
		<a href="#" class="" data-rel="popup" data-position-to="window" data-transition="pop" data-ajax="false"></a>
	</li>
<%				
		}
%>
</ul>
<%
	}
%>
</div>
</content>