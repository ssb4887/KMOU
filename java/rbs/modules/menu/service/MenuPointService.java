package rbs.modules.menu.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

public interface MenuPointService {
	
	public DataMap getTotalView(Map<String, Object> param);
	public Map<Object, Object> getMenuPointMap(Map<String, Object> param);
	
	public int getPointPTotalCount(Map<String, Object> param);
	public List<Object> getPointPList(String localeLang, Map<String, Object> param);
	
	public int getPointMTotalCount(Map<String, Object> param);
	public List<Object> getPointMList(Map<String, Object> param);
	
	public int insert(String siteId, int verIdx, int menuIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
}