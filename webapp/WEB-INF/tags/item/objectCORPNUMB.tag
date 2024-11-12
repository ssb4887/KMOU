<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="objClass1"%>
<%@ attribute name="objClass2"%>
<%@ attribute name="objStyle1"%>
<%@ attribute name="objStyle2"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 법인번호 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="usePrivSec" value="${itemObj['use_privsec']}"/>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}">
		<c:set var="objVal" value="${objDt[itemColumnId]}"/>
	</c:if>
	<c:if test="${usePrivSec == 1}">
		<c:set var="objVal" value="${elfn:privDecrypt(objVal)}"/>
	</c:if>
	<c:set var="objVal1" value=""/>
	<c:set var="objVal2" value=""/>
	<c:if test="${!empty objVal}">
	<c:set var="objVals" value="${fn:split(objVal, '-')}"/>
	<c:set var="objVal1" value="${objVals[0]}"/>
	<c:set var="objVal2" value="${objVals[1]}"/>
	</c:if>

	<c:if test="${empty objStyle1}"><c:set var="objStyle1" value="width:100px;"/></c:if>
	<c:if test="${empty objStyle2}"><c:set var="objStyle2" value="width:100px;"/></c:if>
	<c:if test="${empty objClass1}"><c:set var="objClass1" value="inputTxt"/></c:if>
	<c:if test="${empty objClass2}"><c:set var="objClass2" value="inputTxt"/></c:if>
	
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>

	<input type="text" id="${itemId}1" name="${itemId}1" title="${itemName}" class="${objClass1}" style="ime-mode:disabled;${objStyle1}" maxlength="6"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal1}"/>"/>
	<span>-</span>
	<input type="text" id="${itemId}2" name="${itemId}2" title="${itemName}" class="${objClass2}" style="ime-mode:disabled;${objStyle2}" maxlength="7"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal2}"/>"/>
</c:if>