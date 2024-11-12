package rbs.modules.sbjt.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.AuthHelper;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.DateUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.egovframework.util.ApiUtil;
import rbs.modules.majorInfo.service.MajorInfoService;
import rbs.modules.sbjt.service.SbjtService;
import rbs.modules.sbjt.serviceOra.SbjtServiceOra;
import rbs.modules.search.service.SearchService;

/**
 * 샘플모듈<br/>
 * : 통합관리시스템 > 메뉴콘텐츠관리, 통합관리시스템 > 기능등록관리, 사용자 사이트 에서 사용
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="sbjt")
@RequestMapping({"/{siteId}/sbjt", "/{admSiteId}/menuContents/{usrSiteId}/sbjt"})
public class SbjtController extends ModuleController {
	
	private static final Logger logger = LoggerFactory.getLogger(SbjtController.class);
	
	@Resource(name = "sbjtService")
	private SbjtService sbjtService;
	
	@Resource(name="searchService")
	private SearchService searchService;	
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name = "sbjtServiceOra")
	protected SbjtServiceOra sbjtServiceOra;
	
	@Resource(name = "majorInfoService")
	protected MajorInfoService majorInfoService;
	
	/*@Resource(name = "inuUserService")
	private InuUserService inuUserService;
	
	@Resource(name = "searchLogService")
	private SearchLogService searchLogService;*/
	
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
	
	/**
	 * 검색코드
	 * @param 
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	/*public HashMap<String, Object> getSearchCode() throws Exception {
		
		List<Object> getCptnCode = codeOptnServiceOra.getCptnCode();			//이수구분 ex)전공필수,교양필수
		List<Object> getCorsCode = codeOptnServiceOra.getCorsCode();			//과정구분 ex)학사,석사
		List<Object> get6CoreAbtyCode = codeOptnServiceOra.get6CoreAbtyCode();	//핵심역량 ex)지식탐구,의사소통
		List<Object> getTrackCode = codeOptnServiceOra.getTrackCode();			//연계전공 ex)나노디그리,매트릭스
		List<Object> getTmGbnCode = codeOptnServiceOra.getTmGbnCode();			//학기구분
		List<Object> getHySeqGbn = codeOptnServiceOra.getHySeqGbn();			//학년
		
		HashMap<String, Object> searchCodeMap= new HashMap<>();
		searchCodeMap.put("getCptnCode", getCptnCode);
		searchCodeMap.put("getCorsCode",getCorsCode );
		searchCodeMap.put("get6CoreAbtyCode",get6CoreAbtyCode );
		searchCodeMap.put("getTrackCode", getTrackCode);
		searchCodeMap.put("getTmGbnCode", getTmGbnCode);
		searchCodeMap.put("getHySeqGbn", getHySeqGbn);
		
		return searchCodeMap;
	}*/
	
	/**
	 * 대학리스트 불러오기
	 */
	/*@ModuleAuth(name="LST")
	 @RequestMapping(value = "/searchUnivList.json",  headers="Ajax")
	  public ModelAndView searchUnivList(@RequestParam(value="deptCampus") String deptCampus, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
	    ModelAndView mav = new ModelAndView("jsonView");
		 List<Object> haksaCode = codeOptnServiceOra.getHaksaCode(deptCampus);
		model.addAttribute("haksaCode",haksaCode);
	    return mav;
	  }*/
	
	
	/**
	 * 소속학과(부) 불러오기
	 */
	/*@ModuleAuth(name="LST")
	@RequestMapping(value = "/searchSubjectList.json",  headers="Ajax")
	public ModelAndView searchSubjectList(@RequestParam(value="deptUniv") String deptUniv, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<Object> haksaCode = codeOptnServiceOra.getHaksaCode(deptUniv);
		model.addAttribute("haksaCode",haksaCode);
		return mav;
	}*/
	
	
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
		List<String> stdCoreList = new ArrayList<>();
		
		String stdCore1 = request.getParameter("std_core_1");
		String stdCore2 = request.getParameter("std_core_2");
		String stdCore3 = request.getParameter("std_core_3");
		String stdCore4 = request.getParameter("std_core_4");
		String stdCore5 = request.getParameter("std_core_5");
		String stdCore6 = request.getParameter("std_core_6");
		String stdCore7 = request.getParameter("std_core_7");
		String stdCore8 = request.getParameter("std_core_8");
		
		if (stdCore1 != null) stdCoreList.add(stdCore1);
		if (stdCore2 != null) stdCoreList.add(stdCore2);
		if (stdCore3 != null) stdCoreList.add(stdCore3);
		if (stdCore4 != null) stdCoreList.add(stdCore4);
		if (stdCore5 != null) stdCoreList.add(stdCore5);
		if (stdCore6 != null) stdCoreList.add(stdCore6);
		if (stdCore7 != null) stdCoreList.add(stdCore7);
		if (stdCore8 != null) stdCoreList.add(stdCore8);

		model.addAttribute("stdCore", String.join(",", stdCoreList));
		model.addAttribute("collegeList", majorInfoService.getCollegeList());
		
		fn_setCommonPath(attrVO);
		
		return getViewPath("/list");
	}
	
	
	/**
	 * 목록조회(비동기)
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/initSbjtList.do", headers="Ajax")
	public ModelAndView initSbjtList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		//int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
		//SSO 로그인 세션정보
		HttpSession session = request.getSession(true); 
		
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 10;	// 페이지당 목록 수
		int listMaxUnit = 5;	// 최대 페이지당 목록 수 
		int listUnitStep = 10;	// 페이지당 목록 수 증가값
		
		int pageUnit = 10;
		int pageSize = 10;	
		
		int page = StringUtil.getInt(request.getParameter("page"), 1);	

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
		
		int endNum = page * 6;
		int startNum = endNum - 5;
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		String univ = request.getParameter("univ");
		String depart = request.getParameter("depart");
		if (depart != null && !"".equals(depart)) {
		    depart = depart.substring(0, depart.length() - 1) + '%';
		}
		String major = request.getParameter("major");
		String year = request.getParameter("year");
		String semester = request.getParameter("semester");
		String grade = request.getParameter("grade");
		String stdComplet1 = request.getParameter("std_complet_1");
		String stdComplet2 = request.getParameter("std_complet_2");
		String stdComplet3 = request.getParameter("std_complet_3");
		String stdComplet4 = request.getParameter("std_complet_4");
		String stdComplet5 = request.getParameter("std_complet_5");
		String stdComplet6 = request.getParameter("std_complet_6");
		String stdComplet7 = request.getParameter("std_complet_7");
		String stdComplet8 = request.getParameter("std_complet_8");
		String stdCore1 = request.getParameter("std_core_1");
		String stdCore2 = request.getParameter("std_core_2");
		String stdCore3 = request.getParameter("std_core_3");
		String stdCore4 = request.getParameter("std_core_4");
		String stdCore5 = request.getParameter("std_core_5");
		String stdCore6 = request.getParameter("std_core_6");
		String stdCore7 = request.getParameter("std_core_7");
		String stdCore8 = request.getParameter("std_core_8");
		String flagLog = request.getParameter("flagLog");
		
		List<String> stdCompletList = new ArrayList<>();
		
		if (stdComplet1 != null && stdComplet1 != "") stdCompletList.add(stdComplet1);
		if (stdComplet2 != null && stdComplet1 != "") stdCompletList.add(stdComplet2);
		if (stdComplet3 != null && stdComplet1 != "") stdCompletList.add(stdComplet3);
		if (stdComplet4 != null && stdComplet1 != "") stdCompletList.add(stdComplet4);
		if (stdComplet5 != null && stdComplet1 != "") stdCompletList.add(stdComplet5);
		if (stdComplet6 != null && stdComplet1 != "") stdCompletList.add(stdComplet6);
		if (stdComplet7 != null && stdComplet1 != "") stdCompletList.add(stdComplet7);
		if (stdComplet8 != null && stdComplet1 != "") stdCompletList.add(stdComplet8);
		
		String[] stdComplet = stdCompletList.toArray(new String[0]);
		int completCount = stdComplet.length;
		
		List<String> stdCoreList = new ArrayList<>();
		
		if (stdCore1 != null && stdComplet1 != "") stdCoreList.add(stdCore1);
		if (stdCore2 != null && stdComplet1 != "") stdCoreList.add(stdCore2);
		if (stdCore3 != null && stdComplet1 != "") stdCoreList.add(stdCore3);
		if (stdCore4 != null && stdComplet1 != "") stdCoreList.add(stdCore4);
		if (stdCore5 != null && stdComplet1 != "") stdCoreList.add(stdCore5);
		if (stdCore6 != null && stdComplet1 != "") stdCoreList.add(stdCore6);
		if (stdCore7 != null && stdComplet1 != "") stdCoreList.add(stdCore7);
		if (stdCore8 != null && stdComplet1 != "") stdCoreList.add(stdCore8);

		String[] stdCore = stdCoreList.toArray(new String[0]);
		int coreCount = stdCore.length;
		
		String orderBy = request.getParameter("orderBy");
		
		if(StringUtil.isEquals(orderBy, "bySbjtName")  ) {
			param.put("field", "SUBJECT_NM");
			param.put("order", "ASC");
		} else if(StringUtil.isEquals(orderBy, "bySbjtType")  ) {
			param.put("field", "COMDIV_CODE");
			param.put("order", "DESC");
		} else if(StringUtil.isEquals(orderBy, "byGrade")  ) {
			param.put("field", "GRADE");
			param.put("order", "DESC");
		} else if(StringUtil.isEquals(orderBy, "byRegidate")  ) {
			param.put("field", "YEAR");
			param.put("order", "DESC");
		} else if(StringUtil.isEquals(orderBy, "byMajor")  ) {
			param.put("field", "MAJOR_NM");
			param.put("order", "ASC");
		}  
		
		String preOpen = request.getParameter("open");
		int open = 0;
		if(preOpen != null && !"".equals(preOpen)) {
			open = Integer.parseInt(preOpen);
		}
		
		// 년도 설정
		Date today = new Date();
		
		// Calendar 인스턴스를 현재 날짜로 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        // n년 전으로 설정
        calendar.add(Calendar.YEAR, -open);
        
        // n년 전의 날짜 가져오기
        Date yearsAgo = calendar.getTime();
		
		String strYear = DateUtil.getDateFormat(today, "yyyy");
		String agoYear = DateUtil.getDateFormat(yearsAgo, "yyyy");
		
		
		param.put("startNum", startNum);
		param.put("endNum", endNum);
		param.put("endDate", strYear);						//종료일자
		param.put("startDate", agoYear);					//시작일자
		param.put("university", univ);						//대학코드   309000
		param.put("department", depart);					//학과코드   309050
		param.put("major", major);							//전공코드   309050
		param.put("year", year);							//년도     2024
		param.put("semester", semester);					//학기  GH0210
  		param.put("grade", grade);							//학년   1
		param.put("course_classification", stdComplet);	//이수구분   UE010024
		param.put("completCount", completCount);
		param.put("core_competence", stdCore);				//핵심역량
		param.put("coreCount", coreCount);
		
		List<Object> sbjtList = new ArrayList<>();
		//전체 갯수 설정
		DataMap totalCount = sbjtServiceOra.getInitSbjtListCount(param);
		int totCnt = ((BigDecimal) totalCount.get("TOTAL_COUNT")).intValue();
		model.addAttribute("totalCount", totCnt);
		paginationInfo.setTotalRecordCount(totCnt);
		
		model.addAttribute("paginationInfo", paginationInfo);
		
		sbjtList = sbjtServiceOra.getInitSbjtList(param);
		model.addAttribute("sbjtList", (sbjtList == null) ? null : sbjtList);
		
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return mav;
	}
	
	/**
	 * 목록조회(비동기)
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/sbjtList.do", headers="Ajax")
	public ModelAndView sbjtList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		//int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
		
		String top_search = StringUtil.isEmpty(request.getParameter("top_search")) ? "" : request.getParameter("top_search");
		String univ = request.getParameter("univ");
		String depart = request.getParameter("depart");
		String major = request.getParameter("major");
		String year = request.getParameter("year");
		String semester = request.getParameter("semester");
		String grade = request.getParameter("grade");
		String stdComplet1 = request.getParameter("std_complet_1");
		String stdComplet2 = request.getParameter("std_complet_2");
		String stdComplet3 = request.getParameter("std_complet_3");
		String stdComplet4 = request.getParameter("std_complet_4");
		String stdComplet5 = request.getParameter("std_complet_5");
		String stdComplet6 = request.getParameter("std_complet_6");
		String stdComplet7 = request.getParameter("std_complet_7");
		String stdComplet8 = request.getParameter("std_complet_8");
		String stdCore1 = request.getParameter("std_core_1");
		String stdCore2 = request.getParameter("std_core_2");
		String stdCore3 = request.getParameter("std_core_3");
		String stdCore4 = request.getParameter("std_core_4");
		String stdCore5 = request.getParameter("std_core_5");
		String stdCore6 = request.getParameter("std_core_6");
		String stdCore7 = request.getParameter("std_core_7");
		String stdCore8 = request.getParameter("std_core_8");
		String orderBy = request.getParameter("orderBy");
		String flagLog = request.getParameter("flagLog");
//		String[] stdCore = request.getParameterValues("std_core");
		
		List<String> stdCompletList = new ArrayList<>();
		
		if (stdComplet1 != null) stdCompletList.add(stdComplet1);
		if (stdComplet2 != null) stdCompletList.add(stdComplet2);
		if (stdComplet3 != null) stdCompletList.add(stdComplet3);
		if (stdComplet4 != null) stdCompletList.add(stdComplet4);
		if (stdComplet5 != null) stdCompletList.add(stdComplet5);
		if (stdComplet6 != null) stdCompletList.add(stdComplet6);
		if (stdComplet7 != null) stdCompletList.add(stdComplet7);
		if (stdComplet8 != null) stdCompletList.add(stdComplet8);
		
		String stdComplet = String.join(",", stdCompletList);
		
		List<String> stdCoreList = new ArrayList<>();

		if (stdCore1 != null) stdCoreList.add(stdCore1);
		if (stdCore2 != null) stdCoreList.add(stdCore2);
		if (stdCore3 != null) stdCoreList.add(stdCore3);
		if (stdCore4 != null) stdCoreList.add(stdCore4);
		if (stdCore5 != null) stdCoreList.add(stdCore5);
		if (stdCore6 != null) stdCoreList.add(stdCore6);
		if (stdCore7 != null) stdCoreList.add(stdCore7);
		if (stdCore8 != null) stdCoreList.add(stdCore8);

		String stdCore = String.join(",", stdCoreList);
		
		String preOpen = request.getParameter("open");
		int open = 4;
		if(preOpen != null && !"".equals(preOpen)) {
			open = Integer.parseInt(preOpen);
		}
		
		
		//SSO 로그인 세션정보
		HttpSession session = request.getSession(true); 
		
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		//String memberId = loginVO.getMemberId();
		//List<Object> userInfo = inuUserService.getInuUserInfo(memberId);

		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 10;	// 페이지당 목록 수
		int listMaxUnit = 5;	// 최대 페이지당 목록 수 
		int listUnitStep = 10;	// 페이지당 목록 수 증가값
		
		int pageUnit = 10;
		int pageSize = 10;	
		
		int page = StringUtil.getInt(request.getParameter("page"), 1);				// 현재 페이지 index

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
		// 년도 설정
		Date today = new Date();
		
		// Calendar 인스턴스를 현재 날짜로 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        // n년 전으로 설정
        calendar.add(Calendar.YEAR, -open);
        
        // n년 전의 날짜 가져오기
        Date yearsAgo = calendar.getTime();
		
		String strToday = DateUtil.getDateFormat(today, "yyyy");
		String agoYear = DateUtil.getDateFormat(yearsAgo, "yyyy");
		
		/*if(preOpen != null && !"".equals(preOpen)) {
			agoYear = DateUtil.getDateFormat(yearsAgo, "yyyy");
		}*/
		
		
		//검색 조건 코드 값
		//HashMap<String, Object> searchCodeMap = getSearchCode();
		
		List<?> collegeList = null;
		collegeList = majorInfoService.getCollegeList();
		
