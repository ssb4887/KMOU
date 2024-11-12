<%@ include file="../../include/commonTop.jsp"%>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/tabulator.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/Chart.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/chartjs-plugin-doughnutlabel.min.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/all.js"/>"></script>
<script type="text/javascript" src="<c:out value="${contextPath}/include/js/moment.js"/>"></script>

<script type="text/javascript">
$(function(){
    setForm();
    getData();
});

// 전역 변수로 차트를 저장할 변수 선언
let searchChart;

function setForm() {
    var today = new Date();
    var todayYear = today.getFullYear();
    var todayMonth = today.getMonth() + 1;
    var todayDate = today.getDate();

    for (var year = 2016; year <= todayYear; year++) {
        var selected = year == todayYear ? 'selected' : '';
        $('#searchStatsDate1').append('<option value="'+year+'" '+selected+'>'+year+'년</option>');
    }
    for (var month = 1; month <= 12; month++) {
        var selected = month == todayMonth ? 'selected' : '';
        $('#searchStatsDate2').append('<option value="'+(month < 10 ? '0'+month : month)+'" '+selected+'>'+month+'월</option>');
    }
    for (var date = 1; date <= 31; date++) {
        var selected = date == todayDate ? 'selected' : '';
        $('#searchStatsDate3').append('<option value="'+(date < 10 ? '0'+date : date)+'" '+selected+'>'+date+'일</option>');
    }

    $('#searchStatsType').on('change', function(){
        var value = $(this).val();

        if (value == '1') {
            $('#searchStatsDate1, #searchStatsDate2, #searchStatsDate3').prop('disabled', true);
        }
        if (value == '2'){
            $('#searchStatsDate1').prop('disabled', false);
            $('#searchStatsDate2, #searchStatsDate3').prop('disabled', true);
        }
        if (value == '3'){
            $('#searchStatsDate1, #searchStatsDate2').prop('disabled', false);
            $('#searchStatsDate3').prop('disabled', true);
        }
        if (value == '4'){
            $('#searchStatsDate1, #searchStatsDate2, #searchStatsDate3').prop('disabled', false);
        }
    });
}

function getData() {
    try {
        $.ajax({
            beforeSend: function(request){request.setRequestHeader('Ajax', 'true');},
            url: "${URL_SRCH_CNT_STAT_DATA}",
            data: $('#searchForm').serialize(),
            success: function(data){
                if (data.STATUS != 1) return false;

                setChart(data.CHART_DATA);
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

function setChart(dataList) {
    if (searchChart) searchChart.destroy(); // 기존 차트가 있으면 삭제

    var ctx = document.getElementById('chartCanvas').getContext('2d');
    var hours = [];
    var counts = [];
    for(var i = 0; i < dataList.length; i++){
        var item = dataList[i];
        var hour = item["REGI_DATE"];
        if(hour.includes(":")){
            hour = hour.split(":")[1];
        }
        var count = item["COUNT"];
        
        hours.push(hour);
        counts.push(count);
    }

    searchChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: hours,
            datasets: [{
                label: '검색 건수',
                data: counts,
                borderColor: '#4472c4',
                backgroundColor: 'rgba(68, 114, 196, 0.1)',
                fill: true,
                borderWidth: 2,
                pointStyle: 'rect'
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
                    title: { display: true, text: '시간' }
                },
                yAxes: [{
                    display: true,
                    title: { text: '검색 건수' },
                    ticks: {
                        beginAtZero: true,
                        min: 0
                    }
                }]                
            }
        }
    });
}
</script>
