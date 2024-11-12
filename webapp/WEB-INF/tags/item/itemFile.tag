<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objOVal"%>
<%@ attribute name="objSVal"%>
<%@ attribute name="objTVal"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="textStyle"%>
<%@ attribute name="ulClass"%>
<%@ attribute name="objClass"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="downLink" type="java.lang.Boolean"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<c:if test="${empty downLink}">
	<c:set var="downLink" value="false"/>
</c:if>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:set var="requiredName" value="required_${inputFlag}"/>
<c:set var="required" value="${itemObj[requiredName]}"/>
<th scope="row"><itui:objectLabel itemId="${itemId}" required="${required}" itemInfo="${itemInfo}"/></th>
<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
	<itui:objectFile itemId="${itemId}" downLink="${downLink}" objDt="${objDt}" objOVal="${objOVal}" objSVal="${objSVal}" objTVal="${objTVal}" itemInfo="${itemInfo}" btnClass="${btnClass}" btnId="${btnId}" objClass="${objClass}" objStyle="${objStyle}" textStyle="${textStyle}" ulClass="${ulClass}"/>
</td>
</c:if>