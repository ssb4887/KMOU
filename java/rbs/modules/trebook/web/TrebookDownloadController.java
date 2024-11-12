package rbs.modules.trebook.web;

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
import org.springframework.web.servlet.ModelAndView;

import rbs.modules.trebook.service.TrebookService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.CookieUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleDownloadController;

import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 파일다운로드
 * @author user
 *
 */
@Controller
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/trebook", "/{admSiteId}/moduleFn/trebook", "/{siteId}/trebook"})
@ModuleMapping(moduleId="trebook")
public class TrebookDownloadController extends ModuleDownloadController{

	@Resource(name = "trebookService")
	private TrebookService trebookService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@RequestMapping(value = "/download.do")
	public ModelAndView download(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		
		int treIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		int conIdx = StringUtil.getInt(request.getParameter("conIdx"), 0);
		String itemId = request.getParameter("itId");
		
		if(StringUtil.isEmpty(itemId) || treIdx <= 0) {
			return new ModelAndView(RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path"));
		}
		
		// 파일명 select
		String columnId = null;
		String savedFileName = null;
		String originFileName = null;
		DataMap dt = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.TRE_IDX", treIdx));
		
		if(conIdx > 0) {
			// Con File
			searchList.add(new DTForm("A.CON_IDX", conIdx));
			
			// Con Items
			JSONObject conItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "con_item_info");
			JSONObject conItems = JSONObjectUtil.getJSONObject(conItemInfo, "items");
			JSONObject conItem = JSONObjectUtil.getJSONObject(conItems, itemId);
			columnId = JSONObjectUtil.getString(conItem, "column_id");
			param.put("columnItem", columnId);
			param.put("searchList", searchList);
			dt = trebookService.getConFileView(fnIdx, param);
		} else {
			// Trebook File
			JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
			JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
			columnId = JSONObjectUtil.getString(item, "column_id");
			param.put("columnItem", columnId);
			param.put("searchList", searchList);
			dt = trebookService.getFileView(fnIdx, param);
		}
		
		if(dt == null) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.error.data"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}
		
		/****************************************************************************
		 * 다운로드수 수정 
		 ****************************************************************************/
		if(conIdx > 0) {
			trebookService.updateConDownload(fnIdx, treIdx, conIdx);
		} else {
			trebookService.updateDownload(fnIdx, treIdx);
		}

		savedFileName = StringUtil.getString(dt.get(columnId + "_SAVED_NAME"));
		originFileName = StringUtil.getString(dt.get(columnId + "_ORIGIN_NAME"));

		return download(attrVO, savedFileName, originFileName, model);
	}
}
