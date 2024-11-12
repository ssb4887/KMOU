<%@page import="rbs.modules.menu.web.MenuPointInputUtil"%>
<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${crtMenu.use_poll == 1}">
	<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
	<c:set var="menuPointInputProcUrl" value="${contextPath}/${crtSiteId}/menu/${URL_INPUTPROC}"/>
	<c:set var="menuAvgPoint" value="${elfn:getFloor(menuPointDt.AVG_POINT)}"/>
	<c:set var="menuPointMode" value="${menuPointDt.AVG_POINT % 1}"/>
	<c:choose>
		<c:when test="${menuPointMode >= 0.5}"><c:set var="addStarClassName" value="_hf"/></c:when>
		<c:otherwise><c:set var="addStarClassName" value=""/></c:otherwise>
	</c:choose>
	<c:set var="menuPointInputFormId" value="fn_menuPointForm"/>
	<div id="cms_menu_point">
		<form id="${menuPointInputFormId}" name="${menuPointInputFormId}" method="post" action="<c:out value="${menuPointInputProcUrl}"/>" target="submit_target">
		<div class="socialbox">
			<p class="comment"><spring:message code="message.menu.point.alert.satisfication"/></p>
			<p class="point">(평균 <span class="star star<fmt:formatNumber value="${menuAvgPoint}" pattern="##0" />${addStarClassName}" id="fn_menuPointAP"><spring:message code="item.menu.point.point.text" arguments="${menuPointDt.AVG_POINT}"/></span> / <span id="fn_menuPointTC"><c:out value="${menuPointDt.CNT}"/></span>명 참여)</p>
			<div class="so_input">
				<dl>
					<c:set var="menuPointItemId" value="pmenuPoint"/>
					<dt><itui:objectLabel itemId="${menuPointItemId}" nextItemId="1" itemInfo="${itemInfo}"/></dt>
					<dd class="point"><itui:objectRadio itemId="${menuPointItemId}" itemInfo="${itemInfo}"/></dd>
					<c:set var="menuPointItemId" value="pmenuContents"/>
					<dt><itui:objectLabel itemId="${menuPointItemId}" itemInfo="${itemInfo}"/></dt>
					<dd class="point"><itui:objectTextarea itemId="${menuPointItemId}" itemInfo="${itemInfo}" objDt="${menuPointDt}"/></dd>
				</dl>
				<input type="submit" alt="의견등록" value="의견등록"/>
			</div>
		</div>
		</form>
	</div>
	<c:set var="itemOrderName" value="${submitType}proc_order"/>
	<script type="text/javascript">
		$(function(){
			$("#${menuPointInputFormId}").submit(function(){
				<c:choose>
				<c:when test="${wrtAuth}">
				<%/*권한있는 경우 && 로그인한 경우 : 등록*/%>
				<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
				fn_createMaskLayer();
				return true;
				</c:when>
				<c:when test="${elfn:isLoginAuth()}">
				<%/*권한없는 경우 && 로그인/본인인증한 경우 : 권한없음 메시지*/%>
				alert("<spring:message code="message.menu.point.no.auth"/>");
				return false;
				</c:when>
				<c:when test="${elfn:isNmAuthPage('WRT')}">
				<%/*권한없는 경우 && 로그인 안한 경우 && 글쓰기권한이 본인인증등급이상인 경우 : 본인인증페이지로 이동*/%>
				var varConfirm = confirm("<spring:message code="message.no.auth.confirm"/>");
				if(varConfirm) location.href="${elfn:getMenuUrl('9')}&url=" + encodeURIComponent(location.href);
				return false;
				</c:when>
				<c:otherwise>
				<%/*권한없는 경우 && 로그인 안한 경우 && 글쓰기권한이 본인인증등급초과인 경우 : 로그인페이지로 이동*/%>
				var varConfirm = confirm("<spring:message code="message.no.login.confirm"/>");
				if(varConfirm) location.href="${elfn:getMenuUrl('3')}&url=" + encodeURIComponent(location.href);
				return false;
				</c:otherwise>
				</c:choose>
			});
		});
	</script>
</c:if>