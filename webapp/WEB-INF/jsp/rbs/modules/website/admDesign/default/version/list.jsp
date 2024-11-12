<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ include file="common.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_versionSearchForm"/>
<c:set var="listFormId" value="fn_versionListForm"/>
<spring:message var="useLayoutTmp" code="Globals.layoutTmp.use" text=""/>
<c:if test="${useLayoutTmp == '1'}">
	<spring:message var="layoutItemFlag" code="Globals.layoutTmp.item.flag" text=""/>
</c:if>
<c:choose>
	<c:when test="${useLayoutTmp == '1' && !empty layoutItemFlag}">
		<c:set var="inputFrameHeight" value="580"/>
		<c:set var="listTableHeight" value="160"/>
	</c:when>
	<c:otherwise>
		<c:set var="inputFrameHeight" value="350"/>
		<c:set var="listTableHeight" value="390"/>
	</c:otherwise>
</c:choose>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${menuName}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
		<jsp:param name="listTableHeight" value="${listTableHeight}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<c:if test="${mngAuth}">
		<!-- input -->
		<iframe id="fn_ifrm_input" name="fn_ifrm_input" src="<c:out value="${URL_INPUT}"/>" frameBorder="0px" scrolling="no" scrollbar="no" style="width:100%;height:${inputFrameHeight}px;border;0px;" title="<c:out value="${settingInfo.input_title}"/> 프레임"></iframe>
		<!-- // input -->
		</c:if>
		<!-- button -->
		<div class="btnTopFull">
			<c:set var="deleteList_dialog_title" value="${menuName}"/>
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
				<col width="100px" />
				<col />
				<c:if test="${useLayoutTmp == '1' && !empty layoutItemFlag}">
				<col width="120px" />
				<col width="120px" />
				</c:if>
				<col width="100px" />
				<col width="120px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><c:if test="${mngAuth}"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></c:if></th>
				<th scope="col">번호</th>
				<th scope="col">수정</th>
				<th scope="col">버전</th>
				<th scope="col"><itui:objectItemName itemId="local_path" itemInfo="${itemInfo}"/></th>
				<c:if test="${useLayoutTmp == '1' && !empty layoutItemFlag}">
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="layout_tmp"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="layout_theme"/></th>
				</c:if>
				<th scope="col">미리보기</th>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<c:set var="listColSpan" value="7"/>
				<c:if test="${useLayoutTmp == '1' && !empty layoutItemFlag}">
					<c:set var="listColSpan" value="9"/>
				</c:if>
				<tr>
					<td colspan="${listColSpan}" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt[settingInfo.idx_column]}"/>
				<tr<c:if test="${listDt.ISSERVICE == '1'}"> class="em"</c:if>>
					<td><c:if test="${mngAuth && listDt.ISSERVICE != '1'}"><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></c:if></td>
					<td class="num">${listNo}</td>
					<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF fn_btn_modify">수정</a></c:if></td>
					<td class="num<c:if test="${listDt.ISSERVICE == '1'}"> on</c:if>"><c:out value="${listKey}"/><c:if test="${listDt.ISSERVICE == '1'}"> [적용]</c:if></td>
					<td class="tlt"><itui:objectView itemId="local_path" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<c:if test="${useLayoutTmp == '1' && !empty layoutItemFlag}">
					<td><itui:objectView itemId="layout_tmp" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<td><itui:objectView itemId="layout_theme" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					</c:if>
					<td><a href="<c:out value="${contextPath}/${usrSiteVO.siteId}/main/main.do?mId=1&preVerIdx=${listKey}"/>" class="btnTypeE" target="_blank">미리보기</a></td>
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