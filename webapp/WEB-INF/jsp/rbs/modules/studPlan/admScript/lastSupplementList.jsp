<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">


	$(function(){
        
		// 대학 목록 불러오기
		
		$.ajax({
			url: '/RBISADM/menuContents/web/studPlan/getCollList.do?mId=48',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'GET', 
			data: { 
				sbjtDgnRngFg : ''
			},
			success:function(data){
				// 대학
				if($("#selectColg").children().length == 0){
					var rs = data.COLLEGE_LIST;
					var html = '<option value="">대학</option>';
					
					for(var i=0; i < rs.length; i++){
						html += '<option value="'+rs[i].DEPT_CD+'">'+rs[i].DEPT_NM+'</option>'
					}
					$('#selectColg').html(html);
				}
				
			}
		});
	
		// 학부(과)목록 불러오기(대학 선택시) - 전공교과목
		$("#selectColg").on('change', function(){
			$('#selectMj option:not(:first)').remove();
			$.ajax({
				url : '/RBISADM/menuContents/web/studPlan/getDeptList.do?mId=48',
				beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
				contentType:'application/json',
				type:'GET', 
				data: { 
					univ : $(this).val()
				},
				success:function(data){
					
					var items = data.DEPART_LIST;
					var html = '<option value="">학부(과)</option>';
					
					for(var i=0; i < items.length; i++){
						html += '<option value="'+items[i].DEPT_CD+'">'+items[i].DEPT_NM+'</option>'
					}
					
					$('#selectDept').html(html);
				}
			});
		});
		
		// 전공목록 불러오기(학부 선택시) - 전공교과목
		$("#selectDept").on('change', function(){
			
	        var selectedDept = $("#selectDept option:selected").text();
	        var lastTwoChars = selectedDept.slice(-2);
	        
	        if (lastTwoChars === '학과' || lastTwoChars === '전공') {
	        	$('#selectMj').hide();
	        } else {
	        	$('#selectMj').show();
				$.ajax({
					url : '/RBISADM/menuContents/web/studPlan/getMajorList.do?mId=48',
					beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
					contentType:'application/json',
					type:'GET', 
					data: { 
						depart : $(this).val()
					},
					success:function(data){
						
						var items = data.MAJOR_LIST;
						var html = '<option value="">전공</option>';
						
						for(var i=0; i < items.length; i++){
							html += '<option value="'+items[i].DEPT_CD+'">'+items[i].DEPT_NM+'</option>'
						}
						
						$('#selectMj').html(html);
					}
				});
	        }
		});
		
		//기본 목록 세팅
		getAffairCommitteeList(1)
	});
	
	
	
	// 창의융합교육센터에서 보여줄 학생설계전공 리스트
	function getAffairCommitteeList(pageNo){
		
		//대학/전공 선택값(마지막 선택값만 API로 넘긴다)
		var selectedBox = ($("#selectMj").val() != '' ? $("#selectMj").val() : ($("#selectDept").val() != '' ? $("#selectDept").val() : ($("#selectColg").val() != '' ? $("#selectColg").val() : '')));

       	if(selectedBox != null){
       		$("#showAllList").show();    
       	}else{       		
       		$("#showAllList").hide();    
       	}
       	
       	
       	$.ajax({
			url : '/RBISADM/menuContents/web/studPlan/getLastSupplementList.do?mId=48',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'POST', 
			data: JSON.stringify({ 
				SEARCH_RANGE : selectedBox == null ? '' : selectedBox,
				PAGE : pageNo
			}),
			success:function(data){
				console.log(JSON.stringify(data))
				renderStudPlanList(data)
			}
		});
       	
	        
	}
	
	function renderStudPlanList(data){
		var studPlanList = $('#studPlanList');
        var page = $('#page');
	    studPlanList.empty(); 
        page.empty(); 
		
        
        var html = '';
        var list = data.LIST;

        if (list.length === 0) {
            html += '<tr>';
            html += '<td colspan="8" class="bllist"><spring:message code="message.no.list"/></td>';
            html += '</tr>';
        } else {
            var listNo = 1;
            $.each(list, function(index, listDt) {
            	var btnTitle = '';
            	var btnType = '';
            	if(listDt.STATUS === 70){
            		btnTitle = '수정 / 마감';
            		btnType = 'E'
            	}else{
            		btnTitle = '상세보기';
            		btnType = 'F'
            	}
            	
                html += '<tr onclick="javascript:goModify(\''+listDt.SDM_CD+'\', \''+listDt.REVSN_NO+'\', \''+listDt.APLY_FG+'\')" style="cursor: pointer;">';
                html += '<td class="num">' + listNo + '</td>';
                html += '<td>'+listDt.STATUS_INF_NM+'</td>';
                html += '<td>' + (listDt.APLY_FG == 'NEW' ? '신설' : '변경') + '</td>';
                html += '<td>' + listDt.SDM_KOR_NM + '</td>';
                html += '<td>' + listDt.SBJT_DGN_RNG_FG_NM + '</td>';
                html += '<td>' + listDt.APLY_STUDENT_NM + '</td>';
                html += '<td class="date">' + listDt.DEPART_JUDG_DT + '</td>';                
                html += '<td><a href="#;" onclick="javascript:goModify(\''+listDt.SDM_CD+'\', \''+listDt.REVSN_NO+'\')" class="btnType'+btnType+' fn_btn_write_open" style="padding:0 8px;">'+btnTitle+'</a></td>';
                html += '</tr>';
                listNo++;
            });
        }

        $('#studPlanList').html(html);
		
		
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
				var prevPageNo = firstPageNoOnPageList - pageSize;
			} else{
				var prevPageNo = firstPageNo;
			}
			if(currentPageNo != firstPageNo){
				pageCon += '<li class="page-item page-first"><a href="javascript:getAffairCommitteeList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
				pageCon += '<li class="page-item page-prev"><a href="javascript:getAffairCommitteeList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}/${crtSiteId}/images/arr_left_gray.png"/>" alt="이전으로 화살표" /></a></li>';
			}
  	  		
		}
		
		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
			if(pageNo == currentPageNo){
				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getAffairCommitteeList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
			} else{
				pageCon += '<li class="page-item"><a class="page-link" href="javascript:getAffairCommitteeList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
			}
		}
		
  		if(totalPageCount > pageSize){
			if(lastPageNoOnPageList < totalPageCount){
				var nextPageNo = firstPageNoOnPageList + pageSize;
			} else{
				var nextPageNo = lastPageNo;
			}
			if(currentPageNo != lastPageNo){
				pageCon += '<li class="page-item page-next"><a href="javascript:getAffairCommitteeList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}/${crtSiteId}/images/arr_left_gray.png"/>" alt="다음으로 화살표" /></a></li>';
	  			pageCon += '<li class="page-item page-last"><a href="javascript:getAffairCommitteeList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';
			}
  		
		}
  		
  		page.append(pageCon);
	}	
	
	function goModify(a,b){
		var form = document.frmView;
		$("#SDM_CD").val(a);
		$("#REVSN_NO").val(b);
		form.action = '/RBISADM/menuContents/web/studPlan/lastSupplementModify.do';
		form.submit();
	}	

</script>