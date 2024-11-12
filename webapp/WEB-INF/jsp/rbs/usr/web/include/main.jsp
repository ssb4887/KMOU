<%@ include file="commonTop.jsp" %>

<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tabulator.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/Chart.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/chartjs-plugin-doughnutlabel.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/all.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/moment.js"/>"></script>

<script type="text/javascript">
/* ====================================================================================
 *
 * Declare
 */
var page_prop 			= {};

// Element
const $chartGPA 		= 'chartGPA';
const $chartCDT 		= 'chartCDT';
const $chartCdtDetail 	= 'chartCdtDetail';
const $chartScholAmt 	= 'chartScholAmt';
const $chartCoreDiag 	= 'chartCoreDiagnosis';
const $chartMajorDiag 	= 'chartMajorDiagnosis';
const $tableMajorReq 	= '#tableMajorReq';
const $tableMinoreq 	= '#tableMinorReq';

// URL
const URL_AI_RECOMMEND_SUBJECT 	= "<c:out value="${URL_AI_RECOMMEND_SUBJECT}"/>";	// 교과목(전공&교양) 정보 Ajax URL
const URL_AI_RECOMMEND_COURSE 	= "<c:out value="${URL_AI_RECOMMEND_COURSE}"/>";	// 비교과 정보 Ajax URL
const URL_AI_RECOMMEND_CALENDAR = "<c:out value="${URL_AI_RECOMMEND_CALENDAR}"/>";	// 학사일정 Ajax URL

// Object : Slick Option
const _SLICK_OPTS = {
	dots				: true, 
	arrows				: true,
	autoplay			: false,
  	slidesToShow		: 4,
  	slidesToScroll      : 4,
	autoplaySpeed		: 2000,
	responsive			: [
							{ breakpoint: 1399.99 , settings:{ slidesToShow:3, slidesToScroll:3,}},
					  	   	{ breakpoint: 766.99  , settings:{ slidesToShow:2, slidesToScroll:2,}}
						  ]
}

//Object : Chart Option
const _CHART_OPTS_TOOLTIPS = {
	backgroundColor		: "rgb(0,0,0,.6)",
    bodyFontColor		: "#fff",
    titleMarginBottom	: 10,
    titleFontColor		: '#fff',
    titleFontSize		: 14,
    borderColor			: '#dddfeb',
    borderWidth			: 0,
    xPadding			: 15,
    yPadding			: 15,
    displayColors		: true,
    intersect			: false,
    mode				: 'single',
    caretPadding		: 5,
    callbacks			: {
        					label: function(tooltipItem, data) {
        						return " " + comma(Number(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]));
        					}
    					  }
}

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


const _USER = {
	"NAME" 				: "<c:out value="${USER.NM}"/>",				// 이름
	"STD_NO" 			: "<c:out value="${USER.STUDENT_NO}"/>",		// 학번
	"DEPT_CD" 			: "<c:out value="${USER.DEPT_CD}"/>",			// 학부(과) 코드
	"DEPT_NM" 			: "<c:out value="${USER.DEPT_NM}"/>",			// 학부(과) 명
	"GRADE" 			: "<c:out value="${USER.GRADE}"/>",				// 학년
	"ISU_SMT"			: "<c:out value="${USER.ISU_SMT}"/>",			// 이수 학기
	"HAKJUK_ST_CODE" 	: "<c:out value="${USER.HAKJUK_ST_CODE}"/>",	// 학적상태
	"HAKJUK_NM" 		: "<c:out value="${USER.HAKJUK_ST_NM}"/>",		// 학적상태명
	"MAJOR_CD" 			: "<c:out value="${USER.MAJOR_CD}"/>",			// 전공 코드
	"MAJOR_NM" 			: "<c:out value="${USER.MAJOR_NM}"/>",			// 전공 명
	"BU_NM"				: "<c:out value="${USER.BU_NM}"/>",				// 부전공
	"BOK_NM"			: "<c:out value="${USER.BOK_NM}"/>",			// 복수전공
	"FUSE_NM"			: "<c:out value="${USER.FUSE_NM}"/>",			// 융합전공
	"LINK_NM"			: "<c:out value="${USER.LINK_NM}"/>",			// 연계전공
	"MICRODEGREE_NM"	: "<c:out value="${USER.MICRODEGREE_NM}"/>",	// 디그리전공
	"SUF_STS_NM"  		: "<c:out value="${USER.SUF_STS_NM}"/>",		// 연게전공(학부생 - 그외 '전일제/시간제')
	"PRSBJ_CDT"   		: "<c:out value="${USER.PRSBJ_CDT}"/>"			// 융합전공(학부생 - 그외 '선수과목이수학점')
};

const _CDT = {
	STD_NO 		 		: "<c:out value="${USER_CDT.STUDENT_NO}"/>",
	CUM_CDT 	 		: "<c:out value="${USER_CDT.CUM_CDT}"/>",
	GRADUATE_CDT 		: "<c:out value="${USER_GOAL_CDT[0].GRADUATE_CDT}"/>" != '0' ? "<c:out value="${USER_GOAL_CDT[0].GRADUATE_CDT}"/>" : "<c:out value="${USER_GOAL_CDT[1].GRADUATE_CDT}"/>"
};

const _GPA = {
	STD_NO 				: "<c:out value="${USER_GPA[0].STUDENT_NO}"/>",
	GPA_AVG 			: "<c:out value="${USER_GPA[0].GPA_AVG}"/>",
	SCR_AVG 			: "<c:out value="${USER_GPA[0].TOTAL_PERCENT}"/>"
};

let bookmarkList;

var _SEARCH = {
		MID 			: "",					// 메뉴번호
		TOPIC			: "",					// 검색 키
};

var yearCheck = "";
var smtCheck = "";

