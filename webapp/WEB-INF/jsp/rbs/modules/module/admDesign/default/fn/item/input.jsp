<%@page import="java.util.Calendar"%>
<%@ include file="../../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_ItemInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${itemSettingInfo.input_title} (${queryString.mt})"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/item/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
		<table class="tbWriteA" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:120px;" />
			<col />
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<%/* item_id, column_id */ %>
				<tr>
					<itui:itemText itemId="item_id" itemInfo="${itemInfo}" objStyle="width:200px;" colspan="3"/>
				</tr>
				<tr>
					<itui:itemText itemId="column_id" itemInfo="${itemInfo}" objStyle="width:200px;" colspan="3"/>
				</tr>
				<%/* item_name */ %>
				<tr>
					<itui:itemText itemId="item_name" itemInfo="${itemInfo}" itemLangList="${langList}" objStyle="width:150px;" colspan="3"/>
				</tr>
				<%/* format_type, object_type */ %>
				<tr>
					<th scope="row"><itui:objectLabel itemId="format_type" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectSelect itemId="format_type" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" objStyle="min-width:200px;"/>
					</td>
					<th scope="row"><itui:objectLabel itemId="object_type" itemInfo="${itemInfo}"/></th>
					<td>
					<c:if test="${empty dt.FORMAT_TYPE || dt.FORMAT_TYPE == 0}">
						<itui:objectSelect itemId="object_type" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" objStyle="min-width:200px;"/>
					</c:if>
					</td>
				</tr>
				<%/* format_type 연관항목 */ %>
				<tr id="fn_w_format_type" class="fn_set_item hidden">
					<th scope="row"><spring:message code="item.add.setting.name" arguments="${elfn:getItemName(itemInfo.items['format_type'])}"/></th>
					<td colspan="3">
						<%/* use_privsec : 암/복호화 */ %>
						<div class="fn_w_format_1 fn_w_format_2 fn_w_format_3 fn_w_format_4 fn_w_format_10 fn_inlineBlock hidden" style="margin-left:5px;">
							<c:set var="itemId" value="use_privsec"/>
							<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/>
						</div>
						<%/* 년도 설정 */ %>
						<%
							Calendar calendar = Calendar.getInstance(); 
							request.setAttribute("dSYearVal", calendar.get(Calendar.YEAR)); 
						%>
						<dl class="fn_w_format_19 fn_w_format_20 fn_w_format_21 fn_w_format_22 fn_w_format_29 fn_w_format_30 hidden fn_inlineBlock">
							<c:set var="itemId" value="start_year"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}" required="1"/></dt>
							<dd style="width:100px;"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" defVal="${dSYearVal}" objStyle="width:70px;"/></dd>
							<c:set var="itemId" value="end_addcnt"/>
							<dt style="margin-left:30px;width:100px;"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}" required="1"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:70px;"/></dd>
							<c:if test="${!empty itemInfo.items[itemId]['comment']}"><dd class="comment" style="margin-left:10px;"><c:out value="${itemInfo.items[itemId]['comment']}"/></dd></c:if>
						</dl>
						<%/* 분기 구분 */ %>
						<c:set var="itemId" value="master_code"/>
						<dl class="fn_w_format_21 hidden fn_inlineBlock">
							<dt><label for="${itemId}" class="required"><spring:message code="item.items.quarterly.type.name"/></label></dt>
							<dd><itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" objStyle="min-width:200px;"/></dd>
						</dl>
						<%/* 현재날짜 기본값 */ %>
						<c:set var="itemId" value="default_current"/>
						<dl class="fn_w_format_19 fn_w_format_20 fn_w_format_21 fn_w_format_22 fn_w_format_29 fn_w_format_30 hidden fn_inlineBlock">
							<dt><label for="${itemId}"><spring:message code="item.default_value.name"/></label></dt>
							<dd><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/></dd>
						</dl>
					</td>
				</tr>
				<%/* object_type 연관항목 */ %>
				<tr id="fn_w_object_type" class="fn_set_item hidden">
					<th scope="row"><spring:message code="item.add.setting.name" arguments="${elfn:getItemName(itemInfo.items['object_type'])}"/></th>
					<td colspan="3">
						<%/* 이미지사용여부 */ %>
						<c:set var="itemId" value="isimage"/>
						<div class="fn_w_object_6 fn_w_object_9" class="_hidden fn_inlineBlock">
							<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/>
						</div>
						<%/* 에디터 */ %>
						<dl class="fn_w_object_1" class="_hidden fn_inlineBlock">
							<c:set var="itemId" value="editor"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" objStyle="min-width:200px;"/></dd>
							<c:set var="itemId" value="editor_utype"/>
							<dt style="margin-left:10px;"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" objStyle="min-width:200px;"/></dd>
							<c:if test="${!empty itemInfo.items[itemId]['comment']}"><dd class="comment"><c:out value="${itemInfo.items[itemId]['comment']}"/></dd></c:if>
						</dl>
						<%/* 분류 */ %>
						<c:set var="itemId" value="master_code"/>
						<dl class="fn_w_object_2 fn_w_object_3 fn_w_object_4 fn_w_object_5 fn_w_object_14 hidden fn_inlineBlock">
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" objStyle="min-width:200px;"/></dd>
						</dl>
						<%/* 3차분류 */ %>
						<c:set var="itemId" value="class_master_code"/>
						<dl class="fn_w_object_22 hidden fn_inlineBlock">
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" objStyle="min-width:200px;"/></dd>
						</dl>
						<%/* 기본값 */ %>
						<c:set var="itemId" value="default_value"/>
						<dl class="fn_r_object fn_r_object_6 fn_r_object_9 hidden fn_inlineBlock">
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:190px;"/></dd>
						</dl>
						<%/* 입력값종류 */ %>
						<c:set var="itemId" value="item_type"/>
						<dl class="fn_w_object_0 fn_w_object_1 fn_w_object_101 hidden fn_inlineBlock">
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectCheckbox itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}"/></dd>
						</dl>
						<%/* 입력제한 */ %>
						<c:set var="itemId" value="use_file_ext"/>
						<dl class="fn_w_object_0 fn_w_object_1 fn_w_object_101 fn_w_object_6 fn_w_object_9 hidden fn_inlineBlock">
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd>
								<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" objStyle="min-width:100px;"/>
								<c:set var="itemId" value="dset_file_ext"/>
								<itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:350px;"/>
								<c:if test="${!empty itemInfo.items[itemId]['comment']}"><span class="comment"><c:out value="${itemInfo.items[itemId]['comment']}"/></span></c:if>
							</dd>
						</dl>
						<%/* 썸네일 이미지크기 */ %>
						<dl class="fn_w_isimage_1 hidden fn_inlineBlock">
							<c:set var="itemId" value="dset_simg_width"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd style="width:120px;">
								<itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:70px;"/>
								<c:if test="${!empty itemInfo.items[itemId]['comment']}"><span class="comment"><c:out value="${itemInfo.items[itemId]['comment']}"/></span></c:if>
							</dd>
							<c:set var="itemId" value="dset_simg_height"/>
							<dt style="margin-left:30px;"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd>
								<itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:70px;"/>
								<c:if test="${!empty itemInfo.items[itemId]['comment']}"><span class="comment"><c:out value="${itemInfo.items[itemId]['comment']}"/></span></c:if>
							</dd>
						</dl>
						<%/* 최소/최대값 */ %>
						<dl class="fn_w_object_0 fn_w_object_1 fn_w_object_101 fn_w_object_9 hidden fn_inlineBlock">
							<c:set var="itemId" value="minimum"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd style="width:120px;">
								<itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:70px;"/>
								<c:if test="${!empty itemInfo.items[itemId]['comment']}"><span class="comment"><c:out value="${itemInfo.items[itemId]['comment']}"/></span></c:if>
							</dd>
							<c:set var="itemId" value="maximum"/>
							<dt style="margin-left:30px;"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd>
								<itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:70px;"/>
							</dd>
							<c:if test="${!empty itemInfo.items[itemId]['comment']}"><dd class="comment"><c:out value="${itemInfo.items[itemId]['comment']}"/></dd></c:if>
						</dl>
						<div class="fn_w_object_101 comment hidden"><spring:message code="errors.rsa.limit.length" arguments="117,39" argumentSeparator=","/></div>
						<%/* text+button 연관항목 */ %>
						<dl class="fn_w_object_8 hidden">
							<%/* %>
							<c:set var="itemId" value="name_column_id"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:150px;"/></dd>
							<c:if test="${!empty itemInfo.items[itemId]['comment']}"><dd class="comment" style="margin-left:10px;width:480px;"><c:out value="${itemInfo.items[itemId]['comment']}"/></dd></c:if>
							<%*/ %>
							<c:set var="itemId" value="button_name"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd style="width:600px;"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:150px;"/></dd>
							<%/* %>
							<c:set var="itemId" value="search_url"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd style="width:600px;"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:350px;"/></dd>
							<%*/ %>
							<c:set var="itemId" value="onclick"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:350px;"/></dd>
						</dl>
					</td>
				</tr>
				<%/* object style */ %>
				<c:set var="itemId" value="object_style"/>
				<tr>
					<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:450px;" colspan="3"/>
				</tr>
				<%/* 필수여부 */ %>
				<tr>
					<th><spring:message code="item.items.set.required.name"/></th>
					<td colspan="3">
						<ul class="fn_inlineBlock">
							<li>
								<c:set var="itemId" value="required_write"/>
								<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/>
							</li>
							<li>
								<c:set var="itemId" value="required_modify"/>
								<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/>
							</li>
						</ul>
					</td>
				</tr>
				<%/* 사용페이지 */ %>
				<tr>
					<th><label for="use_list" class="required"><spring:message code="item.items.set.use.page.name"/></label></th>
					<td colspan="3">
						<ul id="fn_usePage" class="fn_inlineBlock">
							<li>
								<c:set var="itemId" value="use_list"/>
								<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/>
							</li>
							<li>
								<c:set var="itemId" value="use_view"/>
								<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/>
							</li>
							<li>
								<c:set var="itemId" value="use_write"/>
								<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/>
							</li>
							<li>
								<c:set var="itemId" value="use_modify"/>
								<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/>
							</li>
							<li>
								<c:set var="itemId" value="use_reply"/>
								<itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="${elfn:getItemName(itemInfo.items[itemId])}"/>
							</li>
						</ul>
					</td>
				</tr>
				<%/* 등록/수정유형 */ %>
				<c:set var="itemId" value="write_type"/>
				<tr>
					<itui:itemSelect itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" colspan="3" objStyle="min-width:200px;"/>
				</tr>
				<c:set var="itemId" value="modify_type"/>
				<tr>
					<itui:itemSelect itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" colspan="3" objStyle="min-width:200px;"/>
				</tr>
				<%/* 순서 */ %>
				<c:set var="itemId" value="order_idx"/>
				<tr>
					<itui:itemSelect itemId="${itemId}" itemInfo="${itemInfo}" optnHashMap="${optnHashMap}" idxColumnId="${itemSettingInfo.idx_column}" addOrder="${true}" colspan="3" objStyle="min-width:200px;"/>
				</tr>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>