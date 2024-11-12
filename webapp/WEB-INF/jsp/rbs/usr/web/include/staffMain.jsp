<%@ include file="commonTop.jsp" %>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tabulator.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/Chart.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/chartjs-plugin-doughnutlabel.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/all.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/moment.js"/>"></script>


<script>

/* ====================================================================================
*
* Declare
*/
var page_prop 			= {};


//Element
const $chartEMP 		= 'chartEMP';

//추천검색어(순서 섞었음)
var suggestions = [
    "해양 특성화", "글로벌 네트워크", "산학 협력", "인공지능", "커리어 개발",
    "해외 연수", "융복합 교육", "취업 준비", "글로벌 교육", "연구 개발",
    "창업 지원", "커뮤니케이션 스킬", "직무 경험", "국제 협력", "기술 혁신",
    "리더십 프로그램", "메타버스", "자기 개발", "산학연 협력", "글로벌 리더십",
    "취업 역량 강화", "해외 인턴십", "맞춤형 교육", "연구 역량 강화", "신기술 학습",
    "문화 이해", "직무 역량 강화", "국제 교류", "정책", "실습 기회 제공",
    "자원 관리", "기술 발전", "해양수산", "글로벌 인재 양성", "비교과 프로그램",
    "산업체 연계", "학문 융합"
];

var _SEARCH = {
		MID 			: "",					// 메뉴번호
		TOPIC			: "",					// 검색 키
};


$(function(){	 
	/*
	 * INIT ----------------------------------------------
	 */	
	// 페이지 초기화
	pageCommonInit();
	
	getAllTabCnt();
	
	animateSearchSuggestions();


});




//페이지 초기화
function pageCommonInit(){
	// Calendar
	page_prop.today = new Date();
	page_prop.date = new Date();
	page_prop.eventStr = [];
	page_prop.eventEnd = [];
	page_prop.data = "";
	page_prop.doMonth = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth(), 1);
	page_prop.lastDate = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth()+1, 0);
	
 	// 학사일정
 	setRangeMonth().then(buildCalendar);
 	
 	// 취업률
 	createChartEmployment($chartEMP);
 	
}




//월 범위 설정
function setRangeMonth(){
	return new Promise(function(resolve) {
		const lastDate = page_prop.lastDate;
	 	const y = lastDate.getFullYear();
	 	const m = (lastDate.getMonth() + 1) < 10 ? "0" + (lastDate.getMonth() + 1) : (lastDate.getMonth() + 1);
	 	const d = lastDate.getDate();
	
	 	$("#scheYmd").val(y+""+m);	
	 	
	 	page_prop.eventStr = [];
	 	page_prop.eventEnd = [];

	 	resolve(getMonthList(y, m, d, 'month'));
	});
}

//이전달 일정 Action (2월에 대한 예외처리 할 것)
function prevCalendar() {
	const getDate = (page_prop.today.getDate() == '31') ? page_prop.today.getDate() - 1 : page_prop.today.getDate();
	page_prop.today = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth() - 1, getDate);
	page_prop.doMonth = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth(), 1);
	page_prop.lastDate = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth()+1, 0);
	
	setRangeMonth().then(buildCalendar);
}

//다음달 일정 Action(2월에 대한 예외처리 할 것)
function nextCalendar() {
	const getDate = (page_prop.today.getDate() == '31') ? page_prop.today.getDate() - 1 : page_prop.today.getDate();
	page_prop.today = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth() + 1, getDate);
	page_prop.doMonth = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth(), 1);
	page_prop.lastDate = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth()+1, 0);
	
	setRangeMonth().then(buildCalendar);
}

