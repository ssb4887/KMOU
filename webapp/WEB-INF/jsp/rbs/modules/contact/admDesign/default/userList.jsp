<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false"/>
</c:if>
<div id="cms_board_article">
	<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
		<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
		<colgroup>
			<col width="120px" />
			<col width="150px" />
			<col />
		</colgroup>
		<thead>
			<tr>
				<th scope="col">접속IP</th>
				<th scope="col">접속일자</th>
				<th scope="col">아이디</th>
			</tr>
		</thead>
		<tbody class="alignC">
		<c:if test="${empty list}">
			<tr>
				<td colspan="3" class="nolist"><spring:message code="message.no.contact.list"/></td>
			</tr>
		</c:if>
		<c:forEach var="listDt" items="${list}">
			<tr>
				<td scope="row" class="num"><c:out value="${listDt.ACCESS_IP}"/></td>
				<td class="date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${listDt.REGI_DATE}"/></td>
				<td><c:out value="${listDt.MEMBER_ID}"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
	<!-- paging -->
	<div class="paginate mgt15">
		<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
	</div>
	<!-- //paging -->
	
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>