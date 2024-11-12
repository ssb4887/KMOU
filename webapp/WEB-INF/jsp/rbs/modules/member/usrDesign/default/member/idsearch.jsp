<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="memberInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/idsearch.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<div class="sContent">
	<div id="cms_member_article">
		<div class="findTit">
			<h4 class="titTypeA">아이디를 잊어버리셨나요?</h4>
		</div>
		<div class="loginWrap">
			<span class="snsLogin"><img src="<c:out value="${contextPath}${moduleResourcePath}/images/find_img.gif"/>" alt=""/></span>
			<form name="${inputFormId}" id="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
				<fieldset class="findForm">
				<legend class="blind">아이디 찾기</legend>
				<ul class="login">
					<li>
						<itui:objectLabel itemId="mbrName" itemInfo="${itemInfo}"/>
						<itui:objectText itemId="mbrName" itemInfo="${itemInfo}" objClass="inputTxt2 inputName"/>
					</li>
					<li>
						<itui:objectLabel itemId="mbrEmail" itemInfo="${itemInfo}"/>
						<itui:objectEMAIL itemId="mbrEmail" itemInfo="${itemInfo}" objClass1="inputTxt3 inputMail" objClass2="inputTxt3 inputMail" objClass3="select"/>	
					</li>
				</ul>
				<input type="submit" value="찾기" class="btnLogin" />
				</fieldset>
			</form>
		</div>
		<p class="findid">이름과 회원가입시 등록했던 이메일 주소를 입력하신 후 [찾기]버튼을 클릭하여 주십시오.<br/>입력하신 정보가 회원가입 정보와 일치할 경우 아이디를 확인하실 수 있습니다.</p>
	</div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>