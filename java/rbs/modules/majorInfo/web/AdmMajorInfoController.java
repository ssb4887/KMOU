package rbs.modules.majorInfo.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

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
import com.woowonsoft.egovframework.util.AuthHelper;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.modules.code.serviceOra.CodeOptnServiceOra;
import rbs.modules.major.service.MajorService;
import rbs.modules.majorInfo.service.MajorInfoService;
import rbs.modules.sbjt.serviceOra.SbjtServiceOra;

/**
 * 샘플모듈<br/>
 * : 통합관리시스템 > 메뉴콘텐츠관리, 통합관리시스템 > 기능등록관리, 사용자 사이트 에서 사용
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="majorInfo")
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/majorInfo", "/{siteId}/majorInfo"})
public class AdmMajorInfoController extends ModuleController {
	
	@Resource(name = "majorInfoService")
	private MajorInfoService majorInfoService;
	
	@Resource(name = "sbjtServiceOra")
	protected SbjtServiceOra sbjtServiceOra;
	
	@Resource(name = "majorService")
	private MajorService majorService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name = "codeOptnServiceOra")
	protected CodeOptnServiceOra codeOptnServiceOra;
	
	@Resource(name = "jsonView")
	View jsonView;
	
	
	/**
	 * 코드
	 * @param submitType
	 * @param itemInfo
	 * @return
	 */
	public HashMap<String, Object> getOptionHashMap(String submitType, JSONObject itemInfo) {
		return getOptionHashMap(submitType, itemInfo, null);
	}
	
	/**
	 * 코드
	 * @param submitType
	 * @param itemInfo
	 * @param addOptionHashMap
	 * @return
	 */
	public HashMap<String, Object> getOptionHashMap(String submitType, JSONObject itemInfo, HashMap<String, Object> addOptionHashMap) {
		// 코드
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		HashMap<String, Object> optnHashMap = (addOptionHashMap != null)?CodeHelper.getItemOptnHashMap(items, itemOrder, addOptionHashMap):CodeHelper.getItemOptnHashMap(items, itemOrder);
		return  optnHashMap;
	}
	
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/DepartAjax.json", headers="Ajax")
	public ModelAndView departAjax(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		String univ = request.getParameter("UNIV");
		
		param.put("univ", univ);
		list = majorInfoService.getDepartList(param);
		
		model.addAttribute("departList", list);
		
		return mav;
	}
	
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/MajorAjax.json", headers="Ajax")
	public ModelAndView majorAjax(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
			
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		String depart = request.getParameter("DEPART");
		
		
		param.put("depart", depart);
		list = majorInfoService.getMajorList(param);
		
		model.addAttribute("majorList", list);
		
		return mav;
	}
	
	/**
	 * 목록조회
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
//			int listUnit = ModuleUtil.getSettingValue(isAdmMode, "dset_list_count", crtMenu, settingInfo, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listUnit = 10;
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
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		//권한 넘버 가져오기
		int userTypeIdx = loginVO.getUsertypeIdx();
		//부서정보 가져오기
		//String deptCd = loginVO.getDeptCd();

		String univ = request.getParameter("college1");
		String depart = request.getParameter("college2");
		String major = request.getParameter("college3");
		
		if(univ != null && !"".equals(univ)) {
			searchList.add(new DTForm("A.COLG_CD", univ));
		}
		if(depart != null && !"".equals(depart)) {
			searchList.add(new DTForm("A.DEPT_CD", depart));
		}
		if(major != null && !"".equals(major)) {
			searchList.add(new DTForm("A.MAJOR_CD", major));
		}
		// 2.1 검색조건

		// 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		
		System.out.println("listSearch : " + listSearch);
		System.out.println("queryString : " + queryString);
		
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		
		
		// 탭코드
		String cateTabItemId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");
		String cateTabColumnId = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "column_id");
		String cateTabMasterCode = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "master_code");
		int cateTabObjectType = JSONObjectUtil.getInt(settingInfo, "dset_cate_tab_object_type");
		// 검색 코드
		HashMap<String, Object> searchOptnHashMap = CodeHelper.getItemOptnSearchHashMap(listSearch, cateTabItemId, cateTabColumnId, cateTabObjectType, cateTabMasterCode);
		// 목록 코드
		HashMap<String, Object> optnHashMap = getOptionHashMap(submitType, itemInfo, searchOptnHashMap);//CodeHelper.getItemOptnHashMap(items, itemOrder, searchOptnHashMap);
		// 탭 검색 조건
		List<DTForm> cateTabSearchList = ModuleUtil.getCateTabSearchList(settingInfo, items, queryString, optnHashMap);// fn_getCateTabSearchList(queryString, settingInfo, items, optnHashMap);
		if(cateTabSearchList != null){
			searchList.addAll(cateTabSearchList);
		}
		
		List<Object> permSustCdList = null;
		
		List<String> permSustCdListForSearch = new ArrayList<String>();
		
		if(userTypeIdx == 46) {
			//param.put("DEPT_CD", deptCd);
			
			//담당전공 listify
			permSustCdList = majorInfoService.getPermSustCdList(param);
			if(!ObjectUtils.isEmpty(permSustCdList)) {
				for(int i = 0; i < permSustCdList.size(); i++) {
					Map<String, Object> temp = (Map<String, Object>) permSustCdList.get(i);
					if(temp != null) {				
						String permCode =  (String) temp.get("PERM_SUST_CD");
						System.out.println(permCode);
						permSustCdListForSearch.add(permCode);
					}
				}
				param.put("permSustCdList",permSustCdListForSearch);			
			}
			
		}else if(userTypeIdx == 45) {
			//searchList.add(new DTForm("A.MJ_CD", loginVO.getMjCd()));
		}
		
		System.out.println("searchList : " + searchList);		
		param.put("searchList", searchList);
		
		// 2.2 목록수
    	totalCount = majorInfoService.getTotalCount(fnIdx, param);
    	
		paginationInfo.setTotalRecordCount(totalCount);

    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    	}
    		
    		// 정렬컬럼 setting
//    		param.put("dsetOrderField", JSONObjectUtil.getString(settingInfo, "dset_order_field"));
    		
    		// 2.3 목록
    		list = majorInfoService.getList(fnIdx, param);
		}
    	
    	Map<String, Object> codeParam = new HashMap<String, Object>();
    	
    	
    	// 3. 속성 setting
//    	model.addAttribute("mjCdList", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 전공코드
    	model.addAttribute("submitType", submitType);
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", searchOptnHashMap);									// 항목코드
		model.addAttribute("optnHashMap", optnHashMap);
		model.addAttribute("userTypeIdx", userTypeIdx);
		model.addAttribute("collegeList", majorInfoService.getCollegeList());
		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/list");
	}	
	
	/**
	 * 목록조회
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/deptList.do")
	public String deptList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
//			int listUnit = ModuleUtil.getSettingValue(isAdmMode, "dset_list_count", crtMenu, settingInfo, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listUnit = 10;
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
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		//권한 넘버 가져오기
		int userTypeIdx = loginVO.getUsertypeIdx();
		//부서정보 가져오기
		//String deptCd = loginVO.getDeptCd();

		
		String majorNm = request.getParameter("is_majorNmKor");
		String univ = request.getParameter("college1");
		String depart = request.getParameter("college2");
		String major = request.getParameter("college3");
		
		if(majorNm != null && !"".equals(majorNm)) {
			param.put("MAJOR_NM", "%" + majorNm + "%");
		}
		if(univ != null && !"".equals(univ)) {
			param.put("COLG", univ);
		}
		if(depart != null && !"".equals(depart)) {
			param.put("DEPART", depart);
		}
		if(major != null && !"".equals(major)) {
			param.put("MAJOR", major);
		}
		
		// 2.1 검색조건

		// 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		
		System.out.println("listSearch : " + listSearch);
		System.out.println("queryString : " + queryString);
		
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		
		System.out.println("itemInfoSearchList : " + itemInfoSearchList);
		
		// 탭코드
		String cateTabItemId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");
		String cateTabColumnId = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "column_id");
		String cateTabMasterCode = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "master_code");
		int cateTabObjectType = JSONObjectUtil.getInt(settingInfo, "dset_cate_tab_object_type");
		// 검색 코드
		HashMap<String, Object> searchOptnHashMap = CodeHelper.getItemOptnSearchHashMap(listSearch, cateTabItemId, cateTabColumnId, cateTabObjectType, cateTabMasterCode);
		// 목록 코드
		HashMap<String, Object> optnHashMap = getOptionHashMap(submitType, itemInfo, searchOptnHashMap);//CodeHelper.getItemOptnHashMap(items, itemOrder, searchOptnHashMap);
		// 탭 검색 조건
		List<DTForm> cateTabSearchList = ModuleUtil.getCateTabSearchList(settingInfo, items, queryString, optnHashMap);// fn_getCateTabSearchList(queryString, settingInfo, items, optnHashMap);
		if(cateTabSearchList != null){
			searchList.addAll(cateTabSearchList);
		}
		
		List<Object> permSustCdList = null;
		
		List<String> permSustCdListForSearch = new ArrayList<String>();
		
		if(userTypeIdx == 46) {
			//param.put("DEPT_CD", deptCd);
			
			//담당전공 listify
			permSustCdList = majorInfoService.getPermSustCdList(param);
			if(!ObjectUtils.isEmpty(permSustCdList)) {
				for(int i = 0; i < permSustCdList.size(); i++) {
					Map<String, Object> temp = (Map<String, Object>) permSustCdList.get(i);
					if(temp != null) {				
						String permCode =  (String) temp.get("PERM_SUST_CD");
						System.out.println(permCode);
						permSustCdListForSearch.add(permCode);
					}
				}
				param.put("permSustCdList",permSustCdListForSearch);			
			}
			
		}else if(userTypeIdx == 45) {
			//searchList.add(new DTForm("A.MJ_CD", loginVO.getMjCd()));
		}
		
		System.out.println("searchList : " + searchList);		
		param.put("searchList", searchList);
		
		// 2.2 목록수
    	totalCount = majorInfoService.getDeptCount(fnIdx, param);
    	
		paginationInfo.setTotalRecordCount(totalCount);

    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    	}
    		
    		// 정렬컬럼 setting
//    		param.put("dsetOrderField", JSONObjectUtil.getString(settingInfo, "dset_order_field"));
    		
    		// 2.3 목록
    		list = majorInfoService.getDeptList(fnIdx, param);
		}
    	
    	Map<String, Object> codeParam = new HashMap<String, Object>();
    	
    	
    	// 3. 속성 setting
//    	model.addAttribute("mjCdList", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 전공코드
    	model.addAttribute("submitType", submitType);
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", searchOptnHashMap);									// 항목코드
		model.addAttribute("optnHashMap", optnHashMap);
		model.addAttribute("userTypeIdx", userTypeIdx);
		model.addAttribute("collegeList", majorInfoService.getCollegeList());
		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/deptList");
	}	
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="WRT")
	@RequestMapping(value = "/deptInput.do")
	public String deptInput(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		
		
		// 1. 속성 setting
		// 1.1 항목설정
		String submitType = "writeDept";
		model.addAttribute("optnHashMap", getOptionHashMap(submitType, itemInfo));
		model.addAttribute("submitType", submitType);
		model.addAttribute("collegeList", sbjtServiceOra.getCollegeList());
		model.addAttribute("departList", sbjtServiceOra.getDepartList(null));
		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));

    	// 2. 기본경로
    	fn_setCommonPath(attrVO);

    	// 3. form action
    	
    	Map<String, Object> codeParam = new HashMap<String, Object>();
    	
    	
		return getViewPath("/deptInput");
	}
	
	/**
	 * 수정 : 관리권한 없는 경우 - 자신이 등록한 글만 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name={"WRT"})
	@RequestMapping(value = "/deptInput.do", params="mode")
	public String deptInput(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();

		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
//		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String keyMajorIdx = StringUtil.getString(request.getParameter("DEPT_CD"));
		//int keyYearIdx = StringUtil.getInt(request.getParameter("year"));
		System.out.println(request.getParameter("DEPT_CD"));
		System.out.println(request.getParameter("year"));
		System.out.println(mode);
//		int keyStatisticIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_statistic_name")), 0);
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;

		if(keyMajorIdx == null || !isModify) {
			System.out.println("여기왔니");
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		/*if(keyMajorIdx == null || keyYearIdx <= 0 || !isModify) {
			System.out.println("여기왔니");
			return RbsProperties.getProperty("Globals.error.400.path");
		}*/
		
		// 2. DB
