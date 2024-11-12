<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
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
	<div id="cms_board_article">
		<%@ include file="../../common/listTab.jsp"%>
		<!-- search -->
		<div class="view_written d-flex justify-content-end my-3">
				<form name="srchForm" method="post" action="/RBISADM/menuContents/web/board/list.do?mId=43">
					<input type="hidden" name="mId" value="43">
					<fieldset class="BD_srch">
						<div>
							<select id="searchType" name="searchType" title="검색옵션">
								<option value="sj">제목</option>
								<option value="all">제목+내용</option>
								<option value="cn">내용</option>
								<option value="ncnm">작성자</option>
							</select>
							<input id="searchValue" name="searchValue" title="검색단어" class="txt inpTxt" type="text" value="">
							<button title="검색" class="btnSearch" onclick="javascript:searchEvent()">검색</button>
						</div>
					</fieldset>
				</form>
			</div> 
		<!-- //search -->
		<!-- button -->
		<div class="btnTopFull">
			<%-- <%@ include file="../../../../../adm/include/module/listBtns.jsp"%> --%>
			<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건</div>
		</div>
		<!-- //button -->
		<c:set var="colSpan" value="5"/>
		<c:set var="subject" value="subject"/>
		<c:set var="replyState" value="replyState"/>
		<c:set var="useQna" value="${settingInfo.use_qna eq '1'}"/>
		<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
		<c:set var="useNotice" value="${settingInfo.use_notice eq '1'}"/>
		<c:set var="useDldate" value="${settingInfo.use_dldate eq '1'}"/>
		<c:set var="dsetCateListId" value="${settingInfo.dset_cate_list_id}"/>
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="table notice_table">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="8%" />
				<col width="auto" />
				<col width="12%" />
				<col width="10%" />
				<col width="8%" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">제목</th>
				<th scope="col">작성자</th>
				<th scope="col">등록일</th>
				<th scope="col" class="end">조회</th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="${colSpan}" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="NTT_SN"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt.NTT_SN}"/>
				<c:set var="isNotice" value="${useNotice and listDt.NOTICE_AT eq 'Y'}"/>
				<tr<c:if test="${isNotice}"> style="background:#FFFBF3;font-weight:500;"</c:if>>
				<c:set var="isListImg" value="${settingInfo.use_list_img eq '1' and !empty listDt.LISTIMG_SAVED_NAME}"/>
					<td class="num">
						<c:choose>
							<c:when test="${isNotice}"><spring:message code="item.notice.name"/></c:when>
							<c:otherwise>${listNo}</c:otherwise>
						</c:choose>
					</td>
					
					<td class="tlt">
						<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="상세보기" class="fn_btn_view"<c:if test="${listDt.RE_LEVEL > 1}"> style="padding-left:${(listDt.RE_LEVEL - 1) * 10}px;"</c:if>>
							<c:if test="${isListImg}"><span class="photo"><img src="<c:out value="${URL_IMAGE}"/>&type=s&id=<c:out value="${elfn:imgNSeedEncrypt(listDt.LISTIMG_SAVED_NAME)}"/>" alt="<c:out value="${listDt.LISTIMG_TEXT}"/>"/></span></c:if>
							<c:if test="${!empty dsetCateListId}">
								<c:set var="dsetCateListVal"><itui:objectView itemId="${dsetCateListId}" objDt="${listDt}"/></c:set>
								<c:if test="${!empty dsetCateListVal}">[${dsetCateListVal}]</c:if>
							</c:if>
							<c:if test="${isListImg}"><span class="subject"><span></c:if>
								<c:out value="${listDt.NTT_SJ}"/>
							<c:if test="${isListImg}"></span></c:if>
							<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(listDt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>
							<c:if test="${isListImg}"></span></c:if>
						</a>
					</td>
					<td class="wri text-center border-end align-middle"><c:out value="${listDt.NCNM}"/></td>
					<td class="dat text-center border-end align-middle"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REG_DT}"/></td>
					<td class="hit text-center align-middle" id="fn_brd_views<c:out value="${listDt.NTT_SN}"/>"><c:out value="${listDt.NTT_RDCNT}"/></td>
				</tr>
				<c:choose>
					<c:when test="${isNotice}"></c:when>
					<c:otherwise><c:set var="listNo" value="${listNo - 1}"/></c:otherwise>
				</c:choose>
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