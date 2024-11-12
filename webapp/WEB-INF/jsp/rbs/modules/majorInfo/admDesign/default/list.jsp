<%@ include file="../../../../include/commonTop.jsp"%>
<%-- <%@ include file="../../../../include/commonEnc.jsp"%> --%>
<link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/>
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
		<form name="${searchFormId}" id="${searchFormId}" method="post"  action="<c:out value="${URL_DEFAULT_LIST}"/>">
			<input type="hidden" name="mId" value="${queryString.mId}">
			<fieldset>				
				<dl>
					<dt>소속 </dt>
					<dd style="width:50%">
						<select name="college1" id="college1" class="select" title="대학" >
							<option value="" selected>대학</option>
                            <c:forEach var="listDt" items="${collegeList }">
                            	<option value="${listDt.COLG_CD }" <c:if test="${param.univ == listDt.COLG_CD }">selected</c:if>>${listDt.COLG_NM }</option>
                            </c:forEach>
						</select>
                        <select name="college2" class="form-select" id="college2" disabled>
                            <option value="" selected>학부(과)</option>
                        </select>
                    
                        <select name="college3" class="form-select" id="college3" disabled>
                            <option value="" selected>전공</option>
                        </select>

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
		
	<div class="btnTopFull">
		<div class="right"><a class="btnTFW fn_btn_write" href="<c:out value="${URL_INPUT}"/>" title="추가" class="btnTW fn_btn_write${inputWinFlag}">등록</a></div>
	</div>
	
	<form id="${listFormId}" name="${listFormId}" method="post" action="${URL_INPUT}&mode=m" target="list_target">
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="80px" />				
				<!-- <col width="80px" /> -->
				<col width="200px" />
				<col width="80px" />
<!-- 				<col width="100px" /> -->
				<col width="100px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">No</th>
				<!-- <th scope="col">학년도</th> -->
				<th scope="col">소속</th>
				<th scope="col">전공정보</th>
<!-- 				<th scope="col">수정자</th> -->
				<th scope="col">최근 업데이트</th>
			</tr>
			</thead>
			<tbody class="alignC">
				<c:if test="${empty list}">
				<tr>
					<td colspan="5" class="bllist"><spring:message code="message.no.list"/></td>
				</tr>
				</c:if>
				<c:set var="listMajorIdxName" value="${settingInfo.idx_major_name}"/>
				<c:set var="listYearIdxName" value="${settingInfo.idx_year_name}"/>
				<c:set var="listColumnName" value="${settingInfo.idx_column}"/>				
				<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
				<c:forEach var="listDt" items="${list}" varStatus="i">
				<tr>
					<td class="num">${listNo}</td>
					<%-- <td><c:out value="${listDt.YY }">년</c:out></td> --%>
					<td>
					<c:if test="${mngAuth || wrtAuth && listDt.AUTH_MNG == '1'}">
						<a href="#;" onclick="window.open('${URL_PREVIEW}&${listMajorIdxName}=${listDt.MAJOR_CD}&${listYearIdxName}=${listDt.YY}', '_blank', 'width=2000, height=2000')"><c:out value="${listDt.KOR_NM}"/>
							<c:out value="${listDt.COLG_NM}"/> > <c:out value="${listDt.DEPT_NM}"/>							
							<c:if test="${listDt.DEPT_CD ne listDt.MAJOR_CD}">
								 > <c:out value="${listDt.MAJOR_NM_KOR}"/>
							</c:if>
						</a>
					</c:if>
					</td>
					<td class="num">
<%-- 						<a href="<c:out value="${URL_MODIFY}"/>&${listMajorIdxName}=${listDt.MJ_CD}&${listYearIdxName}=${listDt.YY}" class="btnTypeF fn_btn_modify">수정</a> --%>
<%-- 						<input type="hidden" name="mjCd" value="${listDt.MJ_CD }"/> --%>
<%-- 						<input type="hidden" name="year" value="${listDt.YY }"/> --%>
<!-- 						<input type="submit" class="btnTypeF fn_btn_submit" value="수정" title="수정"/> -->
						
						<a href="javascript:void(0)" onclick='location.href="<c:out value="${URL_MODIFY}"/>&${listMajorIdxName}=${listDt.MAJOR_CD}"' class="btnTypeF fn_btn_modify">수정</a>
					</td>
<%-- 					<td><p>${listDt.LAST_MODI_ID}</p></td> --%>
					<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.LAST_MODI_DATE}"/></td>
				</tr>
				<c:set var="listNo" value="${listNo + 1}"/>
				</c:forEach>
			</tbody>
		</table>
		</form>
		
		<!-- paging -->
		<div class="paginate mgt15">
			<pgui:pagination listUrl="${URL_PAGE_LIST}&college1=${param.college1 }&college2=${param.college2 }&college3=${param.college3 }&is_majorNmKor=${param.is_majorNmKor }" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
		</div>
		<!-- //paging -->
	</div>
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if> --%>