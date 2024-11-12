<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="searchFormId" value="fn_fnSearchForm"/>
<c:set var="listFormId" value="fn_fnListForm"/>
<c:set var="inputWinFlag" value="_open"/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.list_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/designView.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
<div id="cms_board_article">
	<h4><c:out value="${queryString.dg}"/></h4>
	<ul class="fn_designList">
	<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
	<c:forEach var="designName" items="${list}" varStatus="i">
		<li>
			<p class="img"><img src="<c:out value="${contextPath}${listImgPath}"/>/${elfn:encode(designName, 'UTF-8')}" alt="<c:out value="${designName}"/>" onerror="fn_errorImg(this);"/></p>
		</li>
	</c:forEach>
	</ul>
	<div class="btnTopFull">
		<div class="right">
			<a href="<c:out value="${URL_LIST}"/>" title="목록" class="btnTypeA fn_btn_write">목록</a>
		</div>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>