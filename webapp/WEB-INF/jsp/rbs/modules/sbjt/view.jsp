<%@ include file="../../include/commonTop.jsp"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="text/javascript">

var yearCheck = "";
var smtCheck = "";

$(function(){
	<c:if test="${mngAuth || wrtAuth && dt.AUTH_MNG == '1'}">	
	// 삭제
	$(".fn_btn_delete").click(function(){
		try {
			var varConfirm = confirm("<spring:message code="message.select.delete.confirm"/>");
			if(!varConfirm) return false;
		}catch(e){return false;}
		return true;
	});
	</c:if>
	
	getCurInfo().then(result => {
		yearCheck = result.YEAR;
		smtCheck = result.SMT;
	}).catch(error => {
		
	});
	
	
	var page = "1";
	var year = "";
	var semi = "";
	
	/* getLecture(); */
	getLectureList(page, year, semi);
	getTotalStudent();
	
	
	/* 년도 학기 학년 셀렉트박스 */
	var setYear = "${sbjtInfo.YEAR}";
	$("#hisYear").append("<option value='all' selected>개설년도 전체</option>");
	for(var y = (setYear); y >= (setYear -4); y--){
		$("#hisYear").append("<option value='" + y + "'>" + y + " 년</option>");
	}
	
	
	$("#hisYear, #hisSemi").change(function(){
		try{
			year = $("#hisYear").val();
			semi = $("#hisSemi").val();
			if(semi == "1"){
				semi = "GH0210";
			} else if(semi == "2"){
				semi = "GH0220";
			} else if(semi == "all"){
				semi = "";
			}
			getLectureList(page, year, semi);
		}catch(e){
			
		}
	});
	
	//찜 onRed class 및 관심강좌에  추가
	getBookmarkList()
	.then(data => {
		// 교과목 찜
		var id = "${sbjtInfo.SUBJECT_CD}" + "_" + "${sbjtInfo.DEPT_CD}" + "_" + "${sbjtInfo.YEAR}" + "_" + "${sbjtInfo.GRADE}" + "_" + "${sbjtInfo.SMT}";				
		data.forEach(function(bookmark) {
    		if(id == bookmark.docId){
        		 $("#"+id).addClass("on_red");
    		}
 		});
	});
	
	
});


