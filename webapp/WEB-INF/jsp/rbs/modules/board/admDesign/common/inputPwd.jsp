				<%-- 비회원글쓰기권한 : 비밀번호 등록  --%>
				<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
				<c:set var="isNoMemberAuthPage" value="${elfn:isNoMemberAuthPage(chkAuthName)}"/>
				<c:if test="${isNoMemberAuthPage}">
				<spring:message var="pwdItemName" code="item.board.password"/>
				<c:if test="${param.mode == 'm' && !mngAuth}">
				<spring:message var="prePwdItemName" code="item.pre_password.name" arguments="${pwdItemName}"/>
				<tr>
					<th scope="row"><label for="pre_pwd" class="required">${prePwdItemName}</label></th>
					<td><input type="password" id="pre_pwd" name="pre_pwd" class="inputTxt" maxlength="16" required="required" title="${prePwdItemName}"/></td>
				</tr>
				</c:if>
				<tr>
					<th scope="row"><label for="pwd"<c:if test="${param.mode != 'm'}"> class="required"</c:if>>${pwdItemName}</label></th>
					<td><input type="password" id="pwd" name="pwd" class="inputTxt" maxlength="16"<c:if test="${param.mode != 'm'}"> required="required"</c:if> title="${pwdItemName}"/></td>
				</tr>
				</c:if>