<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="name"%>
<%@ attribute name="id"%>
<%@ attribute name="optnOptList" type="java.util.List"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="minDefVal" type="java.lang.Integer" %>
<%@ attribute name="objNameVal"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="addOrder" type="java.lang.Boolean"%>
<%@ attribute name="idxColumnId"%>
<%@ attribute name="itemRequried"%>
<%@ attribute name="itemOptBlankTitle" %>
<%@ attribute name="disabled" type="java.lang.Boolean"%>
<%@ attribute name="disabledId" type="java.lang.Boolean"%>
<%@ attribute name="limitCnt" type="java.lang.Integer"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<c:if test="${empty addOrder}">
	<c:set var="addOrder" value="false"/>
</c:if>
<c:if test="${empty disabled}">
	<c:set var="disabled" value="false"/>
</c:if>
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
	<c:if test="${objNameVal == null}"><c:set var="objNameVal" value="${objDt[nameColumnId]}"/></c:if>
	<c:set var="itemObjectType" value="${itemObj['object_type']}"/>
	<c:set var="itemOptionType" value="${itemObj['option_type']}"/>
	<c:if test="${!empty idxColumnId}"><c:set var="objVal" value="${dt[idxColumnId]}"/></c:if>
	<c:if test="${empty defVal}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>
	<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
	<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
	<c:set var="optionEtc" value="${itemObj['option_etc']}"/>
	
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
	<c:if test="${empty optnOptList}">
		<c:choose>
			<c:when test="${!empty itemOptionType && itemOptionType != 0}"><c:set var="optnOptList" value="${optnHashMap[itemId]}"/></c:when>
			<c:otherwise><c:set var="optnOptList" value="${optnHashMap[itemObj['master_code']]}"/></c:otherwise>
		</c:choose>
	</c:if>
	<c:choose>
		<c:when test="${inputType == 10}">
			<c:forEach var="optnDt" items="${optnOptList}" varStatus="i">
				<c:if test="${optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal}"><c:out value="${optnDt.OPTION_NAME}"/></c:if>
			</c:forEach>
			<input type="hidden" id="${itemId}" name="${name}" title="${itemName}" value="<c:out value="${objVal}"/>"/>
		</c:when>
		<c:otherwise>
			<c:if test="${!empty itemObj['next_text']}"><div class="input-group"></c:if>
			<c:if test="${empty itemOptBlankTitle}"><spring:message var="itemOptBlankTitle" code="item.itemId.select.name" arguments="${itemName}"/></c:if>
			<select<c:if test="${!disabledId}"> id="${id}"</c:if> name="${name}" title="${itemName}" style="${objStyle}" class="select"<c:if test="${required == 1}"> required="required"</c:if><c:if test="${disabled}"> disabled="disabled"</c:if><c:if test="${!empty defVal}"> data-dval="<c:out value="${defVal}"/>"</c:if>>
				<option value="">${itemOptBlankTitle}</option>
			<c:set var="optnSelIdx" value="0"/>
			<c:forEach var="optnDt" items="${optnOptList}" varStatus="i">
				<c:choose>
					<c:when test="${limitCnt > 0}">
						<c:set var="optnName" value="${elfn:getInlineContents(optnDt.OPTION_NAME, limitCnt)}"/>
					</c:when>
					<c:otherwise>
						<c:set var="optnName" value="${optnDt.OPTION_NAME}"/>
					</c:otherwise>
				</c:choose>
				
				<c:if test="${(empty minDefVal || optnDt.OPTION_CODE >= minDefVal) && (empty optnDt.ISHIDDEN || optnDt.ISHIDDEN == '0' || optnDt.OPTION_CODE == objVal)}">
				<option value="<c:out value="${optnDt.OPTION_CODE}"/>"<c:if test="${optnDt.OPTION_CODE == optionEtc}"> data-etc="${name}${i.count}Etc"</c:if><c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if><c:if test="${optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal}"> selected="selected"</c:if><c:if test="${optnDt.OPTION_CODE == defVal}"> data-default="1"</c:if><c:if test="${!empty optnDt.CONF_MODULE}"> data-confMd="<c:out value="${optnDt.CONF_MODULE}"/>"</c:if><c:if test="${!empty optnDt.MENU_LINK}"> data-link="<c:out value="${optnDt.MENU_LINK}"/>"</c:if><c:if test="${!empty optnDt.AN_MENU_LINK}"> data-anlink="<c:out value="${optnDt.AN_MENU_LINK}"/>"</c:if><c:if test="${!empty optnDt.MENU_LINK2}"> data-link2="<c:out value="${optnDt.MENU_LINK2}"/>"</c:if><c:if test="${!empty optnDt.AN_MENU_LINK2}"> data-anlink2="<c:out value="${optnDt.AN_MENU_LINK2}"/>"</c:if><c:if test="${!empty optnDt.OPTION_LEVEL}"> data-level="<c:out value="${optnDt.OPTION_LEVEL}"/>"</c:if>><c:if test="${itemObjectType == 22 || itemOptionType == 3}"><c:if test="${optnDt.OPTION_LEVEL > 1}"><c:forEach var="optLevel" begin="2" end="${optnDt.OPTION_LEVEL}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if></c:if><c:if test="${addOrder}">${i.count}. </c:if><c:out value="${optnName}"/></option>
					<c:if test="${optnDt.OPTION_CODE == optionEtc}">
						<c:if test="${empty objOptionStyle}">
							<c:set var="objOptionStyle" value="${itemObj['object_option_style']}"/>
						</c:if>
						<c:set var="etcInputStr">
						<c:set var="optionObjClass" value="inputTxt fn_optionEtc ${itemObj['option_class_name']}"/>
						<c:if test="${!empty itemObj['option_next_text']}"><div class="input-group ${name}Etc"></c:if>
						<c:set var="itemEtcColumnId" value="${itemObj['column_id']}_ETC"/>
						<c:set var="etcDisabled" value="${true}"/>
						<c:if test="${optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal}"><c:set var="etcDisabled" value="${false}"/></c:if>
						<input type="text"<c:if test="${!disabledId}"> id="${id}${i.count}Etc"</c:if> name="${name}Etc" data-id="${name}${i.count}Etc"  class="fn_etc ${optionObjClass}<c:if test="${etcDisabled}"> disabled</c:if>" style="${objOptionStyle}" title="<c:out value="${optnDt.OPTION_NAME}"/> 입력" value="<c:out value="${objDt[itemEtcColumnId]}"/>"<c:if test="${etcDisabled}"> disabled="disabled"</c:if>/>
						<c:if test="${!empty itemObj['option_next_text']}"><span class="nText"><c:out value="${itemObj['option_next_text']}"/></span></div></c:if>
						</c:set>
					</c:if>
					<c:set var="optnSelIdx" value="${optnSelIdx + 1}"/>
				</c:if>
			</c:forEach>
			</select>
			<c:if test="${!empty itemObj['next_text']}"><span class="nText"><c:out value="${itemObj['next_text']}"/></span></div></c:if>
			${etcInputStr}
		</c:otherwise>
	</c:choose>
	<c:if test="${!empty itemObj['name_column_id']}">
	<input type="hidden" id="${id}Name" name="${name}Name" title="${itemName}" value="<c:out value="${objNameVal}"/>"/>
	</c:if>
</c:if>