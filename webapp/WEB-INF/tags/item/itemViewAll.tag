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
<%@ attribute name="itemOrderName" required="true"%>							<%/* 항목ID 목록 */%>
<%@ attribute name="exceptIds" type="java.lang.String[]"%>						<%/* 제외 항목ID */%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>						<%/* 코드값 정보 */%>
<%@ attribute name="multiFileHashMap" type="java.util.HashMap"%>				<%/* multi파일값 정보 */%>
<%@ attribute name="multiDataHashMap" type="java.util.HashMap"%>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:if test="${!empty itemOrder}">
	<c:forEach var="itemId" items="${itemOrder}">
		<c:if test="${elfn:arrayIndexOf(exceptIds, itemId) == -1}">
			<%/* 제외항목이 아닌 경우 */%>
			<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
			
			<c:set var="inputType" value="${itemObj['write_type']}"/>
			<c:set var="defaultItem" value="${itemObj['default_item']}"/>
			<c:set var="useSettingId" value="${itemObj['use_setting_id']}"/>
			<c:if test="${empty useSettingId || !empty useSettingId && settingInfo[useSettingId] == 1}">
			<%/* 사용여부에 따른 출력 : 다기능게시판 - 공지,비밀글,답변상태,게시기간 */%>
			<c:if test="${!empty itemObj}">
<tr>
	<c:set var="itemFormatType" value="${itemObj['format_type']}"/>
	<c:set var="itemObjectType" value="${itemObj['object_type']}"/>
	<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
	<c:set var="itemObjStyle" value="${itemObj['object_style']}"/>
	<c:set var="itemObjClass" value="${itemObj['object_class']}"/>				<%/* 현재 미사용 - 추 후 사용가능성 */%>
	<c:set var="itemMaximum" value="${itemObj['maximum']}"/>
	
	<th scope="row"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
	<td<c:if test="${colspan > 0}"> colspan="${colspan}"</c:if>>
		<itui:objectView itemId="${itemId}" itemInfo="${itemInfo}" objDt="${objDt}" optnHashMap="${optnHashMap}" multiFileHashMap="${multiFileHashMap}" multiDataHashMap="${multiDataHashMap}"/>
	</td>
</tr>
			</c:if>
			</c:if>
		</c:if>
	</c:forEach>
</c:if>