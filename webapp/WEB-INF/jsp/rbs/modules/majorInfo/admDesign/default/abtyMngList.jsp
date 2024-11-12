<%@ include file="../../../../include/commonTop.jsp"%>
<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item"%>
<%@ taglib prefix="pgui" tagdir="/WEB-INF/tags/pagination"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}" />
<c:set var="searchFormId" value="fn_codeMstrSearchForm" />
<c:set var="listFormId" value="fn_codeMstrListForm" />
<c:set var="inputFormId" value="fn-abtyRegi-submit" />
<link rel="stylesheet" type="text/css" href="/RBISADM/css/bootstrap.css">
<!-- <link rel="stylesheet" type="text/css" href="/RBISADM/css/bootstrap.min.css"> -->
<c:set var="headTitle" value="전공능력 관리" />

<c:if test="${!empty TOP_PAGE}">
	<jsp:include page="${TOP_PAGE}" flush="false">
		<jsp:param name="page_tit" value="${headTitle }" />
		<jsp:param name="javascript_page"
			value="${moduleJspRPath}/abtyMngList.jsp" />
		<jsp:param name="searchFormId" value="${searchFormId}" />
		<jsp:param name="listFormId" value="${listFormId}" />
	</jsp:include>
</c:if>
<div id="cms_board_article">

	<%-- <div class="tbMSearch">
		<form name="${searchFormId}" id="${searchFormId}" method="get"
			action="<c:out value="${URL_ADD_MAJOR}"/>">
			<input type="hidden" name="mId" value="${queryString.mId}"> <input
				type="hidden" name="majorIdx" value="${queryString.majorIdx}">
			<input type="hidden" name="yearIdx" value="${queryString.yearIdx}">
			<fieldset id="topfieldset">
				<dl>
					<select name="is_sptPsnKor" id="s_sptPsnKor" class="select"
						title="인재상">
						<option value="">전체</option>
					</select>
					<input placeHolder="교과명을 입력해 주세요" type="text" value=""
						title="sbjtKorNm" name="sbjtKorNm"></input>
					<input type="submit" id="fn_btn_search" class="btnTypeL"
						value="검색" title="검색"></input>
					
				</dl>

			</fieldset>

		</form>
	</div> --%>
	
	<a href="<c:out value="${URL_ABTY_MNG_DEL_PROC}"/>" title="삭제" class="btnTypeF fn_btn_delete">삭제</a>
	<a href="#"  title="등록" class="btnTypeG fn_btn_write${inputWinFlag}" data-bs-toggle="modal" data-bs-target="#syllabusModal">등록</a>
	
	<form id="${listFormId}" name="${listFormId}" method="post"
		target="list_target">
		<div style="width: 101%;">
		<table id="tpSubContent" class="tbListA"
			summary="<c:out value="${settingInfo.list_title}"/> 목록을 볼 수 있고 수정 링크를 통해서 수정페이지로 이동합니다.">
			<caption>
				<c:out value="${settingInfo.list_title}" />
				목록
			</caption>
			<colgroup>
				<col width="5%" />
				<col width="5%" />
				<col width="10%" />
				<col width="15%" />
				<col />
				<col width="5%" />
			</colgroup>
			<thead>
				<tr>
					<%-- <c:if test="${isMajorInfo ne 'Y'}">
						<th scope="col"><c:if test="${mngAuth}">
								<input type="checkbox" id="selectAll" name="selectAll"
									title="<spring:message code="item.select.all"/>" />
							</c:if></th>
					</c:if> --%>
					<th scope="col"></th>
					<th scope="col">No</th>
					<th scope="col">관리</th>
					<th scope="col">전공능력명</th>
					<th scope="col">전공능력 정의</th>
					<th scope="col">상위능력</th>
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
					<col width="10%" />
					<col width="15%" />
					<col />
					<col width="5%" />
				</colgroup>
				
				<tbody class="alignC">
					<c:if test="${empty abtyList}">
						<tr>
							<td colspan="5" class="bllist"><spring:message
									code="message.no.list" /></td>
						</tr>
					</c:if>
					<c:set var="listIdxName" value="${settingInfo.idx_name}" />
					<c:set var="listNo" value="${paginationInfo.firstRecordIndex + 1}" />
					<c:forEach var="listDt" items="${abtyList}" varStatus="i">
						<tr>

							<td><c:if test="${mngAuth}">
									<input type="checkbox" id="select" name="select"
										title="<spring:message code="item.select"/>"
										value="${listDt.ABTY_CD}" />
								</c:if></td>
							<td class="num">${listNo}</td>
							<td><a href="#" class="btnTypeF fn_btn_modify open-modal02" data-bs-toggle="modal" data-bs-target="#syllabusModal" data-value="${listDt.ABTY_CD}" data-major="${listDt.MAJOR_ABTY}" data-defn="${listDt.MAJOR_ABTY_DEFN}" data-parent="${listDt.PARENT_ABTY_CD}">수정</a></td>
							<td>${listDt.MAJOR_ABTY}</td>
							<td>${listDt.MAJOR_ABTY_DEFN}</td>
							<td>${listDt.PARENT_ABTY_NM }</td>


							<input type="hidden" name="YY" +${i.index} value="${listDt.YY}"></input>
							<input type="hidden" name="OPEN_SUST_MJ_CD" +${i.index}
								value="${listDt.OPEN_SUST_MJ_CD}"></input>
							<input type="hidden" name="SHTM_CD" +${i.index}
								value="${listDt.SHTM_CD}"></input>
							<input type="hidden" name="SHTM_NM" +${i.index}
								value="${listDt.SHTM_NM}"></input>
							<input type="hidden" name="COURSE_NO" +${i.index}
								value="${listDt.COURSE_NO}"></input>
							<input type="hidden" name="OPEN_SHYR_FG" +${i.index}
								value="${listDt.OPEN_SHYR_FG}"></input>
							<input type="hidden" name="SHYR_NM" +${i.index}
								value="${listDt.SHYR_NM}"></input>
							<input type="hidden" name="SBJT_KOR_NM" +${i.index}
								value="${listDt.SBJT_KOR_NM}"></input>
							<input type="hidden" name="SBJT_ENG_NM" +${i.index}
								value="${listDt.SBJT_ENG_NM}"></input>
							<input type="hidden" name="SBJT_FG" +${i.index}
								value="${listDt.SBJT_FG}"></input>
							<input type="hidden" name="SBJT_NM" +${i.index}
								value="${listDt.SBJT_NM}"></input>
								
							<input type="hidden" name="PNT" +${i.index}
							value="${listDt.PNT}"></input>
							<input type="hidden" name="THEO_TM_CNT" +${i.index}
							value="${listDt.THEO_TM_CNT}"></input>
							<input type="hidden" name="PRAC_TM_CNT" +${i.index}
							value="${listDt.PRAC_TM_CNT}"></input>

						</tr>
						<c:set var="listNo" value="${listNo + 1}" />
					</c:forEach>
				</tbody>
			</table>
		</div>
	</form>

