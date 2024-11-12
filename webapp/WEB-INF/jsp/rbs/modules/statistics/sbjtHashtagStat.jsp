<%@ include file="../../include/commonTop.jsp"%>
<link rel="stylesheet" type="text/css" href="/include/js/calendar/calendar.css">
<script type="text/javascript" src="/include/js/calendar.js"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/Chart.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/chartjs-plugin-doughnutlabel.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/all.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/moment.js"/>"></script>
<style type="text/css">
.page {display:flex;justify-content: center;margin:40px 0;}
.page a {position:relative;display:flex;align-items: center;justify-content: center;margin:2px 2px;width:30px;height:30px;font-size:14px;color:#555;vertical-align:middle;text-decoration:none;background:#fff;border:1px solid #d7d7d7}
.page a.on {color:#fff;background:#f48168;border:1px solid #f48168;z-index:2}
</style>

<script type="text/javascript">
$(function(){
    setForm();
    getData();
});

// 전역 변수로 차트를 저장할 변수 선언
let chart;

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
            beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
            url:"${URL_HASHTAG_STAT_DATA}",
            data: $('#searchForm').serialize(),
            success:function(data){
                if (data.status != 1) return false;
                setChart(data.chartData);
                
                return true;
            },
            error: function(x, y, z){
                console.log(x, y, z);
                return false;
            }
        });
    } catch(e){
        console.log(e);
        return false;
    }
}

function setChart(data) {
    if (chart) chart.destroy(); // 기존 차트가 있으면 삭제

    var ctx = document.getElementById('chartBox').getContext('2d');
    var names = [];
    var rates = [];
    var totalRate = 0;
    for(var j = 0; j < data.length; j++){
        var item = data[j];
        var nm = item["NM"];
        var rate = Number(item["PER"]);
        
        names.push(nm);
        rates.push(rate);
        
        totalRate += Number(rate);
    }
    
    if(totalRate < 100){
        names.push('기타');
        rates.push(100 - totalRate);
    }

    chart = new Chart(ctx, {
        type: 'pie',
        data: {
            labels: names,
            datasets: [{
                data: rates,
                backgroundColor: rates.map(() => getRandomColor()),
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

function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}
</script>
