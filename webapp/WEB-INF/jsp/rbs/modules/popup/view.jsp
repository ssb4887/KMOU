<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	try {
		var varHeight = <c:out value="${dt.POP_HEIGHT}"/>;
		var varWHeight = $("#cms_board_article").height();
		if(varWHeight < varHeight) $("#cms_board_article").css("height", varHeight + "px");
		/*var strWidth, strHeight;
	    strWidth = <c:out value="${dt.POP_WIDTH}"/>;
		if(window.outerHeight) {
			//strWidth = $('#cms_board_article').outerWidth() + window.outerWidth - window.innerWidth;
			strHeight = $('#cms_board_article').outerHeight() + window.outerHeight - window.innerHeight;
		} else {
		    //var strDocumentWidth = $(document).outerWidth();
		    var strDocumentHeight = $(document).outerHeight();

		    fn_window.resizeTo(strWidth, strDocumentHeight);

		    //var strMenuWidth = strDocumentWidth - $(window).width();
		    var strMenuHeight = strDocumentHeight - $(window).height();

		    //strWidth = <c:out value="${dt.POP_WIDTH}"/> + strMenuWidth;
		    strHeight = <c:out value="${dt.POP_HEIGHT}"/> + strMenuHeight;
		}
		fn_window.resizeTo(strWidth, strHeight);*/
	} catch(e){}
});
</script>