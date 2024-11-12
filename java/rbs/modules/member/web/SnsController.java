package rbs.modules.member.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import rbs.egovframework.SnsLoginVO;
import rbs.egovframework.social.kakao.api.Kakao;
import rbs.egovframework.social.kakao.api.impl.KakaoTemplate;
import rbs.egovframework.social.kakao.connect.KakaoConnectionFactory;
import rbs.egovframework.social.naver.api.Naver;
import rbs.egovframework.social.naver.api.impl.NaverTemplate;
import rbs.egovframework.social.naver.connect.NaverConnectionFactory;
import rbs.modules.member.service.MemberService;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.EmailAddress;
import com.google.api.services.people.v1.model.Name;
import com.google.api.services.people.v1.model.Person;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * SNS
 * @author user
 *
 */
@Controller
public class SnsController {
	
	@Resource(name = "memberService")
	private MemberService memberService;
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	private EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	private RbsMessageSource rbsMessageSource;

	// 페이스북 oAuth 관련
	//@Resource(name = "facebookConnectionFactory")
    private FacebookConnectionFactory facebookConnectionFactory;
	@Resource(name = "facebookOAuth2Parameters")
    private OAuth2Parameters facebookOAuth2Parameters;

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
	 * facebook 로그인
	 * @param code
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sns/facebook/login.do", method={RequestMethod.GET, RequestMethod.POST})
	public String facebookLogin(@RequestParam String code, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		HttpSession session = request.getSession(true);
		String siteId = StringUtil.getString(session.getAttribute("snsCrtSiteId"));
		session.setAttribute("snsCrtSiteId", null);
	    JSONObject siteInfo = getSiteInfo(siteId);					// 사이트 정보
	    if(JSONObjectUtil.isEmpty(siteInfo)) {
	    	// 메뉴설정파일 없는 경우
			return RbsProperties.getProperty("Globals.error.400.path");
	    }

	    String snsTypeId = "facebook";
		String redirectUri = facebookOAuth2Parameters.getRedirectUri();
		String clientId = StringUtil.getString(siteInfo.get(snsTypeId + "_aid"));
		String  clientSecret = StringUtil.getString(siteInfo.get(snsTypeId + "_asecret"));

		if(facebookConnectionFactory == null) facebookConnectionFactory = new FacebookConnectionFactory(clientId, clientSecret);
		OAuth2Operations oauthOperations = facebookConnectionFactory.getOAuthOperations();
		AccessGrant accessGrant = null;
		try {
			accessGrant = oauthOperations.exchangeForAccess(code, redirectUri, null);
		} catch(HttpClientErrorException e){
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		String accessToken = accessGrant.getAccessToken();
		Long expireTime = accessGrant.getExpireTime();
		long currentTime = System.currentTimeMillis();
		
		if(expireTime != null && expireTime < currentTime) {
			accessToken = accessGrant.getRefreshToken();
		}
		
		Facebook facebook = new FacebookTemplate(accessToken);
		try{
			// 1. 페이스북 정보 얻기
			String[] fields = {"id", "email", "name", "gender", "birthday"};
			User userProfile = facebook.fetchObject("me", User.class, fields);

			//  로그인 : sns 정보를 세션에 저장
			// 회원 설정정보
			JSONObject settingInfo = JSONObjectUtil.getJSONObject(ModuleUtil.getModuleSettingObject("member", 1), "setting_info");
			JSONObject snsItems = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(settingInfo, "sns_info"), "items");
			JSONObject snsItem = JSONObjectUtil.getJSONObject(snsItems, snsTypeId);
			String snsType = JSONObjectUtil.getString(snsItem, "sns_type");
			
			SnsLoginVO snsLoginVO = new SnsLoginVO();
			snsLoginVO.setSnsType(snsType);
			snsLoginVO.setSnsTypeId(snsTypeId);
			snsLoginVO.setSnsTypeName(JSONObjectUtil.getString(snsItem, "sns_name"));
			snsLoginVO.setSnsId(userProfile.getId());
			snsLoginVO.setSnsName(userProfile.getName());
			snsLoginVO.setSnsEmail(userProfile.getEmail());
			session.invalidate();
			session = request.getSession(true);
			session.setAttribute("snsLoginVO", snsLoginVO);

			/*
			System.out.println("- email :" + userProfile.getEmail());
			System.out.println("- id :" + userProfile.getId());
			System.out.println("- name :" + userProfile.getName());
			System.out.println("- gender :" + userProfile.getGender());
			System.out.println("- birthday :" + userProfile.getBirthday());
			*/
		}catch(MissingAuthorizationException e){
			e.printStackTrace();
		}
		
		model.addAttribute("message", MessageUtil.getAlert(null, "opener.fn_snsLoginFormSubmit(); self.close();"));
		return RbsProperties.getProperty("Globals.message.path");
	}
	
