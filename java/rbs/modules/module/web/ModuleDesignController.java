package rbs.modules.module.web;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;

@Controller
@ModuleMapping(moduleId="moduleFn", confModule="module", confSModule="fn")
@RequestMapping("/{admSiteId}/moduleFn")
public class ModuleDesignController extends ModuleDesignComController{

	/**
	 * 목록조회
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 */
	@ModuleAuth(name="MNG", accessModule="menu")
	@RequestMapping("/designList.do")
	public String list(@RequestParam(value="moduleId", required=false) String moduleId, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) {
		
		return super.list(moduleId, attrVO, request, model);
	}
	
	/**
	 * 수정
	 * @param mode
	 * @param moduleId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG", accessModule="menu")
	@RequestMapping(value = "/designView.do")
	public String view(@RequestParam(value="moduleId") String moduleId, @RequestParam(value="dg") String designName, @ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

		return super.view(moduleId, designName, attrVO, request, model);
	}
	
	/**
	 * 기본경로
	 */
	@Override
	public void fn_setCommonPath(ModuleAttrVO attrVO) {	
		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String idxName = JSONObjectUtil.getString(settingInfo, "idx_name");													// 상세조회 key
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");												// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "list_search"));			// 목록 검색 항목
		String[] baseParams = StringUtil.insertStringArrays(this.baseParams, new String[]{"moduleId", "designType"});		// 기본 parameter
		//String[] tabBaseParams = {"moduleId"};																			// 기본 parameter
		//PathUtil.fn_setCommonPath(queryString, baseParams, tabBaseParams, searchParams, idxName, pageName);
		

		String listName = "designList.do";
		String viewName = "designView.do";
		//String imageName = "moduleDesignImage.do";
		PathUtil.fn_setCommonPath(queryString
				, baseParams, null, searchParams
				, null, null, pageName, idxName
				, listName, viewName, null, null
				, null, null);

		
		//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		//fn_setCommonAddPath(request);
	}
}
