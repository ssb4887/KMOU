package rbs.modules.member.web;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.CookieUtil;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import net.sf.json.JSONObject;
import rbs.egovframework.Defines;
import rbs.egovframework.LoginVO;
import rbs.egovframework.SukangLoginVO;
import rbs.egovframework.com.utl.slm.RbsHttpSessionBindingListener;
import rbs.modules.basket.service.BasketService;
import rbs.modules.member.service.MemberLogService;

@Controller
@ModuleMapping(moduleId="login", confModule="member", useSDesign=true)
public class LoginController extends LoginComController{
	
	@Resource(name = "memberLogService")
	protected MemberLogService memberLogService;
	
	@Resource(name = "basketService")
	private BasketService basketService;
	
	
	
	
	@Resource(name = "loginValidator")
	protected LoginValidator loginValidator;
	
	@RequestMapping("/{siteId}/login/login.do")
	public String login(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		JSONObject itemInfo = getItemInfo(attrVO.getModuleItem());
		model.addAttribute("itemInfo", itemInfo);

		JSONObject settingInfo = getSettingInfo(attrVO.getModuleSetting());
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		// 기본경로
		fn_setCommonPath(attrVO, useSsl);
		return getViewPath("/login");
	}

	@RequestMapping(value="/{siteId}/login/loginPreProc.do", method=RequestMethod.POST)
	public String loginPreProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		boolean isAdmMode = attrVO.isAdmMode();
		String siteMode = attrVO.getSiteMode();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = getItemInfo(attrVO.getModuleItem());
		JSONObject settingInfo = getSettingInfo(attrVO.getModuleSetting());
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		// 기본경로
		fn_setCommonPath(attrVO, useSsl);
		
	    String memberId = parameterMap.getString("mbrId");

	    // 실패 횟수 체크
	    Cookie[] cookies = request.getCookies();
	    int failCount = 0;
	    for (Cookie cookie : cookies) {
	        if (cookie.getName().equals("failCount_" + memberId)) {
	            failCount = Integer.parseInt(cookie.getValue());
	            break;
	        }
	    }

	    // 로그인 시도 제한 체크
	    if (failCount >= 5) {
	        model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.adm.logIn.fail.count")));
	        return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	    }
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message", MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		// 아이디, 비밀번호 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, loginValidator, itemInfo);
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		// 1. 로그인 처리
		LoginVO resultVO = mainService.setUser(parameterMap, request);
		
		if (resultVO != null) {
			

				HttpSession session = request.getSession();
				
				// 2-1. 로그인 정보를 세션에 저장
				session.invalidate();
				session = request.getSession(true);
				session.setAttribute("preLoginVO", resultVO);
				//수강신청 시스템 로그인(세션에 저장)
				basketService.sukangLogin(parameterMap, request);
				
				// 중복 로그인 방지
				RbsHttpSessionBindingListener listener = RbsHttpSessionBindingListener.INSTANCE;
				if(listener.findByLoginId(resultVO.getMemberId()))
				{
					model.addAttribute("message", MessageUtil.getConfirm(isAjax, rbsMessageSource.getMessage("message.duplicate.login"), "fn_procAction('" + request.getAttribute("URL_LOGINPROC") + "');", "fn_procAction('" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_LOGIN"))) + "');"));
					return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
				}
				
		        // 로그인 성공 시 실패 카운트 초기화
		        Cookie cookie = new Cookie("failCount_" + memberId, "0");
		        cookie.setMaxAge(0); // 쿠키 삭제
		        response.addCookie(cookie);
	
				model.addAttribute("message", MessageUtil.getAlert(isAjax, null, "fn_procAction('" + request.getAttribute("URL_LOGINPROC") + "');"));
				return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		}

		// 로그인 실패 - ID / PW 모두 일치하지 않는 경우
		if (resultVO == null) {
			// 로그 저장 - 로그인 시도
			Logger logger = LoggerFactory.getLogger("usrLogin");
	
			JSONObject moduleItem = JSONObjectUtil.getJSONObject(request.getAttribute("moduleItem"));
			JSONObject memberItemInfo = JSONObjectUtil.getJSONObject(moduleItem, "item_info");
			JSONObject memberItems = JSONObjectUtil.getJSONObject(memberItemInfo, "items");
	
			int useRSA = JSONObjectUtil.getInt(settingInfo, "use_rsa");
			JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "items"), "mbrId");
			String encId = ModuleUtil.getParamToDBValue(useRSA, mbrIdItem, parameterMap.getString("mbrId"));
			DataMap dt = new DataMap();
			dt.put("MEMBER_ID", encId);
			memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.login.access"), dt, null, memberItems, "OTHER", "OTHER", encId, null);
			
	        // 실패 횟수 증가
	        failCount++;
	        Cookie failCookie = new Cookie("failCount_" + memberId, String.valueOf(failCount));
	        failCookie.setMaxAge(60 * 60); // 1시간 동안 로그인막기
	        response.addCookie(failCookie);
		}
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.member")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}	
	

