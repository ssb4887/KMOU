<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
	<div id="cms_trebook_article">
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
				<c:set var="idxKey" value="${dt.TRE_IDX}"/>
				<tr>
					<td class="img">
						<c:set var="fImgText" value="${dt.FIMG_TEXT}"/>
						<c:if test="${empty dt.FIMG_TEXT}"><c:set var="fImgText" value="${dt.FIMG_ORIGIN_NAME}"/></c:if>
						<img src="<c:out value="${URL_IMAGE}"/>&id=${elfn:imgNSeedEncrypt(dt.FIMG_SAVED_NAME)}" alt="<c:out value="${fImgText}"/>" class="treImg"/>
					</td>
					<td class="tlt">
						<div class="treIssue">
							[ <itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${dt}"/>
							<c:if test="${!empty settingInfo.use_part_type && settingInfo.use_part_type == 1}">
								<span><itui:objectView itemId="trePartType" itemInfo="${itemInfo}" objDt="${dt}" optnHashMap="${optnHashMap}"/></span>
							</c:if> ]
							<c:if test="${!empty settingInfo.use_multi_cate && settingInfo.use_multi_cate == 1}">
								<span><itui:objectView itemId="category" itemInfo="${itemInfo}" objDt="${dt}" optnHashMap="${optnHashMap}"/></span>
							</c:if>
						</div>
						<div class="treContents"><itui:objectView itemId="contents" itemInfo="${itemInfo}" objDt="${dt}"/></div>
						<div>
							<c:set var="fileText" value="${dt.PDF_FILE_TEXT}"/>
							<c:if test="${empty dt.PDF_FILE_TEXT}"><c:set var="fileText" value="${dt.PDF_FILE_ORIGIN_NAME}"/></c:if>
					<c:choose>
						<c:when test="${settingInfo.use_pdf_upload == 1}">
							<a href="<c:out value="${URL_DOWNLOAD}"/>&${idxName}=${idxKey}&itId=pdfFile" title="<c:out value="${fileText}"/>">
						</c:when>
						<c:otherwise>
							<a href="<c:out value="${dt.FIMG_TEXT}"/> title="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${dt}"/> PDF다운로드">
						</c:otherwise>
					</c:choose>
								<img src="<c:out value="${contextPath}${moduleResourcePath}/images/btn_pdfDown.jpg"/>" alt="<itui:objectView itemId="treIssue" itemInfo="${itemInfo}" objDt="${dt}"/> PDF다운로드" />
							</a>
						</div>
					</td>	
				</tr>
			</tbody>
		</table>
		
		<c:if test="${settingInfo.use_cont_list == 1}">
		<h4>내용</h4>
		<table class="tbListTreA" summary="<c:out value="${settingInfo.list_title}"/> 내용 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 내용 목록</caption>
			<colgroup>
				<col width="100px" />
				<col/>
				<col width="60px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">번호</th>
				<th scope="col">제목</th>
				<th scope="col" class="end">파일</th>
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty conList}">
				<tr>
					<td class="bllist" colspan="3"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:forEach var="listDt" items="${conList}" varStatus="i">
				<c:set var="listKey" value="${listDt.TRE_IDX}"/>
				<c:set var="conKey" value="${listDt.CON_IDX}"/>
				<c:set var="conSubject" value="${listDt.CON_SUBJECT}"/>
				<tr>
					<td class="num"><c:out value="${listDt.CON_NUM}"/></td>
					<td class="tlt"><c:out value="${listDt.CON_SUBJECT}"/></td>			  
					<td>
					<c:choose>
						<c:when test="${settingInfo.use_pdf_upload == 1 && !empty listDt.CON_PDF_FILE_SAVED_NAME}">
							<a href="<c:out value="${URL_DOWNLOAD}"/>&${listIdxName}=${listKey}&conIdx=${conKey}&itId=conPdfFile" title="<c:out value="${conSubject}"/> 다운로드"/>
								<img src="<c:out value="${contextPath}${imgPath}/common/ico_file.gif"/>" alt="${conSubject} 다운로드"/>
							</a>
						</c:when>
						<c:when test="${settingInfo.use_pdf_upload == 0 && !empty listDt.CON_LINK_URL}">
							<a href="<c:out value="${listDt.FIMG_TEXT}"/> title="<c:out value="${conSubject}"/> 다운로드">
								<img src="<c:out value="${contextPath}${imgPath}/common/ico_file.gif"/>" alt="<c:out value="${conSubject}"/> 다운로드"/>
							</a>
						</c:when>
					</c:choose>	
					</td>
				</tr>
				<tr>
					<td class="cont" colspan="3">
						<div class="conContentG">
							<itui:objectView itemId="conContents" itemInfo="${moduleItem.con_item_info}" objDt="${listDt}"/>
						</div>
					</td>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</tbody>
		</table>
		</c:if>
		
		<div class="btnBottmFull">
			<div class="left">
				<a href="<c:out value="${URL_LIST}"/>" title="<c:out value="${settingInfo.list_title}"/> 목록" class="btnTypeA fn_btn_write">목록</a>
			</div>
		</div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>