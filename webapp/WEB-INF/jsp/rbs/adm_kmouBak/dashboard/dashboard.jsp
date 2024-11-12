<%@ include file="../include/commonTop.jsp"%>
<c:if test="${!empty TOP_PAGE}"><jsp:include page = "${TOP_PAGE}" flush = "false"/></c:if>
	<div id="MCont">
		<c:set var="dSiteId" value=""/>
		
		<c:set var="contMId" value="45"/>
		<c:set var="contTitle" value="${dSiteId} 사이트 정보"/>
		<div class="Stats">
			<div class="head">
				<h3>${contTitle}</h3>
				<a href="${elfn:getMenuUrl(contMId)}" class="more">더보기</a>
			</div>
			<div class="Nlist">
				<table class="tbViewA" summary="<c:out value="${contTitle}"/> 상세보기">
					<caption><c:out value="${contTitle}"/> 상세보기</caption>
					<colgroup>
						<col width="120px" />
						<col />
					</colgroup>
					<tbody>
						<tr>
							<th scope="row">사이트아이디</th>
							<td><c:out value="${websiteDt.SITE_ID}"/></td>
						</tr>
						<tr>
							<th scope="row">사이트명</th>
							<td><c:out value="${websiteDt.SITE_NAME}"/></td>
						</tr>
						<tr>
							<th scope="row">언어</th>
							<td><c:out value="${websiteDt.LOCALE_LANG_NAME}"/></td>
						</tr>
						<tr>
							<th scope="row">구분</th>
							<td><c:out value="${websiteDt.SITE_TYPE_NAME}"/></td>
						</tr>
						<tr>
							<th scope="row">도메인</th>
							<td><c:out value="${websiteDt.SITE_DOMAIN}"/></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
		<c:set var="contMId" value="37"/>
		<c:set var="contTitle" value="관리 사이트"/>
		<div class="Site">
			<div class="head">
				<h3>${contTitle}</h3>
				<a href="${elfn:getMenuUrl(contMId)}" class="more">더보기</a>
			</div>
			<div class="Nlist">
				<table class="tbListA" summary="<c:out value="${contTitle}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
					<caption><c:out value="${contTitle}"/> 목록</caption>
					<colgroup>
						<col />
						<col width="60px" />
						<col width="70px" />
						<col width="100px" />
						<col width="60px" />
						<col width="90px" />
					</colgroup>
					<thead>
						<tr>
							<th scope="col">사이트명</th>
							<th scope="col">언어</th>
							<th scope="col">메뉴콘텐츠</th>
							<th scope="col">IA(정보구조)</th>
							<th scope="col">통계</th>
							<th scope="col">바로가기</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${empty websiteList}">
						<tr>
							<td colspan="6" class="nolist"><c:out value="${contTitle}"/> 정보가 없습니다.</td>
						</tr>
					</c:if>
					<c:set var="menuContMngMId" value="10"/>
					<c:set var="menuMngMId" value="42"/>
					<c:set var="statsMngMId" value="47"/>
					<c:forEach var="listDt" items="${websiteList}">
						<tr<c:if test="${listDt.SITE_ID == usrSiteVO.siteId}"> class="on"</c:if>>
							<td scope="row" class="tlt"><c:out value="${listDt.SITE_NAME}"/></td>
							<td><c:out value="${listDt.LOCALE_LANG_NAME}"/></td>
							<td><a href="${elfn:getMenuUrl(menuContMngMId)}" data-id="<c:out value="${listDt.SITE_ID}"/>" class="btnTypeF btn_search_website_link">관리</a></td>
							<td><a href="${elfn:getMenuUrl(menuMngMId)}" data-id="<c:out value="${listDt.SITE_ID}"/>" class="btnTypeF btn_search_website_link">관리</a></td>
							<td><a href="${elfn:getMenuUrl(statsMngMId)}" data-id="<c:out value="${listDt.SITE_ID}"/>" class="btnTypeF btn_search_website_link">통계</a></td>
							<td><a href="<c:out value="${contextPath}/${listDt.SITE_ID}" />" target="_blank" class="btnTypeE">바로가기</a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<script type="text/javascript">
		$(function(){
			// 사이트 선택 링크
			$(".Site .btn_search_website_link").click(function(){
				var varSelTrObj = $(this).parent("td").parent("tr");
				var varIsSel = varSelTrObj.hasClass("on");
				if(varIsSel) return true;
				
				/*var varConfirm = confirm("<spring:message code="message.website.selectSite.change.confirm"/>");
				if(!confirm) return false;*/
				
				var varSiteId = $(this).attr("data-id");
				var varForm = $("#fn_searchTWebsiteForm");
				varForm.find("#selToUrl").val($(this).attr("href"));
				varForm.find("#selSiteId option[value='" + varSiteId + "']").prop("selected", true);
				varForm.submit();
				return false;
			});
		});
		</script>
		
		<c:set var="contMId" value="10"/>
		<c:set var="contTitle" value="${dSiteId} 작업 메뉴콘텐츠"/>
		<div class="Contents">
			<div class="head">
				<h3>${contTitle}</h3>
				<a href="${elfn:getMenuUrl(contMId)}" class="more">더보기</a>
			</div>
			<div class="Nlist">
				<table class="tbListA" summary="<c:out value="${contTitle}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
					<caption><c:out value="${contTitle}"/> 목록</caption>
					<colgroup>
						<col width="150px" />
						<col />
						<col width="100px" />
						<col width="60px" />
					</colgroup>
					<thead>
						<tr>
							<th scope="col">메뉴명</th>
							<th scope="col">콘텐츠</th>
							<th scope="col">상태</th>
							<th scope="col">관리</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${empty contentsList}">
						<tr>
							<td colspan="4" class="nolist"><c:out value="${contTitle}"/> 정보가 없습니다.</td>
						</tr>
					</c:if>
					<spring:message var="admSiteId" code="Globals.site.id.adm"/>
					<c:forEach var="listDt" items="${contentsList}">
						<tr>
							<td scope="row" class="tlt"><c:out value="${listDt.MENU_NAME}"/></td>
							<td class="tlt"><c:out value="${listDt.CONTENTS_NAME}"/> > <c:out value="${listDt.BRANCH_NAME}"/> > <c:out value="${listDt.VER_IDX}"/></td>
							<td><c:out value="${listDt.WORK_TYPE_NAME}"/></td>
							<td><a href="<c:out value="${contextPath}/${admSiteId}/menuContents/${websiteDt.SITE_ID}/contents/workInput.do?mId=${listDt.MENU_IDX}&verIdx=${listDt.VER_IDX}"/>" class="btnTypeF">관리</a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		
		<c:set var="contMId" value="49"/>
		<c:set var="contTitle" value="${dSiteId} 현재접속자"/>
		<div class="Stats">
			<div class="head">
				<h3>${contTitle}</h3>
				<a href="${elfn:getMenuUrl(contMId)}" class="more">더보기</a>
			</div>
			<div class="Nlist">
				<table class="tbListA" summary="<c:out value="${contTitle}"/> 목록을 볼 수 있고 제목 링크를 통해서 게시물 상세 내용으로 이동합니다.">
					<caption><c:out value="${contTitle}"/> 목록</caption>
					<colgroup>
						<col width="120px" />
						<col width="150px" />
						<col />
					</colgroup>
					<thead>
						<tr>
							<th scope="col">접속IP</th>
							<th scope="col">접속일자</th>
							<th scope="col">아이디</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${empty statsList}">
						<tr>
							<td colspan="3" class="nolist"><c:out value="${contTitle}"/> 정보가 없습니다.</td>
						</tr>
					</c:if>
					<c:forEach var="listDt" items="${statsList}">
						<tr>
							<td scope="row" class="num"><c:out value="${listDt.ACCESS_IP}"/></td>
							<td class="date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${listDt.REGI_DATE}"/></td>
							<td><c:out value="${listDt.MEMBER_ID}"/></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>