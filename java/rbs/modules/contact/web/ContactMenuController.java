package rbs.modules.contact.web;

import java.util.ArrayList;
import java.util.Calendar;
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
import rbs.modules.contact.service.ContactService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.CodeHelper;
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
@ModuleMapping(moduleId="contactMenu", confModule="contact")
public class ContactMenuController extends ModuleController{
	
	@Resource(name="contactService")
	ContactService contactService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	public String getSiteId(){
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		if(usrSiteVO != null) return usrSiteVO.getSiteId();
		return null;
	}
	
	public String getStatsViewPath(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String siteId = getSiteId();
		if(StringUtil.isEmpty(siteId)) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.website.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		return getStatsViewPath(siteId, attrVO, request, model);
	}
	
	public String getStatsViewPath(String siteId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		JSONObject moduleItem = attrVO.getModuleItem();
		JSONObject itemInfo = JSONObjectUtil.getJSONObject(moduleItem, "menu_item_info");
		DataForm queryString = attrVO.getQueryString();
		
		int totalSum = 0;
		JSONArray list = null;
		Map<Object, Object> menuMap = null;

		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		JSONObject searchKey = JSONObjectUtil.getJSONObject(listSearch, "key");
		JSONObject searchItems = JSONObjectUtil.getJSONObject(searchKey, "items");
		
		String searchDate1 = request.getParameter("is_statsDate1");
		String searchDate2 = request.getParameter("is_statsDate2");
		if(StringUtil.isEmpty(searchDate1) && StringUtil.isEmpty(searchDate2)) {
			// 처음 접속시 : 금일/금주/금월/금년

			JSONObject statsTypeItem = JSONObjectUtil.getJSONObject(searchItems, "statsType");
			
			int defaultSearchType = JSONObjectUtil.getInt(statsTypeItem, "default_value");
			
			Calendar currentDate  = Calendar.getInstance();
			if(defaultSearchType == 2) {
				// 금주
				int dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
				Calendar sDate = Calendar.getInstance();
				Calendar eDate = Calendar.getInstance();
				sDate.add(Calendar.DATE, dayOfWeek * -1 + 1);
				eDate.add(Calendar.DATE, 7 - dayOfWeek);

				searchDate1 = sDate.get(Calendar.YEAR) + "-" + 
							StringUtil.getLPAD(sDate.get(Calendar.MONTH) + 1, "0", 2) + "-" + 
							StringUtil.getLPAD(sDate.get(Calendar.DAY_OF_MONTH), "0", 2);

				searchDate2 = eDate.get(Calendar.YEAR) + "-" + 
							StringUtil.getLPAD(eDate.get(Calendar.MONTH) + 1, "0", 2) + "-" + 
							StringUtil.getLPAD(eDate.get(Calendar.DAY_OF_MONTH), "0", 2);
			} else if(defaultSearchType == 3) {
				// 금월
				searchDate1 = currentDate.get(Calendar.YEAR) + "-" + 
						StringUtil.getLPAD(currentDate.get(Calendar.MONTH) + 1, "0", 2) + "-01";
				searchDate2 = currentDate.get(Calendar.YEAR) + "-" + 
						StringUtil.getLPAD(currentDate.get(Calendar.MONTH) + 1, "0", 2) + "-" + StringUtil.getLPAD(currentDate.getActualMaximum(Calendar.DAY_OF_MONTH), "0", 2);
			} else if(defaultSearchType == 4) {
				// 금년
				searchDate1 = currentDate.get(Calendar.YEAR) + "-01-01";
				searchDate2 = currentDate.get(Calendar.YEAR) + "-12-31";
			} else {
				// 금일
				searchDate1 = currentDate.get(Calendar.YEAR) + "-" + 
							StringUtil.getLPAD(currentDate.get(Calendar.MONTH) + 1, "0", 2) + "-" + 
							StringUtil.getLPAD(currentDate.get(Calendar.DAY_OF_MONTH), "0", 2);
				searchDate2 = searchDate1;
			}
			queryString.put("is_statsType", defaultSearchType + "");
			queryString.put("is_statsDate1", searchDate1);
			queryString.put("is_statsDate2", searchDate2);
			
			attrVO.setQueryString(queryString);
			model.addAttribute("queryString", queryString);
		}

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		param.put("searchDate1", searchDate1);
		param.put("searchDate2", searchDate2);
		param.put("siteId", siteId.toUpperCase());
		param.put("searchList", searchList);
		
		// 백업 테이블 조회 목록
		Map<String, Object> backupParam = new HashMap<String, Object>();
		List<DTForm> backupSearchList = new ArrayList<DTForm>();
		backupParam.put("siteId", siteId);
		if(!StringUtil.isEmpty(searchDate1)){
			String sYear = searchDate1.substring(0, searchDate1.indexOf("-"));
			backupSearchList.add(new DTForm("YEAR", sYear, ">="));
		}
		if(!StringUtil.isEmpty(searchDate2)){
			String sYear = searchDate2.substring(0, searchDate2.indexOf("-"));
			backupSearchList.add(new DTForm("YEAR", sYear, "<="));
		}
		backupParam.put("searchList", searchList);
		
		List<Object> backupYearList = contactService.getBackupYearList(backupParam);
		/*if(backupYearList != null && backupYearList.size() > 0) {
			// 단일 테이블 조회 && 백업 테이블에서 조회하는 경우
			param.put("backupYear", searchYear);
		}
		else if(!isSingleTable) {*/
			// 백업테이블
			param.put("backupYearList", backupYearList);
		//}
		
		totalSum = contactService.getMenuTotalSum(param);
		menuMap = contactService.getMenuMap(param);
		
		// 메뉴목록
		JSONObject usrSiteObject = null;
		String usrSiteMode = RbsProperties.getProperty("Globals.site.mode.usr");							// 사용자 모드
		String admSiteMode = RbsProperties.getProperty("Globals.site.mode.adm");							// 관리자 모드
		if(StringUtil.isEquals(siteId, admSiteMode)) {
			JSONObject admSiteObject = MenuUtil.getSiteObject("/" + admSiteMode, false);					// 사이트, 메뉴정보 전체
			JSONArray admSiteMenuList = null;
			JSONObject admSiteMenus = null;
	    	admSiteMenuList = JSONObjectUtil.getJSONArray(admSiteObject, "menu-list");
		    admSiteMenus = JSONObjectUtil.getJSONObject(admSiteObject, "menus");
		    list = MenuUtil.getMenuList(null, 1, admSiteMenuList, admSiteMenus);
			model.addAttribute("list", list);
			
			usrSiteObject = MenuUtil.getSiteObject("/" + usrSiteMode + "/" + getSiteId(), false);				// 사용자 사이트, 메뉴정보 전체
		} else {
			usrSiteObject = MenuUtil.getSiteObject("/" + usrSiteMode + "/" + siteId, false);					// 사이트, 메뉴정보 전체
		}
		JSONArray usrSiteMenuList = null;
		JSONObject usrSiteMenus = null;
    	usrSiteMenuList = JSONObjectUtil.getJSONArray(usrSiteObject, "menu-list");
	    usrSiteMenus = JSONObjectUtil.getJSONObject(usrSiteObject, "menus");
	    list = MenuUtil.getMenuList(null, 1, usrSiteMenuList, usrSiteMenus);
	    
		model.addAttribute("queryString", queryString);
		if(StringUtil.isEquals(siteId, admSiteMode)) model.addAttribute("mngList", list);
		else model.addAttribute("list", list);
		
		model.addAttribute("totalSum", totalSum);
    	model.addAttribute("menuMap", menuMap);															// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목 코드
    	model.addAttribute("itemInfo", itemInfo);
    	attrVO.setItemInfo(itemInfo);
    	
    	return null;
	}
	
