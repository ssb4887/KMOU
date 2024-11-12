<script type="text/javascript" src="/include/js/calendar.js"></script>
<link rel="stylesheet" type="text/css" href="/include/js/calendar/calendar.css">
<%@ include file="../../include/commonTop.jsp"%>

<script type="text/javascript">
let bookmarkList;

var page_prop = {};

var _SEARCH = {
		MID 			: "35",					// 메뉴번호
		TOPIC			: "",					// 검색 키
		UNIV 			: "",					// 대학
		DEPT 			: "",					// 학부(과)
		MAJOR 			: "",					// 전공
		ORDERBY			: "",					// 정렬기준
		FLAG			: ""					// 로그FLAG
};

var _page = "";

$(function(){
	
    // 전공 검색 입력창에서 엔터를 누르면 검색
    $("input[name=top_search]").on("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            
            _SEARCH.TOPIC	 		= $("#top_search").val();
			_SEARCH.UNIV     		= $("#college1").val();
			_SEARCH.DEPT 			= $("#college2").val();
			_SEARCH.MAJOR 			= $("#college3").val();
			_SEARCH.ORDERBY 		= $("#orderBy").val();
			_SEARCH.FLAG 			= "Y";
			
			/* sessionStorage.setItem('majorSearchKeyword', JSON.stringify(_SEARCH)); */
			
            search(1);
            
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
			
			/* sessionStorage.setItem('majorSearchKeyword', JSON.stringify(_SEARCH)); */
			
			search(1);
			
			_SEARCH.FLAG 			= "";
	});
    
    /* 정렬기능 */
	$('#orderBy').change(function() {
		_SEARCH.ORDERBY 		= $("#orderBy").val();
		
		if($("#top_search").val().trim() == ""){
			getInitMajorList(_page);
		}else{
			search(_page);
		}
		     
    });
    
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
		
		if($(this).find("option:selected").text().slice(-2) == '학부'){
			$("#college3").removeAttr("disabled");			
			getMajorList(depart, varAction);
		}else{
			$("#college3").find("option").remove();
			$("#college3").append('<option value="">전공</option>\n');	
			$("#college3").attr("disabled", true);
		}
		
			
	}); 
	
	getBookmarkList().then(data => {
		bookmarkList = data;
	});
	
    var sessionVal = new Object();
  	//세션에 들어가있는 검색값(메인페이지에서 더보기 용 + 새로고침시 값 유지)
	searchKeyword = sessionStorage.getItem('majorSearchKeyword');
	if (searchKeyword) {
	    if (isJsonString(searchKeyword)) {
	        sessionVal = JSON.parse(searchKeyword);
	        
	        if(typeof(sessionVal.TOPIC) == "undefined" || sessionVal.TOPIC == '') return getInitMajorList(1);
	        
	        $("input[name=top_search]").val(sessionVal.TOPIC);
	        $("#college1").val(sessionVal.UNIV).prop("selected", true);
	        $("#college2").val(sessionVal.DEPT).prop("selected", true);
	        $("#college3").val(sessionVal.MAJOR).prop("selected", true);
	        
	        
	        _SEARCH.TOPIC	 		= sessionVal.TOPIC;
    		_SEARCH.UNIV     		= sessionVal.UNIV;
    		_SEARCH.DEPT 			= sessionVal.DEPT;
    		_SEARCH.MAJOR 			= sessionVal.MAJOR;
    		
	    } else {
	    	if(typeof(sessionVal.TOPIC) == "undefined" || sessionVal.TOPIC == '') return getInitMajorList(1);
	    	
	        // searchKeyword가 JSON 문자열이 아닌 경우 처리
	    	$("input[name=top_search]").val(searchKeyword);
			
			_SEARCH.TOPIC	 		= $("#top_search").val();
			_SEARCH.UNIV     		= $("#college1").val();
			_SEARCH.DEPT 			= $("#college2").val();
			_SEARCH.MAJOR 			= $("#college3").val();
			
	    }
	    
	    search(1);
	}else{
		getInitMajorList(1);
	}
	sessionStorage.removeItem("majorSearchKeyword"); // 삭제
	
	
	
	
	
	
});

function isJsonString(str) {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
}

