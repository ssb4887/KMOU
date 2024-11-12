<%@ include file="../../../../../include/commonTop.jsp"%>
<spring:message var="msgItemCntName" code="item.contact.member"/>
<spring:message var="msgItemLabelName" code="item.menu.point.person.name"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
	</jsp:include>
</c:if>
<div id="cms_board_article">
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
		<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
		<c:forEach var="listDt" items="${list}">
			<c:if test="${listDt.menu_idx != 4}">
			<c:set var="listKey" value="${listDt.menu_idx}"/>
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
				<td class="graph">
					<c:choose>
						<c:when test="${listDt.use_poll == '1'}">
						<c:set var="avgPoint" value="${elfn:getFloor(menuPointDt.AVG_POINT)}"/>
						<c:set var="pointMode" value="${menuPointDt.AVG_POINT % 1}"/>
						<c:choose>
							<c:when test="${pointMode >= 0.5}"><c:set var="addStarClassName" value="_hf"/></c:when>
							<c:otherwise><c:set var="addStarClassName" value=""/></c:otherwise>
						</c:choose>
						<a href="${URL_VIEW}&${listIdxName}=${listKey}" title="<spring:message code="item.menu.point.name"/> 상세보기" attr-title="<c:out value="${listDt.menu_name}"/>" class="fn_btn_view"><span class="star star<fmt:formatNumber value="${avgPoint}" pattern="##0" />${addStarClassName}"><spring:message code="item.menu.point.point.text" arguments="${menuPointDt.AVG_POINT}"/></span></a>
						</c:when>
						<c:otherwise>
						미사용
						</c:otherwise>
					</c:choose>
				</td>
				<td class="num rt"><a href="${URL_MEMBERLIST}&${listIdxName}=${listKey}" title="<c:out value="${msgItemLabelName}"/> 상세보기" attr-title="<c:out value="${listDt.menu_name}"/>" class="fn_btn_memberList"><c:out value="${elfn:getObjectInt(menuPointDt.CNT)}"/></a></td>
				<td class="num rt"><c:out value="${listPercentStr}"/></td>
			</tr>
			</c:if>
		</c:forEach>
		</tbody>
		<c:if test="${!empty list}">
		<c:set var="avgPoint" value="${elfn:getFloor(menuPointTotalDt.AVG_POINT)}"/>
		<c:set var="pointMode" value="${menuPointTotalDt.AVG_POINT % 1}"/>
		<c:choose>
			<c:when test="${pointMode >= 0.5}"><c:set var="addStarClassName" value="_hf"/></c:when>
			<c:otherwise><c:set var="addStarClassName" value=""/></c:otherwise>
		</c:choose>
		<tfoot class="alignC">
			<tr>
				<th><spring:message code="item.contact.all"/></th>
				<td class="graph">
					<span class="star star<fmt:formatNumber value="${avgPoint}" pattern="##0" />${addStarClassName}"><spring:message code="item.menu.point.point.text" arguments="${menuPointTotalDt.AVG_POINT}"/></span>
				</td>
				<td class="num rt"><c:out value="${menuPointTotalDt.CNT}"/></td>
				<td class="num rt">100.00</td>
			</tr>
		</tfoot>
		</c:if>
	</table>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>