<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="colspan" type="java.lang.Integer"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>	<%/* 저장 값 */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject" required="true"%>	<%/* 항목설정정보 */%>
<%@ attribute name="settingInfo" type="net.sf.json.JSONObject"%>	<%/* 설정정보 */%>
<%@ attribute name="itemOrderName" required="true"%>							<%/* 항목ID 목록 */%>
<%@ attribute name="exceptIds" type="java.lang.String[]"%>						<%/* 제외 항목ID */%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>						<%/* 코드값 정보 */%>
<%@ attribute name="multiFileHashMap" type="java.util.HashMap"%>				<%/* multi파일값 정보 */%>
<%@ attribute name="itemLangList" type="java.util.List"%>						<%/* 언어목록 */%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<% /* 전체항목(tr,th,td) 설정별 출력 */ %>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:if test="${!empty itemOrder}">
	<c:if test="${empty settingInfo}"><c:set var="settingInfo" value="${settingInfo}"/></c:if>
	<c:set var="inputFlag" value="${inputTypeName}"/>
	<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
	<c:forEach var="itemId" items="${itemOrder}">
		<c:if test="${elfn:arrayIndexOf(exceptIds, itemId) == -1}">
			<%/* 제외항목이 아닌 경우 */%>
			<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
			<c:set var="useSettingId" value="${itemObj['use_setting_id']}"/>
			<c:set var="useSetting" value="${1}"/>
			<c:if test="${!empty useSettingId && !empty settingInfo}"><c:set var="useSetting" value="${settingInfo[useSettingId]}"/></c:if>
			<c:if test="${useSetting == 1}">
			<%/* 사용여부에 따른 출력 : 다기능게시판 - 공지,비밀글,답변상태,게시기간,파일 */%>
			<c:if test="${!empty itemObj}">
				<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
				<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
				<c:if test="${isAdmMode || inputType != 20}">
				<%/* 관리시스템  || 관리시스템에서만 저장하는 항목 아닌 경우 */ %>
<tr>
	<itui:itemInput inputTypeName="${inputTypeItemName}" itemId="${itemId}" itemInfo="${itemInfo}" objDt="${objDt}" optnHashMap="${optnHashMap}" multiFileHashMap="${multiFileHashMap}" colspan="${colspan}" itemLangList="${itemLangList}"/>
</tr>
			</c:if>
			</c:if>
			</c:if>
		</c:if>
	</c:forEach>
</c:if>