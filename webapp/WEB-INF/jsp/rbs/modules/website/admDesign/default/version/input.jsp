<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ include file="common.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_versionInputForm"/>
<spring:message var="useLayoutTmp" code="Globals.layoutTmp.use" text=""/>
<c:if test="${useLayoutTmp == '1'}">
<spring:message var="layoutItemFlag" code="Globals.layoutTmp.item.flag" text=""/>
</c:if>
<c:set var="itemOrderName" value="${layoutItemFlag}${submitType}_order"/>
	<jsp:include page="${jspSiteIncPath}/iframe_top.jsp" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title} (${menuName})"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
	<div id="cms_board_article">
		<div class="comment" style="line-height:15px;">
			<ul style="display:inline-block;vertical-align: top;">
				<li>저장하신 버전의 사이트는 <c:if test="${useLayoutTmp == '1'}">'${elfn:getItemName(itemInfo.items['layout_tmp'])}'을 선택하지 않은 경우 </c:if>'${elfn:getItemName(itemInfo.items['local_path'])}'에 파일을 구성 후 운영하실 수 있습니다.</li>
				<li>저장하신 버전정보는 '미리보기'로 확인하실 수 있습니다. 사이트에 적용하실려면 '${elfn:getMenuName('31')} &gt; ${elfn:getMenuName('42')}'의 '적용 버전' 선택 후 '적용'버튼을 클릭합니다.</li>
			</ul>
		</div>
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
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
					<c:set var="itemId" value="ver_idx"/>
					<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectView itemId="${itemId}" itemInfo="${itemInfo}"/>
						<c:if test="${dt.ISSERVICE == '1'}"> [적용]</c:if>
					</td>
					</c:if>
					<c:set var="itemId" value="copy_ver_idx"/>
					<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
					<itui:itemSelect itemId="${itemId}" itemInfo="${itemInfo}"/>
					</c:if>
				</tr>
				<c:set var="itemId" value="layout_tmp"/>
				<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}" nextItemId="Name"/></th>
					<td>
						<itui:objectTextButton itemId="${itemId}" itemInfo="${itemInfo}"/>
						<div class="comment" style="margin-top:5px;line-height:13px;">
							<ul style="display:inline-block;vertical-align: top;">
								<li>${elfn:getItemName(itemInfo.items[itemId])} 선택 후 '${elfn:getItemName(itemInfo.items.layout_theme)}', '${elfn:getItemName(itemInfo.items.logo)}'를 사용하실 수 있습니다.</li>
								<li>${elfn:getItemName(itemInfo.items[itemId])} 선택시 '${elfn:getItemName(itemInfo.items.local_path)}', '${elfn:getItemName(itemInfo.items.include_top)}', '${elfn:getItemName(itemInfo.items.include_bottom)}'는 수정하실 수 없습니다.</li>
							</ul>
						</div>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="layout_theme"/>
				<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}" nextItemId="Name"/></th>
					<td>
						<itui:objectTextButton itemId="${itemId}" itemInfo="${itemInfo}"/>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="logo"/>
				<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectFile itemId="${itemId}" itemInfo="${itemInfo}"/>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="local_path"/>
				<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
				<tr>
					<itui:itemText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				</c:if>
				<c:set var="itemId" value="include_top"/>
				<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:500px;"/>
						<button type="button" id="fn_btn_${itemId}" class="btnTypeF"><spring:message code="item.menu.link.def.name"/></button>
					</td>
				</tr>
				</c:if>
				<c:set var="itemId" value="include_bottom"/>
				<c:if test="${!elfn:isItemClosedArr(itemInfo[itemOrderName], itemInfo.items[itemId])}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:500px;"/>
						<button type="button" id="fn_btn_${itemId}" class="btnTypeF"><spring:message code="item.menu.link.def.name"/></button>
					</td>
				</tr>
				</c:if>
				<tr>
					<itui:itemTextarea itemId="remarks" itemInfo="${itemInfo}" objStyle="width:500px;height:50px;"/>
				</tr>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<jsp:include page = "${jspSiteIncPath}/iframe_bottom.jsp" flush = "false"/>