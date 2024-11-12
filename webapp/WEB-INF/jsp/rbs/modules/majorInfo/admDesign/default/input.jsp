<%@ include file="../../../../include/commonTop.jsp"%>
<%-- <%@ include file="../../../../include/commonEnc.jsp"%> --%>
<link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_sampleInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>

	<div id="cms_board_article">

		<div>
		<jsp:include page="common/tabList.jsp" flush="false">
			<jsp:param name="tab" value="1"/>
			<jsp:param name="dt" value="${dt}"/>
		</jsp:include>
		</div>
		
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_INPUT_PROC}"/>" target="submit_target" enctype="multipart/form-data">
		<%-- table summary, 항목출력에 사용 --%>
		<c:set var="exceptIdStr">제외할 항목id를 구분자(,)로 구분하여 입력(예:name,notice,subject,file,contents,listImg)</c:set>
		<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
		<%-- 
			table summary값 setting - 테이블 사용하지 않는 경우는 필요 없음
			디자인 문제로 제외한 항목(exceptIdStr에 추가했으나 table내에 추가되는 항목)은 수동으로 summary에 추가
			예시)
			<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
		--%>
		<c:set var="summary"><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/> 입력표</c:set>
		
		<div class="tabel_topbx">
			<div class="btnLeft">
			<c:if test="${param.mode eq 'm'}">
				<%-- <c:choose>
					<c:when test="${dt.RESPR_CFM_YN eq 'Y' }">
						상태: <span style="font-size:20px; color:#333; padding-left:3px; display:inline-block;"><b>담당자 확인완료</b></span>
					</c:when>
					<c:otherwise>
						상태: <span style="font-size:20px; color:#F15A38;"><b>담당자 미확인</b></span>
					</c:otherwise>
				</c:choose>	 --%>		
			</c:if>
			</div>
			<div class="btnbx_right">			
				<input type=button value="조회" id="refresh" class="btnTypeI" style="float:right;">
				<input type="submit" class="btnTypeJ fn_btn_submit" value="저장" title="저장" style="float:right; margin-right:5px;"/>
			</div>
		</div>
				
		
		<%-- 2. 디자인에 맞게 필요한 항목만 출력하는 경우 --%>
		<table class="tbWriteA" summary="${summary}">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:10%;" />
			<col style="width:40%;" />
			<col style="width:10%;"/>
			<col style="width:40%;"/>
			</colgroup>
			<tbody>
				<tr>
