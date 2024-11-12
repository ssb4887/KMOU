<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="searchFormId" value="fn_contactSearchForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/mlLogList.jsp"/>	
		<jsp:param name="searchFormId" value="${searchFormId}"/>
	</jsp:include>
</c:if>
<div id="cms_board_article">
	<!-- search -->
	<itui:searchFormItem divClass="tbSearch tbShowDt" formId="${searchFormId}" formName="${searchFormId}" isUseRadio="${true}" formAction="${URL_DEFAULT_LIST}" listAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
	<!-- //search -->
	<%@ include file="logListTopCom.jsp"%>
	<c:set var="summary">번호, 사이트명, 메뉴명, 관리사이트명, 관리메뉴명, URL, 접속정보, 아이디, 성명, 일자, IP의  <c:out value="${crtMenu.menu_name}"/> 목록입니다.</c:set>
	<table class="tbListA tbContact" summary="${summary}">
		<caption><c:out value="${crtMenu.menu_name}"/> 목록</caption>
		<colgroup>
			<col width="50px" />
			<col width="100px" />
			<col width="100px" />
			<col width="100px" />
			<col width="100px" />
			<col />
			<col />
			<col width="100px" />
			<col width="100px" />
			<col width="100px" />
			<col width="100px" />
		</colgroup>
		<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">사이트명</th>
				<th scope="col">메뉴명</th>
				<th scope="col">관리사이트명</th>
				<th scope="col">관리메뉴명</th>
				<th scope="col">URL</th>
				<th scope="col">접속정보</th>
				<th scope="col">아이디</th>
				<th scope="col">성명</th>
				<th scope="col">일자</th>
				<th scope="col">IP</th>
			</tr>
		</thead>
		<tbody class="alignC">
		<c:if test="${empty list}">
			<tr>
				<td colspan="11" class="nolist"><spring:message code="message.no.log.list"/></td>
			</tr>
		</c:if>
		<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
		<c:forEach var="listDt" items="${list}" varStatus="i">
			<tr>
				<td class="num">${listNo}</td>
				<td><c:out value="${listDt.SITE_NAME}"/></td>
				<td><c:out value="${listDt.MENU_NAME}"/></td>
				<td><c:out value="${listDt.MANAGE_SITE_NAME}"/></td>
				<td><c:out value="${listDt.MANAGE_MENU_NAME}"/></td>
				<td class="lt"><c:out value="${listDt.MENU_URL}"/></td>
				<td class="lt"><c:out value="${listDt.ACCESS_INFO}"/></td>
				<td><c:out value="${listDt.MEMBER_ID}"/></td>
				<td><c:out value="${listDt.MEMBER_NAME}"/></td>
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				<td><c:out value="${listDt.ACCESS_IP}"/></td>
			</tr>
			<c:set var="listNo" value="${listNo - 1}"/>
		</c:forEach>
		</tbody>
	</table>
		
	<!-- paging -->
	<div class="paginate mgt15">
		<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}" usePaging="1"/>
	</div>
	<!-- //paging -->
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>