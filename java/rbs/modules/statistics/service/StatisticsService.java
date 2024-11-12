package rbs.modules.statistics.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.woowonsoft.egovframework.form.DataMap;
import com.woowonsoft.egovframework.form.ParamForm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 통계모듈에 관한 인터페이스클래스를 정의한다.
 * @author 이동근
 *
 */
public interface StatisticsService {
	
	/**
	 * 검색 건수 추이
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Object> getSearchCountList(Map<String, Object> param) throws Exception;
	
	/**
	 * 검색 사용자 추이
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Object> getSearchUserList(Map<String, Object> param) throws Exception;
	
	/**
	 * 검색 사용자 추이(학년별)
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Object> getSearchGradeList(Map<String, Object> param) throws Exception;
	
	/**
	 * 검색어 통계
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Object> getSearchKeywordList(Map<String, Object> param) throws Exception;
	
	/**
	 * 검색어 통계 갯수
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int getSearchKeywordCount(Map<String, Object> param) throws Exception;
	
	
	/**
	 * 검색만족도 만족도 평균
	 * 조회
	 */
	public double selectSrchPointAvrg(Map<String, Object> param) throws Exception;

	/**
	 * 검색만족도 만족도 그래프
	 * 조회
	 */
	public List<Object> selectSrchPointGraph(Map<String, Object> param) throws Exception;

	/**
	 * 검색만족도 만족도 구분 그래프
	 * 조회
	 */
	public List<Object> selectSrchPointTypeGraph(Map<String, Object> param) throws Exception;

	/**
	 * 검색만족도
	 * 조회
	 */
	public int selectSrchPointCount(Map<String, Object> param) throws Exception;
	public List<Object> selectSrchPointList(Map<String, Object> param) throws Exception;	
	
	/**
	 * 통계 > 찜수
	 * 조회
	 */
	public List<Object> selectBmk(Map<String, Object> param) throws Exception;
	
	/**
	 * 통계 > 교수 > 교수 인기 찜
	 * 조회
	 */
	public List<Object> selectProfMstBmk(Map<String, Object> param) throws Exception;
	
	/**
	 * 통계 > 비교과 > 비교과 인기 찜
	 * 조회
	 */
	public List<Object> selectNonSbjtMstBmk(Map<String, Object> param) throws Exception;

	/**
	 * 통계 > 관심강좌 > 관심강좌 인기 찜
	 * 조회
	 */
	public List<Object> selectClsMstBmk(Map<String, Object> param) throws Exception;

	/**
	 * 통계 > 전공 > 전공 인기 찜
	 * 조회
	 */
	public List<Object> selectMajorMstBmk(Map<String, Object> param) throws Exception;
	
	/**
	 * 통계 > 전공 > 전공 인기 찜
	 * 조회
	 */
	public List<Object> selectSbjtMstBmk(Map<String, Object> param) throws Exception;

	/**
	 * 통계 > 학생설계융합전공 > 학생설계융합전공 인기 찜
	 * 조회
	 */
	public List<Object> selectStudMstBmk(Map<String, Object> param) throws Exception;	
	
	/**
	 * 통계 > 해시태그 > 해시태그수
	 * 조회
	 */
	public List<Object> selectHashtagCnt(Map<String, Object> param) throws Exception;
	
	/**
	 * 통계 > 해시태그 > 해시태그 사용자수
	 * 조회
	 */
	public List<Object> selectHashtagUsr(Map<String, Object> param) throws Exception;
	
	/**
	 * 통계 > 해시태그 > 해시태그 키워드 통계
	 * 조회
	 */
	public List<Object> selectHashtagStat(Map<String, Object> param) throws Exception;
	

	
}