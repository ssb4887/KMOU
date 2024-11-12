<%@ include file="../../include/commonTop.jsp"%>
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
	getBookmarkList()
	.then(data => {		
		var id = "${majorInfo.MAJOR_CD}";				
		data.forEach(function(bookmark) {
    		if(id == bookmark.docId){
        		 $("#"+id).addClass("on_red");
    		}
 		});	
		
	});
	
});

//찜 가져오기
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

//찜 변경
function likeChange(docId, type){
	if(!$("#"+docId).hasClass("on_red")){			
		$.ajax({
			url: '/web/bookmark/insertBookmark.do?mId=37&menuFg=major',
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
			url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg=major',
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