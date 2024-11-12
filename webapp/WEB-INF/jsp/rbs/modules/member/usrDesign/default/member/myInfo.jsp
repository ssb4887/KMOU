<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>

<c:set var="inputFormId" value="fn_memberInputForm"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="items" value="${itemInfo.items}"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="menuName" value="${crtMenu.menu_name}"/>


<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
 		<jsp:param name="javascript_page" value="${moduleJspRPath}/myInfo.jsp"/>
 		<jsp:param name="inputFormId" value="${inputFormId}"/>
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
	
	.btn_wrap {
	    display: flex;
	    align-items: center;
	    gap: 10px;
	}
	.btn_dele, .btn_put {
	    width: 30px;
	    height: 30px;
	    display: flex;
	    align-items: center;
	    justify-content: center;
	    border-radius: 50%;
	    border: 1px solid #ddd;
	    background-color: #fff;
	    cursor: pointer;
	}
	.btn_dele svg, .btn_put svg {
	    width: 18px;
	    height: 18px;
	}	
	
	.modal_enrollSubj .pre_appl {
	    position: relative;
	}	
	
	.modal_enrollSubj .pre_appl:before {
	    content: '';
	    display: block;
	    width: 25px;
	    height: 16px;
	    background: url(../images/arr_b_tail.png) no-repeat;
	    background-size: 100%;
	    position: absolute;
	    top: 50%;
	    left: -2.70rem;
	    transform: translateY(-50%);	
	}
	
	.modal_enrollSubj .modal-body {
	  max-height: 80vh;
	  overflow-y: auto;
	  display: flex;
	  flex-direction: column;
	}
	
	.modal_enrollSubj .box {
	  flex: 1;
	  display: flex;
	  flex-direction: column;
	  max-height: calc(50% - 1rem);
	}
	
	.modal_enrollSubj .lesson_wrap {
	  flex: 1;
	  overflow-y: auto;
	}
	
	.modal_enrollSubj .bottom_btn_group {
	  position: sticky;
	  bottom: 0;
	  background-color: white;
	  padding-top: 0.5rem;
	  z-index: 1;
	}	
	
    /* 모달 컨텐츠의 최대 높이 설정 */
    .modal-content {
        max-height: 90vh;
        overflow-y: auto;
    }

    /* 모달 본문 레이아웃 수정 */
    .modal-body {
        display: flex;
        flex-wrap: nowrap; /* 박스들이 한 줄에 유지되도록 변경 */
        gap: 2rem; /* 화살표를 위한 공간 확보 */
        padding: 1rem;
        max-height: 80vh;
        overflow-y: auto;
    }

    /* 박스 스타일 수정 */
    .box {
        flex: 1; /* 동일한 너비로 설정 */
        min-width: 0; /* flex-basis 오버라이드 방지 */
        max-height: 100%; /* 부모 높이에 맞춤 */
        overflow-y: auto;
        display: flex;
        flex-direction: column;
        padding: 1rem;
        border-radius: 0.5rem;
    }

    /* lesson_wrap 내부 스크롤 */
    .lesson_wrap {
        flex: 1; /* 남은 공간 모두 차지 */
        overflow-y: auto;
        min-height: 0; /* flex 컨테이너 내에서 스크롤 가능하게 함 */
    }


    .modal_enrollSubj .pre_appl:before {
        content: '';
        display: block;
        width: 25px;
        height: 16px;
        background: url(../images/arr_b_tail.png) no-repeat;
        background-size: 100%;
        position: absolute;
        top: 50%;
        left: -2rem; /* 위치 조정 */
        transform: translateY(-50%) rotate(-90deg); /* 가로 방향으로 회전 */
    }

    /* 하단 버튼 그룹 스타일 */
    .bottom_btn_group {
        position: sticky;
        bottom: 0;
        background-color: white;
        padding-top: 0.5rem;
        z-index: 1;
    }

	/* 반응형 레이아웃을 위한 미디어 쿼리 수정 */
	@media (max-width: 768px) {
	  .modal-body {
	    flex-direction: column;
	  }
	  .box {
	    width: 100%;
	  }
	  .modal_enrollSubj .pre_appl:before {
	    display: none;
	  }
	}
	
	@media (min-width: 769px) {
	  .modal-body.few-items {
	    flex-direction: column;
	  }
	  .modal-body.few-items .box {
	    width: 100%;
	    max-width: 100%;
	  }
	  .modal-body.few-items .modal_enrollSubj .pre_appl:before {
	    display: none;
	  }
	}
    
    
    .reorder-buttons {
    display: inline-flex;
    flex-direction: column;
    margin-left: 10px;
	}
	
	.reorder-buttons button {
	    background: none;
	    border: none;
	    cursor: pointer;
	    padding: 5px;
	    font-size: 14px;
	}
	
	.reorder-buttons button:hover {
	    color: #007bff;
	}
	
	#toggleReorder.active, #saveOrder {
	    background-color: #28a745;
	    color: white;
	}
	
	#saveOrder {
	    display: none;
	}
