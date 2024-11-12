<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">	
	
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
	

	//전체 탭 카운트
	function getAllTabCnt(){
		$.ajax({
			type:'POST', 
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			url: '/web/studPlan/getStudPlanAllTabCnt.do?mId=36',
			success:function(data){			
				console.log(JSON.stringify(data.ALL_TAB_CNT));
				renderTabCnt(data.ALL_TAB_CNT);
			}
		});
	}

	//탭 카운트 rendering
	function renderTabCnt(data){
		data.forEach(function(item){
			$("#"+item.TYPE+"Cnt").text(item.COUNT)
		});
	}


	//선택한 탭 리스트	
	function getTab(e){
		$.ajax({
			type:'POST', 
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			url: '/web/studPlan/getStudPlanTabList.do?mId=36',
			data: JSON.stringify({TAB : e}),
			success:function(data){			
				$(".cnsltTab").removeClass("active");
				$("#"+e+"Tab").addClass("active");
				renderTabList(data.TAB_LIST, e)			
			}
		});
	}


	//탭 리스트 rendering
	function renderTabList(data, e){
		$("#cnsltTabList").empty();
		var html = '';
		var aTagHtml = '';
		if(data.length > 0){
			
//	         aTagHtml += '<a href="/web/studPlan/profView.do?mId=45&docId=' + ra.SDM_CD + '&no=' + ra.REVSN_NO + '&comYn=' + ra.CNSLT_CMPTL_YN + '&judgYn=' + ra.JUDG_CMPTL_YN + '" title="심사" class="writ_consult w-100">심사</a>';
	        
			data.forEach(function(ra) {
				itemClass = ''
				aTagHtml = '<div class="btn_wrap d-flex flex-row gap-2">';			
				switch(e){
				case 'RA':
					if(ra.STATUS == '30'){
							itemClass = 'on_approve';
	      					aTagHtml += '<a href="goModify(\'Approve\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="요청승인" class="writ_consult w-100">요청승인</a>';							
					}else if(Number(ra.STATUS) > 33){
							itemClass = 'approve';
	      					aTagHtml += '<a href="goView(\'Approve\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상세보기" class="compl_consult w-100">상세보기</a>';							
					}else if(ra.STATUS == '33'){
							itemClass = 'reject';
	      					aTagHtml += '<a href="goView(\'Approve\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상세보기" class="compl_consult w-100">상세보기</a>';														
					};
					break;				
				case 'RC':
					itemClass = 'on_consult';
					aTagHtml += '<a href="#" onclick="goModify(\'Cnslt\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상담작성" class="writ_consult w-100">상담작성</a>';	 break;
				case 'AC':
					itemClass = 'on_consult';
					aTagHtml += '<a href="#" onclick="cnsltReceive(\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\')" title="상담접수" class="writ_consult w-100">상담접수</a>';	 break;
				case 'WC':
					itemClass = 'on_consult';
					 if (ra.CNSLT_PROF.indexOf("${loginVO.memberId}") !== -1) {
						 aTagHtml += '<a href="#" onclick="goModify(\'Cnslt\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상담작성" class="writ_consult w-100">상담작성</a>';
				    } else{
						 aTagHtml += '<a href="#" onclick="goModify(\'Cnslt\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상담작성" class="writ_consult">상담작성</a>';
						 aTagHtml += '<a href="#" onclick="cnsltCancel(\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\')" title="접수취소" class="cancel_regi">접수취소</a>';				    	
				    }
					break;
					
				case 'CC':
					itemClass = 'final_consult';
					aTagHtml += '<a href="#" onclick="goView(\'Cnslt\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상세보기" class="compl_consult w-100">상세보기</a>';	 break;
				case 'WJ':
					itemClass = 'on_judge';
					aTagHtml += '<a href="#" onclick="goModify(\'Judge\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="심사" class="writ_consult w-100">심사</a>';	 break;
				case 'CJ':
					itemClass = 'final_judge';
					aTagHtml += '<a href="#" onclick="goView(\'Judge\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상세보기" class="compl_consult w-100">상세보기</a>';	 break;
				}
				aTagHtml += '</div>';
				
				
		        html += '<div class="item ' + itemClass + ' p-3">' +
		        '<div class="title_wrap d-flex flex-row mb-3 justify-content-between">' +
		            '<ul>' +
		                '<li class="title text-truncate">' + ra.SDM_KOR_NM + '</li>' +
		                '<li>' +
		                    '<ul class="cate d-flex gap-2 flex-wrap flex-sm-nowrap">' +
		                        '<li class="color_border">' +
		                            '<span>' + ra.FIRST_MAJOR + '</span>' +
		                            '<span>' + ra.OTHER_MAJOR + '</span>' +
		                        '</li>' +
		                    '</ul>' +
		                '</li>' +
		            '</ul>';
		          
		         html += aTagHtml;
		         
		         html += '</div>' +
	                '<div class="info_wrap d-flex flex-wrap p-3 gap-2">' +
	                    '<dl class="d-flex flex-row gap-2">' +
	                        '<dt>신청학생</dt>' +
	                        '<dd>' + ra.APLY_STUDENT_NM + '</dd>' +
	                    '</dl>' +
	                    '<dl class="d-flex flex-row gap-2">' +
	                        '<dt>수여학위</dt>' +
	                        '<dd>' + ra.AWD_DEGR_KOR_NM + '</dd>' +
	                    '</dl>' +
	                    '<br>' +
	                    '<dl class="d-flex flex-row gap-2">' +
	                        '<dt>설계학점</dt>' +
	                        '<dd>' + ra.TOTAL_CDT_NUM + '학점</dd>' +
	                    '</dl>' +
	                    '<dl class="d-flex flex-row gap-2">' +
	                        '<dt style="min-width: 80px;">교과목설계범위</dt>' +
	                        '<dd>' + ra.SBJT_DGN_RNG_FG_NM + '</dd>' +
	                    '</dl>' +
	                    '<dl class="d-flex flex-row gap-2">' +
	                        '<dt>신청유형</dt>' +
	                        '<dd>'+(ra.APLY_FG === 'NEW' ? '신설' : '변경')+'</dd>' +
	                    '</dl>' +
	                '</div>' +
	            '</div>';	         
			});		
		}else{
			html += '<div class="pf_no_planWrap d-flex flex-column justify-content-center align-items-center">' +
					'<p>정보가 없습니다.</p>' +
					'</div>';
		}
		$("#cnsltTabList").html(html);
	}

	//수정화면
	function goModify(fg, sdmCd, revsnNo, aplyFg){
		var isChg = '';
		if(aplyFg == 'CHG'){
			isChg = 'Chg';
		}
		var form = document.frmView;
		$("#SDM_CD").val(sdmCd);
		$("#REVSN_NO").val(revsnNo);
		form.action = '/web/studPlan/prof'+fg+isChg+'Modify.do';
		form.submit();
	}

	//상세보기화면
	function goView(fg, sdmCd, revsnNo, aplyFg){
		var isChg = '';
		if(aplyFg == 'CHG'){
			isChg = 'Chg';
		}	
		var form = document.frmView;
		$("#SDM_CD").val(sdmCd);
		$("#REVSN_NO").val(revsnNo);
		form.action = '/web/studPlan/prof'+fg+isChg+'View.do';
		form.submit();
	}
	
	//승인된 학생설계전공
	function goConfirmedView(a,b){
		var form = document.frmView;
		$("#SDM_CD").val(a);
		$("#REVSN_NO").val(b);
		form.action = '/web/studPlan/studView.do';
		form.submit();
	}

	//상담접수
	function cnsltReceive(sdmCd, revsnNo){
		if(confirm("접수하시겠습니까?")){	
			$.ajax({
				type:'POST', 
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				url: '/web/studPlan/profCnsltReceive.do?mId=36',
				data: JSON.stringify({
					SDM_CD : sdmCd,
					REVSN_NO : revsnNo
				}),
				success:function(data){	
					alert(data.RTN_MSG)
					if(data.RESULT == 'DONE'){
						getTab('AC');
						getAllTabCnt();				
					}else{
						console.log(data.RESULT)
					}
				}
			});
		}
	}

	//상담접수취소
	function cnsltCancel(sdmCd, revsnNo){
		if(confirm("접수취소하시겠습니까?")){	
			$.ajax({
				type:'POST', 
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				url: '/web/studPlan/profCnsltCancel.do?mId=36',
				data: JSON.stringify({
					SDM_CD : sdmCd,
					REVSN_NO : revsnNo
				}),
				success:function(data){	
					alert(data.RTN_MSG)
					if(data.RESULT == 'DONE'){
						getTab('WC');
						getAllTabCnt();				
					}else{
						console.log(data.RESULT)
					}
				}
			});
		}
	}
			
	
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
	            var item = $('<div class="item" style="cursor:pointer;" onclick="goConfirmedView(\'' + list.SDM_CD + '\',\'' + list.REVSN_NO + '\')"></div>');
	            var ul = $('<ul class="d-flex flex-row gap-2 cate"></ul>');
	            ul.append('<li class="year">' + list.APLY_YEAR + '</li>');
	            item.append(ul);

	            var h3 = $('<h3 class="title fs-5 fw-bolder text-start mb-3"></h3>').text(list.SDM_KOR_NM);
	            item.append(h3);

	            var h4 = $('<div class="spl_title_list"></div>');
	            $.each(data.STUDPLAN_SBJT_LIST, function(i, sbjtList) {
	                if (sbjtList.SDM_CD === list.SDM_CD && sbjtList.REVSN_NO === list.REVSN_NO) {
	                    h4.append($('<h4 class="title fs-6 text-start"></h4>').text(sbjtList.MAJOR_NM));
	                }
	            });
	            item.append(h4);

	            var detailBox = $('<div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2"></div>');
	            var degree = $('<dl class="d-flex flex-wrap"></dl>');
	            degree.append('<dt>수여학위</dt><dd>' + (list.AWD_DEGR_KOR_NM == undefined ? '' : list.AWD_DEGR_KOR_NM) + '</dd>');
	            detailBox.append(degree);

	            var credits = $('<dl class="d-flex flex-wrap"></dl>');
	            credits.append('<dt>설계학점</dt><dd>' + (list.TOTAL_CDT_NUM == undefined ? '' : list.TOTAL_CDT_NUM) + '학점</dd>');
	            detailBox.append(credits);

	            var range = $('<dl class="d-flex flex-wrap"></dl>');
	            range.append('<dt>교과목설계범위</dt><dd>' + (list.SBJT_DGN_RNG_FG_NM == undefined ? '' : list.SBJT_DGN_RNG_FG_NM) + '</dd>');
	            detailBox.append(range);

	            item.append(detailBox);
	            studPlanList.append(item);
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
					console.log(JSON.stringify(data))
					alert(data.RTN_MSG);
					return false;
					if(data.RESULT == "DONE"){
						/*수정화면으로 이동*/
						var form = document.frmView;
						$("#SDM_CD").val(sdmCd);
						$("#REVSN_NO").val(revsnNo);
						form.action = '/web/studPlan/stdChgModify.do';
						form.submit();
					}
				}
			});	
		}
	}	
</script>