//달력 생성
function buildCalendar(){	
	const $tbCalendar = document.getElementById("calendar");
	const $tbCalendarYM = document.getElementById("tbCalendarYM");
	const $scheYmd = document.getElementById("scheYmd");

	var calMon = (page_prop.today.getMonth() + 1) < 10 ? "0" + (page_prop.today.getMonth() + 1) : (page_prop.today.getMonth() + 1);

	$tbCalendarYM.innerHTML = '<h4>'+page_prop.today.getFullYear() + ". " + calMon;
	
	while ($tbCalendar.rows.length > 2) {
		$tbCalendar.deleteRow($tbCalendar.rows.length-1);
	}
	
	var row = $tbCalendar.insertRow()		
	var cnt = 0;		
	var scheMon = page_prop.today.getMonth() + 1;
	
	for(var i=0; i<page_prop.doMonth.getDay(); i++) {
		cell = row.insertCell();
		cnt = cnt + 1;
	}
	
	try{
		for(var i=1; i<=page_prop.lastDate.getDate(); i++) {
			cell = row.insertCell();
			cell.innerHTML = i;
			cnt = cnt + 1;
			cell.className = "date";
			
			if(page_prop.eventStr.indexOf(i) > -1 && page_prop.eventEnd.indexOf(i) > -1){
				cell.className += " sch_start sch_end";
				cell.innerHTML = i;
				
				var tooltip ="";
				$.each(page_prop.data, function(index, item){	
					if(item.sd == i && item.ed == i){
						if(tooltip == ""){
							tooltip = "⦁ " + item.title;	
						}else{
							tooltip += "<br/>⦁ "+item.title;
						}
					}else{
						if(item.sd == i && item.sm == scheMon) {
							if(tooltip == ""){
								tooltip = "⦁ " + item.title + " 시작";	
							}else{
								tooltip += "<br/>⦁ "+item.title + " 시작";
							}
						}
						
						if(item.ed == i && item.em == scheMon) {
							if(tooltip == ""){
								tooltip = "⦁ " + item.title + " 종료";	
							}else{
								tooltip += "<br/>⦁ "+item.title + " 종료";
							}
						}
					}
				})
				cell.setAttribute("data-bs-toggle", "tooltip");
				cell.setAttribute("data-bs-placement", "bottom");
				cell.setAttribute("data-bs-html", "true");
				cell.setAttribute("title", tooltip);
			}else if(page_prop.eventStr.indexOf(i) > -1){
				cell.className += " sch_start";
				cell.innerHTML = i;
				
				var tooltip ="";
				$.each(page_prop.data, function(index, item){					
					if(item.sd == i && item.ed == i){
						if(tooltip == ""){
							tooltip = "⦁ " + item.title;	
						}else{
							tooltip += "<br/>⦁ "+item.title;
						}
					}else{
						if(item.sd == i && item.sm == scheMon) {
							if(tooltip == ""){
								tooltip = "⦁ " + item.title + " 시작";	
							}else{
								tooltip += "<br/>⦁ "+item.title + " 시작";
							}
						}
					}
				})
				cell.setAttribute("data-bs-toggle", "tooltip");
				cell.setAttribute("data-bs-placement", "bottom");
				cell.setAttribute("data-bs-html", "true");
				cell.setAttribute("title", tooltip);
			}else if(page_prop.eventEnd.indexOf(i) > -1){
				cell.className += " sch_end";
				var tooltip ="";
				$.each(page_prop.data, function(index, item){
					if(item.sd == i && item.ed == i){
						if(tooltip == ""){
							tooltip = "⦁ " + item.title;	
						}else{
							tooltip += "<br/>⦁ "+item.title;
						}
					}else{
						if(item.ed == i && item.em == scheMon) {
							if(tooltip == ""){
								tooltip = "⦁ " + item.title + " 종료";	
							}else{
								tooltip += "<br/>⦁ "+item.title + " 종료";
							}
						}
					}
				})
				cell.setAttribute("data-bs-toggle", "tooltip");
				cell.setAttribute("data-bs-placement", "bottom");
				cell.setAttribute("data-bs-html", "true");
				cell.setAttribute("title", tooltip);
			}
			
			if (cnt % 7 == 1) {
				cell.className += " red";
				cell.innerHTML = i;
			}
			
			if (cnt % 7 == 0){
				cell.innerHTML = i;
				row = calendar.insertRow();
			}
		}
	}catch(error){
		asdf(error);
	}

	for (i=1; i<=6; i++) {
		if(cnt%7 != 0){
			cell = row.insertCell();
			cnt = cnt + 1;	 
		}else break;
	};

	
	$(document.body).tooltip({
		selector: "[data-bs-toggle='tooltip']"
	});
}


