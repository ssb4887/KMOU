<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@ include file="../include/common.jsp"%>
<%
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
boolean isAjax = StringUtil.isEquals(request.getHeader("Ajax"), "true");

if(isAjax) {
%>
접근권한이 없습니다.
<%}else{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><c:choose><c:when test="${!empty siteInfo}">${siteInfo.site_title}</c:when><c:otherwise><spring:message code="Globals.site.default.title"/></c:otherwise></c:choose> - 접근권한이 없습니다.</title>
	<script type="text/javascript">
	try{
		if(window != top && window.innerWidth == 0) {
			alert("접근권한이 없습니다.");
		}
	}catch(e){
	}
	</script>
</head>
<body>
	<h1>접근권한이 없습니다.</h1>
	<p>방문하시려는 페이지의 주소가 잘못 입력되었거나,<br />페이지의 주소가 변경되어 요청하신 페이지에 접근하실 수 없습니다.</p>
       <p>입력하신 주소가 정확한지 다시 한번 확인해 주시기 바랍니다.</p>
       <p><button type="button" onclick="history.go(-1);">이전페이지로 돌아가기</button></p>
</body>
</html>
<%} %> 