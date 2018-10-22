<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"	uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if IE 8]><html class="ie8" lang="ko"><![endif]-->
<!--[if IE 9]><html class="ie9" lang="ko"><![endif]-->
<!--[if gt IE 9]><!--><html lang="ko"><!--<![endif]-->
<head>
	<title>아셀교회</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimun-scale=1.0, user-scalable=no" />
	<!-- 안드로이드 홈화면추가시 상단 주소창 제거 -->
	<meta name="mobile-web-app-capable" content="yes">
	<!-- ios홈화면추가시 상단 주소창 제거 -->
	<meta name="apple-mobile-web-app-capable" content="yes">
	<script type="text/javascript" src="/resources/jquery/jquery-1.11.1.min.js"></script>
	<script type="text/javascript" src="/resources/jqmobile/jquery.mobile-1.4.5.min.js"></script>
</head>
<body>
<div data-role="page" id="pgSession" data-dom-cache="false" data-theme="a">
    <div data-role="content">
    	<div style="position:relative;z-index:1;background:#f9f9f9;max-width:300px;margin:0 auto 100px;padding:30px;border-top-left-radius:3px;border-top-right-radius:3px;border-bottom-left-radius:3px;border-bottom-right-radius:3px;text-align:center;">
  			<div style="background:#fff;width:150px;height:150px;margin:0 auto 30px;padding:12px 30px;border-top-left-radius:100%;border-top-right-radius:100%;border-bottom-left-radius:100%;border-bottom-right-radius:100%;box-sizing: border-box;"><img  src="/resources/img/logo.jpg"/></div>
  			<form>
    			<div style="line-height:2em;">세션이 만료되었습니다.<br/>다시로그인해 주세요</div>
    			<button type="button" onClick="window.location.href='/signin'">로그인 화면으로 가기</button>
  			</form>
		</div>
    </div>
</div>
</body>
</html>

