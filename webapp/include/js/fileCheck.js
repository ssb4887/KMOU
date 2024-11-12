var gVarUploadFilesObjId = 'fn_multiFile_';						// 업로드할 파일(input type="file") 저장소
var gVarExistFiles = new HashMap();										// upload된 파일 Array
var gVarDeleteFiles = new HashMap();										// upload된 파일 Array
var gVarFileLimitSize = 0;										// 파일 1개당 제한 용량
var gVarFileLimitTotalSize = 0;									// 파일 전체 제한 용량
var gVarFileLimitCount;											// 파일 업로드 갯수
var gVarLimitExt; 												// 파일 업로드 확장자
var fileIndex = 1;

$(function(){
	$(document).on("focus", "ul.multiFileUL input.altTxt, ul.singleFileUL input.altTxt", function(){
		var varId = $(this).attr("id");
		var varLabelObj = $(this).siblings("label[for='" + varId + "']");
		varLabelObj.hide();
	});
	$(document).on("blur", "ul.multiFileUL input.altTxt, ul.singleFileUL input.altTxt", function(){
		var varId = $(this).attr("id");
		var varLabelObj = $(this).siblings("label[for='" + varId + "']");
		if(fn_isFill($(this))) {
			varLabelObj.hide();
			return;
		}
		varLabelObj.show();
	});
	
	$("ul.multiFileUL input.altTxt, ul.singleFileUL input.altTxt").blur();
});
/************************************************
파일 선택
1. 파일업로드 갯수 제한
2. 중복된 파일 검사
3. 업로드 가능 - 확장자 검사
*************************************************/
function fn_checkMultiFile(theFileObj, theUseSingleUpload) {
	var varItemId = $(theFileObj).attr('name');
	var varDuplicate = false;
	//var varSelectOptions = $('#' + varItemId + '_total').find("option");
	var varSelChecks = $('input[name="' + varItemId + '_total"]');
	var varSelCount = varSelChecks.length + 1;
	
	// 1. 파일업로드 갯수 제한
	// gFileLimitCount : 0일 경우 무제한
	var varFileLimitCount = gVarFileLimitCount[varItemId];
	if(varFileLimitCount > 0) {
		if(varSelCount > varFileLimitCount) {
			alert(fn_Message.uploadFileLimitCountText(varFileLimitCount));

			// 새로운 input[type='file'] 추가, 현재 input[type='file'] 삭제
			fn_resetFileObj(theFileObj);
			//fn_addUploadFile(varItemId);
			//$(theFileObj).remove();
			//$(varUploadFileObjs).eq(varIdx).remove();
			return false;
		}
	}

	// 2. 중복된 파일 검사
	$(varSelChecks).each(function(index, item){
		if(!varDuplicate) {
			var varFileVal = $(theFileObj).val();
			var varVal = $(this).val();
			var varFileSValue = varFileVal.substring(varFileVal.lastIndexOf('\\') + 1);
		    if (varFileSValue == varVal.split('||')[2]) {
		        alert("같은 파일을 여러개 등록 할 수 없습니다.");

		        // 새로운 input[type='file'] 추가, 현재 input[type='file'] 삭제
				fn_resetFileObj(theFileObj);
				
				varDuplicate = true;
				
		        return false;
		    }
		}
	});
	
	if(varDuplicate) return false;
	
	// 이미지&pdf 파일만 업로드해야 할 때 확장자 검사(varItemId는 해당 id에 맞게 추가)
	if(varItemId == "photo" || varItemId == "bill"){
		var varFileValue = $(theFileObj).val();										// 원본파일명
		if(!fn_isValFill(varFileValue)) return true;
		
		var varExtName = varFileValue.substring(varFileValue.lastIndexOf(".") + 1);	// 원본파일확장자명
		varExtName = varExtName.toLowerCase();
		
		// 업로드 가능한 이미지 확장자 목록(추후에 필요하다면 추가나 삭제)
		var imgExtArray = ["jpg", "jpeg", "png", "gif", "pdf", "raw", "svg"];
		var varReVal;
		
		for(var varIdx = 0; varIdx < imgExtArray.length; varIdx ++){
			if(varExtName == imgExtArray[varIdx]){
				// 저장될 파일목록에 추가
				fn_fileCheck(theFileObj);
				
				return true;
			}
		}
		
		alert(".jpg, .jpeg, .png, .gif, .pdf, .raw, .svg 형식의 파일만 업로드 가능합니다.");
		return false;
		
	}
	
	// 3. 업로드 가능 - 확장자 검사
	if(!fn_isUploadExt(varItemId, theFileObj))	{
		alert(fn_Message.noUpfileText());

		fn_resetFileObj(theFileObj);
		//addUploadFile(varItemId);
		//$(theFileObj).remove();
		//$(varUploadFileObjs).eq(varIdx).remove();

		return false;
	}
	
	if(!theUseSingleUpload) {
		// 저장될 파일목록에 추가
		fn_fileCheck(theFileObj);
	}
	return true;
}

