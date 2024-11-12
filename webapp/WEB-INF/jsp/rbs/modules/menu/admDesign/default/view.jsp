<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="settingFormId" value="fn_viewWrap"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
		<jsp:param name="settingFormId" value="${settingFormId}"/>
	</jsp:include>
</c:if>
<div id="cms_menu_article">
	<div id="lnbWrap" style="width:230px;">
		<div class="infoWrap">
			<div class="info">
				<h4>메뉴</h4>
				<div style="position:absolute;top:0px;right:0px;line-height:27px;">
				<input type="checkbox" id="fn_btn_menu_tree_open" name="fn_btn_menu_tree_open"/><label for="fn_btn_menu_tree_open">전체열기</label>
				</div>
				<mnui:iaMenuLnb ulId="fn_iaMenuUL" ulClass="lnb" menuList="${usrSiteMenuList}" menus="${usrSiteMenus}"/>
			</div>
		</div>
	</div>
	<div class="inWfullContWrap">
		<div style="clear:both;">
		<table class="tbViewA" summary="상세보기 서식" id="fn_viewWrap">
			<caption>
			상세보기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<c:set var="itemId" value="menuIdx"/>
				<tr class="fn_item_${itemId}">
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="menuName"/>
				<tr class="fn_item_${itemId}">
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<tr>
					<th>구분자</th>
					<td>
						<dl class="fn_inlineBlock fn_short">
							<c:forEach var="itemIdx" begin="1" end="5">
							<c:set var="itemId" value="flag${itemIdx}"/>
							<dt class="fn_item_${itemId}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" class="fn_item_${itemId}">&nbsp;</dd>
							</c:forEach>
						</dl>
					</td>
				</tr>
				<c:set var="itemId" value="moduleId"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock fn_short">
							<dt class="hidden fn_item_${itemId}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" class="fn_item_${itemId}">&nbsp;</dd>
							<c:set var="itemId" value="fnIdx"/>
							<dt class="fn_item_${itemId}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" class="fn_item_${itemId}">&nbsp;</dd>
							<c:set var="itemId" value="contentsCode"/>
							<dt class="fn_item_${itemId}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" class="fn_item_${itemId}">&nbsp;</dd>
							<c:set var="itemId" value="branchIdx"/>
							<dt class="fn_item_${itemId}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" class="fn_item_${itemId}">&nbsp;</dd>
						</dl>
					</td>
				</tr>
				<c:set var="itemId" value="moduleAuth"/>
				<tr class="fn_item_${itemId}">
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="urDesign"/>
				<tr class="fn_item_${itemId}">
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<tr id="fn_fnSettingDl" class="hidden">
					<th scope="row"><spring:message code="item.menu.module.setting"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="fnUsePrivate"/>
							<dt class="fn_use_private hidden"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd class="fn_use_private hidden" id="fn_item_${itemId}" data-type="14"></dd>
							<c:set var="itemId" value="fnDsetListBlock"/>
							<dt class="fn_dset_list_block hidden"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd class="fn_dset_list_block hidden" id="fn_item_${itemId}"></dd>
							<c:set var="itemId" value="fnDsetListCount"/>
							<dt class="fn_dset_list_count hidden"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd class="fn_dset_list_count hidden" id="fn_item_${itemId}"></dd>
							<c:set var="itemId" value="fnDsetMemoListBlock"/>
							<dt class="fn_dset_memo_list_block hidden"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd class="fn_dset_memo_list_block hidden" id="fn_item_${itemId}"></dd>
							<c:set var="itemId" value="fnDsetMemoListCount"/>
							<dt class="fn_dset_memo_list_count hidden"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd class="fn_dset_memo_list_count hidden" id="fn_item_${itemId}"></dd>
							<c:set var="itemId" value="fnDsetApplListBlock"/>
							<dt class="fn_dset_app_list_block hidden"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd class="fn_dset_app_list_block hidden" id="fn_item_${itemId}"></dd>
							<c:set var="itemId" value="fnDsetApplListCount"/>
							<dt class="fn_dset_app_list_count hidden"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd class="fn_dset_app_list_count hidden" id="fn_item_${itemId}"></dd>
						</dl>
					</td>
				</tr>
				<tr id="fn_memberDl" class="hidden">
					<th scope="row"><spring:message code="item.menu.member.setting"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="perinfoPolicyLink"/>
							<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}"></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="item.menu.useatt.name"/></th>
					<td>
						<dl class="fn_inlineBlock fn_short">
							<c:set var="itemId" value="ishidden"/>
							<dt class="fn_item_${itemId}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" class="fn_item_${itemId}" data-type="14"></dd>
							<c:set var="itemId" value="childHidden"/>
							<dt class="fn_item_${itemId}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" class="fn_item_${itemId}" data-type="14"></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<c:set var="itemId" value="menuLink"/>
					<th scope="row"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock fn_short">
							<dt class="hidden"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}">
							</dd>
							<c:set var="itemId" value="anMenuLink"/>
							<dt class="fn_item_${itemId}"><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" class="fn_item_${itemId}">
							</dd>
						</dl>
					</td>
				</tr>
				<c:set var="itemId" value="menuComment"/>
				<tr class="fn_item_${itemId}">
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="includeTop"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="includeBottom"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="sourceTop"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="sourceBottom"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="item.menu.auth.name"/></th>
					<td class="fn_auth">
						<dl class="fn_inlineBlockDl">
							<c:set var="itemId" value="usertypeIdx"/>
							<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd>
								<span id="fn_item_${itemId}" data-type="2" data-default="비회원"></span>
							</dd>
						</dl>
						<dl class="fn_inlineBlockDl">
							<c:set var="itemId" value="groupIdxs"/>
							<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" data-type="11">
							</dd>
						</dl>
						<dl class="fn_inlineBlockDl">
							<c:set var="itemId" value="departIdxs"/>
							<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" data-type="11">
							</dd>
						</dl>
						<dl class="fn_inlineBlockDl">
							<c:set var="itemId" value="memberIdxs"/>
							<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" data-type="11">
							</dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><spring:message code="item.menu.manager.auth.name"/></th>
					<td class="fn_auth">
						<dl class="fn_inlineBlockDl">
							<c:set var="itemId" value="managerUsertypeIdx"/>
							<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd>
								<span id="fn_item_${itemId}" data-type="2" data-default="비회원"></span>
							</dd>
						</dl>
						<dl class="fn_inlineBlockDl">
							<c:set var="itemId" value="managerGroupIdxs"/>
							<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" data-type="11">
							</dd>
						</dl>
						<dl class="fn_inlineBlockDl">
							<c:set var="itemId" value="managerDepartIdxs"/>
							<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" data-type="11">
							</dd>
						</dl>
						<dl class="fn_inlineBlockDl">
							<c:set var="itemId" value="managerMemberIdxs"/>
							<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd id="fn_item_${itemId}" data-type="11">
							</dd>
						</dl>
					</td>
				</tr>
				<c:set var="itemId" value="usePoll"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}" data-type="14"></td>
				</tr>
				<c:set var="itemId" value="useDmng"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}" data-type="14"></td>
				</tr>
				<c:set var="itemId" value="dmngDepart"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="dmngName"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="dmngPhone"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="dmngLdate"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
				<c:set var="itemId" value="useTotSearch"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}" data-type="14"></td>
				</tr>
				<c:set var="itemId" value="remarks"/>
				<tr>
					<th><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td id="fn_item_${itemId}"></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>