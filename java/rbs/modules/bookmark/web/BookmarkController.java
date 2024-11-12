package rbs.modules.bookmark.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.LoginVO;
import rbs.egovframework.SukangLoginVO;
import rbs.modules.bookmark.service.BookmarkService;
import rbs.modules.code.serviceOra.CodeOptnServiceOra;

/**
 * 샘플모듈<br/>
 * : 통합관리시스템 > 메뉴콘텐츠관리, 통합관리시스템 > 기능등록관리, 사용자 사이트 에서 사용
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="bookmark")
@RequestMapping("/{siteId}/bookmark")
public class BookmarkController extends ModuleController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookmarkController.class);
	
	@Resource(name = "bookmarkService")
	private BookmarkService bookmarkService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	
	@Resource(name = "codeOptnServiceOra")
	protected CodeOptnServiceOra codeOptnServiceOra;
	
	@Resource(name = "jsonView")
	View jsonView;
	
	
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
	 * 찜 목록 가져오기(DB가 달라 JOIN으로 가져오지 못하니 따로 목록을 가져온다) 
	 * select
	 */
	@RequestMapping(value = "/getBookmarkList.do", method = RequestMethod.POST)
	public ModelAndView getBookmarkList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		String menuFg = reqJsonObj.get("menuFg").toString();
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		
		Map<String, Object> param = new HashMap<String, Object>();	
		
		param.put("STUDENT_NO", memberId);
		param.put("MENU_FG", menuFg);
		List<Object> bookmarkList = null;
		bookmarkList = bookmarkService.getBookmarkList(param);					
		
		mav.setView(jsonView);
		mav.addObject("bookmarkList", bookmarkList.size() < 1 ? null : bookmarkList);


		
		return mav;
	}	
	
	/**
	 * 찜하기 수 조회(마이페이지)
	 * select
	 */
	@RequestMapping(value = "/getMyLoveCount.do", method = RequestMethod.POST)
	public ModelAndView getMyLoveCount(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		
		Map<String, Object> param = new HashMap<String, Object>();	
		
		param.put("STUDENT_NO", memberId);
		
		List<Object> countList = null;
		countList = bookmarkService.getMyLoveCount(param);	
		mav.setView(jsonView);
		mav.addObject("countList", countList.size() < 1 ? null : countList);


		
		return mav;
	}
	
	/**
	 * 찜하기 탭 목록(마이페이지)
	 * 
	 */
	@RequestMapping(value = "/getBookmarkTabList.do", method = RequestMethod.POST)
	public ModelAndView getBookmarkTabList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		
		JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
		String menuFg = reqJsonObj.get("menuFg").toString();
		
		// 1. 페이지정보 setting
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int listUnit = 6;	// 페이지당 목록 수
		int listMaxUnit = 10;	// 최대 페이지당 목록 수 
		int listUnitStep = 6;	// 페이지당 목록 수 증가값
		
		//int recordCountPerPage = 5;	
		int pageSize = 10;			//페이징 리스트의 사이즈(페이징을 1부터 몇까지 보여줄껀지)
		int pageUnit = 6;			//한 페이지에 게시되는 게시물 건수(표 데이터 갯수)
		int page = StringUtil.getInt(reqJsonObj.get("page").toString(), 1); // 현재 페이지 index
		int limit = 6;
		int offset = (page - 1) * 6;
		// 나의 찜 탭별 목록 증가수
		if(StringUtil.isEquals(menuFg, "prof")) {
			listUnit = 12;
			listUnitStep = 12;
			pageUnit = 12;
			limit = 12;
			offset = (page - 1) * 12;
		} else if(StringUtil.isEquals(menuFg, "studPlan")) {
			listUnit = 12;
			listUnitStep = 12;
			pageUnit = 12;
			limit = 12;
			offset = (page - 1) * 12;
		}
		

		paginationInfo.setUnitBeginCount(listUnit);
		paginationInfo.setUnitEndCount(listMaxUnit);
		paginationInfo.setUnitStep(listUnitStep);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(pageUnit );	//한 페이지에 게시되는 게시물 건수
		paginationInfo.setPageSize(pageSize); 						//페이징 리스트의 사이즈
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("STUDENT_NO", memberId);
		param.put("MENU_FG", menuFg);
		param.put("LIMIT", limit);
		param.put("OFFSET", offset);
		
		// 2.2 목록수
    	int totalCount = bookmarkService.getBookmarkTypeCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
		List<Object> bookmarkTabList = null;
		
		if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
		}
		
		bookmarkTabList = bookmarkService.getBookmarkTabList(param);	
		
		/*if(menuFg == "sbjt") {
			bookmarkTabList = bookmarkService.getBookmarkSbjtList(param);	
		} else if(menuFg == "lecture") {
			bookmarkTabList = bookmarkService.getBookmarkLecList(param);	
		} else if(menuFg == "nonSbjt") {
			bookmarkTabList = bookmarkService.getBookmarkNonSbjtList(param);	
		} else if(menuFg == "prof") {
			bookmarkTabList = bookmarkService.getBookmarkProfList(param);	
		} else if(menuFg == "major") {
			bookmarkTabList = bookmarkService.getBookmarkMajorList(param);	
		} else if(menuFg == "stdPlan") {
			bookmarkTabList = bookmarkService.getBookmarkStdPlanList(param);	
		}
			*/

		System.out.println("line234");
		
		mav.setView(jsonView);
		mav.addObject("bookmarkTabList", bookmarkTabList.size() < 1 ? null : bookmarkTabList);
		mav.addObject("paginationInfo", paginationInfo);	// 페이징정보
										

		System.out.println("line241");
		return mav;
	}
	
	/**
	 * 찜 등록
	 * insert
	 */
	@RequestMapping(value = "/insertBookmark.do", method = RequestMethod.POST)
	public ModelAndView insertBookmark(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
			String docId = reqJsonObj.get("docId").toString();
			
			int result = bookmarkService.insertBookmark(docId, request);
			mav.setView(jsonView);
			mav.addObject("result", "DONE");
		}catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());	
		}

		
		return mav;
	}
	
	/**
	* 찜 삭제
	* insert
	*/
	@RequestMapping(value = "/deleteBookmark.do", method = RequestMethod.POST)
	public ModelAndView deleteBookmark(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {			
			String bookmarkJson = "";
			JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
			String docId = reqJsonObj.get("docId").toString();
			
			int result = bookmarkService.deleteBookmark(docId, request);
			mav.setView(jsonView);
			mav.addObject("result", "DONE");
		}catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());	
		}	
		
		return mav;
	}
	
	/**
	* 찜 삭제
	* insert
	*/
	@RequestMapping(value = "/deleteSelectedBookmark.do", method = RequestMethod.POST)
	public ModelAndView deleteSelectedBookmark(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {			
			String bookmarkJson = "";
			JSONObject reqJsonObj = JSONObject.fromObject(rawBody);
			String data = reqJsonObj.get("docId").toString();
			//String trimmedData = data.substring(2, data.length() - 2);
			String[] docIdList = data.split(",");
					
			int result = bookmarkService.deleteSelectedBookmark(docIdList, request);
			mav.setView(jsonView);
			mav.addObject("result", "DONE");
		}catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());	
		}	
		
		return mav;
	}
	
	
	/**
	* 찜 전체삭제(선택 탭만)
	* insert
	*/
	@RequestMapping(value = "/deleteAllBookmark.do", method = RequestMethod.POST)
	public ModelAndView deleteAllBookmark(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			int result = bookmarkService.deleteAllBookmark(request);
			
			mav.setView(jsonView);
			mav.addObject("result", "DONE");
		}catch(Exception e) {
			mav.setView(jsonView);
			mav.addObject("result", "FAIL");				
			mav.addObject("error", e.getClass().getName());	
		}
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

		fn_setCommonAddPath(request, attrVO);
	}
	
	public void fn_setCommonAddPath(HttpServletRequest request, @ModuleAttr ModuleAttrVO attrVO) {
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));
		
	}

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

}
