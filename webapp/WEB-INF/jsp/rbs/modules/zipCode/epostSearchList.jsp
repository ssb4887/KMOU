<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	$('#fn_zip_tab li>button').click(function(){
		fn_setZipTab(this);
	});
	<c:set var="paramType" value="${queryString.type}"/>
	<c:if test="${empty paramType}"><c:set var="paramType" value="road"/></c:if>
	fn_setZipTab($('#fn_zip_tab li>button[value="<c:out value="${paramType}"/>"]'), true);

	function fn_setZipTab(theObj, theFlag) {
		var varVal = $(theObj).val();
		if(!theFlag) $('.cont_zip [class*="fn_item_"] input').val('');
		$('.cont_zip [class*="fn_item_"]').addClass('skip');
		$('.cont_zip p[id*="fn_title_"]').addClass('skip');
		$('#fn_title_' + varVal).removeClass('skip');
		
		var varTitle1 = $(theObj).attr('data-title1');
		var varTitle2 = $(theObj).attr('data-title2');
		$('.post-search-area dt.fn_data_title1').text(varTitle1);
		$('.post-search-area dt.fn_data_title2').text(varTitle2);

		$('#sRGRoad').attr('title', varTitle1);
		$('#sRGBNum1').attr('title', varTitle2);
		$('#sRGBNum2').attr('title', varTitle2);
		
		$(theObj).parent('li').siblings('li').find('button').removeClass('on');
		$(theObj).addClass('on');
		$('#type').val(varVal);
	}
	
	$("#<c:out value="${param.searchFormId}"/>").submit(function(){return fnzipcodeSearchFormSubmit();});

	$('#resultData .fn_btn_addr_submit').click(function(){fn_zipcodeSubmitForm(this);});
});
function fnzipcodeSearchFormSubmit(){
	var varTypeVal = $('#type').val();

	var varLenChk = true;
	// 도로명 주소
	var varRoad = $('#sRGRoad');
	var varBNum1 = $('#sRGBNum1');
	var varBNum2 = $('#sRGBNum2');
	if(!fn_isFill(varRoad) || !fn_isFill(varBNum1)) {
		alert(varRoad.attr('title') + '과 ' + varBNum1.attr('title') + '을(를) 입력하셔야 합니다.');
		return false;
	}

	return true;
}

function fn_zipcodeSubmitForm(theObj){
	var varObj = $(theObj);
	var varType = varObj.attr('data-type');
	
	var varPObj = varObj.parents('tr');
	
	var varZip = varPObj.find('.fn_zip').text().trim();
	var varAddr = varPObj.find('.fn_' + varType + '_addr').text().trim();

	var varOpenerObj;
	try{
		varOpenerObj = top.opener;
	}catch(e){
		varOpenerObj = parent;
	}
    try {
    	varOpenerObj.fn_setAddrForm.set(varZip, varAddr);
    } catch(e) {
    }

	self.close();
}
</script>