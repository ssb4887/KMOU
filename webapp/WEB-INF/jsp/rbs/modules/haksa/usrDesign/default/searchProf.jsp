<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="searchFormId" value="fn_techSupportSearchForm"/>
<c:set var="listFormId" value="fn_techSupportListForm"/>
<c:set var="inputWinFlag" value=""/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/searchProf.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<main id="content-wrap">            
            <div class="content-inner">
                <div class="list-top">
                    <p class="list-count">검색어에 대한 총 <span class="bold">${totalCount}</span>건이 검색되었습니다.</p>
					<div class="form-group-line2">
                        <label for="select01-01" class="sr-only">정렬</label>
                        <select id="select01-01" class="form-select02" style="min-width: 150px;">
                            <option>최근등록(임용순)</option>
                        </select>
                    </div>
                </div>		
        <div class="professor-box-list">                    
            <c:forEach var="listDt" items="${profList}" varStatus="i">
            <c:set var="listIdxName" value="docCd"/>
            <c:set var="listKey" value="${listDt.empNo}"/>
           <div class="professor-box" onclick="selectProf('${listDt.empNo}','${listDt.korNm}');">
<!--                 <div class="favorite-checkbox"> -->
<%--                    <input type="checkbox" id="checkbox01-01" name="star" value="${listDt.bookMark}" <c:if test="${'Y' == listDt.bookMark}">checked</c:if>> --%>
<!--                     <label for="checkbox01-01" class="sr-only">즐겨찾기</label> -->
<%-- 					<input type="hidden" name="docCd" value="${listDt.empNo}" /> --%>
<!--                          <i></i> -->
<!--                      </div> -->
			
				<div class="top-cont">
					<div class="user-info-line">
						<span class="image">
<%-- 						<img src="${contextPath}/${crtSiteId}/assets/images/contents/professor_img01.png" alt="교수 사진"> --%>
						</span>
						<div class="right">
							<span class="name">${listDt.korNm}</span>
						</div>
					</div>
					<ul class="user-info-list">
						<li>
							<span class="tit">소속</span>
							<p class="txt">${listDt.deptNm} &gt; ${listDt.deptNm2}</p>
						</li>
						<li>
							<span class="tit">담당과목</span>
							<p class="txt">
							${fn:replace(listDt.scSmry,'\"null\"','-')}
							</p>
						</li>
					</ul>
				</div>
				<div class="bot-cont">
					<ul class="user-info-list">
						<li>
							<span class="tit">이메일</span>
							<p class="txt">
							${fn:replace(listDt.email,'\"null\"','-')}
							</p>
						</li>
						<li>
							<span class="tit">전화번호</span>
							<p class="txt">
							${fn:replace(listDt.ofceTelNo,'\"null\"','-')}
							</p>
						</li>
						<li>
							<span class="tit">홈페이지</span>
							<p class="txt">
							${fn:replace(listDt.homePage,'\"null\"','-')}
							</p>
						</li>
					</ul>
				</div>
			   
		</div>
		</c:forEach>                
	</div>
	
	<!-- start 페이징 -->
    <div class="paginate">
    <pgui:paginationIndex listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" pageName="page"/>
    </div>
    <!-- end 페이징 -->
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>