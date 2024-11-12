<script type="text/javascript">
$(function(){
	changeCaptcha();
	  
  $('#reLoad').click(function(){ changeCaptcha(); }); //'새로고침'버튼의 Click 이벤트 발생시 'changeCaptcha()'호출
  $('#soundOn').click(function(){ audioCaptchaPlayer(); }); //'음성듣기'버튼의 Click 이벤트 발생시 'audioCaptcha()'호출
});

function changeCaptcha(){
	$('#ahCodeImg').attr('src', '<c:out value="${contextPath}/${crtSiteId}/captcha/captcha.do?rand="/>' + Math.random());
}

function audioCaptchaPlayer(){
	var uAgent = navigator.userAgent;
	var soundUrl = '<c:out value="${contextPath}/${crtSiteId}/captcha/audioCaptcha.do?rand="/>' + Math.random();
	if (uAgent.indexOf('Trident') > -1 || uAgent.indexOf('MSIE') > -1) {
		//IE일 경우 호출
		$('#audioAhCodeImg').html(' <bgsound src="' + soundUrl + '">');
	} 
	else if (!!document.createElement('audio').canPlayType) {
		//Chrome일 경우 호출
		try { new Audio(soundUrl).play(); } catch(e) { }
	} 
	else window.open(soundUrl, '', 'width=1,height=1');
}
</script>