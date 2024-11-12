<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objValOptList" type="java.util.List"%>
<%@ attribute name="objValList" type="java.util.List"%>
<%@ attribute name="objValArray" type="java.lang.String[]"%>
<%@ attribute name="name"%>
<%@ attribute name="id"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 다중 select + button */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty optnHashMap}"><c:set var="optnHashMap" value="${optnHashMap}"/></c:if>
	<c:set var="objCValList" value="${objValList}"/>
	<c:if test="${!empty objValArray}"><c:set var="objCValList" value="${objValArray}"/></c:if>
	<c:if test="${objCValList == null}"><c:set var="objCValList" value="${multiDataHashMap[itemId]}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
<select id="${id}" name="${name}" title="${itemName}" style="${objStyle}" multiple="multiple"<c:if test="${required == 1}"> required="required"</c:if>>
<c:choose>
	<c:when test="${!empty objValOptList}">
		<c:forEach var="optnDt" items="${objValOptList}" varStatus="i">
			<option value="<c:out value="${optnDt.OPTION_CODE}"/>"<c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if>><c:out value="${optnDt.OPTION_NAME}"/></option>
		</c:forEach>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${!empty itemObj.option_type && itemObj.option_type != 0}"><c:set var="optnOptList" value="${optnHashMap[itemId]}"/></c:when>
			<c:otherwise><c:set var="optnOptList" value="${optnHashMap[itemObj['master_code']]}"/></c:otherwise>
		</c:choose>
		<c:forEach var="objVal" items="${objCValList}" varStatus="i">
			<c:set var="optnDt" value="${elfn:getMatchHashMap(optnOptList, objVal, 'OPTION_CODE')}"/>
			<c:if test="${!empty optnDt}"><option value="<c:out value="${optnDt.OPTION_CODE}"/>"<c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if>><c:out value="${optnDt.OPTION_NAME}"/></option></c:if>
		</c:forEach>
	</c:otherwise>
</c:choose>
</select>
<ul class="fn_btn_ad">
	<li><button type="button" class="fn_btn_add" id="fn_btn_add_${id}" data-id="${id}"><spring:message code="item.add.name"/></button></li>
	<li><button type="button" class="fn_btn_del" id="fn_btn_del_${id}" data-id="${id}"><spring:message code="item.del.name"/></button></li>
</ul>
</c:if>