<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="itemId"%>
<%@ attribute name="itemName"%>
<%@ attribute name="name"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<%@ attribute name="objAttr"%>
<% /* 항목(th,td) 설정별 출력 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<c:choose>
		<c:when test="${empty itemName}">
			<itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}" var="itemName2"/>
		</c:when>
		<c:otherwise>
			<c:set var="itemName2" value="${itemName}"/>
		</c:otherwise>
	</c:choose>
<th scope="row"><itui:objectLabel itemId="${itemId}" required="${required}" itemInfo="${itemInfo}" itemName="${itemName2}"/></th>
<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
	<itui:objectTextarea itemId="${itemId}" name="${name}" itemInfo="${itemInfo}" objDt="${objDt}" objVal="${objVal}" objStyle="${objStyle}" objClass="${objClass}" objAttr="${objAttr}"/>
</td>
</c:if>