<%@page import="com.woowonsoft.egovframework.form.DataForm"%>
<%@ page language="java" contentType="application/vnd.ms-excel; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="../../../../include/commonTop.jsp"%>
<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.woowonsoft.egovframework.util.WebsiteDetailsHelper"%>
<%@page import="rbs.egovframework.WebsiteVO"%>
<%@page import="com.woowonsoft.egovframework.util.JSONObjectUtil"%>
<%@page import="net.sf.json.JSONObject"%>

<%
String gubunItemName = null;
String titleGubunName = null;
DataForm queryString = (request.getAttribute("queryString") == null)?new DataForm():(DataForm)request.getAttribute("queryString");
String is_statsDate1 = queryString.getString("is_statsDate1");
String is_statsDate2 = queryString.getString("is_statsDate2");

if(!StringUtil.isEmpty(is_statsDate1)) gubunItemName = is_statsDate1;
else gubunItemName = "";
if(!StringUtil.isEmpty(is_statsDate1) || !StringUtil.isEmpty(is_statsDate2)) gubunItemName += "~";
if(!StringUtil.isEmpty(is_statsDate2)) gubunItemName += is_statsDate2;
titleGubunName = gubunItemName;
%>
<%
String titleName = null;
String userAgent = request.getHeader("User-Agent");
JSONObject crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute("crtMenu"));
String menuName = JSONObjectUtil.getString(crtMenu, "menu_name2") + "-" + JSONObjectUtil.getString(crtMenu, "menu_name");
titleName = menuName + "(" + titleGubunName + ")";
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
	.lt{text-align: left;}
	.tlt{text-align: left;}
	//-->
	</style>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
	<table summary="번호, 게시판명, 게시물번호, 파일명, 사유, 아이디, 성명, 일자, IP의  <%=titleName%> 목록입니다." style="width:100%;">
		<caption><%=titleName%> 목록</caption>
		<colgroup>
			<col width="50px" />
			<col width="100px" />
			<col width="70px" />
			<col width="250px" />
			<col />
			<col width="100px" />
			<col width="100px" />
			<col width="100px" />
			<col width="100px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">게시판명</th>
				<th scope="col">게시물번호</th>
				<th scope="col">파일명</th>
				<th scope="col">사유</th>
				<th scope="col">아이디</th>
				<th scope="col">성명</th>
				<th scope="col">일자</th>
				<th scope="col">IP</th>
			</tr>
		</thead>
		<tbody class="alignC">
		<c:if test="${empty list}">
			<tr>
				<td colspan="9" class="nolist"><spring:message code="message.no.log.list"/></td>
			</tr>
		</c:if>
		<c:set var="listNo" value="${totalCount}" />
		<c:forEach var="listDt" items="${list}" varStatus="i">
			<tr>
				<td class="num">${listNo}</td>
				<td><c:out value="${listDt.FN_NAME}"/></td>
				<td><c:out value="${listDt.BRD_IDX}"/></td>
				<td class="lt"><c:out value="${listDt.FILE_ORIGIN_NAME}"/></td>
				<td class="lt"><c:out value="${listDt.CONTENTS}"/></td>
				<td><c:out value="${listDt.REGI_ID}"/></td>
				<td><c:out value="${listDt.REGI_NAME}"/></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				<td><c:out value="${listDt.REGI_IP}"/></td>
			</tr>
			<c:set var="listNo" value="${listNo - 1}"/>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>