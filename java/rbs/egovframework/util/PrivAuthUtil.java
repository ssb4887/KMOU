package rbs.egovframework.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.StringUtil;

/**
 * 본인인증 util
 * @author user
 *
 */
public class PrivAuthUtil {
	
	/**
	 * 본인인증 사용여부
	 * @return
	 */
	public static boolean isUseNameAuth(){
		String isUse = "1"; // 실명인증 사용(1), 사용하지 않음(0)
		String useNice = RbsProperties.getProperty("Globals.nice.use");
		String useIpin = RbsProperties.getProperty("Globals.ipin.use");
		String useGpin = RbsProperties.getProperty("Globals.gpin.use");
		if(StringUtil.isEquals(useNice, isUse) || StringUtil.isEquals(useIpin, isUse) || StringUtil.isEquals(useGpin, isUse)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isUseNiceAuth(){
		String isUse = "1"; // 실명인증 사용(1), 사용하지 않음(0)
		String useNice = RbsProperties.getProperty("Globals.nice.use");
		if(StringUtil.isEquals(useNice, isUse)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isUseIpingAuth(){
		String isUse = "1"; // 실명인증 사용(1), 사용하지 않음(0)
		String useIpin = RbsProperties.getProperty("Globals.ipin.use");
		if(StringUtil.isEquals(useIpin, isUse)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isUseGpinAuth(){
		String isUse = "1"; // 실명인증 사용(1), 사용하지 않음(0)
		String useGpin = RbsProperties.getProperty("Globals.gpin.use");
		if(StringUtil.isEquals(useGpin, isUse)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param isRegi
	 * @return
	 */
	public static boolean isNameAuthCheck(boolean isRegi) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		
		String sAuthType = null;
		String sName = null;
		String sBirthDate = null;
		String sGender = null;
		String sNationalInfo = null;
		String sDupInfo = null;
		String sConnInfo = null;
		
		if(session.getAttribute("sAuthType") != null) sAuthType = StringUtil.getString(session.getAttribute("sAuthType"));
		if(session.getAttribute("sName") != null) sName = StringUtil.getString(session.getAttribute("sName"));
		if(session.getAttribute("sBirthDate") != null) sBirthDate = StringUtil.getString(session.getAttribute("sBirthDate"));
		if(session.getAttribute("sGender") != null) sGender = StringUtil.getString(session.getAttribute("sGender"));
		if(session.getAttribute("sNationalInfo") != null) sNationalInfo = StringUtil.getString(session.getAttribute("sNationalInfo"));
		if(session.getAttribute("sDupInfo") != null) sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"));
		if(session.getAttribute("sConnInfo") != null) sConnInfo = StringUtil.getString(session.getAttribute("sConnInfo"));
		
		// 재인증 방지
		if(StringUtil.isEmpty(sName) || StringUtil.isEmpty(sDupInfo)){
			if(session.getAttribute("iSAuthType") != null) sAuthType = StringUtil.getString(session.getAttribute("iSAuthType"));
			if(session.getAttribute("iSName") != null) sName = StringUtil.getString(session.getAttribute("iSName"));
			if(session.getAttribute("iSBirthDate") != null) sBirthDate = StringUtil.getString(session.getAttribute("iSBirthDate"));
			if(session.getAttribute("iSGender") != null) sGender = StringUtil.getString(session.getAttribute("iSGender"));
			if(session.getAttribute("iSNationalInfo") != null) sNationalInfo = StringUtil.getString(session.getAttribute("iSNationalInfo"));
			if(session.getAttribute("iSDupInfo") != null) sDupInfo = StringUtil.getString(session.getAttribute("iSDupInfo"));
			if(session.getAttribute("iSConnInfo") != null) sConnInfo = StringUtil.getString(session.getAttribute("iSConnInfo"));
		}
		
		if(StringUtil.isEmpty(sName) || StringUtil.isEmpty(sDupInfo)) return false;

		// 본인인증세션 삭제
		removeNameAuthPreSession();
		
		// 재인증 방지 
		if(isRegi){
			session.setAttribute("iSAuthType", sAuthType);
			session.setAttribute("iSName", sName);
			session.setAttribute("iSBirthDate", sBirthDate);
			session.setAttribute("iSGender", sGender);
			session.setAttribute("iSNationalInfo", sNationalInfo);
			session.setAttribute("iSDupInfo", sDupInfo);
			session.setAttribute("iSConnInfo", sConnInfo);
		}
		
		return true;
	}
	
	/**
	 * 본인인증 세션 삭제
	 */
	public static void removeNameAuthPreSession(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute("sAuthType", null);
		session.setAttribute("sName", null);
		session.setAttribute("sBirthDate", null);
		session.setAttribute("sGender", null);
		session.setAttribute("sNationalInfo", null);
		session.setAttribute("sDupInfo", null);
		session.setAttribute("sConnInfo", null);
	}
	
	public static void removeNameAuthSession(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute("iSAuthType", null);
		session.setAttribute("iSName", null);
		session.setAttribute("iSBirthDate", null);
		session.setAttribute("iSGender", null);
		session.setAttribute("iSNationalInfo", null);
		session.setAttribute("iSDupInfo", null);
		session.setAttribute("iSConnInfo", null);
	}

	/**
	 * 본인인증 법정 대리인 세션 삭제
	 */
	public static void removeNameAuthPrePntSession(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute("pnt_sAuthType", null);
		session.setAttribute("pnt_sName", null);
		session.setAttribute("pnt_sBirthDate", null);
		session.setAttribute("pnt_sGender", null);
		session.setAttribute("pnt_sNationalInfo", null);
		session.setAttribute("pnt_sDupInfo", null);
		session.setAttribute("pnt_sConnInfo", null);
	}
	
	public static void removeNameAuthPntSession(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession(true);
		session.setAttribute("pnt_iSAuthType", null);
		session.setAttribute("pnt_iSName", null);
		session.setAttribute("pnt_iSBirthDate", null);
		session.setAttribute("pnt_iSGender", null);
		session.setAttribute("pnt_iSNationalInfo", null);
		session.setAttribute("pnt_iSDupInfo", null);
		session.setAttribute("pnt_iSConnInfo", null);
	}
}