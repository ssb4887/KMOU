<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.woowonsoft.egovframework.util.WebsiteDetailsHelper"%>
<%@page import="rbs.egovframework.WebsiteVO"%>
<%@ page language="java" contentType="application/vnd.ms-excel; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="../../../../include/commonTop.jsp"%>
<%
String userAgent = request.getHeader("User-Agent");

WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
String fileName = usrSiteVO.getSiteName() + " Ver." + StringUtil.getInt(request.getParameter("verIdx")) + ".xls";
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
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
	<title>${usrSiteVO.siteName} IA(Information Architecture)</title>
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
	
	.left{text-align: left;}
	
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
	.col_right_bold
	{
		border-right:.5pt solid black;	
	}
	thead th.row_top_th
	{
		border-bottom:.5pt solid #A5A5A5;
	}
	
	td
	{
		padding               : 5px 10px;
		border-right:.5pt solid #A5A5A5;
		border-bottom:.5pt solid #A5A5A5;
	 	text-align			 : center; 
	}
	
	.depth {
		background-color: #F2F2F2;
		float:left;
	}
	.depth_right_bold {
		background-color: #F2F2F2;
		float:left;
		border-right:.5pt solid black;	
	}
	//-->
	</style>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0">
		<table summary="${usrSiteVO.siteName} - IA(Information Architecture) 테이블로 메뉴 Idx, 
				<c:forEach begin="1" end="${maxMenuLevel}" varStatus="i">Depth${i.index-1}</c:forEach>, 
				경로, 설명, 기능구분, 기능명, 기능권한명, 사용자구분, 분류, 부서, 사용자, 비고, 등록날짜, 등록자, 
				수정날짜, 수정자를 제공하고 있습니다." style="width:100%;">
			<caption>${usrSiteVO.siteName} - IA(Information Architecture)</caption>
			<colgroup>
				<col/>
				<c:forEach begin="1" end="${maxMenuLevel}">
					<col/>
				</c:forEach>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
				<col/>
			</colgroup>
			<thead>
				<tr>
					<th scope="col" rowspan="2" class="col_right_bold">메뉴 일련번호</th>
					<c:forEach begin="1" end="${maxMenuLevel}" varStatus="i">
						<th scope="col" rowspan="2"<c:if test="${i.last}"> class="col_right_bold"</c:if>>Depth${i.index-1}</th>
					</c:forEach>
					<th scope="col" rowspan="2">경로</th>
					<th scope="col" rowspan="2">설명</th>
					<th scope="col" rowspan="2">기능구분</th>
					<th scope="col" rowspan="2">기능명</th>
					<th scope="col" rowspan="2">기능권한명</th>
					<th scope="col" colspan="4" class="row_top_th">메뉴접근권한</th>
					<th scope="col" colspan="4" class="row_top_th">메뉴관리권한</th>
					<th scope="col" rowspan="2">등록날짜</th>
					<th scope="col" rowspan="2">등록자</th>
					<th scope="col" rowspan="2">수정날짜</th>
					<th scope="col" rowspan="2" class="col_right_bold">수정자</th>
				</tr>
				<tr>
					<th scope="col" class="row_bottom_th">등급</th>
					<th scope="col" class="row_bottom_th">그룹</th>
					<th scope="col" class="row_bottom_th">부서</th>
					<th scope="col" class="row_bottom_th">사용자</th>
					<th scope="col" class="row_bottom_th">등급</th>
					<th scope="col" class="row_bottom_th">그룹</th>
					<th scope="col" class="row_bottom_th">부서</th>
					<th scope="col" class="row_bottom_th">사용자</th>
				</tr>
      			</thead>
			<tbody>
			<c:if test="${empty list}">
				<tr>
					<td colspan="${maxMenuLevel+15}" class="nolist">메뉴 목록이 없습니다.</td>
				</tr>
  			</c:if>
				<c:forEach items="${list}" var="listDt" varStatus="i">
               		<tr>
                 		<td class="col_right_bold"><c:out value="${listDt.MENU_IDX}"/></td>
						<c:forEach begin="1" end="${maxMenuLevel}" varStatus="j">
							<td class="depth<c:if test="${j.last}">_right_bold</c:if>"><c:if test="${listDt.MENU_LEVEL eq j.index}"><c:out value="${listDt.MENU_NAME}"/></c:if></td>
                 		</c:forEach>
                 		<td class="left"><c:out value="${listDt.MENU_LINK}"/></td>
                 		<td class="left"><c:out value="${listDt.MENU_COMMENT}"/></td>
                 		<td><c:out value="${listDt.MODULE_NAME}"/></td>
                 		<td><c:out value="${listDt.FN_NAME}"/></td>
                 		<td><c:out value="${listDt.AUTH_NAME}"/></td>
                 		<td>
                 			<c:if test="${!empty listDt.USERTYPE_IDX_NAME}">${listDt.USERTYPE_IDX_NAME} 이상</c:if>
                 		</td>
                 		<td class="left">
                 		<c:set var="itemId" value="groupIdxs"/>
                 		<c:set var="itemMultiMapList" value="${multiMapList[itemId]}"/>
                 		<c:set var="multiGKey" value="${listDt.MENU_IDX}"/>
                 		<c:set var="multiGList" value="${itemMultiMapList[multiGKey]}"/>
                 		<c:forEach var="multiDt" items="${multiGList}" varStatus="j">
                 		<c:if test="${!j.first}"><br/></c:if>${multiDt.OPTION_NAME}
                 		</c:forEach>
   						</td>
                 		<td class="left">
                 		<c:set var="itemId" value="departIdxs"/>
                 		<c:set var="itemMultiMapList" value="${multiMapList[itemId]}"/>
                 		<c:set var="multiGList" value="${itemMultiMapList[multiGKey]}"/>
                 		<c:forEach var="multiDt" items="${multiGList}" varStatus="j">
                 		<c:if test="${!j.first}"><br/></c:if>${multiDt.OPTION_NAME}
                 		</c:forEach>
   						</td>
                 		<td class="left">
                 		<c:set var="itemId" value="memberIdxs"/>
                 		<c:set var="itemMultiMapList" value="${multiMapList[itemId]}"/>
                 		<c:set var="multiGList" value="${itemMultiMapList[multiGKey]}"/>
                 		<c:forEach var="multiDt" items="${multiGList}" varStatus="j">
                 		<c:if test="${!j.first}"><br/></c:if>${multiDt.OPTION_NAME}
                 		</c:forEach>
   						</td>
   						<td>
                 			<c:if test="${!empty listDt.MANAGER_USERTYPE_IDX_NAME}">${listDt.MANAGER_USERTYPE_IDX_NAME} 이상</c:if>
                 		</td>
                 		<td class="left">
                 		<c:set var="itemId" value="managerGroupIdxs"/>
                 		<c:set var="itemMultiMapList" value="${multiMapList[itemId]}"/>
                 		<c:set var="multiGKey" value="${listDt.MENU_IDX}"/>
                 		<c:set var="multiGList" value="${itemMultiMapList[multiGKey]}"/>
                 		<c:forEach var="multiDt" items="${multiGList}" varStatus="j">
                 		<c:if test="${!j.first}"><br/></c:if>${multiDt.OPTION_NAME}
                 		</c:forEach>
   						</td>
                 		<td class="left">
                 		<c:set var="itemId" value="managerDepartIdxs"/>
                 		<c:set var="itemMultiMapList" value="${multiMapList[itemId]}"/>
                 		<c:set var="multiGList" value="${itemMultiMapList[multiGKey]}"/>
                 		<c:forEach var="multiDt" items="${multiGList}" varStatus="j">
                 		<c:if test="${!j.first}"><br/></c:if>${multiDt.OPTION_NAME}
                 		</c:forEach>
   						</td>
                 		<td class="left">
                 		<c:set var="itemId" value="managerMemberIdxs"/>
                 		<c:set var="itemMultiMapList" value="${multiMapList[itemId]}"/>
                 		<c:set var="multiGList" value="${itemMultiMapList[multiGKey]}"/>
                 		<c:forEach var="multiDt" items="${multiGList}" varStatus="j">
                 		<c:if test="${!j.first}"><br/></c:if>${multiDt.OPTION_NAME}
                 		</c:forEach>
   						</td>
                 		<td><c:out value="${listDt.REGI_DATE}"/></td>
                 		<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.REGI_NAME)}"/></td>
                 		<td><c:out value="${listDt.LAST_MODI_DATE}"/></td>
                 		<td class="col_right_bold"><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.LAST_MODI_NAME)}"/></td>
                 	</tr>
            	</c:forEach>
        	</tbody>
		</table>
</body>
</html>