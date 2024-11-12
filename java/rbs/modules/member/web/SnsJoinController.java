package rbs.modules.member.web;


import java.text.MessageFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rbs.egovframework.LoginVO;
import rbs.egovframework.SnsLoginVO;
import rbs.egovframework.social.kakao.connect.KakaoConnectionFactory;
import rbs.egovframework.social.naver.connect.NaverConnectionFactory;

import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

/**
 * SNS 정보 등록
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="member", useSDesign=true)
@RequestMapping("/{siteId}/member")
public class SnsJoinController extends MemberJoinComController{
	
	@Resource(name = "loginValidator")
	protected LoginValidator loginValidator;
	
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
	 * SNS 정보 등록위한 SNS 인증 code 얻기 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/facebookJoin.do")
	public String facebookJoin(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 로그인 여부
		if(!UserDetailsHelper.isLogin()) {
			return RbsProperties.getProperty("Globals.error.400.path");
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
	 * SNS 정보 등록위한 SNS 인증 code 얻기 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/kakaoJoin.do")
	public String kakaoJoin(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 로그인 여부
		if(!UserDetailsHelper.isLogin()) {
			return RbsProperties.getProperty("Globals.error.400.path");
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
	 * SNS 정보 등록위한 SNS 인증 code 얻기 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/naverJoin.do")
	public String naverJoin(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 로그인 여부
		if(!UserDetailsHelper.isLogin()) {
			return RbsProperties.getProperty("Globals.error.400.path");
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
	 * SNS 정보 등록위한 SNS 인증 code 얻기 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/googleJoin.do")
	public String googleJoin(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 로그인 여부
		if(!UserDetailsHelper.isLogin()) {
			return RbsProperties.getProperty("Globals.error.400.path");
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
		String redirectUri = googleOAuth2Parameters.getRedirectUri();
		String clientId = StringUtil.getString(siteInfo.get("google_aid"));
		//String clientSecret = "AoiZSPurDWkYKCTXB9IwTsku";
		String authorizationUrl  = new GoogleBrowserClientRequestUrl(clientId, redirectUri, scope).setResponseTypes(Arrays.asList("code")).build();
		response.sendRedirect(authorizationUrl);
		return null;
	}

	/**
	 * SNS 정보 등록 - sns로그인에서 이메일 중복된 회원 있는 경우
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 */
	/*@ModuleAuth(accessModule={"login"})
	@RequestMapping("/snsJoin.do")*/
	public String login_bk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		int useSnsRegi = RbsProperties.getPropertyInt("Globals.sns.login.register.use");	// sns등록 로그인에서 사용여부
		if(useSnsRegi != 1) {
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		
		// 1.1 필수체크 - sns정보를 세션에서 얻기
		HttpSession session = request.getSession(true);
		SnsLoginVO snsLoginVO = (SnsLoginVO)session.getAttribute("iSnsLoginVO");
		if(snsLoginVO == null || StringUtil.isEmpty(snsLoginVO.getSnsType()) || StringUtil.isEmpty(snsLoginVO.getSnsId())) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		JSONObject itemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "login_item_info");
		model.addAttribute("itemInfo", itemInfo);

		// 기본경로
		fn_setCommonPath(attrVO);
		return getViewPath("/snsJoin");
	}
	
	/**
	 * SNS 연결 페이지 - sns로그인에서 일치하는 회원이 없는 경우
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 */
	@ModuleAuth(accessModule={"login"})
	@RequestMapping("/snsJoin.do")
	public String login(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		
		// 1. 필수체크 - sns정보를 세션에서 얻기
		HttpSession session = request.getSession(true);
		SnsLoginVO snsLoginVO = (SnsLoginVO)session.getAttribute("iSnsLoginVO");
		if(snsLoginVO == null || StringUtil.isEmpty(snsLoginVO.getSnsType()) || StringUtil.isEmpty(snsLoginVO.getSnsId())) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		JSONObject itemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "login_item_info");
		model.addAttribute("itemInfo", itemInfo);

		// 기본경로
		fn_setCommonPath(attrVO);
		return getViewPath("/snsJoin");
	}

	/**
	 * SNS 정보 등록 - sns로그인에서 이메일 중복된 회원 있는 경우
	 * @param parameterMap
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(accessModule={"login"})
	@RequestMapping(value="/snsJoinProc.do", method=RequestMethod.POST, params={"mbrId", "mbrPwd"})
	public String snsJoinProc(@RequestParam String mbrId, @RequestParam String mbrPwd, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		int useSnsRegi = RbsProperties.getPropertyInt("Globals.sns.login.register.use");	// sns등록 로그인에서 사용여부
		if(useSnsRegi != 1) {
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message", MessageUtil.getAlert( null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		// 1.1 필수체크 - sns정보를 세션에서 얻기
		HttpSession session = request.getSession(true);
		SnsLoginVO snsLoginVO = (SnsLoginVO)session.getAttribute("iSnsLoginVO");
		if(snsLoginVO == null || StringUtil.isEmpty(snsLoginVO.getSnsType()) || StringUtil.isEmpty(snsLoginVO.getSnsId())) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		// 1.2 필수체크  - id,pwd 확인
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(items, "mbrId");
		String encId = ModuleUtil.getParamToDBValue(mbrIdItem, mbrId);
		
		JSONObject mbrPwdItem = JSONObjectUtil.getJSONObject(items, "mbrPwd");
		String encPwd = ModuleUtil.getParamPWToDBValue(mbrPwdItem, mbrPwd, mbrId);
		
		searchList.add(new DTForm("MEMBER_ID", encId));
		searchList.add(new DTForm("MEMBER_PWD", encPwd));
		param.put("searchList", searchList);
		
    	// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
		DataMap memberDt = memberService.getView(param);

		if(memberDt == null || !StringUtil.isEquals(memberDt.get("MEMBER_ID"), encId)) {
			// 로그인 실패
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.member")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		String memberIdx = StringUtil.getString(memberDt.get("MEMBER_IDX"));

		String snsType = snsLoginVO.getSnsType();
		String snsId = snsLoginVO.getSnsId();
		
		// 2. SNS ID 중복확인
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.MEMBER_IDX", memberIdx, "<>"));
		searchList.add(new DTForm("A.SNS_TYPE", snsType));
		searchList.add(new DTForm("A.SNS_ID", snsId));
		param.put("searchList", searchList);
		
		if(memberService.getSnsDuplicateCount(param) > 0) {
			model.addAttribute("message", MessageUtil.getAlert(MessageFormat.format(rbsMessageSource.getMessage("message.exist.duplicate"), new String[]{"SNS ID"})));
			return RbsProperties.getProperty("Globals.fail.path");
		}

		// 3. sns정보 update
		parameterMap.put("snsType", snsType);
		parameterMap.put("snsId", snsId);
		parameterMap.put("memberIdx", memberIdx);
		parameterMap.put("memberId", encId);
		parameterMap.put("memberName", StringUtil.getString(memberDt.get("MEMBER_NAME")));
		int result = memberService.snsInsert(memberIdx, request.getRemoteAddr(), parameterMap);
		if(result > 0) {
    		// 저장 성공
			session.setAttribute("iSnsLoginVO", null);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(false, rbsMessageSource.getMessage("message.insert"), "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.message.path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail.path");
	}
	
	/**
	 * SNS 정보 등록 - 로그인 후 내정보수정에서
	 * @param parameterMap
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/snsJoinProc.do", method=RequestMethod.POST)
	public String snsJoinProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		int useSnsRegi = RbsProperties.getPropertyInt("Globals.sns.login.register.use");	// sns등록 로그인에서 사용여부
		if(useSnsRegi == 1) {
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		
		// 로그인 여부
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		if(loginVO == null) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int useRSA = JSONObjectUtil.getInt(settingInfo, "use_rsa");	// 설정정보의 rsa사용여부
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		
		// 1. 필수입력 체크
		// 1.1 비밀번호 체크
		if(!memberService.isMbrPwdMatched(useRSA, parameterMap, items)){
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.password.mismatched")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		// 1.2 sns정보를 세션에서 얻기
		HttpSession session = request.getSession(true);
		SnsLoginVO snsLoginVO = (SnsLoginVO)session.getAttribute("snsLoginVO");
		session.removeAttribute("snsLoginVO");
		if(snsLoginVO == null || StringUtil.isEmpty(snsLoginVO.getSnsType()) || StringUtil.isEmpty(snsLoginVO.getSnsId())) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}

		String snsType = snsLoginVO.getSnsType();
		String snsId = snsLoginVO.getSnsId();
		
		// 2. SNS ID 중복확인
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.MEMBER_IDX", loginVO.getMemberIdx(), "<>"));
		searchList.add(new DTForm("A.SNS_TYPE", snsType));
		searchList.add(new DTForm("A.SNS_ID", snsId));
		param.put("searchList", searchList);
		
		if(memberService.getSnsDuplicateCount(param) > 0) {
			model.addAttribute("message", MessageUtil.getAlert(MessageFormat.format(rbsMessageSource.getMessage("message.exist.duplicate"), new String[]{"SNS ID"})));
			return RbsProperties.getProperty("Globals.fail.path");
		}

		// 3. sns정보 update
		parameterMap.put("snsType", snsType);
		parameterMap.put("snsId", snsId);
		int result = memberService.snsInsert(loginVO.getMemberIdx(), request.getRemoteAddr(), parameterMap);
		if(result > 0) {
    		// 저장 성공
        	// 기본경로
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(false, rbsMessageSource.getMessage("message.insert"), "fn_procAction();"));
			return RbsProperties.getProperty("Globals.message.path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail.path");
	}
	/**
	 * 내정보수정 경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		DataForm queryString = attrVO.getQueryString();
		
		String inputName = "snsJoin.do";
		String inputProcName = "snsJoinProc.do";
		
		PathUtil.fn_setInputPath(queryString, baseParams, inputName, inputProcName);
		
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		request.setAttribute("URL_SNSJOIN", "snsJoin.do" + allQueryString);
		request.setAttribute("URL_SNSJOINPROC", "snsJoinProc.do" + allQueryString);
	}
}
