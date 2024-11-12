<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="settingInfo" type="net.sf.json.JSONObject"%>	<%/* 설정정보 */%>
<%@ attribute name="itemOrder" type="net.sf.json.JSONArray" required="true"%>
<%@ attribute name="items" type="net.sf.json.JSONObject" required="true"%>
<%@ attribute name="exceptIds" type="java.lang.String[]"%>						<%/* 제외 항목ID */%>
<%@ attribute name="itemLangList" type="java.util.List"%>						<%/* 언어목록 */%>
<%@ attribute name="itemLocaleLang"%>											<%/* 언어 */%>
<%@ attribute name="selectEtcVal"%>
<%@ attribute name="preItemId"%>
<%@ attribute name="nextItemId"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<%@ attribute name="multiRowObj"%>												<%/* row object */%>
<%@ attribute name="isExcValidOfS" type="java.lang.Boolean"%>					<%/* 한개 row만 있는 경우 필수체크 제외 여부 */%>
<%@ attribute name="isEnd" type="java.lang.Boolean"%>							<%/* 마지막 스크립트인 경우 */%>
<c:if test="${empty isExcValidOfS}">
	<c:set var="isExcValidOfS" value="false"/>
</c:if>
<c:if test="${empty isEnd}">
	<c:set var="isEnd" value="false"/>
