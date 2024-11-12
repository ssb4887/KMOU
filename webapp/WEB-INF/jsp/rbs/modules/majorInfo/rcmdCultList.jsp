<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item"%>
<c:set var="itemOrderName" value="${submitType}_order" />
<itui:submitScript items="${itemInfo.items}"
	itemOrder="${itemInfo[itemOrderName]}" />
<script type="text/javascript">

var sptPsnKorList = [];

$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_sampleInputReset();
	var mjCd = '<c:out value="${param.majorIdx}"/>';
	var year = '<c:out value="${param.yearIdx}"/>';	
	getEduCorsCapbFgData(mjCd,year);
	
	var originUrl = window.location.href;
	history.replaceState({path : window.location.pathname},'', window.location.pathname);
	
	$("#refresh").click(function(){
		var confirmVal = confirm("조회 시 입력하시던 정보는 사라집니다. 조회하시겠습니까?");
		
		if(confirmVal){
			window.location.href = originUrl;
		}
	});	
	
	var sbjtKorNm = "${param.sbjtKorNm}";
	if(sbjtKorNm != ""){
		$("#sbjtKorNm").val(sbjtKorNm);
	}
	
	
	
	$("[id^=s_eduCorsCapbFg]").change( function() {		
		var i = this.id.substring(15);
		$("#eduCorsCapbFg"+i).val(this.value);
	});
	
	// 검색
	$("#<c:out value="${param.searchFormId}"/>").submit(function(){
		try {
			return fn_sampleInputReset();
		}catch(e){alert(e); return false;}
	});
	
	// 소속구분 변경시 대학 세팅
	$('#s_colgCd').change( function() {			
		getDeptData($('#s_colgCd').val());
	});
	// 소속대학 선택 시 학과 목록 선택값 변경	
	$('#s_fcltSustCd').change( function() {
		getMajorData($('#s_fcltSustCd').val());
	});
	
	$('#s_mjCd').change( function() {
		getRegisteredYear($('#s_mjCd').val());
		$('#mjNmKor').val($('#s_mjCd option:selected').text());
		$('#mjNmEng').val($('#s_mjCd option:selected').attr('deptnmeng'));
		$("#yearbox").show();
	});

	
	$("#graduationCnt, #collegeCnt").change(function() {
		$("#graduationRate").val( ($("#collegeCnt").val() / $("#graduationCnt").val() * 1000 / 10.0 ).toFixed(1) );
	});
	$("#jobCnt, #insuranceCnt").change(function() {
		$("#jobRate").val( ($("#insuranceCnt").val() / $("#jobCnt").val() * 1000 / 10.0).toFixed(1) );
	});
	
	// reset
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${param.inputFormId}"/>").reset();
			fn_sampleInputReset();
		}catch(e){alert(e); return false;}
	});
	<c:if test="${isAdmMode}">
	// cancel
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_cancel").click(function(){
		try {
			<c:choose>
				<c:when test="${param.dl == '1'}">
				self.close();
				</c:when>
				<c:otherwise>
				location.href="${elfn:replaceScriptLink(URL_LIST)}";
				</c:otherwise>
			</c:choose>
		}catch(e){return false;}
	});
	
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_trackCancel").click(function(){
		try {
			<c:choose>
				<c:when test="${param.dl == '1'}">
				self.close();
				</c:when>
				<c:otherwise>
				location.href="${elfn:replaceScriptLink(URL_TRACK_LIST)}";
				</c:otherwise>
			</c:choose>
		}catch(e){return false;}
	});
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_jobCancel").click(function(){
		try {
			<c:choose>
				<c:when test="${param.dl == '1'}">
				self.close();
				</c:when>
				<c:otherwise>
				location.href="${elfn:replaceScriptLink(URL_LIST)}";
				</c:otherwise>
			</c:choose>
		}catch(e){return false;}
	});
	
	</c:if>
	// 등록/수정
// 	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
// 		try {
// 			return fn_sampleInputFormSubmit();
// 		}catch(e){alert(e); return false;}
// 	});

// 	$(".fn_btn_submit").click(function () {
// 		var codeArray = [];
// 		var nameArray = [];
// 		var isDuplicate = false;
		
// 		$("#s_eduCorsCapbFg option[name='CMMN_CD']").each(function () {
// 			var code = $(this).val();
// 			var nm = $(this).text();
			
// 			codeArray.push(code);
// 			nameArray.push(nm);
// 		});
		
// 		for(var i = 0; i < codeArray.length; i++){
// 			var selectedBoxes = $("[class^='eduCorsCapbFg']").filter(function() {
// 				return $(this).val() = codeArray[i];
// 			});
// 			selectedBoxes.each(function () {
// 				var ordArray = [];
// 				var ord = $(this).parent().closest("tr").find("td:eq(3)");
// 				if(ordArray.includes(ord)){
// 					alert(nm + " 항목에 순서가 중복되어 있습니다. 순서를 변경해 주세요.");
// 					isDuplicate = true;
// 					return false;
// 				} else {
// 					ordArray.push(ord);
// 				}
// 			});
			
