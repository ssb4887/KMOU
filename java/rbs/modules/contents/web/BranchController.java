package rbs.modules.contents.web;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rbs.modules.contents.service.BranchService;

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
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;

@Controller
@ModuleMapping(moduleId="contents", confSModule="branch")
@RequestMapping(value={"/{admSiteId}/menuContents/{usrSiteId}/contents", "/{admSiteId}/contents"}, params="contCd")
public class BranchController extends BranchCommonController{
	
	@Resource(name = "branchService")
	private BranchService branchService;
	
	/**
	 * 콘텐츠 정보
	 * @param lang
	 * @param contCd
	 * @param attrVO
	 * @param model
	public void setContentsInfo(String lang, String contCd, ModuleAttrVO attrVO, ModelMap model) {
		
		// Contents 정보
		DataMap contDt = null;
		Map<String, Object> contParam = new HashMap<String, Object>();
		List<DTForm> contSearchList = new ArrayList<DTForm>();

		contSearchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		contParam.put("searchList", contSearchList);
		
		contDt = contentsService.getModify(lang, contParam);

		String submitType = "view";
		JSONObject contentsItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject contentsItems = JSONObjectUtil.getJSONObject(contentsItemInfo, "items");
		JSONArray contentsItemOrder = JSONObjectUtil.getJSONArray(contentsItemInfo, submitType + "_order");
		
		model.addAttribute("contDt", contDt);
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(contentsItems, contentsItemOrder));
	}

	@ModuleAuth(name="MNG")
	@RequestMapping("/branchList.do")
	public String list(@RequestParam(value="contCd") String contCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		
		attrVO = getCommonLangAttrVO(attrVO);
		
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		int branchIdx = queryString.getInt("branchIdx");
		
		// Contents 정보
		setContentsInfo(lang, contCd, attrVO, model);
		
		// Branch 목록
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		
		// 2. DB
		List<?> list = null;
		List<?> versionList = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		Map<String, Object> versionParam = new HashMap<String, Object>();
		List<DTForm> versionSearchList = new ArrayList<DTForm>();

		// 2.1 검색조건
		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		versionSearchList.addAll(searchList);
		param.put("searchList", searchList);
		
		// 2.3 목록
		list = branchService.getList(lang, param);
		
		// 버전목록
		if(branchIdx <= 0 && !list.isEmpty()) {
			DataMap listDt = (DataMap)list.get(0);
			branchIdx = StringUtil.getInt(listDt.get("BRANCH_IDX"));
			queryString.put("branchIdx", branchIdx + "");
			attrVO.setQueryString(queryString);
			request.setAttribute("queryString", queryString);
		}
		if(branchIdx > 0) {
			versionSearchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
			versionParam.put("searchList", versionSearchList);
			
			// 2.3 목록
			versionList = contVersionService.getList(lang, versionParam);
		}
    	
    	model.addAttribute("list", list);															// branch 목록
    	model.addAttribute("verList", versionList);													// 버전 목록
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));

		submitType = "list";
		JSONObject versionItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "version_item_info");
		JSONObject versionItems = JSONObjectUtil.getJSONObject(versionItemInfo, "items");
		JSONArray versionItemOrder = JSONObjectUtil.getJSONArray(versionItemInfo, submitType + "_order");
		model.addAttribute("verOptnHashMap", CodeHelper.getItemOptnHashMap(versionItems, versionItemOrder));
    	
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/list");
	}
	 */

	@ModuleAuth(name="MNG")
	@RequestMapping("/branchList.do")
	public String list(@RequestParam(value="contCd") String contCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		
		attrVO = getCommonLangAttrVO(attrVO);
		
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		int branchIdx = queryString.getInt("branchIdx");
		
		// Contents 정보
		setContentsInfo(lang, contCd, attrVO, model);
		
		// Branch 목록
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		
		// 2. DB
		List<?> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		// 2.1 검색조건
		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		param.put("searchList", searchList);
		
		// 2.3 목록
		list = branchService.getList(lang, param);
		
		// 버전목록
		if(branchIdx <= 0 && !list.isEmpty()) {
			DataMap listDt = (DataMap)list.get(0);
			branchIdx = StringUtil.getInt(listDt.get("BRANCH_IDX"));
			queryString.put("branchIdx", branchIdx + "");
			attrVO.setQueryString(queryString);
			request.setAttribute("queryString", queryString);
		}
		if(branchIdx > 0) {
			String reStr = super.setVerList(contCd, branchIdx, lang, JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "version_item_info"), model);
			if(!StringUtil.isEmpty(reStr)) return reStr;
		}
    	
