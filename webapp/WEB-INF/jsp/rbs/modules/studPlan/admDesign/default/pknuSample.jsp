<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/viewAdm.jsp"/>
	</jsp:include>
</c:if>
<%-- <link rel="stylesheet" type="text/css" href="${contextPath}${cssAssetPath}/sub.css"> --%>
<%-- <script src="${contextPath}${jsAssetPath}/sub.js"></script> --%>
<%
	
%>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
<form name="inputForm" action="${URL_JUDGERESULTMODIFYPROC}" method="post">
<div class="container_wrap">
	<div class="sub_wrap">
		<ul class="tabTypeA tab li4" style="margin-bottom:40px;">
			<li class="fn_tab_view on"><a id="tab1" href="#" class="active">기본정보</a></li>
			<li class="fn_tab_view"><a id="tab2" href="#" class="">컨설팅 상담</a></li>
			<li class="fn_tab_view"><a id="tab3" href="#" class="">학생설계전공 심사표</a></li>
		</ul>
		<!--본문-->
		<div id="containInput">
			<c:forEach var="sbjtList" items="${sbjtAllList}">
				<div>
					<input type="hidden" name="ORIG_FG" value="${sbjtList.origFg}"/>
					<input type="hidden" name="OPEN_COLG_CD" value="${sbjtList.openColgCd}"/>
					<input type="hidden" name="OPEN_COLG_KOR_NM" value="${sbjtList.openColgKorNm}"/>
					<input type="hidden" name="OPEN_COLG_ENG_NM" value="${sbjtList.openColgEngNm}"/>
					<input type="hidden" name="OPEN_SUST_MJ_CD" value="${sbjtList.openSustMjCd}"/>
					<input type="hidden" name="OPEN_SUST_MJ_KOR_NM" value="${sbjtList.openSustKorNm}"/>
					<input type="hidden" name="OPEN_SUST_MJ_ENG_NM" value="${sbjtList.openSustEngNm}"/>
					<input type="hidden" name="SBJT_FG" value="${sbjtList.sbjtFg}"/>
					<input type="hidden" name="SBJT_FG_NM" value="${sbjtList.sbjtFgNm}"/>
					<input type="hidden" name="ORG_SBJT_FG" value="${sbjtList.orgSbjtFg}"/>
					<input type="hidden" name="COURSE_NO" value="${sbjtList.courseNo}"/>
					<input type="hidden" name="SBJT_KOR_NM" value="${sbjtList.sbjtKorNm}"/>
					<input type="hidden" name="SBJT_ENG_NM" value="${sbjtList.sbjtEngNm}"/>
					<input type="hidden" name="SHYR_FG" value="${sbjtList.shyrFg}"/>
					<input type="hidden" name="SHYR_FG_NM" value="${sbjtList.shyrFgNm}"/>
					<input type="hidden" name="YY" value="${sbjtList.yy}"/>
					<input type="hidden" name="SHTM_CD" value="${sbjtList.shtmCd}"/>
					<input type="hidden" name="SHTM_NM" value="${sbjtList.shtmNm}"/>
					<input type="hidden" name="PNT" value="${sbjtList.pnt}"/>
					<input type="hidden" name="THEO_TM_CNT" value="${sbjtList.theoTmCnt}"/>
					<input type="hidden" name="PRAC_TM_CNT" value="${sbjtList.pracTmCnt}"/>						
				</div>
			</c:forEach>
		</div>
		<div id="1">
		<section class="inner mt-5">
			<input type="hidden" name="SDM_CD" value="${dt.SDM_CD}">
			<input type="hidden" name="REVSN_NO" value="${dt.REVSN_NO}">
			<input type="hidden" name="APLY_STD_NO" id="stdNo" value="${dt.APLY_STD_NO}">
			<input type="hidden" name="APLY_YY" value="${dt.APLY_YY}"/>
			<input type="hidden" name="USER_ID" value="${dt.APLY_STD_NO}"/>
			<input type="hidden" name="OPEN_MJ_FG" value="${dt.OPEN_MJ_FG}" />
			<input type="hidden" name="APLY_SHTM_CD" id="aplyStdNo" value="${dt.APLY_SHTM_CD}"/>
			<input type="hidden" name="CNSLT_PROF" value="${dt.CNSLT_PROF}"/>
			<input type="hidden" name="GUID_PROF_STAFF_NO" value="${dt.GUID_PROF_STAFF_NO}"/>
			<!-- 참여학생 -->
			<table class="tbListA">
				<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
				<colgroup>
					<col width="100px" />
					<col width="150px" />
					<col width="150px" />
					<col width="100px" />
					<col width="100px" />
					<col width="100px" />
<!-- 					<col width="100px" /> -->
				</colgroup>
				<thead>
				<tr>
					<th scope="col">구분</th>
					<th scope="col">성명</th>
					<th scope="col">학번</th>
					<th scope="col">단과대학</th>
					<th scope="col">학부(과)·전공</th>
					<th scope="col">이수학기</th>
<!-- 					<th scope="col">학년차수</th> -->
				</tr>
				</thead>
				<tbody>
					<c:forEach var="stdDt" items="${stdList}" varStatus="i">
						<input type="hidden" name="STD_NO" value="${stdDt.STD_NO }" />
						<input type="hidden" name="PARTICI_FG" value="${stdDt.PARTICI_FG }" />
						<input type="hidden" name="PARTICI_FG_NM" value="${stdDt.PARTICI_FG_NM }" />
						<input type="hidden" name="ORD" value="${stdDt.ORD }" />
						<input type="hidden" name="KOR_NM" value="${stdDt.KOR_NM }" />								
						<input type="hidden" name="ENG_FNM" value="${stdDt.ENG_FNM }" />	
						<input type="hidden" name="COLG_NM" value="${stdDt.COLG_NM } " />
						<input type="hidden" name="SUST_NM" value="${stdDt.SUST_NM }" />
						<input type="hidden" name="MJ_NM" value="${stdDt.MJ_NM }" />
						<input type="hidden" name="CPTN_SHTM_CNT" value="${stdDt.CPTN_SHTM_CNT }"/>
						<tr>
							<td><c:out value="${stdDt.PARTICI_FG_NM}"/></td>
							<td><c:out value="${stdDt.KOR_NM}"/></td>
							<td><c:out value="${stdDt.STD_NO}"/></td>
							<td><c:out value="${stdDt.COLG_NM}"/></td>
							<td>
								<c:choose>
									<c:when test="${!empty stdDt.MJ_NM}">
										<c:out value="${stdDt.MJ_NM}"/>
									</c:when>
									<c:otherwise>
										<c:out value="${stdDt.SUST_NM}"/>
									</c:otherwise>
								</c:choose>
							</td>
							<td><c:out value="${stdDt.CPTN_SHTM_CNT}"/></td>
