		<c:set var="inputFormId" value="fn_pollRespForm"/>
		<div class="poll_box">
			<div class="ing_icon">진행중</div>
			<div class="wrap">
				<div class="poll_ing" id="fn_poll_respDiv">
				<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>&pollIdx=<c:out value="${ingDt.POLL_IDX}"/>" target="submit_target">
					<%@ include file="responseCon.jsp"%>
					<div class="btnCenter" id="wrap_poll_submit_btn">
						<button type="button" class="btnTypeT" id="fn_btn_tmp_join">임시저장</button>
						<button type="submit" class="btnTypeA" id="fn_btn_join"><spring:message code="item.poll.join"/></button>
						<button type="button" class="fn_btn_reset btnTypeB"><spring:message code="button.cancel"/></button>
						<c:if test="${(usePrivate && isPollResp || !usePrivate) && ingDt.ISRESULT == '1'}"><button type="button" class="btnTypeH" id="fn_btn_result" value="${URL_VIEW}&pollIdx=<c:out value="${ingDt.POLL_IDX}"/>"><spring:message code="title.poll.result.name"/></button></c:if>
					</div>
					</form>
				</div>
			</div>
		</div>
		<jsp:include page="../../response.jsp" flush="false">
			<jsp:param name="inputFormId" value="${inputFormId}"/>
		</jsp:include>