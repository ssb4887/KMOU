<%@ include file="../../../../include/commonTop.jsp"%>
<c:if test="${!empty TOP_PAGE}">
    <jsp:include page="${TOP_PAGE}" flush="false">
        <jsp:param name="javascript_page" value="${moduleJspRPath}/srchCntStat.jsp"/>
    </jsp:include>
</c:if>

<div id="cms_board_article" class="subConBox">
    <div class="tabMenuC">
        <ul class="tabTypeA tab li4" style="margin-bottom: 40px;">
            <li class="fn_tab_view on" id="tab1" title="검색 건수 추이">
                <a href="${URL_SRCH_CNT_STAT}">검색 건수 추이</a>
            </li>
            <li class="fn_tab_view" id="tab2" title="검색 사용자 추이">
                <a href="${URL_SRCH_USR_STAT}">검색 사용자 추이</a>
            </li>
            <li class="fn_tab_view" id="tab3" title="검색어 통계">
                <a href="${URL_SRCH_KEYWORD_STAT}">검색어 통계</a>
            </li>
        </ul>
    </div>
    <div class="tbSearch tbShowDt">
        <form id="searchForm" action="<c:out value="${URL_SRCH_CNT_STAT}"/>" method="post" target="submit_target" style="display: inline-block;">
            <dl>
                <dt>
                    <label for="statsType">검색옵션</label>
                </dt>
                <dd>
                    <select id="searchStatsType" name="searchStatsType" class="select">
                        <option value="1">연도별</option>
                        <option value="2">월별</option>
                        <option value="3">일별</option>
                        <option value="4" selected>시간별</option>
                    </select>
                </dd>
                <dt>
                    <label for="is_statsDate">검색일자</label>
                </dt>
                <dd>
                    <select id="searchStatsDate1" name="searchStatsDate1" class="select">
                        <option value="">년 선택</option>
                    </select>
                    <select id="searchStatsDate2" name="searchStatsDate2" class="select">
                        <option value="">월 선택</option>
                    </select>
                    <select id="searchStatsDate3" name="searchStatsDate3" class="select">
                        <option value="">일 선택</option>
                    </select>
                </dd>
            </dl>
            <input type="button" class="btnSearch" value="검색" title="검색" onclick="getData();">
        </form>
    </div>

    <div id="chartBox" style="margin-top: 20px; padding: 20px 10px; background-color: #ffffff; border-radius: 10px;">
        <canvas id="chartCanvas"></canvas> <!-- Chart.js 캔버스 -->
    </div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>
