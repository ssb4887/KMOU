<%@ include file="../../include/commonTop.jsp"%>

<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tabulator.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/Chart.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/chartjs-plugin-doughnutlabel.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/all.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/moment.js"/>"></script>

<script type="text/javascript" src="/include/js/calendar.js"></script>
<style type="text/css">
.main_con {display: flex;flex-wrap: wrap;justify-content: space-between;}
.main_con .box {position: relative;margin-bottom: 40px;padding: 30px;width: calc(50% - 20px);background: #fff;border: 1px solid #d7d7d7;border-radius: 20px;min-height: 450px;}
.main_con .box h2 {padding-bottom: 20px;font-size: 20px;font-weight: 500;line-height: 1;border-bottom: 1px solid #e2e2e2;}
.main_con .box .btnMore {position: absolute;top: 25px;right: 20px;}
.main_con .box .btnMore a {display: block;width: 30px;height: 30px;text-indent: -999px;overflow: hidden;background: url('../images/button/ic_more.png') no-repeat center center;}
.main_con .box .chartBox {width: 100%; height: calc(100% - 41px);margin-top: 30px;max-height: 377px;}
</style>
<script type="text/javascript">
$(function(){
	getData();

	$("[id^='fn_btn_search']").click(function () {
		var index = $(this).attr("id").split("-")[1];
		var startDate = $("#searchStDt_" + index).val();
		var endDate = $("#searchEdDt_" + index).val();
		
		if(index == "2"){
			setData2(startDate, endDate);
		} else if(index == "4"){
			setData4(startDate, endDate);
		} else {
			setData6(startDate, endDate);
		}
	});
});

function getData() {
	var today = new Date();
	var todayYear  = today.getFullYear();
	var todayMonth = ("0" + (1 + today.getMonth())).slice(-2);
	var todayDate  = ("0" + today.getDate()).slice(-2);
	var endDate = todayYear + '-' + todayMonth + '-' + todayDate;
	$("[id^='searchEdDt']").val(endDate);

	var monthAgo = new Date();
	monthAgo.setMonth(today.getMonth() - 1);
	var monthAgoYear  = monthAgo.getFullYear();
	var monthAgoMonth = ("0" + (1 + monthAgo.getMonth())).slice(-2);
	var monthAgoDate  = ("0" + monthAgo.getDate()).slice(-2);
	var startDate = monthAgoYear + '-' + monthAgoMonth + '-' + monthAgoDate;
	$("[id^='searchStDt']").val(startDate);
	setData1();
	setData2(startDate, endDate);
	setData3();
	setData4(startDate, endDate);
	setData5();
	setData6(startDate, endDate);

	// 달력 선택
	try {
		$("[id^='btnSearchStDt'], [id^='searchStDt']").click(function(event){
			var index = $(this).attr("id").split("_")[1];
			displayCalendar2($('#searchStDt_' + index), $('#searchEdDt_' + index), '', this);
			return false;
		});
		$("[id^='btnSearchEdDt'], [id^='searchEdDt']").click(function(event){
			var index = $(this).attr("id").split("_")[1];
			displayCalendar2($('#searchEdDt_' + index), $('#searchStDt_' + index), '', this);
			return false;
		});
	} catch(e){
		console.log(e);
	}
}

let chart1, chart2, chart3, chart4, chart5, chart6;

function setData1() {
    try {
        $.ajax({
            beforeSend: function(request) { request.setRequestHeader('Ajax', 'true'); },
            url: "${URL_SELECT_CHART_DATA1}",
            success: function(data) {
                setChart1(data.chart1Data);
            },
            error: function(x, y, z) {
                console.log(x, y, z);
                return false;
            }
        });
    } catch (e) {
        console.log(e);
        return false;
    }
}

function setData2(startDate, endDate) {
    try {
        $.ajax({
            beforeSend: function(request) { request.setRequestHeader('Ajax', 'true'); },
            url: "${URL_SELECT_CHART_DATA2}",
            data: {
                startDate: startDate,
                endDate: endDate
            },
            success: function(data) {
                setChart2(data.chart2Data);
            },
            error: function(x, y, z) {
                console.log(x, y, z);
                return false;
            }
        });
    } catch (e) {
        console.log(e);
        return false;
    }
}

function setData3() {
    try {
        $.ajax({
            beforeSend: function(request) { request.setRequestHeader('Ajax', 'true'); },
            url: "${URL_SELECT_CHART_DATA3}",
            success: function(data) {
                setChart3(data.chart3Data);
            },
            error: function(x, y, z) {
                console.log(x, y, z);
                return false;
            }
        });
    } catch (e) {
        console.log(e);
        return false;
    }
}

function setData4(startDate, endDate) {
    try {
        $.ajax({
            beforeSend: function(request) { request.setRequestHeader('Ajax', 'true'); },
            url: "${URL_SELECT_CHART_DATA4}",
            data: {
                startDate: startDate,
                endDate: endDate
            },
            success: function(data) {
                setChart4(data.chart4Data);
            },
            error: function(x, y, z) {
                console.log(x, y, z);
                return false;
            }
        });
    } catch (e) {
        console.log(e);
        return false;
    }
}

function setData5() {
    try {
        $.ajax({
            beforeSend: function(request) { request.setRequestHeader('Ajax', 'true'); },
            url: "${URL_SELECT_CHART_DATA5}",
            success: function(data) {
                setChart5(data.chart5Data);
            },
            error: function(x, y, z) {
                console.log(x, y, z);
                return false;
            }
        });
    } catch (e) {
        console.log(e);
        return false;
    }
}

function setData6(startDate, endDate) {
    try {
        $.ajax({
            beforeSend: function(request) { request.setRequestHeader('Ajax', 'true'); },
            url: "${URL_SELECT_CHART_DATA6}",
            data: {
                startDate: startDate,
                endDate: endDate
            },
            success: function(data) {
                setChart6(data.chart6Data);
            },
            error: function(x, y, z) {
                console.log(x, y, z);
                return false;
            }
        });
    } catch (e) {
        console.log(e);
        return false;
    }
}

function setChart1(data) {
    if (chart1) chart1.destroy(); // 기존 차트 삭제
    const ctx = document.getElementById('chart1').getContext('2d');
    const hours = data.map(item => item.regiDate);
    const counts = data.map(item => item.count);

    chart1 = new Chart(ctx, {
        type: 'line',
        data: {
            labels: hours,
            datasets: [{
                label: '검색 건수',
                data: counts,
                borderColor: '#4472c4',
                backgroundColor: 'rgba(68, 114, 196, 0.1)',
                fill: true
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.parsed.y;
                        }
                    }
                }
            },
            scales: {
                x: { title: { display: true, text: '시간' } },
                y: { title: { display: true, text: '검색 건수' }, beginAtZero: true }
            }
        }
    });
}

