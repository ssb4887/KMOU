package rbs.modules.contents.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;

@Controller
@ModuleMapping(moduleId="contents", confSModule="version")
@RequestMapping(value={"/{admSiteId}/contents"}, params={"contCd", "branchIdx"})
public class ContVersionController extends VersionCommonController{
	
	/**
	 * 콘텐츠 정보
	 * @param lang
	 * @param contCd
	 * @param attrVO
	 * @param model
	 */
	public void setBranchInfo(String lang, String contCd, int branchIdx, ModuleAttrVO attrVO, ModelMap model) {
		
		// Branch 정보
		DataMap branchDt = null;
		Map<String, Object> branchParam = new HashMap<String, Object>();
		List<DTForm> branchSearchList = new ArrayList<DTForm>();

		branchSearchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		branchSearchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		branchParam.put("searchList", branchSearchList);
		
		branchDt = contentsService.getModify(lang, branchParam);

		String submitType = "view";
		JSONObject branchItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject branchItems = JSONObjectUtil.getJSONObject(branchItemInfo, "items");
		JSONArray branchItemOrder = JSONObjectUtil.getJSONArray(branchItemInfo, submitType + "_order");
		
		model.addAttribute("branchDt", branchDt);
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(branchItems, branchItemOrder));
	}
	
	/**
	 * 수정
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/verInput.do", params="mode")
	public String input(@RequestParam(value="mode") String mode, @RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		return super.input(mode, contCd, branchIdx, lang, attrVO, request, model);
	}

	/**
	 * 수정처리
	 * @param mode
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/verInputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		return super.inputProc(mode, contCd, branchIdx, lang, parameterMap, attrVO, request, model);
	}
	
	/**
	 * 등록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/verInput.do")
	public String input(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		return super.input(contCd, branchIdx, lang, attrVO, request, model);
	}

	/**
	 * 등록처리
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/verInputProc.do")
	public String inputProc(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		String ajaxPName = attrVO.getAjaxPName();
		
		if(branchIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		return super.inputProc(contCd, branchIdx, lang, parameterMap, attrVO, request, model);
	}


	/**
	 * 삭제
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/verDeleteProc.do", params="select")
	public String delete(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @RequestParam(value="select") int[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		return super.delete(contCd, branchIdx, deleteIdxs, lang, attrVO, request, model);
	}

	/**
	 * 삭제
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.GET, value = "/verDeleteProc.do")
	public String delete(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		return super.delete(contCd, branchIdx, lang, attrVO, request, model);
	}

	/**
	 * 복원
	 * @param restoreIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(method=RequestMethod.POST, value = "/verRestoreProc.do", params="select")
	public String restore(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @RequestParam(value="select") int[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		return super.restore(contCd, branchIdx, restoreIdxs, lang, attrVO, request, model);
	}
	
	/**
	 * 완전삭제
	 * @param deleteIdxs
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="CDT")
	@RequestMapping(method=RequestMethod.POST, value = "/verCdeleteProc.do", params="select")
	public String cdelete(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @RequestParam(value="select") int[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		return super.cdelete(contCd, branchIdx, deleteIdxs, lang, attrVO, request, model);
	}


	/**
	 * 삭제목록 : 전체 목록
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/verDeleteList.do")
	public String deleteList(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		return super.deleteList(contCd, branchIdx, lang, attrVO, request, model);
	}

	/**
	 * 콘텐츠 경로
	 * @param attrVO
	 */
	public String[] fn_getContentsParams(ModuleAttrVO attrVO) {	
		JSONObject moduleSetting = attrVO.getModuleSetting();
		JSONObject moduleItem = attrVO.getModuleItem();
		JSONObject itemInfo = JSONObjectUtil.getJSONObject(moduleItem, "item_info");
		JSONObject settingInfo = JSONObjectUtil.getJSONObject(moduleSetting, "setting_info");
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");			// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));					// 목록 검색 항목
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, "list.do");
		//PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);
		
		return PathUtil.getUseLangTotalParams(baseParams, null, searchParams, null, pageName, idxName);
	}
	
	/**
	 * 경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		// 콘텐츠 경로
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String[] baseParams = fn_getContentsParams(attrVO);
		
		baseParams = StringUtil.addStringToArray(baseParams, "branchIdx");
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");											// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");										// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));	// 목록 검색 항목
		//PathUtil.fn_setCommonPath(queryString, baseParams, searchParams, idxName, pageName);

		String listName = "branchList.do";
		String inputName = "verInput.do";
		String inputProcName = "verInputProc.do";
		String deleteProcName = "verDeleteProc.do";
		String deleteListName = "verDeleteList.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, "VERSION", null, pageName, idxName
				, listName, null, inputName, inputProcName
				, deleteProcName, deleteListName);
		
		fn_setAddCommonPath(request);
	}
	
	/**
	 * 코드관리 경로
	 * @param request
	public void fn_setAddCommonPath(HttpServletRequest request) {		
		
		String allQueryString = StringUtil.getString(request.getAttribute("allQueryString"));

		String workName = "workInput.do";
		String workViewName = "workView.do";
		String workUrl = workName;
		String workViewUrl = workViewName;
		if(!StringUtil.isEmpty(allQueryString)) {
			workUrl += allQueryString;
			workViewUrl += allQueryString;
		}

		request.setAttribute("URL_WORKINPUT", workUrl);
		request.setAttribute("URL_WORKVIEW", workViewUrl);
	}
	 */
	
	/**
	 * 휴지통 경로
	public void fn_setDeleteListPath(ModuleAttrVO attrVO) {
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");								// 목록 페이징  key
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{"contCd", "branchIdx"});		// 기본 parameter

		// 항목 설정 정보
		String[] searchParams = StringUtil.concatenateStringArrays(PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search")), deleteListSearchParams);	// 검색 항목
		//PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, pageName);
		if(StringUtil.isEmpty(pageName)) pageName = "page";
		String listName = "verDeleteList.do";
		String restoreProcName = "verRestoreProc.do";
		String cdeleteProcName = "verCdeleteProc.do";
		
		PathUtil.fn_setDeleteListPath(queryString, baseParams, searchParams, null, pageName, listName, restoreProcName, cdeleteProcName);
	
	}
	 */
}