/************************************************
'추가'버튼 클릭한 경우
*************************************************/
function fn_fileCheck(theFileObj) {
	/*var varFileObjs = $("input[name='" + varItemId + "']");
	var varIdx = $(varFileObjs).length - 1;
	var varFileObj = $(varFileObjs).eq(varIdx);*/
	var varItemId = $(theFileObj).attr('name');
	var varFileValue = $(theFileObj).val();
	fn_addFileList(varItemId, 0, 0, varFileValue, '', true);		// 저장될 파일목록에 추가
	fn_addUploadFile(varItemId, true);								// uploadFile object 추가
}

//새로운 input[type='file'] 추가, 현재 input[type='file'] 삭제
function fn_resetFileObj(theFileObj) {
	var varItemId = $(theFileObj).attr('name');
	fn_addUploadFile(varItemId);
	$(theFileObj).remove();
}



/************************************************
업로드된 파일 checkbox 추가
- theItemId             : 항목ID
- theFileIdx			: 파일idx
- theFileSize			: 파일 크기
- theOriginFileName		: 원본 파일명
- theNewSelect          : 추가된 파일선택여부
*************************************************/
function fn_addFileList(theItemId, theFileIdx, theFileSize, theFileValue, theText, theNewSelect)
{
    // 1. select option 추가
	var varFileSValue = theFileValue.substring(theFileValue.lastIndexOf('\\') + 1);
	// value - 파일명(경로 포함)(fileList :fileIdx,fileSize,파일경로,파일명,대체텍스트)
	var varVal = theFileIdx + '||' + theFileSize + '||' + varFileSValue + '||' + theFileValue + '||' + theText;
	// text - 파일명 : 대체텍스트
	var varFileText = varFileSValue;
    if(theText) varFileText += ' : ' + theText;
    
    var varSelCheckDiv = $('#' + theItemId + '_total_layer')
    var varIdx = varSelCheckDiv.find('button').length + 1;
    varSelCheckDiv.append('<li><input type="checkbox" id="' + theItemId + '_total' + varIdx + '" name="' + theItemId + '_total" value="' + varVal + '"/><label for="' + theItemId + '_total' + varIdx + '">' + varFileText + '</label></li>');
    //varSelCheckDiv.append('<li class="att_list d-flex flex-row align-items-center justify-content-start gap-1" data-idx="' + varIdx + '"><button type="button" class="d-inline-block bg-white rounded-circle fn_' + theItemId + '_delete" id="' + theItemId + '_total' + varIdx + '"></button><input type="hidden" name="' + theItemId + '_total" value="' + varVal + '"/><label for="' + theItemId + '_total' + varIdx + '" class="text-truncate">' + varFileText + '</label></li>');
    if(theNewSelect) {
		$('#' + theItemId + "_text").val('');
		$('#' + theItemId + "_text").focus();
    }
}

/************************************************
업로드된 파일 checkbox 추가
- theItemId             : 항목ID
- theFileIdx			: 파일idx
- theFileSize			: 파일 크기
- theOriginFileName		: 원본 파일명
- theFileSavedName      : 저장 파일명
- theText               : 대체텍스트
- theNewSelect          : 추가된 파일선택여부
*************************************************/
function fn_addSingleUploadFileList(theItemId, theFileIdx, theFileSize, theFileOriginValue, theFileSavedName, theText, theNewSelect)
{
    // 1. select option 추가
	var varFileSValue = theFileOriginValue;//theFileOriginValue.substring(theFileValue.lastIndexOf('\\') + 1);
	// value - 파일명(경로 포함)(fileList :fileIdx,fileSize,파일원본명,파일저장명,대체텍스트)
	var varVal = theFileIdx + '||' + theFileSize + '||' + varFileSValue + '||' + theFileSavedName + '||' + theText;
	// text - 파일명 : 대체텍스트
	var varFileText = varFileSValue;
    if(theText) varFileText += ' : ' + theText;
    
    var varSelCheckDiv = $('#' + theItemId + '_total_layer')
    var varIdx = varSelCheckDiv.find('button').length + 1;
    varSelCheckDiv.append('<li><input type="checkbox" id="' + theItemId + '_total' + varIdx + '" name="' + theItemId + '_total" value="' + varVal + '"/><label for="' + theItemId + '_total' + varIdx + '">' + varFileText + '</label></li>');
    if(theNewSelect) {
		$('#' + theItemId + "_text").val('');
		$('#' + theItemId + "_text").focus();
    }
}

