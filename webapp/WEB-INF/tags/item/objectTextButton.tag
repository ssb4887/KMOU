<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="objAttr"%>
<%@ attribute name="itemId"%>
<%@ attribute name="name"%>
<%@ attribute name="id"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="objNameVal"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnDelClass"%>
<%@ attribute name="btnDelHidden" type="java.lang.Boolean"%>
<%@ attribute name="btnText"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:if test="${empty btnDelHidden}">
	<c:set var="btnDelHidden" value="false"/>
</c:if>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:set var="nameColumnId" value="${itemObj['name_column_id']}"/>
	<c:if test="${empty nameColumnId}"><c:set var="nameColumnId" value="${itemObj['column_id']}_NAME"/></c:if>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:if test="${objNameVal == null}"><c:set var="objNameVal" value="${objDt[nameColumnId]}"/></c:if>
	<c:if test="${empty objStyle}"><c:set var="objStyle" value="${itemObj['object_style']}"/></c:if>
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	
	<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_${id}"/></c:if>
	
	<c:if test="${empty btnText}"><spring:message var="btnText" code="item.search.name"/></c:if>
	<c:set var="btnDelId" value="fn_btn_del_${id}"/>
	<spring:message var="delText" code="item.delete.name" />
	<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
	<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<spring:message var="codeItemName" code="item.itemId.code.name" arguments="${itemName}"/>
	<c:choose>
	<c:when test="${inputType == 10}">
		<span id="fn_item_${id}"><c:out value="${objNameVal}"/></span>
		<input type="hidden" id="${id}" name="${name}" title="${codeItemName}" value="<c:out value="${objVal}"/>"/>
	</c:when>
	<c:otherwise>
		<input type="text" id="${id}Name" name="${name}Name" title="${itemName}" class="${objClass}" readonly="readonly" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objNameVal}"/>"${objAttr}/>
		<input type="hidden" id="${id}" name="${name}" title="${codeItemName}" value="<c:out value="${objVal}"/>"/>
		<button type="button" id="${btnId}" title="${btnText}" class="btnTypeF ${btnClass} fn_btn_${itemId}" data-id="${id}">${btnText}</button>
		<c:if test="${!btnDelHidden}"><button type="button" id="${btnDelId}" title="${delText}" class="btnTypeN ${btnDelClass} fn_btn_del_${itemId}" data-id="${id}">${delText}</button></c:if>
		<c:if test="${!empty itemObj['comment']}"><span class="comment"><c:out value="${itemObj['comment']}"/></span></c:if>
	</c:otherwise>
	</c:choose>
</c:if>