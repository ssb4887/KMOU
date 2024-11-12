<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="defVal"%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="downloadUrl"%>
<%@ attribute name="classSplitStr"%>								<%/* 분류 구분자 */%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
<c:if test="${empty optnHashMap}"><c:set var="optnHashMap" value="${optnHashMap}"/></c:if>
<c:set var="itemFormatType" value="${itemObj['format_type']}"/>
<c:set var="itemObjectType" value="${itemObj['object_type']}"/>
<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
	<th scope="row"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
	<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
	<itui:objectView itemId="${itemId}" itemInfo="${itemInfo}" objDt="${objDt}" defVal="${defVal}" objVal="${objVal}" optnHashMap="${optnHashMap}" downloadUrl="${downloadUrl}" classSplitStr="${classSplitStr}"/>
	</td>
</c:if>