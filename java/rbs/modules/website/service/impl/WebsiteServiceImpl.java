package rbs.modules.website.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DTFuncForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.util.FileUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;
import com.woowonsoft.egovframework.util.WebsiteDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.egovframework.WebsiteVO;
import rbs.egovframework.schema.mapper.SchemaMapper;
import rbs.modules.menu.mapper.MenuMapper;
import rbs.modules.menu.mapper.MenuVerMapper;
import rbs.modules.menu.service.MenuVerService;
import rbs.modules.module.service.ModuleFnService;
import rbs.modules.website.mapper.WebsiteMapper;
import rbs.modules.website.service.WebsiteService;

@Service("websiteService")
public class WebsiteServiceImpl extends EgovAbstractServiceImpl implements WebsiteService {

	@Resource(name="websiteMapper")
	private WebsiteMapper websiteDAO;
	
	@Resource(name="menuVerMapper")
	private MenuVerMapper menuVerDAO;
	
	@Resource(name="menuMapper")
	private MenuMapper menuDAO;
	
	@Resource(name = "moduleFnService")
	private ModuleFnService moduleFnService;
	
	@Resource(name="menuVerService")
	protected MenuVerService menuVerService;
	
	@Override
	public List<Object> getWebsiteList(Map<String, Object> param) {
		return websiteDAO.getWebsiteList(param);
	}
	
