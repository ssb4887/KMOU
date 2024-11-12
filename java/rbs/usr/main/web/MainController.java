package rbs.usr.main.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.woowonsoft.egovframework.annotation.MenuMapping;
import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.web.NonModuleController;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.egovframework.util.ApiUtil;
import rbs.modules.board.service.BoardService;
import rbs.modules.studPlan.service.StudPlanService;
import rbs.usr.main.service.MainService;
 
@Slf4j
@Controller
@MenuMapping(uriPattern={"/{siteId}/main/*"})
@RequestMapping("/{siteId}/main")
public class MainController extends NonModuleController{
	private static final Logger logger = Logger.getLogger(MainController.class.getName());
	
	public String getViewModulePath() { return "/main"; }

	@Resource(name = "usrMainService")
	private MainService mainService;
	
	@Resource(name = "studPlanService")
	private StudPlanService studPlanService;
	
	@Resource(name = "boardService")
	private BoardService boardService;
	
	/** EgovMessageSource */
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@RequestMapping("/main.do")
	public String main(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		String viewPath = "";

		/**
		 * [회원 정리]
		 * 
		 * UNION된 회원 쿼리에서 가져온 USER_TYPE_IDX(권한숫자)를 가지고 보여줄 메인 페이지를 정한다.
		 * 추후에 VO가 정립되면 권한숫자와 함께 VO에 set까지 하는걸로 변경해야할듯
		 *
		 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		 * USERTYPE_IDX | 유저구분    	| ID구분
		 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		 * 		5		| 학생          	| 학번
		 * 		45		| 교수		| 사번
		 * 		46 		| 조교		| 사번
		 *      47      | 직원    		| 사번
		 * 
		 * */
		
		// 학번(=아이디)
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();			// 로그인 사용자 정보	
		
		String isPrivacyAgree = mainService.getIsPrivacyPolicy(loginVO.getMemberId()); 
		
		//정보제공 동의 체크
		if(isPrivacyAgree.equals("Y")) {
			//권한에 따른 화면 분류
			switch(loginVO.getUsertypeIdx()) {
			case 5 : 
				model = stdMain(model, loginVO);
				viewPath = "/main";
				break;
			case 45:
				model = profMain(model, loginVO);
				viewPath = "/profMain";
				break;
				//조교는 우선 직원과 같은 화면으로
			case 46:
				model = staffMain(model,loginVO);
				viewPath = "/staffMain";
				break;
			case 47:
				model = staffMain(model, loginVO);
				viewPath = "/staffMain";
				break;
			case -1: 
				break;					
			}			
		}else {
			viewPath = "/privacyPolicy";
		}
		
		/* 기본 경로 설정 */
		fn_setCommonPath(attrVO);					
		return getViewPath(viewPath);
	}
	
	/**
	 * 정보제공 동의
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/privacyPolicyAgree.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView updateStatus(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		

		ModelAndView mav = new ModelAndView("jsonView");
		try {			
//			동의여부 insert
			mainService.insertPrivacyPolicy();		
			
			model.addAttribute("RESULT",  "DONE" );
			
		}catch(Exception e) {
			
			model.addAttribute("RESULT", e.getMessage());
			
		}				
		return mav;
	}	
	
	/*ㅡㅡㅡㅡ학생ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/	
	private ModelMap stdMain(ModelMap model, LoginVO loginVO) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();		
		
		param.put("STUDENT_NO", loginVO.getMemberId()); 	// @param : 20121429 (졸업), 20121428 (재학-화석), 20200004(재학-전과?), 20220102, 20231102 (재학-신입)
		
		/*
		 *  학생 정보
		 */

		/* 기본정보 */
		DataMap myInfo = mainService.getMyInfomation(param);						
		model.addAttribute("USER", myInfo);
		
