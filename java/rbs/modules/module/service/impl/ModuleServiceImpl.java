package rbs.modules.module.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import rbs.modules.module.mapper.ModuleMapper;
import rbs.modules.module.service.ModuleService;

@Service("moduleService")
public class ModuleServiceImpl extends EgovAbstractServiceImpl implements
		ModuleService {

	@Resource(name="moduleMapper")
	private ModuleMapper moduleDAO;
	
	@Override
	public List<Object> getManageList(Map<String, Object> param) {
		return moduleDAO.getManageList(param);
	}
	
	@Override
	public List<Object> getAuthManageList(Map<String, Object> param) {
		return moduleDAO.getAuthManageList(param);
	}

	@Override
	public DataMap getManageView(Map<String, Object> param) {
		return moduleDAO.getManageView(param);
	}

	@Override
	public int getTotalCount(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Object> getList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataMap getView(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataMap getModify(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(String uploadModulePath, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String uploadModulePath, int brdIdx, String regiIp,
			ParamForm parameterMap, JSONObject items, JSONArray itemOrder)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDeleteCount(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Object> getDeleteList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(String[] deleteIdxs, String regiIp)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int restore(String[] restoreIdxs, String regiIp)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int cdelete(String moduleId, String[] deleteIdxs,
			JSONObject items, JSONArray itemOrder) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
