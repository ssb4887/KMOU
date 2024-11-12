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
          	<div class="sub_background commu_bg">
				<section class="inner">
					<h3 class="title fw-bolder text-center text-white">자료실</h3>
					<p class="sub_title_script">학사에 관련된 자료를 확인할 수 있습니다.</p>
				</section>
			</div>
			<!-- 본문 -->
			<section class="inner mt-4">
				<section class="inner ntc_view_title">
					<h2 class="noti_title text-center"><c:out value="${dt.SUBJECT}"/></h2>
				</section>
				<div class="table-view noti_view">
<!-- 					<div class="heading-line"> -->
<!--                         <p class="title"> -->
	                        <c:if test="${settingInfo.use_notice eq '1' and dt.NOTICE eq '1'}">[<spring:message code="item.notice.name"/>] </c:if>
							<c:if test="${!empty dsetCateListId}">
								<c:set var="dsetCateListVal"><itui:objectView itemId="${dsetCateListId}"/></c:set>
								<c:if test="${!empty dsetCateListVal}">[${dsetCateListVal}]</c:if>
							</c:if>
							
<!-- 						</p> -->
<!--                         <div class="heading-etc"> -->
<%--                             <span class="txt"><c:out value="${dt.NAME}"/></span> --%>
<%--                             <span class="txt"><fmt:formatDate pattern="yyyy-MM-dd" value="${dt.REGI_DATE}"/></span> --%>
<%--                             <c:if test="${settingInfo.use_file == '1'}"> --%>
<%--                             	<itui:objectViewBoard itemId="file" multiFileHashMap="${multiFileHashMap}"/> --%>
<%--                             </c:if> --%>
<!--                         </div> -->
<!--                     </div> -->
						<table class="table noti_view">
							<caption class="blind">자료실 view 정보</caption>
							<colgroup>
								<col width="15%" />
								<col width="35%" />
								<col width="15%" />
								<col width="35%" />
							</colgroup>
							<tbody>
								<tr>
									<th scope="row" class="align-middle py-3">작성자</th>
									<td class="wirt text-start align-middle py-3 "><c:out value="${dt.NAME}"/></td>
									<th scope="row" class="align-middle py-3">작성일</th>
									<td class="date text-start align-middle py-3"><fmt:formatDate pattern="yyyy-MM-dd" value="${dt.REGI_DATE}"/></td>
								</tr>
								<tr class="qna_hits">
									<th scope="row" class="align-middle py-3">조회수</th>
									<td colspan="3" class="text-start align-middle py-3"><c:out value="${dt.VIEWS}"/></td>
								</tr>
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
                    <div class="content-line c_bx">
					    <itui:objectView itemId="contents"/>
					</div>
					<div class="pre-next-line">
						<c:if test="${!empty viewList}">
							<%@ include file="../../common/viewList.jsp"%>
						</c:if>
					</div>
				</div>
				<div class="noti-btn-line">
	                <a href="<c:out value="${URL_LIST}"/>" title="목록" class="btn_st">목록</a>
	            </div>
			</section>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>