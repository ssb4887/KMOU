package rbs.modules.member.web;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nl.captcha.Captcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import rbs.egovframework.Defines;
import rbs.egovframework.LoginVO;
import rbs.egovframework.SukangLoginVO;
import rbs.egovframework.service.DataSecurityServiceImpl;
import rbs.egovframework.util.PrivAuthUtil;
import rbs.modules.member.service.MemberInfoService;
import rbs.modules.member.service.MemberLogService;
import rbs.modules.nonSbjt.service.NonSbjtService;

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
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.DataSecurityUtil;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

/**
 * 사용자모드 : 회원가입/내정보수정/아이디찾기/비밀번호찾기/회원탈퇴
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="member", useSDesign=true)
@RequestMapping("/{siteId}/member")
public class MemberJoinController extends MemberJoinComController{
	
	@Resource(name = "memberLogService")
	private MemberLogService memberLogService;
	
	@Resource(name = "MemberInfoService")
	private MemberInfoService memberInfoService;
	
	@Resource(name = "nonSbjtService")
	private NonSbjtService nonSbjtService;

	private Logger logger = LoggerFactory.getLogger("usrMember");
	/**
	 * 내정보수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/myInfo.do")
	public String myInfo(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String mbrIdx = null;
		String mbrId = null;
		String mbrName = null;
		if(loginVO != null){
			mbrIdx = loginVO.getMemberIdx();
			mbrId = loginVO.getMemberId();
			mbrName = loginVO.getMemberName();
		}
		
		if(StringUtil.isEmpty(mbrIdx)) {
			return RbsProperties.getProperty("Globals.error.401.login" + ajaxPName + ".path");
		}
		
		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.MEMBER_IDX", mbrIdx));
		param.put("searchList", searchList);
//		dt = memberService.getModify(param);
//		if(dt == null) {
//			// 해당글이 없는 경우
//			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
//			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
//		}

		// sns로그인 사용여부 
		int useSns = RbsProperties.getPropertyInt("Globals.sns.login.use");					// sns로그인 사용여부
		int useSnsRegi = RbsProperties.getPropertyInt("Globals.sns.login.register.use");	// sns등록 로그인에서 사용여부
		if(useSns == 1 && useSnsRegi != 1) {
			// sns로그인 사용하는 경우 - sns계정정보 얻기
		}
		String submitType = "myinfo";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		
		/**
		 * 마이페이지 학생 정보 ========================================================================================
		 */
		
		// 학번(=아이디) ; 복호화
		String memberId = (loginVO != null) ? loginVO.getMemberId() : null;
		if(StringUtil.isEmpty(memberId)) return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
