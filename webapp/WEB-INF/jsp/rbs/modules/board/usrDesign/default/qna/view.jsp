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
					<h3 class="title fw-bolder text-center text-white">Q&amp;A</h3>
					<p class="sub_title_script">Q&amp;A을 확인할 수 있습니다.</p>
				</section>
			</div>
			<!-- 본문 -->
			<section class="inner mt-4">
				<h2 class="qna_title text-center py-3 px-2"><c:out value="${dt.SUBJECT}"/></h2>
			</section>
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
						<td class="wirt text-start align-middle py-3 "><c:out value="${dt.NAME}"/></td>
						<th scope="row" class="align-middle py-3">작성일</th>
						<td class="date text-start align-middle py-3"><fmt:formatDate pattern="yyyy-mm-dd" value="${dt.REGI_DATE}"/></td>
					</tr>
					<tr class="qna_hits">
						<th scope="row" class="text-center align-middle py-3">조회수</th>
						<td colspan="3" class="text-start align-middle py-3"><c:out value="${dt.VIEWS}"/></td>
					</tr>
					<tr class="qna_attc">
						<th scope="row" class="text-center align-middle">첨부파일</th>
						<td colspan="3" class="text-start align-middle attach_file px-3 px-md-2">
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
				<div class="answer_section">
					<h4 class="title">답변</h4>
					ㅇ 지원자격<br />- 다음의 조건을 모두 충족한 대학생(재학생)<br /> ① 대한민국 국적자로 최종학력이 고졸인 자<br /> ※ 전문학사 취득자가 전공심화과정 및 4년제 대학에 신편입하는 경우, ‘전문학사 취득 전’ 산업체 재직기간(군 복무기간 미포함) 2년을 별도로 충족하면 장학금 지원 가능 증빙서류 제출 필요
				</div>
			</div>
            <div class="table qna_view">
				<div class="table-view">
					<div class="heading-line">
                        <p class="title">
	                        <c:if test="${settingInfo.use_notice eq '1' and dt.NOTICE eq '1'}">[<spring:message code="item.notice.name"/>] </c:if>
							<c:if test="${!empty dsetCateListId}">
								<c:set var="dsetCateListVal"><itui:objectView itemId="${dsetCateListId}"/></c:set>
								<c:if test="${!empty dsetCateListVal}">[${dsetCateListVal}]</c:if>
							</c:if>
							<c:out value="${dt.SUBJECT}"/>
							<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(dt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>
						</p>
                        <div class="heading-etc">
                            <span class="txt"><c:out value="${dt.NAME}"/></span>
                            <span class="txt"><fmt:formatDate pattern="yyyy-MM-dd" value="${dt.REGI_DATE}"/></span>
                            <c:if test="${settingInfo.use_file == '1'}">
                            	<itui:objectViewBoard itemId="file" multiFileHashMap="${multiFileHashMap}"/>
                            </c:if>
                        </div>
                    </div>
                    <div class="content-line">
					    <itui:objectView itemId="contents"/>
					</div>
					<div class="pre-next-line">
						<c:if test="${!empty viewList}">
							<%@ include file="../../common/viewList.jsp"%>
						</c:if>
					</div>
				</div>
			</div>
			<div class="noti-btn-line">
				<a href="<c:out value="${URL_LIST}"/>" title="목록" class="btn_st">목록</a>
<!--                 <button type="button" class="btn-large btn-primary" onclick="location.href='community_notify_list.html'">목록</button> -->
            </div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>