		param.put("HAKJUK_ST", myInfo.get("HAKJUK_ST_CODE"));	// @param : 학적구분(재학생, 졸업생)
		param.put("DEPT_CD", myInfo.get("DEPT_CD"));			// @param : 학부(과)
		param.put("MAJOR_CD", myInfo.get("MAJOR_CD"));			// @param : 전공

		
		/* 교육과정 */
		DataMap myCurriculum = mainService.getMyCurriculum(param);					
		param.put("APY_YEAR", myCurriculum == null ? null : myCurriculum.getValue(0)); // @param : 교육과정 기준년도
		
		
		/* 졸업기준학점 */
		List<Object> userGoalCdtList = mainService.getMyGoalCDT(param);
		model.addAttribute("USER_GOAL_CDT", (userGoalCdtList == null) ? null : userGoalCdtList);

		
		/* 학점정보 */
		DataMap userCdtList = mainService.getMyCDT(param);
		model.addAttribute("USER_CDT", (userCdtList == null) ? null : userCdtList);	
		
		
		/* 성적정보 */
		List<Object> userGpaList = mainService.getMyGPA(param);
		model.addAttribute("USER_GPA", (userGpaList == null) ? null : userGpaList);
		
		
		/* 학점상세정보 */
		List<Object> userCdtDtlList = mainService.getMyCdtDetail(param);
		model.addAttribute("USER_CDT_DTL", (userCdtDtlList == null) ? null : userCdtDtlList); 
		
		
		/* 장학정보 */
//		List<Object> userScholAmtList = mainService.getMyScholAmt(param);
//		model.addAttribute("USER_SCHOL_AMT", (userScholAmtList == null) ? null : userScholAmtList); 	
		
		
		/* 전공필수 이수 현황 */
//		List<Object> userMajorReqList = mainService.getMyMajorReq(param);
//		model.addAttribute("USER_MAJOR_REQ", (userMajorReqList == null) ? null : userMajorReqList); 
		
		
		/* 교양교과목 이수 현황 */
//		List<Object> userMinorReqList = mainService.getMyMinorReq(param);
//		model.addAttribute("USER_MINOR_REQ", (userMinorReqList == null) ? null : userMinorReqList); 	
		
		/* 교육과정 내 교양교과목 정보 */
//		List<Object> userCurrMinorList = mainService.getMyCurrMinor(param);
//		model.addAttribute("USER_CURR_MINOR", (userCurrMinorList == null) ? null : userCurrMinorList); 	
		
		
		/* 핵심역량진단 정보 */
		List<Object> userCoreCompDiagList = mainService.getCoreCompDiagnosis(param);
		model.addAttribute("USER_CORE_DIAGNOSIS", (userCoreCompDiagList == null) ? null : userCoreCompDiagList); 	
		
		
		/* 전공역량진단 정보 */
		List<Object> userMajorDiagList = mainService.getMajorCompDiagnosis(param);
		model.addAttribute("USER_MAJOR_DIAGNOSIS", (userMajorDiagList == null) ? null : userMajorDiagList); 
	
