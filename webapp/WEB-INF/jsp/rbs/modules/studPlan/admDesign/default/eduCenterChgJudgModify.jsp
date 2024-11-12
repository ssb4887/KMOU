<%@ include file="../../../../adm/include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<link rel="stylesheet" type="text/css" href="${contextPath}${cssAssetPath}/sub.css">
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/admScript/eduCenterChgJudgModify.jsp"/>
	</jsp:include>
</c:if>

<form id="frmView" name="frmView" method="post" action="">
	<input type="hidden" name="mId" value="48">
	<input type="hidden" id="SDM_CD" name="SDM_CD" value="">
	<input type="hidden" id="REVSN_NO" name="REVSN_NO" value="">
</form>

<div class="container_wrap">
	<div class="sub_wrap">
		<!--본문-->
		<section class="inner mt-5">
			<!--제목-->

			<!--학생설계전공 기본정보-->
			<section class="plan_dtl_section plan_dtl_basic" id="move_acitve1">
				<h4 class="mt20" style="font-size:30px;">${INFO.SDM_KOR_NM }(${INFO.STATUS_INF_NM })</h4>
				<div class="d-flex flex-wrap table_wrap">
						<table class="tbListB">
							<colgroup>
								<col width="15%" />
								<col width="35%" />
								<col width="15%" />
								<col width="35%" />
							</colgroup>
							<tbody>
								<tr>
									<th>전공 국문명</th>
									<td><c:out value="${INFO.SDM_KOR_NM}"/></td>
									<th>전공 영문명</th>
									<td><c:out value="${INFO.SDM_ENG_NM}"/></td>
								</tr>
								<tr>
									<th>운영 주관학과</th>
									<td><c:out value="${INFO.FIRST_MAJOR}"/></td>
									<th>운영 참여학과</th>
									<td><c:out value="${INFO.OTHER_MAJOR}"/></td>
								</tr>
								<tr>
									<th>수여학위 국문명</th>
									<td><c:out value="${INFO.AWD_DEGR_KOR_NM}"/></td>
									<th>수여학위 영문명</th>
									<td><c:out value="${INFO.AWD_DEGR_ENG_NM}"/></td>
								</tr>

								<tr>
									<th>관련 인재상</th>
									<td colspan="3"><c:out value="${INFO.IDEAL_STU_KOR}"/></td>
								</tr>
								<tr>
									<th>관련 직업분야</th>
									<td colspan="3"><c:out value="${INFO.CONC_JOB_KOR}"/></td>
								</tr>
								<tr>
									<th>관련 학문 분야</th>
									<td colspan="3"><c:out value="${INFO.CONC_STUD_FLD_KOR}"/></td>
								</tr>
								<tr>
									<th>요구되는 기초능력</th>
									<td colspan="3"><c:out value="${INFO.DMND_BASE_LRN_ABTY_KOR}"/></td>
								</tr>
								<tr>
									<th>이수계획</th>
									<td colspan="3"><c:out value="${INFO.CPTN_PLAN_KOR}"/></td>
								</tr>
								<tr>
									<th>기타 사항</th>
									<td colspan="3"><c:out value="${INFO.ETC_CTNT}"/></td>
								</tr>
								<tr>
									<th>변경사유</th>
									<td colspan="3"><c:out value="${INFO.SDM_CHG_RESN}"/></td>
								</tr>								
							</tbody>
						</table>
					</div>
			</section>			
			<!--기본정보-->
			<form name="frmPost" id="frmPost" action="" method="post">
				<input type="hidden" name="SDM_CD" value="${INFO.SDM_CD }" />
				<input type="hidden" name="REVSN_NO" value="${INFO.REVSN_NO }" />
				<input type="hidden" name="APLY_STUDENT_NO" value="${INFO.APLY_STUDENT_NO }"/>
				<input type="hidden" name="APLY_STUDENT_NM" value="${INFO.APLY_STUDENT_NM }"/>
				<input type="hidden" name="SDM_KOR_NM" value="${INFO.SDM_KOR_NM }"/>			
				<section class="plan_dtl_section plan_dtl_basic" id="move_acitve1">
					<div id="containInput"></div>
					<h4 class="mt20">신청학생정보</h4>	
					<div class="d-flex flex-wrap table_wrap">					
						<table class="tbListB">
							<colgroup>
								<col width="15%" />
								<col width="35%" />
								<col width="15%" />
								<col width="35%" />
							</colgroup>
							<tbody>
								<tr>
									<th>이름</th>
									<td><c:out value="${STUDENTINFO.NM}"/></td>
									<th>학번</th>
									<td><c:out value="${STUDENTINFO.STUDENT_NO}"/></td>
								</tr>
								<tr>
									<th>단과대학</th>
									<td><c:out value="${STUDENTINFO.COLL_NM}"/></td>
									<th>학부(과)-전공</th>
									<td><c:out value="${STUDENTINFO.DEPT_NM } - ${STUDENTINFO.MAJOR_NM }"/></td>
								</tr>
								<tr>
									<th>연락처</th>
									<td><c:out value="${STUDENTINFO.HP_NO }"/></td>
									<th>이메일</th>
									<td><c:out value="${STUDENTINFO.EMAIL }"/></td>
								</tr>
							</tbody>
						</table>
					</div>													
				</section>							

	            <!-- 교과목 hidden -->
				<div id="containInput"></div>									
				<!-- 교과목 설계범위 hidden -->
				<input type="hidden" name="SBJT_DGN_RNG_FG" value="${INFO.SBJT_DGN_RNG_FG }" id="checkedSbjtDgnRngFg">
				<input type="hidden" name="SBJT_DGN_RNG_FG_NM" value="${INFO.SBJT_DGN_RNG_FG_NM }" id="checkedSbjtDgnRngFgNm">
				
				<!--교육과정 신.구대비표-->
	            <section class="plan_dtl_section compari_curri">
                	<h4 class="mt20">교육과정 신&middot;구 대비표</h4>
                <!--pc-->
	                <table class="tbListC">
	                    <caption class="blind">교육과정 신.구 대비표</caption>
						<colgroup>
						    <col width="12%"/>
						    <col width="8%"/>
						    <col width="4%"/>
						    <col width="auto"/>
						    <col width="3%"/>
						    <col width="3%"/>
						    <col width="10%"/>
						    <col width="4%"/>
						    <col width="auto"/>
