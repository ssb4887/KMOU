<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="defVal"%>
<%@ attribute name="objVal" type="java.lang.Object"%>
<%@ attribute name="objNameVal" type="java.lang.Object"%>
<%@ attribute name="isDisplayImg" type="java.lang.Boolean"%>	<%/* 이미지 파일인 경우 display 여부 */%>
<%@ attribute name="imgDisplayType" type="java.lang.Integer"%>	<%/* 이미지항목인 경우 display방식: null,0 - 이미지만 / 1 - 링크만 / 2 - 이미지,링크 둘 다 display */%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<%@ attribute name="optnHashMap" type="java.util.HashMap"%>
<%@ attribute name="multiFileHashMap" type="java.util.HashMap"%>
<%@ attribute name="multiDataHashMap" type="java.util.HashMap"%>
<%@ attribute name="downloadUrl"%>
<%@ attribute name="classSplitStr"%>								<%/* 분류 구분자 */%>
<%@ attribute name="imgDefaultUrl"%>										<%/* 이미지기본경로 */%>
<c:if test="${empty isDisplayImg}">
	<c:set var="isDisplayImg" value="false"/>
</c:if>

<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
<c:if test="${empty optnHashMap}"><c:set var="optnHashMap" value="${optnHashMap}"/></c:if>
<c:set var="itemFormatType" value="${itemObj['format_type']}"/>
<c:set var="itemObjectType" value="${itemObj['object_type']}"/>
<c:set var="itemColumnId" value="${itemObj['column_id']}"/>
<c:set var="nameColumnId" value="${itemObj['name_column_id']}"/>
<c:if test="${empty nameColumnId}"><c:set var="nameColumnId" value="${itemObj['column_id']}_NAME"/></c:if>
<c:set var="usePrivSec" value="${itemObj['use_privsec']}"/>
<c:if test="${objVal == null}"><c:set var="objVal" value="${objDt[itemColumnId]}"/></c:if>
<c:if test="${objNameVal == null}"><c:set var="objNameVal" value="${objDt[nameColumnId]}"/></c:if>
<c:if test="${!empty objVal && usePrivSec == 1}"><c:set var="objVal" value="${elfn:privDecrypt(objVal)}"/></c:if>
<c:choose>
	<c:when test="${!empty itemObj.option_type && itemObj.option_type != 0}"><c:set var="optnOptList" value="${optnHashMap[itemId]}"/></c:when>
	<c:when test="${itemObjectType == 22}">
		<c:set var="classMasterCode" value="_class_${itemObj['master_code']}"/>
		<c:set var="optnOptList" value="${optnHashMap[classMasterCode]}"/>
	</c:when>
	<c:otherwise><c:set var="optnOptList" value="${optnHashMap[itemObj['master_code']]}"/></c:otherwise>
