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
	<div class="sub_background sbjt_bg">
                <section class="inner">
                    <h3 class="title fw-bolder text-center text-white">교과목(전공·교양) 검색</h3>
                    <p class="sub_title_script">전공 및 교양 교육과정을 검색하고 상세정보를 확인할 수 있습니다.</p>
                    <!--검색창-->
		            
		            <input type="hidden" name="mId" id="mId" value="32"/>
		            <input type="hidden" name="univ" id="univ" value=""/><!-- 주관대학 -->
		            <input type="hidden" name="depart" id="depart" value=""/><!-- 학부(과) -->
		            <input type="hidden" name="major" id="major" value=""/><!-- 전공 -->
		            <input type="hidden" name="year" id="year" value=""/><!-- 년 -->
		            <input type="hidden" name="semester" id="semester" value=""/><!-- 학기 -->
		            <input type="hidden" name="grade" id="grade" value=""/><!-- 학년 -->
		            <input type="hidden" name="open" id="open" value=""/><!-- 최근개설 -->
		            
		            
                </section>
            </div>
            
            <!--본문-->
            <main class="inner mt-5">

                    <!--서치박스-->
                    <div class="search_wrap_bbox">
                    	<div class="search_keyword_section">
	                    	<span>키워드</span>
	                        <div class="schbox_top_section row_box_basic">
	                            <div class="input-group border rounded position-relative">
	                                <input type="text" class="form-control border-0" name="search" id="search" value="${param.top_search }" placeholder="검색어를 입력해주세요">
	                            </div>
	                            <button type="button" class="schbox_search_icon" id="btnSbjtSearch"></button>
	                        </div>
						</div>
						
                        <div class="row_box row_box_dtl d-flex flex-column flex-lg-row align-items-start">
                            <button class="h4 dtl_tit open bg-transparent border-0 text-start" type="button">검색옵션 <span>&nbsp;열기</span></button>
                            <div class="column_box_wrap">
                                <div class="column_box">
                                    <ul>
                                        <li class="unit unit_univ d-flex flex-column flex-sm-row align-items-start align-items-sm-center ">
                                            <span>주관대학</span>
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
                                                <li class="col-4">
                                                    <select class="form-select" id="college3" disabled>
                                                        <option value="" selected>전공</option>
                                                    </select>
                                                </li>
                                            </ul>
                                        </li>
                                        
                                        <li class="unit unit_univ d-flex flex-column flex-sm-row align-items-start align-items-sm-center ">
                                            <span>최근개설</span>
                                            
                                            <ul class="input-group gap-2">
                                                <li class="col-4">
                                                    <select class="form-select" id="openI">
                                                    	<option value="0" <c:if test="${param.open == 0 }">selected</c:if>>당해년도</option>
                                                        <option value="1" <c:if test="${param.open == 1 }">selected</c:if>>1년이내</option>
                                                        <option value="2" <c:if test="${param.open == 2 }">selected</c:if>>2년이내</option>
                                                        <option value="3" <c:if test="${param.open == 3 }">selected</c:if>>3년이내</option>
                                                        <option value="4" <c:if test="${param.open == 4 }">selected</c:if>>4년이내</option>
                                                    </select>
                                                </li>
                                            </ul>
                                        </li>
                                        
                                        <li class="unit unit_sort d-flex flex-column flex-sm-row align-items-start align-items-sm-center">
                                            <span>이수 학기/학년</span>
                                            <ul class="input-group gap-2">
                                                <!-- <li class="col-4">
                                                    <select class="form-select" id="yearI" name="yearI">
                                                    </select>
                                                </li> -->
                                                <li class="col-4">
                                                    <select class="form-select" id="semeI" name="semeI">
                                                    	<option value="" selected>학기</option>
                                                        <option value="GH0210" <c:if test="${param.semeI == 'GH0210' }">selected</c:if>>1학기</option>
                                                        <option value="GH0211" <c:if test="${param.semeI == 'GH0211' }">selected</c:if>>여름계절학기</option>
                                                        <option value="GH0220" <c:if test="${param.semeI == 'GH0220' }">selected</c:if>>2학기</option>
                                                        <option value="GH0221" <c:if test="${param.semeI == 'GH0221' }">selected</c:if>>겨울계절학기</option>
                                                        <!-- <option value="GH0211" selected>여름계절학기</option>
                                                        <option value="GH0221">겨울계절학기</option> -->
                                                    </select>
                                                </li>
                                                <li class="col-4">
                                                    <select class="form-select" id="gradeI" name="gradeI">
                                                    	<option value="" selected>학년</option>
                                                        <option value="1" <c:if test="${param.gradeI == 1 }">selected</c:if>>1학년</option>
                                                        <option value="2" <c:if test="${param.gradeI == 2 }">selected</c:if>>2학년</option>
                                                        <option value="3" <c:if test="${param.gradeI == 3 }">selected</c:if>>3학년</option>
                                                        <option value="4" <c:if test="${param.gradeI == 4 }">selected</c:if>>4학년</option>
                                                        <option value="5" <c:if test="${param.gradeI == 5 }">selected</c:if>>5학년</option>
                                                    </select>
                                                </li>
                                            </ul>
                                        </li>
                                        
                                        
                                        
                                        <li class="unit d-flex schbox_radio_label_custom">
                                            <span>분류</span>
                                            <ul class="input-group">
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="UE010021" id="std_complet_1" name="std_complet_1" <c:if test="${param.std_complet == 'UE010021' }">checked</c:if>> 
                                                    <label class="form-check-label" for="std_complet_1">전공필수</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="UE010022" id="std_complet_2" name="std_complet_2" <c:if test="${param.std_complet == 'UE010022' }">checked</c:if>>
                                                    <label class="form-check-label" for="std_complet_2">전공선택</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="UE010024" id="std_complet_3" name="std_complet_3" <c:if test="${param.std_complet == 'UE010024' }">checked</c:if>>
                                                    <label class="form-check-label" for="std_complet_3">전공기초</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="UE010011" id="std_complet_4" name="std_complet_4" <c:if test="${param.std_complet == 'UE010011' }">checked</c:if>>
                                                    <label class="form-check-label" for="std_complet_4">교양필수</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="UE010012" id="std_complet_5" name="std_complet_5" <c:if test="${param.std_complet == 'UE010012' }">checked</c:if>>
                                                    <label class="form-check-label" for="std_complet_5">교양선택</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="UE010080" id="std_complet_6" name="std_complet_6" <c:if test="${param.std_complet == 'UE010080' }">checked</c:if>>
                                                    <label class="form-check-label" for="std_complet_6">일반선택</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value=UE010031 id="std_complet_7" name="std_complet_7" <c:if test="${param.std_complet == 'UE010031' }">checked</c:if>>
                                                    <label class="form-check-label" for="std_complet_7">교직</label>  
                                                </li>
                                            </ul>
                                        </li>
                                        <li class="unit d-flex schbox_radio_label_custom">
                                            <span>핵심역량</span>
                                            <ul class="input-group">
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="해양전문지식" id="std_core_1" name="std_core_1" <c:if test="${fn:contains(stdCore, '해양전문지식')}">checked</c:if>> 
                                                    <label class="form-check-label" for="std_core_1">해양전문지식</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="지식정보활용" id="std_core_2" name="std_core_2" <c:if test="${fn:contains(stdCore, '지식정보활용')}">checked</c:if>>
                                                    <label class="form-check-label" for="std_core_2">지식정보활용</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="융복합적사고" id="std_core_3" name="std_core_3" <c:if test="${fn:contains(stdCore, '융복합적사고')}">checked</c:if>>
                                                    <label class="form-check-label" for="std_core_3">융복합적사고</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="문제해결력" id="std_core_4" name="std_core_4" <c:if test="${fn:contains(stdCore, '문제해결력')}">checked</c:if>>
                                                    <label class="form-check-label" for="std_core_4">문제해결력</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="세계시민성" id="std_core_5" name="std_core_5" <c:if test="${fn:contains(stdCore, '세계시민성')}">checked</c:if>>
                                                    <label class="form-check-label" for="std_core_5">세계시민성</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="글로벌네트워킹" id="std_core_6" name="std_core_6" <c:if test="${fn:contains(stdCore, '글로벌네트워킹')}">checked</c:if>>
                                                    <label class="form-check-label" for="std_core_6">글로벌네트워킹</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="자기관리" id="std_core_7" name="std_core_7" <c:if test="${fn:contains(stdCore, '자기관리')}">checked</c:if>>
                                                    <label class="form-check-label" for="std_core_7">자기관리</label>
                                                </li>
                                                <li class="">
                                                    <input class="form-check-input" type="checkbox" value="사회적책임감" id="std_core_8" name="std_core_8" <c:if test="${fn:contains(stdCore, '사회적책임감')}">checked</c:if>>
                                                    <label class="form-check-label" for="std_core_8">사회적책임감</label>
                                                </li>
                                            </ul>
                                        </li>
                                        
                                        
                                    </ul>
                                </div>
                                <!--div class="column_box">
                                    <ul>
                                        <li class="unit d-flex flex-column flex-sm-row align-items-start align-items-sm-center">
                                            <span>최근개설</span>
                                            <ul>
                                                <li class="col-12 col-sm-4">
                                                    <select class="form-select" id="openI">
                                                        <option value="1" <c:if test="${param.open == 1 }">selected</c:if>>1년이내</option>
                                                        <option value="2" <c:if test="${param.open == 2 }">selected</c:if>>2년</option>
                                                        <option value="3" <c:if test="${param.open == 3 }">selected</c:if>>3년</option>
                                                    </select>
                                                </li>
                                            </ul>
                                        </li>
                                    </ul>
                                </div-->
                            </div>
                        </div>
                        <div class="schbox_bottom_section">
                        	<button type="button" class="reload_btn" onclick="reloadFilter();"><img src="../images/ico_reload.png" alt="초기화 아이콘"/>초기화</button>
                    		<button type="button" class="schbox_search" id="btnSbjtDetailSearch">상세검색</button>
                    	</div>
                    </div>

					<div class="sbjt_title_box title_box pb-3 mb-3">
						<div class="sbjt_title_boxselect">
							<select class="form-select rounded-pill" aria-label="select" id="orderBy" name="orderBy">
								<option value="">정렬기능</option>
								<option value="bySbjtName">교과명순</option>
								<option value="bySbjtType">교과분류순</option>
								<option value="byGrade">학년순</option>
								<option value="byRegidate">최근편성순</option>
								<option value="byMajor">전공순</option>
							</select>
						</div>
					</div>
                
                <div class="majref_wrap d-flex flex-wrap" id="sbjtList">
                	<%-- <c:forEach var="listDt" items="${sbjtList }" varStatus="i">
                	<c:set var="listIdxName" value=""/>
                	<c:set var="subjectKey" value="${listDt.subjectCd }"/>
                	<c:set var="deptKey" value="${listDt.deptCd }"/>
                	<c:if test="${!empty listDt.majorCd }"><c:set var="deptKey" value="${listDt.majorCd }"/></c:if>
                	<c:set var="year" value="${listDt.year }"/>
                	<c:set var="smtCd" value="${listDt.smtCd }"/>
               		<div class="item border">
                        <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-3">
                        	<c:choose>
                        		<c:when test="${listDt.comdivNm == '전공기초'}"> <li class="maj_choice">${listDt.comdivNm }</li></c:when> 
                        		<c:when test="${listDt.comdivNm == '전공필수'}"> <li class="maj_esse">${listDt.comdivNm }</li></c:when>
                        		<c:when test="${listDt.comdivNm  == '교양'}"> <li class="refin">${listDt.comdivNm }</li></c:when>
                        		<c:otherwise><li>${listDt.comdivNm }</li></c:otherwise>
                        	</c:choose>
                            <li class="name_of_class"><span>${listDt.colgNm }</span><span>${listDt.deptNm }</span></li>
                        </ul>
                        <h5 class="title ellip_2 mb-3">
                        	<a href="<c:out value="${URL_VIEW}"/>&SUBJECT_CD=${subjectKey}&DEPT_CD=${deptKey}&YEAR=${year}&SMT=${smtCd}" title="제목" class="d-block  fw-semibold">${listDt.subjectNm }</a>
                        	<a href="javascript:void(0);" onclick="sbjtView('${subjectKey}', '${deptKey}', '${year}', '${smtCd}')" title="제목" class="d-block  fw-semibold">${listDt.subjectNm }</a>
                        </h5>
                        <p class="desc_txt mb-4 ellip_2">
                        	<c:if test="${empty listDt.subjDescKor }">등록된 설명이 없습니다.</c:if>${listDt.subjDescKor}
                        </p>
                        <ul class="info d-flex flex-wrap align-items-start">
                            <li class="d-flex flex-row col-12 col-xl-6 mb-2"><strong>편성</strong><span class="rounded-pill">${listDt.year}년 ${listDt.grade}학년 ${listDt.smtNm}</span></li>
                            <li class="d-flex flex-row col-12 col-xl-6 mb-2"><strong>과정구분</strong><span>${listDt.sinbunNm}</span></li>
                            <li class="d-flex flex-row col-12 col-xl-6 mb-2 mb-xl-0"><strong>학점</strong><span class="rounded-pill">학점 ${listDt.cdtNum}</span></li>
                        </ul>
                    </div>
                	</c:forEach> --%>
                </div>
                
                <!-- 페이지 내비게이션 -->
				<ul class="pagination gap-2 justify-content-center mt-5" id="page">
				</ul>
            </main>
        </div>
    </div>
 
<form name="viewForm" method="POST" action="../sbjt/view.do?mId=32">
	<input type="hidden" name="mId" id="mId" value="32"/>
	<input type="hidden" name="SUBJECT_CD" id="SUBJECT_CD" value=""/>
	<input type="hidden" name="DEPT_CD" id="DEPT_CD" value=""/>
	<input type="hidden" name="YEAR" id="YEAR" value=""/>
	<input type="hidden" name="SMT" id="SMT" value=""/>
</form>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>