<!-- 						    <col width="4%"/> -->
						    <col width="3%"/>
						    <col width="3%"/>
<!-- 						    <col width="3%"/> -->
						    <col width="20%"/>
						</colgroup>
						<thead>
						    <tr>
						        <th scope="row" rowspan="3" class="fw-bold text-dark">변경내용</th>
						        <th scope="rowgroup" colspan="5" class="bord">변경 전 교육과정</th>
						        <th scope="rowgroup" colspan="5" class="after_change bord">변경 후 교육과정</th>
						        <th scope="row" rowspan="3" class="after_change">변경사유</th>
						    </tr>
						    <tr>
						        <th scope="row" rowspan="2" class="">구분</th>
						        <th scope="row" rowspan="2" class="">학년</th>
						        <th scope="row" rowspan="2"  class="">교과목명</th>
						        <th scope="rowgroup" colspan="2" class="bord">학점</th>
						        <th scope="row" rowspan="2" class="after_change">구분</th>
						        <th scope="row" rowspan="2" class="after_change">학년</th>
						        <th scope="row" rowspan="2"  class="after_change">교과목명</th>
						        <th scope="rowgroup" colspan="2" class="after_change bord">학점</th>
						    </tr>
						    <tr>
						        <th scope="row" class="">전</th>
						        <th scope="row" class="bord">후</th>
						        <th scope="row" class="after_change">전</th>
						        <th scope="row" class="after_change bord">후</th>
						    </tr>
						</thead>
	                    <tbody id="sbjtListPc">

	                    </tbody>
	                </table>
					<div id="containInput"></div>
                </section>
				<!--교과목 설계범위-->
				<section>
					<h4 class="mt20">교과목 설계범위</h4>
					<div class="plan_ipbx">
						${INFO.SBJT_DGN_RNG_FG_NM }
					</div>
				</section>					
				<!--교과목 설계범위 chart -->
				<section class="plan_dtl_section plan_range pt-4" id="move_acitve3">
					<h4 class="mt20">교육과정표</h4>
					<div class="t_flex">
						<div class="table_box">
							<table class="tbListC">
								<caption class="blind">교과목 설계범위 1학년</caption>
								<colgroup>
									<col width="15%">
									<col width="25%">
									<col width="25%">
									<col width="10%">
									<col width="10%">
								</colgroup>
								<thead>
									<tr>
										<th scope="colgroup" colspan="5">1학년</th>
									</tr>
									<tr>
										<th scope="col" rowspan="2" class="border-end">구분</th>
										<th scope="col" rowspan="2" class="border-end">개설전공</th>
										<th scope="col" rowspan="2" class="border-end">과목명</th>
										<th scope="colgroup" colspan="2">학점</th>
									</tr>
									<tr>
