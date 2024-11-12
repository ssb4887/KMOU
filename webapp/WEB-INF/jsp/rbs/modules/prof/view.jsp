<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<script type="text/javascript">
$(function(){
	<c:if test="${mngAuth || wrtAuth && dt.AUTH_MNG == '1'}">	
	// 삭제
	$(".fn_btn_delete").click(function(){
		try {
			var varConfirm = confirm("<spring:message code="message.select.delete.confirm"/>");
			if(!varConfirm) return false;
		}catch(e){return false;}
		return true;
	});
	</c:if>
	//찜 onRed class 추가
	getBookmarkList().then(data => {		
		var id = "${profInfo.EMP_NO}";				
		data.forEach(function(bookmark) {
    		if(id == bookmark.docId){
        		 $("#"+id).addClass("on_red");
    		}
 		});	
		
	});

	var page = "1";
	
	getSubList(page);	
});

function getSubList(page){
	var empNo = "${profInfo.EMP_NO}";
	var year = "2024";
	var varAction = "${elfn:replaceScriptLink(URL_SUB_LIST_AJAX)}&EMP_NO=" + empNo + "&YEAR=" + year + "&page=" + page;
	
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			
  			var varItemObj = $(".subListTbody");
  			varItemObj.find("tr").remove();
  			var htmlPage = $("#page");
  			htmlPage.empty();
  			var list = result.list;
  			if(list) {
  				$.each(list, function(i, item){ 
  					var varCon = '<tr>\n';
  					varCon += `<td class="name text-start text-lg-center align-middle"><a href="javascript:void(0);" onclick="sbjtView('\${item.SUBJECT_CD}', '\${item.DEPT_CD}', '\${item.YEAR}', '\${item.SMT}')">\${item.SUBJECT_NM}</a></td>\n`;
  					varCon += '<td class="name text-start text-lg-center align-middle">';
  					varCon += item.YEAR;
  					varCon += '</td>\n';
  					varCon += '<td class="name text-start text-lg-center align-middle">';
  					varCon += item.SMT_NM;
  					varCon += '</td>\n';
  					varCon += '<td class="name text-start text-lg-center align-middle">';
  					varCon += item.CLASS_NM;
  					varCon += '</td>\n';
  					varCon += '<td class="name text-start text-lg-center align-middle">';
  					varCon += item.COLG_NM;
  					varCon += '</td>\n';
  					varCon += '<td class="name text-start text-lg-center align-middle">';
  					varCon += item.DEPT_NM;
  					varCon += '</td>\n';
  					varCon += '<td class="name text-start text-lg-center align-middle">';
  					varCon += item.COMDIV_NM;
  					varCon += '</td>\n';
  					varCon += '<td class="name text-start text-lg-center align-middle">';
  					varCon += item.SUBJECT_CD + ' - ' + item.DIVCLS;
  					varCon += '</td>\n';
  					
  					varCon += '</tr>\n';
  					varItemObj.append(varCon);
  					console.log(varCon);
  				});
  				
  			
  			
  			} /* else{
  				var varCon = '<tr>\n';
 					varCon += '<td colspan="8" class="bllist"><spring:message code="message.no.list"/></td>';
 					varCon += '</tr>\n';
 					varItemObj.append(varCon);
  			} */
  			
  			

  			var pageInfo = result.paginationInfo;
  			
  			var currentPageNo = pageInfo.currentPageNo;
  			var totalPageCount = pageInfo.totalPageCount;
  			var pageSize = pageInfo.pageSize;
  			var firstPageNo = pageInfo.firstPageNo;
  			var lastPageNo = pageInfo.lastPageNo;
  			var firstPageNoOnPageList = pageInfo.firstPageNoOnPageList;
  			var lastPageNoOnPageList = pageInfo.lastPageNoOnPageList;
  			var defaultListUrl = '${URL_VIEW}&empNo=${param.empNo}&page=';
  			var pageCon = "";
  			console.log(pageInfo);
  			
  			
  			if(totalPageCount > pageSize){
  				if(firstPageNoOnPageList > pageSize){
  					var prevPageNo = firstPageNoOnPageList  - pageSize;
  				} else{
  					var prevPageNo = firstPageNo;
  				}
  				if(currentPageNo != firstPageNo){
  					pageCon += '<li class="page-item page-first"><a href="javascript:getSubList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
  	  				pageCon += '<li class="page-item page-prev"><a href="javascript:getSubList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';
  				}
  				
  	  	  		
  			}
  			
  			
	  		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
	  			if(pageNo == currentPageNo){
	  				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getSubList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
	  			} else{
	  				pageCon += '<li class="page-item"><a class="page-link" href="javascript:getSubList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
	  			}
	  		}
  	  		if(totalPageCount > pageSize){
  				if(lastPageNoOnPageList < totalPageCount){
  					var nextPageNo = firstPageNoOnPageList + pageSize;
  				} else{
  					var nextPageNo = lastPageNo;
  				}
  				if(currentPageNo != lastPageNo){
  					pageCon += '<li class="page-item page-next"><a href="javascript:getSubList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
  	  	  			pageCon += '<li class="page-item page-last"><a href="javascript:getSubList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';  					
  				}
  				
  	  		
  			}
	  		
  			
  			console.log(pageInfo);
  			htmlPage.append(pageCon);
			
			
  			return false;
  		}, 
  		error:function(request,error){
  			alert("실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	});
}

//찜 가져오기
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

//찜 변경
function likeChange(docId, type){
	if(!$("#"+docId).hasClass("on_red")){			
		$.ajax({
			url: '/web/bookmark/insertBookmark.do?mId=37&menuFg=prof',
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
			url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg=prof',
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

/* 상세보기(교과목코드, 부서코드, 년도, 학기) */
function sbjtView(a,b,c,d){
	var form = document.sbjtViewForm;
	form.SUBJECT_CD.value = a;
	form.DEPT_CD.value = b;
	form.YEAR.value = c;
	form.SMT.value = d;
	form.submit();
}
</script>