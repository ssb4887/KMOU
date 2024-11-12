package rbs.modules.zipCode.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import rbs.modules.zipCode.service.ZipCodeService;

import com.woowonsoft.egovframework.annotation.ModuleAttr;
import com.woowonsoft.egovframework.annotation.ModuleAuth;
import com.woowonsoft.egovframework.annotation.ModuleMapping;
import com.woowonsoft.egovframework.form.DataForm;
import com.woowonsoft.egovframework.form.ModuleAttrVO;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.tag.ui.RbsPaginationInfo;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MessageUtil;
import com.woowonsoft.egovframework.util.PathUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.web.ModuleSearchController;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
@ModuleMapping(moduleId="zipCode")
@RequestMapping("/{siteId}/zipCode")
public class ZipCodeSearchController extends ModuleSearchController {

	@Resource(name = "zipCodeService")
	private ZipCodeService zipCodeService;
	
	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	private EgovPropertyService propertiesService;

	@Resource(name = "rbsMessageSource")
	private RbsMessageSource rbsMessageSource;

	/**
	 * 아이디 중복확인
	 * @param mbrId
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ModuleAuth(accessModule={"member", "memberAn", "board"})
	@RequestMapping(value = "/searchList.do")
	public String list(@ModuleAttr ModuleAttrVO attrVO, HttpServletRequest request, ModelMap model) throws Exception {
		JSONObject settingInfo = attrVO.getSettingInfo();
		DataForm queryString = attrVO.getQueryString();

		String viewName = null;
		int totalCount = 0;
		JSONArray list = null;
		RbsPaginationInfo paginationInfo = new RbsPaginationInfo();
		
		// 주소검색 사용 구분
		int searchType = RbsProperties.getPropertyInt("Globals.address.search.type");
		
		if(searchType == 1) {
			// 공공데이터포털 새우편번호 도로명주소조회 서비스
			String type = queryString.getString("type");
			String load = queryString.getString("sRGRoad");
			String bnum1 = queryString.getString("sRGBNum1");
			String bnum2 = queryString.getString("sRGBNum2");

			if(!StringUtil.isEmpty(type) && (StringUtil.isEmpty(load) || StringUtil.isEmpty(bnum1))) {
				model.addAttribute("message", MessageUtil.getAlert(rbsMessageSource.getMessage("message.search.checkFill")));
				return RbsProperties.getProperty("Globals.fail.path");
			}
			
			// 1. 페이지정보 setting
			int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
			int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
			int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
			
			int pageUnit = 20;
			int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
			String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
			int page = StringUtil.getInt(request.getParameter(pageName), 1);				// 현재 페이지 index

			paginationInfo.setUnitBeginCount(listUnit);
			paginationInfo.setUnitEndCount(listMaxUnit);
			paginationInfo.setUnitStep(listUnitStep);
			paginationInfo.setCurrentPageNo(page);
			paginationInfo.setRecordCountPerPage(pageUnit);
			paginationInfo.setPageSize(pageSize);
			
			// 2. DB
			
			if(!StringUtil.isEmpty(type)){
				//검색
				String key = load + " " + bnum1;
				if(!StringUtil.isEmpty(bnum2)) key += "-" + bnum2;
				JSONObject jsonObject = zipCodeService.getOpenApiEpostXmlData(type, key, listUnit, page);
				
				if(!JSONObjectUtil.isEmpty(jsonObject)) {
					JSONObject pageinfo = JSONObjectUtil.getJSONObject(jsonObject, "cmmMsgHeader");
					totalCount = JSONObjectUtil.getInt(pageinfo, "totalCount");
					paginationInfo.setTotalRecordCount(totalCount);
					list = JSONObjectUtil.getJSONArray(jsonObject, "newAddressListAreaCd");
				}
			}
			
			viewName = "/epostSearchList";
		} else if(searchType == 2) {
			// www.juso.go.kr 도로명주소 API

			String key = queryString.getString("key");

			if(!StringUtil.isEmpty(key)) {
				// 1. 페이지정보 setting
				int listUnit = JSONObjectUtil.getInt(settingInfo, "dset_list_count", propertiesService.getInt("DEFAULT_LIST_UNIT"));	// 페이지당 목록 수
				int listMaxUnit = listUnit * propertiesService.getInt("DEFAULT_LIST_MAX_CNT");											// 최대 페이지당 목록 수 
				int listUnitStep = listUnit;										// 페이지당 목록 수 증가값
				
				int pageUnit = 20;
				int pageSize = JSONObjectUtil.getInt(settingInfo, "dset_list_block", propertiesService.getInt("PAGE_SIZE"));	// 페이징 크기
				String pageName = JSONObjectUtil.getString(settingInfo, "page_name");		// 목록 페이징  key
				int page = StringUtil.getInt(request.getParameter(pageName), 1);				// 현재 페이지 index
	
				paginationInfo.setUnitBeginCount(listUnit);
				paginationInfo.setUnitEndCount(listMaxUnit);
				paginationInfo.setUnitStep(listUnitStep);
				paginationInfo.setCurrentPageNo(page);
				paginationInfo.setRecordCountPerPage(pageUnit);
				paginationInfo.setPageSize(pageSize);
				
				// 2. DB
				
				//검색
				JSONObject jsonObject = zipCodeService.getOpenApiJusoJsonData(key, listUnit, page);
				
				if(!JSONObjectUtil.isEmpty(jsonObject)) {
					JSONObject pageinfo = JSONObjectUtil.getJSONObject(jsonObject, "common");
					totalCount = JSONObjectUtil.getInt(pageinfo, "totalCount");
					paginationInfo.setTotalRecordCount(totalCount);
					list = JSONObjectUtil.getJSONArray(jsonObject, "juso");
				}
			}
			
			viewName = "/jusoSearchList";
		}
    	
    	model.addAttribute("paginationInfo", paginationInfo);										// 페이징정보
    	model.addAttribute("list", list);															// 목록
    	
    	// 기본경로
    	fn_setSearchPath(attrVO, searchType);
		return getViewPath(viewName);
	}
	
	/**
	 * 경로
	 */
	public void fn_setSearchPath(ModuleAttrVO attrVO, int searchType) {

		DataForm queryString = attrVO.getQueryString();
		
		String[] searchParams = null;//PathUtil.getSearchParams(JSONObjectUtil.getJSONObject(itemInfo, "totSearchList_search"));					// 목록 검색 항목
		if(searchType == 1) {
			searchParams = new String[]{"type", "sRGRoad", "sRGBNum1", "sRGBNum2"};
		} else if(searchType == 2) {
			searchParams = new String[]{"key"};
		}

		String listName = "searchList.do";
		String[] baseParams = propertiesService.getStringArray("moduleSearchBaseParams");													// 기본 parameter
		PathUtil.fn_setListPath(queryString, baseParams, searchParams, null, listName);
	}
}
