package rbs.modules.statistics.web;

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
import rbs.modules.contact.service.ContactService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;
import com.woowonsoft.egovframework.web.ModuleController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/statistics", "/{admSiteId}/moduleFn/statistics", "/{siteId}/statistics"})
@ModuleMapping(moduleId="statistics")
public class StatisticUserController extends ModuleController{
	
	@Resource(name="contactService")
	ContactService contactService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	public String getSiteId(){
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		if(usrSiteVO != null) return usrSiteVO.getSiteId();
		return null;
	}
	
	public String getStatsViewPath(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String siteId = getSiteId();
		if(StringUtil.isEmpty(siteId)) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.website.no.manager.list")));
			return RbsProperties.getProperty("Globals.fail.path");
		}
		
		return getStatsViewPath(siteId, attrVO, request, model);
	}
	
	public String getStatsViewPath(String siteId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		
		// 2. DB
		int totalCount = 0;
		List<?> list = null;

		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		int usePaging = JSONObjectUtil.getInt(settingInfo, "use_paging");
		if(usePaging == 1) {
			int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", 0, propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = StringUtil.getInt(request.getParameter("lunit"));
			if(pageUnit == 0) pageUnit = listUnit;//JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("PAGE_UNIT"));	// 페이지당 목록 수
			int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", 0, propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
			
			String pageName = JSONObjectUtil.getString(settingInfo, "page_name");
			int page = StringUtil.getInt(request.getParameter(pageName), 1);				// 현재 페이지 index
	
			paginationInfo.setUnitBeginCount(listUnit);
			paginationInfo.setUnitEndCount(listMaxUnit);
			paginationInfo.setUnitStep(listUnitStep);
			paginationInfo.setCurrentPageNo(page);
			paginationInfo.setRecordCountPerPage(pageUnit);
			paginationInfo.setPageSize(pageSize);
		}

		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		param.put("siteId", StringUtil.toUpperCase(siteId));
		param.put("searchList", searchList);
		
		// 2.2 목록수
    	totalCount = contactService.getUserTotalCount(param);
		paginationInfo.setTotalRecordCount(totalCount);

    	if(totalCount > 0) {
    		if(usePaging == 1) {
	    		param.put("firstIndex", paginationInfo.getFirstRecordIndex());
	    		param.put("lastIndex", paginationInfo.getLastRecordIndex());
	    		param.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    	}
    		
    		// 2.3 목록
    		list = contactService.getUserList(param);
		}
		
		model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
		model.addAttribute("list", list);
		
    	return null;
	}
	
	@RequestMapping("/userList.do")
	public String userList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String viewPath = getStatsViewPath(attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
		
		return getViewPath("/userList");
	}
	
	@RequestMapping("/admUserList.do")
	public String admUserList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String viewPath = getStatsViewPath(RbsProperties.getProperty("Globals.site.mode.adm"), attrVO, request, model);
		if(!StringUtil.isEmpty(viewPath)) return viewPath;
		
		return getViewPath("/userList");
	}
}
