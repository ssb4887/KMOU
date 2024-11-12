					<input type="hidden" id="iscomp" name="iscomp"/>
					<dl class="p">
						<dt><itui:objectView itemId="title" itemInfo="${itemInfo}" objDt="${ingDt}"/></dt>
						<c:if test="${!empty dt.LIMIT_DATE11 || !empty dt.LIMIT_DATE21}">
						<dd class="date"><span class="tlt">설문기간 : </span><span><itui:objectView itemId="limitDate" itemInfo="${itemInfo}" objDt="${ingDt}"/></span></dd>
						</c:if>
						<dd class="img"><itui:objectImgView itemId="img" itemInfo="${itemInfo}" objDt="${ingDt}"/></dd>
						<c:if test="${!empty ingDt.CONTENTS}">
						<dd class="cont"><itui:objectView itemId="contents" itemInfo="${itemInfo}" objDt="${ingDt}"/></dd>
						</c:if>
					</dl>
					<c:set var="quesItemInfo" value="${moduleItem.ques_item_info}"/>
					<itui:objectItemName var="quesContentsItemName" itemInfo="${quesItemInfo}" itemId="questionContents"/>
					<c:set var="quesNum" value="${0}"/>
					<ul class="poll_list<c:if test="${ISQUESTYPE && ISPOLLITEM}"> fn_allOne</c:if>">
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
												<thead<c:if test="${ISQUESTYPE && ISPOLLITEM && !q.first}"> class="hidden"</c:if>>
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
														<c:forEach items="${itemList}" var="itemDt" varStatus="j">
														<c:set var="resultKeyName" value="${inquesDt.QUESTION_IDX},${itemDt.ITEM_IDX}"/>
														<c:set var="resultDt" value="${resultMap[resultKeyName]}"/>
														<td>
															<label for="itemIdx_${inquesDt.QUESTION_IDX}_${itemDt.ITEM_IDX}" class="radio-inline">
																<input class="ques_items" data-ques="${inquesDt.QUESTION_IDX}" type="${objItemType}" name="itemIdx_${inquesDt.QUESTION_IDX}" id="itemIdx_${inquesDt.QUESTION_IDX}_${itemDt.ITEM_IDX}" value="${itemDt.ITEM_IDX}"<c:if test="${!empty resultDt.ITEM_IDX}"> checked="checked"</c:if> />
															</label>
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
												<li>
													<label for="itemIdx_${listQIdx}_${itemDt.ITEM_IDX}" class="radio-inline">
														<input class="ques_items" data-ques="${listQIdx}" data-istext="${itemDt.ISTEXT}" type="${objItemType}" name="itemIdx_${listQIdx}" id="itemIdx_${listQIdx}_${itemDt.ITEM_IDX}" value="${itemDt.ITEM_IDX}"<c:if test="${!empty resultDt.ITEM_IDX}"> checked="checked"</c:if> /> <c:out value="${itemDt.CONTENTS}"/>
													</label>
												<c:if test="${itemDt.ISTEXT == '1'}">
													<!-- 기타의견 -->
													<input type="text" name="istext_contents_${listQIdx}_${itemDt.ITEM_IDX}" id="istext_contents_${listQIdx}_${itemDt.ITEM_IDX}" title="<c:out value="${itemDt.CONTENTS}"/>" value="<c:out value="${resultDt.CONTENTS}"/>" class="inputTxt"/>
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
										<li>
											<c:set var="objItemWidth" value="95%"/>
											<c:if test="${!empty itemContents}"><c:set var="objItemWidth" value="250px"/><c:out value="${itemContents}"/> : </c:if>
											<input type="hidden" data-ques="${listQIdx}" class="ques_items_h" name="itemIdx_${listQIdx}" id="itemIdx_${listQIdx}_${itemDt.ITEM_IDX}" value="${itemDt.ITEM_IDX}" />
											<input type="text" name="istext_contents_${listQIdx}_${itemDt.ITEM_IDX}" id="istext_contents_${listQIdx}_${itemDt.ITEM_IDX}" title="<c:out value="${itemContentsTitle}"/>" value="<c:out value="${resultDt.CONTENTS}"/>" class="inputTxt" style="width:${objItemWidth};"/>
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