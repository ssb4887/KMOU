<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
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
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnText"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 주소 */ %>
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
	
	<c:if test="${empty objStyle1}"><c:set var="objStyle1" value="width:80px;"/></c:if>
	
	<c:if test="${empty objClass1}"><c:set var="objClass1" value="inputTxt"/></c:if>
	<c:if test="${empty objClass2}"><c:set var="objClass2" value="inputTxt wFull"/></c:if>
	<c:if test="${empty objClass3}"><c:set var="objClass3" value="inputTxt wFull"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	
	<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_${itemId}"/></c:if>
	<c:if test="${empty btnClass}"><c:set var="btnClass" value="btnTypeF"/></c:if>
	<c:if test="${empty btnText}">
		<c:set var="addrSearchType" value="${itemObj['search_type']}"/>
		<c:choose>
			<c:when test="${addrSearchType == 'map'}">
				<spring:message var="btnText" code="item.search.name"/>
				<c:set var="objClass2" value="${objClass2} addrMap"/>
				<input type="hidden" id="${itemId}1" name="${itemId}1" title="<spring:message code="item.itemId.addr.zipcode.name" arguments="${itemName}"/>" value="<c:out value="${objVal1}"/>"/>
			</c:when>
			<c:otherwise><spring:message var="btnText" code="item.search.addr.name"/></c:otherwise>
		</c:choose>
	</c:if>
	<ul>
		<c:if test="${addrSearchType != 'map'}">
		<li>
			<input type="text" id="${itemId}1" name="${itemId}1" title="<spring:message code="item.itemId.addr.zipcode.name" arguments="${itemName}"/>" class="${objClass1}" style="${objStyle1}"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal1}"/>" readonly="readonly"/>
			<button type="button" id="${btnId}" title="${btnText}" class="${btnClass}">${btnText}</button>
		</li>
		</c:if>
		<li>
			<div class="input-group-full">
			<input type="text" id="${itemId}2" name="${itemId}2" title="<spring:message code="item.itemId.addr.first.name" arguments="${itemName}"/>" class="${objClass2}" style="${objStyle2}"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal2}"/>"/>
			<c:if test="${addrSearchType == 'map'}">
				<span><button type="button" id="${btnId}" title="${btnText}" class="${btnClass}">${btnText}</button></span>
			</c:if>
			</div>
		</li>
		<li>
			<input type="text" id="${itemId}3" name="${itemId}3" title="<spring:message code="item.itemId.addr.second.name" arguments="${itemName}"/>" class="${objClass3}" style="${objStyle3}"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal3}"/>"/>
		</li>
	</ul>
</c:if>