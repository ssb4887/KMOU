package rbs.modules.poll.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DTOrderForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;

/**
 * 설문관리<br/>
 * @author user
 *
 */
@Controller
@RequestMapping({"/{siteId}/poll"})
@ModuleMapping(moduleId="poll")
public class UsrPollController extends PollResController {

	/**
	 * 목록조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		DataForm queryString = attrVO.getQueryString();
		JSONObject crtMenu = attrVO.getCrtMenu();
		boolean isAdmMode = attrVO.isAdmMode();
		
		int useOnePoll = JSONObjectUtil.getInt(settingInfo, "use_list_noreply");		// 진행설문 1개 사용
		int menuUsePrivateVal = JSONObjectUtil.getInt(crtMenu, "fn_use_private");		// 자신글 이력 보기
		if(useOnePoll != 1 || menuUsePrivateVal == 1) {
			// 목록, 자신글 이력 목록
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
			int totalCount = 0;
			List<?> list = null;
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			List<DTForm> searchList2 = respService.getRespSearchList(-1, "RES");
	
			if(menuUsePrivateVal == 1) {
				// 자신글 이력보기 : 참여 설문 목록, 임시저장 목록
				param.put("isPrivate", true);
			} else {
				// 설문 목록
				param.put("isPrivate", false);
			}
			
			// 2.1 검색조건
			// 항목설정으로 검색조건 setting
			String listSearchId = "list_search";		// 검색설정
			JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
			List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
			if(itemInfoSearchList != null) {
				searchList.addAll(itemInfoSearchList);
				model.addAttribute("isSearchList", new Boolean(true));
			}
			param.put("searchList", searchList);
			param.put("searchList2", searchList2);
				
			// 2.2 목록수
	    	totalCount = pollService.getTotalCount(fnIdx, param);
			paginationInfo.setTotalRecordCount(totalCount);
	    	
	    	if(totalCount > 0) {
	    		if(usePaging == 1) {
		    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
		    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
		    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    		}
	    		
	    		// 2.3 목록
				List<DTOrderForm> orderList = new ArrayList<DTOrderForm>();
				orderList.add(new DTOrderForm("A.LIMIT_DATE11", 1));
				orderList.add(new DTOrderForm("A.LIMIT_DATE12", 1));
				orderList.add(new DTOrderForm("A.LIMIT_DATE13", 1));
				
				param.put("orderList", orderList);
	    		list = pollService.getList(fnIdx, param);
	    	}
	    	
	    	// 3. 속성 setting
	    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
	    	model.addAttribute("list", list);															// 목록
	    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목 코드
		}

		int useListNew = (menuUsePrivateVal == 1)? 0:JSONObjectUtil.getInt(settingInfo, "use_notice");		// 진행설문 목록출력
    	if(useListNew == 1) {
    		// 진행설문 목록출력
    		int pollIdx = StringUtil.getInt(request.getParameter("lpIdx"));
    		DataMap dt = null;
    		List<Object> quesList = null;
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
    		
    		// 2.1 상세정보
    		if(pollIdx > 0) {
    			String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
    			searchList.add(new DTForm("A." + idxColumnName, pollIdx));
    		}
    		param.put("searchList", searchList);
			dt = pollService.getIngView(fnIdx, param);
			
    		if(pollIdx > 0 && dt == null) {
    			// 해당글이 없는 경우
    			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.poll.response.no.ing")));
    			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    		}
    		if(dt != null) {
    			pollIdx = StringUtil.getInt(dt.get("POLL_IDX"));

        		searchList = new ArrayList<DTForm>();
        		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
        		param.put("searchList", searchList);
        		quesList = quesService.getList(fnIdx, param);
        		model.addAttribute("quesList", quesList);
        		if(quesList != null) {
            		// 설문 참여여부
            		int pollRespIdx = isPollRespIdx(request, fnIdx, pollIdx);
            		model.addAttribute("isPollResp", (pollRespIdx) > 0);
            		
        			Map<String, List<Object>> inclassMap = pollService.getInclassMap(fnIdx, param);
	        		Map<String, List<Object>> inquesMap = pollService.getInquesMap(fnIdx, param);
	        		Map<String, List<Object>> itemMap = null;
	        		List<Object> itemList = null;
	        		int ispollitem = StringUtil.getInt(dt.get("ISPOLLITEM"));		// 공통항목 사용여부
	        		if(ispollitem == 1) itemList = pollService.getPItemList(fnIdx, param);
	        		else itemMap = pollService.getItemMap(fnIdx, param);
	        		
	        		int tmpRespIdx = 0;
            		if(pollRespIdx <= 0) {
            			// 설문 참여하지 안한 경우 : 임시저장 얻기
            			Map<String, Object> tmpParam = new HashMap<String, Object>();
            			List<DTForm> tmpSearchList = respService.getRespSearchList(pollIdx);
            			if(tmpSearchList != null) {
	            			tmpParam.put("searchList", tmpSearchList);
	            			tmpRespIdx = respService.getTmpRespIdx(fnIdx, tmpParam);

		            		searchList.add(new DTForm("A.RESP_IDX", tmpRespIdx));
		            		param.put("searchList", searchList);
	    	        		Map<Object, Object> resultMap = respService.getTmpResultMap(fnIdx, param);
	    	        		model.addAttribute("resultMap", resultMap);
            			}
            		}
	        		
	        		model.addAttribute("inclassMap", inclassMap);
	        		model.addAttribute("inquesMap", inquesMap);
	        		model.addAttribute("itemList", itemList);
	        		model.addAttribute("itemMap", itemMap);
	        		
	        		//int usePrivateVal = ModuleUtil.getSettingValue(isAdmMode, "use_private", crtMenu, settingInfo, 0);
	        		int usePrivateVal = JSONObjectUtil.getInt(settingInfo, "use_private");
	        		model.addAttribute("usePrivate", usePrivateVal == 1);
        		}
    		}
    		
    		model.addAttribute("ingDt", dt);
    		model.addAttribute("ISQUESTYPE", (dt != null && StringUtil.getInt(dt.get("ISQUESTYPE")) == 1)?true:false);
    		model.addAttribute("ISPOLLITEM", (dt != null && StringUtil.getInt(dt.get("ISPOLLITEM")) == 1)?true:false);
    	}
    	
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	if(useListNew == 1) {
    		// 진행설문 목록출력
	    	// 6. form action
	    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	}
    	
    	/*String viewPath = null;
    	if(menuUsePrivateVal == 1) viewPath = "/myList";
    	else viewPath = "/list";*/
		return getViewPath("/list");
	}

	/**
	 * 의견보기
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/contList.do")
	public String contList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		boolean isAdmMode = attrVO.isAdmMode();
		int usePrivateVal = ModuleUtil.getSettingValue(isAdmMode, "use_private", crtMenu, settingInfo, 0);
		if(usePrivateVal == 1) {
			// 자신글 이력보기 || 자신글 결과보기 인 경우
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		String viewPath = super.contList(attrVO, request, model);
    	// 5. 기본경로
    	fn_setContListPath(attrVO);
		return viewPath;
	}

	/**
	 * 상세조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/view.do")
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		boolean isAdmMode = attrVO.isAdmMode();
		int usePrivateVal = ModuleUtil.getSettingValue(isAdmMode, "use_private", crtMenu, settingInfo, 0);
		int useReply = JSONObjectUtil.getInt(settingInfo, "use_reply");
		
		String viewPath = super.view(usePrivateVal, useReply, attrVO, request, model);
    	// 5. 기본경로
    	fn_setCommonPath(attrVO);
		return viewPath;
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="RWT")
	@RequestMapping(value = "/input.do")
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		boolean isAdmMode = attrVO.isAdmMode();
		int fnIdx = attrVO.getFnIdx();
		
		int menuUsePrivateVal = JSONObjectUtil.getInt(crtMenu, "fn_use_private");	// 자신글 이력 보기
		if(menuUsePrivateVal == 1) {
			// 자신글 이력보기
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		
		// 1. 필수 parameter 검사
		int pollIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(pollIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		

		DataMap dt = null;
		List<Object> quesList = null;
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		
		// 2.1 상세정보
		searchList.add(new DTForm("A." + idxColumnName, pollIdx));
		param.put("searchList", searchList);
		dt = pollService.getIngView(fnIdx, param);

		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.poll.response.no.ing")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		pollIdx = StringUtil.getInt(dt.get("POLL_IDX"));

		quesList = quesService.getList(fnIdx, param);
		model.addAttribute("quesList", quesList);
		if(quesList != null) {
    		// 설문 참여여부
    		int pollRespIdx = isPollRespIdx(request, fnIdx, pollIdx);
    		model.addAttribute("isPollResp", (pollRespIdx) > 0);
    		
			Map<String, List<Object>> inclassMap = pollService.getInclassMap(fnIdx, param);
    		Map<String, List<Object>> inquesMap = pollService.getInquesMap(fnIdx, param);
    		Map<String, List<Object>> itemMap = null;
    		List<Object> itemList = null;
    		int ispollitem = StringUtil.getInt(dt.get("ISPOLLITEM"));		// 공통항목 사용여부
    		if(ispollitem == 1) itemList = pollService.getPItemList(fnIdx, param);
    		else itemMap = pollService.getItemMap(fnIdx, param);
    		
    		int tmpRespIdx = 0;
    		if(pollRespIdx <= 0) {
    			// 설문 참여하지 안한 경우 : 임시저장 얻기
    			Map<String, Object> tmpParam = new HashMap<String, Object>();
    			List<DTForm> tmpSearchList = respService.getRespSearchList(pollIdx);
    			if(tmpSearchList != null) {
        			tmpParam.put("searchList", tmpSearchList);
        			tmpRespIdx = respService.getTmpRespIdx(fnIdx, tmpParam);

            		searchList.add(new DTForm("A.RESP_IDX", tmpRespIdx));
            		param.put("searchList", searchList);
	        		Map<Object, Object> resultMap = respService.getTmpResultMap(fnIdx, param);
	        		model.addAttribute("resultMap", resultMap);
    			}
    		}
    		
    		model.addAttribute("inclassMap", inclassMap);
    		model.addAttribute("inquesMap", inquesMap);
    		model.addAttribute("itemList", itemList);
    		model.addAttribute("itemMap", itemMap);
    		
    		int usePrivateVal = ModuleUtil.getSettingValue(isAdmMode, "use_private", crtMenu, settingInfo, 0);
    		model.addAttribute("usePrivate", usePrivateVal == 1);
		}
		
		model.addAttribute("ingDt", dt);
		model.addAttribute("ISQUESTYPE", (StringUtil.getInt(dt.get("ISQUESTYPE")) == 1)?true:false);
		model.addAttribute("ISPOLLITEM", (StringUtil.getInt(dt.get("ISPOLLITEM")) == 1)?true:false);
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 2. 속성 setting
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분

    	// 3. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 4. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
		return getViewPath("/response");
	}

	/**
	 * 설문참여처리
	 * @param parameterMap
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="RWT")
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		int fnIdx = attrVO.getFnIdx();
		JSONObject crtMenu = attrVO.getCrtMenu();
		
		int menuUsePrivateVal = JSONObjectUtil.getInt(crtMenu, "fn_use_private");	// 자신글 이력 보기
		if(menuUsePrivateVal == 1) {
			// 자신글 이력보기
			return RbsProperties.getProperty("Globals.error.404" + ajaxPName + ".path");
		}
		
		// 1. 필수 parameter 검사
		int pollIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(pollIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. 진행여부 확인
		int ingCount = pollService.getIngCount(fnIdx, null);
		if(ingCount <= 0) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.poll.response.no.ing")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. 참여 중복확인
		int isPollRespIdx = isPollRespIdx(request, fnIdx, pollIdx);
		if(isPollRespIdx > 0) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.poll.duplicate")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. DB
    	int result = respService.insert(fnIdx, pollIdx, request.getRemoteAddr(), parameterMap);
    	if(result > 0) {
    		// 저장 성공
        	// 4. 기본경로
        	fn_setCommonPath(attrVO);
        	int useListNew = JSONObjectUtil.getInt(settingInfo, "use_notice");	// 진행설문 목록출력
        	String addParam = null;
        	if(useListNew == 1) addParam = "&lpIdx=" + pollIdx;
        	else addParam = "";
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + request.getAttribute("URL_LIST") + addParam + "\");"));
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
	 * 기본경로
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
		fn_setAddPath(request);
	}
	
	/**
	 * 추가 경로
	 * @param request
	 */
	public void fn_setAddPath(HttpServletRequest request) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		String rescontListName = "contList.do";		//의견보기
		String rescontListUrl = rescontListName;
		if(!StringUtil.isEmpty(baseQueryString)) {
			rescontListUrl += baseQueryString;
		}

		request.setAttribute("URL_RESCONT_LIST", rescontListUrl);
	}
	
	/**
	 * 의견목록 경로
	 * @param attrVO
	 */
	public void fn_setContListPath(ModuleAttrVO attrVO) {	
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "cont_setting_info");
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key

		String listName = "contList.do";
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{"pollIdx", "quesIdx", "itemIdx"});													// 기본 parameter
		PathUtil.fn_setListPath(queryString, baseParams, null, pageName, listName);
		
	}
}
