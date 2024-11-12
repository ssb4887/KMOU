<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objValList" type="java.util.List"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 항목(th,td) 설정별 출력 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:set var="requiredName" value="required_${inputFlag}"/>
<c:set var="required" value="${itemObj[requiredName]}"/>
<th scope="row"><itui:objectLabel itemId="${itemId}" required="${required}" itemInfo="${itemInfo}"/></th>
<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
<itui:objectMultiSelect itemId="${itemId}" itemInfo="${itemInfo}" objValList="${objValList}" objStyle="${objStyle}" optnHashMap="${optnHashMap}"/>
</td>
</c:if>