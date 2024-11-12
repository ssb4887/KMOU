			<c:choose>
			<c:when test="${verDt.WORK_TYPE != '10' && verDt.WORK_TYPE != '11'}">
			<div class="btnCenter">
				<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
				<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
				<c:if test="${verDt.WORK_TYPE != '0'}"><a href="<c:out value="${URL_WORKIDX_VIEW}"/>" class="btnTypeHL fn_btn_workView" target="_blank">콘텐츠 보기</a></c:if>
			</div>
			</c:when>
			<c:when test="${verDt.WORK_TYPE != '0'}">
			<div class="btnCenter">
				<a href="<c:out value="${URL_WORKIDX_VIEW}"/>" class="btnTypeHL fn_btn_workView" target="_blank">콘텐츠 보기</a>
			</div>
			</c:when>
			</c:choose>