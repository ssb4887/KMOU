<%@ include file="../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	fn_fixedTableHead($("#<c:out value="${param.listFormId}"/>>table"), "100%", "400px");
	
	$("input[name='select']").change(function(){
		fn_setTreeSelectObjs(this);
	});
});
</script>