</c:choose>
	<c:choose>
		<c:when test="${itemFormatType == 1 || itemFormatType == 2}">
			<%/* 전화번호, 휴대폰 */%>
			<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
			<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
			<c:set var="itemColumnId3" value="${itemObj['column_id']}3"/>
			<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
			<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
			<c:set var="objVal3" value="${objDt[itemColumnId3]}"/>
			<c:if test="${usePrivSec == 1}">
				<c:set var="objVal1" value="${elfn:privDecrypt(objVal1)}"/>
				<c:set var="objVal2" value="${elfn:privDecrypt(objVal2)}"/>
				<c:set var="objVal3" value="${elfn:privDecrypt(objVal3)}"/>
			</c:if>
			${objVal1}<c:if test="${!empty objVal2}">-${objVal2}</c:if><c:if test="${!empty objVal3}">-${objVal3}</c:if>
		</c:when>
		<c:when test="${itemFormatType == 4}">
			<%/* 주소 */%>
			<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
			<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
			<c:set var="itemColumnId3" value="${itemObj['column_id']}3"/>
			<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
			<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
			<c:set var="objVal3" value="${objDt[itemColumnId3]}"/>
			<c:if test="${usePrivSec == 1}">
				<c:set var="objVal1" value="${elfn:privDecrypt(objVal1)}"/>
				<c:set var="objVal2" value="${elfn:privDecrypt(objVal2)}"/>
				<c:set var="objVal3" value="${elfn:privDecrypt(objVal3)}"/>
			</c:if>
			<c:if test="${!empty objVal1}">(${objVal1})&nbsp;</c:if>${objVal2}&nbsp;${objVal3}
		</c:when>
		<c:when test="${itemFormatType == 5}">
			<%/* 날짜 */%>
			<c:choose>
				<c:when test="${itemObj['date_type'] == 1}"><fmt:formatDate value="${objVal}" pattern="yyyy-MM-dd"/></c:when>
				<c:otherwise><c:out value="${objVal}"/></c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${itemFormatType == 7}">
			<%/* 날짜 시:분 */%>
			<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
			<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
			<c:set var="itemColumnId3" value="${itemObj['column_id']}3"/>
			<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
			<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
			<c:set var="objVal3" value="${objDt[itemColumnId3]}"/>
			${objVal1}<c:if test="${!empty objVal2}">&nbsp;${objVal2}</c:if><c:if test="${!empty objVal3}">:${objVal3}</c:if>
		</c:when>
		<c:when test="${itemFormatType == 8}">
			<%/* 날짜 시 */%>
			<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
			<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
			<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
			<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
			${objVal1}<c:if test="${!empty objVal2}">&nbsp;${objVal2}</c:if>
		</c:when>
		<c:when test="${itemFormatType == 15}">
			<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
			<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
			<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
			<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
			<c:out value="${objVal1}"/><c:if test="${!empty objVal1 || !empty objVal2}">&nbsp;~&nbsp;</c:if><c:out value="${objVal2}"/>
		</c:when>
		<c:when test="${itemFormatType == 17}">
			<%/* 날짜 시:분(기간) */%>
			<c:set var="itemColumnId11" value="${itemObj['column_id']}11"/>
			<c:set var="itemColumnId12" value="${itemObj['column_id']}12"/>
			<c:set var="itemColumnId13" value="${itemObj['column_id']}13"/>
			<c:set var="itemColumnId21" value="${itemObj['column_id']}21"/>
			<c:set var="itemColumnId22" value="${itemObj['column_id']}22"/>
			<c:set var="itemColumnId23" value="${itemObj['column_id']}23"/>
			<c:set var="objVal11" value="${objDt[itemColumnId11]}"/>
			<c:set var="objVal12" value="${objDt[itemColumnId12]}"/>
			<c:set var="objVal13" value="${objDt[itemColumnId13]}"/>
			<c:set var="objVal21" value="${objDt[itemColumnId21]}"/>
			<c:set var="objVal22" value="${objDt[itemColumnId22]}"/>
			<c:set var="objVal23" value="${objDt[itemColumnId23]}"/>
			<c:out value="${objVal11}"/><c:if test="${!empty objVal12}">&nbsp;${objVal12}</c:if><c:if test="${!empty objVal13}">:${objVal13}</c:if><c:if test="${!empty objVal11 || !empty objVal12 || !empty objVal13 || !empty objVal21 || !empty objVal22 || !empty objVal23}">&nbsp;~&nbsp;</c:if><c:out value="${objVal21}"/><c:if test="${!empty objVal22}">&nbsp;${objVal22}</c:if><c:if test="${!empty objVal23}">:${objVal23}</c:if>
		</c:when>
		<c:when test="${itemFormatType == 18}">
			<%/* 날짜 시(기간) */%>
			<c:set var="itemColumnId11" value="${itemObj['column_id']}11"/>
			<c:set var="itemColumnId12" value="${itemObj['column_id']}12"/>
			<c:set var="itemColumnId21" value="${itemObj['column_id']}21"/>
			<c:set var="itemColumnId22" value="${itemObj['column_id']}22"/>
			<c:set var="objVal11" value="${objDt[itemColumnId11]}"/>
			<c:set var="objVal12" value="${objDt[itemColumnId12]}"/>
			<c:set var="objVal21" value="${objDt[itemColumnId21]}"/>
			<c:set var="objVal22" value="${objDt[itemColumnId22]}"/>
			<c:out value="${objVal11}"/><c:if test="${!empty objVal12}">&nbsp;${objVal12}</c:if><c:if test="${!empty objVal11 || !empty objVal12 || !empty objVal21 || !empty objVal22}">&nbsp;~&nbsp;</c:if><c:out value="${objVal21}"/><c:if test="${!empty objVal22}">&nbsp;${objVal22}</c:if>
		</c:when>
		<c:when test="${itemFormatType == 19}">
			<%/* 년도 */%>
			<c:if test="${!empty objVal}">${objVal}<spring:message code="item.year.name"/></c:if>
		</c:when>
		<c:when test="${itemFormatType == 20}">
			<%/* 년도 월 */%>
			<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
			<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
			<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
			<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
			<c:if test="${!empty objVal1}">${objVal1}<spring:message code="item.year.name"/></c:if><c:if test="${!empty objVal2}">&nbsp;${objVal2}<spring:message code="item.month.name"/></c:if>
		</c:when>
		<c:when test="${itemFormatType == 21}">
			<%/* 년도 분기 */%>
			<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
			<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
			<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
			<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
			<c:forEach var="optnDt" items="${optnOptList}" varStatus="i">
				<c:if test="${optnDt.OPTION_CODE == objVal2}"><c:set var="objVal2" value="${optnDt.OPTION_NAME}"/></c:if>
			</c:forEach>
			<c:if test="${!empty objVal1}">${objVal1}<spring:message code="item.year.name"/></c:if><c:if test="${!empty objVal2}">&nbsp;${objVal2}<spring:message code="item.quarterly.name"/></c:if>
		</c:when>
		<c:when test="${itemFormatType == 22}">
			<%/* 년도 월 일 */%>
			<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
			<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
			<c:set var="itemColumnId3" value="${itemObj['column_id']}3"/>
			<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
			<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
			<c:set var="objVal3" value="${objDt[itemColumnId3]}"/>
			<c:if test="${!empty objVal1}">${objVal1}<spring:message code="item.year.name"/></c:if><c:if test="${!empty objVal2}">&nbsp;${objVal2}<spring:message code="item.month.name"/></c:if><c:if test="${!empty objVal3}">&nbsp;${objVal3}<spring:message code="item.day.name"/></c:if>
		</c:when>
		<c:when test="${itemFormatType == 29}">
			<%/* 년도(기간) */%>
			<c:set var="itemColumnId1" value="${itemObj['column_id']}1"/>
			<c:set var="itemColumnId2" value="${itemObj['column_id']}2"/>
			<c:set var="objVal1" value="${objDt[itemColumnId1]}"/>
			<c:set var="objVal2" value="${objDt[itemColumnId2]}"/>
			<c:if test="${!empty objVal1}">${objVal1}<spring:message code="item.year.name"/></c:if><c:if test="${!empty objVal1 || !empty objVal2}">&nbsp;~&nbsp;${objVal2}<spring:message code="item.year.name"/></c:if>
		</c:when>
		<c:when test="${itemFormatType == 30}">
			<%/* 년도 월(기간) */%>
			<c:set var="itemColumnId11" value="${itemObj['column_id']}11"/>
			<c:set var="itemColumnId12" value="${itemObj['column_id']}12"/>
			<c:set var="itemColumnId21" value="${itemObj['column_id']}21"/>
			<c:set var="itemColumnId22" value="${itemObj['column_id']}22"/>
			<c:set var="objVal11" value="${objDt[itemColumnId11]}"/>
			<c:set var="objVal12" value="${objDt[itemColumnId12]}"/>
			<c:set var="objVal21" value="${objDt[itemColumnId21]}"/>
			<c:set var="objVal22" value="${objDt[itemColumnId22]}"/>
			<c:if test="${!empty objVal11}">${objVal11}<spring:message code="item.year.name"/></c:if><c:if test="${!empty objVal12}">&nbsp;${objVal12}<spring:message code="item.month.name"/></c:if><c:if test="${!empty objVal11 || !empty objVal12 || !empty objVal21 || !empty objVal22}">&nbsp;~&nbsp;</c:if><c:if test="${!empty objVal21}">${objVal21}<spring:message code="item.year.name"/></c:if><c:if test="${!empty objVal22}">&nbsp;${objVal22}<spring:message code="item.month.name"/></c:if>
		</c:when>
		<c:when test="${itemFormatType == 61}">
			<%/* 시:분(기간) */%>
			<c:set var="itemColumnId11" value="${itemObj['column_id']}11"/>
			<c:set var="itemColumnId12" value="${itemObj['column_id']}12"/>
			<c:set var="itemColumnId21" value="${itemObj['column_id']}21"/>
			<c:set var="itemColumnId22" value="${itemObj['column_id']}22"/>
			<c:set var="objVal11" value="${objDt[itemColumnId11]}"/>
			<c:set var="objVal12" value="${objDt[itemColumnId12]}"/>
			<c:set var="objVal21" value="${objDt[itemColumnId21]}"/>
			<c:set var="objVal22" value="${objDt[itemColumnId22]}"/>
			<c:out value="${objVal11}"/><c:if test="${!empty objVal12}">:${objVal12}</c:if><c:if test="${!empty objVal11 || !empty objVal12 || !empty objVal21 || !empty objVal22}">&nbsp;~&nbsp;</c:if><c:out value="${objVal21}"/><c:if test="${!empty objVal22}">:${objVal22}</c:if>
		</c:when>
		<c:when test="${itemFormatType == 31}">
			<%/* 링크경로 */%>
			<c:if test="${!empty objVal}">
			<a href="<c:out value="${objVal}"/>" target="_blank" class="fn_item_link_url fn_${itemId}"><c:choose><c:when test="${itemObj['display_value'] == 1}"><c:out value="${objVal}"/></c:when><c:otherwise>${elfn:getItemName(itemInfo.items[itemId])}</c:otherwise></c:choose></a>
			</c:if>
		</c:when>
		<c:when test="${itemObjectType == 2 || itemObjectType == 5}">
			<c:choose>
				<c:when test="${itemObjectType == 2 && !empty itemObj['name_column_id'] && !empty objNameVal}"><c:out value="${objNameVal}"/></c:when>
				<c:otherwise>
					<c:forEach var="optnDt" items="${optnOptList}" varStatus="i">
						<c:if test="${optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal}"><c:out value="${optnDt.OPTION_NAME}"/></c:if>
					</c:forEach>
				</c:otherwise>
			</c:choose>
			<c:out value="${itemObj['next_text']}"/>
		</c:when>
		<c:when test="${itemObjectType == 3 || itemObjectType == 4 || itemObjectType == 11}">
			<%/* multi select/checkbox/multi select + button */%>
			<c:if test="${defVal == null}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>
			<c:if test="${empty defVal}"><c:set var="defVal" value="${itemObj['default_value']}"/></c:if>
			<c:set var="multiList" value="${multiDataHashMap[itemId]}"/>
			
			<c:choose>
				<c:when test="${!empty multiList}">
					<ul class="fn_mult">
					<c:forEach var="objValDt" items="${multiList}" varStatus="i">
						<c:set var="optnDt" value="${elfn:getMatchHashMap(optnOptList, objValDt.ITEM_KEY, 'OPTION_CODE')}"/>
						<li><c:out value="${optnDt.OPTION_NAME}"/></li>
					</c:forEach>
					</ul>
				</c:when>
				<c:when test="${!empty defVal}">
					<c:set var="multiStrs" value="${fn:split(defVal, ',')}"/>
					<ul class="fn_mult">
					<c:forEach var="objValDt" items="${multiStrs}" varStatus="i">
						<c:set var="optnDt" value="${elfn:getMatchHashMap(optnOptList, objValDt, 'OPTION_CODE')}"/>
						<li><c:out value="${optnDt.OPTION_NAME}"/></li>
					</c:forEach>
					</ul>
				</c:when>
			</c:choose>
		</c:when>
		<c:when test="${itemObjectType == 22}">
			<%/* 다차원 분류 */%>
			<c:if test="${(!empty objVal || !empty defVal) && !empty optnOptList}">
				<c:set var="itemClassSplitStr" value="${itemObj['option_split_text']}"/>
				<c:if test="${!empty classSplitStr}"><c:set var="itemClassSplitStr" value="${classSplitStr}"/></c:if>
				<c:if test="${empty itemClassSplitStr}"><c:set var="itemClassSplitStr" value=" > "/></c:if>
				<c:set var="optnOptLen" value="${fn:length(optnOptList)}"/>
				<c:set var="parentOptnCode" value=""/>
				<c:set var="optnNameVal" value=""/>
				<c:forEach var="optnI" begin="1" end="${optnOptLen}">
					<c:set var="optnDt" value="${optnOptList[optnOptLen - optnI]}"/>
					<c:if test="${!empty parentOptnCode && optnDt.OPTION_CODE == parentOptnCode || empty parentOptnCode && (optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal)}">
						<c:if test="${!empty optnNameVal}"><c:set var="optnNameVal" value="${itemClassSplitStr}${optnNameVal}"/></c:if>
						<c:set var="optnNameVal" value="${optnDt.OPTION_NAME}${optnNameVal}"/>
						<c:set var="parentOptnCode" value="${optnDt.PARENT_OPTION_CODE}"/>
					</c:if>
				</c:forEach>
				<c:out value="${optnNameVal}"/>
			</c:if>
		</c:when>
		<c:when test="${itemObjectType == 14}">
			<%/* checkbox(단일) */%>
			<c:forEach var="optnDt" items="${optnOptList}" varStatus="i">
				<c:if test="${optnDt.OPTION_CODE == objVal || empty objVal && optnDt.OPTION_CODE == defVal}"><c:set var="optnName" value="${optnDt.OPTION_NAME}"/></c:if>
			</c:forEach>
			<c:if test="${empty optnName && objVal == '1'}">
				<c:set var="optnName" value="${elfn:getItemName(itemInfo.items[itemId])}"/>
			</c:if>
			<c:out value="${optnName}"/>
		</c:when>
		<c:when test="${itemObjectType == 8 || itemObjectType == 98}">
		<%/* text + button */%>
			<c:set var="nameColumnId" value="${itemObj['name_column_id']}"/>
			<c:if test="${empty nameColumnId}">
				<c:set var="nameColumnId" value="${itemColumnId}_NAME"/>
			</c:if>
			<c:set var="objNameVal" value="${objDt[nameColumnId]}"/>
			<c:out value="${objNameVal}"/>
		</c:when>
		<c:when test="${itemObjectType == 6}">
		<%/* file */%>
			<c:set var="keyItemId" value="${settingInfo.idx_name}"/>
			<c:set var="keyColumnId" value="${settingInfo.idx_column}"/>
			<c:set var="originColumnId" value="${itemColumnId}_ORIGIN_NAME"/>
			<c:set var="savedColumnId" value="${itemColumnId}_SAVED_NAME"/>
			<c:set var="textColumnId" value="${itemColumnId}_TEXT"/>
			<c:set var="isImage" value="${itemObj['isimage'] == '1'}"/>		<%/* 이미지 여부 */%>
			<c:if test="${!isImage || isImage && (imgDisplayType == 1 || imgDisplayType == 2)}">
			<%/* 이미지항목이 아닌경우 || 다운로드 display하는 경우 */ %>
				<c:choose>
					<c:when test="${!empty downloadUrl}">
						<a href="<c:out value="${downloadUrl}"/>&itId=<c:out value="${itemId}"/>" class="fn_filedown"><c:out value="${objDt[originColumnId]}"/></a>
					</c:when>
					<c:otherwise>
						<a href="<c:out value="${URL_DOWNLOAD}"/>&${keyItemId}=<c:out value="${objDt[keyColumnId]}"/>&itId=<c:out value="${itemId}"/>" class="fn_filedown"><c:out value="${objDt[originColumnId]}"/></a>
					</c:otherwise>
				</c:choose>
			</c:if>
			<c:if test="${!isImage && isDisplayImg || imgDisplayType != 1 && isImage}">
			<%/* 이미지 파일인 경우 display || 이미지항목인 경우 */ %>
			<spring:message var="imgRootPath" code="Globals.image.domain.path" text=""/>
			<c:choose>
				<c:when test="${!empty imgRootPath}">
					<c:set var="imagePath" value="${imgRootPath}/${crtMenu.module_id}/${crtMenu.fn_idx}/${objDt[savedColumnId]}"/>
					<c:set var="moviePath" value="${imgRootPath}/${crtMenu.module_id}/${crtMenu.fn_idx}/${objDt[savedColumnId]}"/>
				</c:when>
				<c:otherwise>
					<c:set var="itemImgUrl" value="${URL_IMAGE}"/>
					<c:if test="${!empty imgDefaultUrl}"><c:set var="itemImgUrl" value="${imgDefaultUrl}"/></c:if>
					<c:set var="imagePath" value="${itemImgUrl}&id=${elfn:imgNSeedEncrypt(objDt[savedColumnId])}"/>
					<c:set var="moviePath" value="${URL_MOVIE}&id=${elfn:imgNSeedEncrypt(objDt[savedColumnId])}"/>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${elfn:isImage(objDt[savedColumnId])}"><img src="<c:out value="${imagePath}"/>" alt="<c:out value="${objDt[textColumnId]}"/>"/></c:when>
				<c:when test="${fn:endsWith(objDt[savedColumnId], '.mp4')}">
					<video controls preload="none" data-setup='{"controls": true, "autoplay": false, "preload": "none" }' style="max-width:100%;">
						<source src="<c:out value="${moviePath}"/>" type="video/mp4">
						<object classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" width="100%" height="500px">
							<param name="filename" value="<c:out value="${moviePath}"/>">
							<param name="autoplay" value="false">
							<embed src="<c:out value="${moviePath}"/>" height="500px" width="100%" autostart="0" showcontrols="1"></embed>
						</object>
						<c:if test="${!empty objDt[textColumnId]}"><p class="fn_movieTxt"><c:out value="${objDt[textColumnId]}"/></p></c:if>
					</video>
				</c:when>
				<c:when test="${fn:endsWith(objDt[savedColumnId], '.wmv') || fn:endsWith(objDt[savedColumnId], '.mov') || fn:endsWith(objDt[savedColumnId], '.avi') || fn:endsWith(objDt[savedColumnId], '.asf')}">
				<object classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" width="100%" height="500px">
					<param name="filename" value="<c:out value="${moviePath}"/>">
					<param name="autoplay" value="false">
					<embed src="<c:out value="${moviePath}"/>" height="500px" width="100%" autostart="0" showcontrols="1"></embed>
				</object>
				<c:if test="${!empty objDt[textColumnId]}"><p class="fn_movieTxt"><c:out value="${objDt[textColumnId]}"/></p></c:if>
				</c:when>
			</c:choose>
			</c:if>
		</c:when>
		<c:when test="${itemObjectType == 9}">
		<%/* multi file */%>
			<c:set var="fileList" value="${multiFileHashMap[itemId]}"/>
			<c:if test="${!empty fileList}">
			<c:set var="isImage" value="${itemObj['isimage'] == '1'}"/>		<%/* 이미지 여부 */%>
			<c:set var="keyItemId" value="${settingInfo.idx_name}"/>
			<c:set var="keyColumnId" value="${settingInfo.idx_column}"/>
			<c:set var="originColumnId" value="FILE_ORIGIN_NAME"/>
			<c:set var="savedColumnId" value="FILE_SAVED_NAME"/>
			<c:set var="textColumnId" value="FILE_TEXT"/>
			<c:if test="${!isImage || isImage && (imgDisplayType == 1 || imgDisplayType == 2)}">
			<%/* 이미지항목이 아닌경우 || 다운로드 display하는 경우 */ %>
			<c:if test="${!empty fileList}">
			<ul class="fn_con_file">
			<c:forEach var="optnDt" items="${fileList}" varStatus="i">
				<li>
					<img src="/web/images/ico_add.png" alt="첨부파일 아이콘" />
				<c:choose>
					<c:when test="${!empty downloadUrl}"><a href="<c:out value="${downloadUrl}"/>&fidx=<c:out value="${optnDt.FLE_IDX}"/>&itId=<c:out value="${optnDt.ITEM_ID}"/>" class="fn_filedown"><c:out value="${optnDt[originColumnId]}"/></a></c:when>
					<c:otherwise><a href="<c:out value="${URL_DOWNLOAD}"/>&${keyItemId}=<c:out value="${optnDt[keyColumnId]}"/>&fidx=<c:out value="${optnDt.FLE_IDX}"/>&itId=<c:out value="${optnDt.ITEM_ID}"/>" class="fn_filedown"><c:out value="${optnDt[originColumnId]}"/></a></c:otherwise>
				</c:choose>
				</li>
			</c:forEach>
			</ul>
			</c:if>
			</c:if>
			<c:if test="${itemObj['isimage'] != '1' && isDisplayImg || imgDisplayType != 1 && itemObj['isimage'] == '1'}">
			<%/* 이미지 파일인 경우 display || 이미지항목인 경우 */ %>
			<spring:message var="imgRootPath" code="Globals.image.domain.path" text=""/>
			<c:forEach var="optnDt" items="${fileList}" varStatus="i">
				<c:if test="${i.first}"><ul id="fn_con_img_${itemId}" style="margin-top:5px;"></c:if>
				<c:choose>
					<c:when test="${!empty imgRootPath}">
						<c:set var="imagePath" value="${imgRootPath}/${crtMenu.module_id}/${crtMenu.fn_idx}/${optnDt[savedColumnId]}"/>
						<c:set var="moviePath" value="${imgRootPath}/${crtMenu.module_id}/${crtMenu.fn_idx}/${optnDt[savedColumnId]}"/>
					</c:when>
					<c:otherwise>
						<c:set var="itemImgUrl" value="${URL_IMAGE}"/>
						<c:if test="${!empty imgDefaultUrl}"><c:set var="itemImgUrl" value="${imgDefaultUrl}"/></c:if>
						<c:set var="imagePath" value="${itemImgUrl}&id=${elfn:imgNSeedEncrypt(optnDt[savedColumnId])}"/>
						<c:set var="moviePath" value="${URL_MOVIE}&id=${elfn:imgNSeedEncrypt(optnDt[savedColumnId])}"/>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${elfn:isImage(optnDt[savedColumnId])}">
						<li><img src="<c:out value="${imagePath}"/>" alt="<c:out value="${optnDt[textColumnId]}"/>"/></li>
					</c:when>
					<c:when test="${fn:endsWith(optnDt[savedColumnId], '.mp4')}">
					<li>
						<video controls preload="none" data-setup='{"controls": true, "autoplay": false, "preload": "none" }' style="max-width:100%;">
							<source src="<c:out value="${moviePath}"/>" type="video/mp4">
							<object classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" width="100%" height="500px">
								<param name="filename" value="<c:out value="${moviePath}"/>">
								<param name="autoplay" value="false">
								<embed src="<c:out value="${moviePath}"/>" height="500px" width="100%" autostart="0" showcontrols="1"></embed>
							</object>
							<c:if test="${!empty optnDt[textColumnId]}"><p class="fn_movieTxt"><c:out value="${optnDt[textColumnId]}"/></p></c:if>
						</video>
					</li>
					</c:when>
					<c:when test="${fn:endsWith(optnDt[savedColumnId], '.wmv') || fn:endsWith(optnDt[savedColumnId], '.mov') || fn:endsWith(optnDt[savedColumnId], '.avi') || fn:endsWith(optnDt[savedColumnId], '.asf')}">
					<li>
						<object classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" width="100%" height="500px">
							<param name="filename" value="<c:out value="${moviePath}"/>">
							<param name="autoplay" value="false">
							<embed src="<c:out value="${moviePath}"/>" height="500px" width="100%" autostart="0" showcontrols="1"></embed>
						</object>
						<c:if test="${!empty optnDt[textColumnId]}"><p class="fn_movieTxt"><c:out value="${optnDt[textColumnId]}"/></p></c:if>
					</li>
					</c:when>					
				</c:choose>
				<c:if test="${i.last}"></ul></c:if>
			</c:forEach>
			</c:if>
			</c:if>
		</c:when>
		<c:when test="${itemObjectType == 1}">
		<%/* textarea */%>
			<c:if test="${!empty objVal}">
			<c:choose>
				<c:when test="${empty itemObj['content_type']}">
					<c:set var="typeColumnId" value="${itemColumnId}2"/>
					<c:set var="contentsType" value="${objDt[typeColumnId]}"/>
				</c:when>
				<c:otherwise>
					<c:set var="contentsType" value="${itemObj['content_type']}"/>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${contentsType == '2'}">
					<%/* 에디터로 입력한 경우 : javascript만 삭제 */%>
					<%--<div class="se2_outputarea">--%>
					${elfn:removeScriptTag(objVal)}
					<%--</div>--%>
				</c:when>
				<c:otherwise>
					<%/* 에디터로 입력하지않은 경우 : html tag 사용 못함 */%>
					${elfn:replaceHtmlN(objVal)}
				</c:otherwise>
			</c:choose>
			</c:if>
		</c:when>
		<c:when test="${itemObjectType == 101}">
			<c:if test="${!empty objVal}">
				<c:set var="objVal" value="${elfn:privDecrypt(objVal)}"/>
				<c:out value="${objVal}"/>
				<c:out value="${itemObj['next_text']}"/>
			</c:if>
		</c:when>
		<c:when test="${itemObj['item_type'] == '11'}">
			<%/* 통화(원)  */%>
			<fmt:formatNumber value="${objVal}" pattern="#,##0" />
			<c:out value="${itemObj['next_text']}"/>
		</c:when>
		<c:otherwise>
			<c:out value="${objVal}"/>
			<c:out value="${itemObj['next_text']}"/>
		</c:otherwise>
	</c:choose>
</c:if>