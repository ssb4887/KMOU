<c:if test="${!empty newList}">
<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
<c:set var="listIdxColumn" value="${settingInfo.idx_column}"/>
<c:forEach items="${newList}" var="listDt" varStatus="i">
	<c:set var="listKey" value="${listDt[listIdxColumn]}"/>
	<c:set var="viewUrl" value="${URL_VIEW}&${listIdxName}=${listKey}"/>
	<div style="overflow:hidden">
	<c:if test="${settingInfo.use_list_img eq '1' and !empty listDt.LISTIMG_SAVED_NAME}">
	<div class="N_thumb" style="display: inline-block;">
		<a href="<c:out value="${viewUrl}"/>"><img src="<c:out value="${URL_IMAGE}"/>&type=s&id=<c:out value="${elfn:imgNSeedEncrypt(listDt.LISTIMG_SAVED_NAME)}"/>" alt="<c:out value="${listDt.LISTIMG_TEXT}"/>"/></a>
	</div>
	</c:if>
	<div class="N_memo">
	 <a href="<c:out value="${viewUrl}"/>">
	 <dl>
	   <dt><c:out value="${listDt.SUBJECT}"/></dt>
	   <dd><c:out value="${elfn:getInlineContents(listDt.CONTENTS, 100)}"/></dd>
	   </dl>
	 </a>
	</div>
	</div> 
</c:forEach>
</c:if>