//		memberId = new DataSecurityServiceImpl().getPrivDecrypt(memberId);
		
		param.put("STUDENT_NO", memberId); 														// @param : 20121429 (졸업), 20121428 (재학-화석), 20200004(재학-전과?), 20220102, 20231102 (재학-신입)
		
		// 기본정보
		DataMap myInfo = memberInfoService.getMyInfomation(param);						
		model.addAttribute("USER", myInfo);
		
		param.put("HAKJUK_ST", myInfo.get("HAKJUK_ST_CODE"));									// @param : 학적구분(재학생, 졸업생)
		param.put("DEPT_CD", myInfo.get("DEPT_CD"));											// @param : 학부(과)
		param.put("MAJOR_CD", myInfo.get("MAJOR_CD"));											// @param : 전공
				
		// 교육과정
		DataMap myCurriculum = memberInfoService.getMyCurriculum(param);					
		param.put("APY_YEAR", myCurriculum == null ? null : myCurriculum.getValue(0)); // @param : 교육과정 기준년도
		
		
		/* 졸업기준학점 */
		List<Object> userGoalCdtList = memberInfoService.getMyGoalCDT(param);
		model.addAttribute("USER_GOAL_CDT", (userGoalCdtList == null) ? null : userGoalCdtList);

		
		/* 학점정보 */
		DataMap userCdtList = memberInfoService.getMyCDT(param);
		model.addAttribute("USER_CDT", (userCdtList == null) ? null : userCdtList);	
		
		
		/* 성적정보 */
		List<Object> userGpaList = memberInfoService.getMyGPA(param);
		model.addAttribute("USER_GPA", (userGpaList == null) ? null : userGpaList);
		
		
		/* 학점상세정보 */
		List<Object> userCdtDtlList = memberInfoService.getMyCdtDetail(param);
		model.addAttribute("USER_CDT_DTL", (userCdtDtlList == null) ? null : userCdtDtlList); 		
		
		
		/* 누적성적 조회  */
		List<Object> userReqCdtList = memberInfoService.getMyReqCDT(param);
		model.addAttribute("USER_REQ_CDT", (userReqCdtList == null) ? null : userReqCdtList);
		
		
		/* 학기별성적 조회  */
		List<Object> userCumCdtList = memberInfoService.getMyCumCDT(param);
		model.addAttribute("USER_CUM_CDT", (userCumCdtList == null) ? null : userCumCdtList); 
		
		
		/* 졸업인증자격 조회  */
		List<Object> userGradReqList = memberInfoService.getMyGradReq(param);
		model.addAttribute("USER_GRAD_REQ", (userGradReqList == null) ? null : userGradReqList); 
		
		
		/* 과목별성적 조회  */
		List<Object> userSubjCdtList = memberInfoService.getMySubjectCDT(param);
		model.addAttribute("USER_SUBJECT_CDT", (userSubjCdtList == null) ? null : userSubjCdtList);
		
		/* 전공필수 이수 현황 */
		List<Object> userMajorReqList = memberInfoService.getMyMajorReq(param);
		model.addAttribute("USER_MAJOR_REQ", (userMajorReqList == null) ? null : userMajorReqList); 
		
		
		/* 교양교과목 이수 현황 */
		List<Object> userMinorReqList = null;
		int chkMinorReq = memberInfoService.getChkMinorReq(param);
		if(chkMinorReq > 0) {			
			userMinorReqList = memberInfoService.getMyMinorReq(param);
		}
		model.addAttribute("USER_MINOR_REQ", (userMinorReqList == null) ? null : userMinorReqList); 
		
		
		/* 학적변동내역  */
		List<Object> userRecordHistList = memberInfoService.getMyRecordHistory(param);
		model.addAttribute("USER_RECORD_HISTORY", (userRecordHistList == null) ? null : userRecordHistList);
		

		/* 해시태그  */
		List<Object> userHashtagList = memberInfoService.getMyHashtag(param);
		model.addAttribute("USER_HASHTAG", (userHashtagList == null) ? null : userHashtagList);
		
		
		/* 비교과 신청이력 */
		List<Object> userNonSbjtSigninList = nonSbjtService.getMyNonSbjtSigninHist(param);
		model.addAttribute("USER_NON_SBJT_SIGNIN", (userNonSbjtSigninList == null) ? null : userNonSbjtSigninList);
		
		
		model.addAttribute("TARGET", "#" + request.getParameter("target"));	// 탭 이동(element id값)		
		model.addAttribute("dt", dt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
		fn_setMyInfoPath(attrVO);
		fn_setCommonAddPath(attrVO, request);
		
		memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.view"), null, null, items, mbrIdx, mbrIdx, mbrId, mbrName);
    	
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	model.addAttribute("URL_HASHTAG_PROC", request.getAttribute("URL_HASHTAG_PROC"));
		return getViewPath("/myInfo");
	}
	
	@RequestMapping(value = "/hashtagProc.do", headers="Ajax")
	public ModelAndView hashtagProc(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		String hashtag = (!StringUtil.isEmpty(request.getParameter("HASHTAG"))) ? request.getParameter("HASHTAG") : ""; 
		String flagDel = (!StringUtil.isEmpty(request.getParameter("FLAG_DEL"))) ? request.getParameter("FLAG_DEL") : "";
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		if(loginVO != null){
			param.put("STUDENT_NO", loginVO.getMemberIdOrg());
			param.put("HASHTAG", hashtag);
			param.put("FLAG_DEL", flagDel);
			param.put("REGI_IDX", loginVO.getMemberIdx());
			param.put("REGI_ID", loginVO.getMemberId());
			param.put("REGI_NAME", loginVO.getMemberNameOrg());
			param.put("REGI_IP", request.getRemoteAddr());
		}
		
		if("Y".equals(flagDel)) {
			if(memberInfoService.hashtagDelete(param) > 0) {
				model.addAttribute("data", param);
			}
		}else {
			if(memberInfoService.hashtagInsert(param) > 0) {
				model.addAttribute("data", param);
			}
		}

		return mav;
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/myInfoProc.do")
	public String myInfoProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int useRSA = JSONObjectUtil.getInt(settingInfo, "use_rsa");	// 설정정보의 rsa사용여부

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String mbrCd = (loginVO != null)?loginVO.getMemberIdx():null;

		if(StringUtil.isEmpty(mbrCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 1. 항목설정
		String submitType = "myinfo";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)){
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. 비밀번호 체크
		if(!memberService.isMbrPwdMatched(useRSA, parameterMap, items)){
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.password.mismatched")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		boolean isUseNameAuth = PrivAuthUtil.isUseNameAuth();
		if(!isUseNameAuth) {
			// 실명인증 미사용, 이메일 중복 확인
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();

			String mbrEmail = DataSecurityUtil.getRSADecrypt(useRSA, parameterMap.getString("mbrEmail"));
			JSONObject mbrEmailItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "items"), "mbrEmail");	// mbrId 항목 설정 정보
			String dbMbrEmail = ModuleUtil.getParamToDBValue(mbrEmailItem, mbrEmail);	// RSA 및 암호화 설정에 따른 값 얻기

			searchList.add(new DTForm("A.MEMBER_IDX", mbrCd, "<>"));
			searchList.add(new DTForm("A.MEMBER_EMAIL", dbMbrEmail));
			param.put("searchList", searchList);

			if(memberService.getDuplicateCount(param) > 0) {
				model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("message.exist.duplicate"), new String[]{JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, "mbrEmail"), "item_name")})));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
		}
		
		// 회원 아이디 setting : 비밀번호 변경시 필요
		parameterMap.put("mbrId", loginVO.getMemberIdOrg());
		
		// DB
    	int result = memberService.update(submitType, mbrCd, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	@ModuleAuth(accessModule={"login"})
	@RequestMapping(value = "/pwdmodi.do")
	public String pwdmodi(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		if(!UserDetailsHelper.isLogin()) {
			return RbsProperties.getProperty("Globals.error.401.login" + ajaxPName + ".path");
		}
		
		// 2. DB
		String submitType = "pwdmodi";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
		fn_setMyInfoPath(attrVO);
    	
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_PWDMODIPROC"));
    	
		return getViewPath("/pwdmodi");
	}

	@ModuleAuth(accessModule={"login"})
	@RequestMapping(method=RequestMethod.POST, value = "/pwdmodiProc.do")
	public String pwdmodiProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String mbrCd = (loginVO != null)?loginVO.getMemberIdx():null;

		if(StringUtil.isEmpty(mbrCd)) {
			return RbsProperties.getProperty("Globals.error.401.login" + ajaxPName + ".path");
		}

		// 2. DB
		String submitType = "pwdmodi";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");

		String pwdModiType = parameterMap.getString("pwdModiType", "");
		
		int useRSA = JSONObjectUtil.getInt(attrVO.getSettingInfo(), "use_rsa");	// 설정정보의 rsa사용여부
		
		if(!StringUtil.isEquals(pwdModiType, "1") ){
			// 2. 필수입력 체크
			String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
			if(!StringUtil.isEmpty(errorMessage)){
				model.addAttribute("message", errorMessage);
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
			
			if(!memberService.isMbrPwdMatched(useRSA, parameterMap, items)){
				model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.password.mismatched")));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
		}
		
		// DB
    	int result = memberService.pwupdate1(pwdModiType, mbrCd, request.getRemoteAddr(), parameterMap, items, itemOrder);

		String message = null;
    	if(result > 0) {
    		// 저장 성공
    		if(!StringUtil.isEquals(pwdModiType, "1")) message = rbsMessageSource.getMessage("message.password.changed");
    		else message = rbsMessageSource.getMessage("message.process");
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, message, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
    	
		// 저장 실패
		if(!StringUtil.isEquals(pwdModiType, "1")) message = rbsMessageSource.getMessage("message.password.no.changed");
		else message = rbsMessageSource.getMessage("message.no.process");
		model.addAttribute("message", MessageUtil.getAlert(isAjax, message));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/join.do")
	public String join(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(attrVO.isAjax(), null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
		}
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/join");
	}

	@RequestMapping(value = "/join01.do")
	public String join01(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(attrVO.isAjax(), null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
		}
		
		// 회원약관, 개인정보 수집 이용 안내 내용
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", attrVO.getSiteId()));
		param.put("searchList", searchList);
		DataMap clauseDt = memberService.getMemberClause(param);
		
		JSONObject itemInfo = attrVO.getItemInfo();
		
		String submitType = "join01";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		model.addAttribute("clauseDt", clauseDt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/join01");
	}

	@RequestMapping(value = "/join02.do")
	public String join02(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(attrVO.isAjax(), null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
		}
		
		JSONObject itemInfo = attrVO.getItemInfo();

		String submitType = "join01";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
		// 기본경로
    	fn_setCommonPath(attrVO);

    	String checkUserAgreeName = fn_checkUserAgree(attrVO, null, request, model, items);
    	if(!StringUtil.isEmpty(checkUserAgreeName)) return checkUserAgreeName;
    	
		return getViewPath("/join02");
	}
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/join03.do")
	public String join03(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(attrVO.isAjax(), null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
		}
		
		boolean isUseNameAuth = PrivAuthUtil.isUseNameAuth();
		
		String submitType = null;
		if(isUseNameAuth) submitType = "join03_n";	// 실명인증을 사용하는 경우 보여지는 등록 항목
		else submitType = "join03";

		JSONObject itemInfo = attrVO.getItemInfo();
		
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_JOIN03PROC"));
    	
    	String checkUserAgreeName = fn_checkUserAgree(attrVO, null, request, model, items);
    	if(!StringUtil.isEmpty(checkUserAgreeName)) return checkUserAgreeName;
    	
    	if(isUseNameAuth)
    	{
    		if(!PrivAuthUtil.isNameAuthCheck(true)){
    			model.addAttribute("message", MessageUtil.getAlert(attrVO.isAjax(), rbsMessageSource.getMessage("message.name.auth.no")));
    			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
    		}

    		String isNameAuthDup = fn_nameAuthDup(attrVO, request, model);
    		if(!StringUtil.isEmpty(isNameAuthDup)) return isNameAuthDup;
    	}
    	
		return getViewPath("/join03");
	}

	/**
	 * 등록처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value = "/join03Proc.do")
	public String join03Proc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		boolean isUseNameAuth = PrivAuthUtil.isUseNameAuth();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		// 1. 항목설정
		String submitType = null;
		if(isUseNameAuth) submitType = "join03_n";	// 실명인증을 사용하는 경우 보여지는 등록 항목
		else submitType = "join03";
		
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");

    	fn_setCommonPath(attrVO);

    	String checkUserAgreeName = fn_checkUserAgree(attrVO, parameterMap, request, model, items);
    	if(!StringUtil.isEmpty(checkUserAgreeName)) return checkUserAgreeName;
    	
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)){
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 자동기입방지문자
		String ahCode = parameterMap.getString("ahCode");
		
		HttpSession session = request.getSession();
		Captcha captcha = (Captcha)session.getAttribute(Captcha.NAME);
		
		if(!captcha.isCorrect(ahCode)){
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.match.ahCode")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 아이디 중복 확인
		int useRSA = JSONObjectUtil.getInt(attrVO.getSettingInfo(), "use_rsa");	// 설정정보의 rsa사용여부
		String mbrId = DataSecurityUtil.getRSADecrypt(useRSA, parameterMap.getString("mbrId"));
		JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(items, "mbrId");	// mbrId 항목 설정 정보
		String dbMbrId = ModuleUtil.getParamToDBValue(mbrIdItem, mbrId);	// RSA 및 암호화 설정에 따른 값 얻기
		
		searchList.add(new DTForm("A.MEMBER_ID", dbMbrId));
		param.put("searchList", searchList);
		
		if(memberService.getDuplicateCount(param) > 0)
		{
			model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("message.exist.duplicate"), new String[]{JSONObjectUtil.getString(mbrIdItem, "item_name")})));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		if(isUseNameAuth)
		{
			// 실명인증 사용, DI 중복 확인
    		String isNameAuthDup = fn_nameAuthDup(attrVO, request, model);
    		if(!StringUtil.isEmpty(isNameAuthDup)) return isNameAuthDup;
		}
		else
		{
			// 실명인증 미사용, 이메일 중복 확인
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();

			String mbrEmail = DataSecurityUtil.getRSADecrypt(useRSA, parameterMap.getString("mbrEmail"));
			JSONObject mbrEmailItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "items"), "mbrEmail");	// mbrId 항목 설정 정보
			String dbMbrEmail = ModuleUtil.getParamToDBValue(mbrEmailItem, mbrEmail);	// RSA 및 암호화 설정에 따른 값 얻기

			searchList.add(new DTForm("A.MEMBER_EMAIL", dbMbrEmail));
			param.put("searchList", searchList);

			if(memberService.getDuplicateCount(param) > 0)
			{
				model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("message.exist.duplicate"), new String[]{JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, "mbrEmail"), "item_name")})));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
		}

    	int result = memberService.insert(request.getRemoteAddr(), isUseNameAuth, useRSA, parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_JOINCOMP"))) + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
    	
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/joinComp.do")
	public String joinComp(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(attrVO.isAjax(), null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
		}
		
		JSONObject itemInfo = attrVO.getItemInfo();
		
		String submitType = "join03";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/joinComp");
	}

	@RequestMapping(value = "/idsearch.do")
	public String idsearch(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(attrVO.isAjax(), null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
		}
		
		JSONObject itemInfo = attrVO.getItemInfo();

		String submitType = "idsearch";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
		// 기본경로
		fn_setIdsearchPath(attrVO);

    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
    	if(PrivAuthUtil.isUseNameAuth()) return getViewPath("/idsearch1");
    	else return getViewPath("/idsearch");
	}
	
	@RequestMapping(value = "/idsearchProc.do")
	public String idsearchProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath() + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 기본경로
		fn_setIdsearchPath(attrVO);
		
		JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(items, "mbrId");
		JSONObject mbrNameItem = JSONObjectUtil.getJSONObject(items, "mbrName");	// mbrName 항목 설정 정보
		JSONObject mbrEmailItem = JSONObjectUtil.getJSONObject(items, "mbrEmail");	// mbrEmail 항목 설정 정보
		
		String columnId = "column_id";
		String mbrIdColumn = JSONObjectUtil.getString(mbrIdItem, columnId);
		String mbrNameColumn = JSONObjectUtil.getString(mbrNameItem, columnId);
		String mbrEmailCoumn = JSONObjectUtil.getString(mbrEmailItem, columnId);

    	if(PrivAuthUtil.isUseNameAuth()){
    		if(!PrivAuthUtil.isNameAuthCheck(true)){
    			model.addAttribute("message", MessageUtil.getAlert(attrVO.isAjax(), rbsMessageSource.getMessage("message.name.auth.no")));
    			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
    		}
    		
    		HttpSession session = request.getSession();
    		String sDupInfo = StringUtil.getString(session.getAttribute("iSDupInfo"));

    		searchList.add(new DTForm("MEMBER_DI", DataSecurityUtil.getSeedEncrypt(sDupInfo)));
    	}
    	else{
    		// 1. 항목설정
    		String submitType = "idsearch";
    		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
    		
    		// 2. 필수입력 체크
    		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
    		if(!StringUtil.isEmpty(errorMessage)){
    			model.addAttribute("message", errorMessage);
    			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    		}
    		
    		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
    		model.addAttribute("submitType", submitType);
    		
    		// 3. 아이디 확인
    		int useRSA = JSONObjectUtil.getInt(settingInfo, "use_rsa");	// 설정정보의 rsa사용여부
    		
    		String mbrName = DataSecurityUtil.getRSADecrypt(useRSA, parameterMap.getString("mbrName"));
    		String dbMbrName = ModuleUtil.getParamToDBValue(mbrNameItem, mbrName);	// RSA 및 암호화 설정에 따른 값 얻기
    		
    		String mbrEmail = DataSecurityUtil.getRSADecrypt(useRSA, parameterMap.getString("mbrEmail"));
    		String dbMbrEmail = ModuleUtil.getParamToDBValue(mbrEmailItem, mbrEmail);	// RSA 및 암호화 설정에 따른 값 얻기
    		
    		searchList.add(new DTForm("A." + mbrNameColumn, dbMbrName));
    		searchList.add(new DTForm("A." + mbrEmailCoumn, dbMbrEmail));
    		searchList.add(new DTForm("A.USERTYPE_IDX", RbsProperties.getProperty("Globals.code.USERTYPE_ADMIN"), "<"));
    		
    	}

		param.put("searchList", searchList);
		
		DataMap dt = memberService.getView(param);

		String mbrIdx = null;
		String mbrId = null;
		if(dt != null){
			mbrIdx = StringUtil.getString(dt.get("MEMBER_IDX"));
			mbrId = StringUtil.getString(dt.get(mbrIdColumn));
		}
		
		if(!StringUtil.isEmpty(mbrId)){
			request.getSession().setAttribute("idsearchMbrId",ModuleUtil.getPrivDecValue(mbrIdItem,  mbrId));

			memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.idsearch"), null, null, items, mbrIdx, mbrIdx, mbrId, StringUtil.getString(dt.get(mbrNameColumn)));
			
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_INPUTCOMP"))) + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		}
		
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.member.no.exist")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	@RequestMapping(value = "/idsearchComp.do")
	public String idsearchComp(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath(StringUtil.getString(MenuUtil.getMenuUrl(Defines.CODE_MENU_INDEX))) + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 기본경로
		fn_setIdsearchPath(attrVO);
		
		HttpSession session = request.getSession();
		String mbrId = StringUtil.getString(session.getAttribute("idsearchMbrId"));
		if(StringUtil.isEmpty(mbrId)){
			model.addAttribute("message", MessageUtil.getAlert(isAjax, null, "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_INPUT"))) + "\");"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		session.setAttribute("idsearchMbrId", null);
		model.addAttribute("idsearchMbrId", mbrId);
		
		return getViewPath("/idsearchComp");
	}

	@RequestMapping(value = "/pwsearch.do")
	public String pwsearch(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(attrVO.isAjax(), null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath(StringUtil.getString(MenuUtil.getMenuUrl(Defines.CODE_MENU_INDEX))) + "');"));
			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
		}
		
		JSONObject itemInfo = attrVO.getItemInfo();

		String submitType = "pwsearch";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
		// 기본경로
		fn_setPwsearchPath(attrVO);

    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));

    	if(PrivAuthUtil.isUseNameAuth()) return getViewPath("/pwsearch1");
    	else return getViewPath("/pwsearch");
	}
	
	@RequestMapping(value = "/pwsearchProc.do")
	public String pwsearchProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath(StringUtil.getString(MenuUtil.getMenuUrl(Defines.CODE_MENU_INDEX))) + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 기본경로
		fn_setPwsearchPath(attrVO);

		// 1. 항목설정
		String submitType = "pwsearch";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
    	if(PrivAuthUtil.isUseNameAuth()){
    		if(!PrivAuthUtil.isNameAuthCheck(true)){
    			model.addAttribute("message", MessageUtil.getAlert(attrVO.isAjax(), rbsMessageSource.getMessage("message.name.auth.no")));
    			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
    		}
    	}
    	else{
    		// 2. 필수입력 체크
    		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
    		if(!StringUtil.isEmpty(errorMessage)){
    			model.addAttribute("message", errorMessage);
    			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    		}
    		
    		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
    	}
		
		model.addAttribute("submitType", submitType);

		// 3. 비밀번호 변경
		int result = memberService.pwupdate(JSONObjectUtil.getInt(settingInfo, "use_rsa"), parameterMap, items);
		
		if(result > 0){
			request.getSession().setAttribute("pwsearchMbrEmail", request.getAttribute("pwsearchMbrEmail"));
			
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_INPUTCOMP"))) + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		}
		
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.member.no.exist")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	@RequestMapping(value = "/pwsearchComp.do")
	public String pwsearchComp(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		if(UserDetailsHelper.isLogin()){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, null, "fn_procAction('" + PathUtil.getAddProtocolToFullPath(StringUtil.getString(MenuUtil.getMenuUrl(Defines.CODE_MENU_INDEX))) + "');"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 기본경로
		fn_setPwsearchPath(attrVO);
		
		HttpSession session = request.getSession();
		String mbrEmail = StringUtil.getString(session.getAttribute("pwsearchMbrEmail"));
		if(StringUtil.isEmpty(mbrEmail)){
			model.addAttribute("message", MessageUtil.getAlert(isAjax, null, "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_INPUT"))) + "\");"));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		session.setAttribute("pwsearchMbrEmail", null);
		model.addAttribute("pwsearchMbrEmail", mbrEmail);
		
		return getViewPath("/pwsearchComp");
	}
	
	@RequestMapping(value = "/joinout.do")
	public String joinout(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String mbrCd = (loginVO != null)?loginVO.getMemberIdx():null;
		if(StringUtil.isEmpty(mbrCd)) {
			return RbsProperties.getProperty("Globals.error.401.login" + ajaxPName + ".path");
		}
		
		JSONObject itemInfo = attrVO.getItemInfo();

		String submitType = "joinout";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
		// 기본경로
    	fn_setJoinoutPath(attrVO);

    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
		return getViewPath("/joinout");
	}

	@RequestMapping(value = "/joinoutProc.do")
	public String joinoutProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		String mbrIdx = null;
		String mbrId = null;
		if(loginVO != null){
			mbrIdx = loginVO.getMemberIdx();
			mbrId = loginVO.getMemberIdOrg();
		}
		
		if(StringUtil.isEmpty(mbrIdx) || StringUtil.isEmpty(mbrId)) {
			return RbsProperties.getProperty("Globals.error.401.login" + ajaxPName + ".path");
		}
		
		// 기본경로
		fn_setJoinoutPath(attrVO);

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MEMBER_IDX", mbrIdx));
		
    	if(PrivAuthUtil.isUseNameAuth()){
    		if(!PrivAuthUtil.isNameAuthCheck(true)){
    			model.addAttribute("message", MessageUtil.getAlert(attrVO.isAjax(), rbsMessageSource.getMessage("message.name.auth.no")));
    			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
    		}
    		
    		HttpSession session = request.getSession();
    		String sDupInfo = StringUtil.getString(session.getAttribute("iSDupInfo"));

    		searchList.add(new DTForm("MEMBER_DI", DataSecurityUtil.getSeedEncrypt(sDupInfo)));
    	}
    	else{
    		String submitType = "joinout";
    		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
    		
    		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
    		if(!StringUtil.isEmpty(errorMessage)){
    			model.addAttribute("message", errorMessage);
    			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    		}

    		String mbrPwd = parameterMap.getString("mbrPwd");

    		int useRSA = JSONObjectUtil.getInt(settingInfo, "use_rsa");

    		JSONObject mbrPwdItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "items"), "mbrPwd");
    		String encPwd = ModuleUtil.getParamPWToDBValue(useRSA, mbrPwdItem, mbrPwd, mbrId);
    		
    		searchList.add(new DTForm("MEMBER_PWD", encPwd));
    	}

		param.put("searchList", searchList);
		
		DataMap dt = memberService.getView(param);
		
		int result = 0;
		if(dt != null && StringUtil.isEquals(dt.get("MEMBER_IDX"), mbrIdx)){
			result = memberService.joinout(items, new String[]{mbrIdx});
		}
		
		if(result > 0){
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.member.success.joinout"), "fn_procAction(\"" + PathUtil.getAddProtocolToFullPath(MenuUtil.getMenuUrl(Defines.CODE_MENU_LOGOUT)) + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
		}
		
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.member.fail.joinout")));
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
	/*
	@RequestMapping(method=RequestMethod.POST, value = "/cdeleteProc.do")
	public String cdelete(@RequestParam(value="select") String[] deleteIdxs, HttpServletRequest request, ModelMap model) throws Exception {
		// DB
		int result = memberService.cdelete(deleteIdxs);
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.cdelete"), "location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.cdelete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	*/
	
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
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String idConfirmProcName = "idConfirmProc.do";
		String emailConfirmProcName = "emailConfirmProc.do";
		String joinName = "join.do";
		String join01Name = "join01.do";
		String join02Name = "join02.do";
		String join03Name = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/" + attrVO.getModuleId()  + "/join03.do";
		String join03ProcName = "join03Proc.do";
		String join21Name = "join01.do";
		String join22Name = "join02.do";
		String join23Name = "join03.do";
		String join23ProcName = "join03Proc.do";
		String joinCompName = "joinComp.do";
		
		String hashtagProcName = "hashtagProc.do";

		if(useSsl) {
			join03ProcName = PathUtil.getSslPagePath(join03ProcName);
		}

		request.setAttribute("URL_IDCONFIRMPROC", idConfirmProcName + baseQueryString);
		request.setAttribute("URL_EMAILCONFIRMPROC", emailConfirmProcName + baseQueryString);
		request.setAttribute("URL_JOIN", joinName + baseQueryString);
		request.setAttribute("URL_JOIN01", join01Name + baseQueryString);
		request.setAttribute("URL_JOIN02", join02Name + baseQueryString);
		request.setAttribute("URL_JOIN03", join03Name + baseQueryString);
		request.setAttribute("URL_JOIN03PROC", join03ProcName + baseQueryString);
		request.setAttribute("URL_JOIN21", join21Name + baseQueryString);
		request.setAttribute("URL_JOIN22", join22Name + baseQueryString);
		request.setAttribute("URL_JOIN23", join23Name + baseQueryString);
		request.setAttribute("URL_JOIN23PROC", join23ProcName + baseQueryString);
		request.setAttribute("URL_JOINCOMP", joinCompName + baseQueryString);
		
		request.setAttribute("URL_HASHTAG_PROC", hashtagProcName + baseQueryString);
	}

	/**
	 * 아이디 찾기 경로
	 * @param attrVO
	 */
	public void fn_setIdsearchPath(ModuleAttrVO attrVO) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		JSONObject settingInfo = attrVO.getSettingInfo();
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		DataForm queryString = attrVO.getQueryString();
		
		String inputName = "idsearch.do";
		String inputProcName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/" + attrVO.getModuleId()  + "/idsearchProc.do";
		String inputCompName = "idsearchComp.do";
		
		if(useSsl) {
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setInputPath(queryString, baseParams, inputName, inputProcName);

		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		request.setAttribute("URL_INPUTCOMP", inputCompName + baseQueryString);
	}
	
	public void fn_setPwsearchPath(ModuleAttrVO attrVO) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		JSONObject settingInfo = attrVO.getSettingInfo();
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		DataForm queryString = attrVO.getQueryString();
		
		String inputName = "pwsearch.do";
		String inputProcName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/" + attrVO.getModuleId()  + "/pwsearchProc.do";
		String inputCompName = "pwsearchComp.do";
		
		if(useSsl) {
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setInputPath(queryString, baseParams, inputName, inputProcName);

		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		request.setAttribute("URL_INPUTCOMP", inputCompName + baseQueryString);
	}

	public void fn_setJoinoutPath(ModuleAttrVO attrVO) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();

		JSONObject settingInfo = attrVO.getSettingInfo();
		
		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		DataForm queryString = attrVO.getQueryString();
		
		String inputName = "joinout.do";
		String inputProcName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/" + attrVO.getModuleId()  + "/joinoutProc.do";
		
		if(useSsl) {
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setInputPath(queryString, baseParams, inputName, inputProcName);
	}
	
	public String fn_checkUserAgree(ModuleAttrVO attrVO, @ParamMap ParamForm parameterMap, HttpServletRequest request, ModelMap model, JSONObject items)
	{
		String checkUserAgreeName = null;
		String join01SubmitType = "join01";
		
		JSONArray join01ItemOrder = JSONObjectUtil.getJSONArray(attrVO.getItemInfo(), join01SubmitType + "_order");
		model.addAttribute("join01ItemOrder", join01ItemOrder);
		
		if(join01ItemOrder != null)
		{
			for(int i = 0; i < join01ItemOrder.size(); i++)
			{
				String itemId = JSONObjectUtil.getString(join01ItemOrder, i);
				if(!StringUtil.isEquals(parameterMap == null ? request.getParameter(itemId) :  parameterMap.getString(itemId), "1"))
				{
					checkUserAgreeName = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, itemId), "item_name", rbsMessageSource.getMessage("errors.message.errorCode2"));
					break;
				}
			}
		}
		
		if(!StringUtil.isEmpty(checkUserAgreeName)){
    		String message = null;
    		String errorCode = rbsMessageSource.getMessage("errors.message.errorCode2");
    		
    		if(StringUtil.isEquals(checkUserAgreeName, errorCode)) message = errorCode;
    		else message = MessageFormat.format(rbsMessageSource.getMessage("message.member.join.agree"), new String[]{checkUserAgreeName});
    		
    		model.addAttribute("message", MessageUtil.getAlert(attrVO.isAjax(), message, "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_JOIN01"))) + "\");"));
    		return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
    	}
		
		return checkUserAgreeName;
	}
	
	public String fn_nameAuthDup(ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model){

		HttpSession session = request.getSession();
		String dupInfo = StringUtil.getString(session.getAttribute("iSDupInfo"));
		if(StringUtil.isEmpty(dupInfo)) {
			model.addAttribute("message", MessageUtil.getAlert(attrVO.isAjax(), rbsMessageSource.getMessage("message.name.auth.no")));
			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.MEMBER_DI",  DataSecurityUtil.getSeedEncrypt(StringUtil.getString(session.getAttribute("iSDupInfo")))));
		param.put("searchList", searchList);

		if(memberService.getDuplicateCount(param) > 0)
		{
			model.addAttribute("message", MessageUtil.getAlert(attrVO.isAjax(), rbsMessageSource.getMessage("message.already.member.exist")));
			return RbsProperties.getProperty("Globals.fail" + attrVO.getAjaxPName() + ".path");
		}
		
		return null;
	}
}
