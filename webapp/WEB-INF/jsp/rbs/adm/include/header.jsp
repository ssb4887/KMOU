<%@page import="com.woowonsoft.egovframework.util.MenuUtil"%>
	<!-- headerWrap -->
		<div id="header">
			<div class="gnb_bx">	
				<ul>
				<li><h2 style="color:#444;font-size:35px;" id="headerMenuTitle" class="title" >${usrCrtMenu.menu_name }				
				<span style="color:gray; font-size:15px;" id="headerMenuPath">
				<c:if test="${!empty usrCrtMenu.menu_name2}">				
				${usrCrtMenu.menu_name2}
				
				</c:if>
				<c:if test="${!empty usrCrtMenu.menu_name3}">	
				>
				${usrCrtMenu.menu_name3}
				
				</c:if>
				<c:if test="${!empty usrCrtMenu.menu_name4}">	
				>
				${usrCrtMenu.menu_name4}
				
				</c:if>
				<c:if test="${!empty usrCrtMenu.menu_name5}">
				>	
				${usrCrtMenu.menu_name5}
				</c:if>
				</span>
				</h2>		

				</li>
				</ul>
			</div>
			
			<div class="utilWrap" style="height:50px;">
				
				<div class="utilSearch">
				<form id="fn_searchTWebsiteForm" name="fn_searchTWebsiteForm" method="post" action="<%=MenuUtil.getMenuUrl(30) %>" target="submit_target">
					<input type="hidden" id="selToUrl" name="selToUrl"/>
					<fieldset>
						<legend class="blind">사이트검색 폼</legend>
						<label class="tit blind" for="selSiteId">사이트선택</label>
						<select id="selSiteId" name="selSiteId" class="select">
							<c:forEach var="websiteDt" items="${usrSiteList}">
							<option value="<c:out value="${websiteDt.OPTION_CODE}"/>"<c:if test="${websiteDt.OPTION_CODE == usrSiteVO.siteId}"> selected="selected"</c:if>><c:out value="${websiteDt.OPTION_NAME}"/></option>
							</c:forEach>
						</select>
						<button type="submit" class="btnSearch" id="fn_btn_search_website">선택</button>
					</fieldset>
				</form>
				</div>
				<div class="topLink" >
					<hr class="toplink_hr" />
					<div><a href="/web/main/main.do?mId=1">사용자페이지 이동</a></div>		
					<hr class="toplink_hr" />
					<div><a href="<%=MenuUtil.getMenuUrl(4) %>" class="bt_logout">로그아웃</a></div>
					<hr class="toplink_hr" />
					<div class="mem"><a style="cursor:default;"><c:out value="${loginVO.memberName}"/></a></div>
				</div>
			</div>
			<script type="text/javascript">
			$(function(){				
				// 내정보수정
				$("#fn_btn_myInfo").click(function(){
					try {
						var varTitle = "내정보수정";
						fn_dialog.open(varTitle, $(this).attr("href") + "&dl=1");
					}catch(e){}
					return false;
				});
				
				// 사용자사이트 선택
				$("#fn_searchTWebsiteForm").submit(function(){
					var varConfirm = confirm("<spring:message code="message.website.selectSite.change.confirm"/>");
					if(!varConfirm) {
						$(this).reset();
						return false;
					}
					return true;
				});
			});
			</script>

		</div>
		<!-- gnb -->
		<div class="head_box" id="headbox">
			<h1 class="logo"><a href="<%=MenuUtil.getMenuUrl(1) %>" ><img src="<c:out value="${contextPath}${imgPath}/layout/logo_temp.svg"/>" alt="통합관리도구" /></a></h1><!-- 이동근 : 해양대 로고로 변경해야함 -->
			
			<h2 class="blind">주메뉴</h2>			
			
			<c:choose>
				<c:when test="${!empty menuType && menuType != 0}">
					<%@ include file="menuContentsLeft.jsp" %>
				</c:when>
				<c:otherwise>
					
					<mnui:contMenuLnb ulId="fn_contMenuUL" ulClass="gnb" menuList="${usrSiteMenuList}" menus="${usrSiteMenus}" />
					<c:if test="${loginVO.usertypeIdx eq '50'}">
						<mnui:contMenuLnbADM ulId="fn_contMenuUL" ulClass="gnb"  menuList="${siteMenuList}" menus="${siteMenus}" />
					</c:if>
				</c:otherwise>
			</c:choose>
		</div>
	
			<script type="text/javascript">
			
// 			$(document).ready(function(){
// 				var currentUrl = window.location.pathname			
// 				currentUrl = (currentUrl).substring(0, (currentUrl).lastIndexOf('/'))
// 				$(".gnb .depth1 a").each(function(){
					
					
				
// 					var linkUrl = ($(this).attr('href')).substring(0, ($(this).attr('href')).lastIndexOf('/'))
// 					if(currentUrl == linkUrl){
// 						$(this).css('color','#ACE6FF');
// 						$(this).closest('.depth1').removeClass('off').addClass('on').find('*').not(".parentA").show();
// 						var headTitle = "";						
// 						$("#headerMenuTitle").text($(this).attr('title'));
					
// 					}
// 				})
				
// 			})

						
			$(document).ready(function(){
				var currentUrl = window.location.pathname + window.location.search;			
				$(".gnb .depth1 a").each(function(){
					var linkUrl = $(this).attr('href');
					
					if(currentUrl == linkUrl){
						$(this).css('color','#ACE6FF');
						$(this).closest('.depth1').removeClass('off').addClass('on').find('*').not(".parentA").show();
						$("#headerMenuTitle").text($(this).attr('title'));
					}
				})
				
			})
			
			function openNav() {
			  document.getElementById("header").style.paddingLeft = "330px";
			  document.getElementById("headbox").style.width = "280px";
			  document.getElementById("headbox").style.overflow = "visible";
			  document.getElementById("openbtn").style.display = "none";
			  document.getElementById("closebtn").style.display = "block";
			}

			function closeNav() {
			  document.getElementById("header").style.paddingLeft= "130px";
			  document.getElementById("headbox").style.width = "0";
			  document.getElementById("headbox").style.overflow = "hidden";
			  document.getElementById("openbtn").style.display = "block";
			  document.getElementById("closebtn").style.display = "none";
			}
			
			
			$("body").on("click",".depth1",function(){			
								
				if($(this).hasClass("off")){
					$(".depth1").removeClass("on").addClass("off").find("*").not(".parentA").hide();
					$(this).removeClass('off').addClass('on').find('*').not(".parentA").show();
				}else{
					$(this).removeClass('on').addClass('off').find('*').not(".parentA").hide();
				}
				
			})
			
			
			</script>

			<a href="javascript:void(0)" onclick="closeNav()" id="closebtn"><button class="nav_close"></button></a>
			<a href="javascript:void(0)" onclick="openNav()" id="openbtn"><button class="nav_open"></button></a>	
				
		<div class="allMenu">
			<h2 class="blind">전체메뉴</h2>
			<mnui:sitemap menuList="${siteMenuList}" menus="${siteMenus}" />
			<div class="btnClose"><a href="#">닫기</a></div>
		</div>