<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<spring:message var="msgItemCntName" code="item.contact.member"/>
<spring:message var="msgItemLabelName" code="item.contact.site_count"/>
<c:set var="searchFormId" value="fn_contactSearchForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/menuList.jsp"/>	
		<jsp:param name="searchFormId" value="${searchFormId}"/>
	</jsp:include>
</c:if>
<div id="cms_board_article">
	<div>
		<ul class="tabTypeA tab li4">
			<li class="fn_tab_view"><a id="tab1" href="${contextPath}/${crtSiteId}/menuContents/web/statistics/siteList.do?mId=58" class="active">방문수</a></li>
			<li class="fn_tab_view"><a id="tab2" href="${contextPath}/${crtSiteId}/menuContents/web/statistics/pageList.do?mId=58" class="">페이지뷰</a></li>
			<li class="fn_tab_view"><a id="tab3" href="${contextPath}/${crtSiteId}/menuContents/web/statistics/sitePageList.do?mId=58" class="">방문당 페이지뷰</a></li>
			<li class="fn_tab_view on"><a id="tab4" href="${contextPath}/${crtSiteId}/menuContents/web/statistics/menuList.do?mId=58" class="">메뉴별 접속자수</a></li>
			<li class="fn_tab_view"><a id="tab5" href="${contextPath}/${crtSiteId}/menuContents/web/statistics/userList.do?mId=58" class="">현재접속자</a></li>
		</ul>		
	</div>
	<!-- search -->
	<itui:searchFormItem divClass="tbSearch tbShowDt mt20" formId="${searchFormId}" formName="${searchFormId}" isUseRadio="${true}" formAction="${URL_DEFAULT_LIST}" listAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
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
			<col width="280px" />
			<col />
			<col width="120px" />
			<col width="120px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><spring:message code="item.contact.menu.name"/></th>
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
		<c:set var="graphMaxWidth" value="100" />
		<c:forEach var="listDt" items="${list}">
			<c:if test="${listDt.menu_idx != 1}">
			<c:set var="contactKey" value="MENU${listDt.menu_idx}"/>
			<c:set var="contactDt" value="${menuMap[contactKey]}"/>
			<c:choose>
				<c:when test="${totalSum > 0}">
					<c:set var="listPercent" value="${elfn:getPercent(contactDt.CONTACT_COUNT, totalSum)}"/>
				</c:when>
				<c:otherwise>
					<c:set var="listPercent" value="0"/>
				</c:otherwise>
			</c:choose>
			<fmt:formatNumber var="listGraphWidth" value="${listPercent * graphMaxWidth / 100}" maxFractionDigits="1" />
			<fmt:formatNumber var="listPercentStr" value="${listPercent}" maxFractionDigits="2" />
			<tr>
				<td scope="row" class="tlt" title="<c:out value="${listDt.total_menu_name}"/>"><c:out value="${listDt.total_menu_name}"/></td>
				<td class="graph">
					<div class="graph_bg">
					<p class="bar" style="width:${listGraphWidth}px;"><c:out value="${listPercentStr}"/>%(<c:out value="${contactDt.CONTACT_COUNT}"/><c:out value="${msgItemCntName}"/>)</p>
					</div>
				</td>
				<td class="num rt"><c:out value="${elfn:getObjectInt(contactDt.CONTACT_COUNT)}"/></td>
				<td class="num rt"><c:out value="${listPercentStr}"/></td>
			</tr>
			</c:if>
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