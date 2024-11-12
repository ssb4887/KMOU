package rbs.modules.haksa.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.modules.code.serviceOra.CodeOptnServiceOra;
import rbs.modules.haksa.service.HaksaAdmService;
import rbs.modules.member.service.MemberInfoService;
import rbs.modules.nonSbjt.service.NonSbjtService;


/**
 * 샘플모듈<br/>
 * : 통합관리시스템 > 메뉴콘텐츠관리, 통합관리시스템 > 기능등록관리, 사용자 사이트 에서 사용
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="haksa")
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/haksa", "/{siteId}/haksa"})
public class HaksaController extends ModuleController {
	
	@Resource(name = "haksaAdmService")
	private HaksaAdmService haksaAdmService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Resource(name = "codeOptnServiceOra")
	protected CodeOptnServiceOra codeOptnServiceOra;
	
	@Resource(name = "MemberInfoService")
	private MemberInfoService memberInfoService;
	
	@Resource(name = "nonSbjtService")
	private NonSbjtService nonSbjtService;
	
	
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
	 * 목록조회(기본화면)
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/list");
	}
	
	/**
	 * 목록조회(ajax)
	 */
	@ModuleAuth(name="LST")
	@Transactional
	@RequestMapping(value = "/getStudentList.do", headers="Ajax", method = RequestMethod.POST)
	public ModelAndView getStudentList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		
		//권한 넘버 가져오기
		int userTypeIdx = loginVO.getUsertypeIdx();
		//ID가져오기
		String memberIdx = loginVO.getMemberIdx();
		//부서정보 가져오기
		String deptCd = loginVO.getDeptCd();
		
		
		
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
			int page = StringUtil.getInt(rawJsonObj.getString("PAGE"), 1);				// 현재 페이지 index
	
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
		
		param.put("SEARCH_TEXT", rawJsonObj.getString("SEARCH_TEXT"));
		param.put("SEARCH_RANGE", rawJsonObj.getString("SEARCH_RANGE"));
		param.put("searchList", searchList);
		// 2.2 목록수
    	totalCount = haksaAdmService.getTotalCount(param);
    	
		paginationInfo.setTotalRecordCount(totalCount);

    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    	}
    		
    		// 2.3 목록
    		list = haksaAdmService.getList(param);
		}
    	
    	// 3. 속성 setting
//    	model.addAttribute("mjCdList", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 전공코드
    	model.addAttribute("submitType", submitType);
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", searchOptnHashMap);									// 항목코드
		model.addAttribute("optnHashMap", optnHashMap);
		model.addAttribute("userTypeIdx", userTypeIdx);

		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	ModelAndView mav = new ModelAndView("jsonView");
    	
    	return mav;
	}
	
	/**
	 * 학사정보 조회
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/viewInfo.do")
	public String viewInfo(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		
		// 학번(=아이디) ; 복호화
		String memberId = request.getParameter("STUDENT_NO");
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
		

		/* 비교과 신청이력 */
		List<Object> userNonSbjtSigninList = nonSbjtService.getMyNonSbjtSigninHist(param);
		model.addAttribute("USER_NON_SBJT_SIGNIN", (userNonSbjtSigninList == null) ? null : userNonSbjtSigninList);
		
		
		model.addAttribute("TARGET", "#" + request.getParameter("target"));	// 탭 이동(element id값)		
		model.addAttribute("dt", dt);
    	
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	model.addAttribute("URL_HASHTAG_PROC", request.getAttribute("URL_HASHTAG_PROC"));
		return getViewPath("/studentInfo");
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
	@RequestMapping(value = "/input.do")
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();

		
		// 1. 필수 parameter 검사
		// 1.1 필수 key parameter 검사
		String staffNo = StringUtil.getString(request.getParameter("staffNo"));
		
		


		if(staffNo == null ) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}
		
		// 2. DB
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		
		searchList.add(new DTForm("A.STAFF_NO", staffNo));
		
		param.put("searchList", searchList);
		
		// 2.2 상세정보
		dt = haksaAdmService.getModify(fnIdx, param);
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
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분
		
		// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 5. form action
		
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
	@ModuleAuth(name={"WRT"})
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@RequestParam("file") MultipartFile file, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
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
		String staffNo = StringUtil.getString(request.getParameter("staffNo"));
		
		String fileOriginName = file.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		String extension = fileOriginName.substring(fileOriginName.lastIndexOf("."));
		String fileSavedName =  uuid + extension; 
		String uploadDirectory = RbsProperties.getProperty("Globals.fileStorePath") + "rbs/haksa/";
		
		Path filePath = Paths.get(uploadDirectory, fileSavedName);
		
		if(!Files.deleteIfExists(filePath)) {
			try {
				Files.createDirectories(filePath);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		try {
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch(IOException e){
			e.printStackTrace();
		}
		
		parameterMap.put("fileSavedName", fileSavedName);
		parameterMap.put("fileOriginName", fileOriginName);

		System.out.println("fileSavedName :::: " + fileSavedName);
		System.out.println("fileOriginName :::: " + fileOriginName);
		
		// 2. DB
		// 2.1 상세정보 - 해당글이 없는 경우 return
		if(staffNo == null ) {
			return RbsProperties.getProperty("Globals.error.400.path");
		}

		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.STAFF_NO", staffNo));
		param.put("searchList", searchList);

		dt = haksaAdmService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. 필수입력 체크
		// 3.1 항목설정
		String submitType = "modify";		// 항목 order명
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
    	int result = haksaAdmService.update(uploadModulePath, fnIdx, param, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);

    	
    	if(result > 0) {
    		// 4.1 저장 성공
    		
        	// 기본경로
        	fn_setCommonPath(attrVO);
        	
    		String inputNpname = fn_dsetInputNpname(settingInfo);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_" + inputNpname))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
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
	
	
	@GetMapping("/download.do")
	@ResponseBody
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String savedFileName = request.getParameter("savedFileName");
        String originFileName = request.getParameter("originFileName");
        File file = new File(RbsProperties.getProperty("Globals.fileStorePath") + "rbs/haksa/" + savedFileName);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originFileName, "UTF-8") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        // 파일을 읽고 클라이언트에게 전송하는 과정에서 try-with-resources 구문 사용
        try (InputStream fileInputStream = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            byte[] fileByte = new byte[fileInputStream.available()];
            fileInputStream.read(fileByte);
            outputStream.write(fileByte);
            outputStream.flush();
        } 
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
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String searchUnivListName = "searchUnivList.json";
		String searchSubjectListName = "searchSubjectList.json";
		String bookmarkInputName = "inputHaksaBKProc.json";
		
		request.setAttribute("URL_SEARCHUNIVLIST", searchUnivListName += baseQueryString);
		request.setAttribute("URL_SEARCHSUBJECTLIST", searchSubjectListName += baseQueryString);
		request.setAttribute("URL_BOOKMARKINPUT", bookmarkInputName += baseQueryString);
		request.setAttribute("URL_VIEWINFO", "viewInfo.do" + baseQueryString);
	}

	/**
	 * 휴지통 경로
	 * @param attrVO
	
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
	 */
	
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
			modiCnt = haksaService.getAuthCount(fnIdx, param);
			
			return (modiCnt > 0);
		}
		
		return true;
	}
	*/
	
	
}
