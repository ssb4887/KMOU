<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="inputFormId" value="fn_menuInputForm"/>
<c:set var="settingFormId" value="fn_menuSettingForm"/>
<c:if test="${!empty TOP_PAGE}"><jsp:include page="${TOP_PAGE}" flush="false"/></c:if>
	<div class="tcomment" id="fn_comment" style="text-align: center;">
		${pageMessage}
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>