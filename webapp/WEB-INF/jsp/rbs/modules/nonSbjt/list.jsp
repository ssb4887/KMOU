<script type="text/javascript" src="/include/js/calendar.js"></script>
<link rel="stylesheet" type="text/css" href="/include/js/calendar/calendar.css">
<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
let jsonDataVals = [
    'nonSbjt_search',
    'target',
    'sign_in_end_date',
    'sign_in_end_date1',
    'start_date',
    'start_date1',
    'main_category',
    'sub_category',
    'tag',
    'program_type',
    'method'
	];
	
let studentNo = "${loginVO.memberId}";
	
let searchVals;
let bookmarkList;

var _SEARCH = {
		MID 				: "33",					// 메뉴번호
		TOPIC				: "",					// 검색 키
	    NONSBJT_SEARCH		: "",
	    SIGN_IN_START_DATE	: "",
	    SIGN_IN_END_DATE	: "",
	    START_DATE			: "",
	    END_DATE			: "",
	    MAIN_CATEGORY		: "",
	    SUB_CATEGORY		: "",
	    ORDER_BY			: ""
};




// input태그의 데이터들을 검색 api호출 시 필요한 json형태로 변환
function gatherJsonData(){	
	const searchVals = {};

	jsonDataVals.forEach(function(col) {
	    
	    // 검색어 컬럼 치환
	    var colKey = col;
	    if (col === 'nonSbjt_search') colKey = 'keyword';
	    
	    //checkBox  처리
	    if ($('input[name=' + colKey + '][type=checkbox]:checked').length > 0) {
	        var checkedValues = [];
	        $('input[name=' + colKey + '][type=checkbox]:checked').each(function() {
	            checkedValues.push($(this).val());
	        });
	        searchVals[colKey] = checkedValues.join(",");
	        return;
	    } else {
	        searchVals[colKey] = "";
	    }
	    	    	    
	    //일반 field 처리
	    var temp = $('input[name=' + col + '][type!=checkbox]').val();		    
	    temp = (temp === undefined ? temp : temp.replace(/\\/g, ''));
	    if (temp) {
	        searchVals[colKey] = temp;
	        return;
	    }
	    
	    //selectbox 처리
	    var temp = $('select[name=' + col + ']').val();		    
	    if (temp) {
	        searchVals[colKey] = temp;
	        return;
	    }
	    
	    //textArea 처리
	    temp = $('textarea[name=' + colKey + ']').val();
	    if (temp) {
	        searchVals[colKey] = temp;
	        return;
	    }
	    
	    // sort 객체 추가
	    var orderBy = $("#orderBy option:selected").val();
	    
	    if(orderBy == 'RECOMMEND' || orderBy == 'SIGNIN_END_RANK'){	    	
	        searchVals["sort"] = [{
	            "field": 'SIGNIN_END_RANK',
	            "sortType": "fieldSort",
	            "order": "ASC"
	        },{
	            "field": 'SIGNIN_END_DATE',
	            "sortType": "fieldSort",
	            "order": "ASC"
	        }];	  	        	        
	        searchVals["student_no"] = orderBy == "RECOMMEND" ? studentNo : "";
	        
	    }else if(orderBy == 'DEFAULT'){
	        searchVals["sort"] = [{
	        	"sortType":"scoreSort",
	        	"order":"DESC"
	        }];	 
	    }else{	    		    	
	        searchVals["sort"] = [{
	            "field": orderBy,
	            "sortType": "fieldSort",
	            "order": "DESC"
	        }];	    		        
	        searchVals["student_no"] = '';
	        
	    }	  
	});

	return searchVals;
}

function searchBtn(){
	saveFilterState();	
	
    searchVals = gatherJsonData();
    //달력날짜의 '-' 제거
    ['sign_in_start_date', 'sign_in_start_date1', 'sign_in_end_date', 'sign_in_end_date1', 'start_date', 'start_date1', 'end_date', 'end_date1'].forEach(function(dateField) {
        searchVals[dateField] = searchVals[dateField] ? searchVals[dateField].replaceAll("-","") : "";
    });
    
    // _SEARCH 업데이트
    _SEARCH.TOPIC = $('input[name="nonSbjt_search"]').val() || "";
    _SEARCH.NONSBJT_SEARCH = searchVals.keyword || "";
    _SEARCH.SIGN_IN_START_DATE = searchVals.sign_in_start_date || "";
    _SEARCH.SIGN_IN_END_DATE = searchVals.sign_in_end_date || "";
    _SEARCH.START_DATE = searchVals.start_date || "";
    _SEARCH.END_DATE = searchVals.end_date || "";
    _SEARCH.MAIN_CATEGORY = searchVals.main_category || "";
    _SEARCH.SUB_CATEGORY = searchVals.sub_category || "";
    _SEARCH.ORDER_BY = $("#orderBy option:selected").val() || "";
    
    // _SEARCH 객체를 세션에 저장
//     sessionStorage.setItem('nonSbjtSearchKeyword', JSON.stringify(_SEARCH));
    sessionStorage.removeItem('nonSbjtSearchKeyword');
    
	
    nonSbjtSearch(1,'Y');
}

