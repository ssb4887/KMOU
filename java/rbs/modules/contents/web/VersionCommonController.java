package rbs.modules.contents.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;

import rbs.modules.contents.service.BranchService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;

public abstract class VersionCommonController extends BranchCommonController{
	
	@Resource(name = "branchService")
	protected BranchService branchService;
	
	/**
	 * 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String input(String mode, String contCd, int branchIdx, String lang, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		int verIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(branchIdx <= 0 || !isModify || verIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 언어명 setting
		setSearchLangName(lang);
		
		String submitType = "info";
		JSONObject contentsItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject contentsItems = JSONObjectUtil.getJSONObject(contentsItemInfo, "items");
		JSONArray contentsItemOrder = JSONObjectUtil.getJSONArray(contentsItemInfo, submitType + "_order");

		JSONObject branchItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "branch_item_info");
		JSONObject branchItems = JSONObjectUtil.getJSONObject(branchItemInfo, "items");
		JSONArray branchItemOrder = JSONObjectUtil.getJSONArray(branchItemInfo, submitType + "_order");
		
		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("A.ver_IDX", verIdx));
		param.put("searchList", searchList);
		
		dt = contVersionService.getModify(lang, param);

		submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("dt", dt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(contentsItems, contentsItemOrder));
		model.addAttribute("branchOptnHashMap", CodeHelper.getItemOptnHashMap(branchItems, branchItemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_VERSIONIDX_MODIFYPROC"));
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
	public String inputProc(String mode, String contCd, int branchIdx, String lang, ParamForm parameterMap, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		int verIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(branchIdx <= 0 || !isModify || verIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// DB
    	int result = contVersionService.update(lang, contCd, branchIdx, verIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_VERSIONLIST") + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String input(String contCd, int branchIdx, String lang, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		if(branchIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 언어명 setting
		setSearchLangName(lang);

		// Contents 정보
		DataMap branchDt = null;
		Map<String, Object> branchParam = new HashMap<String, Object>();
		List<DTForm> branchSearchList = new ArrayList<DTForm>();

		branchSearchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		branchSearchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		branchParam.put("searchList", branchSearchList);
		
		branchDt = branchService.getModify(lang, branchParam);
		
		List<Object> copyVerList = contVersionService.getCopyVerList(lang, branchParam);
		

		String submitType = "info";
		JSONObject contentsItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject contentsItems = JSONObjectUtil.getJSONObject(contentsItemInfo, "items");
		JSONArray contentsItemOrder = JSONObjectUtil.getJSONArray(contentsItemInfo, submitType + "_order");

		JSONObject branchItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "branch_item_info");
		JSONObject branchItems = JSONObjectUtil.getJSONObject(branchItemInfo, "items");
		JSONArray branchItemOrder = JSONObjectUtil.getJSONArray(branchItemInfo, submitType + "_order");
		
		
		submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		model.addAttribute("dt", branchDt);
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(contentsItems, contentsItemOrder));
		model.addAttribute("branchOptnHashMap", CodeHelper.getItemOptnHashMap(branchItems, branchItemOrder));
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);
		if(copyVerList != null) {
			if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
			optnHashMap.put("copyVerIdx", copyVerList);
		}
		model.addAttribute("optnHashMap", optnHashMap);
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_VERSIONINPUTPROC"));
		return getViewPath("/input");
	}

	/**
	 * 등록처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String inputProc(String contCd, int branchIdx, String lang, ParamForm parameterMap, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		if(branchIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// DB
    	int result = contVersionService.insert(attrVO.getUploadModulePath(), lang, contCd, branchIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder, attrVO.getModuleItem());
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_VERSIONLIST") + "\");"));
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
	public String delete(String contCd, int branchIdx, int[] deleteIdxs, String lang, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		if(deleteIdxs == null) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// DB
		int result = contVersionService.delete(lang, contCd, branchIdx, deleteIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.delete"), "top.location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
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
	public String delete(String contCd, int branchIdx, String lang, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		int verIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));
		
		if(verIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		int[] deleteIdxs = {verIdx};
		// DB
		int result = contVersionService.delete(lang, contCd, branchIdx, deleteIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.delete"), "top.location.reload();"));
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
	public String restore(String contCd, int branchIdx, int[] restoreIdxs, String lang, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		if(restoreIdxs == null) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// DB
		int result = contVersionService.restore(lang, contCd, branchIdx, restoreIdxs, request.getRemoteAddr());
    	
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
	public String cdelete(String contCd, int branchIdx, int[] deleteIdxs, String lang, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();

		// DB
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		int result = contVersionService.cdelete(attrVO.getUploadModulePath(), lang, contCd, branchIdx, deleteIdxs, items, itemOrder);
    	
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
	public String deleteList(String contCd, int branchIdx, String lang, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String listSearchId = "list_search";		// 검색설정
    	
    	// 1. 페이지정보 setting
		int listUnit = propertiesService.getInt("DEFAULT_LIST_UNIT");	// 페이지당 목록 수
		int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
		int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
		
		int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
		if(pageUnit == 0) pageUnit = listUnit;	// 페이지당 목록 수
		int pageSize = propertiesService.getInt("PAGE_SIZE");	// 페이징 크기
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
		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));

		// 2.1 검색조건
		
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.LAST_MODI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = contVersionService.getDeleteCount(lang, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = contVersionService.getDeleteList(lang, param);
    	}
    	
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
    	model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		    	
    	// 3. 경로 setting
    	// 3.1 기본경로
    	fn_setDeleteListPath(attrVO);
    	
		return getViewPath("/deleteList");
	}

	public abstract void fn_setCommonPath(ModuleAttrVO attrVO);
	
	/**
	 * 코드관리 경로
	 * @param request
	 */
	public void fn_setAddCommonPath(HttpServletRequest request) {		
		
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String workName = "workInput.do";
		String workViewName = "workView.do";
		String workUrl = workName;
		String workViewUrl = workViewName;
		if(!StringUtil.isEmpty(allQueryString)) {
			workUrl += allQueryString;
			workViewUrl += allQueryString;
		}

		request.setAttribute("URL_WORKINPUT", workUrl);
		request.setAttribute("URL_WORKVIEW", workViewUrl);
	}
	
	/**
	 * 휴지통 경로
	 */
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");											// 목록 페이징  key
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{"contCd", "branchIdx", "slang"});		// 기본 parameter

		// 항목 설정 정보
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		//PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
		if(StringUtil.isEmpty(pageName)) pageName = "page";
		String listName = "verDeleteList.do";
		String restoreProcName = "verRestoreProc.do";
		String cdeleteProcName = "verCdeleteProc.do";
		
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, null, pageName, listName, restoreProcName, cdeleteProcName);
	
	}
}
