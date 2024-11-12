package rbs.modules.contents.web;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rbs.egovframework.WebsiteVO;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.annotation.ParamMap;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
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
@RequestMapping(value={"/{admSiteId}/menuContents/{usrSiteId}/contents"})
public class MenuContVersionController extends VersionCommonController{
	
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

	@ModuleAuth(name="MNG")
	@RequestMapping("/verList.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String ajaxPName = attrVO.getAjaxPName();
		
		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		if(StringUtil.isEmpty(contCd) || branchIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		JSONObject itemInfo = attrVO.getItemInfo();
		
		// Contents 정보
		setContentsInfo(lang, contCd, attrVO, model);
		
		// 목록
		String reStr = super.setVerList(contCd, branchIdx, lang, itemInfo, model);
		if(!StringUtil.isEmpty(reStr)) return reStr;
    	
    	// 기본경로
    	fn_setCommonPath(attrVO);
		return getViewPath("/list");
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
	public String input(@RequestParam(value="mode") String mode, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String inputProc(@RequestParam(value="mode") String mode, @ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String input(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();

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
	public String inputProc(@ParamMap ParamForm parameterMap, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String delete(@RequestParam(value="select") int[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String delete(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String restore(@RequestParam(value="select") int[] restoreIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String cdelete(@RequestParam(value="select") int[] deleteIdxs, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
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
	public String deleteList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		ContentsVO contVO = getContentsParam(attrVO, request);
		String contCd = contVO.getContCd();
		int branchIdx = contVO.getBranchIdx();
		String lang = contVO.getLang();
		
		return super.deleteList(contCd, branchIdx, lang, attrVO, request, model);
	}
	
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		// 콘텐츠 경로
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");											// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");										// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));	// 목록 검색 항목

		String listName = "verList.do";
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
}
