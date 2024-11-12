<%@ tag language="java" pageEncoding="UTF-8" body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" uri="/WEB-INF/tags/mobile/item" %>
<%@ attribute name="divClass"%>														<% /* 검색 form layer(div) class */ %>
<%@ attribute name="formId"%>														<% /* 검색 form id */ %>
<%@ attribute name="formName"%>														<% /* 검색 form name */ %>
<%@ attribute name="formAction"%>													<% /* 검색 form action */ %>
<%@ attribute name="submitBtnId"%>													<% /* 검색 form submit버튼 id */ %>
<%@ attribute name="submitBtnClass"%>												<% /* 검색 form submit버튼 class */ %>
<%@ attribute name="submitBtnValue"%>												<% /* 검색 form submit버튼 value */ %>
<%@ attribute name="listAction"%>													<% /* 전체목록 경로 */ %>
<%@ attribute name="listBtnId"%>													<% /* 전체목록 버튼 id */ %>
<%@ attribute name="listBtnClass"%>													<% /* 전체목록 버튼 class */ %>
<%@ attribute name="itemListSearch" type="net.sf.json.JSONObject"%>					<% /* 항목설정정보 > 검색항목설정정보 */ %>
<%@ attribute name="searchFormExceptParams" type="java.lang.String[]"%>				<% /* 검색 form에서 제외할 항목ID */ %>
<%@ attribute name="searchOptnHashMap" type="java.util.HashMap"%>					<% /* 기초코드 정보 */ %>
<%@ attribute name="isUseRadio" type="java.lang.Boolean"%>
	<spring:message var="itemSearchPreFlag" code="Globals.item.search.pre.flag"/>
	<c:if test="${!empty itemListSearch}">
		<c:set var="itemSearchOrder" value="${itemListSearch.searchForm_order}"/>
		<c:if test="${!empty itemSearchOrder}">
			<c:set var="keyItemName" value="key"/>
			<c:set var="keyFieldItemName" value="keyField"/>
			<c:set var="keyInfo" value="${itemListSearch[keyItemName]}"/>
			<c:set var="keyFieldInfo" value="${itemListSearch[keyFieldItemName]}"/>
			<c:choose>
				<c:when test="${empty divClass && !empty keyInfo && !empty keyInfo.items}"><c:set var="divClass" value="tbMSearch"/></c:when>
				<c:when test="${empty divClass}"><c:set var="divClass" value="tbSearch"/></c:when>
			</c:choose>
			<div class="${divClass}">
			<form name="${formName}" id="${formId}" method="get"  action="<c:out value="${formAction}"/>">
				<elui:hiddenInput inputInfo="${queryString}" exceptNames="${searchFormExceptParams}"/>
				<fieldset>
					<legend>상세검색</legend>
					<dl>
					<itui:searchFormItemIn itemListSearch="${itemListSearch}" searchOptnHashMap="${searchOptnHashMap}" isUseRadio="${isUseRadio}"/>
					<jsp:doBody/>
					</dl>
					<p>
						<input type="submit" id="${submitBtnId}" class="${submitBtnClass}" value="${submitBtnValue}" title="${submitBtnValue}"/>
						<c:if test="${isSearchList}">
						<a href="${listAction}"<c:if test="${!empty listBtnId}"> id="${listBtnId}"</c:if><c:if test="${!empty listBtnClass}">  class="${listBtnClass}"</c:if> data-role="button">전체목록</a>
						</c:if>
					</p>
				</fieldset>
			</form>
			</div>
			<itui:submitScript items="${keyInfo.items}" itemOrder="${itemSearchOrder}"/>
			<script type="text/javascript">
			$(function(){
				<itui:searchFormSubmitInit items="${keyInfo.items}" itemOrder="${itemSearchOrder}"/>
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
		</c:if>
	</c:if>