<%@ include file="../../../../include/commonTop.jsp"%>
<link rel="stylesheet" href="../../../assets/css/style.css">
<link rel="stylesheet" type="text/css" href="${contextPath}${cssAssetPath}/sub.css">
<script src="../../../assets/js/jquery-3.7.1.min.js"></script>
<script src="../../../assets/js/slick.js"></script>
<script src="../../../assets/js/bootstrap.min.js"></script>
<script src="../../../assets/js/bootstrap.bundle.min.js"></script>
<script src="../../../assets/js/index.global.min.js"></script>
<script src="../../../assets/js/common.js"></script>
<script src="../../../assets/js/sub.js"></script>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<c:set var="inputFormId" value="fn_sampleInputForm"/>
<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="javascript_page" value="${moduleJspRPath}/input.jsp"/>
		<jsp:param name="inputFormId" value="${inputFormId}"/>
	</jsp:include>
</c:if>
<c:set var="itemOrderName" value="${submitType}_order"/>
<c:set var="itemOrder" value="${itemInfo[itemOrderName]}"/>
<c:set var="itemObjs" value="${itemInfo.items}"/>
	<div id="cms_board_article">

		
		<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_INPUTPROC}"/>" target="submit_target" enctype="multipart/form-data">
		<input type="hidden" name="staffNo"  value="${dt.STAFF_NO }">
			
		<%-- table summary, 항목출력에 사용 --%>
		<c:set var="exceptIdStr">제외할 항목id를 구분자(,)로 구분하여 입력(예:name,notice,subject,file,contents,listImg)</c:set>
		<c:set var="exceptIds" value="${fn:split(exceptIdStr,',')}"/>
		<%-- 
			table summary값 setting - 테이블 사용하지 않는 경우는 필요 없음
			디자인 문제로 제외한 항목(exceptIdStr에 추가했으나 table내에 추가되는 항목)은 수동으로 summary에 추가
			예시)
			<c:set var="summary"><itui:objectItemName itemInfo="${itemInfo}" itemId="subject"/>, <spring:message code="item.reginame1.name"/>, <spring:message code="item.regidate1.name"/>, <spring:message code="item.board.views.name"/>, <c:if test="${useFile}"><spring:message code="item.file.name"/>, </c:if><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/><spring:message code="item.contents.name"/>을 제공하는 표</c:set>
		--%>
		<c:set var="summary"><itui:tableSummary items="${items}" itemOrder="${itemOrder}" exceptIds="${exceptIds}"/> 입력표</c:set>
		                <main class="profss_dtl_wrap">
                    <h5 class="fw-semibold mb-3">기본정보</h5>                    
                    <div class="profss_info d-flex flex-column flex-sm-row gap-4 align-items-stretch">
                        <div class="photo_box">
							<c:if test="${!empty photo}">
								<img src="data:image/png;base64,${photo}" onError="${contextPath}/${crtSiteId}/assets/images/contents/human.png" alt="교수 사진" style="width:150px;height:auto;">
							</c:if>
									
				
							<c:if test="${empty photo}">
								<img src="/web/assets/images/contents/human.png" onError="/web/assets/images/contents/human.png" alt="교수 사진">
							</c:if>
                        </div>
                        <table class="table mb-0">
                            <caption class="blind">기본정보</caption>
                            <colgroup>
                                <col width="10%"/>
                                <col width="40%"/>
                                <col width="10%"/>
                                <col width="40%"/>
                            </colgroup>
                            <tbody>
                                <tr>
                                    <th scope="col" class="align-middle ps-3">교수명</th>
                                    <td class="align-middle"><c:out value="${dt.KOR_NM }"/></td>
                                    <th scope="col" class="align-middle ps-3">소속</th>
                                    <td class="align-middle"><c:out value="${dt.DEPT_KOR_NM }"/></td>
                                </tr>
                                <tr>
                                    <th scope="col" class="align-middle ps-3">전화번호</th>
                                    <td class="align-middle text-break"><itui:objectText itemId="telNo" itemInfo="${itemInfo}" objStyle="width:100%;" objClass="onlyNumDash"/></td>
                                    <th scope="col" class="align-middle ps-3">이메일</th>
                                    <td class="align-middle text-break"><itui:objectText itemId="email" itemInfo="${itemInfo}" objStyle="width:100%;"/></td>
                                </tr>
                                <tr>
                                    <th scope="col" class="align-middle ps-3">연구실</th>
                                    <td class="align-middle text-break"><itui:objectText itemId="lab" itemInfo="${itemInfo}" objStyle="width:100%;"/></td>
                                    <th scope="col" class="align-middle ps-3">홈페이지</th>
                                    <td class="align-middle text-break">
                                    <itui:objectText itemId="homePage" itemInfo="${itemInfo}" objStyle="width:100%;" /><br>
                                    <input type="checkbox" id="homePagePubcYn" <c:if test="${dt.HOME_PAGE_PUBC_YN eq 'Y' }">checked</c:if>/> 동의
                                    <input type="hidden" name="homePagePubcYn" id="hiddenHomePageValue" value="N"/>                                                                        
                                   	</td>
                                </tr>                                
                            </tbody>
                        </table>
                    </div>
                </main>
        <p class="title" style="font-size:18px; color:black;"><strong>※ 학사행정정보시스템의 인사 및 연구정보 자료를 연계하여 가져오며,<br>동의항목을 선택·수정하여 등록을 할 수 있습니다. 또한 등록된 정보만 학생에게 조회됩니다.</strong></p>
        <br>
		<%-- 2. 디자인에 맞게 필요한 항목만 출력하는 경우 --%>
		<table class="tbWriteA" summary="${summary}">
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>
				
				<tr>
