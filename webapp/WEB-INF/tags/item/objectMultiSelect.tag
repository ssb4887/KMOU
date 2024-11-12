<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objValList" type="java.util.List"%>
<%@ attribute name="defVal"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 다중 select */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty optnHashMap}"><c:set var="optnHashMap" value="${optnHashMap}"/></c:if>
	<c:if test="${objValList == null}"><c:set var="objValList" value="${multiDataHashMap[itemId]}"/></c:if>
	<c:if test="${defVal == null}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>	
	<c:if test="${!empty defVal}"><c:set var="defVals" value="${fn:split(defVal,',')}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:choose>
		<c:when test="${!empty itemObj.option_type && itemObj.option_type != 0}"><c:set var="optnOptList" value="${optnHashMap[itemId]}"/></c:when>
		<c:otherwise><c:set var="optnOptList" value="${optnHashMap[itemObj['master_code']]}"/></c:otherwise>
	</c:choose>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
<select id="${itemId}" name="${itemId}" title="${itemName}" style="${objStyle}" multiple="multiple"<c:if test="${required == 1}"> required="required"</c:if>>
<c:forEach var="optnDt" items="${optnOptList}" varStatus="i">
	<c:set var="objValDt" value="${elfn:getMatchHashMap(objValList, optnDt.OPTION_CODE, 'ITEM_KEY')}"/>
	<option value="<c:out value="${optnDt.OPTION_CODE}"/>"<c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if><c:if test="${!empty objValDt || empty objValList && elfn:arrayIndexOf(defVals, optnDt.OPTION_CODE) != -1}"> selected="selected"</c:if>><c:out value="${optnDt.OPTION_NAME}"/></option>
</c:forEach>
</select>
</c:if>