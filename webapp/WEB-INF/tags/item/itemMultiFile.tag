<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objVal"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="textStyle"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="objClass"%>
<%@ attribute name="selectClass"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="fileList" type="java.util.List"%>
<%@ attribute name="downLink" type="java.lang.Boolean"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:if test="${empty downLink}">
	<c:set var="downLink" value="false"/>
</c:if>
<% /* 항목(th,td) 설정별 출력 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${fileList == null}"><c:set var="fileList" value="${multiFileHashMap[itemId]}"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
<th scope="row"><itui:objectLabel itemId="${itemId}" required="${required}" itemInfo="${itemInfo}"/></th>
<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
	<itui:objectMultiFile itemId="${itemId}" itemInfo="${itemInfo}" objVal="${objVal}" fileList="${fileList}" objClass="${objClass}" objStyle="${objStyle}" selectClass="${selectClass}" textStyle="${textStyle}" ulClass="${ulClass}" btnId="${btnId}" btnClass="${btnClass}" downLink="${downLink}"/>
	<c:if test="${!empty itemObj['comment']}"><div class="comment"><c:out value="${itemObj['comment']}" escapeXml="false"/></div></c:if>
</td>
</c:if>