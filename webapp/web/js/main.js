$(function () {
  //메인-학생정보 ==> 이름/학번/학년학기 조절
  var nameInfo = $(".summ_info > h3").width();
  $(window).resize(function(){ 
    
    if(375 < window.innerWidth < 575){
      $(".summ_info > div").css({ "width": "calc(100% - " + nameInfo+ "px)" });
    }else{
      $(".summ_info > div").css({ "width": "auto" });
    };

    if(window.innerWidth < 375){
      $(".stu_numb").css({"left": "" + nameInfo+ "px" });
    }
    
  }).resize();
  $(".summ_info > div").css({ "width": "auto" });
  
	$(".pp_tip .open_pp").on("click", function(){
	    $(this).siblings(".open_content").stop().fadeIn();
	});
	$(".pp_tip .clo_pp").on("click", function(){
	    $(this).parents(".open_content").stop().fadeOut();
	});

  //학생설계전공 추천 슬라이드
  $(".item_reco_prog .box_wrap").slick({
      slidesToShow:1, 
      slidesToScroll:1, 
      arrows:false,
      dots:true,
      autoplay:false, 
      autoplaySpeed:2000,
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

  $(".nav-recomm").on("click", function (){
    $('.box_wrap').slick('setPosition');
  });

});

//메인 추천방식 팝업
$(function () {
	$('.btn-example').click(function () {
		var $href = $(this).attr('href');
		layer_popup($href);
	});

	function layer_popup(el) {
		var $el = $(el);
		var isDim = $el.prev().hasClass('dimBg');

		if (isDim) {
			$('.dim-layer').fadeIn();
		} else {
			$el.fadeIn();
		}

		// Add scrollLock class to body
		$('body').addClass('scrollLock');

		//var $elHeight = $el.outerHeight();
		//var $elWidth = $el.outerWidth();
		//var docHeight = $(window).height();
		//var docWidth = $(window).width();

		if ($elHeight < docHeight || $elWidth < docWidth) {
			$el.css({
				marginTop: -$elHeight / 2,
				marginLeft: -$elWidth / 2
			});
		} else {
			$el.css({
				top: 0,
				left: 0
			});
		}

		$el.find('a.btn-layerClose').click(function () {
			isDim ? $('.dim-layer').fadeOut() : $el.fadeOut();
			// Remove scrollLock class from body
			$('body').removeClass('scrollLock');
			return false;
		});

		$('.dimBg').click(function () {
			$('.dim-layer').fadeOut();
			// Remove scrollLock class from body
			$('body').removeClass('scrollLock');
			return false;
		});
	}

	$(document).mouseup(function (e) {
		if ($(".dimBg").has(e.target).length === 0) {
			$(".dim-layer").hide();
			// Remove scrollLock class from body
			$('body').removeClass('scrollLock');
		}
	});
});

/*
// 메인 추천방식 팝업
$(function () {
	$('.btn-example').click(function(){
	    var $href = $(this).attr('href');
	    layer_popup($href);
	});
	
	function layer_popup(el){
	
	    var $el = $(el);   
	    var isDim = $el.prev().hasClass('dimBg'); 
	    
	    isDim ? $('.dim-layer').fadeIn() : $el.fadeIn();

	    if ($elHeight < docHeight || $elWidth < docWidth) {
	        $el.css({
	            marginTop: -$elHeight /2,
	            marginLeft: -$elWidth/2
	        })
	    } else {
	        $el.css({top: 0, left: 0});
	    }
	    
	    $el.find('a.btn-layerClose').click(function(){
	        isDim ? $('.dim-layer').fadeOut() : $el.fadeOut(); 
	        return false;
	    });
	
	    $('.layer .dimBg').click(function(){
	        $('.dim-layer').fadeOut();
	        return false;
	    });
	    	
	}

});

 외부영역 클릭시 팝업 닫기 
$(document).mouseup(function (e){
	if($(".dimBg").has(e.target).length === 0){
		$(".dim-layer").hide();
	}
});
*/



