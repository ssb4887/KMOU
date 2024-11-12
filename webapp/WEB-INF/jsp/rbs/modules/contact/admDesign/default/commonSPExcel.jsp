<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.woowonsoft.egovframework.util.WebsiteDetailsHelper"%>
<%@page import="rbs.egovframework.WebsiteVO"%>
<%@page import="com.woowonsoft.egovframework.util.JSONObjectUtil"%>
<%@page import="net.sf.json.JSONObject"%>
<%@ include file="commonTopExcel.jsp"%>
<%
String titleName = null;
String userAgent = request.getHeader("User-Agent");
JSONObject crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute("crtMenu"));
String menuName = JSONObjectUtil.getString(crtMenu, "menu_name");
String fileSiteName = StringUtil.getString(request.getAttribute("fileSiteName"));
if(StringUtil.isEmpty(fileSiteName)) {
	WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
	fileSiteName = usrSiteVO.getSiteName();
}
titleName = fileSiteName + " " + menuName + "(" + titleGubunName + ")";
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
	.left{text-align: left;}
	//-->
	</style>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
	<table summary="<%=titleName%> 테이블로 
				<spring:message code="item.contact.gubun"/>(<c:out value="${gubunItemName}"/>), 
				<c:out value="${msgItemLabelName}"/>(<c:out value="${msgItemCntName}"/>),
				<spring:message code="item.contact.percent"/>(%)를 제공하고 있습니다." style="width:100%;">
			<caption><%=titleName%></caption>
		<colgroup>
			<col width="70px" />
			<col width="80px" />
			<col width="80px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><spring:message code="item.contact.gubun"/>(<%=gubunItemName%>)</th>
				<th scope="col"><c:out value="${msgItemLabelName}"/>(<c:out value="${msgItemCntName}"/>)</th>
				<th scope="col"><spring:message code="item.contact.percent"/>(%)</th>
			</tr>
		</thead>
		<tbody class="alignC">
		<c:if test="${empty list}">
			<tr>
				<td colspan="3" class="nolist"><spring:message code="message.no.contact.list"/></td>
			</tr>
		</c:if>
		<c:forEach var="listDt" items="${list}">
			<c:choose>
				<c:when test="${totalSum > 0}">
					<c:set var="listPercent" value="${elfn:getPercent(listDt.CONTACT_COUNT, totalSum)}"/>
				</c:when>
				<c:otherwise>
					<c:set var="listPercent" value="0"/>
				</c:otherwise>
			</c:choose>
			<fmt:formatNumber var="listPercentStr" value="${listPercent}" maxFractionDigits="2" />
			<tr>
				<td scope="row" class="num"><c:out value="${listDt.GUBUN_DATA}"/>&nbsp;</td>
				<td class="rt"><c:out value="${listDt.CONTACT_COUNT}"/>&nbsp;</td>
				<td class="rt"><c:out value="${listPercentStr}"/>&nbsp;</td>
			</tr>
		</c:forEach>
		</tbody>
		<c:if test="${!empty list}">
		<tfoot>
			<tr>
				<th><spring:message code="item.contact.all"/></th>
				<td class="rt"><c:out value="${totalSum}"/>&nbsp;</td>
				<td class="rt">100.00&nbsp;</td>
			</tr>
		</tfoot>
		</c:if>
	</table>
</body>
</html>