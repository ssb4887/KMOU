/******************************************************************
주소 찾기
*******************************************************************/
function fn_jusoAddrSearch(theItemId, theUrl) {
	fn_setAddrForm.itemZip = theItemId + "1";
	fn_setAddrForm.itemAddr = theItemId + "2";
	var varUrl = (fn_isValFill(theUrl))?theUrl:CheckForm.contextPath + "/" + CheckForm.siteId;
	varUrl += "/zipCode/searchList.do" + location.search;
	fn_dialog.open("", varUrl + "&dl=1", 700, 700);
	/*
	new daum.Postcode({
        oncomplete: function(data) {
        	// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var fullAddr = ''; // 최종 주소 변수
            var extraAddr = ''; // 조합형 주소 변수

            // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                fullAddr = data.roadAddress;

            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                fullAddr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
            if(data.userSelectedType === 'R'){
                //법정동명이 있을 경우 추가한다.
                if(data.bname !== ''){
                    extraAddr += data.bname;
                }
                // 건물명이 있을 경우 추가한다.
                if(data.buildingName !== ''){
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                
                try {
                	
                	if(fn_setOrgAddrForm) {
                    	$('#' + theItemId + '1').val(data.zonecode);
                    	$('#' + theItemId + '2').val(fullAddr);
                    	var varRn = data.roadAddress.replace(data.sido + ' ' + data.sigungu, '');
                    	var varRns = varRn.trim().split(' ');
                    	var varRnLen = varRns.length;
                    	var varRn1 = '';
                    	var varRn2 = '';
                    	if(varRnLen > 0) {
	                    	varRn1 = varRns[0];
	                    	for(var r = 1 ; r < varRnLen ; r ++) {
	                    		varRn2 += varRns[r];
	                    	}
                    	}
                    	fn_setOrgAddrForm('', varRn1, varRn2);
                	} else fn_setAddrForm(data.zonecode, fullAddr);
                } catch(e) {
                	$('#' + theItemId + '1').val(data.zonecode);
                	$('#' + theItemId + '2').val(fullAddr);
                }
            } else {
                
                try {
                	if(fn_setOrgAddrForm) {
                    	$('#' + theItemId + '1').val(data.zonecode);
                    	$('#' + theItemId + '2').val(fullAddr);
                	} else fn_setAddrForm(data.zonecode, fullAddr);
                } catch(e) {
                	$('#' + theItemId + '1').val(data.zonecode);
                	$('#' + theItemId + '2').val(fullAddr);
                }
            	
            }
        }
    }).open();*/
}

var fn_setAddrForm = {
	itemZip:false
	, itemAddr:false
	, set:function(theZip, theAddr){
		$("#" + fn_setAddrForm.itemZip).val(theZip);
		$("#" + fn_setAddrForm.itemAddr).val(theAddr);
	}	
};