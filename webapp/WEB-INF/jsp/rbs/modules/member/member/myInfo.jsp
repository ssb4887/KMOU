<%@ include file="../../../include/commonTop.jsp"%>

<%-- <script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-1.9.1.min.js"/>"></script> --%>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tabulator.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/xlsx.full.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/Chart.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/chartjs-plugin-doughnutlabel.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/chartjs-plugin-datalabels.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/all.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/moment.js"/>"></script>

<script type="text/javascript">
/* ====================================================================================
 *
 * Declare
 */
var page_prop = {};

// Elements
const $chartGPA = 'chartGPA';
const $chartCDT = 'chartCDT';
const $chartCdtDetail = 'chartCdtDetail';
const $chartReqCDT = 'chartReqCDT';
const $tableReqCDT  = '#tableReqCDT';
const $tableCumCDT = '#tableCumCDT';
const $tableSubjectCDT = '#tableSubjectCDT';
const $tableRecHist = '#tableRecHist';
const $tableGradReq = '#tableGradReq';
const $tableMajorReq 	= '#tableMajorReq';
const $tableMinoreq 	= '#tableMinorReq';
const $tableNonSbjtHist 	= '#tableNonSbjtHist';
const $tabTarget = "<c:out value="${TARGET}"/>";


// URL
const URL_HASHTAG_PROC = "<c:out value="${URL_HASHTAG_PROC}"/>";

// Object
const _USER = {
	NAME 			: "<c:out value="${USER.NM}"/>",			// 이름
	E_NAME			: "<c:out value="${USER.ENM}"/>",			// 영문이름
	STD_NO 			: "<c:out value="${USER.STUDENT_NO}"/>",	// 학번
	DEPT_NM 		: "<c:out value="${USER.DEPT_NM}"/>",		// 학부(과)
	GRADE 			: "<c:out value="${USER.GRADE}"/>",			// 학년
	ISU_SMT			: "<c:out value="${USER.ISU_SMT}"/>",		// 이수 학기
	HAKJUK_NM 		: "<c:out value="${USER.HAKJUK_ST_NM}"/>",	// 학적상태
	MAJOR_NM 		: "<c:out value="${USER.MAJOR_NM}"/>",		// 전공
	BU_NM			: "<c:out value="${USER.BU_NM}"/>",			// 부전공
	BOK_NM			: "<c:out value="${USER.BOK_NM}"/>",		// 복수전공
	SUF_STS_NM  	: "<c:out value="${USER.SUF_STS_NM}"/>",	// 연게전공(학부생 - 그외 '전일제/시간제')
	PRSBJ_CDT   	: "<c:out value="${USER.PRSBJ_CDT}"/>",		// 융합전공(학부생 - 그외 '선수과목이수학점')
};

const _CDT = {
	STD_NO 			: "<c:out value="${USER_CDT.STUDENT_NO}"/>",
	CUM_CDT 		: "<c:out value="${USER_CDT.CUM_CDT}"/>",
	GRADUATE_CDT 	: ("<c:out value="${USER_GOAL_CDT[0].GRADUATE_CDT}"/>" != '0') ? "<c:out value="${USER_GOAL_CDT[0].GRADUATE_CDT}"/>" : "<c:out value="${USER_GOAL_CDT[1].GRADUATE_CDT}"/>"
};

const _GPA = {
	STD_NO 			: "<c:out value="${USER_GPA[0].STUDENT_NO}"/>",
	GPA_AVG 		: "<c:out value="${USER_GPA[0].GPA_AVG}"/>",
	SCR_AVG 		: "<c:out value="${USER_GPA[0].TOTAL_PERCENT}"/>"
};

const _SUKANG_LOGIN = {
	TRY_TIME		: "<c:out value="${sukangLoginVO.tryTime}"/>",	
	ERR_CODE		: "<c:out value="${sukangLoginVO.errCode}"/>",	
	ERR_MSG			: "<c:out value="${sukangLoginVO.errMessage}"/>",	
	PRE_APPL_DTTM	: "<c:out value="${sukangLoginVO.preApplDttm}"/>"
}


//장바구니
var basketList = {};
//로그인 프로시저 리턴 데이터
var sukangData = {};

$(function(){
	/*
	 * DECLARE ----------------------------------------------
	 */
	const $hashInput = $('#hashInput');
	const $hashRegi = $('#hashRegi');
	const $hashDelete = $('button.hashDelete');
	
	
	/*
	 * FUNCTION ----------------------------------------------
	 */	 
	// 페이지 초기화
 	pageCommonInit();
	
	
	/* 
     * EVENT ----------------------------------------------
     */     
    // 해시태그 등록(Enter Key)
	$hashInput.keydown(function(key){
		if(key.keyCode == 13){
			$hashRegi.trigger('click');
		}
	});
	
	// 해시태그 등록(Click)
	$hashRegi.on('click', function(){
		if(!$hashInput.val()) return false;
		
		const $hashtagList = $("#hashtagList");
		const hashtag = $hashInput.val();
		
		var tagList = [];
		$('.hashtag').each(function(idx, item){
			tagList.push($('.hashtag').eq(idx).text());
		});

		if(tagList.indexOf(hashtag) != -1) return alert("이미 등록된 키워드입니다.");
		
		try{
			$.ajax({
				type: 'POST'
				, beforeSend: function(request){ request.setRequestHeader('Ajax', 'true') }
				, dataType: 'json'
				, url: URL_HASHTAG_PROC + "&HASHTAG=" + hashtag + "&FLAG_DEL=N"
				, async: true
				, success: function(result){
					var $elem = '<li class="rounded-pill bg-white  d-flex flex-row align-items-center">#<span class="hashtag">' 
								+ hashtag
								+ '</span><button type="button" class="border-0 rounded-circle hashDelete"></button></li>';
			
					$hashtagList.append($elem);
					
					$hashInput.val('').focus(); // 키워드 등록 후, 입력란 공백 및 포커스
				}, error: function(request, error){ asdf(error) }
			});
		}catch(e){
			return alert(e);
		}
	});
	
	// 해시태그 삭제(Click)
	$(document).on('click', '.hashDelete', function(){
		const hashtag = $(this).prev().text();
		const $elemTag = $(this).closest('li');
		
		try{
			$.ajax({
				type: 'POST'
				, beforeSend: function(request){ request.setRequestHeader('Ajax', 'true') }
				, dataType: 'json'
				, url: URL_HASHTAG_PROC + "&HASHTAG=" + hashtag + "&FLAG_DEL=Y"
				, async: true
				, success: function(result){
					$elemTag.remove();
				}, error: function(request, error){ 
					asdf(error); 
				}
			});
		}catch(e){
			return alert(e);
		}
	});
	
	
	$("#sbjtTab").on('click', function(){
		$(".bookmarkTab").removeClass("active");
		$("#sbjtTab").addClass("active");	
		//교과목(전공·교양)
		getSbjtList(1);
	});
	
	$("#lecTab").on('click', function(){
		$(".bookmarkTab").removeClass("active");
		$("#lecTab").addClass("active");
		getLecList(1);

	});
	
	$("#nonSbjtTab").on('click', function(){
		$(".bookmarkTab").removeClass("active");
		$("#nonSbjtTab").addClass("active");
		getNonSbjtList(1);
	});
		
	$("#profTab").on('click', function(){
		$(".bookmarkTab").removeClass("active");
		$("#profTab").addClass("active");
		getProfList(1);
	});
	
	$("#majorTab").on('click', function(){
		$(".bookmarkTab").removeClass("active");
		$("#majorTab").addClass("active");
		getMajorList(1);
	});
	
	$("#studPlanTab").on('click', function(){
		$(".bookmarkTab").removeClass("active");
		$("#studPlanTab").addClass("active");
		getStudPlanList(1);
	});
	
	// 나의 찜하기 선택 삭제
	$("#bmk_all_check").on('click', function(){
		bmkChkDel();
	});
	
	// 나의 찜하기 전체 삭제
	$("#bmk_all_remove").on('click', function(){
		bmkAllDel();
	});
	
	var allChecked = false;
	$(".all_check").on('click', function(){
		allChecked = !allChecked;
		if(allChecked){
			$("input[name='bmkChk']").prop("checked", true);	
		} else{
			$("input[name='bmkChk']").prop("checked", false);
		}
				
	});
		 
	// 장바구니 신청목록 선택 삭제
	$("#cartSelectedDel").click(function () {
		if($("input[type='checkbox'][name='cartChk']:checked").length == 0){
			alert("장바구니에서 삭제할 강의를 선택해 주세요.");
			return false;
		}
		
		var confm = confirm("선택한 강의를 장바구니에서 삭제하시겠습니까?");
		if(!confm) return false;
		
		var deleteList = [];
		$("input[type='checkbox'][name='cartChk']:checked").each(function() {
			var year = $(this).attr("data-year");
			var smt = $(this).attr("data-smt");
			var subjectCd = $(this).attr("data-subjectCd");
			var deptCd = $(this).attr("data-deptCd");
			var divCls = $(this).attr("data-divCls");
			var empNo = $(this).attr("data-empNo");
			var deleteInfo = year + "-" + smt + "-" + subjectCd + "-" + deptCd + "-" + divCls + "-" +empNo;
			
			deleteList.push(deleteInfo);
		});
		
		cartSelectedDel(deleteList);
	});
	
	// 예비수강 신청목록 검색 조건 변경
	$("#hisYear, #hisSemi").change(function () {
		getBasketCart();
	});
	
	// 예비수강 신청목록 시간표 보기
	$("#showTimeTable").click(function () {
		if($("input[type='checkbox'][name='cartChk']:checked").length == 0){
// 			$("#timeTableBody").empty();	
			alert("과목을 선택해주세요.")
			$("#isMerged").hide();
			return false;
		}
		
	    var cartList = [];
	    $("input[type='checkbox'][name='cartChk']:checked").each(function() {
	        var roomTime = $(this).attr("data-roomTime");
	        var subjectNm = $(this).attr("data-subjectNm");
	        cartList.push({ roomTime: roomTime, subjectNm: subjectNm });
	    });
		$("#timeTable").modal("show");
	    setTimeTable(cartList);
	});
	
	// 선택 일괄담기 button
	$("#btnAddSbjt").on('click', function(){
		sbjtContain();
	});
	
	// 선택 일괄담기 button
	$("#btnDelSbjt").on('click', function(){
		delSbjt();
	});
	
	// 전체선택/선택 일괄해제 button
	$(".btn-fg").on('click', function(){
		if($(this).val() == 'beforeChk'){
			$(".chk-before").prop("checked", true);
		}else if($(this).val() == 'beforeClear'){
			$(".chk-before").prop("checked", false);
		}else if($(this).val() == 'afterChk'){
			$(".chk-after").prop("checked", true);
		}else{
			$(".chk-after").prop("checked", false);
		}
	});
	
	$('#showApplTimeTable').click(function(){
		if(_SUKANG_LOGIN.ERR_CODE === 'N'){
			alert('['+_SUKANG_LOGIN.ERR_MSG + ']\n' + '예비수강신청 시작일시가 지났을 경우 로그아웃 후 다시 로그인해주세요. \n\n' +
					'예비수강신청 시작 일시\n'+ '- '+ _SUKANG_LOGIN.PRE_APPL_DTTM + '\n\n' +
					'회원님의 로그인 일시\n' + '- '+ _SUKANG_LOGIN.TRY_TIME)
			return false;
		}else{
			//예비수강신청 나의 장바구니
			renderBeforeCart();
			
			//예비수강신청 현황
			renderApplList();	
		    $('#applTimeTable').modal('show');	    					
		}
	})
	
	$('#applTimeTableClose').click(function() {
	    getBasketCart();
	});
	
	//순서변경 모드 활성화 버튼
	$('#reorderBtn').click(function(){
		$('.appl').addClass('hidden');
		$('#appndSbjt').empty();
		var html = '';
		html += '<div class="lesson_wrap no_contents_wrap d-flex justify-content-center align-items-center reorder">' +
				'<div class="d-flex flex-column justify-content-center align-items-center">' +
				'<img src="../images/kmou_noshad_big.png" alt="해양이" />' + 
			    '<p class="text-center mt-2">' + 
			    	'순서변경중...' + 
			    '</p>' + 
			    '</div>' + 
				'</div>';
		$('#appndSbjt').append(html)
		$('.reorder').removeClass('hidden');
		adjustLayout();
	});
	
	//순서변경 취소
	$('#cancelOrder').click(function(){
		$('.appl').removeClass('hidden');
		$('.reorder').addClass('hidden');
		renderBeforeCart();
		renderApplList();	
		adjustLayout();
	});
	
	//순서저장
	$('#saveOrder').click(function(){
		if(confirm('변경된 순서로 저장하시겠습니까?')){
			saveNewOrder();
		}		
	});
	
    
    $("input[name=pwdInput]").on("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            sukangLogin();
            
        }
    });


});


/* ====================================================================================
 *
 * Function
 */
 
//페이지 초기화
function pageCommonInit(){
	// 기본정보
	obj2elem(_USER);
	
	// 나의 취득학점
	createChartCDT(_CDT);
	
	// 나의 평균성적
	createChartGPA(_GPA);
	
	// 학점 상세
	createChartCdtDetail();
	
	// 소요학점조회(테이블)
	createTableReqCDT($tableReqCDT);
	
	// 소요학점조회(차트)
	createChartReqCDT();
	
	// 학기별성적조회
	createTableCumCDT($tableCumCDT);
	
	// 졸업인증자격
	createTableGradReq($tableGradReq);
	
	// 과목별성적조회 
	createTableSubjectCDT($tableSubjectCDT);
	
	// 학적변동내역
	createTableRecordHistory($tableRecHist);
	
	// 전공필수 이수현황
	createTableMajorReq($tableMajorReq);
	
	// 교양필수 이수현황
	createTableMinorReq($tableMinoreq);
	
	// 비교과 신청이력
	createTableNonSbjtHist($tableNonSbjtHist);
	
	// 해시태그
	setListHashtag();
	
	// 탭 이동(메인에서 클릭한 페이지 - 학생설계전공, 해시태그, 나이찜, 장바구니)
	$($tabTarget).trigger('click');
	
	// 장바구니 탭
    var yearOptions = '';
    var currentYear = new Date().getFullYear();
    for (var i = currentYear; i > currentYear-5; i--) {
        yearOptions += '<option value="' + i + '"' + '>' + i + '</option>';
    }
    $('#hisYear').append(yearOptions);
	getBasketCart();
	
	//찜 항목별 카운트
	getMyBookmarkCount();
	hideLoading();
	
	//교과목(전공·교양)	
	$(".bookmarkTab").removeClass("active");
	$("#sbjtTab").addClass("active");	
	getSbjtList(1);
	
// 	setTimeout(function(){		
// 		//예비수강신청 나의 장바구니
// 		renderBeforeCart();
		
// 		//예비수강신청 현황
// 		renderApplList();	
// 	},1000)
}
 
