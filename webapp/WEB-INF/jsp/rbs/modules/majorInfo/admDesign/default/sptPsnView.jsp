<%@ include file="../../../../include/commonTop.jsp"%>
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
						<input type="hidden" name="mjCd" value="${dt.MJ_CD}">
						<input type="hidden" name="year" value="${dt.YY}">
						<c:out value="${dt.COLG_NM_KOR}"/> > <c:out value="${dt.FCLT_SUST_NM_KOR}"/>							
						<c:if test="${dt.FCLT_SUST_CD ne dt.MJ_CD}">
							> <c:out value="${dt.MJ_NM_KOR}"/>
						</c:if>
					</td>
					<th>학년도</th>
					<td>${dt.YY}</td>
				</tr>			
			</tbody>
		</table>
		<br>
		<div>
		<table id="tpSubContent" class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
			  <col style="width: 5%;" />
			  <col style="width: 5%;" />
			  <col style="width: 8%;" />
			  <col style="width: 8%;" />
			  <col style="width: 15%;" />
			  <col style="width: 14%;" />
			  <col style="width: 13%;" />
			  <col style="width: 12%;" />
			  <col style="width: 10%;" />
			  <col style="width: 10%;" />
			</colgroup>	
			<thead>
			<tr>
<%-- 				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th> --%>
				<th scope="col">No</th>
				<th scope="col">교과구분</th>
				<th scope="col">개설학년</th>
				<th scope="col">학기구분</th>
				<th scope="col">개설학과전공명</th>
				<th scope="col">과목명</th>
				<th scope="col">*인재상</th>
				<th scope="col" style="padding-left:40px;">복사</th>
				<th scope="col">삭제</th>
				<th scope="col">수정자</th>
<%-- 				<th scope="col"><itui:objectItemName itemId="sampleItemId" itemInfo="${itemInfo}"/></th> --%>
<%-- 				<th scope="col"><spring:message code="item.regidate.name"/></th> --%>
			</tr>
			</thead>
			</table>
			<div class="scrollable-tbody">
			<table id="tpSubContent" class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<colgroup>
			  <col style="width: 5%;" />
			  <col style="width: 5%;" />
			  <col style="width: 9%;" />
			  <col style="width: 9%;" />
			  <col style="width: 14%;" />
			  <col style="width: 14%;" />
			  <col style="width: 14%;" />
			  <col style="width: 10%;" />
			  <col style="width: 10%;" />
			  <col style="width: 10%;" />
			</colgroup>
			
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="7" class="bllist"><spring:message code="message.no.list"/></td>
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
					<itui:objectText itemId="mjCd" itemInfo="${itemInfo}" objStyle="display:none"/>
					<itui:objectText itemId="year" itemInfo="${itemInfo}" objStyle="display:none"/>