//일정 시작일 관리
function getEventStr(date, tit){
	page_prop.eventStr.push(date);
	return page_prop.eventStr;
}

//일정 종료일 관리
function getEventEnd(date, tit){
	page_prop.eventEnd.push(date);
	return page_prop.eventEnd;
}

//월별 일정 리스트 생성
function getMonthList(year, month, day, action){
	return new Promise(function(resolve, reject){
		
		var month = (page_prop.today.getMonth() + 1) < 10 ? "0" + (page_prop.today.getMonth() + 1) : (page_prop.today.getMonth() + 1);
		
		$.ajax({
			type:'POST', 
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			url: '/web/main/aiRecommendCalendar.json?mId=1',
			data: {BASE_YMD : page_prop.today.getFullYear() + String(month)},
			success:function(result){
				const data = result.CALENDAR;
				page_prop.data = [];
	
				var html ="";
				$.each(data, function(index, item){
					const title = item.SCDUL_NM
					const sFullDate = to_date(item.BEGIN_DTM);
					const eFullDate = to_date(item.END_DTM);
					
					const sMonth = sFullDate.getMonth()+1;
					const sDate = sFullDate.getDate();
					const eMonth = eFullDate.getMonth()+1;
					const eDate = eFullDate.getDate();

					if(sMonth+sDate != eMonth+eDate){					
						if(eMonth == month) getEventEnd(eDate, title);
	
						html 	+= '<dl class="school_sch d-flex flex-row align-items-start gap-2">'
		                    	+'		<dt class="d-flex flex-row gap-3"><span>'+sMonth+'.'+sDate+'</span><span>'+eMonth+'.'+eDate+'</span></dt>'
		                    	+'		<dd class="text-truncate">'+title+'</dd>'
		                		+'	</dl>';
					}else{
						html 	+= '<dl class="school_sch d-flex flex-row align-items-start gap-2">'
		                    	+'		<dt class="d-flex flex-row gap-3"><span>'+sMonth+'.'+sDate+'</span></dt>'
		                    	+'		<dd class="text-truncate">'+title+'</dd>'
		                		+'	</dl>';
					}
					if(sMonth == month) getEventStr(sDate, title);		
					
					var objSch = {};
					objSch.sm = sMonth;
					objSch.em = eMonth;
					objSch.sd = sDate;
					objSch.ed = eDate;
					objSch.title = title;
					page_prop.data.push(objSch);
				});
				
				$("#shafScheList").empty();
				$("#shafScheList").html(html);

				resolve(true);
	
			}, error:function(error) { console.log(error); reject(); }
		});
	});
}


function to_date(date){
	const yyyyMMdd = String(date);
	const sYear = yyyyMMdd.substring(0,4);
	const sMonth = yyyyMMdd.substring(4,6);
	const sDate = yyyyMMdd.substring(6,8);
	
 return new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
}

//전체 탭 카운트
function getAllTabCnt(){
	$.ajax({
		type:'POST', 
		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
		contentType:'application/json',
		url: '/web/studPlan/getStudPlanAllTabCnt.do?mId=36',
		success:function(data){			
			console.log(JSON.stringify(data.ALL_TAB_CNT));
			renderTabCnt(data.ALL_TAB_CNT);
		}
	});
}

//탭 카운트 rendering
function renderTabCnt(data){
	data.forEach(function(item){
		$("#"+item.TYPE+"Cnt").text(item.COUNT)
	});
}


//선택한 탭 리스트	
function getTab(e){
	$.ajax({
		type:'POST', 
		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
		contentType:'application/json',
		url: '/web/studPlan/getStudPlanTabList.do?mId=36',
		data: JSON.stringify({TAB : e}),
		success:function(data){			
			$(".cnsltTab").removeClass("active");
			$("#"+e+"Tab").addClass("active");
			renderTabList(data.TAB_LIST, e)			
		}
	});
}


