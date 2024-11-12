$(function () { 
    /*즐겨찾기 checkbox event*/
    $('.favorite-checkbox input').on('click', function(){
        if ($(this).is(":checked")) {
            $(this).closest('.subject-box').addClass('checked')
        } else {
             $(this).closest('.subject-box').removeClass('checked')
        }
    })
    
    /*상세검색 온, 오프*/
    $('#btn-detail-search-prof').on('click', function(){
	    if($(this).hasClass('on')){
	        $(this).removeClass('on')
	        $('#detail-search-wrap-prof').removeClass('on')
	    } else {
	        $(this).addClass('on')
	        $('#detail-search-wrap-prof').addClass('on')
	    }            
	})
	$('#btn-detail-search-sbjt').on('click', function(){
		if($(this).hasClass('on')){
			$(this).removeClass('on')
			$('#detail-search-wrap-sbjt').removeClass('on')
		} else {
			$(this).addClass('on')
			$('#detail-search-wrap-sbjt').addClass('on')
		}            
	})

	/*만족도 레이어 온, 오프*/
	$('.satisfied-layer .inner .btn-close').on('click', function(){
	    $(this).closest('.inner').css({'display':'none'});
	    $(this).closest('.satisfied-layer').addClass('close');
	    $(this).closest('.inner').next().css({'display':'flex'});
	})
	$('.satisfied-layer .inner2 .btn-open').on('click', function(){
	    $(this).closest('.inner2').css({'display':'none'});
	    $(this).closest('.satisfied-layer').removeClass('close');
	    $(this).closest('.inner2').prev().css({'display':'flex'});
	})
	
	/*만족도 레이어 일주일간 안보이기*/
	$('.satisfied-layer .form-checkbox2 input').on('click', function(){
	    $(this).closest('.satisfied-layer').css({'display':'none'});
	})

})