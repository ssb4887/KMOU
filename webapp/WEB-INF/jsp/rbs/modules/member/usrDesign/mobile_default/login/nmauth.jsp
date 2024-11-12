<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/login.jsp"/>
	</jsp:include>
</c:if>
<%@ include file="../../../../../usr/nice/niceIpinInit.jsp"%>
<c:choose>
	<c:when test="${!empty param.url}"><c:set var="url" value="${param.url}"/></c:when>
	<c:otherwise><c:set var="url" value="${indexUrl}"/></c:otherwise>
</c:choose>
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

<div class="sContent">
	<div id="cms_member_article">
		<div class="titTypeA">회원가입 유형을 선택하세요.</div>
		<div class="register_wrap">
			<ul>
				<c:if test="${elfn:isUseNiceAuth()}">
				<li class="line">
					<div class="reg_icon1"></div>
					<h4>휴대폰인증</h4>
					<p>휴대폰인증</p>
					<!-- 본인인증 서비스 팝업을 호출하기 위해서는 다음과 같은 form이 필요합니다. -->
					<form name="form_chk" method="post">
						<input type="hidden" name="m" value="checkplusSerivce">						<!-- 필수 데이타로, 누락하시면 안됩니다. -->
						<input type="hidden" name="EncodeData" value="<%= sEncData %>">		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
					    
					    <!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
					    	 해당 파라미터는 추가하실 수 없습니다. -->
						<input type="hidden" name="param_r1" value="<c:out value="${contextPath}${url}"/>">
						<input type="hidden" name="param_r2" value="nmA">
						<input type="hidden" name="param_r3" value="">
					    
						<a href="javascript:fn_checkplusPopup();" class="btnTypeAL">휴대폰인증</a>
					</form>
				</li>
				</c:if>
				<c:if test="${elfn:isUseIpingAuth()}">
				<li>
					<div class="reg_icon2"></div>
					<h4>IPIN인증</h4>
					<p>IPIN인증</p>
					<!-- 가상주민번호 서비스 팝업을 호출하기 위해서는 다음과 같은 form이 필요합니다. -->
					<form name="form_ipin" method="post">
						<input type="hidden" name="m" value="pubmain">						<!-- 필수 데이타로, 누락하시면 안됩니다. -->
					    <input type="hidden" name="enc_data" value="<%= sIPINEncData %>">		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
					    
					    <!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
					    	 해당 파라미터는 추가하실 수 없습니다. -->
					    <input type="hidden" name="param_r1" value="<c:out value="${contextPath}${url}"/>">
					    <input type="hidden" name="param_r2" value="nmA">
					    <input type="hidden" name="param_r3" value="">
					    
						<a href="javascript:fn_ipinPopup();" class="btnTypeAL">IPIN인증</a>
					</form>
					<!-- 가상주민번호 서비스 팝업 페이지에서 사용자가 인증을 받으면 암호화된 사용자 정보는 해당 팝업창으로 받게됩니다.
						 따라서 부모 페이지로 이동하기 위해서는 다음과 같은 form이 필요합니다. -->
					<form name="vnoform" method="post">
						<input type="hidden" name="enc_data">								<!-- 인증받은 사용자 정보 암호화 데이타입니다. -->
						
						<!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
					    	 해당 파라미터는 추가하실 수 없습니다. -->
					    <input type="hidden" name="param_r1" value="">
					    <input type="hidden" name="param_r2" value="">
					    <input type="hidden" name="param_r3" value="">
					</form>
				</li>
				</c:if>
				<div class="cboth"></div>
			</ul>
		</div>
	</div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>