function nonSbjtSearch(page, loggingYn){
		
		//search API는 0페이지부터 시작 , CMS에서 그리는 페이징은 1페이지부터이므로 
		//컨트롤러에 값을 넘길땐 -1(API용), 컨트롤러에서 페이징을 세팅할땐 +1을 한다
		page -= 1;
			
		//필터를 쿼리 조건 방식으로 했을때의 페이징 설정
// 		searchVals["page_num"] = 0;
// 		searchVals["page_per"] = '10000';

		//필터를 쿼리 조건 방식으로 했을때의 페이징 설정
		searchVals["page_num"] = page;
		searchVals["page_per"] = '8';
		showLoading();
		$.ajax({
		 	url: '/web/nonSbjt/nonSbjtSearch.do?mId=34',
		 	contentType:'application/json',	
		 	type: 'POST',
		 	data: JSON.stringify({
		 	    'data' : searchVals,
		 	    'ORDER_BY' : $("#orderBy option:selected").val(),
		 	    'LOGGING_YN' : loggingYn,
		 	    'IS_EMPTY_SEARCH' : ($('input[name=nonSbjt_search]').val() == '') ? 'Y' : 'N'
				//필터를 쿼리 조건 방식으로 했을때의 페이징 설정
// 		 	    'PAGE_NUM' : page,
		 	}),
		 	success: function(response){	
		 		var result = response.responseData;
		 		renderSearchResults(result, response.totalCount, response.paginationInfo);			 		

		 	}
		});			
}


