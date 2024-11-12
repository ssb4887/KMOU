<spring:message var="autoLogoutUse" code="Globals.auto.logout.use"/>
<c:if test="${autoLogoutUse eq 1 and elfn:isLogin()}">
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/autoLogoutCheck.js"/>"></script>
<script type="text/javascript"><!--
$(function(){
	varAutoLogout.logoutUrl = '<c:out value="${elfn:getMenuUrl(4)}"/>';
	varAutoLogout.windowType = '<c:out value="${queryString.mdl}"/>';
	varAutoLogout.init();
});
//--></script>
</c:if>