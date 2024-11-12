<%@ include file="../../include/commonTop.jsp"%>
<script>
let bookmarkList;

var _SEARCH = {
		MID 			: "32",					// 메뉴번호
		TOPIC			: "",					// 검색 키
		UNIV 			: "",					// 대학
		DEPT 			: "",					// 학부(과)
		MAJOR 			: "",					// 전공
		YEAR			: "",					// 년도
		SEMESTER 		: "",					// 학기
		GRADE 			: "",					// 대상 학년
		OPEN			: "",					// 최근개설
		
		STD_COMPLET_1   : "",					// 이수구분
		STD_COMPLET_2   : "",
		STD_COMPLET_3   : "",
		STD_COMPLET_4   : "",
		STD_COMPLET_5   : "",
		STD_COMPLET_6   : "",
		STD_COMPLET_7   : "",
		STD_COMPLET_8   : "",
		STD_CORE_1 		: "",					// 핵심역량
		STD_CORE_2 		: "",
		STD_CORE_3 		: "",
		STD_CORE_4 		: "",
		STD_CORE_5 		: "",
		STD_CORE_6 		: "",
		STD_CORE_7 		: "",
		STD_CORE_8 		: "",
		
		ORDERBY 		: "",					// 정렬순서
		FLAG			: ""					// 로그FLAG
};

var _page = "";

