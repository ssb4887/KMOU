<%@ include file="../include/commonTop.jsp"%>
<link rel="stylesheet" type="text/css" href ="../../RBISADM/css/majorInfo.css"/>
<jsp:include page = "../include/dashboard_top.jsp" flush="false">
    <jsp:param name="javascript_page" value="../include/dashboard.jsp"/>
</jsp:include>

<div class="main_con">
    <div class="box">
        <h2>검색 건수 추이</h2>
        <div class="input-group nodata"></div>
        <span class="btnMore"><a href="${contextPath}/${crtSiteId}/menuContents/web/statistics/srchCntStat.do?mId=55">더보기</a></span>
        <canvas class="chart1" id="chart1" width="400" height="200"></canvas>
    </div>
    <div class="box">
        <h2>검색어 통계</h2>
        <div class="input-group bgg">
            <input type="text" name="searchStDt_2" id="searchStDt_2" title="검색일자" readonly="readonly" style="width:110px;" value="${stDt}">
            <span><button type="button" class="btnCal" id="btnSearchStDt_2" title="달력선택">달력선택</button></span>
            <span class="input-group-sp">~</span>
            <input type="text" name="searchEdDt_2" id="searchEdDt_2" title="검색일자" readonly="readonly" style="width:110px;" value="${edDt}">
            <span><button type="button" class="btnCal" id="btnSearchEdDt_2" title="달력선택">달력선택</button></span>
            <button type="button" id="fn_btn_search-2" class="btnSearch" title="검색">검색</button>
        </div>
        <span class="btnMore"><a href="${contextPath}/${crtSiteId}/menuContents/web/statistics/srchKeywordStat.do?mId=55">더보기</a></span>
        <canvas class="chart2" id="chart2" width="400" height="200"></canvas>
    </div>
    <div class="box">
        <h2>로그인 통계</h2>
        <div class="input-group nodata"></div>
        <span class="btnMore"></span>
        <canvas class="chart3" id="chart3" width="400" height="200"></canvas>
    </div>
    <div class="box">
        <h2>메뉴별 접속자 수</h2>
        <div class="input-group bgg">
            <input type="text" name="searchStDt_4" id="searchStDt_4" title="검색일자" readonly="readonly" style="width:110px;" value="${stDt}">
            <span><button type="button" class="btnCal" id="btnSearchStDt_4" title="달력선택">달력선택</button></span>
            <span class="input-group-sp">~</span>
            <input type="text" name="searchEdDt_4" id="searchEdDt_4" title="검색일자" readonly="readonly" style="width:110px;" value="${edDt}">
            <span><button type="button" class="btnCal" id="btnSearchEdDt_4" title="달력선택">달력선택</button></span>
            <button type="button" id="fn_btn_search-4" class="btnSearch" title="검색">검색</button>
        </div>
        <span class="btnMore"><a href="${contextPath}/${crtSiteId}/menuContents/web/statistics/menuList.do?mId=63">더보기</a></span>
        <canvas class="chart4" id="chart4" width="400" height="200"></canvas>
    </div>
    <div class="box">
        <h2>찜현황 통계(전체)</h2>
        <div class="input-group nodata"></div>
        <span class="btnMore"><a href="${contextPath}/${crtSiteId}/menuContents/web/statistics/profBmk.do?mId=60">더보기</a></span>
        <canvas class="chart5" id="chart5" width="400" height="200"></canvas>
    </div>
    <div class="box">
        <h2>해시태그 키워드 통계</h2>
        <div class="input-group bgg">
            <input type="text" name="searchStDt_5" id="searchStDt_5" title="검색일자" readonly="readonly" style="width:110px;" value="${stDt}">
            <span><button type="button" class="btnCal" id="btnSearchStDt_5" title="달력선택">달력선택</button></span>
            <span class="input-group-sp">~</span>
            <input type="text" name="searchEdDt_5" id="searchEdDt_5" title="검색일자" readonly="readonly" style="width:110px;" value="${edDt}">
            <span><button type="button" class="btnCal" id="btnSearchEdDt_5" title="달력선택">달력선택</button></span>
            <button type="button" id="fn_btn_search-5" class="btnSearch" title="검색">검색</button>
        </div>
        <span class="btnMore"><a href="${contextPath}/${crtSiteId}/menuContents/web/statistics/sbjtHashtagStat.do?mId=64">더보기</a></span>
        <canvas class="chart6" id="chart6" width="400" height="200"></canvas>
    </div>
</div>

<jsp:include page="${BOTTOM_PAGE}" flush="false"/>