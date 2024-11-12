<script type="text/javascript">
$(function(){
	// 등록/수정
	$("#fn_passwordForm").submit(function(){
		try {
			$.ajax({
		  		type:'POST', 
		  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
		  		url:$(this).prop('action'), 
		  		data:$(this).serialize(),
		  		success:function(result){
		  			eval(result.replace(/<br\/>/g, '\\n'));
		  			if(result == '' && $('#chkUrl').val().indexOf('&dataNm=') >= 0){
		  				$.ajax({
	  						beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
	  						url:$('#chkUrl').val(),
	  						success:function(data){
	  							$('#fn_infoPwd').dialog("close");
	  							eval(data);
	  						}
		  				});
		  			}
		  		}
			});
			return false;
		}catch(e){return false;}
	});	
});

function fn_showPwdDialog(theIdx, theAction, pageType) {
	if(!fn_isValFill(theIdx)) return false;
	var varIdx = theIdx.split('|');
	var varChkUrl = (typeof(theAction) == "undefined")?"":theAction;
	$("#fn_passwordForm input[name='pwd']").val("");
	$("#fn_passwordForm input[name='chkUrl']").val(varChkUrl);
	var varAction = $("#fn_passwordForm").attr("action");
	if(typeof(pageType) == "undefined") pageType = '${pageType}'; 
	var passProcPath;
	if(pageType == 'v') passProcPath = 'viewPassProc.do';
	else passProcPath = 'passProc.do';
	var varAction = passProcPath + "<c:out value="${baseQueryString}"/>&<c:out value="${settingInfo.idx_name}"/>=" + varIdx[0];
	if(varIdx.length > 1) varAction += "&<c:out value="${settingInfo.memo_idx_name}"/>=" + varIdx[1];
	$("#fn_passwordForm").attr("action", varAction);
	var varDId = "fn_infoPwd";
	$("#" + varDId).dialog({
		bgifram: true,
   		autoOpen: false,
		resizable:false,
   		modal: true
   	});
	$("#" + varDId).dialog("option", "title", "비밀번호 확인");
	$("#" + varDId).dialog("option", "width", "500");
	$("#" + varDId).dialog("option", "height", "150");
	$("#" + varDId).css("z-index",10007);
	$("#" + varDId).css("overflow",'hidden');
	$("#" + varDId).css("background",'#FFF');
	$("#" + varDId).css("padding",'0px');
	$("#" + varDId).parent().css({"z-index":10006, "padding":"0px","border":"0px","background":"transparent"});
	$("#" + varDId).dialog("open");
}
</script>