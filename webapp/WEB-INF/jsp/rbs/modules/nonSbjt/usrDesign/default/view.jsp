<%@ include file="../../../../usr/web/include/commonTop.jsp"%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
	<div class="sub_background speci_bg">
                <section class="inner">
                    <h3 class="title fw-bolder text-center text-white">비교과</h3>
                </section>
            </div>
            <!--본문-->
            <section class="inner mt-5">
                <!--item 상세-->
                <section class="detail_title_wrap">
                    <section class="d-flex flex-row justify-content-between align-items-center title_box px-2 py-3 mb-5">
                        <a href="#" title="이전페이지" class="d-flex flex-row align-items-center gap-2"><img src="../images/arr_blue.png"/><em class="fst-normal">이전페이지</em></a>
                        <h5 class="content_title text-center fw-bolder d-flex align-items-center justify-content-center gap-1 flex-wrap">
                            <span class="col-12 col-md-auto text-truncate">${info.PROGRAM_TITLE }</span>
                        </h5>
                        <div id="${info.IDX}_${info.TIDX}" class="like_container">
                            <div class="link_cnt text-end">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange('${info.IDX}_${info.TIDX}','nonSbjt');">
                                <path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>
                                <path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/>
                                </svg>
                            </div>
                        </div>                                                
                    </section>
                </section>
               
                <main class="majref_dtl_wrap nonsbjt_wrap">
                    <h5 class="fw-semibold mb-3 nonsbjt_titl"></h5>
                    <!--전공일때 테이블-->
                    <div class="nonsbjt_top">
	                    <div class="nonsbjt_top_left">
	                    	<img width="100%" src="https://cts.kmou.ac.kr/attachment/view/${info.COVER}/cover.jpg?ts=${info.UPDATED_DATE}" onerror="this.src='${contextPath}${imgPath}/nonSbjt_no_image.png'">
							<h5>키워드</h5>
							<div class="keword">
								<c:if test="${!empty tagList }">
								<c:forEach var="tagDt" items="${tagList}" varStatus="status">
								    ${tagDt.tag}<c:if test="${!status.last}">, </c:if>
								</c:forEach>
		                        </c:if>
		                        <c:if test="${empty tagList }">
		                        	-
		                        </c:if>
							</div>
							
							<h5>주관기관</h5>
							<div class="apparatus">
								<div class="apparatus_info name_link">
                                 		<p>${info.DEPARTMENT }</p>
                                 		<a href="javascript:" onclick="goOceanCts('https://cts.kmou.ac.kr/ko/program/application?status=ALL&operator1=C&operator2=${info.IIDX}')" class="button_nonsbjtview_process">비교과 교육과정 보기</a>
                                </div>
                                <div class="apparatus_info">
                                  	<p><b>E-mail</b> ${info.EMAIL }</p>
                                  	<p><b>Number</b> ${info.CONTACT }</p>
                                  	<p><b>장소 </b> ${info.LOCATION }</p>
                                </div>
							</div>
							
	                    </div>
	                    <div class="nonsbjt_top_right">
	                    	<section class="table_wrap table_maj">
		                        <table class="table">
		                            <caption class="blind">비교과 교육과정 상세</caption>
		                            <!--colgroup>
		                                <col width="10%">
		                                <col width="15%">
		                                <col width="10%">
		                                
		                                <col width="15%">
		                                <col width="10%">
		                                <col width="15%">
		                                
		                                <col width="10%">
		                                <col width="15%">
		                            </colgroup-->
		                            <tbody>
		                                <tr class="">
		                                    <th scope="row" class="border-end">모집대상</th>
		                                    <td colspan="3" class="border-end">${info.SIGNIN_STATUS_NM}/${info.SIGNIN_TARGET_NM}</td>
		                                    <th scope="row" class="border-end">학년/성별</th>
		                                    <td colspan="3" class="border-end">${info.SIGNIN_GRADE} 학년 / ${info.SIGNIN_GENDER}</td>
		                                </tr>
		                                <tr>
		                                    <th scope="row" class="border-end">학과</th>
		                                    <td colspan="3" class="border-end">${info.IIDX_NM }<c:if test="${!empty info.DIDX_NM }">${info.DIDX_NM }</c:if></td>
		                                    <th scope="row" class="border-end">기대점수</th>
		                                    <td colspan="3" class="border-end">${info.EXPECTED_SCORE } / 5</td>
		                                </tr>
		                                <tr>
		                                    <th scope="row" class="border-end">만족도</th>
		                                    <td colspan="7" class="border-end" style="white-space: pre-wrap;">${info.SATISFACTION} / 5.00점</td>
		                                </tr>
		                                <tr>
		                                    <th scope="row" class="border-end">설명</th>
		                                    <td colspan="7" class="border-end" style="white-space: pre-wrap;">${info.ABSTRACT}</td>
		                                </tr>
		                                <tr>
		                                    <th scope="row" class="border-end">역량지수</th>
		                                    <td colspan="7" class="in_table" >
		                                    	<div class="essnal_table">
													<div class="nonsbjtess">
														<h3>핵심역량지수</h3>
														<canvas id="nonSbjtEssential" width="400" height="200"></canvas>
													</div>	
			                                    	<div class="useressential">
			                                    		<h3>나의 역량지수</h3>
			                                    		<canvas id="userEssential" width="400" height="200"></canvas>
							                      		<div class="empty_box d-flex mt-5 mb-5 flex-column w-100 align-items-center justify-content-center hidden" style="margin-top:8rem !important;">
							                            	<p class="text-center text-muted">
							                            		'핵심역량진단' 정보가 없습니다.<br>
							                            		『Ocean-CTS』<b>'핵심역량'</b>을 진단해주세요.<br>
							                            		<a class='go_oceancts_essential px-2' onclick="goOceanCts('https://cts.kmou.ac.kr/ko/diagnosis/essential')" title='Ocean-CTS' target='_blank'>바로가기</a>	
							                            	</p>
														</div>			                                    		
			                                    	</div>
												</div>																			
		                                    </td>
		                                </tr>
		
		                            </tbody>
		                        </table>
		                        <div class="nonsbjt_topbutton">
			                    	<button class="applicate" onclick="goOceanCts('https://cts.kmou.ac.kr/ko/program/application/view/${info.IDX}')">신청하기</button>
			                    </div>
		                    </section>                    
	                    </div>
                    </div>
                    
                    <h5 class="fw-bold nonsbjt_in_title">세부내용</h5>
                    <section class="table_wrap table_maj">
                    
                    <div class="my_applicatae_info">
							<h5 class="fw-bold mb-3">나의 신청 내역</h5>
							<div class="table_wrap">
								<table class="table">
									<caption class="blind">나의 신청 내역</caption>
									<colgroup>
										<col width="auto">
										<col width="25%">
										<col width="15%">
										<col width="15%">
									</colgroup>
									<thead>
										<tr>
											<th scope="row" class="text-center">프로그램</th>
											<th scope="row" class="text-center">프로그램 일정</th>
											<th scope="row" class="text-center">상태</th>
											<th scope="row" class="text-center">비고</th>
										</tr>
									</thead>
									<tbody>
									<c:if test="${!empty myHist}">
									<c:forEach var="myHist" items="${myHist}" varStatus="status" >
										<tr>
											<td class="text-center">${myHist.TITLE }</td>
											<td class="text-center">${myHist.START_DATE}(${myHist.START_DAY}) ${myHist.START_TIME } ~ ${myHist.END_DATE}(${myHist.END_DAY}) ${myHist.END_TIME }</td>
											<td class="text-center">${myHist.STATUS_NM } </td>
											<td class="text-center">${myHist.NOTE } </td>
										</tr>
									</c:forEach>
									</c:if>
									<c:if test="${empty myHist}">
										<tr>
											<td colspan="4" class="text-center">해당 비교과에 대한 신청이력이 없습니다</td>
										</tr>
									</c:if>
									</tbody>
								</table>
							</div>
						</div>
						
						
                       <div style="white-space: pre-wrap;">${contentText }</div>
						<%-- ${contentText } --%>
                       <br>
                       - 첨부파일
                       <c:forEach var="attachmentFile" items="${attachmentFileList}" varStatus="status" >
                           <a href="${attachmentFile.ATTACHMENT_URL }" download>${attachmentFile.NAME }</a> 
                       </c:forEach>
                       
						
													
						<div class="applicatae_detailinfo pt-4">
							<h5 class="fw-bold mb-3">상세일정</h5>
							<div class="table_wrap">
								<table class="table">
									<caption class="blind">상세일정</caption>
									<colgroup>
										<col width="auto">
										<col width="20%">
										<col width="20%">
									</colgroup>
									<thead>
										<tr>
											<th scope="row" class="text-center">프로그램 일정</th>
											<th scope="row" class="text-center">신청 기간</th>
											<th scope="row" class="text-center">신청 현황</th>
										</tr>
									</thead>
									<tbody>
									<c:forEach var="hist" items="${hist}" varStatus="status" >
										<c:choose>
										<c:when test="${hist.D_DAY eq '종료' }">
											<c:set var="onOff" value="off"/>
										</c:when>
										<c:otherwise>
											<c:set var="onOff" value="on"/>
										</c:otherwise>
										</c:choose>
										
										<tr class="${onOff}">
											<td>
												<label for="c1">
													${hist.TITLE}<br>${hist.START_DATE}(${hist.START_DAY}) ${hist.START_TIME} ~ ${hist.END_DATE}(${hist.END_DAY}) ${hist.END_TIME}
												</label>
											</td>
											<td>
												${hist.SIGNIN_START_DATE}(${hist.SIGNIN_START_DAY}) 부터<br>
												${hist.SIGNIN_END_DATE}(${hist.SIGNIN_END_DAY}) 까지
											</td>
											<td>
												<div class="<c:if test="${onOff eq 'off'}">off_box</c:if>">${hist.D_DAY}</div>
												${hist.PARTICIPANT }명 / ${hist.SIGNIN_LIMIT}명 <br>(최대 ${hist.SIGNIN_LIMIT}명 접수가능)
											</td>
										</tr>
									</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
						
					</section>
                </main>

                <a href="javascript:history.back(-1);" title="목록으로" class="back_to_list">
                    목록으로
                </a>
            </section>

        </section>
      
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>