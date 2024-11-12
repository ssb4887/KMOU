package rbs.modules.contents.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.ui.ModelMap;

import rbs.modules.contents.service.ContVersionService;
import rbs.modules.contents.service.ContentsService;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;

public class BranchCommonController extends CommonController{
	
	@Resource(name = "contentsService")
	protected ContentsService contentsService;
	
	@Resource(name = "contVersionService")
	protected ContVersionService contVersionService;
	
	/**
	 * 콘텐츠 정보 : 콘텐츠관리(Branch/버전목록) / 메뉴콘텐츠관리 > 콘텐츠관리(버전목록)
	 * @param lang
	 * @param contCd
	 * @param attrVO
	 * @param model
	 */
	public void setContentsInfo(String lang, String contCd, ModuleAttrVO attrVO, ModelMap model) {
		
		// Contents 정보
		DataMap contDt = null;
		Map<String, Object> contParam = new HashMap<String, Object>();
		List<DTForm> contSearchList = new ArrayList<DTForm>();

		contSearchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		contParam.put("searchList", contSearchList);
		
		contDt = contentsService.getModify(lang, contParam);

		String submitType = "view";
		JSONObject contentsItemInfo = JSONObjectUtil.getJSONObject(attrVO.getModuleItem(), "item_info");
		JSONObject contentsItems = JSONObjectUtil.getJSONObject(contentsItemInfo, "items");
		JSONArray contentsItemOrder = JSONObjectUtil.getJSONArray(contentsItemInfo, submitType + "_order");
		
		model.addAttribute("contDt", contDt);
		model.addAttribute("contOptnHashMap", CodeHelper.getItemOptnHashMap(contentsItems, contentsItemOrder));
	}
	
	/**
	 * 버전 목록 정보
	 * @param contCd
	 * @param branchIdx
	 * @param lang
	 * @param itemInfo
	 * @param model
	 * @return
	 */
	public String setVerList(String contCd, int branchIdx, String lang, JSONObject itemInfo, ModelMap model) {
		/*if(StringUtil.isEmpty(contCd) || branchIdx <= 0) {
			return RbsProperties.getProperty("Globals.error.400" + ajaxPName + ".path");
		}*/
		
		// 목록
		String submitType = "list";
		JSONObject items = JSONObjectUtil.getJSONObject(itemInfo, "items");
		JSONArray itemOrder = JSONObjectUtil.getJSONArray(itemInfo, submitType + "_order");
		
		
		// 2. DB
		List<?> versionList = null;
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		// 2.1 검색조건
		searchList.add(new DTForm("A.CONTENTS_CODE", contCd));
		searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));
		param.put("searchList", searchList);
		
		// 2.3 목록
		versionList = contVersionService.getList(lang, param);
    	
    	model.addAttribute("verList", versionList);													// 버전 목록
		model.addAttribute("verOptnHashMap", CodeHelper.getItemOptnHashMap(items, itemOrder));
		
		return null;
    	
	}
	
}
