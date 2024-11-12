
<c:choose>
	<c:when test="${viewList[0].ISPRENEXT == 1 && viewList.size() == 1}">
		<c:set var="sumTit" value="다음글"/>
		<c:set var="sumCon" value="다음글 바로가기"/>
	</c:when>
	<c:when test="${viewList[0].ISPRENEXT == 2 && viewList.size() == 1}">
		<c:set var="sumTit" value="이전글"/>
		<c:set var="sumCon" value="이전글 바로가기"/>
	</c:when>
	<c:otherwise>
		<c:set var="sumTit" value="이전글, 다음글"/>
		<c:set var="sumCon" value="이전글,다음글 바로가기"/>
	</c:otherwise>
</c:choose>

<h5 class="titTypeC mgb10"><c:out value="${sumTit}"/></h5>
<table border="1" class="goArticle" summary="<c:out value="${sumCon}"/>">
	<caption>게시판 읽기</caption >
	<colgroup>
		<col width="50px" />
		<col />
	</colgroup>
	<tbody>
		<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
		<c:forEach items="${viewList}" var="listDt" varStatus="i">
			<c:set var="listKey" value="${listDt.BRD_IDX}"/>
			<c:choose>
				<c:when test="${listDt.ISPRENEXT eq 1}">
					<c:set var="viewListName" value="다음글"/>
					<c:set var="viewListClass" value="goPrev"/>
				</c:when>
				<c:when test="${listDt.ISPRENEXT eq 2}">
					<c:set var="viewListName" value="이전글"/>
					<c:set var="viewListClass" value=""/>
				</c:when>
			</c:choose>
			<%-- 비밀번호 입력창 display 여부 --%>
			<c:set var="isDisplaySecretPassAuth" value="${settingInfo.use_secret eq '1' and listDt.SECRET eq '1' && elfn:isDisplaySecretPassAuth((settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') and listDt.RE_LEVEL > 1, listDt.REGI_IDX, listDt.MEMBER_DUP, listDt.P_REGI_IDX, listDt.P_MEMBER_DUP)}"/>
			<tr class="${viewListClass}">
				<th>${viewListName}</th>
				<td><a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" class="fn_btn_view"<c:if test="${isDisplaySecretPassAuth}"> data-nm="<c:out value="${listKey}"/>"</c:if>><c:out value="${listDt.SUBJECT}"/></a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
