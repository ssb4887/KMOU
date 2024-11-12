<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="itemId"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 이전 비밀번호 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objStyle}"><c:set var="objStyle" value="${itemObj['object_style']}"/></c:if>
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<input type="password" id="pre_${itemId}" name="pre_${itemId}" autocomplete="off" class="${objClass}" style="${objStyle}"<c:if test="${itemObj['maximum'] > 0}"> maxlength="${itemObj['maximum']}"</c:if><c:if test="${required == 1}"> required="required"</c:if>/>
</c:if>