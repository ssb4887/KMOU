<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_tabContentsInputForm"/>	
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
	<jsp:param name="javascript_page" value="${moduleJspRPath}/view.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<div id="cms_tabContents_article">
<c:set var="classItemInfo" value="${moduleItem.class_item_info}"/>
<ul class="tabTypeSub successiveTab">
		<c:set var="tabIdx" value="1"/>
		<c:forEach var="tab" begin="1" end="${tabCnt}">
			<c:set var="tab_name" value="TAB_NAME${tab}"/>
		    
			<c:if test="${!empty dt[tab_name]}">
			<c:set var="tab_contents" value="TAB_CONTENTS${tab}"/>
			<li id="tab${tabIdx}" rel="tabContent${tabIdx}"<c:if test="${tabIdx == 1}"> class="on"</c:if>>
				<a href="#"><span>${dt[tab_name]}</span></a>
			</li>
			<c:set var="tabIdx" value="${tabIdx + 1}"/>
			</c:if>
		</c:forEach>
		<c:if test="${dt.REFERENCE == '1'}">
			<li id="tab${tabIdx}" rel="tabContent${tabIdx}">
				<a href="#"><span>참고문헌</span></a>
			</li>
		</c:if>
		</ul>
		
		<div class="successiveWrap">
		<c:set var="tabIdx" value="1"/>
		<c:forEach var="tab" begin="1" end="${tabCnt}">
			<c:set var="tab_name" value="TAB_NAME${tab}"/>
			<c:if test="${!empty dt[tab_name]}">
			<c:set var="tab_contents" value="TAB_CONTENTS${tab}"/>
			<div id="tabContent${tabIdx}"<c:if test="${tabIdx > 1}"> style="display:none;"</c:if>>
            ${dt[tab_contents] }	

			</div>
			<c:set var="tabIdx" value="${tabIdx + 1}"/>
			</c:if>
		</c:forEach>
		<c:if test="${dt.REFERENCE == '1'}">
			<div id="tabContent${tabIdx}" style="display:none;">
				<c:set var="file_item_name" value="${items['file'][item_name]}"/>
				<div class="Memo">
					<h4>참고자료</h4>
					<table class="tbListA" summary="${file_item_name} 목록">
						<caption class="skip">${file_item_name} 목록</caption>
						<colgroup>
							<col width="8%" />
							<col />
						</colgroup>
						<thead>
							<tr>
								<th scope="col" class="first">번호</th>
								<th scope="col">이름</th>
							</tr>
						</thead>
						<tbody class="pd10 alignC">
						<c:set var="isEmptyFileList" value="${true}"/>
						<c:set var="fileItemId" value="file"/>
						<c:set var="fileHashMap" value="${multiFileHashMap}"/>
						<c:if test="${!empty fileHashMap}">
						<c:set var="fileList" value="${fileHashMap[fileItemId]}"/>
						
						<c:if test="${!empty fileList}"><c:set var="isEmptyFileList" value="${false}"/></c:if>
						<c:forEach var="fileDt" items="${fileList}" varStatus="i">
							<tr>
								<td class="num">${i.count}</td>
								<td class="tlt">
								<a href="${URL_DOWNLOAD}&CALIDX=${fileDt.CLASS_IDX}&fidx=${fileDt.FLE_IDX}&itId=file">${elfn:replaceScript(fileDt.FILE_ORIGIN_NAME)}</a>
								</td>
							</tr>
						</c:forEach>
						</c:if>
						<c:if test="${isEmptyFileList}">
							<tr>
								<td colspan="2" class="nolist">참고자료가 없습니다.</td>
							</tr>
						</c:if>
						</tbody>
					</table>
					<h4>참고 사이트</h4>
					<table class="bbsListA" summary="참고 사이트 목록">
						<caption class="skip">참고 사이트 목록</caption>
						<colgroup>
							<col width="8%" />
							<col />
						</colgroup>
						<thead>
							<tr>
								<th scope="col" class="first">번호</th>
								<th scope="col">사이트</th>
							</tr>
						</thead>
						<tbody class="pd10 alignC">
		               	<c:forEach items="${tabData}" var="listDt" varStatus="i">
							<tr>
								<td class="num">${i.count}</td>
								<td class="tlt">
							<a href="${listDt.URL}" title="tadData${i.count}" target="_blank">${listDt.URL_CON}</a>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty tabData}">
							<tr>
								<td colspan="2" class="nolist">참고 사이트가 없습니다.</td>
							</tr>
						</c:if>
						</tbody>
					</table>
		        </div>
			</div>
		</c:if>
		</div>
		</div>
		
	
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>