// 기본정보
function obj2elem(object){
	const deptNM 	= object.DEPT_NM;
	const majorNM 	= object.MAJOR_NM;
	const buNM		= object.BU_NM;
	const bokNM		= object.BOK_NM;
	const fuseNM    = object.PRSBJ_CDT;
	const linkNM	= object.SUF_STS_NM;
	const grade 	= object.GRADE;
	const hakjukNM 	= object.HAKJUK_NM;
	const name 		= object.NAME;
	const eName		= object.E_NAME;
	const stdNO 	= object.STD_NO;
	const isuSmt 	= Number(object.ISU_SMT) + 1;
	
	const $major 	= deptNM + '<span class="d-inline-block ms-1">' + majorNM + '</span>';
	const $sub 		= '<span class="d-inline-block ms-1">' + ((!isNullOrBlank(buNM)) ? buNM : (!isNullOrBlank(bokNM)) ? bokNM : '-') + '</span>';
	const $fuseLink	= '<span class="d-inline-block ms-1">' + ((!isNullOrBlank(fuseNM)) ? fuseNM : (!isNullOrBlank(linkNM)) ? linkNM : '-') + '</span>';
	const $grade	= '<li class="sch_y"><span>' + grade + '학년</span> / <span>' + ((hakjukNM == "졸업" || hakjukNM == "휴학") ? hakjukNM : isuSmt + '학기') + '</span></li>';

	// 우측 사이드 카드 > 복수/부전공
	if(isNullOrBlank(buNM) && isNullOrBlank(bokNM)){
		$('#mySub').closest('dl').addClass('hidden');
	}else{
		$('#mySub').html($sub);				
	}
	
	// 우측 사이드 카드 > 융합/연계전공
	if(isNullOrBlank(fuseNM) && isNullOrBlank(linkNM)){
		$('#myFuseLink').closest('dl').addClass('hidden');
	}else{
		$('#myFuseLink').html($fuseLink);				
	}
	
	$('#myInfoName').text(name);			// 이름
	$('#myInfoEName').text(eName);			// 영문이름
	$('#myInfoStdNo').text(stdNO);			// 학번
	$('#myInfoGradeSmt').append($grade);	// 학년+(재학여부/현재학기)
	$('#myInfoDeptMajor').html($major);		// 메인 카드 > 학과+전공
	$('#myMajor').html($major);				// 우측 사이드 카드 > 학과+전공
}

// 나의 취득학점(도넛차트)
function createChartCDT(object){
    const cumCDT = Number(object.CUM_CDT);
    const goalCDT = Number(object.GRADUATE_CDT);
    
    // cumCDT가 goalCDT보다 크거나 같은 경우 처리
    const displayCDT = Math.max(cumCDT, goalCDT);
    const remainingCDT = Math.max(0, goalCDT - cumCDT);
    
    const data = {
        labels : ['취득','미취득'],
        datasets: [{
            data : [displayCDT, remainingCDT],
            backgroundColor : ["#0F3E8E", "#E6E9EF"], 
            borderWidth : 1,
        }]   
    };
   	
    const opts = {
        maintainAspectRatio: false,
        cutoutPercentage: 70,
        reverse: true,
        borderRadius : 20,
        legend : { display: false, position:'bottom' },
        plugins : {
            doughnutlabel : {
                labels: [{
                    text: cumCDT + ' /' + goalCDT,
                    font: { size: '15' },
                    color: '#0F3E8E'
                },{
                    text: '(학점)',
                    font: { size: '15' },
                    color: '#0F3E8E'
                }]
            }
        },
        tooltips: {
            backgroundColor: "rgb(0,0,0,.6)",
            bodyFontColor: "#fff",
            titleMarginBottom: 10,
            titleFontColor: '#fff',
            titleFontSize: 14,
            borderColor: '#dddfeb',
            borderWidth: 0,
            xPadding: 15,
            yPadding: 15,
            displayColors: true,
            intersect: false,
            mode: 'single',
            caretPadding: 5,
            callbacks: {
                label: function(tooltipItem, data) {
                    return "남은학점: " + remainingCDT;
                }
            }
        },
    }
    
    page_prop.chartCDT = new Chart(document.getElementById($chartCDT).getContext('2d'), {
        type : "doughnut",
        data : data,
        options : opts,
    });
}

// 나의 성적(도넛차트)
function createChartGPA(object){
	const gpaAvg = Math.floor((object.GPA_AVG) * 10) / 10; //평균 학점
	const scrAvg = object.SCR_AVG; //평균 점수
	const labelList = ['학점', '점수'];
	
	const data = {
	    labels: ['학점', '점수'],
	    datasets: [{
	        data: [gpaAvg, Math.floor((4.5 - gpaAvg) * 10) / 10],
	        backgroundColor: ["#0F3E8E", "#F6F9FF"],
	        borderWidth: 1,
	        label: '학점'
	    }, {
	        data: [scrAvg, Math.floor((100 - scrAvg) * 10) / 10],
	        backgroundColor: ["#81a3e6", "#F6F9FF"],
	        borderWidth: 1,
	        label: '점수'
	    }]
	};
	
	const opts = {
		maintainAspectRatio: false,
		cutoutPercentage: 70,
		reverse: true,
		borderRadius : 20,
		legend : { display: false, position:'bottom' },
		plugins : {
			doughnutlabel : {
				labels: [{
		    		text: gpaAvg + ' (학점)',
		    		font: { size: '15' },
		    		color: '#0F3E8E'
			    },{
			    	text: scrAvg + ' (점수)',
		    		font: { size: '15' },
		    		color: '#81a3e6'
			    }]
			}
		},		
		tooltips: {
	        backgroundColor: "rgb(0,0,0,.6)",
	        bodyFontColor: "#fff",
	        titleMarginBottom: 10,
	        titleFontColor: '#fff',
	        titleFontSize: 14,
	        borderColor: '#dddfeb',
	        borderWidth: 0,
	        xPadding: 15,
	        yPadding: 15,
	        displayColors: true,
	        intersect: false,
	        mode: 'single',
	        caretPadding: 5,
// 	        callbacks: {
// 	            label: function(tooltipItem, data) {
// 	                return " " + Number(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]).toFixed(1);
// 	            }
// 	        }
	    },
	}
	
	page_prop.chartGPA = new Chart(document.getElementById($chartGPA).getContext('2d'), {
		type : "doughnut",
		data : data,
		options : opts,
	});
}

// 학점 상세(바 차트)
function createChartCdtDetail(){
	var valueList = [],
		labelList = [];
	
	"<c:forEach items="${USER_CDT_DTL}" var="item">"
	labelList.push("${item.COMDIV_NM}");
	valueList.push("${item.CDT_CNT}");
	"</c:forEach>"

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
		reverse: true,
		borderRadius : 20,
		legend : { display: false, position:'bottom' },	
		scales : {
			xAxes : [{ barPercentage: 0.5, gridLines : { color: "#ffffff00" }, }],
			yAxes : [{ gridLines : { color: "#ffffff00" }, ticks:{ beginAtZero: true, max:4.5 }, }]
		},
		tooltips: {
	        backgroundColor: "rgb(0,0,0,.6)",
	        bodyFontColor: "#fff",
	        titleMarginBottom: 10,
	        titleFontColor: '#fff',
	        titleFontSize: 14,
	        borderColor: '#dddfeb',
	        borderWidth: 0,
	        xPadding: 15,
	        yPadding: 15,
	        displayColors: true,
	        intersect: false,
	        mode: 'single',
	        caretPadding: 5,
	        callbacks: {
	            label: function(tooltipItem, data) {
	            	return labelList[tooltipItem.index] + " : " + comma(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]);
	            }
	        }
	    },
        plugins: {
            noDataMessage: {
                message: '학점취득내역이 없습니다.'
            }
        }
	}
	
    // 'No data' 플러그인 정의
    Chart.plugins.register({
        id: 'noDataMessage',
        beforeDraw: function(chart) {
            if (chart.data.datasets[0].data.length === 0) {
                var ctx = chart.chart.ctx;
                var width = chart.chart.width;
                var height = chart.chart.height;

                chart.clear();

                ctx.save();
                ctx.textAlign = 'center';
                ctx.textBaseline = 'middle';
                ctx.font = "14px 'sans-serif'";
                ctx.fillStyle = '#212529';
                ctx.fillText(opts.plugins.noDataMessage.message, width / 2, height / 2.5);
                ctx.restore();

                return true;  // 차트 렌더링 중지
            }
        }
    });
	
	page_prop.chartCdtDetail = new Chart(document.getElementById($chartCdtDetail).getContext('2d'), {
		type : "bar",
		data : data,
		options : opts,
	});
}

//소요학점(차트)
function createChartReqCDT() {
    const labelList = [];
    const baseDataList = [];
    const doneDataList = [];
    const takingDataList = [];
    
    "<c:forEach items="${USER_REQ_CDT}" var="item">"
    labelList.push("${item.GUBUN}");
    if ("${item.GUBUN}" === '일반선택') {
        baseDataList.push(null);  // 일반선택의 경우 기준학점을 null로 설정
    } else {
        baseDataList.push(Number("${item.BASE_CDT}"));
    }
    doneDataList.push(Number("${item.DONE_CDT}"));
    takingDataList.push(Number("${item.TAKING_CDT}"));
    "</c:forEach>"
    
    const data = {
        labels: labelList,
        datasets: [
            {
                label: '기준학점(학번기준)',
                data: baseDataList,
                backgroundColor: "#4571e9"
            },
            {
                label: '수강 중인 학점',
                data: takingDataList,
                backgroundColor: "#0f3e83",
                stack: 'Stack 0'
            },
            {
                label: '취득학점',
                data: doneDataList,
                backgroundColor: "#5fbcd0",
                stack: 'Stack 0'
            }
        ]
    };
    const opts = {
        responsive: true,
        maintainAspectRatio: false,
        legend: { display: true, position: 'top', labels: { fontSize: 10, fontColor: "#979797" } },
        scales: {
            xAxes: [{ 
                stacked: true, 
                ticks: { fontSize: 10, fontColor: "#979797" },
                gridLines: { color: "#ffffff00" },
                barPercentage: 0.5 
            }],
            yAxes: [{ 
                stacked: true, 
                ticks: { 
                    fontSize: 10, 
                    fontColor: "#000000", 
                    fontStyle: 'bold',
                    beginAtZero: true 
                },
                gridLines: { color: "#ffffff00" } 
            }]
        },
        tooltips: {
            backgroundColor: "rgba(0, 0, 0, 0.6)",
            bodyFontColor: "#fff",
            titleMarginBottom: 10,
            titleFontColor: '#fff',
            titleFontSize: 14,
            borderColor: '#dddfeb',
            borderWidth: 0,
            xPadding: 15,
            yPadding: 15,
            displayColors: true,
            intersect: false,
            mode: 'index',
            caretPadding: 5,
            callbacks: {
                label: function(tooltipItem, data) {
                    const datasetLabel = data.datasets[tooltipItem.datasetIndex].label || '';
                    if (datasetLabel === '기준학점(학번기준)' && tooltipItem.yLabel === '일반선택') {
                        return datasetLabel + ': -';
                    }
                    return datasetLabel + ": " + tooltipItem.xLabel;
                }
            }
        },
        plugins: {
            datalabels: {
                color: 'white',
                display: function(context) {
                    return context.dataset.data[context.dataIndex] > 0;
                },
                font: {
                    weight: 'bold'
                },
                formatter: function(value, context) {
                    if (context.dataset.label === '기준학점(학번기준)' && context.chart.data.labels[context.dataIndex] === '일반선택') {
                        return '';
                    }
                    return value;
                }
            }
        }
    };
    page_prop.chartReqCDT = new Chart(document.getElementById('chartReqCDT').getContext('2d'), {
        type: 'horizontalBar',
        data: data,
        options: opts,
    });
}



// 소요학점(테이블)
function createTableReqCDT(ctx){
	const _DATASET = [];
	
	"<c:forEach items="${USER_REQ_CDT}" var="item">"
	var data = {};	
	
    data.GUBUN = "${item.GUBUN}";
    data.BASE_CDT = "${item.GUBUN}" === '일반선택' ? '-' : "${item.BASE_CDT}";
    data.DONE_CDT = "${item.DONE_CDT}";
    data.TAKING_CDT = "${item.TAKING_CDT}";
	
	_DATASET.push(data);
	"</c:forEach>"
	
	page_prop.tableReqCDT = new Tabulator(ctx, {
        layout			: "fitColumns",
        placeholder		: "There is no data viewed.",
        cellHozAlign	: 'center',
        cellVertAlign	: "middle",
        data			: _DATASET,
        index			: "GUBUN",
//         frozenRows		: 1,
        paginationSize	: 100,
        
        rowFormatter	: (row) => setRowStyle(row, '#FEFCE2'),

        columns			: [ 							
            { title		: "구분", 			field: "GUBUN", 			headerSort:false, width: 120 },
            { title		: "기준학점", 		field: "BASE_CDT", 			headerSort:false, },
            { title		: "이수학점", 		field: "DONE_CDT", 			headerSort:false, }, 
            { title		: "수강중학점", 	field: "TAKING_CDT", 		headerSort:false, },
        ],
        
        downloadConfig:{
            columnGroups:false,
            rowGroups:false,
            columnCalcs:false,
        },
        downloadReady: function(fileContents, blob){
            return blob;
        },
    });

    // Excel 다운로드 버튼 추가
    $('#tableGradReqExcel').append('<button type="button" id="excelDownloadBtnGradReq" class="btn btn-sm btn-primary">Excel 다운로드</button>');
    $('#excelDownloadBtnGradReq').click(function() {
        page_prop.tableGradReq.download("xlsx", "졸업인증내역.xlsx");
    });
	
	
}

