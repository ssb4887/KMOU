package rbs.modules.member.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rbs.modules.member.service.MemberService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleSearchController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
//@ModuleMapping(moduleId="member", confModule="member")
@ModuleMapping(moduleId="member", useSDesign=true)
@RequestMapping("/{siteId}/member")
public class MemberSearchController extends ModuleSearchController {

	@Resource(name = "memberService")
	private MemberService memberService;
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	private EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	private RbsMessageSource rbsMessageSource;

	/**
	 * 아이디 중복확인
	 * @param mbrId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(accessModule={"member", "memberAn"})
	@RequestMapping(method=RequestMethod.POST, value = "/idConfirmProc.do")
	public String idConfirmProc(@RequestParam(value="mbrId") String mbrId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 2. DB
		int duplicate = 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 2.1 검색조건
		// 아이디
		int useRSA = JSONObjectUtil.getInt(settingInfo, "use_rsa");	// 설정정보의 rsa사용여부
		JSONObject mbrIdItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "items"), "mbrId");	// mbrId 항목 설정 정보
		String dbMbrId = ModuleUtil.getParamToDBValue(useRSA, mbrIdItem, mbrId);	// RSA 및 암호화 설정에 따른 값 얻기

		searchList.add(new DTForm("A.MEMBER_ID", dbMbrId));
		param.put("searchList", searchList);

		// 2.2 목록수
		duplicate = memberService.getDuplicateCount(param);
    	if(duplicate == 0) {
    		// 중복된 아이디 없음
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, null, "fn_checkObj.success();"));
    	} else {
			// 중복된 아이디 존재
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, null, "fn_checkObj.fail(1);"));
    	}
		return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
	}

	/**
	 * 이메일 중복확인
	 * @param mbrId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(accessModule={"member", "memberAn"})
	@RequestMapping(method=RequestMethod.POST, value = "/emailConfirmProc.do")
	public String emailConfirmProc(@RequestParam(value="mbrEmail") String mbrEmail, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// 2. DB
		int duplicate = 0;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		// 2.1 검색조건
		// 아이디
		int useRSA = JSONObjectUtil.getInt(settingInfo, "use_rsa");	// 설정정보의 rsa사용여부
		JSONObject mbrEmailItem = JSONObjectUtil.getJSONObject(JSONObjectUtil.getJSONObject(itemInfo, "items"), "mbrEmail");	// mbrEmail 항목 설정 정보
		String dbMbrEmail = ModuleUtil.getParamToDBValue(useRSA, mbrEmailItem, mbrEmail);	// RSA 및 암호화 설정에 따른 값 얻기

		String mbrIdx = StringUtil.getString(request.getParameter("mbrIdx"));

		if(!StringUtil.isEmpty(mbrIdx)) searchList.add(new DTForm("A.MEMBER_IDX", mbrIdx, "<>"));
		searchList.add(new DTForm("A.MEMBER_EMAIL", dbMbrEmail));
		param.put("searchList", searchList);

		// 2.2 목록수
		duplicate = memberService.getDuplicateCount(param);
    	if(duplicate == 0) {
    		// 중복된 이메일 없음
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, null, "fn_checkObj.success();"));
    	} else {
			// 중복된 이메일 존재
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, null, "fn_checkObj.fail(1);"));
    	}
		return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
	}

	/**
	 * 회원검색
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG", accessModule={"menu", "moduleAuth"})
	@RequestMapping(value = "/totSearchList.do")
	public String totSearchList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		// 1. 페이지정보 setting
		int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
		int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
		int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
		
		int pageUnit = 20;
		int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		int page = StringUtil.getInt(request.getParameter(pageName), 1);				// 현재 페이지 index

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
		String listSearchId = "totSearchList_search";		// 검색설정
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
    	totalCount = memberService.getTotTotalCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = memberService.getTotSearchList(param);
    	}
    	
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
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
    	model.addAttribute("itemListSearchId", listSearchId);
    	
    	// 기본경로
    	fn_setTotSearchPath(attrVO);
		return getViewPath("/searchList");
	}
	
	/**
	 * 경로
	 */
	public void fn_setTotSearchPath(ModuleAttrVO attrVO) {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");															// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "totSearchList_search"));					// 목록 검색 항목

		String listName = "totSearchList.do";
		//String[] baseParams = StringUtil.addStringToArray(this.baseParams, "itemId");													// 기본 parameter
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, listName);
	}
}
