		<c:set var="searchLangUrl" value="${URL_WORKINPUT}"/>
		<c:if test="${empty menuType || menuType == 0}">
		<%@ include file="../top.jsp"%>
		</c:if>
		<div class="topContInfo">
			<dl>
				<c:set var="contSettingInfo" value="${moduleSetting.setting_info}"/>
				<c:set var="contItemInfo" value="${moduleItem.item_info}"/>
				<c:set var="branchItemInfo" value="${moduleItem.branch_item_info}"/>
				<c:set var="itemId" value="contName"/>
				<dt><itui:objectItemName itemId="${itemId}" itemInfo="${contItemInfo}"/></dt>
				<dd>
					<itui:objectView itemId="${itemId}" itemInfo="${contItemInfo}" objDt="${verDt}"/>
					<c:set var="itemId" value="contType"/>
					<c:set var="useContentsType" value="${elfn:useSettingtem(contSettingInfo, contItemInfo.items[itemId])}"/>
					<c:if test="${useContentsType}">
					(<itui:objectView itemId="${itemId}" itemInfo="${contItemInfo}" objDt="${verDt}" optnHashMap="${contOptnHashMap}"/>)
					</c:if>
				</dd>
				<c:set var="itemId" value="branchName"/>
				<dt><itui:objectItemName itemId="${itemId}" itemInfo="${branchItemInfo}"/></dt>
				<dd>[<itui:objectView itemId="branchType" itemInfo="${branchItemInfo}" objDt="${verDt}" optnHashMap="${branchOptnHashMap}"/>]<itui:objectView itemId="${itemId}" itemInfo="${branchItemInfo}" objDt="${verDt}"/></dd>
				<c:set var="itemId" value="verIdx"/>
				<dt><itui:objectItemName itemId="${itemId}" itemInfo="${itemInfo}"/></dt>
				<dd>
					<span style="margin-right:10px;"><itui:objectView itemId="${itemId}" itemInfo="${itemInfo}" objDt="${verDt}"/></span>
					<c:choose>
					<c:when test="${verDt.WORK_TYPE != '0' && verDt.WORK_TYPE != '10'}">
					<button type="button" class="btnTypeH btn_work_apply" data-target="#infoMng"><itui:objectView itemId="workStep" itemInfo="${itemInfo}" objDt="${verDt}"/></button>
					</c:when>
					<c:otherwise>
					<c:set var="itemId" value="workType"/>
					[<itui:objectView itemId="${itemId}" itemInfo="${itemInfo}" objDt="${verDt}" optnHashMap="${optnHashMap}"/>]
					</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<c:if test="${verDt.WORK_TYPE != '0' && verDt.WORK_TYPE != '10'}">
			<div id="infoMng" class="fn_skip fn_dialog">
				<form id="fn_workTypeInputForm" name="fn_workTypeInputForm" method="post" action="<c:out value="${URL_WORKDELETEPROC}"/>&verIdx=<c:out value="${queryString.verIdx}"/>" target="submit_target">
					<input type="hidden" name="workType" id="workType" value="10"/>
				<% /* 적용로그 저장 - 2차 작업 %>
				<div class="header">
					<h4><spring:message code="button.managerinfo"/></h4>
					<button type="button" data-target="#infoMng" title="<spring:message var="btn_close" code="button.close"/>" class="btn_close"><c:out value="${btn_close}"/></button>
				</div>
				<fieldset>
					<legend>메뉴 관리 담당자 정보 입력 양식</legend>
					<div class="fn_dialog_content">
					<table class="tbWriteA" summary="글쓰기 서식">
						<caption>
						글쓰기 서식
						</caption>
						<colgroup>
						<col style="width:120px;" />
						<col />
						</colgroup>
						<tbody>
							<tr>
								<th>관리담당</th>
								<td><input type="checkbox" id="top_use_dmng1" name="use_dmng" value="1"/><label for="top_use_dmng1">사용</label></td>
							</tr>
						</tbody>
					</table>
					<div class="btnCenter">
						<spring:message var="btn_save" code="button.save"/><input type="submit" class="btnTypeA fn_btn_submit" title="<c:out value="${btn_save}"/>" value="<c:out value="${btn_save}"/>" />
						<spring:message var="btn_cancel" code="button.cancel"/><input type="button" class="btnTypeB fn_btn_cancel" title="<c:out value="${btn_cancel}"/>" value="<c:out value="${btn_cancel}"/>"/>
					</div>
					</div>
				</fieldset>
				<%*/ %>
			</form>
			</div>
			</c:if>
		</div>
		<script type="text/javascript">
		
		// 적용 정보  입력폼 열기
		$(".btn_work_apply").click(function() { 
			var varConfirm = confirm(fn_Message.confirmText($(this).text()));
			if(!varConfirm) return false;
			$("#fn_workTypeInputForm").submit();
		});
		</script>
		<%/* 적용로그 저장 - 2차 작업%>
		<script type="text/javascript">
		
		// 적용 정보  입력폼 열기
		$(".btn_work_apply").click(function() { 
			var varDivObj = $(this).attr('data-target');
			
			var varTop = 0;
			var varLeft = 0;
			var varWidth = $("#content").width() + 20;
			var varHeight = $("#content").height() + 20;
			
			fn_dialog_layer.open($("#content"), "fn_dialog_layer", "z-index:10001;top:" + varTop + ";left:" + varLeft + ";width:" + varWidth + "px;height:" + varHeight + "px;");
			$(varDivObj).removeClass('fn_skip');
			//$("#wrapper").append($(varDivObj));
			return false;
		});

		// 적용 정보  입력폼 닫기
		$("#infoMng .btn_close").click(function() { 
			var varDivObj = $(this).attr('data-target');
			$(varDivObj).addClass('fn_skip');
			fn_dialog_layer.close("fn_dialog_layer");
			
			$("#fn_dmngForm input[type='reset']").click();
			return false;
		});
		</script>
		<%*/ %>