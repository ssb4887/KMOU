<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							<%/* 등록관리권한 */ %>
<c:set var="searchFormId" value="fn_quesSearchForm"/>							<%/* 검색폼 id/name */ %>
<c:set var="listFormId" value="fn_quesListForm"/>								<%/* 목록관리폼 id/name */ %>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/>				<%/* 수정버튼 class */%>
<spring:message var="msgItemMng" code="title.poll.question.manager"/>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<%/* 검색폼 id/name : javascript에서 사용 */ %>
	<%/* 목록관리폼 id/name : javascript에서 사용 */ %>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.list_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>	
		<jsp:param name="searchFormId" value="${searchFormId}"/>				
		<jsp:param name="listFormId" value="${listFormId}"/>					
	</jsp:include>
</c:if>
	<div id="cms_poll_article">
		<%@ include file="top.jsp" %>
		<!-- search -->
		<itui:searchFormItem divClass="tbMSearch" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		<!-- //search -->
		
		<!-- button -->
		<div class="btnTopFull">
			<c:set var="addBtnScript">
				<a href="<c:out value="${URL_POLLLIST}"/>" title="설문목록" class="btnTFDL">설문목록</a>
			</c:set>
			<%@ include file="../../../../../adm/include/module/listBtns.jsp"%>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="50px" />
				<col width="60px" />
				<col width="60px" />
				<col />
				<c:if test="${!ISQUESTYPE}">
				<col width="120px" />
				<col width="60px" />
				</c:if>
				<col width="80px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
				<th scope="col">번호</th>
				<th scope="col">수정</th>
				<c:choose>
				<c:when test="${ISQUESTYPE}"><c:set var="itemName" value="유형"/></c:when>
				<c:otherwise><itui:objectItemName var="itemName" itemInfo="${itemInfo}" itemId="questionContents"/></c:otherwise>
				</c:choose>
				<th scope="col">${itemName}</th>
				<c:if test="${!ISQUESTYPE}">
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="answerType"/></th>
				<th scope="col"><spring:message code="item.poll.question.rel"/></th>
				</c:if>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<c:set var="colspanNum" value="7"/>
				<c:if test="${ISQUESTYPE}">
					<c:set var="colspanNum" value="5"/>
				</c:if>
				<tr>
					<td colspan="${colspanNum}" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
				<tr>
					<td><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></td>
					<td class="num">${listNo}</td>
					<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF ${btnModifyClass}">수정</a></c:if></td>
					<td class="lt"><itui:objectView itemId="questionContents" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<c:if test="${!ISQUESTYPE}">
					<td><itui:objectView itemId="answerType" itemInfo="${itemInfo}" objDt="${listDt}"/><c:if test="${listDt.ANSWER_TYPE == '1'}">(<itui:objectView itemId="itemType" itemInfo="${itemInfo}" objDt="${listDt}"/>)</c:if></td>
					<td><c:choose><c:when test="${listDt.REL_QUES_IDX > 0 && !empty listDt.REL_ITEM_IDX}">사용</c:when><c:otherwise>미사용</c:otherwise></c:choose></td>
					</c:if>
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
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