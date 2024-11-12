<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="objClass1"%>
<%@ attribute name="objClass2"%>
<%@ attribute name="objClass3"%>
<%@ attribute name="objStyle1"%>
<%@ attribute name="objStyle2"%>
<%@ attribute name="objStyle3"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal"%>
<%@ attribute name="dupConfirm"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnText"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:set var="requiredName" value="required_${inputFlag}"/>
<c:set var="required" value="${itemObj[requiredName]}"/>
<th scope="row"><itui:objectLabel itemId="${itemId}" nextItemId="1" required="${required}" itemInfo="${itemInfo}"/></th>
<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
	<itui:objectEMAIL itemId="${itemId}" itemInfo="${itemInfo}" objVal="${objVal}" objDt="${objDt}" objClass1="${objClass1}" objClass2="${objClass2}" objClass3="${objClass3}" objStyle1="${objStyle1}" objStyle2="${objStyle2}" objStyle3="${objStyle3}" dupConfirm="${dupConfirm}" btnId="${btnId}" btnClass="${btnClass}" btnText="${btnText}"/>
</td>
</c:if>