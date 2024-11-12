<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="itemNameCRStr"%>
<%@ attribute name="thColspan" type="java.lang.Integer"%>
<%@ attribute name="rowspan" type="java.lang.Integer"%>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="thClass"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="objAttr"%>
<%@ attribute name="itemId"%>
<%@ attribute name="itemName"%>
<%@ attribute name="name"%>
<%@ attribute name="id"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="itemLangList" type="java.util.List"%>						<%/* 언어목록 */%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 항목(th,td) 설정별 출력 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:set var="itemtype" value="${itemObj['item_type']}"/>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
	<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
	<c:set var="formatType" value="${itemObj['format_type']}"/>
	<c:choose>
		<c:when test="${empty itemName}">
			<itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}" var="itemName2"/>
		</c:when>
		<c:otherwise>
			<c:set var="itemName2" value="${itemName}"/>
		</c:otherwise>
	</c:choose>
<c:choose>
	<c:when test="${inputType == 10}">
	<th scope="row<c:if test="${rowspan > 0}">group</c:if>"<c:if test="${thColspan > 0}"> colspan="${thColspan}"</c:if> <c:if test="${rowspan > 0}">rowspan="${rowspan}"</c:if><c:if test="${!empty thClass}"> class="${thClass}"</c:if>><itui:objectLabel itemId="${itemId}" id="${id}" required="0" itemInfo="${itemInfo}" itemName="${itemName2}" itemNameCRStr="${itemNameCRStr}"/></th>
	<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
		<itui:objectText itemId="${itemId}" id="${id}" name="${name}" itemInfo="${itemInfo}" itemLangList="${itemLangList}" objDt="${objDt}" objVal="${objVal}" objClass="${objClass}" objStyle="${objStyle}" objAttr="${objAttr}"/>
	</td>
	</c:when>
	<c:otherwise>
	<th scope="row<c:if test="${rowspan > 0}">group</c:if>"<c:if test="${thColspan > 0}"> colspan="${thColspan}"</c:if> <c:if test="${rowspan > 0}">rowspan="${rowspan}"</c:if><c:if test="${!empty thClass}"> class="${thClass}"</c:if>><itui:objectLabel itemId="${itemId}" id="${id}" required="${required}" itemInfo="${itemInfo}" itemName="${itemName2}" itemNameCRStr="${itemNameCRStr}"/></th>
	<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
		<itui:objectText itemId="${itemId}" id="${id}" name="${name}" itemInfo="${itemInfo}" itemLangList="${itemLangList}" objDt="${objDt}" objVal="${objVal}" objClass="${objClass}" objStyle="${objStyle}" objAttr="${objAttr}"/>
		<c:if test="${empty itemObj['comment'] && formatType == 31}"><span class="comment"><spring:message code="item.module.input.format_type.url_link"/></span></c:if>
		<c:if test="${!empty itemObj['comment']}"><span class="comment"><c:out value="${itemObj['comment']}" escapeXml="false"/></span></c:if>
	</td>
	</c:otherwise>
</c:choose>
</c:if>