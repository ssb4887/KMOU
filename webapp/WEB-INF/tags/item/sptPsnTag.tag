<%@ tag language="java" pageEncoding="UTF-8" body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="elfn" uri="/WEB-INF/tlds/el-fn.tld"%>
<%@ attribute name="itemId"%>
<%@ attribute name="objDt" type="com.woowonsoft.egovframework.form.DataMap"%>
<%@ attribute name="defVal"%>
<%@ attribute name="objVal" type="java.lang.Object"%>
<%@ attribute name="itemInfo" type="net.sf.json.JSONObject"%>
<%@ attribute name="content"%>
<%@ attribute name="pageContext" type="javax.servlet.jsp.PageContext"%>
<c:if test="${empty objDt}"><c:set var="objDt" value="${dt}"/></c:if>
<%
	PageContext page = (PageContext)this.getJspContext();
	page.setAttribute("newline","\r\n");
%>
				
<div class="btnbx_right">
	<input type=button value="조회" id="refresh" class="btnTypeI" style="float:right;">
	<input type="submit" class="btnTypeJ fn_btn_submit" value="저장" title="저장" style="float:right; margin-right:5px;"/>
</div>
<div class="tbListA">
<table id="tpSubContent" class="tbListA" >
	<colgroup>
		<col style="width:5%">	
		<col style="width:20%">		
		<col style="width:40%">	
		<col style="width:10%">	
		<col style="width:20%">	
	</colgroup>
	<thead>
        <tr>
            <th scope="row">번호</th>          
            <th scope="row">진출분야코드</th>
            <th scope="row"><strong>*</strong> 진출분야</th>
            <th scope="row"><strong>*</strong> 순서</th>
            <th scope="row">등록일</th>
        </tr>		
	</thead>
	<tbody class="alignC">
		<c:choose>
			<c:when test="${empty list}">
				<tr>
					<td><input type="hidden" value="1" name="ord0"><span id="ord">1</span></td>
					<td><input type="hidden" value="${majorCd}-001" name="optionCode0" id="optionCode1" readonly/>${majorCd}-001</td>
					<td><textarea placeholder="전문인" name="optionName0" id="optionName1" style="width:300px; font-size:15px;">${listDt.OPTION_NAME}</textarea></td>
               		<td>
						<select name="ordIdx0" id="ordIdx1" style="width:60px;">
							<option value="1" selected="selected">1</option>
						</select>
					</td>
					<td></td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td>영문</td> -->
<!-- 					<td><textarea placeholder="영문" name="sptPsnEng0" id="sptPsnEng1" style="margin-top:30px; margin-bottom:30px;width: 100%;height: 100%;border: 1px solid #dbdbdb;"></textarea></td> -->
<!-- 					<td><textarea placeholder="영문" name="mjAbtyEng0" id="mjAbtyEng1" style="margin-top:30px; margin-bottom:30px;width: 100%;height: 100%;border: 1px solid #dbdbdb;"></textarea></td> -->
<!-- 					<td><textarea placeholder="영문" name="mjAbtyDefnEng0" id="mjAbtyDefnEng1" style="margin-top:30px; margin-bottom:30px;width: 100%;height: 100%;border: 1px solid #dbdbdb;"></textarea></td> -->
<!-- 				</tr>					 -->
			</c:when>
			<c:otherwise>
               	<c:forEach var="listDt" items="${list}" varStatus="i">
               		<input type="hidden" value="${listDt.REGI_DATE }" name="regiDate${i.index }">
               		<input type="hidden" value="${listDt.REGI_ID }" name="regiId${i.index }">
               		<input type="hidden" value="${listDt.REGI_IP }" name="regiIp${i.index }">
                	<tr>
<!--                 		<td><input type="checkbox" class="checkbox-type01" name="chkbox"></td> -->
                		<td><input type="hidden" value="${i.count}" name="ord${i.index}" id="ord${i.count}" class="w100"><span id="ord">${i.count}</span></td>
                		<td><input type="hidden" value="${listDt.OPTION_CODE}" name="optionCode${i.index}" id="optionCode${i.count}"><span>${listDt.OPTION_CODE}</span></td>
                		<td><textarea placeholder="진출분야" name="optionName${i.index}" id="optionName${i.count}" style="width:300px; font-size:15px;">${listDt.OPTION_NAME}</textarea></td>
						<td>
							<select name="ordIdx${i.index}" id="ordIdx${i.count}" style="width:60px;">
								<c:forEach var="ordDt" items="${ordList}" varStatus="i">
									<option value="<c:out value="${i.count}"/>"<c:if test="${ordDt.OPTION_CODE == listDt.OPTION_CODE}"> selected="selected"</c:if>>${i.count}</option>
								</c:forEach>
							</select>
						</td>
	               		<td class="date"><fmt:formatDate pattern="yyyy-MM-dd" value="${listDt.REGI_DATE}"/></td>	               		
                	</tr>
               	</c:forEach>
			</c:otherwise>			
		</c:choose>
	</tbody>
