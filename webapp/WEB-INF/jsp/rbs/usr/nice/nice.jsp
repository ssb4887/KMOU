<%@page import="com.woowonsoft.egovframework.util.ModuleUtil"%>
<%@page import="com.woowonsoft.egovframework.util.UserDetailsHelper"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.woowonsoft.egovframework.util.JSONObjectUtil"%>
<%@page import="rbs.egovframework.LoginVO"%>
<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@ include file="../../include/commonTop.jsp"%>
<%@ include file="niceIpinCommon.jsp"%>
<%
    NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

    String sEncodeData = requestReplace(request.getParameter("EncodeData"), "encodeData");
    String sReserved1  = StringUtil.getString(StringUtil.replaceScript(request.getParameter("param_r1")), "");
    String sReserved2  = StringUtil.getString(StringUtil.replaceScript(request.getParameter("param_r2")), "");
    String sReserved3  = StringUtil.getString(StringUtil.replaceScript(request.getParameter("param_r3")), "");


    String sCipherTime = "";			// 복호화한 시간
    String sRequestNumber = "";			// 요청 번호
    String sResponseNumber = "";		// 인증 고유번호
    String sAuthType = "";				// 인증 수단
    String sName = "";					// 성명
    String sDupInfo = "";				// 중복가입 확인값 (DI_64 byte)
    String sConnInfo = "";				// 연계정보 확인값 (CI_88 byte)
    String sBirthDate = "";				// 생일
    String sGender = "";				// 성별
    String sNationalInfo = "";       	// 내/외국인정보 (개발가이드 참조)
    String sMessage = "";
    String sPlainData = "";
    
    int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

    if( iReturn == 0 )
    {
        sPlainData = niceCheck.getPlainData();
        sCipherTime = niceCheck.getCipherDateTime();
        
        // 데이타를 추출합니다.
        java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
        
        sRequestNumber  = (String)mapresult.get("REQ_SEQ");			// CP 요청번호
        sResponseNumber = (String)mapresult.get("RES_SEQ");			// 처리결과 고유번호 (NICE에서 부여)
        sAuthType 		= (String)mapresult.get("AUTH_TYPE");		// 인증수단 (M :휴대폰, C : 신용카드, X : 공인인증서)
        sName 			= (String)mapresult.get("NAME");			// 이름
       	sBirthDate 		= (String)mapresult.get("BIRTHDATE");		// 생년월일(YYYYMMDD)
        sGender 		= (String)mapresult.get("GENDER");			// 성별코드 (0 : 여성, 1 : 남성)
        sNationalInfo  	= (String)mapresult.get("NATIONALINFO");	// 국적정보 (0 : 내국인, 1: 외국인)
        sDupInfo 		= (String)mapresult.get("DI");				// 개인회원의 중복가입여부 확인을 위해 사용되는 값(크기 : 64byte), NICE로부터 부여받은 사이트 코드마다 다른 값을 가진다.
        sConnInfo 		= (String)mapresult.get("CI");				// 주민등록번호와 1:1로 매칭되는 고유키(크기 : 88byte), NICE로부터 부여받은 사이트 코드에 관계없이 같은 값을 가진다. 
        
        /*
        System.out.println("sRequestNumber : " + sRequestNumber);
        System.out.println("sResponseNumber : " + sResponseNumber);
        System.out.println("sAuthType : " + sAuthType);
        System.out.println("sName : " + sName);
        System.out.println("sBirthDate : " + sBirthDate);
        System.out.println("sGender : " + sGender);						
        System.out.println("sNationalInfo : " + sNationalInfo);
        System.out.println("sDupInfo : " + sDupInfo);
        System.out.println("sConnInfo : " + sConnInfo);
        */
        
        String session_sRequestNumber = (String)session.getAttribute("REQ_SEQ");
        if(!sRequestNumber.equals(session_sRequestNumber))
        {
            sMessage = "세션값이 다릅니다. 올바른 경로로 접근하시기 바랍니다.";
            sResponseNumber = "";
            sAuthType = "";
        }
        
        session.setAttribute("sAuthType", sAuthType);
        session.setAttribute("sName", sName);
        session.setAttribute("sBirthDate", sBirthDate);
        session.setAttribute("sGender", sGender);
        session.setAttribute("sNationalInfo", sNationalInfo);
        session.setAttribute("sDupInfo", sDupInfo);
        session.setAttribute("sConnInfo", sConnInfo);	// CI값을 받으려면 요청을 해야한다
        
        if(StringUtil.isEquals(sReserved2, "nmA")){
        	LoginVO loginVo = new LoginVO();
        	loginVo.setMemberName(ModuleUtil.getMemberItemValue("mbrName", sName));
        	loginVo.setMemberNameOrg(sName);
        	loginVo.setUsertypeIdx(StringUtil.getInt(RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER")));

    		session.setAttribute("loginVO", loginVo);
        }
        
        sMessage = "<script type=\"text/javascript\">opener.location.href='" + sReserved1 + "'; self.close();</script>";
    }
    else if( iReturn == -1)
    {
        sMessage = "복호화 시스템 에러입니다.";
    }    
    else if( iReturn == -4)
    {
        sMessage = "복호화 처리오류입니다.";
    }    
    else if( iReturn == -5)
    {
        sMessage = "복호화 해쉬 오류입니다.";
    }    
    else if( iReturn == -6)
    {
        sMessage = "복호화 데이터 오류입니다.";
    }    
    else if( iReturn == -9)
    {
        sMessage = "입력 데이터 오류입니다.";
    }    
    else if( iReturn == -12)
    {
        sMessage = "사이트 패스워드 오류입니다.";
    }    
    else
    {
        sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
    }

%>
<%=sMessage%>