</style>
<div class="mask2"></div>
<div id="overlay" style="display:none;"></div>
<div class="loader" style="display:none;"></div>


<form id="sbjtView" name="sbjtView" method="post" action="../sbjt/view.do?mId=32">
	<input type="hidden" name="mId" id="mId" value="32"/>
	<input type="hidden" name="SUBJECT_CD" id="SUBJECT_CD" value=""/>
	<input type="hidden" name="DEPT_CD" id="DEPT_CD" value=""/>
	<input type="hidden" name="YEAR" id="YEAR" value=""/>
	<input type="hidden" name="SMT" id="SMT" value=""/>
</form>
<form id="majorView" name="majorView" method="post" action="">
	<input type="hidden" name="mId" value="35">
	<input type="hidden" id="MAJOR_CD" name="MAJOR_CD" value="">
</form>
<form id="nonSbjtView" name="nonSbjtView" method="post">
	<input type="hidden" name="mId" value="34">
	<input type="hidden" name="idx" value="">
	<input type="hidden" name="tidx" value="">
</form>
<form id="sdmView" name="sdmView" method="post" action="">
	<input type="hidden" name="mId" value="36">
	<input type="hidden" id="SDM_CD" name="SDM_CD" value="">
	<input type="hidden" id="REVSN_NO" name="REVSN_NO" value="">
</form>
<form id="profView" name="profView" method="POST" action="">
	<input type="hidden" name="mId" class="mId" value="33"/>
	<input type="hidden" name="empNo" id="empNo" value=""/>
