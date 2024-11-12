<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@tag import="java.util.Calendar"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal1"%>
<%@ attribute name="objVal2"%>
<%@ attribute name="defVal1"%>
<%@ attribute name="defVal2"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
	<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
	<c:if test="${objVal1 == null}"><c:set var="objVal1" value="${objDt[itemColumnId1]}"/></c:if>
	<c:if test="${objVal2 == null}"><c:set var="objVal2" value="${objDt[itemColumnId2]}"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<%
		Calendar calendar = Calendar.getInstance();
		request.setAttribute("nowYear", calendar.get(Calendar.YEAR));
		request.setAttribute("nowMonth", calendar.get(Calendar.MONTH));
	%>
	<c:set var="beginYear" value="${itemObj.start_year}"/>
	<c:if test="${empty beginYear || beginYear == 0}"><c:set var="beginYear" value="${nowYear}"/></c:if>
	<c:set var="endYear" value="${nowYear + itemObj.end_addcnt}"/>
	<c:if test="${itemObj.default_current == 1}">
		<c:set var="defVal1" value="${nowYear}"/>
		<c:set var="defVal2" value="${nowMonth + 1}"/>
	</c:if>
	<c:set var="objTitle" value="${elfn:getItemName(itemInfo.items[itemId])}"/>
	<spring:message var="yearObjTitle" code="item.itemId.year.name" arguments="${objTitle}"/>
	<spring:message var="itemYearName" code="item.year.name"/>
	<spring:message var="yearOptTitle" code="item.itemId.select.name" arguments="${itemYearName}"/>
	<spring:message var="monthObjTitle" code="item.itemId.month.name" arguments="${objTitle}"/>
	<spring:message var="itemMonthName" code="item.month.name"/>
	<spring:message var="monthOptTitle" code="item.itemId.select.name" arguments="${itemMonthName}"/>
<select id="${itemId}1" name="${itemId}1" style="${objStyle}" title="${yearObjTitle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${yearOptTitle}</option>
<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
	<option value="<c:out value="${optnYear}"/>"<c:if test="${optnYear == objVal1 || empty objVal1 && optnYear == defVal1}"> selected="selected"</c:if>><c:out value="${optnYear}"/></option>
</c:forEach>
</select>${itemYearName}&nbsp;
<select id="${itemId}2" name="${itemId}2" style="${objStyle}" title="${monthObjTitle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${monthOptTitle}</option>
<c:forEach var="optnMonth" begin="1" end="12">
	<c:set var="optnMonthStr" value="${elfn:getIntLPAD(optnMonth, '0', 2)}"/>
	<option value="<c:out value="${optnMonthStr}"/>"<c:if test="${optnMonthStr == objVal2 || empty objVal2 && optnMonth == defVal2}"> selected="selected"</c:if>><c:out value="${optnMonthStr}"/></option>
</c:forEach>
</select>${itemMonthName}
</c:if>