function setChart2(data) {
    if (chart2) chart2.destroy(); // 기존 차트 삭제
    const ctx = document.getElementById('chart2').getContext('2d');
    const keywords = data.map(item => item.keyword);
    const counts = data.map(item => item.count);

    chart2 = new Chart(ctx, {
        type: 'horizontalBar', // 가로 바 차트
        data: {
            labels: keywords,
            datasets: [{
                label: '검색어',
                data: counts,
                backgroundColor: '#5A8AFA'
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.parsed.x;
                        }
                    }
                }
            },
            scales: {
                xAxes: [{ 
                    ticks: { 
                        beginAtZero: true,
                    },
                    scaleLabel: {
                        display: true,
                        labelString: '검색 건수'
                    }
                }],
                yAxes: [{ 
                    scaleLabel: {
                        display: true,
                        labelString: '검색어'
                    }
                }]
            }
        }
    });
}

function setChart3(data) {
    if (chart3) chart3.destroy(); // 기존 차트 삭제
    const ctx = document.getElementById('chart3').getContext('2d');
    const dates = data.map(item => item.regiDate);
    const counts = data.map(item => item.count);

    chart3 = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: dates,
            datasets: [{
                label: '로그인 수',
                data: counts,
                backgroundColor: '#5A8AFA'
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.parsed.y;
                        }
                    }
                }
            },
            scales: {
                x: { title: { display: true, text: '날짜' } },
                y: { title: { display: true, text: '로그인 수' }, beginAtZero: true }
            }
        }
    });
}