</div>

<div class="btnCenter">
	<!-- <input type="submit" id="saveSub" class="btnTypeA fn_btn_submit" value="교과목 추가" title="교과목 추가" /> --> 
		<a href="javascript:self.close();" title="닫기" class="btnTypeB back_to_list"> 닫기 </a>
</div>


	
	
	
	<div class="modal fade modal-xl modal_syllabus" id="syllabusModal" tabindex="-1" aria-labelledby="syllabusModalLabel" aria-hidden="true">
	<form id="${inputFormId}" name="${inputFormId}" method="post" action="<c:out value="${URL_ABTY_MNG_INPUT_PROC}"/>" target="submit_target" enctype="multipart/form-data">
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
            	<table class="table">
                    <caption class="blind">전공능력 등록</caption>
                    <colgroup>
                        <col width="20%">
                        <col width="20%">
                        <col width="20%">
                        <col width="20%">
                        <col width="20%">
                    </colgroup>
                    <tbody>
                        <tr>
                            <th scope="row">능력 명</th>
                            <td colspan="4"><input name="majorAbty" id="majorAbty" class="inputTxt" title="능력 명" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;"></td>
                        </tr>
                        <tr>
                            <th scope="row">능력 정의</th>
                            <td colspan="4"><textarea placeholder="능력 정의" class="inputTxt" name="majorAbtyDefn" id="majorAbtyDefn" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;"></textarea></td>
                        </tr>
                        <tr>
                            <th scope="row">상위전공능력</th>
                            <td colspan="4">
                            	<select class="select" name="parentAbty" id="parentAbty" title="상위전공능력" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;">							
									<option value="">상위 전공능력 지정</option>
								</select>	
							</td>
                        </tr>
                        
                        
                    </tbody>
                </table>
                <div class="btnbx_right">
						<button type="button" class="btnTypeJ fn-abtyRegi-submit">저장</button>
            			<button type="button" class="btnTypeI fn-abtyRegi-cancel" data-bs-dismiss="modal" aria-label="Close">취소</button>
				</div>
                
            </section>
        </div>
      </div>
    </div>
    </form>
  </div>
	


<c:if test="${!empty BOTTOM_PAGE}"><jsp:include	page="${BOTTOM_PAGE}" flush="false" /></c:if>