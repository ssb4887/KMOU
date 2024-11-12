<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item"%>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_codeMstrSearchForm"/>
<c:set var="listFormId" value="fn_codeMstrListForm"/>

<c:set var="headTitle" value="추천균형교양교과목"/>



<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${headTitle }"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/rcmdCultInput.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
<div id="cms_board_article">

	<form name="${searchFormId}" id="${searchFormId}" method="get"
			action="<c:out value="${URL_RCMD_CULT_INPUT}"/>">
			
			<div class="tbMSearch">
				<input type="hidden" name="mId" value="${queryString.mId}">
				<input type="hidden" name="mjCd" value="${queryString.mjCd}">
				<input type="hidden" name="year" value="${queryString.year}">
				<input type="hidden" name="dl" value="1">
				<fieldset id="topfieldset">
					<dl>
						<input placeHolder="교양교과명을 입력해 주세요" type="text" value=""
							title="sbjtKorNm" name="sbjtKorNm" id="sbjtKorNm"/>						
						<input type="submit" id="fn_btn_search" class="btnTypeL"
							value="검색" title="검색"/>
						<select name="is_eduCorsCapbFg" id="s_eduCorsCapbFg"
							class="select" title="역량구분" style="margin-left:300px;">
							<option value="">핵심역량 선택</option>
						</select>
					</dl>

				</fieldset>
			</div>
		</form>
	</div>
	
	<div class="btnRight">
		<button type="button" id="saveSub" class="btnTypeA fn_btn_submit">교과목 추가</button>
		 <a href="javascript:self.close();" title="닫기" class="btnTypeB back_to_list">닫기 </a>
	</div>

	<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_RCMD_CULT_INPUT}&dl=1"/>" target="submit_target" enctype="multipart/form-data">
		<div style="padding-right:1%; margin-top:15px;">
		<table id="tpSubContent" class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption>
				<c:out value="${settingInfo.list_title}" />
				목록
			</caption>
			<colgroup>
				<col width="5%" />
					<col width="5%" />
					<col width="10%" />
					<col width="" />
					<col width="10%" />
					<col width="10%" />
					<col width="10%" />
			</colgroup>
			<thead>
				<tr>
					<c:if test="${isMajorInfo ne 'Y'}">
						<th scope="col"><c:if test="${mngAuth}">
								<input type="checkbox" id="selectAll" name="selectAll"
									title="<spring:message code="item.select.all"/>" />
							</c:if></th>
					</c:if>
					<th scope="col">선택</th>
					<th scope="col">학수번호</th>
					<th scope="col">과목명</th>
					<th scope="col">학점</th>
					<th scope="col">이론</th>
					<th scope="col">실습</th>

					<!-- 마지막 th에 class="end" -->
				</tr>
			</thead>
		</table>
		</div>
		
		<div class="scrollable-tbody">
			<table id="tpSubContent" class="tbListA"
				summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
				
				<colgroup>
					<col width="5%" />
					<col width="5%" />
					<col width="10%" />
					<col width="" />
					<col width="10%" />
					<col width="10%" />
					<col width="10%" />
				</colgroup>
				<tbody class="alignC">
					<c:if test="${empty haksaRcmdCultList}">
						<tr>
							<td colspan="7" class="bllist"><spring:message
									code="message.no.list" /></td>
						</tr>
					</c:if>
					<c:set var="listIdxName" value="${settingInfo.idx_name}" />
					<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
					<c:forEach var="listDt" items="${haksaRcmdCultList}" varStatus="i">
						<c:if test="${i.last}">
							<input type="hidden" id="dtLength" name="dtLength" value="${i.index}" />									
						</c:if>
						<tr>
							<td>
								<c:if test="${mngAuth}">
									<input type="checkbox" id="select" name="select"  data-idx="${i.index}" title="<spring:message code="item.select"/>" value="${listKey}" />
								</c:if>
							</td>
							<td class="num">${listNo}</td>
							<td id="courseNo_${i.index}">${listDt.COURSE_NO}</td>
							<td id="sbjt_${i.index}">${listDt.SBJT_KOR_NM}</td>
							<td id="pnt_${i.index}">${listDt.PNT}</td>
							<td id="theo_${i.index}">${listDt.THEO_TM_CNT}</td>
							<td id="prac_${i.index}">${listDt.PRAC_TM_CNT}</td>

							<input type="hidden" name="YY_${i.index}" value="${listDt.YY}"></input>
<%-- 							<input type="hidden" name="OPEN_SUST_MJ_CD_${i.index}" --%>
<%-- 								value="${listDt.OPEN_SUST_MJ_CD}"></input> --%>
<%-- 							<input type="hidden" name="SHTM_CD_${i.index}" --%>
<%-- 								value="${listDt.SHTM_CD}"></input> --%>
<%-- 							<input type="hidden" name="SHTM_NM_${i.index}" --%>
<%-- 								value="${listDt.SHTM_NM}"></input> --%>
<%-- 							<input type="hidden" name="COURSE_NO_${i.index}" --%>
<%-- 								value="${listDt.COURSE_NO}"></input> --%>
<%-- 							<input type="hidden" name="OPEN_SHYR_FG_${i.index}" --%>
<%-- 								value="${listDt.OPEN_SHYR_FG}"></input> --%>
<%-- 							<input type="hidden" name="SHYR_NM_${i.index}" --%>
<%-- 								value="${listDt.SHYR_NM}"></input> --%>
<%-- 							<input type="hidden" name="SBJT_KOR_NM_${i.index}" --%>
<%-- 								value="${listDt.SBJT_KOR_NM}"></input> --%>
							<input type="hidden" name="SBJT_ENG_NM_${i.index}"
								value="${listDt.SBJT_ENG_NM}" id="SBJT_ENG_NM_${i.index}"></input>
<%-- 							<input type="hidden" name="SBJT_FG_${i.index}"} --%>
<%-- 								value="${listDt.SBJT_FG}"></input> --%>
<%-- 							<input type="hidden" name="SBJT_NM_${i.index}" --%>
<%-- 								value="${listDt.SBJT_NM}"></input> --%>
						</tr>
						<c:set var="listNo" value="${listNo + 1}" />
					</c:forEach>
				</tbody>
			</table>
		</div>
	</form>

</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include
		page="${BOTTOM_PAGE}" flush="false" /></c:if>