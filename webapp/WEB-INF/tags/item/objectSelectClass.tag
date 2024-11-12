<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="id"%>
<%@ attribute name="name"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="addOrder" type="java.lang.Boolean"%>
<%@ attribute name="idxColumnId"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:if test="${empty addOrder}">
	<c:set var="addOrder" value="false"/>
</c:if>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:if test="${empty optnHashMap}"><c:set var="optnHashMap" value="${optnHashMap}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:set var="itemOptionType" value="${itemObj['option_type']}"/>
	<c:if test="${!empty idxColumnId}"><c:set var="objVal" value="${dt[idxColumnId]}"/></c:if>
	<c:if test="${empty defVal}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>
	<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
	<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	<c:set var="optMaxLevelKey" value="_class_${itemObj['master_code']}_max_level"/>
	<c:set var="optListKey" value="_class_${itemObj['master_code']}"/>
	<c:set var="optnOptList" value="${optnHashMap[optListKey]}"/>
	<c:set var="optMaxLevelDt" value="${optnHashMap[optMaxLevelKey]}"/>
	<c:set var="optMaxLevel" value="${optMaxLevelDt.MAX_LEVEL}"/>
	<c:choose>
		<c:when test="${inputType == 10}">
			<c:forEach var="optnDt" items="${optnOptList}" varStatus="i">
				<c:if test="${optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal}"><c:out value="${optnDt.OPTION_NAME}"/></c:if>
			</c:forEach>
			<input type="hidden" id="${itemId}" name="${name}" title="${itemName}" value="<c:out value="${objVal}"/>"/>
		</c:when>
		<c:otherwise>
			<c:if test="${optMaxLevel >= 1}">
			<c:set var="optionTitle" value="${keyItem['option_title']}"/>
			<c:set var="optionTitleLen" value="${0}"/>
			<c:if test="${!empty optionTitle}">
				<c:set var="optionTitles" value="${fn:split(optionTitle, ',')}"/>
				<c:set var="optionTitleLen" value="${fn:length(optionTitles)}"/>
			</c:if>
			<c:forEach var="levelIdx" begin="1" end="${optMaxLevel}">
				<c:choose>
					<c:when test="${!empty optionTitles && fn:length(optionTitles) >= levelIdx}">
						<spring:message var="itemTitle" code="item.class.custom.select.title.name" arguments="${optionTitles[levelIdx - 1]}"/>
						<spring:message var="itemOptTitle" code="item.class.custom.select.name" arguments="${optionTitles[levelIdx - 1]}"/>
					</c:when>
					<c:otherwise>
						<spring:message var="itemTitle" code="item.class.select.title.name" arguments="${levelIdx},${itemName}" argumentSeparator=","/>
						<spring:message var="itemOptTitle" code="item.class.select.name" arguments="${levelIdx},${itemName}"/>
					</c:otherwise>
				</c:choose>
				<select id="${id}${levelIdx}" name="${name}_tmp" title="${itemTitle}" style="${objStyle}" class="select t_select"<c:if test="${required == 1}"> required="required"</c:if>>
					<option value="">${itemOptTitle}</option>
				</select>
			</c:forEach>
			</c:if>
			<spring:message var="itemOptTitle" code="item.itemId.select.name" arguments="${itemName}"/>
			<select id="${id}" name="${name}" title="${itemName}" style="display:none;" class="select t_select" data-max="${optMaxLevel}">
				<option value="">${itemOptTitle}</option>
			<c:forEach var="optnDt" items="${optnOptList}" varStatus="i">
				<option value="<c:out value="${optnDt.OPTION_CODE}"/>" data-pcode="${optnDt.PARENT_OPTION_CODE}" data-level="${optnDt.OPTION_LEVEL}" <c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if><c:if test="${optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal}"> selected="selected"</c:if><c:if test="${optnDt.OPTION_CODE == defVal}"> data-default="1"</c:if>><c:if test="${addOrder}">${i.count}. </c:if><c:out value="${optnDt.OPTION_NAME}"/></option>
			</c:forEach>
			</select>
		</c:otherwise>
	</c:choose>
</c:if>