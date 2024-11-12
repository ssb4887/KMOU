			<c:if test="${useLocaleLang == '1'}">
			<div class="absRight">
			<form id="fn_searchLangForm" name="fn_searchLangForm" method="get" action="<c:out value="${searchLangUrl}"/>">
				<elui:hiddenInput inputInfo="${queryString}" exceptNames="${langSearchFormExceptParams}"/>
				<fieldset>
					<legend class="blind">언어검색 폼</legend>
					<label class="tit blind" for="lang">언어검색</label>
					<select id="slang" name="slang" class="select">
						<c:forEach var="langDt" items="${langList}">
						<option value="<c:out value="${langDt.OPTION_CODE}"/>"<c:if test="${langDt.OPTION_CODE == queryString.slang}"> selected="selected"</c:if>><c:out value="${langDt.OPTION_NAME}"/></option>
						</c:forEach>
					</select>
					<button type="submit" class="btnTFDL" id="fn_btn_search_slang">선택</button>
				</fieldset>
			</form>
			</div>
			</c:if>