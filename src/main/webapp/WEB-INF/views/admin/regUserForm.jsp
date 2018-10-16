<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<form method="post" id="regUserForm" data-ajax="false">
<label for="userId">아이디 : <span>*</span></label>
<input type="text" name="userId" id="userId" placeholder="아이디">
<label for="userName">한글명 : <span>*</span></label>
<input type="text" name="userName" id="userName" placeholder="사용자명">
<label for="userPhone">전화번호 : <span>*</span></label>
<input type="text" name="userId" id="userPhone" placeholder="예) 010-1111-1111">
<label for="userEmail">이메일: <span>*</span></label>
<input type="email" id="userEmail" name="userEmail" placeholder="이메일"/>
<input type="button" data-inline="true" value="등록" data-theme="b" id="btnRegUser">
</form>
</content>