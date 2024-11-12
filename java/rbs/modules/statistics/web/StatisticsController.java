package rbs.modules.statistics.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
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
import rbs.modules.statistics.service.StatisticsService;

/**
 * 통계모듈
 * @author 이동근
 *
 */
@Controller
@ModuleMapping(moduleId="statistics")
@RequestMapping({"/{siteId}/statistics", "/{admSiteId}/menuContents/{usrSiteId}/statistics"})
public class StatisticsController extends ModuleController {
	
	@Resource(name = "statisticsService")
	private StatisticsService statisticsService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;	
	
	@Resource(name = "jsonView")
	View jsonView;
	
/*------------------------------검색통계 시작 --------------------------*/	
	/**
	 * 검색 건수 추이
	 * 화면
	 */
	@RequestMapping(value = "/srchCntStat.do")
	public String srchCntStat(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		fn_setCommonPath(attrVO);
		return getViewPath("/srchCntStat");
	}
	
	/**
	 * 검색 건수 추이
	 * 조회
	 */
	@RequestMapping(value = "/srchCntStatData.do", headers="ajax")
	public ModelAndView srchCntStatData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		model.addAttribute("STATUS", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
//		Map<String, Object> param = CommonUtil.convertSearchParameterToHashMap(request);

		String statsType = request.getParameter("searchStatsType") == null ? "" : request.getParameter("searchStatsType");
		String statsDate1 = request.getParameter("searchStatsDate1") == null ? "" : request.getParameter("searchStatsDate1");
		String statsDate2 = request.getParameter("searchStatsDate2") == null ? "" : request.getParameter("searchStatsDate2");
		String statsDate3 = request.getParameter("searchStatsDate3") == null ? "" : request.getParameter("searchStatsDate3");

		if (StringUtils.isEmpty(statsType)) {
			model.addAttribute("msg", "검색 조건을 설정해 주세요.");
			return mav;
		} else {
			// MariaDB 형식에 맞게 변경
			if (statsType.equals("1")) {
			    param.put("dateFormat1", "'%Y'");
			}
			if (statsType.equals("2")) {
			    param.put("dateFormat1", "'%Y-%m'");
			    param.put("dateFormat2", "'%Y'");
			}
			if (statsType.equals("3")) {
			    param.put("dateFormat1", "'%Y-%m-%d'");
			    param.put("dateFormat2", "'%Y%m'");
			}
			if (statsType.equals("4")) {
			    param.put("dateFormat1", "'%Y%m%d:%H'");
			    param.put("dateFormat2", "'%Y%m%d'");
			}

			
			param.put("date", statsDate1 + statsDate2 + statsDate3);

			List<Object> chartData = statisticsService.getSearchCountList(param);
			model.addAttribute("STATUS", 1);
			model.addAttribute("CHART_DATA", chartData);

			return mav;
		}
	}
	
	/**
	 * 검색 사용자 추이 화면
	 */
	@RequestMapping(value = "/srchUsrStat.do")
	public String srchUsrStat(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		fn_setCommonPath(attrVO);
		return getViewPath("/srchUsrStat");
	}
	
	/**
	 * 검색 사용자 추이
	 * 조회
	 */
	@RequestMapping(value = "/srchUsrStatData.do", headers="ajax")
	public ModelAndView srchUsrStatData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("STATUS", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String searchStDt = request.getParameter("searchStDt");
		String searchEdDt = request.getParameter("searchEdDt");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate endDate = LocalDate.parse(searchEdDt, formatter);
	    endDate = endDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = endDate.format(formatter);


		param.put("searchStDt", searchStDt);
		param.put("searchEdDt", searchEdDt);
		
		List<Object> userData = statisticsService.getSearchUserList(param);
		List<Object> gradeData = statisticsService.getSearchGradeList(param);
		model.addAttribute("STATUS", 1);
		model.addAttribute("USER_DATA", userData);
		model.addAttribute("GRADE_DATA", gradeData);
		
		return mav;
	}	
	
