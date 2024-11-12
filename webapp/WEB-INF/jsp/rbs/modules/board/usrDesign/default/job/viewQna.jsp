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
<script src="${contextPath}${jsAssetPath}/sub.js"></script>
<!-- 	<div id="cms_board_article"> -->
	<div class="container_wrap">
		<div class="sub_wrap">	
			<div class="sub_background commu_bg">
				<section class="inner">
					<h3 class="title fw-bolder text-center">Q&amp;A</h3>
				</section>
			</div>
			<!-- 본문 -->
			<section class="inner mt-4">
				<h2 class="qna_title text-center py-3 px-2"><c:out value="${dt.SUBJECT}"/></h2>
			
					<c:set var="isReply" value="false"/>
					<c:set var="reply" value=""/>
					<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
					<c:set var="useReply" value="${settingInfo.use_reply eq '1'}"/>
					<c:set var="dsetCateListId" value="${settingInfo.dset_cate_list_id}"/>
					<c:set var="exceptIdDStr">name,notice,subject,file,contents,listImg<c:if test="${!empty dsetCateListId}">,${dsetCateListId}</c:if></c:set>
					<c:forEach var="listDt" items="${pntList}" varStatus="i">
					<c:if test="${listDt.BRD_IDX != listDt.PNT_IDX}">
						<%/* 답변글인 경우 */%>
						<c:set var="reply" value="${listDt.CONTENTS}"/>
					</c:if>
					</c:forEach>
					<c:set var="itemOrderName" value="${itemOrderType}_order"/>
					<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
					<c:set var="itemObjs" value="${itemInfo.items}"/>
					<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
					<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
					<table class="table qna_view">
						<caption class="blind">qna view 정보</caption>
						<colgroup>
							<col width="15%" />
							<col width="35%" />
							<col width="15%" />
							<col width="35%" />
						</colgroup>
						<tbody>
							<tr>
								<th scope="row" class="align-middle py-3">작성자</th>
								<td class="wirt text-start align-middle "><c:out value="${dt.NAME}"/></td>
								<th scope="row" class="align-middle py-3">작성일</th>
								<td class="date text-start align-middle"><fmt:formatDate pattern="yyyy-MM-dd" value="${dt.REGI_DATE}"/></td>
							</tr>
							<%-- <tr class="qna_hits">
								<th scope="row" class="text-center align-middle py-3">조회수</th>
								<td colspan="3" class="text-start align-middle py-3"><c:out value="${dt.VIEWS}"/></td>
							</tr> --%>
							<tr class="qna_attc">
								<th scope="row" class="align-middle">첨부파일</th>
								<td colspan="3" class="text-start align-middle attach_file">
									<c:set var="fileList" value="${multiFileHashMap['file']}"/>
									<c:if test="${!empty fileList}">
									<c:set var="isImage" value="${itemObj['isimage'] == '1'}"/>		<%/* 이미지 여부 */%>
									<c:set var="keyItemId" value="${settingInfo.idx_name}"/>
									<c:set var="keyColumnId" value="${settingInfo.idx_column}"/>
									<c:set var="originColumnId" value="FILE_ORIGIN_NAME"/>
									<c:set var="savedColumnId" value="FILE_SAVED_NAME"/>
									<c:set var="textColumnId" value="FILE_TEXT"/>
									<c:forEach var="optnDt" items="${fileList}" varStatus="i">
										<p class="d-flex flex-row align-items-center ">
											<img src="../images/ico_add.png" alt="첨부파일 아이콘" />
											<c:choose>
												<c:when test="${!empty downloadUrl}"><a href="<c:out value="${downloadUrl}"/>&fidx=<c:out value="${optnDt.FLE_IDX}"/>&itId=<c:out value="${optnDt.ITEM_ID}"/>" class="text-truncate"><c:out value="${optnDt[originColumnId]}"/></a></c:when>
												<c:otherwise><a href="<c:out value="${URL_DOWNLOAD}"/>&${keyItemId}=<c:out value="${optnDt[keyColumnId]}"/>&fidx=<c:out value="${optnDt.FLE_IDX}"/>&itId=<c:out value="${optnDt.ITEM_ID}"/>" class="text-truncate"><c:out value="${optnDt[originColumnId]}"/></a></c:otherwise>
											</c:choose>
										</p>
									</c:forEach>
									</c:if>
								</td>
							</tr>
						</tbody>
					</table>
					<!--qna 컨텐츠 내용-->
					<div class="qna_container p-4">
						<c:out value="${dt.CONTENTS}"/>
						<c:if test="${!empty reply}">
						<div class="answer_section">
							<h4 class="title">답변</h4>
							<c:out value="${elfn:removeHTMLTag(reply,0) }"/>
							<c:set var="isReply" value="true"/> 
						</div>
						</c:if>
					</div>
				<div class="d-flex flex-row gap-3 justify-content-center mt-4">
<!-- 				<button type="button" class="qna_conf border-0 text-center text-white py-2">수정</button> -->
<!-- 				<button type="button" class="qna_dele border-0 text-center text-white py-2">삭제</button> -->
					<c:if test="${isReply eq 'false'}">
					 <a href="<c:out value="${URL_MODIFY}&brdIdx=${dt.BRD_IDX}"/>" title="수정" class="qna_conf border-0 text-center text-white py-2">수정</a>
					</c:if>
	                <a href="<c:out value="${URL_LIST}"/>" title="목록" class="qna_dele border-0 text-center text-white py-2">목록</a>
	            </div>
			</section>
        </div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false" /></c:if>