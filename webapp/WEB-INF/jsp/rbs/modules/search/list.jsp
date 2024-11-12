<%@ include file="../../include/commonTop.jsp"%>
<script>

var _SEARCH = {
		MID 			: "",					// 메뉴번호
		TOPIC			: "",					// 검색 키
};

/* 교과목 검색 리스트 */
function sbjtList(keyword){
	_SEARCH.MID	 		= "32";
	_SEARCH.TOPIC	 	= keyword;
	
	sessionStorage.setItem('sbjtSearchKeyword', JSON.stringify(_SEARCH));	
	location.href = '/web/sbjt/list.do?mId=32'
}

/* 상세보기(교과목코드, 부서코드, 년도, 분반) */
function sbjtView(a,b,c,d){
	var form = document.sbjtViewForm;
	form.SUBJECT_CD.value = a;
	form.DEPT_CD.value = b;
	form.YEAR.value = c;
	form.SMT.value = d;
	form.submit();
}

/* 교수 검색 리스트 */
function profList(keyword){
	_SEARCH.MID	 		= "33";
	_SEARCH.TOPIC	 	= keyword;
	
	sessionStorage.setItem('profSearchKeyword', JSON.stringify(_SEARCH));	
	location.href = '/web/prof/list.do?mId=33'
}

/* 상세보기(임직원코드) */
function profView(a){
	
	var form = document.profViewForm;
	form.empNo.value = a;
	form.action ="/web/prof/view.do";
	form.submit(); 
}

/* 비교과 검색 리스트 */
function nonSbjtList(keyword){
	_SEARCH.MID	 		= "34";
	_SEARCH.TOPIC	 	= keyword;
	sessionStorage.setItem('nonSbjtSearchKeyword', JSON.stringify(_SEARCH));	
	location.href = '/web/nonSbjt/list.do?mId=34'
}

/* 비교과 상세보기 */
function nonSbjtView(p_idx, p_tidx){
	var form = document.nonSbjtViewForm;
	form.idx.value = p_idx;
	form.tidx.value = p_tidx;
	form.submit();
	
}

/* 전공 검색 리스트 */
function majorList(keyword){
	_SEARCH.MID	 		= "35";
	_SEARCH.TOPIC	 	= keyword;
	
	sessionStorage.setItem('majorSearchKeyword', JSON.stringify(_SEARCH));	
	location.href = '/web/major/list.do?mId=35'
}

/* 전공 상세보기 */
function majorView(majorCD){
	var form = document.majorViewForm;
	form.MAJOR_CD.value = majorCD;
	form.action ="/web/major/view.do";
	form.submit();
}

$(function(){
	renderNonSbjtResults();
	// 교과목 찜
	getBookmarkList('sbjt').then(data => {		
		data.forEach(function(bookmark) {
			$("#"+bookmark.docId).addClass("on_red");
 		});
	});
	// 전공 찜
	getBookmarkList('major').then(data => {	
		data.forEach(function(bookmark) {
			$("#"+bookmark.docId).addClass("on_red");
 		});
	});
	// 교수 찜
	getBookmarkList('prof').then(data => {	
		data.forEach(function(bookmark) {
			$("#"+bookmark.docId).addClass("on_red");
 		});
	});
})