<%-- 					<th scope="row"><itui:objectLabel itemId="${itemId}" itemInfo="${itemInfo}"/></th> --%>
					<th><c:if test="${usertypeIdx ne 45 }"><span style="color:red;"><strong>* </strong></span></c:if>위임장</th>			
					<td colspan="3">
						<itui:profFile objClass="file" itemId="file" itemInfo="${itemInfo}" encfileOriginName="${encfileOriginName }"/>
					</td>					
					<td colspan="2">
<%-- 						<button onclick="location.href='${URL_DOWNLOAD}&originFileName=/sample/api호출결과_1.png&savedFileName=/sample/api호출결과_1.png'" class="btnTypeA" style="width:200px;" title="위임장 양식 다운로드">위임장 양식 다운로드</button> --%>
						
						<a href="${URL_DOWNLOAD}&isSample=Y" style="width:150px;" class="btnTypeA">위임장 양식 다운로드</a>
					</td>
				</tr>			

					<tr><itui:itemTextarea itemId="shcr" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
					<tr><th>학력동의여부</th>                                    
					<td colspan="5">
						<input type="checkbox" id="shcrPubcYn" <c:if test="${dt.SHCR_PUBC_YN eq 'Y' }">checked</c:if>/> 동의
		                 <input type="hidden" name="shcrPubcYn" id="hiddenShcrValue" value="N"/>   
	                 </td>
	                </tr>
					<tr><itui:itemTextarea itemId="carr" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
					<tr><th>경력동의여부</th>                                    
					<td colspan="5">
						<input type="checkbox" id="carrPubcYn" <c:if test="${dt.CARR_PUBC_YN eq 'Y' }">checked</c:if>/> 동의
		                 <input type="hidden" name="carrPubcYn" id="hiddenCarrValue" value="N"/>   
	                 </td>
	                </tr>
			</tbody>
		</table>
		<br>
		<table class="tbWriteA" summary="${summary}">
			<colgroup>
			<col style="width:150px;" />
			<col />
			</colgroup>
			<tbody>				
				<tr class="alignR"><td colspan="6" style="text-align:center;"><span style="float:center;"><a href="#;" class="btnTypeG alignC" onclick="window.open('${URL_ARCH_VIEW}&staffNo=${dt.STAFF_NO}', '_blank', 'width=1200, height=1200')">연구업적시스템 자료 확인</a></span></td></tr>
				<tr><itui:itemTextarea itemId="rechFld" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
				<tr><th>주연구분야 동의여부</th>                                    
				<td colspan="5">
					<input type="checkbox" id="rechFldPubcYn" <c:if test="${dt.RECH_FLD_PUBC_YN eq 'Y' }">checked</c:if>/> 동의
	                 <input type="hidden" name="rechFldPubcYn" id="hiddenRechFldValue" value="N"/>   
                 </td>
                </tr>
				<tr><itui:itemTextarea itemId="thssRech" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
				<tr><itui:itemTextarea itemId="bookThss" itemInfo="${itemInfo}" objStyle="height:310px; width:100%;" colspan="5"/></tr>
				<tr><th>논문연구실적 및 저역서 동의여부</th>                                    
				<td colspan="5">
					<input type="checkbox" id="thssRechPubcYn" <c:if test="${dt.THSS_RECH_PUBC_YN eq 'Y' }">checked</c:if>/> 동의
	                 <input type="hidden" name="thssRechPubcYn" id="hiddenThssRechValue" value="N"/>   
                 </td>
                </tr>
			</tbody>
		</table>
		<div class="btnCenter">
			<input type="submit" class="btnTypeA fn_btn_submit" value="저장" title="저장"/>
			<a href="#;" class="btnTypeC" onclick="window.open('${URL_PREVIEW}&staffNo=${dt.STAFF_NO}', '_blank', 'width=1200, height=1200')">미리보기</a>					
			<input type=button value="취소"  class="btnTypeB fn_btn_cancel">			
		</div>
		</form>
	</div>	
	<div id="footerWrap" style="bottom:auto; width:93%">
		<div id="footer">
			<p class="copyright"><c:out value="${siteInfo.site_copyright}"/></p>
		</div>
	</div>
