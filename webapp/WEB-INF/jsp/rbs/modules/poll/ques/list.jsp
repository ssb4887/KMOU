<%@ include file="../../../include/commonTop.jsp"%>
<%@ include file="pollMngChk.jsp" %>
<script type="text/javascript">
$(function(){
	<c:if test="${!isPollMng}">
	// 삭제 비활성화
	$('.fn_btn_delete').attr('title', '${pollIngMsg}');
	$('.fn_btn_delete,.fn_btn_delete *').css('opacity', '0.5');
	$('.fn_btn_delete,.fn_btn_delete *').prop('disabled', true);
	$('.fn_btn_delete,.fn_btn_delete *').click(function(e){
		e.preventDefault();
		e.stopImmediatePropagation();
		return false;
	});
	$('input[name="selectAll"], input[name="select"]').prop('disabled', true);

	// 등록 비활성화
	//$('.fn_btn_write, .fn_btn_write_open').attr('title', '${pollIngMsg}');
	$('.fn_btn_write, .fn_btn_write_open').css('opacity', '0.5');
	$('.fn_btn_write, .fn_btn_write_open').prop('disabled', true);
	$('.fn_btn_write, .fn_btn_write_open').click(function(e){
		e.preventDefault();
		e.stopImmediatePropagation();
		return false;
	});
	
	$(".fn_pollIngComment").removeClass("hidden");
	</c:if>
});
</script>