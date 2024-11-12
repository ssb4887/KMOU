<%@ include file="../../include/commonTop.jsp"%>
<c:set var="rwtAuth" value="${elfn:isAuth('RWT')}"/>							<%/* 참여권한 */ %>
<c:if test="${!empty param.inputFormId}">
	<c:set var="inputFormId" value="${param.inputFormId}"/>
</c:if>
<c:if test="${!empty param.btnScrolling}">
	<c:set var="btnScrolling" value="${param.btnScrolling}"/>
</c:if>
<script type="text/javascript">
$(function(){
	
	fn_pollRespReset();
	
	// 항목 클릭 : 연관문항 활성/비활성
	$('.ques_items').click(function(){
		fn_setChkRelQues($(this));
	});
	<c:if test="${btnScrolling}">
	// 버튼 위치 setting
	fn_setPollBtn();
	</c:if>
	
	// reset
	$("#<c:out value="${inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${inputFormId}"/>").reset();
			fn_pollRespReset();
		}catch(e){return false;}
	});
	
	// cancel
	$("#<c:out value="${inputFormId}"/> .fn_btn_cancel").click(function(){
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
	$("#<c:out value="${inputFormId}"/>").submit(function(){
		try {
			// 임시저장인 경우
			var varComp = $("#iscomp").val();
			if(varComp == '0') return true;
			
			return fn_pollRespFormSubmit();
		}catch(e){alert(e); return false;}
	});
	
	// 임시저장
	$("#fn_btn_tmp_join").click(function(){
		try {
			var varResult = fn_pollRespFormTmpSubmit();
			if(varResult) $("#<c:out value="${inputFormId}"/>").submit();
		} catch(e) {alert(e); return false;}
	});
	
	<c:if test="${ingDt.ISRESULT == '1'}">
	// 결과보기
	$("#fn_btn_result").click(function(){
		var varHref = $(this).val();
		if(varHref) {
			location.href = $(this).val();
			return false;
		}
		return true;
	});
	</c:if>
});
<c:if test="${btnScrolling}">
//버튼 위치 setting
function fn_setPollBtn(){
	var varScrollTop;
	var varBtnHeight = $('#wrap_poll_submit_btn').height();
	var varPollTop = $('#fn_poll_respDiv').offset().top;
	var varBtnTop = $('#wrap_poll_submit_btn').offset().top - varPollTop - 20;
	var varPollBottom = varPollTop + $('#fn_poll_respDiv').height();
	//$('html, body').animate({scrollTop:0}, 1000);
	$(window).scroll(function(){
		varScrollTop = $(window).scrollTop();
		var varTop = varScrollTop + varBtnTop;
		var varBtnBottom = $('#wrap_poll_submit_btn').offset().top + varBtnHeight + varScrollTop;
		if(varPollBottom <= varBtnBottom) return false;
		$('#wrap_poll_submit_btn').stop().animate({'top':varTop + 'px'}, 500);
	});
}
</c:if>
function fn_pollRespReset(theData) {

	// 전체 연관문항 비활성화
	fn_setRelQues($('.relQues'), -1, true);
	
	<c:if test="${!empty resultMap}">
	var varChkedItems = $('.ques_items:checked');
	$.each(varChkedItems, function(){
		fn_setChkRelQues($(this));
	});
	
	</c:if>
	
	if(typeof(theData) == 'undefined') return false;
	
	// 임시저장값 setting
	var varDataLen = 0;
	varDataLen = Object.keys(theData).length;
	if(varDataLen > 0) {
		for(var key in theData) {
			var varKeys = key.split(',');
			var varQuesIdx = varKeys[0];
			var varItemIdx = varKeys[1];
			
			var varRData = theData[key];
			var varItemObj = $('#itemIdx_' + varQuesIdx + '_' + varItemIdx);
			try{varItemObj.prop('checked', true);}catch(e){}
			try{$('#istext_contents_' + varQuesIdx + '_' + varItemIdx).val(varRData.contents);}catch(e){}
			// 선택값에 대한 연관문항 비활성화
			try{fn_setChkRelQues(varItemObj);}catch(e){}
		}
	}
}

/**
 * 항목선택시 연관문항 setting
 */