/*비교과 렌더링*/
function renderNonSbjtResults() {
    var $nonSbjtResult = $('.nonSbjtSearchResult');
    var bookmarkList;
	getBookmarkList('nonSbjt').then(data => {
		bookmarkList = data;
	});
    
    $nonSbjtResult.empty();
    
	const _DATASET = [];
	
	"<c:forEach items="${nonSbjtList}" var="item">"
	
	var data = {};
	data.IDX = "${item.IDX}";
	data.TIDX = "${item.TIDX}";
	data.D_DAY = "${item.D_DAY}";
	data.TITLE = "${item.TITLE}";
	data.SIGNIN_START_DATE = "${item.SIGNIN_START_DATE}";	
	data.SIGNIN_START_DAY = "${item.SIGNIN_START_DAY}";
	data.SIGNIN_END_DATE = "${item.SIGNIN_END_DATE}";
	data.SIGNIN_END_DAY = "${item.SIGNIN_END_DAY}";
	data.START_DATE = "${item.START_DATE}";
	data.START_DAY = "${item.START_DAY}";
	data.END_DATE = "${item.END_DATE}";
	data.END_DAY = "${item.END_DAY}";
	data.SIGNIN_LIMIT = "${item.SIGNIN_LIMIT}";
	data.PARTICIPANT = "${item.PARTICIPANT}";
	data.D_DAY = "${item.D_DAY}";
	data.COVER = "${item.COVER}";
	data.UPDATED_DATE = "${item.UPDATED_DATE}";
	data.POINT = "${item.POINT}";

	_DATASET.push(data);
	"</c:forEach>"
	
    $('#nonSbjtCount').text("${nonSbjtCount}" + '건');
    var nonMajorWrap = $('<div class="box"></div>');
    _DATASET.forEach(function(listDt) {
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
            	'<a href="javascript:nonSbjtView(' + listDt.IDX + ',' + listDt.TIDX + ');" title="제목">' +
	            		'<div class="nonsbjt_list_img">'+
	            			'<div class="acon_bx">' +
	            			'<span>ⓐ</span>'+
	            			'<p>'+listDt.POINT+'점</p>'+
	            		'</div>'+
	            		'<img src="https://cts.kmou.ac.kr/attachment/view/' + listDt.COVER + '/cover.jpg?ts=' + listDt.UPDATED_DATE  + '" onerror="this.src=\'${contextPath}${imgPath}/nonSbjt_no_image.png\'">' +
	            	'</div>'+
	            	'<div class="nonsbjt_list_textbox">' +
	            		'<strong class="title ellip_2">' + listDt.TITLE + '</strong>' +
	            		'<p class="period">' + listDt.COLG_NM + ' ' + (listDt.DEPT_NM === undefined ? '' : listDt.DEPT_NM) + '</p>' +
	            		'<span class="period peoplespan"><b>모집기간</b><em class="fst-normal">' + listDt.SIGNIN_START_DATE + '(' + listDt.SIGNIN_START_DAY + ') ~ ' + listDt.SIGNIN_END_DATE + '(' + listDt.SIGNIN_END_DAY +')</em></span>' +
	            		'<span class="period peoplespan"><b>강의기간</b><em class="fst-normal">' + listDt.START_DATE.split(" ")[0] + '(' + listDt.START_DAY + ') ~ ' + listDt.END_DATE.split(" ")[0] + '(' + listDt.END_DAY +')</em></span>' +
	            		'<span class="period peoplespan"><b>모집인원</b>' +
	            			'<div class="nonsbjt_list_inboxgraph">'+
	            				'<span><strong>'+listDt.SIGNIN_LIMIT+'</strong> / '+listDt.PARTICIPANT+'명 지원' + ((listDt.D_DAY == '종료') ? '' : '중') + '</span>' +
	            				'<p class="peopleC"><span style="width:'+particiRate+'%;"></span></p>'+
	            			'</div>'+
	            		'</span>'+
	            	'</div>'+
	            '</a>' +
	         	'<div id="' + id + '" class="like_container ">' + 
	            	'<div class="link_cnt">' +
	                		'<ul class="box2">'+
	                			'<li>'+
	                				'<a href="javascript:" class="'+id+' '+ct+'" onclick="likeChange(\''+id+'\',\'nonSbjt\');" tabindex="0"><b class="nonsbjt_list_like_text">찜하기</b></a>' +
	                			'</li>'+
	                		'</ul>'+
	                    '</div>' +
	                '</div>' + 
	            '</div>' ;     
        nonMajorWrap.append(itemHtml);
            
    });
    $nonSbjtResult.append(nonMajorWrap);
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


function getBookmarkList(menuFg){
	return new Promise((resolve, reject) => {		
		$.ajax({
			url: '/web/bookmark/getBookmarkList.do?mId=37',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    menuFg : menuFg
			}),
			success: function(data){			
				resolve(data.bookmarkList);
			},error:function() {
				reject("");
			}
		});
	})
	
}


</script>