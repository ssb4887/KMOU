<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="itemId"%>
<%@ attribute name="id"%>
<%@ attribute name="name"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<%@ attribute name="objAttr"%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:choose>
	<c:when test="${empty itemObj['content_type']}">
		<c:set var="typeColumnId" value="${itemColumnId}2"/>
		<c:set var="contentsType" value="${objDt[typeColumnId]}"/>
	</c:when>
	<c:otherwise>
		<c:set var="contentsType" value="${itemObj['content_type']}"/>
	</c:otherwise>
	</c:choose>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<%/* 에디터로 입력한 경우 : javascript만 삭제 */%>
	<c:if test="${!empty objVal && contentsType == '2'}"><c:set var="objVal" value="${elfn:removeScriptTag(objVal)}"/></c:if>
	
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
	<c:set var="useEditor" value="${false}"/>
	<c:if test="${itemObj['editor'] == '1' && (itemObj['editor_utype'] == '1' || isAdmMode && (empty itemObj['editor_utype'] || itemObj['editor_utype'] == '0'))}">
		<c:set var="useEditor" value="${true}"/>
	</c:if>
	<%/* 에디터 사용하지 않는 경우 : html 태그 제거 */%>
	<c:if test="${!useEditor && contentsType == '2'}"><c:set var="objVal" value="${elfn:getEditorToTxtarea(objVal)}"/></c:if>
	<c:if test="${empty objStyle}">
	<c:set var="objStyle" value="${itemObj['object_style']}"/>
	</c:if>
	<textarea id="${id}" name="${name}" title="${itemName}" class="${objClass}" style="${objStyle}" data-type="<c:out value="${contentsType}"/>"<c:if test="${!useEditor && required == 1}"> required="required"</c:if> ${objAttr}><c:out value="${objVal}"/></textarea>
	<c:if test="${!empty itemObj['editor'] && itemObj['content_type'] != 2}">
		<c:choose>
			<c:when test="${itemObj['editor_utype'] == '1' || isAdmMode && (empty itemObj['editor_utype'] || itemObj['editor_utype'] == '0')}">
				<c:set var="hcontentsType" value="2"/>
			</c:when>
			<c:otherwise>
				<c:set var="hcontentsType" value="1"/>
			</c:otherwise>
		</c:choose>
		<input type="hidden" id="${id}2" name="${name}2" value="${hcontentsType}"/>
	</c:if>
</c:if>