// 			if(isDuplicate) return false;
// 		}
		
		
// 		if(isDuplicate) return false;
		
// 		//return true;
// 	});
	
	// 소속 구분 검색 정보 불러오기
	<c:if test="${!empty param.is_clsfCd}">
			var clsfCd = '<c:out value="${param.is_clsfCd}"/>';
			$('#s_clsfCd').val(clsfCd);
			getDeptData(clsfCd);		
	</c:if>
		// 대학 검색 정보 불러오기
	<c:if test="${!empty param.is_colgCd}">
			var colgCd = '<c:out value="${param.is_colgCd}"/>';		
			$('#s_colgCd').val(colgCd);
			getMajorData(colgCd);
	</c:if>
		// 학과/전공 검색 정보 불러오기
	<c:if test="${!empty param.is_deptCd}">
			var deptCd = '<c:out value="${param.is_deptCd}"/>';
			$('#s_deptCd').val(deptCd);
	</c:if>
	
	// 인재상 정보 불러오기
	<c:if test="${!empty param.mjCd}">
			var mjCd = '<c:out value="${param.mjCd}"/>';
			var year = '<c:out value="${param.year}"/>';
			getSptPsnData(mjCd,year);
	</c:if>
	

	
});

//학부 교육과정 인재상 입력 행 삭제
function fn_deleteTp(){
	// cancel
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_cancel").click(function(){
		try {
			<c:choose>
				<c:when test="${param.dl == '1'}">
				self.close();
				</c:when>
				<c:otherwise>
				location.href="${elfn:replaceScriptLink(URL_LIST)}";
				</c:otherwise>
			</c:choose>
		}catch(e){return false;}
	});
}

function fun_delete_major(button){	
	
	var tempList = "${list}";
	
	/* alert(tempList.length); */
	
	/* var key = Object.keys(tempList[0])[0];
	alert(key); */
	
// 	var jsonArray = JSON.parse(tempList);
// 	var row = button.parentNode.parentNode;
// 	var rowIndex = row.rowIndex;
	
// 	jsonArray.splice(rowIndex,1);
	
// 	console.log(jsonArray);
	
	/* var jsonString = JSON.stringify(jsonArray);
	console.log(tempList); */
	
	/* <forEach var="listDt" items=tempList var Status="i">
		console.log(listDt);
	</forEach> */
	
	
	
	
	
	/* alert(rowIndex); */
	var row = button.parentNode.parentNode;
 	row.parentNode.removeChild(row);
 	
 	
 	
 	
		
	
// 	tempList.splice(row,1);
	
// 	alert("변경전 " +  tempList);
	/* console.log(${list}.delete(row)); */
	
// 	tempList.splice(row,1);
	
// 	alert("변경후 " +  tempList);
	
}


//학부 교육과정 인재상 입력 행 추가
function fn_sampleInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	
	var inputList = ["sptPsnKor"];
	var inputName = ["전문인(한글)"];
	var docList = [];
	
	for(var p = 0; p < inputList.length; p++){
		
		
		var value = inputList[p]
		
		for(var i = 0; i <= $("#dtLength").val() ; i++){
			
			var sbjtFgNm = ($('#sbjtFgNm'+i).val());
			var openShyrNm = ($('#openShyrNm'+i).val());
			var shtmNm = ($('#shtmNm'+i).val());
			var openSustMjNm = ($('#openSustMjNm'+i).val());
			var courseNo = ($('#courseNo'+i).val());
			var sptPsnKor = ($('#s_sptPsnKor'+i).val());
			
			var cons = sbjtFgNm + openShyrNm + openSustMjNm + courseNo + sptPsnKor;
			
			docList.push(cons);
			
		}
		
		for(var i = 0; i <= $("#dtLength").val() ; i++){
			
			var id = '#s_' + value + i;
			var docId = '#docId' + ((docList.length)-1);
			console.log(docList.length);
			
			for (var k= 0 ; k < docList.length ; k++) {
				if ( i != k) {
					if(i != docList.length) {						
						/* console.log("i :::" + i);
						console.log("k :::" + k);
						console.log("docList.length :::" + docList.length);
						console.log("docList[i] :::" + docList[i]);
						console.log("docList[k] :::" + docList[k]); */						
						if (docList[i] == null || docList[k] == null) {
							continue;
						}
						if (docList[k] == docList[i]){							
							alert("중복된 인재상(한글)을 입력할 수 없습니다");
							$(id).focus();
							return false;	
						}							
												
					} else {
						break;
					}
				}				
			}
		
			if ($(id).val() != null) {
				if($(id).val().trim() === ''){
					alert("인재상(한글) 항목들은 필수 입력값입니다.");
					$(id).focus();
					return false;
				}				
			}
			
		}	
		
		
	};
	
	fn_createMaskLayer();	
	// 여기서 값을 추가해주도록 변경
	
	const urlParams = new URL(location.href).searchParams;
	const majorIdx = urlParams.get('majorIdx');
	const yearIdx = urlParams.get('yearIdx');
	
	console.log("majorIdx", majorIdx)
	console.log("yearIdx", yearIdx)	
	
	$("#majorIdx").attr("value", majorIdx);
	$("#yearIdx").attr("value", yearIdx);
	return true;
}