//학기별성적(테이블)
function createTableCumCDT(ctx){
    const _DATASET = [];
    
    "<c:forEach items="${USER_CUM_CDT}" var="item">"
    var data = {};
    
    "<c:if test="${item.YEAR == null}">"
    data.YEAR_SMT = "전체";
    "</c:if>"
    
    "<c:if test="${item.YEAR != null}">"
    data.YEAR_SMT = "${item.YEAR} - ${item.SMT}";
    "</c:if>"
    
    data.STUDENT_NO = "${item.STUDENT_NO}";
    data.REQ_CDT = "${item.REQ_CDT}";
    data.GAIN_CDT = "${item.GAIN_CDT}";
    data.GPA_AVG = "${item.GPA_AVG}";
    data.TOTAL_PERCENT = "${item.TOTAL_PERCENT}";
    data.DEPT_RANK = "${item.DEPT_RANK}";
    
    _DATASET.push(data);
    "</c:forEach>"
    
    page_prop.tableCumCDT = new Tabulator(ctx, {
        layout          : "fitColumns",
        height          : "300px",
        placeholder     : "학기별 성적 정보가 없습니다.",
        cellHozAlign    : 'center',
        cellVertAlign   : "middle",
        data            : _DATASET,
        index           : "YEAR_SMT",
        paginationSize  : 100,
        
        rowFormatter    : (row) => setRowStyle(row, '#FEFCE2'),
        columns         : [                             
            { title     : "학년도/학기",    field: "YEAR_SMT",          headerSort:false, },
            { title     : "신청학점",       field: "REQ_CDT",           headerSort:true, },
            { title     : "취득학점",       field: "GAIN_CDT",          headerSort:true, }, 
            { title     : "평균학점",       field: "GPA_AVG",           headerSort:true, },
            { title     : "백분율",        field: "TOTAL_PERCENT",     headerSort:true, },
            { title     : "석차",          field: "DEPT_RANK",         headerSort:true, },
        ],

        // Excel 다운로드 설정 추가
        downloadConfig:{
            columnGroups:false,
            rowGroups:false,
            columnCalcs:false,
        },
        downloadReady: function(fileContents, blob){
            return blob;
        },
    });

    // Excel 다운로드 버튼 추가
    $('#tableCumCDTExcel').append('<button type="button" id="excelDownloadBtnCumCDT" class="btn btn-sm btn-primary">Excel 다운로드</button>');
    $('#excelDownloadBtnCumCDT').click(function() {
        page_prop.tableCumCDT.download("xlsx", "학기별성적.xlsx", {
            sheetName: "학기별성적"
        });
    });
}

// 행 스타일 설정 함수 (기존 코드에 없었다면 추가해주세요)
function setRowStyle(row, color) {
    if (row.getData().YEAR_SMT === "전체") {
        row.getElement().style.backgroundColor = color;
    }
}

// 졸업인증내역(테이블)
function createTableGradReq(ctx){
const _DATASET = [];
	
	"<c:forEach items="${USER_GRAD_REQ}" var="item">"
	
	var data = {};
	data.FIELD = "${item.FIELD}";
	data.DOMAIN = "${item.DOMAIN}";
	data.PASS_YN = "${item.PASS_YN}";
	data.ISU_DESC = "${item.ISU_DESC}";
	data.REQ_DT = "${item.REQ_DT}";	

	_DATASET.push(data);
	
	"</c:forEach>"
	
	page_prop.tableGradReq = new Tabulator(ctx, {
        layout			: "fitColumns",
		/*height			: "300px",*/
        placeholder		: "졸업인증 신청 내역이 없습니다.",
        cellHozAlign	: 'center',
        cellVertAlign	: "middle",
        data			: _DATASET,
        index			: "DOMAIN",
        paginationSize	: 100,

        columns			: [ 							
            { title		: "인증분야", 			field: "FIELD", 		headerSort:true, },
            { title		: "인증영역", 			field: "DOMAIN", 		headerSort:true, 	width:150},
            { title		: "인증여부", 			field: "PASS_YN", 		headerSort:false, 
								            	formatter:function(cell){ 
													return makePassBadge(cell.getData().PASS_YN);
												}, 	
            },
            { title		: "이수사항",	 		field: "ISU_DESC", 		headerSort:false, 	},
            { title		: "신청일자", 			field: "REQ_DT", 		headerSort:true, 	width:150},
        ],
       
    });
}

// 과목별성적(테이블)
function createTableSubjectCDT(ctx){
	const _DATASET = [];
	
	"<c:forEach items="${USER_SUBJECT_CDT}" var="item">"
	
	var data = {};
	data.YEAR_SMT = "${item.YEAR} - ${item.SMT_NM}";
	data.STUDENT_NO = "${item.STUDENT_NO}";
	data.SUBJECT_CD = "${item.SUBJECT_CD}";
	data.SUBJECT_NM = "${item.SUBJECT_NM}";
	data.MAJOR_NM = "${item.MAJOR_NM}";	
	data.DEPT_NM = "${item.DEPT_NM}";
	data.COMDIV_NM = "${item.COMDIV_NM}";
	data.CDT_NUM = "${item.CDT_NUM}";
	data.CONV_MAG = "${item.CONV_MAG}";
	data.SCR = "${item.SCR}";
	data.GPA = "${item.GPA}";
	data.CLASS_NM = "${item.CLASS_NM}";
	data.EMP_NO = "${item.EMP_NO}";
	data.EMP_NM = "${item.EMP_NM}";
	data.SUF_DEPT = "${item.SUF_DEPT}";
	data.SUF_DEPT_NM = "${item.SUF_DEPT_NM}";

	_DATASET.push(data);
	
	"</c:forEach>"
	
	page_prop.tableSubjectCDT = new Tabulator(ctx, {
        layout			: "fitColumns",
		height			: "300px",
        placeholder		: "과목별 성적 정보가 없습니다.",
        cellHozAlign	: 'center',
        cellVertAlign	: "middle",
        data			: _DATASET,
        index			: "YEAR_SMT",
//      	frozenRows		: 1,
        paginationSize	: 100,

        columns			: [ 							
            { title		: "편성", 			field: "YEAR_SMT", 			headerSort:true, },
            { title		: "과목", 			field: "SUBJECT_NM", 		headerSort:true, 	width:150},
            { title		: "구분",	 		field: "COMDIV_NM", 		headerSort:true, 
            								formatter:function(cell){ 
            									return makeComDivBadge(cell.getData().COMDIV_NM);
            								}, 	
            },
            { title		: "강좌구분", 		field: "CLASS_NM", 			headerSort:false, },
            { title		: "개설학부(과)", 	field: "SUF_DEPT_NM", 		headerSort:false, 	width:150},
            { title		: "교수", 			field: "EMP_NM", 			headerSort:false, },
            { title		: "학점", 			field: "CDT_NUM", 			headerSort:true, },
            { title		: "환산등급", 		field: "CONV_MAG", 			headerSort:true, },
            { title		: "점수", 			field: "SCR", 				headerSort:true, },
            { title		: "평점", 			field: "GPA", 				headerSort:true, },
        ],
        
        rowFormatter:function(row){
        	const convMag = ['C', 'C+', 'C-', 'D', 'D+', 'D-', 'F'];
        	var data = row.getData();
        	
        	if(convMag.indexOf(data.CONV_MAG) != -1){
        		row.getElement().style.backgroundColor = "#F0C80899";
            }
        },

        // Excel 다운로드 설정 추가
        downloadConfig:{
            columnGroups:false,
            rowGroups:false,
            columnCalcs:false,
        },
        downloadReady: function(fileContents, blob){
            return blob;
        },
    });

    // Excel 다운로드 버튼 추가
    $('#tableSubjectCDTExcel').before('<button type="button" id="excelDownloadBtnSubjectCDT" class="btn btn-sm btn-primary">Excel 다운로드</button>');
    $('#excelDownloadBtnSubjectCDT').click(function() {
        page_prop.tableSubjectCDT.download("xlsx", "과목별성적.xlsx", {
            sheetName: "과목별성적"
        });
    });
}

// 학적변동내역(테이블)
function createTableRecordHistory(ctx){
	const _DATASET = [];
	
	"<c:forEach items="${USER_RECORD_HISTORY}" var="item">"	
	var data = {};
	data.STUDENT_NO = "${item.STUDENT_NO}";
	data.YEAR_SMT = "${item.YEAR} - ${item.SMT}";
	data.CHG_NM = "${item.CHG_NM}";
	data.REQ_DT = moment("${item.REQ_DT}").format('YYYY-MM-DD');
	data.CHG_DT = moment("${item.CHG_DT}").format('YYYY-MM-DD');

	_DATASET.push(data);
	"</c:forEach>"
	
	page_prop.tablerRecordHistory = new Tabulator(ctx, {
        layout			: "fitColumns",
		height			: "150px",
        placeholder		: "학적변동 내역이 없습니다.",
        cellHozAlign	: 'center',
        cellVertAlign	: "middle",
        data			: _DATASET,
        index			: "YEAR_SMT",
        paginationSize	: 100,

        columns			: [ 							
            { title		: "학년도/학기", 	field: "YEAR_SMT", 		headerSort:false, },
            { title		: "학적변동내용", 	field: "CHG_NM", 		headerSort:false, },
            { title		: "신청일자", 		field: "REQ_DT", 		headerSort:true, },
            { title		: "변경일자", 		field: "CHG_DT", 		headerSort:true, },
        ],
        // Excel 다운로드 설정 추가
        downloadConfig:{
            columnGroups:false,
            rowGroups:false,
            columnCalcs:false,
        },
        downloadReady: function(fileContents, blob){
            return blob;
        },
    });

    // Excel 다운로드 버튼 추가
    $('#tableRecHistExcel').append('<button type="button" id="excelDownloadBtnRecHist" class="btn btn-sm btn-primary">Excel 다운로드</button>');
    $('#excelDownloadBtnRecHist').click(function() {
        page_prop.tablerRecordHistory.download("xlsx", "학적변동내역.xlsx");
    });
}

//전공필수 이수현황(테이블)
function createTableMajorReq(ctx){
	const _DATASET 	= [];
	
	"<c:forEach items="${USER_MAJOR_REQ}" var="item">"
	var data = {};
	data.S_FLAG = "${item.S_FLAG}";
	data.STUDENT_NO = "${item.STUDENT_NO}";
	data.GRADE_SMT = "${item.APY_GRADE} - ${item.APY_SMT_NM.substring(0, 1)}";
	data.COMDIV_NM = "${item.CURI_COMDIV_NM}";
	data.SUBJECT_NM = "${item.CURI_SUBJECT_NM}(${item.CURI_SUBJECT_CD})";
	data.CDT_NUM = "${item.CDT_NUM}";
	_DATASET.push(data);
	"</c:forEach>"
	
	
	try{
		page_prop.tableMajorReq = new Tabulator(ctx, {
	        layout			: "fitColumns",
			height			: "180px",
			placeholder		: "'개인별적용학점/교육과정' 이 <b>변경</b>되었습니다.</br>"
							  + "『종합정보시스템 > 미이수필수과목체크및확인』에서</br>"
							  + "<b>실행</b> 버튼을 눌러주세요. </br>"
							  + "<a class='go_tis px-2' href='https://tis.kmou.ac.kr/' title='종합정보시스템' target='_blank'>바로가기</a>",
	        cellHozAlign	: 'center',
	        cellVertAlign	: "middle",
	        data			: _DATASET,
	        index			: "GRADE_SMT",
	        paginationSize	: 100,
	
	        columns			: [ 							
	            { title		: "학년/학기", 		field: "GRADE_SMT", 	headerSort:false, 	width:80, },
	            { title		: "과목", 			field: "SUBJECT_NM", 	headerSort:false, },
	            { title		: "학점", 			field: "CDT_NUM", 		headerSort:false, 	width:80, },
	            { title		: "이수여부", 		field: "S_FLAG", 		headerSort:false, 	width:80,
	            								formatter:function(cell){ return makeCompleteBadge(cell.getData().S_FLAG);}, 
	            }, 
	        ],
	        downloadConfig:{
	            columnGroups:false,
	            rowGroups:false,
	            columnCalcs:false,
	        },
	        downloadReady: function(fileContents, blob){
	            return blob;
	        },
	    });

	    // Excel 다운로드 버튼 추가
	    $('#tableMajorReqExcel').append('<button type="button" id="excelDownloadBtnMajorReq" class="btn btn-sm btn-primary">Excel 다운로드</button>');
	    $('#excelDownloadBtnMajorReq').click(function() {
	        page_prop.tableMajorReq.download("xlsx", "전공필수이수현황.xlsx");
	    });
	}
	catch(error){ asdf(error);}
	finally{ return; }
}


// 교양필수 이수현황(테이블)
function createTableMinorReq(ctx){
	const _DATASET 		= []
		  , target		= [] ;
	
	"<c:forEach items="${USER_MINOR_REQ}" var="item">"
	"<c:if test="${not empty item.S_SUBJECT_NM}">"
	var data = {};
	data.S_FLAG = "${item.S_FLAG}";
	data.STUDENT_NO = "${item.STUDENT_NO}";
	data.GRADE_SMT = "${item.S_GRADE} - ${item.S_SMT_NM.substring(0, 1)}";
	data.SUBJECT_CD = "${item.S_SUBJECT_CD}";
	data.SUBJECT_NM = "${item.S_SUBJECT_NM}(${item.S_SUBJECT_CD})";
	data.CDT_NUM = "${item.S_CDT_NUM}";
	_DATASET.push(data);
	"</c:if>"
	"</c:forEach>"
	
	"<c:forEach items="${USER_CURR_MINOR}" var="item">"
	data = {};
	data.GRADE_SMT = "${item.COM_GRADE} - ${item.SMT_NM.substring(0, 1)}";
	data.SUBJECT_CD = "${item.SUBJECT_CD}";
	data.SUBJECT_NM = "${item.SUBJECT_NM}(${item.SUBJECT_CD})";
	data.CDT_NUM = "${item.CDT_NUM}";
	data.S_FLAG = "${item.S_FLAG}";
	target.push(data);
	"</c:forEach>"

	// 교육과정과 비교하여 이수하지 않은 교양과목 추가
	const base = new Set(_DATASET.map(item => item.SUBJECT_CD));
	target.forEach(bItem => {
		if(!base.has(bItem.SUBJECT_CD)) {
			_DATASET.push({
				S_FLAG			: bItem.S_FLAG, 
				STUDENT_NO		: '', 
				GRADE_SMT		: bItem.GRADE_SMT,
				SUBJECT_CD		: bItem.SUBJECT_CD,
				SUBJECT_NM		: bItem.SUBJECT_NM,
				CDT_NUM			: bItem.CDT_NUM
			});
		}
	});
	
	try{
		page_prop.tableMinorReq = new Tabulator(ctx, {
	        layout			: "fitColumns",
			height			: "180px",
			placeholder		: "'개인별적용학점/교육과정' 이 <b>변경</b>되었습니다.</br>"
							  + "『종합정보시스템 > 교양교과목이수체크및확인』에서</br>"
							  + "<b>조회</b> 버튼을 눌러주세요. </br>"
							  + "<a class='go_tis px-2' href='https://tis.kmou.ac.kr/' title='종합정보시스템' target='_blank'>바로가기</a>",
	        cellHozAlign	: 'center',
	        cellVertAlign	: "middle",
	        data			: _DATASET, 
	        paginationSize	: 100,
	
	        columns			: [ 							
	            { title		: "학년/학기", 		field: "GRADE_SMT", 		headerSort:false, 	width:65, },
	            { title		: "과목", 			field: "SUBJECT_NM", 		headerSort:false, },
	            { title		: "학점", 			field: "CDT_NUM", 			headerSort:false, 	width:60, },
	            { title		: "이수여부", 		field: "S_FLAG", 			headerSort:false, 	width:65,
	            								formatter:function(cell){ return makeCompleteBadge(cell.getData().S_FLAG);}, 
	            }, 
	        ],
	        downloadConfig:{
	            columnGroups:false,
	            rowGroups:false,
	            columnCalcs:false,
	        },
	        downloadReady: function(fileContents, blob){
	            return blob;
	        },
	    });

	    // Excel 다운로드 버튼 추가
	    $('#tableMinorReqExcel').append('<button type="button" id="excelDownloadBtnMinorReq" class="btn btn-sm btn-primary">Excel 다운로드</button>');
	    $('#excelDownloadBtnMinorReq').click(function() {
	        page_prop.tableMinorReq.download("xlsx", "교양필수이수현황.xlsx");
	    });	
	}
	catch(error){ asdf(error);}
	finally{ return; }
}

