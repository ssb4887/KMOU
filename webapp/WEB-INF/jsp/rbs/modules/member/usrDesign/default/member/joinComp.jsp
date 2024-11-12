<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="memberInputForm"/>
<c:choose>
	<c:when test="${elfn:isUseNameAuth()}"><c:set var="joinStep" value="4"/></c:when>
	<c:otherwise><c:set var="joinStep" value="3"/></c:otherwise>
</c:choose>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/join01.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
		<jsp:param name="joinStep" value="${joinStep}"/>
	</jsp:include>
</c:if>
<div class="sContent">
	<div id="cms_member_article">
		<jsp:include page="joinStep.jsp" flush="false"/>
		
		<div class="regConfirm">
			<img src="<c:out value="${contextPath}${moduleResourcePath}/images/reg_end.png"/>" alt="회원 가입완료" />
			<span class="titTypeA mgt40"><c:out value="${siteInfo['site_name']}"/> 회원가입을 축하드립니다.</span>
		</div>
		
		<div class="btn" style="text-align:center;">
			<a href="<c:out value="${contextPath}${indexUrl}"/>" class="btnTypeAL">메인으로</a>
			<a href="<%=MenuUtil.getMenuUrl(3) %>" class="btnTypeAL">로그인</a>
		</div>

	</div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>