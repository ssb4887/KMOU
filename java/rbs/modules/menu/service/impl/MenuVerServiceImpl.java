package rbs.modules.menu.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DTFuncForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.service.WebsiteService;
import com.woowonsoft.egovframework.util.DateUtil;
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
import rbs.modules.member.mapper.MemberAnMapper;
import rbs.modules.member.mapper.MemberMapper;
import rbs.modules.menu.mapper.MenuAuthLogMapper;
import rbs.modules.menu.mapper.MenuMapper;
import rbs.modules.menu.mapper.MenuMultiMapper;
import rbs.modules.menu.mapper.MenuVerMapper;
import rbs.modules.menu.service.MenuVerService;
import rbs.modules.website.mapper.WebsiteMapper;

@Service("menuVerService")
public class MenuVerServiceImpl extends EgovAbstractServiceImpl implements MenuVerService {
	
	@Resource(name = "selSiteService")
	protected WebsiteService websiteService;

	@Resource(name="websiteMapper")
	private WebsiteMapper websiteDAO;
	
	@Resource(name="menuVerMapper")
	private MenuVerMapper menuVerDAO;
	
	@Resource(name="menuMapper")
	private MenuMapper menuDAO;
	
	@Resource(name="menuMultiMapper")
	private MenuMultiMapper menuMultiDAO;

	@Resource(name="memberAnMapper")
	private MemberAnMapper memberAnDAO;

	@Resource(name="memberMapper")
	private MemberMapper memberDAO;

	@Resource(name="menuAuthLogMapper")
	private MenuAuthLogMapper menuAuthLogDAO;

	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;
	
	private Logger logger = LoggerFactory.getLogger("manageContentsAuth");
	
	@Override
	public List<Object> getOptnList(Map<String, Object> param) {
		return menuVerDAO.getOptnList(param);
	}
	
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getTotalCount(Map<String, Object> param) {
    	return menuVerDAO.getTotalCount(param);
    }

