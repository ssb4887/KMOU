package rbs.modules.dashboard.service;

import java.util.List;

import net.sf.json.JSONObject;

public interface ModuleListDashboardService {

	public List<Object> getModuleList(String moduleId, int fnIdx, JSONObject dashboardLObject, String itemMasterCode);
	
	public List<Object> getModuleList(String moduleId, int fnIdx, int limitNumber, String itemMasterCode);
}