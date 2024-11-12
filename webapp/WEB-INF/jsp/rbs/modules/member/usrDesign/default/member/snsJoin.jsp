<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/snsJoin.jsp"/>
	</jsp:include>
</c:if>
<div class="sContent">
	<div id="cms_member_article">
		<div class="loginTit">
			<h4 class="tit">SNS 계정 연결</h4>
			
			<p class="txt">
			이미 가입되어있는 <c:out value="${siteInfo['site_name']}"/> 아이디가 있으신가요?<br/>
			<c:out value="${iSnsLoginVO.snsTypeName}"/> 계정과 연결을 원하시면 아이디와 비밀번호 입력 후 확인 버튼을 클릭해주세요.
			</p>
		</div>
		<div class="loginWrap">
			<form id="fn_loginForm" name="fn_loginForm" method="post" action="<c:out value="${URL_SNSJOINPROC}"/>" target="submit_target">
				<fieldset class="loginForm">
					<legend class="blind">SNS 간편로그인 - 아이디 연결 폼</legend>
					<ul class="login">
						<li>
							<itui:objectLabel itemId="mbrId" itemInfo="${itemInfo}"/><itui:objectText itemId="mbrId" itemInfo="${itemInfo}" objStyle="ime-mode:disabled;"/>
						</li>
						<li>
							<itui:objectLabel itemId="mbrPwd" itemInfo="${itemInfo}"/><itui:objectPassword itemId="mbrPwd" itemInfo="${itemInfo}" objStyle="width:260px;"/>
						</li>
					</ul>
					<div class="loginbtn">
						<input type="submit" id="fn_logIn_btn" class="btnLogin" value="확인"/>
					</div>
				</fieldset>
			</form>
		</div>
		<ul class="findJoin">
			<li>
				<a href="<%=MenuUtil.getMenuUrl(7) %>" title="아이디 찾기" class="btnTypeK">아이디 찾기</a>
				<span>아이디를 잊으셨나요?</span>
			</li>
			<li>
				<a href="<%=MenuUtil.getMenuUrl(8) %>" title="비밀번호 찾기" class="btnTypeK">비밀번호 찾기</a>
				<span>비밀번호를 잊으셨나요?</span>
			</li>				
			<li>
				<a href="<%=MenuUtil.getMenuUrl(2) %>" title="회원가입하기" class="btnTypeK">회원가입하기</a>
				<span>회원이 아니세요?</span>
			</li>
		</ul>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>