package rbs.modules.menu.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import rbs.egovframework.WebsiteVO;
import rbs.modules.contents.service.ContentsService;
import rbs.modules.menu.service.MenuService;
import rbs.modules.menu.service.MenuVerService;
import rbs.modules.module.service.ModuleFnService;

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
import com.woowonsoft.egovframework.service.WebsiteService;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import com.woowonsoft.egovframework.form.EgovMap;

/**
 * IA(정보구조)관리
 * @author user
 *
 */
@Controller
@RequestMapping("/{admSiteId}/menu")
@ModuleMapping(moduleId="menu")
public class MenuController extends ModuleController{
	
	@Resource(name = "menuService")
	private MenuService menuService;
	
	@Resource(name="menuVerService")
	protected MenuVerService menuVerService;
	
	@Resource(name = "selSiteService")
	protected WebsiteService websiteService;
	
	@Resource(name = "contentsService")
	private ContentsService contentsService;
	
	@Resource(name = "moduleFnService")
	private ModuleFnService moduleFnService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	/**
	 * 사이트 버전
	 * @param selSiteVO
	 * @return
	 */
	public List<Object> getSiteVerList(String siteId) {
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", siteId));
		param.put("searchList", searchList);
		return websiteService.getVersionList(param);
	}
	
	public JSONObject getUsrSiteObject(){
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		String urSiteMode = RbsProperties.getProperty("Globals.site.mode.usr");		// 사용자 모드
		JSONObject urSiteObject = MenuUtil.getSiteObject("/" + urSiteMode + "/" + usrSiteVO.getSiteId());					// 사이트, 메뉴정보 전체
		return urSiteObject;
	}


	@ModuleAuth(name="VEW")
	@RequestMapping("/view.do")
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		DataForm queryString = attrVO.getQueryString();
		
		// 선택버전의 메뉴목록 : 적용 사이트, 메뉴 정보 얻기
		JSONObject urSiteObject = getUsrSiteObject();
		JSONArray usrSiteMenuList = null;
		JSONObject usrSiteMenus = null;
    	usrSiteMenuList = JSONObjectUtil.getJSONArray(urSiteObject, "menu-list");
	    usrSiteMenus = JSONObjectUtil.getJSONObject(urSiteObject, "menus");
	    
