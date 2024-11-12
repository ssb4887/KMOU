package rbs.modules.poll.web;

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
import org.springframework.web.servlet.ModelAndView;

import rbs.modules.poll.service.QuesService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
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

/**
 * 설문관리<br/>
 * @author user
 *
 */
@Controller
@RequestMapping(value={"/{admSiteId}/menuContents/{usrSiteId}/poll", "/{admSiteId}/moduleFn/poll", "/{siteId}/poll"}, params="pollIdx")
@ModuleMapping(moduleId="poll", confSModule="ques")
public class QuesController extends ModuleController {

	//@Resource(name = "pollService")
	//private PollService pollService;
	
	@Resource(name = "pollQuesService")
	private QuesService quesService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	/**
	 * 목록조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/quesList.do")
	public String list(@RequestParam(value="pollIdx") int pollIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		DataForm queryString = attrVO.getQueryString();
		JSONObject crtMenu = attrVO.getCrtMenu();
		boolean isAdmMode = attrVO.isAdmMode();
		
		// 설문정보
		String pollInfoPathFail = getPollInfo(false, model, fnIdx, pollIdx);
		if(pollInfoPathFail != null) return pollInfoPathFail;
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = ModuleUtil.getSettingValue(isAdmMode, "dset_list_count", crtMenu, settingInfo, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = ModuleUtil.getSettingValue(isAdmMode, "dset_list_block", crtMenu, settingInfo, propertiesService.getInt("PAGE_SIZE"));	
			
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
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));

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
    	totalCount = quesService.getTotalCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		}
    		
    		// 2.3 목록
    		list = quesService.getList(fnIdx, param);
    	}
    	
    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목 코드
		// 3. 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
    	
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/list");
	}


	/**
	 * 상세조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/quesView.do")
	public String view(@RequestParam(value="pollIdx") int pollIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 필수 parameter 검사
		int popIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(popIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		
		searchList.add(new DTForm("A." + idxColumnName, popIdx));
		param.put("searchList", searchList);
		
		// 2.1 상세정보
		dt = quesService.getView(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 3. 항목설정
		String submitType = "view";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 4. 속성 setting
		model.addAttribute("dt", dt);																// 상세정보
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(dt, items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);												// 페이지구분
		
    	// 5. 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/view");
	}

	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/itemList.json", headers="Ajax", params={"pollIdx", "quesIdx"})
	public ModelAndView itemList(@RequestParam(value="pollIdx") int pollIdx, @RequestParam(value="quesIdx") int quesIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		int fnIdx = attrVO.getFnIdx();
		
		ModelAndView mav = new ModelAndView("jsonView");

		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		searchList.add(new DTForm("A.QUESTION_IDX", quesIdx));
		param.put("searchList", searchList);
		list = quesService.getItemJSONList(fnIdx, param);
		
		model.addAttribute("list", list);
		
		return mav;
	}
	
	/**
	 * 결과유형 항목 setting
	 * @param isquestype
	 * @param itemInfo
	 * @param request
	 */
	public DataMap getQuestypeDt(DataMap dt, JSONObject itemInfo, HttpServletRequest request){
		Object pollInfoObj = request.getAttribute("pollDt");
		if(pollInfoObj == null) return dt;
		
		DataMap pollInfo = (DataMap)pollInfoObj;
		int isquestype = StringUtil.getInt(pollInfo.get("ISQUESTYPE"));
		if(isquestype == 1) {
			// 결과유형 사용하는 경우
			JSONObject itemInfo2 = new JSONObject();
			itemInfo2.accumulateAll(itemInfo);
			JSONObject items2 = JSONObjectUtil.getJSONObject(itemInfo2, "items");
			JSONObject questionContentsObj = JSONObjectUtil.getJSONObject(items2, "questionContents");
			questionContentsObj.put("object_type", 0);
			
			request.setAttribute("itemInfo", itemInfo2);
			
			if(dt == null) dt = new DataMap();
			dt.put("USE_INQUES", "1");
			dt.put("ANSWER_TYPE", "1");
			dt.put("ITEM_TYPE", "1");
		}
		
		return dt;
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
	@RequestMapping(value = "/quesInput.do", params="mode")
	public String input(@RequestParam(value="pollIdx") int pollIdx, @RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;

		// 1.2 필수 parameter 검사
		int quesIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		if(!isModify || quesIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		
		// 설문정보
		String pollInfoPathFail = getPollInfo(false, model, fnIdx, pollIdx);
		if(pollInfoPathFail != null) return pollInfoPathFail;
		
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		
		searchList.add(new DTForm("A." + idxColumnName, quesIdx));
		param.put("searchList", searchList);

		// 2.1 상세정보
		dt = quesService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		int ispollitem = StringUtil.getInt(request.getAttribute("ISPOLLITEM"));		// 공통항목 사용여부
		// 문항 목록
		List<Object> quesList = getQuesList(fnIdx, pollIdx);
		List<Object> inclassList = getInclassList(fnIdx, pollIdx, quesIdx);
		Map<String, List<Object>> inquesMap = getInquesMap(fnIdx, pollIdx, quesIdx);
		List<Object> itemList = (ispollitem == 1)?null:getItemList(fnIdx, pollIdx, quesIdx);

		// 3. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 결과유형 항목 setting
		dt = getQuestypeDt(dt, itemInfo, request);

		// 4. 속성 setting
		model.addAttribute("dt", dt);															// 상세정보
		model.addAttribute("quesList", quesList);
		model.addAttribute("inclassList", inclassList);
		model.addAttribute("inquesMap", inquesMap);
		model.addAttribute("itemList", itemList);
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);
		if(quesList != null) {
			if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
			optnHashMap.put("ordIdx", quesList);
		}
		model.addAttribute("optnHashMap", optnHashMap);		// 항목코드
		//model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분

    	// 5. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 6. form action
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
	@RequestMapping(value = "/quesInput.do")
	public String input(@RequestParam(value="pollIdx") int pollIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		
		// 설문정보
		String pollInfoPathFail = getPollInfo(true, model, fnIdx, pollIdx);
		if(pollInfoPathFail != null) return pollInfoPathFail;
		
		// 문항목록
		List<Object> quesList = getQuesList(fnIdx, pollIdx);
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 결과유형 항목 setting
		DataMap dt = getQuestypeDt(null, itemInfo, request);

		// 4. 속성 setting
		model.addAttribute("dt", dt);															// 상세정보

		// 2. 속성 setting
		model.addAttribute("quesList", quesList);
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);
		if(quesList != null) {
			if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
			optnHashMap.put("ordIdx", quesList);
		}
		model.addAttribute("optnHashMap", optnHashMap);		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분

    	// 3. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 4. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
		return getViewPath("/input");
	}

	/**
	 * 수정처리
	 * @param mode
	 * @param parameterMap
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/quesInputProc.do", params="mode")
	public String inputProc(@RequestParam(value="pollIdx") int pollIdx, @RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		int quesIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		// 1.2 필수 parameter 검사
		if(!isModify || quesIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 3. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 4. DB
    	int result = quesService.update(uploadModulePath, fnIdx, pollIdx, quesIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 5. 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
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
	 * @param parameterMap
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/quesInputProc.do")
	public String inputProc(@RequestParam(value="pollIdx") int pollIdx, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		
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
    	int result = quesService.insert(uploadModulePath, fnIdx, pollIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 4. 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	} else if(result == -11) {
			// 설문내용이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(false, rbsMessageSource.getMessage("message.poll.no.contents")));
			return RbsProperties.getProperty("Globals.fail.path");
    	} else if(result == -12) {
			// 설문진행중인 경우
			model.addAttribute("message", MessageUtil.getAlert(false, rbsMessageSource.getMessage("message.poll.app.ing.con")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 삭제처리
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/quesDeleteProc.do", params="select")
	public String delete(@RequestParam(value="pollIdx") int pollIdx, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. DB
		int result = quesService.delete(fnIdx, pollIdx, deleteIdxs, request.getRemoteAddr());
    	
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
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.GET, value = "/quesDeleteProc.do")
	public String delete(@RequestParam(value="pollIdx") int pollIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		String[] deleteIdxs = {brdIdx + ""};
		// 2. DB
		int result = quesService.delete(fnIdx, pollIdx, deleteIdxs, request.getRemoteAddr());
    	
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
	 * @param restoreIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/quesRestoreProc.do", params="select")
	public String restore(@RequestParam(value="pollIdx") int pollIdx, @RequestParam(value="select") String[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. DB
		int result = quesService.restore(fnIdx, pollIdx, restoreIdxs, request.getRemoteAddr());
    	
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
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="CDT")
	@RequestMapping(method=RequestMethod.POST, value = "/quesCdeleteProc.do", params="select")
	public String cdelete(@RequestParam(value="pollIdx") int pollIdx, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 2. DB
		int result = quesService.cdelete(uploadModulePath, fnIdx, pollIdx, deleteIdxs, items, itemOrder);
    	
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
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/quesDeleteList.do")
	public String deleteList(@RequestParam(value="pollIdx") int pollIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		int fnIdx = attrVO.getFnIdx();
		
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
		
		// 2.1 검색조건
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, "list_search");
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.LAST_MODI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = quesService.getDeleteCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = quesService.getDeleteList(fnIdx, param);
    	}

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드
		// 3. 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		    	
    	// 4. 휴지통 경로
    	fn_setDeleteListPath(attrVO);
    	
		return getViewPath("/deleteList");
	}
	
	/**
	 * 설문 경로
	 * @param attrVO
	 */
	public String[] fn_getPollParams(ModuleAttrVO attrVO) {
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject moduleItem = attrVO.getModuleItem();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "setting_info");
		JSONObject itemInfo = JSONObjectUtil.getJSONObject(moduleItem, "item_info");
		
		//String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		String[] tabBaseParams = null;	
		
		return PathUtil.getTotalBaseParams(baseParams, tabBaseParams, searchParams, null, null, pageName);
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		String[] pollAllParams = fn_getPollParams(attrVO);
		// IDX setting
		JSONObject pollSettingInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleSetting(), "setting_info");
		String pollIdxName = JSONObjectUtil.getString(pollSettingInfo, "idx_name");			// 상세조회 key
		String[] idxNames = StringUtil.stringToArray(pollIdxName, ",");
		String[] baseParams = StringUtil.insertStringArrays(pollAllParams, idxNames);
				
