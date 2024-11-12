package rbs.modules.statistics.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.woowonsoft.egovframework.form.DataMap;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 통계모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("statisticsMapper")
public class StatisticsMapper extends EgovAbstractMapper{

	/**
	 * 검색통계 > 검색 건수 추이
	 * @param param
	 * @return
	 */
	public List<Object> getSearchCountList(Map<String, Object> param) {
 	   return (List<Object>)selectList("rbs.modules.statistics.statisticsMapper.getSearhCountList", param);
    }
    
	/**
	 * 검색통계 > 검색 사용자 추이
	 * @param param
	 * @return
	 */
    public List<Object> getSearchUserList(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.statistics.statisticsMapper.getSearchUserList", param);
    }
    
    /**
     * 검색통계 > 검색 사용자 추이(학년별)
     * @param param
     * @return
     */
    public List<Object> getSearchGradeList(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.statistics.statisticsMapper.getSearchGradeList", param);
    }
    
    /**
     * 검색통계 > 검색어 통계
     * @param param
     * @return
     */
    public List<Object> getSearchKeywordList(Map<String, Object> param) {
    	return (List<Object>)selectList("rbs.modules.statistics.statisticsMapper.getSearchKeywordList", param);
    }
    
    /**
     * 검색통계 > 검색어 통계 갯수
     * @param param
     * @return
     */
    public int getSearchKeywordCount(Map<String, Object> param) {
    	return (int)selectOne("rbs.modules.statistics.statisticsMapper.getSearchKeywordCount", param);
    }
    
    
	/**
	 * 검색만족도 만족도 평균
	 * 조회
	 */
	public double selectSrchPointAvrg(Map<String, Object> param){
		return (Double) selectOne("rbs.modules.statistics.statisticsMapper.selectSrchPointAvrg", param);
	}

	/**
	 * 검색만족도 만족도 그래프
	 * 조회
	 */
	public List<Object> selectSrchPointGraph(Map<String, Object> param){
		return (List<Object>) selectList("rbs.modules.statistics.statisticsMapper.selectSrchPointGraph", param);
	}

	/**
	 * 검색만족도 만족도 구분 그래프
	 * 조회
	 */
	public List<Object> selectSrchPointTypeGraph(Map<String, Object> param){
		return (List<Object>) selectList("rbs.modules.statistics.statisticsMapper.selectSrchPointTypeGraph", param);
	}

	/**
	 * 검색만족도
	 * 조회
	 */
	public int selectSrchPointCount(Map<String, Object> param){
		return (Integer) selectOne("rbs.modules.statistics.statisticsMapper.selectSrchPointCount", param);
	}
	public List<Object> selectSrchPointList(Map<String, Object> param){
		return (List<Object>) selectList("rbs.modules.statistics.statisticsMapper.selectSrchPointList", param);
	}
	
	/**
	 * 통계 > 찜수 가져오기
	 * 조회
	 */
	public List<Object> selectBmk(Map<String, Object> param){
		return (List<Object>) selectList("rbs.modules.statistics.statisticsMapper.selectBmk", param);
	}

	/**
	 * 통계 > 교수 > 교수 인기 찜
	 * 조회
	 */
	public List<Map<String, Object>> selectProfMstBmk(Map<String, Object> param){
		return selectList("rbs.modules.statistics.statisticsMapper.selectProfMstBmk", param);
	}
	
	/**
	 * 통계 > 비교과> 비교과 인기 찜
	 * 조회
	 */
	public List<Map<String, Object>> selectNonSbjtMstBmk(Map<String, Object> param){
		return selectList("rbs.modules.statistics.statisticsMapper.selectNonSbjtMstBmk", param);
	}

	/**
	 * 통계 > 관심강좌 > 관심강좌 인기 찜
	 * 조회
	 */
	public List<Map<String, Object>> selectClsMstBmk(Map<String, Object> param){
		return selectList("rbs.modules.statistics.statisticsMapper.selectClsMstBmk", param);
	}

	/**
	 * 통계 > 전공 > 전공 인기 찜
	 * 조회
	 */
	public List<Map<String, Object>> selectMajorMstBmk(Map<String, Object> param){
		return selectList("rbs.modules.statistics.statisticsMapper.selectMajorMstBmk", param);
	}

	/**
	 * 통계 > 전공·교양 > 전공·교양 인기 찜
	 * 조회
	 */
	public List<Map<String, Object>> selectSbjtMstBmk(Map<String, Object> param) {
		return selectList("rbs.modules.statistics.statisticsMapper.selectSbjtMstBmk", param);
	}
	
	/**
	 * 통계 > 학생설계융합전공 > 학생설계융합전공 인기 찜
	 * 조회
	 */
	public List<Object> selectStudMstBmk(Map<String, Object> param){
		return (List<Object>) selectList("rbs.modules.statistics.statisticsMapper.selectStudMstBmk", param);
	}
	
	/**
	 * 통계 > 해시태그 > 해시태그수
	 * 조회
	 */
	public List<Object> selectHashtagCnt(Map<String, Object> param){
		return (List<Object>) selectList("rbs.modules.statistics.statisticsMapper.selectHashtagCnt", param);
	}

	/**
	 * 통계 > 해시태그 > 해시태그 사용자수
	 * 조회
	 */
	public List<Object> selectHashtagUsr(Map<String, Object> param){
		return (List<Object>) selectList("rbs.modules.statistics.statisticsMapper.selectHashtagUsr", param);
	}
	
	/**
	 * 통계 > 해시태그 > 해시태그 키워드 통계
	 * 조회
	 */
	public List<Object> selectHashtagStat(Map<String, Object> param){
		return (List<Object>) selectList("rbs.modules.statistics.statisticsMapper.selectHashtagStat", param);
	}
		
	

	    
}