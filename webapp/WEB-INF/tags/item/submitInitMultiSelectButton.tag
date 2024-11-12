<%@tag import="net.sf.json.JSONObject"%>
<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="itemId"%>
<%@ attribute name="name"%>
<%@ attribute name="id"%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<c:set var="itemObj" value="${itemInfo.items[itemId]}"/>
<c:if test="${!empty itemObj}">
<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
<c:if test="${empty name}"><c:set var="name" value="${itemId}"/></c:if>
<c:if test="${empty id}"><c:set var="id" value="${itemId}"/></c:if>
<%/* multi select + button */%>
try{
	$("#fn_btn_add_${id}").click(function(event){
	<c:choose>
		<c:when test="${!empty itemObj['onclick']}">
			${itemObj['onclick']};
			return false;
		</c:when>
		<c:otherwise>
		fn_dialog.init("fn_${id}_search");
		var varItemId = "${id}";
		try {
			var varTitle = "${itemName} 검색";
			fn_dialog.open(varTitle, "${itemObj['search_url']}<c:choose><c:when test="${fn:indexOf(itemObj['search_url'], '?') == -1}">?</c:when><c:otherwise>&</c:otherwise></c:choose>mId=<c:out value="${queryString.mId}"/>&itemId=" + varItemId +"&dl=1");
		}catch(e){}
		return false;
		</c:otherwise>
	</c:choose>
	});

	// ${itemName} 삭제
	$("#fn_btn_del_${id}").click(function(){
		try {
			var varItemId = "${id}";
			var varObj = $("#" + varItemId);
			if(!fn_checkSelected(varObj, "삭제")) return false;
			
			varObj.find("option:selected").remove();
		}catch(e){}
	});
}catch(e){}
</c:if>