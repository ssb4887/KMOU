<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<spring:message var="msgNoAuth" code="message.no.auth"/>
<c:set var="itemOrderName" value="${submitType}proc_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tree.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/settingForm.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/item.js"/>"></script>
<script type="text/javascript">
$(function(){
	// 선택메뉴정보 얻기위한 기본설정
	fn_MenuTree.rtObj = $("#fn_iaMenuUL");
	fn_MenuTree.addForm = $("#<c:out value="${param.inputFormId}"/>");
	fn_MenuTree.setForm = $("#<c:out value="${param.settingFormId}"/>");
	fn_itemMenuLink.rUrl = "<c:out value="/${usrSiteVO.siteId}"/>";
	fn_setMenuForm.formId = "<c:out value="${param.settingFormId}"/>";				// 입력폼
	fn_setMenuForm.infoUrl = "${elfn:replaceScriptLink(URL_MENUINFO)}";				// 메뉴정보경로
	
	fn_itemModule.infoUrl = "${elfn:replaceScriptLink(URL_MODULEINFO)}";				// 사용기능정보경로
	fn_itemModuleFn.infoUrl = "${elfn:replaceScriptLink(URL_MODULEFNINFO)}";		// 기능정보경로
	
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>

	<c:if test="${!mngAuth}">
	fn_itemFormSubmit.disabled();
	</c:if>
	
	// 적용메뉴보기 새창
	$(".fn_btn_view").click(function(){
		try {
			var varTitle = "적용메뉴";
			fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1", 1100, 0);
		}catch(e){}
		return false;
	});
	<c:if test="${mngAuth}">
	// 적용
	$("#fn_btn_apply_sversion").click(function(){
		var varVerIdx = "<c:out value="${queryString.verIdx}"/>";
		var varConfirm = confirm(fn_Message.versionApplyConfirm(varVerIdx));
		if(!varConfirm) return false;
	});
	// 휴지통
	$(".fn_btn_deleteList").click(function(){
		try {
			<c:choose>
				<c:when test="${!empty deleteList_dialog_title}">
				var varTitle = "<c:out value="${deleteList_dialog_title}"/>";
				</c:when>
				<c:otherwise>
				var varTitle = "<c:out value="${settingInfo.deleteList_title}"/>";
				</c:otherwise>
			</c:choose>
			<c:if test="${!empty deleteList_dialog_addTitle}">
			varTitle += " (<c:out value="${deleteList_dialog_addTitle}"/>)";
			</c:if>
			fn_dialog.open(varTitle, $(this).attr("href"), 0, 0, "fn_deleteList");
		}catch(e){/*alert(e);*/}
		return false;
	});
	
	// 삭제
	$(".fn_btn_delete").click(function(){
		try {
			var varResult = fn_deleteFormSubmit();
			if(!varResult) return false;
			/*var varConfirm = confirm("<spring:message code="message.select.delete.confirm"/>");
			if(!varConfirm) return false;*/
			fn_createHiddenIframe($(this).attr('target'));
			$(this).attr("href", $(this).attr("href") + "&menuIdx=" + $("#menuIdx").val());
		}catch(e){return false;}
		return true;
	});
	</c:if>
	
	// 미리보기 버튼 클릭
	$("#fn_btn_preview").click(function(){
		var varAction = <c:if test="${!empty pageContext.request.contextPath}">"<c:out value="${pageContext.request.contextPath}"/>" + </c:if>fn_setMenuForm.menuInfo["menuLink"] + "&preVerIdx=<c:out value="${queryString.verIdx}"/>";
		fn_dialog.openF(varAction, "_blank");
	});
	
	// 등록
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			<c:choose>
			<c:when test="${mngAuth}">
			return fn_menuInputFormSubmit();
			</c:when>
			<c:otherwise>
			alert("<c:out value="${msgNoAuth}"/>");
			return false;
			</c:otherwise>
			</c:choose>
		}catch(e){alert(e); return false;}
	});
	
	// reset
	$("#<c:out value="${param.settingFormId}"/> .fn_btn_reset").click(function(){
		try {
			fn_menuSettingReset();
		}catch(e){alert(e); return false;}
	});
	
	// 수정
	$("#<c:out value="${param.settingFormId}"/>").submit(function(){
		try {
			<c:choose>
			<c:when test="${mngAuth}">
			return fn_menuSettingFormSubmit();
			</c:when>
			<c:otherwise>
			alert("<c:out value="${msgNoAuth}"/>");
			return false;
			</c:otherwise>
			</c:choose>
		}catch(e){alert(e); return false;}
	});
});
</script>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/mng.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/common.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/version.js"/>"></script>

