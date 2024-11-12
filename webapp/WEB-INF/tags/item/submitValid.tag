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
<c:set var="inputFlag" value="${inputTypeName}"/>
<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:if test="${!empty items && !empty itemOrder}">
<c:if test="${empty settingInfo}"><c:set var="settingInfo" value="${settingInfo}"/></c:if>
<c:set var="requiredName" value="required_${inputFlag}"/>
<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
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
			if(fn_isFill($("#${id}1")) || fn_isFill($("#${id}2"))) {
				$("#${id}").val($("#${id}1").val() + '@' + $("#${id}2").val());
			}
		}catch(e){}
		</c:when>
		<c:when test="${formatType == 9}">
		try{if($("#pre_${id}").size() > 0 && !fn_checkFill($("#pre_${id}"), "<spring:message code="item.pre_password.name" arguments="${itemName}"/>")) return false;}catch(e){}
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${objectType == 1 && itemObj['editor'] == '1' && (itemObj['editor_utype'] == '1' || isAdmMode && (empty itemObj['editor_utype'] || itemObj['editor_utype'] == '0'))}">
				<%/* 네이버 스마트에디터 사용하는 경우 */%>
				try {oEditors.getById["${id}"].exec("UPDATE_CONTENTS_FIELD", []);}catch(e){}
				</c:when>
				<c:when test="${objectType == 22}">
				try {
					$("select[name='${name}_tmp']").each(function(index, item){
						var varVal = $(item).val();
						if(varVal) $("#${id}").val(varVal);
					});
				}catch(e){}
				</c:when>
			</c:choose>
		</c:otherwise>
	</c:choose>
	<c:if test="${required == '1'}">
	<%/* 필수입력 */%>
		<c:choose>
			<c:when test="${formatType == 1 || formatType == 2}">
			<%/* 전화번호 */%>
			try{if(!fn_checkSelected($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}3'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 3}">
			<%/* 이메일 */%>
			try{if(!fn_checkFill($("#${id}1"), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}3'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($("#${id}2"), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 4}">
			<%/* 주소 */%>
			try{
				if(!$('#${id}1').prop("disabled") && !fn_isFill($('#${id}1'))) {
					alert(fn_Message.checkText('<c:out value="${itemName}"/>'));
					$("#fn_btn_${id}").focus();
					return false;
				}
			}catch(e){}
			try{if(!fn_checkFill($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}3'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 12}">
			<%/* 사업자번호 */%>
			try{if(!fn_checkFill($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}3'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 13}">
			<%/* 법인번호 */%>
			try{if(!fn_checkFill($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 19}">
			<%/* 년도 */%>
			try{if(!fn_checkSelected($("#${id}"), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 20 || formatType == 21}">
			<%/* 년도-월 / 년도-분기 */%>
			try{if(!fn_checkSelected($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 22}">
			<%/* 년도-월-일 */%>
			try{if(!fn_checkSelected($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}3'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 6}">
			<%/* 날짜 시:분:초 */%>
			try{if(!fn_checkFill($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}3'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}4'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 7}">
			<%/* 날짜 시:분 */%>
			try{if(!fn_checkFill($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}3'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 8}">
			<%/* 날짜 시 */%>
			try{if(!fn_checkFill($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 15}">
			<%/* 날짜(기간) */%>
			try{if(!fn_checkFill($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 16}">
			<%/* 날짜 시:분:초(기간) */%>
			try{if(!fn_checkFill($('#${id}11'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}12'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}13'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}14'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}21'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}22'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}23'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}24'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 17}">
			<%/* 날짜 시:분(기간) */%>
			try{if(!fn_checkFill($('#${id}11'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}12'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}13'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}21'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}22'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}23'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 18}">
			<%/* 날짜 시(기간) */%>
			try{if(!fn_checkFill($('#${id}11'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}12'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkFill($('#${id}21'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}22'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 29}">
			<%/* 년도(기간) */%>
			try{if(!fn_checkSelected($('#${id}1'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}2'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 30}">
			<%/* 년도-월(기간) */%>
			try{if(!fn_checkSelected($('#${id}11'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}12'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}21'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}22'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 61}">
			<%/* 시:분(기간) */%>
			try{if(!fn_checkSelected($('#${id}11'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}12'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}21'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if(!fn_checkSelected($('#${id}22'), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 9}">
			<%/* 비밀번호 */%>
			try{if(!fn_checkFill($("#${id}"), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			try{if($("#${id}_reconfirm").size() > 0 && !fn_checkFill($('#${id}_reconfirm'), "<spring:message code="item.password_reconfirm.name" arguments="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:when test="${formatType == 10}">
			<%/* 아이디 */%>
			try{if(!fn_checkFill($("#${id}"), "<c:out value="${itemName}"/>")) return false;}catch(e){}
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${objectType == 4 || objectType == 5}">
					<%/* checkbox / radio */%>
					try{if(!fn_checkElementChecked("${name}", "<c:out value="${itemName}"/>")) return false;}catch(e){}
					</c:when>
					<c:when test="${objectType == 6}">
					<%/* file */%>
					try{if($("#${id}_saved").is("input") && (!fn_isFill($("#${id}_saved")) || fn_isFill($("#${id}_deleted_idxs"))) && !fn_checkFill($("#${id}"), "<c:out value="${itemName}"/>") || !$("#${id}_saved").is("input") && !fn_checkFill($("#${id}"), "<c:out value="${itemName}"/>")) return false;}catch(e){}
					</c:when>
					<c:when test="${objectType == 9}">
					<%/* multi file */%>
					try{if(!fn_checkFileSelectOption($("#${id}_total_layer"), "<c:out value="${itemName}"/>", -1<c:if test="${!empty minimum && !empty maximum}">, ${minimum}, ${maximum}</c:if>)) return false;}catch(e){}
					</c:when>
					<c:when test="${objectType == 2}">
					<%/* select */%>
					try{if(!fn_checkSelected($("#${id}"), "<c:out value="${itemName}"/>")) return false;}catch(e){}
					<c:if test="${!empty selectEtcVal}">
					<%/* 기타 입력 */%>
					try{if($("#${id}").val() == '" + selectEtcVal + "' && !fn_checkFill($("#${id}_etc"), "<c:out value="${itemName}"/>")) return false;}catch(e){}
					</c:if>
					</c:when>
					<c:when test="${objectType == 22}">
					<%/* select (다차원) */%>
					try{
						var varSel${id}=true;
						$("select[name='${name}_tmp']:not(:disabled)").each(function(){
							if(!fn_checkSelected($(this), "<c:out value="${itemName}"/>")) {varSel${id}=false; return false;}
						});
						if(!varSel${id}){return false;};
					}catch(e){}
					</c:when>
					<c:when test="${objectType == 3 || objectType == 11}">
					<%/* multi select / multi select + button */%>
					try{if(!fn_checkSelectOption("${name}_total", "<c:out value="${itemName}"/>")) return false;}catch(e){}
					</c:when>
					<c:when test="${objectType == 1 && itemObj['editor'] == '1' && (itemObj['editor_utype'] == '1' || isAdmMode && (empty itemObj['editor_utype'] || itemObj['editor_utype'] == '0'))}">
					<%/* 네이버 스마트에디터 사용하는 경우 */%>
					try {
						if(!fn_checkFill($("#${id}"), "<c:out value="${itemName}"/>")) {
							oEditors.getById["${id}"].exec("FOCUS");
							return false;
						}
					}catch(e){}
					</c:when>
					<c:otherwise>
					<%/* 그외 입력  */%>
					<c:choose>
						<c:when test="${useLang == 1}">
							<c:forEach var="langDt" items="${itemLangList}" varStatus="i">
								<spring:message var="langItemName" code="item.space2.name" arguments="${itemName},${langDt.OPTION_NAME}" argumentSeparator=","/>
								try{
									if(!fn_checkFill($("#${id}_<c:out value="${langDt.OPTION_CODE}"/>"), "${langItemName}")) return false;
								}catch(e){}
							</c:forEach>
						</c:when>
						<c:otherwise>
					try{
						if(!fn_checkFill($("#${id}"), "<c:out value="${itemName}"/>")) return false;
					}catch(e){}
						</c:otherwise>
					</c:choose>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
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
			if(!fn_checkHomePhone($("#${id}1"), $("#${id}2"), $("#${id}3"), "<c:out value="${itemName}"/>")) return false;
		} catch(e) {}
		</c:when>
		<c:when test="${formatType == 2}">
		<%/* 휴대폰번호 */%>
		try {
			if(!fn_checkMobilePhone($("#${id}1"), $("#${id}2"), $("#${id}3"), "<c:out value="${itemName}"/>")) return false;
		} catch(e) {}
		</c:when>
		<c:when test="${formatType == 3}">
		<%/* 이메일 */%>
		try {
			if(!fn_checkEmail($("#${id}"))) return false;
			
			if($('#fn_btn_${id}').length > 0 && fn_isFill($("#${id}")) && !fn_checkObj.checkeds["${name}"]) {
				alert(fn_Message.duplicateText("${itemName}"));
				$("#${id}").focus();
				return false;
			}
		} catch(e) {}
		</c:when>
		<c:when test="${formatType == 9}">
		<%/* 비밀번호 */%>
		try {
			if(!fn_validPW($("#${id}"), "<c:out value="${itemName}"/>")) {
				return false;
			}
		}catch(e){}
			
		<%/* 비밀번호 아이디포함 유효성체크 */%>
		<c:set var="relativeId" value="${itemObj['relative_id']}"/>
		<c:if test="${!empty relativeId}">
		try{
			var varIdTD = <c:choose><c:when test="${param.mode=='m'}">$('#fn_item_${relativeId}')</c:when><c:otherwise>$('#${relativeId}')</c:otherwise></c:choose>;
			if(varIdTD.size() > 0) {
				var varValidPWID = fn_validPWIDVal($("#${id}").val(), "<c:out value="${itemName}"/>", <c:choose><c:when test="${param.mode=='m'}">$('#fn_item_${relativeId}').text()</c:when><c:otherwise>$('#${relativeId}').val()</c:otherwise></c:choose>, $('#fn_name_${relativeId}').text());
				if(!varValidPWID) return false;
			}
		}catch(e){}
		</c:if>
		
		<%/* 비밀번호 확인 값 체크 */%>
		try{if(fn_isFill($("#${id}")) && $("#${id}_reconfirm").size() > 0 && !fn_checkFill($('#${id}_reconfirm'), "<spring:message code="item.password_reconfirm.name" arguments="${itemName}"/>")) return false;}catch(e){}
			
		try {
			if($("#${id}_reconfirm").size() > 0 && $("#${id}").val() != $("#${id}_reconfirm").val()) {
				alert(fn_Message.passwordText("<c:out value="${itemName}"/>"));
				$("#${id}_reconfirm").val("");
				$("#${id}_reconfirm").focus();
				return false;
			}
		} catch(e) {}
		</c:when>
		<c:when test="${formatType == 10}">
		<%/* 아이디 */%>
		try {
			<c:set var="formatStyle" value="${itemObj['format_style']}"/>
			<c:choose>
				<c:when test="${formatStyle == 1}">
				if(!fn_validEmailID($("#${id}"), "<c:out value="${itemName}"/>")) {
					return false;
				}
				</c:when>
				<c:otherwise>
				if(!fn_validID($("#${id}"), "<c:out value="${itemName}"/>"<c:if test="${!empty minimum && !empty maximum}">, ${minimum}, ${maximum}</c:if>)) {
					return false;
				}
				</c:otherwise>
			</c:choose>
			if($("#fn_btn_${id}").length > 0)
			{
				if(!fn_checkObj.checkeds["${name}"]) {
					alert(fn_Message.duplicateText("<c:out value="${itemName}"/>"));
					$("#${id}").focus();
					return false;
				}
			}
		} catch(e) {}
		</c:when>
		<c:when test="${formatType == 0 && (objectType == 0 || objectType == 101 || objectType == 1 || objectType == 7)}">
			<c:choose>
				<c:when test="${itemtype == '11'}">
					<%/* 통화(원)  */%>
					try{
						if(!fn_checkCurrent($("#${id}"), "<c:out value="${itemName}"/>")) return false;
					}catch(e){}
				</c:when>
				<c:when test="${!empty itemtype && itemtype != '0'}">
				<c:set var="addDsetText" value=""/>
				<c:if test="${useExt == 1 && !empty dsetExt}">
					<c:set var="addDsetText" value=", '${dsetExt}'"/>
				</c:if>
				try{
					if(!fn_checkItemTypeForm("${itemtype}", $("#${id}"), "<c:out value="${itemName}"/>"${addDsetText})) return false;
				}catch(e){}
				</c:when>
				<c:when test="${!empty dsetText}">
				try{
					if(!fn_checkTextForm("${useExt}", "<c:out value="${dsetExt}"/>", $("#${id}"), "<c:out value="${itemName}"/>")) return false;
				}catch(e){}
				</c:when>
			</c:choose>
			<c:set var="dsetPrvt" value="${itemObj['dset_prevent_text']}"/>
			<c:if test="${!empty dsetPrvt}">
			try{
				if(!fn_checkPrvTextForm("${dsetPrvt}", $("#${id}"), "<c:out value="${itemName}"/>")) return false;
			}catch(e){alert(e);}
			</c:if>
		</c:when>
	</c:choose>
	
	<c:if test="${!empty itemObj['option_etc']}">
		<c:choose>
		<c:when test="${objectType == 2}">
		<%/* select */%>
		var var${name}Obj =  $("select[name='${name}']");
		var var${name}OptObj =  $("select[name='${name}'] option:selected");
		var var${name}Id = var${name}Obj.attr('id');
		var var${name}EtcObj = $("#" + var${name}OptObj.attr("data-etc"));
		var var${name}Val = var${name}OptObj.val();
		if(var${name}EtcObj.length > 0) {
			try{
				var var${name}Text = (var${name}OptObj.text()).trim();
				if(!fn_checkFill(var${name}EtcObj, "<c:out value="${itemName}"/> " + var${name}Text)) return false;
			}catch(e){}
		}
		</c:when>
		<c:when test="${objectType == 5}">
		<%/* radio */%>
		var var${name}Obj =  $("input[name='${name}']:checked");
		var var${name}Id = var${name}Obj.attr('id');
		var var${name}EtcObj = $("#" + var${name}Id + "Etc");
		var var${name}Val = var${name}Obj.val();
		if(var${name}EtcObj.length > 0) {
			try{
				var var${name}TextObj = $("label[for='" + var${name}Id + "']:last").clone();
				var${name}TextObj.find(".nText").remove();
				var var${name}Text = (var${name}TextObj.text()).trim();
				if(!fn_checkFill(var${name}EtcObj, "<c:out value="${itemName}"/> " + var${name}Text)) return false;
			}catch(e){}
		}
		</c:when>
		<c:when test="${objectType == 4}">
		<%/* checkbox */%>
		var var${name}Chked = false;
		var var${name}Obj =  $("input[name='${name}']:checked");
		$.each(var${name}Obj, function(){
			var varId = $(this).attr('id');
			var varEtcObj = $("#" + varId + "Etc");
			var varVal = $(this).val();
			if(varEtcObj.length > 0) {
				var varTextObj = $("label[for='" + varId + "']:last").clone();
				varTextObj.find(".nText").remove();
				var varText = (varTextObj.text()).trim();
				if(!fn_checkFill(varEtcObj, "<c:out value="${itemName}"/> " + varText)) {
					var${name}Chked = true;
					return false;
				}
			}
		});
		if(var${name}Chked) return false;
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
			<c:choose>
				<c:when test="${useLang == 1}">
					<c:forEach var="langDt" items="${itemLangList}" varStatus="i">
						<spring:message var="langItemName" code="item.space2.name" arguments="${itemName},${langDt.OPTION_NAME}" argumentSeparator=","/>
		try{
			if(!fn_checkStrLenForm($("#${chkItemId}_<c:out value="${langDt.OPTION_CODE}"/>"), "${langItemName}", ${minimum}, ${maximum})) return false;
		}catch(e){}
					</c:forEach>
				</c:when>
				<c:otherwise>
		try{
			if(!fn_checkStrLenForm($("#${chkItemId}"), "<c:out value="${itemName}"/>", ${minimum}, ${maximum})) return false;
		}catch(e){}
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${maximum > 0}">
			<c:choose>
				<c:when test="${useLang == 1}">
					<c:forEach var="langDt" items="${itemLangList}" varStatus="i">
						<spring:message var="langItemName" code="item.space2.name" arguments="${itemName},${langDt.OPTION_NAME}" argumentSeparator=","/>
		try{
			if(!fn_checkStrMaxLenForm($("#${chkItemId}_<c:out value="${langDt.OPTION_CODE}"/>"), "${langItemName}", ${maximum})) return false;
		}catch(e){}
					</c:forEach>
				</c:when>
				<c:otherwise>
		try{
			if(!fn_checkStrMaxLenForm($("#${chkItemId}"), "<c:out value="${itemName}"/>", ${maximum})) return false;
		}catch(e){}
				</c:otherwise>
			</c:choose>
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
				try{$("input[name='${name}_total']").prop("checked", true);}catch(e){}
				</c:when>
				<c:when test="${formatType == 0 && (objectType == 3 || objectType == 11)}">
				// multi select + button
				try{$("select[name='${name}'] option").prop("selected", true);}catch(e){}
				</c:when>		
				<c:otherwise>
					<c:choose>
					<c:when test="${itemtype == '11'}">
						<%/* 통화(원)  */%>
						fn_setNFormatCurrent($("#${id}"));
					</c:when>
					<c:when test="${useLang == 1 && !empty itemLocaleLang}">
						$("#${id}").val($("#${id}_<c:out value="${itemLocaleLang}"/>").val());
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
</c:if>