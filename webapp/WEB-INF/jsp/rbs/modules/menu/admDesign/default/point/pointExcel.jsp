<%@page import="com.woowonsoft.egovframework.form.DataForm"%>
<%@ page language="java" contentType="application/vnd.ms-excel; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="../../../../../include/commonTop.jsp"%>
<spring:message var="msgItemCntName" code="item.contact.member"/>
<spring:message var="msgItemLabelName" code="item.menu.point.person.name"/>
<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.woowonsoft.egovframework.util.WebsiteDetailsHelper"%>
<%@page import="rbs.egovframework.WebsiteVO"%>
<%@page import="com.woowonsoft.egovframework.util.JSONObjectUtil"%>
<%@page import="net.sf.json.JSONObject"%>
<%
String titleName = null;
String userAgent = request.getHeader("User-Agent");
JSONObject crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute("crtMenu"));
String menuName = JSONObjectUtil.getString(crtMenu, "menu_name");
WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
titleName = usrSiteVO.getSiteName() + " " + menuName;
String fileName = titleName + ".xls";
titleName = StringUtil.replaceHtmlN(titleName);
boolean ie = userAgent.indexOf("MSIE") > -1;
if(ie) {
	fileName = URLEncoder.encode(fileName, "utf-8");
	if(!StringUtil.isEmpty(fileName)) fileName = fileName.replaceAll("[+]", "%20");
} else {
	fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
}

response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
response.setHeader("Content-Transfer-Encoding", "binary");
%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
	<title><%=titleName%></title>
	<meta http-equiv="Content-Type" content="application/vnd.ms-excel?charset=utf-8">
	<style>
	<!--
	table
		{mso-displayed-decimal-separator:"\.";
		mso-displayed-thousand-separator:"\,";}
	@page
		{margin:.75in .71in .75in .71in;
		mso-header-margin:.31in;
		mso-footer-margin:.31in;
		mso-page-orientation:landscape;}
	
	body {
		font-family:"맑은 고딕";
		color:black;
	}
	
	caption
	{
		text-align            : center;
		font-size             : 12.0pt;
		font-weight			:bold;
		padding               : 10px 0;
		height:50px;
	}
	
	table
	{
		border               :.5pt solid #000;
		border-collapse      : collapse;
	 	text-align			 : center; 
		font-size             : 9.0pt;
	}
	
	thead th
	{
		background            : #DBE5F1;
		padding               : 5px 10px;
		border-right:.5pt solid #A5A5A5;
		border-bottom:.5pt solid black;	
	 	text-align			  : center;
	 	font-weight: normal;
	}
	
	td, tfoot th
	{
		padding               : 5px 10px;
		border-right:.5pt solid #A5A5A5;
		border-bottom:.5pt solid #A5A5A5;
	 	text-align			 : center; 
	}
	.rt{text-align: right;}
	.tlt{text-align: left;}
	//-->
	</style>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
	<table summary="<%=titleName%> 테이블로 
				<spring:message code="item.contact.menu.name"/>, 
				<spring:message code="item.menu.point.name"/>, 
				<c:out value="${msgItemLabelName}"/>(<c:out value="${msgItemCntName}"/>),
				<spring:message code="item.menu.point.person.percent"/>(%)를 제공하고 있습니다." style="width:100%;">
		<caption><%=titleName%></caption>
		<colgroup>
			<col width="500px" />
			<col />
			<col width="80px" />
			<col width="80px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><spring:message code="item.contact.menu.name"/></th>
				<th scope="col"><spring:message code="item.menu.point.name"/></th>
				<th scope="col"><c:out value="${msgItemLabelName}"/>(<c:out value="${msgItemCntName}"/>)</th>
				<th scope="col"><spring:message code="item.menu.point.person.percent"/>(%)</th>
			</tr>
		</thead>
		<tbody class="alignC">
		<c:if test="${empty list}">
			<tr>
				<td colspan="4" class="nolist"><spring:message code="message.no.menu.point.list"/></td>
			</tr>
		</c:if>
		<c:forEach var="listDt" items="${list}">
			<c:if test="${listDt.menu_idx != 4}">
			<c:set var="contactKey" value="MENU${listDt.menu_idx}"/>
			<c:set var="menuPointDt" value="${menuPointMap[contactKey]}"/>
			<c:choose>
				<c:when test="${menuPointTotalDt.CNT > 0}">
					<c:set var="listPercent" value="${menuPointDt.CNT * 100 / menuPointTotalDt.CNT}"/>
				</c:when>
				<c:otherwise>
					<c:set var="listPercent" value="0"/>
				</c:otherwise>
			</c:choose>
			<fmt:formatNumber var="listPercentStr" value="${listPercent}" pattern="#,##0.00" />
			<tr>
				<td scope="row" class="tlt" title="<c:out value="${listDt.total_menu_name}"/>"><c:out value="${listDt.total_menu_name}"/></td>
				<td class="rt">
					<c:choose>
						<c:when test="${listDt.use_poll == '1'}">
						<spring:message code="item.menu.point.point.text" arguments="${menuPointDt.AVG_POINT}"/>
						</c:when>
						<c:otherwise>
						미사용
						</c:otherwise>
					</c:choose>
				</td>
				<td class="rt"><c:out value="${elfn:getObjectInt(menuPointDt.CNT)}"/>&nbsp;</td>
				<td class="rt"><c:out value="${listPercentStr}"/>&nbsp;</td>
			</tr>
			</c:if>
		</c:forEach>
		</tbody>
		<c:if test="${!empty list}">
		<tfoot class="alignC">
			<tr>
				<th><spring:message code="item.contact.all"/></th>
				<td class="rt">
					<spring:message code="item.menu.point.point.text" arguments="${menuPointTotalDt.AVG_POINT}"/>
				</td>
				<td class="rt"><c:out value="${menuPointTotalDt.CNT}"/>&nbsp;</td>
				<td class="rt">100.00&nbsp;</td>
			</tr>
		</tfoot>
		</c:if>
	</table>
</body>
</html>