package rbs.modules.nonSbjt.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import rbs.egovframework.LoginVO;
import rbs.egovframework.util.ApiUtil;
import rbs.modules.nonSbjt.service.NonSbjtService;
import rbs.modules.search.service.SearchService;
import rbs.usr.main.service.MainService;

/**
 * 비교과
 * @author 이동근
 *
 */
@Controller
@ModuleMapping(moduleId="nonSbjt")
@RequestMapping({"/{siteId}/nonSbjt", "/{admSiteId}/menuContents/{usrSiteId}/nonSbjt"})
public class NonSbjtController extends ModuleController {
	
	private static final Logger logger = LoggerFactory.getLogger(NonSbjtController.class);
	
	@Resource(name = "nonSbjtService")
	private NonSbjtService nonSbjtService;
	
	@Resource(name = "usrMainService")
	private MainService mainService;
	
	@Resource(name="searchService")
	private SearchService searchService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;	
	
	@Resource(name = "jsonView")
	View jsonView;			
	
	
	/**
	 * 목록조회
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="LST")
	@RequestMapping(value = "/list.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		boolean isAdmMode = attrVO.isAdmMode(); // 관리자면 true, 사용자면 false

		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return getViewPath("/list");
	}	
	
	
	/**
	 * 비교과 검색 ajax
	 * select
	 */
	@RequestMapping(value = "/nonSbjtSearch.do", method = RequestMethod.POST)
	public ModelAndView nonSbjtSearch(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model, @RequestBody String rawBody) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		//세션 사용
		HttpSession session = request.getSession();
		session = request.getSession(true);
	
		//request raw body
		JSONObject rawJsonObj = JSONObject.fromObject(rawBody);
		JSONObject reqJsonObj = rawJsonObj.getJSONObject("data");
		
		String orderBy = rawJsonObj.getString("ORDER_BY");
		String loggingYn = rawJsonObj.getString("LOGGING_YN");
		
		//빈값검색에서 사용할 필터(myBatis에서 빈값검색이 아닐때는 사용하지 않도록 해둠)
		param.put("ORDER_BY", orderBy);
		param.put("MAIN_CATEGORY",reqJsonObj.getString("main_category"));
		param.put("SUB_CATEGORY",reqJsonObj.getString("sub_category"));
		param.put("SIGNIN_END_DATE",reqJsonObj.getString("sign_in_end_date"));
		param.put("SIGNIN_END_DATE1",reqJsonObj.getString("sign_in_end_date1").equals("") ? "" : addOneDay(reqJsonObj.getString("sign_in_end_date1")));
		param.put("START_DATE",reqJsonObj.getString("start_date"));
		param.put("START_DATE1",reqJsonObj.getString("start_date1").equals("") ? "" : addOneDay(reqJsonObj.getString("start_date1")));
		param.put("METHOD",reqJsonObj.getString("method"));
		param.put("PROGRAM_TYPE",reqJsonObj.getString("program_type"));
		
		String url = RbsProperties.getProperty("Globals.search.url");
		String endpointNonSbjt = RbsProperties.getProperty("Globals.search.endpoint.non-subject");
		
		//response값(raw)
		String responseData = null;
		responseData = ApiUtil.getRestApi(url, endpointNonSbjt, ApiUtil.METHOD_POST, reqJsonObj);
		
		

		//response값 json 변환
    	JSONObject jsonResponseData = null;
    	JSONArray resultArray = null;
    	String isEmptySearch = rawJsonObj.getString("IS_EMPTY_SEARCH");
    	int searchTotalCount = 0;
        List<Object> responseDataList = null;

