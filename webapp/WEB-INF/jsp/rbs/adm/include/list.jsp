<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_techSupportSearchForm"/>
<c:set var="listFormId" value="fn_techSupportListForm"/>
<c:set var="inputWinFlag" value=""/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<!-- search -->
	<c:if test="${userTypeIdx ne '45' && userTypeIdx ne '46'}">
	<div class="tbMSearch">
		<form name="${searchFormId}" id="${searchFormId}" method="get"  action="<c:out value="${URL_DEFAULT_LIST}"/>">
			<input type="hidden" name="mId" value="${queryString.mId}">
			<fieldset>				
				<dl>
					<dt>소속 </dt>
					<dd style="width:80%">
						<select name="is_colgCd" id="s_colgCd" class="select" title="대학">
							<option value="">전체</option>
						</select>
						<select name="is_fcltSustCd" id="s_fcltSustCd" class="select" title="학부/학과">
							<option value="">전체</option>
						</select>
						<select name="is_mjCd" id="s_mjCd" class="select" title="전공">
							<option value="">전체</option>
						</select>					


<%-- 					<itui:objectSelectClassCustom itemId="mjCd" itemInfo="${itemInfo}" optnHashMap="${mjCdList}"/> --%>

					</dd>
					<itui:searchFormItemIn itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" isUseRadio="${isUseRadio}" isUseMoreItem="${isUseMoreItem}"/>	
				</dl>
			</fieldset>
			<p style="padding-bottom: 15px;">
				<input type="submit" id="fn_btn_search" class="btnSearch" value="검색" title="검색">
			</p>
		</form>
	</div>		
	</c:if>
	<!-- //search -->
		
	<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="80px" />
				<col width="80px" />
				<col width="100px"/>
				<col width="200px" />
				<col width="100px" />
				<col width="200px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">No</th>
				<th scope="col">교수정보제공등록</th>
				<th scope="col">소속</th>
				<th scope="col">이름</th>
				<th scope="col">사번</th>
				<th scope="col"></th>
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="6" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listMajorIdxName" value="${settingInfo.idx_major_name}"/>
				<c:set var="listYearIdxName" value="${settingInfo.idx_year_name}"/>
				<c:set var="listColumnName" value="${settingInfo.idx_column}"/>				
				<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<tr>
					<td class="num">${listNo}</td>
					<td class="num">
<%-- 						<a href="<c:out value="${URL_MODIFY}"/>&mjCd=${listDt.MJ_CD}&year=${listDt.YY}" class="btnTypeF fn_btn_modify">수정</a> --%>
						<a href="<c:out value="${URL_MODIFY}"/>&staffNo=${listDt.STAFF_NO}&deptCd=${listDt.POSI_DEPT_CD}" class="btnTypeF fn_btn_modify">등록</a>
					</td>
					<td>						
						<c:out value="${listDt.DEPT_KOR_NM}"/>
					</td>
					<td>
					<a href="#;" onclick="window.open('${URL_PREVIEW}&staffNo=${listDt.STAFF_NO}', '_blank', 'width=1200, height=1200')"><c:out value="${listDt.KOR_NM}"/></a>
					</td>
					<td>
						<c:out value="${listDt.STAFF_NO }"/>
					</td>
					<td>
					<a href="#;" onclick="window.open('${URL_PREVIEW}&staffNo=${listDt.STAFF_NO}', '_blank', 'width=1200, height=1200')"  class="btnTypeA fn_btn_modify">
						미리보기
					</a>
					</td>
				</tr>
				<c:set var="listNo" value="${listNo - 1}"/>
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
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if> --%>