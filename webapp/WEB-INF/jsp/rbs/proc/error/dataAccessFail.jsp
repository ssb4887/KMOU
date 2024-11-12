<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@ include file="../../include/common.jsp"%>
<%
response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
boolean isAjax = StringUtil.isEquals(request.getHeader("Ajax"), "true");

if(isAjax) {
%>
데이터 처리 중 오류가 발생하였습니다.
<%}else{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><c:choose><c:when test="${!empty siteInfo}"><c:out value="${siteInfo.site_title}"/></c:when><c:otherwise><spring:message code="Globals.site.default.title"/></c:otherwise></c:choose> - 오류가 발생했습니다.</title>
	<script type="text/javascript">
	try{
		if(window != top && window.innerWidth == 0) {
			alert("데이터 처리 중 오류가 발생하였습니다.<c:if test="${!empty errorCode}"> (${errorCode})</c:if>");
		}
	}catch(e){}
	</script>
</head>
<body>
	<h1>오류가 발생했습니다.<c:if test="${!empty errorCode}"> (${errorCode})</c:if></h1>
	<p><c:choose><c:when test="${!empty errorCode}"><spring:message code="errors.message.${errorCode}"/></c:when><c:otherwise>데이터 처리 중 오류가 발생하였습니다.</c:otherwise></c:choose></p>
	<p><button type="button" onclick="history.go(-1);">이전페이지로 돌아가기</button></p>
</body>
</html>
<%}%>