<%@ include file="../../include/commonTop.jsp"%>
<c:set var="mngAuth" value="${elfn:isAuth('MNG')}"/>
<c:set var="wrtAuth" value="${elfn:isAuth('WRT')}"/>
<script src="${contextPath}/web/code/highcharts.js"></script>
<script src="${contextPath}/web/code/highcharts-more.js"></script>
<script src="${contextPath}/web/code/modules/solid-gauge.js"></script>
<script src="${contextPath}/web/code/modules/export-data.js"></script>
<script src="${contextPath}/web/code/modules/accessibility.js"></script>
<script>
$(function (){
//		이동근 : 현업요청, 총학점 문구 삭제 및 그래프 추가 수정
	var subTtl ="";
	//총 이수학점
	var ttCptnPnt = $("#ttCptnPnt").val();
	//동일학기 이수평균
	var ttSameShtmCptnPntAvg = $("#sameShtmCptnPntAvg").val();
	//총 기준학점
	var ttBasePnt = $("#ttBasePnt").val();
	
	// 나의학점		
	subTtl = 	' <ul class="myGrade_infog">'
				+' <li>'
				+'		<strong>'+ ttCptnPnt +'</strong>'
				+'		<small>('+ttSameShtmCptnPntAvg+')</small>'
				+'	</li>'
				+'	<li>총학점 <span>'+ttBasePnt+'<span></li>' 
				+'</ul>';
				
	Highcharts.chart('myCrd', {

	    chart: {
	        type: 'solidgauge',
	        height: '100%',
	        backgroundColor: "#F6F9FF",
	        style:{
	        	fontFamily:'pretendard'
	        }
	    },

	    title: {
	        text: '',
	        style: {
	            fontSize: '24px'
	        }
	    },
	    subtitle: {
	            useHTML: true,
	            text: subTtl,
	            floating: true,
	            verticalAlign: 'middle',
	            y: 20
	    },
	    credits: {
	    		enabled: false
	    },
	  	tooltip: {
	        enabled: false
	      },
	    pane: {
	        startAngle: 0,
	        size: 85 + '%',
	        endAngle: 360,
	        background: [{ // Track for Conversion
	            outerRadius: '110%',
	            innerRadius: '98%',
	            backgroundColor: "#DFE6F3",
	            borderWidth: 0
	        }, { // Track for Engagement
	            outerRadius: '95%',
	            innerRadius: '83%',
	            backgroundColor: "#E6EEFE",
	            borderWidth: 0,
	             dataLabels: {
	              y:-35,
	              x: 25,
	              color:'#2C3D8F', 
	            }
	        }]
	    },
	    legend:{
	    	align:'center',
	    	/* width:'110%', */
	    	verticalAlign:'bottom',
	    	layout: 'horizontal',
	    	symbolPadding: 1,
	    	itemDistance:10,
	    	itemStyle:{
    			fontSize:'12px',
    			color:'#666',
	    	},
	    	/* floating:true,
	    	x:0,
	    	y:0 */
	    },

	    yAxis: {
	        min: 0,
	        max: 100,
	        lineWidth: 0,
	        tickPositions: [],
	    
	    },

	    plotOptions: {
	        	solidgauge: {
	         colorByPoint: false,
	            dataLabels: {
	                enabled: false
	            },
	            linecap: 'round',
	            stickyTracking: false,
	            rounded: true,
	            
	        }
	    },

	    series: [{
	        name: '누적학점(학년기준)',
	        showInLegend: true,
	        color: "#2C3D8F",
	        data: [{
	            color: "#2C3D8F",
	            radius: '110%',
	            innerRadius: '98%',
	            y: Number(Math.floor((ttCptnPnt / ttBasePnt) * 100))
	         }]
	    }, {
	        name: '동일학기이수평균',
	        showInLegend: true,
	        color: "#67B7DC",
	        data: [{
	            color: "#67B7DC",
	            radius: '95%',
	            innerRadius: '83%',
	            y: Number(Math.floor((ttCptnPnt / ttSameShtmCptnPntAvg) * 100))
	        }]
	    }],
	    responsive: {  
    	  rules: [{  
    	    condition: {  
    	      maxWidth: 500  
    	    } 
    	  }]  
    	}
	});
	
	
	// 나의 성적
	var ttGpaAvg = $("#ttGpaAvg").val();
	var ttGpaAvg100 = $("#ttGpaAvg100").val();
	var ttSameMjShtmGpaAvg = $("#sameMjShtmGpaAvg").val();
	
	/* subTtl = ""+ttGpaAvg+" / 4.5 <br/> "+ttGpaAvg100+" / 100"; */
	subTtl = '<ul class="myPerform_infog">'
			+'<li>'
			+	'<strong>'+ttGpaAvg+'</strong>'
			+	'<small>'+ttSameMjShtmGpaAvg+'</small>'
//				+	'<span> / 4.5</span>'
			+'</li>'
			+'<li>만점 <span> 4.5 </span></li>'
			+'</ul>';
	
	
	Highcharts.chart('myGrade', {

	    chart: {
	        type: 'solidgauge',
	        height: '100%',
	        backgroundColor: "#F6F9FF",
	        style:{
	        	fontFamily:'pretendard'
	        }
	    },

	    title: {
	        text: '',
	        style: {
	            fontSize: '24px'
	        }
	    },
	    subtitle: {
	            useHTML: true,
	            text: subTtl,
	            floating: true,
	            verticalAlign: 'middle',
	            y: 30
	    },
	    credits: {
	    		enabled: false
	    },
	  	tooltip: {
	        enabled: false
	      },
	      
      
      pane: {
	        startAngle: 0,
	        size: 85 + '%',
	        endAngle: 360,
	        background: [{ // Track for Conversion
	            outerRadius: '110%',
	            innerRadius: '98%',
	            backgroundColor: "#DFE6F3",
	            borderWidth: 0
	        }, { // Track for Engagement
	            outerRadius: '95%',
	            innerRadius: '83%',
	            backgroundColor: "#E6EEFE",
	            borderWidth: 0,
	             dataLabels: {
	              y:-35,
	              x: 25,
	              color:'#2C3D8F', 
	            }
	        }]
	    },

	    legend:{
	    	align:'center',
	    	verticalAlign:'bottom',
	    	layout: 'horizontal',
	    	symbolPadding: 1,
	    	itemDistance:10,
	    	itemStyle:{
    			fontSize:'12px',
    			color:'#666'
	    	},
	    	/* floating:true,
	    	x:-0,
	    	y:30 */
	    },
	    
	    yAxis: {
	        min: 0,
	        max: 100,
	        lineWidth: 0,
	        tickPositions: []
	    },

	    plotOptions: {
        	solidgauge: {
         		colorByPoint: false,
            	dataLabels: {
                	enabled: false
            	},
            	linecap: 'round',
            	stickyTracking: false,
            	rounded: true
            
        	}
	    },

	    series: [{
	        name: '성적평균',
	        showInLegend: true,
	        color: "#8067DC",
	        data: [{
	            color: "#8067DC",
	            radius: '110%',
	            innerRadius: '98%',
	            y: Number(Math.floor((ttGpaAvg / 4.5) * 100))
	         }]
	    }, {
	        name: '전공평균',
	        showInLegend: true,
	        color: "#6794DC",
	        data: [{
	            color: "#6794DC",
	            radius: '95%',
	            innerRadius: '83%',
	            y: Number(Math.floor((ttGpaAvg / ttSameMjShtmGpaAvg) * 100))
	        }]
	    }],
	    responsive: {  
    	  rules: [{  
    	    condition: {  
    	      maxWidth: 500  
    	    } 
    	  }]  
    	}
	});
	
	
	//핵심역량진단
	
	var capbData1 =[];
	var capbData2 =[];
	var capbData3 =[];
	var capbData4 =[];
	
	$("input[name=capbData]").each(function(){
		var shyr = $(this).attr("aria-capb-shyr");
		
		if(shyr == "1"){
			capbData1.push(Number($(this).attr("aria-capb-a01")));
			capbData1.push(Number($(this).attr("aria-capb-b01")));
			capbData1.push(Number($(this).attr("aria-capb-a02")));
			capbData1.push(Number($(this).attr("aria-capb-b02")));
			capbData1.push(Number($(this).attr("aria-capb-a03")));
			capbData1.push(Number($(this).attr("aria-capb-b03")));
		}else if(shyr == "2"){
			capbData2.push(Number($(this).attr("aria-capb-a01")));
			capbData2.push(Number($(this).attr("aria-capb-b01")));
			capbData2.push(Number($(this).attr("aria-capb-a02")));
			capbData2.push(Number($(this).attr("aria-capb-b02")));
			capbData2.push(Number($(this).attr("aria-capb-a03")));
			capbData2.push(Number($(this).attr("aria-capb-b03")));
		}else if(shyr == "3"){
			capbData3.push(Number($(this).attr("aria-capb-a01")));
			capbData3.push(Number($(this).attr("aria-capb-b01")));
			capbData3.push(Number($(this).attr("aria-capb-a02")));
			capbData3.push(Number($(this).attr("aria-capb-b02")));
			capbData3.push(Number($(this).attr("aria-capb-a03")));
			capbData3.push(Number($(this).attr("aria-capb-b03")));
		}else if(shyr == "4"){
			capbData4.push(Number($(this).attr("aria-capb-a01")));
			capbData4.push(Number($(this).attr("aria-capb-b01")));
			capbData4.push(Number($(this).attr("aria-capb-a02")));
			capbData4.push(Number($(this).attr("aria-capb-b02")));
			capbData4.push(Number($(this).attr("aria-capb-a03")));
			capbData4.push(Number($(this).attr("aria-capb-b03")));
		}
		
	})
	
	
	Highcharts.chart('myCapabilities', {
	    chart: {
	        polar: true,
	        type: 'line',
	        height: '100%',
	        backgroundColor: "#F6F9FF",
	        style:{
	        	fontFamily:'pretendard'
	        }
	    },
	    title: {
	        text: ''
	    },
	    pane: {
	        size: '80%',
	        backgroundColor: "#fff"
	    },
	    xAxis: {
	        categories: ['주도적 학습', '협력적 소통', '통섭적 사고', '문화적 포용', '확산적 연계', '사회적 실천'],
	        tickmarkPlacement: 'on',
	        backgroundColor: "#F6F9FF",
	        lineWidth:0, 
	        labels:{
	        	style:{
	        		color:'#666',
	        		fontSize:'11px'
	        	}
	        }
	    },
	    yAxis: {
	        gridLineInterpolation: 'polygon',
	        backgroundColor: "#FFF",
	        lineWidth: 0,
	        min: 0,
	        max: 100,
	        tickInterval: 25,
	        labels: {
	        	type:'line',
	            formatter: function () {
	                return '';
	            }
	       }
	    },
	    tooltip: {
	        enabled: false
	    },
	    credits: {
		    enabled: false
		 },
		
		legend:{
	    	align:'center',
	    	position:'bottom',
	    	/* width:'100%', */
	    	verticalAlign:'bottom',
	    	layout: 'horizontal',
	    	symbol:'circle',
	    	symbolPadding: 1,
	    	itemDistance:8,
	    	symbolHeight: 10,
	      	symbolWidth: 10,
		    symbolPadding: 4,
	    	itemStyle:{
    			fontSize:'11px',
    			color:'#666'
	    	}
	    },
	    
	    
	    series: [
	    	 {
	    		type: 'area',
	    		name: '1학년',
		        data: capbData1,
		        color:'#67B7DC',
		        pointPlacement: 'on',
		        marker:{
		        	enabled: false,
		        	states:{
		        		hover: {
	                        enabled: false
	                    }
		        	}
		        }
		    },
		    {
	    		type: 'area',
	    		name: '2학년',
		        data: capbData2,
		        color:'#6771DC',
		        pointPlacement: 'on',
		        marker:{
		        	enabled: false,
		        	states:{
		        		hover: {
	                        enabled: false
	                    }
		        	}
		        }
		    },
		    {
	    		type: 'area',
	    		name: '3학년',
		        data: capbData3,
		        color:'#A367DC',
		        pointPlacement: 'on',
		        marker:{
		        	enabled: false,
		        	states:{
		        		hover: {
	                        enabled: false
	                    }
		        	}
		        }
		    },
		    {
	    		type: 'area',
	    		name: '4학년',
		        data: capbData4,
		        color:'#DC67AB',
		        pointPlacement: 'on',
		        marker:{
		        	enabled: false,
		        	states:{
		        		hover: {
	                        enabled: false
	                    }
		        	}
		        }
		    }],
		    responsive: {  
	    	  rules: [{  
	    	    condition: {  
	    	      maxWidth: 500  
	    	    } 
	    	  }]  
	    	}
	});
	
	
	
	// 소요학점
	/*x축 항목 이름 리스트*/
	var xAxisNmList = [];
	/*기준학점 리스트*/
	var baseDataList = [];
	/*취득학점 리스트*/
	var cptnDataList = [];
	/*수강중학점 리스트*/
	var courDataList = [];
	/*데이터리스트 길이*/
	var uniStdLast = $("input[name=uniStdLast]").val();
	
	
	for(var i = 0; i < uniStdLast; i++){
		var xAxisNm = $("input[name=fgNm" + i + "]").val();
		var basePnt = $("input[name=basePnt" + i + "]").val();	
		var cptnPnt = $("input[name=cptnPnt" + i + "]").val();
		var courPnt = $("input[name=courPnt" + i + "]").val();
		
		xAxisNmList.push(xAxisNm);
		baseDataList.push(Number(basePnt));
		cptnDataList.push(Number(cptnPnt));
		courDataList.push(Number(courPnt));
	}
	
	
	Highcharts.chart('myReqCrdDetails', {
	    chart: {
	        type: 'bar',
	        backgroundColor: "#F6F9FF",
	        style:{
	        	fontFamily:'pretendard'
	        },
	        height:50+'%',
	        verticalAlign:'center'
	        
	    },
	    title: {
	        text: '',
	    },
	    
	    xAxis: {
	        categories: xAxisNmList
	    },
	    yAxis: {
	    	title : null,
	        min: 0,
	        labels:{
	        	style:{
	        		color:'#979797',
	        		fontSize:'10px'
	        	}
	        }
	    },
	    legend: {
	        reversed: true,
	        itemStyle:{
	        	fontSize:'10px',
	        	color:"#979797"
	        }
	    },
	    credits: {
    		enabled: false
   		},	
	    plotOptions: {
	        series: {
	            dataLabels: {	            	
	                enabled: true,
	                align: 'center',
	                inside: true,
	                /*값이 0인 bar 제외 처리*/
	                formatter: function(){
	                	return this.y >0 ? this.y : null;
	                }
	            }
	        },
	        column:{
	        	pointPadding: 2,
	            borderWidth: 0
	        }
	    },	    
	    series: [
	    	{
		    	name : '기준학점(학번기준)',
		    	data : baseDataList,
		    	color:"#4571e9"
	    	},
	    	{
	    		name : '수강 중인 학점',
		    	data : courDataList,
		    	stacking : 'normal',
		    	color:"#0f3e83"
	    	},
	    	{
	    		name : '취득학점',
		    	data : cptnDataList,
		    	stacking : 'normal',
	    		color:"#5fbcd0"
	    	}
	    ],
	    responsive: {  
    	  rules: [{  
    	    condition: {  
    	      maxWidth: 500  
    	    } 
    	  }]  
    	}
	});
	

	
	
	var appl = "";
	var acqu = "";
	var gpas = "";
	var perc = "";
	var fexc = "";
	var mjRank = "";
	var mjRankObjRcnt = "";
	var shtmMrksIdx = "";
	$(".shtmMrks").each(function(index){
		if(index == 0){
			appl = $(".applTot").eq(index).text();
			acqu = $(".acquTot").eq(index).text();
			gpas = $(".gpasTot").eq(index).text();
			perc = $(".percTot").eq(index).text();
			fexc = $(".fexcTot").eq(index).text();
			mjRank = $(".mjRank").eq(index).val();
			mjRankObjRcnt = $(".mjRankObjRcnt").eq(index).val();
		}else{
			appl = Number(appl) + Number($(".applTot").eq(index).text());
			acqu = Number(acqu) + Number($(".acquTot").eq(index).text());
			gpas = Number(gpas) + Number($(".gpasTot").eq(index).text());
			perc = Number(perc) + Number($(".percTot").eq(index).text());
			fexc = Number(fexc) + Number($(".fexcTot").eq(index).text());
			mjRank = Number(mjRank) + Number($(".mjRank").eq(index).val());
			mjRankObjRcnt = Number(mjRankObjRcnt) + Number($(".mjRankObjRcnt").eq(index).val());
		}
		shtmMrksIdx = index+1;
	});
	
	$("#applTot").text(appl);	
	$("#acquTot").text(acqu);	
	$("#gpasTot").text((gpas/shtmMrksIdx).toFixed(2));	
	$("#percTot").text((perc/shtmMrksIdx).toFixed(2));	
	$("#fexcTot").text((fexc/shtmMrksIdx).toFixed(2));	
	
	mjRank = (mjRank/shtmMrksIdx).toFixed(1);
	mjRankObjRcnt = (mjRankObjRcnt/shtmMrksIdx).toFixed(1);
	var rankText = '<span>'+mjRank+'</span>/ '+mjRankObjRcnt+'';
	$("#rankTot").html(rankText);	
	
});	
</script>