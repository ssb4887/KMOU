<%@ include file="../../../../include/commonTop.jsp"%>
<link rel="stylesheet" type="text/css" href ="../../../css/majorInfo.css"/>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination" %>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="inputFormId" value="fn_sampleInputForm"/>
<c:set var="inputWinFlag" value=""/><%/* 등록/수정창 새창으로 띄울 경우 사용 */%>
<c:set var="btnModifyClass" value="fn_btn_modify${inputWinFlag}"/><%/* 수정버튼 class */%>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/copyView.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
	<!-- search -->
	<c:if test="${userTypeIdx ne '45' && userTypeIdx ne '46'}">
		<h4>유의사항</h4>
		<span style="color:red;"> ※ 자료복사를 처리하면, 기존의 입력된 자료는 삭제되고 새로 생성됩니다.</span>
	<div class="tbMSearch">
	    <form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_COPY_PROC}"/>" target="submit_target">
	        <input type="hidden" name="mId" value="${queryString.mId}">
	        <!-- Flex container -->
	        <div class="tbMSearch_p2">
	            <div class="fieldset-container" style="flex:1;">
	                <!-- 첫 번째 fieldset -->
	                <fieldset>
					<dl>
						<dt>대상연도</dt>
						<dd style="width:75%;">
							<select name="s_YY" id="s_YY" class="select"></select>
						</dd>
						<dt>대학</dt>
						<dd style="width:75%;">
						<select name="s_colgCd" id="s_colgCd" class="select" title="대학">
							<option value="">전체</option>
						</select>
						</dd>	
						<dt>학과/전공</dt>
						<dd style="width:75%;">
							<select name="s_fcltSustCd" id="s_fcltSustCd" class="select" title="학부/학과">
								<option value="">전체</option>
							</select>							
							<select name="s_mjCd" id="s_mjCd" class="select" title="전공">
								<option value="">전체</option>
							</select>							
						</dd>						
					</dl>
	                </fieldset>
	            </div>
	            <div class="arrow-container" style="flex: 0 0 auto; font-size: 24px; padding: 0 20px;">
	                <!-- 중앙 화살표, flex-grow: 0, flex-shrink: 0, flex-basis: auto -->
	                &raquo;&raquo;
	            </div>
	            <div class="fieldset-container" style="flex:1;">
	                <!-- 두 번째 fieldset -->
	                <fieldset>
					<dl>
						<dt>기준연도</dt>
						<dd style="width:75%">
							<input type="hidden" id="t_YY" name="t_YY" value="" disabled/><span id="t_YY_ui"></span>
						</dd>
						<dt>대학 </dt>
						<dd style="width:75%">
							<input type="hidden" id="t_colgCd" name="t_colgCd" value=""/><span id="t_colgNm">전체</span>	
						</dd>	
						<dt>학과/전공 </dt>
						<dd style="width:75%">
							<input type="hidden" id="t_fcltSustCd" name="t_fcltSustCd" value=""/><span id="t_fcltSustNm">전체</span>
							<input type="hidden" id="t_mjCd" name="t_mjCd" value=""/><span id="t_mjNm" style="display:none;"> > 전체</span>
						</dd>		
					</dl>					
	                </fieldset>
	            </div>
	        </div>
		<p>
			<input type="submit" class="btnSearch fn_btn_submit" value="복사" title="복사"/>
		</p>
	    </form>
	</div>
	</c:if>
	<!-- //search -->
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
<%-- <c:if test="${!empty BOTTOM_PAGE}"><jsp:include page="${BOTTOM_PAGE}" flush="false"/></c:if> --%>