<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false"/>
</c:if>
<script src="${contextPath}${jsAssetPath}/sub.js"></script>
<!--content-->
<div class="container_wrap">
	<div class="sub_wrap">
		<div class="sub_background commu_bg">
			<section class="inner">
				<h3 class="title fw-bolder text-center">Q&amp;A</h3>
			</section>
		</div>
		<!--본문-->
		<section class="inner mt-4">
			<h2 class="qna_title text-center py-3 px-2">2024-1학기 고졸 후학습자 장학금(희망사다리2유형) 신규장학생 신청 안내</h2>
			<table class="table qna_view">
				<caption class="blind">qna view 정보</caption>
				<colgroup>
					<col width="15%" />
					<col width="35%" />
					<col width="15%" />
					<col width="35%" />
				</colgroup>
				<tbody>
					<tr>
						<th scope="row" class="text-center align-middle py-3">작성자</th>
						<td class="wirt text-start align-middle py-3 ">학생복지과</td>
						<th scope="row" class="text-center align-middle py-3">작성일</th>
						<td class="date text-start align-middle py-3">2023-00-00</td>
					</tr>
					<tr class="qna_hits">
						<th scope="row" class="text-center align-middle py-3">조회수</th>
						<td colspan="3" class="text-start align-middle py-3">300</td>
					</tr>
					<tr class="qna_attc">
						<th scope="row" class="text-center align-middle">첨부파일</th>
						<td colspan="3" class="text-start align-middle attach_file px-3 px-md-2">
							<p class="d-flex flex-row align-items-center ">
								<img src="../images/ico_add.png" alt="첨부파일 아이콘" />
								<a href="#" download="파일명" class="text-truncate">붙임2. 고졸 후학습자 Q.zip</a>
								<button type="button" class="pre_view bg-white">미리보기</button>
							</p>
							<p class="d-flex flex-row align-items-center ">
								<img src="../images/ico_add.png" alt="첨부파일 아이콘" />
								<a href="#" download="파일명" class="text-truncate">붙임2. 고졸 후학습자 장학사업 신규신청 매뉴얼 및 FAQ.zip</a>
								<button type="button" class="pre_view bg-white">미리보기</button>
							</p>
						</td>
					</tr>
				</tbody>
			</table>
			<!--qna 컨텐츠 내용-->
			<div class="qna_container p-4">
				ㅇ 지원자격<br />- 다음의 조건을 모두 충족한 대학생(재학생)<br /> ① 대한민국 국적자로 최종학력이 고졸인 자<br /> ※ 전문학사 취득자가 전공심화과정 및 4년제 대학에 신편입하는 경우, ‘전문학사 취득 전’ 산업체 재직기간(군 복무기간 미포함) 2년을 별도로 충족하면 장학금 지원 가능 증빙서류 제출 필요
				<div class="answer_section">
					<h4 class="title">답변</h4>
					ㅇ 지원자격<br />- 다음의 조건을 모두 충족한 대학생(재학생)<br /> ① 대한민국 국적자로 최종학력이 고졸인 자<br /> ※ 전문학사 취득자가 전공심화과정 및 4년제 대학에 신편입하는 경우, ‘전문학사 취득 전’ 산업체 재직기간(군 복무기간 미포함) 2년을 별도로 충족하면 장학금 지원 가능 증빙서류 제출 필요
				</div>
			</div>
			<!--이전다음-->
			<section class="qna_prvNex">
				<dl class="d-flex flex-row ">
					<dt class="text-center p-2 d-flex align-items-center justify-content-center">이전</dt>
					<dd class="px-3 py-2">
						<a href="#" title="제목">2024-1학기 고졸 후학습자 장학금(희망사다리2유형) 신규장학생 신청 안내</a>
					</dd>
				</dl>
				<dl class="d-flex flex-row ">
					<dt class="text-center p-2 d-flex align-items-center justify-content-center">다음</dt>
					<dd class="px-3 py-2">
						<a href="#" title="제목">2024-1학기 고졸 후학습자 장학금(희망사다리2유형) 신규장학생 신청 안내</a>
					</dd>
				</dl>
			</section>
			<div class="d-flex flex-row gap-3 justify-content-center mt-4">
				<button type="button" class="qna_conf border-0 text-center text-white py-2">수정</button>
				<button type="button" class="qna_dele border-0 text-center text-white py-2">삭제</button>
			</div>
			<button type="button" class="qna_goLi bg-white py-2 text-center mx-auto d-block">목록으로</button>
		</section>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false" /></c:if>
