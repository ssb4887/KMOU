		<%-- 파일다운로드 사유등록  --%>
		<div id="fn_infoFileCmt" style="display:none; width:500px; height:150px;">
			<form name="fn_fileCmtForm" id="fn_fileCmtForm" method="post" target="submit_target">
				<fieldset>
					<legend>글쓰기 서식</legend>
					<table class="tbWriteA" summary="글쓰기 서식" style="margin:20px auto;width:90%;">
						<caption>
						글쓰기 서식
						</caption>
						<colgroup>
						<col style="width:70px;" />
						<col />
						</colgroup>
						<tbody>
							<tr>
								<th><label for="fileCmt" class="required">사유</label></th>
								<td><textarea id="fileCmt" name="fileCmt" class="inputTxt" required="required" style="width:100%;height:180px;"></textarea></td>
							</tr>
						</tbody>
					</table>
					<div class="btnCenter">
						<input type="submit" class="btnTypeA fn_btn_submit" value="확인" title="확인"/>
						<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
					</div>
				</fieldset>
			</form>
		</div>