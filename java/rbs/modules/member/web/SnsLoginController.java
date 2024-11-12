package rbs.modules.member.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import rbs.egovframework.Defines;
import rbs.egovframework.LoginVO;
import rbs.egovframework.SnsLoginVO;
import rbs.egovframework.com.utl.slm.RbsHttpSessionBindingListener;
import rbs.egovframework.social.kakao.connect.KakaoConnectionFactory;
import rbs.egovframework.social.naver.connect.NaverConnectionFactory;
import rbs.modules.member.service.MemberService;

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

@Controller
@RequestMapping(value = "/{siteId}/login")
@ModuleMapping(moduleId="login", confModule="member", useSDesign=true)
public class SnsLoginController extends LoginComController{
	
	@Resource(name = "memberService")
	protected MemberService memberService;
	
	// 페이스북 oAuth 관련
	//@Resource(name = "facebookConnectionFactory")
	protected FacebookConnectionFactory facebookConnectionFactory;
	@Resource(name = "facebookOAuth2Parameters")
	protected OAuth2Parameters facebookOAuth2Parameters;

	// kakao oAuth 관련
	//@Resource(name = "kakaoConnectionFactory")
    private KakaoConnectionFactory kakaoConnectionFactory;
	@Resource(name = "kakaoOAuth2Parameters")
    private OAuth2Parameters kakaoOAuth2Parameters;

	// naver oAuth 관련
	//@Resource(name = "naverConnectionFactory")
    private NaverConnectionFactory naverConnectionFactory;
	@Resource(name = "naverOAuth2Parameters")
    private OAuth2Parameters naverOAuth2Parameters;

	// google oAuth 관련
	//@Resource(name = "googleConnectionFactory")
    //private GoogleConnectionFactory googleConnectionFactory;
	@Resource(name = "googleOAuth2Parameters")
    private OAuth2Parameters googleOAuth2Parameters;

