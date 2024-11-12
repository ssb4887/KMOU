package rbs.modules.trebook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 이북게시판관리에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface TrebookService {

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
	
	/**
	 * Min Year 
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getMinYear(int fnIdx, Map<String, Object> param);
	
	/**
	 * Con 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getConTotalCount(int fnIdx, Map<String, Object> param);
	
	/**
	 * Con 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public List<Object> getConList(int fnIdx, Map<String, Object> param);
	
	/**
	 * 파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getFileView(int fnIdx, Map<String, Object> param);
	
	/**
	 * 다운로드수 수정
	 * @param fnIdx
	 * @param treIdx
	 * @return
	 * @throws Exception
	 */
	public int updateDownload(int fnIdx, int treIdx) throws Exception;
	
	/**
	 * Con 파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getConFileView(int fnIdx, Map<String, Object> param);
	
	/**
	 * Con 다운로드수 수정
	 * @param fnIdx
	 * @param treIdx
	 * @return
	 * @throws Exception
	 */
	public int updateConDownload(int fnIdx, int treIdx, int conIdx) throws Exception;
	
	/**
	 * 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getView(int fnIdx, Map<String, Object> param);

	/**
	 * 조회수 수정
	 * @param fnIdx
	 * @param treIdx
	 * @return
	 * @throws Exception
	 */
	public int updateViews(int fnIdx, int treIdx) throws Exception;
	
	/**
	 * 권한여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getAuthCount(int fnIdx, Map<String, Object> param);
	
	/**
	 * 수정 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getModify(int fnIdx, Map<String, Object> param);
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param siteMode
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int insert(String uploadModulePath, int fnIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, JSONObject conFileItem) throws Exception;
	
	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param treIdx
	 * @param siteMode
	 * @param regiIp
	 * @param parameterMap
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int update(String uploadModulePath, int fnIdx, int treIdx, String siteMode, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, JSONObject conFileItem) throws Exception;

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
	 * @param siteMode
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int delete(int fnIdx, String[] deleteIdxs, String siteMode, String regiIp) throws Exception;
	
	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param fnIdx
	 * @param restoreIdxs
	 * @param siteMode
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int restore(int fnIdx, String[] restoreIdxs, String siteMode, String regiIp) throws Exception;

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
	public int cdelete(String uploadModulePath, int fnIdx, String[] deleteIdxs, JSONObject items, JSONArray itemOrder) throws Exception;
}