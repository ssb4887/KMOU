package rbs.modules.module.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import rbs.modules.module.service.ModuleFnItemSService;
import rbs.modules.module.service.ModuleFnItemService;
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
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
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
public class ModuleFnItemController extends ModuleController{

	@Resource(name = "moduleService")
	private ModuleService moduleService;
	
	@Resource(name = "moduleFnService")
	private ModuleFnService moduleFnService;

	@Resource(name = "moduleFnItemService")
	private ModuleFnItemService moduleFnItemService;

	@Resource(name = "moduleFnItemSService")
	private ModuleFnItemSService moduleFnItemSService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	/**
	 * 목록조회
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping("/itemList.do")
	public String list(@RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		DataForm queryString = attrVO.getQueryString();
		
		// 1. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		// 1.2 필수 parameter 검사
		// 1.2.1 기능일련번호
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		String submitType = "list";
		// 4.1 항목관리파일 항목설정 (해당기능의 항목관리파일의 항목 얻기) 및 속성 setting
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		setItemItemInfo(confModule, submitType, attrVO, request, model, null, 0);

		/*
		// 설정관리 항목정보
		String confModule = StringUtil.getString(dt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		JSONObject itemManagerObject = ModuleUtil.getModuleItemManagerObject(confModule, null);
		if(JSONObjectUtil.isEmpty(itemManagerObject)) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		*/
		
		// 2. DB
		List<?> list = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.FN_IDX", mfIdx));

    	param.put("MODULE_ID", moduleId.toUpperCase());
		param.put("searchList", searchList);
		
		// 2.1 목록
		list = moduleFnItemService.getList(param);
    	
    	model.addAttribute("list", list);												// 목록
    	model.addAttribute("queryString", queryString);
    	JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
    	model.addAttribute("itemSettingInfo", itemSettingInfo);
		    	