//		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명 이거 안쓸거임..
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
//		String idxStatisticColumnName = JSONObjectUtil.getString(settingInfo, "idx_statistic_column");		// key column명
		
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		
		// 2.1 수정권한 체크
		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, keyMajorIdx); // keyIdx > keyMajorIdx
		if(!isMngAuth) {
			// 수정권한 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		

		//searchList.add(new DTForm("A.DEPT_CD" , keyMajorIdx));
		
		//param.put("searchList", searchList);
		
		param.put("DEPT_CD", keyMajorIdx);
		
		// 2.2 상세정보
		dt = majorInfoService.getDeptModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		// 3 속성 setting
		// 3.1 항목설정
		String submitType = "writeDept";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 3.2 속성 setting
		model.addAttribute("dt", dt);
//		model.addAttribute("multiFileHashMap", majorInfoService.getMultiFileHashMap(fnIdx, keyIdx, settingInfo, items, itemOrder));	// multi file 목록
//		model.addAttribute("multiDataHashMap", majorInfoService.getMultiHashMap(fnIdx, keyIdx, items, itemOrder));		// multi data 목록
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		
		String majorIdx = StringUtil.getString(request.getParameter("DEPT_CD"));
		String yearIdx = StringUtil.getString(request.getParameter("year"));
		
		// 4. 기본경로
//    	fn_setCommonPath(attrVO);
    	fn_setCommonPath(attrVO, majorIdx, yearIdx);
    	
    	Map<String, Object> codeParam = new HashMap<String, Object>();
    	//List<Object> haksaCode = codeOptnServiceOra.getHaksaAllCode(codeParam);
    	//model.addAttribute("haksaCode", haksaCode);
    	
    	// 5. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_DEPT_INPUT_PROC"));
		
    	return getViewPath("/deptInput");
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
	@ModuleAuth(name={"WRT"})
	@RequestMapping(method=RequestMethod.POST, value = "/deptInputProc.do", params="mode")
	public String deptInputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		uploadModulePath = "major" + File.separator + "1";
		int fnIdx = attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		
		//##############
		//##############
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
//		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String keyMajorIdx = StringUtil.getString(request.getParameter("deptCd"));
		//int keyYearIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_year_name")), 0);
		String isDelete = StringUtil.getString(request.getParameter("useFg"));
		String deptLevel = StringUtil.getString(request.getParameter("deptLevel"));
		if(StringUtil.isEquals(isDelete, "0")) {
			isDelete = "1";
		}else {
			isDelete = "0";
		}
		
		Enumeration<String> paramKeys = request.getParameterNames();
		while (paramKeys.hasMoreElements()) {
		     String key = paramKeys.nextElement();
		     System.out.println(key+":"+request.getParameter(key));
		}
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;

		if(keyMajorIdx == null || !isModify) {
			System.out.println("여기왔니");
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		// 2.1 상세정보 - 해당글이 없는 경우 return
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		
		if(StringUtil.isEquals(deptLevel, "2")) {
			searchList.add(new DTForm("COLG_CD", keyMajorIdx));
		}
		if(StringUtil.isEquals(deptLevel, "3")) {
			searchList.add(new DTForm("UP_CD", keyMajorIdx));
		}
		if(StringUtil.isEquals(deptLevel, "4")) {
			searchList.add(new DTForm("DEPT_CD", keyMajorIdx));
		}
		//searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
		param.put("searchList", searchList);
		param.put("DEPT_CD", keyMajorIdx);
		dt = majorInfoService.getDeptModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		
		// 2.2 전체관리/완전삭제 권한 체크 - 수정권한 없는 경우 return
//		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, keyMajorIdx);
//		if(!isMngAuth) {
//			// 수정권한 없는 경우
//			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
//			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
//		}
		
		// 3. 필수입력 체크
		// 3.1 항목설정
		String submitType = "modify";		// 항목 order명
		String inputFlag = "modify";		// 항목설정 중 write_type, modify_type 구분
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		JSONArray itemYearOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Yearproc_order");
		JSONArray itemStatisticOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Statisticproc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		param.put("DEPT_CD", keyMajorIdx);
		param.put("ISDELETE", isDelete);
		param.put("DEPT_LEVEL", deptLevel);
		// 3.2 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
    	int result = majorInfoService.deptUpdate(uploadModulePath, fnIdx, param, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
//    	result = majorInfoService.updateYear(uploadModulePath, fnIdx, keyMajorIdx, keyYearIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemYearOrder);
    	
    	
    	if(result > 0) {
    		// 4.1 저장 성공
    		
			String majorIdx = StringUtil.getString(request.getParameter("mjCd"));
			String yearIdx = StringUtil.getString(request.getParameter("year"));
			
			// 4. 기본경로
			fn_setCommonPath(attrVO, majorIdx, yearIdx);
			System.out.println(StringUtil.getString(request.getAttribute("URL_INPUT")));
//			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procWReload();"));
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath("deptList.do?mId=68") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 4.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 4.3 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	/**
	 * 목록조회
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/majorList.do")
	public String majorList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
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
		
		String keyMajorIdx = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_major_name")));
		String keyYearIdx = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_year_name")));		
		
		// 2. DB
		
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");
		
		// 2. DB
		int totalCount = 0;
		List<?> list = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		
		System.out.println("listSearch : " + listSearch);
		System.out.println("queryString : " + queryString);
		
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		
		// 3 속성 setting
		// 3.1 항목설정
		String submitType = "majorList";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		System.out.println("mjCd_tmp_multi :: " + queryString.get("mjCd_tmp_multi"));
		System.out.println("isYear :: " + queryString.get("isYear"));


		
		
		String[] mjCdTmpMultis;
		
		//전공정보 세팅(기본정보에서 넘어온 데이터로 검색)
		searchList.add(new DTForm("A." + idxMajorColumnName, keyMajorIdx));
		//searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
		
		param.put("searchList", searchList);
		
		DataMap dt = null;
		dt = majorInfoService.getModify(fnIdx, param);

		
		param.put("MAJOR_CD", keyMajorIdx);
		//param.put("YEAR", keyYearIdx);
		
		List<Object> fieldList = majorInfoService.getFieldList(param);
		
		model.addAttribute("fieldList",fieldList);
		

		// 3.2 속성 setting
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		model.addAttribute("dt", dt);											// 페이지구분
		
		String majorCd = StringUtil.getString(request.getParameter("majorCd"));
		String year = StringUtil.getString(request.getParameter("year"));
		System.out.println("request : " + request);
		
		System.out.println("searchList : " + searchList);
		param.put("searchList", searchList);
		
		fn_setCommonPath(attrVO, keyMajorIdx, keyYearIdx);
		
		// DB 값 가져오기
		List<Object> result = majorInfoService.getCourMajorList(fnIdx,param);
		model.addAttribute("list", result);
		
		return getViewPath("/courMajorList");
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
	@ModuleAuth(name={"WRT"})
	@RequestMapping(method=RequestMethod.POST, value = "/rcmdCultInputProc.do")
	public String rcmdCultInputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		uploadModulePath = "rcmdCult" + File.separator + "1";
		int fnIdx = attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		
		
		String mjCd = request.getParameter("mjCd");
		String year = request.getParameter("year");

//		Enumeration<String> paramKeys = request.getParameterNames();
//		while (paramKeys.hasMoreElements()) {
//		     String key = paramKeys.nextElement();
//		     System.out.println(key+":"+request.getParameter(key));
//		}
		
		System.out.println("keyMajorIdx ："+mjCd);
		System.out.println("keyYearIdx ："+year);
		

		// 2. DB
		// 2.1 상세정보 - 해당글이 없는 경우 return
		
		
		DataMap dt = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + "MJ_CD", mjCd));
		searchList.add(new DTForm("A." + "YY", year));
		
		param.put("searchList", searchList);

		
		
		dt = majorInfoService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		
		param.put("searchList", searchList);
		
		// 3. 필수입력 체크
		// 3.1 항목설정
		String submitType = "rcmdCultModify";		// 항목 order명
		String inputFlag = "modify";		// 항목설정 중 write_type, modify_type 구분
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		
		// 3.2 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
				
		
		// 4. DB
    	/*int result = majorInfoService.updateCourMajor(keyMajorIdx, keyYearIdx, uploadModulePath, fnIdx, param, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);*/
    	int result = majorInfoService.updateRcmdCult(mjCd, StringUtil.getInt(year), uploadModulePath, fnIdx, param, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);

    	
    	if(result > 0) {
    		// 4.1 저장 성공
    		
        	// 기본경로
        	
        	fn_setCommonPath(attrVO,mjCd, request.getParameter("year"));
        	
        	model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_RCMD_CULT_LIST"))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 4.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 4.3 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
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
	@ModuleAuth(name={"WRT"})
	@RequestMapping(method=RequestMethod.POST, value = "/courMajorInputProc.do")
	public String courMajorInputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		uploadModulePath = "major" + File.separator + "1";
		int fnIdx = attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
//		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
		
		String keyMajorIdx = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_major_name")));
		int keyYearIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_year_name")), 0);

		Enumeration<String> paramKeys = request.getParameterNames();
		while (paramKeys.hasMoreElements()) {
		     String key = paramKeys.nextElement();
		     System.out.println(key+":"+request.getParameter(key));
		}
		
		System.out.println("keyMajorIdx ："+keyMajorIdx);
		System.out.println("keyYearIdx ："+keyYearIdx);
		

		// 2. DB
		// 2.1 상세정보 - 해당글이 없는 경우 return
		
		
		DataMap dt = null;
		HashMap<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxMajorColumnName, keyMajorIdx));
		//searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
		
		param.put("searchList", searchList);

		dt = majorInfoService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		
		param.put("searchList", searchList);
		
		// 3. 필수입력 체크
		// 3.1 항목설정
		String submitType = "courMajorModify";		// 항목 order명
		String inputFlag = "modify";		// 항목설정 중 write_type, modify_type 구분
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		
		// 3.2 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
				
		
		// 4. DB
    	int result = majorInfoService.updateCourMajor(keyMajorIdx, keyYearIdx, uploadModulePath, fnIdx, param, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);

    	
    	if(result > 0) {
    		// 4.1 저장 성공
    		
        	// 기본경로
        	
        	fn_setCommonPath(attrVO,keyMajorIdx, request.getParameter("year"));
        	
        	model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_MAJOR_LIST"))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 4.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 4.3 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/abilityList.do")
	public String abilityList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
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
//		int totalCount = 0;
		int jobCount = 0;
		List<?> list = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
		String keyMajorIdx = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_major_name")));
		int keyYearIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_year_name")), 0);
		
		searchList.add(new DTForm("A." + idxMajorColumnName, keyMajorIdx));
		searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
//		searchList.add(new DTForm("C." + idxStatisticColumnName, keyStatisticIdx));
		
		param.put("searchList", searchList);
		
		
		// 2.2 목록수
//		totalCount = majorInfoService.getTotalCount(fnIdx, param);
		jobCount = majorInfoService.getAbilityCount(fnIdx, param);
		
		paginationInfo.setTotalRecordCount(jobCount);
		
		if(jobCount > 0) {
			if(usePaging == 1) {
				param.put("firstIndex", paginationInfo.getFirstRecordIndex());
				param.put("lastIndex", paginationInfo.getLastRecordIndex());
				param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
			}
			
			// 정렬컬럼 setting
			param.put("dsetOrderField", JSONObjectUtil.getString(settingInfo, "dset_order_field"));
			
			// 2.3 목록
			list = majorInfoService.getAbilityList(fnIdx, param);
		}
		
		
		// 3. 속성 setting
		model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
		model.addAttribute("list", list);															// 목록
		
		String majorIdx = StringUtil.getString(request.getParameter("majorIdx"));
		String yearIdx = StringUtil.getString(request.getParameter("yearIdx"));
		
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		fn_setCommonPath(attrVO, majorIdx, yearIdx);
		
		return getViewPath("/abilityList");
	}
	
	/**
	 * 추천균형교양교과목
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/rcmdCultList.do")
	public String rcmdCultList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
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
		
		String keyMajorIdx = StringUtil.getString(request.getParameter("mjCd"));
		String keyYearIdx = StringUtil.getString(request.getParameter("year"));
		String sbjtNmKor = StringUtil.getString(request.getParameter("sbjtKorNm"));
		String isEduCorsCapbFg = StringUtil.getString(request.getParameter("is_eduCorsCapbFg"));
		
		
		System.out.println("keyMajorIdx :::" + keyMajorIdx);
		System.out.println("keyYearIdx :::" + keyYearIdx);
		
		// 2. DB
		
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");
		
		
		
		// 2. DB
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		
		System.out.println("listSearch : " + listSearch);
		System.out.println("queryString : " + queryString);
		
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		
		// 3 속성 setting
		// 3.1 항목설정
		String submitType = "majorList";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");				
		
		//전공정보 세팅(기본정보에서 넘어온 데이터로 검색)
		searchList.add(new DTForm("A." + idxMajorColumnName, keyMajorIdx));
		searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
		
		param.put("searchList", searchList);
		
		DataMap dt = null;
		dt = majorInfoService.getModify(fnIdx, param);

		
		param.put("OPEN_SUST_MJ_CD", keyMajorIdx);
		param.put("YY", keyYearIdx);
		param.put("SBJT_NM_KOR", sbjtNmKor);
		param.put("EDU_CORS_CAPB_FG", isEduCorsCapbFg);
		

		// 3.2 속성 setting
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		model.addAttribute("dt", dt);											// 페이지구분
			
		
		param.put("searchList", searchList);
		
		fn_setCommonPath(attrVO, keyMajorIdx, keyYearIdx);
		
		// DB 값 가져오기
		List<Object> result = majorInfoService.getRcmdCultList(fnIdx,param);
		model.addAttribute("list", result);
		
		return getViewPath("/rcmdCultList");
	}	
	
	
	/**
	 * 전공정보능력 관리 목록
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/abtyMngList.do")
	public String abtyMngList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();		
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		JSONObject crtMenu = attrVO.getCrtMenu();

		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
		String yearIdx = StringUtil.getString(request.getParameter("yearIdx"));
		
		
		

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
		
		DataMap basicInfoList = null;
		List<Object> abtyList = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		// 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		
		
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		
		
		/*searchList.add(new DTForm("YY", yearIdx));
		searchList.add(new DTForm("OPEN_SUST_MJ_CD", majorIdx));*/
		/*searchList.add(new DTForm("SBJT_KOR_NM", sbjtKorNm));*/
		
		param.put("YEAR",yearIdx);
		param.put("MAJOR_CD", majorIdx);
		
		param.put("searchList", searchList);
		
		
		// 2.2 상세정보
		abtyList = majorInfoService.getAbtyMngList(param);
		

		// 3.2 속성 setting
		model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
		model.addAttribute("abtyList", abtyList);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		model.addAttribute("majorCd",majorIdx);
		model.addAttribute("year",yearIdx);
    	
    	// 4. 기본경로
		fn_setCommonPath(attrVO, majorIdx, yearIdx);
    	
    	return getViewPath("/abtyMngList");
	}
	
	
	
	
	/**
	 * 정보능력 관리 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name={"WRT"})
	@RequestMapping(method=RequestMethod.POST, value = "/abtyMngInputProc.do")
	public String abtyMngInputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		boolean isAdmMode = attrVO.isAdmMode();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		uploadModulePath = "major" + File.separator + "1";
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 필수입력 체크
		// 1.1 항목설정
		String submitType = "writeAbty";		// 항목 order명
		String inputFlag = "writeAbty";			// 항목설정 중 write_type, modify_type 구분
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		String modi = StringUtil.getObjectToString(parameterMap.get("modi"));
		System.out.println("line 1001 :  진입 체크 ");
		
		// 1.2 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 2. DB
		
		int result = 0;
		
		// 여기서 중복 체크!!!!!!!!!!!!!!!!!
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> param = new HashMap<String, Object>();
		
		System.out.println("parameterMap" + parameterMap);
		
//		int isDupl = 1;
		

//		dataList.add(new DTForm("A.YEAR", parameterMap.get("year") ));
//		dataList.add(new DTForm("A.MJ_CD", parameterMap.get("mjCd") ));
		
//		param.put("searchList", dataList);
//		isDupl = majorInfoService.getMajorCount(fnIdx, param);
//		
//		
//		
//		
//		if(isDupl > 0) {
//			model.addAttribute("message", MessageUtil.getAlert(isAjax, "해당 년도의 정보가 이미 있습니다." ));
//			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
//		}
			
		////
		int major_idx = 0;
		if(StringUtil.isEquals(modi, "")) {
			major_idx = majorInfoService.abtyInsert(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
		} else {
			major_idx = majorInfoService.abtyUpdate(uploadModulePath, fnIdx, param, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
		}
		
//		int year_idx = majorInfoService.insertYear(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemYearOrder, major_idx);
		
		if( major_idx > 0  ) {
			// 2.1 저장 성공
			// 기본경로
			// 4.1 저장 성공
			
						
			fn_setCommonPath(attrVO);
			String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
			String yearIdx = StringUtil.getString(request.getParameter("year"));
			
			// 4. 기본경로
			fn_setCommonPath(attrVO, majorIdx, yearIdx);
//        	String inputNpname = fn_dsetInputNpname(settingInfo);
			String toUrl = PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_ABTY_MNG_LIST")));
			toUrl += "&" + JSONObjectUtil.getString(settingInfo, "idx_major_name") + "=" + parameterMap.get("majorCd") + "&dl=1";
					/*+ "&" + JSONObjectUtil.getString(settingInfo, "idx_year_name") + "=" + parameterMap.get("year");*/
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + toUrl + "\", 1);"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		} else if(result == -1) {
			// 2.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
		}
		// 2.3 저장 실패
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
	@RequestMapping(method=RequestMethod.POST, value = "/abtyDeleteProc.do", params="select")
	public String abtyDelete(@ParamMap ParamForm parameterMap, @RequestParam(value="select") int[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		
		int fnIdx = attrVO.getFnIdx();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String siteMode = attrVO.getSiteMode();
		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		
		// 1. DB
		int result = majorInfoService.abtyDelete(parameterMap, settingInfo, fnIdx, deleteIdxs, siteMode, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
    		fn_setCommonPath(attrVO);
			String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
			String yearIdx = StringUtil.getString(request.getParameter("year"));
			
			// 4. 기본경로
			fn_setCommonPath(attrVO, majorIdx, yearIdx);
//        	String inputNpname = fn_dsetInputNpname(settingInfo);
			String toUrl = PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_ABTY_MNG_LIST")));
			toUrl += "&" + JSONObjectUtil.getString(settingInfo, "idx_major_name") + "=" + parameterMap.get("majorCd") + "&dl=1";
					/*+ "&" + JSONObjectUtil.getString(settingInfo, "idx_year_name") + "=" + parameterMap.get("year");*/
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + toUrl + "\", 1);"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	
	/* 비교과 입력 및 수정 */
	
	@ModuleAuth(name={"WRT"})
	@RequestMapping(value = "/nonSbjtInput.do", params="mode")
	public String nonSbjtInput(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		System.out.println("/nonSbjtInput.do\", params=\"mode");
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
		String yearIdx = StringUtil.getString(request.getParameter("year"));
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;
		
		
		// 2. DB
//		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명 이거 안쓸거임..
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
//		String idxStatisticColumnName = JSONObjectUtil.getString(settingInfo, "idx_statistic_column");		// key column명
		
		DataMap dt = null;
		List<Object> listDt = null;
		List<Object> codeDt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		
		// 2.1 수정권한 체크
		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, majorIdx); // keyIdx > keyMajorIdx
		if(!isMngAuth) {
			// 수정권한 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
//		searchList.add(new DTForm("A." + idxColumnName, keyIdx));
		searchList.add(new DTForm("A." + idxMajorColumnName, majorIdx));
//		searchList.add(new DTForm("A." + idxYearColumnName, yearIdx));
//		searchList.add(new DTForm("C." + idxStatisticColumnName, keyStatisticIdx));
		
		param.put("searchList", searchList);
		//param.put("YY",yearIdx);
		param.put("MAJOR_CD",majorIdx);
		
		// 2.2 상세정보
		dt = majorInfoService.getModify(fnIdx, param);
		codeDt = majorInfoService.getFieldList(param);
		listDt = majorInfoService.getNonSbjtList(param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		// 3 속성 setting
		// 3.1 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Job_order");
		
		// 3.2 속성 setting
		model.addAttribute("dt", dt);
		model.addAttribute("code", codeDt);
		model.addAttribute("list", listDt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		

		
		// 4. 기본경로
//    	fn_setCommonPath(attrVO);
		
		fn_setCommonPath(attrVO, majorIdx, yearIdx);
		
		
		// 5. form action
//		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_JOB_MODIFY_PROC"));
		
		return getViewPath("/nonSbjtInput");
	}
	
	@ModuleAuth(name={"WRT"})
	@RequestMapping(method=RequestMethod.POST, value = "/nonSbjtInputProc.do", params="mode")
	public String nonSbjtInputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		System.out.println("비교과입력");
		//##############
		//##############
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
//		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String keyMajorIdx = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_major_name")));
		int keyYearIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_year_name")), 0);
		
//		Enumeration<String> paramKeys = request.getParameterNames();
//		while (paramKeys.hasMoreElements()) {
//			String key = paramKeys.nextElement();
//			System.out.println(key+":"+request.getParameter(key));
//		}
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;
		
		if(keyMajorIdx == null || !isModify) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		/*if(keyMajorIdx == null || keyYearIdx <= 0 || !isModify) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}*/
		
		// 2. DB
		// 2.1 상세정보 - 해당글이 없는 경우 return
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		//String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명		
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm(idxMajorColumnName, keyMajorIdx));
		//searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
		param.put("searchList", searchList);
		
//		dt = majorInfoService.getModifyAbility(fnIdx, param);
//		if(dt == null) {
//			// 해당글이 없는 경우
//			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
//			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
//		}
		
		
		// 2.2 전체관리/완전삭제 권한 체크 - 수정권한 없는 경우 return
		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, keyMajorIdx);
		if(!isMngAuth) {
			// 수정권한 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. 필수입력 체크
		// 3.1 항목설정
		String submitType = "modifyNonMajor";		// 항목 order명
		String inputFlag = "modifyNonMajor";		// 항목설정 중 write_type, modify_type 구분
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemJobOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Jobproc_order");
		JSONArray itemDtlOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Dtlproc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		
		// 3.2 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemJobOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
//		int result = majorInfoService.update(uploadModulePath, fnIdx, keyMajorIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
//		result = majorInfoService.updateYear(uploadModulePath, fnIdx, keyMajorIdx, keyYearIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemYearOrder);
		int result = majorInfoService.deleteAndInsertNonSbjt(searchList, request.getRemoteAddr(), parameterMap);
		// 업데이트 긴한데, 여러개가 올 수 있는 만큼 ( 지웠다가 다시 다 집어넣는 방식으로 간다고 가정 - 맞는지 모름 )
		// 여기에 지우는 로직 필요
//		result = majorInfoService.deleteDtl(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemDtlOrder, keyMajorIdx, keyYearIdx, keyJobIdx);
//		result = majorInfoService.insertDtl(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemDtlOrder, keyMajorIdx, keyYearIdx, keyJobIdx);
		
		
//		int result = 1;
		if(result > 0) {
			// 4.1 저장 성공
			
			// 기본경로
			fn_setCommonPath(attrVO);
			String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
			String yearIdx = StringUtil.getString(request.getParameter("year"));
			
			// 4. 기본경로
			fn_setCommonPath(attrVO, majorIdx, yearIdx);
			
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_NON_SBJT_INPUT"))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		} else if(result == -1) {
			// 4.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
		}
		// 4.3 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	
	/* 자격증 입력 및 수정 */
	
	@ModuleAuth(name={"WRT"})
	@RequestMapping(value = "/licenseInput.do", params="mode")
	public String licenseInput(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		System.out.println("/licenseInput.do\", params=\"mode");
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
		String yearIdx = StringUtil.getString(request.getParameter("year"));
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;
		
		
		// 2. DB
//		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명 이거 안쓸거임..
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
//		String idxStatisticColumnName = JSONObjectUtil.getString(settingInfo, "idx_statistic_column");		// key column명
		
		DataMap dt = null;
		List<Object> listDt = null;
		List<Object> codeDt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		
		// 2.1 수정권한 체크
		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, majorIdx); // keyIdx > keyMajorIdx
		if(!isMngAuth) {
			// 수정권한 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
//		searchList.add(new DTForm("A." + idxColumnName, keyIdx));
		searchList.add(new DTForm("A." + idxMajorColumnName, majorIdx));
//		searchList.add(new DTForm("A." + idxYearColumnName, yearIdx));
//		searchList.add(new DTForm("C." + idxStatisticColumnName, keyStatisticIdx));
		
		param.put("searchList", searchList);
		//param.put("YY",yearIdx);
		param.put("MAJOR_CD",majorIdx);
		
		// 2.2 상세정보
		dt = majorInfoService.getModify(fnIdx, param);
		codeDt = majorInfoService.getFieldList(param);
		listDt = majorInfoService.getLicenseList(param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		// 3 속성 setting
		// 3.1 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Job_order");
		
		// 3.2 속성 setting
		model.addAttribute("dt", dt);
		model.addAttribute("code", codeDt);
		model.addAttribute("list", listDt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		

		
		// 4. 기본경로
//    	fn_setCommonPath(attrVO);
		
		fn_setCommonPath(attrVO, majorIdx, yearIdx);
		
		
		// 5. form action
//		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_JOB_MODIFY_PROC"));
		
		return getViewPath("/licenseInput");
	}
	
	@ModuleAuth(name={"WRT"})
	@RequestMapping(method=RequestMethod.POST, value = "/licenseInputProc.do", params="mode")
	public String licenseInputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		System.out.println("자격증입력");
		//##############
		//##############
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
//		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String keyMajorIdx = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_major_name")));
		int keyYearIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_year_name")), 0);
		
//		Enumeration<String> paramKeys = request.getParameterNames();
//		while (paramKeys.hasMoreElements()) {
//			String key = paramKeys.nextElement();
//			System.out.println(key+":"+request.getParameter(key));
//		}
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;
		
		if(keyMajorIdx == null || !isModify) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		/*if(keyMajorIdx == null || keyYearIdx <= 0 || !isModify) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}*/
		
		// 2. DB
		// 2.1 상세정보 - 해당글이 없는 경우 return
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		//String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명		
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm(idxMajorColumnName, keyMajorIdx));
		//searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
		param.put("searchList", searchList);
		
//		dt = majorInfoService.getModifyAbility(fnIdx, param);
//		if(dt == null) {
//			// 해당글이 없는 경우
//			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
//			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
//		}
		
		
		// 2.2 전체관리/완전삭제 권한 체크 - 수정권한 없는 경우 return
		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, keyMajorIdx);
		if(!isMngAuth) {
			// 수정권한 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. 필수입력 체크
		// 3.1 항목설정
		String submitType = "modify";		// 항목 order명
		String inputFlag = "modify";		// 항목설정 중 write_type, modify_type 구분
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemJobOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Jobproc_order");
		JSONArray itemDtlOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Dtlproc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		
		// 3.2 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemJobOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
//		int result = majorInfoService.update(uploadModulePath, fnIdx, keyMajorIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
//		result = majorInfoService.updateYear(uploadModulePath, fnIdx, keyMajorIdx, keyYearIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemYearOrder);
		int result = majorInfoService.deleteAndInsertLicense(searchList, request.getRemoteAddr(), parameterMap);
		// 업데이트 긴한데, 여러개가 올 수 있는 만큼 ( 지웠다가 다시 다 집어넣는 방식으로 간다고 가정 - 맞는지 모름 )
		// 여기에 지우는 로직 필요
//		result = majorInfoService.deleteDtl(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemDtlOrder, keyMajorIdx, keyYearIdx, keyJobIdx);
//		result = majorInfoService.insertDtl(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemDtlOrder, keyMajorIdx, keyYearIdx, keyJobIdx);
		
		
//		int result = 1;
		if(result > 0) {
			// 4.1 저장 성공
			
			// 기본경로
			fn_setCommonPath(attrVO);
			String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
			String yearIdx = StringUtil.getString(request.getParameter("year"));
			
			// 4. 기본경로
			fn_setCommonPath(attrVO, majorIdx, yearIdx);
			
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_LICENSE_INPUT"))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		} else if(result == -1) {
			// 4.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
		}
		// 4.3 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	
	/**
	 * 학부교육과정 교과목 추가
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/addMajor.do")
	public String addMajor(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();		
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		JSONObject crtMenu = attrVO.getCrtMenu();

		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String majorIdx = StringUtil.getString(request.getParameter("majorIdx"));
		String yearIdx = StringUtil.getString(request.getParameter("yearIdx"));
		
		
		String sbjtNm = StringUtil.getString(request.getParameter("sbjtNm"));

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
		
		DataMap basicInfoList = null;
		List<Object> addMajorList = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		// 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		
		
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		
		
		/*searchList.add(new DTForm("YY", yearIdx));
		searchList.add(new DTForm("OPEN_SUST_MJ_CD", majorIdx));*/
		/*searchList.add(new DTForm("SBJT_KOR_NM", sbjtKorNm));*/
		
		param.put("YEAR",yearIdx);
		param.put("MAJOR_CD",majorIdx);		
		param.put("SUBJECT_NM", sbjtNm);
		
		param.put("searchList", searchList);
		
		
		
		// 2.2 상세정보
		addMajorList = majorInfoService.getAddMajorList(fnIdx, param);
		

		// 3.2 속성 setting
		model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
		model.addAttribute("addMajorList", addMajorList);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		model.addAttribute("maorjCd",majorIdx);
		model.addAttribute("year",yearIdx);
    	
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	return getViewPath("/addMajor");
	}
	
	/**
	 * 추천균형교양교과목 검색
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/rcmdCultInput.do")
	public String rcmdCultInput(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		DataForm queryString = attrVO.getQueryString();
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();		
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		JSONObject crtMenu = attrVO.getCrtMenu();

		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String mjCd = StringUtil.getString(request.getParameter("mjCd"));
		String year = StringUtil.getString(request.getParameter("year"));
		String sbjtNmKor = StringUtil.getString(request.getParameter("sbjtKorNm"));
		String isEduCorsCapbFg = StringUtil.getString(request.getParameter("is_eduCorsCapbFg"));
		
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
		
		DataMap basicInfoList = null;
		List<Object> haksaRcmdCultList = null;
		List<Object> rcmdCultList = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		// 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 항목설정으로 검색조건 setting
//		String listSearchId = "list_search";		// 검색설정
//		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
//		
//		
//		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
//		if(itemInfoSearchList != null) {
//			searchList.addAll(itemInfoSearchList);
//			model.addAttribute("isSearchList", new Boolean(true));
//		}
		
		/*searchList.add(new DTForm("YY", yearIdx));
		searchList.add(new DTForm("OPEN_SUST_MJ_CD", majorIdx));*/
		/*searchList.add(new DTForm("SBJT_KOR_NM", sbjtKorNm));*/
		
		param.put("YY",year);
		param.put("OPEN_SUST_MJ_CD",mjCd);		
		param.put("SBJT_KOR_NM", sbjtNmKor);
		param.put("EDU_CORS_CAPB_FG", isEduCorsCapbFg);
		
		// 2.2 상세정보
		haksaRcmdCultList = majorInfoService.getHaksaRcmdCultList(fnIdx, param);
//		rcmdCultList = majorInfoService.getRcmdCultList(fnIdx, param);
		
		
		

		// 3.2 속성 setting
		model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
		model.addAttribute("haksaRcmdCultList", haksaRcmdCultList);
//		model.addAttribute("rcmdCultList", rcmdCultList);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		model.addAttribute("mjCd",mjCd);
		model.addAttribute("year",year);
    	
    	// 4. 기본경로
    	fn_setCommonPath(attrVO,mjCd,year);
    	
    	return getViewPath("/rcmdCultInput");
	}
	
	/**
	 * 인재상 리스트 불러오기
	 * */
	@RequestMapping(value = "/getFieldList.json",  headers="Ajax")
	public ModelAndView getFieldList(HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		Map<String, Object> param = new HashMap<String, Object>();		
		param.put("YEAR", request.getParameter("year"));
		param.put("MAJOR_CD", request.getParameter("majorCd"));	
		
		List<Object> fieldList = majorInfoService.getFieldList(param);
		
		model.addAttribute("fieldList",fieldList);
	    return mav;
	}
	
	
	/**
	 * 학부교육과정 교과목 팝업
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@ModuleAuth(name="VEW")*/
	@ModuleAuth(name={"WRT"})
	@RequestMapping(value = "/addMajorInput.do", method = RequestMethod.POST)
	public ModelAndView addMajorInput(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setView(jsonView);
		
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();		
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		JSONObject crtMenu = attrVO.getCrtMenu();

		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
		String yearIdx = StringUtil.getString(request.getParameter("year"));
		String sbjtKorNm = StringUtil.getString(request.getParameter("field"));


		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		JSONArray reqJsonArray = reqJsonObj.getJSONArray("data");
		
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		for(int i=0; i < reqJsonArray.size() ; i++) {
			JSONObject reuslt = reqJsonArray.getJSONObject(i);
			param.put("FIELD_CD", reuslt.getString("FIELD_CD"));
			param.put("FIELD", reuslt.getString("FIELD"));
			param.put("YEAR", reuslt.getString("YEAR"));
			param.put("MAJOR_CD", reuslt.getString("MAJOR_CD"));
			param.put("SMT", reuslt.getString("SMT"));
			param.put("SUBJECT_CD", reuslt.getString("SUBJECT_CD"));
			param.put("SUBJECT_NM", reuslt.getString("SUBJECT_NM"));
			param.put("COM_GRADE", reuslt.getString("COM_GRADE"));
			param.put("CDT_NUM", reuslt.getString("CDT_NUM"));
			param.put("DEPT_CD", reuslt.getString("DEPT_CD"));
			param.put("DEPT_NM", reuslt.getString("DEPT_NM"));
			param.put("REGI_ID", loginVO.getMemberId());
			param.put("REGI_IP", request.getRemoteAddr());
			param.put("LAST_MODI_ID", loginVO.getMemberId());
			param.put("LAST_MODI_IP", request.getRemoteAddr());
			//param.put("COMDIV_CODE", reuslt.getString("COMDIV_CODE"));
			//param.put("COMDIV_CODE_NM", reuslt.getString("COMDIV_CODE_NM"));
			
			//param.put("WTIME_NUM", reuslt.getString("WTIME_NUM"));
			//param.put("PTIME_NUM", reuslt.getString("PTIME_NUM"));
			
			// 중복 체크하는 구문 제작
			
			try {
				
				majorInfoService.insertAddMajorList(fnIdx, param);
				
			} catch(Exception e) {
				String message = e.getMessage();
				if (message.indexOf("ORA-00001") > 0) {
					continue;
				}
				mav.addObject("처리중 오류가 발생했습니다. 관리자에게 문의해주세요.");
			}
					
			
		}
		
		
    	
    	return mav;
	}
	
	/**
	 * 추천균형교양교과목 교과목 팝업
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@ModuleAuth(name="VEW")*/
	@ModuleAuth(name={"WRT"})
	@RequestMapping(value = "/rcmdCultInput.do", method = RequestMethod.POST)
	public ModelAndView rcmdCultInput(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setView(jsonView);
		
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();		
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		JSONObject crtMenu = attrVO.getCrtMenu();

		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String majorIdx = StringUtil.getString(request.getParameter("mjCd"));
		String yearIdx = StringUtil.getString(request.getParameter("year"));
		String sbjtKorNm = StringUtil.getString(request.getParameter("sbjtKorNm"));


		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		JSONArray reqJsonArray = reqJsonObj.getJSONArray("data");
		
		Map<String, Object> param = new HashMap<String, Object>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		for(int i=0; i < reqJsonArray.size() ; i++) {
			
			JSONObject reuslt = reqJsonArray.getJSONObject(i);
			param.put("SPT_PSN_CD", reuslt.getString("SPT_PSN_CD"));
			param.put("YY", reuslt.getString("YY"));
			param.put("OPEN_SUST_MJ_CD", reuslt.getString("OPEN_SUST_MJ_CD"));
			param.put("COURSE_NO", reuslt.getString("COURSE_NO"));
			param.put("OPEN_SHYR_FG", reuslt.getString("OPEN_SHYR_FG"));
			param.put("REGI_ID", loginVO.getMemberId());
			param.put("REGI_IP", request.getRemoteAddr());
			param.put("LAST_MODI_ID", loginVO.getMemberId());		
			
			// 중복 체크하는 구문 제작
			
			
			try {
				
				majorInfoService.insertAddMajorList(fnIdx, param);
				
			} catch(Exception e) {
				String message = e.getMessage();
				if (message.indexOf("ORA-00001") > 0) {
					continue;
				}
				mav.addObject("처리중 오류가 발생했습니다. 관리자에게 문의해주세요.");
			}					
			
		}		
    	
    	return mav;
	}
	
	/**
	 * 추천균형교양교과목 추가
	 * insert
	 */
	@RequestMapping(value = "/insertRcmdCult.do", method = RequestMethod.POST)
	public ModelAndView insertRcmdCult(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			int insert = majorInfoService.insertRcmdCult(rawBody, request.getRemoteAddr());
			
			mav.setView(jsonView);
			mav.addObject("result", "DONE");			
		}catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());				
		}
		
		return mav;
	}
	
	/**
	 * 추천균형교양교과목 중복 체크
	 * 검색
	 */
	@RequestMapping(value = "/checkRcmdCult.do", method = RequestMethod.POST)
	public ModelAndView checkRcmdCult(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			int result = majorInfoService.getCourCultCount(rawBody);
			
			mav.setView(jsonView);
			mav.addObject("result", result);			
		}catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", -1);				
			mav.addObject("error", e.getClass().getName());				
		}
		
		return mav;
	}

	/**
	 * 복사
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/copy.do")
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();

		// 3 속성 setting
		// 3.1 항목설정
		String submitType = "copy";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 3.2 속성 setting
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));							// 항목코드
		model.addAttribute("submitType", submitType);																// 페이지구분
		
		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	return getViewPath("/copyView");
	}
	
	/**
	 * 미리보기
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/preview.do")
	public String preview(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();		
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		JSONObject crtMenu = attrVO.getCrtMenu();

		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
		String yearIdx = StringUtil.getString(request.getParameter("year"));
		
		


		if(majorIdx == null ) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		
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
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("MAJOR_CD", majorIdx);
		
		// 전공 상세-학부(과)
		model.addAttribute("info", param);
		
		// 전공 상세-기본정보
		model.addAttribute("majorInfo", majorService.getView(param) != null ? majorService.getView(param) : null);
		
		// 전공 상세-인재상 + 전공능력
		model.addAttribute("majorTalent", majorService.getMajorAbty(param) != null ? majorService.getMajorAbty(param) : null);
		
		// 전공 상세- 전공능력 + 하위역량 정의
		model.addAttribute("checkAbty", majorService.checkAbty(param)); // 하위역량 존재 체크 
		model.addAttribute("majorAbty", majorService.getMajorAbtyDef(param) != null ? majorService.getMajorAbtyDef(param) : null);
		
		// 전공 상세-교육과정
		model.addAttribute("majorSbjt", majorService.getMajorSbjtList(param) != null ? majorService.getMajorSbjtList(param) : null);
		//majorServiceOra.getNewMajorList();
		
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return getViewPath("/preview");
	}
	
	/**
	 * 수정 : 관리권한 없는 경우 - 자신이 등록한 글만 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name={"WRT"})
	@RequestMapping(value = "/input.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();

		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
//		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String keyMajorIdx = StringUtil.getString(request.getParameter("majorCd"));
		//int keyYearIdx = StringUtil.getInt(request.getParameter("year"));
		System.out.println(request.getParameter("mjCd"));
		System.out.println(request.getParameter("year"));
		System.out.println(mode);
//		int keyStatisticIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_statistic_name")), 0);
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;

		if(keyMajorIdx == null || !isModify) {
			System.out.println("여기왔니");
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		/*if(keyMajorIdx == null || keyYearIdx <= 0 || !isModify) {
			System.out.println("여기왔니");
			return RbsProperties.getProperty("Globals.error.400.path");
		}*/
		
		// 2. DB