$(function(){	 
	
	/*
	 * INIT ----------------------------------------------
	 */	
	// 페이지 초기화
	pageCommonInit();

	/* 
     * EVENT ----------------------------------------------
     */   
     // 전공/교양 Toggle
     $('#chkComDiv').on('click', function(){
		if(page_prop.slickSubject != null){
			page_prop.slickSubject.slick('slickRemove', null, null, true);
        	page_prop.slickSubject.slick('unslick');
		}    	 
    	 
		if($(this).is(":checked") == true){
			// #교양
			$('#recomm-majr-tab').closest('li').addClass('hidden');
			
			if($('#recomm-majr-tab').hasClass('active')){
				$('#recomm-home-tab').removeClass('active');
				$('#recomm-home-tab').addClass('active');
				$('#recomm-home').addClass('active').addClass('show');
				$('#recomm-majr').removeClass('active').removeClass('show');
			}else{
				$('#recomm-majr-tab').removeClass('active');
			}
			
			if(isNullOrBlank(page_prop.AI_MINOR)){
				requestAjaxSubject();
			}else{
				createSlideCardMajorAndMinor(page_prop.AI_MINOR);
			}
 		}else{
 			// #전공
 			$('#recomm-majr-tab').closest('li').removeClass('hidden');
 			$('#recomm-majr-tab').removeClass('hidden');
 			if($('#recomm-majr-tab').hasClass('active')){
 				$('#recomm-majr-tab').removeClass('active');
 			}
 			
 			if(isNullOrBlank(page_prop.AI_MAJOR)){
 				requestAjaxSubject();
			}else{
				createSlideCardMajorAndMinor(page_prop.AI_MAJOR);
			}
 		}
     });
	
  	// 현재/과거 Toggle
     $('#chkPeriod').on('click', function(){
		if(page_prop.slickNonSubject != null){
			page_prop.slickNonSubject.slick('slickRemove', null, null, true);
        	page_prop.slickNonSubject.slick('unslick');
		}    	 

		if($(this).is(":checked") == true){
			page_prop.aiCoursePeriod = "2";
			
			// 비교과 과거 추천
			if(isNullOrBlank(page_prop.AI_COURSE)){
				requestAjaxCourse();
			}else{
				createSlideCardPresentAndPast(page_prop.AI_COURSE);
			}
 		}else{
 			page_prop.aiCoursePeriod = "1";
 			
 			// 비교과 현재 추천
 			if(isNullOrBlank(page_prop.AI_COURSE)){
 				requestAjaxCourse();
			}else{
				createSlideCardPresentAndPast(page_prop.AI_COURSE);
			}
 		}
     });
  	
  	// 성적 숨기기/보이기
  	$(".textbx").on('click',function(){
  		if($(".m_chart1").hasClass("maskbx")){
  			$('#chartGPA').parent().parent().removeClass('blur');
  			$('#chartCdtDetail').parent().parent().removeClass('blur');
	  		$(".m_chart1").removeClass("maskbx");
	  		$("#cdtDetailView").removeClass("hidden");  	
	  		createChartGPA(_GPA, $chartGPA);
	  		createChartCdtDetail($chartCdtDetail);
  		}else{
	  		$(".m_chart1").addClass("maskbx");
	  		$("#"+$chartGPA).addClass("hidden");  	
	  		$("#cdtDetailView").addClass("hidden");  	
  		}
  	});
  	
    $(document).click(function(e){ 
        if (!$(e.target).is('#searchCondition')) { 
            $("#listSc").attr("style", "display:none;");
            $("#searchCondition").removeClass("rtt");
        }
    });
    
    $("#searchCondition").click(function(){
        $("#listSc").attr("style", "display:block;");
        $(this).addClass("rtt");
    });
    
    //추천검색어
    animateSearchSuggestions();
    
	$('.btn-example').click(function () {
		var $href = $(this).attr('href');
		layer_popup($href);
	});
	
	$(document).mouseup(function (e) {
		if ($(".dimBg").has(e.target).length === 0) {
			$(".dim-layer").hide();
			// Remove scrollLock class from body
			$('body').removeClass('scrollLock');
		}
	});
	
    // 전공 검색 입력창에서 엔터를 누르면 검색
    $("input[name=main_search]").on("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            mainSearch();
        }
    });
    
    // 현재 학기 정보 가져오기
    getCurInfo().then(result => {
		yearCheck = result.YEAR;
		smtCheck = result.SMT;
	}).catch(error => {
		
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
// 	createChartCDT(_CDT, $chartCDT);
	
	// 나의 평균성적
// 	createChartGPA(_GPA, $chartGPA);
	
	// 학점 상세
// 	createChartCdtDetail($chartCdtDetail);
	
	// 장학내역
	createChartScholAmt($chartScholAmt);
	
	// 핵심역량
	createChartCoreDiagnosis($chartCoreDiag);
	
	// 전공역량
	createChartMajorDiagnosis($chartMajorDiag);
	
	// 전공필수 이수현황
// 	createTableMajorReq($tableMajorReq);
	
	// 교양필수 이수현황
// 	createTableMinorReq($tableMinoreq);
	
	// 교과목 전공/교양 추천
 	requestAjaxSubject();
 	
 	// 비교과 현재/과거 추천
 	requestAjaxCourse();
 	
 	// 학사 일정
 	setRangeMonth().then(buildCalendar);
 	
 	// 커뮤니티(공지사항, 취업정보)
 	getBoardList("10373,10002722");
 	
}
 

// 기본정보
function obj2elem(object){

	const deptNM 	= object.DEPT_NM;
	const majorNM 	= object.MAJOR_NM;
	const buNM		= object.BU_NM; 			//부전공
	const bokNM		= object.BOK_NM; 			//복수전공
	const fuseNM	= object.FUSE_NM; 			//융합전공
	const linkNM	= object.LINK_NM; 			//연계전공
	const mdgNM		= object.MICRODEGREE_NM; 	//디그리전공
	const prsbjCdt  = object.PRSBJ_CDT;
	const sufStsNM	= object.SUF_STS_NM;
	const grade 	= object.GRADE;
	const hakjukNM 	= object.HAKJUK_NM;
	const name 		= object.NAME;
	const stdNO 	= object.STD_NO;
	const isuSmt 	= Number(object.ISU_SMT) + 1;
	
	
// 	const $major 	= deptNM + '<span class="d-inline-block ms-1">' + majorNM + '</span>';
// // 	const $sub 		= '<span class="d-inline-block ms-1">' + ((!isNullOrBlank(buNM)) ? buNM : (!isNullOrBlank(bokNM)) ? bokNM : '-') + '</span>';
// 	const $sub = '<span class="d-inline-block ms-1">' + (!isNullOrBlank(buNM) ? buNM : !isNullOrBlank(bokNM) ? bokNM : !isNullOrBlank(fuseNM) ? fuseNM : !isNullOrBlank(linkNM) ? linkNM : '-') + '</span>';
// 	const $fuseLink	= '<span class="d-inline-block ms-1">' + ((!isNullOrBlank(fuseNM)) ? fuseNM : (!isNullOrBlank(linkNM)) ? linkNM : '-') + '</span>';
// 	const $grade	= '<li>' + grade + '학년</li><li>' + ((hakjukNM == "졸업" || hakjukNM == "휴학") ? hakjukNM : isuSmt + '학기') + '</li>';
// 	$('#myInfoGrade').text(grade + "학년")
	$('.student-nm').text(name);			// 이름
// 	$('#myInfoStdNo').text(stdNO);			// 학번
// 	$('#myInfoGradeSmt').append($grade);	// 학년+(재학여부/현재학기)
// 	$('#myDeptMajor').html($major);		// 메인 카드 > 학과+전공
// 	$('#myMajor').html($major);				// 우측 사이드 카드 > 학과+전공
// 	$('#mySub').html($sub);					// 우측 사이드 카드 > 복수/부전공
// 	$('#mySub').html($fuseLink);			// 우측 사이드 카드 > 융합/연계전공

// 	-- BU_NM : 부전공
// 	-- BOK_NM : 복수전공
// 	-- FUSE_NM : 융합전공
// 	-- LINK_NM : 연계전공
// 	-- MICRODEGREE_NM : 디그리과정	
	
	var $myInfoHtml = '<li>' +
							 '<i id="myInfoGrade">' + grade + '학년' + '</i>' +
							 '<h3 class="name student-nm" id="myInfoName">' + name + '</h3>' +
							 '<span class="stu_numb" id="myInfoStdNo">' + stdNO + '</span>' +
							 '<ul class="school_year" id="myInfoGradeSmt"><li>' + grade + '학년</li><li>' + ((hakjukNM == "졸업" || hakjukNM == "휴학") ? hakjukNM : isuSmt + '학기') + '</li></ul>' + 
						 '</li>' +
						 '<li><h3>전공</h3><p id="myDeptMajor">' + deptNM +  '<span class="d-inline-block ms-1">' + majorNM + '</span></p></li>' +
						 '</li>';
		$myInfoHtml += (isNullOrBlank(buNM)   ? '' : '<li><h3>부전공</h3><p id="myDeptMajor"><span class="d-inline-block ms-1">' + buNM + '</span></p></li>'); 
		$myInfoHtml += (isNullOrBlank(bokNM)  ? '' : '<li><h3>복수전공</h3><p id="myDeptMajor"><span class="d-inline-block ms-1">' + bokNM + '</span></p></li>'); 
		$myInfoHtml += (isNullOrBlank(fuseNM) ? '' : '<li><h3>융합전공</h3><p id="myDeptMajor"><span class="d-inline-block ms-1">' + fuseNM + '</span></p></li>');
		$myInfoHtml += (isNullOrBlank(linkNM) ? '' : '<li><h3>연계전공</h3><p id="myDeptMajor"><span class="d-inline-block ms-1">' + linkNM + '</span></p></li>'); 
		$myInfoHtml += (isNullOrBlank(mdgNM)  ? '' : '<li><h3>디그리과정</h3><p id="myDeptMajor"><span class="d-inline-block ms-1">' + mdgNM + '</span></p></li>');
		
	
	$("#myInfo").append($myInfoHtml);
					 
	
	// Calendar
	page_prop.today = new Date();
	page_prop.date = new Date();
	page_prop.eventStr = [];
	page_prop.eventEnd = [];
	page_prop.data = "";
	page_prop.doMonth = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth(), 1);
	page_prop.lastDate = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth()+1, 0);
	
	
}


// 나의 취득학점(도넛 차트)
function createChartCDT(object, ctx){
	if(object == null) return;
	
	var cumCDT 	= (typeof(object.CUM_CDT) != 'undefined' && object.CUM_CDT != '') ? object.CUM_CDT : 0;
	var goalCDT = (typeof(object.GRADUATE_CDT) != 'undefined' && object.GRADUATE_CDT != '') ? object.GRADUATE_CDT : 0;

	if(isNullOrBlank(cumCDT) && isNullOrBlank(goalCDT)) {
		cumCDT = 0;
		goalCDT = 0;
	}
	
	const data = {
		labels : ['취득','미취득'],
		datasets: [{
			data : [cumCDT, Number(goalCDT)-Number(cumCDT)],
			backgroundColor : ["#0F3E8E", "#E6E9EF"], 
			borderWidth : 1,
		}]	
	};
	
	const opts = {
		maintainAspectRatio: false,
		cutoutPercentage: 70,
		reverse: true,
		legend : { display: false, position:'bottom' },
		plugins : {
			doughnutlabel : {
				labels: [{
		    		text: cumCDT + ' /' + goalCDT,
		    		font: { size: '15' },
		    		color: '#003377'
			    },{
			    	text: '(학점)',
			    	font: { size: '15' },
		    		color: '#1D46D7'
			    }]
			}
		},
		tooltips: _CHART_OPTS_TOOLTIPS,
	}
	
	try{
		page_prop.chartCDT = new Chart(document.getElementById(ctx).getContext('2d'), {
			type : "doughnut",
			data : data,
			options : opts,
		});
	}
	catch(error){ asdf(error);}
	finally{ return; }
}


