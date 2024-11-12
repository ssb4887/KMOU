<%@ include file="../../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_ItemInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${itemSettingInfo.searchInput_title} (${queryString.mt})"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/item/searchInput.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<spring:message var="defaultItemName" code="item.module.items.set.search.defaultItem"/>
	<spring:message var="searchItemName" code="item.module.items.set.search.searchItem"/>
	
	<spring:message var="defaultKeyItemTName" code="item.module.items.set.search.itemSearch.defaultItem"/>
	<spring:message var="searchKeyItemTName" code="item.module.items.set.search.itemSearch.searchItem"/>
	
	<spring:message var="defaultKeyFItemTName" code="item.module.items.set.search.selectSearch.defaultItem"/>
	<spring:message var="searchIKeyFtemTName" code="item.module.items.set.search.selectSearch.searchItem"/>
	
	<spring:message var="msgBtnMoveRight" code="button.move.right"/>
	<spring:message var="msgBtnMoveLeft" code="button.move.left"/>
	<spring:message var="msgBtnRight" code="button.right"/>
	<spring:message var="msgBtnLeft" code="button.left"/>
	<spring:message var="msgBtnMoveUp" code="button.move.up"/>
	<spring:message var="msgBtnMoveDown" code="button.move.down"/>
	<spring:message var="msgBtnUp" code="button.up"/>
	<spring:message var="msgBtnDown" code="button.down"/>
	<div id="cms_board_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
		<table class="tbWriteA fn_setSearchItem" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<th><label for="defaultItemKey"><spring:message code="item.module.items.set.search.itemSearch"/></label></th>
					<td>
						<dl class="multi">
							<dt>${defaultItemName}</dt>
							<dd>
								<select id="defaultItemKey" name="defaultItemKey" title="${defaultKeyItemTName}" multiple="multiple">
								<c:forEach var="itemDt" items="${defaultIItemList}" varStatus="i">
									<option value="<c:out value="${itemDt.OPTION_CODE}"/>"><c:out value="${itemDt.OPTION_NAME}"/></option>
								</c:forEach>
								</select>
							</dd>
						</dl>
						<ul class="btn_h_arrow">
							<li><button type="button" class="right" data-from="defaultItemKey" data-to="searchItemKey" title="${msgBtnMoveRight}">${msgBtnRight}</button></li>
							<li><button type="button" class="left" data-from="searchItemKey" data-to="defaultItemKey" title="${msgBtnMoveLeft}">${msgBtnLeft}</button></li>
						</ul>
						<dl class="multi">
							<dt>${searchItemName}</dt>
							<dd>
								<select id="searchItemKey" name="searchItemKey" title="${searchKeyItemTName}" multiple="multiple">
								<c:forEach var="itemDt" items="${searchIItemList}" varStatus="i">
									<option value="<c:out value="${itemDt.OPTION_CODE}"/>"><c:out value="${itemDt.OPTION_NAME}"/></option>
								</c:forEach>
								</select>
							</dd>
						</dl>
						<ul class="btn_v_arrow">
							<li><button type="button" class="up" data-target="searchItemKey" title="${msgBtnMoveUp}">${msgBtnUp}</button></li>
							<li><button type="button" class="down" data-target="searchItemKey" title="${msgBtnMoveDown}">${msgBtnDown}</button></li>
						</ul>
					</td>
				</tr>
				<tr>
					<th><label for="defaultItemKeyField"><spring:message code="item.module.items.set.search.selectSearch"/></label></th>
					<td>
						<dl class="multi">
							<dt>${defaultItemName}</dt>
							<dd>
								<select id="defaultItemKeyField" name="defaultItemKeyField" title="${defaultKeyFItemTName}" multiple="multiple">
								<c:forEach var="itemDt" items="${defaultSItemList}" varStatus="i">
									<option value="<c:out value="${itemDt.OPTION_CODE}"/>"><c:out value="${itemDt.OPTION_NAME}"/></option>
								</c:forEach>
								</select>
							</dd>
						</dl>
						<ul class="btn_h_arrow">
							<li><button type="button" class="right" data-from="defaultItemKeyField" data-to="searchItemKeyField" title="${msgBtnMoveRight}">${msgBtnRight}</button></li>
							<li><button type="button" class="left" data-from="searchItemKeyField" data-to="defaultItemKeyField" title="${msgBtnMoveLeft}">${msgBtnLeft}</button></li>
						</ul>
						<dl class="multi">
							<dt>${searchItemName}</dt>
							<dd>
								<select id="searchItemKeyField" name="searchItemKeyField" title="${searchIKeyFtemTName}" multiple="multiple">
								<c:forEach var="itemDt" items="${searchSItemList}" varStatus="i">
									<option value="<c:out value="${itemDt.OPTION_CODE}"/>"><c:out value="${itemDt.OPTION_NAME}"/></option>
								</c:forEach>
								</select>
							</dd>
						</dl>
						<ul class="btn_v_arrow">
							<li><button type="button" class="up" data-target="searchItemKeyField" title="${msgBtnMoveUp}">${msgBtnUp}</button></li>
							<li><button type="button" class="down" data-target="searchItemKeyField" title="${msgBtnMoveDown}">${msgBtnDown}</button></li>
						</ul>
					</td>
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