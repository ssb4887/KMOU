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

import rbs.modules.contents.service.ContVersionService;
import rbs.modules.contents.service.ContWorkService;
import rbs.modules.contents.service.ContentsService;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;

public abstract class WorkCommonController extends CommonController{
	
	@Resource(name = "contentsService")
	protected ContentsService contentsService;
	
	@Resource(name = "contVersionService")
	protected ContVersionService contVersionService;
	
	@Resource(name = "contWorkService")
	private ContWorkService contWorkService;
	
	/**
	 * 콘텐츠 정보
	 * @param lang
	 * @param contCd
	 * @param attrVO
	 * @param model
	 */
	public void setBranchInfo(String lang, String contCd, int branchIdx, ModuleAttrVO attrVO, ModelMap model) {
		
		// Branch 정보
		DataMap branchDt = null;
		Map<String, Object> branchParam = new HashMap<String, Object>();
		List<DTForm> branchSearchList = new ArrayList<DTForm>();

		branchSearchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		branchSearchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		branchParam.put("searchList", branchSearchList);
		
		branchDt = contentsService.getModify(lang, branchParam);

		String submitType = "view";
		JSONObject branchItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject branchItems = JSONObjectUtil.getJSONObject(branchItemInfo, "items");
		JSONArray branchItemOrder = JSONObjectUtil.getJSONArray(branchItemInfo, submitType + "_order");
		
		model.addAttribute("branchDt", branchDt);
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(branchItems, branchItemOrder));
	}
	
	/**
	 * 작업
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String view(String contCd, int branchIdx, int verIdx, String lang, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		if(branchIdx <= 0 || verIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 언어 setting
		setSearchLangName(lang);

		String submitType = "info";
		JSONObject contentsItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject contentsItems = JSONObjectUtil.getJSONObject(contentsItemInfo, "items");
		JSONArray contentsItemOrder = JSONObjectUtil.getJSONArray(contentsItemInfo, submitType + "_order");

		JSONObject branchItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "branch_item_info");
		JSONObject branchItems = JSONObjectUtil.getJSONObject(branchItemInfo, "items");
		JSONArray branchItemOrder = JSONObjectUtil.getJSONArray(branchItemInfo, submitType + "_order");
		
		// 2. DB
		DataMap verDt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("A.ver_IDX", verIdx));
		param.put("searchList", searchList);
		
		verDt = contVersionService.getModify(lang, param);
		
		String contentsType = StringUtil.getString(verDt.get("CONTENTS_TYPE"));

		DataMap dt = contWorkService.getModify(contentsType, lang, param);

		submitType = "view";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("verDt", verDt);
		model.addAttribute("dt", dt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(contentsItems, contentsItemOrder));
		model.addAttribute("branchOptnHashMap", CodeHelper.getItemOptnHashMap(branchItems, branchItemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/workView" + contentsType);
	}
	
	/**
	 * 작업
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String input(String contCd, int branchIdx, int verIdx, String lang, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		if(branchIdx <= 0 || verIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 언어 setting
		attrVO = getCommonLangAttrVO(attrVO);
		//setSearchLangName(attrVO);

		String submitType = "info";
		JSONObject contentsItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject contentsItems = JSONObjectUtil.getJSONObject(contentsItemInfo, "items");
		JSONArray contentsItemOrder = JSONObjectUtil.getJSONArray(contentsItemInfo, submitType + "_order");

		JSONObject branchItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "branch_item_info");
		JSONObject branchItems = JSONObjectUtil.getJSONObject(branchItemInfo, "items");
		JSONArray branchItemOrder = JSONObjectUtil.getJSONArray(branchItemInfo, submitType + "_order");
		
		// 2. DB
		DataMap verDt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("A.ver_IDX", verIdx));
		param.put("searchList", searchList);
		
		verDt = contVersionService.getModify(lang, param);
		
		if(verDt == null) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		String contentsType = StringUtil.getString(verDt.get("CONTENTS_TYPE"));

		DataMap dt = contWorkService.getModify(contentsType, lang, param);

		submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("verDt", verDt);
		model.addAttribute("dt", dt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(contentsItems, contentsItemOrder));
		model.addAttribute("branchOptnHashMap", CodeHelper.getItemOptnHashMap(branchItems, branchItemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_WORKIDX_MODIFYPROC"));
		return getViewPath("/workInput" + contentsType);
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
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		int verIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(branchIdx <= 0 || !isModify || verIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		
		// DB
    	int result = contWorkService.update(lang, contCd, branchIdx, verIdx, request.getRemoteAddr(), parameterMap, settingInfo, attrVO.getModuleItem());
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	//fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procReload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 수정처리
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String applyProc(String contCd, int branchIdx, String lang, ParamForm parameterMap, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		int verIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(branchIdx <= 0 || verIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		
		// DB
    	int result = contWorkService.apply(lang, contCd, branchIdx, verIdx, request.getRemoteAddr(), parameterMap, settingInfo, attrVO.getModuleItem());
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
    		String mfStr = null;
    		int workType = StringUtil.getInt(parameterMap.get("workType"));
    		if(workType == 10) mfStr = "fn_procAction(\"" + request.getAttribute("URL_WORKLIST") + "\");";
    		else mfStr = "fn_procReload();";
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), mfStr));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
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
	public abstract void fn_setCommonPath(ModuleAttrVO attrVO);
	
	/**
	 * 코드관리 경로
	 * @param request
	 */
	public void fn_setAddCommonPath(HttpServletRequest request) {		
		
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String workName = "workInput.do";
		String workUrl = workName;
		if(!StringUtil.isEmpty(allQueryString)) {
			workUrl += allQueryString;
		}

		request.setAttribute("URL_WORKINPUT", workUrl);
	}
}
