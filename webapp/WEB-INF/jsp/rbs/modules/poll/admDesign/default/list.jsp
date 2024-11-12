<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							<%/* 등록관리권한 */ %>
<c:set var="vewAuth" value="${elfn:isAuth('VEW')}"/>							<%/* 결과보기권한 */ %>
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
		<!-- search -->
		<itui:searchFormItem divClass="tbMSearch" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		<!-- //search -->
		
		<!-- button -->
		<div class="btnTopFull">
			<%@ include file="../../../../adm/include/module/listBtns.jsp"%>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="50px" />
				<col width="60px" />
				<col width="60px" />
				<col width="85px" />
				<col width="90px" />
				<col />
				<col width="100px" />
				<col width="60px" />
				<col width="60px" />
				<col width="80px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
				<th scope="col">번호</th>
				<th scope="col">수정</th>
				<th scope="col"><c:out value="${msgItemMng}"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="isresult"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="title"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="limitDate"/></th>
				<th scope="col"><spring:message code="item.poll.joiner.count"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="isstop"/></th>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="10" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<fmt:formatDate var="crtDate" value="<%=new java.util.Date() %>" pattern="yyyy-MM-ddHHmm"  />
				<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
               	<c:set var="limitDate1" value="${listDt.LIMIT_DATE11}${listDt.LIMIT_DATE12}${listDt.LIMIT_DATE13}" />
               	<c:set var="limitDate2" value="${listDt.LIMIT_DATE21}${listDt.LIMIT_DATE22}${listDt.LIMIT_DATE23}" />
				<c:choose>
               		<c:when test="${listDt.ISSTOP eq '2' || !empty limitDate2 && limitDate2 < crtDate}">
               			<%/* 상태 '2' || 설문종료일 지난 경우 : 설문종료 */ %>
               			<spring:message var="displayName" code="message.poll.end" />
               		</c:when>
               		<c:when test="${listDt.ISSTOP eq '0' || !empty limitDate1 && limitDate1 > crtDate}">
               			<%/* 상태 '0' && 설문시작일 전인 경우 : 준비중  */ %>
               			<spring:message var="displayName" code="message.poll.ready" />
               		</c:when>
               		<c:otherwise>
               			<%/* 진행중  */ %>
               			<spring:message var="displayName" code="message.poll.progress" />
               		</c:otherwise>
               	</c:choose>
				<tr>
					<td><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></td>
					<td class="num">${listNo}</td>
					<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF ${btnModifyClass}">수정</a></c:if></td>
					<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_QUESLIST}"/>&${listIdxName}=${listKey}" class="btnTypeE"><c:out value="${msgItemMng}"/></a></c:if></td>
					<td>
					<c:if test="${mngAuth}">
						<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" class="btnTypeAS"><itui:objectItemName itemInfo="${itemInfo}" itemId="isresult"/></a>
						<a href="<c:out value="${URL_RESPLIST}"/>&${listIdxName}=${listKey}" class="btnTypeAS fn_btn_resplist_open">참여목록</a>
						<c:if test="${settingInfo.use_private == '1' && settingInfo.use_reply == '1'}">
						<a href="<c:out value="${URL_RESEXCELDOWN}"/>&${listIdxName}=${listKey}" class="btnTypeAS fn_btn_exceldown" title="엑셀다운로드">엑셀다운로드</a>
						</c:if>
					</c:if>
					</td>
					<td class="tlt"><itui:objectView itemId="title" itemInfo="${itemInfo}" objDt="${listDt}"/></td>
					<td class="date lt"><c:if test="${!empty listDt.LIMIT_DATE11 || !empty listDt.LIMIT_DATE21}">
					<c:set var="objVal11" value="${listDt.LIMIT_DATE11}"/>
					<c:set var="objVal21" value="${listDt.LIMIT_DATE21}"/>
					<c:out value="${objVal11}"/><c:out value="${objVal12}"/> ~<br/><c:out value="${objVal21}"/><c:out value="${objVal22}"/>
					</c:if></td>
					<td><fmt:formatNumber value="${listDt.RESP_CNT}" pattern="#,##0" /></td>
					<td><c:out value="${displayName}"/></td>
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