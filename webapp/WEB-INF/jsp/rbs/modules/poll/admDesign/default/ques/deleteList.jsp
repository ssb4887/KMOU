<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="searchFormId" value="fn_quesSearchForm"/>							<%/* 검색폼 id/name */ %>
<c:set var="listFormId" value="fn_quesListForm"/>								<%/* 목록관리폼 id/name */ %>
<%/* javascript 파일경로 */%>
<%/* 검색폼 id/name : javascript에서 사용 */ %>
<%/* 목록관리폼 id/name : javascript에서 사용 */ %>
<jsp:include page="${jspSiteIncPath}/dialog_top.jsp" flush="false">
	<jsp:param name="page_tit" value="${settingInfo.deleteList_title}"/>
	<jsp:param name="javascript_page" value="${moduleJspRPath}/deleteList.jsp"/>	
	<jsp:param name="searchFormId" value="${searchFormId}"/>						
	<jsp:param name="listFormId" value="${listFormId}"/>							
</jsp:include>
<spring:message var="msItDelDate" code="item.delete_date.name"/>
<spring:message var="msItSDate" code="item.start_date.name"/>
<spring:message var="msItEDate" code="item.end_date.name"/>
<spring:message var="msBtnCalSel" code="button.calendar.select"/>
	<div id="cms_poll_article">
		<!-- search -->
		<itui:searchDelFormItem formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		<!-- //search -->
		
		<!-- button -->
		<div class="btnTopFull">
			<%@ include file="../../../../../adm/include/module/deleteListBtns.jsp"%>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="submit_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 삭제 목록을 볼 수 있습니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 삭제 목록</caption>
			<colgroup>
				<col width="50px" />
				<col width="60px" />
				<col />
				<col width="120px" />
				<col width="60px" />
				<col width="90px" />
				<col width="90px" />
				<col width="90px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
				<th scope="col">번호</th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="questionContents"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="answerType"/></th>
				<th scope="col"><spring:message code="item.poll.question.rel"/></th>
				<th scope="col"><spring:message code="item.deletename.name"/></th>
				<th scope="col"><spring:message code="item.regidate.name"/></th>
				<th scope="col" class="end"><spring:message code="item.deletedate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="8" class="bllist"><spring:message code="message.no.delete.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
				<tr>
					<td><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></td>
					<td class="num">${listNo}</td>
					<td class="lt"><itui:objectView itemId="questionContents" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<td><itui:objectView itemId="answerType" itemInfo="${itemInfo}" objDt="${listDt}"/><c:if test="${listDt.ANSWER_TYPE == '1'}">(<itui:objectView itemId="itemType" itemInfo="${itemInfo}" objDt="${listDt}"/>)</c:if></td>
					<td><c:choose><c:when test="${listDt.REL_QUES_IDX > 0 && !empty listDt.REL_ITEM_IDX}">사용</c:when><c:otherwise>미사용</c:otherwise></c:choose></td>
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
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" usePaging="1" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
		</div>
		<!-- //paging -->
	</div>
<jsp:include page="${jspSiteIncPath}/dialog_bottom.jsp" flush="false"/>