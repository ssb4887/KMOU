<spring:message var="autoLogoutUse" code="Globals.auto.logout.use"/>
<c:if test="${autoLogoutUse eq 1 and elfn:isLogin()}">
<div class="fn_autoLogout_layerBg" style="display:none;"></div>
<div class="fn_autoLogout_layer" style="display:none;position:absolute;top:40%;left:50%;margin-left:-22%;">
	<div class="rsvHeader"><h1>자동 로그아웃</h1></div>
	<div class="rsvCont">
		<span class="fn_autoLogout_countdown"></span> 후 자동 로그아웃
		<a href="#none" class="fn_login_extension">로그인 연장</a>
		<a href="<c:out value="${elfn:getMenuUrl(4)}"/>">로그아웃</a>
	</div>
	<a href="#" class="fn_autoLogout_btnClose"><img src="<c:out value="${contextPath}${imgPath}/button/btn_close.png"/>" alt="닫기" /></a>
</div>
</c:if>