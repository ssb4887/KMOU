<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_tabContentsInputForm"/>	
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<c:set var="classItemInfo" value="${moduleItem.class_item_info}"/>

	<div id="cms_tabContents_article">
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_SUBMITPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<table class="tbWriteA" summary="글쓰기 서식">
			<caption>
			글쓰기 서식
			</caption>
			<colgroup>
			<col style="width:120px;" />
			<col />
			<col style="width:120px;" />
			<col />
			</colgroup>
			<tbody>
				<tr>
					<itui:itemText itemId="clName" itemInfo="${classItemInfo}" colspan="3"/>
				</tr>
				<tr class="fn_dmng" style="display:none;">
					<itui:itemText itemId="dmgDepart" itemInfo="${classItemInfo}" colspan="3"/>
				</tr>
			     <tr class="fn_dmng" style="display:none;">
					<itui:itemText itemId="dmgLdate" itemInfo="${classItemInfo}" colspan="3"/>
				</tr>
			    <tr class="fn_dmng" style="display:none;">
					<itui:itemText itemId="dmgName" itemInfo="${classItemInfo}" colspan="3"/>
				</tr>
				<tr class="fn_dmng" style="display:none;">
					<itui:itemText itemId="dmgPhone" itemInfo="${classItemInfo}" colspan="3" />
				</tr>
				<tr>
					<th><label for="classIdx" class="required">위치</label></th>
					<td colspan="3">
						<select id="classIdx" name="classIdx" style="width:500px;">
							<option value="">위치 선택</option>
						<c:forEach var="ordDt" items="${ordList}" varStatus="i">
							<option value="<c:out value="${ordDt.OPTION_CODE}"/>"<c:if test="${ordDt.OPTION_CODE == dt.CLASS_IDX}"> selected="selected"</c:if>><c:if test="${ordDt.OPTION_LEVEL > 1}"><c:forEach var="optLevel" begin="2" end="${ordDt.OPTION_LEVEL}">&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></c:if><c:out value="${ordDt.OPTION_NAME}"/></option>
						</c:forEach>
						</select>
						<div style="display:inline-block; margin-left:10px;">
							<itui:objectRadio itemId="ordType" itemInfo="${itemInfo}"/> 
						</div>
					</td>
				</tr>
				<tr>
					<th><label for="콘텐츠" class="title">콘텐츠</label></th>
					<td colspan="3" class="form_area">
						<select name="tabMaxCnt" id="tabMaxCnt" title="콘텐츠 개수">
							<option value="">미사용</option>
							<c:forEach var="tabMaxCnt" begin="1" end="${tabCnt}">
							<option value="${tabMaxCnt}">${tabMaxCnt} 개</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th><label for="참고문헌" class="title">참고문헌</label></th>
				    <td colspan="3">
						<itui:objectCheckboxSG itemId="reference"  itemInfo="${itemInfo}" optnHashMap="${optnHashMap}"/>
					</td>
				</tr>
			</tbody>
		</table>
		<div id="c_w_h_tab">
			<ul id="fn_formTab">
			<c:set var="refIdx" value="${tabCnt + 1}"/>
			<c:forEach var="tab" begin="1" end="${tabCnt}">
				<li><a href="#form_tab${tab}" data-idx="${tab}">콘텐츠${tab}</a></li>
			</c:forEach>
				<li><a href="#form_tab${refIdx}">참고문헌 </a></li>
			</ul>
		</div>
			
		<div id="c_w_h_tab_con" class="cont_reg" style="border-top:0px; margin-top:0px;">
			<c:forEach var="tab" begin="1" end="${tabCnt}" varStatus="i">
				<c:set var="tabName" value="tabName${tab}"/>
				<c:set var="tabCont" value="tabCont${tab}"/>
				<div<c:if test="${!i.first}"> class="hidden"</c:if> id="form_tab${tab}">
					<table class="tbWriteA"  summary="글쓰기 서식">
						<caption>
						글쓰기 서식
						</caption>
						<colgroup>
						<col style="width:120px;" />
						<col />
						</colgroup>
						<tbody>
							<tr>
								<itui:itemText itemId="tabName${tab}" itemInfo="${itemInfo}" colspan="3"/>
							</tr>
							<tr>
								<itui:itemTextarea itemId="tabCont${tab}" itemInfo="${itemInfo}" colspan="3"/>
							</tr>
						</tbody>
			        </table>
			    </div>
			</c:forEach>
				<div id="form_tab${refIdx}" class="hidden">
				
					<table class="tbWriteA"  summary="글쓰기 서식">
						<caption>
						글쓰기 서식
						</caption>
						<colgroup>
						<col style="width:120px;" />
						<col />
						</colgroup>
						<tbody>
							<tr>
								<itui:itemMultiFile itemId="file" itemInfo="${itemInfo}"/>
							</tr>
							<tr>
								<th scope="row">
									참고 사이트
									<button type="button" class="btnTypeG fn_add_down" title="">추가</button>
								</th>
								<td class="data_con">
									<c:choose>
				                	<c:when test="${!empty tabData}">
				                	<c:forEach items="${tabData}" var="listDt" varStatus="i">
									<dl>
										<dd>url	<input type="text" class="fn_url inTxt" name="url" title="url" maxlength="250" value="<c:out  value="${listDt.URL}"/>"/>
										설명  <input type="text" class="fn_url_con inTxt" name="url_con" title="설명" maxlength="250" value="<c:out  value="${listDt.URL_CON}"/>"/>
											<input type="hidden" name="con_idx" value="<c:out  value="${listDt.CON_IDX}"/>" class="fn_con_idx"/>
										    <button type="button" class="btnTypeF fn_add_delete" title="">삭제</button>
										</dd>
									</dl>
									</c:forEach>
									</c:when>
									<c:otherwise>
										<dl>
											<dd>url <input type="text" class="fn_url inTxt" name="url" title="url" maxlength="250" value="${tabData.URL }" style ="width: 300px;"/>
											설명<input type="text" class="fn_url_con inTxt" name="url_con" title="설명" maxlength="250" value="" style ="width: 300px;"/>
											<input type="hidden" name="con_idx" value="0" class="fn_con_idx"/>
											<button type="button" class="btnTypeF fn_add_delete" title="">삭제</button>
											</dd>
										</dl>
									</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#" title="취소" class="btnTypeB fn_btn_cancel">취소</a>
		</div>
		</form>
	</div>
<c:if test="${!empty BOTTOM_PAGE}"><jsp:include page = "${BOTTOM_PAGE}" flush = "false"/></c:if>