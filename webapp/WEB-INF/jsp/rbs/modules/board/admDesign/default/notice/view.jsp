<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="mwtAuth" value="${elfn:isAuth('MWT')}"/>
<c:set var="listFormId" value="fn_boardListForm"/>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="items" value="${itemInfo.items}"/>
<c:set var="useQna" value="${settingInfo.use_qna eq '1'}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
		<c:set var="useReply" value="${settingInfo.use_reply eq '1'}"/>
		<c:set var="dsetCateListId" value="${settingInfo.dset_cate_list_id}"/>
		<c:set var="exceptIdStr">name,notice,subject,file,contents<c:if test="${!empty dsetCateListId}">,${dsetCateListId}</c:if></c:set>
		<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
		<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
		<table class="tbViewA">
			<caption>
			공지사항 상세정보
			</caption>
			<colgroup>
				<col width="15%" />
				<col width="35%" />
				<col width="15%" />
				<col width="35%" />
			</colgroup>
			<thead>
				<tr>
					<th colspan="4" class="viewTlt"><c:out value="${dt.NTT_SJ}"/></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th scope="row" class="align-middle">작성자</th>
					<td class="wirt text-start align-middle py-3 "><c:out value="${dt.NCNM}"/></td>
					<th scope="row" class="align-middle">등록일</th>
					<td class="date text-start align-middle py-3"><fmt:formatDate pattern="YYYY.MM.dd" value="${dt.REG_DT}"/></td>
				</tr>
				<c:set var="fileList" value="${multiFileHashMap['file']}"/>
				<tr>
					<td class="c_bx" colspan="4">
						<div>
							<c:if test="${!empty fileList}">
							<c:forEach var="optnDt" items="${fileList}" varStatus="i">
								<c:if test="${optnDt.FILE_TY == 'img' }">
								<a href="https://www.kmou.ac.kr<c:out value="${optnDt.FLPTH}"/>" title="<c:out value="${dt.NTT_SJ}"/>(확대)" data-lightbox="picImg">
									<img src="https://www.kmou.ac.kr<c:out value="${optnDt.FLPTH}"/>" alt="<c:out value="${dt.NTT_SJ}"/>">
								</a>
								</c:if>
							</c:forEach>
							</c:if>
						</div>
						<%-- <c:out value="${elfn:replaceHtmlY(dt.NTT_CN)}"/> --%>
						${dt.NTT_CN }
					</td>
				</tr>
				<tr class="qna_attc">
					<th scope="row" class="text-center align-middle">첨부파일</th>
					<td colspan="3" class="text-start align-middle attach_file px-3 px-md-2">
						<c:if test="${!empty fileList}">
						<c:forEach var="optnDt" items="${fileList}" varStatus="i">
							<p class="d-flex flex-row align-items-center ">
								<img src="/RBISADM/images/ico_add.png" alt="첨부파일 아이콘" />
								<c:if test="${!empty optnDt.DWLD_URL}"><a href="https://www.kmou.ac.kr/common/nttFileDownload.do?fileKey=<c:out value="${optnDt.DWLD_URL}"/>" class="text-truncate"><c:out value="${optnDt.FILE_NM}"/></a></c:if>
							</p>
						</c:forEach>
						</c:if>
					</td>
				</tr>
				<tr>
					<th scope="row" class="text-center align-middle">게시 설정 기간</th>
					<td colspan="3" class="text-start align-middle attach_file px-3 px-md-2">
						<c:if test="${!empty dt.NTCE_BGNDE}"><fmt:formatDate pattern="yyyy/MM/dd" value="${dt.NTCE_BGNDE}"/>&nbsp;~&nbsp;</c:if>
						<c:if test="${!empty dt.NTCE_ENDDE}"><fmt:formatDate pattern="yyyy/MM/dd" value="${dt.NTCE_ENDDE}"/></c:if>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btnTopFull">
			<div class="right"><a href="<c:out value="${URL_LIST}"/>" title="목록" class="btnTypeA fn_btn_write">목록</a></div>
        </div>
		<!--  ul class="goArticle">
           	<c:forEach var="listDt" items="${list }" varStatus="i">
           		<c:choose>
           			<c:when test="${len eq '2'}">
           				<c:choose>
       						<c:when test="${i.first }">
								<li class="goPrev"><a href="/web/board/view.do?mId=39&page=1&NTT_SN=${listDt.NTT_SN }"><strong>이전글</strong><span>${listDt.NTT_SJ }</span></a></li>
							</c:when>
       						<c:when test="${i.last }">
       							<li><a href="/web/board/view.do?mId=39&page=1&NTT_SN=${listDt.NTT_SN }"><strong>다음글</strong><span>${listDt.NTT_SJ }</span></a></li>
							</c:when>
							<c:otherwise></c:otherwise>
       					</c:choose>
					</c:when>
           			<c:when test="${len eq '1'}">
           				<c:choose>
       						<c:when test="${listDt.RN eq '2' }">
								<li class="goPrev"><a href="/web/board/view.do?mId=39&page=1&NTT_SN=${listDt.NTT_SN }"><strong>이전글</strong><span>${listDt.NTT_SJ }</span></a></li>
							</c:when>
       						<c:when test="${listDt.RN ne '2' }">
       							<li><a href="/web/board/view.do?mId=39&page=1&NTT_SN=${listDt.NTT_SN }"><strong>다음글</strong><span>${listDt.NTT_SJ }</span></a></li>
							</c:when>
							<c:otherwise></c:otherwise>
       					</c:choose>
					</c:when>
           			<c:otherwise></c:otherwise>
           		</c:choose>
           		
           	</c:forEach>
           </ul -->
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>