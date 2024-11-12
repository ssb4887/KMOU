<%@ include file="../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	
	// 상세보기
	$(".fn_btn_view").click(function(){
		try {
			location.href=$(this).attr("data-url");
		}catch(e){}
		return false;
	});
	
	// 선택
	$(".fn_btn_select").click(function(){
		try {
			opener.fn_setDesignForm($(this).attr("data-value"));
			self.close();
		}catch(e){alert(e);}
		return false;
	});
});

// 이미지 없는 경우 숨기기
function fn_errorImg(theObj){
	$(theObj).hide();
	$(theObj).parent().next().find(".fn_btn_view").hide();
}

</script>