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
	 <div class="sub_background major_bg">
	     <section class="inner">
	         <h3 class="title fw-bolder text-center text-white">통합검색</h3>
	     </section>
     </div>
     
     <section class="inner mt-4">
                <h4 class="resu_word text-center fw-semibold pb-3"><mark>${top_search }</mark>에 대해 총 <span>${totalCount }</span>건이 검색되었습니다.</h4>

                <!--전공교양-->
                <section class="resu_box">
                    <h4 class="d-flex flex-row align-items-center gap-3 mb-3">전공&middot;교양
                        <p>검색결과 <span>${sbjtCount }</span></p>
                    </h4>
                    <div class="majref_wrap d-flex flex-wrap gap-2">
                        <c:forEach var="listDt" items="${sbjtList }" varStatus="i">
                        <c:set var="listIdxName" value=""/>
                        <c:set var="subjectNm" value="${listDt.subjectNm }"/>
                        <c:if test="${empty listDt.subjectNm }"><c:set var="subjectNm" value="-"/></c:if>
	                	<c:set var="subjectKey" value="${listDt.subjectCd }"/>
	                	<c:set var="deptKey" value="${listDt.colgCd }"/>
	                	<c:if test="${!empty listDt.deptCd }"><c:set var="deptKey" value="${listDt.deptCd }"/></c:if>
	                	<c:if test="${!empty listDt.majorCd }"><c:set var="deptKey" value="${listDt.majorCd }"/></c:if>
	                	<c:set var="year" value="${listDt.year }"/>
	                	<c:set var="grade" value="${listDt.grade }"/>
	                	<c:set var="smtCd" value="${listDt.smtCd }"/>
	                	<c:choose>
	                		<c:when test="${fn:contains(listDt.comdivNm, '전공') }"><c:set var="mainAbi" value="${listDt.majorAbi }"/></c:when>
	                		<c:when test="${fn:contains(listDt.comdivNm, '교양') }"><c:set var="mainAbi" value="${listDt.essentialAbi }"/></c:when>
	                		<c:when test="${fn:contains(listDt.comdivNm, '일반') }"><c:set var="mainAbi" value="${listDt.majorAbi }"/></c:when>
	                		<c:otherwise></c:otherwise>
	                	</c:choose>
	                	<c:set var="sbjtId" value="${subjectKey}_${deptKey}_${year}_${grade}_${smtCd}"/>
	               		 <div class="item border" onclick="sbjtView('${subjectKey}', '${deptKey}', '${year}', '${smtCd}')" style="cursor:pointer;">
	               		 	<div id="${sbjtId}" class="like_container ' + onRed + '">
				 				<div class="link_cnt text-end">
				 					<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18"  onclick="event.stopPropagation();likeChange('${sbjtId}', 'sbjt')">
				 					<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>
				 					<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>
				 				</div>
 	  						</div>
	                        <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-3">
	                        	<c:choose>
	                        		<c:when test="${listDt.comdivNm == '전공과목' }"><li class="maj">${listDt.comdivNm }</li></c:when>
	                        		<c:when test="${listDt.comdivNm == '전공기초' }"><li class="maj_basic">${listDt.comdivNm }</li></c:when>
	                        		<c:when test="${listDt.comdivNm == '전공필수' }"><li class="maj_esse">${listDt.comdivNm }</li></c:when>
	                        		<c:when test="${listDt.comdivNm == '전공선택' }"><li class="maj_choice">${listDt.comdivNm }</li></c:when>
	                        		<c:when test="${listDt.comdivNm == '일반선택' }"><li class="general_selection">${listDt.comdivNm }</li></c:when>
	                        		<c:when test="${!empty fn:contains(listDt.comdivNm, '교양')}"><li class="refin">${listDt.comdivNm }</li></c:when>
	                        		<c:otherwise></c:otherwise>
	                        	</c:choose>
	                            <li class="name_of_class"><span>${listDt.colgNm }</span><c:if test="${!empty listDt.deptNm }"><span>${listDt.deptNm }</span></c:if></li>
	                            <c:if test="${!empty mainAbi }"> <li class="name_of_class"><span>${mainAbi }</span></li></c:if>
	                        </ul>
	                        <h5 class="d-flex flex-wrap align-itmes-end">
	                        <a href="javascript:void(0);" onclick="sbjtView('${subjectKey}', '${deptKey}', '${year}', '${smtCd}')" title="제목" class="d-block  fw-semibold"> ${subjectNm}(${subjectKey})</a>
	                        <%-- <a href="/web/sbjt/view.do?mId=32&SUBJECT_CD=${subjectKey}&DEPT_CD=${deptKey}&YEAR=${year}&SMT=${smtCd}" title="제목" class="d-block  fw-semibold">${listDt.subjectNm }</a> --%>
	                        </h5>
	                        <p class="desc_txt mb-3 text-truncate">
	                        	<c:if test="${empty listDt.subjDescKor }">등록된 설명이 없습니다.</c:if>${listDt.subjDescKor}
	                        </p>
	                        <ul class="info d-flex flex-wrap align-items-center">
	                        	<li class="d-flex flex-row align-items-center gap-2 order-1"><strong>편성</strong><span class="rounded-pill">${listDt.year}년 ${listDt.grade}학년 ${listDt.smtNm}</span></li>
	                        	<li class="d-flex flex-row align-items-center gap-2 order-2"><strong>과정구분</strong><span>${listDt.sinbunNm}</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-3"><strong>학점</strong><span class="rounded-pill">학점 ${listDt.cdtNum}</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-4"><strong>이수학년</strong>
	                                <span>
	                                	<c:choose>
	                                		<c:when test="${listDt.grade == '0'}">전체학년</c:when>
	                                		<c:otherwise>${listDt.grade}학년</c:otherwise>
	                                	</c:choose>
	                                </span>
                                </li>
                                <!-- <li class="d-flex flex-row align-items-center gap-2 order-4"><strong>역량구분</strong><span>사회적실천(주), 통섭적 사고, 주도적 학습</span></li> -->
	                        </ul>
	                    </div>
                        </c:forEach>
                    </div>
                    <form name="sbjtForm" method="POST" action="${elfn:getMenuUrl(32)}">
                    	<input type="hidden" name="mId" value="32"/>
                    	<input type="hidden" name="top_search" value="${top_search }"/>
                    	<a href="javascript:sbjtList('${top_search }');" title="검색결과더보기" class="resu_more_btn">교과목 검색결과 더보기</a>
                    </form>
                </section>

                <!--비교과-->                
				<section class="resu_box">
				    <h4 class="d-flex flex-row align-items-center gap-3 mb-3">비교과<p>검색결과 <span>${nonSbjtCount}건</span></p></h4>
				    
				    <div id="searchResult" class="nonSbjtSearchResult">
	                </div>
				    
				    <!--  div class="non_majref_wrap d-flex flex-wrap gap-2">
				        <c:forEach var="listDt" items="${nonSbjtList}" varStatus="status">
				            <div class="item border" onclick="nonSbjtView('${listDt.IDX}','${listDt.TIDX}');" style="cursor:pointer;">
				                <h5 class="gap-2 mb-3">
				                    <c:choose>
				                        <c:when test="${listDt.D_DAY eq '종료'}">
				                            <span class="tag_end">${listDt.D_DAY}</span>
				                        </c:when>
				                        <c:otherwise>
				                            <span class="tag_adv">${listDt.D_DAY}</span>
				                        </c:otherwise>
				                    </c:choose>    
				                    <a href="javascript:void(0);" title="제목" class="text-truncate">${listDt.TITLE}</a>
				                </h5>
				                <div class="d-flex flex-column flex-sm-row flex-md-column">
				                    <dl class="d-flex flex-wrap mb-1">
				                        <dt>모집기간</dt>
				                        <dd>${listDt.SIGNIN_START_DATE}(${listDt.SIGNIN_START_DAY}) ~ ${listDt.SIGNIN_END_DATE}(${listDt.SIGNIN_END_DAY})</dd>
				                    </dl>
				                    <dl class="d-flex flex-wrap mb-1">
				                        <dt>운영기간</dt>
				                        <dd>${listDt.START_DATE}(${listDt.START_DAY}) ~ ${listDt.END_DATE}(${listDt.END_DAY})</dd>
				                    </dl>
				                </div>
				                <dl class="d-flex flex-row">
				                    <dt>모집인원</dt>
				                    <dd>
				                        <strong>${listDt.SIGNIN_LIMIT}</strong> / <span>${listDt.PARTICIPANT}</span> 명 
				                        <c:choose>
				                            <c:when test="${listDt.D_DAY eq '종료'}">
				                                모집완료
				                            </c:when>
				                            <c:otherwise>
				                                지원중
				                            </c:otherwise>
				                        </c:choose>   
				                    </dd>
				                </dl>
				            </div>
				        </c:forEach>
				    </div> -->
				    <a href="javascript:nonSbjtList('${top_search}');" title="검색결과더보기" class="resu_more_btn">비교과 검색결과 더보기</a>
				</section>

                <!--전공-->
                <section class="resu_box">
                    <h4 class="d-flex flex-row align-items-center gap-3 mb-3">전공
                        <p>검색결과 <span>${majorCount }건</span></p>
                    </h4>
                    <div class="speci_wrap d-flex flex-wrap">
	                    
	                    <!-- ------------------------------------------------------------------------------------- -->
	                    
	                    <c:forEach var="listDt" items="${majorList }" varStatus="i">
                        <c:set var="listIdxName" value=""/>
	                	<c:set var="majorKey" value="${listDt.MAJOR_CD }"/>
	                	
                        <div class="item border" onclick="majorView('${majorKey}')" style="cursor:pointer;">
                        	<div id="${majorKey}" class="like_container">
                        		<div class="link_cnt text-end">
                        		<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18"  onclick="event.stopPropagation();likeChange('${majorKey}', 'major')">
                        		<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>
                        		<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>
                        		</div>
                        	</div>
                            <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-2">
                                <li><span>${listDt.COLG_NM}</span><span>${listDt.DEPT_NM}</span></li>
                            </ul>
                            <h5 class="title mb-3">
                                <a href="javascript:majorView('${majorKey}');" title="제목" class="d-block  fw-semibold">
                                    ${listDt.MAJOR_NM_KOR} <br>
                                    <small class="d-inlinle-block fw-normal mt-1">${listDt.MAJOR_NM_ENG}</small>
                                   
                                </a>
                            </h5>
                            <dl class="d-flex flex-row mb-1">
                                <dt>전공 목표</dt>
                                <dd>${listDt.GOAL }</dd>
                            </dl>
                            <dl class="d-flex flex-row ">
                                <dt>진로</dt>
                                <dd>${listDt.FIELD}</dd>
                            </dl>
                        </div>
                        
                        </c:forEach>
                        <!-- ------------------------------------------------------------------------------------- -->
                    </div>
                    <a href="javascript:majorList('${top_search }');" title="검색결과더보기" class="resu_more_btn">전공 검색결과 더보기</a>
                </section>

                <!--교수-->
                <section class="resu_box">
                    <h4 class="d-flex flex-row align-items-center gap-3 mb-3">교수
                        <p>검색결과 <span>${profCount }건</span></p>
                    </h4>
                    <div class="profss_wrap d-flex flex-wrap">
                    	<c:forEach var="listDt" items="${profList }">
                    	<c:if test="${!empty listDt.file_path }"><c:set var="imagePath" value="https://www.kmou.ac.kr/${listDt.file_path}"/></c:if>
                    	<c:if test="${empty listDt.file_path }"><c:set var="imagePath" value="../images/bg_sub_profess.png"/></c:if>
                    	<c:set var="empKey" value="${listDt.empNo }"/>
                        <div class="item border" onclick="profView('${empKey}')" style="cursor:pointer;">
                        	<div id="${empKey}" class="like_container ' + onRed + '">
				 				<div class="link_cnt text-end">
				 					<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18"  onclick="event.stopPropagation();likeChange('${empKey}', 'prof')">
				 					<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>
				 					<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>
				 				</div>
			 	  			</div>
                        	<a href="javascript:void(0);" onclick="profView('${empKey}')" title="교수" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3">
                            <%-- <a href="/web/prof/view.do?mId=33&empNo=${empKey}" title="교수" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3"> --%>
                            
                                <span class="photo_box d-inline-block rounded-circle overflow-hidden">
	                                <img src="${imagePath }" alt="교수님사진" style="width:100%; height:100%; object-fit:cover;"/>
	                            </span>
                                <span class="txt_box">
                                    <strong>${listDt.empNm }<small class="fw-normal ps-2">교수</small></strong>
                                    <span class="d-flex flex-wrap">
                                        <em class="fst-normal d-inline-block">${listDt.deptNm }</em>
                                        <em class="fst-normal d-inline-block">기계조선에너지시스템 공학전공</em>
                                    </span>
                                </span>
                            </a>
                            <ul class="dtl_info pt-4 d-flex flex-column gap-2">
                                <li class="d-flex flex-row text-break"><strong>이메일</strong>${listDt.email }</li>
	                            <li class="d-flex flex-row text-break"><strong>전화번호</strong>${listDt.tlphon }</li>
	                            <li class="d-flex flex-row text-break"><strong>연구분야</strong>-</li>
	                            <li class="d-flex flex-row text-break"><strong>연구실</strong>${listDt.labRum }</li>
                            </ul>
                        </div>
                        </c:forEach>
                    </div>
                    <form name="profForm" method="POST" action="${elfn:getMenuUrl(33)}">
                    	<input type="hidden" name="mId" value="33"/>
                    	<input type="hidden" name="top_search" value="${top_search }"/>
                    	<a href="javascript:profList('${top_search }');" title="검색결과더보기" class="resu_more_btn">교수 검색결과 더보기</a>
                    </form>
                </section>

                <!--학생설계전공-->