	/**
	 * 검색어 통계 화면
	 */
	@RequestMapping(value = "/srchKeywordStat.do")
	public String srchKeywordStat(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
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
		
		// 2.1 검색조건
		String selRange = StringUtil.getString(parameterMap.get("SEL_RANGE"));
		String startDate = StringUtil.getString(parameterMap.get("searchStDt"));
		String endDate = StringUtil.getString(parameterMap.get("searchEdDt"));
		String userType = StringUtil.getString(parameterMap.get("searchUserType"));
		String keyword = StringUtil.getString(parameterMap.get("searchKeyword"));
		String today = "";
		String oneMonthAgo = "";
		
		String searchList = "&SEL_RANGE="+selRange+"&searchUserType="+userType+"&searchKeyword="+keyword;
		
		param.put("SEL_RANGE", selRange);
		
		if(startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			searchList += "&searchStDt="+startDate+"&searchEdDt="+endDate;
			//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
		    DateTimeFormatter endDtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    LocalDate formatterEndDate = LocalDate.parse(endDate, endDtformatter);
		    formatterEndDate = formatterEndDate.plusDays(1);
	
		    // 다시 문자열로 변환
		    endDate = formatterEndDate.format(endDtformatter);			
		    
			param.put("START_DATE", startDate);
			param.put("END_DATE", endDate);
		} else {
			searchList += "&searchStDt="+oneMonthAgo+"&searchEdDt="+today;
			LocalDate now = LocalDate.now();
			LocalDate oneMonth = now.minusMonths(1);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					
			today = now.format(formatter);
			oneMonthAgo = oneMonth.format(formatter);
			
			param.put("START_DATE", oneMonthAgo);
			param.put("END_DATE", today);
		}
		
		if(userType != null && !userType.isEmpty()) {
			param.put("USER_TYPE", userType);
		}
		
		if(keyword != null && !keyword.isEmpty()) {
			param.put("KEYWORD", keyword.replaceAll(" ", ""));
		}
		
		// 2.2 목록수
    	totalCount = statisticsService.getSearchKeywordCount(param);    	
		paginationInfo.setTotalRecordCount(totalCount);
    	
    	if(totalCount > 0) {
    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
    		
    		// 2.3 목록
    		list = statisticsService.getSearchKeywordList(param);
    	}
    	
    	model.addAttribute("END_DATE", today);								
    	model.addAttribute("START_DATE", oneMonthAgo);					
    	model.addAttribute("PAGINATIONINFO", paginationInfo);					// 페이징정보
    	model.addAttribute("LIST", list);										// 목록
    	model.addAttribute("searchList",searchList);
    	
    	System.out.println(searchList);
    	System.out.println(searchList);
    	System.out.println(searchList);
    	
    	fn_setCommonPath(attrVO);
		return getViewPath("/srchKeywordStat");
	}
	
/*------------------------------검색통계 끝 --------------------------*/
	
/*------------------------------검색만족도 시작 --------------------------*/
	
	/**
	 * 검색 건수 추이
	 * 화면
	 */
	@RequestMapping(value = "/srchPoint.do")
	public String srchPoint(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		// 파라미터 설정
		Map<String, Object> param = new HashMap<String, Object>();

		String selRange = request.getParameter("SEL_RANGE");
		String startDate = request.getParameter("searchStDt");
		String endDate = request.getParameter("searchEdDt");
		String pointType = request.getParameter("pointType");
		String point = request.getParameter("point");
		String keyword = request.getParameter("searchKeyword");
		String today = "";
		String oneMonthAgo = "";
		
		param.put("SEL_RANGE", selRange);
		
		if(startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
			//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
		    DateTimeFormatter endDtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    LocalDate formatterEndDate = LocalDate.parse(endDate, endDtformatter);
		    formatterEndDate = formatterEndDate.plusDays(1);
	
		    // 다시 문자열로 변환
		    endDate = formatterEndDate.format(endDtformatter);			
		    
			
			param.put("searchStDt", startDate);
			param.put("searchEdDt", endDate);
		} else {
			LocalDate now = LocalDate.now();
			LocalDate oneMonth = now.minusMonths(1);
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					
			today = now.format(formatter);
			oneMonthAgo = oneMonth.format(formatter);
			
			param.put("searchStDt", oneMonthAgo);
			param.put("searchEdDt", today);
		}
		
		if(pointType != null && !pointType.isEmpty()) {
			param.put("pointType", pointType);
		}
		
		if(point != null && !point.isEmpty()) {
			param.put("point", point);
		}
		
		if(keyword != null && !keyword.isEmpty()) {
			param.put("searchKeyword", keyword.replaceAll(" ", ""));
		}

		// 검색만족도 만족도 평균 조회
		double pointAvrg = statisticsService.selectSrchPointAvrg(param);

		// 검색만족도 만족도 그래프 조회
		List<?> pointGraph = statisticsService.selectSrchPointGraph(param);

		// 검색만족도 만족도 구분 그래프 조회
		List<?> pointTypeGraph = statisticsService.selectSrchPointTypeGraph(param);

		// 페이징 관련
		int page = StringUtil.getInt(request.getParameter("page"), 1);

		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		paginationInfo.setUnitBeginCount(10);
		paginationInfo.setUnitEndCount(10);
		paginationInfo.setUnitStep(10);
		paginationInfo.setCurrentPageNo(page);
		paginationInfo.setRecordCountPerPage(10);
		paginationInfo.setPageSize(10);

		JSONObject itemInfo = attrVO.getItemInfo();
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, "list_order");
		
		int totalCount = 0;
		List<?> pointList = null;
		
		// 조회 카운트
		totalCount = statisticsService.selectSrchPointCount(param);
		paginationInfo.setTotalRecordCount(totalCount);
		
		if(totalCount > 0) {
			param.put("firstIndex", paginationInfo.getFirstRecordIndex());
			param.put("lastIndex", paginationInfo.getLastRecordIndex());
			param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
			
			pointList = statisticsService.selectSrchPointList(param);
		}

