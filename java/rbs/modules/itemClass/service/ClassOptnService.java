package rbs.modules.itemClass.service;

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
public interface ClassOptnService {
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getOptnList(Map<String, Object> param);

	/**
	 * 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getTotalCount(String lang, Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getList(String lang, Map<String, Object> param);

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
	public int insert(String mstCd, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;
	
	/**
	 * 수정
	 * @param param
	 * @return
	 */
	public int update(String lang, String mstCd, String classIdx, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;

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
	public int delete(String mstCd, String[] deleteIdxs, String regiIp) throws Exception;
	
	/**
	 * 복원
	 * @param param
	 * @return
	 */
	public int restore(String mstCd, String[] restoreIdxs, String regiIp) throws Exception;

	/**
	 * 완전삭제
	 * @param param
	 * @return
	 */
	public int cdelete(String mstCd, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception;

}