// 비교과 신청이력 테이블
function createTableNonSbjtHist(ctx){
	const _DATASET 		= []
		  , target		= [] ;
	
	"<c:forEach items="${USER_NON_SBJT_SIGNIN}" var="item">"
	var data = {};
	data.TITLE = `${item.TITLE}`;
	data.SUB_TITLE = `${item.SUB_TITLE}`;
	data.GRADE = "${item.GRADE}학년";
	data.IS_COMPLETE = "${item.IS_COMPLETE}";
	data.PROGRAM_DATE = "${item.START_DATE}(${item.START_DAY}) ${item.START_TIME} ~ ${item.END_DATE}(${item.END_DAY}) ${item.END_TIME }";
	data.STATUS_NM = "${item.STATUS_NM}";
	data.NOTE = "${item.NOTE}";
	_DATASET.push(data);
	"</c:forEach>"
	
	try{
		page_prop.tableMinorReq = new Tabulator(ctx, {
	        layout			: "fitColumns",
			height			: "200px",
			placeholder		: "비교과 프로그램 신청 이력이 없습니다.",
	        cellHozAlign	: 'center',
	        cellVertAlign	: "middle",
	        data			: _DATASET, 
	        paginationSize	: 100,
	
	        columns			: [ 							
	            { title		: "프로그램명", 		field: "TITLE", 		headerSort:false, 	 },
	            { title		: "주제명", 		field: "SUB_TITLE", 		headerSort:false, 	 },
	            { title		: "프로그램 일정", 			field: "PROGRAM_DATE", 		headerSort:false, },
	            { title		: "상태", 			field: "STATUS_NM", 			headerSort:false, width:70,	},
	            { title		: "신청학년", 			field: "GRADE", 			headerSort:false, 	width:70, },
	            { title		: "이수여부", 			field: "IS_COMPLETE", 			headerSort:false, width:70, },
	            { title		: "비고", 			field: "NOTE", 			headerSort:false, width:100,	 },
	        ],
	        // Excel 다운로드 설정 추가
	        downloadConfig:{
	            columnGroups:false,
	            rowGroups:false,
	            columnCalcs:false,
	        },
	        downloadReady: function(fileContents, blob){
	            return blob;
	        },
	    });

	    // Excel 다운로드 버튼 추가
	    $('#tableNonSbjtHistExcel').append('<button type="button" id="excelDownloadBtnNonSbjtHist" class="btn btn-sm btn-primary">Excel 다운로드</button>');
	    $('#excelDownloadBtnNonSbjtHist').click(function() {
	        page_prop.tableMinorReq.download("xlsx", "비교과신청이력.xlsx");
	    });
	}
	catch(error){ asdf(error);}
	finally{ return; }
}

// 해시태그(리스트)
function setListHashtag(){
	const $hashtagList = $("#hashtagList");
	
	var $elem = '';
	"<c:forEach items="${USER_HASHTAG}" var="item">"	
	$elem += '<li class="rounded-pill bg-white  d-flex flex-row align-items-center">#<span class="hashtag">' 
				+ "${item.HASHTAG}"
				+ '</span><button type="button" class="border-0 rounded-circle hashDelete"></button></li>';
	"</c:forEach>"
	
	$hashtagList.append($elem);
}

/**
 * @function
 * @description console.log 대체
 * @param {String} 메세지
 * @example
 * asdf('log test');
 * > 'log test'
 */
function asdf(msg){console.log(msg);}
 
/**
 * @function
 * @description Null & Blank 체크
 * @param {Parameter} 매개변수
 * @example
 * isNull(object / String / );
 * > true
 */
function isNullOrBlank(param){
	if(param == null || typeof(param) == 'undefined' || param == '') return true;
	return false;
}

/**
 * @function
 * @description 숫자에 000 단위 콤마 씌우기
 * @param {Number} num 숫자
 * @example
 * comma(100000000);
 * > '100,000,000'
 */
function comma(num, nullFormatter) {
	if(isNullOrBlank(nullFormatter)) nullFormatter = '-';
	if(num == null || num == "") return '0';
	
	num = String(num);
	return num.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}
 

/**
 * @function
 * @description Table의 특정 row background-color 변경
 * @param {row}
 * @example
 * comma(100000000);
 * > '100,000,000'
 */
function setRowStyle(row, color) {
	 if(row.getData().YEAR_SMT == '전체'){
		 row.getElement().style.backgroundColor = color;
	 }
	 if(row.getData().GUBUN == '전체'){
		 row.getElement().style.backgroundColor = color;
	 }
}
 
 /**
  * @function
  * @description Tabulator - 이수 여부 뱃지
  * @param {String} "이수/미이수"
  */
 const $completeBadge0 = $('<span class="badge badge-success"><i class="fa-solid fa-check"></i></span>');
 const $completeBadge1 = $('<span class="badge badge-danger"><i class="fa-solid fa-minus"></i></span>');
 function makeCompleteBadge(val){
     if(val == '이수') return $completeBadge0.clone('true')[0];
     if(typeof(val) == 'undefined' || val == '미이수' || val == '' || val == null) return $completeBadge1.clone('true')[0];
 }
 
 /**
  * @function
  * @description Tabulator - 교과구분 뱃지
  * @param {String} [교양선택, 교양필수, 전공선택, 전공필수 등]
  */
function makeComDivBadge(val){ 	  	  
    const $comDivBadge0 = $('<span class="badge badge-grass">교양  선택</span>');
    const $comDivBadge1 = $('<span class="badge badge-green">교양  필수</span>');
    const $comDivBadge2 = $('<span class="badge badge-litePurple">전공  기초</span>');
    const $comDivBadge3 = $('<span class="badge badge-sky">전공  선택</span>');
    const $comDivBadge4 = $('<span class="badge badge-blue">전공  필수</span>');
    const $comDivBadge5 = $('<span class="badge badge-orange">일반  선택</span>');
    const $comDivBadge6 = $('<span class="badge badge-pink">기초  선택</span>');
    const $comDivBadge7 = $('<span class="badge badge-navy">기초  필수</span>');
    const $comDivBadge8 = $('<span class="badge badge-purple">융합전공  선택</span>');
    const $comDivBadge9 = $('<span class="badge badge-darkNavy">융합전공  필수</span>');
    const $comDivBadge10 = $('<span class="badge badge-grass">교양  과목</span>');
    const $comDivBadge11 = $('<span class="badge badge-sky">복수전공  선택</span>');
    const $comDivBadge12 = $('<span class="badge badge-blue">복수전공  필수</span>');
    const $comDivBadge13 = $('<span class="badge badge-sky">부전공</span>');
    const $comDivBadge14 = $('<span class="badge badge-purple">연계전공  선택</span>');
    const $comDivBadge15 = $('<span class="badge badge-blue">교직  과목</span>');
    const $comDivBadge16 = $('<span class="badge badge-blue">교육학  공통필수</span>');
    const $comDivBadge17 = $('<span class="badge badge-sky">교육학  선택</span>');
    const $comDivBadge18 = $('<span class="badge badge-litePurple">선수  과목</span>');
    const $comDivBadge19 = $('<span class="badge badge-sky">전공  과목</span>');
    
    if(val == '교양선택')         return $comDivBadge0.clone('true')[0];
    if(val == '교양필수')         return $comDivBadge1.clone('true')[0];
    if(val == '전공기초')         return $comDivBadge2.clone('true')[0];
    if(val == '전공선택')         return $comDivBadge3.clone('true')[0];
    if(val == '전공필수')         return $comDivBadge4.clone('true')[0];
    if(val == '일반선택')         return $comDivBadge5.clone('true')[0];
    if(val == '기초선택')         return $comDivBadge6.clone('true')[0];
    if(val == '기초필수')         return $comDivBadge7.clone('true')[0];
    if(val == '융합전공선택')     return $comDivBadge8.clone('true')[0];
    if(val == '융합전공필수')     return $comDivBadge9.clone('true')[0];
    if(val == '교양과목')         return $comDivBadge10.clone('true')[0];
    if(val == '복수전공선택')     return $comDivBadge11.clone('true')[0];
    if(val == '복수전공필수')     return $comDivBadge12.clone('true')[0];
    if(val == '부전공')           return $comDivBadge13.clone('true')[0];
    if(val == '연계전공선택')     return $comDivBadge14.clone('true')[0];
    if(val == '교직과목')         return $comDivBadge15.clone('true')[0];
    if(val == '교육학공통필수')   return $comDivBadge16.clone('true')[0];
    if(val == '교육학선택')       return $comDivBadge17.clone('true')[0];
    if(val == '선수과목')         return $comDivBadge18.clone('true')[0];
    if(val == '전공과목')         return $comDivBadge19.clone('true')[0];
 }
  
function makePassBadge(val){
	const $passBadge0 = $('<span class="badge badge-blue">인증</span>');
	const $passBadge1 = $('<span class="badge badge-pink">미인증</span>');
	
	if(val == '인증') 			return $passBadge0.clone('true')[0];
    if(val == '미인증') 		return $passBadge1.clone('true')[0];
}
  
/* ====================================================================================
 * 찜하기 탭
 *
 */ 
// 찜하기 수 조회



function getMyBookmarkCount(){
	$.ajax({
		url: '/web/bookmark/getMyLoveCount.do?mId=37',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
		    
		}),
		success: function(result){
			var count = result.countList;
			
		    $('[id$="Count"]').each(function() {
		        $(this).text('0');
		    });
		    
			if(result.countList != null){
				$.each(count, function(i, item){
					$("#"+ item.menuFg + "Count").text(item.cnt);
				});				
			}
			
		}
	});
}



function setBookmarkBottomContents(contentsHtml, tabFg, data){
	getMyBookmarkCount();
   	//페이징 변수 선언
	var pageInfo = data.paginationInfo;
		
	var currentPageNo = pageInfo.currentPageNo;
	var totalPageCount = pageInfo.totalPageCount;
	var pageSize = pageInfo.pageSize;
	var firstPageNo = pageInfo.firstPageNo;
	var lastPageNo = pageInfo.lastPageNo;
	var firstPageNoOnPageList = pageInfo.firstPageNoOnPageList;
	var lastPageNoOnPageList = pageInfo.lastPageNoOnPageList;
	var pageCon = "";
		
	
	//해제관련 버튼 처리
    var actionButtons = '<section class="all_about_wrap d-flex flex-wrap justify-content-between mt-3 gap-2">' +
    '<div class="d-flex flex-row gap-2">' +
    '<button type="button" id="bmk_all_check" class="all_check text-white border-0 d-flex flex-row align-items-center gap-1" onclick="bmkAllChk(true)">' +
    '<i></i>전체선택</button>' +
    '<button type="button" id="bmk_remove_check" class="remove_check" onclick="bmkAllChk(false)">선택 일괄해제</button>' +
    '</div>' +
    '<div>' +
    '<button type="button" class="remove_luv bg-white d-flex flex-row align-items-center gap-1" onclick="bmkChkDel(\'get'+tabFg+'List\',\''+currentPageNo+'\')">' +
    '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18">' +
    '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0" />' +
    '<path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202" />' +
    '</svg>찜 해제</button>' +
    '</div>' +
    '</section>';

    contentsHtml += actionButtons;

    

    // 페이징 처리
	pageCon += '<ul class="pagination gap-2 justify-content-center mt-5" id="page">'
	
	if(totalPageCount > pageSize){
		if(firstPageNoOnPageList > pageSize){
			var prevPageNo = firstPageNoOnPageList - pageSize;
		} else{
			var prevPageNo = firstPageNo;
		}
		pageCon += '<li class="page-item page-first"><a href="javascript:get'+tabFg+'List('+firstPageNo+');" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>';
		pageCon += '<li class="page-item page-prev"><a href="javascript:get'+tabFg+'List('+prevPageNo+');" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>';
  		
	}
	
	
	for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
		if(pageNo == currentPageNo){
			pageCon += '<li class="page-item active"><a class="page-link" href="javascript:get'+tabFg+'List('+pageNo+');" title="'+pageNo+'페이지" value="'+pageNo+'">'+pageNo+'</a></li>';
		} else{
			pageCon += '<li class="page-item"><a class="page-link" href="javascript:get'+tabFg+'List('+pageNo+');" title="'+pageNo+'페이지" value="'+pageNo+'">'+pageNo+'</a></li>';
		}
	}
	if(totalPageCount > pageSize){
		if(lastPageNoOnPageList < totalPageCount){
			var nextPageNo = firstPageNoOnPageList + pageSize;
		} else{
			var nextPageNo = lastPageNo;
		}
		pageCon += `<li class="page-item page-next"><a href="javascript:get${menuFg}List(\${nextPageNo});" class="page-link" title="next"><span class="blind">next</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="다음으로 화살표" /></a></li>`;
		pageCon += `<li class="page-item page-last"><a href="javascript:get${menuFg}List(\${lastPageNo});" class="page-link" title="last"><span class="blind">last</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="이전으로 화살표" /></a></li>`;
	
	}
	pageCon += '</ul>'
	contentsHtml += pageCon;
	return contentsHtml;
}



