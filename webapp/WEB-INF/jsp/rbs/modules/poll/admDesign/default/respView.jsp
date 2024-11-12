<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="quesItemInfo" value="${moduleItem.ques_item_info}"/>
<c:set var="resultItemInfo" value="${moduleItem.result_item_info}"/>
<c:set var="inputFormId" value="fn_pollResultForm"/>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/respView.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_poll_article">
		<div class="poll_box">
			<div class="wrap">
				<div class="poll_ing" id="fn_poll_result">
					<dl class="p">
						<dt><itui:objectView itemId="title" itemInfo="${itemInfo}" objDt="${dt}"/></dt>
						<c:if test="${!empty dt.LIMIT_DATE11 || !empty dt.LIMIT_DATE21}">
						<dd class="date_l"><span class="tlt">설문기간 : </span><span><itui:objectView itemId="limitDate" itemInfo="${itemInfo}" objDt="${dt}"/></span></dd>
						</c:if>
						<dd class="num"><span class="tlt"><spring:message code="item.poll.joiner"/> : </span><span><c:out value="${elfn:memberItemOrgValue('mbrName', dt.REGI_NAME)}"/><c:if test="${!empty dt.REGI_ID}">(<c:out value="${elfn:memberItemOrgValue('mbrId', dt.REGI_ID)}"/>)</c:if></span></dd>
						<c:if test="${!empty dt.CONTENTS}">
						<dd class="cont"><itui:objectView itemId="contents" itemInfo="${itemInfo}" objDt="${dt}"/></dd>
						</c:if>
						<c:if test="${!empty questypeResultList}">
							<c:set var="questypeResultEIdx" value="${fn:length(questypeResultList) - 1}"/>
							<c:set var="questypeMaxSum" value="${-1}"/>
							<c:forEach var="listDt" items="${questypeResultList}" varStatus="i">
								<c:if test="${listDt.POINT_SUM > 0 && questypeMaxSum <= listDt.POINT_SUM}">
									<c:if test="${!i.first}"><c:set var="questypeNames" value="${questypeNames}, "/></c:if>
									<c:set var="questypeNames" value="${questypeNames}${listDt.CONTENTS}"/>
									<c:set var="questypeResults">
									${questypeResults}
									<dd class="r cont">
										<dl>
											<dt><c:out value="${listDt.CONTENTS}"/></dt>
											<dd><itui:objectView itemId="resultContents" itemInfo="${quesItemInfo}" objDt="${listDt}"/></dd>
										</dl>
									</dd>
									</c:set>
									<c:set var="questypeMaxSum" value="${listDt.POINT_SUM}"/>
								</c:if>
							</c:forEach>
						</c:if>
						<c:if test="${!empty dt.MNG_COMMENT || !empty questypeNames}">
						<dt class="r">결과</dt>
						</c:if>
						<c:if test="${!empty questypeNames}">
						<dd class="r tlt"><c:out value="${elfn:memberItemOrgValue('mbrName', dt.REGI_NAME)}"/>님의 유형은 <strong><c:out value="${questypeNames}"/></strong>입니다.</dd>
						${questypeResults}
						</c:if>
						<c:if test="${settingInfo.use_reply == '1' && !empty dt.MNG_COMMENT}">
						<dt class="r">추가의견</dt>
						<dd class="r cont"><itui:objectView itemId="mngComment" itemInfo="${resultItemInfo}" objDt="${dt}"/></dd>
						</c:if>
					</dl>
					<c:set var="quesItemInfo" value="${moduleItem.ques_item_info}"/>
					<itui:objectItemName var="quesContentsItemName" itemInfo="${quesItemInfo}" itemId="questionContents"/>
					<ul class="poll_list">
						<c:forEach var="quesDt" items="${quesList}" varStatus="q">
							<c:set var="listQIdx" value="${quesDt.QUESTION_IDX}"/>
							<c:choose>
								<c:when test="${quesDt.ITEM_TYPE == '1'}">
									<c:set var="objItemType" value="radio"/>
								</c:when>
								<c:otherwise>
									<c:set var="objItemType" value="checkbox"/>
								</c:otherwise>
							</c:choose>
						<li<c:if test="${!empty quesDt.REL_QUES_IDX && quesDt.REL_QUES_IDX != 0}"> class="relQues relQues<c:out value="${quesDt.REL_QUES_IDX}"/>"</c:if> id="fn_ques${listQIdx}"<c:if test="${!empty quesDt.REL_QUES_IDX && quesDt.REL_QUES_IDX != 0 && !empty quesDt.REL_ITEM_IDX}"> data-relitems="${quesDt.REL_ITEM_IDX}"</c:if>>
							<div class="q">
								<span class="num">${elfn:getIntLPAD(q.count, '0', 2)}</span>
								<itui:objectView itemId="questionContents" itemInfo="${quesItemInfo}" objDt="${quesDt}"/>
							</div>
							<div class="a">
							<c:choose>
								<c:when test="${quesDt.ANSWER_TYPE == '1'}">
									<%/*객관식 begin*/ %>
									<c:choose>
										<c:when test="${quesDt.USE_INQUES == '1'}">
											<%/*내부문항 begin*/ %>
											<c:set var="inclassList" value="${inclassMap[listQIdx]}"/>
											<c:forEach var="inclassDt" items="${inclassList}" varStatus="ic">
											<c:if test="${!empty inclassDt.CLASS_NAME}"><div class="inclassTitle"><c:out value="${inclassDt.CLASS_NAME}"/></div></c:if>
											<table class="inQuesTable">
												<thead>
													<tr>
														<th>${quesContentsItemName}</th>
														<c:if test="${!ISPOLLITEM}">
															<c:set var="itemList" value="${itemMap[listQIdx]}"/>
														</c:if>
														<c:forEach items="${itemList}" var="itemDt" varStatus="j">
														<th><c:out value="${itemDt.CONTENTS}"/></th>
														</c:forEach>
													</tr>
												</thead>
												<tbody>
												<c:set var="inquesKey" value="${inclassDt.QUESTION_IDX},${inclassDt.CLASS_IDX}"/>
												<c:set var="inquesList" value="${inquesMap[inquesKey]}"/>
												<c:forEach var="inquesDt" items="${inquesList}" varStatus="k">
													<tr>
														<th>
														${k.count}. <c:out value="${inquesDt.CONTENTS}"/>
														<input type="hidden" name="questionIdx" value="${inquesDt.QUESTION_IDX}" />
														</th>
														<c:if test="${!ISPOLLITEM}">
															<c:set var="itemList" value="${itemMap[listQIdx]}"/>
														</c:if>
														<c:forEach items="${itemList}" var="itemDt" varStatus="j">
														<c:set var="resultKeyName" value="${inquesDt.QUESTION_IDX},${itemDt.ITEM_IDX}"/>
														<c:set var="resultDt" value="${resultMap[resultKeyName]}"/>
														<td<c:if test="${!empty resultDt.ITEM_IDX}"> class="checked"</c:if>>
															<c:if test="${!empty resultDt.ITEM_IDX}"><span class="bul">선택</span></c:if>
														</td>
														</c:forEach>
													</tr>
												</c:forEach>
												</tbody>
											</table>
											</c:forEach>
											<%/*내부문항 end*/ %>
										</c:when>
										<c:otherwise>
											<ul>
												<c:forEach var="itemDt" items="${itemMap[listQIdx]}" varStatus="j">
												<c:set var="resultKeyName" value="${listQIdx},${itemDt.ITEM_IDX}"/>
												<c:set var="resultDt" value="${resultMap[resultKeyName]}"/>
												<li<c:if test="${!empty resultDt.ITEM_IDX}"> class="checked"</c:if>>
												<c:out value="${itemDt.CONTENTS}"/><c:if test="${!empty resultDt.ITEM_IDX}"> <span class="bul">선택</span></c:if>
												<c:if test="${itemDt.ISTEXT == '1'}">
													<!-- 기타의견 -->
													<c:out value="${resultDt.CONTENTS}"/>
												</c:if>
												</li>
												</c:forEach>
											</ul>
										</c:otherwise>
									</c:choose>
									<%/*객관식 end*/ %>
								</c:when>
								<c:otherwise>
									<%/*주관식 begin*/ %>
									<ul>
									<c:forEach var="itemDt" items="${itemMap[listQIdx]}" varStatus="j">
										<c:set var="resultKeyName" value="${listQIdx},${itemDt.ITEM_IDX}"/>
										<c:set var="resultDt" value="${resultMap[resultKeyName]}"/>
										<c:set var="itemContents" value="${itemDt.CONTENTS}"/>
										<c:choose>
											<c:when test="${!empty itemContents}">
												<c:set var="itemContentsTitle" value="${itemContents}"/>
											</c:when>
											<c:otherwise>
												<spring:message var="itemContentsTitle" code="item.poll.opn.name"/>
											</c:otherwise>
										</c:choose>
										<li<c:if test="${!empty resultDt.CONTENTS}"> class="checked"</c:if>>
											<c:if test="${!empty itemContents}"><span class="tlt"><c:out value="${itemContents}"/> : </span></c:if>
											<c:if test="${!empty resultDt.CONTENTS}"><span><c:out value="${resultDt.CONTENTS}"/></span></c:if>
										</li>
									</c:forEach>
									</ul>
									<%/*주관식 end*/ %>
								</c:otherwise>
							</c:choose>
							</div>
						</li>
						</c:forEach>
					</ul>
					<div class="btnCenter" id="wrap_poll_submit_btn">
						<c:if test="${settingInfo.use_reply == '1'}">
							<button type="button" class="fn_btn_result btnTypeA">의견등록</button>
						</c:if>
						<a href="${URL_LIST}" class="fn_btn_list btnTypeB">목록</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${settingInfo.use_reply == '1'}">
	<div id="fn_resultCon_dialog" style="display:none; width:700px; height:100px;text-align:center;">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_RESULTINPUTPROC}"/>&respIdx=<c:out value="${param.respIdx}"/>" target="submit_target">
			<itui:objectTextarea itemId="mngComment" itemInfo="${resultItemInfo}" objStyle="margin:20px;width:700px;height:300px;" objDt="${dt}"/>
			<div class="btnCenter">
				<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
				<input type="button" title="취소" class="btnTypeB fn_btn_reset" value="취소"/>
			</div>
		</form>
	</div>
	</c:if>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>