<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item"%>
<c:set var="itemOrderName" value="${submitType}_order" />
<itui:submitScript items="${itemInfo.items}"
	itemOrder="${itemInfo[itemOrderName]}" />
<script type="text/javascript">

var fieldList = [];

$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_sampleInputReset();
	// getCollegeData();
	
	var originUrl = window.location.href;
	history.replaceState({path : window.location.pathname},'', window.location.pathname);
	
	$("#refresh").click(function(){
		var confirmVal = confirm("조회 시 입력하시던 정보는 사라집니다. 조회하시겠습니까?");
		
		if(confirmVal){
			window.location.href = originUrl;
		}
	});	
	
	var majorCd = '<c:out value="${param.majorCd}"/>';
	getFieldData(majorCd);

	
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
	
	/* $("[id^=field]").change(function(){
		console.log("진입확인");
		var idValue = $(this).attr('id');
		var indexNumber = idValue.replace("field",'');
		console.log(indexNumber);
		$("#fieldNm" + indexNumber).text($(this).children("option:selected").text())
		console.log($(this).children("option:selected").text());
		
		console.log($(this).children("option:selected").val());
	}) */
	
	//전문인 추가 행 추가
	$("[id^=addSub]").click(function(){
		
		
		var idValue = $(this).attr('id');
		var indexNumber = idValue.replace("addSub",'');
		
		
		
		var index = $("#tpSubContent > tbody > tr").length;
		var length = Number($("#majorRowLength").val());
		var dtLength = Number($("#dtLength").val());
		
		var comdivCd = $("#comdivCd" + indexNumber).val();
		var grade = $("#grade" + indexNumber).val();
		var openDept = $("#openDept" + indexNumber).val();
		var subjectNm = $("#subjectNm" + indexNumber).val();
		var field = $("#field" + indexNumber).val();
		
		var sinbunCd = $("#sinbunCd" + indexNumber).val();
		var subjectCd = $("#subjectCd" + indexNumber).val();
		var fieldCd = $("#fieldCd" + indexNumber).val();
		var year = $("#year" + indexNumber).val();
		var smt = $("#smt" + indexNumber).val();
		var cdtNum = $("#cdtNum" + indexNumber).val();
		var lastModiId = $("#lastModiId" + indexNumber).val();
		var lastModiDate = $("#lastModiDate" + indexNumber).val();
		
		var theoTmCnt = $("#theoTmCnt" + indexNumber).val();
		var pracTmCnt = $("#pracTmCnt" + indexNumber).val();
		var courseNo = $("#courseNo" + indexNumber).val();
		var inputList = ["filed"];
		var inputName = ["진출분야"];
		
		
		
		length += 1;
		$("#majorRowLength").val(length);
		var innerHtml = "";
		
		//innerHtml += '<tr><td><button type="button" id="delete' + (index -1) + '" data-idx="' + (index -1) + '" class="btn-m02 btn-color02">삭제</button></td>'
		//innerHtml += '<tr><td><input type="checkbox" class="checkbox-type01" name="chkbox"></td>'
		innerHtml += '<tr>'
		innerHtml += '<td class="num" id="num"></td>'
		innerHtml += '<td><input type="hidden" value="'+ comdivCd +'" name="comdivCd' + (length -2) + '" id="comdivCd' + (length -2) + '"><span >' + comdivCd + '</span></td>'
		innerHtml += '<td><input type="hidden" value="'+ grade +'" name="grade' + (length -2) + '" id="grade' + (length -2) + '"><span >' + grade + '</span></td>'
		innerHtml += '<td><input type="hidden" value="'+ openDept +'" name="openDept' + (length -2) + '" id="openDept' + (length -2) + '"><span >' + openDept + '</span></td>'
		innerHtml += '<td><input type="hidden" value="'+ subjectNm +'" name="subjectNm' + (length -2) + '" id="subjectNm' + (length -2) + '"><span >' + subjectNm + '</span></td>'		
		innerHtml += '<td><select name="field'+ (length -2) +'" id="field' + (length -2) + '" class="field" style="width:60%" title="진출분야" "></select> '+ '</td>'
		
		innerHtml += '<td><button type="button" class="btnTypeG">NEW</button></td>'
		
		innerHtml += '<td><button type="button" onclick=fun_delete_major(this); id="fn_btn_majorCancel" class="btnTypeF">삭제</button></td>'		
	
		innerHtml += '<input type="hidden" value="'+ lastModiId +'" name="lastModiId' + (length -2) + '" id="lastModiId' + (length -2) + '">'
		innerHtml += '<input type="hidden" value="" name="fieldNm' + (length -2) + '" id="fieldNm' + (length -2) + '">' + '</>'
		/* innerHtml += '<input type="hidden" value="" name="fieldCd' + (length -2) + '" id="fieldCd' + (length -2) + '">' + '</>' */
		innerHtml += '<input type="hidden" value="'+ year +'" name="year' + (length -2) + '" id="year' + (length -2) + '">' + '</>'
		innerHtml += '<input type="hidden" value="'+ smt +'" name="smt' + (length -2) + '" id="smt' + (length -2) + '">' + '</>'
		innerHtml += '<input type="hidden" value="'+ cdtNum +'" name="cdtNum' + (length -2) + '" id="cdtNum' + (length -2) + '">' + '</>'
		innerHtml += '<input type="hidden" value="'+ subjectCd +'" name="subjectCd' + (length -2) + '" id="subjectCd' + (length -2) + '">' + '</>'

		innerHtml += '<input type="text" name="dtLength" value="' + length + '"/>'
		innerHtml += '</tr>'			
		$("#dtLength").val(length);
		$(this).closest('tr').after(innerHtml);
		
		console.log("필드리스트 출력");
		console.log(fieldList);
		for(var i=0; i < fieldList.length; i++){
			$('#field'+(length -2)).append("<option value='" + fieldList[i].OPTION_CODE + "'>" + fieldList[i].OPTION_NAME + "</option>");
		}
		
		$("#tpSubContent tbody tr").each(function(index){			
			$(this).find("#num").text(index + 1);
		})
		
// 		$("#tpSubContent > tbody:last").append(innerHtml);
		$("#field"+ (length-2)).focus();		

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
	
	// 진출분야 정보 불러오기
	<c:if test="${!empty param.mjCd}">
			var mjCd = '<c:out value="${param.mjCd}"/>';
			var year = '<c:out value="${param.year}"/>';
			getSptPsnData(mjCd,year);
	</c:if>
	


	
});

