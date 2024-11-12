<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							
<c:set var="searchFormId" value="fn_calendarSearchForm"/>							
<c:set var="listFormId" value="fn_calendarListForm"/>						
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/>				
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>	
		<jsp:param name="searchFormId" value="${searchFormId}"/>				
		<jsp:param name="listFormId" value="${listFormId}"/>					
	</jsp:include>
</c:if>
<div id="cms_calendar_article"> 
	<!-- search -->
	<itui:searchFormItem divClass="tbSearch tbShowDt" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
	<!-- //search -->
	
	<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
	<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
		<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
		<colgroup>
			<col width="40px" />
			<col />
			<col width="50px" />
			<col width="90px" />
			<col width="50px" />
		</colgroup>
		<thead>
		<tr>
			<th scope="col">번호</th>
			<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/></th>
			<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="file"/></th>
			<th scope="col"><spring:message code="item.regidate.name"/></th>
			<th scope="col" class="end"><spring:message code="item.board.views.name"/></th>
			<!-- 마지막 th에 class="end" -->
		</tr>
		</thead>
		<tbody class="alignC">
			<c:if test="${empty list}">
			<tr>
				<td colspan="5" class="bllist"><spring:message code="message.no.list"/></td>
			</tr>
			</c:if>
			<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
			<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
			<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
			<c:forEach var="listDt" items="${list}" varStatus="i">
			<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
			<tr>
				<td class="num"><c:out value="${listNo}"/></td>
				<td class="tlt">
						<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="상세보기" class="fn_btn_view"><c:out value="${listDt.SUBJECT}"/></a>
				</td>	
				<td><c:if test="${listDt.FILE_CNT > 0}"><img src="<c:out value="${contextPath}${imgPath}/common/ico_file.gif"/>" alt="파일"/></c:if></td>
				<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				<td class="num" id="fn_cal_views<c:out value="${listDt.CAL_IDX}"/>"><c:out value="${listDt.VIEWS}"/></td>
			</tr>
			<c:set var="listNo" value="${listNo - 1}"/>
			</c:forEach>
		</tbody>
	</table>
	</form>
	
	<%@ include file="../common/listBtns.jsp"%>
	<!-- paging -->
	<div class="paginate mgt15">
		<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
	</div>
	<!-- //paging -->
	
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>