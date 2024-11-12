<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="itemId"%>
<%@ attribute name="itemName"%>
<%@ attribute name="id"%>
<%@ attribute name="required" type="java.lang.Integer"%>
<%@ attribute name="itemNameCRStr"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="objClass"%>
<%@ attribute name="preItemId"%>
<%@ attribute name="nextItemId"%>
<%@ attribute name="preStr"%>
<%@ attribute name="nextStr"%>
<%@ attribute name="removeItemName"%>											<%/* 항목명 중 삭제할 내용 */%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* label */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:if test="${empty required}">
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
</c:if>
<c:if test="${empty id}"><c:set var="id" value="${preItemId}${itemId}${nextItemId}"/></c:if>
<c:if test="${required == 1}"><c:if test="${!empty objClass}"><c:set var="objClass" value="${objClass} "/></c:if><c:set var="objClass" value="${objClass}required"/></c:if>
<label for="${id}"<c:if test="${!empty objStyle}"> style="${objStyle}"</c:if><c:if test="${!empty objClass}"> class="${objClass}"</c:if>><c:if test="${!isAdmMode && required == 1}"><em class="point01">(필수)</em></c:if><span>${preStr}<itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}" removeItemName="${removeItemName}" itemName="${itemName}" itemNameCRStr="${itemNameCRStr}"/>${nextStr}</span></label></c:if>