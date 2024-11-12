<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
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
	<div class="sub_background profess_bg">
                <section class="inner">
                    <h3 class="title fw-bolder text-center text-white">교수</h3>
                </section>
            </div>
            <!--본문-->
            <section class="inner mt-5">
                <!--item 상세-->
                <section class="detail_title_wrap">
                    <section class="d-flex flex-row justify-content-between align-items-center title_box px-2 py-3 mb-5">
                        <a href="javascript:history.back();" title="이전페이지" class="d-flex flex-row align-items-center gap-2"><img src="../images/arr_blue.png"/><em class="fst-normal">이전페이지</em></a>
                        <h5 class="content_title text-center fw-bolder d-flex align-items-center justify-content-center gap-1 flex-wrap">
                            [교수]
                            <span class="col-12 col-md-auto text-truncate">${profInfo.EMP_NM }</span>
                        </h5>
                        <div id="${profInfo.EMP_NO}" class="like_container">
                            <div class="link_cnt text-end">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange('${profInfo.EMP_NO}','prof');">
                                <path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>
                                <path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>
                            </div>
                        </div>
                    </section>
                </section>
                <!--교수상세 테이블-->  
                <main class="profss_dtl_wrap">
                    <h5 class="fw-semibold mb-3">교수상세</h5>
                    <div class="profss_info d-flex flex-column flex-sm-row gap-4 align-items-stretch">
                        <div class="photo_box">
                        	<c:choose>
                        		<c:when test="${!empty profInfo.TEA_FILE_PATH }"><img src="https://www.kmou.ac.kr/${profInfo.TEA_FILE_PATH }" alt="교수님 사진"/></c:when>
                        		<c:otherwise><img src="../images/profile.png" alt="교수님 사진"/></c:otherwise>	
                        	</c:choose>
                        	
                            
                        </div>
                        <table class="table mb-0">
                            <caption class="blind">교수님 상세 정보</caption>
                            <colgroup>
                                <col width="10%"/>
                                <col width="40%"/>
                                <col width="10%"/>
                                <col width="40%"/>
                            </colgroup>
                            <tbody>
                                <tr>
                                    <th scope="col" class="align-middle ps-3">교수명</th>
                                    <td class="align-middle">${profInfo.EMP_NM }</td>
                                    <th scope="col" class="align-middle ps-3">소속</th>
                                    <td class="align-middle">${profInfo.COLG_NM } > ${profInfo.DEPT_NM }</td>
                                </tr>
                                <tr>
                                    <th scope="col" class="align-middle ps-3">전화번호</th>
                                    <td class="align-middle">${profInfo.TEA_TLPHON }</td>
                                    <th scope="col" class="align-middle ps-3">이메일</th>
                                    <td class="align-middle text-break">${profInfo.TEA_EMAIL }</td>
                                </tr>
                                <tr>
                                    <th scope="col" class="align-middle ps-3">연구실</th>
                                    <td class="align-middle">${profInfo.TEA_LOCATION }</td>
                                    <th scope="col" class="align-middle ps-3">홈페이지</th>
                                    <td class="align-middle text-break">-</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </main>

                <!--강의과목-->
                <section class="prof_section prof_lectu">
                    <h5 class="fw-semibold mb-3">강의과목(최근5년)</h5>
                    <div class="">
                        <table class="table">
                            <caption class="blind">교수님 강의과목</caption>
                            <colgroup>
                                <col width="24%"/>
                                <col width="8%"/>
                                <col width="8%"/>
                                <col width="8%"/>
                                <col width="18%"/>
                                <col width="18%"/>
                                <col width="8%"/>
                                <col width="8%"/>
                            </colgroup>
                            <thead>
                                <tr>
                                    <th scope="row" class="text-center align-middle">교과목명</th>
                                    <th scope="row" class="text-center align-middle">학년도</th>
                                    <th scope="row" class="text-center align-middle">학사</th>
                                    <th scope="row" class="text-center align-middle">주야</th>
                                    <th scope="row" class="text-center align-middle">개설대학(원)</th>
                                    <th scope="row" class="text-center align-middle">개설학과</th>
                                    <th scope="row" class="text-center align-middle">과정구분</th>
                                    <th scope="row" class="text-center align-middle">교과목번호-분반</th>
                                </tr>
                            </thead>
                            <tbody class="subListTbody">
                                
                            </tbody>
                        </table>

                        <!--paging-->
                        <ul class="pagination gap-2 justify-content-center mt-5" id="page">
						</ul>
                       
                    </div>
                </section>

                <!--학력-->
                <section class="prof_section prof_level">
                    <h5 class="fw-semibold mb-3">학력</h5>
                    <div class="box">
                        <p>${elfn:replaceHtmlY(profInfo.TEA_MAIN)}</p>   
                    </div>
                </section>

                <!--경력-->
                <section class="prof_section prof_career">   
                    <h5 class="fw-semibold mb-3">경력</h5>
                    <div class="box">
                        <p>${elfn:replaceHtmlY(profInfo.TEA_CAREER) }</p>
                    </div>
                </section>
                
                <!--연구분야-->
                <section class="prof_section prof_career">
                    <h5 class="fw-semibold mb-3">연구분야</h5>
                    <div class="box">
                        <p>${elfn:replaceHtmlY(profInfo.TEA_RSRCH_REALM) }</p>
                    </div>
                </section>
                

                <!--목록으로-->
                <a href="javascript:history.back();" title="목록으로" class="back_to_list">
                    목록으로
                </a>
            </section>
            
<form name="sbjtViewForm" method="POST" action="../sbjt/view.do?mId=32">
	<input type="hidden" name="mId" id="mId" value="32"/>
	<input type="hidden" name="SUBJECT_CD" id="SUBJECT_CD" value=""/>
	<input type="hidden" name="DEPT_CD" id="DEPT_CD" value=""/>
	<input type="hidden" name="YEAR" id="YEAR" value=""/>
	<input type="hidden" name="SMT" id="SMT" value=""/>
</form>
    
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>