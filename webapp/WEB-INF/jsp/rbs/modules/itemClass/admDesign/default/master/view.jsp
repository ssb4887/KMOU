<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false"/>
</c:if>
	<div id="cms_board_article">
		<table class="tbViewA" summary="게시글 읽기">
			<caption>
			게시글 읽기
			</caption>
			<colgroup>
			<col style="width:120px;" />
			<col />
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemView itemId="mstCd" itemInfo="${itemInfo}"/>
					<itui:itemView itemId="mstName" itemInfo="${itemInfo}"/>
				</tr>
				<tr>
					<itui:itemView itemId="mstApp" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
				<tr>
					<itui:itemView itemId="remarks" itemInfo="${itemInfo}" colspan="3"/>
				</tr>
			</tbody>
		</table>
		<h4>코드정보</h4>
		<table class="tbListA" summary="코드 목록을 볼 수 있습니다.">
			<caption>코드 목록</caption>
			<colgroup>
				<col width="60px" />
				<col width="150px" />
				<col width="150px" />
				<col />
				<col width="100px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">코드</th>
				<th scope="col">코드명</th>
				<th scope="col">비고</th>
				<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty optnList}">
				<tr>
					<td colspan="5" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
				<c:forEach var="listDt" items="${optnList}" varStatus="i">
				<tr>
					<td class="num">${listNo}</td>
					<td><c:out value="${listDt.OPTION_CODE}"/></td>
					<td><c:out value="${listDt.OPTION_NAME}"/></td>
					<td><c:out value="${listDt.REMARKS}"/></td>
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				</tr>
				<c:set var="listNo" value="${listNo + 1}"/>
				</c:forEach>
			</tbody>
		</table>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>