		model.addAttribute("endDate", today);								
    	model.addAttribute("startDate", oneMonthAgo);	
		model.addAttribute("list", pointList);
		model.addAttribute("pointAvrg", pointAvrg);
		model.addAttribute("pointGraph", pointGraph);
		model.addAttribute("pointTypeGraph", pointTypeGraph);
		model.addAttribute("paginationInfo", paginationInfo);

    	fn_setCommonPath(attrVO);
		return getViewPath("/srchPoint");
	}
/*------------------------------검색만족도 끝 --------------------------*/	
	
/*------------------------------찜 통계 시작 --------------------------*/
	
	/**
	 * 찜 현황 > 교수 > 교수 찜수
	 * 화면
	 */
	@RequestMapping(value = "/profBmk.do")
	public String profBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		fn_setCommonPath(attrVO);
		return getViewPath("/profBmk");
	}

	/**
	 * 찜 현황 > 교수 > 교수 인기찜
	 * 화면
	 */
	@RequestMapping(value = "/profMstBmk.do")
	public String profMstBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/profMstBmk");
	}

	/**
	 * 찜 현황 > 교수 > 교수 찜수
	 * 조회
	 */
	@RequestMapping(value = "/profBmkData.json", headers="ajax")
	public ModelAndView profBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String statsType = request.getParameter("searchStatsType") == null ? "" : request.getParameter("searchStatsType");
		String statsDate1 = request.getParameter("searchStatsDate1") == null ? "" : request.getParameter("searchStatsDate1");
		String statsDate2 = request.getParameter("searchStatsDate2") == null ? "" : request.getParameter("searchStatsDate2");
		String statsDate3 = request.getParameter("searchStatsDate3") == null ? "" : request.getParameter("searchStatsDate3");
		String date = "";

		if (StringUtils.isEmpty(statsType)) {
			model.addAttribute("msg", "검색 조건을 설정 해주세요.");
			return mav;
		} else {
			if (statsType.equals("1")) {
				param.put("showDateFormat", "'%Y'");
			}
			if (statsType.equals("2")) {
				param.put("showDateFormat", "'%Y-%m'");
				param.put("dateFormat", "'%Y'");
				date = statsDate1;
			}
			if (statsType.equals("3")) {
				param.put("showDateFormat", "'%Y-%m-%d'");
				param.put("dateFormat", "'%Y%m'");
				date = statsDate1 + statsDate2;
			}
			if (statsType.equals("4")) {
				param.put("showDateFormat", "'%Y%m%d:%H'");
				param.put("dateFormat", "'%Y%m%d'");
				date = statsDate1 + statsDate2 + statsDate3;
			}
			param.put("date", date);
			param.put("menu_fg", "prof");

			List<Object> chartData = statisticsService.selectBmk(param);
			model.addAttribute("status", 1);
			model.addAttribute("chartData", chartData);

			return mav;
		}
	}

	/**
	 * 찜 현황 > 교수 > 교수 인기찜
	 * 조회
	 */
	@RequestMapping(value = "/profMstBmkData.json", headers="ajax")
	public ModelAndView profMstBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String searchStDt = request.getParameter("searchStDt") == null ? "" : request.getParameter("searchStDt");
		String searchEdDt = request.getParameter("searchEdDt") == null ? "" : request.getParameter("searchEdDt");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter endDtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate formatterEndDate = LocalDate.parse(searchEdDt, endDtformatter);
	    formatterEndDate = formatterEndDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = formatterEndDate.format(endDtformatter);	
		
		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);
		
		List<Object> chartData = statisticsService.selectProfMstBmk(param);
		model.addAttribute("status", 1);
		model.addAttribute("chartData", chartData);

		return mav;
	}
	
	/**
	 * 찜 현황 > 비교과 > 비교과 찜수
	 * 화면
	 */
	@RequestMapping(value = "/nonSbjtBmk.do")
	public String nonSbjtBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		fn_setCommonPath(attrVO);
		return getViewPath("/nonSbjtBmk");
	}
	
	/**
	 * 찜 현황 > 비교과 > 비교과 인기찜
	 * 화면
	 */
	@RequestMapping(value = "/nonSbjtMstBmk.do")
	public String nonSbjtMstBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		fn_setCommonPath(attrVO);
		return getViewPath("/nonSbjtMstBmk");
	}
	
	/**
	 * 찜 현황 > 비교과 > 비교과 찜수
	 * 조회
	 */
	@RequestMapping(value = "/nonSbjtBmkData.json", headers="ajax")
	public ModelAndView nonSbjtBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String statsType = request.getParameter("searchStatsType") == null ? "" : request.getParameter("searchStatsType");
		String statsDate1 = request.getParameter("searchStatsDate1") == null ? "" : request.getParameter("searchStatsDate1");
		String statsDate2 = request.getParameter("searchStatsDate2") == null ? "" : request.getParameter("searchStatsDate2");
		String statsDate3 = request.getParameter("searchStatsDate3") == null ? "" : request.getParameter("searchStatsDate3");
		String date = "";

		if (StringUtils.isEmpty(statsType)) {
			model.addAttribute("msg", "검색 조건을 설정 해주세요.");
			return mav;
		} else {
			if (statsType.equals("1")) {
				param.put("showDateFormat", "'%Y'");
			}
			if (statsType.equals("2")) {
				param.put("showDateFormat", "'%Y-%m'");
				param.put("dateFormat", "'%Y'");
				date = statsDate1;
			}
			if (statsType.equals("3")) {
				param.put("showDateFormat", "'%Y-%m-%d'");
				param.put("dateFormat", "'%Y%m'");
				date = statsDate1 + statsDate2;
			}
			if (statsType.equals("4")) {
				param.put("showDateFormat", "'%Y%m%d:%H'");
				param.put("dateFormat", "'%Y%m%d'");
				date = statsDate1 + statsDate2 + statsDate3;
			}
			param.put("date", date);
			param.put("menu_fg", "nonSbjt");

			List<Object> chartData = statisticsService.selectBmk(param);
			model.addAttribute("status", 1);
			model.addAttribute("chartData", chartData);

			return mav;
		}
	}
	
	/**
	 * 찜 현황 > 비교과 > 비교과 인기찜
	 * 조회
	 */
	@RequestMapping(value = "/nonSbjtMstBmkData.json", headers="ajax")
	public ModelAndView nonSbjtMstBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String searchStDt = request.getParameter("searchStDt") == null ? "" : request.getParameter("searchStDt");
		String searchEdDt = request.getParameter("searchEdDt") == null ? "" : request.getParameter("searchEdDt");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter endDtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate formatterEndDate = LocalDate.parse(searchEdDt, endDtformatter);
	    formatterEndDate = formatterEndDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = formatterEndDate.format(endDtformatter);	
		
		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);
		
		List<Object> chartData = statisticsService.selectNonSbjtMstBmk(param);
		model.addAttribute("status", 1);
		model.addAttribute("chartData", chartData);
		
		return mav;
	}

	/**
	 * 찜 현황 > 관심강좌 > 관심강좌 찜수
	 * 화면
	 */
	@RequestMapping(value = "/clsBmk.do")
	public String clsBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/clsBmk");
	}

	/**
	 * 찜 현황 > 관심강좌 > 관심강좌 인기찜
	 * 화면
	 */
	@RequestMapping(value = "/clsMstBmk.do")
	public String clsMstBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/clsMstBmk");
	}

	/**
	 * 찜 현황 > 관심강좌 > 관심강좌 찜수
	 * 조회
	 */
	@RequestMapping(value = "/clsBmkData.json", headers="ajax")
	public ModelAndView clsBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String statsType = request.getParameter("searchStatsType") == null ? "" : request.getParameter("searchStatsType");
		String statsDate1 = request.getParameter("searchStatsDate1") == null ? "" : request.getParameter("searchStatsDate1");
		String statsDate2 = request.getParameter("searchStatsDate2") == null ? "" : request.getParameter("searchStatsDate2");
		String statsDate3 = request.getParameter("searchStatsDate3") == null ? "" : request.getParameter("searchStatsDate3");
		String date = "";

		if (StringUtils.isEmpty(statsType)) {
			model.addAttribute("msg", "검색 조건을 설정 해주세요.");
			return mav;
		} else {
			if (statsType.equals("1")) {
				param.put("showDateFormat", "'%Y'");
			}
			if (statsType.equals("2")) {
				param.put("showDateFormat", "'%Y-%m'");
				param.put("dateFormat", "'%Y'");
				date = statsDate1;
			}
			if (statsType.equals("3")) {
				param.put("showDateFormat", "'%Y-%m-%d'");
				param.put("dateFormat", "'%Y%m'");
				date = statsDate1 + statsDate2;
			}
			if (statsType.equals("4")) {
				param.put("showDateFormat", "'%Y%m%d:%H'");
				param.put("dateFormat", "'%Y%m%d'");
				date = statsDate1 + statsDate2 + statsDate3;
			}
			

			param.put("date", date);
			param.put("menu_fg", "lecture");

			List<Object> chartData = statisticsService.selectBmk(param);
			model.addAttribute("status", 1);
			model.addAttribute("chartData", chartData);

			return mav;
		}

	}

	/**
	 * 찜 현황 > 관심강좌 > 관심강좌 인기찜
	 * 조회
	 */
	@RequestMapping(value = "/clsMstBmkData.json", headers="ajax")
	public ModelAndView clsMstBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String searchStDt = request.getParameter("searchStDt") == null ? "" : request.getParameter("searchStDt");
		String searchEdDt = request.getParameter("searchEdDt") == null ? "" : request.getParameter("searchEdDt");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter endDtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate formatterEndDate = LocalDate.parse(searchEdDt, endDtformatter);
	    formatterEndDate = formatterEndDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = formatterEndDate.format(endDtformatter);	
		
		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);

		List<Object> chartData = statisticsService.selectClsMstBmk(param);
		model.addAttribute("status", 1);
		model.addAttribute("chartData", chartData);

		return mav;
	}

	/**
	 * 찜 현황 > 전공 > 전공 찜수
	 * 화면
	 */
	@RequestMapping(value = "/majorBmk.do")
	public String majorBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/majorBmk");
	}

	/**
	 * 찜 현황 > 전공 > 전공 인기찜
	 * 화면
	 */
	@RequestMapping(value = "/majorMstBmk.do")
	public String majorMstBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/majorMstBmk");
	}

	/**
	 * 찜 현황 > 전공 > 전공 찜수
	 * 조회
	 */
	@RequestMapping(value = "/majorBmkData.json", headers="ajax")
	public ModelAndView majorBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String statsType = request.getParameter("searchStatsType") == null ? "" : request.getParameter("searchStatsType");
		String statsDate1 = request.getParameter("searchStatsDate1") == null ? "" : request.getParameter("searchStatsDate1");
		String statsDate2 = request.getParameter("searchStatsDate2") == null ? "" : request.getParameter("searchStatsDate2");
		String statsDate3 = request.getParameter("searchStatsDate3") == null ? "" : request.getParameter("searchStatsDate3");
		String date = "";

		if (StringUtils.isEmpty(statsType)) {
			model.addAttribute("msg", "검색 조건을 설정 해주세요.");
			return mav;
		} else {
			if (statsType.equals("1")) {
				param.put("showDateFormat", "'%Y'");
			}
			if (statsType.equals("2")) {
				param.put("showDateFormat", "'%Y-%m'");
				param.put("dateFormat", "'%Y'");
				date = statsDate1;
			}
			if (statsType.equals("3")) {
				param.put("showDateFormat", "'%Y-%m-%d'");
				param.put("dateFormat", "'%Y%m'");
				date = statsDate1 + statsDate2;
			}
			if (statsType.equals("4")) {
				param.put("showDateFormat", "'%Y%m%d:%H'");
				param.put("dateFormat", "'%Y%m%d'");
				date = statsDate1 + statsDate2 + statsDate3;
			}
			param.put("date", date);
			param.put("menu_fg", "major");

			List<Object> chartData = statisticsService.selectBmk(param);
			model.addAttribute("status", 1);
			model.addAttribute("chartData", chartData);

			return mav;
		}
	}

	/**
	 * 찜 현황 > 전공 > 전공 인기찜
	 * 조회
	 */
	@RequestMapping(value = "/majorMstBmkData.json", headers="ajax")
	public ModelAndView majorMstBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String searchStDt = request.getParameter("searchStDt") == null ? "" : request.getParameter("searchStDt");
		String searchEdDt = request.getParameter("searchEdDt") == null ? "" : request.getParameter("searchEdDt");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter endDtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate formatterEndDate = LocalDate.parse(searchEdDt, endDtformatter);
	    formatterEndDate = formatterEndDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = formatterEndDate.format(endDtformatter);	
		
		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);

		List<Object> chartData = statisticsService.selectMajorMstBmk(param);
		model.addAttribute("status", 1);
		model.addAttribute("chartData", chartData);

		return mav;
	}
	
	
	/**
	 * 찜 현황 > 전공·교양 >  전공·교양 찜수
	 * 화면
	 */
	@RequestMapping(value = "/sbjtBmk.do")
	public String sbjtBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/sbjtBmk");
	}

	/**
	 * 찜 현황 > 전공·교양 >  전공·교양 인기찜
	 * 화면
	 */
	@RequestMapping(value = "/sbjtMstBmk.do")
	public String sbjtMstBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/sbjtMstBmk");
	}	
	
	/**
	 * 찜 현황 > 전공·교양 >  전공·교양 찜수
	 * 조회
	 */
	@RequestMapping(value = "/sbjtBmkData.json", headers="ajax")
	public ModelAndView sbjtBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		String statsType = request.getParameter("searchStatsType") == null ? "" : request.getParameter("searchStatsType");
		String statsDate1 = request.getParameter("searchStatsDate1") == null ? "" : request.getParameter("searchStatsDate1");
		String statsDate2 = request.getParameter("searchStatsDate2") == null ? "" : request.getParameter("searchStatsDate2");
		String statsDate3 = request.getParameter("searchStatsDate3") == null ? "" : request.getParameter("searchStatsDate3");
		String date = "";
		
		if (StringUtils.isEmpty(statsType)) {
			model.addAttribute("msg", "검색 조건을 설정 해주세요.");
			return mav;
		} else {
			if (statsType.equals("1")) {
				param.put("showDateFormat", "'%Y'");
			}
			if (statsType.equals("2")) {
				param.put("showDateFormat", "'%Y-%m'");
				param.put("dateFormat", "'%Y'");
				date = statsDate1;
			}
			if (statsType.equals("3")) {
				param.put("showDateFormat", "'%Y-%m-%d'");
				param.put("dateFormat", "'%Y%m'");
				date = statsDate1 + statsDate2;
			}
			if (statsType.equals("4")) {
				param.put("showDateFormat", "'%Y%m%d:%H'");
				param.put("dateFormat", "'%Y%m%d'");
				date = statsDate1 + statsDate2 + statsDate3;
			}
			param.put("date", date);
			param.put("menu_fg", "sbjt");
			
			List<Object> chartData = statisticsService.selectBmk(param);
			model.addAttribute("status", 1);
			model.addAttribute("chartData", chartData);
			
			return mav;
		}
	}
	
	/**
	 * 찜 현황 > 전공·교양 >  전공·교양 인기찜
	 * 조회
	 */
	@RequestMapping(value = "/sbjtMstBmkData.json", headers="ajax")
	public ModelAndView sbjtMstBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		String searchStDt = request.getParameter("searchStDt") == null ? "" : request.getParameter("searchStDt");
		String searchEdDt = request.getParameter("searchEdDt") == null ? "" : request.getParameter("searchEdDt");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
		DateTimeFormatter endDtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate formatterEndDate = LocalDate.parse(searchEdDt, endDtformatter);
		formatterEndDate = formatterEndDate.plusDays(1);
		
		// 다시 문자열로 변환
		searchEdDt = formatterEndDate.format(endDtformatter);	
		
		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);
		
		List<Object> chartData = statisticsService.selectSbjtMstBmk(param);
		model.addAttribute("status", 1);
		model.addAttribute("chartData", chartData);
		
		return mav;
	}

	/**
	 * 찜 현황 > 학생설계융합전공 > 학생설계융합전공 찜수
	 * 화면
	 */
	@RequestMapping(value = "/studBmk.do")
	public String studBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/studBmk");
	}

	/**
	 * 찜 현황 > 학생설계융합전공 > 학생설계융합전공 인기찜
	 * 화면
	 */
	@RequestMapping(value = "/studMstBmk.do")
	public String studMstBmk(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/studMstBmk");
	}

	/**
	 * 찜 현황 > 학생설계융합전공 > 학생설계융합전공 찜수
	 * 조회
	 */
	@RequestMapping(value = "/studBmkData.json", headers="ajax")
	public ModelAndView studBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String statsType = request.getParameter("searchStatsType") == null ? "" : request.getParameter("searchStatsType");
		String statsDate1 = request.getParameter("searchStatsDate1") == null ? "" : request.getParameter("searchStatsDate1");
		String statsDate2 = request.getParameter("searchStatsDate2") == null ? "" : request.getParameter("searchStatsDate2");
		String statsDate3 = request.getParameter("searchStatsDate3") == null ? "" : request.getParameter("searchStatsDate3");
		String date = "";

		if (StringUtils.isEmpty(statsType)) {
			model.addAttribute("msg", "검색 조건을 설정 해주세요.");
			return mav;
		} else {
			if (statsType.equals("1")) {
				param.put("showDateFormat", "'%Y'");
			}
			if (statsType.equals("2")) {
				param.put("showDateFormat", "'%Y-%m'");
				param.put("dateFormat", "'%Y'");
				date = statsDate1;
			}
			if (statsType.equals("3")) {
				param.put("showDateFormat", "'%Y-%m-%d'");
				param.put("dateFormat", "'%Y%m'");
				date = statsDate1 + statsDate2;
			}
			if (statsType.equals("4")) {
				param.put("showDateFormat", "'%Y%m%d:%H'");
				param.put("dateFormat", "'%Y%m%d'");
				date = statsDate1 + statsDate2 + statsDate3;
			}
			param.put("date", date);
			param.put("menu_fg", "studPlan");

			List<Object> chartData = statisticsService.selectBmk(param);
			model.addAttribute("status", 1);
			model.addAttribute("chartData", chartData);

			return mav;
		}
	}

	/**
	 * 찜 현황 > 학생설계융합전공 > 학생설계융합전공 인기찜
	 * 조회
	 */
	@RequestMapping(value = "/studMstBmkData.json", headers="ajax")
	public ModelAndView studMstBmkData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String searchStDt = request.getParameter("searchStDt") == null ? "" : request.getParameter("searchStDt");
		String searchEdDt = request.getParameter("searchEdDt") == null ? "" : request.getParameter("searchEdDt");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter endDtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate formatterEndDate = LocalDate.parse(searchEdDt, endDtformatter);
	    formatterEndDate = formatterEndDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = formatterEndDate.format(endDtformatter);	
		
		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);

		List<Object> chartData = statisticsService.selectStudMstBmk(param);
		model.addAttribute("status", 1);
		model.addAttribute("chartData", chartData);

		return mav;
	}	
	
