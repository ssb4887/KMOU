<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/mobile/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/mobile" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_boardSearchForm"/>
<c:set var="listFormId" value="fn_boardListForm"/>
<c:set var="btnModifyClass" value="fn_btn_modify"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">11111111111
	<!-- search -->
	<itui:searcFormItem formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
	
	<ul data-role="listview">
		<c:choose>
			<c:when test="${empty list}">
				<li class="com-egovNodata">
					<spring:message code="message.no.list"/>
				</li>			
			</c:when>
			<c:otherwise>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				
				<c:forEach var="listDt" items="${list}" varStatus="i">
					<c:set var="listKey" value="${listDt.BRD_IDX}"/>
					<c:set var="isNotice" value="${useNotice and listDt.NOTICE eq '1'}"/>
					
						<li>
							<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="상세보기" rel="external" data-transition="slide">
			            	<span class="uss-txtBlack"><c:out value="${listDt.SUBJECT}"/></span>
							<span class="uss-txtDate"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></span>
							</a>
						</li>
						<c:if test="${!isNotice}"><c:set var="listNo" value="${listNo - 1}"/></c:if>
				</c:forEach>	
				
			</c:otherwise>
		</c:choose>
	</ul>

	<!-- paging -->
	<div id="pageNavi" class="paginate mgt15">
		<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
	</div>
	<!-- //paging -->
	<%@ include file="../../common_mobile/listBtns.jsp"%>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>