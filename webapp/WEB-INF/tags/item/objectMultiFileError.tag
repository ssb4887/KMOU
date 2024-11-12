<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objVal"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="textStyle"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="objClass"%>
<%@ attribute name="selectClass"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="fileList" type="java.util.List"%>
<%@ attribute name="downLink" type="java.lang.Boolean"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:if test="${empty downLink}">
	<c:set var="downLink" value="false"/>
</c:if>
<% /* 다중 파일 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${fileList == null}"><c:set var="fileList" value="${multiFileHashMap[itemId]}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>

	<c:if test="${empty ulClass}"><c:set var="ulClass" value="multiFileUL"/></c:if>
	<c:if test="${empty selectClass}"><c:set var="selectClass" value="selectMultiFile"/></c:if>
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	
	<c:if test="${empty btnId}"><c:set var="btnId" value="fn_btn_${itemId}"/></c:if>
	<c:if test="${empty btnClass}"><c:set var="btnClass" value="btnTypeF"/></c:if>
	
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>

	<c:set var="minimum" value="${itemObj['minimum']}"/>
	<c:set var="maximum" value="${itemObj['maximum']}"/>
	<c:if test="${empty maximum || maximum == 0}"><spring:message var="maximum" code="Globals.upload.file.maximum"/></c:if>
	<ul class="${ulClass}">
		<li>
			<span id="fn_multiFile_${itemId}"><input type="file" name="${itemId}" title="${itemName}" class="${objClass}" style="${objStyle}"/></span>
			<button type="button" id="fn_btn_del_${itemId}" class="${btnClass}"><spring:message code="item.delete.name"/></button>
		</li>
		<li>
			<label for="${itemId}_text" class="fn_text_label"><spring:message code="item.file.replace.only.text"/></label>
			<input type="text" id="${itemId}_text" name="${itemId}_text" title="<spring:message code="item.file.replace.text" arguments="${itemName}"/>" class="altTxt ${objClass}" style="${textStyle}"/>
			<button type="button" id="fn_btn_text_${itemId}" class="${btnClass}"><spring:message code="item.file.text.input.name"/></button>
		</li>
		<li>
			<c:choose>
				<c:when test="${!empty minimum && minimum > 0}">
					<spring:message var="fileCntText" code="item.file.attach.min.max" arguments="${minimum},${maximum}" argumentSeparator=","/>
				</c:when>
				<c:otherwise>
					<spring:message var="fileCntText" code="item.file.attach.max" arguments="${maximum}"/>
				</c:otherwise>
			</c:choose>
			<div class="fileAttachTxt"><spring:message code="item.file.attach.list"/> (${fileCntText})</div>
		</li>
		<li>
			<ul id="${itemId}_total_layer" data-title="${itemName}" class="${selectClass}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if>>
				<c:forEach var="optnDt" items="${fileList}" varStatus="i">
				<li><input type="checkbox" id="${itemId}_total${i.count}" name="${itemId}_total" value="<c:out value="${optnDt.FLE_IDX}"/>||<c:out value="${optnDt.FILE_SIZE}"/>||<c:out value="${optnDt.FILE_ORIGIN_NAME}"/>||<c:out value="${optnDt.FILE_ORIGIN_NAME}"/>||<c:out value="${optnDt.FILE_TEXT}"/>"/><label for="${itemId}_total<c:out value="${optnDt.FLE_IDX}"/>"><c:out value="${optnDt.FILE_ORIGIN_NAME}"/><c:if test="${!empty optnDt.FILE_TEXT}"> :</c:if><c:out value="${optnDt.FILE_TEXT}"/></label></li>
				</c:forEach>
			</ul>
			<ul class="fn_btn_ud">
				<li><button type="button" class="fn_btn_up" id="fn_btn_up_${itemId}"><spring:message code="item.file.order.up.name"/></button></li>
				<li><button type="button" class="fn_btn_down" id="fn_btn_down_${itemId}"><spring:message code="item.file.order.down.name"/></button></li>
			</ul>
		</li>
		<c:if test="${downLink && !empty fileList}">
			<c:set var="keyItemId" value="${settingInfo.idx_name}"/>
			<c:set var="keyColumnId" value="${settingInfo.idx_column}"/>
		<li class="fn_multiFileDown">
			<dl class="saved_file_nb">
				<dt><spring:message code="item.file.saved.name"/></dt>
				<dd>
				<ul>
				<c:forEach var="optnDt" items="${fileList}" varStatus="i">
					<li><a href="<c:out value="${URL_DOWNLOAD}"/>&${keyItemId}=<c:out value="${optnDt[keyColumnId]}"/>&fidx=<c:out value="${optnDt.FLE_IDX}"/>&itId=<c:out value="${itemId}"/>"><c:out value="${optnDt.FILE_ORIGIN_NAME}"/></a></li>
				</c:forEach>
				</ul>
				</dd>
			</dl>
		</li>
		</c:if>
	</ul>
	<input type="hidden" id="${itemId}_deleted_idxs" name="${itemId}_deleted_idxs"/>
</c:if>