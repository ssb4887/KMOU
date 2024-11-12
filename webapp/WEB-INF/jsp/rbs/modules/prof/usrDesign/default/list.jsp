<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="searchFormId" value="fn_techSupportSearchForm"/>
<c:set var="listFormId" value="fn_techSupportListForm"/>
<c:set var="inputWinFlag" value=""/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
<style>
	#overlay {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0,0,0,0.5);
		display: none;
		z-index: 9999;
	}
	
	.loader {
		border:5px solid #f3f3f3;
		border-top: 5px solid #3498db;
		border-radius: 50%;
		width: 50px;
		height: 50px;
		animation: spin 2s linear infinite;
		position: fixed;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		z-index: 10000;
	}
	
	@keyframes spin {
		0% { transform: translate(-50%, -50%) rotate(0deg); }
		100% { transform: translate(-50%, -50%) rotate(360deg); }
	}
	
	.checkbox-height {
		height: 35px;
	}
</style>
<div class="mask2"></div>
<div id="overlay" style="display:none;"></div>
<div class="loader" style="display:none;"></div>
	<div class="sub_background profess_bg">
                <section class="inner">
                    <h3 class="title fw-bolder text-center text-white">교수 검색</h3>
                     <p class="sub_title_script">교수를 검색하고 상세정보를 확인할 수 있습니다.</p>
                    <!--검색창-->
		            
		            <input type="hidden" name="mId" id="mId" value="33"/>
		            <input type="hidden" name="univ" id="univ" value=""/><!-- 주관대학 -->
		            <input type="hidden" name="depart" id="depart" value=""/><!-- 학부(과) -->
		            <input type="hidden" name="major" id="major" value=""/><!-- 전공 -->
                </section>
            </div>
            
            <!--본문-->
            <main class="inner mt-5 ">
            	<%-- <c:if test="${!empty param.top_search }"><h5 class="search_res mb-3"><strong class="fw-bolder">${param.top_search }</strong>에 대한 총 <span>${totalCount }</span>건의 게시글이 있습니다.</h5></c:if> --%>
               
                <!--서치박스-->
                <div class="search_wrap_bbox">
                	<div class="search_keyword_section">
	                    <span>키워드</span>
	                	<div class="schbox_top_section row_box_basic">
	                     	<!-- form action="#">
								<select name="전체" id="all">
									<option value="etc01">전체</option>
									<option value="etc02">기타02</option>
									<option value="etc03">기타03</option>
									<option value="etc04">기타04</option>
								</select>
							</form -->
	                        <div class="input-group border rounded position-relative">
	                             <input type="text" class="form-control border-0" name="top_search" id="top_search" value="${param.top_search }" placeholder="검색어를 입력해주세요">
	                        </div>
	                        <button type="button" class="schbox_search_icon pk_btn"></button>
	                    </div>
					</div>
	                       

                    <div class="row_box row_box_dtl d-flex flex-column flex-lg-row align-items-start">
                    	<button class="h4 dtl_tit open bg-transparent border-0 text-start" type="button">검색옵션 <span>&nbsp;열기</span></button>
                        <div class="column_box_wrap">
                        	<div class="column_box pe-0 pe-xl-3 ">
                                 <ul>
                                     <li class="unit unit_univ d-flex flex-column flex-sm-row align-items-start align-items-sm-center ">
                                         <span>소속</span>
                                         <ul class="input-group gap-2">
                                             <li class="col-4">
                                                 <select class="form-select" id="college1">
                                                 	<option value="" selected>대학</option>
                                                  <c:forEach var="listDt" items="${collegeList }">
                                                  	<option value="${listDt.COLG_CD }" <c:if test="${param.univ == listDt.COLG_CD }">selected</c:if>>${listDt.COLG_NM }</option>
                                                  </c:forEach>
                                                 </select>
                                             </li>
                                             <li class="col-4">
                                                 <select class="form-select" id="college2" disabled>
                                                     <option value="" selected>학부(과)</option>
                                                 </select>
                                             </li>
                                             <!-- <li class="col-4">
                                                 <select class="form-select" id="college3" disabled>
                                                     <option value="" selected>전공</option>
                                                 </select>
                                             </li> -->
                                         </ul>
                                     </li>
                                 </ul>
                             </div>
                         </div>
                    </div>
                    <div class="schbox_bottom_section">
                       	 <button type="button" class="reload_btn" onclick="reloadFilter();"><img src="../images/ico_reload.png" alt="초기화 아이콘"/>초기화</button>
                   		<button type="button" class="schbox_search pk_btn" id="btnSbjtSearch">상세검색</button>
					</div>
                </div>
                
                <div class="prof_title_box title_box pb-3 mb-3">
					<div class="prof_title_boxselect">
						<select class="form-select rounded-pill" aria-label="select" id="orderBy" name="orderBy">
							<option value="">정렬기능</option>
							<option value="byName">이름순</option>
							<option value="byMajor">전공순</option>
						</select>
					</div>
				</div>   
                    
                <div class="profss_wrap d-flex flex-wrap gap-3 gap-md-4" id="profList">
                	<!--<c:forEach var="listDt" items="${profList }" varStatus="i">
                	<c:set var="listIdxName" value=""/>
                	<c:set var="empKey" value="${listDt.empNo }"/>
                		<div class="item border">
                			<a href="<c:out value="${URL_VIEW}"/>&empNo=${empKey}" title="교수" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3">
	                            <span class="photo_box d-inline-block rounded-circle overflow-hidden">
	                                <img src="https://www.kmou.ac.kr/${listDt.file_path }" alt="교수님사진" style="width:100%; height:100%; object-fit:cover;"/>
	                            </span>
	                            <span class="txt_box mb-2 mb-sm-0 mb-md-2">
	                                <strong>${listDt.empNm }<small class="fw-normal ps-2">교수</small></strong>
	                                <span class="d-flex flex-wrap">
	                                    <em class="fst-normal d-inline-block">${listDt.deptNm }</em>
	                                    <em class="fst-normal d-inline-block">기계조선에너지시스템 공학전공</em>
	                                </span>
	                            </span>
	                        </a>
	                        <ul class="dtl_info pt-4 d-flex flex-column gap-2">
	                            <li class="d-flex flex-row text-break"><strong>이메일</strong>${listDt.email }</li>
	                            <li class="d-flex flex-row text-break"><strong>전화번호</strong>${listDt.tlphon }</li>
	                            <li class="d-flex flex-row text-break"><strong>연구분야</strong>-</li>
	                            <li class="d-flex flex-row text-break"><strong>연구실</strong>${listDt.labRum }</li>
	                        </ul>
                    	</div>
                	</c:forEach>-->
               </div> 
                
                <!-- 페이지 내비게이션 -->
				<ul class="pagination gap-2 justify-content-center mt-5" id="page">
					<%-- <pgui:pagination listUrl="${URL_PAGE_LIST}&top_search=${param.top_search }" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/> --%>
				</ul>
				<!-- //페이지 내비게이션 -->

            </main>
        </div>
    </div>
    
  
  
<form name="viewForm" method="POST" action="../prof/view.do?mId=33">
	<input type="hidden" name="mId" class="mId" value="33"/>
	<input type="hidden" name="empNo" id="empNo" value=""/>
</form>
             
		
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>