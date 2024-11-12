	<div id="cms_menu_dmng">
		<dl>
			<dt>자료관리담당자 :</dt>
			<dd><c:out value="${crtMenu.dmng_depart} ${crtMenu.dmng_name}"/><c:if test="${!empty crtMenu.dmng_phone}"> (<c:out value="${crtMenu.dmng_depart} ${crtMenu.dmng_phone}"/>)</c:if></dd>
		</dl>
		<dl class="date">
			<dt>최근업데이트 :</dt>
			<dd><c:out value="${crtMenu.dmng_ldate}"/></dd>
		</dl>
	</div>