<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="objAttr"%>
<%@ attribute name="itemId"%>
<%@ attribute name="itemName"%>
<%@ attribute name="itemRequried"%>
<%@ attribute name="name"%>
<%@ attribute name="id"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="disabledId" type="java.lang.Boolean"%>
<%@ attribute name="itemLangList" type="java.util.List"%>						<%/* 언어목록 */%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:if test="${empty disabledId}">
	<c:set var="disabledId" value="false"/>
</c:if>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:if test="${empty itemName}"><c:set var="itemName" value="${elfn:getItemName(itemObj)}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:if test="${empty objVal}"><c:set var="objVal" value="${defVal}"/></c:if>
	
	<c:set var="objectType" value="${itemObj['object_type']}"/>
	<c:if test="${objectType == 101 && !empty objVal}"><c:set var="objVal" value="${elfn:privDecrypt(objVal)}"/></c:if>
	
	<c:set var="objClass" value="inputTxt ${itemObj['class_name']} ${objClass}"/>
	<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
	<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
	<c:choose>
		<c:when test="${!empty itemRequried}">
			<c:set var="required" value="${itemRequried}"/>
		</c:when>
		<c:otherwise>
			<c:set var="requiredName" value="required_${inputFlag}"/>
			<c:set var="required" value="${itemObj[requiredName]}"/>
		</c:otherwise>
	</c:choose>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	
	<c:choose>
		<c:when test="${inputType == 10}">
			<span id="fn_item_${itemId}"><c:out value="${objVal}"/></span>
			<input type="hidden" id="${itemId}" name="${itemId}" value="<c:out value="${objVal}"/>"/>
		</c:when>
		<c:otherwise>
		<c:if test="${empty objStyle}">
			<c:set var="objStyle" value="${itemObj['object_style']}"/>
		</c:if>
		<c:set var="itemtype" value="${itemObj['item_type']}"/>
		<c:if test="${itemtype == '1' || itemtype == '11'}"><c:set var="objStyle" value="text-align:right;${objStyle}"/></c:if>
		<c:if test="${itemtype == '1' || itemtype == '2' || itemtype == '3' || itemtype == '4' || itemtype == '5' || itemtype == '11'}"><c:set var="objStyle" value="ime-mode:disabled;${objStyle}"/></c:if>
		<c:choose>
		<c:when test="${!empty itemLangList}">
			<input type="hidden" id="${itemId}" name="${itemId}"<c:if test="${itemtype == '11'}"> data-type="current"</c:if> value="<c:out value="${objVal}"/>"/>
			<c:forEach var="langDt" items="${itemLangList}" varStatus="i">
				<c:set var="itemColumnId" value="${itemObj['column_id']}_${fn:toUpperCase(langDt.OPTION_CODE)}"/>
				<c:set var="objVal" value="${objDt[itemColumnId]}"/>
			<dl class="fn_langInput fn_inlineBlock">
				<dt><c:out value="${langDt.OPTION_NAME}"/></dt>
				<dd>
				<spring:message var="langItemName" code="item.space2.name" arguments="${itemName},${langDt.OPTION_NAME}" argumentSeparator=","/>
				<input type="text" id="${id}_<c:out value="${langDt.OPTION_CODE}"/>" name="${name}_<c:out value="${langDt.OPTION_CODE}"/>" title="${langItemName}"<c:if test="${itemtype == '11'}"> data-type="current"</c:if> class="${objClass}" style="${objStyle}"<c:if test="${itemObj['maximum'] > 0}"> maxlength="${itemObj['maximum']}"</c:if><c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal}"/>" ${objAttr}/>
				</dd>
			</dl>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:if test="${!empty itemObj['next_text']}"><div class="input-group"></c:if>
			<input type="text"<c:if test="${!disabledId}"> id="${id}"</c:if> name="${name}" title="${itemName}"<c:if test="${itemtype == '11'}"> data-type="current"</c:if> class="${objClass}" style="${objStyle}"<c:if test="${itemObj['maximum'] > 0}"> maxlength="${itemObj['maximum']}"</c:if><c:if test="${!empty defVal}"> data-default="<c:out value="${defVal}"/>"</c:if><c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal}"/>" ${objAttr}/>
			<c:if test="${!empty itemObj['next_text']}"><span class="nText"><c:out value="${itemObj['next_text']}"/></span></div></c:if>
		</c:otherwise>
		</c:choose>
		</c:otherwise>
	</c:choose>
</c:if>