function renderSearchResults(results, totalCount, paginationInfo) {
	
	var $searchResult = $('#searchResult');
	var $page = $('#page');
	 
    $searchResult.empty(); // div 초기화
    $page.empty(); // div 초기화
	
    
    
    var keyword = $('input[name=nonSbjt_search]').val();

    
    if (keyword) {
    	$("#keyword").text(keyword)
    	$("#totalCount").text(totalCount)
    }else{
    	$("#keyword").text("전체검색")
    	$("#totalCount").text(totalCount)
    }

    var nonMajorWrap = $('<div class="box" style="width: 100%; display: inline-block;"></div>');
	if(results != null){
	    results.forEach(function(listDt) {
	    	var id = listDt.IDX + '_' + listDt.TIDX;
	    	var particiRate = 0;
	    	if(listDt.SIGNIN_LIMIT > 0){
	    		particiRate = (Number(listDt.PARTICIPANT) / Number(listDt.SIGNIN_LIMIT) * 100) >= 100 ? '100' : (Number(listDt.PARTICIPANT) / Number(listDt.SIGNIN_LIMIT) * 100);	    		    		
	    	}
	    	var ct = "";
	    	
	    	if(bookmarkList != undefined){	
	        	bookmarkList.forEach(function(bookmark) {	            		
	        		if(bookmark.docId == id){
	            		 ct = 'ct';            			
	        		}
		 		});
	    	}
	    		    	
	        var itemHtml = 
	            '<div class="nonsbjt_box '+ ((listDt.D_DAY == '종료') ? 'duedate' : '') + '">' +
	            	'<a href="javascript:nonSbjtView(' + listDt.IDX + ',' + listDt.TIDX + ');" title="' + listDt.TITLE + '">' +
	            	'<div class="nonsbjt_list_img"><div class="acon_bx"><span>' + 'ⓐ' + '</span><p>' + listDt.POINT + '점</p></div><img src="https://cts.kmou.ac.kr/attachment/view/' + listDt.COVER + '/cover.jpg?ts=' + listDt.UPDATED_DATE  + '" onerror="this.src=\'${contextPath}${imgPath}/nonSbjt_no_image.png\'"></div>'+
	            	'<div class="nonsbjt_list_textbox">' +
	            		'<strong class="title ellip_2">' + listDt.TITLE + '</strong>' +
	            		'<p class="period">' + listDt.COLG_NM + ' ' + (listDt.DEPT_NM === undefined ? '' : listDt.DEPT_NM) + '</p>' +
	            		'<span class="period peoplespan"><b>모집기간</b><em class="fst-normal">' + listDt.SIGNIN_START_DATE + '(' + listDt.SIGNIN_START_DAY + ') ~ ' + listDt.SIGNIN_END_DATE + '(' + listDt.SIGNIN_END_DAY +')</em></span>' +
	            		'<span class="period peoplespan"><b>강의기간</b><em class="fst-normal">' + listDt.START_DATE.split(" ")[0] + '(' + listDt.START_DAY + ') ~ ' + listDt.END_DATE.split(" ")[0] + '(' + listDt.END_DAY +')</em></span>' +
	            		'<span class="period peoplespan"><b>모집인원</b>' +
	            		'<div class="nonsbjt_list_inboxgraph"><span><strong>'+listDt.SIGNIN_LIMIT+'</strong> / '+listDt.PARTICIPANT+'명 지원' + ((listDt.D_DAY == '종료') ? '' : '중') + '</span>' +
	            		'<p class="peopleC"><span style="width:'+particiRate+'%;"></span></span></p>'+'</div>'+'</div>' +'</a>' +
	            		'<div id="' + id + '" class="like_container ">' + 
	                		'<div class="link_cnt">' +
	                			'<ul class="box2"><li><a href="javascript:" class="'+id+' '+ct+'" onclick="likeChange(\''+id+'\',\'nonSbjt\');" tabindex="0"><b class="nonsbjt_list_like_text">찜하기</b></a></li></ul>' +
	                    	'</div>' +
	                	'</div>' + 
	            	'</div>' 
	            '</div>';
	         nonMajorWrap.append(itemHtml);
	    });

	    $searchResult.append(nonMajorWrap);
	    
	    // 페이징
		var pageInfo = paginationInfo;
		
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
				pageCon += '<li class="page-item page-first"><a href="javascript:" onclick="javascript:nonSbjtSearch(' + firstPageNo + ',\'N\');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
				pageCon += '<li class="page-item page-prev"><a href="javascript:" onclick="javascript:nonSbjtSearch(' + prevPageNo + ',\'N\');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';
			}
		}
		
		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
			if(pageNo == currentPageNo){
				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:" onclick="javascript:nonSbjtSearch(' + pageNo + ',\'N\');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
			} else{
				pageCon += '<li class="page-item"><a class="page-link" href="javascript:" onclick="javascript:nonSbjtSearch(' + pageNo + ',\'N\');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
			}
		}
		
  		if(totalPageCount > pageSize){
			if(lastPageNoOnPageList < totalPageCount){
				var nextPageNo = firstPageNoOnPageList + pageSize;
			} else{
				var nextPageNo = lastPageNo;
			}
			if(currentPageNo != lastPageNo){
				pageCon += '<li class="page-item page-next"><a href="javascript:" onclick="javascript:nonSbjtSearch(' + nextPageNo + ',\'N\');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
	  			pageCon += '<li class="page-item page-last"><a href="javascript:" onclick="javascript:nonSbjtSearch(' + lastPageNo + ',\'N\');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';	
			}
		}
  		
	    $page.append(pageCon);
	}
	hideLoading();
		
		

}

function getBookmarkList(){
	return new Promise((resolve, reject) => {		
		$.ajax({
			url: '/web/bookmark/getBookmarkList.do?mId=37',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    menuFg : 'nonSbjt'
			}),
			success: function(data){			
				resolve(data.bookmarkList);
			},error:function() {
				reject("");
			}
		});
	})
	
}

//찜 변경
function likeChange(docId, type){
	
	//찜 등록/삭제
// 	$("#"+docId).children().children().on("click",function(){
		if(!$("."+docId).hasClass("ct")){			
			$.ajax({
				url: '/web/bookmark/insertBookmark.do?mId=37&menuFg=nonSbjt',
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    docId : docId
				}),
				success: function(data){
					$("."+docId).addClass("ct");
				}
			});
		} else {
			$.ajax({
				url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg=nonSbjt',
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    docId : docId
				}),
				success: function(data){
					$("."+docId).removeClass("ct");
				}
			});
		}
// 	})
}

function nonSbjtView(p_idx, p_tidx){
	var form = document.nonSbjtView;
	form.idx.value = p_idx;
	form.tidx.value = p_tidx;
	form.action = "view.do";
	form.submit();
	
}

