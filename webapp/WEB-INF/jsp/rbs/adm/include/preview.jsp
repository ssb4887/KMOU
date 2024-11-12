<%@ include file="../../../../include/commonTop.jsp"%>
<link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/>
<link rel="stylesheet" href="../../../assets/css/style.css">
<link rel="stylesheet" type="text/css" href="${contextPath}${cssAssetPath}/sub.css">
<link rel="stylesheet" href="../../../css/contents.css">
<script src="../../../assets/js/jquery-3.7.1.min.js"></script>
<script src="../../../assets/js/slick.js"></script>
<script src="../../../assets/js/bootstrap.min.js"></script>
<script src="../../../assets/js/bootstrap.bundle.min.js"></script>
<script src="../../../assets/js/index.global.min.js"></script>
<script src="../../../assets/js/common.js"></script>
<script src="../../../assets/js/sub.js"></script>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
   <!--content-->
    <div class="container_wrap" style="padding-top:0px;">
        <div class="sub_wrap">
            <div class="sub_background profess_bg">
                <section class="inner">
                    <h3 class="title fw-bolder text-center text-white">교수</h3>
                </section>
            </div>

            <!--본문-->
            <section class="inner mt-5">
                <!--제목-->
                <section class="detail_title_wrap">
                    <section class="d-flex flex-row justify-content-between align-items-center title_box px-2 py-3 mb-5"> 
					<a href="#" title="이전페이지"
						class="d-flex flex-row align-items-center gap-2"><img
						src="../images/arr_blue.png" /><em class="fst-normal">이전페이지</em></a>                                          
                        <h5 class="content_title text-center fw-bolder d-flex align-items-center justify-content-center gap-1 flex-wrap">
                            [교수]
                            <span class="col-12 col-md-auto text-truncate"><c:out value="${dt.KOR_NM }"/></span>
                        </h5>
						<div class="like_container">
							<div class="link_cnt text-end">
								<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18">
									<path
										d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z"
										style="fill:#fff;stroke-width:0" />
									<path
										d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z"
										style="stroke-width:0;fill:#ff0202" /></svg>
							</div>
						</div>                        
                    </section>
                </section>

                <!--교수상세 기본정보-->
                <main class="profss_dtl_wrap">
                    <h5 class="fw-semibold mb-3">기본정보</h5>
                    <div class="profss_info d-flex flex-column flex-sm-row gap-4 align-items-stretch">
                      <div class="photo_box">
							<c:if test="${!empty photo}">
								<img src="data:image/png;base64,${photo}" onError="${contextPath}/${crtSiteId}/assets/images/contents/human.png" alt="교수 사진" style="width:150px;height:auto;">
							</c:if>
									
				
							<c:if test="${empty photo}">
								<img src="/web/assets/images/contents/human.png" onError="/web/assets/images/contents/human.png" alt="교수 사진">
							</c:if>
                        </div>
                        <table class="table mb-0">
                            <caption class="blind">기본정보</caption>
                            <colgroup>
                                <col width="10%"/>
                                <col width="40%"/>
                                <col width="10%"/>
                                <col width="40%"/>
                            </colgroup>
                            <tbody>
                                <tr>
                                    <th scope="col" class="align-middle ps-3">교수명</th>
                                    <td class="align-middle"><c:out value="${dt.KOR_NM }"/></td>
                                    <th scope="col" class="align-middle ps-3">소속</th>
                                    <td class="align-middle"><c:out value="${dt.DEPT_KOR_NM }"/></td>
                                </tr>
                                <tr>
                                    <th scope="col" class="align-middle ps-3">전화번호</th>
                                    <td class="align-middle text-break"><c:out value="${dt.TEL_NO }"/></td>
                                    <th scope="col" class="align-middle ps-3">이메일</th>
                                    <td class="align-middle text-break"><c:out value="${dt.EMAIL }"/></td>
                                </tr>
                                <tr>
                                    <th scope="col" class="align-middle ps-3">연구실</th>
                                    <td class="align-middle text-break"><c:out value="${dt.LAB }"/></td>
                                    <th scope="col" class="align-middle ps-3">홈페이지</th>
                                    <td class="align-middle text-break">
                                    <c:if test="${dt.HOME_PAGE_PUBC_YN eq 'Y' }">
                                    	<c:out value="${dt.HOME_PAGE }"/>
                                    </c:if>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </main>

                <!--강의과목-->
                <section class="prof_section prof_lectu">
                    <h5 class="fw-semibold mb-3">강의과목</h5>
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
                                    <th scope="row" class="text-center align-middle">년도</th>
                                    <th scope="row" class="text-center align-middle">학기</th>
                                    <th scope="row" class="text-center align-middle">주야</th>
                                    <th scope="row" class="text-center align-middle">수강대상대학(원)</th>
                                    <th scope="row" class="text-center align-middle">수강대상학과</th>
                                    <th scope="row" class="text-center align-middle">과정구분</th>
                                    <th scope="row" class="text-center align-middle">학수번호</th>
                                </tr>
                            </thead>
                            <tbody>
                            	<c:forEach var="listDt" items="${lectureList}">
                                <tr>
                                    <td class="name text-start text-lg-center align-middle">${listDt.SBJT_KOR_NM}</td>
                                    <td class="year text-start text-lg-center align-middle">${listDt.YY}</td>
                                    <td class="semi text-start text-lg-center align-middle">${listDt.SHTM_NM}</td>
                                    <td class="week text-start text-lg-center align-middle">${listDt.DAN_FG_NM}</td>
                                    <td class="coll text-start text-lg-center align-middle">${listDt.OPEN_COLG_NM}</td>
                                    <td class="undg text-start text-lg-center align-middle">${listDt.OPEN_SUST_MJ_NM}</td>
                                    <td class="acad text-start text-lg-center align-middle">${listDt.EDU_CORS_NM}</td>
                                    <td class="numb text-start text-lg-center align-middle">${listDt.COURSE_NO}</td>
                                </tr>
                            	</c:forEach>                                                           
                            </tbody>
                        </table>
                        
					<ul class="pagination gap-2 justify-content-center mt-4">
                       <pgui:paginationCustom listUrl="${URL_PREVIEW}&staffNo=${dt.STAFF_NO}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'searchPage')}"/>
                    </ul>        

                    </div>
                </section>
                <!--학력-->
                <c:if test="${dt.SHCR_PUBC_YN eq 'Y'}">
	                <section class="prof_section prof_level">
	                    <h5 class="fw-semibold mb-3">학력</h5>
	                    <div class="box" style="white-space: pre-wrap;">${dt.SHCR }</div>
	                </section>
                </c:if>

                <!--경력-->
                <c:if test="${dt.CARR_PUBC_YN eq 'Y'}">
	                <section class="prof_section prof_career">
	                    <h5 class="fw-semibold mb-3">경력</h5>
	                    <div class="box" style="white-space: pre-wrap;">${dt.CARR }</div>
	                </section>
                </c:if>
                
                <!--주연구분야-->
                <c:if test="${dt.RECH_FLD_PUBC_YN eq 'Y'}">
	                <section class="prof_section prof_career">
	                    <h5 class="fw-semibold mb-3">주연구분야</h5>
	                    <div class="box" style="white-space: pre-wrap;">${dt.RECH_FLD }</div>
	                </section>
                </c:if>
                
                <!--논문연구실적-->
                <c:if test="${dt.THSS_RECH_PUBC_YN eq 'Y'}">
	                <section class="prof_section prof_career">
	                    <h5 class="fw-semibold mb-3">논문연구실적</h5>
	                    <div class="box" style="white-space: pre-wrap;"><c:out value="${dt.THSS_RECH }"/></div>
	                </section>
                

                <!--저역서 및 논문연구-->
	                <section class="prof_section prof_career">
	                    <h5 class="fw-semibold mb-3">저역서</h5>
	                    <div class="box" style="white-space: pre-wrap;"><c:out value="${dt.BOOK_THSS }"/></div>
	                </section>
                </c:if>
                

                <!--닫기-->
                <a href="javascript:self.close();" title="닫기" class="back_to_list">
                    닫기
                </a>
            </section>

        </div>


        

    </div>