// 교과목 조회(찜하기)
function getSbjtList(page){
	showLoading();
	$.ajax({
		url: '/web/bookmark/getBookmarkTabList.do?mId=37',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			page : page,
			menuFg : 'sbjt'
		}),
		success: function(data){			
			var container = $("#bookmarkConList");
			container.empty();
			if(data.bookmarkTabList != null){
				var bookmarkList = '<section class="majref_wrap d-flex flex-wrap gap-3 mt-4">';
			    $.each(data.bookmarkTabList, function(index, list) {
			    	var deptKey = (typeof(list.MAJOR_CD) != "undefined" && list.MAJOR_CD != "") ? list.MAJOR_CD : list.DEPT_CD;
			    	var DOC_ID = list.SUBJECT_CD+'_'+list.DEPT_CD+'_'+list.YEAR+'_'+list.GRADE+'_'+list.SMT
			    	
			    	
			        var categoryClass = '';
			        switch (list.COMDIV_NM) {
			            case '전공과목': categoryClass = 'maj'; break;
			            case '전공기초': categoryClass = 'maj_basic'; break;
			            case '전공필수': categoryClass = 'maj_esse'; break;
			            case '전공선택': categoryClass = 'maj_choice'; break;
			            default: categoryClass = 'refin'; break;
			        }
	
			        var capaList = list.capa1Nm;
			        if (list.capa2Nm) capaList += ', ' + list.capa2Nm;
			        if (list.capa3Nm) capaList += ', ' + list.capa3Nm;
	
			        bookmarkList += '<div class="item border d-flex flex-row align-items-center">' +
			            '<div class="form-check d-flex justify-content-center pe-2">' +
			            '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="bmkChk_' + index + '" name="bmkChk">' +
			            '<label class="blind" for="bmkChk_' + index + '">전공교양선택</label>' +
			            '</div>' +
			            '<div>' +
			            '<ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-2">' +
			            '<li class="' + categoryClass + '">' + list.COMDIV_NM + '</li>' +
		           		'<li class="name_of_class"><span>' + list.COLG_NM + '</span><span>' + list.DEPT_NM + '</span>' +
		           		'</li>' +
			            '<li class="like_container on_red" id="'+DOC_ID+'">'+
			            '<span class="link_cnt text-end">'+
						'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'sbjt\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						'</span>'+
						'</li>'+
				        '</ul>' +
			            '<h5 class="title mb-1">' +
			            '<a href="javascript:sbjtView(\''+list.SUBJECT_CD+'\',\''+deptKey+'\',\''+list.YEAR+'\',\''+list.SMT+'\');"  title="' + list.SUBJECT_NM + '" class="d-block  fw-semibold">' + list.SUBJECT_NM + '</a> <span>' + (list.SUBJECT_ENM === undefined ? '' : list.SUBJECT_ENM) + '</span>' +
			            '</h5>' +
			            '<p class="desc_txt mb-4 mb-sm-3" >' + (list.SUBJ_DESC === undefined ? '' : list.SUBJ_DESC) + '</p>' +
			            '<ul class="desc_txt info d-flex flex-wrap align-items-start maj_card" style="width:100%">' +
			            '<li class="d-flex flex-row col-12 col-xl-5 mb-2">' +
			            '<strong>편성</strong>' +
			            '<span class="rounded-pill"><span>' + list.YEAR + '학년도<span> <span>'+ (list.GRADE == '0'? '전체':list.GRADE) +'학년 </span> <span>' + list.SMT_NM + '</span></span>' +
			            '</li>' +
			            '<li class="d-flex flex-row col-12 col-xl-7 mb-2">';
			        
		            bookmarkList += '<strong>과정구분</strong>' +
		                '<span>' + list.SINBUN_NM + '</span>';
	
			        bookmarkList += '</li>' +
			            '<li class="d-flex flex-row col-12 col-xl-5 mb-2 mb-xl-0">' +
			            '<strong>학점</strong>' +
			            '<span class="rounded-pill">학점 ' + list.CDT_NUM + '.0</span>' +
			            '</li>' +
			            '<li class="d-flex flex-row col-12 col-xl-7 mb-2 mb-xl-0">' +
			            '<strong>역량구분</strong>' +
			            '<span class="desc_txt">' + (list.MAIN_ABI_NM||'') + '</span>' +
			            '</li>' +
			            '</ul>' +
			            '</div>' +
			            '</div>';
			    });
	
			    bookmarkList += '</section>';
			    bookmarkList = setBookmarkBottomContents(bookmarkList, 'Sbjt', data);
			    container.append(bookmarkList);
			}
		    hideLoading();
		},error:function(request, error) {
			alert("교과목 목록 조회 실패");
			hideLoading();
		}
	});
 }
 
/* 상세보기(교과목코드, 부서코드, 년도, 분반) */
function sbjtView(a,b,c,d){
	
	var form = document.sbjtView;
	form.SUBJECT_CD.value = a;
	form.DEPT_CD.value = b;
	form.YEAR.value = c;
	form.SMT.value = d;
	form.submit();
}
 
//강좌 조회(찜하기)
function getLecList(page){
	showLoading();
	$.ajax({
		url: '/web/bookmark/getBookmarkTabList.do?mId=37',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			page : page,
			menuFg : 'lecture'
		}),
		success: function(data){	
			var currentYear = new Date().getFullYear();
			
			var container = $("#bookmarkConList");
			container.empty();
			if(data.bookmarkTabList != null){
				var bookmarkList = '<section class="open_history d-flex flex-wrap gap-4 mt-4">';
			    $.each(data.bookmarkTabList, function(index, list) {
			        var categoryClass = '';
			        switch (list.COMDIV_NM) {
			            case '전공과목': categoryClass = 'maj'; break;
			            case '전공기초': categoryClass = 'maj_basic'; break;
			            case '전공필수': categoryClass = 'maj_esse'; break;
			            case '전공선택': categoryClass = 'maj_choice'; break;
			            default: categoryClass = 'refin'; break;
		        	}	
	
			        var courseCategory = list.GRADE === '0' ? '전체학년' : list.GRADE + '학년';
			        var DOC_ID = list.SUBJECT_CD +'_'+list.DEPT_CD+'_'+list.YEAR+'_'+list.GRADE+'_'+list.SMT+'_'+list.DIVCLS+'_'+list.EMP_NO
			        var rstrMcnt = (typeof(list.RSTR_MCNT) == "undefined") ? "-" : list.RSTR_MCNT;
	
			        console.log(list);
			        bookmarkList += '<div class="item border d-flex flex-row align-items-center">' +
			            '<div class="form-check d-flex justify-content-center pe-2">' +
			            '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="bmkChk_' + index + '" name="bmkChk">' +
			            '<label class="blind" for="bmkChk_' + index + '">강좌선택</label>' +
			            '</div>' +
			            '<div>' +
			            '<div class="cursor_pointer" data-bs-toggle="modal" data-bs-target="#syllabusModal" onclick="getLectureView(\''+list.SUBJECT_CD+'\',\''+list.DEPT_CD+'\',\''+list.EMP_NO+'\',\''+list.YEAR+'\',\''+list.DIVCLS+'\',\''+list.SMT+'\');">' +
			            '<ul class="cate d-flex flex-row gap-1 flex-wrap">' +
			            '<li class="' + categoryClass + '">' + list.COMDIV_CODE_NAME + '</li>' +
	
	// 		        if (list.YEAR === currentYear) {
	// 		            bookmarkList += '<li class="ongo">' + list.YEAR + '학년' + list.SMT_NAME + '</li>';
	// 		        } else {
	// 		            bookmarkList += '<li class="past">' + list.YEAR + '학년' + list.SMT_NAME + '</li>';
	// 		        }
			            '<li class="like_container on_red" id="'+DOC_ID+'">'+
			            '<span class="link_cnt text-end">'+
						'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'lecture\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						'</span>'+
						'</li>'+
			        	'</ul>' +
			            '<h5 class="fw-semibold ellip_2 mt-2 mb-2">' + list.SUBJECT_NM + '</h5>' +
			            '<div class="simply_info d-flex flex-row flex-wrap">' +
			            '<dl class="d-flex flex-row gap-1 align-items-center">' +
			            '<dt><img src="../images/ico_b_loc.png" alt="장소아이콘" /></dt>' +
			            '<dd class="text-secondary">' + list.COLG_NM + '</dd>' +
			            '</dl>' +
			            '<dl class="d-flex flex-row gap-1 align-items-center">' +
			            '<dt><img src="../images/ico_b_tim.png" alt="시간아이콘" /></dt>' +
			            '<dd class="text-secondary">' + list.YEAR + '년 ' + (list.GRADE == '0' ? '전체' : list.GRADE) + '학년 '+ list.SMT_NAME +  ' ' + list.ROOM_TIME+'</dd>' +
			            '</dl>' +
			            '</div>' +
			            '<div class="info_box mt-2">' +
			            '<ol class="d-flex flex-wrap">' +
			            '<li class="d-flex flex-wrap col-12 col-sm-8 col-md-12 col-lg-8">' +
			            '<b class="d-inline-block">수강대상학과</b>' + list.DEPT_NM +
			            '</li>' +
			            '<li class="d-flex flex-row col-12 col-sm-4 col-md-12 col-lg-4">' +
			            '<b class="d-inline-block">정원</b>' + rstrMcnt +
			            ' 명 </li>' +
			            '<li class="d-flex flex-row col-12 col-sm-8 col-md-12 col-lg-8">' +
			            '<b class="d-inline-block">담당교수</b>' + list.EMP_NM +
			            '</li>' +
			            '<li class="d-flex flex-row col-12 col-sm-4 col-md-12 col-lg-4">' +
			            '<b class="d-inline-block">학년</b>' + courseCategory +
			            '</li>' +
			            '</ol>' +
			            '</div>' +
			            '</div>' +
			            '</div>' +
			            '</div>';
			    });
	
			    bookmarkList += '</section>';
			    bookmarkList = setBookmarkBottomContents(bookmarkList, 'Lec', data);
			    container.append(bookmarkList);
			}
		    hideLoading();
		},error:function(request, error) {
			alert("강좌 목록 조회 실패");
			hideLoading();
		}
	});
 }
 
/* 개설강좌 상세정보 및 강의평가 */
function getLectureView(subjectCd, deptCd, empNo, year, divcls, smt){
	
	var varAction = "/web/sbjt/lectureView.json?mId=32&SUBJECT_CD=" + subjectCd + "&DEPT_CD=" + deptCd + "&EMP_NO=" + empNo + "&YEAR=" + year + "&DIVCLS=" + divcls + "&SMT=" + smt;
	
	
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
  				
  				var grade = (typeof(dt.GRADE) == "undefined") ? "-" : dt.COM_GRADE;
				var year = dt.YEAR;
				var smt = dt.SMT;
				var subjectNm = dt.SUBJECT_NM;
				var subjectCd = dt.SUBJECT_CD;
				var deptCd = dt.DEPT_CD;
				var divcls = (typeof(dt.DIVCLS) == "undefined") ? "-" : dt.DIVCLS;
  				
  				$("#syllabusModalLabel").text(dt.SUBJECT_NM + ' [' + dt.SUBJECT_CD + ']') ;
  				$("#sbjtName").text(dt.SUBJECT_NM);
  	  			$("#sbjtEName").text(dt.SUBJECT_ENM);
  	  			$("#empNm").text(dt.EMP_NM);
  	  			$("#grade").text(dt.COM_GRADE);
  	  			$("#engYn").text(dt.ENG_YN);
  	  			$("#college").text(dt.COLG_NM);
	  			$("#deptNm").text(dt.DEPT_NM);
	  			$("#subjectCd").text(dt.SUBJECT_CD);
	  			$("#divcls").text(dt.DIVCLS);
	  			$("#sinbun").text(dt.COMDIV_CODE_NAME);
	  			$("#lectureWay").text(dt.PYENGGA_TP_NM);
	  			$("#classNm").text(dt.CLASS_NM);
	  			$("#cdtNum").text(dt.CDT_NUM);
	  			$("#timeRoom").text(dt.ROOM_TIME);
	  			$("#fixNumber").text(dt.FIX_STU_CNT);
  	  			
  	  			$("#detailBookmart").attr("onclick", `likeChange('\${subjectCd}_\${deptCd}_\${year}_\${grade}_\${smt}_\${divcls}_\${empNo}','lecture');`);
  	  			$("#detailBasket").attr("onclick", `cartAddRemove('\${year}', '\${smt}', '\${subjectCd}', '\${divcls}', '\${subjectNm}', 'id');`);
  	  		
  			}
  			
  			
  			/* 강의평가 */
  			/* var varItemObj = $("#evalList");
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
			}  */
		
			
  			return false;
  		}, 
  		error:function(request,error){
  			alert("실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	});
}
 
 
//비교과 조회(찜하기)
function getNonSbjtList(page){
	showLoading();
	$.ajax({
		url: '/web/bookmark/getBookmarkTabList.do?mId=37',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			page : page,
			menuFg : 'nonSbjt'
		}),
		success: function(data){	
			
			var container = $("#bookmarkConList");
			container.empty();
			
			if(data.bookmarkTabList != null){
				var bookmarkList = '<section class="non_majref_wrap d-flex flex-wrap gap-4 gap-lg-3 mt-4">';
			    $.each(data.bookmarkTabList, function(index, list) {		    	
			    	var DOC_ID = list.IDX+'_'+list.TIDX
			    	
			    	bookmarkList += '<div class="item border d-flex flex-row align-items-center">' +
		             '<div class="form-check d-flex justify-content-center pe-2">' +
		             '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="bmkChk_' + index + '" name="bmkChk">' +
		             '<label class="blind" for="bmkChk_' + index + '">비교과선택</label>' +
		             '</div>' +
		             '<div>' +
		             '<div class="like_container on_red" id="'+DOC_ID+'">'+
		             '<span class="link_cnt text-end">'+
					 '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'nonSbjt\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
				     '</span>'+
					 '</div>'+	 
		             '<h5 class="d-flex flex-column align-items-start gap-2 mb-2">' +
		             '<span class="'+((list.D_DAY == '종료') ? 'tag_end' : 'tag_adv')+'" style="text-align:center;">' + ((list.D_DAY == '종료') ? '모집완료' : list.D_DAY) + '</span>' +
		             '<a href="javascript:nonSbjtView(' + list.IDX + ',' + list.TIDX + ');" class="ellip_2">' + list.PROGRAM_TITLE + '</a>' +
		             '</h5>' +   
		             '<div class="d-flex flex-column">' +
		             '<dl class="d-flex flex-wrap">' +
		             '<dt class="fw-normal">모집기간</dt>' +
		             '<dd>' + list.SIGNIN_START_DATE + ' ~ ' + list.SIGNIN_END_DATE + '</dd>' +
		             '</dl>' +
		             '<dl class="d-flex flex-wrap mb-2">' +
		             '<dt class="fw-normal">운영기간</dt>' +
		             '<dd>' + list.START_DATE +' ~ ' + list.END_DATE + '</dd>' +
		             '</dl>' +
		             '<dl class="d-flex flex-row justify-content-between">' +
		             '<dt>모집인원</dt>' +
		             '<dd>' +
		             '<strong>' + list.SIGNIN_LIMIT + ' / </strong>' +
		             '<span>' + list.PARTICIPANT + '</span>' +
		             '명 지원중' +
		             '</dd>' +
		             '</dl>' +
		             '</div>' +
		             '</div>' +
		             '</div>';
			    });
	
			    bookmarkList += '</section>';
			    bookmarkList = setBookmarkBottomContents(bookmarkList, 'NonSbjt', data);
			    container.append(bookmarkList);
			}
		    hideLoading();
		},error:function(request, error) {
			alert("강좌 목록 조회 실패");
			hideLoading();
		}
	});
 }


