<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false"/>
</c:if>
<div class="sContent">
	<div id="cms_member_article">
		<div class="titTypeA">회원가입 유형을 선택하세요.</div>
		<div class="register_wrap">
			<ul>
				<li class="line">
					<div class="reg_icon1"></div>
					<h4>일반회원가입</h4>
					<p>만 14세 이상의 개인회원</p>
					<a href="<c:out value="${URL_JOIN01}"/>" title="일반회원가입 만 14세 이상의 개인회원 가입하기" class="btnTypeAL">회원가입하기</a>
				</li>
				<li>
					<div class="reg_icon2"></div>
					<h4>14세미만 회원가입</h4>
					<p>만 14세 이하의 개인회원</p>
					<a href="<c:out value="${URL_JOIN21}"/>" title="14세미만 회원가입 만 14세 이하의 개인회원 가입하기" class="btnTypeAL">회원가입하기</a>
				</li>
				<div class="cboth"></div>
			</ul>
		</div>
		<div class="mgt40">
		<p class="titTypeB">만 14세 미만 회원가입</p>
			<p class="titTypeD mgt10">만 14세 미만은 법률에 의거하여 보호자(법적대리인)의 동의가 필요합니다.</p>
		</div>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>