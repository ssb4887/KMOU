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

	<div class="sub_background speci_bg">
                <section class="inner">
                    <h3 class="title fw-bolder text-center text-white">비교과 검색</h3>
                    <p class="sub_title_script">비교과를 검색하고 상세정보를 확인할 수 있습니다.</p>
                    <!--검색창-->
		            <input type="hidden" name="mId" id="mId" value="34"/>
		            <!-- <input type="hidden" name="applyStart" id="applyStart" value=""/>
		            <input type="hidden" name="applyEnd" id="applyEnd" value=""/>
		            <input type="hidden" name="operStart" id="operStart" value=""/>
		            <input type="hidden" name="operEnd" id="operEnd" value=""/> -->
		            
                </section>
            </div>
            
            <!--본문-->
            <section class="inner mt-5 ">
            
            	<!--서치박스-->
                    <div class="search_wrap_bbox">
                    	<div class="search_keyword_section">
	                    	<span>키워드</span>
	                        <div class="schbox_top_section row_box_basic">
	                           	<div class="input-group border rounded position-relative">
	                                <input type="text" class="form-control border-0" name="nonSbjt_search" placeholder="검색어를 입력해주세요">
	                            </div>
	                            <button type="button" class="schbox_search_icon pk_btn"  onclick="searchBtn();"></button>
	                        </div>
	                    </div>

                        <div class="row_box row_box_dtl d-flex flex-column flex-lg-row align-items-start pt-0 pt-lg-3 ">
                            <!-- <h4 class="dtl_tit open mb-3 mb-lg-0"><span>펼치기</span></h4> -->
                            <button class="h4 dtl_tit open bg-transparent border-0 text-start" type="button">검색옵션 <span>&nbsp;열기</span></button>
                            <div class="column_box_wrap">
                                <div class="column_box pe-0 pe-xl-3 ">
                                    <ul>
