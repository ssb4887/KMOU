
<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
	<div class="sub_background sbjt_bg">
                <section class="inner">
                    <h3 class="title fw-bolder text-center text-white">교과목(전공&middot;교양)</h3>
                </section>
            </div>
            <!--본문-->
            <section class="inner mt-5">
                <!--item 상세-->
                <section class="detail_title_wrap">
                    <section class="d-flex flex-row justify-content-between align-items-center title_box px-2 py-3 mb-5">
                        <a href="javascript:window.history.back();" title="이전페이지" class="d-flex flex-row align-items-center gap-2"><img src="../images/arr_blue.png"/><em class="fst-normal">이전페이지</em></a>
                        <h5 class="content_title text-center fw-bolder d-flex align-items-center justify-content-center gap-1 flex-wrap">
                            <span class="col-12 col-md-auto text-truncate">${sbjtInfo.SUBJECT_NM }(${sbjtInfo.SUBJECT_CD})</span>
                        </h5>
                        <div id="${sbjtInfo.SUBJECT_CD}_${sbjtInfo.DEPT_CD}_${sbjtInfo.YEAR}_${sbjtInfo.GRADE}_${sbjtInfo.SMT}" class="like_container">
                            <div class="link_cnt text-end">
                                 <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange('${sbjtInfo.SUBJECT_CD}_${sbjtInfo.DEPT_CD}_${sbjtInfo.YEAR}_${sbjtInfo.GRADE}_${sbjtInfo.SMT}','sbjt');">
                                 <path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>
                                 <path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>
                            </div>
                        </div>
                    </section>
				</section>
				
                <main class="majref_dtl_wrap">
                    <h5 class="fw-semibold mb-3">교과목 상세정보</h5>
                    <!--전공일때 테이블-->
                    <section class="table_wrap table_maj">
                        <table class="table">
                            <caption class="blind">전공 교육과정상세</caption>
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
                                <tr class="">
                                    <th scope="row" class="border-end">교과목명</th>
                                    <td colspan="3" class="border-end">${sbjtInfo.SUBJECT_NM }</td>
                                    <th scope="row" class="border-end">교과목명(영문)</th>
                                    <td colspan="3">${sbjtInfo.SUBJECT_ENM }</td>
                                </tr>
                                <tr>
                                    <th scope="row" class="border-end">대학(원)</th>
                                    <td class="border-end">${sbjtInfo.COLG_NM }</td>
                                    <th scope="row" class="border-end">학부(과)/전공</th>
                                    <td class="border-end">${sbjtInfo.DEPT_NM }</td>
                                    <th scope="row" class="border-end m_half">편성년도</th>
                                    <td class="border-end m_half">${sbjtInfo.YEAR }</td>
                                    <th scope="row" class="border-end m_half">이수학점</th>
                                    <td class=" m_half">${sbjtInfo.CDT_NUM }</td>
                                </tr>
                                <tr>
                                    <th scope="row" class="border-end m_half">교과구분</th>
                                    <td class="border-end m_half">${sbjtInfo.COMDIV_NM }</td>
                                    <th scope="row" class="border-end m_half">이수학년</th>
                                    <td class="border-end m_half">
                                    	<c:choose>
	                                		<c:when test="${sbjtInfo.GRADE == '0'}">전체학년</c:when>
	                                		<c:otherwise>${sbjtInfo.GRADE}학년</c:otherwise>
	                                	</c:choose>
									</td>
                                    <th scope="row" class="border-end m_half">학기</th>
                                    <td class="border-end m_half">${sbjtInfo.SMT_NM }</td>
                                    <th scope="row" class="border-end word_keep m_half">강의시수</th>
                                    <td class=" m_half">${sbjtInfo.WTIME_NUM }/${sbjtInfo.PTIME_NUM } (이론/시수)</td>
                                </tr>
                                <tr>
                                    <th scope="row" class="border-end m_half">강좌구분</th>
                                    <td class="border-end m_half">${sbjtInfo.CLASS_NM }</td>
                                    <th scope="row" class="border-end m_half">개설여부</th>
                                    <td class="border-end m_half">${sbjtInfo.ABO_YN }</td>
                                    <%-- <th scope="row" class="border-end m_half">KCU</th>
                                    <td class="border-end m_half">${sbjtInfo.SDU_YN }</td> --%>
                                    <th scope="row" class="border-end word_keep m_half">학내가상강좌</th>
                                    <td class="border-end m_half">${sbjtInfo.CYB_YN }</td>
                                    <th scope="row" class="border-end word_keep m_half">융합전공</th>
                                    <td class="m_half">${sbjtInfo.LECPLN_FUSE_FL }</td>
                                </tr>
                                <tr>
                                   <%--  <th scope="row" class="border-end m_half">융합전공</th>
                                    <td class="border-end m_half">${sbjtInfo.LECPLN_FUSE_FL }</td> --%>
                                    <th scope="row" class="border-end m_half">연계전공</th>
                                    <td class="border-end m_half">${sbjtInfo.LECPLN_LINK_FL }</td>
                                    <th scope="row" class="border-end m_half">마이크로디그리</th>
                                    <td class="border-end m_half">${sbjtInfo.LECPLN_MICRO_FL }</td>
                                    <th scope="row" class="border-end word_keep m_half">나노디그리</th>
                                    <td class="border-end m_half">${sbjtInfo.LECPLN_NANO_FL }</td>
                                    <th scope="row" class="border-end word_keep m_half"></th>
                                    <td class="m_half"></td>
                                </tr>
                                <tr>
                                    <th scope="row" class="border-end">교과목 개요</th>
                                    <td colspan="7">${sbjtInfo.SUBJ_DESC}</td>
                                </tr>
                                <tr>
                                    <th scope="row" class="border-end">강의 운영 방법</th>
                                    <td colspan="7">${sbjtInfo.METHOD}</td>
                                </tr>
                                <tr>
                                	<c:choose>
                                		<c:when test="${sbjtInfo.DEPT_CD != '446000' }">
                                			<th scope="row" class="border-end"><c:if test="${empty coreInfo.ABI_NAME}">핵심역량</c:if>${coreInfo.ABI_NAME }</th>
		                                    <td colspan="3">
		                                    <c:if test="${empty coreInfo.ABI_NM_LIST}">등록된 데이터가 없습니다.</c:if>${coreInfo.ABI_NM_LIST }</td>
		                                    <th scope="row" class="border-end"><c:if test="${empty abiInfo.ABI_NAME}">전공능력</c:if>${abiInfo.ABI_NAME }</th>
		                                    <td colspan="3">
		                                    <c:if test="${empty abiInfo.ABI_NM_LIST}">등록된 데이터가 없습니다.</c:if>${abiInfo.ABI_NM_LIST }</td>
                                		</c:when>
                                		<c:otherwise>
                                			<th scope="row" class="border-end"><c:if test="${empty coreInfo.ABI_NAME}">핵심역량</c:if>${coreInfo.ABI_NAME }</th>
                                    		<td colspan="7">
                                    		<c:if test="${empty coreInfo.ABI_NM_LIST}">등록된 데이터가 없습니다.</c:if>${coreInfo.ABI_NM_LIST }</td>
                                		</c:otherwise>
                                	</c:choose>
                                </tr>

                            </tbody>
                        </table>
                    </section>
                </main>
                
                <!--강좌개설 이력-->
                <section class="open_history">
                    <div class="d-flex flex-column flex-sm-row justify-content-between align-items-center mb-3">
                        <h5 class="fw-semibold col-12 col-sm-6 mb-2 mb-ms-0">강좌 개설 이력(최근 5년)</h5>
                        <div class="d-flex flex-row gap-2 gap-sm-3 justify-content-end col-12 col-sm-6">
                            <select class="form-select rounded-pill" id="hisYear" aria-label="select">
                            </select>
                            <select class="form-select rounded-pill" id="hisSemi" aria-label="select">
                                <option value="all" selected>개설학기 전체</option>
                                <option value="1">1학기</option>
                                <option value="2">2학기</option>
                            </select>
                        </div>
                    </div>
                    
                    
                    
                    <div class="d-flex flex-wrap gap-4" id="classView">
                       
                    </div>
                    
                    <!--paging-->
                    <ul class="pagination gap-2 justify-content-center mt-5" id="page">
                    </ul>
                </section>

                <section class="majref_dtl_chart my-5 pt-4">
                    <h5 class="fw-bold mb-3">수강생통계</h5>
                    <div class="chart_box">
                    	<canvas id="chart_box" width="60vw"></canvas>
                    </div>
                </section>

                <a href="javascript:history.back();" title="목록으로" class="back_to_list">목록으로 </a>
            </section>

        </div>


        

    </div>  
    
    
   
   
   	<!-- 개설학과 세부정보 Modal -->
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
                <h2 class="d-flex justify-content-between align-items-center mb-3">개설강좌 상세정보</h2>
                <table class="table">
                    <caption class="blind">개설강좌 상세정보</caption>
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
                            <th scope="row">학점</th>
                            <td id="cdtNum"></td>
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
                            <th scope="row">정원</th>
                            <td id="fixNumber"></td>
                            <th scope="row">강의시간/강의실</th>
                            <td id="timeRoom"></td>
                        </tr>
                        <tr>
                            <th scope="row" id="coreNm">핵심역량</th>
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
                                            <td id="core0">-</td>
                                            <td id="core1">-</td>
                                            <td id="core2">-</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" id="abiNm">전공능력</th>
                            <td colspan="7">
                                <table class="table in_table">
                                    <caption class="blind">전공능력</caption>
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
                                            <td id="abi0">-</td>
                                            <td id="abi1">-</td>
                                            <td id="abi2">-</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </section>

           <!--  <section class="mt-4">
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

                paging
                <ul class="pagination gap-2 justify-content-center mt-5" id="evalPage">
                </ul>
            </section> -->
        </div>
        <div class="modal-footer d-flex flex-row align-items-center justify-content-center ">
            <button type="button" id="detailBookmart" class="p-3 d-flex flex-row align-items-center justify-content-center gap-2 bg-transparent">
                <img src="../images/modal_ico_fill_lov.png" id="fillLov" alt="찜하기 아이콘"/>
                <img src="../images/modal_ico_r_lov.png" id="rLov" alt="찜하기 아이콘"/>
                찜하기
            </button>
            <button type="button" id="detailBasket"  class="p-3 d-flex flex-row align-items-center justify-content-center gap-2 bg-transparent">
                <img src="../images/modal_ico_fill_sho.png" id="fillSho" alt="장바구니 아이콘"/>
                <img src="../images/modal_ico_b_sho.png" id="bSho" alt="장바구니 아이콘"/>
                장바구니
            </button>
        </div>
      </div>
    </div>
  </div>

