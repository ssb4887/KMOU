<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="mwtAuth" value="${elfn:isAuth('MWT')}"/>
<c:set var="listFormId" value="fn_boardListForm"/>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="items" value="${itemInfo.items}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
		<c:set var="exceptIds" value="${fn:split('name,notice,subject,file,contents',',')}"/>
		<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
		<table class="tbViewA" summary="${summary}">
			<caption>
			게시글 읽기
			</caption>
			<colgroup>
			<col style="width:100px;" />
			<col />
			<col style="width:100px;" />
			<col />
			<col style="width:100px;" />
			<col style="width:60px;" />
			</colgroup>
			<thead>
				<tr>
					<th colspan="6" class="viewTlt">
					<c:if test="${settingInfo.use_notice eq '1' and dt.NOTICE eq '1'}">[<spring:message code="item.notice.name"/>] </c:if>
					<c:out value="${dt.SUBJECT}"/>
					<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(dt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th scope="row"><spring:message code="item.reginame1.name"/></th><td><c:out value="${dt.NAME}"/></td>
					<th scope="row"><spring:message code="item.regidate1.name"/></th><td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${dt.REGI_DATE}"/></td>
					<th scope="row"><spring:message code="item.board.views.name"/></th><td><c:out value="${dt.VIEWS}"/></td>
				</tr>
				<c:if test="${settingInfo.use_file == '1'}">
				<tr>
					<th scope="row"><spring:message code="item.file.name"/></th><td colspan="5"><itui:objectView itemId="file" multiFileHashMap="${multiFileHashMap}"/></td>
				</tr>
				</c:if>
				<itui:itemViewAll colspan="5" itemInfo="${itemInfo}" itemOrderName="${submitType}_order" exceptIds="${exceptIds}"/>
				<tr>
					<td class="cont" colspan="6">
						<itui:objectView itemId="contents"/>
					</td>
				</tr>
			</tbody>
		</table>
		<%@ include file="../../common/viewBtns.jsp"%>
		<c:if test="${settingInfo.use_memo eq '1'}">
		<%@ include file="../../common/viewMemo.jsp"%>
		</c:if>
		<%-- 비회원글쓰기/댓글쓰기권한 : 비밀번호 확인  --%>
		<%@ include file="../../common/password.jsp"%>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>