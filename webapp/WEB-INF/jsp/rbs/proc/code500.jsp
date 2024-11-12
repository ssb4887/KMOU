<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@ include file="../include/common.jsp"%>
<%
	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
boolean isAjax = StringUtil.isEquals(request.getHeader("Ajax"), "true");

if(isAjax) {
%>
내부오류입니다. 담당자에게 문의하세요.
<%}else{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><c:choose><c:when test="${!empty siteInfo}">${siteInfo.site_title}</c:when><c:otherwise><spring:message code="Globals.site.default.title"/></c:otherwise></c:choose> - 페이지를 찾을 수 없습니다.</title>
	<script type="text/javascript">
	try{
		if(window != top && window.innerWidth == 0) {
			alert("요청하신 페이지를 찾을 수 없습니다.");
		}
	}catch(e){}
	</script>
</head>
<body>
	<h1>내부오류입니다.</h1>
	<p>담당자에게 문의하세요.</p>
       <p><button type="button" onclick="history.go(-1);">이전페이지로 돌아가기</button></p>
</body>
</html>
<%} %> 