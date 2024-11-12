package rbs.modules.majorInfo.web;

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

import rbs.modules.majorInfo.service.MajorInfoService;

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
@ModuleMapping(moduleId="majorInfo")
@RequestMapping({"/{siteId}/majorInfo", "/{admSiteId}/menuContents/{usrSiteId}/majorInfo"})
public class MajorInfoDownloadController extends ModuleDownloadController{

	@Resource(name = "majorInfoService")
	private MajorInfoService majorInfoService;
	
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
		
		int keyMajorIdx = StringUtil.getInt(request.getParameter("majorIdx"), 0);
		int keyYear = StringUtil.getInt(request.getParameter("majorYear"), 0);
//		int fidx = StringUtil.getInt(request.getParameter("fidx"), 0);
		String itemId = "file"; 
//		if(fidx > 0 && StringUtil.isEmpty(itemId)) itemId = "file";
		
		if(StringUtil.isEmpty(itemId) || keyMajorIdx <= 0 || keyYear <= 0) {
			return new ModelAndView(RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path"));
		}
		
		// 파일명 select
		String columnId = null;
		String savedFileName = null;
		String originFileName = null;
		DataMap dt = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.MAJOR_IDX", keyMajorIdx));
		searchList.add(new DTForm("A.YEAR" , keyYear));
//		if(fidx > 0) {
//			// multi file
//			columnId = "FILE";
//			searchList.add(new DTForm("A.FLE_IDX", fidx));
//			searchList.add(new DTForm("A.FILE_SAVED_NAME", itemId));
//			param.put("searchList", searchList);
//			dt = majorInfoService.getMultiFileView(fnIdx, param);
//			if(dt != null) {
//				int result = majorInfoService.updateMultiFileDown(fnIdx, keyIdx, fidx, itemId);
//			}
//		} else {
			// single file : columnItem
			JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
			JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
			columnId = "FILE"; //FILE
			param.put("columnItem", columnId);
			param.put("searchList", searchList);
			dt = majorInfoService.getFileView(fnIdx, param);
			if(dt != null) {
				int result = majorInfoService.updateFileDown(fnIdx, keyMajorIdx, keyYear, columnId);
			}
//		}
		
		if(dt == null) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.error.data"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}

		savedFileName = StringUtil.getString(dt.get(columnId + "_SAVED_NAME"));
		originFileName = StringUtil.getString(dt.get(columnId + "_ORIGIN_NAME"));

		return download(attrVO, savedFileName, originFileName, model);
	}
}