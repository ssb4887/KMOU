<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>

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

<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/usrScript/stdChgModify.jsp"/>
	</jsp:include>
</c:if>


<form id="frmView" name="frmView" method="post" action="">
	<input type="hidden" name="mId" value="36">
	<input type="hidden" id="SDM_CD" name="SDM_CD" value="">
	<input type="hidden" id="REVSN_NO" name="REVSN_NO" value="">
</form>

<!-- <div class="container_wrap"> -->
	<div class="sub_wrap">
		<div class="sub_background plan_bg">
			<section class="inner">
				<h3 class="title fw-bolder text-center">학생설계전공</h3>
			</section>
		</div>
		<!--본문-->
		<section class="inner mt-5">
			<!--제목-->
			<section class="detail_title_wrap">
				<section class="d-flex flex-row justify-content-between align-items-center title_box px-2 py-3 mb-5">
					<a href="/web/studPlan/list.do?mId=36" title="이전페이지" class="d-flex flex-row align-items-center gap-2">
						<img src="../images/arr_blue.png" /><em class="fst-normal">이전페이지</em>
					</a>
					<h5 class="content_title text-center fw-bolder d-flex align-items-center justify-content-center gap-1 flex-wrap">
						<span class="text-truncate">${INFO.SDM_KOR_NM }</span>
					</h5>
				</section>
			</section>				
                        
			<!--기본정보-->
			<form name="frmPost" id="frmPost" action="/web/studPlan/stuEdit.do?mId=45&site=pknuai" method="post">
				<input type="hidden" name="SDM_CD" value="${INFO.SDM_CD }" />
				<input type="hidden" name="REVSN_NO" value="${INFO.REVSN_NO }" />
				<input type="hidden" name="APLY_STUDENT_NO" value="${MYINFO.STUDENT_NO }"/>
				<input type="hidden" name="SDM_KOR_NM" value="${INFO.SDM_KOR_NM }"/>
				<!-- 교과목 hidden -->
				<div id="containInput"></div>		
				<!-- 변경사유 -->
				<section class="plan_dtl_section plan_dtl_text" id="move_acitve2">
					<h5 class="fw-bold mb-3">변경사유</h5>				
					<label class="blind" for="planReason8">변경사유</label>
					<textarea id="planReason6" name="SDM_CHG_RESN" class="form-control text-start" style="height:150px;" placeholder="-">${INFO.SDM_CHG_RESN }</textarea>
				</section>									
				<!-- 교과목 설계범위 hidden -->
				<input type="hidden" name="SBJT_DGN_RNG_FG" value="${INFO.SBJT_DGN_RNG_FG }" id="checkedSbjtDgnRngFg">
				<input type="hidden" name="SBJT_DGN_RNG_FG_NM" value="${INFO.SBJT_DGN_RNG_FG_NM }" id="checkedSbjtDgnRngFgNm">
				
				<!--교육과정 신.구대비표-->
	            <section class="plan_dtl_section compari_curri">
	            	<div class="title_box d-flex flex-row justify-content-between align-items-end mb-3 flex-wrap">
	                    <h5 class="fw-bold">교육과정 신&middot;구 대비표</h5>
	                    <button type="button" class="border-0 fw-bolder text-white py-2 px-3" data-bs-toggle="modal" data-bs-target="#compariSubj" id="btnModal"><i></i>과목등록/삭제</button>
	                </div>
                
                <!--pc-->
	                <table class="table d-none d-lg-table ">
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
				
				<!--교과목 설계범위 chart -->
				<section class="plan_dtl_section plan_range pt-4" id="move_acitve3">
					<div class="title_box d-flex flex-row justify-content-between align-items-center mb-3">
						<h5 class="fw-bold">교육과정표</h5>
		            <button type="button" class="border-1 fw-bolder text-white py-2 px-3" id="afterSbjtShow"  style="display:none;">	
					   변경 교육과정표 보기
					</button>
		            <button type="button" class="border-1 fw-bolder text-white py-2 px-3" id="beforeSbjtShow">	
					    기존 교육과정표 보기
					</button>
					</div>
					<div class="table_wrap d-flex flex-wrap">
						<div class="table_box">
							<table class="table mb-0">
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
							<table class="table mb-0">
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
							<table class="table mb-0">
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
							<table class="table mb-0">
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
					<div class="dtl_bottom py-2 px-3 d-flex flex-column flex-sm-row align-items-start align-items-sm-center justify-content-between mt-3 gap-2">
						<div class="d-flex flex-row align-items-center gap-2 col-12 col-sm-auto">
						</div>
						<div class="d-flex flex-row align-items-center gap-2 col-12 col-sm-auto">
							<h5>총합계</h5>
							<p class="bg-white py-2 px-4">
								<span class="d-inline-block pe-1 totalPnt">0</span>
								학점
							</p>
						</div>
					</div>
					<section class="plan_total pt-4">
						<h5 class="fw-bold mb-3">설계학점 총계</h5>
						<table class="table">
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
						</table>
						<div class="dtl_bottom py-2 px-3 d-flex flex-row align-items-center justify-content-end mt-3 gap-2">
							<h5>총 설계학점 합계</h5>
							<p class="bg-white py-2 px-4">
								<input type="hidden" id="desTotCnt" value="" />
								<span class="d-inline-block pe-1 totalPnt">0</span>
								학점
							</p>
						</div>
						<div class="search_proff d-flex flex-column flex-md-row mt-3">
							<dl class="d-flex flex-row col-12 col-md-6 align-items-center">
								<dt class="col-4 col-sm-3 ps-3 d-flex align-items-center">지도교수</dt>
								<dd class="col-8 col-sm-9 d-flex flex-row px-3 py-2 gap-2">
									<input id="proNm" type="text" name="GUID_PROF_STAFF_NM" class="form-control" placeholder="지도교수 검색" value="${INFO.GUID_PROF_STAFF_NM}" readonly>
									<input id="proCd" type="hidden" name="GUID_PROF_STAFF_NO" value="${INFO.GUID_PROF_STAFF_NO}"/>
									<input id="proEmail" type="hidden" name="GUID_PROF_STAFF_EMAIL" value="${INFO.GUID_PROF_STAFF_NO}"/>
									<button type="button" class="text-white border-0 px-2 py-2" onclick="profAddBtn(1);">검색</button>
								</dd>
							</dl>
							<dl class="d-flex flex-row col-12 col-md-6 align-items-center">
								<dt class="col-4 col-sm-3 ps-3 d-flex align-items-center">상담교수</dt>
								<dd class="col-8 col-sm-9 d-flex flex-row px-3 py-2 gap-2">
									<input id="proConNm" type="text" name="CNSLT_PROF_NM" class="form-control" placeholder="상담교수 검색" value="${INFO.CNSLT_PROF_NM}" readonly>
									<input id="proConCd" type="hidden" name="CNSLT_PROF" value="${INFO.CNSLT_PROF}"/>
									<input id="proConEmail" type="hidden" name="CNSLT_MSG_EMAIL"/>
									<button type="button" class="text-white border-0 px-2 py-2" onclick="profAddBtn(2);">검색</button>
									<button type="button" class="text-white border-0 px-2 py-2" onclick="proDelBtn();">초기화</button>
								</dd>
							</dl>
						</div>
					</section>
				</section>
				<c:set var="rejectList" value="33,43,53,63" />					
				<c:if test="${fn:contains(rejectList, INFO.STATUS)}">
				<section class="plan_dtl_section plan_dtl_text" id="move_acitve2">
					<h5 class="fw-bold mb-3">반려 사유</h5>
					<label for="rjtResn" class="blind">반려 사유</label>  
					<textarea id="rjtResn" class="form-control mb-4" placeholder="-" readonly></textarea>
				</section>				        
				</c:if>			        
			</form>
