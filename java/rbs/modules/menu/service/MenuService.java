package rbs.modules.menu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import com.woowonsoft.egovframework.form.EgovMap;


public interface MenuService {
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getOptnList(Map<String, Object> param);
	

	public List<Object> getModuleFnJSONList(Map<String, Object> param);
	public List<Object> getContentsJSONList(String lang, Map<String, Object> param);
	public List<Object> getBranchSONList(String lang, Map<String, Object> param);
	public List<Object> getModuleAuthJSONList(Map<String, Object> param);
	
	public int getMaxMenuLevel(Map<String, Object> param);
	public List<Object> getExcelList(Map<String, Object> param);
	public Map<String, List<Object>> getGroupMapList(Map<String, Object> param);
	public Map<String, List<Object>> getDepartMapList(Map<String, Object> param);
	public Map<String, List<Object>> getMemberMapList(Map<String, Object> param);

	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getTotalCount(Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getList(Map<String, Object> param);

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getModify(Map<String, Object> param);

	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	public EgovMap getJSONModify(Map<String, Object> param);
	
	public JSONObject getMenuApplyJSONObject(Map<String, Object> param);
	public EgovMap getJSONApplyView(Map<String, Object> param);
	public HashMap<String, Object> getJSONApplyMultiHashMap(int menuIdx, int verIdx, String siteId, JSONObject items, JSONArray itemOrder);
		
	
	/**
	 * 등록
	 * @param param
	 * @return
	 */
	public int insert(String siteId, int verIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	
	/**
	 * 수정
	 * @param param
	 * @return
	 */
	public int update(String siteId, int verIdx, int menuIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;

	public int mngUpdate(String siteId, int verIdx, int[] mngMenuIdxs, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getDeleteCount(Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getDeleteList(Map<String, Object> param);
	
	/**
	 * 삭제
	 * @param param
	 * @return
	 */
	public int delete(String siteId, int verIdx, int menuIdx, String regiIp) throws Exception;
	
	/**
	 * 복원
	 * @param param
	 * @return
	 */
	public int restore(String siteId, int verIdx, int[] restoreIdxs, String regiIp) throws Exception;

	/**
	 * 완전삭제
	 * @param param
	 * @return
	 */
	public int cdelete(String siteId, int verIdx, int[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception;
	
	public HashMap<String, Object> getJSONMultiHashMap(int menuIdx, int verIdx, String siteId, JSONObject items, JSONArray itemOrder);

}