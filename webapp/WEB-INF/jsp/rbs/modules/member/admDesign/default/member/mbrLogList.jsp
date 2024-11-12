<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="searchFormId" value="fn_memberSearchForm"/>
<c:set var="listFormId" value="fn_memberListForm"/>
<c:set var="idxName" value="${settingInfo.idx_name}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/mbrLogList.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<div class="tbMSearch">
			<form name="fn_memberSearchForm" id="fn_memberSearchForm" method="get" action="${URL_IDX_VIEW}">
				<input type="hidden" name="mId" value="<c:out value="${queryString.mId}"/>"/>
				<input type="hidden" name="${idxName}" value="<c:out value="${queryString[idxName]}"/>"/>
				<input type="hidden" name="tit" value="<c:out value="${queryString.tit}"/>"/>
				<input type="hidden" name="mdl" value="1">
				<fieldset>
					<legend>상세검색</legend>
					<dl>
						<c:set var="isRegiStartDate"><spring:message code="Globals.item.search.pre.flag"/>regiStartDate</c:set>
						<dt id="fn_dt_${isRegiStartDate}"><label for="${isRegiStartDate}">접근일시</label></dt>
						<dd>
							<input type="text" name="${isRegiStartDate}" id="${isRegiStartDate}" title="접근일시 시작일" readonly="readonly" style="width:80px;" value="<c:out value="${queryString[isRegiStartDate]}"/>">
							<button type="button" class="btnCal" id="fn_btn_${isRegiStartDate}" title="접근일시 시작일 달력"></button>
							~
							<c:set var="isRegiEndDate"><spring:message code="Globals.item.search.pre.flag"/>regiEndDate</c:set>
							<input type="text" name="${isRegiEndDate}" id="${isRegiEndDate}" title="접근일시 종료일" readonly="readonly" style="width:80px;" value="<c:out value="${queryString[isRegiEndDate]}"/>">
							<button type="button" class="btnCal" id="fn_btn_${isRegiEndDate}" title="접근일시 종료일 달력"></button>
						</dd>
						<c:set var="isRegiId"><spring:message code="Globals.item.search.pre.flag"/>regiId</c:set>
						<dt id="fn_dt_${isRegiId}"><label for="${isRegiId}">접근 ID</label></dt>
						<dd>
							<input type="text" name="${isRegiId}" id="${isRegiId}" value="<c:out value="${queryString[isRegiId]}"/>" class="search_text" title="검색어" />
						</dd>
						<c:set var="isLogType"><spring:message code="Globals.item.search.pre.flag"/>logType</c:set>
						<dt id="fn_dt_${isLogType}"><label for="${isLogType}">작업 내용</label></dt>
						<dd class="full">
							<c:set var="qsIsLogTypeName" value="${isLogType}_multi"/>
							<c:if test="${empty queryString[qsIsLogTypeName]}"><c:set var="qsIsLogTypeName" value="${isLogType}"/></c:if>
							<c:set var="qsIsLogType" value="${queryString[qsIsLogTypeName]}"/>
							<spring:message var="isLogType1" code="message.member.log.insert"/>
							<input type="checkbox" name="${isLogType}" id="${isLogType}1" value="${isLogType1}" title="${isLogType1}"<c:if test="${fn:indexOf(qsIsLogType, isLogType1) >= 0}"> checked="checked"</c:if>/> ${isLogType1}
							<spring:message var="isLogType2" code="message.member.log.view"/>
							&nbsp;<input type="checkbox" name="${isLogType}" id="${isLogType}2" value="${isLogType2}" title="${isLogType2}"<c:if test="${fn:indexOf(qsIsLogType, isLogType2) >= 0}"> checked="checked"</c:if>/> ${isLogType2}
							<spring:message var="isLogType3" code="message.member.log.modify"/>
							&nbsp;<input type="checkbox" name="${isLogType}" id="${isLogType}3" value="${isLogType3}" title="${isLogType3}"<c:if test="${fn:indexOf(qsIsLogType, isLogType3) >= 0}"> checked="checked"</c:if>/> ${isLogType3}
							<spring:message var="isLogType4" code="message.member.log.delete"/>
							&nbsp;<input type="checkbox" name="${isLogType}" id="${isLogType}4" value="${isLogType4}" title="${isLogType4}"<c:if test="${fn:indexOf(qsIsLogType, isLogType4) >= 0}"> checked="checked"</c:if>/> ${isLogType4}
							<spring:message var="isLogType5" code="message.member.log.idsearch"/>
							&nbsp;<input type="checkbox" name="${isLogType}" id="${isLogType}5" value="${isLogType5}" title="${isLogType5}"<c:if test="${fn:indexOf(qsIsLogType, isLogType5) >= 0}"> checked="checked"</c:if>/> ${isLogType5}
							<spring:message var="isLogType6" code="message.member.log.pwsearch"/>
							&nbsp;<input type="checkbox" name="${isLogType}" id="${isLogType}6" value="${isLogType6}" title="${isLogType6}"<c:if test="${fn:indexOf(qsIsLogType, isLogType6) >= 0}"> checked="checked"</c:if>/> ${isLogType6}
							<spring:message var="isLogType7" code="message.member.log.login"/>
							&nbsp;<input type="checkbox" name="${isLogType}" id="${isLogType}7" value="${isLogType7}" title="${isLogType7}"<c:if test="${fn:indexOf(qsIsLogType, isLogType7) >= 0}"> checked="checked"</c:if>/> ${isLogType7}
						</dd>
					</dl>
					<p>
						<input type="submit" id="fn_btn_search" class="btnSearch" value="검색" title="검색"/>
					</p>
				</fieldset>
			</form>
		</div>
	
		<!-- button -->
		<div class="btnTopFull">
			<div class="right">
				<div class="resultCount">총 <strong>${paginationInfo.totalRecordCount}</strong>건 (${paginationInfo.currentPageNo}/${paginationInfo.totalPageCount}페이지)</div>
			</div>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="60px" />
				<col width="150px" />
				<col />
				<col width="150px" />
				<col width="150px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">개인정보 ID</th>
				<th scope="col">작업내용</th>
				<th scope="col">접근 ID</th>
				<th scope="col" class="end">접근일시</th>
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="5" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<tr>
					<td class="num">${listNo}</td>
					<td><itui:objectView itemId="mbrId" itemInfo="${itemInfo}" objVal="${listDt.MEMBER_ID}"/></td>
					<td class="tlt"><c:out value="${listDt.LOG_TYPE}"/></td>
					<td><itui:objectView itemId="mbrId" itemInfo="${itemInfo}" objVal="${listDt.REGI_ID}"/></td>
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${listDt.REGI_DATE}"/></td>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</tbody>
		</table>
		</form>
		
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
		</div>
		<!-- //paging -->
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>