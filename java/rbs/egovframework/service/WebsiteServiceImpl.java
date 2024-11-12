package rbs.egovframework.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.JSONMap;
import com.woowonsoft.egovframework.service.WebsiteService;
import com.woowonsoft.egovframework.util.MenuUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import rbs.egovframework.WebsiteVO;
import rbs.egovframework.mapper.WebsiteMapper;

@Service("selSiteService")
public class WebsiteServiceImpl implements WebsiteService {

	@Resource(name="rbsWebsiteMapper")
	private WebsiteMapper websiteDAO;
	
	@Override
	public List<Object> getWebsiteList(Map<String, Object> param) {
		return websiteDAO.getWebsiteList(param);
	}
	
	@Override
	public WebsiteVO getDefSiteInfo(Map<String, Object> param) {
		return websiteDAO.getDefSiteInfo(param);
	}
	
	@Override
	public DataMap getSiteVerInfo(Map<String, Object> param) {
		return websiteDAO.getSiteVerInfo(param);
	}
	
	@Override
	public List<Object> getVersionList(Map<String, Object> param) {
		return websiteDAO.getVersionList(param);
	}
	@Override
	public JSONObject getMenuJSONObject(Map<String, Object> param) {
		boolean isApply = StringUtil.getBoolean(param.get("isApply"));
		List<Object> menuList = null;
		if(isApply) menuList = websiteDAO.getApplyMenuList(param); //websiteDAO.getApplyLogMenuList(param);
		else menuList = websiteDAO.getMenuList(param);
		if(menuList == null) return null;
		Map<String, List<Object>> multiMap = null;
		if(isApply) multiMap = websiteDAO.getApplyMenuMultiMapList(param); //websiteDAO.getApplyLogMenuMultiMapList(param);
		else multiMap = websiteDAO.getMenuMultiMapList(param);
		
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
		int menusSize = menus.size();
		int menuListSize = 0;
		if(menuListArray != null && !menuListArray.isEmpty()) menuListSize = menuListArray.size();
		if(menusSize > 0 || menuListSize > 0) {
			urMenuTotalObject = new JSONObject(); 
	        if(menuSize > 0) urMenuTotalObject.put("menus", menus);
	        if(menuListSize > 0) urMenuTotalObject.put("menu-list", menuListArray);
		}
		return urMenuTotalObject;
	}

}