<!-- 			컨설팅 결과 -->
			<c:if test="${!empty CNSLTLIST }">
				<section class="plan_dtl_section plan_result" id="move_acitve4">
					<h5 class="fw-bold mb-3">상담 결과</h5>
					<div class="table_wrap">
						<table class="table">
							<caption class="blind">상담 결과</caption>
							<colgroup>
								<col width="15%" />
								<col width="25%" />
								<col width="auto" />
								<col width="15%" />
							</colgroup>
							<thead>
								<tr>
									<th scope="row" class="text-center">이름</th>
									<th scope="row" class="text-center">개설전공</th>
									<th scope="row" class="text-center">지도의견</th>
									<th scope="row" class="text-center">지도일자</th>
								</tr>
							</thead>
							<tbody>								
								<c:forEach var="cnslt" items="${CNSLTLIST}">
									<!-- 컨설팅 완료 O, 방문요청 X -->
									<c:if test="${!empty cnslt.CNSLT_CMPTL_DT and empty cnslt.MEET_CNSLT_ADV_DT}">
									<tr>
										<th rowspan="3" scope="colgroup" class="fw-normal align-middle text-center">${cnslt.CNSLT_PROF_STAFF_NM }</th>
										<td class="open_maj text-center align-middle">교과목 구성 적합성</td>
										<td class="guid_opi text-start align-middle">
											<p class="text-center">${cnslt.SBJT_CNST_ADV_OPIN }</p>
										</td>
										<td class="date_opi text-center align-middle">
											${cnslt.SBJT_CNST_ADV_DT}
										</td>
									</tr>
									<tr>
										<td class="open_maj text-center align-middle">전공 명칭 적합성</td>
										<td class="guid_opi text-start align-middle">
											<p class="text-center">${cnslt.MJ_NM_ADV_OPIN }</p>
										</td>
										<td class="date_opi text-center align-middle">
											${cnslt.MJ_NM_ADV_DT}
										</td>
									</tr>
									<tr>
										<td class="open_maj text-center align-middle">수여학위명 적합성</td>
										<td class="guid_opi text-start align-middle">
											<p class="text-center">${cnslt.AWD_DEGR_NM_ADV_OPIN }</p>
										</td>
										<td class="date_opi text-center align-middle">
											${cnslt.AWD_DEGR_NM_ADV_DT}
										</td>
									</tr>
									</c:if>
									<!-- 컨설팅 완료 O, 방문요청 O -->
									<c:if test="${!empty cnslt.CNSLT_CMPTL_DT and !empty cnslt.MEET_CNSLT_ADV_DT }">
									<tr>
										<th rowspan="3" scope="colgroup" class="fw-normal align-middle text-center">${cnslt.CNSLT_PROF_STAFF_NM }</th>
										<td class="open_maj text-center align-middle">방문상담 요청</td>
										<td class="guid_opi text-start align-middle">
											<p class="text-center">${cnslt.MEET_CNSLT_ADV_OPIN }</p>
										</td>
										<td class="date_opi text-center align-middle">
											${cnslt.MEET_CNSLT_ADV_DT}
										</td>
									</tr>
									<tr></tr>
									<tr></tr>
									</c:if>
									<!-- 컨설팅 완료 X, 방문요청 X -->
									<c:if test="${empty cnslt.CNSLT_CMPTL_DT and empty cnslt.MEET_CNSLT_ADV_DT}">
									<tr>
										<th rowspan="3" scope="colgroup" class="fw-normal align-middle text-center">${cnslt.CNSLT_PROF_STAFF_NM }</th>
										<td class="guid_opi text-start align-middle" colspan="3">
											<p class="text-center">상담 미완료</p>
										</td>
									</tr>
									<tr></tr>
									<tr></tr>
									</c:if>										
								</c:forEach>
							</tbody>
						</table>
					</div>
				</section>
				</c:if>
			<section class="plan_button_wrap d-flex flex-wrap justify-content-center gap-2 gap-md-3 mt-4">
					<button onclick="chgModifyTempSave()" type="button" class="border-0 py-3" style="background-color: #309CBE;">임시 저장</button>
					<button onclick="chgModifyReqCnslt()" type="button" class="border-0 py-3" style="background-color: #003C92;">상담신청</button>
					<button onclick="chgModifySubmit()" type="button" class="border-0 py-3" style="background-color: #4571E9;">신청서 제출</button>
			</section>
		</section>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false" /></c:if>
