package rbs.modules.studPlan.web;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.transaction.annotation.Transactional;
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
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.modules.code.serviceOra.CodeOptnServiceOra;
import rbs.modules.prof.service.ProfService;
import rbs.modules.sbjt.service.SbjtService;
import rbs.modules.sbjt.serviceOra.SbjtServiceOra;
import rbs.modules.studPlan.service.StudPlanAdmService;
import rbs.modules.studPlan.service.StudPlanService;
import rbs.usr.main.service.MainService;


/**
 * 학생설계융합전공 Controller
 * @author	이동근
 */
@Controller
@ModuleMapping(moduleId="studPlan")
@RequestMapping("/{admSiteId}/menuContents/{usrSiteId}/studPlan")
public class StudPlanAdmController extends ModuleController {

	private static final Logger logger = LoggerFactory.getLogger(StudPlanAdmController.class);
	
	
	@Resource(name = "codeOptnServiceOra")
	protected CodeOptnServiceOra codeOptnServiceOra;
	
	@Resource(name = "sbjtService")
	private SbjtService sbjtService;
	
	@Resource(name = "studPlanService")
	private StudPlanService studPlanService;
	
	@Resource(name = "studPlanAdmService")
	private StudPlanAdmService studPlanAdmService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	

	@Resource(name = "sbjtServiceOra")
	protected SbjtServiceOra sbjtServiceOra;

	@Resource(name = "usrMainService")
	private MainService mainService;
	
	
	@Resource(name = "profService")
	private ProfService profService;
	
	
	@Resource(name = "jsonView")
	View jsonView;

/*----------학생설계전공 공통 시작----------*/	
	
	/**
	 * 학생설계전공 전공 목록
	 * @param 
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getStudPlanMajorList.do", headers="Ajax")
	public ModelAndView getStudPlanMajorList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		// 모듈정보
		List<Object> list = null;
		
		list = studPlanService.getStudPlanMajorList();
		
		model.addAttribute("STUDPLAN_MAJOR_LIST", list);
		
		return mav;
	}
	
	/**
	 * 대학 목록
	 * @param sbjtDgnRngFg(교과목 설계범위)
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getCollList.do", headers="Ajax")
	public ModelAndView getCollList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		String sbjtDgnRngFg = request.getParameter("sbjtDgnRngFg");
		param.put("SBJT_DGN_RNG_FG", sbjtDgnRngFg);
		
		list = studPlanService.getCollegeList(param);
		
		model.addAttribute("COLLEGE_LIST", list);
		
		return mav;
	}
	
	/**
	 * 학부(과) 목록
	 * @param univ
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getDeptList.do", headers="Ajax")
	public ModelAndView getDeptList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
			
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		String univ = request.getParameter("univ");
		
		
		param.put("UNIV", univ);
		list = studPlanService.getDepartList(param);
		
		model.addAttribute("DEPART_LIST", list);
		
		return mav;
	}
	
	/**
	 * 전공 목록
	 * @param depart
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getMajorList.do", headers="Ajax")
	public ModelAndView getMajorList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
			
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		String depart = request.getParameter("depart");
		
		
		param.put("DEPART", depart);
		list = studPlanService.getMajorList(param);
		
		model.addAttribute("MAJOR_LIST", list);
		
		return mav;
	}	
				
		
	/**
	 * 승인
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/judgApprove.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView judgApprove(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		

		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//상태 update
			studPlanAdmService.judgApprove(rawJsonObj, request);	

			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 승인되었습니다.");
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "승인에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}		
	
	/**
	 * 반려
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/judgReject.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView judgReject(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//상태 update
			studPlanAdmService.judgReject(rawJsonObj, request);			
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 반려되었습니다.");
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "반려에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}			

/*----------학생설계전공 공통 끝----------*/	
	
/*----------학생설계전공 창의융합교육센터 시작----------*/
	
	/* 창의융합교육센터 심의할 학생설계전공 목록 화면*/
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/eduCenterJudgList.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws Exception {
		
		List<Object> myList = null;	
		List<Object> mySbjtList = null;	
	

		
		model.addAttribute("MY_LIST",myList);
		model.addAttribute("MY_SBJT_LIST",mySbjtList);

		
		return getViewPath("/eduCenterJudgList");
	}	
	
	
	/* 창의융합교육센터 심의할 학생설계전공 조회 (ajax)*/
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/getEduCenterJudgList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getStudPlanTabList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		List<Object> list = null;
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		String searchRange = rawJsonObj.getString("SEARCH_RANGE");
		
		param.put("SEARCH_RANGE", searchRange);
		
