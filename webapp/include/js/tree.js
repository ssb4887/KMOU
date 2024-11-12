var varTreeInfo = {"minus":{"class_name":"plus", "btn_text":"열기", "child_show":1}, "plus":{"class_name":"minus", "btn_text":"닫기", "child_show":0}};

/**
 * 메뉴트리 열기/닫기 초기화
 * @param theFlag   : 'O':열기, 'C':닫기
 * @param theUlObj  : 1차메뉴의 ul
 */
function fn_initTree(theFlag, theUlObj) {
	var varTotUlObj = theUlObj.parent("li").find("ul");
	$.each(varTotUlObj, function(){
		$(this).parent("li").addClass("minus");
		$(this).find(" > li:last").addClass("last");
	});
	fn_setTreeAllToggle(theFlag, theUlObj);
	
	// 선택된 메뉴트리 상단 열기
	fn_setTreeOpen(theUlObj, theUlObj.find("li.on").filter(":last"));
}

/**
 * 하위메뉴 열기/닫기
 * @param theBtnObj   : 열기/닫기 버튼
 */
function fn_setTreeToggle(theBtnObj) {
	var varLiObj = theBtnObj.parent("li");
	var varLiClassName;
	if(varLiObj.hasClass("minus")) {
		// 열린상태 : 닫기
		varLiClassName = "minus";
	} else if(varLiObj.hasClass("plus")){
		// 닫힌상태 : 열기
		varLiClassName = "plus";
	}
	if(typeof(varLiClassName) != 'undefined') {
		varLiObj.addClass(varTreeInfo[varLiClassName].class_name);
		varLiObj.removeClass(varLiClassName);
		theBtnObj.text(varTreeInfo[varLiClassName].btn_text);
	}
}

/**
 * 상위메뉴 열기
 */
function fn_setParentTreeOpen(theLiObj) {
	var varLiObj = theLiObj;
	var varLiClassName = "plus";
	varLiObj.addClass(varTreeInfo[varLiClassName].class_name);
	varLiObj.removeClass(varLiClassName);
	
	var varBtnObj = varLiObj.find(">.btn_tree");
	if(varBtnObj.size() > 0) varBtnObj.text(varTreeInfo[varLiClassName].btn_text);
}

/**
 * 상위메뉴 ~ 현재메뉴 열기
 * @param theUlObj   : 1차메뉴의 ul
 * @param theLiObj   : 현재메뉴 li
 */
function fn_setTreeOpen(theUlObj, theLiObj) {
	if(theUlObj.size() == 0 || theLiObj.size() == 0) return;
	var varIsRoot = theLiObj.hasClass("root");
	fn_setTreeToggle(theLiObj.find("btn_tree"));
	if(!varIsRoot) {
		// root 아닌 경우
		var varIsPClose = theLiObj.parent("ul").hasClass("fn_skip");
		if(varIsPClose) {
			// 상위 메뉴 닫혀있는 경우
			fn_setParentTreeOpen(theLiObj.parent("ul").parent("li"));
			fn_setTreeOpen(theUlObj, theLiObj.parent("ul").parent("li"));
			/*var varIdx = theUlObj.find("li").index(theLiObj);
			if(varIdx != -1) fn_setTreeAllToggle("O", theUlObj, varIdx);*/
		}
	}
}

/**
 * 메뉴 전체 열기/닫기
 * @param theFlag   : 'O':열기, 'C':닫기
 * @param theUlObj  : 1차메뉴의 ul
 */
function fn_setTreeAllToggle(theFlag, theUlObj, theEndIdx) {
	var varLiObj;// = theUlObj.find("li");//theUlObj.parent("li");
	if(typeof(theEndIdx) != "undefined" && theEndIdx != -1) varLiObj = theUlObj.find("li:lt(" + (theEndIdx + 1) + ")");
	else varLiObj = theUlObj.find("li");
	var varChildObj = varLiObj.find(">ul");
	var varBtnObj = varLiObj.find(".btn_tree");
	var varLiClassName;
	var varSkipClassName = "fn_skip";
	if(theFlag == "O") {
		// 열기
		varLiClassName = "plus";
		varChildObj.removeClass(varSkipClassName);
	} else {
		// 닫기
		varLiClassName = "minus";
		varChildObj.addClass(varSkipClassName);
	}
	
	if(typeof(varLiClassName) != 'undefined') {
		varLiObj.addClass(varTreeInfo[varLiClassName].class_name);
		varLiObj.removeClass(varLiClassName);
		varBtnObj.text(varTreeInfo[varLiClassName].btn_text);
	}
}

function fn_removeAllTreeOn(theUlObj) {
	$(theUlObj).find("li").removeClass("on");
}

/**
 * 상위메뉴 ~ 현재메뉴 메뉴명
 * @param theUlObj   : 1차메뉴의 ul
 * @param theLiObj   : 현재메뉴 li
 */
function fn_getTreePathName(thePath, theUlObj, theLiObj) {
	var varIsRoot = theLiObj.hasClass("root");
	if(varIsRoot || theUlObj.size() == 0 || theLiObj.size() == 0) return thePath;

	theLiObj.addClass("on");
	var varPath = theLiObj.find(">a").text();
	if(thePath != "") varPath = varPath + "&gt;" + thePath;
	
	var varParentPath = fn_getTreePathName(varPath, theUlObj, theLiObj.parent("ul").parent("li"));
	
	return varParentPath;
}


function fn_removeTreeOn(theUlObj, theLiObj) {
	var varIsRoot = theLiObj.hasClass("root");
	if(varIsRoot || theUlObj.size() == 0 || theLiObj.size() == 0) return false;

	theLiObj.removeClass("on");
	var varResult = fn_removeTreeOn(theUlObj, theLiObj.parent("ul").parent("li"));
	
	return true;
}


function fn_setTreeParentClass(theClass, theUlObj, theLiObj) {
	var varIsRoot = theLiObj.hasClass("root");
	if(varIsRoot || theUlObj.size() == 0 || theLiObj.size() == 0) return;

	theLiObj.addClass(theClass);
	
	fn_setTreeParentClass(theClass, theUlObj, theLiObj.parent("ul").parent("li"));
	
}