<%-- 					<itui:itemText itemId="sampleItemId" itemInfo="${itemInfo}" colspan="3" objStyle="width:400px"/> --%>
					<th><span style="color:red;">*</span> 소속</th>
					<td>
						<c:choose>
							<c:when test="${empty dt}">
                                <select class="form-select" id="college1">
                                	<option value="" selected>대학</option>
                                 <c:forEach var="listDt" items="${collegeList}">
                                 	<option value="${listDt.COLG_CD}" <c:if test="${param.univ == listDt.COLG_CD}">selected</c:if>>${listDt.COLG_NM}</option>
                                 </c:forEach>
                                </select>
                            
                                <select class="form-select" id="college2">
                                    <option value="" selected>학부(과)</option>
                                </select>
                            
                                <select class="form-select" id="college3">
                                    <option value="" selected>전공</option>
                                </select>
	                                       
								<%--<c:choose>
									<c:when test="${loginVO.usertypeIdx ne '46'}">
									<select name="colgCd" id="s_colgCd" class="select" title="대학">
										<option value="">전체</option>
									</select>
									<select name="fcltSustCd" id="s_fcltSustCd" class="select" title="학부/학과">
										<option value="">전체</option>
									</select>
									<select name="mjCd" id="s_mjCd" class="select" title="전공">
										<option value="">전체</option>
									</select>
									</c:when>
									 <c:otherwise>
									<itui:objectText itemId="colgCd" itemInfo="${itemInfo}" objVal="${loginVO.colgCd }" objStyle="display:none"/>
									<itui:objectText itemId="fcltSustCd" itemInfo="${itemInfo}" objVal="${loginVO.sustCd }" objStyle="display:none"/>
									
									<c:out value="${loginVO.colgNm}"/> > <c:out value="${loginVO.sustNm}"/>							
									<c:if test="${loginVO.sustCd ne loginVO.mjCd}">
										> <c:out value="${loginVO.mjNm}"/>
									    <itui:objectText itemId="mjCd" itemInfo="${itemInfo}" objVal="${loginVO.deptCd }" objStyle="display:none"/>
									</c:if>		
									<c:if test="${loginVO.sustCd eq loginVO.mjCd}">
										> 									
										<select name="mjCd" id="s_mjCd" class="select" title="전공">
										<option value="">전체</option>
									</select>
									</c:if>										
									</c:otherwise> 
								</c:choose> --%>
								<input type="hidden" id="colgCd" name="colgCd" value=""/>
								<input type="hidden" id="colgNm" name="colgNm" value=""/>
								<input type="hidden" id="deptCd" name="deptCd" value=""/>
								<input type="hidden" id="deptNm" name="deptNm" value=""/>
								<input type="hidden" id="majorCd" name="majorCd" value=""/>
								<input type="hidden" id="majorNmKor" name="majorNmKor" value=""/>
								<input type="hidden" id="majorNmEng" name="majorNmEng" value=""/>
							</c:when>
							<c:otherwise>
								<itui:objectText itemId="majorCd" itemInfo="${itemInfo}" objStyle="display:none"/>
								<itui:objectText itemId="year" itemInfo="${itemInfo}" objStyle="display:none"/>
								<c:out value="${dt.COLG_NM}"/> > <c:out value="${dt.DEPT_NM}"/>							
								<c:if test="${dt.DEPT_CD ne dt.MAJOR_CD}">
									> <c:out value="${dt.MAJOR_NM_KOR}"/>
								</c:if>
							</c:otherwise>
						</c:choose>
					</td><%--
					<c:choose>
						<c:when test="${empty dt}">
							<span id="yearbox" style="<c:if test='${empty dt}'>display:none;</c:if>"><itui:itemYEAR itemId="year" itemInfo="${itemInfo}" /></span>
						</c:when>
						<c:otherwise>
								<th>학년도</th> 
								<td>${dt.YEAR}</td>					
						</c:otherwise>
					</c:choose>
					 --%>	
				</tr>
					<c:choose>
						<c:when test="${submitType eq 'modify' }">
							<tr><itui:itemRadio itemId="useFg" itemInfo="${itemInfo}"  colspan="3"/></tr>
						</c:when>
						<c:otherwise>
							<input type="hidden" id="useFg" name="useFg" value="1"/>
						</c:otherwise>
					</c:choose>					
					
					<tr><itui:itemTextarea itemId="majorIntro" itemInfo="${itemInfo}" objStyle="height:100px; width:100%;" colspan="3"/></tr>
					<tr><itui:itemTextarea itemId="goal" itemInfo="${itemInfo}" objStyle="height:100px; width:100%;" colspan="3"/></tr>
					<tr><itui:itemTextarea itemId="grdtAfCarr" itemInfo="${itemInfo}" objStyle="height:100px; width:100%;" colspan="3"/></tr>

					

					<%-- <tr><itui:itemTextarea itemId="idealStuKor" itemInfo="${itemInfo}" objStyle="height:100px; width:100%; border:1px solid #b5b5b5; background:#ffffff;" colspan="3"/></tr> --%>

			
					
					<tr class="nonMj" style="<c:if test='${!empty dt.CONN_MJ_MNGT_HG_CD}'></c:if>" >
						<itui:itemTextarea itemId="jobCompany" itemInfo="${itemInfo}" objStyle="height:50px;"/>
						<itui:itemCheckbox itemId="sampleItemId" itemInfo="${itemInfo}" colspan="3"/>
					</tr>
					<tr class="nonMj" style="<c:if test='${!empty dt.CONN_MJ_MNGT_HG_CD}'></c:if>" >
						<%-- <th>진학 현황</th>
						<td>
							<table style="table-layout:fixed;">
							<colgroup>
							<col/>
							<col/>
							<col/>
							<col/>
							<col/>
							<col/>
							<col/>
							<col/>
							</colgroup>
							<tbody>
								<tr>
									<td style="text-align:center;"><itui:objectLabel itemId="graduationCnt" required="0" itemInfo="${itemInfo}"/></td>
									<td style="text-align:center;"><itui:objectLabel itemId="jobCnt" required="0" itemInfo="${itemInfo}"/></td>
									<td style="text-align:center;"><itui:objectLabel itemId="insuranceCnt" required="0" itemInfo="${itemInfo}"/></td>
									<td style="text-align:center;"><itui:objectLabel itemId="collegeCnt" required="0" itemInfo="${itemInfo}"/></td>
									<td style="text-align:center;"><itui:objectLabel itemId="job1KeepRate" required="0" itemInfo="${itemInfo}"/></td>
									<td style="text-align:center;"><itui:objectLabel itemId="job4KeepRate" required="0" itemInfo="${itemInfo}"/></td>
									<td style="text-align:center;"><itui:objectLabel itemId="jobRate" required="0" itemInfo="${itemInfo}"/></td>
									<td style="text-align:center;"><itui:objectLabel itemId="graduationRate" required="0" itemInfo="${itemInfo}"/></td>
								</tr>
								<tr>
									<td><itui:objectText itemId="graduationCnt" itemInfo="${itemInfo}"/></td>
									<td><itui:objectText itemId="jobCnt" itemInfo="${itemInfo}"/></td>
									<td><itui:objectText itemId="insuranceCnt" itemInfo="${itemInfo}"/></td>
									<td><itui:objectText itemId="collegeCnt" itemInfo="${itemInfo}"/></td>
									<td><itui:objectText itemId="job1KeepRate" itemInfo="${itemInfo}"/></td>
									<td><itui:objectText itemId="job4KeepRate" itemInfo="${itemInfo}"/></td>
									<td>
										<c:choose>
											<c:when test="${empty dt}">
												<itui:objectText itemId="jobRate" itemInfo="${itemInfo}" objAttr="disabled"/>
											</c:when>
											<c:when test="${!empty dt && empty dt.CONN_MJ_MNGT_HG_CD}">
												<itui:objectText itemId="jobRate" objVal="${Math.round(dt.INSURANCE_CNT / dt.JOB_CNT * 1000.0 ) /10}" itemInfo="${itemInfo}" objAttr="disabled"/>
											</c:when>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${empty dt}">
												<itui:objectText itemId="graduationRate" itemInfo="${itemInfo}" objAttr="disabled"/>
											</c:when>
											<c:when test="${!empty dt && empty dt.CONN_MJ_MNGT_HG_CD}">
												<itui:objectText itemId="graduationRate" objVal="${Math.round(dt.COLLEGE_CNT / dt.GRADUATION_CNT *1000.0 ) /10}" itemInfo="${itemInfo}" objAttr="disabled"/>
											</c:when>
										</c:choose>
									</td>
								</tr>
							</tbody>
							</table>
						</td> --%>
					</tr>
					<tr class="nonMj" style="<c:if test='${!empty dt.CONN_MJ_MNGT_HG_CD}'></c:if>" >
						<%-- <th>파일</th>
						<td>
							<div class="filebox">
								<input class="upload-name" value="${dt.FILE_ORIGIN_NAME}" placeholder="첨부파일">
								<label for="file">파일찾기</label> 
	    					</div>
    					</td> --%>
					</tr>
					<tr style="display:none">
						<%-- <%-- <th></th>
						<td>
							<input type="file" name="file" value="" title="파일첨부" onchange="changeValue(this)" id="file">
						</td>
						<itui:itemFile itemId="file" itemInfo="${itemInfo}"/> --%>
					</tr>
			</tbody>
		</table>
		<div>
		
		</div>
		<div class="btnCenter">
<!-- TODO -->
<%-- 			<c:if test="${empty dt}"> --%>
<%-- 				<a class="btnCopy" href="<c:out value="${URL_COPY_PROC}"/>" title="불러오기" class="btnTD fn_btn_delete">복사</a> --%>
<%-- 			</c:if>			 --%>
<!-- TODO -->			
			<input type=button value="목록"  class="btnTypeB fn_btn_cancel">
		</div>
		</form>
	</div>
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if> --%>