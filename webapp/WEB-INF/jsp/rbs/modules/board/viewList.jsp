<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<script type="text/javascript">
$(function(){	
	// 상세보기
	$(".fn_btn_view").click(function(){
		var varIsNMChk = $(this).attr("data-nm");
		if(fn_isValFill(varIsNMChk)) {
			<%-- 비회원 글쓰기/댓글쓰기 권한 --%>
			<c:choose>
			<%--
				<c:when test="${(elfn:isNmAuthPage('WRT') and elfn:isNoMemberAuthPage('RWT')) or (elfn:isNmAuthPage('RWT') and elfn:isNoMemberAuthPage('WRT'))}">
			--%>
				<c:when test="${elfn:isNoMemberAuthPage('RWT') or elfn:isNoMemberAuthPage('WRT')}">
					if(confirm('비밀번호를 입력하시겠습니까?'))
						fn_showPwdDialog(varIsNMChk, $(this).attr("href"), 'v');
					else
						top.location.href = $(this).attr("href");
				</c:when>
				<c:otherwise>
					fn_showPwdDialog(varIsNMChk, $(this).attr("href"), 'v');
				</c:otherwise>
			</c:choose>
			return false;
		}
		return true;
	});
});
</script>
