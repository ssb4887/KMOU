<c:set var="limitState" value="${1}"/>
<c:set var="limitSDateStr" value="${pollDt.LIMIT_DATE11}"/>
<c:if test="${!empty limitSDateStr}">
	<c:set var="limitSDateStr" value="${pollDt.LIMIT_DATE11} ${elfn:getString(pollDt.LIMIT_DATE12, '00')}:${elfn:getString(pollDt.LIMIT_DATE13, '00')}:00"/>
	<c:set var="limitState" value="${elfn:getNDateTimeState(limitSDateStr)}"/>
</c:if>
<%/* 설문참여여부 : 시작일, 참여자만 체크 */ %>
<c:set var="isPollMng" value="${true}"/>
<c:if test="${respCnt > 0}">
<c:set var="isPollMng" value="${false}"/>
</c:if>