<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
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
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><spring:message code="item.contact.gubun"/>(<c:out value="${gubunItemName}"/>)</th>
				<th scope="col"><spring:message code="item.contact.graph"/></th>
				<th scope="col"><c:out value="${msgItemLabelName}"/>(<c:out value="${msgItemCntName}"/>)</th>
				<th scope="col"><spring:message code="item.contact.percent"/>(%)</th>
			</tr>
		</thead>
		<tbody class="alignC">
		<c:if test="${empty list}">
			<tr>
				<td colspan="4" class="nolist"><spring:message code="message.no.contact.list"/></td>
			</tr>
		</c:if>
		<c:set var="graphMaxWidth" value="900" />
		<c:forEach var="listDt" items="${list}">
			<c:choose>
				<c:when test="${totalSum > 0}">
					<c:set var="listPercent" value="${elfn:getPercent(listDt.CONTACT_COUNT, totalSum)}"/>
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
					<p class="bar" style="width:${listGraphWidth}px;"><c:out value="${listPercentStr}"/>%(<c:out value="${listDt.CONTACT_COUNT}"/><c:out value="${msgItemCntName}"/>)</p>
					</div>
				</td>
				<td class="num rt"><c:out value="${listDt.CONTACT_COUNT}"/></td>
				<td class="num rt"><c:out value="${listPercentStr}"/></td>
			</tr>
		</c:forEach>
		</tbody>
		<c:if test="${!empty list}">
		<tfoot>
			<tr>
				<th><spring:message code="item.contact.all"/></th>
				<td colspan="2" class="num rt"><c:out value="${totalSum}"/></td>
				<td class="num rt">100.00</td>
			</tr>
		</tfoot>
		</c:if>
	</table>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>