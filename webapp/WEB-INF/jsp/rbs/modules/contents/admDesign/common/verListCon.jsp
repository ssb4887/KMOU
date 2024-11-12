			<c:set var="input_dialog_height" value="450"/>
			<c:set var="deleteList_dialog_title" value=""/>
			<%@ include file="../../../../adm/include/module/listBtnsScript.jsp"%>
			<div class="btnTopFull right">
				<h5>버전관리</h5>
				<div class="left">
					<c:if test="${mngAuth}">
					<a href="<c:out value="${URL_VERSIONDELETE_LIST}"/>" title="휴지통" class="btnTDL fn_btn_deleteList">휴지통</a>
					<a href="<c:out value="${URL_VERSIONDELETEPROC}"/>" title="삭제" class="btnTD fn_btn_delete">삭제</a>
					</c:if>
				</div>
				<div class="right">
					<c:if test="${wrtAuth}">
					<a href="<c:out value="${URL_VERSIONINPUT}"/>" title="등록" class="btnTW fn_btn_write${inputWinFlag}">등록</a>
					</c:if>
					<c:if test="${empty menuType || menuType == 0}">
					<a href="<c:out value="${URL_LIST}"/>" class="btnTFDL">콘텐츠 목록</a>
					</c:if>
				</div>
			</div>
			<c:set var="versionSettingInfo" value="${moduleSetting.version_setting_info}"/>
			<form id="${versionListFormId}" name="${versionListFormId}" method="post" target="list_target">
			<table class="tbListA" summary="버전 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
				<caption>버전 목록</caption>
				<colgroup>
					<col width="50px" />
					<col />
					<col width="70px" />
					<col width="70px" />
					<col width="100px" />
					<col width="100px" />
					<col width="150px" />
					<col width="100px" />
				</colgroup>
				<thead>
				<tr>
					<th scope="col"><input type="checkbox" id="selectAll" name="selectAll" title="<spring:message code="item.select.all"/>"/></th>
					<th scope="col">버전</th>
					<th scope="col">수정</th>
					<th scope="col">관리</th>
					<th scope="col">보기</th>
					<th scope="col">상태</th>
					<th scope="col">최종작업자</th>
					<th scope="col" class="end"><spring:message code="item.regidate.name"/></th>
					<!-- 마지막 th에 class="end" -->
				</tr>
				</thead>
				<tbody class="alignC">
					<c:if test="${empty verList}">
					<tr>
						<td colspan="8" class="bllist"><spring:message code="message.no.list"/></td>
					</tr>
					</c:if>
					<c:set var="listIdxName" value="${versionSettingInfo.idx_name}"/>
					<c:forEach var="listDt" items="${verList}" varStatus="i">
					<c:set var="listKey" value="${listDt[versionSettingInfo.idx_column]}"/>
					<tr>
						<td><c:if test="${listDt.WORK_TYPE != '10'}"><input type="checkbox" id="select" name="select" title="<spring:message code="item.select"/>" value="${listKey}"/></c:if></td>
						<td class="num">${listKey}</td>
						<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_VERSIONINPUT}"/>&${listIdxName}=${listKey}&mode=m" class="btnTypeF ${btnVerModifyClass}">수정</a></c:if></td>
						<td><c:if test="${mngAuth}"><a href="<c:out value="${URL_WORKINPUT}"/>&${listIdxName}=${listKey}" class="btnTypeE fn_btn_work">관리</a></c:if></td>
						<td><c:if test="${listDt.WORK_TYPE != '0'}"><a href="<c:out value="${URL_WORKVIEW}"/>&${listIdxName}=${listKey}" data-idx="<c:out value="${listKey}"/>" class="btnTypeH fn_btn_workView">콘텐츠 보기</a></c:if></td>
						<td><itui:objectView itemId="workType" itemInfo="${moduleItem.version_item_info}" objDt="${listDt}" optnHashMap="${verOptnHashMap}"/></td>
						<td><c:out value="${elfn:memberItemOrgValue('mbrName',listDt.LAST_WORK_NAME)}"/><c:if test="">(<c:out value="${elfn:memberItemOrgValue('mbrId',listDt.LAST_WORK_ID)}"/>)</c:if></td>
						<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>
					</tr>
					<c:set var="listNo" value="${listNo - 1}"/>
					</c:forEach>
				</tbody>
			</table>
			</form>