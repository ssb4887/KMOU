<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="memberInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/join01.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
		<jsp:param name="joinStep" value="1"/>
	</jsp:include>
</c:if>
<div class="sContent">
	<div id="cms_member_article">
		<jsp:include page="joinStep.jsp" flush="false"/>
		
		<c:choose>
			<c:when test="${elfn:isUseNameAuth()}"><c:set var="formAction" value="${URL_JOIN02}"/></c:when>
			<c:otherwise><c:set var="formAction" value="${URL_JOIN03}"/></c:otherwise>
		</c:choose>
		
		<!-- 약관동의 -->
		<div class="terms">
		<form name="${inputFormId}" id="${inputFormId}" method="get" action="<c:out value="${formAction}"/>">
			<div class="terms">
				<p class="tit" style="width:100%; display:inline-block;"/><span class="fl titTypeB"><itui:objectItemName itemId="mAgree" itemInfo="${itemInfo}"/></span><span style="float:right;"><input type="checkbox" name="selectAll" id="selectAll"/><label for="selectAll">전체 동의</label></span>
				<div class="termsBox"><c:out value="${elfn:replaceHtmlN(clauseDt.MEMBER_CLAUSE)}" escapeXml="false"/></div>
				<div class="agreeChk">
					<span class="txt">* 위의 <itui:objectItemName itemId="mAgree" itemInfo="${itemInfo}"/>에 대하여 동의하십니까?</span>
					<itui:objectRadio itemId="mAgree" itemInfo="${itemInfo}"/>
				</div>
			</div>
			<div class="terms mgt70">
				<p class="tit" style="width:100%; display:inline-block;"><span class="fl titTypeB"><itui:objectItemName itemId="piAgree" itemInfo="${itemInfo}"/></span>
				<div class="termsBox"><c:out value="${elfn:replaceHtmlN(clauseDt.MEMBER_PERINFO)}" escapeXml="false"/></div>
				<div class="agreeChk">
					<span class="txt">* 위의 <itui:objectItemName itemId="piAgree" itemInfo="${itemInfo}"/>에 대하여 동의하십니까?</span>
					<itui:objectRadio itemId="piAgree" itemInfo="${itemInfo}"/>
				</div>
			</div>
			
			<div class="termsBtn">
				<input type="submit" class="btnOrganSearch" value="확인" alt="확인" title="확인" />
				<a href="<c:out value="${URL_JOIN}"/>" class="btnTypeB">취소</a>
			</div>
		</form>
		</div>
		<!-- //약관동의 -->	
	</div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>