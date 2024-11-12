<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="var" rtexprvalue="false"%>
<%@ attribute name="itemId"%>
<%@ attribute name="itemName"%>
<%@ attribute name="removeItemName"%>											<%/* 항목명 중 삭제할 내용 */%>
<%@ attribute name="itemNameCRStr"%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:choose>
		<c:when test="${!empty itemName}"><c:set var="itemName" value="${itemName}"/></c:when>
		<c:otherwise><c:set var="itemName" value="${elfn:getItemName(itemObj)}"/></c:otherwise>
	</c:choose>
	<c:if test="${!empty removeItemName}">
		<c:set var="itemName" value="${fn:replace(itemName, removeItemName, '')}"/>
	</c:if>
	<c:choose>
	<c:when test="${!empty var}">
	${elfn:setAttribute(var, itemName)}
	</c:when>
	<c:when test="${!empty itemNameCRStr}">
		<c:set var="replaceStr" value="<br/>${itemNameCRStr}"/>
		<c:out value="${fn:replace(itemName, itemNameCRStr, replaceStr)}" escapeXml="false"/>
	</c:when>
	<c:otherwise><c:out value="${itemName}"/></c:otherwise>
	</c:choose>
</c:if>