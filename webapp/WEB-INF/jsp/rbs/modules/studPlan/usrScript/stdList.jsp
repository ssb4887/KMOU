<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
let bookmarkList;

	$(function(){
		getBookmarkList().then(data => {
			bookmarkList = data;
		});
	});

	//변경등록하기
	function newRevision(){
			
			if(confirm("변경하시겠습니까? 변경된 학생설계전공은 새로 저장됩니다.")){
				var form = document.frmChgSubmit;
				form.action = '/web/studPlan/stuChgSubmit.do?mId=45'
				form.submit();			
			}
	}
	
	function linkPage(pageIndex){ 
		var form = document.frmSearch;
		form.pageIndex.value = pageIndex;
		form.submit();
	} 
	
	function aplyYyChk(e){
		$("#aplyYy").val(e);
		$("#pageIndex").val("1");
		
		$("#frmSearch").submit();
	}
	
	function chrgColgCdChk(e){
		$("#chrgColgCd").val(e);
		$("#pageIndex").val("1");
		
		$("#frmSearch").submit();
	}
	
	
	function goModify(a,b){
		var form = document.frmView;
		$("#SDM_CD").val(a);
		$("#REVSN_NO").val(b);
		form.action = '/web/studPlan/stdModify.do';
		form.submit();
	}
	
	function goMyView(a,b){
		var form = document.frmView;
		$("#SDM_CD").val(a);
		$("#REVSN_NO").val(b);
		form.action = '/web/studPlan/stdMyView.do';
		form.submit();
	}
	
	function goChgModify(a,b){
		var form = document.frmView;
		$("#SDM_CD").val(a);
		$("#REVSN_NO").val(b);
		form.action = '/web/studPlan/stdChgModify.do';
		form.submit();
	}
	
	function goMyChgView(a,b){
		var form = document.frmView;
		$("#SDM_CD").val(a);
		$("#REVSN_NO").val(b);
		form.action = '/web/studPlan/stdMyChgView.do';
		form.submit();
	}
	
	function goView(a,b){
		var form = document.frmView;
		$("#SDM_CD").val(a);
		$("#REVSN_NO").val(b);
		form.action = '/web/studPlan/studView.do';
		form.submit();
	}
	
	$(function(){
		
		/*연도 세팅(현재연도 ~ -3)*/
        const currentYear = new Date().getFullYear();
        const startYear = currentYear - 3;
        for (let year = currentYear; year >= startYear; year--) {
            $('#year').append($('<option>', { value: year, text: year + '년' }));
        }
        
        //승인된 전체 학생설계전공 전공 목록
        getStudPlanDeptList();
        //승인된 전체 학생설계전공 목록
        getStudPlanList(1);
        
        /*필터링이 변경될때마다 승인된 전체 학생설계전공 목록 API 재호출*/
        $('#majorCd, #year').change(function() {
            var selectedMajor = $('#majorCd').val();
            var selectedYear = $('#year').val();

            getStudPlanList(1);
        });
        
	});
	
	/*승인된 전체 학생설계전공 전공목록 가져오기*/
	function getStudPlanDeptList(){
		$.ajax({
		 	url: '/web/studPlan/getStudPlanMajorList.do?mId=36',
		 	beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
		 	contentType:'application/json',	
		 	type: 'GET',
		 	success: function(data){
		 		//데이터 세팅
		 		var $mainCategory = $("#mainCategory");
		 		
		 		var studPlanDeptList = data.STUDPLAN_MAJOR_LIST;
		 		studPlanDeptList.forEach(function(major) {
		 			 $('#majorCd').append($('<option>', { value: major.MAJOR_CD, text: major.MAJOR_NM }));
		 		});
		 		
		 	}
		});			
	}
	
	/*승인된 전체 학생설계전공 가져오기*/
	function getStudPlanList(pageNo){
		$.ajax({
		 	url: '/web/studPlan/getStudPlanList.do?mId=36',
		 	beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
		 	contentType:'application/json',	
		 	type: 'POST',
		 	data: JSON.stringify({
		 		majorCd : $("#majorCd option:selected").val(),
		 		year : $("#year option:selected").val(),
		 		page : pageNo
		 	}),
		 	success: function(data){
		 		renderStudPlanList(data)		 				       		 		
		 	}
		});	
	}
	
	/*승인된 전체 학생설계전공 rendering*/
	function renderStudPlanList(data){
		 	var studPlanList = $('#studPlanList');
	        var page = $('#page');
	        studPlanList.empty(); 
	        page.empty(); 
	        $("#studPlanCnt").text(data.STUDPLAN_LIST.length);
	        $.each(data.STUDPLAN_LIST, function(index, list) {
	        	var id = list.SDM_DEPT_CD
	        	var on_red = '';
	        	
		    	if(bookmarkList != undefined){	
		        	bookmarkList.forEach(function(bookmark) {	            		
		        		if(bookmark.docId == id){
		        			on_red = 'on_red';            			
		        		}
			 		});
		    	}
	        	
	            var itemHtml = '';
	            itemHtml += '<div class="item" style="cursor:pointer;" onclick="goView(\'' + list.SDM_CD + '\',\'' + list.REVSN_NO + '\')">';
	            
	            itemHtml += '<ul class="d-flex flex-row gap-2 cate"></ul>';
	            
	            if(list.SDM_DEPT_CD != 'SAMPLE001'){ //샘플은 찜 못누르게 	            	
		            itemHtml += '<div id="'+list.SDM_DEPT_CD+'" class="like_container '+on_red+'">';
		            itemHtml += '<div class="link_cnt text-end">';
		            itemHtml += '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="event.stopPropagation(); likeChange(\''+list.SDM_DEPT_CD+'\', \'studPlan\')">';
		            itemHtml += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"></path>';
		            itemHtml += '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"></path></svg>';
		            itemHtml += '</div>';
		            itemHtml += '</div>';
	            }
	            
	            itemHtml += '<h3 class="title fs-5 fw-bolder text-start mb-3">' + list.SDM_KOR_NM + '</h3>';
	

	            
	            itemHtml += '<div class="spl_title_list">';
	            $.each(data.STUDPLAN_SBJT_LIST, function(i, sbjtList) {
	                if (sbjtList.SDM_CD === list.SDM_CD && sbjtList.REVSN_NO === list.REVSN_NO) {
	                    itemHtml += '<h4 class="title fs-6 text-start">' + sbjtList.MAJOR_NM + '</h4>';
	                }
	            });
	            itemHtml += '</div>';
	            
	            itemHtml += '<div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2">';
	            itemHtml += '<dl class="d-flex flex-wrap">';
	            itemHtml += '<dt>수여학위</dt><dd>' + (list.AWD_DEGR_KOR_NM || '') + '</dd>';
	            itemHtml += '</dl>';
	            itemHtml += '<dl class="d-flex flex-wrap">';
	            itemHtml += '<dt>설계학점</dt><dd>' + (list.TOTAL_CDT_NUM ? list.TOTAL_CDT_NUM + '학점' : '') + '</dd>';
	            itemHtml += '</dl>';
	            itemHtml += '<dl class="d-flex flex-wrap">';
	            itemHtml += '<dt>교과목설계범위</dt><dd>' + (list.SBJT_DGN_RNG_FG_NM || '') + '</dd>';
	            itemHtml += '</dl>';
	            itemHtml += '</div>';
	            
	            itemHtml += '</div>';

	            studPlanList.append(itemHtml);
	        });
	        
		    // 페이징
			var pageInfo = data.PAGINATION_INFO;
			
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
					var prevPageNo = firstPageNoOnPageList;
				} else{
					var prevPageNo = firstPageNo;
				}
				pageCon += '<li class="page-item page-first"><a href="javascript:getStudPlanList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="../${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
				pageCon += '<li class="page-item page-prev"><a href="javascript:getStudPlanList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="../${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';
	  	  		
			}
			
			for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
				if(pageNo == currentPageNo){
					pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getStudPlanList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
				} else{
					pageCon += '<li class="page-item"><a class="page-link" href="javascript:getStudPlanList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
				}
			}
			
	  		if(totalPageCount > pageSize){
				if(lastPageNoOnPageList < totalPageCount){
					var nextPageNo = firstPageNoOnPageList + pageSize;
				} else{
					var nextPageNo = lastPageNo;
				}
				pageCon += '<li class="page-item page-next"><a href="javascript:getStudPlanList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="../${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
	  			pageCon += '<li class="page-item page-last"><a href="javascript:getStudPlanList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="../${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';
	  		
			}
	  		
	  		page.append(pageCon);
	}
	
	/*학생설계전공 변경등록*/	
	function changeInput(sdmCd, revsnNo){
		if(confirm("해당 학생설계전공을 변경등록 하시겠습니까?")){
	        
			$.ajax({
				url: '/web/studPlan/stdChangeInput.do?mId=36',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'POST', 
				data: JSON.stringify({
					SDM_CD : sdmCd,
					REVSN_NO : revsnNo
				}),
				success:function(data){							
					if(data.RESULT == "DONE"){
						/*수정화면으로 이동*/
						alert("정상적으로 변경가능 처리되었습니다.")
						var form = document.frmView;
						$("#SDM_CD").val(data.SDM_CD);
						$("#REVSN_NO").val(data.REVSN_NO);
						form.action = '/web/studPlan/stdChgModify.do';
						form.submit();
					}else{
						alert("변경가능 처리에 실패하였습니다. 관리자에게 문의해주세요.")
					}
				}
			});	
		}
	}	
	
	
	//찜 가져오기
	function getBookmarkList(){
		return new Promise((resolve, reject) => {		
			$.ajax({
				url: '/web/bookmark/getBookmarkList.do?mId=37',
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    menuFg : 'studPlan'
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
			if(!$("#"+docId).hasClass("on_red")){			
				$.ajax({
					url: '/web/bookmark/insertBookmark.do?mId=37&menuFg='+type,
					contentType:'application/json',	
					type: 'POST',
					data: JSON.stringify({ 
					    docId : docId
					}),
					success: function(data){
						$("#"+docId).addClass("on_red");
					}
				});
			} else {
				$.ajax({
					url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg='+type,
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
</script>