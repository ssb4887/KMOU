package rbs.modules.board.web;

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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rbs.egovframework.Defines;
import rbs.egovframework.LoginVO;
import rbs.egovframework.util.PrivAuthUtil;
import rbs.modules.board.service.BoardService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DTOrderForm;
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
import com.woowonsoft.egovframework.util.DataSecurityUtil;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 다기능게시판<br/>
 * : 통합관리시스템 > 메뉴콘텐츠관리, 통합관리시스템 > 기능등록관리, 사용자 사이트 에서 사용
 * @author user
 *
 */
@Controller
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/board", "/{admSiteId}/moduleFn/board", "/{siteId}/board"})
@ModuleMapping(moduleId="board")
public class BoardController extends ModuleController {
	
	@Resource(name = "boardService")
	private BoardService boardService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

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
		HttpSession session = request.getSession();

		DataForm queryString = attrVO.getQueryString();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		int loginUsertypeIdx = 0;
		String loginMemberIdx = null;
		if(loginVO != null){
			loginUsertypeIdx = loginVO.getUsertypeIdx();
			loginMemberIdx = loginVO.getMemberIdx();
		}
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();

		// 1. 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		int fnIdx = attrVO.getFnIdx();
		int dsetListNewcnt = JSONObjectUtil.getInt(settingInfo, "dset_list_newcnt");
		