	@RequestMapping(value="/{siteId}/login/loginProc.do")
	public String loginProc(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = getSettingInfo(attrVO.getModuleSetting());
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		HttpSession session = request.getSession();
		LoginVO resultVO = (LoginVO)session.getAttribute("preLoginVO");
		// 1. 로그인 처리
		if (resultVO != null) {
			
			loginService.loginProc(resultVO);

			// 로그 저장
			Logger logger = null;
			int admMbrType = RbsProperties.getPropertyInt("Globals.code.USERTYPE_ADMIN");
    		if(resultVO.getUsertypeIdx() >= admMbrType) logger = LoggerFactory.getLogger("admLogin");
    		else logger = LoggerFactory.getLogger("usrLogin");

    		JSONObject moduleItem = JSONObjectUtil.getJSONObject(request.getAttribute("moduleItem"));
    		JSONObject memberItemInfo = JSONObjectUtil.getJSONObject(moduleItem, "item_info");
    		JSONObject memberItems = JSONObjectUtil.getJSONObject(memberItemInfo, "items");
    		
    		memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.login"), null, null, memberItems, resultVO.getMemberIdx(), resultVO.getMemberIdx(), resultVO.getMemberId(), resultVO.getMemberName());
    		
    		loginService.loginUpdate(resultVO.getMemberIdx(), request.getRemoteAddr());
    		
    		// 비밀번호 수정일자 확인
    		String url = null;
    		double pwdModiIntv = resultVO.getPwdModiIntv();
    		double pwdModiIntv2 = resultVO.getPwdModiIntv2();
    		boolean isNextChangePwd = StringUtil.isEquals(resultVO.getPwdModiType(), "1");
    		if(!attrVO.isAdmMode() && ((pwdModiIntv > 0 && (!isNextChangePwd || (isNextChangePwd && pwdModiIntv2 > 0))) || StringUtil.isEquals(resultVO.getPwdModiType(), "2"))){
        		// 기본경로
        		fn_setCommonPath(attrVO, useSsl);
    			url = PathUtil.getAddProtocolToFullPath(StringUtil.getString(request.getAttribute("URL_PWDMODI")));
    		} else url = PathUtil.getAddProtocolToFullPath();
    		
			model.addAttribute("message", MessageUtil.getAlert(isAjax, null, "fn_procAction('" + url + "', '1');"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		}

		
		// 기본경로
		fn_setCommonPath(attrVO, useSsl);
		
		// 로그인 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.member"), "fn_procAction('" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_LOGIN"))) + "', '1');"));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	@RequestMapping("/{siteId}/login/nmauth.do")
	public String nmauth(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();

		boolean isAjax = attrVO.isAjax();
		
		String ajaxPName = attrVO.getAjaxPName();
		
		int loginUsertypeIdx = 0;
		if(loginVO != null) loginUsertypeIdx = loginVO.getUsertypeIdx();
		if(UserDetailsHelper.isLogin() || loginUsertypeIdx == RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER")){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		JSONObject itemInfo = getItemInfo(attrVO.getModuleItem());
		model.addAttribute("itemInfo", itemInfo);

		JSONObject settingInfo = getSettingInfo(attrVO.getModuleSetting());
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		// 기본경로
		fn_setCommonPath(attrVO, useSsl);
		return getViewPath("/nmauth");
	}
	
	/**
	 * 로그아웃
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{siteId}/logout/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		loginService.logout();
		// 자동로그아웃 시간 삭제
		CookieUtil.deleteCookie(request, response, "rbis_auto_logout");
		/*HttpSession session = request.getSession();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		if(loginVO != null) session.setAttribute(loginVO.getMemberId(), null);
		session.setAttribute("loginVO", null);
		session.setAttribute("selSiteVO", null);*/

		// dashboard 기본으로 설정된 접근권한 체크 : 현재 dashboard 접근권한 설정 안하기때문
		JSONObject indexInfo = MenuUtil.getMenuInfo(Defines.CODE_MENU_INDEX);
		int usertypeIdx = JSONObjectUtil.getInt(indexInfo, "usertype_idx");
		if(usertypeIdx > 0) {
			try {
				response.sendRedirect(MenuUtil.getMenuUrl(true, 3, null));
			} catch(Exception e) {}
		} else {
			try {
				response.sendRedirect(MenuUtil.getMenuUrl(true, 1, null));
			} catch(Exception e) {}
		}
		
		return null;
	}
}
