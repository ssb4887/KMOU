<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="mnui" tagdir="/WEB-INF/tags/menu/adm" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="vewAuth" value="${elfn:isAuth('VEW')}"/>
<c:set var="searchFormId" value="fn_authSearchForm"/>
<c:set var="listFormId" value="fn_authListForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/list.jsp"/>
		<jsp:param name="searchFormId" value="${searchFormId}"/>
		<jsp:param name="listFormId" value="${listFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<div id="lnbWrap">
			<div class="infoWrap">
				<div class="info">
					<h4><c:out value="${siteInfo.site_name}"/> 메뉴</h4>
					<c:if test="${!empty admSiteMenuList}">
					<c:choose>
						<c:when test="${!mngAuth && vewAuth}">
							<c:set var="inputUrl" value="${URL_VIEW}"/>
						</c:when>
						<c:when test="${mngAuth}">
							<c:set var="inputUrl" value="${URL_MODIFY}"/>
						</c:when>
						<c:otherwise>
							<c:set var="inputUrl" value=""/>
						</c:otherwise>
					</c:choose>
					<mnui:authMenuAll ulId="fn_admMenuUL" ulClass="lnbM" menuList="${admSiteMenuList}" menus="${admSiteMenus}" inputUrl="${inputUrl}" target="fn_iframe_input"/>
					</c:if>
				</div>
			</div>
		</div>
		<div class="inWfullContWrap">
			<div class="tcomment" id="fn_comment">
				<p><c:out value="${siteInfo.site_name}"/> 메뉴를 클릭하시면 해당메뉴의 권한을 관리하실 수 있습니다.</p>
			</div>
			
			<iframe title ="<c:out value="${siteInfo.site_name}"/> 입력폼" id="fn_iframe_input" name="fn_iframe_input" style="width:100%;height:670px;border:0px; display:none;" frameBorder="0"></iframe>
		</div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>