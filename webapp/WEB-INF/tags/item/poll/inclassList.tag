<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="pollui" tagdir="/WEB-INF/tags/item/poll" %>
<%@ attribute name="inclassList" type="java.util.List"%>
<%@ attribute name="inquesMap" type="java.util.Map"%>
<%@ attribute name="msgItemInclassName"%>
<%@ attribute name="msgItemInclass"%>
<%@ attribute name="msgItemSelect"%>
<%@ attribute name="msgItemInques"%>
<% /* sitemap */ %>
				<c:if test="${empty inclassList}">
					<pollui:inclassTR msgItemInclass="${msgItemInclass}" msgItemInclassName="${msgItemInclassName}" msgItemInques="${msgItemInques}" msgItemSelect="${msgItemSelect}"/>
				</c:if>
				<c:forEach var="inclassDt" items="${inclassList}">
					<c:set var="keyColumnId" value="${dt.QUESTION_IDX},${inclassDt.CLASS_IDX}"/>
					<pollui:inclassTR inclassDt="${inclassDt}" inquesList="${inquesMap[keyColumnId]}" msgItemInclass="${msgItemInclass}" msgItemInclassName="${msgItemInclassName}" msgItemInques="${msgItemInques}" msgItemSelect="${msgItemSelect}"/>
				</c:forEach>