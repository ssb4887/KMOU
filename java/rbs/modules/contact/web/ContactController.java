package rbs.modules.contact.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerMapping;

import rbs.egovframework.LoginVO;
import rbs.egovframework.WebsiteVO;
import rbs.modules.contact.service.ContactService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;

/**
 * 웹로그 등록
 * @author usercontact.do
 *
 */
@Controller
public class ContactController{

	@Resource(name="contactService")
	private ContactService contactService;
	
	/**
	 * 사용자 사이트, 통합관리 시스템 
	 * @param menuIdx
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{siteId}/stats/contact.do")
	public String conatact(@RequestParam(value="mId") int menuIdx, HttpServletRequest request, ModelMap model) throws Exception {
		Map<?, ?> matchTemplate = (Map<?, ?>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		String siteId = StringUtil.getString(matchTemplate.get("siteId"));							// siteId
		String admSiteId = RbsProperties.getProperty("Globals.site.id.adm");						// 통합관리시스템 siteId
		
		
		
		HttpSession session = request.getSession(true);
		String sessionId = session.getId();
		String accessIp = ClientUtil.getClientIp(request);
		String accessUrl = request.getHeader("referer");
		//String accessUrl = request.getScheme() + "//" + request.getServerName() + request.getRequestURI() + ((!StringUtil.isEmpty(request.getQueryString()))?"?" + request.getQueryString():"");
		String userAgent = request.getHeader("User-Agent");		// userAgent

		DataForm queryString = PathUtil.getQueryString();
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("menuIdx", menuIdx);
		param.put("sessionId", sessionId);
		param.put("accessIp", accessIp);		
		param.put("accessUrl", accessUrl);		// accessUrl
		param.put("userAgent", userAgent);		// userAgent
		param.put("siteName", queryString.get("sn"));
		param.put("menuName", queryString.get("mn"));
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		if(loginVO != null) {
			param.put("memberIdx", loginVO.getMemberIdx());
			param.put("memberId", loginVO.getMemberIdOrg());
			param.put("memberName", loginVO.getMemberNameOrg());
		}
		
		if(!StringUtil.isEquals(siteId, admSiteId)) {
			// 사용자 사이트
			param.put("siteId", siteId.toUpperCase());
			param.put("mlSiteId", siteId);

			contactService.insertMl(param);
			contactService.insert(param);
		} else {
			// 통합관리 시스템 
			String siteMode = RbsProperties.getProperty("Globals.site.mode.adm");
			param.put("siteId", siteMode.toUpperCase());
			param.put("mlSiteId", siteMode);
			param.put("mngSiteId", "-");
			param.put("mngMenuIdx", 0);

			contactService.insertMl(param);
			contactService.insertMng(param);
		}
		return RbsProperties.getProperty("Globals.message.path");
	}
	
	/**
	 * 통합관리 시스템 : 메뉴콘텐츠관리 > 사용자메뉴관리
	 * @param menuIdx
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{siteId}/menuContents/stats/contact.do")
	public String contentsConatact(@RequestParam(value="mId") int mngMenuIdx, HttpServletRequest request, ModelMap model) throws Exception {
		Map<?, ?> matchTemplate = (Map<?, ?>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		String siteId = StringUtil.getString(matchTemplate.get("siteId"));
		String admSiteId = RbsProperties.getProperty("Globals.site.id.adm");						// 통합관리시스템 siteId
		if(!StringUtil.isEquals(siteId, admSiteId)) {
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		DataForm queryString = PathUtil.getQueryString();
		HttpSession session = request.getSession(true);
		String sessionId = session.getId();
		String accessIp = ClientUtil.getClientIp(request);
		String accessUrl = request.getHeader("referer");
		//String accessUrl = request.getScheme() + "//" + request.getServerName() + request.getRequestURI() + ((!StringUtil.isEmpty(request.getQueryString()))?"?" + request.getQueryString():"");
		String userAgent = request.getHeader("User-Agent");		// userAgent
		String mngSiteId = null;
		String mngSiteName = null;
		String mngMenuName = null;
		WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
		if(usrSiteVO != null) {
			mngSiteId = usrSiteVO.getSiteId();
			mngSiteName = usrSiteVO.getSiteName();
			mngMenuName = StringUtil.getString(queryString.get("mn"));
		}
		String siteMode = RbsProperties.getProperty("Globals.site.mode.adm");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("siteId", siteMode.toUpperCase());
		param.put("mlSiteId", siteMode);
		param.put("menuIdx", 10);
		param.put("mngSiteId", mngSiteId);
		param.put("mngMenuIdx", mngMenuIdx);
		param.put("sessionId", sessionId);
		param.put("accessIp", accessIp);		
		param.put("accessUrl", accessUrl);		// accessUrl
		param.put("userAgent", userAgent);		// userAgent
		param.put("siteName", "통합관리 시스템");
		param.put("menuName", "메뉴콘텐츠관리");
		param.put("mngSiteName", mngSiteName);
		param.put("mngMenuName", mngMenuName);
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();						// 로그인 사용자 정보
		if(loginVO != null) {
			param.put("memberIdx", loginVO.getMemberIdx());
			param.put("memberId", loginVO.getMemberIdOrg());
			param.put("memberName", loginVO.getMemberNameOrg());
		}

		contactService.insertMl(param);
		contactService.insertMng(param);
		return RbsProperties.getProperty("Globals.message.path");
	}
}
