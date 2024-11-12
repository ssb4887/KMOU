package rbs.modules.module.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.WebsiteVO;
import rbs.modules.module.service.ModuleAuthService;

@Controller
@ModuleMapping(moduleId="moduleAuth", confModule="module", confSModule="auth")
@RequestMapping("/{admSiteId}/moduleAuth")
public class ModuleAuthController extends CommonController{
	
	@Resource(name = "moduleAuthService")
	private ModuleAuthService moduleAuthService;
	
	/**
	 * 목록조회
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 */
	@ModuleAuth(name="LST")
	@RequestMapping("/list.do")
	public String list(@RequestParam(value="moduleId", required=false) String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		//이동근 - 관리자 페이지에 보여줄 사용자 페이지 가져오기
		WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		
		String urSiteMode = RbsProperties.getProperty("Globals.site.mode.usr");										// 사용자 모드
		JSONObject urSiteObject = MenuUtil.getSiteObject("/" + urSiteMode + "/" + siteId, false);					// 사이트, 메뉴정보 전체
		JSONArray usrSiteMenuList = null;
		JSONObject usrSiteMenus = null;
    	usrSiteMenuList = JSONObjectUtil.getJSONArray(urSiteObject, "menu-list");
	    usrSiteMenus = JSONObjectUtil.getJSONObject(urSiteObject, "menus");
		model.addAttribute("usrSiteMenuList", usrSiteMenuList);
		model.addAttribute("usrSiteMenus", usrSiteMenus);
		
		/* 좌측 모듈(기능) */
		// 모듈 목록
		List<Object> moduleList = getAuthModuleList();
		if(moduleList == null) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 선택 모듈의 기능 목록 얻기
		if(StringUtil.isEmpty(moduleId)) {
			// 선택 모듈이 없는 경우 : 모듈 목록의 첫번째 얻기
			DataMap moduleDt = (DataMap)moduleList.get(0);
			moduleId = StringUtil.getString(moduleDt.get("OPTION_CODE"));
			queryString.put("moduleId", moduleId);
		}

		// 선택 모듈 상세 정보
		DataMap moduleDt = getModuleInfo(moduleId);
		
		/* 선택 모듈의 기능 목록 */
		// 1. 페이지정보 setting
		int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
		int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
		int listUnitStep = listUnit;																							// 페이지당 목록 수 증가값
		
		int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
		if(pageUnit == 0) pageUnit = listUnit;																					// 페이지당 목록 수
		int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));			// 페이징 크기
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");
		int page = StringUtil.getInt(request.getParameter(pageName), 1);														// 현재 페이지 index															// 현재 페이지 index

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
		searchList.add(new DTForm("A.MODULE_ID", moduleId));

		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);
		
		// 2.2 목록수
    	totalCount = moduleAuthService.getTotalCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = moduleAuthService.getList(param);
    	}
    	
    	model.addAttribute("paginationInfo", paginationInfo);							// 페이징정보
    	model.addAttribute("list", list);												// 목록
		model.addAttribute("moduleList", moduleList);									// 모듈 목록
		model.addAttribute("moduleDt", moduleDt);										// 선택 모듈 정보
    	model.addAttribute("queryString", queryString);
		    	
    	// 3. 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/list");
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
	@RequestMapping(value = "/input.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		int authIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		if(!isModify || authIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.MODULE_ID", moduleId));
		searchList.add(new DTForm("A.AUTH_IDX", authIdx));
		param.put("searchList", searchList);
		
		// 2.1 상세정보
		dt = moduleAuthService.getModify(param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		/*String confModule = StringUtil.getString(dt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray(confModule, null);
		if(JSONObjectUtil.isEmpty(authManagerArray)) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.auth.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 권한 정보
		JSONObject authObject = ModuleUtil.getModuleAuthObject(confModule, authIdx);*/
		// 권한 설정 정보
		JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray(moduleId);
		if(JSONObjectUtil.isEmpty(authManagerArray)) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.auth.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		// 권한 정보
		JSONObject authObject = ModuleUtil.getModuleAuthObject(moduleId, authIdx);
		
		List authMemberList = null;
		if(!JSONObjectUtil.isEmpty(authObject)) {
			// 회원정보 setting
			Set<?> set = authObject.keySet();
			Iterator<?> keys = set.iterator();
			String key = null;
			String authMVal = null;
			StringBuffer memberIdxs = new StringBuffer();
			while(keys.hasNext()) {
				key = (String) keys.next();
				if(key.endsWith("_MBR"))authMVal = JSONObjectUtil.getString(authObject, key);
				else authMVal = null;
				if(!StringUtil.isEmpty(authMVal)){
					int memberLen = memberIdxs.length();
					if(memberLen > 0) memberIdxs.append(",");
					memberIdxs.append(authMVal);
				}
			}

			int memberLen = memberIdxs.length();
			if(memberLen > 0) {
				authMemberList = CodeHelper.getMemberList(memberIdxs.toString().split(","));
			}
		}

		// 4. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 5. 속성 setting
		model.addAttribute("dt", dt);
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);
		if(authMemberList != null) {
			if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
			optnHashMap.put("mbrMbr", authMemberList);
		}
		model.addAttribute("optnHashMap", optnHashMap);
		model.addAttribute("authManagerArray", authManagerArray);
		model.addAttribute("authObject", authObject);
		model.addAttribute("submitType", submitType);
		
		// 6. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 7. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
		return getViewPath("/input");
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/input.do")
	public String input(@RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();

		// 1. 선택 모듈 상세 정보
		DataMap moduleDt = getModuleInfo(moduleId);
		if(moduleDt == null) {
			// 관리할 모듈(기능)이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		/*String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray(confModule, null);*/
		JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray(moduleId);
		if(JSONObjectUtil.isEmpty(authManagerArray)) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.auth.manage.file")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		//JSONObject authObject = ModuleUtil.getModuleAuthObject(confModule, authIdx);

		// 2. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 3. 속성 setting
		model.addAttribute("dt", moduleDt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("authManagerArray", authManagerArray);
		//model.addAttribute("authObject", authObject);
		model.addAttribute("submitType", submitType);
		
		// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 5. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
		return getViewPath("/input");
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
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @RequestParam(value="moduleId") String moduleId, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		int authIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		if(!isModify || authIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 3. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 4. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
    	int result = moduleAuthService.update(moduleId, authIdx, request.getRemoteAddr(), parameterMap, items, itemOrder);
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
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@RequestParam(value="moduleId") String moduleId, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. DB
    	int result = moduleAuthService.insert(moduleId, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + request.getAttribute("URL_LIST") + "\");"));
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
	@RequestMapping(method=RequestMethod.POST, value = "/deleteProc.do", params="select")
	public String delete(@RequestParam(value="moduleId") String moduleId, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		// 1. DB
		int result = moduleAuthService.delete(moduleId, deleteIdxs, request.getRemoteAddr());
    	
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
	 * 삭제처리 : 단일 parameter
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.GET, value = "/deleteProc.do")
	public String delete(@RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 1. 필수 parameter 검사
		int authIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(authIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		String[] deleteIdxs = {authIdx + ""};
		
		// 2. DB
		int result = moduleAuthService.delete(moduleId, deleteIdxs, request.getRemoteAddr());
    	
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
	@RequestMapping(method=RequestMethod.POST, value = "/restoreProc.do", params="select")
	public String restore(@RequestParam(value="moduleId") String moduleId, @RequestParam(value="select") String[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		// 1. DB
		int result = moduleAuthService.restore(moduleId, restoreIdxs, request.getRemoteAddr());
    	
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
	@RequestMapping(method=RequestMethod.POST, value = "/cdeleteProc.do", params="select")
	public String cdelete(@RequestParam(value="moduleId") String moduleId, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 2. DB
		int result = moduleAuthService.cdelete(moduleId, deleteIdxs, items, itemOrder);
    	
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
	@RequestMapping(value = "/deleteList.do")
	public String deleteList(@RequestParam(value="moduleId") String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
    	// 1. 페이지정보 setting
		int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
		int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
		int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
		
		int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
		if(pageUnit == 0) pageUnit = listUnit;	// 페이지당 목록 수
		int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
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
		searchList.add(new DTForm("A.MODULE_ID", moduleId));
		searchList.add(new DTForm("A.AUTH_IDX", 0, "<>"));
		
		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, "list_search");
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.LAST_MODI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = moduleAuthService.getDeleteCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = moduleAuthService.getDeleteList(param);
    	}

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드
		    	
    	// 4. 휴지통 경로
    	fn_setDeleteListPath(attrVO);
    	
		return getViewPath("/deleteList");
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
		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, idxName, pageName);

		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setCommonAddPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setCommonAddPath(HttpServletRequest request) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String grupSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/group/searchList.do";
		String dptSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/depart/searchList.do";
		String mbrSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/member/totSearchList.do";
		String grupSearchUrl = grupSearchName;
		String dptSearchUrl = dptSearchName;
		String mbrSearchUrl = mbrSearchName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			grupSearchUrl += baseQueryString;
			dptSearchUrl += baseQueryString;
			mbrSearchUrl += baseQueryString;
		}

		request.setAttribute("URL_GRUPSEARCHLIST", grupSearchUrl);
		request.setAttribute("URL_DPRTSEARCHLIST", dptSearchUrl);
		request.setAttribute("URL_MMBRSEARCHLIST", mbrSearchUrl);
	}

	/**
	 * 휴지통 경로
	 */
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");			// 목록 페이징  key

		// 항목 설정 정보
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		String[] baseParams = StringUtil.addStringToArray(this.baseParams, "moduleId");
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
	}
}
