<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<c:set var="searchFormId" value="fn_zipCodeSearchForm"/>
<jsp:include page="${jspSiteIncPath}/dialog_top.jsp" flush="false">
	<jsp:param name="javascript_page" value="${moduleJspRPath}/epostSearchList.jsp"/>
	<jsp:param name="searchFormId" value="${searchFormId}"/>
	<jsp:param name="addCss" value="${contextPath}${moduleResourcePath}/style.css"/>
</jsp:include>
	<spring:message code="button.select" var="btnSelectName"/>
	<spring:message code="item.search.name" var="itemSearchName"/>
	<div id="cms_zipCode_article">
		<div class="popup-head cont_zip">
			<h1 class="titTypeB">우편번호검색</h1>
       		<p id="fn_title_road" class="title skip">
       			<spring:message code="message.zipcode1.road.example.text1"/>
       			<br/><spring:message code="message.zipcode1.road.example.text2"/>
			</p>
            <p id="fn_title_dong" class="title skip">
       			<spring:message code="message.zipcode1.dong.example.text1"/>
       			<br/><spring:message code="message.zipcode1.dong.example.text2"/>
			</p>
		</div>
		<div class="popup-content cont_zip">
			<div class="post-search-area">
				<form id="${searchFormId}" name="${searchFormId}" method="get">
				<elui:hiddenInput inputInfo="${queryString}" exceptNames="${searchFormExceptParams}"/>
				<input type="hidden" name="type" id="type"/>
				<div id="fn_zip_tab">
					<ul>
						<li><button type="button" value="road" data-title1="도로명" data-title2="건물번호">도로명 주소</button></li>
						<li><button type="button" value="dong" data-title1="동명" data-title2="지번">지번 주소</button></li>
					</ul>
				</div>
				<fieldset>
					<dl>
						<dt class="first fn_data_title1">도로명</dt><dd><input type="text" id="sRGRoad" name="sRGRoad" value="<c:out value="${queryString.sRGRoad)}"/>" class="inTxt" style="ime-mode:active;width:120px;" title="도로명"/></dd>
						<dt class="fn_data_title2">건물번호</dt>
						<dd>
							<input type="text" id="sRGBNum1" name="sRGBNum1" value="<c:out value="${queryString.sRGBNum1}"/>" class="inTxt" style="ime-mode:active;width:50px;" title="건물번호 첫번째자리"/>
							-
							<input type="text" id="sRGBNum2" name="sRGBNum2" value="<c:out value="${queryString.sRGBNum2}"/>" class="inTxt" style="ime-mode:active;width:50px;" title="건물번호 두번째자리"/>
						</dd>
					</dl>
					<div class="submit">
						<input type="submit" class="btnSearch" value="<c:out value="${itemSearchName}"/>" title="<c:out value="${itemSearchName}"/>"/>
					</div>
				</fieldset>
				</form>
			</div>
			<div class="post-search-result">
        	<c:if test="${!empty param.type}">
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
									<dd class="fn_road_addr"><c:out value="${listDt.lnmAdres}"/></dd>
									<dd class="btn"><button type="button" data-type="road" class="btnTypeG fn_btn_addr_submit"><c:out value="${btnSelectName}"/></button></dd>
									<dt>지번</dt>
									<dd class="fn_dong_addr"><c:out value="${listDt.rnAdres}"/></dd>
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