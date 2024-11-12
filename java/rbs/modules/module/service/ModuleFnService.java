package rbs.modules.module.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;


/**
 * 기관정보관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface ModuleFnService {

	public List<Object> getOptnList(Map<String, Object> param);
	

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
	
	public void createModuleTable(String moduleId, int fnIdx, String fnName, String designType);
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장   <br/>
	 * 기능 정보 <br/>
	 * 설정 정보  <br/>
	 * 항목 정보  <br/>
	 * 검색 항목 정보  <br/>
	 * @param uploadModulePath
	 * @param moduleId
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int insert(String uploadModulePath, String moduleId, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;
	
	public int insert(boolean isCreateTable, String uploadModulePath, String moduleId, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;
	
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
	public int update(String uploadModulePath, String moduleId, int brdIdx, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;
	
	/**
	 * 적용처리
	 * @param moduleId
	 * @param fnIdx
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int apply(String moduleId, int fnIdx, String regiIp) throws Exception;
	
	/**
	 * 항목 코드정보 목록
	 * @param param
	 * @return
	 */
	public List<Object> getItemOptionList(Map<String, Object> param);
	
	/**
	 * 설정정보 상세조회
	 * @param param
	 * @return
	 */
	public DataMap getSettingModify(Map<String, Object> param);

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
	public int settingUpdate(String uploadModulePath, String moduleId, int brdIdx, String regiIp, ParamForm parameterMap, JSONObject items, JSONArray itemOrder) throws Exception;

	
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
	 * @param deleteIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int delete(String moduleId, String[] deleteIdxs, String regiIp) throws Exception;
	
	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param moduleId
	 * @param restoreIdxs
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int restore(String moduleId, String[] restoreIdxs, String regiIp) throws Exception;

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param uploadModulePath
	 * @param moduleId
	 * @param deleteIdxs
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int cdelete(String moduleId, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception;
}