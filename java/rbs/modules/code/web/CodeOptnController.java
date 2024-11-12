package rbs.modules.code.web;

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

import rbs.modules.code.service.CodeOptnService;

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
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 코드관리 
 * 1. 등록관리권한으로 접근가능
 * 2. 완전삭제 - 완전삭제 권한
 * @author user
 *
 */
@Controller
@RequestMapping(value="/{admSiteId}/code", params="mstCd")
@ModuleMapping(moduleId="code", confSModule="option")
public class CodeOptnController extends ModuleController {

	@Resource(name = "codeOptnService")
	private CodeOptnService codeOptnService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	/**
	 * 전문인 관리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="WRT")
	@RequestMapping(value = "/sptInputProc.do")
	public String sptInputProc(@RequestParam(value="mstCd") String mstCd, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		
    	int result = codeOptnService.deleteAndInsert(request, parameterMap);
    	
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.insert")));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -2) {
    		// 중복코드
    		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.code.exist.duplicate")));
    		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	
	/**
	 * 언어 속성 얻기
	 * @param attrVO
	 * @return
	 */
	public ModuleAttrVO getCommonLangAttrVO(ModuleAttrVO attrVO) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		DataForm queryString = attrVO.getQueryString();
		
		String slang = queryString.getString("slang");
		if(StringUtil.isEmpty(slang)) {
			slang = rbsMessageSource.getLocaleLang();
			queryString.put("slang", slang);
			attrVO.setQueryString(queryString);
			request.setAttribute("queryString", queryString);
		}
		request.setAttribute("langList", CodeHelper.getOptnList("LOCALE"));
		