/* 강의개설이력 목록 불러오기 */
function getLectureList(page, year, semi){
	var dt = new Date();
	var setYear = dt.getFullYear();
	var setMonth = dt.getMonth()+1;
	
	var pYear = "";
	var type = "";
	if(year == "" || year == "all"){
		pYear = "${sbjtInfo.YEAR}";
		type = "N";
	}else{
		pYear = year;
		type = "Y"
	}
	
	var pSmt = "";
	var cSmt = "";
	if(semi == ""){
		pSmt = "${sbjtInfo.SMT}"
		cSmt = "N";	
	}else{
		pSmt = semi;
		cSmt = "Y";
	}
	
	const pSubjectCd = "${sbjtInfo.SUBJECT_CD}";
	const pDeptCd = "${sbjtInfo.DEPT_CD}";
	
	
	const varAction = "/web/sbjt/classView.json?mId=32&YEAR="+ pYear +"&SUBJECT_CD=" + pSubjectCd + "&DEPT_CD=" + pDeptCd + "&SMT="+ pSmt +"&page=" + page + "&type=" + type + "&cSmt=" + cSmt;
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			const varItemObj = $("#classView");
  			//const $courseOffer = $("#courseOfferView");
  			var htmlPage = $("#page");
  			
  			//$courseOffer.empty();
  			varItemObj.empty();
  			htmlPage.empty();
  			var list = result.list;
  			
  			
  			/* 최근개설이력 */
			if(list) {
  				$.each(list, function(i, item){
  					var empNo = item.EMP_NO;
  					var subjectNm = item.SUBJECT_NM;
  					var subjectCd = item.SUBJECT_CD;
  					var deptCd = item.DEPT_CD;
  					var empNm = (typeof(item.EMP_NM) != "undefined" && item.EMP_NM != "") ? item.EMP_NM : '-';
  					var year = item.YEAR;
  					var grade = (item.GRADE == '0') ? '전체' : item.GRADE;
  					var smt = item.SMT;
  					var divcls = item.DIVCLS;
  					var varCon = '<div class="item p-4">\n';
  					var classLec = "";
  					var studentCnt = (typeof(item.RSTR_MCNT) != "undefined" && item.RSTR_MCNT != "") ? item.RSTR_MCNT + ' 명' : '-';
  					// 관심강좌에 alrea 추가
  					
  					
  					varCon += '<ul class="cate d-flex flex-row gap-1 flex-wrap">\n';
  					if(item.COMDIV_CODE == 'UE010074'){
  						varCon += '<li class="styl_gray">교양과목</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010011'){ 
  	  					varCon += '<li class="styl_lighb">교양필수</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010012'){
  	  					varCon += '<li class="styl_gray">교양선택</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010080'){
  	  					varCon += '<li class="general_selection">일반선택</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010024'){
  	  					varCon += '<li class="nati_navy">전공기초</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010021'){
  	  					varCon += '<li class="alon_red">전공필수</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010022'){
  	  					varCon += '<li class="styl_skyb">전공선택</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010031'){
  	  					varCon += '<li class="styl_gray">교직</li>\n';
  	  				}
  					varCon += '</ul>\n';
  					varCon += '<h5 class="fw-semibold ellip_2 my-3">' + subjectNm + '(' + subjectCd + ')' + '</h5>\n';
  					varCon += '<div class="simply_info d-flex flex-row flex-wrap">\n';
  					varCon += '<dl class="d-flex flex-row gap-1 align-items-center">\n';
  					varCon += '<dt><img src="../images/ico_b_loc.png" alt="장소아이콘"></dt>\n';
  					varCon += '<dd class="text-secondary">' + item.COLG_NM + '</dd>\n';
  					varCon += '<td class="name text-start text-lg-center align-middle">\n';
  					varCon += '</dl>\n';
  					varCon += '<dl class="d-flex flex-row gap-1 align-items-center">\n';
  					varCon += '<dt><img src="../images/ico_b_tim.png" alt="시간아이콘"></dt>\n';
  					varCon += '<dd class="text-secondary">'+ item.YEAR + '년 ' + grade + '학년 ' + item.SMT_NAME + ' '+ item.ROOM_TIME + '</dd>';
  					varCon += '</dl>\n';
  					varCon += '</div>\n';
  					varCon += '<div class="p-3 info_box mt-3">\n';
  					varCon += '<ol class="d-flex flex-wrap">\n';
  					varCon += '<li class="d-flex flex-row col-12 col-sm-8 col-md-12 col-lg-8"><b class="d-inline-block">수강대상학과</b>' + item.DEPT_NM + '</li>\n';
  					varCon += '<li class="d-flex flex-row col-12 col-sm-4 col-md-12 col-lg-4"><b class="d-inline-block">정원</b>' + studentCnt + '</li>\n';
  					varCon += '<li class="d-flex flex-row col-12 col-sm-8 col-md-12 col-lg-8"><b class="d-inline-block">담당교수</b>' + empNm + '</li>\n';
  					varCon += '<li class="d-flex flex-row col-12 col-sm-4 col-md-12 col-lg-4"><b class="d-inline-block">학년</b>' + grade + '학년</li>\n';
  					varCon += '</ol>\n';
  					varCon += '</div>\n';
  					varCon += '<ul class="link_wrap d-flex flex-wrap mt-4">\n';
  					varCon += '<li class="col-3">\n';
  					varCon += `<a href="javascript:void(0);" class="d-flex flex-column align-items-center gap-2" onclick="getLectureView('\${empNo}', \${year}, '\${smt}', '\${divcls}');" data-bs-toggle="modal" data-bs-target="#syllabusModal">\n `;
  					varCon += '<i class="rounded-circle d-flex justify-content-center align-items-center"><img src="../images/ico_w_loc.png" alt="장소아이콘"/></i>\n';
  					varCon += '<span class="fw-semibold text-center">강의정보</span>\n';
  					varCon += '</a>\n';
  					varCon += '</li>\n';
  					varCon += '<li class="col-3">\n';
  					varCon += `<a href="#" class="d-flex flex-column align-items-center gap-2" onclick="window.open('/web/sbjt/planView.do?mId=32&dl=1&SUBJECT_CD=\${subjectCd}&DEPT_CD=\${deptCd}&EMP_NO=\${empNo}&YEAR=\${year}&SMT=\${smt}&DIVCLS=\${divcls}','_blank', 'width=1000, height=500')">\n `;
  					/* varCon += `<a href="javascript:void(0);" class="d-flex flex-column align-items-center gap-2" onclick="getPlanView('\${empNo}', \${year}, '\${divcls}', '\${smt}');" data-bs-toggle="modal" data-bs-target="#plannerModal">\n `; */
  					varCon += '<i class="rounded-circle d-flex justify-content-center align-items-center"><img src="../images/ico_w_doc.png" alt="계획서아이콘"/></i>\n';
  					varCon += '<span class="fw-semibold text-center">수업계획서</span>\n';
  					varCon += '</a>\n';
  					varCon += '</li>\n';
  					varCon += `<li class="col-3 vari_link inter_link" id="lecture_\${subjectCd}_\${deptCd}_\${year}_\${grade}_\${smt}_\${divcls}_\${empNo}">\n`;
  					varCon += `<a href="javascript:void(0);" class="d-flex flex-column align-items-center gap-2" onclick="likeChange('\${subjectCd}_\${deptCd}_\${year}_\${grade}_\${smt}_\${divcls}_\${empNo}','lecture');">\n`;
  					varCon += '<i class="rounded-circle d-flex justify-content-center align-items-center"><img src="../images/ico_w_lov.png" alt="하트아이콘"/></i>\n';
  					varCon += '<span class="fw-semibold text-center">관심강좌</span>\n';
  					varCon += '</a>\n';
  					varCon += '</li>\n';
  					varCon += `<li class="col-3 vari_link shop_link" id="\${year}_\${smt}_\${subjectCd}_\${deptCd}_\${divcls}_\${empNo}">\n`;
  					varCon += `<a href="javascript:void(0);" class="d-flex flex-column align-items-center gap-2" onclick="cartAddRemove('\${year}', '\${smt}', '\${subjectCd}', '\${deptCd}', '\${divcls}', '\${subjectNm}', '\${empNo}','\${year}_\${smt}_\${subjectCd}_\${deptCd}_\${divcls}_\${empNo}');">\n`;
  					varCon += '<i class="rounded-circle d-flex justify-content-center align-items-center"><img src="../images/ico_w_sho.png" alt="카트아이콘"/></i>\n';
  					varCon += '<span class="fw-semibold text-center">장바구니</span>\n';
  					varCon += '</a>\n';
  					varCon += '</li>\n';
  					varCon += '</ul>\n';
  					
  					
  					varItemObj.append(varCon);
  					
  					
  					getOpenLecList()
  					.then(data => {
  						// 교과목 찜
  						var id = subjectCd + "_" + deptCd + "_" + year + "_" + grade + "_" + smt + "_" + divcls + "_" + empNo;
  						data.forEach(function(bookmark) {
  				    		if(id == bookmark.docId){
  				    			$("#lecture_" + id).addClass("alrea");
  				    		}
  				 		});
  					});
  					
  					getBasketCart(year,smt,subjectCd,deptCd,divcls,empNo)
  					.then(data => {
  						if (!data || data.length === 0) {
   					      //console.warn('No data found or empty data array.');
   					      //return;
    					}
  						data.forEach(function(basket){
  							var id = year + "_" + smt + "_" + subjectCd + "_" + deptCd + "_" + divcls + "_" + empNo;
  							$("#" + id).addClass("alrea");
  						});
  					})
  					.catch(error =>{
  						//console.error('Error fetching basket cart data:', error);
  					});
  				});
  			} 
			
			if(list.length == '0'){
  				var varCon = '<div class="no_data_class">';
  				varCon += '<img src="../images/nodata_character.png">';
  				varCon += '<p>개설이력이 확인되지 않습니다.</p>';
  				varCon += '</div>';
  				
  				varItemObj.before(varCon);
  				
  				varItemObj.remove();
  	  			htmlPage.remove();
  			}
  			
			
        
			
  			
  			/* 강의개설이력 페이징  */
  			var pageInfo = result.paginationInfo;
  			
  			var currentPageNo = pageInfo.currentPageNo;
  			var totalPageCount = pageInfo.totalPageCount;
  			var pageSize = pageInfo.pageSize;
  			var firstPageNo = pageInfo.firstPageNo;
  			var lastPageNo = pageInfo.lastPageNo;
  			var firstPageNoOnPageList = pageInfo.firstPageNoOnPageList;
  			var lastPageNoOnPageList = pageInfo.lastPageNoOnPageList;
  			var pageCon = "";
  			
  			
  			if(totalPageCount > pageSize){
  				if(firstPageNoOnPageList > pageSize){
  					var prevPageNo = firstPageNoOnPageList;
  				} else{
  					var prevPageNo = firstPageNo;
  				}
  				if(currentPageNo != firstPageNo){
  					pageCon += `<li class="page-item page-first"><a href="javascript:getLectureList(\${firstPageNo}, '\${year}', '\${semi}');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>`;
  	  				pageCon += `<li class="page-item page-prev"><a href="javascript:getLectureList(\${prevPageNo}, '\${year}', '\${semi}');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>`;	
  				}
  			}
  			
  			
	  		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
	  			if(pageNo == currentPageNo){
	  				pageCon += `<li class="page-item active"><a class="page-link" href="javascript:getLectureList(\${pageNo}, '\${year}', '\${semi}');" title="\${pageNo}페이지" value="\${pageNo}">\${pageNo}</a></li>`;
	  			} else{
	  				pageCon += `<li class="page-item"><a class="page-link" href="javascript:getLectureList(\${pageNo}, '\${year}', '\${semi}');" title="\${pageNo}페이지" value="\${pageNo}">\${pageNo}</a></li>`;
	  			}
	  		}
  	  		if(totalPageCount > pageSize){
  				if(lastPageNoOnPageList < totalPageCount){
  					var nextPageNo = firstPageNoOnPageList + pageSize;
  				} else{
  					var nextPageNo = lastPageNo;
  				}
  				if(currentPageNo != lastPageNo){
  					pageCon += `<li class="page-item page-next"><a href="javascript:getLectureList(\${nextPageNo}, '\${year}', '\${semi}');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>`;
  	  	  			pageCon += `<li class="page-item page-last"><a href="javascript:getLectureList(\${lastPageNo}, '\${year}', '\${semi}');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>`;	
  				}
  			}
	  		
  			
  			htmlPage.append(pageCon);
			
			
  			return false;
  		}, 
  		error:function(request,error){
  			alert("개설강좌 실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	});
}

/* 수강생 통계 차트 세팅을 위한 데이터 조회 */
function getTotalStudent() {
	var subjectCd = "${sbjtInfo.SUBJECT_CD}";
	var deptCd = "${sbjtInfo.DEPT_CD}";
	var year = "${sbjtInfo.YEAR}";
	
	var varAction = "/web/sbjt/statView.json?mId=32&SUBJECT_CD=" + subjectCd + "&DEPT_CD=" + deptCd + "&YEAR=" + year;
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			var varItemObj = $(".chart_box");
  			
  			varItemObj.empty();
  			varItemObj.append('<canvas id="chart_box" style="height:100vh; width:200vw;"></canvas>');
  			
  			var list = result.list;
  			if(list) {
  				setStudentChart(list);
  			} /* else{
  				var varCon = '<tr>\n';
 					varCon += '<td colspan="8" class="bllist"><spring:message code="message.no.list"/></td>';
 					varCon += '</tr>\n';
 					varItemObj.append(varCon);
  			} */
			
  			return false;
  		}, 
  		error:function(request,error){
  			alert("수강생 실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	});
}

/* 수강생 통계 차트 세팅 */
function setStudentChart(data) {
	
	const categories = data.map(item => item.COLG_NM);
    const counts = data.map(item => item.TOTAL_COUNT);
    
    
    const ctx = document.getElementById('chart_box').getContext('2d');

    // 기존 차트가 있으면 삭제
    if (window.myChart) {
        window.myChart.destroy();
    }

    // Chart.js 차트 생성
    window.myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: categories,
            datasets: [{
                label: '학생수',
                data: counts,
                backgroundColor: '#004FC5aa',
                borderColor: '#004FC5',
                borderWidth: 3,
                barThickness : 200
            }]
        },
        options: {
            responsive: true,
            scales: {
                x: {
                    title: {
                        display: true,
                        text: '단과대학별'
                    }
                },
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: '학생수'
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                }
            }
        }
    });
    
	
}