/* 최초 전공 메뉴 진입 시, 전공 목록 출력 */
function getInitMajorList(page){
	
	_page = page;
	
	var orderBy		= _SEARCH.ORDERBY;
	var univ 		= _SEARCH.UNIV;
	var depart 		= _SEARCH.DEPT;
	var major 		= _SEARCH.MAJOR;
	showLoading();
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url : '/web/major/initMajorList.do?mId=35',
  		data: { 
  			mId : "35",
  			univ : univ,
  			depart : depart,
  			major : major,
  			orderBy : orderBy,
  			page : page
  		},
  		async:true, 
  		success:function(result){
  			const varItemObj = $("#majorList");
  			var htmlPage = $("#page");
  			
  			const topic = $("#top_search").val();
  			const list = result.majorList;
  			const totalCount = result.totalCount;

  			varItemObj.empty();
  			htmlPage.empty();
  			
  			$("#header").remove();
			var vartopic = '<h5 class="search_res mb-3" id="header"><strong class="fw-bolder">전체검색</strong>에 대한 총 <span>' + totalCount + '</span>건의 게시글이 있습니다.</h5>\n';
			$(".major_title_boxselect").before(vartopic);
  			
  			// 대학 코드 객체
  			var objColg = {};
  			"<c:forEach items="${collegeList}" var="item">"
  			objColg["${item.COLG_CD}"] = "${item.COLG_NM}";
  			"</c:forEach>"
  			
  			// 학부(과) 코드 객체
  			var objDept = {};
  			"<c:forEach items="${departList}" var="item">"
  			objDept["${item.DEPT_CD}"] = "${item.DEPT_NM}";
  			"</c:forEach>"
  			
  			$.each(list, function(i, item){
  				
  				var id = item.MAJOR_CD;
  				var onRed = "";
  				if(bookmarkList != undefined){
  		        	bookmarkList.forEach(function(bookmark) {	            		
  		        		if(bookmark.docId == id){
  		            		 onRed = 'on_red';            			
  		        		}
  			 		});
  		    	}
  				
  				var $elem = '<div class="item border" onclick="redirectMajorView(`'+ item.MAJOR_CD +'`,`'+ objColg[item.COLG_CD] +'`,`'+ objDept[item.DEPT_CD] +'`)" style="cursor:pointer;">'
  							+ '<div id="' + id + '" class="like_container ' + onRed + '">'
  			 				+ '<div class="link_cnt text-end">'
  			 				+ '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18"  onclick="event.stopPropagation();likeChange(\''+ id + '\', \'major\')">'
  			 				+ '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>'
  			 				+ '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>'
  			 				+ '</div>'
  			 				+ '</div>'
  							+ '<ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-2">'
  							+ '<li class="name_of_class"><span>'+objColg[item.COLG_CD]+'</span><span>'+objDept[item.DEPT_CD]+'</span></li>'
  							+ '</ul>'
  							+ '<h5 class="title mb-3">'
  							+ '<a href="javascript:void(0);" onclick="redirectMajorView(`'+ item.MAJOR_CD +'`,`'+ objColg[item.COLG_CD] +'`,`'+ objDept[item.DEPT_CD] +'`)" class="d-block  fw-semibold">'
  							+ item.MAJOR_NM + '(<small class="d-inlinle-block fw-normal mt-1">'+item.MAJOR_ENM+'</small>)'
  							+ '</a>'
  							+ '</h5>'
  							+ '<dl class="major_target01">'
  							+ '<dt>전공 목표. </dt> '
  							+ '<dd>'+item.GOAL+'</dd>' // 인재상 : item.TALENT
  							+ '</dl>'
  							+ '<dl class="major_target02">'
  							+ '<dt>진출 분야. </dt> '
  							+ '<dd>'+item.FIELD+'</dd>'
  							+ '</dl>'
  							+ '</div>';
  				
  				varItemObj.append($elem);
  			});
  			
  			/* 전공 목록 페이징  */
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
  					var prevPageNo = firstPageNoOnPageList  - pageSize;
  				} else{
  					var prevPageNo = firstPageNo;
  				}
  				if(currentPageNo != firstPageNo){
  					pageCon += '<li class="page-item page-first"><a href="javascript:getInitMajorList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
  	  				pageCon += '<li class="page-item page-prev"><a href="javascript:getInitMajorList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
  			
  			
	  		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
	  			if(pageNo == currentPageNo){
	  				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getInitMajorList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
	  			} else{
	  				pageCon += '<li class="page-item"><a class="page-link" href="javascript:getInitMajorList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
	  			}
	  		}
  	  		if(totalPageCount > pageSize){
  				if(lastPageNoOnPageList < totalPageCount){
  					var nextPageNo = firstPageNoOnPageList + pageSize;
  				} else{
  					var nextPageNo = lastPageNo;
  				}
  				if(currentPageNo != lastPageNo){
  					pageCon += '<li class="page-item page-next"><a href="javascript:getInitMajorList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
  	  	  			pageCon += '<li class="page-item page-last"><a href="javascript:getInitMajorList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
	  		
  			htmlPage.append(pageCon);
  			
  			$("#checkSearch").val("N");
  			hideLoading();	
  			return false;
  		}, 
  		error:function(request,error){ asdf(error); }
  	});
}

