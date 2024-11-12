<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnText"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 항목(th,td) 설정별 출력 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:set var="nameColumnId" value="${itemObj['name_column_id']}"/>
	<c:if test="${empty nameColumnId}"><c:set var="nameColumnId" value="${itemObj['column_id']}_NAME"/></c:if>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:set var="objNameVal" value="${objDt[nameColumnId]}"/>
	<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
	<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
<c:choose>
	<c:when test="${inputType == 10}">
	<th scope="row"><label for="${itemId}Name" id="fn_name_${itemId}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></label></th>
	</c:when>
	<c:otherwise>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<th scope="row"><label for="${itemId}Name"<c:if test="${required == 1}"> class="required"</c:if>><c:if test="${!isAdmMode && required == 1}"><em class="point01">(필수)</em></c:if><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></label></th>
	</c:otherwise>
</c:choose>
	<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
		<itui:objectTextButton itemId="${itemId}" itemInfo="${itemInfo}" objDt="${objDt}" objVal="${objVal}" objNameVal="${objNameVal}" objStyle="${objStyle}" objClass="${objClass}" btnId="${btnId}" btnClass="${btnClass}" btnText="${btnText}" inputTypeName="${inputTypeName}"/>
	</td>
</c:if>