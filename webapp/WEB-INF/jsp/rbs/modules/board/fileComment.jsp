<script type="text/javascript">
$(function(){
	
	<%-- 파일다운로드 사유등록 --%>
	$(".fn_filedown").click(function(){
		fn_showFileCmtDialog($(this).attr("href"));
		return false;
	});
	
	// 파일다운로드 사유 등록
	$("#fn_fileCmtForm").submit(function(){
		$('#fn_infoFileCmt').dialog("close");
		/*try {
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
		}catch(e){return false;}*/
	});	
});

/**
 * 파일다운로드 사유 등록
 */
function fn_showFileCmtDialog(theAction) {
	if(!fn_isValFill(theAction)) return false;
	$("#fn_fileCmtForm").attr("action", theAction);
	$("#fn_fileCmtForm textarea").val("");
	var varDId = "fn_infoFileCmt";
	$("#" + varDId).dialog({
		bgifram: true,
   		autoOpen: false,
		resizable:false,
   		modal: true
   	});
	$("#" + varDId).dialog("option", "title", "파일다운로드 사유 등록");
	$("#" + varDId).dialog("option", "width", "700");
	$("#" + varDId).dialog("option", "height", "350");
	$("#" + varDId).css("z-index",10007);
	$("#" + varDId).css("overflow",'hidden');
	$("#" + varDId).css("background",'#FFF');
	$("#" + varDId).css("padding",'0px');
	$("#" + varDId).parent().css({"z-index":10006, "padding":"0px","border":"0px","background":"transparent"});
	$("#" + varDId).dialog("open");
}
</script>