	/**
	 * kakao 로그인
	 * @param code
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sns/kakao/login.do", method={RequestMethod.GET, RequestMethod.POST})
	public String kakaoLogin(@RequestParam String code, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		HttpSession session = request.getSession(true);
		String siteId = StringUtil.getString(session.getAttribute("snsCrtSiteId"));
		session.setAttribute("snsCrtSiteId", null);
	    JSONObject siteInfo = getSiteInfo(siteId);					// 사이트 정보
	    if(JSONObjectUtil.isEmpty(siteInfo)) {
	    	// 메뉴설정파일 없는 경우
			return RbsProperties.getProperty("Globals.error.400.path");
	    }

	    String snsTypeId = "kakao";
		String redirectUri = kakaoOAuth2Parameters.getRedirectUri();
		String clientId = StringUtil.getString(siteInfo.get(snsTypeId + "_aid"));
		String  clientSecret = StringUtil.getString(siteInfo.get(snsTypeId + "_asecret"));

		if(kakaoConnectionFactory == null) kakaoConnectionFactory = new KakaoConnectionFactory(clientId, clientSecret);
		OAuth2Operations oauthOperations = kakaoConnectionFactory.getOAuthOperations();
		AccessGrant accessGrant = null;
		try {
			accessGrant = oauthOperations.exchangeForAccess(code, redirectUri, null);
		} catch(HttpClientErrorException e){
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		String accessToken = accessGrant.getAccessToken();
		Long expireTime = accessGrant.getExpireTime();
		long currentTime = System.currentTimeMillis();
		
		if(expireTime != null && expireTime < currentTime) {
			accessToken = accessGrant.getRefreshToken();
		}
		
		//Connection<Kakao> connection = kakaoConnectionFactory.createConnection(accessGrant);
		//Kakao kakao = (connection == null)?new KakaoTemplate(accessToken):connection.getApi();
		Kakao kakao = new KakaoTemplate(accessToken);
		try{
			// 1. 정보 얻기
			rbs.egovframework.social.kakao.api.User userProfile = kakao.fetchObject("user/me", rbs.egovframework.social.kakao.api.User.class);

			//  로그인 : sns 정보를 세션에 저장
			// 회원 설정정보
			JSONObject settingInfo = JSONObjectUtil.getJSONObject(ModuleUtil.getModuleSettingObject("member", 1), "setting_info");
			JSONObject snsItems = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(settingInfo, "sns_info"), "items");
			JSONObject snsItem = JSONObjectUtil.getJSONObject(snsItems, snsTypeId);
			String snsType = JSONObjectUtil.getString(snsItem, "sns_type");
			
			SnsLoginVO snsLoginVO = new SnsLoginVO();
			snsLoginVO.setSnsType(snsType);
			snsLoginVO.setSnsTypeId(snsTypeId);
			snsLoginVO.setSnsTypeName(JSONObjectUtil.getString(snsItem, "sns_name"));
			snsLoginVO.setSnsId(userProfile.getId());
			snsLoginVO.setSnsName(userProfile.getName());
			snsLoginVO.setSnsEmail(userProfile.getEmail());
			session.invalidate();
			session = request.getSession(true);
			session.setAttribute("snsLoginVO", snsLoginVO);

			/*
			System.out.println("- email :" + userProfile.getEmail());
			System.out.println("- id :" + userProfile.getId());
			System.out.println("- name :" + userProfile.getName());
			System.out.println("- gender :" + userProfile.getGender());
			System.out.println("- birthday :" + userProfile.getBirthday());
			*/
		}catch(MissingAuthorizationException e){
			e.printStackTrace();
		}
		
		model.addAttribute("message", MessageUtil.getAlert(null, "opener.fn_snsLoginFormSubmit(); self.close();"));
		return RbsProperties.getProperty("Globals.message.path");
	}
	
	/**
	 * naver 로그인
	 * @param code
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sns/naver/login.do", method={RequestMethod.GET, RequestMethod.POST})
	public String naverLogin(@RequestParam String code, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		HttpSession session = request.getSession(true);
		String siteId = StringUtil.getString(session.getAttribute("snsCrtSiteId"));
		session.setAttribute("snsCrtSiteId", null);
	    JSONObject siteInfo = getSiteInfo(siteId);					// 사이트 정보
	    if(JSONObjectUtil.isEmpty(siteInfo)) {
	    	// 메뉴설정파일 없는 경우
			return RbsProperties.getProperty("Globals.error.400.path");
	    }

	    String snsTypeId = "naver";
		String redirectUri = naverOAuth2Parameters.getRedirectUri();
		String clientId = StringUtil.getString(siteInfo.get(snsTypeId + "_aid"));
		String  clientSecret = StringUtil.getString(siteInfo.get(snsTypeId + "_asecret"));

		if(naverConnectionFactory == null) naverConnectionFactory = new NaverConnectionFactory(clientId, clientSecret);
		OAuth2Operations oauthOperations = naverConnectionFactory.getOAuthOperations();
		AccessGrant accessGrant = null;
		try {
			accessGrant = oauthOperations.exchangeForAccess(code, redirectUri, null);
		} catch(HttpClientErrorException e){
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		String accessToken = accessGrant.getAccessToken();
		Long expireTime = accessGrant.getExpireTime();
		long currentTime = System.currentTimeMillis();
		
		if(expireTime != null && expireTime < currentTime) {
			accessToken = accessGrant.getRefreshToken();
		}
		
		Naver naver = new NaverTemplate(accessToken);
		try{
			// 1. 정보 얻기
			rbs.egovframework.social.naver.api.User userProfile = naver.fetchObject("nid/me", rbs.egovframework.social.naver.api.User.class);

			//  로그인 : sns 정보를 세션에 저장
			// 회원 설정정보
			JSONObject settingInfo = JSONObjectUtil.getJSONObject(ModuleUtil.getModuleSettingObject("member", 1), "setting_info");
			JSONObject snsItems = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(settingInfo, "sns_info"), "items");
			JSONObject snsItem = JSONObjectUtil.getJSONObject(snsItems, snsTypeId);
			String snsType = JSONObjectUtil.getString(snsItem, "sns_type");
			
			SnsLoginVO snsLoginVO = new SnsLoginVO();
			snsLoginVO.setSnsType(snsType);
			snsLoginVO.setSnsTypeId(snsTypeId);
			snsLoginVO.setSnsTypeName(JSONObjectUtil.getString(snsItem, "sns_name"));
			snsLoginVO.setSnsId(userProfile.getId());
			snsLoginVO.setSnsName(userProfile.getName());
			snsLoginVO.setSnsEmail(userProfile.getEmail());
			session.invalidate();
			session = request.getSession(true);
			session.setAttribute("snsLoginVO", snsLoginVO);

			/*
			System.out.println("- email :" + userProfile.getEmail());
			System.out.println("- id :" + userProfile.getId());
			System.out.println("- name :" + userProfile.getName());
			System.out.println("- gender :" + userProfile.getGender());
			System.out.println("- birthday :" + userProfile.getBirthday());
			*/
		}catch(MissingAuthorizationException e){
			e.printStackTrace();
		}
		
		model.addAttribute("message", MessageUtil.getAlert(null, "opener.fn_snsLoginFormSubmit(); self.close();"));
		return RbsProperties.getProperty("Globals.message.path");
	}
	
	/**
	 * google 로그인
	 * @param code
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sns/google/login.do", method={RequestMethod.GET, RequestMethod.POST})
	public String googleLogin(/*@PathVariable String siteId,*/ @RequestParam String code, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		HttpSession session = request.getSession(true);
		String siteId = StringUtil.getString(session.getAttribute("snsCrtSiteId"));
		session.setAttribute("snsCrtSiteId", null);
	    JSONObject siteInfo = getSiteInfo(siteId);					// 사이트 정보
	    if(JSONObjectUtil.isEmpty(siteInfo)) {
	    	// 메뉴설정파일 없는 경우
			return RbsProperties.getProperty("Globals.error.400.path");
	    }

	    String snsTypeId = "google";
		String redirectUri = googleOAuth2Parameters.getRedirectUri();
		String clientId = StringUtil.getString(siteInfo.get(snsTypeId + "_aid"));
		String  clientSecret = StringUtil.getString(siteInfo.get(snsTypeId + "_asecret"));
		
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();
		GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(httpTransport, jsonFactory, clientId, clientSecret, code, redirectUri).execute();
		
		GoogleCredential credential = new GoogleCredential.Builder()
	        .setTransport(httpTransport)
	        .setJsonFactory(jsonFactory)
	        .setClientSecrets(clientId, clientSecret)
	        .build()
	        .setFromTokenResponse(tokenResponse);
		PeopleService peopleService = new PeopleService.Builder(httpTransport, jsonFactory, credential).build();
		try{
			// 1. 정보 얻기
			Person userProfile = peopleService.people().get("people/me")/*.setRequestMaskIncludeField("person.emailAddresses")*/.setPersonFields("names,emailAddresses").execute();
			Name name = userProfile.getNames().get(0);
			EmailAddress emailAddress = userProfile.getEmailAddresses().get(0);

			//  로그인 : sns 정보를 세션에 저장
			// 회원 설정정보
			JSONObject settingInfo = JSONObjectUtil.getJSONObject(ModuleUtil.getModuleSettingObject("member", 1), "setting_info");
			JSONObject snsItems = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(settingInfo, "sns_info"), "items");
			JSONObject snsItem = JSONObjectUtil.getJSONObject(snsItems, snsTypeId);
			String snsType = JSONObjectUtil.getString(snsItem, "sns_type");
			
			SnsLoginVO snsLoginVO = new SnsLoginVO();
			snsLoginVO.setSnsType(snsType);
			snsLoginVO.setSnsTypeId(snsTypeId);
			snsLoginVO.setSnsTypeName(JSONObjectUtil.getString(snsItem, "sns_name"));
			snsLoginVO.setSnsId(name.getMetadata().getSource().getId());
			snsLoginVO.setSnsName(name.getDisplayName());
			snsLoginVO.setSnsEmail(emailAddress.getValue());
			session.invalidate();
			session = request.getSession(true);
			session.setAttribute("snsLoginVO", snsLoginVO);

			/*
			System.out.println("- email :" + userProfile.getEmail());
			System.out.println("- id :" + userProfile.getId());
			System.out.println("- name :" + userProfile.getName());
			System.out.println("- gender :" + userProfile.getGender());
			System.out.println("- birthday :" + userProfile.getBirthday());
			*/
		}catch(MissingAuthorizationException e){
			e.printStackTrace();
		}
		
		model.addAttribute("message", MessageUtil.getAlert(null, "opener.fn_snsLoginFormSubmit(); self.close();"));
		return RbsProperties.getProperty("Globals.message.path");
	}
	
	public JSONObject getSiteInfo(String siteId) {
		// 2. 사이트 설정 정보 얻기
	    JSONObject siteObject = MenuUtil.getSiteObject("/" + RbsProperties.getProperty("Globals.site.mode.usr") + "/" + siteId);					// 사이트, 메뉴정보 전체
	    if(JSONObjectUtil.isEmpty(siteObject)) {
	    	// 메뉴설정파일 없는 경우
			return null;
	    }
	    return JSONObjectUtil.getJSONObject(siteObject, "site_info");					// 사이트 정보
	}
}
