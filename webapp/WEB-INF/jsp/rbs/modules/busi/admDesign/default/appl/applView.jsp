<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
	<div id="cms_board_article">
		<c:set var="summary"><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/>을 제공하는 표</c:set>
		<table class="tbViewA" summary="${summary}">
			<caption>
			상세보기
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<th><itui:objectLabel itemId="name" itemInfo="${itemInfo}"/></th>
					<td colspan="3"><itui:objectView itemId="name" itemInfo="${itemInfo}"/></td>
				</tr>
				<tr>
					<itui:itemView itemId="subject" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<itui:itemView itemId="orgCode" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<itui:itemView itemId="contents" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<itui:itemView itemId="file" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
			</tbody>
		</table>
		<div class="btnTopFull">
			<div class="right">
				<a href="<c:out value="${URL_LIST}"/>" title="목록" class="btnTypeA fn_btn_write">목록</a>
			</div>
		</div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>