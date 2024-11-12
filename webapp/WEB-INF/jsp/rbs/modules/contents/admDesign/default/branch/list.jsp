<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="vewAuth" value="${elfn:isAuth('VEW')}"/>
<c:set var="branchListFormId" value="fn_branchListForm"/>
<c:set var="versionListFormId" value="fn_versionListForm"/>
<c:set var="listFormId" value="${versionListFormId}"/>
<c:set var="inputWinFlag" value="_open"/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnBranchModifyClass" value="fn_btn_branch_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:set var="btnVerModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="branchListFormId" value="${branchListFormId}"/>
		<jsp:param name="listFormId" value="${versionListFormId}"/>
	</jsp:include>
</c:if>
	<c:set var="branch_input_dialog_height" value="400"/>
	<%@ include file="../../../branch/listBtnsScript.jsp"%>
	<div id="cms_board_article">
		<c:set var="searchLangUrl" value="${URL_BRANCHLANG_LIST}"/>
		<%@ include file="../top.jsp"%>
		<div class="topContInfo">
			<%@ include file="../../common/branchVerTop.jsp"%>
		</div>
		<div id="lnbWrap">
			<div class="infoWrap">
				<div class="info">
					<h4><spring:message code="title.branch.branchList"/></h4>
					<div class="btnTop">
						<a href="<c:out value="${URL_BRANCHDELETE_LIST}"/>" class="btnTFDL fn_btn_branch_deleteList">휴지통</a>
						<a href="<c:out value="${URL_BRANCHINPUT}"/>" title="등록" class="btnTFWL fn_btn_branch_write${inputWinFlag}">등록</a>
					</div>
					<form id="${branchListFormId}" name="${branchListFormId}" method="post" target="list_target">
					<ul class="lnbMd branch" style="min-height: 550px;">
					<c:if test="${empty list}">
						<li class="bllist"><spring:message code="message.no.list"/></li>
					</c:if>
					<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
					<c:forEach var="listDt" items="${list}" varStatus="i">
						<c:set var="listKey" value="${listDt[settingInfo.idx_column]}"/>
						<li<c:if test="${listKey == queryString.branchIdx}"> class="on"</c:if>>
							<a href="<c:out value="${URL_BRANCHDEFAULT_LIST}"/>&${listIdxName}=${listKey}" title="<c:out value="${listDt.BRANCH_NAME}"/>">[<itui:objectView itemId="branchType" itemInfo="${itemInfo}" objDt="${listDt}" optnHashMap="${optnHashMap}"/>]<c:out value="${listDt.BRANCH_NAME}"/></a>
							<p class="btn">
								<a href="<c:out value="${URL_BRANCHMODIFY}"/>&${listIdxName}=${listKey}" class="btnTFW ${btnBranchModifyClass}">수정</a>
								<a href="<c:out value="${URL_BRANCHDELETEPROC}"/>&${listIdxName}=${listKey}" class="btnTFDL fn_btn_branch_delete">삭제</a>
							</p>
						</li>
					</c:forEach>
					</ul>
					</form>
				</div>
			</div>
		</div>
		
		<div class="inWfullContWrap">
			<%@ include file="../../common/verListCon.jsp"%>
		</div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>