<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_contentsInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
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
			</colgroup>
			<tbody>
				<c:if test="${useLocaleLang == '1'}">
				<tr>
					<th scope="row"><spring:message code="item.lang_name.name"/></th>
					<td><c:out value="${searchLangName}"/></td>
				</tr>
				</c:if>
				<c:if test="${param.mode == 'm' && !empty dt.CONTENTS_CODE}">
				<tr>
					<th scope="row">코드</th>
					<td><c:out value="${dt.CONTENTS_CODE}"/></td>
				</tr>
				</c:if>
				<tr>
					<itui:itemText itemId="contName" itemInfo="${itemInfo}"/>
				</tr>
				<c:set var="itemId" value="contType"/>
				<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
				<c:set var="useContentsType" value="${elfn:useSettingtem(settingInfo, itemObj)}"/>
				<c:if test="${useContentsType}">
				<tr>
					<itui:itemSelect itemId="${itemId}" itemInfo="${itemInfo}"/>
				</tr>
				</c:if>
				<c:if test="${param.mode != 'm'}">
				<%/* 콘텐츠 복제 - 2차 작업 %>
				<tr>
					<th scope="row"><spring:message code="item.copy_contents.name"/></th>
					<td></td>
				</tr>
				<%*/ %>
				<%/* 기본 Branch Type 선택 -> 기본 Web1으로 저장 %>
				<c:set var="branchItemInfo" value="${moduleItem.branch_item_info}"/>
				<c:set var="itemId" value="branchType"/>
				<spring:message var="branchTypeTitle" code="branch.default_name.name" arguments="${elfn:getItemName(branchItemInfo.items[itemId])}"/>
				<tr>
					<itui:itemSelect itemId="${itemId}" objTitle="${branchTypeTitle}"  itemInfo="${branchItemInfo}" optnHashMap="${branchOptnHashMap}"/>
				</tr>
				<%*/ %>
				</c:if>
				<tr>
					<itui:itemText itemId="metaKeyword" itemInfo="${itemInfo}" objClass="wFull"/>
				</tr>
				<tr>
					<itui:itemTextarea itemId="remarks" itemInfo="${itemInfo}"/>
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