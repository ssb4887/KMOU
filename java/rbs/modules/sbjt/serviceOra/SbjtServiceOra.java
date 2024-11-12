package rbs.modules.sbjt.serviceOra;

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
public interface SbjtServiceOra {
	
	/**
	 * 최초 default 교과목 조회
	 * @return List<Object>
	 */
	public List<Object> getInitSbjtList(Map<String, Object> param) throws Exception;
	
	/**
	 * 최초 default 교과목 총 건 수
	 * @return int
	 */
	public DataMap getInitSbjtListCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 상세조회
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public DataMap getView(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 상세조회 핵심역량
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public DataMap getCoreView(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 상세조회 전공능력
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public DataMap getAbiView(Map<String, Object> param) throws Exception;
	
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
	 * 교과목 - 개설과목 목록
	 * @param empNo, year, smt
	 * @param param
	 * @return
	 */
	public List<Object> getCoursesOffered(Map<String, Object> param);
	
	/**
	 * 교과목 - 최근개설강좌 리스트
	 * @param subjectCd, deptCd, year
	 * @param param
	 * @return
	 */
	public List<Object> getLectureList(Map<String, Object> param);
	
	/**
	 * 교과목 - 개설강좌 리스트(메인페이지 장바구니용)
	 * @param subjectCd, deptCd, year
	 * @param param
	 * @return
	 */
	public List<Object> getClassList(Map<String, Object> param);
	
	/**
	 * 교과목 - 최근개설강좌 총 수
	 * @param empNo, year
	 * @param param
	 * @return
	 */
	public int getLectureCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 개설강좌 상세정보
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public DataMap getLectureView(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 핵심역량(서치API집어넣는용)
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public DataMap getCoreAbi(Map<String, String> param) throws Exception;
	
	/**
	 * 교과목 - 개설강좌 전공능력/핵심역량
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public List<Object> getLectureCore(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 개설강좌 전공능력/핵심역량
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public List<Object> getLectureAbi(Map<String, Object> param) throws Exception;
	
	
	/**
	 * 교과목 - 강의계획서
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public DataMap getPlanView(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 강의계획서(산학연연계)
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public List<Object> getCoreList(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 강의계획서(산학연연계)
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public List<Object> getAbiList(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 강의계획서(주차별 수업계획)
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public List<Object> getWeekList(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 강의계획서(교재 목록 수)
	 * @param param
	 * @return
	 */
	public int getBookCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 강의계획서(교재 목록)
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public List<Object> getBookList(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 강의계획서(산학연연계)
	 * @param subjectCd
	 * @param param
	 * @return
	 */
	public List<Object> getLinkList(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 강의계획서(산학연연계 수)
	 * @param param
	 * @return
	 */
	public int getLinkCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 강의평가 리스트
	 * @param empNo, year
	 * @param param
	 * @return
	 */
	public List<Object> getEvalList(Map<String, Object> param);
	
	/**
	 * 교과목 - 강의평가 총 수
	 * @param empNo, year
	 * @param param
	 * @return
	 */
	public int getEvalCount(Map<String, Object> param) throws Exception;
	
	/**
	 * 교과목 - 수강생통계
	 * @param empNo, year
	 * @param param
	 * @return
	 */
	public List<Object> getTotalStudent(Map<String, Object> param);
	
	
	/**
	 * 교과목 상세-직업/직무
	 * @param docId
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getJobCd(String docId) throws Exception;
	
	/**
	 * 교과목 상세-해시태그
	 * @param docId
	 * @param param
	 * @return
	 */
	public Map<String, Object> getPlanInfo(String docId) throws Exception;
	
	/**
	 * 나노디그리&매트릭스 상단정보
	 * @param 교과목코드
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getTrackList(Map<String, Object> param) throws Exception;
	
	/**
	 * 개설강좌-강의실 정보
	 * @param docId
	 * @param param
	 * @return
	 */
	public List<Object> getRoomInfo(List<Object> arr) throws Exception;
	
	
	/**
	 * 개설강좌-강의계획서
	 * @param docId
	 * @param param
	 * @return
	 */
	public Map<String, Object> getLessonPlan(String docId) throws Exception;
	
	/**
	 * 수강생통계
	 * @param 학번
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getStatList(Map<String, Object> param) throws Exception;

}