		model.addAttribute("queryString", queryString);
		model.addAttribute("usrSiteMenuList", usrSiteMenuList);
		model.addAttribute("usrSiteMenus", usrSiteMenus);
		fn_setCommonViewPath(attrVO);
		return getViewPath("/view");
	}

	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/viewInfo.json", headers="Ajax", params={"menuIdx"})
	public ModelAndView viewInfo(@RequestParam(value="menuIdx") int menuIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		
		JSONObject itemInfo = attrVO.getItemInfo();
		ModelAndView mav = new ModelAndView("jsonView");

		// 선택버전의 메뉴목록 : 적용 사이트, 메뉴 정보 얻기
		JSONObject urSiteObject = getUsrSiteObject();
		JSONObject usrSiteMenus = null;
	    usrSiteMenus = JSONObjectUtil.getJSONObject(urSiteObject, "menus");
	    
		EgovMap dt = null;								// 메뉴정보
		JSONArray settingOrder = null;					// 모듈설정항목

		// 1. 메뉴정보
		JSONObject menuObj = JSONObjectUtil.getJSONObject(usrSiteMenus, "menu" + menuIdx);
		
		if(menuObj == null) {
			// 해당메뉴가 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.menu.no.select.info")));
			//return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			return mav;
		}
		
		String moduleId = JSONObjectUtil.getString(menuObj, "module_id");
		int fnIdx = JSONObjectUtil.getInt(menuObj, "fn_idx");
		String contentsCode = JSONObjectUtil.getString(menuObj, "contents_code");
		int branchIdx = JSONObjectUtil.getInt(menuObj, "branch_idx");
		int moduleAuth = JSONObjectUtil.getInt(menuObj, "module_auth");
		int usertypeIdx = JSONObjectUtil.getInt(menuObj, "usertype_idx");
		int managerUsertypeIdx = JSONObjectUtil.getInt(menuObj, "manager_usertype_idx");

		// 모듈정보
		Map<String, Object> param = new HashMap<String, Object>();
		//List<DTForm> searchList = new ArrayList<DTForm>();
		param.put("MODULE_ID", moduleId);
		param.put("FN_IDX", fnIdx);
		param.put("CONTENTS_CODE", contentsCode);
		param.put("BRANCH_IDX", branchIdx);
		param.put("MODULE_AUTH", moduleAuth);
		dt = menuService.getJSONApplyView(param);
		if(dt == null) dt = new EgovMap();
		
		// 회원등급명
		if(usertypeIdx > 0) dt.put("usertype_idx_name", CodeHelper.getMemberUstpName(usertypeIdx));
		if(managerUsertypeIdx > 0) dt.put("manager_usertype_idx_name", CodeHelper.getMemberUstpName(managerUsertypeIdx));
		
		Set<?> set = menuObj.keySet();
		Iterator<?> keys = set.iterator();
		String key = null;
		while(keys.hasNext()) {
			key = (String) keys.next();
			dt.put(key, menuObj.get(key));
		}
		
		if(!StringUtil.isEmpty(moduleId)) {
			//int fnIdx = StringUtil.getInt(dt.get("fnIdx"));
			String confModule = StringUtil.getString(dt.get("confModule"));
			if(StringUtil.isEmpty(confModule)) confModule = moduleId;
			String designType = StringUtil.getString(dt.get("designType"));
			// 2. 기능설정정보
			JSONObject moduleSettingManagerInfo = ModuleUtil.getModuleSettingManagerObject(confModule, null, designType);
			String menuOrderName = null;
			if(StringUtil.isEquals(moduleId, "poll")) {
				JSONObject moduleSettingInfo = ModuleUtil.getModuleSettingObject(confModule, fnIdx);
				JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSettingInfo, "setting_info");
				int usePrivate = JSONObjectUtil.getInt(settingInfo, "use_private");
				if(usePrivate == 1) menuOrderName = "menu_private_order";
			}
			if(StringUtil.isEmpty(menuOrderName)) {
				menuOrderName = "menu_order";
				if(!StringUtil.isEquals(moduleId, confModule)) {
					menuOrderName = moduleId + "_" + menuOrderName;
				}
			}
			settingOrder = JSONObjectUtil.getJSONArray(moduleSettingManagerInfo, menuOrderName);
		}
		
		model.addAttribute("info", dt);
		model.addAttribute("settingList", settingOrder);
		
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);
		List<Object> authMemberList = null;
		List<Object> authMngMemberList = null;
		// 사용자정보 setting
		StringBuffer memberIdxs = new StringBuffer();
		String[] memberList = StringUtil.stringToArray(dt.get("memberIdxs"), ",");
		if(memberList != null) {
			for(String memberIdx : memberList) {
				int memberLen = memberIdxs.length();
				if(memberLen > 0) memberIdxs.append(",");
				memberIdxs.append(memberIdx);
			}
			int memberLen = memberIdxs.length();
			if(memberLen > 0) {
				authMemberList = CodeHelper.getMemberList(memberIdxs.toString().split(","));
			}
		}
		
		// 관리자정보 setting
		StringBuffer mngMemberIdxs = new StringBuffer();
		String[] mngMemberList = StringUtil.stringToArray(dt.get("managerMemberIdxs"), ",");

		if(mngMemberList != null) {
			for(String memberIdx : mngMemberList) {
				int mngMemberLen = mngMemberIdxs.length();
				if(mngMemberLen > 0) mngMemberIdxs.append(",");
				mngMemberIdxs.append(memberIdx);
			}
			int mngMemberLen = mngMemberIdxs.length();
			if(mngMemberLen > 0) {
				authMngMemberList = CodeHelper.getMemberList(mngMemberIdxs.toString().split(","));
			}
		}
		
		HashMap<String, Object> optnData = new HashMap<String, Object>();
		optnData.put("departIdxs", optnHashMap.get("departIdxs"));
		optnData.put("groupIdxs", optnHashMap.get("groupIdxs"));
		optnData.put("managerDepartIdxs", optnHashMap.get("managerDepartIdxs"));
		optnData.put("managerGroupIdxs", optnHashMap.get("managerGroupIdxs"));
		if(authMemberList != null) {
			optnData.put("memberIdxs", authMemberList);
		}
		if(authMngMemberList != null) {
			optnData.put("managerMemberIdxs", authMngMemberList);
		}
		model.addAttribute("optnData", optnData);
    	
		return mav;
	}

	/**
	 * 작업 목록 엑셀 다운로드
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/excel.do")
	public String excelList(@RequestParam("verIdx")int verIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		String siteId = getSiteId();
    	
		// 2. DB
		int maxMenuLevel = 0;
		List<?> list = null;
		Map<String, Map<String, List<Object>>> multiMapList = new HashMap<String, Map<String,List<Object>>>(); 
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 2.1 검색조건
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		
		param.put("searchList", searchList);

		// 2.2 max menu level
		maxMenuLevel = menuService.getMaxMenuLevel(param);
    		
		// 2.3 목록
		list = menuService.getExcelList(param);
		
		String multiItemId = null;
		multiItemId = "groupIdxs";
		param.put("itemId", multiItemId);
		multiMapList.put(multiItemId, menuService.getGroupMapList(param));
		multiItemId = "departIdxs";
		param.put("itemId", multiItemId);
		multiMapList.put(multiItemId, menuService.getDepartMapList(param));
		multiItemId = "memberIdxs";
		param.put("itemId", multiItemId);
		multiMapList.put(multiItemId, menuService.getMemberMapList(param));

		multiItemId = "managerGroupIdxs";
		param.put("itemId", multiItemId);
		multiMapList.put(multiItemId, menuService.getGroupMapList(param));
		multiItemId = "managerDepartIdxs";
		param.put("itemId", multiItemId);
		multiMapList.put(multiItemId, menuService.getDepartMapList(param));
		multiItemId = "managerMemberIdxs";
		param.put("itemId", multiItemId);
		multiMapList.put(multiItemId, menuService.getMemberMapList(param));

    	model.addAttribute("maxMenuLevel", maxMenuLevel);
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("multiMapList", multiMapList);						// multi 목록
		    	
		return getViewPath("/excel");
	}
	
	/**
	 * 메뉴 설정
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping("/setting.do")
	public String setting(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		boolean isAjax = attrVO.isAjax();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		// 사이트 ID
		String siteId = getSiteId();
		// 버전 목록이 없는 경우
		if(StringUtil.isEmpty(siteId)) {
			String message = rbsMessageSource.getMessage("message.website.no.manager.list.view");
			model.addAttribute("pageMessage", message);
			model.addAttribute("message", MessageUtil.getAlert(isAjax, message));
			return getViewPath("/settingNoManager");
		}
		int verIdx = StringUtil.getInt(request.getParameter("verIdx"));
		// 버전 목록
		List usrSiteVerList = getSiteVerList(siteId);
		
		// 버전 목록이 없는 경우
		if(usrSiteVerList == null) {
			String message = rbsMessageSource.getMessage("message.website.version.no.manager.list");
			model.addAttribute("pageMessage", message);
			model.addAttribute("message", MessageUtil.getAlert(isAjax, message));
			return getViewPath("/settingNoManager");
		}
		
		// 관리할 버전일련번호 setting
		if(usrSiteVerList != null && verIdx <= 0) {
			// 선택 버전이 없는 경우 서비스상태인 버전 가져오기 : 서비스상태인 버전 없는 경우 첫번째 버전 가져오기
			int fstVerIdx = 0;
			int isService = 0;
			for(Object verObj:usrSiteVerList) {
				if(verObj instanceof DataMap){
					DataMap verDt = (DataMap)verObj;
					if(fstVerIdx <= 0) fstVerIdx = StringUtil.getInt(verDt.get("OPTION_CODE"));
					isService = StringUtil.getInt(verDt.get("ISSERVICE"));
					if(isService == 1) {
						verIdx = StringUtil.getInt(verDt.get("OPTION_CODE"));
						break;
					}
				}
			}
			
			if(isService == 0) verIdx = fstVerIdx;
			
			queryString.put("verIdx", verIdx + "");
			attrVO.setQueryString(queryString);
		}
		
		// 선택버전의 메뉴목록 : DB의 사이트, 메뉴 정보 얻기
		JSONArray usrSiteMenuList = null;
		JSONObject usrSiteMenus = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		param.put("searchList", searchList);
    	JSONObject menuJSONObject = websiteService.getMenuJSONObject(param);
    	usrSiteMenuList = JSONObjectUtil.getJSONArray(menuJSONObject, "menu-list");
	    usrSiteMenus = JSONObjectUtil.getJSONObject(menuJSONObject, "menus");
	    
	    List<?> menuList = menuService.getOptnList(param);
		
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(false, items, itemOrder);
		if(menuList != null) {
			if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
			optnHashMap.put("ordIdx", menuList);
			optnHashMap.put("menuLinkIdx", menuList);
		}
		model.addAttribute("optnHashMap", optnHashMap);
		model.addAttribute("submitType", submitType);
		
		
		model.addAttribute("queryString", queryString);
		model.addAttribute("usrSiteVerList", usrSiteVerList);
		model.addAttribute("usrSiteMenuList", usrSiteMenuList);
		model.addAttribute("usrSiteMenus", usrSiteMenus);
		fn_setCommonPath(attrVO);
		return getViewPath("/setting");
	}

	/**
	 * 선택메뉴 정보
	 * @param verIdx
	 * @param menuIdx
	 * @param attrVO
	 * @param request
	 * @param model
	 * @param moduleAttriVO
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/menuInfo.json", headers="Ajax", params={"verIdx", "menuIdx"})
	public ModelAndView menuInfo(@RequestParam(value="verIdx") int verIdx, @RequestParam(value="menuIdx") int menuIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO moduleAttriVO) throws Exception {

		JSONObject itemInfo = attrVO.getItemInfo();
		ModelAndView mav = new ModelAndView("jsonView");

		// 선택사이트 정보
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		String siteType = usrSiteVO.getSiteType();
		
		EgovMap dt = null;								// 메뉴정보
		JSONArray settingOrder = null;					// 모듈설정항목
		List<Object> moduleFnList = null;				// 기능목록
		List<Object> moduleAuthList = null;				// 권한목록
		List<Object> contentsList = null;				// 콘텐츠목록
		List<Object> branchList = null;					// Branch목록

		// 1. 메뉴정보
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		searchList.add(new DTForm("A.MENU_IDX", menuIdx));
		param.put("searchList", searchList);
		dt = menuService.getJSONModify(param);
		
		if(dt == null) {
			return mav;
		}
		
		String moduleId = StringUtil.getString(dt.get("moduleId"));
		if(!StringUtil.isEmpty(moduleId)) {
			String confModule = StringUtil.getString(dt.get("confModule"));
			if(StringUtil.isEmpty(confModule)) confModule = moduleId;
			String designType = StringUtil.getString(dt.get("designType"));
			// 2. 기능설정정보
			JSONObject moduleSettingManagerInfo = ModuleUtil.getModuleSettingManagerObject(confModule, null, designType);
			String menuOrderName = null;
			//System.out.println(moduleId + "1:" + dt.get("fnIdx"));
			if(StringUtil.isEquals(moduleId, "poll")) {
				JSONObject moduleSettingInfo = ModuleUtil.getModuleSettingObject(confModule, StringUtil.getInt(dt.get("fnIdx")));
				JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSettingInfo, "setting_info");
				int usePrivate = JSONObjectUtil.getInt(settingInfo, "use_private");
				if(usePrivate == 1) menuOrderName = "menu_private_order";
				//System.out.println(moduleId + ":" + usePrivate);
			}
			if(StringUtil.isEmpty(menuOrderName)) {
				menuOrderName = "menu_order";
				if(!StringUtil.isEquals(moduleId, confModule)) {
					menuOrderName = moduleId + "_" + menuOrderName;
				}
			}
			settingOrder = JSONObjectUtil.getJSONArray(moduleSettingManagerInfo, menuOrderName);
			
			// 3. 기능 / 콘텐츠 / Branch 목록
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			// 기능 목록
			searchList.add(new DTForm("A.MODULE_ID", moduleId));
			param.put("searchList", searchList);
			moduleFnList = menuService.getModuleFnJSONList(param);
			if(StringUtil.isEquals(moduleId, "contents")) {
				// 콘텐츠 목록
				JSONObject usrSiteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("usrSiteInfo"));
				String lang = JSONObjectUtil.getString(usrSiteInfo, "locale_lang");
				
				String contCd = StringUtil.getString(dt.get("contentsCode"));
				// 4. 콘텐츠목록
				param = new HashMap<String, Object>();
				searchList = new ArrayList<DTForm>();
				param.put("searchList", searchList);
				contentsList = menuService.getContentsJSONList(lang, param);

				if(!StringUtil.isEmpty(contCd)) {
					// 5. Branch목록
					param = new HashMap<String, Object>();
					searchList = new ArrayList<DTForm>();
					searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
					searchList.add(new DTForm("A.BRANCH_TYPE", siteType));
					param.put("searchList", searchList);
					branchList = menuService.getBranchSONList(lang, param);
				}
				model.addAttribute("contentsList", contentsList);
				model.addAttribute("branchList", branchList);
			}
			// 6. 모듈권한
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.MODULE_ID", moduleId));
			param.put("searchList", searchList);
			moduleAuthList = menuService.getModuleAuthJSONList(param);
		}
		
		model.addAttribute("info", dt);
		model.addAttribute("settingList", settingOrder);
		model.addAttribute("moduleFnList", moduleFnList);
		model.addAttribute("moduleAuthList", moduleAuthList);
		
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);
		HashMap<String, Object> multiDataHashMap = menuService.getJSONMultiHashMap(menuIdx, verIdx, siteId, items, itemOrder);
		List authMemberList = null;
		List authMngMemberList = null;
		if(multiDataHashMap != null) {
			// 사용자정보 setting
			StringBuffer memberIdxs = new StringBuffer();
			List memberList = StringUtil.getList(multiDataHashMap.get("memberIdxs"));
			for(Object memberObj : memberList) {
				if(memberObj instanceof EgovMap) {
					EgovMap memberDt = (EgovMap) memberObj;
					int memberLen = memberIdxs.length();
					if(memberLen > 0) memberIdxs.append(",");
					memberIdxs.append(memberDt.get("itemKey"));
				}
			}
			int memberLen = memberIdxs.length();
			if(memberLen > 0) {
				authMemberList = CodeHelper.getMemberList(memberIdxs.toString().split(","));
			}
			
			// 관리자정보
			StringBuffer mngMemberIdxs = new StringBuffer();
			List mngMemberList = StringUtil.getList(multiDataHashMap.get("managerMemberIdxs"));
			for(Object memberObj : mngMemberList) {
				if(memberObj instanceof EgovMap) {
					EgovMap memberDt = (EgovMap) memberObj;
					int mngMemberLen = mngMemberIdxs.length();
					if(mngMemberLen > 0) mngMemberIdxs.append(",");
					mngMemberIdxs.append(memberDt.get("itemKey"));
				}
			}
			int mngMemberLen = mngMemberIdxs.length();
			if(mngMemberLen > 0) {
				authMngMemberList = CodeHelper.getMemberList(mngMemberIdxs.toString().split(","));
			}
		}
		
		HashMap<String, Object> optnData = new HashMap<String, Object>();
		optnData.put("departIdxs", optnHashMap.get("departIdxs"));
		optnData.put("groupIdxs", optnHashMap.get("groupIdxs"));
		optnData.put("managerDepartIdxs", optnHashMap.get("managerDepartIdxs"));
		optnData.put("managerGroupIdxs", optnHashMap.get("managerGroupIdxs"));
		if(authMemberList != null) {
			optnData.put("memberIdxs", authMemberList);
		}
		if(authMngMemberList != null) {
			optnData.put("managerMemberIdxs", authMngMemberList);
		}
		model.addAttribute("optnData", optnData);
		model.addAttribute("multiData", multiDataHashMap);	// multi data 목록
    	
		return mav;
	}

	/**
	 * 선택 모듈 정보
	 * @param moduleId
	 * @param request
	 * @param model
	 * @param moduleAttriVO
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/moduleInfo.json", headers="Ajax", params="moduleId")
	public ModelAndView moduleInfo(@RequestParam(value="moduleId") String moduleId, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO moduleAttriVO) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		
		List<Object> moduleFnList = null;				// 기능목록
		List<Object> moduleAuthList = null;				// 권한목록
		List<Object> contentsList = null;				// 콘텐츠목록

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		/*searchList.add(new DTForm("A.MODULE_ID", moduleId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		searchList.add(new DTForm("A.MENU_IDX", menuIdx));
		param.put("searchList", searchList);
		dt = menuService.getJSONModify(param);*/
		
		if(!StringUtil.isEmpty(moduleId)) {
			// 3. 기능 / 콘텐츠 / Branch 목록
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			// 기능 목록
			searchList.add(new DTForm("A.MODULE_ID", moduleId));
			param.put("searchList", searchList);
			moduleFnList = menuService.getModuleFnJSONList(param);
			if(StringUtil.isEquals(moduleId, "contents")) {
				// 콘텐츠 목록
				JSONObject usrSiteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("usrSiteInfo"));
				String lang = JSONObjectUtil.getString(usrSiteInfo, "locale_lang");
				
				// 4. 콘텐츠목록
				param = new HashMap<String, Object>();
				searchList = new ArrayList<DTForm>();
				param.put("searchList", searchList);
				contentsList = menuService.getContentsJSONList(lang, param);

				model.addAttribute("contentsList", contentsList);
			}
			// 6. 모듈권한
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.MODULE_ID", moduleId));
			param.put("searchList", searchList);
			moduleAuthList = menuService.getModuleAuthJSONList(param);
		}
		
		model.addAttribute("moduleFnList", moduleFnList);
		model.addAttribute("moduleAuthList", moduleAuthList);
    	
		return mav;
	}
	
	/**
	 * 기능 / 콘텐츠 선택 : 기능 정보, 콘텐츠 정보, Branch목록
	 * @param moduleId
	 * @param request
	 * @param model
	 * @param moduleAttriVO
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/moduleFnInfo.json", headers="Ajax", params={"moduleId"})
	public ModelAndView moduleFnInfo(@RequestParam(value="moduleId") String moduleId, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO moduleAttriVO) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");

		// 선택사이트 정보
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		//String siteId = usrSiteVO.getSiteId();
		String lang = usrSiteVO.getLocaleLang();
		String siteType = usrSiteVO.getSiteType();
		
		int fnIdx = StringUtil.getInt(request.getParameter("fId"));
		String contCd = request.getParameter("contCd");
		boolean isCont = (StringUtil.isEquals(moduleId, "contents"))?true:false;
		
		DataMap dt = null;								// 기능정보
		JSONArray settingOrder = null;					// 모듈설정항목
		List<Object> branchList = null;					// Branch목록

		/*JSONObject usrSiteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("usrSiteInfo"));
		String lang = JSONObjectUtil.getString(usrSiteInfo, "locale_lang");*/
		
		// 1. 메뉴정보
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		if(isCont) {
			// 콘텐츠
			searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
			param.put("searchList", searchList);
			dt = contentsService.getModify(lang, param);
			
			// 5. Branch목록
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
			searchList.add(new DTForm("A.BRANCH_TYPE", siteType));
			param.put("searchList", searchList);
			branchList = menuService.getBranchSONList(lang, param);
			
			model.addAttribute("contentsType", dt.get("CONTENTS_TYPE"));
			model.addAttribute("branchList", branchList);
		} else {
			// 일반 기능
			if(fnIdx <= 0) {
				return mav;
			}
			searchList.add(new DTForm("A.MODULE_ID", moduleId));
			searchList.add(new DTForm("A.FN_IDX", fnIdx));
			param.put("searchList", searchList);
			dt = moduleFnService.getModify(param);
			
			String confModule = StringUtil.getString(dt.get("CONF_MODULE"));
			if(StringUtil.isEmpty(confModule)) confModule = moduleId;
			String designType = StringUtil.getString(dt.get("DESIGN_TYPE"));
			// 2. 기능설정정보
			JSONObject moduleSettingManagerInfo = ModuleUtil.getModuleSettingManagerObject(confModule, null, designType);
			JSONObject moduleSettingInfo = ModuleUtil.getModuleSettingObject(confModule, fnIdx);
			JSONObject settingInfo = new JSONObject();
			JSONObject moduleSettingInfoObj = JSONObjectUtil.getJSONObject(moduleSettingInfo, "setting_info");
			if(!StringUtil.isEquals(moduleId, confModule)) {
				 moduleSettingInfoObj = JSONObjectUtil.getJSONObject(moduleSettingInfo, moduleId + "_setting_info");
			}
			if(moduleSettingInfoObj != null) settingInfo.accumulateAll(moduleSettingInfoObj);
			String menuOrderName = null;
			if(StringUtil.isEquals(moduleId, "poll")) {
				// 설문모듈인 경우 : '자신글 결과보기' 사용하는 경우만 '자신글 이력보기' 가능
				int usePrivate = JSONObjectUtil.getInt(settingInfo, "use_private");
				if(usePrivate == 1) {
					menuOrderName = "menu_private_order";
					settingInfo.put("use_private", 0);
				}
				
			}
			if(StringUtil.isEmpty(menuOrderName)) {
				menuOrderName = "menu_order";
				if(!StringUtil.isEquals(moduleId, confModule)) {
					menuOrderName = moduleId + "_" + menuOrderName;
				}
			}
			settingOrder = JSONObjectUtil.getJSONArray(moduleSettingManagerInfo, menuOrderName);
			
			model.addAttribute("designType", dt.get("DESIGN_TYPE"));
			model.addAttribute("settingList", settingOrder);
			if(!JSONObjectUtil.isEmpty(moduleSettingInfo)) model.addAttribute("settingInfo", settingInfo);
		}
    	
		return mav;
	}

	/**
	 * 수정처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/settingProc.do")
	public String settingProc(@RequestParam("verIdx")int verIdx, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();

		// 1. 필수 parameter 검사
		String siteId = getSiteId();
		int menuIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		if(StringUtil.isEmpty(siteId) || menuIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 3. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
    	int result = menuService.update(siteId, verIdx, menuIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "location.reload();"));
			//model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + request.getAttribute("URL_INPUT") + "#" + menuIdx + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 등록처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@RequestParam("verIdx")int verIdx, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();

		String siteId = getSiteId();

		if(StringUtil.isEmpty(siteId)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 4. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
    	int menuIdx = menuService.insert(siteId, verIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	
    	if(menuIdx > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
        	//model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_MenuTree.addMenu(\"" + menuIdx + "\", \"" + StringUtil.replaceJSHtmlN(parameterMap.get("menuName")) + "\");"));
			// model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + request.getAttribute("URL_INPUT") + "&menuIdx=" + menuIdx + "\");"));
        	model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "location.hash = \"#" + menuIdx + "\";" + MessageUtil.getScriptAddWindow(isAjax, "location.reload();")));
        	
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 적용처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/applyProc.do")
	public String applyProc(@RequestParam("verIdx")int verIdx, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();

		String siteId = getSiteId();

		if(StringUtil.isEmpty(siteId)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, "copyproc_order");
		
    	int result = menuVerService.apply(siteId, verIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -11){
    		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.error.data")));
    		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 관리 담당 등록처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/dmngInputProc.do", params={"verIdx", "mngMenuIdx"})
	public String dmngInputProc(@RequestParam("verIdx")int verIdx, @RequestParam(value="mngMenuIdx") int[] mngMenuIdxs, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();

		String siteId = getSiteId();

		if(StringUtil.isEmpty(siteId)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		String submitType = "dmng";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
    	int result = menuService.mngUpdate(siteId, verIdx, mngMenuIdxs, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_menuSettingReset();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 삭제
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.GET, value = "/deleteProc.do")
	public String delete(@RequestParam("verIdx")int verIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		int menuIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(menuIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		//int[] deleteIdxs = {menuIdx};
		String siteId = getSiteId();
		
		// DB
		int result = menuService.delete(siteId, verIdx, menuIdx, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			//model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.delete"), "location.reload();"));
			// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.delete"), "fn_procAction(\"" + request.getAttribute("URL_INPUT") + "\");"));
			
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 복원
	 * @param restoreIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/restoreProc.do", params="select")
	public String restore(@RequestParam("verIdx")int verIdx, @RequestParam(value="select") int[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		String siteId = getSiteId();

		// DB
		int result = menuService.restore(siteId, verIdx, restoreIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.restore"), "fn_procReload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.restore")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	/**
	 * 완전삭제
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="CDT")
	@RequestMapping(method=RequestMethod.POST, value = "/cdeleteProc.do", params="select")
	public String cdelete(@RequestParam("verIdx")int verIdx, @RequestParam(value="select") int[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		String siteId = getSiteId();
		
		// DB
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		int result = menuService.cdelete(siteId, verIdx, deleteIdxs, items, itemOrder);
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.cdelete"), "location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.cdelete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 삭제목록 : 전체 목록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/deleteList.do")
	public String deleteList(@RequestParam("verIdx")int verIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String siteId = getSiteId();
		
		String listSearchId = "list_search";		// 검색설정
    	
    	// 1. 페이지정보 setting
		int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
		int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
		int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
		
		int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
		if(pageUnit == 0) pageUnit = listUnit;	// 페이지당 목록 수
		int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
		int page = StringUtil.getInt(request.getParameter("page"), 1);				// 현재 페이지 index

		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
		// 2. DB
		int totalCount = 0;
		List<?> list = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 2.1 검색조건
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.LAST_MODI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = menuService.getDeleteCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = menuService.getDeleteList(param);
    	}
    	
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드
		    	
    	// 3. 경로 setting
    	// 3.1 기본경로
    	fn_setDeleteListPath(attrVO);
    	
		return getViewPath("/deleteList");
	}
	
	/**
	 * 사이트 아이디
	 * @return
	 */
	private String getSiteId() {
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = null;
		if(usrSiteVO != null) siteId = usrSiteVO.getSiteId();
		
		return siteId;
	}
	
	/**
	 * 경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		//PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);
		String[] baseParams = StringUtil.addStringToArray(this.baseParams, "verIdx");

		if(StringUtil.isEmpty(pageName)) pageName = "page";
		String listName = "setting.do";
		String viewName = "view.do";
		String inputName = "setting.do";
		String inputProcName = "settingProc.do";
		String deleteProcName = "deleteProc.do";
		String deleteListName = "deleteList.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, null, null, pageName, idxName
				, listName, viewName, inputName, inputProcName
				, deleteProcName, deleteListName);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setAddPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setAddPath(HttpServletRequest request) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String inputProcName = "inputProc.do";
		String verListName = "verList.do";
		String menuInfoName ="menuInfo.json";
		String moduleInfoName ="moduleInfo.json";
		String moduleFnInfoName = "moduleFnInfo.json";
		String moduleDesignListName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/moduleFn/designList.do";
		String applyProcName = "applyProc.do";
		String excelDownName = "excel.do";
		String inputProcUrl = inputProcName;
		String verListUrl = verListName;
		String menuInfoUrl = menuInfoName;
		String moduleInfoUrl = moduleInfoName;
		String moduleFnInfoUrl = moduleFnInfoName;
		String moduleDesignListUrl = moduleDesignListName;
		String applyProcUrl = applyProcName;
		String excelDownUrl = excelDownName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			inputProcUrl += baseQueryString;
			verListUrl += baseQueryString;
			menuInfoUrl += baseQueryString;
			moduleInfoUrl += baseQueryString;
			moduleFnInfoUrl += baseQueryString;
			moduleDesignListUrl += baseQueryString;
			applyProcUrl += baseQueryString;
			excelDownUrl += baseQueryString;
		}

		request.setAttribute("URL_INPUTPROC", inputProcUrl);
		request.setAttribute("URL_VERLIST", verListUrl);
		request.setAttribute("URL_MENUINFO", menuInfoUrl);
		request.setAttribute("URL_MODULEINFO", moduleInfoUrl);
		request.setAttribute("URL_MODULEFNINFO", moduleFnInfoUrl);
		request.setAttribute("URL_DESIGNLIST", moduleDesignListUrl);
		request.setAttribute("URL_APPLYPROC", applyProcUrl);
		request.setAttribute("URL_EXCELDOWN", excelDownUrl);
		

		// 검색
		String grupSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/group/searchList.do";
		String dptSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/depart/searchList.do";
		String mbrSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/member/totSearchList.do";
		String mbrAnSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/memberAn/searchList.do";
		String grupSearchUrl = grupSearchName;
		String dptSearchUrl = dptSearchName;
		String mbrSearchUrl = mbrSearchName;
		String mbrAnSearchUrl = mbrAnSearchName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			grupSearchUrl += baseQueryString;
			dptSearchUrl += baseQueryString;
			mbrSearchUrl += baseQueryString;
			mbrAnSearchUrl += baseQueryString;
		}

		request.setAttribute("URL_GRUPSEARCHLIST", grupSearchUrl);
		request.setAttribute("URL_DPRTSEARCHLIST", dptSearchUrl);
		request.setAttribute("URL_MMBRSEARCHLIST", mbrSearchUrl);
		request.setAttribute("URL_MMBRANSEARCHLIST", mbrAnSearchUrl);
	}
	
	/**
	 * 적용구성보기 경로
	 * @param attrVO
	 */
	public void fn_setCommonViewPath(ModuleAttrVO attrVO) {		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		//PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);
		String[] baseParams = StringUtil.addStringToArray(this.baseParams, "verIdx");

		if(StringUtil.isEmpty(pageName)) pageName = "page";
		String listName = "setting.do";
		String viewName = "view.do";
		String inputName = "setting.do";
		String inputProcName = "settingProc.do";
		String deleteProcName = "deleteProc.do";
		String deleteListName = "deleteList.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, null, null, pageName, idxName
				, listName, viewName, inputName, inputProcName
				, deleteProcName, deleteListName);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setAddViewPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setAddViewPath(HttpServletRequest request) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String menuInfoName ="viewInfo.json";
		String menuInfoUrl = menuInfoName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			menuInfoUrl += baseQueryString;
		}

		request.setAttribute("URL_VIEWINFO", menuInfoUrl);
	}
	
	/**
	 * 휴지통 경로
	 */
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");								// 목록 페이징  key
		String[] baseParams = StringUtil.addStringToArray(this.baseParams, "verIdx");						// 기본 parameter

		// 항목 설정 정보
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		//PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
		if(StringUtil.isEmpty(pageName)) pageName = "page";
		String listName = "deleteList.do";
		String restoreProcName = "restoreProc.do";
		String cdeleteProcName = "cdeleteProc.do";
		
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, null, pageName, listName, restoreProcName, cdeleteProcName);
	
	}
}