// 나의 평균성적(도넛 차트)
function createChartGPA(object, ctx){
    var gpaAvg  = Number(object.GPA_AVG); //평균 학점
    var scrAvg  = Number(object.SCR_AVG); //평균 점수
    
    if(isNullOrBlank(gpaAvg) || isNullOrBlank(scrAvg)) {
        gpaAvg = 0;
        scrAvg = 0;
    }
    
    // 반올림을 사용하여 정확한 값 계산
    const remainingGPA = Number((4.5 - gpaAvg).toFixed(2));
    const remainingSCR = Number((100 - scrAvg).toFixed(2));
    
    const data = {
    		labels: ['평균 학점', '남은 학점'],
        datasets: [{
            data : [gpaAvg, remainingGPA],
            backgroundColor : ["#0F3E8E", "#E8E9EF"], 
            borderWidth : 1,
        },{
            data : [scrAvg, remainingSCR],
            backgroundColor : ["#81a3e6", "#E6E9EF"], 
            borderWidth : 1,
        }]   
    };
	
	const opts = {
		maintainAspectRatio: false,
		cutoutPercentage: 70,
		reverse: true,
		borderRadius : 20,
		legend : { display: false, position:'top'},	
		plugins : {
			doughnutlabel : {
				labels: [{
		    		text: gpaAvg + ' (학점)',
		    		font: { size: '15' },
		    		color: '#000'
			    },{
			    	text: scrAvg + ' (점수)',
		    		font: { size: '15' },
		    		color: '#000'
			    }]
			}
		},		
		tooltips: _CHART_OPTS_TOOLTIPS,
	}
	
	try{
		page_prop.chartGPA = new Chart(document.getElementById(ctx).getContext('2d'), {
			type : "doughnut",
			data : data,
			options : opts,
		});
		
	}

	
	catch(error){ asdf(error);}
	finally{ return; }
}


// 학점상세(바 차트)
function createChartCdtDetail(ctx){
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
            backgroundColor : ["#B8FFCB","#B0F1FF","#C5CEFF","#C588FF","#FF8BCE","#DC67AB","#C367DC","#A167DC","#8A6EDC","#6F86D4","#6794DC"], 
            borderWidth : 0.5,
            borderRadius: {
                topLeft: 10,
                topRight: 10
            }
        }]    
    };
    
    const opts = {
        responsive: true,
        maintainAspectRatio: false,
        cutoutPercentage: 50,
        reverse: false,
        borderSkipped: false,
        legend : { display: false, position:'bottom' },    
        scales : {
            xAxes : [{ barPercentage: 0.5, gridLines : { color: "#ffffff00" }, ticks:{ fontColor:'white' }, lineWidth:1,}],
            yAxes : [{ gridLines : { color: "#ffffff00" }, ticks:{ beginAtZero: true, fontColor:'white', max: 4.5 }, }]
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
                ctx.font = "16px 'sans-serif'";
                ctx.fillStyle = 'white';
                ctx.fillText(opts.plugins.noDataMessage.message, width / 2, height / 2.5);
                ctx.restore();

                return true;  // 차트 렌더링 중지
            }
        }
    });
    
    try {
        page_prop.chartCdtDetail = new Chart(document.getElementById(ctx).getContext('2d'), {
            type : "bar",
            data : data,
            options : opts,
        });
    }
    catch(error) { 
        asdf(error);
    }
    finally { 
        return; 
    }
}


// 장학내역(바 차트)
function createChartScholAmt(ctx){
	var valueList 	= [],
		labelList 	= [];
	
	"<c:forEach items="${USER_SCHOL_AMT}" var="item">"
// 	valueList.push("${item.SCHOL_AMT}");
// 	"<c:if test="${item.SCHOL_AMT == 0}">"
// 	labelList.push("-");
// 	"</c:if>"
	"<c:if test="${item.SCHOL_AMT > 0}">"
	valueList.push("${item.SCHOL_AMT}");
	labelList.push("${item.YEAR.substring(2,4)}` ${item.SMT_NM.substring(0,1)}");
	"</c:if>"
	"</c:forEach>"
	
	if(valueList.length < 1) {
		$('#'+ ctx).addClass('hidden');
		$('#'+ ctx).next().removeClass('hidden');
		return;
	}

	const data = {
		labels : labelList,
		datasets: [{
			data : valueList,
			label : labelList,
			backgroundColor : ["#8FAEFE","#7F9EEE","#6F8EDE","#5F7ECE","#4F6EBE","#3F5EAE","#2F4E9E","#1F3E8E","#0F2E7E","#0F1E6E"], 
			borderWidth : 0.5,
 			yAxisID : 'y-axis'
		}]	
	};
	
	const opts = {
		responsive: true,
		maintainAspectRatio: false,
		legend : { display: false, position:'top' },	
		scale : {
			xAxes : [{ barPercentage: 0.5, gridLines : { color: "#ffffff00" }, }],
			yAxes : [{ 
				display : true,
				id: "y-axis",
	            type: "linear",
	            position: "left",
	            gridLines : { color: "#ffffff00" },
	            scaleLabel: { display: false },
	            ticks:{
					beginAtZero: true,
					userCallback: function(value, index, values){
						const idx = (index == values.length-2) ? 2 : 3
						return String(value).slice(value.length-1, idx);
					}
				}
			}]
		},
		tooltips: _CHART_OPTS_TOOLTIPS,
	}
	
	try{
		page_prop.chartScholAmt = new Chart(document.getElementById(ctx).getContext('2d'), {
			type : "bar",
			data : data,
			options : opts,
		});
	}
	catch(error){ asdf(error);}
	finally{ return; }
}


//핵심역량진단(레이더 차트)
function createChartCoreDiagnosis(ctx){
    var dataByYearMonth = {};
    var labelList = [];

    "<c:forEach items="${USER_CORE_DIAGNOSIS}" var="item">"
    var yearMonth = "${item.YEAR}";
    var year = yearMonth.substring(0, 4);
    var label = "${item.ESSENTIAL1_NM}";
    var point = parseFloat("${item.POINT}");

    if (!dataByYearMonth[yearMonth]) {
        dataByYearMonth[yearMonth] = {
            data: [],
            label: year
        };
    }
    dataByYearMonth[yearMonth].data.push(point);

    if (!labelList.includes(label)) {
        labelList.push(label);
    }
    "</c:forEach>"

    if (Object.keys(dataByYearMonth).length < 1) {
        $('#'+ ctx).addClass('hidden');
        $('#'+ ctx).next().removeClass('hidden');
        return;
    }

    const colors = [
        'rgba(0, 180, 216, 0.6)',   // #00b4d8 with 60% opacity
        'rgba(0, 119, 190, 0.6)',   // #0077be with 60% opacity
        'rgba(2, 62, 138, 0.6)',    // #023e8a with 60% opacity
        'rgba(3, 4, 94, 0.6)',      // #03045e with 60% opacity
        'rgba(0, 150, 199, 0.6)',   // #0096c7 with 60% opacity
        'rgba(72, 202, 228, 0.6)',  // #48cae4 with 60% opacity
        'rgba(144, 224, 239, 0.6)'  // #90e0ef with 60% opacity
    ];
    const datasets = Object.entries(dataByYearMonth).map(([yearMonth, yearData], index) => ({
        data: yearData.data,
        label: yearData.label,
        backgroundColor: colors[index % colors.length],
        borderColor: colors[index % colors.length].replace('0.6', '1'), // 테두리는 불투명하게
        borderWidth: 2,
        pointRadius: 0.5
    }));


    const data = {
        labels: labelList,
        datasets: datasets
    };

    const opts = {
        responsive: true,
        maintainAspectRatio: false,
        legend: { 
            display: true, 
            position: 'bottom',
            labels: {
                fontColor: '#ffffff',
                generateLabels: function(chart) {
                    const originalLabels = Chart.defaults.global.legend.labels.generateLabels(chart);
                    return originalLabels.map((label, index) => {
                        const originalYearMonth = Object.keys(dataByYearMonth)[index];
                        label.text = label.text;
                        return label;
                    });
                }
            }
        },
        scale: {
            ticks: {
                beginAtZero: false,
                min: 0,
                max: 45,
                stepSize: 9,
                display: false
            },
            pointLabels: { fontColor: "#ffffff", fontSize: 12 },
            gridLines: { color: "#ffffff", lineWidth: 0.5 }
        },
        tooltips: {
            backgroundColor: "rgb(255,255,255,.6)",
            bodyFontColor: "#333333",
            titleMarginBottom: 10,
            titleFontColor: '#333333',
            titleFontSize: 14,  
            bodyFontSize: 16,   
            borderColor: '#ffffff',
            borderWidth: 0,
            xPadding: 15,
            yPadding: 15,
            displayColors: true,
            intersect: false,
            mode: 'index',
            caretPadding: 5,
            callbacks: {
                title: function(tooltipItem, data) {
                    return data.labels[tooltipItem[0].index];
                },
                label: function(tooltipItem, data) {
                    const originalYearMonth = Object.keys(dataByYearMonth)[tooltipItem.datasetIndex];
                    const year = originalYearMonth.substring(0, 4);
                    const month = originalYearMonth.substring(4);
                    return year + '년 ' + month + '월: ' + comma(tooltipItem.value) + '점';
                }
            }
        },
    }

    try {
        page_prop.chartCoreDiagnosis = new Chart(document.getElementById(ctx).getContext('2d'), {
            type: "radar",
            data: data,
            options: opts,
        });
    }
    catch(error) { console.error(error); }
    finally { return; }
}