<!--                 <section class="resu_box"> -->
<!--                     <h4 class="d-flex flex-row align-items-center gap-3 mb-3">학생설계전공 -->
<!--                         <p>검색결과 <span>12건</span></p> -->
<!--                     </h4> -->
<!--                     <div class="plan_section d-flex flex-wrap"> -->
<!--                         <div class="item"> -->
<!--                             <ul class="d-flex flex-row gap-2 cate"> -->
<!--                                 <li class="year">2023</li> -->
<!--                                 <li class="border">복수전공</li> -->
<!--                                 <li class="border">부전공</li> -->
<!--                             </ul> -->
<!--                             <h3 class="title fs-5 fw-bolder text-start mb-3"> -->
<!--                                 <a href="#" title="학생설계전공 제목" class="text-truncate">데이터네트워크인공지능 학생설계전공</a> -->
<!--                             </h3> -->
<!--                             <div class="detail_maj position-relative"> -->
<!--                                 <span class="d-block fs-6 lh-sm mb-1">융합공학부 기계조전에너지시스템 공학전공</span> -->
<!--                                 <span class="d-block fs-6 lh-sm">국어국문학전공, 인문사회과학대학 전공</span> -->
<!--                             </div> -->
<!--                             <div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2"> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>수여학위</dt> -->
<!--                                     <dd>OOOOO학사</dd> -->
<!--                                 </dl> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>설계학점</dt> -->
<!--                                     <dd>OO학점</dd> -->
<!--                                 </dl> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>교과목설계범위</dt> -->
<!--                                     <dd>교내 교과목 + 온라인 공개강좌</dd> -->
<!--                                 </dl> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>개설유형</dt> -->
<!--                                     <dd>부전공</dd> -->
<!--                                 </dl> -->
<!--                             </div> -->
<!--                         </div> -->
<!--                         <div class="item"> -->
<!--                             <ul class="d-flex flex-row gap-2 cate"> -->
<!--                                 <li class="year">2023</li> -->
<!--                                 <li class="border">복수전공</li> -->
<!--                                 <li class="border">부전공</li> -->
<!--                             </ul> -->
<!--                             <h3 class="title fs-5 fw-bolder text-start mb-3"> -->
<!--                                 <a href="#" title="학생설계전공 제목" class="text-truncate">데이터네트워크인공지능 학생설계전공</a> -->
<!--                             </h3> -->
<!--                             <div class="detail_maj position-relative"> -->
<!--                                 <span class="d-block fs-6 lh-sm mb-1">융합공학부 기계조전에너지시스템 공학전공</span> -->
<!--                                 <span class="d-block fs-6 lh-sm">국어국문학전공, 인문사회과학대학 전공</span> -->
<!--                             </div> -->
<!--                             <div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2"> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>수여학위</dt> -->
<!--                                     <dd>OOOOO학사</dd> -->
<!--                                 </dl> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>설계학점</dt> -->
<!--                                     <dd>OO학점</dd> -->
<!--                                 </dl> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>교과목설계범위</dt> -->
<!--                                     <dd>교내 교과목 + 온라인 공개강좌</dd> -->
<!--                                 </dl> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>개설유형</dt> -->
<!--                                     <dd>부전공</dd> -->
<!--                                 </dl> -->
<!--                             </div> -->
<!--                         </div> -->
<!--                         <div class="item"> -->
<!--                             <ul class="d-flex flex-row gap-2 cate"> -->
<!--                                 <li class="year">2023</li> -->
<!--                                 <li class="border">복수전공</li> -->
<!--                                 <li class="border">부전공</li> -->
<!--                             </ul> -->
<!--                             <h3 class="title fs-5 fw-bolder text-start mb-3"> -->
<!--                                 <a href="#" title="학생설계전공 제목" class="text-truncate">데이터네트워크인공지능 학생설계전공</a> -->
<!--                             </h3> -->
<!--                             <div class="detail_maj position-relative"> -->
<!--                                 <span class="d-block fs-6 lh-sm mb-1">융합공학부 기계조전에너지시스템 공학전공</span> -->
<!--                                 <span class="d-block fs-6 lh-sm">국어국문학전공, 인문사회과학대학 전공</span> -->
<!--                             </div> -->
<!--                             <div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2"> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>수여학위</dt> -->
<!--                                     <dd>OOOOO학사</dd> -->
<!--                                 </dl> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>설계학점</dt> -->
<!--                                     <dd>OO학점</dd> -->
<!--                                 </dl> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>교과목설계범위</dt> -->
<!--                                     <dd>교내 교과목 + 온라인 공개강좌</dd> -->
<!--                                 </dl> -->
<!--                                 <dl class="d-flex flex-wrap"> -->
<!--                                     <dt>개설유형</dt> -->
<!--                                     <dd>부전공</dd> -->
<!--                                 </dl> -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <a href="#" title="검색결과더보기" class="resu_more_btn">학생설계전공 검색결과 더보기</a> -->
<!--                 </section> -->
            </section>

        </div>
        
        </div>
 
    
<form name="sbjtViewForm" method="POST" action="../sbjt/view.do?mId=32">
	<input type="hidden" name="mId" id="mId" value="32"/>
	<input type="hidden" name="SUBJECT_CD" id="SUBJECT_CD" value=""/>
	<input type="hidden" name="DEPT_CD" id="DEPT_CD" value=""/>
	<input type="hidden" name="YEAR" id="YEAR" value=""/>
	<input type="hidden" name="SMT" id="SMT" value=""/>
</form>
    
<form name="profViewForm" method="POST" action="../prof/view.do?mId=33">
	<input type="hidden" name="mId" class="mId" value="33"/>
	<input type="hidden" name="empNo" id="empNo" value=""/>
</form>

<form name="majorViewForm" method="POST" action="../major/view.do?mId=35">
	<input type="hidden" name="mId" class="mId" value="35"/>
	<input type="hidden" name="MAJOR_CD" id="MAJOR_CD" value=""/>
</form>

<form name="nonSbjtViewForm" method="POST" action="/web/nonSbjt/view.do">
   	<input type="hidden" name="mId" value="34"/>
   	<input type="hidden" name="idx" value=""/>
   	<input type="hidden" name="tidx" value=""/>
</form>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>