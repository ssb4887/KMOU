
<%@ include file="../../../../include/commonTop.jsp"%>
<link rel="stylesheet" type="text/css" href="../../../css/majorInfo.css" />
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item"%>
<c:set var="inputFormId" value="fn_sampleInputForm" />
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page"
			value="${moduleJspRPath}/rcmdCultList.jsp" />
		<jsp:param name="inputFormId" value="${inputFormId}" />
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order" />
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}" />
<c:set var="itemObjs" value="${itemInfo.items}" />
<div id="cms_board_article">

	<jsp:include page="common/tabList.jsp" flush="false">
		<jsp:param name="tab" value="5" />
		<jsp:param name="dt" value="${dt}" />
	</jsp:include>

		<%-- table summary, 항목출력에 사용 --%>
		<c:set var="exceptIdStr">제외할 항목id를 구분자(,)로 구분하여 입력(예:name,notice,subject,file,contents,listImg)</c:set>
		<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}" />
		<%-- 
			table summary값 setting - 테이블 사용하지 않는 경우는 필요 없음
			디자인 문제로 제외한 항목(exceptIdStr에 추가했으나 table내에 추가되는 항목)은 수동으로 summary에 추가
			예시)
			<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
		--%>
		<c:set var="summary">
			<itui:tableSummary items="${items}" itemOrder="${itemOrder}"
				exceptIds="${exceptIds}" /> 입력표</c:set>

		<%-- 2. 디자인에 맞게 필요한 항목만 출력하는 경우 --%>
		<table class="tbWriteA" summary="${summary}">
			<caption>글쓰기 서식</caption>
			<colgroup>
				<col style="width: 150px;" />
				<col />
			</colgroup>
			<tbody>
				<tr>
					<th>소속</th>
					<td><input type="hidden" name="mjCd" value="${dt.MJ_CD}">
						<input type="hidden" name="year" value="${dt.YY}"> <c:out
							value="${dt.COLG_NM_KOR}" /> > <c:out
							value="${dt.FCLT_SUST_NM_KOR}" /> <c:if
							test="${dt.FCLT_SUST_CD ne dt.MJ_CD}">
							> <c:out value="${dt.MJ_NM_KOR}" />
						</c:if></td>
					<th>학년도</th>
					<td>${dt.YY}</td>
				</tr>
			</tbody>
		</table>
<!-- 			이동근 오픈때문에 우선 주석처리 -->
<%-- 		<form name="${searchFormId}" id="${searchFormId}" method="get" --%>
<%-- 			action="<c:out value="${URL_RCMD_CULT_LIST}"/>"> --%>
			

<!-- 			<div class="tbMSearch"> -->
<%-- 				<input type="hidden" name="mId" value="${queryString.mId}"> --%>
<%-- 				<input type="hidden" name="mjCd" value="${queryString.mjCd}"> --%>
<%-- 				<input type="hidden" name="year" value="${queryString.year}"> --%>
<!-- 				<fieldset id="topfieldset"> -->
<!-- 					<dl> -->
<!-- 						<select name="is_eduCorsCapbFg" id="s_eduCorsCapbFg" -->
<!-- 							class="select" title="역량구분"> -->
<!-- 							<option value="">핵심역량 선택</option> -->
<!-- 						</select> -->
<!-- 						<input placeHolder="교양교과명을 입력해 주세요" type="text" value="" -->
<!-- 							id="sbjtKorNm" title="sbjtKorNm" name="sbjtKorNm"></input>						 -->
<!-- 						<input type="submit" id="fn_btn_search" class="btnTypeL" -->
<!-- 							value="검색" title="검색"></input> -->

<!-- 					</dl> -->

<!-- 				</fieldset> -->
<!-- 			</div> -->
<!-- 			이동근 오픈때문에 우선 주석처리 끝 -->

		</form>
