<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_sitenputForm"/>								<%/* 입력폼 id/name */ %>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<%/* 입력폼 id/name : javascript에서 사용 */ %>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/manage.jsp"/>		
		<jsp:param name="inputFormId" value="${inputFormId}"/>						
	</jsp:include>
</c:if>
	<div id="cms_website_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		
		<h4>운영정보</h4>
		<table class="tbWriteA" summary="운영정보 글쓰기 서식">
			<caption>
			운영정보 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
<!-- 				<tr> -->
<%-- 					<itui:itemText itemId="manager_name" itemInfo="${itemInfo}" objStyle="width:500px;"/> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<%-- 					<itui:itemText itemId="site_email" itemInfo="${itemInfo}" objStyle="width:500px;"/> --%>
<!-- 				</tr> -->
				<tr>
					<itui:itemText itemId="site_phone" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_fax" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_addr" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_copyright" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
<!-- 				<tr> -->
<%-- 					<itui:itemText itemId="pri_manager_name" itemInfo="${itemInfo}" objStyle="width:500px;"/> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<%-- 					<itui:itemTextarea itemId="member_clause" itemInfo="${itemInfo}" objStyle="width:500px;height:200px;"/> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<%-- 					<itui:itemTextarea itemId="member_perinfo" itemInfo="${itemInfo}" objStyle="width:500px;height:200px;"/> --%>
<!-- 				</tr> -->
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_reset">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>