// 전공역량진단(레이더 차트)
function createChartMajorDiagnosis(ctx){
    var dataByYearMonth = {};
    var labelList = [];

    "<c:forEach items="${USER_MAJOR_DIAGNOSIS}" var="item">"
    var yearMonth = "${item.YEAR}";
    var year = yearMonth.substring(0, 4);
    var label = "${item.COMPETENCY1_NM.trim()}";
    var point = parseFloat("${item.POINT}");

    if (!dataByYearMonth[yearMonth]) {
        dataByYearMonth[yearMonth] = {
            data: [],
            label: year
        };
    }
    dataByYearMonth[yearMonth].data.push(point);

    if (!labelList.includes(label)) {
        labelList.push(label);
    }
    "</c:forEach>"

    if (Object.keys(dataByYearMonth).length < 1) {
        $('#'+ ctx).addClass('hidden');
        $('#'+ ctx).next().removeClass('hidden');
        return;
    }

    const colors = [
        'rgba(0, 79, 197, 0.6)',    // #004FC5 with 60% opacity
        'rgba(0, 119, 190, 0.6)',   // #0077be with 60% opacity
        'rgba(2, 62, 138, 0.6)',    // #023e8a with 60% opacity
        'rgba(3, 4, 94, 0.6)',      // #03045e with 60% opacity
        'rgba(0, 150, 199, 0.6)',   // #0096c7 with 60% opacity
        'rgba(72, 202, 228, 0.6)',  // #48cae4 with 60% opacity
        'rgba(144, 224, 239, 0.6)'  // #90e0ef with 60% opacity
    ];
    const datasets = Object.entries(dataByYearMonth).map(([yearMonth, yearData], index) => ({
        data: yearData.data,
        label: yearData.label,
        backgroundColor: colors[index % colors.length],
        borderColor: colors[index % colors.length].replace('0.6', '1'), // 테두리는 불투명하게
        borderWidth: 2,
        pointRadius: 0.5
    }));

    const data = {
        labels: labelList,
        datasets: datasets
    };

    const opts = {
        startAngle: 108, //오각형 한각별 108도  
        responsive: true,
        maintainAspectRatio: false,
        legend: { 
            display: true, 
            position: 'bottom',
            labels: {
                fontColor: '#ffffff',
                generateLabels: function(chart) {
                    const originalLabels = Chart.defaults.global.legend.labels.generateLabels(chart);
                    return originalLabels.map((label, index) => {
                        const originalYearMonth = Object.keys(dataByYearMonth)[index];
                        label.text = label.text;
                        return label;
                    });
                }
            }
        },
        scale: {
            ticks: {
                beginAtZero: false,
                min: 0,
                max: 20,
                stepSize: 4,
                display: false
            },
            pointLabels: { fontColor: "#ffffff", fontSize: 12, width: '50px' },
            gridLines: { color: "#ffffff", lineWidth: 0.5 }
        },
        tooltips: {
            backgroundColor: "rgb(0,0,0,.6)",
            bodyFontColor: "#ffffff",
            titleMarginBottom: 10,
            titleFontColor: '#ffffff',
            titleFontSize: 14,  
            bodyFontSize: 16, 
            borderColor: '#ffffff',
            borderWidth: 0,
            xPadding: 10,
            yPadding: 10,
            displayColors: true,
            intersect: false,
            mode: 'index',
            caretPadding: 5,
            callbacks: {
                title: function(tooltipItem, data) {
                    return data.labels[tooltipItem[0].index];
                },
                label: function(tooltipItem, data) {
                    const originalYearMonth = Object.keys(dataByYearMonth)[tooltipItem.datasetIndex];
                    const year = originalYearMonth.substring(0, 4);
                    const month = originalYearMonth.substring(4);
                    return year + '년 ' + month + '월: ' + comma(tooltipItem.value) + '점';
                }
            }
        },
    }

    try {
        page_prop.chartMajorDiagnosis = new Chart(document.getElementById(ctx).getContext('2d'), {
            type: "radar",
            data: data,
            options: opts,
        });
    }
    catch(error) { console.error(error); }
    finally { return; }
}


// 전공필수 이수현황(테이블)
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
			height			: "160px",
			placeholder		: "'개인별적용학점/교육과정' 이 <b>변경</b>되었습니다.</br>"
							  + "『종합정보시스템 > 미이수필수과목체크및확인』에서</br>"
							  + "<b>실행</b> 버튼을 눌러주세요. </br>"
							  + "<a class='go_tis px-2' href='https://tis.kmou.ac.kr/' title='종합정보시스템' target='_blank'>바로가기</a>",
	        cellHozAlign	: 'center',
	        cellVertAlign	: "middle",
	        data			: _DATASET,
	        index			: "STUDENT_NO",
	        paginationSize	: 100,
	
	        columns			: [ 							
	            { title		: "학년/학기", 		field: "GRADE_SMT", 	headerSort:false, 	width:65, },
	            { title		: "과목", 			field: "SUBJECT_NM", 	headerSort:false, },
	            { title		: "학점", 			field: "CDT_NUM", 		headerSort:false, 	width:60, },
	            { title		: "이수여부", 		field: "S_FLAG", 		headerSort:false, 	width:65,
	            								formatter:function(cell){ return makeCompleteBadge(cell.getData().S_FLAG);}, 
	            }, 
	        ],
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
			height			: "160px",
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
	    });
	}
	catch(error){ asdf(error);}
	finally{ return; }
}


// Request Ajax 교과목(전공/교양) 추천 
function requestAjaxSubject(){
	return new Promise(resolve => {
		
		$.ajax({
			type: 'post'
			, beforeSend: (request) => { request.setRequestHeader('Ajax', 'true'); }
			, url: URL_AI_RECOMMEND_SUBJECT
		 	, data: _USER
		 	, success: function(response){	
		 		page_prop.AI_MAJOR = response.AI_MAJOR;
		 		page_prop.AI_MINOR = response.AI_MINOR;
				$(".subjectLoaderText").remove();
				$(".subjectLoader").remove();
		 		if(!createSlideCardMajorAndMinor(page_prop.AI_MAJOR)){
		 			asdf("Failed to create slick. Check the data.");
		 			asdf("requestAjaxSubject() => createSlideCardMajorAndMinor()");
		 			asdf("Data => " + page_prop.AI_MAJOR);
		 		}				
		 		
		 		resolve();	
		 	}
			, error: function(request, error){ asdf(error); }
		});	
	});
}

// Request Ajax 비교과 추천 
function requestAjaxCourse(){
	return new Promise(resolve => {
		
		$.ajax({
			type: 'post'
			, beforeSend: (request) => { request.setRequestHeader('Ajax', 'true'); }
			, url: URL_AI_RECOMMEND_COURSE
		 	, data: _USER
		 	, success: function(response){	
		 		page_prop.AI_COURSE = response.AI_COURSE;
				$(".nonSubjectLoaderText").remove();
				$(".nonSubjectLoader").remove();
		 		if(!createSlideCardPresentAndPast(page_prop.AI_COURSE)){
		 			
		 			asdf("Failed to create slick. Check the data.");
		 			asdf("requestAjaxCourse() => createSlideCardPresentAndPast()");
		 			asdf("Data => " + page_prop.AI_COURSE);
		 		}				
		 		
		 		resolve();	
		 	}
			, error: function(request, error){ asdf(error); }
		});	
	});
}

