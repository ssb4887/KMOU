<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="settingInfo" value="${moduleSetting.member_setting_info}"/>
<c:set var="viewTitle" value="${settingInfo.list_title}"/>
<c:if test="${!empty queryString.tit}">
	<c:set var="viewTitle" value="${viewTitle} (${queryString.tit})"/>
</c:if>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${viewTitle}"/>
	</jsp:include>
</c:if>
<div id="cms_board_article">
	<table class="tbListA tbContact" summary="${settingInfo.list_title} (${queryString.tit}) 목록을 볼 수 있습니다.">
		<caption>${settingInfo.list_title} (${queryString.tit}) 목록</caption>
		<colgroup>
			<col width="100px" />
			<col width="50px" />
			<col />
			<col width="80px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><spring:message code="item.menu.point.person.name"/></th>
				<th scope="col"><spring:message code="item.menu.point.name"/></th>
				<th scope="col"><spring:message code="item.menu.point.person.contents.name"/></th>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
			</tr>
		</thead>
		<tbody class="alignC">
		<c:if test="${empty list}">
			<tr>
				<td colspan="4" class="nolist"><spring:message code="message.no.menu.point.list"/></td>
			</tr>
		</c:if>
		<c:forEach var="listDt" items="${list}">
			<tr>
				<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.REGI_NAME)}"/></td>
				<td class="num rt"><spring:message code="item.menu.point.point.text" arguments="${elfn:getObjectInt(listDt.POINT)}"/></td>
				<td class="lt">${elfn:replaceScript(listDt.CONTENTS)}</td>
				<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<!-- paging -->
	<div class="paginate mgt15">
		<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'mpage')}" usePaging="${settingInfo.use_paging}"/>
	</div>
	<!-- //paging -->
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>