</form>

		<div class="sub_background mypage_bg">
		    <section class="inner">
		        <h3 class="title fw-bolder text-center text-white">마이페이지</h3>
				<p class="sub_title_script">성적 및 이수사항을 확인할 수 있습니다.</p>
		    </section>
		</div>
		<!--본문-->
		<section class="inner mt-5">
		    <!--제목-->
			<section class="d-flex flex-wrap gap-4">
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
			    <ul class="nav border-0 nav-tabs mb-4" id="myTab" role="tablist">
			        <li class="nav-item col" role="presentation"><button class="nav-link text-center w-100 active" id="myDegr-tab" data-bs-toggle="tab" data-bs-target="#myDegr-tab-pane" type="button" role="tab" aria-controls="myDegr-tab-pane" aria-selected="true">학업이수현황</button></li>
			        <li class="nav-item col" role="presentation"><button class="nav-link text-center w-100" id="myLov-tab" data-bs-toggle="tab" data-bs-target="#myLov-tab-pane" type="button" role="tab" aria-controls="myLov-tab-pane" aria-selected="false">나의찜</button></li>
			        <li class="nav-item col" role="presentation"><button class="nav-link text-center w-100" id="shoBag-tab" data-bs-toggle="tab" data-bs-target="#shoBag-tab-pane" type="button" role="tab" aria-controls="shoBag-tab-pane" aria-selected="false">장바구니</button></li>
			        <li class="nav-item col" role="presentation"><button class="nav-link text-center w-100" id="hashTg-tab" data-bs-toggle="tab" data-bs-target="#hashTg-tab-pane" type="button" role="tab" aria-controls="hashTg-tab-pane" aria-selected="false">해시태그설정</button></li>
			    </ul>
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
		                


		                <!--section class="table_item mb-5 accum_chek">
		                    <h5 class="pb-2">누계성적조회</h5>
	                    	<div class="table_area table_section_type02 tabulator" id="tableReqCDT" role="grid" tabulator-layout="fitColumns">
	                    		<div class="tabulator-header" role="rowgroup">
	                    			<div class="tabulator-header-contents" role="rowgroup">
	                    				<div class="tabulator-headers" role="row">
	                    					<div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="GUBUN">
	                    						<div class="tabulator-col-content">
	                    							<div class="tabulator-col-title-holder">
	                    								<div class="tabulator-col-title col-3">학년도/학기</div>
	                    							</div>
	                    						</div>
	                    					</div>
	                    					<span class="tabulator-col-resize-handle"></span>
	                    					<div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="BASE_CDT">
	                    						<div class="tabulator-col-content">
	                    							<div class="tabulator-col-title-holder">
	                    								<div class="tabulator-col-title col-3">신청학점</div>
	                    							</div>
	                    						</div>
	                    					</div>
                    						<span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="DONE_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">취득학점</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">평점</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="DONE_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">백분율</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="DONE_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">석차</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="DONE_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">F제외평점</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="DONE_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">비고</div>
						                    		</div>
						                    	</div>
						                    </div>
					                    </div>
						                <div class="tabulator-frozen-rows-holder"></div>
				                	</div>
			                	</div>
			                    <div class="tabulator-tableholder" tabindex="0">
			                    	<div class="tabulator-table" role="rowgroup">
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">17</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">17</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.53</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">88.3</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">31/51</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.62</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">-</div>
					                    </div>
					                    <div class="tabulator-row tabulator-selectable tabulator-row-even" role="row">
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">17</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">17</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.53</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">88.3</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">31/51</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.62</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">-</div>
					                   	</div>
					                    <div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">17</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">17</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.53</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">88.3</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">31/51</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.62</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">-</div>
			                    		</div>
					                    <div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">17</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">17</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.53</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">88.3</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">31/51</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.62</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">-</div>
			                    		</div>
					                    <div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">17</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">17</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.53</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">88.3</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">31/51</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.62</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">-</div>
			                    		</div>
					                    <div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">17</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">17</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.53</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">88.3</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">31/51</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.62</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">-</div>
			                    		</div>
					                    <div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">17</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">17</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.53</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">88.3</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">31/51</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.62</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">-</div>
			                    		</div>
					                    <div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">17</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">17</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.53</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">88.3</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">31/51</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">3.62</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">-</div>
			                    		</div>
			                    	</div>
			                    </div>
	                    	</div>
	                	</section>
	                	
						<section class="table_item mb-5 accum_chek">
		                    <h5 class="pb-2">성적조회</h5>
	                    	<div class="table_area table_section_type04 tabulator" id="tableReqCDT" role="grid" tabulator-layout="fitColumns">
	                    		<div class="tabulator-header" role="rowgroup">
	                    			<div class="tabulator-header-contents" role="rowgroup">
	                    				<div class="tabulator-headers" role="row">
	                    					<div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="GUBUN">
	                    						<div class="tabulator-col-content">
	                    							<div class="tabulator-col-title-holder">
	                    								<div class="tabulator-col-title col-3">연도/학기</div>
	                    							</div>
	                    						</div>
	                    					</div>
	                    					<span class="tabulator-col-resize-handle"></span>
	                    					<div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="BASE_CDT">
	                    						<div class="tabulator-col-content">
	                    							<div class="tabulator-col-title-holder">
	                    								<div class="tabulator-col-title col-3">과목코드번호</div>
	                    							</div>
	                    						</div>
	                    					</div>
                    						<span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="DONE_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">교과목명</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">이수구분</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">이수영역</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">학점</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">등급</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">성적유효구분</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">입력 및 수정일차</div>
						                    		</div>
						                    	</div>
						                    </div>
					                    </div>
						                <div class="tabulator-frozen-rows-holder"></div>
				                	</div>
			                	</div>
			                    <div class="tabulator-tableholder" tabindex="0">
			                    	<div class="tabulator-table" role="rowgroup">
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    	</div>
			                    </div>
	                    	</div>
	                	</section>
	                	
	                	
	                	
	                	<!-- section class="table_item mb-5 accum_chek">
		                    <h5 class="pb-2">이수학년도</h5>
	                    	<div class="table_area table_section_type05 tabulator" id="tableReqCDT" role="grid" tabulator-layout="fitColumns">
	                    		<div class="tabulator-header" role="rowgroup">
	                    			<div class="tabulator-header-contents" role="rowgroup">
	                    				<div class="tabulator-headers" role="row">
	                    					<div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="GUBUN">
	                    						<div class="tabulator-col-content">
	                    							<div class="tabulator-col-title-holder">
	                    								<div class="tabulator-col-title col-3">학년</div>
	                    							</div>
	                    						</div>
	                    					</div>
	                    					<span class="tabulator-col-resize-handle"></span>
	                    					<div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="BASE_CDT">
	                    						<div class="tabulator-col-content">
	                    							<div class="tabulator-col-title-holder">
	                    								<div class="tabulator-col-title col-3">학기</div>
	                    							</div>
	                    						</div>
	                    					</div>
                    						<span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="DONE_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3 first_title">성적</div>
						                    			<div class="tabulator-col-title col-3">이수년도</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">적용년도</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">복수전공1</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">복수전공2</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3 first_title">졸업사정</div>
						                    			<div class="tabulator-col-title col-3">연계전공</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">융합전공</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <span class="tabulator-col-resize-handle"></span>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">학생설계전공</div>
						                    		</div>
						                    	</div>
						                    </div>
						                    <div class="tabulator-col" role="columnheader" aria-sort="none" tabulator-field="TAKING_CDT">
						                    	<div class="tabulator-col-content">
						                    		<div class="tabulator-col-title-holder">
						                    			<div class="tabulator-col-title col-3">융합(공유)전공</div>
						                    		</div>
						                    	</div>
						                    </div>
					                    </div>
						                <div class="tabulator-frozen-rows-holder"></div>
				                	</div>
			                	</div>
			                    <div class="tabulator-tableholder" tabindex="0">
			                    	<div class="tabulator-table" role="rowgroup">
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    		<div class="tabulator-row tabulator-selectable tabulator-row-odd" role="row">
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="GUBUN">2023-1학기</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="BASE_CDT">152590-103</div>
			                    			<div class="tabulator-cell" role="gridcell" tabulator-field="DONE_CDT">색채의이해</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">균형</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">(균형)자연과학기술영역</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">2</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">A+</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">Y</div>
						                    <div class="tabulator-cell" role="gridcell" tabulator-field="TAKING_CDT">YYYY-MM-DD</div>
					                    </div>
			                    	</div>
			                    </div>
	                    	</div>
	                	</section-->
		                
		                <section class="table_item perf_chek">
		                    <h5 class="pb-2" id="tableRecHistExcel">학적변동내역</h5>
		                    <div class="table_area" id="tableRecHist"></div>
		                </section>
		                
		            </div>
		            
		            <div class="tab-pane fade myP_favorit" id="myLov-tab-pane" role="tabpanel" aria-labelledby="myLov-tab" tabindex="0">
		                <!--나의찜 탭 컨텐츠-->
		                <ul class="btn_wrap d-flex flex-wrap align-items-center justify-content-center" id="tabInfo">
		                    <li><button type="button" id="sbjtTab" class="rounded-pill d-flex flex-row justify-content-between bg-white align-items-center bookmarkTab">교과목(전공&middot;교양)<span id="sbjtCount">0</span></button></li>
		                    <li><button type="button" id="lecTab" class="rounded-pill d-flex flex-row justify-content-between bg-white align-items-center bookmarkTab">강좌<span id="lectureCount">0</span></button></li>
		                    <li><button type="button" id="nonSbjtTab" class="rounded-pill d-flex flex-row justify-content-between bg-white align-items-center bookmarkTab">비교과<span id="nonSbjtCount">0</span></button></li>
		                    <li><button type="button" id="profTab" class="rounded-pill d-flex flex-row justify-content-between bg-white align-items-center bookmarkTab">교수<span id="profCount">0</span></button></li>
		                    <li><button type="button" id="majorTab" class="rounded-pill d-flex flex-row justify-content-between bg-white align-items-center bookmarkTab">전공<span id="majorCount">0</span></button></li>
		                    <li><button type="button" id="studPlanTab" class="rounded-pill d-flex flex-row justify-content-between bg-white align-items-center bookmarkTab">학생설계<span id="studPlanCount">0</span></button></li>
		                </ul>
		                <div id="bookmarkConList">
		                </div>				              
		            </div>
		            <div class="tab-pane fade myP_add_hash" id="hashTg-tab-pane" role="tabpanel" aria-labelledby="hashTg-tab" tabindex="0">
		                <!--해시태그설정 탭 컨텐츠-->
		                <div class="input-group justify-content-center">
		                    <label for="hashRegi" class="blind">해시태그설정</label>
		                    <input type="input" class="form-control" id="hashInput" placeholder="원하는 키워드를 추가하세요">
		                    <button type="button" class="text-white border-0 px-3" id="hashRegi">해시태그 추가</button>
		                </div>
		                <ul class="p-4 d-flex flex-wrap gap-2 mt-4" id="hashtagList"></ul>
		            </div>
		            <div class="tab-pane fade myP_shopp" id="shoBag-tab-pane" role="tabpanel" aria-labelledby="shoBag-tab" tabindex="0">
		                <!--장바구니 탭 컨텐츠-->
		                <section class="open_history">