function nonSbjtView(idx, tidx){
	var form = document.nonSbjtView;
	form.idx.value = idx;
	form.tidx.value = tidx;
	form.action = "/web/nonSbjt/view.do";
	form.submit();
}
 
//교수 조회(찜하기)
function getProfList(page){
	showLoading();
	$.ajax({
		url: '/web/bookmark/getBookmarkTabList.do?mId=37',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			page : page,
			menuFg : 'prof'
		}),
		success: function(data){	
			var container = $("#bookmarkConList");
			container.empty();
			if(data.bookmarkTabList != null){
				var bookmarkList = '<section class="profss_wrap d-flex flex-wrap gap-4 gap-lg-3 mt-4">';
				$.each(data.bookmarkTabList, function(index, list) {	
				    var DOC_ID = list.EMP_NO;
	
				    bookmarkList += '<div class="item border d-flex flex-row align-items-center">' +
				        '<div class="form-check d-flex justify-content-center pe-2">' +
				        '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="bmkChk_' + index + '" name="bmkChk">' +
				        '<label class="blind" for="bmkChk_' + index + '">교수님 선택</label>' +
				        '</div>' +
				        '<div>' +
				        '<div class="like_container on_red" id="'+DOC_ID+'">'+
			            '<span class="link_cnt text-end">'+
						'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'prof\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						'</span>'+
						'</div>'+	
				        '<a href="javascript:profView(\'' + DOC_ID + '\');" title="' + list.EMP_NM + '" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3">' +
				        '<span class="photo_box d-inline-block rounded-circle overflow-hidden">' +
				        '<img src="https://www.kmou.ac.kr/' + list.TEA_FILE_PATH + '" alt="교수님사진" style="width:100%; height:100%; object-fit:cover;"/>' +
				        '</span>' +
				        '<span class="txt_box mb-2 mb-sm-0 mb-md-2">' +
				        '<strong>' + list.EMP_NM + '<small class="fw-normal ps-2">교수</small></strong>' +
				        '<span class="d-flex flex-wrap">' +
				        '<em class="fst-normal d-inline-block">' + list.COLG_NM + '</em>' +
				        '<em class="fst-normal d-inline-block">' + list.DEPT_NM + '</em>' +
				        '</span>' +
				        '</span>' +
				        '</a>' +
				        '<ul class="dtl_info pt-4 d-flex flex-column gap-2">' +
				        '<li class="d-flex flex-row text-break">' +
				        '<strong>이메일</strong>' + (list.TEA_EMAIL || '') +
				        '</li>' +
				        '<li class="d-flex flex-row text-break">' +
				        '<strong>전화번호</strong>' + (list.TEA_TLPHON || '') +
				        '</li>' +
				        '<li class="d-flex flex-row text-break" style="white-space: nowrap; text-overflow: ellipsis; overflow: hidden;">' +
				        '<strong>연구분야</strong>' + (list.TEA_RSRCH_REALM || '') +
				        '</li>' +
				        '<li class="d-flex flex-row text-break">' +
				        '<strong>연구실</strong>' + (list.TEA_LOCATION || '') +
				        '</li>' +
				        '</ul>' +
				        '</div>' +
				        '</div>';
				});
	
	
			    bookmarkList += '</section>';
			    bookmarkList = setBookmarkBottomContents(bookmarkList, 'Prof', data);
			    container.append(bookmarkList);
			}
		    hideLoading();
		},error:function(request, error) {
			alert("교수 목록 조회 실패");
			hideLoading();
		}
	});
 }
 
 
/* 상세보기 */
function profView(a){	
	var form = document.profView;
	form.empNo.value = a;
	form.action ="/web/prof/view.do";
	form.submit();
}
 
//전공 조회(찜하기)
function getMajorList(page){
	showLoading();
	$.ajax({
		url: '/web/bookmark/getBookmarkTabList.do?mId=37',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			page : page,
			menuFg : 'major'
		}),
		success: function(data){	
			var container = $("#bookmarkConList");
			container.empty();
			if(data.bookmarkTabList != null){
				var bookmarkList = '<section class="speci_wrap d-flex flex-wrap gap-3 mt-4">';
				$.each(data.bookmarkTabList, function(index, list) {	
				    var DOC_ID = list.MAJOR_CD;
	
				    bookmarkList += '<div class="item border d-flex flex-row align-items-center">' +
			            '<div class="form-check d-flex justify-content-center pe-2">' +
			            '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="bmkChk_' + index + '" name="bmkChk">' +
			            '<label class="blind" for="bmkChk_' + index + '">전공 선택</label>' +
			            '</div>' +
			            '<div>' +
			            '<ul class="cate d-flex flex-row flex-wrap gap-1 align-items-center mb-2">' +
			            '<li>' + list.MAJOR_NM_KOR + '</li>' +
			            '<div class="like_container on_red" id="'+DOC_ID+'">'+
			            '<span class="link_cnt text-end">'+
						'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'major\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						'</span>'+
						'</div>'+	
			            '</ul>' +
			            '<h5 class="title mb-3">' +
			            '<a href="javascript:" onclick="redirectMajorView(\''+ DOC_ID +'\')" title="' + list.MAJOR_NM_KOR + '" class="d-block fw-semibold text-truncate">' +
			            list.MAJOR_NM_KOR + ' <small class="d-block fw-normal mt-1 text-truncate">' + (list.MAJOR_NM_ENG||'') + '</small>' +
			            '</a>' +
			            '</h5>' +	            
			            '<dl class="d-flex flex-row mb-1">' +
			            '<dt>전공소개</dt>' +
			            '<dd>' + list.MAJOR_INTRO + '</dd>' +
			            '</dl>' +
			            '<dl class="d-flex flex-row ">' +
			            '<dt>진로</dt>' +
			            '<dd>' + list.GRDT_AF_CARR + '</dd>' +
			            '</dl>' +
			            '</div>' +
			            '</div>';
				});
	
	
			    bookmarkList += '</section>';
			    bookmarkList = setBookmarkBottomContents(bookmarkList, 'Major', data);
			    container.append(bookmarkList);
			}
		    hideLoading();
		},error:function(request, error) {
			alert("전공 목록 조회 실패");
			hideLoading();
		}
	});
 }
 
//상세페이지 이동
function redirectMajorView(majorCd){
	var form = document.majorView;
	form.MAJOR_CD.value = majorCd;
	form.action = "/web/major/view.do"; 
	form.submit();
}


//학생설계전공 조회(찜하기)
function getStudPlanList(page){
	showLoading();
	$.ajax({
		url: '/web/bookmark/getBookmarkTabList.do?mId=37',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			page : page,
			menuFg : 'studPlan'
		}),
		success: function(data){	
			var container = $("#bookmarkConList");
			container.empty();
			
			if(data.bookmarkTabList != null){
				var bookmarkList = '<section class="plan_section d-flex flex-wrap gap-4 gap-lg-3 mt-4">';
				$.each(data.bookmarkTabList, function(index, list) {	
				    var DOC_ID = list.SDM_DEPT_CD;
	
				    bookmarkList += '<div class="item border d-flex flex-row align-items-center">' +
			             '<div class="form-check d-flex justify-content-center pe-2">' +
			             '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="bmkChk_' + index + '" name="bmkChk">' +
			             '<label class="blind" for="bmkChk_' + index + '">학생설계전공 선택</label>' +
			             '</div>' +
			             '<div>' +
			             '<ul class="d-flex flex-row gap-2 cate">' +
			             '<li class="year">' + list.APLY_YEAR + '</li>' +
			             '<li class="border">복수전공</li>' +
			             '<li class="like_container on_red" id="'+DOC_ID+'">'+
			             '<span class="link_cnt text-end">'+
						 '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'studPlan\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						 '</span>'+
						 '</li>'+	
			             '</ul>' +
			             '<h3 class="title fs-5 fw-bolder text-start mb-3">' +
			             '<a href="javascript:" onclick="goView(\'' + list.SDM_CD + '\',\'' + list.REVSN_NO + '\')" title="' + list.SDM_KOR_NM + '" class="text-truncate">' + list.SDM_KOR_NM + '</a>' +
			             '</h3>' +	             
			             '<div class="detail_maj position-relative">' +
			             '<span class="d-block lh-sm mb-1">' + list.FIRST_MAJOR + '</span>' +
			             (list.OTHER_MAJOR ? '<span class="d-block lh-sm">' + list.OTHER_MAJOR + '</span>' : '') +
			             '</div>' +
			             '<div class="detail_box mt-3 px-3 px-sm-4 py-3 d-flex flex-column gap-2">' +
			             '<dl class="d-flex flex-wrap">' +
			             '<dt>수여학위</dt>' +
			             '<dd>' + list.AWD_DEGR_KOR_NM + '</dd>' +
			             '</dl>' +
			             '<dl class="d-flex flex-wrap">' +
			             '<dt>설계학점</dt>' +
			             '<dd>' + list.TOTAL_CDT_NUM + '</dd>' +
			             '</dl>' +
			             '<dl class="d-flex flex-wrap">' +
			             '<dt>교과목설계범위</dt>' +
			             '<dd>' + list.SBJT_DGN_RNG_FG_NM + '</dd>' +
			             '</dl>' +
			             '</div>' +
			             '</div>' +
			             '</div>';
				});
	
	
			    bookmarkList += '</section>';
			    bookmarkList = setBookmarkBottomContents(bookmarkList, 'StudPlan', data);
			    container.append(bookmarkList);
			}
		    hideLoading();
		},error:function(request, error) {
			alert("학생설계전공 목록 조회 실패");
			hideLoading();
		}
	});
}


function goView(a,b){
	var form = document.sdmView;
	$("#SDM_CD").val(a);
	$("#REVSN_NO").val(b);
	form.action = '/web/studPlan/studView.do';
	form.submit();
}
//찜 개별삭제
function likeDel(docId, tabFg){
	$.ajax({
		url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg=' + type,
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
		    docId : docId
		}),
		success: function(data){
			$("#"+ docId + "_" + type).removeClass("on_red");
			alert("삭제 되었습니다.");
			getMyBookmarkCount();
			
			var functionName = 'get' + tabFg + 'List';
			getMyBookmarkCount();
			window[functionName](1);
			
		}
	});	
}
//관심강좌 가져오기
function getMyLoveList(type){
	return new Promise((resolve, reject) => {		
		$.ajax({
			url: '/web/bookmark/getBookmarkList.do?mId=37',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    menuFg : type
			}),
			success: function(data){			
				resolve(data.bookmarkList);
				console.log(JSON.stringify(data))
			},error:function() {
				reject("");
			}
		});
	})
}

// 나의 찜 전체 선택
function bmkAllChk(e){
	$("input[name=bmkChk]").prop("checked", e);
}

function bmkChkDel(pageFg, pageNo){
	var menuFg = $("#tabValue").val();
	var dataArr = "";
	$("input[name=bmkChk]:checked").each(function(){
//			dataArr.push($(this).val());
		if(dataArr == ""){
			dataArr = $(this).val();
		}else{
			dataArr = dataArr + ","+$(this).val();
		}
	});
	
	$.ajax({
		url: '/web/bookmark/deleteSelectedBookmark.do?mId=37&menuFg='+menuFg,
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
		    docId : dataArr
		}),
		success: function(data){
			getMyBookmarkCount();
			window[pageFg](pageNo);				
		}
	});	
}


function bmkAllDel(tabFg){
	var menu = "";
	var menuFg = "";
	
	if(tabFg == "Sbjt"){
		menu = "전공·교양";
	}else if(tabFg == "Lec"){
		menu = "강좌";
	}else if(tabFg == "NonSbjt"){
		menu = "비교과";
	}else if(tabFg == "Prof"){ 
		menu = "교수";
	}else if(tabFg == "Major"){
		menu = "전공";
	}else if(tabFg == "StudPlan"){
		menu = "학생설계";
	}
	$.ajax({
		url: '/web/bookmark/deleteAllBookmark.do?mId=37&menuFg='+menuFg,
		type: 'POST',
		success: function(data){
			alert(menu + " 찜이 모두 삭제되었습니다.");
			var functionName = 'get' + tabFg + 'List';
			getMyBookmarkCount();
			window[functionName](1);			
		}
	});	
}
 
/* ====================================================================================
 * 장바구니 탭
 *
 */


 
