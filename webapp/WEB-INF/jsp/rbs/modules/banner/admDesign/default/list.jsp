<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							
<c:set var="searchFormId" value="fn_bannerSearchForm"/>							
<c:set var="listFormId" value="fn_bannerListForm"/>								
<c:set var="inputWinFlag" value="_open"/>										
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/>				
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
			<col width="160px" />
			<col />
			<col width="70px" />
			<col width="250px" />
			<col width="100px" />
			<col width="100px" />
		</colgroup>
		<thead>
		<tr>
			<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
			<th scope="col">번호</th>
			<th scope="col">수정</th>
			<th scope="col">이미지</th>
			<th scope="col">제목</th>
			<th scope="col">진행상태</th>
			<th scope="col">게시기간</th>
			<th scope="col"><spring:message code="item.reginame.name"/></th>
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
			<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
			<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
			<c:forEach var="listDt" items="${list}" varStatus="i">
			<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
			<c:set var="linkUrl" value="${listDt.LINK_URL}"/>
			<c:if test="${!empty linkUrl}">
				<c:set var="beginALink">
					<a href="<c:out value="${linkUrl}"/>" target="<c:out value="${listDt.LINK_TARGET}"/>" data-title="<c:out value="${listDt.SUBJECT}"/>">
				</c:set>
			</c:if>
			<c:if test="${!empty linkUrl}">
				<c:set var="endALink" value="</a>"/>
			</c:if>
			<tr>
				<td><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="<c:out value="${listKey}"/>"/></td>
				<td class="num"><c:out value="${listNo}"/></td>
				<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&<c:out value="${listIdxName}"/>=<c:out value="${listKey}"/>" class="btnTypeF <c:out value="${btnModifyClass}"/>">수정</a></c:if></td>
				<td class="pop_img"><img src="<c:out value="${URL_IMAGE}"/>&id=<c:out value="${elfn:imgNSeedEncrypt(listDt.IMG_SAVED_NAME)}"/>" alt="<c:out value="${listDt.IMG_TEXT}"/>"/></td>
				<td class="tlt">
					${beginALink}<c:out value="${listDt.SUBJECT}"/>${endALink}
				</td>
				<td><c:out value="${listDt.ISSTOP_NAME}"/></td>
				<td class="date"><c:if test="${!empty listDt.DSP_DATE1 || !empty listDt.DSP_DATE2}">
				<c:set var="objVal1" value="${listDt.DSP_DATE1}"/>
				<c:set var="objVal2" value="${listDt.DSP_DATE2}"/>
				<c:out value="${objVal1}"/> ~ <c:out value="${objVal2}"/>
				</c:if></td>
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