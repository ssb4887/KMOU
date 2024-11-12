package rbs.modules.tabContents.web;

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




























import rbs.modules.itemClass.service.ClassOptnService;
import rbs.modules.tabContents.mapper.TabContentsDataMapper;
import rbs.modules.tabContents.service.TabContentsService;

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
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/tabContents", "/{admSiteId}/moduleFn/tabContents", "/{siteId}/tabContents"})
@ModuleMapping(moduleId="tabContents")
public class TabContentsController extends ModuleController{
	
	@Resource(name = "classOptnService")
	private ClassOptnService classOptnService;
	
	@Resource(name = "tabContentsService")
	private TabContentsService tabContentsService;
	
	@Resource(name="tabContentsDataMapper")
	private TabContentsDataMapper tabcontentsDataDAO;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@ModuleAuth(name = "LST")
	@RequestMapping("/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		int fnIdx = attrVO.getFnIdx();

		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", 0, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");	// 최대 페이지당 목록 수
			int listUnitStep = listUnit;	// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;	// 페이지당 목록 수
			int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", 0, propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
			
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
		// 항목설정으로 검색조건  setting
		String listSearchId = "list_search";	// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null){
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);
		
		if(usePaging == 1) {
			// 2.2 목록수
			totalCount = tabContentsService.getTotalCount(fnIdx, param);
			paginationInfo.setTotalRecordCount(totalCount);
			
			if(totalCount > 0){
				param.put("firstIndex", paginationInfo.getFirstRecordIndex());
				param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
				
				// 2.3 목록
				list = tabContentsService.getList(fnIdx, param);
			}
		} else {
			// 2.3 목록
			list = tabContentsService.getList(fnIdx, param);
			// 2.3 목록수
			if(list != null) totalCount = list.size();
			paginationInfo.setTotalRecordCount(totalCount);
		}
		
		model.addAttribute("paginationInfo", paginationInfo);	// 페이징정보
		model.addAttribute("list", list);	// 목록
		model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목코드

		fn_setCommonPath(attrVO);
		return getViewPath("/list");
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value="/input.do")
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
			JSONObject settingInfo = attrVO.getSettingInfo();
			JSONObject itemInfo = attrVO.getItemInfo();
			
			String masterCode = JSONObjectUtil.getString(settingInfo, "dset_cate_master_code");
			
			
			// 1. 항목설정
			String submitType = "write";
			JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
			JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
			
			
			
			// 2. 속성 setting
			model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
			model.addAttribute("submitType", submitType);
			
			Map<String, Object> param1 = new HashMap<String, Object>();
			List<DTForm> searchList1 = new ArrayList<DTForm>();
			searchList1.add(new DTForm("A." +"MASTER_CODE", masterCode));
			param1.put("searchList", searchList1);
			List<?> ordList = classOptnService.getOptnList(param1);
			
			
			// 3. 기본경로
			fn_setCommonPath(attrVO);
			
			// 4. form action
			model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
			request.setAttribute("tabCnt", 10);
			model.addAttribute("ordList", ordList);
			return getViewPath("/input");
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
	@RequestMapping(method=RequestMethod.POST, value="/inputProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, HttpServletRequest request, HttpServletResponse response, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		boolean isAjax = attrVO.isAjax();
        String siteMode=attrVO.getSiteMode();
        int fnIdx=attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		String uploadModulePath = attrVO.getUploadModulePath();
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
		int result = tabContentsService.insert(uploadModulePath, fnIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
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
	 * 뷰
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value="/view.do")
	public String view( String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception{
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		
		String masterCode = JSONObjectUtil.getString(settingInfo, "dset_cate_master_code");
	
		
		// 1.2 필수 parameter 검사
		int classIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(classIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");	// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
	
		
		searchList.add(new DTForm("A." + idxColumnName, classIdx));
		searchList.add(new DTForm("B." +"MASTER_CODE", masterCode));
		param.put("searchList", searchList);
		
		Map<String, Object> param1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(new DTForm("A." +"MASTER_CODE", masterCode));
		param1.put("searchList", searchList1);
		List<?> ordList = classOptnService.getOptnList(param1);
		
		Map<String, Object> param2 = new HashMap<String, Object>();
		List<DTForm> searchList2 = new ArrayList<DTForm>();
		searchList2.add(new DTForm("A." + idxColumnName, classIdx));
		param2.put("searchList", searchList2);
		param2.put("fnIdx", fnIdx);
		// 2.1 상세정보
		dt = tabContentsService.getModify(fnIdx, param);
		System.out.println("===============구분================"+dt);
		
		
		if(dt == null){
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 3. 항목설정
		String submitType = "view";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType +  "_order");
		
		// 4. 속성 setting
		model.addAttribute("dt", dt);	// 상세정보
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));	// 항목코드
		model.addAttribute("multiFileHashMap", tabContentsService.getMultiFileHashMap(fnIdx, classIdx, items, itemOrder));	// multi file 목록
		model.addAttribute("tabData",tabcontentsDataDAO.getList(param2));
		model.addAttribute("submitType", submitType);	// 페이지구분
		// 5. 기본경로
		fn_setCommonPath(attrVO);
		// 6. form action
		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
		model.addAttribute("ordList", ordList);
		request.setAttribute("tabCnt", 10);
		request.setAttribute("dt", dt);
		return getViewPath("/view");
		
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
	@RequestMapping(value="/input.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception{
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		
		String masterCode = JSONObjectUtil.getString(settingInfo, "dset_cate_master_code");
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		int classIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(!isModify || classIdx <= 0){
			return RbsProperties.getProperty("Globals.error.400" +  ajaxPName + ".path");
		}
		
		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");	// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
	
		
		searchList.add(new DTForm("A." + idxColumnName, classIdx));
		searchList.add(new DTForm("B." +"MASTER_CODE", masterCode));
		param.put("searchList", searchList);
		
		Map<String, Object> param1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(new DTForm("A." +"MASTER_CODE", masterCode));
		param1.put("searchList", searchList1);
		List<?> ordList = classOptnService.getOptnList(param1);
		
		Map<String, Object> param2 = new HashMap<String, Object>();
		List<DTForm> searchList2 = new ArrayList<DTForm>();
		searchList2.add(new DTForm("A." + idxColumnName, classIdx));
		param2.put("searchList", searchList2);
		param2.put("fnIdx", fnIdx);
		// 2.1 상세정보
		dt = tabContentsService.getModify(fnIdx, param);
		
		
		
		if(dt == null){
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 3. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType +  "_order");
		
		// 4. 속성 setting
		model.addAttribute("dt", dt);	// 상세정보
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));	// 항목코드
		model.addAttribute("multiFileHashMap", tabContentsService.getMultiFileHashMap(fnIdx, classIdx, items, itemOrder));	// multi file 목록
		model.addAttribute("tabData",tabcontentsDataDAO.getList(param2));
		model.addAttribute("submitType", submitType);	// 페이지구분
		// 5. 기본경로
		fn_setCommonPath(attrVO);
		// 6. form action
		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
		model.addAttribute("ordList", ordList);
		request.setAttribute("tabCnt", 10);
		request.setAttribute("dt", dt);
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
	@RequestMapping(method=RequestMethod.POST, value="/inputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception{
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		String siteMode=attrVO.getSiteMode();
		String uploadModulePath = attrVO.getUploadModulePath();
		boolean isAdmMode = attrVO.isAdmMode();
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(isModify && brdIdx <= 0){
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 3. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder});
		if(!StringUtil.isEmpty(errorMessage)){
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
		Map<String, Object> param = new HashMap<String, Object>();
		List<?> list = null;
		list = tabContentsService.getList(fnIdx, param);
		
		int result = tabContentsService.update(uploadModulePath, fnIdx, brdIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
		if(result > 0){
			// 저장 성공
			// 5. 기본경로
			fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" +  ajaxPName + ".path");
		}else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		request.setAttribute("tabCnt", 10);
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
	@RequestMapping(method=RequestMethod.POST, value="/deleteProc.do", params="select")
	public String delete(@RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception{
		JSONObject settingInfo = attrVO.getSettingInfo();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		String masterCode = JSONObjectUtil.getString(settingInfo, "dset_cate_master_code");
		
		// 1. DB
		int result = tabContentsService.delete(fnIdx, deleteIdxs, request.getRemoteAddr(),masterCode);
		
		if(result > 0){
			// 저장 성공
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.delete"), "top.location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
		return "";
	}

	/**
	 * 삭제목록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value="/deleteList.do")
	public String deleteList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception{
		
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
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, "list_search");
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null){
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);
		
		// 2.2 목록수
		totalCount = tabContentsService.getDeleteCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
		
		if(totalCount > 0){
			param.put("firstIndex", paginationInfo.getFirstRecordIndex());
			param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
			
			// 2.3 목록
			list = tabContentsService.getDeleteList(fnIdx, param);
		}
		
		// 3. 속성 setting
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("list", list);
		model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));
		
		// 4. 휴지통 경로
		fn_setDeleteListPath(attrVO);
		
		return getViewPath("/deleteList");
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
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		String masterCode = JSONObjectUtil.getString(settingInfo, "dset_cate_master_code");
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		
		// 1. DB
		int result = tabContentsService.restore(fnIdx, restoreIdxs, request.getRemoteAddr(), masterCode);
    	
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
		JSONObject settingInfo = attrVO.getSettingInfo();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		String masterCode = JSONObjectUtil.getString(settingInfo, "dset_cate_master_code");
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 2. DB
		int result = tabContentsService.cdelete(uploadModulePath, fnIdx, deleteIdxs, items, itemOrder, masterCode);
    	
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
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		
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
