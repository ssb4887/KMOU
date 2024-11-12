<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<link type="text/css" rel="stylesheet" href="<c:out value="${contextPath}${moduleResourceRPath}/common.css"/>"></link>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tree.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/viewForm.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/viewItem.js"/>"></script>
<script type="text/javascript">
$(function(){
	// 선택메뉴정보 얻기위한 기본설정
	fn_MenuTree.rtObj = $("#fn_iaMenuUL");
	fn_MenuTree.setForm = $("#<c:out value="${param.settingFormId}"/>");
	fn_setMenuForm.formId = "<c:out value="${param.settingFormId}"/>";				// 입력폼
	fn_setMenuForm.infoUrl = "${elfn:replaceScriptLink(URL_VIEWINFO)}";				// 메뉴정보경로
});
</script>
<script type="text/javascript" src="<c:out value="${contextPath}${moduleResourceRPath}/view.js"/>"></script>
<script type="text/javascript">
$(function(){
	<c:set var="selMenuIdx" value="${param.menuIdx}"/>
	<c:if test="${empty selMenuIdx}"><c:set var="selMenuIdx" value="1"/></c:if>
	fn_MenuTree.select("<c:out value="${selMenuIdx}"/>", true);
	$("#fn_iaMenuUL li.fn_leftMenu_<c:out value="${selMenuIdx}"/> .btn_tree").click();
	fn_setTreeOpen($("#fn_iaMenuUL>li.root>ul"), $('#fn_leftMenu_${selMenuIdx}'));
});
</script>