//탭 리스트 rendering
function renderTabList(data, e){
	$("#cnsltTabList").empty();
	var html = '';
	var aTagHtml = '';
	if(data.length > 0){
		
//         aTagHtml += '<a href="/web/studPlan/profView.do?mId=45&docId=' + ra.SDM_CD + '&no=' + ra.REVSN_NO + '&comYn=' + ra.CNSLT_CMPTL_YN + '&judgYn=' + ra.JUDG_CMPTL_YN + '" title="심사" class="writ_consult w-100">심사</a>';
        
		data.forEach(function(ra) {
			itemClass = ''
			aTagHtml = '<div class="btn_wrap d-flex flex-row gap-2">';			
			switch(e){
			case 'RA':
				if(ra.STATUS == '30'){
						itemClass = 'on_approve';
      					aTagHtml += '<a href="goModify(\'Approve\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="요청승인" class="writ_consult w-100">요청승인</a>';							
				}else if(Number(ra.STATUS) > 33){
						itemClass = 'approve';
      					aTagHtml += '<a href="goView(\'Approve\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상세보기" class="compl_consult w-100">상세보기</a>';							
				}else if(ra.STATUS == '33'){
						itemClass = 'reject';
      					aTagHtml += '<a href="goView(\'Approve\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상세보기" class="compl_consult w-100">상세보기</a>';														
				};
				break;				
			case 'RC':
				itemClass = 'on_consult';
				aTagHtml += '<a href="#" onclick="goModify(\'Cnslt\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상담작성" class="writ_consult w-100">상담작성</a>';	 break;
			case 'AC':
				itemClass = 'on_consult';
				aTagHtml += '<a href="#" onclick="cnsltReceive(\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\')" title="상담접수" class="writ_consult w-100">상담접수</a>';	 break;
			case 'WC':
				itemClass = 'on_consult';
				 if (ra.CNSLT_PROF.indexOf("${loginVO.memberId}") !== -1) {
					 aTagHtml += '<a href="#" onclick="goModify(\'Cnslt\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상담작성" class="writ_consult w-100">상담작성</a>';
			    } else{
					 aTagHtml += '<a href="#" onclick="goModify(\'Cnslt\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상담작성" class="writ_consult">상담작성</a>';
					 aTagHtml += '<a href="#" onclick="cnsltCancel(\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\')" title="접수취소" class="cancel_regi">접수취소</a>';				    	
			    }
				break;
				
			case 'CC':
				itemClass = 'final_consult';
				aTagHtml += '<a href="#" onclick="goView(\'Cnslt\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상세보기" class="compl_consult w-100">상세보기</a>';	 break;
			case 'WJ':
				itemClass = 'on_judge';
				aTagHtml += '<a href="#" onclick="goModify(\'Judge\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="심사" class="writ_consult w-100">심사</a>';	 break;
			case 'CJ':
				itemClass = 'final_judge';
				aTagHtml += '<a href="#" onclick="goView(\'Judge\',\''+ra.SDM_CD+'\',\''+ra.REVSN_NO+'\',\''+ra.APLY_FG+'\')" title="상세보기" class="compl_consult w-100">상세보기</a>';	 break;
			}
			aTagHtml += '</div>';
			
			
	        html += '<div class="item ' + itemClass + ' p-3">' +
	        '<div class="title_wrap d-flex flex-row mb-3 justify-content-between">' +
	            '<ul>' +
	                '<li class="title text-truncate">' + ra.SDM_KOR_NM + '</li>' +
	                '<li>' +
	                    '<ul class="cate d-flex gap-2 flex-wrap flex-sm-nowrap">' +
	                        '<li class="color_border">' +
	                            '<span>' + ra.FIRST_MAJOR + '</span>' +
	                            '<span>' + ra.OTHER_MAJOR + '</span>' +
	                        '</li>' +
	                    '</ul>' +
	                '</li>' +
	            '</ul>';
	          
	         html += aTagHtml;
	         
	         html += '</div>' +
                '<div class="info_wrap d-flex flex-wrap p-3 gap-2">' +
                    '<dl class="d-flex flex-row gap-2">' +
                        '<dt>신청학생</dt>' +
                        '<dd>' + ra.APLY_STUDENT_NM + '</dd>' +
                    '</dl>' +
                    '<dl class="d-flex flex-row gap-2">' +
                        '<dt>수여학위</dt>' +
                        '<dd>' + ra.AWD_DEGR_KOR_NM + '</dd>' +
                    '</dl>' +
                    '<br>' +
                    '<dl class="d-flex flex-row gap-2">' +
                        '<dt>설계학점</dt>' +
                        '<dd>' + ra.TOTAL_CDT_NUM + '학점</dd>' +
                    '</dl>' +
                    '<dl class="d-flex flex-row gap-2">' +
                        '<dt style="min-width: 80px;">교과목설계범위</dt>' +
                        '<dd>' + ra.SBJT_DGN_RNG_FG_NM + '</dd>' +
                    '</dl>' +
                    '<dl class="d-flex flex-row gap-2">' +
                        '<dt>신청유형</dt>' +
                        '<dd>'+(ra.APLY_FG === 'NEW' ? '신설' : '변경')+'</dd>' +
                    '</dl>' +
                '</div>' +
            '</div>';	         
		});		
	}else{
		html += '<div class="pf_no_planWrap d-flex flex-column justify-content-center align-items-center">' +
				'<p>정보가 없습니다.</p>' +
				'<a href="/web/studPlan/profList.do?mId=36" title="학생설계전공전체보기" class="text-end view_more px-3 py-2 mt-2 rounded-pill">학생설계전공 보러가기</a>' + 
				'</div>';
	}
	$("#cnsltTabList").html(html);
}