</table>
</div>
<div class="btnRight">
	<span style="font-size:16px; float:left;">*순서 항목은 모듈형 교육과정에 표시될 전문인의 순서입니다. 중복되지 않게 선택해주세요.</span>
	<button type="button" id="addSptSub" class="btnTypeE">행 추가</button>
	<button type="button" id="deleteSptSub" class="btnTypeF" >행 삭제</button>
		<c:choose>
		<c:when test="${!empty list}">
			<input type="hidden" name="sptPsnRowLength" id="sptPsnRowLength" value="${fn:length(list) }">
		</c:when>
		<c:otherwise>
			<input type="hidden" name="sptPsnRowLength" id="sptPsnRowLength" value="1">
		</c:otherwise>
	</c:choose>
</div>	



<script type="text/javascript">
	var originUrl = window.location.href;
	history.replaceState({path : window.location.pathname},'', window.location.pathname);
	
	$("#refresh").click(function(){
		var confirmVal = confirm("조회 시 입력하시던 정보는 사라집니다. 조회하시겠습니까?");
		
		if(confirmVal){
			window.location.href = originUrl;
		}
	});	
	
	//행 추가
	$("#addSptSub").click(function(){
		var index = $("#tpSubContent > tbody > tr").length;
		var length = Number($("#sptPsnRowLength").val());
		
		var inputList = ["optionName"];
		var inputName = ["진출분야"];
		
		for(var i = 0; i < inputList.length; i++){
			if(!$("#" + inputList[i] + index).val()){
				alert("진출분야 항목은 필수 입력값입니다.");
				$("#" + inputList[i] + index).focus();
				return false;
			}
		}
		
		length += 1;
		$("#sptPsnRowLength").val(length);
		console.log($("#sptPsnRowLength").val(length));
		
		var nextSptCodeNum = ("00" + (index+1)).slice(-3);
		
		
		var innerHtml = "";
		//innerHtml += '<tr><td><button type="button" id="delete' + (index -1) + '" data-idx="' + (index -1) + '" class="btn-m02 btn-color02">삭제</button></td>'
		innerHtml += '<tr>'
		innerHtml += '<td><input type="hidden" value="'+ (length) +'" name="ord' + (length -1) + '"><span id="ord">' + (index + 1) + '</span></td>'
		innerHtml += '<td><input type="hidden" name="optionCode'+ (length -1) +'" id="optionCode' + (length) + '" value="${majorCd}-' + nextSptCodeNum + '"><span >' + "${majorCd}-" + nextSptCodeNum + '</span> '+ '</td>'
		innerHtml += '<td><textarea placeholder="진출분야" name="optionName'+ (length -1) +'" id="optionName' + (length) + '" style="width:300px;"></textarea></td>'
		innerHtml += '<td><select name="ordIdx'+ (length -1) +'" id="ordIdx' + (length) + '" style="width:60px;"><option></option></select></td>'
		innerHtml += '<td></td>'	
		$("#tpSubContent > tbody:last").append(innerHtml);		
				
		for(var i=1; i<=length+1; i++){
			var selectedOrd = $("#ordIdx" + i + " option:selected").text();
			
			$("#ordIdx" + i).empty();
			for(var j=1; j<=index+1; j++){				
				$("#ordIdx" + i).append($('<option>', {
					value : j,
					text : j
				}));
			};
			$("#ordIdx" + i + " option").each(function(){
				if($(this).text() == selectedOrd){
					$(this).prop("selected", true);
				}
			});			
		}
		
		
	});
	
	//행 삭제
	$("#deleteSptSub").click(function(){
		var index = $("#tpSubContent tbody tr").length;
		var length = Number($("#sptPsnRowLength").val());
		
		if(index > 1){
			index -= 1;
			$("#tpSubContent > tbody:last > tr:last").remove();
			$("#sptPsnRowLength").val(index);
			
		} else {
			return false;
		}
		
		for(var i=1; i<=length+1; i++){
			var selectedOrd = $("#ordIdx" + i + " option:selected").text();
			
			$("#ordIdx" + i).empty();
			for(var j=1; j<=index; j++){				
				$("#ordIdx" + i).append($('<option>', {
					value : j,
					text : j
				}));
			};
			$("#ordIdx" + i + " option").each(function(){
				if($(this).text() == selectedOrd){
					$(this).prop("selected", true);
				}
			});			
		}
	});
	
	
	
</script>