<!-- 							<td>확인필요</td> -->
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<td class="plan_tf" colspan="6">참여 학생 수 <span>${stdCount}명</span></td>
					</tr>
				</tfoot>
			</table>
			<!--기본정보-->
			<section class="plan_dtl_section plan_dtl_basic" id="move_acitve1">
				<h4 class="mt20">기본정보</h4>
				<div class="d-flex flex-wrap table_wrap">
					<div>
						<table class="tbListB">
							<colgroup>
								<col width="15%" />
								<col width="35%" />
								<col width="15%" />
								<col width="35%" />
							</colgroup>
							<tbody>
								<tr>
									<th>전공 국문명</th>
									<td><itui:objectText itemId="sdmKorNm" itemInfo="${itemInfo}"/></td>
<%-- 									<td><input type="text" name="sdmKorNm" id="sdmKorNm" class="" value="${dt.SDM_KOR_NM}"></td> --%>
									<th>전공 영문명</th>
									<td><itui:objectText itemId="sdmEngNm" itemInfo="${itemInfo}"/></td>
<%-- 									<td><input type="text" name="sdmEngNm" id="sdmEngNm" class="" value="${dt.SDM_ENG_NM}"></td> --%>
								</tr>
								<tr>
									<th>운영 주관학과</th>
									<td><c:out value="${dt.CHRG_SUST_NM}"/></td>
									<th>운영 참여학과</th>
									<td><c:out value="${dt.PARTICI_SUST_NM}"/></td>
								</tr>
								<tr>
									<th>수여학위 국문명</th>
									<td><itui:objectText itemId="awdDegrKorNm" itemInfo="${itemInfo}"/></td>
									<th>수여학위 영문명</th>
									<td><itui:objectText itemId="awdDegrEngNm" itemInfo="${itemInfo}"/></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="">
					<h4 class="mt20">기본정보2</h4>
					<div>
						<table class="tbListB">
							<colgroup>
								<col width="15%" />
								<col width="85%" />
							</colgroup>
							<tbody>
								<tr>
									<th>관련직업</th>
									<td>
										<c:out value="${dt.CONC_JOB_KOR}"/>
										<input type="hidden" name="CONC_JOB_KOR" value="${dt.CONC_JOB_KOR}"/>
									</td>
								</tr>
								<tr>
									<th>관련 학문 분야</th>
									<td>
										<c:out value="${dt.CONC_STUD_FLD_KOR}"/>
										<input type="hidden" name="CONC_STUD_FLD_KOR" value="${dt.CONC_STUD_FLD_KOR}"/>
									</td>
								</tr>
								<tr>
									<th>요구되는 기초학습능력</th>
									<td>
										<c:out value="${dt.DMND_BASE_LRN_ABTY_KOR}"/>
										<input type="hidden" name="DMND_BASE_LRN_ABTY_KOR" value="${dt.DMND_BASE_LRN_ABTY_KOR}"/>
									</td>
								</tr>
								<tr>
									<th>기대사항</th>
									<td>
										<c:out value="${dt.EXPT_CTNT_KOR}"/>
										<input type="hidden" name="EXPT_CTNT_KOR" value="${dt.EXPT_CTNT_KOR}"/>
									</td>
								</tr>
								<tr>
									<th>기타 궁금한 점</th>
									<td>
										<c:out value="${dt.ETC_CTNT}"/>
										<input type="hidden" name="ETC_CTNT" value="${dt.ETC_CTNT}"/>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</section>
			<!--교과목 설계범위-->
			<section>
				<h4 class="mt20">교과목 설계범위</h4>
				<div class="plan_ipbx">
					<span><input type="checkbox" class="" disabled <c:if test="${dt.SBJT_DGN_RNG_FG eq 'U0177001'}">checked</c:if>>교내 교과목</span>
					<span><input type="checkbox" class="" disabled <c:if test="${dt.SBJT_DGN_RNG_FG eq 'U0177002'}">checked</c:if>>교내 교과목+국내·외 타 대학 교과목</span>
					<span><input type="checkbox" class="" disabled <c:if test="${dt.SBJT_DGN_RNG_FG eq 'U0177003'}">checked</c:if>>교내 교과목+온라인 공개강좌</span>
					<input type="hidden" name="SBJT_DGN_RNG_FG" value="${dt.SBJT_DGN_RNG_FG}"/>
				</div>
			</section>
			<!-- 교육과정표 -->
			<section class="plan_dtl_section plan_range pt-4" id="move_acitve2">
				<h4 class="mt20">교육과정표</h4>
				<button type="button" class="" id="btnModal">
					<i></i>
					과목등록하기
				</button>
				<div class="t_flex">
					<div class="table_box">
						<table class="tbListC">
							<caption class="blind">교과목 설계범위 1학년</caption>
							<colgroup>
								<col width="15%">
								<col width="25%">
								<col width="25%">
								<col width="15%">
								<col width="10%">
								<col width="10%">
							</colgroup>
							<thead>
								<tr>
									<th scope="colgroup" colspan="6">1학년</th>
								</tr>
								<tr>
									<th scope="col" rowspan="2" class="border-end">구분</th>
									<th scope="col" rowspan="2" class="border-end">개설전공</th>
									<th scope="col" rowspan="2" class="border-end">과목명</th>
									<th scope="colgroup" colspan="3">학점</th>
								</tr>
								<tr>
									<th scope="col" class="border-end">전체</th>
									<th scope="col" class="border-end">전</th>
									<th scope="col">후</th>
								</tr>
							</thead>
							<tbody id="grade1">
							<c:if test="${not empty sbjtList1.U0209020 }">
								<c:set var="sbjtList11Tot" value="0" />
								<c:set var="sbjtList11One" value="0" />
								<c:set var="sbjtList11Two" value="0" />
								<c:forEach items="${sbjtList1.U0209020 }" var="sbjtList" varStatus="stat">
									<tr>
										<!--th : rowspan = 과목명 갯수-->
										<c:if test="${stat.index eq 0 }">
											<th scope="rowgroup" rowspan="${fn:length(sbjtList1.U0209020)}" class="border-end">${sbjtList.sbjtFgNm }</th>
										</c:if>
										<td class="border-end sbjtList11" >${sbjtList.openSustMjKorNm }</td>
										<td class="border-end">${sbjtList.sbjtKorNm }</td>
										<td class="border-end">
											<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList11Tot" value="${sbjtList11Tot + sbjtList.pnt}" />
											</c:if>
										</td>
										<td class="border-end">
											<c:if test="${sbjtList.shtmNm eq '전'}">
												${sbjtList.pnt }
												<c:set var="sbjtList11One" value="${sbjtList11One + sbjtList.pnt}" />
											</c:if>
										</td>
										<td>
											<c:if test="${sbjtList.shtmNm eq '후'}">
												${sbjtList.pnt }
												<c:set var="sbjtList11Two" value="${sbjtList11Two + sbjtList.pnt}" />
											</c:if>
										</td>
									</tr>
								</c:forEach>
								<tr class="table_sum">
									<td colspan="3" class="border-end">소계</td>
									<td class="border-end">${sbjtList11Tot }</td>
									<td class="border-end">${sbjtList11One }</td>
									<td>${sbjtList11Two }</td>
								</tr>
							</c:if>
							<c:if test="${not empty sbjtList1.U0209021 }">
								<c:set var="sbjtList12Tot" value="0" />
								<c:set var="sbjtList12One" value="0" />
								<c:set var="sbjtList12Two" value="0" />
								<c:forEach items="${sbjtList1.U0209021 }" var="sbjtList" varStatus="stat">
									<tr>
										<!--th : rowspan = 과목명 갯수-->
										<c:if test="${stat.index eq 0 }">
											<th scope="rowgroup" rowspan="${fn:length(sbjtList1.U0209021)}" class="border-end">${sbjtList.sbjtFgNm }</th>
										</c:if>
										<td class="border-end sbjtList12" >${sbjtList.openSustMjKorNm }</td>
										<td class="border-end">${sbjtList.sbjtKorNm }</td>
										<td class="border-end">
											<c:if test="${sbjtList.shtmNm eq '전체'}">
											${sbjtList.pnt }
											<c:set var="sbjtList12Tot" value="${sbjtList12Tot + sbjtList.pnt}" />
											</c:if>
										</td>
										<td class="border-end">
											<c:if test="${sbjtList.shtmNm eq '전'}">
												${sbjtList.pnt }
												<c:set var="sbjtList12One" value="${sbjtList12One + sbjtList.pnt}" />
											</c:if>
										</td>
										<td>
											<c:if test="${sbjtList.shtmNm eq '후'}">
												${sbjtList.pnt }
												<c:set var="sbjtList12Two" value="${sbjtList12Two + sbjtList.pnt}" />
											</c:if>
										</td>
									</tr>
								</c:forEach>
								<tr class="table_sum">
									<td colspan="3" class="border-end">소계</td>
									<td class="border-end">${sbjtList12Tot }</td>
									<td class="border-end">${sbjtList12One }</td>
									<td>${sbjtList12Two }</td>
								</tr>
							</c:if>
							<c:if test="${not empty sbjtList1.U0209022 }">
								<c:set var="sbjtList13Tot" value="0" />
								<c:set var="sbjtList13One" value="0" />
								<c:set var="sbjtList13Two" value="0" />
								<c:forEach items="${sbjtList1.U0209022 }" var="sbjtList" varStatus="stat">
									<tr>
										<!--th : rowspan = 과목명 갯수-->
										<c:if test="${stat.index eq 0 }">
											<th scope="rowgroup" rowspan="${fn:length(sbjtList1.U0209022)}" class="border-end">${sbjtList.sbjtFgNm }</th>
										</c:if>
										<td class="border-end sbjtList13" >${sbjtList.openSustMjKorNm }</td>
										<td class="border-end">${sbjtList.sbjtKorNm }</td>
										<td class="border-end">
											<c:if test="${sbjtList.shtmNm eq '전체'}">
											${sbjtList.pnt }
											<c:set var="sbjtList13Tot" value="${sbjtList13Tot + sbjtList.pnt}" />
											</c:if>
										</td>
										<td class="border-end">
											<c:if test="${sbjtList.shtmNm eq '전'}">
												${sbjtList.pnt }
												<c:set var="sbjtList13One" value="${sbjtList13One + sbjtList.pnt}" />
											</c:if>
										</td>
										<td>
											<c:if test="${sbjtList.shtmNm eq '후'}">
												${sbjtList.pnt }
												<c:set var="sbjtList13Two" value="${sbjtList13Two + sbjtList.pnt}" />
											</c:if>
										</td>
									</tr>
								</c:forEach>
								<tr class="table_sum">
									<td colspan="3" class="border-end">소계</td>
									<td class="border-end" id="sbjt1AllTotalCnt">${sbjtList13Tot }</td>
									<td class="border-end" id="sbjt1OneTotalCnt">${sbjtList13One }</td>
									<td id="sbjt1TwoTotalCnt">${sbjtList13Two }</td>
								</tr>
							</c:if>
							</tbody>
						</table>
					</div>
					<div class="table_box">
						<table class="tbListC">
							<caption class="blind">교과목 설계범위 2학년</caption>
							<colgroup>
								<col width="15%">
								<col width="25%">
								<col width="25%">
								<col width="15%">
								<col width="10%">
								<col width="10%">
							</colgroup>
							<thead>
								<tr>
									<th scope="colgroup" colspan="6">2학년</th>
								</tr>
								<tr>
									<th scope="col" rowspan="2" class="border-end">구분</th>
									<th scope="col" rowspan="2" class="border-end">개설전공</th>
									<th scope="col" rowspan="2" class="border-end">과목명</th>
									<th scope="colgroup" colspan="3">학점</th>
								</tr>
								<tr>
									<th scope="col" class="border-end">전체</th>
									<th scope="col" class="border-end">전</th>
									<th scope="col">후</th>
								</tr>
							</thead>
							<tbody id="grade2">
								<c:if test="${not empty sbjtList2.U0209020 }">
									<c:set var="sbjtList21Tot" value="0" />
									<c:set var="sbjtList21One" value="0" />
									<c:set var="sbjtList21Two" value="0" />
									<c:forEach items="${sbjtList2.U0209020 }" var="sbjtList" varStatus="stat">
										<tr>
											<!--th : rowspan = 과목명 갯수-->
											<c:if test="${stat.index eq 0 }">
												<th scope="rowgroup" rowspan="${fn:length(sbjtList2.U0209020)}" class="border-end">${sbjtList.sbjtFgNm }</th>
											</c:if>
											<td class="border-end sbjtList21">${sbjtList.openSustMjKorNm }</td>
											<td class="border-end">${sbjtList.sbjtKorNm }</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList21Tot" value="${sbjtList21Tot + sbjtList.pnt}" />
												</c:if>
											</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전'}">
													${sbjtList.pnt }
													<c:set var="sbjtList21One" value="${sbjtList21One + sbjtList.pnt}" />
												</c:if>
											</td>
											<td>
												<c:if test="${sbjtList.shtmNm eq '후'}">
													${sbjtList.pnt }
													<c:set var="sbjtList21Two" value="${sbjtList21Two + sbjtList.pnt}" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
									<tr class="table_sum">
										<td colspan="3" class="border-end">소계</td>
										<td class="border-end">${sbjtList21Tot }</td>
										<td class="border-end">${sbjtList21One }</td>
										<td>${sbjtList21Two }</td>
									</tr>
								</c:if>
								<c:if test="${not empty sbjtList2.U0209021 }">
									<c:set var="sbjtList22Tot" value="0" />
									<c:set var="sbjtList22One" value="0" />
									<c:set var="sbjtList22Two" value="0" />
									<c:forEach items="${sbjtList2.U0209021 }" var="sbjtList" varStatus="stat">
										<tr>
											<!--th : rowspan = 과목명 갯수-->
											<c:if test="${stat.index eq 0 }">
												<th scope="rowgroup" rowspan="${fn:length(sbjtList2.U0209021)}" class="border-end">${sbjtList.sbjtFgNm }</th>
											</c:if>
											<td class="border-end sbjtList22">${sbjtList.openSustMjKorNm }</td>
											<td class="border-end">${sbjtList.sbjtKorNm }</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList22Tot" value="${sbjtList22Tot + sbjtList.pnt}" />
												</c:if>
											</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전'}">
													${sbjtList.pnt }
													<c:set var="sbjtList22One" value="${sbjtList22One + sbjtList.pnt}" />
												</c:if>
											</td>
											<td>
												<c:if test="${sbjtList.shtmNm eq '후'}">
													${sbjtList.pnt }
													<c:set var="sbjtList22Two" value="${sbjtList22Two + sbjtList.pnt}" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
									<tr class="table_sum">
										<td colspan="3" class="border-end">소계</td>
										<td class="border-end">${sbjtList22Tot }</td>
										<td class="border-end">${sbjtList22One }</td>
										<td>${sbjtList22Two }</td>
									</tr>
								</c:if>
								<c:if test="${not empty sbjtList2.U0209022 }">
									<c:set var="sbjtList23Tot" value="0" />
									<c:set var="sbjtList23One" value="0" />
									<c:set var="sbjtList23Two" value="0" />
									<c:forEach items="${sbjtList2.U0209022 }" var="sbjtList" varStatus="stat">
										<tr>
											<!--th : rowspan = 과목명 갯수-->
											<c:if test="${stat.index eq 0 }">
												<th scope="rowgroup" rowspan="${fn:length(sbjtList2.U0209022)}" class="border-end">${sbjtList.sbjtFgNm }</th>
											</c:if>
											<td class="border-end sbjtList23">${sbjtList.openSustMjKorNm }</td>
											<td class="border-end">${sbjtList.sbjtKorNm }</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList23Tot" value="${sbjtList23Tot + sbjtList.pnt}" />
												</c:if>
											</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전'}">
													${sbjtList.pnt }
													<c:set var="sbjtList23One" value="${sbjtList23One + sbjtList.pnt}" />
												</c:if>
											</td>
											<td>
												<c:if test="${sbjtList.shtmNm eq '후'}">
													${sbjtList.pnt }
													<c:set var="sbjtList23Two" value="${sbjtList23Two + sbjtList.pnt}" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
									<tr class="table_sum">
										<td colspan="3" class="border-end">소계</td>
										<td class="border-end">${sbjtList23Tot }</td>
										<td class="border-end">${sbjtList23One }</td>
										<td>${sbjtList23Two }</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
					<div class="table_box">
						<table class="tbListC">
							<caption class="blind">교과목 설계범위 3학년</caption>
							<colgroup>
								<col width="15%">
								<col width="25%">
								<col width="25%">
								<col width="15%">
								<col width="10%">
								<col width="10%">
							</colgroup>
							<thead>
								<tr>
									<th scope="colgroup" colspan="6">3학년</th>
								</tr>
								<tr>
									<th scope="col" rowspan="2" class="border-end">구분</th>
									<th scope="col" rowspan="2" class="border-end">개설전공</th>
									<th scope="col" rowspan="2" class="border-end">과목명</th>
									<th scope="colgroup" colspan="3">학점</th>
								</tr>
								<tr>
									<th scope="col" class="border-end">전체</th>
									<th scope="col" class="border-end">전</th>
									<th scope="col">후</th>
								</tr>
							</thead>
							<tbody id="grade3">
								<c:if test="${not empty sbjtList3.U0209020 }">
									<c:set var="sbjtList31Tot" value="0" />
									<c:set var="sbjtList31One" value="0" />
									<c:set var="sbjtList31Two" value="0" />
									<c:forEach items="${sbjtList3.U0209020 }" var="sbjtList" varStatus="stat">
										<tr>
											<!--th : rowspan = 과목명 갯수-->
											<c:if test="${stat.index eq 0 }">
												<th scope="rowgroup" rowspan="${fn:length(sbjtList3.U0209020)}" class="border-end">${sbjtList.sbjtFgNm }</th>
											</c:if>
											<td class="border-end sbjtList31" >${sbjtList.openSustMjKorNm }</td>
											<td class="border-end">${sbjtList.sbjtKorNm }</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList31Tot" value="${sbjtList31Tot + sbjtList.pnt}" />
												</c:if>
											</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전'}">
													${sbjtList.pnt }
													<c:set var="sbjtList31One" value="${sbjtList31One + sbjtList.pnt}" />
												</c:if>
											</td>
											<td>
												<c:if test="${sbjtList.shtmNm eq '후'}">
													${sbjtList.pnt }
													<c:set var="sbjtList31Two" value="${sbjtList31Two + sbjtList.pnt}" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
									<tr class="table_sum">
										<td colspan="3" class="border-end">소계</td>
										<td class="border-end">${sbjtList31Tot }</td>
										<td class="border-end">${sbjtList31One }</td>
										<td>${sbjtList31Two }</td>
									</tr>
								</c:if>
								<c:if test="${not empty sbjtList3.U0209021 }">
									<c:set var="sbjtList32Tot" value="0" />
									<c:set var="sbjtList32One" value="0" />
									<c:set var="sbjtList32Two" value="0" />
									<c:forEach items="${sbjtList3.U0209021 }" var="sbjtList" varStatus="stat">
										<tr>
											<!--th : rowspan = 과목명 갯수-->
											<c:if test="${stat.index eq 0 }">
												<th scope="rowgroup" rowspan="${fn:length(sbjtList3.U0209021)}" class="border-end">${sbjtList.sbjtFgNm }</th>
											</c:if>
											<td class="border-end sbjtList32" >${sbjtList.openSustMjKorNm }</td>
											<td class="border-end">${sbjtList.sbjtKorNm }</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList32Tot" value="${sbjtList32Tot + sbjtList.pnt}" />
												</c:if>
											</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전'}">
													${sbjtList.pnt }
													<c:set var="sbjtList32One" value="${sbjtList32One + sbjtList.pnt}" />
												</c:if>
											</td>
											<td>
												<c:if test="${sbjtList.shtmNm eq '후'}">
													${sbjtList.pnt }
													<c:set var="sbjtList32Two" value="${sbjtList32Two + sbjtList.pnt}" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
									<tr class="table_sum">
										<td colspan="3" class="border-end">소계</td>
										<td class="border-end">${sbjtList32Tot }</td>
										<td class="border-end">${sbjtList32One }</td>
										<td>${sbjtList32Two }</td>
									</tr>
								</c:if>
								<c:if test="${not empty sbjtList3.U0209022 }">
									<c:set var="sbjtList33Tot" value="0" />
									<c:set var="sbjtList33One" value="0" />
									<c:set var="sbjtList33Two" value="0" />
									<c:forEach items="${sbjtList3.U0209022 }" var="sbjtList" varStatus="stat">
										<tr>
											<!--th : rowspan = 과목명 갯수-->
											<c:if test="${stat.index eq 0 }">
												<th scope="rowgroup" rowspan="${fn:length(sbjtList3.U0209022)}" class="border-end">${sbjtList.sbjtFgNm }</th>
											</c:if>
											<td class="border-end sbjtList33" >${sbjtList.openSustMjKorNm }</td>
											<td class="border-end">${sbjtList.sbjtKorNm }</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList33Tot" value="${sbjtList33Tot + sbjtList.pnt}" />
												</c:if>
											</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전'}">
													${sbjtList.pnt }
													<c:set var="sbjtList33One" value="${sbjtList33One + sbjtList.pnt}" />
												</c:if>
											</td>
											<td>
												<c:if test="${sbjtList.shtmNm eq '후'}">
													${sbjtList.pnt }
													<c:set var="sbjtList33Two" value="${sbjtList33Two + sbjtList.pnt}" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
									<tr class="table_sum">
										<td colspan="3" class="border-end">소계</td>
										<td class="border-end">${sbjtList33Tot }</td>
										<td class="border-end">${sbjtList33One }</td>
										<td>${sbjtList33Two }</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
					<div class="table_box">
						<table class="tbListC">
							<caption class="blind">교과목 설계범위 4학년</caption>
							<colgroup>
								<col width="15%">
								<col width="25%">
								<col width="25%">
								<col width="15%">
								<col width="10%">
								<col width="10%">
							</colgroup>
							<thead>
								<tr>
									<th scope="colgroup" colspan="6">4학년</th>
								</tr>
								<tr>
									<th scope="col" rowspan="2" class="border-end">구분</th>
									<th scope="col" rowspan="2" class="border-end">개설전공</th>
									<th scope="col" rowspan="2" class="border-end">과목명</th>
									<th scope="colgroup" colspan="3">학점</th>
								</tr>
								<tr>
									<th scope="col" class="border-end">전체</th>
									<th scope="col" class="border-end">전</th>
									<th scope="col">후</th>
								</tr>
							</thead>
							<tbody id="grade4">
								<c:if test="${not empty sbjtList4.U0209020 }">
									<c:set var="sbjtList41Tot" value="0" />
									<c:set var="sbjtList41One" value="0" />
									<c:set var="sbjtList41Two" value="0" />
									<c:forEach items="${sbjtList4.U0209020 }" var="sbjtList" varStatus="stat">
										<tr>
											<!--th : rowspan = 과목명 갯수-->
											<c:if test="${stat.index eq 0 }">
												<th scope="rowgroup" rowspan="${fn:length(sbjtList4.U0209020)}" class="border-end">${sbjtList.sbjtFgNm }</th>
											</c:if>
											<td class="border-end sbjtList41" >${sbjtList.openSustMjKorNm }</td>
											<td class="border-end">${sbjtList.sbjtKorNm }</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList41Tot" value="${sbjtList41Tot + sbjtList.pnt}" />
												</c:if>
											</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전'}">
													${sbjtList.pnt }
													<c:set var="sbjtList41One" value="${sbjtList41One + sbjtList.pnt}" />
												</c:if>
											</td>
											<td>
												<c:if test="${sbjtList.shtmNm eq '후'}">
													${sbjtList.pnt }
													<c:set var="sbjtList41Two" value="${sbjtList41Two + sbjtList.pnt}" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
									<tr class="table_sum">
										<td colspan="3" class="border-end">소계</td>
										<td class="border-end">${sbjtList41Tot }</td>
										<td class="border-end">${sbjtList41One }</td>
										<td>${sbjtList41Two }</td>
									</tr>
								</c:if>
								<c:if test="${not empty sbjtList4.U0209021 }">
									<c:set var="sbjtList42Tot" value="0" />
									<c:set var="sbjtList42One" value="0" />
									<c:set var="sbjtList42Two" value="0" />
									<c:forEach items="${sbjtList4.U0209021 }" var="sbjtList" varStatus="stat">
										<tr>
											<!--th : rowspan = 과목명 갯수-->
											<c:if test="${stat.index eq 0 }">
												<th scope="rowgroup" rowspan="${fn:length(sbjtList4.U0209021)}" class="border-end">${sbjtList.sbjtFgNm }</th>
											</c:if>
											<td class="border-end sbjtList42" >${sbjtList.openSustMjKorNm }</td>
											<td class="border-end">${sbjtList.sbjtKorNm }</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList42Tot" value="${sbjtList42Tot + sbjtList.pnt}" />
												</c:if>
											</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전'}">
													${sbjtList.pnt }
													<c:set var="sbjtList42One" value="${sbjtList42One + sbjtList.pnt}" />
												</c:if>
											</td>
											<td>
												<c:if test="${sbjtList.shtmNm eq '후'}">
													${sbjtList.pnt }
													<c:set var="sbjtList42Two" value="${sbjtList42Two + sbjtList.pnt}" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
									<tr class="table_sum">
										<td colspan="3" class="border-end">소계</td>
										<td class="border-end">${sbjtList42Tot }</td>
										<td class="border-end">${sbjtList42One }</td>
										<td>${sbjtList42Two }</td>
									</tr>
								</c:if>
								<c:if test="${not empty sbjtList4.U0209022 }">
									<c:set var="sbjtList43Tot" value="0" />
									<c:set var="sbjtList43One" value="0" />
									<c:set var="sbjtList43Two" value="0" />
									<c:forEach items="${sbjtList4.U0209022 }" var="sbjtList" varStatus="stat">
										<tr>
											<!--th : rowspan = 과목명 갯수-->
											<c:if test="${stat.index eq 0 }">
												<th scope="rowgroup" rowspan="${fn:length(sbjtList4.U0209022)}" class="border-end">${sbjtList.sbjtFgNm }</th>
											</c:if>
											<td class="border-end sbjtList43">${sbjtList.openSustMjKorNm }</td>
											<td class="border-end">${sbjtList.sbjtKorNm }</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전체'}">
												${sbjtList.pnt }
												<c:set var="sbjtList43Tot" value="${sbjtList43Tot + sbjtList.pnt }" />
												</c:if>
											</td>
											<td class="border-end">
												<c:if test="${sbjtList.shtmNm eq '전'}">
													${sbjtList.pnt }
													<c:set var="sbjtList43One" value="${sbjtList43One + sbjtList.pnt }" />
												</c:if>
											</td>
											<td>
												<c:if test="${sbjtList.shtmNm eq '후'}">
													${sbjtList.pnt }
													<c:set var="sbjtList43Two" value="${sbjtList43Two + sbjtList.pnt }" />
												</c:if>
											</td>
										</tr>
									</c:forEach>
									<tr class="table_sum">
										<td colspan="3" class="border-end">소계</td>
										<td class="border-end">${sbjtList43Tot }</td>
										<td class="border-end">${sbjtList43One }</td>
										<td>${sbjtList43Two }</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</div>
				</div>
			</section>
			<!--설계학점 총계-->
			<section class="plan_dtl_section plan_total pt-4" id="move_acitve3">
				<h4 class="mt20">설계학점 총계</h4>
				<table class="tbListA alignC">
                       <caption class="blind">설계학점 총점</caption>
                       <colgroup>
                           <col width="10%" />
							<col width="15%" />
							<col width="30%" />
							<col width="30%" />
							<col width="15%" />
                       </colgroup>
                       <thead>
                           <tr>
                               <th scope="col" class="text-center">번호</th>
                               <th scope="col">대학</th>
                               <th scope="col">단과대학</th>
                               <th scope="col">학부&middot;학과(전공)</th>
                               <th scope="col">설계 학점</th>
                           </tr>
                       </thead>
                       <tbody id="appndSumPnt">
                      		<c:set var="getSdmSbjtTotalPnt" value="0" />
							<c:forEach items="${getSdmSbjtTotal}" var="getSdmSbjtTotal" varStatus="stat">
	                           <tr>
	                               <td class="numb text-center"><c:if test="${stat.count lt 10}">0</c:if>${stat.count }</td>
	                               <td class="univ">
		                           	   <c:choose>
			                           	   <c:when test="${getSdmSbjtTotal.origFg eq 'U0175020'}">타대학</c:when>
			                           	   <c:when test="${getSdmSbjtTotal.origFg eq 'U0175030'}">온라인</c:when>
			                           	   <c:otherwise>부경대학교</c:otherwise>
		                           	   </c:choose>
	                           	   </td>
	                               <td class="coll">${getSdmSbjtTotal.openColgKorNm }</td>
	                               <td class="majo">${getSdmSbjtTotal.openSustMjKorNm }</td>
	                               <td class="scor">${getSdmSbjtTotal.pnt }학점</td>
	                           </tr>
	                           <c:set var="getSdmSbjtTotalPnt" value="${getSdmSbjtTotalPnt + getSdmSbjtTotal.pnt }" />
							</c:forEach>
                       </tbody>
                   </table>
				<div class="dtl_bottom py-2 px-3 d-flex flex-row align-items-center justify-content-end mt-3 gap-2">
					<h5>총 설계학점 합계</h5>
					<p class="bg-white py-2 px-4">
						<input type="hidden" id="desTotCnt" value="${getSdmSbjtTotalPnt }" />
						<span class="d-inline-block pe-1 totalPnt">${getSdmSbjtTotalPnt }</span>
						학점
					</p>
				</div>
			</section>
		</section>
		</div>
		<!-- 컨설팅 이력 -->
		<div id="2" style="display:none;">
			<div class="">
				<h4 class="mt20">컨설팅 상담</h4>
				<table class="tbListB">
					<colgroup>
	                    <col width="15%"/>
	                    <col width="15%"/>
	                    <col width="70%"/>
	                </colgroup>
	                <tbody>
	                	<c:forEach var="cnslDt" items="${cnslList}" varStatus="i">
	                		<tr>
		                		<td rowspan="3"><c:out value="${cnslDt.CNSLT_PROF_STAFF_NM}"/></td>
		                		<td>교과목 구성 적합성</td>
	                			<td>
	                				<c:choose>
	                					<c:when test="${!empty cnslDt.SBJT_CNST_ADV_OPIN}"><c:out value="${cnslDt.SBJT_CNST_ADV_OPIN}"/></c:when>
	                					<c:otherwise>-</c:otherwise>
	                				</c:choose>
	                			</td>
	                		</tr>
	                		<tr>
	                			<td>전공 명칭 적합성</td>
	                			<td>
	                				<c:choose>
	                					<c:when test="${!empty cnslDt.MJ_NM_ADV_OPIN}"><c:out value="${cnslDt.MJ_NM_ADV_OPIN}"/></c:when>
	                					<c:otherwise>-</c:otherwise>
	                				</c:choose>
	                			</td>
	                		</tr>
	                		<tr>
	                			<td>수여학위 적합성</td>
	                			<td>
	                				<c:choose>
	                					<c:when test="${!empty cnslDt.AWD_DEGR_NM_ADV_OPIN}"><c:out value="${cnslDt.AWD_DEGR_NM_ADV_OPIN}"/></c:when>
	                					<c:otherwise>-</c:otherwise>
	                				</c:choose>
	                			</td>
	                		</tr>
	                	</c:forEach>
	                </tbody>
				</table>
			</div>
		</div>
		<!-- 심사 이력 -->
		<div id="3" style="display:none;">
			<div class="">
				<h4 class="mt20">학생설계전공 심사표</h4>
				<table class="tbListB">
					<colgroup>
	                    <col width="15%"/>
	                    <col width="15%"/>
	                    <col width="7%"/>
	                    <col width="7%"/>
	                    <col width="7%"/>
	                    <col width="49%"/>
	                </colgroup>
	                <thead>
	                	<tr>
	                		<th>심사 교수</th>
	                		<th>심의 항목</th>
	                		<th>적합</th>
	                		<th>일부 수정</th>
	                		<th>재검토</th>
	                		<th>검토의견</th>
	                	</tr>
	                </thead>
	                <tbody>
	               	<c:forEach var="judDt" items="${judList}" varStatus="i">
	               		<tr>
	               			<td rowspan="3"><c:out value="${judDt.JUDG_STAFF_NM}"/></td>
	               			<td>교과목 구성 적합성</td>
	               			<td><c:if test="${judDt.SBJT_CNST_JUDG_FG eq 'U0181010'}">V</c:if></td>
	               			<td><c:if test="${judDt.SBJT_CNST_JUDG_FG eq 'U0181020'}">V</c:if></td>
	               			<td><c:if test="${judDt.SBJT_CNST_JUDG_FG eq 'U0181030'}">V</c:if></td>
	               			<td>
	               				<c:choose>
	               					<c:when test="${!empty judDt.SBJT_CNST_QLFD}"><c:out value="${judDt.SBJT_CNST_QLFD}"/></c:when>
	               					<c:otherwise>-</c:otherwise>
	               				</c:choose>
	               			</td>
	               		</tr>
	               		<tr>
	               			<td>전공 명칭 적합성</td>
	               			<td><c:if test="${judDt.MJ_NM_JUDG_FG eq 'U0181010'}">V</c:if></td>
	               			<td><c:if test="${judDt.MJ_NM_JUDG_FG eq 'U0181020'}">V</c:if></td>
	               			<td><c:if test="${judDt.MJ_NM_JUDG_FG eq 'U0181030'}">V</c:if></td>
	               			<td>
	               				<c:choose>
	               					<c:when test="${!empty judDt.MJ_NM_QLFD}"><c:out value="${judDt.MJ_NM_QLFD}"/></c:when>
	               					<c:otherwise>-</c:otherwise>
	               				</c:choose>
	               			</td>
	               		</tr>
	               		<tr>
	               			<td>수여학위명 적합성</td>
	               			<td><c:if test="${judDt.AWD_DEGR_NM_JUDG_FG eq 'U0181010'}">V</c:if></td>
	               			<td><c:if test="${judDt.AWD_DEGR_NM_JUDG_FG eq 'U0181020'}">V</c:if></td>
	               			<td><c:if test="${judDt.AWD_DEGR_NM_JUDG_FG eq 'U0181030'}">V</c:if></td>
	               			<td>
	               				<c:choose>
	               					<c:when test="${!empty judDt.AWD_DEGR_NM_QLFD}"><c:out value="${judDt.AWD_DEGR_NM_QLFD}"/></c:when>
	               					<c:otherwise>-</c:otherwise>
	               				</c:choose>
	               			</td>
	               		</tr>
	                </c:forEach>
		        	</tbody>
				</table>
			</div>
		</div>
		<div class="btnCenter">
			<button type="button" class="btnTypeC fn_btn_reload" style="margin:0 5px;">조회</button>
			<button type="submit" id="btn-modify" class="btnTypeA fn_btn_submit">저장</button>
			<a href="${URL_JUDGERESULTLIST}&aply=asc&tret=asc" class="btnTypeB">목록</a>
		</div>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>
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
								<button class="nav-link w-100" id="micro-tab" data-bs-toggle="tab" data-bs-target="#micro-tab-pane" type="button" role="tab" aria-controls="micro-tab-pane" aria-selected="false">마이크로전공</button>
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
												<li class="form-check  col-3">
													<input type="checkbox" id="chkMaj_all" class="form-check-input">
													<label class="form-check-label" for="chkMaj_all">전체</label>
												</li>
												<li class="form-check  col-3">
													<input type="checkbox" id="chkNation" class="form-check-input chk-sbjt-search" value="U0209020" checked>
													<label class="form-check-label" for="chkNation">전공공통</label>
												</li>
												<li class="form-check  col-3">
													<input type="checkbox" id="chkMaj_Requi" class="form-check-input chk-sbjt-search" value="U0209021">
													<label class="form-check-label" for="chkMaj_Requi">전공필수</label>
												</li>
												<li class="form-check  col-3">
													<input type="checkbox" id="chkMaj_Selc" class="form-check-input chk-sbjt-search" value="U0209022">
													<label class="form-check-label" for="chkMaj_Selc">전공선택</label>
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
									<button type="button" class="border-0 text-center w-100 d-block" id="searchSbjt">검색</button>
								</div>
							</div>
							<div class="tab-pane fade" id="micro-tab-pane" role="tabpanel" aria-labelledby="micro-tab" tabindex="0">
								<!--마이크로전공-->
								<div class="wrap p-4">
									<div class="d-flex flex-row align-items-center">
										<h5 class="mic">마이크로전공</h5>
										<div class="">
											<label class="blind">전공</label>
											<select class="form-select w-100" id="selectMcm">
											</select>
										</div>
									</div>
									<button type="button" class="border-0 text-white text-center w-100 d-block" id="searchMcmSbjt">검색</button>
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
											<label class="blind">전공</label>
											<select class="form-select" id="selectMjMy">
												<option selected>전공</option>
											</select>
										</div>
									</div>
									<div class="d-flex flex-row align-items-center mb-3">
										<h5>이수구분</h5>
										<div class="">
											<ul class="d-flex flex-row">
												<li class="form-check  col-4">
													<input type="checkbox" id="chkNationMy" class="form-check-input chk-sbjt-searchMy" value="U0209020">
													<label class="form-check-label" for="chkNationMy">전국공통</label>
												</li>
												<li class="form-check  col-4">
													<input type="checkbox" id="chkMaj_RequiMy" class="form-check-input chk-sbjt-searchMy" value="U0209021">
													<label class="form-check-label" for="chkMaj_RequiMy">전공필수</label>
												</li>
												<li class="form-check  col-4">
													<input type="checkbox" id="chkMaj_SelcMy" class="form-check-inpu chk-sbjt-searchMy" value="U0209022">
													<label class="form-check-label" for="chkMaj_SelcMy">전공선택</label>
												</li>
											</ul>
										</div>
									</div>
									<div class="d-flex flex-row align-items-center">
										<h5>키워드</h5>
										<div class="">
											<input type="text" class="form-control" placeholder="키워드 검색">
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
							<button type="button" class="select_put border-0" id="btnAddSbjt">
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
						<h2 class="">설계 선택 목록</h2>
						<div class="d-flex flex-row align-items-center ">
							<label for="orderSort">정렬순서</label>
							<select class="form-select" id="orderSort">
								<option selected>최신순</option>
								<option value="1">One</option>
								<option value="2">Two</option>
								<option value="3">Three</option>
							</select>
						</div>
					</div>
					<!--item wrap-->
					<div class="lesson_wrap" id="appndSbjtChoice">
						<c:forEach items="${sbjtAllList }" var="choiceList">
							<div class="item p-3 head-after<c:if test="${choiceList.origFg ne 'U0175010' }"> otherColg</c:if>" id="headAfter-${choiceList.yy }${choiceList.shtmCdTemp }${choiceList.courseNo }${choiceList.openSustMjCd }${choiceList.shyrFg }" <c:if test="${choiceList.origFg ne 'U0175010' }">aria-cnt="${choiceList.yy }${choiceList.shtmCdTemp }${choiceList.courseNo }${choiceList.openSustMjCd }${choiceList.shyrFg }" aria-colgNm="${choiceList.openColgKorNm }" aria-yy="${choiceList.yy }" aria-origFg="${choiceList.origFg }" aria-gubun="${choiceList.sbjtFg }" aria-gubunNm="${choiceList.sbjtFgNm }" aria-extOpMj="${choiceList.openSustMjKorNm }" aria-extNmMj="${choiceList.sbjtKorNm }" aria-extYear="${choiceList.shyrFg }" aria-extYearNm="${choiceList.shyrFgNm }" aria-extSemi="${choiceList.shtmCdTemp }" aria-extSemiNm="${choiceList.shtmNm }" aria-extScore="${choiceList.pnt }"</c:if>>
								<input type="hidden" name="sdmAddList" aria-yy="${choiceList.yy }" aria-shtmCd="${choiceList.shtmCdTemp }" aria-shtmNm="${choiceList.shtmNm }" aria-courseNo="${choiceList.courseNo }" aria-openSustMjCd="${choiceList.openSustMjCd }" aria-openSustMjNm="${choiceList.openSustMjKorNm }" aria-openShyrFg="${choiceList.shyrFg }" aria-openShyrFgNm="${choiceList.shyrFgNm }" aria-openColgCd="${choiceList.openColgCd }" aria-openColgNm="${choiceList.openColgKorNm }" aria-sbjtFg="${choiceList.sbjtFg }" aria-sbjtFgNm="${choiceList.sbjtFgNm }" aria-sbjtKorNm="${choiceList.sbjtKorNm }" aria-sbjtEngNm="${choiceList.sbjtEngNm }" aria-pnt="${choiceList.pnt }" aria-theoTmCnt="${choiceList.theoTmCnt }" aria-pracTmCnt="${choiceList.pracTmCnt }" aria-origFg="${choiceList.origFg }" aria-sbjtOrgFg="${choiceList.orgSbjtFg }" />
								<div class="d-inline-flex form-check">
									<label class="blind" for="lessons_${choiceList.yy }${choiceList.shtmCdTemp }${choiceList.courseNo }${choiceList.openSustMjCd }${choiceList.shyrFg }">선택</label>
									<input type="checkbox" id="lessons_${choiceList.yy }${choiceList.shtmCdTemp }${choiceList.courseNo }${choiceList.openSustMjCd }${choiceList.shyrFg }" class="form-check-input chk-after">
								</div>
								<section class="d-inline-flex flex-colummn flex-lg-row justify-content-between">
									<div class="d-flex flex-column align-items-start div-after">
										<div class="d-flex flex-column align-items-start" id="div-${choiceList.yy }${choiceList.shtmCd }${choiceList.courseNo }${choiceList.openSustMjCd }${choiceList.shyrFg }">
											<ul class="cate d-flex flex-wrap gap-2">
													<li>
													<select class="form-select selectSbjtFg" id="selectSbjtFg">
														<option value="U0209021" <c:if test="${choiceList.sbjtFg eq 'U0209021'}"> selected </c:if> >전공필수</option>
														<option value="U0209022" <c:if test="${choiceList.sbjtFg eq 'U0209022'}"> selected </c:if> >전공선택</option>
													</select>
													</li>
												<li class="border">
													<span>${choiceList.ttOpenSustMjKorNm }</span>
												</li>
											</ul>
											<h4 class="text-truncate ellip_2 my-2 h-sbjt-kor-nm" id="sbjtNm-${choiceList.yy }${choiceList.shtmCd }${choiceList.courseNo }${choiceList.openSustMjCd }${choiceList.shyrFg }">${choiceList.sbjtKorNm }</h4>
											<div class="d-flex flex-row w-100 mb-1">
												<strong class="d-block me-2"><c:if test="${choiceList.origFg eq 'U0175010' }">[${choiceList.courseNo }]</c:if></strong>
												<span class="d-block mb-0 text-truncate text-dark text-opacity-75"><c:if test="${choiceList.origFg eq 'U0175010' }">${choiceList.sbjtEngNm }</c:if></span>
											</div>
											<div class="w-100">
												<dl class="d-inline-flex position-relative pe-4">
							 						<dt class="me-2">편성</dt>
													<dd class="text-dark text-opacity-75">${choiceList.yy }학년도 <c:if test="${choiceList.shtmCd eq 'U0214001'}">전체학기</c:if><c:if test="${choiceList.shtmCd eq 'U0214002'}">1학기</c:if><c:if test="${choiceList.shtmCd eq 'U0214003'}">2학기</c:if>&nbsp;${choiceList.shyrFgNm }</dd>
												</dl>
												<dl class="d-inline-flex">
													<dt class="me-2">학점</dt>
													<dd class="text-dark text-opacity-75">${choiceList.pnt }-${choiceList.theoTmCnt }-${choiceList.pracTmCnt }</dd>
												</dl>
											</div>
										</div>
									</div>
									<ol class="d-inline-flex btn_wrap">
										<li>
											<button type="button" class="border-0 bg-transparent btn_dele" onclick="delSbjt('${choiceList.yy }${choiceList.shtmCdTemp }${choiceList.courseNo }${choiceList.openSustMjCd }${choiceList.shyrFg }');">
												<span class="text-center ps-1 d-inline-block">삭제</span>
											</button>
										</li>
									</ol>
								</section>
							</div>
						</c:forEach>
						<c:if test="${empty sbjtAllList}">
							<div class="lesson_wrap no_contents_wrap d-flex justify-content-center align-items-center" id="no-data-img">
								<div class="d-flex flex-column justify-content-center align-items-center">
									<img src="${contextPath}/${crtSiteId}/assets/images/kmou_noshad_big.png" alt="해양이" />
									<p class="text-center mt-2">
										왼쪽 상자에서 원하는 교과목을<br />담아주세요.
									</p>
								</div>
							</div>
						</c:if>
					</div>
					<!--하단 버튼모음-->
					<section class="bottom_btn_group d-flex flex-row justify-content-between mt-3">
						<div class="d-flex flex-wrap gap-2">
							<button type="button" class="all_check text-white border-0 d-flex flex-row align-items-center gap-1 btn-fg" value="afterChk">
								<i></i>
								전체선택
							</button>
							<button type="button" class="remove_check d-inline-flex btn-fg" value="afterClear">선택 일괄해제</button>
							<button class="open_univ d-inline-flex align-items-center text-white border-0" id="otherColg">
								<img src="${contextPath}/${crtSiteId}/assets/images/ico_univ.png" alt="대학 아이콘" class="d-inline-block" /> 타 대학/온라인 교과목 등록
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
<div class="modal fade modal-xl modal_openUniv" id="openUniv" aria-hidden="true" aria-labelledby="openUnivLabel" tabindex="-1" style="display: none;">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header bg-black">
				<h1 class="modal-title fs-5 text-white" id="openUnivLabel">타 대학/온라인 교과목 등록</h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
					<img src="${contextPath}/${crtSiteId}/assets/images/ico_w_close.png" alt="닫기 아이콘" />
				</button>
			</div>
			<div class="modal-body">
				<table class="table">
					<caption class="blind">타대학/온라인교과목등록</caption>
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
								<label class="blind" for="ext_gubun">학점</label>
								<select class="form-select" id="ext_gubun">
									<option value="U0209020" selected>전공공통</option>
									<option value="U0209021">전공필수</option>
									<option value="U0209022">전공선택</option>
								</select>
							</td>
						</tr>
						<tr>
							<th scope="col">개설전공</th>
							<td colspan="3">
								<label for="ext_openMajor" class="blind">타대학 개설전공</label>
								<input id="ext_openMajor" type="text" placeholder="-" class="form-control">
							</td>
						</tr>
						<tr>
							<th scope="col">과목명</th>
							<td colspan="3">
								<label for="ext_nameMajor" class="blind">타대학 과목명</label>
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
													<option value="U0003001" selected>1학기</option>
													<option value="U0003002">2학기</option>
													<option value="U0003005">전체학기</option>
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
</form>