<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_calendar_article">
		<table class="tbViewA" summary="게시글 읽기">
			<caption>
			게시글 읽기
			</caption>
			<colgroup>
			<col style="width:100px;" />
			<col />
			<col style="width:100px;" />
			<col />
			<col style="width:100px;" />
			<col style="width:60px;" />
			</colgroup>
			<thead>
				<tr>
					<th colspan="6" class="viewTlt">
					<c:out value="${dt.SUBJECT}"/>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th scope="row">작성자</th><td><c:out value="${dt.NAME}"/></td>
					<th scope="row">작성일</th><td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${dt.REGI_DATE}"/></td>
					<th scope="row">조회수</th><td><c:out value="${dt.VIEWS}"/></td>
				</tr>
				<c:if test="${settingInfo.use_file == '1'}">
				<tr>
					<th scope="row">파일</th><td colspan="5"><itui:objectView itemId="file" multiFileHashMap="${multiFileHashMap}"/></td>
				</tr>
				</c:if>
				<c:set var="exceptIds" value="${fn:split('name,subject,file,contents',',')}"/>
				<itui:itemViewAll colspan="5" itemInfo="${itemInfo}" itemOrderName="${submitType}_order" exceptIds="${exceptIds}"/>
				<tr>
					<td class="cont" colspan="6">
						<itui:objectView itemId="contents"/>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btnTopFull">
			<div class="left">
				<a href="<c:out value="${URL_LIST}"/>" title="목록" class="btnTypeA fn_btn_write">목록</a>
			</div>
			<div class="right">
				<c:if test="${wrtAuth}">
				<a href="<c:out value="${URL_INPUT}"/>" title="등록" class="btnTypeA fn_btn_write">등록</a>
				</c:if>
				<c:if test="${mngAuth}">
				<a href="<c:out value="${URL_IDX_MODIFY}"/>" title="수정" class="btnTypeA fn_btn_modify">수정</a>
				<a href="<c:out value="${URL_IDX_DELETEPROC}"/>" title="삭제" class="btnTypeB fn_btn_delete">삭제</a>
				</c:if>
			</div>
		</div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>