<%@ include file="../../../include/commonTop.jsp"%>
<spring:message var="useMemberLog" code="Globals.memberAn.log.use.db" text="0"/>
<script type="text/javascript">
$(function(){	
	<c:if test="${useMemberLog == '1' && elfn:isAuth('MNG')}">
		$('.fn_btn_member_log_open').click(function(){
			try {
				fn_dialog.open($(this).parent('td').siblings('.fn_mbrId').text() + ' 개인정보 접근 기록', $(this).attr("href") + "&mdl=1", 0, 470);
			}catch(e){}
			return false;
		});
	</c:if>
});
</script>