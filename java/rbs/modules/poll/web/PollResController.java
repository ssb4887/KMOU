package rbs.modules.poll.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;

import rbs.modules.poll.service.PollService;
import rbs.modules.poll.service.QuesService;
import rbs.modules.poll.service.RespService;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 설문관리<br/>
 * @author user
 *
 */
public class PollResController extends ModuleController {

	@Resource(name = "pollService")
	protected PollService pollService;
	
	@Resource(name = "pollQuesService")
	protected QuesService quesService;
	
	@Resource(name = "pollRespService")
	protected RespService respService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	protected RbsMessageSource rbsMessageSource;

	
	/**
	 * 설문참여여부
	 * @param request
	 * @param fnIdx
	 * @param pollIdx
	 * @return
	 */
	protected int isPollRespIdx(HttpServletRequest request, int fnIdx, int pollIdx) {

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = respService.getRespSearchList(pollIdx);
		if(searchList == null) return 0;
		param.put("searchList", searchList);
		return respService.getRespIdx(fnIdx, param);
	}
	
	/**
	 * 의견목록
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String contList(ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject moduleSetting = attrVO.getModuleSetting();
		int fnIdx = attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "cont_setting_info");
		
		// 1. 필수 parameter 검사
		int pollIdx = StringUtil.getInt(request.getParameter("pollIdx"));
		int quesIdx = StringUtil.getInt(request.getParameter("quesIdx"));
		int itemIdx = StringUtil.getInt(request.getParameter("itemIdx"));
		if(pollIdx <= 0 || quesIdx <= 0 || itemIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		searchList.add(new DTForm("A.QUESTION_IDX", quesIdx));
		searchList.add(new DTForm("A.ITEM_IDX", itemIdx));
		param.put("searchList", searchList);
		
		if(!isAdmMode) {
			// 결과보기 설정된 설문만 가능
			searchList.add(new DTForm("POLL.ISRESULT", "1"));
		}
		param.put("searchList", searchList);
		
		// 설문, 문항, 항목 상세정보
		dt = quesService.getItemContView(fnIdx, param);
		if(dt == null) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
			
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", 0, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", 0, propertiesService.getInt("PAGE_SIZE"));	
			
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
		searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.POLL_IDX", pollIdx));
		searchList.add(new DTForm("A.QUESTION_IDX", quesIdx));
		searchList.add(new DTForm("A.ITEM_IDX", itemIdx));
		param.put("searchList", searchList);
		// 2.2 목록수
    	totalCount = respService.getResultTotalCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		}
    		
    		// 2.3 목록
    		list = respService.getResultList(fnIdx, param);
    	}
    	
    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("dt", dt);																// 항목 상세 정보
    	
    	return getViewPath("/contList");
	}
	
	/**
	 * 상세조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String view(int usePrivateVal, int useReply, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		boolean isAdmMode = attrVO.isAdmMode();
		
		// 1. 필수 parameter 검사
		int pollIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(pollIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		
		String viewPath = null;
		DataMap dt = null;
		List<Object> quesList = null;
		Map<String, List<Object>> inclassMap = null;
		Map<String, List<Object>> inquesMap = null;
		Map<String, List<Object>> itemMap = null;
		List<Object> itemList = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A." + idxColumnName, pollIdx));
		if(!isAdmMode) {
			// 결과보기 설정된 설문만 가능
			searchList.add(new DTForm("A.ISRESULT", "1"));
		}
		param.put("searchList", searchList);
		
		if(usePrivateVal == 1) {
			// 개인정보만 출력
			
    		int pollRespIdx = (isAdmMode)?StringUtil.getInt(request.getParameter("respIdx")) : isPollRespIdx(request, fnIdx, pollIdx);
    		if(pollRespIdx <= 0) {
    			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.poll.no.response")));
    			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    		}
    		searchList.add(new DTForm("RES.RESP_IDX", pollRespIdx));
    		param.put("searchList", searchList);
    		
    		// 2.1 상세정보
    		dt = respService.getPollView(fnIdx, param);
			//dt = pollService.getView(fnIdx, param);
			if(dt == null) {
				if(isAdmMode){
					model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.poll.no.contents")));
					return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
				} else {
					return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
				}
			}

    		int ispollitem = StringUtil.getInt(dt.get("ISPOLLITEM"));		// 공통항목 사용여부
			if(useReply != 1) {
				// 답변 사용하지 않는 경우 : 응답 정보 display
	    		searchList = new ArrayList<DTForm>();
	    		searchList.add(new DTForm("A." + idxColumnName, pollIdx));
	    		param.put("searchList", searchList);
	    		
	    		quesList = quesService.getList(fnIdx, param);
	    		model.addAttribute("quesList", quesList);
	    		if(quesList != null) {
	    			inclassMap = pollService.getInclassMap(fnIdx, param);
	        		inquesMap = pollService.getInquesMap(fnIdx, param);
	        		
	        		if(ispollitem == 1) itemList = pollService.getPItemList(fnIdx, param);
	        		else itemMap = pollService.getItemMap(fnIdx, param);
	        		
	        		searchList.add(new DTForm("A.RESP_IDX", pollRespIdx));
	        		param.put("searchList", searchList);
	        		Map<Object, Object> resultMap = respService.getResultMap(fnIdx, param);
	        		
	        		model.addAttribute("inclassMap", inclassMap);
	        		model.addAttribute("inquesMap", inquesMap);
	        		model.addAttribute("itemMap", itemMap);
	        		model.addAttribute("itemList", itemList);
	        		model.addAttribute("resultMap", resultMap);
	    		}
			}
    		
    		int isQuestype = StringUtil.getInt(dt.get("ISQUESTYPE"));
    		if(/*useReply == 1 &&*/ isQuestype == 1) {
        		// 결과유형 사용하는 경우
	    		searchList = new ArrayList<DTForm>();
	    		searchList.add(new DTForm("A." + idxColumnName, pollIdx));
        		searchList.add(new DTForm("A.RESP_IDX", pollRespIdx));
	    		param.put("searchList", searchList);
	    		
        		List<Object> questypeResultList = (ispollitem == 1)?respService.getQuestypePResultList(fnIdx, param):respService.getQuestypeResultList(fnIdx, param);
        		model.addAttribute("questypeResultList", questypeResultList);
    		}
    		viewPath = "/respView";
		} else {
			// 전체 결과
			
			// 2.1 상세정보
			dt = pollService.getRespCntView(fnIdx, param);
			if(dt == null) {
				return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
			}
			
    		int ispollitem = StringUtil.getInt(dt.get("ISPOLLITEM"));		// 공통항목 사용여부
			
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A." + idxColumnName, pollIdx));
			param.put("searchList", searchList);
			
			quesList = quesService.getRespCntList(fnIdx, param);
			if(quesList == null || quesList.size() == 0) {
				if(isAdmMode){
					model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.poll.question.no.list")));
					return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
				} else {
					return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
				}
			}
    		model.addAttribute("quesList", quesList);
    		if(quesList != null) {
    			inclassMap = pollService.getInclassMap(fnIdx, param);
        		inquesMap = pollService.getInquesMap(fnIdx, param);
        		
        		if(ispollitem == 1) itemMap = pollService.getRespCntPItemMap(fnIdx, param);
        		else itemMap = pollService.getRespCntItemMap(fnIdx, param);
        		
        		model.addAttribute("inclassMap", inclassMap);
        		model.addAttribute("inquesMap", inquesMap);
        		model.addAttribute("itemMap", itemMap);
    		}
    		viewPath = "/view";
		}

		// 3. 항목설정
		String submitType = "view";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 4. 속성 setting
		model.addAttribute("dt", dt);																// 상세정보
		model.addAttribute("ISQUESTYPE", (dt != null && StringUtil.getInt(dt.get("ISQUESTYPE")) == 1)?true:false);
		model.addAttribute("ISPOLLITEM", (dt != null && StringUtil.getInt(dt.get("ISPOLLITEM")) == 1)?true:false);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(dt, items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);												// 페이지구분
		
		return getViewPath(viewPath);
	}
}