<!-- 강의계획서 Modal -->
<div class="modal fade modal-xl modal_syllabus" id="plannerModal" tabindex="-1" aria-labelledby="syllabusModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header bg-black">
            <h1 class="modal-title fs-5 text-white" id="syllabusModalLabel">강의계획서</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                <img src="../images/ico_w_close.png" alt="닫기 아이콘"/>
            </button>
        </div>
        <div class="modal-body">
            <section class="mt-4">
                <h2 class="d-flex justify-content-between align-items-center mb-3">강의계획서<br></h2>
                <table class="table">
                    <caption class="blind">강의계획서</caption>
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
                            <td colspan="3" id="sbjtNamePlan"></td>
                            <th scope="row">교과목명(영문)</th>
                            <td colspan="3" id="sbjtENamePlan" ></td>
                        </tr>
                        <tr>
                            <th scope="row">학년</th>
                            <td id="yearPlan"></td>
                            <th scope="row">학기</th>
                            <td id="smtPlan"></td>
                            <th scope="row">담당교수</th>
                            <td id="empPlan"></td>
                            <th scope="row">과목구분</th>	
                            <td id="comdivPlan"></td>
                        </tr>
                        <tr>
                            <th scope="row">수강대상대학(원)</th>
                            <td id="collegePlan"></td>
                            <th scope="row">수강대상학과</th>
                            <td id="deptNmPlan"></td>
                            <th scope="row">교과목번호 - 분반</th>
                            <td id="sbjDivPlan"></td>
                        	<th scope="row">학점</th>
                            <td id="cdtNumPlan"></td>
                        </tr>
                        <tr>
                            <th scope="row">시수(이론/실습·실기)</th>
                            <td id="sisuPlan"></td>
                            <th scope="row">-</th>
                            <td id="">-</td>
                            <th scope="row">-</th>
                            <td id="">-</td>
                            <th scope="row">-</th>
                            <td id=""></td>
                        </tr>
                    </tbody>
                </table>
            </section>
            
            <section class="mt-4">
                <h2 class="d-flex justify-content-between align-items-center mb-3">교과목정보<br></h2>
                <table class="table">
                    <caption class="blind">교과목정보</caption>
                    <colgroup>
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="row" colspan="2">교과목개요</th>
                            <td colspan="8" id="sbjtDescPlan" class="plan"></td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2">세부정보</th>
                            <td colspan="8" id="sbjtDetailPlan" class="plan"></td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2">수강대상</th>
                            <td colspan="8" id="sugang"></td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2">핵심역량</th>
                            <td id="" colspan="8">
                            	<table class="table in_table">
                                    <caption class="blind">핵심역량</caption>
                                    <colgroup>
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                    </colgroup>
                                    <thead>
                                        <tr id="coreGbList">
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr id="coreList">
                                        </tr>
                                        <tr id="coreVal">
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr id="abiRow">
                            <th scope="row" colspan="2">전공능력</th>
                            <td id="" colspan="8">
                            	<table class="table in_table">
                                    <caption class="blind">핵심역량</caption>
                                    <colgroup>
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                        <col width="auto">
                                    </colgroup>
                                    <thead>
                                        <tr id="abiList">
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr id="abiVal">
                                        </tr>
                                    </tbody>
                                </table>
							</td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2" rowspan="5">수업목표</th>
                            <td class="text-center">[핵심역량 연계 목표]</td>
                            <td id="coreGoalPlan" colspan="7"></td>
                        </tr>
                        <tr>
                        	 <td class="text-center">[전공능력 연계 목표]</td>
                            <td id="majorGoalPlan" colspan="7"></td>
                        </tr>
                        <tr>
                        	<td class="text-center">수업목표 1</td>
                            <td id="clsGoal1Plan" colspan="7"></td>
                        </tr>
                        <tr>
                        	<td class="text-center">수업목표 2</td>
                            <td id="clsGoal2Plan" colspan="7"></td>
                        </tr>
                        <tr>
                        	<td class="text-center">수업목표 3</td>
                            <td id="clsGoal3Plan" colspan="7"></td>
                        </tr>
                        <tr>
                            <th id="classTypePlan" scope="row" colspan="2" rowspan="11">수업유형</th>
                            <td class="text-center">구분</td>
                            <td class="text-center" colspan="7">내용</td>
                        </tr>
                        <tr>
                            <td class="text-center" rowspan="2">Anchor 학사제도</td>
                            <td id="focusFlPlan" colspan="7"></td>
                        </tr>
                        <tr>
                            <td colspan="7">▣ 운영계획 :
                            	<span id="focusMethodPlan"></span>
                            </td>
                        </tr>
                        <tr id="linkList">
                            <td id="linkCnt" class="text-center">산학연연계</td>
                            <td colspan="3" class="text-center">연계 기업(기관)</td>
                            <td colspan="4" class="text-center">주요내용</td>
                        </tr>
                        <tr>
                            <td colspan="3"></td>
                            <td colspan="4"></td>
                        </tr>
                        <tr>
                            <td class="text-center" rowspan="2">비교과연계</td>
                            <td colspan="7">▣ 연계부서 :
                            	<span id="nonSbjtDeptNmPlan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="7">▣ 운영계획 :
                            	<span id="nonnSbjtMethodPlan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-center" rowspan="2">해양DNA교과</td>
                            <td colspan="7">
                            	<span id="fuseFlPlan"></span>
                            	<span id="linkFlPlan"></span>
                            	<span id="microFlPlan"></span>
                            	<span id="nanoFlPlan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="7">▣ 운영계획 :
                            	<span id="dnaMethodPlan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-center">기타</td>
                            <td id="clsTpEtcPlan" colspan="6"></td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2" rowspan="6">교수학습</th>
                            <td id="" colspan="8">▣ 교수학습환경 : 
                            	<span id="offlinePlan"></span>
                            	<span id="onlinePlan"></span>
                            	<span id="blendPlan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="8">▣ 교수학습방법</td>
                        </tr>
                        <tr>
                            <td colspan="8">
                            	<span id="typeCode1Plan"></span>
                            	<span id="typeCode2Plan"></span>
                            	<span id="typeCode3Plan"></span>
                            	<span id="typeCode4Plan"></span>
                            	<span id="typeCode5Plan"></span>
                            	<span id="typeCode6Plan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="8">
                            	<span id="typeCode7Plan"></span>
                            	<span id="typeCode8Plan"></span>
                            	<span id="typeCode9Plan"></span>
                            	<span id="typeCode10Plan"></span>
							</td>
                        </tr>
                        <tr>
                            <td colspan="8">
                            	<span id="typeCode11Plan"></span>
                            	<span id="typeCode12Plan"></span>
                            	<span id="typeCode13Plan"></span>
                            	<span id="typeCode14Plan"></span>
                            	<span id="typeCode15Plan"></span>
							</td>
                        </tr>
                        <tr>
                            <td colspan="8">
                            	<span id="typeDescPlanBefore"></span>
                            	기타 : 
                            	<span id="typeDescPlanAfter"></span>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2" rowspan="9">평가방법·내용</th>
                            <td colspan="2" class="text-center">평가방법</td>
                            <td colspan="5" class="text-center">내용</td>
                            <td class="text-center">비율(%)</td>
                        </tr>
                        <tr>
                            <td class="text-center">중간평가</td>
                            <td id="midCodePlan" class="text-center"></td>
                            <td id="midDescPlan" colspan="5"></td>
                            <td id="midRtPlan" class="text-center"></td>
                        </tr>
                        <tr>
                            <td class="text-center">기말평가</td>
                            <td id="endCodePlan" class="text-center"></td>
                            <td id="endDescPlan" colspan="5"></td>
                            <td id="endRtPlan" class="text-center"></td>
                        </tr>
                        <tr>
                            <td class="text-center">출석</td>
                            <td id="attCodePlan" class="text-center"></td>
                            <td id="attDescPlan" colspan="5"></td>
                            <td id="attRtPlan" class="text-center"></td>
                        </tr>
                        <tr>
                            <td id="etcType1Plan" class="text-center"></td>
                            <td id="etcCode1Plan" class="text-center"></td>
                            <td id="etcDesc1Plan" colspan="5"></td>
                            <td id="etcRt1Plan" class="text-center"></td>
                        </tr>
                        <tr>
                            <td id="etcType2Plan" class="text-center"></td>
                            <td id="etcCode2Plan" class="text-center"></td>
                            <td id="etcDesc2Plan" colspan="5"></td>
                            <td id="etcRt2Plan" class="text-center"></td>
                        </tr>
                        <tr>
                            <td id="etcType3Plan" class="text-center"></td>
                            <td id="etcCode3Plan" class="text-center"></td>
                            <td id="etcDesc3Plan" colspan="5"></td>
                            <td id="etcRt3Plan" class="text-center"></td>
                        </tr>
                        <tr>
                            <td id="etcType4Plan" class="text-center"></td>
                            <td id="etcCode4Plan" class="text-center"></td>
                            <td id="etcDesc4Plan" colspan="5"></td>
                            <td id="etcRt4Plan" class="text-center"></td>
                        </tr>
                        <tr>
                            <td colspan="7" class="text-center">계</td>
                            <td id="totalRtPlan" class="text-center"></td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2">CQI반영 개선 사항</th>
                            <td id="cqiImpPlan" colspan="8"></td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2" rowspan="3">장애학생을 위한 수업지원</th>
                            <td id="" colspan="8">▣ 학습지원 :
                            	<span id="studySupp1Plan"></span>
                            	<span id="studySupp2Plan"></span>
                            	<span id="studySupp3Plan"></span>
                            	<span id="studySupp4Plan"></span>
                            	<span id="studySupp5Plan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td id="" colspan="8">▣ 평가지원 :
                            	<span id="estiSupp1Plan"></span>
                            	<span id="estiSupp2Plan"></span>
                            	<span id="estiSupp3Plan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="8">▣ 기타 : 
                            	<span id="etcSuppPlan"></span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </section>

           <section class="mt-4">
                <h2 class="d-flex justify-content-between align-items-center mb-3">교수학습자료</h2>
                <table class="table border-top appr_table">
                    <caption class="blind">교수학습자료</caption>
                    <colgroup>
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                    </colgroup>
                    <tbody>
                        <tr>
                        	<th id="bookCnt" scope="row" colspan="2">교재</th>
                            <td id="">구분</td>
                            <td id="" colspan="3">교재명</td>
                            <td id="">저자</td>
                            <td id="" colspan="2">출판사</td>
                            <td id="">출판년도</td>
                        </tr>
                        <tr id="bookList">
                        	<th scope="row" colspan="2" rowspan="3">교구</th>
                            <td colspan="8">▣ 장비/기구 : 
                            	<span id="toolEquPlan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="8">▣ 소프트웨어 : 
                            	<span id="toolSwPlan"></span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="8">▣ 기타 : 
                            	<span id="toolEtcPlan"></span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </section>
            
            <section class="mt-4">
                <h2 class="d-flex justify-content-between align-items-center mb-3">주차별 수업계획${weekList }</h2>
                <table class="table border-top appr_table">
                    <caption class="blind">주차별 수업계획</caption>
                    <colgroup>
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                        <col width="10%">
                    </colgroup>
                    <thead>
						<tr>
							<th class="">주차</th>
							<th colspan="3" class="">내용</th>
							<th class="">수업환경</th>
							<th colspan="2" class="">교수학습방법</th>
							<th colspan="2" class="">교수학습자료</th>
							<th class="">비고</th>
						</tr>
					</thead>
                    <tbody id="weekList">
                    </tbody>
                </table>
            </section>
        </div>
      </div>
    </div>
  </div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>