// 교과목(전공/교양) Slick 생성
function createSlideCardMajorAndMinor(array){
	//찜 목록 가져오기(promise)		
	getBookmarkList()
	.then(data => {
		bookmarkList = data;
	});
	
	const $recommHome = $('#recomm-home .box_wrap');
	const $recommData = $('#recomm-data .box_wrap');
	const $recommSimi = $('#recomm-simi .box_wrap');
	const $recommPers = $('#recomm-pers .box_wrap');
	const $recommMajr = $('#recomm-majr .box_wrap');
	const $recommCore = $('#recomm-core .box_wrap');
	
	if(typeof(array) == 'undefined' || array == null) {
		$recommHome.next().removeClass('hidden');
		$recommData.next().removeClass('hidden');
		$recommSimi.next().removeClass('hidden');
		$recommPers.next().removeClass('hidden');
		$recommMajr.next().removeClass('hidden');
		$recommCore.next().removeClass('hidden');
		
		return false;	
	}
	
	var favorite1Count = 0		// 전체 건 수
		, favorite2Count = 0	// 졸업이수기반 건 수
		, favorite3Count = 0	// 유사사용자기반 건 수
		, favorite4Count = 0	// 개인선호도기반 건 수
		, favorite5Count = 0	// 전공능력기반 건 수
		, favorite6Count = 0;	// 핵심역량 건 수
		
	var flag_success = false;

	try{
		$recommHome.empty();
		$recommMajr.empty();
		$recommCore.empty(); 
		$recommData.empty();
		$recommSimi.empty();
		$recommPers.empty();
		
		$.each(array, function (index, item){
			var _class = "";
			
			switch(item.COMDIV_CODE){
				case "UE010021" : _class = 'tag_req'; break;
				case "UE010022" : _class = 'tag_sel'; break;
				case "UE010011" : _class = 'tag_ref'; break;
				case "UE010012" : _class = 'tag_bal'; break;
			}
			
			var method = String(item.METHOD_NUM)
						, rank = String(item.ORDER_NUM)
					 	, year = item.YEAR
					 	, smt = item.SMT
					 	, smtNM = item.SMT_NM
						, colgNM = item.COLG_NM
						, deptCD = item.DEPT_CD
					 	, deptNM = item.DEPT_NM
					 	, grade = (item.GRADE == '0' ? '전체' : item.GRADE)
					 	, subjectCD = item.SUBJECT_CD
					 	, subjectNM = item.SUBJECT_NM
					 	, comDivNM = item.COMDIV_NM
					 	, cdtNum = item.CDT_NUM
						, retakeYN = item.RETAKE_AVAILABLE_YN;
			
			var DOC_ID = subjectCD+'_'+deptCD+'_'+year+'_'+grade+'_'+smt;
			const action = "/web/sbjt/view.do"; 

			var $elem = '<div class="box"><a href="javascript:redirectSubjView(`'+ action +'`,`'+ subjectCD +'`,`'+ deptCD +'`,`'+ year +'`,`'+ smt +'`)" class="d-flex flex-column align-items-start gap-2">'
						+ '<div class="item_reco_sjt"><label class="subject_tag '+ _class +'">'+ comDivNM +'</label>'+(retakeYN==='Y'? '<span class="tag_re">재수강가능</span>' : '') +'</div>'
						+ '<strong class="title ellip_2 mb-2">'+ subjectNM +'</strong>'
						//+ '<span class="info d-flex flex-wrap gap-1">'
						+ '<span class="period"><b>학점</b><i>' + cdtNum + '학점</i></span>'
						+ '<span class="period"><b>이수학년</b>' + grade + '학년</span>'
						+ '<span class="period"><b>편성시기</b>'+ year +' - '+ smtNM +'</span>'
						+ '<span class="period"><b>개설학과</b>'+ colgNM 
						+ '<p class="peopleC"> '+ deptNM + '</p>' + '</span>'
						+ '<span class="period"><b>과목코드</b>'+ subjectCD +'</span>'
						//+ '</span>'
						+ '</a>'
						+ '<ul class="box2"><li><a href="javascript:" class="'+DOC_ID+'" onclick="likeChange(\''+DOC_ID+'\',\'sbjt\');">찜하기</a></li><li><a href="javascript:void(0);" onclick="getClassList(`' + subjectCD +'`,`'+ deptCD +'`,`'+ year +'`)" data-bs-toggle="modal" data-bs-target="#syllabusModal">장바구니</a></li></ul></div>';
						// 찜하기 선택시 <a href="#" class="ct">찜하기</a>로 변경

			switch(method){
				case "1": favorite1Count++; $recommHome.append($elem); break; // 전체
				case "2": favorite2Count++; $recommData.append($elem); break; // 졸업이수기반
				case "3": favorite3Count++; $recommSimi.append($elem); break; // 유사사용자기반
				case "4": favorite4Count++; $recommPers.append($elem); break; // 개인선호도기반
				case "5": favorite5Count++; $recommMajr.append($elem); break; // 전공능력기반
				case "6": favorite6Count++; $recommCore.append($elem); break; // 핵심역량기반
			}
						
		});
		
		asdf($recommMajr);
				
		flag_success = true;
	}catch(error){				
		asdf(error);
		flag_success = false;
	}finally{
		// 데이터 없으면 안내문구
		if(favorite1Count < 1) $recommHome.next().removeClass('hidden');
		else $recommHome.next().addClass('hidden');
		if(favorite2Count < 1) $recommData.next().removeClass('hidden');
		else $recommData.next().addClass('hidden');
		if(favorite3Count < 1) $recommSimi.next().removeClass('hidden');
		else $recommSimi.next().addClass('hidden');
		if(favorite4Count < 1) $recommPers.next().removeClass('hidden');
		else $recommPers.next().addClass('hidden');
		if(favorite5Count < 1) $recommMajr.next().removeClass('hidden');
		else $recommMajr.next().addClass('hidden');
		if(favorite6Count < 1) $recommCore.next().removeClass('hidden');
		else $recommCore.next().addClass('hidden');
		
		page_prop.slickSubject = $("#divRecommSubject .box_wrap").not('.slick-initialized').slick(_SLICK_OPTS);
		
		// Init Slick (교과목)
	 	$("#divRecommSubject .nav-recomm").on("click", function (){
	 		$('#divRecommSubject .box_wrap').slick('setPosition');
	 	});
		
		// 교과목 찜 세팅
        if (bookmarkList) {
            bookmarkList.forEach(function(item) {
                $("."+item.docId).addClass("ct");
            });
        }
		
		return flag_success;
	}
}

//찜 변경
function likeChange(docId, type){
		if(!$("."+docId).hasClass("ct")){			
			$.ajax({
				url: '/web/bookmark/insertBookmark.do?mId=37&menuFg='+type,
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    docId : docId
				}),
				success: function(data){
					$("."+docId).addClass("ct");
				}
			});
		} else {
			$.ajax({
				url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg='+type,
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    docId : docId
				}),
				success: function(data){
					$("."+docId).removeClass("ct");
				}
			});
		}
}

//찜 목록 조회
function getBookmarkList(){
	return new Promise((resolve, reject) => {		
		$.ajax({
			url: '/web/bookmark/getBookmarkList.do?mId=37',
			contentType:'application/json',	
			type: 'POST',
			data: JSON.stringify({ 
			    menuFg : 'all'
			}),
			success: function(data){			
				resolve(data.bookmarkList);
			},error:function() {
				reject("");
			}
		});
	})
	
}


