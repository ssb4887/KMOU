<%@ include file="../../../../../include/commonTop.jsp"%>
<c:set var="isLoginPage" value="Y"/>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/login.jsp"/>
	</jsp:include>
</c:if>
<div class="sContent login_wrap">
		<div class="container_wrap">
        	<div class="sub_wrap">
				<div id="cms_member_article">
					<div class="login_article">
						<div class="loginTit">
							<h4 class="tit"><img src="../images/system_logo.png"></h4>
							<p class="txt"><!-- c:out value="${siteInfo['site_name']}"/ -->지능형 교과·비교과 검색 추천 서비스에 오신것을 환영합니다.<br/>대학 구성원(학생, 교직원)은 종합정보시스템 아이디/비밀번호를<br/>사용하여 로그인이 가능합니다.</p>
						</div>
						<div class="loginWrap">
							<spring:message var="useSnsLogin" code="Globals.sns.login.use" text="0"/>
							<form id="fn_loginForm" name="fn_loginForm" method="post" action="<c:out value="${URL_LOGINPREPROC}"/>" target="submit_target"><!-- URL_LOGINORACLEPROC, URL_LOGINPREPROC  -->
								<fieldset class="loginForm">
									<legend class="blind">회원 로그인 폼</legend>
									<ul class="login">
										<li>
											<itui:objectLabel itemId="mbrId" itemInfo="${itemInfo}"/><itui:objectText itemId="mbrId" itemInfo="${itemInfo}" objStyle="ime-mode:disabled;"/>
										</li>
										<li>
											<itui:objectLabel itemId="mbrPwd" itemInfo="${itemInfo}"/><itui:objectPassword itemId="mbrPwd" itemInfo="${itemInfo}" objStyle="width:260px;"/>
										</li>
									</ul>
									<div class="loginbtn">
										<input type="submit" id="fn_logIn_btn" class="btnLogin" value="로그인"/>
									</div>
								</fieldset>
							</form>
						</div>
					</div>	
				</div>
			</div>
		</div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>
<div style=" margin-bottom: -3vh;"></div>