function fn_setChkRelQues(theItemObj) {

	var varChked = theItemObj.prop('checked');
	var varQuesIdx = theItemObj.attr('data-ques');
	var varItemIdx = theItemObj.val();
	
	// 연관문항
	var varRelQues = $('.relQues' + varQuesIdx);
	var varRelQuesSize = varRelQues.size();
	
	if(varRelQuesSize == 0) return false;
	
	if(varChked) {
		$.each(varRelQues, function(){
			var varItemStr = $(this).attr('data-relitems');
			var varIsRel = fn_getIndexOf(',', varItemStr, varItemIdx);
			fn_setRelQues($(this), varIsRel);
		});
		
	}
}

//연관문항 활성/비활성
function fn_setRelQues(theObj, theIsRel, theInit){

	if(theIsRel != -1) {
		// 활성
		theObj.find('*').prop('disabled', false);
		theObj.find('input[name="questionIdx"]').removeClass('disabled');
	} else {
		// 비활성
		if(!theInit) {
			theObj.find('input[type="radio"], input[type="checkbox"]').prop('checked', false);
			theObj.find('input[type="text"]').val('');
		}
		theObj.find('*').prop('disabled', true);
		
		var varQuesObj = theObj.find('input[name="questionIdx"]');
		var varDisabled = varQuesObj.hasClass("disabled");
		if(varDisabled) return false;
		varQuesObj.addClass('disabled');
		
		if(theInit) return false;
		
		// 활성 상태인 경우 -> 연관문항 비활성
		//alert(varQuesObj.val());
		var varItemObjs = theObj.find(".ques_items");
		$.each(varItemObjs, function(){
			fn_initRelQues($(this));
		});
	}
}

// 연관문항 비활성화
function fn_initRelQues(theItemObj) {

	var varQuesIdx = theItemObj.attr('data-ques');
	var varItemIdx = theItemObj.val();
	
	// 연관문항
	var varRelQues = $('.relQues' + varQuesIdx);
	var varRelQuesSize = varRelQues.size();
	
	if(varRelQuesSize == 0) return false;
	
	$.each(varRelQues, function(){
		fn_setRelQues($(this), -1);
	});
}



var varConLimitLen = 100;		// 입력 글자수

/**
 * 참여하기 체크
 */
