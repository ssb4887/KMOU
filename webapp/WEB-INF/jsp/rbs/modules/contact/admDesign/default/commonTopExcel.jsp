<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.woowonsoft.egovframework.resource.RbsMessageSource"%>
<%@page import="com.woowonsoft.egovframework.form.DataForm"%>
<%
String gubunItemName = null;
String titleGubunName = null;
DataForm queryString = (request.getAttribute("queryString") == null)?new DataForm():(DataForm)request.getAttribute("queryString");
ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
RbsMessageSource rbsMessageSource = (RbsMessageSource)act.getBean("rbsMessageSource");
int statsType = queryString.getInt("is_statsType");
String is_statsDate1 = queryString.getString("is_statsDate1");
String is_statsDate2 = queryString.getString("is_statsDate2");
String is_statsDate3 = queryString.getString("is_statsDate3");

if(statsType == 1) {
	gubunItemName = rbsMessageSource.getMessage("item.contact.date.month");
	titleGubunName = is_statsDate1 + rbsMessageSource.getMessage("item.contact.date.year") + " " + 
					rbsMessageSource.getMessage("item.contact.date.months");
} else if(statsType == 2) {
	gubunItemName = rbsMessageSource.getMessage("item.contact.date.day");
	titleGubunName = is_statsDate1 + rbsMessageSource.getMessage("item.contact.date.year") + " " + 
					is_statsDate2 + rbsMessageSource.getMessage("item.contact.date.month") + " " + 
					rbsMessageSource.getMessage("item.contact.date.days");
} else if(statsType == 3) {
	gubunItemName = rbsMessageSource.getMessage("item.contact.date.hour");
	titleGubunName = is_statsDate1 + rbsMessageSource.getMessage("item.contact.date.year") + " " + 
					is_statsDate2 + rbsMessageSource.getMessage("item.contact.date.month") + " " + 
					is_statsDate3 + rbsMessageSource.getMessage("item.contact.date.day") + " " + 
					rbsMessageSource.getMessage("item.contact.date.hours");
} else {
	gubunItemName = rbsMessageSource.getMessage("item.contact.date.year");
	titleGubunName = rbsMessageSource.getMessage("item.contact.date.days");
}
%>