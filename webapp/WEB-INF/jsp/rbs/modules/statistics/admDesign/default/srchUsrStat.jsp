<%@ include file="../../../../include/commonTop.jsp"%>
<c:if test="${!empty TOP_PAGE}">
    <jsp:include page="${TOP_PAGE}" flush="false">
        <jsp:param name="javascript_page" value="${moduleJspRPath}/srchUsrStat.jsp"/>
    </jsp:include>
</c:if>

<div id="cms_board_article" class="subConBox">
    <div class="tabMenuC">
        <ul class="tabTypeA tab li4" style="margin-bottom: 40px;">
            <li class="fn_tab_view" id="tab1" title="검색 건수 추이">
                <a href="${URL_SRCH_CNT_STAT}">검색 건수 추이</a>
            </li>
            <li class="fn_tab_view on" id="tab2" title="검색 사용자 추이">
                <a href="${URL_SRCH_USR_STAT}">검색 사용자 추이</a>
            </li>
            <li class="fn_tab_view" id="tab3" title="검색어 통계">
                <a href="${URL_SRCH_KEYWORD_STAT}">검색어 통계</a>
            </li>
        </ul>
    </div>
    <div class="tbSearch tbShowDt">
        <form id="${searchFormId}" action="<c:out value="${URL_SRCH_CNT_STAT}"/>" method="post" target="submit_target" style="display: inline-block;">
            <dl>
                <dt>
                    <label for="statsType">검색일자</label>
                </dt>
                <dd>
                    <div class="input-group">
                        <input type="text" name="searchStDt" id="searchStDt" title="검색일자" readonly="readonly" style="width:110px;">
                        <span><button type="button" class="btnCal" id="btnSearchStDt" title="달력선택">달력선택</button></span>
                        <span class="input-group-sp">~</span>
                        <input type="text" name="searchEdDt" id="searchEdDt" title="검색일자" readonly="readonly" style="width:110px;">
                        <span><button type="button" class="btnCal" id="btnSearchEdDt" title="달력선택">달력선택</button></span>
                    </div>
                </dd>
            </dl>
            <input type="button" class="btnSearch" value="검색" title="검색" onclick="getData();">
        </form>
    </div>

    <div style="display: flex; justify-content: space-between; margin-top: 20px;">
        <div style="width: 49%; padding: 20px 10px; background-color: #ffffff; border-radius: 10px;">
            <canvas id="usrChartBox"></canvas>
        </div>
        <div style="width: 49%; padding: 20px 10px; background-color: #ffffff; border-radius: 10px;">
            <canvas id="shyrChartBox"></canvas>
        </div>
    </div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>
