<c:if test="${empty param.preVerIdx}">
<script type="text/javascript">
$(function(){
	if("${queryString.mId}" != ''){
		fn_contactSubmitForm('<c:out value="${contextPath}/${crtSiteId}/stats/contact.do?mId=${queryString.mId}"/>&sn=${elfn:encode(siteInfo.site_name, 'UTF-8')}&mn=${elfn:encode(crtMenu.menu_name, 'UTF-8')}');	
	}
});</script>
</c:if>