/* 개설강좌 상세정보 및 강의평가 */
function getLectureView(empNo, year, smt, divcls){
	
	var subjectCd = "${sbjtInfo.SUBJECT_CD}";
	var deptCd = "${sbjtInfo.DEPT_CD}";
	
	var varAction = "${elfn:replaceScriptLink(URL_LECTUREVIEW)}&SUBJECT_CD=" + subjectCd + "&DEPT_CD=" + deptCd + "&EMP_NO=" + empNo + "&YEAR=" + year + "&SMT=" + smt + "&DIVCLS=" + divcls;
	
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			
  			/* 개설강좌 상세정보 */
  			var dt = result.dt;
  			if(dt){
  				
  				var grade = (typeof(dt.GRADE) == "undefined") ? "-" : dt.GRADE;
				var year = dt.YEAR;
				var smt = dt.SMT;
				var subjectNm = dt.SUBJECT_NM;
				var subjectCd = dt.SUBJECT_CD;
				var deptCd = dt.DEPT_CD;
				var divcls = (typeof(dt.DIVCLS) == "undefined") ? "-" : dt.DIVCLS;
				var roomTime = (typeof(dt.ROOM_TIME) == "undefined" || dt.ROOM_TIME == "") ? "-" : dt.ROOM_TIME;
				var empNo = dt.EMP_NO;
				var comdivName = dt.COMDIV_CODE_NAME;
  				
  				
  				$("#syllabusModalLabel").text(dt.SUBJECT_NM + ' [' + dt.SUBJECT_CD + ']') ;
  				$("#sbjtName").text(dt.SUBJECT_NM);
  	  			$("#sbjtEName").text(dt.SUBJECT_ENM);
  	  			$("#empNm").text(dt.EMP_NM);
  	  			$("#grade").text(dt.GRADE);
  	  			$("#engYn").text(dt.ENG_YN);
  	  			$("#college").text(dt.COLG_NM);
	  			$("#deptNm").text(dt.DEPT_NM);
	  			$("#subjectCd").text(dt.SUBJECT_CD);
	  			$("#divcls").text(dt.DIVCLS);
	  			$("#sinbun").text(dt.COMDIV_CODE_NAME);
	  			$("#lectureWay").text(dt.PYENGGA_TP_NM);
	  			$("#classNm").text(dt.CLASS_NM);
	  			$("#cdtNum").text(dt.CDT_NUM);
	  			$("#timeRoom").text(roomTime);
	  			$("#fixNumber").text(dt.FIX_STU_CNT);
	  			
	  			if(comdivName.includes('교양')){
	  				$("#abiNm").text("핵심역량");
	  			} else {
	  				$("#abiNm").text("전공능력");
	  			}
  	  			
	  			
	  			getOpenLecList()
				.then(data => {
					// 교과목 찜
					var id = subjectCd + "_" + deptCd + "_" + year + "_" + grade + "_" + smt + "_" + divcls + "_" + empNo;
					
					$("#rLov").attr('style','display:block;');
					$("#fillLov").attr('style','display:none;');
					
					data.forEach(function(bookmark) {
			    		if(id == bookmark.docId){
			    			$("#fillLov").attr('style','display:block;');
							$("#rLov").attr('style','display:none;');
			    		}
			 		});
				});
	  			
	  			getBasketCart(year,smt,subjectCd,deptCd,divcls,empNo)
					.then(data => {
						if (!data || data.length === 0) {
							$("#bSho").attr('style','display:block;');
							$("#fillSho").attr('style','display:none;');
						}
						else {
							$("#fillSho").attr('style','display:block;');
							$("#bSho").attr('style','display:none;');
						}
					})
	  				
  	  			$("#detailBookmart").attr("onclick", `likeChange('\${subjectCd}_\${deptCd}_\${year}_\${grade}_\${smt}_\${divcls}_\${empNo}','lecture');`);
  	  			$("#detailBasket").attr("onclick", `cartAddRemove('\${year}', '\${smt}', '\${subjectCd}', '\${deptCd}', '\${divcls}', '\${subjectNm}', '\${empNo}', '\${year}_\${smt}_\${subjectCd}_\${deptCd}_\${divcls}_\${empNo}');`);
  	  		
  			}
  			/* 핵심능력 */
  			var coreList = result.coreList;
			if(coreList){
  				$.each(coreList,function(i, item){
  					var coreNm = (typeof(item.ABI_NM) == "undefined" && item.ABI_NM == "") ? "-" : item.ABI_NM;
  					$("#core"+i).text(coreNm);
  				})
				
  				/* 
				var year = abiDt.YEAR;
				var smt = abiDt.SMT;
				var deptCd = abiDt.DEPT_CD;
				var abiName = (typeof(abiDt.ABI_NAME) == "undefined" && abiDt.ABI_NAME == "") ? "핵심역량/전공능력" : abiDt.ABI_NAME;
				var abiNmList = (typeof(abiDt.ABI_NM_LIST) == "undefined") ? "등록된 데이터가 없습니다." : abiDt.ABI_NM_LIST; */
  				
  				/* $("#abiNm").text(abiName);
  	  			$("#abiNmList").text(abiNmList); */
  	  			
  			} else{
  				/* $("#coreNm").text("핵심역량");
  	  			$("#coreNmList").text("등록된 데이터가 없습니다."); */
  			}
  			
  			/* 전공능력  */
  			var abiList = result.abiList;
			if(abiList){
  				$.each(abiList,function(i, item){
  					var abiNm = (typeof(item.ABI_NM) == "undefined" && item.ABI_NM == "") ? "-" : item.ABI_NM;
  					$("#abi"+i).text(abiNm);
  				})
				
  				/* 
				var year = abiDt.YEAR;
				var smt = abiDt.SMT;
				var deptCd = abiDt.DEPT_CD;
				var abiName = (typeof(abiDt.ABI_NAME) == "undefined" && abiDt.ABI_NAME == "") ? "핵심역량/전공능력" : abiDt.ABI_NAME;
				var abiNmList = (typeof(abiDt.ABI_NM_LIST) == "undefined") ? "등록된 데이터가 없습니다." : abiDt.ABI_NM_LIST; */
  				
  				/* $("#abiNm").text(abiName);
  	  			$("#abiNmList").text(abiNmList); */
  	  			
  			} else{/* 
  				$("#abiNm").text("전공능력");
  	  			$("#abiNmList").text("등록된 데이터가 없습니다."); */
  			}
  			
  			
  			/* 강의평가 */
  			var varItemObj = $("#evalList");
  			varItemObj.find("tr").remove();
  			
  			var list = result.list;
  			if(list) {
  				$.each(list, function(i, item){
  					var varCon = '<tr>\n';
  					varCon += '<td class="year_semi text-start text-lg-center py-2">' + item.YEAR  + '-' + item.SMT_NM + '</td>\n';
  					varCon += '<td class="name text-start text-lg-center py-2">' + item.SUBJECT_CD + '</td>\n';
  					varCon += '<td class="numb text-start text-lg-center py-2">' + item.SUBJECT_CD + '-' + item.DIVCLS + '</td>\n';
  					varCon += '<td class="pepl text-start text-lg-center py-2">' + item.SUGANG_STD_CNT + '</td>\n';
  					varCon += '<td class="app_pepl text-start text-lg-center py-2">' + item.ANSWER_STD_CNT + '</td>\n';
  					varCon += '<td class="avera text-start text-lg-center py-2">' + item.LECESTI_AVG + '</td>\n';
  					varCon += '<td class="note text-start text-lg-center py-2"></td>\n';
  					
  					varCon += '</tr>\n';
  					varItemObj.append(varCon);
  					 
  				});
  			}  else{
				var varCon = '<tr>\n';
				varCon += '<td colspan="7" class="bllist"><spring:message code="message.no.list"/></td>';
				varCon += '</tr>\n';
				varItemObj.append(varCon);
			}
		
			
  			return false;
  		}, 
  		error:function(request,error){
  			alert("실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	});
}

/* 수업계획서 보기 */
function getPlanView(empNo, year, divcls, smt){  
	
	var subjectCd = "${sbjtInfo.SUBJECT_CD}";
	var deptCd = "${sbjtInfo.DEPT_CD}";
	
	var varAction = "/web/sbjt/planView.json?mId=32&SUBJECT_CD=" + subjectCd + "&DEPT_CD=" + deptCd + "&EMP_NO=" + empNo + "&YEAR=" + year + "&SMT=" + smt + "&DIVCLS=" + divcls;
	
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			
  			var planObject = $(".plan");
  			planObject.empty();
  			
  			var coreGbObject = $("#coreGbList");
  			var coreObject = $("#coreList");
  			var valObject = $("#coreVal");
  			coreGbObject.find("th").remove();
  			coreObject.find("td").remove();
  			valObject.find("td").remove();
  			
  			var coreList = result.coreList;
  			
  			var coreCheck = '';
  			if(coreList){
  				$.each(coreList, function(i, item){
  					var abiGbNm = (typeof(item.ABI_GB_NM) == 'undefined' || item.ABI_GB_NM == '') ? '-' : item.ABI_GB_NM;
  					var abiNm = (typeof(item.ABI_NM) == 'undefined' || item.ABI_NM == '') ? '-' : item.ABI_NM;
  					var mainGbNm = (typeof(item.MAIN_GB_NM) == 'undefined' || item.MAIN_GB_NM == '') ? '-' : item.MAIN_GB_NM;
  					
					if (coreCheck !== null && coreCheck !== abiGbNm) {
				        var conGb = '<th scope="col" colspan="2">' + abiGbNm + '</th>';
				        coreGbObject.append(conGb);
				    }
					coreCheck = abiGbNm;
					
					
  					var  varCore = '<td>' + abiNm + '</td>';
  					coreObject.append(varCore);
  					
  					var varVal = '<td>' + mainGbNm + '</td>';
  					
  					valObject.append(varVal);
  				});
  			}
  			
  			var deptChk = result.deptCd;
  			if(deptChk != '446000'){
  				var abiObject = $("#abiList");
  	  			var abiValObject = $("#abiVal");
  	  			abiObject.find("td").remove();
  	  			abiValObject.find("td").remove();
  	  			
  	  			var abiList = result.abiList;
  	  		
  	  			if(abiList){
  	  				$.each(abiList, function(i, item){
  	  					var abiNm = (typeof(item.ABI_NM) == 'undefined' || item.ABI_NM == '') ? '-' : item.ABI_NM;
  	  					var mainGbNm = (typeof(item.MAIN_GB_NM) == 'undefined' || item.MAIN_GB_NM == '') ? '-' : item.MAIN_GB_NM;
  	  					
  	  					var varAbi = '<td scope="col">' + abiNm + '</td>';
  	  					abiObject.append(varAbi);
  	  					
  	  					var varVal = '<td>' + mainGbNm + '</td>';
  	  					
  	  					abiValObject.append(varVal);
  	  				});
  	  			}
  			} else{
  				var abiRow = $("#abiRow");
  				abiRow.remove();
  			}
  			
  			
  			
  			var list = result.weekList;
  			var bookList = result.bookList;
  			
  			/* 수업계획서  */
  			var dt = result.dt;
  			if(dt){
  				
  				var grade = (typeof(dt.GRADE) == "undefined") ? "-" : dt.COM_GRADE;
				var year = dt.YEAR;
				var smt = dt.SMT;
				var smtNm = dt.SMT_NAME;
				var subjectNm = dt.SUBJECT_NM;
				var subjectCd = dt.SUBJECT_CD;
				var deptCd = dt.DEPT_CD;
				var deptNm = (typeof(dt.DEPT_NM) == "undefined") ? "-" : dt.DEPT_NM;
				var colgNm = (typeof(dt.COLG_NM) == "undefined") ? "-" : dt.COLG_NM;
				var divcls = (typeof(dt.DIVCLS) == "undefined") ? "-" : dt.DIVCLS;
				var sbjDiv = subjectCd + "-" + divcls;
				var empNm = (typeof(dt.EMP_EMAIL) == "undefined") ? "-" : dt.EMP_EMAIL;
				var empEmail = (typeof(dt.EMP_EMAIL) == "undefined") ? "-" : dt.EMP_EMAIL;
				var empInfo = empNm + "(" + empEmail + ")";
				
				var codeName = (typeof(dt.COMDIV_CODE_NAME) == "undefined") ? "-" : dt.COMDIV_CODE_NAME;
				var comdivCode = (typeof(dt.COMDIV_CODE) == "undefined") ? "-" : dt.COMDIV_CODE;
				var cdtNum = (typeof(dt.CDT_NUM) == "undefined") ? "-" : dt.CDT_NUM;
				var wtime = (typeof(dt.WTIME_NUM) == "undefined") ? "-" : dt.WTIME_NUM;
				var ptime = (typeof(dt.PTIME_NUM) == "undefined") ? "-" : dt.PTIME_NUM;
				var sisu = wtime + "/" + ptime;
				var subjDesc = (typeof(dt.SUBJ_DESC) == "undefined") ? "-" : dt.SUBJ_DESC.replace(/\n/g, '<br>');
				var method = (typeof(dt.METHOD) == "undefined") ? "-" : dt.METHOD.replace(/\n/g, '<br>');
				var sugangTrg = (typeof(dt.SUGANG_TRG) == "undefined") ? "-" : dt.SUGANG_TRG;
				var cqiImp = (typeof(dt.CQI_IMP) == "undefined") ? "-" : dt.CQI_IMP;
				var coreGoal = (typeof(dt.CORE_GOAL) == "undefined") ? "-" : dt.CORE_GOAL;
				var majorGoal = (typeof(dt.MAJOR_GOAL) == "undefined") ? "-" : dt.MAJOR_GOAL;
				var clsGoal1 = (typeof(dt.CLS_GOAL1) == "undefined") ? "-" : dt.CLS_GOAL1;
				var clsGoal2 = (typeof(dt.CLS_GOAL2) == "undefined") ? "-" : dt.CLS_GOAL2;
				var clsGoal3 = (typeof(dt.CLS_GOAL3) == "undefined") ? "-" : dt.CLS_GOAL3;
				var prefocusFl = (typeof(dt.LECPLN_FOCUS_FL) == "undefined") ? "-" : dt.LECPLN_FOCUS_FL;
				var focusFl = (prefocusFl == '1') ? '■ 집중이수' : '□ 집중이수';
				var focusMethod = (typeof(dt.LECPLN_FOCUS_METHOD) == "undefined") ? "-" : dt.LECPLN_FOCUS_METHOD;
				var nonSbjtDeptNm = (typeof(dt.NONSUBJECT_DEPT_NM) == "undefined") ? "-" : dt.NONSUBJECT_DEPT_NM;
				var nonnSbjtMethod = (typeof(dt.NONSUBJECT_METHOD) == "undefined") ? "-" : dt.NONSUBJECT_METHOD;
				var prefuseFl = (typeof(dt.LECPLN_FUSE_FL) == "undefined") ? "-" : dt.LECPLN_FUSE_FL;
				var prelinkFl = (typeof(dt.LECPLN_LINK_FL) == "undefined") ? "-" : dt.LECPLN_LINK_FL;
				var premicroFl = (typeof(dt.LECPLN_MICRO_FL) == "undefined") ? "-" : dt.LECPLN_MICRO_FL;
				var prenanoFl = (typeof(dt.LECPLN_NANO_FL) == "undefined") ? "-" : dt.LECPLN_NANO_FL;
				var fuseFl = (prefuseFl == '1') ? '■ 융합전공 ' : '□ 융합전공 ';
				var linkFl = (prelinkFl == '1') ? '■ 연계전공 ' : '□ 연계전공 ';
				var microFl = (premicroFl == '1') ? '■ 마이크로디그리 ' : '□ 마이크로디그리 ';
				var nanoFl = (prenanoFl == '1') ? '■ 나노디그리 ' : '□ 나노디그리 ';
				var dnaMethod = (typeof(dt.DNA_METHOD) == "undefined") ? "-" : dt.DNA_METHOD;
				var clsTpEtc = (typeof(dt.CLS_TP_ETC) == "undefined") ? "-" : dt.CLS_TP_ETC;
				var envirOnline = (typeof(dt.ENVIR_ONLINE_FL) == "undefined") ? "-" : dt.ENVIR_ONLINE_FL;
				var envirOffline = (typeof(dt.ENVIR_OFFLINE_FL) == "undefined") ? "-" : dt.ENVIR_OFFLINE_FL;
				var envirBlend = (typeof(dt.ENVIR_BLEND_FL) == "undefined") ? "-" : dt.ENVIR_BLEND_FL;
				var online = (envirOnline == '1') ? '■ 온라인(원격) ' : '□ 온라인(원격) ';
				var offline = (envirOffline == '1') ? '■ 오프라인' : '□ 오프라인';
				var blend = (envirBlend == '1') ? '■ 블렌디드(혼합)' : '□ 블렌디드(혼합)';
				var typeCode1 = (typeof(dt.LEC_TYPE_CODE1) == "undefined") ? "□ 강의 " : '■ 강의 ';
				var typeCode2 = (typeof(dt.LEC_TYPE_CODE2) == "undefined") ? "□ 실험/실습/실기 " : '■ 실험/실습/실기 ';
				var typeCode3 = (typeof(dt.LEC_TYPE_CODE3) == "undefined") ? "□ 토의/토론 " : '■ 토의/토론 ';
				var typeCode4 = (typeof(dt.LEC_TYPE_CODE4) == "undefined") ? "□ 발표 " : '■ 발표 ';
				var typeCode5 = (typeof(dt.LEC_TYPE_CODE5) == "undefined") ? "□ 질의응답 " : '■ 질의응답 ';
				var typeCode6 = (typeof(dt.LEC_TYPE_CODE6) == "undefined") ? "□ 문제풀이학습 " : '■ 문제풀이학습 ';
				var typeCode7 = (typeof(dt.LEC_TYPE_CODE7) == "undefined") ? "□ 협력학습(팀기반학습) " : '■ 협력학습(팀기반학습) ';
				var typeCode8 = (typeof(dt.LEC_TYPE_CODE8) == "undefined") ? "□ 문제기반학습(PBL) " : '■ 문제기반학습(PBL) ';
				var typeCode9 = (typeof(dt.LEC_TYPE_CODE9) == "undefined") ? "□ 프로젝트기반학습(PJBL) " : '■ 프로젝트기반학습(PJBL) ';
				var typeCode10 = (typeof(dt.LEC_TYPE_CODE10) == "undefined") ? "□ 사례기반학습 " : '■ 사례기반학습 ';
				var typeCode11 = (typeof(dt.LEC_TYPE_CODE11) == "undefined") ? "□ 플립러닝 " : '■ 플립러닝 ';
				var typeCode12 = (typeof(dt.LEC_TYPE_CODE12) == "undefined") ? "□ 전문가초청 " : '■ 전문가초청 ';
				var typeCode13 = (typeof(dt.LEC_TYPE_CODE13) == "undefined") ? "□ 세미나 " : '■ 세미나 ';
				var typeCode14 = (typeof(dt.LEC_TYPE_CODE14) == "undefined") ? "□ 현장견학(실습) " : '■ 현장견학(실습) ';
				var typeCode15 = (typeof(dt.LEC_TYPE_CODE15) == "undefined") ? "□ 논문지도 " : '■ 논문지도 ';
				var typeDesc = (typeof(dt.LEC_TYPE_DESC) == "undefined") ? "-" : dt.LEC_TYPE_DESC;
				var midCode = (typeof(dt.MID_CODE) == "undefined") ? "-" : dt.MID_CODE;
				var midDesc = (typeof(dt.MID_DESC) == "undefined") ? "-" : dt.MID_DESC;
				var midRt = (typeof(dt.MID_RT) == "undefined") ? "-" : dt.MID_RT;
				var endCode = (typeof(dt.END_CODE) == "undefined") ? "-" : dt.END_CODE;
				var endDesc = (typeof(dt.END_DESC) == "undefined") ? "-" : dt.END_DESC;
				var endRt = (typeof(dt.END_RT) == "undefined") ? "-" : dt.END_RT;
				var attCode = (typeof(dt.ATT_CODE) == "undefined") ? "-" : dt.ATT_CODE;
				var attDesc = (typeof(dt.ATT_DESC) == "undefined") ? "-" : dt.ATT_DESC;
				var attRt = (typeof(dt.ATT_RT) == "undefined") ? "-" : dt.ATT_RT;
				var etcType1 = (typeof(dt.ETC_TYPE1) == "undefined") ? "기타1" : dt.ETC_TYPE1;
				var etcCode1 = (typeof(dt.ETC_CODE1) == "undefined") ? "-" : dt.ETC_CODE1;
				var etcDesc1 = (typeof(dt.ETC_DESC1) == "undefined") ? "-" : dt.ETC_DESC1;
				var etcRt1 = (typeof(dt.ETC_RT1) == "undefined") ? "-" : dt.ETC_RT1;
				var etcType2 = (typeof(dt.ETC_TYPE2) == "undefined") ? "기타2" : dt.ETC_TYPE2;
				var etcCode2 = (typeof(dt.ETC_CODE2) == "undefined") ? "-" : dt.ETC_CODE2;
				var etcDesc2 = (typeof(dt.ETC_DESC2) == "undefined") ? "-" : dt.ETC_DESC2;
				var etcRt2 = (typeof(dt.ETC_RT2) == "undefined") ? "-" : dt.ETC_RT2;
				var etcType3 = (typeof(dt.ETC_TYPE3) == "undefined") ? "기타3" : dt.ETC_TYPE3;
				var etcCode3 = (typeof(dt.ETC_CODE3) == "undefined") ? "-" : dt.ETC_CODE3;
				var etcDesc3 = (typeof(dt.ETC_DESC3) == "undefined") ? "-" : dt.ETC_DESC3;
				var etcRt3 = (typeof(dt.ETC_RT3) == "undefined") ? "-" : dt.ETC_RT3;
				var etcType4 = (typeof(dt.ETC_TYPE4) == "undefined") ? "기타4" : dt.ETC_TYPE4;
				var etcCode4 = (typeof(dt.ETC_CODE4) == "undefined") ? "-" : dt.ETC_CODE4;
				var etcDesc4 = (typeof(dt.ETC_DESC4) == "undefined") ? "-" : dt.ETC_DESC4;
				var etcRt4 = (typeof(dt.ETC_RT4) == "undefined") ? "-" : dt.ETC_RT4;
				var toolEqu = (typeof(dt.TOOL_EQU) == "undefined") ? "-" : dt.TOOL_EQU;
				var toolSw = (typeof(dt.TOOL_SW) == "undefined") ? "-" : dt.TOOL_SW;
				var toolEtc = (typeof(dt.TOOL_ETC) == "undefined") ? "-" : dt.TOOL_ETC;
				var studySupp1 = (typeof(dt.STUDY_SUPP_CODE1) == "undefined") ? "□ 교육기자재 " : '■ 교육기자재 ';
				var studySupp2 = (typeof(dt.STUDY_SUPP_CODE2) == "undefined") ? "□ 대체·보조자료 " : '■ 대체·보조자료 ';
				var studySupp3 = (typeof(dt.STUDY_SUPP_CODE3) == "undefined") ? "□ 지정좌석 " : '■ 지정좌석 ';
				var studySupp4 = (typeof(dt.STUDY_SUPP_CODE4) == "undefined") ? "□ 수업녹음녹화 " : '■ 수업녹음녹화 ';
				var studySupp5 = (typeof(dt.STUDY_SUPP_CODE5) == "undefined") ? "□ 도우미(튜터링, 대필, 통역 등) " : '■ 도우미(튜터링, 대필, 통역 등) ';
				var estiSupp1 = (typeof(dt.ESTI_SUPP_CODE1) == "undefined") ? "□ 평가장소 " : '■ 평가장소 ';
				var estiSupp2 = (typeof(dt.ESTI_SUPP_CODE2) == "undefined") ? "□ 평가시간 " : '■ 평가시간 ';
				var estiSupp3 = (typeof(dt.ESTI_SUPP_CODE3) == "undefined") ? "□ 평가방법 " : '■ 평가방법 ';
				var etcSupp = (typeof(dt.ETC_SUPP) == "undefined") ? "-" : dt.ETC_SUPP;
				
  				console.log(dt);
  				
  				/* 수업계획서 기본정보 */
  				$("#sbjtNamePlan").text(subjectNm);
  	  			$("#sbjtENamePlan").text(dt.SUBJECT_ENM);
  	  			$("#yearPlan").text(year);
  	  			$("#empPlan").text(empInfo);
  	  			$("#smtPlan").text(smtNm);
  	  			$("#comdivPlan").text(codeName);
  	  			
	  			$("#collegePlan").text(colgNm);
	  			$("#deptNmPlan").text(deptNm);
	  			$("#sbjDivPlan").text(sbjDiv);
	  			$("#cdtNumPlan").text(cdtNum);
	  			$("#sisuPlan").text(sisu);
	  			
	  			/* 교과목정보 */
	  			$("#sbjtDescPlan").append(subjDesc);
	  			$("#sbjtDetailPlan").append(method);
	  			$("#sugangPlan").text(sugangTrg);
	  			/* $("#cdtNumPlan").text(cdtNum);
	  			$("#sisuPlan").text(sisu); */
	  			/* 교과목정보  - 수업목표*/
	  			$("#coreGoalPlan").text(coreGoal);
	  			$("#majorGoalPlan").text(majorGoal);
	  			$("#clsGoal1Plan").text(clsGoal1);
	  			$("#clsGoal2Plan").text(clsGoal2);
	  			$("#clsGoal3Plan").text(clsGoal3);
	  			/* 교과목정보 - 수업유형 */  
	  			$("#focusFlPlan").text(focusFl);
	  			$("#focusMethodPlan").text(focusMethod);
	  			$("#nonSbjtDeptNmPlan").text(nonSbjtDeptNm);
	  			$("#nonnSbjtMethodPlan").text(nonnSbjtMethod);
	  			$("#fuseFlPlan").text(fuseFl);
	  			$("#linkFlPlan").text(linkFl);
	  			$("#microFlPlan").text(microFl);
	  			$("#nanoFlPlan").text(nanoFl);
	  			$("#dnaMethodPlan").text(dnaMethod);
	  			$("#clsTpEtcPlan").text(clsTpEtc);
	  			/* 교과목정보 - 교수학습환경방법 */
	  			$("#onlinePlan").text(online);
	  			$("#offlinePlan").text(offline);
	  			$("#blendPlan").text(blend);
	  			$("#typeCode1Plan").text(typeCode1);
	  			$("#typeCode2Plan").text(typeCode2);
	  			$("#typeCode3Plan").text(typeCode3);
	  			$("#typeCode4Plan").text(typeCode4);
	  			$("#typeCode5Plan").text(typeCode5);
	  			$("#typeCode6Plan").text(typeCode6);
	  			$("#typeCode7Plan").text(typeCode7);
	  			$("#typeCode8Plan").text(typeCode8);
	  			$("#typeCode9Plan").text(typeCode9);
	  			$("#typeCode10Plan").text(typeCode10);
	  			$("#typeCode11Plan").text(typeCode11);
	  			$("#typeCode12Plan").text(typeCode12);
	  			$("#typeCode13Plan").text(typeCode13);
	  			$("#typeCode14Plan").text(typeCode14);
	  			$("#typeCode15Plan").text(typeCode15);
	  			if(typeDesc == '-' || typeDesc == ''){
	  				$("#typeDescPlanBefore").text('□');
	  			} else{
	  				$("#typeDescPlanBefore").text('■');
	  			}
	  			$("#typeDescPlanAfter").text(typeDesc);
	  			/* 교과목정보 - 평가방법내용 */
	  			$("#midCodePlan").text(midCode);
	  			$("#midDescPlan").text(midDesc);
	  			$("#midRtPlan").text(midRt);
	  			$("#endCodePlan").text(endCode);
	  			$("#endDescPlan").text(endDesc);
	  			$("#endRtPlan").text(endRt);
	  			$("#attCodePlan").text(attCode);
	  			$("#attDescPlan").text(attDesc);
	  			$("#attRtPlan").text(attRt);
	  			$("#etcType1Plan").text(etcType1);
	  			$("#etcCode1Plan").text(etcCode1);
	  			$("#etcDesc1Plan").text(etcDesc1);
	  			$("#etcRt1Plan").text(etcRt1);
	  			$("#etcType2Plan").text(etcType2);
	  			$("#etcCode2Plan").text(etcCode2);
	  			$("#etcDesc2Plan").text(etcDesc2);
	  			$("#etcRt2Plan").text(etcRt2);
	  			$("#etcType3Plan").text(etcType3);
	  			$("#etcCode3Plan").text(etcCode3);
	  			$("#etcDesc3Plan").text(etcDesc3);
	  			$("#etcRt3Plan").text(etcRt3);
	  			$("#etcType4Plan").text(etcType4);
	  			$("#etcCode4Plan").text(etcCode4);
	  			$("#etcDesc4Plan").text(etcDesc4);
	  			$("#etcRt4Plan").text(etcRt4);
	  			var totalRt = midRt + endRt + attRt + etcRt1 + etcRt2 + etcRt3 + etcRt4;
	  			$("#totalRtPlan").text(totalRt);
	  			/* 교과목정보 - CQI */
	  			$("#cqiImpPlan").text(cqiImp);
	  			/* 교과목정보 - 장애학생 수업지원 */
	  			$("#studySupp1Plan").text(studySupp1);
	  			$("#studySupp2Plan").text(studySupp2);
	  			$("#studySupp3Plan").text(studySupp3);
	  			$("#studySupp4Plan").text(studySupp4);
	  			$("#studySupp5Plan").text(studySupp5);
	  			$("#estiSupp1Plan").text(estiSupp1);
	  			$("#estiSupp2Plan").text(estiSupp2);
	  			$("#estiSupp3Plan").text(estiSupp3);
	  			$("#etcSuppPlan").text(etcSupp);
	  			/* 교수학습자료 */
	  			$("#toolEquPlan").text(toolEqu);
	  			$("#toolSwPlan").text(toolEqu);
	  			$("#toolEtcPlan").text(toolEqu);
	  			
  	  		
  			}
  			
  			var weekObject = $("#weekList");
  			var bookObject = $("#bookList");
  			var bookEach = $(".book");
  			var linkObject = $("#linkList");
  			var linkEach = $(".link");
  			weekObject.find("tr").remove();
  			bookObject.find("tr").remove();
  			bookEach.remove();
  			linkObject.next("tr").remove();
  			linkEach.remove();
  			
  			var list = result.weekList;
  			var bookList = result.bookList;
  			var bookCount = result.bookTotalCount;
  			var linkList = result.linkList;
  			var linkCount = result.linkTotalCount;
  			
  			if(bookCount != '0'){
  				$("#bookCnt").attr("rowspan",bookCount + 1);
  			} else{
  				$("#bookCnt").attr("rowspan",2);
  			}
  			
  			if(linkCount != '0'){
  				$("#linkCnt").attr("rowspan",linkCount + 1);
  				$("#classTypePlan").attr("rowspan",linkCount + 9);
  			} else{
  				$("#classTypePlan").attr("rowspan",linkCount + 10);
  			}
  			
  			/* 교과목정보 - 수업유형(산학연연계) */
  			if(linkList.length != '0'){
	  				$.each(linkList, function(i, item){
	  					var linkCompNm = (typeof(item.LINK_COMP_NM) == 'undefined' || item.LINK_COMP_NM == '') ? '-' : item.LINK_COMP_NM;
	  					var linkSummary = (typeof(item.LINK_SUMMARY) == 'undefined' || item.LINK_SUMMARY == '') ? '-' : item.LINK_SUMMARY;
	  					
	  					var  varCon = '<tr class="link">';
	  					varCon += '<td colspan="3">' + linkCompNm + '</td>';
	  					varCon += '<td colspan="4">' + linkSummary + '</td>';
	  					varCon += ''
	  					linkObject.after(varCon);
	  				});
  			} else {
  					var  varCon = '<tr class="link">';
  					varCon += '<td colspan="3"></td>';
					varCon += '<td colspan="4"></td>';
					varCon += '</tr>'
					linkObject.after(varCon);
  			}
  			
  			/* 교수학습자료 - 교재 */
  			if(bookList.length != '0'){
  				$.each(bookList, function(i, item){
  					var bookCodeNm = item.BOOK_CODE_NM;
  					var bookNm = (typeof(item.BOOK) == 'undefined' || item.BOOK == '') ? '-' : item.BOOK;
  					var author = (typeof(item.AUTHOR) == 'undefined' || item.AUTHOR == '') ? '-' : item.AUTHOR;
  					var publiComp = (typeof(item.PUBLI_COMP) == 'undefined' || item.PUBLI_COMP == '') ? '-' : item.PUBLI_COMP;
  					var publiYear = (typeof(item.PUBLI_YEAR) == 'undefined' || item.PUBLI_YEAR == '') ? '-' : item.PUBLI_YEAR;
  					
  					var varCon ='<tr class="book">';
					varCon += '<td id="">' + bookCodeNm + '</td>';
					varCon += '<td id="" colspan="3">' + bookNm + '</td>';
					varCon += '<td id="">' + author + '</td>';
					varCon += '<td id="" colspan="2">' + publiComp + '</td>';
					varCon += '<td id="">' + publiYear + '</td>';
					varCon += '</tr>';
  					
  					bookObject.before(varCon);
  					
  				});
  			} else{
  				var varCon ='<tr class="book text-center">';
				varCon += '<td colspan="8">등록된 교재가 없습니다.</td>';
				bookObject.before(varCon);
  			}
  			/* 주차별 수업계획 */
  			if(list){
  				$.each(list, function(i, item){
  					var weekTp = (i == '15' || i == '16') ? '기타' : item.WEEK_TP;
  					var content = (typeof(item.CURI_CONTENT) == 'undefined' || item.CURI_CONTENT == '') ? '-' : item.CURI_CONTENT;
  					var online = (typeof(item.ENVIR_ONLINE_FL) == 'undefined' || item.ENVIR_ONLINE_FL == '') ? '-' : item.ENVIR_ONLINE_FL;
  					var offline = (typeof(item.ENVIR_OFFLINE_FL) == 'undefined' || item.ENVIR_OFFLINE_FL == '') ? '-' : item.ENVIR_OFFLINE_FL;
  					var blend = (typeof(item.ENVIR_BLEND_FL) == 'undefined' || item.ENVIR_BLEND_FL == '') ? '-' : item.ENVIR_BLEND_FL;
  					var lecTypeDesc = (typeof(item.LEC_TYPE_DESC) == 'undefined' || item.LEC_TYPE_DESC == '') ? '-' : item.LEC_TYPE_DESC;
  					var studyData = (typeof(item.STUDY_DATA) == 'undefined' || item.STUDY_DATA == '') ? '-' : item.STUDY_DATA;
  					var etc = (typeof(item.ETC) == 'undefined' || item.ETC == '') ? '-' : item.ETC;
					var method = '';
					if(online == '1'){
						method = "온라인";
					} else if(offline == '1') {
						method = "오프라인";
					} else if(blend == '1'){
						method = "온라인/오프라인";
					}
  					
  					var varCon ='<tr>';
  					varCon += '<td class="">' + weekTp + '</td>';
  					varCon += '<td colspan="3" class="">' + content + '</td>';
  					varCon += '<td class="">' + method + '</td>';
  					varCon += '<td colspan="2" class="">' + lecTypeDesc + '</td>';
  					varCon += '<td colspan="2" class="">' + studyData + '</td>';
  					varCon += '<td class="">' + etc + '</td>';
  					varCon +='</tr>';
  					
                    
  					weekObject.append(varCon);		
  				});
  			}
			
  			
  			return false;
  		}, 
  		error:function(request,error){
  			alert("수업계획서 실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	});
}



/* 강의평가 목록 불러오기 */
function getEvalList(subjectCd, empNo, year, page){
	
	
	var varAction = "${elfn:replaceScriptLink(URL_EVALLIST)}&SUBJECT_CD=" + subjectCd + "&EMP_NO=" + empNo + "&YEAR=" + year + "&page=" + page ;
	
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			
  			var varItemObj = $("#evalList");
  			varItemObj.find("tr").remove();
  			var htmlPage = $("#evalPage");
  			htmlPage.empty();
  			
  			var list = result.list;
  			if(list) {
  				$.each(list, function(i, item){
  					var varCon = '<tr>\n';
  					varCon += '<td class="year_semi text-start text-lg-center py-2">' + item.YEAR  + '-' + item.SMT_NM + '</td>\n';
  					varCon += '<td class="name text-start text-lg-center py-2">' + item.SUBJECT_CD + '</td>\n';
  					varCon += '<td class="numb text-start text-lg-center py-2">' + item.SUBJECT_CD + '-' + item.DIVCLS + '</td>\n';
  					varCon += '<td class="pepl text-start text-lg-center py-2">' + item.SUGANG_STD_CNT + '</td>\n';
  					varCon += '<td class="app_pepl text-start text-lg-center py-2">' + item.ANSWER_STD_CNT + '</td>\n';
  					varCon += '<td class="avera text-start text-lg-center py-2">' + item.LECESTI_AVG + '</td>\n';
  					varCon += '<td class="note text-start text-lg-center py-2"></td>\n';
  					
  					varCon += '</tr>\n';
  					varItemObj.append(varCon);
  					 
  				});
  				
  				
  			
  			
  			} /* else{
  				var varCon = '<tr>\n';
 					varCon += '<td colspan="8" class="bllist"><spring:message code="message.no.list"/></td>';
 					varCon += '</tr>\n';
 					varItemObj.append(varCon);
  			} */
  			
  			
  			/* 강의개설이력 페이징  */
  			var pageInfo = result.paginationInfo;
  			
  			var currentPageNo = pageInfo.currentPageNo;
  			var totalPageCount = pageInfo.totalPageCount;
  			var pageSize = pageInfo.pageSize;
  			var firstPageNo = pageInfo.firstPageNo;
  			var lastPageNo = pageInfo.lastPageNo;
  			var firstPageNoOnPageList = pageInfo.firstPageNoOnPageList;
  			var lastPageNoOnPageList = pageInfo.lastPageNoOnPageList;
  			var pageCon = "";
  			
  			
  			if(totalPageCount > pageSize){
  				if(firstPageNoOnPageList > pageSize){
  					var prevPageNo = firstPageNoOnPageList;
  				} else{
  					var prevPageNo = firstPageNo;
  				}
  				pageCon += '<li class="page-item page-first"><a href="javascript:getLectureList(' + firstPageNo + ');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
  				pageCon += '<li class="page-item page-prev"><a href="javascript:getLectureList(' + prevPageNo + ');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';
  	  	  		
  			}
  			
  			
	  		for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
	  			if(pageNo == currentPageNo){
	  				pageCon += '<li class="page-item active"><a class="page-link" href="javascript:getLectureList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</li>';
	  			} else{
	  				pageCon += '<li class="page-item"><a class="page-link" href="javascript:getLectureList(' + pageNo + ');" title="' + pageNo + '페이지" value="' + pageNo + '">' + pageNo + '</a></li>';
	  			}
	  		}
  	  		if(totalPageCount > pageSize){
  				if(lastPageNoOnPageList < totalPageCount){
  					var nextPageNo = firstPageNoOnPageList + pageSize;
  				} else{
  					var nextPageNo = lastPageNo;
  				}
  				pageCon += '<li class="page-item page-next"><a href="javascript:getLectureList(' + nextPageNo + ');" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>';
  	  			pageCon += '<li class="page-item page-last"><a href="javascript:getLectureList(' + lastPageNo + ');" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>';
  	  		
  			}
	  		
  			
  			htmlPage.append(pageCon);
			
			
  			return false;
  		}, 
  		error:function(request,error){
  			alert("실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	});
}


//찜 가져오기
function getBookmarkList(){
	return new Promise((resolve, reject) => {		
		$.ajax({
			url: '/web/bookmark/getBookmarkList.do?mId=37',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    menuFg : 'sbjt'
			}),
			success: function(data){			
				resolve(data.bookmarkList);
			},error:function() {
				reject("");
			}
		});
	})
}