		/*페이징*/
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 10;	// 페이지당 목록 수
		int listMaxUnit = 5;	// 최대 페이지 수 
		int listUnitStep = 10;	// 페이지당 목록 수 증가값
		
		int pageUnit = 10;
		int pageSize = 10;	
		

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(StringUtil.getInt(rawJsonObj.get("PAGE").toString()));
		paginationInfo.setRecordCountPerPage(pageUnit );	//한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(pageSize); 						//페이징 리스트의 사이즈
		
		//목록수
		int totalCount = 0;
    	totalCount = studPlanAdmService.getEduCenterJudgListCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		//리스트
    		list = studPlanAdmService.getEduCenterJudgList(param);
    	}

							
		
		
		model.addAttribute("PAGINATION_INFO", paginationInfo);					// 페이징정보
		model.addAttribute("LIST", list);
		model.addAttribute("RESULT",  "DONE");
		
		return mav;
	}		
	
	/* 심의 수정화면 */
	@RequestMapping(value = "/eduCenterJudgModify.do")
	public String eduCenterJudgModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		
    	return getViewPath("/eduCenterJudgModify");
	}
	
	/* 변경 - 심의 수정화면 */
	@RequestMapping(value = "/eduCenterChgJudgModify.do")
	public String eduCenterChgJudgModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		
		return getViewPath("/eduCenterChgJudgModify");
	}

	
	
/*----------학생설계전공 창의융합교육센터 끝----------*/
	
	
/*----------학생설계전공 학사과 시작----------*/
	
	/* 학사과 심의할 학생설계전공 목록 화면*/
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/departJudgList.do")
	public String departJudgList(@ModuleAttr ModuleAttrVO attrVO, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws Exception {
		
		List<Object> myList = null;	
		List<Object> mySbjtList = null;	
	

		
		model.addAttribute("MY_LIST",myList);
		model.addAttribute("MY_SBJT_LIST",mySbjtList);

		
		return getViewPath("/departJudgList");
	}	
	
	
	/* 학사과 심의할 학생설계전공 조회 (ajax)*/
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/getDepartJudgList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getDepartJudgList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		List<Object> list = null;
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		String searchRange = rawJsonObj.getString("SEARCH_RANGE");
		
		param.put("SEARCH_RANGE", searchRange);
		
		/*페이징*/
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 10;	// 페이지당 목록 수
		int listMaxUnit = 5;	// 최대 페이지 수 
		int listUnitStep = 10;	// 페이지당 목록 수 증가값
		
		int pageUnit = 10;
		int pageSize = 10;	
		

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(StringUtil.getInt(rawJsonObj.get("PAGE").toString()));
		paginationInfo.setRecordCountPerPage(pageUnit );	//한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(pageSize); 						//페이징 리스트의 사이즈
		
		//목록수
		int totalCount = 0;
    	totalCount = studPlanAdmService.getDepartJudgListCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		//리스트
    		list = studPlanAdmService.getDepartJudgList(param);
    	}

							
		
		
		model.addAttribute("PAGINATION_INFO", paginationInfo);					// 페이징정보
		model.addAttribute("LIST", list);
		model.addAttribute("RESULT",  "DONE");
		
		return mav;
	}		
	
	/* 심의 수정화면 */
	@RequestMapping(value = "/departJudgModify.do")
	public String departJudgModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		
    	return getViewPath("/departJudgModify");
	}
	
	/* 변경 - 심의 수정화면 */
	@RequestMapping(value = "/departChgJudgModify.do")
	public String departChgJudgModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		
		return getViewPath("/departChgJudgModify");
	}
