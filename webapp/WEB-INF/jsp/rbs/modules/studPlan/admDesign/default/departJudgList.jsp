<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="searchFormId" value="fn_techSupportSearchForm"/>
<c:set var="listFormId" value="fn_techSupportListForm"/>
<c:set var="inputWinFlag" value=""/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/admScript/departJudgList.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>

<form id="frmView" name="frmView" method="post" action="">
	<input type="hidden" name="mId" value="48">
	<input type="hidden" id="SDM_CD" name="SDM_CD" value="">
	<input type="hidden" id="REVSN_NO" name="REVSN_NO" value="">
</form>

<div id="cms_board_article">
	<!-- search -->
	<div class="tbMSearch">
		<form name="${searchFormId}" id="${searchFormId}" method="get"  action="<c:out value="${URL_JUDGENOASSIGNLIST}"/>">
		<input type="hidden" name="mId" value="${queryString.mId}">
		<fieldset>				
			<dl>
				<dt style="width:105px">교과목 소속 구성</dt>
				<dd style="width:50%">
					<label class="blind">대학</label>
					<select name="selectColg" id="selectColg" class="select" title="대학" >
					</select>
					<label class="blind">학부(과)</label>
					<select name="selectDept" id="selectDept" class="select" title="학부/학과" >
						<option value="" selected>학부(과)</option>
					</select>
					<label class="blind">전공</label>
					<select name="selectMj" id="selectMj" class="select" title="전공">
						<option value="" selected>전공</option>	
					</select>					
				</dd>
				<itui:searchFormItemIn itemListSearch="${itemInfo.noAssign_list_search}" searchOptnHashMap="${searchOptnHashMap}" isUseRadio="${isUseRadio}" isUseMoreItem="${isUseMoreItem}"/>	
			</dl>
		</fieldset>
		<p style="padding-bottom: 15px;">
			<button type="button" onclick="javascript:getDepartJudgList(1)" class="btnSearch">검색</button>
			<a href="#" id="showAllList" class="btnTotalList" style="display:none;">전체목록</a>
		</p>
	</form>
	</div>
	<!-- //search -->
	<!-- button -->
	<div class="btnTopFull">
		<div class="right">
			<div class="resultCount">총 <strong><span id="listCnt"></span></strong>건이 검색되었습니다.</div>
		</div>
	</div>
	<!-- //button -->
<%-- 		<form id="${listFormId}" name="${listFormId}" method="post" target="list_target"> --%>
	<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
		<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
		<colgroup>
			<col width="5%" />
			<col width="15%" />
			<col width="10%" />
			<col width="auto" />
			<col width="10%" />
			<col width="10%" />
			<col width="10%" />
			<col width="10%" />
		</colgroup>
		<thead>
		<tr>
			<th scope="col">번호</th>
			<th scope="col">상태</th>
			<th scope="col">신설구분</th>
			<th scope="col">학생설계전공명</th>
			<th scope="col">신청전공</th>
			<th scope="col">신청자</th>
			<th scope="col">창의융합교육센터 승인일시</th>
			<th scope="col"></th>
		</tr>
		</thead>
		<tbody class="alignC" id="studPlanList">
		</tbody>
	</table>
	<!--paging-->
	<div class="paginate mgt15">
		<ul class="pagination gap-2 justify-content-center mt-5" id="page">					
		</ul>
	</div>
	<!-- //paging -->
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>