<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ include file="pollMngChk.jsp" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>							<% /* 등록/수정 처리 항목목록 */ %>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/common.js"/>"></script>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript include */ %>
<script type="text/javascript">
$(function(){
	fn_setRelItemForm.infoUrl = "${elfn:replaceScriptLink(URL_ITEMLIST)}";				// 항목
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript 초기 setting */ %>
	
	fn_boardInputReset();
	
	<c:if test="${ISQUESTYPE}">
	try {$(document).on('keydown', '#<c:out value="${param.inputFormId}"/> input[name="itemPoints"]', function(event){return fn_onlyNumKD(event);});} catch(e) {}
	$(".fn_questype_hidden").hide();
	$(".fn_questype").hide();
	$(".fn_questype select, .fn_questype input").prop("disabled", true);
	</c:if>
	
	$('#answerType').trigger("change");
	$('#relQuesIdx').trigger("change");
	
	<c:if test="${dt.USE_INQUES == '1'}">
		fn_setInquesList();
	</c:if>
	
	<c:if test="${!isPollMng}">
	try {
		// 설문조사 진행중 비활성화 항목
		$('.fn_pollMng input[type="checkbox"], .fn_pollMng label, .fn_pollMng select, .fn_pollMng button').prop('disabled', true);
		$('.fn_pollMng button').css('opacity', '0.5');
		$(".fn_pollIngComment").removeClass("hidden");
	} catch(e) {}
	</c:if>
	
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

function fn_boardInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	<% /* javascript reset setting */ %>
}
function fn_boardInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/> 	<% /* javascript submit전 유효성 체크 */ %>
	
	try {
		var varChk = true;
		var varAsTypeVal = $('#answerType option:selected').val();
		var varItemNames = $('input.itemContents');
		var varItemNameSize = varItemNames.size();
		var varItemVal = '';
		var varItemItemNames = [['itemContents', '항목내용', 1, 100], <c:choose><c:when test="${ISQUESTYPE}">['itemPoints', '점수', 1, 2]</c:when><c:otherwise>['isInputText', '기타의견', 0]</c:otherwise></c:choose>];
		
		if(varAsTypeVal == '1' || varAsTypeVal == '2' && varItemNameSize > 1) {
			// 객관식, 주관식 다중 항목 체크
			varChk = fn_checkFillRequired(varItemNames, varItemItemNames);
			if(!varChk) return false;
		}
		
		if(varAsTypeVal == '1' && $('#useInques').prop('checked')) {
			// 객관식 && 내부문항 사용하는 경우
			var varInclassVal = '';
			var varInclassNames = $('input.inclassContents');
			var varInclassNameSize = varInclassNames.size();
			var varInclassItemNames = [['inclassContents', '분류', 1, 100]];
			
			if(varInclassNameSize > 1) {
				varChk = fn_checkFillRequired(varInclassNames, varInclassItemNames);
				if(!varChk) return false;
			}
	
			var varInquesVal = '';
			var varInquesTNames = $('input.inquesContents');
			var varInquesItemNames = [['inquesContents', '<c:if test="${!ISQUESTYPE}">내부</c:if>문항', 1, 100]];
	
			varChk = fn_checkFillRequired(varInquesTNames, varInquesItemNames);
			if(!varChk) return false;
			
			<c:choose>
			<c:when test="${ISQUESTYPE}">
			<%/* 결과유형 */ %>
			// 내부문항 name setting
			var i = 0;
			var varVal = 1;
			varInclassVal += varVal;
			
			// 내부문항
			var varInquesNames = varInquesTNames;
			$.each(varInquesNames, function(k){
				var varQVal = k + 1;
				if(i > 0 && k == 0) varInquesVal += ',';
				if(k > 0) varInquesVal += ',';
				varInquesVal += varVal + ':' + varQVal;
				$.each(varInquesItemNames, function(j){
					var varQItem = this;
					$('input.' + varQItem[0] + ':eq(' + k + ')').attr('name', varQItem[0] + varVal + '_' + varQVal);
				});
			});
			</c:when>
			<c:otherwise>
			<%/* 일반 */ %>
			// 분류, 내부문항 name setting
			$.each(varInclassNames, function(i){
				var varVal = i + 1;
				if(i > 0) varInclassVal += ',';
				varInclassVal += varVal;
				$.each(varInclassItemNames, function(j){
					var varItem = this;
					$('input.' + varItem[0] + ':eq(' + i + ')').attr('name', varItem[0] + varVal);
				});
				
				// 내부문항
				var varInquesWP = $(this).siblings('.fn_inques_list_wp');
				var varInquesNames = varInquesWP.find('input.inquesContents');
				$.each(varInquesNames, function(k){
					var varQVal = k + 1;
					if(i > 0 && k == 0) varInquesVal += ',';
					if(k > 0) varInquesVal += ',';
					varInquesVal += varVal + ':' + varQVal;
					$.each(varInquesItemNames, function(j){
						var varQItem = this;
						varInquesWP.find('input.' + varQItem[0] + ':eq(' + k + ')').attr('name', varQItem[0] + varVal + '_' + varQVal);
					});
					
					
				});
			});
			</c:otherwise>
		</c:choose>
			
			$('input[name="inclassIdxs"]').val(varInclassVal);
			$('input[name="inquesIdxs"]').val(varInquesVal);
		}
		
		// 항목 name setting
		$.each(varItemNames, function(i){
			var varVal = i + 1;
			if(i > 0) varItemVal += ',';
			varItemVal += varVal;
			$.each(varItemItemNames, function(j){
				var varItem = this;
				$('input.' + varItem[0] + ':eq(' + i + ')').attr('name', varItem[0] + varVal);
			});
		});
		
		$('input[name="itemIdxs"]').val(varItemVal);
		
		var varRelItemVal = '';
		$.each($('input[name="tmpRelItemIdx"]:checked'), function(i){
			var varVal = $(this).val();
			if(i > 0) varRelItemVal += ',';
			varRelItemVal += varVal;
		});
		$('input[name="relItemIdx"]').val(varRelItemVal);
		
		$('#fn_top_pollMng .fn_pollMng input[type="checkbox"], #fn_top_pollMng .fn_pollMng label, #fn_top_pollMng .fn_pollMng select').prop('disabled', false);
	}catch(e) {alert(e); return false;}
	fn_createMaskLayer();
	return true;
}
</script>