<%@ include file="../../../include/commonTop.jsp"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/calendar.js"/>"></script>
<script type="text/javascript">
$(function(){		
	try{$("#fn_btn_is_regiStartDate").click(function(event){displayCalendar2($('#is_regiStartDate'), $('#is_regiEndDate'), '', this);return false;});}catch(e){}
	try{$("#fn_btn_is_regiEndDate").click(function(event){displayCalendar2($('#is_regiEndDate'), $('#is_regiStartDate'), '', this);return false;});}catch(e){}
});
</script>
