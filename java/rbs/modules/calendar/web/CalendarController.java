package rbs.modules.calendar.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rbs.egovframework.LoginVO;
import rbs.modules.calendar.service.CalendarService;

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
import com.woowonsoft.egovframework.util.CookieUtil;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/calendar", "/{admSiteId}/moduleFn/calendar", "/{siteId}/calendar"})
@ModuleMapping(moduleId="calendar")
public class CalendarController extends ModuleController {
	
	@Resource(name = "calendarService")
	private CalendarService calendarService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	protected void setCalendar(HttpServletRequest request)
	{
		int year = 0;
		int month = 0;
		int date = 0;
		int firstDayOfWeek = 0;
		int endDay = 0;
		
		Calendar calendar = Calendar.getInstance();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");

		//현재년도
		request.setAttribute("nyear", sdf2.format(calendar.getTime()));
		
		// 오늘날짜
		String sysYMD = sdf3.format(calendar.getTime());
		request.setAttribute("sysYMD", sysYMD);
		
		request.setAttribute("nowDate", sdf.format(calendar.getTime()));
		
		String nowDate = sdf.format(calendar.getTime());
		request.setAttribute("nowDate", nowDate);
		request.setAttribute("nowDay", new Integer(calendar.get(Calendar.DAY_OF_MONTH)));
		if(request.getParameter("year") != null)
		{
			year = Integer.parseInt(request.getParameter("year"));
			calendar.set(Calendar.YEAR, year);
		}
		else year = calendar.get(Calendar.YEAR);
		if(request.getParameter("month") != null)
		{
			month = Integer.parseInt(request.getParameter("month"));
			calendar.set(Calendar.MONTH, month);
		}
		else month = calendar.get(Calendar.MONTH);
		if(request.getParameter("date") != null)
		{
			date = Integer.parseInt(request.getParameter("date"));
			calendar.set(Calendar.DATE, date);
		}
		// 오늘날짜
		
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		

		String nowYM = sdf.format(calendar.getTime());
		
		if(!nowDate.equals(nowYM) && (date == 1 || (date > 27 && date <= 31))) request.setAttribute("nowDay", 0);
		
		// 현재 년, 월, 요일(일월화수목금토:1234567)
		request.setAttribute("nowYear", new Integer(year));
		request.setAttribute("nowMonth", new Integer(month));
		request.setAttribute("firstDayOfWeek", new Integer(firstDayOfWeek));
		request.setAttribute("endDay", new Integer(endDay));

		// 현재년월 title
		//request.setAttribute("nowYM", nowYM.replaceAll("-", ". "));
		request.setAttribute("nowYM", nowYM);
		request.setAttribute("nowYMR", nowYM.replaceAll("\\.", "-"));

		calendar.add(Calendar.MONTH, -1);
		
		// 이전 년월
		request.setAttribute("preYear", new Integer(calendar.get(Calendar.YEAR)));
		request.setAttribute("preMonth", new Integer(calendar.get(Calendar.MONTH)));
		request.setAttribute("preDay", calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		calendar.add(Calendar.MONTH, 2);

		// 다음 년월
		request.setAttribute("nextYear", new Integer(calendar.get(Calendar.YEAR)));
		request.setAttribute("nextMonth", new Integer(calendar.get(Calendar.MONTH)));
		request.setAttribute("nextDay", new Integer(1));
	}
	
	

	
	
	/**
	 * 목록조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/list.do")
	public String list(HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		JSONObject crtMenu = attrVO.getCrtMenu();
		boolean isAdmMode = attrVO.isAdmMode();
		
		int fnIdx=attrVO.getFnIdx();         
		
		setCalendar(request);
		
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
		List<?> dayEventList = null;
		int totalCount = 0;
		List<?> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> param1 = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		
		
		String nowYMR = StringUtil.getString(request.getAttribute("nowYMR"));
		String searchDate = queryString.getString("date");
		String endDay = StringUtil.getLPAD(StringUtil.getInt(request.getAttribute("endDay")), "0", 2);
		if(StringUtil.isEmpty(searchDate))
		{
			searchList.add(new DTForm("A.CAL_DATE1", nowYMR + "-" + endDay, "<="));
			searchList.add(new DTForm("A.CAL_DATE2", nowYMR + "-01", ">="));
		} else {
			searchList.add(new DTForm("A.CAL_DATE1", nowYMR + "-" + searchDate, "<="));
			searchList.add(new DTForm("A.CAL_DATE2", nowYMR + "-" + searchDate, ">="));
			//model.addAttribute("isSearchList", new Boolean(true));
		}
		
		searchList1.add(new DTForm("A.CAL_DATE1", nowYMR + "-" + endDay, "<="));
		searchList1.add(new DTForm("A.CAL_DATE2", nowYMR + "-01", ">="));
		dayEventList = calendarService.getList(fnIdx, param1);

		param.put("searchList", searchList);
		// 2.2 목록수
    	totalCount = calendarService.getTotalCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = calendarService.getList(fnIdx, param);
    	}
		

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	request.setAttribute("dayEventList", dayEventList);
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목코드
        
    	
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/list");
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
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		
		// 1. 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxColumnName, brdIdx));
		param.put("searchList", searchList);

		// 2.1 상세정보
		dt = calendarService.getView(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		/****************************************************************************
		 * 조회수 수정 
		 ****************************************************************************/
		int input = queryString.getInt("input");		// 글쓰기 후 확인  페이지 여부
		boolean isAddViews = false;
		String cookieName = request.getAttribute("crtSiteId") + "_" + StringUtil.getInt(request.getParameter("mId")) + "_" + fnIdx + "_calendar_" + brdIdx;
		String cookieSetValue = brdIdx + "";
		String cookieVal = null;
		if(input != 1) {
			// 저장된 쿠키 가져오기
			cookieVal = CookieUtil.getCookie(request, cookieName);
			System.out.println("cookieVal==>" + cookieName + ":" + cookieSetValue + ":" + cookieVal);
			if(StringUtil.isEmpty(cookieVal) || !StringUtil.isEquals(cookieVal, cookieSetValue))	{

				// 조회수증가
		    	calendarService.updateViews(fnIdx, brdIdx);
				dt.put("VIEWS", StringUtil.getInt(dt.get("VIEWS")) + 1);
				isAddViews = true;
				model.addAttribute("addViews", isAddViews);
			}
			
			// 쿠키저장
			if(StringUtil.isEmpty(cookieVal)) CookieUtil.setCookie(response, cookieName, cookieSetValue, 60*60*24, "UTF-8");
		}

		// 3. 항목설정
		String submitType = "view";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 4. 속성 setting
		model.addAttribute("dt", dt);																				// 상세정보
		model.addAttribute("multiFileHashMap", calendarService.getMultiFileHashMap(fnIdx, brdIdx, items, itemOrder));	// multi file 목록
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(dt, items, itemOrder));						// 항목코드
		model.addAttribute("submitType", submitType);																// 페이지구분
		
