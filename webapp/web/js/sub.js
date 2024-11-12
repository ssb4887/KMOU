var link = "";

$(function () {

	//전공교양 강의계획서 모달 - 공학인증보기
	$(".ttip").on("click", function(){
        $("#enginDescript").fadeIn();
    });
    $("#enginDescript .btn-close").on("click",function(){
        $("#enginDescript").fadeOut();
    });
	
	//커뮤니티 - qna
    $("#qnaAddAtt").on('change',function(){
        var fileName = $("#qnaAddAtt").val();
        $(".upload_name").val(fileName);
    });

	
    /*0422 수정*/
    
	

    //전공·교양 서치박스
    $(".search_wrap_bbox .dtl_tit").on("click", function(){
        $(this).stop().toggleClass("open");
        $(this).siblings(".column_box_wrap").stop().slideToggle();
        $(this).parents(".row_box_dtl").stop().toggleClass("fold_conts");
    });

    $(".search_btn_wrap .btn").on("click", function(){
        $(this).stop().toggleClass("on");
    });


    //전공·교양 서치박스 -- 핵심역량
    $('input[type=range]').on('input', function(){
        var val = $(this).val();
        val = (val*33.3);
        $(this).css('background', 'linear-gradient(to right, #0038ff 0%, #0038ff '+ val +'%, #ccc ' + val + '%, #ccc 100%)');
    });

    $(".unit .form-check-input").on("click" , function(){
        if($(".unit .form-check-input").is(":checked")){
            $(this).siblings(".range").removeAttr("disabled");
        }
    });


    //전공·교양 본문
    // var desc_txt = $(".majref_wrap .desc_txt").text();
    // if(desc_txt.length >= 150){
    //     console.log(desc_txt.length);
    //     desc_txt = desc_txt.substring(0, 150);
    //     $(".majref_wrap .desc_txt").text(desc_txt+"...");
    // }else{};

    // $(".majref_wrap .desc_txt").each(function(index, item){
    //     var desc_txt = $(this).text(); 
    //     if(desc_txt.length >= 150){
    //         desc_txt = desc_txt.substring(0, 150);
    //         $(this).text(desc_txt+"...");
    //     }else{
            
    //     };
    // })

    //전공·교양 상세보기 - 타이틀 하트
    $(".like_container").on("click", function(){
        $(this).toggleClass("on_red");
    });

    //전공·교양 상세보기 - 강좌개설이력
    $(".open_history .link_wrap a").on("click" , function(e){
        e.preventDefault();
    });

    //전공·교양 상세보기 - 하단 만족도조사
    $(".sub_bottom .bottom_text button").on("click", function(){
    	$(this).stop().toggleClass("rtt");
    	$(this).siblings("p").children("span").text("열기");
        $(this).parents(".bottom_text").siblings(".botton_box").stop().slideToggle();
        if($(".sub_bottom .bottom_text button").hasClass("rtt") != true){
        	$(this).siblings("p").children("span").text("열기");
        }else{
        	$(this).siblings("p").children("span").text("접기");
        }
    });

    //전공 상세 - 인재상 테이블
    var table_border = $(".speci_talent tbody tr");
    if(table_border.children("th").is("[scope]")){
        $(this).addClass("border-top");
    }

    //마이페이지-나의찜
    $(".myP_tab_wrap .item .form-check-input").on("click" , function(){
        $(this).parents(".item").toggleClass("border-color");
    });	


    //학생설계전공
    $(".my_plan .box_wrap").slick({
        slidesToShow:3, 
        slidesToScroll:1, 
        arrows:true,
        dots:true,
        autoplay:false, 
        responsive:[
            {
              breakpoint: 991.99,
              settings:{
                slidesToShow:2, 
                infinity:false, 
                arrows:false,
              }
            },
            {
              breakpoint : 575.99,
              settings:{
                slidesToShow:1
              }
            }
          ]
    });

    //학생설계전공 -- 우측배너
    var currentPositionB = parseInt($(".right_bann").css("top"));
    var currentPositionI = parseInt($(".right_input").css("top"));

    link = $(".right_input a");
    link.on("click" , function(e){
        var target = $($(this).attr("href"));
        $('html, body').animate({
            scrollTop: target.offset().top
        },300);

        $(this).addClass('active');

        e.preventDefault();
    })
    

    //마이페이지 > 장바구니 스크롤 나타나게 하는 위치 값 조정 포지션 조정하시면됩니다
    $(window).scroll(function() {
        var position = $(window).scrollTop(); 
        $(".right_bann").stop().animate({"top":position+currentPositionB+"px"},500);
        $(".right_input").stop().animate({"top":position+currentPositionI+"px"},500);

		if(position > 150){  
            $(".myP_shopp .all_about_wrap").addClass("fix");  
        }else{  
            $(".myP_shopp .all_about_wrap").removeClass("fix");  
        }


        findPosition();
    });
    
   
    //findPosition();
    

    var resize = "";
    $(window).on("resize", function(){
        resize = window.innerWidth;
        if(resize < 1800){ // 패드 모바일
            $(".right_bann .close").addClass("if_close"); //클래스 붙여서 원형만들기
            $(".right_bann_conts").fadeOut(); // 몸통없애기
            $(".right_bann").css("right", "0"); //오른쪽에 붙이기

            $(".right_bann .close.if_close").on("click", function(){ // 클릭
                $(this).stop().toggleClass("if_close"); //클래스 
                if($(this).hasClass("if_close")){
                    $(".right_bann_conts").stop().fadeOut();
                }else{
                    $(".right_bann_conts").stop().fadeIn();
                };
            });
            
        }else{//pc 사이즈일때
            $(".right_bann .close").removeClass("if_close");
            $(".right_bann_conts").fadeIn();
            $(".right_bann .close").on("click", function(){
                $(this).stop().toggleClass("if_close");
                if($(this).hasClass("if_close")){
                    $(".right_bann_conts").stop().fadeOut();
                }else{
                    $(".right_bann_conts").stop().fadeIn();
                };
            });
        };
        
        if(resize < 1520){ 
            //마이페이지 장바구니-우측버튼wrap
	        if($("#shoBag-tab").hasClass("active") == true){
	            $(".all_about_wrap22").addClass("d-flex");
	            $(".all_about_wrap22").removeClass("d-none");
	        }else{
	            $(".all_about_wrap22").addClass("d-none");
	            $(".all_about_wrap22").removeClass("d-flex");
	        }
        }else{
            $(".all_about_wrap22").removeClass("d-flex");
            $(".all_about_wrap22").addClass("d-none");
        }
        

        if(767 < resize ){
            $(".modal_enrollSubj .nav-link").on("click" , function(){
                var selcBoxHei = $(".find_height").height();
                var he = 600 + selcBoxHei;
                $(".modal_enrollSubj .selected_box .lesson_wrap").css({"height" : ""+he+"px"});
            });
        };

        //학생설계전공상세 -- 우측인풋
        if(window.innerWidth < 575){
        $(".right_input .title").on("click", function(){
            $(this).siblings("ul").stop().slideToggle();
            $(this).stop().toggleClass("rtt")
        });
    };
        
        
        
    }).resize();


    //학생설계전공상세 -- 과목등록 모달 check
    $(".modal_enrollSubj .lesson_wrap .item .form-check-input").on("click", function(){
        if($(this).is(":checked")){
            $(this).parents(".item").css("background" , "#EBF5FF");
        }else{
            $(this).parents(".item").css("background" , "#fff");
        } 
    });

    var scroll= "";
    $(".modal_enrollSubj .lesson_wrap").scroll(function() {
        scroll = $(".modal_enrollSubj .lesson_wrap").scrollTop(); 

        var scrollTop = $(".modal_enrollSubj .lesson_wrap").scrollTop();
        var innerHeight = $(".modal_enrollSubj .lesson_wrap").innerHeight();
        var scrollHeight = $(".modal_enrollSubj .lesson_wrap").prop('scrollHeight');

        $(this).stop().addClass("shad");
        if (scrollTop + innerHeight >= scrollHeight) {
            $(this).stop().removeClass("shad");
        }; 

        

    });
   


});
//전공·교양 서치박스 -- 핵심역량
function rangeSlide(value) {
    document.getElementById('std_rangeValue1').innerHTML = value;
}
// 이녀석은 HTML 의 모든 데이터를 로드 후 동작
window.onload = function(){

    
};