		//String[] baseParams = fn_getPollParams(attrVO);

		JSONObject settingInfo = attrVO.getSettingInfo();
		//JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		//String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		String listName = "quesList.do";
		String viewName = "quesView.do";
		String inputName = "quesInput.do";
		String inputProcName = "quesInputProc.do";
		String deleteProcName = "quesDeleteProc.do";
		String deleteListName = "quesDeleteList.do";
		String imageName = "quesImage.do";
		String downloadName = "quesDownload.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, null
				, null, null, pageName, idxName
				, listName, viewName, inputName, inputProcName
				, deleteProcName, deleteListName
				, imageName, downloadName, null);

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		StringBuffer pollQueryString = PathUtil.getQueryString(queryString, pollAllParams);
		request.setAttribute("pollQueryString", pollQueryString.toString());
		fn_setAddPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setAddPath(HttpServletRequest request) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String itemListName = "itemList.json";
		String itemListUrl = itemListName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			itemListUrl += baseQueryString;
		}

		request.setAttribute("URL_ITEMLIST", itemListUrl);
		
		String pollQueryString = StringUtil.getString(request.getAttribute("pollQueryString"));
		String pollListName = "list.do?";
		String pollListUrl = pollListName;
		if(!StringUtil.isEmpty(pollQueryString)) {
			pollListUrl += pollQueryString;
		}
		request.setAttribute("URL_POLLLIST", pollListUrl);
	}

	/**
	 * 휴지통 경로
	 */
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {
		// 설문기본 key, page
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject pollSettingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "setting_info");
		
		String pollIdxName = JSONObjectUtil.getString(pollSettingInfo, "idx_name");			// 상세조회 key
		String pollPageName = JSONObjectUtil.getString(pollSettingInfo, "page_name");		// 목록 페이징  key
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{pollIdxName, pollPageName});		// 기본 parameter

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");			// 목록 페이징  key

		String listName = "quesList.do";
		String restoreProcName = "quesRestoreProc.do";
		String cdeleteProcName = "quesCdeleteProc.do";
		// 항목 설정 정보
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, null, pageName, listName, restoreProcName, cdeleteProcName);
	}
	
	/**
	 * 문항 목록
	 * @param fnIdx
	 * @param pollIdx
	 * @return
	 */
	private List<Object> getQuesList(int fnIdx, int pollIdx) {
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		param.put("searchList", searchList);
		list = quesService.getQuesList(fnIdx, param);
		
		return list;
	}
	
	/**
	 * 내부분류 목록
	 * @param fnIdx
	 * @param pollIdx
	 * @return
	 */
	private List<Object> getInclassList(int fnIdx, int pollIdx, int quesIdx) {
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		searchList.add(new DTForm("A.QUESTION_IDX", quesIdx));
		param.put("searchList", searchList);
		list = quesService.getInclassList(fnIdx, param);
		
		return list;
	}
	
	/**
	 * 내부문항 목록
	 * @param fnIdx
	 * @param pollIdx
	 * @param quesIdx
	 * @return
	 */
	private Map<String, List<Object>> getInquesMap(int fnIdx, int pollIdx, int quesIdx) {
		Map<String, List<Object>> map = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		searchList.add(new DTForm("A.PAR_QUES_IDX", quesIdx));
		param.put("searchList", searchList);
		map = quesService.getInquesMap(fnIdx, param);
		
		return map;
	}
	
	/**
	 * 항목 목록
	 * @param fnIdx
	 * @param pollIdx
	 * @param quesIdx
	 * @return
	 */
	private List<Object> getItemList(int fnIdx, int pollIdx, int quesIdx) {
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		searchList.add(new DTForm("A.QUESTION_IDX", quesIdx));
		param.put("searchList", searchList);
		list = quesService.getItemList(fnIdx, param);
		
		return list;
	}
	
	/**
	 * 설문정보
	 * @param fnIdx
	 * @param pollIdx
	 * @return
	 */
	private String getPollInfo(boolean isPollIngChk, ModelMap model, int fnIdx, int pollIdx) {
		
		int result = quesService.pollMng(isPollIngChk, fnIdx, pollIdx);
		if(result == -1) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(false, rbsMessageSource.getMessage("message.poll.no.contents")));
			return RbsProperties.getProperty("Globals.fail.path");
		} else if(result == -2) {
			// 설문진행중인 경우
			model.addAttribute("message", MessageUtil.getAlert(false, rbsMessageSource.getMessage("message.poll.app.ing.con")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		/*
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		param.put("searchList", searchList);
		DataMap dt = quesService.getPollInfo(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(false, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		int respCnt = quesService.getRespCnt(fnIdx, param);
		
		if(isPollIngChk) {
			// 설문진행 체크
			String slimitDate = StringUtil.getString(dt.get("LIMIT_DATE11"));
			if(!StringUtil.isEmpty(slimitDate)) {
				int pollState = StringUtil.getInt(dt.get("ISSTOP"));
				slimitDate = dt.get("LIMIT_DATE11") + " " + StringUtil.getString(dt.get("LIMIT_DATE12"), "00")  + ":" + StringUtil.getString(dt.get("LIMIT_DATE13"), "00") + ":00";
				int limitState = DateUtil.getDateTimeState(slimitDate);
				
				if(pollState != 2 && limitState <= 0 && respCnt > 0 || respCnt > 0) {
					// 설문진행중인 경우
					model.addAttribute("message", MessageUtil.getAlert(false, rbsMessageSource.getMessage("message.poll.app.ing.con")));
					return RbsProperties.getProperty("Globals.fail.path");
				}
			}
		}
		model.addAttribute("pollDt", dt);
		model.addAttribute("respCnt", respCnt);
		*/
		return null;
	}
}