	/**
	 * facebook code 얻기위한 경로
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/facebookLogin.do")
	public String facebookLogin(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message", MessageUtil.getAlert(null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		int useSnsLogin = StringUtil.getInt(siteInfo.get("facebook_login"));
		
		if(useSnsLogin != 1) {
			// 해당 sns 로그인 사용하지 않는 경우
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		
		String clientId = StringUtil.getString(siteInfo.get("facebook_aid"));
		String  clientSecret = StringUtil.getString(siteInfo.get("facebook_asecret"));

		HttpSession session = request.getSession(true);
		session.setAttribute("snsCrtSiteId", request.getAttribute("crtSiteId"));
		
		if(facebookConnectionFactory == null) facebookConnectionFactory = new FacebookConnectionFactory(clientId, clientSecret);
		OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
		String redirectUrl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, facebookOAuth2Parameters);
		response.sendRedirect(redirectUrl);
		return null;
	}

	/**
	 * kakao code 얻기위한 경로
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/kakaoLogin.do")
	public String kakaoLogin(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message", MessageUtil.getAlert(null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		int useSnsLogin = StringUtil.getInt(siteInfo.get("kakao_login"));
		
		if(useSnsLogin != 1) {
			// 해당 sns 로그인 사용하지 않는 경우
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		
		String clientId = StringUtil.getString(siteInfo.get("kakao_aid"));
		String  clientSecret = StringUtil.getString(siteInfo.get("kakao_asecret"));

		HttpSession session = request.getSession(true);
		session.setAttribute("snsCrtSiteId", request.getAttribute("crtSiteId"));
		
		if(kakaoConnectionFactory == null) kakaoConnectionFactory = new KakaoConnectionFactory(clientId, clientSecret);
		OAuth2Operations oauthOperations = kakaoConnectionFactory.getOAuthOperations();
		String redirectUrl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, kakaoOAuth2Parameters);
		response.sendRedirect(redirectUrl);
		return null;
	}

	/**
	 * naver code 얻기위한 경로
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/naverLogin.do")
	public String naverLogin(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message", MessageUtil.getAlert(null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		int useSnsLogin = StringUtil.getInt(siteInfo.get("naver_login"));
		
		if(useSnsLogin != 1) {
			// 해당 sns 로그인 사용하지 않는 경우
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		
		String clientId = StringUtil.getString(siteInfo.get("naver_aid"));
		String  clientSecret = StringUtil.getString(siteInfo.get("naver_asecret"));

		HttpSession session = request.getSession(true);
		session.setAttribute("snsCrtSiteId", request.getAttribute("crtSiteId"));
		
		if(naverConnectionFactory == null) naverConnectionFactory = new NaverConnectionFactory(clientId, clientSecret);
		OAuth2Operations oauthOperations = naverConnectionFactory.getOAuthOperations();
		String redirectUrl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, naverOAuth2Parameters);
		response.sendRedirect(redirectUrl);
		return null;
	}

	/**
	 * google code 얻기위한 경로
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/googleLogin.do")
	public String googleLogin(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message", MessageUtil.getAlert(null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		int useSnsLogin = StringUtil.getInt(siteInfo.get("google_login"));
		
		if(useSnsLogin != 1) {
			// 해당 sns 로그인 사용하지 않는 경우
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		
		List<String> scope = new ArrayList<String>();
		scope.add("profile");
		scope.add("https://www.googleapis.com/auth/userinfo.email");

		HttpSession session = request.getSession(true);
		session.setAttribute("snsCrtSiteId", request.getAttribute("crtSiteId"));
		String redirectUri = googleOAuth2Parameters.getRedirectUri();//request.getScheme() + "://" + request.getServerName() + ((request.getServerPort() != 80 && request.getServerPort() != 443)?":" + request.getServerPort():"") + "/" + request.getAttribute("crtSiteId") + "/sns/google/login.do";//googleOAuth2Parameters.getRedirectUri();
		String clientId = StringUtil.getString(siteInfo.get("google_aid"));//"85969008733-3m8odjcstgc414b4lo2qojbhnk7gqrik.apps.googleusercontent.com";
		//String clientSecret = "AoiZSPurDWkYKCTXB9IwTsku";
		String authorizationUrl  = new GoogleBrowserClientRequestUrl(clientId, redirectUri, scope).setResponseTypes(Arrays.asList("code")).build();
		response.sendRedirect(authorizationUrl);
		return null;
	}

	@RequestMapping(value="/snsLoginProc.do", method=RequestMethod.POST)
	public String loginPreProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = getSettingInfo(attrVO.getModuleSetting());
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		// 기본경로
		fn_setCommonPath(attrVO, useSsl);
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message", MessageUtil.getAlert(null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		// 필수입력 체크
		// 1. sns 정보를 세션에서 얻기
		HttpSession session = request.getSession(true);
		SnsLoginVO snsLoginVO = (SnsLoginVO)session.getAttribute("snsLoginVO");
		session.removeAttribute("snsLoginVO");
		if(snsLoginVO == null || StringUtil.isEmpty(snsLoginVO.getSnsType()) || StringUtil.isEmpty(snsLoginVO.getSnsId())) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		// 2. 회원정보에 있는지 확인 : 있는 경우 로그인처리, 없는 경우 회원가입처리 - email, name과 일치하는 회원
		String snsType = snsLoginVO.getSnsType();
		String snsId = snsLoginVO.getSnsId();
		parameterMap.put("snsType", snsType);
		parameterMap.put("snsId", snsId);
		LoginVO resultVO = loginService.snsLogin(request.getRemoteAddr(), parameterMap, attrVO.getItemInfo());
		
		if(resultVO != null) {
			// 2.1 일치하는 회원정보 있는 경우 - sns계정과 일치하는 경우 로그인 처리, 아닌경우 오류
			if(StringUtil.isEquals(snsType, resultVO.getSnsType()) && StringUtil.isEquals(snsId, resultVO.getSnsId())) {
				// 2.1.1 sns계정과 일치하는 경우 - 로그인 처리
				
				//  로그인 정보를 세션에 저장
				session.invalidate();
				session = request.getSession(true);
				session.setAttribute("preLoginVO", resultVO);
				
				// 중복 로그인 방지
				RbsHttpSessionBindingListener listener = RbsHttpSessionBindingListener.INSTANCE;
				if(listener.findByLoginId(resultVO.getMemberId())) {
					// 중복로그인인 경우 - 확인 메시지 띄움
					model.addAttribute("message", MessageUtil.getConfirm(rbsMessageSource.getMessage("message.duplicate.login"), "fn_procAction('" + request.getAttribute("URL_LOGINPROC") + "');", "fn_procAction('" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_LOGIN"))) + "');"));
					return RbsProperties.getProperty("Globals.fail.path");
				}

				model.addAttribute("message", MessageUtil.getAlert(null, "fn_procAction('" + request.getAttribute("URL_LOGINPROC") + "');"));
				return RbsProperties.getProperty("Globals.message.path");
			} else {
				// 2.1.2 sns계정과 일치하지 않는 경우 - 오류처리
				return RbsProperties.getProperty("Globals.error.400.path");
			}
		}
		
		// 2.2 일치하는 회원정보 없는 경우 - 아이디 연결 페이지로 이동
		session.setAttribute("iSnsLoginVO", snsLoginVO);
		model.addAttribute("message", MessageUtil.getConfirm(rbsMessageSource.getMessage("message.sns.no.member.confirm"), "fn_procAction('" + request.getAttribute("URL_SNSJOIN") + "');"));
		return RbsProperties.getProperty("Globals.message.path");
		
		/*
		// 2.2 일치하는 회원정보 없는 경우 - 이메일 중복 확인
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		JSONObject memberItems = JSONObjectUtil.getJSONObject(memberItemInfo, "items");
		JSONObject mbrEmailItem = JSONObjectUtil.getJSONObject(memberItems, "mbrEmail");			// mbrId 항목 설정 정보
		String dbMbrEmail = ModuleUtil.getPrivEncValue(mbrEmailItem, snsLoginVO.getSnsEmail());		// 암호화 설정에 따른 값 얻기

		searchList.add(new DTForm("A.MEMBER_EMAIL", dbMbrEmail));
		param.put("searchList", searchList);

		if(memberService.getDuplicateCount(param) > 0){
			// 2.2.1 이미 등록된 email인 경우 - email 중복 및 내정보수정에서 sns계정 등록 유도 메시지 처리

			int useSnsRegi = RbsProperties.getPropertyInt("Globals.sns.login.register.use");	// sns등록 로그인에서 사용여부
			if(useSnsRegi == 1) {
				// sns등록 로그인에서 사용하는 경우 - sns등록 페이지로 이동
				session.setAttribute("iSnsLoginVO", snsLoginVO);
				model.addAttribute("message", MessageUtil.getConfirm(rbsMessageSource.getMessage("message.sns.login.member.duplicate.email"), "fn_procAction('" + request.getAttribute("URL_SNSJOIN") + "');"));
				return RbsProperties.getProperty("Globals.message.path");
			} else {
				// sns등록 로그인에서 사용하지 않는 경우 - 내정보수정에서 sns계정 등록 유도 메시지 처리
				model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.sns.member.duplicate.email")));
				return RbsProperties.getProperty("Globals.fail.path");
			}
		}
		
		// 2.2.2 일치하는 회원정보 없는 경우 && 이미 등록된 email 아닌 경우 - 회원가입페이지로 이동
		session.setAttribute("iSnsLoginVO", snsLoginVO);
		model.addAttribute("message", MessageUtil.getConfirm(rbsMessageSource.getMessage("message.sns.no.member.confirm"), "fn_procAction('" + MenuUtil.getMenuUrl(Defines.CODE_MENU_MEMBER) + "');"));
		return RbsProperties.getProperty("Globals.fail.path");*/
	}
}
