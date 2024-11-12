package rbs.modules.log.web;

import java.text.MessageFormat;
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

import rbs.modules.log.service.MlLogService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@RequestMapping("/{admSiteId}/log")
@ModuleMapping(moduleId="mlLog", confModule="log")
public class MlLogController extends ModuleController{
	
	@Resource(name="mlLogService")
	MlLogService mlLogService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	/**
	 * 코드
	 * @param submitType
	 * @param itemInfo
	 * @return
	 */
	public HashMap<String, Object> getOptionHashMap(String submitType, JSONObject itemInfo) {
		return getOptionHashMap(submitType, itemInfo, null);
	}
	
	/**
	 * 코드
	 * @param submitType
	 * @param itemInfo
	 * @param addOptionHashMap
	 * @return
	 */
	public HashMap<String, Object> getOptionHashMap(String submitType, JSONObject itemInfo, HashMap<String, Object> addOptionHashMap) {
		// 코드
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		HashMap<String, Object> optnHashMap = (addOptionHashMap != null)?CodeHelper.getItemOptnHashMap(items, itemOrder, addOptionHashMap):CodeHelper.getItemOptnHashMap(items, itemOrder);
		return  optnHashMap;
	}
	
	public String getStatsViewPath(boolean isExceldown, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject moduleItem = attrVO.getModuleItem();
		JSONObject itemInfo = JSONObjectUtil.getJSONObject(moduleItem, attrVO.getModuleId() + "_item_info");
		DataForm queryString = attrVO.getQueryString();
		JSONObject crtMenu = attrVO.getCrtMenu();

		RbsPaginationInfo paginationInfo = null;
		if(!isExceldown) {
			// 1. 페이지정보 setting
			paginationInfo = new RbsPaginationInfo();
			int listUnit = ModuleUtil.getSettingValue(true, "dset_list_count", crtMenu, settingInfo, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = ModuleUtil.getSettingValue(true, "dset_list_block", crtMenu, settingInfo, propertiesService.getInt("PAGE_SIZE"));	
			
			String pageName = JSONObjectUtil.getString(settingInfo, "page_name");
			int page = StringUtil.getInt(request.getParameter(pageName), 1);				// 현재 페이지 index
	
			paginationInfo.setUnitBeginCount(listUnit);
			paginationInfo.setUnitEndCount(listMaxUnit);
			paginationInfo.setUnitStep(listUnitStep);
			paginationInfo.setCurrentPageNo(page);
			paginationInfo.setRecordCountPerPage(pageUnit);
			paginationInfo.setPageSize(pageSize);
		}
		// 2. DB
		int totalCount = 0;
		List<?> list = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 2.1 검색조건

		// 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");

		// 항목설정으로 검색조건 setting
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
		
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		
		// 탭코드
		String cateTabItemId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");
		String cateTabColumnId = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "column_id");
		String cateTabMasterCode = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "master_code");
		int cateTabObjectType = JSONObjectUtil.getInt(settingInfo, "dset_cate_tab_object_type");
		// 검색 코드
		HashMap<String, Object> searchOptnHashMap = CodeHelper.getItemOptnSearchHashMap(listSearch, cateTabItemId, cateTabColumnId, cateTabObjectType, cateTabMasterCode);
		// 목록 코드
		HashMap<String, Object> optnHashMap = getOptionHashMap(submitType, itemInfo, searchOptnHashMap);//CodeHelper.getItemOptnHashMap(items, itemOrder, searchOptnHashMap);
		// 탭 검색 조건
		List<DTForm> cateTabSearchList = ModuleUtil.getCateTabSearchList(settingInfo, items, queryString, optnHashMap);
		if(cateTabSearchList != null){
			searchList.addAll(cateTabSearchList);
		}

		param.put("searchList", searchList);

		// 2.2 목록수
		totalCount = mlLogService.getTotalCount(param);
		if(!isExceldown) {
			paginationInfo.setTotalRecordCount(totalCount);
	    	if(totalCount > 0) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    		
	    		// 정렬컬럼 setting
	    		param.put("dsetOrderField", JSONObjectUtil.getString(settingInfo, "dset_order_field"));
	    		
	    		// 2.3 목록
	    		list = mlLogService.getList(param);
	    	}
		} else {
			// 엑셀다운로드시 사용하는 목록
			int listMaxUnit = RbsProperties.getPropertyInt("Globals.exceldown.maximum.listcnt", 1000);
    		
    		// 목록수 제한
    		if(listMaxUnit < totalCount) {
    			model.addAttribute("message", MessageUtil.getAlert(MessageFormat.format(rbsMessageSource.getMessage("message.log.excel.limitCnt.noDownload"), listMaxUnit)));
    			return RbsProperties.getProperty("Globals.fail.path");
    		}
    		
			param.put("firstIndex", 0);
			param.put("lastIndex", listMaxUnit);
			param.put("recordCountPerPage", listMaxUnit);
    		
    		// 정렬컬럼 setting
    		param.put("dsetOrderField", JSONObjectUtil.getString(settingInfo, "dset_order_field"));
    		
    		// 2.3 목록
    		list = mlLogService.getList(param);
        	model.addAttribute("totalCount", totalCount);												// 목록수
		}

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", searchOptnHashMap);									// 항목코드
		model.addAttribute("optnHashMap", optnHashMap);
		model.addAttribute("queryString", queryString);
    	model.addAttribute("itemInfo", itemInfo);
    	attrVO.setItemInfo(itemInfo);
    	
    	return null;
	}
	
	@RequestMapping("/mlLogList.do")
	public String logList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String viewPath = getStatsViewPath(false, attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/mlLogList");
	}

	/**
	 * 엑셀 다운로드
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mlLogExcel.do")
	public String excelList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String viewPath = getStatsViewPath(true, attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
    	
		return getViewPath("/mlLogExcel");
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
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "mlLogList.do");


		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String excelDownName = "mlLogExcel.do";
		String excelDownUrl = excelDownName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			excelDownUrl += baseQueryString;
		}

		request.setAttribute("URL_EXCELDOWN", excelDownUrl);
	}
}