	@RequestMapping("/menuList.do")
	public String menuList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String viewPath = getStatsViewPath(attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/menuList");
	}

	/**
	 * 엑셀 다운로드
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/menuExcel.do")
	public String excelList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String viewPath = getStatsViewPath(attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	
		return getViewPath("/menuExcel");
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "menuList.do");


		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String excelDownName = "menuExcel.do";
		String excelDownUrl = excelDownName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			excelDownUrl += baseQueryString;
		}

		request.setAttribute("URL_EXCELDOWN", excelDownUrl);
	}
	
	@RequestMapping("/admMenuList.do")
	public String admMenuList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String viewPath = getStatsViewPath(RbsProperties.getProperty("Globals.site.mode.adm"), attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	// 4. 기본경로
    	fn_setAdmCommonPath(attrVO);
		return getViewPath("/admMenuList");
	}

	/**
	 * 엑셀 다운로드
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admMenuExcel.do")
	public String admExcelList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String viewPath = getStatsViewPath(RbsProperties.getProperty("Globals.site.mode.adm"), attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	
		return getViewPath("/admMenuExcel");
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setAdmCommonPath(ModuleAttrVO attrVO) {	

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "admMenuList.do");


		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String excelDownName = "admMenuExcel.do";
		String excelDownUrl = excelDownName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			excelDownUrl += baseQueryString;
		}

		request.setAttribute("URL_EXCELDOWN", excelDownUrl);
	}
}