		boolean isAdmMode = attrVO.isAdmMode();
		boolean useNotice = JSONObjectUtil.isEquals(settingInfo, "use_notice", "1");
		boolean useDldate = JSONObjectUtil.isEquals(settingInfo, "use_dldate", "1");
		int usePrivateVal = ModuleUtil.getSettingValue(isAdmMode, "use_private", crtMenu, settingInfo, 0);
		boolean usePrivate = (usePrivateVal == 1);
		//boolean usePrivate = JSONObjectUtil.isEquals(settingInfo, "use_private", "1");
		boolean useListImg = JSONObjectUtil.isEquals(settingInfo, "use_list_img", "1");
		boolean useSecret = JSONObjectUtil.isEquals(settingInfo, "use_secret", "1");
		boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
		boolean useRss = JSONObjectUtil.isEquals(settingInfo, "use_rss", "1");
		boolean isRss = StringUtil.isEquals(request.getParameter("rss"), "2.0");
		boolean isNmPage = loginUsertypeIdx == RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");

		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		String dsetCateListId = JSONObjectUtil.getString(settingInfo, "dset_cate_list_id");
		String dsetCateColumnId = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, dsetCateListId), "column_id");
		String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
		
		//HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);

		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		
		// 탭코드
		String cateTabItemId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");
		String cateTabColumnId = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "column_id");
		String cateTabMasterCode = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "master_code");
		int cateTabObjectType = JSONObjectUtil.getInt(settingInfo, "dset_cate_tab_object_type");
		// 검색 코드
		HashMap<String, Object> searchOptnHashMap = CodeHelper.getItemOptnSearchHashMap(listSearch, cateTabItemId, cateTabColumnId, cateTabObjectType, cateTabMasterCode);
		// 목록 코드
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder, searchOptnHashMap);
				
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
			if(useRss && isRss) page = 1;
	
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
		
		// 탭 검색 조건
		List<DTForm> cateTabSearchList = ModuleUtil.getCateTabSearchList(settingInfo, items, queryString, optnHashMap);//fn_getCateTabSearchList(queryString, settingInfo, items, optnHashMap);
		if(cateTabSearchList != null&& (!useRss || (useRss && !isRss))){
			searchList.addAll(cateTabSearchList);
		}
		
		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null && (!useRss || (useRss && !isRss))) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
			useNotice = false;
		}
		
		String searchType = request.getParameter("searchType");
		String searchValue = request.getParameter("searchValue");
		
		if(searchValue != null && !"".equals(searchValue)) {
			if(StringUtil.isEquals(searchType, "sj")  ) { //제목
				param.put("field", "sj");
			} else if(StringUtil.isEquals(searchType, "all")  ) { //제목 + 내용
				param.put("field", "all");
			} else if(StringUtil.isEquals(searchType, "cn")  ) { // 내용
				param.put("field", "cn");
			} else if(StringUtil.isEquals(searchType, "ncnm")  ) { //작성자
				param.put("field", "ncnm");
			}
			
			param.put("searchValue", "%" + searchValue + "%");
		}
		
		param.put("searchList", searchList);
		param.put("boardDesignType", boardDesignType);
		param.put("isAdmMode", isAdmMode);
		param.put("useNotice", useNotice);
		param.put("useDldate", useDldate);
		param.put("usePrivate", usePrivate);
		param.put("useListImg", useListImg);
		param.put("useSecret", useSecret);
		param.put("useQna", useQna);
		param.put("dsetCateColumnId", dsetCateColumnId);
		param.put("memberIdx", loginMemberIdx);
		if(isNmPage) param.put("memberDup", sDupInfo);
		param.put("isNmPage", isNmPage);
		
		// 답글사용, 비밀글 사용하는 경우 - 비밀번호 인증창 사용하기위한 부모글정보 얻기
		boolean useReply = JSONObjectUtil.isEquals(settingInfo, "use_reply", "1");
		boolean isNoMemberWRT = AuthHelper.isNoMemberAuthPage("WRT");
		boolean isNoMemberRWT = AuthHelper.isNoMemberAuthPage("RWT");
		if(useReply && useSecret && (isNoMemberWRT || isNoMemberRWT)) param.put("usePntInfo", true);
		
		// 2.2 목록수
		totalCount = boardService.getTotalCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);

    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    	}
    		
    		param.put("dsetOrderField", JSONObjectUtil.getString(settingInfo, "dset_order_field"));
    		
    		// 2.3 목록
    		list = boardService.getList(fnIdx, param);
		}

		if(dsetListNewcnt > 0){
			List<?> newList = fn_getViewList(attrVO, queryString, settingInfo, itemInfo, items, 4, null, null);
			model.addAttribute("newList", newList);
		}
		
    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", searchOptnHashMap);									// 항목코드
    	//model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목코드
		
		// 2. 속성 setting
		model.addAttribute("optnHashMap", optnHashMap);
		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	if(StringUtil.isEquals(boardDesignType, RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO")))
        	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
    	if(useRss && isRss) return getViewPath("/rss");
		return getViewPath("/list");
	}
	
	/**
	 * 공지사항 목록조회
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/noticelist.do")
	public String noticelist(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		HttpSession session = request.getSession();

		DataForm queryString = attrVO.getQueryString();
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		int loginUsertypeIdx = 0;
		String loginMemberIdx = null;
		if(loginVO != null){
			loginUsertypeIdx = loginVO.getUsertypeIdx();
			loginMemberIdx = loginVO.getMemberIdx();
		}
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();

		// 1. 항목설정
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		int fnIdx = attrVO.getFnIdx();
		int dsetListNewcnt = JSONObjectUtil.getInt(settingInfo, "dset_list_newcnt");
		
		boolean isAdmMode = attrVO.isAdmMode();
		boolean useNotice = JSONObjectUtil.isEquals(settingInfo, "use_notice", "1");
		boolean useDldate = JSONObjectUtil.isEquals(settingInfo, "use_dldate", "1");
		int usePrivateVal = ModuleUtil.getSettingValue(isAdmMode, "use_private", crtMenu, settingInfo, 0);
		boolean usePrivate = (usePrivateVal == 1);
		//boolean usePrivate = JSONObjectUtil.isEquals(settingInfo, "use_private", "1");
		boolean useListImg = JSONObjectUtil.isEquals(settingInfo, "use_list_img", "1");
		boolean useSecret = JSONObjectUtil.isEquals(settingInfo, "use_secret", "1");
		boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
		boolean useRss = JSONObjectUtil.isEquals(settingInfo, "use_rss", "1");
		boolean isRss = StringUtil.isEquals(request.getParameter("rss"), "2.0");
		boolean isNmPage = loginUsertypeIdx == RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");

		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		String dsetCateListId = JSONObjectUtil.getString(settingInfo, "dset_cate_list_id");
		String dsetCateColumnId = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, dsetCateListId), "column_id");
		String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
		
		//HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);

		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, listSearchId);
		
		// 탭코드
		String cateTabItemId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");
		String cateTabColumnId = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "column_id");
		String cateTabMasterCode = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, cateTabItemId), "master_code");
		int cateTabObjectType = JSONObjectUtil.getInt(settingInfo, "dset_cate_tab_object_type");
		// 검색 코드
		HashMap<String, Object> searchOptnHashMap = CodeHelper.getItemOptnSearchHashMap(listSearch, cateTabItemId, cateTabColumnId, cateTabObjectType, cateTabMasterCode);
		// 목록 코드
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder, searchOptnHashMap);
				
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
			if(useRss && isRss) page = 1;
	
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
		
		// 탭 검색 조건
		List<DTForm> cateTabSearchList = ModuleUtil.getCateTabSearchList(settingInfo, items, queryString, optnHashMap);//fn_getCateTabSearchList(queryString, settingInfo, items, optnHashMap);
		if(cateTabSearchList != null&& (!useRss || (useRss && !isRss))){
			searchList.addAll(cateTabSearchList);
		}
		
		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(itemInfoSearchList != null && (!useRss || (useRss && !isRss))) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
			useNotice = false;
		}
		
		param.put("searchList", searchList);
		param.put("boardDesignType", boardDesignType);
		param.put("isAdmMode", isAdmMode);
		param.put("useNotice", useNotice);
		param.put("useDldate", useDldate);
		param.put("usePrivate", usePrivate);
		param.put("useListImg", useListImg);
		param.put("useSecret", useSecret);
		param.put("useQna", useQna);
		param.put("dsetCateColumnId", dsetCateColumnId);
		param.put("memberIdx", loginMemberIdx);
		if(isNmPage) param.put("memberDup", sDupInfo);
		param.put("isNmPage", isNmPage);
		
		// 답글사용, 비밀글 사용하는 경우 - 비밀번호 인증창 사용하기위한 부모글정보 얻기
		boolean useReply = JSONObjectUtil.isEquals(settingInfo, "use_reply", "1");
		boolean isNoMemberWRT = AuthHelper.isNoMemberAuthPage("WRT");
		boolean isNoMemberRWT = AuthHelper.isNoMemberAuthPage("RWT");
		if(useReply && useSecret && (isNoMemberWRT || isNoMemberRWT)) param.put("usePntInfo", true);
		
		// 2.2 목록수
    	totalCount = boardService.getTotalCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);

    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    	}
    		
    		param.put("dsetOrderField", JSONObjectUtil.getString(settingInfo, "dset_order_field"));
    		
    		// 2.3 목록
    		list = boardService.getList(fnIdx, param);
		}

		if(dsetListNewcnt > 0){
			List<?> newList = fn_getViewList(attrVO, queryString, settingInfo, itemInfo, items, 4, null, null);
			model.addAttribute("newList", newList);
		}
		
    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", searchOptnHashMap);									// 항목코드
    	//model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목코드
		
		// 2. 속성 setting
		model.addAttribute("optnHashMap", optnHashMap);
		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	if(StringUtil.isEquals(boardDesignType, RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO")))
        	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_INPUTPROC"));
    	
    	if(useRss && isRss) return getViewPath("/rss");
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
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		HttpSession session = request.getSession(true);
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		int loginUsertypeIdx = 0;
		String loginMemberIdx = null;
		if(loginVO != null){
			loginUsertypeIdx = loginVO.getUsertypeIdx();
			loginMemberIdx = loginVO.getMemberIdx();
		}

		DataForm queryString = attrVO.getQueryString();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();

		// 3. 항목설정
		String submitType = "view";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		int fnIdx = attrVO.getFnIdx();
		int dsetViewPreCnt = JSONObjectUtil.getInt(settingInfo, "dset_view_precnt");
		int dsetViewNextCnt = JSONObjectUtil.getInt(settingInfo, "dset_view_nextcnt");

		boolean isAjax = attrVO.isAjax();
		boolean isAdmMode = attrVO.isAdmMode();
		boolean useDldate = JSONObjectUtil.isEquals(settingInfo, "use_dldate", "1");
		int usePrivateVal = ModuleUtil.getSettingValue(isAdmMode, "use_private", crtMenu, settingInfo, 0);
		boolean usePrivate = (usePrivateVal == 1);
		boolean useReply = JSONObjectUtil.isEquals(settingInfo, "use_reply", "1");
		boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
		boolean useSecret = JSONObjectUtil.isEquals(settingInfo, "use_secret", "1");
		boolean isNmPage = loginUsertypeIdx == RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");

		String ajaxPName = attrVO.getAjaxPName();
		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		String dsetCateListId = JSONObjectUtil.getString(settingInfo, "dset_cate_list_id");
		String dsetCateColumnId = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, dsetCateListId), "column_id");
		String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");
		
		// 1. 필수 parameter 검사
		int brdIdx = 0;
		if(fnIdx == 1 || fnIdx == 5) {
			brdIdx = StringUtil.getInt(request.getParameter("NTT_SN"), 0);
			if(brdIdx <= 0) {
				return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
			}
		} else {
			brdIdx = StringUtil.getInt(request.getParameter(idxName), 0);
			if(brdIdx <= 0) {
				return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
			}
		}
		
		
		// 2. DB
		DataMap dt = null;
		List<?> list = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A." + idxColumnName, brdIdx));
		if(fnIdx == 1 || fnIdx == 5) {
			param.put("nttSn",brdIdx);
		}
		param.put("searchList", searchList);
		param.put("boardDesignType", boardDesignType);
		param.put("isAdmMode", isAdmMode);
		param.put("useDldate", useDldate);
		param.put("usePrivate", usePrivate);
		param.put("useQna", useQna);
		param.put("dsetCateColumnId", dsetCateColumnId);
		param.put("memberIdx", loginMemberIdx);
		if(isNmPage) param.put("memberDup", sDupInfo);
		param.put("isNmPage", isNmPage);

		// 2.1 상세정보
		dt = boardService.getView(fnIdx, param);
		if(fnIdx == 1 || fnIdx == 5) {
			list = boardService.getBnAList(fnIdx, param);
			int len = list.size();
			model.addAttribute("len", len);
		}
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		String pntIdxColumnName = "PNT_IDX";
		if(useSecret){
			if(StringUtil.getInt(dt.get("SECRET")) == 1){
				// 상세보기 페이지 비밀글 접근 권한 체크 (전체관리/완전삭제 권한 포함)
				int pntIdx = StringUtil.getInt(dt.get(pntIdxColumnName));
				int reLevel = StringUtil.getInt(dt.get("RE_LEVEL"));
				String regiIdx = StringUtil.getString(dt.get("REGI_IDX"));
				String regiDupInfo = StringUtil.getString(dt.get("MEMBER_DUP"));
				String regiPwd = StringUtil.getString(dt.get("PWD"));
				Boolean isMngAuth = BoardAuthUtil.isViewSecretAuth(fnIdx, brdIdx, pntIdx, (useQna || useReply) && (reLevel > 1), regiIdx, regiDupInfo, regiPwd);
				
				if(!isMngAuth) {
					boolean isAuthNoMember = (RbsProperties.getPropertyInt("Globals.auth.no.member") == 1); 
					boolean isWrtNmPage = false; 
					if(StringUtil.getInt(dt.get("RE_LEVEL")) > 1) isWrtNmPage = isAuthNoMember ?  (AuthHelper.isNoMemberAuthPage("WRT") && AuthHelper.isNmAuthPage("RWT")) ? AuthHelper.isNmAuthPage("RWT") : AuthHelper.isNmAuthPage("WRT") : AuthHelper.getModuleUTP("RWT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					else isWrtNmPage = isAuthNoMember ? AuthHelper.isNmAuthPage("WRT") : AuthHelper.getModuleUTP("WRT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
					
					// 본인인증회원 글쓰기일 경우 실명인증 여부
					if(PrivAuthUtil.isUseNameAuth() && (isWrtNmPage || AuthHelper.isNoMemberAuthPage("WRT") || AuthHelper.isNoMemberAuthPage("RWT")) && !UserDetailsHelper.isLogin() && !PrivAuthUtil.isNameAuthCheck(true)){
						model.addAttribute("nmAuthURL", MenuUtil.getMenuUrl(Defines.CODE_MENU_NMEMBER_AUTH));
						model.addAttribute("message", MessageUtil.getConfirm(attrVO.isAjax(), rbsMessageSource.getMessage("message.no.auth.confirm"), null));
						return RbsProperties.getProperty("Globals.error.401.login" + attrVO.getAjaxPName() + ".path");
					}
					
					// 자신이 등록한 글이 아닌 경우
					model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
					return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");	
				}

			}
		}

		if(queryString.isEmpty("dataNm")){
			if(useQna || useReply){
				Map<String, Object> pntParam = new HashMap<String, Object>();
				List<DTForm> pntSearchList = new ArrayList<DTForm>();
	
				pntSearchList.add(new DTForm("A." + pntIdxColumnName, StringUtil.getInt(dt.get(pntIdxColumnName))));
	
				List<?> pntList = fn_getViewList(attrVO, queryString, settingInfo, itemInfo, items, 1, pntParam, pntSearchList);
	
				HashMap<String, Object> pntMultiFileHashMap = new HashMap<String, Object>();
				HashMap<String, Object> pntMultiDataHashMap = new HashMap<String, Object>();
				if(pntList != null){
					for(int i = 0; i < pntList.size(); i++){
						DataMap pntDt = (DataMap)pntList.get(i);
						int pntBrdIdx = StringUtil.getInt(pntDt.get(idxColumnName));
						String pntBrdIdxStr = StringUtil.toString(pntBrdIdx);
						pntMultiFileHashMap.put(pntBrdIdxStr, boardService.getMultiFileHashMap(fnIdx, pntBrdIdx, items, itemOrder));
						pntMultiDataHashMap.put(pntBrdIdxStr, boardService.getMultiHashMap(fnIdx, pntBrdIdx, items, itemOrder));
					}
				}
	
				model.addAttribute("pntMultiFileHashMap", pntMultiFileHashMap);	// multi file 목록
				model.addAttribute("pntMultiDataHashMap", pntMultiDataHashMap);	// multi data 목록
				JSONArray replyItemOrder = JSONObjectUtil.getJSONArray(itemInfo, "reply_order");
				model.addAttribute("replyOptnHashMap", CodeHelper.getItemOptnHashMap(items, replyItemOrder));	// 답변글 항목코드
				
				/* 해당글의 부모글 리스트만 
				String pntIdxColumnName = "PNT_IDX";
				String reLevelColumnName = "RE_LEVEL";
				String reStepColumnName = "RE_STEP";
				
				if(useQna){
					pntSearchList.add(new DTForm(pntIdxColumnName, StringUtil.getInt(dt.get(pntIdxColumnName))));
					pntSearchList.add(new DTForm(reLevelColumnName, 1, ">"));
				}
				else{
					pntSearchList.add(new DTForm(pntIdxColumnName, StringUtil.getInt(dt.get(pntIdxColumnName))));
					pntSearchList.add(new DTForm(reLevelColumnName, StringUtil.getInt(dt.get(reLevelColumnName)), "<"));
					pntSearchList.add(new DTForm(reStepColumnName, StringUtil.getInt(dt.get(reStepColumnName)), "<"));
				}
				
				LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
				String loginMemberIdx = null;
				if(loginVO != null) loginMemberIdx = loginVO.getMemberIdx();
				
				boolean isAdmMode = attrVO.isAdmMode();
				boolean useDldate = JSONObjectUtil.isEquals(settingInfo, "use_dldate", "1");
				boolean usePrivate = JSONObjectUtil.isEquals(settingInfo, "use_private", "1");
	
				pntParam.put("isAdmMode", isAdmMode);
				pntParam.put("useDldate", useDldate);
				pntParam.put("usePrivate", usePrivate);
				pntParam.put("memberIdx", loginMemberIdx);
				pntParam.put("searchList", pntSearchList);
	
				List pntList = boardService.getPntList(fnIdx, pntParam);
				*/
				model.addAttribute("pntList", pntList);
			}
			
			if(!useQna && (dsetViewPreCnt > 0 || dsetViewNextCnt > 0)){
				Map<String, Object> viewListParam = new HashMap<String, Object>();
				List<DTForm> viewListSearchList = new ArrayList<DTForm>();
				
				List<?> viewList = fn_getViewList(attrVO, queryString, settingInfo, itemInfo, items, 2, viewListParam, viewListSearchList);
				model.addAttribute("viewList", viewList);
			}
			
			/****************************************************************************
			 * 조회수 수정 
			 ****************************************************************************/
			int input = queryString.getInt("input");		// 글쓰기 후 확인  페이지 여부
			boolean isAddViews = false;
			String cookieName = request.getAttribute("crtSiteId") + "_" + StringUtil.getInt(request.getParameter("mId")) + "_" + fnIdx + "_board_" + brdIdx;
			String cookieSetValue = brdIdx + "";
			String cookieVal = null;
			if(input != 1) {
				// 저장된 쿠키 가져오기
				cookieVal = CookieUtil.getCookie(request, cookieName);
				//System.out.println("cookieVal==>" + cookieName + ":" + cookieSetValue + ":" + cookieVal);
				if(StringUtil.isEmpty(cookieVal) || !StringUtil.isEquals(cookieVal, cookieSetValue))	{
	
					// 조회수증가
			    	boardService.updateViews(fnIdx, brdIdx);
					dt.put("VIEWS", StringUtil.getInt(dt.get("VIEWS")) + 1);
					isAddViews = true;
					model.addAttribute("addViews", isAddViews);
				}
				
				// 쿠키저장
				if(StringUtil.isEmpty(cookieVal)) CookieUtil.setCookie(response, cookieName, cookieSetValue, 60*60*24, "UTF-8");
			}
		}

		// 4. 속성 setting
		model.addAttribute("dt", dt);																				// 상세정보
		model.addAttribute("list", list);																			// 이전 이후 게시글
		model.addAttribute("multiFileHashMap", boardService.getMultiFileHashMap(fnIdx, brdIdx, items, itemOrder));	// multi file 목록
		model.addAttribute("multiDataHashMap", boardService.getMultiHashMap(fnIdx, brdIdx, items, itemOrder));		// multi data 목록
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));						// 항목코드
		model.addAttribute("submitType", submitType);																// 페이지구분
		
    	// 5. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	if(!queryString.isEmpty("dataNm")) return getViewPath("/pntView");
    	else if(useQna) return getViewPath("/viewQna");
    	else return getViewPath("/view");
	}
	
	/**
	 * 수정 : 관리권한 없는 경우 - 자신이 등록한 글만 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name={"WRT", "RWT"}, checkMode=true)
	@RequestMapping(value = "/input.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		DataForm queryString = attrVO.getQueryString();
		
		boolean isAjax = attrVO.isAjax();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject crtMenu = attrVO.getCrtMenu();
		boolean isAdmMode = attrVO.isAdmMode();

		int fnIdx = attrVO.getFnIdx();

		String ajaxPName = attrVO.getAjaxPName();
		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");

		boolean isModify = false;
		boolean isReply = false;
		boolean useReply = JSONObjectUtil.isEquals(settingInfo, "use_reply", "1");
		boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
		
		// 1. 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);

		if(brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		String loginMemberIdx = null;
		String loginMemberNameOrg = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberNameOrg = loginVO.getMemberNameOrg();
		}
		
		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		
		DataMap dt = null;
		DataMap replyDt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxColumnName, brdIdx));
		
		param.put("searchList", searchList);
		param.put("boardDesignType", boardDesignType);
		param.put("useQna", useQna);
		
		// 2.1 상세정보
		dt = boardService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. 수정모드 여부 검사
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		else if(((!useQna && useReply) || (useQna && StringUtil.getInt(dt.get("BRD_IDX")) == StringUtil.getInt(dt.get("PNT_IDX")) 
				&& StringUtil.getInt(dt.get("QNA_CNT")) < 2)) && StringUtil.isEquals(mode, "r")) {
			// 답글인 경우
			isReply = true;
		}
		
		if(!isModify && !isReply) return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		
		if(!isAdmMode && useQna && StringUtil.getInt(dt.get("BRD_IDX")) == StringUtil.getInt(dt.get("PNT_IDX")) && isModify 
				&& StringUtil.getInt(dt.get("REPLY_STATE")) > 0) {
			// Qna 질문글 수정모드 - 대기 상태 지난 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		boolean isAuthNoMember = (RbsProperties.getPropertyInt("Globals.auth.no.member") == 1); 
		boolean isWrtNmPage = false;
		if(isReply) isWrtNmPage = isAuthNoMember ? AuthHelper.isNmAuthPage("RWT") :  AuthHelper.getModuleUTP("RWT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		else isWrtNmPage = isAuthNoMember ? AuthHelper.isNmAuthPage("WRT") :  AuthHelper.getModuleUTP("WRT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		
		if(PrivAuthUtil.isUseNameAuth() && isWrtNmPage && !UserDetailsHelper.isLogin() && !PrivAuthUtil.isNameAuthCheck(true)){
			model.addAttribute("nmAuthURL", MenuUtil.getMenuUrl(Defines.CODE_MENU_NMEMBER_AUTH));
			model.addAttribute("message", MessageUtil.getConfirm(isAjax, rbsMessageSource.getMessage("message.no.auth.confirm"), null));
			return RbsProperties.getProperty("Globals.error.401.login" + ajaxPName + ".path");
		}
		
		if(isReply){
			// 권한 체크 : 답글쓰기 권한 체크
			boolean isAuth = AuthHelper.isAuthenticated("RWT");
			if(!isAuth) {
				model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
		}

		String pntIdxColumnName = "PNT_IDX";
		String reLevelColumnName = "RE_LEVEL";
		String reStepColumnName = "RE_STEP";
		String submitType = null;
		if(isModify){
			// 4. 전체관리/완전삭제 권한 체크
			int reLevel = StringUtil.getInt(dt.get("RE_LEVEL"));
			String regiIdx = StringUtil.getString(dt.get("REGI_IDX"));
			String regiDupInfo = StringUtil.getString(dt.get("MEMBER_DUP"));
			String regiPwd = StringUtil.getString(dt.get("PWD"));
			Boolean isMngAuth = BoardAuthUtil.isModifyAuth(fnIdx, brdIdx, 0, (useQna || useReply) && (reLevel > 1), regiIdx, regiDupInfo, regiPwd);
					
			if(!isMngAuth) {
				// 자신이 등록한 글이 아닌 경우
				model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}

			submitType = "modify";
		}
		else if(isReply){
			replyDt = new DataMap();
			
			replyDt.put("NAME", loginMemberNameOrg);
			replyDt.put("SUBJECT", "Re : " + StringUtil.getString(dt.get("SUBJECT")));
			replyDt.put(pntIdxColumnName, StringUtil.getInt(dt.get(pntIdxColumnName)));
			replyDt.put(reLevelColumnName, StringUtil.getInt(dt.get(reLevelColumnName)));
			replyDt.put(reStepColumnName, StringUtil.getInt(dt.get(reStepColumnName)));
			replyDt.put("REPLY_STATE", StringUtil.getString(dt.get("REPLY_STATE")));
			
			submitType = "reply";
		}

		boolean isReplyModify = StringUtil.getInt(dt.get(reLevelColumnName)) > 1;
		if(isReply || isReplyModify) submitType = "reply";

		// 5. 항목설정
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		if(isReply || isReplyModify){
			Map<String, Object> pntParam = new HashMap<String, Object>();
			List<DTForm> pntSearchList = new ArrayList<DTForm>();
	
			String columnVFlag = null;
			if(isReply) columnVFlag = "<=";
			else columnVFlag = "<";
			
			pntSearchList.add(new DTForm(pntIdxColumnName, StringUtil.getInt(dt.get(pntIdxColumnName))));
			pntSearchList.add(new DTForm(reLevelColumnName, StringUtil.getInt(dt.get(reLevelColumnName)), columnVFlag));
			pntSearchList.add(new DTForm(reStepColumnName, StringUtil.getInt(dt.get(reStepColumnName)), columnVFlag));
			
			/*
			boolean isAdmMode = attrVO.isAdmMode();
			boolean useDldate = JSONObjectUtil.isEquals(settingInfo, "use_dldate", "1");
			int usePrivateVal = ModuleUtil.getSettingValue(isAdmMode, "use_private", crtMenu, settingInfo, 0);
			boolean usePrivate = (usePrivateVal == 1);
			//boolean usePrivate = JSONObjectUtil.isEquals(settingInfo, "use_private", "1");
	
			pntParam.put("isAdmMode", isAdmMode);
			pntParam.put("useDldate", useDldate);
			pntParam.put("usePrivate", usePrivate);
			pntParam.put("memberIdx", loginMemberIdx);
			pntParam.put("searchList", pntSearchList);
			
			List<?> pntList = boardService.getPntList(fnIdx, pntParam);
			*/
			
			List<?> pntList = fn_getViewList(attrVO, queryString, settingInfo, itemInfo, items, 3, pntParam, pntSearchList);
			model.addAttribute("pntList", pntList);

			JSONArray pntItemOrder = JSONObjectUtil.getJSONArray(itemInfo, "view_order");
			HashMap<String, Object> pntMultiFileHashMap = new HashMap<String, Object>();
			HashMap<String, Object> pntMultiDataHashMap = new HashMap<String, Object>();
			if(pntList != null){
				for(int i = 0; i < pntList.size(); i++){
					DataMap pntDt = (DataMap)pntList.get(i);
					int pntBrdIdx = StringUtil.getInt(pntDt.get(idxColumnName));
					String pntBrdIdxStr = StringUtil.toString(pntBrdIdx);
					pntMultiFileHashMap.put(pntBrdIdxStr, boardService.getMultiFileHashMap(fnIdx, pntBrdIdx, items, pntItemOrder));
					pntMultiDataHashMap.put(pntBrdIdxStr, boardService.getMultiHashMap(fnIdx, pntBrdIdx, items, pntItemOrder));
				}
			}

			model.addAttribute("pntMultiFileHashMap", pntMultiFileHashMap);	// multi file 목록
			model.addAttribute("pntMultiDataHashMap", pntMultiDataHashMap);	// multi data 목록
			model.addAttribute("pntOptnHashMap", CodeHelper.getItemOptnHashMap(items, pntItemOrder));	// 항목코드
		}

		// 6. 속성 setting
		if(isModify){
			model.addAttribute("dt", dt);
			model.addAttribute("multiFileHashMap", boardService.getMultiFileHashMap(fnIdx, brdIdx, items, itemOrder));	// multi file 목록
			model.addAttribute("multiDataHashMap", boardService.getMultiHashMap(fnIdx, brdIdx, items, itemOrder));	// multi data 목록	
		}
		else{
			model.addAttribute("dt", replyDt);
		}
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));							// 항목코드
		model.addAttribute("submitType", submitType);																// 페이지구분
		
    	// 7. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 8. form action
    	if(isModify) model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
    	else if(isReply) model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_REPLYPROC"));
		
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
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		/*
		// 본인인증회원 글쓰기일 경우 실명인증 여부
		if(PrivAuthUtil.isUseNameAuth() && AuthHelper.getModuleUTP("WRT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER") 
				 && !UserDetailsHelper.isLogin() && !PrivAuthUtil.isNameAuthCheck(true)){
			model.addAttribute("nmAuthURL", MenuUtil.getMenuUrl(Defines.CODE_MENU_NMEMBER_AUTH));
			model.addAttribute("message", MessageUtil.getConfirm(attrVO.isAjax(), rbsMessageSource.getMessage("message.no.auth.confirm"), null));
			return RbsProperties.getProperty("Globals.error.401.login" + attrVO.getAjaxPName() + ".path");
		}
		*/
		
		JSONObject itemInfo = attrVO.getItemInfo();

		// 1. 작성자명 setting
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
	@ModuleAuth(name={"WRT", "RWT"}, checkMode=true)
	@RequestMapping(method=RequestMethod.POST, value = "/inputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		boolean isAdmMode = attrVO.isAdmMode();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		int fnIdx = attrVO.getFnIdx();

		String ajaxPName = attrVO.getAjaxPName();
		String siteMode = attrVO.getSiteMode();
		String uploadModulePath = attrVO.getUploadModulePath();
		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		
		// 1.2 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		boolean isModify = false;
		boolean isReply = false;
		boolean useReply = JSONObjectUtil.isEquals(settingInfo, "use_reply", "1");
		boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
		
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxColumnName, brdIdx));
		param.put("searchList", searchList);
		param.put("boardDesignType", boardDesignType);
		param.put("useQna", useQna);

		dt = boardService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 1.1 수정모드 여부 검사
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		else if(((!useQna && useReply) || (useQna && StringUtil.getInt(dt.get("BRD_IDX")) == StringUtil.getInt(dt.get("PNT_IDX")) 
				&& StringUtil.getInt(dt.get("QNA_CNT")) < 2)) && StringUtil.isEquals(mode, "r")) {
			isReply = true;

			// 권한 체크 : 답글쓰기 권한 체크
			boolean isAuth = AuthHelper.isAuthenticated("RWT");
			if(!isAuth) {
				model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
		}

		if(!isModify && !isReply) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		if(!isAdmMode && useQna && StringUtil.getInt(dt.get("BRD_IDX")) == StringUtil.getInt(dt.get("PNT_IDX")) && isModify 
				&& StringUtil.getInt(dt.get("REPLY_STATE")) > 0) {
			// Qna 질문글 수정모드 - 대기 상태 지난 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		/*
		boolean isWrtNmPage = false; 
		if(isReply) isWrtNmPage = AuthHelper.getModuleUTP("RWT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		else isWrtNmPage = AuthHelper.getModuleUTP("WRT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");
		
		if(PrivAuthUtil.isUseNameAuth() && isWrtNmPage && !UserDetailsHelper.isLogin() && !PrivAuthUtil.isNameAuthCheck(true)){
			model.addAttribute("nmAuthURL", MenuUtil.getMenuUrl(Defines.CODE_MENU_NMEMBER_AUTH));
			model.addAttribute("message", MessageUtil.getConfirm(isAjax, rbsMessageSource.getMessage("message.no.auth.confirm"), null));
			return RbsProperties.getProperty("Globals.error.401.login" + ajaxPName + ".path");
		}
		*/
		
		String submitType = null;
		String inputFlag = null;
		if(isModify){
			
			String prePwd = StringUtil.getString(parameterMap.get("pre_pwd"));
			String designTypeMemo = RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO");
			if(StringUtil.isEquals(boardDesignType, designTypeMemo)) {
				// 한줄형 게시판
				prePwd = StringUtil.getString(parameterMap.get("pwd"));
			}
			// 2. 전체관리/완전삭제 권한 체크
			Boolean isMngAuth = BoardAuthUtil.isMngProcAuth(settingInfo, fnIdx, brdIdx, 0, (useQna || useReply), prePwd);
			if(!isMngAuth) {
				// 자신이 등록한 글이 아닌 경우
				model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
			
			submitType = "modify";
			inputFlag = "modify";
		}
		else if(isReply){
			submitType = "reply";
			inputFlag = "write";
		}

		String reLevelColumnName = "RE_LEVEL";
		boolean isReplyModify = StringUtil.getInt(dt.get(reLevelColumnName)) > 1;
		if(isReply || isReplyModify) submitType = "reply";
			
		// 3. 항목설정
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		parameterMap.put("_itemInputTypeFlag", inputFlag);
		
		// 4. 필수입력 체크
		/*int useQNAItem = 0;		//Q&A항목 사용여부
		if(StringUtil.isEquals(submitType, "reply") && !useReply && useQna) {
			useQNAItem = 1;
		}*/
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, inputFlag, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 비회원 댓글쓰기 권한 : 비밀번호 입력 체크
		boolean isNoMemberAuthPage = AuthHelper.isNoMemberAuthPage("RWT");
		if(isReply && isNoMemberAuthPage) {
			String pwd = StringUtil.getString(parameterMap.get("pwd"));
			if(StringUtil.isEmpty(pwd)) {
				model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required"), rbsMessageSource.getMessage("item.board.password"))));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
		}
		
		// 4. DB
    	int result = 0;
    	if(isModify) result = boardService.update(uploadModulePath, boardDesignType, fnIdx, brdIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	else if(isReply) result = boardService.insert(uploadModulePath, boardDesignType, fnIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
    		
        	// 기본경로
        	fn_setCommonPath(attrVO);
        	
    		String inputNpname = fn_dsetInputNpname(settingInfo);
    		
    		// 비밀번호 session 생성
    		if(isModify) {
    			HttpSession session = request.getSession(true);
	    		String chkAuthName = StringUtil.getString(session.getAttribute("brdChkAuthName"));
	    		isNoMemberAuthPage = AuthHelper.isNoMemberAuthPage(chkAuthName);
    		}
    		int secret = StringUtil.getInt(parameterMap.get("secret"));
			String pwd = StringUtil.getString(parameterMap.get("pwd"));
			if(StringUtil.isEmpty(pwd)) pwd = StringUtil.getString(parameterMap.get("pre_pwd"));
    		setPwdSKeySession(secret == 1, isNoMemberAuthPage, StringUtil.isEquals(inputNpname, "IDX_VIEW"), "bpwd_" + fnIdx + "_" + brdIdx, pwd);

			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_" + inputNpname))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
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
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		boolean isAdmMode = attrVO.isAdmMode();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		int fnIdx = attrVO.getFnIdx();

		String ajaxPName = attrVO.getAjaxPName();
		String siteMode = attrVO.getSiteMode();
		String uploadModulePath = attrVO.getUploadModulePath();
		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");

		/*
		if(PrivAuthUtil.isUseNameAuth() && AuthHelper.getModuleUTP("WRT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER") 
				 && !UserDetailsHelper.isLogin() && !PrivAuthUtil.isNameAuthCheck(true)){
			model.addAttribute("nmAuthURL", MenuUtil.getMenuUrl(Defines.CODE_MENU_NMEMBER_AUTH));
			model.addAttribute("message", MessageUtil.getConfirm(isAjax, rbsMessageSource.getMessage("message.no.auth.confirm"), null));
			return RbsProperties.getProperty("Globals.error.401.login" + ajaxPName + ".path");
		}
		*/
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		parameterMap.put("_itemInputTypeFlag", submitType);
		
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 비회원 글쓰기 권한 : 비밀번호 입력 체크
		boolean isNoMemberAuthPage = AuthHelper.isNoMemberAuthPage("WRT");
		if(isNoMemberAuthPage) {
			String pwd = StringUtil.getString(parameterMap.get("pwd"));
			if(StringUtil.isEmpty(pwd)) {
				model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required"), rbsMessageSource.getMessage("item.board.password"))));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
		}
		
		// 3. DB
    	int result = boardService.insert(uploadModulePath, boardDesignType, fnIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
        	String inputNpname = fn_dsetInputNpname(settingInfo);

    		// 비밀번호 session 생성
    		int secret = StringUtil.getInt(parameterMap.get("secret"));
    		setPwdSKeySession(secret == 1, isNoMemberAuthPage, StringUtil.isEquals(inputNpname, "IDX_VIEW"), "bpwd_" + fnIdx + "_" + result, StringUtil.getString(parameterMap.get("pwd")));
    		
        	String toUrl = PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_" + inputNpname)));
        	if(StringUtil.isEquals(inputNpname, "IDX_VIEW")) toUrl += "&" + JSONObjectUtil.getString(settingInfo, "idx_name") + "=" + result;
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + toUrl + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
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

	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/moList.do")
	public String moList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject memoItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "memo_item_info");
		DataForm queryString = attrVO.getQueryString();
		JSONObject crtMenu = attrVO.getCrtMenu();
		boolean isAdmMode = attrVO.isAdmMode();
		
		int fnIdx = attrVO.getFnIdx();
		
		// 1. 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = ModuleUtil.getSettingValue(isAdmMode, "dset_memo_list_count", crtMenu, settingInfo, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = ModuleUtil.getSettingValue(isAdmMode, "dset_memo_list_block", crtMenu, settingInfo, propertiesService.getInt("PAGE_SIZE"));	
			
			String pageName = JSONObjectUtil.getString(settingInfo, "memo_page_name");
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
		searchList.add(new DTForm(JSONObjectUtil.getString(settingInfo, "idx_column"), brdIdx));
		
		// 2.1 검색조건
		// 항목설정으로 검색조건 setting
		String listSearchId = "list_search";		// 검색설정
		JSONObject listSearch = JSONObjectUtil.getJSONObject(memoItemInfo, listSearchId);
		List<DTForm> memoItemInfoSearchList = ModuleUtil.getItemInfoSearchList(listSearch, queryString);
		if(memoItemInfoSearchList != null) {
			searchList.addAll(memoItemInfoSearchList);
			model.addAttribute("isMemoSearchList", new Boolean(true));
		}
		
		param.put("searchList", searchList);
		param.put("boardDesignType", RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO"));
		
		// 2.2 목록수
    	totalCount = boardService.getTotalCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);

    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    	}
    		
    		// 2.3 목록
    		list = boardService.getList(fnIdx, param);
		}

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 항목코드

		// 1. 항목설정
		String submitType = "list";
		JSONObject memoItems = JSONObjectUtil.getJSONObject(memoItemInfo, "items");
		JSONArray memoItemOrder = JSONObjectUtil.getJSONArray(memoItemInfo, submitType + "_order");
		
		// 2. 속성 setting
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(memoItems, memoItemOrder));
		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/moList");
	}
	
	/**
	 * 수정 : 관리권한 없는 경우 - 자신이 등록한 글만 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MWT")
	@RequestMapping(value = "/moInput.do", params="mode")
	public String moInput(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject memoItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "memo_item_info");

		int fnIdx = attrVO.getFnIdx();

		String ajaxPName = attrVO.getAjaxPName();
		String boardDesignType = RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO");

		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		int memIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "memo_idx_name")), 0);

		if(!isModify || brdIdx <= 0 || memIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		/*
		// 본인인증회원 글쓰기일 경우 실명인증 여부
		if(PrivAuthUtil.isUseNameAuth() && AuthHelper.getModuleUTP("MWT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER") 
				 && !UserDetailsHelper.isLogin() && !PrivAuthUtil.isNameAuthCheck(true)){
			model.addAttribute("nmAuthURL", MenuUtil.getMenuUrl(Defines.CODE_MENU_NMEMBER_AUTH));
			model.addAttribute("message", MessageUtil.getConfirm(attrVO.isAjax(), rbsMessageSource.getMessage("message.no.auth.confirm"), null));
			return RbsProperties.getProperty("Globals.error.401.login" + attrVO.getAjaxPName() + ".path");
		}
		*/
		
		// 2. DB
		String idxBrdColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		String idxMemColumnName = JSONObjectUtil.getString(settingInfo, "memo_idx_column");		// key column명
		
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A." + idxBrdColumnName, brdIdx));
		searchList.add(new DTForm("A." + idxMemColumnName, memIdx));
		
		param.put("boardDesignType", boardDesignType);
		/*
		if(AuthHelper.isNoMemberAuthPage("MWT")){
			String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx + "_" + memIdx;
			HttpSession session = request.getSession(true);
			String encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
			if(StringUtil.isEmpty(encPwd)){
				model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required"), rbsMessageSource.getMessage("item.board.password"))));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
			else{
				searchList.add(new DTForm("A.PWD", encPwd));
				
				Map<String, Object> pwdParam = new HashMap<String, Object>();
				pwdParam.put("searchList", searchList);
				pwdParam.putAll(param);
				
				int dbPwdCnt = boardService.getPwdCnt(fnIdx, pwdParam);

				if(dbPwdCnt <= 0) {
					// 해당글의 비밀번호가 없는 경우
					model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.board.no.auth")));
					return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
				}
			}
		}
		*/

		param.put("searchList", searchList);
		
		// 2.1 상세정보
		dt = boardService.getModify(fnIdx, param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. 수정 페이지 접근 권한 체크 (전체관리/완전삭제 권한 포함)
		String regiIdx = StringUtil.getString(dt.get("REGI_IDX"));
		String regiDupInfo = StringUtil.getString(dt.get("MEMBER_DUP"));
		String regiPwd = StringUtil.getString(dt.get("PWD"));
		Boolean isMngAuth = BoardAuthUtil.isModifyAuth(fnIdx, brdIdx, memIdx, false, regiIdx, regiDupInfo, regiPwd);
				
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. 항목설정
		String submitType = "modify";
		JSONObject memoItems = JSONObjectUtil.getJSONObject(memoItemInfo, "items");
		JSONArray memoItemOrder = JSONObjectUtil.getJSONArray(memoItemInfo, submitType + "_order");

		// 5. 속성 setting
		model.addAttribute("dt", dt);					
		model.addAttribute("itemInfo", memoItemInfo);															// 상세정보
		model.addAttribute("multiFileHashMap", boardService.getMultiFileHashMap(fnIdx, brdIdx, memoItems, memoItemOrder));	// multi file 목록
		model.addAttribute("multiDataHashMap", boardService.getMultiHashMap(fnIdx, brdIdx, memoItems, memoItemOrder));	// multi data 목록
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(memoItems, memoItemOrder));					// 항목코드
		model.addAttribute("submitType", submitType);																// 페이지구분
		
    	// 6. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 7. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MEMOMODIFYPROC") + "&" + JSONObjectUtil.getString(settingInfo, "memo_idx_name") + "=" + memIdx );
		
    	return getViewPath("/moInput");
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/moInput.do")
	public String moInput(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject memoItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "memo_item_info");

		// 1. 작성자명 setting
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		DataMap dt = new DataMap();

		// 2. 항목설정
		String submitType = "write";
		JSONObject memoItems = JSONObjectUtil.getJSONObject(memoItemInfo, "items");
		JSONArray memoItemOrder = JSONObjectUtil.getJSONArray(memoItemInfo, submitType + "_order");
		
		if(loginVO != null)	{
			String memberName = loginVO.getMemberNameOrg();
			JSONObject nameItem = JSONObjectUtil.getJSONObject(memoItems, "name");
			int objectType = JSONObjectUtil.getInt(nameItem, "object_type");
			if(objectType == 101) {
				// 암/복호화 항목
				memberName = loginVO.getMemberName();
			}
			dt.put("NAME", memberName);
		}

		// 3. 속성 setting
		model.addAttribute("dt", dt);
		model.addAttribute("itemInfo", memoItemInfo);
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(memoItems, memoItemOrder));
		model.addAttribute("submitType", submitType);

    	// 4. 기본경로
    	fn_setCommonPath(attrVO);

    	// 5. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MEMOINPUTPROC"));
    	
		return getViewPath("/moInput");
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
	@ModuleAuth(name="MWT")
	@RequestMapping(method=RequestMethod.POST, value = "/moInputProc.do", params="mode")
	public String moInputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		boolean isAdmMode = attrVO.isAdmMode();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject memoItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "memo_item_info");
		int fnIdx = attrVO.getFnIdx();

		String ajaxPName = attrVO.getAjaxPName();
		String siteMode = attrVO.getSiteMode();
		String uploadModulePath = attrVO.getUploadModulePath();
		String boardDesignType = RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO");
		
		// 1. 수정모드 여부 / 필수 parameter 검사
		// 1.1 수정모드 여부 검사
		boolean isModify = false;
		if(StringUtil.isEquals(mode, "m")) isModify = true;
		
		// 1.2 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		int memIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "memo_idx_name")), 0);

		if(!isModify || brdIdx <= 0 || memIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		/*
		if(AuthHelper.isNoMemberAuthPage("MWT")){
			if(parameterMap.isEmpty("pwd")){
				model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required"), rbsMessageSource.getMessage("item.board.password"))));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
			else{
				String encPwd = DataSecurityUtil.getDigest(parameterMap.getString("pwd"));
				
				Map<String, Object> param = new HashMap<String, Object>();
				List<DTForm> searchList = new ArrayList<DTForm>();
				searchList.add(new DTForm("A." + JSONObjectUtil.getString(settingInfo, "idx_column"), brdIdx));
				searchList.add(new DTForm("A." + JSONObjectUtil.getString(settingInfo, "memo_idx_column"), memIdx));
				searchList.add(new DTForm("A.PWD", encPwd));
				
				param.put("searchList", searchList);
				param.put("boardDesignType", boardDesignType);
				
				int dbPwdCnt = boardService.getPwdCnt(fnIdx, param);

				if(dbPwdCnt <= 0) {
					// 해당글의 비밀번호가 없는 경우
					model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.board.no.auth")));
					return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
				}
			}
		}
 		*/		
		/*
		// 본인인증회원 글쓰기일 경우 실명인증 여부
		if(PrivAuthUtil.isUseNameAuth() && AuthHelper.getModuleUTP("MWT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER") 
				 && !UserDetailsHelper.isLogin() && !PrivAuthUtil.isNameAuthCheck(true)){
			model.addAttribute("nmAuthURL", MenuUtil.getMenuUrl(Defines.CODE_MENU_NMEMBER_AUTH));
			model.addAttribute("message", MessageUtil.getConfirm(attrVO.isAjax(), rbsMessageSource.getMessage("message.no.auth.confirm"), null));
			return RbsProperties.getProperty("Globals.error.401.login" + attrVO.getAjaxPName() + ".path");
		}
		*/
		/*
		// 비회원 글쓰기/댓글쓰기 권한에 따른 비밀번호 인증 체크
		String chkAuthNoMemberPage = getChkAuthNoMemberPage(settingInfo, fnIdx, brdIdx, memIdx, false, false, null, null, request, model);
		
		if(!StringUtil.isEmpty(chkAuthNoMemberPage)) {
			return chkAuthNoMemberPage;
		}
		*/
		// 2. 전체관리/완전삭제 권한 체크
		Boolean isMngAuth = BoardAuthUtil.isMngProcAuth(settingInfo, fnIdx, brdIdx, memIdx, false, StringUtil.getString(parameterMap.get("pwd")));
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
			
		// 3. 항목설정
		String submitType = "modify";
		JSONObject memoItems = JSONObjectUtil.getJSONObject(memoItemInfo, "items");
		JSONArray memoItemOrder = JSONObjectUtil.getJSONArray(memoItemInfo, submitType + "proc_order");
		
		// 4. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, memoItems, memoItemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 4. DB
    	int result = boardService.update(uploadModulePath, boardDesignType, fnIdx, brdIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, memoItems, memoItemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_IDX_VIEW"))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
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
	@ModuleAuth(name="MWT")
	@RequestMapping(method=RequestMethod.POST, value = "/moInputProc.do")
	public String moInputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		boolean isAdmMode = attrVO.isAdmMode();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject memoItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "memo_item_info");
		
		int fnIdx = attrVO.getFnIdx();

		String ajaxPName = attrVO.getAjaxPName();
		String siteMode = attrVO.getSiteMode();
		String uploadModulePath = attrVO.getUploadModulePath();
		String boardDesignType = RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO");

		if(AuthHelper.isNoMemberAuthPage("MWT") && parameterMap.isEmpty("pwd")){
			model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required"), rbsMessageSource.getMessage("item.board.password"))));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		/*
		// 본인인증회원 글쓰기일 경우 실명인증 여부
		if(PrivAuthUtil.isUseNameAuth() && AuthHelper.getModuleUTP("MWT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER") 
				 && !UserDetailsHelper.isLogin() && !PrivAuthUtil.isNameAuthCheck(true)){
			model.addAttribute("nmAuthURL", MenuUtil.getMenuUrl(Defines.CODE_MENU_NMEMBER_AUTH));
			model.addAttribute("message", MessageUtil.getConfirm(attrVO.isAjax(), rbsMessageSource.getMessage("message.no.auth.confirm"), null));
			return RbsProperties.getProperty("Globals.error.401.login" + attrVO.getAjaxPName() + ".path");
		}
		*/
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject memoItems = JSONObjectUtil.getJSONObject(memoItemInfo, "items");
		JSONArray memoItemOrder = JSONObjectUtil.getJSONArray(memoItemInfo, submitType + "proc_order");
		
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, memoItems, memoItemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// 3. DB
    	int result = boardService.insert(uploadModulePath, boardDesignType, fnIdx, siteMode, request.getRemoteAddr(), parameterMap, settingInfo, memoItems, memoItemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procAction(\"" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_IDX_VIEW"))) + "\", " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
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
	public String delete(@ParamMap ParamForm parameterMap, @RequestParam(value="select") int[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		
		int fnIdx = attrVO.getFnIdx();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		String ajaxPName = attrVO.getAjaxPName();
		String siteMode = attrVO.getSiteMode();
		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		
		// 1. DB
		int result = boardService.delete(parameterMap, settingInfo, boardDesignType, fnIdx, deleteIdxs, siteMode, request.getRemoteAddr());
    	
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
	public String delete(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		String siteMode = attrVO.getSiteMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		int fnIdx = attrVO.getFnIdx();

		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		
		// 1. 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		if(brdIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. 전체관리/완전삭제 권한 체크
		boolean useReply = JSONObjectUtil.isEquals(settingInfo, "use_reply", "1");
		boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
		Boolean isMngAuth = BoardAuthUtil.isMngProcAuth(settingInfo, fnIdx, brdIdx, 0, (useQna || useReply), null);
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		// Qna 질문글 - 대기 상태 지난 경우 삭제처리 금지
		boolean isAdmMode = attrVO.isAdmMode();
		if(!isAdmMode && useQna) {
			String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");
			DataMap dt = null;
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			
			searchList.add(new DTForm("A." + idxColumnName, brdIdx));
			
			param.put("searchList", searchList);
			param.put("boardDesignType", boardDesignType);
			param.put("useQna", useQna);
			
			// 2.1 상세정보
			dt = boardService.getModify(fnIdx, param);
			if(dt == null) {
				// 해당글이 없는 경우
				model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
			
			if(StringUtil.getInt(dt.get("BRD_IDX")) == StringUtil.getInt(dt.get("PNT_IDX"))
				&& StringUtil.getInt(dt.get("REPLY_STATE")) > 0){
				// Qna 질문글 - 대기 상태 지난 경우
				model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
		}
		
		int[] deleteIdxs = {brdIdx};
		// 2. DB
		int result = boardService.delete(parameterMap, settingInfo, boardDesignType, fnIdx, deleteIdxs, siteMode, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.delete"), "fn_procAction('" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_LIST"))) + "', " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.delete")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}

	@ModuleAuth(name="WRT")
	@RequestMapping(method=RequestMethod.GET, value = "/moDeleteProc.do")
	public String moDelete(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		boolean isAdmMode = attrVO.isAdmMode();
		
		int fnIdx = attrVO.getFnIdx();

		String ajaxPName = attrVO.getAjaxPName();
		String siteMode = attrVO.getSiteMode();
		String boardDesignType = RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO");

		/*
		// 본인인증회원 글쓰기일 경우 실명인증 여부
		if(PrivAuthUtil.isUseNameAuth() && AuthHelper.getModuleUTP("MWT") <= RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER") 
				 && !UserDetailsHelper.isLogin() && !PrivAuthUtil.isNameAuthCheck(true)){
			model.addAttribute("nmAuthURL", MenuUtil.getMenuUrl(Defines.CODE_MENU_NMEMBER_AUTH));
			model.addAttribute("message", MessageUtil.getConfirm(attrVO.isAjax(), rbsMessageSource.getMessage("message.no.auth.confirm"), null));
			return RbsProperties.getProperty("Globals.error.401.login" + attrVO.getAjaxPName() + ".path");
		}
		*/
		
		// 1. 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		int memIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "memo_idx_name")), 0);
		if(brdIdx <= 0 || memIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		if(AuthHelper.isNoMemberAuthPage("MWT") && !isAdmMode){
			String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx + "_" + memIdx;
			HttpSession session = request.getSession(true);
			String encPwd = StringUtil.getString(session.getAttribute(pwdSKey));
			if(StringUtil.isEmpty(encPwd)){
				model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required"), rbsMessageSource.getMessage("item.board.password"))));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}else{
				List<DTForm> searchList = new ArrayList<DTForm>();
				searchList.add(new DTForm("A." + JSONObjectUtil.getString(settingInfo, "idx_column"), brdIdx));
				searchList.add(new DTForm("A." + JSONObjectUtil.getString(settingInfo, "memo_idx_column"), memIdx));
				searchList.add(new DTForm("A.PWD", encPwd));
				
				Map<String, Object> pwdParam = new HashMap<String, Object>();
				pwdParam.put("searchList", searchList);
				pwdParam.put("boardDesignType", boardDesignType);
				
				int dbPwdCnt = boardService.getPwdCnt(fnIdx, pwdParam);

				if(dbPwdCnt <= 0) {
					// 해당글의 비밀번호가 없는 경우
					model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.board.no.auth")));
					return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
				}
			}
		}
		
		// 2. 전체관리/완전삭제 권한 체크
		Boolean isMngAuth = BoardAuthUtil.isMngProcAuth(settingInfo, fnIdx, brdIdx, memIdx, false, StringUtil.getString(parameterMap.get("pwd")));
		if(!isMngAuth) {
			// 자신이 등록한 글이 아닌 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		int[] deleteIdxs = {memIdx};
		// 2. DB
		int result = boardService.delete(parameterMap, settingInfo, boardDesignType, fnIdx, deleteIdxs, siteMode, request.getRemoteAddr());
    	
    	if(result > 0) {
    		// 저장 성공
        	// 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.delete"), "fn_procAction('" + PathUtil.getAddProtocolToPagePath(StringUtil.getString(request.getAttribute("URL_IDX_VIEW"))) + "', " + StringUtil.getInt(request.getParameter("mdl")) + ");"));
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
	public String restore(@RequestParam(value="select") int[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String siteMode = attrVO.getSiteMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 1. DB
		int result = boardService.restore(settingInfo, fnIdx, restoreIdxs, siteMode, request.getRemoteAddr());
    	
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
	public String cdelete(@RequestParam(value="select") int[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		int fnIdx = attrVO.getFnIdx();
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		// 2. DB
		int result = boardService.cdelete(uploadModulePath, settingInfo, fnIdx, deleteIdxs, items, itemOrder);
    	
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
	public String deleteList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		int fnIdx = attrVO.getFnIdx();
		DataForm queryString = attrVO.getQueryString();

		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		
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
		param.put("boardDesignType", boardDesignType);

		// 2.2 목록수
    	totalCount = boardService.getDeleteCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = boardService.getDeleteList(fnIdx, param);
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
	 * 
	 * @param attrVO
	 * @param queryString
	 * @param settingInfo
	 * @param itemInfo
	 * @param items
	 * @param listType - 1:관련글(useReply, useQna), 2:이전글, 다음글, 3:부모글, 4:조회수 높은 글 노출
	 * @param param
	 * @param searchList
	 * @return
	 */
	
	public List<?> fn_getViewList(ModuleAttrVO attrVO, DataForm queryString, JSONObject settingInfo, JSONObject itemInfo, JSONObject items, int listType, Map<String, Object> param, List<DTForm> searchList){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		int loginUsertypeIdx = 0;
		String loginMemberIdx = null;
		if(loginVO != null){
			loginUsertypeIdx = loginVO.getUsertypeIdx();
			loginMemberIdx = loginVO.getMemberIdx();
		}

		JSONObject crtMenu = attrVO.getCrtMenu();

		String sDupInfo = StringUtil.getString(session.getAttribute("sDupInfo"), StringUtil.getString(session.getAttribute("iSDupInfo")));
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");
		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		String dsetCateListId = JSONObjectUtil.getString(settingInfo, "dset_cate_list_id");
		String dsetCateColumnId = JSONObjectUtil.getString(JSONObjectUtil.getJSONObject(items, dsetCateListId), "column_id");
		String dsetOrderField = JSONObjectUtil.getString(settingInfo, "dset_order_field");

		int fnIdx = attrVO.getFnIdx();
		int brdIdx = StringUtil.getInt(request.getParameter(idxName), 0);
		int dsetListNewcnt = JSONObjectUtil.getInt(settingInfo, "dset_list_newcnt");
		int dsetViewPreCnt = JSONObjectUtil.getInt(settingInfo, "dset_view_precnt");
		int dsetViewNextCnt = JSONObjectUtil.getInt(settingInfo, "dset_view_nextcnt");
				
		boolean isAdmMode = attrVO.isAdmMode();
		boolean useDldate = JSONObjectUtil.isEquals(settingInfo, "use_dldate", "1");
		int usePrivateVal = ModuleUtil.getSettingValue(isAdmMode, "use_private", crtMenu, settingInfo, 0);
		boolean usePrivate = (usePrivateVal == 1);
		boolean useListImg = JSONObjectUtil.isEquals(settingInfo, "use_list_img", "1");
		boolean useSecret = JSONObjectUtil.isEquals(settingInfo, "use_secret", "1");
		boolean useQna = JSONObjectUtil.isEquals(settingInfo, "use_qna", "1");
		boolean useReply = JSONObjectUtil.isEquals(settingInfo, "use_reply", "1");
		boolean isLstNmPage = loginUsertypeIdx == RbsProperties.getPropertyInt("Globals.code.USERTYPE_SMEMBER");

		if(param == null) param = new HashMap<String, Object>();
		if(searchList == null) searchList = new ArrayList<DTForm>();
		
		// 탭 검색 조건
		if(listType == 2){
			List<DTForm> cateTabSearchList = ModuleUtil.getCateTabSearchList(settingInfo, items, queryString, CodeHelper.getItemOptnHashMap(items, JSONObjectUtil.getJSONArray(itemInfo, "list_order")));
			//fn_getCateTabSearchList(queryString, settingInfo, items, CodeHelper.getItemOptnHashMap(items, JSONObjectUtil.getJSONArray(itemInfo, "list_order")));
			if(cateTabSearchList != null){
				searchList.addAll(cateTabSearchList);
			}
		}

		param.put("searchList", searchList);
		param.put("boardDesignType", boardDesignType);
		param.put("isAdmMode", isAdmMode);
		param.put("useDldate", useDldate);
		param.put("usePrivate", usePrivate);
		param.put("useListImg", useListImg);
		param.put("useSecret", useSecret);
		param.put("useQna", useQna);
		param.put("pageType", "view");
		param.put("useReply", useReply);
		param.put("dsetCateColumnId", dsetCateColumnId);
		param.put("memberIdx", loginMemberIdx);
		if(listType == 1) param.put("isAllSelect", true);
		if(isLstNmPage) param.put("memberDup", sDupInfo);
		param.put("isNmPage", isLstNmPage);
		param.put("dsetOrderField", dsetOrderField);
		
		if(listType == 4){
			List<DTOrderForm> orderList = new ArrayList<DTOrderForm>();
			orderList.add(new DTOrderForm("A.VIEWS", 1));
			orderList.add(new DTOrderForm("A.PNT_IDX", 1));
			orderList.add(new DTOrderForm("A.RE_STEP", 0));
    		param.put("firstIndex", 0);
    		param.put("lastIndex", 1);
    		param.put("recordCountPerPage", dsetListNewcnt);
			param.put("orderList", orderList);
		}
			
		List<?> list = null;
		if(listType == 2){
			param.put("brdIdx", brdIdx);
			if(dsetViewPreCnt > 1 || dsetViewNextCnt > 1){
				int nextListCnt = boardService.getNextCount(fnIdx, param);
				int preListCnt = boardService.getPreCount(fnIdx, param);

				if(nextListCnt < dsetViewNextCnt)
				{
					dsetViewPreCnt = dsetViewPreCnt + dsetViewNextCnt - nextListCnt;
					dsetViewNextCnt = nextListCnt;
				}
				else if(preListCnt < dsetViewPreCnt)
				{
					dsetViewNextCnt = dsetViewPreCnt + dsetViewNextCnt - preListCnt;
					dsetViewPreCnt = preListCnt;
				}
			}
			
			param.put("dsetViewNextCnt", dsetViewNextCnt);
			param.put("dsetViewPreCnt", dsetViewPreCnt);

			if(!StringUtil.isEmpty(dsetOrderField)){
				String orderStr = "ASC";
				String inverseOrderStr = "DESC";
				StringBuffer inverseDesetOrderField = new StringBuffer();
				String[] dsetOrderFields = dsetOrderField.split(",");
				if(!StringUtil.isEmpty(dsetOrderFields)){
					for (int i = 0; i < dsetOrderFields.length; i++) {
						int orderFieldLen = 0;
						if(!StringUtil.isEmpty(dsetOrderFields[i])){
							String[] orderField = dsetOrderFields[i].trim().split(" ");
							if(!StringUtil.isEmpty(orderField)) orderFieldLen = orderField.length;
							try{
								if(orderFieldLen > 0){
									if(i > 0) inverseDesetOrderField.append(", ");
									if(orderFieldLen == 1) inverseDesetOrderField.append(orderField[0] + " " + inverseOrderStr);
									else{
										String orderFieldOrderStr = StringUtil.toUpperCase(orderField[1]);
										if(StringUtil.isEquals(orderFieldOrderStr, orderStr)) inverseDesetOrderField.append(orderField[0] + " " + inverseOrderStr);
										else inverseDesetOrderField.append(orderField[0] + " " + orderStr);
									}
								}
							}catch(Exception e){}	
						}
					}
				}
				param.put("inverseDefaultOrderStr", inverseDesetOrderField.toString());
			}
			
			list = boardService.getPreNextList(fnIdx, param);
		}
		else if(listType == 3) list = boardService.getPntList(fnIdx, param);
		else list = boardService.getList(fnIdx, param);
		
		return list;
	}
	
	/**
	 * 비밀번호 session 생성
	 * @param isSecret
	 * @param isNoMemberAuthPage
	 * @param isView
	 * @param pwdSKey
	 * @param pwd
	 */
	public void setPwdSKeySession(boolean isSecret, boolean isNoMemberAuthPage, boolean isView, String pwdSKey, String pwd) {
    	
		if(isSecret && isNoMemberAuthPage && isView) {
			// 비밀글 && 비회원글쓰기 && 상세페이지로 이동 : 비밀번호 session 생성
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			HttpSession session = request.getSession(true);
			
			String encPwd = DataSecurityUtil.getDigest(pwd);
			session.setAttribute(pwdSKey, encPwd);
		}
	}

	/**
	 * 비밀번호 확인
	 * @param parameterMap
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value = "/viewPassProc.do")
	public String viewPassProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		int fnIdx = attrVO.getFnIdx();
		String ajaxPName = attrVO.getAjaxPName();
		
		// 1.2 필수 parameter 검사
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String chkUrl = StringUtil.getString(parameterMap.get("chkUrl"));
		if(brdIdx <= 0 || StringUtil.isEmpty(chkUrl)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 비밀번호 session 삭제
		String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
		HttpSession session = request.getSession(true);
		session.removeAttribute(pwdSKey);
		
		String pwd = StringUtil.getString(parameterMap.get("pwd"));
		if(StringUtil.isEmpty(pwd)) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required"), rbsMessageSource.getMessage("item.board.password"))));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		String encPwd = DataSecurityUtil.getDigest(pwd);
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명

		
		int dbPwdCnt = 0;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxColumnName, brdIdx));
		param.put("searchList", searchList);
		int reLevel = boardService.getReLevel(fnIdx, param);
		if(reLevel > 1) {
			// 답글인 경우
			boolean isNoMemberWRT = AuthHelper.isNoMemberAuthPage("WRT");
			boolean isNoMemberRWT = AuthHelper.isNoMemberAuthPage("RWT");
			if(isNoMemberWRT && isNoMemberRWT) {
				// 비회원 글쓰기, 비회원 답글쓰기 : 해당글, 부모글 비밀번호 확인
				searchList = new ArrayList<DTForm>();
				param.put("checkType", "2");
				param.put("brdIdx", brdIdx);
			} else if(isNoMemberWRT && !isNoMemberRWT) {
				// 비회원 글쓰기, 회원 답글쓰기 : 부모글 비밀번호 확인
				searchList = new ArrayList<DTForm>();
				param.put("checkType", "1");
				param.put("brdIdx", brdIdx);
			} /*else {
				// 해당글 비밀번호 확인
			}*/
		}

		searchList.add(new DTForm("A.PWD", encPwd));
		param.put("searchList", searchList);

		dbPwdCnt = boardService.getPwdCnt(fnIdx, param);
		//if(dbPwd == null || !StringUtil.isEquals(encPwd, dbPwd)) {
		if(dbPwdCnt <= 0) {
			// 해당글의 비밀번호가 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.board.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 비밀번호 session 생성
		session.setAttribute(pwdSKey, encPwd);
		
		if(StringUtil.indexOf(chkUrl, "&dataNm=") < 0) model.addAttribute("message", MessageUtil.getAjaxAlert("", "top.location.href='" + chkUrl + "'"));
		//model.addAttribute("message", MessageUtil.getParentLocationHref(isAjax, chkUrl));
		return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
	}

	/**
	 * 비밀번호 확인
	 * @param parameterMap
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method=RequestMethod.POST, value = "/passProc.do")
	public String passProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		boolean isAjax = attrVO.isAjax();
		JSONObject settingInfo = attrVO.getSettingInfo();
		int fnIdx = attrVO.getFnIdx();
		String ajaxPName = attrVO.getAjaxPName();
		
		// 1.2 필수 parameter 검사
		int brdIdx = StringUtil.getInt(parameterMap.getInt(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		int memIdx = StringUtil.getInt(parameterMap.getInt(JSONObjectUtil.getString(settingInfo, "memo_idx_name")), 0);
		String chkUrl = StringUtil.getString(parameterMap.get("chkUrl"));
		if(brdIdx <= 0 || StringUtil.isEmpty(chkUrl) || (StringUtil.isEquals(StringUtil.substring(chkUrl, 0, 2), "mo") && memIdx <= 0)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 비밀번호 session 삭제
		String pwdSKey = "bpwd_" + fnIdx + "_" + brdIdx;
		if(memIdx > 0) pwdSKey += "_" + memIdx;
		HttpSession session = request.getSession(true);
		session.removeAttribute(pwdSKey);
		
		String pwd = StringUtil.getString(parameterMap.get("pwd"));
		if(StringUtil.isEmpty(pwd)) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required"), rbsMessageSource.getMessage("item.board.password"))));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		String designTypeMemo = RbsProperties.getProperty("Globals.design.NAME_DESIGN_TYPE_MEMO");
		String boardDesignType = JSONObjectUtil.getString(settingInfo, "design_type", "default");
		
		String encPwd = DataSecurityUtil.getDigest(pwd);
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		String idxMemColumnName = JSONObjectUtil.getString(settingInfo, "memo_idx_column");		// key column명
		
		int dbPwdCnt = 0;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		if(StringUtil.isEquals(boardDesignType, designTypeMemo)) {
			searchList.add(new DTForm("A." + idxColumnName, brdIdx));
			param.put("boardDesignType", designTypeMemo);
		} else {
			searchList.add(new DTForm("A." + idxColumnName, brdIdx));
			if(memIdx > 0) {
				searchList.add(new DTForm("A." + idxMemColumnName, memIdx));
				param.put("boardDesignType", designTypeMemo);
			}
		}
		searchList.add(new DTForm("A.PWD", encPwd));
		param.put("searchList", searchList);

		dbPwdCnt = boardService.getPwdCnt(fnIdx, param);
		//if(dbPwd == null || !StringUtil.isEquals(encPwd, dbPwd)) {
		if(dbPwdCnt <= 0) {
			// 해당글의 비밀번호가 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.board.no.auth")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 비밀번호 session 생성
		session.setAttribute(pwdSKey, encPwd);
		
		if(memIdx > 0 || StringUtil.isEquals(boardDesignType, designTypeMemo)) {
			model.addAttribute("message", MessageUtil.getAjaxAlert("", "$.ajax({beforeSend:function(request){request.setRequestHeader('Ajax', 'true');}, url:'" + chkUrl + "', success:function(data){$('#fn_infoPwd').dialog('close');eval(data.replace(/<br\\/>/g, '\\\n'));}});"));
		}
		else if(StringUtil.indexOf(chkUrl, "&dataNm=") < 0) model.addAttribute("message", MessageUtil.getAjaxAlert("", "top.location.href='" + chkUrl + "'"));
		//model.addAttribute("message", MessageUtil.getParentLocationHref(isAjax, chkUrl));
		return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
	}
	
	/**
	 * 파일다운로드 사유 설정 정보
	 * @param attrVO
	 * @return
	 */
	private JSONObject getFileCmtSettingInfo(ModuleAttrVO attrVO) {
		/*JSONObject moduleSetting = attrVO.getModuleSetting();
		return JSONObjectUtil.getJSONObject(moduleSetting, "fileCmt_setting_info");*/
		return attrVO.getSettingInfo();
	}
	
	/**
	 * 파일다운로드 사유 항목 정보
	 * @param attrVO
	 * @return
	 */
	private JSONObject getFileCmtItemInfo(ModuleAttrVO attrVO) {
		JSONObject moduleItem = attrVO.getModuleItem();
		return JSONObjectUtil.getJSONObject(moduleItem, "fileCmt_item_info");
	}

	/**
	 * 파일다운로드 사유목록
	 * @param brdIdx
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/fileCmtList.do")
	public String fileCmtList(@RequestParam("brdIdx") int brdIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = getFileCmtSettingInfo(attrVO);
		JSONObject itemInfo = getFileCmtItemInfo(attrVO);
		request.setAttribute("settingInfo", settingInfo);
		request.setAttribute("itemInfo", itemInfo);
		int useFileCmt = JSONObjectUtil.getInt(settingInfo, "use_filecomt");
		if(useFileCmt != 1) {
			return RbsProperties.getProperty("Globals.error.404.path");
		}
		
		int fnIdx = attrVO.getFnIdx();
		DataForm queryString = attrVO.getQueryString();

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
		searchList.add(new DTForm("A.BRD_IDX", brdIdx));
		
		// 항목설정으로 검색조건 setting
		JSONObject listSearch = JSONObjectUtil.getJSONObject(itemInfo, "list_search");
		List<DTForm> itemInfoSearchList = ModuleUtil.getItemInfoDeleteSearchList("A.REGI_DATE", deleteListSearchParams, listSearch, queryString);
		if(itemInfoSearchList != null) {
			searchList.addAll(itemInfoSearchList);
			model.addAttribute("isSearchList", new Boolean(true));
		}
		param.put("searchList", searchList);

		// 2.2 목록수
    	totalCount = boardService.getFileCmtCount(fnIdx, param);
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = boardService.getFileCmtList(fnIdx, param);
    	}

    	// 3. 속성 setting
    	model.addAttribute("paginationInfo", paginationInfo);					// 페이징정보
    	model.addAttribute("list", list);										// 목록
    	model.addAttribute("searchOptnHashMap", CodeHelper.getItemOptnSearchHashMap(listSearch));	// 코드

    	// 4. 휴지통 경로
    	fn_setFileCmtListPath(attrVO);
    	
		return getViewPath("/fileCmtList");
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
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));	// 목록 검색 항목
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
		String memoListName = "moList.do";
		String memoInputName = "moInput.do";
		String memoInputProcName = "moInputProc.do";
		String memoDeleteProcName = "moDeleteProc.do";
		
		if(useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}
		
		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, null, null, pageName, idxName, listName, viewName, memoListName, memoInputName, memoInputProcName, memoDeleteProcName, inputName, inputProcName, deleteProcName, deleteListName, imageName, movieName, downloadName);

		// 추가경로
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
		
		String fileCmtListUrl = "fileCmtList.do";				// 파일다운로드 사유 목록
		if(!StringUtil.isEmpty(baseQueryString)) {
			fileCmtListUrl += baseQueryString;
		}
		request.setAttribute("URL_FILECMTLIST", fileCmtListUrl);
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

	/**
	 * 파일다운로드 사유 목록 경로 - 기본parameter에 brdIdx 포함
	 */
	public void fn_setFileCmtListPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = getFileCmtSettingInfo(attrVO);
		JSONObject itemInfo = getFileCmtItemInfo(attrVO);
		DataForm queryString = attrVO.getQueryString();
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");			// 목록 페이징  key

		// 항목 설정 정보
		String[] baseParams = StringUtil.addStringToArray(this.baseParams, "brdIdx");
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "fileCmtList.do");
	}
	
	public String fn_dsetInputNpname(JSONObject settingInfo){
		String dsetInputNpname = JSONObjectUtil.getString(settingInfo, "dset_input_npname");
		if(StringUtil.isEmpty(dsetInputNpname)){
			dsetInputNpname = "LIST";
			return dsetInputNpname;
		}
		
		return dsetInputNpname;
	}
	
	/*public List<DTForm> fn_getCateTabSearchList(DataForm queryString, JSONObject settingInfo, JSONObject items, HashMap<String, Object> optnHashMap){
		if(JSONObjectUtil.isEmpty(settingInfo) || JSONObjectUtil.isEmpty(items) || optnHashMap == null) return null;

		boolean isTotalCateTabUse = StringUtil.isEquals(JSONObjectUtil.getString(settingInfo, "use_cate_tab_total"), "1");	// 전체 탭 사용 true, 사용안함 false
		
		String cateTabItemId = JSONObjectUtil.getString(settingInfo, "dset_cate_tab_id");	// 탭 검색 item id
		String cateTabSearchName = null;
		String cateTabColumnName = null;	// 탭 검색 item 컬럼명
		String cateTabMasterCode = null;	// 탭 검색 item 마스터 코드
		String cateTabSearchValue = null;	// 탭 검색 값
		
		List<?> cateTabList = null;
		List<DTForm> cateTabSearchList = null;
		
		JSONObject cateTabItem = null;	// 탭 검색 item
		
		if(!StringUtil.isEmpty(cateTabItemId)){
			cateTabSearchName = RbsProperties.getProperty("Globals.item.tab.search.pre.flag") + cateTabItemId;
			cateTabItem = JSONObjectUtil.getJSONObject(items, cateTabItemId);
			cateTabSearchValue = queryString.getString(cateTabSearchName);
			if(!JSONObjectUtil.isEmpty(cateTabItem)){
				cateTabColumnName = JSONObjectUtil.getString(cateTabItem, "column_id");
				cateTabMasterCode = JSONObjectUtil.getString(cateTabItem, "master_code");
				if(!StringUtil.isEmpty(cateTabMasterCode)) cateTabList = (List<?>)optnHashMap.get(cateTabMasterCode);
				
				if(cateTabList == null) return null;
			}
			else return null;
		}
		else return null;
		
		if(StringUtil.isEmpty(cateTabColumnName) || StringUtil.isEmpty(cateTabMasterCode)) return null;
		
		if(!isTotalCateTabUse && StringUtil.isEmpty(cateTabSearchValue)){
			DataMap dm = (DataMap)cateTabList.get(0);
			if(dm != null){
				cateTabSearchValue = StringUtil.getString(dm.get("OPTION_CODE"));
				queryString.setObject(cateTabSearchName, cateTabSearchValue);
			}
		}
		
		if(!StringUtil.isEmpty(cateTabSearchValue)){
			if(cateTabSearchList == null) cateTabSearchList = new ArrayList<DTForm>();
			cateTabSearchList.add(new DTForm("A." + cateTabColumnName, cateTabSearchValue));
		}
		
		return cateTabSearchList;
	}*/
}
