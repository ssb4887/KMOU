<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
	<spring:message var="msgViewOpinion" code="item.poll.view.opinion"/>
	<div id="cms_poll_article">
		<div class="poll_box fn_poll_result">
			<div class="wrap">
				<div class="poll_ing">
					<dl class="p">
						<dt><itui:objectView itemId="title" itemInfo="${itemInfo}" objDt="${dt}"/></dt>
						<c:if test="${!empty dt.LIMIT_DATE11 || !empty dt.LIMIT_DATE21}">
						<dd class="date_l"><span class="tlt">설문기간 : </span><span><itui:objectView itemId="limitDate" itemInfo="${itemInfo}" objDt="${dt}"/></span></dd>
						</c:if>
						<dd class="num"><span class="tlt">참여인원 : </span><span><c:out value="${dt.RESP_CNT}"/> 명</span></dd>
						<c:if test="${!empty dt.CONTENTS}">
						<dd class="cont"><itui:objectView itemId="contents" itemInfo="${itemInfo}" objDt="${dt}"/></dd>
						</c:if>
					</dl>
					<c:set var="quesItemInfo" value="${moduleItem.ques_item_info}"/>
					<itui:objectItemName var="quesContentsItemName" itemInfo="${quesItemInfo}" itemId="questionContents"/>
					<c:set var="quesNum" value="${0}"/>
					<ul class="poll_list<c:if test="${ISQUESTYPE}"> fn_allOne</c:if>">
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
							<input type="hidden" name="questionIdx" id="questionIdx${q.count}" value="${listQIdx}" />
							<input type="hidden" name="answerType" id="answerType${q.count}" value="${quesDt.ANSWER_TYPE}" />
							<div class="q<c:if test="${ISQUESTYPE}"> hidden</c:if>">
								<span class="num">${elfn:getIntLPAD(q.count, '0', 2)}</span>
								<c:if test="${!ISQUESTYPE}">
								<itui:objectView itemId="questionContents" itemInfo="${quesItemInfo}" objDt="${quesDt}"/>
								</c:if>
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
												<thead<c:if test="${ISQUESTYPE && !q.first}"> class="hidden"</c:if>>
													<tr>
														<th>${quesContentsItemName}</th>
														<c:set var="itemList" value="${itemMap[listQIdx]}"/>
														<c:forEach items="${itemList}" var="itemDt" varStatus="j">
														<th><c:out value="${itemDt.CONTENTS}"/></th>
														</c:forEach>
													</tr>
												</thead>
												<tbody>
												<c:set var="inquesKey" value="${inclassDt.QUESTION_IDX},${inclassDt.CLASS_IDX}"/>
												<c:set var="inquesList" value="${inquesMap[inquesKey]}"/>
												<c:if test="${!ISQUESTYPE}"><c:set var="quesNum" value="${0}"/></c:if>
												<c:forEach var="inquesDt" items="${inquesList}" varStatus="k">
													<c:set var="quesNum" value="${quesNum + 1}"/>
													<tr>
														<th>
														${quesNum}. <c:out value="${inquesDt.CONTENTS}"/>
														<input type="hidden" name="questionIdx" value="${inquesDt.QUESTION_IDX}" />
														</th>
														<c:set var="listQIdx" value="${inquesDt.QUESTION_IDX}"/>
														<c:set var="itemList" value="${itemMap[listQIdx]}"/>
														<c:forEach items="${itemList}" var="itemDt" varStatus="j">
														<c:set var="itemPercent" value="0" />
														<c:if test="${dt.RESP_CNT > 0}">
															<fmt:formatNumber maxFractionDigits="1" var="itemPercent" value="${elfn:getPercent(itemDt.RESP_CNT, dt.RESP_CNT)}" />
														</c:if>
														<td>${itemPercent}%</td>
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
												<c:set var="itemPercent" value="0" />
												<c:if test="${dt.RESP_CNT > 0}">
													<fmt:formatNumber maxFractionDigits="1" var="itemPercent" value="${elfn:getPercent(itemDt.RESP_CNT, dt.RESP_CNT)}" />
												</c:if>
												<li>
													<c:out value="${itemDt.CONTENTS}"/>
													<c:if test="${itemDt.ISTEXT == '1'}">
														<!-- 기타의견 -->
														<button type="button" class="btnTypeT fn_btn_view_opinion" value="${URL_RESCONT_LIST}&pollIdx=<c:out value="${param.pollIdx}"/>&quesIdx=<c:out value="${listQIdx}"/>&itemIdx=<c:out value="${itemDt.ITEM_IDX}"/>"><c:out value="${msgViewOpinion}"/></button>
													</c:if>
													<div class="graph">
														<div>
															<p class="bar" style="width:${itemPercent}%;"><span><c:out value="${itemPercent}"/>%</span></p>
														</div>
													</div>
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
										<c:set var="itemContents" value="${itemDt.CONTENTS}"/>
										<c:choose>
											<c:when test="${!empty itemContents}">
												<c:set var="itemContentsTitle" value="${itemContents}"/>
											</c:when>
											<c:otherwise>
												<spring:message var="itemContentsTitle" code="item.poll.opn.name"/>
											</c:otherwise>
										</c:choose>
										<li>
											<c:if test="${!empty itemContents}"><c:out value="${itemContents}"/> </c:if>
											<button type="button" class="btnTypeT fn_btn_view_opinion" value="${URL_RESCONT_LIST}&pollIdx=<c:out value="${param.pollIdx}"/>&quesIdx=<c:out value="${listQIdx}"/>&itemIdx=<c:out value="${itemDt.ITEM_IDX}"/>"><c:out value="${msgViewOpinion}"/></button>
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
				</div>
			</div>
		</div>
		<div class="btnCenter" id="wrap_poll_submit_btn">
			<a href="${URL_LIST}" class="fn_btn_list btnTypeB">목록</a>
		</div>
	</div>
	<div id="fn_resultCon_dialog" style="display:none; width:500px; height:100px;">
		<iframe src="about:blank" id="fn_resultCon_iframe" name="fn_resultCon_iframe" frameborder="0" marginwidth="0" marginheight="0" scrolling="yes" style="width:100%; height:100%"></iframe>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>