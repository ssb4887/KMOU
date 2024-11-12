<%@ include file="../../include/commonTop.jsp"%>
 <script type="text/javascript">
      $(document).ready(function() {
    	  
    	  
    	  
    	  
    	  
    	  
    	  
    	  
    	  
    	  
    	  
    	  
    	  
    	  
          // 교과과정 체크박스
          <c:forEach var="cfDt" items="${searchCondition.majorCorsFg}">
          	var val = '${cfDt}';
			$('input[name=optMajorCorsFg]:input[value=' + val + ']').prop('checked', true);
          </c:forEach>

       		// 소속대학 선택 시 단과대학 목록 선택값 변경
	    	// 학과 초가화 필요
			$('#dept_campus').change( function() {
				
				$('#dept_subject option').remove();
		        $('#dept_subject').append("<option value=''>전체</option>");
		        $('#dept_subject option:first').attr("selected","selected");
				if(this.value != "") {
					var paramObj = {deptCampus : this.value}
					$.ajax({
				    url: '${URL_SEARCHUNIVLIST}',
				    type: 'POST',
				    crossDomain: true,
				    data: {
				    	deptCampus : paramObj.deptCampus
			  		},
				    beforeSend:function(request){
				    	request.setRequestHeader('Ajax','true');
				    }
					}).done(function (data, textStatus, xhr) {
					$('#dept_univ option').remove();
				    $('#dept_univ').append("<option value=''>전체</option>");
				    for(var idx in data.haksaCode){
				        $('#dept_univ').append("<option value='" + data.haksaCode[idx].DEPT_CD + "'>" + data.haksaCode[idx].DEPT_NM + "</option>");
				    }
				    $('#dept_univ option:first').attr("selected","selected");
					}).fail(function(data, textStatus, errorThrown){
					      /*pass*/
					      console.log('error 발생 : ' + errorThrown);
					});
				}
				else {
					$('#dept_univ option').remove();
			        $('#dept_univ').append("<option value=''>전체</option>");
			        $('#dept_univ option:first').attr("selected","selected");
				}
			});
          		
	    	// 소속대학 선택 시 단과대학 목록 선택값 변경
	    	// 단과대 및 학과 초가화 필요
			$('#dept_univ').change( function() {
				
				if(this.value != "") {
					var paramObj = {deptUniv : this.value}
					$.ajax({
				    url: '${URL_SEARCHSUBJECTLIST}',
				    type: 'POST',
				    crossDomain: true,
				    data: {
				    	deptUniv : paramObj.deptUniv
			  		},
				    beforeSend:function(request){
				    	request.setRequestHeader('Ajax','true');
				    }
					}).done(function (data, textStatus, xhr) {
					$('#dept_subject option').remove();
				    $('#dept_subject').append("<option value=''>전체</option>");
				    for(var idx in data.haksaCode){
				        $('#dept_subject').append("<option value='" + data.haksaCode[idx].DEPT_CD + "'>" + data.haksaCode[idx].DEPT_NM + "</option>");
				    }
				    $('#dept_subject option:first').attr("selected","selected");
					}).fail(function(data, textStatus, errorThrown){
					      /*pass*/
					      console.log('error 발생 : ' + errorThrown);
					});
				}
				else {
					$('#dept_subject option').remove();
			        $('#dept_subject').append("<option value=''>전체</option>");
			        $('#dept_subject option:first').attr("selected","selected");
				}
			});
        
		//초기화 클릭 시
		$('#btnReset').click( function() {
			$("#profSearch").reset();
			$("#searchBoxHead").val("${crtMenu.module_id}").attr("selected", "selected");
		});
				
				
		/*	
		 $("input[name='star']").click(function(){
				//클릭한 교과목 정보
				//즐겨찾기 바로 다음에인풋박스 넣엇을 경우
				//만약 부모요소 찾아서 그 부모요소의 안에서 값을 찾고싶으면
				var parentId = $(this).parent('td').attr('id');
				//부모의 부모요소는 parents
				//$("#"+parentId).find('input').eq(0).val()
				
				var param = new Object();
				param.docCd = $(this).nextAll('input').eq(0).val();
				
				if($(this).val() == "Y"){
					$(this).prop('checked',false);	//관심교과목 해지 시
					$(this).val("N");
					param.booMarkIdx = "1"
				}else{
					$(this).prop('checked',true);	//관심교과목 등록 시
					$(this).val("Y");
					param.booMarkIdx = "0"
				}
				
				bMarkinsert(param)
			});
		*/
		
		//찜목록추가
        $(".favorites").click(function(){
        	var MajorCd = $(this).attr('data-idx');
        	var elem = $(this);
        	
					if($(this).hasClass("fill")) {
						var paramObj = {intrstMjCd : MajorCd}
						$.ajax({
					    url: '/search/delFavoriteMajor.json',
					    type: 'POST',
					    method: 'POST',
					    dataType:'json',
					    contentType: 'application/json; charset=utf-8',
					    crossDomain: true,
					    data: JSON.stringify(paramObj)
						}).done(function (data, textStatus, xhr) {
							elem.removeClass("fill");
						}).fail(function(data, textStatus, errorThrown){
							  /*pass*/
						});
					} 
					else {
						var paramObj = {intrstMjCd : MajorCd}
						$.ajax({
					    url: '/search/addFavoriteMajor.json',
					    type: 'POST',
					    method: 'POST',
					    dataType:'json',
					    contentType: 'application/json; charset=utf-8',
					    crossDomain: true,
					    data: JSON.stringify(paramObj)
						}).done(function (data, textStatus, xhr) {
							elem.addClass("fill");
						}).fail(function(data, textStatus, errorThrown){
						    /*pass*/
						});
					}
        });
      });
      
      function setFilterData() {
        $('div.detail_search_area select[id^="optMajor"]').each(function(index, item) {
            var sOptId = $(item).attr("name").replace(/opt/,"").replace(/\b\w/g,function(f){return f.toLowerCase();});
            var sOptVal = $(item).find("option:selected").val();
            if(sOptVal != "") {
              $('#isrchForm').append($('<input/>', {type: 'hidden', name: sOptId, value: sOptVal}));
            }
          });
          
        $('div.detail_search_area input[id^="optMajor"]').each(function(index, item) {
          var sOptId = $(item).attr("name").replace(/opt/,"").replace(/\b\w/g,function(f){return f.toLowerCase();});
          var sOptVal = "";
          if($(item).attr("type") == "checkbox") {
            if($(item).is(":checked")) {
              sOptVal = $(item).val();
            }
          }
          else if($(item).attr("type") == "text") {
            sOptVal = $(item).val();
          }
          if(sOptVal != "") {
            $('#isrchForm').append($('<input/>', {type: 'hidden', name: sOptId, value: sOptVal}));
          }
        });
        
        /* if($('.results_sort').find('span.on').length) {
        	$('#isrchForm').append($('<input/>', {type: 'hidden', name: "majorSort", value: $('.results_sort span.on').data("sort")}));
        } */
        if($('.results_sort select option:selected').length && !isEmpty($('.results_sort select option:selected').val())) {
        	$('#isrchForm').append($('<input/>', {type: 'hidden', name: "majorSort", value: $('.results_sort select option:selected').val()}));
        }
      }
      
      
     
      
      
      
      /* 관심 교과목 저장 */
      function bMarkinsert(param){

      	$.ajax({
      		type:'POST', 
      		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
      		url:'${URL_BOOKMARKINPUT}',
      		data: param,
      		success:function(result){
      			/*pass*/
      		}, error:function() {
      			alert("오류가 발생했습니다 관리자에 문의해주세요.");
      		}
      	});
      }
      
      
	function selectProf(empNo, korNm){
			
		$("#professorNo", opener.document).val(empNo);
		$("#professorNm", opener.document).val(korNm);
      
		self.close();
	}
    </script>