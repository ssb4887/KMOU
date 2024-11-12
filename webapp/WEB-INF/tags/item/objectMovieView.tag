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
	<c:choose>
		<c:when test="${!empty objDt}">
		<%/* file */%>
			<c:set var="savedColumnId" value="${itemColumnId}_SAVED_NAME"/>
			<c:if test="${!empty objDt[savedColumnId]}">
				<c:set var="keyItemId" value="${settingInfo.idx_name}"/>
				<c:set var="keyColumnId" value="${settingInfo.idx_column}"/>
				<c:set var="originColumnId" value="${itemColumnId}_ORIGIN_NAME"/>
				<c:set var="textColumnId" value="${itemColumnId}_TEXT"/>
				<spring:message var="imgRootPath" code="Globals.image.domain.path" text=""/>
				<c:choose>
					<c:when test="${!empty imgRootPath}">
						<c:set var="moviePath" value="${imgRootPath}/${crtMenu.module_id}/${crtMenu.fn_idx}/${objDt[savedColumnId]}"/>
					</c:when>
					<c:otherwise>
						<c:set var="moviePath" value="${URL_MOVIE}&id=${elfn:imgNSeedEncrypt(objDt[savedColumnId])}"/>
					</c:otherwise>
				</c:choose>
				<c:choose>
				<c:when test="${fn:endsWith(objDt[savedColumnId], '.mp4')}">
				<div style="margin-top:5px;">
					<video controls style="max-width:100%;">
						<source src="<c:out value="${moviePath}"/>" type="video/mp4">
						<object classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" width="100%" height="500px">
							<param name="filename" value="<c:out value="${moviePath}"/>">
							<param name="autoplay" value="false">							
							<embed src="<c:out value="${moviePath}"/>" height="500px" width="100%" autostart="0" showcontrols="1"></embed>
						</object>
						<c:if test="${!empty objDt[textColumnId]}"><p class="fn_movieTxt"><c:out value="${objDt[textColumnId]}"/></p></c:if>
					</video>
				</div>
				</c:when>
				<c:otherwise>
				<object classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" width="100%" height="500px"codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#version=5,1,52,701" standby="loading microsoft windows media player components..." type="application/x-oleobject">
					<param name="filename" value="<c:out value="${moviePath}"/>">
					<param name="autostart" value="false">
					<embed src="<c:out value="${moviePath}"/>" height="500px" width="100%" autostart="0" showcontrols="1" pluginspage="http://www.microsoft.com/korea/windows/windowsmedia/"></embed>
				</object>
				<c:if test="${!empty objDt[textColumnId]}"><p class="fn_movieTxt"><c:out value="${objDt[textColumnId]}"/></p></c:if>
				</c:otherwise>
				</c:choose>
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
				<spring:message var="imgRootPath" code="Globals.image.domain.path" text=""/>
				<c:forEach var="optnDt" items="${fileList}" varStatus="i">
					<c:choose>
						<c:when test="${!empty imgRootPath}">
							<c:set var="moviePath" value="${imgRootPath}/${crtMenu.module_id}/${crtMenu.fn_idx}/${optnDt[savedColumnId]}"/>
						</c:when>
						<c:otherwise>
							<c:set var="moviePath" value="${URL_MOVIE}"/>&id=<c:out value="${elfn:imgNSeedEncrypt(optnDt[savedColumnId])}"/>
						</c:otherwise>
					</c:choose>
					<c:choose>
					<c:when test="${fn:endsWith(optnDt[savedColumnId], '.mp4')}">
					<li>
						<video controls style="max-width:100%;">
							<source src="<c:out value="${moviePath}"/>" type="video/mp4">
							<object classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" width="100%" height="500px">
								<param name="filename" value="<c:out value="${moviePath}"/>">
								<param name="autoplay" value="false">
								<embed src="<c:out value="${moviePath}"/>" height="500px" width="100%" autostart="0" showcontrols="1"></embed>
							</object>
							<c:if test="${!empty optnDt[textColumnId]}"><p class="fn_movieTxt"><c:out value="${optnDt[textColumnId]}"/></p></c:if>
						</video>
					</li>
					</c:when>
					<c:otherwise>
					<li>
						<object classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" width="100%" height="500px">
							<param name="filename" value="<c:out value="${moviePath}"/>">
							<param name="autostart" value="false">
							<embed src="<c:out value="${moviePath}"/>" height="500px" width="100%" autostart="0" showcontrols="1"></embed>
						</object>
						<c:if test="${!empty optnDt[textColumnId]}"><p class="fn_movieTxt"><c:out value="${optnDt[textColumnId]}"/></p></c:if>
					</li>
					</c:otherwise>
					</c:choose>
				</c:forEach>
				</ul>
			</c:if>
		</c:when>
	</c:choose>
</c:if>