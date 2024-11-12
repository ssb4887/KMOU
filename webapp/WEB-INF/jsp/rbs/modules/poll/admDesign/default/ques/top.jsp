<div class="btnTopFull">
	<div class="left">
		<h5>
			<span>[<c:out value="${pollDt.ISSTOP_NAME}"/>] <c:out value="${pollDt.TITLE}"/></span>
			<c:if test="${!empty pollDt.LIMIT_DATE11 || !empty pollDt.LIMIT_DATE21}">
			<span style="font-weight: normal;"><c:out value="${pollDt.LIMIT_DATE11}"/><c:if test="${!empty pollDt.LIMIT_DATE12 || !empty pollDt.LIMIT_DATE13}">&nbsp;</c:if><c:out value="${pollDt.LIMIT_DATE12}"/><c:if test="${!empty pollDt.LIMIT_DATE13}">:<c:out value="${pollDt.LIMIT_DATE13}"/></c:if><c:if test="${!empty pollDt.LIMIT_DATE21 || !empty pollDt.LIMIT_DATE22 || !empty pollDt.LIMIT_DATE23}"> ~ </c:if><c:out value="${pollDt.LIMIT_DATE21}"/><c:if test="${!empty pollDt.LIMIT_DATE22 || !empty pollDt.LIMIT_DATE23}">&nbsp;</c:if><c:out value="${pollDt.LIMIT_DATE22}"/><c:if test="${!empty pollDt.LIMIT_DATE23}">:<c:out value="${pollDt.LIMIT_DATE23}"/></c:if></span>
			</c:if>
		</h5>
	</div>
	<div class="fn_pollIngComment comment hidden" style="clear:both;"><spring:message code="message.poll.app.ing.con"/></div>
</div>