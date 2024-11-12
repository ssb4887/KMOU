<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="imgPath"%>																		<% /* 이전/다음 이미지 root 경로 */ %>
<%@ attribute name="listUrl"%>																		<% /* 페이징 목록 경로 */ %>
<%@ attribute name="pageName"%>																		<% /* 페이징 parameter명 */ %>
<%@ attribute name="pgInfo" type="egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo"%>	<% /* 페이징 정보 */ %>
<%@ attribute name="usePaging"%>
<%@ attribute name="addParam"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:if test="${!empty addParam}"><c:set var="addParam" value="&${addParam}"/></c:if>
<c:if test="${empty usePaging}"><c:set var="usePaging" value="${settingInfo.use_paging}"/></c:if>
<c:if test="${!empty pgInfo and usePaging eq '1'}">
<c:set var="currentPageNo" value="${pgInfo.currentPageNo}"/>
<c:set var="totalPageCount" value="${pgInfo.totalPageCount}"/>
<c:set var="pageSize" value="${pgInfo.pageSize}"/>
<c:set var="firstPageNo" value="${pgInfo.firstPageNo}"/>
<c:set var="lastPageNo" value="${pgInfo.lastPageNo}"/>
<c:set var="firstPageNoOnPageList" value="${pgInfo.firstPageNoOnPageList}"/>
<c:set var="lastPageNoOnPageList" value="${pgInfo.lastPageNoOnPageList}"/>
<c:set var="listUrl" value="${listUrl}${addParam}"/>
<c:set var="defaultListUrl" value="${listUrl}&${pageName}="/>
<c:if test="${totalPageCount > pageSize}">
	<c:choose>
	<c:when test="${firstPageNoOnPageList > pageSize}">
		<c:set var="prevPageNo" value="${firstPageNoOnPageList - 1}"/>
	</c:when>
	<c:otherwise>
		<c:set var="prevPageNo" value="${firstPageNo}"/>
	</c:otherwise>
	</c:choose>
	<li class="page-item page-first">
	<a href="<c:out value="${defaultListUrl}${firstPageNo}"/>" class="page-link" title="first"><img src="${contextPath}/${crtSiteId}/assets/images/arr_2x_gray.png" alt="처음으로 화살표" /></a>
	</li>
	<li class="page-item page-prev">
	<a href="<c:out value="${defaultListUrl}${prevPageNo}"/>" class="page-link" title="prev"><img src="${contextPath}/${crtSiteId}/assets/images/arr_bottom_gray.png" alt="이전으로 화살표" /></a>
	</li>
	</c:if>
	<c:forEach var="pageNo" begin="${firstPageNoOnPageList}" end="${lastPageNoOnPageList}">
	<c:choose>
	<c:when test="${pageNo == currentPageNo}"><li class="page-item active"><a class="page-link">${pageNo}</a></li></c:when>
	<c:otherwise>
		<li class="page-item"><a class="page-link" href="<c:out value="${defaultListUrl}${pageNo}"/>" title="${pageNo} 페이지">${pageNo}</a></li>
	</c:otherwise>
	</c:choose>
	</c:forEach>
	<c:if test="${totalPageCount > pageSize}">
		<c:choose>
		<c:when test="${lastPageNoOnPageList < totalPageCount}">
			<c:set var="nextPageNo" value="${firstPageNoOnPageList + pageSize}"/>
		</c:when>
		<c:otherwise>
			<c:set var="nextPageNo" value="${lastPageNo}"/>
		</c:otherwise>
		</c:choose>
	<li class="page-item page-next">
		<a href="<c:out value="${defaultListUrl}${lastPageNo}"/>" class="page-link" title="last"><img src="${contextPath}/${crtSiteId}/assets/images/arr_bottom_gray.png" alt="다음으로 화살표" /></a>
	</li>
	<li class="page-item page-last">
		<a href="<c:out value="${defaultListUrl}${lastPageNo}"/>" class="page-link" title="last"><img src="${contextPath}/${crtSiteId}/assets/images/arr_2x_gray.png" alt="이전으로 화살표" /></a>
	</li>
</c:if>
</c:if>