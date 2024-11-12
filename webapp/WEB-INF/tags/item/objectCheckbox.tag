<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objVal"%>
<%@ attribute name="objValList" type="java.util.List"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="defVal"%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="objOptionStyle"%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
<c:if test="${objValList == null}"><c:set var="objValList" value="${multiDataHashMap[itemId]}"/></c:if>
<c:if test="${defVal == null}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>	
<c:if test="${!empty defVal}"><c:set var="defVals" value="${fn:split(defVal,',')}"/></c:if>
<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
<c:if test="${empty defVal}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>

<c:set var="optionEtc" value="${itemObj['option_etc']}"/>
<c:if test="${!empty optionEtc}">
	<c:set var="optionEtcs" value="${fn:split(optionEtc, ',')}"/>
	<c:if test="${empty objOptionStyle}">
		<c:set var="objOptionStyle" value="${itemObj['object_option_style']}"/>
	</c:if>
</c:if>
<c:forEach var="optnDt" items="${optnHashMap[itemObj['master_code']]}" varStatus="i">
	<c:set var="objChked" value="${false}"/>
	<c:set var="objValDt" value="${elfn:getMatchHashMap(objValList, optnDt.OPTION_CODE, 'ITEM_KEY')}"/>
	<c:if test="${empty optnDt.ISHIDDEN || optnDt.ISHIDDEN == '0' || optnDt.OPTION_CODE == objVal}">
	<label for="${itemId}${i.count}">
		<c:set var="objChked" value="${!empty objValDt || empty objValList && elfn:arrayIndexOf(defVals, optnDt.OPTION_CODE) != -1}"/>
		<input type="checkbox" id="${itemId}${i.count}" name="${itemId}" value="<c:out value="${optnDt.OPTION_CODE}"/>"<c:if test="${objChked}"> checked="checked"</c:if>/> <c:out value="${optnDt.OPTION_NAME}"/>
		<c:if test="${elfn:arrayIndexOf(optionEtcs, optnDt.OPTION_CODE) != -1}">
			<c:set var="optionObjClass" value="inputTxt fn_optionEtc ${itemObj['option_class_name']}"/>
			<c:if test="${!empty itemObj['option_next_text']}"><div class="input-group"></c:if>
			<input type="text" id="${itemId}${i.count}Etc" name="${itemId}Etc_<c:out value="${optnDt.OPTION_CODE}"/>" style="${objOptionStyle}" class="${optionObjClass}<c:if test="${!objChked}"> disabled</c:if>" title="<c:out value="${optnDt.OPTION_NAME}"/> 입력" value="<c:out value="${objValDt.ITEM_ETC}"/>"<c:if test="${!objChked}"> disabled="disabled"</c:if>/>
			<c:if test="${!empty itemObj['option_next_text']}"><span class="nText"><c:out value="${itemObj['option_next_text']}"/></span></div></c:if>
		</c:if>
	</label>
	</c:if>
</c:forEach>
</c:if>