$(function(){
	var dt = new Date();
	var year = dt.getFullYear();
	
	var keyword = $("#search").val();
	if(keyword != ""){
		getSbjtList(1);
	}

	//엔터 눌렀을때
	$("#search").on("keyup",function(key){
		if(key.keyCode==13) { 
			
			_SEARCH.TOPIC	 		= $("#search").val();
			_SEARCH.UNIV     		= $("#college1").val();
			_SEARCH.DEPT 			= $("#college2").val();
			_SEARCH.MAJOR 			= $("#college3").val();
			_SEARCH.YEAR 	 		= $("#yearI").val();
			_SEARCH.SEMESTER 		= $("#semeI").val();
			_SEARCH.GRADE 	 		= $("#gradeI").val();
			_SEARCH.OPEN 			= $("#openI").val();
			
			_SEARCH.STD_COMPLET_1 	= $("input[name=std_complet_1]:checked").val();
			_SEARCH.STD_COMPLET_2 	= $("input[name=std_complet_2]:checked").val();
			_SEARCH.STD_COMPLET_3 	= $("input[name=std_complet_3]:checked").val();
			_SEARCH.STD_COMPLET_4 	= $("input[name=std_complet_4]:checked").val();
			_SEARCH.STD_COMPLET_5 	= $("input[name=std_complet_5]:checked").val();
			_SEARCH.STD_COMPLET_6 	= $("input[name=std_complet_6]:checked").val();
			_SEARCH.STD_COMPLET_7 	= $("input[name=std_complet_7]:checked").val();
			_SEARCH.STD_COMPLET_8 	= $("input[name=std_complet_8]:checked").val();
			_SEARCH.STD_CORE_1 		= $("input[id='std_core_1']:checked").val();
			_SEARCH.STD_CORE_2 		= $("input[id='std_core_2']:checked").val();
			_SEARCH.STD_CORE_3		= $("input[id='std_core_3']:checked").val();
			_SEARCH.STD_CORE_4		= $("input[id='std_core_4']:checked").val();
			_SEARCH.STD_CORE_5 		= $("input[id='std_core_5']:checked").val();
			_SEARCH.STD_CORE_6 		= $("input[id='std_core_6']:checked").val();
			_SEARCH.STD_CORE_7 		= $("input[id='std_core_7']:checked").val();
			_SEARCH.STD_CORE_8 		= $("input[id='std_core_8']:checked").val();
			
			_SEARCH.ORDERBY 		= $("#orderBy").val();
			_SEARCH.FLAG 			= "Y";
			
			/* sessionStorage.setItem('sbjtSearchKeyword', JSON.stringify(_SEARCH)); */	
			
			getSbjtList(1);
			
			_SEARCH.FLAG 			= "";
		}    
	});
	
	// 검색 버튼 클릭시 
	$("#btnSbjtSearch, #btnSbjtDetailSearch").click(function(){
		
			_SEARCH.TOPIC	 		= $("#search").val();
			_SEARCH.UNIV     		= $("#college1").val();
			_SEARCH.DEPT 			= $("#college2").val();
			_SEARCH.MAJOR 			= $("#college3").val();
			_SEARCH.YEAR 	 		= $("#yearI").val();
			_SEARCH.SEMESTER 		= $("#semeI").val();
			_SEARCH.GRADE 	 		= $("#gradeI").val();
			_SEARCH.OPEN 			= $("#openI").val();
			
			_SEARCH.STD_COMPLET_1 	= $("input[name=std_complet_1]:checked").val();
			_SEARCH.STD_COMPLET_2 	= $("input[name=std_complet_2]:checked").val();
			_SEARCH.STD_COMPLET_3 	= $("input[name=std_complet_3]:checked").val();
			_SEARCH.STD_COMPLET_4 	= $("input[name=std_complet_4]:checked").val();
			_SEARCH.STD_COMPLET_5 	= $("input[name=std_complet_5]:checked").val();
			_SEARCH.STD_COMPLET_6 	= $("input[name=std_complet_6]:checked").val();
			_SEARCH.STD_COMPLET_7 	= $("input[name=std_complet_7]:checked").val();
			_SEARCH.STD_COMPLET_8 	= $("input[name=std_complet_8]:checked").val();
			_SEARCH.STD_CORE_1 		= $("input[id='std_core_1']:checked").val();
			_SEARCH.STD_CORE_2 		= $("input[id='std_core_2']:checked").val();
			_SEARCH.STD_CORE_3		= $("input[id='std_core_3']:checked").val();
			_SEARCH.STD_CORE_4		= $("input[id='std_core_4']:checked").val();
			_SEARCH.STD_CORE_5 		= $("input[id='std_core_5']:checked").val();
			_SEARCH.STD_CORE_6 		= $("input[id='std_core_6']:checked").val();
			_SEARCH.STD_CORE_7 		= $("input[id='std_core_7']:checked").val();
			_SEARCH.STD_CORE_8 		= $("input[id='std_core_8']:checked").val();
			
			_SEARCH.ORDERBY 		= $("#orderBy").val();
			_SEARCH.FLAG 			= "Y";
			
			/* sessionStorage.setItem('sbjtSearchKeyword', JSON.stringify(_SEARCH)); */
			
			getSbjtList(1);
			
			_SEARCH.FLAG 			= "";
	});

	
	/* 정렬기능 */
	$('#orderBy').change(function() {
		_SEARCH.ORDERBY 		= $("#orderBy").val();
		
		if($("#search").val().trim() == ""){
			getInitSbjtList(_page);
		}else{
			getSbjtList(_page);        
		}
    });
	
	
	/* 년도 학기 학년 셀렉트박스 */
	$("#yearI").append("<option value=''>년도</option>");
	var chkYear = "${param.yearI}";
	for(var y = (year); y >= (year -4); y--){
		if(y == chkYear){
			$("#yearI").append("<option value='" + y + "' selected>" + y + " 년</option>");
		}else{
			$("#yearI").append("<option value='" + y + "'>" + y + " 년</option>");	
		}
		 
	}

	getBookmarkList().then(data => {
		bookmarkList = data;
	});
	
	
	var isUniv = "${param.univ}";
	var isDepart = "${param.depart}";
	var isMajor = "${param.major}";
	
	if(isUniv != ""){
		var varAction = "${elfn:replaceScriptLink(URL_DEPARTLIST)}&UNIV=" + isUniv;
		getDepartList(isUniv, "");
		$("#college2").val(sessionVal.DEPT).prop("selected", true);
	}
	
	if(isDepart != ""){
		var varAction = "${elfn:replaceScriptLink(URL_MAJORLIST)}&DEPART=" + isDepart;
		getMajorList(isDepart, "");
		$("#college3").val(isMajor);
		$("#college3").val(sessionVal.MAJOR).prop("selected", true);
	}
	
	/* 주관대학 - 학부(과) 셀렉트박스 */
	$("#college1").change(function(){
		var univ = $(this).val();				
		
		$("#college2").find("option").remove();
		$("#college2").append('<option value="">학부(과)</option>\n');
		$("#college3").find("option").remove();
		$("#college3").append('<option value="">전공</option>\n');			
		
		if(univ != ''){	
			$("#college2").removeAttr("disabled"); 
			getDepartList(univ, "");
		}else{			
			$("#college2").attr("disabled", true); 
			$("#college3").attr("disabled", true); 
		}
			
	});
	
	
	/* 주관대학 - 학부(과) - 전공 셀렉트박스 */
	$("#college2").change(function(){
		var depart = $(this).val();
		
		var varAction = "${elfn:replaceScriptLink(URL_MAJORLIST)}&DEPART=" + depart;
		$("#college3").append('<option value="">전공</option>\n');
		
		if($(this).find("option:selected").text().slice(-2) == '학부'){
			$("#college3").removeAttr("disabled");			
			getMajorList(depart, "");
		}else{
			$("#college3").find("option").remove();
			$("#college3").append('<option value="">전공</option>\n');	
			$("#college3").attr("disabled", true);
		}
		
			
	});
	
	
	var sessionVal = new Object();
	//세션에 들어가있는 검색값(메인페이지에서 더보기 용 )	
	searchKeyword = sessionStorage.getItem('sbjtSearchKeyword');	
	
	if (searchKeyword) {
	    if (isJsonString(searchKeyword)) {
	        sessionVal = JSON.parse(searchKeyword);
	        
	        if(typeof(sessionVal.TOPIC) == "undefined" || sessionVal.TOPIC == '') return getInitSbjtList(1);
	        //console.log(sessionVal.UNIV);
	        getDepartList(sessionVal.UNIV, sessionVal.DEPT);
	        getMajorList(sessionVal.DEPT, sessionVal.MAJOR);
	        
	        
	        $("input[name=search]").val(sessionVal.TOPIC);
	        $("#college1").val(sessionVal.UNIV).prop("selected", true);
	        $("#college2").val(sessionVal.DEPT).prop("selected", true);
	        $("#college3").val(sessionVal.MAJOR).prop("selected", true);
	        $("#yearI").val(sessionVal.YEAR).prop("selected", true);
	        $("#semeI").val(sessionVal.SEMESTER).prop("selected", true);
	        $("#gradeI").val(sessionVal.GRADE).prop("selected", true);
	        $("#openI").val(sessionVal.OPEN).prop("selected", true);
	        
	        if(sessionVal.STD_COMPLET_1 == $("input[name=std_complet_1]").val()){
	        	$('input:checkbox[id="std_complet_1"]').attr("checked", true)
	        }
	        if(sessionVal.STD_COMPLET_2 == $("input[name=std_complet_2]").val()){
	        	$('input:checkbox[id="std_complet_2"]').attr("checked", true)
	        }
	        if(sessionVal.STD_COMPLET_3 == $("input[name=std_complet_3]").val()){
	        	$('input:checkbox[id="std_complet_3"]').attr("checked", true)
	        }
	        if(sessionVal.STD_COMPLET_4 == $("input[name=std_complet_4]").val()){
	        	$('input:checkbox[id="std_complet_4"]').attr("checked", true)
	        }
	        if(sessionVal.STD_COMPLET_5 == $("input[name=std_complet_5]").val()){
	        	$('input:checkbox[id="std_complet_5"]').attr("checked", true)
	        }
	        if(sessionVal.STD_COMPLET_6 == $("input[name=std_complet_6]").val()){
	        	$('input:checkbox[id="std_complet_6"]').attr("checked", true)
	        }
	        if(sessionVal.STD_COMPLET_7 == $("input[name=std_complet_7]").val()){
	        	$('input:checkbox[id="std_complet_7"]').attr("checked", true)
	        }
	        if(sessionVal.STD_COMPLET_8 == $("input[name=std_complet_8]").val()){
	        	$('input:checkbox[id="std_complet_8"]').attr("checked", true)
	        }
	        
	        if(sessionVal.STD_CORE_1 == $("input[name=std_core_1]").val()){
	        	$('input:checkbox[id="std_core_1"]').attr("checked", true)
	        }
	        if(sessionVal.STD_CORE_2 == $("input[name=std_core_2]").val()){
	        	$('input:checkbox[id="std_core_2"]').attr("checked", true)
	        }
	        if(sessionVal.STD_CORE_3 == $("input[name=std_core_3]").val()){
	        	$('input:checkbox[id="std_core_3"]').attr("checked", true)
	        }
	        if(sessionVal.STD_CORE_4 == $("input[name=std_core_4]").val()){
	        	$('input:checkbox[id="std_core_4"]').attr("checked", true)
	        }
	        if(sessionVal.STD_CORE_5 == $("input[name=std_core_5]").val()){
	        	$('input:checkbox[id="std_core_5"]').attr("checked", true)
	        }
	        if(sessionVal.STD_CORE_6 == $("input[name=std_core_6]").val()){
	        	$('input:checkbox[id="std_core_6"]').attr("checked", true)
	        }
	        if(sessionVal.STD_CORE_7 == $("input[name=std_core_7]").val()){
	        	$('input:checkbox[id="std_core_7"]').attr("checked", true)
	        }
	        if(sessionVal.STD_CORE_8 == $("input[name=std_core_8]").val()){
	        	$('input:checkbox[id="std_core_8"]').attr("checked", true)
	        }
	        
	        _SEARCH.TOPIC	 		= sessionVal.TOPIC;
    		_SEARCH.UNIV     		= sessionVal.UNIV;
    		_SEARCH.DEPT 			= sessionVal.DEPT;
    		_SEARCH.MAJOR 			= sessionVal.MAJOR;
    		_SEARCH.YEAR 	 		= sessionVal.YEAR;
    		_SEARCH.SEMESTER 		= sessionVal.SEMESTER;
    		_SEARCH.GRADE 	 		= sessionVal.GRADE;
    		_SEARCH.OPEN 			= sessionVal.OPEN;
    		
    		_SEARCH.STD_COMPLET_1 	= sessionVal.STD_COMPLET_1;
    		_SEARCH.STD_COMPLET_2 	= sessionVal.STD_COMPLET_2;
    		_SEARCH.STD_COMPLET_3 	= sessionVal.STD_COMPLET_3;
    		_SEARCH.STD_COMPLET_4 	= sessionVal.STD_COMPLET_4;
    		_SEARCH.STD_COMPLET_5 	= sessionVal.STD_COMPLET_5;
    		_SEARCH.STD_COMPLET_6 	= sessionVal.STD_COMPLET_6;
    		_SEARCH.STD_COMPLET_7 	= sessionVal.STD_COMPLET_7;
    		_SEARCH.STD_COMPLET_8 	= sessionVal.STD_COMPLET_8;
    		_SEARCH.STD_CORE_1 		= sessionVal.STD_CORE_1;
    		_SEARCH.STD_CORE_2 		= sessionVal.STD_CORE_2;
    		_SEARCH.STD_CORE_3		= sessionVal.STD_CORE_3;
    		_SEARCH.STD_CORE_4		= sessionVal.STD_CORE_4;
    		_SEARCH.STD_CORE_5 		= sessionVal.STD_CORE_5;
    		_SEARCH.STD_CORE_6 		= sessionVal.STD_CORE_6;
    		_SEARCH.STD_CORE_7 		= sessionVal.STD_CORE_7;
    		_SEARCH.STD_CORE_8 		= sessionVal.STD_CORE_8;
    		
    		sessionStorage.removeItem("sbjtSearchKeyword"); // 삭제
	    } else {
	    	if(typeof($("#search").val()) == "undefined" || $("#search").val() == '') return getInitSbjtList(1);
	    	
	        // searchKeyword가 JSON 문자열이 아닌 경우 처리
	    	$("input[name=search]").val(searchKeyword);
			
			_SEARCH.TOPIC	 		= $("#search").val();
			_SEARCH.UNIV     		= $("#college1").val();
			_SEARCH.DEPT 			= $("#college2").val();
			_SEARCH.MAJOR 			= $("#college3").val();
			_SEARCH.YEAR 	 		= $("#yearI").val();
			_SEARCH.SEMESTER 		= $("#semeI").val();
			_SEARCH.GRADE 	 		= $("#gradeI").val();
			_SEARCH.OPEN 			= $("#openI").val();
			
			_SEARCH.STD_COMPLET_1 	= $("input[name=std_complet_1]:checked").val();
			_SEARCH.STD_COMPLET_2 	= $("input[name=std_complet_2]:checked").val();
			_SEARCH.STD_COMPLET_3 	= $("input[name=std_complet_3]:checked").val();
			_SEARCH.STD_COMPLET_4 	= $("input[name=std_complet_4]:checked").val();
			_SEARCH.STD_COMPLET_5 	= $("input[name=std_complet_5]:checked").val();
			_SEARCH.STD_COMPLET_6 	= $("input[name=std_complet_6]:checked").val();
			_SEARCH.STD_COMPLET_7 	= $("input[name=std_complet_7]:checked").val();
			_SEARCH.STD_COMPLET_8 	= $("input[name=std_complet_8]:checked").val();
			_SEARCH.STD_CORE_1 		= $("input[id='std_core_1']:checked").val();
			_SEARCH.STD_CORE_2 		= $("input[id='std_core_2']:checked").val();
			_SEARCH.STD_CORE_3		= $("input[id='std_core_3']:checked").val();
			_SEARCH.STD_CORE_4		= $("input[id='std_core_4']:checked").val();
			_SEARCH.STD_CORE_5 		= $("input[id='std_core_5']:checked").val();
			_SEARCH.STD_CORE_6 		= $("input[id='std_core_6']:checked").val();
			_SEARCH.STD_CORE_7 		= $("input[id='std_core_7']:checked").val();
			_SEARCH.STD_CORE_8 		= $("input[id='std_core_8']:checked").val();
			
			sessionStorage.removeItem("sbjtSearchKeyword"); // 삭제
	    }
	    
	    getSbjtList(1);  
	}else{
		getInitSbjtList(1);
	}
	
	
	
});


function isJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}


function getInitSbjtList(page){
	
	_page = page;
	
	var flagLog 	= "N";
	
	var mId 		= _SEARCH.MID;
	var topic 		= _SEARCH.TOPIC;
	var univ 		= _SEARCH.UNIV;
	var depart 		= _SEARCH.DEPT;
	var major 		= _SEARCH.MAJOR;
	var year 		= _SEARCH.YEAR;
	var semester 	= _SEARCH.SEMESTER;
	var grade 		= _SEARCH.GRADE;
	var open 		= _SEARCH.OPEN;
	
	
	var std_complet_1 = _SEARCH.STD_COMPLET_1;
	var std_complet_2 = _SEARCH.STD_COMPLET_2;
	var std_complet_3 = _SEARCH.STD_COMPLET_3;
	var std_complet_4 = _SEARCH.STD_COMPLET_4;
	var std_complet_5 = _SEARCH.STD_COMPLET_5;
	var std_complet_6 = _SEARCH.STD_COMPLET_6;
	var std_complet_7 = _SEARCH.STD_COMPLET_7;
	var std_complet_8 = _SEARCH.STD_COMPLET_8;
	var std_core_1 = _SEARCH.STD_CORE_1;
	var std_core_2 = _SEARCH.STD_CORE_2;
	var std_core_3 = _SEARCH.STD_CORE_3;
	var std_core_4 = _SEARCH.STD_CORE_4;
	var std_core_5 = _SEARCH.STD_CORE_5;
	var std_core_6 = _SEARCH.STD_CORE_6;
	var std_core_7 = _SEARCH.STD_CORE_7;
	var std_core_8 = _SEARCH.STD_CORE_8;
	
	var orderBy		= _SEARCH.ORDERBY;
	showLoading();
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json',
  		url : '/web/sbjt/initSbjtList.do?mId=32',
  		data: { 
  			mId : "32",
  			top_search : topic,
  			univ : univ,
  			depart : depart,
  			major : major,
  			year : year,
  			semester : semester,
  			grade : grade,
  			open : open,
  			std_complet_1 : std_complet_1,
  			std_complet_2 : std_complet_2,
  			std_complet_3 : std_complet_3,
  			std_complet_4 : std_complet_4,
  			std_complet_5 : std_complet_5,
  			std_complet_6 : std_complet_6,
  			std_complet_7 : std_complet_7,
  			std_complet_8 : std_complet_8,
  			std_core_1 : std_core_1,
  			std_core_2 : std_core_2,
  			std_core_3 : std_core_3,
  			std_core_4 : std_core_4,
  			std_core_5 : std_core_5,
  			std_core_6 : std_core_6,
  			std_core_7 : std_core_7,
  			std_core_8 : std_core_8,
  			orderBy : orderBy,
  			flagLog : flagLog,
  			page : page
  		},
  		async:true, 
  		success:function(result){
  			var varItemObj = $("#sbjtList");
  			var htmlPage = $("#page");
  			
  			var topic = $("#search").val();
  			
  			varItemObj.empty();
  			htmlPage.empty();
  			
  			$("#header").remove(); 
			var vartopic = '<h5 class="search_res mb-3 test" id="header"><strong class="fw-bolder">전체검색</strong>에 대한 총 <span>' + result.totalCount + '</span>건의 게시글이 있습니다.</h5>\n';
			$(".sbjt_title_boxselect").before(vartopic);
  			
  			var list = result.sbjtList;
  			console.log(list);
			$.each(list, function(i, item){
				var subjectKey = item.SUBJECT_CD;
				var deptKey = (typeof(item.MAJOR_CD) != "undefined" && item.MAJOR_CD != "") ? item.MAJOR_CD : item.DEPT_CD;
				if(typeof(deptKey) == "undefined" ){
					deptKey = item.COLG_CD;
				} 
				var majorNm = (typeof(item.MAJOR_CD) != "undefined" && item.MAJOR_CD != "") ? item.MAJOR_NM : "";
				var $elemMajorNm = (majorNm != "") ? '<span>' + majorNm + '</span>' : ""; 
	            var year = item.YEAR;
	            var smtCd = item.SMT_CD;
	            var subjectNm = (typeof(item.SUBJECT_NM) != "undefined" && item.SUBJECT_NM != "") ? item.SUBJECT_NM : '-';
	            var id = subjectKey + "_" + deptKey + "_" + year + "_" + item.GRADE + "_" + smtCd;
				var onRed = "";
				
				
				if(bookmarkList != undefined){	
					//console.log(bookmarkList);
		        	bookmarkList.forEach(function(bookmark) {	            		
		        		if(bookmark.docId == id){
		            		 onRed = 'on_red';            			
		        		}
			 		});
		    	}
	            
	            
 				var varCon = `<div class="item border" onclick="sbjtView('\${subjectKey}', '\${deptKey}', '\${year}', '\${smtCd}')" style="cursor:pointer;">\n`;
 				
 				varCon += '<div id="' + id + '" class="like_container ' + onRed + '">\n';
 				varCon += '<div class="link_cnt text-end">\n';
 				varCon += '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18"  onclick="event.stopPropagation();likeChange(\''+ id + '\', \'sbjt\')">\n';
 				varCon += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>\n';
 				varCon += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>\n';
 				varCon += '</div>\n';
 	  			varCon += '</div>\n';
 	  			
 	  			varCon += '<ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-3">\n';
 	  				
				if(item.COMDIV_NM == '전공과목'){
					varCon += '<li class="maj">' + item.COMDIV_NM + '</li>\n';
				} else if(item.COMDIV_NM == '전공기초'){
					varCon += '<li class="maj_basic">' + item.COMDIV_NM + '</li>\n';
				} else if(item.COMDIV_NM == '전공필수'){
					varCon += '<li class="maj_esse">' + item.COMDIV_NM + '</li>\n';
				} else  if(item.COMDIV_NM == '전공선택'){
					varCon += '<li class="maj_choice">' + item.COMDIV_NM + '</li>\n';
				} else if(item.COMDIV_NM == '일반선택'){
					varCon += '<li class="general_selection">' + item.COMDIV_NM + '</li>\n';
				} else if((item.COMDIV_NM).indexOf('교양') != -1){
					varCon += '<li class="refin">' + item.COMDIV_NM + '</li>\n';
				} else{
					varCon += '<li class="">' + item.COMDIV_NM + '</li>\n';
				}
				

				varCon += '<li class="name_of_class"><span>' + item.COLG_NM + '</span>';
				if(item.ORG_LVL == '3'){
					if(item.DEPT_NM != "" && item.DEPT_NM != "교양교육원" && item.DEPT_NM != "학사과"){
						varCon += '<span>' + item.DEPT_NM + '</span>';	
					}	
				}
				if(item.ORG_LVL == '4'){
					if(item.DEPT_NM != "" && item.DEPT_NM != "교양교육원" && item.DEPT_NM != "학사과"){
						varCon += '<span>' + item.UP_DEPT_NM + '</span>';	
					}	
				}
				varCon += $elemMajorNm + '</li>\n';
				/* varCon += '<li class="name_of_class"><span>' + item.COLG_NM + '</span><span>' + item.DEPT_NM + '</span>' + $elemMajorNm + '</li>\n'; */
				if(typeof(item.MAIN_ABI_NM) != "undefined"){
					varCon += '<li class="name_of_class"><span>' + item.MAIN_ABI_NM + '</span></li>\n';
				}
				varCon += '</ul>\n';
				varCon += '<h5 class="title ellip_2 mb-3">\n';
				varCon += `<a href="javascript:void(0);" onclick="sbjtView('\${subjectKey}', '\${deptKey}', '\${year}', '\${smtCd}')" title="제목" class="d-block  fw-semibold"> \${subjectNm}(\${subjectKey})</a>\n`;
				varCon += '</h5>\n';
				varCon += '<p class="desc_txt mb-4 ellip_2">';
				if(typeof(item.SUBJ_DESC) == "undefined" || item.SUBJ_DESC == ""){
					varCon += '해당 교과목에 대한 정보가 없습니다.';
				} else {
					varCon += item.SUBJ_DESC;
				}
				varCon += '</p>\n';
				varCon += '<ul class="info d-flex flex-wrap align-items-start">';
				varCon += '<li class="d-flex flex-row col-12 col-xl-6 mb-2"><strong>최근편성</strong><span class="rounded-pill">' + item.YEAR + '년 ' + item.SMT_NM + '</span></li>\n';
				varCon += '<li class="d-flex flex-row col-12 col-xl-6 mb-2"><strong>과정구분</strong><span>' + item.SINBUN_NM + '</span></li>\n';
				varCon += '<li class="d-flex flex-row col-12 col-xl-6 mb-2 mb-xl-0"><strong>학점</strong><span class="rounded-pill">' + item.CDT_NUM + ' 학점</span></li>\n';
				varCon += '<li class="d-flex flex-row col-12 col-xl-6 mb-2 mb-xl-0"><strong>이수학년</strong><span>' + (item.GRADE == '0' ? '전체' : item.GRADE) + '학년</span></li>\n';
				varCon += '</ul>\n';
				varCon += '</div>\n';
				
				varItemObj.append(varCon);
				
				
			});
			
			/* 교과목 리스트 페이징  */
  			var pageInfo = result.paginationInfo;
  			
  			var currentPageNo = pageInfo.currentPageNo;
  			var totalPageCount = pageInfo.totalPageCount;
  			var pageSize = pageInfo.pageSize;
  			var firstPageNo = pageInfo.firstPageNo;
  			var lastPageNo = pageInfo.lastPageNo;
  			var firstPageNoOnPageList = pageInfo.firstPageNoOnPageList;
  			var lastPageNoOnPageList = pageInfo.lastPageNoOnPageList;
  			var pageCon = "";
  			
  			
  			if(totalPageCount > pageSize){
  				if(firstPageNoOnPageList > pageSize){
  					var prevPageNo = firstPageNoOnPageList - pageSize;
  				} else{
  					var prevPageNo = firstPageNo;
  				}
  				if(currentPageNo != firstPageNo){
  					pageCon += '<li class="page-item page-first"><a href="javascript:getInitSbjtList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
  	  				pageCon += '<li class="page-item page-prev"><a href="javascript:getInitSbjtList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
  			
	  		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
	  			if(pageNo == currentPageNo){
	  				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getInitSbjtList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
	  			} else{
	  				pageCon += '<li class="page-item"><a class="page-link" href="javascript:getInitSbjtList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
	  			}
	  		}
  	  		if(totalPageCount > pageSize){
  				if(lastPageNoOnPageList < totalPageCount){
  					var nextPageNo = firstPageNoOnPageList + pageSize;
  				} else{
  					var nextPageNo = lastPageNo;
  				}
  				if(currentPageNo != lastPageNo){
  					pageCon += '<li class="page-item page-next"><a href="javascript:getInitSbjtList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
  	  	  			pageCon += '<li class="page-item page-last"><a href="javascript:getInitSbjtList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
	  		
  			htmlPage.append(pageCon);
  			hideLoading();
  			$("#checkSearch").val("N");
  				
  			return false;
  		}, 
  		error:function(request,error){
  			alert("교과목 목록 실패");
  		}
	}); 
}


function getSbjtList(page){
	if($("#search").val().trim() == ""){
		getInitSbjtList(page);
		return false;
	}

	var form = document.searchForm;
	
	_page = page;
	
	var flagLog 	= "N";
	
	if(_SEARCH.FLAG != ""){
		flagLog = _SEARCH.FLAG;
	}
	
	var mId 		= _SEARCH.MID;
	var topic 		= _SEARCH.TOPIC;
	var univ 		= _SEARCH.UNIV;
	var depart 		= _SEARCH.DEPT;
	var major 		= _SEARCH.MAJOR;
	var year 		= _SEARCH.YEAR;
	var semester 	= _SEARCH.SEMESTER;
	var grade 		= _SEARCH.GRADE;
	var open 		= _SEARCH.OPEN;
	
	
	var std_complet_1 = _SEARCH.STD_COMPLET_1;
	var std_complet_2 = _SEARCH.STD_COMPLET_2;
	var std_complet_3 = _SEARCH.STD_COMPLET_3;
	var std_complet_4 = _SEARCH.STD_COMPLET_4;
	var std_complet_5 = _SEARCH.STD_COMPLET_5;
	var std_complet_6 = _SEARCH.STD_COMPLET_6;
	var std_complet_7 = _SEARCH.STD_COMPLET_7;
	var std_complet_8 = _SEARCH.STD_COMPLET_8;
	var std_core_1 = _SEARCH.STD_CORE_1;
	var std_core_2 = _SEARCH.STD_CORE_2;
	var std_core_3 = _SEARCH.STD_CORE_3;
	var std_core_4 = _SEARCH.STD_CORE_4;
	var std_core_5 = _SEARCH.STD_CORE_5;
	var std_core_6 = _SEARCH.STD_CORE_6;
	var std_core_7 = _SEARCH.STD_CORE_7;
	var std_core_8 = _SEARCH.STD_CORE_8;
	
	var orderBy		= _SEARCH.ORDERBY;
	
	showLoading();
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json',
  		url : '/web/sbjt/sbjtList.do?mId=32',
  		data: { 
  			mId : mId,
  			top_search : topic,
  			univ : univ,
  			depart : depart,
  			major : major,
  			year : year,
  			semester : semester,
  			grade : grade,
  			open : open,
  			std_complet_1 : std_complet_1,
  			std_complet_2 : std_complet_2,
  			std_complet_3 : std_complet_3,
  			std_complet_4 : std_complet_4,
  			std_complet_5 : std_complet_5,
  			std_complet_6 : std_complet_6,
  			std_complet_7 : std_complet_7,
  			std_complet_8 : std_complet_8,
  			std_core_1 : std_core_1,
  			std_core_2 : std_core_2,
  			std_core_3 : std_core_3,
  			std_core_4 : std_core_4,
  			std_core_5 : std_core_5,
  			std_core_6 : std_core_6,
  			std_core_7 : std_core_7,
  			std_core_8 : std_core_8,
  			orderBy : orderBy,
  			flagLog : flagLog,
  			page : page
  		},
  		async:true, 
  		success:function(result){
  			
  			var varItemObj = $("#sbjtList");
  			var htmlPage = $("#page");
  			
  			var topic = $("#search").val();
  			var totalCount = result.totalCount;
  			
  			if(topic != ""){
  				$("#header").remove();
  				var vartopic = '<h5 class="search_res mb-3 test" id="header"><strong class="fw-bolder">' + topic + '</strong>에 대한 총 <span>' + totalCount + '</span>건의 게시글이 있습니다.</h5>\n';
  				$(".sbjt_title_boxselect").before(vartopic);
  			} else {
  				$("#header").remove();
  				var vartopic = '<h5 class="search_res mb-3 test" id="header"><strong class="fw-bolder">전체검색</strong>에 대한 총 <span>' + totalCount + '</span>건의 게시글이 있습니다.</h5>\n';
  				$(".sbjt_title_boxselect").before(vartopic);
  			}
  			
  			varItemObj.empty();
  			htmlPage.empty();
  			var list = result.sbjtList;
  			console.log(list);
			$.each(list, function(i, item){
				var subjectKey = item.subjectCd;
				var deptKey = (typeof(item.majorCd) != "undefined" && item.majorCd != "") ? item.majorCd : item.deptCd;
				if(deptKey == ""){
					deptKey = item.colgCd;
				}
				var abiNm = "전공능력";
				var abi   = (typeof(item.majorAbi) == "undefined" || item.majorAbi == "") ? "-" : item.majorAbi;
				if((item.comdivNm).indexOf('교양') != -1){
					console.log("씨부레");
					abiNm = "핵심역량"
					abi   = (typeof(item.essentialAbi) == "undefined" || item.essentialAbi == "") ? "-" : item.essentialAbi;
				}
				
				
	            var year = item.year;
	            var smtCd = item.smtCd;
	            var subjectNm = (typeof(item.subjectNm) != "undefined" && item.subjectNm != "") ? item.subjectNm : '-';
	            var id = subjectKey + "_" + deptKey + "_" + year + "_" + item.grade + "_" + smtCd;
				var onRed = "";
				if(bookmarkList != undefined){
		        	bookmarkList.forEach(function(bookmark) {	            		
		        		if(bookmark.docId == id){
		            		 onRed = 'on_red';            			
		        		}
			 		});
		    	}
				 
				var varCon = '';			
 				varCon += `<div class="item border" onclick="sbjtView('\${subjectKey}', '\${deptKey}', '\${year}', '\${smtCd}')" style="cursor:pointer;">\n`;
 				varCon += '<div id="' + id + '" class="like_container ' + onRed + '">\n';
 				varCon += '<div class="link_cnt text-end">\n';
 				varCon += '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="event.stopPropagation();likeChange(\''+ id + '\', \'sbjt\')">\n';
 				varCon += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>\n';
 				varCon += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>\n';
 				varCon += '</div>\n';
 	  			varCon += '</div>\n';
 					
 	  			varCon += '<ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-3">\n';
 	  				
				if(item.comdivNm == '전공과목'){
					varCon += '<li class="maj">' + item.comdivNm + '</li>\n';
				} else if(item.comdivNm == '전공기초'){
					varCon += '<li class="maj_basic">' + item.comdivNm + '</li>\n';
				} else if(item.comdivNm == '전공필수'){
					varCon += '<li class="maj_esse">' + item.comdivNm + '</li>\n';
				} else  if(item.comdivNm == '전공선택'){
					varCon += '<li class="maj_choice">' + item.comdivNm + '</li>\n';
				} else if(item.comdivNm == '일반선택'){
					varCon += '<li class="general_selection">' + item.comdivNm + '</li>\n';
				} else if((item.comdivNm).indexOf('교양') != -1){
					varCon += '<li class="refin">' + item.comdivNm + '</li>\n';
				} else{
					varCon += '<li class="">' + item.comdivNm + '</li>\n';
				}
				
				varCon += '<li class="name_of_class"><span>' + item.colgNm + '</span>';
				if(item.deptNm != "" && item.deptNm != "학사과"){
					varCon += '<span>' + item.deptNm + '</span>';	
				}
				varCon += '</li>\n';
				if((item.comdivNm).indexOf('전공') != -1 || (item.comdivNm).indexOf('일반') != -1){
					var majorAbi = item.majorAbi;
					if(typeof(majorAbi) != 'undefined' && majorAbi != ''){
						varCon += '<li class="name_of_class"><span>' + majorAbi + '</span></li>\n';	
					}
					
				} 
				if((item.comdivNm).indexOf('교양') != -1) {
					var essentialAbi = item.essentialAbi;
					
					if(typeof(essentialAbi) != 'undefined' && essentialAbi != ''){
						varCon += '<li class="name_of_class"><span>' + essentialAbi + '</span></li>\n'; 
					}	
				}
				varCon += '</ul>\n';
				varCon += '<h5 class="title ellip_2 mb-3">\n';
				varCon += `<a href="javascript:void(0);" onclick="sbjtView('\${subjectKey}', '\${deptKey}', '\${year}', '\${smtCd}')" title="제목" class="d-block  fw-semibold"> \${subjectNm}(\${subjectKey})</a>\n`;
				varCon += '</h5>\n';
				varCon += '<p class="desc_txt mb-4 ellip_2">';
				if(item.subjDescKor == ""){
					varCon += '해당 교과목에 대한 정보가 없습니다.';
				} else {
					varCon += item.subjDescKor;
				}
				varCon += '</p>\n';
				varCon += '<ul class="info d-flex flex-wrap align-items-start">';
				varCon += '<li class="d-flex flex-row col-12 col-xl-6 mb-2"><strong>최근편성</strong><span class="rounded-pill">' + item.year + '년 ' + item.smtNm + '</span></li>\n';
				varCon += '<li class="d-flex flex-row col-12 col-xl-6 mb-2"><strong>과정구분</strong><span>' + item.sinbunNm + '</span></li>\n';
				varCon += '<li class="d-flex flex-row col-12 col-xl-6 mb-2 mb-xl-0"><strong>학점</strong><span class="rounded-pill">학점 ' + item.cdtNum + '</span></li>\n';
				varCon += '<li class="d-flex flex-row col-12 col-xl-6 mb-2 mb-xl-0"><strong>이수학년</strong><span>' + (item.grade == '0' ? '전체' : item.grade) + '학년</span></li>\n';
				/* varCon += '<li class="d-flex flex-row col-12 col-xl-6 mb-2"><strong>' + abiNm + '</strong><span>' + abi + '</span></li>\n'; */
				varCon += '</ul>\n';
				varCon += '</div>\n';
				
				varItemObj.append(varCon);
			});
			
			/* 교과목 리스트 페이징  */
  			var pageInfo = result.paginationInfo;
  			
  			var currentPageNo = pageInfo.currentPageNo;
  			var totalPageCount = pageInfo.totalPageCount;
  			var pageSize = pageInfo.pageSize;
  			var firstPageNo = pageInfo.firstPageNo;
  			var lastPageNo = pageInfo.lastPageNo;
  			var firstPageNoOnPageList = pageInfo.firstPageNoOnPageList;
  			var lastPageNoOnPageList = pageInfo.lastPageNoOnPageList;
  			var pageCon = "";
  			
  			
  			if(totalPageCount > pageSize){
  				if(firstPageNoOnPageList > pageSize){
  					var prevPageNo = firstPageNoOnPageList - pageSize;
  				} else{
  					var prevPageNo = firstPageNo;
  				}
  				if(currentPageNo != firstPageNo){
  					pageCon += '<li class="page-item page-first"><a href="javascript:getSbjtList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
  	  				pageCon += '<li class="page-item page-prev"><a href="javascript:getSbjtList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
  			
  			
	  		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
	  			if(pageNo == currentPageNo){
	  				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getSbjtList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
	  			} else{
	  				pageCon += '<li class="page-item"><a class="page-link" href="javascript:getSbjtList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
	  			}
	  		}
  	  		if(totalPageCount > pageSize){
  				if(lastPageNoOnPageList < totalPageCount){
  					var nextPageNo = firstPageNoOnPageList + pageSize;
  				} else{
  					var nextPageNo = lastPageNo;
  				}
  				if(currentPageNo != lastPageNo){
  					pageCon += '<li class="page-item page-next"><a href="javascript:getSbjtList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
  	  	  			pageCon += '<li class="page-item page-last"><a href="javascript:getSbjtList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
	  		
  			htmlPage.append(pageCon);
  			hideLoading();
  			$("#checkSearch").val("N");
  				
  			return false;
  		}, 
  		error:function(request,error){
  			alert("교과목 목록 실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
	}); 
}


function getDepartList(univ, dpCd){
	
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:'/web/sbjt/DepartAjax.json?mId=32&UNIV=' + univ, 
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
  			alert("실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	}); 
}

function getMajorList(depart, majorCd){
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:'/web/sbjt/MajorAjax.json?mId=32&DEPART=' + depart, 
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
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	}); 
}

/* 상세보기(교과목코드, 부서코드, 년도, 학기) */
function sbjtView(a,b,c,d){
	var form = document.viewForm;
	form.SUBJECT_CD.value = a;
	form.DEPT_CD.value = b;
	form.YEAR.value = c;
	form.SMT.value = d;
	form.action ="view.do";
	form.submit();
}

/* 필터 초기화 */
function reloadFilter(){
    $('input[type="text"]').val('');
    
    $('input[type="checkbox"]').prop('checked', false);
    
    $('input[type="radio"]').prop('checked', false);
    
    //학부(과), 전공은 첫번째빼고 모든 옵션 없애기
    $('#college2, #college3').each(function() {
        $(this).find('option').not(':first').remove();
        this.selectedIndex = 0; 
    });
    
    $('select').not('#college2, #college3').each(function() {
        this.selectedIndex = 0;
    });
    
    //학부(과), 전공에 disabled 추가
	$("#college2").attr("disabled", true); 
	$("#college3").attr("disabled", true); 
}

//찜 등록/삭제
function likeChange(docId, type){
	if(!$("#"+docId).hasClass("on_red")){
		$.ajax({
			url: '/web/bookmark/insertBookmark.do?mId=37&menuFg=' + type,
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    docId : docId
			}),
			success: function(data){
				$("#"+docId).addClass("on_red");
			}
		});
	} else{
		$.ajax({
			url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg=' + type,
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    docId : docId
			}),
			success: function(data){
				$("#"+docId).removeClass("on_red");
			}
		});
	}
}
// 찜 가져오기
function getBookmarkList(){
	return new Promise((resolve, reject) => {		
		$.ajax({
			url: '/web/bookmark/getBookmarkList.do?mId=37',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    menuFg : 'sbjt'
			}),
			success: function(data){			
				resolve(data.bookmarkList);
			},error:function() {
				reject("");
			}
		});
	})
	
}

//로딩 바	
function showLoading() {
	const loader = document.querySelector('.loader');
	const overlay = document.getElementById('overlay');
	loader.style.display = 'block';	
	overlay.style.display = 'block';
	
}	
//로딩바 hide	
function hideLoading() {
	const loader = document.querySelector('.loader');
	const overlay = document.getElementById('overlay');
	loader.style.display = 'none';
	overlay.style.display = 'none';	
}	

function asdf(str){ console.log(str); }
</script>