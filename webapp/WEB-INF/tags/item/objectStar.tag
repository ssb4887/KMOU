<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="itemId"%>
<%@ attribute name="id"%>
<%@ attribute name="name"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="objAttr"%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:if test="${empty defVal}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
<ul class="contents">
<c:forEach var="optnDt" items="${optnHashMap[itemObj['master_code']]}" varStatus="i">
	<c:if test="${empty optnDt.ISHIDDEN || optnDt.ISHIDDEN == '0' || optnDt.OPTION_CODE == objVal}">
	<li>
		<input type="radio" id="${id}${i.count}" name="${name}" value="<c:out value="${optnDt.OPTION_CODE}"/>"<c:if test="${optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal}"> checked="checked"</c:if><c:if test="${optnDt.OPTION_CODE == defVal}"> data-default="1"</c:if><c:if test="${!empty objAttr}"> ${objAttr}</c:if>/>
		<label for="star${i.count}" class="blind">매우만족</label> <span class="star${i.count}"><c:out value="${optnDt.OPTION_NAME}"/></span>
	</li>
	</c:if>
</c:forEach>
</ul>
</c:if>