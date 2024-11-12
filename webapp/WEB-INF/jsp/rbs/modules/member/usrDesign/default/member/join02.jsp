<%@page import="com.woowonsoft.egovframework.form.DataForm"%>
<%@page import="net.sf.json.JSONArray"%>
<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/join02.jsp"/>
		<jsp:param name="joinStep" value="2"/>
	</jsp:include>
</c:if>
<%
// 나이스 본인인증 변경에 따른 수정
JSONArray itemOrder = JSONObjectUtil.getJSONArray(JSONObjectUtil.getJSONObject(request.getAttribute("itemInfo")), request.getAttribute("submitType") + "_order");
DataForm queryString1 = (DataForm)request.getAttribute("queryString");
String paramR1 = StringUtil.getString(request.getAttribute("URL_JOIN03"));
String paramR2 = null;
for(Object itemIdObj:itemOrder) {
	String itemId = StringUtil.getString(itemIdObj);
	paramR1 += "&" + itemId + "=" + queryString1.get(itemId);
}
%>
<%@ include file="../../../../../usr/nice/niceIpinInit.jsp"%>
<%-- <c:set var="urlJoin03" value="${URL_JOIN03}"/>
<c:forEach var="itemId" items="${itemOrder}" varStatus="i">
	<c:set var="urlJoin03" value="${urlJoin03}&${itemId}=${queryString[itemId]}"/>
</c:forEach> --%>
<div class="sContent">
	<div id="cms_member_article">
		<jsp:include page="joinStep.jsp" flush="false"/>
		
		<div class="titTypeA">회원가입 유형을 선택하세요.</div>
		<div class="register_wrap">
			<ul>
				<c:if test="${elfn:isUseNiceAuth()}">
				<li class="line">
					<div class="reg_icon1"></div>
					<h4>휴대폰인증</h4>
					<p>휴대폰인증</p>
					<!-- 본인인증 서비스 팝업을 호출하기 위해서는 다음과 같은 form이 필요합니다. -->
					<form name="form_chk" method="post">
						<input type="hidden" name="m" value="checkplusSerivce">						<!-- 필수 데이타로, 누락하시면 안됩니다. -->
						<input type="hidden" name="EncodeData" value="<%= sEncData %>">		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
					    
					    <!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
					    	 해당 파라미터는 추가하실 수 없습니다. -->
						<%-- <input type="hidden" name="param_r1" value="<c:out value="${urlJoin03}"/>">
						<input type="hidden" name="param_r2" value="">
						<input type="hidden" name="param_r3" value=""> --%>
					    
						<a href="javascript:fn_checkplusPopup();" class="btnTypeAL">휴대폰인증</a>
					</form>
				</li>
				</c:if>
				<c:if test="${elfn:isUseIpingAuth()}">
				<li>
					<div class="reg_icon2"></div>
					<h4>IPIN인증</h4>
					<p>IPIN인증</p>
					<!-- 가상주민번호 서비스 팝업을 호출하기 위해서는 다음과 같은 form이 필요합니다. -->
					<form name="form_ipin" method="post">
						<input type="hidden" name="m" value="pubmain">						<!-- 필수 데이타로, 누락하시면 안됩니다. -->
					    <input type="hidden" name="enc_data" value="<%= sIPINEncData %>">		<!-- 위에서 업체정보를 암호화 한 데이타입니다. -->
					    
					    <!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
					    	 해당 파라미터는 추가하실 수 없습니다. -->
					    <%-- <input type="hidden" name="param_r1" value="<c:out value="${urlJoin03}"/>">
					    <input type="hidden" name="param_r2" value="">
					    <input type="hidden" name="param_r3" value=""> --%>
						<input type="hidden" name="param_r1" value="">
					    <input type="hidden" name="param_r2" value="">
					    <input type="hidden" name="param_r3" value="">
					    
						<a href="javascript:fn_ipinPopup();" class="btnTypeAL">IPIN인증</a>
					</form>
					<!-- 가상주민번호 서비스 팝업 페이지에서 사용자가 인증을 받으면 암호화된 사용자 정보는 해당 팝업창으로 받게됩니다.
						 따라서 부모 페이지로 이동하기 위해서는 다음과 같은 form이 필요합니다. -->
					<form name="vnoform" method="post">
						<input type="hidden" name="enc_data">								<!-- 인증받은 사용자 정보 암호화 데이타입니다. -->
						
						<!-- 업체에서 응답받기 원하는 데이타를 설정하기 위해 사용할 수 있으며, 인증결과 응답시 해당 값을 그대로 송신합니다.
					    	 해당 파라미터는 추가하실 수 없습니다. -->
					    <input type="hidden" name="param_r1" value="">
					    <input type="hidden" name="param_r2" value="">
					    <input type="hidden" name="param_r3" value="">
					</form>
				</li>
				</c:if>
				<div class="cboth"></div>
			</ul>
		</div>
	</div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>