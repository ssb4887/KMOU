$(function () {
	
	buildCalendar();


  //메인-학생정보 ==> 이름/학번/학년학기 조절
  var nameInfo = $(".summ_info > h3").width();
  $(window).resize(function(){ 
    
    if(375 < window.innerWidth < 575){
      $(".summ_info > div").css({ "width": "calc(100% - " + nameInfo+ "px)" });
    }else{
      $(".summ_info > div").css({ "width": "auto" });
    };

    if(window.innerWidth < 375){
      $(".stu_numb").css({"left": "" + nameInfo+ "px" });
    }
    
  }).resize();
  $(".summ_info > div").css({ "width": "auto" });

  //학생설계전공 추천 슬라이드
  $(".item_reco_prog .box_wrap").slick({
      slidesToShow:1, 
      slidesToScroll:1, 
      arrows:false,
      dots:true,
      autoplay:false, 
      autoplaySpeed:2000,
      responsive:[
        {
          breakpoint: 991.99,
          settings:{
            slidesToShow:2, 
            infinity:false, 
            arrows:false,
          }
        },
        {
          breakpoint : 575.99,
          settings:{
            slidesToShow:1
          }
        }
      ]
  }); 


  //추천교과목 슬라이드
  $(".item_reco_subj .box_wrap").slick({
    slidesToScroll:1, 
    slidesToShow:3,
    arrows:true,
    dots:true, 
    autoplay:false,
    autoplaySpeed:2000,
    responsive:[
      {
        breakpoint: 1399.99, 
        settings:{
          slidesToShow:2,
        }
      },
      {
        breakpoint: 575.99,
        settings:{
          slidesToShow:1,
        }
      }
    ]
  });

  $(".nav-recomm").on("click", function (){
    $('.box_wrap').slick('setPosition');
  });
  

  //추천비교과 슬라이드
  $(".item_reco_nonSubj .box_wrap").slick({
    slidesToScroll:1, 
    slidesToShow:3,
    arrows:false,
    dots:true, 
    autoplay:false,
    autoplaySpeed:2000,
    rows:2,
    responsive:[
      {
        breakpoint: 1399.99, 
        settings:{
          slidesToShow:2,
        }
      },
      {
        breakpoint:575.99,
        settings:{
          rows:1,
          slidesToShow:1
        }
      }
    ]
  });

  $(".nav-recomm").on("click", function (){
    $('.box_wrap').slick('setPosition');
  });



});

