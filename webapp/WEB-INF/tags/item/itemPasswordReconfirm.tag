<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="itemId"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 항목(th,td) 설정별 출력 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:if test="${empty objStyle}"><c:set var="objStyle" value="${itemObj['object_style']}"/></c:if>
<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>

<c:set var="requiredName" value="required_${inputFlag}"/>
<c:set var="required" value="${itemObj[requiredName]}"/>
<spring:message var="itemName" code="item.password_reconfirm.name" arguments="${elfn:getItemName(itemObj)}"/>
<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}" id="${itemId}_reconfirm" required="${required}" itemName="${itemName}"/></th>
<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
	<itui:objectPasswordReconfirm itemId="${itemId}" itemInfo="${itemInfo}" objClass="${objClass}" objStyle="${objStyle}"/>
</td>
</c:if>