    /**
	 * 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getList(Map<String, Object> param) {
		return menuVerDAO.getList(param);
	}

	/**
	 * 수정 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public DataMap getModify(Map<String, Object> param) {
    	DataMap viewDAO = menuVerDAO.getModify(param);
		return viewDAO;
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
	public int insert(String uploadModulePath, String siteId, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		//고유아이디 셋팅
		Map<String, Object> sparam1 = new HashMap<String, Object>();
		List<DTForm> searchList1 = new ArrayList<DTForm>();
		searchList1.add(new DTForm("A.SITE_ID", siteId));
		sparam1.put("searchList", searchList1);
		int verIdx = menuVerDAO.getNextId(sparam1);

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		Map<String, Object> menuParam = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> menuSearchList = new ArrayList<DTForm>();						// 검색항목
		List<DTForm> menuDataList = new ArrayList<DTForm>();						// 저장항목
		
		// 3. 저장 data setting : 사이트, 버전, 메뉴
		dataList.add(new DTForm("SITE_ID", siteId));
    	dataList.add(new DTForm("VER_IDX", verIdx));
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
    	menuDataList.addAll(dataList);
    	
		// 3.1 항목설정으로 사이트 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;

		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);

    	param.put("dataList", dataList);
    	
    	// 3.3 테이블 저장
		int result = menuVerDAO.insert(param);
		
		if(result > 0) {
			
			// 3.5 메뉴 저장
			boolean isDefault = false;
			String sourceSiteId = siteId;
			int sourceVerIdx = StringUtil.getInt(parameterMap.get("copy_ver_idx"));
			
			// 3.5.2 복제버전 없는 경우 기본 사이트 복제
			if(sourceVerIdx <= 0) {
				isDefault = true;
				sourceSiteId = "default";
				sourceVerIdx = 1;
			}

			// 3.5.3 메뉴설정 정보 얻기
			String siteMode = RbsProperties.getProperty("Globals.site.mode.usr");
			JSONObject targetSiteObject = MenuUtil.getSiteObject("/" + siteMode + "/" + siteId, false);
			JSONObject targetSiteInfo = JSONObjectUtil.getJSONObject(targetSiteObject, "site_info");
			// 기본경로
			String sourceLocalePath = JSONObjectUtil.getString(targetSiteInfo, "local_path");
		    String targetLocalPath = StringUtil.getString(parameterMap.get("local_path"));

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
			
			// 3.5.6.2 메뉴 작업 테이블 저장
			if(!isDefault) {
				menuSearchList.add(new DTForm("SITE_ID", sourceSiteId));
				menuSearchList.add(new DTForm("VER_IDX", sourceVerIdx));
			}
			menuParam.put("dataList", menuDataList);
			menuParam.put("searchList", menuSearchList);
			int result1 = menuDAO.copyInsert(isDefault, menuParam);				// 메뉴 작업 테이블
			
			int useLayoutTmp = RbsProperties.getPropertyInt("Globals.layoutTmp.use");		// 레이아웃 템플릿 사용 여부
		    if(useLayoutTmp == 1) {
				// 6.5 레이아웃 템플릿 사용하는 경우 : 템플릿 사이트 생성
		    	WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
				String localPath = parameterMap.getString("local_path");
				String layoutTmp = parameterMap.getString("layout_tmp");
			    setLayoutTmp(usrSiteVO.getSiteType(), null, localPath, layoutTmp, null);
		    }
		}
		
		return result;
	}

	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param siteId
	 * @param verIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int update(String uploadModulePath, String siteId, int verIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목

    	// 1. 검색조건 setting
		searchList.add(new DTForm("SITE_ID", siteId));
		searchList.add(new DTForm("VER_IDX", verIdx));
		
		// 2. 항목설정으로 저장항목 setting
		String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + uploadModulePath;
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(fileRealPath, parameterMap, settingInfo, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		// 2.1 저장항목
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
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
		int result = menuVerDAO.update(param);
		
		if(result > 0) {
			// 7. file(단일항목) 삭제
			List deleteFileList = StringUtil.getList(dataMap.get("deleteFileList"));
			if(deleteFileList != null) {
				FileUtil.isDelete(fileRealPath, deleteFileList);
			}
			
			int useLayoutTmp = RbsProperties.getPropertyInt("Globals.layoutTmp.use");		// 레이아웃 템플릿 사용 여부
		    if(useLayoutTmp == 1) {
				// 6.5 레이아웃 템플릿 사용하는 경우 : 템플릿 사이트 생성
		    	WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
				String localPath = parameterMap.getString("local_path");
				String layoutTmp = parameterMap.getString("layout_tmp");
			    setLayoutTmp(usrSiteVO.getSiteType(), null, localPath, layoutTmp, null);
		    }
		}
		
		return result;
	}
/*
	public JSONObject getMenuTotSearchJSONObject(Map<String, Object> param) {
		boolean isApply = StringUtil.getBoolean(param.get("isApply"));
		List<Object> menuList = null;
		if(isApply) menuList = menuDAO.getApplyMenuList(param); //websiteDAO.getApplyLogMenuList(param);
		else menuList = menuDAO.getMenuList(param);
		if(menuList == null) return null;
		Map<String, List<Object>> multiMap = null;
		if(isApply) multiMap = menuMultiDAO.getApplyMenuMultiMapList(param); //websiteDAO.getApplyLogMenuMultiMapList(param);
		else multiMap = menuMultiDAO.getMenuMultiMapList(param);
		
		JSONObject urMenuTotalObject = null;

        JSONObject menus = new JSONObject();
        JSONObject menuObject = null;
        JSONMap menuDt = null;
        int menuSize = menuList.size();
        for(int i = 0 ; i < menuSize ; i ++){
        	menuDt = (JSONMap)menuList.get(i);
        	List<Object> groupList = multiMap.get(menuDt.get("site_id") + "," + menuDt.get("menu_idx") + "," + "groupIdxs");
        	List<Object> departList = multiMap.get(menuDt.get("site_id") + "," + menuDt.get("menu_idx") + "," + "departIdxs");
        	List<Object> memberList = multiMap.get(menuDt.get("site_id") + "," + menuDt.get("menu_idx") + "," + "memberIdxs");
        	List<Object> managerGroupList = multiMap.get(menuDt.get("site_id") + "," + menuDt.get("menu_idx") + "," + "managerGroupIdxs");
        	List<Object> managerDepartList = multiMap.get(menuDt.get("site_id") + "," + menuDt.get("menu_idx") + "," + "managerDepartIdxs");
        	List<Object> managerMemberList = multiMap.get(menuDt.get("site_id") + "," + menuDt.get("menu_idx") + "," + "managerMemberIdxs");
        	
        	menuDt.remove("site_id");
        	menuDt.remove("isdelete");
        	menuDt.remove("ver_idx");
        	menuDt.remove("regi_idx");
        	menuDt.remove("regi_id");
        	menuDt.remove("regi_name");
        	menuDt.remove("regi_date");
        	menuDt.remove("regi_ip");
        	menuDt.remove("last_modi_idx");
        	menuDt.remove("last_modi_id");
        	menuDt.remove("last_modi_name");
        	menuDt.remove("last_modi_date");
        	menuDt.remove("last_modi_ip");
        	menuObject = new JSONObject();
        	menuObject.putAll((Map)menuDt);
        	
        	String groupIdxs = StringUtil.getJSONListToString(groupList, "ITEM_KEY", ",");
        	String departIdxs = StringUtil.getJSONListToString(departList, "ITEM_KEY", ",");
        	String memberIdxs = StringUtil.getJSONListToString(memberList, "ITEM_KEY", ",");
        	String managerGroupIdxs = StringUtil.getJSONListToString(managerGroupList, "ITEM_KEY", ",");
        	String managerDepartIdxs = StringUtil.getJSONListToString(managerDepartList, "ITEM_KEY", ",");
        	String managerMemberIdxs = StringUtil.getJSONListToString(managerMemberList, "ITEM_KEY", ",");
        	
        	menuObject.put("group_idxs", groupIdxs);
        	menuObject.put("depart_idxs", departIdxs);
        	menuObject.put("member_idxs", memberIdxs);
        	menuObject.put("manager_group_idxs", managerGroupIdxs);
        	menuObject.put("manager_depart_idxs", managerDepartIdxs);
        	menuObject.put("manager_member_idxs", managerMemberIdxs);
        	
        	menus.put("menu" + menuDt.get("menu_idx"), menuObject);
        }
        
		JSONArray menuListArray = MenuUtil.getListToJSON(0, 100, 0, 0, menuList, 0);
		String[] totsearchModuleIds = StringUtil.stringToArray(RbsProperties.getProperty("Globals.totsearch.module_ids"), ",");
		JSONObject totSearchJson = (!isApply)?MenuUtil.getTotSearchListToJSON(0, 100, 0, 0, menuList, 0, totsearchModuleIds):null;	// 통합검색사용목록 - 적용메뉴아닌 경우
		int menusSize = menus.size();
		int menuListSize = 0;
		if(menuListArray != null && !menuListArray.isEmpty()) menuListSize = menuListArray.size();
		if(menusSize > 0 || menuListSize > 0 || totSearchJson != null && !totSearchJson.isEmpty()) {
			urMenuTotalObject = new JSONObject(); 
	        if(menuSize > 0) urMenuTotalObject.put("menus", menus);
	        if(menuListSize > 0) urMenuTotalObject.put("menu-list", menuListArray);
	        if(totSearchJson != null && !totSearchJson.isEmpty()) urMenuTotalObject.put("totsearch-lists", totSearchJson);
		}
		return urMenuTotalObject;
	}*/
	
	/**
	 * 적용처리
	 * @param siteId
	 * @param verIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param menuSettingInfo
	 * @param menuItems
	 * @param menuItemOrder
	 * @return
	 * @throws Exception
	 */
	@Override
	public int apply(String siteId, int verIdx, String regiIp, ParamForm parameterMap, JSONObject menuSettingInfo, JSONObject menuItems, JSONArray menuItemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(menuItems) || JSONObjectUtil.isEmpty(menuItemOrder)) return -1;
		
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		List<DTForm> menuDataList = new ArrayList<DTForm>();					// 메뉴 저장항목
		
