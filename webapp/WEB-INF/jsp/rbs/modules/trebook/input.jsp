<%@ include file="../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}proc_${settingInfo.use_pdf_upload}_order"/>
<itui:submitScript items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
<script type="text/javascript">
$(function(){
	<itui:submitInit items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
	fn_boardInputReset();
	
	// reset
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_reset").click(function(){
		try {
			$("#<c:out value="${param.inputFormId}"/>").reset();
			fn_boardInputReset();
		}catch(e){return false;}
	});
	
	// cancel
	$("#<c:out value="${param.inputFormId}"/> .fn_btn_cancel").click(function(){
		try {
			location.href="<c:out value="${URL_LIST}"/>";
		}catch(e){return false;}
	});
	
	// 등록/수정
	$("#<c:out value="${param.inputFormId}"/>").submit(function(){
		try {
			return fn_boardInputFormSubmit();
		}catch(e){return false;}
	});
	
	// 위 추가
	$(document).on('click', '#fn_textareaUl .fn_add_up', function(){
		fn_addTextArea($(this), 1);
	});
	// 아래 추가
	$(document).on('click', '#fn_textareaUl .fn_add_down', function(){
		fn_addTextArea($(this), 2);
	});
	// 삭제
	$(document).on('click', '#fn_textareaUl .fn_add_delete', function(){
		fn_deleteTextArea($(this));
	});
	// Con File Delete
	$(document).on('click', '#fn_textareaUl #fn_btn_del_conPdfFile', function(){
		fn_deletePdfFile($(this));
	});	
});

function fn_boardInputReset(){
	<itui:submitReset items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>
}
function fn_boardInputFormSubmit(){
	<itui:submitValid items="${itemInfo.items}" itemOrder="${itemInfo[itemOrderName]}"/>	
	
	<c:set var="conItems" value="${moduleItem.con_item_info.items}"/>			
	<c:set var="conNumItem" value="${conItems.conNum}"/>
	<c:set var="conSubjectItem" value="${conItems.conSubject}"/>
	<c:choose>
		<c:when test="${settingInfo.use_pdf_upload == 1}">
			<c:set var="conLinkUrlItem" value="${conItems.conPdfFile}"/>
			<c:set var="conLinkUrlClass" value="conPdfFile"/>
		</c:when>
		<c:otherwise>
			<c:set var="conLinkUrlItem" value="${conItems.conLinkUrl}"/>
			<c:set var="conLinkUrlClass" value="conLinkUrl"/>
		</c:otherwise>
	</c:choose>
	<c:set var="conContentsItem" value="${conItems.conContents}"/>
	
	var varChk = true;	
	var varConObjs = [	$('#fn_textareaUl input[name="conNum"]')
	            		,$('#fn_textareaUl input[name="conSubject"]')
	            		,$('#fn_textareaUl input[name="${conLinkUrlClass}"]')
						,$('#fn_textareaUl textarea[name="conContents"]')
           			 ];
	
	var varConObjMaxs = [	${conNumItem['maximum']}
							,${conSubjectItem['maximum']}
							,${conLinkUrlItem['maximum']}
							,${conContentsItem['maximum']}
	                     ];	
	
	try {		
		// 내용 입력여부 체크
		var varIsConFill = true;
		var varConSize = $('#fn_textareaUl input[name="conSubject"]').size();
		if(varConSize == 1)	{
			varIsConFill = false;						
			$.each(varConObjs, function(index, item){
				varIsConFill = fn_isFill(item);
				if(varIsConFill) return false;
			});
		} 
		
		//입력된 내용 있는 경우 필수입력, 입력글길이 제한 체크
		if(varIsConFill) {
			$.each($('#fn_textareaUl input[name="conSubject"]'), function(theIdx, theObjItem) {
				if(varChk) {
					try {
						var varVal = $(this).val();
						if(!fn_isValFill(varVal)) {
							var varTitle = $(this).attr('title');
							alert(fn_Message.fillAllText(varTitle));
							varChk = false;
							return false;
						}
						
						$.each(varConObjs, function(index, item) {
							var varObj = item[theIdx];
							if(!fn_checkStrMaxLenForm(varObj, varConObjMaxs[index], varObj.title))
							{
								varChk = false;
								return false;
							}
						});
					} catch(e) {alert(e);varChk = false;}
				}
			});
			
			if(!varChk) return false;
			
			var varConIdxs = $('#fn_textareaUl input[name="conIdx"]');
			$.each(varConObjs, function(index, item) {
				$.each($(item), function(theIdx, theObjItem) {
					var varObjType = theObjItem.type;
					if(varObjType == 'file') {
						var varFObjName = $(theObjItem).attr('name');
						var varObjName, varObj;
						var varFOObjNames = [varFObjName + '_text', varFObjName + '_saved', varFObjName + '_origin', varFObjName + '_size', varFObjName + '_deleted_idxs'];
						$.each(varFOObjNames, function(theFIdx, theFItem) {
							varObj = $('input[name="' + theFItem + '"]:eq(0)'); // 주의!!
							varObj.attr('name', varObj.attr('name') + $(varConIdxs[theIdx]).val());
						});
					}
					$(theObjItem).attr('name', $(theObjItem).attr('name') + $(varConIdxs[theIdx]).val());
					
				});
			});
		}
	} catch(e){return false}

	fn_createMaskLayer();
	return true;
}

