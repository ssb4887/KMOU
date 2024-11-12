<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_bannerInputForm"/>								
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>		
		<jsp:param name="inputFormId" value="${inputFormId}"/>						
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		
		<h4>배너존</h4>
		<table class="tbWriteA" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemText itemId="subject" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemFile itemId="img" itemInfo="${itemInfo}" downLink="${true}"/>
				</tr>
				<tr>
					<itui:itemPYMD itemId="dspDate" itemInfo="${itemInfo}"/>
				</tr>
				<tr>
					<itui:itemRadio itemId="dspPage" itemInfo="${itemInfo}"/>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="linkUrl" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectText itemId="linkUrl" itemInfo="${itemInfo}" objStyle="width:500px;"/>
						<itui:objectSelect itemId="linkTarget" itemInfo="${itemInfo}"/>
					</td>
				</tr>
				<tr>
					<itui:itemRadio itemId="stop" itemInfo="${itemInfo}"/>
				</tr>
				<tr>
					<itui:itemText itemId="orderIdx" itemInfo="${itemInfo}" objStyle="width:20px;"/>
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