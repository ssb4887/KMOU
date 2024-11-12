<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objOVal"%>
<%@ attribute name="objSVal"%>
<%@ attribute name="objTVal"%>
<%@ attribute name="objSZVal"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="textStyle"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="objClass"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="downLink" type="java.lang.Boolean"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:if test="${empty downLink}">
	<c:set var="downLink" value="false"/>
</c:if>
<% /* 단일파일 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:set var="textColumnId" value="${itemColumnId}_TEXT"/>
	<c:set var="sizeColumnId" value="${itemColumnId}_SIZE"/>
	<c:set var="originColumnId" value="${itemColumnId}_ORIGIN_NAME"/>
	<c:set var="savedColumnId" value="${itemColumnId}_SAVED_NAME"/>
	<c:if test="${empty objOVal}"><c:set var="objOVal" value="${objDt[originColumnId]}"/></c:if>
	<c:if test="${empty objSVal}"><c:set var="objSVal" value="${objDt[savedColumnId]}"/></c:if>
	<c:if test="${empty objTVal}"><c:set var="objTVal" value="${objDt[textColumnId]}"/></c:if>
	<c:if test="${empty objSZVal}"><c:set var="objSZVal" value="${objDt[sizeColumnId]}"/></c:if>
	
	<c:if test="${empty ulClass}"><c:set var="ulClass" value="singleFileUL"/></c:if>
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	
	<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_del_${itemId}"/></c:if>
	<c:if test="${empty btnClass}"><c:set var="btnClass" value="btnTypeF"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>

	<input type="hidden" id="${itemId}_origin" name="${itemId}_origin" value="<c:out value="${objOVal}"/>"/>
	<input type="hidden" id="${itemId}_size" name="${itemId}_size" value="<c:out value="${objSZVal}"/>"/>
	<input type="hidden" id="${itemId}_saved" name="${itemId}_saved" value="<c:out value="${objSVal}"/>"/>
	<input type="hidden" id="${itemId}_deleted_idxs" name="${itemId}_deleted_idxs"/>
	<ul class="${ulClass}">
		<li>
			<input type="file" id="${itemId}" name="${itemId}" title="${itemName}" class="${objClass}" style="${objStyle}"<c:if test="${empty objOVal && required == 1}"> required="required"</c:if>/>
		</li>
		<li>
			<label for="${itemId}_text" class="fn_text_label"><spring:message code="item.file.replace.only.text"/></label>
			<input type="text" id="${itemId}_text" name="${itemId}_text" title="<spring:message code="item.file.replace.text" arguments="${itemName}"/>" class="altTxt ${objClass}" style="${textStyle}" value="<c:out value="${objTVal}"/>"/>
		</li>
		<c:if test="${!empty itemObj['comment']}">
		<li>
			<p class="comment"><c:out value="${itemObj['comment']}"/></p>
		</li>
		</c:if>
		<c:if test="${!empty objOVal}">
		<c:set var="keyItemId" value="${settingInfo.idx_name}"/>
		<c:set var="keyColumnId" value="${settingInfo.idx_column}"/>
		<li>
			<dl class="saved_file_nb">
				<dt><spring:message code="item.file.saved.name"/></dt>
				<dd><c:choose><c:when test="${downLink}"><a href="<c:out value="${URL_DOWNLOAD}"/>&${keyItemId}=<c:out value="${objDt[keyColumnId]}"/>&itId=<c:out value="${itemId}"/>"><c:out value="${objOVal}"/></a></c:when><c:otherwise><c:out value="${objOVal}"/></c:otherwise></c:choose></dd>
			</dl>
			<button type="button" class="${btnClass}" id="${btnId}"><spring:message code="item.delete.name"/></button>
		</li>
		</c:if>
	</ul>
</c:if>