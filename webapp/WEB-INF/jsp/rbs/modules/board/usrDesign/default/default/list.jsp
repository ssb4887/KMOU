<%@ include file="../../../../../include/commonTop.jsp"%>
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
	<div id="cms_board_article">
		<%@ include file="../../common/listTab.jsp"%>
		<c:if test="${settingInfo.dset_list_newcnt > 0}">
			<%@ include file="../../common/newList.jsp"%>
		</c:if>
		<!-- search -->
		<itui:searchFormItem formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		<!-- //search -->
		<%@ include file="../../common/listInfo.jsp"%>
		<c:set var="colSpan" value="5"/>
		<c:set var="subject" value="subject"/>
		<c:set var="replyState" value="replyState"/>
		<c:set var="useQna" value="${settingInfo.use_qna eq '1'}"/>
		<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
		<c:set var="useNotice" value="${settingInfo.use_notice eq '1'}"/>
		<c:set var="dsetCateListId" value="${settingInfo.dset_cate_list_id}"/>
		<table class="tbListA<c:if test="${settingInfo.use_list_img eq '1'}"> gallery</c:if>" summary="<spring:message code="item.no.name"/>, <spring:message code="item.modify.name"/>, <itui:objectItemName itemInfo="${itemInfo}" itemId="${subject}"/>, <c:if test="${useQna}"><itui:objectItemName itemInfo="${itemInfo}" itemId="${replyState}"/>, </c:if><c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>를 제공하는 표">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="60px" />
				<col />
				<c:if test="${useQna}"><col width="60px" /></c:if>
				<c:if test="${useFile}"><col width="60px" /><c:set var="colSpan" value="${colSpan + 1}"/></c:if>
				<col width="100px" />
				<col width="100px" />
				<c:if test="${!useQna}"><col width="90px" /></c:if>
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><spring:message code="item.no.name"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="${subject}"/></th>
				<c:if test="${useQna}"><th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="${replyState}"/></th></c:if>
				<c:if test="${useFile}"><th scope="col"><spring:message code="item.file.name"/></th></c:if>
				<th scope="col"><spring:message code="item.reginame1.name"/></th>
				<th scope="col"><spring:message code="item.regidate1.name"/></th>
				<c:if test="${!useQna}"><th scope="col" class="end"><spring:message code="item.board.views.name"/></th></c:if>
				<!-- 마지막 th에 class="end" -->
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="${colSpan}" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt.BRD_IDX}"/>
				<c:set var="isNotice" value="${useNotice and listDt.NOTICE eq '1'}"/>
				<c:set var="isListImg" value="${settingInfo.use_list_img eq '1' and !empty listDt.LISTIMG_SAVED_NAME}"/>
				<%-- 비밀번호 입력창 display 여부 --%>
				<c:set var="isDisplaySecretPassAuth" value="${settingInfo.use_secret eq '1' and listDt.SECRET eq '1' && elfn:isDisplaySecretPassAuth((settingInfo.use_reply eq '1' or settingInfo.use_qna eq '1') and listDt.RE_LEVEL > 1, listDt.REGI_IDX, listDt.MEMBER_DUP, listDt.P_REGI_IDX, listDt.P_MEMBER_DUP)}"/>
				<tr<c:if test="${isNotice}"> style="background-color:#f9f0f9;"</c:if>>
					<td class="num">
						<c:choose>
							<c:when test="${isNotice}"><spring:message code="item.notice.name"/></c:when>
							<c:otherwise>${listNo}</c:otherwise>
						</c:choose>
					</td>
					<td class="tlt">
						<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="상세보기" class="fn_btn_view"<c:if test="${listDt.RE_LEVEL > 1}"> style="padding-left:${(listDt.RE_LEVEL - 1) * 10}px;"</c:if><c:if test="${isDisplaySecretPassAuth}"> data-nm="<c:out value="${listKey}"/>"</c:if>>
							<c:if test="${isListImg}"><span class="photo"><img src="<c:out value="${URL_IMAGE}"/>&type=s&id=<c:out value="${elfn:imgNSeedEncrypt(listDt.LISTIMG_SAVED_NAME)}"/>" alt="<c:out value="${listDt.LISTIMG_TEXT}"/>"/></span></c:if>
							<c:if test="${!empty dsetCateListId}">
								<c:set var="dsetCateListVal"><itui:objectView itemId="${dsetCateListId}" objDt="${listDt}"/></c:set>
								<c:if test="${!empty dsetCateListVal}">[<c:out value="${dsetCateListVal}"/>]</c:if>
							</c:if>
							<c:if test="${isListImg}"><span class="subject"><span></c:if>
								<c:out value="${listDt.SUBJECT}"/>
							<c:if test="${isListImg}"></span></c:if>
							<c:if test="${settingInfo.use_secret eq '1' and listDt.SECRET eq '1'}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_secret.gif"/>" alt="비밀글"/></c:if>
							<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(listDt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>
							<c:if test="${isListImg}"></span></c:if>
						</a>
					</td>	 
					<c:if test="${useQna}"><td><itui:objectView itemId="${replyState}" objDt="${listDt}"/></td></c:if>		  
					<c:if test="${useFile}"><td><c:if test="${listDt.FILE_CNT > 0}"><img src="<c:out value="${contextPath}${imgPath}/common/ico_file.gif"/>" alt="파일"/></c:if></td></c:if>
					<td><c:out value="${listDt.NAME}"/></td>		
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
					<c:if test="${!useQna}"><td class="num" id="fn_brd_views<c:out value="${listDt.BRD_IDX}"/>"><c:out value="${listDt.VIEWS}"/></td></c:if>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
				</c:forEach>
			</tbody>
		</table>
		
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
		</div>
		<!-- //paging -->
		<%@ include file="../../common/listBtns.jsp"%>
		<%-- 비회원글쓰기/댓글쓰기권한 : 비밀번호 확인  --%>
		<%@ include file="../../common/password.jsp"%>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>