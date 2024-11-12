var fn_Message = {
	checkItemType:{1:'숫자', 2:'소문자', 3:'대문자', 4:'숫자문자', 5:'한글을 제외한 문자', 11:'통화(원)문자'}, 
	fillAllText: function(theText) {
		return theText + '항목을 모두 입력하세요.';
	}, 
	fillText: function(theText) {
		return theText + '항목을 입력하세요.';
	}, 
	checkText: function(theText) {
		return theText + '항목을 선택하세요.';
	},
	numberText: function(theText) {
		return theText + '항목은 \'0\'으로 시작하지 않는 숫자를 입력 하세요.';
	}, 
	itemTypeText: function (theItemTypeText, theText) {
		return theText + '항목은 ' + theItemTypeText + '만 입력하실 수 있습니다.';
	},
	impossibleBeginText: function (theItemTypeText, theText, theIsText) {
		var varStr;
		if(theIsText) varStr = '\'';
		else varStr = '';
		return theText + '항목은  ' + varStr + theItemTypeText + varStr + '로 시작하는 문자는 입력하실 수 없습니다.';
	},
	impossibleEndText: function (theItemTypeText, theText, theIsText) {
		var varStr;
		if(theIsText) varStr = '\'';
		else varStr = '';
		return theText + '항목은  ' + varStr + theItemTypeText + varStr + '로 끝나는 문자는 입력하실 수 없습니다.';
	},
	impossibleText: function (theItemTypeText, theText, theIsText) {
		var varStr;
		if(theIsText) varStr = '\'';
		else varStr = '';
		return theText + '항목은  ' + varStr + theItemTypeText + varStr + ' 문자는 입력하실 수 없습니다.';
	},
	possibleText: function (theItemTypeText, theText) {
		return theText + '항목은  \'' + theItemTypeText + '\' 문자만 입력하실 수 있습니다.';
	},
	numbertextText: function(theText) {
		return theText + '항목은 숫자를 입력 하세요';
	}, 
	limitLenText: function(theText, theMaxLen) {
		var varText = '';
		if(theText) varText = theText + '항목은 ';
		varText += "총 영문 " + (theMaxLen * 2) + "자 한글 " + theMaxLen + "자 까지만 입력하실 수 있습니다.";
		return varText;
	},
	limitLenText2: function(theText, theEnMaxLen, theKoMaxLen) {
		var varText = '';
		if(theText) varText = theText + '항목은 ';
		varText += "총 영문 " + theEnMaxLen + "자 한글 " + theKoMaxLen + "자 까지만 입력하실 수 있습니다.";
		return varText;
	},
	limitTLenText: function(theText, theMaxLen) {
		var varText = '';
		if(theText) varText = theText + '항목은 ';
		varText += "총  " + theMaxLen + "자 까지만 입력하실 수 있습니다.";
		return varText;
	},
	limitBTLenText: function(theText, theMinLen, theMaxLen) {
		var varText = '';
		if(theText) varText = theText + '항목은 ';
		varText += theMinLen + "자이상 " + theMaxLen + "자 이내로 입력하실 수 있습니다.";
		return varText;
	},
	duplicateText: function(theText) {
		return theText + '중복체크를 하셔야 합니다.';
	}, 
	formatText: function(theText) {
		return theText + '항목이 정확하지 않습니다.';
	}, 
	passwordText: function(theText) {
		return theText + '항목이 일치하지 않습니다.';
	},
	dataSelectText: function(theText) {
		return theText + '할 데이터를 1개 이상 선택하셔야 합니다.';
	},
	nodataText: function(theText) {
		return theText + '할 데이터가 존재하지 않습니다.';
	},
	validNoSpaceText: function(theText) {
		return theText + '는 빈 공간 없이 연속된 영문 소문자와 숫자만 사용할 수 있습니다.';
	},
	validNoFirstCharText: function(theText) {
		return theText + '의 첫문자는 특수문자로 시작할수 없습니다.';
	},
	validNoIDCharText: function(theText, theMinLen, theMaxLen) {
		return theText + '는 ' + theMinLen + '자이상 ' + theMaxLen + '자 이내 영문 소문자와 숫자,특수기호(_)만 사용할 수 있습니다.';
	},
	validNoPWCharText: function(theText, theMinLen, theMaxLen) {
		return theText + '는 ' + theMinLen + '자이상 ' + theMaxLen + '자 이내 영문자,숫자,특수문자를 혼용하셔야 합니다.';
	}, 
	validNoPWCharText2: function(theText) {
		return theText + '는 영문자,숫자,특수문자를 혼용하여야 합니다.';
	}, 
	validNoPWCharText3: function(theText) {
		return theText + '는 같은 문자를 4번 이상 사용하실 수 없습니다.';
	},
	validNoPWCharText4: function(theText, theText2) {
		return theText + '는 ' + theText2 + '를 포함하여 사용하실 수 없습니다.';
	}, 
	validEmailText : '유효하지 않은 이메일입니다.\n확인 후 다시 입력해 주세요.'
	,
	validEmailIdText : function(theText) {
		return theText + '는 이메일로 입력하셔야 합니다.\n확인 후 다시 입력해 주세요.';
	}
	,
	validText : function(theText) {
		return theText + '항목 값이 유효하지 않습니다.\n확인 후 다시 입력해 주세요.';
	},
	confirmText: function(theText) {
		return theText + ' 하시겠습니까?';
	}, 
	noUpfileText: function(theText) {
		var varText = '';
		if(theText) varText = theText + '항목에 ';
		return varText + '업로드 할 수 없는 파일입니다.';
	}, 
	checkFileMaxLimitCountText: function(theText, theMaxCount) {
		return theText + '항목은 ' + theMaxCount + '이하 업로드 하셔야 합니다.';
	},
	checkFileMinMaxLimitCountText: function(theText, theMinCount, theMaxCount) {
		return theText + '항목은 ' + theMinCount + '개 이상 ' + theMaxCount + '이하 업로드 하셔야 합니다.';
	},
	uploadFileLimitCountText : function(theFileLimitCount) {
		return '업로드하실 파일의 갯수는 ' + theFileLimitCount + '개를 넘을 수 없습니다.';
	}, 
	uploadDuplicateText : '파일명이 같은 파일을 여러개 등록 하실 수 없습니다.', 
	uploadDeleteSelect : '삭제할 파일이 올바르게 선택되지 않았습니다.',
	uploadTextInputSelect: '대체 텍스트를 입력할 파일이 올바르게 선택되지 않았습니다.', 
	errorsRSAEncrypt : '오류가 발생했습니다.\n관리자에게 문의하십시오.', 
	deletedFile : '삭제된 파일', 
	savedFile : '저장된 파일', 
	deleteBtnText : '삭제', 
	deleteCancelBtnText : '삭제취소',
	continueConfirmText : '계속 등록하시겠습니까?', 
	popupConfirmText : '새 창을 띄우시겠습니까?',
	newWinConfirmText : '새 창을 띄우시겠습니까?', 
	getPopUrlTitle : function(theStr) {
		return '팝업 ' + theStr;
	}, 
	getPollQuesText : function(theIdx, theTitle){
		return theIdx + ' 질문의  ' + theTitle;
	}, 
	getText : function(theMainTitle, theTitle) {
		if(typeof(theMainTitle) == 'undefined') return theTitle;
		return theMainTitle + ' ' + theTitle;
	},
	duplicateCheck : '중복확인',
	useConfirm : function(theObjName){
		return '사용가능한 ' + theObjName + '입니다. \n사용하시겠습니까?';
	},
	existDuplicate : function(theObjName){
		return '중복된 ' + theObjName + '가 존재합니다.';
	},
	reconfirm : function(theObjName){
		return theObjName + '를 다시 입력하시겠습니까?';
	},
	authenticationComplete : '재입력', 
	verChangeConfirmText: '관리할 버전을 변경하시겠습니까?', 
	menuMoveInMain:'메뉴는 메인 하위메뉴로만 이동하실 수 있습니다.', 
	loginConfirmText:'로그인하셔야 사용하실 수 있습니다. \n로그인하시겠습니까?', 
	versionApplyConfirm: function(theVerIdx) {
		return "현재 적용중인 메뉴가 버전 " + theVerIdx + "의 메뉴로 변경됩니다.\n변경시 서비스에 영향을 미칠 수 있으므로 꼭 확인하시기 바랍니다.\n선택하신 버전을 적용하시겠습니까?";
	}
};