<!--                                     	모집대상 -->
<!-- 										<li class="unit unit_univ d-flex flex-sm-row"> -->
<!-- 	                                    	<span>모집대상</span> -->
<!-- 	                                    	<div> -->
<!-- 										 		<div data-role="input" class="check default" data-type="checkbox" data-name="target"> -->
<!-- 										 			<label class="col-2"> -->
<!-- 														<input type="checkbox" name="target" value="STUDENT" data-display="학부생"> -->
<!-- 														학부생 -->
<!-- 													</label> -->
<!-- 													<label class="col-2"> -->
<!-- 														<input type="checkbox" name="target" value="GRADUATE" data-display="대학원생" > -->
<!-- 														대학원생 -->
<!-- 													</label>	 -->
<!-- 													<label class="col-2"> -->
<!-- 														<input type="checkbox" name="target" value="PROFESSOR" data-display="교수"  > -->
<!-- 														교수 -->
<!-- 													</label> -->
<!-- 													<label class="col-2"> -->
<!-- 														<input type="checkbox" name="target" value="EMPLOYEE" data-display="교직원" > -->
<!-- 														교직원 -->
<!-- 													</label>																							 -->
<!-- 												</div>											 -->
<!-- 											</div> -->
<!-- 	                                    </li>              -->
	                                    
	                                    <!-- 프로그램 분류 -->                       
										<li class="unit unit_univ d-flex flex-column flex-sm-row align-items-start align-items-sm-center ">
											<span>분류</span>
											<ul class="input-group gap-2">
												<li class="col-4">
													<select class="form-select" id="mainCategory" name="main_category">
														<option value="">전체</option>															
													</select>
												</li>
												<li class="col-4">
													<select class="form-select" id="subCategory" name="sub_category" <c:if test="${shcConditions.searchRange eq '2'}">disabled="disabled"</c:if>>
														<option value="">전체</option>
													</select>
												</li>
											</ul>
										</li>                                    
                                    
                                    	<!-- 신청기간 -->
                                        <li class="unit d-flex schbox_radio_label_custom">
                                            <span>신청종료일</span>
                                            <ul class="input-group">
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="금일 이내" id="sign_end_1" name="sign_end_1" <c:if test="${fn:contains(stdCore, '해양전문지식')}">checked</c:if>> 
                                                    <label class="form-check-label" for="sign_end_1">금일 이내</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="1주 이내" id="sign_end_2" name="sign_end_2" <c:if test="${fn:contains(stdCore, '지식정보활용')}">checked</c:if>>
                                                    <label class="form-check-label" for="sign_end_2">1주 이내</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="1개월 이내" id="sign_end_3" name="sign_end_3" <c:if test="${fn:contains(stdCore, '융복합적사고')}">checked</c:if>>
                                                    <label class="form-check-label" for="sign_end_3">1개월 이내</label>
                                                </li>
                                                <li class="in_schbox_type_data">
                                                	<input class="form-check-input" type="checkbox" value="직접입력" id="sign_end_4" name="sign_end_4" <c:if test="${fn:contains(stdCore, '글로벌네트워킹')}">checked</c:if>>
                                                    <label class="form-check-label" for="sign_end_4">직접입력</label>
                                                    <div class="input-group">
                                                    	<div class="data_start">
															<input type="text" name="sign_in_end_date" id="is_applDate1" title="신청기간" placeholder="신청기간" readonly="readonly" style="width:120px;" value="" disabled>
															<button type="button" class="btnCal" id="fn_btn_is_applDate1" title=""></button>
														</div>
														<p>~</p>
														<div class="data_end">
															<input type="text" name="sign_in_end_date1" id="is_applDate2" title="신청기간" placeholder="신청기간" readonly="readonly" style="width:120px;" value="" disabled>
															<button type="button" class="btnCal" id="fn_btn_is_applDate2" title=""></button>
														</div>
													</div>
                                                </li>
                                            </ul>									 											 	
										 	
                                        </li>
                                        
                                        <!-- 운영기간 -->
                                        <li class="unit d-flex schbox_radio_label_custom">
                                            <span>교육시작일</span>
                                            <ul class="input-group">
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="금일 이내" id="edu_end_1" name="edu_end_1" <c:if test="${fn:contains(stdCore, '해양전문지식')}">checked</c:if>> 
                                                    <label class="form-check-label" for="edu_end_1">금일 이내</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="1주 이내" id="edu_end_2" name="edu_end_2" <c:if test="${fn:contains(stdCore, '지식정보활용')}">checked</c:if>>
                                                    <label class="form-check-label" for="edu_end_2">1주 이내</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="1개월 이내" id="edu_end_3" name="edu_end_3" <c:if test="${fn:contains(stdCore, '융복합적사고')}">checked</c:if>>
                                                    <label class="form-check-label" for="edu_end_3">1개월 이내</label>
                                                </li>
                                                <li class="in_schbox_type_data">
                                                	<input class="form-check-input" type="checkbox" value="직접입력" id="edu_end_4" name="edu_end_4" <c:if test="${fn:contains(stdCore, '글로벌네트워킹')}">checked</c:if>>
                                                    <label class="form-check-label" for="edu_end_4">직접입력</label>
                                                    <div class="input-group">
                                                    	<div class="data_start">
															<input type="text" name="start_date" id="is_operDate1" title="운영기간" placeholder="운영기간" readonly="readonly" style="width:120px;" value="">
															<button type="button" class="btnCal" id="fn_btn_is_operDate1" title=""></button>
														</div>
														<p>~</p>
														<div class="data_end">
															<input type="text" name="start_date1" id="is_operDate2" title="운영기간" placeholder="운영기간" readonly="readonly" style="width:120px;" value="">
															<button type="button" class="btnCal" id="fn_btn_is_operDate2" title=""></button>
														</div>
													</div>
                                                </li>
                                            </ul>

                                        </li>
                                        
                                        <!-- 교육형식 -->                       
										<li class="unit unit_univ d-flex flex-column flex-sm-row align-items-start align-items-sm-center ">
											<span>교육형식</span>
											<ul class="input-group gap-2">
												<li class="col-4">
													<select class="form-select" id="program_type" name="program_type">
														<option value="">전체</option>															
													</select>
												</li>
											</ul>
										</li>
										     
                                        <!-- 운영방식 -->                       
										<li class="unit unit_univ d-flex flex-column flex-sm-row align-items-start align-items-sm-center ">
											<span>운영방식</span>
											<ul class="input-group gap-2">
												<li class="col-4">
													<select class="form-select" id="method" name="method">
														<option value="">전체</option>															
													</select>
												</li>
											</ul>
										</li>     
                                    </ul>
                                    
                                </div>
                            </div>
                        </div>
                        
                        <div class="schbox_bottom_section">
                        	<button type="button" class="reload_btn" onclick="reloadFilter();"><img src="../images/ico_reload.png" alt="초기화 아이콘"/>초기화</button>
                    		<button type="button" class="schbox_search" onclick="searchBtn();">상세검색</button>
                    	</div>
                    </div>
                    
                    
            	<form method="post" id="nonSbjtView" name="nonSbjtView">
            		<input type="hidden" name="mId" value="34">
	      			<input type="hidden" name="idx" value="">
					<input type="hidden" name="tidx" value="">
					
					<div class="nonsbjt_title_box title_box pb-3  mb-3">
						<h5 class="search_res mb-3" ><strong class="fw-bolder"><span id="keyword"></span></strong>에 대한 총 <span id="totalCount"></span>건의 게시글이 있습니다.</h5>
						<div class="nonsbjt_title_boxselect">
							<select class="form-select rounded-pill" aria-label="select" id="orderBy" name="orderBy">
								<option value="DEFAULT">정렬 기준</option>
								<option value="SIGNIN_END_RANK">모집종료 임박순</option>
								<option value="PARTICIPANT_RATE_RANK">인기순</option>
								<option value="REG_DATE_RANK">최신등록순</option>
								<c:if test="${loginVO.usertypeIdx eq '5' }">
								<option value="RECOMMEND">추천순</option>
								</c:if>
							</select>
						</div>
					</div>
				
				
				
            		<div class="d-flex flex-row gap-2 gap-sm-3 justify-content-end col-12 col-sm-6">
						
					</div>
					
					
	            	<div id="searchResult">
	             	</div>
             	</form>
             	 <!-- 페이지 내비게이션 -->
				<ul class="pagination gap-2 justify-content-center mt-5" id="page">					
				</ul>
				<!-- //페이지 내비게이션 -->
            </section>
<!--         </div> -->
<!--     </div> -->
             
		
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>