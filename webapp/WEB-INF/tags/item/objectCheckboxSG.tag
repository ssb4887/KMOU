<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="itemId"%>
<%@ attribute name="name"%>
<%@ attribute name="id"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="optnCode"%>
<%@ attribute name="optnName"%>
<%@ attribute name="disabledId" type="java.lang.Boolean"%>
<c:if test="${empty disabledId}">
	<c:set var="disabledId" value="false"/>
</c:if>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<c:if test="${empty defVal}">
		<c:set var="defVal" value="${itemObj['default_value']}"/>
	</c:if>
	<c:if test="${empty optnCode && !empty optnHashMap}">
		<c:forEach var="optnDt" items="${optnHashMap[itemObj['master_code']]}" varStatus="i">
			<c:set var="optnCode" value="${optnDt.OPTION_CODE}"/>
			<c:set var="optnName" value="${optnDt.OPTION_NAME}"/>
		</c:forEach>
	</c:if>
	<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
	<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
	<c:if test="${empty optnCode}">
		<c:set var="optnCode" value="1"/>
		<c:set var="optnName" value="${elfn:getItemName(itemInfo.items[itemId])}"/>
	</c:if>
	<label for="${id}"><input type="checkbox"<c:if test="${!disabledId}"> id="${id}"</c:if> name="${name}" value="<c:out value="${optnCode}"/>"<c:if test="${optnCode == objVal || empty sVal && optnCode == defVal}"> checked="checked"</c:if>/> <c:out value="${optnName}"/></label>
</c:if>