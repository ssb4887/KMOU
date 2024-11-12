<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_siteInputForm"/>								<%/* 입력폼 id/name */ %>
<spring:message var="useLayoutTmp" code="Globals.layoutTmp.use" text=""/>
<c:if test="${useLayoutTmp == '1'}">
	<spring:message var="layoutItemFlag" code="Globals.layoutTmp.item.flag" text=""/>
</c:if>
<c:set var="verItemOrderName" value="${layoutItemFlag}site_order"/>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<%/* 입력폼 id/name : javascript에서 사용 */ %>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>		
		<jsp:param name="inputFormId" value="${inputFormId}"/>						
	</jsp:include>
</c:if>
	<c:set var="verItemInfo" value="${moduleItem.version_item_info}"/>
	<div id="cms_website_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		
		<h4>기본정보</h4>
		<table class="tbWriteA" summary="기본정보 글쓰기 서식">
			<caption>
			기본정보 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemText itemId="site_id" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_name" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<c:if test="${empty param.mode}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="copy_site_id" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectSelect itemId="copy_site_id" itemInfo="${itemInfo}" objStyle="min-width:150px;"/>
					</td>
				</tr>
				</c:if>
				<tr>
					<itui:itemText itemId="site_domain" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemSelect itemId="site_type" itemInfo="${itemInfo}" objStyle="min-width:150px;"/>
				</tr>
				<tr>
					<itui:itemSelect itemId="locale_lang" itemInfo="${itemInfo}" objStyle="min-width:150px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_title" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="ssl_port" itemInfo="${itemInfo}" objStyle="width:140px;"/>
				</tr>
				<spring:message var="useIpRestrict" code="Globals.ip.restrict.use" text="0"/>
				<c:if test="${useIpRestrict == '1'}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="access_restrict" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectCheckboxSG itemId="access_restrict" itemInfo="${itemInfo}"/>
						<div>
							<itui:objectLabel itemId="restrict_ip" itemInfo="${itemInfo}"/>
							<itui:objectText itemId="restrict_ip" itemInfo="${itemInfo}" objStyle="width:500px;"/>
							<c:if test="${!empty itemInfo.items['restrict_ip']['comment']}"><span class="comment"><c:out value="${itemInfo.items['restrict_ip']['comment']}" escapeXml="false"/></span></c:if>
						</div>
					</td>
				</tr>
				</c:if>
				<spring:message var="msgInputNew" code="item.select.input.new"/>
				<tr>
					<itui:itemSelect itemId="banner_fn_idx" itemInfo="${itemInfo}" itemOptBlankTitle="${msgInputNew}" objStyle="min-width:150px;"/>
				</tr>
				<tr>
					<itui:itemSelect itemId="popup_fn_idx" itemInfo="${itemInfo}" itemOptBlankTitle="${msgInputNew}" objStyle="min-width:150px;"/>
				</tr>
				<c:set var="itemId" value="manager_member_idxs"/>
				<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
				<tr>
					<itui:itemMultiSelectButton itemId="${itemId}" objValArray="${fn:split(dt['MANAGER_MEMBER_IDXS'],',')}" itemInfo="${itemInfo}" objStyle="width:150px;height:55px;"/>
				</tr>
				</c:if>
			</tbody>
		</table>
		<c:if test="${empty param.mode}">
		<h4 style="float:left;">디자인정보</h4>
		<div class="comment" style="margin-top:5px;float:right;line-height:15px;">
			<ul style="display:inline-block;vertical-align: top;">
				<li>저장하신 사이트는 <c:if test="${useLayoutTmp == '1'}">'${elfn:getItemName(verItemInfo.items['layout_tmp'])}'을 선택하지 않은 경우 </c:if>'${elfn:getItemName(verItemInfo.items['local_path'])}'에 파일을 구성 후 운영하실 수 있습니다.</li>
				<li>디자인정보는 '${elfn:getMenuName('31')} &gt; ${elfn:getMenuName('42')}'의 '버전관리'에서 수정하실 수 있습니다.</li>
			</ul>
		</div>
		<table class="tbWriteA" summary="버전정보 글쓰기 서식">
			<caption>
			버전정보 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<c:set var="itemId" value="layout_tmp"/>
				<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${verItemInfo}" nextItemId="Name"/></th>
					<td>
						<itui:objectTextButton itemId="${itemId}" itemInfo="${verItemInfo}"/>
						<div class="comment" style="margin-top:5px;line-height:13px;">
							<ul style="display:inline-block;vertical-align: top;">
								<li>${elfn:getItemName(verItemInfo.items[itemId])}은 '${elfn:getItemName(itemInfo.items.site_type)}' 선택 후 사용하실 수 있습니다.</li>
								<li>${elfn:getItemName(verItemInfo.items[itemId])} 선택 후 '${elfn:getItemName(verItemInfo.items.layout_theme)}', '${elfn:getItemName(verItemInfo.items.logo)}'를 사용하실 수 있습니다.</li>
								<li>${elfn:getItemName(verItemInfo.items[itemId])} 선택시 '${elfn:getItemName(verItemInfo.items.local_path)}', '${elfn:getItemName(verItemInfo.items.include_top)}', '${elfn:getItemName(verItemInfo.items.include_bottom)}'는 수정하실 수 없습니다.</li>
							</ul>
						</div>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="layout_theme"/>
				<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${verItemInfo}" nextItemId="Name"/></th>
					<td>
						<itui:objectTextButton itemId="${itemId}" itemInfo="${verItemInfo}"/>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="logo"/>
				<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${verItemInfo}"/></th>
					<td>
						<itui:objectFile itemId="${itemId}" itemInfo="${verItemInfo}"/>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="local_path"/>
				<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
				<tr>
					<itui:itemText itemId="${itemId}" itemInfo="${verItemInfo}" objStyle="width:500px;"/>
				</tr>
				</c:if>
				<c:set var="itemId" value="include_top"/>
				<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${verItemInfo}"/></th>
					<td>
						<itui:objectText itemId="${itemId}" itemInfo="${verItemInfo}" objStyle="width:500px;"/>
						<button type="button" id="fn_btn_${itemId}" class="btnTypeF"><spring:message code="item.menu.link.def.name"/></button>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="include_bottom"/>
				<c:if test="${!elfn:isItemClosedArr(verItemInfo[verItemOrderName], verItemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${verItemInfo}"/></th>
					<td>
						<itui:objectText itemId="${itemId}" itemInfo="${verItemInfo}" objStyle="width:500px;"/>
						<button type="button" id="fn_btn_${itemId}" class="btnTypeF"><spring:message code="item.menu.link.def.name"/></button>
					</td>
				</tr>
				</c:if>
			</tbody>
		</table>
		</c:if>
		<h4>API연동</h4>
		<table class="tbWriteA" summary="API연동 글쓰기 서식">
			<caption>
			API연동 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemText itemId="daum_map_key" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="daum_local_key" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="twitter" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="twitter_id"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="twitter_url"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="twitter_ckey"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="twitter_csecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="twitter_wgid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="facebook" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="facebook_id"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="facebook_url"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="facebook_atoken"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="facebook_aid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="facebook_asecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="kakao" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="kakao_aid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="kakao_asecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="naver" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="naver_aid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="naver_asecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="google" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="google_aid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="google_asecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<spring:message var="useSnsLogin" code="Globals.sns.login.use" text="0"/>
				<c:if test="${useSnsLogin == '1'}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="sns_login" itemInfo="${itemInfo}"/></th>
					<td>
						<ul class="fn_inlineBlock fn_sns_login">
							<c:set var="itemId" value="facebook_login"/>
							<li><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></li>
							<c:set var="itemId" value="kakao_login"/>
							<li><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></li>
							<c:set var="itemId" value="naver_login"/>
							<li><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></li>
							<c:set var="itemId" value="google_login"/>
							<li><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></li>
						</ul>
					</td>
				</tr>
				</c:if>
			</tbody>
		</table>
		<h4>운영정보</h4>
		<table class="tbWriteA" summary="운영정보 글쓰기 서식">
			<caption>
			운영정보 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemText itemId="manager_name" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_email" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_phone" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_fax" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemTextarea itemId="site_addr" itemInfo="${itemInfo}" objStyle="width:500px;height:50px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_copyright" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="pri_manager_name" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemTextarea itemId="member_clause" itemInfo="${itemInfo}" objStyle="width:500px;height:200px;"/>
				</tr>
				<tr>
					<itui:itemTextarea itemId="member_perinfo" itemInfo="${itemInfo}" objStyle="width:500px;height:200px;"/>
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