</c:if>
<c:set var="inputFlag" value="${inputTypeName}"/>
<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:if test="${!empty items && !empty itemOrder}">
<c:if test="${empty settingInfo}"><c:set var="settingInfo" value="${settingInfo}"/></c:if>
<c:set var="requiredName" value="required_${inputFlag}"/>
<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
try{
var varMultiChked = false;
var varMultiOInput = false;
var varMultiChkItemName, varMultiChkObj;
$.each(${multiRowObj}, function(i ,item){
	varMultiOInput = false;
<c:forEach var="itemId" items="${itemOrder}">
	<c:set var="itemObj" value="${items[itemId]}"/>
	<c:if test="${!empty itemObj}">
	<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
	<c:if test="${isAdmMode || inputType != 20}">
		<c:set var="formatType" value="${itemObj['format_type']}"/>
		<c:set var="objectType" value="${itemObj['object_type']}"/>
		<c:set var="required" value="${itemObj[requiredName]}"/>
		<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
		<c:set var="useLang" value="${itemObj['use_lang']}"/>
		<c:set var="minimum" value="${itemObj['minimum']}"/>
		<c:set var="maximum" value="${itemObj['maximum']}"/>
		<c:set var="id" value="${itemId}"/>
		<c:set var="name" value="${itemId}"/>
		<c:if test="${!empty preItemId}">
			<c:set var="id" value="${preItemId}${id}"/>
			<c:set var="name" value="${preItemId}${name}"/>
		</c:if>
		<c:if test="${!empty nextItemId}">
			<c:set var="id" value="${id}${nextItemId}"/>
			<c:set var="name" value="${name}${nextItemId}"/>
		</c:if>
		<%/* 제외항목이 아닌 경우 */%>
		<c:set var="useSettingId" value="${itemObj['use_setting_id']}"/>
		<%/* 사용여부에 따른 출력 : 다기능게시판 - 공지,비밀글,답변상태,게시기간,파일 */%>
	
	<c:set var="useSetting" value="${1}"/>
	<c:if test="${!empty useSettingId && !empty settingInfo}"><c:set var="useSetting" value="${settingInfo[useSettingId]}"/></c:if>
	<c:if test="${elfn:arrayIndexOf(exceptIds, itemId) == -1 && useSetting == 1 && inputType != 10}">
	<%/* 저장값 setting */%>
	<c:choose>
		<c:when test="${formatType == 3}">
		try{
			if(fn_isFill($(this).find("[name='${name}1']")) || fn_isFill($(this).find("[name='${name}2']"))) {
				$(this).find("[name='${name}']").val($(this).find("[name='${name}1']").val() + '@' + $(this).find("[name='${name}2']").val());
			}
		}catch(e){}
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${objectType == 1 && itemObj['editor'] == '1' && (itemObj['editor_utype'] == '1' || isAdmMode && (empty itemObj['editor_utype'] || itemObj['editor_utype'] == '0'))}">
				<%/* 네이버 스마트에디터 사용하는 경우 */%>
				try {oEditors.getById[$(this).find("[name='${name}']").attr("id")].exec("UPDATE_CONTENTS_FIELD", []);}catch(e){}
				</c:when>
				<c:when test="${objectType == 22}">
				try {
					var varObj = $(this).find("select[name='${name}']");
					$(this).find("select[name='${name}_tmp']").each(function(index, item){
						var varVal = $(item).val();
						if(varVal) varObj.val(varVal);
					});
				}catch(e){}
				</c:when>
			</c:choose>
		</c:otherwise>
	</c:choose>
	<c:if test="${required == '1'}">
	<%/* 필수입력 */%>
	<c:if test="${isExcValidOfS}">
	//if(${multiRowObj}.length > 1) {
	</c:if>
		<c:choose>
			<c:when test="${formatType == 1 || formatType == 2}">
			<%/* 전화번호 */%>
			try{if(!fn_isSelected($(this).find("[name='${name}1']"))) {
					$(this).find("[name='${name}1']").focus();
					varMultiChkItemName = "<c:out value="${itemName}"/>";
					varMultiChked = true;
					<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
				} else {
					varMultiOInput = true;
					if(varMultiChked) return false;
				}}catch(e){}
			try{if(!fn_isFill($(this).find("[name='${name}2']"), "<c:out value="${itemName}"/>")) {
					$(this).find("[name='${name}2']").focus();
					varMultiChkItemName = "<c:out value="${itemName}"/>";
					varMultiChked = true;
					<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
				} else {
					varMultiOInput = true;
					if(varMultiChked) return false;
				}}catch(e){}
			try{if(!fn_isFill($(this).find("[name='${name}3]"), "<c:out value="${itemName}"/>")) {
					$(this).find("[name='${name}3']").focus();
					varMultiChkItemName = "<c:out value="${itemName}"/>";
					varMultiChked = true;
					<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
				} else {
					varMultiOInput = true;
					if(varMultiChked) return false;
				}}catch(e){}
			</c:when>
			<c:when test="${formatType == 3}">
			<%/* 이메일 */%>
			try{if(!fn_isFill($(this).find("[name='${name}1']"), "<c:out value="${itemName}"/>")) {
					$(this).find("[name='${name}1']").focus();
					varMultiChkItemName = "<c:out value="${itemName}"/>";
					varMultiChked = true;
					<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
				} else {
					varMultiOInput = true;
					if(varMultiChked) return false;
				}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}3']"), "<c:out value="${itemName}"/>")) {
					$(this).find("[name='${name}3']").focus();
					varMultiChkItemName = "<c:out value="${itemName}"/>";
					varMultiChked = true;
				} else {
					varMultiOInput = true;
					if(varMultiChked) return false;
				}}catch(e){}
			try{if(!fn_isFill($(this).find("[name='${name}2']"), "<c:out value="${itemName}"/>")) {
					$(this).find("[name='${name}2']").focus();
					varMultiChkItemName = "<c:out value="${itemName}"/>";
					varMultiChked = true;
				} else {
					varMultiOInput = true;
					if(varMultiChked) return false;
				}}catch(e){}
			</c:when>
			<c:when test="${formatType == 4}">
			<%/* 주소 */%>
			try{
				if(!fn_isFill($(this).find("[name='${name}1']"))) {
					//alert(fn_Message.checkText('<c:out value="${itemName}"/>'));
					$(this).find("#fn_btn_" + $(this).find("[name='${name}']").attr("id")).focus();
					varMultiChkItemName = "<c:out value="${itemName}"/>";
					varMultiChked = true;
					<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
				} else {
					varMultiOInput = true;
					if(varMultiChked) return false;
				}
			}catch(e){}
			try{if(!fn_isFill($(this).find("[name='${name}']2"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']2").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isFill($(this).find("[name='${name}']3"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']3").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 19}">
			<%/* 년도 */%>
			try{if(!fn_isSelected($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 20 || formatType == 21}">
			<%/* 년도-월 / 년도-분기 */%>
			try{if(!fn_isSelected($(this).find("[name='${name}1']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}1']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}']2"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']2").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 22}">
			<%/* 년도-월-일 */%>
			try{if(!fn_isSelected($(this).find("[name='${name}1']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}1']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}']2"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']2").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}']3"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']3").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 6}">
			<%/* 날짜 시:분:초 */%>
			try{if(!fn_isFill($(this).find("[name='${name}1']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}1']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}']2"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']2").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}']3"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']3").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}']4"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']4").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 7}">
			<%/* 날짜 시:분 */%>
			try{if(!fn_isFill($(this).find("[name='${name}1']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}1']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}']2"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']2").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}']3"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']3").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 8}">
			<%/* 날짜 시 */%>
			try{if(!fn_isFill($(this).find("[name='${name}1']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}1']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}2']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}2']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 15}">
			<%/* 날짜(기간) */%>
			try{if(!fn_isFill($(this).find("[name='${name}1']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}1']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isFill($(this).find("[name='${name}2']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}2']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 16}">
			<%/* 날짜 시:분:초(기간) */%>
			try{if(!fn_isFill($(this).find("[name='${name}11']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}11']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}12']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}12']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}13']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}13']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}14']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}14']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isFill($(this).find("[name='${name}21']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}21']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}22']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}22']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}23']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}23']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}24']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}24']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 17}">
			<%/* 날짜 시:분(기간) */%>
			try{if(!fn_isFill($(this).find("[name='${name}11']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}11']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}12']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}12']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}13']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}13']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isFill($(this).find("[name='${name}21']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}21']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}22']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}22']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}23']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}23']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 18}">
			<%/* 날짜 시(기간) */%>
			try{if(!fn_isFill($(this).find("[name='${name}11']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}11']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}12']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}12']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isFill($(this).find("[name='${name}21']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}21']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}22']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}22']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 29}">
			<%/* 년도(기간) */%>
			try{if(!fn_isSelected($(this).find("[name='${name}1']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}1']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}2']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}2']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 30}">
			<%/* 년도-월(기간) */%>
			try{if(!fn_isSelected($(this).find("[name='${name}11']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}11']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}12']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}12']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}21']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}21']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if(!fn_isSelected($(this).find("[name='${name}22']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}22']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 9}">
			<%/* 비밀번호 */%>
			try{if(!fn_isFill($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			try{if($("#" + $(this).find("[name='${name}']").attr("id") + "_reconfirm").size() > 0 && !fn_isFill($("#" + $(this).find("[name='${name}']").attr("id") + "_reconfirm"), "<spring:message code="item.password_reconfirm.name" arguments="${itemName}"/>")) {
			
				$("#" + $(this).find("[name='${name}']").attr("id") + "_reconfirm").focus();
				varMultiChkItemName = "<spring:message code="item.password_reconfirm.name" arguments="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:when test="${formatType == 10}">
			<%/* 아이디 */%>
			try{if(!fn_isFill($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
				$(this).find("[name='${name}']").focus();
				varMultiChkItemName = "<c:out value="${itemName}"/>";
				varMultiChked = true;
				<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
			} else {
				varMultiOInput = true;
				if(varMultiChked) return false;
			}}catch(e){}
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${objectType == 4 || objectType == 5}">
					<%/* checkbox / radio */%>
					try{if(!fn_checkElementChecked($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
						$(this).find("[name='${name}']").focus();
						varMultiChkItemName = "<c:out value="${itemName}"/>";
						varMultiChked = true;
						<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
					} else {
						varMultiOInput = true;
						if(varMultiChked) return false;
					}}catch(e){}
					</c:when>
					<c:when test="${objectType == 6}">
					<%/* file */%>
					try{if($(this).find("[name='${name}_saved']").is("input") && (!fn_isFill($(this).find("[name='${name}_saved']")) || fn_isFill($("#" + $(this).find("[name='${name}").attr("id") + "_deleted_idxs"))) && !fn_isFill($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>") || !$(this).find("[name='${name}_saved']").is("input") && !fn_isFill($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
						$(this).find("[name='${name}']").focus();
						varMultiChkItemName = "<c:out value="${itemName}"/>";
						varMultiChked = true;
						<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
					} else {
						varMultiOInput = true;
						if(varMultiChked) return false;
					}}catch(e){}
					</c:when>
					<c:when test="${objectType == 9}">
					<%/* multi file */%>
					try{if(!fn_checkFileSelectOption($("#" + $(this).find("[name='${name}").attr("id") + "_total_layer"), "<c:out value="${itemName}"/>", -1<c:if test="${!empty minimum && !empty maximum}">, ${minimum}, ${maximum}</c:if>)) {
						$("#" + $(this).find("[name='${name}").attr("id") + "_total_layer").focus();
						varMultiChkItemName = "<c:out value="${itemName}"/>";
						varMultiChked = true;
						<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
					} else {
						varMultiOInput = true;
						if(varMultiChked) return false;
					}}catch(e){}
					</c:when>
					<c:when test="${objectType == 2}">
					<%/* select */%>
					try{if(!fn_isSelected($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
						$(this).find("[name='${name}']").focus();
						varMultiChkItemName = "<c:out value="${itemName}"/>";
						varMultiChked = true;
						<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
					} else {
						varMultiOInput = true;
						if(varMultiChked) return false;
					}}catch(e){}
					<c:if test="${!empty selectEtcVal}">
					<%/* 기타 입력 */%>
					try{if($(this).find("[name='${name}']").val() == '" + selectEtcVal + "' && !fn_isFill($(this).find("[name='${name}_etc']"), "<c:out value="${itemName}"/>")) {
						$(this).find("[name='${name}_etc']").focus();
						varMultiChkItemName = "<c:out value="${itemName}"/>";
						varMultiChked = true;
						<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
					} else {
						varMultiOInput = true;
						if(varMultiChked) return false;
					}}catch(e){}
					</c:if>
					</c:when>
					<c:when test="${objectType == 22}">
					<%/* select (다차원) */%>
					try{
						var varSel${id}=true;
						$(this).find("select[name='${name}_tmp']").each(function(){
							if(!fn_isSelected($(this), "<c:out value="${itemName}"/>")) {
								$(this).find("[name='${name}']").focus();
								varSel${id}=false; 
								<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
							}
						});
						if(!varSel${id}){
							varMultiChkItemName = "<c:out value="${itemName}"/>";
							varMultiChked = true; 
							<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
						} else {
							varMultiOInput = true;
							if(varMultiChked) return false;
						}
					}catch(e){}
					</c:when>
					<c:when test="${objectType == 3 || objectType == 11}">
					<%/* multi select / multi select + button */%>
					try{if(!fn_isSelectOption($(this).find("[name='${name}_total']"), "<c:out value="${itemName}"/>") {
						$(this).find("[name='${name}_total']").focus();
						varMultiChkItemName = "<c:out value="${itemName}"/>";
						varMultiChked = true;
						<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
					} else {
						varMultiOInput = true;
						if(varMultiChked) return false;
					}}catch(e){}
					</c:when>
					<c:when test="${objectType == 1 && itemObj['editor'] == '1' && (itemObj['editor_utype'] == '1' || isAdmMode && (empty itemObj['editor_utype'] || itemObj['editor_utype'] == '0'))}">
					<%/* 네이버 스마트에디터 사용하는 경우 */%>
					try {
						if(!fn_isFill($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
							$(this).find("[name='${name}']").focus();
							varMultiChkItemName = "<c:out value="${itemName}"/>";
							oEditors.getById[$(this).find("[name='${name}']").attr("id")].exec("FOCUS");
							varMultiChked = true;
							<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
						} else {
							varMultiOInput = true;
							if(varMultiChked) return false;
						}
					}catch(e){}
					</c:when>
					<c:otherwise>
					<%/* 그외 입력  */%>
					try{
						if(!fn_isFill($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
							$(this).find("[name='${name}']").focus();
							varMultiChkItemName = "<c:out value="${itemName}"/>";
							varMultiChked = true;
							<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
						} else {
							varMultiOInput = true;
							if(varMultiChked) return false;
						}
					}catch(e){}
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	<c:if test="${isExcValidOfS}">
	//}
	</c:if>
	</c:if>
	
	<c:set var="itemtype" value="${itemObj['item_type']}"/>
	<c:set var="minimum" value="${itemObj['minimum']}"/>
	<c:set var="maximum" value="${itemObj['maximum']}"/>
	<c:set var="useExt" value="${itemObj['use_file_ext']}"/>
	<c:set var="dsetExt" value="${itemObj['dset_file_ext']}"/>

	<%/* 유효성 체크 */%>
	<c:choose>
		<c:when test="${formatType == 1}">
		<%/* 전화번호 */%>
		try {
			if(!fn_checkHomePhone($(this).find("[name='${name}1']"), $(this).find("[name='${name}2']"), $(this).find("[name='${name}3']"), "<c:out value="${itemName}"/>")) {
				varMultiChked = true;
				return false;
			}
		} catch(e) {}
		</c:when>
		<c:when test="${formatType == 2}">
		<%/* 휴대폰번호 */%>
		try {
			if(!fn_checkMobilePhone($(this).find("[name='${name}1']"), $(this).find("[name='${name}2']"), $(this).find("[name='${name}3']"), "<c:out value="${itemName}"/>")) {
				varMultiChked = true;
				return false;
			}
		} catch(e) {}
		</c:when>
		<c:when test="${formatType == 3}">
		<%/* 이메일 */%>
		try {
			if(!fn_checkEmail($(this).find("[name='${name}']"))) {
				varMultiChked = true;
				return false;
			}
			
			if($('#fn_btn_' + $(this).find("[name='${name}']").attr("id")).length > 0 && fn_isFill($(this).find("[name='${name}']")) && !fn_checkObj.checkeds["${name}"]) {
				alert(fn_Message.duplicateText("${itemName}"));
				$(this).find("[name='${name}']").focus();
				varMultiChked = true;
				return false;
			}
		} catch(e) {}
		</c:when>
		<c:when test="${formatType == 0 && (objectType == 0 || objectType == 101 || objectType == 1 || objectType == 7)}">
			<c:choose>
				<c:when test="${itemtype == '11'}">
					<%/* 통화(원)  */%>
					try{
						if(!fn_checkCurrent($(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
							varMultiChked = true;
							return false;
						}
					}catch(e){}
				</c:when>
				<c:when test="${!empty itemtype && itemtype != '0'}">
				<c:set var="addDsetText" value=""/>
				<c:if test="${useExt == 1 && !empty dsetExt}">
					<c:set var="addDsetText" value=", '${dsetExt}'"/>
				</c:if>
				try{
					if(!fn_checkItemTypeForm("${itemtype}", $(this).find("[name='${name}']"), "<c:out value="${itemName}"/>"${addDsetText})) {
						varMultiChked = true;
						return false;
					}
				}catch(e){}
				</c:when>
				<c:when test="${!empty dsetText}">
				try{
					if(!fn_checkTextForm("${useExt}", "<c:out value="${dsetExt}"/>", $(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
						varMultiChked = true;
						return false;
					}
				}catch(e){}
				</c:when>
			</c:choose>
			<c:set var="dsetPrvt" value="${itemObj['dset_prevent_text']}"/>
			<c:if test="${!empty dsetPrvt}">
			try{
				if(!fn_checkPrvTextForm("${dsetPrvt}", $(this).find("[name='${name}']"), "<c:out value="${itemName}"/>")) {
					varMultiChked = true;
					return false;
				}
			}catch(e){alert(e);}
			</c:if>
		</c:when>
	</c:choose>
	<c:if test="${!empty itemObj['option_etc']}">
		<c:choose>
		<c:when test="${objectType == 2}">
		<%/* select */%>
		var var${name}Obj =  $(this).find("select[name='${name}']");
		$.each(var${name}Obj, function(){
			var varName = $(this).attr("name");
			var var${name}OptObj = $(this).find("option:selected");
			var var${name}EtcObj = $(this).siblings("input[data-id='" + var${name}OptObj.attr("data-etc") + "']") || $(this).siblings("." + varName + "Etc").find("input[data-id='" + var${name}OptObj.attr("data-etc") + "']");
			var var${name}Val = var${name}OptObj.val();
			if(var${name}EtcObj.length > 0) {
				try{
					var var${name}Text = (var${name}OptObj.text()).trim();
					if(!fn_isFill(var${name}EtcObj, "<c:out value="${itemName}"/> " + var${name}Text)){
						var${name}EtcObj.focus();
						varMultiChkItemName = "<c:out value="${itemName}"/>";
						varMultiChked = true;
						<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
					} else {
						varMultiOInput = true;
						if(varMultiChked) return false;
					}
				}catch(e){}
			}
		});
		</c:when>
		<c:when test="${objectType == 5}">
		<%/* radio */%>
		var var${name}Obj =  $(this).find("input[name='${name}']:checked");
		var var${name}Id = var${name}Obj.attr('id');
		var var${name}EtcObj = $(this).find("#" + var${name}Id + "Etc");
		var var${name}Val = var${name}Obj.val();
		if(var${name}EtcObj.size() > 0) {
			try{
				var var${name}TextObj = $("label[for='" + var${name}Id + "']:last").clone();
				var${name}TextObj.find(".nText").remove();
				var var${name}Text = (var${name}TextObj.text()).trim();
				if(!fn_isFill(var${name}EtcObj, "<c:out value="${itemName}"/> " + var${name}Text)) {
					var${name}EtcObj.focus();
					varMultiChked = true;
					<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
				} else {
					varMultiOInput = true;
					if(varMultiChked) return false;
				}
			}catch(e){}
		}
		</c:when>
		<c:when test="${objectType == 4}">
		<%/* checkbox */%>
		var var${name}Chked = false;
		var var${name}Obj =  $(this).find("input[name='${name}']:checked");
		$.each(var${name}Obj, function(){
			var varId = $(this).attr('id');
			var varEtcObj = $("#" + varId + "Etc");
			var varVal = $(this).val();
			if(varEtcObj.size() > 0) {
				var varTextObj = $("label[for='" + varId + "']:last").clone();
				varTextObj.find(".nText").remove();
				var varText = (varTextObj.text()).trim();
				if(!fn_isFill(varEtcObj, "<c:out value="${itemName}"/> " + varText)) {
					var${name}Chked = true;
					return false;
				}
			}
		});
		if(var${name}Chked) {
			varMultiChked = true; 
			<c:if test="${isExcValidOfS}">if(${multiRowObj}.length > 1) </c:if>return false;
		} else {
			varMultiOInput = true;
			if(varMultiChked) return false;
		}
		</c:when>
		</c:choose>
	</c:if>
	
	<%/* 글자수 체크 */%>
	<c:if test="${objectType != 6 && objectType != 9}">
	<c:choose>
		<c:when test="${objectType == 98}">
			<c:set var="chkItemId" value="${id}Name"/>
		</c:when>
		<c:otherwise>
			<c:set var="chkItemId" value="${id}"/>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${minimum > 0 && maximum > 0}">
		try{
			if(!fn_checkStrLenForm($(this).find("[name='${chkItemId}']"), "<c:out value="${itemName}"/>", ${minimum}, ${maximum})) {
				varMultiChked = true;
				return false;
			}
		}catch(e){}
		</c:when>
		<c:when test="${maximum > 0}">
		try{
			if(!fn_checkStrMaxLenForm($(this).find("[name='${chkItemId}']"), "<c:out value="${itemName}"/>", ${maximum})) {
				varMultiChked = true;
				return false;
			}
		}catch(e){}
		</c:when>
	</c:choose>
	</c:if>
	</c:if>
	</c:if>
	</c:if>
</c:forEach>
<%/* submit 전 setting */%>
<c:forEach var="itemId" items="${itemOrder}">
	
	<c:set var="id" value="${itemId}"/>
	<c:set var="name" value="${itemId}"/>
	<c:if test="${!empty preItemId}">
		<c:set var="id" value="${preItemId}${id}"/>
		<c:set var="name" value="${preItemId}${name}"/>
	</c:if>
	<c:if test="${!empty nextItemId}">
		<c:set var="id" value="${id}${nextItemId}"/>
		<c:set var="name" value="${name}${nextItemId}"/>
	</c:if>
	
	<c:set var="itemObj" value="${items[itemId]}"/>
	<c:if test="${!empty itemObj}">
		<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
		<c:if test="${isAdmMode || inputType != 20}">
			<c:set var="formatType" value="${itemObj['format_type']}"/>
			<c:set var="objectType" value="${itemObj['object_type']}"/>
			<c:set var="required" value="${itemObj[requiredName]}"/>
			<c:set var="itemName" value="${itemObj['item_name']}"/>
			<c:set var="itemtype" value="${itemObj['item_type']}"/>
			<c:set var="useLang" value="${itemObj['use_lang']}"/>
	
		<%/* 제외항목이 아닌 경우 */%>
		<c:set var="useSettingId" value="${itemObj['use_setting_id']}"/>
		<%/* 사용여부에 따른 출력 : 다기능게시판 - 공지,비밀글,답변상태,게시기간,파일 */%>
		
		<c:if test="${elfn:arrayIndexOf(exceptIds, id) == -1 && (empty useSettingId || !empty useSettingId && settingInfo[useSettingId] == 1) && inputType != 10}">
			<c:choose>
				<c:when test="${formatType == 0 && objectType == 9}">
				// multi file
				try{
					var varDeleteObj = gVarDeleteFiles.get("${id}");
					
					if(varDeleteObj) {
						var varFileDeletedIdxs = "";
						var varIdx = 0;
						var varDIdx = 0;
						var varDKeys = varDeleteObj.getKeys();
						for (var kk = varDeleteObj.keyIterator(); kk.hasNext();) {
							var key = kk.next();
							var value = varDeleteObj.get(key);
							if(value) {
								if(varIdx > 0) varFileDeletedIdxs += ",";
								varFileDeletedIdxs += key.replace("k", "");
								varIdx ++;
							}
						}
						$("#${id}_deleted_idxs").val(varFileDeletedIdxs);
					}
				}catch(e){}
				try{$(this).find("input[name='${name}_total']").prop("checked", true);}catch(e){}
				</c:when>
				<c:when test="${formatType == 0 && (objectType == 3 || objectType == 11)}">
				// multi select + button
				try{$(this).find("select[name='${name}'] option").prop("selected", true);}catch(e){}
				</c:when>
				<c:otherwise>
					<c:choose>
					<c:when test="${itemtype == '11'}">
						<%/* 통화(원)  */%>
						fn_setNFormatCurrent($(this).find("[name='${name}']"));
					</c:when>
					<c:when test="${useLang == 1 && !empty itemLocaleLang}">
						$(this).find("[name='${name}']").val($("#${id}_<c:out value="${itemLocaleLang}"/>").val());
					</c:when>
					<c:otherwise>
					<%/* 그외 입력  */%>
					</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</c:if>
	</c:if>
	</c:if>
</c:forEach>
	if(varMultiOInput && varMultiChked) return false;
});
<c:choose>
	<c:when test="${isExcValidOfS}">
if(varMultiChked && varMultiOInput || ${multiRowObj}.length > 1 && varMultiChked) {
	alert(fn_Message.fillText(varMultiChkItemName));
	return false;
}
	</c:when>
	<c:otherwise>
if(varMultiChked) {
	alert(fn_Message.fillText(varMultiChkItemName));
	return false;
}
	</c:otherwise>
</c:choose>
}catch(e){console.log(e);}
<c:if test="${isEnd}">
$(".fn_etc:disabled").val('');
$(".fn_etc:disabled").prop("disabled", false);
</c:if>
</c:if>