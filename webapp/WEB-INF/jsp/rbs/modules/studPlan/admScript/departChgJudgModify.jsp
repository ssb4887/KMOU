<script>
let bookmarkList;
let duplSbjtArr = [];
let beforeSbjtData = [];
let sbjtArray = new Array();

	$(function(){
		
		// 반려사유 입력마다 hidden에 동기화
	    $('#judgOpin').on('input', function() {
	        $('input[name="JUDG_OPIN"]').val($(this).val());
	    });
		
		// 저장된 교과목 세팅
		$.ajax({
			url: '/web/studPlan/getMyChgSbjtList.do?mId=36',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'POST',
			data: JSON.stringify({sdmCd : "${INFO.SDM_CD}", revsnNo : "${INFO.REVSN_NO}", beforeRevsnNo : "${INFO.REVSN_NO}"}),
			success:function(data){
				//변경전 교과목 전역변수 선언
				beforeSbjtData = data.SBJTLIST;
				setSavedChgSbjtToHidden(data.CHG_SBJTLIST);	//hidden값 세팅
				sbjtDisplay(beforeSbjtData);
				chgSbjtDiffDisplay();		//교육과정 신구대비표


			}
		});							
		
	});	// end $(function)
	
	
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
					DEPART_FG : 'DEPART'
				}),
				success:function(data){				
					alert(data.RTN_MSG);
					if(data.RESULT == "DONE"){
						/*상세보기화면으로 이동*/
						var form = document.frmView;
						$("#SDM_CD").val(json_data.SDM_CD);
						$("#REVSN_NO").val(json_data.REVSN_NO);
						form.action = '/RBISADM/menuContents/web/studPlan/departChgJudgModify.do';
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
					DEPART_FG : 'DEPART'
				}),
				success:function(data){				
					alert(data.RTN_MSG);
					if(data.RESULT == "DONE"){
						/*상세보기화면으로 이동*/
						var form = document.frmView;
						$("#SDM_CD").val(json_data.SDM_CD);
						$("#REVSN_NO").val(json_data.REVSN_NO);
						form.action = '/RBISADM/menuContents/web/studPlan/departChgJudgModify.do';
						form.submit();
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
                'ORG_CODE', 'COLG_CD', 'COLG_NM', 'MAJOR_CD', 'MAJOR_NM', 
                'COMDIV_CODE', 'COMDIV_CODE_NM', 'ORG_COMDIV_CODE', 'SUBJECT_CD', 
                'SUBJECT_NM', 'SUBJECT_ENM', 'COM_GRADE', 'COM_GRADE_NM', 
                'YEAR', 'SMT', 'SMT_NM', 'CDT_NUM', 'WTIME_NUM', 'PTIME_NUM',
                'CHG_FG','CHG_RESN',
                'CHG_COLG_CD', 'CHG_COLG_NM', 'CHG_MAJOR_CD', 'CHG_MAJOR_NM', 
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
	
	// 교과목 display()
	function sbjtDisplay(data){
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
			obj.FUSE_DEPT_CD 		= sbjt.FUSE_DEPT_CD;
			obj.FUSE_DEPT_CD_NM 	= sbjt.FUSE_DEPT_CD_NM;

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
	
	//교육과정 신구대비표
	function chgSbjtDiffDisplay(){
		//추가할때마다 동적으로 변경해야되기때문에 hidden에서 가져온다		
   	    var jsonArray = [];
   	    getCommonJsonArray(jsonArray);    
   	    
       var sbjtList = jsonArray;
       var sbjtListPc = $('#sbjtListPc');
       
       sbjtListPc.empty(); // 기존 내용을 비움

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
           
           row += '<td style="text-align:center;">';
           switch(sbjt.CHG_FG){
           		case 'C': row += '추가'; break; 
           		case 'U': row += '이수구분변경'; break; 
           		case 'D': row += '삭제'; break; 
           		case 'N': row += '변경없음'; break; 
           }
           row += '</td>';

           //추가
           if(sbjt.CHG_FG === 'C'){
               row += '<td class="bord" colspan="5" style="text-align:center;">---추가---</td>';
               
               row += '<td class="afterSbjt_CHG_COMDIV_CODE_NM">' + sbjt.CHG_COMDIV_CODE_NM + '</td>';
               row += '<td class="afterSbjt_COM_GRADE">' + sbjt.CHG_COM_GRADE + '</td>';
               row += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.CHG_SUBJECT_NM + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.CHG_SMT_NM === '1학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.CHG_SMT_NM === '2학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';               
               row += '<td>'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'</td>';

               row += '</tr>';
           }
           
           //삭제
           if(sbjt.CHG_FG === 'D'){

               row += '<td>' + sbjt.COMDIV_CODE_NM + '</td>';
               row += '<td>' + sbjt.COM_GRADE + '</td>';
               row += '<td class="text-start">' + sbjt.SUBJECT_NM + '</td>';
               row += '<td>' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
               row += '<td class="bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
               
               row += '<td class="bord deleteSbjt" colspan="5" style="text-align:center;">---삭제---</td>';
               
               row += '<td>'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'</td>';

               row += '</tr>';
           }
           
           //이수구분 변경
           if(sbjt.CHG_FG === 'U'){
        	   var comDivCodeSelect = "";
        	   
	       		var majorArray = ['UE010021', 'UE010022', 'UE010024'];
	    		var genArray = ['UE010011', 'UE010012'];	    	

               
               row += '<td>' + sbjt.COMDIV_CODE_NM + '</td>';
               row += '<td>' + sbjt.COM_GRADE + '</td>';
               row += '<td class="text-start">' + sbjt.SUBJECT_NM + '</td>';
               row += '<td>' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
               row += '<td class="bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
               
               row += '<td class="afterSbjt_CHG_COMDIV_CODE_NM_select">' + sbjt.CHG_COMDIV_CODE_NM + '</td>';
               row += '<td class="afterSbjt_COM_GRADE">' + sbjt.COM_GRADE + '</td>';
               row += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.CHG_SUBJECT_NM + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.CHG_SMT_NM === '1학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.CHG_SMT_NM === '2학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';               
               row += '<td>'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'</td>';

               row += '</tr>';
           }
           
           //변경없음
           if(sbjt.CHG_FG === 'N'){
               
               row += '<td>' + sbjt.COMDIV_CODE_NM + '</td>';
               row += '<td>' + sbjt.COM_GRADE + '</td>';
               row += '<td class="text-start">' + sbjt.SUBJECT_NM + '</td>';
               row += '<td>' + (sbjt.SMT_NM === '1학기' ? sbjt.CDT_NUM : '') + '</td>';
               row += '<td class="bord">' + (sbjt.SMT_NM === '2학기' ? sbjt.CDT_NUM : '') + '</td>';
               
               
               row += '<td class="afterSbjt_CHG_COMDIV_CODE_NM">' + sbjt.CHG_COMDIV_CODE_NM + '</td>';
               row += '<td class="afterSbjt_COM_GRADE">' + sbjt.COM_GRADE + '</td>';
               row += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.CHG_SUBJECT_NM + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.CHG_SMT_NM === '1학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.CHG_SMT_NM === '2학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';               
               row += '<td>'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'</td>';

           }
           row += '</tr>';
           sbjtListPc.append(row);
       });
       
       

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

</script>