//		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명 이거 안쓸거임..
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
//		String idxStatisticColumnName = JSONObjectUtil.getString(settingInfo, "idx_statistic_column");		// key column명
		
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		
		// 2.1 수정권한 체크
		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, keyMajorIdx); // keyIdx > keyMajorIdx
		if(!isMngAuth) {
			// 수정권한 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
//		searchList.add(new DTForm("A." + idxColumnName, keyIdx));
		searchList.add(new DTForm("A." + idxMajorColumnName, keyMajorIdx));
		//searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
//		searchList.add(new DTForm("C." + idxStatisticColumnName, keyStatisticIdx));
		
		param.put("searchList", searchList);
		
		// 2.2 상세정보
		dt = majorInfoService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		// 3 속성 setting
		// 3.1 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 3.2 속성 setting
		model.addAttribute("dt", dt);
//		model.addAttribute("multiFileHashMap", majorInfoService.getMultiFileHashMap(fnIdx, keyIdx, settingInfo, items, itemOrder));	// multi file 목록
//		model.addAttribute("multiDataHashMap", majorInfoService.getMultiHashMap(fnIdx, keyIdx, items, itemOrder));		// multi data 목록
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		
		String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
		String yearIdx = StringUtil.getString(request.getParameter("year"));
		
		// 4. 기본경로
//    	fn_setCommonPath(attrVO);
    	fn_setCommonPath(attrVO, majorIdx, yearIdx);
    	
    	Map<String, Object> codeParam = new HashMap<String, Object>();
    	//List<Object> haksaCode = codeOptnServiceOra.getHaksaAllCode(codeParam);
    	//model.addAttribute("haksaCode", haksaCode);
    	
    	// 5. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
		
    	return getViewPath("/input");
	}
	
	@ModuleAuth(name={"WRT"})
	@RequestMapping(value = "/trackInput.do", params="mode")
	public String trackInput(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		System.out.println("/trackInput.do\", params=\"mode");
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
//		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		int keyMajorIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_major_name")), 0);
		int keyYearIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_year_name")), 0);
		int keyTrackIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_track_name")), 0);
//		int keyStatisticIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_statistic_name")), 0);
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;
		
		if(keyMajorIdx <= 0 || keyYearIdx <= 0 || !isModify) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		// 2. DB
//		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명 이거 안쓸거임..
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
		String idxTrackColumnName = JSONObjectUtil.getString(settingInfo, "idx_track_column");		// key column명
//		String idxStatisticColumnName = JSONObjectUtil.getString(settingInfo, "idx_statistic_column");		// key column명
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		
		// 2.1 수정권한 체크
		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, keyMajorIdx); // keyIdx > keyMajorIdx
		if(!isMngAuth) {
			// 수정권한 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
//		searchList.add(new DTForm("A." + idxColumnName, keyIdx));
		searchList.add(new DTForm("A." + idxMajorColumnName, keyMajorIdx));
		searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
		searchList.add(new DTForm("A." + idxTrackColumnName, keyTrackIdx));
//		searchList.add(new DTForm("C." + idxStatisticColumnName, keyStatisticIdx));
		
		param.put("searchList", searchList);
		
		// 2.2 상세정보
		dt = majorInfoService.getModifyTrack(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		// 3 속성 setting
		// 3.1 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Track_order");
		
		// 3.2 속성 setting
		model.addAttribute("dt", dt);
//		model.addAttribute("multiFileHashMap", majorInfoService.getMultiFileHashMap(fnIdx, keyIdx, settingInfo, items, itemOrder));	// multi file 목록
//		model.addAttribute("multiDataHashMap", majorInfoService.getMultiHashMap(fnIdx, keyIdx, items, itemOrder));		// multi data 목록
		model.addAttribute("mjCdList", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 전공코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		
		String majorIdx = StringUtil.getString(request.getParameter("majorIdx"));
		String yearIdx = StringUtil.getString(request.getParameter("yearIdx"));
		
		// 4. 기본경로
//    	fn_setCommonPath(attrVO);
		fn_setCommonPath(attrVO, majorIdx, yearIdx);
		
		
		// 5. form action
		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_TRACK_MODIFY_PROC"));
		model.addAttribute("URL_DELETETRACK_PROC", request.getAttribute("URL_DELETE_TRACK_PROC") + "&trackIdx=" + keyTrackIdx);
		
		return getViewPath("/trackInput");
	}
	
	/* 전공능력 입력 및 수정 */
	
	@ModuleAuth(name={"WRT"})
	@RequestMapping(value = "/abilityInput.do", params="mode")
	public String abilityInput(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		System.out.println("/abilityInput.do\", params=\"mode");
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
		String yearIdx = StringUtil.getString(request.getParameter("year"));
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;
		
		
		// 2. DB
//		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명 이거 안쓸거임..
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
//		String idxStatisticColumnName = JSONObjectUtil.getString(settingInfo, "idx_statistic_column");		// key column명
		
		DataMap dt = null;
		List<Object> listDt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		
		// 2.1 수정권한 체크
		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, majorIdx); // keyIdx > keyMajorIdx
		if(!isMngAuth) {
			// 수정권한 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
//		searchList.add(new DTForm("A." + idxColumnName, keyIdx));
		searchList.add(new DTForm("A." + idxMajorColumnName, majorIdx));
//		searchList.add(new DTForm("A." + idxYearColumnName, yearIdx));
//		searchList.add(new DTForm("C." + idxStatisticColumnName, keyStatisticIdx));
		
		param.put("searchList", searchList);
		//param.put("YY",yearIdx);
		param.put("MAJOR_CD",majorIdx);
		
		// 2.2 상세정보
		dt = majorInfoService.getModify(fnIdx, param);
		listDt = majorInfoService.getAbilityList(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		// 3 속성 setting
		// 3.1 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Job_order");
		
		// 3.2 속성 setting
		model.addAttribute("dt", dt);
		model.addAttribute("list", listDt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		

		
		// 4. 기본경로
//    	fn_setCommonPath(attrVO);
		
		fn_setCommonPath(attrVO, majorIdx, yearIdx);
		
		
		// 5. form action
//		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_JOB_MODIFY_PROC"));
		
		return getViewPath("/abilityInput");
	}
	
	
	/**
	 * 인재상 리스트 불러오기
	 * */
	@RequestMapping(value = "/getAbtyList.json",  headers="Ajax")
	public ModelAndView getAbtyList(HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		List<DTForm> searchList = new ArrayList<DTForm>();
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("YEAR", request.getParameter("year"));
		param.put("MAJOR_CD", request.getParameter("majorCd"));

		searchList.add(new DTForm("A.MAJOR_CD", request.getParameter("majorCd")));

		List<Object> talentAbtyList = majorInfoService.getTalentAbtyList(param);
		param.put("searchList", searchList);
		
		List<Object> talentList = majorInfoService.getAbilityList(1, param);
		//List<Object> talentAbtyList = majorInfoService.getTalentAbtyList(param);
		
		//model.addAttribute("haksaCode",haksaCode);
		model.addAttribute("talentList",talentList);
		model.addAttribute("talentAbtyList",talentAbtyList);
	    return mav;
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="WRT")
	@RequestMapping(value = "/input.do")
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 1. 속성 setting
		// 1.1 항목설정
		String submitType = "write";
		model.addAttribute("optnHashMap", getOptionHashMap(submitType, itemInfo));
		model.addAttribute("submitType", submitType);
		model.addAttribute("collegeList", sbjtServiceOra.getCollegeList());
		model.addAttribute("departList", sbjtServiceOra.getDepartList(null));
		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));

    	// 2. 기본경로
    	fn_setCommonPath(attrVO);

    	// 3. form action
    	
    	Map<String, Object> codeParam = new HashMap<String, Object>();
    	
    	
		return getViewPath("/input");
	}
	
	/* 
	 * 전공별 등록된 연도 가져오기 ajax 통신용
	 */ 
	@RequestMapping(method = RequestMethod.GET, value = "/getRegisteredYear.do")
	public ModelAndView getRegisteredYear( HttpServletRequest request, ModelMap model) throws Exception {
		// 로그인한 회원의 정보 가져오기 
//		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		String mjCd = StringUtil.getString(request.getParameter("mjCd"));
				
    	List<Object> registeredYearList = majorInfoService.getRegisteredYear(mjCd);
		
		ModelAndView mav = new ModelAndView();
		mav.setView(jsonView);
		
		mav.addObject("registeredYearList", registeredYearList);
		
		return mav;
	}
	
	@ModuleAuth(name="WRT")
	@RequestMapping(value = "/trackInput.do")
	public String trackInput(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		
		System.out.println("trackInput.do");
		
		// 1. 속성 setting
		// 1.1 항목설정
		String submitType = "writeTrack";
		model.addAttribute("optnHashMap", getOptionHashMap(submitType, itemInfo));
		model.addAttribute("submitType", submitType);
		
		// 2. 기본경로
		String majorIdx = StringUtil.getString(request.getParameter("majorIdx"));
		String yearIdx = StringUtil.getString(request.getParameter("yearIdx"));
		
		// 4. 기본경로
//    	fn_setCommonPath(attrVO);
		fn_setCommonPath(attrVO, majorIdx, yearIdx);
		
		// 3. form action
		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_TRACK_INPUT_PROC"));
		
		return getViewPath("/trackInput");
	}
	
	@ModuleAuth(name="WRT")
	@RequestMapping(value = "/abilityInput.do")
	public String jobInput(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		
		System.out.println("jobInput.do");
		
		// 1. 속성 setting
		// 1.1 항목설정
		String submitType = "writeJob";
		model.addAttribute("optnHashMap", getOptionHashMap(submitType, itemInfo));
		model.addAttribute("submitType", submitType);
		
		// 2. 기본경로
		String majorIdx = StringUtil.getString(request.getParameter("majorIdx"));
		String yearIdx = StringUtil.getString(request.getParameter("yearIdx"));
		
		// 4. 기본경로
//    	fn_setCommonPath(attrVO);
		fn_setCommonPath(attrVO, majorIdx, yearIdx);
		
		// 3. form action
		model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_JOB_INPUT_PROC"));
		
		return getViewPath("/jobInput");
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
	@ModuleAuth(name={"WRT"})
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		uploadModulePath = "major" + File.separator + "1";
		int fnIdx = attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		
		//##############
		//##############
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
//		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String keyMajorIdx = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_major_name")));
		int keyYearIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_year_name")), 0);
		
		Enumeration<String> paramKeys = request.getParameterNames();
		while (paramKeys.hasMoreElements()) {
		     String key = paramKeys.nextElement();
		     System.out.println(key+":"+request.getParameter(key));
		}
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;

		if(keyMajorIdx == null || keyYearIdx <= 0 || !isModify) {
			System.out.println("여기왔니");
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		// 2.1 상세정보 - 해당글이 없는 경우 return
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxMajorColumnName, keyMajorIdx));
		searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
		param.put("searchList", searchList);
		dt = majorInfoService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		
		// 2.2 전체관리/완전삭제 권한 체크 - 수정권한 없는 경우 return
//		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, keyMajorIdx);
//		if(!isMngAuth) {
//			// 수정권한 없는 경우
//			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
//			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
//		}
		
		// 3. 필수입력 체크
		// 3.1 항목설정
		String submitType = "modify";		// 항목 order명
		String inputFlag = "modify";		// 항목설정 중 write_type, modify_type 구분
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		JSONArray itemYearOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Yearproc_order");
		JSONArray itemStatisticOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Statisticproc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		
		// 3.2 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
    	int result = majorInfoService.update(uploadModulePath, fnIdx, param, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
//    	result = majorInfoService.updateYear(uploadModulePath, fnIdx, keyMajorIdx, keyYearIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemYearOrder);

    	
    	if(result > 0) {
    		// 4.1 저장 성공
    		
			String majorIdx = StringUtil.getString(request.getParameter("mjCd"));
			String yearIdx = StringUtil.getString(request.getParameter("year"));
			
			// 4. 기본경로
			fn_setCommonPath(attrVO, majorIdx, yearIdx);
			
//			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procWReload();"));
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_INPUT"))) + "&mjCd=" + majorIdx + "&year="  + yearIdx + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 4.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 4.3 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	
	@ModuleAuth(name={"WRT"})
	@RequestMapping(method=RequestMethod.POST, value = "/abilityInputProc.do", params="mode")
	public String abilityInputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		System.out.println("여기오나");
		//##############
		//##############
		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
//		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String keyMajorIdx = StringUtil.getString(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_major_name")));
		int keyYearIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_year_name")), 0);
		
//		Enumeration<String> paramKeys = request.getParameterNames();
//		while (paramKeys.hasMoreElements()) {
//			String key = paramKeys.nextElement();
//			System.out.println(key+":"+request.getParameter(key));
//		}
		
		// 1.2 수정모드 여부 검사
		boolean isModify = (StringUtil.isEquals(mode, "m"))?true:false;
		
		if(keyMajorIdx == null || !isModify) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		/*if(keyMajorIdx == null || keyYearIdx <= 0 || !isModify) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}*/
		
		// 2. DB
		// 2.1 상세정보 - 해당글이 없는 경우 return
		String idxMajorColumnName = JSONObjectUtil.getString(settingInfo, "idx_major_column");		// key column명
		//String idxYearColumnName = JSONObjectUtil.getString(settingInfo, "idx_year_column");		// key column명		
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm(idxMajorColumnName, keyMajorIdx));
		//searchList.add(new DTForm("A." + idxYearColumnName, keyYearIdx));
		param.put("searchList", searchList);
		
//		dt = majorInfoService.getModifyAbility(fnIdx, param);
//		if(dt == null) {
//			// 해당글이 없는 경우
//			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
//			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
//		}
		
		
		// 2.2 전체관리/완전삭제 권한 체크 - 수정권한 없는 경우 return
		Boolean isMngAuth = isMngProcAuth(settingInfo, fnIdx, keyMajorIdx);
		if(!isMngAuth) {
			// 수정권한 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. 필수입력 체크
		// 3.1 항목설정
		String submitType = "modify";		// 항목 order명
		String inputFlag = "modify";		// 항목설정 중 write_type, modify_type 구분
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemJobOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Jobproc_order");
		JSONArray itemDtlOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Dtlproc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		
		// 3.2 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemJobOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
//		int result = majorInfoService.update(uploadModulePath, fnIdx, keyMajorIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
//		result = majorInfoService.updateYear(uploadModulePath, fnIdx, keyMajorIdx, keyYearIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemYearOrder);
		int result = majorInfoService.deleteAndInsertMajorAbility(searchList, request.getRemoteAddr(), parameterMap);
		// 업데이트 긴한데, 여러개가 올 수 있는 만큼 ( 지웠다가 다시 다 집어넣는 방식으로 간다고 가정 - 맞는지 모름 )
		// 여기에 지우는 로직 필요
//		result = majorInfoService.deleteDtl(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemDtlOrder, keyMajorIdx, keyYearIdx, keyJobIdx);
//		result = majorInfoService.insertDtl(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemDtlOrder, keyMajorIdx, keyYearIdx, keyJobIdx);
		
		
//		int result = 1;
		if(result > 0) {
			// 4.1 저장 성공
			
			// 기본경로
			fn_setCommonPath(attrVO);
			String majorIdx = StringUtil.getString(request.getParameter("majorCd"));
			String yearIdx = StringUtil.getString(request.getParameter("year"));
			
			// 4. 기본경로
			fn_setCommonPath(attrVO, majorIdx, yearIdx);
			
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_ABILITY_INPUT"))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		} else if(result == -1) {
			// 4.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
		}
		// 4.3 저장 실패
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
	@ModuleAuth(name="WRT")
	@RequestMapping(method=RequestMethod.POST, value = "/copyProc.do")
	public String copyProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		System.out.println("parameterMap :::: " + parameterMap);
		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		String s_colgCd = (String) parameterMap.get("s_colgCd");
		String s_fcltSustCd = (String) parameterMap.get("s_fcltSustCd");
		String s_mjCd = (String) parameterMap.get("s_mjCd");
		String s_yy = (String) parameterMap.get("s_YY");

		// 2. DB
		
		List<Object> mjCdList = new ArrayList<Object>();
		int copyResult = 0;
		
		
		Map<String, Object> param = new HashMap<String, Object>();					// mapper parameter 데이터
		param.put("COLG_CD", s_colgCd);
		param.put("FCLT_SUST_CD", s_fcltSustCd);
		param.put("MJ_CD", s_mjCd);
		param.put("YY", s_yy);
		param.put("REGI_IP", request.getRemoteAddr());
		param.put("LAST_MODI_IP", request.getRemoteAddr());
		
		mjCdList = majorInfoService.getMjCdList(fnIdx, param);
		if(mjCdList.size() == 0) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, "대상연도의 자료가 존재하지 않습니다."));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}else {
			param.put("mjCdList", mjCdList);
			copyResult = majorInfoService.copyMajorInfo(param);
			System.out.println(copyResult);
		}
		
		
		
		
    	if(copyResult > 0) {
    		// 2.1 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
        	
        	String inputNpname = fn_dsetInputNpname(settingInfo);
        	model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_COPY_VIEW"))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(copyResult == -1) {
    		// 2.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 2.3 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	@ModuleAuth(name="WRT")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		boolean isAdmMode = attrVO.isAdmMode();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		uploadModulePath = "major" + File.separator + "1";
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 필수입력 체크
		// 1.1 항목설정
		String submitType = "write";		// 항목 order명
		String inputFlag = "write";			// 항목설정 중 write_type, modify_type 구분
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		JSONArray itemYearOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Yearproc_order");
		JSONArray itemStatisticOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Statisticproc_order");
		JSONArray itemJobOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "Jobproc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		// 1.2 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 2. DB
		
		int result = 0;
		
		// 여기서 중복 체크!!!!!!!!!!!!!!!!!
		List<DTForm> dataList = new ArrayList<DTForm>();
		Map<String, Object> param = new HashMap<String, Object>();
		
		
		System.out.println("parameterMap" + parameterMap);
		
//		int isDupl = 1;
		

//		dataList.add(new DTForm("A.YEAR", parameterMap.get("year") ));
//		dataList.add(new DTForm("A.MJ_CD", parameterMap.get("mjCd") ));
		
//		param.put("searchList", dataList);
//		isDupl = majorInfoService.getMajorCount(fnIdx, param);
//		
//		
//		
//		
//		if(isDupl > 0) {
//			model.addAttribute("message", MessageUtil.getAlert(isAjax, "해당 년도의 정보가 이미 있습니다." ));
//			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
//		}
		System.out.println("여기인거 같습니다.");
		int etcCd = 0;
		
		////
		int major_idx = majorInfoService.insert(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
//		int year_idx = majorInfoService.insertYear(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemYearOrder, major_idx);
		
		int job_idx = 0;
		if(etcCd <1) {			
//			int statistic_idx = majorInfoService.insertStatistic(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemStatisticOrder, major_idx, year_idx);
//			job_idx = majorInfoService.insertJob(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemJobOrder, major_idx, year_idx);
		}
		
		if( major_idx > 0  ) {
			// 2.1 저장 성공
			// 기본경로
			fn_setCommonPath(attrVO);
//        	String inputNpname = fn_dsetInputNpname(settingInfo);
			String toUrl = PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_MODIFY")));
			toUrl += "&" + JSONObjectUtil.getString(settingInfo, "idx_major_name") + "=" + parameterMap.get("majorCd");
					/*+ "&" + JSONObjectUtil.getString(settingInfo, "idx_year_name") + "=" + parameterMap.get("year");*/
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + toUrl + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		} else if(result == -1) {
			// 2.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
		}
		// 2.3 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}


	/**
	 * 파일업로드
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="WRT")
	@RequestMapping(value = "/fileUpload.do")
	public ModelAndView fileUpload(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		boolean isAjax = true;
		boolean isAdmMode = attrVO.isAdmMode();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();

		// 1. 필수입력 체크
		// 1.1 항목설정
		String submitType = StringUtil.getString(parameterMap.get("itemId"));		// 항목 order명
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 1.2 필수입력 체크
		if (parameterMap.get(submitType) == null) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.error.data")));
			return mav;
		}
		
		// 2. DB
		Map<String, Object> fileInfo = majorInfoService.getFileUpload(uploadModulePath, fnIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(fileInfo != null && !fileInfo.isEmpty()) {
    		// 2.1 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), ""));
			model.addAttribute("data", fileInfo);
			return mav;
    	} else{
    		// 2.2 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) {
				model.addAttribute("message", fileFailView);
				return mav;
			}
    	}
		// 2.3 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return mav;
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key

		String listSearchId = "list_search";
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, listSearchId));	// 목록 검색 항목
		
		String[] tabBaseParams = null;
		String cateTabId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");
		if(!StringUtil.isEmpty(cateTabId)) tabBaseParams = new String[]{RbsProperties.getProperty("Globals.item.tab.search.pre.flag") + cateTabId};
		
//		String listName = "list.do";
//		String viewName = "view.do";
//		String inputName = "input.do";
//		String inputProcName = "inputProc.do";
//		String deleteProcName = "deleteProc.do";
//		String deleteListName = "deleteList.do";
//		String imageName = "image.do";
//		String movieName = "movie.do";
//		String downloadName = "download.do";
		String listName = "list.do";
//		String trackListName = "trackList.do";
		String majorListName = "majorList.do";
		String jobListName = "jobList.do";
		String abilityListName = "abilityList.do";
		String viewName = "view.do";
		String inputName = "input.do";
		String inputProcName = "inputProc.do";
		String deptInputName = "deptInput.do";
		String deptModifyName = "deptInput.do";
		String deptInputProcName = "deptInputProc.do";
		String trackInputName = "trackInput.do";
		String trackInputProcName = "trackInputProc.do";
		String jobInputName = "jobInput.do";
		String jobInputProcName = "jobInputProc.do";
		String deleteProcName = "deleteProc.do";
		String deleteTrackProcName = "deleteTrackProc.do";
		String deleteListName = "deleteList.do";
		String imageName = "image.do";
		String movieName = "movie.do";
		String downloadName = "download.do";
		String copyViewName = "copy.do";
		String copyName = "copyProc.do";
		String getRegisteredYear = "getRegisteredYear.do";
		String previewName = "preview.do";
		String addmajorName = "addMajor.do";
		String sptPsnViewName = "sptPsnView.do";
		String addmajorInputName = "addMajorInput.do";
		String rcmdCultListName = "rcmdCultList.do";
		String rcmdCultInputName = "rcmdCultInput.do";
		String rcmdCultInputProcName = "rcmdCultInputProc.do";
		
		if(useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, null, null, pageName, idxName, listName, viewName, null, null, null, null, inputName, inputProcName, deleteProcName, deleteListName, imageName, movieName, downloadName);
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
		
		String searchUnivListName = "searchUnivList.json";
		String searchSubjectListName = "searchSubjectList.json";
		
		
		//주관대학 3차 셀렉트 박스 
		request.setAttribute("URL_DEPARTLIST", "DepartAjax.json" + baseQueryString);
		request.setAttribute("URL_MAJORLIST", "MajorAjax.json" + baseQueryString);
		
		
//		trackListName += (baseQueryString);
		majorListName += (baseQueryString);
		abilityListName += baseQueryString;
		jobListName += baseQueryString;
		inputName += baseQueryString;
		inputProcName += baseQueryString;
		deptInputName += baseQueryString;
		deptModifyName += (baseQueryString + "&mode=m");
		deptInputProcName += baseQueryString;
		deptInputName += baseQueryString;
		trackInputName += baseQueryString;
		trackInputProcName += baseQueryString;
		jobInputName += baseQueryString;
		jobInputProcName += baseQueryString;
		copyViewName += baseQueryString;
		copyName += baseQueryString;
		getRegisteredYear += baseQueryString;
		previewName += baseQueryString;
		addmajorName += baseQueryString;
		sptPsnViewName += baseQueryString;
		addmajorInputName += baseQueryString;
		rcmdCultListName += baseQueryString;
		rcmdCultInputName += baseQueryString;
		rcmdCultInputProcName += baseQueryString;
		
//		request.setAttribute("URL_TRACK_LIST", trackListName);
		request.setAttribute("URL_MAJOR_LIST", majorListName);
		request.setAttribute("URL_JOB_LIST", jobListName);
		request.setAttribute("URL_ABILITY_LIST", abilityListName);
		request.setAttribute("URL_INPUT", inputName);
		request.setAttribute("URL_INPUT_PROC", inputProcName);
		request.setAttribute("URL_DEPT_INPUT", deptInputName);
		request.setAttribute("URL_DEPT_MODI", deptModifyName);
		request.setAttribute("URL_DEPT_INPUT_PROC", deptInputProcName);
		request.setAttribute("URL_TRACK_INPUT", trackInputName);
		request.setAttribute("URL_TRACK_INPUT_PROC", trackInputProcName);
		request.setAttribute("URL_JOB_INPUT", jobInputName);
		request.setAttribute("URL_JOB_INPUT_PROC", jobInputProcName);

		request.setAttribute("URL_COPY_VIEW", copyViewName);
		request.setAttribute("URL_COPY_PROC", copyName);
		request.setAttribute("URL_PREVIEW", previewName);
		request.setAttribute("URL_ADD_MAJOR", addmajorName);
		request.setAttribute("URL_ADD_MAJOR_INPUT", addmajorInputName);
		
		request.setAttribute("URL_SPT_PSN_VIEW", sptPsnViewName);
		
		request.setAttribute("URL_SPT_PSN_VIEW", sptPsnViewName);
		
		request.setAttribute("URL_REGISTERED_YEAR", getRegisteredYear);
		
		request.setAttribute("URL_DELETE_TRACK_PROC", deleteTrackProcName);
		
		request.setAttribute("URL_SEARCHUNIVLIST", searchUnivListName += baseQueryString);
		request.setAttribute("URL_SEARCHSUBJECTLIST", searchSubjectListName += baseQueryString);
		request.setAttribute("URL_RCMD_CULT_LIST", rcmdCultListName);
		request.setAttribute("URL_RCMD_CULT_INPUT", rcmdCultInputName);
		request.setAttribute("URL_RCMD_CULT_INPUT_PROC", rcmdCultInputProcName);
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO, String majorIdx, String yearIdx) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key

		String listSearchId = "list_search";
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, listSearchId));	// 목록 검색 항목
		
		String[] tabBaseParams = null;
		String cateTabId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");
		if(!StringUtil.isEmpty(cateTabId)) tabBaseParams = new String[]{RbsProperties.getProperty("Globals.item.tab.search.pre.flag") + cateTabId};
		
		String listName = "list.do";
//		String trackListName = "trackList.do";
		String majorListName = "majorList.do";
		String majorInputName = "courMajorInputProc.do";
		String jobListName = "jobList.do";
		String abilityListName = "abilityList.do";   
		String viewName = "view.do";
		String inputName = "input.do";
		String modifyName = "input.do";
		String inputProcName = "inputProc.do";
		String deptInputName = "deptInput.do";
		String deptModifyName = "deptInput.do";
		String deptInputProcName = "deptInputProc.do";
		String trackInputName = "trackInput.do";
		String trackInputProcName = "trackInputProc.do";
		String trackModifyProcName = "trackInputProc.do";
		String jobInputName = "jobInput.do";
		String jobInputProcName = "jobInputProc.do";
		String abilityInputName = "abilityInput.do";
		String abilityInputProcName = "abilityInputProc.do";
		String jobModifyProcName = "jobInputProc.do";
		String deleteProcName = "deleteProc.do";
		String abtyDeleteProcName = "abtyDeleteProc.do";
		String deleteListName = "deleteList.do";
		String imageName = "image.do";
		String movieName = "movie.do";
		String downloadName = "download.do";
		String deleteTrackProcName = "deleteTrackProc.do";
		String copyName = "copyProc.do";
		
		String abtyMngListName = "abtyMngList.do";
		String abtyMngInputProcName = "abtyMngInputProc.do";
		String abtyMngModiProcName = "abtyMngInputProc.do";
		
		String nonSbjtInputName = "nonSbjtInput.do";
		String licenseInputName = "licenseInput.do";
		String nonSbjtInputProcName = "nonSbjtInputProc.do";
		String licenseInputProcName = "licenseInputProc.do";
		
		String previewName = "preview.do";
		String addMajorName = "addMajor.do";
		String addMajorInputName = "addMajorInput.do";
		
		String sptPsnViewName = "sptPsnView.do";
		String rcmdCultListName = "rcmdCultList.do";
		String rcmdCultInputName = "rcmdCultInput.do";
		String rcmdCultInputProcName = "rcmdCultInputProc.do";
		
		if(useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, null, null, pageName, idxName, listName, viewName, null, null, null, null, inputName, inputProcName, deleteProcName, deleteListName, imageName, movieName, downloadName);
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
		
		String searchUnivListName = "searchUnivList.json";
		String searchSubjectListName = "searchSubjectList.json";
		
//		trackListName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		majorListName += (baseQueryString + "&majorCd=" + majorIdx + "&year=" + yearIdx);
		majorInputName += (baseQueryString + "&majorCd=" + majorIdx + "&year=" + yearIdx);
		jobListName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		inputName += baseQueryString;
		modifyName += (baseQueryString + "&mode=m&majorCd=" + majorIdx + "&year=" + yearIdx);
		inputProcName += baseQueryString;
		deptInputName += baseQueryString;
		deptInputProcName += baseQueryString;
		deptModifyName += (baseQueryString + "&mode=m&majorCd=" + majorIdx) ;
		trackInputName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		trackInputProcName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		trackModifyProcName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx + "&mode=m");
		jobInputName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		jobInputProcName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		jobModifyProcName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx + "&mode=m");
		deleteTrackProcName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		copyName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		
		abilityListName += (baseQueryString + "&majorCd=" + majorIdx + "&year=" + yearIdx);
		abilityInputName += (baseQueryString + "&mode=m&majorCd=" + majorIdx + "&year=" + yearIdx);
		abilityInputProcName += (baseQueryString + "&mode=m&majorCd=" + majorIdx + "&year=" + yearIdx );
		
		sptPsnViewName += baseQueryString;

		previewName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		addMajorName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		addMajorInputName += (baseQueryString + "&majorIdx=" + majorIdx + "&yearIdx=" + yearIdx);
		rcmdCultListName += (baseQueryString + "&majorCd=" + majorIdx + "&year=" + yearIdx + "&mode=m");
		rcmdCultInputName += (baseQueryString + "&majorCd=" + majorIdx + "&year=" + yearIdx + "&mode=m&dl=1");
		rcmdCultInputProcName += (baseQueryString + "&majorCd=" + majorIdx + "&year=" + yearIdx + "&mode=m");
		
		abtyMngListName += (baseQueryString + "&majorCd=" + majorIdx);
		abtyMngInputProcName += (baseQueryString + "&majorCd=" + majorIdx);
		abtyMngModiProcName += (baseQueryString + "&majorCd=" + majorIdx + "&mode=m");
		abtyDeleteProcName += (baseQueryString + "&majorCd=" + majorIdx);
		
		nonSbjtInputName += (baseQueryString + "&mode=m&majorCd=" + majorIdx + "&year=" + yearIdx);
		licenseInputName += (baseQueryString + "&mode=m&majorCd=" + majorIdx + "&year=" + yearIdx);
		nonSbjtInputProcName += (baseQueryString + "&mode=m&majorCd=" + majorIdx + "&year=" + yearIdx);
		licenseInputProcName += (baseQueryString + "&mode=m&majorCd=" + majorIdx + "&year=" + yearIdx);
		
		//주관대학 3차 셀렉트 박스 
		request.setAttribute("URL_DEPARTLIST", "DepartAjax.json" + baseQueryString);
		request.setAttribute("URL_MAJORLIST", "MajorAjax.json" + baseQueryString );	
	
//		request.setAttribute("URL_TRACK_LIST", trackListName);
		request.setAttribute("URL_MAJOR_LIST", majorListName);
		request.setAttribute("URL_COUR_MAJOR_INPUT_PROC", majorInputName);
		request.setAttribute("URL_JOB_LIST", jobListName);
		request.setAttribute("URL_INPUT", inputName);
		request.setAttribute("URL_MODIFY", modifyName);
		request.setAttribute("URL_INPUT_PROC", inputProcName);
		request.setAttribute("URL_DEPT_INPUT", deptInputName);
		request.setAttribute("URL_DEPT_INPUT_PROC", deptInputProcName);
		request.setAttribute("URL_DEPT_MODI", deptModifyName);
		request.setAttribute("URL_TRACK_INPUT", trackInputName);
		request.setAttribute("URL_TRACK_INPUT_PROC", trackInputProcName);
		request.setAttribute("URL_TRACK_MODIFY_PROC", trackModifyProcName);
		request.setAttribute("URL_JOB_INPUT", jobInputName);
		request.setAttribute("URL_JOB_INPUT_PROC", jobInputProcName);
		request.setAttribute("URL_JOB_MODIFY_PROC", jobModifyProcName);
		
		request.setAttribute("URL_COPY_PROC", copyName);
		request.setAttribute("URL_DELETE_TRACK_PROC", deleteTrackProcName);
		
		request.setAttribute("URL_SEARCHUNIVLIST", searchUnivListName += baseQueryString);
		request.setAttribute("URL_SEARCHSUBJECTLIST", searchSubjectListName += baseQueryString);
		
		
		request.setAttribute("URL_ABILITY_LIST", abilityListName);
		request.setAttribute("URL_ABILITY_INPUT", abilityInputName);
		request.setAttribute("URL_ABILITY_INPUT_PROC", abilityInputProcName);
		
		request.setAttribute("URL_PREVIEW", previewName);
		
		request.setAttribute("URL_ADD_MAJOR", addMajorName);
		request.setAttribute("URL_ADD_MAJOR_INPUT", addMajorInputName);
		
		request.setAttribute("URL_SPT_PSN_VIEW", sptPsnViewName);
		
		request.setAttribute("URL_RCMD_CULT_LIST", rcmdCultListName);
		request.setAttribute("URL_RCMD_CULT_INPUT", rcmdCultInputName);
		request.setAttribute("URL_RCMD_CULT_INPUT_PROC", rcmdCultInputProcName);
		
		request.setAttribute("URL_ABTY_MNG_LIST", abtyMngListName);
		request.setAttribute("URL_ABTY_MNG_INPUT_PROC", abtyMngInputProcName);
		request.setAttribute("URL_ABTY_MNG_MODI_PROC", abtyMngModiProcName);
		request.setAttribute("URL_ABTY_MNG_DEL_PROC", abtyDeleteProcName);
		
		
		request.setAttribute("URL_NON_SBJT_INPUT", nonSbjtInputName);
		request.setAttribute("URL_LICENSE_INPUT", licenseInputName);
		request.setAttribute("URL_NON_SBJT_INPUT_PROC", nonSbjtInputProcName);
		request.setAttribute("URL_LICENSE_INPUT_PROC", licenseInputProcName);
	}

	/**
	 * 휴지통 경로
	 * @param attrVO
	 */
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");			// 목록 페이징  key

		// 항목 설정 정보
		String listSearchId = "list_search";
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, listSearchId)), deleteListSearchParams);	// 검색 항목
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
	}
	
	/**
	 * 저장 후 되돌려줄 페이지 속성명
	 * @param settingInfo
	 * @return
	 */
	public String fn_dsetInputNpname(JSONObject settingInfo){
		String dsetInputNpname = JSONObjectUtil.getString(settingInfo, "dset_input_npname");
		if(StringUtil.isEmpty(dsetInputNpname)){
			dsetInputNpname = "LIST";
			return dsetInputNpname;
		}
		
		return dsetInputNpname;
	}
	
	/**
	 * 관리권한 체크
	 * @param settingInfo
	 * @param fnIdx
	 * @param keyIdx
	 * @param memIdx
	 * @param useReply
	 * @param pwd
	 * @return
	 */
	public boolean isMngProcAuth(JSONObject settingInfo, int fnIdx, int keyIdx) {
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");
		
		int modiCnt = 0;
		// 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");		// 전체관리
		
		if(!isMngAuth){
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			
			searchList.add(new DTForm("A." + columnName, keyIdx));
			
			// 전체관리권한 없는 경우 : 자신글만 수정
			boolean isLogin = UserDetailsHelper.isLogin();		// 로그인한 경우
			if(isLogin){
				// 로그인한 경우
				String memberIdx = null;
				
				LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
				if(loginVO != null) {
					memberIdx = loginVO.getMemberIdx();
				}
				
				if(StringUtil.isEmpty(memberIdx)) return false;
				param.put("AUTH_MEMBER_IDX", memberIdx);
			} else {
				// 본인인증 or 로그인 안 한 경우
				return false;
			}

			param.put("searchList", searchList);
			modiCnt = majorInfoService.getAuthCount(fnIdx, param);
			
			return (modiCnt > 0);
		}
		
		return true;
	}
	
	
	/**
	 * 관리권한 체크
	 * @param settingInfo
	 * @param fnIdx
	 * @param keyIdx
	 * @param memIdx
	 * @param useReply
	 * @param pwd
	 * @return
	 */
	public boolean isMngProcAuth(JSONObject settingInfo, int fnIdx, String keyIdx) {
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");
		
		int modiCnt = 0;
		// 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");		// 전체관리
		
		if(!isMngAuth){
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			
			searchList.add(new DTForm("A." + columnName, keyIdx));
			
			// 전체관리권한 없는 경우 : 자신글만 수정
			boolean isLogin = UserDetailsHelper.isLogin();		// 로그인한 경우
			if(isLogin){
				// 로그인한 경우
				String memberIdx = null;
				
				LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
				if(loginVO != null) {
					memberIdx = loginVO.getMemberIdx();
				}
				
				if(StringUtil.isEmpty(memberIdx)) return false;
				param.put("AUTH_MEMBER_IDX", memberIdx);
			} else {
				// 본인인증 or 로그인 안 한 경우
				return false;
			}

			param.put("searchList", searchList);
			modiCnt = majorInfoService.getAuthCount(fnIdx, param);
			
			return (modiCnt > 0);
		}
		
		return true;
	}
	
	
	
	/**
	 * 대학리스트 불러오기
	 */
	@ModuleAuth(name="LST")
	 @RequestMapping(value = "/searchUnivList.json",  headers="Ajax")
	  public ModelAndView searchUnivList(@RequestParam(value="deptCampus") String deptCampus, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
	    ModelAndView mav = new ModelAndView("jsonView");
		 List<Object> haksaCode = codeOptnServiceOra.getHaksaCode(deptCampus);
		model.addAttribute("haksaCode",haksaCode);
	    return mav;
	  }
	
	
	/**
	 * 대학리스트 불러오기
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/searchSubjectList.json",  headers="Ajax")
	public ModelAndView searchSubjectList(@RequestParam(value="deptUniv") String deptUniv, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<Object> haksaCode = codeOptnServiceOra.getHaksaCode(deptUniv);
		model.addAttribute("haksaCode",haksaCode);
		return mav;
	}
}