/************************************************
uploadFile object 추가
*************************************************/
function fn_addUploadFile(theItemId) {
	var varUploadFilesObj = $("#" + gVarUploadFilesObjId + theItemId);

	if($("[id^='fn_multiFile_']").hasClass("fn_multiFile") == false){
		// 기존의 uploadFile object 추가
		var varUploadFileObj = varUploadFilesObj.find("input[type='file']:first");
		var varNewFileObj = $(varUploadFileObj).clone();
		$(varNewFileObj).appendTo($(varUploadFilesObj));
		var varFileObj = varUploadFilesObj.find("input[type='file']:last");
		varFileObj.val('');
		$(varFileObj).show();
		varUploadFilesObj.find("input[type='file']").not(":last").hide();
	} else {
		// label 추가에 따른 기존 소스 변경
		// label을 복사해도 file id가 동일하면 하나의 file만 인식해서 file id에 일련번호 추가
		var varUploadFileObj = varUploadFilesObj.find("input[type^='file']:first");
		var varNewFileObj = $(varUploadFileObj).clone();
		$(varNewFileObj).attr("id", theItemId + fileIndex);
		$(varNewFileObj).appendTo($(varUploadFilesObj));
		var varFileObj = varUploadFilesObj.find("input[type^='file']:last");
		varFileObj.val('');
		$(varFileObj).show();
		varUploadFilesObj.find("input[type='file']").not(":last").hide();
		
		var varUploadFilesLabel = $("#findFile_" + theItemId);
		
		// 첨부파일 찾기 버튼 커스텀에 따라 label 복사 추가 (file만 복사하고 label을 복사 안하면 label이 첫번째 파일만 인식해서 다중 업로드가 안됨)
		var varUploadFileLabel = varUploadFilesLabel.find("label[for^='" + theItemId + "']:first");
		var varNewFileLabel = $(varUploadFileLabel).clone();
		$(varNewFileLabel).attr("for", theItemId + fileIndex);
		$(varNewFileLabel).appendTo($(varUploadFilesLabel));
		var varFileLabel = varUploadFilesLabel.find("label[for^='" + theItemId + "']:last");
		varFileLabel.val('');
		$(varFileLabel).show();
		varUploadFilesLabel.find("label[for^='" + theItemId + "']").not(":last").hide();
		
		fileIndex ++;
	}
	
}



/************************************************
select fileList에서 선택된 option의 업로드 파일목록, fileList 삭제
*************************************************/
function fn_deleteFile(theItemId, theDeletedIdx) {
	$('#' + theItemId + "_text").val('');
    var varFileIdx;
    var varFileValue;

    var varSelCheckDiv = $('#' + theItemId + '_total_layer')
	if (typeof(theDeletedIdx) != 'undefined') {
		// 삭제할 파일 순서를 넘긴 경우
		try {
			var varDelOptionValues = varSelCheckDiv.find('input[type="checkbox"]:eq(' + theDeletedIdx + ')').val().split('||');
			varFileIdx = varDelOptionValues[0]; // 선택된 fileIdx
			varFileValue = varDelOptionValues[3]; // 선택된 파일 원본명
			//if(varFileIdx != 0)	gVarDeleteFiles[theItemId]["k" + varFileIdx] = true;
			if(varFileIdx != 0)	gVarDeleteFiles.get(theItemId).put("k" + varFileIdx, true);
			fn_deleteFileList(theItemId, theDeletedIdx);
		} catch(e){}
		return true;
	} else {
		// 삭제할 파일 목록 선택한 경우
		var varSelectOptions = varSelCheckDiv.find('input[type="checkbox"]:checked');
		varSelectOptions = $(varSelectOptions).get().reverse();
		var varSelected = false;
		$(varSelectOptions).each(function(index){
			var varVal = $(this).val();
			if(!fn_isFill($(this))){
				$(this).prop('selected', false);
				return false;
			}

			var varVals = varVal.split('||');
			varFileIdx = varVals[0]; 	// 선택된 fileIdx
			varFileValue = varVals[3]; // 선택된 파일 원본명
			if (varFileIdx == '0') {
				// 신규 추가된 파일 : input[type='file'] 삭제
				$("#" + gVarUploadFilesObjId + theItemId).find("input[value='" + varFileValue + "']").remove();				
			} else {
				// 기존 등록된 파일
				//gVarDeleteFiles[theItemId]["k" + varFileIdx] = true;
				gVarDeleteFiles.get(theItemId).put("k" + varFileIdx, true);
			}
			varSelected = true;
		});

		if (!varSelected) {
			// title 선택한 경우
			alert(fn_Message.uploadDeleteSelect);
			return false;
		}
		fn_deleteFileList(theItemId);
		return true;
	}
    return false;
}



