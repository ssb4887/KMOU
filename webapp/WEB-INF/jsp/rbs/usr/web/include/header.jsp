<!-- header -->
	<%@page import="com.woowonsoft.egovframework.util.MenuUtil"%>
<%-- <div id="headerWrap">
		<div id="util">
			<div class="utilWrap">
				<div class="utilBtn">
					<c:choose>
						<c:when test="${elfn:isLoginAuth()}">
							<c:out value="${loginVO.memberNameOrg}"/>님 반갑습니다!
							<a href="<%=MenuUtil.getMenuUrl(4) %>">로그아웃</a>
							<c:if test="${elfn:isLogin()}">
							<a href="<%=MenuUtil.getMenuUrl(5) %>">내정보수정</a>
							</c:if>
						</c:when>
						<c:otherwise>
							<a href="<%=MenuUtil.getMenuUrl(3) %>"><img src="<c:out value="${contextPath}${imgPath}/layout/top_login.gif"/>" alt="로그인" /></a>
							<a href="<%=MenuUtil.getMenuUrl(2) %>"><img src="<c:out value="${contextPath}${imgPath}/layout/top_join.gif"/>" alt="회원가입" /></a>
						</c:otherwise>
					</c:choose>				
				</div>
			</div>

			<div id="header">
				<h1><a href="<c:out value="${contextPath}${indexUrl}"/>"><img src="<c:out value="${contextPath}${imgPath}/layout/logo.gif"/>" alt="R.biz v1.0" /></a></h1>
				<!-- gnb -->
				<div id="gnbWrap">
					<h2 class="blind">주메뉴</h2>
					<mnui:gnb ulId="gnb" gid="${crtMenu.menu_idx2}" sid="${crtMenu.menu_idx3}" menuList="${siteMenuList}" menus="${siteMenus}" />
				</div>
				<!-- //gnb -->
			</div>
		</div>
	</div> --%>
	<!-- //header -->

	<script>
	let notiList = {};
	$(function(){	
		getNoti(4);
		
        $(document).on('mouseenter', '.title a', function() {
            var notiMsg = $(this).data('noti_msg');
            var shortNotiMsgElement = $(this).closest('li').find('.shortNotiMsg');
            
            shortNotiMsgElement.data('original-content', shortNotiMsgElement.text());
            shortNotiMsgElement.text(notiMsg);
        });

        $(document).on('mouseleave', '.title a', function() {
            var shortNotiMsgElement = $(this).closest('li').find('.shortNotiMsg');
            var originalContent = shortNotiMsgElement.data('original-content');
            
            shortNotiMsgElement.text(originalContent);
        });
	});
		
	function totSearch(){
		if(SearchTextValidation('top_search')){	document.searchForm.submit(); }
		else{ return false; }
	}
	
	function SearchTextValidation(searchBoxName){
		var searchValue = $('input[name=' + searchBoxName + ']').val(); 
		if(searchValue === '' || searchValue === undefined){
			alert("검색어를 입력해주세요.");
			return false;
		}else{
			return true;			
		}
	}
	
	function getNoti(cnt) {
		if("${loginVO.memberId}" != ''){
		    $.ajax({
		        type: 'POST',
		        beforeSend: function (request) {
		            request.setRequestHeader('Ajax', 'true');
		        },
		        url: '/web/studPlan/getNoti.do?mId=36',
		        success: function (data) {
	        	 	notiList = data.NOTI;     
	        	 	if(notiList.length > 0){
	        	 		$("#notiCnt").removeClass("hidden");
	        	 		viewNoti(cnt);
	        	 	}else{
	        	 		$("#notiCnt").addClass("hidden");
	        	 		$("#notiList").append('<p>알림이 없습니다.</p>');
	        	 	}
		        }
		    });			
		}
	}
	
	function viewNoti(cnt){				
		
        var ulElement = $('#notiList');
        var ulElement = $('#notiList');
        var viewMoreElement = '<li class="view_more pb-0 mb-0 flex-column align-items-center justify-content-center">';
        	viewMoreElement += '<a href="#" onclick="javascript:viewNoti('+(cnt+4)+');" title="더보기" class="p-1 w-100 text-center pe-3">더보기</a>';
        	viewMoreElement += '</li>';
        	
        ulElement.empty(); // 기존 리스트 초기화				

        for (var i = 0; i < Math.min(notiList.length, cnt); i++) {
            var noti = notiList[i];
            var liClass = noti.CNF_YN === 'N' ? 'not_see' : '';

            var shortMsg = noti.NOTI_MSG.replace(/(<([^>]+)>)/ig, "").substring(0, 20); // 태그 제거 후 자르기
            if (shortMsg.length > 20) {
                shortMsg += '...';
            }
            var replacedNotiMsg = noti.NOTI_MSG.replace(/(<([^>]+)>)/ig, "\r\n");

            var liElement = '<li class="item ' + liClass + ' flex-column gap-1" >' +
                '<strong class="title position-relative" onclick="notiMsgCnf(\'' + noti.NOTI_MSG_NO + '\')">' +
            	'<a href="#" title="알림제목" data-noti_msg="'+replacedNotiMsg+'" id="' + noti.NOTI_MSG_NO + '" class="ps-2 position-relative">' + noti.NOTI_TTL + '</a></strong>' +
                '<span class="conts shortNotiMsg">내용: ' + shortMsg + '</span>' +
                '<span class="dat_tras d-flex flex-row justify-content-between align-items-center">등록일: '  +noti.NOTI_FR_DT + '</span>' +
                '<button class="bg-white border-0 px-1" onclick="notiDel(\'' + noti.NOTI_MSG_NO + '\','+cnt+')"><img src="../images/trash.png" alt="휴지통아이콘"/></button>' +
                '</li>';
                

            ulElement.append(liElement);
        }

        if (notiList.length > cnt) {
            ulElement.append(viewMoreElement);
        }
                       

        // 스크롤을 맨 아래로 이동
        ulElement.scrollTop(ulElement.prop("scrollHeight"));
	}	
	
	function notiMsgCnf(notiMsgNo){
		var notiMsg = $("#"+notiMsgNo).data('noti_msg').replace(/(<([^>]+)>)/ig, "");
// 		if(confirm(notiMsg)){			
		    $.ajax({
		        url: '/web/studPlan/notiMsgCnf.do?mId=36',
			 	contentType:'application/json',
		        type: 'POST',
		        beforeSend: function (request) {
		            request.setRequestHeader('Ajax', 'true');
		        },
		        data: JSON.stringify({NOTI_MSG_NO : notiMsgNo}),
		        success: function (data) {
					if(data.result > 0){
						$("#"+notiMsgNo).removeClass("not_see")
					}
		        }
		    });		
// 		}
	        
	}
	
	function notiDel(notiMsgNo, cnt){
	    $.ajax({
	        url: '/web/studPlan/notiMsgDel.do?mId=36',
		 	contentType:'application/json',
	        type: 'POST',
	        beforeSend: function (request) {
	            request.setRequestHeader('Ajax', 'true');
	        },
	        data: JSON.stringify({NOTI_MSG_NO : notiMsgNo}),
	        success: function (data) {
				if(data.result > 0){
					getNoti(cnt);
				}
	        }
	    });		
	        
	}
	
	$('.btnSearch').click(function(){
		//$('.btnTotalMenu').removeClass('on');
		//$('#totalMb').hide();
		if($('+ .searchForm',this).is(':hidden')){
			$(this).addClass('on').next().slideDown('fast');;
			$(".btnMenu").removeClass('on');
			$("#totalMb").removeClass("on");
		}else{
			$(this).removeClass('on').next().slideUp('fast');
			
		}
		$('.searTxt').focus();
		return false;
	});
	
	 $(document).ready(function() {
         $('.user_info button').click(function() {
             $('.myinfo_wrap').slideToggle();
         });
     });
	 
	 $(document).ready(function() {
         $('.tsearch_info button').click(function() {
             $('.searchForm').slideToggle();
             $('.searchForm').addClass("on");
         });
     });
	</script>	
     
	<!-- top -->
	<header class="full_inner header_bx">

        <section class="hambug ">
            <a href="#none" title="사이드메뉴" class="w-100 h-100 d-flex flex-column justify-content-around">
                <span></span>
                <span></span>
                <span></span>
            </a>
        </section>

        <section class="search_wrap">
        	<div class="gnb_bx">
	            <!--로고-->
	            <a href="/web" title="해양대학교 학사지원시스템" class="logo"><img src="../images/logo.png" alt="logo"/></a>
	
	            <ul class="gnb">
	            	<li><a href="<%=MenuUtil.getMenuUrl(32) %>">교과목(전공&middot;교양)</a></li>
	            	<li><a href="<%=MenuUtil.getMenuUrl(34) %>">비교과</a></li>
	            	<li><a href="<%=MenuUtil.getMenuUrl(35) %>">전공</a></li>
	            	<li><a href="<%=MenuUtil.getMenuUrl(36) %>">학생설계전공</a></li>
	            	<li><a href="<%=MenuUtil.getMenuUrl(33) %>">교수</a></li>
	            	
	            	<li><a href="<%=MenuUtil.getMenuUrl(39) %>">커뮤니티</a>
	            		<ul>
	            			<li><a href="<%=MenuUtil.getMenuUrl(39) %>">공지사항</a></li>
	            			<li><a href="<%=MenuUtil.getMenuUrl(42) %>">자료실</a></li>
	            			<li><a href="<%=MenuUtil.getMenuUrl(40) %>">FAQ</a></li>
	            			<li><a href="<%=MenuUtil.getMenuUrl(41) %>#">Q&A</a></li>
	            			<li><a href="<%=MenuUtil.getMenuUrl(67) %>">오류신고</a></li>
	            			<li><a href="<%=MenuUtil.getMenuUrl(43) %>">취업정보</a></li>
	            		</ul>
	            	</li>
	            	<c:if test="${loginVO.usertypeIdx eq '5' }">
	            	<li><a href="<%=MenuUtil.getMenuUrl(5) %>">내정보수정</a></li>
	            	</c:if>
	            	<c:if test="${loginVO.usertypeIdx ne '5' }">
	            	<li><a href="/RBISADM">관리자페이지</a></li>
	            	</c:if>
	            </ul>
            </div>
        </section>

        <!--사용자아이콘-->
        <section class="brief_cont">
            <ul class="d-flex flex-row">
            	<li class="tsearch_info">
            		<button type="button" class="btnSearch"><span>검색</span></button>
					<form name="searchForm" method="POST" action="../search/total.do?mId=31">
						<div class="searchForm">
							<div class="box"><input type="text" name="top_search" class="gnb_totalsearch" placeholder="원하시는 검색어를 입력하세요."><a href="#" onclick="totSearch();" title="검색" class="tsearch_info_search msrch_btn" type="button\">검색</a></div>
						</div>
					</form>
            	</li>
                <li class="aram_info position-relative">
                    <button class="d-flex flex-row gap-1 align-items-center bg-transparent border-0">
                        <i><img src="../images/ico_aram.png" alt="종모양 아이콘" class="d-inline-block"/></i>
                        <span class="d-flex flex-row align-items-center">
                            <span class="d-none d-xl-block"></span>
                            <strong class="aram_numb rounded-circle bg-danger text-white d-inline-flex justify-content-center align-items-center ms-1" id="notiCnt"></strong>
                        </span>
                    </button>
                    <ul class="aram_wrap position-absolute bg-white" id="notiList" style="max-height: 600px; overflow-y: auto; overflow-x: hidden;">

                    </ul>
                </li>
                <li class="user_info">
                    <button class="d-flex flex-row gap-1 align-items-center bg-transparent border-0">
                        <i><img src="../images/ico_user.png" alt="사용자아이콘"/></i>
                        <span class="d-none d-xl-block">
                            <strong><c:out value="${loginVO.memberNameOrg}"/></strong><span class="sch_numb me-2"><c:out value="${loginVO.memberIdOrg}"/></span>님
                        </span>
                    </button>
                    <ul class="position-absolute bg-white myinfo_wrap" id="myList" style="max-height:600px; overflow-y:auto; overflow-x:hidden; display:none;">

		            	<c:if test="${loginVO.usertypeIdx ne '5' }">
		            	<li><a href="/RBISADM">관리자페이지 이동</a></li>
		            	</c:if>
                    	<li><a href="<%=MenuUtil.getMenuUrl(4) %>">로그아웃</a></li>
                    </ul>
                </li>
            </ul>
        </section>

        <!--사이드네비-->
        <div class="side_wrap"> 
            <section class="side_nav">
                <button class="close_ham position-absolute">
                    
                </button>
                <div class="menu_info d-flex align-items-center justify-content-between gap-2">
                    <div class="img_box bg-white rounded-circle">
                        <img src="../images/profile_photo.png" alt="프로필사진">
                    </div>
                    <div class="name_box">
                        <h5><c:out value="${loginVO.memberNameOrg}"/></h5>
                        <p>${loginVO.memberId }</p>
                        <strong id="myInfoDeptMajor"></strong>
                    </div>
                    <div class="btn_mpg"><a href="#">마이페이지로 가기</a></div>
                    <ul class="gnb_mylist">
                    	<li><a href="#">해시태그</a></li>
                    	<li><a href="#">나의찜</a></li>
                    	<li><a href="#">장바구니</a></li>
                    </ul>
                </div>

                <nav class="menu_wrap">
                    <ul class="menu_dep1 d-flex flex-column">
                        <li>
                            <a href="<%=MenuUtil.getMenuUrl(32) %>" title="교과목(전공.교양)">교과목(전공&middot;교양)</a>
                        </li>
                        <li>
                            <a href="<%=MenuUtil.getMenuUrl(34) %>" title="비교과">비교과</a>
                        </li>
                        <li>
                            <a href="<%=MenuUtil.getMenuUrl(33) %>" title="교수">교수</a>
                        </li>
                        <li>
                            <a href="<%=MenuUtil.getMenuUrl(35) %>" title="전공">전공</a>
                        </li>
                        <li>
                            <a href="<%=MenuUtil.getMenuUrl(36) %>" title="학생설계전공">학생설계전공</a>
                        </li>
                        <li class="have_dept">
                            <a href="#none" title="커뮤니티">커뮤니티</a>
                            <ul class="menu_dep2">
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(39) %>">공지사항</a>
                                </li>
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(40) %>">FAQ</a>
                                </li>
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(41) %>">Q&amp;A</a>
                                </li>
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(42) %>">자료실</a>
                                </li>
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(43) %>">취업정보</a>
                                </li>
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(67) %>">오류신고</a>
                                </li>
                            </ul>
                        </li>
                        <c:if test="${loginVO.usertypeIdx eq '5' }">
                        <li class="have_dept">
                            <a href="#none" title="마이페이지">마이페이지</a>
                            <ul class="menu_dep2">
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(5) %>">기본정보</a>
                                </li>
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(5)%>&target=myLov-tab">나의 찜</a>
                                </li>
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(5)%>&target=shoBag-tab">장바구니</a>
                                </li>                                
                                <li>
                                    <a href="<%=MenuUtil.getMenuUrl(5)%>&target=hashTg-tab">해시태그 관리</a>
                                </li>
                            </ul>
                        </li>
                        </c:if>
                    </ul>
                </nav>
                
                <a href="<%=MenuUtil.getMenuUrl(4) %>" title="로그아웃" class="lgout_btn rounded-2">LOGOUT</a>

            </section>
        </div>

		
        
    </header>