<%@ include file="commonTop.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="commonHead.jsp" %>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}/include/js/jquery/css/jquery-ui.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/dialog.css"/>"/>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}/include/js/editor/css/smart_editor2_out.css"/>"/>
	<script type="text/javascript">
	$(function(){
		$(".fn_btn_topClose").click(function(){
			self.close();
		});
	});
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
	<!-- container   style="position:relative;"-->
	<div>
		<div id="contentsWrap">
			<div id="content">
			<c:if test="${param.hideTop != '1'}">
			<h3><c:choose><c:when test="${!empty param.page_tit}"><c:out value="${param.page_tit}"/></c:when><c:when test="${!empty queryString.mt}"><c:out value="${queryString.mt}"/></c:when><c:when test="${!empty queryString.tit}"><c:out value="${queryString.tit}"/></c:when><c:otherwise><c:out value="${crtMenu.menu_name}"/></c:otherwise></c:choose></h3>
			<button type="button" class="fn_btn_topClose" title="닫기">닫기</button>
			</c:if>