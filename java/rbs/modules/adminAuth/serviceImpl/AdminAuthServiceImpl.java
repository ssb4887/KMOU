package rbs.modules.adminAuth.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.ParamForm;
import com.woowonsoft.egovframework.prop.RbsProperties;
import com.woowonsoft.egovframework.resource.RbsMessageSource;
import com.woowonsoft.egovframework.util.DateUtil;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.ModuleUtil;
import com.woowonsoft.egovframework.util.StringUtil;
import com.woowonsoft.egovframework.util.UserDetailsHelper;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.egovframework.LoginVO;
import rbs.modules.adminAuth.mapper.AdminAuthMapper;
import rbs.modules.adminAuth.mapper.AdminAuthMultiMapper;
import rbs.modules.adminAuth.service.AdminAuthService;
import rbs.modules.menu.service.MenuVerService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
@Service("adminAuthService")
public class AdminAuthServiceImpl extends EgovAbstractServiceImpl implements AdminAuthService {

	@Resource(name="adminAuthMapper")
	private AdminAuthMapper adminAuthDAO;
	
	@Resource(name="adminAuthMultiMapper")
	private AdminAuthMultiMapper adminAuthMultiDAO;

	@Resource(name="menuVerService")
	protected MenuVerService menuVerService;
	
	@Resource(name = "rbsMessageSource")
	RbsMessageSource rbsMessageSource;

	/**
	 * 선택 메뉴정보
	 * @return
	 */
    @Override
	public JSONObject getMenuInfo(int mnuCd) {
    	JSONObject admSiteObjects = getMenusObject();					// 사이트, 메뉴정보 전체
	    if(JSONObjectUtil.isEmpty(admSiteObjects)) return null;
    	// 메뉴설정파일 있는 경우
	    JSONObject admSiteMenus = JSONObjectUtil.getJSONObject(admSiteObjects, "menus");
	    return MenuUtil.getMenuInfo(mnuCd, admSiteMenus);
    }

