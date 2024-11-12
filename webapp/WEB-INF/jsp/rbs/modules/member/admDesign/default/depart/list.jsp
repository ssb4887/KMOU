<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_departSearchForm"/>
<c:set var="listFormId" value="fn_departListForm"/>
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
				<col width="300px" />
				<col />
				<col width="100px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><c:if test="${mngAuth}"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></c:if></th>
				<th scope="col">번호</th>
				<th scope="col">수정</th>
				<th scope="col">부서명</th>
				<th scope="col">비고</th>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="6" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt.DEPART_IDX}"/>
				<tr>
					<td><c:if test="${mngAuth}"><input type="checkbox" id="select" name="select" data-p="<c:out value="${listDt.PARENT_DEPART_IDX}"/>" data-l="<c:out value="${listDt.DEPART_LEVEL}"/>"  title="<spring:message code="item.select"/>" value="${listKey}"/></c:if></td>
					<td class="num">${listNo}</td>
					<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF ${btnModifyClass}">수정</a></c:if></td>
					<td class="tlt unt_level<c:out value="${listDt.DEPART_LEVEL}"/>"><c:out value="${listDt.DEPART_NAME}"/></td>
					<td><c:out value="${listDt.REMARKS}"/></td>
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				</tr>
				<c:set var="listNo" value="${listNo + 1}"/>
				</c:forEach>
			</tbody>
		</table>
		</form>
		
		<c:if test="${settingInfo.use_paging == '1'}">
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
		</div>
		<!-- //paging -->
		</c:if>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>