        if(isEmptySearch.equals("N")) {
        	//log insert
        	if(loggingYn.equals("Y")) {          		
        		searchService.insertSearchLog(endpointNonSbjt, reqJsonObj, responseData, request);
        	}
        	if(responseData.length() > 0) {        		
        		jsonResponseData =  JSONObject.fromObject(responseData);
        		resultArray = jsonResponseData.getJSONObject("data").getJSONArray("result");
        		searchTotalCount = jsonResponseData.getJSONObject("data").getInt("total_count");
        		
        		//검색어가 있을때(서치API검색)
        		// in 조건 쿼리를 위한 idList('idx_tidx')
        		List<String> idList = new ArrayList<>();
        		
        		// result 배열에서 id 값 추출
        		for (int i = 0; i < resultArray.size(); i++) {
        			JSONObject item = resultArray.getJSONObject(i);
        			String id = item.getString("id");
        			idList.add(id);
        		}
        		
        		param.put("idList", idList);  
        	}
        }
        param.put("IS_EMPTY_SEARCH",isEmptySearch);
    	if(isEmptySearch.equals("N")) {   
  		
    	}else {
    		//검색어가 없을때(DB전체검색)
    		//세션에 담긴 비교과 추천결과를 담을 배열
    		List<Map<String, Object>> recommendedKeys = (List<Map<String, Object>>) session.getAttribute("nonSbjtRecommendKeys");
            if (recommendedKeys != null && !recommendedKeys.isEmpty()) {
                List<String> keys = new ArrayList<>();
                List<Integer> ranks = new ArrayList<>();
                for (Map<String, Object> item : recommendedKeys) {
                    keys.add((String) item.get("key"));
                    ranks.add((Integer) item.get("rank"));
                }
                param.put("RECOMMENDED_KEYS", keys);
                param.put("RECOMMENDED_RANKS", ranks);
            }    		
    	}
    	
    	
    	
		//전체 갯수 설정
		DataMap rawTotCnt = nonSbjtService.getInitNonSbjtListCount(param);
		int totalCount = (isEmptySearch.equals("N")) ? searchTotalCount : ((Long) rawTotCnt.get("TOTAL_COUNT")).intValue();
    	
    	//페이징 설정
    	RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
    	int listUnit = 8;	// 페이지당 목록 수
    	int listMaxUnit = 5;	// 최대 페이지 수 
    	int listUnitStep = 10;	// 페이지당 목록 수 증가값
    	
    	int pageUnit = 8;
    	int pageSize = 10;	
    	
    	String pageNum = reqJsonObj.get("page_num").toString();
    	//필터를 쿼리 조건 방식으로 했을때의 페이징 설정
//    	String pageNum = rawJsonObj.getString("PAGE_NUM");
    	
    	int currentPage = StringUtil.getInt(pageNum) + 1; // 현재 페이지 index
    	
//    	int totalCount = (isEmptySearch.equals("N")) ?  JSONObject.fromObject(jsonResponseData).getJSONObject("data").getInt("total_count") : totCnt;
    	//필터를 쿼리 조건 방식으로 했을때의 페이징 설정
//    	int totalCount = totCnt;
    	
