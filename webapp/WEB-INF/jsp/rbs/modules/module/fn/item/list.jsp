<%@ include file="../../../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){

	// 위
	$("button.fn_btn_up").click(function(){
		fn_setOrderSubmitForm($(this), 0);
		return false;
	});

	// 아래
	$("button.fn_btn_down").click(function(){
		fn_setOrderSubmitForm($(this), 1);
		return false;
	});

});

function fn_setOrderSubmitForm(theObj, theType){
	try{
		var varSBtnType, varTBtnType;
	
		var varTypeNames = ['.fn_btn_up', '.fn_btn_down'];
	
		varSBtnType = varTypeNames[theType];
		varTBtnType = varTypeNames[(theType+1)%2];
			
		var varIdx = $(varSBtnType).index(theObj);
		var varItemId = $(theObj).attr('data-id');
		var varOrderIdx = $(varTBtnType).eq(varIdx).attr('data-id');
		
		var varForm = $("#<c:out value="${param.inputFormId}"/>");
		varForm.find("#orderIdx").val(varOrderIdx);
		varForm.find("#itemId").val(varItemId);
		
		varForm.submit();
	}catch(e){return false;}

}
</script>