    	model.addAttribute("list", list);															// branch 목록
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
    	
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
	@RequestMapping(value = "/branchInput.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @RequestParam(value="contCd") String contCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		int branchIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(!isModify || branchIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 언어명 setting
		setSearchLangName(lang);

		String submitType = "info";
		JSONObject contentsItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject contentsItems = JSONObjectUtil.getJSONObject(contentsItemInfo, "items");
		JSONArray contentsItemOrder = JSONObjectUtil.getJSONArray(contentsItemInfo, submitType + "_order");
		
		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		param.put("searchList", searchList);
		
		dt = branchService.getModify(lang, param);

		submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("dt", dt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(contentsItems, contentsItemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_BRANCHIDX_MODIFYPROC"));
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
	@RequestMapping(method=RequestMethod.POST, value = "/branchInputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @RequestParam(value="contCd") String contCd, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		int branchIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(!isModify || branchIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// DB
    	int result = branchService.update(lang, contCd, branchIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_BRANCHLIST") + "\");"));
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
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/branchInput.do")
	public String input(@RequestParam(value="contCd") String contCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");

		// 언어명 setting
		setSearchLangName(lang);

		// Contents 정보
		DataMap contDt = null;
		Map<String, Object> contParam = new HashMap<String, Object>();
		List<DTForm> contSearchList = new ArrayList<DTForm>();

		contSearchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		contParam.put("searchList", contSearchList);
		
		contDt = contentsService.getModify(lang, contParam);

		String submitType = "info";
		JSONObject contentsItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject contentsItems = JSONObjectUtil.getJSONObject(contentsItemInfo, "items");
		JSONArray contentsItemOrder = JSONObjectUtil.getJSONArray(contentsItemInfo, submitType + "_order");
		
		
		submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		model.addAttribute("dt", contDt);
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(contentsItems, contentsItemOrder));
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_BRANCHINPUTPROC"));
		return getViewPath("/input");
	}

	/**
	 * 등록처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/branchInputProc.do")
	public String inputProc(@RequestParam(value="contCd") String contCd, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 2. DB
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// DB
    	int result = branchService.insert(contCd, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_BRANCHLIST") + "\");"));
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
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/branchDeleteProc.do", params="select")
	public String delete(@RequestParam(value="contCd") String contCd, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		// DB
		int result = branchService.delete(contCd, deleteIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.delete"), "top.location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	 */

	/**
	 * 삭제
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/branchDeleteProc.do")
	public String delete(@RequestParam(value="contCd") String contCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		String mstCd = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));
		if(StringUtil.isEmpty(mstCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		String[] deleteIdxs = {mstCd};
		// DB
		int result = branchService.delete(contCd, deleteIdxs, request.getRemoteAddr());
    	
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
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/branchRestoreProc.do", params="select")
	public String restore(@RequestParam(value="contCd") String contCd, @RequestParam(value="select") String[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		// DB
		int result = branchService.restore(contCd, restoreIdxs, request.getRemoteAddr());
    	
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
	@RequestMapping(method=RequestMethod.POST, value = "/branchCdeleteProc.do", params="select")
	public String cdelete(@RequestParam(value="contCd") String contCd, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();

		// DB
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		int result = branchService.cdelete(attrVO.getUploadModulePath(), contCd, deleteIdxs, items, itemOrder);
    	
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
	@RequestMapping(value = "/branchDeleteList.do")
	public String deleteList(@RequestParam(value="contCd") String contCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
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
    	totalCount = branchService.getDeleteCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = branchService.getDeleteList(param);
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

	/**
	 * 콘텐츠 경로
	 * @param attrVO
	 */
	public String[] fn_getContentsParams(ModuleAttrVO attrVO) {	
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject moduleItem = attrVO.getModuleItem();
		JSONObject itemInfo = JSONObjectUtil.getJSONObject(moduleItem, "item_info");
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "setting_info");
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "list.do");
		//PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);
		
		return PathUtil.getUseLangTotalParams(baseParams, null, searchParams, null, pageName, idxName);
	}
	
	/**
	 * 경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		// contents 검색 항목
		/*
		JSONObject moduleItem = attrVO.getModuleItem();
		JSONObject contentsItemInfo = JSONObjectUtil.getJSONObject(moduleItem, "item_info");
		String[] contentsSearchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(contentsItemInfo, "list_search"));
		*/
		// 콘텐츠 경로
		//fn_setContentsPath(attrVO);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		//String[] baseParams = StringUtil.getStringArray(request.getAttribute("totalParams"));
		String[] baseParams = fn_getContentsParams(attrVO);
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");											// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");										// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));	// 목록 검색 항목
		//PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);

		String listName = "branchList.do";
		String inputName = "branchInput.do";
		String inputProcName = "branchInputProc.do";
		String deleteProcName = "branchDeleteProc.do";
		String deleteListName = "branchDeleteList.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, "BRANCH", null, pageName, idxName
				, listName, null, inputName, inputProcName
				, deleteProcName, deleteListName);
		
		fn_setAddCommonPath(request);
	}
	
	/**
	 * 코드관리 경로
	 * @param request
	 */
	public void fn_setAddCommonPath(HttpServletRequest request) {		
		
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String versionInputName = "verInput.do";
		String versionDeleteProcName = "verDeleteProc.do";
		String versionDeleteListName = "verDeleteList.do";
		String workInputName = "workInput.do";
		String workViewName = "workView.do";
		String versionInputUrl = versionInputName;
		String versionDeleteProcUrl = versionDeleteProcName;
		String versionDeleteListUrl = versionDeleteListName;
		String workInputUrl = workInputName;
		String workViewUrl = workViewName;
		if(!StringUtil.isEmpty(allQueryString)) {
			versionInputUrl += allQueryString;
			workInputUrl += allQueryString;
			workViewUrl += allQueryString;
			versionDeleteProcUrl += allQueryString;
			versionDeleteListUrl += allQueryString;
		}

		request.setAttribute("URL_VERSIONINPUT", versionInputUrl);
		request.setAttribute("URL_VERSIONDELETEPROC", versionDeleteProcUrl);
		request.setAttribute("URL_VERSIONDELETE_LIST", versionDeleteListUrl);
		request.setAttribute("URL_WORKINPUT", workInputUrl);
		request.setAttribute("URL_WORKVIEW", workViewUrl);
	}
	
	/**
	 * 휴지통 경로
	 */
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");								// 목록 페이징  key
		String[] baseParams = StringUtil.addStringToArray(this.baseParams, "contCd");						// 기본 parameter

		// 항목 설정 정보
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		//PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
		if(StringUtil.isEmpty(pageName)) pageName = "page";
		String listName = "branchDeleteList.do";
		String restoreProcName = "branchRestoreProc.do";
		String cdeleteProcName = "branchCdeleteProc.do";
		
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, null, pageName, listName, restoreProcName, cdeleteProcName);
	
	}
}
