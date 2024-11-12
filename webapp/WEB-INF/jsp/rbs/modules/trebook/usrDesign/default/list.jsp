<%@ include file="../../../../include/commonTop.jsp"%>
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
	<div id="cms_trebook_article">
		<div class="btnTopFull">	
			<!-- search -->
			<itui:searchFormItem divClass="tbSearch tbShowDt" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>	
			<!-- //search -->
		
			<!-- button -->			
			<%@ include file="../common/listBtns.jsp"%>
			<!-- //button -->
		</div>
		
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="60px" />
				<col width="90px" />
				<col />
				<col width="60px" />
				<col width="100px" />
				<col width="100px" />
				<col width="70px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col" colspan="2">발행호</th>
				<th scope="col">파일</th>
				<th scope="col">작성자</th>
				<th scope="col">작성일</th>
				<th scope="col" class="end"><spring:message code="item.board.views.name"/></th>
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
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt.TRE_IDX}"/>
				<tr>
					<td class="num"><c:out value="${listNo}"/></td>					
					<td class="img">
						<c:set var="fImgText" value="${listDt.FIMG_TEXT}"/>
						<c:if test="${empty listDt.FIMG_TEXT}"><c:set var="fImgText" value="${listDt.FIMG_ORIGIN_NAME}"/></c:if>
						<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/> 상세보기">
							<img src="<c:out value="${URL_IMAGE}"/>&id=${elfn:imgNSeedEncrypt(listDt.FIMG_SAVED_NAME)}" alt="<c:out value="${fImgText}"/>" class="treImg"/>
						</a>
					</td>
					<td class="tlt">
						<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/> 상세보기">
							<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/>
							<c:if test="${!empty settingInfo.use_part_type && settingInfo.use_part_type == 1}">
								<font class="tpl5"><itui:objectView itemId="trePartType" itemInfo="${itemInfo}" objDt="${listDt}" optnHashMap="${optnHashMap}"/></font>
							</c:if>
						</a>
					</td>			  
					<td>
					<c:set var="fileText" value="${listDt.PDF_FILE_TEXT}"/>
					<c:if test="${empty listDt.PDF_FILE_TEXT}"><c:set var="fileText" value="${listDt.PDF_FILE_ORIGIN_NAME}"/></c:if>
				<c:choose>
					<c:when test="${settingInfo.use_pdf_upload == 1}">
						<a href="<c:out value="${URL_DOWNLOAD}"/>&${listIdxName}=${listKey}&itId=pdfFile" title="<c:out value="${fileText}"/>">
					</c:when>
					<c:otherwise>
						<a href="<c:out value="${listDt.DOWN_URL}"/> title="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/> PDF다운로드">
					</c:otherwise>
				</c:choose>
							<img src="<c:out value="${contextPath}${imgPath}/common/ico_file.gif"/>" alt="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/> PDF다운로드"/>
						</a>
					</td>
					<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.REGI_NAME)}"/></td>		
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
					<td class="num" id="fn_brd_views<c:out value="${listDt.TRE_IDX}"/>">
						<c:if test="${empty listDt.VIEWS}">0</c:if>
						<c:out value="${listDt.VIEWS}"/>
					</td>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</tbody>
		</table>
		
		<c:if test="${settingInfo.use_paging == '1'}">
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
		</div>
		<!-- //paging -->
		</c:if>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>