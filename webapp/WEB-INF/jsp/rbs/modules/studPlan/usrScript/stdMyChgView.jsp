<script>
let bookmarkList;
let duplSbjtArr = [];
let beforeSbjtData = [];
let sbjtArray = new Array();
	$(function(){	
		
		// 저장된 교과목 세팅
		$.ajax({
			url: '/web/studPlan/getMyChgSbjtList.do?mId=36',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'POST',
			data: JSON.stringify({
				sdmCd : "${INFO.SDM_CD}", 
				revsnNo : "${INFO.REVSN_NO}", 
				beforeRevsnNo : "${INFO.REVSN_NO}" - ("${INFO.STATUS}" == '10' ? 1 : 0)	//임시저장 상태이면 이전 교과목을 불러와야 보여진다.
				}),
			success:function(data){
				//변경전 교과목 전역변수 선언
				beforeSbjtData = data.SBJTLIST;
				
				setSavedChgSbjtToHidden(data.CHG_SBJTLIST);	//hidden값 세팅
				sbjtDisplay(beforeSbjtData);			//교육과정표		
				chgSbjtDiffDisplay();		//교육과정 신구대비표


			}
		});		
		
		//상태에 따른 하단의 진행도 세팅
		switch("${INFO.STATUS}"){
			case "20" : $("#20").addClass('on'); break; 
			case "25" : $("#25").addClass('on'); break; 
			case "30" : $("#30").addClass('on'); break; 
			case "33" : ["#20", "#25", "#30", "#40", "#100"].forEach(function(id) { $(id).hide(); }); $("#33").show(); $("#rjtResn").val("${INFO.RJT_RESN}"); break;
			case "40" : $("#40").addClass('on'); break;
			case "50" : $("#40").addClass('on'); break;
			case "60" : $("#40").addClass('on'); break;
			case "70" : $("#40").addClass('on'); break;
			case "43" : ["#20", "#25", "#30", "#40", "#100"].forEach(function(id) { $(id).hide(); }); $("#43").show(); $("#rjtResn").val("${INFO.EDU_CENTER_JUDG_OPIN}"); break;
			case "53" : ["#20", "#25", "#30", "#40", "#100"].forEach(function(id) { $(id).hide(); }); $("#53").show(); $("#rjtResn").val("${INFO.DEPART_JUDG_OPIN}"); break;
			case "63" : ["#20", "#25", "#30", "#40", "#100"].forEach(function(id) { $(id).hide(); }); $("#63").show(); $("#rjtResn").val("${INFO.AFFAIR_COMMITTEE_JUDG_OPIN}"); break;
			case "100" : $("#100").addClass('on'); break; 
		}
	});

	
    
</script>