<!-- 										<th scope="col" class="border-end">전체</th> -->
										<th scope="col" class="border-end">전</th>
										<th scope="col">후</th>
									</tr>
								</thead>
								<tbody id="grade1">
								</tbody>
							</table>
						</div>
						<div class="table_box">
							<table class="tbListC">
								<caption class="blind">교과목 설계범위 2학년</caption>
								<colgroup>
									<col width="15%">
									<col width="25%">
									<col width="25%">
									<col width="10%">
									<col width="10%">
								</colgroup>
								<thead>
									<tr>
										<th scope="colgroup" colspan="5">2학년</th>
									</tr>
									<tr>
										<th scope="col" rowspan="2" class="border-end">구분</th>
										<th scope="col" rowspan="2" class="border-end">개설전공</th>
										<th scope="col" rowspan="2" class="border-end">과목명</th>
										<th scope="colgroup" colspan="2">학점</th>
									</tr>
									<tr>
<!-- 										<th scope="col" class="border-end">전체</th> -->
										<th scope="col" class="border-end">전</th>
										<th scope="col">후</th>
									</tr>
								</thead>
								<tbody id="grade2">
								</tbody>
							</table>
						</div>
						<div class="table_box">
							<table class="tbListC">
								<caption class="blind">교과목 설계범위 3학년</caption>
								<colgroup>
									<col width="15%">
									<col width="25%">
									<col width="25%">
									<col width="10%">
									<col width="10%">
								</colgroup>
								<thead>
									<tr>
										<th scope="colgroup" colspan="5">3학년</th>
									</tr>
									<tr>
										<th scope="col" rowspan="2" class="border-end">구분</th>
										<th scope="col" rowspan="2" class="border-end">개설전공</th>
										<th scope="col" rowspan="2" class="border-end">과목명</th>
										<th scope="colgroup" colspan="2">학점</th>
									</tr>
									<tr>
<!-- 										<th scope="col" class="border-end">전체</th> -->
										<th scope="col" class="border-end">전</th>
										<th scope="col">후</th>
									</tr>
								</thead>
								<tbody id="grade3">
								</tbody>
							</table>
						</div>
						<div class="table_box">
							<table class="tbListC">
								<caption class="blind">교과목 설계범위 4학년</caption>
								<colgroup>
									<col width="15%">
									<col width="25%">
									<col width="25%">
									<col width="10%">
									<col width="10%">
								</colgroup>
								<thead>
									<tr>
										<th scope="colgroup" colspan="5">4학년</th>
									</tr>
									<tr>
										<th scope="col" rowspan="2" class="border-end">구분</th>
										<th scope="col" rowspan="2" class="border-end">개설전공</th>
										<th scope="col" rowspan="2" class="border-end">과목명</th>
										<th scope="colgroup" colspan="2">학점</th>
									</tr>
									<tr>
