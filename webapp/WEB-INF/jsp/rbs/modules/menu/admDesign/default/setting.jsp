<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="inputFormId" value="fn_menuInputForm"/>
<c:set var="settingFormId" value="fn_menuSettingForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/setting.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
		<jsp:param name="settingFormId" value="${settingFormId}"/>
	</jsp:include>
</c:if>
<div id="cms_menu_article">
	<div class="btnTopFull">
		<div class="left">
			<div style="display:inline-block;vertical-align:top;">
			<form id="fn_searchSVerForm" name="fn_searchSVerForm" method="get" action="<c:out value="${URL_INPUT}"/>">
				<elui:hiddenInput inputInfo="${queryString}" exceptNames="${fn:split('verIdx',',')}"/>
				<fieldset>
					<legend class="blind">적용 버전검색 폼</legend>
					<label class="tit blind" for="verIdx">적용 버전검색</label>
					<select id="verIdx" name="verIdx" class="select" title="적용 버전">
						<c:forEach var="verDt" items="${usrSiteVerList}">
						<option value="<c:out value="${verDt.OPTION_CODE}"/>"<c:if test="${elfn:toString(verDt.OPTION_CODE) == queryString.verIdx}"> selected="selected"</c:if>><c:out value="${verDt.OPTION_NAME}"/><c:if test="${verDt.ISSERVICE == '1'}">[<spring:message code="message.version.isservice"/>]</c:if></option>
						</c:forEach>
					</select>
					<button type="submit" class="btnTFDL" id="fn_btn_search_sversion">선택</button>
				</fieldset>
			</form>
			</div>
			<div style="display:inline-block;vertical-align:top;">
				<span style="margin:0 5px;">버전 : <c:out value="${queryString.verIdx}"/></span>
				<c:if test="${mngAuth}">
				<a href="<c:out value="${URL_APPLYPROC}"/>" class="btnTFEW" id="fn_btn_apply_sversion" target="submit_target">적용</a>
				<a href="<c:out value="${URL_VERLIST}"/>" class="btnTFW" id="fn_btn_version" title="버전관리 새창">버전관리</a>
				</c:if>
			</div>
		</div>
		<div class="right">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_INPUTPROC}"/>" target="submit_target">
			<input type="hidden" id="top_ordIdx" name="ordIdx" />
			<input type="hidden" id="top_menuLevel" name="menuLevel" />
			<fieldset>
				<legend class="blind">메뉴추가 폼</legend>
				<label for="menuPath"><spring:message code="item.menu.position.name"/>:</label>
				<input type="text" id="menuPath" name="menuPath" title="메뉴위치" readonly="readonly" class="inputTxt fn_readonly" style="width:470px;"/>
				
				<itui:objectRadio itemId="ordType" id="top_ordType" itemInfo="${itemInfo}"/>
				<input type="text" id="top_menuName" name="menuName" required="required" title="메뉴명" class="inputTxt" style="width:150px;"/>
				<input type="submit" class="btnTFW" value="저장" title="저장"/>
			</fieldset>
		</form>
		</div>
	</div>
	<div style="clear:both;border-top: 1px solid #eee;">
		<div id="lnbWrap">
			<div class="infoWrap">
				<div class="info">
					<h4>메뉴</h4>
					<form id="fn_searchSVerForm" name="fn_searchSVerForm" method="post" action="">
						<fieldset>
						<legend class="blind">버전검색 폼</legend>
							<input type="checkbox" id="fn_btn_menu_tree_open" name="fn_btn_menu_tree_open"/><label for="fn_btn_menu_tree_open">전체열기</label>
							<span id="fn_menu_select_all" class="fn_skip"><input type="checkbox" id="fn_btn_menu_select_all"><label for="fn_btn_menu_select_all">전체선택</label></span>
						</fieldset>
					</form>
					<form name="fn_dmngForm" id="fn_dmngForm" method="post" target="submit_target" action="dmngInputProc.do?mId=<c:out value="${queryString.mId}"/>&verIdx=<c:out value="${queryString.verIdx}"/>">
					<mnui:iaMenuLnb ulId="fn_iaMenuUL" ulClass="lnb" menuList="${usrSiteMenuList}" menus="${usrSiteMenus}"/>
					<div id="infoMng" class="fn_skip fn_dialog">
						<div class="header">
							<h4>${crtMenu.menu_name} 담당자 정보</h4>
							<button type="button" data-target="#infoMng" title="<spring:message var="btn_close" code="button.close"/>" class="btn_close"><c:out value="${btn_close}"/></button>
						</div>
						<fieldset>
							<legend>${crtMenu.menu_name} 담당자 정보 입력 양식</legend>
							<div class="fn_dialog_content">
							<div class="comment">메뉴 선택 후 ${crtMenu.menu_name} 담당자 정보를 입력하셔야 합니다.</div>
							<table class="tbWriteA" summary="메뉴 관리 담당자 정보 입력 서식">
								<caption>
								${crtMenu.menu_name} 담당자 정보 입력 서식
								</caption>
								<colgroup>
								<col style="width:120px;" />
								<col />
								</colgroup>
								<tbody>
									<c:set var="itemId" value="useDmng"/>
									<tr>
										<itui:itemCheckboxSG itemId="${itemId}" id="all_${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="사용"/>
									</tr>
									<c:set var="itemId" value="dmngDepart"/>
									<tr>
										<itui:itemText itemId="${itemId}" id="all_${itemId}" itemInfo="${itemInfo}"  objStyle="width:150px;"/>
									</tr>
									<c:set var="itemId" value="dmngName"/>
									<tr>
										<itui:itemText itemId="${itemId}" id="all_${itemId}" itemInfo="${itemInfo}" objStyle="width:150px;"/>
									</tr>
									<c:set var="itemId" value="dmngPhone"/>
									<tr>
										<itui:itemText itemId="${itemId}" id="all_${itemId}" itemInfo="${itemInfo}" objStyle="width:150px;"/>
									</tr>
									<c:set var="itemId" value="dmngLdate"/>
									<tr>
										<itui:itemText itemId="${itemId}" id="all_${itemId}" itemInfo="${itemInfo}" objStyle="width:150px;"/>
									</tr>
								</tbody>
							</table>
							<div class="btnCenter">
								<spring:message var="btn_save" code="button.save"/><input type="submit" class="btnTypeA fn_btn_submit" title="<c:out value="${btn_save}"/>" value="<c:out value="${btn_save}"/>" />
								<spring:message var="btn_cancel" code="button.cancel"/><input type="button" class="btnTypeB fn_btn_cancel" title="<c:out value="${btn_cancel}"/>" value="<c:out value="${btn_cancel}"/>"/>
							</div>
							</div>
						</fieldset>
					</div>
					</form>
				</div>
			</div>
		</div>
		<div class="inWfullContWrap" style="margin-top:5px;">
		
			<div class="btnTopFull">
				<div class="left">
					<c:if test="${mngAuth}">
					<input type="button" class="btnTFD" value="<spring:message code="button.managerinfo"/>" title="<spring:message code="button.title.managerinfo"/>" id="bt_open_mng" data-target="#infoMng"/>
					</c:if>
					<a href="<c:out value="${URL_EXCELDOWN}"/>" class="btnTFDL fn_btn_exceldown" title="엑셀다운로드">엑셀다운로드</a>
					<a href="<c:out value="${URL_VIEW}"/>" class="btnTFEW fn_btn_view" title="적용메뉴보기 새창">적용메뉴보기</a>
				</div>
				<div class="right">
					<c:if test="${mngAuth}">
					<a href="<c:out value="${URL_DELETEPROC}"/>" target="submit_target" title="삭제" class="btnTD fn_btn_delete">삭제</a>
					<a href="<c:out value="${URL_DELETE_LIST}"/>" title="휴지통" class="btnTDL fn_btn_deleteList">휴지통</a>
					</c:if>
					<input type="button" class="btnTFEW" id="fn_btn_preview" value="미리보기" title="미리보기"/>
				</div>
			</div>
			<div style="clear:both;">
			<form id="${settingFormId}" name="${settingFormId}" method="post" action="<c:out value="${URL_MODIFYPROC}"/>" target="submit_target">
			<input type="hidden" id="parentMenuIdx" name="parentMenuIdx"/>
			<table class="tbWriteA" summary="글쓰기 서식">
				<caption>
				글쓰기 서식
				</caption>
				<colgroup>
				<col style="width:150px;" />
				<col />
				</colgroup>
				<tbody>
					<tr>
						<itui:itemText itemId="menuIdx" itemInfo="${itemInfo}"/>
					</tr>
					<tr>
						<itui:itemText itemId="menuName" itemInfo="${itemInfo}" objStyle="width:150px;"/>
					</tr>
					<c:set var="itemId" value="ordIdx"/>
					<tr>
						<th><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th>
						<td>
							<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:500px;"/>
							<div style="display:inline-block; margin-left:10px;">
							<itui:objectRadio itemId="ordType" itemInfo="${itemInfo}"/>
							</div>
						</td>
					</tr>
					<tr>
						<th><label for="flag1">구분자</label></th>
						<td>
							<dl class="fn_inlineBlock fn_short">
								<c:forEach var="itemIdx" begin="1" end="5">
								<c:set var="itemId" value="flag${itemIdx}"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:50px;"/></dd>
								</c:forEach>
							</dl>
						</td>
					</tr>
					<c:set var="itemId" value="moduleId"/>
					<tr>
						<th><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th>
						<td>
							<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" objStyle="min-width:215px;"/>
							<c:set var="itemId" value="confModule"/>
							<input type="hidden" id="${itemId}" name="${itemId}" title="<c:out value="${elfn:getItemName(itemInfo.items[itemId])}"/>"/>
							<c:set var="itemId" value="fnIdx"/>
							<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" objStyle="min-width:215px;"/>
							<c:set var="itemId" value="contentsCode"/>
							<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" objStyle="min-width:215px;"/>
							<c:set var="itemId" value="designType"/>
							<input type="hidden" id="${itemId}" name="${itemId}" title="<c:out value="${elfn:getItemName(itemInfo.items[itemId])}"/>"/>
							<c:set var="itemId" value="contentsType"/>
							<input type="hidden" id="${itemId}" name="${itemId}" title="<c:out value="${elfn:getItemName(itemInfo.items[itemId])}"/>"/>
							
							<c:set var="itemId" value="branchIdx"/>
							<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" objStyle="min-width:215px;"/>
							<c:set var="itemId" value="branchType"/>
							<input type="hidden" id="${itemId}" name="${itemId}" title="<c:out value="${elfn:getItemName(itemInfo.items[itemId])}"/>"/>
						</td>
					</tr>
					<c:set var="itemId" value="moduleAuth"/>
					<c:set var="requiredName" value="required_${submitType}"/>
					<c:set var="moduleAuthRequired" value="${itemInfo.items[itemId][requiredName]}"/>
					<tr id="fn_moduleAuth"<c:if test="${moduleAuthRequired == 1}">class="fn_requried_item"</c:if>>
						<itui:itemSelect itemId="moduleAuth" itemInfo="${itemInfo}" objStyle="min-width:215px;"/>
					</tr>
					<c:set var="itemId" value="urDesign"/>
					<tr>
						<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}" nextItemId="Name"/></th>
						<td>
							<itui:objectTextButton itemId="${itemId}" itemInfo="${itemInfo}" btnDelHidden="${true}"/>
							<c:set var="itemId" value="anDesign"/>
							<input type="hidden" id="${itemId}" name="${itemId}" title="<c:out value="${elfn:getItemName(itemInfo.items[itemId])}"/>"/>
						</td>
					</tr>
					<tr id="fn_fnSettingDl" class="hidden">
						<th scope="row"><spring:message code="item.menu.module.setting"/></th>
						<td>
							<div class="comment" style="display:block;">기능설정의 입력항목은 '0' 또는 입력하지 않는 경우 '기능관리 &gt; 기능등록관리의 설정관리' 정보가 적용됩니다.</div>
							<dl class="fn_inlineBlock">
								<c:set var="itemId" value="fnUsePrivate"/>
								<dt class="fn_use_private hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_use_private hidden"><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></dd>
								<%/* %>
								<c:set var="itemId" value="privateMenuLink"/>
								<dt class="fn_use_private hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_use_private hidden"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:480px;"/></dd>
								<%*/ %>
								<c:set var="itemId" value="fnDsetListBlock"/>
								<dt class="fn_dset_list_block hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_dset_list_block hidden"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:200px;"/></dd>
								<c:set var="itemId" value="fnDsetListCount"/>
								<dt class="fn_dset_list_count hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_dset_list_count hidden"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:200px;"/></dd>
								<c:set var="itemId" value="fnDsetMemoListBlock"/>
								<dt class="fn_dset_memo_list_block hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_dset_memo_list_block hidden"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:200px;"/></dd>
								<c:set var="itemId" value="fnDsetMemoListCount"/>
								<dt class="fn_dset_memo_list_count hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_dset_memo_list_count hidden"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:200px;"/></dd>
								<c:set var="itemId" value="fnDsetApplListBlock"/>
								<dt class="fn_dset_app_list_block hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_dset_app_list_block hidden"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:200px;"/></dd>
								<c:set var="itemId" value="fnDsetApplListCount"/>
								<dt class="fn_dset_app_list_count hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_dset_app_list_count hidden"><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:200px;"/></dd>
							</dl>
						</td>
					</tr>
					<tr id="fn_memberDl" class="hidden">
						<th scope="row"><spring:message code="item.menu.member.setting"/></th>
						<td>
							<dl class="fn_inlineBlock">
								<c:set var="itemId" value="perinfoPolicyLink"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:480px;"/></dd>
							</dl>
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="item.menu.useatt.name"/></th>
						<td>
							<dl class="fn_inlineBlock fn_short">
								<c:set var="itemId" value="anIshidden"/>
								<dt class="hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></dd>
								<c:set var="itemId" value="ishidden"/>
								<dt class="hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></dd>
								<c:set var="itemId" value="childHidden"/>
								<dt class="hidden"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></dd>
							</dl>
						</td>
					</tr>
					<tr>
						<c:set var="itemId" value="menuLink"/>
						<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th>
						<td>
							<dl class="fn_inlineBlock fn_short">
								<c:set var="itemId" value="menuLinkIdx"/>
								<dt class="fn_${itemId}" style="width:100px;"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_${itemId}" style="width:950px;">
									<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:540px;"/>
								</dd>
								<c:set var="itemId" value="menuLink"/>
								<dt class="fn_${itemId}" style="width:100px;"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd style="width:950px;">
									<itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:425px;"/>
									<c:set var="itemId" value="menuTarget"/>
									<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}"/>
									<c:set var="itemId" value="menuLink"/>
									<button type="button" id="fn_btn_${itemId}" class="btnTypeF"><spring:message code="item.menu.link.def.name"/></button>
									<!-- input type="hidden" id="menuLinkDef" name="menuLinkDef" / -->
								</dd>
								<c:set var="itemId" value="anMenuLink"/>
								<dt class="fn_${itemId}" style="width:100px;"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd class="fn_${itemId}">
									<itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:530px;"/>
									<!-- input type="hidden" id="anMenuLinkDef" name="anMenuLinkDef" / -->
								</dd>
							</dl>
						</td>
					</tr>
					<c:set var="itemId" value="menuComment"/>
					<tr>
						<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:550px;"/>
					</tr>
					<c:set var="itemId" value="includeTop"/>
					<tr>
						<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:550px;"/>
					</tr>
					<c:set var="itemId" value="includeBottom"/>
					<tr>
						<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:550px;"/>
					</tr>
					<c:set var="itemId" value="sourceTop"/>
					<tr>
						<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:550px;"/>
					</tr>
					<c:set var="itemId" value="sourceBottom"/>
					<tr>
						<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:550px;"/>
					</tr>
					<tr>
						<th scope="row"><spring:message code="item.menu.auth.name"/></th>
						<td>
							<dl class="fn_inlineBlockDl">
								<c:set var="itemId" value="usertypeIdx"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd>
									<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" objStyle="min-width:120px;"/>이상
								</dd>
							</dl>
							<dl class="fn_inlineBlockDl">
								<c:set var="itemId" value="groupIdxs"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd>
									<itui:objectMultiSelectButton itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:140px;height:55px;"/>
								</dd>
							</dl>
							<dl class="fn_inlineBlockDl">
								<c:set var="itemId" value="departIdxs"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd>
									<itui:objectMultiSelectButton itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:140px;height:55px;"/>
								</dd>
							</dl>
							<dl class="fn_inlineBlockDl">
								<c:set var="itemId" value="memberIdxs"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd>
									<itui:objectMultiSelectButton itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:140px;height:55px;"/>
								</dd>
							</dl>
						</td>
					</tr>
					<tr>
						<th scope="row"><spring:message code="item.menu.manager.auth.name"/></th>
						<td>
							<dl class="fn_inlineBlockDl">
								<c:set var="itemId" value="managerUsertypeIdx"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd>
									<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" objStyle="min-width:120px;"/>이상
								</dd>
							</dl>
							<dl class="fn_inlineBlockDl">
								<c:set var="itemId" value="managerGroupIdxs"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd>
									<itui:objectMultiSelectButton itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:140px;height:55px;"/>
								</dd>
							</dl>
							<dl class="fn_inlineBlockDl">
								<c:set var="itemId" value="managerDepartIdxs"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd>
									<itui:objectMultiSelectButton itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:140px;height:55px;"/>
								</dd>
							</dl>
							<dl class="fn_inlineBlockDl">
								<c:set var="itemId" value="managerMemberIdxs"/>
								<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
								<dd>
									<itui:objectMultiSelectButton itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:140px;height:55px;"/>
								</dd>
							</dl>
						</td>
					</tr>
					<c:set var="itemId" value="usePoll"/>
					<tr>
						<itui:itemCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="사용"/>
					</tr>
					<c:set var="itemId" value="useDmng"/>
					<tr>
						<itui:itemCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="사용"/>
					</tr>
					<c:set var="itemId" value="dmngDepart"/>
					<tr>
						<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:150px;"/>
					</tr>
					<c:set var="itemId" value="dmngName"/>
					<tr>
						<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:150px;"/>
					</tr>
					<c:set var="itemId" value="dmngPhone"/>
					<tr>
						<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:150px;"/>
					</tr>
					<c:set var="itemId" value="dmngLdate"/>
					<tr>
						<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:150px;"/>
					</tr>
					<c:set var="itemId" value="useTotSearch"/>
					<tr>
						<itui:itemCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="사용"/>
					</tr>
					<c:set var="itemId" value="remarks"/>
					<tr>
						<itui:itemTextarea itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:550px;height:70px;"/>
					</tr>
				</tbody>
			</table>
			<div class="btnCenter">
				<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
				<a href="#" title="취소" class="btnTypeB fn_btn_reset">취소</a>
			</div>
			</form>
		</div>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>