<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="id"%>
<%@ attribute name="name"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="objNameVal"%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="objAttr"%>
<%@ attribute name="objOptionStyle"%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:set var="nameColumnId" value="${itemObj['name_column_id']}"/>
	<c:if test="${empty nameColumnId}"><c:set var="nameColumnId" value="${itemObj['column_id']}_NAME"/></c:if>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:if test="${objNameVal == null}"><c:set var="objNameVal" value="${objDt[nameColumnId]}"/></c:if>
	<c:if test="${empty defVal}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:set var="optionEtc" value="${itemObj['option_etc']}"/>
<c:forEach var="optnDt" items="${optnHashMap[itemObj['master_code']]}" varStatus="i">
	<c:if test="${empty optnDt.ISHIDDEN || optnDt.ISHIDDEN == '0' || optnDt.OPTION_CODE == objVal}">
	<label for="${id}${i.count}">
		<input type="radio" id="${id}${i.count}" name="${name}" value="<c:out value="${optnDt.OPTION_CODE}"/>"<c:if test="${optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal}"> checked="checked"</c:if><c:if test="${optnDt.OPTION_CODE == defVal}"> data-default="1"</c:if><c:if test="${!empty objAttr}"> ${objAttr}</c:if>/> <c:out value="${optnDt.OPTION_NAME}"/>
		<c:if test="${!empty optionEtc && i.last}">
			<c:if test="${empty objOptionStyle}">
				<c:set var="objOptionStyle" value="${itemObj['object_option_style']}"/>
			</c:if>
			<c:set var="optionObjClass" value="inputTxt fn_optionEtc ${itemObj['option_class_name']}"/>
			<c:if test="${!empty itemObj['option_next_text']}"><div class="input-group"></c:if>
			<c:set var="itemEtcColumnId" value="${itemObj['column_id']}_ETC"/>
			<input type="text" id="${id}${i.count}Etc" name="${name}Etc" class="${optionObjClass}" style="${objOptionStyle}" title="<c:out value="${optnDt.OPTION_NAME}"/> 입력" value="<c:out value="${objDt[itemEtcColumnId]}"/>"<c:if test="${!(optionEtc == objVal || empty objVal && optionEtc == defVal)}"> disabled="disabled"</c:if>/>
			<c:if test="${!empty itemObj['option_next_text']}"><span class="nText"><c:out value="${itemObj['option_next_text']}"/></span></div></c:if>
		</c:if>
	</label>
	</c:if>
</c:forEach>
	<c:if test="${!empty itemObj['name_column_id']}">
	<input type="hidden" id="${id}Name" name="${name}Name" title="${itemName}" value="<c:out value="${objNameVal}"/>"/>
	</c:if>
</c:if>