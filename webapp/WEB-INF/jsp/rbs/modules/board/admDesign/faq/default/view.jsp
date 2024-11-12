<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<table class="tbViewA" summary="게시글 읽기">
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
				<c:set var="exceptIds" value="${fn:split('name,notice,subject,file',',')}"/>
				<itui:itemViewAll colspan="5" itemInfo="${itemInfo}" itemOrderName="${submitType}_order" exceptIds="${exceptIds}"/>
			</tbody>
		</table>
		<jsp:include page="../../common/viewBtns.jsp" flush="false"/>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>