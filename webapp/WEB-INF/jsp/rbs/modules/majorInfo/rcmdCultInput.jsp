<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_sampleInputReset();
	getCollegeData();
	
	var originUrl = window.location.href;
	history.replaceState({path : window.location.pathname},'', window.location.pathname);
	
	$("#refresh").click(function(){
		var confirmVal = confirm("조회 시 입력하시던 정보는 사라집니다. 조회하시겠습니까?");
		
		if(confirmVal){
			window.location.href = originUrl;
		}
	});	
	
	var mjCd = '<c:out value="${param.majorIdx}"/>';
	var year = '<c:out value="${param.yearIdx}"/>';	
	var year2 = "${param.year}";
	var mjCd2 = "${param.mjCd}";
	getEduCorsCapbFgData(mjCd,year);
	
	var sbjtKorNm = "${param.sbjtKorNm}";
	if(sbjtKorNm != ""){
		$("#sbjtKorNm").val(sbjtKorNm);
	}
	
	// 검색
	$("#<c:out value="${param.searchFormId}"/>").submit(function(){
		try {
			return fn_sampleInputReset();
		}catch(e){alert(e); return false;}
	});
	
	$("input[name='select']").click(function () {
		var isDuplicate = false;
		var idx = $(this).attr("data-idx");
		var edu = $("#s_eduCorsCapbFg").val();
		var courseNo = $("#courseNo_" + idx).text();
		var nmKor = $("#sbjt_" + idx).text();
		
		if(edu == ""){
			alert("역량구분을 선택해 주세요.");
			$("#s_eduCorsCapbFg").focus();
			return false;
		}
		
		$.ajax({
			url: '/RBISADM/menuContents/web/majorInfo/checkRcmdCult.do?mId=145',
			contentType:'application/json',
			type: 'POST',
			data: JSON.stringify({ 
			    courseNo : courseNo,
			    edu : edu,
			    yy : year2,
			    mjCd : mjCd2
			}),
			success: function(data){
				count = data.result;
				
				if(parseInt(count, 10) != 0){
					alert(nmKor + " 은/는 추천균형교양교과목에 이미 등록되어 있습니다.");
					isDuplicate = true;
				}
			}
		});
		
		setTimeout(function () {
			if(isDuplicate) $("input[name='select']:checked").prop("checked", false);
		}, 200);
	});
	
	
	$("#saveSub").click(function() {
		var courseNos = [];
		var nmKors = [];
		var nmEngs = [];
		var pnts = [];
		var theos = [];
		var pracs = [];
		var edu = $("#s_eduCorsCapbFg").val();
		
		if($("input[name='select']:checked").length == 0){
			alert("추가할 교과목을 선택해 주세요.");
			return false;
		}
		
		if(edu == ""){
			alert("역량구분을 선택해 주세요.");
			$("#s_eduCorsCapbFg").focus();
			return false;
		}
		
		$("input[name='select']:checked").each(function () {
			var idx = $(this).attr("data-idx");
			
			var courseNo = $("#courseNo_" + idx).text();
			var nmKor = $("#sbjt_" + idx).text();
			var nmEng = $("#SBJT_ENG_NM_" + idx).val();
			var pnt = $("#pnt_" + idx).text();
			var theo = $("#theo_" + idx).text();
			var prac = $("#prac_" + idx).text();
			
			courseNos.push(courseNo);
			nmKors.push(nmKor);
			nmEngs.push(nmEng);
			pnts.push(pnt);
			theos.push(theo);
			pracs.push(prac);
			
		});
		
		fn_insertCultCors(courseNos, nmKors, nmEngs, pnts, theos, pracs, edu, year2, mjCd2);
	});
	
// 	$("#saveSub").click(function(){
// 		var jsonString;
// 		var jsonArray = [];		
// 		var flag = false;
// 		var state = false;
		
// 		$('input:checkbox[name=select]').each(function (index) {			
// 			if($(this).is(":checked")==true) {
// 				var obj = {};	
// 				$(this).closest('tr').find('input').each(function(idx){		
// 					if(idx > 0) {						
// 						$('#topfieldset dl select option:selected').each(function(){
							
// 							obj['EDU_CORS_CAPB_FG'] = $(this).val();
// 							if($(this).val() == '') {
// 								flag = true;
// 							}
// 						})
// 						obj[$(this).attr("name")] = $(this).val();						
// 					}
// 				});
				
// 				if(flag) {
// 					alert("역량구분을 선택해 주세요");
// 					$(this).focus();
// 					return false;
// 				}
				 
// 				console.log(JSON.stringify(obj));
// 				jsonArray.push(obj);
// 			}
			
// 		})
		
// 		try {		
// 			$.ajax({
// 				url: '${URL_RCMD_CULT_INPUT}&dl=1',
// 			    type: 'POST',
// 			    crossDomain: true,
// 			    contentType: 'application/json',
// 			    data: 
// 			    	JSON.stringify({"data" : jsonArray})						
// 			    ,beforeSend:function(request){
// 			    	request.setRequestHeader('Ajax','true');
// 			    }
// 				}).done(function (data, textStatus, error) {
// 					console.log("ajax 호출 완료" + data);
					
