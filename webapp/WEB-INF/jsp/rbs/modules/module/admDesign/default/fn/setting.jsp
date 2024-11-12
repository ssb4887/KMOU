<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_moduleFnSetInputForm"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${setSettingInfo.list_title} (${queryString.tit})"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/setting.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<spring:message var="itemUseName" code="item.use.name"/>
	<div id="cms_board_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
		<table class="tbWriteA" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:170px;" />
			</colgroup>
			<tbody>
				<tr>
					<th scope="row">No</th>
					<td><c:out value="${dt.FN_IDX}"/></td>
				</tr>
				<tr>
					<itui:itemView itemId="fnName" itemInfo="${itemInfo}"/>
				</tr>
				<c:set var="itemId" value="design_type"/>
				<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
				<tr>
					<itui:itemSelect itemId="${itemId}" name="${itemId}" itemInfo="${settingItemInfo}" optnHashMap="${settingOptnHashMap}" objStyle="min-width:200px;"/>
				</tr>
				</c:if>
				<c:set var="itemId" value="design"/>
				<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
				<tr>
					<itui:itemTextButton itemId="${itemId}" itemInfo="${settingItemInfo}" objStyle="min-width:200px;"/>
				</tr>
				</c:if>
				<c:set var="itemId" value="dset_input_btn"/>
				<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
				<tr>
					<itui:itemSelect itemId="${itemId}" name="${itemId}" itemInfo="${settingItemInfo}" optnHashMap="${settingOptnHashMap}" objStyle="min-width:200px;"/>
				</tr>
				</c:if>
				<c:set var="itemId" value="dset_input_npname"/>
				<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
				<tr>
					<itui:itemSelect itemId="${itemId}" name="${itemId}" itemInfo="${settingItemInfo}" optnHashMap="${settingOptnHashMap}" objStyle="min-width:200px;"/>
				</tr>
				</c:if>
				<c:set var="itemId" value="dset_cate_tab_id"/>
				<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${settingItemInfo}"/></th>
					<td>
						<itui:objectSelect itemId="${itemId}" name="${itemId}" itemInfo="${settingItemInfo}" optnHashMap="${settingOptnHashMap}" objStyle="min-width:200px;"/>
						&nbsp;
						<c:set var="itemId" value="use_cate_tab_total"/>
						<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
						<div id="fn_${itemId}" class="fn_inlineBlock">
						<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${settingItemInfo}" optnCode="1" optnName="${elfn:getItemName(settingItemInfo.items[itemId])}"/>
						</div>
						</c:if>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="dset_cate_list_id"/>
				<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
				<tr>
					<itui:itemSelect itemId="${itemId}" name="${itemId}" itemInfo="${settingItemInfo}" optnHashMap="${settingOptnHashMap}" objStyle="min-width:200px;"/>
				</tr>
				</c:if>
				<c:set var="itemId" value="use_multi_cate"/>
				<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${settingItemInfo}"/></th>
					<td>
						<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${settingItemInfo}" optnCode="1" optnName="${itemUseName}"/>
						<c:set var="itemId" value="dset_cate_master_code"/>
						<dl class="fn_inlineBlock">
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${settingItemInfo}"/> :</dt>
							<dd><itui:objectSelect itemId="${itemId}" name="${itemId}" itemInfo="${settingItemInfo}" optnHashMap="${settingOptnHashMap}" objStyle="min-width:200px;"/></dd>
						</dl>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="use_scurity"/>
				<c:set var="itemId_scs" value="${fn:split('use_ssl,use_rsa', ',')}"/>
				<c:if test="${!elfn:isItemClosedArrs(itemId_scs, settingItemInfo[itemOrderName], settingItemInfo.items)}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${settingItemInfo}"/></th>
					<td>
						<c:forEach var="itemId" items="${itemId_scs}">
						<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
						<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${settingItemInfo}" objDt="${dt}" optnCode="1" optnName="${elfn:getItemName(settingItemInfo.items[itemId])}"/>
						</c:if>
						</c:forEach>
					</td>
				</tr>
				</c:if>
				<spring:message var="smsUse" code="Globals.sms.use"/>
				<c:set var="itemId" value="use_etc_setting"/>
				<c:if test="${!elfn:isItemClosed(settingItemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${settingItemInfo}"/></th>
					<td>
						<ul class="fn_inlineBlock">
						<c:forEach var="itemId" items="${settingItemInfo['use_item_order']}">
							<c:if test="${smsUse == '1' && itemId == 'use_qna'}"><c:set var="displaySMS" value="${true}"/></c:if>
							<li><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${settingItemInfo}" objDt="${dt}" optnCode="1" optnName="${elfn:getItemName(settingItemInfo.items[itemId])}"/></li>
						</c:forEach>
						</ul>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="use_sms"/>
				<c:if test="${!elfn:isItemClosedArr(settingItemInfo[itemOrderName], settingItemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${settingItemInfo}"/></th>
					<td>
						<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${settingItemInfo}" optnCode="1" optnName="${itemUseName}"/>
						<c:if test="${displaySMS}"><% /* 알림수신 - sms 연동하는 경우 */ %>
						<c:set var="itemId" value="dset_sms_mgphone"/>
						<dl class="fn_inlineBlock">
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${settingItemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${settingItemInfo}" objStyle="width:200px;"/></dd>
						</dl>
						</c:if>
						<c:set var="itemId" value="dset_sms_mgemail"/>
						<dl class="fn_inlineBlock">
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${settingItemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${settingItemInfo}" objStyle="width:200px;"/></dd>
						</dl>
					</td>
				</tr>
				</c:if>
				<c:forEach var="itemId" items="${settingItemInfo['dset_item_order']}">
				<tr>
					<itui:itemText itemId="${itemId}" itemInfo="${settingItemInfo}" objStyle="width:200px;"/>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>