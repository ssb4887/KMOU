<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page = "../include/main_top.jsp" flush = "false">
	<jsp:param name="javascript_page" value="../include/profMain.jsp"/>
	</jsp:include>
</c:if>

<link rel="stylesheet" type="text/css" href="${contextPath}${cssAssetPath}/main.css">

<style>

	/*추천검색어*/
	.search_word_box {
	    overflow: hidden;
	    width: 100%; /* 컨테이너의 전체 너비를 사용 */
	}
	
	.s_wordbx {
	    display: flex;
	    align-items: center;
	    width: 100%;
	}
	
	.s_w_title {
	    flex-shrink: 0;
	    margin-right: 10px; /* 'HOT'과 추천 검색어 사이의 간격 */
	}
	
	.s_w_content {
	    flex-grow: 1;
	    overflow: hidden;
	}
	
	#searchSuggestions {
	    position: relative;
	    height: 1.5em; /* 한 줄의 높이 */
	    overflow: hidden;
	}
	
	.suggestion-group {
	    position: absolute;
	    left: 0;
	    right: 0;
	    white-space: nowrap;
	    transition: transform 0.5s ease, opacity 0.5s ease;
	    text-align: left;
	}
	
	.suggestion-group.entering {
	    transform: translateY(100%);
	    opacity: 0;
	}
	
	.suggestion-group.leaving {
	    transform: translateY(-100%);
	    opacity: 0;
	}
	
	.suggestion-group a {
	    margin-right: 30px; /* 각 키워드 사이의 간격 */
	    display: inline-block;
	}
	
	/*로딩바*/
	.loader-container {
	    position: relative;
	    min-height: 100px;
	}
	
	.subjectLoader, .nonSubjectLoader {
	    border: 5px solid #d3d3d3; /* 밝은 파란색 배경 */
	    border-top: 5px solid #1976d2; /* 진한 파란색 */
	    border-radius: 50%;
	    width: 50px;
	    height: 50px;
	    animation: spin 2s linear infinite;
	    position: absolute;
	    top: 50%;
	    left: 50%;
	    transform: translate(-50%, -50%);
	    box-shadow: 0 0 10px rgba(25, 118, 210, 0.2); /* 부드러운 그림자 효과 */
	}
	
	@keyframes spin {
	    0% { transform: translate(-50%, -50%) rotate(0deg); }
	    100% { transform: translate(-50%, -50%) rotate(360deg); }
	}
</style>

<form id="frmView" name="frmView" method="post" action="">
	<input type="hidden" name="mId" value="36">
	<input type="hidden" id="SDM_CD" name="SDM_CD" value="">
	<input type="hidden" id="REVSN_NO" name="REVSN_NO" value="">
