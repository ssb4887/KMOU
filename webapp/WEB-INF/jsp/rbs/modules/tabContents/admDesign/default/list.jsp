<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							
<c:set var="listFormId" value="fn_tanContentsListForm"/>								
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/>				
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>	
		<jsp:param name="searchFormId" value="${searchFormId}"/>				
		<jsp:param name="listFormId" value="${listFormId}"/>					
	</jsp:include>
</c:if>
<div id="cms_tabContents_article">
	<!-- search -->
	<itui:searchFormItem divClass="tbSearch tbShowDt" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
	<!-- //search -->
	
	<!-- button -->
	<div class="btnTopFull">
		<%@ include file="../../../../adm/include/module/listBtns.jsp"%>
	</div>
	<!-- //button -->

	<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
	<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
		<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
		<colgroup>
			<col width="50px" />
			<col width="60px" />
			<col width="70px" />
			<col width="100px" />
			<col />
			<col width="80px" />
			<col width="80px" />
		</colgroup>
		<thead>
		<tr>
			<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
			<th scope="col">번호</th>
			<th scope="col">수정</th>
			<th scope="col">분류코드</th>
			<th scope="col">분류명</th>
			<th scope="col"><spring:message code="item.reginame.name"/></th>
			<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
			<!-- 마지막 th에 class="end" -->
		</tr>
		</thead>
		<tbody class="alignC">
			<c:if test="${empty list}">
			<tr>
				<td colspan="7" class="bllist"><spring:message code="message.no.list"/></td>
			</tr>
			</c:if>
			<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
			<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
			<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
			<c:forEach var="listDt" items="${list}" varStatus="i">
			<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
			<tr>
				<td><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="<c:out value="${listKey}"/>"/></td>
				<td class="num"><c:out value="${listNo}"/></td>
				<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&<c:out value="${listIdxName}"/>=<c:out value="${listKey}"/>" class="btnTypeF <c:out value="${btnModifyClass}"/>">수정</a></c:if></td>
                <td><c:out value="${listDt.CLASS_IDX}"/></td>
				<td><a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="상세보기" class="fn_btn_view"><c:out value="${listDt.CLASS_NAME}"/></a></td>
				<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.REGI_NAME)}"/></td>
				<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
			</tr>
			<c:set var="listNo" value="${listNo - 1}"/>
			</c:forEach>
		</tbody>
	</table>
	</form>
	
	<!-- paging -->
	<div class="paginate mgt15">
		<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
	</div>
	<!-- //paging -->
	
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>