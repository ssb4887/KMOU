<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false"/>
</c:if>

<script src="${contextPath}${jsAssetPath}/sub.js"></script>
<!--content-->
	<c:set var="colSpan" value="5"/>
	<c:set var="subject" value="subject"/>
	<c:set var="replyState" value="replyState"/>
	
	<c:set var="useView" value="${settingInfo.use_view eq '1'}"/>
	<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
	<c:set var="useNotice" value="${settingInfo.use_notice eq '1'}"/>
	<c:set var="dsetCateListId" value="${settingInfo.dset_cate_list_id}"/>
		<div class="sub_background commu_bg">
			<section class="inner">
				<h3 class="title fw-bolder text-center text-white">자료실</h3>
				<p class="sub_title_script">학사에 관련된 자료를 확인할 수 있습니다.</p>
			</section>
		</div>
		<!--본문-->
		<section class="inner mt-5">
			<div class="com_tab">
				<ul class="tab_menuA">
				  <li><a href="<%=MenuUtil.getMenuUrl(39) %>">공지사항</a></li>
        		  <li class="on"><a href="<%=MenuUtil.getMenuUrl(42) %>">자료실</a></li>
        		  <li><a href="<%=MenuUtil.getMenuUrl(40) %>">FAQ</a></li>
        		  <li><a href="<%=MenuUtil.getMenuUrl(41) %>">Q&A</a></li>
        		  <li><a href="<%=MenuUtil.getMenuUrl(67) %>">오류신고</a></li>
        		  <li><a href="<%=MenuUtil.getMenuUrl(43) %>">취업정보</a></li>
				</ul>
			</div>
			<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건</div>
			<div class="view_written d-flex justify-content-end my-3">
				<form name="srchForm" method="post" action="/web/board/list.do?mId=42">
					<input type="hidden" name="mId" value="42">
					<fieldset class="BD_srch">
						<div>
							<select id="searchType" name="searchType" title="검색옵션">
								<option value="sj">제목</option>
								<option value="all">제목+내용</option>
								<option value="cn">내용</option>
								<option value="ncnm">작성자</option>
							</select>
							<input id="searchValue" name="searchValue" title="검색단어" class="txt inpTxt" type="text" value="${param.searchValue }">
							<button title="검색" class="btnSearch" onclick="javascript:searchEvent()">검색</button>
						</div>
					</fieldset>
				</form>
			</div> 
			<table class="table notice_table">
				<caption class="blind">qna 테이블</caption>
				<colgroup>
					<col width="8%" />
					<col width="auto" />
					<col width="12%" />
					<col width="10%" />
					<col width="8%" />
				</colgroup>
				<thead>
					<tr>
						<th scope="col" class="text-center">번호</th>
						<th scope="col" class="text-center ps-4">제목</th>
						<th scope="col" class="text-center">작성자</th>
						<th scope="col" class="text-center">작성일</th>
						<th scope="col" class="text-center">조회</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty list}">
					<tr>
						<td colspan="${colSpan}" class="bllist" style="text-align:center;"><spring:message code="message.no.list"/></td>
					</tr>
					</c:if>
					<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
					<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
					<c:forEach var="listDt" items="${list}" varStatus="i">
					<c:set var="listKey" value="${listDt.BRD_IDX}"/>
					<c:set var="isNotice" value="${useNotice and listDt.NOTICE eq '1'}"/>
					<c:set var="isListImg" value="${settingInfo.use_list_img eq '1' and !empty listDt.LISTIMG_SAVED_NAME}"/>				
					<tr>
						<td class="numb text-center border-end align-middle">
							<c:choose>
								<c:when test="${isNotice}"><spring:message code="item.notice.name"/></c:when>
								<c:otherwise>${listNo}</c:otherwise>
							</c:choose>
						</td>						
						<td class="title_wrap text-start border-end px-3 px-sm-4 align-middle">
							<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="상세보기" class="fn_btn_view title-link">
								<c:if test="${isListImg}"><span class="photo"><img src="<c:out value="${URL_IMAGE}"/>&type=s&id=<c:out value="${elfn:imgNSeedEncrypt(listDt.LISTIMG_SAVED_NAME)}"/>" alt="<c:out value="${listDt.LISTIMG_TEXT}"/>"/></span></c:if>
								<c:if test="${!empty dsetCateListId}">
									<c:set var="dsetCateListVal"><itui:objectView itemId="${dsetCateListId}" objDt="${listDt}"/></c:set>
									<c:if test="${!empty dsetCateListVal}">[<c:out value="${dsetCateListVal}"/>]</c:if>
								</c:if>
								<c:if test="${isListImg}"><span class="subject"><span></c:if>
									<c:out value="${listDt.SUBJECT}"/>
								<c:if test="${isListImg}"></span></c:if>
								<c:if test="${settingInfo.use_secret eq '1' and listDt.SECRET eq '1'}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_secret.gif"/>" alt="비밀글"/></c:if>
<%-- 								<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(listDt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if> --%>
								<c:if test="${isListImg}"></span></c:if>
							</a>
						</td>
						<td class="wri text-center border-end align-middle"><c:out value="${listDt.REGI_NAME}"/></td>
						<td class="dat text-center border-end align-middle"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
						<c:if test="${useView}"><td class="hit text-center align-middle" id="fn_brd_views<c:out value="${listDt.BRD_IDX}"/>"><c:out value="${listDt.VIEWS}"/></c:if>
					</tr>
					<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
				</tbody>
			</table>
			<!--paging-->
			<ul class="pagination gap-2 justify-content-center mt-5">
				<pgui:paginationBoard listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
			</ul>
		</section>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false" /></c:if>
