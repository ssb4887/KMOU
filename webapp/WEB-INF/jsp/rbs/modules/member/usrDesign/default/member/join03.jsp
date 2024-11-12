<%@ include file="../../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="memberInputForm"/>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="items" value="${itemInfo.items}"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="menuName" value="${crtMenu.menu_name}"/>
<c:choose>
	<c:when test="${elfn:isUseNameAuth()}"><c:set var="joinStep" value="3"/></c:when>
	<c:otherwise><c:set var="joinStep" value="2"/></c:otherwise>
</c:choose>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
		<jsp:param name="joinStep" value="${joinStep}"/>
	</jsp:include>
</c:if>
<div class="sContent">
	<div id="cms_member_article">
		<jsp:include page="joinStep.jsp" flush="false"/>
		
		<p class="titTypeB">회원정보입력</p>
		<p class="titTypeD mgt10" style="padding-bottom:5px;">아래 항목을 정확히 입력해주세요! <span style="color:#f84418;"> *</span>표시는 필수입력항목입니다.</p>
		<form name="${inputFormId}" id="${inputFormId}" method="post" enctype="multipart/form-data"  action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target">
		<c:forEach var="itemId" items="${join01ItemOrder}">
			<input type="hidden" name="${itemId}" id= "${itemId}" value="<c:out value="${queryString[itemId]}"/>"/>
		</c:forEach>
		<table class="tbWriteA" summary="<itui:tableSummary items="${items}" itemOrder="${itemOrder}"/> 작성을 위한 서식 표">
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
						<c:choose>
							<c:when test="${elfn:isUseNameAuth()}"><th><itui:objectLabel itemId="mbrName" itemInfo="${itemInfo}"/></th><td><c:out value="${iSName}"/></td></c:when>
							<c:otherwise><itui:itemText itemId="mbrName" itemInfo="${itemInfo}"/></c:otherwise>
						</c:choose>
					</tr>
					<tr>
						<itui:itemPassword itemId="mbrPwd" itemInfo="${itemInfo}"/>
					</tr>
					<tr>
						<itui:itemPasswordReconfirm itemId="mbrPwd" itemInfo="${itemInfo}"/>
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
						<c:set var="memberEmail" value=""/>
						<spring:message var="useSnsLogin" code="Globals.sns.login.use" text="0"/>
						<c:if test="${useSnsLogin == '1' && !empty iSnsLoginVO.snsEmail}">
							<c:set var="memberEmail" value="${iSnsLoginVO.snsEmail}"/>
						</c:if>
						<c:choose>
							<c:when test="${elfn:isUseNameAuth()}"><c:set var="dupConfirm" value="0"/></c:when>
							<c:otherwise><c:set var="dupConfirm" value="1"/></c:otherwise>
						</c:choose>
						<itui:itemEMAIL itemId="mbrEmail" itemInfo="${itemInfo}" dupConfirm="${dupConfirm}" objVal="${memberEmail}"/>	
					</tr>
					<tr>
						<th><itui:objectLabel itemId="ahCode" itemInfo="${itemInfo}"/></th>
						<td>
							<div style="margin-bottom:5px;">
								<img id="ahCodeImg"/>
								<input id="reLoad" type="button" class="btnTypeF" value="새로고침" />
	  							<input id="soundOn" type="button" class="btnTypeF" value="음성듣기" />
							</div>
  							<div id="audioAhCodeImg" style="display: none;"></div>
							<itui:objectText itemId="ahCode" itemInfo="${itemInfo}"/>
						</td>
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
		<form id="fn_mbrIdConfirmForm" name="fn_mbrIdConfirmForm" method="post" action="<c:out value="${URL_IDCONFIRMPROC}"/>" target="submit_target">
			<input type="hidden" name="mbrId"/>
		</form>
		<form id="fn_mbrEmailConfirmForm" name="fn_mbrEmailConfirmForm" method="post" action="<c:out value="${URL_EMAILCONFIRMPROC}"/>" target="submit_target">
			<input type="hidden" name="mbrEmail"/>
		</form>
	</div>
</div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>