function fn_pollRespFormSubmit(){
	var varForm = $("#<c:out value="${inputFormId}"/>");
	<c:choose>
		<c:when test="${isPollResp}">
		alert("<spring:message code="message.poll.duplicate"/>");
		return false;
		</c:when>
		<c:when test="${rwtAuth}">
		<%/*권한있는 경우 && 로그인한 경우 : 등록*/%>
		var varRetIdx = 0;
		var isComp = true;
		var varQObjs = $('.poll_list>li>input[name="questionIdx"]:not(.disabled)');
		var varQIdx = 0;
		varQObjs.each(function(){
			if(!isComp) return false;

			var varQObj = $(this).siblings(".q");
			var varHasQNum = (!varQObj.hasClass("hidden"));
			var varQOrderTxt = varQObj.find(".num").text();
			var varQOrder = new Number(varQOrderTxt);
			var ItemChkCnt = 0;
			var varQObj = $(this);
			var varQuesVal = varQObj.val();
			var varAObj = varQObj.siblings(".a");
			var varAnswerTypeVal = $(this).siblings('input[name="answerType"]').val();
			
			if(varAnswerTypeVal == '2'){
				varAObj.find("ul input[name='itemIdx_" + varQuesVal + "']").each(function(j){
					var varItemObj = $(this);
					var varItemVal = varItemObj.val();
					var varTextObj = $('#istext_contents_' + varQuesVal + '_' + varItemVal);

					// 내용
					if(!fn_checkFill(varTextObj, fn_Message.getPollQuesText(varQOrderTxt, varTextObj.attr('title')))){
						varRetIdx = varQuesVal;
						isComp = false;
						return false;
					}
					if(!fn_checkStrMaxLenForm(varTextObj, fn_Message.getPollQuesText(varQOrderTxt, varTextObj.attr('title')), varConLimitLen)) {
						isComp = false;
						return false;
					}
				});
			}else{
				// 객관식
				// 내부문항
				var varMsgQOrderText = (varHasQNum)? varQOrderTxt + '의 ':'';
				var varInquesTObjs = varAObj.find('table.inQuesTable');
				var varInquesTObjSize = varInquesTObjs.size();
				if(varInquesTObjSize > 0) {
					// 내부문항 있는 경우
					varInquesTObjs.each(function(){
						// 내부분류
						var varInclassObj = $(this).prev('.inclassTitle');
						var varInclassObjSize = varInclassObj.size();
						var varInclassName = (varInclassObjSize > 0) ? varInclassObj.text() + ' ':'';
						var varInquesObjs = $(this).find('input[name="questionIdx"]');
						var varInquesObjSize = varInquesObjs.size();
					
						varInquesObjs.each(function(theInqOrder){
							var varMsgInqOrder = (varHasQNum)? (theInqOrder + 1):(++varQIdx);
							var varInquesVal = $(this).val();
							var varItemObjs = $(this).parent().siblings('td').find('input[name="itemIdx_' + varInquesVal + '"]');
							
							
							isComp = fn_checkRequiredItems(varMsgQOrderText + varInclassName + varMsgInqOrder, varInquesVal, varItemObjs);
							if(!isComp) return false;
						});
						
						if(!isComp) return false;
					});
				} else {
					// 내부문항 없는 경우
					var varItemObjs = varAObj.find("ul input[name='itemIdx_" + varQuesVal + "']");
					isComp = fn_checkRequiredItems(varQOrderTxt, varQuesVal, varItemObjs);
				}
				
				if(!isComp) {
					varRetIdx = varQuesVal;
					return false;
				}
			}
		});

		if(!isComp) {
			location.href = '#fn_ques' + varRetIdx;
			return false;
		}
		
		var varConf = confirm('참여하신 설문은 수정하실 수 없습니다.\n설문에 참여하시겠습니까?');
		if(!varConf) return false;
		
		$('#iscomp').val('1');
		return true;
		</c:when>
		<c:when test="${elfn:isLoginAuth()}">
		<%/*권한없는 경우 && 로그인/본인인증한 경우 : 권한없음 메시지*/%>
		alert("<spring:message code="message.no.auth"/>");
		return false;
		</c:when>
		<c:when test="${elfn:isNmAuthPage('RWT')}">
		<%/*권한없는 경우 && 로그인 안한 경우 && 글쓰기권한이 본인인증등급이상인 경우 : 본인인증페이지로 이동*/%>
		var varConfirm = confirm("<spring:message code="message.no.auth.confirm"/>");
		if(varConfirm) location.href="${elfn:getMenuUrl('9')}&url=" + encodeURIComponent(location.href);
		return false;
		</c:when>
		<c:otherwise>
		<%/*권한없는 경우 && 로그인 안한 경우 && 글쓰기권한이 본인인증등급초과인 경우 : 로그인페이지로 이동*/%>
		var varConfirm = confirm("<spring:message code="message.no.login.confirm"/>");
		if(varConfirm) location.href="${elfn:getMenuUrl('3')}&url=" + encodeURIComponent(location.href);
		return false;
		</c:otherwise>
	</c:choose>
}

/**
 * 객관식 필수선택 체크
 */
function fn_checkRequiredItems(theQOrder, theQuesVal, theItemObjs){

	// 선택 체크
	var varItemObjs = theItemObjs.filter(':checked');
	var varItemObjSize = varItemObjs.size();
	if(varItemObjSize == 0) {
		alert(theQOrder + "<spring:message code="message.poll.radio.check"/>");
		return false;
	}
	
	// 기타의견 체크
	var isComp = true;
	$.each(varItemObjs, function(){
		var varItemObj = $(this);
		var varItemVal = varItemObj.val();
		var varIsText = varItemObj.attr('data-istext');
		// 기타 의견
		if(varIsText == '1') {
			var varTextObj = $('#istext_contents_' + theQuesVal + '_' + varItemVal);
			if(!fn_checkFill(varTextObj, fn_Message.getPollQuesText(theQOrder, varTextObj.attr('title')))){
				isComp = false;
				return false;
			}

			if(!fn_checkStrMaxLenForm(varTextObj, fn_Message.getPollQuesText(theQOrder, varTextObj.attr('title')), varConLimitLen)) {
				isComp = false;
				return false;
			}
		}
	});
	
	return isComp;
}

/**
 * 임시저장 체크
 */