function setRangeMonth(date){
	 	var y = date.getFullYear();
	 	var m = date.getMonth() + 1;
	 	var d = date.getDate();
	 	if (m < 10) { m = "0" + m; }
	 	$("#scheYmd").val(y+""+m);
	 	getMonthList(y, m, d, 'month');
	}

	var today = new Date();
	var date = new Date();
	var eventStr = [];
	var eventEnd = [];
	var data = "";
	function prevCalendar() {
		today = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
		buildCalendar();  
	}

	function nextCalendar() {
		today = new Date(today.getFullYear(), today.getMonth() + 1, today.getDate());
		buildCalendar();
	}
	
	function buildCalendar(){
		eventStr = [];
		eventEnd = [];
		var doMonth = new Date(today.getFullYear(),today.getMonth(),1);
		var lastDate = new Date(today.getFullYear(),today.getMonth()+1,0);
		setRangeMonth(lastDate);
		setTimeout(function (){ 
			var tbCalendar = document.getElementById("calendar");
			var tbCalendarYM = document.getElementById("tbCalendarYM");
			var calMon = (today.getMonth() + 1);
			if (calMon < 10) { calMon = "0" + calMon; }
			tbCalendarYM.innerHTML = '<h4>'+today.getFullYear() + "." + calMon + " 학사일정"; 
			while (tbCalendar.rows.length > 2) {
				tbCalendar.deleteRow(tbCalendar.rows.length-1);
			}
			var row = null;
			row = tbCalendar.insertRow();
			var cnt = 0;
			
			var scheMon = today.getMonth() + 1;
			
			for (i=0; i<doMonth.getDay(); i++) {
				cell = row.insertCell();
				cnt = cnt + 1;
			}
			for (i=1; i<=lastDate.getDate(); i++) {
				cell = row.insertCell();
				cell.innerHTML = i;
				cnt = cnt + 1;
				cell.className = "date";
				
				if(eventStr.indexOf(i) > -1 && eventEnd.indexOf(i) > -1){
					cell.className += " sch_start sch_end";
					cell.innerHTML = i;
					
					var tooltip ="";
					$.each(data, function(index, item){
						if(item.frDay == i && item.toDay == i){
							if(tooltip == ""){
								tooltip = item.shafScheNm;	
							}else{
								tooltip += "<br/>"+item.shafScheNm;
							}
						}else{
							if(item.frDay == i && item.frMon == scheMon) {
								if(tooltip == ""){
									tooltip = item.shafScheNm + " 시작";	
								}else{
									tooltip += "<br/>"+item.shafScheNm + " 시작";
								}
							}
							
							if(item.toDay == i && item.toMon == scheMon) {
								if(tooltip == ""){
									tooltip = item.shafScheNm + " 종료";	
								}else{
									tooltip += "<br/>"+item.shafScheNm + " 종료";
								}
							}
						}
					})
					cell.setAttribute("data-bs-toggle", "tooltip");
					cell.setAttribute("data-bs-placement", "bottom");
					cell.setAttribute("data-bs-html", "true");
					cell.setAttribute("title", tooltip);
				}else if(eventStr.indexOf(i) > -1){
					cell.className += " sch_start";
					cell.innerHTML = i;
					
					var tooltip ="";
					$.each(data, function(index, item){
						if(item.frDay == i && item.toDay == i){
							if(tooltip == ""){
								tooltip = item.shafScheNm;	
							}else{
								tooltip += "<br/>"+item.shafScheNm;
							}
						}else{
							if(item.frDay == i && item.frMon == scheMon) {
								if(tooltip == ""){
									tooltip = item.shafScheNm + " 시작";	
								}else{
									tooltip += "<br/>"+item.shafScheNm + " 시작";
								}
							}
						}
					})
					cell.setAttribute("data-bs-toggle", "tooltip");
					cell.setAttribute("data-bs-placement", "bottom");
					cell.setAttribute("data-bs-html", "true");
					cell.setAttribute("title", tooltip);
				}else if(eventEnd.indexOf(i) > -1){
					cell.className += " sch_end";
					var tooltip ="";
					$.each(data, function(index, item){
						if(item.frDay == i && item.toDay == i){
							if(tooltip == ""){
								tooltip = item.shafScheNm;	
							}else{
								tooltip += "<br/>"+item.shafScheNm;
							}
						}else{
							if(item.toDay == i && item.toMon == scheMon) {
								if(tooltip == ""){
									tooltip = item.shafScheNm + " 종료";	
								}else{
									tooltip += "<br/>"+item.shafScheNm + " 종료";
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
				if (cnt%7 == 0){
					cell.innerHTML = i;
					row = calendar.insertRow();
				}
			}
			
			for (i=1; i<=6; i++) {
				if (cnt%7 == 0){
					break;
				} else {
					cell = row.insertCell();
					cnt = cnt + 1;	 
				}
			};
		} , 200); 
		
		$(document.body).tooltip({
			selector: "[data-bs-toggle='tooltip']"
		});
	}
	function getDay(idx) { 
		var date = ""; 
		switch (idx){ 
			case 0: date = "일"; break; 
			case 1: date = "월"; break; 
			case 2: date = "화"; break; 
			case 3: date = "수"; break; 
			case 4: date = "목"; break; 
			case 5: date = "금"; break; 
			case 6: date = "토"; break; 
		} 
		return date; 
	}
	function getEventStr(date, tit){
		eventStr.push(date);
		return eventStr;
	}
	function getEventEnd(date, tit){
		eventEnd.push(date);
		return eventEnd;
	}
	function getMonthList(year, month, day, action){
		var scheYmd = $("#scheYmd").val();
		$.ajax({
			type:'POST', 
			beforeSend:function(request){request.setRequestHeader('Ajax', 'true');},
			url: '/web/main/shafScheList.json?mId=1',
			data: {scheYmd : scheYmd},
			success:function(result){
				data = result.shafSche;
				var html ="";
				$.each(data, function(index, item){
					var dateStrDt = to_date(item.frDt);
					var dateEndDt = to_date(item.toDt);
					
					var sm = dateStrDt.getMonth()+1;
					var sd = dateStrDt.getDate();
					
					if(item.frDt != item.toDt){
						var em = dateEndDt.getMonth()+1;
						var ed = dateEndDt.getDate();
						if(em == month){
							getEventEnd(ed, item.shafScheNm);
						}
						html 	+= '<dl class="school_sch d-flex flex-wrap gap-2">'
	                    	+'		<dt class="d-flex flex-row gap-3"><span>'+sm+'.'+sd+'</span><span>'+em+'.'+ed+'</span></dt>'
	                    	+'		<dd>'+item.shafScheNm+'</dd>'
	                		+'	</dl>';
						
					}else{
						html 	+= '<dl class="school_sch d-flex flex-wrap gap-2">'
	                    	+'		<dt class="d-flex flex-row gap-3"><span>'+sm+'.'+sd+'</span></dt>'
	                    	+'		<dd>'+item.shafScheNm+'</dd>'
	                		+'	</dl>';
					}
					
					if(sm == month){
						getEventStr(sd, item.shafScheNm);
					}
					
				});
				$("#shafScheList").empty();
				$("#shafScheList").html(html);
			}, error:function() {
			}
		});
	}
	function to_date(date_str){
		var yyyyMMdd = String(date_str);
		var sYear = yyyyMMdd.substring(0,4);
		var sMonth = yyyyMMdd.substring(4,6);
		var sDate = yyyyMMdd.substring(6,8);
	    return new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
	}
