<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="thClass"%>
<%@ attribute name="objType"%>
<%@ attribute name="objClass1"%>
<%@ attribute name="objClass2"%>
<%@ attribute name="objClass3"%>
<%@ attribute name="objStyle1"%>
<%@ attribute name="objStyle2"%>
<%@ attribute name="objStyle3"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal1"%>
<%@ attribute name="objVal2"%>
<%@ attribute name="objVal3"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 항목(th,td) 설정별 출력 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
	<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
	<c:set var="itemColumnId3" value="${itemObj['column_id']}3"/>
	<c:if test="${objVal1 == null && objVal2 == null && objVal3 == null}">
		<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
		<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
		<c:set var="objVal3" value="${objDt[itemColumnId3]}"/>
	</c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
<th scope="row"<c:if test="${!empty thClass}"> class="${thClass}"</c:if>><itui:objectLabel itemId="${itemId}" nextItemId="1" required="${required}" itemInfo="${itemInfo}"/></th>
<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
	<itui:objectPHONE itemId="${itemId}" itemInfo="${itemInfo}" objDt="${objDt}" objVal1="${objVal1}" objVal2="${objVal2}" objVal3="${objVal3}" objType="${objType}" objClass1="${objClass1}" objClass2="${objClass2}" objClass3="${objClass3}" objStyle1="${objStyle1}" objStyle2="${objStyle2}" objStyle3="${objStyle3}"/>
</td>
</c:if>