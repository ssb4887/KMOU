<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@ include file="../include/common.jsp"%>
<%
boolean isAjax = StringUtil.isEquals(request.getHeader("Ajax"), "true");
if(isAjax) {
%>
${message}
top.fn_removeMaskLayer();
<%}else{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" /> 
	<title><c:choose><c:when test="${!empty siteInfo}">${siteInfo.site_title}<c:if test="${!empty crtMenu.menu_name}">-${crtMenu.menu_name}</c:if></c:when><c:otherwise><spring:message code="Globals.site.default.title"/></c:otherwise></c:choose></title>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-1.9.1.min.js"/>"></script>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/checkForm2.js?cp=${pageContext.request.contextPath}&lg=${siteInfo.locale_lang}"/>"></script>
	<noscript><spring:message code="message.no.script"/></noscript>
</head>
<body>
${message}
<script type="text/javascript">
$(function(){
	top.fn_removeMaskLayer();
});
</script>
</body>
</html>
<%} %> 