<%@ include file="../../../include/commonTop.jsp"%>

<%-- <script type="text/javascript" src="<c:out value="${contextPath}/include/js/jquery-1.9.1.min.js"/>"></script> --%>
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
var page_prop = {};

// Elements
const $chartGPA = 'chartGPA';
const $chartCDT = 'chartCDT';
const $chartCdtDetail = 'chartCdtDetail';
const $tableCumCDT = '#tableCumCDT';
const $tableSubjectCDT = '#tableSubjectCDT';
const $tableRecHist = '#tableRecHist';
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
	
	// 누계성적조회
	createTableCumCDT($tableCumCDT);
	
	// 과목별성적조회 
	createTableSubjectCDT($tableSubjectCDT);
	
	// 학적변동내역
	createTableRecordHistory($tableRecHist);
	
	// 해시태그
	setListHashtag();
	
	// 탭 이동(메인에서 클릭한 페이지 - 학생설계전공, 해시태그, 나이찜, 장바구니)
	$($tabTarget).trigger('click');
	
	// 나의 찜 탭
// 	MyLoveList();	//이동근 주석
	
	// 장바구니 탭
	BasketList();
	
	//찜 항목별 카운트
	getMyBookmarkCount();
	hideLoading();
	
	//교과목(전공·교양)	
	$(".bookmarkTab").removeClass("active");
	$("#sbjtTab").addClass("active");	
	getSbjtList(1);
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
	const $grade	= '<li class="sch_y"><span>' + grade + '학년</span> / <span>' + ((hakjukNM == "졸업") ? hakjukNM : isuSmt + '학기') + '</span></li>';

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
	const cumCDT = object.CUM_CDT;
	const goalCDT = object.GRADUATE_CDT;
	
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
	            	return " " + Number(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]);
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

// 나의 평균성적(도넛차트)
function createChartGPA(object){
	const gpaAvg = object.GPA_AVG; //평균 학점
	const scrAvg = object.SCR_AVG; //평균 점수
	
	const data = {
		labels : ['평균',''],
		datasets: [{
			data : [gpaAvg, 4.50 - gpaAvg],
			backgroundColor : ["#0F3E8E", "#F6F9FF"], 
			borderWidth : 1,
		},{
			data : [scrAvg, 100 - scrAvg],
			backgroundColor : ["#81a3e6", "#F6F9FF"], 
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
	        callbacks: {
	            label: function(tooltipItem, data) {
	                return " " + Number(data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index]).toFixed(1);
	            }
	        }
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
			yAxes : [{ gridLines : { color: "#ffffff00" }, ticks:{ beginAtZero: true, }, }]
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
	}
	
	page_prop.chartCdtDetail = new Chart(document.getElementById($chartCdtDetail).getContext('2d'), {
		type : "bar",
		data : data,
		options : opts,
	});
}

// 학기별성적(테이블)
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
        layout			: "fitColumns",
		height			: "200px",
        placeholder		: "There is no data viewed.",
        cellHozAlign	: 'center',
        cellVertAlign	: "middle",
        data			: _DATASET,
        index			: "YEAR_SMT",
//         frozenRows		: 1,
        paginationSize	: 100,
        
        rowFormatter	: (row) => setRowStyle(row, '#FEFCE2'),

        columns			: [ 							
            { title		: "학년도/학기", 	field: "YEAR_SMT", 			headerSort:false, },
            { title		: "신청학점", 		field: "REQ_CDT", 			headerSort:true, },
            { title		: "취득학점", 		field: "GAIN_CDT", 			headerSort:true, }, 
            { title		: "평균학점", 		field: "GPA_AVG", 			headerSort:true, },
            { title		: "백분율", 		field: "TOTAL_PERCENT", 	headerSort:true, },
            { title		: "석차", 			field: "DEPT_RANK", 		headerSort:true, },
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
        placeholder		: "There is no data viewed.",
        cellHozAlign	: 'center',
        cellVertAlign	: "middle",
        data			: _DATASET,
        index			: "YEAR_SMT",
        paginationSize	: 100,

        columns			: [ 							
            { title		: "학년도/학기", 	field: "YEAR_SMT", 			headerSort:false, },
            { title		: "과목", 			field: "SUBJECT_NM", 		headerSort:false, },
            { title		: "교과구분",	 	field: "COMDIV_NM", 		headerSort:true, 
            								formatter:function(cell){ 
            									return makeComDivBadge(cell.getData().COMDIV_NM);
            								}, 	
            },
            { title		: "강좌구분", 		field: "CLASS_NM", 			headerSort:true, },
            { title		: "개설학부(과)", 	field: "SUF_DEPT_NM", 		headerSort:false, },
            { title		: "교수", 			field: "EMP_NM", 			headerSort:false, },
            { title		: "학점", 			field: "CDT_NUM", 			headerSort:true, },
            { title		: "환산등급", 		field: "CONV_MAG", 			headerSort:true, },
            { title		: "점수", 			field: "SCR", 				headerSort:true, },
            { title		: "평점", 			field: "GPA", 				headerSort:true, },
        ],
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
		height			: "130px",
        placeholder		: "There is no data viewed.",
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
    });
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
	  
	if(val == '교양선택') 		return $comDivBadge0.clone('true')[0];
    if(val == '교양필수') 		return $comDivBadge1.clone('true')[0];
    if(val == '전공기초') 		return $comDivBadge2.clone('true')[0];
    if(val == '전공선택') 		return $comDivBadge3.clone('true')[0];
    if(val == '전공필수') 		return $comDivBadge4.clone('true')[0];
    if(val == '일반선택') 		return $comDivBadge5.clone('true')[0];
    if(val == '기초선택') 		return $comDivBadge6.clone('true')[0];
    if(val == '기초필수') 		return $comDivBadge7.clone('true')[0];
    if(val == '융합전공선택') 	return $comDivBadge8.clone('true')[0];
    if(val == '융합전공필수') 	return $comDivBadge9.clone('true')[0];
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
			
			$.each(count, function(i, item){
				$("#"+ item.menuFg + "Count").text(item.cnt);
			});
			
		}
	});
}