</form>
	<!-- 해양대 교수html -->
	
	<div class="main_bx">
		<span class="fs-3" style=" font-weight: 600;color: #1d46d7;">지능형 교과·비교과 검색 추천 서비스</span>
		<p class="main_ctxt">해양의 미래를 선도하는 플랫폼 대학</p>
		<span>A platform university leading the future of the ocean</span>
		
		<div class="main_searchbx">
            <form name="mainSearchForm" method="POST" action="../search/total.do?mId=31">
            <input type="hidden" name="mId" id="mId" value="31"/>
            <input type="hidden" name="isMainSearch" value="Y"/>
            <div class="search_cont input-group">
		       <div class="sel_box">
		            <button type="button" class="" id="searchCondition">통합검색</button>
		            <ul id="listSc">
		                <li><a href="#" onclick="sCChange('all')" title="통합검색">통합검색</a></li>
		                <li><a href="#" onclick="sCChange('sbjt')" title="전공·교양">전공·교양</a></li>
		                <li><a href="#" onclick="sCChange('nonSbjt')" title="비교과">비교과</a></li>
		                <li><a href="#" onclick="sCChange('major')" title="전공">전공</a></li>
		                <li><a href="#" onclick="sCChange('prof')" title="교수">교수</a></li>
		            </ul>
		        </div>
                <span class="d-block d-md-none d-xl-block"></span>
	            <input class="form-control border-0 py-3 rounded-pill msrch_input" type="text" name="main_search" placeholder="검색어를 입력하세요."/>
	            <a href="#" onclick="mainSearch();" title="검색" class="text-center msrch_btn" type="button">검색</a>

                
            </div>
			<div class="search_word_box">
			    <ul class="s_wordbx" id="popularKeword">
			        <li class="s_w_title">추천검색어<i>HOT</i></li>
			        <li class="s_w_content">
			            <div id="searchSuggestions"></div>
			        </li>
			    </ul>
			</div>
            
            <!--  button type="submit" class="search_ico position-relative d-block d-xl-none border-0 bg-transparent">
                <img src="../images/ico_search.png" alt="돋보기아이콘"/>
            </button -->
            </form>
		</div>
		
		<div class="ps_main_bx">
			<div class="psmain_img">
				<ul>
					<li class="ps_li01"><img src="/web/assets/images/psmain_img02.png" alt="핵심으로 파고드는 학업이수 모니터링"><img src="/web/assets/images/psmain_img07.png" alt="핵심으로 파고드는 학업이수 모니터링"></li>
					<li class="ps_li02"><img src="/web/assets/images/psmain_img03.png" alt="학습AI로 효율적인자기주도 학습설계"><img src="/web/assets/images/psmain_img08.png" alt="학습AI로 효율적인자기주도 학습설계"></li>
					<li class="ps_li03"><img src="/web/assets/images/psmain_img04.png" alt="개인맞춤 키워드로 추천받는 교과검색"><img src="/web/assets/images/psmain_img09.png" alt="개인맞춤 키워드로 추천받는 교과검색"></li>
				</ul>
			</div>
			<div class="psmain_myinfo">
				<div class="section_haeyang"><img src="/web/assets/images/psmain_img01.png" alt="경례하는 해양이캐릭터"></div>
				<div class="section_myinfo">
					<p class="personal_info">
						<span class="ps_ptag">교수</span>
						<span class="ps_pname">${loginVO.memberName}</span>
						<span class="ps_pscript">님, 환영합니다!</span>
						<span class="ps_pbelong">${loginVO.collNm} > ${loginVO.deptNm }</span>
					</p>
					<button class="btn_mypinfo"></button>
				</div>
			</div>
		</div>
	
	<main class="item item_main item_psmain">
		<!--추천교과목-->
		<div class="noStud_main pf_plan_proc mt-3 position-relative">
			<h4 class="title mb-3">컨설팅 진행&amp;완료된 설계전공</h4>
			<ul class="cate_proc px-3 px-md-0 px-lg-3 py-3 d-flex flex-wrap justify-content-center align-itmes-start ">
				<c:forEach items="${ALL_TAB_CNT }" var="item">
				<c:choose>
					<c:when test="${item.TYPE eq 'RA'}">
	                	<li><a href="javaScript:" onclick="getTab('RA');" title="승인" class="pr_all active cnsltTab" id="RATab">승인요청<span id="RACnt">${item.COUNT}</span></a></li>
					</c:when>
					<c:when test="${item.TYPE eq 'RC'}">
	                	<li><a href="javaScript:" onclick="getTab('RC');" title="요청" class="pr_cons cnsltTab" id="RCTab">상담요청<span id="RCCnt">${item.COUNT}</span></a></li>
					</c:when>
					<c:when test="${item.TYPE eq 'AC'}">
	                	<li><a href="javaScript:" onclick="getTab('AC');" title="참여" class="pr_cons cnsltTab" id="ACTab">참여가능상담<span id="ACCnt">${item.COUNT}</span></a></li>
					</c:when>
					<c:when test="${item.TYPE eq 'WC'}">
	                	<li><a href="javaScript:" onclick="getTab('WC');" title="진행" class="pr_cons cnsltTab" id="WCTab">상담진행<span id="WCCnt">${item.COUNT}</span></a></li>
					</c:when>
					<c:when test="${item.TYPE eq 'CC'}">
	                	<li><a href="javaScript:" onclick="getTab('CC');" title="완료" class="pr_cons cnsltTab" id="CCTab">상담완료<span id="CCCnt">${item.COUNT}</span></a></li>
					</c:when>
					<c:when test="${item.TYPE eq 'WJ'}">
	                	<li><a href="javaScript:" onclick="getTab('WJ');" title="심사진행" class="pr_sims cnsltTab" id="WJTab">심사진행 <span id="WJCnt">${item.COUNT}</span></a></li>
					</c:when>
					<c:when test="${item.TYPE eq 'CJ'}">
	                	<li><a href="javaScript:" onclick="getTab('CJ');" title="심사완료" class="pr_sims cnsltTab" id="CJTab">심사완료<span id="CJCnt">${item.COUNT}</span></a></li>
					</c:when>
				</c:choose>
				</c:forEach>	                
			</ul>

			<div class="pf_planWrap" id="cnsltTabList">
				<c:if test="${not empty RA }">
					<div class="box_wrap">
						<!--item :: 기본-->
						<c:forEach var="ra" items="${RA }">
							<div class="item <c:if test="${ra.STATUS eq '30'}">on_approve</c:if> <c:if test="${ra.STATUS eq '33'}">reject</c:if> <c:if test="${ra.STATUS gt '33'}">approve</c:if> p-3">
								<!---->
								<div class="title_wrap d-flex flex-row mb-3 justify-content-between">
									<ul>
										<li class="title text-truncate">${ra.SDM_KOR_NM }</li>
										<li>
											<ul class="cate d-flex gap-2 flex-wrap flex-sm-nowrap">
												<li class="color_border">
	                                                <span>${ra.FIRST_MAJOR}</span>
	                                                <span>${ra.OTHER_MAJOR}</span>
												</li>
											</ul>
										</li>
									</ul>											
									<div class="btn_wrap d-flex flex-row gap-2">
										<a href="#" onclick="goModify('Approve','${ra.SDM_CD}','${ra.REVSN_NO}','${ra.APLY_FG}')" title="요청승인" class="writ_consult w-100">
										<c:if test="${ra.STATUS eq '30'}">	
											요청승인
										</c:if>
										<c:if test="${ra.STATUS gt '30'}">
											상세보기
										</c:if>
										</a>												
									</div>
								</div>
								<!---->
								<div class="info_wrap d-flex flex-wrap p-3 gap-2">
									<dl class="d-flex flex-row gap-2">
										<dt>신청학생</dt>
										<dd>${ra.APLY_STUDENT_NM }</dd>
									</dl>
									<dl class="d-flex flex-row gap-2">
										<dt>수여학위</dt>
										<dd>${ra.AWD_DEGR_KOR_NM }</dd>
									</dl>
									<br>
									<dl class="d-flex flex-row gap-2">
										<dt>설계학점</dt>
										<dd>${ra.TOTAL_CDT_NUM }학점</dd>
									</dl>
									<dl class="d-flex flex-row gap-2">
										<dt style="min-width: 80px;">교과목설계범위</dt>
										<dd>${ra.SBJT_DGN_RNG_FG_NM }</dd>
									</dl>
									<dl class="d-flex flex-row gap-2">
										<dt>신청유형</dt>
										<dd><c:if test="${ra.APLY_FG eq 'NEW' }">신설</c:if><c:if test="${ra.APLY_FG eq 'CHG' }">변경</c:if></dd>
									</dl>
								</div>
							</div>
						</c:forEach>
					</div>
				</c:if>
				
						
						
				<c:if test="${empty RA }">
					<div class="pf_no_planWrap d-flex flex-column justify-content-center align-items-center" id="">
						<p>정보가 없습니다.</p>
						<a href="/web/studPlan/list.do?mId=36" title="학생설계전공전체보기" class="text-end view_more px-3 py-2 mt-2 rounded-pill">학생설계전공 보러가기</a>
					</div>
				</c:if>
			</div>
		</div>
		<ul class="pagination gap-2 justify-content-center mt-5" id="pagination"></ul>
	</main>
	
	<section class="m_commbx psmain_combox">
		<h4 class="title">커뮤니티</h4>
		<!--커뮤니티-->
		<div class="psmain_combox_in">
			<div class="m_comm_L">
				<!--커뮤니티 탭 전체-->
				<ul class="m_comm_news" id="commu-home" role="tabpanel" aria-labelledby="commu-home-tab">
					<li>
						<a href="/web/board/view.do?mId=39&amp;NTT_SN=10348693" title="게시물 제목" class="">
							<strong class="comm-tag d-inline-block me-1 notice">공지사항</strong>
							<b>「부산광역시 청년정책조정위원회」 위원 공개 모집 안내</b>
							<p>-</p>
							<span>2024-07-30</span>
						</a>
					</li>
					<li>
						<a href="/web/board/view.do?mId=39&amp;NTT_SN=10348693" title="게시물 제목" class="">
							<strong class="comm-tag d-inline-block me-1 notice">공지사항</strong>
							<b>「부산광역시 청년정책조정위원회」 위원 공개 모집 안내</b>
							<p>-</p>
							<span>2024-07-30</span>
						</a>
					</li>
					<li>
						<a href="/web/board/view.do?mId=39&amp;NTT_SN=10348693" title="게시물 제목" class="">
							<strong class="comm-tag d-inline-block me-1 notice">공지사항</strong>
							<b>「부산광역시 청년정책조정위원회」 위원 공개 모집 안내</b>
							<p>-</p>
							<span>2024-07-30</span>
						</a>
					</li>
					<li>
						<a href="/web/board/view.do?mId=39&amp;NTT_SN=10348693" title="게시물 제목" class="">
							<strong class="comm-tag d-inline-block me-1 notice">공지사항</strong>
							<b>「부산광역시 청년정책조정위원회」 위원 공개 모집 안내</b>
							<p>-</p>
							<span>2024-07-30</span>
						</a>
					</li>
				</ul>
				
			</div>
	
			<!--학사일정-->
	        <div class="item_calendar m_comm_R">
	            <div class="sch_dtl p-2">
	            	<p>학사일정</p>
	               <div class="" id="shafScheList"><dl class="school_sch">		<dt class="d-flex flex-row gap-3"><span>6.25</span><span>7.15</span></dt>		<dd class="text-truncate">여름계절학기 수업</dd>	</dl><dl class="school_sch">		<dt class="d-flex flex-row gap-3"><span>6.29</span><span>7.3</span></dt>		<dd class="text-truncate">제1학기 성적열람</dd>	</dl><dl class="school_sch">		<dt class="d-flex flex-row gap-3"><span>7.4</span><span>7.17</span></dt>		<dd class="text-truncate">해사대학 3·4학년 제1학기 성적입력·열람 및 정정</dd>	</dl><dl class="school_sch">		<dt class="d-flex flex-row gap-3"><span>7.4</span><span>7.10</span></dt>		<dd class="text-truncate">제1학기 성적 확정 및 조회</dd>	</dl></div>
	            </div>
	
				 <table class="con04_cal_list" id="calendar">
					<tbody>
						<tr>
							<td>
								<a href="javascript:prevCalendar();" class="con04_cal_btn_left"><span class="blind">이전달</span></a>
							</td>
							<td id="tbCalendarYM" colspan="5"><h4>2024. 07</h4></td>
							<td>
								<a href="javascript:nextCalendar();" class="con04_cal_btn_right">
									<span class="blind">다음달</span>
								</a>
							</td>
						</tr>
						<tr class="cal_week">
							<th class="red">Sun</th>
							<th>Mon</th>
							<th>Tue</th>
							<th>Wen</th>
							<th>Thu</th>
							<th>Fri</th>
							<th class="text-primary">Sat</th>
						</tr>
			
						<tr>
							<td></td>
							<td class="date">1</td><td class="date">2</td><td class="date sch_end" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-html="true" title="⦁ 제1학기 성적열람 종료">3</td><td class="date sch_start" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-html="true" data-bs-original-title="⦁ 해사대학 3·4학년 제1학기 성적입력·열람 및 정정 시작<br/>⦁ 제1학기 성적 확정 및 조회 시작">4</td><td class="date">5</td><td class="date">6</td></tr><tr><td class="date red">7</td><td class="date">8</td><td class="date">9</td><td class="date sch_end" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-html="true" title="⦁ 제1학기 성적 확정 및 조회 종료">10</td><td class="date">11</td><td class="date">12</td><td class="date">13</td></tr><tr><td class="date red">14</td><td class="date sch_end" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-html="true" title="⦁ 여름계절학기 수업 종료">15</td><td class="date">16</td><td class="date sch_end" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-html="true" title="⦁ 해사대학 3·4학년 제1학기 성적입력·열람 및 정정 종료">17</td><td class="date">18</td><td class="date">19</td><td class="date">20</td></tr>
							<tr><td class="date red">21</td>
							<td class="date">22</td><td class="date">23</td>
							<td class="date">24</td><td class="date">25</td>
							<td class="date">26</td><td class="date">27</td></tr>
						<tr>
							<td class="date red">28</td>
							<td class="date">29</td>
							<td class="date">30</td>
							<td class="date">31</td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>	
	</section>
</div>   
        
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "../include/main_bottom.jsp" flush = "false"/></c:if>