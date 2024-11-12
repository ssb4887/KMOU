<%@ taglib prefix="elui" uri="/WEB-INF/tlds/el-tag.tld"%>
<%@ taglib prefix="itui" tagdir="/WEB-INF/tags/item" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="tab1Url" value="${URL_MODIFY}"/>
<c:choose>
	<c:when test="${empty param.dt}"><c:set var="tab1Url" value="${URL_INPUT}"/></c:when>
	<c:when test="${param.dt eq 'fromList'}"><c:set var="tab1Url" value="${URL_MODIFY}"/></c:when>
</c:choose>
<c:set var="tab2Url" value="${URL_ABILITY_INPUT}"/>
<c:set var="tab3Url" value="${URL_MAJOR_LIST}"/>
<c:set var="tab4Url" value="${URL_NON_SBJT_INPUT}"/>
<c:set var="tab5Url" value="${URL_LICENSE_INPUT}"/>
	

<div class="tabMenuC">
	<ul class="tabTypeA tab li4" style="margin-bottom: 40px;">
		 <li class="fn_tab_view <c:if test="${param.tab == '1' }"> on</c:if>" id="tablist1" title="기본정보"><a href="<c:out value="${tab1Url}"/>">기본정보</a></li>
<%-- 		 <li class="fn_tab_view <c:if test="${param.tab == '2' }"> on</c:if>" id="tablist2" title="학부 교육과정"><a href="<c:out value="${tab2Url}"/>">학부 교육과정</a></li> --%>
		 <li class="fn_tab_view <c:if test="${param.tab == '2' }"> on</c:if>" id="tablist2" title="전공능력">
		 	<c:if test="${empty param.dt}"><button onclick="alert('기본정보를 저장해주세요.')">전공능력</button></c:if>
		 	<c:if test="${!empty param.dt}"><a href="<c:out value="${tab2Url}"/>">전공능력</a></c:if>
	 	</li>
		 <li class="fn_tab_view <c:if test="${param.tab == '3' }"> on</c:if>" id="tablist3" title="학부 교육과정">
		 	<c:if test="${empty param.dt}"><button onclick="alert('기본정보를 저장해주세요.')">교육과정</button></c:if>
		 	<c:if test="${!empty param.dt}"><a href="<c:out value="${tab3Url}"/>">교육과정</a></c:if>
		 </li>
		 <li class="fn_tab_view <c:if test="${param.tab == '4' }"> on</c:if>" id="tablist4" title="비교과">
		 	<c:if test="${empty param.dt}"><button onclick="alert('기본정보를 저장해주세요.')">비교과</button></c:if>
		 	<c:if test="${!empty param.dt}"><a href="<c:out value="${tab4Url}"/>">비교과</a></c:if>
		 </li>
		 <li class="fn_tab_view <c:if test="${param.tab == '5' }"> on</c:if>" id="tablist5" title="관련자격증">
		 	<c:if test="${empty param.dt}"><button onclick="alert('기본정보를 저장해주세요.')">관련자격증</button></c:if>
		 	<c:if test="${!empty param.dt}"><a href="<c:out value="${tab5Url}"/>">관련자격증</a></c:if>
		 </li>
		 
		 <li class="fn_tab_view" id="tablist4" title="전문인 관리">
		 	<c:if test="${empty param.dt}"><button onclick="alert('기본정보를 저장해주세요.')">진출분야 관리</button></c:if>
<%-- 		 	<c:if test="${!empty param.dt}"><a href="#" onclick="window.open('/RBISADM/code/optnList.do?mId=52&mstCd=SPT_PSN_CD&dl=1&tit=${dt.MJ_NM_KOR}&isMajorInfo=Y&mjCd=${dt.MJ_CD}&slang=ko', '_blank', 'width=1000, height=500')">전문인 관리</a></c:if>		 	 --%>
		 	<c:if test="${!empty param.dt}"><a href="#" onclick="window.open('${contextPath }/RBISADM/code/optnList.do?mId=52&mstCd=FIELD&dl=1&tit=${dt.MAJOR_NM_KOR}&isMajorInfo=Y&majorCd=${dt.MAJOR_CD}&slang=ko', '_blank', 'width=1000, height=500')">진출분야 관리</a></c:if>		 	
<%-- 		 	<c:if test="${!empty param.dt}"><a href="<c:out value="${URL_SPT_PSN_VIEW}&mstCd=SPT_PSN_CD&dl=1&tit=${dt.MJ_NM_KOR}&isMajorInfo=Y&mjCd=${dt.MJ_CD}&year=${dt.YY}&slang=ko"/>">학부 교육과정</a></c:if> --%>
		 </li>
	</ul>
</div>
