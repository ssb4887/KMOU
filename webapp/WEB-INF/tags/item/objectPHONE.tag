<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="objType"%>													<%/* 휴대전화인 경우 : 1 */%>
<%@ attribute name="objClass1"%>
<%@ attribute name="objClass2"%>
<%@ attribute name="objClass3"%>
<%@ attribute name="objStyle1"%>
<%@ attribute name="objStyle2"%>
<%@ attribute name="objStyle3"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal1"%>
<%@ attribute name="objVal2"%>
<%@ attribute name="objVal3"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 전화번호 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="usePrivSec" value="${itemObj['use_privsec']}"/>
	<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
	<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
	<c:set var="itemColumnId3" value="${itemObj['column_id']}3"/>
	<c:if test="${objVal1 == null && objVal2 == null && objVal3 == null}">
		<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
		<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
		<c:set var="objVal3" value="${objDt[itemColumnId3]}"/>
	</c:if>
	<c:if test="${usePrivSec == 1}">
		<c:set var="objVal1" value="${elfn:privDecrypt(objVal1)}"/>
		<c:set var="objVal2" value="${elfn:privDecrypt(objVal2)}"/>
		<c:set var="objVal3" value="${elfn:privDecrypt(objVal3)}"/>
	</c:if>

	<c:if test="${empty objStyle1}"><c:set var="objStyle1" value="width:100px;"/></c:if>
	<c:if test="${empty objStyle2}"><c:set var="objStyle2" value="width:100px;"/></c:if>
	<c:if test="${empty objStyle3}"><c:set var="objStyle3" value="width:100px;"/></c:if>
	<c:if test="${empty objClass2}"><c:set var="objClass2" value="inputTxt"/></c:if>
	<c:if test="${empty objClass3}"><c:set var="objClass3" value="inputTxt"/></c:if>
	
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>

	<spring:message var="itemName1" code="item.itemId.phone${objType}.first.name" arguments="${itemName}"/>
	<spring:message var="itemName2" code="item.itemId.phone${objType}.second.name" arguments="${itemName}"/>
	<spring:message var="itemName3" code="item.itemId.phone${objType}.third.name" arguments="${itemName}"/>
	<spring:message var="itemOptTitle" code="item.select.name"/>
	<itui:objectPhoneSelect objId="${itemId}1" objName="${itemId}1" objTitle="${itemName1}" objOptTitle="${itemOptTitle}" objClass="${objClass1}" objStyle="${objStyle1}" objType="${objType}" required="${required}" objVal="${objVal1}"/>
	<span>-</span>
	<input type="text" id="${itemId}2" name="${itemId}2" title="${itemName2}" class="${objClass2}" style="ime-mode:disabled;${objStyle2}" maxlength="4"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal2}"/>"/>
	<span>-</span>
	<input type="text" id="${itemId}3" name="${itemId}3" title="${itemName3}" class="${objClass3}" style="ime-mode:disabled;${objStyle3}" maxlength="4"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal3}"/>"/>
</c:if>