function setChart4(data) {
    if (chart4) chart4.destroy(); // 기존 차트 삭제
    const ctx = document.getElementById('chart4').getContext('2d');
    const menus = data.map(item => item.name);
    const counts = data.map(item => parseInt(item.cnt));

    chart4 = new Chart(ctx, {
        type: 'horizontalBar', // 가로 바 차트
        data: {
            labels: menus,
            datasets: [{
                label: '메뉴별 접속자 수',
                data: counts,
                backgroundColor: '#5A8AFA'
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.parsed.x;
                        }
                    }
                }
            },
            scales: {
                xAxes: [{ 
                    ticks: { 
                        beginAtZero: true,
                    },
                    scaleLabel: {
                        display: true,
                        labelString: '접속자 수'
                    }
                }],
                yAxes: [{ 
                    scaleLabel: {
                        display: true,
                        labelString: '메뉴'
                    }
                }]
            }
        }
    });
}

function setChart5(data) {
    if (chart5) chart5.destroy(); // 기존 차트 삭제
    const ctx = document.getElementById('chart5').getContext('2d');
    const today = new Date();
    const todayYear = today.getFullYear();
    const todayBeforeYear = todayYear - 1;

    const todays = [];
    const todayBefores = [];
    const menuFgs = [];

    data.forEach(item => {
        const year = item.year;
        const menuNm = getMenuName(item.menuFg);

        if (!menuFgs.includes(menuNm)) {
            menuFgs.push(menuNm);
        }

        if (year == todayYear) {
            todays.push(item.count);
        } else {
            todayBefores.push(item.count);
        }
    });

    chart5 = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: menuFgs,
            datasets: [{
                label: todayYear + '년',
                data: todays,
                backgroundColor: '#5A8AFA'
            }, {
                label: todayBeforeYear + '년',
                data: todayBefores,
                backgroundColor: '#ddd'
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: true },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.parsed.y;
                        }
                    }
                }
            },
            scales: {
                x: { title: { display: true, text: '메뉴' } },
                y: { title: { display: true, text: '찜 현황' }, beginAtZero: true }
            }
        }
    });
}

function setChart6(data) {
    if (chart6) chart6.destroy(); // 기존 차트 삭제
	if(data != ''){
	    const ctx = document.getElementById('chart6').getContext('2d');
	    const names = data.map(item => item.nm);
	    const rates = data.map(item => Number(item.per));
	    const cnts = data.map(item => Number(item.cnt));
	
	    const totalRate = rates.reduce((a, b) => a + b, 0);
	
	    if (totalRate < 100) {
	        names.push('기타');
	        rates.push(100 - totalRate);
	        cnts.push('기타 ');
	    }
	
	    chart6 = new Chart(ctx, {
	        type: 'pie',
	        data: {
	            labels: names,
	            datasets: [{
	                label: '비율',
	                data: cnts,
	                backgroundColor: rates.map(() => getRandomColor())
	            }]
	        },
	        options: {
	            responsive: true,
	            plugins: {
	                tooltip: {
	                    callbacks: {
	                        label: function(context) {
	                            return context.label + ': ' + context.parsed + '%';
	                        }
	                    }
	                }
	            }
	        }
	    });
		
	}
}

function getMenuName(menuFg) {
    switch (menuFg) {
        case 'sbjt':
            return '교과목(교육과정)';
        case 'prof':
            return '교수';
        case 'major':
            return '전공';
        case 'lecture':
            return '교과목(강좌)';
        case 'nonSbjt':
            return '비교과';
        case 'studPlan':
            return '학생설계전공';
    }
}

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}
</script>
