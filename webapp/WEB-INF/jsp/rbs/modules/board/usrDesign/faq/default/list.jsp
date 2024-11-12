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
	<div id="container_wrap">
		<div class="sub_wrap">
			<div class="sub_background commu_bg">
				<section class="inner">
					<h3 class="title fw-bolder text-center text-white">FAQ</h3>
					<p class="sub_title_script">지능형 교과·비교과 사이트의 자주찾는 질문을 모은 공간입니다.</p>
				</section>
			</div>
			<!--본문-->
			<section class="inner mt-4">
				<div class="com_tab">
					<ul class="tab_menuA">
					  <li><a href="<%=MenuUtil.getMenuUrl(39) %>">공지사항</a></li>
	        		  <li><a href="<%=MenuUtil.getMenuUrl(42) %>">자료실</a></li>
	        		  <li class="on"><a href="<%=MenuUtil.getMenuUrl(40) %>">FAQ</a></li>
	        		  <li><a href="<%=MenuUtil.getMenuUrl(41) %>">Q&A</a></li>
	        		  <li><a href="<%=MenuUtil.getMenuUrl(67) %>">오류신고</a></li>
	        		  <li><a href="<%=MenuUtil.getMenuUrl(43) %>">취업정보</a></li>
					</ul>
				</div>
				<itui:searchFormItemBoard divClass="faq_search d-flex flex-row gap-1 align-items-strech justify-content-end ms-auto mb-3 pt-5" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="border-0 text-white" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="border-0"/>
				<div class="accordion acc_faq" id="accFaq">
					<c:if test="${empty list}">
					<ul data-role="listview" class="tbListA">
						<li class="bllist"><spring:message code="message.no.list"/></li>
					</ul>
					</c:if>
					<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
					<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
					<c:forEach var="listDt" items="${list}" varStatus="i">
						<c:set var="listKey" value="${listDt.BRD_IDX}"/>
						<div class="accordion-item">
						<h2 class="accordion-header" id="accFaqH${i.count}">
							<button class="accordion-button align-items-start" type="button" data-bs-toggle="collapse" data-bs-target="#accFaq${i.count}" aria-expanded="true" aria-controls="accFaq${i.count}">
								<b>Q.</b> <p><itui:objectView itemId="contents" objDt="${listDt}"/></p>
							</button>
						</h2>
						<div id="accFaq${i.count}" class="accordion-collapse collapse show" aria-labelledby="accFaqH${i.count}" data-bs-parent="#accFaq">
							<div class="accordion-body"><itui:objectView itemId="replyCon" objDt="${listDt}"/></div>
			            </div>
			            </div>
						<c:set var="listNo" value="${listNo - 1}"/>
					</c:forEach>
				</div>
	
				<!--paging-->
				<ul class="pagination gap-2 justify-content-center mt-5">
					<pgui:paginationBoard listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
				</ul>
			</section>
		</div>
		<%@ include file="../../common/listTab.jsp"%>
		<!-- search -->
		<%-- <itui:searchFormItem formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/> --%>
		<!-- //search -->
		
		<%-- <%@ include file="../../common/listInfo.jsp"%> --%>
		<%-- <div class="faqList">
			<a href="#none" class="btnMore fn_btn_toggle">답변 모두 열기</a>
			<div class="fn_faq_list">
				<c:set var="colSpan" value="7"/>
				<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
				<c:set var="useNotice" value="${settingInfo.use_notice eq '1'}"/>
				<c:if test="${empty list}">
				<ul data-role="listview" class="tbListA">
					<li class="bllist"><spring:message code="message.no.list"/></li>
				</ul>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt.BRD_IDX}"/>
				<c:set var="isNotice" value="${useNotice and listDt.NOTICE eq '1'}"/>
				<dl>
					<dt>
						<itui:objectView itemId="contents" objDt="${listDt}"/>
					</dt>
					<dd style="display:none">
						<itui:objectView itemId="replyCon" objDt="${listDt}"/>
					</dd>
				</dl>				
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</div>
		</div> --%>
		<!-- paging -->
		<%-- <div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
		</div> --%>
		<!-- //paging -->
		
		<%@ include file="../../common/listBtns.jsp"%>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>