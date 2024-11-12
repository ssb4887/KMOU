<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@ include file="../include/common.jsp"%>
<%
	response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	boolean isAjax = StringUtil.isEquals(request.getHeader("Ajax"), "true");

if(isAjax) {
%>
<c:if test="${!empty nmAuthURL}">
	<c:set var="loginURL" value="${nmAuthURL}"/>
</c:if>
{"loginUrl":"${loginURL}"
<c:if test="${isAdmMode}">
	,"contUrl":"<c:out value="${elfn:getMenuUrl('10')}"/>"
</c:if>
}
<%}else{ %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="user-scalable=yes, initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width" name="viewport">
	<title><c:choose><c:when test="${!empty siteInfo}">${siteInfo.site_title}</c:when><c:otherwise><spring:message code="Globals.site.default.title"/></c:otherwise></c:choose> - 접근권한이 없습니다.</title>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}/include/css/proc.css"/>"/>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-1.9.1.min.js"/>"></script>
	<script type="text/javascript">
		$(function(){
			<c:choose>
				<c:when test="${!empty nmAuthURL}">
					<spring:message var="msgConfirm" code="message.no.auth.confirm"/>
					<spring:message var="msgPage" code="message.no.auth.page"/>
					<c:set var="loginURL" value="${nmAuthURL}"/>
				</c:when>
				<c:otherwise>
					<spring:message var="msgConfirm" code="message.no.login.confirm"/>
					<spring:message var="msgPage" code="message.no.login.page"/>
				</c:otherwise>
			</c:choose>
			var varConfirm = confirm("${msgConfirm}");
			fn_goLoginPage(varConfirm);
		});
		
		function fn_goLoginPage(varConfirm) {
			var varWin = top;
			var varWinO = [];
			var varWinCnt = 0;
			var varIsOpener = false;
			if(varConfirm) {
				<c:if test="${empty loginURL}">
					<c:set var="loginURL" value="${elfn:getMenuUrl('3')}"/>
				</c:if>
				var varLoginUrl = "${loginURL}";	// 로그인 경로
				var varToUrl;						// 되돌아갈 경로
				var varTmpWin = varWin;				// 현재창
				var varTmpOpener;					// opener
				for(var winI = 0 ; winI < 3 ; winI ++) {
					try {
						varTmpOpener = varTmpWin.opener;
						if(!varTmpWin) break;
						try{
							varIsOpener = varTmpOpener.top.gVarM;
						}catch(e){}
	
						varWinO[varWinCnt ++] = varTmpWin;
						if(varIsOpener) {
							varWin = varTmpOpener.top;
							break;
						}
						varTmpWin = varTmpOpener;
					} catch(e) {break;}
				}
				if(!varIsOpener) {
					varWin = top;
					varWinO = [];
					varWinCnt = 0;
				}
				varToUrl = varWin.location.href;
	    		<c:if test="${isAdmMode}">
	    		<c:set var="menuContentsUrl" value="${elfn:getMenuUrl('10')}"/>
	    		var varMenuContentsUrl = "<c:out value="${menuContentsUrl}"/>";
	    		if(varToUrl.indexOf("/menuContents/") != -1/* && varToUrl.indexOf(varMenuContentsUrl) == -1*/) {
	    			varToUrl = varMenuContentsUrl;
	    		} else if(varToUrl.indexOf("/menu/") != -1) {
	    			varToUrl = "<c:out value="${elfn:getMenuUrl('42')}"/>";
	    		}
	    		</c:if>
				
				varLoginUrl += (varLoginUrl.indexOf("?") != -1)?"&":"?";
				varLoginUrl += "url=" + encodeURIComponent(varToUrl);

				varWin.location.href = varLoginUrl;
				if(varWinCnt > 0) {
					for(var winI = varWinCnt ; winI > 0 ; winI --) {
						varWinO[winI - 1].close();
					}
				}
			} else {
				//history.go(-1);
				$("#fn_loginInfo").show();
			}
		}
	</script>
</head>
<body>
	<div id="fn_loginInfo" style="display:none;">
	<h1>로그인 안내</h1>
	<p>${msgPage}</p>
	<p class="btn">
		<button type="button" class="btnTypeAL" onclick="history.go(-1);return false;">이전페이지</button>
		<c:if test="${!isAdmMode}">
		<button type="button" class="btnTypeAL" onclick="location.href='<c:out value="${elfn:getMenuUrl('1')}"/>';return false;">메인</button>
		</c:if>
		<button type="button" class="btnTypeAL" onclick="fn_goLoginPage(true);return false;">로그인</button>
	</p>
	</div>
</body>
</html>
<%} %> 