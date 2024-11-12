var _d = document;

/* tabOn */
function tabOn(containerId,a){
	var tabContainer=document.getElementById(containerId);
	var tabid=(tabContainer)?tabContainer.id:"tab"+containerId;//이전코드호환
	var tabTagAll=document.getElementById(tabid).getElementsByTagName("*");
	var tabSum=0;//탭수
	for(var i=0;i<tabTagAll.length;i++){var where=tabTagAll[i].id.indexOf(containerId+"m");if(where!=-1)tabSum++;}
	for(var i=1;i<=tabSum;i++){//탭수만큼루프
		if(i<10){inn="0"+i;}else{inn=""+i;}
		tabMenu=document.getElementById(tabid+"m"+i);
		tabContent=document.getElementById(tabid+"c"+i);
		if(tabMenu){
			if(tabMenu.tagName=="IMG"){
				_imgtype=tabMenu.src.substr(tabMenu.src.length-3,tabMenu.src.length-1);
				tabMenu.src=tabMenu.src.replace("on."+_imgtype, "."+_imgtype);
				tabMenu.parentNode.className="";
			}
			if(tabMenu.tagName=="A"){tabMenu.className="";}
		}
		if(tabContent){tabContent.style.display="none";}
	}
	if(a<10){ann="0"+a;}else{ann=""+a;}
	tabMenu=document.getElementById(tabid+"m"+a);
	tabContent=document.getElementById(tabid+"c"+a);
	if(tabMenu){
		if(tabMenu.tagName=="IMG"){
			_imgtype=tabMenu.src.substr(tabMenu.src.length-3,tabMenu.src.length-1);
			tabMenu.src=tabMenu.src.replace("."+_imgtype, "on."+_imgtype);
			tabMenu.parentNode.className="on";
		}
		if(tabMenu.tagName=="A"){tabMenu.className="on";}
	}
	if(tabContent){tabContent.style.display="block";}
	tabMore=document.getElementById(tabid+"more");
}



// for onload event
function addEvent(elm, evType, fn, useCapture) {
    if (elm.addEventListener) {
        elm.addEventListener(evType, fn, useCapture);
        return true;
    }
    else if (elm.attachEvent) {
        var r = elm.attachEvent('on' + evType, fn);
        return r;
    }
    else {
        elm['on' + evType] = fn;
    }
}

// add class
function addClass(element,value) {
	try
	{
		if (!element.className) {
			element.className = value;
		} else {
			newClassName = element.className;
			newClassName += " ";
			newClassName += value;
			element.className = newClassName;
		}
	} catch(e) {}
}

// remove class
function removeClass(element,value) {
	if (element.className == value) {
		element.className = "";
	} else if (element.className.indexOf(value) != -1) {
		element.className = element.className.replace(value,"");
	}
}

// get Class
function getClass(classname, tagname, tarID) {
	if (tarID == undefined) tarID = document;
	var element = this.nodeType == 1 ? this : tarID;
	var elements = [], nodes = tagname ? element.getElementsByTagName(tagname) : element.getElementsByTagName('*');	
	for(var i=0; i<nodes.length; i++) {
		var elementClassName = nodes[i].className;
		if (elementClassName.length > 0 && (elementClassName == arguments[0] || new RegExp("(^|\\s)" + arguments[0] + "(\\s|$)").test(elementClassName))) {
			elements.push(nodes[i]);
		}
	}
	return elements;
}

// input value
function inputValue(classname) {
	inputEl = getClass(classname, 'input');
	for (var i=0; i<inputEl.length; i++ ) {
		if(this.value == true) removeClass(this,classname);
		inputEl[i].onfocus = function() {
			removeClass(this,classname);
		}
		inputEl[i].onblur = function() {
			if(this.value == false) {
				addClass(this,classname);
			} else {
				return false;
			}
		}
	}
}

