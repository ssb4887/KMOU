<%@ include file="../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	fn_fixedTableHead($("#<c:out value="${param.listFormId}"/>>table"), "100%", "<c:out value="${param.listTableHeight}"/>px");
});
</script>