//관심강좌 가져오기
function getOpenLecList(){
	return new Promise((resolve, reject) => {		
		$.ajax({
			url: '/web/bookmark/getBookmarkList.do?mId=37',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    menuFg : 'lecture'
			}),
			success: function(data){			
				resolve(data.bookmarkList);
			},error:function() {
				reject("");
			}
		});
	})
}

//찜 변경
function likeChange(docId, type){
	
	//찜 등록/삭제
	if(type != 'lecture'){
		if(!$("#"+docId).hasClass("on_red")){
			$.ajax({
				url: '/web/bookmark/insertBookmark.do?mId=37&menuFg=' + type,
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    docId : docId
				}),
				success: function(data){
					$("#"+docId).addClass("on_red");
				}
			});
		} else{
			$.ajax({
				url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg=' + type,
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    docId : docId
				}),
				success: function(data){
					$("#"+docId).removeClass("on_red");
				}
			});
		}
	} else{
		if(!$("#lecture_"+docId).hasClass("alrea")){
			$.ajax({
				url: '/web/bookmark/insertBookmark.do?mId=37&menuFg=' + type,
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    docId : docId
				}),
				success: function(data){
					$("#fillLov").attr('style','display:block;');
					$("#rLov").attr('style','display:none;');
					$("#lecture_"+docId).addClass("alrea");
					alert("관심강좌에 등록 되었습니다.");
				}
			});
		} else {
			$.ajax({
				url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg=' + type,
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    docId : docId
				}),
				success: function(data){
					$("#rLov").attr('style','display:block;');
					$("#fillLov").attr('style','display:none;');
					$("#lecture_"+docId).removeClass("alrea");
					alert("관심강좌에서 삭제 되었습니다.");
				}
			});
		}
	}
