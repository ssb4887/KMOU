<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="items" value="${itemInfo.items}"/>
<c:set var="dsetCateListId" value="${settingInfo.dset_cate_list_id}"/>
<c:set var="exceptIdStr">name,notice,subject,file,contents<c:if test="${!empty dsetCateListId}">,${dsetCateListId}</c:if></c:set>
<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
var varSource = '';
varSource += '<tr class="fn_qCont">';
varSource += '<td colspan="6">';
varSource += '<table class="tbViewA" summary="${summary}">';
varSource += '<caption>';
varSource += '게시글 읽기';
varSource += '</caption>';
varSource += '<colgroup>';
varSource += '<col style="width:100px;" />';
varSource += '<col />';
varSource += '<col style="width:100px;" />';
varSource += '<col />';
varSource += '<col style="width:100px;" />';
varSource += '<col style="width:60px;" />';
varSource += '</colgroup>';
varSource += '<tbody>';
				<c:if test="${settingInfo.use_file == '1'}">
varSource += '<tr>';
<c:set var="fileItemView"><itui:objectView itemId="file" multiFileHashMap="${multiFileHashMap}"/></c:set>
<c:set var="fileItemView" value="${elfn:getNLinetoBR(fileItemView)}"/>
varSource += '<th scope="row"><spring:message code="item.file.name"/></th><td colspan="5">${fileItemView}</td>';
varSource += '</tr>';
				</c:if>
<c:set var="itemViewAll"><itui:itemViewAll colspan="5" itemInfo="${itemInfo}" itemOrderName="${submitType}_order" exceptIds="${exceptIds}"/></c:set>
<c:set var="itemViewAll" value="${elfn:getNLinetoBR(itemViewAll)}"/>		
varSource += '${itemViewAll}';
varSource += '<tr>';
varSource += '<td class="cont" colspan="6">';
<c:set var="contentsItemView"><itui:objectView itemId="contents"/></c:set>
<c:set var="contentsItemView" value="${elfn:getNLinetoBR(contentsItemView)}"/>
varSource += '${contentsItemView}';
varSource += '</td>';
varSource += '</tr>';
varSource += '</tbody>';
varSource += '</table>';
varSource += '</td>';
varSource += '</tr>';
$(".fn_qList a[data-nm=${queryString.dataNm}]").parents("tr").after(varSource);
$(".fn_qList a[data-nm=${queryString.dataNm}]").parents("tr").next('.fn_qCont').find('table').siblings('br').remove();