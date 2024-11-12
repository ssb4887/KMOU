<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="quesItemInfo" value="${moduleItem.ques_item_info}"/>
<c:set var="resultItemInfo" value="${moduleItem.result_item_info}"/>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
	</jsp:include>
</c:if>
	<div id="cms_poll_article">
		<div class="poll_box">
			<div class="wrap">
				<div class="poll_ing" id="fn_poll_result">
					<dl class="p">
						<dt>
							<itui:objectView itemId="title" itemInfo="${itemInfo}" objDt="${dt}"/>
							<c:if test="${settingInfo.use_reply == '1'}">
							Report
							</c:if>
						</dt>
						<c:if test="${!empty dt.LIMIT_DATE11 || !empty dt.LIMIT_DATE21}">
						<dd class="date"><span class="tlt">설문기간 : </span><span><itui:objectView itemId="limitDate" itemInfo="${itemInfo}" objDt="${dt}"/></span></dd>
						</c:if>
						<dd class="img"><itui:objectImgView itemId="img" itemInfo="${itemInfo}" objDt="${dt}"/></dd>
						<c:if test="${settingInfo.use_reply != '1' && !empty dt.CONTENTS}">
						<dd class="cont"><itui:objectView itemId="contents" itemInfo="${itemInfo}" objDt="${dt}"/></dd>
						</c:if>
						<c:if test="${settingInfo.use_reply == '1'}">
						<%/* 결과 답변 사용하는 경우 */ %>
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
						<c:if test="${!empty questypeNames}">
						<dd class="r tlt"><c:out value="${elfn:memberItemOrgValue('mbrName', dt.REGI_NAME)}"/>님의 유형은 <strong><c:out value="${questypeNames}"/></strong>입니다.</dd>
						${questypeResults}
						</c:if>
						<c:if test="${!empty dt.MNG_COMMENT}">
						<dt class="r">추가</dt>
						<dd class="r cont"><itui:objectView itemId="mngComment" itemInfo="${resultItemInfo}" objDt="${dt}"/></dd>
						</c:if>
						</c:if>
					</dl>
					<c:if test="${settingInfo.use_reply != '1'}">
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
														<c:if test="${!ISPOLLITEM}">
															<c:set var="itemList" value="${itemMap[listQIdx]}"/>
														</c:if>
														<c:forEach items="${itemList}" var="itemDt" varStatus="j">
														<th><c:out value="${itemDt.CONTENTS}"/></th>
														</c:forEach>
													</tr>
												</thead>
												<tbody>
												<c:if test="${!ISQUESTYPE}"><c:set var="quesNum" value="${0}"/></c:if>
												<c:set var="inquesKey" value="${inclassDt.QUESTION_IDX},${inclassDt.CLASS_IDX}"/>
												<c:set var="inquesList" value="${inquesMap[inquesKey]}"/>
												<c:forEach var="inquesDt" items="${inquesList}" varStatus="k">
													<c:set var="quesNum" value="${quesNum + 1}"/>
													<tr>
														<th>
														${quesNum}. <c:out value="${inquesDt.CONTENTS}"/>
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
													<c:out value="${itemDt.CONTENTS}"/><c:if test="${!empty resultDt.ITEM_IDX}"> <span>선택</span></c:if>
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
					</c:if>
				</div>
			</div>
		</div>
		<div class="btnCenter" id="wrap_poll_submit_btn">
			<a href="${URL_LIST}" class="fn_btn_list btnTypeB">목록</a>
		</div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if>