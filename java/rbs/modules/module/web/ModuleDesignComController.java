package rbs.modules.module.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;

import rbs.modules.module.service.ModuleService;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.FilenameFilter;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleSearchController;

import egovframework.rte.fdl.property.EgovPropertyService;

public abstract class ModuleDesignComController extends ModuleSearchController{

	@Resource(name = "moduleService")
	private ModuleService moduleService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	/**
	 * 목록조회
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 */
	public String list(String moduleId, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		DataForm queryString = attrVO.getQueryString();
		
		JSONObject moduleSetting = attrVO.getModuleSetting();
		//String confSModule = attrVO.getConfSModule();
		
		//String contentsGubun = request.getParameter("contentsGubun");
		String designType = request.getParameter("designType");

		boolean isContents = false;
		boolean isBoard = false;
		// 1. 필수 parameter 검사
		String contentsModuleName = RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_CONTENTS");
		String boardModuleName = RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_BOARD");
		isContents = StringUtil.isEquals(moduleId, contentsModuleName);
		isBoard = StringUtil.isEquals(moduleId, boardModuleName);
		
		/*if(isContents && StringUtil.isEmpty(contentsGubun)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		} else */if(isBoard && StringUtil.isEmpty(designType)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		/* 선택 모듈의 기능 목록 */
		settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "design_setting_info");
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
		// 선택 모듈 상세 정보
		Map<String, Object> moduleParam = new HashMap<String, Object>();
		List<DTForm> moduleSearchList = new ArrayList<DTForm>();
		moduleSearchList.add(new DTForm("A.MODULE_ID", moduleId));
		moduleParam.put("searchList", moduleSearchList);
		DataMap moduleDt = moduleService.getManageView(moduleParam);
		
		if(moduleDt == null) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		
		int totalCount = 0;
		List<String> list = null;
		
		HttpSession session = request.getSession(true);
		String pathSeparator = "/";
		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		String confSModule = null;
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		else confSModule = moduleId;
		int useSDesign = StringUtil.getInt(moduleDt.get("USE_SDESIGN"));
		String moduleResourcePath = RbsProperties.getProperty("Globals.module.resource.path");
		moduleResourcePath += pathSeparator + confModule;
		if(!StringUtil.isEmpty(confSModule)) moduleResourcePath += pathSeparator + confSModule;
		else if(!StringUtil.isEmpty(moduleId) && useSDesign == 1) moduleResourcePath += pathSeparator + moduleId;
		if(isBoard) moduleResourcePath += pathSeparator + designType;
		
		String moduleDesignPath = session.getServletContext().getRealPath(moduleResourcePath);

		File path = new File(moduleDesignPath);
		
		if(path != null && path.isDirectory()) {
			int offset = 0;				// 목록 select 시작 row번호
			int limitNumber = 0;		// 목록 select 갯수
			
			File[] designs = path.listFiles(new FilenameFilter());
			totalCount = designs.length;
			paginationInfo.setTotalRecordCount(totalCount);
			//System.out.println("-offset:" + totalCount + ":" + settingListCount + ":" +page);
			//System.out.println("-offset:" + offset);
			
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
    	model.addAttribute("listImgPath", moduleResourcePath);						// 이미지 경로
		    	
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/designList");
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
	public String view(String moduleId, String designName, ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		//String confSModule = attrVO.getConfSModule();
		
		String contentsGubun = request.getParameter("contentsGubun");
		String designType = request.getParameter("designType");

		boolean isContents = false;
		boolean isBoard = false;
		// 1. 필수 parameter 검사
		String contentsModuleName = RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_CONTENTS");
		String boardModuleName = RbsProperties.getProperty("Globals.design.NAME_MODULE_ID_BOARD");
		isContents = StringUtil.isEquals(moduleId, contentsModuleName);
		isBoard = StringUtil.isEquals(moduleId, boardModuleName);
		
		if(isContents && StringUtil.isEmpty(contentsGubun)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		} else if(isBoard && StringUtil.isEmpty(designType)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		// 선택 모듈 상세 정보
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "design_setting_info");
		request.setAttribute("settingInfo", settingInfo);
		
		Map<String, Object> moduleParam = new HashMap<String, Object>();
		List<DTForm> moduleSearchList = new ArrayList<DTForm>();
		moduleSearchList.add(new DTForm("A.MODULE_ID", moduleId));
		moduleParam.put("searchList", moduleSearchList);
		DataMap moduleDt = moduleService.getManageView(moduleParam);
		
		if(moduleDt == null) {
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.module.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		int totalCount = 0;
		List<String> list = null;
		
		HttpSession session = request.getSession(true);
		String pathSeparator = "/";

		String confModule = StringUtil.getString(moduleDt.get("CONF_MODULE"));
		String confSModule = null;
		if(StringUtil.isEmpty(confModule)) confModule = moduleId;
		else confSModule = moduleId;
		int useSDesign = StringUtil.getInt(moduleDt.get("USE_SDESIGN"));
		String moduleResourcePath = RbsProperties.getProperty("Globals.module.resource.path");
		moduleResourcePath += pathSeparator + confModule;
		if(!StringUtil.isEmpty(confSModule)) moduleResourcePath += pathSeparator + confSModule;
		else if(!StringUtil.isEmpty(moduleId) && useSDesign == 1) moduleResourcePath += pathSeparator + moduleId;
		if(isBoard) moduleResourcePath += pathSeparator + designType;
		moduleResourcePath += pathSeparator + designName + "/simages";
		
		String moduleDesignPath = session.getServletContext().getRealPath(moduleResourcePath);

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
    	model.addAttribute("listImgPath", moduleResourcePath);						// 이미지 경로
		
    	// 기본경로
    	fn_setCommonPath(attrVO);
    	
		return getViewPath("/designView");
	}
	
	/**
	 * 기본경로
	 */
	public abstract void fn_setCommonPath(ModuleAttrVO attrVO);
}
