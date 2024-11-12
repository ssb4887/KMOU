<%@ include file="../../../../../include/commonTop.jsp"%>
<!-- STEP -->
<div class="regStep">
	<ol>
		<c:set var="step" value="1"/>
		<li class="regs${step} step${elfn:getLPAD(step, '0', 2)}">
			<span class="num">${step}단계</span>
			<strong>약관동의</strong>
		</li>
		<c:if test="${elfn:isUseNameAuth()}">
		<c:set var="step" value="${step + 1}"/>
		<li class="regs${step} step${elfn:getLPAD(step, '0', 2)}">
			<span class="num">${step}단계</span>
			<strong>실명인증</strong>
		</li>
		</c:if>
		<c:set var="step" value="${step + 1}"/>
		<li class="regs${step} step${elfn:getLPAD(step, '0', 2)}">
			<span class="num">${step}단계</span>
			<strong>정보입력</strong>
		</li>
		<c:set var="step" value="${step + 1}"/>
		<li class="regs${step} step${elfn:getLPAD(step, '0', 2)}">
			<span class="num">${step}단계</span>
			<strong>가입완료</strong>
		</li>
	</ol>
</div>
<!-- //STEP -->
