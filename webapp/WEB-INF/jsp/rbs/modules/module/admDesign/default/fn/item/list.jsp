<%@ include file="../../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="searchFormId" value="fn_fnItemSearchForm"/>
<c:set var="listFormId" value="fn_fnItemListForm"/>
<c:set var="inputFormId" value="fn_ItemInputForm"/>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${itemSettingInfo.list_title} (${queryString.mt})"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/item/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<!-- button -->
		<div class="btnTopFull">
			<c:set var="deleteList_dialog_title" value="${queryString.tit}"/>
			<%@ include file="listBtns.jsp"%>
		</div>
		<!-- //button -->
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="tbListA" summary="<c:out value="${itemSettingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
			<caption><c:out value="${itemSettingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="50px" />
				<col width="60px" />
				<col width="70px" />
				<col width="70px" />
				<col width="100px" />
				<col />
				<col width="90px" />
				<col width="100px" />
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
				<th scope="col">번호</th>
				<th scope="col">수정</th>
				<th scope="col"><itui:objectItemName itemId="order_idx" itemInfo="${itemInfo}"/></th>
				<th scope="col"><itui:objectItemName itemId="item_id" itemInfo="${itemInfo}"/></th>
				<th scope="col"><itui:objectItemName itemId="item_name" itemInfo="${itemInfo}"/></th>
				<th scope="col"><itui:objectItemName itemId="required_write" itemInfo="${itemInfo}"/></th>
				<th scope="col"><itui:objectItemName itemId="object_type" itemInfo="${itemInfo}"/></th>
				<th scope="col"><itui:objectItemName itemId="format_type" itemInfo="${itemInfo}"/></th>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="10" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<spring:message var="msgBtnUpName" code="button.up"/>
				<spring:message var="msgBtnDownName" code="button.down"/>
				<spring:message var="msgItemRequiredName" code="item.required.name"/>
				<c:set var="listIdxName" value="${itemSettingInfo.idx_name}"/>
				<c:set var="listIdxColumn" value="${itemSettingInfo.idx_column}"/>
				<c:set var="listNo" value="${fn:length(list)}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
				<tr>
					<td><c:if test="${listDt.DEFAULT_ITEM != '1'}"><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></c:if></td>
					<td class="num"><c:out value="${listNo}"/></td>
					<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF ${btnModifyClass}">수정</a></c:if></td>
					<td>
						<ul class="fn_order">
							<li><c:if test="${!i.first}"><button type="button" class="fn_btn_up" title="${msgBtnUpName}" data-id="<c:out value="${listDt.ITEM_ID}"/>">${msgBtnUpName}</button></c:if></li>
							<li><c:if test="${!i.last}"><button type="button" class="fn_btn_down" title="${msgBtnDownName}" data-id="<c:out value="${listDt.ITEM_ID}"/>">${msgBtnDownName}</button></c:if></li>
						</ul>
					</td>
					<td><c:out value="${listDt.ITEM_ID}"/></td>
					<td class="tlt"><c:out value="${listDt.ITEM_NAME}"/></td>
					<td><c:if test="${listDt.REQUIRED_WRITE == '1'}"><c:out value="${msgItemRequiredName}"/></c:if></td>
					<td><c:if test="${empty listDt.FORMAT_TYPE || listDt.FORMAT_TYPE == 0}"><itui:objectView itemId="object_type" objDt="${listDt}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}"/></c:if></td>
					<td><itui:objectView itemId="format_type" objDt="${listDt}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}"/></td>
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</tbody>
		</table>
		</form>
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_ORDERPROC}"/>" target="submit_target">
			<input type="hidden" id="orderIdx" name="orderIdx"/>
			<input type="hidden" id="itemId" name="itemId"/>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>