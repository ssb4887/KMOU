<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_boardSearchForm"/>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:set var="listFormId" value="fn_boardListForm"/>
<c:set var="inputWinFlag" value="_list"/>										
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
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
				</tbody>
			</table>
			<div class="btnCenter">
				<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
				<a href="#" title="취소" class="btnTypeB fn_btn_reset">취소</a>
			</div>
		</form>
		
		<!-- button -->
		<div class="btnTopFull">
			<%@ include file="../../../../../adm/include/module/listBtns.jsp"%>
		</div>
		<!-- //button -->
		<c:set var="colSpan" value="6"/>
		<c:set var="useFile" value="${settingInfo.use_file eq '1'}"/>
		<c:set var="useNotice" value="${settingInfo.use_notice eq '1'}"/>
		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="tbListA" summary="<spring:message code="item.no.name"/>, <spring:message code="item.modify.name"/>, <itui:objectItemName itemInfo="${itemInfo}" itemId="contents"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>을 제공하는 표">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="50px" />
				<col width="60px" />
				<col width="70px" />
				<col />
				<c:if test="${useFile}"><col width="60px" /><c:set var="colSpan" value="${colSpan + 1}"/></c:if>
				<col width="100px" />
				<col width="100px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
				<th scope="col"><spring:message code="item.no.name"/></th>
				<th scope="col"><spring:message code="item.modify.name"/></th>
				<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="contents"/></th>
				<c:if test="${useFile}"><th scope="col"><spring:message code="item.file.name"/></th></c:if>
				<th scope="col"><spring:message code="item.reginame1.name"/></th>
				<th scope="col" class="end"><spring:message code="item.regidate1.name"/></th>
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
				<c:set var="listKey" value="${listDt[settingInfo.idx_column]}"/>
				<c:set var="isNotice" value="${useNotice and listDt.NOTICE eq '1'}"/>
				<tr<c:if test="${isNotice}"> style="background-color:#f9f0f9;"</c:if>>
					<td><input type="checkbox" name="select" title="<spring:message code="item.select"/><c:out value="${listNo}"/>" value="${listKey}"/></td>
					<td class="num">
						<c:choose>
							<c:when test="${isNotice}"><spring:message code="item.notice.name"/></c:when>
							<c:otherwise>${listNo}</c:otherwise>
						</c:choose>
					</td>
					<td><c:if test="${elfn:isDisplayAuth(usrCrtMenu.fn_idx, listKey, 0, false , listDt['REGI_IDX'], listDt['MEMBER_DUP'], listDt['PWD'])}"><a href="<c:out value="${URL_MODIFY}"/>&${listIdxName}=${listKey}" class="btnTypeF fn_btn_modify${inputWinFlag}">수정</a></c:if></td>
					<td class="tlt" style="white-space:pre-line !important;">
						<c:if test="${settingInfo.use_list_img eq '1' and !empty listDt.LISTIMG_SAVED_NAME}"><img src="<c:out value="${URL_IMAGE}"/>&type=s&id=<c:out value="${elfn:imgNSeedEncrypt(listDt.LISTIMG_SAVED_NAME)}"/>" alt="<c:out value="${listDt.LISTIMG_TEXT}"/>"/></c:if>
						<c:out value="${listDt.CONTENTS}"/>
						<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(listDt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>
					</td>			  
					<c:if test="${useFile}"><td><c:if test="${listDt.FILE_CNT > 0}"><img src="<c:out value="${contextPath}${imgPath}/common/ico_file.gif"/>" alt="파일"/></c:if></td></c:if>
					<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.REGI_NAME)}"/></td>		
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				</tr>
				<c:if test="${!isNotice}"><c:set var="listNo" value="${listNo - 1}"/></c:if>
				</c:forEach>
			</tbody>
		</table>
		</form>
		
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
		</div>
		<!-- //paging -->
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>