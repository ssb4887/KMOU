<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>							<% /* 등록/수정 처리 항목목록 */ %>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript include */ %>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript 초기 setting */ %>
	
	
	// 사용 콘텐츠 개수 선택
	$('#tabMaxCnt').change(function(){
		fn_setTabDisplay(this);
	});

	// 참고문헌 사용
	$('#reference').click(function(){
		fn_setRefDisplay(this);
	});
	
	// 참고사이트 추가
	$(document).on('click', '.tbWriteA .fn_add_down', function(){
		fn_addTextArea($(this), 2);
	});
	
	// 삭제
	$(document).on('click', '.tbWriteA .fn_add_delete', function(){
		fn_deleteTextArea($(this));
	});

	// 탭 클릭
	$('#fn_formTab>li a').click(function(){
		var varTabIdx = $(this).attr('data-idx');
		//fn_tab.setTabCont(varTabIdx);
		$('#fn_formTab li a').removeClass('on');
		$(this).addClass('on');
		var varObjId = $(this).attr('href');
		$(varObjId).siblings('div').addClass('hidden');
		$(varObjId).removeClass('hidden');
		
		return false;
	});
	fn_init();
	for(var i = 1; i <= ${tabCnt} ; i ++) {
		try {
			(function(i){
			$('#tabName' + i).keyup(function(){
				var varChkStrLen = fn_checkStrMaxLenForm($(this), ${itemInfo.items['tabName1']['maximum']}, $(this).attr('title'));
				if(!varChkStrLen) return false;

				fn_setTabName(i);
			});
			})(i);
		} catch(e) {}
		try {$('#tabCont' + i).css('display', 'none');} catch(e) {}

		fn_setTabName(i);
	}
	
	fn_bannerInputReset();
	
	// reset
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${param.inputFormId}"/>").reset();
			fn_bannerInputReset();
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
			return fn_bannerInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
});


//콘텐츠 tab setting
function fn_setTabDisplay(thObj) {
	var varCnt = $(thObj).find('option:selected').val();
	var varRefUse = $('#reference1').prop('checked');
	if(varCnt == '' && !varRefUse) {
		$('#c_w_h_tab').hide();
		$('#c_w_h_tab_con').hide();
	} 
	if(varCnt == '') {
		$('.fn_dmng').hide();
		$('#fn_formTab li').hide();
	} else {
		$('.fn_dmng').show();
		$('#c_w_h_tab').show();
		$('#c_w_h_tab_con').show();
		var varNum = new Number(varCnt) - 1;
		$('#fn_formTab>li:gt(' + varNum + ')').hide();
		$('#fn_formTab>li:lt(' + varCnt + ')').show();
		//$('#fn_formTab>li:eq(0) a').click();
		var varCnt = $('#tabMaxCnt').find('option:selected').val();
		var varSelIdx = $('#fn_formTab li').index($('#fn_formTab li a.on').parent('li'));
		if(varSelIdx == -1 || varSelIdx != ${tabCnt} && varCnt <= varSelIdx) {
			$('#fn_formTab>li:eq(0) a').click();
		}
		
	}
	if(varRefUse) {
		$('#fn_formTab>li:last').show();
		if(varCnt == '') $('#fn_formTab>li:last a').click();
	}
}

//참고문헌 tab setting
function fn_setRefDisplay(theObj) {
	var varUse = $(theObj).prop('checked');
	var varTab = $('#fn_formTab>li:last');
	var varCnt = $('#tabMaxCnt option:selected').val();
	if(varUse) {
		if(varCnt == '') {
			$('#c_w_h_tab').show();
			$('#c_w_h_tab_con').show();
			$('#fn_formTab>li:last a').click();
		}
		varTab.show();
	}
	
	else {
		if(varCnt != '') {
			var varSelIdx = $('#fn_formTab li').index($('#fn_formTab li a.on').parent('li'));
			if(varCnt <= varSelIdx) $('#fn_formTab>li:eq(0) a').click();
		} else {
			$('#c_w_h_tab').hide();
			$('#c_w_h_tab_con').hide();
		}
		varTab.hide();
	}
}

//탭명 setting
function fn_setTabName(theTabIdx) {
	try {
		if(!theTabIdx) return;
		var varCont;
		var varIsFill = fn_isFill($('#tabName' + theTabIdx));
		if(varIsFill) varCont = $('#tabName' + theTabIdx).val();
		else varCont = '콘텐츠' + theTabIdx;
		$('#fn_formTab li:eq(' + (theTabIdx - 1) + ')>a').html(varCont);
	} catch(e) {}
}
// 참고문헌추가
var gVarConMaxIdx = <c:choose><c:when test="${!empty conList}">${fn:length(conList)}</c:when><c:otherwise>1</c:otherwise></c:choose>;
function fn_addTextArea(theObj, theFlag)
{
	var varUlObj = $('.tbWriteA');
	var varContentsObjs = $('.tbWriteA input.fn_url');
	var varContentsLen = $('.tbWriteA input.fn_url').size();
	if(varContentsLen > 20) {
		return;
	}

	var varThisLi = varUlObj.find('dl:last');//theObj.parents('dl');
	var varNewLi = varThisLi.clone();
	var varThisLiIdx = varThisLi.parent().find('dl').index(varThisLi);
	if(theFlag == 1)
	{
		// 위 
		varThisLi.before(varNewLi);
		if(varThisLiIdx == 0)
		{ 
			varNewLi.addClass('first');
			varThisLi.removeClass('first');
		}
	}
	else
	{
		// 아래 
		varThisLi.after(varNewLi);
		if(varThisLiIdx == 0)
		{ 
			varNewLi.removeClass('first');
		}
	}

	varNewLi.find('input').val('');
	varNewLi.find('input.fn_con_idx').val(gVarConMaxIdx);
	gVarConMaxIdx ++;
}
function fn_init() {

<c:if test="${!empty dt}">
<c:set var="tabNameCnt" value="${0}"/>
<c:forEach var="tabIdx" begin="1" end="${2}" varStatus="i">
	<c:set var="tabName" value="TAB_NAME${tabIdx}"/>
	<c:if test="${!empty dt[tabName]}">
		<c:set var="tabNameCnt" value="${tabNameCnt + 1}"/>
	</c:if>
</c:forEach>
</c:if>

<c:if test="${tabNameCnt > 0}">
$('#tabMaxCnt option[value="${tabNameCnt}"]').prop('selected', true);
$('#tabMaxCnt').change();
</c:if>

$('#tab_max_cnt option[value="${tabNameCnt}"]').prop('selected', true);
$('#tab_max_cnt').change();

var varTab = $('#fn_formTab>li:last');
var varCnt = $('#tabMaxCnt option:selected').val();
if("${dt.REFERENCE}" == 1) {
	if(varCnt == '') {
		$('#c_w_h_tab').show();
		$('#c_w_h_tab_con').show();
		$('#fn_formTab>li:last a').click();
	}
	varTab.show();
}
}

/***************************************************************
textarea 추가
***************************************************************/
function fn_deleteTextArea(theObj)
{
	var varUlObj = $('.data_con');
	var varLiObjs = varUlObj.find('>dl');
	var varLiLen = varLiObjs.size();
	if(varLiLen <= 1)
	{
		alert('지울게 없어요');
		return;
	}
	theObj.parents('dl:first').remove();
}

function fn_bannerInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript reset setting */ %>
}

function fn_bannerInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/> 	<% /* javascript submit전 유효성 체크 */ %>
	fn_createMaskLayer();
	return true;
}

</script>