//대분류 가져오기(parent = 0)
function getMainCategory(callback){
    $.ajax({
        url: '/web/nonSbjt/getCategory.do?mId=34',
        contentType:'application/json',    
        type: 'GET',
        data: {
            "parent" : "0"
        },
        success: function(data){
            //대, 소분류 옵션 제거(첫번째 값은 [전체])
            $('#mainCategory option').not(':first').remove();
            $('#subCategory option').not(':first').remove();
            
            //데이터 세팅
            var $mainCategory = $("#mainCategory");
            
            var html = '';
            var categoryList = data.categoryList;
            categoryList.forEach(function(category) {
                html += '<option value="' + category.idx + '">' + category.title + '</option>';
            });
            $mainCategory.append(html);
            
            if (typeof callback === 'function') {
                callback();
            }
        }
    }); 
}

//소분류 가져오기(parent = idx)
function getSubCategory(tdx){
    $.ajax({
        url: '/web/nonSbjt/getCategory.do?mId=34',
        contentType:'application/json',    
        type: 'GET',
        data: {
            "parent" : tdx
        },
        success: function(data){
            //소분류 옵션 제거(첫번째 값은 [전체])
             $('#subCategory option').not(':first').remove();
            
            //데이터 세팅
            var $subCategory = $("#subCategory");
            
            var html = '';
            var categoryList = data.categoryList;
            categoryList.forEach(function(category) {
                html += '<option value="' + category.idx + '">' + category.title + '</option>';
            });
            $subCategory.append(html);
            
            if (typeof callback === 'function') {
                callback();
            }
        }
    }); 
}

//교육형식 가져오기
function getProgramType(){
    $.ajax({
        url: '/web/nonSbjt/getProgramType.do?mId=34',
        contentType:'application/json',    
        type: 'GET',
        success: function(data){
        
            var $program_type = $("#program_type");
            var html = '';
            var programTypeList = data.PROGRAM_TYPE_LIST;
            programTypeList.forEach(function(programType) {
                html += '<option value="' + programType.type + '">' + programType.title + '</option>';
            });
            $program_type.append(html);
            
            if (typeof callback === 'function') {
                callback();
            }
        }
    });             
}

//운영방식 가져오기
function getMethod(){
    $.ajax({
        url: '/web/nonSbjt/getMethod.do?mId=34',
        contentType:'application/json',    
        type: 'GET',
        success: function(data){
        
            var $method = $("#method");
            var html = '';
            var methodList = data.METHOD_LIST;
            methodList.forEach(function(method) {
                html += '<option value="' + method.idx + '">' + method.title + '</option>';
            });
            $method.append(html);
            
            if (typeof callback === 'function') {
                callback();
            }
        }
    });             
}

function getTag(){
	
	$.ajax({
	 	url: '/web/nonSbjt/getTag.do?mId=34',
	 	contentType:'application/json',	
	 	type: 'GET',
	 	success: function(data){	 	
	 		//데이터 세팅
	 		var $tag = $("#tag");
	 		
	 		var html = '';
	 		var tagList = data.tagList;
	 		tagList.forEach(function(tag) {
	 			html += '<input class="form-check-input" type="checkbox" value="'+tag.tag+'" id="'+tag.tag+'" name="tag">'
	 			html += '<label class="form-check-label" for="'+tag.tag+'">'+tag.tag+'</label>';
	 		});
	 		$tag.append(html);
	 		
	 	}
	});	
}


function reloadFilter(){
		jsonDataVals.forEach(function(col) {
		     // 체크박스 처리
		     $('input[name=' + col + '][type=checkbox]').prop('checked', false);
		     
		     // 일반 input 처리
		     $('input[name=' + col + '][type!=checkbox]').val('');
		
		     // selectbox 처리
            $('select[name=' + col + ']').each(function() {
            	getMainCategory();
        	});
		
		     // textArea 처리
		     $('textarea[name=' + col + ']').val('');
		});
		
	    sessionStorage.removeItem('nonSbjtFilterState');
	    sessionStorage.removeItem('nonSbjtSearchKeyword');
}

