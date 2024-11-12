<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="objType"%>
<%@ attribute name="objId"%>
<%@ attribute name="objName"%>
<%@ attribute name="objTitle"%>
<%@ attribute name="objOptTitle"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="objVal"%>
<%@ attribute name="required"%>
<c:choose>
	<c:when test="${objType == '1'}">
		<c:set var="phoneNums" value="010,011,016,017,018,019"/>
	</c:when>
	<c:otherwise>
		<c:set var="phoneNums" value="02,031,032,033,041,042,043,044,051,052,053,054,055,061,062,063,064,0502,0504,0505,0506,070"/>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${empty objOptTitle && !empty objTitle}"><spring:message var="objOptTitle" code="item.itemId.select.name" arguments="${objTitle}"/></c:when>
	<c:when test="${empty objOptTitle && empty objTitle}"><spring:message var="objOptTitle" code="item.select.name"/></c:when>
</c:choose> 
<select id="${objId}" name="${objName}"<c:if test="${!empty objTitle}"> title="${objTitle}"</c:if> class="${objClass}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${objOptTitle}</option>
	<c:forTokens var="phoneNum" items="${phoneNums}" delims=",">
	<option value="${phoneNum}"<c:if test="${phoneNum == objVal}"> selected="selected"</c:if>>${phoneNum}</option>
	</c:forTokens>
</select>