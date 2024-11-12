<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript">
function showObject(object){
	$('#' + object).show();
}
function hideObject(object){
	$('#' + object).hide();
}
function fn_preMonthCon(theAction){
	<c:choose>
		<c:when test="${nowMonth eq 0}">
			<c:set var="nYear" value="${nowYear-1}" />
			<c:set var="nMonth" value="11" />
		</c:when>
		<c:otherwise>
			<c:set var="nYear" value="${nowYear}" />
			<c:set var="nMonth" value="${nowMonth-1}" />
		</c:otherwise>
	</c:choose>
	location.href = theAction + '&year=${nYear}&month=${nMonth}';
}
function fn_nextMonthCon(theAction){
	<c:choose>
		<c:when test="${nowMonth eq 11}">
			<c:set var="nYear" value="${nowYear+1}" />
			<c:set var="nMonth" value="0" />
		</c:when>
		<c:otherwise>
			<c:set var="nYear" value="${nowYear}" />
			<c:set var="nMonth" value="${nowMonth+1}" />
		</c:otherwise>
	</c:choose>
	location.href = theAction + '&year=${nYear}&month=${nMonth}';
}
function fn_view(theAction){
	fnAjaxSubmitForm({url:theAction, load_selector:$('#fn_view_page')});
}
</script>