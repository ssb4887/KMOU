<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal11"%>
<%@ attribute name="objVal12"%>
<%@ attribute name="objVal13"%>
<%@ attribute name="objVal21"%>
<%@ attribute name="objVal22"%>
<%@ attribute name="objVal23"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnText"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 일자 시간 분(기간) */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId11" value="${itemObj['column_id']}11"/>
	<c:set var="itemColumnId12" value="${itemObj['column_id']}12"/>
	<c:set var="itemColumnId13" value="${itemObj['column_id']}13"/>
	<c:set var="itemColumnId21" value="${itemObj['column_id']}21"/>
	<c:set var="itemColumnId22" value="${itemObj['column_id']}22"/>
	<c:set var="itemColumnId23" value="${itemObj['column_id']}23"/>
	<c:if test="${objVal11 == null}"><c:set var="objVal11" value="${objDt[itemColumnId11]}"/></c:if>
	<c:if test="${objVal12 == null}"><c:set var="objVal12" value="${objDt[itemColumnId12]}"/></c:if>
	<c:if test="${objVal13 == null}"><c:set var="objVal13" value="${objDt[itemColumnId13]}"/></c:if>
	<c:if test="${objVal21 == null}"><c:set var="objVal21" value="${objDt[itemColumnId21]}"/></c:if>
	<c:if test="${objVal22 == null}"><c:set var="objVal22" value="${objDt[itemColumnId22]}"/></c:if>
	<c:if test="${objVal23 == null}"><c:set var="objVal23" value="${objDt[itemColumnId23]}"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	
	<c:if test="${empty objStyle}"><c:set var="objStyle" value="width:80px;"/></c:if>
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	
	<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_${itemId}"/></c:if>
	<c:if test="${empty btnClass}"><c:set var="btnClass" value="btnCal"/></c:if>
	<c:if test="${empty btnText}"><c:set var="btnText" value="${itemName}"/></c:if>
	
	<spring:message var="ymdObjTitle" code="item.itemId.ymd.name" arguments="${itemName}"/>
	<spring:message var="ymdObjBeginTitle" code="item.itemId.begin.name" arguments="${ymdObjTitle}"/>
	<spring:message var="ymdObjEndTitle" code="item.itemId.end.name" arguments="${ymdObjTitle}"/>
	
	<spring:message var="hourObjTitle" code="item.itemId.hour.name" arguments="${itemName}"/>
	<spring:message var="hourObjBeginTitle" code="item.itemId.begin.name" arguments="${hourObjTitle}"/>
	<spring:message var="hourObjEndTitle" code="item.itemId.end.name" arguments="${hourObjTitle}"/>
	
	<spring:message var="itemHourName" code="item.hour.name"/>
	<spring:message var="hourOptTitle" code="item.itemId.select.name" arguments="${itemHourName}"/>
	
	<spring:message var="minuteObjTitle" code="item.itemId.minute.name" arguments="${itemName}"/>
	<spring:message var="minuteObjBeginTitle" code="item.itemId.begin.name" arguments="${minuteObjTitle}"/>
	<spring:message var="minuteObjEndTitle" code="item.itemId.end.name" arguments="${minuteObjTitle}"/>
	
	<spring:message var="itemMinuteName" code="item.minute.name"/>
	<spring:message var="minuteOptTitle" code="item.itemId.select.name" arguments="${itemMinuteName}"/>
	<div class="input-group">
	<input type="text" id="${itemId}11" name="${itemId}11" title="${ymdObjBeginTitle}" class="${objClass}" style="${objStyle}" readonly="readonly"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal11}"/>"/>
	<span><button type="button" id="${btnId}11" title="${btnText}" class="${btnClass}">${btnText}</button></span>
	<select id="${itemId}12" name="${itemId}12" title="${hourObjBeginTitle}"<c:if test="${required == 1}"> required="required"</c:if>>
		<option value="">${hourOptTitle}</option>
		<c:forEach var="dateHour" begin="0" end="23">
		<c:set var="dateHourStr" value="${elfn:getLPAD(dateHour, '0', 2)}"/>
		<option value="${dateHourStr}"<c:if test="${objVal12 == dateHourStr}"> selected="selected"</c:if>>${dateHourStr}</option>
		</c:forEach>
	</select>
	<span>:</span>
	<select id="${itemId}13" name="${itemId}13" title="${minuteObjBeginTitle}"<c:if test="${required == 1}"> required="required"</c:if>>
		<option value="">${minuteOptTitle}</option>
		<c:forEach var="dateMin" begin="0" end="59">
		<c:set var="dateMinStr" value="${elfn:getLPAD(dateMin, '0', 2)}"/>
		<option value="${dateMinStr}"<c:if test="${objVal13 == dateMinStr}"> selected="selected"</c:if>>${dateMinStr}</option>
		</c:forEach>
	</select>
	</div>
	<span class="input-group-sp">~</span>
	<div class="input-group">
	<input type="text" id="${itemId}21" name="${itemId}21" title="${ymdObjEndTitle}" class="${objClass}" style="${objStyle}" readonly="readonly"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal21}"/>"/>
	<span><button type="button" id="${btnId}21" title="${btnText}" class="${btnClass}">${btnText}</button></span>
	<select id="${itemId}22" name="${itemId}22" title="${hourObjEndTitle}"<c:if test="${required == 1}"> required="required"</c:if>>
		<option value="">${hourOptTitle}</option>
		<c:forEach var="dateHour" begin="0" end="23" varStatus="mi">
		<c:set var="dateHourStr" value="${elfn:getLPAD(dateHour, '0', 2)}"/>
		<option value="${dateHourStr}"<c:if test="${objVal22 == dateHourStr}"> selected="selected"</c:if>>${dateHourStr}</option>
		</c:forEach>
	</select>
	<span>:</span>
	<select id="${itemId}23" name="${itemId}23" title="${minuteObjEndTitle}"<c:if test="${required == 1}"> required="required"</c:if>>
		<option value="">${minuteOptTitle}</option>
		<c:forEach var="dateMin" begin="0" end="59" varStatus="mi">
		<c:set var="dateMinStr" value="${elfn:getLPAD(dateMin, '0', 2)}"/>
		<option value="${dateMinStr}"<c:if test="${objVal23 == dateMinStr}"> selected="selected"</c:if>>${dateMinStr}</option>
		</c:forEach>
	</select>
	</div>
</c:if>