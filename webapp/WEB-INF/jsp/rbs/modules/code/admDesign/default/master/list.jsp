<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="vewAuth" value="${elfn:isAuth('VEW')}"/>
<c:set var="searchFormId" value="fn_codeMstrSearchForm"/>
<c:set var="listFormId" value="fn_codeMstrListForm"/>
<c:set var="inputWinFlag" value="_open"/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<!-- search -->
		<itui:searchFormItem divClass="tbSearch" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		<!-- //search -->
		
		<!-- button -->
		<div class="btnTopFull">
			<c:set var="input_dialog_height" value="400"/>
			<%@ include file="../../../../../adm/include/module/listBtns.jsp"%>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="50px" />
				<col width="60px" />
				<col width="70px" />
				<col width="70px" />
				<col width="150px" />
				<col width="150px" />
				<col />
				<col width="100px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
				<th scope="col">번호</th>
				<th scope="col">수정</th>
				<th scope="col">코드관리</th>
				<th scope="col">코드</th>
				<th scope="col">코드명</th>
				<th scope="col">비고</th>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="8" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt.MASTER_CODE}"/>
				<c:set var="attrFlag" value="${false}"/>
				<c:if test="${crtMenu.attr_flag == 1 && listDt.ISHIDDEN == '1'}"><c:set var="attrFlag" value="${true}"/></c:if>
				<tr>
					<td><c:if test="${!attrFlag}"><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></c:if></td>
					<td class="num">${listNo}</td>
					<td><c:if test="${mngAuth && !attrFlag}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF ${btnModifyClass}">수정</a></c:if></td>
					<td><c:if test="${mngAuth && !attrFlag}"><a href="<c:out value="${URL_OPTNLIST}"/>&${listIdxName}=${listKey}" attr-title="<c:out value="${listDt.MASTER_NAME}"/>" class="btnTypeE fn_btn_optnlist">관리</a></c:if></td>
					<td><c:out value="${listKey}"/></td>
					<td><c:out value="${listDt.MASTER_NAME}"/></td>
					<td><c:out value="${listDt.REMARKS}"/></td>
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