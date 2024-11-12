package rbs.modules.major.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 샘플모듈에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface MajorService {
	
	/**
	 * 최초 default 전공 목록 조회
	 * @return List<Object>
	 */
	public List<Object> getInitMajorList(Map<String, Object> param) throws Exception;
	
	/**
	 * 최초 default 전공 목록 총 건 수
	 * @return int
	 */
	public DataMap getInitMajorListCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 전공 - 상세조회
	 * @param majorCd
	 * @param param
	 * @return
	 */
	public DataMap getView(Map<String, Object> param) throws Exception;
	
	/**
	 * 전공 - 인재상
	 * @param majorCd
	 * @param param
	 * @return
	 */
	public List<Object> getMajorAbty(Map<String, Object> param) throws Exception;
	
	/**
	 * 전공 - 하위역량 유무 체크
	 * @param majorCd
	 * @param param
	 * @return
	 */
	public int checkAbty(Map<String, Object> param) throws Exception;
	
	/**
	 * 전공 - 전공능력(정의 및 하위역량)
	 * @param majorCd
	 * @param param
	 * @return
	 */
	public List<Object> getMajorAbtyDef(Map<String, Object> param) throws Exception;
	
	/**
	 * 전공 - 진출분야별 교육과정
	 * @param majorCd
	 * @param param
	 * @return
	 */
	public List<Object> getMajorSbjtList(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 직업 직무 코드명
	 * @param docId
	 * @param param
	 * @return
	 */
	public List<Object> getJobCdNm(Map<String, Object> jobparam) throws Exception;
	
	
	/**
	 * 관심교과목 북마크 등록 리스트
	 * @param docId
	 * @param param
	 * @return
	 */
	public List<Object> getCourBk(Map<String, Object> courParam) throws Exception;
	
	/**
	 *  관심교과목 북마크 등록 여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int bkCourCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 등록처리 : 관심교과목 북마크 등록
	 * @return
	 * @throws Exception
	 */
	public int insertBkCour(Map<String, Object> param, HttpServletRequest request) throws Exception;
	
	/**
	 * 등록처리 : 관심교과목 북마크 업데이트
	 * @return
	 * @throws Exception
	 */
	public int updateBkCour(Map<String, Object> param, HttpServletRequest request) throws Exception;
	
	/**
	 * 관심강좌 북마크 등록리스트
	 * @param docId
	 * @param param
	 * @return
	 */
	public List<Object> getLectureBk(Map<String, Object> lectureParam) throws Exception;
	
	/**
	 *  관심강좌 북마크 등록 여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int bkLectureCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 등록처리 : 관심강좌 북마크 등록
	 * @return
	 * @throws Exception
	 */
	public int insertBkLecture(Map<String, Object> param, HttpServletRequest request) throws Exception;
	
	/**
	 * 등록처리 : 관심강좌 북마크 업데이트
	 * @return
	 * @throws Exception
	 */
	public int updateBkLecture(Map<String, Object> param, HttpServletRequest request) throws Exception;
	
	/**
	 * 권한여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int getAuthCount(int fnIdx, Map<String, Object> param);
	
	
	}