/*----------학생설계전공 학사과 끝----------*/	
	
	
/*----------학생설계전공 교무회 및 평의원회 시작----------*/
	
	/* 교무회 및 평의원회 심의할 학생설계전공 목록 화면*/
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/affairCommitteeJudgList.do")
	public String affairCommitteeJudgList(@ModuleAttr ModuleAttrVO attrVO, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws Exception {
		
		List<Object> myList = null;	
		List<Object> mySbjtList = null;	
		
		
		
		model.addAttribute("MY_LIST",myList);
		model.addAttribute("MY_SBJT_LIST",mySbjtList);
		
		
		return getViewPath("/affairCommitteeJudgList");
	}	
	
	
	/* 교무회 및 평의원회 심의할 학생설계전공 조회 (ajax)*/
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/getAffairCommitteeJudgList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getAffairCommitteeJudgList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		List<Object> list = null;
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		String searchRange = rawJsonObj.getString("SEARCH_RANGE");
		
		param.put("SEARCH_RANGE", searchRange);
		
		/*페이징*/
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 10;	// 페이지당 목록 수
		int listMaxUnit = 5;	// 최대 페이지 수 
		int listUnitStep = 10;	// 페이지당 목록 수 증가값
		
		int pageUnit = 10;
		int pageSize = 10;	
		
		
		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(StringUtil.getInt(rawJsonObj.get("PAGE").toString()));
		paginationInfo.setRecordCountPerPage(pageUnit );	//한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(pageSize); 						//페이징 리스트의 사이즈
		
		//목록수
		int totalCount = 0;
		totalCount = studPlanAdmService.getAffairCommitteeJudgListCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
		
		if(totalCount > 0) {
			param.put("firstIndex", paginationInfo.getFirstRecordIndex());
			param.put("lastIndex", paginationInfo.getLastRecordIndex());
			param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
			
			//리스트
			list = studPlanAdmService.getAffairCommitteeJudgList(param);
		}
		
		
		
		
		model.addAttribute("PAGINATION_INFO", paginationInfo);					// 페이징정보
		model.addAttribute("LIST", list);
		model.addAttribute("RESULT",  "DONE");
		
		return mav;
	}		
	
	/* 심의 수정화면 */
	@RequestMapping(value = "/affairCommitteeJudgModify.do")
	public String affairCommitteeJudgModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		
		return getViewPath("/affairCommitteeJudgModify");
	}
	
	/* 변경 - 심의 수정화면 */
	@RequestMapping(value = "/affairCommitteeChgJudgModify.do")
	public String affairCommitteeChgJudgModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		
		return getViewPath("/affairCommitteeJudgModify");
	}