		return attrVO;
	}

	//@ModuleAuth(name="MNG")
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/optnList.do")
	public String list(@RequestParam(value="mstCd") String mstCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		attrVO = getCommonLangAttrVO(attrVO);
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		String majorCd = "";
		String tit = "";
		
		//이동근 추가 전공정보등록용
		String isMajorInfo = "N";
		if(request.getParameter("isMajorInfo") != null) {
			isMajorInfo = request.getParameter("isMajorInfo");
			majorCd = request.getParameter("majorCd");
			tit = request.getParameter("tit");
		}
		
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
			
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
		searchList.add(new DTForm("A.MASTER_CODE", mstCd));
		
		//이동근 추가 전공정보등록용
		if(isMajorInfo.equals("Y")) {
			searchList.add(new DTForm("SUBSTRING_INDEX(A.OPTION_CODE, \"-\", 1)", majorCd));		
		}

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
		List<?> ordList = null;

		if(usePaging == 1) {
			// 2.2 목록수
	    	totalCount = codeOptnService.getTotalCount(lang, param);
			paginationInfo.setTotalRecordCount(totalCount);
	    	
	    	if(totalCount > 0) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    		
	    		// 2.3 목록
	    		list = codeOptnService.getList(lang, param);
	    	}
		} else {
			// 2.2 목록
			list = codeOptnService.getList(lang, param);
			// 2.3 목록수
			if(list != null) totalCount = list.size();
			paginationInfo.setTotalRecordCount(totalCount);
		}
		
		if(isMajorInfo.equals("Y")) {
			System.out.println("??????????????????");
			ordList = codeOptnService.getOrderList(param);
		}
		
    	model.addAttribute("paginationInfo", paginationInfo);							// 페이징정보
    	model.addAttribute("list", list);												// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드
    	model.addAttribute("isMajorInfo", isMajorInfo);			// 이동근 추가 전공정보등록용
    	model.addAttribute("majorCd", majorCd);						// 이동근 추가 전공정보등록용
    	model.addAttribute("tit", tit);						// 이동근 추가 전공정보등록용
    	model.addAttribute("ordList", ordList);						// 이동근 추가 전공정보등록용
    	
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/list");
	}
	
	// 백업용
	/*@ModuleAuth(name="VEW")
	@RequestMapping(value = "/optnList.do")
	public String list(@RequestParam(value="mstCd") String mstCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		attrVO = getCommonLangAttrVO(attrVO);
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
			
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
		searchList.add(new DTForm("A.MASTER_CODE", mstCd));

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

		if(usePaging == 1) {
			// 2.2 목록수
	    	totalCount = codeOptnService.getTotalCount(lang, param);
			paginationInfo.setTotalRecordCount(totalCount);
	    	
	    	if(totalCount > 0) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    		
	    		// 2.3 목록
	    		list = codeOptnService.getList(lang, param);
	    	}
		} else {
			// 2.2 목록
			list = codeOptnService.getList(lang, param);
			// 2.3 목록수
			if(list != null) totalCount = list.size();
			paginationInfo.setTotalRecordCount(totalCount);
		}
    	model.addAttribute("paginationInfo", paginationInfo);							// 페이징정보
    	model.addAttribute("list", list);												// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드
    	
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/list");
	}*/
	
	/**
	 * 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/optnInput.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @RequestParam(value="mstCd") String mstCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		String isMajorInfo = request.getParameter("isMajorInfo");
		if(isMajorInfo == null) {
			isMajorInfo = "N";
		}
		String majorCd = request.getParameter("majorCd");
		String tit = request.getParameter("tit");
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		String optnCd = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(!isModify || StringUtil.isEmpty(optnCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.MASTER_CODE", mstCd));
		searchList.add(new DTForm("A.OPTION_CODE", optnCd));
		param.put("searchList", searchList);
		
		dt = codeOptnService.getModify(lang, param);

		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.MASTER_CODE", mstCd));
		if(isMajorInfo.equals("Y")) {
			searchList.add(new DTForm("SUBSTRING_INDEX(A.OPTION_CODE, \"-\", 1)", majorCd));		
		}
		param.put("searchList", searchList);
		List<?> ordList = codeOptnService.getOrderList(param);

		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		//전공정보등록용
		List<?> majorOptnHashMap = null;
		if(isMajorInfo.equals("Y")) {			
			majorOptnHashMap = codeOptnService.getMajorOptnHashMap(param);							
		}
		
		model.addAttribute("dt", dt);
		model.addAttribute("ordList", isMajorInfo.equals("Y") ? majorOptnHashMap : ordList);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		model.addAttribute("isMajorInfo", isMajorInfo);
		model.addAttribute("tit", tit);
		model.addAttribute("majorCd", majorCd);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	
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
	@RequestMapping(value = "/optnInput.do")
	public String input(@RequestParam(value="mstCd") String mstCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		String isMajorInfo = request.getParameter("isMajorInfo");
		if(isMajorInfo == null) {
			isMajorInfo = "N";
		}
		String majorCd = request.getParameter("mjCd");
		String tit = request.getParameter("tit");
		String nextCode = "";
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.MASTER_CODE", mstCd));
		if(isMajorInfo.equals("Y")) {
			searchList.add(new DTForm("SUBSTRING_INDEX(A.OPTION_CODE, \"-\", 1)", majorCd));		
		}
		param.put("searchList", searchList);
		List<?> ordList = codeOptnService.getOrderList(param);
		
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		//전공정보등록용
		List<?> majorOptnHashMap = null;
		if(isMajorInfo.equals("Y")) {			
			majorOptnHashMap = codeOptnService.getMajorOptnHashMap(param);
			nextCode = codeOptnService.getNextSptCode(majorCd);
		}

		model.addAttribute("ordList", isMajorInfo.equals("Y") ? majorOptnHashMap : ordList);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		model.addAttribute("isMajorInfo", isMajorInfo);
		model.addAttribute("majorCd", majorCd);
		model.addAttribute("tit", tit);
		model.addAttribute("nextSptCode", nextCode);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
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
	@RequestMapping(method=RequestMethod.POST, value = "/optnInputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @RequestParam(value="mstCd") String mstCd, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		String isMajorInfo = request.getParameter("isMajorInfo");
		if(isMajorInfo == null) {
			isMajorInfo = "N";
		}
		String majorCd = request.getParameter("majorCd");
		String tit = request.getParameter("tit");
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		String optCd = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));

		if(!isModify || StringUtil.isEmpty(optCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// DB
    	int result = codeOptnService.update(lang, mstCd, optCd, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
        	if(isMajorInfo.equals("Y")) {
        		System.out.println("fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_LIST")+ "&isMajorInfo=Y&mjCd=" + majorCd + "&tit=" + tit )) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");");
        		model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_LIST")+ "&dl=1&slang=ko&isMajorInfo=Y&mjCd=" + majorCd + "&tit=" + tit )) + "\", " + 1 + ");"));
        	}else {        		
        		model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction();"));
        	}
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 등록처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/optnInputProc.do")
	public String inputProc(@RequestParam(value="mstCd") String mstCd, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 2. DB
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
    	int result = codeOptnService.insert(mstCd, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -2) {
    		// 중복코드
    		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.code.exist.duplicate")));
    		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
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
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/optnDeleteProc.do", params="select")
	public String delete(@RequestParam(value="mstCd") String mstCd, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		// DB
		int result = codeOptnService.delete(mstCd, deleteIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.delete"), "location.reload();"));
			
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
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.GET, value = "/optnDeleteProc.do")
	public String delete(@RequestParam(value="mstCd") String mstCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();

		String optCd = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));
		if(StringUtil.isEmpty(optCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		String[] deleteIdxs = {optCd};
		// DB
		int result = codeOptnService.delete(mstCd, deleteIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.delete"), "location.reload();"));
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
	@RequestMapping(method=RequestMethod.POST, value = "/optnRestoreProc.do", params="select")
	public String restore(@RequestParam(value="mstCd") String mstCd, @RequestParam(value="select") String[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		// DB
		int result = codeOptnService.restore(mstCd, restoreIdxs, request.getRemoteAddr());
    	
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
	@RequestMapping(method=RequestMethod.POST, value = "/optnCdeleteProc.do", params="select")
	public String cdelete(@RequestParam(value="mstCd") String mstCd, @RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// DB
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		int result = codeOptnService.cdelete(mstCd, deleteIdxs, items, itemOrder);
    	
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
	@RequestMapping(value = "/optnDeleteList.do")
	public String deleteList(@RequestParam(value="mstCd") String mstCd, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		String listSearchId = "list_search";		// 검색설정
    	
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
		searchList.add(new DTForm("A.MASTER_CODE", mstCd));
		
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.LAST_MODI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = codeOptnService.getDeleteCount(lang, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = codeOptnService.getDeleteList(lang, param);
    	}
    	
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드
		    	
    	// 3. 경로 setting
    	// 3.1 기본경로
    	fn_setDeleteListPath(attrVO);
    	
		return getViewPath("/deleteList");
	}
	
	/**
	 * 경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");											// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");										// 목록 페이징  key
		String[] baseParams = StringUtil.addStringToArray(this.baseParams, "mstCd");								// 기본 parameter
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));	// 목록 검색 항목
		//PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);

		String sptInputProcName = "sptInputProc.do";
		
		if(StringUtil.isEmpty(pageName)) pageName = "page";
		String listName = "optnList.do";
		String viewName = "optnView.do";
		String inputName = "optnInput.do";
		String inputProcName = "optnInputProc.do";
		String deleteProcName = "optnDeleteProc.do";
		String deleteListName = "optnDeleteList.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, null, null, pageName, idxName
				, listName, viewName, inputName, inputProcName
				, deleteProcName, deleteListName);
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
		
		sptInputProcName += baseQueryString;
		
		request.setAttribute("URL_SPT_INPUT_PROC", sptInputProcName);
	}
	
	/**
	 * 휴지통 경로
	 */
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");								// 목록 페이징  key
		String[] baseParams = StringUtil.addStringToArray(this.baseParams, "mstCd");						// 기본 parameter

		// 항목 설정 정보
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		//PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
		if(StringUtil.isEmpty(pageName)) pageName = "page";
		String listName = "optnDeleteList.do";
		String restoreProcName = "optnRestoreProc.do";
		String cdeleteProcName = "optnCdeleteProc.do";
		
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, null, pageName, listName, restoreProcName, cdeleteProcName);
	
	}

}