var gVarConMaxIdx = <c:choose><c:when test="${!empty conList}">${fn:length(conList)}</c:when><c:otherwise>1</c:otherwise></c:choose>;
function fn_addTextArea(theObj, theFlag)
{	
	var varUlObj = $('#fn_textareaUl');	
	if($('#fn_textareaUl input[name="conNum"]').size() > 20) {
		alert('내용은 20개만 등록하실 수 있습니다.');
		return;
	}

	var varThisLi = theObj.parents('dl');
	var varNewLi = varThisLi.clone();
	var varThisLiIdx = varThisLi.parent().find('dl').index(varThisLi);
	if(theFlag == 1)
	{
		// 위 
		varThisLi.before(varNewLi);
		if(varThisLiIdx == 0)
		{ 
			varNewLi.addClass('first');
			varThisLi.removeClass('first');
		}
	}
	else
	{
		// 아래 
		varThisLi.after(varNewLi);
		if(varThisLiIdx == 0)
		{ 
			varNewLi.removeClass('first');
		}
	}
	
	varNewLi.find('input[name="conNum"]').val('');
	varNewLi.find('input[name="conSubject"]').val('');
	<c:choose>
	<c:when test="${settingInfo.use_pdf_upload == 1}">
	varNewLi.find('input[name="conPdfFile_saved"]').val('');
	varNewLi.find('input[name="conPdfFile_deleted_idxs"]').val('');	
	varNewLi.find('input[name="conPdfFile"]').val('');
	varNewLi.find('input[name="conPdfFile_text"]').val('');
	varNewLi.find('.saved_file_nb').remove();	
	</c:when>
	<c:otherwise>
	varNewLi.find('input[name="conLinkUrl"]').val('');
	</c:otherwise>
	</c:choose>
	varNewLi.find('textarea[name="conContents"]').val('');
	varNewLi.find('input[name="conIdx"]').val(gVarConMaxIdx);
	gVarConMaxIdx ++;
}

/***************************************************************
 textarea 추가
***************************************************************/
function fn_deleteTextArea(theObj)
{
	var varUlObj = $('#fn_textareaUl');
	var varLiObjs = varUlObj.find('>dl');
	var varLiLen = varLiObjs.size();

	if(varLiLen <= 1)
	{
		alert('모두 삭제하실 수 없습니다.');
		return;
	}
	theObj.parents('dl:first').remove();
}

/***************************************************************
Con PDF File Delete
***************************************************************/
function fn_deletePdfFile(theObj)
{	
	var varThisLi = theObj.parents('dl');
	var state = varThisLi.find('input[name="conPdfFile_deleted_idxs"]').val();
	if(state == 1){
		varThisLi.find('input[name="conPdfFile_deleted_idxs"]').val('');
		theObj.text('삭제');
	}else{
		varThisLi.find('input[name="conPdfFile_deleted_idxs"]').val('1');
		theObj.text('삭제취소');	
	}
}
</script>