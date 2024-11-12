<%@ include file="../include/commonTop.jsp"%> 
<c:if test="${!empty TOP_PAGE}"><jsp:include page = "../include/top.jsp" flush = "false"/></c:if>
	<div class="bdSearch">
		<form id="fn_totalSearchForm" name="fn_totalSearchForm" method="get" action="../main/search.do">
			<input type="hidden" id="mId" name="mId" value="11"/>
			<fieldset>	
				<legend>통합검색</legend>				
				<input type="text" name="fn_totalKey" id="fn_totalKey" value="<c:out value="${queryString.fn_totalKey}"/>" title="검색어를 입력하세요"/>
				<input type="submit" class="btnSearch03" id="searchBtn" title="검색" value="검색"/>
			</fieldset>
		</form>
	</div>
	
	<c:forEach items="${boardMenuJsonArray}" var="menuDt" varStatus="i">
	<c:set var="boardMenuIdx" value="${menuDt.menu_idx}"/>
	<c:set var="boardList" value="${boardListHashMap[boardMenuIdx]}"/>
	<c:set var="moduleId" value="${menuDt.module_id}"/>
    <div class="srcList">
    	<div class="sltitleBox">
    		<h4 class="titTypeB">${menuDt.total_menu_name}</h4>
    		<span class="more"><a href="<c:out value="${contextPath}/${crtSiteId}/${moduleId}/list.do?mId=${boardMenuIdx}&keyField=contents&key=${elfn:encode(queryString.fn_totalKey , 'UTF-8')}"/>"><img src="<c:out value="${contextPath}${imgPath}/main/btn_more1.gif"/>" alt="${menuDt.menu_name} 더보기" /></a></span>
    	</div>
        <ul class="mBoardList">
        <c:choose>
        	<c:when test="${empty boardList}">
        		<li style="height:50px;padding-top:30px;text-align:center;"><spring:message code="message.no.list"/></li>
        	</c:when>
        	<c:otherwise>
				<c:forEach items="${boardList}" var="listDt" varStatus="j">
					<li><a href="<c:out value="${contextPath}/${crtSiteId}/${moduleId}/view.do?mId=${boardMenuIdx}&${menuDt.idx_name}=${listDt.IDX}"/>"><span class="title"><c:if test="${!empty listDt.OPTN_NAME}">[<c:out value="${listDt.OPTN_NAME}"/>] </c:if><c:out value="${listDt.SUBJECT}"/></span><span class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></span></a></li>
	        	</c:forEach>
        	</c:otherwise>
        </c:choose>
        </ul>
    </div>
	</c:forEach>
	
	<c:forEach items="${contentsMenuJsonArray}" var="menuDt" varStatus="i">
	<c:set var="contentsMenuIdx" value="${menuDt.menu_idx}"/>
	<c:set var="contentsList" value="${contentsListHashMap[contentsMenuIdx]}"/>
	<c:set var="moduleId" value="${menuDt.module_id}"/>
    <div class="srcList">
    	<div class="sltitleBox">
    		<h4 class="titTypeB">${menuDt.total_menu_name}</h4>
    		<span class="more"><a href="<c:out value="${contextPath}/${crtSiteId}/${moduleId}/view.do?mId=${contentsMenuIdx}"/>"><img src="<c:out value="${contextPath}${imgPath}/main/btn_more1.gif"/>" alt="${menuDt.menu_name} 더보기" /></a></span>
    	</div>
        <ul class="mBoardList">
        <c:choose>
        	<c:when test="${empty contentsList}">
        		<li style="height:50px;padding-top:30px;text-align:center;"><spring:message code="message.no.list"/></li>
        	</c:when>
        	<c:otherwise>
				<c:forEach items="${contentsList}" var="listDt" varStatus="j">
					<li><a href="<c:out value="${contextPath}/${crtSiteId}/${moduleId}/view.do?mId=${contentsMenuIdx}"/>"><span class="title"><c:out value="${elfn:substring(elfn:removeHTMLTag(listDt.WORK_CONTENTS, 0), queryString.fn_totalKey, 100)}"/></span></a></li>
	        	</c:forEach>
        	</c:otherwise>
        </c:choose>
        </ul>
    </div>
	</c:forEach>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "../include/bottom.jsp" flush = "false"/></c:if>