<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@tag import="java.util.Calendar"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal11"%>
<%@ attribute name="objVal12"%>
<%@ attribute name="objVal21"%>
<%@ attribute name="objVal22"%>
<%@ attribute name="defVal11"%>
<%@ attribute name="defVal12"%>
<%@ attribute name="defVal21"%>
<%@ attribute name="defVal22"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 년월(기간) */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:set var="itemColumnId11" value="${itemObj['column_id']}11"/>
	<c:set var="itemColumnId12" value="${itemObj['column_id']}12"/>
	<c:set var="itemColumnId21" value="${itemObj['column_id']}21"/>
	<c:set var="itemColumnId22" value="${itemObj['column_id']}22"/>
	<c:if test="${objVal11 == null}"><c:set var="objVal11" value="${objDt[itemColumnId11]}"/></c:if>
	<c:if test="${objVal12 == null}"><c:set var="objVal12" value="${objDt[itemColumnId12]}"/></c:if>
	<c:if test="${objVal21 == null}"><c:set var="objVal11" value="${objDt[itemColumnId21]}"/></c:if>
	<c:if test="${objVal22 == null}"><c:set var="objVal12" value="${objDt[itemColumnId22]}"/></c:if>
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
	<spring:message var="yearObjTitle" code="item.itemId.year.name" arguments="${itemName}"/>
	<spring:message var="yearObjBeginTitle" code="item.itemId.begin.name" arguments="${yearObjTitle}"/>
	<spring:message var="yearObjEndTitle" code="item.itemId.end.name" arguments="${yearObjTitle}"/>
	
	<spring:message var="itemYearName" code="item.year.name"/>
	<spring:message var="yearOptTitle" code="item.itemId.select.name" arguments="${itemYearName}"/>
	
	<spring:message var="monthObjTitle" code="item.itemId.month.name" arguments="${itemName}"/>
	<spring:message var="monthObjBeginTitle" code="item.itemId.begin.name" arguments="${monthObjTitle}"/>
	<spring:message var="monthObjEndTitle" code="item.itemId.end.name" arguments="${monthObjTitle}"/>
	
	<spring:message var="itemMonthName" code="item.month.name"/>
	<spring:message var="monthOptTitle" code="item.itemId.select.name" arguments="${itemMonthName}"/>
<select id="${itemId}11" name="${itemId}11" title="${yearObjBeginTitle}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${yearOptTitle}</option>
<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
	<option value="<c:out value="${optnYear}"/>"<c:if test="${optnYear == objVal11 || empty objVal11 && optnYear == defVal11}"> selected="selected"</c:if>><c:out value="${optnYear}"/></option>
</c:forEach>
</select>${itemYearName}
<select id="${itemId}12" name="${itemId}12" title="${monthObjBeginTitle}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${monthOptTitle}</option>
<c:forEach var="optnMonth" begin="1" end="12">
	<c:set var="optnMonthStr" value="${elfn:getIntLPAD(optnMonth, '0', 2)}"/>
	<option value="<c:out value="${optnMonthStr}"/>"<c:if test="${optnMonthStr == objVal12 || empty objVal12 && optnMonth == defVal12}"> selected="selected"</c:if>><c:out value="${optnMonthStr}"/></option>
</c:forEach>
</select>${itemMonthName}
~
<select id="${itemId}21" name="${itemId}21" title="${yearObjEndTitle}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${yearOptTitle}</option>
<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
	<option value="<c:out value="${optnYear}"/>"<c:if test="${optnYear == objVal21 || empty objVal21 && optnYear == defVal21}"> selected="selected"</c:if>><c:out value="${optnYear}"/></option>
</c:forEach>
</select>${itemYearName}
<select id="${itemId}22" name="${itemId}22" title="${monthObjEndTitle}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if>>
	<option value="">${monthOptTitle}</option>
<c:forEach var="optnMonth" begin="1" end="22">
	<c:set var="optnMonthStr" value="${elfn:getIntLPAD(optnMonth, '0', 2)}"/>
	<option value="<c:out value="${optnMonthStr}"/>"<c:if test="${optnMonthStr == objVal22 || empty objVal22 && optnMonth == defVal22}"> selected="selected"</c:if>><c:out value="${optnMonthStr}"/></option>
</c:forEach>
</select>${itemMonthName}
</c:if>