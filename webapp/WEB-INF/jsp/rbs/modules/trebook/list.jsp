<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	// View 변경
	$(document).on('click', '#cms_trebook_article .fn_view', function(){
		fnSetView($(this));
	});
	
});

function fnSetView(theObj){
	var hKey = theObj.attr('href');
	$('.fn_view_on').attr('class','fn_view');
	theObj.attr('class', 'fn_view_on');
	
	var varThisLi = $('#selectView .tbViewTreA');
	var varNewLi = varThisLi.clone();	
	
	varNewLi.find('#viewFImg').attr('src', $('#hFImg'+hKey).val());
	varNewLi.find('#viewFImg').attr('alt', $('#hFImgAlt'+hKey).val());
	varNewLi.find('#viewTreIssue').html($('#hTreIssue'+hKey).val());	
	<c:if test="${!empty settingInfo.use_part_type && settingInfo.use_part_type == 1}">
	varNewLi.find('#viewTrePartType').html($('#hTrePartType'+hKey).val());
	</c:if>	
	<c:if test="${!empty settingInfo.use_multi_cate && settingInfo.use_multi_cate == 1}">
	varNewLi.find('#viewCategory').html($('#hCategory'+hKey).val());
	</c:if>
	varNewLi.find('#viewTreContents').html($('#hContents'+hKey).val());
	
	varNewLi.find('#viewFileLink').attr('href', $('#hFileLink'+hKey).val());
	varNewLi.find('#viewFileLink').attr('title', $('#hFileLinkTitle'+hKey).val());
	varNewLi.find('#viewFileImg').attr('alt', $('#hFileAlt'+hKey).val());
	
	varThisLi.after(varNewLi);
	varThisLi.remove();
}
</script>