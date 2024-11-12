<%@ tag language="java" pageEncoding="UTF-8" body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ attribute name="divClass"%>											<% /* 검색 form layer(div) class */ %>
<%@ attribute name="formId"%>											<% /* 검색 form id */ %>
<%@ attribute name="formName"%>											<% /* 검색 form name */ %>
<%@ attribute name="formAction"%>										<% /* 검색 form action */ %>
<%@ attribute name="submitBtnId"%>										<% /* 검색 form submit버튼 id */ %>
<%@ attribute name="submitBtnClass"%>									<% /* 검색 form submit버튼 class */ %>
<%@ attribute name="submitBtnValue"%>									<% /* 검색 form submit버튼 value */ %>
<%@ attribute name="listAction"%>										<% /* 전체목록 경로 */ %>
<%@ attribute name="listBtnId"%>										<% /* 전체목록 버튼 id */ %>
<%@ attribute name="listBtnClass"%>										<% /* 전체목록 버튼 class */ %>
<%@ attribute name="itemListSearch" type="net.sf.json.JSONObject"%>		<% /* 항목설정정보 > 검색항목설정정보 */ %>
<%@ attribute name="searchFormExceptParams" type="java.lang.String[]"%>	<% /* 검색 form에서 제외할 항목ID */ %>
<%@ attribute name="searchOptnHashMap" type="java.util.HashMap"%>		<% /* 기초코드 정보 */ %>
<c:set var="msItDelDate" value="등록일"/>
<spring:message var="msItSDate" code="item.start_date.name"/>
<spring:message var="msItEDate" code="item.end_date.name"/>
<spring:message var="msBtnCalSel" code="button.calendar.select"/>
<spring:message var="itemSearchPreFlag" code="Globals.item.search.pre.flag"/>
	<c:if test="${!empty itemListSearch}">
		<c:set var="itemSearchOrder" value="${itemListSearch.searchForm_order}"/>
		<c:if test="${!empty itemSearchOrder}">
			<c:set var="keyItemName" value="key"/>
			<c:set var="keyFieldItemName" value="keyField"/>
			<c:set var="keyInfo" value="${itemListSearch[keyItemName]}"/>
			<c:set var="keyFieldInfo" value="${itemListSearch[keyFieldItemName]}"/>
			<c:if test="${empty divClass}"><c:set var="divClass" value="tbMSearch"/></c:if>
			<div class="${divClass}">
			<form name="${formName}" id="${formId}" method="get"  action="<c:out value="${formAction}"/>">
				<elui:hiddenInput inputInfo="${queryString}" exceptNames="${searchFormExceptParams}"/>
				<fieldset>
					<legend>상세검색</legend>
					<dl>
						<dt>${msItDelDate}</dt>
						<dd class="full fn_date">
							<div class="input-group">
							<input type="text" name="delSDate" id="delSDate" title="${msItSDate}" readonly="readonly" style="width:100px;" value="<c:out value="${queryString.delSDate}"/>"/>
							<span><button type="button" class="btnCal" id="fn_btn_delSDate" title="${msBtnCalSel}">${msBtnCalSel}</button></span>
							<span class="input-group-sp">~</span>
							<input type="text" name="delEDate" id="delEDate" title="${msItEDate}" readonly="readonly" style="width:100px;" value="<c:out value="${queryString.delEDate}"/>"/>
							<span><button type="button" class="btnCal" id="fn_btn_delEDate" title="${msBtnCalSel}">${msBtnCalSel}</button></span>
							</div>
						</dd>
						<itui:searchFormItemIn itemListSearch="${itemListSearch}" searchOptnHashMap="${searchOptnHashMap}"/>
						<jsp:doBody/>
					</dl>
					<p>
						<input type="submit" id="${submitBtnId}" class="${submitBtnClass}" value="${submitBtnValue}" title="${submitBtnValue}"/>
						<c:if test="${isSearchList}">
						<a href="<c:out value="${listAction}"/>"<c:if test="${!empty listBtnId}"> id="${listBtnId}"</c:if><c:if test="${!empty listBtnClass}">  class="${listBtnClass}"</c:if>>전체목록</a>
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
				
				// 달력 선택
				try{$("#fn_btn_delSDate").click(function(event){displayCalendar($('#delSDate'),'', this);return false;});}catch(e){}
				try{$("#fn_btn_delEDate").click(function(event){displayCalendar($('#delEDate'),'', this);return false;});}catch(e){}
			});
			</script>
		</c:if>
	</c:if>