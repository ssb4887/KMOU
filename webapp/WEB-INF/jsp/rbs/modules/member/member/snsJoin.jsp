<%@ include file="../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	// load init
	fn_init();
	
	// form submit
	$("#fn_loginForm").submit(function(){
		try {
			if(!fn_loginFormSubmit(this)) return false;
		} catch(e) {
			return false;
		}
	});
	<spring:message var="useSnsLogin" code="Globals.sns.login.use" text="0"/>
	<c:if test="${useSnsLogin == '1'}">
	$(".fn_btn_snsLogin").click(function(){
		var varUrl = $(this).attr("data-url");
		fn_dialog.open('', varUrl, 800, 500, 'fn_win_snsLogin');
		return false;
	});
	</c:if>
});
// load init
function fn_init() {
	$("#fn_loginForm input[name='mbrId']").focus();
}

// form submit
function fn_loginFormSubmit(theForm) {
	if(!fn_checkFill($(theForm).find("input[name='mbrId']"), "아이디")) return false;
	if(!fn_checkFill($(theForm).find("input[name='mbrPwd']"), "비밀번호")) return false;
    
    return true;
}

<c:if test="${useSnsLogin == '1'}">
function fn_snsLoginFormSubmit() {
	$("#fn_snsLoginForm").attr("action", "${URL_SNSLOGINOK}");
	$("#fn_snsLoginForm").submit();
}
</c:if>
</script>