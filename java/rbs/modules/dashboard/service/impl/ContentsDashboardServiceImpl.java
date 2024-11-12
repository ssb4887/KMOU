package rbs.modules.dashboard.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.util.JSONObjectUtil;
import com.woowonsoft.egovframework.util.StringUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.modules.dashboard.mapper.DashboardMapper;
import rbs.modules.dashboard.service.ModuleDashboardService;

@Service("contentsDashboardService")
public class ContentsDashboardServiceImpl extends EgovAbstractServiceImpl implements ModuleDashboardService {

	@Resource(name="dashboardMapper")
	private DashboardMapper dashboardDao;


	@Override
	public HashMap<String, Object> getModuleHashMap(String lang, String moduleId, JSONObject siteMenus, JSONObject dashboardObject, JSONObject moduleTotalObject){

		HashMap<String, Object> resultHashMap = new HashMap<String, Object>();
		JSONArray objNames = dashboardObject.names();
		JSONObject dashboardLObject = null;
		int dashboardMenuIdx = 0;
		JSONObject menuObj = null;
		String menuModuleId = null;
		String contentsCode = null;
		int branchIdx = 0;
		for(Object obj:objNames) {
			String objName = (String)obj;
			
			dashboardLObject = JSONObjectUtil.getJSONObject(dashboardObject, objName);
			if(JSONObjectUtil.isEmpty(dashboardLObject)) continue;
			
			dashboardMenuIdx = JSONObjectUtil.getInt(dashboardLObject, "menu_idx");
			menuObj = JSONObjectUtil.getJSONObject(siteMenus, "menu" + dashboardMenuIdx);
			menuModuleId = JSONObjectUtil.getString(menuObj, "module_id");
			
			// 메뉴의 모듈과 일치하지 않는 경우 삭제
			if(!StringUtil.isEquals(moduleId, menuModuleId)) continue;

			// 메뉴명 setting
			dashboardLObject.put("menu_name", JSONObjectUtil.getString(menuObj, "menu_name"));
			
			contentsCode = JSONObjectUtil.getString(menuObj, "contents_code");
			branchIdx = JSONObjectUtil.getInt(menuObj, "branch_idx");
			
			Map<String, Object> param = new HashMap<String, Object>();
			List<DTForm> searchList = new ArrayList<DTForm>();
			searchList.add(new DTForm("A.CONTENTS_CODE", contentsCode));
			searchList.add(new DTForm("A.BRANCH_IDX", branchIdx));

			param.put("searchList", searchList);
			resultHashMap.put(objName, dashboardDao.getContentsView(lang, param));
		}
		
		return resultHashMap;
	}
}