<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							
<c:set var="searchFormId" value="fn_historySearchForm"/>							
<c:set var="listFormId" value="fn_historyListForm"/>								
<c:set var="btnModifyClass" value="fn_btn_modify"/>				
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>	
		<jsp:param name="searchFormId" value="${searchFormId}"/>				
		<jsp:param name="listFormId" value="${listFormId}"/>					
	</jsp:include>
</c:if>
<div id="cms_history_article">
	<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
		<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
		<colgroup>
			<col width="100px" />
			<col />
		</colgroup>
		<thead class="hidden">
		<tr>
			<th scope="col">년도</th>
			<th scope="col">내용</th>
			<!-- 마지막 th에 class="end" -->
		</tr>
		</thead>
		<tbody class="alignC">
			<c:if test="${empty list}">
			<tr>
				<td colspan="2" class="bllist"><spring:message code="message.no.list"/></td>
			</tr>
			</c:if>
			<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
			<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
			<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
			<c:forEach var="listDt" items="${list}" varStatus="i">
			<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
			<tr>
                <td><itui:objectView itemId="hisDate" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
				<td class="tlt"><itui:objectView itemId="contents" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
			</tr>
			<c:set var="listNo" value="${listNo - 1}"/>
			</c:forEach>
		</tbody>
	</table>
	
	<!-- paging -->
	<div class="paginate mgt15">
		<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
	</div>
	<!-- //paging -->
	
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>