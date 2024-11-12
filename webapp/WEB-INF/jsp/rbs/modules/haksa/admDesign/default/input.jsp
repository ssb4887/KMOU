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
	<div id="cms_board_article">

		
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_INPUTPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<input type="hidden" name="staffNo"  value="${dt.STAFF_NO }">
	
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
		<table class="tbWriteA" summary="${summary}">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
<%-- 					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th> --%>
					<th>위임장</th>					
					<td colspan="4">
						<itui:profFile itemId="file" itemInfo="${itemInfo}"/>
					</td>					
					<td colspan="1">
<%-- 						<button onclick="location.href='${URL_DOWNLOAD}&originFileName=/sample/api호출결과_1.png&savedFileName=/sample/api호출결과_1.png'" class="btnTypeA" style="width:200px;" title="위임장 양식 다운로드">위임장 양식 다운로드</button> --%>
						<a href="${URL_DOWNLOAD}&originFileName=/sample/api호출결과_1.png&savedFileName=/sample/api호출결과_1.png" style="width:150px;" class="btnTypeA">위임장 양식 다운로드</a>
					</td>
				</tr>			
				<tr>
					<itui:itemRadio itemId="pubcYn" itemInfo="${itemInfo}" colspan="5"/>
				</tr>
				<tr>
<%-- 					<itui:itemText itemId="sampleItemId" itemInfo="${itemInfo}" colspan="3" objStyle="width:400px"/> --%>
					<th>소속</th>
					<td colspan="3">
						<c:out value="${dt.DEPT_KOR_NM }"/>
					</td>					
					<td colspan="2">
						<img src="aa"/>
					</td>
				</tr>
				<tr>
					<th>이름(한글)</th>
					<td>
						<c:out value="${dt.KOR_NM }"/>
					</td>
					<th>이름(영문)</th>
					<td>
						<c:out value="${dt.ENG_FNM }"/>
					</td>
					<th>이름(한문)</th>
					<td>
						<c:out value="${dt.CHA_FNM }"/>
					</td>										
				</tr>
				<tr>
				</tr>
					<itui:objectText itemId="useFg" itemInfo="${itemInfo}" objStyle="display:none"/>
					<tr><th>재직구분</th><td colspan="5"><c:out value="${dt.HOOF_FG_KOR }"/></td></tr>	 
					<tr>
						<itui:itemText itemId="telNo" itemInfo="${itemInfo}" objStyle="width:150px;" colspan="2"/>
						<itui:itemText itemId="email" itemInfo="${itemInfo}" objStyle="width:250px;" colspan="2"/>						
					</tr>
					<tr><itui:itemText itemId="lab" itemInfo="${itemInfo}" objStyle="width:100%;" colspan="5"/></tr>
					<tr><itui:itemText itemId="homePage" itemInfo="${itemInfo}" objStyle="width:100%;" colspan="5"/></tr>					
					<tr><itui:itemTextarea itemId="shcr" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
					<tr><itui:itemRadio itemId="shcrPubcYn" itemInfo="${itemInfo}" colspan="5"/></tr>
					<tr><itui:itemTextarea itemId="carr" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
					<tr><itui:itemRadio itemId="carrPubcYn" itemInfo="${itemInfo}" colspan="5"/></tr>
					<tr><itui:itemTextarea itemId="thssRech" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
					<itui:itemRadio itemId="thssRechPubcYn" itemInfo="${itemInfo}" colspan="5"/>
					<tr><itui:itemTextarea itemId="rechFld" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
					<itui:itemRadio itemId="rechFldPubcYn" itemInfo="${itemInfo}" colspan="5"/>
					<tr><itui:itemTextarea itemId="bookThss" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
					<itui:itemRadio itemId="bookThssPubcYn" itemInfo="${itemInfo}" colspan="5"/>

			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<input type=button value="취소"  class="btnTypeB fn_btn_cancel">
		</div>
		</form>
	</div>
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if> --%>