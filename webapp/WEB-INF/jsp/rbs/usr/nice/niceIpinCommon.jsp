<%@page import="com.woowonsoft.egovframework.util.StringUtil"%>
<%@page import="com.woowonsoft.egovframework.prop.RbsProperties"%>
<%
	String sSiteCode = RbsProperties.getProperty("Globals.nice.id");		// NICE로부터 부여받은 사이트 코드
	String sSitePassword = RbsProperties.getProperty("Globals.nice.pwd");	// NICE로부터 부여받은 사이트 패스워드
    String sIPINSiteCode = RbsProperties.getProperty("Globals.ipin.id");	// IPIN 서비스 사이트 코드 (NICE평가정보에서 발급한 사이트코드)
	String sIPINPassword = RbsProperties.getProperty("Globals.ipin.pw");	// IPIN 서비스 사이트 패스워드 (NICE평가정보에서 발급한 사이트패스워드)
%>
<%!
public String requestReplace (String paramValue, String gubun) {
        String result = "";
        
        if (!StringUtil.isEmpty(paramValue)) {
        	
        	paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        	paramValue = paramValue.replaceAll("\\*", "");
        	paramValue = paramValue.replaceAll("\\?", "");
        	paramValue = paramValue.replaceAll("\\[", "");
        	paramValue = paramValue.replaceAll("\\{", "");
        	paramValue = paramValue.replaceAll("\\(", "");
        	paramValue = paramValue.replaceAll("\\)", "");
        	paramValue = paramValue.replaceAll("\\^", "");
        	paramValue = paramValue.replaceAll("\\$", "");
        	paramValue = paramValue.replaceAll("'", "");
        	paramValue = paramValue.replaceAll("@", "");
        	paramValue = paramValue.replaceAll("%", "");
        	paramValue = paramValue.replaceAll(";", "");
        	paramValue = paramValue.replaceAll(":", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll("#", "");
        	paramValue = paramValue.replaceAll("--", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll(",", "");
        	
        	if(!StringUtil.isEquals(gubun, "encodeData")){
        		paramValue = paramValue.replaceAll("\\+", "");
        		paramValue = paramValue.replaceAll("/", "");
				paramValue = paramValue.replaceAll("=", "");
        	}
        	
        	result = paramValue;
            
        }
        return result;
  }
%>