//장바구니 가져오기
function getBasketCart(){
// 	var year = $("#hisYear").val();
// 	var smt = $("#hisSemi").val();
	
	$.ajax({
		url: '/web/basket/getBasketList.do?mId=52',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			YEAR : 'all',
			SMT : 'all'
		}),
		success: function(data){
			const $basketList = $("#basketList");			
			$basketList.empty();
			
			var today = new Date();
			var year = today.getFullYear();
			
			basketList = data.basketList;
			innerHtml = '';
			if(basketList != null){
			/* 장바구니 */
				$.each(basketList, function(i, list){
					var DOC_ID = list.SUBJECT_CD+'_'+list.DEPT_CD+'_'+list.YEAR+'_'+list.COM_GRADE+'_'+list.SMT
					var sbjt_class;
					switch(list.COMDIV_CODE_NAME){
						case "전공공통" :
							sbjt_class = "nati_navy";
							break;
						case "전공필수" :
							sbjt_class = "alon_red";
							break;
						case "전공선택" :
							sbjt_class = "styl_skyb";
							break;
						case "필수교양" :
							sbjt_class = "styl_lighb";
							break;
						default : 
							sbjt_class = "refi_yell";
					}
					
// 					var year_class;
// 					if(Number(list.YEAR) == year){
						year_class = "ongo";
// 					} else {
// 						year_class = "past";
// 					}
					
// 					var errMsg = '예비수강신청 연계 개발 전';
// 					var errMsg = list.errMsg;
// 					if(list.aplyRslt == "-20"){
// 						errMsg = "수강신청 기간이 아닙니다";
// 					} else if(list.aplyRslt == "-210") {
// 						errMsg = "신청가능학점 초과";
// 					} else if(list.errMsg == null){
// 						errMsg = "";
// 					}
					
					var cls_class;
					var cls_id;
					var cls_text;
// 					if(list.clsClssYn == "Y"){
// 						cls_class = "my_st_nop_app2";
// 						cls_id = "closeCls_" + i;
// 						cls_text = "폐강";
// 					} else if(list.aplyRslt == "0"){
// 						cls_class = "my_st_succ_app";
// 						cls_id = "aplyCls_" + i;
// 						cls_text = "신청완료";
// 					} else if(list.aplyRslt == "-700"){
// 						cls_class = "my_st_re_app";
// 						cls_id = "reAplyCls" + i;
// 						cls_text = "재수강";
// 					} else {
						cls_class = "my_st_first_app";
						cls_id = "aplyCls_" + i;
						cls_text = "예비수강신청";
// 					}
					

// 					innerHtml += '<div class="item d-flex flex-row gap-2 align-items-center position-relative" id="cartArea_' + i + '">' +
					innerHtml += '<div class="item d-flex flex-row gap-2 align-items-center position-relative" id="' + DOC_ID + '">' +
								 '<div class="form-check">' +
								 '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="cartChk_' + i +'" name="cartChk" data-year="' + list.YEAR + '" data-smt="' + list.SMT + '" data-subjectCd="' + list.SUBJECT_CD + '" data-divCls="' + list.DIVCLS + '" data-deptCd="' + list.DEPT_CD + '" data-empNo="'+list.EMP_NO+'" data-roomTime="'+list.ROOM_TIME+'" data-subjectNm="'+list.SUBJECT_NM+'"' +
								 ' data-empNm="'+list.EMP_NM+'" data-deptNm="'+list.DEPT_NM+'" data-comdivCodeName="'+list.COMDIV_CODE_NAME+'" data-deptNm="'+list.DEPT_NM+'" data-cdtNum="'+list.CDT_NUM+'" data-comGrade="'+list.COM_GRADE+'">' +
								 '<label class="blind" for="cartChk">강좌선택</label></div>'
					
					
					innerHtml += '<div><div class="d-flex flex-row justify-content-between align-items-center">' +
								 '<ul class="cate d-flex flex-row gap-1 flex-wrap">' +
								 '<li class="' + sbjt_class + '">' + list.COMDIV_CODE_NAME + '</li>' +
								 '<li class="' + year_class + '">' + list.YEAR + '학년도 ' + list.SMT_NAME + '</li>' + 
								 '<ol class="d-flex btn_wrap align-items-center gap-2 ">' + 
								 '<li><button type="button" class="border-0 btn_dele rounded-circle" onclick="deleteBasket(\'' + list.YEAR + '\',\'' + list.SMT + '\',\''+ list.SUBJECT_CD + '\',\'' + list.DEPT_CD + '\',\'' + list.DIVCLS + '\',\'' + list.SUBJECT_NM + '\', \'' + list.EMP_NO + '\', \'' + DOC_ID + '\')"></button></li></ol></div>' +
								 '<h5 class="fw-semibold text-truncate w-100">' + list.SUBJECT_NM + '</h5>' +
// 								 '<li style="font-weight:400;">' + errMsg + '</li></ul>' + 
								 '<ul class="info_wrap d-flex flex-column">' +
								 '<li class="d-flex flex-wrap"><dl class="d-flex flex-row align-items-start gap-2"><dt>전공</dt><dd>' + list.DEPT_NM + '</dd></dl><dl class="d-flex flex-row align-items-start gap-2"><dt>교수</dt><dd>' + list.EMP_NM + '</dd></dl><dl class="d-flex flex-row align-items-start gap-2"><dt>수강학년</dt><dd>' + list.COM_GRADE + '학년</dd></dl></li>' +
								 '<li class="d-flex flex-wrap"><dl class="d-flex flex-row align-items-start gap-2"><dt>시간</dt><dd>' + list.ROOM_TIME + '</dd></dl><dl class="d-flex flex-row align-items-start gap-2"><dt>정원</dt><dd>' + (list.STUDENT_CNT||'00') + '명</dd></dl></li></ul>' +
// 								 '<div class="my_statt d-flex flex-row align-items-center ms-auto ' + cls_class + '">' + 
// 								 '<button class="bg-transparent border-0" id="' + cls_id + '">' + cls_text + '</button></div></div></div>'
// 								 '</div>'+
								 '</div></div>'
				});
			}
				
			$basketList.empty();
			$basketList.append(innerHtml);	
		},error:function(request, error) {
			alert("장바구니 목록 조회 실패");
		}
	});
 }
 
//장바구니 삭제
function deleteBasket(year, smt, subjectCd, deptCd, divcls, sbjtKorNm, empNo, id){
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
		success: function(result){
			$("#"+id).remove();
			
            // basketList에서 해당 항목 삭제
            basketList = basketList.filter(item => 
                !(item.YEAR === year &&
                  item.SMT === smt &&
                  item.SUBJECT_CD === subjectCd &&
                  item.DEPT_CD === deptCd &&
                  item.DIVCLS === divcls &&
                  item.SUBJECT_NM === sbjtKorNm &&
                  item.EMP_NO === empNo)
            );
            // 예비수강신청 AI시스템 장바구니 재배치
            renderBeforeCart();
			
			//$("#"+id).addClass("disa");
			alert("장바구니에서 삭제 되었습니다.");
			//location.reload();
		}
	});
}





//선택한 과목 해제
function cartSelectedDel(deleteList){
	$.ajax({
		url: '/web/basket/deleteSelectedBsk.do?mId=52',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			deleteList : deleteList.toString()
		}),
		success: function(data){
			if(data.result == "DONE"){
				alert("선택된 강의가 정상적으로 삭제되었습니다.");
				getBasketCart();
			} else{
				alert("선택된 강의를 정상적으로 삭제하지 못했습니다.");
			} 
		}
	});	
}

//장바구니 전체 선택
function cartAllChk(e){
	$("input[name=cartChk]").prop("checked", e);
}

//장바구니 시간표
function setTimeTable(cartList) {
    // 시간표 구조 초기화 (17행 7열)
    var timeTable = Array(17).fill().map(() => Array(7).fill(null));

    // 각 수업을 시간표에 배치
    cartList.forEach(function(item, index) {
        var match = item.roomTime.match(/([월화수목금토일])(\d+)~(\d+)/);
        if (match) {
            var day = ['월', '화', '수', '목', '금', '토', '일'].indexOf(match[1]);
            var startTime = parseInt(match[2]);
            var endTime = parseInt(match[3]);
            for (var period = startTime; period <= endTime; period++) {
                if (!timeTable[period][day]) {
                    timeTable[period][day] = [];
                }
                timeTable[period][day].push({
                    subjectNm: item.subjectNm,
                    index: index
                });
            }
        }
    });

    // 병합 정보 계산
    var mergeInfo = Array(17).fill().map(() => Array(7).fill(null));
    for (var period = 0; period < 17; period++) {
        for (var day = 0; day < 7; day++) {
            if (timeTable[period][day] && !mergeInfo[period][day]) {
                var rowspan = 1;
                var colspan = 1;
                
                // 세로 병합 (rowspan) 계산
                while (period + rowspan < 17 && 
                       JSON.stringify(timeTable[period + rowspan][day]) === JSON.stringify(timeTable[period][day])) {
                    rowspan++;
                }
                
                // 가로 병합 (colspan) 계산
                while (day + colspan < 7 && 
                       JSON.stringify(timeTable[period][day + colspan]) === JSON.stringify(timeTable[period][day])) {
                    colspan++;
                }
                mergeInfo[period][day] = { rowspan: rowspan, colspan: colspan };
                
                // 병합될 셀 표시
                for (var r = 0; r < rowspan; r++) {
                    for (var c = 0; c < colspan; c++) {
                        if (r !== 0 || c !== 0) {
                            mergeInfo[period + r][day + c] = 'merged';
                        }
                    }
                }
            }
        }
    }

    // 테이블 생성
    var tableHTML = '';
    for (var period = 0; period < 17; period++) {
        tableHTML += '<tr>';
        tableHTML += '<td style="text-align: center">' + period + '</td>';
        tableHTML += '<td style="text-align: center">' + (period + 8) + ':00</td>';
        
        for (var day = 0; day < 7; day++) {
            if (mergeInfo[period][day] === 'merged') continue;
            
            if (timeTable[period][day]) {
                var info = mergeInfo[period][day];
                var subjects = timeTable[period][day];
                var cellContent = '';
                var cellStyle = '';
                
                if (subjects.length === 1) {
                    cellContent = subjects[0].subjectNm;
                    cellStyle = 'background-color: hsl(' + (subjects[0].index * 20 % 360) + ', 50%, 70%); color: #FFFFFF;';
                } else {
                    var shortText = subjects[0].subjectNm + ' 외 ' + (subjects.length - 1) + '개';
                    var fullText = subjects.map(s => s.subjectNm).join(',\n');
                    cellContent = shortText;
                    cellStyle = 'background-color: red; color: #FFFFFF;';
                }
                
                tableHTML += '<td style="text-align: center; vertical-align: middle; ' + cellStyle + '" ' +
                             'rowspan="' + info.rowspan + '" colspan="' + info.colspan + '" ' +
                             (subjects.length > 1 ? 'data-fulltext="' + fullText + '"' : '') + '>' +
                             cellContent + '</td>';
            } else {
                tableHTML += '<td style="text-align: center; vertical-align: middle;">.</td>';
            }
        }
        tableHTML += '</tr>';
    }

    // 테이블에 HTML 삽입
    $("#timeTableBody").html(tableHTML);

    // 호버 이벤트 추가
    $("#timeTableBody td[data-fulltext]").hover(
        function() {
            var fullText = $(this).attr('data-fulltext').replace(/\n/g, '<br>');
            $(this).html(fullText);
        },
        function() {
            var shortText = $(this).text().split(',')[0] + ' 외 ' + ($(this).text().split(',').length - 1) + '개';
            $(this).text(shortText);
        }
    );

    $("#isMerged").show();
}


//찜 변경
function likeChange(docId, type){
	//찜 등록/삭제
	if($("#"+docId).hasClass("on_red")){
		$.ajax({
			url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg=' + type,
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    docId : docId
			}),
			success: function(data){
				$("#"+docId).removeClass("on_red");
				getMyBookmarkCount();
			}
		});		
	} else {
		$.ajax({
			url: '/web/bookmark/insertBookmark.do?mId=37&menuFg=' + type,
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    docId : docId
			}),
			success: function(data){
				$("#"+docId).addClass("on_red");
				getMyBookmarkCount();
			}
		});
	}
}

