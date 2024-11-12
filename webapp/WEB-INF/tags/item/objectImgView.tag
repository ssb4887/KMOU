<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<%@ attribute name="multiFileHashMap" type="java.util.HashMap"%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<spring:message var="imgRootPath" code="Globals.image.domain.path" text=""/>
	<c:choose>
		<c:when test="${!empty objDt}">
		<%/* file */%>
			<c:set var="savedColumnId" value="${itemColumnId}_SAVED_NAME"/>
			<c:if test="${!empty objDt[savedColumnId]}">
				<c:set var="keyItemId" value="${settingInfo.idx_name}"/>
				<c:set var="keyColumnId" value="${settingInfo.idx_column}"/>
				<c:set var="originColumnId" value="${itemColumnId}_ORIGIN_NAME"/>
				<c:set var="textColumnId" value="${itemColumnId}_TEXT"/>
				<c:choose>
					<c:when test="${!empty imgRootPath}">
						<c:set var="imagePath" value="${imgRootPath}/${crtMenu.module_id}/${crtMenu.fn_idx}/${objDt[savedColumnId]}"/>
					</c:when>
					<c:otherwise>
						<c:set var="imagePath" value="${URL_IMAGE}&id=${elfn:imgNSeedEncrypt(objDt[savedColumnId])}"/>
					</c:otherwise>
				</c:choose>
				<img src="<c:out value="${imagePath}"/>" alt="${objDt[textColumnId]}"/>
			</c:if>
		</c:when>
		<c:when test="${!empty multiFileHashMap}">
		<%/* multi file */%>
			<c:set var="fileList" value="${multiFileHashMap[itemId]}"/>
			<c:if test="${!empty fileList}">
				<c:set var="keyItemId" value="${settingInfo.idx_name}"/>
				<c:set var="keyColumnId" value="${settingInfo.idx_column}"/>
				<c:set var="originColumnId" value="${itemColumnId}_ORIGIN_NAME"/>
				<c:set var="savedColumnId" value="${itemColumnId}_SAVED_NAME"/>
				<c:set var="textColumnId" value="${itemColumnId}_TEXT"/>
				<ul>
				<c:forEach var="optnDt" items="${fileList}" varStatus="i">
				<c:choose>
					<c:when test="${!empty imgRootPath}">
						<c:set var="imagePath" value="${imgRootPath}/${crtMenu.module_id}/${crtMenu.fn_idx}/${optnDt[savedColumnId]}"/>
					</c:when>
					<c:otherwise>
						<c:set var="imagePath" value="${URL_IMAGE}"/>&id=<c:out value="${elfn:imgNSeedEncrypt(optnDt[savedColumnId])}"/>
					</c:otherwise>
				</c:choose>
					<li><img src="<c:out value="${imagePath}"/>" alt="${optnDt[textColumnId]}"/></li>
				</c:forEach>
				</ul>
			</c:if>
		</c:when>
	</c:choose>
</c:if>