/* 검색 */
function search(page){
	if($("#top_search").val().trim() == ""){
		getInitMajorList(page);
		return false;
	}
	
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
	var orderBy 	= _SEARCH.ORDERBY;
	
	showLoading();
	/* const topic = $("#top_search").val();
	const univ = $("#college1").val();
	const depart = $("#college2").val();
	const major = $("#college3").val(); */
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url : '/web/major/majorList.do?mId=35',
  		data: { 
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
  			const varItemObj = $("#majorList");
  			var htmlPage = $("#page");
  			
  			const topic = $("#top_search").val();
  			const list = result.majorList;
  			const totalCount = result.totalCount;
  			
  			if(topic != ""){
  				$("#header").remove();
  				var vartopic = '<h5 class="search_res mb-3" id="header"><strong class="fw-bolder">' + topic + '</strong>에 대한 총 <span>' + totalCount + '</span>건의 게시글이 있습니다.</h5>\n';
  				$(".major_title_boxselect").before(vartopic);
  			}
  			
  			varItemObj.empty();
  			htmlPage.empty();
  			
  			// 대학 코드 객체
  			var objColg = {};
  			"<c:forEach items="${collegeList}" var="item">"
  			objColg["${item.COLG_CD}"] = "${item.COLG_NM}";
  			"</c:forEach>"
  			
  			// 학부(과) 코드 객체
  			var objDept = {};
  			"<c:forEach items="${departList}" var="item">"
  			objDept["${item.DEPT_CD}"] = "${item.DEPT_NM}";
  			"</c:forEach>"
  			
  			$.each(list, function(i, item){

  	  			var id = item.MAJOR_CD;
  				var onRed = "";
  				if(bookmarkList != undefined){
  		        	bookmarkList.forEach(function(bookmark) {	            		
  		        		if(bookmark.docId == id){
  		            		 onRed = 'on_red';            			
  		        		}
  			 		});
  		    	}
  				
  				var $elem = '<div class="item border" onclick="redirectMajorView(`'+ item.MAJOR_CD +'`,`'+ objColg[item.COLG_CD] +'`,`'+ objDept[item.DEPT_CD] +'`)" style="cursor:pointer;">'
	  						+ '<div id="' + id + '" class="like_container ' + onRed + '">'
			 				+ '<div class="link_cnt text-end">'
			 				+ '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18"  onclick="event.stopPropagation();likeChange(\''+ id + '\', \'major\')">'
			 				+ '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/>'
			 				+ '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>'
			 				+ '</div>'
			 				+ '</div>'
  							+ '<ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-2">'
  							+ '<li class="name_of_class"><span>'+objColg[item.COLG_CD]+'</span><span>'+objDept[item.DEPT_CD]+'</span></li>'
  							+ '</ul>'
  							+ '<h5 class="title mb-3">'
  							+ '<a href="javascript:void(0);" onclick="redirectMajorView(`'+ item.MAJOR_CD +'`,`'+ objColg[item.COLG_CD] +'`,`'+ objDept[item.DEPT_CD] +'`)" class="d-block  fw-semibold">'
  							+ item.MAJOR_NM_KOR + '(<small class="d-inlinle-block fw-normal mt-1">'+item.MAJOR_NM_ENG+'</small>)'
  							+ '</a>'
  							+ '</h5>'
  							+ '<dl class="major_target01">'
  							+ '<dt>전공 목표. </dt> '
  							+ '<dd>'+item.GOAL+'</dd>' // 인재상 : item.TALENT
  							+ '</dl>'
  							+ '<dl class="major_target02">'
  							+ '<dt>진출 분야. </dt> '
  							+ '<dd>'+item.FIELD+'</dd>'
  							+ '</dl>'
  							+ '</div>';
  				
  				varItemObj.append($elem);
  			});
  			
  			
  			/* 전공 목록 페이징  */
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
  					var prevPageNo = firstPageNoOnPageList  - pageSize;
  				} else{
  					var prevPageNo = firstPageNo;
  				}
  				if(currentPageNo != firstPageNo){
  					pageCon += '<li class="page-item page-first"><a href="javascript:search(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
  	  				pageCon += '<li class="page-item page-prev"><a href="javascript:search(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
  			
  			
	  		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
	  			if(pageNo == currentPageNo){
	  				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:search(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
	  			} else{
	  				pageCon += '<li class="page-item"><a class="page-link" href="javascript:search(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
	  			}
	  		}
  	  		if(totalPageCount > pageSize){
  				if(lastPageNoOnPageList < totalPageCount){
  					var nextPageNo = firstPageNoOnPageList + pageSize;
  				} else{
  					var nextPageNo = lastPageNo;
  				}
  				if(currentPageNo != lastPageNo){
  					pageCon += '<li class="page-item page-next"><a href="javascript:search(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
  	  	  			pageCon += '<li class="page-item page-last"><a href="javascript:search(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
  				}
  			}
	  		
  			
  			htmlPage.append(pageCon);
  			hideLoading();
  		}, 
  		error:function(request,error){ asdf(error); }
  	});
}

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
  		error:function(request,error){ adsf(error); }
  	}); 
}

// 상세페이지 이동
function redirectMajorView(majorCd, colgNm, deptNm){
	var form = document.majorView;
	form.MAJOR_CD.value = majorCd;
	form.COLG_NM.value = colgNm;
	form.DEPT_NM.value = deptNm;
	form.action = "view.do"; 
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
			    menuFg : 'major'
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