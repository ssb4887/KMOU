<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_sampleInputReset();
	
// 	
	var originUrl = window.location.href;
	history.replaceState({path : window.location.pathname},'', window.location.pathname);
	
	$("#refresh").click(function(){
		var confirmVal = confirm("조회 시 입력하시던 정보는 사라집니다. 조회하시겠습니까?");
		
		if(confirmVal){
			window.location.href = originUrl;
		}
	});
	
	
	
	 document.getElementById('college3').addEventListener('change', function() {
	        const selectedValue = this.value;
	        const values = selectedValue.split(',');
	        const value1 = values[0];
	        const value2 = values[1];
	        $('#majorCd').val(value1);
	        $('#majorNmEng').val(value2);
			$('#majorNmKor').val($('#college3 option:selected').text());
	 });
	
	
	/* 주관대학 - 학부(과) 셀렉트박스 */
	$("#college1").change(function(){
		const univ = $(this).val();
		const varAction = "${elfn:replaceScriptLink(URL_DEPARTLIST)}&UNIV=" + univ;
		
		$("#college2").removeAttr("disabled"); 
		$("#college3").removeAttr("disabled"); 
		
		$("#college3").find("option").remove();
		$("#college3").append('<option value="">전공</option>\n');
		
		$('#colgCd').val($('#college1 option:selected').val());
		$('#colgNm').val($('#college1 option:selected').text());
		getDepartList(univ, varAction);
			
	});
	
	/* 주관대학 - 학부(과) - 전공 셀렉트박스 */
	$("#college2").change(function(){
		
		const selectedValue = this.value;
        const values = selectedValue.split(',');
        const value1 = values[0];
        const value2 = values[1];
		
		$("#college3").removeAttr("disabled"); 
		
		$('#deptCd').val(value1);
		$('#deptNm').val($('#college2 option:selected').text());
		
		 $('#majorNmEng').val(value2);
        
        console.log('Value 1:', value1);
        console.log('Value 2:', value2);
        
        const depart = values[0];
        const varAction = "${elfn:replaceScriptLink(URL_MAJORLIST)}&DEPART=" + depart;
        
		getMajorList(depart, varAction);
	});
	
	
	/* $('#college3').change( function() {
		//getRegisteredYear($('#college3').val());
		
		$('#majorCd').val(value1);
		$('#majorNmKor').val($('#college3 option:selected').text());
		
		//$('#majorNmEng').val($('#college3 option:selected').attr('deptnmeng'));
		$("#yearbox").show();
	}); */
	
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
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_sampleInputFormSubmit();
		}catch(e){alert(e); return false;}
	});

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
	



	
});


// 대학에 따른 학부(과) 목록 가져오기
function getDepartList(univ, varAction){
	var dpCd = "${param.depart}";
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			var varItemObj = $("#college2");
  			varItemObj.find("option").remove();
  			var list = result.departList;
  			if(list != "") {
  				var varCon = '<option value="">학부(과)</option>\n';
  				$.each(list, function(i, item){
  					var selected = (dpCd === item.DEPT_CD) ? ' selected' : '';
  					varCon += '<option value=' + item.DEPT_CD + ',' + item.DEPT_ENM + '"' + selected +'>' + item.DEPT_NM + '</option>\n';
  				});
  				varItemObj.append(varCon);
  			} else{
  				varItemObj.attr("disabled", true);
  				$("#college3").attr("disabled", true);
  				var varCon = '<option value="">학부(과)</option>\n';
  				varItemObj.append(varCon);
  			} 
  			
  			return false;
  		}, 
  		error:function(request,error){ asdf("실패"); }
  	}); 
}

// 학부(과)에 따른 전공 목록 가져오기
function getMajorList(depart, varAction){
	var majorCd = "${param.major}";
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			var varItemObj = $("#college3");
  			varItemObj.find("option").remove();
  			var list = result.majorList;
  			if(list != "") {
  				var varCon = '<option value="">전공</option>\n';
  				$.each(list, function(i, item){
  					var selected = (majorCd === item.DEPT_CD) ? ' selected' : '';
  					varCon += '<option value="' + item.DEPT_CD + ',' + item.DEPT_ENM + '"' + selected + '>' + item.DEPT_NM + '</option>\n';
  				});
  				varItemObj.append(varCon);
  			} else{
  				varItemObj.attr("disabled", true);
  				var varCon = '<option value="">전공</option>\n';
  				varItemObj.append(varCon);
  			} 

  			return false;
  		}, 
  		error:function(request,error){ adsf(error); }
  	}); 
}


function fn_sampleInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	/* if($("#year").val() == ('' || undefined)){
		alert("연도를 선택해주세요");
		return false;
	} */
	if(($("#mjCd").val() == '' || $("#mjCd").val() == undefined) && ($("#college3").val() == '' || $("#college3").val() == undefined)){
		alert("소속을 선택해주세요");
		return false;
	}
	
	

	
	$('#hiddenResprCfmYnValue').val($('#resprCfmYn').is(':checked') ? "Y" : "N");

	
	
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



</script>