/*------------------------------찜 통계 끝 --------------------------*/
	
/*------------------------------해시태그 통계 끝 --------------------------*/	
	

	/**
	 * 통계 > 해시태그 > 해시태그수
	 * 화면
	 */
	@RequestMapping(value = "/sbjtHashtagCnt.do")
	public String sbjtHashtagCnt(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		fn_setCommonPath(attrVO);
		return getViewPath("/sbjtHashtagCnt");
	}

	/**
	 * 통계 > 해시태그 > 해시태그 사용자수
	 * 화면
	 */
	@RequestMapping(value = "/sbjtHashtagUsr.do")
	public String sbjtHashtagUsr(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
    	fn_setCommonPath(attrVO);
		return getViewPath("/sbjtHashtagUsr");
	}
	
	/**
	 * 통계 > 해시태그 > 해시태그 키워드 통계
	 * 화면
	 */
	@RequestMapping(value = "/sbjtHashtagStat.do")
	public String sbjtHashtagStat(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		fn_setCommonPath(attrVO);
		return getViewPath("/sbjtHashtagStat");
	}

	/**
	 * 해시태그 > 교과목 해시태그수
	 * 조회
	 */
	@RequestMapping(value = "/sbjtHashtagCntData.json", headers="ajax")
	public ModelAndView sbjtHashtagCntData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String statsType = request.getParameter("searchStatsType") == null ? "" : request.getParameter("searchStatsType");
		String statsDate1 = request.getParameter("searchStatsDate1") == null ? "" : request.getParameter("searchStatsDate1");
		String statsDate2 = request.getParameter("searchStatsDate2") == null ? "" : request.getParameter("searchStatsDate2");
		String statsDate3 = request.getParameter("searchStatsDate3") == null ? "" : request.getParameter("searchStatsDate3");
		String date = "";

		if (StringUtils.isEmpty(statsType)) {
			model.addAttribute("msg", "검색 조건을 설정 해주세요.");
			return mav;
		} else {
			if (statsType.equals("1")) {
				param.put("showDateFormat", "'%Y'");
			}
			if (statsType.equals("2")) {
				param.put("showDateFormat", "'%Y-%m'");
				param.put("dateFormat", "'%Y'");
				date = statsDate1;
			}
			if (statsType.equals("3")) {
				param.put("showDateFormat", "'%Y-%m-%d'");
				param.put("dateFormat", "'%Y%m'");
				date = statsDate1 + statsDate2;
			}
			if (statsType.equals("4")) {
				param.put("showDateFormat", "'%Y%m%d:%H'");
				param.put("dateFormat", "'%Y%m%d'");
				date = statsDate1 + statsDate2 + statsDate3;
			}
			param.put("date", date);

			List<Object> chartData = statisticsService.selectHashtagCnt(param);
			model.addAttribute("status", 1);
			model.addAttribute("chartData", chartData);

			return mav;
		}
	}

	/**
	 * 해시태그 > 교과목 해시태그 사용자수
	 * 조회
	 */
	@RequestMapping(value = "/sbjtHashtagUsrData.json", headers="ajax")
	public ModelAndView sbjtHashtagUsrData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String statsType = request.getParameter("searchStatsType") == null ? "" : request.getParameter("searchStatsType");
		String statsDate1 = request.getParameter("searchStatsDate1") == null ? "" : request.getParameter("searchStatsDate1");
		String statsDate2 = request.getParameter("searchStatsDate2") == null ? "" : request.getParameter("searchStatsDate2");
		String statsDate3 = request.getParameter("searchStatsDate3") == null ? "" : request.getParameter("searchStatsDate3");
		String date = "";

		if (StringUtils.isEmpty(statsType)) {
			model.addAttribute("msg", "검색 조건을 설정 해주세요.");
			return mav;
		} else {
			if (statsType.equals("1")) {
				param.put("showDateFormat", "'%Y'");
			}
			if (statsType.equals("2")) {
				param.put("showDateFormat", "'%Y-%m'");
				param.put("dateFormat", "'%Y'");
				date = statsDate1;
			}
			if (statsType.equals("3")) {
				param.put("showDateFormat", "'%Y-%m-%d'");
				param.put("dateFormat", "'%Y%m'");
				date = statsDate1 + statsDate2;
			}
			if (statsType.equals("4")) {
				param.put("showDateFormat", "'%Y%m%d:%H'");
				param.put("dateFormat", "'%Y%m%d'");
				date = statsDate1 + statsDate2 + statsDate3;
			}
			param.put("date", date);

			List<Object> chartData = statisticsService.selectHashtagUsr(param);
			model.addAttribute("status", 1);
			model.addAttribute("chartData", chartData);

			return mav;
		}
	}
	
	/**
	 * 통계 > 해시태그 > 해시태그 키워드 통계
	 * 조회
	 */
	@RequestMapping(value = "/sbjtHashtagStatData.json", headers="ajax")
	public ModelAndView sbjtHashtagStatData(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		model.addAttribute("status", 0);
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		String searchStDt = request.getParameter("searchStDt") == null ? "" : request.getParameter("searchStDt");
		String searchEdDt = request.getParameter("searchEdDt") == null ? "" : request.getParameter("searchEdDt");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter endDtformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate formatterEndDate = LocalDate.parse(searchEdDt, endDtformatter);
	    formatterEndDate = formatterEndDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = formatterEndDate.format(endDtformatter);	
		
		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);

		List<Object> chartData = statisticsService.selectHashtagStat(param);
		model.addAttribute("status", 1);
		model.addAttribute("chartData", chartData);

		return mav;
	}
	
