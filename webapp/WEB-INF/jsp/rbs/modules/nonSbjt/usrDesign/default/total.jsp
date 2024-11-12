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
                <h4 class="resu_word text-center fw-semibold pb-3"><mark>부경대학교</mark>에 대해 총 <span>157건</span> 검색이 되었습니다.</h4>

                <!--전공교양-->
                <section class="resu_box">
                    <h4 class="d-flex flex-row align-items-center gap-3 mb-3">전공&middot;교양
                        <p>검색결과 <span>12건</span></p>
                    </h4>
                    <div class="majref_wrap d-flex flex-wrap">
                        <div class="item border">
                            <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-3">
                                <li class="maj_choice">전공선택</li>
                                <!-- <li class="maj_esse">전공필수</li>
                                <li class="refin">교양</li> -->
                                <li class="name_of_class"><span>정보융합대학</span><span>디지털금융학과</span></li>
                            </ul>
                            <h5 class="d-flex flex-wrap align-itmes-end mb-2"><a href="#" title="제목" class="fw-semibold ellip_2">부산권 파워반도체 인재양성 공유대학 「System IC Testing Solution 트랙 」</a><span>Department of Software Engineering</span></h5>
                            <p class="desc_txt mb-3 text-truncate">미래 금융산업을 선도할 퀀트(QUANT, Quantitative Analyst)형 핀테크 전문인 미래 금융산업을 선도할 퀀트(QUANT, Quantitative Analyst)형 핀테크 전문인 미래 금융산업을 선도할 퀀트(QUANT, Quantitative Analyst)형 핀테크 전문인 미래 금융산업을 선도할 퀀트(QUANT, Quantitative Analyst)형 핀테크 전문인</p>
                            <ul class="info d-flex flex-wrap align-items-center">
                                <li class="d-flex flex-row align-items-center gap-2 order-1"><strong>편성</strong><span class="rounded-pill">2024년 2학년 2학기</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-2"><strong>과정구분</strong><span>학사</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-3"><strong>학점</strong><span class="rounded-pill">3.0</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-4"><strong>역량구분</strong><span>사회적실천(주), 통섭적 사고, 주도적 학습</span></li>
                            </ul>
                        </div>
                        <div class="item border">
                            <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-3">
                                <!--<li class="maj_choice">전공선택</li>-->
                                 <li class="maj_esse">전공필수</li>
                                <!--<li class="refin">교양</li> -->
                                <li class="name_of_class"><span>정보융합대학</span><span>디지털금융학과</span></li>
                            </ul>
                            <h5 class="d-flex flex-wrap align-itmes-end mb-2"><a href="#" title="제목" class="fw-semibold ellip_2">부산권 파워반도체 인재양성 공유대학 「System IC Testing Solution 트랙 」</a><span>Department of Software Engineering</span></h5>
                            <p class="desc_txt mb-3 text-truncate">생태계 이용기술, 인공 생태계 조성기술, 생태계 복원기술, 생태적으로 건전한 생물 수확설계, 청정생산 기술 등 인간과 자연의 공존을 목적으로 자연환경을 이용하여 지속가능한 생태계를 설계</p>
                            <ul class="info d-flex flex-wrap align-items-center">
                                <li class="d-flex flex-row align-items-center gap-2 order-1"><strong>편성</strong><span class="rounded-pill">2024년 2학년 2학기</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-2"><strong>과정구분</strong><span>학사</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-3"><strong>학점</strong><span class="rounded-pill">3.0</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-4"><strong>역량구분</strong><span>사회적실천(주), 통섭적 사고, 주도적 학습</span></li>
                            </ul>
                        </div>
                        <div class="item border">
                            <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-3">
                                <!--<li class="maj_choice">전공선택</li>
                                 <li class="maj_esse">전공필수</li>-->
                                <li class="refin">교양</li> 
                                <li class="name_of_class"><span>정보융합대학</span><span>디지털금융학과</span></li>
                            </ul>
                            <h5 class="d-flex flex-wrap align-itmes-end mb-2"><a href="#" title="제목" class="fw-semibold ellip_2">객체지향프로그래밍</a><span>Department of Software Engineering</span></h5>
                            <p class="desc_txt mb-3 text-truncate">생태계 이용기술, 인공 생태계 조성기술, 생태계 복원기술, 생태적으로 건전한 생물 수확설계국제적 연구 인력 양성</p>
                            <ul class="info d-flex flex-wrap align-items-center">
                                <li class="d-flex flex-row align-items-center gap-2 order-1"><strong>편성</strong><span class="rounded-pill">2024년 2학년 2학기</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-2"><strong>과정구분</strong><span>학사</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-3"><strong>학점</strong><span class="rounded-pill">3.0</span></li>
                                <li class="d-flex flex-row align-items-center gap-2 order-4"><strong>역량구분</strong><span>사회적실천(주), 통섭적 사고, 주도적 학습</span></li>
                            </ul>
                        </div>
                    </div>
                    <a href="#" title="검색결과더보기" class="resu_more_btn">교과목 검색결과 더보기</a>
                </section>

                <!--전공교양-->
                <section class="resu_box">
                    <h4 class="d-flex flex-row align-items-center gap-3 mb-3">비교과
                        <p>검색결과 <span>12건</span></p>
                    </h4>
                    <div class="non_majref_wrap d-flex flex-wrap">
                        <div class="item border">
                            <h5 class="d-flex gap-2 mb-3">
                                <span class="tag_adv">상담</span>
                                <!-- <span class="tag_ref">교양</span> -->
                                <a href="#" title="제목" class="text-truncate">KMOU 미래설계및 상담</a>
                            </h5>
                            <div class="d-flex flex-column flex-sm-row flex-md-column flex-lg-row">
                                <dl class="d-flex flex-wrap mb-1">
                                    <dt>모집기간</dt>
                                    <dd>2000-00-00 ~ 0000-00-00</dd>
                                </dl>
                                <dl class="d-flex flex-wrap mb-1">
                                    <dt>운영기간</dt>
                                    <dd>2000-00-00 ~ 0000-00-00</dd>
                                </dl>
                            </div>
                            <dl class="d-flex flex-row">
                                <dt>모집인원</dt>
                                <dd><strong>20</strong> / <span>30</span> 명 지원중</dd>
                            </dl>
                            
                        </div>
                        <div class="item border">
                            <h5 class="d-flex gap-2 mb-3">
                                <span class="tag_adv">상담</span>
                                <!-- <span class="tag_ref">교양</span> -->
                                <a href="#" title="제목" class="text-truncate">부산권 파워반도체 인재양성 공유대학 「System IC Testing Solution 트랙 」부산권 파워반도체 인재양성 공유대학 「System IC Testing Solution 트랙 」</a>
                            </h5>
                            <div class="d-flex flex-column flex-sm-row flex-md-column flex-lg-row">
                                <dl class="d-flex flex-wrap mb-1">
                                    <dt>모집기간</dt>
                                    <dd>2000-00-00 ~ 0000-00-00</dd>
                                </dl>
                                <dl class="d-flex flex-wrap mb-1">
                                    <dt>운영기간</dt>
                                    <dd>2000-00-00 ~ 0000-00-00</dd>
                                </dl>
                            </div>
                            <dl class="d-flex flex-row">
                                <dt>모집인원</dt>
                                <dd><strong>20</strong> / <span>30</span> 명 지원중</dd>
                            </dl>
                            
                        </div>
                        <div class="item border">
                            <h5 class="d-flex gap-2 mb-3">
                                <span class="tag_adv">상담</span>
                                <!-- <span class="tag_ref">교양</span> -->
                                <a href="#" title="제목" class="text-truncate">KMOU 미래설계및 상담</a>
                            </h5>
                            <div class="d-flex flex-column flex-sm-row flex-md-column flex-lg-row">
                                <dl class="d-flex flex-wrap mb-1">
                                    <dt>모집기간</dt>
                                    <dd>2000-00-00 ~ 0000-00-00</dd>
                                </dl>
                                <dl class="d-flex flex-wrap mb-1">
                                    <dt>운영기간</dt>
                                    <dd>2000-00-00 ~ 0000-00-00</dd>
                                </dl>
                            </div>
                            <dl class="d-flex flex-row">
                                <dt>모집인원</dt>
                                <dd><strong>20</strong> / <span>30</span> 명 지원중</dd>
                            </dl>
                            
                        </div>
                        <div class="item border">
                            <h5 class="d-flex gap-2 mb-3">
                                <!--<span class="tag_adv">상담</span>-->
                                 <span class="tag_ref">교양</span> 
                                <a href="#" title="제목" class="text-truncate">부산권 파워반도체 인재양성 공유대학 「System IC Testing Solution 트랙 」부산권 파워반도체 인재양성 공유대학 「System IC Testing Solution 트랙 」</a>
                            </h5>
                            <div class="d-flex flex-column flex-lg-row">
                                <dl class="d-flex flex-wrap mb-1">
                                    <dt>모집기간</dt>
                                    <dd>2000-00-00 ~ 0000-00-00</dd>
                                </dl>
                                <dl class="d-flex flex-wrap mb-1">
                                    <dt>운영기간</dt>
                                    <dd>2000-00-00 ~ 0000-00-00</dd>
                                </dl>
                            </div>
                            <dl class="d-flex flex-row">
                                <dt>모집인원</dt>
                                <dd><strong>20</strong> / <span>30</span> 명 지원중</dd>
                            </dl>
                            
                        </div>
                    </div>
                    <a href="https://whalebe.pknu.ac.kr/main" title="검색결과더보기" class="resu_more_btn" target="_blank">비교과 검색결과 더보기</a>
                </section>

                <!--전공-->
                <section class="resu_box">
                    <h4 class="d-flex flex-row align-items-center gap-3 mb-3">전공
                        <p>검색결과 <span>12건</span></p>
                    </h4>
                    <div class="speci_wrap d-flex flex-wrap">
                        <div class="item border">
                            <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-2">
                                <li><span>융합공학부</span><span>기계조선에너지시스템</span></li>
                            </ul>
                            <h5 class="title mb-3">
                                <a href="#" title="제목" class="d-block  fw-semibold">
                                    소프트웨어,<mark>공학</mark>과
                                    <small class="d-inlinle-block fw-normal mt-1">department of Software Engineering</small>
                                </a>
                            </h5>
                            <dl class="d-flex flex-row mb-1">
                                <dt>인재상</dt>
                                <dd>해양생태공학전문인, 유역생태공학전문인, 도시 및 산업생태공학전문인</dd>
                            </dl>
                            <dl class="d-flex flex-row ">
                                <dt>진로</dt>
                                <dd>국제환경기구, 해양수산부, 환경부, 한국환경공단, 농협경제지주, LG이노텍</dd>
                            </dl>
                        </div>
                        <div class="item border">
                            <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-2">
                                <li><span>융합공학부</span><span>기계조선에너지시스템</span></li>
                            </ul>
                            <h5 class="title mb-3">
                                <a href="#" title="제목" class="d-block  fw-semibold">
                                    소프트웨어,<mark>공학</mark>과
                                    <small class="d-inlinle-block fw-normal mt-1">department of Software Engineering</small>
                                </a>
                            </h5>
                            <dl class="d-flex flex-row mb-1">
                                <dt>인재상</dt>
                                <dd>해양생태공학전문인, 유역생태공학전문인, 도시 및 산업생태공학전문인</dd>
                            </dl>
                            <dl class="d-flex flex-row ">
                                <dt>진로</dt>
                                <dd>국제환경기구, 해양수산부, 환경부, 한국환경공단, 농협경제지주, LG이노텍</dd>
                            </dl>
                        </div>
                        <div class="item border">
                            <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-2">
                                <li><span>융합공학부</span><span>기계조선에너지시스템</span></li>
                            </ul>
                            <h5 class="title mb-3">
                                <a href="#" title="제목" class="d-block  fw-semibold">
                                    소프트웨어,<mark>공학</mark>과
                                    <small class="d-inlinle-block fw-normal mt-1">department of Software Engineering</small>
                                </a>
                            </h5>
                            <dl class="d-flex flex-row mb-1">
                                <dt>인재상</dt>
                                <dd>해양생태공학전문인, 유역생태공학전문인, 도시 및 산업생태공학전문인</dd>
                            </dl>
                            <dl class="d-flex flex-row ">
                                <dt>진로</dt>
                                <dd>국제환경기구, 해양수산부, 환경부, 한국환경공단, 농협경제지주, LG이노텍</dd>
                            </dl>
                        </div>
                        <div class="item border">
                            <ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-2">
                                <li><span>융합공학부</span><span>기계조선에너지시스템</span></li>
                            </ul>
                            <h5 class="title mb-3">
                                <a href="#" title="제목" class="d-block  fw-semibold">
                                    소프트웨어,<mark>공학</mark>과
                                    <small class="d-inlinle-block fw-normal mt-1">department of Software Engineering</small>
                                </a>
                            </h5>
                            <dl class="d-flex flex-row mb-1">
                                <dt>인재상</dt>
                                <dd>해양생태공학전문인, 유역생태공학전문인, 도시 및 산업생태공학전문인</dd>
                            </dl>
                            <dl class="d-flex flex-row ">
                                <dt>진로</dt>
                                <dd>국제환경기구, 해양수산부, 환경부, 한국환경공단, 농협경제지주, LG이노텍</dd>
                            </dl>
                        </div>
                    </div>
                    <a href="#" title="검색결과더보기" class="resu_more_btn">전공 검색결과 더보기</a>
                </section>

                <!--교수-->
                <section class="resu_box">
                    <h4 class="d-flex flex-row align-items-center gap-3 mb-3">교수
                        <p>검색결과 <span>12건</span></p>
                    </h4>
                    <div class="profss_wrap d-flex flex-wrap">
                        <div class="item border">
                            <a href="#" title="교수" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3">
                                <span class="photo_box d-inline-block rounded-circle overflow-hidden">
                                    <img src="../images/prof_test1.jpg" alt="교수님사진"/>
                                </span>
                                <span class="txt_box mb-2 mb-sm-0 mb-md-2">
                                    <strong>홍길동<small class="fw-normal ps-2">교수</small></strong>
                                    <span class="d-flex flex-wrap">
                                        <em class="fst-normal d-inline-block">융합공학부</em>
                                        <em class="fst-normal d-inline-block">기계조선에너지시스템 공학전공</em>
                                    </span>
                                </span>
                            </a>
                            <ul class="dtl_info pt-4 d-flex flex-column gap-2">
                                <li class="d-flex flex-row text-break"><strong>이메일</strong>yuoundf02020@incheon.ac.kr</li>
                                <li class="d-flex flex-row text-break"><strong>전화번호</strong>032-222-0999</li>
                                <li class="d-flex flex-row text-break"><strong>홈페이지</strong>-</li>
                                <li class="d-flex flex-row text-break"><strong>과정구분</strong>프랑스시</li>
                                <li class="d-flex flex-row text-break"><strong>학점</strong>프랑스어 듣기와 말하기, 20C 프랑스시</li>
                            </ul>
                        </div>
                    
                    
                        <div class="item border">
                            <a href="#" title="교수" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3">
                                <span class="photo_box d-inline-block rounded-circle overflow-hidden">
                                    <img src="../images/prof_test1.jpg" alt="교수님사진"/>
                                </span>
                                <span class="txt_box mb-2 mb-sm-0 mb-md-2">
                                    <strong>홍길동<small class="fw-normal ps-2">교수</small></strong>
                                    <span class="d-flex flex-wrap">
                                        <em class="fst-normal d-inline-block">융합공학부</em>
                                        <em class="fst-normal d-inline-block">기계조선에너지시스템 공학전공</em>
                                        <em class="fst-normal d-inline-block">기계조선에너지시스템 공학전공</em>
                                    </span>
                                </span>
                            </a>
                            <ul class="dtl_info pt-4 d-flex flex-column gap-2">
                                <li class="d-flex flex-row text-break"><strong>이메일</strong>yuoundf02020@incheon.ac.kr</li>
                                <li class="d-flex flex-row text-break"><strong>전화번호</strong>032-222-0999</li>
                                <li class="d-flex flex-row text-break"><strong>홈페이지</strong>-</li>
                                <li class="d-flex flex-row text-break"><strong>과정구분</strong>프랑스시</li>
                                <li class="d-flex flex-row text-break"><strong>학점</strong>프랑스어 듣기와 말하기, 20C 프랑스시</li>
                            </ul>
                        </div>
                    
                        <div class="item border">
                            <a href="#" title="교수" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3">
                                <span class="photo_box d-inline-block rounded-circle overflow-hidden">
                                    <img src="../images/prof_test1.jpg" alt="교수님사진"/>
                                </span>
                                <span class="txt_box mb-2 mb-sm-0 mb-md-2">
                                    <strong>홍길동<small class="fw-normal ps-2">교수</small></strong>
                                    <span class="d-flex flex-wrap">
                                        <em class="fst-normal d-inline-block">융합공학부</em>
                                        <em class="fst-normal d-inline-block">기계조선에너지시스템 공학전공</em>
                                    </span>
                                </span>
                            </a>
                            <ul class="dtl_info pt-4 d-flex flex-column gap-2">
                                <li class="d-flex flex-row text-break"><strong>이메일</strong>yuoundf02020@incheon.ac.kr</li>
                                <li class="d-flex flex-row text-break"><strong>전화번호</strong>032-222-0999</li>
                                <li class="d-flex flex-row text-break"><strong>홈페이지</strong>-</li>
                                <li class="d-flex flex-row text-break"><strong>과정구분</strong>프랑스시</li>
                                <li class="d-flex flex-row text-break"><strong>학점</strong>프랑스어 듣기와 말하기, 20C 프랑스시</li>
                            </ul>
                        </div>
                    </div>
                    <a href="#" title="검색결과더보기" class="resu_more_btn">교수 검색결과 더보기</a>
                </section>

                <!--학생설계전공-->
                <section class="resu_box">
                    <h4 class="d-flex flex-row align-items-center gap-3 mb-3">학생설계전공
                        <p>검색결과 <span>12건</span></p>
                    </h4>
                    <div class="plan_section d-flex flex-wrap">
                        <div class="item">
                            <ul class="d-flex flex-row gap-2 cate">
                                <li class="year">2023</li>
                                <li class="border">복수전공</li>
                                <li class="border">부전공</li>
                            </ul>
                            <h3 class="title fs-5 fw-bolder text-start mb-3">
                                <a href="#" title="학생설계전공 제목" class="text-truncate">데이터네트워크인공지능 학생설계전공</a>
                            </h3>
                            <div class="detail_maj position-relative">
                                <span class="d-block fs-6 lh-sm mb-1">융합공학부 기계조전에너지시스템 공학전공</span>
                                <span class="d-block fs-6 lh-sm">국어국문학전공, 인문사회과학대학 전공</span>
                            </div>
                            <div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2">
                                <dl class="d-flex flex-wrap">
                                    <dt>수여학위</dt>
                                    <dd>OOOOO학사</dd>
                                </dl>
                                <dl class="d-flex flex-wrap">
                                    <dt>설계학점</dt>
                                    <dd>OO학점</dd>
                                </dl>
                                <dl class="d-flex flex-wrap">
                                    <dt>교과목설계범위</dt>
                                    <dd>교내 교과목 + 온라인 공개강좌</dd>
                                </dl>
                            </div>
                        </div>
                        <div class="item">
                            <ul class="d-flex flex-row gap-2 cate">
                                <li class="year">2023</li>
                                <li class="border">복수전공</li>
                                <li class="border">부전공</li>
                            </ul>
                            <h3 class="title fs-5 fw-bolder text-start mb-3">
                                <a href="#" title="학생설계전공 제목" class="text-truncate">데이터네트워크인공지능 학생설계전공</a>
                            </h3>
                            <div class="detail_maj position-relative">
                                <span class="d-block fs-6 lh-sm mb-1">융합공학부 기계조전에너지시스템 공학전공</span>
                                <span class="d-block fs-6 lh-sm">국어국문학전공, 인문사회과학대학 전공</span>
                            </div>
                            <div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2">
                                <dl class="d-flex flex-wrap">
                                    <dt>수여학위</dt>
                                    <dd>OOOOO학사</dd>
                                </dl>
                                <dl class="d-flex flex-wrap">
                                    <dt>설계학점</dt>
                                    <dd>OO학점</dd>
                                </dl>
                                <dl class="d-flex flex-wrap">
                                    <dt>교과목설계범위</dt>
                                    <dd>교내 교과목 + 온라인 공개강좌</dd>
                                </dl>
                            </div>
                        </div>
                        <div class="item">
                            <ul class="d-flex flex-row gap-2 cate">
                                <li class="year">2023</li>
                                <li class="border">복수전공</li>
                                <li class="border">부전공</li>
                            </ul>
                            <h3 class="title fs-5 fw-bolder text-start mb-3">
                                <a href="#" title="학생설계전공 제목" class="text-truncate">데이터네트워크인공지능 학생설계전공</a>
                            </h3>
                            <div class="detail_maj position-relative">
                                <span class="d-block fs-6 lh-sm mb-1">융합공학부 기계조전에너지시스템 공학전공</span>
                                <span class="d-block fs-6 lh-sm">국어국문학전공, 인문사회과학대학 전공</span>
                            </div>
                            <div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2">
                                <dl class="d-flex flex-wrap">
                                    <dt>수여학위</dt>
                                    <dd>OOOOO학사</dd>
                                </dl>
                                <dl class="d-flex flex-wrap">
                                    <dt>설계학점</dt>
                                    <dd>OO학점</dd>
                                </dl>
                                <dl class="d-flex flex-wrap">
                                    <dt>교과목설계범위</dt>
                                    <dd>교내 교과목 + 온라인 공개강좌</dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                    <a href="#" title="검색결과더보기" class="resu_more_btn">학생설계전공 검색결과 더보기</a>
                </section>
            </section>

        </div>
        
        </div>
    <seciton class="sub_bottom d-none d-md-block ">
        <div class="inner">
            <div class="bottom_text border p-3 position-relative border-bottom-0">
                <p>만족도 평가에 참여해주세요! 더욱더 정확한 추천에 도움이 됩니다. </p>
                <button type="button" class="border-0 d-block position-absolute top-50 translate-middle-y end-0 h-100 px-4"><img src="../images/arr_bottom_gray.png" alt="화살표"/></button>
            </div>
            <div class="botton_box flex-wrap border border-bottom-0 align-items-start align-items-xl-center  ">
                <div class="col-12 col-lg-6 col-xl-5">
                    <h5 class="fw-bold h4 mb-1">교과목 검색결과에 만족하셨습니까?</h5>
                    <p class="d-none d-lg-block">만족도 평가에 참여해주세요! 더욱더 정확한 추천에 도움이 됩니다.</p>
                </div>
                <div class="col-12 col-lg-6 col-xl-7 d-flex flex-column flex-xl-row align-items-center gap-2">
                    <div class="rating_box col-12 col-xl-6">
                        <div class="input-group justify-content-end justify-content-xl-center gap-3">
                            <label class="blind" for="satisfRat">만족도평가</label>
                            <select class="form-select" id="satisfRat">
                                <option selected>검색 정확도</option>
                                <option value="1">검색 정확도</option>
                                <option value="2">검색 정확도</option>
                                <option value="3">검색 정확도</option>
                            </select>
                            <section class="rating d-flex flex-row gap-2 align-items-center">
                                
                                <input class="" type="radio" value="1" id="firStart" name="rating_star">
                                <label class="star" for="firStart"></label>
                                

                                
                                <input class="" type="radio" value="2" id="secStart" name="rating_star">
                                <label class="star" for="secStart"></label>
                                

                                
                                <input class="" type="radio" value="3" id="thrStart" name="rating_star">
                                <label class="star" for="thrStart"></label>
                                

                                
                                <input class="" type="radio" value="4" id="fouStart" name="rating_star">
                                <label class="star" for="fouStart"></label>             
                                

                                
                                <input class="" type="radio" value="5" id="fifStart" name="rating_star">
                                <label class="star" for="fifStart"></label>
                                
                            </section>
                        </div>
                    </div>
                    <div class="submit_box col-12 col-xl-6">
                        <div class="input-group d-flex flex-row flex-nowrap align-items-center gap-2">
                            <label for="onLineRat">한 줄 평가</label>
                            <textarea id="onLineRat" rows="1" placeholder="여러분의 소중한 의견을 보내주세요"></textarea>
                            <button type="submit" class="pk_btn">작성</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </seciton>	
     
     
	<%-- <div id="cms_board_article" class="subConBox">
		<h2 class="title">기술지원신청현황</h2>
		<!-- search -->
		<itui:searchFormItem divClass="tbMSearch fn_techSupportSearch" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		<!-- //search -->
		<div class="bbs_top">
			<p class="listNum">총 <b>${paginationInfo.totalRecordCount}</b>건 (${paginationInfo.currentPageNo}/${paginationInfo.totalPageCount}페이지)</p>
		</div>
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="listTypeA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col style="width:100px">
				<col>
				<col style="width:200px">
				<col style="width:150px">
				<col style="width:150px">
				<col style="width:150px">
			</colgroup>
			<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col"><itui:objectItemName itemId="organName" itemInfo="${itemInfo}"/></th>
				<th scope="col"><itui:objectItemName itemId="organNumb" itemInfo="${itemInfo}"/></th>
				<th scope="col"><itui:objectItemName itemId="organType" itemInfo="${itemInfo}"/></th>
				<th scope="col"><itui:objectItemName itemId="mngerName" itemInfo="${itemInfo}"/></th>
				<th scope="col"><spring:message code="item.regidate.name"/></th>
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="6" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listColumnName" value="${settingInfo.idx_column}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt[listColumnName]}"/>
				<tr>
					<td class="num">${listNo}</td>
					<td class="subject"><a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" class="fn_btn_view"><itui:objectView itemId="organName" itemInfo="${itemInfo}" objDt="${listDt}"/></a></td>
					<td><itui:objectView itemId="organNumb" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<td><itui:objectView itemId="organType" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<td><itui:objectView itemId="mngerName" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</tbody>
		</table>
		</form>
		
		<div class="btnList">
			<c:if test="${wrtAuth}">
			<a href="<c:out value="${URL_INPUT}"/>" class="fn_btn_write">등록</a>
			</c:if>
		</div>
	</div> --%>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>