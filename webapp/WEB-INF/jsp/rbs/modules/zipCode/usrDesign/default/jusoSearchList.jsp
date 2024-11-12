<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<c:set var="searchFormId" value="fn_zipCodeSearchForm"/>
<jsp:include page="${jspSiteIncPath}/dialog_top.jsp" flush="false">
	<jsp:param name="javascript_page" value="${moduleJspRPath}/jusoSearchList.jsp"/>
	<jsp:param name="searchFormId" value="${searchFormId}"/>
	<jsp:param name="addCss" value="${contextPath}${moduleResourcePath}/style.css"/>
</jsp:include>
	<spring:message code="button.select" var="btnSelectName"/>
	<spring:message code="item.search.name" var="itemSearchName"/>
	<div id="cms_zipCode_article">
		<div class="popup-head cont_zip">
			<h1 class="titTypeB">우편번호검색</h1>
       		<p class="title">
       			<spring:message code="message.zipcode2.example.text1"/>
       			<br/><spring:message code="message.zipcode2.example.text2"/>
			</p>
		</div>
		<div class="popup-content cont_zip">
			<div class="post-search-area juso">
				<form id="${searchFormId}" name="${searchFormId}" method="get">
				<elui:hiddenInput inputInfo="${queryString}" exceptNames="${searchFormExceptParams}"/>
				<fieldset>
					<div class="fn_data">
						<input type="text" id="key" name="key" value="<c:out value="${queryString.key}"/>" class="inTxt" style="ime-mode:active;width:320px;" title="검색어"/></dd>
					</div>
					<div class="submit">
						<input type="submit" class="btnSearch" value="<c:out value="${itemSearchName}"/>" title="<c:out value="${itemSearchName}"/>"/>
					</div>
				</fieldset>
				</form>
			</div>
			<div class="post-search-result">
        	<c:if test="${!empty param.key}">
        		<table class="tbListA" id="resultData">
        			<colgroup>
        				<col width="70px"/>
        				<col/>
        			</colgroup>
        			<thead>
        				<tr>
        					<th>우편번호</th>
        					<th>주소</th>
        				</tr>
        			</thead>
        			<tbody class="alignC">
        			<c:if test="${empty list}">
	        			<tr>
	        				<td class="nolist" colspan="2">검색된 주소가 없습니다.</td>
	        			</tr>
        			</c:if>
					<c:forEach var="listDt" items="${list}" varStatus="i">
						<tr>
							<td class="num fn_zip"><c:out value="${listDt.zipNo}"/></td>
							<td class="tlt">
								<dl>
									<dt>도로명</dt>
									<dd class="fn_road_addr"><c:out value="${listDt.roadAddr}"/></dd>
									<dd class="btn"><button type="button" data-type="road" class="btnTypeG fn_btn_addr_submit"><c:out value="${btnSelectName}"/></button></dd>
									<dt>지번</dt>
									<dd class="fn_dong_addr"><c:out value="${listDt.jibunAddr}"/></dd>
									<dd class="btn"><button type="button" data-type="dong" class="btnTypeG fn_btn_addr_submit"><c:out value="${btnSelectName}"/></button></dd>
								</dl>
							</td>
						</tr>
					</c:forEach>
					</tbody>
        		</table>
				<!-- paging -->
				<div class="paginate mgt15">
					<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
				</div>
				<!-- //paging -->
			</c:if>
			</div>
		</div>
	</div>
<jsp:include page = "${jspSiteIncPath}/dialog_bottom.jsp" flush = "false"/>