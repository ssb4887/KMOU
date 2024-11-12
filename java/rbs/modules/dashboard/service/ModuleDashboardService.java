package rbs.modules.dashboard.service;

import java.util.HashMap;

import net.sf.json.JSONObject;

public interface ModuleDashboardService {

	public HashMap<String, Object> getModuleHashMap(String lang, String moduleId, JSONObject siteMenus, JSONObject dashboardObject, JSONObject moduleTotalObject);
}