function setBookmarkBottomContents(contentsHtml, tabFg, data){	
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
			var prevPageNo = firstPageNoOnPageList;
		} else{
			var prevPageNo = firstPageNo;
		}
		pageCon += `<li class="page-item page-first"><a href="javascript:getSbjtList(\${firstPageNo});" class="page-link" title="first"><span class="blind">first</span><img src="<c:out value="${contextPath}${imgPath}/arr_2x_gray.png"/>" alt="처음으로 화살표" /></a></li>`;
		pageCon += `<li class="page-item page-prev"><a href="javascript:getSbjtList(\${prevPageNo});" class="page-link" title="prev"><span class="blind">prev</span><img src="<c:out value="${contextPath}${imgPath}/arr_bottom_gray.png"/>" alt="이전으로 화살표" /></a></li>`;
  		
	}
	
	
	for(var pageNo = firstPageNoOnPageList; pageNo <= lastPageNoOnPageList; pageNo++){
		if(pageNo == currentPageNo){
			pageCon += `<li class="page-item active"><a class="page-link" href="javascript:getSbjtList(\${pageNo});" title="\${pageNo}페이지" value="\${pageNo}">\${pageNo}</a></li>`;
		} else{
			pageCon += `<li class="page-item"><a class="page-link" href="javascript:getSbjtList(\${pageNo});" title="\${pageNo}페이지" value="\${pageNo}">\${pageNo}</a></li>`;
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
			            '<li class="' + categoryClass + '">' + list.COMDIV_NM + '</li>';
	
		            bookmarkList += '<li class="name_of_class"><span>' + list.COLG_NM + '</span><span>' + list.DEPT_NM + '</span>';
		            bookmarkList += '</li>';
	
			        bookmarkList += '</ul>' +
			            '<h5 class="title mb-1">' +
			            '<a href="javascript:" onclick="sbjtView(\''+list.SUBJECT_CD+'\',\''+deptKey+'\',\''+list.YEAR+'\',\''+list.SMT+'\')" title="' + list.SUBJECT_NM + '" class="d-block  fw-semibold">' + list.SUBJECT_NM + '</a> <span>' + (list.SUBJECT_ENM === undefined ? '' : list.SUBJECT_ENM) + '</span>' +
			            '</h5>' +
			            '<li class="like_container on_red" id="'+DOC_ID+'">'+
			            '<span class="link_cnt text-end">'+
						'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'sbjt\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						'</span>'+
						'</li>'+
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
			    bookmarkList = setBookmarkBottomContents(bookmarkList, 'Lec', data);
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
	
	var form = document.viewForm;
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
	
			        var courseCategory = list.COM_GRADE === '0' ? '전체학년' : list.COM_GRADE + '학년';
			        var DOC_ID = list.SUBJECT_CD +'_'+list.DEPT_CD+'_'+list.YEAR+'_'+list.COM_GRADE+'_'+list.SMT+'_'+list.DIVCLS+'_'+list.EMP_NO
	
			        bookmarkList += '<div class="item border d-flex flex-row align-items-center">' +
			            '<div class="form-check d-flex justify-content-center pe-2">' +
			            '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="bmkChk_' + index + '" name="bmkChk">' +
			            '<label class="blind" for="bmkChk_' + index + '">강좌선택</label>' +
			            '</div>' +
			            '<div>' +
			            '<div class="cursor_pointer" onclick="getLectureView(\''+list.SUBJECT_CD+'\',\''+list.DEPT_CD+'\',\''+list.EMP_NO+'\',\''+list.YEAR+'\',\''+list.DIVCLS+'\');">' +
			            '<ul class="cate d-flex flex-row gap-1 flex-wrap">' +
			            '<li class="' + categoryClass + '">' + list.COMDIV_CODE_NAME + '</li>';
	
	// 		        if (list.YEAR === currentYear) {
	// 		            bookmarkList += '<li class="ongo">' + list.YEAR + '학년' + list.SMT_NAME + '</li>';
	// 		        } else {
	// 		            bookmarkList += '<li class="past">' + list.YEAR + '학년' + list.SMT_NAME + '</li>';
	// 		        }
	
			        bookmarkList += '</ul>' +
			            '<h5 class="fw-semibold ellip_2 mt-2 mb-2">' + list.SUBJECT_NM + '</h5>' +
			            '<li class="like_container on_red" id="'+DOC_ID+'">'+
			            '<span class="link_cnt text-end">'+
						'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'lecture\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						'</span>'+
						'</li>'+
			            '<div class="simply_info d-flex flex-row flex-wrap">' +
			            '<dl class="d-flex flex-row gap-1 align-items-center">' +
			            '<dt><img src="../images/ico_b_loc.png" alt="장소아이콘" /></dt>' +
			            '<dd class="text-secondary">' + list.COLG_NM + '</dd>' +
			            '</dl>' +
			            '<dl class="d-flex flex-row gap-1 align-items-center">' +
			            '<dt><img src="../images/ico_b_tim.png" alt="시간아이콘" /></dt>' +
			            '<dd class="text-secondary">' + list.SISU + '시간(이론 ' + list.WTIME_NUM + '시간 / 실습 '+ list.PTIME_NUM +  '시간)'+'</dd>' +
			            '</dl>' +
			            '</div>' +
			            '<div class="info_box mt-2">' +
			            '<ol class="d-flex flex-wrap">' +
			            '<li class="d-flex flex-wrap col-12 col-sm-8 col-md-12 col-lg-8">' +
			            '<b class="d-inline-block">수강대상학과</b>' + list.DEPT_NM +
			            '</li>' +
			            '<li class="d-flex flex-row col-12 col-sm-4 col-md-12 col-lg-4">' +
			            '<b class="d-inline-block">정원</b>' + list.STUDENT_CNT +
			            '</li>' +
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
			    bookmarkList = setBookmarkBottomContents(bookmarkList, 'Sbjt', data);
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
function getLectureView(subjectCd, deptCd, empNo, year, divcls){
	
	var varAction = "/web/sbjt/lectureView.json?mId=32&SUBJECT_CD=" + subjectCd + "&DEPT_CD=" + deptCd + "&EMP_NO=" + empNo + "&YEAR=" + year + "&DIVCLS=" + divcls;
	
	
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
		             '<h5 class="d-flex flex-column align-items-start gap-2 mb-2">' +
		             '<span class="tag_adv">' + list.D_DAY + '</span>' +
		             '<a href="" class="ellip_2">' + list.PROGRAM_TITLE + '</a>' +
		             '</h5>' +
		            '<li class="like_container on_red" id="'+DOC_ID+'">'+
		            '<span class="link_cnt text-end">'+
					'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'nonSbjt\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
					'</span>'+
					'</li>'+	             
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
				        '<a href="javascript:" onclick="profView(' + DOC_ID + ')" title="' + list.EMP_NM + '" class="simply_info d-flex flex-row flex-sm-column flex-md-row align-items-end align-items-sm-center align-items-md-end border-bottom pb-4 gap-3">' +
				        '<span class="photo_box d-inline-block rounded-circle overflow-hidden">' +
				        '<img src="https://www.kmou.ac.kr/' + list.TEA_FILE_PATH + '" alt="교수님사진" style="width:100%; height:100%; object-fit:cover;"/>' +
				        '</span>' +
				        '<span class="txt_box mb-2 mb-sm-0 mb-md-2">' +
				        '<strong>' + list.EMP_NM + '<small class="fw-normal ps-2">교수</small></strong>' +
				        '<span class="d-flex flex-wrap">' +
			            '<li class="like_container on_red" id="'+DOC_ID+'">'+
			            '<span class="link_cnt text-end">'+
						'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'prof\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						'</span>'+
						'</li>'+			       
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
 
 
/* 상세보기(교과목코드, 부서코드, 년도, 분반) */
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
			            '</ul>' +
			            '<h5 class="title mb-3">' +
			            '<a href="javascript:" onclick="redirectMajorView(\''+ DOC_ID +'\')" title="' + list.MAJOR_NM_KOR + '" class="d-block fw-semibold text-truncate">' +
			            list.MAJOR_NM_KOR + ' <small class="d-block fw-normal mt-1 text-truncate">' + (list.MAJOR_NM_ENG||'') + '</small>' +
			            '</a>' +
			            '</h5>' +
			            '<li class="like_container on_red" id="'+DOC_ID+'">'+
			            '<span class="link_cnt text-end">'+
						'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'major\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						'</span>'+
						'</li>'+		            
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
			             '<li class="border">부전공</li>' +
			             '</ul>' +
			             '<h3 class="title fs-5 fw-bolder text-start mb-3">' +
			             '<a href="javascript:" onclick="sdmView(\''+ DOC_ID+ '\')" title="' + list.SDM_KOR_NM + '" class="text-truncate">' + list.SDM_KOR_NM + '</a>' +
			             '</h3>' +
			             '<li class="like_container on_red" id="'+DOC_ID+'">'+
			             '<span class="link_cnt text-end">'+
						 '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 18" onclick="likeChange(\''+DOC_ID+'\',\'studPlan\');"><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.08.01 4.53.01 6.07s.64 3 1.79 4.09l7.8 7.39a.62.62 0 0 0 .83 0l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Z" style="fill:#fff;stroke-width:0"/><path d="M18.23 1.98C17.08.89 15.55.29 13.92.29c-1.44 0-2.81.47-3.9 1.33A6.262 6.262 0 0 0 6.12.29c-1.64 0-3.17.6-4.32 1.69C.65 3.07.01 4.53.01 6.07s.63 3 1.79 4.09 7.8 7.39 7.8 7.39c.11.1.26.16.41.16.15 0 .3-.06.41-.16l7.79-7.38v-.01c2.37-2.25 2.37-5.92 0-8.17Zm-.83 7.39-7.39 7-7.39-7c-.93-.89-1.44-2.06-1.44-3.3s.51-2.42 1.44-3.3c.94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.48 1.37.23.22.6.22.83 0 .94-.89 2.19-1.38 3.49-1.37 1.32 0 2.55.49 3.49 1.37 1.92 1.82 1.92 4.78 0 6.6Z" style="stroke-width:0;fill:#ff0202"/></svg>' +
						 '</span>'+
						 '</li>'+		             
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
			             '<dl class="d-flex flex-wrap">' +
			             '<dt>개설유형</dt>' +
			             '<dd>부전공</dd>' +
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

function BasketList(){
	getBasketCart(1);
}
 
//장바구니 가져오기
function getBasketCart(page){
	$.ajax({
		url: '/web/basket/getBasketList.do?mId=52',
		contentType:'application/json',	
		type: 'POST',
		data: JSON.stringify({ 
			page : page
		}),
		success: function(data){
			const $basketList = $("#basketList");
			
			var today = new Date();
			var year = today.getFullYear();
			
			
			innerHtml = '';
			if(data.basketList != null){
			/* 장바구니 */
				$.each(data.basketList, function(i, list){
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
					
					var errMsg = '예비수강신청 연계 개발 전';
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
								 '<input class="form-check-input" type="checkbox" value="' + DOC_ID + '" id="cartChk_' + i +'" name="cartChk" data-year="' + list.YEAR + '" data-smt="' + list.SMT + '" data-subjectCd="' + list.SUBJECT_CD + '" data-divCls="' + list.DIVCLS + '" data-deptCd="' + list.DEPT_CD + '" data-empNo="'+list.EMP_NO+'">' +
								 '<label class="blind" for="cartChk">강좌선택</label></div>'
					
					
					innerHtml += '<div><div class="d-flex flex-row justify-content-between align-items-center">' +
								 '<ul class="cate d-flex flex-row gap-1 flex-wrap">' +
								 '<li class="' + sbjt_class + '">' + list.COMDIV_CODE_NAME + '</li>' +
								 '<li class="' + year_class + '">' + list.YEAR + '학년도 ' + list.SMT_NAME + '</li>' + 
								 '<li style="font-weight:400;">' + errMsg + '</li>' + 
								 '<li class="my_statt d-flex flex-row align-items-center ms-auto ' + cls_class + '">' + 
								 '<button class="bg-transparent border-0" id="' + cls_id + '">' + cls_text + '</button></li></ul>' + 
								 '<ol class="d-flex btn_wrap align-items-center gap-2 ">' + 
								 '<li><button type="button" class="border-0 btn_dele rounded-circle" onclick="deleteBasket(\'' + list.YEAR + '\',\'' + list.SMT + '\',\''+ list.SUBJECT_CD + '\',\'' + list.DEPT_CD + '\',\'' + list.DIVCLS + '\',\'' + list.SUBJECT_NM + '\', \'' + list.EMP_NO + '\', \'' + DOC_ID + '\')"></button></li></ol></div>' +
								 '<h5 class="fw-semibold text-truncate w-100">' + list.SUBJECT_NM + '</h5>' +
								 '<ul class="info_wrap d-flex flex-column">' +
								 '<li class="d-flex flex-wrap"><dl class="d-flex flex-row align-items-start gap-2"><dt>전공</dt><dd>' + list.DEPT_NM + '</dd></dl><dl class="d-flex flex-row align-items-start gap-2"><dt>교수</dt><dd>' + list.EMP_NM + '</dd></dl><dl class="d-flex flex-row align-items-start gap-2"><dt>수강학년</dt><dd>' + list.COM_GRADE + '학년</dd></dl></li>' +
								 '<li class="d-flex flex-wrap"><dl class="d-flex flex-row align-items-start gap-2"><dt>시간</dt><dd>' + list.SISU + '</dd></dl><dl class="d-flex flex-row align-items-start gap-2"><dt>정원</dt><dd>' + list.STUDENT_CNT + '명</dd></dl></li>' + 
								 '</ul></div></div>'
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
			//$("#"+id).addClass("disa");
			alert("장바구니에서 삭제 되었습니다.");
			//location.reload();
		}
	});
}




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
				BasketList();
			} else{
				alert("선택된 강의를 정상적으로 삭제하지 못했습니다.");
			} 
		}
	});	
}

function cartAllChk(e){
	$("input[name=cartChk]").prop("checked", e);
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

</script>
