<%@ include file="../../../../include/commonTop.jsp"%>
<link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<link rel="stylesheet" type="text/css" href="/web/assets/css/sub.css">
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_techSupportSearchForm"/>
<c:set var="listFormId" value="fn_techSupportListForm"/>
<c:set var="inputWinFlag" value=""/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/viewInfo.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
 <!--content-->
<div class="container_wrap">
	<div class="sub_wrap">
		<!--본문-->
		<section class="inner mt-5">
			<!--제목-->
			<section class="d-flex flex-wrap gap-4" style="display:flex;">
				<div class="myP_info">
					<div class="info_box d-flex flex-wrap gap-2 align-items-center pt-2 pb-3">
						<input type="hidden" id="action" value="${action }">
						<div class="img_box">
						<c:choose>
							<c:when test="${not empty info.photo }">
								<img src="data:image/png;base64,${info.photo }" alt="내사진"  />'
							</c:when>
							<c:otherwise>
								<img src="/web/assets/images/contents/human.png" alt="기본이미지"/>
							</c:otherwise>
						</c:choose>
						</div>
						<div class="text_box">
							<h4>${info.korNm }</h4>
							<p>${info.engFnm }</p>
						</div>
						<ul>
							<li class="numb">${info.stdNo }</li>
							<li class="sch_y">
								<span>${info.currShyr }</span>
								학년 /
								<span>${info.cptnShtmCnt }</span>
								학기
							</li>
						</ul>
					</div>
					<dl>
						<dt>전공</dt>
						<dd>
							<span>${info.ttMjNm }</span>							
						</dd>
					</dl>
					<c:if test="${not empty info.mjFgNm1 }">
						<dl>
							<dt>${info.mjFgNm1 }</dt>
							<dd>
								<span>${info.ttMjNm1 }</span>
							</dd>
						</dl>
					</c:if>
					<c:if test="${not empty info.mjFgNm2 }">
						<dl>
							<dt>${info.mjFgNm2 }</dt>
							<dd>
								<span>${info.ttMjNm2 }
							</dd>
						</dl>
					</c:if>
					<c:if test="${not empty info.mjFgNm3 }">
						<dl>
							<dt>${info.mjFgNm3 }</dt>
							<dd>
								<span>${info.ttMjNm3 }
							</dd>
						</dl>
					</c:if>
				</div>
				<div class="myP_chart d-flex align-items-center justify-content-between flex-wrap" style="display:flex;">
					<div class="chart_box myGrade_box">
						<h5>나의 학점</h5>
<!--                      		이동근 : 현업요청, 총학점 문구 삭제 및 그래프 추가 수정 -->
                        <input type="hidden" id="ttBasePnt" value="${info.ttBasePnt }">
                        <input type="hidden" id="ttCptnPnt" value="${info.ttCptnPnt }">
                        <input type="hidden" id="sameShtmCptnPntAvg" value="${info.sameShtmCptnPntAvg }">
                        <div id="myCrd"></div>
					</div>
					<div class="chart_box myPerform_box">
						<h5>나의 성적</h5>
