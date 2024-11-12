package rbs.modules.majorInfo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DTForm;
import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 샘플모듈에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface MajorInfoService {
	
	/**
	 * 전체 목록 수
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getTotalCount(int fnIdx, Map<String, Object> param);
	public int getDeptCount(int fnIdx, Map<String, Object> param);
	public int getTrackCount(int fnIdx, Map<String, Object> param);
	public int getAbilityCount(int fnIdx, Map<String, Object> param);
	public int getMajorCount(int fnIdx, Map<String, Object> param);
	public int getMajorEtcCount(int fnIdx, Map<String, Object> param);
	public int getMajorYearCount(int fnIdx, Map<String, Object> param);
	
	/**
	 * 전체 목록
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public List<Object> getList(int fnIdx, Map<String, Object> param);
	public List<Object> getDeptList(int fnIdx, Map<String, Object> param);
	
	public List<Object> getTalentAbtyList(Map<String, Object> param);
	public List<Object> getAbtyMngList(Map<String, Object> param);
	
	public List<Object> getFieldList(Map<String, Object> param);
	public List<Object> getNonSbjtList(Map<String, Object> param);
	public List<Object> getLicenseList( Map<String, Object> param);
	
	public List<Object> getTrackList(int fnIdx, Map<String, Object> param);
	public List<Object> getAbilityList(int fnIdx, Map<String, Object> param);
	public List<Object> getDtlList(int fnIdx, Map<String, Object> param);
	public List<Object> getCourMajorList(int fnIdx, Map<String, Object> param);
	public List<Object> getAddMajorList(int fnIdx, Map<String, Object> param);
	public List<Object> getRcmdCultList(int fnIdx, Map<String, Object> param);
	public List<Object> getHaksaRcmdCultList(int fnIdx, Map<String, Object> param);
	public List<Object> insertAddMajorList(int fnIdx, Map<String, Object> param);
	
	/**
	 * 주관 대학 리스트
	 * @author 석수빈
	 * @return
	 */
	public List<Object> getCollegeList() throws Exception;
	
	/**
	 * 주관 대학 - 학부(과) 리스트
	 * @author 석수빈
	 * @return
	 */
	public List<Object> getDepartList(Map<String, Object> param) throws Exception;
	
	/**
	 * 주관 대학 - 학부(과) - 전공 리스트
	 * @author 석수빈
	 * @return
	 */
	public List<Object> getMajorList(Map<String, Object> param) throws Exception;
	
	/**
	 * 새로운 학과/학부 등록
	 * @return
	 * @throws Exception
	 */
	public int insertDept(Map<String, Object> param) throws Exception;
	
	

	/**
	 * 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getView(int fnIdx, Map<String, Object> param);
	
	/**
	 * 파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getFileView(int fnIdx, Map<String, Object> param);
	
	/**
	 * multi파일 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getMultiFileView(int fnIdx, Map<String, Object> param);
	
	/**
	 * 권한여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getAuthCount(int fnIdx, Map<String, Object> param);
	
	/**
	 * 다운로드 수 수정
	 * @param fnIdx
	 * @param brdIdx
	 * @return
	 * @throws Exception
	 */
	public int updateFileDown(int fnIdx, int keyMajorIdx, int keyYear, String fileColumnName) throws Exception;
	
	/**
	 * multi file 다운로드 수 수정
	 * @param fnIdx
	 * @param brdIdx
	 * @return
	 * @throws Exception
	 */
	public int updateMultiFileDown(int fnIdx, int keyIdx, int fleIdx, String itemId) throws Exception;
		
	/**
	 * 수정 상세조회
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public DataMap getModify(int fnIdx, Map<String, Object> param);
	public DataMap getDeptModify(int fnIdx, Map<String, Object> param);
	public DataMap getModifyTrack(int fnIdx, Map<String, Object> param);
	public DataMap getModifyAbility(int fnIdx, Map<String, Object> param);
	
	
	/**
	 * 등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int insert(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	public int insertYear(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx) throws Exception;
	public int insertStatistic(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx) throws Exception;
	public int insertTrack(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx) throws Exception;
	public int insertJob(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx) throws Exception;
	public int insertDtl(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int jobIdx) throws Exception;
	public int insertRcmdCult(String rawBody, String regiIp) throws Exception;
	
	public int deleteAndInsertNonSbjt(List<DTForm> searchList, String remoteAddr, ParamForm parameterMap) throws Exception;
	public int deleteAndInsertLicense(List<DTForm> searchList, String remoteAddr, ParamForm parameterMap) throws Exception;
	
	public int deleteDtl(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int jobIdx) throws Exception;
	
	public int copyYear(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx) throws Exception;
	public int copyStatistic(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int copyYearIdx) throws Exception;
	public int copyJob(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int copyYearIdx) throws Exception;
	public int copyTrack(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int copyYearIdx) throws Exception;
	public int copyDtl(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder, int majorIdx, int yearIdx, int copyYearIdx) throws Exception;
	
	public int abtyInsert(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	
	/**
	 * 수정처리 : 화면에 조회된 정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param keyMajorIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int update(String uploadModulePath, int fnIdx, Map<String,Object> param, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	public int deptUpdate(String uploadModulePath, int fnIdx, Map<String,Object> param, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	public int updateYear(String uploadModulePath, int fnIdx, int keyMajorIdx, int keyYearIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	public int updateStatistic(String uploadModulePath, int fnIdx, int keyMajorIdx, int keyYearIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	public int updateTrack(String uploadModulePath, int fnIdx, int keyMajorIdx, int keyYearIdx, int keyTrackIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	public int updateJob(String uploadModulePath, int fnIdx, int keyMajorIdx, int keyYearIdx, int keyJobIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	
	public int abtyUpdate(String uploadModulePath, int fnIdx, Map<String,Object> param, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	
	/**
	 * 삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제 flag 반영
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param siteMode
	 * @param regiIp
	 * @return
	 * @throws Exception
	 */
	public int abtyDelete(ParamForm parameterMap, JSONObject settingInfo, int fnIdx, int[] deleteIdxs, String siteMode, String regiIp) throws Exception;
	
	
	/**
	 * 추천균형교양교과목 중복 체크
	 * @param rawBody
	 * @return
	 * @throws Exception
	 */
	public int getCourCultCount(String rawBody) throws Exception;

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
	 * @param parameterMap
	 * @param deleteIdxs
	 * @param regiIp
	 * @param settingInfo
	 * @return
	 * @throws Exception
	 */
	public int delete(int fnIdx, ParamForm parameterMap, int[] deleteIdxs, String regiIp, JSONObject settingInfo) throws Exception;
	public int deleteTrack(int fnIdx, ParamForm parameterMap, int majorIdx, int yearIdx, int trackIdx, String regiIp, JSONObject settingInfo) throws Exception;
	
	/**
	 * 복원처리 : 화면에 조회된 정보를 데이터베이스에서 복원 flag 반영
	 * @param fnIdx
	 * @param restoreIdxs
	 * @param regiIp
	 * @param settingInfo
	 * @return
	 * @throws Exception
	 */
	public int restore(int fnIdx, int[] restoreIdxs, String regiIp, JSONObject settingInfo) throws Exception;

	/**
	 * 완전삭제처리 : 화면에 조회된 정보를 데이터베이스에서 삭제
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param deleteIdxs
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public int cdelete(String uploadModulePath, int fnIdx, int[] deleteIdxs, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;

	/**
	 * mult file 전체 목록 : 항목ID에 대한 HashMap
	 * @param fnIdx
	 * @param keyIdx
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 */
	public HashMap<String, Object> getMultiFileHashMap(int fnIdx, int keyIdx, JSONObject settingInfo, JSONObject items, JSONArray itemOrder);
	
	/**
	 * mult data 전체 목록 : 항목ID에 대한 HashMap
	 * @param fnIdx
	 * @param keyIdx
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 */
	public HashMap<String, Object> getMultiHashMap(int fnIdx, int brdIdx, JSONObject items, JSONArray itemOrder);
	
	/**
	 * 파일등록처리 : 정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param uploadModulePath
	 * @param fnIdx
	 * @param regiIp
	 * @param parameterMap
	 * @param settingInfo
	 * @param items
	 * @param itemOrder
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getFileUpload(String uploadModulePath, int fnIdx, String regiIp, ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	
	/**
	 * 전공별 등록된 기본정보의 연도 가져오기
	 * @param mjCd 
	 * @param MJ_CD
	 * @return YY
	 */
	public List<Object> getRegisteredYear(String mjCd) throws Exception;
	
	/**
	 * 전공능력 정보 인서트
	 * @param searchList
	 * @param remoteAddr
	 * @param parameterMap
	 * @return
	 * @throws Exception
	 */
	public int deleteAndInsertMajorAbility(List<DTForm> searchList, String remoteAddr, ParamForm parameterMap) throws Exception;
	public DataMap getModifyCourMajor(int fnIdx, Map<String, Object> param);
	public int updateCourMajor(String uploadModulePath, int fnIdx, String uploadModulePath2, int fnIdx2, HashMap<String, Object> param, String remoteAddr,
			ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	public int updateRcmdCult(String uploadModulePath, int fnIdx, String uploadModulePath2, int fnIdx2, HashMap<String, Object> param, String remoteAddr,
			ParamForm parameterMap, JSONObject settingInfo, JSONObject items, JSONArray itemOrder) throws Exception;
	public List<Object> getFrontCourMajorList(int fnIdx, Map<String, Object> param) throws Exception;
	
	/**
	 * 마이크로 전공
	 * @param MJ_CD
	 * @return YY
	 */
	public List<Object> getMicroMajorList(Map<String, Object> param) throws Exception;
	
	/**
	 * 마이크로 전공 교과목
	 * @param MCM_CD
	 * @return YY
	 */
	public List<Object> getMicroMajorSubjectList(Map<String, Object> param) throws Exception;
	public List<Object> getMjCdList(int fnIdx, Map<String, Object> findParam);
	public int copyMajorInfo(Map<String, Object> param);
	public List<Object> getFrontRcmdCultList(int fnIdx, Map<String, Object> param);
	public List<Object> getPermSustCdList(Map<String, Object> param);
}