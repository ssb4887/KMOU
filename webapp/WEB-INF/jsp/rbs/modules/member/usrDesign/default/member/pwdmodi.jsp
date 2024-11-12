<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="memberInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/pwdmodi.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<div class="sContent">
	<div id="cms_member_article">
		<div class="findTit">
			<h4 class="titTypeA">비밀번호를 변경해 주세요!</h4>
		</div>
		<div class="loginWrap pwModiForm">
			<form name="${inputFormId}" id="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
				<fieldset>
					<ul class="login pwModiG">
						<li>
							<itui:objectLabel itemId="mbrPwd" itemInfo="${itemInfo}" preItemId="pre_" preStr="이전 "/>
							<itui:objectPasswordPre itemId="mbrPwd" itemInfo="${itemInfo}" objClass="inputTxt2 inputID"/>
						</li>
						<li>
							<itui:objectLabel itemId="mbrPwd" itemInfo="${itemInfo}"/>
							<itui:objectPassword itemId="mbrPwd" itemInfo="${itemInfo}" objClass="inputTxt2 inputID"/>
						</li>
						<li>
							<itui:objectLabel itemId="mbrPwd" itemInfo="${itemInfo}" nextItemId="_reconfirm" nextStr=" 확인"/>
							<itui:objectPasswordReconfirm itemId="mbrPwd" itemInfo="${itemInfo}" objClass="inputTxt2 inputID"/>
						</li>
					</ul>
					<p class="pwModiBtn">
						<input type="submit" value="변경하기" title="변경하기" class="inputBtn btnTypeA" />
						<a href="#none" id="pwdNextchange" class="btn nextchange btnTypeC">다음에 변경하기</a>
					</p>
				</fieldset>
			</form>
		</div>
		<ul class="comment">
			<li>회원님의 아이디 <strong>'<c:out value="${elfn:loginInfo().memberIdOrg}"/>'</strong>는 비밀번호 변경 안내 대상입니다.</li>
			<li><c:out value="${siteInfo.site_name}"/>에서는 회원님의 개인정보를 안전하게 보호하고, 개인정보도용으로 인한 피해를 예방하기 위해
			<strong>3개월 이상 비밀번호를 변경하지 않은 경우</strong> 비밀번호 변경을 안내하고 있습니다.</li>
			<li>비밀번호 변경을 원하지 않을 경우 '다음에 변경하기' 버튼을 눌러 1개월 동안 안내를 받지 않을 수 있습니다.</li>
			<li>비밀번호는 숫자와 영문자, 특수문자를 조합하여 사용하시는것이 안전하며, 주기적(최소 3개월)으로 변경하시기 바랍니다.</li>
		</ul>
	</div>
</div>
<form name="pwdNextchangeForm" id="pwdNextchangeForm" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
	<input type="hidden" name="pwdModiType" id="pwdModiType" value="1"/>
</form>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>