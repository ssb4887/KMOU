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
<%@ attribute name="optnOptList" type="java.util.List"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="objIdxVal"%>
<%@ attribute name="itemRequried"%>
<%@ attribute name="datalistId"%>
<%@ attribute name="disabledId" type="java.lang.Boolean"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:if test="${empty disabledId}">
	<c:set var="disabledId" value="false"/>
</c:if>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:if test="${empty optnHashMap}"><c:set var="optnHashMap" value="${optnHashMap}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:set var="nameColumnId" value="${itemObj['name_column_id']}"/>
	<c:if test="${empty nameColumnId}"><c:set var="nameColumnId" value="${itemObj['column_id']}_NAME"/></c:if>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:if test="${objIdxVal == null}"><c:set var="objIdxVal" value="${objDt[nameColumnId]}"/></c:if>
	<c:if test="${empty objStyle}"><c:set var="objStyle" value="${itemObj['object_style']}"/></c:if>
	<c:if test="${empty objClass}"><c:set var="objClass" value="inputTxt"/></c:if>
	
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	<c:set var="itemObjectType" value="${itemObj['object_type']}"/>
	<c:set var="itemOptionType" value="${itemObj['option_type']}"/>
	<c:if test="${empty defVal}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>
	<c:if test="${empty optnOptList}">
		<c:choose>
			<c:when test="${!empty itemOptionType && itemOptionType != 0}"><c:set var="optnOptList" value="${optnHashMap[itemId]}"/></c:when>
			<c:otherwise><c:set var="optnOptList" value="${optnHashMap[itemObj['master_code']]}"/></c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${empty datalistId}"><c:set var="datalistId" value="${id}_dataist"/></c:if>
	
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
	<spring:message var="codeItemName" code="item.itemId.code.name" arguments="${itemName}"/>
	<c:choose>
	<c:when test="${inputType == 10}">
		<span id="fn_item_${id}"><c:out value="${objVal}"/></span>
		<input type="hidden"<c:if test="${!disabledId}">  id="${id}"</c:if> name="${name}" title="${codeItemName}" value="<c:out value="${objVal}"/>"/>
		<input type="hidden"<c:if test="${!disabledId}">  id="${id}Idx"</c:if> name="${name}Idx" title="${codeItemName}" value="<c:out value="${objIdxVal}"/>"/>
	</c:when>
	<c:otherwise>
		<input type="text" list="${datalistId}"<c:if test="${!disabledId}"> id="${id}"</c:if> name="${name}" title="${itemName}" class="${objClass}" style="${objStyle}"<c:if test="${required == 1}"> required="required"</c:if> value="<c:out value="${objVal}"/>"${objAttr}/>
		<input type="hidden"<c:if test="${!disabledId}"> id="${id}Idx"</c:if> name="${name}Idx" title="${codeItemName}" value="<c:out value="${objIdxVal}"/>"/>
		<datalist id="${datalistId}">
			<c:forEach var="optnDt" items="${optnOptList}" varStatus="i">
				<c:choose>
					<c:when test="${limitCnt > 0}">
						<c:set var="optnName" value="${elfn:getInlineContents(optnDt.OPTION_NAME, limitCnt)}"/>
					</c:when>
					<c:otherwise>
						<c:set var="optnName" value="${optnDt.OPTION_NAME}"/>
					</c:otherwise>
				</c:choose>
				
				<c:if test="${(empty minDefVal || optnDt.OPTION_CODE >= minDefVal) && (empty optnDt.ISHIDDEN || optnDt.ISHIDDEN == '0' || optnDt.OPTION_CODE == objIdxVal)}">
				<option value="<c:out value="${optnName}"/>" data-code="<c:out value="${optnDt.OPTION_CODE}"/>"<c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if><c:if test="${optnDt.OPTION_CODE == objIdxVal || empty objIdxVal && optnDt.OPTION_CODE == defVal}"> selected="selected"</c:if><c:if test="${optnDt.OPTION_CODE == defVal}"> data-default="1"</c:if><c:if test="${!empty optnDt.CONF_MODULE}"> data-confMd="<c:out value="${optnDt.CONF_MODULE}"/>"</c:if><c:if test="${!empty optnDt.MENU_LINK}"> data-link="<c:out value="${optnDt.MENU_LINK}"/>"</c:if><c:if test="${!empty optnDt.AN_MENU_LINK}"> data-anlink="<c:out value="${optnDt.AN_MENU_LINK}"/>"</c:if><c:if test="${!empty optnDt.MENU_LINK2}"> data-link2="<c:out value="${optnDt.MENU_LINK2}"/>"</c:if><c:if test="${!empty optnDt.AN_MENU_LINK2}"> data-anlink2="<c:out value="${optnDt.AN_MENU_LINK2}"/>"</c:if><c:if test="${!empty optnDt.OPTION_LEVEL}"> data-level="<c:out value="${optnDt.OPTION_LEVEL}"/>"</c:if>><c:if test="${itemObjectType == 22 || itemOptionType == 3}"><c:if test="${optnDt.OPTION_LEVEL > 1}"><c:forEach var="optLevel" begin="2" end="${optnDt.OPTION_LEVEL}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if></c:if><c:if test="${addOrder}">${i.count}. </c:if><c:out value="${optnName}"/></option>
				</c:if>
			</c:forEach>
		</datalist>
		<c:if test="${!empty itemObj['comment']}"><span class="comment"><c:out value="${itemObj['comment']}"/></span></c:if>
	</c:otherwise>
	</c:choose>
</c:if>