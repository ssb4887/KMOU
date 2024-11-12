<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_itemClassOptnInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
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
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemText itemId="dptName" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<th><label for="ordIdx" class="required">위치</label></th>
					<td colspan="3">
						<select id="ordIdx" name="ordIdx" style="width:500px;">
							<option value="">위치 선택</option>
						<c:forEach var="ordDt" items="${ordList}" varStatus="i">
							<option value="<c:out value="${ordDt.OPTION_CODE}"/>"<c:if test="${ordDt.OPTION_CODE == dt.DEPART_IDX}"> selected="selected"</c:if>><c:if test="${ordDt.OPTION_LEVEL > 1}"><c:forEach var="optLevel" begin="2" end="${ordDt.OPTION_LEVEL}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if><c:out value="${ordDt.OPTION_NAME}"/></option>
						</c:forEach>
						</select>
						<div style="display:inline-block; margin-left:10px;">
						<itui:objectRadio itemId="ordType" itemInfo="${itemInfo}"/>
						</div>
					</td>
				</tr>
				<tr>
					<itui:itemTextarea itemId="dptDuty" itemInfo="${itemInfo}" colspan="3" objStyle="width:500px;height:50px;"/>
				</tr>
				<tr>
					<itui:itemTextarea itemId="remarks" itemInfo="${itemInfo}" colspan="3" objStyle="width:500px;height:50px;"/>
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