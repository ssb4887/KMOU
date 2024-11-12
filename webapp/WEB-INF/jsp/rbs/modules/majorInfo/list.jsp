<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
	$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_sampleInputReset();
	
	

	// reset
	$("#<c:out value="${param.searchFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${param.searchFormId}"/>").reset();
			fn_sampleInputReset();
		}catch(e){alert(e); return false;}
	});	

	/* 주관대학 - 학부(과) 셀렉트박스 */
	$("#college1").change(function(){
		var univ = $(this).val();				
		
		var varAction = "/RBISADM/menuContents/web/majorInfo/DepartAjax.json?mId=44&&UNIV=" + univ;
		
		$("#college2").find("option").remove();
		$("#college2").append('<option value="">학부(과)</option>\n');
		$("#college3").find("option").remove();
		$("#college3").append('<option value="">전공</option>\n');			
		
		if(univ != ''){	
			$("#college2").removeAttr("disabled"); 
			getDepartList(univ, varAction);
		}else{			
			$("#college2").attr("disabled", true); 
			$("#college3").attr("disabled", true); 
		}
			
	});
	
	/* 주관대학 - 학부(과) - 전공 셀렉트박스 */
	$("#college2").change(function(){
		var depart = $(this).val();
		
		var varAction = "/RBISADM/menuContents/web/majorInfo/MajorAjax.json?mId=44&DEPART=" + depart;
		$("#college3").append('<option value="">전공</option>\n');
		
		if($(this).find("option:selected").text().slice(-2) == '학부'){
			$("#college3").removeAttr("disabled");			
			getMajorList(depart, varAction);
		}else{
			$("#college3").find("option").remove();
			$("#college3").append('<option value="">전공</option>\n');	
			$("#college3").attr("disabled", true);
		}
			
	});
	
});
	

 
function fn_sampleInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
}	

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
  					varCon += '<option value=' + item.DEPT_CD + ' ' + selected + '>' + item.DEPT_NM + '</option>\n';
  					
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
  		error:function(request,error){
  			alert("학부실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	}); 
}

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
  					varCon += '<option value=' + item.DEPT_CD + ' ' + selected + '>' + item.DEPT_NM + '</option>\n';
  				});
  				varItemObj.append(varCon);
  			} else{
  				varItemObj.attr("disabled", true);
  				var varCon = '<option value="">전공</option>\n';
  				varItemObj.append(varCon);
  			} 
  			
  				
  			return false;
  		}, 
  		error:function(request,error){
  			alert("실패");
  		}
  	}); 
}

function setColgCd(){
	
	// 소속 구분 검색 정보 불러오기
	<c:if test="${!empty param.is_colgCd}">
			var colgCd = '<c:out value="${param.is_colgCd}"/>';
			$("#s_colgCd").val(colgCd).prop("selected",true);
			getDeptData(colgCd);		
	</c:if>


}

function setFcltSustCd(){
	// 대학 검색 정보 불러오기
	<c:if test="${!empty param.is_fcltSustCd}">
			var fcltSustCd = '<c:out value="${param.is_fcltSustCd}"/>';
			$("#s_fcltSustCd").val(fcltSustCd).prop("selected",true);
			getMajorData(fcltSustCd);
	</c:if>
}

function setJmCd(){
		// 학과/전공 검색 정보 불러오기
	<c:if test="${!empty param.is_mjCd}">
		if($('#s_colgCd option:selected').val() != '' && $('#s_fcltSustCd option:selected').val() != ''){
			var mjCd = '<c:out value="${param.is_mjCd}"/>';
			$("#s_mjCd").val(mjCd).prop("selected",true);
		}else{
			$('#s_mjCd').show();
			$("#s_mjCd").children('option:not(:first)').remove();
		}
	</c:if>
}
</script>