<%@ include file="../../../../include/commonTop.jsp"%>
<link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_sampleInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
	<div id="cms_board_article" style="height:95%; overflow-y:auto;">
		
		
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<%-- table summary, 항목출력에 사용 --%>
		<c:set var="exceptIdStr">제외할 항목id를 구분자(,)로 구분하여 입력(예:name,notice,subject,file,contents,listImg)</c:set>
		<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
		<%-- 
			table summary값 setting - 테이블 사용하지 않는 경우는 필요 없음
			디자인 문제로 제외한 항목(exceptIdStr에 추가했으나 table내에 추가되는 항목)은 수동으로 summary에 추가
			예시)
			<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
		--%>
		<c:set var="summary"><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/> 입력표</c:set>
		
		
		<%-- 2. 디자인에 맞게 필요한 항목만 출력하는 경우 --%>
		<table class="tbWriteA trackTable" summary="${summary}">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:200px;" />
			<col style="width:140px;" />
			<col />
			</colgroup>
			<tbody>
				<itui:objectText itemId="majorIdx" itemInfo="${itemInfo}" objStyle="display:none"/>
				<itui:objectText itemId="yearIdx" itemInfo="${itemInfo}" objStyle="display:none"/>
				<itui:objectText itemId="trackIdx" itemInfo="${itemInfo}" objStyle="display:none"/>
			
				<tr>
					<th colspan="2"><itui:objectLabel itemId="sptPsnKor" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="sptPsnKor" itemInfo="${itemInfo}" objStyle="height:80px;"/></td>
				</tr>
				<%-- <tr>
					<th colspan="2"><itui:objectLabel itemId="professor" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="professor" itemInfo="${itemInfo}" objStyle="height:80px;"/></td>
				</tr>
				<tr>
					<th colspan="2"><itui:objectLabel itemId="contents" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="contents" itemInfo="${itemInfo}" objStyle="height:220px;"/></td>
				</tr>
				<tr>
					<th colspan="2"><itui:objectLabel itemId="job" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="job" itemInfo="${itemInfo}" objStyle="height:220px;"/></td>
				</tr>
				<tr>
					<th colspan="2"><itui:objectLabel itemId="license" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="license" itemInfo="${itemInfo}" objStyle="height:220px;"/></td>
				</tr>
				<tr>
					<th colspan="2"><itui:objectLabel itemId="nonsubject" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="nonsubject" itemInfo="${itemInfo}" objStyle="height:220px;"/></td>
				</tr> --%>
				<tr>
					<th rowspan="3" style="border-right: 1px solid #e1e1e1;">1학년</th>
					<%-- <th>공통</th>
					<td colspan="2">
						<itui:objectTextarea itemId="firstCommon" itemInfo="${itemInfo}" objStyle="height:140px;"/>
					</td> --%>
				</tr>
				<tr>
					<th>전공필수</th>
					<td colspan="2">
						<itui:objectTextarea itemId="firstRequired" itemInfo="${itemInfo}" objStyle="height:140px;"/>
					</td>
				</tr>
				<tr>
					<th>전공선택</th>
					<td colspan="2">
						<itui:objectTextarea itemId="firstChoice" itemInfo="${itemInfo}"  objStyle="height:140px;"/>
					</td>
				</tr>
				<tr>
					<th rowspan="3" style="border-right: 1px solid #e1e1e1;">2학년</th>
					<%-- <th>공통</th>
					<td colspan="2">
						<itui:objectTextarea itemId="secondCommon" itemInfo="${itemInfo}" objStyle="height:140px;"/>
					</td> --%>
				</tr>
				<tr>
					<th>전공필수</th>
					<td colspan="2">
						<itui:objectTextarea itemId="secondRequired" itemInfo="${itemInfo}" objStyle="height:140px;"/>
					</td>
				</tr>
				<tr>
					<th>전공선택</th>
					<td colspan="2">
						<itui:objectTextarea itemId="secondChoice" itemInfo="${itemInfo}"  objStyle="height:140px;"/>
					</td>
				</tr>
				<tr>
					<th rowspan="3" style="border-right: 1px solid #e1e1e1;">3학년</th>
					<%-- <th>공통</th>
					<td colspan="2">
						<itui:objectTextarea itemId="thirdCommon" itemInfo="${itemInfo}" objStyle="height:140px;" />
					</td> --%>
				</tr>
				<tr>
					<th>전공필수</th>
					<td colspan="2">
						<itui:objectTextarea itemId="thirdRequired" itemInfo="${itemInfo}" objStyle="height:140px;"/>
					</td>
				</tr>
				<tr>
					<th>전공선택</th>
					<td colspan="2">
						<itui:objectTextarea itemId="thirdChoice" itemInfo="${itemInfo}" objStyle="height:140px;"/>
					</td>
				</tr>
				<tr>
					<th rowspan="3" style="border-right: 1px solid #e1e1e1;">4학년</th>
					<%-- <th>공통</th>
					<td colspan="2">
						<itui:objectTextarea itemId="fourthCommon" itemInfo="${itemInfo}" objStyle="height:140px;"/>
					</td> --%>
				</tr>
				<tr>
					<th>전공필수</th>
					<td colspan="2">
						<itui:objectTextarea itemId="fourthRequired" itemInfo="${itemInfo}" objStyle="height:140px;"/>
					</td>
				</tr>
				<tr>
					<th>전공선택</th>
					<td colspan="2">
						<itui:objectTextarea itemId="fourthChoice" itemInfo="${itemInfo}" objStyle="height:140px;"/>
					</td>
				</tr>
				<%-- <tr>
					<th colspan="2"><itui:objectLabel itemId="majorNm" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="majorNm" itemInfo="${itemInfo}" objStyle="height:220px;"/></td>
				</tr>
				<tr>
					<th colspan="2"><itui:objectLabel itemId="subjectNm" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="subjectNm" itemInfo="${itemInfo}" objStyle="height:220px;"/></td>
				</tr>
				<tr>
					<th colspan="2"><itui:objectLabel itemId="graduateNm" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="graduateNm" itemInfo="${itemInfo}" objStyle="height:220px;"/></td>
				</tr>
				<tr>
					<th colspan="2"><itui:objectLabel itemId="graduateMajor" itemInfo="${itemInfo}"/></th>
					<td colspan="2"><itui:objectTextarea itemId="graduateMajor" itemInfo="${itemInfo}" objStyle="height:220px;"/></td>
				</tr> --%>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<c:if test="${!empty dt}">
				<a class="btnDelete" href="<c:out value="${URL_DELETETRACK_PROC}"/>" title="삭제" class="btnTD fn_btn_delete">삭제</a>
			</c:if>
			<input type=button value="취소"  class="btnTypeB fn_btn_trackCancel">
		</div>
		</form>
	</div>
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if> --%>