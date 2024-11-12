package rbs.modules.contents.service;

import java.util.List;
import java.util.Map;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface ContVersionService {
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getCopyVerList(String lang, Map<String, Object> param);

	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	//public int getTotalCount(Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getList(String lang, Map<String, Object> param);
	
	/**
	 * 마스터코드별 목록
	 * @param param
	 * @return
	 */
	//public HashMap<String, Object> getHashMapList(JSONObject items, JSONArray itemOrder);
	
	/**
	 * 마스터코드별 목록
	 * @param param
	 * @return
	 */
	//public HashMap<String, Object> getHashMapList(HashMap<String, Object> dt, JSONObject items, JSONArray itemOrder);


	/**
	 * 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getModify(String lang, Map<String, Object> param);
	
	/**
	 * 등록
	 * @param param
	 * @return
	 */
	public int insert(String uploadModulePath, String lang, String contCd, int branchIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, JSONObject moduleItem) throws Exception;
	
	/**
	 * 수정
	 * @param param
	 * @return
	 */
	public int update(String lang, String contCd, int branchIdx, int versionIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;

	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getDeleteCount(String lang, Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getDeleteList(String lang, Map<String, Object> param);
	
	/**
	 * 삭제
	 * @param param
	 * @return
	 */
	public int delete(String lang, String contCd, int branchIdx, int[] deleteIdxs, String regiIp) throws Exception;
	
	/**
	 * 복원
	 * @param param
	 * @return
	 */
	public int restore(String lang, String contCd, int branchIdx, int[] restoreIdxs, String regiIp) throws Exception;

	/**
	 * 완전삭제
	 * @param param
	 * @return
	 */
	public int cdelete(String uploadModulePath, String lang, String contCd, int branchIdx, int[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception;

}