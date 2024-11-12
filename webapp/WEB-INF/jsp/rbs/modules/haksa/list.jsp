<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
var originMemberId = "${loginVO.memberId}"
var originMemberName = "${loginVO.memberName}"
	$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	getCollegeData();

	// 학부(과)목록 불러오기(대학 선택시) - 전공교과목
	$("#selectColg").on('change', function(){
		$('#selectMj option:not(:first)').remove();
		if($(this).val() != ''){			
			$.ajax({
				url : '/web/studPlan/getDeptList.do?mId=36',
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
		}else{
	           var html = '<option value="">학부(과)</option>';            
	           $('#selectDept').html(html);
	           $('#selectMj').show();
		}
	});
	
	// 전공목록 불러오기(학부 선택시) - 전공교과목
	$("#selectDept").on('change', function(){
		
        var selectedDept = $("#selectDept option:selected").text();
        var lastTwoChars = selectedDept.slice(-2);
        
        if (lastTwoChars === '학과' || lastTwoChars === '전공' || selectedDept.slice(-1) === '과') {
        	$('#selectMj').hide();
        } else {
        	$('#selectMj').show();
			$.ajax({
				url : '/web/studPlan/getMajorList.do?mId=36',
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
	
    // 전공 검색 입력창에서 엔터를 누르면 검색
    $("input[name=student_nm]").on("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            getStudentList(1);
        }
    });
	
	$('#fn_btn_search').click(function(){
		getStudentList(1);
	})

});
 

// 대학 selectbox 세팅
function getCollegeData() {
	try {		
		// 대학 목록 불러오기
		$.ajax({
			url: '/web/studPlan/getCollList.do?mId=36',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'GET', 
			data: { 
				sbjtDgnRngFg : ""
			},
			success:function(data){
				console.log($("#selectColg").children().length)
				// 대학
				if($("#selectColg").children().length == 0){
					var rs = data.COLLEGE_LIST;
					var html = '<option value="">전체</option>';
					
					for(var i=0; i < rs.length; i++){
						html += '<option value="'+rs[i].DEPT_CD+'">'+rs[i].DEPT_NM+'</option>'
					}
					$('#selectColg').html(html);
				}
			}
		});
	} catch(e){
		console.log(e);
	}
}

// 학생 리스트
function getStudentList(pageNo){		
	
	//대학/전공 선택값(마지막 선택값만 API로 넘긴다)
	var selectedBox = ($("#selectMj").val() != '' ? $("#selectMj").val() : ($("#selectDept").val() != '' ? $("#selectDept").val() : ($("#selectColg").val() != '' ? $("#selectColg").val() : '')));	
	var searchText = $('#student_nm').val();

   	
   	$.ajax({
		url : '/RBISADM/menuContents/web/haksa/getStudentList.do?mId=69',
		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
		contentType:'application/json',
		type:'POST', 
		data: JSON.stringify({ 
			SEARCH_RANGE : selectedBox == null ? '' : selectedBox,
			SEARCH_TEXT : (searchText||''),
			PAGE : pageNo
		}),
		success:function(data){
			console.log(JSON.stringify(data))
			renderStudentList(data)
		}
	});
   	
        
}

function renderStudentList(data){
    $('#studentList').empty(); 
	$('#page').empty(); 
	
    var html = '';
    
    var list = data.list;
	var pageInfo = data.paginationInfo;
	
    if (list == null) {
        html += '<tr>';
        html += '<td colspan="5" class="bllist">조회된 학생이 없습니다.</td>';
        html += '</tr>';
    } else {
        var listNo = 1;
        $.each(list, function(index, listDt) {        	
            html += '<tr onclick="javascript:goStudentView(\''+listDt.STUDENT_NO+'\')" style="cursor: pointer;">';
            html += '<td class="num">' + listNo + '</td>';
            html += '<td>'+listDt.NM+'</td>';
            html += '<td>' + listDt.STUDENT_NO + '</td>';
            html += '<td>' + ((listDt.R_COLG_NM||'') != '' ? listDt.R_COLG_NM + '>' : '') + (listDt.R_DEPT_NM||'') + ((listDt.R_MAJOR_NM||'') != '' ? '>' + listDt.R_MAJOR_NM  : '' )+ '</td>';
            html += '<td><a href="#;" onclick="javascript:goStudentView(\''+listDt.STUDENT_NO+'\')" class="btnTypeE fn_btn_write_open" style="padding:0 8px;">조회</a></td>';
            html += '</tr>';
            listNo++;
        });
    }

    $('#studentList').html(html);
	
	
    // 페이징
	
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
			pageCon += '<li class="page-item page-first"><a href="javascript:getStudentList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
			pageCon += '<li class="page-item page-prev"><a href="javascript:getStudentList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}/${crtSiteId}/images/arr_left_gray.png"/>" alt="이전으로 화살표" /></a></li>';
		}
	  		
	}
	
	for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
		if(pageNo == currentPageNo){
			pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getStudentList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
		} else{
			pageCon += '<li class="page-item"><a class="page-link" href="javascript:getStudentList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
		}
	}
	
		if(totalPageCount > pageSize){
		if(lastPageNoOnPageList < totalPageCount){
			var nextPageNo = firstPageNoOnPageList + pageSize;
		} else{
			var nextPageNo = lastPageNo;
		}
		if(currentPageNo != lastPageNo){
			pageCon += '<li class="page-item page-next"><a href="javascript:getStudentList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}/${crtSiteId}/images/arr_left_gray.png"/>" alt="다음으로 화살표" /></a></li>';
			pageCon += '<li class="page-item page-last"><a href="javascript:getStudentList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';
		}
		
	}
		
	$('#page').append(pageCon);
}	

function goStudentView(student_no){
	location.href = '${URL_VIEWINFO}&STUDENT_NO='+student_no
}

</script>