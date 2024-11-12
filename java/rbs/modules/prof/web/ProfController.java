package rbs.modules.prof.web;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
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
import rbs.modules.code.serviceOra.CodeOptnServiceOra;
import rbs.modules.majorInfo.service.MajorInfoService;
import rbs.modules.prof.service.ProfService;
import rbs.modules.sbjt.web.SbjtController;
import rbs.modules.search.service.SearchService;

/**
 * 샘플 모듈<br/>
 * : 통합관리시스템 > 메뉴콘텐츠관리, 통합관리시스템 > 기능등록관리, 사용자 사이트 에서 사용
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="prof")
@RequestMapping({"/{siteId}/prof", "/{admSiteId}/menuContents/{usrSiteId}/prof"})
public class ProfController extends ModuleController {
	private static final Logger logger = LoggerFactory.getLogger(SbjtController.class);
	
	@Resource(name="profService")
	private ProfService profService;
	
	@Resource(name="searchService")
	private SearchService searchService;	
	
	@Resource(name="propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name="rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Resource(name="codeOptnServiceOra")
	protected CodeOptnServiceOra codeOptnServiceOra;
	
	@Resource(name = "majorInfoService")
	protected MajorInfoService majorInfoService;
	
	
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
		
		return  optnHashMap;
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
	@RequestMapping(value="/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		// SSO 로그인 세션 정보
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		//String memberId = loginVO.getMemberId();
		//List<Object> userInfo = inuUserService.getInuUserInfo(memberId);
		
		//System.out.println(">>>>>>>>>>>>>>>>>>userInfo : " + userInfo);
		
		
		model.addAttribute("collegeList", majorInfoService.getCollegeList());
		
    	// 4. 기본 경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/list");
	}
	
	
	
	/**
	 * 목록 조회(비동기)
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value="/initProfList.do", headers="Ajax")
	public ModelAndView initProfList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		
		// SSO 로그인 세션 정보
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		
		// 1. 페이지 정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 12;	// 페이지 당 목록 수
		int listMaxUnit = 5;	// 최대 페이지 당 목록 수 
		int listUnitStep = 12;	// 페이지 당 목록 수 증가 값
		int pageUnit = 12;
		int pageSize = 10;
		int page = StringUtil.getInt(request.getParameter("page"), 1); // 현재 페이지 index

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
		
		int endNum = page * 12;
		int startNum = endNum - 11;
		
		Map<String, Object> param = new HashMap<String, Object>();
		String univ = request.getParameter("univ");
		String depart = request.getParameter("depart");
		String major = request.getParameter("major");
		String orderBy = request.getParameter("orderBy");
		if(StringUtil.isEquals(orderBy, "byName")  ) {
			param.put("field", "EMP_NM");
			param.put("order", "ASC");
		} else if(StringUtil.isEquals(orderBy, "byMajor")  ) {
			param.put("field", "DEPT_NM");
			param.put("order", "ASC");
		} 
		
		param.put("startNum", startNum);
		param.put("endNum", endNum);
		param.put("university", univ);						
		param.put("department", depart);
		
		List<Object> sbjtList = new ArrayList<>();
		
		//전체 갯수 설정
		DataMap totalCount = profService.getInitProfListCount(param);
		int totCnt = ((BigDecimal) totalCount.get("TOTAL_COUNT")).intValue();
		model.addAttribute("totalCount", totCnt);
		paginationInfo.setTotalRecordCount(totCnt);
		
		model.addAttribute("paginationInfo", paginationInfo);
		
		sbjtList = profService.getInitProfList(param);
		model.addAttribute("profList", (sbjtList == null) ? null : sbjtList);
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return mav;
	}
	
	
	/**
	 * 목록 조회(비동기)
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value="/profList.do", headers="Ajax")
	public ModelAndView profList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		// SSO 로그인 세션 정보
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		//String memberId = loginVO.getMemberId();
		//List<Object> userInfo = inuUserService.getInuUserInfo(memberId);
		
		//System.out.println(">>>>>>>>>>>>>>>>>>userInfo : " + userInfo);
		
		String top_search = request.getParameter("top_search");
		
		// 1. 페이지 정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 12;	// 페이지 당 목록 수
		int listMaxUnit = 5;	// 최대 페이지 당 목록 수 
		int listUnitStep = 12;	// 페이지 당 목록 수 증가 값
		int pageUnit = 12;
		int pageSize = 10;
		int page = StringUtil.getInt(request.getParameter("page"), 1); // 현재 페이지 index

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
		// 년도 설정
		String majorYear = request.getParameter("majorYear");
		Date today = new Date();
		
		// Calendar 인스턴스를 현재 날짜로 설정
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        // n년 전으로 설정
        calendar.add(Calendar.YEAR, -10);
        
        // n년 전의 날짜 가져오기
        Date yearsAgo = calendar.getTime();
        
        String strToday = DateUtil.getDateFormat(today, "yyyy");
		String agoYear = DateUtil.getDateFormat(yearsAgo, "yyyy");
		
		//if (StringUtil.isEmpty(majorYear)) majorYear = strToday;
		
		// 2. DB
		
		if(request.getParameter("top_search") != null && !"".equals(request.getParameter("top_search"))) {
		
			// API setting
			String url = RbsProperties.getProperty("Globals.search.url");
			String endpoint = RbsProperties.getProperty("Globals.search.endpoint.professor");
			JSONObject reqJsonObj = new JSONObject();
			// 검색 파라미터 설정
			String keyword;
			String univ = request.getParameter("univ");
			String depart = request.getParameter("depart");
			String major = request.getParameter("major");
			String orderBy = request.getParameter("orderBy");
			String flagLog = request.getParameter("flagLog");
			
			//파라미터 정렬 세팅
			List<HashMap<String, String>> sortList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> sortMap = new HashMap<String, String>();
			if(StringUtil.isEquals(orderBy, "byName")  ) {
				sortMap.put("sortType", "fieldSort");
				sortMap.put("field", "EMP_NM");
				sortMap.put("order", "ASC");
				sortList.add(sortMap);
			} else if(StringUtil.isEquals(orderBy, "byMajor")  ) {
				sortMap.put("sortType", "fieldSort");
				sortMap.put("field", "DEPT_NM");
				sortMap.put("order", "ASC");
				sortList.add(sortMap);
			} else {
				sortMap.put("sortType", "scoreSort");
				sortMap.put("order", "DESC");
				sortList.add(sortMap);
			} 
			
			
			reqJsonObj.put("keyword", top_search);					//검색어
			reqJsonObj.put("page_num", page-1);						//요청 페이지 번호
			reqJsonObj.put("page_per", pageUnit);					//페이지당 목록 수
			reqJsonObj.put("sort", sortList);						//정렬
			reqJsonObj.put("start_date", agoYear);					//시작날짜
			reqJsonObj.put("end_date", strToday);					//종료날짜
			reqJsonObj.put("university", univ);						//대학코드   309000
			reqJsonObj.put("department", depart);					//학과코드   309050
			reqJsonObj.put("major", major);							//전공코드   309050
			
			if (null != request.getParameter("top_search")) {
				keyword = request.getParameter("top_search");
				
				reqJsonObj.put("keyword", keyword);
			}
			
			///////////// 페이지 세팅
			reqJsonObj.put("pageNo", 1);
			
			if (null != request.getParameter("page")) {
				int startNum = Integer.parseInt(request.getParameter("page"));
				
				System.out.println("**********************startNum : " + startNum);
				
				reqJsonObj.put("pageNo", startNum);
			}
			
			String responseData = null;
			
			//api 호출
			try {
				responseData = ApiUtil.getRestApi(url, endpoint, ApiUtil.METHOD_POST, reqJsonObj);
				
				//log insert
				if(StringUtil.isEquals(flagLog, "Y")) {
					searchService.insertSearchLog(endpoint, reqJsonObj, responseData, request);
				}
				
				System.out.println("responseData : " + responseData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONObject responseJson = JSONObject.fromObject(responseData);
			
	        // JSON 객체 생성
			//JSONObject reqJsonObj2 = JSONObject.fromObject(responseData);
			// 전체 갯수 설정
			int totalCount = 0;
			if(null != responseJson.getJSONObject("data").getString("total_count")) {
				totalCount = responseJson.getJSONObject("data").getInt("total_count");
			}
			
	        model.addAttribute("totalCount", totalCount);
	        paginationInfo.setTotalRecordCount(totalCount);
	        model.addAttribute("paginationInfo", paginationInfo);
	
	        // 교수 목록 추출
	        JSONArray professorList = responseJson.getJSONObject("data").getJSONArray("result");
	
	        System.out.println("professorList : " + professorList);
	        
	        
	        // 교수 정보를 담을 List 생성
	        List<Map<String, String>> professors = new ArrayList<>();
	
	        // 각 교수 정보를 HashMap에 담기
	        for (Object professorObj : professorList) {
	        	JSONObject professorJson = (JSONObject) professorObj;
	            Map<String, String> professorMap = new HashMap<>();
	            
	            
	            professorMap.put("tlphon", professorJson.getString("TLPHON"));
	            professorMap.put("rsrch_realm", professorJson.getString("RSRCH_REALM"));
	            professorMap.put("emp_enm", professorJson.getString("EMP_ENM"));
	            professorMap.put("deptNm", professorJson.getString("DEPT_NM"));
	            professorMap.put("file_path", professorJson.getString("FILE_PATH"));
	            professorMap.put("empNo", professorJson.getString("EMP_NO"));
	            professorMap.put("email", professorJson.getString("EMAIL"));
	            professorMap.put("colg_nm", professorJson.getString("COLG_NM"));
	            professorMap.put("empNm", professorJson.getString("EMP_NM"));
	            professorMap.put("labRum", professorJson.getString("LABRUM"));
	            professorMap.put("location", professorJson.getString("LOCATION"));
	            
	           /* String empNo = professorJson.getString("EMP_NO");
	            Map<String, Object> param = new HashMap<String, Object>();
	            param.put("empNo", empNo);
	           
	            if("".equals(professorJson.getString("FILE_PATH"))) {
	            	DataMap profMap = profService.getProfSn(param);
	            	if(!"".equals(profMap.get("V_TEA_SN")) && profMap.get("V_TEA_SN") != null) {
	            		Map<String, Object> rshParam = new HashMap<String, Object>();
		            	rshParam.put("sn", profMap.get("V_TEA_SN"));
		            	DataMap serialMap = profService.getProfRsh(rshParam);
		            	//System.out.println(serialMap);
		            	
		            	professorMap.put("tlphon", (String) serialMap.get("V_TEA_TLPHON"));
			            professorMap.put("rsrch_realm", (String) serialMap.get("TEA_RSRCH_REALM"));
			            professorMap.put("file_path", (String) serialMap.get("V_TEA_FILE_PATH"));
			            professorMap.put("email", (String) serialMap.get("V_TEA_EMAIL"));
			            professorMap.put("labRum", (String) serialMap.get("V_TEA_LABRUM"));
			            professorMap.put("location", (String) serialMap.get("V_TEA_LOCATION"));
	            	}
	            }*/
	
	            professors.add(professorMap);
	        }
	
			model.addAttribute("profList", professors);
			model.addAttribute("searchCondition", reqJsonObj);
		}
		
		List<?> collegeList = null;
		collegeList = majorInfoService.getCollegeList();
		model.addAttribute("collegeList", collegeList);
		
    	// 4. 기본 경로
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
	 * 상세 조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value="/view.do")
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		// 1. 필수 parameter 검사 - 교수별 식별ID
		String empNo = request.getParameter("empNo");
		
		if (empNo == null) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}

		DataForm queryString = attrVO.getQueryString();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 2. DB
		// List<?> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("empNo", empNo);
		
		// 교수 정보 호출
		DataMap profMap = profService.getView(param);
		
		String main = null;
		String career = null;
		String rsrch = null;
		
		try {
			main = getStringFromObject(profMap.get("TEA_MAIN"));
            career = getStringFromObject(profMap.get("TEA_CAREER"));
            rsrch = getStringFromObject(profMap.get("TEA_RSRCH_REALM"));

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
		
		/*// Null 체크
		if (main != null) {
		    // \r\n을 <br>로 변환
		    String  formattedMain = main.replace("\r\n", "<br>").replace("\n", "<br>");
		    
		    // 변환된 값을 다시 DataMap에 넣음
		    profMap.put("TEA_MAIN", formattedMain);
		}
		
		if (career != null) {
		    // \r\n을 <br>로 변환
		    String  formattedCareer = career.replace("\r\n", "<br>").replace("\n", "<br>");
		    
		    // 변환된 값을 다시 DataMap에 넣음
		    profMap.put("TEA_CAREER", formattedCareer);
		}
		
		if (rsrch != null) {
		    // \r\n을 <br>로 변환
		    String  formattedRsrch = rsrch.replace("\r\n", "<br>").replace("\n", "<br>");
		    
		    // 변환된 값을 다시 DataMap에 넣음
		    profMap.put("TEA_RSRCH_REALM", formattedRsrch);
		}*/
		

		// 3 속성 setting
		// 3.1 항목 설정
		String submitType = "view";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 3.2 속성 setting
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder)); // 항목 코드
		model.addAttribute("submitType", submitType); // 페이지 구분
		model.addAttribute("profInfo", profMap); // 교수 상세정보
		
    	// 4. 기본 경로
    	fn_setCommonPath(attrVO);
    	
    	return getViewPath("/view");
	}
	
	 public static String convertClobToString(Clob clob) throws SQLException, IOException {
	        if (clob == null) {
	            return null;
	        }

	        StringBuilder stringBuilder = new StringBuilder();
	        Reader reader = clob.getCharacterStream();
	        char[] buffer = new char[1024];
	        int bytesRead;

	        while ((bytesRead = reader.read(buffer)) != -1) {
	            stringBuilder.append(buffer, 0, bytesRead);
	        }

	        reader.close();
	        return stringBuilder.toString();
	}
	 
	 public static String getStringFromObject(Object obj) throws SQLException, IOException {
	        if (obj instanceof String) {
	            return (String) obj;
	        } else if (obj instanceof Clob) {
	            return convertClobToString((Clob) obj);
	        }
	        return null;
	    }

	
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/subListAjax.json", headers="Ajax")
	public ModelAndView itemList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject settingInfo = attrVO.getSettingInfo();
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		int fnIdx = attrVO.getFnIdx();
		
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 3; // 페이지 당 목록 수
		int listMaxUnit = 10; // 최대 페이지 당 목록 수 
		int listUnitStep = 3; // 페이지 당 목록 수 증가 값
		int pageUnit = 3;
		int pageSize = 10;
		int page = StringUtil.getInt(request.getParameter("page"), 1); // 현재 페이지 index
			
	
		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);
		
			
		// 2.DB
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		String empNo = request.getParameter("EMP_NO");
		int year = StringUtil.getInt(request.getParameter("YEAR"));
		int agoYear = year -4;
		
		int endNum = page * 3;
		int startNum = endNum - 2;
		
		
		param.put("startNum", startNum);
		param.put("endNum", endNum);
		param.put("empNo", empNo);
		param.put("year", year);
		param.put("agoYear", agoYear);
		list = profService.getList(param);
		
		// 2.2 목록수
    	int totalCount = profService.getTotalCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
		
		model.addAttribute("list", list);
		model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
		
		return mav;
	}
	
	
	/**
	 * 대학 리스트 불러오기
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value="/searchUnivList.json", headers="Ajax")
	public ModelAndView searchUnivList(@RequestParam(value="deptCampus") String deptCampus, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<Object> haksaCode = codeOptnServiceOra.getHaksaCode(deptCampus);
		
		model.addAttribute("haksaCode", haksaCode);
		
	    return mav;
	}
	
	/**
	 * 대학 리스트 불러오기
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value="/searchSubjectList.json",  headers="Ajax")
	public ModelAndView searchSubjectList(@RequestParam(value="deptUniv") String deptUniv, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<Object> haksaCode = codeOptnServiceOra.getHaksaCode(deptUniv);
		
		model.addAttribute("haksaCode", haksaCode);
		
		return mav;
	}
	
	
	/**
	 * 관심 교과목 등록 수정
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/inputProfBKProc.json", headers="Ajax")
	public ModelAndView inputProfBKProc(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestParam Map<String, Object> paramMap) throws Exception {
		// SSO 값 꺼내쓰기
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> queryList = new ArrayList<DTForm>();
		
		
		queryList.add(new DTForm("DOC_CD", paramMap.get("docCd")));
		queryList.add(new DTForm("PERS_NO", loginVO.getMemberId()));
		param.put("searchList", queryList);
		
		int bkCnt = profService.bkProfCount(param);
		int result = 0;
		
		if (paramMap.get("booMarkIdx").equals("0")) { // 등록
			if (bkCnt<1) {
				param.put("queryList", queryList); // 처음 등록시
				
				result = profService.insertBkProf(param, request);
			} else {
				param.put("isdelete", "0"); // 재등록시
				param.put("searchList", queryList);
				
				result = profService.updateBkProf(param, request);
			}
		} else {
			param.put("isdelete", "1"); // 삭제 시
			param.put("searchList", queryList);
			
			result = profService.updateBkProf(param, request);
		}
	
		model.addAttribute("res", result);

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
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
		String searchUnivListName = "searchUnivList.json";
		String searchSubjectListName = "searchSubjectList.json";
		String bookmarkInputName = "inputProfBKProc.json";
		
		String mId = "?mId=" + request.getParameter("mId");
		
		request.setAttribute("URL_SEARCHUNIVLIST", searchUnivListName += baseQueryString);
		request.setAttribute("URL_SEARCHSUBJECTLIST", searchSubjectListName += baseQueryString);
		request.setAttribute("URL_BOOKMARKINPUT", bookmarkInputName += baseQueryString);
		
		request.setAttribute("URL_SUB_LIST_AJAX", "subListAjax.json" + mId + baseQueryString);
		
		//소속 3차 셀렉트 박스 
		request.setAttribute("URL_DEPARTLIST", "DepartAjax.json" + baseQueryString);
		request.setAttribute("URL_MAJORLIST", "MajorAjax.json" + baseQueryString);
	}

	/**
	 * 휴지통 경로
	 * @param attrVO
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name"); // 목록 페이징  key
		// 항목 설정 정보
		String listSearchId = "list_search";
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, listSearchId)), deleteListSearchParams);	// 검색 항목
		
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
	} */
	
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
	
	/**
	 * 관리 권한 체크
	 * @param settingInfo
	 * @param fnIdx
	 * @param keyIdx
	 * @param memIdx
	 * @param useReply
	 * @param pwd
	 * @return
	 
	public boolean isMngProcAuth(JSONObject settingInfo, int fnIdx, int keyIdx) {
		String columnName = JSONObjectUtil.getString(settingInfo, "idx_column");
		int modiCnt = 0;
		// 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG"); // 전체 관리
		
		if (!isMngAuth) {
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A." + columnName, keyIdx));
			
			// 전체 관리 권한 없는 경우 : 자신 글만 수정
			boolean isLogin = UserDetailsHelper.isLogin(); // 로그인한 경우
			
			if (isLogin) {
				// 로그인한 경우
				String memberIdx = null;
				LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
				
				if (loginVO != null) {
					memberIdx = loginVO.getMemberIdx();
				}
				
				if (StringUtil.isEmpty(memberIdx)) return false;
				
				param.put("AUTH_MEMBER_IDX", memberIdx);
			} else {
				// 본인인증 or 로그인 안 한 경우
				return false;
			}

			param.put("searchList", searchList);
			
			modiCnt = profService.getAuthCount(fnIdx, param);
			
			return (modiCnt > 0);
		}
		
		return true;
	} */
}