<!-- 										<th scope="col" class="border-end">전체</th> -->
										<th scope="col" class="border-end">전</th>
										<th scope="col">후</th>
									</tr>
								</thead>
								<tbody id="grade4">
								</tbody>
							</table>
						</div>
					</div>
<!-- 					<div class="dtl_bottom py-2 px-3 d-flex flex-column flex-sm-row align-items-start align-items-sm-center justify-content-between mt-3 gap-2"> -->
<!-- 						<div class="d-flex flex-row align-items-center gap-2 col-12 col-sm-auto"></div> -->
<!-- 						<div class="d-flex flex-row align-items-center gap-2 col-12 col-sm-auto"> -->
<!-- 							<h5>총합계</h5> -->
<!-- 							<p class="bg-white py-2 px-4"> -->
<!-- 								<span class="d-inline-block pe-1 totalPnt">0</span> -->
<!-- 								학점 -->
<!-- 							</p> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<section class="plan_dtl_section plan_total pt-4">
						<h4 class="mt20">설계학점 총계</h4>
						<table class="tbListA alignC">
							<caption class="blind">설계학점 총점</caption>
							<colgroup>
								<col width="10%" />
								<col width="30%" />
								<col width="30%" />
								<col width="15%" />
								<col width="15%" />
							</colgroup>
							<thead>
								<tr>
									<th scope="col">번호</th>
									<th scope="col">대학</th>
									<th scope="col">단과대학</th>
									<th scope="col">학부&middot;학과(전공)</th>
									<th scope="col">설계 학점</th>
								</tr>
							</thead>
							<tbody id="appndSumPnt">
							</tbody>
	                       <tfoot>
	                       	<tr>
	                       		<td colspan="5" class="plan_tf">총 설계학점 합계 <span class="totalPnt">0</span> 학점</td>
	                       	</tr>
	                       </tfoot>							
						</table>
						<input type="hidden" id="desTotCnt" value="" />
					</section>					
					<section class="plan_dtl_section plan_dtl_basic" style="margin:40px 0;">
						<div class="d-flex flex-wrap table_wrap">
							<table class="tbListB">
								<colgroup>
									<col width="15%"/>
									<col width="85%"/>
								</colgroup>
								<tbody>
									<tr>
										<c:if test="${!empty INFO.DEPART_JUDG_DT }">
										<th scope="col">학사과 검토 의견</th>
										<td style="text-align:left;">
											${INFO.DEPART_JUDG_OPIN }
										</td>
										</c:if>
									</tr>
									<tr>										
										<c:if test="${!empty INFO.AFFAIR_COMMITTEE_JUDG_DT }">
										<th scope="col">교무회 검토 의견</th>
										<td style="text-align:left;">
											${INFO.AFFAIR_COMMITTEE_JUDG_OPIN }
										</td>
										</c:if>
									</tr>
								</tbody>
							</table>						
							<h4 class="mt20">검토 의견</h4>
							<input type="hidden" name="JUDG_OPIN" value="">
							<table class="tbListB">
								<colgroup>
									<col width="15%"/>
									<col width="85%"/>
								</colgroup>
								<tbody>
									<tr>
										<td colspan="2">
											<textarea class="" style="width:100%;" rows="5" placeholder="<c:if test="${INFO.STATUS eq '40' }">반려 시 필수입력입니다.(승인 시 선택입력)</c:if>" id="judgOpin" <c:if test="${INFO.STATUS gt '40' }">disabled</c:if>>${INFO.EDU_CENTER_JUDG_OPIN }</textarea>
											<label for="judgOpin" class="blind">검토 의견</label>   
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</section>				
				</section>
			</form>
			<c:if test="${INFO.STATUS eq '40' }">
			<div class="btnCenter">
				<a href="javascript:" onclick="approve()" class="btnTypeA fn_btn_submit">승인</a>
				<a href="javascript:" onclick="reject()" class="btnTypeB">반려</a>
			</div>			
			</c:if>
		</section>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false" /></c:if>

