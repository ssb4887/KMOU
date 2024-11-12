<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	try {
		var varHeight = <c:out value="${dt.POP_HEIGHT + 30}"/>;
		var varWHeight = $("#cms_board_article").height();
		if(varWHeight < varHeight) $("#cms_board_article").css("height", varHeight + "px");
	} catch(e){}
});
</script>