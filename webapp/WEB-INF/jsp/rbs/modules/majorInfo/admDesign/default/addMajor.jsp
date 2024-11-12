<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item"%>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}" />
<c:set var="searchFormId" value="fn_codeMstrSearchForm" />
<c:set var="listFormId" value="fn_codeMstrListForm" />
<c:set var="headTitle" value="교과목 등록" />

<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${headTitle }" />
		<jsp:param name="javascript_page"
			value="${moduleJspRPath}/addMajorInput.jsp" />
		<jsp:param name="searchFormId" value="${searchFormId}" />
		<jsp:param name="listFormId" value="${listFormId}" />
	</jsp:include>
</c:if>
<div id="cms_board_article">

	<div class="plan_admin">
		<div class="plan_admin_L">
			<div class="bx_top">
				<form name="${searchFormId}" id="${searchFormId}" method="get" action="<c:out value="${URL_ADD_MAJOR}"/>">
					<input type="hidden" name="mId" value="${queryString.mId}">
					<input type="hidden" name="majorIdx" value="${queryString.majorIdx}">
					<input type="hidden" name="yearIdx" value="${queryString.yearIdx}">
					<fieldset>
					<!-- <fieldset id="topfieldset"> -->
						<!-- <select name="field" id="s_field" class="select" title="진출분야">
							<option value="">전체</option>
						</select> -->
						<input placeHolder="교과명을 입력해 주세요" type="text" value="" title="sbjtNm" name="sbjtNm"></input>
						<input type="submit" id="fn_btn_search" class="btnTypeL" value="검색" title="검색"></input>
					</fieldset>
				</form>
			</div>
			<div class="bx_content">
				<div class="title">
					<h5>검색된 항목</h5>
					<!-- <ul>
						<li><button class="">전체추가</button></li>
					</ul> -->
				</div>
				<div class="planad_content">
					<div class="plan_bx">
						<!-- 교과목선택 리스트 시작 -->
						<c:set var="listIdxName" value="${settingInfo.idx_name}" />
						<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
						<c:forEach var="listDt" items="${addMajorList}" varStatus="i">
						<div class="plan_list">
							<ul>
								<li class="title"><span>${listNo }</span>${listDt.SUBJECT_NM }(${listDt.SUBJECT_CD})</li>
								<li>
									<dl>
										<dt>개설 년도</dt>
										<dd>${listDt.YEAR}</dd>
									</dl>
									<dl>
										<dt>수강대상 학년</dt>
										<dd>${listDt.COM_GRADE}</dd>
									</dl>
									<dl>
										<dt>학기구분</dt>
										<dd>${listDt.SMT_NM}</dd>
									</dl>
									<dl>
										<dt>학점</dt>
										<dd>${listDt.CDT_NUM}</dd>
									</dl>
								</li>
							</ul>
							<div>
								<button class="btn_st2" id="preSelect${i.index }">선택</button>
							</div>
							<input type="hidden" name="grade${i.index }" id="grade${i.index }" value="${listDt.COM_GRADE }"/>
							<input type="hidden" name="year${i.index }" id="year${i.index }" value="${listDt.YEAR }"/>
							<input type="hidden" name="smt${i.index }" id="smt${i.index }" value="${listDt.SMT }"/>
							<input type="hidden" name="cdtNum${i.index }" id="cdtNum${i.index }" value="${listDt.CDT_NUM }"/>
							<input type="hidden" name="subjectNm${i.index }" id="subjectNm${i.index }" value="${listDt.SUBJECT_NM }"/>
							<input type="hidden" name="subjectCd${i.index }" id="subjectCd${i.index }" value="${listDt.SUBJECT_CD }"/>
							<input type="hidden" name="comdivCd${i.index }" id="comdivCd${i.index }" value="${listDt.COMDIV_CODE }"/>
							<input type="hidden" name="comdivNm${i.index }" id="comdivNm${i.index }" value="${listDt.COMDIV_CODE_NM }"/>
							<input type="hidden" name="smtNm${i.index }" id="smtNm${i.index }" value="${listDt.SMT_NM }"/>
							<input type="hidden" name="openDeptCd${i.index }" id="openDeptCd${i.index }" value="${listDt.DEPT_CD }"/>
							<input type="hidden" name="openDeptNm${i.index }" id="openDeptNm${i.index }" value="${listDt.DEPT_NM }"/>
						</div>
						<c:set var="listNo" value="${listNo + 1}" />
						</c:forEach>
					
							
							
						
						<!-- //교과목선택 리스트 끝 -->
					</div>
				</div>
			</div>
		</div>
		<div class="plan_admin_C"></div>
		<div class="plan_admin_R">
			<div class="bx_content">
				<div class="title">
					<h5>선택된 항목</h5>
					<ul>
						<li><button class="btn_st1" data-bs-backdrop="static" data-bs-keyboard="false" data-bs-toggle="modal" data-bs-target="#majorbusModal">직접입력</button></li>
						<li><button type="button" onclick="selDelCard();" class="btn_st2">선택삭제</button></li>
						<li><button type="button" onclick="allDelCard();">비우기</button></li>
					</ul>
				</div>
				<form id="${listFormId}" name="${listFormId}" method="post" target="list_target">
				<div class="planad_content">
					<div class="plan_bx" id="eduList">
						<!-- 교과목선택 리스트 시작 -->
						<!-- <div class="plan_list" >
							<ul>
								<li class="title"><span><input type="checkbox" id="select" name="select" title="선택" value=""></span>해양문화분석능력</li>
								<li>
									<dl>
										<dt>전공능력명</dt>
										<dd>21세기 세계의 변화에 적극적으로 대처하는 해양문화분석능력</dd>
									</dl>
									<dl>
										<dt>상위능력명</dt>
										<dd>-</dd>
									</dl>
								</li>
							</ul>
							<div>
								<button>삭제</button>
							</div>
						</div> -->
						
						<div class="plan_list noitem">
							<p>교과목을 추가해주세요.</p>
						</div>
						<!-- //교과목선택 리스트 끝 -->
					</div>
				</div>
				</form>
				<div class="planad_btnbx">
					<fieldset id="topfieldset">
						<select name="field" id="s_field" class="select" title="진출분야">
							<option value="">진출분야 선택</option>
						</select>
					</fieldset>
					<input type="submit" id="saveSub" class="btnTypeE fn_btn_submit" value="교과목 추가" title="교과목 추가" />
				</div> 
			</div>
		</div>
	</div>


	<%-- <form id="${listFormId}" name="${listFormId}" method="post"
		target="list_target">
		<div>
		<table id="tpSubContent" class="tbListA"
			summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption>
				<c:out value="${settingInfo.list_title}" />
				목록
			</caption>
			<colgroup>
				<col width="5%" />
				<col width="5%" />
				<col width="15%" />
				<col width="15%" />
				<col width="15%" />
				<col />
			</colgroup>
			<thead>
				<tr>
					<c:if test="${isMajorInfo ne 'Y'}">
						<th scope="col"><c:if test="${mngAuth}">
								<input type="checkbox" id="selectAll" name="selectAll"
									title="<spring:message code="item.select.all"/>" />
							</c:if></th>
					</c:if>
					<th scope="col">No</th>
					<th scope="col">교과구분</th>
					<th scope="col">개설학년</th>
					<th scope="col">학기구분</th>
					<th scope="col">교과목번호</th>
					<th scope="col">교과목명</th>


					<!-- 마지막 th에 class="end" -->
				</tr>
			</thead>
		</table>
		</div>
		<div class="scrollable-tbody">
			<table id="tpSubContent" class="tbListA"
				summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
				<colgroup>
					<col width="5%" />
					<col width="5%" />
					<col width="15%" />
					<col width="15%" />
					<col width="15%" />
					<col />
				</colgroup>
				
				<tbody class="alignC">
					<c:if test="${empty addMajorList}">
						<tr>
							<td colspan="7" class="bllist"><spring:message
									code="message.no.list" /></td>
						</tr>
					</c:if>
					<c:set var="listIdxName" value="${settingInfo.idx_name}" />
					<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
					<c:forEach var="listDt" items="${addMajorList}" varStatus="i">
						<tr>

							<td><c:if test="${mngAuth}">
									<input type="checkbox" id="Oselect${i.index }" name="select"
										title="<spring:message code="item.select"/>"
										value="${listKey}" />
								</c:if></td>
							<td class="num">${listNo}</td>
							<td value="${listDt.COMDIV_CODE}">${listDt.COMDIV_CODE_NM}</td>
							<td>${listDt.COM_GRADE}</td>
							<td>${listDt.SMT_NM}</td>
							<td>${listDt.SUBJECT_CD}</td>
							<td>${listDt.SUBJECT_NM}</td>
							
							<input type="hidden" name="YEAR" +${i.index} value="${listDt.YEAR}"></input>
							<input type="hidden" name="MAJOR_CD" +${i.index} value="${maorjCd}"></input>
							<input type="hidden" name="SMT" +${i.index} value="${listDt.SMT}"></input>
							<input type="hidden" name="SUBJECT_CD" +${i.index} value="${listDt.SUBJECT_CD}"></input>
							<input type="hidden" name="SUBJECT_NM" +${i.index} value="${listDt.SUBJECT_NM}"></input>
							<input type="hidden" name="DEPT_CD" +${i.index} value="${listDt.DEPT_CD}"></input>
							<input type="hidden" name="DEPT_NM" +${i.index} value="${listDt.DEPT_NM}"></input>
							<input type="hidden" name="COMDIV_CODE" +${i.index}	value="${listDt.COMDIV_CODE}"></input>
							<input type="hidden" name="COMDIV_CODE_NM" +${i.index} value="${listDt.COMDIV_CODE_NM}"></input>
							<input type="hidden" name="COM_GRADE" +${i.index} value="${listDt.COM_GRADE}"></input>
							<input type="hidden" name="CDT_NUM" +${i.index} value="${listDt.CDT_NUM}"></input>
							<input type="hidden" name="WTIME_NUM" +${i.index} value="${listDt.WTIME_NUM}"></input>
							<input type="hidden" name="PTIME_NUM" +${i.index} value="${listDt.PTIME_NUM}"></input>
							
						</tr>
						<c:set var="listNo" value="${listNo + 1}" />
					</c:forEach>
				</tbody>
			</table>
		</div>
	</form> --%>

