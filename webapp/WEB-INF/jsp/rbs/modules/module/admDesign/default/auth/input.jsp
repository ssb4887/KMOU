<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_moduleFnInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title} (${queryString.tit})"/>
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
				<tr>
					<itui:itemText itemId="authName" itemInfo="${itemInfo}" objStyle="width:200px;"/>
				</tr>
				<tr>
					<itui:itemTextarea itemId="remarks" itemInfo="${itemInfo}" objStyle="width:500px;height:50px;"/>
				</tr>
			</tbody>
		</table>
		<h4>권한설정</h4>
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
   			<c:set var="requiredName" value="required_${submitType}"/>
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
              			<c:set var="itemRequired" value="${itemInfo['mbrUtp'][requiredName]}"/>
				<tr>
					<th scope="row"><label for="${utp_itemName}"<c:if test="${itemRequired == 1}"> class="required"</c:if>><c:out value="${authItemName}"/> 권한</label></th>
					<td class="alignC">
						<itui:objectSelect itemId="mbrUtp" name="${utp_itemIdName}" id="${utp_itemIdName}" defVal="${authManagerObject.usr_min_utp}" objVal="${authObject[utp_columnIdName]}" itemInfo="${itemInfo}" objStyle="min-width:150px;"/>이상
					</td>
					<td class="alignC">
						<itui:objectMultiSelectButton itemId="mbrGrp" name="${grp_itemIdName}" id="${grp_itemIdName}" objValArray="${fn:split(authObject[grp_columnIdName],',')}" itemInfo="${itemInfo}" objStyle="width:150px;height:55px;"/>
					</td>
					<td class="alignC">
						<itui:objectMultiSelectButton itemId="mbrDpt" name="${dpt_itemIdName}" id="${dpt_itemIdName}" objValArray="${fn:split(authObject[dpt_columnIdName],',')}" itemInfo="${itemInfo}" objStyle="width:150px;height:55px;"/>
					</td>
					<td class="alignC">
						<itui:objectMultiSelectButton itemId="mbrMbr" name="${mbr_itemIdName}" id="${mbr_itemIdName}" objValArray="${fn:split(authObject[mbr_columnIdName],',')}" itemInfo="${itemInfo}" objStyle="width:150px;height:55px;"/>
					</td>
				</tr>
				</c:if>
			</c:forEach>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>