<%@ include file="commonTop.jsp" %>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/usr" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<!-- 기존 css 및 js -->
	<%@ include file="commonHead.jsp" %>
	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/jquery.orbit.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/main.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/common.css"/>"/>
	<script type="text/javascript">
	var gVarM = true;
	</script>
	<%@ include file="../../../include/contact.jsp"%>
</head>
<body>

<!-- skip navi -->
<!-- <ul id="skipNavi">
	<li><a href="#gnb">주메뉴 바로가기</a></li>
	<li><a href="#mainContainer">본문내용 바로가기</a></li>
</ul> -->
<!-- //skip navi -->

<div class="header_wrap position-fixed start-50 translate-middle-x w-100">
<%@ include file="header.jsp" %>
</div>
<!-- container -->
	<!-- <div id="mainContainer">
		<div id="mainContents"> -->
		
	<div class="container_wrap">
        <div class="main_wrap">
