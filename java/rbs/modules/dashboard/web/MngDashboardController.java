package rbs.modules.dashboard.web;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DataForm;
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

import rbs.egovframework.WebsiteVO;
import rbs.modules.dashboard.service.MngDashboardService;

@Controller
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/main"})
@ModuleMapping(moduleId="dashboard")
public class MngDashboardController extends ModuleController{
	
	@Resource(name = "mngDashboardService")
	private MngDashboardService mngDashboardService;
	
	/** EgovMessageSource */
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MngDashboardController.class);

	@ModuleAuth(name="MNG")
	@RequestMapping("/main.do")
	public String main(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @ParamMap ParamForm parameterMap) {
		
		HttpSession session = request.getSession(true);
		WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		int siteVerIdx = StringUtil.getInt(session.getAttribute("siteVerIdx"));
		if(siteVerIdx <= 0) {
			JSONObject usrSiteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("usrSiteInfo"));
			siteVerIdx = JSONObjectUtil.getInt(usrSiteInfo, "version");
		}
		
		if(siteVerIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + attrVO.getAjaxPName() + ".path");
		}

		String siteMode = RbsProperties.getProperty("Globals.site.mode.usr");
		// dashboard 설정 얻기
		JSONObject dashboardObjects = MenuUtil.getDashboardObject("/" + siteMode + "/" + siteId, siteVerIdx, attrVO.getModuleId());
		// 메뉴 설정 얻기
		JSONObject usrMenus = JSONObjectUtil.getJSONObject(request.getAttribute("usrSiteMenus"));
		JSONArray usrMenuList = JSONObjectUtil.getJSONArray(request.getAttribute("usrSiteMenuList"));
		
		// dashboard 설정 정보와 메뉴 설정 module_id 비교 : 메뉴와 일치하지 않는 경우 dashboard 설정(dashboardObjects)에서 삭제
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONArray checkModules = JSONObjectUtil.getJSONArray(moduleSetting, "check_module");
		JSONObject optionModules = JSONObjectUtil.getJSONObject(moduleSetting, "option_module");
		JSONObject settingInfo = null;
		JSONObject dashboardObject = null;
		JSONObject dashboardLObject = null;
		int dashboardMenuIdx = 0;
		JSONObject menuObj = null;
		String menuModuleId = null;
		int menuFnIdx = 0;
		HashMap<String, Object> optnHashMap = new HashMap<String, Object>();
		for(Object moduleIdObj:checkModules) {
			String moduleId = StringUtil.getString(moduleIdObj);
			dashboardObject = JSONObjectUtil.getJSONObject(dashboardObjects, moduleId);
			if(JSONObjectUtil.isEmpty(dashboardObject)) continue;
			
			settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, moduleId + "_setting_info");
			int itemNumber = JSONObjectUtil.getInt(settingInfo, "item_number");
			if(itemNumber <= 0) continue;
			
			for(int itemIdx = 1;itemIdx <= itemNumber;itemIdx ++) {
				dashboardLObject = JSONObjectUtil.getJSONObject(dashboardObject, moduleId + itemIdx);
				if(JSONObjectUtil.isEmpty(dashboardLObject)) continue;
				dashboardMenuIdx = JSONObjectUtil.getInt(dashboardLObject, "menu_idx");
				menuObj = JSONObjectUtil.getJSONObject(usrMenus, "menu" + dashboardMenuIdx);
				menuModuleId = JSONObjectUtil.getString(menuObj, "module_id");
				
				// 메뉴의 모듈과 일치하지 않는 경우 삭제
				if(!StringUtil.isEquals(moduleId, menuModuleId)) {
					dashboardObject.remove(moduleId + itemIdx);
				}

				// 메뉴명 setting
				//dashboardLObject.put("menu_idx_oname", JSONObjectUtil.getString(menuObj, "menu_name"));
				dashboardLObject.put("menu_idx_name", MenuUtil.getMenuPathName(dashboardMenuIdx, 2, 1, usrMenuList, usrMenus));

				// 옵션사용 안하는 모듈은 아래 실행 안함
				if(JSONObjectUtil.getInt(optionModules, moduleId) != 1) continue;
				
				// option 목록 얻기
				menuFnIdx = JSONObjectUtil.getInt(menuObj, "fn_idx");
				
				// 모듈 전체 설정
				JSONObject moduleTotalObject = ModuleUtil.getModuleTotalSettingObject(menuModuleId);
				JSONObject moduleObject = JSONObjectUtil.getJSONObject(moduleTotalObject, "item" + menuFnIdx);
				
				String mstCd = JSONObjectUtil.getString(moduleObject, "dset_cate_list_master_code");
				if(!StringUtil.isEmpty(mstCd) && optnHashMap.get(mstCd) == null) optnHashMap.put(mstCd, CodeHelper.getOptnList(mstCd));
				if(!StringUtil.isEmpty(mstCd)) dashboardLObject.put("master_code", mstCd);
			}
		}
		
		String submitType = "modify";
		if(dashboardObjects != null) model.addAttribute("dashboardDt", JSONObjectUtil.toDataMap(dashboardObjects));
		
		// 배너 사용위치 master_code
		String moduleId = "banner";
		settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, moduleId + "_setting_info");
		String itemMasterCode = JSONObjectUtil.getString(settingInfo, "item_master_code");
		List<Object> bannerItemOptnList = CodeHelper.getOptnList(itemMasterCode);
		model.addAttribute("bannerItemOptnList", bannerItemOptnList);
		
		model.addAttribute("optnHashMap", optnHashMap);
		model.addAttribute("submitType", submitType);											// 페이지구분
		
		// 5. 기본경로
		fn_setCommonPath(attrVO);

    	// 6. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
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
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject moduleItem = attrVO.getModuleItem();
		JSONObject moduleSetting = attrVO.getModuleSetting();
		HttpSession session = request.getSession(true);
		WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		int siteVerIdx = StringUtil.getInt(session.getAttribute("siteVerIdx"));
		if(siteVerIdx <= 0) {
			JSONObject usrSiteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("usrSiteInfo"));
			siteVerIdx = JSONObjectUtil.getInt(usrSiteInfo, "version");
		}
		
		if(siteVerIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + attrVO.getAjaxPName() + ".path");
		}
		
		// DB
		int result = mngDashboardService.update(siteId, siteVerIdx, attrVO.getModuleId(), parameterMap, moduleSetting, moduleItem);
		if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procReload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	@RequestMapping(value = "/optnSearch.json", headers="Ajax", params={"mdId", "sfIdx"})
	public ModelAndView optnSearch(@RequestParam(value="mdId") String moduleId, @RequestParam(value="sfIdx") int fnIdx, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO moduleAttriVO) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		JSONObject moduleTotalObject = ModuleUtil.getModuleTotalSettingObject(moduleId);
		JSONObject moduleObject = JSONObjectUtil.getJSONObject(moduleTotalObject, "item" + fnIdx);
		
		String mstCd = JSONObjectUtil.getString(moduleObject, "dset_cate_list_master_code");
		if(!StringUtil.isEmpty(mstCd)) model.addAttribute("list", CodeHelper.getOptnJSONList(mstCd));
    	
		return mav;
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	

		DataForm queryString = attrVO.getQueryString();
		
		String inputName = "main.do";
		String inputProcName = "inputProc.do";
		PathUtil.fn_setInputPath(queryString, baseParams, inputName, inputProcName);
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setOptnListPath(request);
	}
	
	/**
	 * 메뉴검색 경로
	 * @param request
	 */
	public void fn_setOptnListPath(HttpServletRequest request) {
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

	    WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
		String menuSearchListName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/menuContents/" + usrSiteVO.getSiteId() + "/menu/searchOList.do";
		String optnSearchJSONName = "optnSearch.json";
		String menuSearchListUrl = menuSearchListName;
		String optnSearchJSONUrl = optnSearchJSONName;
		if(!StringUtil.isEmpty(allQueryString)) {
			menuSearchListUrl += allQueryString;
			optnSearchJSONUrl += allQueryString;
		}

		request.setAttribute("URL_MENUSEARCHLIST", menuSearchListUrl);
		request.setAttribute("URL_OPTNSEARCHJSON", optnSearchJSONUrl);
	}
}
