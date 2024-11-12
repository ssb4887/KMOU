		<%-- 비회원글쓰기/댓글쓰기권한 : 비밀번호 확인  --%>
		<div id="fn_infoPwd" style="display:none; width:500px; height:150px;">
			<form name="fn_passwordForm" id="fn_passwordForm" method="post" target="submit_target">
				<input type="hidden" id="chkUrl" name="chkUrl"/>
				<fieldset>
					<legend>글쓰기 서식</legend>
					<table class="tbWriteA" summary="글쓰기 서식" style="margin:20px auto;width:90%;">
						<caption>
						글쓰기 서식
						</caption>
						<colgroup>
						<col style="width:120px;" />
						<col />
						</colgroup>
						<tbody>
							<tr>
								<th><label for="chk_pwd" class="required"><spring:message code="item.board.password"/></label></th>
								<td><input type="password" id="chk_pwd" name="pwd" class="inputTxt" maxlength="16" required="required" title="<spring:message code="item.board.password"/>"/></td>
								<td><spring:message var="btn_confirm" code="button.confirm"/><input type="submit" class="btnTypeA fn_btn_submit_nm" title="<c:out value="${btn_confirm}"/>" value="<c:out value="${btn_confirm}"/>" /></td>
							</tr>
						</tbody>
					</table>
				</fieldset>
			</form>
		</div>