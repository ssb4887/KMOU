package rbs.modules.adminAuth.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.WebsiteVO;
import rbs.modules.adminAuth.service.AdminAuthService;
import rbs.modules.module.service.ModuleAuthService;
import rbs.modules.module.service.ModuleService;

@Controller
@RequestMapping("/{admSiteId}/adminAuth")
@ModuleMapping(moduleId="adminAuth")
public class AdminAuthController extends ModuleController{
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Resource(name = "adminAuthService")
	private AdminAuthService adminAuthService;
	
	@Resource(name = "moduleService")
	private ModuleService moduleService;
	
	@Resource(name = "moduleAuthService")
	private ModuleAuthService moduleAuthService;

	@ModuleAuth(name="LST")
	@RequestMapping("/list.do")
	public String main(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String ajaxPName = attrVO.getAjaxPName();
		boolean isAjax = attrVO.isAjax();
		
		//이동근 - 관리자 페이지에 보여줄 사용자 페이지 가져오기
		WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		
		String urSiteMode = RbsProperties.getProperty("Globals.site.mode.usr");										// 사용자 모드
		JSONObject urSiteObject = MenuUtil.getSiteObject("/" + urSiteMode + "/" + siteId, false);					// 사이트, 메뉴정보 전체
		JSONArray usrSiteMenuList = null;
		JSONObject usrSiteMenus = null;
    	usrSiteMenuList = JSONObjectUtil.getJSONArray(urSiteObject, "menu-list");
	    usrSiteMenus = JSONObjectUtil.getJSONObject(urSiteObject, "menus");
		model.addAttribute("usrSiteMenuList", usrSiteMenuList);
		model.addAttribute("usrSiteMenus", usrSiteMenus);
		
		JSONObject admSiteObject = getMenusObject();					// 사이트, 메뉴정보 전체
	    if(admSiteObject == null) {
	    	// 메뉴설정파일 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.menu")));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
	    }
	    
	    request.setAttribute("admSiteMenuList", JSONObjectUtil.getJSONArray(admSiteObject, "menu-list"));
	    request.setAttribute("admSiteMenus", JSONObjectUtil.getJSONObject(admSiteObject, "menus"));
	    
    	// 기본경로
    	fn_setCommonPath(attrVO);

		return getViewPath("/list");
	}
	
	/**
	 * 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/input.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, HttpServletRequest request, ModelMap model, @ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		String ajaxPName = attrVO.getAjaxPName();
		boolean isAjax = attrVO.isAjax();
		//String siteMode = attrVO.getSiteMode();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		int mnuCd = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(!isModify || StringUtil.isEmpty(mnuCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
	    
	    JSONArray authManagerArray = null;								// 모듈 권한 설정정보
	    JSONObject authObject = null;									// 모듈 권한 정보
		/*JSONObject admSiteObjects = getMenusObject();					// 사이트, 메뉴정보 전체
		JSONObject admMenu = null;
	    if(!JSONObjectUtil.isEmpty(admSiteObjects)) {
	    	// 메뉴설정파일 있는 경우
		    JSONObject admSiteMenus = JSONObjectUtil.getJSONObject(admSiteObjects, "menus");
		    admMenu = MenuUtil.getMenuInfo(mnuCd, admSiteMenus);
	    }*/
		
		JSONObject admMenu = adminAuthService.getMenuInfo(mnuCd);
	    if(JSONObjectUtil.isEmpty(admMenu)) {
	    	// 메뉴설정파일 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.menu")));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
	    }
		request.setAttribute("admMenu", admMenu);

		// 사용자회원 권한 
	 	List authMemberList = null;
		StringBuffer memberIdxs = new StringBuffer();
		// 메뉴 접근 권한
		String authMVal = JSONObjectUtil.getString(admMenu, "member_idxs");
		if(!StringUtil.isEmpty(authMVal)) memberIdxs.append(authMVal);
		
