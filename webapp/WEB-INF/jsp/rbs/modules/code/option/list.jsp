<%@ include file="../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	fn_fixedTableHead($("#<c:out value="${param.listFormId}"/>>table"), "100%", "400px");
	
	$("#sptInputProc").submit(function(e){
		var selectedValues = [];
		var hasDuplicates = false;
		
		$('select[id^=ordIdx]').each(function(){
			var selectedValue = $(this).val();
			if(selectedValues.indexOf(selectedValue) != -1){
				hasDuplicates = true;
			}
			selectedValues.push(selectedValue);
		});
		
		if(hasDuplicates){
			alert("순서가 중복되었습니다. 다시 확인해주세요.");
			e.preventDefault();
		}
	});
});
</script>