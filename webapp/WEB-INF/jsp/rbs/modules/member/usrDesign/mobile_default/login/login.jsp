<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/login.jsp"/>
	</jsp:include>
</c:if>
<div id="cms_member_article">
	<div class="loginTit">
		<h4 class="tit">LOGIN</h4>
		<p class="txt"><c:out value="${siteInfo['site_name']}"/>에 오신것을 환영합니다.<br/>서비스를 이용하기 위해서는 로그인이 필요합니다.<br/>로그인 후 서비스를 이용해 주세요.</p>
	</div>
	<div class="loginWrap">
		<form id="fn_loginForm" name="fn_loginForm" method="post" action="<c:out value="${URL_LOGINPREPROC}"/>" target="submit_target">
			<fieldset class="loginForm">
				<legend class="blind">회원 로그인 폼</legend>
				<ul class="login">
					<li>
						<itui:objectLabel itemId="mbrId" itemInfo="${itemInfo}"/><itui:objectText itemId="mbrId" itemInfo="${itemInfo}" objStyle="ime-mode:disabled;width:150px;" objAttr=" data-role='none'"/>
					</li>
					<li>
						<itui:objectLabel itemId="mbrPwd" itemInfo="${itemInfo}"/><itui:objectPassword itemId="mbrPwd" itemInfo="${itemInfo}" objStyle="width:150px;" objAttr=" data-role='none'"/>
					</li>
				</ul>
				<div class="loginbtn">
					<input type="submit" id="fn_logIn_btn" class="btnLogin" value="로그인" data-role="none" rel="external"/>
				</div>
			</fieldset>
		</form>
	</div>
	<div class="findJoin">
		<p class="txt">회원가입, 아이디/비밀번호찾기는 PC버전에서 이용가능합니다.</p>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>