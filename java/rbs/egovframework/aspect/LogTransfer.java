package rbs.egovframework.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rbs.egovframework.LoginVO;

import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

public class LogTransfer {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogTransfer.class);
	
	/**
	 * 등록
	 * @param joinPoint
	 * @param result
	 * @throws Exception
	 */
	public void insertLog(JoinPoint joinPoint, Object result) throws Exception {
		setLog(1, joinPoint, result);
	}
	
	/**
	 * 수정
	 * @param joinPoint
	 * @param result
	 * @throws Exception
	 */
	public void updateLog(JoinPoint joinPoint, Object result) throws Exception {
		setLog(2, joinPoint, result);
	}
	
	/**
	 * 삭제
	 * @param joinPoint
	 * @param result
	 * @throws Exception
	 */
	public void deleteLog(JoinPoint joinPoint, Object result) throws Exception {
		setLog(3, joinPoint, result);
	}
	
	/**
	 * 복구
	 * @param joinPoint
	 * @param result
	 * @throws Exception
	 */
	public void restoreLog(JoinPoint joinPoint, Object result) throws Exception {
		setLog(4, joinPoint, result);
	}
	
	/**
	 * 완전삭제
	 * @param joinPoint
	 * @param result
	 * @throws Exception
	 */
	public void cdeleteLog(JoinPoint joinPoint, Object result) throws Exception {
		setLog(5, joinPoint, result);
	}
	
	private void setLog(int logType, JoinPoint joinPoint, Object result){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();	// request
		boolean isAdmMode = StringUtil.getBoolean(request.getAttribute("isAdmMode"));											// 관리자사이트여부
		int resultInt = StringUtil.getInt(result);
		if(isAdmMode && resultInt >= 1) {
			// 관리자 사이트인 경우만 로그 남길 경우 && 결과값 >= 1
			LOGGER.debug("execute LogTransfer.setLog " + logType + " 시작");
			
			// 1. log에 사용할 정보 예시
			Signature signature = joinPoint.getSignature();
			Object target = joinPoint.getTarget();
			LOGGER.debug("target.getClass().getName():" + target.getClass().getName());		// class명
			LOGGER.debug("signature.getName():" + signature.getName());						// 함수명
			LOGGER.debug("signature.toLongString():" + signature.toLongString());			// 함수명 전체
			LOGGER.debug("result:" + result);												// 함수 return값
			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();			// 로그인 사용자 정보
			
			String regiIdx = loginVO.getMemberIdx();					// 회원코드
			String regiId = loginVO.getMemberIdOrg();					// 회원ID
			String regiName = loginVO.getMemberNameOrg();				// 회원명
			String remoteAddr = ClientUtil.getClientIp(request);		// 접속IP
			String userAgent = request.getHeader("User-Agent");			// Agent
			String browser = null;										// 브라우저 - 추가작업 요함
			String os = null;											// OS - 추가작업 요함
			if(userAgent != null) {
				// 브라우저
				if(userAgent.indexOf("Trident") > -1) browser = "MSIE";
				else if (userAgent.indexOf("OPR/") > -1 || userAgent.indexOf("Opera") > -1) browser = "Opera";
				else if(userAgent.indexOf("Chrome") > -1) browser = "Chrome";
				else if(userAgent.indexOf("Firefox") > -1) browser = "Firefox";
				else if (userAgent.indexOf("iPhone") > -1 && userAgent.indexOf("Mobile") > -1) browser = "iPhone";
				else if (userAgent.indexOf("Android") > -1 && userAgent.indexOf("Mobile") > -1) browser = "Android";

				// OS
				if(userAgent.indexOf("NT 6.3") != -1) os = "Windows 10";
				else if(userAgent.indexOf("NT 6.1") != -1) os = "Windows 7";
				else if(userAgent.indexOf("NT 6.0") != -1) os = "Windows Vista";
				else if(userAgent.indexOf("NT 5.2") != -1) os = "Windows Server 2003";
				else if(userAgent.indexOf("NT 5.1") != -1) os = "Windows XP";
				else if(userAgent.indexOf("NT 5.0") != -1) os = "Windows 2000";
				else if(userAgent.indexOf("NT") != -1) os = "Windows NT";
				else if(userAgent.indexOf("9x 4.90") != -1) os = "Windows Me";
				else if(userAgent.indexOf("98") != -1) os = "Windows 98";
				else if(userAgent.indexOf("95") != -1) os = "Windows 95";
				else if(userAgent.indexOf("Win16") != -1) os = "Windows 3.x";
				else if(userAgent.indexOf("Windows") != -1) os = "Windows";
				else if(userAgent.indexOf("Linux") != -1) os = "Linux";
				else if(userAgent.indexOf("Macintosh") != -1) os = "Macintosh";
			}
			
			// 2. DB저장로직 추가
			
			//LOGGER.debug("- result:" + result);
			LOGGER.debug("execute LogTransfer.setLog " + logType + " 종료");
		}
	}
}