function fn_sampleInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
}	
  // 대학 selectbox 세팅
function getCollegeData() {
	try {		
		$.ajax({
			url: '/web/haksaCode/getCollegeList.json?mId=141',
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


// 학과/학부 select 변경시
function getDeptData(clsfCd) {
	try {
		var colgCd = $('#s_colgCd').val();
		$.ajax({
			url: '/web/haksaCode/getDeptList.json?mId=141',
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
		var fcltSustCd = $('#s_fcltSustCd').val();
		var fcltSustNm = $('#s_fcltSustCd option:selected').text();
		var fcltSustEngNm = $('#s_fcltSustCd option:selected').attr('deptnmeng');
		$.ajax({
			url: '/web/haksaCode/getMajorList.json?mId=141',
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
 
// 인재상 리스트 불러오기
function getSptPsnData(mjCd,year) {
	
	var dtLength = Number($("#dtLength").val());
	
	try {		
		$.ajax({
			url: '/web/haksaCode/getSptPsnList.json?mId=192',
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
				sptPsnKorList = data.haksaCode;
				for(var j=0; j <= dtLength; j++){					
					
		  			for(var i=0; i < sptPsnKorList.length; i++){
		  				
		  				if ($("#sptPsnKor"+j).val() == sptPsnKorList[i].SPT_PSN_KOR) {
		  					$('#s_sptPsnKor'+j).append("<option value='" + sptPsnKorList[i].SPT_PSN_KOR + "' selected>" + sptPsnKorList[i].SPT_PSN_NM  + "</option>");		  					
		  				} else {
		  					$('#s_sptPsnKor'+j).append("<option value='" + sptPsnKorList[i].SPT_PSN_KOR + "'>" + sptPsnKorList[i].SPT_PSN_NM + "</option>");	  						
		  				}		  				
			    	}		  			
		  			
		  			if ($("#sptPsnKor"+j).val() == $('#s_sptPsnKor'+j).val()) {
		  				$('#sptPsnKor'+j).val($('#s_sptPsnKor'+ j + 'option:selected').text());
		  			}		  			 
				}
			}).fail(function(data, textStatus, errorThrown){
			      /*pass*/
			      console.log('error 발생 : ' + errorThrown);
			});
	} catch(e){
		console.log(e);
	}	
}
	
	
	
	

 
function getRegisteredYear(value){
	$.ajax({
		url: '${URL_REGISTERED_YEAR}',
	    type: 'GET',
	    crossDomain: true,
	    data: {
	    	mjCd : value
  		},
  		success:function(data){
  			$("#year option").prop('disabled', false);
  			var registeredYearList = data.registeredYearList
  			for(var i=0; i < registeredYearList.length; i++){
  	  			$("#year option").each(function(){
  	  			var optionVal = $(this).val();
  					if(optionVal === registeredYearList[i].YY){
  						$(this).prop('disabled', true).text(optionVal + "(등록됨)");
  					}else{
  						$(this).prop('disabled', false).text(optionVal);
  					}
  				})
  			};

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
				haksaRcmdCultList = data.haksaRcmdCult;		
				for(var j=0; j <= dtLength; j++){
				
		  			for(var i=0; i < haksaRcmdCultList.length; i++){
		  				
		  				if( j == 0) {
		  					$('#s_eduCorsCapbFg').append("<option name='CMMN_CD' value='" + haksaRcmdCultList[i].CMMN_CD + "'>" + haksaRcmdCultList[i].KOR_NM + "</option>");	
		  					
		  					var isEduCorsCapbFg = "${param.is_eduCorsCapbFg}";
		  					if(isEduCorsCapbFg != ""){
		  						$("#s_eduCorsCapbFg option[value='" + isEduCorsCapbFg + "']").prop("selected", true);
		  					}
		  				}	  					
	  					
	  					if ($("#eduCorsCapbFg"+j).val() == haksaRcmdCultList[i].CMMN_CD) {
		  					$('#s_eduCorsCapbFg'+j).append("<option name='CMMN_CD'" +j + "' value='" + haksaRcmdCultList[i].CMMN_CD + "' selected>" + haksaRcmdCultList[i].KOR_NM  + "</option>");		  					
		  				} else {
		  					$('#s_eduCorsCapbFg'+j).append("<option name='CMMN_CD'" +j + "' value='" + haksaRcmdCultList[i].CMMN_CD + "'>" + haksaRcmdCultList[i].KOR_NM + "</option>");	  						
		  				}
	  					
			    	}
		  			if ($("#eduCorsCapbFg").val() == $('#s_eduCorsCapbFg').val()) {
		  				$('#eduCorsCapbFg').val($('#s_eduCorsCapbFg' + 'option:selected').text());
		  			}
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