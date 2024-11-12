package rbs.modules.contents.web;

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
import org.springframework.web.bind.annotation.RequestParam;

import rbs.modules.contents.service.ContWorkService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleController;

/**
 * 사용자 사이트 콘텐츠 보기
 * @author user
 *
 */
@Controller
@ModuleMapping(moduleId="contents")
@RequestMapping("/{siteId}/contents")
public class UsrContentsController extends ModuleController{
	
	@Resource(name = "contWorkService")
	private ContWorkService contWorkService;
	
	/**
	 * 적용 콘텐츠 보기
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/view.do")
	public String view(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String ajaxPName = attrVO.getAjaxPName();
		
		JSONObject urCrtMenu = attrVO.getCrtMenu();
		String contCd = JSONObjectUtil.getString(urCrtMenu, "contents_code");
		int branchIdx = JSONObjectUtil.getInt(urCrtMenu, "branch_idx");
		String contentsType = JSONObjectUtil.getString(urCrtMenu, "contents_type");
		//JSONObject usrSiteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		String lang = attrVO.getSiteLocaleLang();//JSONObjectUtil.getString(usrSiteInfo, "locale_lang");

		if(StringUtil.isEmpty(contCd) || branchIdx <= 0 || StringUtil.isEmpty(contentsType)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		
		param.put("searchList", searchList);
		DataMap dt = contWorkService.getApplyView(contentsType, lang, param);

		model.addAttribute("dt", dt);
		
		return getViewPath("/view" + contentsType);
	}

	/**
	 * 메뉴콘텐츠관리 > 콘텐츠보기 버튼 클릭한 경우 : 해당 사이트로 미리보기
	 * @param verIdx
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 */
	@ModuleAuth(name="MNG")
	@RequestMapping(value="/view.do", params="verIdx")
	public String workView(@RequestParam(value="verIdx") int verIdx, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		String ajaxPName = attrVO.getAjaxPName();
		
		JSONObject urCrtMenu = attrVO.getCrtMenu();
		String contCd = JSONObjectUtil.getString(urCrtMenu, "contents_code");
		int branchIdx = JSONObjectUtil.getInt(urCrtMenu, "branch_idx");
		String contentsType = JSONObjectUtil.getString(urCrtMenu, "contents_type");
		//JSONObject usrSiteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		String lang = attrVO.getSiteLocaleLang();//JSONObjectUtil.getString(usrSiteInfo, "locale_lang");

		if(StringUtil.isEmpty(contCd) || branchIdx <= 0 || StringUtil.isEmpty(contentsType)) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}
		
		// 2. DB
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
		
		param.put("searchList", searchList);

		DataMap dt = contWorkService.getModify(contentsType, lang, param);
		
		model.addAttribute("dt", dt);
		
		return getViewPath("/view" + contentsType);
	}
}