<!--                             이동근 : 현업요청, 나의성적 그래프 추가 -->
                        <input type="hidden" id="ttGpaAvg" value="${info.ttGpaAvg }">
                        <input type="hidden" id="ttGpaAvg100" value="${info.ttGpaAvg100 }">
                        <input type="hidden" id="sameMjShtmGpaAvg" value="${info.sameMjShtmGpaAvg }">
						<div id="myGrade"></div>
					</div>
                    <div class="chart_box diagno_box">
                    	<h5>핵심 역량</h5>
                        <c:forEach items="${stdCoreCapbDiag }" var="stdCoreCapbDiag">
                        	<input type="hidden" name="capbData" aria-capb-shyr="${stdCoreCapbDiag.shyr }" aria-capb-a01="${stdCoreCapbDiag.diagA01Tscor }" aria-capb-b01="${stdCoreCapbDiag.diagB01Tscor }" aria-capb-a02="${stdCoreCapbDiag.diagA02Tscor }" aria-capb-b02="${stdCoreCapbDiag.diagB02Tscor }" aria-capb-a03="${stdCoreCapbDiag.diagA03Tscor }" aria-capb-b03="${stdCoreCapbDiag.diagB03Tscor }">
                        </c:forEach>
                        <div id="myCapabilities"></div>
                    </div>
				</div>
			</section>
			<section class="myP_tab_wrap">
				<div class="tab-content" id="myTabContent">
					<div class="tab-pane fade myP_degrr_cont <c:if test="${action eq 'info' }">show active</c:if>" id="myDegr-tab-pane" role="tabpanel" aria-labelledby="myDegr-tab" tabindex="0">
						<!--전체 탭 컨텐츠-->
						<section class="table_item">
							<h5>소요학점</h5>
							<div class="d-flex flex-wrap gap-4 mb-4" style="display:flex;">
								<div class="req_credit_box">
									<table class="table mb-0">
										<caption class="blind">소요학점</caption>
										<colgroup>
											<col width="auto" />
											<col width="auto" />
											<col width="auto" />
											<col width="auto" />
										</colgroup>
										<thead>
											<tr>
												<th scope="col">구분</th>
												<th scope="col">기준학점</th>
												<th scope="col">취득학점</th>
												<th scope="col">수강중학점</th>
											</tr>
										</thead>
										<tbody>
										<c:forEach var="uniStd" items="${uniStd}">
											<tr>
												<td>${uniStd.fgNm }</td>
												<td>${uniStd.basePnt }</td>
												<td>${uniStd.cptnPnt}</td>
												<td>${uniStd.courPnt}</td>
											</tr>
										</c:forEach>
										</tbody>
									</table>
								</div>
								<div class="req_credit_chart_box">		
									<c:forEach var="uniStd" items="${uniStd}" varStatus="i">
										<input type="hidden" name="fgNm${i.index}" value="${uniStd.fgNm }">
										<input type="hidden" name="basePnt${i.index}" value="${uniStd.basePnt }">
										<input type="hidden" name="cptnPnt${i.index}" value="${uniStd.cptnPnt }">
										<input type="hidden" name="courPnt${i.index}" value="${uniStd.courPnt }">
										<c:if test="${i.last eq true }">
											<input type="hidden" name="uniStdLast" value="${(i.count)}"/>
										</c:if>		
									</c:forEach>						
		                            <div id="myReqCrdDetails"></div>
		                        </div>
							</div>
						</section>
						<section class="table_item accum_chek">
							<h5>누계성적조회</h5>
							<table class="table">
								<caption class="blind">누계성적조회</caption>
								<colgroup>
									<col width="auto" />
								</colgroup>
								<thead>
									<tr>
										<th scope="col" class="word_keep">학년도/학기</th>
										<th scope="col">신청학점</th>
										<th scope="col">취득학점</th>
										<th scope="col">평점</th>
										<th scope="col">백분율</th>
										<th scope="col">석차</th>
										<th scope="col">F 제외평점</th>
										<th scope="col">비고</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${shtmMrks }" var="shtmMrks">
										<tr class="shtmMrks">
											<td class="year word_keep">${shtmMrks.yy }-${shtmMrks.shtmCdNm }</td>
											<td class="appl applTot">${shtmMrks.modAplyPnt}</td>
											<td class="acqu acquTot">${shtmMrks.modAcqPnt}</td>
											<td class="gpas gpasTot">${shtmMrks.modGpaAvg}</td>
											<td class="perc percTot">${shtmMrks.gYul}</td>
											<td class="rank">
												<input type="hidden" class="mjRank" value="${shtmMrks.mjRank}">
												<input type="hidden" class="mjRankObjRcnt" value="${shtmMrks.mjRankObjRcnt}">
												<span>${shtmMrks.mjRank}</span>
												/ ${shtmMrks.mjRankObjRcnt}
											</td>
											<td class="fexc fexcTot">${shtmMrks.modAfGpaAvg}</td>
											<td class="rema">-</td>
										</tr>
									</c:forEach>
								</tbody>
								<tfoot>
									<tr>
										<th scrope="row">계</th>
										<td class="appl" id="applTot"></td>
										<td class="acqu" id="acquTot"></td>
										<td class="gpas" id="gpasTot"></td>
										<td class="perc" id="percTot"></td>
										<td class="rank" id="rankTot">
											<span>21</span>
											/ 51
										</td>
										<td class="fexc" id="fexcTot">3.44</td>
										<td class="rema">-</td>
									</tr>
								</tfoot>
							</table>
						</section>
						<section class="table_item perf_chek">
							<h5 class="d-flex flex-row justify-content-between align-items-center">
								성적조회
								<span class="retake_i">재수강가능</span>
							</h5>
							<table class="table">
								<caption class="blind">성적조회</caption>
								<colgroup>
									<col width="14%" />
									<col width="12%" />
									<col width="20%" />
									<col width="8%" />
									<col width="16%" />
									<col width="7%" />
									<col width="7%" />
									<col width="8%" />
									<col width="12%" />
								</colgroup>
								<thead>
									<tr>
										<th scope="col">학년도/학기</th>
										<th scope="col">과목번호</th>
										<th scope="col">교과목명</th>
										<th scope="col">이수구분</th>
										<th scope="col">이수영역</th>
										<th scope="col">학점</th>
										<th scope="col">등급</th>
										<th scope="col">성적유효구분</th>
										<th scope="col">입력 및 수정일자</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${sbjtMrks }" var="sbjtMrks">
										<tr class="<c:if test="${sbjtMrks.retlsnPosbYn eq 'Y'}">retake</c:if><c:if test="${sbjtMrks.retlsnPosbYn eq 'N'}"></c:if>">
											<td class="year">
												<p class="d-inline-block">${sbjtMrks.yy }-${sbjtMrks.shtmNm }</p>
											</td>
											<td class="numb">${sbjtMrks.courseNo}-${sbjtMrks.dclssNo }</td>
											<td class="name">${sbjtMrks.sbjtNm}</td>
											<td class="cate">${sbjtMrks.cptnFgNm}</td>
											<td class="area">${sbjtMrks.clsfNm}</td>
											<td class="scor">${sbjtMrks.pnt}</td>
											<td class="rank">${sbjtMrks.mrksGrd}</td>
											<td class="vali">${sbjtMrks.mrksValdYn}</td>
											<td class="modi">${sbjtMrks.mrksRecdDt}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</section>
						<section class="table_item table_item_finish">
							<h5>이수학년도</h5>
							<table class="table">
								<caption class="blind">이수학년도</caption>
								<colgroup>
									<col width="7%" />
									<col width="7%" />
									<col width="7%" />
									<col width="7%" />
									<col width="auto" />
									<col width="auto" />
									<col width="auto" />
									<col width="auto" />
									<col width="auto" />
									<col width="auto" />
								</colgroup>
								<thead>
									<tr>
										<th scope="col" rowspan="2">학년</th>
										<th scope="col" rowspan="2">학기</th>
										<th scope="col">성적</th>
										<th scope="colgroup" colspan="7">졸업사정</th>
									</tr>
									<tr>
										<th scope="col">이수년도</th>
										<th scope="col">적용년도</th>
										<th scope="col">복수전공1</th>
										<th scope="col">복수전공2</th>
										<th scope="col">연계전공</th>
										<th scope="col">융합전공</th>
										<th scope="col">학생설계전공</th>
										<th scope="col" class="word_keep">융합(공유)전공</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${cptnScyy}" var="cptnScyy">
										<tr>
											<td class="grad">${cptnScyy.shyrFg}학년</td>
											<td class="semi">${cptnScyy.shtmNm}</td>
											<td class="year_comp">${cptnScyy.cptnYy}</td>
											<td class="year_appl">${cptnScyy.yy}</td>
											<td class="doub1">${cptnScyy.dualYy1}</td>
											<td class="doub2">${cptnScyy.dualYy2}</td>
											<td class="rela">${cptnScyy.dualYy3}</td>
											<td class="conv">${cptnScyy.dualYy4}</td>
											<td class="stud">${cptnScyy.dualYy5}</td>
											<td class="shar">${cptnScyy.dualYy6}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</section>
					</div>
				</div>
			</section>
		</section>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>