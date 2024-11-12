<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
$(function(){
	$("#<c:out value="${param.searchFormId}"/>").submit(function(){return fnzipcodeSearchFormSubmit();});

	$('#resultData .fn_btn_addr_submit').click(function(){fn_zipcodeSubmitForm(this);});
});
function fnzipcodeSearchFormSubmit(){
	var varObj = $('#key');
	if(!fn_isFill(varObj)) {
		alert(varObj.attr('title') + '를 입력하셔야 합니다.');
		return false;
	}
	var varKeyVal = varObj.val();
	
	if(varKeyVal.length >0){
		//특수문자 제거
		var expText = /[%=><]/ ;
		if(expText.test(varKeyVal) == true){
			alert("특수문자를 입력 할수 없습니다.") ;
			varObj.val(varKeyVal.split(expText).join("")); 
			return false;
		}
		
		//특정문자열(sql예약어의 앞뒤공백포함) 제거
		var sqlArray = new Array(
			//sql 예약어
			"OR", "SELECT", "INSERT", "DELETE", "UPDATE", "CREATE", "DROP", "EXEC",
             		 "UNION",  "FETCH", "DECLARE", "TRUNCATE" 
		);
		
		var regex ;
		var regex_plus ;
		for(var i=0; i<sqlArray.length; i++){
			regex = new RegExp("\\s" + sqlArray[i] + "\\s","gi") ;
			if (regex.test(varKeyVal)) {
			    alert("\"" + sqlArray[i]+"\"와(과) 같은 특정문자로 검색할 수 없습니다.");
			    varObj.val(varKeyVal.replace(regex, ""));
				return false;
			}
			
			regex_plus = new RegExp( "\\+" + sqlArray[i] + "\\+","gi") ;
			if (regex_plus.test(varKeyVal)) {
			    alert("\"" + sqlArray[i]+"\"와(과) 같은 특정문자로 검색할 수 없습니다.");
			    varObj.val(varKeyVal.replace(regex_plus, ""));
				return false;
			}
		}
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