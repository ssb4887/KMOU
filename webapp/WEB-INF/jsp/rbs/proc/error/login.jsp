<%@ include file="../../include/common.jsp"%>
<%
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><c:choose><c:when test="${!empty siteInfo}">${siteInfo.site_title}</c:when><c:otherwise><spring:message code="Globals.site.default.title"/></c:otherwise></c:choose> - 접근권한이 없습니다.</title>
	<script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-1.9.1.min.js"/>"></script>
	<script type="text/javascript">
		$(function(){
			var varConfirm = confirm("<spring:message code="message.no.login.confirm"/>");
			
			var varWin = top;
			var varWinO = [];
			var varWinCnt = 0;
			var varIsOpener = false;
			if(varConfirm) {
				var varLoginUrl = "<c:out value="${loginURL}"/>";	// 로그인 경로
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
					
					//if(varIsWinOpen == "1") varLoginUrl += "&dl=1";
				}
				varToUrl = varWin.location.href;
				<c:if test="${isAdmMode}">
	    		<c:set var="menuContentsUrl" value="${elfn:getMenuUrl('10')}"/>
	    		var varMenuContentsUrl = "<c:out value="${menuContentsUrl}"/>";
	    		if(varToUrl.indexOf("/menuContents/") != -1/* && varToUrl.indexOf(varMenuContentsUrl) == -1*/) {
	    			varToUrl = varMenuContentsUrl;
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
			}
		});
	</script>
</head>
<body></body>
</html>