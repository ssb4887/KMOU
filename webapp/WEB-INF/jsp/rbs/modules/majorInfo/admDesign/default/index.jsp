<%@ include file="../../../../include/commonTop.jsp"%>
<!-- <link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/> -->
<%-- <%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %> --%>
<%-- <%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %> --%>
<%-- <c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/> --%>
<%-- <c:set var="searchFormId" value="fn_techSupportSearchForm"/> --%>
<%-- <c:set var="listFormId" value="fn_techSupportListForm"/> --%>
<%-- <c:set var="inputWinFlag" value=""/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%> --%>
<%-- <c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%> --%>
<%-- <c:if test="${!empty TOP_PAGE}"> --%>
<%-- 	<jsp:include page="${TOP_PAGE}" flush="false"> --%>
<%-- 		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/> --%>
<%-- 		<jsp:param name="searchFormId" value="${searchFormId}"/> --%>
<%-- 		<jsp:param name="listFormId" value="${listFormId}"/> --%>
<%-- 	</jsp:include> --%>
<%-- </c:if>	 --%>
	<body style="margin:0px;">
	<div >
	<iframe id="fn_ifrm_input" name="fn_ifrm_input" src="<c:out value="${URL_LIST}"/>" frameBorder="0px" scrolling="yes" scrollbar="yes" style="width:100%;height:100%;">
	
	</iframe>
	</div>
	</body>
	
	
<script type="text/javascript">
$(function(){
	myFunction();
});
function myFunction(){
	
	document.querySelectorAll('a').addEventListener("click", handleClick, {once: true});
}

function handleClick(){
    const currentUrl = new URL(window.location.href);	  
    
    alert(currentUrl.pathname)

    if ("/RBISADM/menuContents/web/majorInfo/index.do" === currentUrl.pathname) {
        alert("?")
        window.location.reload(); // 페이지를 새로고침합니다.
        alert("1")
        document.getElementById("fn_ifrm_input").removeEventListener("click", handleClick);
        alert("2")
        e.preventDefault(); // 기본 이동 동작을 방지합니다.
    }

	
}

</script>

	