//학부 교육과정 진출분야 입력 행 삭제
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
	
	var row = button.parentNode.parentNode;	
	row.parentNode.removeChild(row);
	
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
					
	  			/* for(var i=0; i < fieldList.length; i++){
  					$('.field').append("<option name='FIELD' value='" + fieldList[i].OPTION_CODE + "'>" + fieldList[i].OPTION_NAME + "</option>");
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

//학부 교육과정 진출분야 입력 행 추가
function fn_sampleInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	
	var inputList = ["field"];
	var inputName = ["진출분야"];
	var docList = [];

	
	for(var p = 0; p < inputList.length; p++){
		
		
		var value = inputList[p]
		
		for(var i = 0; i <= $("#dtLength").val() ; i++){
			
			var comdivCd = ($('#comdivCd'+i).val());
			var grade = ($('#grade'+i).val());
			var openDept = ($('#openDept'+i).val());
			var subjectNm = ($('#subjectNm'+i).val());
			var subjectCd = ($('#subjectCd'+i).val());
			var fieldCd = ($('#field'+i + ' option:selected').val());
			var fieldNm = ($('#field'+i + ' option:selected').text());
			$("#fieldNm"+i).val(fieldNm);
			var cons = grade + openDept + subjectNm + subjectCd + fieldCd;
			
			
			docList.push(cons);
			
		}
		for(var i = 0; i <= $("#dtLength").val() ; i++){
			
			var id = '#s_' + value + i;
			var docId = '#docId' + ((docList.length)-1);
			console.log(docList.length);
			
			for (var k= 0 ; k < docList.length ; k++) {
				if ( i != k) {
					if(i != docList.length) {						
						if($('#field'+k).val() != undefined){
							if (docList[i] == null || docList[k] == null) {
								continue;
							}
							if (docList[k] == docList[i]){			
								alert("중복된 진출분야를 입력할 수 없습니다");
								$(id).focus();
								return false;	
							}	
						}													
												
					} else {
						break;
					}
				}				
			}
		
			if ($(id).val() != null) {
				if($(id).val().trim() === ''){
					alert("진출분야 항목들은 필수 입력값입니다.");
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
	
	
	$("#majorIdx").attr("value", majorIdx);
	$("#yearIdx").attr("value", yearIdx);
	return true;
}

function fn_sampleInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
}	

</script>