//		if(request.getParameter("top_search") != null && !"".equals(request.getParameter("top_search"))) {
			
			//api 세팅
			String url = RbsProperties.getProperty("Globals.search.url");
			String endpoint = RbsProperties.getProperty("Globals.search.endpoint.subject");
			
			//api 호출 파라미터
			JSONObject reqJsonObj = new JSONObject();
			
			//파라미터 정렬 세팅
			List<HashMap<String, String>> sortList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> sortMap1 = new HashMap<String, String>();
			HashMap<String, String> sortMap2 = new HashMap<String, String>();
			HashMap<String, String> sortMap3 = new HashMap<String, String>();
			if(StringUtil.isEquals(orderBy, "byRegidate")  ) {
				sortMap1.put("sortType", "fieldSort");
				sortMap1.put("field", "YEAR");
				sortMap1.put("order", "DESC");
				sortMap2.put("sortType", "fieldSort");
				sortMap2.put("field", "SMT_CD");
				sortMap2.put("order", "DESC");
				sortList.add(sortMap1);
				sortList.add(sortMap2);
			} else if(StringUtil.isEquals(orderBy, "bySbjtName")  ) {
				sortMap1.put("sortType", "fieldSort");
				sortMap1.put("field", "SUBJECT_NM");
				sortMap1.put("order", "ASC");
				sortMap2.put("sortType", "fieldSort");
				sortMap2.put("field", "YEAR");
				sortMap2.put("order", "DESC");
				sortMap3.put("sortType", "fieldSort");
				sortMap3.put("field", "SMT_CD");
				sortMap3.put("order", "DESC");
				sortList.add(sortMap1);
				sortList.add(sortMap2);
				sortList.add(sortMap3);
			} else if(StringUtil.isEquals(orderBy, "byMajor")  ) {
				sortMap1.put("sortType", "fieldSort");
				sortMap1.put("field", "MAJOR_NM");
				sortMap1.put("order", "ASC");
				sortMap2.put("sortType", "fieldSort");
				sortMap2.put("field", "YEAR");
				sortMap2.put("order", "DESC");
				sortMap3.put("sortType", "fieldSort");
				sortMap3.put("field", "SMT_CD");
				sortMap3.put("order", "DESC");
				sortList.add(sortMap1);
				sortList.add(sortMap2);
				sortList.add(sortMap3);
			} else if(StringUtil.isEquals(orderBy, "bySbjtType")  ) {
				sortMap1.put("sortType", "fieldSort");
				sortMap1.put("field", "COMDIV_CD");
				sortMap1.put("order", "DESC");
				sortMap2.put("sortType", "fieldSort");
				sortMap2.put("field", "YEAR");
				sortMap2.put("order", "DESC");
				sortMap3.put("sortType", "fieldSort");
				sortMap3.put("field", "SMT_CD");
				sortMap3.put("order", "DESC");
				sortList.add(sortMap1);
				sortList.add(sortMap2);
				sortList.add(sortMap3);
			} else if(StringUtil.isEquals(orderBy, "byGrade")  ) {
				sortMap1.put("sortType", "fieldSort");
				sortMap1.put("field", "GRADE");
				sortMap1.put("order", "DESC");
				sortMap2.put("sortType", "fieldSort");
				sortMap2.put("field", "YEAR");
				sortMap2.put("order", "DESC");
				sortMap3.put("sortType", "fieldSort");
				sortMap3.put("field", "SMT_CD");
				sortMap3.put("order", "DESC");
				sortList.add(sortMap1);
				sortList.add(sortMap2);
				sortList.add(sortMap3);
			} else {
				sortMap1.put("sortType", "scoreSort");
				sortMap1.put("order", "DESC");
				sortList.add(sortMap1);
			} 
		
			
			reqJsonObj.put("keyword", top_search);					//검색어
			reqJsonObj.put("page_num", page-1);						//요청 페이지 번호
			reqJsonObj.put("page_per", 10);							//페이지당 목록 수
			reqJsonObj.put("start_date", agoYear);					//시작날짜
			reqJsonObj.put("end_date", strToday);					//종료날짜
			reqJsonObj.put("sort", sortList);						//정렬
			reqJsonObj.put("university", univ);						//대학코드   309000
			reqJsonObj.put("department", depart);					//학과코드   309050
			reqJsonObj.put("major", major);							//전공코드   309050
			reqJsonObj.put("year", year);							//년도     2024
			reqJsonObj.put("semester", semester);					//학기  GH0210
			reqJsonObj.put("grade", grade);							//학년   1
			reqJsonObj.put("course_classification", stdComplet);	//이수구분   UE010024
			reqJsonObj.put("competence", stdCore);					//핵심역량(전부)
			//reqJsonObj.put("core_competence", stdCore);				//핵심역량(주역량)
			//reqJsonObj.put("sub_core_competence", stdCore);			//핵심역량(부역량)
			
			//sortMap = null;
			//sortList = null;	
			
			//페이지 세팅
			//reqJsonObj.put("pageNo", 1);
			if(null != request.getParameter("spjtListPage")) {
				int startNum = Integer.parseInt(request.getParameter("spjtListPage"));
				//reqJsonObj.put("pageNo", startNum);
			}
			
			//api 호출
			String responseData = null;
			responseData = ApiUtil.getRestApi(url, endpoint, ApiUtil.METHOD_POST, reqJsonObj);
			
			//log insert
			if(StringUtil.isEquals(flagLog, "Y")) {
				searchService.insertSearchLog(endpoint, reqJsonObj, responseData, request);
			}
			
			JSONObject responseJson = JSONObject.fromObject(responseData);
			
			//전체 갯수 설정
			int totalCount = 0;
			if(null != responseJson.getJSONObject("data").getString("total_count")) {
				totalCount = responseJson.getJSONObject("data").getInt("total_count");
			}
	
			model.addAttribute("totalCount", totalCount);
			paginationInfo.setTotalRecordCount(totalCount);
			model.addAttribute("paginationInfo", paginationInfo);
			
			
			//교과목 목록 추출
			JSONArray resultArray = responseJson.getJSONObject("data").getJSONArray("result");
			
			
			//관심교과목 추출 
			List<Object> lectureDocId = new ArrayList<>();
			List<Object> bkDocIdList = new ArrayList<>();
			List<Map<String, String>> sbjtList = new ArrayList<>();
			
			if(totalCount>0) {
				
				//화면에 뿌려줄 데이터 세팅
				for (Object sbjtObj : resultArray) {
					JSONObject sbjtJobj = (JSONObject) sbjtObj;
	
					Map<String, String> map = new HashMap<>();
					
					map.put("year", sbjtJobj.getString("YEAR"));
					map.put("smtCd", sbjtJobj.getString("SMT_CD"));				//학기 코드
					map.put("smtNm", sbjtJobj.getString("SMT_NM"));				//학기 명			
					map.put("sinbunCd", sbjtJobj.getString("SINBUN_CD"));
					map.put("sinbunNm", sbjtJobj.getString("SINBUN_NM"));
					map.put("subjectCd", sbjtJobj.getString("SUBJECT_CD"));
					map.put("subjectNm", sbjtJobj.getString("SUBJECT_NM"));
					map.put("subjectENm", sbjtJobj.getString("SUBJECT_ENM"));
					map.put("deptCd", sbjtJobj.getString("DEPT_CD"));
					map.put("deptNm", sbjtJobj.getString("DEPT_NM"));
					map.put("majorCd", sbjtJobj.getString("MAJOR_CD"));
					map.put("majorNm", sbjtJobj.getString("MAJOR_NM"));
					map.put("cdtNum", sbjtJobj.getString("CDT_NUM"));
					map.put("wtimeNum", sbjtJobj.getString("WTIME_NUM"));	
					map.put("ptimeNum", sbjtJobj.getString("PTIME_NUM"));
					map.put("degGbCd", sbjtJobj.getString("DEG_GB_CD"));
					map.put("score", sbjtJobj.getString("score"));
					map.put("lectureId", sbjtJobj.getString("LECTURE_ID"));
					map.put("colgNm", sbjtJobj.getString("COLG_NM"));
					map.put("degGbNm", sbjtJobj.getString("DEG_GB_NM"));
					map.put("colgCd", sbjtJobj.getString("COLG_CD"));
					map.put("grade", sbjtJobj.getString("GRADE"));
					map.put("id", sbjtJobj.getString("id"));
					map.put("subjDescKor", sbjtJobj.getString("SUBJ_DESC"));
					map.put("subjDescEng", sbjtJobj.getString("SUBJ_DESC_ENG"));
					map.put("sisu", sbjtJobj.getString("SISU"));
					map.put("comdivNm", sbjtJobj.getString("COMDIV_NM"));
					map.put("comdivCd", sbjtJobj.getString("COMDIV_CD"));
					map.put("sbjt_key", sbjtJobj.getString("LECTURE_ID"));
					
					DataMap resultMap = sbjtServiceOra.getCoreAbi(map);
					
					if(resultMap != null) {
						map.put("majorAbi", (resultMap.get("MAJOR_ABI") == null ) ? "" : (String) resultMap.get("MAJOR_ABI"));
						map.put("essentialAbi", (resultMap.get("ESSENTIAL_ABI") == null ) ? "" : (String) resultMap.get("ESSENTIAL_ABI"));
					}
					
					
					map.put("bookMark", "N");
					
					sbjtList.add(map);
				}
			}
			
			
			model.addAttribute("sbjtList", sbjtList);
		
