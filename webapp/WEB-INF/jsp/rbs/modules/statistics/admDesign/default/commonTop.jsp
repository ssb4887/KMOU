<c:choose>
	<c:when test="${queryString.is_statsType == '1'}">
		<spring:message var="gubunItemName" code="item.contact.date.month"/>
	</c:when>
	<c:when test="${queryString.is_statsType == '2'}">
		<spring:message var="gubunItemName" code="item.contact.date.day"/>
	</c:when>
	<c:when test="${queryString.is_statsType == '3'}">
		<spring:message var="gubunItemName" code="item.contact.date.hour"/>
	</c:when>
	<c:otherwise>
		<spring:message var="gubunItemName" code="item.contact.date.year"/>
	</c:otherwise>
</c:choose>
<c:set var="searchFormId" value="fn_contactSearchForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/siteList.jsp"/>	
		<jsp:param name="searchFormId" value="${searchFormId}"/>
	</jsp:include>
</c:if>