/*----------학생설계전공 교무회 및 평의원회 끝----------*/
	
	
/*----------학생설계전공 최종수정 시작----------*/
	
	/* 최종수정 가능 목록 화면*/
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/lastSupplementList.do")
	public String lastModifyList(@ModuleAttr ModuleAttrVO attrVO, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws Exception {
		
		List<Object> myList = null;	
		List<Object> mySbjtList = null;	
		
		
		
		model.addAttribute("MY_LIST",myList);
		model.addAttribute("MY_SBJT_LIST",mySbjtList);
		
		
		return getViewPath("/lastSupplementList");
	}	
	
	
	/* 최종수정할 학생설계전공 조회 (ajax)*/
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/getLastSupplementList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getlastModifyList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		List<Object> list = null;
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		String searchRange = rawJsonObj.getString("SEARCH_RANGE");
		
		param.put("SEARCH_RANGE", searchRange);
		
		/*페이징*/
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 10;	// 페이지당 목록 수
		int listMaxUnit = 5;	// 최대 페이지 수 
		int listUnitStep = 10;	// 페이지당 목록 수 증가값
		
		int pageUnit = 10;
		int pageSize = 10;	
		
		
		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(StringUtil.getInt(rawJsonObj.get("PAGE").toString()));
		paginationInfo.setRecordCountPerPage(pageUnit );	//한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(pageSize); 						//페이징 리스트의 사이즈
		
		//목록수
		int totalCount = 0;
		totalCount = studPlanAdmService.getLastSupplementListCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
		
		if(totalCount > 0) {
			param.put("firstIndex", paginationInfo.getFirstRecordIndex());
			param.put("lastIndex", paginationInfo.getLastRecordIndex());
			param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
			
			//리스트
			list = studPlanAdmService.getLastSupplementList(param);
		}
		
		
		
		
		model.addAttribute("PAGINATION_INFO", paginationInfo);					// 페이징정보
		model.addAttribute("LIST", list);
		model.addAttribute("RESULT",  "DONE");
		
		return mav;
	}		
	
	/* 최종수정 화면 */
	@RequestMapping(value = "/lastSupplementModify.do")
	public String lastModifyComplete(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		
		return getViewPath("/lastSupplementModify");
	}
	
	/**
	 * 저장
	 */
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/lastSupplementSave.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView lastSupplementSave(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		

		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//기본정보 insert
			int aplyInfmtResult = studPlanAdmService.lastSupplementSave(rawJsonObj, request);			

			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 저장되었습니다.");
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "저장에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}
	
	/**
	 * 수정 마감
	 */
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/lastSupplementComplete.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView lastSupplementComplete(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		

		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//기본정보 insert
			int aplyInfmtResult = studPlanAdmService.lastSupplementComplete(rawJsonObj, request);
			
			int sendStudPlanToAHS010TB = studPlanAdmService.sendStudPlanToAHS010TB(rawJsonObj);
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 수정마감되었습니다. 해당 학생설계전공은 종합정보시스템으로 전송되고 이후 전공/교육과정에 등록됩니다.");
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "수정마감에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}
	

/*----------학생설계전공 최종수정 끝----------*/	
	


	


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

		if (useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}

		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, null, null, pageName, idxName, listName, viewName, null, null, null, null, inputName, inputProcName, deleteProcName, deleteListName, imageName, movieName, downloadName);

		// 추가경로
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
		
		
		
		String searchProfPopup = "searchProf.json";
		String sbjtList = "sbjtList.json";
		String studPlanSbjtList = "studPlanSbjtList.json";
		String modifySbjtList = "modifySbjtList.json";
		String saveSbjtPlan = "saveStudPlan.json";
		String updateSbjtPlan = "updateStudPlan.json";
		String getPlanIdx = "getPlanIdx.json";
		String completePlan = "completePlan.do";
		String completionSbjtPlan = "completionStudPlan.json";
		String myList = "myList.do";
		String studPlanView = "studPlanView.do";
		String myStudPlanView = "myStudPlanView.do";
		String studPlanBookmark = "studPlanBookmark.json";
		String status = "status.json";
		String modifyStudPlan = "modifyStudPlan.do";
		String planSampleList = "planSampleList.do";
		String planStatus = "planStatus.json";
		
		String sampleList = "/RBISADM/menuContents/web/studPlan/planList.do?mId=135";
		String sharelist = "/RBISADM/menuContents/web/studPlan/planList.do?mId=137";
		String transferList = "/RBISADM/menuContents/web/studPlan/planList.do?mId=138";
		
		
		request.setAttribute("URL_SAMPLE_LIST", sampleList);
		request.setAttribute("URL_SHARE_LIST", sharelist);
		request.setAttribute("URL_TRANSFER_LIST", transferList);
		
		request.setAttribute("URL_PLANSTATUS", planStatus+= baseQueryString);
		request.setAttribute("URL_PLANSAMPLELIST", planSampleList+= baseQueryString);
		request.setAttribute("URL_MODIFYSTUDPLAN", modifyStudPlan+= baseQueryString);
		request.setAttribute("URL_STATUS", status+= baseQueryString);
		request.setAttribute("URL_STUDPLANBOOKMARK", studPlanBookmark+= baseQueryString);
		request.setAttribute("URL_MYSTUDPLANVIEW", myStudPlanView+= baseQueryString);
		request.setAttribute("URL_STUDPLANVIEW", studPlanView+= baseQueryString);
		request.setAttribute("URL_COMPLETEPLAN", completePlan+= baseQueryString);
		request.setAttribute("URL_GETPLANIDX", getPlanIdx+= baseQueryString);
		request.setAttribute("URL_SEARCHPROF_POPUP", searchProfPopup+= baseQueryString);
		request.setAttribute("URL_SBJTLIST", sbjtList += baseQueryString);
		request.setAttribute("URL_STUDPLANSBJTLIST", studPlanSbjtList += baseQueryString);
		request.setAttribute("URL_MODIFYSBJTLIST", modifySbjtList += baseQueryString);
		request.setAttribute("URL_SAVESBJTPLAN", saveSbjtPlan += baseQueryString);
		request.setAttribute("URL_COMPLETIONSBJTPLAN", completionSbjtPlan += baseQueryString);
		request.setAttribute("URL_UPDATESBJTPLAN", updateSbjtPlan += baseQueryString);
		request.setAttribute("URL_MYLIST", myList += baseQueryString);
//		request.setAttribute("URL_TERMHPLIST", termHPInfoList += baseQueryString);
		
		request.setAttribute("baseQueryString", baseQueryString);
		request.setAttribute("URL_HASHTAG", "hashtag.do" + baseQueryString);
		request.setAttribute("URL_HASHTAG_INSERT", "hashtagInsert.do" + baseQueryString);
		request.setAttribute("URL_HASHTAG_DELETE", "hashtagDelete.do" + baseQueryString);
		request.setAttribute("URL_JOB_OBJECTIVES", "jobObjectives.do" + baseQueryString);
		request.setAttribute("URL_JOB_OBJECTIVE_DATA", "jobObjectiveData.json" + baseQueryString);
		request.setAttribute("URL_JOB_OBJECTIVE_INSERT", "jobObjectiveInsert.do" + baseQueryString);
		request.setAttribute("URL_JOB_OBJECTIVE_DELETE", "jobObjectiveDelete.do" + baseQueryString);
		
		request.setAttribute("URL_SEARCHUNIVLIST", "searchUnivList.json" + baseQueryString);
		request.setAttribute("URL_SEARCHSUBJECTLIST", "searchSubjectList.json" + baseQueryString);
	}
	
	



}