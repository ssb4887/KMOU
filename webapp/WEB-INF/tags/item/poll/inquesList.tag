<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="pollui" tagdir="/WEB-INF/tags/item/poll" %>
<%@ attribute name="msgItemSelect"%>
<%@ attribute name="msgItemInques"%>
<%@ attribute name="inquesList" type="java.util.List"%>
<% /* inquesList */ %>
								<c:if test="${empty inquesList}">
									<pollui:inquesTR/>
								</c:if>
								<c:forEach var="inquesDt" items="${inquesList}">
									<pollui:inquesTR inquesDt="${inquesDt}" msgItemSelect="${msgItemSelect}" msgItemInques="${msgItemInques}"/>
								</c:forEach>