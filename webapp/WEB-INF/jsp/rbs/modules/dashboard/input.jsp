<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="verItemInfo" value="${moduleItem.version_item_info}"/>
<c:set var="itemViewOrderName" value="${submitType}_order"/>
<c:set var="itemOrderName" value="${submitType}proc_order"/>							<% /* 등록/수정 처리 항목목록 */ %>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript include */ %>
<script type="text/javascript">
$(function(){
	<c:set var="bannerItemInfo" value="${moduleItem.banner_item_info}"/>
	<c:set var="popupItemInfo" value="${moduleItem.popup_item_info}"/>
	<c:set var="boardItemInfo" value="${moduleItem.board_item_info}"/>
	<c:set var="contentsItemInfo" value="${moduleItem.contents_item_info}"/>

	$(".tbWriteA .fn_boardmenuIdxOName").focus(function(){
		var varId = $(this).attr("id");
		var varLabelObj = $("label[for='" + varId + "']");
		varLabelObj.hide();
	});
	$(".tbWriteA .fn_boardmenuIdxOName").blur(function(){
		fn_setMenuNameLabel(this);
	});
	$(".tbWriteA .fn_boardmenuIdxOName").blur();
	
	// 숫자입력
	$(".tbWriteA .fn_limitNumber").keydown(function(event){
		return fn_onlyNumKD(event);
	});
	
	// 게시판 메뉴 검색
	$(".fn_btn_boardmenuIdx").click(function(event){
		fn_boardMenuSearchForm(this);
			return false;
	});

	// 메뉴 삭제
	$(".fn_btn_del_boardmenuIdx, .fn_btn_del_contentsmenuIdx").click(function(){
		try{
			var varPObj = $(this).parent("dd").parent("dl");
			varPObj.find("input[type='text']").val("");
			varPObj.find("input[type='hidden']").val("");
			varPObj.find("input[type='checkbox']").prop("checked", false);
		}catch(e){}
	});
	
	// 콘텐츠 메뉴 검색
	$(".fn_btn_contentsmenuIdx").click(function(event){
		fn_contentsMenuSearchForm(this);
			return false;
	});
	
	fn_boardInputReset();
	
	// reset
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${param.inputFormId}"/>").reset();
			fn_boardInputReset();
		}catch(e){alert(e); return false;}
	});
	
	// cancel
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_cancel").click(function(){
		try {
			<c:choose>
				<c:when test="${param.dl == '1'}">
				self.close();
				</c:when>
				<c:otherwise>
				location.href="${elfn:replaceScriptLink(URL_LIST)}";
				</c:otherwise>
			</c:choose>
		}catch(e){return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
});
// 게시판 메뉴 검색
function fn_boardMenuSearchForm(theObj){
	var varItemId = $(theObj).attr("data-id");
	try {
		var varTitle = "메뉴 검색";
		fn_dialog.open(varTitle, "<c:out value="${URL_MENUSEARCHLIST}"/>&itemId=" + varItemId +"&type=1&dl=1", 650, 500);
	}catch(e){}
}
// 게시판 메뉴 setting
function fn_setBoardMenuInfo(theItemId, theChkedObjs){
	fn_setMenuInfo(theItemId, theChkedObjs);
}

// 콘텐츠 메뉴 검색
function fn_contentsMenuSearchForm(theObj){
	var varItemId = $(theObj).attr("data-id");
	try {
		var varTitle = "메뉴 검색";
		fn_dialog.open(varTitle, "<c:out value="${URL_MENUSEARCHLIST}"/>&itemId=" + varItemId +"&type=2&dl=1", 650, 500);
	}catch(e){}
}
// 콘텐츠 메뉴 setting
function fn_setContentsMenuInfo(theItemId, theChkedObjs){
	fn_setMenuInfo(theItemId, theChkedObjs);
}

// 메뉴 공통 setting
function fn_setMenuInfo(theItemId, theValue, theText, thePathText, theMdId, theFnIdx){
	var varObj =  $("#" + theItemId);
	var varPathNameObj =  $("#" + theItemId + "Name");
	var varNameObj =  $("#" + theItemId + "OName");
	varObj.val(theValue);
	varPathNameObj.val(thePathText);
	varNameObj.val(theText);
	fn_setMenuNameLabel(varNameObj);
	
	var varCateObj = varObj.parent("dd").parent("dl").find("select");
	varCateObj.find("option:gt(0)").remove();
	varCateObj.prop("disabled", true);
	var varAction = "${URL_OPTNSEARCHJSON}&mdId=" + theMdId + "&sfIdx=" + theFnIdx;
	$.ajax({
  		type:'POST', 
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			if(result.list) {
  				varCateObj.prop("disabled", false);
  				$.each(result.list, function(i, item){
	  				varCateObj.append("<option value='" + item.optionCode + "'>" + item.optionName + "</option>");
  				});
  			}
  		}, 
  		error:function(request,error){
  			fn_ajax.checkError(request);
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	});
}

/**
 * 메뉴 label setting
 */
function fn_setMenuNameLabel(theObj) {
	var varId = $(theObj).attr("id");
	var varLabelObj = $("label[for='" + varId + "']");
	if(fn_isFill($(theObj))) {
		varLabelObj.hide();
		return;
	}
	varLabelObj.show();
}

function fn_boardInputReset(){
}
function fn_boardInputFormSubmit(){
	return true;
}
</script>