<!--교수님추가하기-->
<div class="modal fade modal-xl modal_addstu modal_add_teac" id="addProf" tabindex="-1" aria-labelledby="addProfLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header bg-black">
				<h1 class="modal-title fs-5 text-white" id="addProfLabel"></h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
					<img src="${contextPath}/${crtSiteId}/assets/images/ico_w_close.png" alt="닫기 아이콘" />
				</button>
			</div>
			<div class="modal-body">
				<h2 id="divProf" class="mb-3"></h2>
				<table class="table">
					<caption class="blind">교수님정보검색</caption>
					<colgroup>
						<col width="15%" />
						<col width="35%" />
						<col width="15%" />
						<col width="35%" />
					</colgroup>
					<tbody>
						<tr>
							<th class="border-end align-middle">이름</th>
							<td class="border-end ">
								<input type="text" class="form-control" placeholder="이름입력" id="profName">
							</td>
							<th class="border-end align-middle">학과</th>
							<td class="border-end ">
								<select class="form-select" id="profDept"></select>
							</td>
						</tr>
					</tbody>
				</table>
				<button type="button" class="text-white py-2 px-5 border-0 mt-3 text-center mx-auto d-block" onclick="proSrcList();">검색</button>
					<div id="profResult" class="mt-5">
					<h2 class="mb-3">
						<span id="profCnt"></span>건의 검색결과가 있습니다.
					</h2>
					<div class="result_table">
						<table class="table ">
							<caption class="blind">교수검색결과</caption>
							<colgroup>
								<col id="profColWidth" width="50px" />
								<col width="auto" />
								<col width="auto" />
							</colgroup>
							<thead>
								<tr>
									<th scope="col" class="text-center" id="prof_2" style="display: block !important;">
										<label class="blind">체크박스 전체</label>
										<input id="checkAll" type="checkbox" class="form-check-input" />
									</th>
									<th scope="col" class="text-center border-end">이름</th>
									<th scope="col" class="text-center">전공</th>
								</tr>
							</thead>
							<tbody id="profList"></tbody>
						</table>
					</div>
					<a id="divBtn" href="javascript:" title="추가하기" class="text-center mx-auto mb-3 mt-4 py-3 px-3 fw-bolder">
						<i></i>
						추가하기
					</a>
				</div>
			</div>
		</div>
	</div>
</div>

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
												<li class="form-check  col-2">
													<input type="checkbox" id="chkMaj_Teach" class="form-check-input chk-sbjt-search" value="UE010031">
													<label class="form-check-label" for="chkMaj_Teach">교직</label>
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
												<li class="form-check  col-2">
													<input type="checkbox" id="chkMaj_TeachMy" class="form-check-input chk-sbjt-searchMy" value="UE010031">
													<label class="form-check-label" for="chkMaj_TeachMy">교직</label>
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
								<img src="${contextPath}/${crtSiteId}/assets/images/kmou_noshad_big.png" alt="헤양이" />
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