// tab menu
function tabMenu(id,currentNum) {
	var objAnchor = [], objLink = [];
	var ID = document.getElementById(id);
	var objLI = ID.getElementsByTagName('li');

	var initialize = function() {
		for (var i=0; i<objLI.length; i++) {
			objLink[i] = objLI[i].getElementsByTagName('a')[0];
			objAnchor[i] = document.getElementById(objLink[i].getAttribute('href').split('#')[1]);

			if (i != 0)
				objAnchor[i].className += ' hidden';
			else
				objLink[i].parentNode.className += ' visible';

			objEvent(i);
		}
		if (currentNum) objLink[currentNum-1].onclick();
	};

	var objEvent = function(num) {
		objLink[num].onclick = function() {
			for (var i=0; i<objLI.length; i++) {
				var imgEl = objLink[i].getElementsByTagName('img')[0];
				if (i == num) {
					if (imgEl){
						if(imgEl.src.indexOf('_on') == -1)
							imgEl.src = imgEl.src.replace('.gif', '_on.gif');
						else
							imgEl.src = imgEl.src;
					}
					if (objLink[i].parentNode.className.indexOf('visible') == -1)
						objLink[i].parentNode.className += ' visible';
					objAnchor[i].className = objAnchor[i].className.replace('hidden', '');
				} else {
					if (imgEl) imgEl.src = imgEl.src.replace('_on.gif', '.gif');
					objLink[i].parentNode.className = objLink[i].parentNode.className.replace('visible', '');
					if (objAnchor[i].className.indexOf('hidden') == -1)
						objAnchor[i].className += ' hidden';
				}
			}
			return false;
		}
	};

	initialize();
}

// tab menu
function tabMenu2(id,currentNum) {

	$('#tab_menu li>a').click(function() {
		$(this).find('img').attr('src',function(){return this.src.replace("_off.gif","_on.gif")});
		$(this).parent('li').siblings().find('img').attr('src',function(){return this.src.replace("_on.gif","_off.gif")});
	});
}
/**********************************
 * 占쏙옙占폻占쏙옙 占쏢세븝옙占쏙옙 占쏙옙
 * @param id
 * @param currentNum
 * @return
 **********************************/
function tabMenuEnt(id,currentNum) {
	var ID = document.getElementById(id);
	var objLI = ID.getElementsByTagName('li');

		for (var i=0; i<objLI.length; i++) {
			var imgEl = document.getElementById('tab_img0' + (i + 1));
			if (i + 1 != currentNum)
			{
				document.getElementById('ci0' + (i + 1)).style.display = 'none';
				if (imgEl) imgEl.src = imgEl.src.replace('_on.gif', '.gif');
			}
			else
			{
				if (imgEl) imgEl.src = imgEl.src.replace('.gif', '_on.gif');
				document.getElementById('ci0' + (i + 1)).style.display = 'inline';
			}

		}
}

// GNB
var gnbNavi = function(gnbID,noscript,currentNum){
	var wrapper = _d.getElementById(gnbID);
	if (noscript) removeClass(wrapper,noscript);	// 占쏙옙크占쏙옙트占쏙옙 占싸듸옙퓔占?wrapper占쏙옙 class占쏙옙 'noscript'占쏙옙 占쏙옙f占쌌니댐옙.

	var menu = getClass('depth1','li',wrapper);
	var menuLink = [];			// 占싱븝옙트占쏙옙 占쌩삼옙占신?占쏙옙濱占쏙옙占?a占쏙옙
	var submenu = [];			// 占쏙옙濱占쏙옙占?占쏙옙'占쏙옙 '치占쏙옙 ul占쏙옙
	var submenuLink = [];		// 占쏙옙占쏙옙濱占쏙옙占?a占쏙옙

	var initialize = function(){
		for (var i=0; i<menu.length; i++){
			menuLink[i] = menu[i].getElementsByTagName('a')[0];
			submenu[i] = menu[i].getElementsByTagName('div')[0];	// 占쏙옙占쏙옙濱占쏙옙占?div
			if(submenu[i] == undefined){
				submenu[i] = null;
			}
			if(submenu[i]) submenu[i].style.visibility = 'hidden';	// 占쏙옙占쏙옙濱占?'hidden'占십깍옙화

			showCurrentmenu(i);
		}
		if (currentNum) menuLink[currentNum-1].onmouseover();		// 占쏙옙占쏙옙 占쌨댐옙 활占쏙옙화
	};
	var showCurrentmenu = function(num){
		menuLink[num].onmouseover = menuLink[num].onfocus = function(){
			for(var i=0; i<menu.length; i++){
				var imgEl = menuLink[i].getElementsByTagName('img')[0];
				if(i == num){
					if (menu[i].className.indexOf('visible') == -1) addClass(menu[i],'visible');		// 활占쏙옙화 占쏙옙 占쌨댐옙占쏙옙 class占쌩곤옙
					if (imgEl && imgEl.src.indexOf('_on.gif') == -1) imgEl.src = imgEl.src.replace('.gif', '_on.gif');	// 占쏙옙濱占?占싱뱄옙占쏙옙 占?占?
					if(submenu[i]) {					// 占쏙옙占쏙옙濱占쏙옙占?占쏙옙;占쏙옙占쏙옙 占쏙옙占쏙옙占싼댐옙.
						submenu[i].style.visibility = 'visible';
						subImgOver(i);					// 占쏙옙占쏙옙濱占쏙옙占?占싱뱄옙占쏙옙 占?占?
					}
				} else {
					removeClass(menu[i],'visible');
					if (imgEl) imgEl.src = imgEl.src.replace('_on.gif', '.gif');
					if(submenu[i]) {					// 占쏙옙占쏙옙濱占쏙옙占?占쏙옙;占쏙옙占쏙옙 占쏙옙占쏙옙占싼댐옙.
						submenu[i].style.visibility = 'hidden';
					}
				}
			}
		}
		/* 占쏙옙占쎌스 占싣울옙占?占쏙옙占쏙옙 활占쏙옙화占쏙옙 占쏙옙占쏙옙濱占쏙옙占?占쌘듸옙 占쏙옙占쏙옙
		menuLink[num].onmouseout = function(){
			if(currentNum) {
				setTimeout(function(){if (currentNum) menuLink[currentNum-1].onmouseover();},1000);
			}
		}
		*/
	};
	var subImgOver = function(num){
		submenuLink = submenu[num].getElementsByTagName('a');
		for(var i=0; i<submenuLink.length; i++){
			var imgEl = submenuLink[i].getElementsByTagName('img');
			for(var j=0; j<imgEl.length; j++){
				imgEl[j].onmouseover = imgEl[j].onfocus = function(){
					this.src = this.src.replace('.gif','_on.gif');
				}
				imgEl[j].onmouseout = imgEl[j].onblur = function(){
					this.src = this.src.replace('_on.gif','.gif');
				}
			}
		}
	}
	initialize();
}

