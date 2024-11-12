<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){	
	<c:if test="${settingInfo.use_qna eq '1' or settingInfo.use_reply eq '1'}">
		$("a[id^=fn_btn_view_]").click(function(){
			var varIsNMChk = $(this).attr("data-nm");
			if(fn_isValFill(varIsNMChk) && $(this).parents('tr').next('.fn_qCont').length <= 0) {
				if($(this).attr('data-chk') == 'true'){
					<c:choose>
					<%--
						<c:when test="${(elfn:isNmAuthPage('WRT') and elfn:isNoMemberAuthPage('RWT')) or (elfn:isNmAuthPage('RWT') and elfn:isNoMemberAuthPage('WRT'))}">
					--%>
						<c:when test="${elfn:isNoMemberAuthPage('RWT') or elfn:isNoMemberAuthPage('WRT')}">
							if(confirm('비밀번호를 입력하시겠습니까?'))
								fn_showPwdDialog(varIsNMChk, $(this).attr("href") + '&dataNm=' + varIsNMChk, 'v');
							else
								top.location.href = $(this).attr("href");
						</c:when>
						<c:otherwise>
							fn_showPwdDialog(varIsNMChk, $(this).attr("href") + '&dataNm=' + varIsNMChk, 'v');
						</c:otherwise>
					</c:choose>
				}
				else{
					top.location.href = $(this).attr("href");
				}
			}
			else $(this).parent('td').parent('tr').next('tr').toggle();
			return false;
		});
	</c:if>
});
</script>