<%@tag import="java.util.Calendar"%>
<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ attribute name="itemListSearch" type="net.sf.json.JSONObject"%>
<%@ attribute name="searchOptnHashMap" type="java.util.HashMap"%>
<%@ attribute name="isUseRadio" type="java.lang.Boolean"%>
			<spring:message var="itemSearchPreFlag" code="Globals.item.search.pre.flag"/>
			<c:if test="${!empty itemListSearch}">
				<c:set var="itemSearchOrder" value="${itemListSearch.searchForm_order}"/>
				<c:if test="${!empty itemSearchOrder}">
					<c:set var="keyItemName" value="key"/>
					<c:set var="keyFieldItemName" value="keyField"/>
					<c:set var="keyInfo" value="${itemListSearch[keyItemName]}"/>
					<c:set var="keyFieldInfo" value="${itemListSearch[keyFieldItemName]}"/>
					<c:forEach var="itemSearchId" items="${itemSearchOrder}">
						<c:choose>
							<c:when test="${itemSearchId == keyFieldItemName}">
								<c:if test="${!empty keyFieldInfo && !empty keyFieldInfo.select_name && !empty keyFieldInfo.query_name && !empty keyFieldInfo.items && !empty keyFieldInfo.select_order}">
								<!-- 옵션 -->
							<dt id="fn_dt_${keyFieldInfo.select_name}"><label for="${keyFieldInfo.select_name}">검색</label></dt>
							<dd<c:if test="${keyFieldInfo.del_full_class != 1}"> class="full"</c:if>>
							<select name="${keyFieldInfo.select_name}" id="${keyFieldInfo.select_name}" class="select" title="검색옵션">
							<c:forEach var="itemKeyFieldId" items="${keyFieldInfo.select_order}">
								<c:set var="keyItem" value="${keyFieldInfo.items[itemKeyFieldId]}"/>
								<c:set var="itemKeyFieldName" value="${elfn:getItemName(keyItem)}"/>
								<option value="${itemKeyFieldId}"<c:if test="${queryString[keyFieldInfo.select_name] == itemKeyFieldId}"> selected="selected"</c:if>>${itemKeyFieldName}</option>
							</c:forEach>
							</select>
			                <input type="text" name="${keyFieldInfo.query_name}" id="${keyFieldInfo.query_name}" value="<c:out value="${queryString[keyFieldInfo.query_name]}"/>" class="type-text" title="검색어"/>
							
							</dd>
								</c:if>
							</c:when>
							<c:otherwise>
								<c:if test="${!empty keyInfo && !empty keyInfo.items}">
									<!-- 단일 -->
									<c:set var="keyItem" value="${keyInfo.items[itemSearchId]}"/>
									<c:if test="${!empty keyItem}">
									<c:set var="itemKeyId" value="${itemSearchPreFlag}${itemSearchId}"/>
									<c:set var="itemKeyName" value="${elfn:getItemName(keyItem)}"/>
									<c:set var="namePt" value=""/>
									<c:set var="margeOrder" value=""/>
									<c:set var="itemLabelName" value="${itemKeyName}"/>
									<c:if test="${!empty keyItem.merge_info && !empty keyItem.merge_info.item_name}">
										<c:set var="namePt" value="${keyItem.merge_info.name_pt}"/>
										<c:set var="margeOrder" value="${keyItem.merge_info.merge_order}"/>
										<c:set var="itemLabelName" value="${keyItem.merge_info.item_name}"/>
									</c:if>
									<c:set var="formatType" value="${keyItem.format_type}"/>
									<c:set var="objectType" value="${keyItem.object_type}"/>
									<c:set var="optionType" value="${keyItem.option_type}"/>
						<dt id="fn_dt_${itemKeyId}"><label for="${itemKeyId}">${itemLabelName}</label></dt>
						<dd<c:if test="${!empty keyItem.class_name}"> class="${keyItem.class_name}"</c:if>>
						<c:if test="${namePt == 1}">${itemKeyName}</c:if>
						<c:choose>
							<c:when test="${formatType >= 5 && formatType <= 8 || formatType >= 15 && formatType <= 18}">
							<% /* 날짜(기간) / 날짜 시:분:초(기간) / 날짜 시:분(기간) / 날짜 시(기간) - 날짜 기간 검색 */ %>
							<c:set var="value1Name" value="${itemKeyId}1"/>
							<c:set var="value2Name" value="${itemKeyId}2"/>
							<input type="text" name="${itemKeyId}1" id="${itemKeyId}1" title="${itemKeyName}" readonly="readonly" style="width:80px;" value="<c:out value="${queryString[value1Name]}"/>"/>
							<button type="button" class="btnCal" id="fn_btn_${itemKeyId}1" title="${msBtnCalSel}">${msBtnCalSel}</button>
							~
							<input type="text" name="${itemKeyId}2" id="${itemKeyId}2" title="${itemKeyName}" readonly="readonly" style="width:80px;" value="<c:out value="${queryString[value2Name]}"/>"/>
							<button type="button" class="btnCal" id="fn_btn_${itemKeyId}2" title="${msBtnCalSel}">${msBtnCalSel}</button>
							</c:when>
							<c:when test="${formatType == 19}">
							<% /* 년도 */ %>
								<%
									Calendar calendar = Calendar.getInstance();
									request.setAttribute("nowYear", calendar.get(Calendar.YEAR));
								%>
								<c:set var="beginYear" value="${keyItem.start_year}"/>
								<c:set var="endYear" value="${nowYear + keyItem.end_addcnt}"/>
								<c:set var="objTitle" value="${itemKeyName}"/>
								<spring:message var="yearObjTitle" code="item.itemId.year.name" arguments="${objTitle}"/>
								<spring:message var="itemYearName" code="item.year.name"/>
								<spring:message var="yearOptTitle" code="item.itemId.select.name" arguments="${itemYearName}"/>
								<select id="${itemKeyId}" name="${itemKeyId}" class="select" style="${objStyle}" title="${yearObjTitle}">
									<option value="">${yearOptTitle}</option>
								<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
									<c:set var="optnYearStr" value="${elfn:toString(optnYear)}"/>
									<option value="<c:out value="${optnYear}"/>"<c:if test="${optnYearStr == queryString[itemKeyId]}"> selected="selected"</c:if>><c:out value="${optnYear}"/></option>
								</c:forEach>
								</select>${itemYearName}
							</c:when>
							<c:when test="${formatType == 20}">
							<% /* 년도 월 */ %>
								<%
									Calendar calendar = Calendar.getInstance();
									request.setAttribute("nowYear", calendar.get(Calendar.YEAR));
								%>
								<c:set var="value1Name" value="${itemKeyId}1"/>
								<c:set var="value2Name" value="${itemKeyId}2"/>
								<c:set var="beginYear" value="${keyItem.start_year}"/>
								<c:set var="endYear" value="${nowYear + keyItem.end_addcnt}"/>
								<c:set var="objTitle" value="${itemKeyName}"/>
								<spring:message var="yearObjTitle" code="item.itemId.year.name" arguments="${objTitle}"/>
								<spring:message var="itemYearName" code="item.year.name"/>
								<spring:message var="yearOptTitle" code="item.itemId.select.name" arguments="${itemYearName}"/>
								<spring:message var="monthObjTitle" code="item.itemId.month.name" arguments="${objTitle}"/>
								<spring:message var="itemMonthName" code="item.month.name"/>
								<spring:message var="monthOptTitle" code="item.itemId.select.name" arguments="${itemMonthName}"/>
								<select id="${itemKeyId}1" name="${itemKeyId}1" class="select" style="${objStyle}" title="${yearObjTitle}">
									<option value="">${yearOptTitle}</option>
								<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
									<c:set var="optnYearStr" value="${elfn:toString(optnYear)}"/>
									<option value="<c:out value="${optnYear}"/>"<c:if test="${optnYearStr == queryString[value1Name]}"> selected="selected"</c:if>><c:out value="${optnYear}"/></option>
								</c:forEach>
								</select>${itemYearName}&nbsp;
								<select id="${itemKeyId}2" name="${itemKeyId}2" class="select" style="${objStyle}" title="${monthObjTitle}">
									<option value="">${monthOptTitle}</option>
								<c:forEach var="optnMonth" begin="1" end="12">
									<c:set var="optnMonthStr" value="${elfn:getIntLPAD(optnMonth, '0', 2)}"/>
									<option value="<c:out value="${optnMonthStr}"/>"<c:if test="${optnMonthStr == queryString[value2Name]}"> selected="selected"</c:if>><c:out value="${optnMonthStr}"/></option>
								</c:forEach>
								</select>${itemMonthName}
							</c:when>
							<c:when test="${formatType == 21}">
							<% /* 년도 분기 */ %>
								<%
									Calendar calendar = Calendar.getInstance();
									request.setAttribute("nowYear", calendar.get(Calendar.YEAR));
								%>
								<c:set var="value1Name" value="${itemKeyId}1"/>
								<c:set var="value2Name" value="${itemKeyId}2"/>
								<c:set var="beginYear" value="${keyItem.start_year}"/>
								<c:set var="endYear" value="${nowYear + keyItem.end_addcnt}"/>
								<c:set var="objTitle" value="${itemKeyName}"/>
								<spring:message var="yearObjTitle" code="item.itemId.year.name" arguments="${objTitle}"/>
								<spring:message var="itemYearName" code="item.year.name"/>
								<spring:message var="yearOptTitle" code="item.itemId.select.name" arguments="${itemYearName}"/>
								<spring:message var="quartObjTitle" code="item.itemId.quarterly.name" arguments="${objTitle}"/>
								<spring:message var="itemQuartName" code="item.quarterly.name"/>
								<spring:message var="quartOptTitle" code="item.itemId.select.name" arguments="${itemQuartName}"/>
								<select id="${itemKeyId}1" name="${itemKeyId}1" class="select" style="${objStyle}" title="${yearObjTitle}">
									<option value="">${yearOptTitle}</option>
								<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
									<c:set var="optnYearStr" value="${elfn:toString(optnYear)}"/>
									<option value="<c:out value="${optnYear}"/>"<c:if test="${optnYearStr == queryString[value1Name]}"> selected="selected"</c:if>><c:out value="${optnYear}"/></option>
								</c:forEach>
								</select>${itemYearName}&nbsp;
								<c:set var="optnOptList" value="${searchOptnHashMap[keyItem['master_code']]}"/>
								<select id="${itemKeyId}2" name="${itemKeyId}2" class="select" style="${objStyle}" title="${quartObjTitle}">
									<option value="">${quartOptTitle}</option>
									<c:forEach var="optnDt" items="${optnOptList}">
									<option value="${optnDt.OPTION_CODE}"<c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if><c:if test="${queryString[value2Name] == optnDt.OPTION_CODE}"> selected="selected"</c:if>>${optnDt.OPTION_NAME}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:when test="${formatType == 22}">
							<% /* 년도 월 일 */ %>
								<%
									Calendar calendar = Calendar.getInstance();
									request.setAttribute("nowYear", calendar.get(Calendar.YEAR));
								%>
								<c:set var="value1Name" value="${itemKeyId}1"/>
								<c:set var="value2Name" value="${itemKeyId}2"/>
								<c:set var="value3Name" value="${itemKeyId}3"/>
								<c:set var="beginYear" value="${keyItem.start_year}"/>
								<c:set var="endYear" value="${nowYear + keyItem.end_addcnt}"/>
								<c:set var="objTitle" value="${itemKeyName}"/>
								<spring:message var="yearObjTitle" code="item.itemId.year.name" arguments="${objTitle}"/>
								<spring:message var="itemYearName" code="item.year.name"/>
								<spring:message var="yearOptTitle" code="item.itemId.select.name" arguments="${itemYearName}"/>
								<spring:message var="monthObjTitle" code="item.itemId.month.name" arguments="${objTitle}"/>
								<spring:message var="itemMonthName" code="item.month.name"/>
								<spring:message var="monthOptTitle" code="item.itemId.select.name" arguments="${itemMonthName}"/>
								<spring:message var="dateObjTitle" code="item.itemId.date.name" arguments="${objTitle}"/>
								<spring:message var="itemDateName" code="item.date.name"/>
								<spring:message var="dateOptTitle" code="item.itemId.select.name" arguments="${itemDateName}"/>
								<select id="${itemKeyId}1" name="${itemKeyId}1" class="select" style="${objStyle}" title="${yearObjTitle}">
									<option value="">${yearOptTitle}</option>
								<c:forEach var="optnYear" begin="${beginYear}" end="${endYear}">
									<c:set var="optnYearStr" value="${elfn:toString(optnYear)}"/>
									<option value="<c:out value="${optnYear}"/>"<c:if test="${optnYearStr == queryString[value1Name]}"> selected="selected"</c:if>><c:out value="${optnYear}"/></option>
								</c:forEach>
								</select><label for="${itemKeyId}1">${itemYearName}</label>
								<select id="${itemKeyId}2" name="${itemKeyId}2" class="select" style="${objStyle}" title="${monthObjTitle}">
									<option value="">${monthOptTitle}</option>
								<c:forEach var="optnMonth" begin="1" end="12">
									<c:set var="optnMonthStr" value="${elfn:getIntLPAD(optnMonth, '0', 2)}"/>
									<option value="<c:out value="${optnMonthStr}"/>"<c:if test="${optnMonthStr == queryString[value2Name]}"> selected="selected"</c:if>><c:out value="${optnMonthStr}"/></option>
								</c:forEach>
								</select><label for="${itemKeyId}2">${itemMonthName}</label>
								<select id="${itemKeyId}3" name="${itemKeyId}3" class="select" style="${objStyle}" title="${dateObjTitle}">
									<option value="">${dateOptTitle}</option>
								<c:forEach var="optnDate" begin="1" end="31">
									<c:set var="optnDateStr" value="${elfn:getIntLPAD(optnDate, '0', 2)}"/>
									<option value="<c:out value="${optnDateStr}"/>"<c:if test="${optnDateStr == queryString[value3Name]}"> selected="selected"</c:if>><c:out value="${optnDateStr}"/></option>
								</c:forEach>
								</select><label for="${itemKeyId}3">${itemDateName}</label>
							</c:when>
							<c:when test="${isUseRadio && formatType == 0 && objectType == 5}">
							<% /* radio */ %>
							<c:choose>
								<c:when test="${!empty keyItem.option_type && keyItem.option_type != 0}"><c:set var="optnOptList" value="${searchOptnHashMap[itemSearchId]}"/></c:when>
								<c:otherwise><c:set var="optnOptList" value="${searchOptnHashMap[keyItem['master_code']]}"/></c:otherwise>
							</c:choose>
							<c:forEach var="optnDt" items="${optnOptList}" varStatus="oi">
							<input type="radio" name="${itemKeyId}" id="${itemKeyId}${oi.count}" value="${optnDt.OPTION_CODE}"<c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if><c:if test="${queryString[itemKeyId] == optnDt.OPTION_CODE}"> checked="checked"</c:if>/><label for="${itemKeyId}${oi.count}">${optnDt.OPTION_NAME}</label>
							</c:forEach>
							</c:when>
							<c:when test="${formatType == 0 && (objectType >= 2 && objectType <= 5 || objectType == 14)}">
							<% /* select */ %>
							<c:choose>
								<c:when test="${!empty keyItem.option_type && keyItem.option_type != 0}"><c:set var="optnOptList" value="${searchOptnHashMap[itemSearchId]}"/></c:when>
								<c:otherwise><c:set var="optnOptList" value="${searchOptnHashMap[keyItem['master_code']]}"/></c:otherwise>
							</c:choose>
							<select name="${itemKeyId}" id="${itemKeyId}" class="select" title="${itemKeyName}"<c:if test="${!empty keyItem.style_width}"> style="width:${keyItem.style_width}px;"</c:if>>
								<option value="">${itemKeyName} 선택</option>
								<c:forEach var="optnDt" items="${optnOptList}">
								<option value="${optnDt.OPTION_CODE}"<c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if><c:if test="${queryString[itemKeyId] == optnDt.OPTION_CODE}"> selected="selected"</c:if>><c:if test="${optionType == 3}"><c:if test="${optnDt.OPTION_LEVEL > 1}"><c:forEach var="optLevel" begin="2" end="${optnDt.OPTION_LEVEL}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if></c:if>${optnDt.OPTION_NAME}</option>
								</c:forEach>
								<c:if test="${objectType == 14 && empty optnOptList}">
								<option value="0"<c:if test="${queryString[itemKeyId] == '0'}"> selected="selected"</c:if>><spring:message code="item.no.use.name"/></option>
								<option value="1"<c:if test="${queryString[itemKeyId] == '1'}"> selected="selected"</c:if>><spring:message code="item.use.name"/></option>
								</c:if>
							</select>
							</c:when>
							<c:when test="${formatType == 0 && objectType == 22}">
							<% /* 3차 select */ %>
								<c:set var="optMaxLevelKey" value="_class_${keyItem['master_code']}_max_level"/>
								<c:set var="optListKey" value="_class_${keyItem['master_code']}"/>
							<c:set var="optnOptList" value="${searchOptnHashMap[optListKey]}"/>
							<select name="${itemKeyId}" id="${itemKeyId}" class="select t_select_full" title="${itemKeyName}"<c:if test="${!empty keyItem.style_width}"> style="width:${keyItem.style_width}px;"</c:if>>
								<option value="">${itemKeyName} 선택</option>
								<c:forEach var="optnDt" items="${optnOptList}">
								<option value="${optnDt.OPTION_CODE}"<c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if><c:if test="${queryString[itemKeyId] == optnDt.OPTION_CODE}"> selected="selected"</c:if>><c:if test="${optnDt.OPTION_LEVEL > 1}"><c:forEach var="optLevel" begin="2" end="${optnDt.OPTION_LEVEL}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if>${optnDt.OPTION_NAME}</option>
								</c:forEach>
							</select>
							</c:when>
							<c:otherwise>
								<input type="text" name="${itemKeyId}" id="${itemKeyId}" title="${itemKeyName}"<c:if test="${!empty keyItem.style_width}"> style="width:${keyItem.style_width}px;"</c:if> value="<c:out value="${queryString[itemKeyId]}"/>"/>
							</c:otherwise>
						</c:choose>
						<c:if test="${namePt == 2}">${itemKeyName}&nbsp;</c:if>
						
						<c:forEach var="mergeId" items="${margeOrder}">
							<c:set var="keyItem" value="${keyInfo.items[mergeId]}"/>
							<c:set var="itemKeyId" value="${itemSearchPreFlag}${mergeId}"/>
							<c:set var="itemKeyName" value="${keyItem.item_name}"/>
							<c:if test="${namePt == 1}">${itemKeyName}</c:if>
							<c:choose>
							<c:when test="${formatType == 0 && (objectType >= 2 && objectType <= 5 || objectType == 14)}">
							<c:choose>
								<c:when test="${!empty keyItem.option_type && keyItem.option_type != 0}"><c:set var="optnOptList" value="${searchOptnHashMap[mergeId]}"/></c:when>
								<c:otherwise><c:set var="optnOptList" value="${searchOptnHashMap[keyItem['master_code']]}"/></c:otherwise>
							</c:choose>
							<select name="${itemKeyId}" id="${itemKeyId}" class="select" title="${itemKeyName}">
								<option value="">${itemKeyName} 선택</option>
								<c:forEach var="optnDt" items="${optnOptList}">
								<option value="${optnDt.OPTION_CODE}"<c:if test="${!empty optnDt.P_OPT_CD}"> data-p="<c:out value="${optnDt.P_OPT_CD}"/>"</c:if><c:if test="${queryString[itemKeyId] == optnDt.OPTION_CODE}"> selected="selected"</c:if>>${optnDt.OPTION_NAME}</option>
								</c:forEach>
								<c:if test="${objectType == 14 && empty optnOptList}">
								<option value="0"<c:if test="${queryString[itemKeyId] == '0'}"> selected="selected"</c:if>><spring:message code="item.no.use.name"/></option>
								<option value="1"<c:if test="${queryString[itemKeyId] == '1'}"> selected="selected"</c:if>><spring:message code="item.use.name"/></option>
								</c:if>
							</select>
							</c:when>
							<c:otherwise>
								<input type="text" name="${itemKeyId}" id="${itemKeyId}" title="${itemKeyName}"<c:if test="${!empty keyItem.style_width}"> style="width:${keyItem.style_width}px;"</c:if> value="<c:out value="${queryString[itemKeyId]}"/>"/>
							</c:otherwise>
						</c:choose>
							<c:if test="${namePt == 2}">${itemKeyName}&nbsp;</c:if>
						</c:forEach>
						</dd>
								</c:if>
								</c:if>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:if>
			</c:if>