function handleDateGroup(group, startDateField, endDateField, isApplyGroup) {
    group.on('change', function() {
        if ($(this).is(':checked')) {
            // 현재 체크된 항목 제외하고 나머지 체크 해제
            group.not(this).prop('checked', false);
            
            var today = new Date();
            var endDate = new Date();

            // 직접입력 체크박스가 선택된 경우
            if (this.id === 'sign_end_4' || this.id === 'edu_end_4') {
                $(this).closest('li').find('.input-group').show();
                // 직접 입력의 경우 해당 날짜 필드를 비움
                $('input[name=' + startDateField + ']').val('');
                $('input[name=' + endDateField + ']').val('');
            } else {
                group.filter('#sign_end_4, #edu_end_4').closest('li').find('.input-group').hide();
                // 선택된 값에 따라 날짜 설정
                switch(this.id) {
                    case 'sign_end_1':
                    case 'edu_end_1':
                        // 금일 이내
                        endDate = today;
                        break;
                    case 'sign_end_2':
                    case 'edu_end_2':
                        // 1주 이내
                        endDate.setDate(today.getDate() + 7);
                        break;
                    case 'sign_end_3':
                    case 'edu_end_3':
                        // 1개월 이내
                        endDate.setMonth(today.getMonth() + 1);
                        break;
                }
                $('input[name=' + startDateField + ']').val(formatDate(today));
                $('input[name=' + endDateField + ']').val(formatDate(endDate));
            }
        } else {
            // 체크 해제 시 날짜 선택 부분 숨기기 및 해당 날짜 필드 초기화
            $(this).closest('li').find('.input-group').hide();
            $('input[name=' + startDateField + ']').val('');
            $('input[name=' + endDateField + ']').val('');
        }
    });
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

// 날짜 포맷 함수
function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) 
        month = '0' + month;
    if (day.length < 2) 
        day = '0' + day;

    return [year, month, day].join('-');
}

function saveFilterState() {
    var filterState = {
        searchText: $('input[name="nonSbjt_search"]').val(),
        mainCategory: $('#mainCategory').val(),
        subCategory: $('#subCategory').val(),
        signEndChecks: {
            sign_end_1: $('#sign_end_1').prop('checked'),
            sign_end_2: $('#sign_end_2').prop('checked'),
            sign_end_3: $('#sign_end_3').prop('checked'),
            sign_end_4: $('#sign_end_4').prop('checked')
        },
        signInDates: {
            start: $('#is_applDate1').val(),
            end: $('#is_applDate2').val()
        },
        eduEndChecks: {
            edu_end_1: $('#edu_end_1').prop('checked'),
            edu_end_2: $('#edu_end_2').prop('checked'),
            edu_end_3: $('#edu_end_3').prop('checked'),
            edu_end_4: $('#edu_end_4').prop('checked')
        },
        eduDates: {
            start: $('#is_operDate1').val(),
            end: $('#is_operDate2').val()
        }
    };
//     sessionStorage.setItem('nonSbjtFilterState', JSON.stringify(filterState));
}

function restoreFilterState() {	
    var filterState = sessionStorage.getItem('nonSbjtFilterState');
    if (filterState) {
        filterState = JSON.parse(filterState);
        $('input[name="nonSbjt_search"]').val(filterState.searchText);
        
        // 메인 카테고리 복원
        getMainCategory(function() {
            $('#mainCategory').val(filterState.mainCategory);
            
            // 서브 카테고리 복원
            if (filterState.mainCategory) {
                getSubCategory(filterState.mainCategory, function() {
                    $('#subCategory').val(filterState.subCategory);
                });
            }
        });
        
        $('input[name="nonSbjt_search"]').val(filterState.searchText);
        
        // 체크박스 상태 복원
        for (var key in filterState.signEndChecks) {
            $('#' + key).prop('checked', filterState.signEndChecks[key]);
        }
        for (var key in filterState.eduEndChecks) {
            $('#' + key).prop('checked', filterState.eduEndChecks[key]);
        }
        
        // 날짜 필드 복원
        $('#is_applDate1').val(filterState.signInDates.start);
        $('#is_applDate2').val(filterState.signInDates.end);
        $('#is_operDate1').val(filterState.eduDates.start);
        $('#is_operDate2').val(filterState.eduDates.end);
        
        // 날짜 입력 필드 활성화/비활성화
        $('#is_applDate1, #is_applDate2').prop('disabled', !filterState.signEndChecks.sign_end_4);
        $('#is_operDate1, #is_operDate2').prop('disabled', !filterState.eduEndChecks.edu_end_4);
    }
}


