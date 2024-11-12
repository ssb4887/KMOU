package rbs.modules.module.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;


/**
 * 항목관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface ModuleFnItemSService {
	/**
	 * 입력항목 검색에 사용할 기본항목정보
	 * @param param
	 * @return
	 */
	public List<Object> getDefaultSearchIList(Map<String, Object> param);
	
	/**
	 * 선택항목 검색에 사용할 기본항목정보
	 * @param param
	 * @return
	 */
	public List<Object> getDefaultSearchSList(Map<String, Object> param);

	/**
	 * 입력항목 검색에 사용할 기본항목정보
	 * @param param
	 * @return
	 */
	public List<Object> getSearchIList(Map<String, Object> param);
	
	/**
	 * 선택항목 검색에 사용할 기본항목정보
	 * @param param
	 * @return
	 */
	public List<Object> getSearchSList(Map<String, Object> param);

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
	public DataMap getView(Map<String, Object> param);

	/**
	 * 수정 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getModify(Map<String, Object> param);
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장   <br/>
	 * 기능 정보 <br/>
	 * 설정 정보  <br/>
	 * 항목 정보  <br/>
	 * 검색 항목 정보  <br/>
	 * @param moduleId
	 * @param regiIp
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public int insert(String moduleId, int fnIdx, String regiIp, ParamForm parameterMap) throws Exception;
	
	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param moduleId
	 * @param brdIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int update(String uploadModulePath, String moduleId, int fnIdx, String itemId, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;

	
	public int update(String uploadModulePath, String moduleId, int fnIdx, String itemId, String regiIp,
			ParamForm parameterMap, String ordItemId)
			throws Exception;
	
	/**
	 * 항목 코드정보 목록
	 * @param param
	 * @return
	 */
	public List<Object> getItemOptionList(Map<String, Object> param);
	
	/**
	 * 삭제 전체 목록 수
	 * @param param
	 * @return
	 */
	public int getDeleteCount(Map<String, Object> param);
	
	/**
	 * 삭제 전체 목록
	 * @param param
	 * @return
	 */
	public List<Object> getDeleteList(Map<String, Object> param);
	
	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param moduleId
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int delete(String moduleId, int fnIdx, String[] deleteIdxs, String regiIp) throws Exception;
	
	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param moduleId
	 * @param fnIdx
	 * @param restoreIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int restore(String moduleId, int fnIdx, String[] restoreIdxs, String regiIp) throws Exception;

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param uploadModulePath
	 * @param moduleId
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int cdelete(String uploadModulePath, String moduleId, int fnIdx, String[] deleteIdxs, String regiIp, JSONObject items, JSONArray itemOrder) throws Exception;
}