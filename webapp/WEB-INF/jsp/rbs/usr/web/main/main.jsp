<%@ include file="../include/commonTop.jsp"%>

<c:if test="${!empty TOP_PAGE}">
	<jsp:include page = "../include/main_top.jsp" flush = "false">
		<jsp:param name="javascript_page" value="../include/main.jsp"/>
	</jsp:include>
</c:if>


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



	<!-- 해양대  -->
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
		
		<div class="m_chart_bx"> <!-- 기본상태는 maskbx클래스 추가. 학업이수현황 오픈은 해당 클래스 제거 -->
			<div class="m_chart1 maskbx">
				<div class="inbx grade blur">
					<h3 class="">나의 평균성적</h3>
			        <div class="chart_box">
			            <canvas id="chartGPA" height="220px"></canvas>    
			        </div>
				</div>
				<div class="inbx grade blur">
					<h3 class="">학점 상세</h3>
                   	<div class="chart_box">
                   		<canvas id="chartCdtDetail" height="180px"></canvas>
                   	</div>
                   	<a href="/web/member/myInfo.do?mId=5" id="cdtDetailView" class="hidden">학업 이수현황 상세보기</a>
				</div>
				<div class="inbx">
					<h3>전공능력<a href="https://cts.kmou.ac.kr/ko/about/capa/major" title="전공능력에 대해서" target="_blank" class="mchart_3_info">?</a></h3>
                      <div class="chart_box">
                      		<canvas id="chartMajorDiagnosis" height="220px"></canvas>
                      		<div class="empty_box d-flex mt-5 mb-5 flex-column w-100 align-items-center justify-content-center hidden" style="margin-top:8rem !important;">
                            	<p class="text-center text-dark">
                            		'전공능력진단' 정보가 없습니다.</br>
                            		『Ocean-CTS』<b>'전공능력'</b>을 진단해주세요.</br>
                            		<a class='go_oceancts_major px-2' href='javascript:' onclick="goOceanCts('https://cts.kmou.ac.kr/ko/diagnosis/major')" title='Ocean-CTS' target='_blank' style="margin-top:1rem;">바로가기</a>
                            	</p>
							</div>
                      </div>
				</div>
				<div class="inbx">
					<h3>핵심역량<a href="https://cts.kmou.ac.kr/ko/about/capa/essential" title="핵심역량에 대해서" target="_blank" class="mchart_4_info">?</a></h3>
                      <div class="chart_box">
                      		<canvas id="chartCoreDiagnosis" height="220px"></canvas>
                      		<div class="empty_box d-flex mt-5 mb-5 flex-column w-100 align-items-center justify-content-center hidden" style="margin-top:8rem !important;">
                            	<p class="text-center text-light">
                            		'핵심역량진단' 정보가 없습니다.<br>
                            		『Ocean-CTS』<b>'핵심역량'</b>을 진단해주세요.<br>
                            		<a class='go_oceancts_essential px-2' href='javascript:' onclick="goOceanCts('https://cts.kmou.ac.kr/ko/diagnosis/essential')" title='Ocean-CTS' target='_blank' style="margin-top:1rem;">바로가기</a>	
                            	</p>
							</div>
                      </div>
				</div>
				<div class="mask">
					<img src="../images/lock.png" alt=" ">
					<h3>학업정보 보호 브라우징이 잠겨 있음</h3>
					<p class="textbx"><a href="#">잠금 <b>해제</b></a></p>
				</div>
			</div>
			
			<div class="m_chart2">
               	<ul class="summ_info" id="myInfo">

				</ul>
			</div>
			
			<ul class="m_chart3">
				<li class="m_c_1"><a href="/web/member/myInfo.do?mId=5&target=myDegr-tab">학생설계전공</a></li>
				<li class="m_c_2"><a href="/web/member/myInfo.do?mId=5&target=hashTg-tab">해시태그</a></li>
				<li class="m_c_3"><a href="/web/member/myInfo.do?mId=5&target=myLov-tab">나의찜</a></li>
				<li class="m_c_4"><a href="/web/member/myInfo.do?mId=5&target=shoBag-tab">장바구니</a></li>
			</ul>
		</div>
	</div>
			
	<section class="main_L_bottom">
       	<form method="post" id="sbjtView" name="sbjtView">
          	<input type="hidden" name="mId" value="32">
     		<input type="hidden" name="SUBJECT_CD" value="">
			<input type="hidden" name="DEPT_CD" value="">
			<input type="hidden" name="YEAR" value="">
			<input type="hidden" name="SMT" value="">
				
            <!--추천교과목-->
            <div class="item item_reco_subj" id="divRecommSubject">
            	<div class="d-flex flex-row align-items-center mb-3 gap-2">
                       <h4 class="title"><span class="student-nm"></span>님을 위한 AI <i>추천 교과목</i></h4>
                   </div>
                   
                   <div class="item_reco_etc">
	                   <a href="/web/member/myInfo.do?mId=5&target=hashTg-tab" title="해시태그 설정하기" class="go_hash">해시태그 설정하기</a>
	                   <a href="#layer2" title="추천방식" class="vote_method btn-example">추천방식</a>
	                   <div class="recomm_btn">
	                       <input type="checkbox" class="checkbox" id="chkComDiv" value="0"/>
	                       <div class="knobs"></div>
	                       <div class="layer"></div>
	                   </div>
                   </div>
                   
                   <ul class="nav nav-recomm" id="pills-tab" role="tablist">
                       <li class="nav-item" role="presentation">
                         <button class="nav-link active" id="recomm-home-tab" data-bs-toggle="pill" data-bs-target="#recomm-home" type="button" role="tab" aria-controls="recomm-home" aria-selected="true">전체</button>
                       </li>
                       <li class="nav-item" role="presentation">
                         <button class="nav-link" id="recomm-data-tab" data-bs-toggle="pill" data-bs-target="#recomm-data" type="button" role="tab" aria-controls="recomm-data" aria-selected="false">졸업이수<br class="d-block d-sm-none"/>기반</button>
                       </li>
                       <li class="nav-item" role="presentation">
                         <button class="nav-link" id="recomm-simi-tab" data-bs-toggle="pill" data-bs-target="#recomm-simi" type="button" role="tab" aria-controls="recomm-simi" aria-selected="false">유사사용자<br class="d-block d-sm-none"/>기반</button>
                       </li>
                       <li class="nav-item" role="presentation">
                         <button class="nav-link" id="recomm-pers-tab" data-bs-toggle="pill" data-bs-target="#recomm-pers" type="button" role="tab" aria-controls="recomm-pers" aria-selected="false">개인선호도<br class="d-block d-sm-none"/>기반</button>
                       </li>
                       <li class="nav-item" role="presentation">
                         <button class="nav-link" id="recomm-majr-tab" data-bs-toggle="pill" data-bs-target="#recomm-majr" type="button" role="tab" aria-controls="recomm-majr" aria-selected="false">전공능력<br class="d-block d-sm-none"/>기반</button>
                       </li>
                       <li class="nav-item" role="presentation">
                         <button class="nav-link" id="recomm-core-tab" data-bs-toggle="pill" data-bs-target="#recomm-core" type="button" role="tab" aria-controls="recomm-core" aria-selected="false">핵심역량<br class="d-block d-sm-none"/>기반</button>
                       </li>
                   </ul>
                   <div class="tab-content" id="pills-tabContent">
                  		<!--추천교과목 탭 전체-->
                       <div class="tab-pane fade show active" id="recomm-home" role="tabpanel" aria-labelledby="recomm-home-tab" tabindex="0">
                            <p class="text-center subjectLoaderText">
                            	<!-- <span class="">교과목 추천중 ...</span> -->
                            </p>
                           	<div class="subjectLoader"></div>
                           <div class="box_wrap">
                           </div>
                           <div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 교과가 없습니다.</p>
						</div>
                       </div>
                       
                       <!--추천교과목 탭 졸업이수기반-->
                       <div class="tab-pane fade" id="recomm-data" role="tabpanel" aria-labelledby="recomm-data-tab" tabindex="0">
                            <p class="text-center subjectLoaderText">
                           		<!-- <span class="">교과목 추천중 ...</span> -->
                            </p>
                           	<div class="subjectLoader"></div>                       	
                           <div class="box_wrap">                               
                           </div>
                           <div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           	
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 교과가 없습니다.</p>
						</div>
                       </div>
                       
                        <!--추천교과목 탭 유사사용자기반-->
                       <div class="tab-pane fade" id="recomm-simi" role="tabpanel" aria-labelledby="recomm-simi-tab" tabindex="0">
                            <p class="text-center subjectLoaderText">
                            	<!-- <span class="">교과목 추천중 ...</span> -->
                            </p>
                           	<div class="subjectLoader"></div>                       
                           <div class="box_wrap">                               
                           </div>
                           <div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           	                           
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 교과가 없습니다.</p>
						</div>
                       </div>
                       
                        <!--추천교과목 탭 개인선호도기반-->
                       <div class="tab-pane fade" id="recomm-pers" role="tabpanel" aria-labelledby="recomm-pers-tab" tabindex="0">
                            <p class="text-center subjectLoaderText">
                            	<!-- <span class="">교과목 추천중 ...</span> -->
                            </p>
                           	<div class="subjectLoader"></div>                       
                           <div class="box_wrap">
                           </div>
						<div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           							
                           	<p class="text-center">추천교과가 없습니다.<br/>해시태그를 설정해 주세요.</p>
							<a href="/web/member/myInfo.do?mId=5&target=hashTg-tab" title="해시태그 설정하기" class="mt-2">해시태그 설정하기</a>
						</div>
                       </div>
                       
                        <!--추천교과목 탭 전공능력기반-->
                       <div class="tab-pane fade" id="recomm-majr" role="tabpanel" aria-labelledby="recomm-majr-tab" tabindex="0">
                            <p class="text-center subjectLoaderText">
                            	<!-- <span class="">교과목 추천중 ...</span> -->
                            </p>
                           	<div class="subjectLoader"></div>                              
                           <div class="box_wrap">                               
                           </div>
						<div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           							
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 교과가 없습니다.</p>
						</div>
                       </div>
                       
                        <!--추천교과목 탭 핵심역량기반-->
                       <div class="tab-pane fade" id="recomm-core" role="tabpanel" aria-labelledby="recomm-core-tab" tabindex="0">
                            <p class="text-center subjectLoaderText">
                            	<!-- <span class="">교과목 추천중 ...</span> -->
                            </p>
                           	<div class="subjectLoader"></div>                              
                           <div class="box_wrap">                               
                           </div>
						<div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           							
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 교과가 없습니다.</p>
						</div>
                       </div>
                   </div>
               </div>
		</form>	
		
		<div class="dim-layer">
			<div class="dimBg"></div>
			<div id="layer2" class="pop-layer">
				<div class="pop-container">
					<div class="pop-conts">
					<!--content //-->
						<div class="btn-r"><a class="btn-layerClose"><img src="../images/ico_b_close.png"></a></div>
						<img src="../images/cts_img01.png">
						<!-- div class="dim_con_01">
							<img src="../images/">
							<h3 class="dim_con_title01">해양대 교과/비교과 추천 방식은?</h3>
							<p class="dim_con_subtitle01">한국해양대 지능형 교과/비교과 검색 추천 서비스 시스템에 적용된 추천 방식에 대해 소개합니다.</p>
							<div class="dim_con_innerbox_grey01">
								<h3 class="dim_con_title02">추천 시스템이란, 사용자(User)가 선호하는 상품(Item)을 예측하는 시스템!</h3>
								<p class="dim_con_subtitle02">※ 사용자 = 학생, 상품 = 전공, 교양, 비교과</p>
							</div>
						</div>
						<div class="dim_con_02">
							<img src="../images/">
							<h3 class="dim_con_title02"><span>대표적인</span> 추천 알고리즘</h3>
							<p class="dim_con_subtitle02">추천 시스템이란 인터넷이 발전됨에 따라 아이템 구매 및 선호에 대한 사용자의 피드백을 얻기 쉬워졌으며, 이러한 피드백을 바탕으로  과거의 사용자 – 아이템 간 데이터를 분석하여 아이템을 추천하는 것이 추천 시스템의 기본적인 아이디어</p>
							<div class="dim_con_innerbox_half">
								<div class="dci_half_L">
									<div class="dci_half_L_txt">
										<h3 class="dim_con_title02">협업 필터링</h3>
										<p class="dim_con_subtitle03">Collaborative Filtering</p>
										<p class="dim_con_subtitle04">두 명의 사용자가 비슷한 관심사를 가지고 있다면, 한 사용자의 데이터를 바탕으로 다른 사용자에게 추천해주는 방식, 사용자 간의 선호도를 고려하여 많은 선택사항들로부터 아이템을 걸러내거나 선택함</p>
									</div>
									<img src="../images/">
								</div>
								<div class="dci_half_R">
									<div class="dci_half_R_txt">
										<h3 class="dim_con_title02">컨텐츠 기반 필터링</h3>
										<p class="dim_con_subtitle03">Content-based Filtering</p>
										<p class="dim_con_subtitle02">협업 필터링과 다르게, 사용자가 경험했던 아이템 중 비슷한 아이템을 추천하는 방식, 정보를 찾는 과정과 과거 정보를 활용해서 유저의 성향을 배우는 방법</p>
									</div>
									<img src="../images/">
								</div>
							</div>
						</div>
						<div class="dim_con_03">
							<img src="../images/">
							<h3 class="dim_con_title02">지능형 교과/비교과 검색 추천 시스템에 <span>적용된</span> 추천 알고리즘</h3>
							<p class="dim_con_subtitle02">추천 시스템이란 인터넷이 발전됨에 따라 아이템 구매 및 선호에 대한 사용자의 피드백을 얻기 쉬워졌으며, 이러한 피드백을 바탕으로  과거의 사용자 – 아이템 간 데이터를 분석하여 아이템을 추천하는 것이 추천 시스템의 기본적인 아이디어</p>
							<div class="dim_con_innerbox_grey02">
								<h3 class="dim_con_title02">멀티 스테이지 하이브리드 추천 알고리즘​</h3>
								<p class="dim_con_subtitle03">Multi-Stage Hybrid Recommender Algorithm</p>
								<p class="dim_con_subtitle02">컨텐츠 기반 필터링과 협업 필터링의 단점을 보완하기 위한 하이브리드 방식 적용​. 수많은 추천 아이템 후보군에서, 추천 목적 및 각 사용자의 특성(학적상태, 졸업요건 등)에 따른 개인화 추천을 위해 멀티 스테이지(Ranking, Re-ranking) 알고리즘 적용​</p>		
								<img src="../images/">
							</div>
						</div>
						<div class="dim_con_04">
							<img src="../images/">
							<h3 class="dim_con_title02"><span>개인화</span> 추천을 위한 특성 정보</h3>
							<div class="dim_con_innerbox_half">
								<div class="dci_half_L">
									<div class="dci_half_L_txt">
										<h3 class="dim_con_title02">사용자 및 아이템 정보에 따른 특성​</h3>
										
										<p class="dim_con_subtitle02"><b>1) 사용자의 이력이 존재하는 상품은 추천에서 제외(재수강 가능 항목은 추천)​</b></p>
										<p class="dim_con_subtitle02">단, 이력이 존재하지 않는 사용자(신입생, 편입생 등)의 경우에는 소속학부/학과 선배 중 프로필 정보가 가장 유사한 사용자들의 이력을 기반으로 추천(프로필 정보란 성별, 학생신분, 소속학부/학과, 전공 등의 데이터를 의미)​</p>
										
										<p class="dim_con_subtitle02"><b>2) 아이템 정보에 따른 특성​​</b></p>
										<p class="dim_con_subtitle02">개설강좌조회가 가능한 시점을 기준으로 현재 학기에 개설된 아이템을 추천(계절학기는 제외)​</p>
									
									</div>
								</div>
								<div class="dci_half_R">
									<div class="dci_half_R_txt">
										<h3 class="dim_con_title02">추천 목적(방식)에 따른 특성​</h3>
										<p class="dim_con_subtitle02"><b>1) 사용자의 이력이 존재하는 상품은 추천에서 제외(재수강 가능 항목은 추천)​</b></p>
										<p class="dim_con_subtitle02">단, 이력이 존재하지 않는 사용자(신입생, 편입생 등)의 경우에는 소속학부/학과 선배 중 프로필 정보가 가장 유사한 사용자들의 이력을 기반으로 추천(프로필 정보란 성별, 학생신분, 소속학부/학과, 전공 등의 데이터를 의미)​</p>
									</div>
								</div>
							</div>
						</div-->
						
						
					<!--// content-->
					</div>
				</div>
			</div>
		</div>




		<!--추천비교과-->
		<form method="post" id="nonSbjtView" name="nonSbjtView">
          		<input type="hidden" name="mId" value="34">
     			<input type="hidden" name="idx" value="">
				<input type="hidden" name="tidx" value="">
				
               <div class="item item_reco_nonSubj position-relative" id="divRecommNonCourse">
                   <div class="d-flex flex-row align-items-center mb-3 gap-2">
                       <h4 class="title"><span class="student-nm"></span>님을 위한 AI <i>추천 비교과</i></h4>
                   </div>
                   
                   <div class="item_reco_etc">
	                   <a href="javascript:" onclick="goOceanCts('https://cts.kmou.ac.kr/')" title="[새창]OceanCTS" target="_blank" class="go_hash">오션 CTS 바로가기</a>
	                   <a title="추천방식" class="vote_method btn-example" href="#layer2">추천방식</a>
	                   <div class="recomm_btn">
	                       <input type="checkbox" class="checkbox" id="chkPeriod" value="0"/>
                       		<div class="present"></div>
                       		<div class="past"></div>
	                   </div>
                   </div>
                   
                   
                   
                   <ul class="nav nav-recomm" id="pills-tab" role="tablist">
                       <li class="nav-item" role="presentation">
                         <button class="nav-link active" id="recomm-home2-tab" data-bs-toggle="pill" data-bs-target="#recomm-home2" type="button" role="tab" aria-controls="recomm-home2" aria-selected="true">전체</button>
                       </li>
                       <li class="nav-item" role="presentation">
                         <button class="nav-link" id="recomm-pcore2-tab" data-bs-toggle="pill" data-bs-target="#recomm-pcore2" type="button" role="tab" aria-controls="recomm-pcore2" aria-selected="false">핵심역량[강점]<br class="d-block d-sm-none"/>기반</button>
                       </li>
                       <li class="nav-item" role="presentation">
                         <button class="nav-link" id="recomm-wcore2-tab" data-bs-toggle="pill" data-bs-target="#recomm-wcore2" type="button" role="tab" aria-controls="recomm-wcore2" aria-selected="false">핵심역량[약점]<br class="d-block d-sm-none"/>기반</button>
                       </li>
                       <li class="nav-item" role="presentation">
                         <button class="nav-link" id="recomm-simi2-tab" data-bs-toggle="pill" data-bs-target="#recomm-simi2" type="button" role="tab" aria-controls="recomm-simi2" aria-selected="false">유사사용자<br class="d-block d-sm-none"/>기반</button>
                       </li>
                       <li class="nav-item" role="presentation">
                         <button class="nav-link" id="recomm-pers2-tab" data-bs-toggle="pill" data-bs-target="#recomm-pers2" type="button" role="tab" aria-controls="recomm-pers2" aria-selected="false">개인선호도<br class="d-block d-sm-none"/>기반</button>
                       </li>
                   </ul>
	
                   <div class="tab-content" id="pills-tabContent">
                   	<!--추천비교과 탭 전체-->
                       <div class="tab-pane fade show active" id="recomm-home2" role="tabpanel" aria-labelledby="recomm-home2-tab" tabindex="0">
                            <p class="text-center nonSubjectLoaderText">
                            	<!-- <span class="">비교과 추천중 ...</span> -->
                            </p>
    						<div class="nonSubjectLoader"></div>
                           <div class="box_wrap"> 
                           </div>
                           <div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           	                           
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 비교과가 없습니다.</p>
						</div>
                       </div>
                       
                       <!--추천비교과 탭 핵심역량기반(강점 강화)-->
                       <div class="tab-pane fade" id="recomm-pcore2" role="tabpanel" aria-labelledby="recomm-pcore2-tab" tabindex="0">
                            <p class="text-center nonSubjectLoaderText">
                            	<!-- <span class="">비교과 추천중 ...</span> -->
                            </p>
    						<div class="nonSubjectLoader"></div>                       
                           <div class="box_wrap">
                           </div>
                           <div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           	                           
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 비교과가 없습니다.<br/><b>핵심역량진단</b>을 진행해주세요.</p>
						</div>
                       </div>
                       
                       <!--추천비교과 탭 핵심역량기반(약점 보완)-->
                       <div class="tab-pane fade" id="recomm-wcore2" role="tabpanel" aria-labelledby="recomm-wcore2-tab" tabindex="0">
                            <p class="text-center nonSubjectLoaderText">
                            	<!-- <span class="">비교과 추천중 ...</span> -->
                            </p>
    						<div class="nonSubjectLoader"></div>                       
                           <div class="box_wrap">
                           </div>
                           <div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           	                          
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 비교과가 없습니다.<br/><b>핵심역량진단</b>을 진행해주세요.</p>
						</div>
                       </div>
                       
                       <!--추천비교과 탭 유사사용자기반-->
                       <div class="tab-pane fade" id="recomm-simi2" role="tabpanel" aria-labelledby="recomm-simi2-tab" tabindex="0">
                            <p class="text-center nonSubjectLoaderText">
                            	<!-- <span class="">비교과 추천중 ...</span> -->
                            </p>
    						<div class="nonSubjectLoader"></div>                       
                           <div class="box_wrap">                               
                           </div>
                           <div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           	                           
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 비교과가 없습니다.</p>
						</div>
                       </div>
                       
                       <!--추천비교과 탭 개인선호도기반-->
                       <div class="tab-pane fade" id="recomm-pers2" role="tabpanel" aria-labelledby="recomm-pers2-tab" tabindex="0">
                            <p class="text-center nonSubjectLoaderText">
                            	<!-- <span class="">비교과 추천중 ...</span> -->
                            </p>
    						<div class="nonSubjectLoader"></div>                       
                           <div class="box_wrap">                               
                           </div>
                           <div class="empty_box d-flex flex-column align-items-center justify-content-center hidden">                           	                          
                           	<p class="text-center"><span class="student-nm"></span>님을 위한 AI추천 비교과가 없습니다.<br/><b>해시태그</b>를 설정해주세요.</p>
							<a href="/web/member/myInfo.do?mId=5&target=hashTg-tab" title="해시태그 설정하기" class="mt-2">#해시태그 설정하기</a>
						</div>
                       </div>
                   </div>
			</div>
		</form>
	</section>
	
	<section class="m_commbx">
		<h4 class="title">커뮤니티</h4>
		<!--커뮤니티-->
		<div class="m_comm_L">
			<!--커뮤니티 탭 전체-->
			<ul class="m_comm_news" id="commu-home" role="tabpanel" aria-labelledby="commu-home-tab">
				<li>
					<strong class="comm-tag d-inline-block me-1 notice">공지사항</strong>
					<a href="#" title="게시물 제목" class="">★2024학년도 1학기 글로벌커넥트 카페 (GCC)운영 일정 안내★</a>
					<p>RIS스마트항만물류사업단-동원글로벌터미널부산, 업무협약 체결 스마트항만물류 혁신인재 양성을 위한 산·학·연 협력 국립한국해양대학교 RIS 스마트항만물류 사업단(단장 김율성)의 </p>
					<span>2024-01-01</span>
				</li>
				<li>
					<strong class="comm-tag d-inline-block me-1 supervision">장학</strong>
					<a href="#" title="게시물 제목" class="">★2024년 우수학생 국가장학금(인문100년,예술체육비전) 학생 사전 신청 안내(2024.3.19.~4.5. 18시까지)★</a>
					<p>RIS스마트항만물류사업단-동원글로벌터미널부산, 업무협약 체결 스마트항만물류 혁신인재 양성을 위한 산·학·연 협력 국립한국해양대학교 RIS 스마트항만물류 사업단(단장 김율성)의 </p>
					<span>2024-01-01</span>
				</li>
			</ul>
			<div class="m_stubx">
				<p>학생설계전공이란?</p>
				<span>학생 스스로가 여러 다른 학과의 전공과목들로 새로운 전공을 설계하여 이를 주도적으로 <br>학습해 나가는 신개념 교육과정</span>
				<a href="/web/studPlan/list.do?mId=36">학생설계전공 작성하기</a>
			</div>
		</div>

            <!--학사일정-->
             <div class="item_calendar m_comm_R">
				<input type="hidden" id="gubun" value="load">
             	<input type="hidden" id="scheYmd" value="202405">
             	
                 <div class="sch_dtl p-2">
                 	<p>학사일정</p>
                     <div class="" id="shafScheList">
                     </div>
                 </div>
				
				<table class="con04_cal_list" id="calendar">
					<tr>
						<td>
							<a href="javascript:prevCalendar();" class="con04_cal_btn_left"><span class="blind">이전달</span></a>
						</td>
						<td id="tbCalendarYM" colspan="5">yyyy년 m월</td>
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
					
				</table>
             </div>            
        </section>
        
    	<div class="modal fade modal-xl modal_syllabus" id="syllabusModal" tabindex="-1" aria-labelledby="syllabusModalLabel" aria-hidden="true">
		    <div class="modal-dialog modal-dialog-scrollable">
		      <div class="modal-content">
		        <div class="modal-header bg-black">
		            <h1 class="modal-title fs-5 text-white" id="syllabusModalLabel">교과목 개설강좌</h1>
		            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
		                <img src="../images/ico_w_close.png" alt="닫기 아이콘"/>
		            </button>
		        </div>
		        <div class="modal-body">
		            <div class="d-flex flex-wrap gap-4" id="mainBasList">
		            </div>
		        </div>
		        <!-- <div class="modal-footer d-flex flex-row align-items-center justify-content-center ">
		            <button type="button" id="detailBookmart" class="p-3 d-flex flex-row align-items-center justify-content-center gap-2 bg-transparent">
		                <img src="../images/modal_ico_fill_lov.png" id="fillLov" alt="찜하기 아이콘"/>
		                <img src="../images/modal_ico_r_lov.png" id="rLov" alt="찜하기 아이콘"/>
		                찜하기
		            </button>
		            <button type="button" id="detailBasket"  class="p-3 d-flex flex-row align-items-center justify-content-center gap-2 bg-transparent">
		                <img src="../images/modal_ico_fill_sho.png" id="fillSho" alt="장바구니 아이콘"/>
		                <img src="../images/modal_ico_b_sho.png" id="bSho" alt="장바구니 아이콘"/>
		                장바구니
		            </button>
		        </div> -->
		      </div>
		    </div>
		  </div>
        
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "../include/main_bottom.jsp" flush = "false"/></c:if>