// 	})
}


//장바구니
function cartAddRemove(year, smt, subjectCd, deptCd, divcls, sbjtKorNm, empNo, id){
	
	if(yearCheck != year){
		alert("현재년도가 아닙니다.");
		return false;
	}
	if(smtCheck != smt){
		alert("현재학기가 아닙니다.");
		return false;
	} 
	
	
	if (!$("#"+id).hasClass("alrea")) { // 추가
		$.ajax({
			url: '/web/basket/insertBsk.do?mId=52',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
				year : year,
			    smt : smt,
			    subjectCd : subjectCd,
			    deptCd : deptCd,
			    divcls : divcls,
			    sbjt : sbjtKorNm,
			    empNo : empNo
			}),
			success: function(data){
				$("#fillSho").attr('style','display:block;');
				$("#bSho").attr('style','display:none;');
				$("#"+id).addClass("alrea");
				//$("#"+id).removeClass("disa");
				alert("장바구니에 등록 되었습니다.");
				//location.reload();
			}
		});	
	} else { // 삭제
		$.ajax({
			url: '/web/basket/deleteBsk.do?mId=52',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
				year : year,
			    smt : smt,
			    subjectCd : subjectCd,
			    deptCd : deptCd,
			    divcls : divcls,
			    sbjt : sbjtKorNm,
			    empNo : empNo
			}),
			success: function(data){
				$("#bSho").attr('style','display:block;');
				$("#fillSho").attr('style','display:none;');
				$("#"+id).removeClass("alrea");
				//$("#"+id).addClass("disa");
				alert("장바구니에서 삭제 되었습니다.");
				//location.reload();
			}
		});
	}
}

function getCurInfo(){
	return new Promise((resolve, reject) => {
		$.ajax({
			url: '/web/basket/getCurInfo.json?mId=52',
			dataType:'json',	
			type: 'POST',
			async:true,
	  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
	  		success:function(result){
				resolve(result.dt);
			}, 
	  		error:function(){
	  			reject("");
	  		}
		});	
	})
}


//장바구니 가져오기
function getBasketCart(year,smt,subjectCd,deptCd,divcls,empNo){
	return new Promise((resolve, reject) => {		
		$.ajax({
			url: '/web/basket/getSbjtBasket.do?mId=52',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
				year : year,
			    smt : smt,
			    subjectCd : subjectCd,
			    deptCd : deptCd,
			    divcls : divcls,
			    empNo : empNo
			}),
			success: function(data){			
				resolve(data.basketList);
			},error:function() {
				reject("");
			}
		});
	})
}

</script>