function findPosition(){
    
    $(".plan_dtl_section").each(function(index, item){
        //console.log(index);
        //console.log($(this).offset().top - $(window).scrollTop());    
        if( ($(this).offset().top - $(window).scrollTop() ) < 20){
            link.removeClass('active');
            $(".right_input").find('[data-scroll="'+ $(this).attr('id') +'"]').addClass('active');
        }
    });
}


//마이페이지 장바구니 버튼 아래로 붙이기
document.addEventListener('DOMContentLoaded', function() {
    const fixedElement = document.querySelector('.all_about_wrap');
    const offsetFromBottom = -200; // Scroll condition set to this value from bottom of the document.

    function checkScrollPosition() {
        if (!fixedElement) return;

        const scrollTop = window.scrollY || document.documentElement.scrollTop;
        const scrollHeight = document.documentElement.scrollHeight;
        const clientHeight = document.documentElement.clientHeight;

        // Distance from the bottom of the document to the viewport
        const bottomOffset = scrollHeight - clientHeight;

        if (scrollTop + clientHeight >= bottomOffset - offsetFromBottom) {
            fixedElement.style.position = 'unset';
        } else {
            fixedElement.style.position = 'fixed';
        }
    }

    window.addEventListener('scroll', checkScrollPosition);
    // Check initial state on page load
    checkScrollPosition();
});

