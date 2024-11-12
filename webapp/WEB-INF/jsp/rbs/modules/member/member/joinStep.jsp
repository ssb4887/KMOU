<script type="text/javascript">
$(function(){	
	$('.regStep ol li').each(function(index, item){
		var varJoinStep = '<c:out value="${param.joinStep}"/>';
		if((index + 1) == varJoinStep) $(item).addClass('on');
		else $(item).removeClass('on');
	});
});
</script>