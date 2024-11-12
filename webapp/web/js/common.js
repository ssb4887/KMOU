$(function () {

	//history.pushState(null, null, location.href);
	//window.onpopstate = function(event){
	//	history.go(1);
	//};


	//history.replaceState({}, null, location.pathname); 

    $(".sel_box button").on("click", function(){
        $(".sel_box ul").stop().slideToggle(500);
        $(this).stop().toggleClass("rtt");
    });

    $(".aram_info button").on("click" , function(){
        $(this).siblings(".aram_wrap").stop().slideToggle(500);
    });
    $(".aram_wrap .item").hide();
    $(".aram_wrap .item").slice(0, 3).css("display" , "block");
    $(".view_more > a").click(function(e){
        e.preventDefault();
        $(".aram_wrap .item:hidden").slice(0, 1).fadeIn(200).css('display', 'block'); // 클릭시 more 갯수 지저정
        if($(".aram_wrap .item:hidden").length == 0){ // 컨텐츠 남아있는지 확인
            $(".view_more").fadeOut(100); // 컨텐츠 없을 시 버튼 사라짐
        }
    });
    
/*0418 수정*/
   	$(".aram_wrap .item .title").on("click", function(){
        $(this).siblings(".conts").slideToggle();
        $(this).siblings(".dat_tras").children("button").fadeToggle();
    });
   



    // let delay = 300;
    // let timer = null;
    var resize = "";

    $(window).on("resize", function(){ 
        resize = window.innerWidth;
		if(1199 < resize){ //1199이상
			$(".search_word").removeClass("hide");
	        $(".search_cont").removeClass("on");
	        $(".header_wrap").removeClass("pd , pb-10");
		};
        if(767 < resize && resize < 1199){ //767이상 1199이하
            $(".search_word").removeClass("hide");
	        $(".search_cont").removeClass("on");
	        $(".header_wrap").removeClass("pd , pb-10");
        };
        // if(resize < 767){//767이하
        //     $(".header-wrap").removeClass("pb-10");
        //     $(".search_cont").removeClass("on");
        // }

    }).resize();


  $(function (){
        $(".search_cont").on("mousedown" , function(){ //서치창 포커스 얻음
            if(767 < resize && resize < 1199){ //767이상, 1199이하 태블릿 사이즈
                $(this).addClass("on"); //서치컨텐츠 열림
                $(".header_wrap").addClass("pb-5"); //헤더패딩 추가
            }
            
        }).on("focusout", function(){ //서치창 포커스 잃음
            if(767 < resize && resize < 1199){ //767이상, 1199이하 태블릿 사이즈
                $(this).removeClass("on"); //서치컨텐츠 닫힘
                $(".header_wrap").removeClass("pb-5"); // 헤더패딩 제거
            };
        });
        
        $(".search_ico").on("click" , function(){ //아이콘 클릭시 
            var search_open = $(".search_wrap .search_cont");
            
            if(resize < 767){ //767이하 모바일사이즈
                if(search_open.hasClass("on") === true){
                    //서치컨텐츠에 on 이 있으면 == 서치컨텐츠가 열려있는 상태
                    search_open.removeClass("on"); //서치컨텐츠 on을 제거해 서치컨텐츠 닫힘
                    $(".header_wrap").removeClass("pb-10"); //헤더에 패딩 제거
                }else{ 
                    //서치컨텐츠에 on 이 없으면 == 서치컨텐츠가 닫힌 상태
                    search_open.addClass("on"); //서치컨텐츠 on을 추가해 서치컨텐츠 오픈
                    $(".header_wrap").addClass("pb-10"); //헤더에 패딩 추가
                }
            }
            
        });
    
  })  


   var scroll= "";
    $(window).scroll(function() { //윈도우 스크롤하면
        scroll = $(window).scrollTop(); 
        $(".header_wrap").removeClass("change , pd"); //헤더에 패딩+아랫쪽선+배경색 없앰 == 패딩X + 선X + 배경색 흰색(투명도100)

        if (scroll >= 50) { //스크롤위치 50이상
            $(".header_wrap").addClass("change"); //헤더배경색 투명도90
            $(".goTop_btn").addClass("on");
            
            if(1199 < window.innerWidth){ //스크롤위치 50이상 + 1199이상 사이즈
                $(".search_word").addClass("hide"); //추천검색어 없어지기
                $(".header_wrap").removeClass("pd"); // 헤더에 패딩+아랫쪽선+배경색 없어지기
                $(".sel_box").removeClass("rtt");
                $(".sel_box > ul").css("display", "none");
            };
            
        }else if(scroll == 0) { //스크롤 위치 0일때 
            if(1199 < window.innerWidth){ //1199이상 사이즈
                $(".search_word").removeClass("hide");
                $(".header_wrap").removeClass("pd");
            }
            
        }else {
            $(".header_wrap").removeClass("change , pd");
            $(".search_word").removeClass("hide");
            $(".goTop_btn").removeClass("on");
        };
        
        
    });

    $(".search_cont").on("mousedown" , function(){ // 서치컨텐츠 마우스다운
        if (scroll >= 50){ 
            if(1199 < window.innerWidth){ // 스크롤 50 이상, 1199이상일떄
                $(".search_word").removeClass("hide");  // 추천검색어 보이기
                $(".header_wrap").addClass("pd"); //헤더 패딩추가하기
            }
        }
    }).on("blur", function(){
        if (scroll > 0){ 
            if(1199 < window.innerWidth){ //스크롤 0이상, 1199이상일때
                $(".search_word").addClass("hide");   
                $(".header_wrap").removeClass("pd");
            }
        }
    });
    
    //사이드네비
    $(".hambug").on("click", function(){
        $(".side_wrap").stop().addClass("open");
        $("body").addClass("overflow-hidden");
        
        if($(".side_wrap").hasClass("open")){
            $(document).mouseup(function (e){
                if($(".side_nav").has(e.target).length === 0){
                    $(".side_nav").parents(".side_wrap").removeClass("open");
                    $("body").removeClass("overflow-hidden");
                };
            });
        };
    });
    $(".close_ham").on("mousedown", function(){
        $(".side_wrap").stop().removeClass("open");
        $("body").removeClass("overflow-hidden");
    });

    $(".have_dept a").on("click", function(){
        $(this).siblings(".menu_dep2").stop().slideToggle();
        $(this).stop().toggleClass("bt_round");
    });

    $(".goTop_btn").on("click", function(){
        $("html, body").animate({
            scrollTop:0
        }, 200);
    });

    //툴팁
    $('[data-bs-toggle="tooltip"]').tooltip();

});
// $(function) , $(document).ready -- 태그의 로드만 완료 후 동작

// 이녀석은 HTML 의 모든 데이터를 로드 후 동작
window.onload = function(){
    var scroll = $(window).scrollTop();
    
    $(".header_wrap").removeClass("change , pd");
    if (scroll >= 50) {
        $(".header_wrap").addClass("change");
        if(1199 < window.innerWidth){
            $(".search_word").addClass("hide");
            $(".search_cont").on("mousedown" , function(){
                $(".search_word").removeClass("hide");  
                $(".header_wrap").addClass("pd");
            }).on("blur", function(){
                $(".search_word").addClass("hide");   
                $(".header_wrap").removeClass("pd");
            });
        }
        $(".goTop_btn").addClass("on");
    } else {
        $(".header_wrap").removeClass("change , pd");
        $(".search_word").removeClass("hide");
        $(".goTop_btn").removeClass("on");
    };
}

