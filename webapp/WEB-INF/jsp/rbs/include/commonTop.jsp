<%@page import="com.woowonsoft.egovframework.util.MenuUtil"%>
<!-- <link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/> -->
<%@ include file="common.jsp" %>
<%
response.setHeader("Cache-Control", "must-revalidate");
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-store");
response.setDateHeader("Expires", 0);
if(request.getProtocol().equals("HTTP/1.1")){
	response.setHeader("Cache-Control", "no-cache");
}
%>
<spring:message var="rootJspPath" code="Globals.root.jsp.path"/>
<spring:message var="rbsJspPath" code="Globals.rbs.jsp.path"/>
<c:set var="jspRbsPath" value="${rootJspPath}${rbsJspPath}"/>
<c:set var="siteLocalPath" value="${siteInfo.local_path}"/>
<c:choose>
	<c:when test="${isAdmMode}">
		<c:set var="siteJSPPath" value="${jspRbsPath}/${siteMode}"/>
	</c:when>
	<c:otherwise>
		<c:set var="siteJSPPath" value="${jspRbsPath}/${siteMode}${siteLocalPath}"/>
	</c:otherwise>
</c:choose>
<c:set var="jspSiteIncPath" value="${siteJSPPath}/include"/>
<c:set var="jsPath" value="${siteLocalPath}/js"/>
<c:set var="jsAssetPath" value="${siteLocalPath}/assets/js"/>
<c:set var="cssPath" value="${siteLocalPath}/css"/>
<c:set var="cssAssetPath" value="${siteLocalPath}/assets/css"/>
<c:set var="imgPath" value="${siteLocalPath}/images"/>
<c:set var="imgPath2" value="${siteLocalPath}/img"/>
<c:set var="imgAssetPath" value="${siteLocalPath}/assets/images"/>
<c:set var="moduleImgPath" value="${moduleResourcePath}/images"/>
<c:set var="indexUrl" value="<%=MenuUtil.getViewRedirectMenuUrl(1) %>"/>
<spring:message var="useLocaleLang" code="Globals.locale.lang.use"/>
<%/* 선택된 사용자 사이트 정보 */%>
<c:set var="usrSiteVO" value="${elfn:usrSiteInfo()}"/>