<%-- 		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_RCMD_CULT_INPUT_PROC}"/>" target="submit_target" enctype="multipart/form-data"> --%>
<!-- 			<div class="btnbx_right" style="margin-top:30px;"> -->
<%-- 				<a href="#;" onclick="window.open('${URL_RCMD_CULT_INPUT}&dl=1', '_blank', 'width=1200, height=1200')" class="btnTypeK fn_btn_modify" style="width:150px; margin-right:5px;"> 교양교과목 가져오기 </a> --%>
<!-- 				<input type=button value="조회" id="refresh" class="btnTypeI""> -->
<!-- 				<input type="submit" class="btnTypeJ fn_btn_submit" value="저장" title="저장" /> -->
<!-- 			</div> -->
<!-- 		</form> -->

		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_RCMD_CULT_INPUT_PROC}"/>" target="submit_target" enctype="multipart/form-data">
		<div class="btnbx_right" style="margin-top:30px;">
			<a href="#;" onclick="window.open('${URL_RCMD_CULT_INPUT}', '_blank', 'width=1200, height=1200')" class="btnTypeK fn_btn_modify" style="width:150px; margin-right:5px;"> 교양교과목 가져오기 </a>
<!-- 			<input type=button value="조회" id="refresh" class="btnTypeI"> -->
			<button type="submit" class="btnTypeJ fn_btn_submit">저장</button>
		</div>
		<div style="padding-right: 1%;">
			<table id="tpSubContent" class="tbListA"
				summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
				<caption>
					<c:out value="${settingInfo.list_title}" />
					목록
				</caption>
				<colgroup>
					<col style="width: 5%;" />
					<col style="width: 30%;" />
					<col />
					<col style="width: 9%;" />
					<col style="width: 10%;" />
					<col style="width: 15%;" />
				</colgroup>
				<thead>
					<tr>
						<%-- 				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th> --%>
						<th scope="col">순번</th>
						<th scope="col">역량구분</th>
						<th scope="col">과목명</th>
						<th scope="col">순번</th>
						<th scope="col">삭제</th>
						<th scope="col">학점</th>
						<%-- 				<th scope="col"><itui:objectItemName itemId="sampleItemId" itemInfo="${itemInfo}"/></th> --%>
						<%-- 				<th scope="col"><spring:message code="item.regidate.name"/></th> --%>
					</tr>
				</thead>
			</table>
			
			<div class="scrollable-tbody">
				<table id="tpSubContent" class="tbListA"
					summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
					<colgroup>
						<col style="width: 5%;" />
						<col style="width: 30%;" />
						<col />
						<col style="width: 9%;" />
						<col style="width: 10%;" />
						<col style="width: 15%;" />
					</colgroup>

					<tbody class="alignC">
						<c:if test="${empty list}">
							<tr>
								<td colspan="7" class="bllist"><spring:message
										code="message.no.list" /></td>
							</tr>
						</c:if>
						<c:set var="listMjCdIdxName" value="${settingInfo.idx_major_name}" />
						<c:set var="listCourseNoIdxName"
							value="${settingInfo.idx_course_no_name}" />
						<c:set var="listYearIdxName" value="${settingInfo.idx_year_name}" />
						<c:set var="listNo"
							value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex + 1}" />						
						<c:forEach var="listDt" items="${list}" varStatus="i">

							<c:if test="${i.last}">
								<input type="hidden" id="dtLength" name="dtLength" value="${i.index}" />
									
							</c:if>
							<tr>
								<itui:objectText itemId="mjCd" itemInfo="${itemInfo}"
									objStyle="display:none" />
								<itui:objectText itemId="year" itemInfo="${itemInfo}"
									objStyle="display:none" />
								<%-- 					<td><c:if test="${mngAuth || wrtAuth && listDt.AUTH_MNG == '1'}"><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></c:if></td> --%>

								<td class="num" id="num">${listNo}</td>
								<%-- <td><a href="<c:out value="${URL_TRACK_INPUT}"/>&mode=m&${listMajorIdxName}=${listDt.MAJOR_IDX}&${listYearIdxName}=${listDt.YEAR_IDX}&${listSptPsnIdxName}=${listDt.SPT_PSN_KOR}" >수정</a></td> --%>

								<!-- 역량구분 -->
								<td id="test${i.index}"> 
									<itui:objectView itemId="sbjtFgNm" itemInfo="${itemInfo}" objDt="${listDt}" />
									<input type="hidden" name="eduCorsCapbFg${i.index }" id="eduCorsCapbFg${i.index }" value="${listDt.EDU_CORS_CAPB_FG}">
								
									<select name="is_eduCorsCapbFg${i.index }" id="s_eduCorsCapbFg${i.index }" class="select" title="역량구분" style="width:50%;">
									</select>
								</td>

								<!-- 과목명 -->
								<td>${listDt.SBJT_NM_KOR }</td>
								<c:set var="sbjtNmKor" value="${listDt.SBJT_NM_KOR }" />

								<!-- 순번 -->
								<td><input type="number" name="ord${i.index}" id="ord_${i.index}" value="${listDt.ORD}" style="padding:0 5px; border:1px solid #ddd; height:32px; border-radius:3px; backgroud:#f9f9f9; width:45px;"></td>
								<c:set var="ord" value="${listDt.ORD }" />

								<!-- 삭제 -->
								<td><button type="button" onclick=fun_delete_major(this);
										id="deleteSub" class="btnTypeF">삭제</button></td>

								<c:choose>
									<c:when test="${!empty list}">
										<input type="hidden" name="rcmdCultRowLength"
											id="rcmdCultRowLength" value="${fn:length(list)+1 }">
									</c:when>
									<c:otherwise>
										<input type="hidden" name="rcmdCultRowLength"
											id="rcmdCultRowLength" value="1">
									</c:otherwise>
								</c:choose>

								<!-- 학점 -->
								<td>${listDt.PNT}-${listDt.THEO_TM_CNT}-${listDt.PRAC_TM_CNT}</td>
								<c:set var="pnt" value="${listDt.PNT }" />
								<c:set var="theoTmCnt" value="${listDt.THEO_TM_CNT }" />
								<c:set var="pracTmCnt" value="${listDt.PRAC_TM_CNT }" />

							</tr>
							<c:set var="listNo" value="${listNo + 1}" />
							<input type="hidden" name="courseNo${i.index }" id="courseNo${i.index }" value="${listDt.COURSE_NO}" />
							<input type="hidden" name="nmKor${i.index }" id="sbjtNmKor${i.index }" value="${listDt.SBJT_NM_KOR}"/>
							<input type="hidden" name="nmEng${i.index }" id="sbjtNmEng${i.index }" value="${listDt.SBJT_NM_ENG}"/>
							<input type="hidden" name="pnt${i.index }" id="pnt${i.index }" value="${listDt.PNT}"/>
							<input type="hidden" name="theo${i.index }" id="theo${i.index }" value="${listDt.THEO_TM_CNT}"/>
							<input type="hidden" name="prac${i.index }" id="prac${i.index }" value="${listDt.PRAC_TM_CNT}"/>
							
