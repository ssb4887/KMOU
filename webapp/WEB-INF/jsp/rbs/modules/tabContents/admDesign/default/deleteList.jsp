<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="searchFormId" value="fn_tabContentsSearchForm"/>								<%/* 검색폼 id/name */ %>
<c:set var="listFormId" value="fn_tabContentsListForm"/>									<%/* 목록관리폼 id/name */ %>
<jsp:include page="${jspSiteIncPath}/dialog_top.jsp" flush="false">
	<jsp:param name="page_tit" value="${settingInfo.deleteList_title}"/>
	<jsp:param name="javascript_page" value="${moduleJspRPath}/deleteList.jsp"/>	
	<jsp:param name="searchFormId" value="${searchFormId}"/>						
	<jsp:param name="listFormId" value="${listFormId}"/>							
</jsp:include>

	<div id="cms_tabContents_article">
		<!-- search -->
		<div class="tbMSearch">
			<itui:searchDelFormItem formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		</div>
		<!-- //search -->
		
		<!-- button -->
		<div class="btnTopFull">
			<%@ include file="../../../../adm/include/module/deleteListBtns.jsp"%>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="submit_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 삭제 목록을 볼 수 있습니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 삭제 목록</caption>
			<colgroup>
				<col width="50px" />
				<col width="60px" />
				<col width="100px"/>
				<col />
				<col width="90px" />
				<col width="90px" />
				<col width="90px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
				<th scope="col">번호</th>
				<th scope="col">분류코드</th>
				<th scope="col">분류명</th>
				<th scope="col"><spring:message code="item.deletename.name"/></th>
				<th scope="col"><spring:message code="item.regidate.name"/></th>
				<th scope="col" class="end"><spring:message code="item.deletedate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="7" class="bllist"><spring:message code="message.no.delete.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
				<tr>
					<td><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="<c:out value="${listKey}"/>"/></td>
					<td class="num">${listNo}</td>
					<td><c:out value="${listDt.CLASS_IDX}"/></td>
					<td></td>
					<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.LAST_MODI_NAME)}"/><br/>(<c:out value="${elfn:memberItemOrgValue('mbrId',listDt.LAST_MODI_ID)}"/>)</td>
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.LAST_MODI_DATE}"/></td>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</tbody>
		</table>
		</form>
		
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}" usePaging="1"/>
		</div>
		<!-- //paging -->
	</div>
<jsp:include page="${jspSiteIncPath}/dialog_bottom.jsp" flush="false"/>