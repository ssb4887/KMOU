<%@ include file="commonTop.jsp" %>
<!DOCTYPE html> 
<html> 
<head>
	<%@ include file="commonHead.jsp" %>
	<link rel="stylesheet" type="text/css" href="<c:out value="${contextPath}${cssPath}/common.css"/>"/>
	<script type="text/javascript">
	var gVarM = true;
	$(function(){
		$(".fn_btn_back").click(function(){history.go(-1);});
	});
	</script>
	<%@ include file="../../../include/designPath.jsp" %>
	<%@ include file="../../../include/contact.jsp"%>
</head>
<body>
<div data-role="page" class="ui-body-d">
	<%@ include file="header.jsp"%>
	
	<!-- container -->
	<div data-role="content">
		<h2><c:choose><c:when test="${!empty crtMenu.menu_name3}"><c:out value="${crtMenu.menu_name3}"/></c:when><c:otherwise><c:out value="${crtMenu.menu_name2}"/></c:otherwise></c:choose></h2>