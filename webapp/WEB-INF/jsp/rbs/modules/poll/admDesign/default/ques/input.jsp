<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pollui" tagdir="/WEB-INF/tags/item/poll" %>
<c:set var="inputFormId" value="fn_boardInputForm"/>								<%/* 입력폼 id/name */ %>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<%/* 입력폼 id/name : javascript에서 사용 */ %>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<spring:message var="msgPollQuesRel" code="item.poll.question.rel"/>
	<spring:message var="msgPollQues" code="item.poll.question"/>
	<div id="cms_poll_article">
		<%@ include file="top.jsp" %>
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<input type="hidden" id="inclassIdxs" name="inclassIdxs"/>
		<input type="hidden" id="inquesIdxs" name="inquesIdxs"/>
		<input type="hidden" id="itemIdxs" name="itemIdxs"/>
		<input type="hidden" id="relItemIdx" name="relItemIdx" value="<c:out value="${dt.REL_ITEM_IDX}"/>"/>
		<table class="tbWriteA" id="fn_top_pollMng" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<c:choose>
						<c:when test="${ISQUESTYPE}">
						<%/* 결과유형 */ %>
						<c:set var="contItemName" value="유형"/>
						<itui:itemText itemId="questionContents" itemName="${contItemName}" itemInfo="${itemInfo}" objStyle="width:500px;"/>
						</c:when>
						<c:otherwise>
						<%/* 일반 */ %>
						<itui:itemTextarea itemId="questionContents" itemInfo="${itemInfo}" objStyle="width:500px;height:70px;"/>
						</c:otherwise>
					</c:choose>
				</tr>
				<c:if test="${ISQUESTYPE}">
				<%/* 결과유형 */ %>
				<tr>
					<itui:itemTextarea itemId="resultContents" itemInfo="${itemInfo}" objStyle="width:500px;height:70px;"/>
				</tr>
				</c:if>
				<c:set var="itemId" value="ordIdx"/>
				<tr>
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}" addOrder="${true}" limitCnt="${50}" objStyle="width:554px;"/>
						<itui:objectRadio itemId="ordType" itemInfo="${itemInfo}"/>
					</td>
				</tr>
				<tr class="fn_questype fn_pollMng">
					<th scope="row"><label for="relQuesIdx"><c:out value="${msgPollQuesRel}"/></label></th>
					<td>
						<dl class="fn_inlineBlock">
							<dt><label for="relQuesIdx"><c:out value="${msgPollQues}"/></label></dt>
							<dd>
								<select id="relQuesIdx" name="relQuesIdx" title="<c:out value="${msgPollQuesRel}"/>" class="select" style="width:500px;">
									<option value=""><spring:message code="item.itemId.select.name" arguments="${msgPollQues}"/></option>
									<c:forEach var="quesDt" items="${quesList}">
									<c:if test="${dt.QUESTION_IDX != quesDt.OPTION_CODE && quesDt.USE_INQUES != '1' && quesDt.ANSWER_TYPE == '1'}">
									<option value="<c:out value="${quesDt.OPTION_CODE}"/>"<c:if test="${quesDt.OPTION_CODE == dt.REL_QUES_IDX}"> selected="selected"</c:if>><c:out value="${quesDt.QUESTION_ORDER}"/>. ${elfn:getInlineContents(quesDt.OPTION_NAME, 45)}</option>
									</c:if>
									</c:forEach>
								</select>
							</dd>
						</dl>
						<dl class="fn_inlineBlock">
							<dt class="fn_relItemIdx hidden"><label for="relItemIdx1"><spring:message code="item.poll.question.item"/></label></dt>
							<dd class="fn_relItemIdx hidden">
							</dd>
						</dl>
					</td>
				</tr>
				<c:set var="itemId" value="answerType"/>
				<tr class="fn_questype_hidden fn_pollMng">
					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}"/>
						<c:set var="itemId" value="itemType"/>
						<itui:objectSelect itemId="${itemId}" itemInfo="${itemInfo}"/>
					</td>
				</tr>
				<tr class="fn_questype_hidden">
					<c:set var="itemId" value="useInques"/>
					<itui:itemCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}" optnCode="1" optnName="사용"/>
				</tr>
			</tbody>
		</table>
		<div id="fn_inclass_list_wp" class="fn_pollMng hidden">
		<c:choose>
			<c:when test="${ISQUESTYPE}">
			<%/* 결과유형 */ %>
			<div class="btnTopFull">
				<div class="left">
					<h5><c:out value="${msgPollQues}"/></h5>
					<button type="button" title="추가" class="btnTFW fn_btn_add_inques" data-idx="1" data-title="<c:out value="${msgPollQues}"/>">추가</button>
					<button type="button" title="삭제" class="btnTFDL fn_btn_del_inques" data-idx="1" data-title="<c:out value="${msgPollQues}"/>">삭제</button>
				</div>
			</div>
			<table class="tbListA fn_inques_list_ul" summary="<c:out value="${msgPollQues}"/> 목록을 볼 수 있습니다.">
				<caption><c:out value="${msgPollQues}"/> 목록</caption>
				<colgroup>
					<col width="50px" />
					<col />
				</colgroup>
				<thead>
					<tr>
						<th scope="col"><input type="checkbox" id="inquesSelectAll" name="inquesSelectAll" title="<spring:message code="item.select.all"/>"/></th>
						<th scope="col" class="end"><c:out value="${msgPollQues}"/></th>
					</tr>
				</thead>
				<tbody class="alignC">
				<c:set var="keyColumnId" value="${dt.QUESTION_IDX},1"/>
				<pollui:inquesList inquesList="${inquesMap[keyColumnId]}" msgItemInques="${msgItemInques}"  msgItemSelect="${msgItemSelect}"/>
				</tbody>
			</table>
			</c:when>
			<c:otherwise>
			<%/* 일반 */ %>
			<!-- 내부문항 분류 -->
			<spring:message var="msgItemInclass" code="item.poll.question.inclass"/>
			<spring:message var="msgItemInclassName" code="item.poll.question.inclass.name"/>
			<spring:message var="msgItemSelect" code="button.select"/>
			<spring:message var="msgItemInques" code="item.poll.question.inques"/>
			<div class="btnTopFull">
				<div class="left">
					<h5><c:out value="${msgItemInclass}"/></h5>
					<button type="button" title="추가" class="btnTFW fn_btn_add_inclass">추가</button>
					<button type="button" title="삭제" class="btnTFDL fn_btn_del_inclass">삭제</button>
				</div>
			</div>
			<table class="tbListA fn_inclass_list" summary="<c:out value="${msgItem}"/> 목록을 볼 수 있습니다.">
				<caption><c:out value="${msgItem}"/> 목록</caption>
				<colgroup>
					<col width="50px" />
					<col />
				</colgroup>
				<thead>
					<tr>
						<th scope="col"><input type="checkbox" id="inclassSelectAll" name="inclassSelectAll" title="<spring:message code="item.select.all"/>"/></th>
						<th scope="col" class="end"><c:out value="${msgItemInclassName}"/></th>
					</tr>
				</thead>
				<tbody class="alignC">
				<pollui:inclassList inclassList="${inclassList}" inquesMap="${inquesMap}" msgItemInclass="${msgItemInclass}" msgItemInclassName="${msgItemInclassName}" msgItemInques="${msgItemInques}" msgItemSelect="${msgItemSelect}"/>
				</tbody>
			</table>
			</c:otherwise>
		</c:choose>
		</div>
		<c:if test="${!ISPOLLITEM}">
		<!-- 항목 -->
		<spring:message var="msgItem" code="item.poll.question.item"/>
		<spring:message var="msgItemEtc" code="item.poll.etc.opinion"/>
		<spring:message var="msgPoint" code="item.poll.points"/>
		<div id="fn_item_list_wp" class="fn_pollMng">
			<div class="btnTopFull">
				<div class="left">
					<h5><c:out value="${msgItem}"/></h5>
					<button type="button" title="추가" class="btnTFW fn_btn_add_item">추가</button>
					<button type="button" title="삭제" class="btnTFDL fn_btn_del_item">삭제</button>
				</div>
			</div>
			<div class="fn_item_list">
				<table class="tbListA" summary="<c:out value="${msgItem}"/> 목록을 볼 수 있습니다.">
					<caption><c:out value="${msgItem}"/> 목록</caption>
					<colgroup>
						<col width="50px" />
						<col />
						<col width="80px"/>
					</colgroup>
					<thead>
						<tr>
							<th scope="col"><input type="checkbox" id="itemSelectAll" name="itemSelectAll" title="<spring:message code="item.select.all"/>"/></th>
							<th scope="col"><c:out value="${msgItem}"/></th>
							<th scope="col" class="end">
							<c:choose>
								<c:when test="${ISQUESTYPE}">
								<c:out value="${msgPoint}"/>
								</c:when>
								<c:otherwise>
								<c:out value="${msgItemEtc}"/>
								</c:otherwise>
							</c:choose>
							</th>
						</tr>
					</thead>
					<tbody class="alignC">
					<pollui:itemList itemList="${itemList}" usePoint="${ISQUESTYPE}" useEtc="${!ISQUESTYPE}" msgPoint="${msgPoint}" msgItem="${msgItem}" msgItemEtc="${msgItemEtc}" msgItemSelect="${msgItemSelect}"/>
					</tbody>
				</table>
			</div>
		</div>
		</c:if>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>