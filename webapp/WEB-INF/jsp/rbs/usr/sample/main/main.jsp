<%@ include file="../include/commonTop.jsp"%> 
<c:if test="${!empty TOP_PAGE}"><jsp:include page = "../include/main_top.jsp" flush = "false"/></c:if>
	<div id="visual">
		<h3 class="hidden">메인 비주얼</h3>
		<div id="featured">
			<c:set var="bannerUrl" value="banner/image.do?mId=1&fnIdx=${siteInfo.banner_fn_idx}"/>
			<c:forEach var="bannerDt" items="${bannerDashboardList01}" varStatus="i">
				<c:choose>
					<c:when test="${!empty bannerDt.LINK_URL}">
						<c:set var="bannerViewUrl" value="${bannerDt.LINK_URL}"/>
					</c:when>
					<c:otherwise>
						<c:set var="bannerViewUrl" value="#"/>
					</c:otherwise>
				</c:choose>
			<a href="<c:out value="${bannerViewUrl}"/>"<c:if test="${bannerDt.LINK_TARGET == '_blank'}"> target="_blank"</c:if>><img src="${bannerUrl}&id=${elfn:imgNSeedEncrypt(bannerDt.IMG_SAVED_NAME)}" rel="mimg${i.count}"/></a>
			</c:forEach>
		</div> 
	</div>
	<script type="text/javascript">
	$(function(){
		$('#visual a>img').each(function(){
			var varObj = $(this);
			var varImg = new Image();
			$(varImg).error(function(){
				$(varObj).parent("a").remove();
				//$(varObj).remove();
			}).attr('src', varObj.attr('src'));
		});
	});
	</script>

	<!-- 공지사항 , 자료실-->
	<div id="alim1">
		<h3 class="blind">일반 게시판</h3>
		<c:set var="boardModuleIdx" value="board"/>
		<c:set var="boardMenuKey" value="board1"/>
		<c:if test="${!empty boardDashboardObject && !empty boardDashboardObject[boardMenuKey] && !empty boardDashboardObject[boardMenuKey]['menu_idx']}">
			<c:set var="boardMenuIdx" value="${boardDashboardObject[boardMenuKey]['menu_idx']}"/>
			<c:set var="boardList" value="${boardDashboardHashMap[boardMenuKey]}"/>
		</c:if>
		<h4 class="tab"><a href="#alim1c1" id="alim1m1" onclick="tabOn('alim1',1);return false;" title="일반 게시판">일반 게시판</a></h4>						
		<!--공지사항 -->
		<div id="alim1c1" class="cont1">
			<span class="tab_more"><a href="${elfn:getMenuUrl(boardMenuIdx)}" title="더보기">더보기</a></span>
			<div class="j_box">	
			<c:if test="${empty boardList}"><div style="margin:20px 0;">등록된 내용이 없습니다.</div></c:if>
			<c:forEach var="boardDt" items="${boardList}" varStatus="i">
				<ul>
					<li class="text"><a href="<c:out value="${contextPath}/${crtSiteId}/${boardModuleIdx}/view.do?mId=${boardMenuIdx}&brdIdx=${boardDt.BRD_IDX}"/>" title="<c:out value="${boardDt.SUBJECT}"/>"><c:if test="${!empty boardDt.CATE_CODE_NAME}">[${boardDt.CATE_CODE_NAME}] </c:if><c:out value="${boardDt.SUBJECT}"/></a></li>
					<li class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${boardDt.REGI_DATE}"/></li>
				</ul>
			</c:forEach>
			</div>
		</div>
		<!-- /공지사항-->
	</div>
	<script type="text/javascript">tabOn('alim1',1);</script>
	<!-- /공지사항 , 자료실 -->
	
	<c:set var="boardMenuKey" value="board2"/>
	<c:if test="${!empty boardDashboardObject && !empty boardDashboardObject[boardMenuKey] && !empty boardDashboardObject[boardMenuKey]['menu_idx']}">
		<c:set var="boardMenuIdx" value="${boardDashboardObject[boardMenuKey]['menu_idx']}"/>
		<c:set var="boardList" value="${boardDashboardHashMap[boardMenuKey]}"/>
	</c:if>
	<!--갤러리-->
	<div id="gallery">
		<h3>갤러리</h3>
		<span class="more"><a href="${elfn:getMenuUrl(boardMenuIdx)}">더보기</a></span>
		<ul>
		<c:if test="${empty boardList}"><li class="last">등록된 내용이 없습니다.</li></c:if>
			<c:forEach var="boardDt" items="${boardList}" varStatus="i">
			<li class="last"><a href="<c:out value="${contextPath}/${crtSiteId}/${boardModuleIdx}/view.do?mId=${boardMenuIdx}&brdIdx=${boardDt.BRD_IDX}"/>" title="<c:out value="${boardDt.SUBJECT}"/>"><span class="img"><img src="<c:out value="${contextPath}/${crtSiteId}/${boardModuleIdx}/image.do?mId=${boardMenuIdx}&id="/>${elfn:imgNSeedEncrypt(boardDt.FILE_SAVED_NAME)}" onerror="$(this).remove();" alt="<c:out value="${boardDt.FILE_TEXT}"/>"/></span><span class="txt"><c:out value="${boardDt.SUBJECT}"/></span></a></li>
		</c:forEach>
		</ul>
		</div>	
	<!--/ 갤러리-->
	<div id="popup">
		<h3 class="hidden">팝업</h3>
		<div id="popup_featured"> 
			<c:set var="popupUrl" value="popup/image.do?mId=1&fnIdx=${siteInfo.popup_fn_idx}"/>
			<c:forEach var="popupDt" items="${popupDashboardList}" varStatus="i">
				<c:choose>
					<c:when test="${popupDt.POP_TYPE == '2'}">
						<c:set var="linkUrl" value="${contextPath}/${crtSiteId}/popup/popup.do?mId=1&popIdx=${popupDt.POP_IDX}"/>
						<c:set var="beginALink">
							<a href="${linkUrl}" class="fn_pop_view" data-title="<c:out value="${popupDt.SUBJECT}"/>" data-properties="<c:if test="${!empty popupDt.POP_TOP}">top=<c:out value="${popupDt.POP_TOP}"/>px<c:set var="isProperties" value="${true}"/></c:if><c:if test="${!empty popupDt.POP_LEFT}"><c:if test="${isProperties}">,</c:if>left=<c:out value="${popupDt.POP_LEFT}"/>px<c:set var="isProperties" value="${true}"/></c:if><c:if test="${isProperties}">,</c:if>width=<c:out value="${popupDt.POP_WIDTH}"/>px,height=<c:out value="${popupDt.POP_HEIGHT}"/>px">
						</c:set>
					</c:when>
					<c:when test="${popupDt.POP_TYPE == '1' && !empty popupDt.LINK_URL}">
						<c:set var="linkUrl" value="${popupDt.LINK_URL}"/>
						<c:set var="beginALink">
							<a href="<c:out value="${linkUrl}"/>" target="${popupDt.LINK_TARGET}" data-title="<c:out value="${popupDt.SUBJECT}"/>">
						</c:set>
					</c:when>
					<c:otherwise>
						<c:set var="beginALink" value=""/>
					</c:otherwise>
				</c:choose>
				<c:if test="${!empty beginALink}">
					<c:set var="endALink" value="</a>"/>
				</c:if>
				${beginALink}<img src="${popupUrl}&id=${elfn:imgNSeedEncrypt(popupDt.IMG_SAVED_NAME)}" rel="mimg${i.count}"/>${endALink}
			</c:forEach>
		</div> 
	</div>
	
	<script type="text/javascript">
	$(function(){
		$(".fn_pop_view").click(function(){
				fn_dialog.openP($(this).attr("data-title"), $(this).attr("href"), '', $(this).attr("data-properties"));
				return false;
		});
	});
	</script>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "../include/main_bottom.jsp" flush = "false"/></c:if>