package rbs.modules.member.web;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DTDateForm;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
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
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.egovframework.WebsiteVO;
import rbs.modules.member.service.MemberAnLogService;
import rbs.modules.member.service.MemberAnService;
import rbs.modules.member.service.MemberService;

@Controller
@ModuleMapping(moduleId="memberAn", confModule="member", confSModule="memberAn")
@RequestMapping("/{admSiteId}/memberAn")
public class MemberAnController extends ModuleController{

	@Resource(name = "memberAnService")
	private MemberAnService memberAnService;
	
	@Resource(name = "memberService")
	private MemberService memberService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@Resource(name = "memberAnLogService")
	private MemberAnLogService memberLogService;
	private Logger logger = LoggerFactory.getLogger("admMember");

	@ModuleAuth(name="MNG")
	@RequestMapping("/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		//이동근 - 관리자 페이지에 보여줄 사용자 페이지 가져오기
		WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		
		String urSiteMode = RbsProperties.getProperty("Globals.site.mode.usr");										// 사용자 모드
		JSONObject urSiteObject = MenuUtil.getSiteObject("/" + urSiteMode + "/" + siteId, false);					// 사이트, 메뉴정보 전체
		JSONArray usrSiteMenuList = null;
		JSONObject usrSiteMenus = null;
    	usrSiteMenuList = JSONObjectUtil.getJSONArray(urSiteObject, "menu-list");
	    usrSiteMenus = JSONObjectUtil.getJSONObject(urSiteObject, "menus");
		model.addAttribute("usrSiteMenuList", usrSiteMenuList);
		model.addAttribute("usrSiteMenus", usrSiteMenus);
		
		String listSearchId = "list_search";		// 검색설정
    	
		// 1. 페이지정보 setting
		int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
		int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
		int listUnitStep = listUnit;																							// 페이지당 목록 수 증가값
		
		int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
		if(pageUnit == 0) pageUnit = listUnit;																					// 페이지당 목록 수
		int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));			// 페이징 크기
		int page = StringUtil.getInt(request.getParameter("page"), 1);															// 현재 페이지 index

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
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		
		// 사용자 그룹 검색
		String searchKeyFlag = RbsProperties.getProperty("Globals.item.search.pre.flag");
		String mbrGrpItemName = "mbrGrp";
		String mbrGrpCd = queryString.getString(searchKeyFlag + mbrGrpItemName);
		if(!StringUtil.isEmpty(mbrGrpCd)) {
			param.put("mbrGrpCd", mbrGrpCd);
			model.addAttribute("isSearchList", new Boolean(true));
		}

		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = memberAnService.getTotalCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = memberAnService.getList(param);
    	}
    	
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	HashMap<String, Object> searchOptnHashMap = CodeHelper.getItemOptnSearchHashMap(listSearch);	// 검색항목코드

		// 사용자그룹 추가
		JSONArray searchOrder = JSONObjectUtil.getJSONArray(listSearch, "searchAddOptn_order");
		JSONObject keyInfo = JSONObjectUtil.getJSONObject(listSearch, "key");
		JSONObject keyItems = JSONObjectUtil.getJSONObject(keyInfo, "items");
		HashMap<String, Object> searchAddOptnHashMap = CodeHelper.getItemOptnHashMap(true, keyItems, searchOrder);
		if(searchAddOptnHashMap != null) {
			if(searchOptnHashMap == null) searchOptnHashMap = new HashMap<String, Object>();
			searchOptnHashMap.putAll(searchAddOptnHashMap);
		}
		model.addAttribute("searchOptnHashMap", searchOptnHashMap);	// 검색항목코드
		    	
    	// 3. 경로 setting
    	// 3.1 기본경로
    	fn_setCommonPath(attrVO);

		return getViewPath("/list");
	}

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

		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String mbrCd = (loginVO != null)?loginVO.getMemberIdx():null;
		if(StringUtil.isEmpty(mbrCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.MEMBER_IDX", mbrCd));
		param.put("searchList", searchList);
		
		dt = memberAnService.getModify(param);
		String submitType = "myinfo";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("dt", dt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
		fn_setMyInfoPath(attrVO);
    	
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
		return getViewPath("/myInfo");
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/myInfoProc.do")
	public String myInfoProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String mbrCd = (loginVO != null)?loginVO.getMemberIdx():null;

		if(StringUtil.isEmpty(mbrCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		String submitType = "myinfo";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// DB
    	int result = memberAnService.update(submitType, mbrCd, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
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
	@RequestMapping(value = "/input.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		String mbrCd = request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name"));

		if(!isModify || StringUtil.isEmpty(mbrCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.MEMBER_IDX", mbrCd));
		param.put("searchList", searchList);
		
		dt = memberAnService.getModify(param);

		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		List<Object> memberGrupList = memberAnService.getMemberGrupList(param);

		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		model.addAttribute("dt", dt);
		model.addAttribute("memberGrupList", memberGrupList);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String mbrIdx = null;
		String mbrId = null;
		String mbrName = null;
		if(loginVO != null){
			mbrIdx = loginVO.getMemberIdx();
			mbrId = loginVO.getMemberId();
			mbrName = loginVO.getMemberName();
		}

		memberLogService.setEprivacy(logger, rbsMessageSource.getMessage("message.member.log.view"), null, null, items, mbrCd, mbrIdx, mbrId, mbrName);
		
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
		return getViewPath("/input");
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/input.do")
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		JSONObject itemInfo = attrVO.getItemInfo();
		
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
		return getViewPath("/input");
	}

	/**
	 * 수정처리
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		String mbrCd = request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name"));

		if(!isModify || StringUtil.isEmpty(mbrCd)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{attrVO.isAdmMode(), submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// DB
    	int result = memberAnService.update(submitType, mbrCd, request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_LIST"))) + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 등록처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 2. DB
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{attrVO.isAdmMode(), submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		int useRSA = JSONObjectUtil.getInt(attrVO.getSettingInfo(), "use_rsa");	// 설정정보의 rsa사용여부
		
		// 아이디 중복 확인
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		String mbrId = DataSecurityUtil.getRSADecrypt(useRSA, parameterMap.getString("mbrId"));
		JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(items, "mbrId");	// mbrId 항목 설정 정보
		String dbMbrId = ModuleUtil.getParamToDBValue(mbrIdItem, mbrId);	// RSA 및 암호화 설정에 따른 값 얻기
		
		searchList.add(new DTForm("A.MEMBER_ID", dbMbrId));
		param.put("searchList", searchList);
		
		if(memberService.getDuplicateCount(param) > 0){
			model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("message.exist.duplicate"), new String[]{JSONObjectUtil.getString(mbrIdItem, "item_name")})));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 이메일 중복 확인
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
		
    	int result = memberAnService.insert(request.getRemoteAddr(), parameterMap, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procActionOpReload(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_LIST"))) + "\");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 삭제
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/deleteProc.do")
	public String delete(@RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		// DB
		int result = memberAnService.delete(deleteIdxs, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
			model.addAttribute("message",   MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.delete"), "top.location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 복원
	 * @param restoreIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/restoreProc.do")
	public String restore(@RequestParam(value="select") String[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		
		// DB
		int result = memberAnService.restore(restoreIdxs, request.getRemoteAddr());
    	
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
	 * 완전삭제
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="CDT")
	@RequestMapping(method=RequestMethod.POST, value = "/cdeleteProc.do")
	public String cdelete(@RequestParam(value="select") String[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		
		// DB
		int result = memberAnService.cdelete(items, deleteIdxs);
    	
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
	 * 삭제목록 : 전체 목록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/deleteList.do")
	public String deleteList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String listSearchId = "list_search";		// 검색설정

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
		
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.LAST_MODI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		// 사용자 그룹 검색
		String searchKeyFlag = RbsProperties.getProperty("Globals.item.search.pre.flag");
		String mbrGrpItemName = "mbrGrp";
		String mbrGrpCd = queryString.getString(searchKeyFlag + mbrGrpItemName);
		if(!StringUtil.isEmpty(mbrGrpCd)) {
			param.put("mbrGrpCd", mbrGrpCd);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = memberAnService.getDeleteCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = memberAnService.getDeleteList(param);
    	}
    	
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	HashMap<String, Object> searchOptnHashMap = CodeHelper.getItemOptnSearchHashMap(listSearch);	// 검색항목코드

		// 사용자그룹 추가
		JSONArray searchOrder = JSONObjectUtil.getJSONArray(listSearch, "searchAddOptn_order");
		JSONObject keyInfo = JSONObjectUtil.getJSONObject(listSearch, "key");
		JSONObject keyItems = JSONObjectUtil.getJSONObject(keyInfo, "items");
		HashMap<String, Object> searchAddOptnHashMap = CodeHelper.getItemOptnHashMap(true, keyItems, searchOrder);
		if(searchAddOptnHashMap != null) {
			if(searchOptnHashMap == null) searchOptnHashMap = new HashMap<String, Object>();
			searchOptnHashMap.putAll(searchAddOptnHashMap);
		}
		model.addAttribute("searchOptnHashMap", searchOptnHashMap);	// 검색항목코드
		    	
    	// 3. 경로 setting
    	// 3.1 기본경로
    	fn_setDeleteListPath(attrVO);
    	
		return getViewPath("/deleteList");
	}

	@ModuleAuth(name="MNG")
	@RequestMapping("/mbrLogList.do")
	public String mbrLogList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		DataForm queryString = attrVO.getQueryString();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 1. 페이지정보 setting
		int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
		int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
		int listUnitStep = listUnit;																							// 페이지당 목록 수 증가값
		
		int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
		if(pageUnit == 0) pageUnit = listUnit;																					// 페이지당 목록 수
		int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));			// 페이징 크기
		int page = StringUtil.getInt(request.getParameter("page"), 1);															// 현재 페이지 index

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
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");
		String idxColumn = JSONObjectUtil.getString(settingInfo, "idx_column");
		String mbrIdx = queryString.getString(idxName);
		if(!StringUtil.isEmpty(mbrIdx)) searchList.add(new DTForm("A." + idxColumn, mbrIdx));
		
		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		String searchKeyFlag = RbsProperties.getProperty("Globals.item.search.pre.flag");
		String isRegiStartDate = queryString.getString(searchKeyFlag + "regiStartDate");
		boolean isEmptyRegiStartDate = StringUtil.isEmpty(isRegiStartDate);
		if(!isEmptyRegiStartDate) searchList.add(new DTDateForm("A.REGI_DATE", isRegiStartDate, ">="));
		String isRegiEndDate = queryString.getString(searchKeyFlag + "regiEndDate");
		boolean isEmptyRegiEndDate = StringUtil.isEmpty(isRegiEndDate);
		if(!isEmptyRegiEndDate) searchList.add(new DTDateForm("A.REGI_DATE", isRegiEndDate, "<="));
		String isRegiId = queryString.getString(searchKeyFlag + "regiId");
		boolean isEmptyRegiId = StringUtil.isEmpty(isRegiId);
		if(!isEmptyRegiId) searchList.add(new DTForm("A.REGI_ID", ModuleUtil.getMemberItemValue("mbrId", isRegiId)));
		String[] isLogType = StringUtil.toStringArray(queryString.getArrayList(searchKeyFlag + "logType"));
		boolean isEmptyLogType = StringUtil.isEmpty(isLogType);
		if(!isEmptyLogType){
			searchList.add(new DTForm("A.LOG_TYPE", isLogType));
		}
		
		if(!isEmptyRegiStartDate || !isEmptyRegiEndDate || !isEmptyRegiId || !isEmptyLogType) model.addAttribute("isSearchList", new Boolean(true));
		
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = memberLogService.getTotalCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = memberLogService.getList(param);
    	}
    	
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
		    	
    	// 3. 경로 setting
    	// 3.1 기본경로
    	fn_setMbrLogCommonPath(attrVO);

		return getViewPath("/mbrLogList");
	}

	/**
	 * 내정보수정 경로
	 */
	public void fn_setMyInfoPath(ModuleAttrVO attrVO) {

		DataForm queryString = attrVO.getQueryString();
		
		String inputName = "myInfo.do";
		String inputProcName = "myInfoProc.do";
		PathUtil.fn_setInputPath(queryString, baseParams, inputName, inputProcName);
	}
	
	/**
	 * 경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();

		boolean useSsl = JSONObjectUtil.isEquals(settingInfo, "use_ssl", "1");
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		
		String listName = "list.do";
		String viewName = "view.do";
		String inputName = "input.do";
		String inputProcName = "inputProc.do";
		String deleteProcName = "deleteProc.do";
		String deleteListName = "deleteList.do";
		String imageName = "image.do";
		String downloadName = "download.do";
		
		if(useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, null, null, pageName, idxName, listName, viewName, inputName, inputProcName, deleteProcName, deleteListName, imageName, null, downloadName);
		
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		fn_setCommonAddPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setCommonAddPath(HttpServletRequest request) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String idConfirmProcName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/member/idConfirmProc.do";
		String emailConfirmProcName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/member/emailConfirmProc.do";
		String grupSearchName = request.getContextPath() + "/" + request.getAttribute("crtSiteId") + "/group/searchList.do";
		String idConfirmProcUrl = idConfirmProcName;
		String emailConfirmProcUrl = emailConfirmProcName;
		String grupSearchUrl = grupSearchName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			idConfirmProcUrl += baseQueryString;
			emailConfirmProcUrl += baseQueryString;
			grupSearchUrl += baseQueryString;
		}

		request.setAttribute("URL_IDCONFIRMPROC", idConfirmProcUrl);
		request.setAttribute("URL_EMAILCONFIRMPROC", emailConfirmProcUrl);
		request.setAttribute("URL_GRUPSEARCHLIST", grupSearchUrl);
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

	public void fn_setMbrLogCommonPath(ModuleAttrVO attrVO) {

		JSONObject settingInfo = attrVO.getSettingInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String searchKeyFlag = RbsProperties.getProperty("Globals.item.search.pre.flag");
		String[] searchParams = {searchKeyFlag + "regiStartDate", searchKeyFlag + "regiEndDate"
				, searchKeyFlag + "regiId", searchKeyFlag + "logType", idxName};
		
		String listName = "mbrLogList.do";
		String viewName = "mbrLogList.do";
		String downloadName = "";
		String[] baseParams = StringUtil.addStringToArray(this.baseParams, idxName);
		PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, null, null, pageName, idxName, listName, viewName, null, null, null, null, null, null, downloadName);
	}
}
