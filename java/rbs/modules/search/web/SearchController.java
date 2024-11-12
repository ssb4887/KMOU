package rbs.modules.search.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.DateUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.egovframework.util.ApiUtil;
import rbs.modules.nonSbjt.service.NonSbjtService;
import rbs.modules.sbjt.serviceOra.SbjtServiceOra;
import rbs.modules.search.service.SearchService;

/**
 * 검색 관련 모듈
 * @author 석수빈, 이동근
 *
 */
@Controller
@ModuleMapping(moduleId="search")
@RequestMapping({"/{siteId}/search", "/{admSiteId}/menuContents/{usrSiteId}/search"})
public class SearchController extends ModuleController {
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Resource(name="searchService")
	private SearchService searchService;
	
	@Resource(name="propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name="rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name = "sbjtServiceOra")
	protected SbjtServiceOra sbjtServiceOra;
	
	@Resource(name = "nonSbjtService")
	private NonSbjtService nonSbjtService;
	
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
		HashMap<String, Object> optnHashMap = (addOptionHashMap != null) ? CodeHelper.getItemOptnHashMap(items, itemOrder, addOptionHashMap) : CodeHelper.getItemOptnHashMap(items, itemOrder);
		
		return optnHashMap;
	}
	
	
	
	/**
	 * 목록 조회
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value="/total.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		logger.debug("****************subject**********apiStart****************");
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		Map<String, Object> nonSbjtParam = new HashMap<String, Object>();
		// SSO 값 꺼내 쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String top_search = request.getParameter("isMainSearch") != null ? request.getParameter("main_search") : request.getParameter("top_search");
		String searchBoxHead = request.getParameter("searchBoxHead");
		
		model.addAttribute("top_search",top_search);
		
		if (StringUtil.isEmpty(top_search)) top_search = "";
		
		// 년도 설정
		String majorYear = request.getParameter("majorYear");
		Date today = new Date();
		String strToday = DateUtil.getDateFormat(today, "yyyy");
		
        // 3년 전으로 설정
		// Calendar 인스턴스를 현재 날짜로 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, -0);
        
        Date yearAgo = calendar.getTime();
        
        String majorStartYear = DateUtil.getDateFormat(yearAgo, "yyyy");
		
		if (StringUtil.isEmpty(majorYear)) majorYear = strToday;
		
		// API 세팅 ( 4번 호출 )
		String url = RbsProperties.getProperty("Globals.search.url");
		String endpointSbjt = RbsProperties.getProperty("Globals.search.endpoint.subject");
		String endpointNonSbjt = RbsProperties.getProperty("Globals.search.endpoint.non-subject");
		String endpointProf = RbsProperties.getProperty("Globals.search.endpoint.professor");
		String endpointMajor = RbsProperties.getProperty("Globals.search.endpoint.major");
		
		
		
		// 교과목 API 호출 파라미터
		JSONObject reqJsonObj = new JSONObject();
		
		// 파라미터 정렬 세팅
		List<HashMap<String, String>> sortList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> sortMap1 = new HashMap<String, String>();
		HashMap<String, String> sortMap2 = new HashMap<String, String>();
		HashMap<String, String> sortMap3 = new HashMap<String, String>();
		sortMap1.put("sortType", "scoreSort");
		sortMap1.put("order", "DESC");
		/*sortMap2.put("sortType", "fieldSort");
		sortMap2.put("field", "YEAR");
		sortMap2.put("order", "DESC");
		sortMap3.put("sortType", "fieldSort");
		sortMap3.put("field", "SMT_CD");
		sortMap3.put("order", "DESC");*/
		sortList.add(sortMap1);
		/*sortList.add(sortMap2);
		sortList.add(sortMap3);*/
		
		reqJsonObj.put("keyword", top_search); // 통합 검색
		reqJsonObj.put("page_num", 0); 		  // 요청 페이지 번호
		reqJsonObj.put("page_per", 4); 		  // 페이지당 목록 수
		reqJsonObj.put("start_date", majorStartYear);  // 시작날짜
		reqJsonObj.put("end_date", majorYear); 	  // 종료날짜
		reqJsonObj.put("sort", sortList);		//정렬
        
		// 교과목 API 호출
		String responseData = null;
		
		try {
			responseData = ApiUtil.getRestApi(url, endpointSbjt, ApiUtil.METHOD_POST, reqJsonObj);
			
			//log insert
			searchService.insertSearchLog(endpointSbjt, reqJsonObj, responseData, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject responseJson = JSONObject.fromObject(responseData);
		
		// 전체 갯수 설정
		int sbjtCount = 0;
		
		if (responseJson.getJSONObject("data").getInt("total_count") != 0)  {
			sbjtCount = responseJson.getJSONObject("data").getInt("total_count");
		}
		
		model.addAttribute("sbjtCount", sbjtCount);
		
		// 교과목 통합 검색
		List<Map<String, String>> sbjtList = new ArrayList<>();
		
		JSONArray sbjtArray = responseJson.getJSONObject("data").getJSONArray("result");
		if (sbjtCount > 0) {
			
			// 화면에 뿌려줄 데이터 세팅
			for (Object sbjtObj : sbjtArray ) {
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
				map.put("cdtNum", sbjtJobj.getString("CDT_NUM"));
				map.put("wtimeNum", sbjtJobj.getString("WTIME_NUM"));	
				map.put("ptimeNum", sbjtJobj.getString("PTIME_NUM"));
				map.put("degGbCd", sbjtJobj.getString("DEG_GB_CD"));
				map.put("score", sbjtJobj.getString("score"));
				map.put("lectureId", sbjtJobj.getString("LECTURE_ID"));
				map.put("colgNm", sbjtJobj.getString("COLG_NM"));
				map.put("degGbNm", sbjtJobj.getString("DEG_GB_NM"));
				map.put("colgCd", sbjtJobj.getString("COLG_CD"));
				map.put("majorCd", sbjtJobj.getString("MAJOR_CD"));
				map.put("majorNm", sbjtJobj.getString("MAJOR_NM"));
				map.put("grade", sbjtJobj.getString("GRADE"));
				map.put("id", sbjtJobj.getString("id"));
				map.put("subjDescKor", sbjtJobj.getString("SUBJ_DESC"));
				map.put("subjDescEng", sbjtJobj.getString("SUBJ_DESC_ENG"));
				map.put("sisu", sbjtJobj.getString("SISU"));
				map.put("comdivNm", sbjtJobj.getString("COMDIV_NM"));
				map.put("comdivCd", sbjtJobj.getString("COMDIV_CD"));
				
				DataMap resultMap = sbjtServiceOra.getCoreAbi(map);
				
				if(resultMap != null) {
					map.put("majorAbi", (resultMap.get("MAJOR_ABI") == null ) ? "" : (String) resultMap.get("MAJOR_ABI"));
					map.put("essentialAbi", (resultMap.get("ESSENTIAL_ABI") == null ) ? "" : (String) resultMap.get("ESSENTIAL_ABI"));
				}
					

				sbjtList.add(map);
			}
		}
		
		logger.debug("****************subject**********apiEnd****************");
		
		
		/*
		 * 비교과 통합 검색
		 */
		logger.debug("****************nonSbjt**********apiStart****************");
		// 비교과 API 호출 파라미터
		reqJsonObj = new JSONObject();
		reqJsonObj.put("keyword", top_search); // 통합 검색
		reqJsonObj.put("page_num", 0); 		  // 요청 페이지 번호
		reqJsonObj.put("page_per", 4); 		  // 페이지당 목록 수
		String sortString = "["
			    + "{"
			    + "    \"field\": \"SIGNIN_END_RANK\","
			    + "    \"sortType\": \"fieldSort\","
			    + "    \"order\": \"ASC\""
			    + "},"
			    + "{"
			    + "    \"field\": \"SIGNIN_END_DATE\","
			    + "    \"sortType\": \"fieldSort\","
			    + "    \"order\": \"ASC\""
			    + "}"
			    + "]";

		JSONArray sortArray = JSONArray.fromObject(sortString);
		reqJsonObj.put("sort", sortArray);
		reqJsonObj.put("student_no", "");
        
		// 비교과 API 호출
		responseData = null;
		
		try {
			responseData = ApiUtil.getRestApi(url, endpointNonSbjt, ApiUtil.METHOD_POST, reqJsonObj);
			
			//log insert
			searchService.insertSearchLog(endpointNonSbjt, reqJsonObj, responseData, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseJson = null;
		responseJson = JSONObject.fromObject(responseData);
		
		// 전체 갯수 설정
		int nonSbjtCount = 0;
		JSONObject jsonResponseData = null;
		JSONArray resultArray = null;
		List<Object> nonSbjtResult = null;
		List<Map<String, String>> nonSbjtList = new ArrayList<>();
		
		if (responseJson.getJSONObject("data").getInt("total_count") != 0)  {
			nonSbjtCount = responseJson.getJSONObject("data").getInt("total_count");
			
    		jsonResponseData =  JSONObject.fromObject(responseData);
    		resultArray = jsonResponseData.getJSONObject("data").getJSONArray("result");
    		
    		//검색어가 있을때(서치API검색)
    		// in 조건 쿼리를 위한 idList('idx_tidx')
    		List<String> idList = new ArrayList<>();
    		
    		// result 배열에서 id 값 추출
    		for (int i = 0; i < resultArray.size(); i++) {
    			JSONObject item = resultArray.getJSONObject(i);
    			String id = item.getString("id");
    			idList.add(id);
    		}
    		
    		nonSbjtParam.put("idList", idList); 
    		nonSbjtParam.put("IS_EMPTY_SEARCH", "N"); 
    		nonSbjtParam.put("ORDER_BY", "SIGNIN_END_RANK");
		}
		
		model.addAttribute("nonSbjtCount", nonSbjtCount);
		
		// 비교과 통합 검색
		
		JSONArray nonSbjtArray = responseJson.getJSONObject("data").getJSONArray("result");
		if (nonSbjtCount > 0) {
			
			//전체 갯수 설정
			DataMap rawTotCnt = nonSbjtService.getInitNonSbjtListCount(nonSbjtParam);
			int totalCount = nonSbjtCount;
	    	
	    	//페이징 설정
	    	RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
	    	int listUnit = 4;	// 페이지당 목록 수
	    	int listMaxUnit = 5;	// 최대 페이지 수 
	    	int listUnitStep = 10;	// 페이지당 목록 수 증가값
	    	
	    	int pageUnit = 4;
	    	int pageSize = 10;	
	    	
	    	String pageNum = reqJsonObj.get("page_num").toString();
	    	//필터를 쿼리 조건 방식으로 했을때의 페이징 설정
//	    	String pageNum = rawJsonObj.getString("PAGE_NUM");
	    	
	    	int currentPage = StringUtil.getInt(pageNum) + 1; // 현재 페이지 index
	    	
//	    	int totalCount = (isEmptySearch.equals("N")) ?  JSONObject.fromObject(jsonResponseData).getJSONObject("data").getInt("total_count") : totCnt;
	    	//필터를 쿼리 조건 방식으로 했을때의 페이징 설정
//	    	int totalCount = totCnt;
	    	
	    	paginationInfo.setUnitBeginCount(listUnit);
	    	paginationInfo.setUnitEndCount(listMaxUnit);
	    	paginationInfo.setUnitStep(listUnitStep);
	    	paginationInfo.setCurrentPageNo(currentPage);
	    	paginationInfo.setRecordCountPerPage(pageUnit);
	    	paginationInfo.setPageSize(pageSize);
	    	paginationInfo.setTotalRecordCount(totalCount);
	    	
	    	if(totalCount > 0) {
	    		nonSbjtParam.put("firstIndex", 0);
	    		//필터를 쿼리 조건 방식으로 했을때의 페이징 설정
//	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex() );
	    		
	    		nonSbjtParam.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		nonSbjtParam.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    		
	    		nonSbjtResult = nonSbjtService.getNonSbjtList(nonSbjtParam) ;
	    	}
	    	
	        for (Object obj : nonSbjtResult) {
	        	Map<String, Object> nonSbjtMap = (Map<String, Object>) obj;
	            
	            Map<String, String> map = new HashMap<>();
	            //우선 다 가져오자
	            map.put("TITLE", String.valueOf(nonSbjtMap.get("TITLE")));
	            map.put("CONTACT", String.valueOf(nonSbjtMap.get("CONTACT")));
	            map.put("COVER", String.valueOf(nonSbjtMap.get("COVER")));
	            map.put("END_DATE", String.valueOf(nonSbjtMap.get("END_DATE")));
	            map.put("TOPIC", String.valueOf(nonSbjtMap.get("TOPIC")));
	            map.put("DEPT_NM", String.valueOf(nonSbjtMap.get("DEPT_NM")));
	            map.put("EMAIL", String.valueOf(nonSbjtMap.get("EMAIL")));
	            map.put("ABSTRACT", String.valueOf(nonSbjtMap.get("ABSTRACT")));
	            map.put("IS_DELETE", String.valueOf(nonSbjtMap.get("IS_DELETE")));
	            map.put("SEMESTER", String.valueOf(nonSbjtMap.get("SEMESTER")));
	            map.put("YEAR", String.valueOf(nonSbjtMap.get("YEAR")));
	            map.put("MAIN_CATEGORY_CD", String.valueOf(nonSbjtMap.get("MAIN_CATEGORY_CD")));
	            map.put("SCORE", String.valueOf(nonSbjtMap.get("SCORE")));
	            map.put("TYPE_NM", String.valueOf(nonSbjtMap.get("TYPE_NM")));
	            map.put("GIDX", String.valueOf(nonSbjtMap.get("GIDX")));
	            map.put("METHOD_CD", String.valueOf(nonSbjtMap.get("METHOD_CD")));
	            map.put("COLG_CD", String.valueOf(nonSbjtMap.get("COLG_CD")));
	            map.put("SUB_CATEGORY_CD", String.valueOf(nonSbjtMap.get("SUB_CATEGORY_CD")));
	            map.put("IDX", String.valueOf(nonSbjtMap.get("IDX")));
	            map.put("SIGNIN_START_DATE", String.valueOf(nonSbjtMap.get("SIGNIN_START_DATE")));
	            map.put("SUB_ABI_NM", String.valueOf(nonSbjtMap.get("SUB_ABI_NM")));
	            map.put("TIME", String.valueOf(nonSbjtMap.get("TIME")));
	            map.put("DEPARTMENT", String.valueOf(nonSbjtMap.get("DEPARTMENT")));
	            map.put("START_DATE", String.valueOf(nonSbjtMap.get("START_DATE")));
	            map.put("APPLICATION_METHOD", String.valueOf(nonSbjtMap.get("APPLICATION_METHOD")));
	            map.put("TIDX", String.valueOf(nonSbjtMap.get("TIDX")));
	            map.put("DEPT_CD", String.valueOf(nonSbjtMap.get("DEPT_CD")));
	            map.put("MAIN_ABI_NM", String.valueOf(nonSbjtMap.get("MAIN_ABI_NM")));
	            map.put("MAIN_CATEGORY_NM", String.valueOf(nonSbjtMap.get("MAIN_CATEGORY_NM")));
	            map.put("TYPE_CD", String.valueOf(nonSbjtMap.get("TYPE_CD")));
	            map.put("METHOD_NM", String.valueOf(nonSbjtMap.get("METHOD_NM")));
	            map.put("COLG_NM", String.valueOf(nonSbjtMap.get("COLG_NM")));
	            map.put("IS_COMPLETE", String.valueOf(nonSbjtMap.get("IS_COMPLETE")));
	            map.put("SUB_CATEGORY_NM", String.valueOf(nonSbjtMap.get("SUB_CATEGORY_NM")));
	            map.put("COUNT", String.valueOf(nonSbjtMap.get("COUNT")));
	            map.put("POINT", String.valueOf(nonSbjtMap.get("POINT")));
	            map.put("PROGRAM_TAG", String.valueOf(nonSbjtMap.get("PROGRAM_TAG")));
	            map.put("CONFIRM_STATUS", String.valueOf(nonSbjtMap.get("CONFIRM_STATUS")));
	            map.put("SIGNIN_END_DATE", String.valueOf(nonSbjtMap.get("SIGNIN_END_DATE")));
	            map.put("SIGNIN_END_DAY", String.valueOf(nonSbjtMap.get("SIGNIN_END_DAY")));
	            map.put("SIGNIN_START_DAY", String.valueOf(nonSbjtMap.get("SIGNIN_START_DAY")));
	            map.put("END_DAY", String.valueOf(nonSbjtMap.get("END_DAY")));
	            map.put("START_DAY", String.valueOf(nonSbjtMap.get("START_DAY")));
	            map.put("D_DAY", String.valueOf(nonSbjtMap.get("D_DAY")));
	            map.put("PARTICIPANT", String.valueOf(nonSbjtMap.get("PARTICIPANT")));
	            map.put("SIGNIN_LIMIT", String.valueOf(nonSbjtMap.get("SIGNIN_LIMIT")));


	            nonSbjtList.add(map);
	        }
		}
		logger.debug("****************nonject**********apiEnd****************");
		
		
		/* 
		 * 전공 통합 검색
		 */
		logger.debug("****************major**********apiStart****************");
		
		sortList = new ArrayList<HashMap<String, String>>();
		sortMap1 = new HashMap<String, String>();
		
		sortMap1.put("sortType", "scoreSort");
		sortMap1.put("order", "DESC");
		
		/*sortMap1.put("sortType", "fieldSort");
		sortMap1.put("field", "MAJOR_NM_KOR");
		sortMap1.put("order", "ASC");*/
		sortList.add(sortMap1);
		
		// Set Params
		reqJsonObj = new JSONObject();		
		reqJsonObj.put("keyword", top_search); // 통합 검색
		reqJsonObj.put("page_num", 0); 		  // 요청 페이지 번호
		reqJsonObj.put("page_per", 4); 		  // 페이지당 목록 수
		reqJsonObj.put("sort", sortList);		//정렬
		
		// Call API
		responseData = null;		
		try {
			responseData = ApiUtil.getRestApi(url, endpointMajor, ApiUtil.METHOD_POST, reqJsonObj);
			
			//log insert
			searchService.insertSearchLog(endpointMajor, reqJsonObj, responseData, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseJson = null;
		responseJson = JSONObject.fromObject(responseData);
		
		// Set Total Count for Paging
		int majorCount = 0;		
		if (responseJson.getJSONObject("data").getInt("total_count") != 0)  {
			majorCount = responseJson.getJSONObject("data").getInt("total_count");
		}
		model.addAttribute("majorCount", majorCount);
		
		// Search Major info
		List<Map<String, String>> majorList = new ArrayList<>();
		JSONArray majorArray = responseJson.getJSONObject("data").getJSONArray("result");

		if (majorCount > 0) {			
			for (Object majorObj : majorArray ) {
				JSONObject majorJobj = (JSONObject) majorObj;;
				Map<String, String> map = new HashMap<>();
				String talent = majorJobj.getString("TALENT").replaceAll("\"", "").replace("[", "").replace("]", "").replace(",", ", ");
				String field = majorJobj.getString("FIELD").replaceAll("\"", "").replace("[", "").replace("]", "").replace(",", ", ");
					
				map.put("MAJOR_CD", 		majorJobj.getString("MAJOR_CD"));
				map.put("COLG_CD", 			majorJobj.getString("COLG_CD"));
				map.put("COLG_NM", 			majorJobj.getString("COLG_NM"));
				map.put("DEPT_CD", 			majorJobj.getString("DEPT_CD"));
				map.put("DEPT_NM", 			majorJobj.getString("DEPT_NM"));
				map.put("MAJOR_NM_KOR", 	majorJobj.getString("MAJOR_NM_KOR"));
				map.put("MAJOR_NM_ENG", 	majorJobj.getString("MAJOR_NM_ENG"));
				map.put("MAJOR_INTRO", 		majorJobj.getString("MAJOR_INTRO"));
				map.put("GOAL", 			majorJobj.getString("GOAL"));
				map.put("TALENT", 			talent);
				map.put("FIELD", 			field);
				map.put("MAJOR_ABTY", 		majorJobj.getString("MAJOR_ABTY"));
					
				majorList.add(map);
			}
		}
		logger.debug("****************End****************");
		
		
		/*
		 *  교수 통합 검색
		 */
		logger.debug("****************professor**********apiStart****************");
		
		sortList = new ArrayList<HashMap<String, String>>();
		sortMap1 = new HashMap<String, String>();
		
		sortMap1.put("sortType", "scoreSort");
		sortMap1.put("order", "DESC");
		/*sortMap1.put("sortType", "fieldSort");
		sortMap1.put("field", "EMP_NM");
		sortMap1.put("order", "ASC");*/
		sortList.add(sortMap1);
		
		// 교수 API 호출 파라미터
		reqJsonObj = new JSONObject();
		reqJsonObj.put("keyword", top_search); // 통합 검색
		reqJsonObj.put("page_num", 0); 		  // 요청 페이지 번호
		reqJsonObj.put("page_per", 3); 		  // 페이지당 목록 수
		reqJsonObj.put("start_date", majorYear);  // 시작날짜
		reqJsonObj.put("end_date", majorYear); 	  // 종료날짜
		reqJsonObj.put("sort", sortList);		//정렬
		
		// 교수 API 호출
		responseData = null;
		
		try {
			responseData = ApiUtil.getRestApi(url, endpointProf, ApiUtil.METHOD_POST, reqJsonObj);
			
			//log insert
			searchService.insertSearchLog(endpointProf, reqJsonObj, responseData, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseJson = null;
		responseJson = JSONObject.fromObject(responseData);
		
		// 전체 갯수 설정
		int profCount = 0;
		
		if (responseJson.getJSONObject("data").getInt("total_count") != 0)  {
			profCount = responseJson.getJSONObject("data").getInt("total_count");
		}
		
		model.addAttribute("profCount", profCount);
		
		// 교수 통합 검색
		List<Map<String, String>> profList = new ArrayList<>();
		
		JSONArray profArray = responseJson.getJSONObject("data").getJSONArray("result");
		if (profCount > 0) {
			
			// 화면에 뿌려줄 데이터 세팅
			for (Object profObj : profArray ) {
				JSONObject profJobj = (JSONObject) profObj;
				Map<String, String> map = new HashMap<>();
					
				map.put("tlphon", profJobj.getString("TLPHON"));
				map.put("rsrch_realm", profJobj.getString("RSRCH_REALM"));
				map.put("emp_enm", profJobj.getString("EMP_ENM"));
				map.put("deptNm", profJobj.getString("DEPT_NM"));
				map.put("file_path", profJobj.getString("FILE_PATH"));
				map.put("empNo", profJobj.getString("EMP_NO"));
				map.put("email", profJobj.getString("EMAIL"));
				map.put("colg_nm", profJobj.getString("COLG_NM"));
				map.put("empNm", profJobj.getString("EMP_NM"));
				map.put("labRum", profJobj.getString("LABRUM"));
					

				profList.add(map);
			}
		}
		logger.debug("****************professor**********apiEnd****************");
				

		int totalCount = sbjtCount + majorCount + nonSbjtCount + profCount;
		
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("sbjtCount", sbjtCount);
		model.addAttribute("nonSbjtCount", nonSbjtCount);
		model.addAttribute("majorCount", majorCount);
		model.addAttribute("profCount", profCount);
		
		model.addAttribute("sbjtList", sbjtList);
		model.addAttribute("nonSbjtList", nonSbjtList);
		model.addAttribute("majorList", majorList);
		model.addAttribute("profList", profList);
		
		// 4. 기본 경로
		fn_setCommonPath(attrVO);
		
		return getViewPath("/total");
	}
	
	/**
	 * 만족도 조사 등록
	 * insert
	 */
	@RequestMapping(value = "/insertPoint.do", method = RequestMethod.POST)
	public ModelAndView insertPoint(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		try {
			searchService.insertPoint(reqJsonObj, request);
			
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
	 * 기본 경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name"); // 상세 조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name"); // 목록 페이징  key
		String listSearchId = "list_search";
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, listSearchId));	// 목록 검색 항목
		String[] tabBaseParams = null;
		String cateTabId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");
		
		if (!StringUtil.isEmpty(cateTabId)) tabBaseParams = new String[] {RbsProperties.getProperty("Globals.item.tab.search.pre.flag") + cateTabId};
		
		String listName = "list.do";
		String viewName = "view.do";
		String inputName = "input.do";
		String inputProcName = "inputProc.do";
		String deleteProcName = "deleteProc.do";
		String deleteListName = "deleteList.do";
		String imageName = "image.do";
		String movieName = "movie.do";
		String downloadName = "download.do";
		
		if (useSsl) {
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, null, null, pageName, idxName, listName, viewName, null, null, null, null, inputName, inputProcName, deleteProcName, deleteListName, imageName, movieName, downloadName);
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		fn_setCommonAddPath(request, attrVO);
	}
	
	public void fn_setCommonAddPath(HttpServletRequest request, @ModuleAttr ModuleAttrVO attrVO) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
		request.setAttribute("URL_TOTALSEARCH", "total.do" + baseQueryString);
		request.setAttribute("URL_SBJTLIST", "/web/sbjt/list.do?mId=42");
		request.setAttribute("URL_PROFLIST", "/web/prof/list.do?mId=43");
		request.setAttribute("URL_MAJORLIST", "/web/major/list.do?mId=44");
		request.setAttribute("URL_STUDPLANLIST", "/web/studPlan/list.do?mId=99");
		request.setAttribute("URL_SBJTVIEW", "/web/sbjt/view.do?mId=42" );
		request.setAttribute("URL_PROFVIEW", "/web/prof/view.do?mId=43");
		request.setAttribute("URL_MAJORVIEW", "/web/major/view.do?mId=44" );
		request.setAttribute("URL_CONNMAJORVIEW", "/web/major/connView.do?mId=44" );
		request.setAttribute("URL_STUDPLANVIEW", "/web/studPlan/studPlanView.do?mId=99");
	}

	/**
	 * 저장 후 되돌려줄 페이지 속성명
	 * @param settingInfo
	 * @return
	 */
	public String fn_dsetInputNpname(JSONObject settingInfo) {
		String dsetInputNpname = JSONObjectUtil.getString(settingInfo, "dset_input_npname");
		
		if (StringUtil.isEmpty(dsetInputNpname)) {
			dsetInputNpname = "LIST";
			
			return dsetInputNpname;
		}
		
		return dsetInputNpname;
	}

	
	
	

}