package rbs.modules.poll.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;


/**
 * 설문관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface RespService {

	public int getRespIdx(int fnIdx, Map<String, Object> param);
	public int getTmpRespIdx(int fnIdx, Map<String, Object> param);
	
	public int getRespCnt(int fnIdx, Map<String, Object> param);
	
	public DataMap getPollView(int fnIdx, Map<String, Object> param);

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

	public List<DTForm> getRespSearchList(int pollIdx, String alias);
	
	/**
	 * 
	 * @param pollIdx
	 * @return
	 */
	public List<DTForm> getRespSearchList(int pollIdx);

	/**
	 * 참여 결과 Map
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public Map<Object, Object> getResultMap(int fnIdx, Map<String, Object> param);
	public Map<Object, Object> getTmpResultMap(int fnIdx, Map<String, Object> param);
	
	/**
	 * 참여 결과 목록 Map
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public Map<String, List<Object>> getResultListMap(int fnIdx, Map<String, Object> param);
	
	public int getResultTotalCount(int fnIdx, Map<String, Object> param);
	public List<Object> getResultList(int fnIdx, Map<String, Object> param);

	/**
	 * 유형 결과
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public List<Object> getQuestypeResultList(int fnIdx, Map<String, Object> param);
	public List<Object> getQuestypePResultList(int fnIdx, Map<String, Object> param);
	
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
	public int insert(int fnIdx, int pollIdx, String regiIp, ParamForm parameterMap) throws Exception;
	
	public int resultUpdate(int fnIdx, int pollIdx, int respIdx, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;
	
}