/*------------------------------해시태그 통계 끝 --------------------------*/	
	


	
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

		if (useSsl){
			inputProcName = PathUtil.getSslPagePath(inputProcName);
		}

		PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, null, null, pageName, idxName, listName, viewName, null, null, null, null, inputName, inputProcName, deleteProcName, deleteListName, imageName, movieName, downloadName);

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		request.setAttribute("baseQueryString", baseQueryString);
		request.setAttribute("URL_SRCH_CNT_STAT", "srchCntStat.do" + baseQueryString);
		request.setAttribute("URL_SRCH_CNT_STAT_DATA", "srchCntStatData.do" + baseQueryString);
		request.setAttribute("URL_SRCH_USR_STAT", "srchUsrStat.do" + baseQueryString);
		request.setAttribute("URL_SRCH_USR_STAT_DATA", "srchUsrStatData.do" + baseQueryString);
		request.setAttribute("URL_SRCH_KEYWORD_STAT", "srchKeywordStat.do" + baseQueryString);
		request.setAttribute("URL_SRCH_KEYWORD_STAT_FORM", "srchKeywordStatForm.do" + baseQueryString);
		
		request.setAttribute("URL_PROF_BOOKMART", "profBmk.do" + baseQueryString);
		request.setAttribute("URL_PROF_BOOKMART_DATA", "profBmkData.json" + baseQueryString);
		request.setAttribute("URL_PROF_MOST_BOOKMART", "profMstBmk.do" + baseQueryString);
		request.setAttribute("URL_PROF_MOST_BOOKMART_DATA", "profMstBmkData.json" + baseQueryString);
		request.setAttribute("URL_NONSBJT_BOOKMART", "nonSbjtBmk.do" + baseQueryString);
		request.setAttribute("URL_NONSBJT_BOOKMART_DATA", "nonSbjtBmkData.json" + baseQueryString);
		request.setAttribute("URL_NONSBJT_MOST_BOOKMART", "nonSbjtMstBmk.do" + baseQueryString);
		request.setAttribute("URL_NONSBJT_MOST_BOOKMART_DATA", "nonSbjtMstBmkData.json" + baseQueryString);
		request.setAttribute("URL_CLASS_BOOKMART", "clsBmk.do" + baseQueryString);
		request.setAttribute("URL_CLASS_BOOKMART_DATA", "clsBmkData.json" + baseQueryString);
		request.setAttribute("URL_CLASS_MOST_BOOKMART", "clsMstBmk.do" + baseQueryString);
		request.setAttribute("URL_CLASS_MOST_BOOKMART_DATA", "clsMstBmkData.json" + baseQueryString);
		request.setAttribute("URL_MAJOR_BOOKMART", "majorBmk.do" + baseQueryString);
		request.setAttribute("URL_MAJOR_BOOKMART_DATA", "majorBmkData.json" + baseQueryString);
		request.setAttribute("URL_MAJOR_MOST_BOOKMART", "majorMstBmk.do" + baseQueryString);
		request.setAttribute("URL_MAJOR_MOST_BOOKMART_DATA", "majorMstBmkData.json" + baseQueryString);
		request.setAttribute("URL_SBJT_BOOKMART", "sbjtBmk.do" + baseQueryString);
		request.setAttribute("URL_SBJT_BOOKMART_DATA", "sbjtBmkData.json" + baseQueryString);
		request.setAttribute("URL_SBJT_MOST_BOOKMART", "sbjtMstBmk.do" + baseQueryString);
		request.setAttribute("URL_SBJT_MOST_BOOKMART_DATA", "sbjtMstBmkData.json" + baseQueryString);
		request.setAttribute("URL_STUDENT_BOOKMART", "studBmk.do" + baseQueryString);
		request.setAttribute("URL_STUDENT_BOOKMART_DATA", "studBmkData.json" + baseQueryString);
		request.setAttribute("URL_STUDENT_MOST_BOOKMART", "studMstBmk.do" + baseQueryString);
		request.setAttribute("URL_STUDENT_MOST_BOOKMART_DATA", "studMstBmkData.json" + baseQueryString);
		
		request.setAttribute("URL_HASHTAG_CNT", "sbjtHashtagCnt.do" + baseQueryString);
		request.setAttribute("URL_HASHTAG_USR", "sbjtHashtagUsr.do" + baseQueryString);
		request.setAttribute("URL_HASHTAG_STAT", "sbjtHashtagStat.do" + baseQueryString);
		request.setAttribute("URL_HASHTAG_CNT_DATA", "sbjtHashtagCntData.json" + baseQueryString);
		request.setAttribute("URL_HASHTAG_USR_DATA", "sbjtHashtagUsrData.json" + baseQueryString);
		request.setAttribute("URL_HASHTAG_STAT_DATA", "sbjtHashtagStatData.json" + baseQueryString);
	}
}