//수정화면
function goModify(fg, sdmCd, revsnNo, aplyFg){
	var isChg = '';
	if(aplyFg == 'CHG'){
		isChg = 'Chg';
	}
	var form = document.frmView;
	$("#SDM_CD").val(sdmCd);
	$("#REVSN_NO").val(revsnNo);
	form.action = '/web/studPlan/prof'+fg+isChg+'Modify.do';
	form.submit();
}

//상세보기화면
function goView(fg, sdmCd, revsnNo, aplyFg){
	var isChg = '';
	if(aplyFg == 'CHG'){
		isChg = 'Chg';
	}	
	var form = document.frmView;
	$("#SDM_CD").val(sdmCd);
	$("#REVSN_NO").val(revsnNo);
	form.action = '/web/studPlan/prof'+fg+isChg+'View.do';
	form.submit();
}

//상담접수
function cnsltReceive(sdmCd, revsnNo){
	if(confirm("접수하시겠습니까?")){	
		$.ajax({
			type:'POST', 
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			url: '/web/studPlan/profCnsltReceive.do?mId=36',
			data: JSON.stringify({
				SDM_CD : sdmCd,
				REVSN_NO : revsnNo
			}),
			success:function(data){	
				alert(data.RTN_MSG)
				if(data.RESULT == 'DONE'){
					getTab('AC');
					getAllTabCnt();				
				}else{
					console.log(data.RESULT)
				}
			}
		});
	}
}

//상담접수취소
function cnsltCancel(sdmCd, revsnNo){
	if(confirm("접수취소하시겠습니까?")){	
		$.ajax({
			type:'POST', 
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			url: '/web/studPlan/profCnsltCancel.do?mId=36',
			data: JSON.stringify({
				SDM_CD : sdmCd,
				REVSN_NO : revsnNo
			}),
			success:function(data){	
				alert(data.RTN_MSG)
				if(data.RESULT == 'DONE'){
					getTab('WC');
					getAllTabCnt();				
				}else{
					console.log(data.RESULT)
				}
			}
		});
	}
}
	
