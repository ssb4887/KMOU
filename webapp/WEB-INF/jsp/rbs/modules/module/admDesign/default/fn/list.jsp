<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="searchFormId" value="fn_fnSearchForm"/>
<c:set var="listFormId" value="fn_fnListForm"/>
<c:set var="inputWinFlag" value="_open"/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
<div id="lnbWrap">
	<div class="infoWrap">
		<div class="info">
			<h4>기능</h4>
			<ul class="lnbMd">
			<c:forEach var="moduleDt" items="${moduleList}">
				<li<c:if test="${moduleDt.OPTION_CODE == queryString.moduleId}"> class="on"</c:if>><a href="<c:out value="${URL_DEFAULT_TAB_LIST}"/>&moduleId=<c:out value="${moduleDt.OPTION_CODE}"/>"><c:out value="${moduleDt.OPTION_NAME}"/></a></li>
			</c:forEach>
			</ul>
		</div>
	</div>
</div>
<div class="inWfullContWrap">
<c:set var="listAnUrl" value="${contextPath}${siteLocalPath}/${crtMenu.module_id}${moduleDt.AN_URL}"/>
<c:set var="isSetManager" value="${moduleDt.ISSETMANAGER}"/>
<c:set var="isItemManager" value="${moduleDt.ISITEMMANAGER}"/>
<c:set var="isInputManager" value="${moduleDt.ISINPUTMANAGER}"/>
<spring:message var="msgItemManageName" code="item.manage.name"/>
<spring:message var="msgItemSettingName" code="item.setting.name"/>
<spring:message var="msgItemItemsName" code="item.items.name"/>
<spring:message var="msgItemApplyName" code="item.apply.name"/>
	<div id="cms_board_article">
		<!-- search -->
		<itui:searchFormItem divClass="tbSearch tbShowDt" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" listAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		<!-- //search -->
		
		<!-- button -->
		<div class="btnTopFull">
			<c:set var="input_dialog_width" value="900"/>
			<c:set var="input_dialog_height" value="300"/>
			<c:set var="input_dialog_title" value="${moduleDt.MODULE_NAME}"/>
			<c:set var="deleteList_dialog_title" value="${moduleDt.MODULE_NAME}"/>
			<%@ include file="../../../../../adm/include/module/listBtns.jsp"%>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="50px" />
				<col width="60px" />
				<col width="70px" />
				<col width="70px" />
				<col />
				<col width="70px" />
				<col width="90px" />
				<col width="90px" />
				<col width="100px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
				<th scope="col">번호</th>
				<th scope="col">수정</th>
				<th scope="col">관리</th>
				<th scope="col"><itui:objectItemName itemId="fnName" itemInfo="${itemInfo}"/></th>
				<th scope="col">적용</th>
				<th scope="col">설정관리</th>
				<th scope="col">항목관리</th>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="9" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt.FN_IDX}"/>
				<tr>
					<td><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></td>
					<td class="num"><c:out value="${listKey}"/></td>
					<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF ${btnModifyClass}">수정</a></c:if></td>
					<td><a href="${listAnUrl}<c:out value="${param.mId}"/>&fnIdx=${listKey}" title="<c:out value="${listDt.FN_NAME}"/>" class="btnTypeE fn_btn_boardList">${msgItemManageName}</a></td>
					<td class="tlt"><c:out value="${listDt.FN_NAME}"/></td>
					<td><c:choose><c:when test="${listDt.ISAPPLY != '1'}"><a href="<c:out value="${URL_APPLYPROC}"/>&${listIdxName}=${listKey}" class="btnTypeH fn_btn_apply">${msgItemApplyName}</a></c:when><c:otherwise>${msgItemApplyName}</c:otherwise></c:choose></td>
					<td><c:if test="${isSetManager == '1'}"><a href="<c:out value="${URL_SETTING}"/>&${listIdxName}=${listKey}" title="<c:out value="${listDt.FN_NAME}"/>" class="btnTypeE fn_btn_setting">${msgItemSettingName}</a></c:if></td>
					<td><c:if test="${isItemManager == '1'}"><a href="<c:out value="${URL_ITEMSLIST}"/>&${listIdxName}=${listKey}" title="<c:out value="${listDt.FN_NAME}"/>" class="btnTypeE fn_btn_items">${msgItemItemsName}</a></c:if></td>
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
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>