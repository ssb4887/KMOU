<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<spring:message var="msgItemSCntName" code="item.contact.member"/>
<spring:message var="msgItemSLabelName" code="item.contact.site_count"/>
<spring:message var="msgItemPCntName" code="item.contact.view"/>
<spring:message var="msgItemPLabelName" code="item.contact.page_count"/>
<spring:message var="msgItemLabelName" code="item.contact.site_page_count"/>
<%@ include file="commonTop.jsp"%>
<div id="cms_board_article">
	<!-- search -->
	<itui:searchFormItem divClass="tbSearch tbShowDt" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" listAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
	<!-- //search -->
	<!-- button -->
	<div class="btnTopFull">
			<div class="right">
				<a href="<c:out value="${URL_EXCELDOWN}"/>" title="엑셀다운로드" class="btnTFDL fn_btn_exceldown">엑셀다운로드</a>
			</div>
	</div>
	<!-- //button -->
	<table class="tbListA tbContact" summary="<c:out value="${crtMenu.menu_name}"/> 목록을 볼 수 있습니다.">
		<caption><c:out value="${crtMenu.menu_name}"/> 목록</caption>
		<colgroup>
			<col width="70px" />
			<col />
			<col width="80px" />
			<col width="80px" />
			<col width="150px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><spring:message code="item.contact.gubun"/>(<c:out value="${gubunItemName}"/>)</th>
				<th scope="col"><spring:message code="item.contact.graph"/></th>
				<th scope="col"><c:out value="${msgItemPLabelName}"/>(<c:out value="${msgItemPCntName}"/>)</th>
				<th scope="col"><c:out value="${msgItemSLabelName}"/>(<c:out value="${msgItemSCntName}"/>)</th>
				<th scope="col"><c:out value="${msgItemLabelName}"/>(<c:out value="${msgItemPCntName}"/>/<c:out value="${msgItemSCntName}"/>)</th>
			</tr>
		</thead>
		<tbody class="alignC">
		<c:if test="${empty list}">
			<tr>
				<td colspan="5" class="nolist"><spring:message code="message.no.contact.list"/></td>
			</tr>
		</c:if>
		<c:set var="graphMaxWidth" value="800" />
		<c:forEach var="listDt" items="${list}">
			<c:choose>
				<c:when test="${totalDt.PAGE_CONTACT_COUNT > 0}">
					<c:set var="listPercent" value="${elfn:getPercent(listDt.PAGE_CONTACT_COUNT, totalDt.PAGE_CONTACT_COUNT)}"/>
				</c:when>
				<c:otherwise>
					<c:set var="listPercent" value="0"/>
				</c:otherwise>
			</c:choose>
			<fmt:formatNumber var="listGraphWidth" value="${listPercent * graphMaxWidth / 100}" maxFractionDigits="1" />
			<fmt:formatNumber var="listPercentStr" value="${listPercent}" maxFractionDigits="2" />
			<tr>
				<td scope="row" class="num"><c:out value="${listDt.GUBUN_DATA}"/></td>
				<td class="graph">
					<div style="width:${graphMaxWidth}px;background-color:#EEE;">
					<p class="bar" style="width:${listGraphWidth}px;"><c:out value="${listPercentStr}"/>%(<c:out value="${listDt.PAGE_CONTACT_COUNT}"/><c:out value="${msgItemPCntName}"/>)</p>
					</div>
				</td>
				<td class="num rt"><c:out value="${listDt.PAGE_CONTACT_COUNT}"/></td>
				<td class="num rt"><c:out value="${listDt.SITE_CONTACT_COUNT}"/></td>
				<td class="num rt"><fmt:formatNumber value="${listDt.SITE_PAGE_COUNT}" pattern="#,##0.00" /></td>
			</tr>
		</c:forEach>
		</tbody>
		<c:if test="${!empty list}">
		<tfoot>
			<tr>
				<th><spring:message code="item.contact.all"/></th>
				<td></td>
				<td class="num rt"><c:out value="${totalDt.PAGE_CONTACT_COUNT}"/></td>
				<td class="num rt"><c:out value="${totalDt.SITE_CONTACT_COUNT}"/></td>
				<td class="num rt"><fmt:formatNumber value="${totalDt.SITE_PAGE_COUNT}" pattern="#,##0.00" /></td>
			</tr>
		</tfoot>
		</c:if>
	</table>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>