package rbs.modules.prof.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 샘플 모듈에 관한 인터페이스클래스를 정의한다.
 * @author user
 *
 */
public interface ProfService {
	
	/**
	 * 최초 default 교수 조회
	 * @return List<Object>
	 */
	public List<Object> getInitProfList(Map<String, Object> param) throws Exception;
	
	/**
	 * 최초 default 교수 총 건 수
	 * @return int
	 */
	public DataMap getInitProfListCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 교수 - 상세조회
	 * @param empNo
	 * @param param
	 * @return
	 */
	public DataMap getView(Map<String, Object> param) throws Exception;
	
	/**
	 * 교수 - 연구번호조회
	 * @param empNo
	 * @param param
	 * @return
	 */
	public DataMap getProfSn(Map<String, Object> param) throws Exception;
	
	/**
	 * 교수 - 연구기록조회
	 * @param empNo
	 * @param param
	 * @return
	 */
	public DataMap getProfRsh(Map<String, Object> param) throws Exception;
	
	/**
	 * 소속 대학 리스트
	 * @author 석수빈
	 * @return
	 */
	public List<Object> getCollegeList() throws Exception;
	
	/**
	 * 소속 대학 - 학부(과) 리스트
	 * @author 석수빈
	 * @return
	 */
	public List<Object> getDepartList(Map<String, Object> param) throws Exception;
	
	/**
	 * 소속 대학 - 학부(과) - 전공 리스트
	 * @author 석수빈
	 * @return
	 */
	public List<Object> getMajorList(Map<String, Object> param) throws Exception;
	
	/**
	 * 교수 - 강의과목 리스트
	 * @param empNo, year
	 * @param param
	 * @return
	 */
	public List<Object> getList(Map<String, Object> param);
	
	/**
	 * 교수 - 강의과목 총 수
	 * @param empNo, year
	 * @param param
	 * @return
	 */
	public int getTotalCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 관심 교과목 북마크 등록 리스트
	 * @param docId
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getProfBk(List<Object> arr) throws Exception;
	
	/**
	 *  관심 교과목 북마크 등록 여부
	 * @param fnIdx
	 * @param param
	 * @return
	 */
	public int bkProfCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 등록 처리 : 관심 교과목 북마크 등록
	 * @return
	 * @throws Exception
	 */
	public int insertBkProf(Map<String, Object> param, HttpServletRequest request) throws Exception;
	
	/**
	 * 등록 처리 : 관심 교과목 북마크 업데이트
	 * @return
	 * @throws Exception
	 */
	public int updateBkProf(Map<String, Object> param, HttpServletRequest request) throws Exception;
}