// 비교과(현재/과거) Slick 생성
function createSlideCardPresentAndPast(array){
	//찜 목록 가져오기(promise)		
	getBookmarkList()
	.then(data => {
		bookmarkList = data;
	});
	
	
	const $recommHome = $('#recomm-home2 .box_wrap');
	const $recommPCore = $('#recomm-pcore2 .box_wrap');
	const $recommWCore = $('#recomm-wcore2 .box_wrap');
	const $recommSimi = $('#recomm-simi2 .box_wrap');
	const $recommPers = $('#recomm-pers2 .box_wrap');
	
	if(typeof(array) == 'undefined' || array == null) {
		$recommHome.next().removeClass('hidden');
		$recommPCore.next().removeClass('hidden');
		$recommWCore.next().removeClass('hidden');
		$recommSimi.next().removeClass('hidden');
		$recommPers.next().removeClass('hidden');
		
		return false;	
	}
	
	var favorite1Count = 0		// 전체 건 수
		, favorite2Count = 0	// 핵심역량(강점)기반 건 수
		, favorite3Count = 0	// 핵심역량(약점)기반 건 수
		, favorite4Count = 0	// 유사사용자기반 건 수
		, favorite5Count = 0;	// 개인선호도기반 건 수
		
	var flag_success = false;
		

	try{
		$.each(array, function (index, item){
			if(isNullOrBlank(page_prop.aiCoursePeriod)){			// 토글(현재/과거)로 변경한게 아닐 경우 '현재' 데이터만
				if(item.PERIOD != '1') return;
			}else{
				if(item.PERIOD != page_prop.aiCoursePeriod) return;	// 토글(현재/과거)로 변경한 경우 'page_prop.aiCoursePeriod'
			}
	
			var method = item.METHOD
				, pidx = String(item.PIDX)
				, tidx = String(item.TIDX)
				, category1 = item.CATEGORY1_NM
				, category2 = isNullOrBlank(item.CATEGORY2_NM) ? "" : " > " + item.CATEGORY2_NM
				, type = isNullOrBlank(item.TYPE_NM) ? "-" : item.TYPE_NM 
			 	, topicTitle = isNullOrBlank(item.topic_title) ? "-" : item.TOPIC_TITLE
				, programTitle = item.PROGRAM_TITLE
			 	, startDt = item.START_DATE
			 	, endDt = item.END_DATE
			 	, signinStartDt = item.SIGNIN_START_DATE
			 	, signinEndDt = item.SIGNIN_END_DATE
			 	, signinLimit = item.SIGNIN_LIMIT
			 	, applicant = item.APPLICANT
			 	, abstractTitle = item.ABSTRACT_TITLE 
			 	, cover = item.COVER 
			 	, updatedDate = item.UPDATED_DATE 
				, participant = item.PARTICIPANT
				, signinLimit = item.SIGNIN_LIMIT
				, point		  = item.POINT;
					
			var DOC_ID = pidx+'_'+tidx; 
			var particiRate = 0;
			if(signinLimit > 0){				
				particiRate = (Number(participant) / Number(signinLimit) * 100) >= 100 ? 100 : (Number(participant) / Number(signinLimit) * 100);
			}
			
				
			const action = "/web/nonSbjt/view.do"; 
				
// 			var $elem = '<div class="box"><a href="'+ link +'" title="'+ programTitle +'" class="d-flex flex-column align-items-start gap-2">'
//			var $elem = '<div class="box"><a href="javascript:redirectNonSubjView(`'+ action +'`,`'+ pidx +'`,`'+ tidx +'`)" title="'+ programTitle +'" class="d-flex flex-column align-items-start gap-2">'
//						+ '<label class="other_tag tag_adv">'+ type +'</label>'
//						+ '<strong class="title ellip_2 mt-2">'+ programTitle +'</strong>'
//						+ '<span class="badge badge-pink mb-2 fw-light">'+ category1 +' <em class="fst-normal category"> '+ category2 +'</em></span>'
//						+ '<span class="period"><b>모집 .</b> <em class="fst-normal">'+ signinStartDt +' ~ '+ signinEndDt +'</em></span>'
//						+ '<span class="period"><b>운영 .</b> <em class="fst-normal">'+ startDt +' ~ '+ endDt +'</em></span>'
//						+ '<span class="personnel d-flex flex-row justify-content-between w-100 mt-3">'
//						+ '<strong>모집인원</strong>'
//						+ '<span><strong>'+ applicant +'</strong> / '+ signinLimit +'명 지원중</span>'
//						+ '</span></a></div>';
			var $elem = '<div class="box"><a href="javascript:redirectNonSubjView(`'+ action +'`,`'+ pidx +'`,`'+ tidx +'`)" title="'+ programTitle +'" class="d-flex flex-column align-items-start gap-2">'
						+ '<div class="nsjimg_bx">' 
						+ '<div class="acon_bx"><span>' + 'ⓐ' + '</span><p>' + point + '점</p></div>' 
						+ '<img src="https://cts.kmou.ac.kr/attachment/view/' + cover + '/cover.jpg?ts=' + updatedDate  + '" onerror="this.src=\'${contextPath}${imgPath}/nonSbjt_no_image.png\'" alt=""></div>'
//						+ '<label class="other_tag tag_adv">'+ type +'</label>'
						+ '<strong class="title ellip_2 mt-2">'+ programTitle +'</strong>'
						+ '<p class="period">'+ abstractTitle +'</p>'
//						+ '<span class="badge badge-pink mb-2 fw-light">'+ category1 +' <em class="fst-normal category"> '+ category2 +'</em></span>'
						+ '<span class="period peoplespan"><b>모집기간</b> <em class="fst-normal">'+ signinStartDt +' ~ '+ signinEndDt +'</em></span>'
						+ '<span class="period peoplespan"><b>강의기간</b> <em class="fst-normal">'+ startDt +' ~ '+ endDt +'</em></span>'
						+ '<span class="period peoplespan"><b>모집인원</b> <div class="nonsbjt_list_inboxgraph"><span><strong>'+ participant +'</strong> / '+ signinLimit +'명 지원중</span>'
						+ '<p class="peopleC"><span style="width:'+Number(particiRate)+'%;"></span></p></div>'
						+ '</span></a>'
						+ '<ul class="box2"><li><a href="javascript:" class="'+DOC_ID+'" onclick="likeChange(\''+DOC_ID+'\',\'nonSbjt\');">찜하기</a></li></ul></div>';
						
			
			switch(method){
				case "1": favorite1Count++; $recommHome.append($elem); break; // 전체
				case "2": favorite2Count++; $recommPCore.append($elem); break; // 핵심역량(강점)기반
				case "3": favorite3Count++; $recommWCore.append($elem); break; // 핵심역량(약점)기반
				case "4": favorite4Count++; $recommSimi.append($elem); break; // 유사사용자기반
				case "5": favorite5Count++; $recommPers.append($elem); break; // 개인선호도기반
			}
		});		                
		
		flag_success = true;
	}catch(error){		
		
		asdf(error);
		flag_success = false;
	}finally{
		// 데이터 없으면 안내문구
		if(favorite1Count < 1) $recommHome.next().removeClass('hidden');
		else $recommHome.next().addClass('hidden');
		if(favorite2Count < 1) $recommPCore.next().removeClass('hidden');
		else $recommPCore.next().addClass('hidden');
		if(favorite3Count < 1) $recommWCore.next().removeClass('hidden');
		else $recommWCore.next().addClass('hidden');
		if(favorite4Count < 1) $recommSimi.next().removeClass('hidden');
		else $recommSimi.next().addClass('hidden');
		if(favorite5Count < 1) $recommPers.next().removeClass('hidden');
		else $recommPers.next().addClass('hidden');
	
   		page_prop.slickNonSubject = $("#divRecommNonCourse .box_wrap").not('.slick-initialized').slick(_SLICK_OPTS);
	    
		// Init Slick (비교과)
	 	$("#divRecommNonCourse .nav-recomm").on("click", function (){
	 		$('#divRecommNonCourse .box_wrap').slick('setPosition');
	 	});
		
		// 비교과 찜 세팅
        if (bookmarkList) {
            bookmarkList.forEach(function(item) {
                $("."+item.docId).addClass("ct");
            });
        }
		
		
	 	return flag_success;
	}

}

//교과 상세페이지 이동
function redirectSubjView(action, subjCD, deptCD, year, smt){
	var form = document.sbjtView;
	form.SUBJECT_CD.value = subjCD;
	form.DEPT_CD.value = deptCD;
	form.YEAR.value = year;
	form.SMT.value = smt;
	form.action = action;
	form.submit();
}


// 비교과 상세페이지 이동
function redirectNonSubjView(action, idx, tidx){
	var form = document.nonSbjtView;
	form.idx.value = idx;
	form.tidx.value = tidx;
	form.action =action;
	form.submit();
}

