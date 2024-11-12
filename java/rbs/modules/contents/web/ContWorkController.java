package rbs.modules.contents.web;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;

@Controller
@ModuleMapping(moduleId="contents", confSModule="version")
@RequestMapping(value={"/{admSiteId}/contents"}, params={"contCd", "branchIdx", "verIdx"})
public class ContWorkController extends WorkCommonController{
	
	/**
	 * 작업
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/workView.do")
	public String view(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @RequestParam(value="verIdx") int verIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		return super.view(contCd, branchIdx, verIdx, lang, attrVO, request, model);
	}
	
	/**
	 * 작업
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/workInput.do")
	public String input(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @RequestParam(value="verIdx") int verIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		return super.input(contCd, branchIdx, verIdx, lang, attrVO, request, model);
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
	@RequestMapping(method=RequestMethod.POST, value = "/workInputProc.do", params="mode")
	public String inputProc(@RequestParam(value="mode") String mode, @RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		return super.inputProc(mode, contCd, branchIdx, lang, parameterMap, attrVO, request, model);
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
	@RequestMapping(method=RequestMethod.POST, value = "/workApplyProc.do")
	public String applyProc(@RequestParam(value="contCd") String contCd, @RequestParam(value="branchIdx") int branchIdx, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		DataForm queryString = attrVO.getQueryString();
		String lang = queryString.getString("slang");
		
		return super.applyProc(contCd, branchIdx, lang, parameterMap, attrVO, request, model);
	}
	
	/**
	 * 경로
	 */
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		// 콘텐츠 경로
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
		String viewName = "workView.do";
		String inputName = "workInput.do";
		String inputProcName = "workInputProc.do";
		String deleteProcName = "workApplyProc.do";
		String deleteListName = "workLogList.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, "WORK", null, pageName, idxName
				, listName, viewName, inputName, inputProcName
				, deleteProcName, deleteListName);
	}
}
