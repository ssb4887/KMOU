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
			<h4 class="titTypeA">비밀번호 찾기를 완료하였습니다.</h4>
		</div>
		<div class="loginWrap">
			<ul class="pwSearchComp">
				<li>임시비밀번호를 발행하여 <span>${pwsearchMbrEmail}</span>으로 발송완료하였습니다.</li>
			</ul>
		</div>	
	</div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>