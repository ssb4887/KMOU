<%@tag import="net.sf.json.JSONObject"%>
<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="itemOrder" type="net.sf.json.JSONArray"%>
<%@ attribute name="items" type="net.sf.json.JSONObject"%>
<%@ attribute name="selectEtcVal"%>
<c:if test="${!empty items && !empty itemOrder}">
<spring:message var="itemSearchPreFlag" code="Globals.item.search.pre.flag"/>
<c:set var="requiredName" value="required_${submitType}"/>
<c:forEach var="itemId" items="${itemOrder}">
	<c:set var="itemKeyId" value="${itemSearchPreFlag}${itemId}"/>
	<c:set var="itemObj" value="${items[itemId]}"/>
	<c:set var="formatType" value="${itemObj['format_type']}"/>
	<c:set var="objectType" value="${itemObj['object_type']}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<c:set var="itemName" value="${itemObj['item_name']}"/>
	<c:choose>
		<c:when test="${formatType >= 5 && formatType <= 8 || formatType >= 15 && formatType <= 18}">
		<%/* 날짜(기간) / 날짜 시:분:초(기간) / 날짜 시:분(기간) / 날짜 시(기간) */%>
		try{$("#fn_btn_${itemKeyId}1").click(function(event){displayCalendar2($('#${itemKeyId}1'), $('#${itemKeyId}2'), '', this);return false;});}catch(e){}
		try{$("#fn_btn_${itemKeyId}2").click(function(event){displayCalendar2($('#${itemKeyId}2'), $('#${itemKeyId}1'), '', this);return false;});}catch(e){}
		</c:when>
	</c:choose>
		
</c:forEach>
<c:if test="${!empty limitCount}">
	${limitCount}};
</c:if>
<c:if test="${!empty limitExt}">
	${limitExt}};
</c:if>
</c:if>