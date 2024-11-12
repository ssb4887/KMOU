<%@ include file="commonTop.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<%@ include file="commonHead.jsp" %>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}/include/js/jquery/css/jquery-ui.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/popup.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}/include/js/editor/css/smart_editor2_out.css"/>"/>
	<script type="text/javascript">
	$(function(){
		// 닫기
		$('#fn_btn_close').click(function(){
			closePopup();
			return false;
		});
	});

	function closePopup() {
		try {
			self.close();
		} catch(e) {
		}
	}
	</script>
	<c:choose>
		<c:when test="${menuType == 2}">
		<script type="text/javascript">
		$(function(){
			//	fn_contactSubmitForm('<c:out value="${contextPath}/${crtSiteId}/menuContents/stats/contact.do?mId=${queryString.mId}"/>&mn=${elfn:encode(usrCrtMenu.menu_name, 'UTF-8')}');
			});</script>
		</c:when>
		<c:otherwise>
			<%@ include file="../../include/contact.jsp"%>
		</c:otherwise>
	</c:choose>
</head>
<body>
<div id="wrapper">
	<!-- container -->
	<div id="container">
		<div id="contentsWrap">
			<div id="content">
