<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_boardSearchForm"/>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:set var="listFormId" value="fn_boardListForm"/>
<c:set var="inputWinFlag" value="_list"/>										
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:set var="isNoMemberAuthPage" value="${elfn:isNoMemberAuthPage('WRT')}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<!-- search -->
		<itui:searchFormItem formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
		<!-- //search -->
	
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data" style="margin:10px 0 5px 0;">
			<table class="tbWriteA" summary="<itui:objectItemName itemInfo="${itemInfo}" itemId="contents"/>을 제공하는 표">
				<caption>
				글쓰기 서식
				</caption>
				<colgroup>
					<col style="width:120px;" />
					<col />
				</colgroup>
				<tbody>
					<itui:itemInputAll itemInfo="${itemInfo}" itemOrderName="write_order"/>
				</tbody>
			</table>
			<div class="btnCenter">
				<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
				<a href="<c:out value="${URL_INPUT}"/>" title="취소" class="btnTypeB fn_btn_write_list">취소</a>
			</div>
		</form>
		
		<%@ include file="../../common/listInfo.jsp"%>
		
		<c:set var="colSpan" value="6"/>
		<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
		<c:set var="useNotice" value="${settingInfo.use_notice eq '1'}"/>
		<table class="tbListA" summary="<spring:message code="item.no.name"/>, <spring:message code="item.modify.name"/>, <itui:objectItemName itemInfo="${itemInfo}" itemId="contents"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>을 제공하는 표">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col />
				<col width="100px" />
				<col width="70px" />
				<col width="70px" />
			</colgroup>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="${colSpan}" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<c:set var="listKey" value="${listDt[settingInfo.idx_column]}"/>
				<c:set var="isNotice" value="${useNotice and listDt.NOTICE eq '1'}"/>
				<tr<c:if test="${isNotice}"> style="background-color:#f9f0f9;"</c:if>>
					<td class="tlt" style="white-space:pre-line !important;">
						<c:if test="${settingInfo.use_list_img eq '1' and !empty listDt.LISTIMG_SAVED_NAME}"><img src="<c:out value="${URL_IMAGE}"/>&type=s&id=<c:out value="${elfn:imgNSeedEncrypt(listDt.LISTIMG_SAVED_NAME)}"/>" alt="<c:out value="${listDt.LISTIMG_TEXT}"/>"/></c:if>
						<c:out value="${listDt.CONTENTS}"/>
						<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(listDt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>
					</td>			  
					<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.REGI_NAME)}"/><br/>(<fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/>)</td>
					<c:choose>
					<c:when test="${elfn:isDisplayAuth(crtMenu.fn_idx, listKey, 0, false, listDt['REGI_IDX'], listDt['MEMBER_DUP'], listDt['PWD'])}">
					<td><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF fn_btn_modify${inputWinFlag}"<c:if test="${isNoMemberAuthPage}"> data-nm="<c:out value="${listKey}"/>"</c:if>><spring:message code="item.modify.name"/></a></td>
					<td><a href="<c:out value="${URL_DELETEPROC}"/>&${listIdxName}=${listKey}" class="btnTypeF fn_btn_delete${inputWinFlag}"<c:if test="${isNoMemberAuthPage}"> data-nm="<c:out value="${listKey}"/>"</c:if>><spring:message code="item.delete.name"/></a></td>
					</c:when>
					<c:otherwise>
					<td></td>
					<td></td>
					</c:otherwise>
					</c:choose>
				</tr>
				<c:if test="${!isNotice}"><c:set var="listNo" value="${listNo - 1}"/></c:if>
				</c:forEach>
			</tbody>
		</table>
		
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
		</div>
		<!-- //paging -->		
		<%-- 비회원글쓰기/댓글쓰기권한 : 비밀번호 확인  --%>
		<%@ include file="../../common/password.jsp"%>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>