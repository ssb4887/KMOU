<%@ include file="../../../../include/commonTop.jsp"%>
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

<form name="studentView" method="get" action="${URL_VIEWINFO}">
	<input type="hidden" name="STUDENT_NO" value="">
</form>	
	<!-- search -->
	<div class="tbMSearch">
			<input type="hidden" name="mId" value="${queryString.mId}">
			<fieldset>				
				<dl>
					<dt>소속 </dt>
					<dd style="width:50%;">
						<select name="is_colgCd" id="selectColg" class="select" title="대학">
						</select>
						<select name="is_fcltSustCd" id="selectDept" class="select" title="학부/학과">
							<option value="">전체</option>
						</select>
						<select name="is_mjCd" id="selectMj" class="select" title="전공">
							<option value="">전체</option>
						</select>					
					</dd>
					<dt id="dt_student_no" class=""><label for="student_nm">이름/학번</label></dt>
					<dd>
						<input type="text" name="student_nm" id="student_nm" title="이름/학번" value="">
					</dd>
				</dl>
			</fieldset>
			<p style="padding-bottom: 15px;">
				<input type="submit" id="fn_btn_search" class="btnSearch" value="검색" title="검색">
			</p>
	</div>		
		
		<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
			<colgroup>
				<col width="40px" />
				<col width="80px" />
				<col width="80px"/>
				<col width="150px"/>
				<col width="80px" />
			</colgroup>
			<thead>
			<tr>
				<th scope="col">No</th>
				<th scope="col">이름</th>
				<th scope="col">학번</th>
				<th scope="col">소속</th>
				<th scope="col">조회</th>
			</tr>
			</thead>
			<tbody class="alignC" id="studentList">
			</tbody>
		</table>
		
		<!-- paging -->
		<div class="paginate mgt15" id="page">
		</div>
		<!-- //paging -->
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if> --%>