    	paginationInfo.setUnitBeginCount(listUnit);
    	paginationInfo.setUnitEndCount(listMaxUnit);
    	paginationInfo.setUnitStep(listUnitStep);
    	paginationInfo.setCurrentPageNo(currentPage);
    	paginationInfo.setRecordCountPerPage(pageUnit);
    	paginationInfo.setPageSize(pageSize);
    	paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", (isEmptySearch.equals("N")) ? 0 : paginationInfo.getFirstRecordIndex() );
    		//필터를 쿼리 조건 방식으로 했을때의 페이징 설정
//    		param.put("firstIndex", paginationInfo.getFirstRecordIndex() );
    		
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		responseDataList = nonSbjtService.getNonSbjtList(param) ;
    	}
    	
    	mav.addObject("responseData", responseDataList);
    	mav.addObject("totalCount", totalCount);
    	mav.addObject("paginationInfo", paginationInfo);

		
		mav.setView(jsonView);

	
		return mav;
	}	
	

	/**
	 * 비교과 상세조회
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="VEW")
	@RequestMapping(value = "/view.do")
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {		
		
		Map<String, Object> param = new HashMap<String, Object>();
		HttpSession session = request.getSession(true); 
		LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
		String memberId = loginVO.getMemberId();
		
		String idx = "";
		String tidx = "";
		
		idx = request.getParameter("idx");
		tidx = request.getParameter("tidx");
		
		param.put("idx",idx);
		param.put("tidx",tidx);
		param.put("STUDENT_NO", memberId);
		
		
		/*비교과 정보 상세조회*/
		DataMap nonSbjtInfo = null;
		nonSbjtInfo = nonSbjtService.getNonsbjtInfo(param);
		
		/*비교과 프로그램이력 조회*/
		List<Object> nonSbjtHist = null;
		nonSbjtHist = nonSbjtService.getNonSbjtHist(param);
		
		/*개인 비교과 신청이력 조회*/
		List<Object> myNonSbjtSigninHist = null;
		myNonSbjtSigninHist = nonSbjtService.getMyNonSbjtSigninHist(param);
		
		
		/*비교과 태그(키워드) 조회*/
		List<Object> tagList = null;
		tagList = nonSbjtService.getNonSbjtTag(param);
		
		
		/*비교과 핵심역량지수 조회*/
		List<Object> essentialList = null;
		essentialList = nonSbjtService.getNonSbjtEssential(param);
		JSONArray essentialJson = JSONArray.fromObject(essentialList);
		
		/*나의 핵심역량지수 조회*/
		
		List<Object> userEssentialList = null;
		userEssentialList = mainService.getCoreCompDiagnosis(param);
//		JSONArray userEssentialJson = JSONArray.fromObject(userEssentialList);
		
		
		/*첨부파일 및 컨텐츠 내용*/
		//비교과  기본정보에서 content컬럼의 files를 뽑아온 후 처리
        String content = (String) nonSbjtInfo.get("CONTENT");
        //비교과 기본정보에서 content 뽑아온 후 처리
        String contentText = null;
        if(content != null) {        	
        	JSONObject jsonObject = JSONObject.fromObject(content);
        	JSONArray filesArray = jsonObject.getJSONArray("files");
        	
        	List<Integer> filesList = new ArrayList<>();
        	if(filesArray.size() > 1) {        	
        		List<Object> attachmentFileList = null;
        		for (int i = 0; i < filesArray.size(); i++) {
        			filesList.add(filesArray.getInt(i));
        		}
        		param.put("filesList",filesList);
        		
        		attachmentFileList = nonSbjtService.getNonSbjtAttachmentFile(param);
        		model.addAttribute("attachmentFileList", (attachmentFileList.size() < 1) ? null : attachmentFileList);
        	}
        	
        	
        	/*콘텐츠 내용*/
        	contentText = jsonObject.getString("text");
        	
        }
        
        
        model.addAttribute("info", (nonSbjtInfo.size() < 1) ? null : nonSbjtInfo);
        model.addAttribute("hist", (nonSbjtHist.size() < 1) ? null : nonSbjtHist);
        model.addAttribute("myHist", (myNonSbjtSigninHist.size() < 1) ? null : myNonSbjtSigninHist);
        model.addAttribute("tagList", (tagList.size() < 1) ? null : tagList);
        model.addAttribute("essentialList", (essentialList.size() < 1) ? null : essentialJson.toString());		
