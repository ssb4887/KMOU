package rbs.modules.menu.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import rbs.egovframework.WebsiteVO;
import rbs.modules.website.service.WebsiteService;

import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleSearchController;

@Controller
@RequestMapping("/{admSiteId}/menu")
@ModuleMapping(moduleId="version", confModule="website", confSModule="version")
public class LayoutComController extends ModuleSearchController{

	@Resource(name = "websiteService")
	protected WebsiteService websiteService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	
	protected String getSiteId() {

		WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = usrSiteVO.getSiteId();
		
		return siteId;
	}
	
	protected String getSiteType(ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		JSONObject crtMenu = attrVO.getCrtMenu();
		
		String crtModuleId = JSONObjectUtil.getString(crtMenu, "module_id");
		String siteType = null;
		if(StringUtil.isEquals(crtModuleId, "website")) siteType = request.getParameter("siteType");
		else {
			String siteId = getSiteId();
			Map<String, Object> verParam = new HashMap<String, Object>();
			List<DTForm> verSearchList = new ArrayList<DTForm>();
			verSearchList.add(new DTForm("A.SITE_ID", siteId));
			verParam.put("searchList", verSearchList);
			DataMap verDt = websiteService.getModify(verParam);

			if(verDt == null) {
				boolean isAjax = attrVO.isAjax();
				String ajaxPName = attrVO.getAjaxPName();
				model.addAttribute("message", MessageUtil.getAlert(isAjax, rbsMessageSource.getMessage("message.website.no.manager.list")));
				return RbsProperties.getProperty("Globals.fail" + ajaxPName + ".path");
			}
			
			siteType = StringUtil.getString(verDt.get("SITE_TYPE"));
		}
		
		return siteType;
	}
	
	protected JSONObject getSiteTypeItem(JSONObject moduleItem) {
		JSONObject siteItemInfo = JSONObjectUtil.getJSONObject(moduleItem, "item_info");
		JSONObject siteItems = JSONObjectUtil.getJSONObject(siteItemInfo, "items");
		JSONObject siteTypeItem = JSONObjectUtil.getJSONObject(siteItems, "site_type");
		
		return siteTypeItem;
	}
	
}
