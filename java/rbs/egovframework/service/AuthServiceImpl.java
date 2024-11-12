package rbs.egovframework.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.service.AuthService;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import rbs.egovframework.LoginVO;

public class AuthServiceImpl extends EgovAbstractServiceImpl implements AuthService {
	/**
	 * 현재 설정된 세부권한 중 해당권한 여부 체크 : 이미 MENU_AUTH가 설정되어 있는 경우
	 * @param moduleAuthName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean isComAuthenticated(String moduleAuthName) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HashMap<String, Boolean> auths = null;
		Object authObjs = request.getAttribute("MENU_AUTH");
		
		// 권한 설정이 없는 경우
		if(authObjs == null) return null;
		
		if(authObjs instanceof HashMap) {
			auths = (HashMap<String, Boolean>) authObjs;
			Boolean isAuth = auths.get("AUTH_" + moduleAuthName);
			//if(isAuth == null) return null;
				
			if(isAuth != null && isAuth) return isAuth;
			
			// 글쓰기(상위권한 - 전체관리,완전삭제) / 전체관리(상위권한 - 완전삭제) 권한 인 경우 - 상위권한 체크
			if(StringUtil.isEquals(moduleAuthName, "WRT") || 
					StringUtil.isEquals(moduleAuthName, "RWT") || 
					StringUtil.isEquals(moduleAuthName, "MWT") || 
					StringUtil.isEquals(moduleAuthName, "MNG")) {
			
				int chkSIdx = 0;
				String[] checkAuthNames = {"MNG", "CDT"};
				if(StringUtil.isEquals(moduleAuthName, "MNG")) chkSIdx = 1;
				
				Object checkAuthObj = null;
				int checkAuthLen = checkAuthNames.length;
				for(int i = chkSIdx ; i < checkAuthLen ; i ++) {
					checkAuthObj = auths.get("AUTH_" + checkAuthNames[i]);
					if(checkAuthObj != null) {
						isAuth = StringUtil.getBoolean(checkAuthObj);
						if(isAuth != null && isAuth) break;
					}
				}
			}
			
			return isAuth;
		}
		
		return true;
	}
	
	/**
	 * 체크권한 설정파일 없는 경우, 설정권한 없는 경우 return -2<br/>
	 * 권한있는 경우 return 1
	 * @param moduleAuthName	: 권한id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getAuthType(String moduleAuthName) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HashMap<String, Boolean> auths = null;
		Object authObjs = request.getAttribute("MENU_AUTH");
		
		// 체크권한 설정파일 없는 경우
		if(authObjs == null) return -2;
		
		if(authObjs instanceof HashMap) {
			auths = (HashMap<String, Boolean>) authObjs;
			Boolean isAuthClosed = auths.get("AUTH_CLOSED_" + moduleAuthName);
			if(isAuthClosed != null && isAuthClosed) return -1;
			Boolean isAuth = auths.get("AUTH_" + moduleAuthName);
			// 설정권한 없는 경우
			if(isAuth == null) return -2;
			
			// 접근권한 있는 경우
			if(isAuth != null && isAuth) return 1;
			
			// 글쓰기(상위권한 - 전체관리,완전삭제) / 전체관리(상위권한 - 완전삭제) 권한 인 경우 - 상위권한 체크
			if(StringUtil.isEquals(moduleAuthName, "WRT") || 
					StringUtil.isEquals(moduleAuthName, "RWT") || 
					StringUtil.isEquals(moduleAuthName, "MWT") || 
					StringUtil.isEquals(moduleAuthName, "MNG")) {
			
				int chkSIdx = 0;
				String[] checkAuthNames = {"MNG", "CDT"};
				if(StringUtil.isEquals(moduleAuthName, "MNG")) chkSIdx = 1;
				
				Object checkAuthObj = null;
				int checkAuthLen = checkAuthNames.length;
				for(int i = chkSIdx ; i < checkAuthLen ; i ++) {
					checkAuthObj = auths.get("AUTH_" + checkAuthNames[i]);
					if(checkAuthObj != null) {
						isAuth = StringUtil.getBoolean(checkAuthObj);
						if(isAuth != null && isAuth) break;
					}
				}
			}
			
			return (isAuth)?1:0;
		}
		
		return 1;
	}
	
	/**
	 * 현재 메뉴의 권한 여부 체크 : 요청권한에 해당하는 값이 null인 경우 return true 
	 * 현재 설정된 세부권한 중 해당권한 여부 체크 : 이미 MENU_AUTH가 설정되어 있는 경우
	 * @param moduleAuthName	: 권한id
	 * @return
	 */
	@Override
	public Boolean isAuthenticated(String moduleAuthName) {
		Boolean isAuth = isComAuthenticated(moduleAuthName);
		if(isAuth == null) return true;
				
		return isAuth;
	}

