$(function(){		
		var varAllMenu = {
			sDMenuTop:0
			, show:function(){
				if(varAllMenu.sDMenuTop == 0) {
					/*var varGnbDiv = $("#gnb>li.on>div");
					if(varGnbDiv.size() == 0) varGnbDiv = $("#gnb>li:first>div");
					varAllMenu.sDMenuTop = varGnbDiv.offset().top / 2;*/
					//varAllMenu.sDMenuTop = ($("#gnb>li:first").offset().top + $("#gnb>li:first").height()) / 2;
					varAllMenu.sDMenuTop = $("#gnb>li:first").offset().top - 5;
				}
				$(".allMenu").show(1, function(){
					$("#gnb>li>div").css("top", varAllMenu.sDMenuTop + $(".allMenu").height() + "px");
				});
			}, hide:function(){
				$(".allMenu").hide(1, function(){
					$("#gnb>li>div").css("top", varAllMenu.sDMenuTop + "px");
				});
			}
		};
		$(".btnAllMenu").click(function(){
			varAllMenu.show();
			return false;
		});

		$(".allMenu .btnClose").click(function(){
			varAllMenu.hide();
			return false;
		});
	});