	@Override
	public WebsiteVO getSiteInfo(Map<String, Object> param) {
		return websiteDAO.getSiteInfo(param);
	}
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return websiteDAO.getTotalCount(param);
    }

    /**
	 * 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return websiteDAO.getList(param);
	}

	/**
	 * 수정 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
    	DataMap viewDAO = websiteDAO.getModify(param);
		return viewDAO;
	}
	
	private void setSiteInfo(JSONObject siteInfo, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) {
		Object paramVal = null;
	    String itemId = null;
	    JSONObject item = null;
	    String itemType = null;
	    for(Object itemIdObj:itemOrder) {
	    	itemId = StringUtil.getString(itemIdObj);
	    	if(StringUtil.isEmpty(itemId)) continue;
	    	item = JSONObjectUtil.getJSONObject(items, itemId);
	    	itemType = JSONObjectUtil.getString(item, "item_type");
	    	paramVal = parameterMap.get(itemId);
	    	if(StringUtil.isEmpty(paramVal)) {
	    		siteInfo.remove(itemId);
	    		continue;
	    	}
	    	
	    	if(StringUtil.isEquals(itemType, "1")) paramVal = StringUtil.getInt(parameterMap.get(itemId));
	    	siteInfo.put(itemId, paramVal);
	    }
	}
    
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int insert(String uploadModulePath, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, JSONObject verSettingInfo, JSONObject verItems, JSONArray verItemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		String siteId = StringUtil.getString(parameterMap.get("site_id"));
		// 1. key 중복확인
		Map<String, Object> sparam1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(new DTForm("A.SITE_ID", siteId));
		sparam1.put("searchList", searchList1);
		int duplicate = websiteDAO.getDuplicateCount(sparam1);
		if(duplicate > 0) {
			return -2;
		}

		// 2. 배너, 팝업 기능 생성
		boolean isCreateBannerTable = false;
		boolean isCreatePopupTable = false;
		String bannerModuleId = "banner";
		int bannerFnIdx = StringUtil.getInt(parameterMap.get("banner_fn_idx"));
		String bannerFnName = null;
		String popupModuleId = "popup";
		int popupFnIdx = StringUtil.getInt(parameterMap.get("popup_fn_idx"));
		String popupFnName = null;
		
		JSONObject moduleFnItems = null;
		JSONArray moduleFnItemOrder = null;
		if(bannerFnIdx <= 0 || popupFnIdx <= 0) {
			JSONObject moduleFnItem = ModuleUtil.getModuleItemObject("module", 1);
			JSONObject moduleFnItemInfo = JSONObjectUtil.getJSONObject(moduleFnItem, "fn_item_info");
			moduleFnItems = JSONObjectUtil.getJSONObject(moduleFnItemInfo, "items");
			moduleFnItemOrder = JSONObjectUtil.getJSONArray(moduleFnItemInfo, "writeproc_order");
		}
		if(bannerFnIdx <= 0) {
			// 2.1 banner fnIdx 신규생성
			ParamForm bannerParameterMap = new ParamForm();
			bannerFnName = parameterMap.get("site_name") + " 배너";
			bannerParameterMap.put("fnName", bannerFnName);
			bannerFnIdx = moduleFnService.insert(false, null, bannerModuleId, regiIp, bannerParameterMap, moduleFnItems, moduleFnItemOrder);
			if(bannerFnIdx > 0) isCreateBannerTable = true;
			parameterMap.put("banner_fn_idx", bannerFnIdx);
		}
		
		if(popupFnIdx <= 0) {
			// 2.2 popup fnIdx 신규생성
			ParamForm popupParameterMap = new ParamForm();
			popupFnName = parameterMap.get("site_name") + " 팝업";
			popupParameterMap.put("fnName", popupFnName);
			popupFnIdx = moduleFnService.insert(false, null, popupModuleId, regiIp, popupParameterMap, moduleFnItems, moduleFnItemOrder);
			if(popupFnIdx > 0) isCreatePopupTable = true;
			parameterMap.put("popup_fn_idx", popupFnIdx);
		}
		
		//int result = moduleFnService.websiteInsert(uploadModulePath, regiIp, parameterMap, settingInfo, items, itemOrder, verSettingInfo, verItems, verItemOrder);
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		Map<String, Object> verParam = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> verDataList = new ArrayList<DTForm>();						// 저장항목
		Map<String, Object> menuParam = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> menuSearchList = new ArrayList<DTForm>();						// 검색항목
		List<DTForm> menuDataList = new ArrayList<DTForm>();						// 저장항목
		
		// 3. 저장 data setting : 사이트, 버전, 메뉴
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
		    	
    	dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("REGI_ID", loginMemberId));
    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
    	dataList.add(new DTForm("REGI_IP", regiIp));
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

    	verDataList.addAll(dataList);
    	menuDataList.addAll(dataList);
    	
		// 3.1 항목설정으로 사이트 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder, "ver", verSettingInfo, verItems, verItemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;

		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		// 3.2 순서 setting
		int ordIdx = websiteDAO.getMaxOrdIdx(null) + 1;
		dataList.add(new DTForm("ORDER_IDX", ordIdx));

		// 관리자 setting
		Object multiObjVals = parameterMap.get("manager_member_idxs");
		String[] multiVals = null;
		if(multiObjVals instanceof String[]) multiVals = (String[])multiObjVals;
		else if(multiObjVals instanceof String){
			multiVals = new String[1];
			multiVals[0] = (String)multiObjVals;
		}
		dataList.add(new DTForm("MANAGER_MEMBER_IDXS", StringUtil.getString(multiVals, ",", null)));

    	param.put("dataList", dataList);
    	
    	// 3.3 사이트 테이블 저장
		int result = websiteDAO.insert(param);
		
		if(result > 0) {
			
			// 3.4 버전 테이블 저장
			verDataList.add(new DTForm("SITE_ID", siteId));
			verDataList.add(new DTForm("VER_IDX", 1));
			verDataList.add(new DTForm("ISSERVICE", "1"));


			List verItemDataList = StringUtil.getList(dataMap.get("ver_dataList"));
			if(verItemDataList != null) verDataList.addAll(verItemDataList);
			
			verParam.put("dataList", verDataList);
			int result1 = menuVerDAO.insert(verParam);
			
			// 3.5 메뉴 저장
			boolean isDefault = false;
			String sourceSiteId = null;
			int sourceVerIdx = 0;
			int targetVerIdx = 1;
			String copySiteId = StringUtil.getString(parameterMap.get("copy_site_id"));
			if(!StringUtil.isEmpty(copySiteId)) {
				// 3.5.1 메뉴 복제 : 복제 버전 얻기 - 적용버전 없는 경우 최근 등록 버전
				sourceSiteId = copySiteId;
				
				Map<String, Object> verSParam = new HashMap<String, Object>();				// mapper parameter 데이터
				List<DTForm> verSearchList = new ArrayList<DTForm>();						// 검색항목
				verSearchList.add(new DTForm("A.SITE_ID", sourceSiteId));
				verSParam.put("searchList", verSearchList);
				sourceVerIdx = menuVerDAO.getServiceVerIdx(verSParam);
				if(sourceVerIdx <= 0) sourceVerIdx = menuVerDAO.getMaxVerIdx(verSParam);
			}
			
			// 3.5.2 복제버전 없는 경우 기본 사이트 복제
			if(sourceVerIdx <= 0) {
				isDefault = true;
				sourceSiteId = "default";
				sourceVerIdx = 1;
			}
			
			// 3.5.3 메뉴설정 정보 얻기
			String siteMode = RbsProperties.getProperty("Globals.site.mode.usr");
			JSONObject targetSiteObject = new JSONObject();
			JSONObject sourceSiteObject = null;
			
			sourceSiteObject = MenuUtil.getSiteObject("/" + siteMode + "/" + sourceSiteId, false);
			if(sourceSiteObject != null) targetSiteObject.accumulateAll(sourceSiteObject);
			
			JSONObject targetSiteInfo = JSONObjectUtil.getJSONObject(targetSiteObject, "site_info");
			JSONObject targetMenus = JSONObjectUtil.getJSONObject(targetSiteObject, "menus");
			
			// 기본경로
			String sourceLocalePath = JSONObjectUtil.getString(targetSiteInfo, "local_path");
		    String targetLocalPath = StringUtil.getString(parameterMap.get("local_path"));
			
			// 3.5.4 사이트 정보 setting
		    setSiteInfo(targetSiteInfo, parameterMap, items, itemOrder);
		    targetSiteInfo.put("version", targetVerIdx);
		    if(verItemDataList != null) {
			    String verColumnId = null;
			    Object verValue = null;
			    DTForm verDtForm = null;
			    for(Object dataObj:verItemDataList){
			    	if(dataObj instanceof DTForm) {
			    		verDtForm = (DTForm) dataObj;
			    		
			    		verColumnId = StringUtil.getString(verDtForm.get("columnId")).toLowerCase();
			    		verValue = verDtForm.get("columnValue");
	
					    targetSiteInfo.put(verColumnId, verValue);
			    	}
			    }
		    }

		    // 레이아웃 템플릿 정보
		    String layoutItemFlag = RbsProperties.getProperty("Globals.layoutTmp.item.flag");
			String layoutTmp = StringUtil.getString(parameterMap.get("layout_tmp"));
			String logoSavedName = StringUtil.getString(parameterMap.get("logo_saved_name"));
		    if(!StringUtil.isEmpty(layoutItemFlag)) {
			    // 레이아웃 템플릿 정보 setting
				String logoFileName = null;
				if(!StringUtil.isEmpty(layoutTmp) && !StringUtil.isEmpty(logoSavedName)) {
					logoFileName = "logo" + logoSavedName.substring(logoSavedName.lastIndexOf("."));
					targetSiteInfo.put("logo_file", logoFileName);
				}
		    }

		    // 3.5.5  메뉴 정보 setting 및 파일 생성
		    Set<?> set = targetMenus.keySet();
		    Iterator<?> keys = set.iterator();
		    String key = null;
		    JSONObject targetMenuInfo = null;
		    String targetIncludeTop = null;
		    String targetIncludeBottom = null;
		    String targetMenuLink = null;
		    String targetAnMenuLink = null;
		    while(keys.hasNext()){
				key = (String)keys.next();
				targetMenuInfo = JSONObjectUtil.getJSONObject(targetMenus, key);
				targetIncludeTop = JSONObjectUtil.getString(targetMenuInfo, "include_top");
				targetIncludeBottom = JSONObjectUtil.getString(targetMenuInfo, "include_bottom");
				targetMenuLink = JSONObjectUtil.getString(targetMenuInfo, "menu_link");
				targetAnMenuLink = JSONObjectUtil.getString(targetMenuInfo, "an_menu_link");
				
				if(!StringUtil.isEmpty(targetIncludeTop)) {
					targetIncludeTop = targetIncludeTop.replaceFirst(sourceLocalePath + "/", targetLocalPath + "/");
					targetMenuInfo.put("include_top", targetIncludeTop);
				}
				if(!StringUtil.isEmpty(targetIncludeBottom)) {
					targetIncludeBottom = targetIncludeBottom.replaceFirst(sourceLocalePath + "/", targetLocalPath + "/");
					targetMenuInfo.put("include_bottom", targetIncludeBottom);
				}
				if(!StringUtil.isEmpty(targetMenuLink)) {
					targetMenuLink = targetMenuLink.replaceFirst("/" + sourceSiteId + "/", "/" + siteId + "/");
					targetMenuInfo.put("menu_link", targetMenuLink);
				}
				if(!StringUtil.isEmpty(targetAnMenuLink)) {
					targetAnMenuLink = targetAnMenuLink.replaceFirst("/" + sourceSiteId + "/", "/" + siteId + "/");
					targetMenuInfo.put("an_menu_link", targetAnMenuLink);
				}
		    }
		    
		    // 메뉴 설정 파일 생성
		    MenuUtil.writeSiteObject(siteMode, siteId, targetSiteObject);
		    
		    // dashboard 설정 파일 생성
		    String dashboardModuleId = "dashboard";
			JSONObject dashboardObject = MenuUtil.getDashboardObject("/" + siteMode + "/" + sourceSiteId, sourceVerIdx, dashboardModuleId);
			if(JSONObjectUtil.isEmpty(dashboardObject)) {
				MenuUtil.writeDashboardObject("/" + siteMode + "/" + siteId, targetVerIdx, dashboardModuleId, dashboardObject);
			}
			
		    // 3.5.6 메뉴 테이블 저장
		    JSONObject menuModuleItem = ModuleUtil.getModuleItemObject("menu", 1);
		    JSONObject menuItemInfo = JSONObjectUtil.getJSONObject(menuModuleItem, "item_info");
		    JSONObject menuItems = JSONObjectUtil.getJSONObject(menuItemInfo, "items");
		    JSONArray menuItemOrder = JSONObjectUtil.getJSONArray(menuItemInfo, "ver_copyproc_order");
		    
		    List<DTForm> menuColumnList = ModuleUtil.getItemInfoColumnList(menuItems, menuItemOrder);
		    if(menuColumnList != null) menuDataList.addAll(menuColumnList);
		     if(!StringUtil.isEquals(sourceLocalePath, targetLocalPath)) {
		    	 menuDataList.add(new DTFuncForm("INCLUDE_TOP", "INCLUDE_TOP", 1, "REPLACE", sourceLocalePath + "/", targetLocalPath + "/"));
		    	 menuDataList.add(new DTFuncForm("INCLUDE_BOTTOM", "INCLUDE_BOTTOM", 1, "REPLACE", sourceLocalePath + "/", targetLocalPath + "/"));
		    	 menuDataList.add(new DTFuncForm("MENU_LINK", "MENU_LINK", 1, "REPLACE", "/" + sourceSiteId + "/", "/" + siteId + "/"));
		    	 menuDataList.add(new DTFuncForm("AN_MENU_LINK", "AN_MENU_LINK", 1, "REPLACE", "/" + sourceSiteId + "/", "/" + siteId + "/"));
		     } else {
		    	menuDataList.add(new DTForm("INCLUDE_TOP", "INCLUDE_TOP", 1));
			    menuDataList.add(new DTForm("INCLUDE_BOTTOM", "INCLUDE_BOTTOM", 1));
			    menuDataList.add(new DTForm("MENU_LINK", "MENU_LINK", 1));
			    menuDataList.add(new DTForm("AN_MENU_LINK", "AN_MENU_LINK", 1));
		    }
		    menuDataList.add(new DTForm("SITE_ID", siteId));
		    menuDataList.add(new DTForm("VER_IDX", targetVerIdx));

			// 3.5.6.1 메뉴 적용 테이블 저장
			if(!isDefault) menuSearchList.add(new DTForm("SITE_ID", sourceSiteId));
			menuParam.put("dataList", menuDataList);
			menuParam.put("searchList", menuSearchList);
			result1 = menuDAO.copyAInsert(isDefault, menuParam);				// 메뉴 적용 테이블
			
			// 3.5.6.2 메뉴 작업 테이블 저장
			if(!isDefault) menuSearchList.add(new DTForm("VER_IDX", sourceVerIdx));
			menuParam.put("searchList", menuSearchList);
			result1 = menuDAO.copyInsert(isDefault, menuParam);				// 메뉴 작업 테이블
			
			// 3.6 접속 테이블 및 프로시저 생성
			// table 생성
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
			SchemaMapper schemaMapper = (SchemaMapper)act.getBean("contactSchemaMapper");
			if(schemaMapper != null) {
				Map<String, Object> createParam = new HashMap<String, Object>();
				createParam.put("siteId", siteId.toUpperCase());
				result1 = schemaMapper.createTable(null, createParam);
			}
			
			// 3.7 banner, popup 테이블
			if(isCreateBannerTable) {
				moduleFnService.createModuleTable(bannerModuleId, bannerFnIdx, bannerFnName, null);
			}
			
			if(isCreatePopupTable) {
				moduleFnService.createModuleTable(popupModuleId, popupFnIdx, popupFnName, null);
			}
			
		    int useLayoutTmp = RbsProperties.getPropertyInt("Globals.layoutTmp.use");		// 레이아웃 템플릿 사용 여부
		    if(useLayoutTmp == 1) {
				// 3.8 레이아웃 템플릿 사용하는 경우 : 템플릿 사이트 생성
				String siteType = StringUtil.getString(parameterMap.get("site_type"));
				String localPath =  StringUtil.getString(parameterMap.get("local_path"));
				menuVerService.setLayoutTmp(siteType, null, localPath, layoutTmp, logoSavedName);
		    }
		}
		/*
		if(result > 0) {
			
			// 3.7 레이아웃 템플릿 사용하는 경우 : 템플릿 사이트 생성
			String layoutTmp = StringUtil.getString(parameterMap.get("layout_tmp"));
			String logoSavedName = StringUtil.getString(parameterMap.get("logo_saved_name"));
			String siteType = StringUtil.getString(parameterMap.get("site_type"));
			String localPath =  StringUtil.getString(parameterMap.get("local_path"));
			menuVerService.setLayoutTmp(siteType, localPath, layoutTmp, logoSavedName, localPath, layoutTmp, logoSavedName);
		}*/
		return result;
	}

	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param brdIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(int type, String uploadModulePath, String siteId, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, JSONArray fileItemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		boolean isClosedBannerItem = ModuleUtil.isClosed(itemOrder, JSONObjectUtil.getJSONObject(items, "banner_fn_idx"));
		boolean isClosedPopupItem = ModuleUtil.isClosed(itemOrder, JSONObjectUtil.getJSONObject(items, "popup_fn_idx"));

		// 배너, 팝업 기능 생성
		boolean isCreateBannerTable = false;
		boolean isCreatePopupTable = false;
		String bannerModuleId = "banner";
		int bannerFnIdx = StringUtil.getInt(parameterMap.get("banner_fn_idx"));
		String bannerFnName = null;
		String popupModuleId = "popup";
		int popupFnIdx = StringUtil.getInt(parameterMap.get("popup_fn_idx"));
		String popupFnName = null;
		
		if(!isClosedBannerItem || !isClosedPopupItem) {
			// 배너, 팝업 기능 생성
			
			JSONObject moduleFnItems = null;
			JSONArray moduleFnItemOrder = null;
			if(bannerFnIdx <= 0 || popupFnIdx <= 0) {
				JSONObject moduleFnItem = ModuleUtil.getModuleItemObject("module", 1);
				JSONObject moduleFnItemInfo = JSONObjectUtil.getJSONObject(moduleFnItem, "fn_item_info");
				moduleFnItems = JSONObjectUtil.getJSONObject(moduleFnItemInfo, "items");
				moduleFnItemOrder = JSONObjectUtil.getJSONArray(moduleFnItemInfo, "writeproc_order");
			}
			if(!isClosedBannerItem && bannerFnIdx <= 0) {
				// banner fnIdx 신규생성
				ParamForm bannerParameterMap = new ParamForm();
				bannerFnName = parameterMap.get("site_name") + " 배너";
				bannerParameterMap.put("fnName", bannerFnName);
				bannerFnIdx = moduleFnService.insert(false, null, bannerModuleId, regiIp, bannerParameterMap, moduleFnItems, moduleFnItemOrder);
				if(bannerFnIdx > 0) isCreateBannerTable = true;
				parameterMap.put("banner_fn_idx", bannerFnIdx);
			}
			
			if(!isClosedPopupItem && popupFnIdx <= 0) {
				// popup fnIdx 신규생성
				ParamForm popupParameterMap = new ParamForm();
				popupFnName = parameterMap.get("site_name") + " 팝업";
				popupParameterMap.put("fnName", bannerFnName);
				popupFnIdx = moduleFnService.insert(false, null, "popup", regiIp, popupParameterMap, moduleFnItems, moduleFnItemOrder);
				if(popupFnIdx > 0) isCreatePopupTable = true;
				parameterMap.put("popup_fn_idx", popupFnIdx);
			}
		}

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목

    	// 1. 검색조건 setting
		searchList.add(new DTForm("SITE_ID", siteId));
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

		if(type == 0) {
			// 관리자 setting : 사이트관리에서만 저장
			Object multiObjVals = parameterMap.get("manager_member_idxs");
			String[] multiVals = null;
			if(multiObjVals instanceof String[]) multiVals = (String[])multiObjVals;
			else if(multiObjVals instanceof String){
				multiVals = new String[1];
				multiVals[0] = (String)multiObjVals;
			}
			dataList.add(new DTForm("MANAGER_MEMBER_IDXS", StringUtil.getString(multiVals, ",", null)));
		}
		
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
		// 3. DB 저장
		int result = websiteDAO.update(param);
		
		if(result > 0) {
			// menu.json에 적용
			String siteMode = RbsProperties.getProperty("Globals.site.mode.usr");
			String siteORPath = "/" + siteMode + "/" + siteId;
		    JSONObject siteObject = MenuUtil.getSiteObject(siteORPath, false);					// 사이트, 메뉴정보 전체
		    JSONObject siteInfo = JSONObjectUtil.getJSONObject(siteObject, "site_info");
		    setSiteInfo(siteInfo, parameterMap, items, fileItemOrder);
		    
		    MenuUtil.writeSiteObject(siteMode, siteId, siteObject);
			
			// 3.7 banner, popup 테이블
			if(isCreateBannerTable) {
				moduleFnService.createModuleTable(bannerModuleId, bannerFnIdx, bannerFnName, null);
			}
			
			if(isCreatePopupTable) {
				moduleFnService.createModuleTable(popupModuleId, popupFnIdx, popupFnName, null);
			}
		}
		
		return result;
	}

	/**
	 * 삭제 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getDeleteCount(Map<String, Object> param) {
    	return websiteDAO.getDeleteCount(param);
    }

    /**
	 * 삭제 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return websiteDAO.getDeleteList(param);
	}

	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int delete(String[] deleteIdxs, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		
		// 1. 저장조건
		searchList.add(new DTForm("SITE_ID", deleteIdxs));
		
		// 2. 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

		param.put("searchList", searchList);
		param.put("dataList", dataList);
		
		// 3. DB 저장
		int result = websiteDAO.delete(param);
		if(result > 0) {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			HttpSession session = request.getSession(true);
			WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();

			String usrSiteId = usrSiteVO.getSiteId();
			
			String usrSiteRPath = RbsProperties.getProperty("Globals.menu.path") + "/" + RbsProperties.getProperty("Globals.site.mode.usr");
			String filePath = null;
			for(String deleteIdx : deleteIdxs) {
				
				// 선택 사용자사이트 websiteList에 속하는지 체크
				if(StringUtil.isEquals(usrSiteId, deleteIdx)) {
					session.removeAttribute("selSiteVO");
				}
				
				filePath = usrSiteRPath + "/" + deleteIdx;
				//System.out.println("-filePath:" + filePath);
				File file = new File(filePath);
				if(file.isDirectory()) {
					File deleteFile = new File(filePath + "_del");
					file.renameTo(deleteFile);
				}
			}
		}
		
		return result;
	}

	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param restoreIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	@Override
	public int restore(String[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목

		// 1. 저장조건
		searchList.add(new DTForm("SITE_ID", restoreIdxs));

		// 2. 저장 항목
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	
    	dataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
    	dataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
    	dataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
    	dataList.add(new DTForm("LAST_MODI_IP", regiIp));

		param.put("searchList", searchList);
		param.put("dataList", dataList);
		
		// 3. DB 저장
		int result = websiteDAO.restore(param);
		if(result > 0) {
			String usrSiteRPath = RbsProperties.getProperty("Globals.menu.path") + "/" + RbsProperties.getProperty("Globals.site.mode.usr");
			String filePath = null;
			for(String deleteIdx : restoreIdxs) {
				filePath = usrSiteRPath + "/" + deleteIdx;
				//System.out.println("-filePath:" + filePath);
				File file = new File(filePath + "_del");
				if(file.isDirectory()) {
					File deleteFile = new File(filePath);
					file.renameTo(deleteFile);
				}
			}
		}
		
		return result;
	}

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param uploadModulePath
	 * @param deleteIdxs
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public int cdelete(String uploadModulePath, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		
		// 1. 저장조건
		searchList.add(new DTForm("SITE_ID", deleteIdxs));
		param.put("searchList", searchList);
		
		// 2. 삭제할 파일 select
		
		// 3. delete
		int result = websiteDAO.cdelete(param);
		if(result > 0) {
			String usrSiteRPath = RbsProperties.getProperty("Globals.menu.path") + "/" + RbsProperties.getProperty("Globals.site.mode.usr");
			String filePath = null;

			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			ApplicationContext act = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
			SchemaMapper schemaMapper = (SchemaMapper)act.getBean("contactSchemaMapper");
			int result1 = 0;
			for(String deleteIdx : deleteIdxs) {
				// 3.1 메뉴파일 완전삭제
				filePath = usrSiteRPath + "/" + deleteIdx;
				FileUtil.isDelete(filePath + "_del");

				// 3.2 접속 테이블 및 프로시저 삭제
				if(schemaMapper != null) {
					Map<String, Object> createParam = new HashMap<String, Object>();
					createParam.put("siteId", deleteIdx.toUpperCase());
					result1 = schemaMapper.dropTable(createParam);
				}
			}
		}
		
		return result;
	}

}