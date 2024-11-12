<%@ include file="../../../include/commonTop.jsp"%>
<%@ include file="joinStep.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="items" value="${itemInfo.items}"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<itui:submitScript items="${items}" itemOrder="${itemOrder}"/>	<% /* javascript include */ %>
<script type="text/javascript">
$(function(){	
	$('#selectAll').change(function(){
		if($(this).prop("checked")) $('#<c:out value="${param.inputFormId}"/> input[id$=Agree1]').prop('checked', true);
		else $('#<c:out value="${param.inputFormId}"/> input[id$=Agree2]').prop('checked', true);
	});
	
	$('#<c:out value="${param.inputFormId}"/>').submit(function(){
		try {
			return fn_memberInputSubmitForm();
		}catch(e){alert(e); return false;}
	});
});

function fn_memberInputSubmitForm(){
	var varParam = '';
	<c:if test="${!empty items && !empty itemOrder}">
	<c:forEach var="itemId" items="${itemOrder}">
		<c:set var="objectType" value="${items[itemId]['object_type']}"/>
		<c:if test="${objectType eq 4 or objectType eq 5}">
		var varObj = $('#<c:out value="${param.inputFormId}"/> input[id=${itemId}1]');
		if(!varObj.prop('checked'))
		{
			alert('<spring:message code="message.member.join.agree" arguments="${items[itemId][\'item_name\']}"/>');
			varObj.focus();
			return false;
		}
		else varParam += '&${itemId}=' + varObj.val();
		</c:if>
	</c:forEach>
	</c:if>
	
	location.href = $('#<c:out value="${param.inputFormId}"/>').prop('action') + varParam;
	return false;
}
</script>