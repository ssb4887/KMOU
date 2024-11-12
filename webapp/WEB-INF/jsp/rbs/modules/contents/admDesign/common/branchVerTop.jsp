			<dl>
				<c:set var="contSettingInfo" value="${moduleSetting.setting_info}"/>
				<c:set var="contItemInfo" value="${moduleItem.item_info}"/>
				<c:set var="itemId" value="contName"/>
				<dt><itui:objectItemName itemId="${itemId}" itemInfo="${contItemInfo}"/></dt>
				<dd>
					<itui:objectView itemId="${itemId}" itemInfo="${contItemInfo}" objDt="${contDt}"/>
					<c:set var="itemId" value="contType"/>
					<c:set var="useContentsType" value="${elfn:useSettingtem(contSettingInfo, contItemInfo.items[itemId])}"/>
					<c:if test="${useContentsType}">
					(<itui:objectView itemId="${itemId}" itemInfo="${contItemInfo}" objDt="${contDt}" optnHashMap="${contOptnHashMap}"/>)
					</c:if>
				</dd>
				<c:set var="itemId" value="metaKeyword"/>
				<dt><itui:objectItemName itemId="${itemId}" itemInfo="${contItemInfo}"/></dt>
				<dd class="lw"><itui:objectView itemId="${itemId}" itemInfo="${contItemInfo}" objDt="${contDt}"/></dd>
			</dl>