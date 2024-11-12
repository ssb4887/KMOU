<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/style.css"/>">
<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/main.css"/>">
<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/sub.css"/>">
<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/tabulator_bootstrap5.css"/>">

<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/jquery-1.9.1.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/jquery-ui.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/slick.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/index.global.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/common.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/sub.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/checkForm.js?cp=${pageContext.request.contextPath}&lg=${siteInfo.locale_lang}&st=${crtSiteId}"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${jsPath}/bootstrap.bundle.min.js"/>"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.5.3/jspdf.min.js"></script>
<script type="text/javascript" src="https://html2canvas.hertzen.com/dist/html2canvas.min.js"></script>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
<button type="button" id="pdf">pdf 다운</button>
    <div id="report">
    
      <div class="modal-content">
        <div class="modal-body">
            <section class="mt-4">
                <h2 class="d-flex justify-content-between align-items-center mb-3">수업계획서<br></h2>
                <table class="table">
                    <caption class="blind">수업계획서</caption>
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
                            <td colspan="3">${dt.SUBJECT_NM}</td>
                            <th scope="row">교과목명(영문)</th>
                            <td colspan="3">${dt.SUBJECT_ENM}</td>
                        </tr>
                        <tr>
                            <th scope="row">학년</th>
                            <td>${dt.GRADE}</td>
                            <th scope="row">학기</th>
                            <td>${dt.SMT_NAME}</td>
                            <th scope="row">담당교수</th>
                            <td>${dt.EMP_NM}</td>
                            <th scope="row">과목구분</th>	
                            <td>${dt.COMDIV_CODE_NAME}</td>
                        </tr>
                        <tr>
                            <th scope="row">수강대상대학(원)</th>
                            <td>${dt.DEPT_NM}</td>
                            <th scope="row">수강대상학과</th>
                            <td>${dt.COLG_NM}</td>
                            <th scope="row">교과목번호 - 분반</th>
                            <td>${dt.SUBJECT_CD} - ${dt.DIVCLS}</td>
                        	<th scope="row">학점</th>
                            <td>${dt.CDT_NUM}</td>
                        </tr>
                        <tr>
                            <th scope="row">시수(이론/실습·실기)</th>
                            <td>${dt.WTIME_NUM}/${dt.PTIME_NUM}</td>
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
            <!-- 교과목정보  -->
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
                            <td colspan="8">${elfn:replaceHtmlY(dt.SUBJ_DESC) }</td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2">세부정보</th>
                            <td colspan="8">${elfn:replaceHtmlY(dt.METHOD) }</td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2">수강대상</th>
                            <td colspan="8">${dt.SUGANG_TRG}</td>
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
                                        <tr>
                                        	<c:forEach var="coreDt" items="${coreList }" varStatus="i">
	                                        	<c:if test="${coreList[i.index].ABI_GB_NM != coreList[i.index-1].ABI_GB_NM}">
	                                        		<th scope="col" colspan="2">${coreDt.ABI_GB_NM }</th>
	                                        	</c:if>
                                       		</c:forEach>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                        	<c:forEach var="coreDt" items="${coreList }" varStatus="i">
	                                        	<td>${coreDt.ABI_NM }</td>
	                                        </c:forEach>
                                        </tr>
                                        <tr>
                                        	<c:forEach var="coreDt" items="${coreList }" varStatus="i">
	                                        	<td>${coreDt.MAIN_GB_NM }</td>
	                                        </c:forEach>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <c:if test="${dt.DEPT_CD != '446000'}">
                        	<tr>
	                            <th scope="row" colspan="2">전공능력</th>
	                            <td colspan="8">
	                            	<table class="table in_table">
	                                    <caption class="blind">전공능력</caption>
	                                    <colgroup>
	                                        <col width="auto">
	                                        <col width="auto">
	                                        <col width="auto">
	                                        <col width="auto">
	                                        <col width="auto">
	                                    </colgroup>
	                                    <thead>
	                                        <tr>
	                                        	<c:forEach var="abiDt" items="${abiList }" varStatus="i">
		                                        	<td>${abiDt.ABI_NM }</td>
		                                        </c:forEach>
	                                        </tr>
	                                    </thead>
	                                    <tbody>
	                                        <tr>
	                                        	<c:forEach var="abiDt" items="${abiList }" varStatus="i">
		                                        	<td>${abiDt.MAIN_GB_NM }</td>
		                                        </c:forEach>
	                                        </tr>
	                                    </tbody>
	                                </table>
								</td>
	                        </tr>
                        </c:if>
                        <!-- 교과목정보  - 수업목표 -->
                        <tr>
                            <th scope="row" colspan="2" rowspan="5">수업목표</th>
                            <td class="text-center">[핵심역량 연계 목표]</td>
                            <td colspan="7">${dt.CORE_GOAL }</td>
                        </tr>
                        <tr>
                        	 <td class="text-center">[전공능력 연계 목표]</td>
                            <td colspan="7">${dt.MAJOR_GOAL }</td>
                        </tr>
                        <tr>
                        	<td class="text-center">수업목표 1</td>
                            <td colspan="7">${dt.CLS_GOAL1 }</td>
                        </tr>
                        <tr>
                        	<td class="text-center">수업목표 2</td>
                            <td colspan="7">${dt.CLS_GOAL2 }</td>
                        </tr>
                        <tr>
                        	<td class="text-center">수업목표 3</td>
                            <td colspan="7">${dt.CLS_GOAL3 }</td>
                        </tr>
                        
                        <c:set var="linkCount" value="2"/>
                        <c:set var="classTypeCount" value="${linkTotalCount + 10}"/>
                        
                        <c:if test="${linkTotalCount != '0' }">
                        	<c:set var="linkCount" value="${linkTotalCount + 1}"/>
                        	<c:set var="classTypeCount" value="${linkTotalCount + 9}"/>
                        </c:if>
                        <!-- 교과목정보  - 수업유형 -->
                        <tr>
                            <th scope="row" colspan="2" rowspan="${classTypeCount }">수업유형</th>
                            <td class="text-center">구분</td>
                            <td class="text-center" colspan="7">내용</td>
                        </tr>
                        <tr>
                            <td class="text-center" rowspan="2">Anchor 학사제도</td>
                            <td colspan="7">
                            	<c:choose>
                            		<c:when test="${dt.LECPLN_FOCUS_FL == '1'}">■ 집중이수</c:when>
                            		<c:otherwise>□ 집중이수</c:otherwise>
                            	</c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="7">▣ 운영계획 :
                            	<span>${dt.LECPLN_FOCUS_METHOD }</span>
                            </td>
                        </tr>
                       
                        <tr>
                            <td class="text-center" rowspan="${linkCount}">산학연연계</td>
                            <td colspan="3" class="text-center">연계 기업(기관)</td>
                            <td colspan="4" class="text-center">주요내용</td>
                        </tr>
                        <c:if test="${empty linkList }">
                        	<tr>
                        		<td colspan="3">-</td>
                            	<td colspan="4">-</td>
                        	</tr>
                        </c:if>
                        <c:forEach var="linkDt" items="${linkList }" varStatus="i">
                        	<tr>
		                    	<td colspan="3">${linkDt.LINK_COMP_NM }</td>
                            	<td colspan="4">${linkDt.LINK_SUMMARY }</td>
                        	</tr>
                        </c:forEach>
                        <tr>
                            <td class="text-center" rowspan="2">비교과연계</td>
                            <td colspan="7">▣ 연계부서 :
                            	<span>${dt.NONSUBJECT_DEPT_NM }</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="7">▣ 운영계획 :
                            	<span>${dt.NONSUBJECT_METHOD }</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-center" rowspan="2">해양DNA교과</td>
                            <td colspan="7">
                            	<span>
                            		<c:choose>
	                            		<c:when test="${dt.LECPLN_FUSE_FL == '1'}">■ 융합전공</c:when>
	                            		<c:otherwise>□ 융합전공</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${dt.LECPLN_LINK_FL == '1'}">■ 연계전공</c:when>
	                            		<c:otherwise>□ 연계전공</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${dt.LECPLN_MICRO_FL == '1'}">■ 마이크로디그리</c:when>
	                            		<c:otherwise>□ 마이크로디그리</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${dt.LECPLN_NANO_FL == '1'}">■ 나노디그리</c:when>
	                            		<c:otherwise>□ 나노디그리</c:otherwise>
	                            	</c:choose>
                            	</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="7">▣ 운영계획 :
                            	<span>${dt.DNA_METHOD }</span>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-center">기타</td>
                            <td colspan="6">${dt.CLS_TP_ETC }</td>
                        </tr>
                        <!-- 교과목정보  - 교수학습 -->
                        <tr>
                            <th scope="row" colspan="2" rowspan="6">교수학습</th>
                            <td id="" colspan="8">▣ 교수학습환경 : 
                            	<span>
                            		<c:choose>
	                            		<c:when test="${dt.ENVIR_OFFLINE_FL == '1'}">■ 오프라인</c:when>
	                            		<c:otherwise>□ 오프라인</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${dt.ENVIR_ONLINE_FL == '1'}">■ 온라인(원격)</c:when>
	                            		<c:otherwise>□ 온라인(원격)</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${dt.ENVIR_BLEND_FL == '1'}">■ 블렌디드(혼합)</c:when>
	                            		<c:otherwise>□ 블렌디드(혼합)</c:otherwise>
	                            	</c:choose>
                            	</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="8">▣ 교수학습방법</td>
                        </tr>
                        <tr>
                            <td colspan="8">
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE1}">■ 강의</c:when>
	                            		<c:otherwise>□ 강의</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE2}">■ 실험/실습/실기</c:when>
	                            		<c:otherwise>□ 실험/실습/실기</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE3}">■ 토의/토론</c:when>
	                            		<c:otherwise>□ 토의/토론</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE4}">■ 발표</c:when>
	                            		<c:otherwise>□ 발표</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE5}">■ 질의응답</c:when>
	                            		<c:otherwise>□ 질의응답</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE6}">■ 문제풀이학습</c:when>
	                            		<c:otherwise>□ 문제풀이학습</c:otherwise>
	                            	</c:choose>
                            	</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="8">
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE7}">■ 협력학습(팀기반학습)</c:when>
	                            		<c:otherwise>□ 협력학습(팀기반학습)</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE8}">■ 문제기반학습(PBL)</c:when>
	                            		<c:otherwise>□ 문제기반학습(PBL)</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE9}">■ 프로젝트기반학습(PJBL)</c:when>
	                            		<c:otherwise>□ 프로젝트기반학습(PJBL)</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE10}">■ 사례기반학습</c:when>
	                            		<c:otherwise>□ 사례기반학습</c:otherwise>
	                            	</c:choose>
                            	</span>
							</td>
                        </tr>
                        <tr>
                            <td colspan="8">
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE11}">■ 플립러닝</c:when>
	                            		<c:otherwise>□ 플립러닝</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE12}">■ 전문가초청</c:when>
	                            		<c:otherwise>□ 전문가초청</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE13}">■ 세미나</c:when>
	                            		<c:otherwise>□ 세미나</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE14}">■ 현장견학(실습)</c:when>
	                            		<c:otherwise>□ 현장견학(실습)</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.LEC_TYPE_CODE15}">■ 논문지도</c:when>
	                            		<c:otherwise>□ 논문지도</c:otherwise>
	                            	</c:choose>
                            	</span>
							</td>
                        </tr>
                        <tr>
                            <td colspan="8">
                            	<span>
                            		<c:choose>
	                            		<c:when test="${dt.LEC_TYPE_DESC == '-' || dt.LEC_TYPE_DESC == ''}">■ </c:when>
	                            		<c:otherwise>□ </c:otherwise>
	                            	</c:choose>
                            	</span>
                            	기타 : 
                            	<span>${dt.LEC_TYPE_DESC }</span>
                            </td>
                        </tr>
                        <!-- 교과목정보 - 평가방법내용 -->
                        <tr>
                            <th scope="row" colspan="2" rowspan="9">평가방법·내용</th>
                            <td colspan="2" class="text-center">평가방법</td>
                            <td colspan="5" class="text-center">내용</td>
                            <td class="text-center">비율(%)</td>
                        </tr>
                        <tr>
                            <td class="text-center">중간평가</td>
                            <td class="text-center">${dt.MID_CODE }</td>
                            <td colspan="5">${dt.MID_DESC }</td>
                            <td class="text-center">${dt.MID_RT }</td>
                        </tr>
                        <tr>
                            <td class="text-center">기말평가</td>
                            <td class="text-center">${dt.END_CODE }</td>
                            <td colspan="5">${dt.END_DESC }</td>
                            <td class="text-center">${dt.END_RT }</td>
                        </tr>
                        <tr>
                            <td class="text-center">출석</td>
                            <td class="text-center">${dt.ATT_CODE }</td>
                            <td colspan="5">${dt.ATT_DESC }</td>
                            <td class="text-center">${dt.ATT_RT }</td>
                        </tr>
                        <tr>
                            <td class="text-center">
                            	<c:choose>
                            		<c:when test="${!empty dt.ETC_TYPE1 }">${dt.ETC_TYPE1 }</c:when>
                            		<c:otherwise>기타1</c:otherwise>
	                            </c:choose>
                            </td>
                            <td class="text-center">${dt.ETC_CODE1 }</td>
                            <td colspan="5">${dt.ETC_DESC1 }</td>
                            <td class="text-center">${dt.ETC_RT1 }</td>
                        </tr>
                        <tr>
                            <td class="text-center">
                            	<c:choose>
                            		<c:when test="${!empty dt.ETC_TYPE2 }">${dt.ETC_TYPE2 }</c:when>
                            		<c:otherwise>기타2</c:otherwise>
	                            </c:choose>
							</td>
                            <td class="text-center">${dt.ETC_CODE2 }</td>
                            <td colspan="5">${dt.ETC_DESC2 }</td>
                            <td class="text-center">${dt.ETC_RT2 }</td>
                        </tr>
                        <tr>
                            <td class="text-center">
                            	<c:choose>
                            		<c:when test="${!empty dt.ETC_TYPE3 }">${dt.ETC_TYPE3 }</c:when>
                            		<c:otherwise>기타3</c:otherwise>
	                            </c:choose>
                            </td>
                            <td class="text-center">${dt.ETC_CODE3 }</td>
                            <td colspan="5">${dt.ETC_DESC3 }</td>
                            <td class="text-center">${dt.ETC_RT3 }</td>
                        </tr>
                        <tr>
                            <td class="text-center">
                            	<c:choose>
                            		<c:when test="${!empty dt.ETC_TYPE4 }">${dt.ETC_TYPE4 }</c:when>
                            		<c:otherwise>기타4</c:otherwise>
	                            </c:choose>
                            </td>
                            <td class="text-center">${dt.ETC_CODE4 }</td>
                            <td colspan="5">${dt.ETC_DESC4 }</td>
                            <td class="text-center">${dt.ETC_RT4 }</td>
                        </tr>
                        <c:set var="sum" value="${dt.MID_RT + dt.END_RT + dt.ATT_RT + dt.ETC_RT1 + dt.ETC_RT2 + dt.ETC_RT3 + dt.ETC_RT4}"/>
                        <tr>
                            <td colspan="7" class="text-center">계</td>
                            <td class="text-center">${sum}</td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2">CQI반영 개선 사항</th>
                            <td colspan="8">${dt.CQI_IMP}</td>
                        </tr>
                        <tr>
                            <th scope="row" colspan="2" rowspan="3">장애학생을 위한 수업지원</th>
                            <td colspan="8">▣ 학습지원 :
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.STUDY_SUPP_CODE1}">■ 교육기자재</c:when>
	                            		<c:otherwise>□ 교육기자재</c:otherwise>
	                            	</c:choose>
	                            </span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.STUDY_SUPP_CODE2}">■ 대체·보조자료</c:when>
	                            		<c:otherwise>□ 대체·보조자료</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.STUDY_SUPP_CODE3}">■ 지정좌석</c:when>
	                            		<c:otherwise>□ 지정좌석</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.STUDY_SUPP_CODE4}">■ 수업녹음녹화</c:when>
	                            		<c:otherwise>□ 수업녹음녹화</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.STUDY_SUPP_CODE5}">■ 도우미(튜터링, 대필, 통역 등)</c:when>
	                            		<c:otherwise>□ 도우미(튜터링, 대필, 통역 등)</c:otherwise>
	                            	</c:choose>
                            	</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="8">▣ 평가지원 :
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.STUDY_SUPP_CODE4}">■ 평가장소</c:when>
	                            		<c:otherwise>□ 평가장소</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.STUDY_SUPP_CODE4}">■ 평가시간</c:when>
	                            		<c:otherwise>□ 평가시간</c:otherwise>
	                            	</c:choose>
                            	</span>
                            	<span>
                            		<c:choose>
	                            		<c:when test="${!empty dt.STUDY_SUPP_CODE4}">■ 평가시간</c:when>
	                            		<c:otherwise>□ 평가시간</c:otherwise>
	                            	</c:choose>
                            	</span>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="8">▣ 기타 : 
                            	<span>${dt.ETC_SUPP}</span>
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
                    	
                        
                        <c:if test="${linkCount != '0' }">
                        	<c:set var="linkCount" value="${linkCount + 1}"/>
                        	<c:set var="classTypeCount" value="${linkTotalCount + 9}"/>
                        </c:if>
                    	
                    	<c:set var="bookCount" value="2"/>
                    	<c:if test="${bookTotalCount != '0' }"> 
                    		<c:set var="bookCount" value="${bookTotalCount + 1}"/>
                    	</c:if>
                        <tr>
                        	<th scope="row" colspan="2" rowspan="${bookCount }">교재</th>
                            <td>구분</td>
                            <td colspan="3">교재명</td>
                            <td>저자</td>
                            <td colspan="2">출판사</td>
                            <td>출판년도</td>
                        </tr>
                        <c:choose>
                        	<c:when test="${!empty bookList }">
                        		<c:forEach var="bookDt" items="${bookList }" varStatus="i">
		                        	<tr>
		                        		<td>${bookDt.BOOK_CODE_NM }</td>
		                            	<td colspan="3">${bookDt.BOOK }</td>
		                            	<td>${bookDt.AUTHOR }</td>
		                            	<td colspan="2">${bookDt.PUBLI_COMP }</td>
		                            	<td>${bookDt.PUBLI_YEAR }</td>
		                        	</tr>
		                        </c:forEach>
                        	</c:when>
                        	<c:otherwise>
                        		<tr>
	                        		<td colspan="8">등록된 교재가 없습니다.</td>
		                        </tr>
                        	</c:otherwise>
                        </c:choose>
                        
                        <tr>
                        	<th scope="row" colspan="2" rowspan="3">교구</th>
                            <td colspan="8">▣ 장비/기구 : ${dt.TOOL_EQU }</td>
                        </tr>
                        <tr>
                            <td colspan="8">▣ 소프트웨어 : ${dt.TOOL_SW }</td>
                        </tr>
                        <tr>
                            <td colspan="8">▣ 기타 : ${dt.TOOL_ETC }</td>
                        </tr>
                    </tbody>
                </table>
            </section>
            
            <section class="mt-4">
                <h2 class="d-flex justify-content-between align-items-center mb-3">주차별 수업계획</h2>
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
							<th>주차</th>
							<th colspan="3">내용</th>
							<th>수업환경</th>
							<th colspan="2">교수학습방법</th>
							<th colspan="2">교수학습자료</th>
							<th>비고</th>
						</tr>
					</thead>
                    <tbody>
                    	<c:if test="${empty weekList }"><tr><td class="text-center" colspan="10">주차별 수업계획서가 없습니다.</td></tr></c:if>
                    	 <c:forEach var="weekDt" items="${weekList }" varStatus="i">
                        	<tr>
                        		<c:choose>
                            		<c:when test="${weekDt.WEEK_TP == '16' || weekDt.WEEK_TP == '17'}"><td>기타</td></c:when>
                            		<c:otherwise><td>${weekDt.WEEK_TP }</td></c:otherwise>
                            	</c:choose>
                            	<td colspan="3">${weekDt.CURI_CONTENT }</td>
                            	<td>
                            		<c:if test="${weekDt.ENVIR_ONLINE_FL == '1'}">온라인<br/></c:if>
                            		<c:if test="${weekDt.ENVIR_OFFLINE_FL == '1' }">오프라인<br/></c:if>
                            		<c:if test="${weekDt.ENVIR_BLEND_FL == '1' }">온라인/오프라인</c:if>
                            	</td>
                            	<td colspan="2">${weekDt.LEC_TYPE_DESC }</td>
                            	<td colspan="2">${weekDt.STUDY_DATA }</td>
                            	<td>${weekDt.ETC }</td>
                        	</tr>
                        </c:forEach>
                    </tbody>
                </table>
            </section>
        </div>
      </div>
    </div>
