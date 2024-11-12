package rbs.modules.menu.web;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rbs.egovframework.WebsiteVO;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;

@Controller
@RequestMapping("/{admSiteId}/menu")
@ModuleMapping(moduleId="version", confModule="website", confSModule="version")
public class LayoutThemeController extends LayoutThemeComController{
	
	/**
	 * 목록조회
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 */
	@ModuleAuth(name="MNG", accessModule={"menu", "website"})
	@RequestMapping("/layoutThemeList.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {

		String siteType = getSiteType(attrVO, request, model);

		if(StringUtil.isEmpty(siteType)) {
			boolean isAjax = attrVO.isAjax();
			String ajaxPName = attrVO.getAjaxPName();
			JSONObject moduleItem = attrVO.getModuleItem();
			JSONObject siteTypeItem = getSiteTypeItem(moduleItem);
			model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required.select"), ModuleUtil.getItemName(siteTypeItem))));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		return super.list(siteType, attrVO, request, model);
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
	@ModuleAuth(name="MNG", accessModule={"menu", "website"})
	@RequestMapping(value = "/layoutThemeView.do")
	public String view(@RequestParam(value="dg") String designName, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		String siteType = getSiteType(attrVO, request, model);

		if(StringUtil.isEmpty(siteType)) {
			boolean isAjax = attrVO.isAjax();
			String ajaxPName = attrVO.getAjaxPName();
			JSONObject moduleItem = attrVO.getModuleItem();
			JSONObject siteTypeItem = getSiteTypeItem(moduleItem);
			model.addAttribute("message", MessageUtil.getAlert(isAjax, MessageFormat.format(rbsMessageSource.getMessage("errors.required.select"), ModuleUtil.getItemName(siteTypeItem))));
			return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
		}
		return super.view(siteType, designName, attrVO, request, model);
	}
	
	/**
	 * 기본경로
	 */
	@Override
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");													// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");												// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));			// 목록 검색 항목
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{"siteType"});						// 기본 parameter

		String listName = "layoutThemeList.do";
		String viewName = "layoutThemeView.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, null, null, pageName, idxName
				, listName, viewName, null, null
				, null, null);
	}
}
