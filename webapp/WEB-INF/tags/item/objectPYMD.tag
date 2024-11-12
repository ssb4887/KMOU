<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal1"%>
<%@ attribute name="objVal2"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnText"%>
<%@ attribute name="id"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 일자(기간) */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
	<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
	<c:if test="${objVal1 == null}"><c:set var="objVal1" value="${objDt[itemColumnId1]}"/></c:if>
	<c:if test="${objVal2 == null}"><c:set var="objVal2" value="${objDt[itemColumnId2]}"/></c:if>
	<c:if test="${empty objStyle}"><c:set var="objStyle" value="width:80px;"/></c:if>
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	
	<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_${id}"/></c:if>
	<c:if test="${empty btnClass}"><c:set var="btnClass" value="btnCal"/></c:if>
	<c:if test="${empty btnText}"><c:set var="btnText" value="${itemName}"/></c:if>
	<spring:message var="itemObjBeginTitle" code="item.itemId.begin.name" arguments="${itemName}"/>
	<spring:message var="itemObjEndTitle" code="item.itemId.end.name" arguments="${itemName}"/>
	<div class="input-group">
		<input type="text" id="${id}1" name="${itemId}1" title="${itemObjBeginTitle}" class="${objClass}" style="${objStyle}" readonly="readonly"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal1}"/>"/>
		<span><button type="button" id="${btnId}1" title="${btnText}" class="${btnClass}">${btnText}</button></span>
	</div>
	<span class="input-group-sp">~</span>
	<div class="input-group">
		<input type="text" id="${id}2" name="${itemId}2" title="${itemObjEndTitle}" class="${objClass}" style="${objStyle}" readonly="readonly"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal2}"/>"/>
		<span><button type="button" id="${btnId}2" title="${btnText}" class="${btnClass}">${btnText}</button></span>
	</div>
</c:if>