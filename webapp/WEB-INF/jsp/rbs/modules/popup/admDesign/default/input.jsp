<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_boardInputForm"/>								<%/* 입력폼 id/name */ %>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<%/* 입력폼 id/name : javascript에서 사용 */ %>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>		
		<jsp:param name="inputFormId" value="${inputFormId}"/>						
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		
		<h4>팝업존</h4>
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
					<itui:itemRadio itemId="popType" itemInfo="${itemInfo}"/>
				</tr>
				<tr class="fn_tr_popType1">
					<th scope="row"><itui:objectLabel itemId="linkUrl" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectText itemId="linkUrl" itemInfo="${itemInfo}" objStyle="width:500px;"/>
						<itui:objectSelect itemId="linkTarget" itemInfo="${itemInfo}"/>
					</td>
				</tr>
				<tr>
					<itui:itemPYMDHM itemId="dspDate" itemInfo="${itemInfo}"/>
				</tr>
				<tr>
					<itui:itemRadio itemId="stop" itemInfo="${itemInfo}"/>
				</tr>
			</tbody>
		</table>
		<div class="fn_popTypeWrap">
		<h4>팝업</h4>
		<table class="tbWriteA" summary="팝업 글쓰기 서식">
			<caption>
			팝업 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemFile itemId="pbg" itemInfo="${itemInfo}" downLink="${true}"/>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="popSize" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectLabel itemId="popWidth" itemInfo="${itemInfo}" required="0"/><itui:objectText itemId="popWidth" itemInfo="${itemInfo}" objStyle="width:50px;"/>px
						<itui:objectLabel itemId="popHeight" itemInfo="${itemInfo}" required="0"/><itui:objectText itemId="popHeight" itemInfo="${itemInfo}" objStyle="width:50px;"/>px
					</td>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="popLocation" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectLabel itemId="popLeft" itemInfo="${itemInfo}"/><itui:objectText itemId="popLeft" itemInfo="${itemInfo}" objStyle="width:50px;"/>px
						<itui:objectLabel itemId="popTop" itemInfo="${itemInfo}"/><itui:objectText itemId="popTop" itemInfo="${itemInfo}" objStyle="width:50px;"/>px
					</td>
				</tr>
				<tr>
					<itui:itemTextarea itemId="contents" itemInfo="${itemInfo}" objStyle="width:500px;height:100px;"/>
				</tr>
			</tbody>
		</table>
		</div>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>