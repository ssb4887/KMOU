package rbs.modules.statistics.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import rbs.egovframework.mapper.MartAbstractMapper;

/**
 * 통계모듈에 관한 데이터 접근 클래스를 정의한다.
 */
@Repository("statisticsOraMapper")
public class StatisticsOraMapper extends MartAbstractMapper{
	
	/**
	 * 찜 현황 > 교수이름 가져오기
	 * 조회
	 */
	public Map<String,Object> getProfName(Map<String, Object> oraParam){
		return selectOne("mart.modules.statistics.statisticsOraMapper.getProfName", oraParam);
	}

	/**
	 * 찜 현황 > 강좌이름 가져오기
	 * 조회
	 */
	public Map<String, Object> getClsName(Map<String, Object> oraParam) {
		return selectOne("mart.modules.statistics.statisticsOraMapper.getClsName", oraParam);
	}
	
	/**
	 * 찜 현황 > 전공이름 가져오기
	 * 조회
	 */
	public Map<String, Object> getMajorName(Map<String, Object> oraParam) {
		return selectOne("mart.modules.statistics.statisticsOraMapper.getMajorName", oraParam);
	}
	
	/**
	 * 찜 현황 > 교과목이름 가져오기
	 * 조회
	 */
	public Map<String, Object> getSbjtName(Map<String, Object> oraParam) {
		return selectOne("mart.modules.statistics.statisticsOraMapper.getSbjtName", oraParam);
	}




	    
}