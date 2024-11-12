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
		<div class="sub_background major_bg">
		    <section class="inner">
		        <h3 class="title fw-bolder text-center text-white">전공 검색</h3>
		        <p class="sub_title_script">전공을 검색하고 상세정보를 확인할 수 있습니다.</p>
			</section>
		</div>
            
		<!--본문-->
	    <main class="inner mt-5 ">
	    
	    	<!--서치박스-->
		        <div class="search_wrap_bbox">
		        	<div class="search_keyword_section">
	                    <span>키워드</span>
			            <div class="schbox_top_section row_box_basic">
			            	<!-- form action="#">
								<select class="" id="all">
			                        <option selected>제목 + 내용</option>
			                        <option value="1">제목</option>
			                        <option value="2">내용</option>
			                    </select>
							</form -->
	                       	<div class="input-group border rounded position-relative">
	                            <input type="text" class="form-control border-0" name="top_search" id="top_search" placeholder="검색어를 입력해주세요">
	                        </div>
			                <button type="submit" class="schbox_search_icon pk_btn" id="btnMajorSearch"></button>
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
	                                             <c:forEach var="listDt" items="${collegeList}">
	                                             	<option value="${listDt.COLG_CD}" <c:if test="${param.univ == listDt.COLG_CD}">selected</c:if>>${listDt.COLG_NM}</option>
	                                             </c:forEach>
	                                            </select>
	                                        </li>
	                                        <li class="col-4">
	                                            <select class="form-select" id="college2" disabled>
	                                                <option value="" selected>학부(과)</option>
	                                            </select>
	                                        </li>
	                                        <li class="col-4">
	                                            <select class="form-select" id="college3" disabled>
	                                                <option value="" selected>전공</option>
	                                            </select>
	                                        </li>
	                                    </ul>
									</li>
									<%-- <li class="unit d-flex schbox_radio_label_custom">
                                        <span>분류</span>
                                        <ul class="input-group">
                                            <li class="">
                                                <input class="form-check-input" type="checkbox" value="전공필수" id="std_core_1" name="std_core_1" <c:if test="${fn:contains(stdCore, '해양전문지식')}">checked</c:if>> 
                                                <label class="form-check-label" for="std_core_1">전공필수</label>
                                            </li>
                                            <li class="">
                                                <input class="form-check-input" type="checkbox" value="전공선택" id="std_core_2" name="std_core_2" <c:if test="${fn:contains(stdCore, '지식정보활용')}">checked</c:if>>
                                                <label class="form-check-label" for="std_core_2">전공선택</label>
                                            </li>
                                            <li class="">
                                                <input class="form-check-input" type="checkbox" value="전공기초" id="std_core_3" name="std_core_3" <c:if test="${fn:contains(stdCore, '융복합적사고')}">checked</c:if>>
                                                <label class="form-check-label" for="std_core_3">전공기초</label>
                                            </li>
                                            <li class="">
                                                <input class="form-check-input" type="checkbox" value="교양필수" id="std_core_4" name="std_core_4" <c:if test="${fn:contains(stdCore, '문제해결력')}">checked</c:if>>
                                                <label class="form-check-label" for="std_core_4">교양필수</label>
                                            </li>
                                            <li class="">
                                                <input class="form-check-input" type="checkbox" value="교양선택" id="std_core_5" name="std_core_5" <c:if test="${fn:contains(stdCore, '세계시민성')}">checked</c:if>>
                                                <label class="form-check-label" for="std_core_5">교양선택</label>
                                            </li>
                                            <li class="">
                                                <input class="form-check-input" type="checkbox" value="교직" id="std_core_6" name="std_core_6" <c:if test="${fn:contains(stdCore, '글로벌네트워킹')}">checked</c:if>>
                                                <label class="form-check-label" for="std_core_6">교직</label>
                                            </li>
                                            <li class="">
                                                <input class="form-check-input" type="checkbox" value="부전공" id="std_core_7" name="std_core_7" <c:if test="${fn:contains(stdCore, '자기관리')}">checked</c:if>>
                                                <label class="form-check-label" for="std_core_7">부전공</label>
                                            </li>
                                        </ul>
                                    </li> --%>
								</ul>
		                     </div>
		                </div>
		             </div>
		             
                     <div class="schbox_bottom_section">
                       	<button type="btn" class="reload_btn" onclick="reloadFilter();"><img src="../images/ico_reload.png" alt="초기화 아이콘"/>초기화</button>
                   		<button type="button" class="schbox_search pk_btn" id="btnMajorSearch">상세검색</button>
                   	 </div>
				</div>
				
				<div class="major_title_box title_box pb-3 mb-3">
					<div class="major_title_boxselect">
						<select class="form-select rounded-pill" aria-label="select" id="orderBy" name="orderBy">
							<option value="">정렬기능</option>
							<option value="byMajor">전공명순</option>
							<option value="byDepart">학부(과)순</option>
						</select>
					</div>
				</div> 
				
				<div class="majref_wrap d-flex flex-wrap" id="majorList">
	        	</div>
	                
	            <!-- 페이지 내비게이션 -->
				<ul class="pagination gap-2 justify-content-center mt-5" id="page"></ul>
				<!-- //페이지 내비게이션 -->
		</main>
	</div>
</div>

<form method="post" id="majorView" name="majorView">
 	<input type="hidden" name="mId" value="35">	
 	<input type="hidden" name="MAJOR_CD" value="">
 	<input type="hidden" name="COLG_NM" value="">
 	<input type="hidden" name="DEPT_NM" value="">
</form>
             
		
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>