<%-- 					<td><c:if test="${mngAuth || wrtAuth && listDt.AUTH_MNG == '1'}"><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></c:if></td> --%>
					
					<td class="num" id="num">${listNo}</td>
					<%-- <td><a href="<c:out value="${URL_TRACK_INPUT}"/>&mode=m&${listMajorIdxName}=${listDt.MAJOR_IDX}&${listYearIdxName}=${listDt.YEAR_IDX}&${listSptPsnIdxName}=${listDt.SPT_PSN_KOR}" >수정</a></td> --%>
					<td><itui:objectView itemId="sbjtFgNm" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<c:set var="sbjtFgNm" value="${listDt.SBJT_FG_NM }"/>
					<td><itui:objectView itemId="openShyrNm" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<c:set var="openShyrNm" value="${listDt.OPEN_SHYR_NM }"/>
					<td>${listDt.SHTM_NM }</td>
					<c:set var="shtmNm" value="${listDt.SHTM_NM }"/>
					<c:set var="shtmCd" value="${listDt.SHTM_CD }"/>					
					<td><itui:objectView itemId="openSustMjNm" itemInfo="${itemInfo}" objDt="${listDt}" /></td>
					<c:set var="openSustMjNm" value="${listDt.OPEN_SUST_MJ_NM }"/>
					<td>

					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213002'}">					
						${listDt.SBJT_KOR_NM_1 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_1 }"/>
					</c:if>
					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213003'}">					
						${listDt.SBJT_KOR_NM_2 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_2 }"/>
					</c:if>
					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213004'}">					
						${listDt.SBJT_KOR_NM_1 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_1 }"/>
					</c:if>
					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213005'}">					
						${listDt.SBJT_KOR_NM_3 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_3 }"/>
					</c:if>
					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213007'}">					
						${listDt.SBJT_KOR_NM_4 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_4 }"/>
					</c:if>
					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213008'}">					
						${listDt.SBJT_KOR_NM_3 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_3 }"/>
					</c:if>
					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213009'}">					
						${listDt.SBJT_KOR_NM_2 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_2 }"/>
					</c:if>
					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213010'}">					
						${listDt.SBJT_KOR_NM_5 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_5 }"/>
					</c:if>
					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213011'}">					
						${listDt.SBJT_KOR_NM_4 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_4 }"/>
					</c:if>
					<c:if test="${listDt.OPEN_SHYR_FG eq 'U0213012'}">					
						${listDt.SBJT_KOR_NM_3 }
						<c:set var="sbjtNmKor" value="${listDt.SBJT_KOR_NM_3 }"/>
					</c:if>					
					
					</td>
					<%-- <td><itui:itemText itemId="sptPsnKor" itemInfo="${itemInfo}" objDt="${listDt}"/></td> --%>
					<%-- <td><textarea name="sptPsnKor${i.index }"><c:out value="${listDt.SPT_PSN_KOR}"></c:out></textarea></td> --%>
					<%-- <td><textarea name="sptPsnKor${i.index }" id="sptPsnKor${i.index }"><c:out value="${listDt.SPT_PSN_KOR}"></c:out></textarea></td> --%>
											
					<input type="hidden" name="" id="sptPsnKor${i.index }" value="${listDt.SPT_PSN_KOR}">
					
					<td>
						<select name="is_sptPsnKor${i.index }" id="s_sptPsnKor${i.index }" class="select" title="인재상">							
							<option value="">전체</option>
						</select>
					</td>
					
					<td><button type="button" id="addSub${i.index }" class="btnTypeA">복사</button></td>
					<td><button type="button" onclick=fun_delete_major(this); id="deleteSub" class="btnTypeB">삭제</button></td>
					<c:choose>
						<c:when test="${!empty list}">
							<input type="hidden" name="majorRowLength" id="majorRowLength" value="${fn:length(list)+1 }">
						</c:when>
						<c:otherwise>
							<input type="hidden" name="majorRowLength" id="majorRowLength" value="1">
						</c:otherwise>
					</c:choose>
					
					<td><itui:objectView itemId="lastModiId" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<c:set var="lastModiId" value="${listDt.LAST_MODI_ID }"/>
					<%-- <c:set var="lastModiDate" value="${listDt.LAST_MODI_DATE }"/> --%>
					
				</tr>
				<c:set var="listNo" value="${listNo + 1}"/>
				<input type="hidden" name="sbjtFgNm${i.index }" id="sbjtFgNm${i.index }" value="${sbjtFgNm }"/>
				<input type="hidden" name="openShyrNm${i.index }" id="openShyrNm${i.index }" value="${openShyrNm }"/>
				<input type="hidden" name="shtmNm${i.index }" id="shtmNm${i.index }" value="${shtmNm }"/>
				<input type="hidden" name="shtmCd${i.index }" id="shtmCd${i.index }" value="${shtmCd }"/>
				<input type="hidden" name="openSustMjNm${i.index }" id="openSustMjNm${i.index }" value="${openSustMjNm }"/>
				
				<input type="hidden" name="sbjtFg${i.index }" id="sbjtFg${i.index }" value="${listDt.SBJT_FG }"/>
				<input type="hidden" name="openShyrFg${i.index }" id="openShyrFg${i.index }" value="${listDt.OPEN_SHYR_FG }"/>
				<input type="hidden" name="sbjtNmKor${i.index }" id="sbjtNmKor${i.index }" value="${sbjtNmKor}"/>
				<input type="hidden" name="courseNo${i.index }" id="courseNo${i.index }" value="${listDt.COURSE_NO }"/>
				<input type="hidden" name="lastModiId${i.index }" id="lastModiId${i.index }" value="${lastModiId }"/>
				<input type="hidden" name="lastModiDate${i.index }" id="lastModiDate${i.index }" value="${lastModiDate }"/>
				
				<input type="hidden" name="docId${i.index }" id="docId${i.index }" value="${sbjtFgNm}+${openShyrNm}+${openSustMjNm }+${listDt.COURSE_NO }+${listDt.SPT_PSN_KOR}"/>
				
				</c:forEach>
			</tbody>
			</table>
			</div>
  		</div>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<input type=button value="취소"  class="btnTypeB fn_btn_jobCancel">
		</div>
		</form>
	</div>
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if> --%>