$(function(){	
	
	//셀렉트박스 필터 가져오기
	getMainCategory();
	getProgramType();
	getMethod();
// 	getTag();
    
    /*필터링이 변경될때마다 검색 재호출*/
    $('#orderBy').change(function() {
    	saveFilterState();	
    	
        searchVals = gatherJsonData();
        //달력날짜의 '-' 제거
        ['sign_in_start_date', 'sign_in_start_date1', 'sign_in_end_date', 'sign_in_end_date1', 'start_date', 'start_date1', 'end_date', 'end_date1'].forEach(function(dateField) {
            searchVals[dateField] = searchVals[dateField] ? searchVals[dateField].replaceAll("-","") : "";
        });
        
        // _SEARCH 업데이트
        _SEARCH.TOPIC = $('input[name="nonSbjt_search"]').val() || "";
        _SEARCH.NONSBJT_SEARCH = searchVals.keyword || "";
        _SEARCH.SIGN_IN_START_DATE = searchVals.sign_in_start_date || "";
        _SEARCH.SIGN_IN_END_DATE = searchVals.sign_in_end_date || "";
        _SEARCH.START_DATE = searchVals.start_date || "";
        _SEARCH.END_DATE = searchVals.end_date || "";
        _SEARCH.MAIN_CATEGORY = searchVals.main_category || "";
        _SEARCH.SUB_CATEGORY = searchVals.sub_category || "";
        _SEARCH.ORDER_BY = $("#orderBy option:selected").val() || "";
        
        // _SEARCH 객체를 세션에 저장
//         sessionStorage.setItem('nonSbjtSearchKeyword', JSON.stringify(_SEARCH));
    	
    	nonSbjtSearch(1,'N');
    });
    
	
    // 신청기간 체크박스 그룹
    const applyGroup = $('#sign_end_1, #sign_end_2, #sign_end_3, #sign_end_4');
    // 교육기간 체크박스 그룹
    const eduGroup = $('#edu_end_1, #edu_end_2, #edu_end_3, #edu_end_4');

    // 각 그룹에 대해 함수 적용
    handleDateGroup(applyGroup, 'sign_in_end_date', 'sign_in_end_date1', true);
    handleDateGroup(eduGroup, 'start_date', 'start_date1', false);
    
    // 날짜 선택 부분 숨기기
    $('.in_schbox_type_data .input-group').hide();
    
    
    // 직접입력 날짜 변경 시 hidden input 업데이트
    $('#is_applDate1, #is_applDate2').change(function() {
        $('input[name=sign_in_end_date]').val($('#is_applDate1').val());
        $('input[name=sign_in_end_date1]').val($('#is_applDate2').val());
    });
    
    $('#is_operDate1, #is_operDate2').change(function() {
        $('input[name=start_date]').val($('#is_operDate1').val());
        $('input[name=start_date1]').val($('#is_operDate2').val());
    });
    
    // 비교과 검색 입력창에서 엔터를 누르면 검색
    $("input[name=nonSbjt_search]").on("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault(); 
            searchBtn();
        }
    });
	
	getBookmarkList().then(data => {
			bookmarkList = data;
	});
	

	setTimeout(function() {
//         restoreFilterState();
        var sessionVal = sessionStorage.getItem('nonSbjtSearchKeyword');
        if(sessionVal){
//         	if(typeof(sessionVal.TOPIC) == "undefined" || sessionVal.TOPIC == '') return initNonSbjtSearch(1);
        	
            var savedSearch = JSON.parse(sessionVal);
            Object.assign(_SEARCH, savedSearch);
            $('input[name="nonSbjt_search"]').val(_SEARCH.TOPIC || "");
            $("#orderBy").val(_SEARCH.ORDER_BY).prop("selected", true);
            searchBtn();
        } else {
        	searchBtn();
        }
    }, 100);
	
    // 메인 카테고리 변경 이벤트
    $("#mainCategory").change(function(){
        var idx = $(this).val();
        if(idx === ''){
            $('#subCategory option').not(':first').remove();
        } else {
            getSubCategory(idx);
        }
    });
	
	try{$("#fn_btn_is_applDate1").click(function(event){displayCalendar2($('#is_applDate1'), $('#is_applDate2'), '', this);return false;});}catch(e){}
	try{$("#fn_btn_is_applDate2").click(function(event){displayCalendar2($('#is_applDate2'), $('#is_applDate1'), '', this);return false;});}catch(e){}
	
	try{$("#fn_btn_is_operDate1").click(function(event){displayCalendar2($('#is_operDate1'), $('#is_operDate2'), '', this);return false;});}catch(e){}
	try{$("#fn_btn_is_operDate2").click(function(event){displayCalendar2($('#is_operDate2'), $('#is_operDate1'), '', this);return false;});}catch(e){}
	
});
</script>