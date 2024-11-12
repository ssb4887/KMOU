<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="itemId"%>
<%@ attribute name="id"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal" type="java.lang.Object"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnText"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:if test="${objVal != null && itemObj['use_privsec'] == 1}"><c:set var="objVal" value="${elfn:privDecrypt(objVal)}"/></c:if>
	<c:if test="${empty objStyle}"><c:set var="objStyle" value="width:80px;"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	
	<c:if test="${empty objStyle}"><c:set var="objStyle" value="${itemObj['object_style']}"/></c:if>
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	
	<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_${id}"/></c:if>
	<c:if test="${empty btnClass}"><c:set var="btnClass" value="btnCal"/></c:if>
	<c:if test="${empty btnText}"><c:set var="btnText" value="${itemObj['item_name']}"/></c:if>
	
	<c:if test="${itemObj['date_type'] == 1}"><fmt:formatDate var="objVal" value="${objVal}" pattern="yyyy-MM-dd"/></c:if>
	<div class="input-group">		
		<input type="text" id="${id}" name="${itemId}" title="${itemName}" class="${objClass}" style="${objStyle}" readonly="readonly"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal}"/>"/>
		<span><button type="button" id="${btnId}" title="${btnText}" class="${btnClass}">${btnText}</button></span>
	</div>
</c:if>