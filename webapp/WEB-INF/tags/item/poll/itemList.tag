<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="pollui" tagdir="/WEB-INF/tags/item/poll" %>
<%@ attribute name="itemList" type="java.util.List"%>
<%@ attribute name="msgItemSelect"%>
<%@ attribute name="msgItem"%>
<%@ attribute name="msgItemEtc"%>
<%@ attribute name="msgPoint"%>
<%@ attribute name="usePoint" type="java.lang.Boolean"%>
<%@ attribute name="useEtc" type="java.lang.Boolean"%>
<c:if test="${empty usePoint}">
	<c:set var="usePoint" value="false"/>
</c:if>
<c:if test="${empty useEtc}">
	<c:set var="useEtc" value="false"/>
</c:if>
<% /* itemList */ %>
								<c:if test="${empty itemList}">
									<pollui:itemTR usePoint="${usePoint}" useEtc="${useEtc}" msgItem="${msgItem}" msgItemEtc="${msgItemEtc}" msgItemSelect="${msgItemSelect}"/>
								</c:if>
								<c:forEach var="itemDt" items="${itemList}">
									<pollui:itemTR itemDt="${itemDt}" useEtc="${useEtc}" usePoint="${usePoint}" msgItem="${msgItem}" msgItemEtc="${msgItemEtc}" msgItemSelect="${msgItemSelect}"/>
								</c:forEach>