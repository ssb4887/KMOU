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
	
	var currentYear = new Date().getFullYear();
	
	$("#t_YY").val(currentYear);
	$("#t_YY_ui").text(currentYear);
	
	for(var i = currentYear -1; i > currentYear -5; i--){
		$("#s_YY").append($('<option>',{
			value : i,
			text: i
		}));
	}
	

	// 소속구분 변경시 대학 세팅
	$('#s_colgCd').change( function() {			
		getDeptData($('#s_colgCd').val());
	});
	// 소속대학 선택 시 학과 목록 선택값 변경	
	$('#s_fcltSustCd').change( function() {
		getMajorData($('#s_fcltSustCd').val());
	});
	
	
	// 전공 선택시	
	$('#s_mjCd').change( function() {
		$('#t_mjCd').val($('#s_mjCd option:selected').val());
		$('#t_mjNm').text(" > " + $('#s_mjCd option:selected').text())
		
	});
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_sampleInputFormSubmit();
		}catch(e){alert(e); return false;}
	});
	
	
});
	


function fn_sampleInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	if($("#s_YY").val() == ('' || undefined)){
		alert("연도를 선택해주세요");
		return false;
	}
	var confirmText = "대상연도 : " + $("#s_YY option:selected").text() + "년 \n";
	 	confirmText += "대학 : " + $("#s_colgCd option:selected").text() + "\n";
	 	confirmText += "학과/전공 : " + $("#s_fcltSustCd option:selected").text() + "\n";
	 	if($("#s_fcltSustCd option:selected").val() != $("#s_mjCd option:selected").val()){
	 		confirmText += "전공 : " + $("#s_mjCd option:selected").text() + "\n";	 		
	 	}	 	 	
	 	confirmText +="에 해당하는 자료를 " + $("#t_YY").text() + "년으로 복사하시겠습니까?\n\n";
	 	confirmText +="※ 자료복사를 처리하면, 기존의 입력된 자료는 삭제되고 새로 생성됩니다.";
// 	,대학,학과/전공에 대한 자료를 올해연도로 복사하시겠습니까?
	var result = confirm(confirmText);
	return false;
	if(!result){
		return false;	
	}
	
	
	fn_createMaskLayer();			
	return true;
}

function fn_sampleInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
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
		        	$('#s_colgCd').append("<option value='" + data.haksaCode[idx].DEPT_CD + "'>" + data.haksaCode[idx].DEPT_KOR_NM + "</option>");
		    	}
				setColgCd();
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
				$("#t_fcltSustCd").val("");
				$("#t_mjCd").val("");
				$("#t_fcltSustNm").text("전체");
				$("#t_mjNm").text("");
				for(var idx in data.haksaCode){
		        	$('#s_fcltSustCd').append("<option value='" + data.haksaCode[idx].DEPT_CD + "'>" + data.haksaCode[idx].DEPT_KOR_NM + "</option>");
		    	}
				setFcltSustCd();
				$('#s_colgCd').val(colgCd);
				$('#t_colgCd').val(colgCd);
				$('#t_colgNm').text($('#s_colgCd option:selected').text())
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
		var fcltSustNm = $('#s_fcltSustCd option:selected').text();
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
				$("#t_mjCd").val("");
				$("#t_mjNm").text(" > 전체");
				// 학과/학부의 정보가 전공일시
				if(data.haksaCode.length == 0){					
					 if(fcltSustNm != '전체'){
						$('#s_mjCd option:first').val(fcltSustCd);							
						$('#s_mjCd').hide();
						$('#t_fcltSustCd').val(fcltSustCd);
						$('#t_fcltSustNm').text(fcltSustNm);
						
					 }else{
						$('#s_mjCd option:first').val("");						
					 }
				}else{
					for(var idx in data.haksaCode){						
			        	$('#s_mjCd').append("<option value='" + data.haksaCode[idx].DEPT_CD + "'>" + data.haksaCode[idx].DEPT_KOR_NM + "</option>");
			        	$('#s_mjCd').show();
						$('#t_fcltSustCd').val(fcltSustCd);
						$('#t_fcltSustNm').text(fcltSustNm);
			        	$('#t_mjNm').show();
			    	}					
					setJmCd();
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

function setColgCd(){
	
	// 소속 구분 검색 정보 불러오기
	<c:if test="${!empty param.s_colgCd}">
			var colgCd = '<c:out value="${param.s_colgCd}"/>';
			$("#s_colgCd").val(colgCd).prop("selected",true);
			getDeptData(colgCd);		
	</c:if>


}

function setFcltSustCd(){
	// 대학 검색 정보 불러오기
	<c:if test="${!empty param.s_fcltSustCd}">
			var fcltSustCd = '<c:out value="${param.s_fcltSustCd}"/>';
			$("#s_fcltSustCd").val(fcltSustCd).prop("selected",true);
			getMajorData(fcltSustCd);
	</c:if>
}

function setJmCd(){
		// 학과/전공 검색 정보 불러오기
	<c:if test="${!empty param.s_mjCd}">
			var mjCd = '<c:out value="${param.s_mjCd}"/>';
			$("#s_mjCd").val(mjCd).prop("selected",true);
	</c:if>
}
</script>