//취업률(바 차트)
function createChartEmployment(ctx){
	var valueList 	= [],
		labelList 	= [];
	
	"<c:forEach items="${UNIV_EMPLOYMENT_RATE}" var="item">"
	labelList.push("${item.COLL_NM}");
	valueList.push("${item.COLL_CNT}");
	"</c:forEach>"
	
	if(valueList.length < 1) {
		$('#'+ ctx).addClass('hidden');
		$('#'+ ctx).next().removeClass('hidden');
		return;
	}
	console.log(valueList)
	const data = {
		labels : labelList,
		datasets: [{
			data : valueList,
			label : labelList,
			backgroundColor : ["#6794DC","#6771DC","#8067DC","#A367DC","#C767DC","#DC67AB","#C367DC","#A167DC","#8A6EDC","#6F86D4","#6794DC"], 
 			borderWidth : 0.5,
		}]	
	};
	
	const opts = {
		responsive:true,
		maintainAspectRatio: false,
 		cutoutPercentage: 70,
		reverse: false,
		borderSkipped: false,
		legend : { display: false, position:'bottom' },	
		scales : {
			xAxes : [{ barPercentage: 0.5, gridLines : { color: "#ffffff00" }, }],
			yAxes : [{ gridLines : { color: "#ffffff00" }, ticks:{ beginAtZero: true, }, }]
		},
		tooltips: {
	        backgroundColor: "rgb(0,0,0,.6)",
	        bodyFontColor: "#fff",
	        titleMarginBottom: 10,
	        titleFontColor: '#fff',
	        titleFontSize: 14,
	        borderColor: '#fff',
	        borderWidth: 0,
	        xPadding: 15,
	        yPadding: 15,
	        displayColors: true,
	        intersect: false,
	        mode: 'single',
	        caretPadding: 5,
	        callbacks: {
	            label: function(tooltipItem, data) {
	            	console.log(labelList[tooltipItem.index] + " : " + comma(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]))
	            	return labelList[tooltipItem.index] + " : " + comma(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]);
	            }
	        }
	    },
	}
	
	try{
		page_prop.chartCdtDetail = new Chart(document.getElementById(ctx).getContext('2d'), {
			type : "bar",
			data : data,
			options : opts,
		});
	}
	catch(error){ asdf(error);}
	finally{ return; }
}

	
// 접수 버튼
function recBtn(a,b,c,d,e,f){
	if(confirm("접수하시겠습니까?")){
		$.ajax({
			url : '/web/studPlan/receiveProf.do?mId=45',
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			contentType:'application/json',
			type:'GET', 
			data: { 
				cd : a,
				no : b,
				tit : c,
				nm : d,
				cnt : e,
				sCd : f
			},
			success:function(data){
				alert("접수되었습니다.");
				location.reload();
				
			}
		});
	}		
}
	
// 접수 취소
function deleCnslt(e, f){
	if(confirm("접수 취소하시겠습니까?")){
		var form = document.frmCnslt;
		form.sdmCd.value = e;
		form.revsnNo.value = f;
		form.submit();			
	}
}

function popularSearch(a){
	var searchCondition = $("#searchCondition");
	searchCondition.empty();
	searchCondition.append("통합검색");
	$('input[name="main_search"]').val(a);
	document.mainSearchForm.submit();
}
 
