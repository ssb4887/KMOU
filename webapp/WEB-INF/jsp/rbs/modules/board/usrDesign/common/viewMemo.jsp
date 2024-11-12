			<c:set var="conMmgUTP"><spring:message code="Globals.code.USERTYPE_ADMIN"/></c:set>
			<c:set var="wrtUTPAuth" value="${elfn:getModuleUTP('MWT')}"/>
			<h3 class="titTypeC mgt20 mgb10">댓글</h3>
			<c:if test="${wrtUTPAuth < conMmgUTP}">
			<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
				<table class="tbWriteA fn_writeMemo" summary="글쓰기 서식">
					<caption>
					글쓰기 서식
					</caption>
					<colgroup>
						<col style="width:120px;" />
						<col />
					</colgroup>
					<tbody>
					</tbody>
				</table>
				<div class="btnCenter">
					<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
					<a href="<c:out value="${URL_IDX_MEMOINPUT}"/>" title="취소" class="btnTypeB fn_btn_write_view">취소</a>
				</div>
			</form>
			</c:if>
			<div id="${listFormId}"></div>