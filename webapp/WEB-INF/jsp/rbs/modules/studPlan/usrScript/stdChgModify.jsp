<script>
let bookmarkList;
let duplSbjtArr = [];
let beforeSbjtData = [];
let majorArray = ['UE010021', 'UE010022', 'UE010024', 'UE010031'];
let genArray = ['UE010011', 'UE010012']
	$(function(){
		
			//반려사유 세팅
			switch("${INFO.STATUS}"){
				case "33" : $("#rjtResn").val("${INFO.RJT_RESN}"); break;
				case "43" : $("#rjtResn").val("${INFO.EDU_CENTER_JUDG_OPIN}"); break;
				case "53" : $("#rjtResn").val("${INFO.DEPART_JUDG_OPIN}"); break;
				case "63" : $("#rjtResn").val("${INFO.AFFAIR_COMMITTEE_JUDG_OPIN}"); break;
			}			

 	       // 변경내용 이벤트 핸들러	       
	       $('#sbjtListPc').on('change', '.chgFg', function () {
	    	    var selectedValue = $(this).val();
	    	    var row = $(this).closest('tr');
	    	    var keyDocId = row.find('input[name="keyDocId"]').val();

	    	    // hidden input에서 해당 keyDocId에 맞는 값을 찾고 업데이트
	    	    var hiddenDiv = $('.SBJT_LIST_' + keyDocId);
	    	    hiddenDiv.find('input[name="CHG_FG"]').val(selectedValue);

	    	    // hidden input 값을 다시 배열로 변환
	    	    var jsonArray = [];
	    	    getCommonJsonArray(jsonArray)
	    	    
	    	    // sbjtList에서 해당 keyDocId에 맞는 sbjt 객체 찾기
	    	    var sbjt = jsonArray.find(function (item) {
	    	        return item.CHG_DOC_ID === keyDocId;
	    	    });
	    	    sbjt["CHG_FG"] = selectedValue;  // 선택된 값에 따라 sbjt 객체 업데이트

	    	    // 변경된 행만 업데이트
	    	    updateRow(row, sbjt);
	    	    chgSbjtDisplay();  // 교육과정표 다시 그리기
	    	});	    	    	       	    
	    
	    // 이수구분 변경시의 변경후 교육과정의 구분 이벤트 핸들러
	       $('#sbjtListPc').on('change', '.selectChgSbjtFg', function () {
	           var row = $(this).closest('tr');
	           var keyDocId = row.find('input[name="keyDocId"]').val();
	           var comDivCode = $(this).val();
	           var comDivCodeNm = $(this).find('option:selected').text();

	           // hidden input에서 해당 keyDocId에 맞는 값을 찾고 업데이트
	           var hiddenDiv = $('.SBJT_LIST_' + keyDocId);
	           hiddenDiv.find('input[name="CHG_COMDIV_CODE"]').val(comDivCode);
	           hiddenDiv.find('input[name="CHG_COMDIV_CODE_NM"]').val(comDivCodeNm);

	           // hidden input 값을 다시 배열로 변환
	    	   var jsonArray = [];
	    	   getCommonJsonArray(jsonArray)

	           // sbjtList에서 해당 keyDocId에 맞는 sbjt 객체 찾기
	           var sbjt = jsonArray.find(function (item) {
	               return item.CHG_DOC_ID === keyDocId;
	           });

	           sbjt.CHG_COMDIV_CODE = comDivCode;
	           sbjt.CHG_COMDIV_CODE_NM = comDivCodeNm;
	       });	    
	    
	    // 변경사유 입력할때마다의 이벤트 핸들러
	       $('#sbjtListPc').on('input', '#CHG_RESN', function () {
	           var row = $(this).closest('tr');
	           var keyDocId = row.find('input[name="keyDocId"]').val();
	           var chgResnValue = $(this).val();
	           
	           // hidden input 값을 다시 배열로 변환
	    	   var jsonArray = [];
	    	   getCommonJsonArray(jsonArray)

	           var sbjt = jsonArray.find(function (item) {
	               return item.CHG_DOC_ID === keyDocId;
	           });

	           sbjt.CHG_RESN = chgResnValue;

	           // JSP의 hidden input 값 업데이트
	           var hiddenDiv = $('.SBJT_LIST_' + keyDocId);
	           hiddenDiv.find('input[name="CHG_RESN"]').val(chgResnValue);

	       });
	    
		
		//변경 교육과정표 보기 버튼
		$("#afterSbjtShow").click(function(){
			chgSbjtDisplay();
			
			$("#afterSbjtShow").hide();
			$("#beforeSbjtShow").show();
		})
		
		//기존 교육과정표 보기 버튼
		$("#beforeSbjtShow").click(function(){
			beforeSbjtDisplay(beforeSbjtData);
			
			$("#beforeSbjtShow").hide();
			$("#afterSbjtShow").show();
		})
		
		//설계범위에 따른 [타 대학 기관 산업체] 항목 display 구분 
		if("${INFO.SBJT_DGN_RNG_FG}" == 'IME'){
			$("#otherColg").removeClass("d-none");
		}else{
			$("#otherColg").addClass("d-none");
		}		

		
		$("[id^='chgResn-'], [id^='chgResnM-']").change(function() {
			var index = $(this).attr("id").split("-")[1];
			var inputValue = $(this).val();
			
			if($(this).attr("id").includes("M")) $("#chgResn-" + index).val(inputValue);
			else $("#chgResnM-" + index).val(inputValue);
		});				
		
		
		// 저장된 교과목 세팅
		$.ajax({
			url: '/web/studPlan/getMyChgSbjtList.do?mId=36',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'POST',
			data: JSON.stringify({sdmCd : "${INFO.SDM_CD}", revsnNo : "${INFO.REVSN_NO}", beforeRevsnNo : "${INFO.REVSN_NO}" -1}),
			success:function(data){
				//변경전 교과목 전역변수 선언
				beforeSbjtData = data.SBJTLIST;
				
				setSavedChgSbjtToHidden(data.CHG_SBJTLIST);	//hidden값 세팅
				chgSbjtDisplay();			//교육과정표		
				chgSbjtDiffDisplay();		//교육과정 신구대비표
				setSavedChgSbjtToModal();	//모달 오른쪽 추가부분에 세팅


			}
		});		
		
		//찜 목록 가져오기(promise)		
		getBookmarkList()
		.then(data => {
			bookmarkList = data;
		});				
		
		// 전공교과목 검색 Enter
		$("#keyWord").on('keyup', function(key){
			if(key.keyCode==13) {
				$("#keyWord").val($(this).val());
				getSbjtList();			
			}
		});

		// 나의찜목록 검색 Enter
		$("#keyWordMy").on('keyup', function(key){
			if(key.keyCode==13) {
				$("#keyWordMy").val($(this).val());
				getMySbjtList();			
			}
		});
		
		// 유사 설계전공 보기 button
		$("#getSimilityMj").on('click', function(){
			
			if(sbjtArray.length > 0){
				$.ajax({
					url: '/web/studPlan/getSimilityMj.json?mId=45&site=pknuai',
				    type: "POST",
					contentType:'application/json',
				    data : JSON.stringify({
				    	sbjtArray : sbjtArray
				    }),
				    dataType : 'json',
					beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
					success:function(data){
						
						var info = data.items;
						
						if(info.RATE != null){
							$("#similitySdmNm").text(info.SDM_KOR_NM);
							$("#similityRate").text(info.RATE);
						}else{
							$("#similitySdmNm").text("-");
							$("#similityRate").text("0");
							if(info.O_RTN_MSG != null){								
								alert(info.O_RTN_MSG);
							}else{
								alert("등록된 교과목과 유사한 학생설계전공이 없습니다.")
							}							
						}
					}
					
				});
			}else{
				alert("등록된 교과목이 없습니다.");
			}
		});
		
		
		// 과목등록하기 Modal button
		$("#btnModal").on('click', function(){
			if($(".chk-sbjt-range:checked").val() != "IMG"){
			    $(".general-check-form").hide();
			    $("#chkMaj_all, #chkMaj_Requi, #chkMaj_Selc, #chkMaj_Basi, #chkMaj_Teach").prop("checked", true);
			    $("#chkMaj_allMy, #chkMaj_RequiMy, #chkMaj_SelcMy, #chkMaj_BasiMy, #chkMaj_TeachMy").prop("checked", true);
			}else{
			    $(".general-check-form").show();
			    $("#chkMaj_all, #chkMaj_Requi, #chkMaj_Selc, #chkMaj_Basi, #chkGen_Requi, #chkGen_Selc, #chkMaj_Teach").prop("checked", true);
			    $("#chkMaj_allMy, #chkMaj_RequiMy, #chkMaj_SelcMy, #chkMaj_BasiMy, #chkGen_RequiMy, #chkGen_SelcMy, #chkMaj_TeachMy").prop("checked", true);
			};		
			
			// 대학 목록 불러오기
			$.ajax({
				url: '/web/studPlan/getCollList.do?mId=36',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'GET', 
				data: { 
					sbjtDgnRngFg : $("#checkedSbjtDgnRngFgNm").val()
				},
				success:function(data){
					
					// 대학
					if($("#selectColg").children().length == 0){
						var rs = data.COLLEGE_LIST;
						var html = '<option value="">대학</option>';
						
						for(var i=0; i < rs.length; i++){
							html += '<option value="'+rs[i].DEPT_CD+'">'+rs[i].DEPT_NM+'</option>'
						}
						$('#selectColg').html(html);
						$("#selectColgMy").html(html);
					}
					
					$("#enrollSubj").modal('show');
				}
			});
			$(".chk-before").prop("checked", false);
		});
		
		// 학부(과)목록 불러오기(대학 선택시) - 전공교과목
		$("#selectColg").on('change', function(){
			$('#selectMj option:not(:first)').remove();
			$.ajax({
				url : '/web/studPlan/getDeptList.do?mId=36',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'GET', 
				data: { 
					univ : $(this).val()
				},
				success:function(data){
					
					var items = data.DEPART_LIST;
					var html = '<option value="">학부(과)</option>';
					
					for(var i=0; i < items.length; i++){
						html += '<option value="'+items[i].DEPT_CD+'">'+items[i].DEPT_NM+'</option>'
					}
					
					$('#selectDept').html(html);
				}
			});
		});
		
		// 전공목록 불러오기(학부 선택시) - 전공교과목
		$("#selectDept").on('change', function(){
			
	        var selectedDept = $("#selectDept option:selected").text();
	        var lastTwoChars = selectedDept.slice(-2);
	        
	        if (lastTwoChars === '학과' || lastTwoChars === '전공') {
	        	$('#selectMj').hide();
	        } else {
	        	$('#selectMj').show();
				$.ajax({
					url : '/web/studPlan/getMajorList.do?mId=36',
					beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
					contentType:'application/json',
					type:'GET', 
					data: { 
						depart : $(this).val()
					},
					success:function(data){
						
						var items = data.MAJOR_LIST;
						var html = '<option value="">전공</option>';
						
						for(var i=0; i < items.length; i++){
							html += '<option value="'+items[i].DEPT_CD+'">'+items[i].DEPT_NM+'</option>'
						}
						
						$('#selectMj').html(html);
					}
				});
	        }
		});
		
		// 학부(과)목록 불러오기(대학 선택시) - 나의찜목록
		$("#selectColgMy").on('change', function(){
			$('#selectMj option:not(:first)').remove();
			$.ajax({
				url : '/web/studPlan/getDeptList.do?mId=36',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'GET', 
				data: { 
					univ : $(this).val()
				},
				success:function(data){
					
					var items = data.DEPART_LIST;
					var html = '<option value="">학부(과)</option>';
					
					for(var i=0; i < items.length; i++){
						html += '<option value="'+items[i].DEPT_CD+'">'+items[i].DEPT_NM+'</option>'
					}
					
					$('#selectDeptMy').html(html);
				}
			});
		});
		
		// 전공목록 불러오기(학부 선택시) - 나의찜목록
		$("#selectDeptMy").on('change', function(){
	        var selectedDept = $("#selectDeptMy option:selected").text();
	        var lastTwoChars = selectedDept.slice(-2);
	        
	        if (lastTwoChars === '학과' || lastTwoChars === '전공') {
	        	$('#selectMjMy').hide();
	        } else {
	        	$('#selectMjMy').show();
				$.ajax({
					url : '/web/studPlan/getMajorList.do?mId=36',
					beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
					contentType:'application/json',
					type:'GET', 
					data: { 
						depart : $(this).val()
					},
					success:function(data){
						
						var items = data.MAJOR_LIST;
						var html = '<option value="">전공</option>';
						
						for(var i=0; i < items.length; i++){
							html += '<option value="'+items[i].DEPT_CD+'">'+items[i].DEPT_NM+'</option>'
						}
						
						$('#selectMjMy').html(html);
					}
				});
	        }
		});
		
		// 타 대학/기관/산업체 교과목 등록 button
		$("#otherColg").on('click', function(){
			$("#tdSbjt").html('타 대학/기관/산업체');
			
			$("#enrollSubj").modal("hide");
			$("#openExternal").modal("show");
		});
		
		// 타 대학/기관/산업체 교과목 등록 modal close button
		$("#closeExternal").on('click', function(){
			$("#enrollSubj").modal("show");
		});

		// 전공교과목 tab
		$("#major-tab").on('click', function(){
			$("#appndSbjt").empty();
		});		

		// 나의찜목록 tab
		$("#myLove-tab").on('click', function(){
			getMySbjtList();
		});

		// 선택 일괄담기 button
		$("#btnAddSbjt").on('click', function(){
			sbjtContain();
		});
		
		// 과목등록하기 저장하기 button
		$("#btnSbjtSave").on("click", function(){
			sbjtDesignScope();
		});
		
		// 전공교과목 검색 button
		$("#searchSbjt").on('click', function(){
			getSbjtList();
		});

		// 나의찜목록 검색 button
		$("#searchMySbjt").on('click', function(){
			getMySbjtList();
		});

		// 전체선택/선택 일괄해제 button
		$(".btn-fg").on('click', function(){
			if($(this).val() == 'beforeChk'){
				$(".chk-before").prop("checked", true);
			}else if($(this).val() == 'beforeClear'){
				$(".chk-before").prop("checked", false);
			}else if($(this).val() == 'afterChk'){
				$(".chk-after").prop("checked", true);
			}else{
				$(".chk-after").prop("checked", false);
			}
		});
		
		// 이수구분 '전체' 클릭 시
		$("#chkMaj_all").change(function() {
			if($(".chk-sbjt-range:checked").val() != "IMG"){
			  $("#chkMaj_Requi, #chkMaj_Selc, #chkMaj_Basi, #chkMaj_Teach").prop("checked", $(this).prop("checked"));
			}else{
			  $("#chkMaj_Requi, #chkMaj_Selc, #chkMaj_Basi, #chkGen_Requi, #chkGen_Selc, #chkMaj_Teach").prop("checked", $(this).prop("checked"));
			};    
		});
		
		// 나의찜목록
		$("#chkMaj_allMy").change(function() {
			if($(".chk-sbjt-range:checked").val() != "IMG"){
			  $("#chkMaj_RequiMy, #chkMaj_SelcMy, #chkMaj_BasiMy, #chkMaj_TeachMy").prop("checked", $(this).prop("checked"));
			}else{
			  $("#chkMaj_RequiMy, #chkMaj_SelcMy, #chkMaj_BasiMy, #chkGen_RequiMy, #chkGen_SelcMy, #chkMaj_TeachMy").prop("checked", $(this).prop("checked"));
			};  
		});

		// 이수구분 종류별로 다 선택 시 '전체'도 클릭 처리
		var checkReqLength = 0;
		
		$(".chk-sbjt-search").change(function() {            
			if($(".chk-sbjt-range:checked").val() != "IMG"){
			  var checkReqLength = 4; 
			}else{
			  var checkReqLength = 6; 
			};  
			
			if($(".chk-sbjt-search:checked").length == checkReqLength){
			  $("#chkMaj_all").prop("checked", true);
			} else {
			  $("#chkMaj_all").prop("checked", false);
			}
		});	
		
		// 나의찜목록
		$(".chk-sbjt-searchMy").change(function() {      
			if($(".chk-sbjt-range:checked").val() != "IMG"){
			  var checkReqLength = 4; 
			}else{
			  var checkReqLength = 6; 
			};  
			
			if($(".chk-sbjt-searchMy:checked").length == checkReqLength){
			  $("#chkMaj_allMy").prop("checked", true);
			} else {
			  $("#chkMaj_allMy").prop("checked", false);
			}
		});	

		// 교과목 설계범위 checkbox
		$(".chk-sbjt-range").on('click', function(){
			$(".chk-sbjt-range").prop("checked", false);
			
			if($(this).val() == 'IMC'){
				$("#internalMajorCourse").prop("checked", true);
				$("#otherColg").addClass("d-none");
			}else if($(this).val() == 'IMG'){
				$("#internalMajorAndGeneral").prop("checked", true);
				$("#otherColg").addClass("d-none");
			}else{
				$("#internalMajorAndExternal").prop("checked", true);
				$("#otherColg").removeClass("d-none");
			}
		});		
		
	});	// end $(function)
	
	
	// 나의 찜목록
	function getMySbjtList(){
		
		//찜 다시 가져오기
		getBookmarkList()
		.then(data => {
			bookmarkList = data;
		});		
		if(bookmarkList != undefined){	
			//대학/전공 선택값(마지막 선택값만 API로 넘긴다)
			var selectedBox = ($("#selectMjMy").val() != "" ? $("#selectMjMy").val() : ($("#selectDeptMy").val() != "" ? $("#selectDeptMy").val() : ($("#selectColgMy").val() != "" ? $("#selectColgMy").val() : "")));	
			//이수구분 선택값(아무것도 선택하지 않을 시엔 배열에 전체 값을 강제로 넣는다.)
			var chkSbFg = [];
	
			$(".chk-sbjt-search:checked").each(function () {
				chkSbFg.push($(this).val());
			});	
			//로딩
			showLoading();
			//API call
			$.ajax({
				url: '/web/studPlan/getMyBookmarkSbjtList.do?mId=36',
				contentType:'application/json',
			    data: JSON.stringify({		    	
			    	"chkSbFg" : chkSbFg.toString(),
			    	"keyWord" : $("#keyWordMy").val(),
			    	"selMjRange" : selectedBox,
			    	"bookmarkList" : bookmarkList
			    }),
		  		method : "POST",
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				success: function(data){
					$("#appndSbjt").empty();
					var rs = data.SBJT_LIST;
					var html = '';
					
					if(rs != null){
						for(var i=0; i < rs.length; i++){
							
							var clssNm = '';
							if(rs[i].SBJT_FG == 'U0209020'){
								clssNm = 'nati_navy';
							}else if(rs[i].SBJT_FG == 'U0209021'){
								clssNm = 'alon_red';
							}else if(rs[i].SBJT_FG == 'U0209022'){
								clssNm = 'styl_skyb';
							}else{
								clssNm = 'styl_gray';	//해양대용 공통(나중에 배경색 더 달라고하기)
							}
							
							//찜 세팅
							var onRed = "";
							if(bookmarkList != undefined){							
					        	bookmarkList.forEach(function(bookmark) {	            		
					        		if(bookmark.docId == rs[i].DOC_ID){
					            		 onRed = 'on_red';            			
					        		}
						 		});		
							}
							
							html += '<div class="item p-3">';
							html += 	'<div class="d-inline-flex form-check">';
							html += 		'<label class="blind" for="lessons_'+rs[i].DOC_ID+'">선택</label>';
							html += 		'<input type="checkbox" id="lessons_'+rs[i].DOC_ID+'" class="form-check-input chk-before" value="'+rs[i].DOC_ID+'">';
							html += 	'</div>';
							html += 	'<section class="d-inline-flex flex-column flex-lg-row justify-content-between">';
							html += 		'<input type="hidden" id="sbInfo-'+rs[i].DOC_ID+'" value="'+rs[i].DOC_ID+'" aria-YEAR="'+rs[i].YEAR+'" aria-SMT="'+rs[i].SMT+'" aria-SMT_NM="'+rs[i].SMT_NM+'" aria-SUBJECT_CD="'+rs[i].SUBJECT_CD+'" aria-MAJOR_CD="'+rs[i].MAJOR_CD+'" aria-MAJOR_NM="'+rs[i].MAJOR_NM+'" aria-COM_GRADE="'+rs[i].COM_GRADE+'" aria-COM_GRADE_NM="'+rs[i].COM_GRADE+'학년'+'" aria-COLG_CD="'+rs[i].COLG_CD+'" aria-COLG_NM="'+rs[i].COLG_NM+'" aria-DEPT_CD="'+rs[i].DEPT_CD+'" aria-DEPT_NM="'+rs[i].DEPT_NM+'" aria-COMDIV_CODE="'+rs[i].COMDIV_CODE+'" aria-COMDIV_CODE_NM="'+rs[i].COMDIV_CODE_NM+'" aria-SUBJECT_NM="'+rs[i].SUBJECT_NM+'" aria-SUBJECT_ENM="'+(rs[i].SUBJECT_ENM||'')+'" aria-CDT_NUM="'+rs[i].CDT_NUM+'" aria-ORG_CODE="INTERNAL"  aria-WTIME_NUM="'+rs[i].WTIME_NUM+'" aria-PTIME_NUM="'+rs[i].PTIME_NUM+'" aria-CHG_RESN=""/>';
							html += 		'<div class="d-flex flex-column align-items-start" id="div-'+rs[i].DOC_ID+'">';
							html += 			'<ul class="cate d-flex flex-wrap gap-2">';
							html += 				'<li class="text-white '+clssNm+'">'+rs[i].COMDIV_CODE_NM+'</li>';
							html += 				'<li class="border">';
							html += 					'<span>'+rs[i].MAJOR_NM+'</span>';
							html += 				'</li>';
							html += 			'</ul>';
							html += 			'<h4 class="text-truncate ellip_2 my-2 h-sbjt-kor-nm" id="sbjtNm-'+rs[i].DOC_ID+'">'+rs[i].SUBJECT_NM+'</h4>';
							html += 			'<div class="d-flex flex-row w-100 mb-1">';
							html += 				'<strong class="d-block me-2">['+rs[i].SUBJECT_CD+']</strong>';
							html += 				'<span class="d-block mb-0 text-truncate text-dark text-opacity-75">'+(rs[i].SUBJECT_ENM||'')+'</span>';
							html += 			'</div>';
							html += 			'<div class="w-100">';
							html += 				'<dl class="d-inline-flex position-relative pe-4">';
							html += 					'<dt class="me-2">편성</dt>';
							html += 					'<dd class="text-dark text-opacity-75">'+rs[i].YEAR+'학년도 '+rs[i].COM_GRADE+'학년'+' '+rs[i].SMT_NM+'</dd>';
							html += 				'</dl>';
							html += 				'<dl class="d-inline-flex">';
							html += 					'<dt class="me-2">학점</dt>';
							html += 					'<dd class="text-dark text-opacity-75">'+rs[i].CDT_NUM+'-'+rs[i].WTIME_NUM+'-'+rs[i].PTIME_NUM+'</dd>';
							html +=					'</dl>';
							html += 			'</div>';
							html += 		'</div>';
							html += 		'<ol class="d-inline-flex btn_wrap">';	
							html += 			'<li class="like_container ' + onRed + '" id="'+rs[i].DOC_ID+'">';
							html += 				'<span class="link_cnt">';
							html += 					'<svg xmlns="http://www.w3.org/2000/svg" width="20" height="18" viewBox="0 0 20 18" onclick="likeChange(\''+rs[i].DOC_ID+'\', \'sbjt\')" id="likeChange'+rs[i].DOC_ID+'">';
							html += 					'<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0" />';
							html += 					'<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202" /></svg>';
							html += 					'<span class="text-center ps-1 d-inline-block d-lg-none">찜하기</span>';
							html += 				'</span>';
							html += 			'</li>';
							html += 			'<li>';
							html += 				'<button type="button" class="border-0 bg-transparent btn_put" onclick="sbjtContain(\''+rs[i].DOC_ID+'\');">';
							html += 				'<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none">';
							html += 				'<g fill="#4571E9" clip-path="url(#a)">';
							html += 				'<path d="M17.018.982A3.33 3.33 0 0 0 14.648 0H3.352A3.355 3.355 0 0 0 0 3.352v11.296A3.355 3.355 0 0 0 3.352 18h11.296A3.355 3.355 0 0 0 18 14.648V3.352a3.33 3.33 0 0 0-.982-2.37Zm-.073 13.666a2.3 2.3 0 0 1-2.297 2.297H3.352a2.3 2.3 0 0 1-2.297-2.297V3.352a2.3 2.3 0 0 1 2.297-2.297h11.296c.614 0 1.19.239 1.625.672.433.434.672 1.011.672 1.625v11.296Z" />';
							html += 				'<path d="M12.169 7.646a.527.527 0 0 0-.746 0L9.527 9.54V3.916a.527.527 0 0 0-1.054 0v5.626L6.577 7.646a.527.527 0 1 0-.746.746l2.796 2.796a.527.527 0 0 0 .746 0l2.796-2.796a.527.527 0 0 0 0-.746ZM14.084 13.557H3.916a.527.527 0 0 0 0 1.054h10.168a.527.527 0 0 0 0-1.054Z" /></g>';
							html += 				'<defs>';
							html += 				'<clipPath id="a">';
							html += 				'<path fill="#fff" d="M0 0h18v18H0z" /></clipPath></defs></svg>';
							html += 				'<span class="text-center ps-1 d-inline-block d-lg-none">담기</span>';
							html += 			'</button>';
							html += 			'</li>';
							html += 		'</ol>';
							html += 	'</section>';
							html += '</div>';
						}

					}				
					$("#appndSbjt").append(html);
					hideLoading();
				} 
			})	// end ajax
		}
	}
	
	
	
	// 타 대학/기관/산업체 교과목 추가하기
	var i = 0;
	function addOtherSbjt(){
		
		if($("#ext_openMajor").val().trim() == ''){
			alert("개설전공명을 입력해주세요.");
			return false;
		}
		if($("#ext_nameMajor").val().trim() == ''){
			alert("과목명을 입력해주세요.");
			return false;
		}

		var now = new Date();
		var yy = now.getFullYear();
		
		var extGubun  	= $("#ext_gubun").val();					// 구분
		var extGubunNm	= $("#ext_gubun option:checked").text();	// 구분명
		var extOpMj	  	= $("#ext_openMajor").val();				// 개설전공명
		var extNmMj   	= $("#ext_nameMajor").val();				// 과목명
		var extYear   	= $("#ext_year").val(); 					// 학년
		var extYearNm 	= $("#ext_year option:checked").text(); 	// 학년명
		var extSemi   	= $("#ext_semi").val();						// 학기
		var extSemiNm  	= $("#ext_semi option:checked").text();		// 학기명
		var extScore  	= $("#ext_score").val();					// 학점
		var origFg    	= 'EXTERNAL';								// 출처구분
		var colgNm   	= '타 대학/기관/산업체';							// 교과목 분류

		
		var clssNm = '';
		if(extGubun == 'U0209020'){
			clssNm = 'nati_navy';
		}else if(extGubun == 'U0209021'){
			clssNm = 'alon_red';
		}else if(extGubun == 'U0209022'){
			clssNm = 'styl_skyb';
		}else{
			clssNm = 'styl_gray';	//해양대용 공통(나중에 배경색 더 달라고하기)
		}

		var html = '';
		
		html += '<div class="item p-3 head-after otherColg" id="headAfter-'+i+'" aria-cnt="'+i+'" aria-COLG_NM="'+colgNm+'" aria-YEAR="'+yy+'" aria-ORG_CODE="'+origFg+'" aria-COMDIV_CODE="'+extGubun+'" aria-COMDIV_CODE_NM="'+extGubunNm+'" aria-MAJOR_NM="'+extOpMj+'" aria-SUBJECT_NM="'+extNmMj+'" aria-COM_GRADE="'+extYear+'" aria-COM_GRADE_NM="'+extYearNm+'" aria-SMT="'+extSemi+'" aria-SMT_NM="'+extSemiNm+'" aria-CDT_NUM="'+extScore+'" aria-CHG_RESN="">';
		html += 	'<div class="d-inline-flex form-check">';
		html += 		'<label class="blind" for="lessons_'+i+'">선택</label>';
		html += 		'<input type="checkbox" id="lessons_'+i+'" class="form-check-input chk-after">';
		html += 	'</div>';
		html += 	'<section class="d-inline-flex flex-column flex-lg-row justify-content-between">';
		html += 		'<div class="d-flex flex-column align-items-start">';
		html +=				'<ul class="cate d-flex flex-wrap gap-2">';
		
		html +=					'<li class="text-white '+clssNm+'">'+$("#ext_gubun option:checked").text()+'</li>';
		html +=					'<li class="border">';
		html +=						'<span>'+extOpMj+'</span>';
		html +=					'</li>';
		html +=				'</ul>';
		html += 			'<h4 class="ellip_2 my-2">'+extNmMj+'</h4>';
		html += 			'<div class="d-flex flex-row w-100 mb-1">';
		html += 			'</div>';
		html += 			'<div class="w-100">';
		html += 				'<dl class="d-inline-flex position-relative pe-4">';
		html += 					'<dt class="me-2">편성</dt>';
		html += 					'<dd class="text-dark text-opacity-75">'+yy+'년 '+extYear+'학년 '+extSemiNm+'</dd>';
		html += 				'</dl>';
		html += 				'<dl class="d-inline-flex">';
		html += 					'<dt class="me-2">학점</dt>';
		html += 					'<dd class="text-dark text-opacity-75">'+extScore+'-0-0</dd>';
		html += 				'</dl>';
		html += 			'</div>';
		html += 		'</div>';
		html += 		'<ol class="d-inline-flex btn_wrap">';
		html += 			'<li>';
		html += 				'<button type="button" class="border-0 bg-transparent btn_dele" onclick="delSbjt(\''+i+'\')">';
		html += 					'<span class="text-center ps-1 d-inline-block d-lg-none text-white">삭제</span>';
		html += 				'</button>';
		html += 			'</li>';
		html += 		'</ol>';
		html += 	'</section>';
		html += '</div>';

		$("#no-data-img").remove();
		$("#appndSbjtChoice").append(html);
		
		$("#openExternal").modal("hide");
		$("#enrollSubj").modal('show');			
		
		i++;
	}
	
	// 선택일괄담기
	
	function sbjtContain(DOC_ID){		
		var html = '';
		var sId  = '';
		
		if(DOC_ID != null){
			if(duplSbjtArr.indexOf(DOC_ID) != -1){
				alert("\'"+$("#sbjtNm-"+DOC_ID).text()+"\' 은 중복된 과목입니다.");
				return false;
			}
			
			sId = $("#sbInfo-"+DOC_ID);
			
			//셀렉트박스 분기용(전공/교양에 따라)
			var sbjtFgHtml = '';
			
			var majorArray = ['UE010021', 'UE010022', 'UE010024'];
			var genArray = ['UE010011', 'UE010012']
			
			if(majorArray.includes(sId.attr('aria-COMDIV_CODE'))){
				sbjtFgHtml = '<li>'+
							 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
							 '<option value="UE010021"'+ (sId.attr('COMDIV_CODE') == "UE010021" ? 'selected' : '') +'>전공필수</option>'+
							 '<option value="UE010022"'+ (sId.attr('COMDIV_CODE') == "UE010022" ? 'selected' : '') +'>전공선택</option>'+
							 '<option value="UE010024"'+ (sId.attr('COMDIV_CODE') == "UE010024" ? 'selected' : '') +'>전공기초</option>'+
							 '</select>'+
							 '</li>';				
			}else if(genArray.includes(sId.attr('aria-COMDIV_CODE'))){
				sbjtFgHtml = '<li>'+
							 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
							 '<option value="UE010011"'+ (sId.attr('COMDIV_CODE') == "UE010011" ? 'selected' : '') +'>교양필수</option>'+
							 '<option value="UE010012"'+ (sId.attr('COMDIV_CODE') == "UE010012" ? 'selected' : '') +'>교양선택</option>'+
							 '</select>'+
							 '</li>';	
			}else{
				sbjtFgHtml = '<li>'+
				 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
				 '<option value="UE010031"'+ (sId.attr('COMDIV_CODE') == "UE010031" ? 'selected' : '') +'>교직</option>'+							 
				 '</select>'+
				 '</li>';	
			}

// 			if (majorArray.includes(sId.attr('aria-COMDIV_CODE'))) {
// 			    console.log(sbjt.CHG_COMDIV_CODE + " / " + sbjt.CHG_COMDIV_CODE_NM + " : " + sbjt.SUBJECT_NM);
// 			    var majorOptions = [
// 			        { value: 'UE010021', label: '전공필수' },
// 			        { value: 'UE010022', label: '전공선택' },
// 			        { value: 'UE010024', label: '전공기초' }
// 			    ];
// 			    sbjtFgHtml = createSelectBox(sbjt, majorOptions);
// 			} else if (genArray.includes(sbjt.CHG_COMDIV_CODE)) {
// 			    var genOptions = [
// 			        { value: 'UE010011', label: '교양필수' },
// 			        { value: 'UE010012', label: '교양선택' }
// 			    ];
// 			    sbjtFgHtml = createSelectBox(sbjt, genOptions);
// 			}
			
			
			html += '<div class="item p-3 head-after" id="headAfter-'+DOC_ID+'">';
			html += 	'<input type="hidden" name="sdmAddList" aria-YEAR="'+sId.attr('aria-YEAR')+'" aria-SMT="'+sId.attr('aria-SMT')+'" aria-SMT_NM="'+sId.attr('aria-SMT_NM')+'" aria-SUBJECT_CD="'+sId.attr('aria-SUBJECT_CD')+'" aria-MAJOR_CD="'+sId.attr('aria-MAJOR_CD')+'" aria-MAJOR_NM="'+sId.attr('aria-MAJOR_NM')+'" aria-COM_GRADE="'+sId.attr('aria-COM_GRADE')+'" aria-COM_GRADE_NM="'+sId.attr('aria-COM_GRADE_NM')+'" aria-COLG_CD="'+sId.attr('aria-COLG_CD')+'" aria-COLG_NM="'+sId.attr('aria-COLG_NM')+'" aria-DEPT_CD="'+sId.attr('aria-DEPT_CD')+'" aria-DEPT_NM="'+sId.attr('aria-DEPT_NM')+'" aria-COMDIV_CODE="'+sId.attr('aria-COMDIV_CODE')+'" aria-COMDIV_CODE_NM="'+sId.attr('aria-COMDIV_CODE_NM')+'" aria-SUBJECT_NM="'+sId.attr('aria-SUBJECT_NM')+'" aria-SUBJECT_ENM="'+sId.attr('aria-SUBJECT_ENM')+'" aria-CDT_NUM="'+sId.attr('aria-CDT_NUM')+'" aria-WTIME_NUM="'+sId.attr('aria-WTIME_NUM')+'" aria-PTIME_NUM="'+sId.attr('aria-PTIME_NUM')+'"  aria-ORG_CODE="'+sId.attr('aria-ORG_CODE')+'" aria-ORG_COMDIV_CODE="'+sId.attr('aria-COMDIV_CODE')+'" aira-CHG_RESN=""/>';
			html += 	'<div class="d-inline-flex form-check">';
			html +=			'<label class="blind" for="lessons_'+DOC_ID+'">선택</label>';
			html +=			'<input type="checkbox" id="lessons_'+DOC_ID+'" class="form-check-input chk-after">';
			html +=		'</div>';
			html += 	'<section class="d-inline-flex flex-column flex-lg-row justify-content-between">';
			html += 		'<div class="d-flex flex-column align-items-start div-after">';
			html += 			'<ul class="cate d-flex flex-wrap gap-2">';
			html += 				sbjtFgHtml;
			html += 				'<li class="border">';
			html += 					'<span>'+sId.attr('aria-MAJOR_NM')+'</span>';
			html += 				'</li>';
			html += 			'</ul>';
			html += 			'<h4 class="text-truncate ellip_2 my-2 h-sbjt-kor-nm" id="sbjtNm-'+DOC_ID+'">'+sId.attr('aria-SUBJECT_NM')+'</h4>';
			html += 			'<div class="d-flex flex-row w-100 mb-1">';
			html += 				'<strong class="d-block me-2">['+sId.attr('aria-SUBJECT_CD')+']</strong>';
			html += 				'<span class="d-block mb-0 text-truncate text-dark text-opacity-75">'+sId.attr('aria-SUBJECT_ENM')+'</span>';
			html += 			'</div>';
			html += 			'<div class="w-100">';
			html += 				'<dl class="d-inline-flex position-relative pe-4">';
			html += 					'<dt class="me-2">편성</dt>';
			html += 					'<dd class="text-dark text-opacity-75">'+sId.attr('aria-YEAR')+'학년도 '+sId.attr('aria-COM_GRADE')+'학년 '+sId.attr('aria-SMT_NM')+'</dd>';
			html += 				'</dl>';
			html += 				'<dl class="d-inline-flex">';
			html += 					'<dt class="me-2">학점</dt>';
			html += 					'<dd class="text-dark text-opacity-75">'+sId.attr('aria-CDT_NUM')+'-'+sId.attr('aria-WTIME_NUM')+'-'+sId.attr('aria-PTIME_NUM')+'</dd>';
			html +=					'</dl>';
			html += 			'</div>';
			html +=			'</div>';
			html += 		'<ol class="d-inline-flex btn_wrap">';
			html +=				'<li>';
			html +=					'<button type="button" class="border-0 bg-transparent btn_dele" onclick="delSbjt(\''+DOC_ID+'\')">';
			html +=						'<span class="text-center ps-1 d-inline-block d-lg-none text-white">삭제</span>';
			html +=					'</button>';
			html +=				'</li>';
			html +=			'</ol>';
			html += 	'</section>';
			html += '</div>';

			duplSbjtArr.push(DOC_ID);

			$("#appndSbjtChoice").append(html);
			$("#no-data-img").remove();
		}else{
			
			if($(".chk-before:checked").length == 0){
					alert("과목을 선택해주세요.");
					return false;
			}else{
				var chkIdx 	= $(".chk-before:checked");
				
				for(var i=0; i < chkIdx.length; i++){
					if(duplSbjtArr.indexOf(chkIdx[i].value) != -1){
						alert("\'"+$("#sbjtNm-"+chkIdx[i].value).text()+"\' 은 중복된 과목입니다. 동일한 교과목은 하나만 선택 가능합니다.");
						return false;
					}
					
					sId = $("#sbInfo-"+chkIdx[i].value);	
					
					//셀렉트박스 분기용(전공/교양에 따라)
					var majorArray = ['UE010021', 'UE010022', 'UE010024', 'UE10031'];
					var genArray = ['UE010011', 'UE010012']
					var sbjtFgHtml = '';
					if(majorArray.includes(sId.attr('aria-COMDIV_CODE'))){
						sbjtFgHtml = '<li>'+
									 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
									 '<option value="UE010021"'+ (sId.attr('COMDIV_CODE') == "UE010021" ? 'selected' : '') +'>전공필수</option>'+
									 '<option value="UE010022"'+ (sId.attr('COMDIV_CODE') == "UE010022" ? 'selected' : '') +'>전공선택</option>'+
									 '<option value="UE010024"'+ (sId.attr('COMDIV_CODE') == "UE010024" ? 'selected' : '') +'>전공기초</option>'+
									 '</select>'+
									 '</li>';				
					}else if(genArray.includes(sId.attr('aria-COMDIV_CODE'))){
						sbjtFgHtml = '<li>'+
									 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
									 '<option value="UE010011"'+ (sId.attr('COMDIV_CODE') == "UE010011" ? 'selected' : '') +'>교양필수</option>'+
									 '<option value="UE010012"'+ (sId.attr('COMDIV_CODE') == "UE010012" ? 'selected' : '') +'>교양선택</option>'+
									 '</select>'+
									 '</li>';	
					}else{
						sbjtFgHtml = '<li>'+
						 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
						 '<option value="UE010031"'+ (sId.attr('COMDIV_CODE') == "UE010031" ? 'selected' : '') +'>교직</option>'+									 
						 '</select>'+
						 '</li>';		
					}
					
					html += '<div class="item p-3 head-after" id="headAfter-'+chkIdx[i].value+'">';
					html += 	'<input type="hidden" name="sdmAddList" aria-YEAR="'+sId.attr('aria-YEAR')+'" aria-SMT="'+sId.attr('aria-SMT')+'" aria-SMT_NM="'+sId.attr('aria-SMT_NM')+'" aria-SUBJECT_CD="'+sId.attr('aria-SUBJECT_CD')+'" aria-MAJOR_CD="'+sId.attr('aria-MAJOR_CD')+'" aria-MAJOR_NM="'+sId.attr('aria-MAJOR_NM')+'" aria-COM_GRADE="'+sId.attr('aria-COM_GRADE')+'" aria-COM_GRADE_NM="'+sId.attr('aria-COM_GRADE_NM')+'" aria-COLG_CD="'+sId.attr('aria-COLG_CD')+'" aria-COLG_NM="'+sId.attr('aria-COLG_NM')+'" aria-DEPT_CD="'+sId.attr('aria-DEPT_CD')+'" aria-DEPT_NM="'+sId.attr('aria-DEPT_NM')+'" aria-COMDIV_CODE="'+sId.attr('aria-COMDIV_CODE')+'" aria-COMDIV_CODE_NM="'+sId.attr('aria-COMDIV_CODE_NM')+'" aria-SUBJECT_NM="'+sId.attr('aria-SUBJECT_NM')+'" aria-SUBJECT_ENM="'+sId.attr('aria-SUBJECT_ENM')+'" aria-CDT_NUM="'+sId.attr('aria-CDT_NUM')+'" aria-WTIME_NUM="'+sId.attr('aria-WTIME_NUM')+'" aria-PTIME_NUM="'+sId.attr('aria-PTIME_NUM')+'" aria-ORG_CODE="'+sId.attr('aria-ORG_CODE')+'" aria-ORG_COMDIV_CODE="'+sId.attr('aria-COMDIV_CODE')+'" />';
					html += 	'<div class="d-inline-flex form-check">';
					html +=			'<label class="blind" for="lessons_'+DOC_ID+'">선택</label>';
					html +=			'<input type="checkbox" id="lessons_'+DOC_ID+'" class="form-check-input chk-after">';
					html +=		'</div>';
					html += 	'<section class="d-inline-flex flex-column flex-lg-row justify-content-between">';
					html += 		'<div class="d-flex flex-column align-items-start div-after">';
					html += 			'<ul class="cate d-flex flex-wrap gap-2">';
					html += 				sbjtFgHtml;
					html += 				'<li class="border">';
					html += 					'<span>'+sId.attr('aria-MAJOR_NM')+'</span>';
					html += 				'</li>';
					html += 			'</ul>';
					html += 			'<h4 class="text-truncate ellip_2 my-2 h-sbjt-kor-nm" id="sbjtNm-'+DOC_ID+'">'+sId.attr('aria-SUBJECT_NM')+'</h4>';
					html += 			'<div class="d-flex flex-row w-100 mb-1">';
					html += 				'<strong class="d-block me-2">['+sId.attr('aria-SUBJECT_CD')+']</strong>';
					html += 				'<span class="d-block mb-0 text-truncate text-dark text-opacity-75">'+sId.attr('aria-SUBJECT_ENM')+'</span>';
					html += 			'</div>';
					html += 			'<div class="w-100">';
					html += 				'<dl class="d-inline-flex position-relative pe-4">';
					html += 					'<dt class="me-2">편성</dt>';
					html += 					'<dd class="text-dark text-opacity-75">'+sId.attr('aria-YEAR')+'학년도 '+sId.attr('aria-COM_GRADE')+'학년 '+sId.attr('aria-SMT_NM')+'</dd>';
					html += 				'</dl>';
					html += 				'<dl class="d-inline-flex">';
					html += 					'<dt class="me-2">학점</dt>';
					html += 					'<dd class="text-dark text-opacity-75">'+sId.attr('aria-CDT_NUM')+'-'+sId.attr('aria-WTIME_NUM')+'-'+sId.attr('aria-PTIME_NUM')+'</dd>';
					html +=					'</dl>';
					html += 			'</div>';
					html +=			'</div>';
					html += 		'<ol class="d-inline-flex btn_wrap">';
					html +=				'<li>';
					html +=					'<button type="button" class="border-0 bg-transparent btn_dele" onclick="delSbjt(\''+DOC_ID+'\')">';
					html +=						'<span class="text-center ps-1 d-inline-block d-lg-none text-white">삭제</span>';
					html +=					'</button>';
					html +=				'</li>';
					html +=			'</ol>';
					html += 	'</section>';
					html += '</div>';
					
					duplSbjtArr.push(chkIdx[i].value);
				}
				$("#appndSbjtChoice").append(html);
				$("#no-data-img").remove();
				
			}
		}
		
		// 클래스 'selectSbjtFg'를 가진 모든 selectbox에 대해 change 이벤트 핸들러를 추가
	    $('.selectSbjtFg').on('change', function() {
	    	
	        // 이 selectbox의 value와 선택된 텍스트를 가져옴
	        var selectedValue = $(this).val();
	        var selectedText = $(this).find('option:selected').text();
	        
	        // 이 selectbox를 포함하고 있는 상위 div를 찾고, 그 안에서 input 태그를 찾아 aria 속성을 업데이트
	        var parentDiv = $(this).closest('div.item');
	        parentDiv.find('input[name="sdmAddList"]').attr('aria-COMDIV_CODE', selectedValue);
	        parentDiv.find('input[name="sdmAddList"]').attr('aria-COMDIV_CODE_NM', selectedText);
	    });
	}
	
	// 설계 선택 목록 삭제 or 전체삭제 button
	function delSbjt(no){
		
		if(no != 'all'){
			$("#headAfter-"+no).remove();
			
			if(duplSbjtArr.indexOf(no) != -1){
				duplSbjtArr.splice(duplSbjtArr.indexOf(no), 1);
			}
		}else{
			$("#appndSbjtChoice").empty();
			duplSbjtArr.splice(0, duplSbjtArr.length);
		}
		
		if($(".head-after").length == 0){
			
			var html = '';
			
			html =	'<div class="lesson_wrap no_contents_wrap d-flex justify-content-center align-items-center" id="no-data-img">'
			 	 +		'<div class="d-flex flex-column justify-content-center align-items-center">'
			 	 +			'<img src="../images/kmou_noshad_big.png" alt="해양이" />'
			 	 +			'<p class="text-center mt-2">'
			 	 +				'왼쪽 상자에서 원하는 교과목을<br />담아주세요.'
			 	 +			'</p>'
			 	 +		'</div>'
			 	 +	'</div>';
	
			$("#appndSbjtChoice").empty();
			$("#appndSbjtChoice").append(html);
		}
	}
	
	// 과목 목록 불러오기 button
	function getSbjtList(){		
		if($("#selectColg").val() == ""){
			alert("대학을 선택해주세요.");
			return false;
		}
		//대학/전공 선택값(마지막 선택값만 API로 넘긴다)
		var selectedBox = ($("#selectMj").val() != "" ? $("#selectMj").val() : ($("#selectDept").val() != "" ? $("#selectDept").val() : ($("#selectColg").val() != "" ? $("#selectColg").val() : "")));	
		//이수구분 선택값(아무것도 선택하지 않을 시엔 배열에 전체 값을 강제로 넣는다.)
		var chkSbFg = [];		
		
		var chkSbFg = [];
		$(".chk-sbjt-search:checked").each(function () {
			chkSbFg.push($(this).val());
		});		
					
		//로딩
		showLoading();
		//API call
		$.ajax({
			url: '/web/studPlan/getSbjtList.do?mId=36',
			contentType:'application/json',
		    data: JSON.stringify({		    	
		    	"chkSbFg" : chkSbFg.toString(),
		    	"keyWord" : $("#keyWord").val(),
		    	"selMjRange" : selectedBox		    	
	  		}),
	  		method : "POST",
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			success: function(data){
				$("#appndSbjt").empty();
				var rs = data.SBJT_LIST;
				var html = '';
				
				if(rs != null){
					for(var i=0; i < rs.length; i++){
						
						var clssNm = '';
						if(rs[i].SBJT_FG == 'U0209020'){
							clssNm = 'nati_navy';
						}else if(rs[i].SBJT_FG == 'U0209021'){
							clssNm = 'alon_red';
						}else if(rs[i].SBJT_FG == 'U0209022'){
							clssNm = 'styl_skyb';
						}else{
							clssNm = 'styl_gray';	//해양대용 공통(나중에 배경색 더 달라고하기)
						}
						
						//찜 세팅
						var onRed = "";
						if(bookmarkList != undefined){							
				        	bookmarkList.forEach(function(bookmark) {	            		
				        		if(bookmark.docId == rs[i].DOC_ID){
				            		 onRed = 'on_red';            			
				        		}
					 		});		
						}
						
						html += '<div class="item p-3">';
						html += 	'<div class="d-inline-flex form-check">';
						html += 		'<label class="blind" for="lessons_'+rs[i].DOC_ID+'">선택</label>';
						html += 		'<input type="checkbox" id="lessons_'+rs[i].DOC_ID+'" class="form-check-input chk-before" value="'+rs[i].DOC_ID+'">';
						html += 	'</div>';
						html += 	'<section class="d-inline-flex flex-column flex-lg-row justify-content-between">';
						html += 		'<input type="hidden" id="sbInfo-'+rs[i].DOC_ID+'" value="'+rs[i].DOC_ID+'" aria-YEAR="'+rs[i].YEAR+'" aria-SMT="'+rs[i].SMT+'" aria-SMT_NM="'+rs[i].SMT_NM+'" aria-SUBJECT_CD="'+rs[i].SUBJECT_CD+'" aria-MAJOR_CD="'+rs[i].MAJOR_CD+'" aria-MAJOR_NM="'+rs[i].MAJOR_NM+'" aria-COM_GRADE="'+rs[i].COM_GRADE+'" aria-COM_GRADE_NM="'+rs[i].COM_GRADE+'학년'+'" aria-COLG_CD="'+rs[i].COLG_CD+'" aria-COLG_NM="'+rs[i].COLG_NM+'" aria-DEPT_CD="'+rs[i].DEPT_CD+'" aria-DEPT_NM="'+rs[i].DEPT_NM+'" aria-COMDIV_CODE="'+rs[i].COMDIV_CODE+'" aria-COMDIV_CODE_NM="'+rs[i].COMDIV_CODE_NM+'" aria-SUBJECT_NM="'+rs[i].SUBJECT_NM+'" aria-SUBJECT_ENM="'+(rs[i].SUBJECT_ENM||'')+'" aria-CDT_NUM="'+rs[i].CDT_NUM+'" aria-ORG_CODE="INTERNAL"  aria-WTIME_NUM="'+rs[i].WTIME_NUM+'" aria-PTIME_NUM="'+rs[i].PTIME_NUM+'" />';
						html += 		'<div class="d-flex flex-column align-items-start" id="div-'+rs[i].DOC_ID+'">';
						html += 			'<ul class="cate d-flex flex-wrap gap-2">';
						html += 				'<li class="text-white '+clssNm+'">'+rs[i].COMDIV_CODE_NM+'</li>';
						html += 				'<li class="border">';
						html += 					'<span>'+rs[i].MAJOR_NM+'</span>';
						html += 				'</li>';
						html += 			'</ul>';
						html += 			'<h4 class="text-truncate ellip_2 my-2 h-sbjt-kor-nm" id="sbjtNm-'+rs[i].DOC_ID+'">'+rs[i].SUBJECT_NM+'</h4>';
						html += 			'<div class="d-flex flex-row w-100 mb-1">';
						html += 				'<strong class="d-block me-2">['+rs[i].SUBJECT_CD+']</strong>';
						html += 				'<span class="d-block mb-0 text-truncate text-dark text-opacity-75">'+(rs[i].SUBJECT_ENM||'')+'</span>';
						html += 			'</div>';
						html += 			'<div class="w-100">';
						html += 				'<dl class="d-inline-flex position-relative pe-4">';
						html += 					'<dt class="me-2">편성</dt>';
						html += 					'<dd class="text-dark text-opacity-75">'+rs[i].YEAR+'학년도 '+rs[i].COM_GRADE+'학년'+' '+rs[i].SMT_NM+'</dd>';
						html += 				'</dl>';
						html += 				'<dl class="d-inline-flex">';
						html += 					'<dt class="me-2">학점</dt>';
						html += 					'<dd class="text-dark text-opacity-75">'+rs[i].CDT_NUM+'-'+rs[i].WTIME_NUM+'-'+rs[i].PTIME_NUM+'</dd>';
						html +=					'</dl>';
						html += 			'</div>';
						html += 		'</div>';
						html += 		'<ol class="d-inline-flex btn_wrap">';	
						html += 			'<li class="like_container ' + onRed + '" id="'+rs[i].DOC_ID+'">';
						html += 				'<span class="link_cnt">';
						html += 					'<svg xmlns="http://www.w3.org/2000/svg" width="20" height="18" viewBox="0 0 20 18" onclick="likeChange(\''+rs[i].DOC_ID+'\', \'sbjt\')" id="likeChange'+rs[i].DOC_ID+'">';
						html += 					'<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0" />';
						html += 					'<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202" /></svg>';
						html += 					'<span class="text-center ps-1 d-inline-block d-lg-none">찜하기</span>';
						html += 				'</span>';
						html += 			'</li>';
						html += 			'<li>';
						html += 				'<button type="button" class="border-0 bg-transparent btn_put" onclick="sbjtContain(\''+rs[i].DOC_ID+'\');">';
						html += 				'<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="none">';
						html += 				'<g fill="#4571E9" clip-path="url(#a)">';
						html += 				'<path d="M17.018.982A3.33 3.33 0 0 0 14.648 0H3.352A3.355 3.355 0 0 0 0 3.352v11.296A3.355 3.355 0 0 0 3.352 18h11.296A3.355 3.355 0 0 0 18 14.648V3.352a3.33 3.33 0 0 0-.982-2.37Zm-.073 13.666a2.3 2.3 0 0 1-2.297 2.297H3.352a2.3 2.3 0 0 1-2.297-2.297V3.352a2.3 2.3 0 0 1 2.297-2.297h11.296c.614 0 1.19.239 1.625.672.433.434.672 1.011.672 1.625v11.296Z" />';
						html += 				'<path d="M12.169 7.646a.527.527 0 0 0-.746 0L9.527 9.54V3.916a.527.527 0 0 0-1.054 0v5.626L6.577 7.646a.527.527 0 1 0-.746.746l2.796 2.796a.527.527 0 0 0 .746 0l2.796-2.796a.527.527 0 0 0 0-.746ZM14.084 13.557H3.916a.527.527 0 0 0 0 1.054h10.168a.527.527 0 0 0 0-1.054Z" /></g>';
						html += 				'<defs>';
						html += 				'<clipPath id="a">';
						html += 				'<path fill="#fff" d="M0 0h18v18H0z" /></clipPath></defs></svg>';
						html += 				'<span class="text-center ps-1 d-inline-block d-lg-none">담기</span>';
						html += 			'</button>';
						html += 			'</li>';
						html += 		'</ol>';
						html += 	'</section>';
						html += '</div>';
					}

				}				
				$("#appndSbjt").append(html);
				hideLoading();
			} 
		})	// end ajax
	}
	
	var sbjtArray = new Array();
	
	// 과목등록하기 Modal (저장하기 버튼 클릭시)
	function sbjtDesignScope(){
		var addSbjtArray = new Array();
		
		// 교내
		$("input[name=sdmAddList]").each(function(){
			var obj = new Object();					
			obj.SUBJECT_CD 			= $(this).attr('aria-SUBJECT_CD');
			obj.SUBJECT_NM 			= $(this).attr('aria-SUBJECT_NM');
			obj.SUBJECT_ENM 		= $(this).attr('aria-SUBJECT_ENM');
			obj.ORG_CODE 			= $(this).attr("aria-ORG_CODE");		 // 출처구분
			obj.COLG_CD 			= $(this).attr('aria-COLG_CD');
			obj.COLG_NM 			= $(this).attr('aria-COLG_NM');
			obj.DEPT_CD 			= $(this).attr('aria-DEPT_CD');
			obj.DEPT_NM 			= $(this).attr('aria-DEPT_NM');
			obj.MAJOR_CD 			= $(this).attr('aria-MAJOR_CD');
			obj.MAJOR_NM 			= $(this).attr('aria-MAJOR_NM');
			obj.YEAR 				= $(this).attr('aria-YEAR');
			obj.COM_GRADE 			= $(this).attr('aria-COM_GRADE');
			obj.COM_GRADE_NM 		= $(this).attr('aria-COM_GRADE_NM');
			obj.SMT 				= $(this).attr('aria-SMT');
			obj.SMT_NM 				= $(this).attr('aria-SMT_NM');
			obj.WTIME_NUM 			= $(this).attr('aria-WTIME_NUM');
			obj.PTIME_NUM	 		= $(this).attr('aria-PTIME_NUM');
			obj.CDT_NUM 			= $(this).attr('aria-CDT_NUM');
			obj.COMDIV_CODE 		= $(this).attr('aria-COMDIV_CODE');
			obj.COMDIV_CODE_NM 		= $(this).attr('aria-COMDIV_CODE_NM');
			obj.ORG_COMDIV_CODE		= $(this).attr("aria-ORG_COMDIV_CODE");
			obj.MNRCOM_DIV_CODE 	= $(this).attr('aria-MNRCOM_DIV_CODE');
			obj.MNRCOM_DIV_CODE_NM 	= $(this).attr('aria-MNRCOM_DIV_CODE_NM');
			obj.CHG_RESN 			= $(this).attr('aria-CHG_RESN');			
			
			addSbjtArray.push(obj);			
		});
		
		// 타 대학/기관/산업체
		$(".otherColg").each(function(){
			var obj = new Object();
			var num = $(this).attr("aria-cnt");
			
			obj.SDM_CD 				= '';				 				 				// 학생설계전공코드
			obj.REVSN_NO 			= '';								 				// 개정번호
			obj.ORG_CODE 			= $("#headAfter-"+num).attr("aria-ORG_CODE");		// 출처구분			
			obj.COLG_CD 			= '';	 							 				// 개설대학코드         
			obj.COLG_NM 			= $("#headAfter-"+num).attr("aria-COLG_NM");	 	// 개설대학한글명	 	
			obj.DEPT_CD 			= '';	 							 				// 개설대학코드         
			obj.DEPT_NM 			= $("#headAfter-"+num).attr("aria-DEPT_NM");	 	// 개설대학한글명	 	
			obj.MAJOR_CD 			= ''; 												// 개설학과전공코드 	
			obj.MAJOR_NM 			= $("#headAfter-"+num).attr("aria-MAJOR_NM"); 		// 개설학과전공한글명	
			obj.COMDIV_CODE 		= $("#headAfter-"+num).attr("aria-COMDIV_CODE");	// 교과구분			
			obj.COMDIV_CODE_NM 		= $("#headAfter-"+num).attr("aria-COMDIV_CODE_NM");	// 교과구분명
			obj.ORG_COMDIV_CODE 	= '';	     						 				// 원 교과구분		
			obj.SUBJECT_CD 			= '';	 											// 학수번호 			
			obj.SUBJECT_NM 			= $("#headAfter-"+num).attr("aria-SUBJECT_NM");		// 교과목한글명		
			obj.COM_GRADE 			= $("#headAfter-"+num).attr("aria-COM_GRADE");		// 학년				
			obj.COM_GRADE_NM		= $("#headAfter-"+num).attr("aria-COM_GRADE_NM");	// 학년명
			obj.YEAR 				= $("#headAfter-"+num).attr("aria-YEAR");		    // 편성년도			
			obj.SMT 				= $("#headAfter-"+num).attr("aria-SMT");			// 학기
			obj.SMT_NM 				= $("#headAfter-"+num).attr("aria-SMT_NM");			// 학기명			
			obj.CDT_NUM 			= $("#headAfter-"+num).attr("aria-CDT_NUM");		// 학점				
			obj.WTIME_NUM 			= '';	 											// 이론
			obj.PTIME_NUM 			= '';	 											// 실습
			obj.PTIME_NUM 			= $("#headAfter-"+num).attr("aria-CHG_RESN");		// 실습
						
			addSbjtArray.push(obj);

		});
		
		//교과목 hidden 중 C인것만 삭제
		$('#containInput div#C').remove();		
		
		// 교과목 input hidden으로 데이터 넘기기
		var contains = "";	
		addSbjtArray.forEach(function(sbjt) {
		    contains +=  '<div class="SBJT_LIST_'+sbjt.SUBJECT_CD+'_'+sbjt.MAJOR_CD+'_'+sbjt.YEAR+'_'+sbjt.COM_GRADE+'_'+sbjt.SMT+'" id="C">'
		    		+	'<input type="hidden" name="ORG_CODE" value=""/>'
			    	+ 	'<input type="hidden" name="COLG_CD" value=""/>'
			    	+ 	'<input type="hidden" name="COLG_NM" value=""/>'
			    	+ 	'<input type="hidden" name="DEPT_CD" value=""/>'
			    	+ 	'<input type="hidden" name="DEPT_NM" value=""/>'
			    	+ 	'<input type="hidden" name="COLG_ENM" value=""/>'
			    	+ 	'<input type="hidden" name="MAJOR_CD" value=""/>'
			    	+ 	'<input type="hidden" name="MAJOR_NM" value=""/>'
			    	+ 	'<input type="hidden" name="MAJOR_ENM" value=""/>'
			    	+ 	'<input type="hidden" name="COMDIV_CODE" value=""/>'
			    	+ 	'<input type="hidden" name="COMDIV_CODE_NM" value=""/>'
			    	+ 	'<input type="hidden" name="ORG_COMDIV_CODE" value=""/>'
			    	+ 	'<input type="hidden" name="SUBJECT_CD" value=""/>'
			    	+ 	'<input type="hidden" name="SUBJECT_NM" value=""/>'
			    	+ 	'<input type="hidden" name="SUBJECT_ENM" value=""/>'
			    	+ 	'<input type="hidden" name="COM_GRADE" value=""/>'
			    	+ 	'<input type="hidden" name="COM_GRADE_NM" value=""/>'
			    	+ 	'<input type="hidden" name="YEAR" value=""/>'
			    	+ 	'<input type="hidden" name="SMT" value=""/>'
			    	+ 	'<input type="hidden" name="SMT_NM" value=""/>'
			    	+ 	'<input type="hidden" name="CDT_NUM" value=""/>'
			    	+ 	'<input type="hidden" name="WTIME_NUM" value=""/>'
			    	+ 	'<input type="hidden" name="PTIME_NUM" value=""/>'
			    	+ 	'<input type="hidden" name="CHG_FG" value="C"/>'
			    	+ 	'<input type="hidden" name="CHG_RESN" value="'+(sbjt.CHG_RESN === undefined ? '' : sbjt.CHG_RESN)+'"/>'
		            +   '<input type="hidden" name="CHG_ORG_CODE" value="'+sbjt.ORG_CODE+'"/>'
		            +   '<input type="hidden" name="CHG_COLG_CD" value="'+sbjt.COLG_CD+'"/>'
		            +   '<input type="hidden" name="CHG_COLG_NM" value="'+sbjt.COLG_NM+'"/>'
		            +   '<input type="hidden" name="CHG_DEPT_CD" value="'+sbjt.DEPT_CD+'"/>'
		            +   '<input type="hidden" name="CHG_DEPT_NM" value="'+sbjt.DEPT_NM+'"/>'
		            +   '<input type="hidden" name="CHG_COLG_ENM" value="'+(sbjt.COLG_ENM == undefined ? '' : sbjt.COLG_ENM)+'"/>'
		            +   '<input type="hidden" name="CHG_MAJOR_CD" value="'+sbjt.MAJOR_CD+'"/>'
		            +   '<input type="hidden" name="CHG_MAJOR_NM" value="'+sbjt.MAJOR_NM+'"/>'
		            +   '<input type="hidden" name="CHG_MAJOR_ENM" value="'+(sbjt.MAJOR_ENM == undefined ? '' : sbjt.MAJOR_ENM)+'"/>'
		            +   '<input type="hidden" name="CHG_COMDIV_CODE" value="'+sbjt.COMDIV_CODE+'"/>'
		            +   '<input type="hidden" name="CHG_COMDIV_CODE_NM" value="'+sbjt.COMDIV_CODE_NM+'"/>'
		            +   '<input type="hidden" name="CHG_ORG_COMDIV_CODE" value="'+sbjt.ORG_COMDIV_CODE+'"/>'
		            +   '<input type="hidden" name="CHG_SUBJECT_CD" value="'+sbjt.SUBJECT_CD+'"/>'
		            +   '<input type="hidden" name="CHG_SUBJECT_NM" value="'+sbjt.SUBJECT_NM+'"/>'
		            +   '<input type="hidden" name="CHG_SUBJECT_ENM" value="'+(sbjt.SUBJECT_ENM == undefined ? '' : sbjt.SUBJECT_ENM)+'"/>'
		            +   '<input type="hidden" name="CHG_COM_GRADE" value="'+sbjt.COM_GRADE+'"/>'
		            +   '<input type="hidden" name="CHG_COM_GRADE_NM" value="'+sbjt.COM_GRADE_NM+'"/>'
		            +   '<input type="hidden" name="CHG_YEAR" value="'+sbjt.YEAR+'"/>'
		            +   '<input type="hidden" name="CHG_SMT" value="'+sbjt.SMT+'"/>'
		            +   '<input type="hidden" name="CHG_SMT_NM" value="'+sbjt.SMT_NM+'"/>'
		            +   '<input type="hidden" name="CHG_CDT_NUM" value="'+sbjt.CDT_NUM+'"/>'
		            +   '<input type="hidden" name="CHG_WTIME_NUM" value="'+sbjt.WTIME_NUM+'"/>'
		            +   '<input type="hidden" name="CHG_PTIME_NUM" value="'+sbjt.PTIME_NUM+'"/>'
		            +   '</div>';
		});

		$('#containInput').append(contains);	
		
		chgSbjtDisplay();			//교육과정표		
		chgSbjtDiffDisplay();		//교육과정 신구대비표
		
		$("#enrollSubj").modal("hide");	
		
	    // 변경사유 입력할때마다의 이벤트 핸들러
        $('#sbjtListPc').on('input', '#CHG_RESN', function () {
           var row = $(this).closest('tr');
           var keyDocId = row.find('input[name="keyDocId"]').val();
           var chgResnValue = $(this).val();
           
           // hidden input 값을 다시 배열로 변환
    	   var jsonArray = [];
    	   getCommonJsonArray(jsonArray)

           var sbjt = jsonArray.find(function (item) {
               return item.CHG_DOC_ID === keyDocId;
           });

           sbjt.CHG_RESN = chgResnValue;

           // JSP의 hidden input 값 업데이트
           var hiddenDiv = $('.SBJT_LIST_' + keyDocId);
           hiddenDiv.find('input[name="CHG_RESN"]').val(chgResnValue);

        });
				
	}
	// 전공병합
	function rowSpanFnc(e){
		$(e).each(function(){
			var cd = $(this).attr("aria-mjCd");
			var row = $(e+"[aria-mjCd='"+cd+"']");
			
			if(row.length > 1){
				row.eq(0).attr("rowspan", row.length);
				row.not(":eq(0)").remove();
			}
		});
	}
	
