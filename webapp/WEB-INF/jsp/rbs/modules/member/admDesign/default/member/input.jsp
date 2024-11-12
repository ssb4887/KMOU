<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_memberInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<c:set var="itemOrderName" value="${submitType}_order"/>
	<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
	<c:set var="itemObjs" value="${itemInfo.items}"/>
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
			    <tr>
					<itui:itemSelect itemId="utpIdx" itemInfo="${itemInfo}"/>
					<th scope="row"><itui:objectLabel itemId="mbrGrp" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectMultiSelectButton itemId="mbrGrp" itemInfo="${itemInfo}" objValList="${memberGrupList}" objStyle="width:150px;height:55px;"/>
					</td>
				</tr>
				<tr>
					<itui:itemID itemId="mbrId" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<itui:itemText itemId="mbrName" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<itui:itemPassword itemId="mbrPwd" itemInfo="${itemInfo}"/>
					<itui:itemPasswordReconfirm itemId="mbrPwd" itemInfo="${itemInfo}"/>
				</tr>
				<tr>
					<itui:itemPHONE itemId="homePhone" itemInfo="${itemInfo}" objType="0" colspan="3"/>
				</tr>
				<tr>
					<itui:itemPHONE itemId="mobilePhone" itemInfo="${itemInfo}" objType="1" colspan="3"/>
				</tr>
				<c:if test="${!elfn:isItemClosedArr(itemOrder, itemObjs['homeAddr'])}">
				<tr>
					<itui:itemADDR itemId="homeAddr" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				</c:if>
				<tr>
					<c:choose>
						<c:when test="${elfn:isUseNameAuth()}"><c:set var="dupConfirm" value="0"/></c:when>
						<c:otherwise><c:set var="dupConfirm" value="1"/></c:otherwise>
					</c:choose>
					<itui:itemEMAIL itemId="mbrEmail" itemInfo="${itemInfo}" colspan="3" dupConfirm="${dupConfirm}"/>
				</tr>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<input type=button value="취소"  class="btnTypeB fn_btn_cancel">
		</div>
		</form>
		<form id="fn_mbrIdConfirmForm" name="fn_mbrIdConfirmForm" method="post" action="<c:out value="${URL_IDCONFIRMPROC}"/>" target="submit_target">
			<input type="hidden" name="mbrId"/>
		</form>
		<form id="fn_mbrEmailConfirmForm" name="fn_mbrEmailConfirmForm" method="post" action="<c:out value="${URL_EMAILCONFIRMPROC}"/>" target="submit_target">
			<input type="hidden" name="mbrEmail"/>
			<input type="hidden" name="mbrIdx" value="<c:out value="${dt.MEMBER_IDX}"/>"/>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>