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
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
	<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
	<c:if test="${objVal1 == null}"><c:set var="objVal1" value="${objDt[itemColumnId1]}"/></c:if>
	<c:if test="${objVal2 == null}"><c:set var="objVal2" value="${objDt[itemColumnId2]}"/></c:if>
	
	<c:if test="${empty objStyle}"><c:set var="objStyle" value="width:80px;"/></c:if>
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	
	<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_${itemId}"/></c:if>
	<c:if test="${empty btnClass}"><c:set var="btnClass" value="btnCal"/></c:if>
	<c:if test="${empty btnText}"><c:set var="btnText" value="${itemObj['item_name']}"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	
	<spring:message var="ymdObjTitle" code="item.itemId.ymd.name" arguments="${itemName}"/>
	
	<spring:message var="hourObjTitle" code="item.itemId.hour.name" arguments="${itemName}"/>
	
	<spring:message var="itemHourName" code="item.hour.name"/>
	<spring:message var="hourOptTitle" code="item.itemId.select.name" arguments="${itemHourName}"/>

	<input type="text" id="${itemId}1" name="${itemId}1" title="${ymdObjTitle}" class="${objClass}" style="${objStyle}" readonly="readonly"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal1}"/>"/>
	<button type="button" id="${btnId}1" title="${btnText}" class="${btnClass}">${btnText}</button>
	<select id="${itemId}2" name="${itemId}2" title="${hourObjTitle}"<c:if test="${required == 1}"> required="required"</c:if>>
		<option value="">${hourOptTitle}</option>
		<c:forEach var="dateHour" begin="0" end="23">
		<c:set var="dateHourStr" value="${elfn:getLPAD(dateHour, '0', 2)}"/>
		<option value="${dateHourStr}"<c:if test="${objVal2 == dateHourStr}"> selected="selected"</c:if>>${dateHourStr}</option>
		</c:forEach>
	</select>
</c:if>