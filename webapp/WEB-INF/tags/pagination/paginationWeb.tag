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
<a href="<c:out value="${defaultListUrl}${firstPageNo}"/>" class="first" title="처음 페이지"><span class="sr-only">처음페이지</span></a>
<a href="<c:out value="${defaultListUrl}${prevPageNo}"/>" class="prev" title="이전 페이지"><span class="sr-only">이전페이지</span></a>
</c:if>
<c:forEach var="pageNo" begin="${firstPageNoOnPageList}" end="${lastPageNoOnPageList}">
	<c:choose>
		<c:when test="${pageNo == currentPageNo}">
			<span><a href="<c:out value="${defaultListUrl}${pageNo}"/>" class="active" title="${pageNo} 페이지">${pageNo}</a></span>
		</c:when>
		<c:otherwise>
			<span><a href="<c:out value="${defaultListUrl}${pageNo}"/>" title="${pageNo} 페이지">${pageNo}</a></span>
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
<a href="<c:out value="${defaultListUrl}${nextPageNo}"/>" class="next" title="다음 페이지"><span class="sr-only">다음페이지</span></a>
<a href="<c:out value="${defaultListUrl}${lastPageNo}"/>" class="last" title="끝 페이지"><span class="sr-only">마지막페이지</span></a>
</c:if>
</c:if>