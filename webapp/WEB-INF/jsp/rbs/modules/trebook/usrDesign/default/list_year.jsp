<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_boardSearchForm"/>
<c:set var="listFormId" value="fn_boardListForm"/>
<c:set var="btnModifyClass" value="fn_btn_modify"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_trebook_article">
		<div class="btnTopFull">	
			<!-- search -->
			<itui:searchFormItem divClass="tbSearch tbShowDt" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>	
			<!-- //search -->
		
			<!-- button -->			
			<%@ include file="../common/listBtns.jsp"%>
			<!-- //button -->
		</div>
		
<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
<c:set var="setTop" value="0"/>
<c:set var="strYear" value=""/>
<c:forEach var="listDt" items="${list}" varStatus="i">
	<c:if test="${i.first}">
		<div id="selectView">
			<table class="tbViewTreA" summary="<c:out value="${settingInfo.list_title}"/> 게시글 읽기">
				<caption>
				<c:out value="${settingInfo.list_title}"/> 게시글 읽기
				</caption>
				<colgroup>
					<col width="310px" />
					<col />
				</colgroup>
				<tbody class="alignC">
					<c:set var="idxName" value="${settingInfo.idx_name}"/>
					<c:set var="idxKey" value="${listDt.TRE_IDX}"/>
					<tr>
						<td class="img">
							<c:set var="fImgText" value="${listDt.FIMG_TEXT}"/>
							<c:if test="${empty listDt.FIMG_TEXT}"><c:set var="fImgText" value="${listDt.FIMG_ORIGIN_NAME}"/></c:if>
							<img id="viewFImg" src="<c:out value="${URL_IMAGE}"/>&id=${elfn:imgNSeedEncrypt(listDt.FIMG_SAVED_NAME)}" alt="<c:out value="${fImgText}"/>" class="treImg"/>
						</td>
						<td class="tlt">
							<div class="treIssue">
								<span class="trebookType" ><c:out value="${trebookType}"/></span>
								[ <span id="viewTreIssue"><itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/></span>
								<c:if test="${!empty settingInfo.use_part_type && settingInfo.use_part_type == 1}">
									<span id="viewTrePartType"><itui:objectView itemId="trePartType" itemInfo="${itemInfo}" objDt="${listDt}" optnHashMap="${optnHashMap}"/></span>
								</c:if> ]
								<c:if test="${!empty settingInfo.use_multi_cate && settingInfo.use_multi_cate == 1}">
									<span id="viewCategory"><itui:objectView itemId="category" itemInfo="${itemInfo}" objDt="${listDt}" optnHashMap="${optnHashMap}"/></span>
								</c:if>
							</div>
							<div id="viewTreContents" class="treContents"><itui:objectView itemId="contents" itemInfo="${itemInfo}" objDt="${listDt}"/></div>
							<div>
								<c:set var="fileText" value="${listDt.PDF_FILE_TEXT}"/>
								<c:if test="${empty listDt.PDF_FILE_TEXT}"><c:set var="fileText" value="${listDt.PDF_FILE_ORIGIN_NAME}"/></c:if>
						<c:choose>
							<c:when test="${settingInfo.use_pdf_upload == 1}">
								<a id="viewFileLink" href="<c:out value="${URL_DOWNLOAD}"/>&${idxName}=${idxKey}&itId=pdfFile" title="<c:out value="${fileText}"/>">
							</c:when>
							<c:otherwise>
								<a id="viewFileLink" href="<c:out value="${listDt.FIMG_TEXT}"/>" title="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/> PDF다운로드">
							</c:otherwise>
						</c:choose>
									<img id="viewFileImg" src="<c:out value="${contextPath}${moduleResourcePath}/images/btn_pdfDown.jpg"/>" alt="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/> PDF다운로드" />
								</a>
							</div>
						</td>	
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="divYearList">
			<ul class="yearList">
	</c:if>
					
	<c:if test="${strYear != listDt.TRE_ISSUE1}">
		<c:if test="${empty strYear || strYear == ''}"><li class="yearListLiFirst"></c:if>
		<c:if test="${!empty strYear && strYear != ''}"></ul></li><li class="yearListLi"></c:if>
		<c:set var="strYear" value="${listDt.TRE_ISSUE1}"/>
		<c:set var="setTop" value="1"/>
	</c:if>
					
	<c:if test="${setTop == 1}">
					<div class="treYearTit"><c:out value="${strYear}"/></div>
					
					<ul class="treYear">
				<c:set var="setTop" value="0"/>
		</c:if>
						<li class="treMonth">
							<c:set var="hKey" value="${listDt.TRE_IDX}"/>
							<input type="hidden" id="hTreIdx${hKey}" name="hTreIdx${hKey}" value="<c:out value="${listDt.TRE_IDX}"/>"/>
							<input type="hidden" id="hFImg${hKey}" name="hFImg${hKey}" value="<c:out value="${URL_IMAGE}"/>&id=${elfn:imgNSeedEncrypt(listDt.FIMG_SAVED_NAME)}"/>
							<c:set var="fImgText" value="${listDt.FIMG_TEXT}"/>
							<c:if test="${empty listDt.FIMG_TEXT}"><c:set var="fImgText" value="${listDt.FIMG_ORIGIN_NAME}"/></c:if>
							<input type="hidden" id="hFImgAlt${hKey}" name="hFImgAlt${hKey}" value="<c:out value="${fImgText}"/>"/>
							<input type="hidden" id="hTreIssue${hKey}" name="hTreIssue${hKey}" value="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/>"/>
							<c:if test="${!empty settingInfo.use_part_type && settingInfo.use_part_type == 1}">
								<input type="hidden" id="hTrePartType${hKey}" name="hTrePartType${hKey}" value="<itui:objectView itemId="trePartType" itemInfo="${itemInfo}" objDt="${listDt}" optnHashMap="${optnHashMap}"/>"/>
							</c:if>
							<c:if test="${!empty settingInfo.use_multi_cate && settingInfo.use_multi_cate == 1}">
								<input type="hidden" id="hCategory${hKey}" name="hCategory${hKey}" value="<itui:objectView itemId="category" itemInfo="${itemInfo}" objDt="${listDt}" optnHashMap="${optnHashMap}"/>"/>
							</c:if>
							<input type="hidden" id="hContents${hKey}" name="hContents${hKey}" value="<itui:objectView itemId="contents" itemInfo="${itemInfo}" objDt="${listDt}"/>"/>						
							<c:choose>
								<c:when test="${settingInfo.use_pdf_upload == 1}">
									<input type="hidden" id="hFileLink${hKey}" name="hFileLink${hKey}" value="<c:out value="${URL_DOWNLOAD}"/>&${idxName}=${hKey}&itId=pdfFile"/>								
								</c:when>
								<c:otherwise>
									<input type="hidden" id="hFileLink${hKey}" name="hFileLink${hKey}" value="<c:out value="${listDt.FIMG_TEXT}"/>"/>
								</c:otherwise>
							</c:choose>
							<c:set var="fileText" value="${listDt.PDF_FILE_TEXT}"/>
							<c:if test="${empty listDt.PDF_FILE_TEXT}"><c:set var="fileText" value="${listDt.PDF_FILE_ORIGIN_NAME}"/></c:if>
							<input type="hidden" id="hFileLinkTitle${hKey}" name="hFileLinkTitle${hKey}" value="<c:out value="${fileText}"/>/>
							<input type="hidden" id="hFileAlt${hKey}" name="hFileAlt${hKey}" value="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/> PDF다운로드"/>
							
							<a href="<c:out value="${hKey}"/>" title="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/> 상세보기" class="fn_view<c:if test="${i.first}">_on</c:if>" onclick="return false">
								+ <itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${listDt}"/>
							</a>
						</li>
		<c:if test="${i.last}">
					</ul>
				</li>
			</ul>
		</div>
	</c:if>
</c:forEach>

		<div class="pageYeare">
			<%@ include file="../common/listPage.jsp"%>
		</div>		
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>