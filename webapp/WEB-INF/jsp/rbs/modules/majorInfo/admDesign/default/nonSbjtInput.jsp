<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_sampleInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/abilityInput.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
	<div id="cms_board_article">
	
		<jsp:include page="common/tabList.jsp" flush="false">
			<jsp:param name="tab" value="4"/>
			<jsp:param name="dt" value="${dt}"/>
		</jsp:include>	
		
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_NON_SBJT_INPUT_PROC}"/>" target="submit_target" enctype="multipart/form-data">
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
		
		<div class="btnbx_right">
			<input type=button value="조회" id="refresh" class="btnTypeI"">
			<input type="submit" class="btnTypeJ fn_btn_submit" value="저장" title="저장"/>
		</div>		
		
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
					<th>소속</th>
					<td>
						<input type="hidden" name="majorCd" value="${dt.MAJOR_CD}">
						<input type="hidden" name="year" value="${dt.YEAR}">
						<c:out value="${dt.COLG_NM}"/> > <c:out value="${dt.DEPT_NM}"/>							
						<c:if test="${dt.DEPT_CD ne dt.MAJOR_CD}">
							> <c:out value="${dt.MAJOR_NM_KOR}"/>
						</c:if>
					</td>
					<%-- <th>학년도</th>
					<td>${dt.YEAR}</td> --%>
				</tr>			
			</tbody>
		</table>
		<br>
		<div>
			<table id="tpSubContent" class="tbListA" >
				<colgroup>	
					<col style="width:2.5%">	
					<col style="width:15%">		
					<col style="width:30%">
				</colgroup>
				<thead>
			        <tr>
			            <th scope="row">No</th>          
			            <th scope="row"><strong>*</strong> 진출분야</th>
			            <th scope="row"><strong>*</strong> 비교과 프로그램</th>
			        </tr>		
				</thead>
				<tbody class="alignC">
					<c:choose>
						<c:when test="${empty list}">
							<c:forEach var="listDt" items="${code}" varStatus="i">
			                	<tr>
			                		<td><input type="hidden" value="${i.count}" name="ord${i.index}" id="ord${i.count}" class="w100"><span id="ord">${i.count}</span></td>
			                		<td>
			                			<input type="hidden" value="${listDt.OPTION_CODE }" name="fieldCd${i.index}" id="fieldCd${i.count}" class="w100">
			                			<input type="hidden" value="${listDt.OPTION_NAME }" name="field${i.index}" id="field${i.count}" class="w100">
			                			<span id="field">${listDt.OPTION_NAME }</span>
			                		</td>
			                		<td>
			                			<textarea placeholder="비교과 프로그램" class="inputTxt" name="nonSbjtNm${i.index}" id="nonSbjtNm${(i.index)+1}"></textarea>
									</td>
									<c:if test="${i.last == true }"><input type="hidden" value="${i.count }" name="fieldRowLength"></c:if>
			                	</tr>
			               	</c:forEach>
						</c:when>
						<c:otherwise>
			               	<c:forEach var="listDt" items="${list}" varStatus="i">
			               		<input type="hidden" value="${list[i.index].REGI_DATE }" name="regiDate${i.index }">
			               		<input type="hidden" value="${list[i.index].REGI_ID }" name="regiId${i.index }">
			               		<input type="hidden" value="${list[i.index].REGI_IP }" name="regiIp${i.index }">
			               		<tr>
			                		<td><input type="hidden" value="${i.count}" name="ord${i.index}" id="ord${i.count}" class="w100"><span id="ord">${i.count}</span></td>
			                		<td>
			                			<input type="hidden" value="${listDt.FIELD_CD }" name="fieldCd${i.index}" id="fieldCd${i.count}" class="w100">
			                			<input type="hidden" value="${listDt.FIELD }" name="field${i.index}" id="field${i.count}" class="w100">
			                			<span id="field">${listDt.FIELD }</span>
			                		</td>
			                		<td>
			                			<textarea placeholder="비교과 프로그램" class="inputTxt" name="nonSbjtNm${i.index}" id="nonSbjtNm${(i.index)+1}" >${listDt.NON_SBJT_NM}</textarea>
									</td>
									<c:if test="${i.last == true }"><input type="hidden" value="${i.count }" name="fieldRowLength"></c:if>
			                	</tr>
			               	</c:forEach>
						</c:otherwise>			
					</c:choose>
					
					
					
				</tbody>
			</table>

  		</div>
		<div class="btnCenter">
			<input type=button value="목록"  class="btnTypeB fn_btn_jobCancel">			
		</div>
		</form>
	</div>
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if> --%>