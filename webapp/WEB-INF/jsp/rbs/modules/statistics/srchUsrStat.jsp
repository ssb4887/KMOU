<%@ include file="../../include/commonTop.jsp"%>
<link rel="stylesheet" type="text/css" href="/include/js/calendar/calendar.css">
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/calendar.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/Chart.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/chartjs-plugin-doughnutlabel.min.js"/>"></script>

<script type="text/javascript">
$(function(){
    setForm();
    getData();
});

// 전역 변수로 차트를 저장할 변수 선언
let usrChart, shyrChart;

function setForm() {
    var today = new Date();
    var todayYear = today.getFullYear();
    var todayMonth = ("0" + (1 + today.getMonth())).slice(-2);
    var todayDate = ("0" + today.getDate()).slice(-2);
    $('#searchEdDt').val(todayYear + '-' + todayMonth + '-' + todayDate);

    var monthAgo = new Date();
    monthAgo.setMonth(today.getMonth() - 1);
    var monthAgoYear = monthAgo.getFullYear();
    var monthAgoMonth = ("0" + (1 + monthAgo.getMonth())).slice(-2);
    var monthAgoDate = ("0" + monthAgo.getDate()).slice(-2);
    $('#searchStDt').val(monthAgoYear + '-' + monthAgoMonth + '-' + monthAgoDate);

    try {
        $("#btnSearchStDt, #searchStDt").click(function(event){
            displayCalendar2($('#searchStDt'), $('#searchEdDt'), '', this);
            return false;
        });
        $("#btnSearchEdDt, #searchEdDt").click(function(event){
            displayCalendar2($('#searchEdDt'), $('#searchStDt'), '', this);
            return false;
        });
    } catch(e){
        console.log(e);
    }
}

function getData() {
    try {
        $.ajax({
            beforeSend: function(request){request.setRequestHeader('Ajax', 'true');},
            url: "${URL_SRCH_USR_STAT_DATA}",
            data: {
                searchStDt: $('#searchStDt').val(),
                searchEdDt: $('#searchEdDt').val(),
            },
            success: function(data){
                if (data.STATUS != 1) return false;

                var chart1Data = data.USER_DATA.map(item => ({ label: item.NAME, value: item.COUNT }));
                var chart2Data = data.GRADE_DATA.map(item => ({ label: item.NAME, value: item.COUNT }));

                setChart('usrChartBox', chart1Data, '사용자', usrChart);
                setChart('shyrChartBox', chart2Data, '학년', shyrChart);
            },
            error: function(x, y, z){
                console.log(x, y, z);
            }
        });
    } catch(e){
        console.log(e);
    }
}

function setChart(idName, dataList, name, chart) {
    if (chart) chart.destroy(); // 기존 차트가 있으면 삭제
    var labels = dataList.map(item => item.label);
    var counts = dataList.map(item => item.value);
    var ctx = document.getElementById(idName).getContext('2d');
    
    chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: name + '별 추이',
                data: counts,
                backgroundColor: labels.map(() => '#5A8AFA'),
                borderColor: labels.map(() => '#5A8AFA'),
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.parsed.y + '건';
                        }
                    }
                }
            },
            scales: {
                x: {
                    title: { display: true, text: name },
                    beginAtZero: true
                },
                yAxes: [{
                    display: true,
                    title: { text: '건수' },
                    ticks: {
                        beginAtZero: true,
                        min: 0
                    }
                }] 
            }
        }
    });

    if (idName === 'usrChartBox') {
        usrChart = chart;
    } else if (idName === 'shyrChartBox') {
        shyrChart = chart;
    }
}

function getRandomColor(alpha = 1) {
    const r = Math.floor(Math.random() * 256);
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);
    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
}
</script>
