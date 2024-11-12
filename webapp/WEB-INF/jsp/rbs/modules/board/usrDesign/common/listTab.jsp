		<c:set var="cateTabId" value="${settingInfo.dset_cate_tab_id}"/>
		<c:set var="cateTabSearchName"><spring:message code="Globals.item.tab.search.pre.flag"/>${cateTabId}</c:set>
		<c:if test="${!empty cateTabId}">
			<ul class="tabTypeC">
				<c:if test="${settingInfo.use_cate_tab_total eq '1'}"><li><a href="${URL_DEFAULT_TAB_LIST}"<c:if test="${empty param[cateTabSearchName]}"> class="on"</c:if>><spring:message code="item.tab.all"/></a></li></c:if>
				<c:forEach items="${optnHashMap[itemInfo.items[cateTabId]['master_code']]}" var="cateTabDt" varStatus="i">
					<li><a href="<c:out value="${URL_DEFAULT_TAB_LIST}&${cateTabSearchName}=${cateTabDt.OPTION_CODE}"/>"<c:if test="${(settingInfo.use_cate_tab_total ne '1' and empty param[cateTabSearchName] and i.first) or (param[cateTabSearchName] eq cateTabDt.OPTION_CODE)}"> class="on"</c:if>><c:out value="${cateTabDt.OPTION_NAME}"/></a></li>
				</c:forEach>
	        </ul>
		</c:if>