<%@ include file="../../../../include/commonTop.jsp"%>
<link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_sampleInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/courMajorList.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
	<div id="cms_board_article">
	
		<jsp:include page="common/tabList.jsp" flush="false">
			<jsp:param name="tab" value="3"/>
			<jsp:param name="dt" value="${dt}"/>
		</jsp:include>	
		
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_COUR_MAJOR_INPUT_PROC}"/>" target="submit_target" enctype="multipart/form-data">
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
		
		<%-- 2. 디자인에 맞게 필요한 항목만 출력하는 경우 --%>
		<table class="tbWriteA"
			summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
				<caption>
					<c:out value="${settingInfo.list_title}" />
					목록
				</caption>
				
				<colgroup>
					<col width="15%">
					<col width="35%">
					<col width="15%">
					<col width="*">
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
					</tr>
				</tbody>
		</table>
		
		<div class="btnTopFull mb5" >
			<div class="right">
			<a href="#;" onclick="window.open('${URL_ADD_MAJOR}&dl=1', '_blank', 'width=1200, height=1200')"  class="btnTypeK fn_btn_write_open">
				교과목 등록
			</a>
			<input type=button value="조회" id="refresh" class="btnTypeI"">
			<input type="submit" class="btnTypeJ fn_btn_submit" value="저장" title="저장"/>
			</div>
		</div>
		
		<div style="padding-right:1%;">
		<table id="tpSubContent" class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
			  <col style="width: 5%;" />
			  <col style="width: 10%;" />
			  <col style="width: 10%;" />
			  <col style="width: 14%;" />
			  <col style="width: 14%;" />
			  <col  />
			  <col style="width: 7%;" />
			  <col style="width: 8%;" />
<!-- 			  <col style="width: 10%;" /> -->
			</colgroup>	
			<thead>
			<tr>
<%-- 				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th> --%>
				<th scope="col">No</th>
				<th scope="col">개설년도</th>
				<th scope="col">학기구분</th>
				<th scope="col">수강대상학년</th>
				<th scope="col">과목명</th>
				<th scope="col">*진출분야</th>
				<th scope="col">복사</th>
				<th scope="col">삭제</th>
