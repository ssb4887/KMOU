<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="itemOrder" type="net.sf.json.JSONArray"%>
<%@ attribute name="items" type="net.sf.json.JSONObject"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<c:set var="inputFlag" value="${inputTypeName}"/>
<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:if test="${!empty items && !empty itemOrder}">
<c:set var="requiredName" value="required_${inputFlag}"/>
<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
<c:forEach var="itemId" items="${itemOrder}">
	<c:set var="itemObj" value="${items[itemId]}"/>
	<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
	<c:if test="${isAdmMode || inputType != 20}">
		<c:set var="formatType" value="${itemObj['format_type']}"/>
		<c:set var="objectType" value="${itemObj['object_type']}"/>
		<c:set var="required" value="${itemObj[requiredName]}"/>
		<c:set var="itemName" value="${itemObj['item_name']}"/>
		<c:set var="itemtype" value="${itemObj['item_type']}"/>
	
		<c:choose>
			<c:when test="${formatType == 3}">
			<%/* 이메일 */%>
			try{fn_initEmailValue($("#${itemId}3"), '${itemId}2');}catch(e){}
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${objectType == 6}">
					<%/* file */%>
					try {$('#fn_saved_${itemId}').show();} catch(e){}
					</c:when>
					<c:when test="${objectType == 9}">
					<%/* multi file */%>
					try { $('#${itemId}_total_layer>li').remove();} catch(e){}
					var varDH = new HashMap();
					<c:forEach var="optnDt" items="${multiFileHashMap[itemId]}">
					try {
						fn_addFileList("${itemId}", "${optnDt["FLE_IDX"]}", "${optnDt["FILE_SIZE"]}", "${optnDt["FILE_ORIGIN_NAME"]}", "${optnDt["FILE_TEXT"]}");
						varDH.put("k${optnDt["FLE_IDX"]}", false);
					} catch(e){}
					</c:forEach>
						gVarDeleteFiles.put("${itemId}", varDH);
					</c:when>
					<c:when test="${objectType == 22}">
					<%/* 3차 select */%>
					try {
						fn_setItemOrderOption("${itemId}");
					} catch(e){}
					</c:when>
					<c:when test="${objectType == 1}">
					<%/* textarea */%>
					<c:if test="${itemObj['editor'] == '1' && (itemObj['editor_utype'] == '1' || isAdmMode && (empty itemObj['editor_utype'] || itemObj['editor_utype'] == '0'))}">
					<%/* 네이버 스마트에디터 사용하는 경우 */%>
					try {
						<%
						// 관리자모드  && text 사용 (html 사용 안하는 경우)
						// 사용자모드 입력 후 관리자모드에서 수정하는 경우
						%>
						var varType = $("#${itemId}").attr("data-type");
						var varECon;
						if(varType == '2') varECon = $("#${itemId}").val();
						else varECon = $("#${itemId}").html();
						oEditors.getById["${itemId}"].setIR(varECon);
					} catch(e) {}
					</c:if>
					</c:when>
					<c:when test="${objectType == 22}">
					<%/* select (다차원) */%>
					<c:if test="${!empty classOptions}">
					<%// 추후작업%>
					</c:if>
					</c:when>
					<c:otherwise>
					<%/* 그외 입력  */%>
						<c:choose>
						<c:when test="${itemtype == '11'}">
							<%/* 통화(원)  */%>
							fn_setFormatCurrent($("#${itemId}"));
						</c:when>
						<c:otherwise>
						<%/* 그외 입력  */%>
						</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		</c:if>
	</c:forEach>
</c:if>
	