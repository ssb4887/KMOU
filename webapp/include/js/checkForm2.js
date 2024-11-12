var CheckForm = {
	localeLang:"ko"
	, contextPath:""
	, siteId:""
	, require: function(libraryName) {
		document.write('<script type="text/javascript" src="'+libraryName+'"><\/script>');
	}
	, load: function(){
		var varJs = /checkForm2\.js(.*\?.*)?$/;
		var varScripts = $("head script[src*='checkForm2.js']");
		var varSrcQs = varScripts.attr("src").match(varJs);
		var varSrcQLen = $(varSrcQs).size();
		if(varSrcQLen >= 2) {
			var varQ = varSrcQs[1];
			var varContextPaths = varQ.match(/\?(cp=)([/a-zA-Z0-9_]*)(&.*)?/);
	    	var varLocaleLangs = varQ.match(/\?.*[&]?lg=([a-z]*)(&.*)?/);
	    	var varSiteIds = varQ.match(/\?.*[&]?st=([a-zA-Z0-9_]*)(&.*)?/);
	    	if(varContextPaths && varContextPaths.length >= 3)	CheckForm.contextPath = varContextPaths[2];
	    	if(varLocaleLangs && varLocaleLangs.length >= 2 && varLocaleLangs[1])	CheckForm.localeLang = varLocaleLangs[1];
	    	if(varSiteIds && varSiteIds.length >= 2)	CheckForm.siteId = varSiteIds[1];

	    	CheckForm.require(CheckForm.contextPath+"/include/js/checkFormCom.js");
	    	CheckForm.require(CheckForm.contextPath+"/include/js/language/language_" + CheckForm.localeLang + ".js");
	    	CheckForm.require(CheckForm.contextPath+"/include/js/dataMap.js");
	    	CheckForm.require(CheckForm.contextPath+"/include/js/fileCheck.js");
	    	CheckForm.require(CheckForm.contextPath+"/include/js/login.js");
		}
	}
};

CheckForm.load();