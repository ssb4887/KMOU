package rbs.modules.contact.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@RequestMapping("/{admSiteId}/stats")
@ModuleMapping(moduleId="contactSitePage", confModule="contact")
public class ContactSitePageController extends ModuleController{
	
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
		JSONObject itemInfo = JSONObjectUtil.getJSONObject(moduleItem, "sitepage_item_info");
		DataForm queryString = attrVO.getQueryString();
		
		int sitePageMaxCount = 0;
		DataMap totalDt = null;
		List<Object> list = null;

		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		JSONObject searchKey = JSONObjectUtil.getJSONObject(listSearch, "key");
		JSONObject searchItems = JSONObjectUtil.getJSONObject(searchKey, "items");
		JSONObject statsTypeItem = JSONObjectUtil.getJSONObject(searchItems, "statsType");

		Calendar currentDate  = Calendar.getInstance();
		int defaultSearchType = JSONObjectUtil.getInt(statsTypeItem, "default_value");
		String paramSearchType = request.getParameter("is_statsType");
		int searchType = StringUtil.getInt(paramSearchType, defaultSearchType);
		String searchYear = request.getParameter("is_statsDate1");
		String searchMonth = request.getParameter("is_statsDate2");
		String searchDay = request.getParameter("is_statsDate3");
		if(StringUtil.isEmpty(paramSearchType)) {
			// 처음 접속시 : 현재 년, 월, 일
			if(defaultSearchType > 0) {
				searchYear = currentDate.get(Calendar.YEAR) + "";
				searchMonth = StringUtil.getLPAD(currentDate.get(Calendar.MONTH) + 1, "0", 2);
				searchDay = StringUtil.getLPAD(currentDate.get(Calendar.DAY_OF_MONTH), "0", 2);
				
				queryString.put("is_statsDate1", searchYear + "");
				queryString.put("is_statsDate2", searchMonth + "");
				queryString.put("is_statsDate3", searchDay + "");
			}
			queryString.put("is_statsType", defaultSearchType + "");
			
			attrVO.setQueryString(queryString);
			model.addAttribute("queryString", queryString);
		}

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 월/일/시간 별 검색
		if(searchType > 0) {
			searchList.add(new DTForm("YEAR", searchYear));
		} else {
			searchYear = null;
		}
		// 일/시간 별 검색
		if(searchType > 1) {
			searchList.add(new DTForm("MONTH", searchMonth));
		}
		// 시간 별 검색
		if(searchType > 2) {
			searchList.add(new DTForm("DAY", searchDay));
		}
		
		param.put("searchType", searchType);
		param.put("siteId", siteId.toUpperCase());
		param.put("searchList", searchList);
		
		boolean isSingleTable = false;		// 단일 테이블 조회 여부
		
		// 백업 테이블 조회 목록
		Map<String, Object> backupParam = new HashMap<String, Object>();
		backupParam.put("siteId", siteId);
		if(!StringUtil.isEmpty(searchYear)) {
			backupParam.put("searchYear", searchYear);
			isSingleTable = true;
		}
		
		List<Object> backupYearList = contactService.getBackupYearList(backupParam);
		if(isSingleTable && backupYearList != null && backupYearList.size() > 0) {
			// 단일 테이블 조회 && 백업 테이블에서 조회하는 경우
			//param.put("backupYear", searchYear);
			int searchYearNum = Integer.parseInt(searchYear);
			int currentYearNum = currentDate.get(Calendar.YEAR);
			//String currentYear = currentYearNum + "";
			if(searchYearNum == currentYearNum || searchYearNum + 1 == currentYearNum) {
				//param.put("useCurrentTable", 1);
				param.put("backupYearList", backupYearList);
			} else {
				// 단일 테이블 조회 && 백업 테이블에서 조회하는 경우
				backupYearList = new ArrayList<Object>();
				backupYearList.add(searchYear);
				param.put("backupYearList", backupYearList);
				//param.put("backupYear", searchYear);
			}
		}
		else if(!isSingleTable) {
			// 년도별 검색 : 백업테이블 모두 포함
			param.put("backupYearList", backupYearList);
		}
		
		sitePageMaxCount = contactService.getSitePageMaxCount(param);
		totalDt = contactService.getSitePageTotalView(param);
		list = contactService.getSitePageList(param);
		
		model.addAttribute("sitePageMaxCount", sitePageMaxCount);
		model.addAttribute("totalDt", totalDt);
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목 코드
    	model.addAttribute("itemInfo", itemInfo);
    	attrVO.setItemInfo(itemInfo);
    	
    	return null;
	}

	@RequestMapping("/sitePageList.do")
	public String siteList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String viewPath = getStatsViewPath(attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/sitePageList");
	}

	/**
	 * 엑셀 다운로드
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sitePageExcel.do")
	public String excelList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String viewPath = getStatsViewPath(attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	
		return getViewPath("/sitePageExcel");
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
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "sitePageList.do");


		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String excelDownName = "sitePageExcel.do";
		String excelDownUrl = excelDownName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			excelDownUrl += baseQueryString;
		}

		request.setAttribute("URL_EXCELDOWN", excelDownUrl);
	}

	@RequestMapping("/admSitePageList.do")
	public String admSiteList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String viewPath = getStatsViewPath(RbsProperties.getProperty("Globals.site.mode.adm"), attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	// 4. 기본경로
    	fn_setAdmCommonPath(attrVO);
		return getViewPath("/sitePageList");
	}

	/**
	 * 엑셀 다운로드
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/admSitePageExcel.do")
	public String admExcelList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String viewPath = getStatsViewPath(RbsProperties.getProperty("Globals.site.mode.adm"), attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	
		return getViewPath("/admSitePageExcel");
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
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "admSitePageList.do");


		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String excelDownName = "admSitePageExcel.do";
		String excelDownUrl = excelDownName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			excelDownUrl += baseQueryString;
		}

		request.setAttribute("URL_EXCELDOWN", excelDownUrl);
	}
}