	    int mauthManagerClosed = JSONObjectUtil.getInt(admMenu, "mauth_manager_closed");
	    if(mauthManagerClosed == 0) {
		    // 모듈정보
		    String admModuleId = JSONObjectUtil.getString(admMenu, "module_id");
		    
		    if(!StringUtil.isEmpty(admModuleId)) {
			    // 1. 선택 모듈 상세 정보
				authManagerArray = ModuleUtil.getModuleAuthManagerArray(admModuleId);
			 	authObject = ModuleUtil.getModuleAuthObject(admModuleId, JSONObjectUtil.getInt(admMenu, "module_auth"));
			 	
				if(!JSONObjectUtil.isEmpty(authObject)) {
					// 회원정보 setting
					Set<?> set = authObject.keySet();
					Iterator<?> keys = set.iterator();
					String key = null;
					//String authMVal = null;
					while(keys.hasNext()) {
						key = (String) keys.next();
						if(key.endsWith("_MBR"))authMVal = JSONObjectUtil.getString(authObject, key);
						else authMVal = null;
						if(!StringUtil.isEmpty(authMVal)){
							int memberLen = memberIdxs.length();
							if(memberLen > 0) memberIdxs.append(",");
							memberIdxs.append(authMVal);
						}
					}
				}
		    }
	    }

	    if(JSONObjectUtil.isEmpty(admMenu) && JSONObjectUtil.isEmpty(authManagerArray)) {
	    	// 모듈권한 설정파일 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.menu")));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
	    }

		int memberLen = memberIdxs.length();
		if(memberLen > 0) {
			authMemberList = CodeHelper.getMemberList(memberIdxs.toString().split(","));
		}
		
