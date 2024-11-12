<%@tag import="net.sf.json.JSONObject"%>
<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="itemOrder" type="net.sf.json.JSONArray"%>
<%@ attribute name="items" type="net.sf.json.JSONObject"%>
<%@ attribute name="preItemId"%>
<%@ attribute name="nextItemId"%>
<%@ attribute name="inputTypeName"%>											<%/* inputType, required 입력구분 (write,modify) */%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="inputFlag" value="${inputTypeName}"/>
<c:if test="${empty inputFlag}"><c:set var="inputFlag" value="${submitType}"/></c:if>
<c:if test="${!empty items && !empty itemOrder}">
<c:set var="requiredName" value="required_${inputFlag}"/>
<c:set var="inputTypeItemName" value="${inputFlag}_type"/>
<c:forEach var="itemId" items="${itemOrder}">
	<c:set var="itemObj" value="${items[itemId]}"/>
	<c:set var="inputType" value="${itemObj[inputTypeItemName]}"/>
	<c:if test="${isAdmMode || inputType != 20}">
		<%/* 관리시스템 || 관리시스템에서만 저장하는 항목 아닌 경우 */ %>
		<c:set var="formatType" value="${itemObj['format_type']}"/>
		<c:set var="objectType" value="${itemObj['object_type']}"/>
		<c:set var="optionType" value="${itemObj['option_type']}"/>
		<c:set var="required" value="${itemObj[requiredName]}"/>
		<c:set var="itemName" value="${elfn:getItemName(itemObj)}"/>
		<c:set var="maximum" value="${itemObj['maximum']}"/>
		<c:set var="itemtype" value="${itemObj['item_type']}"/>
		<c:set var="id" value="${itemId}"/>
		<c:set var="name" value="${itemId}"/>
		<c:if test="${!empty preItemId}">
			<c:set var="id" value="${preItemId}${id}"/>
			<c:set var="name" value="${preItemId}${name}"/>
		</c:if>
		<c:if test="${!empty nextItemId}">
			<c:set var="id" value="${id}${nextItemId}"/>
			<c:set var="name" value="${name}${nextItemId}"/>
		</c:if>
	
	<c:choose>
		<c:when test="${formatType == 1 || formatType == 2}">
		<%/* 전화번호 */%>
		<%--
		try {$(document).on('keydown', '#${id}2', function(event){return fn_onlyNumKD(event);});} catch(e) {}
		try {$(document).on('keydown', '#${id}3', function(event){return fn_onlyNumKD(event);});} catch(e) {}
		 --%>
		<%/* 한글입력막기  */%>
		for(var i = 2 ; i <= 3; i ++) {
			var varId = '${id}' + i;
			try {$(document).on('keydown', '#' + varId, function(event){return fn_onlyNumKD(event);});} catch(e) {}
			try {
				document.getElementById(varId).addEventListener('paste', function(event){
					var clipboardData, pastedData;
	
				    // Stop data actually being pasted into div
				    event.stopPropagation();
				    event.preventDefault();
				
				    // Get pasted data via clipboard API
				    clipboardData = event.clipboardData || window.clipboardData;
				    pastedData = clipboardData.getData('Text');
					fn_preventHan(event, $('#' + varId), pastedData);
				});
				$('#' + varId).keyup(function(event){
					return fn_preventHan(event, $(this));
				});
				$('#' + varId).blur(function(event){
					return fn_preventHan(event, $(this));
				});
			} catch(e) {}
		}
		</c:when>
		<c:when test="${formatType == 3}">
		<%/* 이메일 */%>
		try{$(document).on('change', '#${id}3', function(event){fn_setEmailValue($(this), '${id}2');});}catch(e){}
		try{
			if($("#fn_btn_${id}").length > 0)
			{
				if($('#${id}').val() != ''){ 
					fn_checkObj.checkeds = {};
					fn_useCheckObj($('*[name^=${name}]'), document.getElementById('fn_btn_${id}'), '${id}');
				}
				$("#fn_btn_${id}").click(function(){
					fn_checkObj.check(this, "${id}", {form:$("#fn_${id}ConfirmForm"), obj:$("#fn_${id}ConfirmForm input[name='${name}']")}, "${formatType}");
					return false;
				});
			}
		}catch(e){}
		</c:when>
		<c:when test="${formatType == 4}">
		<%/* 주소 */%>
		<c:set var="addrSearchType" value="${itemObj['search_type']}"/>
		<c:if test="${empty addrSearchType}">
			<spring:message var="addrSearchType" code="Globals.address.search.type" text="0"/>
		</c:if>
			<c:if test="${menuType == 2}">
				<c:set var="addrSearchAddArg">
				, "${contextPath}/${usrSiteInfo.site_id}"
				</c:set>
			</c:if>
			<c:choose>
			<c:when test="${addrSearchType == 'map'}">
				<%/* daum 지도 API */%>
				try{$(document).on('click', '#fn_btn_${id}', function(event){fn_daumMapSearch('${id}'${addrSearchAddArg});return false;});}catch(e){}
			</c:when>
			<c:when test="${addrSearchType == '1'}">
				<%/* 공공데이터포털 새우편번호 도로명주소조회 서비스 */%>
				try{$(document).on('click', '#fn_btn_${id}', function(event){fn_epostAddrSearch('${id}'${addrSearchAddArg});return false;});}catch(e){}
			</c:when>
			<c:when test="${addrSearchType == '2'}">
				<%/* www.juso.go.kr 도로명주소 API */%>
				try{$(document).on('click', '#fn_btn_${id}', function(event){fn_jusoAddrSearch('${id}'${addrSearchAddArg});return false;});}catch(e){}
			</c:when>
			<c:otherwise>
				<%/* daum 우편번호 검색 도로명 주소 API */%>
				try{$(document).on('click', '#fn_btn_${id}', function(event){fn_daumAddrSearch('${id}');return false;});}catch(e){}
			</c:otherwise>
			</c:choose>
		</c:when>
		<c:when test="${formatType == 12}">
		<%/* 사업자번호 */%>
		<%/* 한글입력막기  */%>
		for(var i = 1 ; i <= 3; i ++) {
			var varId = '${id}' + i;
			try {$(document).on('keydown', '#' + varId, function(event){return fn_onlyNumKD(event);});} catch(e) {}
			try {
				document.getElementById(varId).addEventListener('paste', function(event){
					var clipboardData, pastedData;
	
				    // Stop data actually being pasted into div
				    event.stopPropagation();
				    event.preventDefault();
				
				    // Get pasted data via clipboard API
				    clipboardData = event.clipboardData || window.clipboardData;
				    pastedData = clipboardData.getData('Text');
					fn_preventHan(event, $('#' + varId), pastedData);
				});
				$('#' + varId).keyup(function(event){
					return fn_preventHan(event, $(this));
				});
				$('#' + varId).blur(function(event){
					return fn_preventHan(event, $(this));
				});
			} catch(e) {}
		}
		</c:when>
		<c:when test="${formatType == 13}">
		<%/* 법인번호 */%>
		<%/* 한글입력막기  */%>
		for(var i = 1 ; i <= 2; i ++) {
			var varId = '${id}' + i;
			try {$(document).on('keydown', '#' + varId, function(event){return fn_onlyNumKD(event);});} catch(e) {}
			try {
				document.getElementById(varId).addEventListener('paste', function(event){
					var clipboardData, pastedData;
	
				    // Stop data actually being pasted into div
				    event.stopPropagation();
				    event.preventDefault();
				
				    // Get pasted data via clipboard API
				    clipboardData = event.clipboardData || window.clipboardData;
				    pastedData = clipboardData.getData('Text');
					fn_preventHan(event, $('#' + varId), pastedData);
				});
				$('#' + varId).keyup(function(event){
					return fn_preventHan(event, $(this));
				});
				$('#' + varId).blur(function(event){
					return fn_preventHan(event, $(this));
				});
			} catch(e) {}
		}
		</c:when>
		<c:when test="${formatType == 5}">
		<%/* 날짜 */%>
		try{$("#fn_btn_${id}").click(function(event){displayCalendar($('#${id}'),'', this);return false;});}catch(e){}
		try{$("#${id}").click(function(event){displayCalendar($('#${id}'),'', this);return false;});}catch(e){}
		</c:when>
		<c:when test="${formatType == 6 || formatType == 7 || formatType == 8}">
		<%/* 날짜 시:분:초 / 날짜 시:분 / 날짜 시 */%>
		try{$("#fn_btn_${id}1").click(function(event){displayCalendar($('#${id}1'),'', this);return false;});}catch(e){}
		</c:when>
		<c:when test="${formatType == 15}">
		<%/* 날짜(기간) */%>
		try{$("#fn_btn_${id}1").click(function(event){displayCalendar2($('#${id}1'), $('#${id}2'), '', this);return false;});}catch(e){}
		try{$("#fn_btn_${id}2").click(function(event){displayCalendar2($('#${id}2'), $('#${id}1'), '', this);return false;});}catch(e){}
		</c:when>
		<c:when test="${formatType == 16 || formatType == 17 || formatType == 18}">
		<%/* 날짜 시:분:초(기간) / 날짜 시:분(기간) / 날짜 시(기간) */%>
		try{$("#fn_btn_${id}11").click(function(event){displayCalendar2($('#${id}11'), $('#${id}21'), '', this);return false;});}catch(e){}
		try{$("#fn_btn_${id}21").click(function(event){displayCalendar2($('#${id}21'), $('#${id}11'), '', this);return false;});}catch(e){}
		</c:when>
		<c:when test="${formatType == 10}">
		<%/* 아이디 */%>
		try{
			$("#fn_btn_${id}").click(function(){
				fn_checkObj.check(this, "${id}", {form:$("#fn_${id}ConfirmForm"), obj:$("#fn_${id}ConfirmForm input[name='${name}']")}, "${formatType}");
				return false;
			});
		}catch(e){}
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${objectType == 8 || objectType == 18 || objectType == 98}">
				<%/* text + button / text + button(외부)*/%>
				try{
					// ${itemName} 검색
					$("#fn_btn_${id}").click(function(event){
						<c:choose>
							<c:when test="${!empty itemObj['onclick']}">
								${itemObj['onclick']};
								return false;
							</c:when>
							<c:otherwise>
								fn_dialog.init("fn_${id}_search");
								var varItemId = "${id}";
								try {
									var varTitle = "${itemName} 검색";
									fn_dialog.open(varTitle, "${itemObj['search_url']}<c:choose><c:when test="${fn:indexOf(itemObj['search_url'], '?') == -1}">?</c:when><c:otherwise>&</c:otherwise></c:choose>mId=<c:out value="${queryString.mId}"/>&itemId=" + varItemId +"&dl=1");
								}catch(e){}
								return false;
							</c:otherwise>
						</c:choose>
					});
	
					// ${itemName} 삭제
					$("#fn_btn_del_${id}").click(function(){
						$("#${id}Name").val("");
						$("#${id}").val("");
					});
				}catch(e){}
				</c:when>
				<c:when test="${objectType == 11}">
				<%/* multi select + button */%>
				try{
					$("#fn_btn_add_${id}").click(function(event){
					<c:choose>
						<c:when test="${!empty itemObj['onclick']}">
							${itemObj['onclick']};
							return false;
						</c:when>
						<c:otherwise>
							fn_dialog.init("fn_${id}_search");
							var varItemId = "${id}";
							try {
								var varTitle = "${itemName} 검색";
								fn_dialog.open(varTitle, "${itemObj['search_url']}<c:choose><c:when test="${fn:indexOf(itemObj['search_url'], '?') == -1}">?</c:when><c:otherwise>&</c:otherwise></c:choose>mId=<c:out value="${queryString.mId}"/>&itemId=" + varItemId +"&dl=1");
							}catch(e){}
							return false;
							</c:otherwise>
						</c:choose>
					});
	
					// ${itemName} 삭제
					$("#fn_btn_del_${id}").click(function(){
						try {
							var varItemId = "${id}";
							var varObj = $("#" + varItemId);
							if(!fn_checkSelected(varObj, "삭제")) return false;
							
							varObj.find("option:selected").remove();
						}catch(e){}
					});
				}catch(e){}
				</c:when>
				<c:when test="${objectType == 2 || objectType == 4 || objectType == 14 || objectType == 5}">
				<%/* checkbox / radio */%>
				
				<c:if test="${!empty itemObj['option_etc']}">
					<c:choose>
					<c:when test="${objectType == 2}">
						<%/* select */%>
						$(document).on('change', "select[name='${name}']", function(){
							var varObj = $(this).find("option:selected");
							
							<c:if test="${!empty itemObj['name_column_id']}">
							var varText = (varObj.val() == '')?'':varObj.text();
							$("input[name='${name}Name']").val(varText);
							</c:if>
							var varName = $(this).attr("name");
							var varEtcObj = $(this).siblings("input[data-id='" + varObj.attr("data-etc") + "']") || $(this).siblings("." + varName + "Etc").find("input[data-id='" + varObj.attr("data-etc") + "']");
							if(varEtcObj.size() > 0) {
								varEtcObj.prop("disabled", false);
								varEtcObj.removeClass("disabled");
							} else {
								varEtcObj = $(this).siblings("input[name='" + varName + "Etc']") || $(this).siblings("." + varName + "Etc").siblings("input[name='" + varName + "Etc']");
								varEtcObj.prop("disabled", true);
								varEtcObj.addClass("disabled");
							}
						});
						$("select[name='${name}']").trigger('change');
					</c:when>
					<c:when test="${objectType == 5}">
						<%/* radio */%>
						$("input[name='${name}']").click(function(){
							var varName = $(this).attr("name");
							var varId = $(this).attr("id");
							
							<c:if test="${!empty itemObj['name_column_id']}">
							var varObj = $(this); 
							var varText = (varObj.val() == '')?'':$("label[for='" + varId + "']").text();
							$("input[name='${name}Name']").val(varText);
							</c:if>
							
							var varEtcObj = $("#" + varId + "Etc");
							
							if(varEtcObj.size() > 0) {
								varEtcObj.prop("disabled", false);
								varEtcObj.removeClass("disabled");
							} else {
								varEtcObj = $("input[name='" + varName + "Etc']");
								varEtcObj.prop("disabled", true);
								varEtcObj.addClass("disabled");
							}
						});
					</c:when>
					<c:when test="${objectType == 4}">
						<%/* checkbox */%>
						$("input[name='${name}']").click(function(){
							var varName = $(this).attr("name");
							var varId = $(this).attr("id");
							var varEtcObj = $("#" + varId + "Etc");
							
							if(varEtcObj.size() > 0) {
							
								varEtcObj.prop("disabled", !varEtcObj.prop("disabled"));
								varEtcObj.toggleClass("disabled");
							}/* else {
								varEtcObj = $("input[name='" + varName + "Etc']");
								varEtcObj.prop("disabled", true);
								varEtcObj.addClass("disabled");
							}*/
						});
					</c:when>
					</c:choose>
				</c:if>
				</c:when>
				<c:when test="${objectType == 6}">
				<%/* file */%>
				<c:if test="${empty limitExt}">
					<spring:message var="phUploadFile" code="Globals.ph.upload.file"/>
					<c:set var="phUploadFiles">
						${fn:replace(phUploadFile, ',', '","')}
					</c:set>
					<c:set var="limitExt">
					gVarLimitExt={"total":new Array("0", "${phUploadFiles}")
					</c:set>
				</c:if>
				<c:if test="${!empty itemObj['dset_file_ext']}">
					<c:set var="phUploadFiles">
						${fn:replace(itemObj['dset_file_ext'], ',', '","')}
					</c:set>
					<c:set var="limitExt">
					${limitExt},"${id}":new Array("${itemObj['use_file_ext']}", "${phUploadFiles}")
					</c:set>
				</c:if>
				
				<%/*파일찾기*/%>try{$(document).on("change", "input[name='${name}']", function(event){fn_checkFile(this);return false;});}catch(e){}
				try{$(document).on('click', '#fn_btn_del_${id}', function(event){varSgDeleteFile.setState('${id}', this);return false;});}catch(e){}
						
				</c:when>
				<c:when test="${objectType == 9}">
				<%/* multi file */%>
				<c:if test="${empty maximum || maximum == 0}"><spring:message var="maximum" code="Globals.upload.file.maximum"/></c:if>
				<c:if test="${empty limitExt}">
					<spring:message var="phUploadFile" code="Globals.ph.upload.file"/>
					<c:set var="phUploadFiles">
						${fn:replace(phUploadFile, ',', '","')}
					</c:set>
					<c:set var="limitExt">
					gVarLimitExt={"total":new Array("0", "${phUploadFiles}")
					</c:set>
				</c:if>
				<c:if test="${!empty itemObj['dset_file_ext']}">
					<c:set var="phUploadFiles">
						${fn:replace(itemObj['dset_file_ext'], ',', '","')}
					</c:set>
					<c:set var="limitExt">
					${limitExt}, "${id}":new Array("${itemObj['use_file_ext']}", "${phUploadFiles}")
					</c:set>
				</c:if>
				
				<c:choose>
					<c:when test="${empty limitCount}">
						<c:set var="limitCount">
						gVarFileLimitCount={
						</c:set>
					</c:when>
					<c:otherwise>
						<c:set var="limitCount">
							${limitCount},
						</c:set>
					</c:otherwise>
				</c:choose>
				<c:set var="limitCount">
					${limitCount}"${id}":${maximum}
				</c:set>
				
				<%/*파일찾기*/%>try{$(document).on("change", "input[name='${name}']", function(event){if(!fn_checkMultiFile(this<c:if test="${itemObj['use_single_upload'] == 1}">, 1</c:if>)){event.stopImmediatePropagation();return false;}<c:if test="${itemObj['use_single_upload'] == 1}"><%-- 모듈 javascript에 fn_fileUploadSubmit 생성해야함!! --%>fn_fileUploadSubmit(this);</c:if>});}catch(e){}
				<%-- 
					itemObj['use_single_upload'] == 1인 경우 fn_fileUploadSubmit javascript 예시 
					function fn_fileUploadSubmit(theObj) {
						var varFormObj = $("#submitFormID");
						var varFileItemName = $(theObj).attr("name");
						varFormObj.find("*").prop("disabled", true);
						$(theObj).prop("disabled", false);
						varFormObj.ajaxForm({
					        url : "${elfn:replaceScriptLink(URL_FILEUPLOAD)}&itemId=" + varFileItemName,
					        enctype : "multipart/form-data",
					        dataType : "json",
					        error : function(){
					            console.log("에러") ;
					            fn_removeMaskLayer();
					        },
					        success : function(result){
					        	var varData = result.data;
					        	if(varData) {
									varFormObj.find("*").prop("disabled", false);
						    		// 저장될 파일목록에 추가
									fn_addSingleUploadFileList(varFileItemName, 0, varData[varFileItemName + '_size'], varData[varFileItemName + '_origin_name'], varData[varFileItemName + '_saved_name'], '', true);		// 저장될 파일목록에 추가
									fn_addUploadFile(varFileItemName, true);																																				// uploadFile object 추가
						            $(theObj).remove();
					        	}
					            fn_removeMaskLayer();
					        }
					    });
						varFormObj.submit();
					}
				--%>
				<%/*파일삭제*/%>try{$(document).on('click', '#fn_btn_del_${id}', function(event){fn_deleteFile('${id}');return false;});}catch(e){}
				<%/*대체텍스트추가*/%>try{$(document).on('click', '#fn_btn_text_${id}', function(event){fn_addFileText('${id}');return false;});}catch(e){}
				<%/*파일순서(위)*/%>try{$(document).on('click', '#fn_btn_up_${id}', function(event){fn_setFileOrder('${id}', '1');return false;});}catch(e){}
				<%/*파일순서(아래)*/%>try{$(document).on('click', '#fn_btn_down_${id}', function(event){fn_setFileOrder('${id}', '2');return false;});}catch(e){}
				
				</c:when>
				<c:when test="${objectType == 22}">
				<%/* 3차 select */%>
				try{
					fn_setInitItemOrderOption("${id}");
				}catch(e){}
				</c:when>
				<c:when test="${objectType == 28}">
				<%/* datalist */%>
				try{
					$(document).on("change", "input[name='${name}']", function(){
						var varList = $(this).prop('list');
						var varVal = $(this).val();
						var varSelObj = $(varList).find('option[value="' + varVal + '"]');
						var varCodeObj = $(this).siblings("input[name='${name}Idx']");
						varCodeObj.val(varSelObj.attr("data-code"));
					});
				}catch(e){}
				</c:when>
				<c:when test="${objectType == 1}">
				<%/* textarea */%>
				<c:if test="${itemObj['editor'] == '1' && (itemObj['editor_utype'] == '1' || isAdmMode && (empty itemObj['editor_utype'] || itemObj['editor_utype'] == '0'))}">
				<%/* 네이버 스마트에디터 사용하는 경우 */%>
				try {
					nhn.husky.EZCreator.createInIFrame({
						oAppRef: oEditors,
						elPlaceHolder: "${id}",
						maxLen: "${maximum}",
						sSkinURI: CheckForm.contextPath + "/include/js/editor/SmartEditor2Skin.html?mId=<c:out value="${param.mId}"/>&sp=" + encodeURIComponent("${crtSiteId}<c:if test="${menuType == 2}">/menuContents/${usrSiteVO.siteId}</c:if>") + "&itemId=${id}",
						htParams : {bUseToolbar : true,	fOnBeforeUnload : function(){}},
						fCreator: "createSEditor2"
					});
				} catch(e) {}
				</c:if>
				</c:when>
				<c:when test="${objectType == 0 || objectType == 1 || objectType == 7 || objectType == 101}">
				<%/* 그외 입력  */%>
					<c:choose>
					<c:when test="${itemtype == '1'}">
						<%/* 숫자  */%>
						try {
							$("#${id}").keydown(function(event){
								return fn_onlyNumDotKD(event, $(this));
							});
						} catch(e) {}
					</c:when>
					<c:when test="${itemtype == '4'}">
						<%/* 숫자문자  */%>
						try {
							$("#${id}").keydown(function(event){
								return fn_onlyNumKD(event);
							});
						} catch(e) {}
					</c:when>
					<c:when test="${itemtype == '11'}">
						<%/* 통화(원)  */%>
						try {
							$("#${id}").keydown(function(event){
								return fn_onlyNumKD(event);
							});
						} catch(e) {}
						try {
							$("#${id}").focus(function(){
								fn_setNFormatCurrent($(this));
							});
						} catch(e) {}
						try {
							$("#${id}").blur(function(){
								fn_setFormatCurrent($(this));
							});
						} catch(e) {}
					</c:when>
					<c:otherwise>
					<%/* 그외 입력  */%>
					</c:otherwise>
					</c:choose>
					<c:if test="${itemtype == '1' || itemtype == '2' || itemtype == '3' || itemtype == '4' || itemtype == '5' || itemtype == '11'}">
						<%/* 한글입력막기  */%>
						try {
							document.getElementById('${id}').addEventListener('paste', function(event){
								var clipboardData, pastedData;

							    // Stop data actually being pasted into div
							    event.stopPropagation();
							    event.preventDefault();
							
							    // Get pasted data via clipboard API
							    clipboardData = event.clipboardData || window.clipboardData;
							    pastedData = clipboardData.getData('Text');
								fn_preventHan(event, $("#${id}"), pastedData);
							});
							$("#${id}").keyup(function(event){
								return fn_preventHan(event, $(this));
							});
							$("#${id}").blur(function(event){
								return fn_preventHan(event, $(this));
							});
						} catch(e) {}
					</c:if>
				</c:when>
			</c:choose>
		</c:otherwise>
	</c:choose>
	</c:if>
</c:forEach>
<c:if test="${!empty limitCount}">
	${limitCount}};
</c:if>
<c:if test="${!empty limitExt}">
	${limitExt}};
</c:if>
</c:if>