// NO script case
function scriptOn(element){
	var tarEl = _d.getElementById(element);
	// 占쏙옙크占쏙옙트占쏙옙 占쏙옙x占싹댐옙 占쏙옙占쏙옙占쏙옙트占쏙옙 class占쏙옙 占싸울옙占싹울옙 display占쏙옙 활占쏙옙화 占쏙옙킨占쏙옙(css).
	addClass(tarEl,'script_on');
}
function scriptOnClass(classname){
	var tarEl = getClass(classname);
	for (var i=0; i<tarEl.length; i++){
		addClass(tarEl[i],'script_on');
	}
}

// Rolling
/* css class占쏙옙占쏙옙占쏙옙
--------------------------------*/
var getElementsClass = function() { //className, tagName, parentNode
	var element = arguments[2] || document;
	var elements = [], nodes = arguments[1] ? element.getElementsByTagName(arguments[1]) : element.getElementsByTagName('*');

	for(var i=0; i<nodes.length; i++) {
		if(nodes[i].className.indexOf(arguments[0]) != -1)
			elements.push(nodes[i]);
	}
	return elements;
};

/* element占쏙옙 '치, 占쏙옙占쏙옙占쏙옙鱇틂占쏙옙占?
--------------------------------*/
var getOffset = function () {
	var objRoot = null;
	var currentObj = arguments[0];
	var bLoop = !!arguments[1];
	var data = {left:0, top:0, width:0, height:0}

	data.width = currentObj.offsetWidth;
	data.height = currentObj.offsetHeight;

	switch (bLoop) {
	case true:
		while (!!currentObj && currentObj.nodeName.toLowerCase() != 'body') {
			data.top += currentObj.offsetTop;
			data.left += currentObj.offsetLeft;
			currentObj = currentObj.offsetParent;
		}
		break;
	case false:
		data.top = currentObj.offsetTop;
		data.left = currentObj.offsetLeft;
		break;
	}

	return data;
};



