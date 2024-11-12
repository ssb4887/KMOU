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

		
		
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_DEPT_INPUT_PROC}"/>&mode=m" target="submit_target" enctype="multipart/form-data">
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
	                                      
								<!-- <input type="hidden" id="colgCd" name="colgCd" value=""/>
								<input type="hidden" id="colgNm" name="colgNm" value=""/>
								<input type="hidden" id="deptCd" name="deptCd" value=""/>
								<input type="hidden" id="deptNm" name="deptNm" value=""/>
								<input type="hidden" id="majorCd" name="majorCd" value=""/>
								<input type="hidden" id="majorNmKor" name="majorNmKor" value=""/>
								<input type="hidden" id="majorNmEng" name="majorNmEng" value=""/> -->
							</c:when>
							<c:otherwise>
								<itui:objectText itemId="majorCd" itemInfo="${itemInfo}" objStyle="display:none"/>
								<c:choose>
									<c:when test="${dt.DEPT_LEVEL == '2' }">
										<c:out value="${dt.COLG_NM}"/>		
									</c:when>
									<c:when test="${dt.DEPT_LEVEL == '3' }">
										<c:out value="${dt.COLG_NM}"/> > <c:out value="${dt.DEPT_NM}"/>		
									</c:when>
									<c:when test="${dt.DEPT_LEVEL == '4' }">
										<c:out value="${dt.COLG_NM}"/> > <c:out value="${dt.UP_NM}"/>	> <c:out value="${dt.DEPT_NM}"/>		
									</c:when>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</td>
					
				</tr>
				<input type="hidden" id="deptCd" name="deptCd" value="${dt.DEPT_CD }"/>
				<input type="hidden" id="deptLevel" name="deptLevel" value="${dt.DEPT_LEVEL }"/>
					<c:choose>
						<c:when test="${submitType eq 'writeDept' }">
							<tr><itui:itemRadio itemId="useFg" itemInfo="${itemInfo}"  colspan="3"/></tr>
						</c:when>
						<c:otherwise>
							<input type="hidden" id="useFg" name="useFg" value="1"/>
						</c:otherwise>
					</c:choose>					
					
			</tbody>
		</table>
		<div>
		
		</div>
		<div class="btnCenter">
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