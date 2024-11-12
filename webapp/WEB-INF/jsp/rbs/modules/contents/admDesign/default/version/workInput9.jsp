<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_contentsInputForm"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/workInput9.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<c:set var="workItemInfo" value="${moduleItem.work_9_item_info}"/>
	<div id="cms_board_article">
		<%@ include file="workTop.jsp"%>
		<div class="btnTopFull">
			<c:choose>
			<c:when test="${empty menuType || menuType == 0}">
			<%/* 2차 개발 %>
			<c:if test="${useLocaleLang == '1'}">
			<div class="left">
			<select name="copyLang" id="copyLang" title="복제언어" class="select">
				<option value="">복제언어 선택</option>
			</select>
			<button type="button" class="btnTypeH">콘텐츠 복제</button>
			</div>
			</c:if>
			<% */ %>
			<div class="right">
				<a href="<c:out value="${URL_WORKLIST}"/>" class="btnTFDL">Branch/버전 목록</a>
				<a href="<c:out value="${URL_LIST}"/>" class="btnTFDL">콘텐츠 목록</a>
			</div>
			</c:when>
			<c:otherwise>
			<div class="right">
				<a href="<c:out value="${URL_WORKLIST}"/>" class="btnTFDL">버전 목록</a>
			</div>
			</c:otherwise>
			</c:choose>
		</div>
		<c:set var="useContentsType" value="${elfn:useSettingtem(contSettingInfo, contItemInfo.items['contType'])}"/>
		<c:set var="workContWidth" value="100%"/>
		<c:if test="${useContentsType}"><c:set var="workContWidth" value="700px"/></c:if>
		<div class="form1_cont1">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
			<itui:objectTextarea itemId="workContents" itemInfo="${workItemInfo}" objStyle="width:${workContWidth};height:300px;"/>
			<%@ include file="workBottom.jsp"%>
		</form>
		</div>
		<%/* 2차 개발 %>
		<c:if test="${useContentsType}">
		<div class="form1_cont2">
			사용콘텐츠
		</div>
		</c:if>
		<%*/%>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>