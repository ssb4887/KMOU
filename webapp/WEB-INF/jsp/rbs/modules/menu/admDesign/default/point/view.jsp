<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<spring:message var="msgItemCntName" code="item.contact.member"/>
<spring:message var="msgItemLabelName" code="item.menu.point.person.name"/>
<c:set var="viewTitle" value="${settingInfo.view_title}"/>
<c:if test="${!empty queryString.tit}">
	<c:set var="viewTitle" value="${viewTitle} (${queryString.tit})"/>
</c:if>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${viewTitle}"/>
	</jsp:include>
</c:if>
<div id="cms_board_article">
	<table class="tbListA tbContact" summary="${settingInfo.view_title} (${queryString.tit}) 목록을 볼 수 있습니다.">
		<caption>${settingInfo.view_title} (${queryString.tit}) 목록</caption>
		<colgroup>
			<col />
			<col width="100px" />
			<col width="100px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><spring:message code="item.menu.point.name"/></th>
				<th scope="col"><c:out value="${msgItemLabelName}"/>(<c:out value="${msgItemCntName}"/>)</th>
				<th scope="col"><spring:message code="item.menu.point.person.percent"/>(%)</th>
			</tr>
		</thead>
		<tbody class="alignC">
		<c:if test="${empty list}">
			<tr>
				<td colspan="3" class="nolist"><spring:message code="message.no.menu.point.list"/></td>
			</tr>
		</c:if>
		<c:forEach var="listDt" items="${list}">
			<c:choose>
				<c:when test="${totalCount > 0}">
					<c:set var="listPercent" value="${listDt.CNT * 100 / totalCount}"/>
				</c:when>
				<c:otherwise>
					<c:set var="listPercent" value="0"/>
				</c:otherwise>
			</c:choose>
			<fmt:formatNumber var="listPercentStr" value="${listPercent}" pattern="#,##0.00" />
			<tr>
				<td class="graph">
					 <span class="star star${listDt.POINT}"><c:out value="${listDt.POINT_NAME}"/></span>
				</td>
				<td class="num rt"><c:out value="${elfn:getObjectInt(listDt.CNT)}"/></td>
				<td class="num rt"><c:out value="${listPercentStr}"/></td>
			</tr>
		</c:forEach>
		</tbody>
		<c:if test="${!empty list}">
		<tfoot class="alignC">
			<tr>
				<th><spring:message code="item.contact.all"/></th>
				<td class="num rt"><c:out value="${totalCount}"/></td>
				<td class="num rt">100.00</td>
			</tr>
		</tfoot>
		</c:if>
	</table>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>