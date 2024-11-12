package rbs.modules.busi.web;

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

import rbs.modules.busi.service.BusiService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleDownloadController;

import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 샘플모듈 파일다운로드
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="busi")
@RequestMapping({"/{siteId}/busi", "/{admSiteId}/menuContents/{usrSiteId}/busi"})
public class BusiDownloadController extends ModuleDownloadController{

	@Resource(name = "busiService")
	private BusiService busiService;
		
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	@ModuleAuth(name={"DWN"})
	@RequestMapping(value = "/download.do")
	public ModelAndView download(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		
		int keyIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")));
		int fidx = StringUtil.getInt(request.getParameter("fidx"), 0);
		String itemId = request.getParameter("itId");
		if(fidx > 0 && StringUtil.isEmpty(itemId)) itemId = "file";
		
		if(StringUtil.isEmpty(itemId) || keyIdx <= 0) {
			return new ModelAndView(RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path"));
		}
		
		// 파일명 select
		String columnId = null;
		String savedFileName = null;
		String originFileName = null;
		DataMap dt = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A." + JSONObjectUtil.getString(settingInfo, "idx_column"), keyIdx));
		if(fidx > 0) {
			// multi file
			columnId = "FILE";
			searchList.add(new DTForm("A.FLE_IDX", fidx));
			searchList.add(new DTForm("A.ITEM_ID", itemId));
			param.put("searchList", searchList);
			dt = busiService.getMultiFileView(fnIdx, param);
		} else {
			// single file : columnItem
			JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
			JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
			columnId = JSONObjectUtil.getString(item, "column_id");
			param.put("columnItem", columnId);
			param.put("searchList", searchList);
			dt = busiService.getFileView(fnIdx, param);
		}
		
		if(dt == null) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.error.data"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}

		savedFileName = StringUtil.getString(dt.get(columnId + "_SAVED_NAME"));
		originFileName = StringUtil.getString(dt.get(columnId + "_ORIGIN_NAME"));

		return download(attrVO, savedFileName, originFileName, model);
	}
}