    	// 5. 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/view");
	}
	
	/**
	 * 수정 : 관리권한 없는 경우 - 자신이 등록한 글만 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="WRT")
	@RequestMapping(value = "/input.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		if(!isModify || brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxColumnName, brdIdx));
		param.put("searchList", searchList);
		
		// 2.1 상세정보
		dt = calendarService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. 전체관리/완전삭제 권한 체크
		Boolean isMngAuth = isMngAuth(StringUtil.getString(dt.get("REGI_IDX")));
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 5. 속성 setting
		model.addAttribute("dt", dt);																				// 상세정보
		model.addAttribute("multiFileHashMap", calendarService.getMultiFileHashMap(fnIdx, brdIdx, items, itemOrder));	// multi file 목록
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));							// 항목코드
		model.addAttribute("submitType", submitType);																// 페이지구분
		
    	// 6. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 7. form action
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
	@ModuleAuth(name="WRT")
	@RequestMapping(value = "/input.do")
	public String input(HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 1. 작성자명 setting
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		DataMap dt = new DataMap();

		// 2. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		if(loginVO != null)	{
			String memberName = loginVO.getMemberNameOrg();
			JSONObject nameItem = JSONObjectUtil.getJSONObject(items, "name");
			int objectType = JSONObjectUtil.getInt(nameItem, "object_type");
			if(objectType == 101) {
				// 암/복호화 항목
				memberName = loginVO.getMemberName();
			}
			dt.put("NAME", memberName);
		}

		// 3. 속성 setting
		model.addAttribute("dt", dt);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		model.addAttribute("submitType", submitType);

    	// 4. 기본경로
    	fn_setCommonPath(attrVO);

    	// 5. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
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
	@ModuleAuth(name="WRT")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		String siteMode=attrVO.getSiteMode();
		String uploadModulePath = attrVO.getUploadModulePath();
		boolean isAdmMode = attrVO.isAdmMode();
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(!isModify || brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. 전체관리/완전삭제 권한 체크
		Boolean isMngAuth = isMngProcAuth(brdIdx,attrVO);
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
			

		// 3. 항목설정
		String submitType = "modify";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 4. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
    	int result = calendarService.update(uploadModulePath, fnIdx, brdIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
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
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, HttpServletRequest request, HttpServletResponse response, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		boolean isAjax = attrVO.isAjax();
        String siteMode=attrVO.getSiteMode();
        int fnIdx=attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		String uploadModulePath = attrVO.getUploadModulePath();

		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. DB
    	int result = calendarService.insert(uploadModulePath, fnIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -1) {
    		// 파일업로드 오류
			String fileFailView = getFileFailViewPath(request);
			if(!StringUtil.isEmpty(fileFailView)) return fileFailView;
    	}
		// 저장 실패
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
	@RequestMapping(method=RequestMethod.POST, value = "/deleteProc.do", params="select")
	public String delete(@RequestParam(value="select") String[] deleteIdxs, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		String siteMode=attrVO.getSiteMode();
		// 1. DB
		int result = calendarService.delete(fnIdx, deleteIdxs, siteMode, request.getRemoteAddr());
    	
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
	 * 삭제처리 : 관리권한 없는 경우 - 자신이 등록한 글만 수정
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="WRT")
	@RequestMapping(method=RequestMethod.GET, value = "/deleteProc.do")
	public String delete(HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		String siteMode=attrVO.getSiteMode();
		// 1. 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. 전체관리/완전삭제 권한 체크
		Boolean isMngAuth = isMngProcAuth(brdIdx, attrVO);
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		String[] deleteIdxs = {brdIdx + ""};
		// 2. DB
		int result = calendarService.delete(fnIdx, deleteIdxs, siteMode, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.delete"), "fn_procAction(\"" + request.getAttribute("URL_LIST") + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	/**
	 * 복원처리
	 * @param restoreIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/restoreProc.do", params="select")
	public String restore(@RequestParam(value="select") String[] restoreIdxs, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		String siteMode=attrVO.getSiteMode();
		// 1. DB
		int result = calendarService.restore(fnIdx, restoreIdxs, siteMode, request.getRemoteAddr());
    	
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
	 * 완전삭제처리
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="CDT")
	@RequestMapping(method=RequestMethod.POST, value = "/cdeleteProc.do", params="select")
	public String cdelete(@RequestParam(value="select") String[] deleteIdxs, HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject itemInfo = attrVO.getItemInfo();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx=attrVO.getFnIdx();
		boolean isAjax = attrVO.isAjax();
		String uploadModulePath = attrVO.getUploadModulePath();
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 2. DB
		int result = calendarService.cdelete(uploadModulePath, fnIdx, deleteIdxs, items, itemOrder);
    	
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
	 * 삭제목록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/deleteList.do")
	public String deleteList(HttpServletRequest request, ModelMap model,@ModuleAttr ModuleAttrVO attrVO) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		int fnIdx=attrVO.getFnIdx();
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
		
		// 2.1 검색조건
		
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, "list_search");
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.LAST_MODI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = calendarService.getDeleteCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = calendarService.getDeleteList(fnIdx, param);
    	}

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드

    	// 4. 휴지통 경로
    	fn_setDeleteListPath(attrVO);
    	
		return getViewPath("/deleteList");
	}
	
	/**
	 * 관리권한 체크
	 * @param regCd
	 * @return
	 */
	public boolean isMngAuth(String regiIdx) {

		// 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");				// 전체관리
		if(!isMngAuth/* && !isCdtAuth*/) {
			// 전체관리권한 없는 경우 : 자신글만 수정

			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
			String mbrCd = (loginVO != null)?loginVO.getMemberIdx():null;
			
			return (StringUtil.isEquals(mbrCd, regiIdx)); 
		}
		
		return true;
	}
	
	/**
	 * 관리권한 체크
	 * @param brdIdx
	 * @return
	 */
	public boolean isMngProcAuth(int brdIdx,@ModuleAttr ModuleAttrVO attrVO) {
		int fnIdx=attrVO.getFnIdx();
		int modiCnt = 0;
		// 권한 체크
		Boolean isMngAuth = AuthHelper.isAuthenticated("MNG");		// 전체관리
		if(!isMngAuth) {
			// 전체관리권한 없는 경우 : 자신글만 수정

			LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
			String mbrCd = (loginVO != null)?loginVO.getMemberIdx():null;

			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			
			searchList.add(new DTForm("A.BRD_IDX", brdIdx));
			searchList.add(new DTForm("A.REGI_IDX", mbrCd));
			param.put("searchList", searchList);
			
			modiCnt = calendarService.getAuthCount(fnIdx, param);
			
			return (modiCnt > 0);
		}
		
		return true;
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		String[] tabBaseParams = {"year", "month", "date"};
		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, idxName, pageName);
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
	

}
