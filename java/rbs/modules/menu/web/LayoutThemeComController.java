package rbs.modules.menu.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;

import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.FilenameFilter;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import egovframework.rte.fdl.property.EgovPropertyService;

public abstract class LayoutThemeComController extends LayoutComController{
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/**
	 * 목록조회
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 */
	public String list(String siteType, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		DataForm queryString = attrVO.getQueryString();
		
		JSONObject moduleSetting = attrVO.getModuleSetting();
		
		// 1. 필수 parameter 검사
		/* 선택 모듈의 기능 목록 */
		settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "layoutTheme_setting_info");
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
		
		/*
		// 2. DB
		// 선택 모듈 상세 정보
		Map<String, Object> verParam = new HashMap<String, Object>();
		List<DTForm> verSearchList = new ArrayList<DTForm>();
		verSearchList.add(new DTForm("A.SITE_ID", siteId));
		verParam.put("searchList", verSearchList);
		DataMap verDt = websiteService.getModify(verParam);
		
		if(verDt == null) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.website.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		*/
		int totalCount = 0;
		List<String> list = null;
		
		HttpSession session = request.getSession(true);
		String pathSeparator = "/";
		//String siteType = StringUtil.getString(verDt.get("SITE_TYPE"));
		String layoutResourcePath = RbsProperties.getProperty("Globals.layout.resource.path");
		layoutResourcePath += pathSeparator + siteType + RbsProperties.getProperty("Globals.layout.theme.resource.path");
		
		String layoutPath = session.getServletContext().getRealPath(layoutResourcePath);

		File path = new File(layoutPath);
		
		if(path != null && path.isDirectory()) {
			int offset = 0;				// 목록 select 시작 row번호
			int limitNumber = 0;		// 목록 select 갯수
			
			File[] designs = path.listFiles(new FilenameFilter());
			totalCount = designs.length;
			paginationInfo.setTotalRecordCount(totalCount);
			
			if(totalCount > 0) {
				list = new ArrayList<String>();
				offset = paginationInfo.getFirstRecordIndex();
				limitNumber = paginationInfo.getLastRecordIndex();
				if(limitNumber > totalCount) limitNumber = totalCount;
				for(int i = offset ; i < limitNumber ; i ++) {
					list.add(designs[i].getName());
				}
			}
		}
		
		model.addAttribute("settingInfo", settingInfo);
    	model.addAttribute("paginationInfo", paginationInfo);							// 페이징정보
    	model.addAttribute("list", list);												// 목록
    	model.addAttribute("queryString", queryString);
    	model.addAttribute("listImgPath", layoutResourcePath);						// 이미지 경로
		    	
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/layoutThemeList");
	}
	
	/**
	 * 수정
	 * @param mode
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public String view(String siteType, String designName, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();

		// 1. 필수 parameter 검사
		
		// 2. DB
		// 선택 모듈 상세 정보
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "layoutTheme_setting_info");
		request.setAttribute("settingInfo", settingInfo);

		/*
		Map<String, Object> verParam = new HashMap<String, Object>();
		List<DTForm> verSearchList = new ArrayList<DTForm>();
		verSearchList.add(new DTForm("A.SITE_ID", siteId));
		verParam.put("searchList", verSearchList);
		DataMap verDt = websiteService.getModify(verParam);

		if(verDt == null) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.website.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
*/
		int totalCount = 0;
		List<String> list = null;
		
		HttpSession session = request.getSession(true);
		String pathSeparator = "/";

		//String siteType = StringUtil.getString(verDt.get("SITE_TYPE"));
		String layoutResourcePath = RbsProperties.getProperty("Globals.layout.resource.path");
		layoutResourcePath += pathSeparator + siteType + RbsProperties.getProperty("Globals.layout.theme.resource.path");
		layoutResourcePath += pathSeparator + designName + "/simages";
		
		String moduleDesignPath = session.getServletContext().getRealPath(layoutResourcePath);

		File path = new File(moduleDesignPath);
		if(path != null && path.isDirectory()) {
			int offset = 0;				// 목록 select 시작 row번호
			int limitNumber = 0;		// 목록 select 갯수
			
			File[] designs = path.listFiles(new FilenameFilter());
			totalCount = designs.length;
			
			if(totalCount > 0) {
				list = new ArrayList<String>();
				limitNumber = totalCount;
				for(int i = offset ; i < limitNumber ; i ++) {
					list.add(designs[i].getName());
				}
			}
		}

		model.addAttribute("settingInfo", settingInfo);
		model.addAttribute("list", list);												// 목록
		//model.addAttribute("queryString", queryString);
    	model.addAttribute("listImgPath", layoutResourcePath);						// 이미지 경로
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/layoutThemeView");
	}
	
	/**
	 * 기본경로
	 */
	public abstract void fn_setCommonPath(ModuleAttrVO attrVO);
}
