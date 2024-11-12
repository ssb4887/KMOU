<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<h4 class="titTypeC<c:if test="${!param.hnoMargin}"> mgt70</c:if>">관련글</h4>
<c:set var="colSpan" value="5"/>
<c:set var="subject" value="subject"/>
<c:set var="replyState" value="replyState"/>
<c:set var="dsetCateListId" value="${settingInfo.dset_cate_list_id}"/>
<table class="tbListA fn_qList" summary="<spring:message code="item.no.name"/>, <spring:message code="item.modify.name"/>, <itui:objectItemName itemInfo="${itemInfo}" itemId="${subject}"/>, <c:if test="${settingInfo.use_file == '1'}"><spring:message code="item.file.name"/>, </c:if><spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>를 제공하는 표" style="margin:10px 0 10px 0;">
	<caption><c:out value="${settingInfo.list_title}"/> 목록</caption>
	<colgroup>
		<col width="60px" />
		<col />
		<c:if test="${settingInfo.use_file == '1'}"><col width="60px" /><c:set var="colSpan" value="${colSpan + 1}"/></c:if>
		<col width="100px" />
		<col width="100px" />
		<col width="90px" />
	</colgroup>
	<thead>
	<tr>
		<th scope="col"><spring:message code="item.no.name"/></th>
		<th scope="col"><itui:objectItemName itemInfo="${itemInfo}" itemId="${subject}"/></th>
		<c:if test="${settingInfo.use_file == '1'}"><th scope="col"><spring:message code="item.file.name"/></th></c:if>
		<th scope="col"><spring:message code="item.reginame1.name"/></th>
		<th scope="col"><spring:message code="item.regidate1.name"/></th>
		<th scope="col" class="end"><spring:message code="item.board.views.name"/></th>
		<!-- 마지막 th에 class="end" -->
	</tr>
	</thead>
	<tbody class="alignC">
		<c:if test="${empty pntList}">
		<tr>
			<td colspan="${colSpan}" class="bllist"><spring:message code="message.no.list"/></td>
		</tr>
		</c:if>
		<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
		<c:set var="listNo" value="${paginationInfo.totalRecordCount - paginationInfo.firstRecordIndex}" />
		<c:if test="${listNo eq 0}"><c:set var="listNo" value="${fn:length(pntList)}" /></c:if>
		<c:forEach var="listDt" items="${pntList}" varStatus="i">
			<c:set var="listKey" value="${listDt.BRD_IDX}"/>
			<tr>
				<td class="num">
					<c:choose>
						<c:when test="${isNotice}"><spring:message code="item.notice.name"/></c:when>
						<c:otherwise>${listNo}</c:otherwise>
					</c:choose>
				</td>
				<td class="tlt">
					<a href="#none" title="상세보기" id="fn_btn_view_${listDt.BRD_IDX}" class="fn_btn_view"<c:if test="${listDt.RE_LEVEL > 1}"> style="padding-left:${(listDt.RE_LEVEL - 1) * 10}px;"</c:if>>
						<c:if test="${settingInfo.use_list_img eq '1' and !empty listDt.LISTIMG_SAVED_NAME}"><img src="<c:out value="${URL_IMAGE}"/>&type=s&id=<c:out value="${elfn:imgNSeedEncrypt(listDt.LISTIMG_SAVED_NAME)}"/>" alt="<c:out value="${listDt.LISTIMG_TEXT}"/>"/></c:if>
						<c:if test="${!empty dsetCateListId}">
							<c:set var="dsetCateListVal"><itui:objectView itemId="${dsetCateListId}" optnHashMap="${elfn:getItemOptnHashMap(itemInfo.items, itemInfo.list_order)}" objDt="${listDt}"/></c:set>
							<c:if test="${!empty dsetCateListVal}">[${dsetCateListVal}]</c:if>
						</c:if>
						<c:out value="${listDt.SUBJECT}"/>
						<c:if test="${settingInfo.use_secret eq '1' and listDt.SECRET eq '1'}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_secret.gif"/>" alt="비밀글"/></c:if>
						<c:if test="${settingInfo.use_new eq '1' and elfn:getNewTime(listDt.REGI_DATE, 1)}"> <img src="<c:out value="${contextPath}${imgPath}/common/icon_new.gif"/>" alt="새글"/></c:if>
					</a>
				</td> 
				<c:if test="${settingInfo.use_file == '1'}"><td><c:if test="${listDt.FILE_CNT > 0}"><img src="<c:out value="${contextPath}${imgPath}/common/ico_file.gif"/>" alt="파일"/></c:if></td></c:if>
				<td><c:out value="${listDt.NAME}"/></td>		
				<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
				<td class="num"><c:out value="${listDt.VIEWS}"/></td>			
			</tr>
			<tr class="fn_qCont" style="display:none;">
				<td colspan="${colSpan}">
					<table class="tbViewA" summary="${param.summary}">
						<caption>
						게시글 읽기
						</caption>
						<colgroup>
						<col style="width:100px;" />
						<col />
						<col style="width:100px;" />
						<col />
						<col style="width:100px;" />
						<col style="width:60px;" />
						</colgroup>
						<tbody>
							<c:if test="${settingInfo.use_file == '1'}">
							<tr>
								<th scope="row"><spring:message code="item.file.name"/></th><td colspan="5"><itui:objectView itemId="file" multiFileHashMap="${pntMultiFileHashMap[elfn:toString(listDt.BRD_IDX)]}"/></td>
							</tr>
							</c:if>
							<c:set var="exceptIdStr">name,notice,subject,file,contents<c:if test="${!empty dsetCateListId}">,${dsetCateListId}</c:if></c:set>
							<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
							<itui:itemViewAll colspan="5" itemInfo="${itemInfo}" itemOrderName="${submitType}_order" exceptIds="${exceptIds}" objDt="${listDt}" optnHashMap="${pntOptnHashMap}" multiFileHashMap="${pntMultiFileHashMap[elfn:toString(listDt.BRD_IDX)]}" multiDataHashMap="${pntMultiDataHashMap[elfn:toString(listDt.BRD_IDX)]}"/>
							<tr>
								<td class="cont" colspan="6">
									<itui:objectView itemId="contents" objDt="${listDt}"/>
								</td>
							</tr>
						</tbody>
					</table>
				</td>	
			</tr>
			<c:set var="listNo" value="${listNo - 1}"/>
		</c:forEach>
	</tbody>
</table>