</div>

<div class="btnCenter">
	<!-- <input type="submit" id="saveSub" class="btnTypeA fn_btn_submit" value="교과목 추가" title="교과목 추가" /> --> 
		<a href="javascript:self.close();" title="닫기" class="btnTypeB back_to_list"> 닫기 </a>
</div>


<div class="modal fade modal-xl modal_syllabus" id="majorbusModal" tabindex="-1" aria-labelledby="syllabusModalLabel" aria-hidden="true">
	
	<input type="hidden" name="majorCd" value="${param.majorCd }">
	<input type="hidden" id="modi" name="modi" value="">
	<input type="hidden" id="abtyCd" name="abtyCd" value="">
    <div class="modal-dialog modal-dialog-scrollable">
      <div class="modal-content">
        <div class="modal-header bg-black">
            <h1 class="modal-title fs-5 text-white" id="syllabusModalLabel">전공능력 등록</h1>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                <img src="../../../images/ico_w_close.png" alt="닫기 아이콘"/>
            </button>
        </div>
        <div class="modal-body">
			<section class="mt-4">
            	<table class="tbWriteA">
                    <caption class="blind">전공능력 등록</caption>
                    <colgroup>
                        <col width="10%">
                        <col width="20%">
                        <col width="20%">
                        <col width="10%">
                        <col width="20%">
                        <col width="20%">
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="row">교과목 명</th>
                            <td colspan="2"><input name="sbjtNmDir" id="sbjtNmDir" class="inputTxt" title="교과목 명" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;"></td>
                            <th scope="row">교과목 번호</th>
                            <td colspan="2"><input name="sbjtCdDir" id="sbjtCdDir" class="inputTxt" title="교과목 번호" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;"></td>
                        </tr>
                        <tr>
                            <th scope="row">년도</th>
                            <td colspan="2"><input name="yearDir" id="yearDir" class="inputTxt" title="년도" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;"></td>
                            <th scope="row">학기코드</th>
                            <td colspan="2">
                            	<select class="select" name="smtDir" id="smtDir" title="학기코드" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;">							
									<option value="GH0210">1학기</option>
									<option value="GH0211">여름계절학기</option>
									<option value="GH0220">2학기</option>
									<option value="GH0221">겨울계절학기</option>
								</select>
							</td>
                        </tr>
                        <tr>
                            <th scope="row">학년구분 </th>
                            <td colspan="2"><input name="gradeDir" id="gradeDir" class="inputTxt" title="학년구분" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;"></td>
                            <th scope="row">학점</th>
                            <td colspan="2"><input name="cdtNumDir" id="cdtNumDir" class="inputTxt" title="학점" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;"></td>
                        </tr>
                    </tbody>
                </table>
                <span>학기코드) 1학기 GH0210 2학기 GH0220</span>
                <div class="btnbx_right">
						<button type="button" class="btnTypeJ addDir">저장</button>
            			<button type="button" class="btnTypeI fn-abtyRegi-cancel" data-bs-dismiss="modal" aria-label="Close">취소</button>
				</div>
            </section>
        </div>
      </div>
    </div>
  </div>

<c:if test="${!empty BOTTOM_PAGE}"><jsp:include
		page="${BOTTOM_PAGE}" flush="false" /></c:if>