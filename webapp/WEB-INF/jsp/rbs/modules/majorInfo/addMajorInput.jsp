<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_sampleInputReset();
	
	
	var originUrl = window.location.href;
	history.replaceState({path : window.location.pathname},'', window.location.pathname);
	
	$("#refresh").click(function(){
		var confirmVal = confirm("조회 시 입력하시던 정보는 사라집니다. 조회하시겠습니까?");
		
		if(confirmVal){
			window.location.href = originUrl;
		}
	});	
	
	var majorCd = '<c:out value="${param.majorIdx}"/>';
	var year = '<c:out value="${param.yearIdx}"/>';	
	getFieldData(majorCd);
	

	// 검색
	$("#<c:out value="${param.searchFormId}"/>").submit(function(){
		try {
			return fn_sampleInputReset();
		}catch(e){alert(e); return false;}
	});
	
	
	$("#headbox").hide();
	$("#header").hide();
	$("#closebtn").hide();
	$("#lunit").hide();
	
	$("[id^=preSelect]").click(function(){
		
		$(".noitem").remove();
		
		var idValue = $(this).attr('id');
		var indexNumber = idValue.replace("preSelect",'');
		
		var index = $("#eduList > div").length;
		
		
		var grade = $("#grade" + indexNumber).val();
		var year = $("#year" + indexNumber).val();
		var smt = $("#smt" + indexNumber).val();
		var openDeptCd = $("#openDeptCd" + indexNumber).val();
		var openDeptNm = $("#openDeptNm" + indexNumber).val();
		var subjectNm = $("#subjectNm" + indexNumber).val();
		var subjectCd = $("#subjectCd" + indexNumber).val();
		var fieldCd = $("#fieldCd" + indexNumber).val();
		var cdtNum = $("#cdtNum" + indexNumber).val();
		
		var smtNm = $("#smtNm" + indexNumber).val();
		var sinbunCd = $("#sinbunCd" + indexNumber).val();
		
		var comdivCd = $("#comdivCd" + indexNumber).val();
		var comdivNm = $("#comdivNm" + indexNumber).val();
		var lastModiId = $("#lastModiId" + indexNumber).val();
		var lastModiDate = $("#lastModiDate" + indexNumber).val();
		
		var id = subjectCd + '_' + openDeptCd + '_' + year + '_' + grade + '_' + smt;
		var checkId = $("#"+ id).length;
		if(checkId != '0'){
			alert("해당 교과목은 이미 추가되었습니다.");
			return false;
		}
		console.log(majorCd);
		var innerHtml = "";
		
		//innerHtml += '<tr><td><button type="button" id="delete' + (index -1) + '" data-idx="' + (index -1) + '" class="btn-m02 btn-color02">삭제</button></td>'
		//innerHtml += '<tr><td><input type="checkbox" class="checkbox-type01" name="chkbox"></td>'
		innerHtml += '<div class="plan_list" id="' + id + '">';
		innerHtml += '<ul>';
		innerHtml += '<li class="title"><span><input type="checkbox" id="select' + index + '" name="select" title="선택" data-id="' + id + '" value=""></span>' + subjectNm + '(' + subjectCd + ')' + '</li>';
		innerHtml += '<li>';
		innerHtml += '<dl>';
		innerHtml += '<dt>개설 년도</dt>';
		innerHtml += '<dd>' + year + '</dd>';
		innerHtml += '</dl>';
		innerHtml += '<dl>';
		innerHtml += '<dt>개설학년</dt>';
		innerHtml += '<dd>' + grade + '</dd>';
		innerHtml += '</dl>';
		innerHtml += '<dl>';
		innerHtml += '<dt>학기구분</dt>';
		innerHtml += '<dd>' + smtNm + '</dd>';
		innerHtml += '</dl>';
		innerHtml += '<dl>';
		innerHtml += '<dt>학점</dt>';
		innerHtml += '<dd>' + cdtNum + '</dd>';
		innerHtml += '</dl>';
		innerHtml += '</li>';
		innerHtml += '</ul>';
		innerHtml += '<div>';
		innerHtml += '<button type="button" onclick="delCard(`' + id + '`);">삭제</button>';
		innerHtml += '</div>';
		innerHtml += '<input type="hidden" value="'+ majorCd +'" name="MAJOR_CD" id="majorCd' + index + '">';
		innerHtml += '<input type="hidden" value="'+ comdivCd +'" name="COMDIV_CODE" id="comdivCd' + index + '">';
		innerHtml += '<input type="hidden" value="'+ comdivNm +'" name="COMDIV_CODE_NM" id="comdivNm' + index + '">';
		innerHtml += '<input type="hidden" value="'+ grade +'" name="COM_GRADE" id="grade' + index + '">';
		innerHtml += '<input type="hidden" value="'+ year +'" name="YEAR" id="year' + index + '">';
		innerHtml += '<input type="hidden" value="'+ smt +'" name="SMT" id="smt' + index + '">';
		innerHtml += '<input type="hidden" value="'+ smtNm +'" name="SMT_NM" id="smtNm' + index + '">';
		innerHtml += '<input type="hidden" value="'+ cdtNum +'" name="CDT_NUM" id="cdtNum' + index + '">';
		innerHtml += '<input type="hidden" value="'+ subjectNm +'" name="SUBJECT_NM" id="subjectNm' + index + '">';
		innerHtml += '<input type="hidden" value="'+ subjectCd +'" name="SUBJECT_CD" id="subjectCd' + index + '">';
		innerHtml += '<input type="hidden" value="'+ openDeptCd +'" name="DEPT_CD" id="openDeptCd' + index + '">';
		innerHtml += '<input type="hidden" value="'+ openDeptNm +'" name="DEPT_NM" id="openDeptNm' + index + '">';
		innerHtml += '</div>';
		
		
		
		
		$("#eduList").append(innerHtml);
	});
	
	/* 직접입력 */
	$(".addDir").click(function(){
		
		
		$(".noitem").remove();
		
		var index = $("#eduList > div").length + 100;
		
		
		var grade = $("#gradeDir").val();
		var year = $("#yearDir").val();
		var smt = $("#smtDir").val();
		var subjectNm = $("#sbjtNmDir").val();
		var subjectCd = $("#sbjtCdDir").val();
		var cdtNum = $("#cdtNumDir").val();
		
		var smtNm = "";
		
		if(smt == 'GH0210'){
			smtNm = "1학기";
		} else if(smt == 'GH0211'){
			smtNm = "여름계절학기";
		} else if(smt == 'GH0220'){
			smtNm = "2학기";
		} else if(smt == 'GH0221'){
			smtNm = "겨울계절학기";
		}
		
		var openDeptCd = $("#openDeptCd" + index).val();
		var openDeptNm = $("#openDeptNm" + index).val();
		
		var sinbunCd = $("#sinbunCd" + index).val();
		
		var fieldCd = $("#fieldCd" + index).val();
		var comdivCd = $("#comdivCd" + index).val();
		var comdivNm = $("#comdivNm" + index).val();
		
		var id = subjectCd + '_' + openDeptCd + '_' + year + '_' + grade + '_' + smt;
		var checkId = $("#"+ id).length;
		if(checkId != '0'){
			alert("해당 교과목은 이미 추가되었습니다.");
			return false;
		}
		console.log(majorCd);
		var innerHtml = "";
		
		
		innerHtml += '<div class="plan_list" id="' + id + '">';
		innerHtml += '<ul>';
		innerHtml += '<li class="title"><span><input type="checkbox" id="select' + index + '" name="select" title="선택" data-id="' + id + '" value=""></span>' + subjectNm + '(' + subjectCd + ')' + '</li>';
		innerHtml += '<li>';
		innerHtml += '<dl>';
		innerHtml += '<dt>개설 년도</dt>';
		innerHtml += '<dd>' + year + '</dd>';
		innerHtml += '</dl>';
		innerHtml += '<dl>';
		innerHtml += '<dt>개설학년</dt>';
		innerHtml += '<dd>' + grade + '</dd>';
		innerHtml += '</dl>';
		innerHtml += '<dl>';
		innerHtml += '<dt>학기구분</dt>';
		innerHtml += '<dd>' + smtNm + '</dd>';
		innerHtml += '</dl>';
		innerHtml += '<dl>';
		innerHtml += '<dt>학점</dt>';
		innerHtml += '<dd>' + cdtNum + '</dd>';
		innerHtml += '</dl>';
		innerHtml += '</li>';
		innerHtml += '</ul>';
		innerHtml += '<div>';
		innerHtml += '<button type="button" onclick="delCard(`' + id + '`);">삭제</button>';
		innerHtml += '</div>';
		innerHtml += '<input type="hidden" value="'+ majorCd +'" name="MAJOR_CD" id="majorCd' + index + '">';
		innerHtml += '<input type="hidden" value="'+ comdivCd +'" name="COMDIV_CODE" id="comdivCd' + index + '">';
		innerHtml += '<input type="hidden" value="'+ comdivNm +'" name="COMDIV_CODE_NM" id="comdivNm' + index + '">';
		innerHtml += '<input type="hidden" value="'+ grade +'" name="COM_GRADE" id="grade' + index + '">';
		innerHtml += '<input type="hidden" value="'+ year +'" name="YEAR" id="year' + index + '">';
		innerHtml += '<input type="hidden" value="'+ smt +'" name="SMT" id="smt' + index + '">';
		innerHtml += '<input type="hidden" value="'+ smtNm +'" name="SMT_NM" id="smtNm' + index + '">';
		innerHtml += '<input type="hidden" value="'+ cdtNum +'" name="CDT_NUM" id="cdtNum' + index + '">';
		innerHtml += '<input type="hidden" value="'+ subjectNm +'" name="SUBJECT_NM" id="subjectNm' + index + '">';
		innerHtml += '<input type="hidden" value="'+ subjectCd +'" name="SUBJECT_CD" id="subjectCd' + index + '">';
		innerHtml += '<input type="hidden" value="'+ openDeptCd +'" name="DEPT_CD" id="openDeptCd' + index + '">';
		innerHtml += '<input type="hidden" value="'+ openDeptNm +'" name="DEPT_NM" id="openDeptNm' + index + '">';
		innerHtml += '</div>';
		
		
		
		
		$("#eduList").append(innerHtml);
		
		
	});
	
	
	$("#saveSub").click(function(){
		var jsonString;
		var jsonArray = [];		
		var flag = false;
		var state = false;
		
		$('input:checkbox[name=select]').each(function (index) {
			if($(this).is(":checked")==true) {
				var obj = {};	
				$(this).closest('div').find('input').each(function(idx){
						
					if(idx > 0) {						
						$('#topfieldset select option:selected').each(function(){
							obj['FIELD_CD'] = $(this).val();
							obj['FIELD'] = $(this).text();
							if($(this).val() == '') {
								flag = true;
							}
 						})
						obj[$(this).attr("name")] = $(this).val();
					}
				});
				
				if(flag) {
					alert("인재상을 선택해 주세요");
					$(this).focus();
					return false;
				}
				 
				console.log(JSON.stringify(obj));
				jsonArray.push(obj);
			}
			
		})
		
		try {		
			$.ajax({
				url: '${URL_ADD_MAJOR_INPUT}',
			    type: 'POST',
			    crossDomain: true,
			    contentType: 'application/json',
			    data: 
			    	JSON.stringify({"data" : jsonArray})						
			    ,beforeSend:function(request){
			    	request.setRequestHeader('Ajax','true');
			    }
				}).done(function (data, textStatus, error) {
					console.log("ajax 호출 완료" + data);
					console.log(data);
					if(data == null) {
						alert("ok");
						
					} else {
						
						var errObj = JSON.parse(error.responseText);
						
					}
					
					
					
					/* window.close();
					window.opener.location.reload(); */
					
				}).fail(function(data, textStatus, error){
				      /*pass*/
				      /* alert("이미 존재하는 교과목과 전문인 입니다."); */
				      console.log('error 발생 :@@@@@@@ ' + data+ textStatus + error);				      
				      return false;
				});
			state = true;
		} catch(e){
			console.log(e);
			return false;
		}
		
		var index = $("#eduList > div").length;
		if(($("#eduList div").length - $("#eduList div input[name=chkbox]:checked").length) < 1){
			alert("최소 하나의 교과목 행이 있어야 합니다.")
			return false;
		}
		if($("#eduList div input[name=select]:checked").length == 0){
			alert("추가할 교과목을 선택해주세요.");
			return false;
		}
		
		
		if (state && !flag) {
			window.close();			
			window.opener.location.href = "${URL_MAJOR_LIST}&majorCd=" + majorCd + "&year=" + year;
		}
	});
	
	
});

