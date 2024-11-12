<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<c:set var="conMmgUTP"><spring:message code="Globals.code.USERTYPE_ADMIN"/></c:set>
<c:set var="wrtUTPAuth" value="${elfn:getModuleUTP('WRT')}"/>
	<div class="btnRight">
		<div class="right">
			<c:set var="dsetInputBtn" value="${settingInfo.dset_input_btn}"/>
			<c:if test="${(!empty crtModuleAuth.WRT_GRP || wrtUTPAuth lt conMmgUTP) and (empty dsetInputBtn or dsetInputBtn eq '0' or (dsetInputBtn eq '1' and wrtAuth))}">
				<a href="<c:out value="${URL_INPUT}"/>" title="등록" class="btnTypeA fn_btn_write${inputWinFlag}">등록</a>
			</c:if>
		</div>
	</div>