/************************************************
선택된 fileList 삭제
*************************************************/
function fn_deleteFileList(theItemId, theDeletedIdx){
    var varSelCheckDiv = $('#' + theItemId + '_total_layer')
	if (typeof(theDeletedIdx) != 'undefined') {
		//varSelCheckDiv.find('input[type="checkbox"]:eq(' + theDeletedIdx + ')').remove();	// fileList select option 삭제
		varSelCheckDiv.find('li:eq(' + theDeletedIdx + ')').remove();	// fileList select option 삭제
    } else {
    	varSelCheckDiv.find('input[type="checkbox"]:checked').parent("li").remove();
    }

    $.each($('#' + theItemId + '_total_layer input[type="checkbox"]'), function(i, item){
    	var varId = theItemId + '_total' + (i + 1);
    	$(this).siblings("label").attr("for", varId);
    	$(this).attr("id", varId);
    });
	return true;
}

/************************************************
2024.04.22 추가
업로드한파일 삭제(button 클릭시)
*************************************************/
function fn_deleteFileButton(theItemId, theDeleteItemIdx){
  $('#' + theItemId + "_text").val('');
    var varFileIdx;
    var varFileValue;

    var varSelCheckDiv = $('#' + theItemId + '_total_layer')
  if (typeof(theDeleteItemIdx) != 'undefined') {
		var varSelectOptions = varSelCheckDiv.find('li[data-idx="' + theDeleteItemIdx + '"]').find('input[type="hidden"]');
		var varVal = varSelectOptions.val();

		var varVals = varVal.split('||');
		varFileIdx = varVals[0]; 	// 선택된 fileIdx
		varFileValue = varVals[3]; // 선택된 파일 원본명
		if (varFileIdx == 0) {
			// 신규 추가된 파일 : input[type='file'] 삭제
			$("#" + gVarUploadFilesObjId + theItemId).find("input[id='" + theItemId + theDeleteItemIdx + "']").remove();				
			$("#findFile_" + theItemId).find("label[for='" + theItemId + theDeleteItemIdx + "']").remove();	
			$("#findFile_" + theItemId).find("label[for='" + theItemId + theDeleteItemIdx + "']").last().removeAttr('style');
		} else {
			// 기존 등록된 파일
			//gVarDeleteFiles[theItemId]["k" + varFileIdx] = true;
			gVarDeleteFiles.get(theItemId).put("k" + varFileIdx, true);
		}

    varSelCheckDiv.find('li[data-idx="' + theDeleteItemIdx + '"]').remove();	// fileList select option 삭제

    $.each($('#' + theItemId + '_total_layer button'), function(i, item){
    	var varId = theItemId + '_total' + (i + 1);
    	$(this).siblings("label").attr("for", varId);
    	$(this).attr("id", varId);
    	$(this).parent().attr("data-idx", (i + 1));
    });

	if (varFileIdx == 0) {
		$.each($("#" + gVarUploadFilesObjId + theItemId + " input"), function(i, item){
			var varId = theItemId + i;
			if($(this).attr("id") != theItemId){
		    	$(this).attr("id", varId);
			}
	    });
	
	    $.each($("#findFile_" + theItemId + " label"), function(i, item){
			var varId = theItemId + i;
	    	if($(this).attr("for") != theItemId){
		    	$(this).attr("for", varId);
			}
	    });
	}

	return true;
  }
    return false;
}

/**
 * 대체텍스트 입력
 * @param theFileName
 * @return
 */