/* 교과목 장바구니 목록 불러오기 */
function getClassList(subjectCd, deptCd, year){
	
	if(year == null || year == ""){
		var dt = new Date();
		year = dt.getFullYear();
	}
	const varAction = "/web/sbjt/classList.json?mId=32&YEAR="+ year +"&SUBJECT_CD=" + subjectCd + "&DEPT_CD=" + deptCd;
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		async:true, 
  		success:function(result){
  			const varItemObj = $("#mainBasList");
  			
  			varItemObj.empty();
  			var list = result.list;
  			
  			/* 개설이력(3년) */
			if(list) {
				/* $.each(list, function(i, list){
					console.log(list)
					var DOC_ID = list.SUBJECT_CD+'_'+list.DEPT_CD+'_'+list.YEAR+'_'+list.GRADE+'_'+list.SMT
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
					year_class = "ongo";
					
					var innerHtml = '';

					innerHtml += '<div class="item d-flex flex-row gap-2 align-items-center position-relative" id="' + DOC_ID + '">' +
								 '<div class="form-check">' +
								 '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="cartChk_' + i +'" name="cartChk" data-year="' + list.YEAR + '" data-smt="' + list.SMT + '" data-subjectCd="' + list.SUBJECT_CD + '" data-divCls="' + list.DIVCLS + '" data-deptCd="' + list.DEPT_CD + '" data-empNo="'+list.EMP_NO+'" data-roomTime="'+list.ROOM_TIME+'" data-subjectNm="'+list.SUBJECT_NM+'">' +
								 '<label class="blind" for="cartChk">강좌선택</label></div>'
					
					
					innerHtml += '<div><div class="d-flex flex-row justify-content-between align-items-center">' +
								 '<ul class="cate d-flex flex-row gap-1 flex-wrap">' +
								 '<li class="' + sbjt_class + '">' + list.COMDIV_CODE_NAME + '</li>' +
								 '<li class="' + year_class + '">' + list.YEAR + '학년도 ' + list.SMT_NAME + '</li>' + 
								 '<ol class="d-flex btn_wrap align-items-center gap-2 ">' + 
								 '<li><button type="button" class="border-0 btn_dele rounded-circle" onclick="deleteBasket(\'' + list.YEAR + '\',\'' + list.SMT + '\',\''+ list.SUBJECT_CD + '\',\'' + list.DEPT_CD + '\',\'' + list.DIVCLS + '\',\'' + list.SUBJECT_NM + '\', \'' + list.EMP_NO + '\', \'' + DOC_ID + '\')"></button></li></ol></div>' +
								 '<h5 class="fw-semibold text-truncate w-100">' + list.SUBJECT_NM + '</h5>' +
								 '<ul class="info_wrap d-flex flex-column">' +
								 '<li class="d-flex flex-wrap"><dl class="d-flex flex-row align-items-start gap-2"><dt>전공</dt><dd>' + list.DEPT_NM + '</dd></dl><dl class="d-flex flex-row align-items-start gap-2"><dt>교수</dt><dd>' + list.EMP_NM + '</dd></dl><dl class="d-flex flex-row align-items-start gap-2"><dt>수강학년</dt><dd>' + list.COM_GRADE + '학년</dd></dl></li>' +
								 '<li class="d-flex flex-wrap"><dl class="d-flex flex-row align-items-start gap-2"><dt>시간</dt><dd>' + list.ROOM_TIME + '</dd></dl><dl class="d-flex flex-row align-items-start gap-2"><dt>정원</dt><dd>' + list.STUDENT_CNT + '명</dd></dl></li>' + 
								 '</ul></div></div>'
								 
					varItemObj.append(innerHtml);
				}); */
				
  				$.each(list, function(i, item){
  					var empNo = item.EMP_NO;
  					var subjectNm = item.SUBJECT_NM;
  					var subjectCd = item.SUBJECT_CD;
  					var deptCd = item.DEPT_CD;
  					var empNm = (typeof(item.EMP_NM) != "undefined" && item.EMP_NM != "") ? item.EMP_NM : '-';
  					var year = item.YEAR;
  					var grade = item.GRADE;
  					var smt = item.SMT;
  					var divcls = item.DIVCLS;
  					var varCon = '<div class="item p-4">\n';
  					var studentCnt = (typeof(item.RSTR_MCNT) != "undefined" && item.RSTR_MCNT != "") ? item.RSTR_MCNT + ' 명' : '-';
  					// 관심강좌에 alrea 추가
  					
  					
  					varCon += '<ul class="cate d-flex flex-row gap-1 flex-wrap">\n';
  					if(item.COMDIV_CODE == 'UE010074'){
  						varCon += '<li class="nati_navy">교양과목</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010011'){ 
  	  					varCon += '<li class="nati_navy">교양필수</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010012'){
  	  					varCon += '<li class="nati_navy">교양선택</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010080'){
  	  					varCon += '<li class="nati_navy">일반선택</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010024'){
  	  					varCon += '<li class="nati_navy">전공기초</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010021'){
  	  					varCon += '<li class="nati_navy">전공필수</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010022'){
  	  					varCon += '<li class="nati_navy">전공선택</li>\n';
  	  				} else if(item.COMDIV_CODE == 'UE010031'){
  	  					varCon += '<li class="nati_navy">교직</li>\n';
  	  				}
  					varCon += '</ul>\n';
  					varCon += '<h5 class="fw-semibold ellip_2 my-3">' + item.SUBJECT_NM + '</h5>\n';
  					varCon += `<li class="col-3 vari_link shop_link" id="\${year}_\${smt}_\${subjectCd}_\${deptCd}_\${divcls}_\${empNo}">\n`;
  					varCon += `<a href="javascript:void(0);" class="d-flex flex-column align-items-center gap-2" onclick="cartAddRemove('\${year}', '\${smt}', '\${subjectCd}', '\${deptCd}', '\${divcls}', '\${subjectNm}', '\${empNo}','\${year}_\${smt}_\${subjectCd}_\${deptCd}_\${divcls}_\${empNo}');">\n`;
  					varCon += `<i class="rounded-circle d-flex justify-content-center align-items-center"><img src="../images/modal_ico_fill_sho.png" id="fillSho_\${year}_\${smt}_\${subjectCd}_\${deptCd}_\${divcls}_\${empNo}" alt="카트아이콘"/><img src="../images/modal_ico_b_sho.png" id="bSho_\${year}_\${smt}_\${subjectCd}_\${deptCd}_\${divcls}_\${empNo}" alt="카트아이콘"/></i>\n`;
  					varCon += '<span class="fw-semibold text-center">장바구니</span>\n';
  					varCon += '</a>\n';
  					varCon += '</li>\n';
  					varCon += '<div class="simply_info d-flex flex-row flex-wrap">\n';
  					varCon += '<dl class="d-flex flex-row gap-1 align-items-center">\n';
  					varCon += '<dt><img src="../images/ico_b_loc.png" alt="장소아이콘"></dt>\n';
  					varCon += '<dd class="text-secondary">' + item.COLG_NM + '</dd>\n';
  					varCon += '<td class="name text-start text-lg-center align-middle">\n';
  					varCon += '</dl>\n';
  					varCon += '<dl class="d-flex flex-row gap-1 align-items-center">\n';
  					varCon += '<dt><img src="../images/ico_b_tim.png" alt="시간아이콘"></dt>\n';
  					varCon += '<dd class="text-secondary">'+ item.YEAR + '년 ' + item.GRADE + '학년 ' + item.SMT_NAME + ' '+ item.ROOM_TIME + '</dd>';
  					varCon += '</dl>\n';
  					varCon += '</div>\n';
  					varCon += '<div class="p-3 info_box mt-3">\n';
  					varCon += '<ol class="d-flex flex-wrap">\n';
  					varCon += '<li class="d-flex flex-row col-12 col-sm-8 col-md-12 col-lg-8"><b class="d-inline-block">수강대상학과</b>' + item.DEPT_NM + '</li>\n';
  					varCon += '<li class="d-flex flex-row col-12 col-sm-4 col-md-12 col-lg-4"><b class="d-inline-block">정원</b>' + studentCnt + '</li>\n';
  					varCon += '<li class="d-flex flex-row col-12 col-sm-8 col-md-12 col-lg-8"><b class="d-inline-block">담당교수</b>' + empNm + '</li>\n';
  					varCon += '<li class="d-flex flex-row col-12 col-sm-4 col-md-12 col-lg-4"><b class="d-inline-block">학년</b>' + item.GRADE + '학년</li>\n';
  					varCon += '</ol>\n';
  					varCon += '</div>\n';
  					
  					varCon += '<ul class="link_wrap d-flex flex-wrap mt-4">\n';
  					
  					varCon += '</ul>\n';
  					
  					
  					varItemObj.append(varCon);
  					
  					getBasketCart(year,smt,subjectCd,deptCd,divcls,empNo)
  					.then(data => {
  						if (!data || data.length === 0) {
  							var id = year + "_" + smt + "_" + subjectCd + "_" + deptCd + "_" + divcls + "_" + empNo;
  							$("#bSho_"+id).attr('style','display:block;');
							$("#fillSho_"+id).attr('style','display:none;');
    					}
  						data.forEach(function(basket){
  							var id = year + "_" + smt + "_" + subjectCd + "_" + deptCd + "_" + divcls + "_" + empNo;
  							$("#fillSho_"+id).attr('style','display:block;');
							$("#bSho_"+id).attr('style','display:none;');
							$("#"+id).addClass("alrea");
  						});
  					})

  					.catch(error =>{
  						//console.error('Error fetching basket cart data:', error);
  					});
  				});
  			}
			
  			return false;
  		}, 
  		error:function(request,error){
  			alert("장바구니 목록 실패");
  		}
  	});
}

