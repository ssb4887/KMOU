<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							<%/* 등록관리권한 */ %>
<c:set var="searchFormId" value="fn_pollSearchForm"/>							<%/* 검색폼 id/name */ %>
<c:set var="listFormId" value="fn_pollListForm"/>								<%/* 목록관리폼 id/name */ %>
<c:set var="inputWinFlag" value="_open"/>										<%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/>				<%/* 수정버튼 class */%>
<spring:message var="msgItemMng" code="title.poll.question.manager"/>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<%/* 검색폼 id/name : javascript에서 사용 */ %>
	<%/* 목록관리폼 id/name : javascript에서 사용 */ %>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_poll_article">
		<c:if test="${!empty ingDt}">
		<%@ include file="listResponse.jsp" %>
		</c:if>
		<c:if test="${settingInfo.use_list_noreply != '1' || crtMenu.fn_use_private == '1'}">
		<%/* 진행설문 1개 사용 안하는 경우 || 자신글 이력 목록 */ %>
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="60px" />
				<col />
				<col width="200px" />
				<col width="80px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="title"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="limitDate"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="isstop"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="4" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<fmt:formatDate var="crtDate" value="<%=new java.util.Date() %>" pattern="yyyy-MM-ddHHmm" />
				<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
               	<c:set var="limitDate1" value="${listDt.LIMIT_DATE11}${listDt.LIMIT_DATE12}${listDt.LIMIT_DATE13}" />
               	<c:set var="limitDate2" value="${listDt.LIMIT_DATE21}${listDt.LIMIT_DATE22}${listDt.LIMIT_DATE23}" />
               	<c:set var="isListPollResp" value="${false}"/>
               	<c:if test="${!empty listDt.RESP_IDX}"><c:set var="isListPollResp" value="${true}"/></c:if>
				<c:choose>
					<c:when test="${(crtMenu.fn_use_private == '1' || usePrivate && isListPollResp || (!usePrivate && listDt.ISSTOP eq '2' || !empty limitDate2 && limitDate2 < crtDate)) && listDt.ISRESULT == '1'}">
					<itui:objectItemName var="displayName" itemInfo="${itemInfo}" itemId="isresult"/>
					<c:set var="displayName">
						<a href="${URL_VIEW}&${listIdxName}=<c:out value="${listKey}"/>" class="btnTypeHF"><itui:objectItemName itemInfo="${itemInfo}" itemId="isresult"/></a>
					</c:set>
					</c:when>
               		<c:when test="${crtMenu.fn_use_private == '1' || usePrivate && isListPollResp || listDt.ISSTOP eq '2' || !empty limitDate2 && limitDate2 < crtDate}">
               			<%/* 상태 '2' || 설문종료일 지난 경우 : 설문종료 */ %>
           				<spring:message var="displayName" code="message.poll.end" />
               		</c:when>
               		<c:when test="${listDt.ISSTOP eq '0' || !empty limitDate1 && limitDate1 > crtDate}">
               			<%/* 상태 '0' && 설문시작일 전인 경우 : 준비중  */ %>
               			<spring:message var="displayName" code="message.poll.ready" />
               		</c:when>
               		<c:otherwise>
               			<%/* 진행중  */ %>
						<c:choose>
							<c:when test="${settingInfo.use_notice == '1'}">
							<%/* 진행설문 목록출력 */ %>
								<c:set var="respUrl" value="${URL_LIST}&lpIdx=${listKey}"/>
							</c:when>
							<c:otherwise>
								<c:set var="respUrl" value="${URL_INPUT}&${listIdxName}=${listKey}"/>
							</c:otherwise>
						</c:choose>
						<c:set var="displayName">
							<a href="<c:out value="${respUrl}"/>" class="btnTypeF"><spring:message code="item.poll.join"/></a>
						</c:set>
               		</c:otherwise>
               	</c:choose>
				<tr>
					<td class="num">${listNo}</td>
					<td class="tlt"><itui:objectView itemId="title" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<td class="date lt"><c:if test="${!empty listDt.LIMIT_DATE11 || !empty listDt.LIMIT_DATE21}">
					<c:set var="objVal11" value="${listDt.LIMIT_DATE11}"/>
					<c:set var="objVal21" value="${listDt.LIMIT_DATE21}"/>
					<c:out value="${objVal11}"/><c:out value="${objVal12}"/> ~ <c:out value="${objVal21}"/><c:out value="${objVal22}"/>
					</c:if></td>
					<td>${displayName}</td>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</tbody>
		</table>
		<c:if test="${settingInfo.use_notice == '1' && !empty param.lpIdx}">
			<%/* 진행설문 목록출력 */ %>
			<c:set var="pagingAddParam" value="lpIdx=${param.lpIdx}"/>
		</c:if>
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}" addParam="${pagingAddParam}"/>
		</div>
		<!-- //paging -->
		</c:if>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>