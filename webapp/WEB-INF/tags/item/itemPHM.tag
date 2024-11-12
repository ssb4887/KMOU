<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="objClass"%>
<%@ attribute name="objStyle"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="objVal11"%>
<%@ attribute name="objVal12"%>
<%@ attribute name="objVal21"%>
<%@ attribute name="objVal22"%>
<%@ attribute name="btnId"%>
<%@ attribute name="btnClass"%>
<%@ attribute name="btnText"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>					<%/* 항목설정정보 */%>
<% /* 항목(th,td) 설정별 출력 */ %>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
	<c:set var="itemColumnId11" value="${itemObj['column_id']}11"/>
	<c:set var="itemColumnId12" value="${itemObj['column_id']}12"/>
	<c:set var="itemColumnId21" value="${itemObj['column_id']}21"/>
	<c:set var="itemColumnId22" value="${itemObj['column_id']}22"/>
	<c:if test="${objVal11 == null}"><c:set var="objVal11" value="${objDt[itemColumnId11]}"/></c:if>
	<c:if test="${objVal12 == null}"><c:set var="objVal12" value="${objDt[itemColumnId12]}"/></c:if>
	<c:if test="${objVal21 == null}"><c:set var="objVal21" value="${objDt[itemColumnId21]}"/></c:if>
	<c:if test="${objVal22 == null}"><c:set var="objVal22" value="${objDt[itemColumnId22]}"/></c:if>
	<c:set var="requiredName" value="required_${inputFlag}"/>
	<c:set var="required" value="${itemObj[requiredName]}"/>
<th scope="row"><itui:objectLabel itemId="${itemId}" nextItemId="11" required="${required}" itemInfo="${itemInfo}"/></th>
<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
	<itui:objectPHM itemId="${itemId}" itemInfo="${itemInfo}" objDt="${objDt}" objVal11="${objVal11}" objVal12="${objVal12}" objVal21="${objVal21}" objVal22="${objVal22}" btnId="${btnId}" btnClass="${btnClass}" btnText="${btnText}" objClass="${objClass}" objStyle="${objStyle}"/>
</td>
</c:if>