<!-- 				<th scope="col">수정자</th> -->
<%-- 				<th scope="col"><itui:objectItemName itemId="sampleItemId" itemInfo="${itemInfo}"/></th> --%>
<%-- 				<th scope="col"><spring:message code="item.regidate.name"/></th> --%>
			</tr>
			</thead>
			</table>
			<div class="scrollable-tbody">
			<table id="tpSubContent" class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<colgroup>
			  <col style="width: 5%;" />
			  <col style="width: 10%;" />
			  <col style="width: 10%;" />
			  <col style="width: 14%;" />
			  <col style="width: 14%;" />
			  <col  />
			  <col style="width: 7%;" />
			  <col style="width: 8%;" />
			</colgroup>
			
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="8" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listMjCdIdxName" value="${settingInfo.idx_major_name}"/>
				<c:set var="listCourseNoIdxName" value="${settingInfo.idx_course_no_name}"/>
				<c:set var="listYearIdxName" value="${settingInfo.idx_year_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex + 1}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
					
					<c:if test="${i.last eq true }">
						<input type="hidden" id="dtLength" name="dtLength" value="${(i.index)}"/>
					</c:if>					
				<tr>
					<%-- <itui:objectText itemId="majorCd" itemInfo="${itemInfo}" objStyle="display:none"/> --%>
					<td class="num" id="num">${listNo}</td>
					<td>${listDt.YEAR }</td>  <!-- 개설년도 -->
					<td> <!-- 개설학기 -->
						<c:choose>
							<c:when test="${listDt.SMT == 'GH0210' }">1학기</c:when>
							<c:when test="${listDt.SMT == 'GH0211' }">여름계절학기</c:when>
							<c:when test="${listDt.SMT == 'GH0220' }">2학기</c:when>
							<c:when test="${listDt.SMT == 'GH0221' }">겨울계절학기</c:when>
						</c:choose>
					</td>  
					<c:set var="comdivCd" value="${listDt.COMDIV_CODE }"/> 
					<td>${listDt.GRADE }</td> <!-- 수강대상학년 -->
					<c:set var="grade" value="${listDt.GRADE }"/>					
					<c:set var="fieldCd" value="${listDt.FIELD_CD }"/>
					<c:set var="field" value="${listDt.FIELD }"/>
					<c:set var="year" value="${listDt.YEAR }"/>
					<c:set var="smt" value="${listDt.SMT }"/>
					<c:set var="cdtNum" value="${listDt.CDT_NUM}"/>					
					<c:set var="subjectNm" value="${listDt.SUBJECT_NM }"/>
					<c:set var="subjectCd" value="${listDt.SUBJECT_CD }"/>
					<c:set var="sinbunCd" value="${listDt.SINBUN_CODE }"/>
					<c:set var="openDept" value="${listDt.OPEN_DEPT }"/>					
					<%-- <td>${listDt.OPEN_DEPT}</td> --%>
					<td>${listDt.SUBJECT_NM} </td> <!-- 전공명 -->
					<td> <!-- 진출분야 -->
						<select name="field${i.index }" id="field${i.index }" class="field" style="width:60%;" title="진출분야">
						<c:forEach var="fieldDt" items="${fieldList}" varStatus="stat">
							<option value='${fieldDt.OPTION_CODE }' <c:if test="${listDt.FIELD_CD == fieldDt.OPTION_CODE }"> selected</c:if>>${fieldDt.OPTION_NAME } </option>
						</c:forEach>										
						</select>
					</td>
					
					<td><button type="button" id="addSub${i.index }" class="btnTypeE">복사</button></td>
					<td><button type="button" onclick=fun_delete_major(this); id="deleteSub" class="btnTypeF">삭제</button></td>
				
					<c:set var="lastModiId" value="${listDt.LAST_MODI_ID }"/>
					
				</tr>
				<c:set var="listNo" value="${listNo + 1}"/>
				<input type="hidden" name="comdivCd${i.index }" id="comdivCd${i.index }" value="${comdivCd }"/>
				<input type="hidden" name="grade${i.index }" id="grade${i.index }" value="${grade }"/>
				<input type="hidden" name="fieldNm${i.index }" id="fieldNm${i.index }" value="${field }"/>
				<input type="hidden" name="year${i.index }" id="year${i.index }" value="${year }"/>
				<input type="hidden" name="smt${i.index }" id="smt${i.index }" value="${smt }"/>
				<input type="hidden" name="cdtNum${i.index }" id="cdtNum${i.index }" value="${cdtNum }"/>
				<input type="hidden" name="subjectNm${i.index }" id="subjectNm${i.index }" value="${subjectNm }"/>
				<input type="hidden" name="subjectCd${i.index }" id="subjectCd${i.index }" value="${subjectCd }"/>
				<input type="hidden" name="openDept${i.index }" id="openDept${i.index }" value="${openDept }"/>
				
				<%-- <input type="hidden" name="shyrFg${i.index }" id="shyrFg${i.index }" value="${listDt.SHYR_FG }"/>
				<input type="hidden" name="sbjtNmKor${i.index }" id="sbjtNmKor${i.index }" value="${listDt.SBJT_NM_KOR}"/>
				<input type="hidden" name="courseNo${i.index }" id="courseNo${i.index }" value="${listDt.COURSE_NO }"/>
				<input type="hidden" name="lastModiId${i.index }" id="lastModiId${i.index }" value="${lastModiId }"/>
				<input type="hidden" name="lastModiDate${i.index }" id="lastModiDate${i.index }" value="${lastModiDate }"/> --%>
				
				<input type="hidden" name="docId${i.index }" id="docId${i.index }" value="${sbjtFgNm}+${openShyrNm}+${openSustMjNm }+${listDt.COURSE_NO }+${listDt.SPT_PSN_KOR}"/>
				
				</c:forEach>
				<c:choose>
					<c:when test="${!empty list}">
						<input type="hidden" name="majorRowLength" id="majorRowLength" value="${fn:length(list)+1 }">
					</c:when>
					<c:otherwise>
						<input type="hidden" name="majorRowLength" id="majorRowLength" value="1">
					</c:otherwise>
				</c:choose>
			</tbody>
			</table>
			</div>
  		</div>
		<div class="btnCenter">
			<!-- <input type="submit" class="btnTypeJ fn_btn_submit" value="저장" title="저장"/> -->
			<input type=button value="목록"  class="btnTypeI fn_btn_jobCancel">
		</div>
		</form>
	</div>
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if> --%>