<!-- 					        <div class="d-flex flex-row gap-2 gap-sm-3 justify-content-end col-12 mb-3"> -->
<!-- 					            <select class="form-select rounded-pill" id="hisYear" aria-label="select"> -->
<!-- 					            	<option value="all" selected>개설년도 전체</option> -->
<!-- 					                옵션들은 JavaScript로 추가 -->
<!-- 					            </select>  -->
<!-- 					            <select class="form-select rounded-pill" id="hisSemi" aria-label="select"> -->
<!-- 					                <option value="all" selected>개설학기 전체</option> -->
<!-- 					                <option value="GH0210" >1학기</option> -->
<!-- 					                <option value="GH0220" >2학기</option> -->
<!-- 					                <option value="GH0211" >여름계절학기</option> -->
<!-- 					                <option value="GH0221" >겨울계절학기</option> -->
<!-- 					            </select> -->
<!-- 					        </div>	                 -->
		                    <div class="d-flex flex-wrap gap-4" id="basketList">
		                       
		                    </div>
		                    
		                    <section class="all_about_wrap d-flex flex-wrap justify-content-between mt-3 gap-2">
		                        <div class="d-flex gap-2 mycart_btonset01">
		                            <button type="button" class="all_check text-white border-0 d-flex flex-row align-items-center gap-1" onclick="cartAllChk(true)">
		                                <i></i>전체선택</button>
		                            <button type="button" class="remove_check" onclick="cartAllChk(false)">선택 일괄해제</button>
		                        </div>
		                        <div class="d-flex gap-2 mycart_btonset02">
		                            <button type="button" class="view_timeTable d-flex flex-row align-items-center gap-1 bg-white" data-bs-toggle="modal" id="showTimeTable">
		                                <img src="../images/ico_b_cal.png" alt="달력 아이콘"/>시간표 보기</button>
		                            <button type="button" class="save_applClass d-flex flex-rwo align-items-center gap-1 bg-white text-start word_keep"  data-bs-toggle="modal" id="showApplTimeTable">
		                                <img src="../images/ico_sk_sho.png" alt="장바구니 아이콘"/> 수강신청시스템 장바구니 등록
		                            </button>
		                            <button type="button" class="all_remove d-flex flex-row align-items-center gap-1 text-white border-0" id="cartSelectedDel">
		                                <i></i> 선택 장바구니 비우기</button>
		                        </div>
		                	</section>
		
		
		                    <!--paging-->
		                    <ul class="pagination gap-2 justify-content-center mt-5" id="page">
		                        <!-- <li class="page-item page-first">
		                            <a class="page-link" href="#" title="first">
		                                <span class="blind">first</span>
		                                <img src="../images/arr_2x_gray.png" alt="처음으로 화살표"/>
		                            </a>
		                        </li>
		                        <li class="page-item page-prev">
		                            <a class="page-link" href="#" title="prev">
		                                <span class="blind">prev</span>
		                                <img src="../images/arr_bottom_gray.png" alt="이전으로 화살표"/>
		                            </a>
		                        </li>
		                        <li class="page-item active"><a class="page-link" href="#" title="1">1</a></li>
		                        <li class="page-item"><a class="page-link" href="#" title="2">2</a></li>
		                        <li class="page-item"><a class="page-link" href="#" title="3">3</a></li>
		                        <li class="page-item"><a class="page-link" href="#" title="4">4</a></li>
		                        <li class="page-item"><a class="page-link" href="#" title="5">5</a></li>
		                        <li class="page-item"><a class="page-link" href="#" title="6">6</a></li>
		                        <li class="page-item"><a class="page-link" href="#" title="7">7</a></li>
		                        <li class="page-item"><a class="page-link" href="#" title="8">8</a></li>
		                        <li class="page-item"><a class="page-link" href="#" title="9">9</a></li>
		                        <li class="page-item page-next">
		                            <a class="page-link" href="#" title="next">
		                                <span class="blind">next</span>
		                                <img src="../images/arr_bottom_gray.png" alt="다음으로 화살표"/>
		                            </a>
		                        </li>
		                        <li class="page-item page-last">
		                            <a class="page-link" href="#" title="last">
		                                <span class="blind">last</span>
		                                <img src="../images/arr_2x_gray.png" alt="이전으로 화살표"/>
		                            </a>
		                        </li> -->
		                    </ul>
		                </section>
		            </div>
	            </div>	            		        
			</section>              
		</section>
	</div>
