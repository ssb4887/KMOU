package rbs.modules.poll.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;


/**
 * 설문관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface QuesService {

	/**
	 * 설문정보
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getPollInfo(int fnIdx, Map<String, Object> param);
	
	public int getRespCnt(int fnIdx, Map<String, Object> param);

	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getTotalCount(int fnIdx, Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public List<Object> getList(int fnIdx, Map<String, Object> param);
	
	public List<Object> getRespCntList(int fnIdx, Map<String, Object> param);

	/**
	 * 파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getFileView(int fnIdx, Map<String, Object> param);

	public List<Object> getQuesList(int fnIdx, Map<String, Object> param);
	public List<Object> getInclassList(int fnIdx, Map<String, Object> param);
	public Map<String, List<Object>> getInquesMap(int fnIdx, Map<String, Object> param);
	
	public List<Object> getItemList(int fnIdx, Map<String, Object> param);
	public List<Object> getItemJSONList(int fnIdx, Map<String, Object> param);
	
	public DataMap getItemContView(int fnIdx, Map<String, Object> param);
	
	/**
	 * 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getView(int fnIdx, Map<String, Object> param);

	/**
	 * 수정 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getModify(int fnIdx, Map<String, Object> param);
	
	/**
	 * 
	 * @param isPollIngChk
	 * @param fnIdx
	 * @param pollIdx
	 * @return
	 */
	public int pollMng(boolean isPollIngChk, int fnIdx, int pollIdx);
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int insert(String uploadModulePath, int fnIdx, int pollIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	
	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param brdIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int update(String uploadModulePath, int fnIdx, int pollIdx, int quesIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;

	/**
	 * 삭제 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getDeleteCount(int fnIdx, Map<String, Object> param);
	
	/**
	 * 삭제 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public List<Object> getDeleteList(int fnIdx, Map<String, Object> param);
	
	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int delete(int fnIdx, int pollIdx, String[] deleteIdxs, String regiIp) throws Exception;
	
	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param fnIdx
	 * @param restoreIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int restore(int fnIdx, int pollIdx, String[] restoreIdxs, String regiIp) throws Exception;

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int cdelete(String uploadModulePath, int fnIdx, int pollIdx, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception;
}