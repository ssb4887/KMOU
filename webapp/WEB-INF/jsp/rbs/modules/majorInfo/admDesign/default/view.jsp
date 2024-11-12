<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
	<div id="cms_board_article">
		<%-- table summary, 항목출력에 사용 --%>
		<c:set var="exceptIdStr">제외할 항목id를 구분자(,)로 구분하여 입력(예:name,notice,subject,file,contents,listImg)</c:set>
		<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
		<%-- 
			table summary값 setting - 테이블 사용하지 않는 경우는 필요 없음
			디자인 문제로 제외한 항목(exceptIdStr에 추가했으나 table내에 추가되는 항목)은 수동으로 summary에 추가
			예시)
			<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
		--%>
		<c:set var="summary"><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/>을 제공하는 표</c:set>
		
		
		<%-- 2. 디자인에 맞게 필요한 항목만 출력하는 경우 --%>
		<table class="tbWriteA" summary="${summary}">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
<%-- 					<itui:itemText itemId="sampleItemId" itemInfo="${itemInfo}" colspan="3" objStyle="width:400px"/> --%>
					<th>소속</th>
					<td>
						<c:out value="${dt.COLG_NM_KOR}"/> > <c:out value="${dt.FCLT_SUST_NM_KOR}"/>							
						<c:if test="${dt.FCLT_SUST_CD ne dt.MJ_CD}">
							> <c:out value="${dt.MJ_NM_KOR}"/>
						</c:if>
						
						
						<itui:objectText itemId="mjCd" itemInfo="${itemInfo}" objStyle="display:none"/>
						<itui:objectText itemId="year" itemInfo="${itemInfo}" objStyle="display:none"/>
						
						<itui:objectText itemId="jhhh" itemInfo="${itemInfo}" objStyle="display:none"/>
						<itui:objectText itemId="colgNm" itemInfo="${itemInfo}" objStyle="display:none"/>
						<itui:objectText itemId="deptNm" itemInfo="${itemInfo}" objStyle="display:none"/>
						<itui:objectText itemId="connMjMngtHgCdNm" itemInfo="${itemInfo}" objStyle="display:none"/>
						<itui:objectText itemId="clsfCd" itemInfo="${itemInfo}" objStyle="display:none"/>
						<itui:objectText itemId="colgCd" itemInfo="${itemInfo}" objStyle="display:none"/>
						<itui:objectText itemId="deptCd" itemInfo="${itemInfo}" objStyle="display:none"/>
						<itui:objectText itemId="connMjMngtHgCd" itemInfo="${itemInfo}" objStyle="display:none"/>
					</td>
				</tr>
<!-- 				<tr id="yearbox" style=""> -->
				<tr>
					<c:choose>
						<c:when test="${empty dt}">
							<itui:itemYEAR itemId="year" itemInfo="${itemInfo}" />
						</c:when>
						<c:otherwise>
							<th><itui:objectLabel itemId="year" itemInfo="${itemInfo}" /></th>
							<td>${dt.YY}</td>
						</c:otherwise>
					</c:choose>
<%-- 					<itui:itemTextarea itemId="sampleItemId" itemInfo="${itemInfo}" colspan="3" objStyle="width:400px"/> --%>
				</tr>
				<tr>
					 <%-- <itui:itemTextarea itemId="educationGoal" itemInfo="${itemInfo}" objStyle="height:220px;"/> --%>
				</tr>
					<tr><itui:itemRadio itemId="useFg" itemInfo="${itemInfo}"/></tr>
					<tr><itui:itemTextarea itemId="mjIntroKor" itemInfo="${itemInfo}" objStyle="height:110px;"/></tr>
					<tr><itui:itemTextarea itemId="mjIntroEng" itemInfo="${itemInfo}" objStyle="height:110px;"/></tr>
					<tr><itui:itemTextarea itemId="grdtAfCarrKor" itemInfo="${itemInfo}" objStyle="height:160px;"/></tr>
					<tr><itui:itemTextarea itemId="grdtAfCarrEng" itemInfo="${itemInfo}" objStyle="height:160px;"/></tr>
					<tr><itui:itemTextarea itemId="concLicKor" itemInfo="${itemInfo}" objStyle="height:110px;"/></tr>
					<tr><itui:itemTextarea itemId="concLicEng" itemInfo="${itemInfo}" objStyle="height:110px;"/></tr>
					<tr><itui:itemTextarea itemId="idealStuKor" itemInfo="${itemInfo}" objStyle="height:110px;"/></tr>
					<tr><itui:itemTextarea itemId="idealStuEng" itemInfo="${itemInfo}" objStyle="height:110px;"/></tr>
			
					
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
		<div class="btnCenter">
			<c:if test="${empty dt}">
				<a class="btnCopy" href="<c:out value="${URL_COPY_PROC}"/>" title="복사" class="btnTD fn_btn_delete">복사</a>
			</c:if>
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<input type=button value="취소"  class="btnTypeB fn_btn_cancel">
		</div>
	</div>
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>