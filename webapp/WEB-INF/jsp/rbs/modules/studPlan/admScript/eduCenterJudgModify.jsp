<script>
let bookmarkList;
let duplSbjtArr = [];
	$(function(){
		
		// 반려사유 입력마다 hidden에 동기화
	    $('#judgOpin').on('input', function() {
	        $('input[name="JUDG_OPIN"]').val($(this).val());
	    });
		
		// 저장된 교과목 세팅
		$.ajax({
			url: '/web/studPlan/getMySbjtList.do?mId=36',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'POST',
			data: JSON.stringify({sdmCd : "${INFO.SDM_CD}", revsnNo : "${INFO.REVSN_NO}"}),
			success:function(data){
				setSavedSbjtToHidden(data.SBJTLIST);
				setSavedSbjtToModal(data.SBJTLIST);
			}
		});								
		
	});	// end $(function)
	
	var sbjtArray = new Array();
	
	// 과목등록하기 Modal (저장하기 버튼 클릭시)
	function sbjtDesignScope(){
		// 설계선택 목록 JSON Data
		sbjtArray = new Array();
		
		if($(".head-after").length == 0){
			alert("과목을 추가해주세요.");
			return false;
		}
		
		
		

		// 설계선택 목록의 유사도 JSON Data
		var ﻿﻿sumPntArray = new Array();
		var ﻿﻿colgMjArr = new Array();
		var colgNmArr = [];
		
		var chkPnt = 0;	// 학점체크 ex) 165점 이하만 가능
			
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
			obj.FUSE_DEPT_CD 		= $(this).attr('aria-FUSE_DEPT_CD');
			obj.FUSE_DEPT_CD_NM 	= $(this).attr('aria-FUSE_DEPT_CD_NM');			
			
			sbjtArray.push(obj);
			
			var pntobj = new Object();

			pntobj.COLG_NM = $(this).attr("aria-COLG_NM");
			pntobj.MAJOR_NM = $(this).attr("aria-MAJOR_NM");
			
// 			var shyrFg = $(this).attr("aria-COM_GRADE");
			var comGrade = $(this).attr("aria-COM_GRADE");
			var tempPnt = $(this).attr("aria-CDT_NUM");
			pntobj.CDT_NUM = Number(tempPnt);
			
			sumPntArray.push(pntobj);
			
			chkPnt += Number($(this).attr("aria-CDT_NUM"));
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

			
			sbjtArray.push(obj);

			var pntobj = new Object();
			
			pntobj.COLG_NM 			= $("#headAfter-"+num).attr("aria-COLG_NM");
			pntobj.MAJOR_NM 		= $("#headAfter-"+num).attr("aria-MAJOR_NM");
			pntobj.ORG_CODE 		= $("#headAfter-"+num).attr("aria-ORG_CODE");
			pntobj.CDT_NUM 			= $("#headAfter-"+num).attr("aria-CDT_NUM");
			
			sumPntArray.push(pntobj);

			chkPnt += Number($("#headAfter-"+num).attr("aria-CDT_NUM"));
		});
		
		$("#totalPnt").val(chkPnt);
		
		if(chkPnt > 165){
			alert('선택된 설계학점 총점이 '+chkPnt+'점으로 너무 높습니다. (최대: 165점)');
			$("#totalPnt").val("0");
			return false;
		}
		
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

		var g1 = [];
		var g2 = [];
		var g3 = [];
		var g4 = [];
		
		
		sbjtArray.forEach(function(sbjt) {	            		
			switch(sbjt.COM_GRADE){
				case "1" : g1.push(sbjt);
						   break;
				case "2" : g2.push(sbjt);
						   break;
				case "3" : g3.push(sbjt);
						   break;
				case "4" : g4.push(sbjt);
						   break;
			}
		});					
		
		//1학년 렌더링
		renderSbjtTable(g1,'1');

		//2학년 렌더링
		renderSbjtTable(g2,'2');

		//3학년 렌더링
		renderSbjtTable(g3,'3');
		
		//4학년 렌더링
		renderSbjtTable(g4,'4');
		



		
		var rsSumPnt = 0;
		
		$(".tdPnt").each(function(){
			rsSumPnt += Number($(this).text());
		});
		
		$(".totalPnt").text(rsSumPnt);
		$('#desTotCnt').val(rsSumPnt);

		$("#similitySdmNm").text("-");
		$("#similityRate").text("0");
		
		//교과목 hidden 초기화
		$('#containInput').empty();
		
		// 교과목 input hidden으로 데이터 넘기기
		var contains = "";				
		for(var i=0; i < sbjtArray.length; i++){
			contains +=  '<div>'
					+	'<input type="hidden" name="ORG_CODE" value="'+sbjtArray[i].ORG_CODE+'"/>'
					+	'<input type="hidden" name="COLG_CD" value="'+sbjtArray[i].COLG_CD+'"/>'
					+	'<input type="hidden" name="COLG_NM" value="'+sbjtArray[i].COLG_NM+'"/>'
					+	'<input type="hidden" name="COLG_ENM" value="'+(sbjtArray[i].COLG_ENM == undefined ? '' : sbjtArray[i].COLG_ENM)+'"/>'
					+	'<input type="hidden" name="MAJOR_CD" value="'+sbjtArray[i].MAJOR_CD+'"/>'
					+	'<input type="hidden" name="MAJOR_NM" value="'+sbjtArray[i].MAJOR_NM+'"/>'
					+	'<input type="hidden" name="MAJOR_ENM" value="'+(sbjtArray[i].MAJOR_ENM == undefined ? '' : sbjtArray[i].MAJOR_ENM)+'"/>'
					+	'<input type="hidden" name="COMDIV_CODE" value="'+sbjtArray[i].COMDIV_CODE+'"/>'
					+	'<input type="hidden" name="COMDIV_CODE_NM" value="'+sbjtArray[i].COMDIV_CODE_NM+'"/>'
					+	'<input type="hidden" name="ORG_COMDIV_CODE" value="'+sbjtArray[i].ORG_COMDIV_CODE+'"/>'
					+	'<input type="hidden" name="SUBJECT_CD" value="'+sbjtArray[i].SUBJECT_CD+'"/>'
					+	'<input type="hidden" name="SUBJECT_NM" value="'+sbjtArray[i].SUBJECT_NM+'"/>'
					+	'<input type="hidden" name="SUBJECT_ENM" value="'+(sbjtArray[i].SUBJECT_ENM == undefined ? '' : sbjtArray[i].SUBJECT_ENM)+'"/>'
					+	'<input type="hidden" name="COM_GRADE" value="'+sbjtArray[i].COM_GRADE+'"/>'
					+	'<input type="hidden" name="COM_GRADE_NM" value="'+sbjtArray[i].COM_GRADE_NM+'"/>'
					+	'<input type="hidden" name="YEAR" value="'+sbjtArray[i].YEAR+'"/>'
					+	'<input type="hidden" name="SMT" value="'+sbjtArray[i].SMT+'"/>'
					+	'<input type="hidden" name="SMT_NM" value="'+sbjtArray[i].SMT_NM+'"/>'
					+	'<input type="hidden" name="CDT_NUM" value="'+sbjtArray[i].CDT_NUM+'"/>'
					+	'<input type="hidden" name="WTIME_NUM" value="'+sbjtArray[i].WTIME_NUM+'"/>'
					+	'<input type="hidden" name="PTIME_NUM" value="'+sbjtArray[i].PTIME_NUM+'"/>'
					+	'</div>'
		}
		$('#containInput').html(contains);		
		
		$("#enrollSubj").modal("hide");	
				
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
	