function fn_addFileText(theFileName, theText) {
	// 입력할 파일 목록 선택한 경우
	var varTextVal = $('#' + theFileName + "_text").val();
	if(varTextVal && varTextVal.indexOf("||") != -1) {
		//alert('"||"는 입력하실 수 없습니다.');
		alert(fn_Message.impossibleText('||', fn_Message.fileText(theText), true));
		return false;
	}
	
	var varSelectOptions = $('#' + theFileName + '_total_layer input[type="checkbox"]:checked');

	var varSelected = false;
	$(varSelectOptions).each(function(index){
		var varVal = $(this).val();
		if(!varVal) {
			$(this).prop('checked', false);
			return false;
		}
		
		var varVals = $(this).val().split('||');
		varVals[4] = varTextVal;
		varVal = varVals.join('||');
		$(this).val(varVal);
		
		var varText = $(this).siblings("label").html();
		var varTexts = varText.split(':');
		var varTextLen = varTexts.length;
		if(varTextLen > 2) {
			varTexts = $.grep(varTexts, function(item, index){
				return (index < 2);
			});
		}
		varTexts[1] = ' ' + varTextVal;

		varText = varTexts.join(' :');
		$(this).siblings("label").html(varText);
		
		$('#' + theFileName + "_text").val('');
		$('#' + theFileName + "_text").blur();
		$(this).prop('checked', false);
		varSelected = true;
	});
	
	if(!varSelected) {
		alert(fn_Message.uploadTextInputSelect);
		return false;
	}
}
/*
// 대체텍스트수정
function fn_setFileText(theFileName, theObj) {
	var varChecked = theObj.prop("checked")
	var varSelectOptions = $(theObj).find('option:selected');
	var varSelectSize = $(varSelectOptions).length;
	if(varSelectSize > 1) return;
	var varSetVal = '';
	var varVal = $(varSelectOptions).val();
	if(varVal) {
		var varVals = varVal.split('||');
		if(varVals[4]) varSetVal = varVals[4];
	}
	$('#' + theFileName + "_text").val(varSetVal);
}
*/

/************************************************
업로드 가능 여부 체크
- theIdx		: index
*************************************************/
function fn_isUploadExt(theItemId, theFileObj) {
	var varFileValue = $(theFileObj).val();										// 원본파일명
	if(!fn_isValFill(varFileValue)) return true;
	var varExtName = varFileValue.substring(varFileValue.lastIndexOf(".") + 1);	// 원본파일확장자명

	var varReVal;
	
	// 해당 항목 업로드 파일 체크
	var varExtArray = gVarLimitExt[theItemId];
	if(varExtArray){
    	varReVal = fn_isFileExt(varExtName, varExtArray);
    	if(!varReVal) return false;
	}
	
	// 전체 업로드 파일 체크
	var varTotalExtArray = gVarLimitExt['total'];
	varReVal = fn_isFileExt(varExtName, varTotalExtArray);
	return varReVal;
}

/************************************************
파일확장자 체크
*************************************************/
function fn_isFileExt(theExtName, theTotalExtArray){
	var varExtName = theExtName.toLowerCase();
	var varFileFlag = (theTotalExtArray[0] == 1)?true:false;
	var varLen = theTotalExtArray.length;
	for(var varIdx = 1 ; varIdx < varLen ; varIdx ++){
		if(varExtName == theTotalExtArray[varIdx]){
			return varFileFlag;
		}
	}
	
	return !varFileFlag;
}

/************************************************
file upload 순서 변경
*************************************************/
function fn_setFileOrder(theObjId, theFlag){
	var varObj = $('#' + theObjId + '_total_layer');
	fn_moveUDLiChecks(varObj.attr("data-title"), varObj, theFlag, 0);
}



	
function fn_checkFile(theFileObj) {
	var varItemId = $(theFileObj).attr('name');
	// 3. 업로드 가능 - 확장자 검사
	if(!fn_isUploadExt(varItemId, theFileObj))	{
		alert(fn_Message.noUpfileText());

		var varPObj = $(theFileObj).parent();
		var varNewFileObj = $(theFileObj).clone();
		$(varNewFileObj).appendTo($(varPObj));
		var varFileObj = $(varPObj).find("input[type='file']:last");
		varFileObj.val('');
		$(varPObj).find("input[type='file']:first").remove();

		return false;
	}
	return true;
}
/************************************************
single file 삭제
*************************************************/
var varSgDeleteFile = {
	//sgDeleteItemIds : {}, 
	setState : function(theItemId, theBtnObj){
    	//var varDeleteItem = this.sgDeleteItemIds[theItemId];
		//if(!varDeleteItem) {
    	var varDeleteObj = $('#' + theItemId + '_deleted_idxs');
    	if(!fn_isFill(varDeleteObj)) {
		    try {
		    	varDeleteObj.val($('#' + theItemId + '_saved').val());
			    $(theBtnObj).parent().siblings('dt').html(fn_Message.deletedFile + ' : ');
			    $(theBtnObj).html(fn_Message.deleteCancelBtnText);
		    } catch(e){}
		    //this.sgDeleteItemIds[theItemId] = true;
		}else{
		    try {
		    	varDeleteObj.val('');
			    $(theBtnObj).parent().siblings('dt').html(fn_Message.savedFile + ' : ');
			    $(theBtnObj).html(fn_Message.deleteBtnText);
		    } catch(e){}
		    //this.sgDeleteItemIds[theItemId] = false;
		}
	}
}