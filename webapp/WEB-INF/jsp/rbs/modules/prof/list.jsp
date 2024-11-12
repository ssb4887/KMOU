<%@ include file="../../include/commonTop.jsp"%>
<script>
let bookmarkList;

var _SEARCH = {
		MID 			: "33",	// 메뉴번호
		TOPIC			: "",	// 검색 키
		UNIV 			: "",	// 대학
		DEPT 			: "",	// 학부(과)
		MAJOR 			: "",	// 전공
		ORDERBY			: "",	// 정렬기준
		FLAG			: ""	// 로그FLAG
};

var _page = "";	

$(function(){
	
	/* 주관대학 - 학부(과) 셀렉트박스 */
	$("#college1").change(function(){
		var univ = $(this).val();				
		
		var varAction = "${elfn:replaceScriptLink(URL_DEPARTLIST)}&UNIV=" + univ;
		
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
		
		var varAction = "${elfn:replaceScriptLink(URL_MAJORLIST)}&DEPART=" + depart;
		$("#college3").append('<option value="">전공</option>\n');
		
		/* if($(this).find("option:selected").text().slice(-2) == '학부'){
			$("#college3").removeAttr("disabled");			
			getMajorList(depart, varAction);
		}else{
			$("#college3").find("option").remove();
			$("#college3").append('<option value="">전공</option>\n');	
			$("#college3").attr("disabled", true);
		} */
			
	});
	 
	getBookmarkList().then(data => {
		bookmarkList = data;
	});
	
	
	var keyword = $("#top_search").val();
	if(keyword != "") getProfList(1);
	
	$("#top_search").on("keyup",function(key){
		if(key.keyCode==13) {
			_SEARCH.TOPIC	 		= $("#top_search").val();
			_SEARCH.UNIV     		= $("#college1").val();
			_SEARCH.DEPT 			= $("#college2").val();
			_SEARCH.MAJOR 			= $("#college3").val();
			_SEARCH.ORDERBY 		= $("#orderBy").val();
			_SEARCH.FLAG 			= "Y";
			
			/* sessionStorage.setItem('profSearchKeyword', JSON.stringify(_SEARCH)); */
			
			getProfList(1);      
			
			_SEARCH.FLAG 			= "";
		}    
	});
	
	$(".pk_btn").click(function(){
		_SEARCH.TOPIC	 		= $("#top_search").val();
		_SEARCH.UNIV     		= $("#college1").val();
		_SEARCH.DEPT 			= $("#college2").val();
		_SEARCH.MAJOR 			= $("#college3").val();
		_SEARCH.ORDERBY 		= $("#orderBy").val();
		_SEARCH.FLAG 			= "Y";
		
		/* sessionStorage.setItem('profSearchKeyword', JSON.stringify(_SEARCH)); */
		
		getProfList(1);
		
		_SEARCH.FLAG 			= "";
	});
	
	/* 정렬기능 */
	$('#orderBy').change(function() {
		_SEARCH.ORDERBY 		= $("#orderBy").val();
		
		if($("#top_search").val().trim() == ""){
			getInitProfList(_page);
		}else{
			getProfList(_page); 
		}
		
    });
	
	var sessionVal = new Object();
	//세션에 들어가있는 검색값(메인페이지에서 더보기 용 + 새로고침시 값 유지)
	searchKeyword = sessionStorage.getItem('profSearchKeyword');
	if (searchKeyword) {
	    if (isJsonString(searchKeyword)) {
	        sessionVal = JSON.parse(searchKeyword);
	        
	        if(typeof(sessionVal.TOPIC) == "undefined" || sessionVal.TOPIC == '') return getInitProfList(1);
	        
	        $("input[name=top_search]").val(sessionVal.TOPIC);
	        $("#college1").val(sessionVal.UNIV).prop("selected", true);
	        $("#college2").val(sessionVal.DEPT).prop("selected", true);
	        $("#college3").val(sessionVal.MAJOR).prop("selected", true);
	        
	        _SEARCH.TOPIC	 		= sessionVal.TOPIC;
    		_SEARCH.UNIV     		= sessionVal.UNIV;
    		_SEARCH.DEPT 			= sessionVal.DEPT;
    		_SEARCH.MAJOR 			= sessionVal.MAJOR;
    		
	    } else {
	    	if(typeof(sessionVal.TOPIC) == "undefined" || sessionVal.TOPIC == '') return getInitProfList(1);
	    	
	        // searchKeyword가 JSON 문자열이 아닌 경우 처리
	    	$("input[name=top_search]").val(searchKeyword);
			
			_SEARCH.TOPIC	 		= $("#top_search").val();
			_SEARCH.UNIV     		= $("#college1").val();
			_SEARCH.DEPT 			= $("#college2").val();
			_SEARCH.MAJOR 			= $("#college3").val();
			
	    }
	    
	    getProfList(1);  
	}else{
		getInitProfList(1);
	}

	sessionStorage.removeItem("profSearchKeyword"); // 삭제
	
	var isUniv = "${param.univ}";
	var isDepart = "${param.depart}";
	var isMajor = "${param.major}";
	
	if(isUniv != ""){
		var varAction = "${elfn:replaceScriptLink(URL_DEPARTLIST)}&UNIV=" + isUniv;
		getDepartList(isUniv, varAction);
	}
	
	if(isDepart != ""){
		var varAction = "${elfn:replaceScriptLink(URL_MAJORLIST)}&DEPART=" + isDepart;
		getMajorList(isDepart, varAction);
		$("#college3").val(isMajor);
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

/* 최초 교수 목록 조회 */
function getInitProfList(page){
	
	_page = page;
	
	var orderBy		= _SEARCH.ORDERBY;
	
	var mId 		= _SEARCH.MID;
	var topic 		= _SEARCH.TOPIC;
	var univ 		= _SEARCH.UNIV;
	var depart 		= _SEARCH.DEPT;
	var major 		= _SEARCH.MAJOR;
	var orderBy		= _SEARCH.ORDERBY;
	showLoading();
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json',
  		url : '/web/prof/initProfList.do?mId=33',
  		data: { 
  			mId : "33",
  			univ : univ,
  			depart : depart,
  			major : major,
  			orderBy : orderBy,
  			page : page
  		},
  		async:true, 
  		success:function(result){
  			var varItemObj = $("#profList");
  			var htmlPage = $("#page");
  			
  			var topic = $("#top_search").val();
  			var totalCount = result.totalCount;
  			
  			$("#header").remove();
  			varItemObj.empty();
  			htmlPage.empty();
  			
  			var list = result.profList;
  			
  			
			$("#header").remove();
			var vartopic = '<h5 class="search_res mb-3" id="header"><strong class="fw-bolder">전체검색</strong>에 대한 총 <span>' + totalCount + '</span>건의 게시글이 있습니다.</h5>\n';
			$(".prof_title_boxselect").before(vartopic);
  			
  			
			$.each(list, function(i, item){
				var empKey = item.EMP_NO;
				var empNm = (typeof(item.EMP_NM) == 'undefined' || item.EMP_NM == "") ? '-' : item.EMP_NM;
				var imagePath = (typeof(item.TEA_FILE_PATH) == 'undefined' || item.TEA_FILE_PATH == "") ? '' : item.TEA_FILE_PATH;
				var colgNm = (typeof(item.COLG_NM) == 'undefined' || item.COLG_NM == "") ? '-' : item.COLG_NM;
				var deptNm = (typeof(item.DEPT_NM) == 'undefined' || item.DEPT_NM == "") ? '-' : item.DEPT_NM;
				
				var email = (typeof(item.EMAIL) == 'undefined' || item.EMAIL == "") ? '-' : item.EMAIL;
				var telNo = (typeof(item.TEL_NO) == 'undefined' || item.TEL_NO == "") ? '-' : item.TEL_NO;
				var research = (typeof(item.TEA_RSRCH_REALM) == 'undefined' || item.TEA_RSRCH_REALM == "") ? '-' : item.TEA_RSRCH_REALM;
				var location = (typeof(item.LOCATION) == 'undefined' || item.LOCATION == "") ? '-' : item.LOCATION;
				
				imagePath = (imagePath != '') ? "https://www.kmou.ac.kr/" + imagePath : '../images/profile.png'; 
				var $colgNm = (colgNm != '-') ? '<em class="fst-normal d-inline-block">' + colgNm + '</em>' : '';
				
			 	var id = empKey;
				var onRed = "";
				if(bookmarkList != undefined){
		        	bookmarkList.forEach(function(bookmark) {	            		
		        		if(bookmark.docId == id){
		            		 onRed = 'on_red';            			
		        		}
			 		});
		    	}
				
				
 				var varCon = `<div class="item border" onclick="profView('\${empKey}')" style="cursor:pointer;">\n`;
 				varCon += '<div id="' + id + '" class="like_container ' + onRed + '">\n';
 				varCon += '<div class="link_cnt text-end">\n';
 				varCon += '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18"  onclick="event.stopPropagation();likeChange(\''+ id + '\', \'prof\')">\n';
 				varCon += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>\n';
 				varCon += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>\n';
 				varCon += '</div>\n';
 	  			varCon += '</div>\n';
 				
				varCon += `<a href="javascript:void(0);" onclick="profView('\${empKey}')" title="교수" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3">\n`;
				varCon += '<span class="photo_box d-inline-block rounded-circle overflow-hidden">\n';
				varCon += '<img src="' + imagePath + '" alt="" style="width:100%; height:100%; object-fit:cover;"/>';
				varCon += '</span>\n';
				varCon += '<span class="txt_box mb-2 mb-sm-0 mb-md-2">\n';
				varCon += '<span class="d-flex flex-wrap">\n';
				varCon += $colgNm;
				varCon += '<em class="fst-normal d-inline-block">' + deptNm + '</em>\n';
				varCon += '</span>\n';
				varCon += '<strong>' + empNm + '<small class="fw-normal ps-2">교수</small></strong>\n';
				varCon += '</span>\n';
				varCon += '</a>\n';
				varCon += '<ul class="dtl_info pt-4 d-flex flex-column gap-2">\n';
				varCon += '<li class="d-flex flex-row text-break"><strong>이메일</strong>' + email + '</li>\n';
				varCon += '<li class="d-flex flex-row text-break"><strong>전화번호</strong>' + telNo + '</li>\n';
				varCon += '<li class="d-flex flex-row text-break" style="white-space: nowrap;overflow: hidden;text-overflow:ellipsis"><strong>연구분야</strong>' + research + '</li>\n';
				varCon += '<li class="d-flex flex-row text-break"><strong>연구실</strong>' + location + '</li>\n';
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
  					pageCon += '<li class="page-item page-first"><a href="javascript:getInitProfList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
  	  				pageCon += '<li class="page-item page-prev"><a href="javascript:getInitProfList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';
  				}
  			}
  			
	  		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
	  			if(pageNo == currentPageNo){
	  				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getInitProfList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
	  			} else{
	  				pageCon += '<li class="page-item"><a class="page-link" href="javascript:getInitProfList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
	  			}
	  		}
  	  		if(totalPageCount > pageSize){
  				if(lastPageNoOnPageList < totalPageCount){
  					var nextPageNo = firstPageNoOnPageList + pageSize;
  				} else{
  					var nextPageNo = lastPageNo;
  				}
  				if(currentPageNo != lastPageNo){
  					pageCon += '<li class="page-item page-next"><a href="javascript:getInitProfList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
  	  	  			pageCon += '<li class="page-item page-last"><a href="javascript:getInitProfList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
	  		
  			htmlPage.append(pageCon);
  			hideLoading();
  			return false;
  		}, 
  		error:function(request,error){
//   			alert("등록된 교수 정보가 없습니다.");
  		}
	}); 
}

/* 검색 시 교수 목록 조회 */
function getProfList(page){
	if($("#top_search").val().trim() == ""){
		getInitProfList(page);
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
	var orderBy		= _SEARCH.ORDERBY;
	showLoading();
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json',
  		url : '/web/prof/profList.do?mId=33',
  		data: { 
  			mId : mId,
  			top_search : topic,
  			univ : univ,
  			depart : depart,
  			major : major,
  			orderBy : orderBy,
  			flagLog : flagLog,
  			page : page
  		},
  		async:true, 
  		success:function(result){
  			var varItemObj = $("#profList");
  			var htmlPage = $("#page");
  			
  			var topic = $("#top_search").val();
  			var totalCount = result.totalCount;
  			
  			
  			if(topic != ""){
  				$("#header").remove();
  				var vartopic = '<h5 class="search_res mb-3" id="header"><strong class="fw-bolder">' + topic + '</strong>에 대한 총 <span>' + totalCount + '</span>건의 게시글이 있습니다.</h5>\n';
  				$(".prof_title_boxselect").before(vartopic);
  			}

  			varItemObj.empty();
  			htmlPage.empty();
  			
  			var list = result.profList;
  			
			$.each(list, function(i, item){
				 
				var empKey = item.empNo;  
				
				var imagePath = (typeof(item.file_path) == 'undefined' || item.file_path == "") ? '' : item.file_path;
				imagePath = (imagePath != '') ? "https://www.kmou.ac.kr/" + imagePath : '../images/profile.png';
				var rsrch = (typeof(item.rsrch_realm) == 'undefined' || item.rsrch_realm == "") ? '' : item.rsrch_realm;
				
				var id = empKey;
				var onRed = "";
				if(bookmarkList != undefined){
		        	bookmarkList.forEach(function(bookmark) {	            		
		        		if(bookmark.docId == id){
		            		 onRed = 'on_red';            			
		        		}
			 		});
		    	}
				
				
 				var varCon = `<div class="item border" onclick="profView('\${empKey}')" style="cursor:pointer;">\n`;
 				
 				varCon += '<div id="' + id + '" class="like_container ' + onRed + '">\n';
 				varCon += '<div class="link_cnt text-end">\n';
 				varCon += '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18"  onclick="event.stopPropagation();likeChange(\''+ id + '\', \'prof\')">\n';
 				varCon += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>\n';
 				varCon += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>\n';
 				varCon += '</div>\n';
 	  			varCon += '</div>\n';
 				
 				
				varCon += `<a href="javascript:void(0);" onclick="profView('\${empKey}')" title="교수" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3">\n`;
				varCon += '<span class="photo_box d-inline-block rounded-circle overflow-hidden">\n';
				varCon += '<img src="' + imagePath + '" alt="교수님사진" style="width:100%; height:100%; object-fit:cover;"/>';
				/* varCon += '<img src="https://www.kmou.ac.kr/' + item.file_path + '" alt="교수님사진" style="width:100%; height:100%; object-fit:cover;"/>'; */
				varCon += '</span>\n';
				varCon += '<span class="txt_box mb-2 mb-sm-0 mb-md-2">\n';
				varCon += '<strong>' + item.empNm + '<small class="fw-normal ps-2">교수</small></strong>\n';
				varCon += '<span class="d-flex flex-wrap">\n';
				varCon += '<em class="fst-normal d-inline-block">' + item.deptNm + '</em>';
				varCon += '</span>\n';
				varCon += '</span>\n';
				varCon += '</a>\n';
				varCon += '<ul class="dtl_info pt-4 d-flex flex-column gap-2">\n';
				varCon += '<li class="d-flex flex-row text-break"><strong>이메일</strong>' + item.email + '</li>\n';
				varCon += '<li class="d-flex flex-row text-break"><strong>전화번호</strong>' + item.tlphon + '</li>\n';
				varCon += '<li class="d-flex flex-row text-break" style="white-space:nowrap; overflow:hidden; text-overflow:ellipsis;"><strong>연구분야</strong>' + rsrch + '</li>\n';
				varCon += '<li class="d-flex flex-row text-break"><strong>연구실</strong>' + item.location + '</li>\n';
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
  					pageCon += '<li class="page-item page-first"><a href="javascript:getProfList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
  	  				pageCon += '<li class="page-item page-prev"><a href="javascript:getProfList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
  			
  			
	  		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
	  			if(pageNo == currentPageNo){
	  				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getProfList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
	  			} else{
	  				pageCon += '<li class="page-item"><a class="page-link" href="javascript:getProfList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
	  			}
	  		}
  	  		if(totalPageCount > pageSize){
  				if(lastPageNoOnPageList < totalPageCount){
  					var nextPageNo = firstPageNoOnPageList + pageSize;
  				} else{
  					var nextPageNo = lastPageNo;
  				}
  				if(currentPageNo != lastPageNo){
  					pageCon += '<li class="page-item page-next"><a href="javascript:getProfList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
  	  	  			pageCon += '<li class="page-item page-last"><a href="javascript:getProfList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
	  		
  			
  			htmlPage.append(pageCon);
  			hideLoading();   
  				
  			return false;
  		}, 
  		error:function(request,error){
  			alert("등록된 교수 정보가 없습니다.");
  		}
	}); 
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
  			alert("실패");
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
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	}); 
}


/* 상세보기(교과목코드, 부서코드, 년도, 분반) */
function profView(a){
	
	var form = document.viewForm;
	form.empNo.value = a;
	form.action ="view.do";
	form.submit();
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
			    menuFg : 'prof'
			}),
			success: function(data){			
				resolve(data.bookmarkList);
			},error:function() {
				reject("");
			}
		});
	})
	
}

/* 필터 초기화 */
function reloadFilter(){
    $('input[type="text"]').val('');
        
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
</script>