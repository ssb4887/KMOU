<%-- <%@ include file="../include/commonTop.jsp"%> --%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="cssPath" value="${siteLocalPath}/css"/>
<c:set var="cssAssetPath" value="${siteLocalPath}/assets/css"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page = "../include/top.jsp" flush = "false">
		<jsp:param name="javascript_page" value="../include/privacyPolicy.jsp"/>
	</jsp:include>
</c:if>
<link rel="stylesheet" type="text/css" href="${contextPath}${cssAssetPath}/sub.css">
<script src="${contextPath}${jsAssetPath}/sub.js"></script>

<div class="container_wrap" style="padding-top:50px;">
	<div class="sub_wrap">
		<div class="sub_background major_bg">
			<section class="inner">
				<h3 class="title fw-bolder text-center text-white">정보제공동의</h3>
			</section>
		</div>
		<!--본문-->
		<section class="inner mt-4">
			<h5 class="px-2 py-3 mb-2 text-center fw-bolder etc_title">개인정보처리방침</h5>
			<p class="mb-4">국립한국해양대학교(이하 "본 대학" 이라 한다)는 「개인정보 보호법」 제30조에 따라 정보주체의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리방침을 수립·공개합니다.</p>
			<div class="etc_section">
				<h6>제1조(개인정보의 처리목적)</h6>
				<ul>
					<li>&#9312; 본 대학은 다음의 목적을 위하여 개인정보를 처리합니다. 처리하고 있는 개인정보는 다음의 목적 이외의 용도로는 이용되지 않으며, 이용 목적이 변경되는 경우에는 「개인정보 보호법」 제18조에 따라 별도의 동의를 받는 등 필요한 조치를 이행할 예정입니다.</li>
					<li>
						<dl class="ps-2">
							<dt class="fw-bold text-dark">&bull; 회원 가입 및 관리</dt>
							<dd class="ps-2 mb-1">회원제 서비스 이용에 따른 본인확인, 개인 식별, 불량회원의 부정이용 방지와 비인가 사용방지, 가입의사 확인, 연령확인, 분쟁 조정을 위한 기록 보존, 불만처리 등 민원처리, 고지사항 전달 등을 목적으로 개인정보를 처리</dd>
							<dt class="fw-bold text-dark">&bull; 입시 및 학사 업무 처리</dt>
							<dd class="ps-2 mb-1">입시전형 업무, 신입생 선발, 학사관리 및 학교행정 업무, 외국인 유학생 관리 등</dd>
							<dt class="fw-bold text-dark">&bull; 각종 서비스 제공</dt>
							<dd class="ps-2 mb-1">연구용 서버 회원관리, 웹메일 서버 회원관리, 홈페이지 자동제작 지원, 실험분석수수료 고지 및 안내 지원, 교육훈련 관리 등</dd>
							<dt class="fw-bold text-dark">&bull; 발전기금 기탁자 관리</dt>
<!-- 							<dd class="ps-2 mb-1">발전기금 기탁자 관리 및 기부금 영수증 발급 등</dd> -->
<!-- 							<dt class="fw-bold text-dark">&bull; 본 대학 사이버교육센터(중앙공무원교육원 사이버교육센터 공동활용) 교육생 관리</dt> -->
							<dd></dd>
<!-- 							<dt class="fw-bold text-dark">&bull; 본 대학 사이버교육센터 가입된 교육생의 교육훈련 수강신청 및 이수처리 등 교육훈련 관리</dt> -->
							<dd></dd>
						</dl>
					</li>
					<li class="mt-4">
						&#9313; 본 대학이 「개인정보 보호법」 제32조에 따라 등록·공개하는 개인정보파일 항목은 개인정보보호 종합지원포털
						<a href="https://www.privacy.go.kr/front/main/main.do" target="_blank" title="개인정보포털 바로가기">(www.privacy.go.kr)</a>
						의 개인정보민원 → 개인정보 열람 등 요구 → 개인정보파일 목록검색 → 기관명에 "국립한국해양대학교" 입력 후 조회할 수 있습니다.
					</li>
				</ul>
			</div>
		</section>
		<section class="plan_button_wrap d-flex flex-wrap justify-content-center gap-2 gap-md-3 mt-4">
			<button onclick="privacyPolicyAgree()" type="button" class="border-0 py-3" style="background-color: #003C92;">동의</button>
		</section>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "../include/bottom.jsp" flush = "false"/></c:if>