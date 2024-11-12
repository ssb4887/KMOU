<%@ include file="../../include/commonTop.jsp"%>

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

console.log("${contextPath}/include/js/moment.js");

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

	// 탭 이동(메인에서 클릭한 페이지 - 학생설계전공, 해시태그, 나이찜, 장바구니)
	$($tabTarget).trigger('click');
	

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




/* ====================================================================================
 * 
 *
 */


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

</script>
