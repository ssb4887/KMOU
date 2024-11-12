<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_memberGrupInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
		<input type="hidden" id="grpType" name="grpType" value="ADM"/>
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
					<itui:itemText itemId="grpCd" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<itui:itemText itemId="grpName" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<itui:itemTextarea itemId="remarks" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<th><label for="ordIdx">순서</label></th>
					<td colspan="3">
						<select id="ordIdx" name="ordIdx" style="width:50px;">
						<c:forEach var="ordDt" items="${ordList}" varStatus="i">
							<option value="<c:out value="${ordDt.OPTION_CODE}"/>"<c:if test="${ordDt.OPTION_CODE == dt.GROUP_CODE}"> selected="selected"</c:if>>${i.count}</option>
						</c:forEach>
						<c:if test="${param.mode != 'm'}">
							<option value="" selected="selected">${fn:length(ordList) + 1}</option>
						</c:if>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<input type=button value="취소"  class="btnTypeB fn_btn_cancel">
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>