package rbs.modules.menu.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rbs.egovframework.WebsiteVO;
import rbs.modules.menu.service.MenuPointService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@RequestMapping("/{admSiteId}/stats")
@ModuleMapping(moduleId="menuPoint", confModule="menu", confSModule="point")
public class MenuPointController extends ModuleController{

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name="menuPointService")
	MenuPointService menuPointService;
	
	public String getStatsViewPath(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {

		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		int verIdx = usrSiteVO.getVerIdx();
		if(StringUtil.isEmpty(siteId)) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.website.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail.path");
		}

		JSONArray list = null;
		DataMap menuPointTotalDt = null;
		Map<Object, Object> menuPointMap = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		
		param.put("searchList", searchList);
		
		menuPointTotalDt = menuPointService.getTotalView(param);
		menuPointMap = menuPointService.getMenuPointMap(param);
		
		// 메뉴목록
		String urSiteMode = RbsProperties.getProperty("Globals.site.mode.usr");										// 사용자 모드
		JSONObject urSiteObject = MenuUtil.getSiteObject("/" + urSiteMode + "/" + siteId, false);					// 사이트, 메뉴정보 전체
		JSONArray usrSiteMenuList = null;
		JSONObject usrSiteMenus = null;
    	usrSiteMenuList = JSONObjectUtil.getJSONArray(urSiteObject, "menu-list");
	    usrSiteMenus = JSONObjectUtil.getJSONObject(urSiteObject, "menus");
	    list = MenuUtil.getUsePollMenuList(null, 1, usrSiteMenuList, usrSiteMenus);
	    
		model.addAttribute("menuPointTotalDt", menuPointTotalDt);
		model.addAttribute("menuPointMap", menuPointMap);
		model.addAttribute("list", list);
		return null;
	}

	@RequestMapping("/pointList.do")
	public String main(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String viewPath = getStatsViewPath(attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/list");
	}

	/**
	 * 엑셀 다운로드
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pointExcel.do")
	public String excelList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String viewPath = getStatsViewPath(attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	
		return getViewPath("/pointExcel");
	}

	@RequestMapping("/pointView.do")
	public String view(/*@RequestParam(value="menuIdx") int menuIdx, */@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		int verIdx = usrSiteVO.getVerIdx();
		if(StringUtil.isEmpty(siteId)) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.website.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		int menuIdx = StringUtil.getInt(request.getParameter("menuIdx"));
		
		int totalCount = 0;
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		if(menuIdx > 0) searchList.add(new DTForm("A.MENU_IDX", menuIdx));
		
		param.put("searchList", searchList);
		totalCount = menuPointService.getPointPTotalCount(param);
		
		String lang = usrSiteVO.getLocaleLang();
		list = menuPointService.getPointPList(lang, param);

		model.addAttribute("totalCount", totalCount);
		model.addAttribute("list", list);
		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/view");
	}

	@RequestMapping("/pointMemberList.do")
	public String memberList(/*@RequestParam(value="menuIdx") int menuIdx, */@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		int verIdx = usrSiteVO.getVerIdx();
		if(StringUtil.isEmpty(siteId)) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.website.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		int menuIdx = StringUtil.getInt(request.getParameter("menuIdx"));

		JSONObject settingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "member_setting_info");
		//JSONObject itemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "member_item_info");

		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", 0, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", 0, propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
			
			String pageName = JSONObjectUtil.getString(settingInfo, "page_name");
			int page = StringUtil.getInt(request.getParameter(pageName), 1);				// 현재 페이지 index
	
			paginationInfo.setUnitBeginCount(listUnit);
			paginationInfo.setUnitEndCount(listMaxUnit);
			paginationInfo.setUnitStep(listUnitStep);
			paginationInfo.setCurrentPageNo(page);
			paginationInfo.setRecordCountPerPage(pageUnit);
			paginationInfo.setPageSize(pageSize);
		}
		
		int totalCount = 0;
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		if(menuIdx > 0) searchList.add(new DTForm("A.MENU_IDX", menuIdx));
		
		param.put("searchList", searchList);
		totalCount = menuPointService.getPointMTotalCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
		
		if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    	}
			list = menuPointService.getPointMList(param);
		}
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("list", list);
		
    	// 4. 기본경로
		fn_setMemberPath(attrVO);
		return getViewPath("/memberList");
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
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		//PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "pointList.do");

		String listName = "pointList.do";
		String viewName = "pointView.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, null, null, pageName, idxName
				, listName, viewName, null, null
				, null, null);

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setAddPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setAddPath(HttpServletRequest request) {
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String memberListName = "pointMemberList.do";
		String excelDownName = "pointExcel.do";
		String excelDownUrl = excelDownName;
		String memberListUrl = memberListName;
		if(!StringUtil.isEmpty(allQueryString)) {
			excelDownUrl += allQueryString;
			memberListUrl += allQueryString;
		}

		request.setAttribute("URL_EXCELDOWN", excelDownUrl);
		request.setAttribute("URL_MEMBERLIST", memberListUrl);
	}
	
	public void fn_setMemberPath(ModuleAttrVO attrVO) {	
		JSONObject memberSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "member_setting_info");
		JSONObject memberItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "member_item_info");
		DataForm queryString = attrVO.getQueryString();

		String pageName = JSONObjectUtil.getString(memberSettingInfo, "page_name");		// 목록 페이징  key
		String[] baseParams = fn_getPointParams(attrVO);
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(memberItemInfo, "list_search"));					// 목록 검색 항목
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "pointMemberList.do");
	}
	
	public String[] fn_getPointParams(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		String[] tabBaseParams = null;//{"moduleId"};	
		
		return PathUtil.getTotalParams(baseParams, tabBaseParams, searchParams, null, null, pageName, idxName);
	}
}
