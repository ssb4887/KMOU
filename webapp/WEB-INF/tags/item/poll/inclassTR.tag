<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="pollui" tagdir="/WEB-INF/tags/item/poll" %>
<%@ attribute name="inquesList" type="java.util.List"%>
<%@ attribute name="inclassDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="msgItemInclassName"%>
<%@ attribute name="msgItemInclass"%>
<%@ attribute name="msgItemSelect"%>
<%@ attribute name="msgItemInques"%>
<% /* sitemap */ %>
					<tr>
						<td style="vertical-align:top;padding-top:12px;">
							<input type="checkbox" name="inclassDel" title="<c:out value="${msgItemInclassName}"/>" value="1"/>
						</td>
						<td class="tlt">
							<input type="text" name="inclassContents" title="<c:out value="${msgItemInclass}"/>" class="inputTxt inclassContents" style="width:620px;" value="<c:out value="${inclassDt.CLASS_NAME}"/>"/>
							<div class="fn_inques_list_wp">
								<div class="btnTopFull">
									<div class="left">
										<h6><spring:message code="item.poll.question.inques"/></h6>
										<button type="button" title="추가" class="btnTFW fn_btn_add_inques" data-idx="1">추가</button>
										<button type="button" title="삭제" class="btnTFDL fn_btn_del_inques" data-idx="1">삭제</button>
									</div>
								</div>
								<ul class="fn_inques_list_ul" style="clear:both;">
								<c:if test="${empty inquesList}">
									<pollui:inquesLI inquesDt="${inquesDt}" msgItemSelect="${msgItemSelect}" msgItemInques="${msgItemInques}"/>
								</c:if>
								<c:forEach var="inquesDt" items="${inquesList}">
									<pollui:inquesLI inquesDt="${inquesDt}" msgItemSelect="${msgItemSelect}" msgItemInques="${msgItemInques}"/>
								</c:forEach>
								</ul>
							</div>
						</td>
					</tr>