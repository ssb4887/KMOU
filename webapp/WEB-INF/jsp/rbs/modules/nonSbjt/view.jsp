<%@ include file="../../usr/web/include/commonTop.jsp"%>

<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tabulator.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/Chart.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/chartjs-plugin-doughnutlabel.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/all.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/moment.js"/>"></script>

<script type="text/javascript">


$(function(){
	
    
	//contents의 이미지 파일 OceanCTS에 맞게 url 변환
    var images = document.querySelectorAll('img.fr-fic.fr-dib');    
    images.forEach(function(img) {
        var fileName = img.getAttribute('data-name');
        var encodedFileName = customUrlEncode(fileName);
        
        img.src = 'https://cts.kmou.ac.kr/attachment/view/' + img.getAttribute('data-idx') + '/' + encodedFileName + '?ts=0';
        img.alt = '';
       
        img.removeAttribute('data-idx');
        img.removeAttribute('data-name');
        img.removeAttribute('data-code');
        img.removeAttribute('data-size');
    });
	
	//찜 onRed class 추가
	getBookmarkList()
	.then(data => {		
		var id = "${info.IDX}" + "_" + "${info.TIDX}";	
		if(data != null){
			data.forEach(function(bookmark) {
	    		if(id == bookmark.docId){        		 
	        		 $("#"+id).addClass("on_red")
	    		}
	 		});				
		}
		
	});
		
    //핵심역량지수 그래프
	createBarChart('nonSbjtEssential', JSON.parse('${essentialList}'));
    
    
	//나의 역량지수 그래프(메인화면과 동일)
	if("${USER_CORE_DIAGNOSIS}" == ''){
		$('#userEssential').addClass('hidden');
		$('#userEssential').next().removeClass('hidden');
	}else{
	    createChartCoreDiagnosis('userEssential');		
	}
});

function createBarChart(elementId, data) {
    var ctx = document.getElementById(elementId).getContext('2d');
    
    var labels = data.map(item => item.title || item.ESSENTIAL1_NM);
    var values = data.map(item => item.rate || item.POINT);

    var pastelColors = [
        'rgba(15, 30, 110, 0.7)',  // 기본 색상
        'rgba(20, 35, 120, 0.7)',  // 약간 밝은 파란색
        'rgba(25, 40, 130, 0.7)',  // 더 밝은 파란색
        'rgba(30, 45, 140, 0.7)',  // 가장 밝은 파란색
        'rgba(35, 40, 130, 0.7)',  // 약간 보라빛 파란색
        'rgba(40, 35, 120, 0.7)',  // 보라빛 파란색
        'rgba(45, 30, 110, 0.7)',  // 더 진한 보라빛 파란색
        'rgba(50, 25, 100, 0.7)'   // 가장 진한 보라빛 파란색
    ];

    new Chart(ctx, {
        type: 'horizontalBar',
        data: {
            labels: labels,
            datasets: [{
                data: values,
                backgroundColor: pastelColors,
                borderColor: pastelColors.map(color => color.replace('0.6', '1')),
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                display: false
            },
            scales: {
                xAxes: [{
                    ticks: {
                        beginAtZero: true,
                        max: 100
                    }
                }]
            },
            tooltips: {
                callbacks: {
                    label: function(tooltipItem, data) {
                        return data.labels[tooltipItem.index] + ': ' + tooltipItem.xLabel + '%';
                    }
                }
            },
            elements: {
                rectangle: {
                    borderRadius: 10
                }
            }
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
			    menuFg : 'nonSbjt'
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
// 	$("#"+docId).children().children().on("click",function(){
		if(!$("#"+docId).hasClass("on_red")){			
			$.ajax({
				url: '/web/bookmark/insertBookmark.do?mId=37&menuFg=nonSbjt',
				contentType:'application/json',	
				type: 'POST',
				data: JSON.stringify({ 
				    docId : docId
				}),
				success: function(data){
					$("#"+docId).addClass("on_red");
				}
			});
		} else {
			$.ajax({
				url: '/web/bookmark/deleteBookmark.do?mId=37&menuFg=nonSbjt',
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
// 	})
}

function customUrlEncode(str) {
    return str.replace(/ /g, '+').replace(/[()]/g, function(match) {
        return encodeURIComponent(match);
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

//나의 핵심역량(레이더 차트)
function createChartCoreDiagnosis(ctx){
    var page_prop = {};
    var dataByYear = {};
    var labelList = [];

    "<c:forEach items="${USER_CORE_DIAGNOSIS}" var="item">"
    var year = "${item.YEAR}";
    var label = "${item.ESSENTIAL1_NM}";
    var point = parseFloat("${item.POINT}");

    if (!dataByYear[year]) {
        dataByYear[year] = {
            data: [],
            label: year
        };
    }
    dataByYear[year].data.push(point);

    if (!labelList.includes(label)) {
        labelList.push(label);
    }
    "</c:forEach>"

    if (Object.keys(dataByYear).length < 1) {
        $('#'+ ctx).addClass('hidden');
        $('#'+ ctx).next().removeClass('hidden');
        return;
    }

    // 가장 최신 연도의 데이터만 사용
    const latestYear = Object.keys(dataByYear).sort().pop();
    const latestData = dataByYear[latestYear];

    const minValue = Math.min(...latestData.data) - 5;
    const maxValue = Math.max(...latestData.data);
    console.log(latestData)
    const data = {
        labels: labelList,
        yAxis: {
            min: 0,
            max: 100,
            lineWidth: 0,
            tickPositions: []
        },
        datasets: [{
            data: latestData.data,
            label: latestData.label,
            backgroundColor: "#0F1E6Ea0", 
            borderColor: "#0D1A5E", 
            borderWidth: 1,
            pointRadius: 0.5
        }]
    };

    const opts = {
        responsive: true,
        maintainAspectRatio: false,
        legend: { display: false, position:'top' },    
        scale: {
            ticks: { 
                beginAtZero: false,
                min: 0,
                max: 45,
                stepSize: 9,
                display: false 
            }, 
            pointLabels: {fontColor: "#333333", fontSize:12 }, 
            gridLines: { color: "#CCCCCC", lineWidth: 0.5} 
        },
        tooltips: {
            backgroundColor: "rgb(0,0,0,.8)", 
            bodyFontColor: "#FFFFFF",
            titleMarginBottom: 10,
            titleFontColor: '#FFFFFF',
            titleFontSize: 14,
            bodyFontSize: 16,   
            borderColor: '#555555',
            borderWidth: 1,
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
                    const label = data.labels[tooltipItem.index];
                    const value = data.datasets[tooltipItem.datasetIndex].data[tooltipItem.index];
                    return value + '점';
                }
            }
        },
    }

    try {
        page_prop.userEssential = new Chart(document.getElementById(ctx).getContext('2d'), {
            type: "radar",
            data: data,
            options: opts,
        });
    }
    catch(error) { console.log(error); }
    finally { return; }
}
</script>