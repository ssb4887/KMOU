<%@ include file="../../include/commonTop.jsp"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<script type="text/javascript">
$(function(){	

	$('li[id^=tab]').click(function(){
		var varObj = $(this);
		var varObjRel = varObj.attr('rel');
		$('li[id^=tab]').each(function(){
			$(this).removeClass('on');
			$('#' + $(this).attr('rel')).hide();
		});
		varObj.addClass('on');
		$('#' + varObjRel).show();
		return false;
	});

	<c:if test="${mngAuth}">
	// 삭제
	$(".fn_btn_delete").click(function(){
		try {
			var varConfirm = confirm("<spring:message code="message.select.delete.confirm"/>");
			if(!varConfirm) return false;
		}catch(e){return false;}
		return true;
	});
	</c:if>
});
</script>