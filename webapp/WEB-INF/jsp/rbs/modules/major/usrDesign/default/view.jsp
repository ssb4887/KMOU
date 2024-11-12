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
	<div class="sub_background major_bg">
                <section class="inner">
                    <h3 class="title fw-bolder text-center text-white">전공</h3>
                </section>
            </div>
            <!--본문-->
            <section class="inner mt-5">
                <!--item 상세-->
                <section class="detail_title_wrap">
                    <section class="d-flex flex-row justify-content-between align-items-center title_box px-2 py-3 mb-5">
                        <a href="javascript:window.history.back();" title="이전페이지" class="d-flex flex-row align-items-center gap-2"><img src="../images/arr_blue.png"/><em class="fst-normal">이전페이지</em></a>
                        <h5 class="content_title text-center fw-bolder d-flex align-items-center justify-content-center gap-1 flex-wrap">
                            <span class="col-12 col-md-auto text-truncate">${majorInfo.MAJOR_NM_KOR}</span>(${majorInfo.MAJOR_NM_ENG})
                        </h5>
                        <div id="${majorInfo.MAJOR_CD}" class="like_container">
                            <div class="link_cnt text-end">
                                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange('${majorInfo.MAJOR_CD}','major');">
                                <path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>
                                <path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>
                            </div>
                        </div>
                    </section>
				</section>
				
                <main class="majref_dtl_wrap">
               		
                    <section class="table_wrap table_maj inbx1">
                    	<h5 class="fw-semibold mb-3">${majorInfo.MAJOR_NM_KOR} 소개</h5>
                    	<div class="item">
		  					<b><span>${majorInfo.COLG_NM}</span> > <span>${majorInfo.DEPT_NM}</span></b>
	  					</div>
						${majorInfo.MAJOR_INTRO}
                    </section>
                    
                    
                    <h5 class="fw-semibold mb-3">교육목표</h5>
                    <section class="table_wrap table_maj inbx">
						${majorInfo.GOAL}
                    </section>
                    

                    <h5 class="fw-semibold mb-3">인재상</h5> 
                    <section class="table_wrap table_maj">
                        <table class="table">
                            <colgroup>
                                <col width="30%">
                                <col width="70%">
                            </colgroup>
                            <thead>
								<tr>
									<th scope="col" class="text-center border-end p-3">인재상</th>
									<th scope="col" class="text-center border-end p-3">전공능력</th>
								</tr>
							</thead>
                            <tbody>
                            	<c:forEach var="listDt" items="${majorTalent }">
                               	<tr class="">
                                    <th scope="row" class="text-center border-end">${listDt.TALENT}</th>
                                    <td class="border-end">${listDt.MAJOR_ABTY}</td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        
                        <h5 class="fw-semibold mb-3 mt-5">전공능력</h5> 
                        
                        
                        <!-- 헤더부분 추후 결정에 따라서 수정 -->
                        <table class="table">
                            <colgroup>
                            	<c:choose>
                            		<c:when test="${checkAbty ne 0 }">
	                            		<col width="15%">
		                                <col width="35%">
		                                <col width="15%">
		                                <col width="35%">
                            		</c:when>
                            		<c:otherwise>
                            			<col width="30%">
		                                <col width="70%">
                            		</c:otherwise>
                            	</c:choose>
                            	
                                
                            </colgroup>
                            <thead>
								<tr>
									<c:choose>
	                            		<c:when test="${checkAbty ne 0 }">
		                            		<th scope="col" class="text-center border-end p-3">전공능력</th>
											<th scope="col" class="text-center border-end p-3">전공능력 정의</th>
											<th scope="col" class="text-center border-end p-3">하위역량</th>
											<th scope="col" class="text-center border-end p-3">하위역량 정의</th>
	                            		</c:when>
	                            		<c:otherwise>
	                            			<th scope="col" class="text-center border-end p-3">전공능력</th>
											<th scope="col" class="text-center border-end p-3">전공능력 정의</th>
	                            		</c:otherwise>
	                            	</c:choose>
								</tr>
							</thead>
                            <tbody>
                            	<c:forEach var="listDt" items="${majorAbty }" varStatus="i">
                            	<c:set var="chkCord1" value="${listDt.PARENT_ABTY_CD}"/>
                            	<c:set var="chkCord2" value="${majorAbty[i.index-1].PARENT_ABTY_CD}"/>
                            	<tr class="">
	                            	<c:if test="${i.index eq 0 || chkCord1 ne chkCord2 }">
	                            		<th scope="rowgroup"
	                            			<c:if test="${listDt.CHILD_COUNT ne 0}">rowspan="${listDt.CHILD_COUNT }"</c:if>
											class="px-3 text-center align-middle border-end">
											${listDt.PARENT_MAJOR_ABTY }</th>
										<td class="border-end" <c:if test="${ listDt.CHILD_COUNT ne 0}">rowspan="${listDt.CHILD_COUNT }"</c:if>>${listDt.PARENT_MAJOR_ABTY_DEFN}</td>
	                            	</c:if>
                               	<c:if test="${!empty listDt.CHILD_ORD  }"></c:if>
                                    <td class="text-center border-end">${listDt.CHILD_MAJOR_ABTY}</td>
                                    <td class="border-end">${listDt.CHILD_MAJOR_ABTY_DEFN}</td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </section>
                    
                    <h5 class="fw-semibold mb-3">학부(과) 교육과정</h5>
                    <section class="table_wrap table_maj">
                    	<table class="curricT table d-none d-xxl-table ">
	                        <caption class="blind">학부(과)교육과정</caption>
	                        <colgroup>
	                            <col width="15%"/>
	                            <col width="auto"/>
	                            <col width="auto"/>
	                            <col width="auto"/>
	                            <col width="auto"/>
	                            <col width="auto"/>
	                            <col width="auto"/>
	                            <col width="auto"/>
	                            <col width="auto"/>
	                        </colgroup>
	                        <thead>
	                            <tr>
	                                <th rowspan="2" scope="row" class="text-center align-middle border-end">분야</th>
	                                <th colspan="2" scope="rowgroup" class="text-center align-middle border-end">1학년</th>
	                                <th colspan="2" scope="rowgroup" class="text-center align-middle border-end">2학년</th>
	                                <th colspan="2" scope="rowgroup" class="text-center align-middle border-end">3학년</th>
	                                <th colspan="2" scope="rowgroup" class="text-center align-middle border-end">4학년</th>
	                                <th colspan="2" scope="rowgroup" class="text-center align-middle border-end">비교과</th>
	                                <th colspan="2" scope="rowgroup" class="text-center align-middle">자격증</th>
	                            </tr>
	                        </thead>
	                            <!-- ass ::::클래스 추가시 전공필수-->
							<tbody>
								<c:forEach var="listDt" items="${majorSbjt}" varStatus="i">
									<tr>
										<th class="px-3 text-center align-middle border-end" scope="col" colspan="1" rowspan="1">${listDt.FIELD }</th>																	
										<td class="px-2" colspan="2" id="Caa">${elfn:replaceHtmlY(listDt.GRADE_1) }</td>
										<td class="px-2" colspan="2" id="Cab">${elfn:replaceHtmlY(listDt.GRADE_2) }</td>
										<td class="px-2" colspan="2" id="Cba">${elfn:replaceHtmlY(listDt.GRADE_3) }</td>
										<td class="px-2" colspan="2" id="Cbb">${elfn:replaceHtmlY(listDt.GRADE_4) }</td>
										<td class="px-2" colspan="2" id="Cca">${elfn:replaceHtmlY(listDt.NON_SBJT_NM) }</td>
										<td class="px-2" colspan="2" id="Ccb">${elfn:replaceHtmlY(listDt.LICENSE_NM) }</td>
									</tr>
								</c:forEach>
							</tbody>
	                    </table>
                    </section>
                    
                    <h5 class="fw-semibold mb-3">졸업 후 진로</h5>
                    <section class="table_wrap table_maj inbx">
						${majorInfo.GRDT_AF_CARR}
                    </section>
                </main>

                <div class="mt-5"><a href="javascript:window.history.back();" title="목록으로" class="back_to_list">
                    목록으로
                </a>
                </div>
            </section>
        </div>
    </div>
    
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>