    @Override
    public int update(String siteMode, int menuIdx, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception {
		if(JSONObjectUtil.isEmpty(items) || JSONObjectUtil.isEmpty(itemOrder)) return -1;

		JSONObject menuObject = null;
		JSONObject siteObjects = getMenusObject();					// 사이트, 메뉴정보 전체
		if(JSONObjectUtil.isEmpty(siteObjects)) return -1;
    	// 메뉴설정파일 있는 경우
	    JSONObject admSiteMenus = JSONObjectUtil.getJSONObject(siteObjects, "menus");
	    menuObject = MenuUtil.getMenuInfo(menuIdx, admSiteMenus);
	    
	    int result = 0;
		LoginVO loginVO = (LoginVO) UserDetailsHelper.getAuthenticatedUser();
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		List<DTForm> dataList = new ArrayList<DTForm>();
		searchList.add(new DTForm("MENU_IDX", menuIdx));
		
		// 항목설정으로 저장항목 setting
		HashMap<String, Object> dataMap = ModuleUtil.getItemInfoDataMap(parameterMap, null, items, itemOrder);
		if(dataMap == null || dataMap.size() == 0) return -1;
		
		List itemDataList = StringUtil.getList(dataMap.get("dataList"));
		if(itemDataList != null) dataList.addAll(itemDataList);
		
		// 저장 항목
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
    	
		// 등록여부 
    	param.put("searchList", searchList);
		int duplicate = adminAuthDAO.getTotalCount(param);
		
		if(duplicate <= 0) {
			// 등록
	    	dataList.add(new DTForm("MENU_IDX", menuIdx));
	    	dataList.add(new DTForm("MENU_NAME", menuObject.get("menu_name")));
	    	dataList.add(new DTForm("MENU_LINK", menuObject.get("menu_link")));
	    	dataList.add(new DTForm("MODULE_ID", menuObject.get("module_id")));
	    	dataList.add(new DTForm("FN_IDX", menuObject.get("fn_idx")));
	    	dataList.add(new DTForm("CONF_MODULE", menuObject.get("conf_module")));
	    	dataList.add(new DTForm("MODULE_AUTH", menuObject.get("module_auth")));
	    	dataList.add(new DTForm("INCLUDE_TOP", menuObject.get("include_top")));
	    	dataList.add(new DTForm("INCLUDE_BOTTOM", menuObject.get("include_bottom")));
	    	dataList.add(new DTForm("SOURCE_TOP", menuObject.get("source_top")));
	    	dataList.add(new DTForm("SOURCE_BOTTOM", menuObject.get("source_bottom")));
	    	
	    	dataList.add(new DTForm("REGI_IDX", loginMemberIdx));
	    	dataList.add(new DTForm("REGI_ID", loginMemberId));
	    	dataList.add(new DTForm("REGI_NAME", loginMemberName));
	    	dataList.add(new DTForm("REGI_IP", regiIp));
	    	param.put("dataList", dataList);
	    	
			result = adminAuthDAO.insert(param);
		} else {
			// 수정
	    	param.put("dataList", dataList);
	    	param.put("searchList", searchList);
	    	
			result = adminAuthDAO.update(param);	
		}
		
		if(result > 0) {
			int result1 = 0;
			// 5. multi data 저장
			Map<String, Object> multiDelParam = new HashMap<String, Object>();
			multiDelParam.put("MENU_IDX", menuIdx);
			result1 = adminAuthMultiDAO.cdelete(multiDelParam);
			
			List multiDataList = StringUtil.getList(dataMap.get("multiDataList"));
			if(multiDataList != null) {
				int multiDataSize = multiDataList.size();
				for(int i = 0 ; i < multiDataSize ; i ++) {
					Map<String, Object> multiParam = (HashMap)multiDataList.get(i);
			    	multiParam.put("MENU_IDX", menuIdx);
					result = adminAuthMultiDAO.insert(multiParam);
				}
			}

			JSONObject applyMenu = new JSONObject();
			if(!JSONObjectUtil.isEmpty(menuObject)) applyMenu.putAll(menuObject);
			
			JSONObject menu = getAuthDataObjects(parameterMap, menuObject);

			menuVerService.setMenuManageAuthLog(JSONObjectUtil.getString(menuObject, "menu_name"), menu, applyMenu, "");
			
			// 메뉴 파일 생성
			boolean isResult = MenuUtil.writeSiteObject(siteMode, siteObjects);
			if(!isResult) throw processException("message.no.insert");
		}
				
		return result;
	}
	
    /**
     * 메뉴권한 저장
     * @param parameterMap
     * @param authObject
     * @param dataList
     * @param multiDataList
     * @return
     */
	private JSONObject/*HashMap<String, Object>*/ getAuthDataObjects(ParamForm parameterMap, JSONObject menuObject/*, List<DTForm> dataList, List<HashMap<String, Object>> multiDataList*/) {
		//HashMap<String, Object> authDataObjects = new HashMap<String, Object>();
    	String authItemId = null;
    	String utpItemIdName = null;
    	String utpColumnIdName = null;
    	int utpVal = 0;
    	String multiItemId = null;
    	String[] multiItemIdNames = {"Grp", "Dpt", "Mbr"};
    	String[] multiColulmnNames = {"group_idxs", "depart_idxs", "member_idxs"};
    	Object multiObjVals = null;
    	String[] multiVals = null;
    	
		authItemId = "menu";
		
		utpItemIdName = authItemId + "Utp";
		utpColumnIdName = "usertype_idx";
		
    	// 사용자유형
		utpVal = StringUtil.getInt(parameterMap.get(utpItemIdName));
		//dataList.add(new DTForm(utpColumnIdName, StringUtil.getInt(parameterMap.get(utpItemIdName))));
		
		menuObject.put(utpColumnIdName, utpVal);

		// multi 사용자권한 : 그룹,부서,회원
		int mIdx = 0;
		for(String multiItemIdName:multiItemIdNames) {
			multiItemId = authItemId + multiItemIdName;
			multiObjVals = parameterMap.get(multiItemId);
			if(multiObjVals != null) {
				// 값 있는 경우
				if(multiObjVals instanceof String[]) multiVals = (String[])multiObjVals;
				else if(multiObjVals instanceof String){
					multiVals = new String[1];
					multiVals[0] = (String)multiObjVals;
				}
				/*for(String multiVal:multiVals) {
	    			List<DTForm> multiDataList2 = new ArrayList<DTForm>();
					multiDataList2.add(new DTForm("ITEM_ID", multiItemId));								// item_id
					multiDataList2.add(new DTForm("ITEM_KEY", multiVal));								// item_key
	
					HashMap<String, Object> dataMap2 = new HashMap<String, Object>();
					dataMap2.put("dataList", multiDataList2);
					
					if(multiDataList == null) multiDataList = new ArrayList<HashMap<String,Object>>();
					multiDataList.add(dataMap2);
				}*/
				
				menuObject.put(multiColulmnNames[mIdx], StringUtil.getString(multiVals, ","));
			} else menuObject.remove(multiColulmnNames[mIdx]);
			
			mIdx ++;
		}

		/*authDataObjects.put("authObject", authObject);
		authDataObjects.put("dataList", dataList);
		authDataObjects.put("multiDataList", multiDataList);*/
		
		return menuObject;
	}

	/**
	 * 메뉴설정정보
	 * @return
	 */
	public JSONObject getMenusObject() {
		String siteORPath = "/" + RbsProperties.getProperty("Globals.site.mode.adm");
	    return MenuUtil.getSiteObject(siteORPath, false);					// 사이트, 메뉴정보 전체
	}

}