// ==========================================================================================================교수 검색=================================================================
	
	//교과목 테이블 렌더링
	function renderSbjtTable(g, grade){
		if(g != null){
			gubunArr = [];
			
			for(var i=0; i < g.length; i++){
				if(gubunArr.indexOf(g[i].sbjtFg) == -1){
					gubunArr.push(g[i].sbjtFg);
				}
			}
			
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
				if(g[i].COMDIV_CODE == 'UE010021'){
					essRowSpan++;
				}else if(g[i].COMDIV_CODE == 'UE010022'){
					selRowSpan++;
				}else if(g[i].COMDIV_CODE == 'UE010024'){
					basRowSpan++;
				}else if(g[i].COMDIV_CODE == 'UE010011'){
					genEssRowSpan++;
				}else if(g[i].COMDIV_CODE == 'UE010012'){
					genSelRowSpan++;
				}else if(g[i].COMDIV_CODE == 'UE010031'){
					teachRowSpan++;
				}
			}
			
		    basHtml    = processSbjtHtml(g, 'UE010021', '전공필수', essRowSpan);
		    essHtml    = processSbjtHtml(g, 'UE010022', '전공선택', selRowSpan);
		    selHtml    = processSbjtHtml(g, 'UE010024', '전공기초', basRowSpan);
		    genEssHtml = processSbjtHtml(g, 'UE010011', '교양필수', genEssRowSpan);
		    genSelHtml = processSbjtHtml(g, 'UE010012', '교양선택', genSelRowSpan);
		    teachHtml = processSbjtHtml(g, 'UE010031', '교직과목', teachRowSpan);

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
	function processSbjtHtml(subjects, groupCode, groupName, rowspan) {
	    var html = '';
	    var totalPnt = 0;
	    var beforePnt = 0;
	    var afterPnt = 0;
	    var thTagFg = 0;
	    
	    
	    for (var i = 0; i < subjects.length; i++) {
	        if (subjects[i].COMDIV_CODE == groupCode) {
	        	totalPnt = 0;
	            html += '<tr>';
	            if (thTagFg == 0) {
	                html += '<th scope="rowgroup" rowspan="' + rowspan + '" class="border-end">' + groupName + '</th>';
	                thTagFg++;
	            }
	            html += '<td class="border-end '+groupCode+'_'+ subjects[i].COM_GRADE +'" aria-mjCd="' + subjects[i].MAJOR_CD + '">' + subjects[i].MAJOR_NM + '</td>';
	            html += '<td class="border-end">' + subjects[i].SUBJECT_NM + '</td>';

	            // 학점
	            if (subjects[i].SMT == 'GH0210') { // 1학기
	                html += '<td class="border-end">' + subjects[i].CDT_NUM + '</td>';
	                html += '<td></td>';
	                beforePnt += Number(subjects[i].CDT_NUM);
	            } else if (subjects[i].SMT == 'GH0220') { // 2학기
	                html += '<td class="border-end"></td>';
	                html += '<td>' + subjects[i].CDT_NUM + '</td>';
	                afterPnt += Number(subjects[i].CDT_NUM);
	            }
	            
//                 html += '<td class="border-end">' + Number(beforePnt + afterPnt) + '</td>';
//                 html += '<td class="border-end"></td>';
//                 html += '<td></td>';
//                 totalPnt += Number(beforePnt + afterPnt);
                
	            html += '</tr>';
	        }
	    }
	    
	    // 소계
	    if (html.length > 0) {
	        html += '<tr class="table_sum">';
	        html += '<td colspan="3" class="border-end">소계</td>';
// 	        html += '<td class="border-end tdPnt">' + totalPnt + '</td>';
	        html += '<td class="border-end tdPnt">' + beforePnt + '</td>';
	        html += '<td class="tdPnt">' + afterPnt + '</td>';
	        html += '</tr>';
	    }

	    return html;
	}	
	
	
	function getInputDataHasName(json_data) {
	    // 교과목용 배열 필드
	    const arrayFields = [
	        "ORG_CODE", "COLG_CD", "COLG_NM", "MAJOR_CD", "MAJOR_NM",
	        "COMDIV_CODE", "COMDIV_CODE_NM", "ORG_COMDIV_CODE", "SUBJECT_CD",
	        "SUBJECT_NM", "SUBJECT_ENM", "COM_GRADE", "COM_GRADE_NM", "YEAR",
	        "SMT", "SMT_NM", "CDT_NUM", "WTIME_NUM", "PTIME_NUM"
	    ];

	    $('#frmPost').find('input').each(function() {
	        let name = $(this).attr('name');
	        let value = $(this).val() || ''; // undefined 값을 ''로 치환

	        // 이름이 존재하는 input만 추가 (소문자는 제외)
	        if (name && !/[a-z]/.test(name)) {
	            // 교과목용 필드는 배열로 설정
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
	        let value = $(this).val() || ''; // undefined 값을 ''로 치환

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
	        let value = $(this).val() || ''; // undefined 값을 ''로 치환

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
	
	/*승인*/	
	function approve(){
		if(confirm("승인하시겠습니까?")){
	        let json_data = {};
	        getInputDataHasName(json_data);
	        
			$.ajax({
				url: '/RBISADM/menuContents/web/studPlan/judgApprove.do?mId=48',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'POST', 
				data: JSON.stringify({
					SDM_CD : json_data.SDM_CD,
					REVSN_NO : json_data.REVSN_NO,
					JUDG_OPIN : json_data.JUDG_OPIN,
					DEPART_FG : 'EDU_CENTER'
				}),
				success:function(data){				
					alert(data.RTN_MSG);
					if(data.RESULT == "DONE"){
						/*상세보기화면으로 이동*/
						var form = document.frmView;
						$("#SDM_CD").val(json_data.SDM_CD);
						$("#REVSN_NO").val(json_data.REVSN_NO);
						form.action = '/RBISADM/menuContents/web/studPlan/eduCenterJudgModify.do';
						form.submit();
					}else{
						console.log(JSON.stringify(data.RESULT))
					}
				}
			});	
		}
	}
	
	/*반려*/	
	function reject(){
		if($('input[name="JUDG_OPIN"]').val() === undefined || $('input[name="JUDG_OPIN"]').val() === ''){
			alert("반려 시 검토의견은 필수 입력입니다.");
			return false;
		}
		if(confirm("반려하시겠습니까?")){
	        let json_data = {};
	        getInputDataHasName(json_data);
	        
			$.ajax({
				url: '/RBISADM/menuContents/web/studPlan/judgReject.do?mId=48',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'POST', 
				data: JSON.stringify({
					SDM_CD : json_data.SDM_CD,
					REVSN_NO : json_data.REVSN_NO,
					JUDG_OPIN : json_data.JUDG_OPIN,
					DEPART_FG : 'EDU_CENTER'
				}),
				success:function(data){				
					alert(data.RTN_MSG);
					if(data.RESULT == "DONE"){
						/*상세보기화면으로 이동*/
						var form = document.frmView;
						$("#SDM_CD").val(json_data.SDM_CD);
						$("#REVSN_NO").val(json_data.REVSN_NO);
						form.action = '/RBISADM/menuContents/web/studPlan/eduCenterJudgModify.do';
						form.submit();
					}
				}
			});	
		}
	}	
	
		
	//저장된 교과목 목록 hidden에 추가
	function setSavedSbjtToHidden(data){
        const container = $('#containInput');
        container.empty(); // 기존 내용을 초기화
        data.forEach(function (sbjtList) {
            const sbjtDiv = $('<div>', { class: 'SBJT_LIST' });

            const fields = [
                'ORG_CODE', 'COLG_CD', 'COLG_NM', 'MAJOR_CD', 'MAJOR_NM', 
                'COMDIV_CODE', 'COMDIV_CODE_NM', 'ORG_COMDIV_CODE', 'SUBJECT_CD', 
                'SUBJECT_NM', 'SUBJECT_ENM', 'COM_GRADE', 'COM_GRADE_NM', 
                'YEAR', 'SMT', 'SMT_NM', 'CDT_NUM', 'WTIME_NUM', 'PTIME_NUM'
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
	
	//저장된 교과목 목록 display
	function setSavedSbjtToModal(data){
		var html = "";
		var majorArray = ['UE010021', 'UE010022', 'UE010024'];
		var genArray = ['UE010011', 'UE010012'];
		for(var i=0; i < data.length; i++){
			

			var sbjtFgHtml = '';
			if(majorArray.includes(data[i].COMDIV_CODE)){
			    sbjtFgHtml = '<li>'+
			                 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
			                 '<option value="UE010021"'+ (data[i].COMDIV_CODE == "UE010021" ? 'selected' : '') +'>전공필수</option>'+
			                 '<option value="UE010022"'+ (data[i].COMDIV_CODE == "UE010022" ? 'selected' : '') +'>전공선택</option>'+
			                 '<option value="UE010024"'+ (data[i].COMDIV_CODE == "UE010024" ? 'selected' : '') +'>전공기초</option>'+
			                 '</select>'+
			                 '</li>';                
			}else if(genArray.includes(data[i].COMDIV_CODE)){
			    sbjtFgHtml = '<li>'+
			                 '<select class="form-select selectSbjtFg" id="selectSbjtFg">'+
			                 '<option value="UE010011"'+ (data[i].COMDIV_CODE == "UE010011" ? 'selected' : '') +'>교양필수</option>'+
			                 '<option value="UE010012"'+ (data[i].COMDIV_CODE == "UE010012" ? 'selected' : '') +'>교양선택</option>'+
			                 '</select>'+
			                 '</li>';    
			}

			html += '<div class="item p-3 head-after" id="headAfter-'+data[i].DOC_ID+'">';
			html +=    '<input type="hidden" name="sdmAddList" aria-YEAR="'+data[i].YEAR+'" aria-SMT="'+data[i].SMT+'" aria-SMT_NM="'+data[i].SMT_NM+'" aria-SUBJECT_CD="'+data[i].SUBJECT_CD+'" aria-MAJOR_CD="'+data[i].MAJOR_CD+'" aria-MAJOR_NM="'+data[i].MAJOR_NM+'" aria-COM_GRADE="'+data[i].COM_GRADE+'" aria-COM_GRADE_NM="'+data[i].COM_GRADE_NM+'" aria-COLG_CD="'+data[i].COLG_CD+'" aria-COLG_NM="'+data[i].COLG_NM+'" aria-COMDIV_CODE="'+data[i].COMDIV_CODE+'" aria-COMDIV_CODE_NM="'+data[i].COMDIV_CODE_NM+'" aria-SUBJECT_NM="'+data[i].SUBJECT_NM+'" aria-SUBJECT_ENM="'+data[i].SUBJECT_ENM+'" aria-CDT_NUM="'+data[i].CDT_NUM+'" aria-WTIME_NUM="'+data[i].WTIME_NUM+'" aria-PTIME_NUM="'+data[i].PTIME_NUM+'" aria-ORG_CODE="'+data[i].ORG_CODE+'" aria-ORG_COMDIV_CODE="'+data[i].COMDIV_CODE+'" />';
			html +=    '<div class="d-inline-flex form-check">';
			html +=        '<label class="blind" for="lessons_'+data[i].DOC_ID+'">선택</label>';
			html +=        '<input type="checkbox" id="lessons_'+data[i].DOC_ID+'" class="form-check-input chk-after">';
			html +=    '</div>';
			html +=    '<section class="d-inline-flex flex-column flex-lg-row justify-content-between">';
			html +=        '<div class="d-flex flex-column align-items-start div-after">';
			html +=            '<ul class="cate d-flex flex-wrap gap-2">';
			html +=                sbjtFgHtml;
			html +=                '<li class="border">';
			html +=                    '<span>'+data[i].MAJOR_NM+'</span>';
			html +=                '</li>';
			html +=            '</ul>';
			html +=            '<h4 class="text-truncate ellip_2 my-2 h-sbjt-kor-nm" id="sbjtNm-'+data[i].DOC_ID+'">'+data[i].SUBJECT_NM+'</h4>';
			html +=            '<div class="d-flex flex-row w-100 mb-1">';
			html +=                '<strong class="d-block me-2">['+data[i].SUBJECT_CD+']</strong>';
			html +=                '<span class="d-block mb-0 text-truncate text-dark text-opacity-75">'+data[i].SUBJECT_ENM+'</span>';
			html +=            '</div>';
			html +=            '<div class="w-100">';
			html +=                '<dl class="d-inline-flex position-relative pe-4">';
			html +=                    '<dt class="me-2">편성</dt>';
			html +=                    '<dd class="text-dark text-opacity-75">'+data[i].YEAR+'학년도 '+data[i].COM_GRADE+'학년 '+data[i].SMT_NM+'</dd>';
			html +=                '</dl>';
			html +=                '<dl class="d-inline-flex">';
			html +=                    '<dt class="me-2">학점</dt>';
			html +=                    '<dd class="text-dark text-opacity-75">'+data[i].CDT_NUM+'-'+data[i].WTIME_NUM+'-'+data[i].PTIME_NUM+'</dd>';
			html +=                '</dl>';
			html +=            '</div>';
			html +=        '</div>';
			html +=        '<ol class="d-inline-flex btn_wrap">';
			html +=            '<li>';
			html +=                '<button type="button" class="border-0 bg-transparent btn_dele" onclick="delSbjt(\''+data[i].DOC_ID+'\')">';
			html +=                    '<span class="text-center ps-1 d-inline-block d-lg-none text-white">삭제</span>';
			html +=                '</button>';
			html +=            '</li>';
			html +=        '</ol>';
			html +=    '</section>';
			html += '</div>';
			
			duplSbjtArr.push(data[i].DOC_ID);
		}
		$("#appndSbjtChoice").append(html);
		$("#no-data-img").remove();
		
		//교육과정표 및 설계학점 총계 세팅
		if(data.length != 0){
			sbjtDesignScope();	
		}
	}

</script>