//        model.addAttribute("userEssentialList", (userEssentialList.size() < 1) ? null : userEssentialJson.toString());
        model.addAttribute("USER_CORE_DIAGNOSIS", (userEssentialList.size() < 1) ? null : userEssentialList);
        model.addAttribute("contentText", contentText);
	
		// 4. 기본경로
		fn_setCommonPath(attrVO);
		
		return getViewPath("/view");
	}		
	
	/**
	 * 비교과 핵심역량 분류
	 * select
	 */
	@RequestMapping(value = "/getCategory.do")
	public ModelAndView getCategory(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("parent", request.getParameter("parent") == null ? "" : request.getParameter("parent"));
		
		List<Object> categoryList = null;
		categoryList = nonSbjtService.getCategory(param);
		
		mav.setView(jsonView);
		mav.addObject("categoryList", (categoryList.size() < 1) ? null : categoryList);

		
		return mav;
	}	
	
	/**
	 * 비교과 교육형식
	 * select
	 */
	@RequestMapping(value = "/getProgramType.do")
	public ModelAndView getProgramType(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		List<Object> programTypeList = null;
		programTypeList = nonSbjtService.getProgramType();
		
		mav.setView(jsonView);
		mav.addObject("PROGRAM_TYPE_LIST", (programTypeList.size() < 1) ? null : programTypeList);
		
		
		return mav;
	}
	
	/**
	 * 비교과 운영방식
	 * select
	 */
	@RequestMapping(value = "/getMethod.do")
	public ModelAndView getMethod(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		List<Object> methodList = null;
		methodList = nonSbjtService.getMethod();
		
		mav.setView(jsonView);
		mav.addObject("METHOD_LIST", (methodList.size() < 1) ? null : methodList);
		
		
		return mav;
	}	
		
	/**
	 * 비교과 태그
	 * select
	 */
	@RequestMapping(value = "/getTag.do")
	public ModelAndView getTag(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		List<Object> tagList = null;
		tagList = nonSbjtService.getTag();
		
		mav.setView(jsonView);
		mav.addObject("tagList", (tagList.size() < 1) ? null : tagList);
		
		
		return mav;
	}	
	
	
	/**
	 * OceanCTS 접속 토큰 발급
	 */
	@RequestMapping(value = "/getToken.do", method = RequestMethod.POST)
	public ModelAndView getToken(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
	    ModelAndView mav = new ModelAndView("jsonView");
	    
	    HttpSession session = request.getSession(true); 
	    LoginVO loginVO = (LoginVO)session.getAttribute("loginVO");
	    
	    String baseUrl = RbsProperties.getProperty("Globals.nonSbjt.token.url");
	    baseUrl += RbsProperties.getProperty("Globals.nonSbjt.token.endpoint");
	    String auth = RbsProperties.getProperty("Globals.nonSbjt.token.param.auth");
	    String haksa = loginVO.getMemberId();
	    
        // URL 생성 (쿼리 파라미터 포함)
        String urlStr = baseUrl + "?auth=" + URLEncoder.encode(auth, "UTF-8") 
                               + "&haksa=" + URLEncoder.encode(haksa, "UTF-8");
        URL url = new URL(urlStr);
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("HTTP 오류 코드: " + conn.getResponseCode());
        }
        
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        String output;
        StringBuilder response = new StringBuilder();
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        
        conn.disconnect();
        
        JSONObject jsonResponse = (JSONObject) JSONSerializer.toJSON(response.toString());
        String token = jsonResponse.optString("token");
        
        mav.addObject("success", true);
        mav.addObject("token", token);

	
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
	
		//북마크 등록
		request.setAttribute("URL_INPUTCOURBKPROC", "inputCourBKProc.json" + baseQueryString);
		request.setAttribute("URL_INPUTLECTUREBKPROC", "inputLectureBKProc.json" + baseQueryString);
		
		//상세화면내의 조회
		request.setAttribute("URL_CLASSVIEW", "classView.json" + baseQueryString);
		request.setAttribute("URL_PLANVIEW", "planView.do" + baseQueryString);
		request.setAttribute("URL_NANOVIEW", "nanoView.json" + baseQueryString);
		request.setAttribute("URL_MATIRXVIEW", "matrixView.json" + baseQueryString);
		request.setAttribute("URL_STATVIEW", "statView.json" + baseQueryString);
		request.setAttribute("URL_PROFVIEW", "/web/prof/view.do?mId=43");
		
		request.setAttribute("URL_SEARCHUNIVLIST", "searchUnivList.json" + baseQueryString);
		request.setAttribute("URL_SEARCHSUBJECTLIST", "searchSubjectList.json" + baseQueryString);

		
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
	
	
	private String addOneDay(String dateStr) {
	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(sdf.parse(dateStr));
	        cal.add(Calendar.DAY_OF_MONTH, 1);
	        return sdf.format(cal.getTime());
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return dateStr; // 파싱 오류 시 원래 문자열 반환
	    }
	}

}
