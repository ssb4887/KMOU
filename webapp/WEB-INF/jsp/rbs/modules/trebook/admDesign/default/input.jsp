<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_boardInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<div id="cms_board_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<table class="tbWriteA" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<itui:itemInputAll itemInfo="${itemInfo}" itemOrderName="${submitType}_${settingInfo.use_pdf_upload}_order"/>
			</tbody>
		</table>
				
		<c:if test="${settingInfo.use_cont_list == 1}">
		<br/>
		<h4>내용</h4>
		<div class="cont_reg" id="fn_input_table2">
			<div class="ebook_con" id="fn_textareaUl">
		<c:set var="conItems" value="${moduleItem.con_item_info.items}"/>
		<c:choose>
	       	<c:when test="${!empty conList}">
	       	<c:set var="listIdxName" value="${settingInfo.idx_name}"/>
	       	<c:forEach items="${conList}" var="listDt" varStatus="i">
				<dl<c:if test="${i.first}"> class="first"</c:if>>
					<dt><label for="conNum"<c:if test="${conItems.conNum.required_write == 1}"> class="required"</c:if>>번호</label></dt>
					<dd class="num"><input type="text" class="inputTxt" id="conNum" name="conNum" title="내용 번호" maxlength="3" value="<c:out value="${listDt.CON_NUM}"/>"/></dd>
					<dt><label for="conSubject"<c:if test="${conItems.conSubject.required_write == 1}"> class="required"</c:if>>제목</label></dt>
					<dd class="subject"><input type="text" class="inputTxt" id="conSubject" name="conSubject" title="내용 제목" maxlength="200" value="<c:out value="${listDt.CON_SUBJECT}"/>"/></dd>
			<c:choose>
				<c:when test="${settingInfo.use_pdf_upload == 1}">
					<dt><label for="conPdfFile"<c:if test="${conItems.conPdfFile.required_write == 1}"> class="required"</c:if>>PDF 파일</label></dt>
					<dd class="pdf_file">
						<itui:objectFile itemId="conPdfFile" itemInfo="${moduleItem.con_item_info}" objDt="${listDt}"/>
					</dd>
				</c:when>
				<c:otherwise>
					<dt><label for="conLinkUrl"<c:if test="${conItems.conLinkUrl.required_write == 1}"> class="required"</c:if>>다운로드경로</label></dt>
					<dd class="link"><input type="text" class="inputTxt" name="conLinkUrl" title="링크경로" maxlength="300" value="${listDt.CON_LINK_URL}"/></dd>
				</c:otherwise>
			</c:choose>
					<dt><label for="conContents"<c:if test="${conItems.conContents.required_write == 1}"> class="required"</c:if>>내용</label></dt>
					<dd class="content">
						<textarea id="conContents" name="conContents" class="inputTxt" title="내용 "><c:out value="${listDt.CON_CONTENTS}"/></textarea>
	        		</dd>
	        		<dd class="btn">
		        		<input type="hidden" id="conIdx" name="conIdx" value="<c:out value="${i.index}"/>"/>
						<ul>
							<li><button type="button" class="fn_add_up">위 추가</button></li>
	        				<li><button type="button" class="fn_add_delete" title="삭제">삭제</button></li>
	        				<li><button type="button" class="fn_add_down" title="아래 추가">아래 추가</button></li>
	        			</ul>
        			</dd>
	        	</dl>
			</c:forEach>
			</c:when>
			<c:otherwise>
				<dl class="first">
					<dt><label for="conNum"<c:if test="${conItems.conNum.required_write == 1}"> class="required"</c:if>>번호</label></dt>
					<dd class="num"><input type="text" class="inputTxt" id="conNum" name="conNum" title="내용 번호" maxlength="3" /></dd>
					<dt><label for="conSubject"<c:if test="${conItems.conSubject.required_write == 1}"> class="required"</c:if>>제목</label></dt>
					<dd class="subject"><input type="text" class="inputTxt" id="conSubject" name="conSubject" title="내용 제목" maxlength="200" /></dd>
			<c:choose>
				<c:when test="${settingInfo.use_pdf_upload == 1}">
					<dt><label for="conPdfFile"<c:if test="${conItems.conPdfFile.required_write == 1}"> class="required"</c:if>>PDF 파일</label></dt>
					<dd class="pdf_file">
						<itui:objectFile itemId="conPdfFile" itemInfo="${moduleItem.con_item_info}" objDt="${listDt}"/>
					</dd>
				</c:when>
				<c:otherwise>
					<dt><label for="conLinkUrl"<c:if test="${conItems.conLinkUrl.required_write == 1}"> class="required"</c:if>>다운로드경로</label></dt>
					<dd class="link"><input type="text" class="inputTxt" id="conLinkUrl" name="conLinkUrl" title="링크경로" maxlength="300" /></dd>
				</c:otherwise>
			</c:choose>
					<dt><label for="conContents"<c:if test="${conItems.conContents.required_write == 1}"> class="required"</c:if>>내용</label></dt>
					<dd class="content">
						<textarea id="conContents" name="conContents" class="inputTxt" title="내용"></textarea>
	        		</dd>
	        		<dd class="btn">
						<input type="hidden" id="conIdx" name="conIdx" value="0"/>
						<ul>
							<li><button type="button" class="fn_add_up">위 추가</button></li>
	        				<li><button type="button" class="fn_add_delete" title="삭제">삭제</button></li>
	        				<li><button type="button" class="fn_add_down" title="아래 추가">아래 추가</button></li>
	        			</ul>
	        		</dd>
				</dl>
			</c:otherwise>
		</c:choose>
			</div>
		</div>		
		</c:if>
		
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>		
		</form>
	</div>
	
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>