// 					if(data == null) {
// 						alert("ok");
						
// 					} else {
						
// 						var errObj = JSON.parse(error.responseText);
// 						alert(errObj.string);
						
// 					}
					
					
					
// 					/* window.close();
// 					window.opener.location.reload(); */
					
// 				}).fail(function(data, textStatus, error){
// 				      /*pass*/
// 				      /* alert("이미 존재하는 교과목과 전문인 입니다."); */
// 				      alert(error);
// 				      console.log('error 발생 :@@@@@@@ ' + data+ textStatus + error);				      
// 				      return false;
// 				});
// 			state = true;
// 		} catch(e){
// 			console.log(e);
// 			return false;
// 		}
		
// 		var index = $("#tpSubContent > tbody > tr").length;
// 		if(($("#tpSubContent > tbody > tr").length - $("#tpSubContent tr input[name=chkbox]:checked").length) < 1){
// 			alert("최소 하나의 교과목 행이 있어야 합니다.")
// 			return false;
// 		}
// 		if($("#tpSubContent tbody tr input[name=select]:checked").length == 0){
// 			alert("추가할 교과목을 선택해주세요.");
// 			return false;
// 		}
		
		
// 		/* if (state) {
// 			window.close();
// 			window.opener.location.reload();	
// 		} */		
// 	});
	
	
});

function fn_sampleInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
}	

function fn_checkCultCors(courseNo, edu, year, mjCd){
	var count;
	try {
		$.ajax({
			url: '/RBISADM/menuContents/web/majorInfo/checkRcmdCult.do?mId=145',
			contentType:'application/json',
			type: 'POST',
			data: JSON.stringify({ 
			    courseNo : courseNo,
			    edu : edu,
			    yy : year,
			    mjCd : mjCd
			}),
			success: function(data){
				count = data.result;
				alert("data ==== " + JSON.stringify(data));
			}
		});
	} catch(error){
		
	} finally {
		alert("c12132121unt === " + count);
		return count;
	}
}

function fn_insertCultCors(courseNos, nmKors, nmEngs, pnts, theos, pracs, edu, year, mjCd){
	$.ajax({
		url: '/RBISADM/menuContents/web/majorInfo/insertRcmdCult.do?mId=145',
		contentType:'application/json',
		type: 'POST',
		data: JSON.stringify({ 
		    courseNo : courseNos.toString(),
		    nmKor : nmKors.toString(),
		    nmEng : nmEngs.toString(),
		    pnt : pnts.toString(),
		    theo : theos.toString(),
		    prac : pracs.toString(),
		    edu : edu,
		    yy : year,
		    mjCd : mjCd
		}),
		success: function(data){
			alert("추천균형교양교과목이 추가 되었습니다.");
			$("input[name='select']:checked").prop("checked", false);
			$("#s_eduCorsCapbFg option[value=]").prop("selected", true);
			window.close();
			window.opener.location.href = "${URL_RCMD_CULT_LIST}&mjCd=${mjCd}&year=${year}";
			/* window.opener.location.reload(); */
		}
	});
}


  // 대학 selectbox 세팅
function getCollegeData() {
	try {		
		$.ajax({
			url: '/web/haksaCode/getCollegeList.json?mId=192',
		    type: 'POST',
		    crossDomain: true,
		    beforeSend:function(request){
		    	request.setRequestHeader('Ajax','true');
		    }
			}).done(function (data, textStatus, xhr) {
				$("#s_fcltSustCd").children('option:not(:first)').remove();
				$("#s_mjCd").children('option:not(:first)').remove();
				for(var idx in data.haksaCode){
		        	$('#s_colgCd').append("<option value='" + data.haksaCode[idx].DEPT_CD + "' deptNmEng='" + data.haksaCode[idx].DEPT_ENG_NM + "'>" + data.haksaCode[idx].DEPT_KOR_NM + "</option>");
		    	}
			}).fail(function(data, textStatus, errorThrown){
			      /*pass*/
			      console.log('error 발생 : ' + errorThrown);
			});
	} catch(e){
		console.log(e);
	}
}


// 대학 select 변경시
function getDeptData(clsfCd) {
	try {
		$("#yearbox").hide();
		var colgCd = $('#s_colgCd').val();
		$.ajax({
			url: '/web/haksaCode/getDeptList.json?mId=192',
		    type: 'POST',
		    crossDomain: true,
		    data: {
		    	colgCd : colgCd
	  		},
		    beforeSend:function(request){
		    	request.setRequestHeader('Ajax','true');
		    }
			}).done(function (data, textStatus, xhr) {
				$("#s_fcltSustCd").children('option:not(:first)').remove();
				for(var idx in data.haksaCode){
		        	$('#s_fcltSustCd').append("<option value='" + data.haksaCode[idx].DEPT_CD + "' deptNmEng='" + data.haksaCode[idx].DEPT_ENG_NM + "'>" + data.haksaCode[idx].DEPT_KOR_NM + "</option>");
		    	}
				$('#s_colgCd').val(colgCd);
			}).fail(function(data, textStatus, errorThrown){
			      /*pass*/
			      console.log('error 발생 : ' + errorThrown);
			});
	} catch(e){
		console.log(e);
	}
}
 
