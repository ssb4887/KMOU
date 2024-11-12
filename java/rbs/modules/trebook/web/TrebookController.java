package rbs.modules.trebook.web;

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

import rbs.egovframework.LoginVO;
import rbs.modules.trebook.service.TrebookService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DTOrderForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.AuthHelper;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.CookieUtil;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 이북게시판<br/>
 * : 통합관리시스템 > 메뉴콘텐츠관리, 통합관리시스템 > 기능등록관리, 사용자 사이트 에서 사용
 * @author user
 *
 */
@Controller
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/trebook", "/{admSiteId}/moduleFn/trebook", "/{siteId}/trebook"})
@ModuleMapping(moduleId="trebook")
public class TrebookController extends ModuleController {
	
	@Resource(name = "trebookService")
	private TrebookService trebookService;
	
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
	@RequestMapping(value = "/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAdmMode = attrVO.isAdmMode();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		JSONObject crtMenu = attrVO.getCrtMenu();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(isAdmMode || usePaging == 1) {
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
		List<DTOrderForm> orderList = new ArrayList<DTOrderForm>();

		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);		
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);			
			model.addAttribute("isSearchList", new Boolean(true));
		}
						
		// 정렬조건
		String designType = JSONObjectUtil.getString(settingInfo, "design_type");
		orderList.add(new DTOrderForm("A.TRE_ISSUE1", 1));
		if(!"default".equals(designType)) orderList.add(new DTOrderForm("A.TRE_ISSUE2", 1));
		if("daily".equals(designType)) orderList.add(new DTOrderForm("A.TRE_ISSUE3", 1));		
		
		// 년도별 페이징을 위한 부분
		int useDisplayYear = JSONObjectUtil.getInt(settingInfo, "use_display_year");	// 년도별 페이징 사용여부
		int noTotalCount = 0;															// 페이징이 적용안된 전체 카운트 수
		if(!isAdmMode && usePaging != 1 && useDisplayYear == 1 && !"default".equals(designType)) {
			// 사용자모드, 페이징(우선)이 아닐때, 년도별페이징 사용여부, 비정기지(입력) 일때 사용
			// SearchList Set => 페이징이 적용안된 전체 카운트
			Map<String, Object> yearParam = new HashMap<String, Object>();
			yearParam.put("searchList", searchList);
			noTotalCount = trebookService.getTotalCount(fnIdx, yearParam);			
			
			// 년도별 페이징 Parameter Set 
			int yearCurrentPageNo = StringUtil.getInt(request.getParameter("page"), 1);		// 현재 페이지 No
			int yearListBlock = JSONObjectUtil.getInt(settingInfo, "dset_list_block", 3);	// 페이징수
			List<DTForm> yearSearchList = new ArrayList<DTForm>();
			
			
			
			
			
			// 체크!!
			//yearSearchList.add(new DTForm("ROWNUM", (yearCurrentPageNo*yearListBlock)+1, "<"));			
			yearParam.put("ROWNUM", (yearCurrentPageNo*yearListBlock)+1);
			yearParam.put("searchList", yearSearchList);
			
			// 페이징 수에 해당하는 최소 년도 구하기
			DataMap dt = trebookService.getMinYear(fnIdx, yearParam);
			
			// 최소년도보다 큰 년도에 해당하는 데이터 조회
			searchList.add(new DTForm("A.TRE_ISSUE1", dt.get("TRE_ISSUE1"), ">="));
			
			// 현재페이지+1 페이지 No Set
			paginationInfo.setCurrentPageNo(yearCurrentPageNo+1);
		}
		
		param.put("orderList", orderList);
		param.put("searchList", searchList);
		
		// 관리자는 강제 페이징 사용
		if(isAdmMode || usePaging == 1) {
			// 2.2 목록수
	    	totalCount = trebookService.getTotalCount(fnIdx, param);
			paginationInfo.setTotalRecordCount(totalCount);
	    	
	    	if(totalCount > 0) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    		
	    		// 2.3 목록
	    		list = trebookService.getList(fnIdx, param);
	    	}
		} else {
			// 2.2 목록
			list = trebookService.getList(fnIdx, param);
			// 2.3 목록수
			if(list != null) totalCount = list.size();
			paginationInfo.setTotalRecordCount(totalCount);
		}
		
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_" + JSONObjectUtil.getString(settingInfo, "use_pdf_upload") + "_order");

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목코드    	
    	model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
    	model.addAttribute("trebookType", CodeHelper.getOptnName("TREBOOK_TYPE", JSONObjectUtil.getString(settingInfo, "design_type")));
    	
    	model.addAttribute("noTotalCount", noTotalCount);											// 모든글 Count
    	
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	String viewPage = "/list";
    	if(!isAdmMode && usePaging != 1 && useDisplayYear == 1) viewPage = "/list_year";    	
    	
		return getViewPath(viewPage);
	}
	
	/**
	 * 상세조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/view.do")
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();

		int fnIdx = attrVO.getFnIdx();


		// 1. 필수 parameter 검사
		int treIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(treIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxColumnName, treIdx));
		param.put("searchList", searchList);

		// 2.1 상세정보
		dt = trebookService.getView(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 2.2 Con 목록
		List<?> conList = null;
		int conTotalCount = trebookService.getConTotalCount(fnIdx, param);
		if(conTotalCount > 0){
			conList = trebookService.getConList(fnIdx, param);
		}

		/****************************************************************************
		 * 조회수 수정 
		 ****************************************************************************/
		int input = queryString.getInt("input");		// 글쓰기 후 확인  페이지 여부
		boolean isAddViews = false;
		String cookieName = request.getAttribute("crtSiteId") + "_" + StringUtil.getInt(request.getParameter("mId")) + "_" + fnIdx + "_trebook_" + treIdx;
		String cookieSetValue = treIdx + "";
		String cookieVal = null;
		if(input != 1) {
			// 저장된 쿠키 가져오기
			cookieVal = CookieUtil.getCookie(request, cookieName);
			//System.out.println("cookieVal==>" + cookieName + ":" + cookieSetValue + ":" + cookieVal);
			if(StringUtil.isEmpty(cookieVal) || !StringUtil.isEquals(cookieVal, cookieSetValue))	{

				// 조회수증가
		    	trebookService.updateViews(fnIdx, treIdx);
				dt.put("VIEWS", StringUtil.getInt(dt.get("VIEWS")) + 1);
				isAddViews = true;
				model.addAttribute("addViews", isAddViews);
			}
			
			// 쿠키저장
			if(StringUtil.isEmpty(cookieVal)) CookieUtil.setCookie(response, cookieName, cookieSetValue, 60*60*24, "UTF-8");
		}

		// 3. 항목설정
		String submitType = "view";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");		
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_" + JSONObjectUtil.getString(settingInfo, "use_pdf_upload") + "_order");
			
		// 4. 속성 setting
		model.addAttribute("dt", dt);																				// 상세정보
		model.addAttribute("conList", conList);																		// Con List
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(dt, items, itemOrder));						// 항목코드
		model.addAttribute("submitType", submitType);																// 페이지구분
		model.addAttribute("trebookType", CodeHelper.getOptnName("TREBOOK_TYPE", JSONObjectUtil.getString(settingInfo, "design_type")));
		
    	// 5. 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/view");
	}
	
	/**
	 * 수정 : 관리권한 없는 경우 - 자신이 등록한 글만 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/input.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

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
		int treIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		if(!isModify || treIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxColumnName, treIdx));
		param.put("searchList", searchList);
		
		// 2.1 상세정보
		dt = trebookService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 2.2 Con 목록
		List<?> conList = null;
		int conTotalCount = trebookService.getConTotalCount(fnIdx, param);
		if(conTotalCount > 0){
			conList = trebookService.getConList(fnIdx, param);
		}
				
		// 3. 전체관리/완전삭제 권한 체크
		Boolean isMngAuth = isMngAuth(StringUtil.getString(dt.get("REGI_IDX")));
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");		
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_" + JSONObjectUtil.getString(settingInfo, "use_pdf_upload") + "_order");

		// 5. 속성 setting
		model.addAttribute("dt", dt);																				// 상세정보
		model.addAttribute("conList", conList);																		// Con List
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));							// 항목코드
		model.addAttribute("submitType", submitType);																// 페이지구분
		
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
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();		

		// 1. 작성자명 setting
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		DataMap dt = new DataMap();

		// 2. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");		
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_" + JSONObjectUtil.getString(settingInfo, "use_pdf_upload") + "_order");
		
		if(loginVO != null)	{
			String memberName = loginVO.getMemberNameOrg();
			JSONObject nameItem = JSONObjectUtil.getJSONObject(items, "name");
			int objectType = JSONObjectUtil.getInt(nameItem, "object_type");
			if(objectType == 101) {
				// 암/복호화 항목
				memberName = loginVO.getMemberName();
			}
			dt.put("NAME", memberName);
		}

		// 3. 속성 setting
		model.addAttribute("dt", dt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);

    	// 4. 기본경로
    	fn_setCommonPath(attrVO);

    	// 5. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
		return getViewPath("/input");
	}

	/**
	 * 수정처리 : 관리권한 없는 경우 - 자신이 등록한 글만 수정
	 * @param mode
	 * @param parameterMap
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAdmMode = attrVO.isAdmMode();
		String siteMode = attrVO.getSiteMode();
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
		
		// 1.2 필수 parameter 검사
		int treIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(!isModify || treIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. 전체관리/완전삭제 권한 체크
		Boolean isMngAuth = isMngProcAuth(fnIdx, treIdx);
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}			

		// 3. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc" + "_" + JSONObjectUtil.getString(settingInfo, "use_pdf_upload") + "_order");
		
		// Con Items
		JSONObject conItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "con_item_info");
		JSONObject conItems = JSONObjectUtil.getJSONObject(conItemInfo, "items");
		JSONObject conFileItem = JSONObjectUtil.getJSONObject(conItems, "conPdfFile");
		
		// 4. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
    	int result = trebookService.update(uploadModulePath, fnIdx, treIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder, conFileItem);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
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
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean isAdmMode = attrVO.isAdmMode();
		String siteMode = attrVO.getSiteMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc" + "_" + JSONObjectUtil.getString(settingInfo, "use_pdf_upload") + "_order");
		
		// Con Items
		JSONObject conItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "con_item_info");
		JSONObject conItems = JSONObjectUtil.getJSONObject(conItemInfo, "items");
		JSONObject conFileItem = JSONObjectUtil.getJSONObject(conItems, "conPdfFile");
				
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. DB
    	int result = trebookService.insert(uploadModulePath, fnIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder, conFileItem);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
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
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/deleteProc.do", params="select")
	public String delete(@RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String siteMode = attrVO.getSiteMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. DB
		int result = trebookService.delete(fnIdx, deleteIdxs, siteMode, request.getRemoteAddr());
    	
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
	 * 삭제처리 : 관리권한 없는 경우 - 자신이 등록한 글만 수정
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.GET, value = "/deleteProc.do")
	public String delete(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		String siteMode = attrVO.getSiteMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 필수 parameter 검사
		int treIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(treIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. 전체관리/완전삭제 권한 체크
		Boolean isMngAuth = isMngProcAuth(fnIdx, treIdx);
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		String[] deleteIdxs = {treIdx + ""};
		// 2. DB
		int result = trebookService.delete(fnIdx, deleteIdxs, siteMode, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.delete"), "fn_procAction(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
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
	@RequestMapping(method=RequestMethod.POST, value = "/restoreProc.do", params="select")
	public String restore(@RequestParam(value="select") String[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String siteMode = attrVO.getSiteMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. DB
		int result = trebookService.restore(fnIdx, restoreIdxs, siteMode, request.getRemoteAddr());
    	
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
	@RequestMapping(method=RequestMethod.POST, value = "/cdeleteProc.do", params="select")
	public String cdelete(@RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");		
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_" + JSONObjectUtil.getString(settingInfo, "use_pdf_upload") + "_order");
		
		// 2. DB
		int result = trebookService.cdelete(uploadModulePath, fnIdx, deleteIdxs, items, itemOrder);
    	
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
	@RequestMapping(value = "/deleteList.do")
	public String deleteList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
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
    	totalCount = trebookService.getDeleteCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = trebookService.getDeleteList(fnIdx, param);
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
	 * 관리권한 체크
	 * @param regCd
	 * @return
	 */
	public boolean isMngAuth(String regiIdx) {

		// 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");				// 전체관리
		if(!isMngAuth/* && !isCdtAuth*/) {
			// 전체관리권한 없는 경우 : 자신글만 수정

			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
			String mbrCd = (loginVO != null)?loginVO.getMemberIdx():null;
			
			return (StringUtil.isEquals(mbrCd, regiIdx)); 
		}
		
		return true;
	}
	
	/**
	 * 관리권한 체크
	 * @param treIdx
	 * @return
	 */
	public boolean isMngProcAuth(int fnIdx, int treIdx) {
		
		int modiCnt = 0;
		// 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");		// 전체관리
		if(!isMngAuth) {
			// 전체관리권한 없는 경우 : 자신글만 수정

			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
			String mbrCd = (loginVO != null)?loginVO.getMemberIdx():null;

			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			
			searchList.add(new DTForm("A.TRE_IDX", treIdx));
			searchList.add(new DTForm("A.REGI_IDX", mbrCd));
			param.put("searchList", searchList);
			
			modiCnt = trebookService.getAuthCount(fnIdx, param);
			
			return (modiCnt > 0);
		}
		
		return true;
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		int dsetStartYear = JSONObjectUtil.getInt(settingInfo, "dset_start_year");
		if(dsetStartYear > 0) {			
			JSONObject treIssueItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "items"), "treIssue");
			if(treIssueItem != null) {
				treIssueItem.put("start_year", dsetStartYear);
			}
			JSONObject listSearchItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "list_search"), "key"), "items"), "treIssue");
			if(listSearchItem != null) listSearchItem.put("start_year", dsetStartYear);
		}
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);
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
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
	}

}
