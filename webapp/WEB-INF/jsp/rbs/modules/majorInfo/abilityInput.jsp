<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">

var sptPsnList = [];

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
	
	// 전공 능력 인재상 정보 불러오기
	<c:if test="${!empty param.majorCd}">
			var majorCd = '<c:out value="${param.majorCd}"/>';
			var year = '<c:out value="${param.year}"/>';
			//getSptPsnAbtyData(majorCd,year);
			getTalentAbtyData(majorCd);
	</c:if>

	
});



function fn_sampleInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	
	
	var inputList = ["talent", "majorAbty" ];
	var inputName = ["인재상", "전공능력"];
	
	
	
	for(var p =0; p < inputList.length; p++){
		var value = inputList[p]
		for(var i = 1; ; i++){
			var id = '#' + value + i;
			if($(id).length === 0 ){
				break;
			}
			
			if($(id).val().trim() === ''){
				alert("인재상, 전공능력 항목들은 필수 입력값입니다.");
				return false;
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

//전공능력 인재상 불러오기
function getSptPsnAbtyData(mjCd,year) {
	
	var abilityRowLength = Number($("#abilityRowLength").val());
	
	
	try {		
		$.ajax({
			url: '/web/haksaCode/getSptPsnList.json?mId=192',
		    type: 'POST',
		    crossDomain: true,
		    data: {
		    	year : year,
		    	majorCd : majorCd
	  		},
		    beforeSend:function(request){
		    	request.setRequestHeader('Ajax','true');
		    }
			}).done(function (data, textStatus, xhr) {
				sptPsnList = data.sptPsnList;
				sptPsnAbtyList = data.sptPsnAbtyList;
				
				for(var j=0; j < abilityRowLength; j++){		
		  			for(var i=0; i < sptPsnList.length; i++){
		  				if (sptPsnAbtyList == null) {
		  					console.log("전공능력 데이터가 비어있습니다.")		  						
		  				} else {
		  					if (sptPsnAbtyList[j].SPT_PSN_KOR == sptPsnList[i].SPT_PSN_KOR) {
			  					$('#sptPsnKor'+(j+1)).append("<option value='" + sptPsnList[i].SPT_PSN_KOR + "' selected>" + sptPsnList[i].SPT_PSN_NM + "</option>");
			  					$('#mjAbtyKor'+(j+1)).val(sptPsnAbtyList[j].MJ_ABTY_KOR);
			  					$('#mjAbtyDefnKor'+(j+1)).val(sptPsnAbtyList[j].MJ_ABTY_DEFN_KOR);		  							  					
			  					
			  				} else {
			  					$('#sptPsnKor'+(j+1)).append("<option value='" + sptPsnList[i].SPT_PSN_KOR + "'>" + sptPsnList[i].SPT_PSN_NM + "</option>");	  						
			  					$('#mjAbtyKor'+(j+1)).val(sptPsnAbtyList[j].MJ_ABTY_KOR);
			  					$('#mjAbtyDefnKor'+(j+1)).val(sptPsnAbtyList[j].MJ_ABTY_DEFN_KOR);
			  				}
		  				}
		  						  				
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

//전공능력 인재상 불러오기
function getTalentAbtyData(majorCd) {
	var abilityRowLength = Number($("#abilityRowLength").val());
	
	
	try {		
		$.ajax({
			url: '/web/majorInfo/getAbtyList.json?mId=44',
		    type: 'POST',
		    crossDomain: true,
		    data: {
		    	majorCd : majorCd
	  		},
		    beforeSend:function(request){
		    	request.setRequestHeader('Ajax','true');
		    }
			}).done(function (data, textStatus, xhr) {
				talentList = data.talentList;
				var talentValue = "";
				talentAbtyList = data.talentAbtyList;
				for(var j=0; j < abilityRowLength; j++){		
		  			for(var i=0; i < talentAbtyList.length; i++){
		  				if(talentList != ""){
		  					talentValue = talentList[j].MAJOR_ABTY;
		  				} else{
		  					talentValue = "";
		  				}
		  				if (talentAbtyList == null) {
		  					console.log("전공능력 데이터가 비어있습니다.")		  						
		  				} else {
		  					if (talentAbtyList[i].MAJOR_ABTY == talentValue) {
		  						$('#abtyCd'+(j)).val(talentAbtyList[j].ABTY_CD);
			  					$('#majorAbty'+(j+1)).append("<option value='" + talentAbtyList[i].MAJOR_ABTY + "' selected>" + talentAbtyList[i].MAJOR_ABTY + "</option>");
			  				} else {
			  					$('#abtyCd'+(j)).val(talentAbtyList[j].ABTY_CD);
			  					$('#majorAbty'+(j+1)).append("<option value='" + talentAbtyList[i].MAJOR_ABTY + "'>" + talentAbtyList[i].MAJOR_ABTY + "</option>");
			  				}
		  				}
		  						  				
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




</script>