<%@ include file="../../../../include/commonTop.jsp"%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false"/>
</c:if>
	<div class="inWfullContWrap">
		<div class="tcomment" id="fn_comment">
		<spring:message code="message.menuContents.manager"/>
		</div>
    </div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>