	    /*
		// 2. DB
		HashMap<String, Object> dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.MNU_CD", mnuCd));
		param.put("searchList", searchList);
		
		dt = adminAuthService.getModify(siteMode, param);
		
		List memberUstpList = CodeHelper.getMemberUstpList("ADM");
		List memberGrupList = CodeHelper.getMemberGrupList();

		String submitType = "modify";

		model.addAttribute("authManagerArray", authManagerArray);
		model.addAttribute("dt", dt);
		model.addAttribute("memberUstpList", memberUstpList);
		model.addAttribute("memberGrupList", memberGrupList);*/

		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);
		if(authMemberList != null) {
			if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
			optnHashMap.put("mbrMbr", authMemberList);
		}
		model.addAttribute("optnHashMap", optnHashMap);
		//model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("authManagerArray", authManagerArray);
		model.addAttribute("authObject", authObject);
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
		return getViewPath("/input");
	}

	/**
	 * 수정처리
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		String ajaxPName = attrVO.getAjaxPName();
		boolean isAjax = attrVO.isAjax();
		String siteMode=attrVO.getSiteMode();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		int mnuCd = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(!isModify || StringUtil.isEmpty(mnuCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		JSONObject admMenu = adminAuthService.getMenuInfo(mnuCd);
	    if(JSONObjectUtil.isEmpty(admMenu)) {
	    	// 메뉴설정파일 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.menu")));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
	    }

		// 1. 메뉴설정정보 저장
		// 1.2 메뉴 항목설정
	    JSONObject moduleItem = attrVO.getModuleItem();
	    JSONObject menuItemInfo = JSONObjectUtil.getJSONObject(moduleItem, "menu_item_info");
		String submitType = "modify";
		JSONObject menuItems = JSONObjectUtil.getJSONObject(menuItemInfo, "items");
		JSONArray menuItemOrder = JSONObjectUtil.getJSONArray(menuItemInfo, submitType + "proc_order");

		int result = 0;
		result = adminAuthService.update(siteMode, mnuCd, request.getRemoteAddr(), parameterMap, menuItems, menuItemOrder);
		
		// 2. 모듈권한 저장
	    int mauthManagerClosed = JSONObjectUtil.getInt(admMenu, "mauth_manager_closed");
    	if(mauthManagerClosed == 0) {
	    	// 모듈 권한 설정하는 경우 : 모듈 저장
		    String moduleId = JSONObjectUtil.getString(admMenu, "module_id");
		    if(!StringUtil.isEmpty(moduleId)) {
		    	// 모듈 설정된 메뉴인 경우
		    	JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray(moduleId);
		    	if(!JSONObjectUtil.isEmpty(authManagerArray)) {
				 	int authIdx = JSONObjectUtil.getInt(admMenu, "module_auth");
				    
					int moduleAuthCnt = 0;
					Map<String, Object> param = new HashMap<String, Object>();
					List<DTForm> searchList = new ArrayList<DTForm>();
			
					searchList.add(new DTForm("A.MODULE_ID", moduleId));
					searchList.add(new DTForm("A.AUTH_IDX", authIdx));
					param.put("searchList", searchList);
					
					// 모듈권한 등록 여부
					moduleAuthCnt = moduleAuthService.getTotalCount(param);
		
					//String submitType = "modify";
					JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
					JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
					if(moduleAuthCnt > 0) result = moduleAuthService.update(moduleId, authIdx, request.getRemoteAddr(), parameterMap, items, itemOrder);
					else result = moduleAuthService.insert(moduleId, authIdx, request.getRemoteAddr(), parameterMap, items, itemOrder);
		    	}
		    }
	    }
		
		/*Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.MNU_CD", mnuCd));
		param.put("searchList", searchList);
		
		int modifyCnt = adminAuthService.getTotalCount(siteMode, param);
		
	    // 모듈 권한 설정정보
	    JSONArray authManagerArray = getAuthMArray(request, mnuCd);
	    JSONObject admMenu = JSONObjectUtil.getJSONObject(request.getAttribute("admMenu"));
	    if(JSONObjectUtil.isEmpty(admMenu) && JSONObjectUtil.isEmpty(authManagerArray)) {
	    	// 메뉴설정파일 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.menu")));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
	    }

		// DB
    	int result = 0;

		if(modifyCnt > 0) {
			result = adminAuthService.update(siteMode, mnuCd, request.getRemoteAddr(), parameterMap, authManagerArray);
		} else {
			result = adminAuthService.insert(siteMode, mnuCd, request.getRemoteAddr(), parameterMap, authManagerArray);
		}*/
		
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procWReload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 메뉴설정정보
	 * @return
	 */
	public JSONObject getMenusObject() {
		String siteORPath = "/" + RbsProperties.getProperty("Globals.site.mode.adm");
	    return MenuUtil.getSiteObject(siteORPath, false);					// 사이트, 메뉴정보 전체
	}
	
	/**
	 * 관리자모드 메뉴 권한
	 * @param mnuCd
	 * @return
	 */
	public JSONArray getAuthMArray_(HttpServletRequest request, int mnuCd) {
		JSONObject admSiteObjects = getMenusObject();					// 사이트, 메뉴정보 전체
	    if(JSONObjectUtil.isEmpty(admSiteObjects)) {
	    	// 메뉴설정파일 없는 경우
			return null;
	    }
	    
	    JSONObject admSiteMenus = JSONObjectUtil.getJSONObject(admSiteObjects, "menus");
	    JSONObject admMenu = MenuUtil.getMenuInfo(mnuCd, admSiteMenus);

	    if(JSONObjectUtil.isEmpty(admMenu)) {
	    	// 메뉴설정파일 없는 경우
	    	return null;
	    }
		request.setAttribute("admMenu", admMenu);

	    int mauthManagerClosed = JSONObjectUtil.getInt(admMenu, "mauth_manager_closed");
	    if(mauthManagerClosed == 0) {
		    // 모듈정보
		    String admModuleId = JSONObjectUtil.getString(admMenu, "module_id");
		    
		    if(!StringUtil.isEmpty(admModuleId)) {
		    // 1. 선택 모듈 상세 정보
				/*DataMap moduleDt = getModuleInfo(admModuleId);
				String confModule = null;
				if(moduleDt != null) confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
				if(StringUtil.isEmpty(confModule)) confModule = admModuleId;*/
			    return ModuleUtil.getModuleAuthManagerArray(admModuleId);
		    }
	    }
	    
	    return null;
	}
	
	/**
	 * 모듈 상세 정보
	 * @param moduleId
	 * @return
	 */
	public DataMap getModuleInfo(String moduleId) {
		Map<String, Object> moduleParam = new HashMap<String, Object>();
		List<DTForm> moduleSearchList = new ArrayList<DTForm>();
		moduleSearchList.add(new DTForm("A.MODULE_ID", moduleId));
		moduleParam.put("searchList", moduleSearchList);
		DataMap moduleDt = moduleService.getManageView(moduleParam);
		return moduleDt;
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));	// 목록 검색 항목
		PathUtil.fn_setCommonPath(queryString, baseParams, null, searchParams, idxName, pageName);

		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setCommonAddPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setCommonAddPath(HttpServletRequest request) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String grupSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/group/searchList.do";
		String dptSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/depart/searchList.do";
		String mbrSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/memberAn/searchList.do";
		String grupSearchUrl = grupSearchName;
		String dptSearchUrl = dptSearchName;
		String mbrSearchUrl = mbrSearchName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			grupSearchUrl += baseQueryString;
			dptSearchUrl += baseQueryString;
			mbrSearchUrl += baseQueryString;
		}

		request.setAttribute("URL_GRUPSEARCHLIST", grupSearchUrl);
		request.setAttribute("URL_DPRTSEARCHLIST", dptSearchUrl);
		request.setAttribute("URL_MMBRSEARCHLIST", mbrSearchUrl);
	}
}