<%-- 				<input type="hidden" name="shtmCd${i.index }" id="shtmCd${i.index }" value="${shtmCd }"/> --%>
<%-- 				<input type="hidden" name="openSustMjNm${i.index }" id="openSustMjNm${i.index }" value="${openSustMjNm }"/> --%>
				
<%-- 				<input type="hidden" name="sbjtFg${i.index }" id="sbjtFg${i.index }" value="${listDt.SBJT_FG }"/> --%>
<%-- 				<input type="hidden" name="openShyrFg${i.index }" id="openShyrFg${i.index }" value="${listDt.OPEN_SHYR_FG }"/> --%>
<%-- 				<input type="hidden" name="sbjtNmKor${i.index }" id="sbjtNmKor${i.index }" value="${sbjtNmKor}"/> --%>
<%-- 				<input type="hidden" name="courseNo${i.index }" id="courseNo${i.index }" value="${listDt.COURSE_NO }"/> --%>
<%-- 				<input type="hidden" name="lastModiId${i.index }" id="lastModiId${i.index }" value="${lastModiId }"/> --%>
<%-- 				<input type="hidden" name="lastModiDate${i.index }" id="lastModiDate${i.index }" value="${lastModiDate }"/> --%>
				
<%-- 				<input type="hidden" name="docId${i.index }" id="docId${i.index }" value="${sbjtFgNm}+${openShyrNm}+${openSustMjNm }+${listDt.COURSE_NO }+${listDt.SPT_PSN_KOR}"/> --%>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="btnCenter">
			<input type=button value="목록" class="btnTypeI fn_btn_jobCancel" />
		</div>
	</form>
</div>
<div id="footerWrap" style="bottom: auto; width: 93%">
	<div id="footer">
		<p class="copyright">
			<c:out value="${siteInfo.site_copyright}" />
		</p>
	</div>
</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if> --%>