//		}
		model.addAttribute("collegeList", collegeList);
		model.addAttribute("stdCore", stdCore);
		
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return mav;
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
	 * 관심교과목 등록 수정
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/inputCourBKProc.json", headers="Ajax")
	public ModelAndView inputCourBKProc(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		//SSO값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		
		List<DTForm> queryList = new ArrayList<DTForm>();
		
		queryList.add(new DTForm("PERS_NO",loginVO.getMemberId()));
		queryList.add(new DTForm("DEPT_CLSF_CD", paramMap.get("deptClsfCd")));
		queryList.add(new DTForm("YY", paramMap.get("yy")));
		queryList.add(new DTForm("TM_GBN", paramMap.get("tmGbn")));
		queryList.add(new DTForm("HG_MJ_CD", paramMap.get("hgMjCd")));
		queryList.add(new DTForm("SC_CD", paramMap.get("scCd")));
		
		param.put("searchList", queryList);
		int bkCnt = sbjtService.bkCourCount(param);
		
		int result =0;
		
		if(paramMap.get("booMarkIdx").equals("0")) {	//등록
			if(bkCnt<1) {
				param.put("queryList", queryList);			//처음 등록시
				result = sbjtService.insertBkCour(param, request);
			}else {
				param.put("isdelete", "0");					//재 등록시
				//param.put("searchList", queryList);			
				result = sbjtService.updateBkCour(param, request);
			}
		}else {
			param.put("isdelete", "1");						//삭제 시
			//param.put("searchList", queryList);
			result = sbjtService.updateBkCour(param, request);
		}
	
		model.addAttribute("res", result);

		return mav;
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
		
		// 1. 필수 parameter 검사-과목별 식별ID(소속분류,학과전공,교과목코드,학기,년도)
		String subjectCd = request.getParameter("SUBJECT_CD");
		String deptCd = request.getParameter("DEPT_CD");
		String year = request.getParameter("YEAR");
		String smt = request.getParameter("SMT");
		
		if(subjectCd == null) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		String ajaxPName = attrVO.getAjaxPName();
		boolean isAjax = attrVO.isAjax();

		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
		
		// 2. DB
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("subjectCd", subjectCd);
		param.put("deptCd", deptCd);
		param.put("year", year);
		param.put("smt", smt);
		
		//교과목 상세-기본정보
		DataMap resultMap = sbjtServiceOra.getView(param);
		DataMap resultCore = sbjtServiceOra.getCoreView(param);
		DataMap resultAbi = sbjtServiceOra.getAbiView(param);
		
		if(resultMap == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		model.addAttribute("sbjtInfo", resultMap);			//교과목 상세정보
		model.addAttribute("coreInfo", resultCore);			//교과목 상세정보 전공능력/핵심역량
		model.addAttribute("abiInfo", resultAbi);			//교과목 상세정보 전공능력/핵심역량
		
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return getViewPath("/view");
	}
	/**
	 * 개설강좌
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/openLecView.json", headers="Ajax")
	public ModelAndView openLecView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
			
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		int year = StringUtil.getInt(request.getParameter("YEAR"));
		String subjectCd = request.getParameter("SUBJECT_CD");
		String deptCd = request.getParameter("DEPT_CD");
		String smt = request.getParameter("SMT");
		
		
		param.put("year", year);
		param.put("subjectCd", subjectCd);
		param.put("deptCd", deptCd);
		param.put("smt", smt);
		list = sbjtServiceOra.getCoursesOffered(param);
		
		model.addAttribute("list", list); // 개설과목

		
		return mav;
	}
	
	/**
	 * 강좌 개설 이력(최근 5년)
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/classView.json", headers="Ajax")
	public ModelAndView classView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		int fnIdx = attrVO.getFnIdx();
		
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		JSONObject settingInfo = attrVO.getSettingInfo();
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
			
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 6;	// 페이지당 목록 수
		int listMaxUnit = 10;	// 최대 페이지당 목록 수 
		int listUnitStep = 6;	// 페이지당 목록 수 증가값
		
		//int recordCountPerPage = 5;	
		int pageSize = 10;			//페이징 리스트의 사이즈(페이징을 1부터 몇까지 보여줄껀지)
		int pageUnit = 6;			//한 페이지에 게시되는 게시물 건수(표 데이터 갯수)
		int page = StringUtil.getInt(request.getParameter("page"), 1); // 현재 페이지 index
		

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit );	//한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(pageSize); 						//페이징 리스트의 사이즈

		
		// 모듈정보
		List<Object> list, list2 = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		int year = StringUtil.getInt(request.getParameter("YEAR"));
		String subjectCd = request.getParameter("SUBJECT_CD");
		String deptCd = request.getParameter("DEPT_CD");
		String smt = request.getParameter("SMT");
		String type = request.getParameter("type");
		String cSmt = request.getParameter("cSmt");
		
		int endNum = page * 6;
		int startNum = endNum - 5;
		
		int agoYear = year -4;
		
		
		param.put("startNum", startNum);
		param.put("endNum", endNum);
		
		param.put("year", year);
		param.put("agoYear", agoYear);
		param.put("subjectCd", subjectCd);
		param.put("deptCd", deptCd);
		param.put("smt", smt);
		param.put("type", type);
		param.put("cSmt", cSmt);
		list = sbjtServiceOra.getLectureList(param);
		//list2 = sbjtServiceOra.getCoursesOffered(param);
		
		
		// 2.2 목록수
    	int totalCount = sbjtServiceOra.getLectureCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
		
		model.addAttribute("list", list);	// 강좌이력
		//model.addAttribute("list2", list2); // 개설과목
		model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보

		
		
		return mav;
	}
	
	/**
	 * 메인페이지(장바구니용 개설이력)
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/classList.json", headers="Ajax")
	public ModelAndView classList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		int year = StringUtil.getInt(request.getParameter("YEAR"));
		String subjectCd = request.getParameter("SUBJECT_CD");
		String deptCd = request.getParameter("DEPT_CD");
		
		int agoYear = year -2;
		
		param.put("year", year);
		param.put("agoYear", agoYear);
		param.put("subjectCd", subjectCd);
		param.put("deptCd", deptCd);
		list = sbjtServiceOra.getClassList(param);
		
		model.addAttribute("list", list);	// 개설이력

		return mav;
	}
	
	/**
	 * 강의계획서
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value="/planView.do")
	public String planView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
	
		// 모듈정보
		Map<String, Object> param = new HashMap<String, Object>();

		String subjectCd = StringUtil.getString(request.getParameter("SUBJECT_CD"));
		String deptCd = StringUtil.getString(request.getParameter("DEPT_CD"));
		String empNo = StringUtil.getString(request.getParameter("EMP_NO"));
		String year = StringUtil.getString(request.getParameter("YEAR"));
		String smt = StringUtil.getString(request.getParameter("SMT"));
		String divcls = StringUtil.getString(request.getParameter("DIVCLS"));
		
		param.put("subjectCd", subjectCd);
		param.put("deptCd", deptCd);
		param.put("empNo", empNo);
		param.put("year", year);
		param.put("smt", smt);
		param.put("divcls", divcls);
		
		DataMap dt = sbjtServiceOra.getPlanView(param);
		List<?> weekList = sbjtServiceOra.getWeekList(param);
		int bookTotalCount = sbjtServiceOra.getBookCount(param);
		List<?> bookList = sbjtServiceOra.getBookList(param);
		int linkTotalCount = sbjtServiceOra.getLinkCount(param);
		List<?> linkList = sbjtServiceOra.getLinkList(param);
		List<?> coreList = sbjtServiceOra.getCoreList(param);
		List<?> abiList = sbjtServiceOra.getAbiList(param);
		
		model.addAttribute("dt", dt);
		model.addAttribute("deptCd", deptCd);
		model.addAttribute("weekList", weekList);
		model.addAttribute("bookTotalCount", bookTotalCount);
		model.addAttribute("bookList", bookList);
		model.addAttribute("linkTotalCount", linkTotalCount);
		model.addAttribute("linkList", linkList);
		model.addAttribute("coreList", coreList);
		model.addAttribute("abiList", abiList);
		
		
		
		// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	return getViewPath("/planView");
	}
	
	/**
	 * 강의계획서
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/planView.json", headers="Ajax")
	public ModelAndView planView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		// 모듈정보
		Map<String, Object> param = new HashMap<String, Object>();

		String subjectCd = request.getParameter("SUBJECT_CD");
		String deptCd = request.getParameter("DEPT_CD");
		String empNo = request.getParameter("EMP_NO");
		String year = request.getParameter("YEAR");
		String smt = request.getParameter("SMT");
		String divcls = request.getParameter("DIVCLS");
		
		param.put("subjectCd", subjectCd);
		param.put("deptCd", deptCd);
		param.put("empNo", empNo);
		param.put("year", year);
		param.put("smt", smt);
		param.put("divcls", divcls);
		
		DataMap dt = sbjtServiceOra.getPlanView(param);
		List<?> weekList = sbjtServiceOra.getWeekList(param);
		int bookTotalCount = sbjtServiceOra.getBookCount(param);
		List<?> bookList = sbjtServiceOra.getBookList(param);
		int linkTotalCount = sbjtServiceOra.getLinkCount(param);
		List<?> linkList = sbjtServiceOra.getLinkList(param);
		List<?> coreList = sbjtServiceOra.getCoreList(param);
		List<?> abiList = sbjtServiceOra.getAbiList(param);
		
		model.addAttribute("dt", dt);
		model.addAttribute("deptCd", deptCd);
		model.addAttribute("weekList", weekList);
		model.addAttribute("bookTotalCount", bookTotalCount);
		model.addAttribute("bookList", bookList);
		model.addAttribute("linkTotalCount", linkTotalCount);
		model.addAttribute("linkList", linkList);
		model.addAttribute("coreList", coreList);
		model.addAttribute("abiList", abiList);
		
		
		return mav;
	}*/
	
	/**
	 * 관심강좌 등록 수정
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/inputLectureBKProc.json", headers="Ajax")
	public ModelAndView inputLectureBKProc(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();		
		//SSO값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		
		List<DTForm> queryList = new ArrayList<DTForm>();	//검색항목
		
		queryList.add(new DTForm("PERS_NO", loginVO.getMemberId()));
		queryList.add(new DTForm("DEPT_CLSF_CD", paramMap.get("deptClsfCd")));
		queryList.add(new DTForm("YY", paramMap.get("yy")));
		queryList.add(new DTForm("TM_GBN", paramMap.get("tmGbn")));
		queryList.add(new DTForm("HAKSU_NO", paramMap.get("haksuNo")));
		
		param.put("searchList", queryList);
		int bkCnt = sbjtService.bkLectureCount(param);
		
		int result =0;
		
		if(paramMap.get("booMarkIdx").equals("0")) {	//등록
			if(bkCnt<1) {
				param.put("queryList", queryList);			//처음 등록시
				result = sbjtService.insertBkLecture(param, request);
			}else {
				param.put("isdelete", "0");					//재 등록시
				result = sbjtService.updateBkLecture(param, request);
			}
		}else {
			param.put("isdelete", "1");						//삭제 시
			result = sbjtService.updateBkLecture(param, request);
		}
		
		model.addAttribute("res", result);

		return mav;
	}
	
	
	
	/**
	 * 나노디그리
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/nanoView.json", headers="Ajax")
	public ModelAndView nanoDetail(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		int fnIdx = attrVO.getFnIdx();
		logger.debug("****************nanoDetail**********apiStart****************");
		logger.debug("paramMap============>"+paramMap);
		
		//db조회-나노디그리 상단 기본정보
		List<Map<String, Object>>trackList = sbjtServiceOra.getTrackList(paramMap);
		Map<String, Object> trackMap = new HashMap<>();
		
		//나노디그리[20,30] 매트릭스[40]
		for(Map<String, Object> map : trackList) {
			if(!map.get("TRACK_GBN").equals("40")) trackMap = map;
		}
		logger.debug("trackMap========>"+trackMap);
		
		//api 호출 url
		String callApiUrl = "/api/search/nanoDetail.json";
		
		//api 호출 파라미터
		JSONObject reqJsonObj = new JSONObject();
		reqJsonObj.put("trackCd", paramMap.get("trackCd"));				//트랙코드
		reqJsonObj.put("trackScCd", paramMap.get("trackScCd"));			//트랙교과목코드
		
		//api 호출
		String responseData = null;
		try {
			responseData = ApiUtil.getRestApi(callApiUrl, ApiUtil.METHOD_POST, reqJsonObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("responseData========>"+responseData);
		
		//api 결과괎
		JSONObject responseJson = JSONObject.fromObject(responseData);
		logger.debug("responseJson========>"+responseJson);
		
		//나노디그리
		JSONArray resultArray = responseJson.getJSONArray("trackInfoList");
		logger.debug("trackInfoList======nano==>"+resultArray);
		
		// 3. 속성 setting
		model.addAttribute("trackMap", trackMap);		
		model.addAttribute("resultArray", resultArray);		
		return mv;
	}*/
	
	/**
	 * 매트릭스
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/matrixView.json", headers="Ajax")
	public ModelAndView matrixDetail(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		ModelAndView mv = new ModelAndView("jsonView");
		int fnIdx = attrVO.getFnIdx();
		logger.debug("****************matrixDetail**********apiStart****************");
		logger.debug("paramMap============>"+paramMap);
		
		//db조회-매트릭스 상단 기본정보
		List<Map<String, Object>>trackList = sbjtServiceOra.getTrackList(paramMap);
		Map<String, Object> trackMap = new HashMap<>();
		
		//나노디그리[20,30] 매트릭스[40]
		for(Map<String, Object> map : trackList) {
			if(map.get("TRACK_GBN").equals("40")) trackMap = map;
		}
		
		//api 호출 url
		String callApiUrl = "/api/search/matrixDetail.json";
		//api 호출 파라미터
		JSONObject reqJsonObj = new JSONObject();
		reqJsonObj.put("trackCd", paramMap.get("trackCd"));				//트랙코드
		reqJsonObj.put("trackScCd", paramMap.get("trackScCd"));			//트랙교과목코드
		
		//api 호출
		String responseData = null;
		try {
			responseData = ApiUtil.getRestApi(callApiUrl, ApiUtil.METHOD_POST, reqJsonObj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("responseData========>"+responseData);
		
		//api 결과괎
		JSONObject responseJson = JSONObject.fromObject(responseData);
		logger.debug("responseJson========>"+responseJson);
		
		//매트릭스
		JSONArray resultArray = responseJson.getJSONArray("trackInfoList");
		logger.debug("trackInfoList====matrix====>"+resultArray);
		
		// 3. 속성 setting
		model.addAttribute("trackMap", trackMap);	
		model.addAttribute("resultArray", resultArray);		
		return mv;
	}*/
	
	
	/**
	 * 강좌 개설 상제정보
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/lectureView.json", headers="Ajax")
	public ModelAndView lectureView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		logger.debug("****************lectureView**********ajaxStart****************");
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		// 모듈정보
		Map<String, Object> param = new HashMap<String, Object>();

		String subjectCd = request.getParameter("SUBJECT_CD");
		String deptCd = request.getParameter("DEPT_CD");
		String empNo = request.getParameter("EMP_NO");
		String year = request.getParameter("YEAR");
		String smt = request.getParameter("SMT");
		String divcls = request.getParameter("DIVCLS");
		
		param.put("subjectCd", subjectCd);
		param.put("deptCd", deptCd);
		param.put("empNo", empNo);
		param.put("year", year);
		param.put("smt", smt);
		param.put("divcls", divcls);
		
		DataMap dt = sbjtServiceOra.getLectureView(param);
		List<Object> coreList = sbjtServiceOra.getLectureCore(param);
		List<Object> abiList = sbjtServiceOra.getLectureAbi(param);
		List<Object> list = sbjtServiceOra.getEvalList(param);
		
		model.addAttribute("dt", dt);
		model.addAttribute("coreList", coreList);
		model.addAttribute("abiList", abiList);
		model.addAttribute("list", list);
		
		logger.debug("****************lectureView**********ajaxEnd****************");
		
		
		return mav;
	}
	
	/**
	 * 강의평가  => 추후 삭제 
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/evalList.json", headers="Ajax")
	public ModelAndView evalList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		logger.debug("****************evalList**********ajaxStart****************");
		
		ModelAndView mav = new ModelAndView("jsonView");

		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 5;	// 페이지당 목록 수
		int listMaxUnit = 10;	// 최대 페이지당 목록 수 
		int listUnitStep = 5;	// 페이지당 목록 수 증가값
		
		//int recordCountPerPage = 5;	
		int pageSize = 10;			//페이징 리스트의 사이즈(페이징을 1부터 몇까지 보여줄껀지)
		int pageUnit = 5;			//한 페이지에 게시되는 게시물 건수(표 데이터 갯수)
		int page = StringUtil.getInt(request.getParameter("page"), 1); // 현재 페이지 index
		

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit );	//한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(pageSize); 						//페이징 리스트의 사이즈

		
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		String subjectCd = request.getParameter("SUBJECT_CD");
		String empNo = request.getParameter("EMP_NO");
		String year = request.getParameter("YEAR");
		
		int endNum = page * 5;
		int startNum = endNum - 4;
		
		
		param.put("startNum", startNum);
		param.put("endNum", endNum);
		
		
		param.put("subjectCd", subjectCd);
		param.put("empNo", empNo);
		param.put("year", year);
		list = sbjtServiceOra.getEvalList(param);
		
		// 2.2 목록수
    	int totalCount = sbjtServiceOra.getEvalCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
		
		model.addAttribute("list", list);
		model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보

		
		logger.debug("****************evalList**********ajaxEnd****************");
		
		
		return mav;
	}*/
	
	/**
	 * 수강생통계
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/statView.json", headers="Ajax")
	public ModelAndView statDetail(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model,@RequestParam Map<String, Object> paramMap) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 1. 페이지정보 setting
		
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		String subjectCd = request.getParameter("SUBJECT_CD");
		String deptCd = request.getParameter("DEPT_CD");
		String year = request.getParameter("YEAR");
		String passYear = "";
		if (year != null && !year.isEmpty()) {
			int yearInt = Integer.parseInt(year);
	        passYear = Integer.toString(yearInt - 2);
		} else {
			// 년도 설정
			Date today = new Date();
			
			// Calendar 인스턴스를 현재 날짜로 설정
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(today);

	        // 5년 전으로 설정
	        calendar.add(Calendar.YEAR, -2);
	        
	        // 5년 전의 날짜 가져오기
	        Date yearsAgo = calendar.getTime();
			
			year = DateUtil.getDateFormat(today, "yyyy");
			passYear = DateUtil.getDateFormat(yearsAgo, "yyyy");
		}
		
		
		
		
		param.put("nowYear", year);
		param.put("passYear", passYear);
		param.put("subjectCd", subjectCd);
		param.put("deptCd", deptCd);
		list = sbjtServiceOra.getTotalStudent(param);
		
		// 2.2 목록수
    	//int totalCount = sbjtServiceOra.getLectureCount(param);
		
		
		model.addAttribute("list", list);
		

		
		logger.debug("****************statView**********ajaxEnd****************");
		
		
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
		
		String listName = "list.do";
		String viewName = "view.do";
		String inputName = "input.do";
		String inputProcName = "inputProc.do";
		String deleteProcName = "deleteProc.do";
		String deleteListName = "deleteList.do";
		String imageName = "image.do";
		String movieName = "movie.do";
		String downloadName = "download.do";
		
		if(useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, null, null, pageName, idxName, listName, viewName, null, null, null, null, inputName, inputProcName, deleteProcName, deleteListName, imageName, movieName, downloadName);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		fn_setCommonAddPath(request, attrVO);
	}
	
	public void fn_setCommonAddPath(HttpServletRequest request, @ModuleAttr ModuleAttrVO attrVO) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
		String mId = "?mId=" + request.getParameter("mId");
	
		
		//북마크 등록
		request.setAttribute("URL_INPUTCOURBKPROC", "inputCourBKProc.json" + baseQueryString);
		request.setAttribute("URL_INPUTLECTUREBKPROC", "inputLectureBKProc.json" + baseQueryString);
		
		//주관대학 3차 셀렉트 박스 
		request.setAttribute("URL_DEPARTLIST", "DepartAjax.json" + baseQueryString);
		request.setAttribute("URL_MAJORLIST", "MajorAjax.json" + baseQueryString);
		
		//상세화면내의 조회
		request.setAttribute("URL_CLASSVIEW", "classView.json" + mId + baseQueryString);
		request.setAttribute("URL_LECTUREVIEW", "lectureView.json" + mId + baseQueryString);
		request.setAttribute("URL_EVALLIST", "evalList.json" + mId + baseQueryString);
		request.setAttribute("URL_NANOVIEW", "nanoView.json" + baseQueryString);
		request.setAttribute("URL_MATIRXVIEW", "matrixView.json" + baseQueryString);
		request.setAttribute("URL_STATVIEW", "statView.json" + mId + baseQueryString);
		request.setAttribute("URL_PROFVIEW", "/web/prof/view.do?mId=43");
		request.setAttribute("URL_PLANVIEW", "preView.do" + mId + baseQueryString);
		
		request.setAttribute("URL_SEARCHUNIVLIST", "searchUnivList.json" + baseQueryString);
		request.setAttribute("URL_SEARCHSUBJECTLIST", "searchSubjectList.json" + baseQueryString);

		
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
			modiCnt = sbjtService.getAuthCount(fnIdx, param);
			
			return (modiCnt > 0);
		}
		
		return true;
	}
}
