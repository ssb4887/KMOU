<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>

<c:set var="inputFormId" value="fn_memberInputForm"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="items" value="${itemInfo.items}"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="menuName" value="${crtMenu.menu_name}"/>

 <link rel="stylesheet" type="text/css" href="/web/css/tabulator_bootstrap5.css">

<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
 		<jsp:param name="javascript_page" value="${moduleJspRPath}/studentInfo.jsp"/>
 		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>

<div class="mask2"></div>
<div id="overlay" style="display:none;"></div>
<div class="loader" style="display:none;"></div>

<div class="sub_background mypage_bg">
    <section class="inner">
        <h3 class="title fw-bolder text-center text-white">마이페이지</h3>
		<p class="sub_title_script">성적 및 이수사항을 확인할 수 있습니다.</p>
    </section>
</div>
<!--본문-->
<section class="inner mt-5">
    <!--제목-->
	<section class="myP_info_bx">
	    <div class="myP_info">
	        <div class="info_box">
	            <div class="in_info_box">
	            	<div class="img_box">
		                <img src="../images/profile.png" alt="내 사진"/>
		            </div>
		            <div class="text_box">
		                <h4 id="myInfoName"></h4>
		                <p id="myInfoEName"></p>
		            </div>
	            </div>
	            <ul id="myInfoGradeSmt">
	                <li class="numb" id="myInfoStdNo"></li>
	            </ul>
	        </div>
	        <dl>
                      <dt>전공</dt>
                      <dd id="myMajor">-</dd>
                  </dl>
                  <dl>
                      <dt>복수/부전공</dt>
                      <dd id="mySub">-</dd>
                  </dl>
                  <dl>
                      <dt>융합/연계전공</dt>
                      <dd id="myFuseLink">-</dd>
                  </dl>
<!--                     <dl> -->
<!--                         <dt>학생전공설계</dt> -->
<!--                         <dd id="myStdDesign">-</dd> -->
<!--                     </dl> -->
	    </div>
	    <div class="myP_chart">
	        <div class="chart_box col-12 col-lg-3">
	            <h5 class="mb-3">나의 학점</h5>
	            <div class="box">
	            	<canvas id="chartCDT" height="160px"></canvas>
	            </div>
	        </div>
	        <div class="chart_box col-12 col-lg-3">
	            <h5 class="mb-3">나의 성적</h5>
	            <div class="box">
	            	<canvas id="chartGPA" height="160px"></canvas>	
	            </div>
	        </div>
	        <div class="chart_box col-12 col-lg-6">
	            <h5 class="mb-3">학점 상세</h5>
	            <div class="box">
	            	<canvas id="chartCdtDetail" height="160px"></canvas>
	            </div>
	        </div>
	    </div>
	</section>
	
	<section class="myP_tab_wrap">
	    <!--  ul class="nav border-0 nav-tabs mb-4" id="myTab" role="tablist">
	        <li class="nav-item col" role="presentation"><button class="nav-link text-center w-100 active" id="myDegr-tab" data-bs-toggle="tab" data-bs-target="#myDegr-tab-pane" type="button" role="tab" aria-controls="myDegr-tab-pane" aria-selected="true">학업이수현황</button></li>
	        <li class="nav-item col" role="presentation"><button class="nav-link text-center w-100" id="myLov-tab" data-bs-toggle="tab" data-bs-target="#myLov-tab-pane" type="button" role="tab" aria-controls="myLov-tab-pane" aria-selected="false">나의찜</button></li>
	        <li class="nav-item col" role="presentation"><button class="nav-link text-center w-100" id="shoBag-tab" data-bs-toggle="tab" data-bs-target="#shoBag-tab-pane" type="button" role="tab" aria-controls="shoBag-tab-pane" aria-selected="false">장바구니</button></li>
	        <li class="nav-item col" role="presentation"><button class="nav-link text-center w-100" id="hashTg-tab" data-bs-toggle="tab" data-bs-target="#hashTg-tab-pane" type="button" role="tab" aria-controls="hashTg-tab-pane" aria-selected="false">해시태그설정</button></li>
	    </ul -->
        <div class="tab-content" id="myTabContent">
            <div class="tab-pane fade show active myP_degrr_cont" id="myDegr-tab-pane" role="tabpanel" aria-labelledby="myDegr-tab" tabindex="0">
                <!--전체 탭 컨텐츠-->
                <section class="table_item mb-5">
                    <h5 class="">소요학점</h5>
                    <div class="credits_required">
                        <div class="table_area table_section_type01" id="tableReqCDT"></div>
		                <div class="req_credit_chart_box"><canvas id="chartReqCDT" height="160px"></canvas></div>		                            
                     </div>
                     <span>※ 소요학점은 참고용이며, 정확하지 않을 수 있습니다. 정확한 내용은 『종합정보시스템 > 학적 > 학생정보조회』을 참고하세요.</span>
                </section>
                
                <div class="completion status">
                	<section class="table_item mb-5 accum_chek">
	                    <h5 class="pb-2" id="tableCumCDTExcel">학기별 성적</h5>
	                    <div class="table_area" id="tableCumCDT"></div>
	                </section>
	                
					<section class="table_item mb-5 accum_chek">
	                    <h5 class="pb-2" id="tableGradReqExcel">졸업인증자격</h5>
	                    <div class="table_area" id="tableGradReq"></div>
                	</section>
                </div>
                
                <div class="completion status">
                	<section class="table_item mb-5 accum_chek">
	                    <h5 class="pb-2" id="tableMajorReqExcel">필수 이수현황(전공)</h5>
	                    <div class="table_area" id="tableMajorReq"></div>
	                </section>
	                
					<section class="table_item mb-5 accum_chek">
	                    <h5 class="pb-2" id="tableMinorReqExcel">필수 이수현황(교양)</h5>
	                    <div class="table_area" id="tableMinorReq"></div>
                	</section>
                </div>
                
                <section class="table_item mb-5 nonSbjt_chek">
                    <h5 class="d-flex flex-row align-items-center" id="tableNonSbjtHistExcel">비교과 프로그램 신청이력</h5>
                    <div class="table_area" id="tableNonSbjtHist"></div>
                </section>
                
                <section class="table_item mb-5 perf_chek">
                    <h5 class="d-flex flex-row align-items-center">과목별 성적 <span class="retake_i" id="tableSubjectCDTExcel">재수강가능</span></h5>
                    <div class="table_area" id="tableSubjectCDT"></div>
                </section>		                
                
                <section class="table_item perf_chek">
                    <h5 class="pb-2" id="tableRecHistExcel">학적변동내역</h5>
                    <div class="table_area" id="tableRecHist"></div>
                </section>
                
            </div>
            		            
           </div>	            		        
	</section>              
</section>


<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>
