<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@tag import="java.util.Calendar"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal1"%>
<%@ attribute name="objVal2"%>
<%@ attribute name="defVal1"%>
<%@ attribute name="defVal2"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 년도(기간) */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
	<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
	<c:if test="${objVal1 == null}"><c:set var="objVal1" value="${objDt[itemColumnId1]}"/></c:if>
	<c:if test="${objVal2 == null}"><c:set var="objVal2" value="${objDt[itemColumnId2]}"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>

	<spring:message var="itemYearName" code="item.year.name"/>
	<%Calendar calendar = Calendar.getInstance(); request.setAttribute("nowYear", calendar.get(Calendar.YEAR)); %>
	<c:set var="beginYear" value="${itemObj.start_year}"/>
	<c:if test="${empty beginYear || beginYear == 0}"><c:set var="beginYear" value="${nowYear}"/></c:if>
	<c:set var="endYear" value="${nowYear + itemObj.end_addcnt}"/>
	<spring:message var="yearObjBeginTitle" code="item.itemId.begin.name" arguments="${itemName}"/>
	<spring:message var="yearObjEndTitle" code="item.itemId.end.name" arguments="${itemName}"/>
	<spring:message var="itemYearName" code="item.year.name"/>
	<spring:message var="yearOptTitle" code="item.itemId.select.name" arguments="${itemYearName}"/>
<select id="${itemId}1" name="${itemId}1" title="${yearObjBeginTitle}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${yearOptTitle}</option>
<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
	<option value="<c:out value="${optnYear}"/>"<c:if test="${optnYear == objVal1 || empty objVal1 && optnYear == defVal1}"> selected="selected"</c:if>><c:out value="${optnYear}"/></option>
</c:forEach>
</select>${itemYearName}
~
<select id="${itemId}2" name="${itemId}2" title="${yearObjEndTitle}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${yearOptTitle}</option>
<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
	<option value="<c:out value="${optnYear}"/>"<c:if test="${optnYear == objVal2 || empty objVal2 && optnYear == defVal2}"> selected="selected"</c:if>><c:out value="${optnYear}"/></option>
</c:forEach>
</select>${itemYearName}
</c:if>