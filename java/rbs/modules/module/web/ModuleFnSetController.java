package rbs.modules.module.web;

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

import rbs.modules.module.service.ModuleFnService;
import rbs.modules.module.service.ModuleService;

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
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@ModuleMapping(moduleId="moduleFn", confModule="module", confSModule="fn")
@RequestMapping("/{admSiteId}/moduleFn")
public class ModuleFnSetController extends ModuleController{

	@Resource(name = "moduleService")
	private ModuleService moduleService;
	
	@Resource(name = "moduleFnService")
	private ModuleFnService moduleFnService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	/**
	 * 설정관리
	 * @param mode
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/setting.do")
	public String setting(@RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.MODULE_ID", moduleId));
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		param.put("searchList", searchList);
		
		dt = moduleFnService.getSettingModify(param);

		// 설정관리 항목정보
		String submitType = "modify";
		String confModule = StringUtil.getString(dt.get("CONF_MODULE"));
		String designType = StringUtil.getString(dt.get("DESIGN_TYPE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		JSONObject settingManagerObject = ModuleUtil.getModuleSettingManagerObject(confModule, null, designType);
		if(JSONObjectUtil.isEmpty(settingManagerObject)) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.setting.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		JSONObject items = JSONObjectUtil.getJSONObject(settingManagerObject, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(settingManagerObject, submitType + "_order");

		// 항목코드 얻기
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);
		int isItemManager = StringUtil.getInt(dt.get("ISITEMMANAGER"));
		if(isItemManager == 1) {
			// 항목관리 하는 모듈인 경우 : 분류탭 항목에 사용할 항목정보목록
			Map<String, Object> itemParam = new HashMap<String, Object>();
			List<DTForm> itemSearchList = new ArrayList<DTForm>();

			itemSearchList.add(new DTForm("A.FN_IDX", mfIdx));
			itemSearchList.add(new DTForm("A.FORMAT_TYPE", 0));
			itemSearchList.add(new DTForm("A.OBJECT_TYPE", 2, ">="));
			itemSearchList.add(new DTForm("A.OBJECT_TYPE", 5, "<="));
			itemParam.put("searchList", itemSearchList);
			itemParam.put("MODULE_ID", moduleId.toUpperCase());
			List<Object> itemOptionList = moduleFnService.getItemOptionList(itemParam);
			
			// 분류탭 항목코드 setting
			if(itemOptionList != null) {
				if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
				optnHashMap.put("_CATE_ITEMS", itemOptionList);
			}
		} else if(StringUtil.isEquals(moduleId, RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_TREBOOK"))){
			// 이북모듈인 경우 : 분류 항목코드 생성해 setting
			/*List<Object> itemOptionList = new ArrayList<Object>();
			DataMap itemDt = new DataMap();
			itemDt.put("OPTION_CODE", "CATEGORY");
			itemDt.put("OPTION_NAME", rbsMessageSource.getMessage("item.setting.category.name"));
			itemOptionList.add(itemDt);*/

			List<Object> itemOptionList = CodeHelper.getClassMstrList();
			// 분류탭 항목코드 setting
			if(itemOptionList != null) {
				if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
				optnHashMap.put("_CATE_ITEMS", itemOptionList);
			}
		} else if(StringUtil.isEquals(moduleId, RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_BUSINESS"))){
			// 사업구조모듈인 경우 : 분류 항목코드 생성해 setting
			List<Object> itemOptionList = CodeHelper.getMstrList();
			// 분류탭 항목코드 setting
			if(itemOptionList != null) {
				if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
				optnHashMap.put("_CATE_ITEMS", itemOptionList);
			}
		}
		
		model.addAttribute("settingOptnHashMap", optnHashMap);
		model.addAttribute("settingItemInfo", settingManagerObject);
			
		model.addAttribute("dt", dt);
		model.addAttribute("submitType", submitType);
		model.addAttribute("setSettingInfo", JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "set_setting_info"));
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
		return getViewPath("/setting");
	}
	


	/**
	 * 설정관리 처리
	 * @param mode
	 * @param moduleId
	 * @param parameterMap
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/settingProc.do")
	public String settingProc(@RequestParam(value="moduleId") String moduleId, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.MODULE_ID", moduleId));
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		param.put("searchList", searchList);
		
		dt = moduleFnService.getSettingModify(param);
		
		String submitType = "modify";
		String confModule = StringUtil.getString(dt.get("CONF_MODULE"));
		String designType = StringUtil.getString(dt.get("DESIGN_TYPE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		JSONObject settingManagerObject = ModuleUtil.getModuleSettingManagerObject(confModule, null, designType);
		if(JSONObjectUtil.isEmpty(settingManagerObject)) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.setting.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		JSONObject items = JSONObjectUtil.getJSONObject(settingManagerObject, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(settingManagerObject, submitType + "proc_order");
		
		// DB
    	int result = moduleFnService.settingUpdate(uploadModulePath, moduleId, mfIdx, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_LIST") + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
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
		//String[] baseParams = StringUtil.addStringToArray(this.baseParams, "moduleId");												// 기본 parameter
		String[] tabBaseParams = {"moduleId"};																		// 기본 parameter
		//PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, idxName, pageName);
		String listName = "list.do";
		String viewName = "view.do";
		String inputName = "setting.do";
		String inputProcName = "settingProc.do";
		String deleteProcName = "deleteProc.do";
		String deleteListName = "deleteList.do";
		String imageName = "image.do";
		String downloadName = "download.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, tabBaseParams, searchParams
				, null, null, pageName, idxName
				, listName, viewName, inputName, inputProcName
				, deleteProcName, deleteListName
				, imageName, downloadName, null);
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setCommonAddPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setCommonAddPath(HttpServletRequest request) {
		String baseQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String moduleDesignListName = "designList.do";
		String moduleDesignListUrl = moduleDesignListName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			moduleDesignListUrl += baseQueryString;
		}

		request.setAttribute("URL_DESIGNLIST", moduleDesignListUrl);
	}
}