function delCard(id){
	$("#" + id).remove();
	var index = $("#eduList > div").length;
	if(index == 0){
		var innerHtml = "";
		
		innerHtml += '<div class="plan_list noitem">';
		innerHtml += '<p>교과목을 추가해주세요.</p>';
		innerHtml += '</div>';
		$("#eduList").append(innerHtml);
	}
}

function selDelCard(){
	var checkedValues = [];
	
	$("#eduList input[type='checkbox']:checked").each(function(){
		checkedValues.push($(this).attr("data-id"));
	});
	console.log(checkedValues);
	checkedValues.forEach(function(value) {
		$("#" + value).remove();
    });
	
	var index = $("#eduList > div").length;
	if(index == 0){
		var innerHtml = "";
		
		innerHtml += '<div class="plan_list noitem">';
		innerHtml += '<p>교과목을 추가해주세요.</p>';
		innerHtml += '</div>';
		$("#eduList").append(innerHtml);
	}
}

function allDelCard(){
	$("#eduList").empty();
	var index = $("#eduList > div").length;
	if(index == 0){
		var innerHtml = "";
		
		innerHtml += '<div class="plan_list noitem">';
		innerHtml += '<p>교과목을 추가해주세요.</p>';
		innerHtml += '</div>';
		$("#eduList").append(innerHtml);
	}
}

function fn_sampleInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
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

//진출분야 리스트 불러오기
function getFieldData(majorCd) {
	
	try {		
		$.ajax({
			url: '/web/majorInfo/getFieldList.json?mId=44',
		    type: 'POST',
		    crossDomain: true,
		    data: {
		    	majorCd : majorCd
	  		},
		    beforeSend:function(request){
		    	request.setRequestHeader('Ajax','true');
		    }
			}).done(function (data, textStatus, xhr) {
				fieldList = data.fieldList;							
					
	  			for(var i=0; i < fieldList.length; i++){
  					$('#s_field').append("<option name='FIELD' value='" + fieldList[i].OPTION_CODE + "'>" + fieldList[i].OPTION_NAME + "</option>");
		    	}
	  			/* if ($("#sptPsnKor").val() == $('#s_sptPsnKor').val()) {
	  				$('#sptPsnKor').val($('#s_sptPsnKor' + 'option:selected').text());
	  			} */
				
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