function rollingContent(obj, list, elementNode, autoScroll, scrollTime, scrollType, wrapSize, cntSize) {
	var _d = document,
		objId = _d.getElementById(obj),
		currentBox = _d.getElementById(list),
		objWrap,
		objThis = this,
		boxSize = 0,
		currentNum = 0,
		currentNumber = 0,
		elements,
		oldNum = 0;

	var setTimes = scrollTime * 1000,
		scroll = {time:1, start:0, change:0, duration:25, timer:null},
		originaltime = scroll.time,
		goodsSetTime = null,
		scrollDirection = 'direction',
		autoScrolling = autoScroll;

	var initialize = function() {
		elements = getElementsClass(elementNode, '', currentBox);
		var elementsLength = elements.length;

		objWrap = _d.createElement('div');
		if (objId.childNodes[1])
			objId.insertBefore(objWrap, objId.childNodes[1]);
		else
			objId.appendChild(objWrap);
		currentBox = objId.removeChild(currentBox);
		objWrap.appendChild(currentBox);

		with (objWrap.style) {
			position = 'relative';
			overflow = 'hidden';
			width = wrapSize.width + 'px';
			height = wrapSize.height + 'px';
		}

		for (var i=0; i<elementsLength; i++) {
			with (elements[i].style) {
				display = 'block';
				if (scrollType == 'horizontal') {
					if (_d.all) styleFloat = 'left';
					else cssFloat = 'left';
				}
			}
			if (cntSize == 'auto') {
				if (scrollType == 'horizontal') boxSize += getOffset(elements[i]).width;
				else boxSize += getOffset(elements[i]).height;
			} else {
				if (scrollType == 'horizontal') boxSize += parseInt(cntSize.width);
				else boxSize += parseInt(cntSize.height);
			}
		}

		with (currentBox.style) {
			position = 'relative';
			overflow = 'hidden';
			if (scrollType == 'horizontal') width = boxSize + 'px';
			else height = boxSize + 'px';
		}

		if (autoScrolling == 'none') {
			//do noting
		} else {
			if (scrollDirection == 'direction')
				goodsSetTime = setInterval(objThis.next, setTimes);
			else
				goodsSetTime = setInterval(objThis.prev, setTimes);
		}
	};

	var actionEvent = function(objNum) {
		clearInterval(goodsSetTime);

		elements = getElementsClass(elementNode, '', currentBox);
		var elementsLength = elements.length;

		if (scrollType == 'horizontal')
			startScroll(objWrap.scrollLeft, parseInt(wrapSize.width * objNum));
		else
			startScroll(objWrap.scrollTop, parseInt(wrapSize.height * objNum));

		oldNum = objNum;
		return false;
	};

	this.next = function() {
		elements = getElementsClass(elementNode, '', currentBox);
		var elementsLength = elements.length;

		if (currentNumber == elementsLength - 1) {
			var lastNode = currentBox.removeChild(elements[0]);
			currentBox.appendChild(lastNode);

			switch (scrollType) {
				case 'vertical':
					objWrap.scrollTop -= wrapSize.height;
					break;
				default:
					objWrap.scrollLeft -= wrapSize.width;
					break;
			}
			currentNumber--;
		}

		var position = getActionPoint('direct');
		startScroll(position.start, position.end);

		currentNumber = currentNumber + 1;
		if (currentNumber < elementsLength - 1)
			currentNumber = elementsLength - 1;

		scrollDirection = 'direction';
		if (autoScrolling == 'none') {
			//do noting
		} else {
			clearInterval(goodsSetTime);
			goodsSetTime = setInterval(objThis.next, setTimes);
		}

		return false;
	};

	this.prev = function() {
		elements = getElementsClass(elementNode, '', currentBox);
		var elementsLength = elements.length;

		if (currentNumber == 0) {
			var firstNode = currentBox.removeChild(elements[elementsLength - 1]);
			currentBox.insertBefore(firstNode, elements[0]);

			switch (scrollType) {
				case 'vertical':
					objWrap.scrollTop += wrapSize.height;
					break;
				default:
					objWrap.scrollLeft += wrapSize.width;
					break;
			}
			currentNumber++;
		}

		var position = getActionPoint('indirect');
		startScroll(position.start, position.end);

		currentNumber = currentNumber - 1;
		if (currentNumber > 0)
			currentNumber = 0;

		scrollDirection = 'indirection';
		if (autoScrolling == 'none') {
			//do noting
		} else {
			clearInterval(goodsSetTime);
			goodsSetTime = setInterval(objThis.prev, setTimes);
		}

		return false;
	};

	this.stop = function() {
		clearInterval(goodsSetTime);
	};

	this.play = function() {
		autoScrolling = 'auto';
		clearInterval(goodsSetTime);
		if (scrollDirection == 'direction')
			goodsSetTime = setInterval(objThis.next, setTimes);
		else
			goodsSetTime = setInterval(objThis.prev, setTimes);
	};

	var startScroll = function (start, end) {
		if (scroll.timer != null) {
			clearInterval(scroll.timer);
			scroll.timer = null;
		}

		scroll.start = start;
		scroll.change = end - start;

		scroll.timer = setInterval(scrollAction, 15);
	};

	var scrollAction = function () {
		if (scroll.time > scroll.duration) {
			clearInterval(scroll.timer);
			scroll.time = originaltime;
			scroll.timer = null;
		} else {
			switch (scrollType) {
				case 'vertical':
					objWrap.scrollTop = sineInOut(scroll.time, scroll.start, scroll.change, scroll.duration);
					break;
				default:
					objWrap.scrollLeft = sineInOut(scroll.time, scroll.start, scroll.change, scroll.duration);
					break;
			}
			scroll.time++;
		}
	};

	var sineInOut = function (t, b, c, d) {
		return -c/2 * (Math.cos(Math.PI*t/d) - 1) + b;
	};

	var getActionPoint = function (dir) {
		elements = getElementsClass(elementNode, '', currentBox);
		var startDirectPosition;
		var startInDirectPosition;
		if (dir == 'direct') {
			var position = findElementPos(elements[currentNumber + 1]); // target image.
			var offsetPos = findElementPos(elements[currentNumber]); // first image.
			startPosition = 0;
			startInDirectPosition;
		} else {
			var position = findElementPos(elements[currentNumber - 1]); // target image.
			var offsetPos = findElementPos(elements[currentNumber]); // first image.
			startPosition = elements[currentNumber - 1].offsetWidth;
			startInDirectPosition = elements[currentNumber - 1].offsetHeight;
		}

		switch (scrollType) {
			case 'vertical':
				var start = objWrap.scrollTop;
				var end = position[1] - offsetPos[1];
				break;
			default:
				var start =  startPosition;
				var end = position[0] - offsetPos[0];
				break;
		}

		position.start = start;
		position.end = end;

		return position;
	};

	var findElementPos = function (elemFind) {
		var elemX = 0;
		var elemY = 0;
		try {
			do {
				elemX += elemFind.offsetLeft;
				elemY += elemFind.offsetTop;
			} while (elemFind = elemFind.offsetParent)
		} catch(e) {}

		return Array(elemX, elemY);
	};

	initialize();
}



