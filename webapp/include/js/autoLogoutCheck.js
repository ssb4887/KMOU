document.write('<script type="text/javascript" src="'+CheckForm.contextPath+'/include/js/jquery.countdown.js"><\/script>');
document.write('<script type="text/javascript" src="'+CheckForm.contextPath+'/include/js/jquery.cookie.js"><\/script>');

var varAutoLogout = {
	maxIntervalTime:10		// 세션유지시간 : 10분
	, checkIntevalTime:10	// 로그아웃 카운팅 시간 : 10분
	, milliTime:60000		// 시간 계산
	, check:false			// 로그아웃 시간체크
	, logoutUrl:false		// 로그아웃 경로
	, windowType:false		// 창속성
	, logoutState:false 	//로그아웃 상태
	, init:function(){
		// 초기화
		varAutoLogout.setTimeCheck(true);
		
	}, timeCheck:function(){
		// 로그아웃 시간체크
		var maxIntervalTime = $.cookie('rbis_auto_logout');
		if(maxIntervalTime == undefined) maxIntervalTime = 0;
		else maxIntervalTime = maxIntervalTime;
		var nowTime = new Date().getTime();
		
		// 로그아웃 카운팅 시간(10분), 로그아웃까지 남은 시간
		var varCheckTime = varAutoLogout.checkIntevalTime * varAutoLogout.milliTime;
		var varLeftTime = maxIntervalTime - nowTime;
		
		if(varLeftTime <= varCheckTime){
			// 로그아웃까지 남은 시간 <= 로그아웃 카운팅 시간(3분) : 로그아웃 시간 카운팅
			varAutoLogout.displayLoginoutLayer(1);
			varAutoLogout.countdown(varLeftTime);
		} else {
			// 로그아웃까지 남은 시간 > 로그아웃 카운팅 시간(3분) : 로그아웃 시간체크
			var varIntervalTimeout = varLeftTime - varCheckTime;		// 로그아웃 체크 시간
			varAutoLogout.check = setTimeout(varAutoLogout.timeCheck, varIntervalTimeout);
		}
	}, countdown:function(theLeftTime){
		// 로그아웃 시간 카운트
		
		// 카운트 만료시간
		var varLeftTime = new Date().getTime() + theLeftTime;
		 
		$('.fn_autoLogout_countdown').countdown(varLeftTime).on('update.countdown', function(e){
			
			// 카운팅
			var nowTime = new Date().getTime();
			var cookieTime = $.cookie('rbis_auto_logout');
			if(nowTime >= cookieTime || cookieTime == undefined) {
				// 현재 시간 >= 로그아웃 설정 시간 : 로그아웃
				if(!varAutoLogout.logoutState){
					fn_procActionOpReload(varAutoLogout.logoutUrl, varAutoLogout.windowType);
					varAutoLogout.logoutState = true;
				}
			} else {
				// 로그아웃 설정 시간 3분전 체크
				var varEndLeftTime = (cookieTime - nowTime)/* / varAutoLogout.milliTime*/;	// 로그아웃까지 남은 시간
				var varCheckTime = varAutoLogout.checkIntevalTime * varAutoLogout.milliTime;
				
				//console.log(varCheckTime);
				if(varEndLeftTime == 12000){
					$(".mainLayPop").show();
				}
				if(varEndLeftTime <= varCheckTime) {
					//  로그아웃까지 남은 시간 < 로그아웃 카운팅 시간(3분) : 카운팅 시간 display
					$(this).html(e.strftime('%M분 %S초'));
				} else {
					// 로그아웃까지 남은 시간 > 로그아웃 카운팅 시간(3분) : 로그아웃 시간체크
					$('.fn_autoLogout_countdown').countdown('stop');
					//var varCheckTime = varAutoLogout.checkIntevalTime * varAutoLogout.milliTime;	// 로그아웃 카운팅 시간(3분)
					var varIntervalTimeout = varEndLeftTime - varCheckTime;							// 로그아웃까지 남은 시간  - 로그아웃 카운팅 시간(3분)
					varAutoLogout.check = setTimeout(varAutoLogout.timeCheck, varIntervalTimeout);
					varAutoLogout.displayLoginoutLayer();
				}
			}
		}).on('finish.countdown', function(e){
			// 카운팅 종료
			var nowTime = new Date().getTime();
			var cookieTime = $.cookie('rbis_auto_logout');
			if(nowTime >= cookieTime || cookieTime == undefined) {
				// 현재 시간 >= 로그아웃 설정 시간 : 로그아웃
				fn_procActionOpReload(varAutoLogout.logoutUrl, varAutoLogout.windowType);
			} else {
				// 현재 시간 < 로그아웃 설정시간 : 다른 페이지 접속한 경우 - 로그아웃 설정 시간 3분전 체크
				var varEndLeftTime = (cookieTime - nowTime) / varAutoLogout.milliTime;
				if(varEndLeftTime <= varAutoLogout.checkIntevalTime) {
					// 로그아웃 설정 시간 <= 로그아웃 체크시간 : 로그아웃 시간 카운트
					varAutoLogout.countdown(varEndLeftTime);
				} else {
					// 로그아웃 설정 시간 > 로그아웃 체크시간 : setTimtout
					var varTimeout = (varEndLeftTime - varAutoLogout.checkIntevalTime) * varAutoLogout.milliTime;
					varAutoLogout.check = setTimeout(varAutoLogout.timeCheck, varTimeout);
					varAutoLogout.displayLoginoutLayer();
				}
			}
		});
	}, extension:function(){
		// 연장
		varAutoLogout.displayLoginoutLayer();
		$.get(location.href).done(function(){
			varAutoLogout.setTimeCheck();
		});
	}, setTimeCheck:function(theInit){
		// 로그아웃 시간체크 설정
		var varMaxIntervalTime = varAutoLogout.maxIntervalTime * varAutoLogout.milliTime;
		var varIntervalTimeout = (varAutoLogout.maxIntervalTime - varAutoLogout.checkIntevalTime) * varAutoLogout.milliTime;

		var maxIntervalTime = new Date();
		maxIntervalTime.setTime(maxIntervalTime.getTime() + varMaxIntervalTime);
		$.cookie('rbis_auto_logout', maxIntervalTime.getTime(), {expires:maxIntervalTime, path:'/'});
		
		if(!theInit) {
			$('.fn_autoLogout_countdown').countdown('stop');
			if(varAutoLogout.check) clearTimeout(varAutoLogout.check);
		}
		varAutoLogout.check = setTimeout(varAutoLogout.timeCheck, varIntervalTimeout);
	}, displayLoginoutLayer:function(theType){
		// 타이머 display
		/*if(theType == 1){
			$('.fn_autoLogout_layerBg').css('display', 'block');
			$('.fn_autoLogout_layer').css('display', 'block');
		}else{
			$('.fn_autoLogout_layerBg').css('display', 'none');
			$('.fn_autoLogout_layer').css('display', 'none');
		}*/
	}
}

$(function(){
	$('.fn_login_extension').click(function(){
		varAutoLogout.extension();
	});
	
	$('.fn_autoLogout_btnClose').click(function(){
		//$.removeCookie('rbis_auto_logout');
		$.cookie('rbis_auto_logout', '', {expires:new Date(), path:'/'});
		varAutoLogout.displayLoginoutLayer();
	});
});