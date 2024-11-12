<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="msgItemSelect"%>
<%@ attribute name="msgItemInques"%>
<%@ attribute name="inquesDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<% /* inquesList */ %>
									<tr>
										<td><input type="checkbox" name="inquesDel" title="<c:out value="${msgItemSelect}"/>" value="1"/></td>
										<td><input type="text" name="inquesContents" title="<c:out value="${msgItemInques}"/>" class="inputTxt inquesContents" style="width:95%;" value="<c:out value="${inquesDt.CONTENTS}"/>"/></td>
									</tr>