</script>



<script>
// ==========================================================================================================교수 검색=================================================================
				
	
	// 교수 검색 버튼
	function profAddBtn(e){
		$('#profResult').css('display', 'none'); //검색결과 라인
		$('#profList').empty(); // 검색 목록
		$('#profName').val(""); // 검색어
		$('#profDept').val(""); // 학과
		$('#profCnt').text(0); // 검색 갯수
		$('#checkAll').prop('checked', false); // 체크 목록
		$("#profList").attr("aria-fg", e);
		
		if(e == 1){
			// 지도교수
			$('#addProfLabel').text('지도교수님 검색')
			$('#divProf').text('지도교수님 정보검색')
			$('#divBtn').attr('onclick', 'proAdd(1);')
			$("#prof_1").css("display", "");
			$("#prof_2").css("display", "none");
			$("#profColWidth").attr("width", "50px");
		}else{
			// 컨설팅 교수
			$('#addProfLabel').text('상담교수님 검색')
			$('#divProf').text('상담교수님 정보검색')
			$('#divBtn').attr('onclick', 'proAdd(2);')
			$("#prof_1").css("display", "none");
			$("#prof_2").css("display", "");
			$("#profColWidth").attr("width", "33px");
		}
		
		$.ajax({
			url : '/web/studPlan/getProfDeptList.do?mId=36',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'GET', 
			data: { 
				univ : ""
			},
			success:function(data){
				var items = data.DEPART_LIST;
				var html = '<option value="">학과 전체</option>';
				
				for(var i=0; i < items.length; i++){
					html += '<option value="'+items[i].DEPT_CD+'">'+items[i].DEPT_NM+'</option>'
				}
				$('#profDept').html(html);
				
				
				$('#addProf').modal('show');		
			}
		});
		
		
	}
	
	function proDelBtn(){
		if(confirm('입력하신 상담 교수를 초기화 하시겠습니까?')){
			$("#proConNm").val("");
			$("#proConCd").val("");
		}
	}
	
	$(function(){
		// 교수 검색(엔터)
		$('#profName').on('keyup', function(key){
			if(key.keyCode==13) {
				proSrcList();
			}						
		})
		
		// 전체 선택 click
		$('#checkAll').on('click', function(){
			if($('#checkAll').is(':checked')){
				$('input[name=staffCd]').prop('checked', true);
			}else{
				$('input[name=staffCd]').prop('checked', false);
			}
		});
		
		// 선택 click
		$(document).on('click', 'input[name=staffCd]', function(){
			$('#checkAll').prop('checked', false);
		});
	})
	
	
	function staffChk(e){
		var proList = $("#proList").attr("aria-fg");
		if(proList == 1){
			$('input[name=staffCd]').prop('checked', false);
			$("#"+e).prop("checked", true);
		}
	}
	
	// 교수 검색 목록
	function proSrcList(){
		$('#checkAll').prop('checked', false); // 체크 목록
		if($('#proName').val() == ""){
			alert("이름을 입력해주세요");
			return false;
		}else{
			showLoading();
			$.ajax({
				url : '/web/studPlan/profList.do?mId=36',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'POST', 
				data: JSON.stringify({ 
					keyWord : $('#profName').val(),
					deptCd : $('#profDept option:selected').val()
				}),
				success:function(data){
					var html = "";
					var items = data.PROF_LIST;
					for(var i=0; i < items.length; i++){
						html += '<tr>'
							 +		'<td class="text-center">'
							 + 			'<label class="blind">체크박스</label>'
							 +			'<input name="staffCd" type="checkbox" class="form-check-input" onclick="staffChk(\''+items[i].EMP_NO+'\')" id="'+items[i].EMP_NO+'" value="'+items[i].EMP_NO+'"/ data-name="'+items[i].EMP_NM+'" data-dept_name="'+items[i].DEPT_NM+'">'
							 +		'</td>'
							 +		'<td class="text-center">'+items[i].EMP_NM+'</td>'
							 +		'<td class="text-center">'+items[i].DEPT_NM+'</td>'
							 +	'</tr>'
					}
					$('#profCnt').text(items.length);
					$('#profList').html(html);
					$('#profResult').css('display', 'block');
					hideLoading();	
				}
			})		
			
		}
	}
	
	// 교수 선택 추가
	function proAdd(e){
		var checkArr = new Array();
		var nameArr = new Array();
		$('input[name=staffCd]:checked').each(function(){
			var check = $(this).val();
			var text = $(this).data('name');
			
			checkArr.push(check);
			nameArr.push(text);
		})
		
		if(e == 1){ 
			//지도교수
			if(checkArr.length > 1){
				alert("승인할 지도교수님은 한분만 선택 가능합니다.");
				return false;
			}else{
				$('#proNm').val(nameArr);
				$('#proCd').val(checkArr);
				$('#addProf').modal('hide');
			}
		}else{
			// 컨설팅 교수
			if($('#proConNm').val() == ''){
				$('#proConNm').val(nameArr);			
				$('#proConCd').val(checkArr);
				$('#addProf').modal('hide');
			}else{
				var oldNm = $('#proConNm').val().split(",");
				var oldCd = $('#proConCd').val().split(",");
				
// 				중복 체크
				for(var i=0; i < oldCd.length; i++){
					for(var j=0; j < checkArr.length; j++){
						if(oldCd[i] == checkArr[j]){
							alert("중복으로 선택된 교수님이  있습니다.")
							return false;
						}
					}
					
				}
				$('#proConNm').val(oldNm+','+nameArr);			
				$('#proConCd').val(oldCd+','+checkArr);
				$('#addProf').modal('hide')
			}
		}
	}
	
	

	//로딩 바	
	function showLoading() {
		const loader = document.querySelector('.loader');
		const overlay = document.getElementById('overlay');
		loader.style.display = 'block';	
		overlay.style.display = 'block';
		
	}	
	//로딩바 hide	
	function hideLoading() {
		const loader = document.querySelector('.loader');
		const overlay = document.getElementById('overlay');
		loader.style.display = 'none';
		overlay.style.display = 'none';
		
	}	
	
	//찜 목록
	function getBookmarkList(){
		return new Promise((resolve, reject) => {		
			$.ajax({
				url: '/web/bookmark/getBookmarkList.do?mId=37',
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    menuFg : 'sbjt'
				}),
				success: function(data){			
					resolve(data.bookmarkList);
				},error:function() {
					reject("");
				}
			});
		})
		
	}
	
	//찜 변경
	function likeChange(docId, type){
			if(!$("#"+docId).hasClass("on_red")){			
				$.ajax({
					url: '/web/bookmark/insertBookmark.do?mId=37&menuFg='+type,
					contentType:'application/json',	
					type: 'POST',
					data: JSON.stringify({ 
					    docId : docId
					}),
					success: function(data){
						$("#"+docId).addClass("on_red");
					}
				});
			} else {
				$.ajax({
					url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg='+type,
					contentType:'application/json',	
					type: 'POST',
					data: JSON.stringify({ 
					    docId : docId
					}),
					success: function(data){
						$("#"+docId).removeClass("on_red");
					}
				});
			}
	}	
	
	//교과목 테이블 렌더링
	function renderSbjtTable(g, grade, chgFg){
		
		if(g != null){			
			
			// Html form
			var essHtml = "";		//전공필수
			var selHtml = "";		//전공선택
			var basHtml = "";		//전공기초
			var genEssHtml = "";	//교양필수
			var genSelHtml = "";	//교양선택
			var teachHtml = "";		//교직			
			
			// 구분 rowspan
			var essRowSpan = 0;
			var selRowSpan = 0;
			var basRowSpan = 0;
			var genEssRowSpan = 0;
			var genSelRowSpan = 0;
			var teachRowSpan = 0;			
			
			for(var i=0; i < g.length; i++){
				switch (g[i][chgFg + 'COMDIV_CODE']) {
				    case 'UE010021': essRowSpan++; break;
				    case 'UE010022': selRowSpan++; break;
				    case 'UE010024': basRowSpan++; break;
				    case 'UE010011': genEssRowSpan++; break;
				    case 'UE010012': genSelRowSpan++; break;
				    case 'UE010031': teachRowSpan++; break;
				}
			}
			
		    basHtml    = processSbjtHtml(g, 'UE010021', '전공필수', essRowSpan, chgFg);
		    essHtml    = processSbjtHtml(g, 'UE010022', '전공선택', selRowSpan, chgFg);
		    selHtml    = processSbjtHtml(g, 'UE010024', '전공기초', basRowSpan, chgFg);
		    genEssHtml = processSbjtHtml(g, 'UE010011', '교양필수', genEssRowSpan, chgFg);
		    genSelHtml = processSbjtHtml(g, 'UE010012', '교양선택', genSelRowSpan, chgFg);
		    teachHtml  = processSbjtHtml(g, 'UE010031', '교직', teachRowSpan, chgFg);		    

		    var html = basHtml + essHtml + selHtml + genEssHtml + genSelHtml + teachHtml;
			
			$("#grade"+grade).empty();
			$("#grade"+grade).append(html);
			
			// 전공병합
			rowSpanFnc(".UE010021_"+grade);
			rowSpanFnc(".UE010022_"+grade);
			rowSpanFnc(".UE010024_"+grade);
			rowSpanFnc(".UE010011_"+grade);
			rowSpanFnc(".UE010012_"+grade);
			rowSpanFnc(".UE010031_"+grade);			

		}else{
			$("#grade"+grade).empty();
		}		
	}	
	
	//교과목 공통부분 html
	function processSbjtHtml(subjects, groupCode, groupName, rowspan, chgFg) {
	    var html = '';
	    var totalPnt = 0;
	    var beforePnt = 0;
	    var afterPnt = 0;
	    var thTagFg = 0;
	    
	    
	    for (var i = 0; i < subjects.length; i++) {
	        if (subjects[i][chgFg+'COMDIV_CODE'] == groupCode) {
	        	totalPnt = 0;
	            html += '<tr>';
	            if (thTagFg == 0) {
	                html += '<th scope="rowgroup" rowspan="' + rowspan + '" class="border-end">' + groupName + '</th>';
	                thTagFg++;
	            }
	            html += '<td class="border-end '+groupCode+'_'+ subjects[i][chgFg+'COM_GRADE'] +'" aria-mjCd="' + subjects[i][chgFg+'MAJOR_CD'] + '">' + subjects[i][chgFg+'MAJOR_NM'] + '</td>';
	            html += '<td class="border-end">' + subjects[i][chgFg+'SUBJECT_NM'] + '</td>';

	            // 학점
	            if (subjects[i][chgFg+'SMT'] == 'GH0210') { // 1학기
	                html += '<td class="border-end">' + subjects[i][chgFg+'CDT_NUM']  + '</td>';
	                html += '<td></td>';
	                beforePnt += Number(subjects[i][chgFg+'CDT_NUM']);
	            } else if (subjects[i][chgFg+'SMT'] == 'GH0220') { // 2학기
	                html += '<td class="border-end"></td>';
	                html += '<td>' + subjects[i][chgFg+'CDT_NUM'] + '</td>';
	                afterPnt += Number(subjects[i][chgFg+'CDT_NUM']);
	            }
	            
	            html += '</tr>';
	        }
	    }
	    
	    // 소계
	    if (html.length > 0) {
	        html += '<tr class="table_sum">';
	        html += '<td colspan="3" class="border-end">소계</td>';
	        html += '<td class="border-end tdPnt">' + beforePnt + '</td>';
	        html += '<td class="tdPnt">' + afterPnt + '</td>';
	        html += '</tr>';
	    }

	    return html;
	}	

	function getInputDataHasName(json_data){
		
		//교과목용 배열 필드
        const arrayFields = [
        	'CHG_DOC_ID', 'CHG_ORG_CODE',
            'ORG_CODE', 'COLG_CD', 'COLG_NM', 'DEPT_CD', 'DEPT_NM', 'MAJOR_CD', 'MAJOR_NM', 
            'COMDIV_CODE', 'COMDIV_CODE_NM', 'ORG_COMDIV_CODE', 'SUBJECT_CD', 
            'SUBJECT_NM', 'SUBJECT_ENM', 'COM_GRADE', 'COM_GRADE_NM', 
            'YEAR', 'SMT', 'SMT_NM', 'CDT_NUM', 'WTIME_NUM', 'PTIME_NUM',
            'CHG_FG','CHG_RESN',
            'CHG_COLG_CD', 'CHG_COLG_NM', 'CHG_DEPT_CD', 'CHG_DEPT_NM', 'CHG_MAJOR_CD', 'CHG_MAJOR_NM', 
            'CHG_COMDIV_CODE', 'CHG_COMDIV_CODE_NM', 'CHG_ORG_COMDIV_CODE', 'CHG_SUBJECT_CD', 
            'CHG_SUBJECT_NM', 'CHG_SUBJECT_ENM', 'CHG_COM_GRADE', 'CHG_COM_GRADE_NM', 
            'CHG_YEAR', 'CHG_SMT', 'CHG_SMT_NM', 'CHG_CDT_NUM', 'CHG_WTIME_NUM', 'CHG_PTIME_NUM'
        ];

        $('#frmPost').find('input').each(function() {
            let name = $(this).attr('name');
            let value = $(this).val();
            
            // 이름이 존재하는 input만 추가 (소문자는 제외)
            if (name && !/[a-z]/.test(name)) {
            	//교과목용 필드는 배열로 설정
                if (arrayFields.includes(name)) {
                    if (!json_data[name]) {
                        json_data[name] = [];
                    }
                    json_data[name].push(value);
                } else {
                    json_data[name] = value;
                }
            }
        });

        $('#frmPost').find('textarea').each(function() {
            let name = $(this).attr('name');
            let value = $(this).val();

            if (name && !/[a-z]/.test(name)) {
                if (json_data[name]) {
                    if (!Array.isArray(json_data[name])) {
                        json_data[name] = [json_data[name]];
                    }
                    json_data[name].push(value);
                } else {
                    json_data[name] = value;
                }
            }
        });
        
        $('#frmPost').find('select').each(function() {
            let name = $(this).attr('name');
            let value = $(this).val();

            if (name && !/[a-z]/.test(name)) {
                if (arrayFields.includes(name)) {
                    if (!json_data[name]) {
                        json_data[name] = [];
                    }
                    json_data[name].push(value);
                } else {
                    json_data[name] = value;
                }
            }
        });        
        
	}

	//기본 validationCheck(임시저장용으로 주로 쓸듯)
	function basicValidCheck(){
		var sdmKorNm = $("input[name=SDM_CHG_RESN]").val();		
		if(sdmKorNm == ''){
			alert('변경사유를 입력해주세요');
			return false;
		}
		
		return true;
	}
	
	//상세 validationCheck(상담신청과 신청서제출 시  체크)
	function detailValidCheck(){
		
		
		
		if($('#desTotCnt').val() < 44){
			alert("45학점 이상 설계 가능합니다.");
			return false;
		}
		var returnNow  = false;
		$('input[name=valiPnt]').each(function(){

			if($(this).val() < 8){
				
				returnNow = true;
				return false;
			}
		});
		
		if(returnNow){
			alert("전공별 최소 9학점 이상 설계 가능합니다.");
			return false;
		}
		
		if(Number($("#totalPnt").val()) > 165){
			alert('선택된 설계학점 총점이 '+$("#totalPnt").val()+'점으로 너무 높습니다. (최대: 165점)');
			return false;
		}
		
	   
	    var invalidDiv = false;
	    $('#containInput div').each(function(){
	        var chgFg = $(this).find('input[name=CHG_FG]').val();
	        var chgResn = $(this).find('input[name=CHG_RESN]').val();
	        
	        if(chgFg !== 'N' && (!chgResn || chgResn.trim() === '')){
	            alert('변경된 모든 교과목에 대해 변경사유를 입력하셔야 합니다.');
	            invalidDiv = true;
	            return false; // break the each loop
	        }
	    });

	    if(invalidDiv){
	        return false;
	    }
		
		return true;
	}
	
	/*학생설계전공 임시저장(수정)*/	
	function chgModifyTempSave(){
// 		if(!basicValidCheck()) return false;
		showLoading();
        let json_data = {};
        getInputDataHasName(json_data);
		$.ajax({
			url: '/web/studPlan/chgModifyTempSave.do?mId=36',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'POST', 
			data: JSON.stringify(json_data),
			success:function(data){			
				hideLoading();
				alert(data.RTN_MSG);
				if(data.RESULT == "DONE"){					
					/*수정화면으로 이동*/
					var form = document.frmView;
					$("#SDM_CD").val(json_data.SDM_CD);
					$("#REVSN_NO").val(json_data.REVSN_NO);
// 					form.action = '/web/studPlan/stdChgModify.do';
					form.action = '/web/studPlan/list.do?mId=36';
					form.submit();
				}else{
					console.log(JSON.stringify(data.RESULT));
				}
			}
		});	
	}
	
	/*학생설계전공 상담신청(수정)*/
	function chgModifyReqCnslt(){
		if(!basicValidCheck()) return false;
// 		if (!detailValidCheck()) return false; //이동근 임시주석
		
		if($('input[name=CNSLT_PROF]').val() == ''){
			alert("상담교수를 선택해주세요.");
			return false;
		}
		
        if(confirm("해당 학생설계전공에 대한 상담을 신청하시겠습니까?")){
        	showLoading();
            let json_data = {};
            getInputDataHasName(json_data);
        	
			$.ajax({
				url: '/web/studPlan/chgModifyReqCnslt.do?mId=36',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'POST', 
				data: JSON.stringify(json_data),
				success:function(data){		
					hideLoading();
					alert(data.RTN_MSG);
					if(data.RESULT == "DONE"){
						/*상세보기화면으로 이동*/
						var form = document.frmView;
						$("#SDM_CD").val(json_data.SDM_CD);
						$("#REVSN_NO").val(json_data.REVSN_NO);
// 						form.action = '/web/studPlan/stdMyChgView.do';
						form.action = '/web/studPlan/list.do?mId=36';
						form.submit();
					}else{
						console.log(JSON.stringify(data.RESULT));
					}
				}
			});	
        }
	}
	
	/*학생설계전공 신청서제출(수정)*/
	function chgModifySubmit(){
		if(!basicValidCheck()) return false;
// 		if (!detailValidCheck()) return false;	//이동근 임시주석
		
		if($('input[name=GUID_PROF_STAFF_NO]').val() == ''){
			alert("지도교수를 선택해주세요.");
			return false;
		}
		
		if(confirm("해당 학생설계전공 신청서를 제출하시겠습니까?")){
			showLoading();
	        let json_data = {};
	        getInputDataHasName(json_data);
	        
			$.ajax({
				url: '/web/studPlan/chgModifySubmit.do?mId=36',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'POST', 
				data: JSON.stringify(json_data),
				success:function(data){		
					hideLoading();
					alert(data.RTN_MSG);
					if(data.RESULT == "DONE"){
						/*상세보기화면으로 이동*/
						var form = document.frmView;
						$("#SDM_CD").val(json_data.SDM_CD);
						$("#REVSN_NO").val(json_data.REVSN_NO);
// 						form.action = '/web/studPlan/stdMyChgView.do';
						form.action = '/web/studPlan/list.do?mId=36';
						form.submit();
					}else{
						console.log(JSON.stringify(data.RESULT));
					}
				}
			});				
		}
	}
		
	//저장된 변경 교과목 목록 hidden에 추가
	function setSavedChgSbjtToHidden(data){
        const container = $('#containInput');
        container.empty(); // 기존 내용을 초기화
        data.forEach(function (sbjtList) {
        	
            const sbjtDiv = $('<div>', { class: 'SBJT_LIST_'+sbjtList['CHG_DOC_ID'], id: (sbjtList.CHG_FG == "C" ? "C" : "origin")   });

            const fields = [
            	'CHG_DOC_ID', 'CHG_ORG_CODE',
                'ORG_CODE', 'COLG_CD', 'COLG_NM', 'DEPT_CD', 'DEPT_NM', 'MAJOR_CD', 'MAJOR_NM', 
                'COMDIV_CODE', 'COMDIV_CODE_NM', 'ORG_COMDIV_CODE', 'SUBJECT_CD', 
                'SUBJECT_NM', 'SUBJECT_ENM', 'COM_GRADE', 'COM_GRADE_NM', 
                'YEAR', 'SMT', 'SMT_NM', 'CDT_NUM', 'WTIME_NUM', 'PTIME_NUM',
                'CHG_FG','CHG_RESN',
                'CHG_COLG_CD', 'CHG_COLG_NM', 'CHG_DEPT_CD', 'CHG_DEPT_NM', 'CHG_MAJOR_CD', 'CHG_MAJOR_NM', 
                'CHG_COMDIV_CODE', 'CHG_COMDIV_CODE_NM', 'CHG_ORG_COMDIV_CODE', 'CHG_SUBJECT_CD', 
                'CHG_SUBJECT_NM', 'CHG_SUBJECT_ENM', 'CHG_COM_GRADE', 'CHG_COM_GRADE_NM', 
                'CHG_YEAR', 'CHG_SMT', 'CHG_SMT_NM', 'CHG_CDT_NUM', 'CHG_WTIME_NUM', 'CHG_PTIME_NUM'

            ];
            
            fields.forEach(function (field) {
                sbjtDiv.append($('<input>', {
                    type: 'hidden',
                    name: field,
                    value: sbjtList[field]
                }));
            });

            container.append(sbjtDiv);
        });
	}
	
	//저장된 변경 교과목 목록 중 CHG_FG가 C(추가)인것만 display
	function setSavedChgSbjtToModal(){		
		
		var html = "";
		var majorArray = ['UE010021', 'UE010022', 'UE010024'];
		var genArray = ['UE010011', 'UE010012'];
   	    var data = [];
   	    getCommonJsonArray(data);    
		for(var i=0; i < data.length; i++){
			if(data[i].CHG_FG === 'C'){
				var sbjtFgHtml = '';
				if(majorArray.includes(data[i].CHG_COMDIV_CODE)){
				    sbjtFgHtml = '<li>'+
				                 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
				                 '<option value="UE010021"'+ (data[i].CHG_COMDIV_CODE == "UE010021" ? 'selected' : '') +'>전공필수</option>'+
				                 '<option value="UE010022"'+ (data[i].CHG_COMDIV_CODE == "UE010022" ? 'selected' : '') +'>전공선택</option>'+
				                 '<option value="UE010024"'+ (data[i].CHG_COMDIV_CODE == "UE010024" ? 'selected' : '') +'>전공기초</option>'+
				                 '</select>'+
				                 '</li>';                
				}else if(genArray.includes(data[i].CHG_COMDIV_CODE)){
				    sbjtFgHtml = '<li>'+
				                 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
				                 '<option value="UE010011"'+ (data[i].CHG_COMDIV_CODE == "UE010011" ? 'selected' : '') +'>교양필수</option>'+
				                 '<option value="UE010012"'+ (data[i].CHG_COMDIV_CODE == "UE010012" ? 'selected' : '') +'>교양선택</option>'+
				                 '</select>'+
				                 '</li>';    
				}else{
					sbjtFgHtml = '<li>'+
	                 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
	                 '<option value="UE010031"'+ (data[i].CHG_COMDIV_CODE == "UE010031" ? 'selected' : '') +'>교직</option>'+			                 				                 
	                 '</select>'+
	                 '</li>';     
				}
	
				html += '<div class="item p-3 head-after" id="headAfter-'+data[i].CHG_DOC_ID+'">';
				html +=    '<input type="hidden" name="sdmAddList" aria-YEAR="'+data[i].CHG_YEAR+'" aria-SMT="'+data[i].CHG_SMT+'" aria-SMT_NM="'+data[i].CHG_SMT_NM+'" aria-SUBJECT_CD="'+data[i].CHG_SUBJECT_CD+'" aria-MAJOR_CD="'+data[i].CHG_MAJOR_CD+'" aria-MAJOR_NM="'+data[i].CHG_MAJOR_NM+'" aria-COM_GRADE="'+data[i].CHG_COM_GRADE+'" aria-COM_GRADE_NM="'+data[i].CHG_COM_GRADE_NM+'" aria-COLG_CD="'+data[i].CHG_COLG_CD+'" aria-COLG_NM="'+data[i].CHG_COLG_NM+'" aria-DEPT_CD="'+data[i].CHG_DEPT_CD+'" aria-DEPT_NM="'+data[i].CHG_DEPT_NM+'" aria-COMDIV_CODE="'+data[i].CHG_COMDIV_CODE+'" aria-COMDIV_CODE_NM="'+data[i].CHG_COMDIV_CODE_NM+'" aria-SUBJECT_NM="'+data[i].CHG_SUBJECT_NM+'" aria-SUBJECT_ENM="'+data[i].CHG_SUBJECT_ENM+'" aria-CDT_NUM="'+data[i].CHG_CDT_NUM+'" aria-WTIME_NUM="'+data[i].CHG_WTIME_NUM+'" aria-PTIME_NUM="'+data[i].CHG_PTIME_NUM+'" aria-ORG_CODE="'+data[i].CHG_ORG_CODE+'" aria-ORG_COMDIV_CODE="'+data[i].CHG_COMDIV_CODE+'" aria-CHG_RESN="'+(data[i].CHG_RESN == undefined ? '' : data[i].CHG_RESN)+'"/>';
				html +=    '<div class="d-inline-flex form-check">';
				html +=        '<label class="blind" for="lessons_'+data[i].CHG_DOC_ID+'">선택</label>';
				html +=        '<input type="checkbox" id="lessons_'+data[i].CHG_DOC_ID+'" class="form-check-input chk-after">';
				html +=    '</div>';
				html +=    '<section class="d-inline-flex flex-column flex-lg-row justify-content-between">';
				html +=        '<div class="d-flex flex-column align-items-start div-after">';
				html +=            '<ul class="cate d-flex flex-wrap gap-2">';
				html +=                sbjtFgHtml;
				html +=                '<li class="border">';
				html +=                    '<span>'+data[i].CHG_MAJOR_NM+'</span>';
				html +=                '</li>';
				html +=            '</ul>';
				html +=            '<h4 class="text-truncate ellip_2 my-2 h-sbjt-kor-nm" id="sbjtNm-'+data[i].CHG_DOC_ID+'">'+data[i].CHG_SUBJECT_NM+'</h4>';
				html +=            '<div class="d-flex flex-row w-100 mb-1">';
				html +=                '<strong class="d-block me-2">['+(data[i].CHG_ORG_CODE == 'EXTERNAL' ? '타 대학/기관/산업체' : data[i].CHG_SUBJECT_CD) +']</strong>';
				html +=                '<span class="d-block mb-0 text-truncate text-dark text-opacity-75">'+data[i].CHG_SUBJECT_ENM+'</span>';
				html +=            '</div>';
				html +=            '<div class="w-100">';
				html +=                '<dl class="d-inline-flex position-relative pe-4">';
				html +=                    '<dt class="me-2">편성</dt>';
				html +=                    '<dd class="text-dark text-opacity-75">'+data[i].CHG_YEAR+'학년도 '+data[i].CHG_COM_GRADE+'학년 '+data[i].CHG_SMT_NM+'</dd>';
				html +=                '</dl>';
				html +=                '<dl class="d-inline-flex">';
				html +=                    '<dt class="me-2">학점</dt>';
				html +=                    '<dd class="text-dark text-opacity-75">'+(data[i].CHG_CDT_NUM === '' ? 0 : data[i].CHG_CDT_NUM)+'-'+(data[i].CHG_WTIME_NUM === '' ? 0 : data[i].CHG_WTIME_NUM)+'-'+(data[i].CHG_PTIME_NUM === '' ? 0 : data[i].CHG_PTIME_NUM)+'</dd>';
				html +=                '</dl>';
				html +=            '</div>';
				html +=        '</div>';
				html +=        '<ol class="d-inline-flex btn_wrap">';
				html +=            '<li>';
				html +=                '<button type="button" class="border-0 bg-transparent btn_dele" onclick="delSbjt(\''+data[i].CHG_DOC_ID+'\')">';
				html +=                    '<span class="text-center ps-1 d-inline-block d-lg-none text-white">삭제</span>';
				html +=                '</button>';
				html +=            '</li>';
				html +=        '</ol>';
				html +=    '</section>';
				html += '</div>';
				
				
			}
			duplSbjtArr.push(data[i].CHG_DOC_ID);				
		}
		$("#appndSbjtChoice").append(html);
		$("#no-data-img").remove();
		
	}
	
	// 교과목 display(변경전)
	function beforeSbjtDisplay(data){
	    var grades = {
	        g1: [],
	        g2: [],
	        g3: [],
	        g4: []
	    };
		
		
	    data.forEach(function(sbjt) {
	        switch (sbjt.COM_GRADE) {
	            case "1": grades.g1.push(sbjt);
	                      break;
	            case "2": grades.g2.push(sbjt);
	                      break;
	            case "3": grades.g3.push(sbjt);
	                      break;
	            case "4": grades.g4.push(sbjt);
	                      break;
	        }
	    });				
		
	    
	    // 각 학년별 렌더링
	    for (var i = 1; i <= 4; i++) {
	        renderSbjtTable(grades['g' + i], i.toString(), '');
	    }

		// 설계선택 목록의 유사도 JSON Data
		var ﻿﻿sumPntArray = new Array();
		
		// 교내
		data.forEach(function(sbjt) {	
			var obj = new Object();					
			obj.SUBJECT_CD 			= sbjt.SUBJECT_CD;
			obj.SUBJECT_NM 			= sbjt.SUBJECT_NM;
			obj.SUBJECT_ENM 		= sbjt.SUBJECT_ENM;
			obj.ORG_CODE 			= sbjt.ORG_CODE;		
			obj.COLG_CD 			= sbjt.COLG_CD;
			obj.COLG_NM 			= sbjt.COLG_NM;
			obj.DEPT_CD 			= sbjt.DEPT_CD;
			obj.DEPT_NM 			= sbjt.DEPT_NM;
			obj.MAJOR_CD 			= sbjt.MAJOR_CD;
			obj.MAJOR_NM 			= sbjt.MAJOR_NM;
			obj.YEAR 				= sbjt.YEAR;
			obj.COM_GRADE 			= sbjt.COM_GRADE;
			obj.COM_GRADE_NM 		= sbjt.COM_GRADE_NM;
			obj.SMT 				= sbjt.SMT;
			obj.SMT_NM 				= sbjt.SMT_NM;
			obj.WTIME_NUM 			= sbjt.WTIME_NUM;
			obj.PTIME_NUM	 		= sbjt.PTIME_NUM;
			obj.CDT_NUM 			= sbjt.CDT_NUM;
			obj.COMDIV_CODE 		= sbjt.COMDIV_CODE;
			obj.COMDIV_CODE_NM 		= sbjt.COMDIV_CODE_NM;
			obj.ORG_COMDIV_CODE		= sbjt.ORG_COMDIV_CODE;
			obj.MNRCOM_DIV_CODE 	= sbjt.MNRCOM_DIV_CODE;
			obj.MNRCOM_DIV_CODE_NM 	= sbjt.MNRCOM_DIV_CODE_NM;

			sbjtArray.push(sbjt);

			var pntobj = new Object();

			pntobj.COLG_NM = sbjt.COLG_NM;
			pntobj.MAJOR_NM = sbjt.MAJOR_NM;

			var comGrade = sbjt.COM_GRADE;
			var tempPnt = sbjt.CDT_NUM;
			pntobj.CDT_NUM = Number(tempPnt);

			sumPntArray.push(pntobj);

		});				
		
		
		var finalSumPntList = new Array();
		var mjNmArr = [];
		var pntArr = [];
		
		for(var i=0; i < sumPntArray.length; i++){
			var obj = new Object();

			if(mjNmArr.indexOf(sumPntArray[i].MAJOR_NM) == -1){
				mjNmArr.push(sumPntArray[i].MAJOR_NM);
				pntArr.push(Number(sumPntArray[i].CDT_NUM));

				obj.ORG_CODE = sumPntArray[i].ORG_CODE;
				obj.COLG_NM = sumPntArray[i].COLG_NM;
				obj.MAJOR_NM = sumPntArray[i].MAJOR_NM;
				obj.CDT_NUM = sumPntArray[i].CDT_NUM;
				
				finalSumPntList.push(obj);
			}else{
				var idx = mjNmArr.indexOf(sumPntArray[i].MAJOR_NM);
				pntArr[idx] = Number(pntArr[idx]) + Number(sumPntArray[i].CDT_NUM);
			}
		}
		
		for(var i=0; i < finalSumPntList.length; i++){
			finalSumPntList[i].CDT_NUM = pntArr[i];
		}

		/* 학점순으로 정렬 (내림차순) */
		var fieldNm = "pnt";
		finalSumPntList.sort(function(a, b){
		    return b[fieldNm] - a[fieldNm];
		});

		// 설계학점 총계 출력
		var pHtml = '';
		var tempNum = '';
		var colgNm = '';
		for(var i=0; i < finalSumPntList.length; i++){
			if(i < 9){
				tempNum = '0'+(i+1);
			}else{
				tempNum = i+1;
			}
			
			if(finalSumPntList[i].ORG_CODE == 'EXTERNAL'){
				colgNm = '타 대학/기관/산업체';
			}else{
				colgNm = '국립한국해양대학교';
			}
						
			pHtml += '<tr>';
			pHtml += 	'<td class="numb">'+tempNum+'</td>';
			pHtml += 	'<td class="univ">'+colgNm+'</td>';
			pHtml += 	'<td class="coll">'+finalSumPntList[i].COLG_NM+'</td>';
			pHtml += 	'<td class="majo">'+finalSumPntList[i].MAJOR_NM+'</td>';
			pHtml += 	'<td class="scor">'+finalSumPntList[i].CDT_NUM+'학점<input type="hidden" name="valiPnt" value="'+finalSumPntList[i].CDT_NUM+'"/></td>';
			pHtml += '</tr>';
		}

		$("#appndSumPnt").empty();
		$("#appndSumPnt").append(pHtml);
		
		$("#appndSumPnt").empty();
		$("#appndSumPnt").append(pHtml);
		
		var rsSumPnt = 0;
		
		$(".tdPnt").each(function(){
			rsSumPnt += Number($(this).text());
		});
		
		$(".totalPnt").text(rsSumPnt);		
	}
	
	// 교과목 display(변경)
	function chgSbjtDisplay(){
		
		//추가할때마다 동적으로 변경해야되기때문에 hidden에서 가져온다
		var jsonArray = [];
        $('#containInput [class^="SBJT_LIST"]').each(function() {
            var obj = {};
            var exclude = false;
            $(this).find('input[type="hidden"]').each(function() {
                var name = $(this).attr('name');
                var value = $(this).val();
                if (name === "CHG_FG" && value === "D") {
                    exclude = true;
                    return false; // D(삭제)일땐 제외하고 array를 구성한다
                }
                obj[name] = value;
            });
            if (!exclude) {
                jsonArray.push(obj);
            }
        });
        
	    var grades = {
	        g1: [],
	        g2: [],
	        g3: [],
	        g4: []
	    };
		
		
	    jsonArray.forEach(function(sbjt) {
	        switch (sbjt.CHG_COM_GRADE) {
	            case "1": grades.g1.push(sbjt);
	                      break;
	            case "2": grades.g2.push(sbjt);
	                      break;
	            case "3": grades.g3.push(sbjt);
	                      break;
	            case "4": grades.g4.push(sbjt);
	                      break;
	        }
	    });				
		
	    // 각 학년별 렌더링
	    for (var i = 1; i <= 4; i++) {
	        renderSbjtTable(grades['g' + i], i.toString(), 'CHG_');
	    }

		// 설계선택 목록의 유사도 JSON Data
		var ﻿﻿sumPntArray = new Array();
// 		var ﻿﻿colgMjArr = new Array();
// 		var colgNmArr = [];
		
		// 교내
		jsonArray.forEach(function(sbjt) {	
			var obj = new Object();					
			obj.SUBJECT_CD 			= sbjt.CHG_SUBJECT_CD;
			obj.SUBJECT_NM 			= sbjt.CHG_SUBJECT_NM;
			obj.SUBJECT_ENM 		= sbjt.CHG_SUBJECT_ENM;
			obj.ORG_CODE 			= sbjt.CHG_ORG_CODE;		
			obj.COLG_CD 			= sbjt.CHG_COLG_CD;
			obj.COLG_NM 			= sbjt.CHG_COLG_NM;
			obj.DEPT_CD 			= sbjt.CHG_DEPT_CD;
			obj.DEPT_NM 			= sbjt.CHG_DEPT_NM;
			obj.MAJOR_CD 			= sbjt.CHG_MAJOR_CD;
			obj.MAJOR_NM 			= sbjt.CHG_MAJOR_NM;
			obj.YEAR 				= sbjt.CHG_YEAR;
			obj.COM_GRADE 			= sbjt.CHG_COM_GRADE;
			obj.COM_GRADE_NM 		= sbjt.CHG_COM_GRADE_NM;
			obj.SMT 				= sbjt.CHG_SMT;
			obj.SMT_NM 				= sbjt.CHG_SMT_NM;
			obj.WTIME_NUM 			= sbjt.CHG_WTIME_NUM;
			obj.PTIME_NUM	 		= sbjt.CHG_PTIME_NUM;
			obj.CDT_NUM 			= sbjt.CHG_CDT_NUM;
			obj.COMDIV_CODE 		= sbjt.CHG_COMDIV_CODE;
			obj.COMDIV_CODE_NM 		= sbjt.CHG_COMDIV_CODE_NM;
			obj.ORG_COMDIV_CODE		= sbjt.CHG_ORG_COMDIV_CODE;
			obj.MNRCOM_DIV_CODE 	= sbjt.CHG_MNRCOM_DIV_CODE;
			obj.MNRCOM_DIV_CODE_NM 	= sbjt.CHG_MNRCOM_DIV_CODE_NM;

			sbjtArray.push(sbjt);

			var pntobj = new Object();

			pntobj.COLG_NM = sbjt.CHG_COLG_NM;
			pntobj.MAJOR_NM = sbjt.CHG_MAJOR_NM;

			var comGrade = sbjt.CHG_COM_GRADE;
			var tempPnt = sbjt.CHG_CDT_NUM;
			pntobj.CDT_NUM = Number(tempPnt);

			sumPntArray.push(pntobj);

		});				
		
		
		var finalSumPntList = new Array();
		var mjNmArr = [];
		var pntArr = [];
		
		for(var i=0; i < sumPntArray.length; i++){
			var obj = new Object();

			if(mjNmArr.indexOf(sumPntArray[i].MAJOR_NM) == -1){
				mjNmArr.push(sumPntArray[i].MAJOR_NM);
				pntArr.push(Number(sumPntArray[i].CDT_NUM));

				obj.ORG_CODE = sumPntArray[i].ORG_CODE;
				obj.COLG_NM = sumPntArray[i].COLG_NM;
				obj.MAJOR_NM = sumPntArray[i].MAJOR_NM;
				obj.CDT_NUM = sumPntArray[i].CDT_NUM;
				
				finalSumPntList.push(obj);
			}else{
				var idx = mjNmArr.indexOf(sumPntArray[i].MAJOR_NM);
				pntArr[idx] = Number(pntArr[idx]) + Number(sumPntArray[i].CDT_NUM);
			}
		}
		
		for(var i=0; i < finalSumPntList.length; i++){
			finalSumPntList[i].CDT_NUM = pntArr[i];
		}

		/* 학점순으로 정렬 (내림차순) */
		var fieldNm = "pnt";
		finalSumPntList.sort(function(a, b){
		    return b[fieldNm] - a[fieldNm];
		});

		// 설계학점 총계 출력
		var pHtml = '';
		var tempNum = '';
		var colgNm = '';
		for(var i=0; i < finalSumPntList.length; i++){
			if(i < 9){
				tempNum = '0'+(i+1);
			}else{
				tempNum = i+1;
			}
			
			if(finalSumPntList[i].ORG_CODE == 'EXTERNAL'){
				colgNm = '타 대학/기관/산업체';
			}else{
				colgNm = '국립한국해양대학교';
			}
						
			pHtml += '<tr>';
			pHtml += 	'<td class="numb">'+tempNum+'</td>';
			pHtml += 	'<td class="univ">'+colgNm+'</td>';
			pHtml += 	'<td class="coll">'+finalSumPntList[i].COLG_NM+'</td>';
			pHtml += 	'<td class="majo">'+finalSumPntList[i].MAJOR_NM+'</td>';
			pHtml += 	'<td class="scor">'+finalSumPntList[i].CDT_NUM+'학점<input type="hidden" name="valiPnt" value="'+finalSumPntList[i].CDT_NUM+'"/></td>';
			pHtml += '</tr>';
		}

		$("#appndSumPnt").empty();
		$("#appndSumPnt").append(pHtml);
		
		var rsSumPnt = 0;
		
		$(".tdPnt").each(function(){
			rsSumPnt += Number($(this).text());
		});
		
		$(".totalPnt").text(rsSumPnt);		
		$('#desTotCnt').val(rsSumPnt);	//학점 체크용 hidden에 입력
	}
	
	//교육과정 신구대비표
	function chgSbjtDiffDisplay(){
		//추가할때마다 동적으로 변경해야되기때문에 hidden에서 가져온다		
   	    var jsonArray = [];
   	    getCommonJsonArray(jsonArray);    
   	    
       var sbjtList = jsonArray;
       var sbjtListPc = $('#sbjtListPc');
       
       sbjtListPc.empty(); // 기존 내용을 비움
       
		var majorArray = ['UE010021', 'UE010022', 'UE010024', 'UE010031'];
// 		var majorArray = ['UE010021', 'UE010022', 'UE010024'];
		var genArray = ['UE010011', 'UE010012']			

       // 'C'가 마지막에 오도록 정렬
       sbjtList.sort(function(a, b) {
           if (a.CHG_FG === 'C') return 1;
           if (b.CHG_FG === 'C') return -1;
           return 0;
       });
       
       $.each(sbjtList, function(i, sbjt) {
			
           var rowClass = sbjt.CHG_FG != 'C' ? 'origin' : '';
           var row = '<tr class="' + rowClass + '">';
           
           row += '<input type="hidden" name="keyDocId" value="'+sbjt.CHG_DOC_ID+'">'
           
           row += '<td>';
           if (sbjt.CHG_FG === 'C') {
           	   row += '<select class="form-select chgFg" disabled>';
               row += '<option value="C" selected>추가</option>';
           } else {
        	   row += '<select class="form-select chgFg">';
        	   row += '<option value="D"' + (sbjt.CHG_FG === 'D' ? ' selected' : '') + '>삭제</option>';
        	   if(sbjt.CHG_COMDIV_CODE != 'UE010031'){      		   
	        	   row += '<option value="U"' + (sbjt.CHG_FG === 'U' ? ' selected' : '') + '>이수구분변경</option>';
        	   }
        	   row += '<option value="N"' + (sbjt.CHG_FG === 'N' ? ' selected' : '') + '>변경없음</option>';
           }
           row += '</select>';
           row += '</td>';

           //추가
           if(sbjt.CHG_FG === 'C'){
               row += '<td class="bord" colspan="5">---추가---</td>';
               
               row += '<td class="afterSbjt_CHG_COMDIV_CODE_NM">' + sbjt.CHG_COMDIV_CODE_NM + '</td>';
               row += '<td class="afterSbjt_COM_GRADE">' + sbjt.CHG_COM_GRADE + '</td>';
               row += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.CHG_SUBJECT_NM + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.CHG_SMT_NM === '1학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.CHG_SMT_NM === '2학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';               
               row += '<td><input type="text" class="form-control" id="CHG_RESN" value="'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'"></td>';

               row += '</tr>';
           }
           
           //삭제
           if(sbjt.CHG_FG === 'D'){

               row += '<td>' + sbjt.COMDIV_CODE_NM + '</td>';
               row += '<td>' + sbjt.COM_GRADE + '</td>';
               row += '<td class="text-start">' + sbjt.SUBJECT_NM + '</td>';
               row += '<td>' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
               row += '<td class="bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
               
               row += '<td class="bord deleteSbjt" colspan="5">---삭제---</td>';
               
               row += '<td><input type="text" class="form-control" id="CHG_RESN" value="'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'"></td>';

               row += '</tr>';
           }
           
           //이수구분 변경
           if(sbjt.CHG_FG === 'U'){
        	   var comDivCodeSelect = "";
        	   	    		

 				if (majorArray.includes(sbjt.CHG_COMDIV_CODE)) {
 				    var majorOptions = [
 				        { value: 'UE010021', label: '전공필수' },
 				        { value: 'UE010022', label: '전공선택' },
 				        { value: 'UE010024', label: '전공기초' }
 				    ];
 				    comDivCodeSelect = createSelectBox(sbjt, majorOptions);
 				} else if (genArray.includes(sbjt.CHG_COMDIV_CODE)) {
 				    var genOptions = [
 				        { value: 'UE010011', label: '교양필수' },
 				        { value: 'UE010012', label: '교양선택' }
 				    ];
 				    comDivCodeSelect = createSelectBox(sbjt, genOptions);
 				}
               
               row += '<td>' + sbjt.COMDIV_CODE_NM + '</td>';
               row += '<td>' + sbjt.COM_GRADE + '</td>';
               row += '<td class="text-start">' + sbjt.SUBJECT_NM + '</td>';
               row += '<td>' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
               row += '<td class="bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
               
               row += '<td class="afterSbjt_CHG_COMDIV_CODE_NM_select">' + comDivCodeSelect + '</td>';
               row += '<td class="afterSbjt_COM_GRADE">' + sbjt.COM_GRADE + '</td>';
               row += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.CHG_SUBJECT_NM + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.CHG_SMT_NM === '1학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.CHG_SMT_NM === '2학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';               
               row += '<td><input type="text" class="form-control" id="CHG_RESN" value="'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'"></td>';

               row += '</tr>';
           }
           
           //변경없음
           if(sbjt.CHG_FG === 'N'){
               
               row += '<td>' + sbjt.COMDIV_CODE_NM + '</td>';
               row += '<td>' + sbjt.COM_GRADE + '</td>';
               row += '<td class="text-start">' + sbjt.SUBJECT_NM + '</td>';
               row += '<td>' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
               row += '<td class="bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
               
               
               row += '<td class="afterSbjt_CHG_COMDIV_CODE_NM">' + sbjt.COMDIV_CODE_NM + '</td>';
               row += '<td class="afterSbjt_COM_GRADE">' + sbjt.COM_GRADE + '</td>';
               row += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.SUBJECT_NM + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';               
               row += '<td><input type="text" class="form-control" id="CHG_RESN" value="'+(sbjt.RESN == undefined ? '' : sbjt.RESN)+'" disabled></td>';

           }
           row += '</tr>';
           sbjtListPc.append(row);
       });
       
       

	}
	
	
	// 변경된 행 업데이트
	function updateRow(row, sbjt) {
	    var newRow = '';
		newRow += '<input type="hidden" name="keyDocId" value="'+sbjt.CHG_DOC_ID+'">'
	    // 추가
	    if (sbjt.CHG_FG === 'C') {
	        newRow += '<td>';
	        newRow += '<select class="form-select chgFg" disabled>';
	        newRow += '<option value="C" selected>추가</option>';
	        newRow += '</select>';
	        newRow += '</td>';
	        newRow += '<td class="bord" colspan="5">---추가---</td>';
	        newRow += '<td class="afterSbjt_CHG_COMDIV_CODE_NM">' + sbjt.CHG_COMDIV_CODE_NM + '</td>';
	        newRow += '<td class="afterSbjt_COM_GRADE">' + sbjt.COM_GRADE + '</td>';
	        newRow += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.CHG_SUBJECT_NM + '</td>';
	        newRow += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.CHG_SMT_NM === '1학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
	        newRow += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.CHG_SMT_NM === '2학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
	        newRow += '<td><input type="text" class="form-control" id="CHG_RESN" value="' + (sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN) + '"></td>';
	    }
	
	    // 삭제
	    if (sbjt.CHG_FG === 'D') {
	        newRow += '<td>';
	        newRow += '<select class="form-select chgFg">';
	        newRow += '<option value="D" selected>삭제</option>';
	        newRow += '<option value="U">이수구분변경</option>';
	        newRow += '<option value="N">변경없음</option>';
	        newRow += '</select>';
	        newRow += '</td>';
	        newRow += '<td>' + sbjt.COMDIV_CODE_NM + '</td>';
	        newRow += '<td>' + sbjt.COM_GRADE + '</td>';
	        newRow += '<td class="text-start">' + sbjt.SUBJECT_NM + '</td>';
	        newRow += '<td>' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
	        newRow += '<td class="bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
	        newRow += '<td class="bord deleteSbjt" colspan="5">---삭제---</td>';
	        newRow += '<td><input type="text" class="form-control" id="CHG_RESN" value="' + (sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN) + '"></td>';
	    }
	
	    // 이수구분 변경
	    if (sbjt.CHG_FG === 'U') {
	        var comDivCodeSelect = '';

	
// 	        if (majorArray.includes(sbjt.CHG_COMDIV_CODE)) {
// 	            comDivCodeSelect =
// 	                '<select class="form-select selectChgSbjtFg">' +
// 	                '<option value="UE010021"' + (sbjt.CHG_COMDIV_CODE == 'UE010021' ? ' style="display:none;"' : '') + '>전공필수</option>' +
// 	                '<option value="UE010022"' + (sbjt.CHG_COMDIV_CODE == 'UE010022' ? ' style="display:none;"' : '') + '>전공선택</option>' +
// 	                '<option value="UE010024"' + (sbjt.CHG_COMDIV_CODE == 'UE010024' ? ' style="display:none;"' : '') + '>전공기초</option>' +
// 	                '</select>';
// 	        } else if (genArray.includes(sbjt.CHG_COMDIV_CODE)) {
// 	            comDivCodeSelect =
// 	                '<select class="form-select selectChgSbjtFg">' +
// 	                '<option value="UE010011"' + (sbjt.CHG_COMDIV_CODE == 'UE010011' ? ' style="display:none;"' : '') + '>교양필수</option>' +
// 	                '<option value="UE010012"' + (sbjt.CHG_COMDIV_CODE == 'UE010012' ? ' style="display:none;"' : '') + '>교양선택</option>' +
// 	                '</select>';
// 	        }

			if (majorArray.includes(sbjt.CHG_COMDIV_CODE)) {
			    console.log(sbjt.CHG_COMDIV_CODE + " / " + sbjt.CHG_COMDIV_CODE_NM + " : " + sbjt.SUBJECT_NM);
			    var majorOptions = [
			        { value: 'UE010021', label: '전공필수' },
			        { value: 'UE010022', label: '전공선택' },
			        { value: 'UE010024', label: '전공기초' }
			    ];
			    comDivCodeSelect = createSelectBox(sbjt, majorOptions);
			} else if (genArray.includes(sbjt.CHG_COMDIV_CODE)) {
			    var genOptions = [
			        { value: 'UE010011', label: '교양필수' },
			        { value: 'UE010012', label: '교양선택' }
			    ];
			    comDivCodeSelect = createSelectBox(sbjt, genOptions);
			}
	
	        newRow += '<td>';
	        newRow += '<select class="form-select chgFg">';
	        newRow += '<option value="D">삭제</option>';
	        newRow += '<option value="U" selected>이수구분변경</option>';
	        newRow += '<option value="N">변경없음</option>';
	        newRow += '</select>';
	        newRow += '</td>';
	        newRow += '<td>' + sbjt.COMDIV_CODE_NM + '</td>';
	        newRow += '<td>' + sbjt.COM_GRADE + '</td>';
	        newRow += '<td class="text-start">' + sbjt.SUBJECT_NM + '</td>';
	        newRow += '<td>' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
	        newRow += '<td class="bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
	        newRow += '<td class="afterSbjt_CHG_COMDIV_CODE_NM_select">' + comDivCodeSelect + '</td>';
	        newRow += '<td class="afterSbjt_COM_GRADE">' + sbjt.COM_GRADE + '</td>';
	        newRow += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.CHG_SUBJECT_NM + '</td>';
	        newRow += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.CHG_SMT_NM === '1학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
	        newRow += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.CHG_SMT_NM === '2학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
	        newRow += '<td><input type="text" class="form-control" id="CHG_RESN" value="' + (sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN) + '"></td>';
	    }
	
	    // 변경없음
	    if (sbjt.CHG_FG === 'N') {
	        newRow += '<td>';
	        newRow += '<select class="form-select chgFg">';
	        newRow += '<option value="D">삭제</option>';
	        newRow += '<option value="U">이수구분변경</option>';
	        newRow += '<option value="N" selected>변경없음</option>';
	        newRow += '</select>';
	        newRow += '</td>';
	        newRow += '<td>' + sbjt.COMDIV_CODE_NM + '</td>';
	        newRow += '<td>' + sbjt.COM_GRADE + '</td>';
	        newRow += '<td class="text-start">' + sbjt.SUBJECT_NM + '</td>';
	        newRow += '<td>' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
	        newRow += '<td class="bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
	        newRow += '<td class="afterSbjt_CHG_COMDIV_CODE_NM">' + sbjt.COMDIV_CODE_NM + '</td>';
	        newRow += '<td class="afterSbjt_COM_GRADE">' + sbjt.COM_GRADE + '</td>';
	        newRow += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.SUBJECT_NM + '</td>';
	        newRow += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
	        newRow += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
	        newRow += '<td><input type="text" class="form-control" id="CHG_RESN" value="' + (sbjt.RESN == undefined ? '' : sbjt.RESN) + '"></td>';
	    }
	
	    row.html(newRow); // 기존 행 내용을 새로운 내용으로 대체
	}
	
	function getCommonJsonArray(jsonArray){
        $('#containInput [class^="SBJT_LIST"]').each(function() {
            var obj = {};
            $(this).find('input[type="hidden"]').each(function() {
                var name = $(this).attr('name');
                var value = $(this).val();
                obj[name] = value;
            });
            jsonArray.push(obj);
        });
	}
	
	function createSelectBox(sbjt, options) {
	    var selectHtml = '<li><select class="form-select selectChgSbjtFg" id="selectChgSbjtFg">';
	    options.forEach(function(option) {
	        if (sbjt.COMDIV_CODE !== option.value) {
	            selectHtml += '<option value="' + option.value + '"' + 
	                          (sbjt.CHG_COMDIV_CODE === option.value ? ' selected' : '') + '>' + 
	                          option.label + '</option>';
	        }
	    });
	    selectHtml += '</li></select>';
	    return selectHtml;
	}

</script>