<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="id"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnText"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
<c:if test="${!empty objVal && itemObj['use_privsec'] == 1}"><c:set var="objVal" value="${elfn:privDecrypt(objVal)}"/></c:if>

<c:if test="${empty objStyle}"><c:set var="objStyle" value="${itemObj['object_style']}"/></c:if>
<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>

<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_${itemId}"/></c:if>
<c:if test="${empty btnClass}"><c:set var="btnClass" value="btnTypeF"/></c:if>

<c:if test="${empty btnText}"><spring:message var="btnText" code="item.duplicate.check"/></c:if>

<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
<c:set var="requiredName" value="required_${inputFlag}"/>
<c:set var="required" value="${itemObj[requiredName]}"/>
<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
<%/*inputType == 10 -> 수정모드인 경우로 조건 수정 */ %>
<c:choose>
	<c:when test="${queryString.mode == 'm' || submitType == 'myinfo'}">
	<th scope="row"><label for="${id}" id="fn_name_${id}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></label></th>
	<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
		<span id="fn_item_${id}"><c:out value="${objVal}"/></span>
		<input type="hidden" id="${id}" name="${itemId}" value="<c:out value="${objVal}"/>"/>
	</td>
	</c:when>
	<c:otherwise>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<th scope="row"><label for="${id}" id="fn_name_${id}"<c:if test="${required == 1}"> class="required"</c:if>><c:if test="${!isAdmMode && required == 1}"><em class="point01">(필수)</em></c:if><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></label></th>
	<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
		<input type="text" id="${id}" name="${itemId}" class="${objClass}" style="ime-mode:disabled;${objStyle}"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal}"/>"/>
		<button type="button" id="${btnId}" title="${btnText}" class="${btnClass}">${btnText}</button>
		<c:if test="${!empty itemObj['comment']}"><span class="comment"><c:out value="${itemObj['comment']}"/></span></c:if>
	</td>
	</c:otherwise>
</c:choose>
</c:if>