	/**
	 * 현재 메뉴의 세부권한 등급 얻기
	 * 현재 설정된 세부권한 중 해당권한 등급 얻기 : 이미 MENU_AUTH가 설정되어 있는 경우
	 * @param moduleAuthName	: 권한id
	 * @return
	 */
	@Override
	public int getModuleUtp(String moduleAuthName) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		JSONObject moduleAuth = JSONObjectUtil.getJSONObject(request.getAttribute("crtModuleAuth"));
		return JSONObjectUtil.getInt(moduleAuth, moduleAuthName + "_UTP");
	}
	
	/**
	 * 현재 메뉴의 권한 여부 : 메뉴접근권한, 모듈세부권한 - 모듈권한 설정 후 권한 체크
	 * @param moduleAuthName	: 권한id
	 * @return
	 */
	@Override
	public Boolean isSetAuthenticated(String[] moduleAuthNames) {
		return isSetAuthenticated(null, null, null, moduleAuthNames);
	}
	
	/**
	 * 메뉴, 모듈 세부 권한 인증 여부 설정
	 * 모듈세부권한 설정 없는 경우 : 메뉴기본권한
	 * @param crtMenu			: 메뉴정보
	 * @param authManagerArray	: 권한관리항목정보
	 * @param moduleAuth		: 권한정보
	 * @return
	 */
	@Override
	public HashMap<String, Boolean> getMenuAuthenticated(JSONObject crtMenu, JSONArray authManagerArray, JSONObject moduleAuth) {
		return getMenuAuthenticated(null, crtMenu, authManagerArray, moduleAuth);
	}
	
	/**
	 * 메뉴, 모듈 세부 권한 인증 여부 설정
	 * 모듈세부권한 설정 없는 경우 : 메뉴기본권한
	 * @param preMenuFlag 		: 메뉴관리권한명 추가
	 * @param crtMenu			: 메뉴정보
	 * @param authManagerArray	: 권한관리항목정보
	 * @param moduleAuth		: 권한정보
	 * @return
	 */
	@Override
	public HashMap<String, Boolean> getMenuAuthenticated(String preMenuFlag, JSONObject crtMenu, JSONArray authManagerArray, JSONObject moduleAuth) {
		HashMap<String, Boolean> auths = new HashMap<String, Boolean>();
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	    request.setAttribute("crtModuleAuth", moduleAuth);
	    
		// 로그인 사용자 권한
		int mbrUtp = 0;
		String[] mbrGrp = null;
		String[] mbrDpt = null;
		String mbrIdx = null;
		//HttpSession session = request.getSession(true);
		Object loginVOObj = UserDetailsHelper.getAuthenticatedUser();//session.getAttribute("loginVO");
		LoginVO loginVO = null;
		if(loginVOObj != null) {
			loginVO = (LoginVO)loginVOObj;												// 로그인 정보
			mbrUtp = loginVO.getUsertypeIdx();											// 사용자유형
			mbrDpt = StringUtil.stringToArray(loginVO.getDepartIdxs(), ",");			// 부서
			mbrIdx = loginVO.getMemberIdx();											// 회원일련번호
		}
		
		List<Object> mbrGrpList = UserDetailsHelper.getAuthorities();
		if(mbrGrpList != null) mbrGrp = (String[])mbrGrpList.toArray(new String[mbrGrpList.size()]);

		// 메뉴 세부권한
		boolean isAuth = true;
		String siteMode = StringUtil.getString(request.getAttribute("siteMode"));
		String moduleAuthName = null;
		
		/*************************************************************************************
		// 1. 메뉴 접근 권한 정보
		 *************************************************************************************/
		if(preMenuFlag == null) preMenuFlag = "";
		moduleAuthName = "MNU";
		int pageUtp = 0;
		String[] pageGrp = null;
		String[] pageDpt = null;
		String[] pageMbr = null;
		// 모듈세부권한 설정 없는 경우 : 메뉴기본권한
		if(JSONObjectUtil.isEmpty(crtMenu)) crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute("crtMenu"));
		pageUtp = StringUtil.getInt(crtMenu.get(preMenuFlag + "usertype_idx"));
		pageGrp = StringUtil.stringToArray(StringUtil.getString(crtMenu.get(preMenuFlag + "group_idxs")), ",");
		pageDpt = StringUtil.stringToArray(StringUtil.getString(crtMenu.get(preMenuFlag + "depart_idxs")), ",");
		pageMbr = StringUtil.stringToArray(StringUtil.getString(crtMenu.get(preMenuFlag + "member_idxs")), ",");

		isAuth = isAuth(isAuth, pageUtp, pageGrp, pageDpt, pageMbr, mbrUtp, mbrGrp, mbrDpt, mbrIdx);
		
		if(isAuth && pageGrp == null && pageDpt == null && pageMbr == null) {
			// 본인인증 / 비회원 권한 여부
			int nmMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");//Defines.CODE_USERTYPE_SMEMBER;
			if(pageUtp == nmMbrType) auths.put("AUTH_NMMEMBER_" + moduleAuthName, new Boolean(true));	// 본인인증권한
			else if(pageUtp == 0) auths.put("AUTH_NOMEMBER_" + moduleAuthName, new Boolean(true));		// 비회원권한
		}
		auths.put("AUTH_" + moduleAuthName, new Boolean(isAuth));

		// 모듈 권한 설정 정보 없는 경우
		if(JSONObjectUtil.isEmpty(authManagerArray)/* || JSONObjectUtil.isEmpty(moduleAuth)*/) return auths;

		/*************************************************************************************
		// 2. 모듈 세부 권한 정보
		 *************************************************************************************/
		String moduleSiteFlag = (!StringUtil.isEmpty(siteMode))?siteMode + "_":"";			// 사이트 모드별 체크 (미사용:closed, 기본권한 사용자유형:min_utp)
		int authManagerSize = authManagerArray.size();
		JSONObject roleInfo = null;

		String admMode = RbsProperties.getProperty("Globals.site.mode.adm");
		int admUserTypeIdx = RbsProperties.getPropertyInt("Globals.code.USERTYPE_ADMIN");
		int closed = 0;
		for(int i = 0 ; i < authManagerSize ; i ++) {
			isAuth = true;
			roleInfo = JSONObjectUtil.getJSONObject(authManagerArray, i);
			moduleAuthName = JSONObjectUtil.getString(roleInfo, "column_id");
			closed = JSONObjectUtil.getInt(roleInfo, moduleSiteFlag + "closed");
			
			if(closed == 1) {
				// 사용하는 페이지이나 해당 모드에서는 접근금지!! : 404
				auths.put("AUTH_CLOSED_" + moduleAuthName, new Boolean(true));
				auths.put("AUTH_" + moduleAuthName, new Boolean(false));
				continue;
			}
			
			if(JSONObjectUtil.isEmpty(moduleAuth)) {
				// 모듈세부권한 설정 없는 경우 : 모듈기본권한
				pageUtp = StringUtil.getInt(roleInfo.get(moduleSiteFlag +"min_utp"));
				pageGrp = null;
				pageDpt = null;
				pageMbr = null;
			} else {
				pageUtp = StringUtil.getInt(moduleAuth.get(moduleAuthName + "_UTP"));
				pageGrp = StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_GRP")), ",");
				pageDpt = StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_DPT")), ",");
				pageMbr = StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_MBR")), ",");
			}
			// 사이트모드 != 관리자모드 경우 페이지접근권한 >= 관리자권한 : 404
			if(!StringUtil.isEquals(siteMode, admMode)) {
				// 사이트모드 != 관리자모드 경우
				if(StringUtil.isEmpty(pageGrp) && StringUtil.isEmpty(pageDpt) && StringUtil.isEmpty(pageMbr) && pageUtp >= admUserTypeIdx) {
					// 페이지접근권한 >= 관리자권한
					auths.put("AUTH_CLOSED_" + moduleAuthName, new Boolean(true));
					auths.put("AUTH_" + moduleAuthName, new Boolean(false));
					continue;
				}
			}

			isAuth = isAuth(isAuth, pageUtp, pageGrp, pageDpt, pageMbr, mbrUtp, mbrGrp, mbrDpt, mbrIdx);
			
			String[] checkAuthNames = {"MNG", "CDT"};
			if(StringUtil.indexOf(checkAuthNames, moduleAuthName) != -1) {
				// 관리권한인 경우
				if(pageUtp <= 0 && StringUtil.isEmpty(pageGrp) && StringUtil.isEmpty(pageDpt) && StringUtil.isEmpty(pageMbr) 
						&& isAuth && mbrUtp < admUserTypeIdx) {
					// 모듈 접근권한이 설정되지 않은 경우 && 로그인 회원이 관리자가 아닌 경우 : 권한 없음
					isAuth = false;
				}
			} else if(isAuth && StringUtil.isEmpty(pageGrp) && StringUtil.isEmpty(pageDpt) && StringUtil.isEmpty(pageMbr)) {
				// 본인인증 / 비회원 권한 여부
				int nmMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");//Defines.CODE_USERTYPE_SMEMBER;
				if(pageUtp == nmMbrType) auths.put("AUTH_NMMEMBER_" + moduleAuthName, new Boolean(true));	// 본인인증권한
				else if(pageUtp == 0) auths.put("AUTH_NOMEMBER_" + moduleAuthName, new Boolean(true));		// 비회원권한
			}
			auths.put("AUTH_" + moduleAuthName, new Boolean(isAuth));
				
		}
		return auths;
	}
	
	/**
	 * 모듈 세부 권한 인증 여부 설정
	 * @param authManagerArray	: 권한관리항목정보
	 * @param moduleAuth		: 권한정보
	 * @return
	 */
	@Override
	public HashMap<String, Boolean> getModuleAuthenticated(JSONArray authManagerArray, JSONObject moduleAuth) {
		HashMap<String, Boolean> auths = new HashMap<String, Boolean>();

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		// 로그인 사용자 권한
		int mbrUtp = 0;
		String[] mbrGrp = null;
		String[] mbrDpt = null;
		String mbrIdx = null;
		//HttpSession session = request.getSession(true);
		Object loginVOObj = UserDetailsHelper.getAuthenticatedUser();//session.getAttribute("loginVO");
		LoginVO loginVO = null;
		if(loginVOObj != null) {
			loginVO = (LoginVO)loginVOObj;												// 로그인 정보
			mbrUtp = loginVO.getUsertypeIdx();											// 사용자유형
			mbrDpt = StringUtil.stringToArray(loginVO.getDepartIdxs(), ",");			// 부서
			mbrIdx = loginVO.getMemberIdx();											// 회원일련번호
		}
		
		List<Object> mbrGrpList = UserDetailsHelper.getAuthorities();
		if(mbrGrpList != null) mbrGrp = (String[])mbrGrpList.toArray(new String[mbrGrpList.size()]);

		// 메뉴 세부권한
		boolean isAuth = true;
		String siteMode = StringUtil.getString(request.getAttribute("siteMode"));
		String moduleAuthName = null;

		int pageUtp = 0;
		String[] pageGrp = null;
		String[] pageDpt = null;
		String[] pageMbr = null;
		/*************************************************************************************
		// 1. 모듈 세부 권한 정보
		 *************************************************************************************/
		String moduleSiteFlag = (!StringUtil.isEmpty(siteMode))?siteMode + "_":"";			// 사이트 모드별 체크 (미사용:closed, 기본권한 사용자유형:min_utp)
		int authManagerSize = authManagerArray.size();
		JSONObject roleInfo = null;

		String admMode = RbsProperties.getProperty("Globals.site.mode.adm");
		int admUserTypeIdx = RbsProperties.getPropertyInt("Globals.code.USERTYPE_ADMIN");
		int closed = 0;
		for(int i = 0 ; i < authManagerSize ; i ++) {
			isAuth = true;
			roleInfo = JSONObjectUtil.getJSONObject(authManagerArray, i);
			moduleAuthName = JSONObjectUtil.getString(roleInfo, "column_id");
			closed = JSONObjectUtil.getInt(roleInfo, moduleSiteFlag + "closed");
			
			if(closed == 1) {
				// 사용하는 페이지이나 해당 모드에서는 접근금지!! : 404
				auths.put("AUTH_CLOSED_" + moduleAuthName, new Boolean(true));
				auths.put("AUTH_" + moduleAuthName, new Boolean(false));
				continue;
			}
			
			if(JSONObjectUtil.isEmpty(moduleAuth)) {
				// 모듈세부권한 설정 없는 경우 : 모듈기본권한
				pageUtp = StringUtil.getInt(roleInfo.get(moduleSiteFlag +"min_utp"));
				pageGrp = null;
				pageDpt = null;
				pageMbr = null;
			} else {
				pageUtp = StringUtil.getInt(moduleAuth.get(moduleAuthName + "_UTP"));
				pageGrp = StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_GRP")), ",");
				pageDpt = StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_DPT")), ",");
				pageMbr = StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_MBR")), ",");
			}
			// 사이트모드 != 관리자모드 경우 페이지접근권한 >= 관리자권한 : 404
			if(!StringUtil.isEquals(siteMode, admMode)) {
				// 사이트모드 != 관리자모드 경우
				if(StringUtil.isEmpty(pageGrp) && StringUtil.isEmpty(pageDpt) && StringUtil.isEmpty(pageMbr) && pageUtp >= admUserTypeIdx) {
					// 페이지접근권한 >= 관리자권한
					auths.put("AUTH_CLOSED_" + moduleAuthName, new Boolean(true));
					auths.put("AUTH_" + moduleAuthName, new Boolean(false));
					continue;
				}
			}

			isAuth = isAuth(isAuth, pageUtp, pageGrp, pageDpt, pageMbr, mbrUtp, mbrGrp, mbrDpt, mbrIdx);
			
			if(isAuth && pageGrp == null && pageDpt == null && pageMbr == null) {
				// 본인인증 / 비회원 권한 여부
				int nmMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");//Defines.CODE_USERTYPE_SMEMBER;
				if(pageUtp == nmMbrType) auths.put("AUTH_NMMEMBER_" + moduleAuthName, new Boolean(true));	// 본인인증권한
				else if(pageUtp == 0) auths.put("AUTH_NOMEMBER_" + moduleAuthName, new Boolean(true));		// 비회원권한
			}
			auths.put("AUTH_" + moduleAuthName, new Boolean(isAuth));
				
		}
		return auths;
	}

	/**
	 * 메뉴, 모듈 세부 권한 인증 여부
	 * 모듈세부권한 설정 없는 경우 : 메뉴기본권한
	 * @param preMenuFlag 		: 메뉴관리권한명 추가
	 * @param crtMenu			: 메뉴정보
	 * @param authManagerArray	: 권한관리항목정보
	 * @param moduleAuth		: 권한정보
	 * @param moduleAuthName 	: 권한id
	 * @return
	 */
	@Override
	public Boolean isModuleSearchAuthenticated(String preMenuFlag, JSONObject crtMenu, JSONArray authManagerArray, JSONObject moduleAuth, String moduleAuthName) {
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	    
		// 로그인 사용자 권한
		int mbrUtp = 0;
		String[] mbrGrp = null;
		String[] mbrDpt = null;
		String mbrIdx = null;
		//HttpSession session = request.getSession(true);
		Object loginVOObj = UserDetailsHelper.getAuthenticatedUser();//session.getAttribute("loginVO");
		LoginVO loginVO = null;
		if(loginVOObj != null) {
			loginVO = (LoginVO)loginVOObj;												// 로그인 정보
			mbrUtp = loginVO.getUsertypeIdx();											// 사용자유형
			mbrDpt = StringUtil.stringToArray(loginVO.getDepartIdxs(), ",");			// 부서
			mbrIdx = loginVO.getMemberIdx();											// 회원일련번호
		}
		
		List<Object> mbrGrpList = UserDetailsHelper.getAuthorities();
		if(mbrGrpList != null) mbrGrp = (String[])mbrGrpList.toArray(new String[mbrGrpList.size()]);

		// 메뉴 세부권한
		boolean isAuth = true;
		String siteMode = StringUtil.getString(request.getAttribute("siteMode"));
		String fmoduleAuthName = null;
		
		/*************************************************************************************
		// 1. 메뉴 접근 권한 정보
		 *************************************************************************************/
		if(preMenuFlag == null) preMenuFlag = "";
		fmoduleAuthName = "MNU";
		int pageUtp = 0;
		String[] pageGrp = null;
		String[] pageDpt = null;
		String[] pageMbr = null;
		// 모듈세부권한 설정 없는 경우 : 메뉴기본권한
		if(JSONObjectUtil.isEmpty(crtMenu)) crtMenu = JSONObjectUtil.getJSONObject(request.getAttribute("crtMenu"));
		pageUtp = StringUtil.getInt(crtMenu.get(preMenuFlag + "usertype_idx"));
		pageGrp = StringUtil.stringToArray(StringUtil.getString(crtMenu.get(preMenuFlag + "group_idxs")), ",");
		pageDpt = StringUtil.stringToArray(StringUtil.getString(crtMenu.get(preMenuFlag + "depart_idxs")), ",");
		pageMbr = StringUtil.stringToArray(StringUtil.getString(crtMenu.get(preMenuFlag + "member_idxs")), ",");

		isAuth = isAuth(isAuth, pageUtp, pageGrp, pageDpt, pageMbr, mbrUtp, mbrGrp, mbrDpt, mbrIdx);
		
		// 모듈 권한 설정 정보 없는 경우
		if(JSONObjectUtil.isEmpty(authManagerArray)/* || JSONObjectUtil.isEmpty(moduleAuth)*/) return isAuth;

		/*************************************************************************************
		// 2. 모듈 세부 권한 정보
		 *************************************************************************************/
		String moduleSiteFlag = (!StringUtil.isEmpty(siteMode))?siteMode + "_":"";			// 사이트 모드별 체크 (미사용:closed, 기본권한 사용자유형:min_utp)
		int authManagerSize = authManagerArray.size();
		JSONObject roleInfo = null;

		int closed = 0;
		for(int i = 0 ; i < authManagerSize ; i ++) {
			isAuth = true;
			roleInfo = JSONObjectUtil.getJSONObject(authManagerArray, i);
			fmoduleAuthName = JSONObjectUtil.getString(roleInfo, "column_id");
			
			if(StringUtil.isEquals(fmoduleAuthName, moduleAuthName)) {
				closed = JSONObjectUtil.getInt(roleInfo, moduleSiteFlag + "closed");
				
				if(closed == 1) {
					// 사용하는 페이지이나 해당 모드에서는 접근금지!!
					return false; 
				}
				
				if(JSONObjectUtil.isEmpty(moduleAuth)) {
					// 모듈세부권한 설정 없는 경우 : 모듈기본권한
					pageUtp = StringUtil.getInt(roleInfo.get(moduleSiteFlag +"min_utp"));
					pageGrp = null;
					pageDpt = null;
					pageMbr = null;
				} else {
					pageUtp = StringUtil.getInt(moduleAuth.get(moduleAuthName + "_UTP"));
					pageGrp = StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_GRP")), ",");
					pageDpt = StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_DPT")), ",");
					pageMbr = StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_MBR")), ",");
				}
	
				isAuth = isAuth(isAuth, pageUtp, pageGrp, pageDpt, pageMbr, mbrUtp, mbrGrp, mbrDpt, mbrIdx);
				
				return isAuth;
			}
		}
		return isAuth;
	}
	
	/**
	 * 메뉴의 권한 여부 : 메뉴접근권한, 모듈세부권한 - 모듈권한 설정 후 권한 체크
	 * @param crtMenu			: 메뉴정보
	 * @param authManagerArray	: 권한관리항목정보
	 * @param moduleAuth		: 권한정보
	 * @param moduleAuthNames	: 권한id
	 * @return
	 */
	@Override
	public Boolean isSetAuthenticated(JSONObject crtMenu, JSONArray authManagerArray, JSONObject moduleAuth, String[] moduleAuthNames) {
		Boolean isAuth = true;
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		// 0. 메뉴, 모듈 세부 권한 인증 여부 설정
		Object authObjs = request.getAttribute("MENU_AUTH");
		if(authObjs == null) request.setAttribute("MENU_AUTH", getMenuAuthenticated(crtMenu, authManagerArray, moduleAuth));
		
		// 1. 메뉴 접근 권한 정보
		isAuth = isComAuthenticated("MNU");
		if(isAuth == null) {
			// 메뉴 권한 설정 없는 경우 메뉴설정 기본권한 체크
			isAuth = true;
		} else if(!isAuth) return isAuth;		// 메뉴 접근 권한 없는 경우 return

		// 2. 모듈 세부 권한 정보
		if(moduleAuthNames != null) {
			boolean isAuthName = false;
			Boolean isModuleAuth = null;
			int moduleAuthLen = moduleAuthNames.length;
			for(int i = 0 ; i < moduleAuthLen ; i ++) {
				isModuleAuth = isComAuthenticated(moduleAuthNames[i]);
				if(isModuleAuth != null) {
					isAuth = isModuleAuth;
					isAuthName = true;
					if(isAuth) break;
				}/* else {
					// 모듈 세부 권한 설정 없는 경우 모듈설정 기본권한 체크
				}*/
			}
			
			// 요청권한에 해당하는 값이 null인 경우 return true 
			if(!isAuthName) isAuth = true;
		}
		
		return isAuth;
	}

	/**
	 * 메뉴, 모듈 세부 권한 여부
	 * @param crtMenu			: 메뉴정보
	 * @param moduleAuthName	: 권한id
	 * @return
	 */
	@Override
	public Boolean isAuthenticated(JSONObject crtMenu, String moduleAuthName) {
		String[] moduleAuthNames = null;
		if(!StringUtil.isEmpty(moduleAuthName)) {
			moduleAuthNames = new String[1];
			moduleAuthNames[0] = moduleAuthName;
		}
		return isAuthenticated(crtMenu, moduleAuthNames);
	}

	/**
	 * 메뉴, 모듈 세부 권한 여부
	 * @param crtMenu			: 메뉴정보
	 * @param moduleAuthNames	: 권한id
	 * @return
	 */
	@Override
	public Boolean isAuthenticated(JSONObject crtMenu, String[] moduleAuthNames) {
		Boolean isAuth = true;
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		// 0. 메뉴, 모듈 세부 권한 인증 여부 설정
		Object authObjs = request.getAttribute("MENU_AUTH");
		if(authObjs == null) return false;
		
		// 1. 메뉴 접근 권한 정보
		isAuth = isComAuthenticated("MNU");
		if(isAuth == null) {
			// 메뉴 권한 설정 없는 경우 메뉴설정 기본권한 체크
			isAuth = true;
		} else if(!isAuth) return isAuth;		// 메뉴 접근 권한 없는 경우 return

		// 2. 모듈 세부 권한 정보
		if(moduleAuthNames != null) {
			boolean isAuthName = false;
			Boolean isModuleAuth = null;
			int moduleAuthLen = moduleAuthNames.length;
			for(int i = 0 ; i < moduleAuthLen ; i ++) {
				isModuleAuth = isComAuthenticated(moduleAuthNames[i]);
				if(isModuleAuth != null) {
					isAuth = isModuleAuth;
					isAuthName = true;
					if(isAuth) break;
				}/* else {
					// 모듈 세부 권한 설정 없는 경우 모듈설정 기본권한 체크
				}*/
			}
			
			// 요청권한에 해당하는 값이 null인 경우 return true 
			if(!isAuthName) isAuth = true;
		}
		
		return isAuth;
	}

	/**
	 * 체크권한 설정파일 없는 경우, 설정권한 없는 경우 return -2<br/>
	 * 권한있는 경우 return 1
	 * @param crtMenu			: 메뉴정보
	 * @param authManagerArray	: 권한관리항목정보
	 * @param moduleAuth		: 권한정보
	 * @param moduleAuthNames	: 권한id
	 * @return
	 */
	@Override
	public int getAuthType(JSONObject crtMenu, JSONArray authManagerArray, JSONObject moduleAuth, String[] moduleAuthNames) {
		int authType = 1;
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		// 0. 메뉴, 모듈 세부 권한 인증 여부 설정
		Object authObjs = request.getAttribute("MENU_AUTH");
		if(authObjs == null) request.setAttribute("MENU_AUTH", getMenuAuthenticated(crtMenu, authManagerArray, moduleAuth));
		if(moduleAuth == null) moduleAuth = JSONObjectUtil.getJSONObject(request.getAttribute("crtModuleAuth"));
		
		// 1. 메뉴 접근 권한 정보
		String menuAuthName = "MNU";
		authType = getAuthType(menuAuthName);
		if(authType == -2) {
			// 메뉴 권한 설정 없는 경우 메뉴설정 기본권한 체크
			authType = 1;
			//isAuth = true;
		} else if(authType <= 0) {
			/*boolean isNmAuth = isAuthenticated("NMMEMBER_" + menuAuthName);
			// 본인인증회원인 경우 : 본인인증 권한 페이지 여부 setting
			if(isNmAuth) authType = 2;*/
			return authType;		// 메뉴 접근 권한 없는 경우 return
		}

		// 2. 모듈 세부 권한 정보
		int moduleAuthType = 0;
		int nmAuthType = 0;		// 비회원 / 본인인증 권한 페이지 구분
		if(moduleAuthNames != null) {
			boolean isAuthName = false;	// 권한설정여부
			//Boolean isModuleAuth = null;
			String moduleAuthName = null;		// 모듈 세부권한명
			int moduleAuthLen = moduleAuthNames.length;
			for(int i = 0 ; i < moduleAuthLen ; i ++) {
				moduleAuthType = getAuthType(moduleAuthNames[i]);
				//System.out.println("---------moduleAuthType[" + moduleAuthNames[i] + "]:" + moduleAuthType);
				if(moduleAuthType != -2) isAuthName = true;
				
				moduleAuthName = moduleAuthNames[i];
				
				// 비회원 접근권한 페이지인 경우
				int moduleUtp = JSONObjectUtil.getInt(moduleAuth, moduleAuthName + "_UTP");//getModuleUtp(moduleAuthName);
				String[] moduleGrp = (moduleAuth!= null)?StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_GRP")), ","):null;
				String[] moduleDpt = (moduleAuth!= null)?StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_DPT")), ","):null;
				String[] moduleMbr = (moduleAuth!= null)?StringUtil.stringToArray(StringUtil.getString(moduleAuth.get(moduleAuthName + "_MBR")), ","):null;
				if(moduleAuthType != -1 && (moduleUtp == 0 && moduleGrp == null && moduleDpt == null && moduleMbr == null) && nmAuthType == 0 && (StringUtil.isEquals(moduleAuthName, "WRT") || 
						StringUtil.isEquals(moduleAuthName, "RWT") || 
						StringUtil.isEquals(moduleAuthName, "MWT"))) {
					boolean isNmAuth = isAuthenticated("NOMEMBER_" + moduleAuthName);
					if(isNmAuth) {
						moduleAuthType = 0;
						nmAuthType = 2;
						break;
					}
				}
				
				if(moduleAuthType == 1) break;	// 모듈 세부권한 있는 경우
				int nmMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
				if(moduleUtp == nmMbrType && moduleAuthType == 0 && nmAuthType == 0) {
					// 권한 없는 경우 && 본인인증 권한 페이지 여부
					nmAuthType = 3;
					//boolean isNmAuth = isAuthenticated("NMMEMBER_" + moduleAuthNames[i]);
					//if(isNmAuth) nmAuthType = 3;
				} else if(moduleAuthType == 0) {
					// 권한 없는 경우
					authType = 0;
				}
			}

			//System.out.println("---------authType:" + authType);
			// 요청권한에 해당하는 값이 null인 경우 return true 
			if(!isAuthName) authType = 1;								// 권한설정 없는 경우
			else if(moduleAuthType == -1) authType = moduleAuthType;	// 사용권한 아닌 경우
			else if(moduleAuthType <= 0 && nmAuthType > 0) {
				// 비회원, 본인인증회원인 경우 : 본인인증 권한 페이지 여부 setting
				authType = nmAuthType;
			}
		}
		//System.out.println("---------authType:" + authType);

		return authType;
	}

	/**
	 * 메뉴/모듈 세부  권한, 로그인 권한 비교
	 * @param isAuth		: return할 값
	 * @param pageUtp		: 메뉴/모듈접근가능 사용자유형
	 * @param pageGrps		: 메뉴/모듈접근가능 사용자그룹
	 * @param pageDpts		: 메뉴/모듈접근가능 사용자부서
	 * @param pageMbrs		: 메뉴/모듈접근가능 사용자일련번호
	 * @return
	 */
	@Override
	public boolean isAuth(boolean isAuth, int pageUtp, String pageGrps, String pageDpts, String pageMbrs) {
		//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		int mbrUtp = 0;
		String[] mbrGrp = null;
		String[] mbrDpt = null;
		String mbrIdx = null;
		//HttpSession session = request.getSession(true);
		Object loginVOObj = UserDetailsHelper.getAuthenticatedUser();//session.getAttribute("loginVO");
		LoginVO loginVO = null;
		if(loginVOObj != null) {
			loginVO = (LoginVO)loginVOObj;				// 로그인 정보
			mbrUtp = loginVO.getUsertypeIdx();			// 사용자유형
			mbrDpt = StringUtil.stringToArray(loginVO.getDepartIdxs(), ",");			// 부서
			mbrIdx = loginVO.getMemberIdx();			// 회원일련번호
		}
		
		// 회원 그룹
		List<Object> mbrGrpList = UserDetailsHelper.getAuthorities();
		if(mbrGrpList != null) mbrGrp = (String[])mbrGrpList.toArray(new String[mbrGrpList.size()]);

		String[] pageGrp = StringUtil.stringToArray(pageGrps, ",");
		String[] pageDpt = StringUtil.stringToArray(pageDpts, ",");
		String[] pageMbr = StringUtil.stringToArray(pageMbrs, ",");
		return isAuth(isAuth, pageUtp, pageGrp, pageDpt, pageMbr, mbrUtp, mbrGrp, mbrDpt, mbrIdx);
	}
	
	/**
	 * 메뉴/모듈 세부  권한, 로그인 권한 비교
	 * @param isAuth		: return할 값
	 * @param pageUtp		: 메뉴/모듈접근가능 사용자유형
	 * @param pageGrps		: 메뉴/모듈접근가능 사용자그룹
	 * @param pageDpts		: 메뉴/모듈접근가능 사용자부서
	 * @param pageMbrs		: 메뉴/모듈접근가능 사용자일련번호
	 * @param mbrUtp		: 로그인 회원 사용자유형
	 * @param mbrGrp		: 로그인 회원 사용자그룹
	 * @param mbrDpt		: 로그인 회원 사용자부서
	 * @param mbrIdx		: 로그인 회원 사용자일련번호
	 * @return
	 */
	public boolean isAuth(boolean isAuth, int pageUtp, String[] pageGrp, String[] pageDpt, String[] pageMbr, int mbrUtp, String[] mbrGrp, String[] mbrDpt, String mbrIdx) {
		
		// 1. 모듈 세부  사용자유형 권한, 로그인 사용자유형 권한 비교
		if(pageUtp > 0) {
			// 설정된 경우
			//if(moduleUsertypeIdx <= membUsertypeIdx) isAuth = true;
			// 모듈 세부  사용자유형 > 로그인 사용자유형 : 접근권한 없음
			if(pageUtp > mbrUtp) isAuth = false;
			else isAuth = true;
		}
		//System.out.println(pageUtp + ":" + pageGrp);
		
		if(pageUtp == 0 || !isAuth) {
			// 2. 모듈 세부  사용자부서 권한, 로그인 사용자부서 권한 비교
			if(pageDpt != null) {
				// 설정된 경우
				// 로그인 사용자부서가 속하는 모듈 세부  사용자부서 index
				int matchIdx = StringUtil.indexOf(pageDpt, mbrDpt);
				// 모듈 세부  사용자부서에 로그인 사용자부서이 속하지 않는 경우 : 접근권한 없음
				//System.out.println("-matchIdx:" + matchIdx);
				if(matchIdx == -1) isAuth = false;
				else isAuth = true;
			}
			
			// 3. 모듈 세부  사용자그룹 권한, 로그인 사용자그룹 권한 비교
			if(pageGrp != null) {
				// 설정된 경우
				// 로그인 사용자그룹이 속하는 모듈 세부  사용자그룹 index
				int matchIdx = StringUtil.indexOf(pageGrp, mbrGrp);
				// 모듈 세부  사용자그룹에 로그인 사용자그룹이 속하지 않는 경우 : 접근권한 없음
				//System.out.println("-matchIdx:" + matchIdx);
				if(matchIdx == -1) isAuth = false;
				else isAuth = true;
			}
			
			// 4. 모듈 세부  사용자회원 권한, 로그인 사용자회원 권한 비교
			if(pageMbr != null) {
				// 설정된 경우
				// 로그인 사용자회원가 속하는 모듈 세부  사용자회원 index
				int matchIdx = StringUtil.indexOf(pageMbr, mbrIdx);
				// 모듈 세부  사용자회원에 로그인 사용자회원이 속하지 않는 경우 : 접근권한 없음
				//System.out.println("-matchIdx:" + matchIdx);
				if(matchIdx == -1) isAuth = false;
				else isAuth = true;
			}
		}
		
		return isAuth;
	}

}
