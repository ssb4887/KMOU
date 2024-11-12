<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_itemClassOptnSearchForm"/>
<c:set var="listFormId" value="fn_itemClassOptnListForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<c:if test="${useLocaleLang == '1'}">
		<c:set var="searchLangUrl" value="${URL_LANG_LIST}"/>
		<div class="absRight" style="top:13px;right:60px;">
		<form id="fn_searchLangForm" name="fn_searchLangForm" method="get" action="<c:out value="${searchLangUrl}"/>">
			<elui:hiddenInput inputInfo="${queryString}" exceptNames="${langSearchFormExceptParams}"/>
			<fieldset>
				<legend class="blind">언어검색 폼</legend>
				<label class="tit blind" for="lang">언어검색</label>
				<select id="slang" name="slang" class="select">
					<c:forEach var="langDt" items="${langList}">
					<option value="<c:out value="${langDt.OPTION_CODE}"/>"<c:if test="${langDt.OPTION_CODE == queryString.slang}"> selected="selected"</c:if>><c:out value="${langDt.OPTION_NAME}"/></option>
					</c:forEach>
				</select>
				<button type="submit" class="btnTFDL" id="fn_btn_search_slang">선택</button>
			</fieldset>
		</form>
		</div>
		</c:if>
		<c:if test="${mngAuth}">
		<!-- input -->
		<iframe id="fn_ifrm_input" name="fn_ifrm_input" src="<c:out value="${URL_INPUT}"/>" frameBorder="0px" scrolling="no" scrollbar="no" style="width:100%;height:250px;border;0px;" title="입력페이지"></iframe>
		<!-- // input -->
		</c:if>
		<!-- button -->
		<div class="btnTopFull">
			<%@ include file="../../../../../adm/include/module/listInputBtns.jsp"%>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="50px" />
				<col width="60px" />
				<col width="70px" />
				<col width="150px" />
				<col width="250px" />
				<col />
				<col width="100px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><c:if test="${mngAuth}"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></c:if></th>
				<th scope="col">번호</th>
				<th scope="col">수정</th>
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
					<td colspan="7" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt.CLASS_IDX}"/>
				<tr>
					<td><c:if test="${mngAuth}"><input type="checkbox" id="select" name="select" data-p="<c:out value="${listDt.PARENT_CLASS_IDX}"/>" data-l="<c:out value="${listDt.CLASS_LEVEL}"/>"  title="<spring:message code="item.select"/>" value="${listKey}"/></c:if></td>
					<td class="num">${listNo}</td>
					<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF fn_btn_modify">수정</a></c:if></td>
					<td><c:out value="${listKey}"/></td>
					<td class="tlt unt_level<c:out value="${listDt.CLASS_LEVEL}"/>"><c:out value="${listDt.CLASS_NAME}"/></td>
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