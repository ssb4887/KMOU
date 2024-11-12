package rbs.modules.dashboard.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.util.JSONObjectUtil;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.modules.dashboard.mapper.DashboardMapper;
import rbs.modules.dashboard.service.ModuleListDashboardService;

@Service("moduleListDashboardService")
public class ModuleListDashboardServiceImpl extends EgovAbstractServiceImpl implements ModuleListDashboardService {

	@Resource(name="dashboardMapper")
	private DashboardMapper dashboardDao;

	@Override
	public List<Object> getModuleList(String moduleId, int fnIdx, JSONObject dashboardLObject, String itemMasterCode){
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();
		int limitNumber = JSONObjectUtil.getInt(dashboardLObject, "limit_number");

		param.put("itemMasterCode", itemMasterCode);
		param.put("searchList", searchList);
		param.put("firstIndex", 0);
		param.put("lastIndex", limitNumber);
		param.put("recordCountPerPage", limitNumber);
    	param.put("fnIdx", fnIdx);
		return dashboardDao.getModuleList(moduleId, param);
	}

	@Override
	public List<Object> getModuleList(String moduleId, int fnIdx, int limitNumber, String itemMasterCode){
		Map<String, Object> param = new HashMap<String, Object>();
		List<DTForm> searchList = new ArrayList<DTForm>();

		param.put("itemMasterCode", itemMasterCode);
		param.put("searchList", searchList);
		param.put("firstIndex", 0);
		param.put("lastIndex", limitNumber);
		param.put("recordCountPerPage", limitNumber);
    	param.put("fnIdx", fnIdx);
		return dashboardDao.getModuleList(moduleId, param);
	}
}