</div>


<!-- Modal -->
<div class="modal fade modal-xl modal_syllabus" id="syllabusModal" tabindex="-1" aria-labelledby="syllabusModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header bg-black">
            <h1 class="modal-title fs-5 text-white" id="syllabusModalLabel">객체지향프로그래밍 [000000]</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                <img src="../images/ico_w_close.png" alt="닫기 아이콘"/>
            </button>
        </div>
        <div class="modal-body">
            <section class="mt-4">
                <h2 class="d-flex justify-content-between align-items-center mb-3">수업시간표 및 강의계획서<a href="#" title="강의계획서" class="d-flex flex-row align-items-center gap-2"><img src="../images/modal_ico_w_doc.png" alt="계획서 아이콘"/>강의계획서</a></h2>
                <table class="table">
                    <caption class="blind">수업계획표</caption>
                    <colgroup>
                        <col width="10%">
                        <col width="15%">
                        <col width="10%">
                        <col width="15%">
                        <col width="10%">
                        <col width="15%">
                        <col width="10%">
                        <col width="15%">
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="row">교과목명</th>
                            <td colspan="3" id="sbjtName"></td>
                            <th scope="row">교과목명(영문)</th>
                            <td colspan="3" id="sbjtEName" ></td>
                        </tr>
                        <tr>
                            <th scope="row">담당교수</th>
                            <td id="empNm"></td>
                            <th scope="row">주야</th>
                            <td id="classNm"></td>
                            <th scope="row">학년</th>
                            <td id="grade"></td>
                            <th scope="row">외국어강의</th>
                            <td id="engYn"></td>
                        </tr>
                        <tr>
                            <th scope="row">수강대상대학(원)</th>
                            <td id="college"></td>
                            <th scope="row">수강대상학과</th>
                            <td id="deptNm"></td>
                            <th scope="row">학수번호</th>
                            <td id="subjectCd"></td>
                            <th scope="row">분반</th>
                            <td id="divcls"></td>
                        </tr>
                         <tr>
                            <th scope="row">과목구분</th>	
                            <td id="sinbun"></td>
                            <th scope="row">수업방식</th>
                            <td id="lectureWay"></td>
                            <th scope="row">학점</th>
                            <td id="cdtNum"></td>
                            <th scope="row">공학인증구분</th>
                            <td></td>
                        </tr>
                        <tr><th scope="row">정원</th>
                            <td id="fixNumber"></td>
                            <th scope="row">강의시간/강의실</th>
                            <td id="timeRoom"></td>
                        </tr>
                        <tr>
                            <th scope="row">공학인증 
                                <span class="ttip d-inline-flex justify-content-center align-items-center" data-bs-toggle="tooltip" data-bs-placement="right" data-bs-title="FL, BL, PBL, TBL, ME, AL 설명">?</span>
                            </th>
                            <td colspan="7">
                                <table class="table in_table">
                                    <caption class="blind">공학인증</caption>
                                    <colgroup>
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th scope="col">FL</th>
                                            <th scope="col">BL</th>
                                            <th scope="col">PBL</th>
                                            <th scope="col">TBL</th>
                                            <th scope="col">ME</th>
                                            <th scope="col">AL</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>N</td>
                                            <td>N</td>
                                            <td>N</td>
                                            <td>N</td>
                                            <td>N</td>
                                            <td>N</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row">핵심역량</th>
                            <td colspan="7">
                                <table class="table in_table">
                                    <caption class="blind">핵심역량</caption>
                                    <colgroup>
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th scope="col">주역량</th>
                                            <th scope="col">부역량1</th>
                                            <th scope="col">부역량2</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>주도적 학습</td>
                                            <td>통섭적 사고</td>
                                            <td>사회적 실천</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </section>

            <!-- <section class="mt-4">
                <h2 class="d-flex justify-content-between align-items-center mb-3">강의평가</h2>
                <table class="table border-top appr_table">
                    <caption class="blind">강의평가</caption>
                    <colgroup>
                        <col width="13%">
                        <col width="13%">
                        <col width="13%">
                        <col width="13%">
                        <col width="13%">
                        <col width="13%">
                        <col width="auto">
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="col" class="text-center">년도/학기</th>
                            <th scope="col" class="text-center">교과목명</th>
                            <th scope="col" class="text-center">학수번호/분반</th>
                            <th scope="col" class="text-center">수강인원</th>
                            <th scope="col" class="text-center">평가인원</th>
                            <th scope="col" class="text-center">강좌평균</th>
                            <th scope="col" class="text-center border-end-0">비고</th>
                        </tr>
                    </thead>
                    <tbody id="evalList">
                        <tr>
                            <td class="year_semi text-start text-lg-center py-2"></td>
                            <td class="name text-start text-lg-center py-2"></td>
                            <td class="numb text-start text-lg-center py-2"></td>
                            <td class="pepl text-start text-lg-center py-2"></td>
                            <td class="app_pepl text-start text-lg-center py-2"></td>
                            <td class="avera text-start text-lg-center py-2"></td>
                            <td class="note text-start text-lg-center py-2"></td>
                        </tr>
                    </tbody>
                </table>
            </section> -->
        </div>
        <div class="modal-footer d-flex flex-row align-items-center justify-content-center ">
          <button type="button" class="p-3 d-flex flex-row align-items-center justify-content-center gap-2 bg-transparent"><img src="../images/modal_ico_r_lov.png" alt="찜하기 아이콘"/>찜하기</button>
          <button type="button" class="p-3 d-flex flex-row align-items-center justify-content-center gap-2 bg-transparent"><img src="../images/modal_ico_b_sho.png" alt="장바구니 아이콘"/>장바구니</button>
        </div>
      </div>
    </div>
  </div>
  <!--모달 - 시간표-->
