<%@ include file="commonTop.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<%@ include file="commonHead.jsp" %>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/login.css"/>"/>
	<script type="text/javascript">
	var gVarM = true;
	</script>
	<%@ include file="../../include/contact.jsp"%>
</head>
<body>
<div id="wrapper">
	<!-- container -->
	<div id="container">
		<div id="contentsWrap">
			<div class="loginTit">
				<h4 class="tit"><img src='<c:out value="${contextPath}${imgPath}/layout/logo2_temp.svg"/>' alt="국립한국해양대학교">
				<p>지능형 교과·비교과 검색 추천 시스템 구축 관리자시스템</p>
				</h4>
<%-- 				<h4 class="tit"><img src='<c:out value="${contextPath}${imgPath}/layout/logo2_temp.svg"/>' alt="R.bis [RBS J]gCMS v4.0 standard edition"/></h4> --%>
			</div>
