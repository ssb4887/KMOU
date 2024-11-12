<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							<%/* 등록관리권한 */ %>
<c:set var="searchFormId" value="fn_pollSearchForm"/>							<%/* 검색폼 id/name */ %>
<c:set var="listFormId" value="fn_pollListForm"/>								<%/* 목록관리폼 id/name */ %>
<c:set var="inputWinFlag" value="_open"/>										<%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/>				<%/* 수정버튼 class */%>
<c:set var="settingInfo" value="${moduleSetting.cont_setting_info}"/>
<spring:message var="msgItemMng" code="title.poll.question.manager"/>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<%/* 검색폼 id/name : javascript에서 사용 */ %>
	<%/* 목록관리폼 id/name : javascript에서 사용 */ %>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="hideTop" value="1"/>
	</jsp:include>
</c:if>
	<c:set var="quesItemInfo" value="${moduleItem.ques_item_info}"/>
	<div id="cms_poll_article">
		<div class="poll_box fn_poll_result">
			<div class="wrap">
				<div class="poll_ing">
					<dl class="p">
						<dt><itui:objectView itemId="title" itemInfo="${itemInfo}" objDt="${dt}"/></dt>
						<c:if test="${!empty dt.LIMIT_DATE11 || !empty dt.LIMIT_DATE21}">
						<dd class="date_l"><span class="tlt">설문기간 : </span><span><itui:objectView itemId="limitDate" itemInfo="${itemInfo}" objDt="${dt}"/></span></dd>
						</c:if>
						<dd class="cont_l"><span class="tlt"><spring:message code="item.poll.question"/> : </span><itui:objectView itemId="questionContents" itemInfo="${quesItemInfo}" objDt="${dt}"/></dd>
						<c:if test="${!empty dt.ITEM_CONTENTS}">
						<dd class="cont_l"><span class="tlt"><spring:message code="item.poll.question.item"/> : </span><span><c:out value="${dt.ITEM_CONTENTS}"/></span></dd>
						</c:if>
					</dl>
				</div>
			</div>
		</div>
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="60px" />
				<col width="100px" />
				<col />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col"><spring:message code="item.poll.joiner"/></th>
				<th scope="col"><spring:message code="item.poll.opn.name"/></th>
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="3" class="bllist"><spring:message code="message.poll.etc.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<tr>
					<td class="num">${listNo}</td>
					<td><c:out value="${elfn:memberItemOrgValue('mbrName',listDt.REGI_NAME)}"/><c:if test="${!empty listDt.REGI_ID}"><br/>(<c:out value="${elfn:memberItemOrgValue('mbrId', listDt.REGI_ID)}"/>)</c:if></td>
					<td class="tlt"><c:out value="${listDt.CONTENTS}"/></td>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</tbody>
		</table>
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'cpage')}" usePaging="${settingInfo.use_paging}"/>
		</div>
		<!-- //paging -->
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>