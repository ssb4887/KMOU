<%@page import="com.woowonsoft.egovframework.util.MenuUtil"%>
<%@page import="rbs.egovframework.common.CommonFunction"%>
<%@page import="java.util.*"%>
<%@page import="javax.servlet.http.*"%>

<%
	Enumeration<String> attributeNames = request.getAttributeNames();

	while(attributeNames.hasMoreElements()){
		String key = attributeNames.nextElement();
		Object value = request.getAttribute(key);
		
		if(!"mId".equals(key)){
			String encryptedValue = CommonFunction.encSeedEncrypt(value);
			
			request.setAttribute(key, encryptedValue);
		}
	}
%>


