package rbs.modules.board.web;

import java.io.File;
import java.text.MessageFormat;
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

import rbs.modules.board.service.BoardService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.ClientUtil;
import com.woowonsoft.egovframework.util.FileUtil;
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
@RequestMapping({"/{admSiteId}/menuContents/{usrSiteId}/board", "/{admSiteId}/moduleFn/board", "/{siteId}/board"})
@ModuleMapping(moduleId="board")
public class BoardDownloadController extends ModuleDownloadController{

	@Resource(name = "boardService")
	private BoardService boardService;
	
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	@ModuleAuth(name={"DWN"})
	@RequestMapping(value = "/download.do")
	public ModelAndView download(@ModuleAttr ModuleAttrVO attrVO, @ParamMap ParamForm parameterMap, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		int fnIdx = attrVO.getFnIdx();
		
		int brdIdx = StringUtil.getInt(request.getParameter(JSONObjectUtil.getString(settingInfo, "idx_name")), 0);
		int fidx = StringUtil.getInt(request.getParameter("fidx"), 0);
		String itemId = request.getParameter("itId");
		if(fidx > 0 && StringUtil.isEmpty(itemId)) itemId = "file";

		if(StringUtil.isEmpty(itemId) || brdIdx <= 0) {
			return new ModelAndView(RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path"));
		}
		// 파일다운로드 사유 등록 여부
		int useFileCmt = JSONObjectUtil.getInt(settingInfo, "use_filecomt");
		if(useFileCmt == 1 && StringUtil.isEmpty(parameterMap.get("fileCmt"))) {
			// 사유항목 필수
			model.addAttribute("message", MessageUtil.getAlert(MessageFormat.format(rbsMessageSource.getMessage("errors.required"), "사유"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}
		
		// 파일명 select
		String columnId = null;
		String savedFileName = null;
		String originFileName = null;
		DataMap dt = null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("A.BRD_IDX", brdIdx));
		if(fidx > 0) {
			// multi file
			columnId = "FILE";
			searchList.add(new DTForm("A.FLE_IDX", fidx));
			searchList.add(new DTForm("A.ITEM_ID", itemId));
			param.put("searchList", searchList);
			dt = boardService.getMultiFileView(fnIdx, param);
			if(dt != null) {
				int result = boardService.updateMultiFileDown(fnIdx, brdIdx, fidx, itemId);
			}
		} else {
			// single file : columnItem
			JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
			JSONObject item = JSONObjectUtil.getJSONObject(items, itemId);
			columnId = JSONObjectUtil.getString(item, "column_id");
			param.put("columnItem", columnId);
			param.put("searchList", searchList);
			dt = boardService.getFileView(fnIdx, param);
			if(dt != null) {
				int result = boardService.updateFileDown(fnIdx, brdIdx, columnId);
			}
		}
		
		if(dt == null) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.error.data"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}

		savedFileName = StringUtil.getString(dt.get(columnId + "_SAVED_NAME"));
		originFileName = StringUtil.getString(dt.get(columnId + "_ORIGIN_NAME"));
		

		// 1. 파일경로 체크
		boolean isPrevent = StringUtil.isFilePreventMatch(savedFileName);
		
		if(isPrevent) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.error.data"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}
		
		// 2. 다운로드가능 파일확장자 체크
        List<String[]> extStr = new ArrayList<String[]>();
        extStr.add(("0," + RbsProperties.getProperty("Globals.ph.upload.file")).split(","));
		boolean isDownload = FileUtil.isUploadable(savedFileName, extStr);
		
		if(!isDownload) {
			if(savedFileName.indexOf(".") == -1) isDownload = true;
		}

		if(!isDownload) {
			model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.file.noDownload"), ""));
			return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		}

		String uploadModulePath = attrVO.getUploadModulePath();
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + ((StringUtil.isEmpty(uploadModulePath))?attrVO.getUploadModulePath():uploadModulePath);
		
		File file = new File(fileRealPath + File.separator + savedFileName);
		if(file.isFile()) {

			// 파일다운로드 사유 등록
			if(useFileCmt == 1) {
		    	parameterMap.put("itemId", itemId);
		    	parameterMap.put("fleIdx", fidx);
		    	parameterMap.put("fileSavedName", savedFileName);
		    	parameterMap.put("fileOriginName", originFileName);
		    	parameterMap.put("fnName", dt.get("FN_NAME"));
				boardService.fileCmtInsert(fnIdx, brdIdx, ClientUtil.getClientIp(request), parameterMap);
			}
			
			Map<String, Object> downFileInfo = new HashMap<String, Object>();
			downFileInfo.put("downFile", file);
			downFileInfo.put("orgFileName", originFileName);
			return new ModelAndView("rbsDownloadView", downFileInfo);
		} 
		
		// 파일 없는  경우
		model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.file.searchFile"), ""));
		return new ModelAndView(RbsProperties.getProperty("Globals.fail.path"));
		
		
		//return download(attrVO, savedFileName, originFileName, model);
	}
}