function fn_pollRespFormTmpSubmit(){
	var varForm = $("#<c:out value="${inputFormId}"/>");
	<c:choose>
		<c:when test="${isPollResp}">
		alert("<spring:message code="message.poll.duplicate"/>");
		return false;
		</c:when>
		<c:when test="${rwtAuth}">
		<%/*권한있는 경우 && 로그인한 경우 : 등록*/%>
		var varQObjs = $('.poll_list>li>input[name="questionIdx"]:not(.disabled)');
		var isResp = false;
		var isTLChk = true;
		
		// 객관식
		var varItemObjs = varQObjs.parent().find('input.ques_items:checked');
		var varItemObjSize = varItemObjs.size();
		if(varItemObjSize > 0) isResp = true;
		$.each(varItemObjs, function(){
			var varItemObj = $(this);
			var varItemVal = varItemObj.val();
			var varQuesVal = varItemObj.attr('data-ques');
			var varQOrder = $('#fn_ques' + varQuesVal + ' .num:eq(0)').text();
			var varIsText = varItemObj.attr('data-istext');
			// 기타 의견
			if(varIsText == '1') {
				var varTextObj = $('#istext_contents_' + varQuesVal + '_' + varItemVal);
				if(!fn_checkStrMaxLenForm(varTextObj, fn_Message.getPollQuesText(varQOrder, varTextObj.attr('title')), varConLimitLen)) {
					isTLChk = false;
					return false;
				}
			}
		});
		
		if(isTLChk) {
			// 주관식
			varItemObjs = varQObjs.parent().find('input.ques_items_h');
			varItemObjSize = varItemObjs.size();

			$.each(varItemObjs, function(){
				var varItemObj = $(this);
				var varItemVal = varItemObj.val();
				var varQuesVal = varItemObj.attr('data-ques');
				var varQOrder = $('#fn_ques' + varQuesVal + ' .num:eq(0)').text();
				// 기타 의견
				var varTextObj = $('#istext_contents_' + varQuesVal + '_' + varItemVal);
				if(fn_isFill(varTextObj)) isResp = true;
				if(!fn_checkStrMaxLenForm(varTextObj, fn_Message.getPollQuesText(varQOrder, varTextObj.attr('title')), varConLimitLen)) {
					isTLChk = false;
					return false;
				}
			});
		}
		
		if(!isTLChk) return false;
		else if(!isResp) {
			alert("<spring:message code="message.poll.response.no.input"/>");
			location.href = '#fn_ques' + varQObjs.filter(':eq(0)').val();
			return false;
		}
		
		var varConf = confirm("임시저장하신 설문은 해당 설문의 설문기간 동안 참여하실 수 있습니다.\n임시저장하시겠습니까?");
		if(!varConf) return false;
		
		$('#iscomp').val('0');
		return true;
		</c:when>
		<c:when test="${elfn:isLoginAuth()}">
		<%/*권한없는 경우 && 로그인/본인인증한 경우 : 권한없음 메시지*/%>
		alert("<spring:message code="message.no.auth"/>");
		return false;
		</c:when>
		<c:when test="${elfn:isNmAuthPage('RWT')}">
		<%/*권한없는 경우 && 로그인 안한 경우 && 글쓰기권한이 본인인증등급이상인 경우 : 본인인증페이지로 이동*/%>
		var varConfirm = confirm("<spring:message code="message.no.auth.confirm"/>");
		if(varConfirm) location.href="${elfn:getMenuUrl('9')}&url=" + encodeURIComponent(location.href);
		return false;
		</c:when>
		<c:otherwise>
		<%/*권한없는 경우 && 로그인 안한 경우 && 글쓰기권한이 본인인증등급초과인 경우 : 로그인페이지로 이동*/%>
		var varConfirm = confirm("<spring:message code="message.no.login.confirm"/>");
		if(varConfirm) location.href="${elfn:getMenuUrl('3')}&url=" + encodeURIComponent(location.href);
		return false;
		</c:otherwise>
	</c:choose>
}


/**
 * 임시저장 객관식 선택 체크
 * return : 0 - 선택 and 글자수 true, 1 - 미선택, 2 - 글자수 false
 */
function fn_checkTmpItems(theQOrder, theQuesVal, theItemObjs){
	var varItemObjs = theItemObjs.filter(':checked');
	if(!varItemObjs.is('input')) {
		return 1;
	}
	
	// 기타의견 체크
	var varResp = 0;
	$.each(varItemObjs, function(){
		var varItemObj = $(this);
		var varItemVal = varItemObj.val();
		var varIsText = varItemObj.attr('data-istext');
		// 기타 의견
		if(varIsText == '1') {
			var varTextObj = $('#istext_contents_' + theQuesVal + '_' + varItemVal);
			if(!fn_checkStrMaxLenForm(varTextObj, fn_Message.getPollQuesText(theQOrder, varTextObj.attr('title')), varConLimitLen)) {
				varResp = 2;
				return false;
			}
		}
	});
	
	return varResp;
}
</script>