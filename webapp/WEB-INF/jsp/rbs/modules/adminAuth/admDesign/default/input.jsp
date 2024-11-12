<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_authInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${jspSiteIncPath}/iframe_top.jsp" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<h4><c:out value="${admMenu.menu_name}"/></h4>
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="${URL_SUBMITPROC}" target="submit_target">
		<table class="tbWriteA" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
				<col style="width:120px;" />
				<col />
				<col />
				<col />
				<col />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">권한</th>
				<th scope="col">사용자유형</th>
				<th scope="col"><itui:objectItemName itemId="mbrGrp" itemInfo="${itemInfo}"/></th>
				<th scope="col"><itui:objectItemName itemId="mbrDpt" itemInfo="${itemInfo}"/></th>
				<th scope="col" class="end"><itui:objectItemName itemId="mbrMbr" itemInfo="${itemInfo}"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody>
					<c:set var="authItemId" value="menu"/>
              		<c:set var="utp_itemIdName" value="${authItemId}Utp"/>
              		<c:set var="grp_itemIdName" value="${authItemId}Grp"/>
              		<c:set var="dpt_itemIdName" value="${authItemId}Dpt"/>
              		<c:set var="mbr_itemIdName" value="${authItemId}Mbr"/>
              		<c:set var="utp_columnIdName" value="usertype_idx"/>
              		<c:set var="grp_columnIdName" value="group_idxs"/>
              		<c:set var="dpt_columnIdName" value="depart_idxs"/>
              		<c:set var="mbr_columnIdName" value="member_idxs"/>
				<tr>
					<th scope="row"><label for="menuUtp">메뉴접근 권한</label></th>
					<td class="alignC">
						<itui:objectSelect itemId="mbrUtp" name="${utp_itemIdName}" id="${utp_itemIdName}" objVal="${admMenu[utp_columnIdName]}" itemInfo="${itemInfo}" objStyle="min-width:120px;"/>이상
					</td>
					<td class="alignC">
						<itui:objectMultiSelectButton itemId="mbrGrp" name="${grp_itemIdName}" id="${grp_itemIdName}" objValArray="${fn:split(admMenu[grp_columnIdName],',')}" itemInfo="${itemInfo}" objStyle="width:130px;height:55px;"/>
					</td>
					<td class="alignC">
						<itui:objectMultiSelectButton itemId="mbrDpt" name="${dpt_itemIdName}" id="${dpt_itemIdName}" objValArray="${fn:split(admMenu[dpt_columnIdName],',')}" itemInfo="${itemInfo}" objStyle="width:130px;height:55px;"/>
					</td>
					<td class="alignC">
						<itui:objectMultiSelectButton itemId="mbrMbr" name="${mbr_itemIdName}" id="${mbr_itemIdName}" objValArray="${fn:split(admMenu[mbr_columnIdName],',')}" itemInfo="${itemInfo}" objStyle="width:130px;height:55px;"/>
					</td>
				</tr>
				<c:if test="${!empty authManagerArray}">
				<c:forEach var="authManagerObject" items="${authManagerArray}" varStatus="i">
              		<c:set var="utp_itemIdName" value="${authManagerObject.item_id}Utp"/>
              		<c:set var="grp_itemIdName" value="${authManagerObject.item_id}Grp"/>
              		<c:set var="dpt_itemIdName" value="${authManagerObject.item_id}Dpt"/>
              		<c:set var="mbr_itemIdName" value="${authManagerObject.item_id}Mbr"/>
              		<c:set var="utp_columnIdName" value="${authManagerObject.column_id}_UTP"/>
              		<c:set var="grp_columnIdName" value="${authManagerObject.column_id}_GRP"/>
              		<c:set var="dpt_columnIdName" value="${authManagerObject.column_id}_DPT"/>
              		<c:set var="mbr_columnIdName" value="${authManagerObject.column_id}_MBR"/>
              		<c:if test="${authManagerObject.adm_closed != 1}">
              			<c:set var="authItemName" value="${elfn:getItemName(authManagerObject)}"/>
				<tr>
					<th scope="row"><label for="${utp_itemName}"><c:out value="${authItemName}"/> 권한</label></th>
					<td class="alignC">
						<itui:objectSelect itemId="mbrUtp" name="${utp_itemIdName}" id="${utp_itemIdName}" defVal="${authManagerObject.usr_min_utp}" objVal="${authObject[utp_columnIdName]}" itemInfo="${itemInfo}" objStyle="min-width:120px;"/>이상
					</td>
					<td class="alignC">
						<itui:objectMultiSelectButton itemId="mbrGrp" name="${grp_itemIdName}" id="${grp_itemIdName}" objValArray="${fn:split(authObject[grp_columnIdName],',')}" itemInfo="${itemInfo}" objStyle="width:130px;height:55px;"/>
					</td>
					<td class="alignC">
						<itui:objectMultiSelectButton itemId="mbrDpt" name="${dpt_itemIdName}" id="${dpt_itemIdName}" objValArray="${fn:split(authObject[dpt_columnIdName],',')}" itemInfo="${itemInfo}" objStyle="width:130px;height:55px;"/>
					</td>
					<td class="alignC">
						<itui:objectMultiSelectButton itemId="mbrMbr" name="${mbr_itemIdName}" id="${mbr_itemIdName}" objValArray="${fn:split(authObject[mbr_columnIdName],',')}" itemInfo="${itemInfo}" objStyle="width:130px;height:55px;"/>
					</td>
				</tr>
					</c:if>
				</c:forEach>
				</c:if>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<input type=button value="취소"  class="btnTypeB fn_btn_reset"/>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${jspSiteIncPath}/iframe_bottom.jsp" flush="false"/></c:if>