//관심강좌 가져오기
function getBookmarkList(){
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

//로딩 바	
function showLoading() {
	const loader = document.querySelector('.loader');
	const overlay = document.getElementById('overlay');
	loader.style.display = 'block';	
	overlay.style.display = 'block';
	
}	
//로딩바 hide	
function hideLoading() {
	const loader = document.querySelector('.loader');
	const overlay = document.getElementById('overlay');
	loader.style.display = 'none';
	overlay.style.display = 'none';
	
}	

//예비수강신청 AI시스템 장바구니 리스트
function renderBeforeCart(){	
	var html ='';
	$("#appndSbjt").empty();
	if(basketList.length != 0){
		$.each(basketList, function(i, list){
			
			var sbjt_class;
			switch(list.COMDIV_CODE_NAME){
				case "전공공통" :
					sbjt_class = "nati_navy";
					break;
				case "전공필수" :
					sbjt_class = "alon_red";
					break;
				case "전공선택" :
					sbjt_class = "styl_skyb";
					break;
				case "필수교양" :
					sbjt_class = "styl_lighb";
					break;
				default : 
					sbjt_class = "refi_yell";
			}
			
			var DOC_ID = list.SUBJECT_CD + '_' + list.DIVCLS			
		    html += '<div class="item p-3" style="border-top: 1px solid #ddd;">';
		    html += 	'<div class="d-inline-flex form-check appl">';
		    html += 		'<label class="blind" for="lessons_'+DOC_ID+'">선택</label>';
		    html += 		'<input type="checkbox" id="lessons_'+DOC_ID+'" class="form-check-input chk-before" value="'+DOC_ID+'">';
		    html += 	'</div>';
		    html += 	'<section class="d-inline-flex flex-column flex-lg-row justify-content-between">';
		    html += 		'<input type="hidden" id="sbInfo-'+DOC_ID+'" value="'+DOC_ID+'" data-subjectCd="'+list.SUBJECT_CD+'" data-divcls="'+list.DIVCLS+'" data-subjectNm="'+list.SUBJECT_NM+'"/>';
		    html += 		'<div class="d-flex flex-column align-items-start" id="div-'+DOC_ID+'">';
		    html += 			'<ul class="cate d-flex flex-wrap gap-2">';
		    html += 				'<li class="text-white '+sbjt_class+'">'+list.COMDIV_CODE_NAME+'</li>';
		    html += 				'<li class="border">';
		    html += 					'<span>'+list.DEPT_NM+'</span>';
		    html += 				'</li>';
		    html += 			'</ul>';
		    html += 			'<h4 class="text-truncate ellip_2 my-2 h-sbjt-kor-nm" id="sbjtNm-'+list.DOC_ID+'"><span class="text-truncate text-dark text-opacity-75">['+list.SUBJECT_CD+'-'+list.DIVCLS+'] </span>' +(list.SUBJECT_NM||'')+'</h4>';
		    html += 			'<div class="w-100">';
		    html += 				'<dl class="d-inline-flex">';
		    html += 					'<dt class="me-2">학점</dt>';
		    html += 					'<dd class="text-dark text-opacity-75">'+list.CDT_NUM+'</dd>';
		    html +=					'</dl>';
		    html += 				'<dl class="d-inline-flex position-relative pe-4">';
		    html += 					'<dt class="me-2">편성</dt>';
		    html += 					'<dd class="text-dark text-opacity-75">'+list.YEAR+'학년도 '+list.COM_GRADE+'학년</dd>';
		    html += 				'</dl>';
		    html += 				'<dl class="d-inline-flex position-relative pe-4">';
		    html += 					'<dt class="me-2">담당교수</dt>';
		    html += 					'<dd class="text-dark text-opacity-75">'+list.EMP_NM+'</dd>';
		    html += 				'</dl>';
		    html += 			'</div>';
		    html += 		'</div>';
		    html += 		'<div class="btn_wrap appl">';	
		    html +=				'<button type="button" class="btn_dele" onclick="deleteBasket(\'' + list.YEAR + '\',\'' + list.SMT + '\',\''+ list.SUBJECT_CD + '\',\'' + list.DEPT_CD + '\',\'' + list.DIVCLS + '\',\'' + list.SUBJECT_NM + '\', \'' + list.EMP_NO + '\', \'' + DOC_ID + '\')">';
		    html +=					'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>';
		    html +=				'</button>';
		    html += 			'<button type="button" class="btn_put" onclick="sbjtContain(\''+DOC_ID+'\');">';
		    html += 				'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"></line><polyline points="12 5 19 12 12 19"></polyline></svg>';
		    html += 			'</button>';
		    html += 		'</div>';
		    html += 	'</section>';
		    html += '</div>'; 
		});
		$("#appndSbjt").append(html);	
	}else{
		var html = '';
		html += '<div class="lesson_wrap no_contents_wrap d-flex justify-content-center align-items-center reorder">' +
				'<div class="d-flex flex-column justify-content-center align-items-center">' +
				'<img src="../images/kmou_noshad_big.png" alt="해양이" />' + 
			    '<p class="text-center mt-2">' + 
			    	'장바구니가 비어있습니다.' + 
			    '</p>' + 
			    '</div>' + 
				'</div>';
		$('#appndSbjt').append(html)
	}				
}


//예비수강신청 현황 리스트
function renderApplList(){
	
	try{
		$.ajax({
			url: '/web/basket/getPreApplSbjt.do?mId=52',
			contentType:'application/json',	
			type: 'POST',
			success: function(result){
				var ord = 1;
				var html ='';
				$("#applSbjt").empty();
				if(result.preApplList.length != 0){
					$.each(result.preApplList, function(i, list){
						
						var sbjt_class;
						switch(list.COMDIV_NM){
							case "전공공통" :
								sbjt_class = "nati_navy";
								break;
							case "전공필수" :
								sbjt_class = "alon_red";
								break;
							case "전공선택" :
								sbjt_class = "styl_skyb";
								break;
							case "필수교양" :
								sbjt_class = "styl_lighb";
								break;
							default : 
								sbjt_class = "refi_yell";
						}					
						var DOC_ID = list.SUBJECT_CD + '_' + list.DIVCLS			
					    html += '<div class="item p-3" style="border-top: 1px solid #ddd;" data-year="'+list.YEAR+'" data-smt="'+list.SMT+'" data-subjectCd="'+list.SUBJECT_CD+'" data-divcls="'+list.DIVCLS+'" data-ui_ord="'+ord+'" data-org_ord="'+list.ORD+'">';
					    html += 	'<div class="d-inline-flex form-check appl">';
					    html += 		'<label class="blind" for="lessons_'+DOC_ID+'">선택</label>';
					    html += 		'<input type="checkbox" id="lessons_'+DOC_ID+'" class="form-check-input chk-after" value="'+DOC_ID+'">';
					    html += 	'</div>';
					    html += 	'<section class="d-inline-flex flex-column flex-lg-row justify-content-between">';
					    html += 		'<input type="hidden" id="applInfo-'+DOC_ID+'" value="'+DOC_ID+'" data-subjectCd="'+list.SUBJECT_CD+'" data-divcls="'+list.DIVCLS+'" data-subjectNm="'+list.SUBJECT_NM+'"/>';
					    html += 		'<div class="d-flex flex-column align-items-start" id="div-'+DOC_ID+'">';
					    html += 			'<ul class="cate d-flex flex-wrap gap-2">';
			 			html += 				'<li class="text-white '+sbjt_class+'">'+list.COMDIV_NM+'</li>';				    
					    html += 				'<li class="border">';
					    html += 					'<span>'+list.DEPT_NM+'</span>';
					    html += 				'</li>';
					    html += 			'</ul>';
					    html += 			'<h4 class="text-truncate ellip_2 my-2 h-sbjt-kor-nm" id="sbjtNm-'+list.DOC_ID+'"><span class="text-truncate text-dark text-opacity-75">['+list.SUBJECT_CD+'-'+list.DIVCLS+'] </span>' +(list.SUBJECT_NM||'')+'</h4>';
					    html += 			'<div class="w-100">';
					    html += 				'<dl class="d-inline-flex">';
					    html += 					'<dt class="me-2">학점</dt>';
					    html += 					'<dd class="text-dark text-opacity-75">'+list.CDT_NUM+'</dd>';
					    html +=					'</dl>';
					    html += 				'<dl class="d-inline-flex position-relative pe-4">';
					    html += 					'<dt class="me-2">편성</dt>';
					    html += 					'<dd class="text-dark text-opacity-75">'+list.YEAR+'학년도 '+list.COM_GRADE+'학년</dd>';
					    html += 				'</dl>';
					    html += 				'<dl class="d-inline-flex position-relative pe-4">';
					    html += 					'<dt class="me-2">담당교수</dt>';
					    html += 					'<dd class="text-dark text-opacity-75">'+(list.EMP_NM||'')+'</dd>';
					    html += 				'</dl>';
					    html += 			'</div>';
					    html += 		'</div>';
					    html += 		'<div class="btn_wrap appl">';	
					    html +=				'<button type="button" class="btn_dele" onclick="delSbjt(\''+DOC_ID+'\')">';
					    html +=					'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"></line><line x1="6" y1="6" x2="18" y2="18"></line></svg>';
					    html +=				'</button>';
					    html += 		'</div>';
					    html += 		'<div class="btn_wrap reorder hidden" style="display:block;">';					    	
	                    html += 			'<button type="button" class="btn_put" onclick="moveItem(\''+DOC_ID+'\', \'up\')" style="rotate:270deg;">';
	                    html += 			'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"></line><polyline points="12 5 19 12 12 19"></polyline></svg>';
	                    html += 			'</button>';
	                    html += 			'<button type="button" class="btn_put" onclick="moveItem(\''+DOC_ID+'\', \'down\')" style="rotate:90deg;">';
	                    html += 			'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="5" y1="12" x2="19" y2="12"></line><polyline points="12 5 19 12 12 19"></polyline></svg>';
	                    html += 			'</button>';
					    html += 		'</div>';				    				    	
					    html += 	'</section>';
					    html += '</div>'; 
					    
					    ord ++;
					});
					$("#applSbjt").append(html);
					
				    
					updateArrowButtons();
				}else{
					var html = '';
					html += '<div class="lesson_wrap no_contents_wrap d-flex justify-content-center align-items-center reorder">' +
							'<div class="d-flex flex-column justify-content-center align-items-center">' +
							'<img src="../images/kmou_noshad_big.png" alt="해양이" />' + 
						    '<p class="text-center mt-2">' + 
						    	'예비수강신청 내역이 존재하지 않습니다.' + 
						    '</p>' + 
						    '</div>' + 
							'</div>';
					$('#applSbjt').append(html);
					$('#reorderBtn').addClass('hidden')
				}
				
			}, error: function(request, error){ asdf(error) }
		});
	}catch(e){
		return alert(e);
	}	
	

}

//화살표 버튼 업데이트 함수
function updateArrowButtons() {
    $('#applSbjt .item').each(function(index, item) {
        var $item = $(item);
        var $upButton = $item.find('.btn_put[onclick*="up"]');
        var $downButton = $item.find('.btn_put[onclick*="down"]');
        
        // 첫 번째 아이템
        if (index === 0) {
            $upButton.hide();
            $downButton.show();
        } 
        // 마지막 아이템
        else if (index === $('#applSbjt .item').length - 1) {
            $upButton.show();
            $downButton.hide();
        } 
        // 중간 아이템들
        else {
            $upButton.show();
            $downButton.show();
        }
    });
}

//예비수강신청 현황 카드 이동 함수
function moveItem(docId, direction) {
    var currentItem = $('#div-' + docId).closest('.item');
    var currentIndex = currentItem.index();
    var sibling;
    var targetIndex;

    if (direction === 'up') {
        sibling = currentItem.prev('.item');
        targetIndex = currentIndex - 1;
    } else {
        sibling = currentItem.next('.item');
        targetIndex = currentIndex + 1;
    }

    if (sibling.length > 0) {
        if (direction === 'up') {
            currentItem.insertBefore(sibling);
        } else {
            currentItem.insertAfter(sibling);
        }
        
        currentItem.hide().fadeIn(300); // 애니메이션 효과
        
        updateOrderAttributes(currentIndex, targetIndex);
        
        updateArrowButtons();
    }
}

//순서 속성 업데이트 함수
function updateOrderAttributes(index1, index2) {
    $('#applSbjt .item').each(function(index) {
        var $item = $(this);
        var newOrder = index + 1;
        
        // data-ui_ord는 항상 업데이트
        $item.attr('data-ui_ord', newOrder);
        
        // 변경된 두 항목에 대해서만 data-org_ord 업데이트
        if (index === index1 || index === index2) {
            var originalOrder = $item.attr('data-org_ord');
            if (originalOrder === undefined) {
                originalOrder = 'undefined';
            }
            $item.attr('data-org_ord', newOrder);
        }
    });
}

//저장 버튼 클릭 시 실행될 함수 (예시)
function saveNewOrder() {
    var changedItems = $('#applSbjt .item').filter(function() {
        return $(this).attr('data-ui_ord') == $(this).attr('data-org_ord');
    }).map(function() {
        return {
            YEAR: $(this).attr('data-year'),
            SMT: $(this).attr('data-smt'),
            SUBJECT_CD: $(this).attr('data-subjectCd'),
            DIVCLS: $(this).attr('data-divcls'),
            ORD: $(this).attr('data-ui_ord')
        };
    }).get();


    $.ajax({
		url: '/web/basket/updateOrder.do?mId=52',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			'changedItems' : changedItems
		}),
		success: function(data){
			if(data.result == 'DONE'){
				alert("정상적으로 처리되었습니다.")
				$('#cancelOrder').trigger('click');				
			}else{
				alert("정상적으로 처리되지 못하였습니다. 관리자에게 문의해주세요.");
				asdf(data.error);
			}
		}
	});
}


//예비수강신청 선택 일괄 담기
function sbjtContain(DOC_ID){		
	var html = '';
	var sId  = '';
	 var selectedSubjects = [];
		
	if(DOC_ID != null){ //단건 처리	
		sId = $("#sbInfo-"+DOC_ID);
        selectedSubjects.push({
            subjectCd: sId.attr('data-subjectCd'),
            subjectNm: sId.attr('data-subjectNm'),
            divcls: sId.attr('data-divcls')
        });		
	}else{				//다중건 처리	
		if($(".chk-before:checked").length == 0){
				alert("과목을 선택해주세요.");
				return false;
		}else{
			var chkIdx 	= $(".chk-before:checked");
			
			for(var i=0; i < chkIdx.length; i++){								
				sId = $("#sbInfo-"+chkIdx[i].value);
                selectedSubjects.push({
                    subjectCd: sId.attr('data-subjectCd'),
                    subjectNm: sId.attr('data-subjectNm'),
                    divcls: sId.attr('data-divcls')
                });
			}
		}
	}
	sukangSin(selectedSubjects);
}


// 예비수강신청 삭제
function delSbjt(DOC_ID){
	
	var html = '';
	var sId  = '';
	 var selectedSubjects = [];
		
	if(DOC_ID != null){ //단건 처리	
		sId = $("#applInfo-"+DOC_ID);
        selectedSubjects.push({
            subjectCd: sId.attr('data-subjectCd'),
            subjectNm: sId.attr('data-subjectNm'),
            divcls: sId.attr('data-divcls')
        });		
	}else{				//다중건 처리	
		if($(".chk-after:checked").length == 0){
				alert("과목을 선택해주세요.");
				return false;
		}else{
			var chkIdx 	= $(".chk-after:checked");
			
			for(var i=0; i < chkIdx.length; i++){								
				sId = $("#applInfo-"+chkIdx[i].value);
                selectedSubjects.push({
                    subjectCd: sId.attr('data-subjectCd'),
                    subjectNm: sId.attr('data-subjectNm'),
                    divcls: sId.attr('data-divcls')
                });
			}
		}
	}
	
	console.log(selectedSubjects)
	sukangDel(selectedSubjects);
}


//예비수강신청 - 신청
function sukangSin(selectedSubject){    
   	showLoading();
       $.ajax({
   		url: '/web/basket/sukangSin.do?mId=52',
   		contentType:'application/json',	
   		type: 'POST',
   		data: JSON.stringify({ 
               'SEL_SUBJECT_LIST' : selectedSubject
   		}),
   		success: function(data){
   			alert(data.resultMsg);   	
			hideLoading();
			renderApplList();
   		}
   	});
}

//예비수강신청 - 삭제
function sukangDel(selectedSubject){
   	showLoading();
    $.ajax({
		url: '/web/basket/sukangDel.do?mId=52',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
            'SEL_SUBJECT_LIST' : selectedSubject
		}),
		success: function(data){
			alert(data.resultMsg);   	
			hideLoading();
			renderApplList();
		}
	});
}

//브라우저 및 OS 정보 확인
function getSystemInfo(type) {
    var userAgent = navigator.userAgent;
    var os = "Unknown OS";
    var browser = "Unknown Browser";

    // OS 감지
    if (userAgent.indexOf("Win") > -1) os = "Windows";
    else if (userAgent.indexOf("Mac") > -1) os = "MacOS";
    else if (userAgent.indexOf("Linux") > -1) os = "Linux";
    else if (userAgent.indexOf("Android") > -1) os = "Android";
    else if (userAgent.indexOf("like Mac") > -1) os = "iOS";

    // 브라우저 감지
    if (userAgent.indexOf("Chrome") > -1) browser = "Chrome";
    else if (userAgent.indexOf("Safari") > -1) browser = "Safari";
    else if (userAgent.indexOf("Firefox") > -1) browser = "Firefox";
    else if (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident/") > -1) browser = "Internet Explorer";
    else if (userAgent.indexOf("Edge") > -1) browser = "Edge";
    else if (userAgent.indexOf("Opera") > -1 || userAgent.indexOf("OPR") > -1) browser = "Opera";

    // 매개변수에 따라 OS 또는 브라우저 정보 반환
    if (type === 'os') {
        return os;
    } else if (type === 'browser') {
        return browser;
    }
}

//랜덤 키 생성
function generateRandomKey() {
    var array = new Uint8Array(32);
    window.crypto.getRandomValues(array);
    return Array.from(array, function(byte) {
        return ('0' + byte.toString(16)).slice(-2);
    }).join('');
}

// 패스워드 암호화
function encryptPassword(password, secretKey, callback) {
    var encoder = new TextEncoder();
    var data = encoder.encode(password);
    
    var keyBytes = new Uint8Array(secretKey.match(/[\da-f]{2}/gi).map(function (h) {
        return parseInt(h, 16)
    }));
    
    window.crypto.subtle.importKey(
        "raw",
        keyBytes,
        { name: "AES-GCM" },
        false,
        ["encrypt"]
    ).then(function(cryptoKey) {
        var iv = window.crypto.getRandomValues(new Uint8Array(12));
        
        window.crypto.subtle.encrypt(
            { name: "AES-GCM", iv: iv },
            cryptoKey,
            data
        ).then(function(encryptedData) {
            var encryptedArray = new Uint8Array(encryptedData);
            var encryptedHex = Array.from(encryptedArray, function(byte) {
                return ('0' + byte.toString(16)).slice(-2);
            }).join('');
            var ivHex = Array.from(iv, function(byte) {
                return ('0' + byte.toString(16)).slice(-2);
            }).join('');
            callback(ivHex + encryptedHex);
        });
    });
}


function adjustLayout() {
	const modalBody = document.querySelector('.modal-xl .modal-body');
	const rightBox = modalBody.querySelectorAll('.box')[1];
	const itemCount = rightBox.querySelectorAll('.item').length;
	
	if (itemCount <= 3) {
	  modalBody.classList.add('few-items');
	} else {
	  modalBody.classList.remove('few-items');
	}
}

</script>