<!--과목등록하기 modal-->
<div class="modal fade modal-xl modal_enrollSubj " id="enrollSubj" aria-hidden="true" aria-labelledby="enrollSubjLabel" tabindex="-1" style="display: none;">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header bg-black">
				<h1 class="modal-title fs-5 text-white" id="enrollSubjLabel">과목 등록하기</h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
					<img src="${contextPath}/${crtSiteId}/assets/images/ico_w_close.png" alt="닫기 아이콘" />
				</button>
			</div>
			<div class="modal-body d-flex flex-wrap gap-5">
				<div class="box">
					<h2>교과목 선택</h2>
					<div class="find_height">
						<ul class="nav nav-tabs" id="myTab" role="tablist">
							<li class="nav-item col-4" role="presentation">
								<button class="nav-link w-100 active" id="major-tab" data-bs-toggle="tab" data-bs-target="#major-tab-pane" type="button" role="tab" aria-controls="major-tab-pane" aria-selected="true">전공교과목</button>
							</li>
							<li class="nav-item col-4" role="presentation">
								<button class="nav-link w-100" id="myLove-tab" data-bs-toggle="tab" data-bs-target="#myLove-tab-pane" type="button" role="tab" aria-controls="myLove-tab-pane" aria-selected="false">나의찜목록</button>
							</li>
						</ul>
						<div class="tab-content" id="myTabContent">
							<div class="tab-pane fade show active" id="major-tab-pane" role="tabpanel" aria-labelledby="major-tab" tabindex="0">
								<!--전공교과목-->
								<div class="wrap p-4">
									<div class="d-flex flex-row align-items-center mb-3">
										<h5>대학/전공</h5>
										<div class="d-flex flex-row gap-3">
											<label class="blind">대학</label>
											<select class="form-select" id="selectColg">
											</select>
											<label class="blind">학부(과)</label>
											<select class="form-select" id="selectDept">
												<option value="" selected>학부(과)</option>
											</select>
											<label class="blind">전공</label>
											<select class="form-select" id="selectMj">
												<option value="" selected>전공</option>
											</select>
										</div>
									</div>
									<div class="d-flex flex-row align-items-center mb-3">
										<h5>이수구분</h5>
										<div class="">
											<ul class="d-flex flex-row">
												<li class="form-check  col-2">
													<input type="checkbox" id="chkMaj_all" class="form-check-input">
													<label class="form-check-label" for="chkMaj_all">전체</label>
												</li>
												<li class="form-check  col-3">
													<input type="checkbox" id="chkMaj_Requi" class="form-check-input chk-sbjt-search" value="UE010021">
													<label class="form-check-label" for="chkMaj_Requi">전공필수</label>
												</li>
												<li class="form-check  col-3">
													<input type="checkbox" id="chkMaj_Selc" class="form-check-input chk-sbjt-search" value="UE010022">
													<label class="form-check-label" for="chkMaj_Selc">전공선택</label>
												</li>
												<li class="form-check  col-3">
													<input type="checkbox" id="chkMaj_Basi" class="form-check-input chk-sbjt-search" value="UE010024">
													<label class="form-check-label" for="chkMaj_Basi">전공기초</label>
												</li>																							
											</ul>
											<ul class="d-flex flex-row">
												<li class="form-check  col-3 general-check-form" >
													<input type="checkbox" id="chkGen_Requi" class="form-check-input chk-sbjt-search" value="UE010011">
													<label class="form-check-label" for="chkGen_Requi">교양필수</label>
												</li>
												<li class="form-check  col-3 general-check-form" >
													<input type="checkbox" id="chkGen_Selc" class="form-check-input chk-sbjt-search" value="UE010012">
													<label class="form-check-label" for="chkGen_Selc">교양선택</label>
												</li>																							
											</ul>
										</div>
									</div>
									<div class="d-flex flex-row align-items-center">
										<h5>키워드</h5>
										<div class="">
											<input type="text" class="form-control" id="keyWord" placeholder="키워드 검색">
										</div>
									</div>
									<button type="button" class="border-0 text-white text-center w-100 d-block" id="searchSbjt">검색</button>
								</div>
							</div>
							<div class="tab-pane fade" id="myLove-tab-pane" role="tabpanel" aria-labelledby="myLove-tab" tabindex="0">
								<!--나의찜목록-->
								<div class="wrap p-4">
									<div class="d-flex flex-row align-items-center mb-3">
										<h5>대학/전공</h5>
										<div class="d-flex flex-row gap-3">
											<label class="blind">대학</label>
											<select class="form-select" id="selectColgMy">
											</select>
											<label class="blind">학부(과)</label>
											<select class="form-select" id="selectDeptMy">
												<option value="" selected>학부(과)</option>
											</select>
											<label class="blind">전공</label>
											<select class="form-select" id="selectMjMy">
												<option value="" selected>전공</option>
											</select>
										</div>
									</div>
									<div class="d-flex flex-row align-items-center mb-3">
										<h5>이수구분</h5>
										<div class="">
											<ul class="d-flex flex-row">
												<li class="form-check  col-2">
													<input type="checkbox" id="chkMaj_allMy" class="form-check-input">
													<label class="form-check-label" for="chkMaj_all">전체</label>
												</li>
												<li class="form-check  col-2">
													<input type="checkbox" id="chkMaj_RequiMy" class="form-check-input chk-sbjt-searchMy" value="UE010021">
													<label class="form-check-label" for="chkMaj_RequiMy">전공필수</label>
												</li>
												<li class="form-check  col-2">
													<input type="checkbox" id="chkMaj_SelcMy" class="form-check-input chk-sbjt-searchMy" value="UE010022">
													<label class="form-check-label" for="chkMaj_SelcMy">전공선택</label>
												</li>
												<li class="form-check  col-2">
													<input type="checkbox" id="chkMaj_BasiMy" class="form-check-input chk-sbjt-searchMy" value="UE010024">
													<label class="form-check-label" for="chkMaj_BasiMy">전공기초</label>
												</li>																								
											</ul>
											<ul class="d-flex flex-row">
												<li class="form-check  col-3 general-check-form" >
													<input type="checkbox" id="chkGen_RequiMy" class="form-check-input chk-sbjt-search" value="UE010011">
													<label class="form-check-label" for="chkGen_RequiMy">교양필수</label>
												</li>
												<li class="form-check  col-3 general-check-form" >
													<input type="checkbox" id="chkGen_SelcMy" class="form-check-input chk-sbjt-search" value="UE010012">
													<label class="form-check-label" for="chkGen_SelcMy">교양선택</label>
												</li>																							
											</ul>											
										</div>
									</div>
									<div class="d-flex flex-row align-items-center">
										<h5>키워드</h5>
										<div class="">
											<input type="text" class="form-control" id="keyWordMy" placeholder="키워드 검색">
										</div>
									</div>
									<button type="button" class="border-0 text-white text-center w-100 d-block" id="searchMySbjt">검색</button>
								</div>
							</div>
						</div>
					</div>
					<!--item wrap-->
					<div class="lesson_wrap" id="appndSbjt">
					</div>
					<!--하단 버튼모음-->
					<section class="bottom_btn_group d-flex flex-wrap justify-content-between mt-3 gap-2">
						<div class="d-flex flex-row gap-2">
							<button type="button" class="all_check text-white border-0 d-flex flex-row align-items-center gap-1 btn-fg" value="beforeChk">
								<i></i>
								전체선택
							</button>
							<button type="button" class="remove_check d-inline-flex btn-fg" value="beforeClear">선택 일괄해제</button>
						</div>
						<div>
							<button type="button" class="select_put text-white border-0" id="btnAddSbjt">
								<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none">
									<g fill="#fff" clip-path="url(#a)">
									<path d="M17.018.982A3.33 3.33 0 0 0 14.648 0H3.352A3.355 3.355 0 0 0 0 3.352v11.296A3.355 3.355 0 0 0 3.352 18h11.296A3.355 3.355 0 0 0 18 14.648V3.352a3.33 3.33 0 0 0-.982-2.37Zm-.073 13.666a2.3 2.3 0 0 1-2.297 2.297H3.352a2.3 2.3 0 0 1-2.297-2.297V3.352a2.3 2.3 0 0 1 2.297-2.297h11.296c.614 0 1.19.239 1.625.672.433.434.672 1.011.672 1.625v11.296Z" />
									<path d="M12.169 7.646a.527.527 0 0 0-.746 0L9.527 9.54V3.916a.527.527 0 0 0-1.054 0v5.626L6.577 7.646a.527.527 0 1 0-.746.746l2.796 2.796a.527.527 0 0 0 .746 0l2.796-2.796a.527.527 0 0 0 0-.746ZM14.084 13.557H3.916a.527.527 0 0 0 0 1.054h10.168a.527.527 0 0 0 0-1.054Z" /></g>
									<defs>
									<clipPath id="a">
									<path fill="#fff" d="M0 0h18v18H0z" /></clipPath></defs></svg>
								<span class="text-center ps-1">선택 일괄담기</span>
							</button>
						</div>
					</section>
				</div>
				<!--우측 박스-->
				<div class="box selected_box">
					<!--타이틀-->
					<div class="d-flex flex-row aling-items-center justify-content-between title_row mb-3">
						<h2 class="">설계 선택 목록 </h2>
						<p class="text-left ps-1" style="color:red;">※교육과정은 반드시 전공별 최소 9학점 이상으로 편성하셔야합니다.</p>
						<!-- 
						<div class="d-flex flex-row align-items-center ">
							<label for="orderSort">정렬순서</label>
							<select class="form-select" id="orderSort">
								<option selected>최신순</option>
								<option value="1">One</option>
								<option value="2">Two</option>
								<option value="3">Three</option>
							</select>
						</div>
						-->
					</div>
					<!--item wrap-->
					<div class="lesson_wrap" id="appndSbjtChoice">
					
						<div class="lesson_wrap no_contents_wrap d-flex justify-content-center align-items-center" id="no-data-img">
							<div class="d-flex flex-column justify-content-center align-items-center">
								<img src="${contextPath}/${crtSiteId}/assets/images/kmou_noshad_big.png" alt="해양이" />
								<p class="text-center mt-2">
									왼쪽 상자에서 원하는 교과목을<br />담아주세요.
								</p>
							</div>
						</div>
					</div>
					<!--하단 버튼모음-->
					<section class="bottom_btn_group d-flex flex-row justify-content-between mt-3">
						<div class="d-flex flex-wrap gap-2">
							<button type="button" class="all_check text-white border-0 d-flex flex-row align-items-center gap-1 btn-fg" value="afterChk">
								<i></i>
								전체선택
							</button>
							<button type="button" class="remove_check d-inline-flex btn-fg" value="afterClear">선택 일괄해제</button>
							<button class="open_univ d-inline-flex align-items-center text-white border-0 d-none" id="otherColg">
								<img src="${contextPath}/${crtSiteId}/assets/images/ico_univ.png" alt="대학 아이콘" class="d-inline-block" /> 타 대학/기관/산업체 교과목 등록
							</button>
						</div>
						<div>
							<button type="button" class="all_delet text-white d-flex align-items-center flex-row border-0" onclick="delSbjt('all');">
								<img src="${contextPath}/${crtSiteId}/assets/images/ico_w_close.png" alt="삭제 아이콘" class="d-inline-block" />
								<span class="text-center ps-1">전체 삭제</span>
							</button>
						</div>
					</section>
				</div>
			</div>
			<div class="modal-footer d-flex flex-row align-items-center justify-content-center ">
				<button type="button" class="mod_save p-3 d-flex flex-row align-items-center justify-content-center gap-2 bg-transparent" id="btnSbjtSave">
					<img src="${contextPath}/${crtSiteId}/assets/images/ico_down.png" alt="저장 아이콘" />저장하기
				</button>
