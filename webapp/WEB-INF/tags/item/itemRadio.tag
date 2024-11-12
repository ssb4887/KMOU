<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="thColspan" type="java.lang.Integer"%>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="thClass"%>
<%@ attribute name="itemId"%>
<%@ attribute name="id"%>
<%@ attribute name="name"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<%@ attribute name="objAttr"%>
<%@ attribute name="objOptionStyle"%>
<% /* 항목(th,td) 설정별 출력 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
<th scope="row"<c:if test="${thColspan > 0}"> colspan="${thColspan}"</c:if><c:if test="${!empty thClass}"> class="${thClass}"</c:if>><itui:objectLabel itemId="${itemId}" id="${id}1" required="${required}" itemInfo="${itemInfo}"/></th>
<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
	<itui:objectRadio itemId="${itemId}" id="${id}" name="${name}" itemInfo="${itemInfo}" objDt="${objDt}" defVal="${defVal}" objVal="${objVal}" optnHashMap="${optnHashMap}" objAttr="${objAttr}" objOptionStyle="${objOptionStyle}"/>
</td>
</c:if>