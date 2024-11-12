<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_myInfoInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/myInfo.jsp"/>
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
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
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
					<itui:itemPHONE itemId="mobilePhone" itemInfo="${itemInfo}" objType="1" colspan="3"/>
				</tr>
				<tr>
					<itui:itemEMAIL itemId="mbrEmail" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_reset">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>