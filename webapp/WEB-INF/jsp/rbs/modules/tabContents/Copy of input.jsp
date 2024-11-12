<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>							<% /* 등록/수정 처리 항목목록 */ %>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript include */ %>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript 초기 setting */ %>
	

	for(var i = 1; i <= ${tabCnt} ; i ++) {
		try {
			(function(i){
			$(document).on('keyup', '#tab_name' + i, function(){
				var varChkStrLen = fn_chkStrLenForm($(this),  $(this).attr('title'));
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

$(function() {
	$('#fn_formTab li').hide();
	$('#c_w_h_tab').hide();
	$('#c_w_h_tab_con').hide();
	// 참고문헌 사용
	$('#reference2').click(function(){
		fn_setRefDisplay(this);
	});

	// 사용 콘텐츠 개수 선택
	$('#tab_max_cnt').change(function(){
		fn_setTabDisplay(this);
	});
	
	// 탭 클릭
	$('#fn_formTab>li a').click(function(){
		var varTabIdx = $(this).attr('data-idx');
		fn_tab.setTabCont(varTabIdx);
		$('#fn_formTab li a').removeClass('on');
		$(this).addClass('on');
		var varObjId = $(this).attr('href');
		$(varObjId).siblings('div').addClass('skip');
		$(varObjId).removeClass('skip');
		
		return false;
	});
});	
	// 콘텐츠 tab setting
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
			var varCnt = $('#tab_max_cnt').find('option:selected').val();
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
		var varCnt = $('#tab_max_cnt option:selected').val();
		if(varUse) {
			if(varCnt == '') {
				$('#c_w_h_tab').show();
				$('#c_w_h_tab_con').show();
				$('#fn_formTab>li:last a').click();
			}
			varTab.show();
		} else {
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
	
	// 탭명 setting
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
	/*********************************
	초기화 함수
	*********************************/
	function fn_init() {
		for(var i = 1; i <= ${tabCnt} ; i ++) {
			try {
				(function(i){
				$(document).on('keyup', '#tab_name' + i, function(){
					var varChkStrLen = fn_chkStrLenForm($(this),  $(this).attr('title'));
					if(!varChkStrLen) return false;

					fn_setTabName(i);
				});
				})(i);
			} catch(e) {}
			try {$('#tabCont' + i).css('display', 'none');} catch(e) {}

			fn_setTabName(i);
		}
	
		<c:if test="${!empty dt}">
		<jsp:useBean id="valObject" class="net.sf.json.JSONObject"></jsp:useBean>
		<c:set var="tabNameCnt" value="${0}"/>
		<c:forEach var="tabIdx" begin="1" end="${tabCnt}" varStatus="i">
			<c:set var="tabNameItemId" value="tab_name${tabIdx}"/>
			<c:set var="tabItemId" value="tab_contents${tabIdx}"/>
			<c:if test="${!empty dt[tabNameItemId]}">
				<c:if test="${i.first}">
				fn_tab.setTabCont('1');
				</c:if>
				<c:set var="tabNameCnt" value="${tabNameCnt + 1}"/>
			</c:if>
			
		</c:forEach>
		</c:if>
		<c:if test="${tabNameCnt > 0}">
		$('#tab_max_cnt option[value="${tabNameCnt}"]').prop('selected', true);
		$('#tab_max_cnt').change();
		</c:if>
	
	}
	
	

function fn_bannerInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript reset setting */ %>
}

function fn_bannerInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/> 	<% /* javascript submit전 유효성 체크 */ %>
	return true;
}

</script>