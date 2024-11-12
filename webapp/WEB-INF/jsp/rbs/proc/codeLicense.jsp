<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@ include file="../include/common.jsp"%>
<%
response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
boolean isAjax = StringUtil.isEquals(request.getHeader("Ajax"), "true");

if(isAjax) {
%>
인증되지 않은 서버입니다.
<%}else{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><c:choose><c:when test="${!empty siteInfo}">${siteInfo.site_title}</c:when><c:otherwise><spring:message code="Globals.site.default.title"/></c:otherwise></c:choose> - 인증되지 않은 서버입니다.</title>
	<script type="text/javascript">
	try{
		if(window != top && window.innerWidth == 0) {
			alert("인증되지 않은 서버입니다.\n관리자에게 문의하시기 바랍니다.");
		}
	}catch(e){}
	</script>
</head>
<body>
	<h1>인증되지 않은 서버입니다.</h1>
	<p>인증되지 않은 서버입니다.<br />관리자에게 문의하시기 바랍니다.</p>
</body>
</html>
<%} %> 