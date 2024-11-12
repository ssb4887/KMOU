<%@tag import="net.sf.json.JSONObject"%>
<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemOrder" type="net.sf.json.JSONArray"%>
<%@ attribute name="items" type="net.sf.json.JSONObject"%>
<%@ attribute name="exceptIds" type="java.lang.String[]"%>
<%@ attribute name="hasPrePwd" type="java.lang.Boolean"%>
<c:if test="${empty hasPrePwd}">
	<c:set var="hasPrePwd" value="false"/>
</c:if>
<c:set var="isItemName" value="${false}"/>
<c:if test="${!empty items && !empty itemOrder}">
<c:forEach var="itemId" items="${itemOrder}" varStatus="i">
	<c:if test="${elfn:arrayIndexOf(exceptIds, itemId) == -1}">
		<%/* 제외항목이 아닌 경우 */%>
		<c:set var="itemObj" value="${items[itemId]}"/>
		<c:set var="useSettingId" value="${itemObj['use_setting_id']}"/>
		<c:if test="${empty useSettingId || !empty useSettingId && settingInfo[useSettingId] == 1}">
		<%/* 사용여부에 따른 출력 : 다기능게시판 - 공지,비밀글,답변상태,게시기간 */%>
			<c:if test="${!empty itemObj}">
				<c:set var="itemObj" value="${items[itemId]}"/>
				<c:set var="itemName" value=""/>
				<c:if test="${!empty itemObj['group_item_id']}"><c:set var="itemName" value="${elfn:getItemName(itemInfo.items[itemObj['group_item_id']])} "/></c:if>
				<c:set var="itemName" value="${itemName}${elfn:getItemName(itemObj)}"/>
				<%-- <c:set var="itemName" value="${elfn:getItemName(itemObj)}"/> --%>
				<c:set var="formatType" value="${itemObj['format_type']}"/>
				<c:set var="objectType" value="${itemObj['object_type']}"/>
				<c:if test="${isItemName}">, </c:if>
				<c:choose>
					<c:when test="${formatType == 9}">
					<%/* 비밀번호 */%>
					<c:if test="${hasPrePwd}"><spring:message code="item.pre_password.name" arguments="${itemName}"/>, </c:if>
					${itemName}, <spring:message code="item.password_reconfirm.name" arguments="${itemName}"/>
					</c:when>
					<c:otherwise>
					${itemName}
					</c:otherwise>
				</c:choose>
				<c:set var="isItemName" value="${true}"/>
			</c:if>
		</c:if>
	</c:if>
</c:forEach>
</c:if>