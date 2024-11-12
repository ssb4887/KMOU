<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="mwtAuth" value="${elfn:isAuth('MWT')}"/>
<c:set var="listFormId" value="fn_boardListForm"/>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="items" value="${itemInfo.items}"/>
<c:set var="useQna" value="${settingInfo.use_qna eq '1'}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
		<c:set var="useReply" value="${settingInfo.use_reply eq '1'}"/>
		<c:set var="dsetCateListId" value="${settingInfo.dset_cate_list_id}"/>
		<c:set var="exceptIdStr">name,notice,subject,file,contents<c:if test="${!empty dsetCateListId}">,${dsetCateListId}</c:if></c:set>
		<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
		<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
		<table class="tbViewA" summary="${summary}">
			<caption>
			게시글 읽기
			</caption>
			<colgroup>
			<col style="width:100px;" />
			<col />
			</colgroup>
			<thead>
				<tr>
					<th colspan="2" class="viewTlt">
					<c:if test="${settingInfo.use_notice eq '1' and dt.NOTICE eq '1'}">[<spring:message code="item.notice.name"/>] </c:if>
					<c:if test="${!empty dsetCateListId}">
						<c:set var="dsetCateListVal"><itui:objectView itemId="${dsetCateListId}"/></c:set>
						<c:if test="${!empty dsetCateListVal}">[${dsetCateListVal}]</c:if>
					</c:if>
					<c:out value="${dt.SUBJECT}"/>
					<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(dt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th scope="row"><spring:message code="item.reginame1.name"/></th><td><c:out value="${dt.NAME}"/></td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="item.regidate1.name"/></th><td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${dt.REGI_DATE}"/></td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="item.board.views.name"/></th><td><c:out value="${dt.VIEWS}"/></td>
				</tr>
				<c:if test="${settingInfo.use_file == '1'}">
				<tr>
					<th scope="row"><spring:message code="item.file.name"/></th><td><itui:objectView itemId="file" multiFileHashMap="${multiFileHashMap}"/></td>
				</tr>
				</c:if>
				<itui:itemViewAll itemInfo="${itemInfo}" itemOrderName="${submitType}_order" exceptIds="${exceptIds}"/>
				<tr>
					<td class="cont" colspan="2">
						<itui:objectView itemId="contents"/>
					</td>
				</tr>
			</tbody>
		</table>
		<jsp:include page="../../common_mobile/viewBtns.jsp" flush="false"/>
		<c:if test="${!empty viewList}">
			<%@ include file="../../common/viewList.jsp"%>
		</c:if>
		<c:if test="${!useQna and useReply}">
			<jsp:include page="../../common/pntList.jsp" flush="false">
				<jsp:param name="itemInfo" value="${itemInfo}"/>
				<jsp:param name="summary" value="${summary}"/>
			</jsp:include>
		</c:if>
		<c:if test="${settingInfo.use_memo eq '1'}">
			<h5 class="titTypeC mgt20 mgb10">댓글</h5>
			<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
				<table class="tbWriteA" summary="글쓰기 서식">
					<caption>
					글쓰기 서식
					</caption>
					<colgroup>
						<col style="width:120px;" />
						<col />
					</colgroup>
					<tbody>
					</tbody>
				</table>
				<div class="btnCenter">
					<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
					<a href="<c:out value="${URL_IDX_MEMOINPUT}"/>" title="취소" class="btnTypeB fn_btn_write_view">취소</a>
				</div>
			</form>
			<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
			</form>
		</c:if>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>