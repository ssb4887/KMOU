<%@ tag language="java" pageEncoding="UTF-8" body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ attribute name="formId"%>
<%@ attribute name="formName"%>
<%@ attribute name="formAction"%>
<%@ attribute name="submitBtnId"%>
<%@ attribute name="submitBtnClass"%>
<%@ attribute name="submitBtnValue"%>
<%@ attribute name="listAction"%>
<%@ attribute name="listBtnId"%>
<%@ attribute name="listBtnClass"%>
<%@ attribute name="searchFormExceptParams" type="java.lang.String[]"%>
<form name="${formName}" id="${formId}" method="get"  action="${formAction}">
	<elui:hiddenInput inputInfo="${queryString}" exceptNames="${searchFormExceptParams}"/>
	<fieldset>
		<legend>상세검색</legend>
		<dl>
		<jsp:doBody/>
		</dl>
		<p>
			<input type="submit" id="${submitBtnId}" class="${submitBtnClass}" value="${submitBtnValue}" title="${submitBtnValue}"/>
			<c:if test="${isSearchList}">
			<a href="${listAction}"<c:if test="${!empty listBtnId}"> id="${listBtnId}"</c:if><c:if test="${!empty listBtnClass}">  class="${listBtnClass}"</c:if>>전체목록</a>
			</c:if>
		</p>
	</fieldset>
</form>
<script type="text/javascript">
$(function(){
	// 검색 항목 정렬
	var varSearchDl = $("#<c:out value="${formId}"/> dl");
	var varSearchDDFull = varSearchDl.find("dd.full");
	//var varSearchDDFull = varSearchDl.find("dd.full:first");
	var varSearchDDs = varSearchDl.find("dd");
	var varAddIdx = 0;
	$.each(varSearchDDFull, function(){
		var varFullIdx = varSearchDDs.index($(this));
		if(/*varFullIdx > 0 && */(varFullIdx + varAddIdx)%2 == 1){
			varSearchDDs.filter(":eq(" + (varFullIdx - 1) + ")").addClass("full");
		}
		varAddIdx ++;
	});
});
</script>