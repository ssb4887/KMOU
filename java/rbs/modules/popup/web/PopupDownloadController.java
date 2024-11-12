package rbs.modules.popup.web;

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

import rbs.modules.popup.service.PopupService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
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
 * 파일다운로드
 * @author user
 *
 */
@Controller
@RequestMapping({"/{admSiteId}/popup", "/{admSiteId}/moduleFn/popup"})
@ModuleMapping(moduleId="popup")
public class PopupDownloadController extends ModuleDownloadController{

	@Resource(name = "popupService")
	private PopupService popupService;
	
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
		
		int popIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		String itemId = request.getParameter("itId");
		
		if(StringUtil.isEmpty(itemId) || popIdx <= 0) {
			return new ModelAndView(RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path"));
		}
		
		// 파일명 select
		String columnId = null;
		String savedFileName = null;
		String originFileName = null;
		DataMap dt = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.POP_IDX", popIdx));
		/*if(fidx > 0) {
			// multi file
			columnId = "FILE";
			searchList.add(new DTForm("A.FLE_IDX", fidx));
			searchList.add(new DTForm("A.ITEM_ID", itemId));
			param.put("searchList", searchList);
			dt = boardService.getMultiFileView(fnIdx, param);
		} else {*/
			// single file : columnItem
			JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
			JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
			columnId = JSONObjectUtil.getString(item, "column_id");
			param.put("columnItem", columnId);
			param.put("searchList", searchList);
			dt = popupService.getFileView(fnIdx, param);
		//}
		
		if(dt == null) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.error.data"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}

		savedFileName = StringUtil.getString(dt.get(columnId + "_SAVED_NAME"));
		originFileName = StringUtil.getString(dt.get(columnId + "_ORIGIN_NAME"));

		return download(attrVO, savedFileName, originFileName, model);
		/*
		// 1. 파일경로 체크
		boolean isPrevent = StringUtil.isFilePreventMatch(savedFileName);
		
		if(isPrevent) {
			model.addAttribute("message", MessageUtil.getAlert(portalMessageSource.getMessage("message.error.data"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}
		
		// 2. 다운로드가능 파일확장자 체크
        List<String[]> extStr = new ArrayList<String[]>();
        extStr.add(("0," + RbsProperties.getProperty("Globals.ph.upload.file")).split(","));
		boolean isDownload = FileUtil.isUploadable(savedFileName, extStr);

		if(!isDownload) {
			model.addAttribute("message", MessageUtil.getAlert(portalMessageSource.getMessage("message.file.noDownload"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}
		
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + moduleId + File.separator;
		//if(!StringUtil.isEmpty(inModuleId)) fileRealPath += inModuleId + File.separator;
		fileRealPath += fnIdx;
		//if(StringUtil.isEquals(type, "s")) fileRealPath += File.separator + "sm";
		
		File file = new File(fileRealPath + File.separator + savedFileName);
		if(file.isFile()) {
			Map<String, Object> downFileInfo = new HashMap<String, Object>();
			downFileInfo.put("downFile", file);
			downFileInfo.put("orgFileName", originFileName);
			return new ModelAndView("downloadView", downFileInfo);
		} 
		
		// 파일 없는  경우
		model.addAttribute("message", MessageUtil.getAlert(portalMessageSource.getMessage("message.file.searchFile"), ""));
		return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		*/
	}
}
