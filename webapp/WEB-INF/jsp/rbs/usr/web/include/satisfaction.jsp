<script>
$(function(){
	//만족도 조사 제출
	$("#statisfSbmBtn").on("click", function(){
			
			if($('input[name="point"]:checked').val() == null){
				alert("만족도를 체크해주세요.");
				return false;
			}
			if($("#onLineRat").val() == null || $("#onLineRat").val() == ""){
				alert("내용을 입력해주세요.");
				return false;
			}
			
			$.ajax({
				url: '/web/search/insertPoint.do?mId=31',
				contentType:'application/json',
				type: 'POST',
				data: JSON.stringify({ 
					POINT_TYPE :  $("#satisfRat option:selected").val(),
					POINT : $('input[name="point"]:checked').val(),
					CONTENTS : $("#onLineRat").val()
				}),
				success: function(data){
					alert("소중한 의견 감사드립니다.");
					
	                $('input[name="point"]:checked').prop('checked', false); // 포인트 초기화
	                $("#onLineRat").val(''); // 한 줄 평가 초기화		
	                $("#satisfRat").prop('selectedIndex', 0); // 셀렉트박스 초기화
				}
			});
	});
	
    // 한 줄 평가에서 Enter 키를 눌렀을 때 제출 버튼 클릭
    $("#onLineRat").on("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); // Enter 키의 기본 동작을 막음
            $("#statisfSbmBtn").click(); // 제출 버튼 클릭
        }
    });
})
</script>

<!-- 만족도 조사 -->
<section class="sub_bottom d-none d-md-block ">
	<div class="inner">
		<div class="bottom_text position-relative csm_title">
			<p class="text-end">만족도 조사 <span>접기</span></p>
			<button type="button" class="rtt border-0 d-block position-absolute top-50 translate-middle-y end-0 h-100 px-4">
				<!--img src="${contextPath}/${crtSiteId}/assets/images/arr_bottom_gray.png" alt="화살표" -->
				<img src="../images/arr_bottom_gray.png" alt="화살표" />
			</button>
		</div>
		<div class="botton_box flex-wrap align-items-start align-items-xl-center csm_ctx">
			<div class="col-12 col-lg-6 col-xl-5">
				<h5 class="fw-bold h4 mb-1">서비스에 만족하셨습니까?</h5>
				<p class="d-none d-lg-block">보다 나은 서비스 제공을 위해, 여러분의 소중한 의견을 남겨주세요!</p>
			</div>
			<div class="col-12 col-lg-6 col-xl-7 d-flex flex-column flex-xl-row align-items-center gap-2">
				<div class="rating_box col-12 col-xl-5">
					<div class="input-group justify-content-end justify-content-xl-start gap-3">
						<label class="blind" for="satisfRat">만족도평가</label> 
						<select class="form-select" id="satisfRat">
							<option selected value="ACCURACY">검색정확도</option>
							<option value="QUALITY">검색정보의 양질</option>
							<option value="CONVENIENCE">사용의 편의성</option>
							<option value="ETC">기타</option>
						</select>
						<section class="rating d-flex flex-row gap-2 align-items-center">
							<input class="" type="radio" value="5" id="firStart" name="point"> <label class="star" for="firStart"></label>
							<input class="" type="radio" value="4" id="secStart" name="point"> <label class="star" for="secStart"></label>
							<input class="" type="radio" value="3" id="thrStart" name="point"> <label class="star" for="thrStart"></label>
							<input class="" type="radio" value="2" id="fouStart" name="point"> <label class="star" for="fouStart"></label>
							<input class="" type="radio" value="1" id="fifStart" name="point"> <label class="star" for="fifStart"></label>
						</section>
					</div>
				</div>
				<div class="submit_box col-12 col-xl-7">
					<div class="input-group d-flex flex-row flex-nowrap align-items-center gap-2">
						<label for="onLineRat">한 줄 평가</label>
						<textarea id="onLineRat" rows="1" placeholder="여러분의 소중한 의견을 보내주세요" name="contents" maxlength="400"></textarea>
						<button type="button" id="statisfSbmBtn" class="pk_btn">작성</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</section>