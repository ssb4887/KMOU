<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>

<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/usrScript/profList.jsp"/>
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
			<c:if test="${loginVO.usertypeIdx eq '45' }">
              <!--내 설계전공-->
                <section class="proff_plan pf_plan_proc ">
                    <h5 class="fw-bolder pb-2 mb-3 d-flex flex-wrap justify-content-between align-items-center">컨설팅 진행 &amp; 완료된 설계전공</h5>

                    <ul class="cate_proc px-3 px-md-0 px-lg-3 py-3 d-flex flex-wrap justify-content-center align-itmes-start" style="border-top:0px;">
						<c:forEach items="${ALL_TAB_CNT }" var="item">
						<c:choose>
							<c:when test="${item.TYPE eq 'RA'}">
			                	<li><a href="javaScript:" onclick="getTab('RA');" title="승인" class="pr_all active cnsltTab" id="RATab">승인요청<span id="RACnt">${item.COUNT}</span></a></li>
							</c:when>
							<c:when test="${item.TYPE eq 'RC'}">
			                	<li><a href="javaScript:" onclick="getTab('RC');" title="요청" class="pr_cons cnsltTab" id="RCTab">상담요청<span id="RCCnt">${item.COUNT}</span></a></li>
							</c:when>
							<c:when test="${item.TYPE eq 'AC'}">
			                	<li><a href="javaScript:" onclick="getTab('AC');" title="참여" class="pr_cons cnsltTab" id="ACTab">참여가능상담<span id="ACCnt">${item.COUNT}</span></a></li>
							</c:when>
							<c:when test="${item.TYPE eq 'WC'}">
			                	<li><a href="javaScript:" onclick="getTab('WC');" title="진행" class="pr_cons cnsltTab" id="WCTab">상담진행<span id="WCCnt">${item.COUNT}</span></a></li>
							</c:when>
							<c:when test="${item.TYPE eq 'CC'}">
			                	<li><a href="javaScript:" onclick="getTab('CC');" title="완료" class="pr_cons cnsltTab" id="CCTab">상담완료<span id="CCCnt">${item.COUNT}</span></a></li>
							</c:when>
							<c:when test="${item.TYPE eq 'WJ'}">
			                	<li><a href="javaScript:" onclick="getTab('WJ');" title="심사진행" class="pr_sims cnsltTab" id="WJTab">심사진행 <span id="WJCnt">${item.COUNT}</span></a></li>
							</c:when>
							<c:when test="${item.TYPE eq 'CJ'}">
			                	<li><a href="javaScript:" onclick="getTab('CJ');" title="심사완료" class="pr_sims cnsltTab" id="CJTab">심사완료<span id="CJCnt">${item.COUNT}</span></a></li>
							</c:when>
						</c:choose>
						</c:forEach>
                    </ul>
					<div class="pf_planWrap" id="cnsltTabList">
						<c:if test="${not empty RA }">
							<div class="box_wrap">
								<!--item :: 기본-->
								<c:forEach var="ra" items="${RA }">
									<div class="item <c:if test="${ra.STATUS eq '30'}">on_approve</c:if> <c:if test="${ra.STATUS eq '33'}">reject</c:if> <c:if test="${ra.STATUS gt '33'}">approve</c:if> p-3">
										<!---->
										<div class="title_wrap d-flex flex-row mb-3 justify-content-between">
											<ul>
												<li class="title text-truncate">${ra.SDM_KOR_NM }</li>
												<li>
													<ul class="cate d-flex gap-2 flex-wrap flex-sm-nowrap">
														<li class="color_border">
			                                                <span>${ra.FIRST_MAJOR}</span>
			                                                <span>${ra.OTHER_MAJOR}</span>
														</li>
													</ul>
												</li>
											</ul>											
											<div class="btn_wrap d-flex flex-row gap-2">
											<c:if test="${ra.STATUS eq '30'}">												
												<a href="#" onclick="goModify('Approve','${ra.SDM_CD}','${ra.REVSN_NO}','${ra.APLY_FG}')" title="요청승인" class="writ_consult w-100">요청승인</a>												
											</c:if>
											<c:if test="${ra.STATUS gt '30'}">
												<div class="btn_wrap d-flex flex-row gap-2">
													<a href="#" onclick="goView('Approve','${ra.SDM_CD}','${ra.REVSN_NO}','${ra.APLY_FG}')"  title="상세보기" class="writ_consult w-100">상세보기</a>
												</div>
											</c:if>
											</div>
										</div>
										<!---->
										<div class="info_wrap d-flex flex-wrap p-3 gap-2">
											<dl class="d-flex flex-row gap-2">
												<dt>신청학생</dt>
												<dd>${ra.APLY_STUDENT_NM }</dd>
											</dl>
											<dl class="d-flex flex-row gap-2">
												<dt>수여학위</dt>
												<dd>${ra.AWD_DEGR_KOR_NM }</dd>
											</dl>
											<br>
											<dl class="d-flex flex-row gap-2">
												<dt>설계학점</dt>
												<dd>${ra.TOTAL_CDT_NUM }학점</dd>
											</dl>
											<dl class="d-flex flex-row gap-2">
												<dt style="min-width: 80px;">교과목설계범위</dt>
												<dd>${ra.SBJT_DGN_RNG_FG_NM }</dd>
											</dl>
											<dl class="d-flex flex-row gap-2">
												<dt>신청유형</dt>
												<dd><c:if test="${ra.APLY_FG eq 'NEW' }">신설</c:if><c:if test="${ra.APLY_FG eq 'CHG' }">변경</c:if></dd>
											</dl>
										</div>
									</div>
								</c:forEach>
							</div>
						</c:if>
						<c:if test="${empty RA }">
							<div class="pf_no_planWrap d-flex flex-column justify-content-center align-items-center" id="">
								<p>정보가 없습니다.</p>
							</div>
						</c:if>
					</div>
					<!--paging-->
					<ul class="pagination gap-2 justify-content-center mt-5" id="myPagination">
						<pgui:paginationProfIndex listUrl="${URL_PAGE_LIST}" pgInfo="${paginationMyInfo}"/>
					</ul>
                </section>
            </c:if>
			<!--등록된 학생설계전공-->
			<section class="plan_section regi_plan">
				<div class="title_box d-flex flex-column flex-sm-row justify-content-between align-items-end pb-3  mb-3">
					<h5 class="fw-bolder col-12 col-sm-6 d-flex flex-wrap align-items-end">
						등록된 학생설계전공
						<span>
							총 <strong><span id="studPlanCnt"></span>건</strong>의 학생설계전공이 있습니다.
						</span>
					</h5>
					<div class="d-flex flex-row gap-2 gap-sm-3 justify-content-end col-12 col-sm-6">
						<label for="majorCd" class="blind">전공 전체</label>
						<select class="form-select rounded-pill" aria-label="select" id="majorCd" name="majorCd">
							<option value="">전공 전체</option>
						</select>
						<label for="approvYear" class="blind">승인년도 전체</label>
						<select class="form-select rounded-pill" aria-label="select" id="year" name="year">
							<option value="">승인년도 전체</option>
						</select>
					</div>
				</div>
				<div class="d-flex flex-wrap gap-3" id="studPlanList">
				</div>
				<!--paging-->
				<ul class="pagination gap-2 justify-content-center mt-5" id="page">					
				</ul>
			</section>
		</section>
	</div>
<!-- </div> -->
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>