<script>
// ==========================================================================================================교수 검색=================================================================				
	
	//교과목 테이블 렌더링
	function renderSbjtTable(g, grade, chgFg){
		
		if(g != null){			
			
			// Html form
			var essHtml = "";		//전공필수
			var selHtml = "";		//전공선택
			var basHtml = "";		//전공기초
			var genEssHtml = "";	//교양필수
			var genSelHtml = "";	//교양선택
			
			// 구분 rowspan
			var essRowSpan = 0;
			var selRowSpan = 0;
			var basRowSpan = 0;
			var genEssRowSpan = 0;
			var genSelRowSpan = 0;
			
			for(var i=0; i < g.length; i++){
				switch (g[i][chgFg + 'COMDIV_CODE']) {
				    case 'UE010021': essRowSpan++; break;
				    case 'UE010022': selRowSpan++; break;
				    case 'UE010024': basRowSpan++; break;
				    case 'UE010011': genEssRowSpan++; break;
				    case 'UE010012': genSelRowSpan++; break;
				}
			}
			
		    basHtml    = processSbjtHtml(g, 'UE010021', '전공필수', essRowSpan, chgFg);
		    essHtml    = processSbjtHtml(g, 'UE010022', '전공선택', selRowSpan, chgFg);
		    selHtml    = processSbjtHtml(g, 'UE010024', '전공기초', basRowSpan, chgFg);
		    genEssHtml = processSbjtHtml(g, 'UE010011', '교양필수', genEssRowSpan, chgFg);
		    genSelHtml = processSbjtHtml(g, 'UE010012', '교양선택', genSelRowSpan, chgFg);

		    var html = basHtml + essHtml + selHtml + genEssHtml + genSelHtml;
			
			$("#grade"+grade).empty();
			$("#grade"+grade).append(html);
			
			// 전공병합
			rowSpanFnc(".UE010021_"+grade);
			rowSpanFnc(".UE010022_"+grade);
			rowSpanFnc(".UE010024_"+grade);
			rowSpanFnc(".UE010011_"+grade);
			rowSpanFnc(".UE010012_"+grade);

		}else{
			$("#grade"+grade).empty();
		}		
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
	
	
	//교과목 공통부분 html
	function processSbjtHtml(subjects, groupCode, groupName, rowspan, chgFg) {
	    var html = '';
	    var totalPnt = 0;
	    var beforePnt = 0;
	    var afterPnt = 0;
	    var thTagFg = 0;
	    
	    
	    for (var i = 0; i < subjects.length; i++) {
	        if (subjects[i][chgFg+'COMDIV_CODE'] == groupCode) {
	        	console.log(subjects[i][chgFg+'SUBJECT_NM'])
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
	
	/*학생설계전공 수정 및 상담 마감(액션이 추가될 수 있음)*/	
	function updateStatus(status, msg){
		if(confirm(msg + "하시겠습니까?")){
	        let json_data = {};
	        getInputDataHasName(json_data);
	        
			$.ajax({
				url: '/web/studPlan/updateStatus.do?mId=36',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'POST', 
				data: JSON.stringify({
					SDM_CD : json_data.SDM_CD,
					REVSN_NO : json_data.REVSN_NO,
					STATUS : status
				}),
				success:function(data){				
					alert(data.RTN_MSG);
					if(data.RESULT == "DONE"){
						/*수정화면으로 이동*/
						var form = document.frmView;
						$("#SDM_CD").val(json_data.SDM_CD);
						$("#REVSN_NO").val(json_data.REVSN_NO);
						form.action = '/web/studPlan/stdChgModify.do';
						form.submit();
					}
				}
			});	
		}
	}
	
	/*학생설계전공 변경등록*/	
	function changeInput(sdmCd, revsnNo){
		if(confirm("해당 학생설계전공을 변경등록 하시겠습니까?")){
	        
			$.ajax({
				url: '/web/studPlan/stdChangeInput.do?mId=36',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'POST', 
				data: JSON.stringify({
					SDM_CD : sdmCd,
					REVSN_NO : revsnNo
				}),
				success:function(data){							
					if(data.RESULT == "DONE"){
						alert(data.RESULT);
						/*수정화면으로 이동*/
						var form = document.frmView;
						$("#SDM_CD").val(data.SDM_CD);
						$("#REVSN_NO").val(data.REVSN_NO);
						form.action = '/web/studPlan/stdChgModify.do';
						form.submit();
					}else{
						alert("변경등록에 실패하였습니다. 관리자에게 문의해주세요");
						console.log(data.RESULT);
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
           
           row += '<td>';
           switch(sbjt.CHG_FG){
           		case 'C': row += '추가'; break; 
           		case 'U': row += '이수구분변경'; break; 
           		case 'D': row += '삭제'; break; 
           		case 'N': row += '변경없음'; break; 
           }
           row += '</td>';

           //추가
           if(sbjt.CHG_FG === 'C'){
               row += '<td class="bord" colspan="5">---추가---</td>';
               
               row += '<td class="afterSbjt_CHG_COMDIV_CODE_NM">' + sbjt.CHG_COMDIV_CODE_NM + '</td>';
               row += '<td class="afterSbjt_COM_GRADE">' + sbjt.CHG_COM_GRADE + '</td>';
               row += '<td class="afterSbjt_CHG_SUBJECT_NM text-start">' + sbjt.CHG_SUBJECT_NM + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM">' + (sbjt.CHG_SMT_NM === '1학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';
               row += '<td class="afterSbjt_CHG_SMT_NM bord">' + (sbjt.CHG_SMT_NM === '2학기' ? sbjt.CHG_CDT_NUM : '') + '</td>';               
               row += '<td><input type="text" class="form-control" id="CHG_RESN" value="'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'" readonly></td>';

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
               
               row += '<td><input type="text" class="form-control" id="CHG_RESN" value="'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'" readonly></td>';

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
               row += '<td><input type="text" class="form-control" id="CHG_RESN" value="'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'" readonly></td>';

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
               row += '<td><input type="text" class="form-control" id="CHG_RESN" value="'+(sbjt.CHG_RESN == undefined ? '' : sbjt.CHG_RESN)+'" readonly></td>';

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