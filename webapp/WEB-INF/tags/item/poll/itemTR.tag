<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="pollui" tagdir="/WEB-INF/tags/item/poll" %>
<%@ attribute name="itemDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="msgItemSelect"%>
<%@ attribute name="msgItem"%>
<%@ attribute name="msgItemEtc"%>
<%@ attribute name="msgPoint"%>
<%@ attribute name="usePoint" type="java.lang.Boolean"%>
<%@ attribute name="useEtc" type="java.lang.Boolean"%>
<c:if test="${empty usePoint}">
	<c:set var="usePoint" value="false"/>
</c:if>
<c:if test="${empty useEtc}">
	<c:set var="useEtc" value="false"/>
</c:if>
<% /* itemList */ %>
						<tr>
							<td><input type="checkbox" name="itemDel" title="<c:out value="${msgItemSelect}"/>" value="1"/></td>
							<td><input type="text" name="itemContents" title="<c:out value="${msgItem}"/>" class="inputTxt itemContents" style="width:95%;" value="<c:out value="${itemDt.CONTENTS}"/>"/></td>
							<c:if test="${useEtc}">
							<td class="fn_questype fn_input_text_td"><input type="checkbox" name="isInputText" class="isInputText" title="<c:out value="${msgItemEtc}"/>" value="1"<c:if test="${itemDt.ISTEXT == '1'}"> checked="checked"</c:if>/></td>
							</c:if>
							<c:if test="${usePoint}">
							<td><input type="text" name="itemPoints" title="<c:out value="${msgPoint}"/>" class="inputTxt itemPoints" style="ime-mode:disabled;width:50px;" maxlength="2" value="<c:out value="${itemDt.POINTS}"/>"/></td>
							</c:if>
						</tr>