<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_memberSearchForm"/>
<c:set var="listFormId" value="fn_memberListForm"/>
<c:set var="inputWinFlag" value="_open"/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<spring:message var="useMemberLog" code="Globals.memberAn.log.use.db" text="0"/>
<c:set var="btnMemberLogClass" value="fn_btn_member_log${inputWinFlag}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<!-- search -->
		<itui:searchFormItem divClass="tbMSearch" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		<!-- //search -->
		
		<!-- button -->
		<div class="btnTopFull">
			<c:set var="addBtnScript">
			<c:if test="${mngAuth && useMemberLog == '1'}">
			<a href="<c:out value="mbrLogList.do?mId=${crtMenu.menu_idx}"/>" class="btnTypeH ${btnMemberLogClass}">로그</a>
			</c:if>
			</c:set>
			<c:set var="input_dialog_height" value="470"/>
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
				<col />
				<col />
				<col width="120px" />
				<c:if test="${useMemberLog == '1'}"><col width="150px" /></c:if>
				<col width="100px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
				<th scope="col">번호</th>
				<th scope="col">수정</th>
				<th scope="col">아이디</th>
				<th scope="col">이름</th>
				<th scope="col">사용자유형</th>
				<c:if test="${useMemberLog == '1'}"><th scope="col">로그</th></c:if>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<c:set var="colspan" value="${7}"/>
				<c:if test="${useMemberLog == '1'}"><c:set var="colspan" value="${colspan + 1}"/></c:if>
				<tr>
					<td colspan="${colspan}" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt.MEMBER_IDX}"/>
				<tr>
					<td><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></td>
					<td class="num">${listNo}</td>
					<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF ${btnModifyClass}">수정</a></c:if></td>
					<td><itui:objectView itemId="mbrId" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<td><itui:objectView itemId="mbrName" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<td><c:out value="${listDt.USERTYPE_IDX_NAME}"/></td>
					<c:if test="${useMemberLog == '1'}"><td><a href="<c:out value="mbrLogList.do?mId=${crtMenu.menu_idx}&${listIdxName}=${listKey}"/>" class="btnTypeH ${btnMemberLogClass}">로그</a></td></c:if>
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