<div class="modal modal-xl fade modal_timeTable" id="timeTable" tabindex="-1" aria-labelledby="timeTableLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header bg-black">
                <h1 class="modal-title fs-5 text-white" id="timeTableLabel">시간표</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                    <img src="../images/ico_w_close.png" alt="닫기 아이콘" />
                </button>
            </div>
            <div class="title_wrap d-flex flex-wrap justify-content-between align-items-end align-items-sm-center mb-2 mb-sm-3">
                <p class="ms-auto" id="isMerged" style="display: none;">빨간색으로 표시된 시간표가 중복됩니다.</p>
            </div>
            <div class="modal-body">
                <div class="">
                    <table class="table">
                        <caption class="blind">시간표</caption>
                        <colgroup>
                            <col width="60px">
                            <col width="60px">
                            <col width="auto">
                            <col width="auto">
                            <col width="auto">
                            <col width="auto">
                            <col width="auto">
                            <col width="auto">
                            <col width="auto">
                        </colgroup>
                        <thead>
                            <tr>
                                <th scope="row">교시</th>
                                <th scope="row">시간</th>
                                <th scope="row">월</th>
                                <th scope="row">화</th>
                                <th scope="row">수</th>
                                <th scope="row">목</th>
                                <th scope="row">금</th>
                                <th scope="row">토</th>
                                <th scope="row">일</th>
                            </tr>
                        </thead>
                        <tbody id="timeTableBody">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>

