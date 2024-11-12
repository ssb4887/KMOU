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
<div class="cms_board_article">
<table id="tpSubContent" class="tbListA" >
	<colgroup>
		<col style="width:2.5%">	
		<col style="width:2.5%">	
		<col style="width:15%">		
		<col style="width:30%">
	</colgroup>
	<thead>
        <tr>
            <th scope="row"></th>
            <th scope="row">No</th>          
            <th scope="row"><strong>*</strong> 인재상</th>
            <th scope="row"><strong>*</strong> 전공능력</th>
        </tr>		
	</thead>
	<tbody class="alignC">
		<c:choose>
			<c:when test="${empty list}">
				<tr>
					<td><input type="checkbox" class="checkbox-type01" name="chkbox"></td>
					<td><input type="hidden" value="1" name="ord0"><span id="ord">1</span></td>
					<td>
						<input name="talent0" id="talent1" class="inputTxt" title="인재상" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;">
					</td>
					<td>
						<select class="select" name="majorAbty0" id="majorAbty1" title="전공능력" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;">							
							<option value="">전체</option>
						</select>
					</td>
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
               	
               		<input type="hidden" value="${list[i.index].REGI_DATE }" name="regiDate${i.index }">
               		<input type="hidden" value="${list[i.index].REGI_ID }" name="regiId${i.index }">
               		<input type="hidden" value="${list[i.index].REGI_IP }" name="regiIp${i.index }">
               		<input type="hidden" value="${list[i.index].ABTY_CD }" id="abtyCd${i.index }" name="abtyCd${i.index }">
                	<tr>
                		<td><input type="checkbox" class="checkbox-type01" name="chkbox"></td>
                		<td><input type="hidden" value="${i.count}" name="ord${i.index}" id="ord${i.count}" class="w100"><span id="ord">${i.count}</span></td>
                		<td>
							<input class="inputTxt" name="talent${i.index}" id="talent${(i.index)+1}" title="인재상" value="${listDt.TALENT }" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;">	
						</td>
                		<td>
							<select class="select" name="majorAbty${i.index}" id="majorAbty${(i.index)+1}" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;">							
								<option value="">전체</option>
							</select>
						</td>
                	</tr>
               	</c:forEach>
			</c:otherwise>			
		</c:choose>
		
		
		
	</tbody>
</table>
<div class="btnRight">
	<button type="button" id="addSub" class="btnTypeE">행 추가</button>
	<button type="button" id="deleteSub" class="btnTypeF" >행 삭제</button>
		<c:choose>
		<c:when test="${!empty list}">
			<input type="hidden" name="abilityRowLength" id="abilityRowLength" value="${fn:length(list) }">
		</c:when>
		<c:otherwise>
			<input type="hidden" name="abilityRowLength" id="abilityRowLength" value="1">
		</c:otherwise>
	</c:choose>
</div>	
</div>


<script type="text/javascript">
	fn_deleteTp();
	
	
	// 과정수정일 때 기존 교과목 프로필의 시간 합계를 훈련시간에 setting
	var useFlag = "${useFlag}";
	if(useFlag == "reuse"){
		fn_sumTrTm();
	}
	
	//훈련내용 입력 행 추가
	$("#addSub").click(function(){
		var index = $("#tpSubContent > tbody > tr").length;
		var length = Number($("#abilityRowLength").val());
		
		var inputList = ["talent", "majorAbty"];
		var inputName = ["인재상", "전공능력"];
		
		for(var i = 0; i < inputList.length; i++){
			if(!$("#" + inputList[i] + index).val()){
				alert("'인재상', '전공능력' 항목들은 필수 입력값입니다.");
				$("#" + inputList[i] + index).focus();
				return false;
			}
		}
		
		length += 1;
		$("#abilityRowLength").val(length);
		var innerHtml = "";
		//innerHtml += '<tr><td><button type="button" id="delete' + (index -1) + '" data-idx="' + (index -1) + '" class="btn-m02 btn-color02">삭제</button></td>'
		innerHtml += '<tr><td><input type="checkbox" class="checkbox-type01" name="chkbox"></td>'
		innerHtml += '<td><input type="hidden" value="'+ (length) +'" name="ord' + (length -1) + '"><span id="ord">' + (index + 1) + '</span></td>'
		innerHtml += '<td><input name="talent'+ (length -1) +' " id="talent' + (length) + '" class="inputTxt" title="인재상" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;"></td>'
		innerHtml += '<td><select class="select" name="majorAbty'+ (length -1) +'" id="majorAbty' + (length) + '" style="width: 100%;height: 100%;border: 1px solid #dbdbdb;"><option value="">전체</option></td>'
		
		
		$("#tpSubContent > tbody:last").append(innerHtml);
		
		for(var i=0; i < talentAbtyList.length; i++){
			$('#majorAbty'+(length)).append("<option value='" + talentAbtyList[i].MAJOR_ABTY + "'>" + talentAbtyList[i].MAJOR_ABTY + "</option>");			
		}
		
		fn_validationTp();
		fn_sumTrTmByKeyup();
	});
	
	
	// 훈련내용 입력 행 삭제
	function fn_deleteTp(){

		$("#deleteSub").click(function(){
			var index = $("#tpSubContent > tbody > tr").length;
			if(($("#tpSubContent > tbody > tr").length - $("#tpSubContent tr input[name=chkbox]:checked").length) < 1){
				alert("최소 하나의 교과목 행이 있어야 합니다.")
				return false;
			}
			if($("#tpSubContent tr input[name=chkbox]:checked").length == 0){
				alert("삭제할 행을 선택해 주세요.");
				return false;
			}
			$("#tpSubContent tr input[name=chkbox]:checked").each(function(){		
				$(this).closest('tr').remove();			
			});
			$("#tpSubContent > tbody > tr").each(function(index, item) {
				$(this).find("td:eq(1)").find('span').text(index +1);
				$(this).find("td:eq(1)").find('input').val(index +1);
			});
			
			fn_sumTrTm();
		});
	}
	
	
	
	
</script>