function mainSearch(){
    if(SearchTextValidation('main_search')){
		_SEARCH.TOPIC	 	= $('input[name="main_search"]').val();
    	switch($("#searchCondition").text()){
    		case "전공·교양" :
    			_SEARCH.MID	 		= "32";
    			sessionStorage.setItem('sbjtSearchKeyword', JSON.stringify(_SEARCH));	
    			location.href = '/web/sbjt/list.do?mId=32';
    			break;
    		case "교수" : 
    			_SEARCH.MID	 		= "33";
    			_SEARCH.TOPIC	 	= $('input[name="main_search"]').val();
    			sessionStorage.setItem('profSearchKeyword', JSON.stringify(_SEARCH));	
    			location.href = '/web/prof/list.do?mId=33';
    			break;
    		case "비교과" :
    			_SEARCH.MID	 		= "34";
    			_SEARCH.TOPIC	 	= $('input[name="main_search"]').val();
    			sessionStorage.setItem('nonSbjtSearchKeyword', JSON.stringify(_SEARCH));	
    			location.href = '/web/nonSbjt/list.do?mId=34';
    			break;
    		case "전공" :
    			_SEARCH.MID	 		= "35";
    			_SEARCH.TOPIC	 	= $('input[name="main_search"]').val();
    			sessionStorage.setItem('majorSearchKeyword', JSON.stringify(_SEARCH));	
    			location.href = '/web/major/list.do?mId=35';
    			break;
    		case "통합검색" :
    	        document.mainSearchForm.submit();
    			break;
    	}
    }
    else{
        return false;
    }
}

function SearchTextValidation(searchBoxName){
    var searchValue = $('input[name="' + searchBoxName + '"]').val();
    if(searchValue === '' || searchValue === undefined){
        alert("검색어를 입력해주세요.");
        return false;
    }else{
        return true;            
    }
}

function schKey(e) {
    if(e.keyCode == 13) {
        mainSearch();
    }
}


function sCChange(type) {
    var searchCondition = $("#searchCondition");
    searchCondition.empty();
    
    switch(type) {
        case "all":
            searchCondition.append("통합검색");
            break;
        case "sbjt":
            searchCondition.append("전공·교양");
            break;
        case "nonSbjt":
            searchCondition.append("비교과");
            break;
        case "major":
            searchCondition.append("전공");
            break;
        case "prof":
            searchCondition.append("교수");
            break;
    }
    
    $("#listSc").attr("style", "display:none;");
    searchCondition.removeClass("rtt");
}

//추천 검색어 랜덤 15개 뽑기
function getRandomGroupedSuggestions() {
    // 랜덤으로 15개 선택
    let randomSuggestions = [];
    let tempSuggestions = [...suggestions];
    for (let i = 0; i < 15 && tempSuggestions.length > 0; i++) {
        let randomIndex = Math.floor(Math.random() * tempSuggestions.length);
        randomSuggestions.push(tempSuggestions.splice(randomIndex, 1)[0]);
    }
    
    // 5개씩 그룹화
    let groupedSuggestions = [];
    for (let i = 0; i < randomSuggestions.length; i += 5) {
        groupedSuggestions.push(randomSuggestions.slice(i, i + 5));
    }
    
    return groupedSuggestions;
}

//추천 검색어 애니메이션 함수
function animateSearchSuggestions() {
    var container = document.getElementById('searchSuggestions');
    var groupedSuggestions = getRandomGroupedSuggestions();
    var currentGroup = 0;

    function updateSuggestions() {
        var newGroup = document.createElement('div');
        newGroup.className = 'suggestion-group entering';
        
        for (var i = 0; i < groupedSuggestions[currentGroup].length; i++) {
            var a = document.createElement('a');
            a.href = 'javascript:void(0);';
            a.onclick = function(suggestion) {
                return function() { popularSearch(suggestion); };
            }(groupedSuggestions[currentGroup][i]);
            a.textContent = groupedSuggestions[currentGroup][i];
            newGroup.appendChild(a);
        }
        
        container.appendChild(newGroup);
        
        setTimeout(function() {
            if (container.children.length > 1) {
                container.children[0].classList.add('leaving');
                newGroup.classList.remove('entering');
                
                setTimeout(function() {
                    container.removeChild(container.children[0]);
                }, 500);
            } else {
                newGroup.classList.remove('entering');
            }
        }, 50);

        currentGroup = (currentGroup + 1) % groupedSuggestions.length;
    }

    updateSuggestions();
    setInterval(updateSuggestions, 3000);
}

 window.onload = function(){
	$(document.body).tooltip({
		selector: "[data-bs-toggle='tooltip']"
	});
 }
 

</script>