        return model;
	}
	
	/*ㅡㅡㅡㅡ교수ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/	
	private ModelMap profMain(ModelMap model, LoginVO loginVO) throws Exception {	
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("MEMBER_ID", loginVO.getMemberId());
		param.put("MEMBER_DEPT_CD", loginVO.getDeptCd());
		
		/* 탭별 카운트 */
		model.addAttribute("ALL_TAB_CNT", (studPlanService.getAllTabCnt(param).size() < 1) ? null : studPlanService.getAllTabCnt(param));
		
		/* 승인요청 */
		model.addAttribute("RA", (studPlanService.getRAList(param).size() < 1) ? null : studPlanService.getRAList(param));
		
		/* 취업률 */
		model.addAttribute("UNIV_EMPLOYMENT_RATE", (mainService.getEmploymentRate().size() < 1) ? null : mainService.getEmploymentRate());
		
		return model;
	}
	
	/*ㅡㅡㅡㅡ직원ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ*/	
	private ModelMap staffMain(ModelMap model, LoginVO loginVO) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();		
		return model;
	}
	
	
		
	@RequestMapping("/search.do")
	public String search(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		DataForm queryString = attrVO.getQueryString();
		
		JSONArray siteMenuJsonArray = JSONObjectUtil.getJSONArray(JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONArray(request.getAttribute("siteMenuList")), 0), "menu-list");		// 전체 메뉴
		JSONObject siteMenus = JSONObjectUtil.getJSONObject(request.getAttribute("siteMenus"));																							// 메뉴정보
		JSONObject boardTotalSetting = ModuleUtil.getModuleTotalSettingObject(RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_BOARD"));
		
		JSONArray boardMenuJsonArray = fn_getBoardMenuJsonArray(null, siteMenuJsonArray, siteMenus, boardTotalSetting);

		Map<Object, List<Object>> boardListHashMap = mainService.getBoardListHashMap(queryString, 5, boardMenuJsonArray, boardTotalSetting);
		
		model.addAttribute("boardMenuJsonArray", boardMenuJsonArray);
		model.addAttribute("boardListHashMap", boardListHashMap);
		
		JSONArray contentsMenuJsonArray = fn_getContentsMenuJsonArray(null, siteMenuJsonArray, siteMenus);

		Map<Object, List<Object>> contentsListHashMap = mainService.getContentsListHashMap(queryString, contentsMenuJsonArray);
		
		model.addAttribute("contentsMenuJsonArray", contentsMenuJsonArray);
		model.addAttribute("contentsListHashMap", contentsListHashMap);
		
		return getViewPath("/search");
	}
	
	
	/* 
	 * AI 추천 교과목 
	 * */
	@RequestMapping(value = "/aiRecommendSubject.json", headers="Ajax")
	public ModelAndView aiRecommendSubject(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		List<Object> resultList = new ArrayList<Object>();
		
		param.put("STUDENT_NO", request.getParameter("STD_NO"));
		param.put("HAKJUK_ST", request.getParameter("HAKJUK_ST_CODE"));
		param.put("DEPT_CD", request.getParameter("DEPT_CD"));
		param.put("MAJOR_CD", request.getParameter("MAJOR_CD"));

		// 전공
		String url = RbsProperties.getProperty("Globals.ai.recommend.url");
		String endpoint = RbsProperties.getProperty("Globals.ai.recommend.endpoint.major");

		resultList = fn_getAiRecommendSubject(mainService, param, url, endpoint);
		model.addAttribute("AI_MAJOR", resultList);
	
		// 교양
		endpoint = RbsProperties.getProperty("Globals.ai.recommend.endpoint.nonmajor");

		resultList = fn_getAiRecommendSubject(mainService, param, url, endpoint);
		model.addAttribute("AI_MINOR", resultList);
		
		resultList = null;

		return mav;
	}
	
	/* 
	 * AI 추천 비교과 
	 * */
	@RequestMapping(value = "/aiRecommendCourse.json", headers="Ajax")
	public ModelAndView aiRecommendCourse(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		List<Object> resultList = new ArrayList<Object>();
		
		param.put("STUDENT_NO", request.getParameter("STD_NO"));
		param.put("DEPT_CD", request.getParameter("DEPT_CD"));
		param.put("MAJOR_CD", request.getParameter("MAJOR_CD"));

		// 비교과
		String url = RbsProperties.getProperty("Globals.ai.recommend.url2");
		String endpoint = RbsProperties.getProperty("Globals.ai.recommend.endpoint.noncourse");

		resultList = fn_getAiRecommendNonCourse(mainService, param, url, endpoint, request);
		model.addAttribute("AI_COURSE", resultList);
		
		resultList = null;

		return mav;
	}
	
	
	/* 
	 * 학사일정 
	 * */
	@RequestMapping(value = "/aiRecommendCalendar.json", headers="Ajax")
	public ModelAndView aiRecommendCalendar(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		List<Object> resultList = new ArrayList<Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("BASE_YMD", request.getParameter("BASE_YMD"));
		
		resultList = mainService.getAcademicCalendar(param);
		
		model.addAttribute("CALENDAR", resultList);
		
		resultList = null;

		return mav;
	}

	
	public static JSONArray fn_getBoardMenuJsonArray(String menuName, JSONArray menuArray, JSONObject menus, JSONObject boardTotalSetting){
		if(JSONObjectUtil.isEmpty(menuArray)) return null;

		//JSONArray authManagerArray = ModuleUtil.getModuleAuthManagerArray("board");								// 모듈 권한 항목정보
		JSONArray boardMenuJsonArray = null;
		
		int menuLen = menuArray.size();
		for(int i = 0; i < menuLen; i++ ){
			JSONObject menu = JSONObjectUtil.getJSONObject(menus, "menu" + JSONObjectUtil.getInt(JSONObjectUtil.getJSONObject(menuArray, i), "menu_idx"));
			if(JSONObjectUtil.isEmpty(menu)) continue;

			StringBuffer totalMenuName = new StringBuffer();
			
			boolean isHidden = JSONObjectUtil.isEquals(menu, "ishidden", "1");
			if(isHidden) continue;
			
			if(!StringUtil.isEmpty(menuName)) totalMenuName.append(menuName + " > ");
			totalMenuName.append(JSONObjectUtil.getString(menu, "menu_name"));
			
			menu.put("total_menu_name", totalMenuName.toString());
			
			JSONObject boardTotalSettingItem = JSONObjectUtil.getJSONObject(boardTotalSetting, "item" + JSONObjectUtil.getInt(menu, "fn_idx"));
			menu.put("idx_name", JSONObjectUtil.getString(boardTotalSettingItem, "idx_name"));

			boolean isBoard = JSONObjectUtil.isEquals(menu, "module_id", RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_BOARD"));
			boolean isMenuAuth = MenuUtil.isAuth(menu);
			//boolean isListAuth = AuthHelper.isModuleSearchAuthenticated(menu, authManagerArray, "LST");
			// 통합검색 사용여부
			boolean useTotSearch = JSONObjectUtil.isEquals(menu, "use_totsearch", "1");
			
			if(isBoard && isMenuAuth && useTotSearch/* && isListAuth*/){
				if(JSONObjectUtil.isEmpty(boardMenuJsonArray)) boardMenuJsonArray = new JSONArray();
				boardMenuJsonArray.add(menu);
			}
			
			boolean isChildHidden = JSONObjectUtil.isEquals(menu, "child_hidden", "1");
			if(isChildHidden) continue;
			
			JSONArray childBoardMenuJsonArray = fn_getBoardMenuJsonArray(totalMenuName.toString(), JSONObjectUtil.getJSONArray(JSONObjectUtil.getJSONObject(menuArray, i), "menu-list"), menus, boardTotalSetting);
			
			if(!JSONObjectUtil.isEmpty(childBoardMenuJsonArray)){
				if(JSONObjectUtil.isEmpty(boardMenuJsonArray)) boardMenuJsonArray = new JSONArray();
				boardMenuJsonArray.addAll(childBoardMenuJsonArray);
			}
		}
		return boardMenuJsonArray;
	}
	
	public static JSONArray fn_getContentsMenuJsonArray(String menuName, JSONArray menuArray, JSONObject menus){
		if(JSONObjectUtil.isEmpty(menuArray)) return null;

		JSONArray contentsMenuJsonArray = null;
		
		int menuLen = menuArray.size();
		for(int i = 0; i < menuLen; i++ ){
			JSONObject menu = JSONObjectUtil.getJSONObject(menus, "menu" + JSONObjectUtil.getInt(JSONObjectUtil.getJSONObject(menuArray, i), "menu_idx"));
			if(JSONObjectUtil.isEmpty(menu)) continue;

			StringBuffer totalMenuName = new StringBuffer();
			
			boolean isHidden = JSONObjectUtil.isEquals(menu, "ishidden", "1");
			if(isHidden) continue;
			
			if(!StringUtil.isEmpty(menuName)) totalMenuName.append(menuName + " > ");
			totalMenuName.append(JSONObjectUtil.getString(menu, "menu_name"));
			
			menu.put("total_menu_name", totalMenuName.toString());
			
			boolean isContents = JSONObjectUtil.isEquals(menu, "module_id", RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_CONTENTS"));
			boolean isMenuAuth = MenuUtil.isAuth(menu);
			// 통합검색 사용여부
			boolean useTotSearch = JSONObjectUtil.isEquals(menu, "use_totsearch", "1");
			
			if(isContents && isMenuAuth && useTotSearch){
				if(JSONObjectUtil.isEmpty(contentsMenuJsonArray)) contentsMenuJsonArray = new JSONArray();
				contentsMenuJsonArray.add(menu);
			}
			
			boolean isChildHidden = JSONObjectUtil.isEquals(menu, "child_hidden", "1");
			if(isChildHidden) continue;
			
			JSONArray childContentsMenuJsonArray = fn_getContentsMenuJsonArray(totalMenuName.toString(), JSONObjectUtil.getJSONArray(JSONObjectUtil.getJSONObject(menuArray, i), "menu-list"), menus);
			
			if(!JSONObjectUtil.isEmpty(childContentsMenuJsonArray)){
				if(JSONObjectUtil.isEmpty(contentsMenuJsonArray)) contentsMenuJsonArray = new JSONArray();
				contentsMenuJsonArray.addAll(childContentsMenuJsonArray);
			}
		}
		return contentsMenuJsonArray;
	}
	
	/* 추천 교과목 */
	private static List<Object> fn_getAiRecommendSubject(MainService service, Map<String, Object> param, String url, String endpoint){
		List<Map<String, Object>> paramsList = new ArrayList<Map<String, Object>>();
		List<Object> resultList = new ArrayList<Object>();
		
		String stdNo = param.get("STUDENT_NO").toString();
		String deptCd = param.get("DEPT_CD").toString();
		String majorCd = param.get("MAJOR_CD").toString();
		String hakjukSt = param.get("HAKJUK_ST").toString();
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("STUDENT_NO", stdNo);
		jsonObj.put("DEPT_CD", deptCd);
		jsonObj.put("MAJOR_CD", majorCd);
		
		String rltData = null;
		try {	        
			rltData = fn_convertFromCompletableFutureToString(ApiUtil.getAiRecommendApi(url, endpoint, ApiUtil.METHOD_POST, jsonObj));     
			rltData = rltData.substring(1);
			rltData = rltData.substring(0, rltData.length() - 1);
			rltData = rltData.replaceAll("\\\\", "").toString();

			JSONArray jArray = JSONArray.fromObject(rltData);
			int jArrSize = (jArray != null) ? jArray.size() : 0;
			if(jArrSize < 1) return null;
			
			StopWatch stopWatch = new StopWatch();
			stopWatch.start("fn_getAiRecommendSubject(" + endpoint.split("/")[2] + ")");
			
			for(int idx=0; idx<jArrSize; idx++) {
				JSONObject obj = jArray.getJSONObject(idx);
				Map<String, Object> object = new HashMap<String, Object>();
				
				String[] pk = obj.get("RECOMMEND_SUBJECT_KEY").toString().split("-"); // ex) 2024-GH0210-40283 / 
				String method = obj.get("RECOMMEND_METHOD").toString();
				String order = obj.get("RECOMMEND_RANK").toString();
				
				object.put("YEAR", 			pk[0]);		// @param : 년도
				object.put("SMT", 			pk[1]);		// @param : 학기코드
				object.put("SUBJECT_CD", 	pk[2]);		// @param : 교과목코드
				object.put("DEPT_CD", 		pk[3]);		// @param : 학부(과)코드	
				object.put("STUDENT_NO", 	stdNo);		// @param : 학번
//				object.put("DEPT_CD", 		deptCd);	// @param : 학부(과)코드
				object.put("MAJOR_CD", 		majorCd);	// @param : 전공코드
				object.put("ORDER", 		order);		// @param : 순위/순서
				object.put("METHOD", 		method);	// @param : 추천방식
				object.put("HAKJUK_ST", 	hakjukSt);	// @param : 학적코드
				
				paramsList.add(object);
			}
			
			
			// 전공/교양 교과목 정보
			resultList = service.getAiMajorSubject(paramsList);
			
			paramsList = null;
			jsonObj = null;
			
			stopWatch.stop();
	        log.info("{} => [{}]", stopWatch.getLastTaskInfo().getTaskName(), DurationFormatUtils.formatDurationHMS(stopWatch.getLastTaskInfo().getTimeMillis()));
		} catch (Exception e) {
			e.printStackTrace();
			resultList = null;
			jsonObj = null;
			return null;
		}
		
		return resultList;
	}
	
	
	/* 추천 비교과 */
	@SuppressWarnings("unchecked")
	private static List<Object> fn_getAiRecommendNonCourse(MainService service, Map<String, Object> param, String url, String endpoint, HttpServletRequest request){
		List<Map<String, Object>> paramsList = new ArrayList<Map<String, Object>>();
		List<Object> resultList = new ArrayList<Object>();
		
		 Map<String, Map<String, Object>> nonSbjtRecommendKeysMap = new LinkedHashMap<>(); // 세션에 저장할 추천비교과(현재) - 비교과 빈값검색에서 우선순위를 두기 위해 사용
		HttpSession session = request.getSession();
		
		session = request.getSession(true);
		//세션 초기화
		session.removeAttribute("nonSbjtRecommendKeys");
		
		
		
		String stdNo = param.get("STUDENT_NO").toString();
		String deptCd = param.get("DEPT_CD").toString();
		String majorCd = param.get("MAJOR_CD").toString();
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("STUDENT_NO", stdNo);
		jsonObj.put("DEPT_CD", deptCd);
		jsonObj.put("MAJOR_CD", majorCd);
		
		String rltData = null;
		try {
			rltData = fn_convertFromCompletableFutureToString(ApiUtil.getAiRecommendApi(url, endpoint, ApiUtil.METHOD_POST, jsonObj));
			rltData = rltData.substring(1);
			rltData = rltData.substring(0, rltData.length() - 1);
			rltData = rltData.replaceAll("\\\\", "").toString();

			JSONArray jArray = JSONArray.fromObject(rltData);
			int jArrSize = (jArray != null) ? jArray.size() : 0;
			if(jArrSize < 1) return null;
			
			for(int idx=0; idx<jArrSize; idx++) {				
				JSONObject obj = jArray.getJSONObject(idx);
				Map<String, Object> object = new HashMap<String, Object>();
				
				String[] pk = obj.get("RECOMMEND_NONCOURSE_KEY").toString().split("-"); // ex) 2024-GH0210-40283 / 
				String method = obj.get("RECOMMEND_METHOD").toString();
				String order = obj.get("RECOMMEND_RANK").toString();
				String period = (obj.has("RECOMMEND_PERIOD")) ? obj.get("RECOMMEND_PERIOD").toString() :obj.get("RECOMMEND_TYPE").toString() ;
				
				object.put("pidx", 		pk[0]);		// @param : 프로그램 고유값
				object.put("tidx", 		pk[1]);		// @param : 주제번호
				object.put("METHOD", 	method);	// @param : 추천방식(1:전체, 2:핵심역량, 3:유사사용자, 4:개인선호도)
				object.put("ORDER", 	order);		// @param : 랭킹(순위)/순서
				object.put("PERIOD", 	period);	// @param : 기간(현재/과거)
				
				paramsList.add(object);
				
	            // 현재(1)인 경우에만 키와 랭크를 Map에 추가하여 배열에 저장
	            if ("1".equals(period)) {
	                String key = pk[0] + "_" + pk[1];
	                int rank = Integer.parseInt(order);
	                
	                // 이미 존재하는 키라면 더 순위가 낮은걸로 대체
	                if (!nonSbjtRecommendKeysMap.containsKey(key) || rank < (int)nonSbjtRecommendKeysMap.get(key).get("rank")) {
	                    Map<String, Object> recommendKey = new HashMap<>();
	                    recommendKey.put("key", key);
	                    recommendKey.put("rank", rank);
	                    nonSbjtRecommendKeysMap.put(key, recommendKey);
	                }
	            }
			}
			
			// 비교과 교과목 정보
			List<Object> dataList = service.getAiNonCourse(paramsList);
			if(dataList != null && dataList.size() > 0) {				
				int idx = 0;
				for(Object obj : dataList) {
					Map<String, Object> object = (Map<String, Object>) obj;
					object.put("METHOD", jArray.getJSONObject(idx).get("RECOMMEND_METHOD").toString());
					object.put("ORDER", jArray.getJSONObject(idx).get("RECOMMEND_RANK").toString());
					object.put("PERIOD", (jArray.getJSONObject(idx).has("RECOMMEND_PERIOD")) ? jArray.getJSONObject(idx).get("RECOMMEND_PERIOD").toString() : jArray.getJSONObject(idx).get("RECOMMEND_TYPE").toString());
					
					resultList.add(object);
					idx++;
				}
			}
			
			dataList = null;
			paramsList = null;
			jsonObj = null;
			
	        // 세션에 nonSbjtRecommendKeys 저장
	        List<Map<String, Object>> nonSbjtRecommendKeys = new ArrayList<>(nonSbjtRecommendKeysMap.values());
	        session.setAttribute("nonSbjtRecommendKeys", nonSbjtRecommendKeys);
			
		} catch (Exception e) {
			e.printStackTrace();			
			paramsList = null;
			jsonObj = null;			
			return null;
		}
		
		return resultList;
	}
	
	/* 
	 * 해양대 게시판 공지사항, 취업정보
	 * */
	@RequestMapping(value = "/getBoardList.json", headers="Ajax")
	public ModelAndView getBoardList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		List<Object> resultList = new ArrayList<Object>();
		
		String bbs = request.getParameter("BBS_LIST");
		
		String[] bbsList = bbs.split(",");
		param.put("BBS_LIST", bbsList);
		
		resultList = boardService.getMainList(param);
		model.addAttribute("BOARD_LIST", resultList);
		
		resultList = null;

		return mav;
	}
	
	
	/* 
	 * CompletableFuture<String> Class type 변환 함수(to String) 
	 */
	public static String fn_convertFromCompletableFutureToString(CompletableFuture<String> response) {
	    String result;
	    try {
	        result = response.get(); // CompletableFuture의 결과를 기다림
	    } catch (InterruptedException | ExecutionException e) {
	        e.printStackTrace();
	        result = ""; // 에러가 발생한 경우에는 빈 문자열이나 다른 적절한 값으로 처리
	    }
	    return result;
	}
	
	/**
	 * 경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setCommonAddPath(attrVO, request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setCommonAddPath(ModuleAttrVO attrVO, HttpServletRequest request) {
		String mappingUrlAiSubect = "aiRecommendSubject.json?mId=1";
		String mappingUrlAiCourse = "aiRecommendCourse.json?mId=1";
		String mappingUrlAiCalendar = "aiRecommendCalendar.json?mId=1";		
	
		request.setAttribute("URL_AI_RECOMMEND_SUBJECT", mappingUrlAiSubect);
		request.setAttribute("URL_AI_RECOMMEND_COURSE", mappingUrlAiCourse);
		request.setAttribute("URL_AI_RECOMMEND_CALENDAR", mappingUrlAiCalendar);
	}

}
