<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="memberInputForm"/>
<c:choose>
	<c:when test="${elfn:isUseNameAuth()}"><c:set var="joinStep" value="4"/></c:when>
	<c:otherwise><c:set var="joinStep" value="3"/></c:otherwise>
</c:choose>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false"/>
</c:if>
<div class="sContent">
	<div id="cms_member_article">
		<div class="findTit">
			<h4 class="titTypeA">아이디 찾기를 완료하였습니다.</h4>
		</div>
		<div class="loginWrap">
			<ul class="idSearchComp">
				<li>입력하신 정보와 일치하는 아이디는 <span>${elfn:getReplace(idsearchMbrId, '*', 4)}</span>입니다.</li>
				<li>(개인정보보호를 위해 아이디를 4자리만 표기하였으며, 끝자리는 *로 표시하였습니다.)</li>
			</ul>
		</div>
		<div class="idSearchCompBtn">
			<a href="<%=MenuUtil.getMenuUrl(8) %>" class="btnTypeA">비밀번호 찾기</a>
		</div>
	</div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>