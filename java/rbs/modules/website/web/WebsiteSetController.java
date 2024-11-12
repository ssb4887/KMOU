package rbs.modules.website.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.FormValidatorUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import rbs.egovframework.WebsiteVO;
import rbs.modules.module.service.ModuleFnService;
import rbs.modules.website.service.WebsiteService;

@Controller
@RequestMapping("/{admSiteId}/website")
@ModuleMapping(moduleId="websiteSet", confModule="website")
public class WebsiteSetController extends ModuleController{

	@Resource(name = "websiteService")
	protected WebsiteService websiteService;
	
	@Resource(name = "moduleFnService")
	private ModuleFnService moduleFnService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@RequestMapping("/setting.do")
	public String setting(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
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
		
		// 1.2 필수 parameter 검사
		if(StringUtil.isEmpty(siteId)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		String idxColumnName = JSONObjectUtil.getString(settingInfo, "idx_column");		// key column명
		DataMap dt = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + idxColumnName, siteId));
		param.put("searchList", searchList);

		// 2.1 상세정보
		dt = websiteService.getModify(param);
		if(dt == null) {
			// 해당글이 없는 경우
			model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.contents")));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 3. 항목설정
		String submitType = "setting";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 4. 속성 setting
		model.addAttribute("dt", dt);															// 상세정보
		
		
		HashMap<String, Object> optnHashMap = CodeHelper.getItemOptnHashMap(items, itemOrder);
		
		// 배너, 팝업
		List<Object> bannerList = getModuleFnList("banner");
		List<Object> popupList = getModuleFnList("popup");
		if(bannerList != null) {
			if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
			optnHashMap.put("banner_fn_idx", bannerList);
		}
		if(popupList != null) {
			if(optnHashMap == null) optnHashMap = new HashMap<String, Object>();
			optnHashMap.put("popup_fn_idx", popupList);
		}
		model.addAttribute("optnHashMap", optnHashMap);		// 항목코드
		model.addAttribute("submitType", submitType);														// 페이지구분

    	// 5. 기본경로
    	fn_setCommonPath(attrVO);
    	
    	// 6. form action
    	model.addAttribute("URL_SUBMITPROC", request.getAttribute("URL_IDX_MODIFYPROC"));
		
		return getViewPath("/setting");
	}

	/**
	 * 수정처리
	 * @param mode
	 * @param parameterMap
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/settingProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		String uploadModulePath = attrVO.getUploadModulePath();
		
		// 1. 필수 parameter 검사
		WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();

		if(StringUtil.isEmpty(siteId)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}

		// 2. 항목설정
		String submitType = "setting";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		JSONArray fileItemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_fileproc_order");
		
		// 3. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 4. DB
    	int result = websiteService.update(1, uploadModulePath, siteId, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder, fileItemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 5. 기본경로
        	fn_setCommonPath(attrVO);
			model.addAttribute("message", MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "fn_procReload();"));
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
	 * 기능 목록
	 * @param moduleId
	 * @return
	 */
	public List<Object> getModuleFnList(String moduleId) {
		List<Object> list = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.MODULE_ID", moduleId));	
		param.put("searchList", searchList);
		list = moduleFnService.getOptnList(param);
		
		return list;
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
		//PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);

		String inputName = "setting.do";
		String inputProcName = "settingProc.do";
		String imageName = "image.do";
		String downloadName = "download.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, null, null, pageName, idxName
				, null, null, inputName, inputProcName
				, null, null
				, imageName, downloadName, null);
	}
}
