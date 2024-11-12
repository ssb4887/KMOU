<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:if test="${!empty TOP_PAGE}">
    <jsp:include page="${TOP_PAGE}" flush="false">
        <jsp:param name="javascript_page" value="${moduleJspRPath}/profMstBmk.jsp"/>
    </jsp:include>
</c:if>

<div class="">
    <ul class="tabTypeA tab li4" style="margin-bottom:40px;">
        <li class="fn_tab_view"><a id="tab1" href="${URL_PROF_BOOKMART}" >교수 찜수</a></li>
        <li class="fn_tab_view on"><a id="tab2" href="${URL_PROF_MOST_BOOKMART}">교수 인기 찜</a></li>
    </ul>
</div>

<div id="cms_board_article" class="subConBox">

    <div class="tbSearch tbShowDt">
        <form id="searchForm" action="<c:out value="${URL_SUBJECT_MOST_BOOKMART_DATA}"/>" method="post" target="submit_target" style="width: 100%;">
            <input type="hidden" name="searchFlag" value="1">
            <dl>
                <dt>
                    <label for="">조회기간</label>
                </dt>
                <dd class="lw">
                    <div class="input-group">
                        <input type="text" name="searchStDt" id="searchStDt" title="검색일자" readonly="readonly" style="width:110px; text-align: center;">
                        <span><button type="button" class="btnCal" id="btnSearchStDt" title="달력선택">달력선택</button></span>
                        <span class="input-group-sp">~</span>
                        <input type="text" name="searchEdDt" id="searchEdDt" title="검색일자" readonly="readonly" style="width:110px; text-align: center;">
                        <span><button type="button" class="btnCal" id="btnSearchEdDt" title="달력선택">달력선택</button></span>
                    </div>
                </dd>
            </dl>
            <input type="button" class="btnSearch" value="검색" title="검색" onclick="getData();">
        </form>
    </div>

    <div class="subBox">
        <canvas id="chartBox"></canvas>
    </div>

</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>