    	// 3. 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/item/list");
	}
	
	/**
	 * 수정
	 * @param mode
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/itemInput.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 0. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		// 1.2.1 기능일련번호
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		
		// 1.2.2 항목일련번호
		JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
		String itemId = request.getParameter(JSONObjectUtil.getString(itemSettingInfo, "idx_name"));

		if(!isModify || mfIdx <= 0 || StringUtil.isEmpty(itemId)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		searchList.add(new DTForm("A.ITEM_ID", itemId));

    	param.put("MODULE_ID", moduleId.toUpperCase());
		param.put("searchList", searchList);

		// 2.1 상세정보
		dt = moduleFnItemService.getModify(param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		/* 코드 */
		/*
		// 순서
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		param.put("searchList", searchList);
		param.put("MODULE_ID", moduleId.toUpperCase());
		List<?> ordList = moduleFnItemService.getItemOptionList(param);
		
		// 분류
		List<?> masterList = CodeHelper.getMstrList();
*/
		// 4. 항목설정
		String submitType = "modify";
		
		// 4.1 항목관리파일 항목설정 (해당기능의 항목관리파일의 항목 얻기) 및 속성 setting
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		setItemItemInfo(confModule, submitType, attrVO, request, model, moduleId, mfIdx);
		
		List langList = CodeHelper.getOptnList("LOCALE");
		// 5. 속성 setting
		model.addAttribute("dt", dt);
		model.addAttribute("langList", langList);
		model.addAttribute("submitType", submitType);
		
		String itemTypes = StringUtil.getString(dt.get("ITEM_TYPE"));
		if(!StringUtil.isEmpty(itemTypes)) {
			HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
			List<Object> itemDataList = new ArrayList<Object>();
			String[] itemTypeArr = itemTypes.split(",");
			for(String itemType:itemTypeArr) {
				DataMap dataMap = new DataMap();
				dataMap.put("ITEM_ID", "item_type");
				dataMap.put("ITEM_KEY", itemType);
				itemDataList.add(dataMap);
			}
			resultHashMap.put("item_type", itemDataList);
			model.addAttribute("multiDataHashMap", resultHashMap);
		}
		
		// 6. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 7. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
    	
		return getViewPath("/item/input");
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/itemInput.do")
	public String input(@RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 0. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 1.2 필수 parameter 검사
		// 1.2.1 기능일련번호
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		/*
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		param.put("searchList", searchList);
		param.put("MODULE_ID", moduleId.toUpperCase());
		List<?> ordList = moduleFnItemService.getItemOptionList(param);
		 */
		// 4. 항목설정
		String submitType = "write";
		
		// 4.1 항목관리파일 항목설정 (해당기능의 항목관리파일의 항목 얻기) 및 속성 setting
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		setItemItemInfo(confModule, submitType, attrVO, request, model, moduleId, mfIdx);

		List langList = CodeHelper.getOptnList("LOCALE");
		// 5. 속성 setting
		model.addAttribute("langList", langList);
		model.addAttribute("submitType", submitType);

		// 6. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 7. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
		return getViewPath("/item/input");
	}

	/**
	 * 수정처리
	 * @param mode
	 * @param moduleId
	 * @param parameterMap
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/itemInputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @RequestParam(value="moduleId") String moduleId, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		
		// 0. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		// 1.2.1 기능일련번호
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		
		// 1.2.2 항목일련번호
		JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
		String itemId = request.getParameter(JSONObjectUtil.getString(itemSettingInfo, "idx_name"));

		if(!isModify || mfIdx <= 0 || StringUtil.isEmpty(itemId)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. 항목설정
		String submitType = "modify";
		
		// 4.1 항목관리파일 항목설정 (해당기능의 항목관리파일의 항목 얻기) 및 속성 setting
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		JSONObject itemManagerObject = ModuleUtil.getModuleItemManagerObject(confModule, null);
		JSONObject items = JSONObjectUtil.getJSONObject(itemManagerObject, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemManagerObject, submitType + "proc_order");
		
		// 4. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 4. DB
    	int result = moduleFnItemService.update(uploadModulePath, moduleId, mfIdx, itemId, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 등록처리
	 * @param moduleId
	 * @param parameterMap
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/itemInputProc.do")
	public String inputProc(@RequestParam(value="moduleId") String moduleId, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		
		// 0. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 1.2 필수 parameter 검사
		// 1.2.1 기능일련번호
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 4. 항목설정
		String submitType = "write";
		
		// 4.1 항목관리파일 항목설정 (해당기능의 항목관리파일의 항목 얻기) 및 속성 setting
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		JSONObject itemManagerObject = ModuleUtil.getModuleItemManagerObject(confModule, null);
		JSONObject items = JSONObjectUtil.getJSONObject(itemManagerObject, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemManagerObject, submitType + "proc_order");
		
		// 4. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 4. DB
    	int result = moduleFnItemService.insert(uploadModulePath, moduleId, mfIdx, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -2) {
    		// 중복코드
    		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.itemId.exist.duplicate")));
    		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    	} else if(result == -3) {
    		// 중복컬럼
    		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.columnId.exist.duplicate")));
    		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 순서 수정처리
	 * @param mode
	 * @param moduleId
	 * @param parameterMap
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/itemOrderProc.do")
	public String orderProc(@RequestParam(value="moduleId") String moduleId, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		
		// 0. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 1. 필수 parameter 검사
		// 1.1 기능일련번호
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String ordItemId = StringUtil.getString(parameterMap.get("orderIdx"));
		
		// 1.2.2 항목일련번호
		JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
		String itemId = StringUtil.getString(parameterMap.get(JSONObjectUtil.getString(itemSettingInfo, "idx_name")));

		if(mfIdx <= 0 || StringUtil.isEmpty(itemId) || StringUtil.isEmpty(ordItemId)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. 항목설정
		
		// 4.1 항목관리파일 항목설정 (해당기능의 항목관리파일의 항목 얻기) 및 속성 setting
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;

		// 4. DB
    	int result = moduleFnItemService.update(uploadModulePath, moduleId, mfIdx, itemId, request.getRemoteAddr(), parameterMap, ordItemId);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, "", "fn_procReload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 검색항목 등록
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/itemSearchInput.do")
	public String searchInput(@RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 필수 parameter 검사
		// 1.2.1 기능일련번호
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		
		// 1.2.2 항목일련번호
		//JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 1. 기능설정정보
		DataMap settingDt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.MODULE_ID", moduleId));
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		param.put("searchList", searchList);
		
		settingDt = moduleFnService.getSettingModify(param);

		int useNotice = StringUtil.getInt(settingDt.get("USE_NOTICE"));
		int useQna = StringUtil.getInt(settingDt.get("USE_QNA"));
		int useSecret = StringUtil.getInt(settingDt.get("USE_SECRET"));
		int useSms = StringUtil.getInt(settingDt.get("USE_SMS"));
		
		// 2. 검색항목정보
		List<Object> defaultIItemList = null;				// 항목검색에 사용될 기본항목정보
		List<Object> searchIItemList = null;				// 항목검색 항목정보
		List<Object> defaultSItemList = null;				// 선택검색에 사용될 기본항목정보
		List<Object> searchSItemList = null;				// 선택검색 항목정보
		
		// 2.1 항목검색
		// 2.1.1 기본항목
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		if(useNotice == 0) searchList.add(new DTForm("A.ITEM_ID", "notice", "<>"));
		if(useQna == 0) searchList.add(new DTForm("A.ITEM_ID", "replyState", "<>"));
		if(useSecret == 0) searchList.add(new DTForm("A.ITEM_ID", "secret", "<>"));
		if(useSms == 0) searchList.add(new DTForm("A.ITEM_ID", "smsGubun", "<>"));
		param.put("searchList", searchList);
    	param.put("MODULE_ID", moduleId.toUpperCase());
		defaultIItemList = moduleFnItemSService.getDefaultSearchIList(param);
		
		// 2.1.2 검색항목
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		param.put("searchList", searchList);
    	param.put("MODULE_ID", moduleId.toUpperCase());
    	searchIItemList = moduleFnItemSService.getSearchIList(param);
		
		// 2.2 선택입력검색
		// 2.2.1 기본항목
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		param.put("searchList", searchList);
    	param.put("MODULE_ID", moduleId.toUpperCase());
		defaultSItemList = moduleFnItemSService.getDefaultSearchSList(param);
		
		// 2.2.2 검색항목
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		param.put("searchList", searchList);
    	param.put("MODULE_ID", moduleId.toUpperCase());
    	searchSItemList = moduleFnItemSService.getSearchSList(param);
		
		
		// 5. 속성 setting
		model.addAttribute("defaultIItemList", defaultIItemList);
		model.addAttribute("defaultSItemList", defaultSItemList);
		model.addAttribute("searchIItemList", searchIItemList);
		model.addAttribute("searchSItemList", searchSItemList);
		
		// 6. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 7. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_SEARCHINPUTPROC"));
    	JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
    	model.addAttribute("itemSettingInfo", itemSettingInfo);
    	
		return getViewPath("/item/searchInput");
	}

	/**
	 * 검색항목등록처리
	 * @param mode
	 * @param moduleId
	 * @param parameterMap
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/itemSearchInputProc.do")
	public String searchInputProc(@RequestParam(value="moduleId") String moduleId, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 1. 필수 parameter 검사
		// 1.2.1 기능일련번호
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		
		// 1.2.2 항목일련번호
		//JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");

		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 4. DB
    	int result = moduleFnItemSService.insert( moduleId, mfIdx, request.getRemoteAddr(), parameterMap);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 삭제처리
	 * @param moduleId
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/itemDeleteProc.do", params="select")
	public String delete(@RequestParam(value="moduleId") String moduleId, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 0. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		// 1. DB
		int result = moduleFnItemService.delete(moduleId, mfIdx, deleteIdxs, request.getRemoteAddr());
    	
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
	 * 복원처리
	 * @param moduleId
	 * @param restoreIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/itemRestoreProc.do", params="select")
	public String restore(@RequestParam(value="moduleId") String moduleId, @RequestParam(value="select") String[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 0. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 1. DB
		int result = moduleFnItemService.restore(moduleId, mfIdx, restoreIdxs, request.getRemoteAddr());
    	
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
	 * 완전삭제처리
	 * @param moduleId
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="CDT")
	@RequestMapping(method=RequestMethod.POST, value = "/itemCdeleteProc.do", params="select")
	public String cdelete(@RequestParam(value="moduleId") String moduleId, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		
		// 0. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		// 1. 항목설정
		String submitType = "write";
		
		// 1.1 항목관리파일 항목설정 (해당기능의 항목관리파일의 항목 얻기) 및 속성 setting
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		JSONObject itemManagerObject = ModuleUtil.getModuleItemManagerObject(confModule, null);
		JSONObject items = JSONObjectUtil.getJSONObject(itemManagerObject, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemManagerObject, submitType + "_order");
		
		// 2. DB
		int result = moduleFnItemService.cdelete(uploadModulePath, moduleId, mfIdx, deleteIdxs, request.getRemoteAddr(), items, itemOrder);
    	
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
	 * 삭제목록
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/itemDeleteList.do")
	public String deleteList(@RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		// 1. 모듈 상세 정보 : 항목관리 여부 체크
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 항목관리 하지 않는 모듈인 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.item.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		// 1.2 필수 parameter 검사
		// 1.2.1 기능일련번호
		int mfIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(mfIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		String submitType = "list";
		// 4.1 항목관리파일 항목설정 (해당기능의 항목관리파일의 항목 얻기) 및 속성 setting
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		setItemItemInfo(confModule, submitType, attrVO, request, model, null, 0);
		
    	// 1. 페이지정보 setting
		int listUnit = propertiesService.getInt("DEFAULT_LIST_UNIT");	// 페이지당 목록 수
		int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
		int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
		
		int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
		if(pageUnit == 0) pageUnit = listUnit;	// 페이지당 목록 수
		int pageSize = propertiesService.getInt("PAGE_SIZE");	// 페이징 크기
		int page = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(itemSettingInfo, "page_name")), 1);				// 현재 페이지 index

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
		searchList.add(new DTForm("A.FN_IDX", mfIdx));
		
		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, "list_search");
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.LAST_MODI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);
    	param.put("MODULE_ID", moduleId.toUpperCase());

		// 2.2 목록수
    	totalCount = moduleFnItemService.getDeleteCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = moduleFnItemService.getDeleteList(param);
    	}

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드
    	model.addAttribute("itemSettingInfo", itemSettingInfo);
		    	
    	// 4. 휴지통 경로
    	fn_setDeleteListPath(attrVO);
    	
		return getViewPath("/item/deleteList");
	}
	
	/**
	 * 모듈 상세 정보 : 항목관리 여부 체크
	 * @param moduleId
	 * @return
	 */
	public DataMap getModuleInfo(String moduleId) {

		DataMap dt = null;
		Map<String, Object> moduleParam = new HashMap<String, Object>();
		List<DTForm> moduleSearchList = new ArrayList<DTForm>();

		moduleSearchList.add(new DTForm("A.MODULE_ID", moduleId));
		moduleParam.put("searchList", moduleSearchList);
		
		dt = moduleService.getManageView(moduleParam);
		int isItemManager = StringUtil.getInt(dt.get("ISITEMMANAGER"));
		
		if(isItemManager != 1) {
			// 항목관리 하지 않는 모듈인 경우
			return null;
		}
		return dt;
	}
	
	/**
	 * 설정관리 항목정보 (setting_manager_info.json)
	 * @param confModule
	 * @param submitType
	 * @param request
	 * @param model
	 */
	private void setItemItemInfo(String confModule, String submitType, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, String moduleId, int mfIdx) {
		JSONObject itemManagerObject = ModuleUtil.getModuleItemManagerObject(confModule, null);
		
		if(!JSONObjectUtil.isEmpty(itemManagerObject)) {
			JSONObject items = JSONObjectUtil.getJSONObject(itemManagerObject, "items");
			JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemManagerObject, submitType + "_order");

			HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);

			if(!StringUtil.isEmpty(moduleId) && mfIdx > 0) {
				// 순서
				String itemId = "order_idx";
				JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
				boolean isClosed = ModuleUtil.isClosed(itemOrder, item);
				if(!isClosed) {
					Map<String, Object> param = new HashMap<String, Object>();
					List<DTForm> searchList = new ArrayList<DTForm>();
					searchList.add(new DTForm("A.FN_IDX", mfIdx));
					param.put("searchList", searchList);
					param.put("MODULE_ID", moduleId.toUpperCase());
					List<?> ordList = moduleFnItemService.getItemOptionList(param);
					if(ordList != null) {
						if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
						optnHashMap.put("order_idx", ordList);
					}
				}
				// 분류 / 3차분류
				itemId = "master_code";
				item = JSONObjectUtil.getJSONObject(items, itemId);
				isClosed = ModuleUtil.isClosed(itemOrder, item);
				if(!isClosed) {
					// 1차 분류
					List<?> masterList = CodeHelper.getMstrList();
					if(masterList != null) {
						if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
						optnHashMap.put("master_code", masterList);
					}
					
					// 3차 분류
					List classMasterList = CodeHelper.getClassMstrList();
					if(classMasterList != null) {
						if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
						optnHashMap.put("class_master_code", classMasterList);
					}
				}
			}
			model.addAttribute("optnHashMap", optnHashMap);
			request.setAttribute("itemInfo", itemManagerObject);
		}
		
		JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
    	model.addAttribute("itemSettingInfo", itemSettingInfo);
	}
	
	/**
	 * 기능 경로
	 * @param attrVO
	 */
	public String[] fn_getFnParams(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		//DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		String[] tabBaseParams = {"moduleId"};	
		//PathUtil.fn_setListPath(queryString, baseParams, tabBaseParams, searchParams, null, null, null, pageName, "list.do");
		
		return PathUtil.getTotalParams(baseParams, tabBaseParams, searchParams, null, null, pageName, idxName);
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		/*JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String fnIdxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String fnPageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));	// 목록 검색 항목
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{"moduleId", fnIdxName, fnPageName});		// 기본 parameter
		baseParams = StringUtil.insertStringArrays(baseParams, searchParams);*/
		DataForm queryString = attrVO.getQueryString();
		String[] baseParams = fn_getFnParams(attrVO);
		
		JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
		String idxName = JSONObjectUtil.getString(itemSettingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(itemSettingInfo, "page_name");		// 목록 페이징  key
		String listName = "itemList.do";
		String viewName = "itemView.do";
		String inputName = "itemInput.do";
		String inputProcName = "itemInputProc.do";
		String deleteProcName = "itemDeleteProc.do";
		String deleteListName = "itemDeleteList.do";
		String imageName = "itemImage.do";
		String downloadName = "itemDownload.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, null
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
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String itemSearchInputName = "itemSearchInput.do";				// 검색항목등록
		String itemSearchInputProcName = "itemSearchInputProc.do";		// 검색항목등록처리
		String itemOrderProcName = "itemOrderProc.do";					// 순서 수정
		String itemSearchInputUrl = itemSearchInputName;
		String itemSearchInputProcUrl = itemSearchInputProcName;
		String itemOrderProcUrl = itemOrderProcName;
		if(!StringUtil.isEmpty(allQueryString)) {
			itemSearchInputUrl += allQueryString;
			itemSearchInputProcUrl += allQueryString;
			itemOrderProcUrl += allQueryString;
		}

		request.setAttribute("URL_SEARCHINPUT", itemSearchInputUrl);
		request.setAttribute("URL_SEARCHINPUTPROC", itemSearchInputProcUrl);
		request.setAttribute("URL_ORDERPROC", itemOrderProcUrl);
	}

	/**
	 * 휴지통 경로
	 */
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String fnIdxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String fnPageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));	// 목록 검색 항목
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{"moduleId", fnIdxName, fnPageName});		// 기본 parameter
		baseParams = StringUtil.insertStringArrays(baseParams, searchParams);
		
		JSONObject itemSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "item_setting_info");
		String pageName = JSONObjectUtil.getString(itemSettingInfo, "page_name");		// 목록 페이징  key

		// 항목 설정 정보
		searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		//PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
		
		String listName = "itemDeleteList.do";
		String restoreProcName = "itemRestoreProc.do";
		String cdeleteProcName = "itemCdeleteProc.do";
		
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, null, pageName, listName, restoreProcName, cdeleteProcName);
	}
}
