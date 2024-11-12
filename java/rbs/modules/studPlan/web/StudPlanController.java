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
import rbs.modules.studPlan.service.StudPlanService;
import rbs.usr.main.service.MainService;


/**
 * 학생설계융합전공 Controller
 * @author	이동근
 */
@Controller
@ModuleMapping(moduleId="studPlan")
@RequestMapping("/{siteId}/studPlan")
public class StudPlanController extends ModuleController {

	private static final Logger logger = LoggerFactory.getLogger(StudPlanController.class);
	
	
	@Resource(name = "codeOptnServiceOra")
	protected CodeOptnServiceOra codeOptnServiceOra;
	
	@Resource(name = "sbjtService")
	private SbjtService sbjtService;
	
	@Resource(name = "studPlanService")
	private StudPlanService studPlanService;

	
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
	 * 학과-전공 목록
	 * @param univ
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getProfDeptList.do", headers="Ajax")
	public ModelAndView getProfDeptList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		// 모듈정보
		List<Object> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		list = studPlanService.getProfDepartList(param);
		
		model.addAttribute("DEPART_LIST", list);
		
		return mav;
	}
	
	/**
	 * 교육과정 등록하기 - [전공교과목] 목록
	 * @param depart
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getSbjtList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getSbjtList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String selMjRange = rawJsonObj.get("selMjRange").toString();	/*대학/학부(과)/전공*/
		String keyWord = rawJsonObj.get("keyWord").toString();			/*검색 키워드*/
		String chkSbFg = rawJsonObj.getString("chkSbFg");				/*이수구분(전공: UE010098, 전공필수: UE010021, 전공선택: UE010022, 전공기초: UE010024)*/
		List<String> chkSbFgList = new ArrayList<String>();
		if(!chkSbFg.equals("")) {			
			chkSbFgList = Arrays.asList(chkSbFg.split(","));			//이수구분 listify
		}
		
		// 모듈정보
		List<Object> sbjtList = null;		
		
		param.put("SEL_MJ_RANGE", selMjRange);
		param.put("CHK_SB_FG_LIST", chkSbFgList);
		param.put("KEYWORD", keyWord);
		
		sbjtList = studPlanService.getSbjtList(param);
		
		model.addAttribute("SBJT_LIST", sbjtList);
		
		return mav;
	}
	
	/**
	 * 유사 학생설계전공 보기
	 * @param depart
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getSimilityMj.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getSimilityMj(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
					
		DataMap info = null;
		info = studPlanService.getSimilityMj(rawJsonObj);
		
		model.addAttribute("INFO", info);
		
		return mav;
	}
	
	
	/**
	 * 상태 변경
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/updateStatus.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView updateStatus(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		

		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd = rawJsonObj.getString("SDM_CD");
		int revsnNo = StringUtil.getInt(rawJsonObj.getString("REVSN_NO"));	
		String status = rawJsonObj.getString("STATUS");	
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//상태 update
			studPlanService.updateStatus(sdmCd, revsnNo, status, request);	
			
			//찜 해제(모든 찜에서 해당 sdm_cd에 해당하는 로우를 삭제한다.)
			studPlanService.deleteBookmarkBySdmCd(sdmCd);
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 처리되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "처리에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}	
	
	
	public String getMemberNo(HttpServletRequest request) {
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberNo = loginVO.getMemberId();
		return memberNo;
	}
	
	public String getMemberName(HttpServletRequest request) {
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String studentName = loginVO.getMemberName();
		return studentName;
	}
	
	/**
	 * 알림 가져오기
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getNoti.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getNoti(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		List<Object> noti = null;
		ModelAndView mav = new ModelAndView("jsonView");		
		
		if(getMemberNo(request) != null) {
			String memberNo = getMemberNo(request);								
			noti = studPlanService.getNotiMsg(memberNo);			
		}
		
		model.addAttribute("NOTI", noti);
		
		return mav;
	}
	
	/**
	 * 알람 읽음처리
	 */
	@RequestMapping(value="/notiMsgCnf.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView notiMsgCnf(HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		System.out.println(rawBody);
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		
		Map<String,Object> param = new HashMap<String, Object>();
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();	
		String regiIp = request.getRemoteAddr();
		String notiMsgNo = rawJsonObj.get("NOTI_MSG_NO").toString();
		
		param.put("MEMBER_ID", memberId);
		param.put("REGI_IP", regiIp);
		param.put("NOTI_MSG_NO", notiMsgNo);
		
		int result = 0;
		result = studPlanService.notiMsgCnf(param);
				

		mav.addObject("result", result);
		
		return mav;
	}
	
	/**
	 * 알람 삭제처리
	 */
	@RequestMapping(value="/notiMsgDel.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView notiMsgDel(HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		
		Map<String,Object> param = new HashMap<String, Object>();
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();	
		String regiIp = request.getRemoteAddr();
		String notiMsgNo = rawJsonObj.get("NOTI_MSG_NO").toString();
		
		param.put("MEMBER_ID", memberId);
		param.put("REGI_IP", regiIp);
		param.put("NOTI_MSG_NO", notiMsgNo);
		
		int result = 0;
		result = studPlanService.notiMsgDel(param);
		
		
		mav.addObject("result", result);
		
		return mav;
	}	
		
	
	private int insertNotiMsg(JSONObject rawJsonObj, HttpServletRequest request, String status) throws Exception {
	    List<String> toList = new ArrayList<>();
	    List<String> toNmList = new ArrayList<>();
		
		JSONObject json = new JSONObject();		
		json.put("fg", status);
		
		JSONArray toArr = new JSONArray();
		JSONObject toObject = new JSONObject();

		
		if(status.equals("20")) {			
			toList = Arrays.asList(rawJsonObj.getString("CNSLT_PROF").split(","));						
			toNmList = Arrays.asList(rawJsonObj.getString("CNSLT_PROF_NM").split(","));						
		}else if(status.equals("30")){
			toList.add(0, rawJsonObj.getString("GUID_PROF_STAFF_NO"));						
			toNmList.add(0, rawJsonObj.getString("GUID_PROF_STAFF_NM"));						
		}else if(status.equals("20-2") || status.equals("20-3") || status.equals("33")|| status.equals("40")) {
			toList.add(0, rawJsonObj.getString("APLY_STUDENT_NO"));						
			toNmList.add(0, rawJsonObj.getString("APLY_STUDENT_NM"));	
		}
		
		
		for (int i = 0; i < toList.size(); i++) {
			// 전송 대상()
			toObject.put("cd", toList.get(i)); 
			toObject.put("nm", toNmList.get(i));
			toArr.add(toObject);
		}
		
		json.put("to", toArr);
		json.put("SDM_CD", rawJsonObj.get("SDM_CD").toString());
		json.put("REVSN_NO", rawJsonObj.get("REVSN_NO").toString());
		json.put("tit", rawJsonObj.get("SDM_KOR_NM"));
		
		// 내용 (학생명, 학생수, 학생설계전공명)
		JSONObject cont = new JSONObject();
		cont.put("0", getMemberName(request));	// 학생명
		cont.put("1", rawJsonObj.get("SDM_KOR_NM"));	// 학생설계전공명
		
		json.put("cont", cont);
		json.put("ip", request.getRemoteAddr()); // 보내는 사람 IP
		json.put("memberId", getMemberNo(request)); // 보내는 사람 ID
		
		List<Map<String,Object>> mailData = mailDataChk(json);
		
		int result = studPlanService.insertNotiMsg(mailData);
		return result;
	}	
	
	private List<Map<String, Object>> mailDataChk(JSONObject json) throws Exception {
				/* 데이터 형태 
				 
				fg : 저장 파라미터 
				to : {[ email : '', cd : '' , nm : ''],....},
				tit  : 학생설계전공명
				cont : {이*, 학생설계전공명 } 
			
			
			파라미터 목록
			상담접수(학생용)	(20)
			상담접수(교수용)	(20-1)
			상담완료			(20-2)
			대면 요청			(20-3)
			신청서 접수 알림		(30)
			주관학과승인자료 반영	(mailFg_5)
			심사위원 선정		(mailFg_6)
			심사완료			(mailFg_7)
			본부승인자료 반영		(mailFg_8)
			반려자료 반영		(mailFg_9)
		 */
		String fg = json.get("fg").toString();
		String title = "";
		JSONObject contData = (JSONObject) json.get("cont"); 
		String contents = "";
		
		Map<String, String> to = new HashMap<>();
		
		JSONArray toArr = new JSONArray();
		toArr = (JSONArray) json.get("to");
				
		if(fg.equals("20")) {
			// 상담신청(학생)
			title = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.cnslt.accept.title"), json.get("tit").toString());
			contents = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.cnslt.accept.contents"), contData.get("0"),contData.get("1"));
		}else if(fg.equals("20-1")) {
			// 상담접수(교수)
			title = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.cnslt.accept.prof.title"), json.get("tit").toString());
			contents = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.cnslt.accept.prof.contents"), contData.get("0"),contData.get("1"));
		}else if(fg.equals("20-2")) {
			// 상담완료
			title = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.cnslt.success.title"), json.get("tit").toString());
			contents = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.cnslt.success.contents"), contData.get("0"),contData.get("1"));
		}else if(fg.equals("20-3")) {
			// 대면 요청
			title = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.cnslt.meeting.title"), json.get("tit").toString());
			
			if (contData.get("2")==null || contData.get("3")==null || contData.get("4")==null) {
				contents = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.cnslt.meeting.contents.empty"), contData.get("0"),contData.get("1"));
			}
			
			contents = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.cnslt.meeting.contents"), contData.get("0"),contData.get("1"),contData.get("3"),contData.get("4"));
		}else if(fg.equals("30")) {
			// 신청서 접수 알림	
			title = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.apldoc.accept.title"), json.get("tit").toString());
			contents = MessageFormat.format(RbsProperties.getProperty("Globals.mail.sdm.apldoc.accept.contents"), contData.get("0"));		
		}
		//이 밑으로는 아직 개발 안됨(전체적인 메세지 폼 요청 후 다시 만들어야함)
		
		
		List<Map<String, Object>> param = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < toArr.size(); i++) {
			JSONObject toObj  = new JSONObject();
			toObj = (JSONObject) toArr.get(i);
			
			Map<String, Object> dataArr = new HashMap<String, Object>();
			dataArr.put("SDM_CD", json.getString("SDM_CD"));
			dataArr.put("REVSN_NO", json.getString("REVSN_NO"));
			dataArr.put("NO", toObj.get("cd"));
			dataArr.put("TTL", title);
			dataArr.put("MSG", contents);
			dataArr.put("IP", json.get("ip"));
			LocalDate now = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			String frDt = now.format(formatter);
			now = now.plusMonths(1);
			String toDt = now.format(formatter);
			dataArr.put("FR_DT", frDt);
			dataArr.put("TO_DT", toDt);
			
			
			param.add(dataArr);
		}
		
		// 푸시메일 보내기 (해양대 SMTP서버 정보 알때까지 일단 주석처리)
