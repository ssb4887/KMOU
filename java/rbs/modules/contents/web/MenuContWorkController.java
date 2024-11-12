package rbs.modules.contents.web;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import rbs.egovframework.WebsiteVO;

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
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;

/**
 * 메뉴콘텐츠관리 > 콘텐츠관리
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="contents", confSModule="version")
@RequestMapping(value={"/{admSiteId}/menuContents/{usrSiteId}/contents"}, params={"verIdx"})
public class MenuContWorkController extends WorkCommonController{
	
	private class ContentsVO {
		private String contCd;
		private int branchIdx;
		private String lang;
		public String getContCd() {
			return contCd;
		}
		public void setContCd(String contCd) {
			this.contCd = contCd;
		}
		public int getBranchIdx() {
			return branchIdx;
		}
		public void setBranchIdx(int branchIdx) {
			this.branchIdx = branchIdx;
		}
		public String getLang() {
			return lang;
		}
		public void setLang(String lang) {
			this.lang = lang;
		}
	}
	
	private ContentsVO getContentsParam(ModuleAttrVO attrVO, HttpServletRequest request) {
		JSONObject urCrtMenu = attrVO.getCrtMenu();
		String contCd = JSONObjectUtil.getString(urCrtMenu, "contents_code");
		int branchIdx = JSONObjectUtil.getInt(urCrtMenu, "branch_idx");
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		String lang = usrSiteVO.getLocaleLang();
		/*JSONObject usrSiteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("usrSiteInfo"));
		String lang = JSONObjectUtil.getString(usrSiteInfo, "locale_lang");*/
		
		ContentsVO contVO = new ContentsVO();
		contVO.setContCd(contCd);
		contVO.setBranchIdx(branchIdx);
		contVO.setLang(lang);
		
		return contVO;
	}
	
	/**
	 * 작업
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value = "/workView.do")
	public String view(@RequestParam(value="verIdx") int verIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String input(@RequestParam(value="verIdx") int verIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String applyProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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

		String listName = "verList.do";
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
