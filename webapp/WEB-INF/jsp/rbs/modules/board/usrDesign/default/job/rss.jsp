<%@ include file="../../../../../include/commonTop.jsp"%><?xml version="1.0" encoding="UTF-8"?>
<c:set var="serverName" value="<%=request.getScheme() + \"://\" + request.getServerName() + \":\" +  request.getLocalPort() +  request.getContextPath() %>"/>
<rss version="2.0">
	<channel>
		<title><![CDATA[${siteInfo.site_title}-${crtMenu.menu_name}]]></title>
		<link><![CDATA[${serverName}${siteInfo.local_path}/${crtMenu.module_id}/${URL_DEFAULT_LIST}]]></link>
		<description><![CDATA[${crtMenu.menu_name}]]></description>
		<language><![CDATA[ko]]></language>
		<pubDate><![CDATA[${elfn:getThisDate('yyyy-MM-dd HH:mm:ss')}]]></pubDate>
		<generator><![CDATA[${siteInfo.site_name}]]></generator>
		<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
		<c:set var="listColumnName" value="${settingInfo.idx_column}"/>
		<c:forEach items="${list}" var="listDt" varStatus="i">
		<c:set var="listKey" value="${listDt[listColumnName]}"/>
		<item>
			<title><![CDATA[${listDt.SUBJECT}]]></title>
			<link><![CDATA[${serverName}&${listIdxName}=${listKey}]]></link>
			<description><![CDATA[${listDt.CONTENTS}]]></description>
			<author><![CDATA[${listDt.NAME}]]></author>
			<guid><![CDATA[${serverName}&${listIdxName}=${listKey}]]></guid>
			<pubDate><![CDATA[${listDt.REGI_DATE}]]></pubDate>
		</item>
		</c:forEach>
	</channel>
</rss>
