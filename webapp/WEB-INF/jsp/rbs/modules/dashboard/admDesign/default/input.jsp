<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="bannerItemInfo" value="${moduleItem.banner_item_info}"/>
<c:set var="popupItemInfo" value="${moduleItem.popup_item_info}"/>
<c:set var="boardItemInfo" value="${moduleItem.board_item_info}"/>
<c:set var="contentsItemInfo" value="${moduleItem.contents_item_info}"/>
<c:set var="inputFormId" value="fn_dashboardInputForm"/>								<%/* 입력폼 id/name */ %>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<%/* 입력폼 id/name : javascript에서 사용 */ %>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>		
		<jsp:param name="inputFormId" value="${inputFormId}"/>						
	</jsp:include>
</c:if>
	<div id="cms_dashboard_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<table class="tbWriteA" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:100px;" />
			<col />
			</colgroup>
			<tbody>
			<c:if test="${!empty bannerItemInfo}">
				<c:set var="moduleId" value="banner"/>
				<c:set var="moduleIdUStr" value="BANNER"/>
				<c:set var="itemInfo" value="${bannerItemInfo}"/>
				<c:set var="moduleDt" value="${dashboardDt[moduleIdUStr]}"/>
				<c:set var="tableTitle" value="${moduleSetting['banner_setting_info']['input_title']}"/>
				<tr>
					<th scope="row">${tableTitle}</th>
					<td>
						<c:forEach var="ItemOptnDt" items="${bannerItemOptnList}" varStatus="i">
						<c:set var="itemIdx" value="${ItemOptnDt.OPTION_CODE}"/>
						<c:set var="moduleDtName" value="${moduleIdUStr}${itemIdx}"/>
						<c:set var="moduleDt" value="${dashboardDt[moduleIdUStr][moduleDtName]}"/>
						<dl class="fn_inlineBlock">
							<dt class="fn_autoTitle"><c:out value="${ItemOptnDt.OPTION_NAME}"/></dt>
							<dd>
								<dl class="fn_inlineBlock fn_short">
									<c:set var="itemId" value="isUse"/>
									<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" itemInfo="${itemInfo}"/></dt>
									<dd><itui:objectCheckboxSG itemId="${itemId}" id="${moduleId}${itemId}${itemIdx}" name="${moduleId}${itemId}${itemIdx}" itemInfo="${itemInfo}" objDt="${moduleDt}"/></dd>
									<c:set var="itemId" value="limitNumber"/>
									<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" itemInfo="${itemInfo}"/></dt>
									<dd><itui:objectText itemId="${itemId}" id="${moduleId}${itemId}${itemIdx}" name="${moduleId}${itemId}${itemIdx}" itemInfo="${itemInfo}" objDt="${moduleDt}" objClass="fn_${itemId}" objStyle="width:20px;"/></dd>
								</dl>
							</dd>
						</dl>
						</c:forEach>
					</td>
				</tr>
			</c:if>
			<c:if test="${!empty popupItemInfo}">
				<c:set var="moduleId" value="popup"/>
				<c:set var="moduleIdUStr" value="POPUP"/>
				<c:set var="itemInfo" value="${popupItemInfo}"/>
				<c:set var="moduleDt" value="${dashboardDt[moduleIdUStr]}"/>
				<c:set var="tableTitle" value="${moduleSetting['popup_setting_info']['input_title']}"/>
				<tr>
					<th scope="row">${tableTitle}</th>
					<td>
						<dl class="fn_inlineBlock fn_short">
							<c:set var="itemId" value="isUse"/>
							<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectCheckboxSG itemId="${itemId}" id="${moduleId}${itemId}" name="${moduleId}${itemId}" itemInfo="${itemInfo}" objDt="${moduleDt}"/></dd>
							<c:set var="itemId" value="limitNumber"/>
							<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" id="${moduleId}${itemId}" name="${moduleId}${itemId}" itemInfo="${itemInfo}" objDt="${moduleDt}" objClass="fn_${itemId}" objStyle="width:20px;"/></dd>
						</dl>
					</td>
				</tr>
			</c:if>
			</tbody>
		</table>
		<c:if test="${!empty boardItemInfo}">
		<c:set var="moduleId" value="board"/>
		<c:set var="moduleIdUStr" value="BOARD"/>
		<c:set var="itemInfo" value="${boardItemInfo}"/>
		<c:set var="tableTitle" value="${moduleSetting['board_setting_info']['input_title']}"/>
		<h4><c:out value="${tableTitle}"/></h4>
		<table class="tbWriteA" summary="<c:out value="${tableTitle}"/> 글쓰기 서식">
			<caption>
			<c:out value="${tableTitle}"/> 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:100px;" />
			<col />
			</colgroup>
			<tbody>
				<c:set var="settingName" value="${moduleId}_setting_info"/>
				<c:set var="endNumber" value="${moduleSetting[settingName]['item_number']}"/>
				<c:forEach var="itemIdx" begin="1" end="${endNumber}">
				<c:set var="moduleDtName" value="${moduleIdUStr}${itemIdx}"/>
				<c:set var="moduleDt" value="${dashboardDt[moduleIdUStr][moduleDtName]}"/>
				<c:set var="itemId" value="menuIdx"/>
				<c:set var="columnId" value="MENU_IDX"/>
				<c:set var="cateObjDisabled" value="${false}"/>
				<c:if test="${empty optnHashMap[moduleDt['MASTER_CODE']]}"><c:set var="cateObjDisabled" value="${true}"/></c:if>
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" nextStr="${itemIdx}" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock fn_short">
							<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" itemInfo="${itemInfo}"/></dt>
							<dd>
								<itui:objectTextButton itemId="${itemId}" id="${moduleId}${itemId}${itemIdx}" name="${moduleId}${itemId}${itemIdx}"  itemInfo="${itemInfo}" objDt="${moduleDt}" btnClass="fn_btn_${moduleId}${itemId}" btnDelClass="fn_btn_del_${moduleId}${itemId}" objStyle="width:200px;"/>
							</dd>
							<c:set var="dtName" value="${columnId}_ONAME"/>
							<dt class="fn_text_label"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}OName" itemInfo="${itemInfo}"/></dt>
							<dd><input type="text" name="${moduleId}${itemId}${itemIdx}OName" id="${moduleId}${itemId}${itemIdx}OName" class="fn_boardmenuIdxOName" value="${moduleDt[dtName]}"/></dd>
							<c:set var="itemId" value="cateListCode"/>
							<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectSelect itemId="${itemId}" id="${moduleId}${itemId}${itemIdx}" name="${moduleId}${itemId}${itemIdx}"  itemInfo="${itemInfo}" objDt="${moduleDt}" optnOptList="${optnHashMap[moduleDt['MASTER_CODE']]}" objStyle="width:100px;" disabled="${cateObjDisabled}"/></dd>
							<c:set var="itemId" value="isContents"/>
							<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectCheckboxSG itemId="${itemId}" id="${moduleId}${itemId}${itemIdx}" name="${moduleId}${itemId}${itemIdx}" itemInfo="${itemInfo}" objDt="${moduleDt}"/></dd>
							<c:set var="itemId" value="isListImg"/>
							<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectCheckboxSG itemId="${itemId}" id="${moduleId}${itemId}${itemIdx}" name="${moduleId}${itemId}${itemIdx}" itemInfo="${itemInfo}" objDt="${moduleDt}"/></dd>
							<c:set var="itemId" value="limitNumber"/>
							<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" id="${moduleId}${itemId}${itemIdx}" name="${moduleId}${itemId}${itemIdx}"  itemInfo="${itemInfo}" objDt="${moduleDt}" objClass="fn_${itemId}" objStyle="width:20px;"/></dd>
						</dl>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		</c:if>
		<c:if test="${!empty contentsItemInfo}">
		<c:set var="moduleId" value="contents"/>
		<c:set var="moduleIdUStr" value="CONTENTS"/>
		<c:set var="itemInfo" value="${contentsItemInfo}"/>
		<c:set var="tableTitle" value="${moduleSetting['contents_setting_info']['input_title']}"/>
		<h4><c:out value="${tableTitle}"/></h4>
		<table class="tbWriteA" summary="<c:out value="${tableTitle}"/> 글쓰기 서식">
			<caption>
			<c:out value="${tableTitle}"/> 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:100px;" />
			<col />
			</colgroup>
			<tbody>
				<c:set var="settingName" value="${moduleId}_setting_info"/>
				<c:set var="endNumber" value="${moduleSetting[settingName]['item_number']}"/>
				<c:forEach var="itemIdx" begin="1" end="${endNumber}">
				<c:set var="moduleDtName" value="${moduleIdUStr}${itemIdx}"/>
				<c:set var="moduleDt" value="${dashboardDt[moduleIdUStr][moduleDtName]}"/>
				<c:set var="itemId" value="menuIdx"/>
				<c:set var="columnId" value="MENU_IDX"/>
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" nextStr="${itemIdx}" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock fn_short">
							<dt class="hidden"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}" itemInfo="${itemInfo}"/></dt>
							<dd>
							<itui:objectTextButton itemId="${itemId}" id="${moduleId}${itemId}${itemIdx}" name="${moduleId}${itemId}${itemIdx}" itemInfo="${itemInfo}" objDt="${moduleDt}" btnClass="fn_btn_${moduleId}${itemId}" btnDelClass="fn_btn_del_${moduleId}${itemId}" objStyle="width:200px;"/>
							</dd>
							<c:set var="dtName" value="${columnId}_ONAME"/>
							<dt class="fn_text_label"><itui:objectLabel itemId="${itemId}" preItemId="${moduleId}" nextItemId="${itemIdx}OName" itemInfo="${itemInfo}"/></dt>
							<dd><input type="text" name="${moduleId}${itemId}${itemIdx}OName" id="${moduleId}${itemId}${itemIdx}OName" class="fn_boardmenuIdxOName" value="${moduleDt[dtName]}"/></dd>
						</dl>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		</c:if>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_reset">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>