<%@page import="com.woowonsoft.egovframework.util.DataSecurityUtil"%>
<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/login.jsp"/>
	</jsp:include>
</c:if>
	<div class="loginWrap">
		<form id="fn_loginForm" name="fn_loginForm" method="post" action="<c:out value="${URL_LOGINPREPROC}"/>" target="submit_target">
			<fieldset class="loginForm">
				<legend class="blind">회원 로그인 폼</legend>
				<ul class="login">
					<li>
						<itui:objectLabel itemId="mbrId" itemInfo="${itemInfo}"/><itui:objectText itemId="mbrId" itemInfo="${itemInfo}" objStyle="ime-mode:disabled;"/>
					</li>
					<li>
						<itui:objectLabel itemId="mbrPwd" itemInfo="${itemInfo}"/><itui:objectPassword itemId="mbrPwd" itemInfo="${itemInfo}" objStyle=""/>
					</li>
				</ul>
				<div class="loginbtn">
					<input type="submit" id="fn_logIn_btn" class="btnLogin" value="로그인"/>
				</div>
			</fieldset>
		</form>
       </div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>