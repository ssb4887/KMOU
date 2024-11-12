package rbs.adm.dashboard.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;
import com.woowonsoft.egovframework.web.NonModuleController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.adm.dashboard.service.DashboardService;
import rbs.egovframework.WebsiteVO;

@Controller
@RequestMapping("/{admSiteId}/dashboard")
public class DashboardController extends NonModuleController{
	
	public String getViewModulePath() {
		return "/dashboard";
	}
	
	@Resource(name = "admDashboardService")
	private DashboardService dashboardService;
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

	@RequestMapping("/dashboard.do")
	public String admDashBoard(HttpServletRequest request, ModelMap model, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO) {
		
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
		
		
		fn_setCommonPath(attrVO);
		return getViewPath("/dashboard");
	}
	

	/**
	 * 관리자 사이트 대시보드 차트 데이터 조회
	 * 검색 건수 추이
	 */
	@RequestMapping(value="/selectChartData1.json", headers="ajax")
	public ModelAndView selectChartData1(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		// 1번 차트 (검색 건추 추이)
		List<Object> chart1Data = dashboardService.selectChart1Data(param);
		model.addAttribute("chart1Data", chart1Data);
		return mav;
	}
	/**
	 * 관리자 사이트 대시보드 차트 데이터 조회
	 * 검색어 통계
	 */
	@RequestMapping(value="/selectChartData2.json", headers="ajax")
	public ModelAndView selectChartData2(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		String searchStDt = request.getParameter("startDate");
		String searchEdDt = request.getParameter("endDate");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate endDate = LocalDate.parse(searchEdDt, formatter);
	    endDate = endDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = endDate.format(formatter);

		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);
		
		// 2번 차트 (검색어 통계)
		List<Object> chart2Data = dashboardService.selectChart2Data(param);
		model.addAttribute("chart2Data", chart2Data);
		
		return mav;
	}
	/**
	 * 관리자 사이트 대시보드 차트 데이터 조회
	 * 로그인 통계
	 */
	@RequestMapping(value="/selectChartData3.json", headers="ajax")
	public ModelAndView selectChartData3(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();

		// 3번 차트 (로그인 통계)
		List<Object> chart3Data = dashboardService.selectChart3Data(param);
		model.addAttribute("chart3Data", chart3Data);
		return mav;
	}
	/**
	 * 관리자 사이트 대시보드 차트 데이터 조회
	 * 메뉴별 접속자 수
	 */
	@RequestMapping(value="/selectChartData4.json", headers="ajax")
	public ModelAndView selectChartData4(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		String searchStDt = request.getParameter("startDate");
		String searchEdDt = request.getParameter("endDate");
		
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate endDate = LocalDate.parse(searchEdDt, formatter);
	    endDate = endDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = endDate.format(formatter);

		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);
		
		// 4번 차트 (메뉴별 접속자 수)
		List<Object> chart4Data = dashboardService.selectChart4Data(param);
		model.addAttribute("chart4Data", chart4Data);

		return mav;
	}
	/**
	 * 관리자 사이트 대시보드 차트 데이터 조회
	 * 찜현황 통계(전체)
	 */
	@RequestMapping(value="/selectChartData5.json", headers="ajax")
	public ModelAndView selectChartData5(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		String searchStDt = request.getParameter("startDate");
		String searchEdDt = request.getParameter("endDate");

		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);
		
		// 5번 차트 (찜현황 통계(전체))
		List<Object> chart5Data = dashboardService.selectChart5Data(param);
		model.addAttribute("chart5Data", chart5Data);

		return mav;
	}
	/**
	 * 관리자 사이트 대시보드 차트 데이터 조회
	 * 해시태그 키워드 통계
	 */
	@RequestMapping(value="/selectChartData6.json", headers="ajax")
	public ModelAndView selectChartData6(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, Object> param = new HashMap<String, Object>();
		
		String searchStDt = request.getParameter("startDate");
		String searchEdDt = request.getParameter("endDate");
		
		//between 시 00:00:00에 이후는 안가져오기때문에 endDate를 강제로 하루 올린다.
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate endDate = LocalDate.parse(searchEdDt, formatter);
	    endDate = endDate.plusDays(1);

	    // 다시 문자열로 변환
	    searchEdDt = endDate.format(formatter);
		

		param.put("startDate", searchStDt);
		param.put("endDate", searchEdDt);
		
		// 6번 차트 (해시태그 키워드 통계)
		List<Object> chart6Data = dashboardService.selectChart6Data(param);
		model.addAttribute("chart6Data", chart6Data);
		
		return mav;
	}

	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String baseQueryString = StringUtil.getString(request.getAttribute("baseQueryString"));

		if (StringUtils.isEmpty(baseQueryString)) baseQueryString = "";

		request.setAttribute("baseQueryString", baseQueryString);
		request.setAttribute("URL_SELECT_CHART_DATA1", "selectChartData1.json?mId=1");
		request.setAttribute("URL_SELECT_CHART_DATA2", "selectChartData2.json?mId=1");
		request.setAttribute("URL_SELECT_CHART_DATA3", "selectChartData3.json?mId=1");
		request.setAttribute("URL_SELECT_CHART_DATA4", "selectChartData4.json?mId=1");
		request.setAttribute("URL_SELECT_CHART_DATA5", "selectChartData5.json?mId=1");
		request.setAttribute("URL_SELECT_CHART_DATA6", "selectChartData6.json?mId=1");
	}
}
