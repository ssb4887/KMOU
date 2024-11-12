<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false"/>
</c:if>

<script src="${contextPath}${jsAssetPath}/sub.js"></script>
<!--content-->
	<c:set var="colSpan" value="4"/>
	<c:set var="subject" value="subject"/>
	<c:set var="replyState" value="replyState"/>
	
	<c:set var="useView" value="${settingInfo.use_view eq '1'}"/>
	<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
	<c:set var="useNotice" value="${settingInfo.use_notice eq '1'}"/>
	<c:set var="dsetCateListId" value="${settingInfo.dset_cate_list_id}"/>
		<div class="sub_background commu_bg">
			<section class="inner">
				<h3 class="title fw-bolder text-center text-white">취업정보</h3>
				<p class="sub_title_script">취업정보를 확인할 수 있습니다.</p>
			</section>
		</div>
		<!--본문-->
		<section class="inner mt-5">
			<section class="qna_desc d-flex flex-column flex-sm-row align-items-center gap-3 justify-content-center">
				<div class="img_box">
					<img src="${contextPath}/${crtSiteId}/images/write_wraning1.png" alt="글쓰기주의사항 이미지" />
				</div>
				<ul>
					<li class="title">알림</li>
					<li class="descript">게시글 공개기한이 설정된 게시판입니다. 해당 게시판에서 게시글 작성 시 공개기한을 반드시 설정하여야 하며, 설정된 공개기한이 지나면 게시글을 열람 또는 수정할 수 없습니다.(공개기한은 최대 3년까지 설정 가능하며, 별도로 설정하지 않을 시 1년으로 설정됩니다.)</li>
				</ul>
			</section>
			<div class="com_tab">
				<ul class="tab_menuA">
				  <li><a href="<%=MenuUtil.getMenuUrl(39) %>">공지사항</a></li>
        		  <li><a href="<%=MenuUtil.getMenuUrl(42) %>">자료실</a></li>
        		  <li><a href="<%=MenuUtil.getMenuUrl(40) %>">FAQ</a></li>
        		  <li><a href="<%=MenuUtil.getMenuUrl(41) %>">Q&A</a></li>
        		  <li><a href="<%=MenuUtil.getMenuUrl(67) %>">오류신고</a></li>
        		  <li class="on"><a href="<%=MenuUtil.getMenuUrl(43) %>">취업정보</a></li>
				</ul>
			</div>
			<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건</div>
			<div class="view_written d-flex justify-content-end my-3">
				<form name="srchForm" method="post" action="/web/board/list.do?mId=43">
					<input type="hidden" name="mId" value="43">
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
				<caption class="blind">취업정보 테이블</caption>
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
						<th scope="col" class="text-center">등록일</th>
						<th scope="col" class="text-center">조회</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty list}">
					<tr>
						<td colspan="${colSpan}" class="bllist" style="text-align:center;"><spring:message code="message.no.list"/></td>
					</tr>
					</c:if>
					<c:set var="listIdxName" value="NTT_SN"/>
					<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
					<c:forEach var="listDt" items="${list}" varStatus="i">
					<c:set var="listKey" value="${listDt.NTT_SN}"/>
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
<!-- 							<a href="#" class="d-flex flex-row align-items-center gap-2" title="제목"> -->
<!-- 								<span class="answ_succ">답변완료</span> -->
<!-- 								<span class="answ_wait">답변대기</span> -->
<%-- 								<strong class="title text-truncate">백엔드 개발자로 1년간 취준했던 신입입니다. IT 쪽 개발 전망에 대해 몇가지 궁금한 점이 있습니다. 백엔드 개발자로 1년간 취준했던 신입입니다. IT 쪽 개발 전망에 대해 몇가지 궁금한 점이 있습니다.</strong> <img src="${contextPath}/${crtSiteId}/assets/images/ico_lock.png" class="locked" alt="잠금아이콘" /> --%>
<!-- 								<span class="new_posting text-uppercase">new</span> -->
<!-- 							</a> -->
							<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="상세보기" class="fn_btn_view title-link"<c:if test="${listDt.RE_LEVEL > 1}"> style="padding-left:${(listDt.RE_LEVEL - 1) * 10}px;"</c:if><c:if test="${isDisplaySecretPassAuth}"> data-nm="<c:out value="${listKey}"/>"</c:if>>
								<c:if test="${isListImg}"><span class="photo"><img src="<c:out value="${URL_IMAGE}"/>&type=s&id=<c:out value="${elfn:imgNSeedEncrypt(listDt.LISTIMG_SAVED_NAME)}"/>" alt="<c:out value="${listDt.LISTIMG_TEXT}"/>"/></span></c:if>
								<c:if test="${!empty dsetCateListId}">
									<c:set var="dsetCateListVal"><itui:objectView itemId="${dsetCateListId}" objDt="${listDt}"/></c:set>
									<c:if test="${!empty dsetCateListVal}">[<c:out value="${dsetCateListVal}"/>]</c:if>
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
					<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
				</tbody>
			</table>
			
			<!--paging-->
			<ul class="pagination gap-2 justify-content-center mt-5">
				<pgui:paginationBoard listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
			</ul>
		</section>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false" /></c:if>
