<%@page import="java.net.URLEncoder"%>
<%@ include file="../../include/commonTop.jsp"%>
<%@ include file="niceIpinCommon.jsp"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.woowonsoft.egovframework.util.JSONObjectUtil"%>
<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@page language="java" import="Kisinfo.Check.IPINClient" %>
<%
	// nice 휴대폰 인증
	
	NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

    String sRequestNumber = niceCheck.getRequestNO(sSiteCode); 	// 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로 
                                                    			// 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
  	session.setAttribute("REQ_SEQ" , sRequestNumber);			// 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.
  	

	String sAuthType 	= "";	// 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
   	String popgubun		= "N";	//Y : 취소버튼 있음 / N : 취소버튼 없음
	String customize 	= "";	//없으면 기본 웹페이지 / Mobile : 모바일페이지

	JSONObject siteInfo 	= JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
	String siteLocalPath 	= JSONObjectUtil.getString(siteInfo, "local_path");
	
    // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
    String sReturnUrl = request.getScheme() + "://"+ request.getServerName();
    if(request.getServerPort() != 80 && request.getServerPort() != 443) sReturnUrl += ":" + request.getServerPort();
    sReturnUrl += request.getContextPath() + siteLocalPath + "/nice/nice.do";	// 성공시 이동될 URL
	sReturnUrl += "?param_r1=" + URLEncoder.encode(paramR1, "UTF-8");
    if(!StringUtil.isEmpty(paramR2)) sReturnUrl += "&param_r2=" + paramR2;
    // 입력될 plain 데이타를 만든다.
    String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber +
                        "8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                        "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                        "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl +
                        "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                        "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize;
    
    String sMessage = "";
    String sEncData = "";
    
    int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
    if( iReturn == 0 )
    {
        sEncData = niceCheck.getCipherData();
    }
    else if( iReturn == -1)
    {
        sMessage = "암호화 시스템 에러입니다.";
    }    
    else if( iReturn == -2)
    {
        sMessage = "암호화 처리오류입니다.";
    }    
    else if( iReturn == -3)
    {
        sMessage = "암호화 데이터 오류입니다.";
    }    
    else if( iReturn == -9)
    {
        sMessage = "입력 데이터 오류입니다.";
    }    
    else
    {
        sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
    }
    
    request.setAttribute("sMessage", sMessage);

    // nice ipin 인증
	/********************************************************************************************************************************************
	NICE평가정보 Copyright(c) KOREA INFOMATION SERVICE INC. ALL RIGHTS RESERVED
	
	서비스명 : 가상주민번호서비스 (IPIN) 서비스
	페이지명 : 가상주민번호서비스 (IPIN) 호출 페이지
	*********************************************************************************************************************************************/
	
	/*
	┌ sReturnURL 변수에 대한 설명  ─────────────────────────────────────────────────────
		NICE평가정보 팝업에서 인증받은 사용자 정보를 암호화하여 귀사로 리턴합니다.
		따라서 암호화된 결과 데이타를 리턴받으실 URL 정의해 주세요.
		
		* URL 은 http 부터 입력해 주셔야하며, 외부에서도 접속이 유효한 정보여야 합니다.
		* 당사에서 배포해드린 샘플페이지 중, ipin_process.jsp 페이지가 사용자 정보를 리턴받는 예제 페이지입니다.
		
		아래는 URL 예제이며, 귀사의 서비스 도메인과 서버에 업로드 된 샘플페이지 위치에 따라 경로를 설정하시기 바랍니다.
		예 - http://www.test.co.kr/ipin_process.jsp, https://www.test.co.kr/ipin_process.jsp, https://test.co.kr/ipin_process.jsp
	└────────────────────────────────────────────────────────────────────
	*/
	
	String sIPINReturnURL = request.getScheme() + "://"+ request.getServerName();
    if(request.getServerPort() != 80 && request.getServerPort() != 443) sIPINReturnURL += ":" + request.getServerPort();
    sIPINReturnURL += request.getContextPath() + siteLocalPath + "/nice/ipinProcess.do";	// 성공시 이동될 URL
	sIPINReturnURL += "?param_r1=" + URLEncoder.encode(paramR1, "UTF-8");
    if(!StringUtil.isEmpty(paramR2)) sIPINReturnURL += "&param_r2=" + paramR2;

	/*
	┌ sCPRequest 변수에 대한 설명  ─────────────────────────────────────────────────────
		[CP 요청번호]로 귀사에서 데이타를 임의로 정의하거나, 당사에서 배포된 모듈로 데이타를 생성할 수 있습니다.
		
		CP 요청번호는 인증 완료 후, 암호화된 결과 데이타에 함께 제공되며
		데이타 위변조 방지 및 특정 사용자가 요청한 것임을 확인하기 위한 목적으로 이용하실 수 있습니다.
		
		따라서 귀사의 프로세스에 응용하여 이용할 수 있는 데이타이기에, 필수값은 아닙니다.
	└────────────────────────────────────────────────────────────────────
	*/

	String sCPRequest				= "";

	// 객체 생성
	IPINClient pClient = new IPINClient();

	// 앞서 설명드린 바와같이, CP 요청번호는 배포된 모듈을 통해 아래와 같이 생성할 수 있습니다.
	sCPRequest = pClient.getRequestNO(sIPINSiteCode);

	// CP 요청번호를 세션에 저장합니다.
	// 현재 예제로 저장한 세션은 ipin_result.jsp 페이지에서 데이타 위변조 방지를 위해 확인하기 위함입니다.
	// 필수사항은 아니며, 보안을 위한 권고사항입니다.
	session.setAttribute("CPREQUEST" , sCPRequest);

	// Method 결과값(iRtn)에 따라, 프로세스 진행여부를 파악합니다.
	int iRtn = pClient.fnRequest(sIPINSiteCode, sIPINPassword, sCPRequest, sIPINReturnURL);

	String sRtnMsg = "";		// 처리결과 메세지
	String sIPINEncData = "";	// 암호화 된 데이타
	
	// Method 결과값에 따른 처리사항
	if (iRtn == 0)
	{
	
		// fnRequest 함수 처리시 업체정보를 암호화한 데이터를 추출합니다.
		// 추출된 암호화된 데이타는 당사 팝업 요청시, 함께 보내주셔야 합니다.
		sIPINEncData = pClient.getCipherData();		//암호화 된 데이타
		
	}
	else if (iRtn == -1 || iRtn == -2)
	{
		sRtnMsg =	"배포해 드린 서비스 모듈 중, 귀사 서버환경에 맞는 모듈을 이용해 주시기 바랍니다.<BR>" +
					"귀사 서버환경에 맞는 모듈이 없다면 ..<BR><B>iRtn 값, 서버 환경정보를 정확히 확인하여 메일로 요청해 주시기 바랍니다.</B>";
	}
	else if (iRtn == -9)
	{
		sRtnMsg = "입력값 오류 : fnRequest 함수 처리시, 필요한 4개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.";
	}
	else
	{
		sRtnMsg = "iRtn 값 확인 후, NICE평가정보 개발 담당자에게 문의해 주세요.";
	}

    request.setAttribute("sRtnMsg", sRtnMsg);
%>
<script type="text/javascript">
<%--
<c:if test="${!empty sMessage}">
	alert('${sMessage}');
</c:if>
<c:if test="${!empty sRtnMsg}">
	alert('${sRtnMsg}');
</c:if>
--%>

window.name ="Parent_window";

function fn_checkplusPopup(){
	window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
	document.form_chk.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
	document.form_chk.target = "popupChk";
	document.form_chk.submit();
}

function fn_ipinPopup(){
	window.open('', 'popupIPIN2', 'width=450, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
	document.form_ipin.target = "popupIPIN2";
	document.form_ipin.action = "https://cert.vno.co.kr/ipin.cb";
	document.form_ipin.submit();
}
</script>