		// 1. 버전, 메뉴정보 얻기
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("A.SITE_ID", siteId));
		searchList.add(new DTForm("A.VER_IDX", verIdx));
    	param.put("searchList", searchList);
    	DataMap verDt = menuVerDAO.getModify(param);
    	JSONObject menuJSONObject = websiteService.getMenuJSONObject(param);
    	
    	param.put("isApply", true);
    	JSONObject applyMenuJSONObject = websiteService.getMenuJSONObject(param);;
		
		// 버전, 메뉴정보 없는 경우
		if(verDt == null || menuJSONObject == null) return -11;
		
		// 2. 선택버전 적용처리
    	// 2.1. 검색조건 setting
		param = new HashMap<String, Object>();
		searchList = new ArrayList<DTForm>();
		searchList.add(new DTForm("SITE_ID", siteId));
		searchList.add(new DTForm("VER_IDX", verIdx));
		
		// 2.2 저장항목
		dataList = new ArrayList<DTForm>();
		String loginMemberIdx = null;
		String loginMemberId = null;
		String loginMemberName = null;
		if(loginVO != null) {
			loginMemberIdx = loginVO.getMemberIdx();
			loginMemberId = loginVO.getMemberId();
			loginMemberName = loginVO.getMemberName();
		}
    	dataList.add(new DTForm("APPLY_IDX", loginMemberIdx));
    	dataList.add(new DTForm("APPLY_ID", loginMemberId));
    	dataList.add(new DTForm("APPLY_NAME", loginMemberName));
    	dataList.add(new DTForm("APPLY_IP", regiIp));

    	param.put("dataList", dataList);
    	param.put("searchList", searchList);
    	
    	// 2.3 저장
		int result = menuVerDAO.apply(param);
		
		if(result > 0) {
		
			// 3. 기존 적용버전 취소
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("SITE_ID", siteId));
			searchList.add(new DTForm("VER_IDX", verIdx, "<>"));
	    	param.put("searchList", searchList);
	    	int result1 = menuVerDAO.applyCancel(param);
			
			// 4. 기존 적용메뉴 삭제
			param = new HashMap<String, Object>();
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("SITE_ID", siteId));
	    	param.put("searchList", searchList);
	    	result1 = menuDAO.applyCdelete(param);
			//menuMultiDAO.applyCdelete(param);
			
			// 5. 적용 테이블에 저장
			param = new HashMap<String, Object>();
		    menuDataList.add(new DTForm("SITE_ID", siteId));
		    menuDataList.add(new DTForm("VER_IDX", verIdx));
	
		    menuDataList.add(new DTForm("REGI_IDX", loginMemberIdx));
		    menuDataList.add(new DTForm("REGI_ID", loginMemberId));
		    menuDataList.add(new DTForm("REGI_NAME", loginMemberName));
		    menuDataList.add(new DTForm("REGI_IP", regiIp));
	    	
		    menuDataList.add(new DTForm("LAST_MODI_IDX", loginMemberIdx));
		    menuDataList.add(new DTForm("LAST_MODI_ID", loginMemberId));
		    menuDataList.add(new DTForm("LAST_MODI_NAME", loginMemberName));
		    menuDataList.add(new DTForm("LAST_MODI_IP", regiIp));
	    	
		    List<DTForm> menuColumnList = ModuleUtil.getItemInfoColumnList(menuItems, menuItemOrder);
		    if(menuColumnList != null) menuDataList.addAll(menuColumnList);
	    	param.put("dataList", menuDataList);
			searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.SITE_ID", siteId));
			searchList.add(new DTForm("A.VER_IDX", verIdx));
	    	param.put("searchList", searchList);
	    	result1 = menuDAO.apply(param);
	    	result1 = menuMultiDAO.apply(param);
			
			
			// 6. 파일저장
			
			// 6.1 메뉴파일 읽기
			String siteMode = RbsProperties.getProperty("Globals.site.mode.usr");
			JSONObject targetSiteObject = new JSONObject();
			JSONObject sourceSiteObject = null;
			
			sourceSiteObject = MenuUtil.getSiteObject("/" + siteMode + "/" + siteId, false);
			if(sourceSiteObject != null) targetSiteObject.putAll(sourceSiteObject);
			
			JSONObject targetSiteInfo = JSONObjectUtil.getJSONObject(targetSiteObject, "site_info");
			
			// 6.2 버전 정보 setting
			String siteModuleId = "website";
			int siteFnIdx = 1;
			String localPath = StringUtil.getString(verDt.get("LOCAL_PATH"));
			String layoutTmp = StringUtil.getString(verDt.get("LAYOUT_TMP"));
			String logoSavedName = StringUtil.getString(verDt.get("LOGO_SAVED_NAME"));
			
			/*String applyLocalPath = JSONObjectUtil.getString(targetSiteInfo, "local_path");
			String applyLayoutTmp = JSONObjectUtil.getString(targetSiteInfo, "layout_tmp");*/
			String applyLogoSavedName = JSONObjectUtil.getString(targetSiteInfo, "logo_saved_name");
			
			JSONObject siteItemObject = ModuleUtil.getModuleItemObject(siteModuleId, siteFnIdx);
			JSONObject verItemInfo = JSONObjectUtil.getJSONObject(siteItemObject, "version_item_info");
			JSONObject verItems = JSONObjectUtil.getJSONObject(verItemInfo, "items");
			String layoutItemFlag = RbsProperties.getProperty("Globals.layoutTmp.item.flag");
			JSONArray siteOrder = JSONObjectUtil.getJSONArray(verItemInfo, layoutItemFlag + "site_order");
		    targetSiteInfo.put("version", verIdx);
		    String verItemId = null;
		    JSONObject verItem = null;
		    String verColumnId = null;
		    int verObjectType = 0;
		    for(Object verItemIdObj:siteOrder) {
		    	if(verItemIdObj instanceof String) {
		    		verItemId = (String)verItemIdObj;
		    		verItem = JSONObjectUtil.getJSONObject(verItems, verItemId);
		    		verColumnId = JSONObjectUtil.getString(verItem, "column_id");
		    		verObjectType = JSONObjectUtil.getInt(verItem, "object_type");
				    targetSiteInfo.put(verItemId, verDt.get(verColumnId));
				    
				    if(verObjectType == 6) {
				    	targetSiteInfo.put(verItemId + "_saved_name", verDt.get(verColumnId + "_SAVED_NAME"));
				    	targetSiteInfo.put(verItemId + "_origin_name", verDt.get(verColumnId + "_ORIGIN_NAME"));
				    	targetSiteInfo.put(verItemId + "_size", verDt.get(verColumnId + "_SIZE"));
				    	targetSiteInfo.put(verItemId + "_text", verDt.get(verColumnId + "_TEXT"));
				    }
		    	}
		    }
		    /*targetSiteInfo.put("local_path", verDt.get("LOCAL_PATH"));
		    targetSiteInfo.put("include_top", verDt.get("INCLUDE_TOP"));
		    targetSiteInfo.put("include_bottom", verDt.get("INCLUDE_BOTTOM"));*/
		    // 레이아웃 템플릿 정보 setting
			String logoFileName = null;
			if(!StringUtil.isEmpty(layoutItemFlag) && !StringUtil.isEmpty(layoutTmp) && !StringUtil.isEmpty(logoSavedName)) {
				logoFileName = "logo" + logoSavedName.substring(logoSavedName.lastIndexOf("."));
				targetSiteInfo.put("logo_file", logoFileName);
			}
			
			// 6.3 메뉴정보 저장
			targetSiteObject.putAll(menuJSONObject);
		    
			// 6.4 파일생성
		    MenuUtil.writeSiteObject(siteMode, siteId, targetSiteObject);

		    int useLayoutTmp = RbsProperties.getPropertyInt("Globals.layoutTmp.use");		// 레이아웃 템플릿 사용 여부
		    if(useLayoutTmp == 1) {
				// 6.5 레이아웃 템플릿 사용하는 경우 : 템플릿 사이트 생성
		    	WebsiteVO usrSiteVO = (WebsiteVO) WebsiteDetailsHelper.getWebsiteInfo();
			    setLayoutTmp(usrSiteVO.getSiteType(), /*applyLocalPath, applyLayoutTmp, */applyLogoSavedName, localPath, layoutTmp, logoSavedName);
		    }
		    			
		    JSONArray menuArray = JSONObjectUtil.getJSONArray(menuJSONObject, "menu-list");
			JSONObject menus = JSONObjectUtil.getJSONObject(menuJSONObject, "menus");
			JSONObject applyMenus = JSONObjectUtil.getJSONObject(applyMenuJSONObject, "menus");
			
			setMenuManageAuthLog(null, menus, applyMenus, menuArray);
		}
		
		return result;
	}
	
	/**
	 * 템플릿 사이트 생성
	 * @param siteType
	 * @param applyLocalPath
	 * @param applyLayoutTmp
	 * @param applyLogoSavedName
	 * @param localPath
	 * @param layoutTmp
	 * @param logoSavedName
	 * @throws Exception
	 */
	@Override
	public void setLayoutTmp(String siteType, String applyLogoSavedName, String localPath, String layoutTmp, String logoSavedName) throws Exception {

	    if(StringUtil.isEmpty(layoutTmp)) return;
	    String usrJSPRootPath = RbsProperties.getProperty("Globals.layoutTmp.usr.jsp.path");
    	String layoutBackupRootPath = RbsProperties.getProperty("Globals.layoutTmp.usr.backup.path");
    	
    	// 적용할 local_path 경로
		String usrJSPLocalPath = usrJSPRootPath + localPath;
		File usrJSPFile = new File(usrJSPLocalPath);
		if(!usrJSPFile.isDirectory()) {
			// 레이아웃 템플릿 (jsp include 파일) 경로
			String layoutTmpPath = RbsProperties.getProperty("Globals.layoutTmp.path") + "/" + siteType  + "/" + layoutTmp + "/" + RbsProperties.getProperty("Globals.layoutTmp.jsp.file");
		
			// 적용할 local_path에 템플릿 압축풀기
			FileUtil.isUnZip(layoutTmpPath, usrJSPLocalPath, false);
		}
		
		if(!StringUtil.isEmpty(logoSavedName) && !StringUtil.isEquals(logoSavedName, applyLogoSavedName)) {
			// 레이아웃 템플릿 사용하는 경우 && (로고파일 수정된 경우 || local_path가 수정된 경우)
			String fileRealPath = RbsProperties.getProperty("Globals.upload.file.path") + File.separator + "website" + File.separator + "version" + File.separator + "1";
			File logoSavedFile = new File(fileRealPath, logoSavedName);
			if(!logoSavedFile.isFile()) {
				File siteLogoSavedFile = new File(RbsProperties.getProperty("Globals.upload.file.path") + File.separator + "website" + File.separator + "1", logoSavedName);
				if(siteLogoSavedFile.isFile()) {
		    		File verLogoParent = new File(logoSavedFile.getParent());
		    		if(!verLogoParent.exists()) verLogoParent.mkdirs();
		    		siteLogoSavedFile.renameTo(logoSavedFile);
				}
			}
			if(logoSavedFile.isFile()) {
				String logoPath = RbsProperties.getProperty("Globals.layoutTmp.usr.logo.path");
				String logoExtName = logoSavedName.substring(logoSavedName.lastIndexOf("."));
				
				// logo파일 backup
				FileUtil.isCreateBackupFile(logoPath + localPath + File.separator + "logo" + logoExtName, layoutBackupRootPath + localPath);
				File logoFile = new File(logoPath + localPath, "logo" + logoExtName);
								
				// logo파일 복제
	    		FileUtil.copy(logoSavedFile, logoFile);
			}
		}
		
	    // 백업 일주일전 파일 삭제
	    int deleteDate = RbsProperties.getPropertyInt("Globals.module.backup.xml.delete.date", 0);
	    FileUtil.isBkDelete(layoutBackupRootPath + localPath, deleteDate);
	}

	/**
	 * 삭제 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
    @Override
	public int getDeleteCount(Map<String, Object> param) {
    	return menuVerDAO.getDeleteCount(param);
    }

    /**
	 * 삭제 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		return menuVerDAO.getDeleteList(param);
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
	public int delete(String siteId, String[] deleteIdxs, String regiIp) throws Exception {
		if(deleteIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목
		
		// 1. 저장조건
		searchList.add(new DTForm("SITE_ID", siteId));
		searchList.add(new DTForm("VER_IDX", deleteIdxs));
		
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
		int result = menuVerDAO.delete(param);		
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
	public int restore(String siteId, String[] restoreIdxs, String regiIp) throws Exception {
		if(restoreIdxs == null) return 0;

		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		List<DTForm> dataList = new ArrayList<DTForm>();						// 저장항목

		// 1. 저장조건
		searchList.add(new DTForm("SITE_ID", siteId));
		searchList.add(new DTForm("VER_IDX", restoreIdxs));

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
		int result = menuVerDAO.restore(param);		
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
	public int cdelete(String siteId, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception {
		if(deleteIdxs == null) return 0;
		
		Map<String, Object> param = new HashMap<String, Object>();				// mapper parameter 데이터
		List<DTForm> searchList = new ArrayList<DTForm>();						// 검색조건
		
		// 1. 저장조건
		searchList.add(new DTForm("SITE_ID", siteId));
		searchList.add(new DTForm("VER_IDX", deleteIdxs));
		param.put("searchList", searchList);
		
		// 2. 삭제할 파일 select
		
		// 3. delete
		int result = menuVerDAO.cdelete(param);		
		return result;
	}
	@Override
	public void setMenuManageAuthLog(String menuName, JSONObject menus, JSONObject applyMenus, JSONArray menuArray) throws Exception {
		if(JSONObjectUtil.isEmpty(menuArray)) return;
		for (int i = 0; i < menuArray.size(); i++) {
			int menuIdx = JSONObjectUtil.getInt(JSONObjectUtil.getJSONObject(menuArray, i), "menu_idx");
			String menuKey = "menu" + menuIdx;
			
			JSONObject menu = JSONObjectUtil.getJSONObject(menus, menuKey);
			JSONObject applyMenu = JSONObjectUtil.getJSONObject(applyMenus, menuKey);

			StringBuffer totalMenuName = new StringBuffer();
			
			if(!StringUtil.isEmpty(menuName)) totalMenuName.append(menuName + " > ");
			totalMenuName.append(JSONObjectUtil.getString(menu, "menu_name", JSONObjectUtil.getString(applyMenu, "menu_name")));
			
			String totalMenuSbStr = totalMenuName.toString();
			
			setMenuManageAuthLog(totalMenuSbStr, menu, applyMenu, "manager_");
			
			JSONArray childMenuArray = JSONObjectUtil.getJSONArray(JSONObjectUtil.getJSONObject(menuArray, i), "menu-list");
			if(!JSONObjectUtil.isEmpty(childMenuArray)) setMenuManageAuthLog(totalMenuSbStr, menus, applyMenus, childMenuArray);
		}
	}

	public void setMenuManageAuthLog(String menuName, JSONObject menu, JSONObject applyMenu, String flag) throws Exception {
		int menuIdx = JSONObjectUtil.getInt(menu, "menu_idx", JSONObjectUtil.getInt(applyMenu, "menu_idx"));
		
		Map<String, Object> menuMbrMap = setMenuMbrMap(menu, flag);

		Map<String, Object> applyMenuMbrMap = setMenuMbrMap(applyMenu, flag);
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		
		JSONObject siteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("siteInfo"));
		JSONObject usrSiteInfo = JSONObjectUtil.getJSONObject(request.getAttribute("usrSiteInfo"));
		
		String siteId = StringUtil.replaceAll(JSONObjectUtil.getString(siteInfo, "local_path"), "/", "");
		String siteName = JSONObjectUtil.getString(siteInfo, "site_name");
		int siteVerIdx = JSONObjectUtil.getInt(siteInfo, "version");
		
		if(!JSONObjectUtil.isEmpty(usrSiteInfo)){
			siteId = StringUtil.replaceAll(JSONObjectUtil.getString(usrSiteInfo, "local_path"), "/", "");
			siteName = JSONObjectUtil.getString(usrSiteInfo, "site_name");
			siteVerIdx = JSONObjectUtil.getInt(usrSiteInfo, "version");;
		}
		
		setMenuManageAuthLog(siteId, siteName, siteVerIdx, menuIdx, menuName, (Map<String, Object>)menuMbrMap.get("usertypeMbrMap"), (Map<String, Object>)applyMenuMbrMap.get("usertypeMbrMap"), rbsMessageSource.getMessage("message.auth.usertype"), "USERTYPE_NAME");	// 사용자유형
		setMenuManageAuthLog(siteId, siteName, siteVerIdx, menuIdx, menuName, (Map<String, Object>)menuMbrMap.get("groupMbrMap"), (Map<String, Object>)applyMenuMbrMap.get("groupMbrMap"), rbsMessageSource.getMessage("message.auth.group"), "GROUP_NAME");				// 그룹
		setMenuManageAuthLog(siteId, siteName, siteVerIdx, menuIdx, menuName, (Map<String, Object>)menuMbrMap.get("departMbrMap"), (Map<String, Object>)applyMenuMbrMap.get("departMbrMap"), rbsMessageSource.getMessage("message.auth.depart"), "DEPART_NAME");			// 부서
		setMenuManageAuthLog(siteId, siteName, siteVerIdx, menuIdx, menuName, (Map<String, Object>)menuMbrMap.get("mbrMap"), (Map<String, Object>)applyMenuMbrMap.get("mbrMap"), rbsMessageSource.getMessage("message.auth.member"), null);									// 회원
	}

	public Map<String, Object> setMenuMbrMap(JSONObject menu, String flag){
		Map<String, Object> menuMbrMap = new HashMap<String, Object>();
		
		String usertypeIdx = JSONObjectUtil.getString(menu, flag + "usertype_idx");
		Map<String, Object> usertypeMbrMap = null;
		if(!StringUtil.isEmpty(usertypeIdx)) usertypeMbrMap = memberAnDAO.getMap(setUsertypeParam(usertypeIdx));
		menuMbrMap.put("usertypeMbrMap", usertypeMbrMap);

		String groupIdxs = JSONObjectUtil.getString(menu, flag + "group_idxs");
		Map<String, Object> groupMbrMap = null;
		if(!StringUtil.isEmpty(groupIdxs)) groupMbrMap = memberDAO.getGrupMemberMap(setGroupParam(groupIdxs));
		menuMbrMap.put("groupMbrMap", groupMbrMap);
		
		String departIdxs = JSONObjectUtil.getString(menu, flag + "depart_idxs");
		Map<String, Object> departMbrMap = null; 
		if(!StringUtil.isEmpty(departIdxs)) departMbrMap = memberAnDAO.getDprtMbrMap(setDepartParam(departIdxs));
		menuMbrMap.put("departMbrMap", departMbrMap);

		String memberIdxs = JSONObjectUtil.getString(menu, flag + "member_idxs");
		Map<String, Object> mbrMap = null;
		if(!StringUtil.isEmpty(memberIdxs)) mbrMap = memberAnDAO.getMap(setMemberParam(memberIdxs));
		menuMbrMap.put("mbrMap", mbrMap);
		
		return menuMbrMap;
	}
	
	/**
	 * 메뉴 관리권한 부여/말소 로그
	 * @param siteId
	 * @param siteName
	 * @param siteVerIdx
	 * @param menuIdx
	 * @param menuName
	 * @param mbrMap
	 * @param applyMbrMap
	 * @param authType
	 * @param authTypeKey
	 * @throws Exception
	 */
	public void setMenuManageAuthLog(String siteId, String siteName, int siteVerIdx, int menuIdx, String menuName, Map<String, Object> mbrMap, Map<String, Object> applyMbrMap, String authType, String authTypeKey) throws Exception {
		String authName = rbsMessageSource.getMessage("item.menu.auth.name");
		String grant = rbsMessageSource.getMessage("message.auth.grant");
		String cancel = rbsMessageSource.getMessage("message.auth.cancel");
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();	// 로그인 사용자 정보
		/*
		WebsiteVO usrSiteVO = (WebsiteVO)WebsiteDetailsHelper.getWebsiteInfo();
		String siteId = null;
		String siteName = null;
		int siteVerIdx = 0;
		if(usrSiteVO != null){
			siteId = usrSiteVO.getSiteId();
			siteName = usrSiteVO.getSiteName();
			siteVerIdx = usrSiteVO.getVerIdx();
		}
		*/
		
		String regiMemberIdx = null;
		String regiMemberId = null;
		String regiMemberName = null;
		if(loginVO != null){
			regiMemberIdx = loginVO.getMemberIdx();
			regiMemberId = loginVO.getMemberId();
			regiMemberName = loginVO.getMemberName();
		}
		
		String regiDate = DateUtil.getThisDate("yyyy-MM-dd HH:mm:ss");
		String regiIp = request.getRemoteAddr();

		boolean isNullUsertypeMbrMap = (mbrMap == null);
		boolean isNullApplyUsertypeMbrMap = (applyMbrMap == null);

		if(!isNullApplyUsertypeMbrMap){
			for(String key : applyMbrMap.keySet()) {
				/*
				if(isNullUsertypeMbrMap) break; 

				DataMap dt = (DataMap)mbrMap.get(key);
				DataMap applyDt = (DataMap)applyMbrMap.get(key);
				*/

				DataMap dt = null;
				DataMap applyDt = (DataMap)applyMbrMap.get(key);;
				if(!isNullUsertypeMbrMap) dt = (DataMap)mbrMap.get(key);
				
				if(dt == null){
					String authTypeVal = StringUtil.getString(applyDt.get(authTypeKey));

					if(RbsProperties.getPropertyInt("Globals.auth.log.use.db") == 1){
						// DB사용하는 경우
						Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
						searchList.add(new DTForm("SITE_ID", siteId));
						searchList.add(new DTForm("VER_IDX", siteVerIdx));
						searchList.add(new DTForm("MENU_IDX", menuIdx));
						param.put("searchList", searchList);
						long logIdx = menuAuthLogDAO.getNextId(param);

						String memberId = ModuleUtil.getMemberItemOrgValue("mbrId", StringUtil.getString(applyDt.get("MEMBER_ID")));
						String memberName = ModuleUtil.getMemberItemOrgValue("mbrName", StringUtil.getString(applyDt.get("MEMBER_NAME")));
						List<DTForm> dataList = new ArrayList<DTForm>();
						dataList.add(new DTForm("LOG_IDX", logIdx));
						dataList.add(new DTForm("AUTH_TYPE", authType));
						dataList.add(new DTForm("AUTH_TYPE_VAL", authTypeVal));
						dataList.add(new DTForm("MEMBER_IDX", applyDt.get("MEMBER_IDX")));
						dataList.add(new DTForm("MEMBER_ID", memberId));
						dataList.add(new DTForm("MEMBER_NAME", memberName));
						dataList.add(new DTForm("SITE_ID", siteId));
						dataList.add(new DTForm("VER_IDX", siteVerIdx));
						dataList.add(new DTForm("MENU_IDX", menuIdx));
						dataList.add(new DTForm("SITE_NAME", siteName));
						dataList.add(new DTForm("MENU_NAME", menuName));
						dataList.add(new DTForm("AUTH_NAME", authName));
						dataList.add(new DTForm("LOG_TYPE", cancel));		// 상태(부여/말소)
						dataList.add(new DTForm("REGI_IDX", regiMemberIdx));
						dataList.add(new DTForm("REGI_ID", regiMemberId));
						dataList.add(new DTForm("REGI_NAME", regiMemberName));
						dataList.add(new DTForm("REGI_IP", regiIp));

						param.put("dataList", dataList);
						menuAuthLogDAO.insert(param);
					}else{
						// 로그파일 사용하는 경우
						logger.info(authType + ", " + authTypeVal + ", " + applyDt.get("MEMBER_IDX") + ", " + applyDt.get("MEMBER_ID") + ", " + applyDt.get("MEMBER_NAME") 
								+ ", " + siteId + ", " + siteVerIdx + ", " + menuIdx + ", " + siteName + " " + menuName + ", " + authName + ", " + cancel
								+ ", " + regiMemberIdx + ", " + regiMemberId + ", " + regiMemberName + ", " + regiDate + ", " + regiIp);
					}
				}
			}
		}
		
		if(!isNullUsertypeMbrMap){
			for(String key : mbrMap.keySet()){
				DataMap dt = (DataMap)mbrMap.get(key);
				DataMap applyDt = null;
				if(!isNullApplyUsertypeMbrMap) applyDt = (DataMap)applyMbrMap.get(key);
				
				if(applyDt == null){
					String authTypeVal = StringUtil.getString(dt.get(authTypeKey));
					if(RbsProperties.getPropertyInt("Globals.auth.log.use.db") == 1){
						// DB사용하는 경우
						Map<String, Object> param = new HashMap<String, Object>();
						List<DTForm> searchList = new ArrayList<DTForm>();
						searchList.add(new DTForm("SITE_ID", siteId));
						searchList.add(new DTForm("VER_IDX", siteVerIdx));
						searchList.add(new DTForm("MENU_IDX", menuIdx));
						param.put("searchList", searchList);
						long logIdx = menuAuthLogDAO.getNextId(param);

						String memberId = ModuleUtil.getMemberItemOrgValue("mbrId", StringUtil.getString(dt.get("MEMBER_ID")));
						String memberName = ModuleUtil.getMemberItemOrgValue("mbrName", StringUtil.getString(dt.get("MEMBER_NAME")));
						List<DTForm> dataList = new ArrayList<DTForm>();
						dataList.add(new DTForm("LOG_IDX", logIdx));
						dataList.add(new DTForm("AUTH_TYPE", authType));
						dataList.add(new DTForm("AUTH_TYPE_VAL", authTypeVal));
						dataList.add(new DTForm("MEMBER_IDX", dt.get("MEMBER_IDX")));
						dataList.add(new DTForm("MEMBER_ID", memberId));
						dataList.add(new DTForm("MEMBER_NAME", memberName));
						dataList.add(new DTForm("SITE_ID", siteId));
						dataList.add(new DTForm("VER_IDX", siteVerIdx));
						dataList.add(new DTForm("MENU_IDX", menuIdx));
						dataList.add(new DTForm("SITE_NAME", siteName));
						dataList.add(new DTForm("MENU_NAME", menuName));
						dataList.add(new DTForm("AUTH_NAME", authName));
						dataList.add(new DTForm("LOG_TYPE", grant));		// 상태(부여/말소)
						dataList.add(new DTForm("REGI_IDX", regiMemberIdx));
						dataList.add(new DTForm("REGI_ID", regiMemberId));
						dataList.add(new DTForm("REGI_NAME", regiMemberName));
						dataList.add(new DTForm("REGI_IP", regiIp));

						param.put("dataList", dataList);
						menuAuthLogDAO.insert(param);
					}else{
						logger.info(authType + ", " + authTypeVal + ", " + dt.get("MEMBER_IDX") + ", " + dt.get("MEMBER_ID") + ", " + dt.get("MEMBER_NAME") 
								+ ", " + siteId + ", " + siteVerIdx + ", " + menuIdx + ", " + siteName + " " + menuName + ", " + authName + ", " + grant
								+ ", " + regiMemberIdx + ", " + regiMemberId + ", " + regiMemberName + ", " + regiDate + ", " + regiIp);
					}
				}
			}
		}
	}
	
	public Map<String, Object> setUsertypeParam(String usertypeIdx){
		if(StringUtil.isEmpty(usertypeIdx)) return null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.USERTYPE_IDX", usertypeIdx, ">="));
		param.put("searchList", searchList);
		
		return param;
	}

	public Map<String, Object> setGroupParam(String groupIdxs){
		if(StringUtil.isEmpty(groupIdxs)) return null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("MBR.USERTYPE_IDX", RbsProperties.getPropertyInt("Globals.code.USERTYPE_ADMIN"), ">="));
		searchList.add(new DTForm("OPT.GROUP_CODE", groupIdxs.split(",")));
		param.put("searchList", searchList);
		
		return param;
	}

	public Map<String, Object> setDepartParam(String departIdxs){
		if(StringUtil.isEmpty(departIdxs)) return null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		
		searchList.add(new DTForm("DEPART_IDX", departIdxs.split(",")));
		param.put("searchList", searchList);
		
		return param;
	}

	public Map<String, Object> setMemberParam(String memberIdxs){
		if(StringUtil.isEmpty(memberIdxs)) return null;
		
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		searchList.add(new DTForm("A.MEMBER_IDX", memberIdxs.split(",")));
		param.put("searchList", searchList);
		
		return param;
	}
	
	@Override
	public Map<String, Object> setMenuMbrMap() throws Exception{
		List<?> webSiteList = websiteDAO.getList(null);

		int webSiteLen = 0;
		Map<String, Object> webSiteMenuMbrMap = null;
		if(webSiteList != null){
			webSiteLen = webSiteList.size();
			webSiteMenuMbrMap = new HashMap<String, Object>();
		}
		
		for(int i = 0; i < webSiteLen; i++){
			DataMap webSiteInfo = (DataMap)webSiteList.get(i);
			String siteId = StringUtil.getString(webSiteInfo.get("SITE_ID"));
			
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			
			searchList.add(new DTForm("A.SITE_ID", siteId));
	    	param.put("searchList", searchList);
	    	
	    	List<?> webSiteVerList = websiteService.getVersionList(param);
	    
	    	int webSiteVerLen = 0;
			Map<String, Object> webSiteVerMenuMbrMap = null;
	    	if(webSiteVerList != null){
	    		webSiteVerLen = webSiteVerList.size();
	    		webSiteVerMenuMbrMap = new HashMap<String, Object>();
	    	}
	    	
	    	for(int j = 0; j < webSiteVerLen; j++) {
				DataMap webSiteVerInfo = (DataMap)webSiteVerList.get(j);
				int verIdx = StringUtil.getInt(webSiteVerInfo.get("OPTION_CODE"));
				
				List<DTForm> verSearchList = new ArrayList<DTForm>();
				verSearchList.addAll(searchList);
				verSearchList.add(new DTForm("A.VER_IDX", verIdx));
				//verSearchList.add(new DTForm("A.ISSERVICE", "1"));
		    	param.put("searchList", verSearchList);
		    	param.put("isApply", true);
		    	
		    	JSONObject menuJSONObject = websiteService.getMenuJSONObject(param);

			    JSONArray menuArray = JSONObjectUtil.getJSONArray(menuJSONObject, "menu-list");
				JSONObject menus = JSONObjectUtil.getJSONObject(menuJSONObject, "menus");
				
				Map<String, Object> menuMbrMaps = new HashMap<String, Object>();
				menuMbrMaps = setMenuManageAuthLog(menuMbrMaps, null, menus, menuArray);
				webSiteVerMenuMbrMap.put(StringUtil.toString(verIdx), menuMbrMaps);
			}

	    	webSiteMenuMbrMap.put(siteId, webSiteVerMenuMbrMap);
		}
		
		return webSiteMenuMbrMap;
	}
	
	@Override
	public void setMenuManageAuthLog(Map<String, Object> webSiteMenuMbrMap, Map<String, Object> applyWebSiteMenuMbrMap) throws Exception{
		if(webSiteMenuMbrMap == null || applyWebSiteMenuMbrMap == null) return;
		
		for(String siteId : webSiteMenuMbrMap.keySet()){
			Map<String, Object> webSiteVerMenuMap = (Map<String, Object>)webSiteMenuMbrMap.get(siteId);
			Map<String, Object> applyWebSiteVerMenuMap = (Map<String, Object>)applyWebSiteMenuMbrMap.get(siteId);

			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.SITE_ID", siteId));
			param.put("searchList", searchList);
			
			DataMap siteInfo = websiteDAO.getModify(param);
			String siteName = StringUtil.getString(siteInfo.get("SITE_NAME"));
			
			for(String verIdxStr : webSiteVerMenuMap.keySet()){
				int verIdx = StringUtil.getInt(verIdxStr);
				/*
				Map<String, Object> param = new HashMap<String, Object>();
				List<DTForm> searchList = new ArrayList<DTForm>();
				searchList.add(new DTForm("A.SITE_ID", siteId));
				searchList.add(new DTForm("A.VER_IDX", verIdx));
				param.put("searchList", searchList);
		    	param.put("isApply", true);
		    	JSONObject menuJSONObject = websiteService.getMenuJSONObject(param);

			    JSONArray menuArray = JSONObjectUtil.getJSONArray(menuJSONObject, "menu-list");
				JSONObject menus = JSONObjectUtil.getJSONObject(menuJSONObject, "menus");
				*/
				Map<String, Object> menuMbrMaps = (Map<String, Object>)webSiteVerMenuMap.get(verIdxStr);
				Map<String, Object> applyMenuMbrMaps = (Map<String, Object>)applyWebSiteVerMenuMap.get(verIdxStr);
				if(menuMbrMaps == null) continue;
				for(String menuIdxStr : menuMbrMaps.keySet()){
					Map<String, Object> menuMbrMap = (Map<String, Object>)menuMbrMaps.get(menuIdxStr);
					Map<String, Object> applyMenuMbrMap = (Map<String, Object>)applyMenuMbrMaps.get(menuIdxStr);
					int menuIdx = StringUtil.getInt(menuIdxStr);
					String totalMenuName = StringUtil.getString(menuMbrMap.get("totalMenuName"));
					
					setMenuManageAuthLog(siteId, siteName, verIdx, menuIdx, totalMenuName, (Map<String, Object>)menuMbrMap.get("usertypeMbrMap"), (Map<String, Object>)applyMenuMbrMap.get("usertypeMbrMap"), rbsMessageSource.getMessage("message.auth.usertype"), "USERTYPE_NAME");
					setMenuManageAuthLog(siteId, siteName, verIdx, menuIdx, totalMenuName, (Map<String, Object>)menuMbrMap.get("groupMbrMap"), (Map<String, Object>)applyMenuMbrMap.get("groupMbrMap"), rbsMessageSource.getMessage("message.auth.group"), "GROUP_NAME");
					setMenuManageAuthLog(siteId, siteName, verIdx, menuIdx, totalMenuName, (Map<String, Object>)menuMbrMap.get("departMbrMap"), (Map<String, Object>)applyMenuMbrMap.get("departMbrMap"), rbsMessageSource.getMessage("message.auth.depart"), "DEPART_NAME");
					setMenuManageAuthLog(siteId, siteName, verIdx, menuIdx, totalMenuName, (Map<String, Object>)menuMbrMap.get("mbrMap"), (Map<String, Object>)applyMenuMbrMap.get("mbrMap"), rbsMessageSource.getMessage("message.auth.member"), null);
				}
				
			}	
		}
	}
	
	public Map<String, Object> setMenuManageAuthLog(Map<String, Object> menuMbrMaps, String menuName, JSONObject menus, JSONArray menuArray) throws Exception {
		if(JSONObjectUtil.isEmpty(menuArray)) return null;
		if(menuMbrMaps == null) menuMbrMaps = new HashMap<String, Object>();
		for (int i = 0; i < menuArray.size(); i++) {
			int menuIdx = JSONObjectUtil.getInt(JSONObjectUtil.getJSONObject(menuArray, i), "menu_idx");
			
			String menuKey = "menu" + menuIdx;
			
			JSONObject menu = JSONObjectUtil.getJSONObject(menus, menuKey);

			StringBuffer totalMenuName = new StringBuffer();
			
			if(!StringUtil.isEmpty(menuName)) totalMenuName.append(menuName + " > ");
			totalMenuName.append(JSONObjectUtil.getString(menu, "menu_name"));
			
			String totalMenuSbStr = totalMenuName.toString();
			
			Map<String, Object> menuMbrMap = setMenuMbrMap(menu, "manager_");
			menuMbrMap.put("totalMenuName", totalMenuSbStr);
			menuMbrMaps.put(StringUtil.toString(menuIdx), menuMbrMap);
			
			JSONArray childMenuArray = JSONObjectUtil.getJSONArray(JSONObjectUtil.getJSONObject(menuArray, i), "menu-list");
			if(!JSONObjectUtil.isEmpty(childMenuArray)) menuMbrMaps.putAll(setMenuManageAuthLog(menuMbrMaps, totalMenuSbStr, menus, childMenuArray));
		}
		
		return menuMbrMaps;
	}
}