<!-- 모달 - 수강신청 테이블 -->
<div class="modal fade modal-xl modal_enrollSubj " id="applTimeTable" aria-hidden="true" aria-labelledby="enrollSubjLabel" tabindex="-1" style="display: none;">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header bg-black">
				<h1 class="modal-title text-white modal_title_plstb" id="enrollSubjLabel">예비수강신청 장바구니 등록하기</h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" id="applTimeTableClose">
					<img src="../images/ico_w_close.png" alt="닫기 아이콘" />
				</button>
			</div>
			<div class="modal-body d-flex flex-wrap gap-5 p-4">
				<div class="box">
					<h2>나의 장바구니</h2>
					<!--하단 버튼모음-->
					<section class="bottom_btn_group d-flex flex-wrap justify-content-between mt-3 gap-2">
						<div class="d-flex flex-row gap-2 appl">
							<button type="button" class="all_check text-white border-0 d-flex flex-row align-items-center gap-1 btn-fg" value="beforeChk">
								<i></i>
								전체선택
							</button>
							<button type="button" class="remove_check d-inline-flex btn-fg" value="beforeClear">선택 일괄해제</button>
						</div>
						<div class="appl">
						  <button type="button" class="select_put text-white border-0" id="btnAddSbjt">
						    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none" style="transform: rotate(270deg);">
						      <g fill="#fff" clip-path="url(#a)">
						        <path d="M17.018.982A3.33 3.33 0 0 0 14.648 0H3.352A3.355 3.355 0 0 0 0 3.352v11.296A3.355 3.355 0 0 0 3.352 18h11.296A3.355 3.355 0 0 0 18 14.648V3.352a3.33 3.33 0 0 0-.982-2.37Zm-.073 13.666a2.3 2.3 0 0 1-2.297 2.297H3.352a2.3 2.3 0 0 1-2.297-2.297V3.352a2.3 2.3 0 0 1 2.297-2.297h11.296c.614 0 1.19.239 1.625.672.433.434.672 1.011.672 1.625v11.296Z" />
						        <path d="M12.169 7.646a.527.527 0 0 0-.746 0L9.527 9.54V3.916a.527.527 0 0 0-1.054 0v5.626L6.577 7.646a.527.527 0 1 0-.746.746l2.796 2.796a.527.527 0 0 0 .746 0l2.796-2.796a.527.527 0 0 0 0-.746ZM14.084 13.557H3.916a.527.527 0 0 0 0 1.054h10.168a.527.527 0 0 0 0-1.054Z" />
						      </g>
						      <defs>
						        <clipPath id="a">
						          <path fill="#fff" d="M0 0h18v18H0z" />
						        </clipPath>
						      </defs>
						    </svg>
						    <span class="text-center ps-1">선택 일괄담기</span>
						  </button>
						</div>
					</section>					
					<!--item wrap-->
					<div class="lesson_wrap" id="appndSbjt">

					</div>					

				</div>
				<!--우측 박스-->
				<div class="box">
					<!--타이틀-->
					<h2>예비수강신청 현황</h2>
					<!--하단 버튼모음-->
					<section class="bottom_btn_group d-flex flex-wrap justify-content-between mt-3 gap-2">
						<div class="d-flex flex-wrap gap-3 appl">
							<button type="button" class="all_check text-white border-0 d-flex flex-row align-items-center gap-1 btn-fg" value="afterChk">
								<i></i>
								전체선택
							</button>
							<button type="button" class="remove_check d-inline-flex btn-fg" value="afterClear" >선택 일괄해제</button>
							<button type="button" class="all_delet text-white d-flex align-items-center flex-row border-0" id="btnDelSbjt">
								<img src="../images/ico_w_close.png" alt="삭제 아이콘" class="d-inline-block" />
								<span class="text-center ps-1">선택 일괄삭제</span>
							</button>						
						</div>						
						<div class="d-flex flex-wrap gap-3 reorder hidden">
							<button type="button" class="all_check text-white border-0 d-flex flex-row align-items-center gap-1 btn-fg" id="saveOrder">
								<i></i>
								저장
							</button>
							<button type="button" class="remove_check d-inline-flex btn-fg" id="cancelOrder">
								<i></i>
								취소
							</button>							
						</div>						
						<div class="appl">
							<button type="button" class="select_put text-white border-0" id="reorderBtn">
								<img src="../images/ico_reload2.png" alt="순서변경 아이콘" class="d-inline-block" />
								<span class="text-center ps-1">순서변경</span>
							</button>

						</div>
					</section>
					<!--item wrap-->
					<div class="lesson_wrap" id="applSbjt">
					
						<div class="lesson_wrap no_contents_wrap d-flex justify-content-center align-items-center" id="no-data-img">
							<div class="d-flex flex-column justify-content-center align-items-center">
								<!-- img src="${contextPath}/${crtSiteId}/assets/images/kmou_noshad_big.png" alt="해양이" /-->
								<img src="../images/kmou_noshad_big.png" alt="해양이" />
								<p class="text-center mt-2">
									왼쪽 상자에서 원하는 교과목을<br />등록해주세요.
								</p>
							</div>
						</div>
					</div>
					

				</div>
			</div>
		</div>
	</div>
</div>