<!-- 				<button type="button" class="mod_wrt_plan p-3 d-flex flex-row align-items-center justify-content-center gap-2 bg-transparent"> -->
<%-- 					<img src="${contextPath}/${crtSiteId}/assets/images/ico_b_doc.png" alt="문서 아이콘" />이수계획작성 --%>
<!-- 				</button> -->
			</div>
		</div>
	</div>
</div>
<div class="modal fade modal-xl modal_openUniv" id="openExternal" aria-hidden="true" aria-labelledby="openExternalLabel" tabindex="-1" style="display: none;">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header bg-black">
				<h1 class="modal-title fs-5 text-white" id="openExternalLabel">타 대학/기관/산업체 교과목 등록</h1>
				<button type="button" id="closeExternal" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
					<img src="${contextPath}/${crtSiteId}/assets/images/ico_w_close.png" alt="닫기 아이콘" />
				</button>
			</div>
			<div class="modal-body">
				<table class="table">
					<caption class="blind">타 대학/기관/산업체 교과목등록</caption>
					<colgroup>
						<col width="13%" />
						<col width="37%" />
						<col width="13%" />
						<col width="37%" />
					</colgroup>
					<tbody>
						<tr>
							<th scope="col">교과목 분류</th>
							<td id="tdSbjt"></td>
							<th scope="col">구분</th>
							<td>
								<label class="blind" for="ext_gubun">구분</label>
								<select class="form-select" id="ext_gubun">
									<option value="UE010021" selected>전공필수</option>
									<option value="UE010022">전공선택</option>
									<option value="UE010024">전공기초</option>
									<option value="UE010011">교양필수</option>
									<option value="UE010012">교양선택</option>
								</select>
							</td>
						</tr>
						<tr>
							<th scope="col">개설전공</th>
							<td colspan="3">
								<label for="ext_openMajor" class="blind">타 대학/기관/산업체 개설전공(기관명)</label>
								<input id="ext_openMajor" type="text" placeholder="-" class="form-control">
							</td>
						</tr>
						<tr>
							<th scope="col">과목명</th>
							<td colspan="3">
								<label for="ext_nameMajor" class="blind">타 대학/기관/산업체 과목명</label>
								<input id="ext_nameMajor" type="text" placeholder="-" class="form-control">
							</td>
						</tr>
						<tr>
							<td colspan="4" class="p-0 border-0">
								<table class="table">
									<caption class="blind">학년/학기/학점</caption>
									<colgroup>
										<col width="13%">
										<col width="auto">
										<col width="13%">
										<col width="auto">
										<col width="13%">
										<col width="auto">
									</colgroup>
									<tbody>
										<tr>
											<th scope="col">학년</th>
											<td>
												<label class="blind" for="ext_year">학년</label>
												<select class="form-select" id="ext_year">
													<option value="1" selected>1학년</option>
													<option value="2">2학년</option>
													<option value="3">3학년</option>
													<option value="4">4학년</option>
												</select>
											</td>
											<th scope="col">학기</th>
											<td>
												<label class="blind" for="ext_semi">학기</label>
												<select class="form-select" id="ext_semi">
													<option value="GH0210" selected>1학기</option>
													<option value="GH0220">2학기</option>
												</select>
											</td>
											<th scope="col">학점</th>
											<td>
												<label class="blind" for="ext_score">학점</label>
												<select class="form-select" id="ext_score">
													<option value="1" selected>1</option>
													<option value="2">2</option>
													<option value="3">3</option>
													<option value="3">4</option>
												</select>
											</td>
										</tr>
									</tbody>
								</table>
							</td>
						</tr>
					</tbody>
				</table>
				<a href="javascript:addOtherSbjt();" title="추가하기" class="text-center mx-auto mb-3 mt-4 py-3 px-3 fw-bolder">
					<i></i>
					추가하기
				</a>
			</div>
		</div>
	</div>
</div>