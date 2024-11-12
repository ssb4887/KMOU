			<%/* 콘텐츠 구분 사용여부 */ %>
			<c:set var="itemId" value="contType"/>
			<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
			<c:set var="useContentsType" value="${elfn:useSettingtem(settingInfo, itemObj)}"/>
			<c:if test="${useContentsType}">
				<c:set var="noListColspan" value="${noListColspan + 1}"/>
			</c:if>