//		MailUtil.sendMail(
//				RbsProperties.getProperty("Globals.mail.fromMail")
//				, RbsProperties.getProperty("Globals.mail.fromName")
//				, to
//				, title
//				, contents );
//		
		return param;
	}

/*----------학생설계전공 공통 끝----------*/	
	

/*----------학생설계전공 학생 시작----------*/
	/**
	 * 교육과정 등록하기 - [나의찜목록] 목록
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getMyBookmarkSbjtList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getMyBookmarkSbjtList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String selMjRange = rawJsonObj.get("selMjRange").toString();	/*대학/학부(과)/전공*/
		String keyWord = rawJsonObj.get("keyWord").toString();			/*검색 키워드*/
		String chkSbFg = rawJsonObj.getString("chkSbFg");				/*이수구분(전공: UE010098, 전공필수: UE010021, 전공선택: UE010022, 전공기초: UE010024)*/
		JSONArray bookmarkListArray = rawJsonObj.getJSONArray("bookmarkList");	/*개인이 찜한 목록*/    
		
		List<String> docIdList = new ArrayList<String>();
		for (int i = 0; i < bookmarkListArray.size(); i++) {					//찜목록의 DOC_ID listify
			JSONObject bookmark = bookmarkListArray.getJSONObject(i);
			docIdList.add(bookmark.getString("docId"));
		}        
		
		List<String> chkSbFgList = new ArrayList<String>();
		if(!chkSbFg.equals("")) {			
			chkSbFgList = Arrays.asList(chkSbFg.split(","));			//이수구분 listify
		}
		
		// 모듈정보
		List<Object> sbjtList = null;		
		
		param.put("SEL_MJ_RANGE", selMjRange);
		param.put("KEYWORD", keyWord);
		param.put("DOC_ID_LIST", docIdList);
		param.put("CHK_SB_FG_LIST", chkSbFgList);
		
		sbjtList = studPlanService.getSbjtList(param);
		
		model.addAttribute("SBJT_LIST", sbjtList);
		
		return mav;
	}
	

	/**
	 * 학생설계전공 목록조회
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletResponse response, HttpServletRequest request, ModelMap model) throws Exception {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String viewPath = "";
		
		switch(loginVO.getUsertypeIdx()) {
		case 5 : 
			model = stdList(model, loginVO);
			viewPath = "/stdList";
			break;
		case 45:
			model = profList(model, loginVO);
			viewPath = "/profList";
			break;
		//08.12 조교,직원은 교수와 동일 화면
		case 46:
			model = staffList(model,loginVO);
			viewPath = "/profList";
			break;
		case 47:
			model = staffList(model, loginVO);
			viewPath = "/profList";
			break;
		case -1: 
			break;					
		}
		
		return getViewPath(viewPath);

	}	
	
	private ModelMap profList(ModelMap model, LoginVO loginVO) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("MEMBER_ID", loginVO.getMemberId());
		param.put("MEMBER_DEPT_CD", loginVO.getDeptCd());
		
		/* 탭별 카운트 */
		model.addAttribute("ALL_TAB_CNT", (studPlanService.getAllTabCnt(param).size() < 1) ? null : studPlanService.getAllTabCnt(param));
		
		/* 승인요청 */
		model.addAttribute("RA", (studPlanService.getRAList(param).size() < 1) ? null : studPlanService.getRAList(param));
		
		return model;
	}
	
	private ModelMap staffList(ModelMap model, LoginVO loginVO) throws Exception {
		
		return model;
	}

	private ModelMap stdList(ModelMap model, LoginVO loginVO) throws Exception {
		
		//내 학생설계전공 리스트
		model.addAttribute("MY_LIST",(studPlanService.getMyList(loginVO.getMemberId()).size() < 1) ? null : studPlanService.getMyList(loginVO.getMemberId()));
				
		//내 학생설계전공 교과목별 전공 리스트
		model.addAttribute("MY_SBJT_LIST",(studPlanService.getMySbjtList(loginVO.getMemberId()).size() < 1) ? null : studPlanService.getMySbjtList(loginVO.getMemberId()));		

		
		return model;
	}

	/**
	 * 승인된 전체 학생설계전공 목록조회(ajax)
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getStudPlanList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getStudPlanList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		Map<String, Object> param = new HashMap<String, Object>();						
		List<Object> studPlanList = null;	
		List<Object> studPlanSbjtList = null;	
		
		String majorCd = rawJsonObj.get("majorCd") == null ? "" : rawJsonObj.get("majorCd").toString();			/*전공*/
		String keyWord = rawJsonObj.get("year") == null ? "" : rawJsonObj.get("year").toString();			/*검색 키워드*/
		
		param.put("MAJOR_CD", majorCd);
		param.put("YEAR", keyWord);
		
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
		paginationInfo.setCurrentPageNo(StringUtil.getInt(rawJsonObj.get("page").toString()));
		paginationInfo.setRecordCountPerPage(pageUnit );	//한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(pageSize); 						//페이징 리스트의 사이즈
		
		//목록수
		int totalCount = 0;
    	totalCount = studPlanService.getStudPlanListCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		//승인된 전체 학생설계전공 리스트
    		studPlanList = studPlanService.getStudPlanList(param);
    	}

		
		

		//승인된 전체 학생설계전공 교과목별 전공 리스트
		studPlanSbjtList = studPlanService.getStudPlanSbjtList(param);
		
		model.addAttribute("PAGINATION_INFO", paginationInfo);					// 페이징정보
		model.addAttribute("STUDPLAN_LIST",studPlanList);
		model.addAttribute("STUDPLAN_SBJT_LIST",studPlanSbjtList);
		
		return mav;
	}
	
	/**
	 * 신규등록하기
	 */
	@ModuleAuth(name={"WRT"})
	@RequestMapping(value = "/stdInput.do")
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		
		/*다음 순서의 학생설계전공 번호*/
		String sdmCd = studPlanService.getNextSdmCd(getMemberNo(request));
		
		/* 기본정보 */
		param.put("STUDENT_NO", memberId);
		DataMap myInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("MYINFO",myInfo);
		model.addAttribute("SDM_CD",sdmCd);
		
    	return getViewPath("/stdInput");
	}	
	
	/**
	 * 지도교수 검색 목록
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/profList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView profList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String deptCd = rawJsonObj.get("deptCd").toString();			/*소속 학과*/
		String keyWord = rawJsonObj.get("keyWord").toString();			/*검색 키워드*/
		
	
		
		// 모듈정보
		List<Object> profList = null;		
		
		param.put("DEPT_CD", deptCd);
		param.put("KEYWORD", keyWord);
		
		profList = studPlanService.getProfList(param);
		
		model.addAttribute("PROF_LIST", profList);
		
		return mav;
	}
	
	/**
	 * 신규등록하기 -  임시저장
	 */
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/newTempSave.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView newTempSave(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		

		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd =  rawJsonObj.getString("SDM_CD");
		int revsnNo = 1;	//신규이므로 REVSN_CD는 1로 고정
		String status = "10";	//임시저장
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//기본정보 insert
			int aplyInfmtResult = studPlanService.insertStudInfmt(sdmCd, revsnNo, rawJsonObj, request, status);			
			if(aplyInfmtResult > 0) {
				//교과목 Insert
				studPlanService.insertStudSbjt(sdmCd, revsnNo, rawJsonObj, request);
			}
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 임시저장되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "임시저장에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}
	
	/**
	 * 신규등록하기 -  상담신청
	 */
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/newReqCnslt.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView newReqCnslt(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd =  rawJsonObj.get("SDM_CD").toString();
		int revsnNo = 1;	//신규이므로 REVSN_CD는 1로 고정
		String status = "20";	//상담신청
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {			
			
			//기본정보 insert
			int aplyInfmtResult = studPlanService.insertStudInfmt(sdmCd, revsnNo, rawJsonObj, request, status);			
			if(aplyInfmtResult > 0) {
				//교과목 Insert
				studPlanService.insertStudSbjt(sdmCd, revsnNo, rawJsonObj, request);
				//상담교수 insert
				studPlanService.insertCnslt(sdmCd, revsnNo, rawJsonObj, request);
				
				//알림 insert
				insertNotiMsg(rawJsonObj, request, status);
			}
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 상담신청되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "상담신청에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}

	/**
	 * 신규등록하기 -  신청서제출
	 */
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/newSubmit.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView newSubmit(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd =  rawJsonObj.getString("SDM_CD");
		int revsnNo = 1;	//신규이므로 REVSN_CD는 1로 고정
		String status = "30";	//신청서제출
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {			
			
			//기본정보 insert
			int aplyInfmtResult = studPlanService.insertStudInfmt(sdmCd, revsnNo, rawJsonObj, request, status);			
			if(aplyInfmtResult > 0) {
				//교과목 Insert
				studPlanService.insertStudSbjt(sdmCd, revsnNo, rawJsonObj, request);	
				
				//알림 insert
				insertNotiMsg(rawJsonObj, request, status);
			}
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 제출되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "제출에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}
	
	/* 수정 */
	@RequestMapping(value = "/stdModify.do")
	public String modifyStudPlan(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		List<Object> cnsltList = null;		
		
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		//상담 정보
		cnsltList = studPlanService.getCnslt(sdmCd, revsnNo);
		/* 기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", getMemberNo(request));
		DataMap myInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("MYINFO",myInfo);
		
		model.addAttribute("INFO", info);
		model.addAttribute("CNSLTLIST", cnsltList);
		
    	return getViewPath("/stdModify");
	}	

	
	/**
	 * 수정 - 등록한 교과목 불러오기 (ajax)
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getMySbjtList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getMySbjtList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		
		// 모듈정보
		List<Object> sbjtList = null;
		
		String sdmCd = rawJsonObj.get("sdmCd").toString();
		String revsnNo = rawJsonObj.get("revsnNo").toString();
		
		//교과목 정보
		sbjtList = studPlanService.getStudSbjt(sdmCd, revsnNo);
		
		model.addAttribute("SBJTLIST", sbjtList);
		
		return mav;
	}
	
	
	/**
	 * 수정 -  임시저장
	 */
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/modifyTempSave.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView modifyTempSave(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		

		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd = rawJsonObj.getString("SDM_CD");
		int revsnNo = StringUtil.getInt(rawJsonObj.getString("REVSN_NO"));	
		String status = "10";	//임시저장
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//기본정보 insert
			int aplyInfmtResult = studPlanService.updateStudInfmt(sdmCd, revsnNo, rawJsonObj, request, status);			
			if(aplyInfmtResult > 0) {
				//교과목 delete and insert
				studPlanService.deleteStudSbjt(sdmCd, revsnNo, request);
				studPlanService.insertStudSbjt(sdmCd, revsnNo, rawJsonObj, request);
			}
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 임시저장되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "임시저장에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}
	
	/**
	 * 수정 -  상담신청
	 */
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/modifyReqCnslt.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView modifyReqCnslt(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd = rawJsonObj.getString("SDM_CD");
		int revsnNo = StringUtil.getInt(rawJsonObj.getString("REVSN_NO"));	
		String status = "20";	//상담신청
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//기본정보 insert
			int aplyInfmtResult = studPlanService.updateStudInfmt(sdmCd, revsnNo, rawJsonObj, request, status);			
			if(aplyInfmtResult > 0) {
				//교과목 delete and insert
				studPlanService.deleteStudSbjt(sdmCd, revsnNo, request);
				studPlanService.insertStudSbjt(sdmCd, revsnNo, rawJsonObj, request);
				//상담교수 delete and insert
				studPlanService.deleteCnslt(sdmCd, revsnNo, rawJsonObj, request);		
				studPlanService.insertCnslt(sdmCd, revsnNo, rawJsonObj, request);	
				
				//알림 insert
				insertNotiMsg(rawJsonObj, request, status);
			}
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 상담신청되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "상담신청에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}
	/**
	 * 수정 -  신청서제출
	 */
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/modifySubmit.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView modifySubmit(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd = rawJsonObj.getString("SDM_CD");
		int revsnNo = StringUtil.getInt(rawJsonObj.getString("REVSN_NO"));	
		String status = "30";	//제출
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//기본정보 insert
			int aplyInfmtResult = studPlanService.updateStudInfmt(sdmCd, revsnNo, rawJsonObj, request, status);			
			if(aplyInfmtResult > 0) {
				//교과목 delete and insert
				studPlanService.deleteStudSbjt(sdmCd, revsnNo, request);
				studPlanService.insertStudSbjt(sdmCd, revsnNo, rawJsonObj, request);	
				
				//알림 insert
				insertNotiMsg(rawJsonObj, request, status);
			}
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 제출되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "제출에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}	
	
	
	/**
	 * 조회 - 본인이 작성한 학생설계전공
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/stdMyView.do", method = RequestMethod.POST)
	public String stdMyView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		DataMap info = null;	
		List<Object> cnsltList = null;		
		
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		//상담 정보
		cnsltList = studPlanService.getCnslt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", getMemberNo(request));
		DataMap myInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("MYINFO",myInfo);
		
		model.addAttribute("INFO", info);
		model.addAttribute("CNSLTLIST", cnsltList);
    	
    	return getViewPath("/stdMyView");
	}	
	
	/**
	 * 조회 - 승인된 학생설계전공
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/studView.do", method = RequestMethod.POST)
	public String studView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		
		DataMap info = null;	
		List<Object> studPlanSbjtList = null;	
		List<Object> cnsltList = null;	
		
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap myInfo = mainService.getMyInfomation(param);
		
		//학생설계전공 교과목별 전공 리스트
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		studPlanSbjtList = studPlanService.getStudPlanSbjtList(param);
		
		//상담 정보
		cnsltList = studPlanService.getCnslt(sdmCd, revsnNo);
		
		model.addAttribute("MYINFO",myInfo);		
		model.addAttribute("INFO", info);
		model.addAttribute("STUDPLAN_SBJT_LIST", studPlanSbjtList);
		model.addAttribute("CNSLTLIST", cnsltList);
		
		return getViewPath("/studView");
	}	

	/**
	 * 변경
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/stdChangeInput.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView stdChangeInput(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd = rawJsonObj.getString("SDM_CD");
		int revsnNo = StringUtil.getInt(rawJsonObj.getString("REVSN_NO"));	
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("SDM_CD", sdmCd);
		param.put("REVSN_NO", revsnNo);
		param.put("REGI_IP", request.getRemoteAddr());
		param.put("REGI_ID", getMemberNo(request));
		
		ModelAndView mav = new ModelAndView("jsonView");

		
		//변경용 프로시저 call(insert)
		studPlanService.changeStud(param);			
		
		
		model.addAttribute("RESULT",  param.get("o_RTN_MSG"));
		model.addAttribute("SDM_CD",  param.get("o_SDM_CD"));
		model.addAttribute("REVSN_NO",  param.get("o_REVSN_NO"));

		
		
		return mav;
	}
	
	/* 변경수정화면 */
	@RequestMapping(value = "/stdChgModify.do")
	public String stdChgModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		List<Object> cnsltList = null;		
		
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		/* 기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", getMemberNo(request));
		DataMap myInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("MYINFO",myInfo);
		
		model.addAttribute("INFO", info);
		
    	return getViewPath("/stdChgModify");
	}	
	
	/* 변경 - 복사된 교과목 불러오기 (ajax) */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/getMyChgSbjtList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getMyChgSbjtList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		
		// 모듈정보
		List<Object> chgSbjtList = null;
		List<Object> sbjtList = null;
		
		String sdmCd = rawJsonObj.get("sdmCd").toString();
		String revsnNo = rawJsonObj.get("revsnNo").toString();
		String beforeRevsnNo = rawJsonObj.get("beforeRevsnNo").toString();
		
		
		//변경 교과목 정보
		chgSbjtList = studPlanService.getStudChgSbjt(sdmCd, revsnNo);
		//변경전 기존 교과목 정보
		sbjtList = studPlanService.getStudSbjt(sdmCd, beforeRevsnNo);
		
		model.addAttribute("CHG_SBJTLIST", chgSbjtList);
		model.addAttribute("SBJTLIST", sbjtList);
		
		return mav;
	}
	
	/* 변경 -  임시저장*/
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/chgModifyTempSave.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView chgModifyTempSave(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		

		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd = rawJsonObj.getString("SDM_CD");
		int revsnNo = StringUtil.getInt(rawJsonObj.getString("REVSN_NO"));	
		String status = "10";	//임시저장
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//기본정보 update(교수정보,변경사유)
			int aplyInfmtResult = studPlanService.updateChgStudInfmt(sdmCd, revsnNo, rawJsonObj, request, status);			
			if(aplyInfmtResult > 0) {
				//변경교과목테이블 delete and insert
				studPlanService.deleteChgStudSbjt(sdmCd, revsnNo, request);
				studPlanService.insertChgStudSbjt(sdmCd, revsnNo, rawJsonObj, request);
			}
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 임시저장되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "임시저장에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}	
	
	/* 변경 -  상담신청*/
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/chgModifyReqCnslt.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView chgModifyReqCnslt(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd = rawJsonObj.getString("SDM_CD");
		int revsnNo = StringUtil.getInt(rawJsonObj.getString("REVSN_NO"));	
		String status = "20";	//상담신청
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//기본정보 update(교수정보,변경사유)
			int aplyInfmtResult = studPlanService.updateChgStudInfmt(sdmCd, revsnNo, rawJsonObj, request, status);			
			if(aplyInfmtResult > 0) {
				//변경교과목테이블 delete and insert
				studPlanService.deleteChgStudSbjt(sdmCd, revsnNo, request);
				studPlanService.insertChgStudSbjt(sdmCd, revsnNo, rawJsonObj, request);
				
				//교과목테이블 delete and 변경교과목 -> 교과목 테이블 insert
				studPlanService.deleteStudSbjt(sdmCd, revsnNo, request);
				studPlanService.insertChgDataToStudSbjt(sdmCd, revsnNo, rawJsonObj, request);
				
				//상담교수 delete and insert
				studPlanService.deleteCnslt(sdmCd, revsnNo, rawJsonObj, request);		
				studPlanService.insertCnslt(sdmCd, revsnNo, rawJsonObj, request);	
				
				//알림 insert
				insertNotiMsg(rawJsonObj, request, status);				
			}
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 상담신청되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "상담신청에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}	
	
	
	/* 변경 -  신청서제출*/
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/chgModifySubmit.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView chgModifySubmit(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String sdmCd = rawJsonObj.getString("SDM_CD");
		int revsnNo = StringUtil.getInt(rawJsonObj.getString("REVSN_NO"));	
		String status = "30";	//신청서제출
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//기본정보 update(교수정보,변경사유)
			int aplyInfmtResult = studPlanService.updateChgStudInfmt(sdmCd, revsnNo, rawJsonObj, request, status);			
			if(aplyInfmtResult > 0) {
				//변경교과목테이블 delete and insert
				studPlanService.deleteChgStudSbjt(sdmCd, revsnNo, request);
				studPlanService.insertChgStudSbjt(sdmCd, revsnNo, rawJsonObj, request);
				
				//교과목테이블 delete and 변경교과목 -> 교과목 테이블 insert
				studPlanService.deleteStudSbjt(sdmCd, revsnNo, request);
				studPlanService.insertChgDataToStudSbjt(sdmCd, revsnNo, rawJsonObj, request);
				
				//알림 insert
				insertNotiMsg(rawJsonObj, request, status);				
			}
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 제출되었습니다.");
			model.addAttribute("SDM_CD",  sdmCd);
			model.addAttribute("RESVN_NO",  revsnNo);
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "제출에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}	
	

	/* 변경 조회화면 */
	@RequestMapping(value = "/stdMyChgView.do")
	public String stdMyChgView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		List<Object> cnsltList = null;		
		
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		/* 기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		//상담 정보
		cnsltList = studPlanService.getCnslt(sdmCd, revsnNo);
		param.put("STUDENT_NO", getMemberNo(request));
		DataMap myInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("MYINFO",myInfo);
		model.addAttribute("CNSLTLIST",cnsltList);		
		model.addAttribute("INFO", info);
		
		return getViewPath("/stdMyChgView");
	}	
	
	
/*----------학생설계전공 학생 끝----------*/	
	

/*----------학생설계전공 교수 시작----------*/
	
	/* 메인 -  탭별 리스트(ajax)*/
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/getStudPlanTabList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getStudPlanTabList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		List<Object> tabList = null;
		
		ModelAndView mav = new ModelAndView("jsonView");
			
		tabList = studPlanService.getStudPlanTabList(rawJsonObj);
		
		model.addAttribute("TAB_LIST",  tabList);
		model.addAttribute("RESULT",  "DONE");
		
		return mav;
	}		
	
	/* 메인 -  전체 탭 카운트(ajax)*/
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/getStudPlanAllTabCnt.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getStudPlanAllTabCnt(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {				
		List<Object> allTabCnt = null;
		Map<String, Object> param = new HashMap<>();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		param.put("MEMBER_ID", loginVO.getMemberId());
		param.put("MEMBER_DEPT_CD", loginVO.getDeptCd());
		
		ModelAndView mav = new ModelAndView("jsonView");
		
		allTabCnt = studPlanService.getAllTabCnt(param);
		
		model.addAttribute("ALL_TAB_CNT",  allTabCnt);
		model.addAttribute("RESULT",  "DONE");
		
		return mav;
	}
	
	/* 승인요청 수정화면 */
	@RequestMapping(value = "/profApproveModify.do")
	public String profApproveModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
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
		
    	return getViewPath("/profApproveModify");
	}
	
	/* 변경 - 승인요청 수정화면 */
	@RequestMapping(value = "/profApproveChgModify.do")
	public String profApproveChgModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
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
		
    	return getViewPath("/profApproveChgModify");
	}	
	
	
	/**
	 * 승인
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/profApprove.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView profApprove(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		

		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String status = "40";	
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//상태 update
			studPlanService.profApprove(rawJsonObj, status, request);	

			//알림 insert
//			insertNotiMsg(rawJsonObj, request, status);		
			
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
	@RequestMapping(value = "/profReject.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView profReject(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		String status = "33";	
		
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			
			//상태 update
			studPlanService.profReject(rawJsonObj, status, request);	
			
			//알림 insert
//			insertNotiMsg(rawJsonObj, request, status);		
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 반려되었습니다.");
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "반려에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}		
	
	/* 승인요청 상세보기 */
	@RequestMapping(value = "/profApproveView.do")
	public String profApproveView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
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
		
    	return getViewPath("/profApproveView");
	}
	
	/* 변경 - 승인요청 상세보기 */
	@RequestMapping(value = "/profApproveChgView.do")
	public String profApproveChgView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
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
		
		return getViewPath("/profApproveChgView");
	}	
	
	/* 상담 수정화면 */
	@RequestMapping(value = "/profCnsltModify.do")
	public String profCnsltModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		List<Object> cnsltInfo = null;		
		
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		//상담 정보
		cnsltInfo = studPlanService.getProfCnslt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		model.addAttribute("CNSLT_INFO", cnsltInfo);
		
		return getViewPath("/profCnsltModify");
	}
	
	/* 변경 - 상담 수정화면 */
	@RequestMapping(value = "/profCnsltChgModify.do")
	public String profCnsltChgModify(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		List<Object> cnsltInfo = null;		
		
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		//상담 정보
		cnsltInfo = studPlanService.getProfCnslt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		model.addAttribute("CNSLT_INFO", cnsltInfo);
		
		return getViewPath("/profCnsltChgModify");
	}
	
	/* 상담 상세보기화면 */
	@RequestMapping(value = "/profCnsltView.do")
	public String profCnsltView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		List<Object> cnsltInfo = null;		
		
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		//상담 정보
		cnsltInfo = studPlanService.getProfCnslt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		model.addAttribute("CNSLT_INFO", cnsltInfo);
		
		return getViewPath("/profCnsltView");
	}
	
	/* 변경 - 상담 상세보기화면 */
	@RequestMapping(value = "/profCnsltChgView.do")
	public String profCnsltChgView(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {		
		DataMap info = null;	
		List<Object> cnsltInfo = null;		
		
		String sdmCd = request.getParameter("SDM_CD");
		String revsnNo = request.getParameter("REVSN_NO");
		
		//기본 정보
		info = studPlanService.getStudInfmt(sdmCd, revsnNo);
		//상담 정보
		cnsltInfo = studPlanService.getProfCnslt(sdmCd, revsnNo);
		/* 학생기본정보 */
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", info.get("APLY_STUDENT_NO"));
		DataMap studentInfo = mainService.getMyInfomation(param);
		
		model.addAttribute("STUDENTINFO",studentInfo);
		
		model.addAttribute("INFO", info);
		model.addAttribute("CNSLT_INFO", cnsltInfo);
		
		return getViewPath("/profCnsltChgView");
	}
	
	/**
	 * 상담 action
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/profCnsltAction.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView profCnsltTempSave(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		

		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		ModelAndView mav = new ModelAndView("jsonView");
		String alertMsg = "";
		switch(rawJsonObj.get("ACTION").toString()) {
			case "TEMPSAVE": alertMsg = "임시저장"; break;
			case "COMPLETE": alertMsg = "상담완료"; break;
			case "MEET": alertMsg = "방문요청"; break;
		}
		
//		try {
			
			//상담정보 update
			studPlanService.updateProfCnslt(rawJsonObj, request);	
			
			//알림 insert
			switch(rawJsonObj.get("ACTION").toString()) {
				case "COMPLETE": insertNotiMsg(rawJsonObj, request, "20-2"); break;
				case "MEET": insertNotiMsg(rawJsonObj, request, "20-3"); break;
			}

			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 "+alertMsg+"되었습니다.");
//		}catch(Exception e) {
//			model.addAttribute("RESULT", e.getMessage());
//			model.addAttribute("RTN_MSG", "처리에 실패하였습니다. 관리자에게 문의해주세요.");
//		}
		
		
		return mav;
	}
	
	/**
	 * 상담 접수
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/profCnsltReceive.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView profCnsltReceive(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		ModelAndView mav = new ModelAndView("jsonView");
		
		try {
			
			//상담정보 insert
			studPlanService.insertProfCnslt(rawJsonObj, request);	
			
			//알림 insert
			insertNotiMsg(rawJsonObj, request, "20-1");
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 접수되었습니다.");
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "처리에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}
	
	/**
	 * 상담 접수취소
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/profCnsltCancel.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView profCnsltCancel(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {		
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		ModelAndView mav = new ModelAndView("jsonView");
		
		try {
			
			//상담정보 delete
			studPlanService.deleteProfCnslt(rawJsonObj, request);			
			
			model.addAttribute("RESULT",  "DONE" );
			model.addAttribute("RTN_MSG",  "정상적으로 취소되었습니다.");
		}catch(Exception e) {
			model.addAttribute("RESULT", e.getMessage());
			model.addAttribute("RTN_MSG", "처리에 실패하였습니다. 관리자에게 문의해주세요.");
		}
		
		
		return mav;
	}		
	
/*----------학생설계전공 교수 끝----------*/	


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