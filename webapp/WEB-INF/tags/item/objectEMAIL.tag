<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="objClass1"%>
<%@ attribute name="objClass2"%>
<%@ attribute name="objClass3"%>
<%@ attribute name="objStyle1"%>
<%@ attribute name="objStyle2"%>
<%@ attribute name="objStyle3"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="dupConfirm"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnText"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 이메일 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="usePrivSec" value="${itemObj['use_privsec']}"/>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${empty objVal}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:if test="${!empty objVal && usePrivSec == 1}"><c:set var="objVal" value="${elfn:privDecrypt(objVal)}"/></c:if>
	
	<c:if test="${empty objStyle1}"><c:set var="objStyle1" value="width:100px;"/></c:if>
	<c:if test="${empty objStyle2}"><c:set var="objStyle2" value="width:100px;"/></c:if>
	<c:if test="${empty objStyle3}"><c:set var="objStyle3" value="width:100px;"/></c:if>
	<c:if test="${empty objClass1}"><c:set var="objClass1" value="inputTxt"/></c:if>
	<c:if test="${empty objClass2}"><c:set var="objClass2" value="inputTxt"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	
	<c:if test="${!empty objVal}">
	<c:set var="emailVals" value="${fn:split(objVal, '@')}"/>
	<c:choose>
		<c:when test="${fn:length(emailVals) == 2}">
			<c:set var="objVal1" value="${emailVals[0]}"/>
			<c:set var="objVal2" value="${emailVals[1]}"/>
		</c:when>
		<c:otherwise>
			<c:set var="objVal1" value="${emailVals[0]}"/>
		</c:otherwise>
	</c:choose>
	</c:if>
	<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_${itemId}"/></c:if>
	<c:if test="${empty btnClass}"><c:set var="btnClass" value="btnTypeF"/></c:if>
	<c:if test="${empty btnText}"><spring:message var="btnText" code="item.duplicate.check"/></c:if>
	
	<div class="input-email">
		<input type="hidden" id="${itemId}" name="${itemId}" title="${itemName}" value="<c:out value="${objVal}"/>"/>
		<input type="text" id="${itemId}1" name="${itemId}1" title="<spring:message code="item.itemId.email.first.name" arguments="${itemName}"/>" class="${objClass1}" style="ime-mode:disabled;${objStyle1}"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal1}"/>"/>
		<span class="input-group-sp">@</span>
		<c:set var="emailOpts" value="hanmail.net,yahoo.co.kr,hanafos.com,empal.com,dreamwiz.com,naver.com,hotmail.com,freechal.com,chol.com,paran.com,gmail.com"/>
		<input type="text" id="${itemId}2" name="${itemId}2" title="<spring:message code="item.itemId.email.second.name" arguments="${itemName}"/>" class="${objClass2}" style="ime-mode:disabled;${objStyle2}" readonly="readonly"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal2}"/>" data-readonly="1"/>
		<select id="${itemId}3" name="${itemId}3" title="<spring:message code="item.itemId.email.second.select.name" arguments="${itemName}"/>" class="${objClass3}" style="${objStyle3}"<c:if test="${required == 1}"> required="required"</c:if>>
			<option value=""><spring:message code="item.select.name"/></option>
			<c:set var="isOptionSel" value="${false}"/>
			<c:forTokens var="emailOpt" items="${emailOpts}" delims=",">
			<option value="${emailOpt}"<c:if test="${emailOpt == objVal2}"> selected="selected"<c:set var="isOptionSel" value="${true}"/></c:if>>${emailOpt}</option>
			</c:forTokens>
			<option value="0"<c:if test="${empty objVal || !empty objVal2 && !isOptionSel}"> selected="selected"</c:if>>직접입력</option>
		</select>
	</div>
	<c:if test="${dupConfirm eq '1'}">
		<button type="button" id="${btnId}" title="${btnText}" class="${btnClass}">${btnText}</button>
	</c:if>
</c:if>