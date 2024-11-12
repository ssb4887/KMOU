<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>

<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/usrScript/stdList.jsp"/>
	</jsp:include>
</c:if>
<!--link rel="stylesheet" type="text/css" href="${contextPath}${cssAssetPath}/sub.css"-->
<form id="frmView" name="frmView" method="post" action="">
	<input type="hidden" name="mId" value="36">
	<input type="hidden" id="SDM_CD" name="SDM_CD" value="">
	<input type="hidden" id="REVSN_NO" name="REVSN_NO" value="">
</form>
<form id="frmNewInput" name="frmNewInput" method="post" action="/web/studPlan/stdInput.do">
	<input type="hidden" name="mId" value="36">
</form>	
<!-- <div class="container_wrap"> -->
	<div class="sub_wrap">
		<div class="sub_background plan_bg">
			<section class="inner">
				<h3 class="title fw-bolder text-center text-white">학생설계전공</h3>
				<p class="sub_title_script">학생설계전공을 작성하고 상세정보를 확인할 수 있습니다.</p>
			</section>
		</div>
		<!--본문-->
		<section class="inner mt-5">
			<!--내 설계전공-->
			<section class="plan_section my_plan pb-5">
				<h5 class="fw-bolder pb-2 mb-3 d-flex flex-row justify-content-between align-items-center">
					<!-- 변경가능한 학생설계전공이 있는지 체크(사용하지 않는듯하여 주석 - 요청시 주석 해제) -->
<%-- 					<c:set var="isChange" value="N"/> --%>
<%-- 					<c:forEach items="${MY_LIST }" var="myList"> --%>
<%-- 						<c:if test="${!empty myList.SDM_DEPT_CD and myList.IS_CHG_YN eq 'Y'}"> --%>
<%-- 							<c:set var="lastSdmCd" value="${myList.SDM_CD }"/> --%>
<%-- 							<c:set var="lastRevsn" value="${myList.REVSN_NO }"/> --%>
<%-- 							<c:set var="isChange" value="Y"/>			 --%>
<%-- 						</c:if>			 --%>
<%-- 					</c:forEach> --%>
					내 설계전공
<%-- 					<c:if test="${isChange eq 'N' }"> --%>
					<a href="#" title="신규등록하기" class="new_plan" onclick="javascript:frmNewInput.submit();">신규등록하기</a>
<%-- 					</c:if> --%>
<%-- 					<c:if test="${isChange eq 'Y' }"> --%>
<%-- 					<a href="#" onclick="changeInput('${lastSdmCd}','${lastRevsn }');" title="변경등록하기" class="new_plan">변경등록하기</a> --%>
<%-- 					</c:if> --%>
				</h5>
				<div class="box_wrap ">
					<c:forEach items="${MY_LIST }" var="myList">
						<c:choose>
						<c:when test="${myList.APLY_FG ne 'CHG' }">
							<c:choose>						
							<c:when test="${myList.STATUS eq '10'}">
								<c:set var="goMyFlag" value="Modify"/>	
							</c:when>
							<c:otherwise>
								<c:set var="goMyFlag" value="MyView"/>
							</c:otherwise>		
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>						
							<c:when test="${myList.STATUS eq '10'}">
								<c:set var="goMyFlag" value="ChgModify"/>	
							</c:when>
							<c:otherwise>
								<c:set var="goMyFlag" value="MyChgView"/>
							</c:otherwise>		
							</c:choose>						
						</c:otherwise>
						</c:choose>																		
						<div class="item"  style="cursor:pointer;" onclick="go${goMyFlag}('${myList.SDM_CD }','${myList.REVSN_NO}')" >
							<h3 class="title fs-3 fw-bolder text-start mb-3">
								${myList.SDM_KOR_NM }				
							</h3>
							<div class="spl_title_list">
								<c:forEach items="${MY_SBJT_LIST }" var="mySbjtList">								
									<c:if test="${myList.SDM_CD eq mySbjtList.SDM_CD and myList.REVSN_NO eq mySbjtList.REVSN_NO }">
										<h4 class="title">
											${mySbjtList.MAJOR_NM }
										</h4>
									</c:if>
								</c:forEach>
							</div>
<!-- 							<div class="detail_maj position-relative"> -->
<%-- 								<span class="d-block fs-6 lh-sm mb-1">${myList.CHRG_SUST_NM }</span> --%>
<%-- 								<span class="d-block fs-6 lh-sm">${myList.PARTICI_SUST_NM }</span> --%>
<!-- 							</div> -->
							<div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2">
								<dl class="d-flex">
									<dt>수여학위</dt>
									<dd>${myList.AWD_DEGR_KOR_NM }</dd>
								</dl>
								<dl class="d-flex">
									<dt>설계학점</dt>
									<dd>${myList.TOTAL_CDT_NUM }학점 </dd>
								</dl>
								<dl class="d-flex">
									<dt>교과목설계범위</dt>
									<dd>${myList.SBJT_DGN_RNG_FG_NM}</dd>
								</dl>
								<dl class="d-flex">
									<dt>신청유형</dt>
									<dd>
									<c:choose>
										<c:when test="${myList.APLY_FG eq 'NEW' }">
											신설
										</c:when>
										<c:when test="${myList.APLY_FG eq 'CHG' }">
											변경
										</c:when>
									</c:choose>
									</dd>
								</dl>
							</div>
							<ol class="state_line d-flex flex-wrap justify-content-between mt-3">
								
                                <li class="end">${myList.COM_INF_NM}</li>
								<c:if test="${myList.STATUS ge '20' and myList.STATUS lt '30' }">
									<c:if test="${myList.CNSLT_CNT ge 2 }">								
									<li class="proff_plus2">
										${myList.CNSLT_ONE_NM } 외 ${myList.CNSLT_CNT -1 }명									
										<span></span>
										<span></span>
									</li>
									</c:if>
									<c:if test="${myList.CNSLT_CNT eq 1 }">								
									<li class="proff_plus1">
										${myList.CNSLT_ONE_NM } 
										<span></span>
										<span></span>
									</li>
									</c:if>
								</c:if>
							</ol>
						</div>
					</c:forEach>
				</div>
			</section>
			<!--등록된 학생설계전공-->
			<section class="plan_section regi_plan">
				<div class="title_box d-flex flex-column flex-sm-row justify-content-between align-items-end pb-3  mb-3">
					<h5 class="fw-bolder mr-4">
						등록된 학생설계전공 <span>총 <strong><span id="studPlanCnt"></span>건</strong></span>
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