// onload event
addEvent(window,'load',function () {
//	newWindow("external");
},false);

//?쇱떆二쇱꽍
// Dom load event
/*addDOMLoadEvent(function() {
	inputValue('comment_value');
	scriptOn('util_func');
	scriptOnClass('ctrl_btn');
});*/

// 占쏙옙占쏙옙濱占?

function slide(Id, interval, to) {
	var obj = document.getElementById(Id);
	var H, step = 5;
	if (obj == null) return;
	if (to == undefined) { // user clicking
		if (obj._slideStart == true) return;
		if (obj._expand == true) {
			to = 0;
			obj.style.overflow = "hidden";
		} else {
			slide.addId(Id);
			for(var i=0; i < slide.objects.length; i++) {
				if (slide.objects[i].id != Id && slide.objects[i]._expand == true) {
					slide(slide.objects[i].id);
				}
			}

			obj.style.height = "";
			obj.style.overflow = "";
			obj.style.display = "block";
			to = obj.offsetHeight; // 占싱곤옙占싱곤옙
			obj.style.overflow = "hidden";
			obj.style.height = "1px";
		}
		obj._slideStart = true;
	}
	
	step             = ((to > 0) ? 1:-1) * step;
	interval         = ((interval==undefined)?1:interval);

	obj.style.height = (H=((H=(isNaN(H=parseInt(obj.style.height))?0:H))+step<0)?0:H+step)+"px";
	
	
	if (H <= 0) {
		obj.style.display = "none";
		obj.style.overflow = "hidden";
		obj._expand = false;
		obj._slideStart = false;
	} else if (to > 0 && H >= to) {
		obj.style.display = "block";
		obj.style.overflow = "visible";
		obj.style.height = H + "px";
		obj._expand = true;
		obj._slideStart = false;
	} else {
		setTimeout("slide('"+Id+"' , "+interval+", "+to+");", interval);
	}
}
slide.objects = new Array();
slide.addId = function(Id)
{
	for (var i=0; i < slide.objects.length; i++) {
		if (slide.objects[i].id == Id) return true;
	}
	slide.objects[slide.objects.length] = document.getElementById(Id);
}

// swap image
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}