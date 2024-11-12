<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pollui" tagdir="/WEB-INF/tags/item/poll" %>
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
	<div id="cms_poll_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<input type="hidden" id="itemIdxs" name="itemIdxs"/>
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
					<itui:itemText itemId="title" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemFile itemId="img" itemInfo="${itemInfo}" downLink="${true}"/>
				</tr>
				<tr>
					<itui:itemPYMDHM itemId="limitDate" itemInfo="${itemInfo}"/>
				</tr>
				<tr>
					<itui:itemRadio itemId="isstop" itemInfo="${itemInfo}"/>
				</tr>
				<tr>
					<itui:itemCheckboxSG itemId="isquestype" itemInfo="${itemInfo}" optnCode="1" optnName="사용"/>
				</tr>
				<tr>
					<itui:itemCheckboxSG itemId="ispollitem" itemInfo="${itemInfo}" optnCode="1" optnName="사용"/>
				</tr>
				<tr>
					<itui:itemCheckboxSG itemId="isresult" itemInfo="${itemInfo}" optnCode="1" optnName="사용"/>
				</tr>
				<tr>
					<itui:itemTextarea itemId="contents" itemInfo="${itemInfo}" objStyle="width:500px;height:100px;"/>
				</tr>
			</tbody>
		</table>
		<!-- 항목 -->
		<spring:message var="msgItem" code="item.poll.question.item"/>
		<spring:message var="msgItemEtc" code="item.poll.etc.opinion"/>
		<spring:message var="msgPoint" code="item.poll.points"/>
		<div id="fn_item_list_wp" class="fn_pollMng hidden">
			<div class="btnTopFull">
				<div class="left">
					<h5><c:out value="${msgItem}"/></h5>
					<button type="button" title="추가" class="btnTFW fn_btn_add_item">추가</button>
					<button type="button" title="삭제" class="btnTFDL fn_btn_del_item">삭제</button>
				</div>
			</div>
			<div class="fn_item_list">
				<table class="tbListA" summary="<c:out value="${msgItem}"/> 목록을 볼 수 있습니다.">
					<caption><c:out value="${msgItem}"/> 목록</caption>
					<colgroup>
						<col width="50px" />
						<col />
						<col width="80px"/>
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><input type="checkbox" id="itemSelectAll" name="itemSelectAll" title="<spring:message code="item.select.all"/>"/></th>
							<th scope="col"><c:out value="${msgItem}"/></th>
							<th scope="col" class="end"><c:out value="${msgPoint}"/></th>
						</tr>
					</thead>
					<tbody class="alignC">
					<pollui:itemList itemList="${itemList}" usePoint="${true}" msgPoint="${msgPoint}" msgItem="${msgItem}" msgItemEtc="${msgItemEtc}" msgItemSelect="${msgItemSelect}"/>
					</tbody>
				</table>
			</div>
		</div>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>