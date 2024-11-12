<%-- <%@ include file="commonTop.jsp" %> --%>


<script type="text/javascript">
	$(function(){
		$('header').remove();
		$('.sub_bottom').remove();
	})
	
function privacyPolicyAgree(){
		$.ajax({
			url: '/web/main/privacyPolicyAgree.do?mId=1',
		    type: "POST",
			contentType:'application/json',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			success:function(data){
				if(data.RESULT != "DONE"){
					alert("정보제공동의에 실패하였습니다. 관리자에게 문의해주세요.")
					return false;
				}else{
					location.href="/web/main/main.do?mId=1"
				}

				
				
			}
			
		});
	}
</script>
