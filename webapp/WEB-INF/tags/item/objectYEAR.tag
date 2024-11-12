<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@tag import="java.util.Calendar"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="name"%>
<%@ attribute name="id"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<%@ attribute name="optionReverse" type="java.lang.Boolean"%>
<c:if test="${empty optionReverse}">
	<c:set var="optionReverse" value="false"/>
</c:if>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:if test="${empty defVal}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	
	<spring:message var="itemYearName" code="item.year.name"/>
	<%
		Calendar calendar = Calendar.getInstance(); 
		request.setAttribute("nowYear", calendar.get(Calendar.YEAR)); 
	%>
	<c:set var="beginYear" value="${itemObj.start_year}"/>
	<c:if test="${empty beginYear || beginYear == 0}"><c:set var="beginYear" value="${nowYear}"/></c:if>
	<c:set var="endYear" value="${nowYear + itemObj.end_addcnt}"/>
	<c:if test="${itemObj.default_current == 1}">
		<c:set var="defVal" value="${nowYear}"/>
	</c:if>
	<spring:message var="itemOptTitle" code="item.itemId.select.name" arguments="${itemName}"/>
<select id="${id}" name="${name}" title="${itemName}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${itemOptTitle}</option>
	<c:set var="optnIdx" value="0"/>
<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
	<c:choose>
		<c:when test="${optionReverse}">
			<c:set var="optnVal" value="${endYear - optnIdx}"/>
			<c:set var="optnIdx" value="${optnIdx + 1}"/>
		</c:when>
		<c:otherwise><c:set var="optnVal" value="${optnYear}"/></c:otherwise>
	</c:choose>
	<option value="<c:out value="${optnVal}"/>"<c:if test="${optnVal == objVal || empty objVal && optnVal == defVal}"> selected="selected"</c:if>><c:out value="${optnVal}"/></option>
</c:forEach>
</select>${itemYearName}
</c:if>