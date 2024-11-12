<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_memberInputForm"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="items" value="${itemInfo.items}"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="menuName" value="${crtMenu.menu_name}"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<div class="sContent">
	<div id="cms_member_article">
		<p class="titTypeB"><c:out value="${menuName}"/></p>
		<p class="titTypeD mgt10" style="padding-bottom:5px;">아래 항목을 정확히 입력해주세요! <span style="color:#f84418;"> *</span>표시는 필수입력항목입니다.</p>
		<spring:message var="useSnsLogin" code="Globals.sns.login.use" text="0"/>
		<spring:message var="useSnsLoginRegi" code="Globals.sns.login.register.use" text="0"/>
		<c:if test="${useSnsLogin == '1' && useSnsLoginRegi != '1'}">
		<form id="fn_snsLoginForm" name="snsLoginForm" method="post" target="submit_target"><table class="tbWriteA" summary="<itui:tableSummary items="${items}" itemOrder="${itemOrder}" hasPrePwd="${true}"/> 작성을 위한 서식 표">
				<caption><c:out value="${menuName}"/> 글쓰기 서식</caption>
				<colgroup>
					<col width="20%" />
					<col width="80%" />						
				</colgroup>
				<tbody>
					<tr>
						<itui:itemID id="snsMbrId" itemId="mbrId" itemInfo="${itemInfo}"/>
					</tr>
					<tr>
						<itui:itemPassword id="snsMbrPwd" name="pre_mbrPwd" itemId="mbrPwd" itemInfo="${itemInfo}" objClass="inputTxt2 inputID"/>
					</tr>
				</tbody>
			</table>
			<div>
			<c:forEach var="snsId" items="${settingInfo.sns_info.list}">
				<c:set var="useSnsLoginItemName" value="${snsId}_login"/>
				<c:if test="${siteInfo[useSnsLoginItemName] == 1}">
				<c:set var="snsInfo" value="${settingInfo.sns_info.items[snsId]}"/>
				<c:set var="snsUrlName" value="URL_${snsInfo.sns_url_flag}LOGIN"/>
				<button type="button" data-url="<c:out value="${requestScope[snsUrlName]}"/>" class="fn_btn_snsLogin"><c:out value="${snsInfo.sns_name}"/> 등록</button>
				</c:if>
			</c:forEach>
			</div>
		</form>
		</c:if>
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
			<table class="tbWriteA" summary="<itui:tableSummary items="${items}" itemOrder="${itemOrder}" hasPrePwd="${true}"/> 작성을 위한 서식 표">
				<caption><c:out value="${menuName}"/> 글쓰기 서식</caption>
				<colgroup>
					<col width="20%" />
					<col width="80%" />						
				</colgroup>
				<tbody>
					<tr>
						<itui:itemID itemId="mbrId" itemInfo="${itemInfo}"/>						
					</tr>
					<tr>
						<itui:itemView itemId="mbrName" itemInfo="${itemInfo}"/>
					</tr>
					<tr>
						<itui:itemPasswordPre itemId="mbrPwd" itemInfo="${itemInfo}" objClass="inputTxt2 inputID"/>
					</tr>
					<tr>
						<itui:itemPassword itemId="mbrPwd" itemInfo="${itemInfo}" objClass="inputTxt2 inputID"/>
					</tr>
					<tr>
						<itui:itemPasswordReconfirm itemId="mbrPwd" itemInfo="${itemInfo}" objClass="inputTxt2 inputID"/>
					</tr>
					<tr>
						<itui:itemPHONE itemId="homePhone" itemInfo="${itemInfo}" objType="0"/>											
					</tr>
					<tr>
						<itui:itemPHONE itemId="mobilePhone" itemInfo="${itemInfo}" objType="1"/>											
					</tr>	
					<tr>
						<itui:itemADDR itemId="homeAddr" itemInfo="${itemInfo}"/>	
					</tr>
					<tr>
						<c:choose>
							<c:when test="${elfn:isUseNameAuth()}"><c:set var="dupConfirm" value="0"/></c:when>
							<c:otherwise><c:set var="dupConfirm" value="1"/></c:otherwise>
						</c:choose>
						<itui:itemEMAIL itemId="mbrEmail" itemInfo="${itemInfo}" dupConfirm="${dupConfirm}"/>	
					</tr>
					</tr>
				</tbody>
			</table>
			<!-- btnset -->
			<div class="termsBtn">
				<input type="submit" value="확인" class="btnOrganSearch"/>
				<button type="reset" title="취소" class="btnTypeB">취소</button>
			</div>
			<!-- //btnset -->
		</form>
	</div>
</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>