<script type="text/javascript">
$(function(){
	
	let all_area_array = ['#report']; //전체영역 area
	let area_array = ['#report']; //pdf 다운 영역

	$('#pdf').on("click", function () {
	  let difference = all_area_array.filter(x => !area_array.includes(x));

	  $.each(difference,function(index, item){
	    $(item).attr('data-html2canvas-ignore', true);
	  });
	  setTimeout(pdfMake(),500);
	});
	
	
});



const pdfMake = () => {
  html2canvas($('#report')[0]).then(function(canvas) {
    let imgData = canvas.toDataURL('image/png');

    let imgWidth = 210; // 이미지 가로 길이(mm) A4 기준
    let pageHeight = imgWidth * 1.414;  // 출력 페이지 세로 길이 계산 A4 기준
    let imgHeight = canvas.height * imgWidth / canvas.width;
    let heightLeft = imgHeight;

    let doc = new jsPDF('p', 'mm');
    let position = 0;

    // 첫 페이지 출력
    doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
    heightLeft -= pageHeight;

    // 한 페이지 이상일 경우 루프 돌면서 출력
    while (heightLeft >= 20) {
        position = heightLeft - imgHeight;
        doc.addPage();
        doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);
        heightLeft -= pageHeight;
    }

    let today = new Date();
    let year = today.getFullYear();
    let month = ('0' + (today.getMonth() + 1)).slice(-2);
    let day = ('0' + today.getDate()).slice(-2);
    let hours = ('0' + today.getHours()).slice(-2);
    let minutes = ('0' + today.getMinutes()).slice(-2);

    let dateString = year + month + day + hours + minutes;

    // 파일 저장
    doc.save("Report_"+dateString+'.pdf');
  });
}
</script>