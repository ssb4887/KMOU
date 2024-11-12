package rbs.modules.menu.web;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rbs.modules.member.service.MemberAnService;
import rbs.modules.member.service.MemberService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.CodeHelper;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleSearchController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@ModuleMapping(moduleId="menu")
@RequestMapping("/{admSiteId}/menuContents/{usrSiteId}/menu")
public class MenuSearchController extends ModuleSearchController {

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	private EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	private RbsMessageSource rbsMessageSource;

	/**
	 * 메뉴검색 : 1개 선택 - 메뉴콘텐츠관리 > 메인 : 게시판,콘텐츠 검색시 사용
	 * @param attrVO
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(name="MNG", accessModule={"dashboard"})
	@RequestMapping(value = "/searchOList.do")
	public String totSearchList(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {

    	// 기본경로
		fn_setSearchOPath(attrVO);
		return getViewPath("/searchOList");
	}
	
	/**
	 * 경로
	 */
	public void fn_setSearchOPath(ModuleAttrVO attrVO) {

		JSONObject settingInfo = attrVO.getSettingInfo();
		JSONObject itemInfo = attrVO.getItemInfo();
		DataForm queryString = attrVO.getQueryString();
		
		String pageName = JSONObjectUtil.getString(settingInfo, "page_name");															// 목록 페이징  key
		String[] searchParams = PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "searchList_search"));					// 목록 검색 항목

		String listName = "searchOList.do";
		//String[] baseParams = StringUtil.addStringToArray(this.baseParams, "itemId");													// 기본 parameter
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, pageName, listName);
	}
}
