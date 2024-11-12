package rbs.modules.menu.web;

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

import rbs.modules.menu.service.MenuPointService;

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
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ImportModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@RequestMapping("/{siteId}/menu")
@ModuleMapping(moduleId="menuPoint", confModule="menu", confSModule="point")
public class MenuPointInputController extends ImportModuleController{
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@Resource(name="menuPointService")
	MenuPointService menuPointService;

	@RequestMapping("/pointInput.do")
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		String siteId = attrVO.getSiteId();
		int verIdx = JSONObjectUtil.getInt(siteInfo, "version");
		int menuIdx = StringUtil.getInt(request.getParameter("mId"));
		if(StringUtil.isEmpty(siteId) || verIdx <= 0 || menuIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		DataMap dt = null;
		JSONObject crtMenu = attrVO.getCrtMenu();
		int usePoll = JSONObjectUtil.getInt(crtMenu, "use_poll");
		if(usePoll != 1) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		searchList.add(new DTForm("A.MENU_IDX", menuIdx));
		
		param.put("searchList", searchList);
		
		dt = menuPointService.getTotalView(param);
		
		model.addAttribute("menuPointDt", dt);
		
		JSONObject itemInfo = attrVO.getItemInfo();
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");

		// 2. 속성 setting
		model.addAttribute("optnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));		// 항목코드
		model.addAttribute("submitType", submitType);											// 페이지구분

		
    	// 4. 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/pointInput");
	}
	
	@ModuleAuth(name="WRT")
	@RequestMapping(method=RequestMethod.POST, value = "pointInputProc.do")
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model)  throws Exception {
		
		boolean isAdmMode = attrVO.isAdmMode();
		boolean isAjax = attrVO.isAjax();
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		
		JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		String siteId = attrVO.getSiteId();
		int verIdx = JSONObjectUtil.getInt(siteInfo, "version");
		int menuIdx = StringUtil.getInt(request.getParameter("mId"));
		if(StringUtil.isEmpty(siteId) || verIdx <= 0 || menuIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		JSONObject crtMenu = attrVO.getCrtMenu();
		int usePoll = JSONObjectUtil.getInt(crtMenu, "use_poll");
		if(usePoll != 1) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 1. 항목설정
		String submitType = "write";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "proc_order");
		
		// 2. 필수입력 체크
		String errorMessage = FormValidatorUtil.getErrorMessage(ajaxPName, parameterMap, moduleValidator, new Object[]{isAdmMode, submitType, items, itemOrder, settingInfo});
		if(!StringUtil.isEmpty(errorMessage)) {
			model.addAttribute("message", errorMessage);
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}

		// 3. DB
    	int result = menuPointService.insert(siteId, verIdx, menuIdx, request.getRemoteAddr(), parameterMap, settingInfo, items, itemOrder);
    	if(result > 0) {
    		// 저장 성공
        	// 4. 기본경로
			model.addAttribute("message",   MessageUtil.getAlertAddWindow(isAjax, rbsMessageSource.getMessage("message.insert"), "location.reload();"));
			return RbsProperties.getProperty("Globals.message" + ajaxPName + ".path");
    	} else if(result == -2) {
    		// 중복코드
    		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.menu.point.already.insert")));
    		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
    	}
		// 저장 실패
		model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.no.insert")));
		return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
	}
	
	/**
	 * 기본경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		DataForm queryString = attrVO.getQueryString();
		
		String inputName = "pointInput.do";
		String inputProcName = "pointInputProc.do";
		PathUtil.fn_setInputPath(queryString, baseParams, inputName, inputProcName);
	}
}
