<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_sitenputForm"/>								<%/* 입력폼 id/name */ %>
<c:if test="${!empty TOP_PAGE}">
	<%/* javascript 파일경로 */%>
	<%/* 입력폼 id/name : javascript에서 사용 */ %>
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${settingInfo.input_title}"/>
		<jsp:param name="javascript_page" value="${moduleJspRPath}/setting.jsp"/>		
		<jsp:param name="inputFormId" value="${inputFormId}"/>						
	</jsp:include>
</c:if>
	<div id="cms_website_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		
		<h4>기본정보</h4>
		<table class="tbWriteA" summary="기본정보 글쓰기 서식">
			<caption>
			기본정보 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemText itemId="site_id" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_name" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_domain" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemSelect itemId="site_type" itemInfo="${itemInfo}" objStyle="min-width:150px;"/>
				</tr>
				<tr>
					<itui:itemSelect itemId="locale_lang" itemInfo="${itemInfo}" objStyle="min-width:150px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="site_title" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="ssl_port" itemInfo="${itemInfo}"/>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="access_restrict" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectCheckboxSG itemId="access_restrict" itemInfo="${itemInfo}"/>
						<div>
							<itui:objectLabel itemId="restrict_ip" itemInfo="${itemInfo}"/>
							<itui:objectText itemId="restrict_ip" itemInfo="${itemInfo}" objStyle="width:500px;"/>
							<c:if test="${!empty itemInfo.items['restrict_ip']['comment']}"><span class="comment"><c:out value="${itemInfo.items['restrict_ip']['comment']}" escapeXml="false"/></span></c:if>
						</div>
					</td>
				</tr>
				<spring:message var="msgInputNew" code="item.select.input.new"/>
				<tr>
					<itui:itemSelect itemId="banner_fn_idx" itemInfo="${itemInfo}" itemOptBlankTitle="${msgInputNew}" objStyle="min-width:150px;"/>
				</tr>
				<tr>
					<itui:itemSelect itemId="popup_fn_idx" itemInfo="${itemInfo}" itemOptBlankTitle="${msgInputNew}" objStyle="min-width:150px;"/>
				</tr>
			</tbody>
		</table>
		<h4>API연동</h4>
		<table class="tbWriteA" summary="API연동 글쓰기 서식">
			<caption>
			API연동 글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemText itemId="daum_map_key" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<itui:itemText itemId="daum_local_key" itemInfo="${itemInfo}" objStyle="width:500px;"/>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="twitter" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="twitter_id"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="twitter_url"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="twitter_ckey"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="twitter_csecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="twitter_wgid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="facebook" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="facebook_id"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="facebook_url"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="facebook_atoken"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="facebook_aid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="facebook_asecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="kakao" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="kakao_aid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="kakao_asecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="naver" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="naver_aid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="naver_asecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<tr>
					<th scope="row"><itui:objectLabel itemId="google" itemInfo="${itemInfo}"/></th>
					<td>
						<dl class="fn_inlineBlock">
							<c:set var="itemId" value="google_aid"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
							<c:set var="itemId" value="google_asecret"/>
							<dt><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
							<dd><itui:objectText itemId="${itemId}" itemInfo="${itemInfo}" objStyle="width:250px;"/></dd>
						</dl>
					</td>
				</tr>
				<spring:message var="useSnsLogin" code="Globals.sns.login.use" text="0"/>
				<c:if test="${useSnsLogin == '1'}">
				<tr>
					<th scope="row"><itui:objectLabel itemId="sns_login" itemInfo="${itemInfo}"/></th>
					<td>
						<itui:objectCheckboxSG itemId="sns_login" itemInfo="${itemInfo}"/>
						<ul class="fn_inlineBlock fn_sns_login">
							<c:set var="itemId" value="facebook_login"/>
							<li><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></li>
							<c:set var="itemId" value="kakao_login"/>
							<li><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></li>
							<c:set var="itemId" value="naver_login"/>
							<li><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></li>
							<c:set var="itemId" value="google_login"/>
							<li><itui:objectCheckboxSG itemId="${itemId}" itemInfo="${itemInfo}"/></li>
						</ul>
					</td>
				</tr>
				</c:if>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_reset">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>