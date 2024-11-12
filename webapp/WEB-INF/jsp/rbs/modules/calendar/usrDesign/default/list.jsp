<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>							
<c:set var="searchFormId" value="fn_calendarSearchForm"/>							
<c:set var="listFormId" value="fn_calendarListForm"/>						
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/>				
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>	
		<jsp:param name="searchFormId" value="${searchFormId}"/>				
		<jsp:param name="listFormId" value="${listFormId}"/>					
	</jsp:include>
</c:if>
<div id="cms_calendar_article"> 
     <div id="calendar">
     <div class="date" >
					<a href="<c:out value="${URL_DEFAULT_TAB_LIST}"/>" onclick="fn_preMonthCon(this.href);return false;" onkeypress="this.onclick;"><img src="<c:out value="${contextPath}${imgPath}/common/btn_prev.gif"/>" alt="이전"/></a>&nbsp;
					<a href="<c:out value="${URL_DEFAULT_TAB_LIST}"/>&year=<c:out value="${nowYear}"/>&month=<c:out value="${nowMonth}"/>"><c:out value="${nowYM}"/></a>&nbsp;
					<a href="<c:out value="${URL_DEFAULT_TAB_LIST}"/>" onclick="fn_nextMonthCon(this.href);return false;" onkeypress="this.onclick;"><img src="<c:out value="${contextPath}${imgPath}/common/btn_next.gif"/>" alt="다음"/></a>
	</div>
       <table summary="Calendar" class="cldTable">
			        <caption>Calendar</caption>
			        <colgroup>
			           <col width="200px" />
			           <col width="200px" />
			           <col width="200px" />
			           <col width="200px" />
			           <col width="200px" />
			           <col width="200px" />
			           <col width="200px" />
			        </colgroup>
			        <thead>
			            <tr>
			                <th class="sun">일(SUN)</th>
			                <th>월(MON)</th>
			                <th>화(TUE)</th>
			                <th>수(WED)</th>
			                <th>목(THU)</th>
			                <th>금(FRI)</th>
			                <th class="sat">토(SAT)</th>
			            </tr>
			        </thead>
			        <tbody>
			
			        	<c:set var="isTR" value="${true}"/>
			           	<c:set var="dateConCnt" value="0"/>
						<c:set var="preMConCnt" value="0"/>
						<c:set var="today" value="${nowDay}"/>
						<c:forEach var="calIdx" begin="1" end="42">
							<c:set var="cnt" value="0" />
							<c:set var="calTD" value="${calIdx - firstDayOfWeek + 1}"/>
							<c:if test="${calTD > endDay && calIdx % 7 == 1}">
								<c:set var="isTR" value="${false}"/>
							</c:if>
							<c:if test="${isTR}">
								<c:if test="${calIdx % 7 == 1}">
									<c:if test="${calIdx != 1}"></c:if><tr>
								</c:if>
								<c:choose>
								<c:when test="${calTD > 0 && calTD <= endDay}">
									<c:set var="calTDLP" value="${elfn:getIntLPAD(calTD, '0', 2)}"/>
									<c:set var="isEvent" value="${false}"/>
									<c:set var="listCnt" value="${totalCount}"/>
									<c:set var="calFullDay" value="${nowYMR}-${calTDLP}"/>
									<c:set var="dayEvent" value="${elfn:isBWDate(calFullDay, dayEventList, 'CAL_DATE1', 'CAL_DATE2')}"/>
								<td class="<c:if test="${sysYMD eq calFullDay}">today</c:if><c:if test="${param.date eq calTDLP}"> on</c:if>">
									<span<c:if test="${dayEvent}"> class ="event"</c:if>><a href="<c:out value="${URL_DEFAULT_TAB_LIST}"/>&year=<c:out value="${nowYear}"/>&month=<c:out value="${nowMonth}"/>&date=<c:out value="${calTDLP}"/>"><c:out value="${calTD}"/></a></span>
									<c:if test="${sysYMD eq calFullDay}">Today</c:if>
								</td></c:when><c:otherwise><td></td></c:otherwise></c:choose>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
				</div>
	<!-- search -->
	<itui:searchFormItem divClass="tbSearch tbShowDt" formId="${searchFormId}" formName="${searchFormId}" formAction="${URL_DEFAULT_LIST}" itemListSearch="${itemInfo.list_search}" searchOptnHashMap="${searchOptnHashMap}" searchFormExceptParams="${searchFormExceptParams}" submitBtnId="fn_btn_search" submitBtnClass="btnSearch" submitBtnValue="검색" listAction="${URL_DEFAULT_LIST}" listBtnId="fn_btn_totallist" listBtnClass="btnTotalList"/>
	<!-- //search -->
	
	<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
	<table class="tbListA" summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
		<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
		<colgroup>
			<col width="40px" />
			<col width="180px" />
			<col />
			<col width="50px" />
			<col width="80px" />
			<col width="90px" />
			<col width="50px" />
		</colgroup>
		<thead>
		<tr>
			<th scope="col">번호</th>
			<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="calDate"/></th>
			<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/></th>
			<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="file"/></th>
			<th scope="col"><spring:message code="item.reginame.name"/></th>
			<th scope="col"><spring:message code="item.regidate.name"/></th>
			<th scope="col" class="end"><spring:message code="item.board.views.name"/></th>
			<!-- 마지막 th에 class="end" -->
		</tr>
		</thead>
		<tbody class="alignC">
			<c:if test="${empty list}">
			<tr>
				<td colspan="7" class="bllist"><spring:message code="message.no.list"/></td>
			</tr>
			</c:if>
			<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
			<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
			<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
			<c:forEach var="listDt" items="${list}" varStatus="i">
			<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
			<tr>
				<td class="num"><c:out value="${listNo}"/></td>
                <td><c:out value="${listDt.CAL_DATE1}"/>~<c:out value="${listDt.CAL_DATE2}"/></td>
				<td class="tlt">
						<a href="<c:out value="${URL_VIEW}"/>&${listIdxName}=${listKey}" title="상세보기" class="fn_btn_view"><c:out value="${listDt.SUBJECT}"/></a>
					</td>	
				<td><c:if test="${listDt.FILE_CNT > 0}"><img src="<c:out value="${contextPath}${imgPath}/common/ico_file.gif"/>" alt="파일"/></c:if></td>
				<td><c:out value="${elfn:memberItemOrgValue('mbrName', listDt.REGI_NAME)}"/></td>
				<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				<td class="num" id="fn_cal_views<c:out value="${listDt.CAL_IDX}"/>"><c:out value="${listDt.VIEWS}"/></td>
			</tr>
			<c:set var="listNo" value="${listNo - 1}"/>
			</c:forEach>
		</tbody>
	</table>
	</form>
	
	<!-- paging -->
	<div class="paginate mgt15">
		<pgui:pagination listUrl="${URL_PAGE_LIST}" pgInfo="${paginationInfo}" imgPath="${imgPath}" pageName="${elfn:getString(settingInfo.page_name, 'page')}"/>
	</div>
	<!-- //paging -->
	
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>