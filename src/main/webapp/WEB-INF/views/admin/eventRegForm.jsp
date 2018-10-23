<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<content tag="main">
<form method="post" id="eventRegForm" enctype="multipart/form-data" data-ajax="false">
<label for="eventTitle">이벤트 제목 : <span class="required">*</span></label>
<input type="text" name="eventTitle" id="eventTitle" placeholder="이벤트제목">
<label for="eventContent">이벤트 내용 : <span class="required">*</span></label>
<textarea id="eventContent" class="custom_class"></textarea>
<button id="chooseFile">파일선택</button>
<div class="hiddenfile">
	<input type="file"  id="eventImage" data-clear-btn="false" name="image" accept="image/*" capture>
</div>
<div id="preview"></div>
<ul id="info" data-role="listview" data-inset="true"></ul>


</form>
</content>