// 학과/학부 select 변경시
function getMajorData(fcltSustCd) {
	try {		
		$("#yearbox").hide();
		var fcltSustCd = $('#s_fcltSustCd').val();
		var fcltSustNm = $('#s_fcltSustCd option:selected').text();
		var fcltSustEngNm = $('#s_fcltSustCd option:selected').attr('deptnmeng');
		$.ajax({
			url: '/web/haksaCode/getMajorList.json?mId=192',
		    type: 'POST',
		    crossDomain: true,
		    data: {
		    	fcltSustCd : fcltSustCd
	  		},
		    beforeSend:function(request){
		    	request.setRequestHeader('Ajax','true');
		    }
			}).done(function (data, textStatus, xhr) {
				$("#s_mjCd").children('option:not(:first)').remove();
				// 학과/학부의 정보가 전공일시
				if(data.haksaCode.length == 0){					
					 if(fcltSustNm != '전체'){
						$('#s_mjCd option:first').val(fcltSustCd);
						$('#mjNmKor').val(fcltSustNm);
						$('#mjNmEng').val(fcltSustEngNm);
						getRegisteredYear(fcltSustCd);
						$("#yearbox").show();
						$("#s_mjCd").hide();
					 }else{
						$('#s_mjCd option:first').val("");
					 }
				}else{
					for(var idx in data.haksaCode){
			        	$('#s_mjCd').append("<option value='" + data.haksaCode[idx].DEPT_CD + "' deptNmEng='" + data.haksaCode[idx].DEPT_ENG_NM + "'>" + data.haksaCode[idx].DEPT_KOR_NM + "</option>");
			    	}					
				}		
				$('#s_fcltSustCd').val(fcltSustCd);


			}).fail(function(data, textStatus, errorThrown){
			      /*pass*/
			      console.log('error 발생 : ' + errorThrown);
			});
	} catch(e){
		console.log(e);
	}
 }
function getRegisteredYear(value){
	$("#year option").each(function(){				
  			var optionVal = $(this).val();
  			if(optionVal == ""){
  				optionVal = '연도 선택';
  			}
			$(this).prop('disabled', false).text(optionVal);
		});	
	$.ajax({
		url: '${URL_REGISTERED_YEAR}',
	    type: 'GET',
	    crossDomain: true,
	    data: {
	    	mjCd : value
  		},
  		success:function(data){
  			$("#year option").prop('disabled', false);
  			var registeredYearList = [];
  			
  			for(var i=0; i < data.registeredYearList.length; i++){
  				registeredYearList.push(data.registeredYearList[i].YY)
  			}
  			
  			$("#year option").each(function(){
  	  			var optionVal = $(this).val();
  					if(registeredYearList.includes(optionVal)){
  						$(this).prop('disabled', true).text(optionVal + "(등록됨)");
  					}else{
//   						$(this).prop('disabled', false).text(optionVal);
  					}
  			});



  		},
  		error:function(e){
  			alert(JSON.stringify(e))
  		}
		})

}

//역량구분 리스트 불러오기
function getEduCorsCapbFgData(mjCd,year) {
	
	var dtLength = Number($("#dtLength").val());
	
	try {		
		$.ajax({
			url: '/web/haksaCode/getEduCorsCapbFgList.json?mId=192',
		    type: 'POST',
		    crossDomain: true,
		    data: {
		    	year : year,
		    	mjCd : mjCd
	  		},
		    beforeSend:function(request){
		    	request.setRequestHeader('Ajax','true');
		    }
			}).done(function (data, textStatus, xhr) {
				haksaRcmdCult = data.haksaRcmdCult;	
				console.log("haksaRcmdCult" + haksaRcmdCult);
				
				for(var i = 0; i < haksaRcmdCult.length; i++){
					$('#s_eduCorsCapbFg').append("<option name='CMMN_CD' value='" + haksaRcmdCult[i].CMMN_CD + "'>" + haksaRcmdCult[i].KOR_NM + "</option>");	
				}
				
				var isEduCorsCapbFg = "${param.is_eduCorsCapbFg}";
				if(isEduCorsCapbFg != ""){
					$("#s_eduCorsCapbFg option[value='" + isEduCorsCapbFg + "']").prop("selected", true);
				}
				
			})
			.fail(function(data, textStatus, errorThrown){
			      /*pass*/
			      alert(JSON.stringify(e))
			});
	} catch(e){
		console.log(e);
	}	
}
</script>