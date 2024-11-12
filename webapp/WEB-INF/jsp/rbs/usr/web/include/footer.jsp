<c:if test="${!empty loginVO.memberId && (crtMenu.menu_idx != '1' && crtMenu.menu_idx != '39' && crtMenu.menu_idx != '42' && crtMenu.menu_idx != '40' && crtMenu.menu_idx != '41' && crtMenu.menu_idx != '67' && crtMenu.menu_idx != '43') }">
<jsp:include page = "./satisfaction.jsp" flush = "false"/>
</c:if>
<!-- footer -->
<footer>
    <button class="goTop_btn d-flex justify-content-center align-items-center d-lg-none"><img src="../images/arr_top_footer.png" alt="위로가기버튼" class="d-block"/></button>
	<ul class="footer_1 d-flex flex-row justify-content-center align-items-center gap-4 mb-1">
		<li><a href="https://www.kmou.ac.kr/" title="[새창]대표홈페이지" target="_blank">대표홈페이지</a></li>

       	<li><a href="javascript:" onclick="goOceanCtsFooter('https://cts.kmou.ac.kr/')" title="[새창]오션CTS" target="_blank">오션CTS</a></li>
       	<li><a href="https://sugang.kmou.ac.kr/" title="[새창]수강신청시스템" target="_blank">수강신청시스템</a></li>
       	<li><a href="https://cyberweb.kmou.ac.kr/" title="[새창]KMOU_LMS" target="_blank">KMOU_LMS</a></li>
       	<li><a href="https://www.kmou.ac.kr/onestop/main.do" title="[새창]종합정보시스템" target="_blank">종합정보시스템</a></li>
	</ul>
	<div class="footer_2">
		<address class="d-block lh-sm">${siteInfo.site_addr }<span>TEL : ${siteInfo.site_phone } / FAX : ${siteInfo.site_fax }</span><br>
		${siteInfo.site_copyright }</address>
	</div>
   </footer>
  	<script type="text/javascript">
	function goOceanCtsFooter(gourl){
		var token = '';
		var url = 'https://cts.kmou.ac.kr/ko/process/coursemos/loginToken'
		
		$.ajax({
			url: '/web/nonSbjt/getToken.do?mId=34',
			contentType:'application/json',	
			type: 'POST',
			success: function(data){			
				token = data.token;
				
				var queryString = '?token='+token+'&gourl='+gourl;
				url += queryString;
				window.open(url,'_blank');
			},error:function() {
				alert('비교과 접속에 실패하였습니다. 관리자에게 문의해주세요.');
			}
		});
	}
	</script>
<!-- //footer -->
<%@ include file="../../../include/login_check.jsp"%>