<script type="text/javascript">
<c:set var="itemId" value="urDesign"/>
//기본 템플릿 검색
function fn_designListSearchForm() {
	fn_dialog.init("fn_${itemId}_search");
	var varItemId = "${itemId}";
	try {
		var varTitle = "";
		var varModuleId = $("#moduleId option:selected").val();
		var varDesignType = $("#designType").val();
		if(varModuleId == '') {
			alert("<spring:message code="message.module.no.use"/>");
			return false;
		} else if(varModuleId == "board") {
			if(!fn_checkFill($("#fnIdx"), "기능")) return false;
		}
		fn_dialog.open(varTitle, "${URL_DESIGNLIST}&mdl=1&moduleId=" + varModuleId + "&designType=" + varDesignType + "&itemId=" + varItemId +"&dl=1");
	}catch(e){}
	return false;
}

function fn_setDesignForm(theVal){
	fn_itemDesign.set(theVal);
}

//사용자그룹 검색
function fn_mbrGrpListSearchForm(theObj){
	var varItemId = $(theObj).attr("data-id");
	try {
		var varTitle = "회원그룹 검색";
		fn_dialog.open(varTitle, "<c:out value="${URL_GRUPSEARCHLIST}"/>&itemId=" + varItemId +"&dl=1", 650, 500);
	}catch(e){}
}

//사용자그룹 setting
function fn_setMemberGrupInfo(theItemId, theChkedObjs){
	fn_setMemberComInfo(theItemId, theChkedObjs);
}

//사용자부서 검색
function fn_mbrDptListSearchForm(theObj){
	var varItemId = $(theObj).attr("data-id");
	try {
		var varTitle = "회원부서 검색";
		fn_dialog.open(varTitle, "<c:out value="${URL_DPRTSEARCHLIST}"/>&itemId=" + varItemId +"&dl=1", 650, 500);
	}catch(e){}
}
//사용자부서 setting
function fn_setMemberDprtInfo(theItemId, theChkedObjs){
	fn_setMemberComInfo(theItemId, theChkedObjs);
}

//사용자회원 검색
function fn_mbrMbrListSearchForm(theObj){
	var varItemId = $(theObj).attr("data-id");
	try {
		var varTitle = "회원 검색";
		fn_dialog.open(varTitle, "<c:out value="${URL_MMBRSEARCHLIST}"/>&itemId=" + varItemId +"&dl=1", 650, 500);
	}catch(e){}
}

//관지자회원 검색
function fn_mbrMbrAnListSearchForm(theObj){
	var varItemId = $(theObj).attr("data-id");
	try {
		var varTitle = "회원 검색";
		fn_dialog.open(varTitle, "<c:out value="${URL_MMBRANSEARCHLIST}"/>&itemId=" + varItemId +"&dl=1", 650, 500);
	}catch(e){}
}

//사용자회원 setting
function fn_setMemberMmbrInfo(theItemId, theChkedObjs){
	fn_setMemberComInfo(theItemId, theChkedObjs);
}

//사용자정보 공통 setting
function fn_setMemberComInfo(theItemId, theChkedObjs){
	$.each(theChkedObjs, function(){
		var varCd = $(this).val();
		var varObj =  $("select[name='" + theItemId + "']");
		var varPreChkedObj = varObj.find("option[value='" + varCd + "']");
		if(!varPreChkedObj.is("option")){
			var varBName = $(this).attr("data-name");
			varObj.append("<option value=\"" + varCd + "\">" + varBName + "</option>");
		}
	});
}

// 등록 처리
function fn_menuInputFormSubmit(){
	
	var varOrdIdx = $("#top_ordIdx").val();
	var varOrdType = $("#<c:out value="${param.inputFormId}"/>").find("input[name='ordType']:checked").val();
	if(varOrdIdx == "1") {
		// 메뉴 : 최상위
		if(varOrdType != "3") {
			// 위치구분 : '내부'가 아닌 경우
			alert(fn_Message.menuMoveInMain);
			return false;
		}
	}
	
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo['writeproc_order']}" preItemId="top_"/>

	fn_createMaskLayer();
	return true;
}

// 수정 reset
function fn_menuSettingReset(){
	$("#infoMng .btn_close").click();
	fn_MenuTree.select($("#menuIdx").val(), true);
}

// 수정 처리
function fn_menuSettingFormSubmit(){
	var varMenuIdx = $('#menuIdx').val();
	if(!varMenuIdx)	{
		alert("<spring:message code="message.menu.no.select.info"/>");
		return false;
	}
	// 위치 유효성검사	
	var varCheck = fn_itemOrdType.check($("#<c:out value="${param.settingFormId}"/>"));
	if(!varCheck) return false;
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_createMaskLayer();
	return true;
}


<c:if test="${mngAuth}">
//삭제
function fn_deleteFormSubmit(){
	try {
		var varMenuIdx = $("#menuIdx").val();
		if(varMenuIdx == "1") {
			alert("<spring:message code="message.menu.main.delete"/>");
			return false;
		}
		// 삭제여부
		var varConfirm = confirm("<spring:message code="message.select.menu.delete.confirm"/>");
		if(!varConfirm) return false;
		return true;
	}catch(e){}
	
	return true;
}
</c:if>
</script>