//장바구니
function cartAddRemove(year, smt, subjectCd, deptCd, divcls, sbjtKorNm, empNo, id){
	if(yearCheck != year){
		console.log(yearCheck);
		console.log(year);
		alert("현재년도가 아닙니다.");
		return false;
	}
	if(smtCheck != smt){
		console.log(smtCheck);
		console.log(smt);
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
				$("#fillSho_"+id).attr('style','display:block;');
				$("#bSho_"+id).attr('style','display:none;');
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
				$("#bSho_"+id).attr('style','display:block;');
				$("#fillSho_"+id).attr('style','display:none;');
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


// 월 범위 설정
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

// 이전달 일정 Action (2월에 대한 예외처리 할 것)
function prevCalendar() {
	const getDate = (page_prop.today.getDate() == '31') ? page_prop.today.getDate() - 1 : page_prop.today.getDate();
	page_prop.today = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth() - 1, getDate);
	page_prop.doMonth = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth(), 1);
	page_prop.lastDate = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth()+1, 0);
	
	setRangeMonth().then(buildCalendar);
}

// 다음달 일정 Action(2월에 대한 예외처리 할 것)
function nextCalendar() {
	const getDate = (page_prop.today.getDate() == '31') ? page_prop.today.getDate() - 1 : page_prop.today.getDate();
	page_prop.today = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth() + 1, getDate);
	page_prop.doMonth = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth(), 1);
	page_prop.lastDate = new Date(page_prop.today.getFullYear(), page_prop.today.getMonth()+1, 0);
	
	setRangeMonth().then(buildCalendar);
}

// 달력 생성
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
							tooltip += "<br>⦁ "+item.title;
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

// 일정 시작일 관리
function getEventStr(date, tit){
	page_prop.eventStr.push(date);
	return page_prop.eventStr;
}

// 일정 종료일 관리
function getEventEnd(date, tit){
	page_prop.eventEnd.push(date);
	return page_prop.eventEnd;
}

// 월별 일정 리스트 생성
function getMonthList(year, month, day, action){
	return new Promise(function(resolve, reject){
		
		var month = (page_prop.today.getMonth() + 1) < 10 ? "0" + (page_prop.today.getMonth() + 1) : (page_prop.today.getMonth() + 1);
		
		$.ajax({
			type:'POST', 
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			url: URL_AI_RECOMMEND_CALENDAR,
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
	
						html 	+= '<dl class="school_sch">'
		                    	+'		<dt class="d-flex flex-row gap-3"><span>'+sMonth+'.'+sDate+'</span><span>'+eMonth+'.'+eDate+'</span></dt>'
		                    	+'		<dd class="text-truncate">'+title+'</dd>'
		                		+'	</dl>';
					}else{
						html 	+= '<dl class="school_sch">'
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

/* 커뮤니티 게시글 조회 */
function getBoardList(data) {
	var bbsList = data.split(",");
	var varAction = "/web/main/getBoardList.json?mId=1&BBS_LIST=" + bbsList;
	
	$.ajax({
  		type:'POST',
  		beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
  		dataType:'json', 
  		url:varAction, 
  		data:{
  			bbsList : bbsList
  		},
  		async:true, 
  		success:function(result){
  			
  			var varItemObj = $("#commu-home");
  			
  			varItemObj.empty();
  			
  			var list = result.BOARD_LIST;
  			if(list) {
  				$.each(list, function(i, item){
  					var nttCn = (typeof(item.NTT_CN) == 'undefined' || item.NTT_CN == '') ? "-" : item.NTT_CN;
  					if(nttCn != "-"){
  						nttCn = nttCn.replaceAll(/<[^>]*>?/g, '');
  					}
  					var regDt = item.REG_DT;
  					var bbsNm = "";
  					var bbsLink = "";
  					switch(item.BBS_ID){   /* 추후 학사안내 및 장학정보 게시판도 연결이 된다면 조건 세분화 필요 */
  						/* 공지사항 */
  						case 10373 	  : bbsNm = "공지사항";
  									    bbsLink = "/web/board/view.do?mId=39&NTT_SN=" + item.NTT_SN;
  									   	break;
  						/* 학사안내(공유게시글) */
  						case 11786 	  : bbsNm = "공지사항";
  										bbsLink = "/web/board/view.do?mId=39&NTT_SN=" + item.NTT_SN;
						   	  		    break;
						/* 장학정보(공유게시글) */
  						case 10004365 : bbsNm = "공지사항";
  										bbsLink = "/web/board/view.do?mId=39&NTT_SN=" + item.NTT_SN;
						   	 	 	  	break;
						/* 취업정보 */
  						case 10002722 : bbsNm = "취업정보";
  										bbsLink = "/web/board/view.do?mId=43&NTT_SN=" + item.NTT_SN;
  									  	break;
  						/* 학생성장지원실(공유게시글) */
  						case 10002387 : bbsNm = "취업정보";
  										bbsLink = "/web/board/view.do?mId=43&NTT_SN=" + item.NTT_SN;
  									  	break;
  						/* 학생성장지원실(공유게시글) */
  						case 10002388 : bbsNm = "취업정보";
  										bbsLink = "/web/board/view.do?mId=43&NTT_SN=" + item.NTT_SN;
  									  	break;
  					}
  					
  					
  					var varCon ="<li >";
  					varCon +='<a href="' + bbsLink + '" title="게시물 제목" class="">';
  					varCon +='<strong class="comm-tag d-inline-block me-1 notice">' + bbsNm + '</strong>';
  					varCon +='<b>' + item.NTT_SJ + '</b>';
  					varCon +='<p>' + nttCn + '</p>';
  					varCon +='<span>' + regDt + '</span>';
  					varCon +='</a>';
  					varCon +='</li>';
  					
  					varItemObj.append(varCon);
  					
  				})
  				
  			} 
			
  			return false;
  		}, 
  		error:function(request,error){
  			alert("게시판 조회 실패");
  			/* fn_ajax.checkError(request); */
  			//alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
  		}
  	});
}


function to_date(date){
	const yyyyMMdd = String(date);
	const sYear = yyyyMMdd.substring(0,4);
	const sMonth = yyyyMMdd.substring(4,6);
	const sDate = yyyyMMdd.substring(6,8);
	
    return new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
}


/* ----------------------------------------------------------------------------------------------------- */
/* -------------------------------------- COMMON UTILITY FUNCTION -------------------------------------- */
/* ----------------------------------------------------------------------------------------------------- */ 
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
 * @description Convert escape code
 * @param {String} '&#034'
 * > '"'
 */
function unescapeSpecialChar(str) {
	if(!str) return "";

	return str
		.replace(/&#038;/g, '&')
		.replace(/&#060;/g, '<')
		.replace(/&lt;/g, '<')
		.replace(/&#062;/g, '>')
		.replace(/&gt;/g, '>')
		.replace(/&#039;/g, "'")
		.replace(/&#034;/g, '"');
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

function layer_popup(el) {
	var $el = $(el);
	var isDim = $el.prev().hasClass('dimBg');

	if (isDim) {
		$('.dim-layer').fadeIn();
	} else {
		$el.fadeIn();
	}

	// Add scrollLock class to body
	$('body').addClass('scrollLock');

	//var $elHeight = $el.outerHeight();
	//var $elWidth = $el.outerWidth();
	//var docHeight = $(window).height();
	//var docWidth = $(window).width();

	if ($elHeight < docHeight || $elWidth < docWidth) {
		$el.css({
			marginTop: -$elHeight / 2,
			marginLeft: -$elWidth / 2
		});
	} else {
		$el.css({
			top: 0,
			left: 0
		});
	}

	$el.find('a.btn-layerClose').click(function () {
		isDim ? $('.dim-layer').fadeOut() : $el.fadeOut();
		// Remove scrollLock class from body
		$('body').removeClass('scrollLock');
		return false;
	});

	$('.dimBg').click(function () {
		$('.dim-layer').fadeOut();
		// Remove scrollLock class from body
		$('body').removeClass('scrollLock');
		return false;
	});
}


function goOceanCts(gourl){
	var token = '';
	var url = 'https://cts.kmou.ac.kr/ko/process/coursemos/loginToken'
	
	$.ajax({
		url: '/web/nonSbjt/getToken.do?mId=34',
		contentType:'application/json',	
		type: 'POST',
		success: function(data){			
			token = data.token;
			
			var queryString = '?token='+token+'&gourl='+gourl;
			url += queryString;
			window.open(url,'_blank');
		},error:function() {
			alert('비교과 접속에 실패하였습니다. 관리자에게 문의해주세요.');
		}
	});
}

 window.onload = function(){
	$(document.body).tooltip({
		selector: "[data-bs-toggle='tooltip']"
	});
 }
 
 
 
</script>
